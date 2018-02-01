/*     */ package com.sun.tools.javah;
/*     */ 
/*     */ import java.io.Writer;
/*     */ import java.nio.charset.Charset;
/*     */ import java.util.Locale;
/*     */ import java.util.concurrent.Callable;
/*     */ import javax.tools.DiagnosticListener;
/*     */ import javax.tools.JavaFileManager;
/*     */ import javax.tools.JavaFileObject;
/*     */ import javax.tools.OptionChecker;
/*     */ import javax.tools.StandardJavaFileManager;
/*     */ import javax.tools.Tool;
/*     */ 
/*     */ public abstract interface NativeHeaderTool extends Tool, OptionChecker
/*     */ {
/*     */   public abstract NativeHeaderTask getTask(Writer paramWriter, JavaFileManager paramJavaFileManager, DiagnosticListener<? super JavaFileObject> paramDiagnosticListener, Iterable<String> paramIterable1, Iterable<String> paramIterable2);
/*     */ 
/*     */   public abstract StandardJavaFileManager getStandardFileManager(DiagnosticListener<? super JavaFileObject> paramDiagnosticListener, Locale paramLocale, Charset paramCharset);
/*     */ 
/*     */   public static abstract interface NativeHeaderTask extends Callable<Boolean>
/*     */   {
/*     */     public abstract void setLocale(Locale paramLocale);
/*     */ 
/*     */     public abstract Boolean call();
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.javah.NativeHeaderTool
 * JD-Core Version:    0.6.2
 */