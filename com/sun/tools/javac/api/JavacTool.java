/*     */ package com.sun.tools.javac.api;
/*     */ 
/*     */ import com.sun.source.util.JavacTask;
/*     */ import com.sun.tools.javac.file.JavacFileManager;
/*     */ import com.sun.tools.javac.main.Option;
/*     */ import com.sun.tools.javac.main.OptionHelper.GrumpyHelper;
/*     */ import com.sun.tools.javac.util.ClientCodeException;
/*     */ import com.sun.tools.javac.util.Context;
/*     */ import com.sun.tools.javac.util.Log;
/*     */ import com.sun.tools.javac.util.Log.PrefixKind;
/*     */ import com.sun.tools.javac.util.Options;
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
/*     */ import javax.tools.JavaCompiler;
/*     */ import javax.tools.JavaFileManager;
/*     */ import javax.tools.JavaFileObject;
/*     */ import javax.tools.JavaFileObject.Kind;
/*     */ 
/*     */ public final class JavacTool
/*     */   implements JavaCompiler
/*     */ {
/*     */   public static JavacTool create()
/*     */   {
/*  81 */     return new JavacTool();
/*     */   }
/*     */ 
/*     */   public JavacFileManager getStandardFileManager(DiagnosticListener<? super JavaFileObject> paramDiagnosticListener, Locale paramLocale, Charset paramCharset)
/*     */   {
/*  88 */     Context localContext = new Context();
/*  89 */     localContext.put(Locale.class, paramLocale);
/*  90 */     if (paramDiagnosticListener != null)
/*  91 */       localContext.put(DiagnosticListener.class, paramDiagnosticListener);
/*  92 */     PrintWriter localPrintWriter = paramCharset == null ? new PrintWriter(System.err, true) : new PrintWriter(new OutputStreamWriter(System.err, paramCharset), true);
/*     */ 
/*  95 */     localContext.put(Log.outKey, localPrintWriter);
/*  96 */     return new JavacFileManager(localContext, true, paramCharset);
/*     */   }
/*     */ 
/*     */   public JavacTask getTask(Writer paramWriter, JavaFileManager paramJavaFileManager, DiagnosticListener<? super JavaFileObject> paramDiagnosticListener, Iterable<String> paramIterable1, Iterable<String> paramIterable2, Iterable<? extends JavaFileObject> paramIterable)
/*     */   {
/* 106 */     Context localContext = new Context();
/* 107 */     return getTask(paramWriter, paramJavaFileManager, paramDiagnosticListener, paramIterable1, paramIterable2, paramIterable, localContext);
/*     */   }
/*     */ 
/*     */   public JavacTask getTask(Writer paramWriter, JavaFileManager paramJavaFileManager, DiagnosticListener<? super JavaFileObject> paramDiagnosticListener, Iterable<String> paramIterable1, Iterable<String> paramIterable2, Iterable<? extends JavaFileObject> paramIterable, Context paramContext)
/*     */   {
/*     */     try
/*     */     {
/* 121 */       ClientCodeWrapper localClientCodeWrapper = ClientCodeWrapper.instance(paramContext);
/*     */ 
/* 123 */       if (paramIterable1 != null)
/* 124 */         for (localObject1 = paramIterable1.iterator(); ((Iterator)localObject1).hasNext(); ) { localObject2 = (String)((Iterator)localObject1).next();
/* 125 */           localObject2.getClass();
/*     */         }
/*     */       Object localObject2;
/* 126 */       if (paramIterable2 != null)
/* 127 */         for (localObject1 = paramIterable2.iterator(); ((Iterator)localObject1).hasNext(); ) { localObject2 = (String)((Iterator)localObject1).next();
/* 128 */           if (!SourceVersion.isName((CharSequence)localObject2))
/* 129 */             throw new IllegalArgumentException("Not a valid class name: " + (String)localObject2);
/*     */         }
/* 131 */       if (paramIterable != null) {
/* 132 */         paramIterable = localClientCodeWrapper.wrapJavaFileObjects(paramIterable);
/* 133 */         for (localObject1 = paramIterable.iterator(); ((Iterator)localObject1).hasNext(); ) { localObject2 = (JavaFileObject)((Iterator)localObject1).next();
/* 134 */           if (((JavaFileObject)localObject2).getKind() != JavaFileObject.Kind.SOURCE)
/*     */           {
/* 136 */             String str = "Compilation unit is not of SOURCE kind: \"" + ((JavaFileObject)localObject2)
/* 136 */               .getName() + "\"";
/* 137 */             throw new IllegalArgumentException(str);
/*     */           }
/*     */         }
/*     */       }
/*     */ 
/* 142 */       if (paramDiagnosticListener != null) {
/* 143 */         paramContext.put(DiagnosticListener.class, localClientCodeWrapper.wrap(paramDiagnosticListener));
/*     */       }
/* 145 */       if (paramWriter == null)
/* 146 */         paramContext.put(Log.outKey, new PrintWriter(System.err, true));
/*     */       else {
/* 148 */         paramContext.put(Log.outKey, new PrintWriter(paramWriter, true));
/*     */       }
/* 150 */       if (paramJavaFileManager == null)
/* 151 */         paramJavaFileManager = getStandardFileManager(paramDiagnosticListener, null, null);
/* 152 */       paramJavaFileManager = localClientCodeWrapper.wrap(paramJavaFileManager);
/*     */ 
/* 154 */       paramContext.put(JavaFileManager.class, paramJavaFileManager);
/*     */ 
/* 156 */       processOptions(paramContext, paramJavaFileManager, paramIterable1);
/* 157 */       Object localObject1 = new com.sun.tools.javac.main.Main("javacTask", (PrintWriter)paramContext.get(Log.outKey));
/* 158 */       return new JavacTaskImpl((com.sun.tools.javac.main.Main)localObject1, paramIterable1, paramContext, paramIterable2, paramIterable);
/*     */     } catch (ClientCodeException localClientCodeException) {
/* 160 */       throw new RuntimeException(localClientCodeException.getCause());
/*     */     }
/*     */   }
/*     */ 
/*     */   public static void processOptions(Context paramContext, JavaFileManager paramJavaFileManager, Iterable<String> paramIterable)
/*     */   {
/* 168 */     if (paramIterable == null) {
/* 169 */       return;
/*     */     }
/* 171 */     final Options localOptions = Options.instance(paramContext);
/* 172 */     Log localLog = Log.instance(paramContext);
/*     */ 
/* 175 */     Option[] arrayOfOption = (Option[])Option.getJavacToolOptions().toArray(new Option[0]);
/* 176 */     OptionHelper.GrumpyHelper local1 = new OptionHelper.GrumpyHelper(localLog)
/*     */     {
/*     */       public String get(Option paramAnonymousOption) {
/* 179 */         return localOptions.get(paramAnonymousOption.getText());
/*     */       }
/*     */ 
/*     */       public void put(String paramAnonymousString1, String paramAnonymousString2)
/*     */       {
/* 184 */         localOptions.put(paramAnonymousString1, paramAnonymousString2);
/*     */       }
/*     */ 
/*     */       public void remove(String paramAnonymousString)
/*     */       {
/* 189 */         localOptions.remove(paramAnonymousString);
/*     */       }
/*     */     };
/* 193 */     Iterator localIterator = paramIterable.iterator();
/* 194 */     while (localIterator.hasNext()) {
/* 195 */       String str1 = (String)localIterator.next();
/*     */ 
/* 197 */       for (int i = 0; (i < arrayOfOption.length) && 
/* 198 */         (!arrayOfOption[i].matches(str1)); i++);
/*     */       Object localObject;
/* 201 */       if (i == arrayOfOption.length) {
/* 202 */         if (!paramJavaFileManager.handleOption(str1, localIterator))
/*     */         {
/* 205 */           localObject = localLog.localize(Log.PrefixKind.JAVAC, "err.invalid.flag", new Object[] { str1 });
/* 206 */           throw new IllegalArgumentException((String)localObject);
/*     */         }
/*     */       }
/*     */       else {
/* 210 */         localObject = arrayOfOption[i];
/* 211 */         if (((Option)localObject).hasArg()) {
/* 212 */           if (!localIterator.hasNext()) {
/* 213 */             str2 = localLog.localize(Log.PrefixKind.JAVAC, "err.req.arg", new Object[] { str1 });
/* 214 */             throw new IllegalArgumentException(str2);
/*     */           }
/* 216 */           String str2 = (String)localIterator.next();
/* 217 */           if (((Option)localObject).process(local1, str1, str2))
/*     */           {
/* 220 */             throw new IllegalArgumentException(str1 + " " + str2);
/*     */           }
/* 222 */         } else if (((Option)localObject).process(local1, str1))
/*     */         {
/* 225 */           throw new IllegalArgumentException(str1);
/*     */         }
/*     */       }
/*     */     }
/* 229 */     localOptions.notifyListeners();
/*     */   }
/*     */ 
/*     */   public int run(InputStream paramInputStream, OutputStream paramOutputStream1, OutputStream paramOutputStream2, String[] paramArrayOfString) {
/* 233 */     if (paramOutputStream2 == null)
/* 234 */       paramOutputStream2 = System.err;
/* 235 */     for (String str : paramArrayOfString)
/* 236 */       str.getClass();
/* 237 */     return com.sun.tools.javac.Main.compile(paramArrayOfString, new PrintWriter(paramOutputStream2, true));
/*     */   }
/*     */ 
/*     */   public Set<SourceVersion> getSourceVersions() {
/* 241 */     return Collections.unmodifiableSet(EnumSet.range(SourceVersion.RELEASE_3, 
/* 242 */       SourceVersion.latest()));
/*     */   }
/*     */ 
/*     */   public int isSupportedOption(String paramString) {
/* 246 */     Set localSet = Option.getJavacToolOptions();
/* 247 */     for (Option localOption : localSet) {
/* 248 */       if (localOption.matches(paramString))
/* 249 */         return localOption.hasArg() ? 1 : 0;
/*     */     }
/* 251 */     return -1;
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.javac.api.JavacTool
 * JD-Core Version:    0.6.2
 */