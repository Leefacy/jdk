/*    */ package com.sun.tools.internal.xjc.api.util;
/*    */ 
/*    */ import java.text.MessageFormat;
/*    */ import java.util.ResourceBundle;
/*    */ 
/*    */  enum Messages
/*    */ {
/* 36 */   TOOLS_JAR_NOT_FOUND;
/*    */ 
/* 39 */   private static final ResourceBundle rb = ResourceBundle.getBundle(Messages.class.getName());
/*    */ 
/*    */   public String toString() {
/* 42 */     return format(new Object[0]);
/*    */   }
/*    */ 
/*    */   public String format(Object[] args) {
/* 46 */     return MessageFormat.format(rb.getString(name()), args);
/*    */   }
/*    */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.internal.xjc.api.util.Messages
 * JD-Core Version:    0.6.2
 */