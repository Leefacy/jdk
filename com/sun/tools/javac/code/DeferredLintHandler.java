/*     */ package com.sun.tools.javac.code;
/*     */ 
/*     */ import com.sun.tools.javac.tree.EndPosTable;
/*     */ import com.sun.tools.javac.tree.JCTree;
/*     */ import com.sun.tools.javac.util.Assert;
/*     */ import com.sun.tools.javac.util.Context;
/*     */ import com.sun.tools.javac.util.Context.Key;
/*     */ import com.sun.tools.javac.util.JCDiagnostic.DiagnosticPosition;
/*     */ import com.sun.tools.javac.util.ListBuffer;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ 
/*     */ public class DeferredLintHandler
/*     */ {
/*  46 */   protected static final Context.Key<DeferredLintHandler> deferredLintHandlerKey = new Context.Key();
/*     */   private JCDiagnostic.DiagnosticPosition currentPos;
/*  70 */   private Map<JCDiagnostic.DiagnosticPosition, ListBuffer<LintLogger>> loggersQueue = new HashMap();
/*     */ 
/* 119 */   private static final JCDiagnostic.DiagnosticPosition IMMEDIATE_POSITION = new JCDiagnostic.DiagnosticPosition()
/*     */   {
/*     */     public JCTree getTree() {
/* 122 */       Assert.error();
/* 123 */       return null;
/*     */     }
/*     */ 
/*     */     public int getStartPosition()
/*     */     {
/* 128 */       Assert.error();
/* 129 */       return -1;
/*     */     }
/*     */ 
/*     */     public int getPreferredPosition()
/*     */     {
/* 134 */       Assert.error();
/* 135 */       return -1;
/*     */     }
/*     */ 
/*     */     public int getEndPosition(EndPosTable paramAnonymousEndPosTable)
/*     */     {
/* 140 */       Assert.error();
/* 141 */       return -1;
/*     */     }
/* 119 */   };
/*     */ 
/*     */   public static DeferredLintHandler instance(Context paramContext)
/*     */   {
/*  50 */     DeferredLintHandler localDeferredLintHandler = (DeferredLintHandler)paramContext.get(deferredLintHandlerKey);
/*  51 */     if (localDeferredLintHandler == null)
/*  52 */       localDeferredLintHandler = new DeferredLintHandler(paramContext);
/*  53 */     return localDeferredLintHandler;
/*     */   }
/*     */ 
/*     */   protected DeferredLintHandler(Context paramContext) {
/*  57 */     paramContext.put(deferredLintHandlerKey, this);
/*  58 */     this.currentPos = IMMEDIATE_POSITION;
/*     */   }
/*     */ 
/*     */   public void report(LintLogger paramLintLogger)
/*     */   {
/*  79 */     if (this.currentPos == IMMEDIATE_POSITION) {
/*  80 */       paramLintLogger.report();
/*     */     } else {
/*  82 */       ListBuffer localListBuffer = (ListBuffer)this.loggersQueue.get(this.currentPos);
/*  83 */       if (localListBuffer == null) {
/*  84 */         this.loggersQueue.put(this.currentPos, localListBuffer = new ListBuffer());
/*     */       }
/*  86 */       localListBuffer.append(paramLintLogger);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void flush(JCDiagnostic.DiagnosticPosition paramDiagnosticPosition)
/*     */   {
/*  93 */     ListBuffer localListBuffer = (ListBuffer)this.loggersQueue.get(paramDiagnosticPosition);
/*  94 */     if (localListBuffer != null) {
/*  95 */       for (LintLogger localLintLogger : localListBuffer) {
/*  96 */         localLintLogger.report();
/*     */       }
/*  98 */       this.loggersQueue.remove(paramDiagnosticPosition);
/*     */     }
/*     */   }
/*     */ 
/*     */   public JCDiagnostic.DiagnosticPosition setPos(JCDiagnostic.DiagnosticPosition paramDiagnosticPosition)
/*     */   {
/* 107 */     JCDiagnostic.DiagnosticPosition localDiagnosticPosition = this.currentPos;
/* 108 */     this.currentPos = paramDiagnosticPosition;
/* 109 */     return localDiagnosticPosition;
/*     */   }
/*     */ 
/*     */   public JCDiagnostic.DiagnosticPosition immediate()
/*     */   {
/* 116 */     return setPos(IMMEDIATE_POSITION);
/*     */   }
/*     */ 
/*     */   public static abstract interface LintLogger
/*     */   {
/*     */     public abstract void report();
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.javac.code.DeferredLintHandler
 * JD-Core Version:    0.6.2
 */