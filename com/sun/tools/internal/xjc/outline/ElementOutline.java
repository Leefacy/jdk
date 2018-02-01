/*    */ package com.sun.tools.internal.xjc.outline;
/*    */ 
/*    */ import com.sun.codemodel.internal.JDefinedClass;
/*    */ import com.sun.tools.internal.xjc.model.CElementInfo;
/*    */ 
/*    */ public abstract class ElementOutline
/*    */ {
/*    */   public final CElementInfo target;
/*    */   public final JDefinedClass implClass;
/*    */ 
/*    */   public abstract Outline parent();
/*    */ 
/*    */   public PackageOutline _package()
/*    */   {
/* 53 */     return parent().getPackageContext(this.implClass._package());
/*    */   }
/*    */ 
/*    */   protected ElementOutline(CElementInfo target, JDefinedClass implClass)
/*    */   {
/* 69 */     this.target = target;
/* 70 */     this.implClass = implClass;
/*    */   }
/*    */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.internal.xjc.outline.ElementOutline
 * JD-Core Version:    0.6.2
 */