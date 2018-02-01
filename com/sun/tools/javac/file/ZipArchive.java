/*     */ package com.sun.tools.javac.file;
/*     */ 
/*     */ import com.sun.tools.javac.util.List;
/*     */ import com.sun.tools.javac.util.Log;
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.OutputStream;
/*     */ import java.io.Writer;
/*     */ import java.lang.ref.Reference;
/*     */ import java.lang.ref.SoftReference;
/*     */ import java.net.URI;
/*     */ import java.nio.ByteBuffer;
/*     */ import java.nio.CharBuffer;
/*     */ import java.nio.charset.CharsetDecoder;
/*     */ import java.util.Enumeration;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import java.util.zip.ZipEntry;
/*     */ import java.util.zip.ZipFile;
/*     */ import javax.tools.JavaFileObject;
/*     */ import javax.tools.JavaFileObject.Kind;
/*     */ 
/*     */ public class ZipArchive
/*     */   implements JavacFileManager.Archive
/*     */ {
/*     */   protected JavacFileManager fileManager;
/*     */   protected final Map<RelativePath.RelativeDirectory, List<String>> map;
/*     */   protected final ZipFile zfile;
/*     */   protected Reference<File> absFileRef;
/*     */ 
/*     */   public ZipArchive(JavacFileManager paramJavacFileManager, ZipFile paramZipFile)
/*     */     throws IOException
/*     */   {
/*  62 */     this(paramJavacFileManager, paramZipFile, true);
/*     */   }
/*     */ 
/*     */   protected ZipArchive(JavacFileManager paramJavacFileManager, ZipFile paramZipFile, boolean paramBoolean) throws IOException {
/*  66 */     this.fileManager = paramJavacFileManager;
/*  67 */     this.zfile = paramZipFile;
/*  68 */     this.map = new HashMap();
/*  69 */     if (paramBoolean)
/*  70 */       initMap();
/*     */   }
/*     */ 
/*     */   protected void initMap() throws IOException {
/*  74 */     for (Enumeration localEnumeration = this.zfile.entries(); localEnumeration.hasMoreElements(); ) {
/*     */       ZipEntry localZipEntry;
/*     */       try {
/*  77 */         localZipEntry = (ZipEntry)localEnumeration.nextElement();
/*     */       } catch (InternalError localInternalError) {
/*  79 */         IOException localIOException = new IOException();
/*  80 */         localIOException.initCause(localInternalError);
/*  81 */         throw localIOException;
/*     */       }
/*  83 */       addZipEntry(localZipEntry);
/*     */     }
/*     */   }
/*     */ 
/*     */   void addZipEntry(ZipEntry paramZipEntry) {
/*  88 */     String str1 = paramZipEntry.getName();
/*  89 */     int i = str1.lastIndexOf('/');
/*  90 */     RelativePath.RelativeDirectory localRelativeDirectory = new RelativePath.RelativeDirectory(str1.substring(0, i + 1));
/*  91 */     String str2 = str1.substring(i + 1);
/*  92 */     if (str2.length() == 0)
/*  93 */       return;
/*  94 */     List localList = (List)this.map.get(localRelativeDirectory);
/*  95 */     if (localList == null)
/*  96 */       localList = List.nil();
/*  97 */     localList = localList.prepend(str2);
/*  98 */     this.map.put(localRelativeDirectory, localList);
/*     */   }
/*     */ 
/*     */   public boolean contains(RelativePath paramRelativePath) {
/* 102 */     RelativePath.RelativeDirectory localRelativeDirectory = paramRelativePath.dirname();
/* 103 */     String str = paramRelativePath.basename();
/* 104 */     if (str.length() == 0)
/* 105 */       return false;
/* 106 */     List localList = (List)this.map.get(localRelativeDirectory);
/* 107 */     return (localList != null) && (localList.contains(str));
/*     */   }
/*     */ 
/*     */   public List<String> getFiles(RelativePath.RelativeDirectory paramRelativeDirectory) {
/* 111 */     return (List)this.map.get(paramRelativeDirectory);
/*     */   }
/*     */ 
/*     */   public JavaFileObject getFileObject(RelativePath.RelativeDirectory paramRelativeDirectory, String paramString) {
/* 115 */     ZipEntry localZipEntry = new RelativePath.RelativeFile(paramRelativeDirectory, paramString).getZipEntry(this.zfile);
/* 116 */     return new ZipFileObject(this, paramString, localZipEntry);
/*     */   }
/*     */ 
/*     */   public Set<RelativePath.RelativeDirectory> getSubdirectories() {
/* 120 */     return this.map.keySet();
/*     */   }
/*     */ 
/*     */   public void close() throws IOException {
/* 124 */     this.zfile.close();
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 129 */     return "ZipArchive[" + this.zfile.getName() + "]";
/*     */   }
/*     */ 
/*     */   private File getAbsoluteFile() {
/* 133 */     File localFile = this.absFileRef == null ? null : (File)this.absFileRef.get();
/* 134 */     if (localFile == null) {
/* 135 */       localFile = new File(this.zfile.getName()).getAbsoluteFile();
/* 136 */       this.absFileRef = new SoftReference(localFile);
/*     */     }
/* 138 */     return localFile;
/*     */   }
/*     */ 
/*     */   public static class ZipFileObject extends BaseFileObject
/*     */   {
/*     */     private String name;
/*     */     ZipArchive zarch;
/*     */     ZipEntry entry;
/*     */ 
/*     */     protected ZipFileObject(ZipArchive paramZipArchive, String paramString, ZipEntry paramZipEntry)
/*     */     {
/* 168 */       super();
/* 169 */       this.zarch = paramZipArchive;
/* 170 */       this.name = paramString;
/* 171 */       this.entry = paramZipEntry;
/*     */     }
/*     */ 
/*     */     public URI toUri() {
/* 175 */       File localFile = new File(this.zarch.zfile.getName());
/* 176 */       return createJarUri(localFile, this.entry.getName());
/*     */     }
/*     */ 
/*     */     public String getName()
/*     */     {
/* 181 */       return this.zarch.zfile.getName() + "(" + this.entry.getName() + ")";
/*     */     }
/*     */ 
/*     */     public String getShortName()
/*     */     {
/* 186 */       return new File(this.zarch.zfile.getName()).getName() + "(" + this.entry + ")";
/*     */     }
/*     */ 
/*     */     public JavaFileObject.Kind getKind()
/*     */     {
/* 191 */       return getKind(this.entry.getName());
/*     */     }
/*     */ 
/*     */     public InputStream openInputStream() throws IOException
/*     */     {
/* 196 */       return this.zarch.zfile.getInputStream(this.entry);
/*     */     }
/*     */ 
/*     */     public OutputStream openOutputStream() throws IOException
/*     */     {
/* 201 */       throw new UnsupportedOperationException();
/*     */     }
/*     */ 
/*     */     public CharBuffer getCharContent(boolean paramBoolean) throws IOException
/*     */     {
/* 206 */       CharBuffer localCharBuffer = this.fileManager.getCachedContent(this);
/* 207 */       if (localCharBuffer == null) {
/* 208 */         InputStream localInputStream = this.zarch.zfile.getInputStream(this.entry);
/*     */         try {
/* 210 */           ByteBuffer localByteBuffer = this.fileManager.makeByteBuffer(localInputStream);
/* 211 */           JavaFileObject localJavaFileObject = this.fileManager.log.useSource(this);
/*     */           try {
/* 213 */             localCharBuffer = this.fileManager.decode(localByteBuffer, paramBoolean);
/*     */           } finally {
/* 215 */             this.fileManager.log.useSource(localJavaFileObject);
/*     */           }
/* 217 */           this.fileManager.recycleByteBuffer(localByteBuffer);
/* 218 */           if (!paramBoolean)
/* 219 */             this.fileManager.cache(this, localCharBuffer);
/*     */         }
/*     */         finally {
/* 222 */           localInputStream.close();
/*     */         }
/*     */       }
/* 225 */       return localCharBuffer;
/*     */     }
/*     */ 
/*     */     public Writer openWriter() throws IOException
/*     */     {
/* 230 */       throw new UnsupportedOperationException();
/*     */     }
/*     */ 
/*     */     public long getLastModified()
/*     */     {
/* 235 */       return this.entry.getTime();
/*     */     }
/*     */ 
/*     */     public boolean delete()
/*     */     {
/* 240 */       throw new UnsupportedOperationException();
/*     */     }
/*     */ 
/*     */     protected CharsetDecoder getDecoder(boolean paramBoolean)
/*     */     {
/* 245 */       return this.fileManager.getDecoder(this.fileManager.getEncodingName(), paramBoolean);
/*     */     }
/*     */ 
/*     */     protected String inferBinaryName(Iterable<? extends File> paramIterable)
/*     */     {
/* 250 */       String str = this.entry.getName();
/* 251 */       return removeExtension(str).replace('/', '.');
/*     */     }
/*     */ 
/*     */     public boolean isNameCompatible(String paramString, JavaFileObject.Kind paramKind)
/*     */     {
/* 256 */       paramString.getClass();
/*     */ 
/* 258 */       if ((paramKind == JavaFileObject.Kind.OTHER) && (getKind() != paramKind)) {
/* 259 */         return false;
/*     */       }
/* 261 */       return this.name.equals(paramString + paramKind.extension);
/*     */     }
/*     */ 
/*     */     public boolean equals(Object paramObject)
/*     */     {
/* 271 */       if (this == paramObject) {
/* 272 */         return true;
/*     */       }
/* 274 */       if (!(paramObject instanceof ZipFileObject)) {
/* 275 */         return false;
/*     */       }
/* 277 */       ZipFileObject localZipFileObject = (ZipFileObject)paramObject;
/*     */ 
/* 279 */       return (this.zarch.getAbsoluteFile().equals(localZipFileObject.zarch.getAbsoluteFile())) && 
/* 279 */         (this.name
/* 279 */         .equals(localZipFileObject.name));
/*     */     }
/*     */ 
/*     */     public int hashCode()
/*     */     {
/* 284 */       return this.zarch.getAbsoluteFile().hashCode() + this.name.hashCode();
/*     */     }
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.javac.file.ZipArchive
 * JD-Core Version:    0.6.2
 */