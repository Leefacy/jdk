/*     */ package com.sun.tools.javac.file;
/*     */ 
/*     */ import com.sun.tools.javac.util.Context;
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashMap;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ 
/*     */ public class ZipFileIndexCache
/*     */ {
/*  42 */   private final Map<File, ZipFileIndex> map = new HashMap();
/*     */   private static ZipFileIndexCache sharedInstance;
/*     */ 
/*     */   public static synchronized ZipFileIndexCache getSharedInstance()
/*     */   {
/*  48 */     if (sharedInstance == null)
/*  49 */       sharedInstance = new ZipFileIndexCache();
/*  50 */     return sharedInstance;
/*     */   }
/*     */ 
/*     */   public static ZipFileIndexCache instance(Context paramContext)
/*     */   {
/*  55 */     ZipFileIndexCache localZipFileIndexCache = (ZipFileIndexCache)paramContext.get(ZipFileIndexCache.class);
/*  56 */     if (localZipFileIndexCache == null)
/*  57 */       paramContext.put(ZipFileIndexCache.class, localZipFileIndexCache = new ZipFileIndexCache());
/*  58 */     return localZipFileIndexCache;
/*     */   }
/*     */ 
/*     */   public List<ZipFileIndex> getZipFileIndexes()
/*     */   {
/*  67 */     return getZipFileIndexes(false);
/*     */   }
/*     */ 
/*     */   public synchronized List<ZipFileIndex> getZipFileIndexes(boolean paramBoolean)
/*     */   {
/*  78 */     ArrayList localArrayList = new ArrayList();
/*     */ 
/*  80 */     localArrayList.addAll(this.map.values());
/*     */ 
/*  82 */     if (paramBoolean) {
/*  83 */       for (ZipFileIndex localZipFileIndex : localArrayList) {
/*  84 */         if (!localZipFileIndex.isOpen()) {
/*  85 */           localArrayList.remove(localZipFileIndex);
/*     */         }
/*     */       }
/*     */     }
/*     */ 
/*  90 */     return localArrayList;
/*     */   }
/*     */ 
/*     */   public synchronized ZipFileIndex getZipFileIndex(File paramFile, RelativePath.RelativeDirectory paramRelativeDirectory, boolean paramBoolean1, String paramString, boolean paramBoolean2)
/*     */     throws IOException
/*     */   {
/*  97 */     ZipFileIndex localZipFileIndex = getExistingZipIndex(paramFile);
/*     */ 
/*  99 */     if ((localZipFileIndex == null) || ((localZipFileIndex != null) && (paramFile.lastModified() != localZipFileIndex.zipFileLastModified))) {
/* 100 */       localZipFileIndex = new ZipFileIndex(paramFile, paramRelativeDirectory, paramBoolean2, paramBoolean1, paramString);
/*     */ 
/* 102 */       this.map.put(paramFile, localZipFileIndex);
/*     */     }
/* 104 */     return localZipFileIndex;
/*     */   }
/*     */ 
/*     */   public synchronized ZipFileIndex getExistingZipIndex(File paramFile) {
/* 108 */     return (ZipFileIndex)this.map.get(paramFile);
/*     */   }
/*     */ 
/*     */   public synchronized void clearCache() {
/* 112 */     this.map.clear();
/*     */   }
/*     */ 
/*     */   public synchronized void clearCache(long paramLong) {
/* 116 */     Iterator localIterator = this.map.keySet().iterator();
/* 117 */     while (localIterator.hasNext()) {
/* 118 */       File localFile = (File)localIterator.next();
/* 119 */       ZipFileIndex localZipFileIndex = (ZipFileIndex)this.map.get(localFile);
/* 120 */       if (localZipFileIndex != null) {
/* 121 */         long l = localZipFileIndex.lastReferenceTimeStamp + paramLong;
/* 122 */         if ((l < localZipFileIndex.lastReferenceTimeStamp) || 
/* 123 */           (System.currentTimeMillis() > l))
/* 124 */           this.map.remove(localFile);
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   public synchronized void removeFromCache(File paramFile)
/*     */   {
/* 131 */     this.map.remove(paramFile);
/*     */   }
/*     */ 
/*     */   public synchronized void setOpenedIndexes(List<ZipFileIndex> paramList)
/*     */     throws IllegalStateException
/*     */   {
/* 138 */     if (this.map.isEmpty()) {
/* 139 */       localObject = "Setting opened indexes should be called only when the ZipFileCache is empty. Call JavacFileManager.flush() before calling this method.";
/*     */ 
/* 142 */       throw new IllegalStateException((String)localObject);
/*     */     }
/*     */ 
/* 145 */     for (Object localObject = paramList.iterator(); ((Iterator)localObject).hasNext(); ) { ZipFileIndex localZipFileIndex = (ZipFileIndex)((Iterator)localObject).next();
/* 146 */       this.map.put(localZipFileIndex.zipFile, localZipFileIndex);
/*     */     }
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.javac.file.ZipFileIndexCache
 * JD-Core Version:    0.6.2
 */