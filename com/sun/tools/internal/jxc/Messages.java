/*    */ package com.sun.tools.internal.jxc;
/*    */ 
/*    */ import java.text.MessageFormat;
/*    */ import java.util.ResourceBundle;
/*    */ 
/*    */  enum Messages
/*    */ {
/* 38 */   UNEXPECTED_NGCC_TOKEN, 
/* 39 */   BASEDIR_DOESNT_EXIST, 
/* 40 */   USAGE, 
/* 41 */   FULLVERSION, 
/* 42 */   VERSION;
/*    */ 
/* 45 */   private static final ResourceBundle rb = ResourceBundle.getBundle(Messages.class.getPackage().getName() + ".MessageBundle");
/*    */ 
/*    */   public String toString()
/*    */   {
/* 49 */     return format(new Object[0]);
/*    */   }
/*    */ 
/*    */   public String format(Object[] args) {
/* 53 */     return MessageFormat.format(rb.getString(name()), args);
/*    */   }
/*    */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.internal.jxc.Messages
 * JD-Core Version:    0.6.2
 */