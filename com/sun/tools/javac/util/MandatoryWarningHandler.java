/*     */ package com.sun.tools.javac.util;
/*     */ 
/*     */ import com.sun.tools.javac.code.Lint.LintCategory;
/*     */ import java.util.HashSet;
/*     */ import java.util.Set;
/*     */ import javax.tools.JavaFileObject;
/*     */ 
/*     */ public class MandatoryWarningHandler
/*     */ {
/*     */   private Log log;
/*     */   private boolean verbose;
/*     */   private String prefix;
/*     */   private Set<JavaFileObject> sourcesWithReportedWarnings;
/*     */   private DeferredDiagnosticKind deferredDiagnosticKind;
/*     */   private JavaFileObject deferredDiagnosticSource;
/*     */   private Object deferredDiagnosticArg;
/*     */   private final boolean enforceMandatory;
/*     */   private final Lint.LintCategory lintCategory;
/*     */ 
/*     */   public MandatoryWarningHandler(Log paramLog, boolean paramBoolean1, boolean paramBoolean2, String paramString, Lint.LintCategory paramLintCategory)
/*     */   {
/* 114 */     this.log = paramLog;
/* 115 */     this.verbose = paramBoolean1;
/* 116 */     this.prefix = paramString;
/* 117 */     this.enforceMandatory = paramBoolean2;
/* 118 */     this.lintCategory = paramLintCategory;
/*     */   }
/*     */ 
/*     */   public void report(JCDiagnostic.DiagnosticPosition paramDiagnosticPosition, String paramString, Object[] paramArrayOfObject)
/*     */   {
/* 125 */     JavaFileObject localJavaFileObject = this.log.currentSourceFile();
/*     */ 
/* 127 */     if (this.verbose) {
/* 128 */       if (this.sourcesWithReportedWarnings == null) {
/* 129 */         this.sourcesWithReportedWarnings = new HashSet();
/*     */       }
/* 131 */       if (this.log.nwarnings < this.log.MaxWarnings)
/*     */       {
/* 133 */         logMandatoryWarning(paramDiagnosticPosition, paramString, paramArrayOfObject);
/* 134 */         this.sourcesWithReportedWarnings.add(localJavaFileObject);
/* 135 */       } else if (this.deferredDiagnosticKind == null)
/*     */       {
/* 137 */         if (this.sourcesWithReportedWarnings.contains(localJavaFileObject))
/*     */         {
/* 139 */           this.deferredDiagnosticKind = DeferredDiagnosticKind.ADDITIONAL_IN_FILE;
/*     */         }
/*     */         else {
/* 142 */           this.deferredDiagnosticKind = DeferredDiagnosticKind.IN_FILE;
/*     */         }
/* 144 */         this.deferredDiagnosticSource = localJavaFileObject;
/* 145 */         this.deferredDiagnosticArg = localJavaFileObject;
/* 146 */       } else if ((this.deferredDiagnosticKind == DeferredDiagnosticKind.IN_FILE) || (this.deferredDiagnosticKind == DeferredDiagnosticKind.ADDITIONAL_IN_FILE))
/*     */       {
/* 148 */         if (!equal(this.deferredDiagnosticSource, localJavaFileObject))
/*     */         {
/* 150 */           this.deferredDiagnosticKind = DeferredDiagnosticKind.ADDITIONAL_IN_FILES;
/* 151 */           this.deferredDiagnosticArg = null;
/*     */         }
/*     */       }
/* 154 */     } else if (this.deferredDiagnosticKind == null)
/*     */     {
/* 156 */       this.deferredDiagnosticKind = DeferredDiagnosticKind.IN_FILE;
/* 157 */       this.deferredDiagnosticSource = localJavaFileObject;
/* 158 */       this.deferredDiagnosticArg = localJavaFileObject;
/* 159 */     } else if ((this.deferredDiagnosticKind == DeferredDiagnosticKind.IN_FILE) && 
/* 160 */       (!equal(this.deferredDiagnosticSource, localJavaFileObject)))
/*     */     {
/* 162 */       this.deferredDiagnosticKind = DeferredDiagnosticKind.IN_FILES;
/* 163 */       this.deferredDiagnosticArg = null;
/*     */     }
/*     */   }
/*     */ 
/*     */   public void reportDeferredDiagnostic()
/*     */   {
/* 172 */     if (this.deferredDiagnosticKind != null) {
/* 173 */       if (this.deferredDiagnosticArg == null)
/* 174 */         logMandatoryNote(this.deferredDiagnosticSource, this.deferredDiagnosticKind.getKey(this.prefix), new Object[0]);
/*     */       else {
/* 176 */         logMandatoryNote(this.deferredDiagnosticSource, this.deferredDiagnosticKind.getKey(this.prefix), new Object[] { this.deferredDiagnosticArg });
/*     */       }
/* 178 */       if (!this.verbose)
/* 179 */         logMandatoryNote(this.deferredDiagnosticSource, this.prefix + ".recompile", new Object[0]);
/*     */     }
/*     */   }
/*     */ 
/*     */   private static boolean equal(Object paramObject1, Object paramObject2)
/*     */   {
/* 187 */     return (paramObject1 == null) || (paramObject2 == null) ? false : paramObject1 == paramObject2 ? true : paramObject1.equals(paramObject2);
/*     */   }
/*     */ 
/*     */   private void logMandatoryWarning(JCDiagnostic.DiagnosticPosition paramDiagnosticPosition, String paramString, Object[] paramArrayOfObject)
/*     */   {
/* 254 */     if (this.enforceMandatory)
/* 255 */       this.log.mandatoryWarning(this.lintCategory, paramDiagnosticPosition, paramString, paramArrayOfObject);
/*     */     else
/* 257 */       this.log.warning(this.lintCategory, paramDiagnosticPosition, paramString, paramArrayOfObject);
/*     */   }
/*     */ 
/*     */   private void logMandatoryNote(JavaFileObject paramJavaFileObject, String paramString, Object[] paramArrayOfObject)
/*     */   {
/* 265 */     if (this.enforceMandatory)
/* 266 */       this.log.mandatoryNote(paramJavaFileObject, paramString, paramArrayOfObject);
/*     */     else
/* 268 */       this.log.note(paramJavaFileObject, paramString, paramArrayOfObject);
/*     */   }
/*     */ 
/*     */   private static enum DeferredDiagnosticKind
/*     */   {
/*  68 */     IN_FILE(".filename"), 
/*     */ 
/*  75 */     ADDITIONAL_IN_FILE(".filename.additional"), 
/*     */ 
/*  82 */     IN_FILES(".plural"), 
/*     */ 
/*  89 */     ADDITIONAL_IN_FILES(".plural.additional");
/*     */ 
/*     */     private final String value;
/*     */ 
/*  91 */     private DeferredDiagnosticKind(String paramString) { this.value = paramString; } 
/*  92 */     String getKey(String paramString) { return paramString + this.value; }
/*     */ 
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.javac.util.MandatoryWarningHandler
 * JD-Core Version:    0.6.2
 */