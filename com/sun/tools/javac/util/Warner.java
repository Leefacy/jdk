/*    */ package com.sun.tools.javac.util;
/*    */ 
/*    */ import com.sun.tools.javac.code.Lint.LintCategory;
/*    */ import java.util.EnumSet;
/*    */ 
/*    */ public class Warner
/*    */ {
/* 43 */   private JCDiagnostic.DiagnosticPosition pos = null;
/* 44 */   protected boolean warned = false;
/* 45 */   private EnumSet<Lint.LintCategory> nonSilentLintSet = EnumSet.noneOf(Lint.LintCategory.class);
/* 46 */   private EnumSet<Lint.LintCategory> silentLintSet = EnumSet.noneOf(Lint.LintCategory.class);
/*    */ 
/*    */   public JCDiagnostic.DiagnosticPosition pos() {
/* 49 */     return this.pos;
/*    */   }
/*    */ 
/*    */   public void warn(Lint.LintCategory paramLintCategory) {
/* 53 */     this.nonSilentLintSet.add(paramLintCategory);
/*    */   }
/*    */ 
/*    */   public void silentWarn(Lint.LintCategory paramLintCategory) {
/* 57 */     this.silentLintSet.add(paramLintCategory);
/*    */   }
/*    */ 
/*    */   public Warner(JCDiagnostic.DiagnosticPosition paramDiagnosticPosition) {
/* 61 */     this.pos = paramDiagnosticPosition;
/*    */   }
/*    */ 
/*    */   public boolean hasSilentLint(Lint.LintCategory paramLintCategory) {
/* 65 */     return this.silentLintSet.contains(paramLintCategory);
/*    */   }
/*    */ 
/*    */   public boolean hasNonSilentLint(Lint.LintCategory paramLintCategory) {
/* 69 */     return this.nonSilentLintSet.contains(paramLintCategory);
/*    */   }
/*    */ 
/*    */   public boolean hasLint(Lint.LintCategory paramLintCategory)
/*    */   {
/* 74 */     return (hasSilentLint(paramLintCategory)) || 
/* 74 */       (hasNonSilentLint(paramLintCategory));
/*    */   }
/*    */ 
/*    */   public void clear()
/*    */   {
/* 78 */     this.nonSilentLintSet.clear();
/* 79 */     this.silentLintSet.clear();
/* 80 */     this.warned = false;
/*    */   }
/*    */ 
/*    */   public Warner() {
/* 84 */     this(null);
/*    */   }
/*    */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.javac.util.Warner
 * JD-Core Version:    0.6.2
 */