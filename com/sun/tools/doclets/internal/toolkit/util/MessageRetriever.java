/*     */ package com.sun.tools.doclets.internal.toolkit.util;
/*     */ 
/*     */ import com.sun.javadoc.RootDoc;
/*     */ import com.sun.javadoc.SourcePosition;
/*     */ import com.sun.tools.doclets.internal.toolkit.Configuration;
/*     */ import java.text.MessageFormat;
/*     */ import java.util.MissingResourceException;
/*     */ import java.util.ResourceBundle;
/*     */ 
/*     */ public class MessageRetriever
/*     */ {
/*     */   private final Configuration configuration;
/*     */   private final String resourcelocation;
/*     */   private ResourceBundle messageRB;
/*     */ 
/*     */   public MessageRetriever(ResourceBundle paramResourceBundle)
/*     */   {
/*  68 */     this.configuration = null;
/*  69 */     this.messageRB = paramResourceBundle;
/*  70 */     this.resourcelocation = null;
/*     */   }
/*     */ 
/*     */   public MessageRetriever(Configuration paramConfiguration, String paramString)
/*     */   {
/*  81 */     this.configuration = paramConfiguration;
/*  82 */     this.resourcelocation = paramString;
/*     */   }
/*     */ 
/*     */   public String getText(String paramString, Object[] paramArrayOfObject)
/*     */     throws MissingResourceException
/*     */   {
/*  94 */     if (this.messageRB == null) {
/*     */       try {
/*  96 */         this.messageRB = ResourceBundle.getBundle(this.resourcelocation);
/*     */       } catch (MissingResourceException localMissingResourceException) {
/*  98 */         throw new Error("Fatal: Resource (" + this.resourcelocation + ") for javadoc doclets is missing.");
/*     */       }
/*     */     }
/*     */ 
/* 102 */     String str = this.messageRB.getString(paramString);
/* 103 */     return MessageFormat.format(str, paramArrayOfObject);
/*     */   }
/*     */ 
/*     */   private void printError(SourcePosition paramSourcePosition, String paramString)
/*     */   {
/* 113 */     this.configuration.root.printError(paramSourcePosition, paramString);
/*     */   }
/*     */ 
/*     */   private void printError(String paramString)
/*     */   {
/* 122 */     this.configuration.root.printError(paramString);
/*     */   }
/*     */ 
/*     */   private void printWarning(SourcePosition paramSourcePosition, String paramString)
/*     */   {
/* 132 */     this.configuration.root.printWarning(paramSourcePosition, paramString);
/*     */   }
/*     */ 
/*     */   private void printWarning(String paramString)
/*     */   {
/* 141 */     this.configuration.root.printWarning(paramString);
/*     */   }
/*     */ 
/*     */   private void printNotice(SourcePosition paramSourcePosition, String paramString)
/*     */   {
/* 151 */     this.configuration.root.printNotice(paramSourcePosition, paramString);
/*     */   }
/*     */ 
/*     */   private void printNotice(String paramString)
/*     */   {
/* 160 */     this.configuration.root.printNotice(paramString);
/*     */   }
/*     */ 
/*     */   public void error(SourcePosition paramSourcePosition, String paramString, Object[] paramArrayOfObject)
/*     */   {
/* 171 */     printError(paramSourcePosition, getText(paramString, paramArrayOfObject));
/*     */   }
/*     */ 
/*     */   public void error(String paramString, Object[] paramArrayOfObject)
/*     */   {
/* 181 */     printError(getText(paramString, paramArrayOfObject));
/*     */   }
/*     */ 
/*     */   public void warning(SourcePosition paramSourcePosition, String paramString, Object[] paramArrayOfObject)
/*     */   {
/* 192 */     if (this.configuration.showMessage(paramSourcePosition, paramString))
/* 193 */       printWarning(paramSourcePosition, getText(paramString, paramArrayOfObject));
/*     */   }
/*     */ 
/*     */   public void warning(String paramString, Object[] paramArrayOfObject)
/*     */   {
/* 203 */     printWarning(getText(paramString, paramArrayOfObject));
/*     */   }
/*     */ 
/*     */   public void notice(SourcePosition paramSourcePosition, String paramString, Object[] paramArrayOfObject)
/*     */   {
/* 214 */     printNotice(paramSourcePosition, getText(paramString, paramArrayOfObject));
/*     */   }
/*     */ 
/*     */   public void notice(String paramString, Object[] paramArrayOfObject)
/*     */   {
/* 224 */     printNotice(getText(paramString, paramArrayOfObject));
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.doclets.internal.toolkit.util.MessageRetriever
 * JD-Core Version:    0.6.2
 */