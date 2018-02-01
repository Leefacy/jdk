/*     */ package com.sun.tools.javac.file;
/*     */ 
/*     */ import com.sun.tools.javac.util.Context;
/*     */ import com.sun.tools.javac.util.Context.Factory;
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.concurrent.ConcurrentHashMap;
/*     */ 
/*     */ public class CacheFSInfo extends FSInfo
/*     */ {
/* 115 */   private Map<File, Entry> cache = new ConcurrentHashMap();
/*     */ 
/*     */   public static void preRegister(Context paramContext)
/*     */   {
/*  50 */     paramContext.put(FSInfo.class, new Context.Factory() {
/*     */       public FSInfo make(Context paramAnonymousContext) {
/*  52 */         CacheFSInfo localCacheFSInfo = new CacheFSInfo();
/*  53 */         paramAnonymousContext.put(FSInfo.class, localCacheFSInfo);
/*  54 */         return localCacheFSInfo;
/*     */       }
/*     */     });
/*     */   }
/*     */ 
/*     */   public void clearCache() {
/*  60 */     this.cache.clear();
/*     */   }
/*     */ 
/*     */   public File getCanonicalFile(File paramFile)
/*     */   {
/*  65 */     Entry localEntry = getEntry(paramFile);
/*  66 */     return localEntry.canonicalFile;
/*     */   }
/*     */ 
/*     */   public boolean exists(File paramFile)
/*     */   {
/*  71 */     Entry localEntry = getEntry(paramFile);
/*  72 */     return localEntry.exists;
/*     */   }
/*     */ 
/*     */   public boolean isDirectory(File paramFile)
/*     */   {
/*  77 */     Entry localEntry = getEntry(paramFile);
/*  78 */     return localEntry.isDirectory;
/*     */   }
/*     */ 
/*     */   public boolean isFile(File paramFile)
/*     */   {
/*  83 */     Entry localEntry = getEntry(paramFile);
/*  84 */     return localEntry.isFile;
/*     */   }
/*     */ 
/*     */   public List<File> getJarClassPath(File paramFile)
/*     */     throws IOException
/*     */   {
/*  92 */     Entry localEntry = getEntry(paramFile);
/*  93 */     if (localEntry.jarClassPath == null)
/*  94 */       localEntry.jarClassPath = super.getJarClassPath(paramFile);
/*  95 */     return localEntry.jarClassPath;
/*     */   }
/*     */ 
/*     */   private Entry getEntry(File paramFile)
/*     */   {
/* 102 */     Entry localEntry = (Entry)this.cache.get(paramFile);
/* 103 */     if (localEntry == null) {
/* 104 */       localEntry = new Entry(null);
/* 105 */       localEntry.canonicalFile = super.getCanonicalFile(paramFile);
/* 106 */       localEntry.exists = super.exists(paramFile);
/* 107 */       localEntry.isDirectory = super.isDirectory(paramFile);
/* 108 */       localEntry.isFile = super.isFile(paramFile);
/* 109 */       this.cache.put(paramFile, localEntry);
/*     */     }
/* 111 */     return localEntry;
/*     */   }
/*     */ 
/*     */   private static class Entry
/*     */   {
/*     */     File canonicalFile;
/*     */     boolean exists;
/*     */     boolean isFile;
/*     */     boolean isDirectory;
/*     */     List<File> jarClassPath;
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.javac.file.CacheFSInfo
 * JD-Core Version:    0.6.2
 */