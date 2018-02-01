/*     */ package com.sun.tools.javadoc;
/*     */ 
/*     */ import com.sun.tools.javac.util.ListBuffer;
/*     */ import com.sun.tools.javac.util.Options;
/*     */ import java.util.StringTokenizer;
/*     */ 
/*     */ public enum ToolOption
/*     */ {
/*  45 */   BOOTCLASSPATH("-bootclasspath", true), 
/*     */ 
/*  52 */   CLASSPATH("-classpath", true), 
/*     */ 
/*  59 */   CP("-cp", true), 
/*     */ 
/*  66 */   EXTDIRS("-extdirs", true), 
/*     */ 
/*  73 */   SOURCEPATH("-sourcepath", true), 
/*     */ 
/*  80 */   SYSCLASSPATH("-sysclasspath", true), 
/*     */ 
/*  87 */   ENCODING("-encoding", true), 
/*     */ 
/*  95 */   SOURCE("-source", true), 
/*     */ 
/* 102 */   XMAXERRS("-Xmaxerrs", true), 
/*     */ 
/* 109 */   XMAXWARNS("-Xmaxwarns", true), 
/*     */ 
/* 118 */   DOCLET("-doclet", true), 
/*     */ 
/* 120 */   DOCLETPATH("-docletpath", true), 
/*     */ 
/* 124 */   SUBPACKAGES("-subpackages", true), 
/*     */ 
/* 131 */   EXCLUDE("-exclude", true), 
/*     */ 
/* 140 */   PACKAGE("-package"), 
/*     */ 
/* 148 */   PRIVATE("-private"), 
/*     */ 
/* 155 */   PROTECTED("-protected"), 
/*     */ 
/* 162 */   PUBLIC("-public"), 
/*     */ 
/* 171 */   PROMPT("-prompt"), 
/*     */ 
/* 179 */   QUIET("-quiet"), 
/*     */ 
/* 186 */   VERBOSE("-verbose"), 
/*     */ 
/* 193 */   XWERROR("-Xwerror"), 
/*     */ 
/* 203 */   BREAKITERATOR("-breakiterator"), 
/*     */ 
/* 210 */   LOCALE("-locale", true), 
/*     */ 
/* 217 */   OVERVIEW("-overview", true), 
/*     */ 
/* 219 */   XCLASSES("-Xclasses"), 
/*     */ 
/* 229 */   HELP("-help"), 
/*     */ 
/* 236 */   X("-X");
/*     */ 
/*     */   public final String opt;
/*     */   public final boolean hasArg;
/*     */ 
/*     */   private ToolOption(String paramString)
/*     */   {
/* 247 */     this(paramString, false);
/*     */   }
/*     */ 
/*     */   private ToolOption(String paramString, boolean paramBoolean) {
/* 251 */     this.opt = paramString;
/* 252 */     this.hasArg = paramBoolean;
/*     */   }
/*     */   void process(Helper paramHelper, String paramString) {
/*     */   }
/*     */   void process(Helper paramHelper) {
/*     */   }
/*     */ 
/*     */   static ToolOption get(String paramString) {
/* 260 */     for (ToolOption localToolOption : values()) {
/* 261 */       if (paramString.equals(localToolOption.opt))
/* 262 */         return localToolOption;
/*     */     }
/* 264 */     return null;
/*     */   }
/*     */ 
/*     */   static abstract class Helper
/*     */   {
/* 269 */     final ListBuffer<String[]> options = new ListBuffer();
/*     */ 
/* 272 */     final ListBuffer<String> subPackages = new ListBuffer();
/*     */ 
/* 275 */     final ListBuffer<String> excludedPackages = new ListBuffer();
/*     */     Options compOpts;
/* 281 */     String encoding = null;
/*     */ 
/* 284 */     boolean breakiterator = false;
/*     */ 
/* 287 */     boolean quiet = false;
/*     */ 
/* 290 */     boolean docClasses = false;
/*     */ 
/* 293 */     boolean rejectWarnings = false;
/*     */     boolean promptOnError;
/* 299 */     String docLocale = "";
/*     */ 
/* 302 */     ModifierFilter showAccess = null;
/*     */ 
/*     */     abstract void usage();
/*     */ 
/*     */     abstract void Xusage();
/*     */ 
/*     */     abstract void usageError(String paramString, Object[] paramArrayOfObject);
/*     */ 
/* 310 */     protected void addToList(ListBuffer<String> paramListBuffer, String paramString) { StringTokenizer localStringTokenizer = new StringTokenizer(paramString, ":");
/*     */ 
/* 312 */       while (localStringTokenizer.hasMoreTokens()) {
/* 313 */         String str = localStringTokenizer.nextToken();
/* 314 */         paramListBuffer.append(str);
/*     */       } }
/*     */ 
/*     */     protected void setFilter(long paramLong)
/*     */     {
/* 319 */       if (this.showAccess != null) {
/* 320 */         usageError("main.incompatible.access.flags", new Object[0]);
/*     */       }
/* 322 */       this.showAccess = new ModifierFilter(paramLong);
/*     */     }
/*     */ 
/*     */     private void setCompilerOpt(String paramString1, String paramString2) {
/* 326 */       if (this.compOpts.get(paramString1) != null) {
/* 327 */         usageError("main.option.already.seen", new Object[] { paramString1 });
/*     */       }
/* 329 */       this.compOpts.put(paramString1, paramString2);
/*     */     }
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.javadoc.ToolOption
 * JD-Core Version:    0.6.2
 */