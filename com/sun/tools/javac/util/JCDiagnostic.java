/*     */ package com.sun.tools.javac.util;
/*     */ 
/*     */ import com.sun.tools.javac.api.DiagnosticFormatter;
/*     */ import com.sun.tools.javac.code.Lint.LintCategory;
/*     */ import com.sun.tools.javac.tree.EndPosTable;
/*     */ import com.sun.tools.javac.tree.JCTree;
/*     */ import java.util.EnumSet;
/*     */ import java.util.Locale;
/*     */ import java.util.Set;
/*     */ import javax.tools.Diagnostic;
/*     */ import javax.tools.Diagnostic.Kind;
/*     */ import javax.tools.JavaFileObject;
/*     */ 
/*     */ public class JCDiagnostic
/*     */   implements Diagnostic<JavaFileObject>
/*     */ {
/*     */   private final DiagnosticType type;
/*     */   private final DiagnosticSource source;
/*     */   private final DiagnosticPosition position;
/*     */   private final String key;
/*     */   protected final Object[] args;
/*     */   private final Set<DiagnosticFlag> flags;
/*     */   private final Lint.LintCategory lintCategory;
/*     */   private SourcePosition sourcePosition;
/*     */   private DiagnosticFormatter<JCDiagnostic> defaultFormatter;
/*     */ 
/*     */   @Deprecated
/*     */   private static DiagnosticFormatter<JCDiagnostic> fragmentFormatter;
/*     */ 
/*     */   @Deprecated
/*     */   public static JCDiagnostic fragment(String paramString, Object[] paramArrayOfObject)
/*     */   {
/* 259 */     return new JCDiagnostic(getFragmentFormatter(), DiagnosticType.FRAGMENT, null, 
/* 259 */       EnumSet.noneOf(DiagnosticFlag.class), 
/* 259 */       null, null, "compiler." + DiagnosticType.FRAGMENT.key + "." + paramString, paramArrayOfObject);
/*     */   }
/*     */ 
/*     */   @Deprecated
/*     */   public static DiagnosticFormatter<JCDiagnostic> getFragmentFormatter()
/*     */   {
/* 268 */     if (fragmentFormatter == null) {
/* 269 */       fragmentFormatter = new BasicDiagnosticFormatter(JavacMessages.getDefaultMessages());
/*     */     }
/* 271 */     return fragmentFormatter;
/*     */   }
/*     */ 
/*     */   protected JCDiagnostic(DiagnosticFormatter<JCDiagnostic> paramDiagnosticFormatter, DiagnosticType paramDiagnosticType, Lint.LintCategory paramLintCategory, Set<DiagnosticFlag> paramSet, DiagnosticSource paramDiagnosticSource, DiagnosticPosition paramDiagnosticPosition, String paramString, Object[] paramArrayOfObject)
/*     */   {
/* 411 */     if ((paramDiagnosticSource == null) && (paramDiagnosticPosition != null) && (paramDiagnosticPosition.getPreferredPosition() != -1)) {
/* 412 */       throw new IllegalArgumentException();
/*     */     }
/* 414 */     this.defaultFormatter = paramDiagnosticFormatter;
/* 415 */     this.type = paramDiagnosticType;
/* 416 */     this.lintCategory = paramLintCategory;
/* 417 */     this.flags = paramSet;
/* 418 */     this.source = paramDiagnosticSource;
/* 419 */     this.position = paramDiagnosticPosition;
/* 420 */     this.key = paramString;
/* 421 */     this.args = paramArrayOfObject;
/*     */   }
/*     */ 
/*     */   public DiagnosticType getType()
/*     */   {
/* 429 */     return this.type;
/*     */   }
/*     */ 
/*     */   public List<JCDiagnostic> getSubdiagnostics()
/*     */   {
/* 437 */     return List.nil();
/*     */   }
/*     */ 
/*     */   public boolean isMultiline() {
/* 441 */     return false;
/*     */   }
/*     */ 
/*     */   public boolean isMandatory()
/*     */   {
/* 449 */     return this.flags.contains(DiagnosticFlag.MANDATORY);
/*     */   }
/*     */ 
/*     */   public boolean hasLintCategory()
/*     */   {
/* 456 */     return this.lintCategory != null;
/*     */   }
/*     */ 
/*     */   public Lint.LintCategory getLintCategory()
/*     */   {
/* 463 */     return this.lintCategory;
/*     */   }
/*     */ 
/*     */   public JavaFileObject getSource()
/*     */   {
/* 471 */     if (this.source == null) {
/* 472 */       return null;
/*     */     }
/* 474 */     return this.source.getFile();
/*     */   }
/*     */ 
/*     */   public DiagnosticSource getDiagnosticSource()
/*     */   {
/* 482 */     return this.source;
/*     */   }
/*     */ 
/*     */   protected int getIntStartPosition() {
/* 486 */     return this.position == null ? -1 : this.position.getStartPosition();
/*     */   }
/*     */ 
/*     */   protected int getIntPosition() {
/* 490 */     return this.position == null ? -1 : this.position.getPreferredPosition();
/*     */   }
/*     */ 
/*     */   protected int getIntEndPosition() {
/* 494 */     return this.position == null ? -1 : this.position.getEndPosition(this.source.getEndPosTable());
/*     */   }
/*     */ 
/*     */   public long getStartPosition() {
/* 498 */     return getIntStartPosition();
/*     */   }
/*     */ 
/*     */   public long getPosition() {
/* 502 */     return getIntPosition();
/*     */   }
/*     */ 
/*     */   public long getEndPosition() {
/* 506 */     return getIntEndPosition();
/*     */   }
/*     */ 
/*     */   public DiagnosticPosition getDiagnosticPosition() {
/* 510 */     return this.position;
/*     */   }
/*     */ 
/*     */   public long getLineNumber()
/*     */   {
/* 518 */     if (this.sourcePosition == null) {
/* 519 */       this.sourcePosition = new SourcePosition();
/*     */     }
/* 521 */     return this.sourcePosition.getLineNumber();
/*     */   }
/*     */ 
/*     */   public long getColumnNumber()
/*     */   {
/* 529 */     if (this.sourcePosition == null) {
/* 530 */       this.sourcePosition = new SourcePosition();
/*     */     }
/* 532 */     return this.sourcePosition.getColumnNumber();
/*     */   }
/*     */ 
/*     */   public Object[] getArgs()
/*     */   {
/* 540 */     return this.args;
/*     */   }
/*     */ 
/*     */   public String getPrefix()
/*     */   {
/* 548 */     return getPrefix(this.type);
/*     */   }
/*     */ 
/*     */   public String getPrefix(DiagnosticType paramDiagnosticType)
/*     */   {
/* 556 */     return this.defaultFormatter.formatKind(this, Locale.getDefault());
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 564 */     return this.defaultFormatter.format(this, Locale.getDefault());
/*     */   }
/*     */ 
/*     */   public Diagnostic.Kind getKind()
/*     */   {
/* 574 */     switch (1.$SwitchMap$com$sun$tools$javac$util$JCDiagnostic$DiagnosticType[this.type.ordinal()]) {
/*     */     case 1:
/* 576 */       return Diagnostic.Kind.NOTE;
/*     */     case 2:
/* 578 */       return this.flags.contains(DiagnosticFlag.MANDATORY) ? Diagnostic.Kind.MANDATORY_WARNING : Diagnostic.Kind.WARNING;
/*     */     case 3:
/* 582 */       return Diagnostic.Kind.ERROR;
/*     */     }
/* 584 */     return Diagnostic.Kind.OTHER;
/*     */   }
/*     */ 
/*     */   public String getCode()
/*     */   {
/* 589 */     return this.key;
/*     */   }
/*     */ 
/*     */   public String getMessage(Locale paramLocale) {
/* 593 */     return this.defaultFormatter.formatMessage(this, paramLocale);
/*     */   }
/*     */ 
/*     */   public void setFlag(DiagnosticFlag paramDiagnosticFlag) {
/* 597 */     this.flags.add(paramDiagnosticFlag);
/*     */ 
/* 599 */     if (this.type == DiagnosticType.ERROR)
/* 600 */       switch (paramDiagnosticFlag) {
/*     */       case SYNTAX:
/* 602 */         this.flags.remove(DiagnosticFlag.RECOVERABLE);
/* 603 */         break;
/*     */       case RESOLVE_ERROR:
/* 605 */         this.flags.add(DiagnosticFlag.RECOVERABLE);
/*     */       }
/*     */   }
/*     */ 
/*     */   public boolean isFlagSet(DiagnosticFlag paramDiagnosticFlag)
/*     */   {
/* 612 */     return this.flags.contains(paramDiagnosticFlag);
/*     */   }
/*     */ 
/*     */   public static enum DiagnosticFlag
/*     */   {
/* 347 */     MANDATORY, 
/* 348 */     RESOLVE_ERROR, 
/* 349 */     SYNTAX, 
/* 350 */     RECOVERABLE, 
/* 351 */     NON_DEFERRABLE, 
/* 352 */     COMPRESSED;
/*     */   }
/*     */ 
/*     */   public static abstract interface DiagnosticPosition
/*     */   {
/*     */     public abstract JCTree getTree();
/*     */ 
/*     */     public abstract int getStartPosition();
/*     */ 
/*     */     public abstract int getPreferredPosition();
/*     */ 
/*     */     public abstract int getEndPosition(EndPosTable paramEndPosTable);
/*     */   }
/*     */ 
/*     */   public static enum DiagnosticType
/*     */   {
/* 279 */     FRAGMENT("misc"), 
/*     */ 
/* 281 */     NOTE("note"), 
/*     */ 
/* 283 */     WARNING("warn"), 
/*     */ 
/* 285 */     ERROR("err");
/*     */ 
/*     */     final String key;
/*     */ 
/*     */     private DiagnosticType(String paramString)
/*     */     {
/* 293 */       this.key = paramString;
/*     */     }
/*     */   }
/*     */ 
/*     */   public static class Factory
/*     */   {
/*  53 */     protected static final Context.Key<Factory> diagnosticFactoryKey = new Context.Key();
/*     */     DiagnosticFormatter<JCDiagnostic> formatter;
/*     */     final String prefix;
/*     */     final Set<JCDiagnostic.DiagnosticFlag> defaultErrorFlags;
/*     */ 
/*     */     public static Factory instance(Context paramContext)
/*     */     {
/*  58 */       Factory localFactory = (Factory)paramContext.get(diagnosticFactoryKey);
/*  59 */       if (localFactory == null)
/*  60 */         localFactory = new Factory(paramContext);
/*  61 */       return localFactory;
/*     */     }
/*     */ 
/*     */     protected Factory(Context paramContext)
/*     */     {
/*  70 */       this(JavacMessages.instance(paramContext), "compiler");
/*  71 */       paramContext.put(diagnosticFactoryKey, this);
/*     */ 
/*  73 */       final Options localOptions = Options.instance(paramContext);
/*  74 */       initOptions(localOptions);
/*  75 */       localOptions.addListener(new Runnable() {
/*     */         public void run() {
/*  77 */           JCDiagnostic.Factory.this.initOptions(localOptions);
/*     */         }
/*     */       });
/*     */     }
/*     */ 
/*     */     private void initOptions(Options paramOptions) {
/*  83 */       if (paramOptions.isSet("onlySyntaxErrorsUnrecoverable"))
/*  84 */         this.defaultErrorFlags.add(JCDiagnostic.DiagnosticFlag.RECOVERABLE);
/*     */     }
/*     */ 
/*     */     public Factory(JavacMessages paramJavacMessages, String paramString)
/*     */     {
/*  89 */       this.prefix = paramString;
/*  90 */       this.formatter = new BasicDiagnosticFormatter(paramJavacMessages);
/*  91 */       this.defaultErrorFlags = EnumSet.of(JCDiagnostic.DiagnosticFlag.MANDATORY);
/*     */     }
/*     */ 
/*     */     public JCDiagnostic error(DiagnosticSource paramDiagnosticSource, JCDiagnostic.DiagnosticPosition paramDiagnosticPosition, String paramString, Object[] paramArrayOfObject)
/*     */     {
/* 103 */       return create(JCDiagnostic.DiagnosticType.ERROR, null, this.defaultErrorFlags, paramDiagnosticSource, paramDiagnosticPosition, paramString, paramArrayOfObject);
/*     */     }
/*     */ 
/*     */     public JCDiagnostic mandatoryWarning(DiagnosticSource paramDiagnosticSource, JCDiagnostic.DiagnosticPosition paramDiagnosticPosition, String paramString, Object[] paramArrayOfObject)
/*     */     {
/* 116 */       return create(JCDiagnostic.DiagnosticType.WARNING, null, EnumSet.of(JCDiagnostic.DiagnosticFlag.MANDATORY), paramDiagnosticSource, paramDiagnosticPosition, paramString, paramArrayOfObject);
/*     */     }
/*     */ 
/*     */     public JCDiagnostic mandatoryWarning(Lint.LintCategory paramLintCategory, DiagnosticSource paramDiagnosticSource, JCDiagnostic.DiagnosticPosition paramDiagnosticPosition, String paramString, Object[] paramArrayOfObject)
/*     */     {
/* 131 */       return create(JCDiagnostic.DiagnosticType.WARNING, paramLintCategory, EnumSet.of(JCDiagnostic.DiagnosticFlag.MANDATORY), paramDiagnosticSource, paramDiagnosticPosition, paramString, paramArrayOfObject);
/*     */     }
/*     */ 
/*     */     public JCDiagnostic warning(Lint.LintCategory paramLintCategory, String paramString, Object[] paramArrayOfObject)
/*     */     {
/* 143 */       return create(JCDiagnostic.DiagnosticType.WARNING, paramLintCategory, EnumSet.noneOf(JCDiagnostic.DiagnosticFlag.class), null, null, paramString, paramArrayOfObject);
/*     */     }
/*     */ 
/*     */     public JCDiagnostic warning(DiagnosticSource paramDiagnosticSource, JCDiagnostic.DiagnosticPosition paramDiagnosticPosition, String paramString, Object[] paramArrayOfObject)
/*     */     {
/* 155 */       return create(JCDiagnostic.DiagnosticType.WARNING, null, EnumSet.noneOf(JCDiagnostic.DiagnosticFlag.class), paramDiagnosticSource, paramDiagnosticPosition, paramString, paramArrayOfObject);
/*     */     }
/*     */ 
/*     */     public JCDiagnostic warning(Lint.LintCategory paramLintCategory, DiagnosticSource paramDiagnosticSource, JCDiagnostic.DiagnosticPosition paramDiagnosticPosition, String paramString, Object[] paramArrayOfObject)
/*     */     {
/* 169 */       return create(JCDiagnostic.DiagnosticType.WARNING, paramLintCategory, EnumSet.noneOf(JCDiagnostic.DiagnosticFlag.class), paramDiagnosticSource, paramDiagnosticPosition, paramString, paramArrayOfObject);
/*     */     }
/*     */ 
/*     */     public JCDiagnostic mandatoryNote(DiagnosticSource paramDiagnosticSource, String paramString, Object[] paramArrayOfObject)
/*     */     {
/* 179 */       return create(JCDiagnostic.DiagnosticType.NOTE, null, EnumSet.of(JCDiagnostic.DiagnosticFlag.MANDATORY), paramDiagnosticSource, null, paramString, paramArrayOfObject);
/*     */     }
/*     */ 
/*     */     public JCDiagnostic note(String paramString, Object[] paramArrayOfObject)
/*     */     {
/* 188 */       return create(JCDiagnostic.DiagnosticType.NOTE, null, EnumSet.noneOf(JCDiagnostic.DiagnosticFlag.class), null, null, paramString, paramArrayOfObject);
/*     */     }
/*     */ 
/*     */     public JCDiagnostic note(DiagnosticSource paramDiagnosticSource, JCDiagnostic.DiagnosticPosition paramDiagnosticPosition, String paramString, Object[] paramArrayOfObject)
/*     */     {
/* 200 */       return create(JCDiagnostic.DiagnosticType.NOTE, null, EnumSet.noneOf(JCDiagnostic.DiagnosticFlag.class), paramDiagnosticSource, paramDiagnosticPosition, paramString, paramArrayOfObject);
/*     */     }
/*     */ 
/*     */     public JCDiagnostic fragment(String paramString, Object[] paramArrayOfObject)
/*     */     {
/* 209 */       return create(JCDiagnostic.DiagnosticType.FRAGMENT, null, EnumSet.noneOf(JCDiagnostic.DiagnosticFlag.class), null, null, paramString, paramArrayOfObject);
/*     */     }
/*     */ 
/*     */     public JCDiagnostic create(JCDiagnostic.DiagnosticType paramDiagnosticType, DiagnosticSource paramDiagnosticSource, JCDiagnostic.DiagnosticPosition paramDiagnosticPosition, String paramString, Object[] paramArrayOfObject)
/*     */     {
/* 223 */       return create(paramDiagnosticType, null, EnumSet.noneOf(JCDiagnostic.DiagnosticFlag.class), paramDiagnosticSource, paramDiagnosticPosition, paramString, paramArrayOfObject);
/*     */     }
/*     */ 
/*     */     public JCDiagnostic create(JCDiagnostic.DiagnosticType paramDiagnosticType, Lint.LintCategory paramLintCategory, Set<JCDiagnostic.DiagnosticFlag> paramSet, DiagnosticSource paramDiagnosticSource, JCDiagnostic.DiagnosticPosition paramDiagnosticPosition, String paramString, Object[] paramArrayOfObject)
/*     */     {
/* 238 */       return new JCDiagnostic(this.formatter, paramDiagnosticType, paramLintCategory, paramSet, paramDiagnosticSource, paramDiagnosticPosition, qualify(paramDiagnosticType, paramString), paramArrayOfObject);
/*     */     }
/*     */ 
/*     */     protected String qualify(JCDiagnostic.DiagnosticType paramDiagnosticType, String paramString) {
/* 242 */       return this.prefix + "." + paramDiagnosticType.key + "." + paramString;
/*     */     }
/*     */   }
/*     */ 
/*     */   public static class MultilineDiagnostic extends JCDiagnostic
/*     */   {
/*     */     private final List<JCDiagnostic> subdiagnostics;
/*     */ 
/*     */     public MultilineDiagnostic(JCDiagnostic paramJCDiagnostic, List<JCDiagnostic> paramList)
/*     */     {
/* 620 */       super(paramJCDiagnostic
/* 621 */         .getType(), paramJCDiagnostic
/* 622 */         .getLintCategory(), paramJCDiagnostic.flags, 
/* 623 */         paramJCDiagnostic
/* 624 */         .getDiagnosticSource(), paramJCDiagnostic.position, 
/* 625 */         paramJCDiagnostic
/* 626 */         .getCode(), paramJCDiagnostic
/* 627 */         .getArgs());
/* 628 */       this.subdiagnostics = paramList;
/*     */     }
/*     */ 
/*     */     public List<JCDiagnostic> getSubdiagnostics()
/*     */     {
/* 633 */       return this.subdiagnostics;
/*     */     }
/*     */ 
/*     */     public boolean isMultiline()
/*     */     {
/* 638 */       return true;
/*     */     }
/*     */   }
/*     */ 
/*     */   public static class SimpleDiagnosticPosition
/*     */     implements JCDiagnostic.DiagnosticPosition
/*     */   {
/*     */     private final int pos;
/*     */ 
/*     */     public SimpleDiagnosticPosition(int paramInt)
/*     */     {
/* 324 */       this.pos = paramInt;
/*     */     }
/*     */ 
/*     */     public JCTree getTree() {
/* 328 */       return null;
/*     */     }
/*     */ 
/*     */     public int getStartPosition() {
/* 332 */       return this.pos;
/*     */     }
/*     */ 
/*     */     public int getPreferredPosition() {
/* 336 */       return this.pos;
/*     */     }
/*     */ 
/*     */     public int getEndPosition(EndPosTable paramEndPosTable) {
/* 340 */       return this.pos;
/*     */     }
/*     */   }
/*     */ 
/*     */   class SourcePosition
/*     */   {
/*     */     private final int line;
/*     */     private final int column;
/*     */ 
/*     */     SourcePosition()
/*     */     {
/* 375 */       int i = JCDiagnostic.this.position == null ? -1 : JCDiagnostic.this.position.getPreferredPosition();
/* 376 */       if ((i == -1) || (JCDiagnostic.this.source == null)) {
/* 377 */         this.line = (this.column = -1);
/*     */       } else {
/* 379 */         this.line = JCDiagnostic.this.source.getLineNumber(i);
/* 380 */         this.column = JCDiagnostic.this.source.getColumnNumber(i, true);
/*     */       }
/*     */     }
/*     */ 
/*     */     public int getLineNumber() {
/* 385 */       return this.line;
/*     */     }
/*     */ 
/*     */     public int getColumnNumber() {
/* 389 */       return this.column;
/*     */     }
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.javac.util.JCDiagnostic
 * JD-Core Version:    0.6.2
 */