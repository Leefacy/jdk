/*     */ package com.sun.tools.javac.nio;
/*     */ 
/*     */ import com.sun.tools.javac.util.BaseFileManager;
/*     */ import com.sun.tools.javac.util.Log;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.InputStreamReader;
/*     */ import java.io.OutputStream;
/*     */ import java.io.OutputStreamWriter;
/*     */ import java.io.Reader;
/*     */ import java.io.Writer;
/*     */ import java.net.URI;
/*     */ import java.nio.ByteBuffer;
/*     */ import java.nio.CharBuffer;
/*     */ import java.nio.charset.CharsetDecoder;
/*     */ import java.nio.file.FileSystem;
/*     */ import java.nio.file.Files;
/*     */ import java.nio.file.LinkOption;
/*     */ import java.nio.file.OpenOption;
/*     */ import java.nio.file.Path;
/*     */ import java.nio.file.attribute.FileAttribute;
/*     */ import java.nio.file.attribute.FileTime;
/*     */ import javax.lang.model.element.Modifier;
/*     */ import javax.lang.model.element.NestingKind;
/*     */ import javax.tools.JavaFileObject;
/*     */ import javax.tools.JavaFileObject.Kind;
/*     */ 
/*     */ abstract class PathFileObject
/*     */   implements JavaFileObject
/*     */ {
/*     */   private JavacPathFileManager fileManager;
/*     */   private Path path;
/*     */ 
/*     */   static PathFileObject createDirectoryPathFileObject(JavacPathFileManager paramJavacPathFileManager, final Path paramPath1, final Path paramPath2)
/*     */   {
/*  73 */     return new PathFileObject(paramJavacPathFileManager, paramPath1)
/*     */     {
/*     */       String inferBinaryName(Iterable<? extends Path> paramAnonymousIterable) {
/*  76 */         return toBinaryName(paramPath2.relativize(paramPath1));
/*     */       }
/*     */     };
/*     */   }
/*     */ 
/*     */   static PathFileObject createJarPathFileObject(JavacPathFileManager paramJavacPathFileManager, final Path paramPath)
/*     */   {
/*  87 */     return new PathFileObject(paramJavacPathFileManager, paramPath)
/*     */     {
/*     */       String inferBinaryName(Iterable<? extends Path> paramAnonymousIterable) {
/*  90 */         return toBinaryName(paramPath);
/*     */       }
/*     */     };
/*     */   }
/*     */ 
/*     */   static PathFileObject createSiblingPathFileObject(JavacPathFileManager paramJavacPathFileManager, Path paramPath, final String paramString)
/*     */   {
/* 101 */     return new PathFileObject(paramJavacPathFileManager, paramPath)
/*     */     {
/*     */       String inferBinaryName(Iterable<? extends Path> paramAnonymousIterable) {
/* 104 */         return toBinaryName(paramString, "/");
/*     */       }
/*     */     };
/*     */   }
/*     */ 
/*     */   static PathFileObject createSimplePathFileObject(JavacPathFileManager paramJavacPathFileManager, final Path paramPath)
/*     */   {
/* 115 */     return new PathFileObject(paramJavacPathFileManager, paramPath)
/*     */     {
/*     */       String inferBinaryName(Iterable<? extends Path> paramAnonymousIterable) {
/* 118 */         Path localPath1 = paramPath.toAbsolutePath();
/* 119 */         for (Path localPath2 : paramAnonymousIterable) {
/* 120 */           Path localPath3 = localPath2.toAbsolutePath();
/* 121 */           if (localPath1.startsWith(localPath3))
/*     */             try {
/* 123 */               Path localPath4 = localPath3.relativize(localPath1);
/* 124 */               if (localPath4 != null)
/* 125 */                 return toBinaryName(localPath4);
/*     */             }
/*     */             catch (IllegalArgumentException localIllegalArgumentException)
/*     */             {
/*     */             }
/*     */         }
/* 131 */         return null;
/*     */       }
/*     */     };
/*     */   }
/*     */ 
/*     */   protected PathFileObject(JavacPathFileManager paramJavacPathFileManager, Path paramPath) {
/* 137 */     paramJavacPathFileManager.getClass();
/* 138 */     paramPath.getClass();
/* 139 */     this.fileManager = paramJavacPathFileManager;
/* 140 */     this.path = paramPath;
/*     */   }
/*     */ 
/*     */   abstract String inferBinaryName(Iterable<? extends Path> paramIterable);
/*     */ 
/*     */   Path getPath()
/*     */   {
/* 150 */     return this.path;
/*     */   }
/*     */ 
/*     */   public JavaFileObject.Kind getKind()
/*     */   {
/* 155 */     return BaseFileManager.getKind(this.path.getFileName().toString());
/*     */   }
/*     */ 
/*     */   public boolean isNameCompatible(String paramString, JavaFileObject.Kind paramKind)
/*     */   {
/* 160 */     paramString.getClass();
/*     */ 
/* 162 */     if ((paramKind == JavaFileObject.Kind.OTHER) && (getKind() != paramKind)) {
/* 163 */       return false;
/*     */     }
/* 165 */     String str1 = paramString + paramKind.extension;
/* 166 */     String str2 = this.path.getFileName().toString();
/* 167 */     if (str2.equals(str1)) {
/* 168 */       return true;
/*     */     }
/* 170 */     if (str2.equalsIgnoreCase(str1))
/*     */       try
/*     */       {
/* 173 */         return this.path.toRealPath(new LinkOption[] { LinkOption.NOFOLLOW_LINKS }).getFileName().toString().equals(str1);
/*     */       }
/*     */       catch (IOException localIOException) {
/*     */       }
/* 177 */     return false;
/*     */   }
/*     */ 
/*     */   public NestingKind getNestingKind()
/*     */   {
/* 182 */     return null;
/*     */   }
/*     */ 
/*     */   public Modifier getAccessLevel()
/*     */   {
/* 187 */     return null;
/*     */   }
/*     */ 
/*     */   public URI toUri()
/*     */   {
/* 192 */     return this.path.toUri();
/*     */   }
/*     */ 
/*     */   public String getName()
/*     */   {
/* 197 */     return this.path.toString();
/*     */   }
/*     */ 
/*     */   public InputStream openInputStream() throws IOException
/*     */   {
/* 202 */     return Files.newInputStream(this.path, new OpenOption[0]);
/*     */   }
/*     */ 
/*     */   public OutputStream openOutputStream() throws IOException
/*     */   {
/* 207 */     this.fileManager.flushCache(this);
/* 208 */     ensureParentDirectoriesExist();
/* 209 */     return Files.newOutputStream(this.path, new OpenOption[0]);
/*     */   }
/*     */ 
/*     */   public Reader openReader(boolean paramBoolean) throws IOException
/*     */   {
/* 214 */     CharsetDecoder localCharsetDecoder = this.fileManager.getDecoder(this.fileManager.getEncodingName(), paramBoolean);
/* 215 */     return new InputStreamReader(openInputStream(), localCharsetDecoder);
/*     */   }
/*     */ 
/*     */   public CharSequence getCharContent(boolean paramBoolean) throws IOException
/*     */   {
/* 220 */     CharBuffer localCharBuffer = this.fileManager.getCachedContent(this);
/* 221 */     if (localCharBuffer == null) {
/* 222 */       InputStream localInputStream = openInputStream();
/*     */       try {
/* 224 */         ByteBuffer localByteBuffer = this.fileManager.makeByteBuffer(localInputStream);
/* 225 */         JavaFileObject localJavaFileObject = this.fileManager.log.useSource(this);
/*     */         try {
/* 227 */           localCharBuffer = this.fileManager.decode(localByteBuffer, paramBoolean);
/*     */         } finally {
/* 229 */           this.fileManager.log.useSource(localJavaFileObject);
/*     */         }
/* 231 */         this.fileManager.recycleByteBuffer(localByteBuffer);
/* 232 */         if (!paramBoolean)
/* 233 */           this.fileManager.cache(this, localCharBuffer);
/*     */       }
/*     */       finally {
/* 236 */         localInputStream.close();
/*     */       }
/*     */     }
/* 239 */     return localCharBuffer;
/*     */   }
/*     */ 
/*     */   public Writer openWriter() throws IOException
/*     */   {
/* 244 */     this.fileManager.flushCache(this);
/* 245 */     ensureParentDirectoriesExist();
/* 246 */     return new OutputStreamWriter(Files.newOutputStream(this.path, new OpenOption[0]), this.fileManager.getEncodingName());
/*     */   }
/*     */ 
/*     */   public long getLastModified()
/*     */   {
/*     */     try {
/* 252 */       return Files.getLastModifiedTime(this.path, new LinkOption[0]).toMillis(); } catch (IOException localIOException) {
/*     */     }
/* 254 */     return -1L;
/*     */   }
/*     */ 
/*     */   public boolean delete()
/*     */   {
/*     */     try
/*     */     {
/* 261 */       Files.delete(this.path);
/* 262 */       return true; } catch (IOException localIOException) {
/*     */     }
/* 264 */     return false;
/*     */   }
/*     */ 
/*     */   public boolean isSameFile(PathFileObject paramPathFileObject)
/*     */   {
/*     */     try {
/* 270 */       return Files.isSameFile(this.path, paramPathFileObject.path); } catch (IOException localIOException) {
/*     */     }
/* 272 */     return false;
/*     */   }
/*     */ 
/*     */   public boolean equals(Object paramObject)
/*     */   {
/* 278 */     return ((paramObject instanceof PathFileObject)) && (this.path.equals(((PathFileObject)paramObject).path));
/*     */   }
/*     */ 
/*     */   public int hashCode()
/*     */   {
/* 283 */     return this.path.hashCode();
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 288 */     return getClass().getSimpleName() + "[" + this.path + "]";
/*     */   }
/*     */ 
/*     */   private void ensureParentDirectoriesExist() throws IOException {
/* 292 */     Path localPath = this.path.getParent();
/* 293 */     if (localPath != null)
/* 294 */       Files.createDirectories(localPath, new FileAttribute[0]);
/*     */   }
/*     */ 
/*     */   private long size() {
/*     */     try {
/* 299 */       return Files.size(this.path); } catch (IOException localIOException) {
/*     */     }
/* 301 */     return -1L;
/*     */   }
/*     */ 
/*     */   protected static String toBinaryName(Path paramPath)
/*     */   {
/* 306 */     return toBinaryName(paramPath.toString(), paramPath
/* 307 */       .getFileSystem().getSeparator());
/*     */   }
/*     */ 
/*     */   protected static String toBinaryName(String paramString1, String paramString2) {
/* 311 */     return removeExtension(paramString1).replace(paramString2, ".");
/*     */   }
/*     */ 
/*     */   protected static String removeExtension(String paramString) {
/* 315 */     int i = paramString.lastIndexOf(".");
/* 316 */     return i == -1 ? paramString : paramString.substring(0, i);
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.javac.nio.PathFileObject
 * JD-Core Version:    0.6.2
 */