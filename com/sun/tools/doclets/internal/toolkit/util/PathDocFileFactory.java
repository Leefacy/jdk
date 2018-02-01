/*     */ package com.sun.tools.doclets.internal.toolkit.util;
/*     */ 
/*     */ import com.sun.tools.doclets.internal.toolkit.Configuration;
/*     */ import com.sun.tools.javac.nio.PathFileManager;
/*     */ import java.io.BufferedInputStream;
/*     */ import java.io.BufferedOutputStream;
/*     */ import java.io.BufferedWriter;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.OutputStream;
/*     */ import java.io.OutputStreamWriter;
/*     */ import java.io.UnsupportedEncodingException;
/*     */ import java.io.Writer;
/*     */ import java.nio.file.DirectoryStream;
/*     */ import java.nio.file.FileSystem;
/*     */ import java.nio.file.Files;
/*     */ import java.nio.file.LinkOption;
/*     */ import java.nio.file.Path;
/*     */ import java.nio.file.attribute.FileAttribute;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.Iterator;
/*     */ import java.util.LinkedHashSet;
/*     */ import java.util.List;
/*     */ import java.util.Set;
/*     */ import javax.tools.DocumentationTool.Location;
/*     */ import javax.tools.FileObject;
/*     */ import javax.tools.JavaFileManager.Location;
/*     */ import javax.tools.JavaFileObject;
/*     */ import javax.tools.StandardLocation;
/*     */ 
/*     */ class PathDocFileFactory extends DocFileFactory
/*     */ {
/*     */   private final PathFileManager fileManager;
/*     */   private final Path destDir;
/*     */ 
/*     */   public PathDocFileFactory(Configuration paramConfiguration)
/*     */   {
/*  71 */     super(paramConfiguration);
/*  72 */     this.fileManager = ((PathFileManager)paramConfiguration.getFileManager());
/*     */ 
/*  74 */     if ((!paramConfiguration.destDirName.isEmpty()) || 
/*  75 */       (!this.fileManager
/*  75 */       .hasLocation(DocumentationTool.Location.DOCUMENTATION_OUTPUT))) {
/*     */       try
/*     */       {
/*  77 */         String str = paramConfiguration.destDirName.isEmpty() ? "." : paramConfiguration.destDirName;
/*  78 */         Path localPath = this.fileManager.getDefaultFileSystem().getPath(str, new String[0]);
/*  79 */         this.fileManager.setLocation(DocumentationTool.Location.DOCUMENTATION_OUTPUT, Arrays.asList(new Path[] { localPath }));
/*     */       } catch (IOException localIOException) {
/*  81 */         throw new DocletAbortException(localIOException);
/*     */       }
/*     */     }
/*     */ 
/*  85 */     this.destDir = ((Path)this.fileManager.getLocation(DocumentationTool.Location.DOCUMENTATION_OUTPUT).iterator().next());
/*     */   }
/*     */ 
/*     */   public DocFile createFileForDirectory(String paramString) {
/*  89 */     return new StandardDocFile(this.fileManager.getDefaultFileSystem().getPath(paramString, new String[0]), null);
/*     */   }
/*     */ 
/*     */   public DocFile createFileForInput(String paramString) {
/*  93 */     return new StandardDocFile(this.fileManager.getDefaultFileSystem().getPath(paramString, new String[0]), null);
/*     */   }
/*     */ 
/*     */   public DocFile createFileForOutput(DocPath paramDocPath) {
/*  97 */     return new StandardDocFile(DocumentationTool.Location.DOCUMENTATION_OUTPUT, paramDocPath, null);
/*     */   }
/*     */ 
/*     */   Iterable<DocFile> list(JavaFileManager.Location paramLocation, DocPath paramDocPath)
/*     */   {
/* 102 */     if (paramLocation != StandardLocation.SOURCE_PATH) {
/* 103 */       throw new IllegalArgumentException();
/*     */     }
/* 105 */     LinkedHashSet localLinkedHashSet = new LinkedHashSet();
/* 106 */     if (this.fileManager.hasLocation(paramLocation)) {
/* 107 */       for (Path localPath : this.fileManager.getLocation(paramLocation)) {
/* 108 */         if (Files.isDirectory(localPath, new LinkOption[0])) {
/* 109 */           localPath = localPath.resolve(paramDocPath.getPath());
/* 110 */           if (Files.exists(localPath, new LinkOption[0]))
/* 111 */             localLinkedHashSet.add(new StandardDocFile(localPath, null));
/*     */         }
/*     */       }
/*     */     }
/* 115 */     return localLinkedHashSet;
/*     */   }
/*     */ 
/*     */   class StandardDocFile extends DocFile
/*     */   {
/*     */     private Path file;
/*     */ 
/*     */     private StandardDocFile(Path arg2) {
/* 123 */       super();
/*     */       Object localObject;
/* 124 */       this.file = localObject;
/*     */     }
/*     */ 
/*     */     private StandardDocFile(JavaFileManager.Location paramDocPath, DocPath arg3)
/*     */     {
/* 129 */       super(paramDocPath, localDocPath);
/* 130 */       this.file = PathDocFileFactory.this.destDir.resolve(localDocPath.getPath());
/*     */     }
/*     */ 
/*     */     public InputStream openInputStream() throws IOException
/*     */     {
/* 135 */       JavaFileObject localJavaFileObject = getJavaFileObjectForInput(this.file);
/* 136 */       return new BufferedInputStream(localJavaFileObject.openInputStream());
/*     */     }
/*     */ 
/*     */     public OutputStream openOutputStream()
/*     */       throws IOException, UnsupportedEncodingException
/*     */     {
/* 145 */       if (this.location != DocumentationTool.Location.DOCUMENTATION_OUTPUT) {
/* 146 */         throw new IllegalStateException();
/*     */       }
/* 148 */       OutputStream localOutputStream = getFileObjectForOutput(this.path).openOutputStream();
/* 149 */       return new BufferedOutputStream(localOutputStream);
/*     */     }
/*     */ 
/*     */     public Writer openWriter()
/*     */       throws IOException, UnsupportedEncodingException
/*     */     {
/* 159 */       if (this.location != DocumentationTool.Location.DOCUMENTATION_OUTPUT) {
/* 160 */         throw new IllegalStateException();
/*     */       }
/* 162 */       OutputStream localOutputStream = getFileObjectForOutput(this.path).openOutputStream();
/* 163 */       if (PathDocFileFactory.this.configuration.docencoding == null) {
/* 164 */         return new BufferedWriter(new OutputStreamWriter(localOutputStream));
/*     */       }
/* 166 */       return new BufferedWriter(new OutputStreamWriter(localOutputStream, PathDocFileFactory.this.configuration.docencoding));
/*     */     }
/*     */ 
/*     */     public boolean canRead()
/*     */     {
/* 172 */       return Files.isReadable(this.file);
/*     */     }
/*     */ 
/*     */     public boolean canWrite()
/*     */     {
/* 177 */       return Files.isWritable(this.file);
/*     */     }
/*     */ 
/*     */     public boolean exists()
/*     */     {
/* 182 */       return Files.exists(this.file, new LinkOption[0]);
/*     */     }
/*     */ 
/*     */     public String getName()
/*     */     {
/* 187 */       return this.file.getFileName().toString();
/*     */     }
/*     */ 
/*     */     public String getPath()
/*     */     {
/* 192 */       return this.file.toString();
/*     */     }
/*     */ 
/*     */     public boolean isAbsolute()
/*     */     {
/* 197 */       return this.file.isAbsolute();
/*     */     }
/*     */ 
/*     */     public boolean isDirectory()
/*     */     {
/* 202 */       return Files.isDirectory(this.file, new LinkOption[0]);
/*     */     }
/*     */ 
/*     */     public boolean isFile()
/*     */     {
/* 207 */       return Files.isRegularFile(this.file, new LinkOption[0]);
/*     */     }
/*     */ 
/*     */     public boolean isSameFile(DocFile paramDocFile)
/*     */     {
/* 212 */       if (!(paramDocFile instanceof StandardDocFile))
/* 213 */         return false;
/*     */       try
/*     */       {
/* 216 */         return Files.isSameFile(this.file, ((StandardDocFile)paramDocFile).file); } catch (IOException localIOException) {
/*     */       }
/* 218 */       return false;
/*     */     }
/*     */ 
/*     */     public Iterable<DocFile> list()
/*     */       throws IOException
/*     */     {
/* 224 */       ArrayList localArrayList = new ArrayList();
/* 225 */       DirectoryStream localDirectoryStream = Files.newDirectoryStream(this.file); Object localObject1 = null;
/*     */       try { for (Path localPath : localDirectoryStream)
/* 227 */           localArrayList.add(new StandardDocFile(PathDocFileFactory.this, localPath));
/*     */       }
/*     */       catch (Throwable localThrowable2)
/*     */       {
/* 225 */         localObject1 = localThrowable2; throw localThrowable2;
/*     */       }
/*     */       finally
/*     */       {
/* 229 */         if (localDirectoryStream != null) if (localObject1 != null) try { localDirectoryStream.close(); } catch (Throwable localThrowable3) { localObject1.addSuppressed(localThrowable3); } else localDirectoryStream.close(); 
/*     */       }
/* 230 */       return localArrayList;
/*     */     }
/*     */ 
/*     */     public boolean mkdirs()
/*     */     {
/*     */       try {
/* 236 */         Files.createDirectories(this.file, new FileAttribute[0]);
/* 237 */         return true; } catch (IOException localIOException) {
/*     */       }
/* 239 */       return false;
/*     */     }
/*     */ 
/*     */     public DocFile resolve(DocPath paramDocPath)
/*     */     {
/* 250 */       return resolve(paramDocPath.getPath());
/*     */     }
/*     */ 
/*     */     public DocFile resolve(String paramString)
/*     */     {
/* 260 */       if ((this.location == null) && (this.path == null)) {
/* 261 */         return new StandardDocFile(PathDocFileFactory.this, this.file.resolve(paramString));
/*     */       }
/* 263 */       return new StandardDocFile(PathDocFileFactory.this, this.location, this.path.resolve(paramString));
/*     */     }
/*     */ 
/*     */     public DocFile resolveAgainst(JavaFileManager.Location paramLocation)
/*     */     {
/* 273 */       if (paramLocation != DocumentationTool.Location.DOCUMENTATION_OUTPUT)
/* 274 */         throw new IllegalArgumentException();
/* 275 */       return new StandardDocFile(PathDocFileFactory.this, PathDocFileFactory.this.destDir.resolve(this.file));
/*     */     }
/*     */ 
/*     */     public String toString()
/*     */     {
/* 283 */       StringBuilder localStringBuilder = new StringBuilder();
/* 284 */       localStringBuilder.append("PathDocFile[");
/* 285 */       if (this.location != null)
/* 286 */         localStringBuilder.append("locn:").append(this.location).append(",");
/* 287 */       if (this.path != null)
/* 288 */         localStringBuilder.append("path:").append(this.path.getPath()).append(",");
/* 289 */       localStringBuilder.append("file:").append(this.file);
/* 290 */       localStringBuilder.append("]");
/* 291 */       return localStringBuilder.toString();
/*     */     }
/*     */ 
/*     */     private JavaFileObject getJavaFileObjectForInput(Path paramPath) {
/* 295 */       return (JavaFileObject)PathDocFileFactory.this.fileManager.getJavaFileObjects(new Path[] { paramPath }).iterator().next();
/*     */     }
/*     */ 
/*     */     private FileObject getFileObjectForOutput(DocPath paramDocPath)
/*     */       throws IOException
/*     */     {
/* 303 */       String str1 = paramDocPath.getPath();
/* 304 */       int i = -1;
/* 305 */       for (int j = 0; j < str1.length(); j++) {
/* 306 */         char c = str1.charAt(j);
/* 307 */         if (c == '/')
/* 308 */           i = j;
/* 309 */         else if (((j == i + 1) && (!Character.isJavaIdentifierStart(c))) || 
/* 310 */             (!Character.isJavaIdentifierPart(c)))
/*     */           {
/*     */             break;
/*     */           }
/*     */       }
/* 314 */       String str2 = i == -1 ? "" : str1.substring(0, i);
/* 315 */       String str3 = str1.substring(i + 1);
/* 316 */       return PathDocFileFactory.this.fileManager.getFileForOutput(this.location, str2, str3, null);
/*     */     }
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.doclets.internal.toolkit.util.PathDocFileFactory
 * JD-Core Version:    0.6.2
 */