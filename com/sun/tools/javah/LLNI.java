/*     */ package com.sun.tools.javah;
/*     */ 
/*     */ import java.io.OutputStream;
/*     */ import java.io.PrintWriter;
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashSet;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Set;
/*     */ import javax.lang.model.element.Element;
/*     */ import javax.lang.model.element.ExecutableElement;
/*     */ import javax.lang.model.element.Modifier;
/*     */ import javax.lang.model.element.Name;
/*     */ import javax.lang.model.element.TypeElement;
/*     */ import javax.lang.model.element.VariableElement;
/*     */ import javax.lang.model.type.ArrayType;
/*     */ import javax.lang.model.type.PrimitiveType;
/*     */ import javax.lang.model.type.TypeKind;
/*     */ import javax.lang.model.type.TypeMirror;
/*     */ import javax.lang.model.type.TypeVisitor;
/*     */ import javax.lang.model.util.ElementFilter;
/*     */ import javax.lang.model.util.Elements;
/*     */ import javax.lang.model.util.SimpleTypeVisitor8;
/*     */ import javax.lang.model.util.Types;
/*     */ 
/*     */ public class LLNI extends Gen
/*     */ {
/*  60 */   protected final char innerDelim = '$';
/*     */   protected Set<String> doneHandleTypes;
/*     */   List<VariableElement> fields;
/*     */   List<ExecutableElement> methods;
/*     */   private boolean doubleAlign;
/*  65 */   private int padFieldNum = 0;
/*     */ 
/* 348 */   private static final boolean isWindows = System.getProperty("os.name")
/* 348 */     .startsWith("Windows");
/*     */ 
/*     */   LLNI(boolean paramBoolean, Util paramUtil)
/*     */   {
/*  68 */     super(paramUtil);
/*  69 */     this.doubleAlign = paramBoolean;
/*     */   }
/*     */ 
/*     */   protected String getIncludes() {
/*  73 */     return "";
/*     */   }
/*     */ 
/*     */   protected void write(OutputStream paramOutputStream, TypeElement paramTypeElement) throws Util.Exit {
/*     */     try {
/*  78 */       String str = mangleClassName(paramTypeElement.getQualifiedName().toString());
/*  79 */       PrintWriter localPrintWriter = wrapWriter(paramOutputStream);
/*  80 */       this.fields = ElementFilter.fieldsIn(paramTypeElement.getEnclosedElements());
/*  81 */       this.methods = ElementFilter.methodsIn(paramTypeElement.getEnclosedElements());
/*  82 */       generateDeclsForClass(localPrintWriter, paramTypeElement, str);
/*     */     }
/*     */     catch (TypeSignature.SignatureException localSignatureException) {
/*  85 */       this.util.error("llni.sigerror", new Object[] { localSignatureException.getMessage() });
/*     */     }
/*     */   }
/*     */ 
/*     */   protected void generateDeclsForClass(PrintWriter paramPrintWriter, TypeElement paramTypeElement, String paramString)
/*     */     throws TypeSignature.SignatureException, Util.Exit
/*     */   {
/*  92 */     this.doneHandleTypes = new HashSet();
/*     */ 
/*  95 */     genHandleType(null, "java.lang.Class");
/*  96 */     genHandleType(null, "java.lang.ClassLoader");
/*  97 */     genHandleType(null, "java.lang.Object");
/*  98 */     genHandleType(null, "java.lang.String");
/*  99 */     genHandleType(null, "java.lang.Thread");
/* 100 */     genHandleType(null, "java.lang.ThreadGroup");
/* 101 */     genHandleType(null, "java.lang.Throwable");
/*     */ 
/* 103 */     paramPrintWriter.println("/* LLNI Header for class " + paramTypeElement.getQualifiedName() + " */" + this.lineSep);
/* 104 */     paramPrintWriter.println("#ifndef _Included_" + paramString);
/* 105 */     paramPrintWriter.println("#define _Included_" + paramString);
/* 106 */     paramPrintWriter.println("#include \"typedefs.h\"");
/* 107 */     paramPrintWriter.println("#include \"llni.h\"");
/* 108 */     paramPrintWriter.println("#include \"jni.h\"" + this.lineSep);
/*     */ 
/* 110 */     forwardDecls(paramPrintWriter, paramTypeElement);
/* 111 */     structSectionForClass(paramPrintWriter, paramTypeElement, paramString);
/* 112 */     methodSectionForClass(paramPrintWriter, paramTypeElement, paramString);
/* 113 */     paramPrintWriter.println("#endif");
/*     */   }
/*     */ 
/*     */   protected void genHandleType(PrintWriter paramPrintWriter, String paramString) {
/* 117 */     String str = mangleClassName(paramString);
/* 118 */     if (!this.doneHandleTypes.contains(str)) {
/* 119 */       this.doneHandleTypes.add(str);
/* 120 */       if (paramPrintWriter != null) {
/* 121 */         paramPrintWriter.println("#ifndef DEFINED_" + str);
/* 122 */         paramPrintWriter.println("    #define DEFINED_" + str);
/* 123 */         paramPrintWriter.println("    GEN_HANDLE_TYPES(" + str + ");");
/* 124 */         paramPrintWriter.println("#endif" + this.lineSep);
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   protected String mangleClassName(String paramString)
/*     */   {
/* 132 */     return paramString.replace('.', '_')
/* 131 */       .replace('/', '_')
/* 132 */       .replace('$', '_');
/*     */   }
/*     */ 
/*     */   protected void forwardDecls(PrintWriter paramPrintWriter, TypeElement paramTypeElement)
/*     */     throws TypeSignature.SignatureException
/*     */   {
/* 137 */     TypeElement localTypeElement1 = this.elems.getTypeElement("java.lang.Object");
/* 138 */     if (paramTypeElement.equals(localTypeElement1)) {
/* 139 */       return;
/*     */     }
/* 141 */     genHandleType(paramPrintWriter, paramTypeElement.getQualifiedName().toString());
/* 142 */     TypeElement localTypeElement2 = (TypeElement)this.types.asElement(paramTypeElement.getSuperclass());
/*     */ 
/* 144 */     if (localTypeElement2 != null) {
/* 145 */       localObject1 = localTypeElement2.getQualifiedName().toString();
/* 146 */       forwardDecls(paramPrintWriter, localTypeElement2);
/*     */     }
/*     */ 
/* 149 */     for (Object localObject1 = this.fields.iterator(); ((Iterator)localObject1).hasNext(); ) { localObject2 = (VariableElement)((Iterator)localObject1).next();
/*     */ 
/* 151 */       if (!((VariableElement)localObject2).getModifiers().contains(Modifier.STATIC)) {
/* 152 */         localTypeMirror = this.types.erasure(((VariableElement)localObject2).asType());
/* 153 */         localObject3 = new TypeSignature(this.elems);
/* 154 */         localObject4 = ((TypeSignature)localObject3).qualifiedTypeName(localTypeMirror);
/* 155 */         str = ((TypeSignature)localObject3).getTypeSignature((String)localObject4);
/*     */ 
/* 157 */         if (str.charAt(0) != '[')
/* 158 */           forwardDeclsFromSig(paramPrintWriter, str);
/*     */       }
/*     */     }
/* 162 */     Object localObject2;
/*     */     TypeMirror localTypeMirror;
/*     */     Object localObject3;
/*     */     Object localObject4;
/*     */     String str;
/* 162 */     for (localObject1 = this.methods.iterator(); ((Iterator)localObject1).hasNext(); ) { localObject2 = (ExecutableElement)((Iterator)localObject1).next();
/*     */ 
/* 164 */       if (((ExecutableElement)localObject2).getModifiers().contains(Modifier.NATIVE)) {
/* 165 */         localTypeMirror = this.types.erasure(((ExecutableElement)localObject2).getReturnType());
/* 166 */         localObject3 = signature((ExecutableElement)localObject2);
/* 167 */         localObject4 = new TypeSignature(this.elems);
/* 168 */         str = ((TypeSignature)localObject4).getTypeSignature((String)localObject3, localTypeMirror);
/*     */ 
/* 170 */         if (str.charAt(0) != '[')
/* 171 */           forwardDeclsFromSig(paramPrintWriter, str);
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   protected void forwardDeclsFromSig(PrintWriter paramPrintWriter, String paramString)
/*     */   {
/* 178 */     int i = paramString.length();
/* 179 */     int j = paramString.charAt(0) == '(' ? 1 : 0;
/*     */ 
/* 182 */     while (j < i)
/* 183 */       if (paramString.charAt(j) == 'L') {
/* 184 */         int k = j + 1;
/* 185 */         while (paramString.charAt(k) != ';') k++;
/* 186 */         genHandleType(paramPrintWriter, paramString.substring(j + 1, k));
/* 187 */         j = k + 1;
/*     */       } else {
/* 189 */         j++;
/*     */       }
/*     */   }
/*     */ 
/*     */   protected void structSectionForClass(PrintWriter paramPrintWriter, TypeElement paramTypeElement, String paramString)
/*     */   {
/* 197 */     String str = paramTypeElement.getQualifiedName().toString();
/*     */ 
/* 199 */     if (paramString.equals("java_lang_Object")) {
/* 200 */       paramPrintWriter.println("/* struct java_lang_Object is defined in typedefs.h. */");
/* 201 */       paramPrintWriter.println();
/* 202 */       return;
/*     */     }
/* 204 */     paramPrintWriter.println("#if !defined(__i386)");
/* 205 */     paramPrintWriter.println("#pragma pack(4)");
/* 206 */     paramPrintWriter.println("#endif");
/* 207 */     paramPrintWriter.println();
/* 208 */     paramPrintWriter.println("struct " + paramString + " {");
/* 209 */     paramPrintWriter.println("    ObjHeader h;");
/* 210 */     paramPrintWriter.print(fieldDefs(paramTypeElement, paramString));
/*     */ 
/* 212 */     if (str.equals("java.lang.Class")) {
/* 213 */       paramPrintWriter.println("    Class *LLNI_mask(cClass);  /* Fake field; don't access (see oobj.h) */");
/*     */     }
/* 215 */     paramPrintWriter.println("};" + this.lineSep + this.lineSep + "#pragma pack()");
/* 216 */     paramPrintWriter.println();
/*     */   }
/*     */ 
/*     */   private boolean doField(FieldDefsRes paramFieldDefsRes, VariableElement paramVariableElement, String paramString, boolean paramBoolean)
/*     */   {
/* 242 */     String str = addStructMember(paramVariableElement, paramString, paramBoolean);
/* 243 */     if (str != null) {
/* 244 */       if (!paramFieldDefsRes.printedOne) {
/* 245 */         if (paramFieldDefsRes.bottomMost) {
/* 246 */           if (paramFieldDefsRes.s.length() != 0)
/* 247 */             paramFieldDefsRes.s = (paramFieldDefsRes.s + "    /* local members: */" + this.lineSep);
/*     */         }
/* 249 */         else paramFieldDefsRes.s = (paramFieldDefsRes.s + "    /* inherited members from " + paramFieldDefsRes.className + ": */" + this.lineSep);
/*     */ 
/* 252 */         paramFieldDefsRes.printedOne = true;
/*     */       }
/* 254 */       paramFieldDefsRes.s += str;
/* 255 */       return true;
/*     */     }
/*     */ 
/* 259 */     return false;
/*     */   }
/*     */ 
/*     */   private int doTwoWordFields(FieldDefsRes paramFieldDefsRes, TypeElement paramTypeElement, int paramInt, String paramString, boolean paramBoolean)
/*     */   {
/* 264 */     int i = 1;
/* 265 */     List localList = ElementFilter.fieldsIn(paramTypeElement.getEnclosedElements());
/*     */ 
/* 267 */     for (VariableElement localVariableElement : localList) {
/* 268 */       TypeKind localTypeKind = localVariableElement.asType().getKind();
/* 269 */       int j = (localTypeKind == TypeKind.LONG) || (localTypeKind == TypeKind.DOUBLE) ? 1 : 0;
/* 270 */       if (j != 0) if (doField(paramFieldDefsRes, localVariableElement, paramString, (i != 0) && (paramBoolean))) {
/* 271 */           paramInt += 8; i = 0;
/*     */         }
/*     */     }
/* 274 */     return paramInt;
/*     */   }
/*     */ 
/*     */   String fieldDefs(TypeElement paramTypeElement, String paramString) {
/* 278 */     FieldDefsRes localFieldDefsRes = fieldDefs(paramTypeElement, paramString, true);
/* 279 */     return localFieldDefsRes.s;
/*     */   }
/*     */ 
/*     */   FieldDefsRes fieldDefs(TypeElement paramTypeElement, String paramString, boolean paramBoolean)
/*     */   {
/* 286 */     int j = 0;
/*     */ 
/* 288 */     TypeElement localTypeElement = (TypeElement)this.types.asElement(paramTypeElement.getSuperclass());
/*     */     FieldDefsRes localFieldDefsRes;
/*     */     int i;
/* 290 */     if (localTypeElement != null) {
/* 291 */       localObject = localTypeElement.getQualifiedName().toString();
/*     */ 
/* 293 */       localFieldDefsRes = new FieldDefsRes(paramTypeElement, 
/* 293 */         fieldDefs(localTypeElement, paramString, false), 
/* 293 */         paramBoolean);
/*     */ 
/* 295 */       i = localFieldDefsRes.parent.byteSize;
/*     */     } else {
/* 297 */       localFieldDefsRes = new FieldDefsRes(paramTypeElement, null, paramBoolean);
/* 298 */       i = 0;
/*     */     }
/*     */ 
/* 301 */     Object localObject = ElementFilter.fieldsIn(paramTypeElement.getEnclosedElements());
/*     */ 
/* 303 */     for (VariableElement localVariableElement : (List)localObject)
/*     */     {
/* 305 */       if ((this.doubleAlign) && (j == 0) && (i % 8 == 0)) {
/* 306 */         i = doTwoWordFields(localFieldDefsRes, paramTypeElement, i, paramString, false);
/* 307 */         j = 1;
/*     */       }
/*     */ 
/* 310 */       TypeKind localTypeKind = localVariableElement.asType().getKind();
/* 311 */       int k = (localTypeKind == TypeKind.LONG) || (localTypeKind == TypeKind.DOUBLE) ? 1 : 0;
/*     */ 
/* 313 */       if (((!this.doubleAlign) || (k == 0)) && 
/* 314 */         (doField(localFieldDefsRes, localVariableElement, paramString, false))) i += 4;
/*     */ 
/*     */     }
/*     */ 
/* 319 */     if ((this.doubleAlign) && (j == 0)) {
/* 320 */       if (i % 8 != 0) i += 4;
/* 321 */       i = doTwoWordFields(localFieldDefsRes, paramTypeElement, i, paramString, true);
/*     */     }
/*     */ 
/* 324 */     localFieldDefsRes.byteSize = i;
/* 325 */     return localFieldDefsRes;
/*     */   }
/*     */ 
/*     */   protected String addStructMember(VariableElement paramVariableElement, String paramString, boolean paramBoolean)
/*     */   {
/* 331 */     String str = null;
/*     */ 
/* 333 */     if (paramVariableElement.getModifiers().contains(Modifier.STATIC)) {
/* 334 */       str = addStaticStructMember(paramVariableElement, paramString);
/*     */     }
/*     */     else
/*     */     {
/* 338 */       TypeMirror localTypeMirror = this.types.erasure(paramVariableElement.asType());
/* 339 */       if (paramBoolean) str = "    java_int padWord" + this.padFieldNum++ + ";" + this.lineSep;
/* 340 */       str = "    " + llniType(localTypeMirror, false, false) + " " + llniFieldName(paramVariableElement);
/* 341 */       if (isLongOrDouble(localTypeMirror)) str = str + "[2]";
/* 342 */       str = str + ";" + this.lineSep;
/*     */     }
/* 344 */     return str;
/*     */   }
/*     */ 
/*     */   protected String addStaticStructMember(VariableElement paramVariableElement, String paramString)
/*     */   {
/* 354 */     String str1 = null;
/* 355 */     Object localObject = null;
/*     */ 
/* 357 */     if (!paramVariableElement.getModifiers().contains(Modifier.STATIC))
/* 358 */       return str1;
/* 359 */     if (!paramVariableElement.getModifiers().contains(Modifier.FINAL)) {
/* 360 */       return str1;
/*     */     }
/* 362 */     localObject = paramVariableElement.getConstantValue();
/*     */ 
/* 364 */     if (localObject != null)
/*     */     {
/* 367 */       String str2 = paramString + "_" + paramVariableElement.getSimpleName();
/* 368 */       String str3 = null;
/* 369 */       long l = 0L;
/*     */ 
/* 371 */       if (((localObject instanceof Byte)) || ((localObject instanceof Short)) || ((localObject instanceof Integer)))
/*     */       {
/* 374 */         str3 = "L";
/* 375 */         l = ((Number)localObject).intValue();
/*     */       }
/* 377 */       else if ((localObject instanceof Long))
/*     */       {
/* 379 */         str3 = isWindows ? "i64" : "LL";
/* 380 */         l = ((Long)localObject).longValue();
/*     */       }
/* 382 */       else if ((localObject instanceof Float)) { str3 = "f";
/* 383 */       } else if ((localObject instanceof Double)) { str3 = "";
/* 384 */       } else if ((localObject instanceof Character)) {
/* 385 */         str3 = "L";
/* 386 */         Character localCharacter = (Character)localObject;
/* 387 */         l = localCharacter.charValue() & 0xFFFF;
/*     */       }
/* 389 */       if (str3 != null)
/*     */       {
/* 393 */         if (((str3.equals("L")) && (l == -2147483648L)) || (
/* 394 */           (str3
/* 394 */           .equals("LL")) && 
/* 394 */           (l == -9223372036854775808L))) {
/* 395 */           str1 = "    #undef  " + str2 + this.lineSep + "    #define " + str2 + " (" + (l + 1L) + str3 + "-1)" + this.lineSep;
/*     */         }
/* 398 */         else if ((str3.equals("L")) || (str3.endsWith("LL"))) {
/* 399 */           str1 = "    #undef  " + str2 + this.lineSep + "    #define " + str2 + " " + l + str3 + this.lineSep;
/*     */         }
/*     */         else {
/* 402 */           str1 = "    #undef  " + str2 + this.lineSep + "    #define " + str2 + " " + localObject + str3 + this.lineSep;
/*     */         }
/*     */       }
/*     */     }
/*     */ 
/* 407 */     return str1;
/*     */   }
/*     */ 
/*     */   protected void methodSectionForClass(PrintWriter paramPrintWriter, TypeElement paramTypeElement, String paramString)
/*     */     throws TypeSignature.SignatureException, Util.Exit
/*     */   {
/* 413 */     String str = methodDecls(paramTypeElement, paramString);
/*     */ 
/* 415 */     if (str.length() != 0) {
/* 416 */       paramPrintWriter.println("/* Native method declarations: */" + this.lineSep);
/* 417 */       paramPrintWriter.println("#ifdef __cplusplus");
/* 418 */       paramPrintWriter.println("extern \"C\" {");
/* 419 */       paramPrintWriter.println("#endif" + this.lineSep);
/* 420 */       paramPrintWriter.println(str);
/* 421 */       paramPrintWriter.println("#ifdef __cplusplus");
/* 422 */       paramPrintWriter.println("}");
/* 423 */       paramPrintWriter.println("#endif");
/*     */     }
/*     */   }
/*     */ 
/*     */   protected String methodDecls(TypeElement paramTypeElement, String paramString)
/*     */     throws TypeSignature.SignatureException, Util.Exit
/*     */   {
/* 430 */     String str = "";
/* 431 */     for (ExecutableElement localExecutableElement : this.methods) {
/* 432 */       if (localExecutableElement.getModifiers().contains(Modifier.NATIVE))
/* 433 */         str = str + methodDecl(localExecutableElement, paramTypeElement, paramString);
/*     */     }
/* 435 */     return str;
/*     */   }
/*     */ 
/*     */   protected String methodDecl(ExecutableElement paramExecutableElement, TypeElement paramTypeElement, String paramString)
/*     */     throws TypeSignature.SignatureException, Util.Exit
/*     */   {
/* 441 */     String str1 = null;
/*     */ 
/* 443 */     TypeMirror localTypeMirror = this.types.erasure(paramExecutableElement.getReturnType());
/* 444 */     String str2 = signature(paramExecutableElement);
/* 445 */     TypeSignature localTypeSignature = new TypeSignature(this.elems);
/* 446 */     String str3 = localTypeSignature.getTypeSignature(str2, localTypeMirror);
/* 447 */     boolean bool = needLongName(paramExecutableElement, paramTypeElement);
/*     */ 
/* 449 */     if (str3.charAt(0) != '(') {
/* 450 */       this.util.error("invalid.method.signature", new Object[] { str3 });
/*     */     }
/*     */ 
/* 454 */     str1 = "JNIEXPORT " + jniType(localTypeMirror) + " JNICALL" + this.lineSep + jniMethodName(paramExecutableElement, paramString, bool) + "(JNIEnv *, " + 
/* 454 */       cRcvrDecl(paramExecutableElement, paramString);
/*     */ 
/* 455 */     List localList = paramExecutableElement.getParameters();
/* 456 */     ArrayList localArrayList = new ArrayList();
/* 457 */     for (Iterator localIterator = localList.iterator(); localIterator.hasNext(); ) { localObject = (VariableElement)localIterator.next();
/* 458 */       localArrayList.add(this.types.erasure(((VariableElement)localObject).asType()));
/*     */     }
/* 465 */     Object localObject;
/* 465 */     for (localIterator = localArrayList.iterator(); localIterator.hasNext(); ) { localObject = (TypeMirror)localIterator.next();
/* 466 */       str1 = str1 + ", " + jniType((TypeMirror)localObject); }
/* 467 */     str1 = str1 + ");" + this.lineSep;
/* 468 */     return str1;
/*     */   }
/*     */ 
/*     */   protected final boolean needLongName(ExecutableElement paramExecutableElement, TypeElement paramTypeElement)
/*     */   {
/* 473 */     Name localName = paramExecutableElement.getSimpleName();
/* 474 */     for (ExecutableElement localExecutableElement : this.methods) {
/* 475 */       if ((localExecutableElement != paramExecutableElement) && 
/* 476 */         (localExecutableElement
/* 476 */         .getModifiers().contains(Modifier.NATIVE)) && 
/* 477 */         (localName
/* 477 */         .equals(localExecutableElement
/* 477 */         .getSimpleName())))
/* 478 */         return true;
/*     */     }
/* 480 */     return false;
/*     */   }
/*     */ 
/*     */   protected final String jniMethodName(ExecutableElement paramExecutableElement, String paramString, boolean paramBoolean)
/*     */     throws TypeSignature.SignatureException
/*     */   {
/* 486 */     String str1 = "Java_" + paramString + "_" + paramExecutableElement.getSimpleName();
/*     */     Iterator localIterator;
/*     */     Object localObject;
/* 488 */     if (paramBoolean) {
/* 489 */       TypeMirror localTypeMirror = this.types.erasure(paramExecutableElement.getReturnType());
/* 490 */       List localList = paramExecutableElement.getParameters();
/* 491 */       ArrayList localArrayList = new ArrayList();
/* 492 */       for (localIterator = localList.iterator(); localIterator.hasNext(); ) { localObject = (VariableElement)localIterator.next();
/* 493 */         localArrayList.add(this.types.erasure(((VariableElement)localObject).asType()));
/*     */       }
/*     */ 
/* 496 */       str1 = str1 + "__";
/* 497 */       for (localIterator = localArrayList.iterator(); localIterator.hasNext(); ) { localObject = (TypeMirror)localIterator.next();
/* 498 */         String str2 = ((TypeMirror)localObject).toString();
/* 499 */         TypeSignature localTypeSignature = new TypeSignature(this.elems);
/* 500 */         String str3 = localTypeSignature.getTypeSignature(str2);
/* 501 */         str1 = str1 + nameToIdentifier(str3);
/*     */       }
/*     */     }
/* 504 */     return str1;
/*     */   }
/*     */ 
/*     */   protected final String jniType(TypeMirror paramTypeMirror) throws Util.Exit
/*     */   {
/* 509 */     TypeElement localTypeElement1 = this.elems.getTypeElement("java.lang.Throwable");
/* 510 */     TypeElement localTypeElement2 = this.elems.getTypeElement("java.lang.Class");
/* 511 */     TypeElement localTypeElement3 = this.elems.getTypeElement("java.lang.String");
/* 512 */     Element localElement = this.types.asElement(paramTypeMirror);
/*     */ 
/* 514 */     switch (2.$SwitchMap$javax$lang$model$type$TypeKind[paramTypeMirror.getKind().ordinal()]) {
/*     */     case 9:
/* 516 */       TypeMirror localTypeMirror = ((ArrayType)paramTypeMirror).getComponentType();
/* 517 */       switch (2.$SwitchMap$javax$lang$model$type$TypeKind[localTypeMirror.getKind().ordinal()]) { case 1:
/* 518 */         return "jbooleanArray";
/*     */       case 2:
/* 519 */         return "jbyteArray";
/*     */       case 3:
/* 520 */         return "jcharArray";
/*     */       case 4:
/* 521 */         return "jshortArray";
/*     */       case 5:
/* 522 */         return "jintArray";
/*     */       case 6:
/* 523 */         return "jlongArray";
/*     */       case 7:
/* 524 */         return "jfloatArray";
/*     */       case 8:
/* 525 */         return "jdoubleArray";
/*     */       case 9:
/*     */       case 10:
/* 527 */         return "jobjectArray"; }
/* 528 */       throw new Error(localTypeMirror.toString());
/*     */     case 11:
/* 532 */       return "void";
/*     */     case 1:
/* 533 */       return "jboolean";
/*     */     case 2:
/* 534 */       return "jbyte";
/*     */     case 3:
/* 535 */       return "jchar";
/*     */     case 4:
/* 536 */       return "jshort";
/*     */     case 5:
/* 537 */       return "jint";
/*     */     case 6:
/* 538 */       return "jlong";
/*     */     case 7:
/* 539 */       return "jfloat";
/*     */     case 8:
/* 540 */       return "jdouble";
/*     */     case 10:
/* 543 */       if (localElement.equals(localTypeElement3))
/* 544 */         return "jstring";
/* 545 */       if (this.types.isAssignable(paramTypeMirror, localTypeElement1.asType()))
/* 546 */         return "jthrowable";
/* 547 */       if (this.types.isAssignable(paramTypeMirror, localTypeElement2.asType())) {
/* 548 */         return "jclass";
/*     */       }
/* 550 */       return "jobject";
/*     */     }
/*     */ 
/* 554 */     this.util.bug("jni.unknown.type");
/* 555 */     return null;
/*     */   }
/*     */ 
/*     */   protected String llniType(TypeMirror paramTypeMirror, boolean paramBoolean1, boolean paramBoolean2) {
/* 559 */     String str = null;
/*     */     Object localObject;
/* 561 */     switch (2.$SwitchMap$javax$lang$model$type$TypeKind[paramTypeMirror.getKind().ordinal()]) {
/*     */     case 9:
/* 563 */       localObject = ((ArrayType)paramTypeMirror).getComponentType();
/* 564 */       switch (2.$SwitchMap$javax$lang$model$type$TypeKind[localObject.getKind().ordinal()]) { case 1:
/* 565 */         str = "IArrayOfBoolean"; break;
/*     */       case 2:
/* 566 */         str = "IArrayOfByte"; break;
/*     */       case 3:
/* 567 */         str = "IArrayOfChar"; break;
/*     */       case 4:
/* 568 */         str = "IArrayOfShort"; break;
/*     */       case 5:
/* 569 */         str = "IArrayOfInt"; break;
/*     */       case 6:
/* 570 */         str = "IArrayOfLong"; break;
/*     */       case 7:
/* 571 */         str = "IArrayOfFloat"; break;
/*     */       case 8:
/* 572 */         str = "IArrayOfDouble"; break;
/*     */       case 9:
/*     */       case 10:
/* 574 */         str = "IArrayOfRef"; break;
/*     */       default:
/* 575 */         throw new Error(((TypeMirror)localObject).getKind() + " " + localObject);
/*     */       }
/* 577 */       if (!paramBoolean1) str = "DEREFERENCED_" + str; break;
/*     */     case 11:
/* 582 */       str = "void";
/* 583 */       break;
/*     */     case 1:
/*     */     case 2:
/*     */     case 3:
/*     */     case 4:
/*     */     case 5:
/* 590 */       str = "java_int";
/* 591 */       break;
/*     */     case 6:
/* 594 */       str = paramBoolean2 ? "java_long" : "val32 /* java_long */";
/* 595 */       break;
/*     */     case 7:
/* 598 */       str = "java_float";
/* 599 */       break;
/*     */     case 8:
/* 602 */       str = paramBoolean2 ? "java_double" : "val32 /* java_double */";
/* 603 */       break;
/*     */     case 10:
/* 606 */       localObject = (TypeElement)this.types.asElement(paramTypeMirror);
/* 607 */       str = "I" + mangleClassName(((TypeElement)localObject).getQualifiedName().toString());
/* 608 */       if (!paramBoolean1) str = "DEREFERENCED_" + str; break;
/*     */     default:
/* 612 */       throw new Error(paramTypeMirror.getKind() + " " + paramTypeMirror);
/*     */     }
/*     */ 
/* 615 */     return str;
/*     */   }
/*     */ 
/*     */   protected final String cRcvrDecl(Element paramElement, String paramString) {
/* 619 */     return paramElement.getModifiers().contains(Modifier.STATIC) ? "jclass" : "jobject";
/*     */   }
/*     */ 
/*     */   protected String maskName(String paramString) {
/* 623 */     return "LLNI_mask(" + paramString + ")";
/*     */   }
/*     */ 
/*     */   protected String llniFieldName(VariableElement paramVariableElement) {
/* 627 */     return maskName(paramVariableElement.getSimpleName().toString());
/*     */   }
/*     */ 
/*     */   protected final boolean isLongOrDouble(TypeMirror paramTypeMirror) {
/* 631 */     SimpleTypeVisitor8 local1 = new SimpleTypeVisitor8() {
/*     */       public Boolean defaultAction(TypeMirror paramAnonymousTypeMirror, Void paramAnonymousVoid) {
/* 633 */         return Boolean.valueOf(false);
/*     */       }
/*     */       public Boolean visitArray(ArrayType paramAnonymousArrayType, Void paramAnonymousVoid) {
/* 636 */         return (Boolean)visit(paramAnonymousArrayType.getComponentType(), paramAnonymousVoid);
/*     */       }
/*     */       public Boolean visitPrimitive(PrimitiveType paramAnonymousPrimitiveType, Void paramAnonymousVoid) {
/* 639 */         TypeKind localTypeKind = paramAnonymousPrimitiveType.getKind();
/* 640 */         return Boolean.valueOf((localTypeKind == TypeKind.LONG) || (localTypeKind == TypeKind.DOUBLE));
/*     */       }
/*     */     };
/* 643 */     return ((Boolean)local1.visit(paramTypeMirror, null)).booleanValue();
/*     */   }
/*     */ 
/*     */   protected final String nameToIdentifier(String paramString)
/*     */   {
/* 649 */     int i = paramString.length();
/* 650 */     StringBuilder localStringBuilder = new StringBuilder(i);
/* 651 */     for (int j = 0; j < i; j++) {
/* 652 */       char c = paramString.charAt(j);
/* 653 */       if (isASCIILetterOrDigit(c))
/* 654 */         localStringBuilder.append(c);
/* 655 */       else if (c == '/')
/* 656 */         localStringBuilder.append('_');
/* 657 */       else if (c == '.')
/* 658 */         localStringBuilder.append('_');
/* 659 */       else if (c == '_')
/* 660 */         localStringBuilder.append("_1");
/* 661 */       else if (c == ';')
/* 662 */         localStringBuilder.append("_2");
/* 663 */       else if (c == '[')
/* 664 */         localStringBuilder.append("_3");
/*     */       else
/* 666 */         localStringBuilder.append("_0" + c);
/*     */     }
/* 668 */     return new String(localStringBuilder);
/*     */   }
/*     */ 
/*     */   protected final boolean isASCIILetterOrDigit(char paramChar) {
/* 672 */     if (((paramChar >= 'A') && (paramChar <= 'Z')) || ((paramChar >= 'a') && (paramChar <= 'z')) || ((paramChar >= '0') && (paramChar <= '9')))
/*     */     {
/* 675 */       return true;
/*     */     }
/* 677 */     return false;
/*     */   }
/*     */ 
/*     */   private static class FieldDefsRes
/*     */   {
/*     */     public String className;
/*     */     public FieldDefsRes parent;
/*     */     public String s;
/*     */     public int byteSize;
/*     */     public boolean bottomMost;
/* 226 */     public boolean printedOne = false;
/*     */ 
/*     */     FieldDefsRes(TypeElement paramTypeElement, FieldDefsRes paramFieldDefsRes, boolean paramBoolean) {
/* 229 */       this.className = paramTypeElement.getQualifiedName().toString();
/* 230 */       this.parent = paramFieldDefsRes;
/* 231 */       this.bottomMost = paramBoolean;
/* 232 */       int i = 0;
/* 233 */       if (paramFieldDefsRes == null) this.s = ""; else
/* 234 */         this.s = paramFieldDefsRes.s;
/*     */     }
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.javah.LLNI
 * JD-Core Version:    0.6.2
 */