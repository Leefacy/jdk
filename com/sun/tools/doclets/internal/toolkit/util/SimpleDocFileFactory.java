/*     */ package com.sun.tools.doclets.internal.toolkit.util;
/*     */ 
/*     */ import com.sun.tools.doclets.internal.toolkit.Configuration;
/*     */ import java.io.BufferedInputStream;
/*     */ import java.io.BufferedOutputStream;
/*     */ import java.io.BufferedWriter;
/*     */ import java.io.File;
/*     */ import java.io.FileInputStream;
/*     */ import java.io.FileNotFoundException;
/*     */ import java.io.FileOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.OutputStream;
/*     */ import java.io.OutputStreamWriter;
/*     */ import java.io.UnsupportedEncodingException;
/*     */ import java.io.Writer;
/*     */ import java.util.ArrayList;
/*     */ import java.util.LinkedHashSet;
/*     */ import java.util.List;
/*     */ import java.util.Set;
/*     */ import javax.tools.DocumentationTool.Location;
/*     */ import javax.tools.JavaFileManager.Location;
/*     */ import javax.tools.StandardLocation;
/*     */ 
/*     */ class SimpleDocFileFactory extends DocFileFactory
/*     */ {
/*     */   public SimpleDocFileFactory(Configuration paramConfiguration)
/*     */   {
/*  66 */     super(paramConfiguration);
/*     */   }
/*     */ 
/*     */   public DocFile createFileForDirectory(String paramString) {
/*  70 */     return new SimpleDocFile(new File(paramString), null);
/*     */   }
/*     */ 
/*     */   public DocFile createFileForInput(String paramString) {
/*  74 */     return new SimpleDocFile(new File(paramString), null);
/*     */   }
/*     */ 
/*     */   public DocFile createFileForOutput(DocPath paramDocPath) {
/*  78 */     return new SimpleDocFile(DocumentationTool.Location.DOCUMENTATION_OUTPUT, paramDocPath, null);
/*     */   }
/*     */ 
/*     */   Iterable<DocFile> list(JavaFileManager.Location paramLocation, DocPath paramDocPath)
/*     */   {
/*  83 */     if (paramLocation != StandardLocation.SOURCE_PATH) {
/*  84 */       throw new IllegalArgumentException();
/*     */     }
/*  86 */     LinkedHashSet localLinkedHashSet = new LinkedHashSet();
/*  87 */     for (String str : this.configuration.sourcepath.split(File.pathSeparator))
/*  88 */       if (!str.isEmpty())
/*     */       {
/*  90 */         File localFile = new File(str);
/*  91 */         if (localFile.isDirectory()) {
/*  92 */           localFile = new File(localFile, paramDocPath.getPath());
/*  93 */           if (localFile.exists())
/*  94 */             localLinkedHashSet.add(new SimpleDocFile(localFile, null));
/*     */         }
/*     */       }
/*  97 */     return localLinkedHashSet;
/*     */   }
/*     */ 
/*     */   class SimpleDocFile extends DocFile
/*     */   {
/*     */     private File file;
/*     */ 
/*     */     private SimpleDocFile(File arg2) {
/* 105 */       super();
/*     */       Object localObject;
/* 106 */       this.file = localObject;
/*     */     }
/*     */ 
/*     */     private SimpleDocFile(JavaFileManager.Location paramDocPath, DocPath arg3)
/*     */     {
/* 111 */       super(paramDocPath, localDocPath);
/* 112 */       String str = SimpleDocFileFactory.this.configuration.destDirName;
/* 113 */       this.file = (str.isEmpty() ? new File(localDocPath.getPath()) : new File(str, localDocPath
/* 114 */         .getPath()));
/*     */     }
/*     */ 
/*     */     public InputStream openInputStream() throws FileNotFoundException
/*     */     {
/* 119 */       return new BufferedInputStream(new FileInputStream(this.file));
/*     */     }
/*     */ 
/*     */     public OutputStream openOutputStream()
/*     */       throws IOException, UnsupportedEncodingException
/*     */     {
/* 128 */       if (this.location != DocumentationTool.Location.DOCUMENTATION_OUTPUT) {
/* 129 */         throw new IllegalStateException();
/*     */       }
/* 131 */       createDirectoryForFile(this.file);
/* 132 */       return new BufferedOutputStream(new FileOutputStream(this.file));
/*     */     }
/*     */ 
/*     */     public Writer openWriter()
/*     */       throws IOException, UnsupportedEncodingException
/*     */     {
/* 142 */       if (this.location != DocumentationTool.Location.DOCUMENTATION_OUTPUT) {
/* 143 */         throw new IllegalStateException();
/*     */       }
/* 145 */       createDirectoryForFile(this.file);
/* 146 */       FileOutputStream localFileOutputStream = new FileOutputStream(this.file);
/* 147 */       if (SimpleDocFileFactory.this.configuration.docencoding == null) {
/* 148 */         return new BufferedWriter(new OutputStreamWriter(localFileOutputStream));
/*     */       }
/* 150 */       return new BufferedWriter(new OutputStreamWriter(localFileOutputStream, SimpleDocFileFactory.this.configuration.docencoding));
/*     */     }
/*     */ 
/*     */     public boolean canRead()
/*     */     {
/* 156 */       return this.file.canRead();
/*     */     }
/*     */ 
/*     */     public boolean canWrite()
/*     */     {
/* 161 */       return this.file.canRead();
/*     */     }
/*     */ 
/*     */     public boolean exists()
/*     */     {
/* 166 */       return this.file.exists();
/*     */     }
/*     */ 
/*     */     public String getName()
/*     */     {
/* 171 */       return this.file.getName();
/*     */     }
/*     */ 
/*     */     public String getPath()
/*     */     {
/* 176 */       return this.file.getPath();
/*     */     }
/*     */ 
/*     */     public boolean isAbsolute()
/*     */     {
/* 181 */       return this.file.isAbsolute();
/*     */     }
/*     */ 
/*     */     public boolean isDirectory()
/*     */     {
/* 186 */       return this.file.isDirectory();
/*     */     }
/*     */ 
/*     */     public boolean isFile()
/*     */     {
/* 191 */       return this.file.isFile();
/*     */     }
/*     */ 
/*     */     public boolean isSameFile(DocFile paramDocFile)
/*     */     {
/* 196 */       if (!(paramDocFile instanceof SimpleDocFile)) {
/* 197 */         return false;
/*     */       }
/*     */       try
/*     */       {
/* 201 */         return (this.file.exists()) && 
/* 201 */           (this.file
/* 201 */           .getCanonicalFile().equals(((SimpleDocFile)paramDocFile).file.getCanonicalFile())); } catch (IOException localIOException) {
/*     */       }
/* 203 */       return false;
/*     */     }
/*     */ 
/*     */     public Iterable<DocFile> list()
/*     */     {
/* 209 */       ArrayList localArrayList = new ArrayList();
/* 210 */       for (File localFile : this.file.listFiles()) {
/* 211 */         localArrayList.add(new SimpleDocFile(SimpleDocFileFactory.this, localFile));
/*     */       }
/* 213 */       return localArrayList;
/*     */     }
/*     */ 
/*     */     public boolean mkdirs()
/*     */     {
/* 218 */       return this.file.mkdirs();
/*     */     }
/*     */ 
/*     */     public DocFile resolve(DocPath paramDocPath)
/*     */     {
/* 228 */       return resolve(paramDocPath.getPath());
/*     */     }
/*     */ 
/*     */     public DocFile resolve(String paramString)
/*     */     {
/* 238 */       if ((this.location == null) && (this.path == null)) {
/* 239 */         return new SimpleDocFile(SimpleDocFileFactory.this, new File(this.file, paramString));
/*     */       }
/* 241 */       return new SimpleDocFile(SimpleDocFileFactory.this, this.location, this.path.resolve(paramString));
/*     */     }
/*     */ 
/*     */     public DocFile resolveAgainst(JavaFileManager.Location paramLocation)
/*     */     {
/* 251 */       if (paramLocation != DocumentationTool.Location.DOCUMENTATION_OUTPUT) {
/* 252 */         throw new IllegalArgumentException();
/*     */       }
/* 254 */       return new SimpleDocFile(SimpleDocFileFactory.this, new File(SimpleDocFileFactory.this.configuration.destDirName, this.file
/* 254 */         .getPath()));
/*     */     }
/*     */ 
/*     */     private void createDirectoryForFile(File paramFile)
/*     */     {
/* 266 */       File localFile = paramFile.getParentFile();
/* 267 */       if ((localFile == null) || (localFile.exists()) || (localFile.mkdirs())) {
/* 268 */         return;
/*     */       }
/* 270 */       SimpleDocFileFactory.this.configuration.message.error("doclet.Unable_to_create_directory_0", new Object[] { localFile
/* 271 */         .getPath() });
/* 272 */       throw new DocletAbortException("can't create directory");
/*     */     }
/*     */ 
/*     */     public String toString()
/*     */     {
/* 280 */       StringBuilder localStringBuilder = new StringBuilder();
/* 281 */       localStringBuilder.append("DocFile[");
/* 282 */       if (this.location != null)
/* 283 */         localStringBuilder.append("locn:").append(this.location).append(",");
/* 284 */       if (this.path != null)
/* 285 */         localStringBuilder.append("path:").append(this.path.getPath()).append(",");
/* 286 */       localStringBuilder.append("file:").append(this.file);
/* 287 */       localStringBuilder.append("]");
/* 288 */       return localStringBuilder.toString();
/*     */     }
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.doclets.internal.toolkit.util.SimpleDocFileFactory
 * JD-Core Version:    0.6.2
 */