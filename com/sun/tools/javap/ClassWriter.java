/*     */ package com.sun.tools.javap;
/*     */ 
/*     */ import com.sun.tools.classfile.AccessFlags;
/*     */ import com.sun.tools.classfile.Attribute;
/*     */ import com.sun.tools.classfile.Attributes;
/*     */ import com.sun.tools.classfile.ClassFile;
/*     */ import com.sun.tools.classfile.Code_attribute;
/*     */ import com.sun.tools.classfile.ConstantPool;
/*     */ import com.sun.tools.classfile.ConstantPool.CONSTANT_Integer_info;
/*     */ import com.sun.tools.classfile.ConstantPool.CONSTANT_String_info;
/*     */ import com.sun.tools.classfile.ConstantPool.CPInfo;
/*     */ import com.sun.tools.classfile.ConstantPoolException;
/*     */ import com.sun.tools.classfile.ConstantValue_attribute;
/*     */ import com.sun.tools.classfile.Descriptor;
/*     */ import com.sun.tools.classfile.DescriptorException;
/*     */ import com.sun.tools.classfile.Exceptions_attribute;
/*     */ import com.sun.tools.classfile.Field;
/*     */ import com.sun.tools.classfile.Method;
/*     */ import com.sun.tools.classfile.Signature;
/*     */ import com.sun.tools.classfile.Signature_attribute;
/*     */ import com.sun.tools.classfile.SourceFile_attribute;
/*     */ import com.sun.tools.classfile.Type;
/*     */ import com.sun.tools.classfile.Type.ArrayType;
/*     */ import com.sun.tools.classfile.Type.ClassSigType;
/*     */ import com.sun.tools.classfile.Type.ClassType;
/*     */ import com.sun.tools.classfile.Type.MethodType;
/*     */ import com.sun.tools.classfile.Type.SimpleType;
/*     */ import com.sun.tools.classfile.Type.TypeParamType;
/*     */ import com.sun.tools.classfile.Type.Visitor;
/*     */ import com.sun.tools.classfile.Type.WildcardType;
/*     */ import java.net.URI;
/*     */ import java.text.DateFormat;
/*     */ import java.util.Collection;
/*     */ import java.util.Date;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ 
/*     */ public class ClassWriter extends BasicWriter
/*     */ {
/*     */   private Options options;
/*     */   private AttributeWriter attrWriter;
/*     */   private CodeWriter codeWriter;
/*     */   private ConstantWriter constantWriter;
/*     */   private ClassFile classFile;
/*     */   private URI uri;
/*     */   private long lastModified;
/*     */   private String digestName;
/*     */   private byte[] digest;
/*     */   private int size;
/*     */   private ConstantPool constant_pool;
/*     */   private Method method;
/*     */ 
/*     */   static ClassWriter instance(Context paramContext)
/*     */   {
/*  71 */     ClassWriter localClassWriter = (ClassWriter)paramContext.get(ClassWriter.class);
/*  72 */     if (localClassWriter == null)
/*  73 */       localClassWriter = new ClassWriter(paramContext);
/*  74 */     return localClassWriter;
/*     */   }
/*     */ 
/*     */   protected ClassWriter(Context paramContext) {
/*  78 */     super(paramContext);
/*  79 */     paramContext.put(ClassWriter.class, this);
/*  80 */     this.options = Options.instance(paramContext);
/*  81 */     this.attrWriter = AttributeWriter.instance(paramContext);
/*  82 */     this.codeWriter = CodeWriter.instance(paramContext);
/*  83 */     this.constantWriter = ConstantWriter.instance(paramContext);
/*     */   }
/*     */ 
/*     */   void setDigest(String paramString, byte[] paramArrayOfByte) {
/*  87 */     this.digestName = paramString;
/*  88 */     this.digest = paramArrayOfByte;
/*     */   }
/*     */ 
/*     */   void setFile(URI paramURI) {
/*  92 */     this.uri = paramURI;
/*     */   }
/*     */ 
/*     */   void setFileSize(int paramInt) {
/*  96 */     this.size = paramInt;
/*     */   }
/*     */ 
/*     */   void setLastModified(long paramLong) {
/* 100 */     this.lastModified = paramLong;
/*     */   }
/*     */ 
/*     */   protected ClassFile getClassFile() {
/* 104 */     return this.classFile;
/*     */   }
/*     */ 
/*     */   protected void setClassFile(ClassFile paramClassFile) {
/* 108 */     this.classFile = paramClassFile;
/* 109 */     this.constant_pool = this.classFile.constant_pool;
/*     */   }
/*     */ 
/*     */   protected Method getMethod() {
/* 113 */     return this.method;
/*     */   }
/*     */ 
/*     */   protected void setMethod(Method paramMethod) {
/* 117 */     this.method = paramMethod;
/*     */   }
/*     */ 
/*     */   public void write(ClassFile paramClassFile) {
/* 121 */     setClassFile(paramClassFile);
/*     */ 
/* 123 */     if ((this.options.sysInfo) || (this.options.verbose)) {
/* 124 */       if (this.uri != null) {
/* 125 */         if (this.uri.getScheme().equals("file"))
/* 126 */           println("Classfile " + this.uri.getPath());
/*     */         else
/* 128 */           println("Classfile " + this.uri);
/*     */       }
/* 130 */       indent(1);
/* 131 */       if (this.lastModified != -1L) {
/* 132 */         localObject1 = new Date(this.lastModified);
/* 133 */         localObject2 = DateFormat.getDateInstance();
/* 134 */         if (this.size > 0)
/* 135 */           println("Last modified " + ((DateFormat)localObject2).format((Date)localObject1) + "; size " + this.size + " bytes");
/*     */         else
/* 137 */           println("Last modified " + ((DateFormat)localObject2).format((Date)localObject1));
/*     */       }
/* 139 */       else if (this.size > 0) {
/* 140 */         println("Size " + this.size + " bytes");
/*     */       }
/* 142 */       if ((this.digestName != null) && (this.digest != null)) {
/* 143 */         localObject1 = new StringBuilder();
/* 144 */         for (byte b : this.digest)
/* 145 */           ((StringBuilder)localObject1).append(String.format("%02x", new Object[] { Byte.valueOf(b) }));
/* 146 */         println(this.digestName + " checksum " + localObject1);
/*     */       }
/*     */     }
/*     */ 
/* 150 */     Object localObject1 = paramClassFile.getAttribute("SourceFile");
/* 151 */     if ((localObject1 instanceof SourceFile_attribute)) {
/* 152 */       println("Compiled from \"" + getSourceFile((SourceFile_attribute)localObject1) + "\"");
/*     */     }
/*     */ 
/* 155 */     if ((this.options.sysInfo) || (this.options.verbose)) {
/* 156 */       indent(-1);
/*     */     }
/*     */ 
/* 159 */     Object localObject2 = getJavaName(this.classFile);
/* 160 */     AccessFlags localAccessFlags = paramClassFile.access_flags;
/*     */ 
/* 162 */     writeModifiers(localAccessFlags.getClassModifiers());
/*     */ 
/* 164 */     if (this.classFile.isClass())
/* 165 */       print("class ");
/* 166 */     else if (this.classFile.isInterface()) {
/* 167 */       print("interface ");
/*     */     }
/* 169 */     print((String)localObject2);
/*     */ 
/* 171 */     Signature_attribute localSignature_attribute = getSignature(paramClassFile.attributes);
/* 172 */     if (localSignature_attribute == null)
/*     */     {
/* 174 */       if ((this.classFile.isClass()) && (this.classFile.super_class != 0)) {
/* 175 */         String str = getJavaSuperclassName(paramClassFile);
/* 176 */         if (!str.equals("java.lang.Object")) {
/* 177 */           print(" extends ");
/* 178 */           print(str);
/*     */         }
/*     */       }
/* 181 */       for (int k = 0; k < this.classFile.interfaces.length; k++) {
/* 182 */         print(k == 0 ? " extends " : this.classFile.isClass() ? " implements " : ",");
/* 183 */         print(getJavaInterfaceName(this.classFile, k));
/*     */       }
/*     */     } else {
/*     */       try {
/* 187 */         Type localType = localSignature_attribute.getParsedSignature().getType(this.constant_pool);
/* 188 */         JavaTypePrinter localJavaTypePrinter = new JavaTypePrinter(this.classFile.isInterface());
/*     */ 
/* 191 */         if ((localType instanceof Type.ClassSigType)) {
/* 192 */           print(localJavaTypePrinter.print(localType));
/* 193 */         } else if ((this.options.verbose) || (!localType.isObject())) {
/* 194 */           print(" extends ");
/* 195 */           print(localJavaTypePrinter.print(localType));
/*     */         }
/*     */       } catch (ConstantPoolException localConstantPoolException) {
/* 198 */         print(report(localConstantPoolException));
/*     */       }
/*     */     }
/*     */ 
/* 202 */     if (this.options.verbose) {
/* 203 */       println();
/* 204 */       indent(1);
/* 205 */       println("minor version: " + paramClassFile.minor_version);
/* 206 */       println("major version: " + paramClassFile.major_version);
/* 207 */       writeList("flags: ", localAccessFlags.getClassFlags(), "\n");
/* 208 */       indent(-1);
/* 209 */       this.constantWriter.writeConstantPool();
/*     */     } else {
/* 211 */       print(" ");
/*     */     }
/*     */ 
/* 214 */     println("{");
/* 215 */     indent(1);
/* 216 */     writeFields();
/* 217 */     writeMethods();
/* 218 */     indent(-1);
/* 219 */     println("}");
/*     */ 
/* 221 */     if (this.options.verbose)
/* 222 */       this.attrWriter.write(paramClassFile, paramClassFile.attributes, this.constant_pool);
/*     */   }
/*     */ 
/*     */   protected void writeFields()
/*     */   {
/* 351 */     for (Field localField : this.classFile.fields)
/* 352 */       writeField(localField);
/*     */   }
/*     */ 
/*     */   protected void writeField(Field paramField)
/*     */   {
/* 357 */     if (!this.options.checkAccess(paramField.access_flags)) {
/* 358 */       return;
/*     */     }
/* 360 */     AccessFlags localAccessFlags = paramField.access_flags;
/* 361 */     writeModifiers(localAccessFlags.getFieldModifiers());
/* 362 */     Signature_attribute localSignature_attribute = getSignature(paramField.attributes);
/* 363 */     if (localSignature_attribute == null)
/* 364 */       print(getJavaFieldType(paramField.descriptor));
/*     */     else {
/*     */       try {
/* 367 */         Type localType = localSignature_attribute.getParsedSignature().getType(this.constant_pool);
/* 368 */         print(getJavaName(localType.toString()));
/*     */       }
/*     */       catch (ConstantPoolException localConstantPoolException)
/*     */       {
/* 372 */         print(getJavaFieldType(paramField.descriptor));
/*     */       }
/*     */     }
/* 375 */     print(" ");
/* 376 */     print(getFieldName(paramField));
/*     */     Object localObject;
/* 377 */     if (this.options.showConstants) {
/* 378 */       Attribute localAttribute1 = paramField.attributes.get("ConstantValue");
/* 379 */       if ((localAttribute1 instanceof ConstantValue_attribute)) {
/* 380 */         print(" = ");
/* 381 */         localObject = (ConstantValue_attribute)localAttribute1;
/* 382 */         print(getConstantValue(paramField.descriptor, ((ConstantValue_attribute)localObject).constantvalue_index));
/*     */       }
/*     */     }
/* 385 */     print(";");
/* 386 */     println();
/*     */ 
/* 388 */     indent(1);
/*     */ 
/* 390 */     int i = 0;
/*     */ 
/* 392 */     if (this.options.showDescriptors) {
/* 393 */       println("descriptor: " + getValue(paramField.descriptor));
/*     */     }
/* 395 */     if (this.options.verbose) {
/* 396 */       writeList("flags: ", localAccessFlags.getFieldFlags(), "\n");
/*     */     }
/* 398 */     if (this.options.showAllAttrs) {
/* 399 */       for (localObject = paramField.attributes.iterator(); ((Iterator)localObject).hasNext(); ) { Attribute localAttribute2 = (Attribute)((Iterator)localObject).next();
/* 400 */         this.attrWriter.write(paramField, localAttribute2, this.constant_pool); }
/* 401 */       i = 1;
/*     */     }
/*     */ 
/* 404 */     indent(-1);
/*     */ 
/* 406 */     if ((i != 0) || (this.options.showDisassembled) || (this.options.showLineAndLocalVariableTables))
/* 407 */       println();
/*     */   }
/*     */ 
/*     */   protected void writeMethods() {
/* 411 */     for (Method localMethod : this.classFile.methods)
/* 412 */       writeMethod(localMethod);
/* 413 */     setPendingNewline(false);
/*     */   }
/*     */ 
/*     */   protected void writeMethod(Method paramMethod) {
/* 417 */     if (!this.options.checkAccess(paramMethod.access_flags)) {
/* 418 */       return;
/*     */     }
/* 420 */     this.method = paramMethod;
/*     */ 
/* 422 */     AccessFlags localAccessFlags = paramMethod.access_flags;
/*     */ 
/* 428 */     Signature_attribute localSignature_attribute = getSignature(paramMethod.attributes);
/*     */     Object localObject1;
/*     */     Type.MethodType localMethodType;
/*     */     List localList;
/* 429 */     if (localSignature_attribute == null) {
/* 430 */       localObject1 = paramMethod.descriptor;
/* 431 */       localMethodType = null;
/* 432 */       localList = null;
/*     */     } else {
/* 434 */       localObject2 = localSignature_attribute.getParsedSignature();
/* 435 */       localObject1 = localObject2;
/*     */       try {
/* 437 */         localMethodType = (Type.MethodType)((Signature)localObject2).getType(this.constant_pool);
/* 438 */         localList = localMethodType.throwsTypes;
/* 439 */         if ((localList != null) && (localList.isEmpty()))
/* 440 */           localList = null;
/*     */       }
/*     */       catch (ConstantPoolException localConstantPoolException)
/*     */       {
/* 444 */         localMethodType = null;
/* 445 */         localList = null;
/*     */       }
/*     */     }
/*     */ 
/* 449 */     writeModifiers(localAccessFlags.getMethodModifiers());
/* 450 */     if (localMethodType != null) {
/* 451 */       print(new JavaTypePrinter(false).printTypeArgs(localMethodType.typeParamTypes));
/*     */     }
/* 453 */     if (getName(paramMethod).equals("<init>")) {
/* 454 */       print(getJavaName(this.classFile));
/* 455 */       print(getJavaParameterTypes((Descriptor)localObject1, localAccessFlags));
/* 456 */     } else if (getName(paramMethod).equals("<clinit>")) {
/* 457 */       print("{}");
/*     */     } else {
/* 459 */       print(getJavaReturnType((Descriptor)localObject1));
/* 460 */       print(" ");
/* 461 */       print(getName(paramMethod));
/* 462 */       print(getJavaParameterTypes((Descriptor)localObject1, localAccessFlags));
/*     */     }
/*     */ 
/* 465 */     Object localObject2 = paramMethod.attributes.get("Exceptions");
/* 466 */     if (localObject2 != null) {
/* 467 */       if ((localObject2 instanceof Exceptions_attribute)) {
/* 468 */         localObject3 = (Exceptions_attribute)localObject2;
/* 469 */         print(" throws ");
/* 470 */         if (localList != null)
/* 471 */           writeList("", localList, "");
/*     */         else
/* 473 */           for (int i = 0; i < ((Exceptions_attribute)localObject3).number_of_exceptions; i++) {
/* 474 */             if (i > 0)
/* 475 */               print(", ");
/* 476 */             print(getJavaException((Exceptions_attribute)localObject3, i));
/*     */           }
/*     */       }
/*     */       else {
/* 480 */         report("Unexpected or invalid value for Exceptions attribute");
/*     */       }
/*     */     }
/*     */ 
/* 484 */     println(";");
/*     */ 
/* 486 */     indent(1);
/*     */ 
/* 488 */     if (this.options.showDescriptors) {
/* 489 */       println("descriptor: " + getValue(paramMethod.descriptor));
/*     */     }
/*     */ 
/* 492 */     if (this.options.verbose) {
/* 493 */       writeList("flags: ", localAccessFlags.getMethodFlags(), "\n");
/*     */     }
/*     */ 
/* 496 */     Object localObject3 = null;
/* 497 */     Attribute localAttribute1 = paramMethod.attributes.get("Code");
/* 498 */     if (localAttribute1 != null) {
/* 499 */       if ((localAttribute1 instanceof Code_attribute))
/* 500 */         localObject3 = (Code_attribute)localAttribute1;
/*     */       else {
/* 502 */         report("Unexpected or invalid value for Code attribute");
/*     */       }
/*     */     }
/* 505 */     if (this.options.showAllAttrs) {
/* 506 */       Attribute[] arrayOfAttribute1 = paramMethod.attributes.attrs;
/* 507 */       for (Attribute localAttribute2 : arrayOfAttribute1)
/* 508 */         this.attrWriter.write(paramMethod, localAttribute2, this.constant_pool);
/* 509 */     } else if (localObject3 != null) {
/* 510 */       if (this.options.showDisassembled) {
/* 511 */         println("Code:");
/* 512 */         this.codeWriter.writeInstrs((Code_attribute)localObject3);
/* 513 */         this.codeWriter.writeExceptionTable((Code_attribute)localObject3);
/*     */       }
/*     */ 
/* 516 */       if (this.options.showLineAndLocalVariableTables) {
/* 517 */         this.attrWriter.write(localObject3, ((Code_attribute)localObject3).attributes.get("LineNumberTable"), this.constant_pool);
/* 518 */         this.attrWriter.write(localObject3, ((Code_attribute)localObject3).attributes.get("LocalVariableTable"), this.constant_pool);
/*     */       }
/*     */     }
/*     */ 
/* 522 */     indent(-1);
/*     */ 
/* 526 */     setPendingNewline((this.options.showDisassembled) || (this.options.showAllAttrs) || (this.options.showDescriptors) || (this.options.showLineAndLocalVariableTables) || (this.options.verbose));
/*     */   }
/*     */ 
/*     */   void writeModifiers(Collection<String> paramCollection)
/*     */   {
/* 535 */     for (Iterator localIterator = paramCollection.iterator(); localIterator.hasNext(); ) { Object localObject = localIterator.next();
/* 536 */       print(localObject);
/* 537 */       print(" "); }
/*     */   }
/*     */ 
/*     */   void writeList(String paramString1, Collection<?> paramCollection, String paramString2)
/*     */   {
/* 542 */     print(paramString1);
/* 543 */     String str = "";
/* 544 */     for (Iterator localIterator = paramCollection.iterator(); localIterator.hasNext(); ) { Object localObject = localIterator.next();
/* 545 */       print(str);
/* 546 */       print(localObject);
/* 547 */       str = ", ";
/*     */     }
/* 549 */     print(paramString2);
/*     */   }
/*     */ 
/*     */   void writeListIfNotEmpty(String paramString1, List<?> paramList, String paramString2) {
/* 553 */     if ((paramList != null) && (paramList.size() > 0))
/* 554 */       writeList(paramString1, paramList, paramString2);
/*     */   }
/*     */ 
/*     */   Signature_attribute getSignature(Attributes paramAttributes) {
/* 558 */     return (Signature_attribute)paramAttributes.get("Signature");
/*     */   }
/*     */ 
/*     */   String adjustVarargs(AccessFlags paramAccessFlags, String paramString) {
/* 562 */     if (paramAccessFlags.is(128)) {
/* 563 */       int i = paramString.lastIndexOf("[]");
/* 564 */       if (i > 0) {
/* 565 */         return paramString.substring(0, i) + "..." + paramString.substring(i + 2);
/*     */       }
/*     */     }
/* 568 */     return paramString;
/*     */   }
/*     */ 
/*     */   String getJavaName(ClassFile paramClassFile) {
/*     */     try {
/* 573 */       return getJavaName(paramClassFile.getName());
/*     */     } catch (ConstantPoolException localConstantPoolException) {
/* 575 */       return report(localConstantPoolException);
/*     */     }
/*     */   }
/*     */ 
/*     */   String getJavaSuperclassName(ClassFile paramClassFile) {
/*     */     try {
/* 581 */       return getJavaName(paramClassFile.getSuperclassName());
/*     */     } catch (ConstantPoolException localConstantPoolException) {
/* 583 */       return report(localConstantPoolException);
/*     */     }
/*     */   }
/*     */ 
/*     */   String getJavaInterfaceName(ClassFile paramClassFile, int paramInt) {
/*     */     try {
/* 589 */       return getJavaName(paramClassFile.getInterfaceName(paramInt));
/*     */     } catch (ConstantPoolException localConstantPoolException) {
/* 591 */       return report(localConstantPoolException);
/*     */     }
/*     */   }
/*     */ 
/*     */   String getJavaFieldType(Descriptor paramDescriptor) {
/*     */     try {
/* 597 */       return getJavaName(paramDescriptor.getFieldType(this.constant_pool));
/*     */     } catch (ConstantPoolException localConstantPoolException) {
/* 599 */       return report(localConstantPoolException);
/*     */     } catch (DescriptorException localDescriptorException) {
/* 601 */       return report(localDescriptorException);
/*     */     }
/*     */   }
/*     */ 
/*     */   String getJavaReturnType(Descriptor paramDescriptor) {
/*     */     try {
/* 607 */       return getJavaName(paramDescriptor.getReturnType(this.constant_pool));
/*     */     } catch (ConstantPoolException localConstantPoolException) {
/* 609 */       return report(localConstantPoolException);
/*     */     } catch (DescriptorException localDescriptorException) {
/* 611 */       return report(localDescriptorException);
/*     */     }
/*     */   }
/*     */ 
/*     */   String getJavaParameterTypes(Descriptor paramDescriptor, AccessFlags paramAccessFlags) {
/*     */     try {
/* 617 */       return getJavaName(adjustVarargs(paramAccessFlags, paramDescriptor.getParameterTypes(this.constant_pool)));
/*     */     } catch (ConstantPoolException localConstantPoolException) {
/* 619 */       return report(localConstantPoolException);
/*     */     } catch (DescriptorException localDescriptorException) {
/* 621 */       return report(localDescriptorException);
/*     */     }
/*     */   }
/*     */ 
/*     */   String getJavaException(Exceptions_attribute paramExceptions_attribute, int paramInt) {
/*     */     try {
/* 627 */       return getJavaName(paramExceptions_attribute.getException(paramInt, this.constant_pool));
/*     */     } catch (ConstantPoolException localConstantPoolException) {
/* 629 */       return report(localConstantPoolException);
/*     */     }
/*     */   }
/*     */ 
/*     */   String getValue(Descriptor paramDescriptor) {
/*     */     try {
/* 635 */       return paramDescriptor.getValue(this.constant_pool);
/*     */     } catch (ConstantPoolException localConstantPoolException) {
/* 637 */       return report(localConstantPoolException);
/*     */     }
/*     */   }
/*     */ 
/*     */   String getFieldName(Field paramField) {
/*     */     try {
/* 643 */       return paramField.getName(this.constant_pool);
/*     */     } catch (ConstantPoolException localConstantPoolException) {
/* 645 */       return report(localConstantPoolException);
/*     */     }
/*     */   }
/*     */ 
/*     */   String getName(Method paramMethod) {
/*     */     try {
/* 651 */       return paramMethod.getName(this.constant_pool);
/*     */     } catch (ConstantPoolException localConstantPoolException) {
/* 653 */       return report(localConstantPoolException);
/*     */     }
/*     */   }
/*     */ 
/*     */   static String getJavaName(String paramString) {
/* 658 */     return paramString.replace('/', '.');
/*     */   }
/*     */ 
/*     */   String getSourceFile(SourceFile_attribute paramSourceFile_attribute) {
/*     */     try {
/* 663 */       return paramSourceFile_attribute.getSourceFile(this.constant_pool);
/*     */     } catch (ConstantPoolException localConstantPoolException) {
/* 665 */       return report(localConstantPoolException);
/*     */     }
/*     */   }
/*     */ 
/*     */   String getConstantValue(Descriptor paramDescriptor, int paramInt)
/*     */   {
/*     */     try
/*     */     {
/* 680 */       ConstantPool.CPInfo localCPInfo = this.constant_pool.get(paramInt);
/*     */       Object localObject;
/* 682 */       switch (localCPInfo.getTag()) {
/*     */       case 3:
/* 684 */         localObject = (ConstantPool.CONSTANT_Integer_info)localCPInfo;
/*     */ 
/* 686 */         String str = paramDescriptor.getValue(this.constant_pool);
/* 687 */         if (str.equals("C"))
/* 688 */           return getConstantCharValue((char)((ConstantPool.CONSTANT_Integer_info)localObject).value);
/* 689 */         if (str.equals("Z")) {
/* 690 */           return String.valueOf(((ConstantPool.CONSTANT_Integer_info)localObject).value == 1);
/*     */         }
/* 692 */         return String.valueOf(((ConstantPool.CONSTANT_Integer_info)localObject).value);
/*     */       case 8:
/* 697 */         localObject = (ConstantPool.CONSTANT_String_info)localCPInfo;
/*     */ 
/* 699 */         return getConstantStringValue(((ConstantPool.CONSTANT_String_info)localObject).getString());
/*     */       }
/*     */ 
/* 703 */       return this.constantWriter.stringValue(localCPInfo);
/*     */     } catch (ConstantPoolException localConstantPoolException) {
/*     */     }
/* 706 */     return "#" + paramInt;
/*     */   }
/*     */ 
/*     */   private String getConstantCharValue(char paramChar)
/*     */   {
/* 711 */     StringBuilder localStringBuilder = new StringBuilder();
/* 712 */     localStringBuilder.append('\'');
/* 713 */     localStringBuilder.append(esc(paramChar, '\''));
/* 714 */     localStringBuilder.append('\'');
/* 715 */     return localStringBuilder.toString();
/*     */   }
/*     */ 
/*     */   private String getConstantStringValue(String paramString) {
/* 719 */     StringBuilder localStringBuilder = new StringBuilder();
/* 720 */     localStringBuilder.append("\"");
/* 721 */     for (int i = 0; i < paramString.length(); i++) {
/* 722 */       localStringBuilder.append(esc(paramString.charAt(i), '"'));
/*     */     }
/* 724 */     localStringBuilder.append("\"");
/* 725 */     return localStringBuilder.toString();
/*     */   }
/*     */ 
/*     */   private String esc(char paramChar1, char paramChar2) {
/* 729 */     if ((' ' <= paramChar1) && (paramChar1 <= '~') && (paramChar1 != paramChar2))
/* 730 */       return String.valueOf(paramChar1);
/* 731 */     switch (paramChar1) { case '\b':
/* 732 */       return "\\b";
/*     */     case '\n':
/* 733 */       return "\\n";
/*     */     case '\t':
/* 734 */       return "\\t";
/*     */     case '\f':
/* 735 */       return "\\f";
/*     */     case '\r':
/* 736 */       return "\\r";
/*     */     case '\\':
/* 737 */       return "\\\\";
/*     */     case '\'':
/* 738 */       return "\\'";
/*     */     case '"':
/* 739 */       return "\\\""; }
/* 740 */     return String.format("\\u%04x", new Object[] { Integer.valueOf(paramChar1) });
/*     */   }
/*     */ 
/*     */   class JavaTypePrinter
/*     */     implements Type.Visitor<StringBuilder, StringBuilder>
/*     */   {
/*     */     boolean isInterface;
/*     */ 
/*     */     JavaTypePrinter(boolean arg2)
/*     */     {
/*     */       boolean bool;
/* 230 */       this.isInterface = bool;
/*     */     }
/*     */ 
/*     */     String print(Type paramType) {
/* 234 */       return ((StringBuilder)paramType.accept(this, new StringBuilder())).toString();
/*     */     }
/*     */ 
/*     */     String printTypeArgs(List<? extends Type.TypeParamType> paramList) {
/* 238 */       StringBuilder localStringBuilder = new StringBuilder();
/* 239 */       appendIfNotEmpty(localStringBuilder, "<", paramList, "> ");
/* 240 */       return localStringBuilder.toString();
/*     */     }
/*     */ 
/*     */     public StringBuilder visitSimpleType(Type.SimpleType paramSimpleType, StringBuilder paramStringBuilder) {
/* 244 */       paramStringBuilder.append(ClassWriter.getJavaName(paramSimpleType.name));
/* 245 */       return paramStringBuilder;
/*     */     }
/*     */ 
/*     */     public StringBuilder visitArrayType(Type.ArrayType paramArrayType, StringBuilder paramStringBuilder) {
/* 249 */       append(paramStringBuilder, paramArrayType.elemType);
/* 250 */       paramStringBuilder.append("[]");
/* 251 */       return paramStringBuilder;
/*     */     }
/*     */ 
/*     */     public StringBuilder visitMethodType(Type.MethodType paramMethodType, StringBuilder paramStringBuilder) {
/* 255 */       appendIfNotEmpty(paramStringBuilder, "<", paramMethodType.typeParamTypes, "> ");
/* 256 */       append(paramStringBuilder, paramMethodType.returnType);
/* 257 */       append(paramStringBuilder, " (", paramMethodType.paramTypes, ")");
/* 258 */       appendIfNotEmpty(paramStringBuilder, " throws ", paramMethodType.throwsTypes, "");
/* 259 */       return paramStringBuilder;
/*     */     }
/*     */ 
/*     */     public StringBuilder visitClassSigType(Type.ClassSigType paramClassSigType, StringBuilder paramStringBuilder) {
/* 263 */       appendIfNotEmpty(paramStringBuilder, "<", paramClassSigType.typeParamTypes, ">");
/* 264 */       if (this.isInterface) {
/* 265 */         appendIfNotEmpty(paramStringBuilder, " extends ", paramClassSigType.superinterfaceTypes, "");
/*     */       } else {
/* 267 */         if ((paramClassSigType.superclassType != null) && (
/* 268 */           (ClassWriter.this.options.verbose) || 
/* 268 */           (!paramClassSigType.superclassType.isObject()))) {
/* 269 */           paramStringBuilder.append(" extends ");
/* 270 */           append(paramStringBuilder, paramClassSigType.superclassType);
/*     */         }
/* 272 */         appendIfNotEmpty(paramStringBuilder, " implements ", paramClassSigType.superinterfaceTypes, "");
/*     */       }
/* 274 */       return paramStringBuilder;
/*     */     }
/*     */ 
/*     */     public StringBuilder visitClassType(Type.ClassType paramClassType, StringBuilder paramStringBuilder) {
/* 278 */       if (paramClassType.outerType != null) {
/* 279 */         append(paramStringBuilder, paramClassType.outerType);
/* 280 */         paramStringBuilder.append(".");
/*     */       }
/* 282 */       paramStringBuilder.append(ClassWriter.getJavaName(paramClassType.name));
/* 283 */       appendIfNotEmpty(paramStringBuilder, "<", paramClassType.typeArgs, ">");
/* 284 */       return paramStringBuilder;
/*     */     }
/*     */ 
/*     */     public StringBuilder visitTypeParamType(Type.TypeParamType paramTypeParamType, StringBuilder paramStringBuilder) {
/* 288 */       paramStringBuilder.append(paramTypeParamType.name);
/* 289 */       String str = " extends ";
/* 290 */       if ((paramTypeParamType.classBound != null) && (
/* 291 */         (ClassWriter.this.options.verbose) || 
/* 291 */         (!paramTypeParamType.classBound.isObject()))) {
/* 292 */         paramStringBuilder.append(str);
/* 293 */         append(paramStringBuilder, paramTypeParamType.classBound);
/* 294 */         str = " & ";
/*     */       }
/* 296 */       if (paramTypeParamType.interfaceBounds != null) {
/* 297 */         for (Type localType : paramTypeParamType.interfaceBounds) {
/* 298 */           paramStringBuilder.append(str);
/* 299 */           append(paramStringBuilder, localType);
/* 300 */           str = " & ";
/*     */         }
/*     */       }
/* 303 */       return paramStringBuilder;
/*     */     }
/*     */ 
/*     */     public StringBuilder visitWildcardType(Type.WildcardType paramWildcardType, StringBuilder paramStringBuilder) {
/* 307 */       switch (ClassWriter.1.$SwitchMap$com$sun$tools$classfile$Type$WildcardType$Kind[paramWildcardType.kind.ordinal()]) {
/*     */       case 1:
/* 309 */         paramStringBuilder.append("?");
/* 310 */         break;
/*     */       case 2:
/* 312 */         paramStringBuilder.append("? extends ");
/* 313 */         append(paramStringBuilder, paramWildcardType.boundType);
/* 314 */         break;
/*     */       case 3:
/* 316 */         paramStringBuilder.append("? super ");
/* 317 */         append(paramStringBuilder, paramWildcardType.boundType);
/* 318 */         break;
/*     */       default:
/* 320 */         throw new AssertionError();
/*     */       }
/* 322 */       return paramStringBuilder;
/*     */     }
/*     */ 
/*     */     private void append(StringBuilder paramStringBuilder, Type paramType) {
/* 326 */       paramType.accept(this, paramStringBuilder);
/*     */     }
/*     */ 
/*     */     private void append(StringBuilder paramStringBuilder, String paramString1, List<? extends Type> paramList, String paramString2) {
/* 330 */       paramStringBuilder.append(paramString1);
/* 331 */       String str = "";
/* 332 */       for (Type localType : paramList) {
/* 333 */         paramStringBuilder.append(str);
/* 334 */         append(paramStringBuilder, localType);
/* 335 */         str = ", ";
/*     */       }
/* 337 */       paramStringBuilder.append(paramString2);
/*     */     }
/*     */ 
/*     */     private void appendIfNotEmpty(StringBuilder paramStringBuilder, String paramString1, List<? extends Type> paramList, String paramString2) {
/* 341 */       if (!isEmpty(paramList))
/* 342 */         append(paramStringBuilder, paramString1, paramList, paramString2);
/*     */     }
/*     */ 
/*     */     private boolean isEmpty(List<? extends Type> paramList) {
/* 346 */       return (paramList == null) || (paramList.isEmpty());
/*     */     }
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.javap.ClassWriter
 * JD-Core Version:    0.6.2
 */