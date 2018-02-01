/*     */ package com.sun.tools.doclets.internal.toolkit.util;
/*     */ 
/*     */ import com.sun.tools.doclets.internal.toolkit.Configuration;
/*     */ import java.io.BufferedReader;
/*     */ import java.io.BufferedWriter;
/*     */ import java.io.FileNotFoundException;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.InputStreamReader;
/*     */ import java.io.OutputStream;
/*     */ import java.io.OutputStreamWriter;
/*     */ import java.io.UnsupportedEncodingException;
/*     */ import java.io.Writer;
/*     */ import javax.tools.JavaFileManager.Location;
/*     */ 
/*     */ public abstract class DocFile
/*     */ {
/*     */   private final Configuration configuration;
/*     */   protected final JavaFileManager.Location location;
/*     */   protected final DocPath path;
/*     */ 
/*     */   public static DocFile createFileForDirectory(Configuration paramConfiguration, String paramString)
/*     */   {
/*  59 */     return DocFileFactory.getFactory(paramConfiguration).createFileForDirectory(paramString);
/*     */   }
/*     */ 
/*     */   public static DocFile createFileForInput(Configuration paramConfiguration, String paramString)
/*     */   {
/*  64 */     return DocFileFactory.getFactory(paramConfiguration).createFileForInput(paramString);
/*     */   }
/*     */ 
/*     */   public static DocFile createFileForOutput(Configuration paramConfiguration, DocPath paramDocPath)
/*     */   {
/*  69 */     return DocFileFactory.getFactory(paramConfiguration).createFileForOutput(paramDocPath);
/*     */   }
/*     */ 
/*     */   public static Iterable<DocFile> list(Configuration paramConfiguration, JavaFileManager.Location paramLocation, DocPath paramDocPath)
/*     */   {
/*  95 */     return DocFileFactory.getFactory(paramConfiguration).list(paramLocation, paramDocPath);
/*     */   }
/*     */ 
/*     */   protected DocFile(Configuration paramConfiguration)
/*     */   {
/* 100 */     this.configuration = paramConfiguration;
/* 101 */     this.location = null;
/* 102 */     this.path = null;
/*     */   }
/*     */ 
/*     */   protected DocFile(Configuration paramConfiguration, JavaFileManager.Location paramLocation, DocPath paramDocPath)
/*     */   {
/* 107 */     this.configuration = paramConfiguration;
/* 108 */     this.location = paramLocation;
/* 109 */     this.path = paramDocPath;
/*     */   }
/*     */ 
/*     */   public abstract InputStream openInputStream()
/*     */     throws IOException;
/*     */ 
/*     */   public abstract OutputStream openOutputStream()
/*     */     throws IOException, UnsupportedEncodingException;
/*     */ 
/*     */   public abstract Writer openWriter()
/*     */     throws IOException, UnsupportedEncodingException;
/*     */ 
/*     */   public void copyFile(DocFile paramDocFile)
/*     */     throws IOException
/*     */   {
/* 135 */     InputStream localInputStream = paramDocFile.openInputStream();
/* 136 */     OutputStream localOutputStream = openOutputStream();
/*     */     try {
/* 138 */       byte[] arrayOfByte = new byte[1024];
/*     */       int i;
/* 140 */       while ((i = localInputStream.read(arrayOfByte)) != -1)
/* 141 */         localOutputStream.write(arrayOfByte, 0, i);
/*     */     } catch (FileNotFoundException localFileNotFoundException) {
/*     */     } catch (SecurityException localSecurityException) {
/*     */     }
/*     */     finally {
/* 146 */       localInputStream.close();
/* 147 */       localOutputStream.close();
/*     */     }
/*     */   }
/*     */ 
/*     */   public void copyResource(DocPath paramDocPath, boolean paramBoolean1, boolean paramBoolean2)
/*     */   {
/* 160 */     if ((exists()) && (!paramBoolean1))
/* 161 */       return;
/*     */     try
/*     */     {
/* 164 */       InputStream localInputStream = Configuration.class.getResourceAsStream(paramDocPath.getPath());
/* 165 */       if (localInputStream == null) {
/* 166 */         return;
/*     */       }
/* 168 */       OutputStream localOutputStream = openOutputStream();
/*     */       try
/*     */       {
/*     */         Object localObject1;
/* 170 */         if (!paramBoolean2) {
/* 171 */           localObject1 = new byte[2048];
/*     */           int i;
/* 173 */           while ((i = localInputStream.read((byte[])localObject1)) > 0) localOutputStream.write((byte[])localObject1, 0, i); 
/*     */         }
/* 175 */         else { localObject1 = new BufferedReader(new InputStreamReader(localInputStream));
/*     */           BufferedWriter localBufferedWriter;
/* 177 */           if (this.configuration.docencoding == null)
/* 178 */             localBufferedWriter = new BufferedWriter(new OutputStreamWriter(localOutputStream));
/*     */           else
/* 180 */             localBufferedWriter = new BufferedWriter(new OutputStreamWriter(localOutputStream, this.configuration.docencoding));
/*     */           try
/*     */           {
/*     */             String str;
/* 185 */             while ((str = ((BufferedReader)localObject1).readLine()) != null) {
/* 186 */               localBufferedWriter.write(str);
/* 187 */               localBufferedWriter.write(DocletConstants.NL);
/*     */             }
/*     */           } finally {
/* 190 */             ((BufferedReader)localObject1).close();
/* 191 */             localBufferedWriter.close();
/*     */           } }
/*     */       }
/*     */       finally {
/* 195 */         localInputStream.close();
/* 196 */         localOutputStream.close();
/*     */       }
/*     */     } catch (IOException localIOException) {
/* 199 */       localIOException.printStackTrace(System.err);
/* 200 */       throw new DocletAbortException(localIOException);
/*     */     }
/*     */   }
/*     */ 
/*     */   public abstract boolean canRead();
/*     */ 
/*     */   public abstract boolean canWrite();
/*     */ 
/*     */   public abstract boolean exists();
/*     */ 
/*     */   public abstract String getName();
/*     */ 
/*     */   public abstract String getPath();
/*     */ 
/*     */   public abstract boolean isAbsolute();
/*     */ 
/*     */   public abstract boolean isDirectory();
/*     */ 
/*     */   public abstract boolean isFile();
/*     */ 
/*     */   public abstract boolean isSameFile(DocFile paramDocFile);
/*     */ 
/*     */   public abstract Iterable<DocFile> list()
/*     */     throws IOException;
/*     */ 
/*     */   public abstract boolean mkdirs();
/*     */ 
/*     */   public abstract DocFile resolve(DocPath paramDocPath);
/*     */ 
/*     */   public abstract DocFile resolve(String paramString);
/*     */ 
/*     */   public abstract DocFile resolveAgainst(JavaFileManager.Location paramLocation);
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.doclets.internal.toolkit.util.DocFile
 * JD-Core Version:    0.6.2
 */