/*    */ package com.sun.tools.internal.xjc.reader.dtd.bindinfo;
/*    */ 
/*    */ import java.text.MessageFormat;
/*    */ import java.util.ResourceBundle;
/*    */ 
/*    */ class Messages
/*    */ {
/*    */   static final String ERR_UNDEFINED_FIELD = "BIConstructor.UndefinedField";
/*    */ 
/*    */   static String format(String property, Object[] args)
/*    */   {
/* 38 */     String text = ResourceBundle.getBundle(Messages.class.getPackage().getName() + ".MessageBundle").getString(property);
/* 39 */     return MessageFormat.format(text, args);
/*    */   }
/*    */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.internal.xjc.reader.dtd.bindinfo.Messages
 * JD-Core Version:    0.6.2
 */