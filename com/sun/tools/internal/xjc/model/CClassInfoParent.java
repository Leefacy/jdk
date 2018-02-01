/*    */ package com.sun.tools.internal.xjc.model;
/*    */ 
/*    */ import com.sun.codemodel.internal.JPackage;
/*    */ 
/*    */ public abstract interface CClassInfoParent
/*    */ {
/*    */   public abstract String fullName();
/*    */ 
/*    */   public abstract <T> T accept(Visitor<T> paramVisitor);
/*    */ 
/*    */   public abstract JPackage getOwnerPackage();
/*    */ 
/*    */   public static final class Package
/*    */     implements CClassInfoParent
/*    */   {
/*    */     public final JPackage pkg;
/*    */ 
/*    */     public Package(JPackage pkg)
/*    */     {
/* 68 */       this.pkg = pkg;
/*    */     }
/*    */ 
/*    */     public String fullName() {
/* 72 */       return this.pkg.name();
/*    */     }
/*    */ 
/*    */     public <T> T accept(CClassInfoParent.Visitor<T> visitor) {
/* 76 */       return visitor.onPackage(this.pkg);
/*    */     }
/*    */ 
/*    */     public JPackage getOwnerPackage() {
/* 80 */       return this.pkg;
/*    */     }
/*    */   }
/*    */ 
/*    */   public static abstract interface Visitor<T>
/*    */   {
/*    */     public abstract T onBean(CClassInfo paramCClassInfo);
/*    */ 
/*    */     public abstract T onPackage(JPackage paramJPackage);
/*    */ 
/*    */     public abstract T onElement(CElementInfo paramCElementInfo);
/*    */   }
/*    */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.internal.xjc.model.CClassInfoParent
 * JD-Core Version:    0.6.2
 */