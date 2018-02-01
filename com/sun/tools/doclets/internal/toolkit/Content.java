/*     */ package com.sun.tools.doclets.internal.toolkit;
/*     */ 
/*     */ import com.sun.tools.doclets.internal.toolkit.util.DocletAbortException;
/*     */ import java.io.IOException;
/*     */ import java.io.StringWriter;
/*     */ import java.io.Writer;
/*     */ 
/*     */ public abstract class Content
/*     */ {
/*     */   public String toString()
/*     */   {
/*  53 */     StringWriter localStringWriter = new StringWriter();
/*     */     try {
/*  55 */       write(localStringWriter, true);
/*     */     }
/*     */     catch (IOException localIOException) {
/*  58 */       throw new DocletAbortException(localIOException);
/*     */     }
/*  60 */     return localStringWriter.toString();
/*     */   }
/*     */ 
/*     */   public abstract void addContent(Content paramContent);
/*     */ 
/*     */   public abstract void addContent(String paramString);
/*     */ 
/*     */   public abstract boolean write(Writer paramWriter, boolean paramBoolean)
/*     */     throws IOException;
/*     */ 
/*     */   public abstract boolean isEmpty();
/*     */ 
/*     */   public boolean isValid()
/*     */   {
/*  96 */     return !isEmpty();
/*     */   }
/*     */ 
/*     */   public int charCount()
/*     */   {
/* 105 */     return 0;
/*     */   }
/*     */ 
/*     */   protected static <T> T nullCheck(T paramT)
/*     */   {
/* 115 */     paramT.getClass();
/* 116 */     return paramT;
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.doclets.internal.toolkit.Content
 * JD-Core Version:    0.6.2
 */