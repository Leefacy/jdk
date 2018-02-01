/*     */ package com.sun.tools.javap;
/*     */ 
/*     */ import com.sun.tools.classfile.ClassFile;
/*     */ import com.sun.tools.classfile.ConstantPool;
/*     */ import com.sun.tools.classfile.ConstantPool.CONSTANT_Class_info;
/*     */ import com.sun.tools.classfile.ConstantPool.CONSTANT_Double_info;
/*     */ import com.sun.tools.classfile.ConstantPool.CONSTANT_Fieldref_info;
/*     */ import com.sun.tools.classfile.ConstantPool.CONSTANT_Float_info;
/*     */ import com.sun.tools.classfile.ConstantPool.CONSTANT_Integer_info;
/*     */ import com.sun.tools.classfile.ConstantPool.CONSTANT_InterfaceMethodref_info;
/*     */ import com.sun.tools.classfile.ConstantPool.CONSTANT_InvokeDynamic_info;
/*     */ import com.sun.tools.classfile.ConstantPool.CONSTANT_Long_info;
/*     */ import com.sun.tools.classfile.ConstantPool.CONSTANT_MethodHandle_info;
/*     */ import com.sun.tools.classfile.ConstantPool.CONSTANT_MethodType_info;
/*     */ import com.sun.tools.classfile.ConstantPool.CONSTANT_Methodref_info;
/*     */ import com.sun.tools.classfile.ConstantPool.CONSTANT_NameAndType_info;
/*     */ import com.sun.tools.classfile.ConstantPool.CONSTANT_String_info;
/*     */ import com.sun.tools.classfile.ConstantPool.CONSTANT_Utf8_info;
/*     */ import com.sun.tools.classfile.ConstantPool.CPInfo;
/*     */ import com.sun.tools.classfile.ConstantPool.CPRefInfo;
/*     */ import com.sun.tools.classfile.ConstantPool.InvalidIndex;
/*     */ import com.sun.tools.classfile.ConstantPool.RefKind;
/*     */ import com.sun.tools.classfile.ConstantPool.Visitor;
/*     */ import com.sun.tools.classfile.ConstantPoolException;
/*     */ 
/*     */ public class ConstantWriter extends BasicWriter
/*     */ {
/* 255 */   StringValueVisitor stringValueVisitor = new StringValueVisitor(null);
/*     */   private ClassWriter classWriter;
/*     */   private Options options;
/*     */ 
/*     */   public static ConstantWriter instance(Context paramContext)
/*     */   {
/*  44 */     ConstantWriter localConstantWriter = (ConstantWriter)paramContext.get(ConstantWriter.class);
/*  45 */     if (localConstantWriter == null)
/*  46 */       localConstantWriter = new ConstantWriter(paramContext);
/*  47 */     return localConstantWriter;
/*     */   }
/*     */ 
/*     */   protected ConstantWriter(Context paramContext) {
/*  51 */     super(paramContext);
/*  52 */     paramContext.put(ConstantWriter.class, this);
/*  53 */     this.classWriter = ClassWriter.instance(paramContext);
/*  54 */     this.options = Options.instance(paramContext);
/*     */   }
/*     */ 
/*     */   protected void writeConstantPool() {
/*  58 */     ConstantPool localConstantPool = this.classWriter.getClassFile().constant_pool;
/*  59 */     writeConstantPool(localConstantPool);
/*     */   }
/*     */ 
/*     */   protected void writeConstantPool(ConstantPool paramConstantPool) {
/*  63 */     ConstantPool.Visitor local1 = new ConstantPool.Visitor() {
/*     */       public Integer visitClass(ConstantPool.CONSTANT_Class_info paramAnonymousCONSTANT_Class_info, Void paramAnonymousVoid) {
/*  65 */         ConstantWriter.this.print("#" + paramAnonymousCONSTANT_Class_info.name_index);
/*  66 */         ConstantWriter.this.tab();
/*  67 */         ConstantWriter.this.println("// " + ConstantWriter.this.stringValue(paramAnonymousCONSTANT_Class_info));
/*  68 */         return Integer.valueOf(1);
/*     */       }
/*     */ 
/*     */       public Integer visitDouble(ConstantPool.CONSTANT_Double_info paramAnonymousCONSTANT_Double_info, Void paramAnonymousVoid) {
/*  72 */         ConstantWriter.this.println(ConstantWriter.this.stringValue(paramAnonymousCONSTANT_Double_info));
/*  73 */         return Integer.valueOf(2);
/*     */       }
/*     */ 
/*     */       public Integer visitFieldref(ConstantPool.CONSTANT_Fieldref_info paramAnonymousCONSTANT_Fieldref_info, Void paramAnonymousVoid) {
/*  77 */         ConstantWriter.this.print("#" + paramAnonymousCONSTANT_Fieldref_info.class_index + ".#" + paramAnonymousCONSTANT_Fieldref_info.name_and_type_index);
/*  78 */         ConstantWriter.this.tab();
/*  79 */         ConstantWriter.this.println("// " + ConstantWriter.this.stringValue(paramAnonymousCONSTANT_Fieldref_info));
/*  80 */         return Integer.valueOf(1);
/*     */       }
/*     */ 
/*     */       public Integer visitFloat(ConstantPool.CONSTANT_Float_info paramAnonymousCONSTANT_Float_info, Void paramAnonymousVoid) {
/*  84 */         ConstantWriter.this.println(ConstantWriter.this.stringValue(paramAnonymousCONSTANT_Float_info));
/*  85 */         return Integer.valueOf(1);
/*     */       }
/*     */ 
/*     */       public Integer visitInteger(ConstantPool.CONSTANT_Integer_info paramAnonymousCONSTANT_Integer_info, Void paramAnonymousVoid) {
/*  89 */         ConstantWriter.this.println(ConstantWriter.this.stringValue(paramAnonymousCONSTANT_Integer_info));
/*  90 */         return Integer.valueOf(1);
/*     */       }
/*     */ 
/*     */       public Integer visitInterfaceMethodref(ConstantPool.CONSTANT_InterfaceMethodref_info paramAnonymousCONSTANT_InterfaceMethodref_info, Void paramAnonymousVoid) {
/*  94 */         ConstantWriter.this.print("#" + paramAnonymousCONSTANT_InterfaceMethodref_info.class_index + ".#" + paramAnonymousCONSTANT_InterfaceMethodref_info.name_and_type_index);
/*  95 */         ConstantWriter.this.tab();
/*  96 */         ConstantWriter.this.println("// " + ConstantWriter.this.stringValue(paramAnonymousCONSTANT_InterfaceMethodref_info));
/*  97 */         return Integer.valueOf(1);
/*     */       }
/*     */ 
/*     */       public Integer visitInvokeDynamic(ConstantPool.CONSTANT_InvokeDynamic_info paramAnonymousCONSTANT_InvokeDynamic_info, Void paramAnonymousVoid) {
/* 101 */         ConstantWriter.this.print("#" + paramAnonymousCONSTANT_InvokeDynamic_info.bootstrap_method_attr_index + ":#" + paramAnonymousCONSTANT_InvokeDynamic_info.name_and_type_index);
/* 102 */         ConstantWriter.this.tab();
/* 103 */         ConstantWriter.this.println("// " + ConstantWriter.this.stringValue(paramAnonymousCONSTANT_InvokeDynamic_info));
/* 104 */         return Integer.valueOf(1);
/*     */       }
/*     */ 
/*     */       public Integer visitLong(ConstantPool.CONSTANT_Long_info paramAnonymousCONSTANT_Long_info, Void paramAnonymousVoid) {
/* 108 */         ConstantWriter.this.println(ConstantWriter.this.stringValue(paramAnonymousCONSTANT_Long_info));
/* 109 */         return Integer.valueOf(2);
/*     */       }
/*     */ 
/*     */       public Integer visitNameAndType(ConstantPool.CONSTANT_NameAndType_info paramAnonymousCONSTANT_NameAndType_info, Void paramAnonymousVoid) {
/* 113 */         ConstantWriter.this.print("#" + paramAnonymousCONSTANT_NameAndType_info.name_index + ":#" + paramAnonymousCONSTANT_NameAndType_info.type_index);
/* 114 */         ConstantWriter.this.tab();
/* 115 */         ConstantWriter.this.println("// " + ConstantWriter.this.stringValue(paramAnonymousCONSTANT_NameAndType_info));
/* 116 */         return Integer.valueOf(1);
/*     */       }
/*     */ 
/*     */       public Integer visitMethodref(ConstantPool.CONSTANT_Methodref_info paramAnonymousCONSTANT_Methodref_info, Void paramAnonymousVoid) {
/* 120 */         ConstantWriter.this.print("#" + paramAnonymousCONSTANT_Methodref_info.class_index + ".#" + paramAnonymousCONSTANT_Methodref_info.name_and_type_index);
/* 121 */         ConstantWriter.this.tab();
/* 122 */         ConstantWriter.this.println("// " + ConstantWriter.this.stringValue(paramAnonymousCONSTANT_Methodref_info));
/* 123 */         return Integer.valueOf(1);
/*     */       }
/*     */ 
/*     */       public Integer visitMethodHandle(ConstantPool.CONSTANT_MethodHandle_info paramAnonymousCONSTANT_MethodHandle_info, Void paramAnonymousVoid) {
/* 127 */         ConstantWriter.this.print("#" + paramAnonymousCONSTANT_MethodHandle_info.reference_kind.tag + ":#" + paramAnonymousCONSTANT_MethodHandle_info.reference_index);
/* 128 */         ConstantWriter.this.tab();
/* 129 */         ConstantWriter.this.println("// " + ConstantWriter.this.stringValue(paramAnonymousCONSTANT_MethodHandle_info));
/* 130 */         return Integer.valueOf(1);
/*     */       }
/*     */ 
/*     */       public Integer visitMethodType(ConstantPool.CONSTANT_MethodType_info paramAnonymousCONSTANT_MethodType_info, Void paramAnonymousVoid) {
/* 134 */         ConstantWriter.this.print("#" + paramAnonymousCONSTANT_MethodType_info.descriptor_index);
/* 135 */         ConstantWriter.this.tab();
/* 136 */         ConstantWriter.this.println("//  " + ConstantWriter.this.stringValue(paramAnonymousCONSTANT_MethodType_info));
/* 137 */         return Integer.valueOf(1);
/*     */       }
/*     */ 
/*     */       public Integer visitString(ConstantPool.CONSTANT_String_info paramAnonymousCONSTANT_String_info, Void paramAnonymousVoid) {
/* 141 */         ConstantWriter.this.print("#" + paramAnonymousCONSTANT_String_info.string_index);
/* 142 */         ConstantWriter.this.tab();
/* 143 */         ConstantWriter.this.println("// " + ConstantWriter.this.stringValue(paramAnonymousCONSTANT_String_info));
/* 144 */         return Integer.valueOf(1);
/*     */       }
/*     */ 
/*     */       public Integer visitUtf8(ConstantPool.CONSTANT_Utf8_info paramAnonymousCONSTANT_Utf8_info, Void paramAnonymousVoid) {
/* 148 */         ConstantWriter.this.println(ConstantWriter.this.stringValue(paramAnonymousCONSTANT_Utf8_info));
/* 149 */         return Integer.valueOf(1);
/*     */       }
/*     */     };
/* 153 */     println("Constant pool:");
/* 154 */     indent(1);
/* 155 */     int i = String.valueOf(paramConstantPool.size()).length() + 1;
/* 156 */     int j = 1;
/* 157 */     while (j < paramConstantPool.size()) {
/* 158 */       print(String.format("%" + i + "s", new Object[] { "#" + j }));
/*     */       try {
/* 160 */         ConstantPool.CPInfo localCPInfo = paramConstantPool.get(j);
/* 161 */         print(String.format(" = %-18s ", new Object[] { cpTagName(localCPInfo) }));
/* 162 */         j += ((Integer)localCPInfo.accept(local1, null)).intValue();
/*     */       }
/*     */       catch (ConstantPool.InvalidIndex localInvalidIndex) {
/*     */       }
/*     */     }
/* 167 */     indent(-1);
/*     */   }
/*     */ 
/*     */   protected void write(int paramInt) {
/* 171 */     ClassFile localClassFile = this.classWriter.getClassFile();
/* 172 */     if (paramInt == 0) {
/* 173 */       print("#0");
/*     */       return;
/*     */     }
/*     */     ConstantPool.CPInfo localCPInfo;
/*     */     try {
/* 179 */       localCPInfo = localClassFile.constant_pool.get(paramInt);
/*     */     } catch (ConstantPoolException localConstantPoolException) {
/* 181 */       print("#" + paramInt);
/* 182 */       return;
/*     */     }
/*     */ 
/* 185 */     int i = localCPInfo.getTag();
/* 186 */     switch (i)
/*     */     {
/*     */     case 9:
/*     */     case 10:
/*     */     case 11:
/* 191 */       ConstantPool.CPRefInfo localCPRefInfo = (ConstantPool.CPRefInfo)localCPInfo;
/*     */       try {
/* 193 */         if (localCPRefInfo.class_index == localClassFile.this_class)
/* 194 */           localCPInfo = localClassFile.constant_pool.get(localCPRefInfo.name_and_type_index);
/*     */       }
/*     */       catch (ConstantPool.InvalidIndex localInvalidIndex) {
/*     */       }
/*     */     }
/* 199 */     print(tagName(i) + " " + stringValue(localCPInfo));
/*     */   }
/*     */ 
/*     */   String cpTagName(ConstantPool.CPInfo paramCPInfo) {
/* 203 */     String str = paramCPInfo.getClass().getSimpleName();
/* 204 */     return str.replace("CONSTANT_", "").replace("_info", "");
/*     */   }
/*     */ 
/*     */   String tagName(int paramInt) {
/* 208 */     switch (paramInt) {
/*     */     case 1:
/* 210 */       return "Utf8";
/*     */     case 3:
/* 212 */       return "int";
/*     */     case 4:
/* 214 */       return "float";
/*     */     case 5:
/* 216 */       return "long";
/*     */     case 6:
/* 218 */       return "double";
/*     */     case 7:
/* 220 */       return "class";
/*     */     case 8:
/* 222 */       return "String";
/*     */     case 9:
/* 224 */       return "Field";
/*     */     case 15:
/* 226 */       return "MethodHandle";
/*     */     case 16:
/* 228 */       return "MethodType";
/*     */     case 10:
/* 230 */       return "Method";
/*     */     case 11:
/* 232 */       return "InterfaceMethod";
/*     */     case 18:
/* 234 */       return "InvokeDynamic";
/*     */     case 12:
/* 236 */       return "NameAndType";
/*     */     case 2:
/*     */     case 13:
/*     */     case 14:
/* 238 */     case 17: } return "(unknown tag " + paramInt + ")";
/*     */   }
/*     */ 
/*     */   String stringValue(int paramInt)
/*     */   {
/* 243 */     ClassFile localClassFile = this.classWriter.getClassFile();
/*     */     try {
/* 245 */       return stringValue(localClassFile.constant_pool.get(paramInt));
/*     */     } catch (ConstantPool.InvalidIndex localInvalidIndex) {
/* 247 */       return report(localInvalidIndex);
/*     */     }
/*     */   }
/*     */ 
/*     */   String stringValue(ConstantPool.CPInfo paramCPInfo) {
/* 252 */     return this.stringValueVisitor.visit(paramCPInfo);
/*     */   }
/*     */ 
/*     */   private static String checkName(String paramString)
/*     */   {
/* 404 */     if (paramString == null) {
/* 405 */       return "null";
/*     */     }
/* 407 */     int i = paramString.length();
/* 408 */     if (i == 0) {
/* 409 */       return "\"\"";
/*     */     }
/* 411 */     int j = 47;
/*     */     int k;
/* 413 */     for (int m = 0; m < i; m += Character.charCount(k)) {
/* 414 */       k = paramString.codePointAt(m);
/* 415 */       if (((j == 47) && (!Character.isJavaIdentifierStart(k))) || ((k != 47) && 
/* 416 */         (!Character.isJavaIdentifierPart(k))))
/*     */       {
/* 417 */         return "\"" + addEscapes(paramString) + "\"";
/*     */       }
/* 419 */       j = k;
/*     */     }
/*     */ 
/* 422 */     return paramString;
/*     */   }
/*     */ 
/*     */   private static String addEscapes(String paramString)
/*     */   {
/* 427 */     String str1 = "\\\"\n\t";
/* 428 */     String str2 = "\\\"nt";
/* 429 */     StringBuilder localStringBuilder = null;
/* 430 */     int i = 0;
/* 431 */     int j = paramString.length();
/* 432 */     for (int k = 0; k < j; k++) {
/* 433 */       int m = paramString.charAt(k);
/* 434 */       int n = str1.indexOf(m);
/* 435 */       if (n >= 0) {
/* 436 */         if (localStringBuilder == null)
/* 437 */           localStringBuilder = new StringBuilder(j * 2);
/* 438 */         if (i < k)
/* 439 */           localStringBuilder.append(paramString, i, k);
/* 440 */         localStringBuilder.append('\\');
/* 441 */         localStringBuilder.append(str2.charAt(n));
/* 442 */         i = k + 1;
/*     */       }
/*     */     }
/* 445 */     if (localStringBuilder == null)
/* 446 */       return paramString;
/* 447 */     if (i < j)
/* 448 */       localStringBuilder.append(paramString, i, j);
/* 449 */     return localStringBuilder.toString();
/*     */   }
/*     */ 
/*     */   private class StringValueVisitor
/*     */     implements ConstantPool.Visitor<String, Void>
/*     */   {
/*     */     private StringValueVisitor()
/*     */     {
/*     */     }
/*     */ 
/*     */     public String visit(ConstantPool.CPInfo paramCPInfo)
/*     */     {
/* 259 */       return (String)paramCPInfo.accept(this, null);
/*     */     }
/*     */ 
/*     */     public String visitClass(ConstantPool.CONSTANT_Class_info paramCONSTANT_Class_info, Void paramVoid) {
/* 263 */       return getCheckedName(paramCONSTANT_Class_info);
/*     */     }
/*     */ 
/*     */     String getCheckedName(ConstantPool.CONSTANT_Class_info paramCONSTANT_Class_info) {
/*     */       try {
/* 268 */         return ConstantWriter.checkName(paramCONSTANT_Class_info.getName());
/*     */       } catch (ConstantPoolException localConstantPoolException) {
/* 270 */         return ConstantWriter.this.report(localConstantPoolException);
/*     */       }
/*     */     }
/*     */ 
/*     */     public String visitDouble(ConstantPool.CONSTANT_Double_info paramCONSTANT_Double_info, Void paramVoid) {
/* 275 */       return paramCONSTANT_Double_info.value + "d";
/*     */     }
/*     */ 
/*     */     public String visitFieldref(ConstantPool.CONSTANT_Fieldref_info paramCONSTANT_Fieldref_info, Void paramVoid) {
/* 279 */       return visitRef(paramCONSTANT_Fieldref_info, paramVoid);
/*     */     }
/*     */ 
/*     */     public String visitFloat(ConstantPool.CONSTANT_Float_info paramCONSTANT_Float_info, Void paramVoid) {
/* 283 */       return paramCONSTANT_Float_info.value + "f";
/*     */     }
/*     */ 
/*     */     public String visitInteger(ConstantPool.CONSTANT_Integer_info paramCONSTANT_Integer_info, Void paramVoid) {
/* 287 */       return String.valueOf(paramCONSTANT_Integer_info.value);
/*     */     }
/*     */ 
/*     */     public String visitInterfaceMethodref(ConstantPool.CONSTANT_InterfaceMethodref_info paramCONSTANT_InterfaceMethodref_info, Void paramVoid) {
/* 291 */       return visitRef(paramCONSTANT_InterfaceMethodref_info, paramVoid);
/*     */     }
/*     */ 
/*     */     public String visitInvokeDynamic(ConstantPool.CONSTANT_InvokeDynamic_info paramCONSTANT_InvokeDynamic_info, Void paramVoid) {
/*     */       try {
/* 296 */         String str = ConstantWriter.this.stringValue(paramCONSTANT_InvokeDynamic_info.getNameAndTypeInfo());
/* 297 */         return "#" + paramCONSTANT_InvokeDynamic_info.bootstrap_method_attr_index + ":" + str;
/*     */       } catch (ConstantPoolException localConstantPoolException) {
/* 299 */         return ConstantWriter.this.report(localConstantPoolException);
/*     */       }
/*     */     }
/*     */ 
/*     */     public String visitLong(ConstantPool.CONSTANT_Long_info paramCONSTANT_Long_info, Void paramVoid) {
/* 304 */       return paramCONSTANT_Long_info.value + "l";
/*     */     }
/*     */ 
/*     */     public String visitNameAndType(ConstantPool.CONSTANT_NameAndType_info paramCONSTANT_NameAndType_info, Void paramVoid) {
/* 308 */       return getCheckedName(paramCONSTANT_NameAndType_info) + ":" + getType(paramCONSTANT_NameAndType_info);
/*     */     }
/*     */ 
/*     */     String getCheckedName(ConstantPool.CONSTANT_NameAndType_info paramCONSTANT_NameAndType_info) {
/*     */       try {
/* 313 */         return ConstantWriter.checkName(paramCONSTANT_NameAndType_info.getName());
/*     */       } catch (ConstantPoolException localConstantPoolException) {
/* 315 */         return ConstantWriter.this.report(localConstantPoolException);
/*     */       }
/*     */     }
/*     */ 
/*     */     String getType(ConstantPool.CONSTANT_NameAndType_info paramCONSTANT_NameAndType_info) {
/*     */       try {
/* 321 */         return paramCONSTANT_NameAndType_info.getType();
/*     */       } catch (ConstantPoolException localConstantPoolException) {
/* 323 */         return ConstantWriter.this.report(localConstantPoolException);
/*     */       }
/*     */     }
/*     */ 
/*     */     public String visitMethodHandle(ConstantPool.CONSTANT_MethodHandle_info paramCONSTANT_MethodHandle_info, Void paramVoid) {
/*     */       try {
/* 329 */         return paramCONSTANT_MethodHandle_info.reference_kind.name + " " + ConstantWriter.this.stringValue(paramCONSTANT_MethodHandle_info.getCPRefInfo());
/*     */       } catch (ConstantPoolException localConstantPoolException) {
/* 331 */         return ConstantWriter.this.report(localConstantPoolException);
/*     */       }
/*     */     }
/*     */ 
/*     */     public String visitMethodType(ConstantPool.CONSTANT_MethodType_info paramCONSTANT_MethodType_info, Void paramVoid) {
/*     */       try {
/* 337 */         return paramCONSTANT_MethodType_info.getType();
/*     */       } catch (ConstantPoolException localConstantPoolException) {
/* 339 */         return ConstantWriter.this.report(localConstantPoolException);
/*     */       }
/*     */     }
/*     */ 
/*     */     public String visitMethodref(ConstantPool.CONSTANT_Methodref_info paramCONSTANT_Methodref_info, Void paramVoid) {
/* 344 */       return visitRef(paramCONSTANT_Methodref_info, paramVoid);
/*     */     }
/*     */ 
/*     */     public String visitString(ConstantPool.CONSTANT_String_info paramCONSTANT_String_info, Void paramVoid) {
/*     */       try {
/* 349 */         ClassFile localClassFile = ConstantWriter.this.classWriter.getClassFile();
/* 350 */         int i = paramCONSTANT_String_info.string_index;
/* 351 */         return ConstantWriter.this.stringValue(localClassFile.constant_pool.getUTF8Info(i));
/*     */       } catch (ConstantPoolException localConstantPoolException) {
/* 353 */         return ConstantWriter.this.report(localConstantPoolException);
/*     */       }
/*     */     }
/*     */ 
/*     */     public String visitUtf8(ConstantPool.CONSTANT_Utf8_info paramCONSTANT_Utf8_info, Void paramVoid) {
/* 358 */       String str = paramCONSTANT_Utf8_info.value;
/* 359 */       StringBuilder localStringBuilder = new StringBuilder();
/* 360 */       for (int i = 0; i < str.length(); i++) {
/* 361 */         char c = str.charAt(i);
/* 362 */         switch (c) {
/*     */         case '\t':
/* 364 */           localStringBuilder.append('\\').append('t');
/* 365 */           break;
/*     */         case '\n':
/* 367 */           localStringBuilder.append('\\').append('n');
/* 368 */           break;
/*     */         case '\r':
/* 370 */           localStringBuilder.append('\\').append('r');
/* 371 */           break;
/*     */         case '"':
/* 373 */           localStringBuilder.append('\\').append('"');
/* 374 */           break;
/*     */         default:
/* 376 */           localStringBuilder.append(c);
/*     */         }
/*     */       }
/* 379 */       return localStringBuilder.toString();
/*     */     }
/*     */ 
/* 383 */     String visitRef(ConstantPool.CPRefInfo paramCPRefInfo, Void paramVoid) { String str1 = getCheckedClassName(paramCPRefInfo);
/*     */       String str2;
/*     */       try {
/* 386 */         str2 = ConstantWriter.this.stringValue(paramCPRefInfo.getNameAndTypeInfo());
/*     */       } catch (ConstantPoolException localConstantPoolException) {
/* 388 */         str2 = ConstantWriter.this.report(localConstantPoolException);
/*     */       }
/* 390 */       return str1 + "." + str2; }
/*     */ 
/*     */     String getCheckedClassName(ConstantPool.CPRefInfo paramCPRefInfo)
/*     */     {
/*     */       try {
/* 395 */         return ConstantWriter.checkName(paramCPRefInfo.getClassName());
/*     */       } catch (ConstantPoolException localConstantPoolException) {
/* 397 */         return ConstantWriter.this.report(localConstantPoolException);
/*     */       }
/*     */     }
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.javap.ConstantWriter
 * JD-Core Version:    0.6.2
 */