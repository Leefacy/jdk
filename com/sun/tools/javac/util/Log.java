/*     */ package com.sun.tools.javac.util;
/*     */ 
/*     */ import com.sun.tools.javac.api.DiagnosticFormatter;
/*     */ import com.sun.tools.javac.main.Option;
/*     */ import com.sun.tools.javac.tree.EndPosTable;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.PrintStream;
/*     */ import java.io.PrintWriter;
/*     */ import java.util.Arrays;
/*     */ import java.util.EnumSet;
/*     */ import java.util.HashSet;
/*     */ import java.util.Locale;
/*     */ import java.util.Queue;
/*     */ import java.util.Set;
/*     */ import javax.tools.Diagnostic.Kind;
/*     */ import javax.tools.DiagnosticListener;
/*     */ import javax.tools.JavaFileObject;
/*     */ 
/*     */ public class Log extends AbstractLog
/*     */ {
/*  56 */   public static final Context.Key<Log> logKey = new Context.Key();
/*     */ 
/*  60 */   public static final Context.Key<PrintWriter> outKey = new Context.Key();
/*     */   protected PrintWriter errWriter;
/*     */   protected PrintWriter warnWriter;
/*     */   protected PrintWriter noticeWriter;
/*     */   protected int MaxErrors;
/*     */   protected int MaxWarnings;
/*     */   public boolean promptOnError;
/*     */   public boolean emitWarnings;
/*     */   public boolean suppressNotes;
/*     */   public boolean dumpOnError;
/*     */   public boolean multipleErrors;
/*     */   protected DiagnosticListener<? super JavaFileObject> diagListener;
/*     */   private DiagnosticFormatter<JCDiagnostic> diagFormatter;
/*     */   public Set<String> expectDiagKeys;
/*     */   public boolean compressedOutput;
/*     */   private JavacMessages messages;
/*     */   private DiagnosticHandler diagnosticHandler;
/* 332 */   public int nerrors = 0;
/*     */ 
/* 336 */   public int nwarnings = 0;
/*     */ 
/* 342 */   private Set<Pair<JavaFileObject, Integer>> recorded = new HashSet();
/*     */ 
/* 688 */   private static boolean useRawMessages = false;
/*     */ 
/*     */   protected Log(Context paramContext, PrintWriter paramPrintWriter1, PrintWriter paramPrintWriter2, PrintWriter paramPrintWriter3)
/*     */   {
/* 234 */     super(JCDiagnostic.Factory.instance(paramContext));
/* 235 */     paramContext.put(logKey, this);
/* 236 */     this.errWriter = paramPrintWriter1;
/* 237 */     this.warnWriter = paramPrintWriter2;
/* 238 */     this.noticeWriter = paramPrintWriter3;
/*     */ 
/* 242 */     DiagnosticListener localDiagnosticListener = (DiagnosticListener)paramContext
/* 242 */       .get(DiagnosticListener.class);
/*     */ 
/* 243 */     this.diagListener = localDiagnosticListener;
/*     */ 
/* 245 */     this.diagnosticHandler = new DefaultDiagnosticHandler(null);
/*     */ 
/* 247 */     this.messages = JavacMessages.instance(paramContext);
/* 248 */     this.messages.add("com.sun.tools.javac.resources.javac");
/*     */ 
/* 250 */     final Options localOptions = Options.instance(paramContext);
/* 251 */     initOptions(localOptions);
/* 252 */     localOptions.addListener(new Runnable() {
/*     */       public void run() {
/* 254 */         Log.this.initOptions(localOptions);
/*     */       }
/*     */     });
/*     */   }
/*     */ 
/*     */   private void initOptions(Options paramOptions) {
/* 260 */     this.dumpOnError = paramOptions.isSet(Option.DOE);
/* 261 */     this.promptOnError = paramOptions.isSet(Option.PROMPT);
/* 262 */     this.emitWarnings = paramOptions.isUnset(Option.XLINT_CUSTOM, "none");
/* 263 */     this.suppressNotes = paramOptions.isSet("suppressNotes");
/* 264 */     this.MaxErrors = getIntOption(paramOptions, Option.XMAXERRS, getDefaultMaxErrors());
/* 265 */     this.MaxWarnings = getIntOption(paramOptions, Option.XMAXWARNS, getDefaultMaxWarnings());
/*     */ 
/* 267 */     boolean bool = paramOptions.isSet("rawDiagnostics");
/* 268 */     this.diagFormatter = (bool ? new RawDiagnosticFormatter(paramOptions) : new BasicDiagnosticFormatter(paramOptions, this.messages));
/*     */ 
/* 271 */     String str = paramOptions.get("expectKeys");
/* 272 */     if (str != null)
/* 273 */       this.expectDiagKeys = new HashSet(Arrays.asList(str.split(", *")));
/*     */   }
/*     */ 
/*     */   private int getIntOption(Options paramOptions, Option paramOption, int paramInt) {
/* 277 */     String str = paramOptions.get(paramOption);
/*     */     try {
/* 279 */       if (str != null) {
/* 280 */         int i = Integer.parseInt(str);
/* 281 */         return i <= 0 ? 2147483647 : i;
/*     */       }
/*     */     }
/*     */     catch (NumberFormatException localNumberFormatException) {
/*     */     }
/* 286 */     return paramInt;
/*     */   }
/*     */ 
/*     */   protected int getDefaultMaxErrors()
/*     */   {
/* 292 */     return 100;
/*     */   }
/*     */ 
/*     */   protected int getDefaultMaxWarnings()
/*     */   {
/* 298 */     return 100;
/*     */   }
/*     */ 
/*     */   static PrintWriter defaultWriter(Context paramContext)
/*     */   {
/* 304 */     PrintWriter localPrintWriter = (PrintWriter)paramContext.get(outKey);
/* 305 */     if (localPrintWriter == null)
/* 306 */       paramContext.put(outKey, localPrintWriter = new PrintWriter(System.err));
/* 307 */     return localPrintWriter;
/*     */   }
/*     */ 
/*     */   protected Log(Context paramContext)
/*     */   {
/* 313 */     this(paramContext, defaultWriter(paramContext));
/*     */   }
/*     */ 
/*     */   protected Log(Context paramContext, PrintWriter paramPrintWriter)
/*     */   {
/* 319 */     this(paramContext, paramPrintWriter, paramPrintWriter, paramPrintWriter);
/*     */   }
/*     */ 
/*     */   public static Log instance(Context paramContext)
/*     */   {
/* 324 */     Log localLog = (Log)paramContext.get(logKey);
/* 325 */     if (localLog == null)
/* 326 */       localLog = new Log(paramContext);
/* 327 */     return localLog;
/*     */   }
/*     */ 
/*     */   public boolean hasDiagnosticListener()
/*     */   {
/* 345 */     return this.diagListener != null;
/*     */   }
/*     */ 
/*     */   public void setEndPosTable(JavaFileObject paramJavaFileObject, EndPosTable paramEndPosTable) {
/* 349 */     paramJavaFileObject.getClass();
/* 350 */     getSource(paramJavaFileObject).setEndPosTable(paramEndPosTable);
/*     */   }
/*     */ 
/*     */   public JavaFileObject currentSourceFile()
/*     */   {
/* 356 */     return this.source == null ? null : this.source.getFile();
/*     */   }
/*     */ 
/*     */   public DiagnosticFormatter<JCDiagnostic> getDiagnosticFormatter()
/*     */   {
/* 362 */     return this.diagFormatter;
/*     */   }
/*     */ 
/*     */   public void setDiagnosticFormatter(DiagnosticFormatter<JCDiagnostic> paramDiagnosticFormatter)
/*     */   {
/* 368 */     this.diagFormatter = paramDiagnosticFormatter;
/*     */   }
/*     */ 
/*     */   public PrintWriter getWriter(WriterKind paramWriterKind) {
/* 372 */     switch (2.$SwitchMap$com$sun$tools$javac$util$Log$WriterKind[paramWriterKind.ordinal()]) { case 1:
/* 373 */       return this.noticeWriter;
/*     */     case 2:
/* 374 */       return this.warnWriter;
/*     */     case 3:
/* 375 */       return this.errWriter; }
/* 376 */     throw new IllegalArgumentException();
/*     */   }
/*     */ 
/*     */   public void setWriter(WriterKind paramWriterKind, PrintWriter paramPrintWriter)
/*     */   {
/* 381 */     paramPrintWriter.getClass();
/* 382 */     switch (2.$SwitchMap$com$sun$tools$javac$util$Log$WriterKind[paramWriterKind.ordinal()]) { case 1:
/* 383 */       this.noticeWriter = paramPrintWriter; break;
/*     */     case 2:
/* 384 */       this.warnWriter = paramPrintWriter; break;
/*     */     case 3:
/* 385 */       this.errWriter = paramPrintWriter; break;
/*     */     default:
/* 386 */       throw new IllegalArgumentException(); }
/*     */   }
/*     */ 
/*     */   public void setWriters(PrintWriter paramPrintWriter)
/*     */   {
/* 391 */     paramPrintWriter.getClass();
/* 392 */     this.noticeWriter = (this.warnWriter = this.errWriter = paramPrintWriter);
/*     */   }
/*     */ 
/*     */   public void initRound(Log paramLog)
/*     */   {
/* 399 */     this.noticeWriter = paramLog.noticeWriter;
/* 400 */     this.warnWriter = paramLog.warnWriter;
/* 401 */     this.errWriter = paramLog.errWriter;
/* 402 */     this.sourceMap = paramLog.sourceMap;
/* 403 */     this.recorded = paramLog.recorded;
/* 404 */     this.nerrors = paramLog.nerrors;
/* 405 */     this.nwarnings = paramLog.nwarnings;
/*     */   }
/*     */ 
/*     */   public void popDiagnosticHandler(DiagnosticHandler paramDiagnosticHandler)
/*     */   {
/* 415 */     Assert.check(this.diagnosticHandler == paramDiagnosticHandler);
/* 416 */     this.diagnosticHandler = paramDiagnosticHandler.prev;
/*     */   }
/*     */ 
/*     */   public void flush()
/*     */   {
/* 422 */     this.errWriter.flush();
/* 423 */     this.warnWriter.flush();
/* 424 */     this.noticeWriter.flush();
/*     */   }
/*     */ 
/*     */   public void flush(WriterKind paramWriterKind) {
/* 428 */     getWriter(paramWriterKind).flush();
/*     */   }
/*     */ 
/*     */   protected boolean shouldReport(JavaFileObject paramJavaFileObject, int paramInt)
/*     */   {
/* 435 */     if ((this.multipleErrors) || (paramJavaFileObject == null)) {
/* 436 */       return true;
/*     */     }
/* 438 */     Pair localPair = new Pair(paramJavaFileObject, Integer.valueOf(paramInt));
/* 439 */     boolean bool = !this.recorded.contains(localPair);
/* 440 */     if (bool)
/* 441 */       this.recorded.add(localPair);
/* 442 */     return bool;
/*     */   }
/*     */ 
/*     */   public void prompt()
/*     */   {
/* 448 */     if (this.promptOnError) {
/* 449 */       System.err.println(localize("resume.abort", new Object[0]));
/*     */       try {
/*     */         while (true)
/* 452 */           switch (System.in.read()) { case 65:
/*     */           case 97:
/* 454 */             System.exit(-1);
/* 455 */             return;
/*     */           case 82:
/*     */           case 114:
/* 457 */             return;
/*     */           case 88:
/*     */           case 120:
/* 459 */             throw new AssertionError("user abort");
/*     */           }
/*     */       }
/*     */       catch (IOException localIOException)
/*     */       {
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   private void printErrLine(int paramInt, PrintWriter paramPrintWriter)
/*     */   {
/* 471 */     String str = this.source == null ? null : this.source.getLine(paramInt);
/* 472 */     if (str == null)
/* 473 */       return;
/* 474 */     int i = this.source.getColumnNumber(paramInt, false);
/*     */ 
/* 476 */     printRawLines(paramPrintWriter, str);
/* 477 */     for (int j = 0; j < i - 1; j++) {
/* 478 */       paramPrintWriter.print(str.charAt(j) == '\t' ? "\t" : " ");
/*     */     }
/* 480 */     paramPrintWriter.println("^");
/* 481 */     paramPrintWriter.flush();
/*     */   }
/*     */ 
/*     */   public void printNewline() {
/* 485 */     this.noticeWriter.println();
/*     */   }
/*     */ 
/*     */   public void printNewline(WriterKind paramWriterKind) {
/* 489 */     getWriter(paramWriterKind).println();
/*     */   }
/*     */ 
/*     */   public void printLines(String paramString, Object[] paramArrayOfObject) {
/* 493 */     printRawLines(this.noticeWriter, localize(paramString, paramArrayOfObject));
/*     */   }
/*     */ 
/*     */   public void printLines(PrefixKind paramPrefixKind, String paramString, Object[] paramArrayOfObject) {
/* 497 */     printRawLines(this.noticeWriter, localize(paramPrefixKind, paramString, paramArrayOfObject));
/*     */   }
/*     */ 
/*     */   public void printLines(WriterKind paramWriterKind, String paramString, Object[] paramArrayOfObject) {
/* 501 */     printRawLines(getWriter(paramWriterKind), localize(paramString, paramArrayOfObject));
/*     */   }
/*     */ 
/*     */   public void printLines(WriterKind paramWriterKind, PrefixKind paramPrefixKind, String paramString, Object[] paramArrayOfObject) {
/* 505 */     printRawLines(getWriter(paramWriterKind), localize(paramPrefixKind, paramString, paramArrayOfObject));
/*     */   }
/*     */ 
/*     */   public void printRawLines(String paramString)
/*     */   {
/* 512 */     printRawLines(this.noticeWriter, paramString);
/*     */   }
/*     */ 
/*     */   public void printRawLines(WriterKind paramWriterKind, String paramString)
/*     */   {
/* 519 */     printRawLines(getWriter(paramWriterKind), paramString);
/*     */   }
/*     */ 
/*     */   public static void printRawLines(PrintWriter paramPrintWriter, String paramString)
/*     */   {
/*     */     int i;
/* 527 */     while ((i = paramString.indexOf('\n')) != -1) {
/* 528 */       paramPrintWriter.println(paramString.substring(0, i));
/* 529 */       paramString = paramString.substring(i + 1);
/*     */     }
/* 531 */     if (paramString.length() != 0) paramPrintWriter.println(paramString);
/*     */   }
/*     */ 
/*     */   public void printVerbose(String paramString, Object[] paramArrayOfObject)
/*     */   {
/* 539 */     printRawLines(this.noticeWriter, localize("verbose." + paramString, paramArrayOfObject));
/*     */   }
/*     */ 
/*     */   protected void directError(String paramString, Object[] paramArrayOfObject) {
/* 543 */     printRawLines(this.errWriter, localize(paramString, paramArrayOfObject));
/* 544 */     this.errWriter.flush();
/*     */   }
/*     */ 
/*     */   public void strictWarning(JCDiagnostic.DiagnosticPosition paramDiagnosticPosition, String paramString, Object[] paramArrayOfObject)
/*     */   {
/* 553 */     writeDiagnostic(this.diags.warning(this.source, paramDiagnosticPosition, paramString, paramArrayOfObject));
/* 554 */     this.nwarnings += 1;
/*     */   }
/*     */ 
/*     */   public void report(JCDiagnostic paramJCDiagnostic)
/*     */   {
/* 562 */     this.diagnosticHandler.report(paramJCDiagnostic);
/*     */   }
/*     */ 
/*     */   protected void writeDiagnostic(JCDiagnostic paramJCDiagnostic)
/*     */   {
/* 615 */     if (this.diagListener != null) {
/* 616 */       this.diagListener.report(paramJCDiagnostic);
/* 617 */       return;
/*     */     }
/*     */ 
/* 620 */     PrintWriter localPrintWriter = getWriterForDiagnosticType(paramJCDiagnostic.getType());
/*     */ 
/* 622 */     printRawLines(localPrintWriter, this.diagFormatter.format(paramJCDiagnostic, this.messages.getCurrentLocale()));
/*     */ 
/* 624 */     if (this.promptOnError) {
/* 625 */       switch (paramJCDiagnostic.getType()) {
/*     */       case WARNING:
/*     */       case ERROR:
/* 628 */         prompt();
/*     */       }
/*     */     }
/*     */ 
/* 632 */     if (this.dumpOnError) {
/* 633 */       new RuntimeException().printStackTrace(localPrintWriter);
/*     */     }
/* 635 */     localPrintWriter.flush();
/*     */   }
/*     */ 
/*     */   @Deprecated
/*     */   protected PrintWriter getWriterForDiagnosticType(JCDiagnostic.DiagnosticType paramDiagnosticType) {
/* 640 */     switch (2.$SwitchMap$com$sun$tools$javac$util$JCDiagnostic$DiagnosticType[paramDiagnosticType.ordinal()]) {
/*     */     case 1:
/* 642 */       throw new IllegalArgumentException();
/*     */     case 2:
/* 645 */       return this.noticeWriter;
/*     */     case 3:
/* 648 */       return this.warnWriter;
/*     */     case 4:
/* 651 */       return this.errWriter;
/*     */     }
/*     */ 
/* 654 */     throw new Error();
/*     */   }
/*     */ 
/*     */   public static String getLocalizedString(String paramString, Object[] paramArrayOfObject)
/*     */   {
/* 665 */     return JavacMessages.getDefaultLocalizedString(PrefixKind.COMPILER_MISC.key(paramString), paramArrayOfObject);
/*     */   }
/*     */ 
/*     */   public String localize(String paramString, Object[] paramArrayOfObject)
/*     */   {
/* 673 */     return localize(PrefixKind.COMPILER_MISC, paramString, paramArrayOfObject);
/*     */   }
/*     */ 
/*     */   public String localize(PrefixKind paramPrefixKind, String paramString, Object[] paramArrayOfObject)
/*     */   {
/* 681 */     if (useRawMessages) {
/* 682 */       return paramPrefixKind.key(paramString);
/*     */     }
/* 684 */     return this.messages.getLocalizedString(paramPrefixKind.key(paramString), paramArrayOfObject);
/*     */   }
/*     */ 
/*     */   private void printRawError(int paramInt, String paramString)
/*     */   {
/* 698 */     if ((this.source == null) || (paramInt == -1)) {
/* 699 */       printRawLines(this.errWriter, "error: " + paramString);
/*     */     } else {
/* 701 */       int i = this.source.getLineNumber(paramInt);
/* 702 */       JavaFileObject localJavaFileObject = this.source.getFile();
/* 703 */       if (localJavaFileObject != null) {
/* 704 */         printRawLines(this.errWriter, localJavaFileObject
/* 705 */           .getName() + ":" + i + ": " + paramString);
/*     */       }
/* 707 */       printErrLine(paramInt, this.errWriter);
/*     */     }
/* 709 */     this.errWriter.flush();
/*     */   }
/*     */ 
/*     */   public void rawError(int paramInt, String paramString)
/*     */   {
/* 715 */     if ((this.nerrors < this.MaxErrors) && (shouldReport(currentSourceFile(), paramInt))) {
/* 716 */       printRawError(paramInt, paramString);
/* 717 */       prompt();
/* 718 */       this.nerrors += 1;
/*     */     }
/* 720 */     this.errWriter.flush();
/*     */   }
/*     */ 
/*     */   public void rawWarning(int paramInt, String paramString)
/*     */   {
/* 726 */     if ((this.nwarnings < this.MaxWarnings) && (this.emitWarnings)) {
/* 727 */       printRawError(paramInt, "warning: " + paramString);
/*     */     }
/* 729 */     prompt();
/* 730 */     this.nwarnings += 1;
/* 731 */     this.errWriter.flush();
/*     */   }
/*     */ 
/*     */   public static String format(String paramString, Object[] paramArrayOfObject) {
/* 735 */     return String.format((Locale)null, paramString, paramArrayOfObject);
/*     */   }
/*     */ 
/*     */   private class DefaultDiagnosticHandler extends Log.DiagnosticHandler
/*     */   {
/*     */     private DefaultDiagnosticHandler()
/*     */     {
/*     */     }
/*     */ 
/*     */     public void report(JCDiagnostic paramJCDiagnostic)
/*     */     {
/* 572 */       if (Log.this.expectDiagKeys != null) {
/* 573 */         Log.this.expectDiagKeys.remove(paramJCDiagnostic.getCode());
/*     */       }
/* 575 */       switch (Log.2.$SwitchMap$com$sun$tools$javac$util$JCDiagnostic$DiagnosticType[paramJCDiagnostic.getType().ordinal()]) {
/*     */       case 1:
/* 577 */         throw new IllegalArgumentException();
/*     */       case 2:
/* 583 */         if (((Log.this.emitWarnings) || (paramJCDiagnostic.isMandatory())) && (!Log.this.suppressNotes))
/* 584 */           Log.this.writeDiagnostic(paramJCDiagnostic); break;
/*     */       case 3:
/* 589 */         if (((Log.this.emitWarnings) || (paramJCDiagnostic.isMandatory())) && 
/* 590 */           (Log.this.nwarnings < Log.this.MaxWarnings)) {
/* 591 */           Log.this.writeDiagnostic(paramJCDiagnostic);
/* 592 */           Log.this.nwarnings += 1; } break;
/*     */       case 4:
/* 598 */         if ((Log.this.nerrors < Log.this.MaxErrors) && 
/* 599 */           (Log.this
/* 599 */           .shouldReport(paramJCDiagnostic
/* 599 */           .getSource(), paramJCDiagnostic.getIntPosition()))) {
/* 600 */           Log.this.writeDiagnostic(paramJCDiagnostic);
/* 601 */           Log.this.nerrors += 1;
/*     */         }
/*     */         break;
/*     */       }
/* 605 */       if (paramJCDiagnostic.isFlagSet(JCDiagnostic.DiagnosticFlag.COMPRESSED))
/* 606 */         Log.this.compressedOutput = true;
/*     */     }
/*     */   }
/*     */ 
/*     */   public static class DeferredDiagnosticHandler extends Log.DiagnosticHandler
/*     */   {
/* 126 */     private Queue<JCDiagnostic> deferred = new ListBuffer();
/*     */     private final Filter<JCDiagnostic> filter;
/*     */ 
/*     */     public DeferredDiagnosticHandler(Log paramLog)
/*     */     {
/* 130 */       this(paramLog, null);
/*     */     }
/*     */ 
/*     */     public DeferredDiagnosticHandler(Log paramLog, Filter<JCDiagnostic> paramFilter) {
/* 134 */       this.filter = paramFilter;
/* 135 */       install(paramLog);
/*     */     }
/*     */ 
/*     */     public void report(JCDiagnostic paramJCDiagnostic) {
/* 139 */       if ((!paramJCDiagnostic.isFlagSet(JCDiagnostic.DiagnosticFlag.NON_DEFERRABLE)) && ((this.filter == null) || 
/* 140 */         (this.filter
/* 140 */         .accepts(paramJCDiagnostic))))
/*     */       {
/* 141 */         this.deferred.add(paramJCDiagnostic);
/*     */       }
/* 143 */       else this.prev.report(paramJCDiagnostic);
/*     */     }
/*     */ 
/*     */     public Queue<JCDiagnostic> getDiagnostics()
/*     */     {
/* 148 */       return this.deferred;
/*     */     }
/*     */ 
/*     */     public void reportDeferredDiagnostics()
/*     */     {
/* 153 */       reportDeferredDiagnostics(EnumSet.allOf(Diagnostic.Kind.class));
/*     */     }
/*     */ 
/*     */     public void reportDeferredDiagnostics(Set<Diagnostic.Kind> paramSet)
/*     */     {
/*     */       JCDiagnostic localJCDiagnostic;
/* 159 */       while ((localJCDiagnostic = (JCDiagnostic)this.deferred.poll()) != null) {
/* 160 */         if (paramSet.contains(localJCDiagnostic.getKind()))
/* 161 */           this.prev.report(localJCDiagnostic);
/*     */       }
/* 163 */       this.deferred = null;
/*     */     }
/*     */   }
/*     */ 
/*     */   public static abstract class DiagnosticHandler
/*     */   {
/*     */     protected DiagnosticHandler prev;
/*     */ 
/*     */     protected void install(Log paramLog)
/*     */     {
/*  97 */       this.prev = paramLog.diagnosticHandler;
/*  98 */       paramLog.diagnosticHandler = this;
/*     */     }
/*     */ 
/*     */     public abstract void report(JCDiagnostic paramJCDiagnostic);
/*     */   }
/*     */ 
/*     */   public static class DiscardDiagnosticHandler extends Log.DiagnosticHandler
/*     */   {
/*     */     public DiscardDiagnosticHandler(Log paramLog)
/*     */     {
/* 112 */       install(paramLog);
/*     */     }
/*     */ 
/*     */     public void report(JCDiagnostic paramJCDiagnostic)
/*     */     {
/*     */     }
/*     */   }
/*     */ 
/*     */   public static enum PrefixKind
/*     */   {
/*  65 */     JAVAC("javac."), 
/*  66 */     COMPILER_MISC("compiler.misc.");
/*     */ 
/*     */     final String value;
/*     */ 
/*  68 */     private PrefixKind(String paramString) { this.value = paramString; }
/*     */ 
/*     */     public String key(String paramString) {
/*  71 */       return this.value + paramString;
/*     */     }
/*     */   }
/*     */ 
/*     */   public static enum WriterKind
/*     */   {
/* 167 */     NOTICE, WARNING, ERROR;
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.javac.util.Log
 * JD-Core Version:    0.6.2
 */