/*     */ package com.sun.tools.javac.util;
/*     */ 
/*     */ import com.sun.tools.javac.api.DiagnosticFormatter;
/*     */ import com.sun.tools.javac.api.DiagnosticFormatter.Configuration;
/*     */ import com.sun.tools.javac.api.DiagnosticFormatter.Configuration.DiagnosticPart;
/*     */ import com.sun.tools.javac.api.DiagnosticFormatter.Configuration.MultilineLimit;
/*     */ import com.sun.tools.javac.api.DiagnosticFormatter.PositionKind;
/*     */ import java.util.Locale;
/*     */ import java.util.Set;
/*     */ import javax.tools.Diagnostic;
/*     */ 
/*     */ public class ForwardingDiagnosticFormatter<D extends Diagnostic<?>, F extends DiagnosticFormatter<D>>
/*     */   implements DiagnosticFormatter<D>
/*     */ {
/*     */   protected F formatter;
/*     */   protected ForwardingConfiguration configuration;
/*     */ 
/*     */   public ForwardingDiagnosticFormatter(F paramF)
/*     */   {
/*  60 */     this.formatter = paramF;
/*  61 */     this.configuration = new ForwardingConfiguration(paramF.getConfiguration());
/*     */   }
/*     */ 
/*     */   public F getDelegatedFormatter()
/*     */   {
/*  69 */     return this.formatter;
/*     */   }
/*     */ 
/*     */   public DiagnosticFormatter.Configuration getConfiguration() {
/*  73 */     return this.configuration;
/*     */   }
/*     */ 
/*     */   public boolean displaySource(D paramD) {
/*  77 */     return this.formatter.displaySource(paramD);
/*     */   }
/*     */ 
/*     */   public String format(D paramD, Locale paramLocale) {
/*  81 */     return this.formatter.format(paramD, paramLocale);
/*     */   }
/*     */ 
/*     */   public String formatKind(D paramD, Locale paramLocale) {
/*  85 */     return this.formatter.formatKind(paramD, paramLocale);
/*     */   }
/*     */ 
/*     */   public String formatMessage(D paramD, Locale paramLocale) {
/*  89 */     return this.formatter.formatMessage(paramD, paramLocale);
/*     */   }
/*     */ 
/*     */   public String formatPosition(D paramD, DiagnosticFormatter.PositionKind paramPositionKind, Locale paramLocale) {
/*  93 */     return this.formatter.formatPosition(paramD, paramPositionKind, paramLocale);
/*     */   }
/*     */ 
/*     */   public String formatSource(D paramD, boolean paramBoolean, Locale paramLocale) {
/*  97 */     return this.formatter.formatSource(paramD, paramBoolean, paramLocale);
/*     */   }
/*     */ 
/*     */   public static class ForwardingConfiguration
/*     */     implements DiagnosticFormatter.Configuration
/*     */   {
/*     */     protected DiagnosticFormatter.Configuration configuration;
/*     */ 
/*     */     public ForwardingConfiguration(DiagnosticFormatter.Configuration paramConfiguration)
/*     */     {
/* 110 */       this.configuration = paramConfiguration;
/*     */     }
/*     */ 
/*     */     public DiagnosticFormatter.Configuration getDelegatedConfiguration()
/*     */     {
/* 118 */       return this.configuration;
/*     */     }
/*     */ 
/*     */     public int getMultilineLimit(DiagnosticFormatter.Configuration.MultilineLimit paramMultilineLimit) {
/* 122 */       return this.configuration.getMultilineLimit(paramMultilineLimit);
/*     */     }
/*     */ 
/*     */     public Set<DiagnosticFormatter.Configuration.DiagnosticPart> getVisible() {
/* 126 */       return this.configuration.getVisible();
/*     */     }
/*     */ 
/*     */     public void setMultilineLimit(DiagnosticFormatter.Configuration.MultilineLimit paramMultilineLimit, int paramInt) {
/* 130 */       this.configuration.setMultilineLimit(paramMultilineLimit, paramInt);
/*     */     }
/*     */ 
/*     */     public void setVisible(Set<DiagnosticFormatter.Configuration.DiagnosticPart> paramSet) {
/* 134 */       this.configuration.setVisible(paramSet);
/*     */     }
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.javac.util.ForwardingDiagnosticFormatter
 * JD-Core Version:    0.6.2
 */