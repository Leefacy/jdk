/*     */ package com.sun.tools.javadoc;
/*     */ 
/*     */ import com.sun.javadoc.DocErrorReporter;
/*     */ import com.sun.javadoc.SourcePosition;
/*     */ import com.sun.tools.javac.util.Context;
/*     */ import com.sun.tools.javac.util.Context.Factory;
/*     */ import com.sun.tools.javac.util.JCDiagnostic.DiagnosticType;
/*     */ import com.sun.tools.javac.util.JCDiagnostic.Factory;
/*     */ import com.sun.tools.javac.util.JavacMessages;
/*     */ import com.sun.tools.javac.util.Log;
/*     */ import java.io.PrintWriter;
/*     */ import java.util.Locale;
/*     */ 
/*     */ public class Messager extends Log
/*     */   implements DocErrorReporter
/*     */ {
/*  55 */   public static final SourcePosition NOPOS = null;
/*     */   final String programName;
/*     */   private Locale locale;
/*     */   private final JavacMessages messages;
/*     */   private final JCDiagnostic.Factory javadocDiags;
/* 102 */   static final PrintWriter defaultErrWriter = new PrintWriter(System.err);
/* 103 */   static final PrintWriter defaultWarnWriter = new PrintWriter(System.err);
/* 104 */   static final PrintWriter defaultNoticeWriter = new PrintWriter(System.out);
/*     */ 
/*     */   public static Messager instance0(Context paramContext)
/*     */   {
/*  59 */     Log localLog = (Log)paramContext.get(logKey);
/*  60 */     if ((localLog == null) || (!(localLog instanceof Messager)))
/*  61 */       throw new InternalError("no messager instance!");
/*  62 */     return (Messager)localLog;
/*     */   }
/*     */ 
/*     */   public static void preRegister(Context paramContext, String paramString)
/*     */   {
/*  67 */     paramContext.put(logKey, new Context.Factory() {
/*     */       public Log make(Context paramAnonymousContext) {
/*  69 */         return new Messager(paramAnonymousContext, this.val$programName);
/*     */       }
/*     */     });
/*     */   }
/*     */ 
/*     */   public static void preRegister(Context paramContext, String paramString, final PrintWriter paramPrintWriter1, final PrintWriter paramPrintWriter2, final PrintWriter paramPrintWriter3)
/*     */   {
/*  79 */     paramContext.put(logKey, new Context.Factory() {
/*     */       public Log make(Context paramAnonymousContext) {
/*  81 */         return new Messager(paramAnonymousContext, this.val$programName, paramPrintWriter1, paramPrintWriter2, paramPrintWriter3);
/*     */       }
/*     */     });
/*     */   }
/*     */ 
/*     */   protected Messager(Context paramContext, String paramString)
/*     */   {
/* 111 */     this(paramContext, paramString, defaultErrWriter, defaultWarnWriter, defaultNoticeWriter);
/*     */   }
/*     */ 
/*     */   protected Messager(Context paramContext, String paramString, PrintWriter paramPrintWriter1, PrintWriter paramPrintWriter2, PrintWriter paramPrintWriter3)
/*     */   {
/* 127 */     super(paramContext, paramPrintWriter1, paramPrintWriter2, paramPrintWriter3);
/* 128 */     this.messages = JavacMessages.instance(paramContext);
/* 129 */     this.messages.add("com.sun.tools.javadoc.resources.javadoc");
/* 130 */     this.javadocDiags = new JCDiagnostic.Factory(this.messages, "javadoc");
/* 131 */     this.programName = paramString;
/*     */   }
/*     */ 
/*     */   public void setLocale(Locale paramLocale) {
/* 135 */     this.locale = paramLocale;
/*     */   }
/*     */ 
/*     */   String getText(String paramString, Object[] paramArrayOfObject)
/*     */   {
/* 145 */     return this.messages.getLocalizedString(this.locale, paramString, paramArrayOfObject);
/*     */   }
/*     */ 
/*     */   public void printError(String paramString)
/*     */   {
/* 155 */     printError(null, paramString);
/*     */   }
/*     */ 
/*     */   public void printError(SourcePosition paramSourcePosition, String paramString)
/*     */   {
/* 166 */     if (this.diagListener != null) {
/* 167 */       report(JCDiagnostic.DiagnosticType.ERROR, paramSourcePosition, paramString);
/* 168 */       return;
/*     */     }
/*     */ 
/* 171 */     if (this.nerrors < this.MaxErrors) {
/* 172 */       String str = paramSourcePosition == null ? this.programName : paramSourcePosition.toString();
/* 173 */       this.errWriter.println(str + ": " + getText("javadoc.error", new Object[0]) + " - " + paramString);
/* 174 */       this.errWriter.flush();
/* 175 */       prompt();
/* 176 */       this.nerrors += 1;
/*     */     }
/*     */   }
/*     */ 
/*     */   public void printWarning(String paramString)
/*     */   {
/* 187 */     printWarning(null, paramString);
/*     */   }
/*     */ 
/*     */   public void printWarning(SourcePosition paramSourcePosition, String paramString)
/*     */   {
/* 198 */     if (this.diagListener != null) {
/* 199 */       report(JCDiagnostic.DiagnosticType.WARNING, paramSourcePosition, paramString);
/* 200 */       return;
/*     */     }
/*     */ 
/* 203 */     if (this.nwarnings < this.MaxWarnings) {
/* 204 */       String str = paramSourcePosition == null ? this.programName : paramSourcePosition.toString();
/* 205 */       this.warnWriter.println(str + ": " + getText("javadoc.warning", new Object[0]) + " - " + paramString);
/* 206 */       this.warnWriter.flush();
/* 207 */       this.nwarnings += 1;
/*     */     }
/*     */   }
/*     */ 
/*     */   public void printNotice(String paramString)
/*     */   {
/* 218 */     printNotice(null, paramString);
/*     */   }
/*     */ 
/*     */   public void printNotice(SourcePosition paramSourcePosition, String paramString)
/*     */   {
/* 229 */     if (this.diagListener != null) {
/* 230 */       report(JCDiagnostic.DiagnosticType.NOTE, paramSourcePosition, paramString);
/* 231 */       return;
/*     */     }
/*     */ 
/* 234 */     if (paramSourcePosition == null)
/* 235 */       this.noticeWriter.println(paramString);
/*     */     else
/* 237 */       this.noticeWriter.println(paramSourcePosition + ": " + paramString);
/* 238 */     this.noticeWriter.flush();
/*     */   }
/*     */ 
/*     */   public void error(SourcePosition paramSourcePosition, String paramString, Object[] paramArrayOfObject)
/*     */   {
/* 247 */     printError(paramSourcePosition, getText(paramString, paramArrayOfObject));
/*     */   }
/*     */ 
/*     */   public void warning(SourcePosition paramSourcePosition, String paramString, Object[] paramArrayOfObject)
/*     */   {
/* 256 */     printWarning(paramSourcePosition, getText(paramString, paramArrayOfObject));
/*     */   }
/*     */ 
/*     */   public void notice(String paramString, Object[] paramArrayOfObject)
/*     */   {
/* 265 */     printNotice(getText(paramString, paramArrayOfObject));
/*     */   }
/*     */ 
/*     */   public int nerrors()
/*     */   {
/* 272 */     return this.nerrors;
/*     */   }
/*     */ 
/*     */   public int nwarnings()
/*     */   {
/* 278 */     return this.nwarnings;
/*     */   }
/*     */ 
/*     */   public void exitNotice()
/*     */   {
/* 284 */     if (this.nerrors > 0) {
/* 285 */       notice(this.nerrors > 1 ? "main.errors" : "main.error", new Object[] { "" + this.nerrors });
/*     */     }
/*     */ 
/* 288 */     if (this.nwarnings > 0)
/* 289 */       notice(this.nwarnings > 1 ? "main.warnings" : "main.warning", new Object[] { "" + this.nwarnings });
/*     */   }
/*     */ 
/*     */   public void exit()
/*     */   {
/* 300 */     throw new ExitJavadoc();
/*     */   }
/*     */ 
/*     */   private void report(JCDiagnostic.DiagnosticType paramDiagnosticType, SourcePosition paramSourcePosition, String paramString) {
/* 304 */     switch (3.$SwitchMap$com$sun$tools$javac$util$JCDiagnostic$DiagnosticType[paramDiagnosticType.ordinal()]) {
/*     */     case 1:
/*     */     case 2:
/* 307 */       SourcePosition localSourcePosition = paramSourcePosition == null ? this.programName : paramSourcePosition;
/* 308 */       report(this.javadocDiags.create(paramDiagnosticType, null, null, "msg", new Object[] { localSourcePosition, paramString }));
/* 309 */       break;
/*     */     case 3:
/* 312 */       String str = paramSourcePosition == null ? "msg" : "pos.msg";
/* 313 */       report(this.javadocDiags.create(paramDiagnosticType, null, null, str, new Object[] { paramSourcePosition, paramString }));
/* 314 */       break;
/*     */     default:
/* 317 */       throw new IllegalArgumentException(paramDiagnosticType.toString());
/*     */     }
/*     */   }
/*     */ 
/*     */   public class ExitJavadoc extends Error
/*     */   {
/*     */     private static final long serialVersionUID = 0L;
/*     */ 
/*     */     public ExitJavadoc()
/*     */     {
/*     */     }
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.javadoc.Messager
 * JD-Core Version:    0.6.2
 */