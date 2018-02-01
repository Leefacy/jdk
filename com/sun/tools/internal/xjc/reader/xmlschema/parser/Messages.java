/*    */ package com.sun.tools.internal.xjc.reader.xmlschema.parser;
/*    */ 
/*    */ import java.text.MessageFormat;
/*    */ import java.util.ResourceBundle;
/*    */ 
/*    */ class Messages
/*    */ {
/*    */   static final String ERR_UNACKNOWLEDGED_CUSTOMIZATION = "CustomizationContextChecker.UnacknolwedgedCustomization";
/*    */   static final String WARN_INCORRECT_URI = "IncorrectNamespaceURIChecker.WarnIncorrectURI";
/*    */   static final String WARN_UNABLE_TO_CHECK_CORRECTNESS = "SchemaConstraintChecker.UnableToCheckCorrectness";
/*    */ 
/*    */   static String format(String property, Object[] args)
/*    */   {
/* 38 */     String text = ResourceBundle.getBundle(Messages.class.getPackage().getName() + ".MessageBundle").getString(property);
/* 39 */     return MessageFormat.format(text, args);
/*    */   }
/*    */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.internal.xjc.reader.xmlschema.parser.Messages
 * JD-Core Version:    0.6.2
 */