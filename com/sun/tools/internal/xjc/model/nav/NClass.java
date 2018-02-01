/*    */ package com.sun.tools.internal.xjc.model.nav;
/*    */ 
/*    */ import com.sun.codemodel.internal.JClass;
/*    */ import com.sun.tools.internal.xjc.outline.Aspect;
/*    */ import com.sun.tools.internal.xjc.outline.Outline;
/*    */ 
/*    */ public abstract interface NClass extends NType
/*    */ {
/*    */   public abstract JClass toType(Outline paramOutline, Aspect paramAspect);
/*    */ 
/*    */   public abstract boolean isAbstract();
/*    */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.internal.xjc.model.nav.NClass
 * JD-Core Version:    0.6.2
 */