/*    */ package com.sun.tools.internal.xjc.reader.xmlschema;
/*    */ 
/*    */ import com.sun.tools.internal.xjc.model.CElement;
/*    */ import com.sun.xml.internal.xsom.XSComplexType;
/*    */ import com.sun.xml.internal.xsom.XSElementDecl;
/*    */ 
/*    */ class Abstractifier extends ClassBinderFilter
/*    */ {
/*    */   public Abstractifier(ClassBinder core)
/*    */   {
/* 39 */     super(core);
/*    */   }
/*    */ 
/*    */   public CElement complexType(XSComplexType xs) {
/* 43 */     CElement ci = super.complexType(xs);
/* 44 */     if ((ci != null) && (xs.isAbstract()))
/* 45 */       ci.setAbstract();
/* 46 */     return ci;
/*    */   }
/*    */ 
/*    */   public CElement elementDecl(XSElementDecl xs) {
/* 50 */     CElement ci = super.elementDecl(xs);
/* 51 */     if ((ci != null) && (xs.isAbstract()))
/* 52 */       ci.setAbstract();
/* 53 */     return ci;
/*    */   }
/*    */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.internal.xjc.reader.xmlschema.Abstractifier
 * JD-Core Version:    0.6.2
 */