/*     */ package com.sun.tools.javac.main;
/*     */ 
/*     */ import com.sun.tools.javac.util.Log;
/*     */ import com.sun.tools.javac.util.Log.PrefixKind;
/*     */ import java.io.File;
/*     */ 
/*     */ public abstract class OptionHelper
/*     */ {
/*     */   public abstract String get(Option paramOption);
/*     */ 
/*     */   public abstract void put(String paramString1, String paramString2);
/*     */ 
/*     */   public abstract void remove(String paramString);
/*     */ 
/*     */   public abstract Log getLog();
/*     */ 
/*     */   public abstract String getOwnName();
/*     */ 
/*     */   abstract void error(String paramString, Object[] paramArrayOfObject);
/*     */ 
/*     */   abstract void addFile(File paramFile);
/*     */ 
/*     */   abstract void addClassName(String paramString);
/*     */ 
/*     */   public static class GrumpyHelper extends OptionHelper
/*     */   {
/*     */     private final Log log;
/*     */ 
/*     */     public GrumpyHelper(Log paramLog)
/*     */     {
/*  73 */       this.log = paramLog;
/*     */     }
/*     */ 
/*     */     public Log getLog()
/*     */     {
/*  78 */       return this.log;
/*     */     }
/*     */ 
/*     */     public String getOwnName()
/*     */     {
/*  83 */       throw new IllegalStateException();
/*     */     }
/*     */ 
/*     */     public String get(Option paramOption)
/*     */     {
/*  88 */       throw new IllegalArgumentException();
/*     */     }
/*     */ 
/*     */     public void put(String paramString1, String paramString2)
/*     */     {
/*  93 */       throw new IllegalArgumentException();
/*     */     }
/*     */ 
/*     */     public void remove(String paramString)
/*     */     {
/*  98 */       throw new IllegalArgumentException();
/*     */     }
/*     */ 
/*     */     void error(String paramString, Object[] paramArrayOfObject)
/*     */     {
/* 103 */       throw new IllegalArgumentException(this.log.localize(Log.PrefixKind.JAVAC, paramString, paramArrayOfObject));
/*     */     }
/*     */ 
/*     */     public void addFile(File paramFile)
/*     */     {
/* 108 */       throw new IllegalArgumentException(paramFile.getPath());
/*     */     }
/*     */ 
/*     */     public void addClassName(String paramString)
/*     */     {
/* 113 */       throw new IllegalArgumentException(paramString);
/*     */     }
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.javac.main.OptionHelper
 * JD-Core Version:    0.6.2
 */