/*     */ package com.sun.tools.javac.api;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.net.URI;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.List;
/*     */ import java.util.Set;
/*     */ import javax.tools.FileObject;
/*     */ import javax.tools.ForwardingJavaFileManager;
/*     */ import javax.tools.JavaFileManager;
/*     */ import javax.tools.JavaFileManager.Location;
/*     */ import javax.tools.JavaFileObject;
/*     */ import javax.tools.JavaFileObject.Kind;
/*     */ 
/*     */ public class WrappingJavaFileManager<M extends JavaFileManager> extends ForwardingJavaFileManager<M>
/*     */ {
/*     */   protected WrappingJavaFileManager(M paramM)
/*     */   {
/*  63 */     super(paramM);
/*     */   }
/*     */ 
/*     */   protected FileObject wrap(FileObject paramFileObject)
/*     */   {
/*  73 */     return paramFileObject;
/*     */   }
/*     */ 
/*     */   protected JavaFileObject wrap(JavaFileObject paramJavaFileObject)
/*     */   {
/*  85 */     return (JavaFileObject)wrap(paramJavaFileObject);
/*     */   }
/*     */ 
/*     */   protected FileObject unwrap(FileObject paramFileObject)
/*     */   {
/*  95 */     return paramFileObject;
/*     */   }
/*     */ 
/*     */   protected JavaFileObject unwrap(JavaFileObject paramJavaFileObject)
/*     */   {
/* 107 */     return (JavaFileObject)unwrap(paramJavaFileObject);
/*     */   }
/*     */ 
/*     */   protected Iterable<JavaFileObject> wrap(Iterable<JavaFileObject> paramIterable)
/*     */   {
/* 118 */     ArrayList localArrayList = new ArrayList();
/* 119 */     for (JavaFileObject localJavaFileObject : paramIterable)
/* 120 */       localArrayList.add(wrap(localJavaFileObject));
/* 121 */     return Collections.unmodifiableList(localArrayList);
/*     */   }
/*     */ 
/*     */   protected URI unwrap(URI paramURI)
/*     */   {
/* 131 */     return paramURI;
/*     */   }
/*     */ 
/*     */   public Iterable<JavaFileObject> list(JavaFileManager.Location paramLocation, String paramString, Set<JavaFileObject.Kind> paramSet, boolean paramBoolean)
/*     */     throws IOException
/*     */   {
/* 143 */     return wrap(super.list(paramLocation, paramString, paramSet, paramBoolean));
/*     */   }
/*     */ 
/*     */   public String inferBinaryName(JavaFileManager.Location paramLocation, JavaFileObject paramJavaFileObject)
/*     */   {
/* 150 */     return super.inferBinaryName(paramLocation, unwrap(paramJavaFileObject));
/*     */   }
/*     */ 
/*     */   public JavaFileObject getJavaFileForInput(JavaFileManager.Location paramLocation, String paramString, JavaFileObject.Kind paramKind)
/*     */     throws IOException
/*     */   {
/* 163 */     return wrap(super.getJavaFileForInput(paramLocation, paramString, paramKind));
/*     */   }
/*     */ 
/*     */   public JavaFileObject getJavaFileForOutput(JavaFileManager.Location paramLocation, String paramString, JavaFileObject.Kind paramKind, FileObject paramFileObject)
/*     */     throws IOException
/*     */   {
/* 177 */     return wrap(super.getJavaFileForOutput(paramLocation, paramString, paramKind, unwrap(paramFileObject)));
/*     */   }
/*     */ 
/*     */   public FileObject getFileForInput(JavaFileManager.Location paramLocation, String paramString1, String paramString2)
/*     */     throws IOException
/*     */   {
/* 189 */     return wrap(super.getFileForInput(paramLocation, paramString1, paramString2));
/*     */   }
/*     */ 
/*     */   public FileObject getFileForOutput(JavaFileManager.Location paramLocation, String paramString1, String paramString2, FileObject paramFileObject)
/*     */     throws IOException
/*     */   {
/* 202 */     return wrap(super.getFileForOutput(paramLocation, paramString1, paramString2, 
/* 205 */       unwrap(paramFileObject)));
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.javac.api.WrappingJavaFileManager
 * JD-Core Version:    0.6.2
 */