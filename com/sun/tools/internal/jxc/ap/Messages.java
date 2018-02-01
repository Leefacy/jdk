/*    */ package com.sun.tools.internal.jxc.ap;
/*    */ 
/*    */ import java.text.MessageFormat;
/*    */ import java.util.ResourceBundle;
/*    */ 
/*    */  enum Messages
/*    */ {
/* 38 */   NON_EXISTENT_FILE, 
/* 39 */   UNRECOGNIZED_PARAMETER, 
/* 40 */   OPERAND_MISSING;
/*    */ 
/* 43 */   private static final ResourceBundle rb = ResourceBundle.getBundle(Messages.class.getPackage().getName() + ".MessageBundle");
/*    */ 
/*    */   public String toString() {
/* 46 */     return format(new Object[0]);
/*    */   }
/*    */ 
/*    */   public String format(Object[] args) {
/* 50 */     return MessageFormat.format(rb.getString(name()), args);
/*    */   }
/*    */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.internal.jxc.ap.Messages
 * JD-Core Version:    0.6.2
 */