/*     */ package com.sun.tools.javac.processing;
/*     */ 
/*     */ import com.sun.tools.javac.model.JavacElements;
/*     */ import com.sun.tools.javac.tree.JCTree;
/*     */ import com.sun.tools.javac.tree.JCTree.JCCompilationUnit;
/*     */ import com.sun.tools.javac.util.Context;
/*     */ import com.sun.tools.javac.util.JCDiagnostic.DiagnosticPosition;
/*     */ import com.sun.tools.javac.util.Log;
/*     */ import com.sun.tools.javac.util.Pair;
/*     */ import javax.annotation.processing.Messager;
/*     */ import javax.lang.model.element.AnnotationMirror;
/*     */ import javax.lang.model.element.AnnotationValue;
/*     */ import javax.lang.model.element.Element;
/*     */ import javax.tools.Diagnostic.Kind;
/*     */ import javax.tools.JavaFileObject;
/*     */ 
/*     */ public class JavacMessager
/*     */   implements Messager
/*     */ {
/*     */   Log log;
/*     */   JavacProcessingEnvironment processingEnv;
/*  48 */   int errorCount = 0;
/*  49 */   int warningCount = 0;
/*     */ 
/*     */   JavacMessager(Context paramContext, JavacProcessingEnvironment paramJavacProcessingEnvironment) {
/*  52 */     this.log = Log.instance(paramContext);
/*  53 */     this.processingEnv = paramJavacProcessingEnvironment;
/*     */   }
/*     */ 
/*     */   public void printMessage(Diagnostic.Kind paramKind, CharSequence paramCharSequence)
/*     */   {
/*  59 */     printMessage(paramKind, paramCharSequence, null, null, null);
/*     */   }
/*     */ 
/*     */   public void printMessage(Diagnostic.Kind paramKind, CharSequence paramCharSequence, Element paramElement)
/*     */   {
/*  64 */     printMessage(paramKind, paramCharSequence, paramElement, null, null);
/*     */   }
/*     */ 
/*     */   public void printMessage(Diagnostic.Kind paramKind, CharSequence paramCharSequence, Element paramElement, AnnotationMirror paramAnnotationMirror)
/*     */   {
/*  78 */     printMessage(paramKind, paramCharSequence, paramElement, paramAnnotationMirror, null);
/*     */   }
/*     */ 
/*     */   public void printMessage(Diagnostic.Kind paramKind, CharSequence paramCharSequence, Element paramElement, AnnotationMirror paramAnnotationMirror, AnnotationValue paramAnnotationValue)
/*     */   {
/*  94 */     JavaFileObject localJavaFileObject1 = null;
/*  95 */     JavaFileObject localJavaFileObject2 = null;
/*  96 */     JCDiagnostic.DiagnosticPosition localDiagnosticPosition = null;
/*  97 */     JavacElements localJavacElements = this.processingEnv.getElementUtils();
/*  98 */     Pair localPair = localJavacElements.getTreeAndTopLevel(paramElement, paramAnnotationMirror, paramAnnotationValue);
/*  99 */     if (localPair != null) {
/* 100 */       localJavaFileObject2 = ((JCTree.JCCompilationUnit)localPair.snd).sourcefile;
/* 101 */       if (localJavaFileObject2 != null)
/*     */       {
/* 103 */         localJavaFileObject1 = this.log.useSource(localJavaFileObject2);
/* 104 */         localDiagnosticPosition = ((JCTree)localPair.fst).pos();
/*     */       }
/*     */     }
/*     */     try {
/* 108 */       switch (1.$SwitchMap$javax$tools$Diagnostic$Kind[paramKind.ordinal()]) {
/*     */       case 1:
/* 110 */         this.errorCount += 1;
/* 111 */         boolean bool = this.log.multipleErrors;
/* 112 */         this.log.multipleErrors = true;
/*     */         try {
/* 114 */           this.log.error(localDiagnosticPosition, "proc.messager", new Object[] { paramCharSequence.toString() });
/*     */         } finally {
/* 116 */           this.log.multipleErrors = bool;
/*     */         }
/* 118 */         break;
/*     */       case 2:
/* 121 */         this.warningCount += 1;
/* 122 */         this.log.warning(localDiagnosticPosition, "proc.messager", new Object[] { paramCharSequence.toString() });
/* 123 */         break;
/*     */       case 3:
/* 126 */         this.warningCount += 1;
/* 127 */         this.log.mandatoryWarning(localDiagnosticPosition, "proc.messager", new Object[] { paramCharSequence.toString() });
/* 128 */         break;
/*     */       default:
/* 131 */         this.log.note(localDiagnosticPosition, "proc.messager", new Object[] { paramCharSequence.toString() });
/*     */       }
/*     */     }
/*     */     finally
/*     */     {
/* 136 */       if (localJavaFileObject2 != null)
/* 137 */         this.log.useSource(localJavaFileObject1);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void printError(String paramString)
/*     */   {
/* 147 */     printMessage(Diagnostic.Kind.ERROR, paramString);
/*     */   }
/*     */ 
/*     */   public void printWarning(String paramString)
/*     */   {
/* 156 */     printMessage(Diagnostic.Kind.WARNING, paramString);
/*     */   }
/*     */ 
/*     */   public void printNotice(String paramString)
/*     */   {
/* 164 */     printMessage(Diagnostic.Kind.NOTE, paramString);
/*     */   }
/*     */ 
/*     */   public boolean errorRaised() {
/* 168 */     return this.errorCount > 0;
/*     */   }
/*     */ 
/*     */   public int errorCount() {
/* 172 */     return this.errorCount;
/*     */   }
/*     */ 
/*     */   public int warningCount() {
/* 176 */     return this.warningCount;
/*     */   }
/*     */ 
/*     */   public void newRound(Context paramContext) {
/* 180 */     this.log = Log.instance(paramContext);
/* 181 */     this.errorCount = 0;
/*     */   }
/*     */ 
/*     */   public String toString() {
/* 185 */     return "javac Messager";
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.javac.processing.JavacMessager
 * JD-Core Version:    0.6.2
 */