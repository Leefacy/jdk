/*     */ package com.sun.tools.javac.file;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.util.zip.ZipEntry;
/*     */ import java.util.zip.ZipFile;
/*     */ import javax.tools.JavaFileObject.Kind;
/*     */ 
/*     */ public abstract class RelativePath
/*     */   implements Comparable<RelativePath>
/*     */ {
/*     */   protected final String path;
/*     */ 
/*     */   protected RelativePath(String paramString)
/*     */   {
/*  48 */     this.path = paramString;
/*     */   }
/*     */ 
/*     */   public abstract RelativeDirectory dirname();
/*     */ 
/*     */   public abstract String basename();
/*     */ 
/*     */   public File getFile(File paramFile) {
/*  56 */     if (this.path.length() == 0)
/*  57 */       return paramFile;
/*  58 */     return new File(paramFile, this.path.replace('/', File.separatorChar));
/*     */   }
/*     */ 
/*     */   public int compareTo(RelativePath paramRelativePath) {
/*  62 */     return this.path.compareTo(paramRelativePath.path);
/*     */   }
/*     */ 
/*     */   public boolean equals(Object paramObject)
/*     */   {
/*  67 */     if (!(paramObject instanceof RelativePath))
/*  68 */       return false;
/*  69 */     return this.path.equals(((RelativePath)paramObject).path);
/*     */   }
/*     */ 
/*     */   public int hashCode()
/*     */   {
/*  74 */     return this.path.hashCode();
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/*  79 */     return "RelPath[" + this.path + "]";
/*     */   }
/*     */ 
/*     */   public String getPath() {
/*  83 */     return this.path;
/*     */   }
/*     */ 
/*     */   public static class RelativeDirectory extends RelativePath
/*     */   {
/*     */     static RelativeDirectory forPackage(CharSequence paramCharSequence)
/*     */     {
/*  97 */       return new RelativeDirectory(paramCharSequence.toString().replace('.', '/'));
/*     */     }
/*     */ 
/*     */     public RelativeDirectory(String paramString)
/*     */     {
/* 104 */       super();
/*     */     }
/*     */ 
/*     */     public RelativeDirectory(RelativeDirectory paramRelativeDirectory, String paramString)
/*     */     {
/* 111 */       this(paramRelativeDirectory.path + paramString);
/*     */     }
/*     */ 
/*     */     public RelativeDirectory dirname()
/*     */     {
/* 116 */       int i = this.path.length();
/* 117 */       if (i == 0)
/* 118 */         return this;
/* 119 */       int j = this.path.lastIndexOf('/', i - 2);
/* 120 */       return new RelativeDirectory(this.path.substring(0, j + 1));
/*     */     }
/*     */ 
/*     */     public String basename()
/*     */     {
/* 125 */       int i = this.path.length();
/* 126 */       if (i == 0)
/* 127 */         return this.path;
/* 128 */       int j = this.path.lastIndexOf('/', i - 2);
/* 129 */       return this.path.substring(j + 1, i - 1);
/*     */     }
/*     */ 
/*     */     boolean contains(RelativePath paramRelativePath)
/*     */     {
/* 137 */       return (paramRelativePath.path.length() > this.path.length()) && (paramRelativePath.path.startsWith(this.path));
/*     */     }
/*     */ 
/*     */     public String toString()
/*     */     {
/* 142 */       return "RelativeDirectory[" + this.path + "]";
/*     */     }
/*     */   }
/*     */ 
/*     */   public static class RelativeFile extends RelativePath
/*     */   {
/*     */     static RelativeFile forClass(CharSequence paramCharSequence, JavaFileObject.Kind paramKind)
/*     */     {
/* 153 */       return new RelativeFile(paramCharSequence.toString().replace('.', '/') + paramKind.extension);
/*     */     }
/*     */ 
/*     */     public RelativeFile(String paramString) {
/* 157 */       super();
/* 158 */       if (paramString.endsWith("/"))
/* 159 */         throw new IllegalArgumentException(paramString);
/*     */     }
/*     */ 
/*     */     public RelativeFile(RelativePath.RelativeDirectory paramRelativeDirectory, String paramString)
/*     */     {
/* 166 */       this(paramRelativeDirectory.path + paramString);
/*     */     }
/*     */ 
/*     */     RelativeFile(RelativePath.RelativeDirectory paramRelativeDirectory, RelativePath paramRelativePath) {
/* 170 */       this(paramRelativeDirectory, paramRelativePath.path);
/*     */     }
/*     */ 
/*     */     public RelativePath.RelativeDirectory dirname()
/*     */     {
/* 175 */       int i = this.path.lastIndexOf('/');
/* 176 */       return new RelativePath.RelativeDirectory(this.path.substring(0, i + 1));
/*     */     }
/*     */ 
/*     */     public String basename()
/*     */     {
/* 181 */       int i = this.path.lastIndexOf('/');
/* 182 */       return this.path.substring(i + 1);
/*     */     }
/*     */ 
/*     */     ZipEntry getZipEntry(ZipFile paramZipFile) {
/* 186 */       return paramZipFile.getEntry(this.path);
/*     */     }
/*     */ 
/*     */     public String toString()
/*     */     {
/* 191 */       return "RelativeFile[" + this.path + "]";
/*     */     }
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.javac.file.RelativePath
 * JD-Core Version:    0.6.2
 */