/*    */ package com.sun.tools.hat.internal.model;
/*    */ 
/*    */ public abstract class AbstractJavaHeapObjectVisitor
/*    */   implements JavaHeapObjectVisitor
/*    */ {
/*    */   public abstract void visit(JavaHeapObject paramJavaHeapObject);
/*    */ 
/*    */   public boolean exclude(JavaClass paramJavaClass, JavaField paramJavaField)
/*    */   {
/* 50 */     return false;
/*    */   }
/*    */ 
/*    */   public boolean mightExclude()
/*    */   {
/* 57 */     return false;
/*    */   }
/*    */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.hat.internal.model.AbstractJavaHeapObjectVisitor
 * JD-Core Version:    0.6.2
 */