/*     */ package com.sun.tools.javac.util;
/*     */ 
/*     */ import com.sun.tools.javac.api.DiagnosticFormatter;
/*     */ import com.sun.tools.javac.api.DiagnosticFormatter.Configuration;
/*     */ import com.sun.tools.javac.api.DiagnosticFormatter.Configuration.DiagnosticPart;
/*     */ import com.sun.tools.javac.api.DiagnosticFormatter.Configuration.MultilineLimit;
/*     */ import com.sun.tools.javac.api.DiagnosticFormatter.PositionKind;
/*     */ import com.sun.tools.javac.api.Formattable;
/*     */ import com.sun.tools.javac.code.Lint.LintCategory;
/*     */ import com.sun.tools.javac.code.Printer;
/*     */ import com.sun.tools.javac.code.Symbol;
/*     */ import com.sun.tools.javac.code.Type;
/*     */ import com.sun.tools.javac.code.Type.CapturedType;
/*     */ import com.sun.tools.javac.file.BaseFileObject;
/*     */ import com.sun.tools.javac.jvm.Profile;
/*     */ import com.sun.tools.javac.tree.JCTree.JCExpression;
/*     */ import com.sun.tools.javac.tree.JCTree.JCParens;
/*     */ import com.sun.tools.javac.tree.JCTree.Tag;
/*     */ import com.sun.tools.javac.tree.Pretty;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collection;
/*     */ import java.util.EnumSet;
/*     */ import java.util.HashMap;
/*     */ import java.util.Iterator;
/*     */ import java.util.Locale;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import javax.tools.JavaFileObject;
/*     */ 
/*     */ public abstract class AbstractDiagnosticFormatter
/*     */   implements DiagnosticFormatter<JCDiagnostic>
/*     */ {
/*     */   protected JavacMessages messages;
/*     */   private SimpleConfiguration config;
/*  85 */   protected int depth = 0;
/*     */ 
/*  92 */   private List<Type> allCaptured = List.nil();
/*     */ 
/* 515 */   protected Printer printer = new Printer()
/*     */   {
/*     */     protected String localize(Locale paramAnonymousLocale, String paramAnonymousString, Object[] paramAnonymousArrayOfObject)
/*     */     {
/* 519 */       return AbstractDiagnosticFormatter.this.localize(paramAnonymousLocale, paramAnonymousString, paramAnonymousArrayOfObject);
/*     */     }
/*     */ 
/*     */     protected String capturedVarId(Type.CapturedType paramAnonymousCapturedType, Locale paramAnonymousLocale) {
/* 523 */       return "" + (AbstractDiagnosticFormatter.this.allCaptured.indexOf(paramAnonymousCapturedType) + 1);
/*     */     }
/*     */ 
/*     */     public String visitCapturedType(Type.CapturedType paramAnonymousCapturedType, Locale paramAnonymousLocale) {
/* 527 */       if (!AbstractDiagnosticFormatter.this.allCaptured.contains(paramAnonymousCapturedType)) {
/* 528 */         AbstractDiagnosticFormatter.this.allCaptured = AbstractDiagnosticFormatter.this.allCaptured.append(paramAnonymousCapturedType);
/*     */       }
/* 530 */       return super.visitCapturedType(paramAnonymousCapturedType, paramAnonymousLocale);
/*     */     }
/* 515 */   };
/*     */ 
/*     */   protected AbstractDiagnosticFormatter(JavacMessages paramJavacMessages, SimpleConfiguration paramSimpleConfiguration)
/*     */   {
/*  99 */     this.messages = paramJavacMessages;
/* 100 */     this.config = paramSimpleConfiguration;
/*     */   }
/*     */ 
/*     */   public String formatKind(JCDiagnostic paramJCDiagnostic, Locale paramLocale) {
/* 104 */     switch (2.$SwitchMap$com$sun$tools$javac$util$JCDiagnostic$DiagnosticType[paramJCDiagnostic.getType().ordinal()]) { case 1:
/* 105 */       return "";
/*     */     case 2:
/* 106 */       return localize(paramLocale, "compiler.note.note", new Object[0]);
/*     */     case 3:
/* 107 */       return localize(paramLocale, "compiler.warn.warning", new Object[0]);
/*     */     case 4:
/* 108 */       return localize(paramLocale, "compiler.err.error", new Object[0]);
/*     */     }
/* 110 */     throw new AssertionError("Unknown diagnostic type: " + paramJCDiagnostic.getType());
/*     */   }
/*     */ 
/*     */   public String format(JCDiagnostic paramJCDiagnostic, Locale paramLocale)
/*     */   {
/* 116 */     this.allCaptured = List.nil();
/* 117 */     return formatDiagnostic(paramJCDiagnostic, paramLocale);
/*     */   }
/*     */ 
/*     */   protected abstract String formatDiagnostic(JCDiagnostic paramJCDiagnostic, Locale paramLocale);
/*     */ 
/*     */   public String formatPosition(JCDiagnostic paramJCDiagnostic, DiagnosticFormatter.PositionKind paramPositionKind, Locale paramLocale) {
/* 123 */     Assert.check(paramJCDiagnostic.getPosition() != -1L);
/* 124 */     return String.valueOf(getPosition(paramJCDiagnostic, paramPositionKind));
/*     */   }
/*     */ 
/*     */   private long getPosition(JCDiagnostic paramJCDiagnostic, DiagnosticFormatter.PositionKind paramPositionKind) {
/* 128 */     switch (2.$SwitchMap$com$sun$tools$javac$api$DiagnosticFormatter$PositionKind[paramPositionKind.ordinal()]) { case 1:
/* 129 */       return paramJCDiagnostic.getIntStartPosition();
/*     */     case 2:
/* 130 */       return paramJCDiagnostic.getIntEndPosition();
/*     */     case 3:
/* 131 */       return paramJCDiagnostic.getLineNumber();
/*     */     case 4:
/* 132 */       return paramJCDiagnostic.getColumnNumber();
/*     */     case 5:
/* 133 */       return paramJCDiagnostic.getIntPosition();
/*     */     }
/* 135 */     throw new AssertionError("Unknown diagnostic position: " + paramPositionKind);
/*     */   }
/*     */ 
/*     */   public String formatSource(JCDiagnostic paramJCDiagnostic, boolean paramBoolean, Locale paramLocale)
/*     */   {
/* 140 */     JavaFileObject localJavaFileObject = paramJCDiagnostic.getSource();
/* 141 */     if (localJavaFileObject == null)
/* 142 */       throw new IllegalArgumentException();
/* 143 */     if (paramBoolean)
/* 144 */       return localJavaFileObject.getName();
/* 145 */     if ((localJavaFileObject instanceof BaseFileObject)) {
/* 146 */       return ((BaseFileObject)localJavaFileObject).getShortName();
/*     */     }
/* 148 */     return BaseFileObject.getSimpleName(localJavaFileObject);
/*     */   }
/*     */ 
/*     */   protected Collection<String> formatArguments(JCDiagnostic paramJCDiagnostic, Locale paramLocale)
/*     */   {
/* 159 */     ListBuffer localListBuffer = new ListBuffer();
/* 160 */     for (Object localObject : paramJCDiagnostic.getArgs()) {
/* 161 */       localListBuffer.append(formatArgument(paramJCDiagnostic, localObject, paramLocale));
/*     */     }
/* 163 */     return localListBuffer.toList();
/*     */   }
/*     */ 
/*     */   protected String formatArgument(JCDiagnostic paramJCDiagnostic, Object paramObject, Locale paramLocale)
/*     */   {
/* 175 */     if ((paramObject instanceof JCDiagnostic)) {
/* 176 */       String str = null;
/* 177 */       this.depth += 1;
/*     */       try {
/* 179 */         str = formatMessage((JCDiagnostic)paramObject, paramLocale);
/*     */       }
/*     */       finally {
/* 182 */         this.depth -= 1;
/*     */       }
/* 184 */       return str;
/*     */     }
/* 186 */     if ((paramObject instanceof JCTree.JCExpression)) {
/* 187 */       return expr2String((JCTree.JCExpression)paramObject);
/*     */     }
/* 189 */     if ((paramObject instanceof Iterable)) {
/* 190 */       return formatIterable(paramJCDiagnostic, (Iterable)paramObject, paramLocale);
/*     */     }
/* 192 */     if ((paramObject instanceof Type)) {
/* 193 */       return this.printer.visit((Type)paramObject, paramLocale);
/*     */     }
/* 195 */     if ((paramObject instanceof Symbol)) {
/* 196 */       return this.printer.visit((Symbol)paramObject, paramLocale);
/*     */     }
/* 198 */     if ((paramObject instanceof JavaFileObject)) {
/* 199 */       return ((JavaFileObject)paramObject).getName();
/*     */     }
/* 201 */     if ((paramObject instanceof Profile)) {
/* 202 */       return ((Profile)paramObject).name;
/*     */     }
/* 204 */     if ((paramObject instanceof Formattable)) {
/* 205 */       return ((Formattable)paramObject).toString(paramLocale, this.messages);
/*     */     }
/*     */ 
/* 208 */     return String.valueOf(paramObject);
/*     */   }
/*     */ 
/*     */   private String expr2String(JCTree.JCExpression paramJCExpression)
/*     */   {
/* 213 */     switch (2.$SwitchMap$com$sun$tools$javac$tree$JCTree$Tag[paramJCExpression.getTag().ordinal()]) {
/*     */     case 1:
/* 215 */       return expr2String(((JCTree.JCParens)paramJCExpression).expr);
/*     */     case 2:
/*     */     case 3:
/*     */     case 4:
/* 219 */       return Pretty.toSimpleString(paramJCExpression);
/*     */     }
/* 221 */     Assert.error("unexpected tree kind " + paramJCExpression.getKind());
/* 222 */     return null;
/*     */   }
/*     */ 
/*     */   protected String formatIterable(JCDiagnostic paramJCDiagnostic, Iterable<?> paramIterable, Locale paramLocale)
/*     */   {
/* 235 */     StringBuilder localStringBuilder = new StringBuilder();
/* 236 */     String str = "";
/* 237 */     for (Iterator localIterator = paramIterable.iterator(); localIterator.hasNext(); ) { Object localObject = localIterator.next();
/* 238 */       localStringBuilder.append(str);
/* 239 */       localStringBuilder.append(formatArgument(paramJCDiagnostic, localObject, paramLocale));
/* 240 */       str = ",";
/*     */     }
/* 242 */     return localStringBuilder.toString();
/*     */   }
/*     */ 
/*     */   protected List<String> formatSubdiagnostics(JCDiagnostic paramJCDiagnostic, Locale paramLocale)
/*     */   {
/* 253 */     List localList = List.nil();
/* 254 */     int i = this.config.getMultilineLimit(DiagnosticFormatter.Configuration.MultilineLimit.DEPTH);
/* 255 */     if ((i == -1) || (this.depth < i)) {
/* 256 */       this.depth += 1;
/*     */       try {
/* 258 */         j = this.config.getMultilineLimit(DiagnosticFormatter.Configuration.MultilineLimit.LENGTH);
/* 259 */         k = 0;
/* 260 */         for (JCDiagnostic localJCDiagnostic : paramJCDiagnostic.getSubdiagnostics()) {
/* 261 */           if ((j != -1) && (k >= j)) break;
/* 262 */           localList = localList.append(formatSubdiagnostic(paramJCDiagnostic, localJCDiagnostic, paramLocale));
/* 263 */           k++;
/*     */         }
/*     */       }
/*     */       finally
/*     */       {
/*     */         int j;
/*     */         int k;
/* 270 */         this.depth -= 1;
/*     */       }
/*     */     }
/* 273 */     return localList;
/*     */   }
/*     */ 
/*     */   protected String formatSubdiagnostic(JCDiagnostic paramJCDiagnostic1, JCDiagnostic paramJCDiagnostic2, Locale paramLocale)
/*     */   {
/* 285 */     return formatMessage(paramJCDiagnostic2, paramLocale);
/*     */   }
/*     */ 
/*     */   protected String formatSourceLine(JCDiagnostic paramJCDiagnostic, int paramInt)
/*     */   {
/* 292 */     StringBuilder localStringBuilder = new StringBuilder();
/* 293 */     DiagnosticSource localDiagnosticSource = paramJCDiagnostic.getDiagnosticSource();
/* 294 */     int i = paramJCDiagnostic.getIntPosition();
/* 295 */     if (paramJCDiagnostic.getIntPosition() == -1)
/* 296 */       throw new AssertionError();
/* 297 */     String str = localDiagnosticSource == null ? null : localDiagnosticSource.getLine(i);
/* 298 */     if (str == null)
/* 299 */       return "";
/* 300 */     localStringBuilder.append(indent(str, paramInt));
/* 301 */     int j = localDiagnosticSource.getColumnNumber(i, false);
/* 302 */     if (this.config.isCaretEnabled()) {
/* 303 */       localStringBuilder.append("\n");
/* 304 */       for (int k = 0; k < j - 1; k++) {
/* 305 */         localStringBuilder.append(str.charAt(k) == '\t' ? "\t" : " ");
/*     */       }
/* 307 */       localStringBuilder.append(indent("^", paramInt));
/*     */     }
/* 309 */     return localStringBuilder.toString();
/*     */   }
/*     */ 
/*     */   protected String formatLintCategory(JCDiagnostic paramJCDiagnostic, Locale paramLocale) {
/* 313 */     Lint.LintCategory localLintCategory = paramJCDiagnostic.getLintCategory();
/* 314 */     if (localLintCategory == null)
/* 315 */       return "";
/* 316 */     return localize(paramLocale, "compiler.warn.lintOption", new Object[] { localLintCategory.option });
/*     */   }
/*     */ 
/*     */   protected String localize(Locale paramLocale, String paramString, Object[] paramArrayOfObject)
/*     */   {
/* 328 */     return this.messages.getLocalizedString(paramLocale, paramString, paramArrayOfObject);
/*     */   }
/*     */ 
/*     */   public boolean displaySource(JCDiagnostic paramJCDiagnostic)
/*     */   {
/* 334 */     return (this.config.getVisible().contains(DiagnosticFormatter.Configuration.DiagnosticPart.SOURCE)) && 
/* 333 */       (paramJCDiagnostic
/* 333 */       .getType() != JCDiagnostic.DiagnosticType.FRAGMENT) && 
/* 334 */       (paramJCDiagnostic
/* 334 */       .getIntPosition() != -1);
/*     */   }
/*     */ 
/*     */   public boolean isRaw() {
/* 338 */     return false;
/*     */   }
/*     */ 
/*     */   protected String indentString(int paramInt)
/*     */   {
/* 349 */     String str = "                        ";
/* 350 */     if (paramInt <= str.length()) {
/* 351 */       return str.substring(0, paramInt);
/*     */     }
/* 353 */     StringBuilder localStringBuilder = new StringBuilder();
/* 354 */     for (int i = 0; i < paramInt; i++)
/* 355 */       localStringBuilder.append(" ");
/* 356 */     return localStringBuilder.toString();
/*     */   }
/*     */ 
/*     */   protected String indent(String paramString, int paramInt)
/*     */   {
/* 370 */     String str1 = indentString(paramInt);
/* 371 */     StringBuilder localStringBuilder = new StringBuilder();
/* 372 */     String str2 = "";
/* 373 */     for (String str3 : paramString.split("\n")) {
/* 374 */       localStringBuilder.append(str2);
/* 375 */       localStringBuilder.append(str1 + str3);
/* 376 */       str2 = "\n";
/*     */     }
/* 378 */     return localStringBuilder.toString();
/*     */   }
/*     */ 
/*     */   public SimpleConfiguration getConfiguration() {
/* 382 */     return this.config;
/*     */   }
/*     */ 
/*     */   public Printer getPrinter()
/*     */   {
/* 499 */     return this.printer;
/*     */   }
/*     */ 
/*     */   public void setPrinter(Printer paramPrinter) {
/* 503 */     this.printer = paramPrinter;
/*     */   }
/*     */ 
/*     */   public static class SimpleConfiguration
/*     */     implements DiagnosticFormatter.Configuration
/*     */   {
/*     */     protected Map<DiagnosticFormatter.Configuration.MultilineLimit, Integer> multilineLimits;
/*     */     protected EnumSet<DiagnosticFormatter.Configuration.DiagnosticPart> visibleParts;
/*     */     protected boolean caretEnabled;
/*     */ 
/*     */     public SimpleConfiguration(Set<DiagnosticFormatter.Configuration.DiagnosticPart> paramSet)
/*     */     {
/* 392 */       this.multilineLimits = new HashMap();
/* 393 */       setVisible(paramSet);
/* 394 */       setMultilineLimit(DiagnosticFormatter.Configuration.MultilineLimit.DEPTH, -1);
/* 395 */       setMultilineLimit(DiagnosticFormatter.Configuration.MultilineLimit.LENGTH, -1);
/* 396 */       setCaretEnabled(true);
/*     */     }
/*     */ 
/*     */     public SimpleConfiguration(Options paramOptions, Set<DiagnosticFormatter.Configuration.DiagnosticPart> paramSet)
/*     */     {
/* 401 */       this(paramSet);
/* 402 */       String str1 = null;
/* 403 */       if ((str1 = paramOptions.get("showSource")) != null) {
/* 404 */         if (str1.equals("true"))
/* 405 */           setVisiblePart(DiagnosticFormatter.Configuration.DiagnosticPart.SOURCE, true);
/* 406 */         else if (str1.equals("false"))
/* 407 */           setVisiblePart(DiagnosticFormatter.Configuration.DiagnosticPart.SOURCE, false);
/*     */       }
/* 409 */       String str2 = paramOptions.get("diags");
/* 410 */       if (str2 != null) {
/* 411 */         localObject = Arrays.asList(str2.split(","));
/* 412 */         if (((Collection)localObject).contains("short")) {
/* 413 */           setVisiblePart(DiagnosticFormatter.Configuration.DiagnosticPart.DETAILS, false);
/* 414 */           setVisiblePart(DiagnosticFormatter.Configuration.DiagnosticPart.SUBDIAGNOSTICS, false);
/*     */         }
/* 416 */         if (((Collection)localObject).contains("source"))
/* 417 */           setVisiblePart(DiagnosticFormatter.Configuration.DiagnosticPart.SOURCE, true);
/* 418 */         if (((Collection)localObject).contains("-source"))
/* 419 */           setVisiblePart(DiagnosticFormatter.Configuration.DiagnosticPart.SOURCE, false);
/*     */       }
/* 421 */       Object localObject = null;
/* 422 */       if ((localObject = paramOptions.get("multilinePolicy")) != null) {
/* 423 */         if (((String)localObject).equals("disabled")) {
/* 424 */           setVisiblePart(DiagnosticFormatter.Configuration.DiagnosticPart.SUBDIAGNOSTICS, false);
/* 425 */         } else if (((String)localObject).startsWith("limit:")) {
/* 426 */           str3 = ((String)localObject).substring("limit:".length());
/* 427 */           String[] arrayOfString = str3.split(":");
/*     */           try {
/* 429 */             switch (arrayOfString.length) {
/*     */             case 2:
/* 431 */               if (!arrayOfString[1].equals("*")) {
/* 432 */                 setMultilineLimit(DiagnosticFormatter.Configuration.MultilineLimit.DEPTH, Integer.parseInt(arrayOfString[1]));
/*     */               }
/*     */             case 1:
/* 435 */               if (!arrayOfString[0].equals("*"))
/* 436 */                 setMultilineLimit(DiagnosticFormatter.Configuration.MultilineLimit.LENGTH, Integer.parseInt(arrayOfString[0]));
/*     */               break;
/*     */             }
/*     */           }
/*     */           catch (NumberFormatException localNumberFormatException) {
/* 441 */             setMultilineLimit(DiagnosticFormatter.Configuration.MultilineLimit.DEPTH, -1);
/* 442 */             setMultilineLimit(DiagnosticFormatter.Configuration.MultilineLimit.LENGTH, -1);
/*     */           }
/*     */         }
/*     */       }
/* 446 */       String str3 = null;
/* 447 */       if (((str3 = paramOptions.get("showCaret")) != null) && 
/* 448 */         (str3
/* 448 */         .equals("false")))
/*     */       {
/* 449 */         setCaretEnabled(false);
/*     */       }
/* 451 */       else setCaretEnabled(true); 
/*     */     }
/*     */ 
/*     */     public int getMultilineLimit(DiagnosticFormatter.Configuration.MultilineLimit paramMultilineLimit)
/*     */     {
/* 455 */       return ((Integer)this.multilineLimits.get(paramMultilineLimit)).intValue();
/*     */     }
/*     */ 
/*     */     public EnumSet<DiagnosticFormatter.Configuration.DiagnosticPart> getVisible() {
/* 459 */       return EnumSet.copyOf(this.visibleParts);
/*     */     }
/*     */ 
/*     */     public void setMultilineLimit(DiagnosticFormatter.Configuration.MultilineLimit paramMultilineLimit, int paramInt) {
/* 463 */       this.multilineLimits.put(paramMultilineLimit, Integer.valueOf(paramInt < -1 ? -1 : paramInt));
/*     */     }
/*     */ 
/*     */     public void setVisible(Set<DiagnosticFormatter.Configuration.DiagnosticPart> paramSet)
/*     */     {
/* 468 */       this.visibleParts = EnumSet.copyOf(paramSet);
/*     */     }
/*     */ 
/*     */     public void setVisiblePart(DiagnosticFormatter.Configuration.DiagnosticPart paramDiagnosticPart, boolean paramBoolean) {
/* 472 */       if (paramBoolean)
/* 473 */         this.visibleParts.add(paramDiagnosticPart);
/*     */       else
/* 475 */         this.visibleParts.remove(paramDiagnosticPart);
/*     */     }
/*     */ 
/*     */     public void setCaretEnabled(boolean paramBoolean)
/*     */     {
/* 485 */       this.caretEnabled = paramBoolean;
/*     */     }
/*     */ 
/*     */     public boolean isCaretEnabled()
/*     */     {
/* 494 */       return this.caretEnabled;
/*     */     }
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.javac.util.AbstractDiagnosticFormatter
 * JD-Core Version:    0.6.2
 */