/*    */ package com.sun.tools.internal.xjc.generator.bean.field;
/*    */ 
/*    */ import java.text.MessageFormat;
/*    */ import java.util.ResourceBundle;
/*    */ 
/*    */ public enum Messages
/*    */ {
/* 36 */   DEFAULT_GETTER_JAVADOC, 
/* 37 */   DEFAULT_SETTER_JAVADOC;
/*    */ 
/* 40 */   private static final ResourceBundle rb = ResourceBundle.getBundle(Messages.class.getName().substring(0, Messages.class.getName().lastIndexOf('.')) + ".MessageBundle");
/*    */ 
/*    */   public String toString() {
/* 43 */     return format(new Object[0]);
/*    */   }
/*    */ 
/*    */   public String format(Object[] args) {
/* 47 */     return MessageFormat.format(rb.getString(name()), args);
/*    */   }
/*    */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.internal.xjc.generator.bean.field.Messages
 * JD-Core Version:    0.6.2
 */