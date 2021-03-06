/*     */ package com.sun.codemodel.internal;
/*     */ 
/*     */ public final class JPrimitiveType extends JType
/*     */ {
/*     */   private final String typeName;
/*     */   private final JCodeModel owner;
/*     */   private final JClass wrapperClass;
/*     */   private JClass arrayClass;
/*     */ 
/*     */   JPrimitiveType(JCodeModel owner, String typeName, Class<?> wrapper)
/*     */   {
/*  46 */     this.owner = owner;
/*  47 */     this.typeName = typeName;
/*  48 */     this.wrapperClass = owner.ref(wrapper);
/*     */   }
/*     */   public JCodeModel owner() {
/*  51 */     return this.owner;
/*     */   }
/*     */   public String fullName() {
/*  54 */     return this.typeName;
/*     */   }
/*     */ 
/*     */   public String name() {
/*  58 */     return fullName();
/*     */   }
/*     */ 
/*     */   public boolean isPrimitive() {
/*  62 */     return true;
/*     */   }
/*     */ 
/*     */   public JClass array()
/*     */   {
/*  67 */     if (this.arrayClass == null)
/*  68 */       this.arrayClass = new JArrayClass(this.owner, this);
/*  69 */     return this.arrayClass;
/*     */   }
/*     */ 
/*     */   public JClass boxify()
/*     */   {
/*  78 */     return this.wrapperClass;
/*     */   }
/*     */ 
/*     */   /** @deprecated */
/*     */   public JType unboxify()
/*     */   {
/*  87 */     return this;
/*     */   }
/*     */ 
/*     */   /** @deprecated */
/*     */   public JClass getWrapperClass()
/*     */   {
/*  95 */     return boxify();
/*     */   }
/*     */ 
/*     */   public JExpression wrap(JExpression exp)
/*     */   {
/* 106 */     return JExpr._new(boxify()).arg(exp);
/*     */   }
/*     */ 
/*     */   public JExpression unwrap(JExpression exp)
/*     */   {
/* 117 */     return exp.invoke(this.typeName + "Value");
/*     */   }
/*     */ 
/*     */   public void generate(JFormatter f) {
/* 121 */     f.p(this.typeName);
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.codemodel.internal.JPrimitiveType
 * JD-Core Version:    0.6.2
 */