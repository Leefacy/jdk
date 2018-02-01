/*    */ package com.sun.tools.internal.xjc.reader;
/*    */ 
/*    */ import java.text.MessageFormat;
/*    */ import java.util.ResourceBundle;
/*    */ 
/*    */ public enum Messages
/*    */ {
/* 35 */   DUPLICATE_PROPERTY, 
/* 36 */   DUPLICATE_ELEMENT, 
/*    */ 
/* 38 */   ERR_UNDECLARED_PREFIX, 
/* 39 */   ERR_UNEXPECTED_EXTENSION_BINDING_PREFIXES, 
/* 40 */   ERR_UNSUPPORTED_EXTENSION, 
/* 41 */   ERR_SUPPORTED_EXTENSION_IGNORED, 
/* 42 */   ERR_RELEVANT_LOCATION, 
/* 43 */   ERR_CLASS_NOT_FOUND, 
/* 44 */   PROPERTY_CLASS_IS_RESERVED, 
/* 45 */   ERR_VENDOR_EXTENSION_DISALLOWED_IN_STRICT_MODE, 
/* 46 */   ERR_ILLEGAL_CUSTOMIZATION_TAGNAME, 
/* 47 */   ERR_PLUGIN_NOT_ENABLED;
/*    */ 
/* 50 */   private static final ResourceBundle rb = ResourceBundle.getBundle(Messages.class.getPackage().getName() + ".MessageBundle");
/*    */ 
/*    */   public String toString() {
/* 53 */     return format(new Object[0]);
/*    */   }
/*    */ 
/*    */   public String format(Object[] args) {
/* 57 */     return MessageFormat.format(rb.getString(name()), args);
/*    */   }
/*    */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.internal.xjc.reader.Messages
 * JD-Core Version:    0.6.2
 */