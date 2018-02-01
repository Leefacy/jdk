/*    */ package com.sun.tools.javadoc;
/*    */ 
/*    */ import com.sun.tools.javac.code.Symbol.ClassSymbol;
/*    */ import com.sun.tools.javac.comp.Enter;
/*    */ import com.sun.tools.javac.comp.Env;
/*    */ import com.sun.tools.javac.tree.JCTree.JCClassDecl;
/*    */ import com.sun.tools.javac.tree.JCTree.JCCompilationUnit;
/*    */ import com.sun.tools.javac.util.Context;
/*    */ import com.sun.tools.javac.util.Context.Factory;
/*    */ import com.sun.tools.javac.util.JCDiagnostic.DiagnosticPosition;
/*    */ import com.sun.tools.javac.util.List;
/*    */ import javax.tools.JavaFileObject;
/*    */ import javax.tools.JavaFileObject.Kind;
/*    */ 
/*    */ public class JavadocEnter extends Enter
/*    */ {
/*    */   final Messager messager;
/*    */   final DocEnv docenv;
/*    */ 
/*    */   public static JavadocEnter instance0(Context paramContext)
/*    */   {
/* 51 */     Object localObject = (Enter)paramContext.get(enterKey);
/* 52 */     if (localObject == null)
/* 53 */       localObject = new JavadocEnter(paramContext);
/* 54 */     return (JavadocEnter)localObject;
/*    */   }
/*    */ 
/*    */   public static void preRegister(Context paramContext) {
/* 58 */     paramContext.put(enterKey, new Context.Factory() {
/*    */       public Enter make(Context paramAnonymousContext) {
/* 60 */         return new JavadocEnter(paramAnonymousContext);
/*    */       }
/*    */     });
/*    */   }
/*    */ 
/*    */   protected JavadocEnter(Context paramContext) {
/* 66 */     super(paramContext);
/* 67 */     this.messager = Messager.instance0(paramContext);
/* 68 */     this.docenv = DocEnv.instance(paramContext);
/*    */   }
/*    */ 
/*    */   public void main(List<JCTree.JCCompilationUnit> paramList)
/*    */   {
/* 77 */     int i = this.messager.nerrors;
/* 78 */     super.main(paramList);
/* 79 */     this.messager.nwarnings += this.messager.nerrors - i;
/* 80 */     this.messager.nerrors = i;
/*    */   }
/*    */ 
/*    */   public void visitTopLevel(JCTree.JCCompilationUnit paramJCCompilationUnit)
/*    */   {
/* 85 */     super.visitTopLevel(paramJCCompilationUnit);
/* 86 */     if (paramJCCompilationUnit.sourcefile.isNameCompatible("package-info", JavaFileObject.Kind.SOURCE))
/* 87 */       this.docenv.makePackageDoc(paramJCCompilationUnit.packge, this.docenv.getTreePath(paramJCCompilationUnit));
/*    */   }
/*    */ 
/*    */   public void visitClassDef(JCTree.JCClassDecl paramJCClassDecl)
/*    */   {
/* 93 */     super.visitClassDef(paramJCClassDecl);
/* 94 */     if (paramJCClassDecl.sym == null) return;
/* 95 */     if ((paramJCClassDecl.sym.kind == 2) || (paramJCClassDecl.sym.kind == 63)) {
/* 96 */       Symbol.ClassSymbol localClassSymbol = paramJCClassDecl.sym;
/* 97 */       this.docenv.makeClassDoc(localClassSymbol, this.docenv.getTreePath(this.env.toplevel, paramJCClassDecl));
/*    */     }
/*    */   }
/*    */ 
/*    */   protected void duplicateClass(JCDiagnostic.DiagnosticPosition paramDiagnosticPosition, Symbol.ClassSymbol paramClassSymbol)
/*    */   {
/*    */   }
/*    */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.javadoc.JavadocEnter
 * JD-Core Version:    0.6.2
 */