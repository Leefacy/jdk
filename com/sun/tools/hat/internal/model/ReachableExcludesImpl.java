/*     */ package com.sun.tools.hat.internal.model;
/*     */ 
/*     */ import java.io.BufferedReader;
/*     */ import java.io.File;
/*     */ import java.io.FileInputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStreamReader;
/*     */ import java.io.PrintStream;
/*     */ import java.util.Hashtable;
/*     */ 
/*     */ public class ReachableExcludesImpl
/*     */   implements ReachableExcludes
/*     */ {
/*     */   private File excludesFile;
/*     */   private long lastModified;
/*     */   private Hashtable methods;
/*     */ 
/*     */   public ReachableExcludesImpl(File paramFile)
/*     */   {
/*  64 */     this.excludesFile = paramFile;
/*  65 */     readFile();
/*     */   }
/*     */ 
/*     */   private void readFileIfNeeded() {
/*  69 */     if (this.excludesFile.lastModified() != this.lastModified)
/*  70 */       synchronized (this) {
/*  71 */         if (this.excludesFile.lastModified() != this.lastModified)
/*  72 */           readFile();
/*     */       }
/*     */   }
/*     */ 
/*     */   private void readFile()
/*     */   {
/*  79 */     long l = this.excludesFile.lastModified();
/*  80 */     Hashtable localHashtable = new Hashtable();
/*     */     try
/*     */     {
/*  83 */       BufferedReader localBufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(this.excludesFile)));
/*     */       String str;
/*  87 */       while ((str = localBufferedReader.readLine()) != null) {
/*  88 */         localHashtable.put(str, str);
/*     */       }
/*  90 */       this.lastModified = l;
/*  91 */       this.methods = localHashtable;
/*     */     } catch (IOException localIOException) {
/*  93 */       System.out.println("Error reading " + this.excludesFile + ":  " + localIOException);
/*     */     }
/*     */   }
/*     */ 
/*     */   public boolean isExcluded(String paramString)
/*     */   {
/* 102 */     readFileIfNeeded();
/* 103 */     return this.methods.get(paramString) != null;
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.hat.internal.model.ReachableExcludesImpl
 * JD-Core Version:    0.6.2
 */