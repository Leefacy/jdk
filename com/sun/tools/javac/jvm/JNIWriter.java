/*     */ package com.sun.tools.javac.jvm;
/*     */ 
/*     */ import com.sun.tools.javac.code.Attribute.Compound;
/*     */ import com.sun.tools.javac.code.Scope;
/*     */ import com.sun.tools.javac.code.Scope.Entry;
/*     */ import com.sun.tools.javac.code.Symbol;
/*     */ import com.sun.tools.javac.code.Symbol.ClassSymbol;
/*     */ import com.sun.tools.javac.code.Symtab;
/*     */ import com.sun.tools.javac.code.Type;
/*     */ import com.sun.tools.javac.main.Option;
/*     */ import com.sun.tools.javac.model.JavacElements;
/*     */ import com.sun.tools.javac.model.JavacTypes;
/*     */ import com.sun.tools.javac.util.Assert;
/*     */ import com.sun.tools.javac.util.Context;
/*     */ import com.sun.tools.javac.util.Context.Key;
/*     */ import com.sun.tools.javac.util.Log;
/*     */ import com.sun.tools.javac.util.Options;
/*     */ import java.io.IOException;
/*     */ import java.io.PrintStream;
/*     */ import java.io.Writer;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Set;
/*     */ import java.util.Stack;
/*     */ import java.util.StringTokenizer;
/*     */ import javax.lang.model.element.Element;
/*     */ import javax.lang.model.element.ExecutableElement;
/*     */ import javax.lang.model.element.Modifier;
/*     */ import javax.lang.model.element.TypeElement;
/*     */ import javax.lang.model.element.VariableElement;
/*     */ import javax.lang.model.type.ArrayType;
/*     */ import javax.lang.model.type.DeclaredType;
/*     */ import javax.lang.model.type.NoType;
/*     */ import javax.lang.model.type.PrimitiveType;
/*     */ import javax.lang.model.type.TypeKind;
/*     */ import javax.lang.model.type.TypeMirror;
/*     */ import javax.lang.model.type.TypeVariable;
/*     */ import javax.lang.model.type.TypeVisitor;
/*     */ import javax.lang.model.util.ElementFilter;
/*     */ import javax.lang.model.util.Elements;
/*     */ import javax.lang.model.util.SimpleTypeVisitor8;
/*     */ import javax.lang.model.util.Types;
/*     */ import javax.tools.FileObject;
/*     */ import javax.tools.JavaFileManager;
/*     */ import javax.tools.StandardLocation;
/*     */ 
/*     */ public class JNIWriter
/*     */ {
/*  81 */   protected static final Context.Key<JNIWriter> jniWriterKey = new Context.Key();
/*     */   private final JavaFileManager fileManager;
/*     */   JavacElements elements;
/*     */   JavacTypes types;
/*     */   private final Log log;
/*     */   private boolean verbose;
/*     */   private boolean checkAll;
/*     */   private Mangle mangler;
/*     */   private Context context;
/*     */   private Symtab syms;
/*     */   private String lineSep;
/* 111 */   private final boolean isWindows = System.getProperty("os.name")
/* 111 */     .startsWith("Windows");
/*     */ 
/*     */   public static JNIWriter instance(Context paramContext)
/*     */   {
/* 115 */     JNIWriter localJNIWriter = (JNIWriter)paramContext.get(jniWriterKey);
/* 116 */     if (localJNIWriter == null)
/* 117 */       localJNIWriter = new JNIWriter(paramContext);
/* 118 */     return localJNIWriter;
/*     */   }
/*     */ 
/*     */   private JNIWriter(Context paramContext)
/*     */   {
/* 124 */     paramContext.put(jniWriterKey, this);
/* 125 */     this.fileManager = ((JavaFileManager)paramContext.get(JavaFileManager.class));
/* 126 */     this.log = Log.instance(paramContext);
/*     */ 
/* 128 */     Options localOptions = Options.instance(paramContext);
/* 129 */     this.verbose = localOptions.isSet(Option.VERBOSE);
/* 130 */     this.checkAll = localOptions.isSet("javah:full");
/*     */ 
/* 132 */     this.context = paramContext;
/* 133 */     this.syms = Symtab.instance(paramContext);
/*     */ 
/* 135 */     this.lineSep = System.getProperty("line.separator");
/*     */   }
/*     */ 
/*     */   private void lazyInit() {
/* 139 */     if (this.mangler == null) {
/* 140 */       this.elements = JavacElements.instance(this.context);
/* 141 */       this.types = JavacTypes.instance(this.context);
/* 142 */       this.mangler = new Mangle(this.elements, this.types);
/*     */     }
/*     */   }
/*     */ 
/*     */   public boolean needsHeader(Symbol.ClassSymbol paramClassSymbol) {
/* 147 */     if ((paramClassSymbol.isLocal()) || ((paramClassSymbol.flags() & 0x1000) != 0L)) {
/* 148 */       return false;
/*     */     }
/* 150 */     if (this.checkAll) {
/* 151 */       return needsHeader(paramClassSymbol.outermostClass(), true);
/*     */     }
/* 153 */     return needsHeader(paramClassSymbol, false);
/*     */   }
/*     */ 
/*     */   private boolean needsHeader(Symbol.ClassSymbol paramClassSymbol, boolean paramBoolean) {
/* 157 */     if ((paramClassSymbol.isLocal()) || ((paramClassSymbol.flags() & 0x1000) != 0L)) {
/* 158 */       return false;
/*     */     }
/* 160 */     for (Scope.Entry localEntry = paramClassSymbol.members_field.elems; localEntry != null; localEntry = localEntry.sibling) {
/* 161 */       if ((localEntry.sym.kind == 16) && ((localEntry.sym.flags() & 0x100) != 0L))
/* 162 */         return true;
/* 163 */       for (Attribute.Compound localCompound : localEntry.sym.getDeclarationAttributes()) {
/* 164 */         if (localCompound.type.tsym == this.syms.nativeHeaderType.tsym)
/* 165 */           return true;
/*     */       }
/*     */     }
/* 168 */     if (paramBoolean) {
/* 169 */       for (localEntry = paramClassSymbol.members_field.elems; localEntry != null; localEntry = localEntry.sibling) {
/* 170 */         if ((localEntry.sym.kind == 2) && (needsHeader((Symbol.ClassSymbol)localEntry.sym, true)))
/* 171 */           return true;
/*     */       }
/*     */     }
/* 174 */     return false;
/*     */   }
/*     */ 
/*     */   public FileObject write(Symbol.ClassSymbol paramClassSymbol)
/*     */     throws IOException
/*     */   {
/* 183 */     String str = paramClassSymbol.flatName().toString();
/*     */ 
/* 185 */     FileObject localFileObject = this.fileManager
/* 185 */       .getFileForOutput(StandardLocation.NATIVE_HEADER_OUTPUT, "", str
/* 186 */       .replaceAll("[.$]", "_") + 
/* 186 */       ".h", null);
/* 187 */     Writer localWriter = localFileObject.openWriter();
/*     */     try {
/* 189 */       write(localWriter, paramClassSymbol);
/* 190 */       if (this.verbose)
/* 191 */         this.log.printVerbose("wrote.file", new Object[] { localFileObject });
/* 192 */       localWriter.close();
/* 193 */       localWriter = null;
/*     */     } finally {
/* 195 */       if (localWriter != null)
/*     */       {
/* 197 */         localWriter.close();
/* 198 */         localFileObject.delete();
/* 199 */         localFileObject = null;
/*     */       }
/*     */     }
/* 202 */     return localFileObject;
/*     */   }
/*     */ 
/*     */   public void write(Writer paramWriter, Symbol.ClassSymbol paramClassSymbol) throws IOException
/*     */   {
/* 207 */     lazyInit();
/*     */     try {
/* 209 */       String str = this.mangler.mangle(paramClassSymbol.fullname, 1);
/* 210 */       println(paramWriter, fileTop());
/* 211 */       println(paramWriter, includes());
/* 212 */       println(paramWriter, guardBegin(str));
/* 213 */       println(paramWriter, cppGuardBegin());
/*     */ 
/* 215 */       writeStatics(paramWriter, paramClassSymbol);
/* 216 */       writeMethods(paramWriter, paramClassSymbol, str);
/*     */ 
/* 218 */       println(paramWriter, cppGuardEnd());
/* 219 */       println(paramWriter, guardEnd(str));
/*     */     } catch (JNIWriter.TypeSignature.SignatureException localSignatureException) {
/* 221 */       throw new IOException(localSignatureException);
/*     */     }
/*     */   }
/*     */ 
/*     */   protected void writeStatics(Writer paramWriter, Symbol.ClassSymbol paramClassSymbol) throws IOException {
/* 226 */     List localList = getAllFields(paramClassSymbol);
/*     */ 
/* 228 */     for (VariableElement localVariableElement : localList)
/* 229 */       if (localVariableElement.getModifiers().contains(Modifier.STATIC))
/*     */       {
/* 231 */         String str = null;
/* 232 */         str = defineForStatic(paramClassSymbol, localVariableElement);
/* 233 */         if (str != null)
/* 234 */           println(paramWriter, str);
/*     */       }
/*     */   }
/*     */ 
/*     */   List<VariableElement> getAllFields(TypeElement paramTypeElement)
/*     */   {
/* 243 */     ArrayList localArrayList = new ArrayList();
/* 244 */     Object localObject = null;
/* 245 */     Stack localStack = new Stack();
/*     */ 
/* 247 */     localObject = paramTypeElement;
/*     */     while (true) {
/* 249 */       localStack.push(localObject);
/* 250 */       TypeElement localTypeElement = (TypeElement)this.types.asElement(((TypeElement)localObject).getSuperclass());
/* 251 */       if (localTypeElement == null)
/*     */         break;
/* 253 */       localObject = localTypeElement;
/*     */     }
/*     */ 
/* 256 */     while (!localStack.empty()) {
/* 257 */       localObject = (TypeElement)localStack.pop();
/* 258 */       localArrayList.addAll(ElementFilter.fieldsIn(((TypeElement)localObject).getEnclosedElements()));
/*     */     }
/*     */ 
/* 261 */     return localArrayList;
/*     */   }
/*     */ 
/*     */   protected String defineForStatic(TypeElement paramTypeElement, VariableElement paramVariableElement) {
/* 265 */     javax.lang.model.element.Name localName1 = paramTypeElement.getQualifiedName();
/* 266 */     javax.lang.model.element.Name localName2 = paramVariableElement.getSimpleName();
/*     */ 
/* 268 */     String str1 = this.mangler.mangle(localName1, 1);
/* 269 */     String str2 = this.mangler.mangle(localName2, 2);
/*     */ 
/* 271 */     Assert.check(paramVariableElement.getModifiers().contains(Modifier.STATIC));
/*     */ 
/* 273 */     if (paramVariableElement.getModifiers().contains(Modifier.FINAL)) {
/* 274 */       Object localObject = null;
/*     */ 
/* 276 */       localObject = paramVariableElement.getConstantValue();
/*     */ 
/* 278 */       if (localObject != null) {
/* 279 */         String str3 = null;
/* 280 */         if (((localObject instanceof Integer)) || ((localObject instanceof Byte)) || ((localObject instanceof Short)))
/*     */         {
/* 284 */           str3 = localObject.toString() + "L";
/* 285 */         } else if ((localObject instanceof Boolean)) {
/* 286 */           str3 = ((Boolean)localObject).booleanValue() ? "1L" : "0L";
/* 287 */         } else if ((localObject instanceof Character)) {
/* 288 */           Character localCharacter = (Character)localObject;
/* 289 */           str3 = String.valueOf(localCharacter.charValue() & 0xFFFF) + "L";
/* 290 */         } else if ((localObject instanceof Long))
/*     */         {
/* 292 */           if (this.isWindows)
/* 293 */             str3 = localObject.toString() + "i64";
/*     */           else
/* 295 */             str3 = localObject.toString() + "LL";
/* 296 */         } else if ((localObject instanceof Float))
/*     */         {
/* 298 */           float f = ((Float)localObject).floatValue();
/* 299 */           if (Float.isInfinite(f))
/* 300 */             str3 = (f < 0.0F ? "-" : "") + "Inff";
/*     */           else
/* 302 */             str3 = localObject.toString() + "f";
/* 303 */         } else if ((localObject instanceof Double))
/*     */         {
/* 305 */           double d = ((Double)localObject).doubleValue();
/* 306 */           if (Double.isInfinite(d))
/* 307 */             str3 = (d < 0.0D ? "-" : "") + "InfD";
/*     */           else {
/* 309 */             str3 = localObject.toString();
/*     */           }
/*     */         }
/* 312 */         if (str3 != null) {
/* 313 */           StringBuilder localStringBuilder = new StringBuilder("#undef ");
/* 314 */           localStringBuilder.append(str1); localStringBuilder.append("_"); localStringBuilder.append(str2); localStringBuilder.append(this.lineSep);
/* 315 */           localStringBuilder.append("#define "); localStringBuilder.append(str1); localStringBuilder.append("_");
/* 316 */           localStringBuilder.append(str2); localStringBuilder.append(" "); localStringBuilder.append(str3);
/* 317 */           return localStringBuilder.toString();
/*     */         }
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 323 */     return null;
/*     */   }
/*     */ 
/*     */   protected void writeMethods(Writer paramWriter, Symbol.ClassSymbol paramClassSymbol, String paramString)
/*     */     throws IOException, JNIWriter.TypeSignature.SignatureException
/*     */   {
/* 329 */     List localList = ElementFilter.methodsIn(paramClassSymbol.getEnclosedElements());
/* 330 */     for (ExecutableElement localExecutableElement : localList)
/* 331 */       if (localExecutableElement.getModifiers().contains(Modifier.NATIVE)) {
/* 332 */         TypeMirror localTypeMirror = this.types.erasure(localExecutableElement.getReturnType());
/* 333 */         String str = signature(localExecutableElement);
/* 334 */         TypeSignature localTypeSignature = new TypeSignature(this.elements);
/* 335 */         javax.lang.model.element.Name localName = localExecutableElement.getSimpleName();
/* 336 */         int i = 0;
/* 337 */         for (Object localObject1 = localList.iterator(); ((Iterator)localObject1).hasNext(); ) { localObject2 = (ExecutableElement)((Iterator)localObject1).next();
/* 338 */           if ((localObject2 != localExecutableElement) && 
/* 339 */             (localName
/* 339 */             .equals(((ExecutableElement)localObject2)
/* 339 */             .getSimpleName())) && 
/* 340 */             (((ExecutableElement)localObject2)
/* 340 */             .getModifiers().contains(Modifier.NATIVE))) {
/* 341 */             i = 1;
/*     */           }
/*     */         }
/* 344 */         println(paramWriter, "/*");
/* 345 */         println(paramWriter, " * Class:     " + paramString);
/* 346 */         println(paramWriter, " * Method:    " + this.mangler
/* 347 */           .mangle(localName, 2));
/*     */ 
/* 348 */         println(paramWriter, " * Signature: " + localTypeSignature.getTypeSignature(str, localTypeMirror));
/* 349 */         println(paramWriter, " */");
/* 350 */         println(paramWriter, "JNIEXPORT " + jniType(localTypeMirror) + " JNICALL " + this.mangler
/* 352 */           .mangleMethod(localExecutableElement, paramClassSymbol, i != 0 ? 8 : 7));
/*     */ 
/* 356 */         print(paramWriter, "  (JNIEnv *, ");
/* 357 */         localObject1 = localExecutableElement.getParameters();
/* 358 */         Object localObject2 = new ArrayList();
/* 359 */         for (Iterator localIterator2 = ((List)localObject1).iterator(); localIterator2.hasNext(); ) { localObject3 = (VariableElement)localIterator2.next();
/* 360 */           ((List)localObject2).add(this.types.erasure(((VariableElement)localObject3).asType()));
/*     */         }
/*     */         Object localObject3;
/* 362 */         if (localExecutableElement.getModifiers().contains(Modifier.STATIC))
/* 363 */           print(paramWriter, "jclass");
/*     */         else {
/* 365 */           print(paramWriter, "jobject");
/*     */         }
/* 367 */         for (localIterator2 = ((List)localObject2).iterator(); localIterator2.hasNext(); ) { localObject3 = (TypeMirror)localIterator2.next();
/* 368 */           print(paramWriter, ", ");
/* 369 */           print(paramWriter, jniType((TypeMirror)localObject3));
/*     */         }
/* 371 */         println(paramWriter, ");" + this.lineSep);
/*     */       }
/*     */   }
/*     */ 
/*     */   String signature(ExecutableElement paramExecutableElement)
/*     */   {
/* 379 */     StringBuilder localStringBuilder = new StringBuilder("(");
/* 380 */     String str = "";
/* 381 */     for (VariableElement localVariableElement : paramExecutableElement.getParameters()) {
/* 382 */       localStringBuilder.append(str);
/* 383 */       localStringBuilder.append(this.types.erasure(localVariableElement.asType()).toString());
/* 384 */       str = ",";
/*     */     }
/* 386 */     localStringBuilder.append(")");
/* 387 */     return localStringBuilder.toString();
/*     */   }
/*     */ 
/*     */   protected final String jniType(TypeMirror paramTypeMirror) {
/* 391 */     Symbol.ClassSymbol localClassSymbol1 = this.elements.getTypeElement("java.lang.Throwable");
/* 392 */     Symbol.ClassSymbol localClassSymbol2 = this.elements.getTypeElement("java.lang.Class");
/* 393 */     Symbol.ClassSymbol localClassSymbol3 = this.elements.getTypeElement("java.lang.String");
/* 394 */     Element localElement = this.types.asElement(paramTypeMirror);
/*     */ 
/* 397 */     switch (1.$SwitchMap$javax$lang$model$type$TypeKind[paramTypeMirror.getKind().ordinal()]) {
/*     */     case 9:
/* 399 */       TypeMirror localTypeMirror = ((ArrayType)paramTypeMirror).getComponentType();
/* 400 */       switch (1.$SwitchMap$javax$lang$model$type$TypeKind[localTypeMirror.getKind().ordinal()]) { case 1:
/* 401 */         return "jbooleanArray";
/*     */       case 2:
/* 402 */         return "jbyteArray";
/*     */       case 3:
/* 403 */         return "jcharArray";
/*     */       case 4:
/* 404 */         return "jshortArray";
/*     */       case 5:
/* 405 */         return "jintArray";
/*     */       case 6:
/* 406 */         return "jlongArray";
/*     */       case 7:
/* 407 */         return "jfloatArray";
/*     */       case 8:
/* 408 */         return "jdoubleArray";
/*     */       case 9:
/*     */       case 10:
/* 410 */         return "jobjectArray"; }
/* 411 */       throw new Error(localTypeMirror.toString());
/*     */     case 11:
/* 415 */       return "void";
/*     */     case 1:
/* 416 */       return "jboolean";
/*     */     case 2:
/* 417 */       return "jbyte";
/*     */     case 3:
/* 418 */       return "jchar";
/*     */     case 4:
/* 419 */       return "jshort";
/*     */     case 5:
/* 420 */       return "jint";
/*     */     case 6:
/* 421 */       return "jlong";
/*     */     case 7:
/* 422 */       return "jfloat";
/*     */     case 8:
/* 423 */       return "jdouble";
/*     */     case 10:
/* 426 */       if (localElement.equals(localClassSymbol3))
/* 427 */         return "jstring";
/* 428 */       if (this.types.isAssignable(paramTypeMirror, localClassSymbol1.asType()))
/* 429 */         return "jthrowable";
/* 430 */       if (this.types.isAssignable(paramTypeMirror, localClassSymbol2.asType())) {
/* 431 */         return "jclass";
/*     */       }
/* 433 */       return "jobject";
/*     */     }
/*     */ 
/* 437 */     Assert.check(false, "jni unknown type");
/* 438 */     return null;
/*     */   }
/*     */ 
/*     */   protected String fileTop() {
/* 442 */     return "/* DO NOT EDIT THIS FILE - it is machine generated */";
/*     */   }
/*     */ 
/*     */   protected String includes() {
/* 446 */     return "#include <jni.h>";
/*     */   }
/*     */ 
/*     */   protected String cppGuardBegin()
/*     */   {
/* 453 */     return "#ifdef __cplusplus" + this.lineSep + "extern \"C\" {" + this.lineSep + "#endif";
/*     */   }
/*     */ 
/*     */   protected String cppGuardEnd()
/*     */   {
/* 459 */     return "#ifdef __cplusplus" + this.lineSep + "}" + this.lineSep + "#endif";
/*     */   }
/*     */ 
/*     */   protected String guardBegin(String paramString)
/*     */   {
/* 465 */     return "/* Header for class " + paramString + " */" + this.lineSep + this.lineSep + "#ifndef _Included_" + paramString + this.lineSep + "#define _Included_" + paramString;
/*     */   }
/*     */ 
/*     */   protected String guardEnd(String paramString)
/*     */   {
/* 472 */     return "#endif";
/*     */   }
/*     */ 
/*     */   protected void print(Writer paramWriter, String paramString) throws IOException {
/* 476 */     paramWriter.write(paramString);
/*     */   }
/*     */ 
/*     */   protected void println(Writer paramWriter, String paramString) throws IOException {
/* 480 */     paramWriter.write(paramString);
/* 481 */     paramWriter.write(this.lineSep);
/*     */   }
/*     */ 
/*     */   private static class Mangle
/*     */   {
/*     */     private Elements elems;
/*     */     private Types types;
/*     */ 
/*     */     Mangle(Elements paramElements, Types paramTypes)
/*     */     {
/* 502 */       this.elems = paramElements;
/* 503 */       this.types = paramTypes;
/*     */     }
/*     */ 
/*     */     public final String mangle(CharSequence paramCharSequence, int paramInt) {
/* 507 */       StringBuilder localStringBuilder = new StringBuilder(100);
/* 508 */       int i = paramCharSequence.length();
/*     */ 
/* 510 */       for (int j = 0; j < i; j++) {
/* 511 */         char c = paramCharSequence.charAt(j);
/* 512 */         if (isalnum(c)) {
/* 513 */           localStringBuilder.append(c);
/* 514 */         } else if ((c == '.') && (paramInt == 1))
/*     */         {
/* 516 */           localStringBuilder.append('_');
/* 517 */         } else if ((c == '$') && (paramInt == 1))
/*     */         {
/* 519 */           localStringBuilder.append('_');
/* 520 */           localStringBuilder.append('_');
/* 521 */         } else if ((c == '_') && (paramInt == 2)) {
/* 522 */           localStringBuilder.append('_');
/* 523 */         } else if ((c == '_') && (paramInt == 1)) {
/* 524 */           localStringBuilder.append('_');
/* 525 */         } else if (paramInt == 4) {
/* 526 */           String str = null;
/* 527 */           if (c == '_')
/* 528 */             str = "_1";
/* 529 */           else if (c == '.')
/* 530 */             str = "_";
/* 531 */           else if (c == ';')
/* 532 */             str = "_2";
/* 533 */           else if (c == '[')
/* 534 */             str = "_3";
/* 535 */           if (str != null)
/* 536 */             localStringBuilder.append(str);
/*     */           else
/* 538 */             localStringBuilder.append(mangleChar(c));
/*     */         }
/* 540 */         else if (paramInt == 5) {
/* 541 */           if (isprint(c))
/* 542 */             localStringBuilder.append(c);
/*     */           else
/* 544 */             localStringBuilder.append(mangleChar(c));
/*     */         }
/*     */         else {
/* 547 */           localStringBuilder.append(mangleChar(c));
/*     */         }
/*     */       }
/*     */ 
/* 551 */       return localStringBuilder.toString();
/*     */     }
/*     */ 
/*     */     public String mangleMethod(ExecutableElement paramExecutableElement, TypeElement paramTypeElement, int paramInt) throws JNIWriter.TypeSignature.SignatureException
/*     */     {
/* 556 */       StringBuilder localStringBuilder = new StringBuilder(100);
/* 557 */       localStringBuilder.append("Java_");
/*     */ 
/* 559 */       if (paramInt == 6) {
/* 560 */         localStringBuilder.append(mangle(paramTypeElement.getQualifiedName(), 1));
/* 561 */         localStringBuilder.append('_');
/* 562 */         localStringBuilder.append(mangle(paramExecutableElement.getSimpleName(), 3));
/*     */ 
/* 564 */         localStringBuilder.append("_stub");
/* 565 */         return localStringBuilder.toString();
/*     */       }
/*     */ 
/* 569 */       localStringBuilder.append(mangle(getInnerQualifiedName(paramTypeElement), 4));
/* 570 */       localStringBuilder.append('_');
/* 571 */       localStringBuilder.append(mangle(paramExecutableElement.getSimpleName(), 4));
/*     */ 
/* 573 */       if (paramInt == 8) {
/* 574 */         localStringBuilder.append("__");
/* 575 */         String str1 = signature(paramExecutableElement);
/* 576 */         JNIWriter.TypeSignature localTypeSignature = new JNIWriter.TypeSignature(this.elems);
/* 577 */         String str2 = localTypeSignature.getTypeSignature(str1, paramExecutableElement.getReturnType());
/* 578 */         str2 = str2.substring(1);
/* 579 */         str2 = str2.substring(0, str2.lastIndexOf(')'));
/* 580 */         str2 = str2.replace('/', '.');
/* 581 */         localStringBuilder.append(mangle(str2, 4));
/*     */       }
/*     */ 
/* 584 */       return localStringBuilder.toString();
/*     */     }
/*     */ 
/*     */     private String getInnerQualifiedName(TypeElement paramTypeElement) {
/* 588 */       return this.elems.getBinaryName(paramTypeElement).toString();
/*     */     }
/*     */ 
/*     */     public final String mangleChar(char paramChar) {
/* 592 */       String str = Integer.toHexString(paramChar);
/* 593 */       int i = 5 - str.length();
/* 594 */       char[] arrayOfChar = new char[6];
/* 595 */       arrayOfChar[0] = '_';
/* 596 */       for (int j = 1; j <= i; j++)
/* 597 */         arrayOfChar[j] = '0';
/* 598 */       j = i + 1; for (int k = 0; j < 6; k++) {
/* 599 */         arrayOfChar[j] = str.charAt(k);
/*     */ 
/* 598 */         j++;
/*     */       }
/* 600 */       return new String(arrayOfChar);
/*     */     }
/*     */ 
/*     */     private String signature(ExecutableElement paramExecutableElement)
/*     */     {
/* 605 */       StringBuilder localStringBuilder = new StringBuilder();
/* 606 */       String str = "(";
/* 607 */       for (VariableElement localVariableElement : paramExecutableElement.getParameters()) {
/* 608 */         localStringBuilder.append(str);
/* 609 */         localStringBuilder.append(this.types.erasure(localVariableElement.asType()).toString());
/* 610 */         str = ",";
/*     */       }
/* 612 */       localStringBuilder.append(")");
/* 613 */       return localStringBuilder.toString();
/*     */     }
/*     */ 
/*     */     private static boolean isalnum(char paramChar)
/*     */     {
/* 618 */       return (paramChar <= '') && (((paramChar >= 'A') && (paramChar <= 'Z')) || ((paramChar >= 'a') && (paramChar <= 'z')) || ((paramChar >= '0') && (paramChar <= '9')));
/*     */     }
/*     */ 
/*     */     private static boolean isprint(char paramChar)
/*     */     {
/* 626 */       return (paramChar >= ' ') && (paramChar <= '~'); } 
/*     */     public static class Type { public static final int CLASS = 1;
/*     */       public static final int FIELDSTUB = 2;
/*     */       public static final int FIELD = 3;
/*     */       public static final int JNI = 4;
/*     */       public static final int SIGNATURE = 5;
/*     */       public static final int METHOD_JDK_1 = 6;
/*     */       public static final int METHOD_JNI_SHORT = 7;
/*     */       public static final int METHOD_JNI_LONG = 8; }  } 
/*     */   private static class TypeSignature { Elements elems;
/*     */     private static final String SIG_VOID = "V";
/*     */     private static final String SIG_BOOLEAN = "Z";
/*     */     private static final String SIG_BYTE = "B";
/*     */     private static final String SIG_CHAR = "C";
/*     */     private static final String SIG_SHORT = "S";
/*     */     private static final String SIG_INT = "I";
/*     */     private static final String SIG_LONG = "J";
/*     */     private static final String SIG_FLOAT = "F";
/*     */     private static final String SIG_DOUBLE = "D";
/*     */     private static final String SIG_ARRAY = "[";
/*     */     private static final String SIG_CLASS = "L";
/*     */ 
/* 657 */     public TypeSignature(Elements paramElements) { this.elems = paramElements; }
/*     */ 
/*     */ 
/*     */     public String getTypeSignature(String paramString)
/*     */       throws JNIWriter.TypeSignature.SignatureException
/*     */     {
/* 664 */       return getParamJVMSignature(paramString);
/*     */     }
/*     */ 
/*     */     public String getTypeSignature(String paramString, TypeMirror paramTypeMirror)
/*     */       throws JNIWriter.TypeSignature.SignatureException
/*     */     {
/* 672 */       String str1 = null;
/* 673 */       String str2 = null;
/* 674 */       ArrayList localArrayList = new ArrayList();
/* 675 */       String str3 = null;
/* 676 */       String str4 = null;
/* 677 */       String str5 = null;
/* 678 */       String str6 = null;
/* 679 */       int i = 0;
/*     */ 
/* 681 */       int j = -1;
/* 682 */       int k = -1;
/* 683 */       StringTokenizer localStringTokenizer = null;
/* 684 */       int m = 0;
/*     */ 
/* 687 */       if (paramString != null) {
/* 688 */         j = paramString.indexOf("(");
/* 689 */         k = paramString.indexOf(")");
/*     */       }
/*     */ 
/* 692 */       if ((j != -1) && (k != -1) && 
/* 693 */         (j + 1 < paramString
/* 693 */         .length()) && 
/* 694 */         (k < paramString
/* 694 */         .length())) {
/* 695 */         str1 = paramString.substring(j + 1, k);
/*     */       }
/*     */ 
/* 699 */       if (str1 != null) {
/* 700 */         if (str1.indexOf(",") != -1) {
/* 701 */           localStringTokenizer = new StringTokenizer(str1, ",");
/* 702 */           if (localStringTokenizer != null)
/* 703 */             while (localStringTokenizer.hasMoreTokens())
/* 704 */               localArrayList.add(localStringTokenizer.nextToken());
/*     */         }
/*     */         else
/*     */         {
/* 708 */           localArrayList.add(str1);
/*     */         }
/*     */ 
/*     */       }
/*     */ 
/* 713 */       str2 = "(";
/*     */ 
/* 716 */       while (localArrayList.isEmpty() != true) {
/* 717 */         str3 = ((String)localArrayList.remove(m)).trim();
/* 718 */         str4 = getParamJVMSignature(str3);
/* 719 */         if (str4 != null) {
/* 720 */           str2 = str2 + str4;
/*     */         }
/*     */       }
/*     */ 
/* 724 */       str2 = str2 + ")";
/*     */ 
/* 728 */       str6 = "";
/* 729 */       if (paramTypeMirror != null) {
/* 730 */         i = dimensions(paramTypeMirror);
/*     */       }
/*     */ 
/* 734 */       while (i-- > 0) {
/* 735 */         str6 = str6 + "[";
/*     */       }
/* 737 */       if (paramTypeMirror != null) {
/* 738 */         str5 = qualifiedTypeName(paramTypeMirror);
/* 739 */         str6 = str6 + getComponentType(str5);
/*     */       } else {
/* 741 */         System.out.println("Invalid return type.");
/*     */       }
/*     */ 
/* 744 */       str2 = str2 + str6;
/*     */ 
/* 746 */       return str2;
/*     */     }
/*     */ 
/*     */     private String getParamJVMSignature(String paramString)
/*     */       throws JNIWriter.TypeSignature.SignatureException
/*     */     {
/* 753 */       String str1 = "";
/* 754 */       String str2 = "";
/*     */ 
/* 756 */       if (paramString != null)
/*     */       {
/* 758 */         if (paramString.indexOf("[]") != -1)
/*     */         {
/* 760 */           int i = paramString.indexOf("[]");
/* 761 */           str2 = paramString.substring(0, i);
/* 762 */           String str3 = paramString.substring(i);
/* 763 */           if (str3 != null)
/* 764 */             while (str3.indexOf("[]") != -1) {
/* 765 */               str1 = str1 + "[";
/* 766 */               int j = str3.indexOf("]") + 1;
/* 767 */               if (j < str3.length())
/* 768 */                 str3 = str3.substring(j);
/*     */               else
/* 770 */                 str3 = "";
/*     */             }
/*     */         } else {
/* 773 */           str2 = paramString;
/*     */         }
/* 775 */         str1 = str1 + getComponentType(str2);
/*     */       }
/* 777 */       return str1;
/*     */     }
/*     */ 
/*     */     private String getComponentType(String paramString)
/*     */       throws JNIWriter.TypeSignature.SignatureException
/*     */     {
/* 785 */       String str1 = "";
/*     */ 
/* 787 */       if (paramString != null) {
/* 788 */         if (paramString.equals("void")) { str1 = str1 + "V";
/* 789 */         } else if (paramString.equals("boolean")) { str1 = str1 + "Z";
/* 790 */         } else if (paramString.equals("byte")) { str1 = str1 + "B";
/* 791 */         } else if (paramString.equals("char")) { str1 = str1 + "C";
/* 792 */         } else if (paramString.equals("short")) { str1 = str1 + "S";
/* 793 */         } else if (paramString.equals("int")) { str1 = str1 + "I";
/* 794 */         } else if (paramString.equals("long")) { str1 = str1 + "J";
/* 795 */         } else if (paramString.equals("float")) { str1 = str1 + "F";
/* 796 */         } else if (paramString.equals("double")) { str1 = str1 + "D"; }
/* 798 */         else if (!paramString.equals("")) {
/* 799 */           TypeElement localTypeElement = this.elems.getTypeElement(paramString);
/*     */ 
/* 801 */           if (localTypeElement == null) {
/* 802 */             throw new SignatureException(paramString);
/*     */           }
/* 804 */           String str2 = localTypeElement.getQualifiedName().toString();
/* 805 */           String str3 = str2.replace('.', '/');
/* 806 */           str1 = str1 + "L";
/* 807 */           str1 = str1 + str3;
/* 808 */           str1 = str1 + ";";
/*     */         }
/*     */ 
/*     */       }
/*     */ 
/* 813 */       return str1;
/*     */     }
/*     */ 
/*     */     int dimensions(TypeMirror paramTypeMirror) {
/* 817 */       if (paramTypeMirror.getKind() != TypeKind.ARRAY)
/* 818 */         return 0;
/* 819 */       return 1 + dimensions(((ArrayType)paramTypeMirror).getComponentType());
/*     */     }
/*     */ 
/*     */     String qualifiedTypeName(TypeMirror paramTypeMirror)
/*     */     {
/* 824 */       SimpleTypeVisitor8 local1 = new SimpleTypeVisitor8()
/*     */       {
/*     */         public javax.lang.model.element.Name visitArray(ArrayType paramAnonymousArrayType, Void paramAnonymousVoid) {
/* 827 */           return (javax.lang.model.element.Name)paramAnonymousArrayType.getComponentType().accept(this, paramAnonymousVoid);
/*     */         }
/*     */ 
/*     */         public javax.lang.model.element.Name visitDeclared(DeclaredType paramAnonymousDeclaredType, Void paramAnonymousVoid)
/*     */         {
/* 832 */           return ((TypeElement)paramAnonymousDeclaredType.asElement()).getQualifiedName();
/*     */         }
/*     */ 
/*     */         public javax.lang.model.element.Name visitPrimitive(PrimitiveType paramAnonymousPrimitiveType, Void paramAnonymousVoid)
/*     */         {
/* 837 */           return JNIWriter.TypeSignature.this.elems.getName(paramAnonymousPrimitiveType.toString());
/*     */         }
/*     */ 
/*     */         public javax.lang.model.element.Name visitNoType(NoType paramAnonymousNoType, Void paramAnonymousVoid)
/*     */         {
/* 842 */           if (paramAnonymousNoType.getKind() == TypeKind.VOID)
/* 843 */             return JNIWriter.TypeSignature.this.elems.getName("void");
/* 844 */           return (javax.lang.model.element.Name)defaultAction(paramAnonymousNoType, paramAnonymousVoid);
/*     */         }
/*     */ 
/*     */         public javax.lang.model.element.Name visitTypeVariable(TypeVariable paramAnonymousTypeVariable, Void paramAnonymousVoid)
/*     */         {
/* 849 */           return (javax.lang.model.element.Name)paramAnonymousTypeVariable.getUpperBound().accept(this, paramAnonymousVoid);
/*     */         }
/*     */       };
/* 852 */       return ((javax.lang.model.element.Name)local1.visit(paramTypeMirror)).toString();
/*     */     }
/*     */ 
/*     */     static class SignatureException extends Exception
/*     */     {
/*     */       private static final long serialVersionUID = 1L;
/*     */ 
/*     */       SignatureException(String paramString)
/*     */       {
/* 634 */         super();
/*     */       }
/*     */     }
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.javac.jvm.JNIWriter
 * JD-Core Version:    0.6.2
 */