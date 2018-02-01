/*    */ package com.sun.tools.javac.api;
/*    */ 
/*    */ import java.util.Locale;
/*    */ 
/*    */ public abstract interface Formattable
/*    */ {
/*    */   public abstract String toString(Locale paramLocale, Messages paramMessages);
/*    */ 
/*    */   public abstract String getKind();
/*    */ 
/*    */   public static class LocalizedString
/*    */     implements Formattable
/*    */   {
/*    */     String key;
/*    */ 
/*    */     public LocalizedString(String paramString)
/*    */     {
/* 62 */       this.key = paramString;
/*    */     }
/*    */ 
/*    */     public String toString(Locale paramLocale, Messages paramMessages) {
/* 66 */       return paramMessages.getLocalizedString(paramLocale, this.key, new Object[0]);
/*    */     }
/*    */     public String getKind() {
/* 69 */       return "LocalizedString";
/*    */     }
/*    */ 
/*    */     public String toString() {
/* 73 */       return this.key;
/*    */     }
/*    */   }
/*    */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.javac.api.Formattable
 * JD-Core Version:    0.6.2
 */