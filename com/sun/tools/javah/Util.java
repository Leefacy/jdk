/*     */ package com.sun.tools.javah;
/*     */ 
/*     */ import java.io.PrintWriter;
/*     */ import java.text.MessageFormat;
/*     */ import java.util.Locale;
/*     */ import java.util.MissingResourceException;
/*     */ import java.util.ResourceBundle;
/*     */ import javax.tools.Diagnostic;
/*     */ import javax.tools.Diagnostic.Kind;
/*     */ import javax.tools.DiagnosticListener;
/*     */ import javax.tools.JavaFileObject;
/*     */ 
/*     */ public class Util
/*     */ {
/*  78 */   public boolean verbose = false;
/*     */   public PrintWriter log;
/*     */   public DiagnosticListener<? super JavaFileObject> dl;
/*     */   private ResourceBundle m;
/*     */ 
/*     */   Util(PrintWriter paramPrintWriter, DiagnosticListener<? super JavaFileObject> paramDiagnosticListener)
/*     */   {
/*  84 */     this.log = paramPrintWriter;
/*  85 */     this.dl = paramDiagnosticListener;
/*     */   }
/*     */ 
/*     */   public void log(String paramString) {
/*  89 */     this.log.println(paramString);
/*     */   }
/*     */ 
/*     */   private void initMessages()
/*     */     throws Util.Exit
/*     */   {
/*     */     try
/*     */     {
/* 100 */       this.m = ResourceBundle.getBundle("com.sun.tools.javah.resources.l10n");
/*     */     } catch (MissingResourceException localMissingResourceException) {
/* 102 */       fatal("Error loading resources.  Please file a bug report.", localMissingResourceException);
/*     */     }
/*     */   }
/*     */ 
/*     */   private String getText(String paramString, Object[] paramArrayOfObject) throws Util.Exit {
/* 107 */     if (this.m == null)
/* 108 */       initMessages();
/*     */     try {
/* 110 */       return MessageFormat.format(this.m.getString(paramString), paramArrayOfObject);
/*     */     } catch (MissingResourceException localMissingResourceException) {
/* 112 */       fatal("Key " + paramString + " not found in resources.", localMissingResourceException);
/*     */     }
/* 114 */     return null;
/*     */   }
/*     */ 
/*     */   public void usage()
/*     */     throws Util.Exit
/*     */   {
/* 121 */     this.log.println(getText("usage", new Object[0]));
/*     */   }
/*     */ 
/*     */   public void version() throws Util.Exit {
/* 125 */     this.log.println(getText("javah.version", new Object[] { 
/* 126 */       System.getProperty("java.version"), 
/* 126 */       null }));
/*     */   }
/*     */ 
/*     */   public void bug(String paramString)
/*     */     throws Util.Exit
/*     */   {
/* 133 */     bug(paramString, null);
/*     */   }
/*     */ 
/*     */   public void bug(String paramString, Exception paramException) throws Util.Exit {
/* 137 */     this.dl.report(createDiagnostic(Diagnostic.Kind.ERROR, paramString, new Object[0]));
/* 138 */     this.dl.report(createDiagnostic(Diagnostic.Kind.NOTE, "bug.report", new Object[0]));
/* 139 */     throw new Exit(11, paramException);
/*     */   }
/*     */ 
/*     */   public void error(String paramString, Object[] paramArrayOfObject) throws Util.Exit {
/* 143 */     this.dl.report(createDiagnostic(Diagnostic.Kind.ERROR, paramString, paramArrayOfObject));
/* 144 */     throw new Exit(15);
/*     */   }
/*     */ 
/*     */   private void fatal(String paramString, Exception paramException) throws Util.Exit {
/* 148 */     this.dl.report(createDiagnostic(Diagnostic.Kind.ERROR, "", new Object[] { paramString }));
/* 149 */     throw new Exit(10, paramException);
/*     */   }
/*     */ 
/*     */   private Diagnostic<JavaFileObject> createDiagnostic(final Diagnostic.Kind paramKind, final String paramString, final Object[] paramArrayOfObject)
/*     */   {
/* 154 */     return new Diagnostic() {
/*     */       public String getCode() {
/* 156 */         return paramString;
/*     */       }
/*     */       public long getColumnNumber() {
/* 159 */         return -1L;
/*     */       }
/*     */       public long getEndPosition() {
/* 162 */         return -1L;
/*     */       }
/*     */       public Diagnostic.Kind getKind() {
/* 165 */         return paramKind;
/*     */       }
/*     */       public long getLineNumber() {
/* 168 */         return -1L;
/*     */       }
/*     */       public String getMessage(Locale paramAnonymousLocale) {
/* 171 */         if (paramString.length() == 0)
/* 172 */           return (String)paramArrayOfObject[0];
/* 173 */         return Util.this.getText(paramString, paramArrayOfObject);
/*     */       }
/*     */       public long getPosition() {
/* 176 */         return -1L;
/*     */       }
/*     */       public JavaFileObject getSource() {
/* 179 */         return null;
/*     */       }
/*     */       public long getStartPosition() {
/* 182 */         return -1L;
/*     */       }
/*     */     };
/*     */   }
/*     */ 
/*     */   public static class Exit extends Error
/*     */   {
/*     */     private static final long serialVersionUID = 430820978114067221L;
/*     */     public final int exitValue;
/*     */     public final Throwable cause;
/*     */ 
/*     */     Exit(int paramInt)
/*     */     {
/*  58 */       this(paramInt, null);
/*     */     }
/*     */ 
/*     */     Exit(int paramInt, Throwable paramThrowable) {
/*  62 */       super();
/*  63 */       this.exitValue = paramInt;
/*  64 */       this.cause = paramThrowable;
/*     */     }
/*     */ 
/*     */     Exit(Exit paramExit) {
/*  68 */       this(paramExit.exitValue, paramExit.cause);
/*     */     }
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.javah.Util
 * JD-Core Version:    0.6.2
 */