/*    */ package com.sun.tools.internal.xjc.reader.xmlschema.ct;
/*    */ 
/*    */ import java.text.MessageFormat;
/*    */ import java.util.ResourceBundle;
/*    */ 
/*    */  enum Messages
/*    */ {
/* 35 */   ERR_NO_FURTHER_EXTENSION;
/*    */ 
/* 37 */   private static final ResourceBundle rb = ResourceBundle.getBundle(Messages.class.getPackage().getName() + ".MessageBundle");
/*    */ 
/*    */   public String toString() {
/* 40 */     return format(new Object[0]);
/*    */   }
/*    */ 
/*    */   public String format(Object[] args) {
/* 44 */     return MessageFormat.format(rb.getString(name()), args);
/*    */   }
/*    */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.internal.xjc.reader.xmlschema.ct.Messages
 * JD-Core Version:    0.6.2
 */