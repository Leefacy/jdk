/*    */ package com.sun.tools.internal.xjc.reader.xmlschema.bindinfo;
/*    */ 
/*    */ import java.text.MessageFormat;
/*    */ import java.util.ResourceBundle;
/*    */ 
/*    */  enum Messages
/*    */ {
/* 36 */   ERR_CANNOT_BE_BOUND_TO_SIMPLETYPE, 
/* 37 */   ERR_UNDEFINED_SIMPLE_TYPE, 
/* 38 */   ERR_ILLEGAL_FIXEDATTR;
/*    */ 
/*    */   String format(Object[] args)
/*    */   {
/* 43 */     String text = ResourceBundle.getBundle(Messages.class.getPackage().getName() + ".MessageBundle").getString(name());
/* 44 */     return MessageFormat.format(text, args);
/*    */   }
/*    */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.internal.xjc.reader.xmlschema.bindinfo.Messages
 * JD-Core Version:    0.6.2
 */