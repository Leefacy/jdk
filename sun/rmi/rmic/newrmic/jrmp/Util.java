/*     */ package sun.rmi.rmic.newrmic.jrmp;
/*     */ 
/*     */ import com.sun.javadoc.ClassDoc;
/*     */ import com.sun.javadoc.MethodDoc;
/*     */ import com.sun.javadoc.PackageDoc;
/*     */ import com.sun.javadoc.Parameter;
/*     */ import com.sun.javadoc.Type;
/*     */ 
/*     */ final class Util
/*     */ {
/*     */   private Util()
/*     */   {
/*  44 */     throw new AssertionError();
/*     */   }
/*     */ 
/*     */   static String binaryNameOf(ClassDoc paramClassDoc)
/*     */   {
/*  51 */     String str1 = paramClassDoc.name().replace('.', '$');
/*  52 */     String str2 = paramClassDoc.containingPackage().name();
/*  53 */     return str2 + "." + str1;
/*     */   }
/*     */ 
/*     */   static String methodDescriptorOf(MethodDoc paramMethodDoc)
/*     */   {
/*  63 */     String str = "(";
/*  64 */     Parameter[] arrayOfParameter = paramMethodDoc.parameters();
/*  65 */     for (int i = 0; i < arrayOfParameter.length; i++) {
/*  66 */       str = str + typeDescriptorOf(arrayOfParameter[i].type());
/*     */     }
/*  68 */     str = str + ")" + typeDescriptorOf(paramMethodDoc.returnType());
/*  69 */     return str;
/*     */   }
/*     */ 
/*     */   private static String typeDescriptorOf(Type paramType)
/*     */   {
/*  78 */     ClassDoc localClassDoc = paramType.asClassDoc();
/*     */     String str1;
/*  79 */     if (localClassDoc == null)
/*     */     {
/*  83 */       String str2 = paramType.typeName();
/*  84 */       if (str2.equals("boolean"))
/*  85 */         str1 = "Z";
/*  86 */       else if (str2.equals("byte"))
/*  87 */         str1 = "B";
/*  88 */       else if (str2.equals("char"))
/*  89 */         str1 = "C";
/*  90 */       else if (str2.equals("short"))
/*  91 */         str1 = "S";
/*  92 */       else if (str2.equals("int"))
/*  93 */         str1 = "I";
/*  94 */       else if (str2.equals("long"))
/*  95 */         str1 = "J";
/*  96 */       else if (str2.equals("float"))
/*  97 */         str1 = "F";
/*  98 */       else if (str2.equals("double"))
/*  99 */         str1 = "D";
/* 100 */       else if (str2.equals("void"))
/* 101 */         str1 = "V";
/*     */       else {
/* 103 */         throw new AssertionError("unrecognized primitive type: " + str2);
/*     */       }
/*     */ 
/*     */     }
/*     */     else
/*     */     {
/* 110 */       str1 = "L" + binaryNameOf(localClassDoc).replace('.', '/') + ";";
/*     */     }
/*     */ 
/* 116 */     int i = paramType.dimension().length() / 2;
/* 117 */     for (int j = 0; j < i; j++) {
/* 118 */       str1 = "[" + str1;
/*     */     }
/*     */ 
/* 121 */     return str1;
/*     */   }
/*     */ 
/*     */   static String getFriendlyUnqualifiedSignature(MethodDoc paramMethodDoc)
/*     */   {
/* 130 */     String str = paramMethodDoc.name() + "(";
/* 131 */     Parameter[] arrayOfParameter = paramMethodDoc.parameters();
/* 132 */     for (int i = 0; i < arrayOfParameter.length; i++) {
/* 133 */       if (i > 0) {
/* 134 */         str = str + ", ";
/*     */       }
/* 136 */       Type localType = arrayOfParameter[i].type();
/* 137 */       str = str + localType.typeName() + localType.dimension();
/*     */     }
/* 139 */     str = str + ")";
/* 140 */     return str;
/*     */   }
/*     */ 
/*     */   static boolean isVoid(Type paramType)
/*     */   {
/* 147 */     return (paramType.asClassDoc() == null) && (paramType.typeName().equals("void"));
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     sun.rmi.rmic.newrmic.jrmp.Util
 * JD-Core Version:    0.6.2
 */