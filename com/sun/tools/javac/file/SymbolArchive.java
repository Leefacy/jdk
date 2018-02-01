/*     */ package com.sun.tools.javac.file;
/*     */ 
/*     */ import com.sun.tools.javac.util.List;
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.util.Map;
/*     */ import java.util.zip.ZipEntry;
/*     */ import java.util.zip.ZipFile;
/*     */ import javax.tools.JavaFileObject;
/*     */ 
/*     */ public class SymbolArchive extends ZipArchive
/*     */ {
/*     */   final File origFile;
/*     */   final RelativePath.RelativeDirectory prefix;
/*     */ 
/*     */   public SymbolArchive(JavacFileManager paramJavacFileManager, File paramFile, ZipFile paramZipFile, RelativePath.RelativeDirectory paramRelativeDirectory)
/*     */     throws IOException
/*     */   {
/*  50 */     super(paramJavacFileManager, paramZipFile, false);
/*  51 */     this.origFile = paramFile;
/*  52 */     this.prefix = paramRelativeDirectory;
/*  53 */     initMap();
/*     */   }
/*     */ 
/*     */   void addZipEntry(ZipEntry paramZipEntry)
/*     */   {
/*  58 */     String str1 = paramZipEntry.getName();
/*  59 */     if (!str1.startsWith(this.prefix.path)) {
/*  60 */       return;
/*     */     }
/*  62 */     str1 = str1.substring(this.prefix.path.length());
/*  63 */     int i = str1.lastIndexOf('/');
/*  64 */     RelativePath.RelativeDirectory localRelativeDirectory = new RelativePath.RelativeDirectory(str1.substring(0, i + 1));
/*  65 */     String str2 = str1.substring(i + 1);
/*  66 */     if (str2.length() == 0) {
/*  67 */       return;
/*     */     }
/*  69 */     List localList = (List)this.map.get(localRelativeDirectory);
/*  70 */     if (localList == null)
/*  71 */       localList = List.nil();
/*  72 */     localList = localList.prepend(str2);
/*  73 */     this.map.put(localRelativeDirectory, localList);
/*     */   }
/*     */ 
/*     */   public JavaFileObject getFileObject(RelativePath.RelativeDirectory paramRelativeDirectory, String paramString)
/*     */   {
/*  78 */     RelativePath.RelativeDirectory localRelativeDirectory = new RelativePath.RelativeDirectory(this.prefix, paramRelativeDirectory.path);
/*  79 */     ZipEntry localZipEntry = new RelativePath.RelativeFile(localRelativeDirectory, paramString).getZipEntry(this.zfile);
/*  80 */     return new SymbolFileObject(this, paramString, localZipEntry);
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/*  85 */     return "SymbolArchive[" + this.zfile.getName() + "]";
/*     */   }
/*     */ 
/*     */   public static class SymbolFileObject extends ZipArchive.ZipFileObject
/*     */   {
/*     */     protected SymbolFileObject(SymbolArchive paramSymbolArchive, String paramString, ZipEntry paramZipEntry)
/*     */     {
/*  93 */       super(paramString, paramZipEntry);
/*     */     }
/*     */ 
/*     */     protected String inferBinaryName(Iterable<? extends File> paramIterable)
/*     */     {
/*  98 */       String str1 = this.entry.getName();
/*  99 */       String str2 = ((SymbolArchive)this.zarch).prefix.path;
/* 100 */       if (str1.startsWith(str2))
/* 101 */         str1 = str1.substring(str2.length());
/* 102 */       return removeExtension(str1).replace('/', '.');
/*     */     }
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.javac.file.SymbolArchive
 * JD-Core Version:    0.6.2
 */