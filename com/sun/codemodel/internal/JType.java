/*     */ package com.sun.codemodel.internal;
/*     */ 
/*     */ public abstract class JType
/*     */   implements JGenerable, Comparable<JType>
/*     */ {
/*     */   public static JPrimitiveType parse(JCodeModel codeModel, String typeName)
/*     */   {
/*  41 */     if (typeName.equals("void"))
/*  42 */       return codeModel.VOID;
/*  43 */     if (typeName.equals("boolean"))
/*  44 */       return codeModel.BOOLEAN;
/*  45 */     if (typeName.equals("byte"))
/*  46 */       return codeModel.BYTE;
/*  47 */     if (typeName.equals("short"))
/*  48 */       return codeModel.SHORT;
/*  49 */     if (typeName.equals("char"))
/*  50 */       return codeModel.CHAR;
/*  51 */     if (typeName.equals("int"))
/*  52 */       return codeModel.INT;
/*  53 */     if (typeName.equals("float"))
/*  54 */       return codeModel.FLOAT;
/*  55 */     if (typeName.equals("long"))
/*  56 */       return codeModel.LONG;
/*  57 */     if (typeName.equals("double")) {
/*  58 */       return codeModel.DOUBLE;
/*     */     }
/*  60 */     throw new IllegalArgumentException("Not a primitive type: " + typeName);
/*     */   }
/*     */ 
/*     */   public abstract JCodeModel owner();
/*     */ 
/*     */   public abstract String fullName();
/*     */ 
/*     */   public String binaryName()
/*     */   {
/*  86 */     return fullName();
/*     */   }
/*     */ 
/*     */   public abstract String name();
/*     */ 
/*     */   public abstract JClass array();
/*     */ 
/*     */   public boolean isArray()
/*     */   {
/* 110 */     return false;
/*     */   }
/*     */ 
/*     */   public boolean isPrimitive()
/*     */   {
/* 115 */     return false;
/*     */   }
/*     */ 
/*     */   public abstract JClass boxify();
/*     */ 
/*     */   public abstract JType unboxify();
/*     */ 
/*     */   public JType erasure()
/*     */   {
/* 139 */     return this;
/*     */   }
/*     */ 
/*     */   public final boolean isReference()
/*     */   {
/* 146 */     return !isPrimitive();
/*     */   }
/*     */ 
/*     */   public JType elementType()
/*     */   {
/* 154 */     throw new IllegalArgumentException("Not an array type");
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 159 */     return getClass().getName() + '(' + 
/* 159 */       fullName() + ')';
/*     */   }
/*     */ 
/*     */   public int compareTo(JType o)
/*     */   {
/* 170 */     String rhs = o.fullName();
/* 171 */     boolean p = fullName().startsWith("java");
/* 172 */     boolean q = rhs.startsWith("java");
/*     */ 
/* 174 */     if ((p) && (!q))
/* 175 */       return -1;
/* 176 */     if ((!p) && (q)) {
/* 177 */       return 1;
/*     */     }
/* 179 */     return fullName().compareTo(rhs);
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.codemodel.internal.JType
 * JD-Core Version:    0.6.2
 */