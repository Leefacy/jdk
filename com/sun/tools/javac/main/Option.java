/*     */ package com.sun.tools.javac.main;
/*     */ 
/*     */ import com.sun.tools.doclint.DocLint;
/*     */ import com.sun.tools.javac.code.Lint.LintCategory;
/*     */ import com.sun.tools.javac.code.Source;
/*     */ import com.sun.tools.javac.jvm.Profile;
/*     */ import com.sun.tools.javac.jvm.Target;
/*     */ import com.sun.tools.javac.processing.JavacProcessingEnvironment;
/*     */ import com.sun.tools.javac.util.Log;
/*     */ import com.sun.tools.javac.util.Log.PrefixKind;
/*     */ import com.sun.tools.javac.util.Log.WriterKind;
/*     */ import com.sun.tools.javac.util.Options;
/*     */ import com.sun.tools.javac.util.StringUtils;
/*     */ import java.io.File;
/*     */ import java.io.FileWriter;
/*     */ import java.io.IOException;
/*     */ import java.io.PrintWriter;
/*     */ import java.util.Collections;
/*     */ import java.util.EnumSet;
/*     */ import java.util.Iterator;
/*     */ import java.util.LinkedHashMap;
/*     */ import java.util.Map;
/*     */ import java.util.Map.Entry;
/*     */ import java.util.Set;
/*     */ import javax.lang.model.SourceVersion;
/*     */ 
/*     */ public enum Option
/*     */ {
/*  68 */   G("-g", "opt.g", OptionKind.STANDARD, OptionGroup.BASIC), 
/*     */ 
/*  70 */   G_NONE("-g:none", "opt.g.none", OptionKind.STANDARD, OptionGroup.BASIC), 
/*     */ 
/*  78 */   G_CUSTOM("-g:", "opt.g.lines.vars.source", OptionKind.STANDARD, OptionGroup.BASIC, ChoiceKind.ANYOF, new String[] { "lines", "vars", "source" }), 
/*     */ 
/*  81 */   XLINT("-Xlint", "opt.Xlint", OptionKind.EXTENDED, OptionGroup.BASIC), 
/*     */ 
/*  83 */   XLINT_CUSTOM("-Xlint:", "opt.Xlint.suboptlist", OptionKind.EXTENDED, OptionGroup.BASIC, ChoiceKind.ANYOF, 
/*  84 */     getXLintChoices()), 
/*     */ 
/*  86 */   XDOCLINT("-Xdoclint", "opt.Xdoclint", OptionKind.EXTENDED, OptionGroup.BASIC), 
/*     */ 
/*  88 */   XDOCLINT_CUSTOM("-Xdoclint:", "opt.Xdoclint.subopts", "opt.Xdoclint.custom", OptionKind.EXTENDED, OptionGroup.BASIC), 
/*     */ 
/* 105 */   NOWARN("-nowarn", "opt.nowarn", OptionKind.STANDARD, OptionGroup.BASIC), 
/*     */ 
/* 113 */   VERBOSE("-verbose", "opt.verbose", OptionKind.STANDARD, OptionGroup.BASIC), 
/*     */ 
/* 116 */   DEPRECATION("-deprecation", "opt.deprecation", OptionKind.STANDARD, OptionGroup.BASIC), 
/*     */ 
/* 124 */   CLASSPATH("-classpath", "opt.arg.path", "opt.classpath", OptionKind.STANDARD, OptionGroup.FILEMANAGER), 
/*     */ 
/* 126 */   CP("-cp", "opt.arg.path", "opt.classpath", OptionKind.STANDARD, OptionGroup.FILEMANAGER), 
/*     */ 
/* 133 */   SOURCEPATH("-sourcepath", "opt.arg.path", "opt.sourcepath", OptionKind.STANDARD, OptionGroup.FILEMANAGER), 
/*     */ 
/* 135 */   BOOTCLASSPATH("-bootclasspath", "opt.arg.path", "opt.bootclasspath", OptionKind.STANDARD, OptionGroup.FILEMANAGER), 
/*     */ 
/* 144 */   XBOOTCLASSPATH_PREPEND("-Xbootclasspath/p:", "opt.arg.path", "opt.Xbootclasspath.p", OptionKind.EXTENDED, OptionGroup.FILEMANAGER), 
/*     */ 
/* 146 */   XBOOTCLASSPATH_APPEND("-Xbootclasspath/a:", "opt.arg.path", "opt.Xbootclasspath.a", OptionKind.EXTENDED, OptionGroup.FILEMANAGER), 
/*     */ 
/* 148 */   XBOOTCLASSPATH("-Xbootclasspath:", "opt.arg.path", "opt.bootclasspath", OptionKind.EXTENDED, OptionGroup.FILEMANAGER), 
/*     */ 
/* 157 */   EXTDIRS("-extdirs", "opt.arg.dirs", "opt.extdirs", OptionKind.STANDARD, OptionGroup.FILEMANAGER), 
/*     */ 
/* 159 */   DJAVA_EXT_DIRS("-Djava.ext.dirs=", "opt.arg.dirs", "opt.extdirs", OptionKind.EXTENDED, OptionGroup.FILEMANAGER), 
/*     */ 
/* 166 */   ENDORSEDDIRS("-endorseddirs", "opt.arg.dirs", "opt.endorseddirs", OptionKind.STANDARD, OptionGroup.FILEMANAGER), 
/*     */ 
/* 168 */   DJAVA_ENDORSED_DIRS("-Djava.endorsed.dirs=", "opt.arg.dirs", "opt.endorseddirs", OptionKind.EXTENDED, OptionGroup.FILEMANAGER), 
/*     */ 
/* 175 */   PROC("-proc:", "opt.proc.none.only", OptionKind.STANDARD, OptionGroup.BASIC, ChoiceKind.ONEOF, new String[] { "none", "only" }), 
/*     */ 
/* 177 */   PROCESSOR("-processor", "opt.arg.class.list", "opt.processor", OptionKind.STANDARD, OptionGroup.BASIC), 
/*     */ 
/* 179 */   PROCESSORPATH("-processorpath", "opt.arg.path", "opt.processorpath", OptionKind.STANDARD, OptionGroup.FILEMANAGER), 
/*     */ 
/* 181 */   PARAMETERS("-parameters", "opt.parameters", OptionKind.STANDARD, OptionGroup.BASIC), 
/*     */ 
/* 183 */   D("-d", "opt.arg.directory", "opt.d", OptionKind.STANDARD, OptionGroup.FILEMANAGER), 
/*     */ 
/* 185 */   S("-s", "opt.arg.directory", "opt.sourceDest", OptionKind.STANDARD, OptionGroup.FILEMANAGER), 
/*     */ 
/* 187 */   H("-h", "opt.arg.directory", "opt.headerDest", OptionKind.STANDARD, OptionGroup.FILEMANAGER), 
/*     */ 
/* 189 */   IMPLICIT("-implicit:", "opt.implicit", OptionKind.STANDARD, OptionGroup.BASIC, ChoiceKind.ONEOF, new String[] { "none", "class" }), 
/*     */ 
/* 191 */   ENCODING("-encoding", "opt.arg.encoding", "opt.encoding", OptionKind.STANDARD, OptionGroup.FILEMANAGER), 
/*     */ 
/* 199 */   SOURCE("-source", "opt.arg.release", "opt.source", OptionKind.STANDARD, OptionGroup.BASIC), 
/*     */ 
/* 211 */   TARGET("-target", "opt.arg.release", "opt.target", OptionKind.STANDARD, OptionGroup.BASIC), 
/*     */ 
/* 223 */   PROFILE("-profile", "opt.arg.profile", "opt.profile", OptionKind.STANDARD, OptionGroup.BASIC), 
/*     */ 
/* 235 */   VERSION("-version", "opt.version", OptionKind.STANDARD, OptionGroup.INFO), 
/*     */ 
/* 245 */   FULLVERSION("-fullversion", null, OptionKind.HIDDEN, OptionGroup.INFO), 
/*     */ 
/* 255 */   DIAGS("-XDdiags=", null, OptionKind.HIDDEN, OptionGroup.INFO), 
/*     */ 
/* 270 */   HELP("-help", "opt.help", OptionKind.STANDARD, OptionGroup.INFO), 
/*     */ 
/* 284 */   A("-A", "opt.arg.key.equals.value", "opt.A", OptionKind.STANDARD, OptionGroup.BASIC, true), 
/*     */ 
/* 313 */   X("-X", "opt.X", OptionKind.STANDARD, OptionGroup.INFO), 
/*     */ 
/* 328 */   J("-J", "opt.arg.flag", "opt.J", OptionKind.STANDARD, OptionGroup.INFO, true), 
/*     */ 
/* 336 */   MOREINFO("-moreinfo", null, OptionKind.HIDDEN, OptionGroup.BASIC), 
/*     */ 
/* 345 */   WERROR("-Werror", "opt.Werror", OptionKind.STANDARD, OptionGroup.BASIC), 
/*     */ 
/* 349 */   PROMPT("-prompt", null, OptionKind.HIDDEN, OptionGroup.BASIC), 
/*     */ 
/* 352 */   DOE("-doe", null, OptionKind.HIDDEN, OptionGroup.BASIC), 
/*     */ 
/* 355 */   PRINTSOURCE("-printsource", null, OptionKind.HIDDEN, OptionGroup.BASIC), 
/*     */ 
/* 358 */   WARNUNCHECKED("-warnunchecked", null, OptionKind.HIDDEN, OptionGroup.BASIC), 
/*     */ 
/* 366 */   XMAXERRS("-Xmaxerrs", "opt.arg.number", "opt.maxerrs", OptionKind.EXTENDED, OptionGroup.BASIC), 
/*     */ 
/* 368 */   XMAXWARNS("-Xmaxwarns", "opt.arg.number", "opt.maxwarns", OptionKind.EXTENDED, OptionGroup.BASIC), 
/*     */ 
/* 370 */   XSTDOUT("-Xstdout", "opt.arg.file", "opt.Xstdout", OptionKind.EXTENDED, OptionGroup.INFO), 
/*     */ 
/* 385 */   XPRINT("-Xprint", "opt.print", OptionKind.EXTENDED, OptionGroup.BASIC), 
/*     */ 
/* 387 */   XPRINTROUNDS("-XprintRounds", "opt.printRounds", OptionKind.EXTENDED, OptionGroup.BASIC), 
/*     */ 
/* 389 */   XPRINTPROCESSORINFO("-XprintProcessorInfo", "opt.printProcessorInfo", OptionKind.EXTENDED, OptionGroup.BASIC), 
/*     */ 
/* 391 */   XPREFER("-Xprefer:", "opt.prefer", OptionKind.EXTENDED, OptionGroup.BASIC, ChoiceKind.ONEOF, new String[] { "source", "newer" }), 
/*     */ 
/* 394 */   XPKGINFO("-Xpkginfo:", "opt.pkginfo", OptionKind.EXTENDED, OptionGroup.BASIC, ChoiceKind.ONEOF, new String[] { "always", "legacy", "nonempty" }), 
/*     */ 
/* 397 */   O("-O", null, OptionKind.HIDDEN, OptionGroup.BASIC), 
/*     */ 
/* 400 */   XJCOV("-Xjcov", null, OptionKind.HIDDEN, OptionGroup.BASIC), 
/*     */ 
/* 402 */   PLUGIN("-Xplugin:", "opt.arg.plugin", "opt.plugin", OptionKind.EXTENDED, OptionGroup.BASIC), 
/*     */ 
/* 412 */   XDIAGS("-Xdiags:", "opt.diags", OptionKind.EXTENDED, OptionGroup.BASIC, ChoiceKind.ONEOF, new String[] { "compact", "verbose" }), 
/*     */ 
/* 418 */   XD("-XD", null, OptionKind.HIDDEN, OptionGroup.BASIC), 
/*     */ 
/* 436 */   AT("@", "opt.arg.file", "opt.AT", OptionKind.STANDARD, OptionGroup.INFO, true), 
/*     */ 
/* 451 */   SOURCEFILE("sourcefile", null, OptionKind.HIDDEN, OptionGroup.INFO);
/*     */ 
/*     */   public final String text;
/*     */   final OptionKind kind;
/*     */   final OptionGroup group;
/*     */   final String argsNameKey;
/*     */   final String descrKey;
/*     */   final boolean hasSuffix;
/*     */   final ChoiceKind choiceKind;
/*     */   final Map<String, Boolean> choices;
/*     */ 
/*     */   private Option(String paramString1, String paramString2, OptionKind paramOptionKind, OptionGroup paramOptionGroup)
/*     */   {
/* 540 */     this(paramString1, null, paramString2, paramOptionKind, paramOptionGroup, null, null, false);
/*     */   }
/*     */ 
/*     */   private Option(String paramString1, String paramString2, String paramString3, OptionKind paramOptionKind, OptionGroup paramOptionGroup)
/*     */   {
/* 545 */     this(paramString1, paramString2, paramString3, paramOptionKind, paramOptionGroup, null, null, false);
/*     */   }
/*     */ 
/*     */   private Option(String paramString1, String paramString2, String paramString3, OptionKind paramOptionKind, OptionGroup paramOptionGroup, boolean paramBoolean)
/*     */   {
/* 550 */     this(paramString1, paramString2, paramString3, paramOptionKind, paramOptionGroup, null, null, paramBoolean);
/*     */   }
/*     */ 
/*     */   private Option(String paramString1, String paramString2, OptionKind paramOptionKind, OptionGroup paramOptionGroup, ChoiceKind paramChoiceKind, Map<String, Boolean> paramMap)
/*     */   {
/* 556 */     this(paramString1, null, paramString2, paramOptionKind, paramOptionGroup, paramChoiceKind, paramMap, false);
/*     */   }
/*     */ 
/*     */   private Option(String paramString1, String paramString2, OptionKind paramOptionKind, OptionGroup paramOptionGroup, ChoiceKind paramChoiceKind, String[] paramArrayOfString)
/*     */   {
/* 562 */     this(paramString1, null, paramString2, paramOptionKind, paramOptionGroup, paramChoiceKind, 
/* 563 */       createChoices(paramArrayOfString), 
/* 563 */       false);
/*     */   }
/*     */ 
/*     */   private static Map<String, Boolean> createChoices(String[] paramArrayOfString) {
/* 567 */     LinkedHashMap localLinkedHashMap = new LinkedHashMap();
/* 568 */     for (String str : paramArrayOfString)
/* 569 */       localLinkedHashMap.put(str, Boolean.valueOf(false));
/* 570 */     return localLinkedHashMap;
/*     */   }
/*     */ 
/*     */   private Option(String paramString1, String paramString2, String paramString3, OptionKind paramOptionKind, OptionGroup paramOptionGroup, ChoiceKind paramChoiceKind, Map<String, Boolean> paramMap, boolean paramBoolean)
/*     */   {
/* 577 */     this.text = paramString1;
/* 578 */     this.argsNameKey = paramString2;
/* 579 */     this.descrKey = paramString3;
/* 580 */     this.kind = paramOptionKind;
/* 581 */     this.group = paramOptionGroup;
/* 582 */     this.choiceKind = paramChoiceKind;
/* 583 */     this.choices = paramMap;
/* 584 */     int i = paramString1.charAt(paramString1.length() - 1);
/* 585 */     this.hasSuffix = ((paramBoolean) || (i == 58) || (i == 61));
/*     */   }
/*     */ 
/*     */   public String getText() {
/* 589 */     return this.text;
/*     */   }
/*     */ 
/*     */   public OptionKind getKind() {
/* 593 */     return this.kind;
/*     */   }
/*     */ 
/*     */   public boolean hasArg() {
/* 597 */     return (this.argsNameKey != null) && (!this.hasSuffix);
/*     */   }
/*     */ 
/*     */   public boolean matches(String paramString) {
/* 601 */     if (!this.hasSuffix) {
/* 602 */       return paramString.equals(this.text);
/*     */     }
/* 604 */     if (!paramString.startsWith(this.text)) {
/* 605 */       return false;
/*     */     }
/* 607 */     if (this.choices != null) {
/* 608 */       String str1 = paramString.substring(this.text.length());
/* 609 */       if (this.choiceKind == ChoiceKind.ONEOF) {
/* 610 */         return this.choices.keySet().contains(str1);
/*     */       }
/* 612 */       for (String str2 : str1.split(",+")) {
/* 613 */         if (!this.choices.keySet().contains(str2)) {
/* 614 */           return false;
/*     */         }
/*     */       }
/*     */     }
/*     */ 
/* 619 */     return true;
/*     */   }
/*     */ 
/*     */   public boolean process(OptionHelper paramOptionHelper, String paramString1, String paramString2) {
/* 623 */     if (this.choices != null)
/*     */     {
/*     */       Object localObject;
/* 624 */       if (this.choiceKind == ChoiceKind.ONEOF)
/*     */       {
/* 626 */         for (localObject = this.choices.keySet().iterator(); ((Iterator)localObject).hasNext(); ) { str1 = (String)((Iterator)localObject).next();
/* 627 */           paramOptionHelper.remove(paramString1 + str1); }
/* 628 */         localObject = paramString1 + paramString2;
/* 629 */         paramOptionHelper.put((String)localObject, (String)localObject);
/*     */ 
/* 632 */         String str1 = paramString1.substring(0, paramString1.length() - 1);
/* 633 */         paramOptionHelper.put(str1, paramString2);
/*     */       }
/*     */       else {
/* 636 */         for (String str2 : paramString2.split(",+")) {
/* 637 */           String str3 = paramString1 + str2;
/* 638 */           paramOptionHelper.put(str3, str3);
/*     */         }
/*     */       }
/*     */     }
/* 642 */     paramOptionHelper.put(paramString1, paramString2);
/* 643 */     return false;
/*     */   }
/*     */ 
/*     */   public boolean process(OptionHelper paramOptionHelper, String paramString) {
/* 647 */     if (this.hasSuffix) {
/* 648 */       return process(paramOptionHelper, this.text, paramString.substring(this.text.length()));
/*     */     }
/* 650 */     return process(paramOptionHelper, paramString, paramString);
/*     */   }
/*     */ 
/*     */   void help(Log paramLog, OptionKind paramOptionKind) {
/* 654 */     if (this.kind != paramOptionKind) {
/* 655 */       return;
/*     */     }
/* 657 */     paramLog.printRawLines(Log.WriterKind.NOTICE, 
/* 658 */       String.format("  %-26s %s", new Object[] { 
/* 659 */       helpSynopsis(paramLog), 
/* 659 */       paramLog
/* 660 */       .localize(Log.PrefixKind.JAVAC, this.descrKey, new Object[0]) }));
/*     */   }
/*     */ 
/*     */   private String helpSynopsis(Log paramLog)
/*     */   {
/* 665 */     StringBuilder localStringBuilder = new StringBuilder();
/* 666 */     localStringBuilder.append(this.text);
/* 667 */     if (this.argsNameKey == null) {
/* 668 */       if (this.choices != null) {
/* 669 */         String str = "{";
/* 670 */         for (Map.Entry localEntry : this.choices.entrySet()) {
/* 671 */           if (!((Boolean)localEntry.getValue()).booleanValue()) {
/* 672 */             localStringBuilder.append(str);
/* 673 */             localStringBuilder.append((String)localEntry.getKey());
/* 674 */             str = ",";
/*     */           }
/*     */         }
/* 677 */         localStringBuilder.append("}");
/*     */       }
/*     */     } else {
/* 680 */       if (!this.hasSuffix)
/* 681 */         localStringBuilder.append(" ");
/* 682 */       localStringBuilder.append(paramLog.localize(Log.PrefixKind.JAVAC, this.argsNameKey, new Object[0]));
/*     */     }
/*     */ 
/* 686 */     return localStringBuilder.toString();
/*     */   }
/*     */ 
/*     */   private static Map<String, Boolean> getXLintChoices()
/*     */   {
/* 722 */     LinkedHashMap localLinkedHashMap = new LinkedHashMap();
/* 723 */     localLinkedHashMap.put("all", Boolean.valueOf(false));
/*     */     Lint.LintCategory localLintCategory;
/* 724 */     for (localLintCategory : Lint.LintCategory.values())
/* 725 */       localLinkedHashMap.put(localLintCategory.option, Boolean.valueOf(localLintCategory.hidden));
/* 726 */     for (localLintCategory : Lint.LintCategory.values())
/* 727 */       localLinkedHashMap.put("-" + localLintCategory.option, Boolean.valueOf(localLintCategory.hidden));
/* 728 */     localLinkedHashMap.put("none", Boolean.valueOf(false));
/* 729 */     return localLinkedHashMap;
/*     */   }
/*     */ 
/*     */   static Set<Option> getJavaCompilerOptions() {
/* 733 */     return EnumSet.allOf(Option.class);
/*     */   }
/*     */ 
/*     */   public static Set<Option> getJavacFileManagerOptions() {
/* 737 */     return getOptions(EnumSet.of(OptionGroup.FILEMANAGER));
/*     */   }
/*     */ 
/*     */   public static Set<Option> getJavacToolOptions() {
/* 741 */     return getOptions(EnumSet.of(OptionGroup.BASIC));
/*     */   }
/*     */ 
/*     */   static Set<Option> getOptions(Set<OptionGroup> paramSet) {
/* 745 */     EnumSet localEnumSet = EnumSet.noneOf(Option.class);
/* 746 */     for (Option localOption : values())
/* 747 */       if (paramSet.contains(localOption.group))
/* 748 */         localEnumSet.add(localOption);
/* 749 */     return Collections.unmodifiableSet(localEnumSet);
/*     */   }
/*     */ 
/*     */   static enum ChoiceKind
/*     */   {
/* 505 */     ONEOF, 
/*     */ 
/* 507 */     ANYOF;
/*     */   }
/*     */ 
/*     */   static enum OptionGroup
/*     */   {
/* 492 */     BASIC, 
/*     */ 
/* 495 */     FILEMANAGER, 
/*     */ 
/* 497 */     INFO, 
/*     */ 
/* 499 */     OPERAND;
/*     */   }
/*     */ 
/*     */   public static enum OptionKind
/*     */   {
/* 480 */     STANDARD, 
/*     */ 
/* 482 */     EXTENDED, 
/*     */ 
/* 484 */     HIDDEN;
/*     */   }
/*     */ 
/*     */   public static enum PkgInfo
/*     */   {
/* 698 */     ALWAYS, 
/*     */ 
/* 706 */     LEGACY, 
/*     */ 
/* 711 */     NONEMPTY;
/*     */ 
/*     */     public static PkgInfo get(Options paramOptions) {
/* 714 */       String str = paramOptions.get(Option.XPKGINFO);
/*     */ 
/* 717 */       return str == null ? LEGACY : 
/* 717 */         valueOf(StringUtils.toUpperCase(str));
/*     */     }
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.javac.main.Option
 * JD-Core Version:    0.6.2
 */