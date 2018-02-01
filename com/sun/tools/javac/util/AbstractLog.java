/*     */ package com.sun.tools.javac.util;
/*     */ 
/*     */ import com.sun.tools.javac.code.Lint.LintCategory;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import javax.tools.JavaFileObject;
/*     */ 
/*     */ public abstract class AbstractLog
/*     */ {
/*     */   protected JCDiagnostic.Factory diags;
/*     */   protected DiagnosticSource source;
/*     */   protected Map<JavaFileObject, DiagnosticSource> sourceMap;
/*     */ 
/*     */   AbstractLog(JCDiagnostic.Factory paramFactory)
/*     */   {
/*  49 */     this.diags = paramFactory;
/*  50 */     this.sourceMap = new HashMap();
/*     */   }
/*     */ 
/*     */   public JavaFileObject useSource(JavaFileObject paramJavaFileObject)
/*     */   {
/*  56 */     JavaFileObject localJavaFileObject = this.source == null ? null : this.source.getFile();
/*  57 */     this.source = getSource(paramJavaFileObject);
/*  58 */     return localJavaFileObject;
/*     */   }
/*     */ 
/*     */   protected DiagnosticSource getSource(JavaFileObject paramJavaFileObject) {
/*  62 */     if (paramJavaFileObject == null)
/*  63 */       return DiagnosticSource.NO_SOURCE;
/*  64 */     DiagnosticSource localDiagnosticSource = (DiagnosticSource)this.sourceMap.get(paramJavaFileObject);
/*  65 */     if (localDiagnosticSource == null) {
/*  66 */       localDiagnosticSource = new DiagnosticSource(paramJavaFileObject, this);
/*  67 */       this.sourceMap.put(paramJavaFileObject, localDiagnosticSource);
/*     */     }
/*  69 */     return localDiagnosticSource;
/*     */   }
/*     */ 
/*     */   public DiagnosticSource currentSource()
/*     */   {
/*  75 */     return this.source;
/*     */   }
/*     */ 
/*     */   public void error(String paramString, Object[] paramArrayOfObject)
/*     */   {
/*  84 */     report(this.diags.error(this.source, null, paramString, paramArrayOfObject));
/*     */   }
/*     */ 
/*     */   public void error(JCDiagnostic.DiagnosticPosition paramDiagnosticPosition, String paramString, Object[] paramArrayOfObject)
/*     */   {
/*  94 */     report(this.diags.error(this.source, paramDiagnosticPosition, paramString, paramArrayOfObject));
/*     */   }
/*     */ 
/*     */   public void error(JCDiagnostic.DiagnosticFlag paramDiagnosticFlag, JCDiagnostic.DiagnosticPosition paramDiagnosticPosition, String paramString, Object[] paramArrayOfObject)
/*     */   {
/* 105 */     JCDiagnostic localJCDiagnostic = this.diags.error(this.source, paramDiagnosticPosition, paramString, paramArrayOfObject);
/* 106 */     localJCDiagnostic.setFlag(paramDiagnosticFlag);
/* 107 */     report(localJCDiagnostic);
/*     */   }
/*     */ 
/*     */   public void error(int paramInt, String paramString, Object[] paramArrayOfObject)
/*     */   {
/* 117 */     report(this.diags.error(this.source, wrap(paramInt), paramString, paramArrayOfObject));
/*     */   }
/*     */ 
/*     */   public void error(JCDiagnostic.DiagnosticFlag paramDiagnosticFlag, int paramInt, String paramString, Object[] paramArrayOfObject)
/*     */   {
/* 128 */     JCDiagnostic localJCDiagnostic = this.diags.error(this.source, wrap(paramInt), paramString, paramArrayOfObject);
/* 129 */     localJCDiagnostic.setFlag(paramDiagnosticFlag);
/* 130 */     report(localJCDiagnostic);
/*     */   }
/*     */ 
/*     */   public void warning(String paramString, Object[] paramArrayOfObject)
/*     */   {
/* 139 */     report(this.diags.warning(this.source, null, paramString, paramArrayOfObject));
/*     */   }
/*     */ 
/*     */   public void warning(Lint.LintCategory paramLintCategory, String paramString, Object[] paramArrayOfObject)
/*     */   {
/* 149 */     report(this.diags.warning(paramLintCategory, paramString, paramArrayOfObject));
/*     */   }
/*     */ 
/*     */   public void warning(JCDiagnostic.DiagnosticPosition paramDiagnosticPosition, String paramString, Object[] paramArrayOfObject)
/*     */   {
/* 159 */     report(this.diags.warning(this.source, paramDiagnosticPosition, paramString, paramArrayOfObject));
/*     */   }
/*     */ 
/*     */   public void warning(Lint.LintCategory paramLintCategory, JCDiagnostic.DiagnosticPosition paramDiagnosticPosition, String paramString, Object[] paramArrayOfObject)
/*     */   {
/* 170 */     report(this.diags.warning(paramLintCategory, this.source, paramDiagnosticPosition, paramString, paramArrayOfObject));
/*     */   }
/*     */ 
/*     */   public void warning(int paramInt, String paramString, Object[] paramArrayOfObject)
/*     */   {
/* 180 */     report(this.diags.warning(this.source, wrap(paramInt), paramString, paramArrayOfObject));
/*     */   }
/*     */ 
/*     */   public void mandatoryWarning(JCDiagnostic.DiagnosticPosition paramDiagnosticPosition, String paramString, Object[] paramArrayOfObject)
/*     */   {
/* 189 */     report(this.diags.mandatoryWarning(this.source, paramDiagnosticPosition, paramString, paramArrayOfObject));
/*     */   }
/*     */ 
/*     */   public void mandatoryWarning(Lint.LintCategory paramLintCategory, JCDiagnostic.DiagnosticPosition paramDiagnosticPosition, String paramString, Object[] paramArrayOfObject)
/*     */   {
/* 199 */     report(this.diags.mandatoryWarning(paramLintCategory, this.source, paramDiagnosticPosition, paramString, paramArrayOfObject));
/*     */   }
/*     */ 
/*     */   public void note(String paramString, Object[] paramArrayOfObject)
/*     */   {
/* 207 */     report(this.diags.note(this.source, null, paramString, paramArrayOfObject));
/*     */   }
/*     */ 
/*     */   public void note(JCDiagnostic.DiagnosticPosition paramDiagnosticPosition, String paramString, Object[] paramArrayOfObject)
/*     */   {
/* 215 */     report(this.diags.note(this.source, paramDiagnosticPosition, paramString, paramArrayOfObject));
/*     */   }
/*     */ 
/*     */   public void note(int paramInt, String paramString, Object[] paramArrayOfObject)
/*     */   {
/* 223 */     report(this.diags.note(this.source, wrap(paramInt), paramString, paramArrayOfObject));
/*     */   }
/*     */ 
/*     */   public void note(JavaFileObject paramJavaFileObject, String paramString, Object[] paramArrayOfObject)
/*     */   {
/* 231 */     report(this.diags.note(getSource(paramJavaFileObject), null, paramString, paramArrayOfObject));
/*     */   }
/*     */ 
/*     */   public void mandatoryNote(JavaFileObject paramJavaFileObject, String paramString, Object[] paramArrayOfObject)
/*     */   {
/* 239 */     report(this.diags.mandatoryNote(getSource(paramJavaFileObject), paramString, paramArrayOfObject));
/*     */   }
/*     */ 
/*     */   protected abstract void report(JCDiagnostic paramJCDiagnostic);
/*     */ 
/*     */   protected abstract void directError(String paramString, Object[] paramArrayOfObject);
/*     */ 
/*     */   private JCDiagnostic.DiagnosticPosition wrap(int paramInt) {
/* 247 */     return paramInt == -1 ? null : new JCDiagnostic.SimpleDiagnosticPosition(paramInt);
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.javac.util.AbstractLog
 * JD-Core Version:    0.6.2
 */