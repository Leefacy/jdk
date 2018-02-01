/*     */ package com.sun.tools.doclets.internal.toolkit.util;
/*     */ 
/*     */ import com.sun.tools.doclets.internal.toolkit.Configuration;
/*     */ import com.sun.tools.javac.util.Assert;
/*     */ import java.io.BufferedInputStream;
/*     */ import java.io.BufferedOutputStream;
/*     */ import java.io.BufferedWriter;
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.OutputStream;
/*     */ import java.io.OutputStreamWriter;
/*     */ import java.io.UnsupportedEncodingException;
/*     */ import java.io.Writer;
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
/*     */ import javax.tools.StandardJavaFileManager;
/*     */ import javax.tools.StandardLocation;
/*     */ 
/*     */ class StandardDocFileFactory extends DocFileFactory
/*     */ {
/*     */   private final StandardJavaFileManager fileManager;
/*     */   private File destDir;
/*     */ 
/*     */   public StandardDocFileFactory(Configuration paramConfiguration)
/*     */   {
/*  69 */     super(paramConfiguration);
/*  70 */     this.fileManager = ((StandardJavaFileManager)paramConfiguration.getFileManager());
/*     */   }
/*     */ 
/*     */   private File getDestDir() {
/*  74 */     if (this.destDir == null) {
/*  75 */       if ((!this.configuration.destDirName.isEmpty()) || 
/*  76 */         (!this.fileManager
/*  76 */         .hasLocation(DocumentationTool.Location.DOCUMENTATION_OUTPUT))) {
/*     */         try
/*     */         {
/*  78 */           String str = this.configuration.destDirName.isEmpty() ? "." : this.configuration.destDirName;
/*  79 */           File localFile = new File(str);
/*  80 */           this.fileManager.setLocation(DocumentationTool.Location.DOCUMENTATION_OUTPUT, Arrays.asList(new File[] { localFile }));
/*     */         } catch (IOException localIOException) {
/*  82 */           throw new DocletAbortException(localIOException);
/*     */         }
/*     */       }
/*     */ 
/*  86 */       this.destDir = ((File)this.fileManager.getLocation(DocumentationTool.Location.DOCUMENTATION_OUTPUT).iterator().next());
/*     */     }
/*  88 */     return this.destDir;
/*     */   }
/*     */ 
/*     */   public DocFile createFileForDirectory(String paramString) {
/*  92 */     return new StandardDocFile(new File(paramString), null);
/*     */   }
/*     */ 
/*     */   public DocFile createFileForInput(String paramString) {
/*  96 */     return new StandardDocFile(new File(paramString), null);
/*     */   }
/*     */ 
/*     */   public DocFile createFileForOutput(DocPath paramDocPath) {
/* 100 */     return new StandardDocFile(DocumentationTool.Location.DOCUMENTATION_OUTPUT, paramDocPath, null);
/*     */   }
/*     */ 
/*     */   Iterable<DocFile> list(JavaFileManager.Location paramLocation, DocPath paramDocPath)
/*     */   {
/* 105 */     if (paramLocation != StandardLocation.SOURCE_PATH) {
/* 106 */       throw new IllegalArgumentException();
/*     */     }
/* 108 */     LinkedHashSet localLinkedHashSet = new LinkedHashSet();
/* 109 */     StandardLocation localStandardLocation = this.fileManager.hasLocation(StandardLocation.SOURCE_PATH) ? StandardLocation.SOURCE_PATH : StandardLocation.CLASS_PATH;
/*     */ 
/* 111 */     for (File localFile : this.fileManager.getLocation(localStandardLocation)) {
/* 112 */       if (localFile.isDirectory()) {
/* 113 */         localFile = new File(localFile, paramDocPath.getPath());
/* 114 */         if (localFile.exists())
/* 115 */           localLinkedHashSet.add(new StandardDocFile(localFile, null));
/*     */       }
/*     */     }
/* 118 */     return localLinkedHashSet;
/*     */   }
/*     */ 
/*     */   private static File newFile(File paramFile, String paramString) {
/* 122 */     return paramFile == null ? new File(paramString) : new File(paramFile, paramString);
/*     */   }
/*     */ 
/*     */   class StandardDocFile extends DocFile
/*     */   {
/*     */     private File file;
/*     */ 
/*     */     private StandardDocFile(File arg2)
/*     */     {
/* 131 */       super();
/*     */       Object localObject;
/* 132 */       this.file = localObject;
/*     */     }
/*     */ 
/*     */     private StandardDocFile(JavaFileManager.Location paramDocPath, DocPath arg3)
/*     */     {
/* 137 */       super(paramDocPath, localDocPath);
/* 138 */       Assert.check(paramDocPath == DocumentationTool.Location.DOCUMENTATION_OUTPUT);
/* 139 */       this.file = StandardDocFileFactory.newFile(StandardDocFileFactory.access$200(StandardDocFileFactory.this), localDocPath.getPath());
/*     */     }
/*     */ 
/*     */     public InputStream openInputStream() throws IOException
/*     */     {
/* 144 */       JavaFileObject localJavaFileObject = getJavaFileObjectForInput(this.file);
/* 145 */       return new BufferedInputStream(localJavaFileObject.openInputStream());
/*     */     }
/*     */ 
/*     */     public OutputStream openOutputStream()
/*     */       throws IOException, UnsupportedEncodingException
/*     */     {
/* 154 */       if (this.location != DocumentationTool.Location.DOCUMENTATION_OUTPUT) {
/* 155 */         throw new IllegalStateException();
/*     */       }
/* 157 */       OutputStream localOutputStream = getFileObjectForOutput(this.path).openOutputStream();
/* 158 */       return new BufferedOutputStream(localOutputStream);
/*     */     }
/*     */ 
/*     */     public Writer openWriter()
/*     */       throws IOException, UnsupportedEncodingException
/*     */     {
/* 168 */       if (this.location != DocumentationTool.Location.DOCUMENTATION_OUTPUT) {
/* 169 */         throw new IllegalStateException();
/*     */       }
/* 171 */       OutputStream localOutputStream = getFileObjectForOutput(this.path).openOutputStream();
/* 172 */       if (StandardDocFileFactory.this.configuration.docencoding == null) {
/* 173 */         return new BufferedWriter(new OutputStreamWriter(localOutputStream));
/*     */       }
/* 175 */       return new BufferedWriter(new OutputStreamWriter(localOutputStream, StandardDocFileFactory.this.configuration.docencoding));
/*     */     }
/*     */ 
/*     */     public boolean canRead()
/*     */     {
/* 181 */       return this.file.canRead();
/*     */     }
/*     */ 
/*     */     public boolean canWrite()
/*     */     {
/* 186 */       return this.file.canWrite();
/*     */     }
/*     */ 
/*     */     public boolean exists()
/*     */     {
/* 191 */       return this.file.exists();
/*     */     }
/*     */ 
/*     */     public String getName()
/*     */     {
/* 196 */       return this.file.getName();
/*     */     }
/*     */ 
/*     */     public String getPath()
/*     */     {
/* 201 */       return this.file.getPath();
/*     */     }
/*     */ 
/*     */     public boolean isAbsolute()
/*     */     {
/* 206 */       return this.file.isAbsolute();
/*     */     }
/*     */ 
/*     */     public boolean isDirectory()
/*     */     {
/* 211 */       return this.file.isDirectory();
/*     */     }
/*     */ 
/*     */     public boolean isFile()
/*     */     {
/* 216 */       return this.file.isFile();
/*     */     }
/*     */ 
/*     */     public boolean isSameFile(DocFile paramDocFile)
/*     */     {
/* 221 */       if (!(paramDocFile instanceof StandardDocFile)) {
/* 222 */         return false;
/*     */       }
/*     */       try
/*     */       {
/* 226 */         return (this.file.exists()) && 
/* 226 */           (this.file
/* 226 */           .getCanonicalFile().equals(((StandardDocFile)paramDocFile).file.getCanonicalFile())); } catch (IOException localIOException) {
/*     */       }
/* 228 */       return false;
/*     */     }
/*     */ 
/*     */     public Iterable<DocFile> list()
/*     */     {
/* 234 */       ArrayList localArrayList = new ArrayList();
/* 235 */       for (File localFile : this.file.listFiles()) {
/* 236 */         localArrayList.add(new StandardDocFile(StandardDocFileFactory.this, localFile));
/*     */       }
/* 238 */       return localArrayList;
/*     */     }
/*     */ 
/*     */     public boolean mkdirs()
/*     */     {
/* 243 */       return this.file.mkdirs();
/*     */     }
/*     */ 
/*     */     public DocFile resolve(DocPath paramDocPath)
/*     */     {
/* 253 */       return resolve(paramDocPath.getPath());
/*     */     }
/*     */ 
/*     */     public DocFile resolve(String paramString)
/*     */     {
/* 263 */       if ((this.location == null) && (this.path == null)) {
/* 264 */         return new StandardDocFile(StandardDocFileFactory.this, new File(this.file, paramString));
/*     */       }
/* 266 */       return new StandardDocFile(StandardDocFileFactory.this, this.location, this.path.resolve(paramString));
/*     */     }
/*     */ 
/*     */     public DocFile resolveAgainst(JavaFileManager.Location paramLocation)
/*     */     {
/* 276 */       if (paramLocation != DocumentationTool.Location.DOCUMENTATION_OUTPUT)
/* 277 */         throw new IllegalArgumentException();
/* 278 */       return new StandardDocFile(StandardDocFileFactory.this, StandardDocFileFactory.newFile(StandardDocFileFactory.access$200(StandardDocFileFactory.this), this.file.getPath()));
/*     */     }
/*     */ 
/*     */     public String toString()
/*     */     {
/* 286 */       StringBuilder localStringBuilder = new StringBuilder();
/* 287 */       localStringBuilder.append("StandardDocFile[");
/* 288 */       if (this.location != null)
/* 289 */         localStringBuilder.append("locn:").append(this.location).append(",");
/* 290 */       if (this.path != null)
/* 291 */         localStringBuilder.append("path:").append(this.path.getPath()).append(",");
/* 292 */       localStringBuilder.append("file:").append(this.file);
/* 293 */       localStringBuilder.append("]");
/* 294 */       return localStringBuilder.toString();
/*     */     }
/*     */ 
/*     */     private JavaFileObject getJavaFileObjectForInput(File paramFile) {
/* 298 */       return (JavaFileObject)StandardDocFileFactory.this.fileManager.getJavaFileObjects(new File[] { paramFile }).iterator().next();
/*     */     }
/*     */ 
/*     */     private FileObject getFileObjectForOutput(DocPath paramDocPath)
/*     */       throws IOException
/*     */     {
/* 306 */       String str1 = paramDocPath.getPath();
/* 307 */       int i = -1;
/* 308 */       for (int j = 0; j < str1.length(); j++) {
/* 309 */         char c = str1.charAt(j);
/* 310 */         if (c == '/')
/* 311 */           i = j;
/* 312 */         else if (((j == i + 1) && (!Character.isJavaIdentifierStart(c))) || 
/* 313 */             (!Character.isJavaIdentifierPart(c)))
/*     */           {
/*     */             break;
/*     */           }
/*     */       }
/* 317 */       String str2 = i == -1 ? "" : str1.substring(0, i);
/* 318 */       String str3 = str1.substring(i + 1);
/* 319 */       return StandardDocFileFactory.this.fileManager.getFileForOutput(this.location, str2, str3, null);
/*     */     }
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.doclets.internal.toolkit.util.StandardDocFileFactory
 * JD-Core Version:    0.6.2
 */