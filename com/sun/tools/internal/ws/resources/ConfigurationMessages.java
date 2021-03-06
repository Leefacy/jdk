/*    */ package com.sun.tools.internal.ws.resources;
/*    */ 
/*    */ import com.sun.istack.internal.localization.Localizable;
/*    */ import com.sun.istack.internal.localization.LocalizableMessageFactory;
/*    */ import com.sun.istack.internal.localization.Localizer;
/*    */ 
/*    */ public final class ConfigurationMessages
/*    */ {
/* 39 */   private static final LocalizableMessageFactory messageFactory = new LocalizableMessageFactory("com.sun.tools.internal.ws.resources.configuration");
/* 40 */   private static final Localizer localizer = new Localizer();
/*    */ 
/*    */   public static Localizable localizableCONFIGURATION_NOT_BINDING_FILE(Object arg0) {
/* 43 */     return messageFactory.getMessage("configuration.notBindingFile", new Object[] { arg0 });
/*    */   }
/*    */ 
/*    */   public static String CONFIGURATION_NOT_BINDING_FILE(Object arg0)
/*    */   {
/* 51 */     return localizer.localize(localizableCONFIGURATION_NOT_BINDING_FILE(arg0));
/*    */   }
/*    */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.internal.ws.resources.ConfigurationMessages
 * JD-Core Version:    0.6.2
 */