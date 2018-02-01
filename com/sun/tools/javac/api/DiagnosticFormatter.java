/*     */ package com.sun.tools.javac.api;
/*     */ 
/*     */ import java.util.Locale;
/*     */ import java.util.Set;
/*     */ import javax.tools.Diagnostic;
/*     */ 
/*     */ public abstract interface DiagnosticFormatter<D extends Diagnostic<?>>
/*     */ {
/*     */   public abstract boolean displaySource(D paramD);
/*     */ 
/*     */   public abstract String format(D paramD, Locale paramLocale);
/*     */ 
/*     */   public abstract String formatMessage(D paramD, Locale paramLocale);
/*     */ 
/*     */   public abstract String formatKind(D paramD, Locale paramLocale);
/*     */ 
/*     */   public abstract String formatSource(D paramD, boolean paramBoolean, Locale paramLocale);
/*     */ 
/*     */   public abstract String formatPosition(D paramD, PositionKind paramPositionKind, Locale paramLocale);
/*     */ 
/*     */   public abstract Configuration getConfiguration();
/*     */ 
/*     */   public static abstract interface Configuration
/*     */   {
/*     */     public abstract void setVisible(Set<DiagnosticPart> paramSet);
/*     */ 
/*     */     public abstract Set<DiagnosticPart> getVisible();
/*     */ 
/*     */     public abstract void setMultilineLimit(MultilineLimit paramMultilineLimit, int paramInt);
/*     */ 
/*     */     public abstract int getMultilineLimit(MultilineLimit paramMultilineLimit);
/*     */ 
/*     */     public static enum DiagnosticPart
/*     */     {
/* 162 */       SUMMARY, 
/*     */ 
/* 167 */       DETAILS, 
/*     */ 
/* 171 */       SOURCE, 
/*     */ 
/* 175 */       SUBDIAGNOSTICS, 
/*     */ 
/* 179 */       JLS;
/*     */     }
/*     */ 
/*     */     public static enum MultilineLimit
/*     */     {
/* 210 */       DEPTH, 
/*     */ 
/* 215 */       LENGTH;
/*     */     }
/*     */   }
/*     */ 
/*     */   public static enum PositionKind
/*     */   {
/* 107 */     START, 
/*     */ 
/* 111 */     END, 
/*     */ 
/* 115 */     LINE, 
/*     */ 
/* 119 */     COLUMN, 
/*     */ 
/* 123 */     OFFSET;
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.javac.api.DiagnosticFormatter
 * JD-Core Version:    0.6.2
 */