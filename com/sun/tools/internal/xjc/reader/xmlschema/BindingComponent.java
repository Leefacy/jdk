/*    */ package com.sun.tools.internal.xjc.reader.xmlschema;
/*    */ 
/*    */ import com.sun.tools.internal.xjc.reader.Ring;
/*    */ 
/*    */ public abstract class BindingComponent
/*    */ {
/*    */   protected BindingComponent()
/*    */   {
/* 37 */     Ring.add(this);
/*    */   }
/*    */ 
/*    */   protected final ErrorReporter getErrorReporter()
/*    */   {
/* 47 */     return (ErrorReporter)Ring.get(ErrorReporter.class);
/*    */   }
/*    */   protected final ClassSelector getClassSelector() {
/* 50 */     return (ClassSelector)Ring.get(ClassSelector.class);
/*    */   }
/*    */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.internal.xjc.reader.xmlschema.BindingComponent
 * JD-Core Version:    0.6.2
 */