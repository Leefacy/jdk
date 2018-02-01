/*    */ package com.sun.tools.internal.xjc.generator.bean;
/*    */ 
/*    */ import java.text.MessageFormat;
/*    */ import java.util.ResourceBundle;
/*    */ 
/*    */  enum Messages
/*    */ {
/* 36 */   METHOD_COLLISION, 
/* 37 */   ERR_UNUSABLE_NAME, 
/* 38 */   ERR_KEYNAME_COLLISION, 
/* 39 */   ERR_NAME_COLLISION, 
/* 40 */   ILLEGAL_CONSTRUCTOR_PARAM, 
/* 41 */   OBJECT_FACTORY_CONFLICT, 
/* 42 */   OBJECT_FACTORY_CONFLICT_RELATED;
/*    */ 
/* 45 */   private static final ResourceBundle rb = ResourceBundle.getBundle(Messages.class.getPackage().getName() + ".MessageBundle");
/*    */ 
/*    */   public String toString() {
/* 48 */     return format(new Object[0]);
/*    */   }
/*    */ 
/*    */   public String format(Object[] args) {
/* 52 */     return MessageFormat.format(rb.getString(name()), args);
/*    */   }
/*    */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.internal.xjc.generator.bean.Messages
 * JD-Core Version:    0.6.2
 */