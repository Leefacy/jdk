/*     */ package com.sun.tools.javadoc.api;
/*     */ 
/*     */ import com.sun.tools.javac.api.ClientCodeWrapper;
/*     */ import com.sun.tools.javac.file.JavacFileManager;
/*     */ import com.sun.tools.javac.util.ClientCodeException;
/*     */ import com.sun.tools.javac.util.Context;
/*     */ import com.sun.tools.javac.util.Log;
/*     */ import com.sun.tools.javadoc.Main;
/*     */ import com.sun.tools.javadoc.ToolOption;
/*     */ import java.io.InputStream;
/*     */ import java.io.OutputStream;
/*     */ import java.io.OutputStreamWriter;
/*     */ import java.io.PrintWriter;
/*     */ import java.io.Writer;
/*     */ import java.nio.charset.Charset;
/*     */ import java.util.Collections;
/*     */ import java.util.EnumSet;
/*     */ import java.util.Iterator;
/*     */ import java.util.Locale;
/*     */ import java.util.Set;
/*     */ import javax.lang.model.SourceVersion;
/*     */ import javax.tools.DiagnosticListener;
/*     */ import javax.tools.DocumentationTool;
/*     */ import javax.tools.DocumentationTool.DocumentationTask;
/*     */ import javax.tools.JavaFileManager;
/*     */ import javax.tools.JavaFileObject;
/*     */ import javax.tools.JavaFileObject.Kind;
/*     */ import javax.tools.StandardJavaFileManager;
/*     */ 
/*     */ public class JavadocTool
/*     */   implements DocumentationTool
/*     */ {
/*     */   public DocumentationTool.DocumentationTask getTask(Writer paramWriter, JavaFileManager paramJavaFileManager, DiagnosticListener<? super JavaFileObject> paramDiagnosticListener, Class<?> paramClass, Iterable<String> paramIterable, Iterable<? extends JavaFileObject> paramIterable1)
/*     */   {
/*  71 */     Context localContext = new Context();
/*  72 */     return getTask(paramWriter, paramJavaFileManager, paramDiagnosticListener, paramClass, paramIterable, paramIterable1, localContext);
/*     */   }
/*     */ 
/*     */   public DocumentationTool.DocumentationTask getTask(Writer paramWriter, JavaFileManager paramJavaFileManager, DiagnosticListener<? super JavaFileObject> paramDiagnosticListener, Class<?> paramClass, Iterable<String> paramIterable, Iterable<? extends JavaFileObject> paramIterable1, Context paramContext)
/*     */   {
/*     */     try
/*     */     {
/*  85 */       ClientCodeWrapper localClientCodeWrapper = ClientCodeWrapper.instance(paramContext);
/*     */       Iterator localIterator;
/*  87 */       if (paramIterable != null)
/*  88 */         for (localIterator = paramIterable.iterator(); localIterator.hasNext(); ) { localObject = (String)localIterator.next();
/*  89 */           localObject.getClass();
/*     */         }
/*     */       Object localObject;
/*  92 */       if (paramIterable1 != null) {
/*  93 */         paramIterable1 = localClientCodeWrapper.wrapJavaFileObjects(paramIterable1);
/*  94 */         for (localIterator = paramIterable1.iterator(); localIterator.hasNext(); ) { localObject = (JavaFileObject)localIterator.next();
/*  95 */           if (((JavaFileObject)localObject).getKind() != JavaFileObject.Kind.SOURCE)
/*     */           {
/*  97 */             throw new IllegalArgumentException("All compilation units must be of SOURCE kind");
/*     */           }
/*     */         }
/*     */       }
/*     */ 
/* 102 */       if (paramDiagnosticListener != null) {
/* 103 */         paramContext.put(DiagnosticListener.class, localClientCodeWrapper.wrap(paramDiagnosticListener));
/*     */       }
/* 105 */       if (paramWriter == null)
/* 106 */         paramContext.put(Log.outKey, new PrintWriter(System.err, true));
/* 107 */       else if ((paramWriter instanceof PrintWriter))
/* 108 */         paramContext.put(Log.outKey, (PrintWriter)paramWriter);
/*     */       else {
/* 110 */         paramContext.put(Log.outKey, new PrintWriter(paramWriter, true));
/*     */       }
/* 112 */       if (paramJavaFileManager == null)
/* 113 */         paramJavaFileManager = getStandardFileManager(paramDiagnosticListener, null, null);
/* 114 */       paramJavaFileManager = localClientCodeWrapper.wrap(paramJavaFileManager);
/* 115 */       paramContext.put(JavaFileManager.class, paramJavaFileManager);
/*     */ 
/* 117 */       return new JavadocTaskImpl(paramContext, paramClass, paramIterable, paramIterable1);
/*     */     } catch (ClientCodeException localClientCodeException) {
/* 119 */       throw new RuntimeException(localClientCodeException.getCause());
/*     */     }
/*     */   }
/*     */ 
/*     */   public StandardJavaFileManager getStandardFileManager(DiagnosticListener<? super JavaFileObject> paramDiagnosticListener, Locale paramLocale, Charset paramCharset)
/*     */   {
/* 129 */     Context localContext = new Context();
/* 130 */     localContext.put(Locale.class, paramLocale);
/* 131 */     if (paramDiagnosticListener != null)
/* 132 */       localContext.put(DiagnosticListener.class, paramDiagnosticListener);
/* 133 */     PrintWriter localPrintWriter = paramCharset == null ? new PrintWriter(System.err, true) : new PrintWriter(new OutputStreamWriter(System.err, paramCharset), true);
/*     */ 
/* 136 */     localContext.put(Log.outKey, localPrintWriter);
/* 137 */     return new JavacFileManager(localContext, true, paramCharset);
/*     */   }
/*     */ 
/*     */   public int run(InputStream paramInputStream, OutputStream paramOutputStream1, OutputStream paramOutputStream2, String[] paramArrayOfString)
/*     */   {
/* 142 */     PrintWriter localPrintWriter1 = new PrintWriter(paramOutputStream2 == null ? System.err : paramOutputStream2, true);
/* 143 */     PrintWriter localPrintWriter2 = new PrintWriter(paramOutputStream1 == null ? System.out : paramOutputStream1);
/*     */     try {
/* 145 */       String str = "com.sun.tools.doclets.standard.Standard";
/* 146 */       ClassLoader localClassLoader = getClass().getClassLoader();
/* 147 */       return Main.execute("javadoc", localPrintWriter1, localPrintWriter1, localPrintWriter2, str, localClassLoader, paramArrayOfString);
/*     */     }
/*     */     finally {
/* 150 */       localPrintWriter1.flush();
/* 151 */       localPrintWriter2.flush();
/*     */     }
/*     */   }
/*     */ 
/*     */   public Set<SourceVersion> getSourceVersions()
/*     */   {
/* 157 */     return Collections.unmodifiableSet(
/* 158 */       EnumSet.range(SourceVersion.RELEASE_3, 
/* 158 */       SourceVersion.latest()));
/*     */   }
/*     */ 
/*     */   public int isSupportedOption(String paramString)
/*     */   {
/* 163 */     if (paramString == null)
/* 164 */       throw new NullPointerException();
/* 165 */     for (ToolOption localToolOption : ToolOption.values()) {
/* 166 */       if (localToolOption.opt.equals(paramString))
/* 167 */         return localToolOption.hasArg ? 1 : 0;
/*     */     }
/* 169 */     return -1;
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.javadoc.api.JavadocTool
 * JD-Core Version:    0.6.2
 */