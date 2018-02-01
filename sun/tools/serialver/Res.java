/*     */ package sun.tools.serialver;
/*     */ 
/*     */ import java.text.MessageFormat;
/*     */ import java.util.MissingResourceException;
/*     */ import java.util.ResourceBundle;
/*     */ 
/*     */ class Res
/*     */ {
/*     */   private static ResourceBundle messageRB;
/*     */ 
/*     */   static void initResource()
/*     */   {
/*     */     try
/*     */     {
/* 416 */       messageRB = ResourceBundle.getBundle("sun.tools.serialver.resources.serialver");
/*     */     }
/*     */     catch (MissingResourceException localMissingResourceException) {
/* 418 */       throw new Error("Fatal: Resource for serialver is missing");
/*     */     }
/*     */   }
/*     */ 
/*     */   static String getText(String paramString)
/*     */   {
/* 428 */     return getText(paramString, (String)null);
/*     */   }
/*     */ 
/*     */   static String getText(String paramString1, String paramString2)
/*     */   {
/* 438 */     return getText(paramString1, paramString2, null);
/*     */   }
/*     */ 
/*     */   static String getText(String paramString1, String paramString2, String paramString3)
/*     */   {
/* 449 */     return getText(paramString1, paramString2, paramString3, null);
/*     */   }
/*     */ 
/*     */   static String getText(String paramString1, String paramString2, String paramString3, String paramString4)
/*     */   {
/* 461 */     if (messageRB == null)
/* 462 */       initResource();
/*     */     try
/*     */     {
/* 465 */       String str = messageRB.getString(paramString1);
/* 466 */       return MessageFormat.format(str, new Object[] { paramString2, paramString3, paramString4 }); } catch (MissingResourceException localMissingResourceException) {
/*     */     }
/* 468 */     throw new Error("Fatal: Resource for serialver is broken. There is no " + paramString1 + " key in resource.");
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     sun.tools.serialver.Res
 * JD-Core Version:    0.6.2
 */