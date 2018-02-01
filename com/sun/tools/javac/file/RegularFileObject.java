/*     */ package com.sun.tools.javac.file;
/*     */ 
/*     */ import com.sun.tools.javac.util.Log;
/*     */ import java.io.File;
/*     */ import java.io.FileInputStream;
/*     */ import java.io.FileOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.OutputStream;
/*     */ import java.io.OutputStreamWriter;
/*     */ import java.io.Writer;
/*     */ import java.lang.ref.Reference;
/*     */ import java.lang.ref.SoftReference;
/*     */ import java.net.URI;
/*     */ import java.nio.ByteBuffer;
/*     */ import java.nio.CharBuffer;
/*     */ import java.nio.charset.CharsetDecoder;
/*     */ import java.text.Normalizer;
/*     */ import java.text.Normalizer.Form;
/*     */ import javax.tools.JavaFileObject;
/*     */ import javax.tools.JavaFileObject.Kind;
/*     */ 
/*     */ class RegularFileObject extends BaseFileObject
/*     */ {
/*  57 */   private boolean hasParents = false;
/*     */   private String name;
/*     */   final File file;
/*     */   private Reference<File> absFileRef;
/*  61 */   static final boolean isMacOS = System.getProperty("os.name", "").contains("OS X");
/*     */ 
/*     */   public RegularFileObject(JavacFileManager paramJavacFileManager, File paramFile) {
/*  64 */     this(paramJavacFileManager, paramFile.getName(), paramFile);
/*     */   }
/*     */ 
/*     */   public RegularFileObject(JavacFileManager paramJavacFileManager, String paramString, File paramFile) {
/*  68 */     super(paramJavacFileManager);
/*  69 */     if (paramFile.isDirectory()) {
/*  70 */       throw new IllegalArgumentException("directories not supported");
/*     */     }
/*  72 */     this.name = paramString;
/*  73 */     this.file = paramFile;
/*     */   }
/*     */ 
/*     */   public URI toUri()
/*     */   {
/*  78 */     return this.file.toURI().normalize();
/*     */   }
/*     */ 
/*     */   public String getName()
/*     */   {
/*  83 */     return this.file.getPath();
/*     */   }
/*     */ 
/*     */   public String getShortName()
/*     */   {
/*  88 */     return this.name;
/*     */   }
/*     */ 
/*     */   public JavaFileObject.Kind getKind()
/*     */   {
/*  93 */     return getKind(this.name);
/*     */   }
/*     */ 
/*     */   public InputStream openInputStream() throws IOException
/*     */   {
/*  98 */     return new FileInputStream(this.file);
/*     */   }
/*     */ 
/*     */   public OutputStream openOutputStream() throws IOException
/*     */   {
/* 103 */     this.fileManager.flushCache(this);
/* 104 */     ensureParentDirectoriesExist();
/* 105 */     return new FileOutputStream(this.file);
/*     */   }
/*     */ 
/*     */   public CharBuffer getCharContent(boolean paramBoolean) throws IOException
/*     */   {
/* 110 */     CharBuffer localCharBuffer = this.fileManager.getCachedContent(this);
/* 111 */     if (localCharBuffer == null) {
/* 112 */       FileInputStream localFileInputStream = new FileInputStream(this.file);
/*     */       try {
/* 114 */         ByteBuffer localByteBuffer = this.fileManager.makeByteBuffer(localFileInputStream);
/* 115 */         JavaFileObject localJavaFileObject = this.fileManager.log.useSource(this);
/*     */         try {
/* 117 */           localCharBuffer = this.fileManager.decode(localByteBuffer, paramBoolean);
/*     */         } finally {
/* 119 */           this.fileManager.log.useSource(localJavaFileObject);
/*     */         }
/* 121 */         this.fileManager.recycleByteBuffer(localByteBuffer);
/* 122 */         if (!paramBoolean)
/* 123 */           this.fileManager.cache(this, localCharBuffer);
/*     */       }
/*     */       finally {
/* 126 */         localFileInputStream.close();
/*     */       }
/*     */     }
/* 129 */     return localCharBuffer;
/*     */   }
/*     */ 
/*     */   public Writer openWriter() throws IOException
/*     */   {
/* 134 */     this.fileManager.flushCache(this);
/* 135 */     ensureParentDirectoriesExist();
/* 136 */     return new OutputStreamWriter(new FileOutputStream(this.file), this.fileManager.getEncodingName());
/*     */   }
/*     */ 
/*     */   public long getLastModified()
/*     */   {
/* 141 */     return this.file.lastModified();
/*     */   }
/*     */ 
/*     */   public boolean delete()
/*     */   {
/* 146 */     return this.file.delete();
/*     */   }
/*     */ 
/*     */   protected CharsetDecoder getDecoder(boolean paramBoolean)
/*     */   {
/* 151 */     return this.fileManager.getDecoder(this.fileManager.getEncodingName(), paramBoolean);
/*     */   }
/*     */ 
/*     */   protected String inferBinaryName(Iterable<? extends File> paramIterable)
/*     */   {
/* 156 */     String str1 = this.file.getPath();
/*     */ 
/* 158 */     for (File localFile : paramIterable)
/*     */     {
/* 160 */       String str2 = localFile.getPath();
/* 161 */       if (str2.length() == 0)
/* 162 */         str2 = System.getProperty("user.dir");
/* 163 */       if (!str2.endsWith(File.separator))
/* 164 */         str2 = str2 + File.separator;
/* 165 */       if ((str1.regionMatches(true, 0, str2, 0, str2.length())) && 
/* 166 */         (new File(str1
/* 166 */         .substring(0, str2
/* 166 */         .length())).equals(new File(str2)))) {
/* 167 */         String str3 = str1.substring(str2.length());
/* 168 */         return removeExtension(str3).replace(File.separatorChar, '.');
/*     */       }
/*     */     }
/* 171 */     return null;
/*     */   }
/*     */ 
/*     */   public boolean isNameCompatible(String paramString, JavaFileObject.Kind paramKind)
/*     */   {
/* 176 */     paramString.getClass();
/*     */ 
/* 178 */     if ((paramKind == JavaFileObject.Kind.OTHER) && (getKind() != paramKind)) {
/* 179 */       return false;
/*     */     }
/* 181 */     String str1 = paramString + paramKind.extension;
/* 182 */     if (this.name.equals(str1)) {
/* 183 */       return true;
/*     */     }
/* 185 */     if ((isMacOS) && (Normalizer.isNormalized(this.name, Normalizer.Form.NFD)) && 
/* 186 */       (Normalizer.isNormalized(str1, Normalizer.Form.NFC)))
/*     */     {
/* 190 */       String str2 = Normalizer.normalize(this.name, Normalizer.Form.NFC);
/* 191 */       if (str2.equals(str1)) {
/* 192 */         this.name = str2;
/* 193 */         return true;
/*     */       }
/*     */     }
/*     */ 
/* 197 */     if (this.name.equalsIgnoreCase(str1))
/*     */       try
/*     */       {
/* 200 */         return this.file.getCanonicalFile().getName().equals(str1);
/*     */       }
/*     */       catch (IOException localIOException) {
/*     */       }
/* 204 */     return false;
/*     */   }
/*     */ 
/*     */   private void ensureParentDirectoriesExist() throws IOException {
/* 208 */     if (!this.hasParents) {
/* 209 */       File localFile = this.file.getParentFile();
/* 210 */       if ((localFile != null) && (!localFile.exists()) && 
/* 211 */         (!localFile.mkdirs()) && (
/* 212 */         (!localFile.exists()) || (!localFile.isDirectory()))) {
/* 213 */         throw new IOException("could not create parent directories");
/*     */       }
/*     */ 
/* 217 */       this.hasParents = true;
/*     */     }
/*     */   }
/*     */ 
/*     */   public boolean equals(Object paramObject)
/*     */   {
/* 228 */     if (this == paramObject) {
/* 229 */       return true;
/*     */     }
/* 231 */     if (!(paramObject instanceof RegularFileObject)) {
/* 232 */       return false;
/*     */     }
/* 234 */     RegularFileObject localRegularFileObject = (RegularFileObject)paramObject;
/* 235 */     return getAbsoluteFile().equals(localRegularFileObject.getAbsoluteFile());
/*     */   }
/*     */ 
/*     */   public int hashCode()
/*     */   {
/* 240 */     return getAbsoluteFile().hashCode();
/*     */   }
/*     */ 
/*     */   private File getAbsoluteFile() {
/* 244 */     File localFile = this.absFileRef == null ? null : (File)this.absFileRef.get();
/* 245 */     if (localFile == null) {
/* 246 */       localFile = this.file.getAbsoluteFile();
/* 247 */       this.absFileRef = new SoftReference(localFile);
/*     */     }
/* 249 */     return localFile;
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.javac.file.RegularFileObject
 * JD-Core Version:    0.6.2
 */