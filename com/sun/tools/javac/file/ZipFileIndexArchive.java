/*     */ package com.sun.tools.javac.file;
/*     */ 
/*     */ import com.sun.tools.javac.util.Assert;
/*     */ import com.sun.tools.javac.util.List;
/*     */ import com.sun.tools.javac.util.Log;
/*     */ import java.io.ByteArrayInputStream;
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.OutputStream;
/*     */ import java.io.Writer;
/*     */ import java.net.URI;
/*     */ import java.nio.ByteBuffer;
/*     */ import java.nio.CharBuffer;
/*     */ import java.nio.charset.CharsetDecoder;
/*     */ import java.util.Set;
/*     */ import javax.tools.JavaFileObject;
/*     */ import javax.tools.JavaFileObject.Kind;
/*     */ 
/*     */ public class ZipFileIndexArchive
/*     */   implements JavacFileManager.Archive
/*     */ {
/*     */   private final ZipFileIndex zfIndex;
/*     */   private JavacFileManager fileManager;
/*     */ 
/*     */   public ZipFileIndexArchive(JavacFileManager paramJavacFileManager, ZipFileIndex paramZipFileIndex)
/*     */     throws IOException
/*     */   {
/*  61 */     this.fileManager = paramJavacFileManager;
/*  62 */     this.zfIndex = paramZipFileIndex;
/*     */   }
/*     */ 
/*     */   public boolean contains(RelativePath paramRelativePath) {
/*  66 */     return this.zfIndex.contains(paramRelativePath);
/*     */   }
/*     */ 
/*     */   public List<String> getFiles(RelativePath.RelativeDirectory paramRelativeDirectory) {
/*  70 */     return this.zfIndex.getFiles(paramRelativeDirectory);
/*     */   }
/*     */ 
/*     */   public JavaFileObject getFileObject(RelativePath.RelativeDirectory paramRelativeDirectory, String paramString) {
/*  74 */     RelativePath.RelativeFile localRelativeFile = new RelativePath.RelativeFile(paramRelativeDirectory, paramString);
/*  75 */     ZipFileIndex.Entry localEntry = this.zfIndex.getZipIndexEntry(localRelativeFile);
/*  76 */     ZipFileIndexFileObject localZipFileIndexFileObject = new ZipFileIndexFileObject(this.fileManager, this.zfIndex, localEntry, this.zfIndex.getZipFile());
/*  77 */     return localZipFileIndexFileObject;
/*     */   }
/*     */ 
/*     */   public Set<RelativePath.RelativeDirectory> getSubdirectories() {
/*  81 */     return this.zfIndex.getAllDirectories();
/*     */   }
/*     */ 
/*     */   public void close() throws IOException {
/*  85 */     this.zfIndex.close();
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/*  90 */     return "ZipFileIndexArchive[" + this.zfIndex + "]";
/*     */   }
/*     */ 
/*     */   public static class ZipFileIndexFileObject extends BaseFileObject
/*     */   {
/*     */     private String name;
/*     */     ZipFileIndex zfIndex;
/*     */     ZipFileIndex.Entry entry;
/* 112 */     InputStream inputStream = null;
/*     */     File zipName;
/*     */ 
/*     */     ZipFileIndexFileObject(JavacFileManager paramJavacFileManager, ZipFileIndex paramZipFileIndex, ZipFileIndex.Entry paramEntry, File paramFile)
/*     */     {
/* 120 */       super();
/* 121 */       this.name = paramEntry.getFileName();
/* 122 */       this.zfIndex = paramZipFileIndex;
/* 123 */       this.entry = paramEntry;
/* 124 */       this.zipName = paramFile;
/*     */     }
/*     */ 
/*     */     public URI toUri()
/*     */     {
/* 129 */       return createJarUri(this.zipName, getPrefixedEntryName());
/*     */     }
/*     */ 
/*     */     public String getName()
/*     */     {
/* 134 */       return this.zipName + "(" + getPrefixedEntryName() + ")";
/*     */     }
/*     */ 
/*     */     public String getShortName()
/*     */     {
/* 139 */       return this.zipName.getName() + "(" + this.entry.getName() + ")";
/*     */     }
/*     */ 
/*     */     public JavaFileObject.Kind getKind()
/*     */     {
/* 144 */       return getKind(this.entry.getName());
/*     */     }
/*     */ 
/*     */     public InputStream openInputStream() throws IOException
/*     */     {
/* 149 */       if (this.inputStream == null) {
/* 150 */         Assert.checkNonNull(this.entry);
/* 151 */         this.inputStream = new ByteArrayInputStream(this.zfIndex.read(this.entry));
/*     */       }
/* 153 */       return this.inputStream;
/*     */     }
/*     */ 
/*     */     public OutputStream openOutputStream() throws IOException
/*     */     {
/* 158 */       throw new UnsupportedOperationException();
/*     */     }
/*     */ 
/*     */     public CharBuffer getCharContent(boolean paramBoolean) throws IOException
/*     */     {
/* 163 */       CharBuffer localCharBuffer = this.fileManager.getCachedContent(this);
/* 164 */       if (localCharBuffer == null) {
/* 165 */         ByteArrayInputStream localByteArrayInputStream = new ByteArrayInputStream(this.zfIndex.read(this.entry));
/*     */         try {
/* 167 */           ByteBuffer localByteBuffer = this.fileManager.makeByteBuffer(localByteArrayInputStream);
/* 168 */           JavaFileObject localJavaFileObject = this.fileManager.log.useSource(this);
/*     */           try {
/* 170 */             localCharBuffer = this.fileManager.decode(localByteBuffer, paramBoolean);
/*     */           } finally {
/* 172 */             this.fileManager.log.useSource(localJavaFileObject);
/*     */           }
/* 174 */           this.fileManager.recycleByteBuffer(localByteBuffer);
/* 175 */           if (!paramBoolean)
/* 176 */             this.fileManager.cache(this, localCharBuffer);
/*     */         } finally {
/* 178 */           localByteArrayInputStream.close();
/*     */         }
/*     */       }
/* 181 */       return localCharBuffer;
/*     */     }
/*     */ 
/*     */     public Writer openWriter() throws IOException
/*     */     {
/* 186 */       throw new UnsupportedOperationException();
/*     */     }
/*     */ 
/*     */     public long getLastModified()
/*     */     {
/* 191 */       return this.entry.getLastModified();
/*     */     }
/*     */ 
/*     */     public boolean delete()
/*     */     {
/* 196 */       throw new UnsupportedOperationException();
/*     */     }
/*     */ 
/*     */     protected CharsetDecoder getDecoder(boolean paramBoolean)
/*     */     {
/* 201 */       return this.fileManager.getDecoder(this.fileManager.getEncodingName(), paramBoolean);
/*     */     }
/*     */ 
/*     */     protected String inferBinaryName(Iterable<? extends File> paramIterable)
/*     */     {
/* 206 */       String str1 = this.entry.getName();
/* 207 */       if (this.zfIndex.symbolFilePrefix != null) {
/* 208 */         String str2 = this.zfIndex.symbolFilePrefix.path;
/* 209 */         if (str1.startsWith(str2))
/* 210 */           str1 = str1.substring(str2.length());
/*     */       }
/* 212 */       return removeExtension(str1).replace('/', '.');
/*     */     }
/*     */ 
/*     */     public boolean isNameCompatible(String paramString, JavaFileObject.Kind paramKind)
/*     */     {
/* 217 */       paramString.getClass();
/* 218 */       if ((paramKind == JavaFileObject.Kind.OTHER) && (getKind() != paramKind))
/* 219 */         return false;
/* 220 */       return this.name.equals(paramString + paramKind.extension);
/*     */     }
/*     */ 
/*     */     public boolean equals(Object paramObject)
/*     */     {
/* 230 */       if (this == paramObject) {
/* 231 */         return true;
/*     */       }
/* 233 */       if (!(paramObject instanceof ZipFileIndexFileObject)) {
/* 234 */         return false;
/*     */       }
/* 236 */       ZipFileIndexFileObject localZipFileIndexFileObject = (ZipFileIndexFileObject)paramObject;
/*     */ 
/* 238 */       return (this.zfIndex.getAbsoluteFile().equals(localZipFileIndexFileObject.zfIndex.getAbsoluteFile())) && 
/* 238 */         (this.name
/* 238 */         .equals(localZipFileIndexFileObject.name));
/*     */     }
/*     */ 
/*     */     public int hashCode()
/*     */     {
/* 243 */       return this.zfIndex.getAbsoluteFile().hashCode() + this.name.hashCode();
/*     */     }
/*     */ 
/*     */     private String getPrefixedEntryName() {
/* 247 */       if (this.zfIndex.symbolFilePrefix != null) {
/* 248 */         return this.zfIndex.symbolFilePrefix.path + this.entry.getName();
/*     */       }
/* 250 */       return this.entry.getName();
/*     */     }
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.javac.file.ZipFileIndexArchive
 * JD-Core Version:    0.6.2
 */