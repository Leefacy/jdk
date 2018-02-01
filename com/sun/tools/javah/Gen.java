/*     */ package com.sun.tools.javah;
/*     */ 
/*     */ import java.io.ByteArrayOutputStream;
/*     */ import java.io.FileNotFoundException;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.OutputStream;
/*     */ import java.io.OutputStreamWriter;
/*     */ import java.io.PrintWriter;
/*     */ import java.io.UnsupportedEncodingException;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Set;
/*     */ import java.util.Stack;
/*     */ import javax.annotation.processing.ProcessingEnvironment;
/*     */ import javax.lang.model.element.ExecutableElement;
/*     */ import javax.lang.model.element.Modifier;
/*     */ import javax.lang.model.element.Name;
/*     */ import javax.lang.model.element.TypeElement;
/*     */ import javax.lang.model.element.VariableElement;
/*     */ import javax.lang.model.type.TypeMirror;
/*     */ import javax.lang.model.util.ElementFilter;
/*     */ import javax.lang.model.util.Elements;
/*     */ import javax.lang.model.util.Types;
/*     */ import javax.tools.FileObject;
/*     */ import javax.tools.JavaFileManager;
/*     */ import javax.tools.JavaFileObject;
/*     */ import javax.tools.StandardLocation;
/*     */ 
/*     */ public abstract class Gen
/*     */ {
/*  71 */   protected String lineSep = System.getProperty("line.separator");
/*     */   protected ProcessingEnvironment processingEnvironment;
/*     */   protected Types types;
/*     */   protected Elements elems;
/*     */   protected Mangle mangler;
/*     */   protected Util util;
/*     */   protected Set<TypeElement> classes;
/*  88 */   private static final boolean isWindows = System.getProperty("os.name")
/*  88 */     .startsWith("Windows");
/*     */   protected JavaFileManager fileManager;
/*     */   protected JavaFileObject outFile;
/* 132 */   protected boolean force = false;
/*     */ 
/*     */   protected Gen(Util paramUtil)
/*     */   {
/*  80 */     this.util = paramUtil;
/*     */   }
/*     */ 
/*     */   protected abstract void write(OutputStream paramOutputStream, TypeElement paramTypeElement)
/*     */     throws Util.Exit;
/*     */ 
/*     */   protected abstract String getIncludes();
/*     */ 
/*     */   public void setFileManager(JavaFileManager paramJavaFileManager)
/*     */   {
/* 110 */     this.fileManager = paramJavaFileManager;
/*     */   }
/*     */ 
/*     */   public void setOutFile(JavaFileObject paramJavaFileObject) {
/* 114 */     this.outFile = paramJavaFileObject;
/*     */   }
/*     */ 
/*     */   public void setClasses(Set<TypeElement> paramSet)
/*     */   {
/* 119 */     this.classes = paramSet;
/*     */   }
/*     */ 
/*     */   void setProcessingEnvironment(ProcessingEnvironment paramProcessingEnvironment) {
/* 123 */     this.processingEnvironment = paramProcessingEnvironment;
/* 124 */     this.elems = paramProcessingEnvironment.getElementUtils();
/* 125 */     this.types = paramProcessingEnvironment.getTypeUtils();
/* 126 */     this.mangler = new Mangle(this.elems, this.types);
/*     */   }
/*     */ 
/*     */   public void setForce(boolean paramBoolean)
/*     */   {
/* 135 */     this.force = paramBoolean;
/*     */   }
/*     */ 
/*     */   protected PrintWriter wrapWriter(OutputStream paramOutputStream)
/*     */     throws Util.Exit
/*     */   {
/*     */     try
/*     */     {
/* 144 */       return new PrintWriter(new OutputStreamWriter(paramOutputStream, "ISO8859_1"), true);
/*     */     } catch (UnsupportedEncodingException localUnsupportedEncodingException) {
/* 146 */       this.util.bug("encoding.iso8859_1.not.found");
/* 147 */     }return null;
/*     */   }
/*     */ 
/*     */   public void run()
/*     */     throws IOException, ClassNotFoundException, Util.Exit
/*     */   {
/* 159 */     int i = 0;
/*     */     Object localObject1;
/*     */     Object localObject2;
/*     */     Object localObject3;
/* 160 */     if (this.outFile != null)
/*     */     {
/* 162 */       localObject1 = new ByteArrayOutputStream(8192);
/* 163 */       writeFileTop((OutputStream)localObject1);
/*     */ 
/* 165 */       for (localObject2 = this.classes.iterator(); ((Iterator)localObject2).hasNext(); ) { localObject3 = (TypeElement)((Iterator)localObject2).next();
/* 166 */         write((OutputStream)localObject1, (TypeElement)localObject3);
/*     */       }
/*     */ 
/* 169 */       writeIfChanged(((ByteArrayOutputStream)localObject1).toByteArray(), this.outFile);
/*     */     }
/*     */     else {
/* 172 */       for (localObject1 = this.classes.iterator(); ((Iterator)localObject1).hasNext(); ) { localObject2 = (TypeElement)((Iterator)localObject1).next();
/* 173 */         localObject3 = new ByteArrayOutputStream(8192);
/* 174 */         writeFileTop((OutputStream)localObject3);
/* 175 */         write((OutputStream)localObject3, (TypeElement)localObject2);
/* 176 */         writeIfChanged(((ByteArrayOutputStream)localObject3).toByteArray(), getFileObject(((TypeElement)localObject2).getQualifiedName()));
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   private void writeIfChanged(byte[] paramArrayOfByte, FileObject paramFileObject)
/*     */     throws IOException
/*     */   {
/* 187 */     int i = 0;
/* 188 */     String str = "[No need to update file ";
/*     */     Object localObject;
/* 190 */     if (this.force) {
/* 191 */       i = 1;
/* 192 */       str = "[Forcefully writing file ";
/*     */     }
/*     */     else
/*     */     {
/*     */       try
/*     */       {
/* 200 */         localObject = paramFileObject.openInputStream();
/* 201 */         byte[] arrayOfByte = readBytes((InputStream)localObject);
/* 202 */         if (!Arrays.equals(arrayOfByte, paramArrayOfByte)) {
/* 203 */           i = 1;
/* 204 */           str = "[Overwriting file ";
/*     */         }
/*     */       }
/*     */       catch (FileNotFoundException localFileNotFoundException) {
/* 208 */         i = 1;
/* 209 */         str = "[Creating file ";
/*     */       }
/*     */     }
/*     */ 
/* 213 */     if (this.util.verbose) {
/* 214 */       this.util.log(str + paramFileObject + "]");
/*     */     }
/* 216 */     if (i != 0) {
/* 217 */       localObject = paramFileObject.openOutputStream();
/* 218 */       ((OutputStream)localObject).write(paramArrayOfByte);
/* 219 */       ((OutputStream)localObject).close();
/*     */     }
/*     */   }
/*     */ 
/*     */   protected byte[] readBytes(InputStream paramInputStream) throws IOException {
/*     */     try {
/* 225 */       byte[] arrayOfByte1 = new byte[paramInputStream.available() + 1];
/* 226 */       int i = 0;
/*     */       int j;
/* 228 */       while ((j = paramInputStream.read(arrayOfByte1, i, arrayOfByte1.length - i)) != -1) {
/* 229 */         i += j;
/* 230 */         if (i == arrayOfByte1.length) {
/* 231 */           arrayOfByte1 = Arrays.copyOf(arrayOfByte1, arrayOfByte1.length * 2);
/*     */         }
/*     */       }
/* 234 */       return Arrays.copyOf(arrayOfByte1, i);
/*     */     } finally {
/* 236 */       paramInputStream.close();
/*     */     }
/*     */   }
/*     */ 
/*     */   protected String defineForStatic(TypeElement paramTypeElement, VariableElement paramVariableElement) throws Util.Exit
/*     */   {
/* 242 */     Name localName1 = paramTypeElement.getQualifiedName();
/* 243 */     Name localName2 = paramVariableElement.getSimpleName();
/*     */ 
/* 245 */     String str1 = this.mangler.mangle(localName1, 1);
/* 246 */     String str2 = this.mangler.mangle(localName2, 2);
/*     */ 
/* 248 */     if (!paramVariableElement.getModifiers().contains(Modifier.STATIC)) {
/* 249 */       this.util.bug("tried.to.define.non.static");
/*     */     }
/* 251 */     if (paramVariableElement.getModifiers().contains(Modifier.FINAL)) {
/* 252 */       Object localObject = null;
/*     */ 
/* 254 */       localObject = paramVariableElement.getConstantValue();
/*     */ 
/* 256 */       if (localObject != null) {
/* 257 */         String str3 = null;
/* 258 */         if (((localObject instanceof Integer)) || ((localObject instanceof Byte)) || ((localObject instanceof Short)))
/*     */         {
/* 262 */           str3 = localObject.toString() + "L";
/* 263 */         } else if ((localObject instanceof Boolean)) {
/* 264 */           str3 = ((Boolean)localObject).booleanValue() ? "1L" : "0L";
/* 265 */         } else if ((localObject instanceof Character)) {
/* 266 */           Character localCharacter = (Character)localObject;
/* 267 */           str3 = String.valueOf(localCharacter.charValue() & 0xFFFF) + "L";
/* 268 */         } else if ((localObject instanceof Long))
/*     */         {
/* 270 */           if (isWindows)
/* 271 */             str3 = localObject.toString() + "i64";
/*     */           else
/* 273 */             str3 = localObject.toString() + "LL";
/* 274 */         } else if ((localObject instanceof Float))
/*     */         {
/* 276 */           float f = ((Float)localObject).floatValue();
/* 277 */           if (Float.isInfinite(f))
/* 278 */             str3 = (f < 0.0F ? "-" : "") + "Inff";
/*     */           else
/* 280 */             str3 = localObject.toString() + "f";
/* 281 */         } else if ((localObject instanceof Double))
/*     */         {
/* 283 */           double d = ((Double)localObject).doubleValue();
/* 284 */           if (Double.isInfinite(d))
/* 285 */             str3 = (d < 0.0D ? "-" : "") + "InfD";
/*     */           else
/* 287 */             str3 = localObject.toString();
/*     */         }
/* 289 */         if (str3 != null) {
/* 290 */           StringBuilder localStringBuilder = new StringBuilder("#undef ");
/* 291 */           localStringBuilder.append(str1); localStringBuilder.append("_"); localStringBuilder.append(str2); localStringBuilder.append(this.lineSep);
/* 292 */           localStringBuilder.append("#define "); localStringBuilder.append(str1); localStringBuilder.append("_");
/* 293 */           localStringBuilder.append(str2); localStringBuilder.append(" "); localStringBuilder.append(str3);
/* 294 */           return localStringBuilder.toString();
/*     */         }
/*     */       }
/*     */     }
/*     */ 
/* 299 */     return null;
/*     */   }
/*     */ 
/*     */   protected String cppGuardBegin()
/*     */   {
/* 306 */     return "#ifdef __cplusplus" + this.lineSep + "extern \"C\" {" + this.lineSep + "#endif";
/*     */   }
/*     */ 
/*     */   protected String cppGuardEnd() {
/* 310 */     return "#ifdef __cplusplus" + this.lineSep + "}" + this.lineSep + "#endif";
/*     */   }
/*     */ 
/*     */   protected String guardBegin(String paramString) {
/* 314 */     return "/* Header for class " + paramString + " */" + this.lineSep + this.lineSep + "#ifndef _Included_" + paramString + this.lineSep + "#define _Included_" + paramString;
/*     */   }
/*     */ 
/*     */   protected String guardEnd(String paramString)
/*     */   {
/* 320 */     return "#endif";
/*     */   }
/*     */ 
/*     */   protected void writeFileTop(OutputStream paramOutputStream)
/*     */     throws Util.Exit
/*     */   {
/* 327 */     PrintWriter localPrintWriter = wrapWriter(paramOutputStream);
/* 328 */     localPrintWriter.println("/* DO NOT EDIT THIS FILE - it is machine generated */" + this.lineSep + 
/* 329 */       getIncludes());
/*     */   }
/*     */ 
/*     */   protected String baseFileName(CharSequence paramCharSequence) {
/* 333 */     return this.mangler.mangle(paramCharSequence, 1);
/*     */   }
/*     */ 
/*     */   protected FileObject getFileObject(CharSequence paramCharSequence) throws IOException {
/* 337 */     String str = baseFileName(paramCharSequence) + getFileSuffix();
/* 338 */     return this.fileManager.getFileForOutput(StandardLocation.SOURCE_OUTPUT, "", str, null);
/*     */   }
/*     */ 
/*     */   protected String getFileSuffix() {
/* 342 */     return ".h";
/*     */   }
/*     */ 
/*     */   List<VariableElement> getAllFields(TypeElement paramTypeElement)
/*     */   {
/* 350 */     ArrayList localArrayList = new ArrayList();
/* 351 */     Object localObject = null;
/* 352 */     Stack localStack = new Stack();
/*     */ 
/* 354 */     localObject = paramTypeElement;
/*     */     while (true) {
/* 356 */       localStack.push(localObject);
/* 357 */       TypeElement localTypeElement = (TypeElement)this.types.asElement(((TypeElement)localObject).getSuperclass());
/* 358 */       if (localTypeElement == null)
/*     */         break;
/* 360 */       localObject = localTypeElement;
/*     */     }
/*     */ 
/* 363 */     while (!localStack.empty()) {
/* 364 */       localObject = (TypeElement)localStack.pop();
/* 365 */       localArrayList.addAll(ElementFilter.fieldsIn(((TypeElement)localObject).getEnclosedElements()));
/*     */     }
/*     */ 
/* 368 */     return localArrayList;
/*     */   }
/*     */ 
/*     */   String signature(ExecutableElement paramExecutableElement)
/*     */   {
/* 373 */     StringBuilder localStringBuilder = new StringBuilder("(");
/* 374 */     String str = "";
/* 375 */     for (VariableElement localVariableElement : paramExecutableElement.getParameters()) {
/* 376 */       localStringBuilder.append(str);
/* 377 */       localStringBuilder.append(this.types.erasure(localVariableElement.asType()).toString());
/* 378 */       str = ",";
/*     */     }
/* 380 */     localStringBuilder.append(")");
/* 381 */     return localStringBuilder.toString();
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.javah.Gen
 * JD-Core Version:    0.6.2
 */