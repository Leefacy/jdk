/*    */ package sun.rmi.rmic.newrmic;
/*    */ 
/*    */ import java.text.MessageFormat;
/*    */ import java.util.MissingResourceException;
/*    */ import java.util.ResourceBundle;
/*    */ 
/*    */ public final class Resources
/*    */ {
/* 43 */   private static ResourceBundle resources = null;
/* 44 */   private static ResourceBundle resourcesExt = null;
/*    */ 
/*    */   private Resources()
/*    */   {
/* 60 */     throw new AssertionError();
/*    */   }
/*    */ 
/*    */   public static String getText(String paramString, String[] paramArrayOfString)
/*    */   {
/* 67 */     String str = getString(paramString);
/* 68 */     if (str == null) {
/* 69 */       str = "missing resource key: key = \"" + paramString + "\", " + "arguments = \"{0}\", \"{1}\", \"{2}\"";
/*    */     }
/*    */ 
/* 72 */     return MessageFormat.format(str, (Object[])paramArrayOfString);
/*    */   }
/*    */ 
/*    */   private static String getString(String paramString)
/*    */   {
/* 79 */     if (resourcesExt != null)
/*    */       try {
/* 81 */         return resourcesExt.getString(paramString);
/*    */       }
/*    */       catch (MissingResourceException localMissingResourceException1) {
/*    */       }
/* 85 */     if (resources != null) {
/*    */       try {
/* 87 */         return resources.getString(paramString);
/*    */       } catch (MissingResourceException localMissingResourceException2) {
/* 89 */         return null;
/*    */       }
/*    */     }
/* 92 */     return "missing resource bundle: key = \"" + paramString + "\", " + "arguments = \"{0}\", \"{1}\", \"{2}\"";
/*    */   }
/*    */ 
/*    */   static
/*    */   {
/*    */     try
/*    */     {
/* 48 */       resources = ResourceBundle.getBundle("sun.rmi.rmic.resources.rmic");
/*    */     }
/*    */     catch (MissingResourceException localMissingResourceException1)
/*    */     {
/*    */     }
/*    */     try
/*    */     {
/* 54 */       resourcesExt = ResourceBundle.getBundle("sun.rmi.rmic.resources.rmicext");
/*    */     }
/*    */     catch (MissingResourceException localMissingResourceException2)
/*    */     {
/*    */     }
/*    */   }
/*    */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     sun.rmi.rmic.newrmic.Resources
 * JD-Core Version:    0.6.2
 */