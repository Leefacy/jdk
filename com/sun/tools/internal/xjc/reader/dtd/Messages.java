/*    */ package com.sun.tools.internal.xjc.reader.dtd;
/*    */ 
/*    */ import java.text.MessageFormat;
/*    */ import java.util.ResourceBundle;
/*    */ 
/*    */ class Messages
/*    */ {
/*    */   public static final String ERR_NO_ROOT_ELEMENT = "TDTDReader.NoRootElement";
/*    */   public static final String ERR_UNDEFINED_ELEMENT_IN_BINDINFO = "TDTDReader.UndefinedElementInBindInfo";
/*    */   public static final String ERR_CONVERSION_FOR_NON_VALUE_ELEMENT = "TDTDReader.ConversionForNonValueElement";
/*    */   public static final String ERR_CONTENT_PROPERTY_PARTICLE_MISMATCH = "TDTDReader.ContentProperty.ParticleMismatch";
/*    */   public static final String ERR_CONTENT_PROPERTY_DECLARATION_TOO_SHORT = "TDTDReader.ContentProperty.DeclarationTooShort";
/*    */   public static final String ERR_BINDINFO_NON_EXISTENT_ELEMENT_DECLARATION = "TDTDReader.BindInfo.NonExistentElementDeclaration";
/*    */   public static final String ERR_BINDINFO_NON_EXISTENT_INTERFACE_MEMBER = "TDTDReader.BindInfo.NonExistentInterfaceMember";
/*    */ 
/*    */   static String format(String property, Object[] args)
/*    */   {
/* 38 */     String text = ResourceBundle.getBundle(Messages.class.getPackage().getName() + ".MessageBundle").getString(property);
/* 39 */     return MessageFormat.format(text, args);
/*    */   }
/*    */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.internal.xjc.reader.dtd.Messages
 * JD-Core Version:    0.6.2
 */