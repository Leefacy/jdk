/*     */ package sun.tools.java;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.io.FileInputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.util.zip.ZipEntry;
/*     */ import java.util.zip.ZipException;
/*     */ import java.util.zip.ZipFile;
/*     */ 
/*     */ public class ClassFile
/*     */ {
/*     */   private File file;
/*     */   private ZipFile zipFile;
/*     */   private ZipEntry zipEntry;
/*     */ 
/*     */   public ClassFile(File paramFile)
/*     */   {
/*  59 */     this.file = paramFile;
/*     */   }
/*     */ 
/*     */   public ClassFile(ZipFile paramZipFile, ZipEntry paramZipEntry)
/*     */   {
/*  66 */     this.zipFile = paramZipFile;
/*  67 */     this.zipEntry = paramZipEntry;
/*     */   }
/*     */ 
/*     */   public boolean isZipped()
/*     */   {
/*  74 */     return this.zipFile != null;
/*     */   }
/*     */ 
/*     */   public InputStream getInputStream()
/*     */     throws IOException
/*     */   {
/*  81 */     if (this.file != null)
/*  82 */       return new FileInputStream(this.file);
/*     */     try
/*     */     {
/*  85 */       return this.zipFile.getInputStream(this.zipEntry);
/*     */     } catch (ZipException localZipException) {
/*  87 */       throw new IOException(localZipException.getMessage());
/*     */     }
/*     */   }
/*     */ 
/*     */   public boolean exists()
/*     */   {
/*  96 */     return this.file != null ? this.file.exists() : true;
/*     */   }
/*     */ 
/*     */   public boolean isDirectory()
/*     */   {
/* 104 */     return this.file != null ? this.file.isDirectory() : this.zipEntry
/* 104 */       .getName().endsWith("/");
/*     */   }
/*     */ 
/*     */   public long lastModified()
/*     */   {
/* 111 */     return this.file != null ? this.file.lastModified() : this.zipEntry.getTime();
/*     */   }
/*     */ 
/*     */   public String getPath()
/*     */   {
/* 119 */     if (this.file != null) {
/* 120 */       return this.file.getPath();
/*     */     }
/* 122 */     return this.zipFile.getName() + "(" + this.zipEntry.getName() + ")";
/*     */   }
/*     */ 
/*     */   public String getName()
/*     */   {
/* 130 */     return this.file != null ? this.file.getName() : this.zipEntry.getName();
/*     */   }
/*     */ 
/*     */   public String getAbsoluteName()
/*     */   {
/*     */     String str;
/* 139 */     if (this.file != null)
/*     */       try {
/* 141 */         str = this.file.getCanonicalPath();
/*     */       } catch (IOException localIOException) {
/* 143 */         str = this.file.getAbsolutePath();
/*     */       }
/*     */     else {
/* 146 */       str = this.zipFile.getName() + "(" + this.zipEntry.getName() + ")";
/*     */     }
/* 148 */     return str;
/*     */   }
/*     */ 
/*     */   public long length()
/*     */   {
/* 156 */     return this.file != null ? this.file.length() : this.zipEntry.getSize();
/*     */   }
/*     */ 
/*     */   public String toString() {
/* 160 */     return this.file != null ? this.file.toString() : this.zipEntry.toString();
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     sun.tools.java.ClassFile
 * JD-Core Version:    0.6.2
 */