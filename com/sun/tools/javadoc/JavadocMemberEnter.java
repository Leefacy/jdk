/*     */ package com.sun.tools.javadoc;
/*     */ 
/*     */ import com.sun.source.util.TreePath;
/*     */ import com.sun.tools.javac.code.Symbol.MethodSymbol;
/*     */ import com.sun.tools.javac.code.Symbol.VarSymbol;
/*     */ import com.sun.tools.javac.comp.Env;
/*     */ import com.sun.tools.javac.comp.MemberEnter;
/*     */ import com.sun.tools.javac.tree.JCTree;
/*     */ import com.sun.tools.javac.tree.JCTree.JCBinary;
/*     */ import com.sun.tools.javac.tree.JCTree.JCClassDecl;
/*     */ import com.sun.tools.javac.tree.JCTree.JCConditional;
/*     */ import com.sun.tools.javac.tree.JCTree.JCExpression;
/*     */ import com.sun.tools.javac.tree.JCTree.JCFieldAccess;
/*     */ import com.sun.tools.javac.tree.JCTree.JCIdent;
/*     */ import com.sun.tools.javac.tree.JCTree.JCLiteral;
/*     */ import com.sun.tools.javac.tree.JCTree.JCMethodDecl;
/*     */ import com.sun.tools.javac.tree.JCTree.JCModifiers;
/*     */ import com.sun.tools.javac.tree.JCTree.JCParens;
/*     */ import com.sun.tools.javac.tree.JCTree.JCPrimitiveTypeTree;
/*     */ import com.sun.tools.javac.tree.JCTree.JCTypeCast;
/*     */ import com.sun.tools.javac.tree.JCTree.JCUnary;
/*     */ import com.sun.tools.javac.tree.JCTree.JCVariableDecl;
/*     */ import com.sun.tools.javac.tree.JCTree.Visitor;
/*     */ import com.sun.tools.javac.util.Context;
/*     */ import com.sun.tools.javac.util.Context.Factory;
/*     */ 
/*     */ public class JavadocMemberEnter extends MemberEnter
/*     */ {
/*     */   final DocEnv docenv;
/*     */ 
/*     */   public static JavadocMemberEnter instance0(Context paramContext)
/*     */   {
/*  52 */     Object localObject = (MemberEnter)paramContext.get(memberEnterKey);
/*  53 */     if (localObject == null)
/*  54 */       localObject = new JavadocMemberEnter(paramContext);
/*  55 */     return (JavadocMemberEnter)localObject;
/*     */   }
/*     */ 
/*     */   public static void preRegister(Context paramContext) {
/*  59 */     paramContext.put(memberEnterKey, new Context.Factory() {
/*     */       public MemberEnter make(Context paramAnonymousContext) {
/*  61 */         return new JavadocMemberEnter(paramAnonymousContext);
/*     */       }
/*     */     });
/*     */   }
/*     */ 
/*     */   protected JavadocMemberEnter(Context paramContext)
/*     */   {
/*  69 */     super(paramContext);
/*  70 */     this.docenv = DocEnv.instance(paramContext);
/*     */   }
/*     */ 
/*     */   public void visitMethodDef(JCTree.JCMethodDecl paramJCMethodDecl)
/*     */   {
/*  75 */     super.visitMethodDef(paramJCMethodDecl);
/*  76 */     Symbol.MethodSymbol localMethodSymbol = paramJCMethodDecl.sym;
/*  77 */     if ((localMethodSymbol == null) || (localMethodSymbol.kind != 16)) return;
/*  78 */     TreePath localTreePath = this.docenv.getTreePath(this.env.toplevel, this.env.enclClass, paramJCMethodDecl);
/*  79 */     if (localMethodSymbol.isConstructor())
/*  80 */       this.docenv.makeConstructorDoc(localMethodSymbol, localTreePath);
/*  81 */     else if (isAnnotationTypeElement(localMethodSymbol))
/*  82 */       this.docenv.makeAnnotationTypeElementDoc(localMethodSymbol, localTreePath);
/*     */     else {
/*  84 */       this.docenv.makeMethodDoc(localMethodSymbol, localTreePath);
/*     */     }
/*     */ 
/*  87 */     paramJCMethodDecl.body = null;
/*     */   }
/*     */ 
/*     */   public void visitVarDef(JCTree.JCVariableDecl paramJCVariableDecl)
/*     */   {
/*  92 */     if (paramJCVariableDecl.init != null) {
/*  93 */       int i = ((paramJCVariableDecl.mods.flags & 0x10) != 0L) || ((this.env.enclClass.mods.flags & 0x200) != 0L) ? 1 : 0;
/*     */ 
/*  95 */       if ((i == 0) || (containsNonConstantExpression(paramJCVariableDecl.init)))
/*     */       {
/* 100 */         paramJCVariableDecl.init = null;
/*     */       }
/*     */     }
/* 103 */     super.visitVarDef(paramJCVariableDecl);
/* 104 */     if ((paramJCVariableDecl.sym != null) && (paramJCVariableDecl.sym.kind == 4))
/*     */     {
/* 106 */       if (!isParameter(paramJCVariableDecl.sym))
/*     */       {
/* 107 */         this.docenv.makeFieldDoc(paramJCVariableDecl.sym, this.docenv.getTreePath(this.env.toplevel, this.env.enclClass, paramJCVariableDecl));
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/* 112 */   private static boolean isAnnotationTypeElement(Symbol.MethodSymbol paramMethodSymbol) { return ClassDocImpl.isAnnotationType(paramMethodSymbol.enclClass()); }
/*     */ 
/*     */   private static boolean isParameter(Symbol.VarSymbol paramVarSymbol)
/*     */   {
/* 116 */     return (paramVarSymbol.flags() & 0x0) != 0L;
/*     */   }
/*     */ 
/*     */   private static boolean containsNonConstantExpression(JCTree.JCExpression paramJCExpression)
/*     */   {
/* 125 */     return new MaybeConstantExpressionScanner(null).containsNonConstantExpression(paramJCExpression);
/*     */   }
/*     */ 
/*     */   private static class MaybeConstantExpressionScanner extends JCTree.Visitor
/*     */   {
/* 132 */     boolean maybeConstantExpr = true;
/*     */ 
/*     */     public boolean containsNonConstantExpression(JCTree.JCExpression paramJCExpression) {
/* 135 */       scan(paramJCExpression);
/* 136 */       return !this.maybeConstantExpr;
/*     */     }
/*     */ 
/*     */     public void scan(JCTree paramJCTree)
/*     */     {
/* 141 */       if ((this.maybeConstantExpr) && (paramJCTree != null))
/* 142 */         paramJCTree.accept(this);
/*     */     }
/*     */ 
/*     */     public void visitTree(JCTree paramJCTree)
/*     */     {
/* 148 */       this.maybeConstantExpr = false;
/*     */     }
/*     */ 
/*     */     public void visitBinary(JCTree.JCBinary paramJCBinary)
/*     */     {
/* 153 */       switch (JavadocMemberEnter.2.$SwitchMap$com$sun$tools$javac$tree$JCTree$Tag[paramJCBinary.getTag().ordinal()]) { case 1:
/*     */       case 2:
/*     */       case 3:
/*     */       case 4:
/*     */       case 5:
/*     */       case 6:
/*     */       case 7:
/*     */       case 8:
/*     */       case 9:
/*     */       case 10:
/*     */       case 11:
/*     */       case 12:
/*     */       case 13:
/*     */       case 14:
/*     */       case 15:
/*     */       case 16:
/*     */       case 17:
/*     */       case 18:
/*     */       case 19:
/* 161 */         break;
/*     */       default:
/* 163 */         this.maybeConstantExpr = false;
/*     */       }
/*     */     }
/*     */ 
/*     */     public void visitConditional(JCTree.JCConditional paramJCConditional)
/*     */     {
/* 169 */       scan(paramJCConditional.cond);
/* 170 */       scan(paramJCConditional.truepart);
/* 171 */       scan(paramJCConditional.falsepart);
/*     */     }
/*     */ 
/*     */     public void visitIdent(JCTree.JCIdent paramJCIdent)
/*     */     {
/*     */     }
/*     */ 
/*     */     public void visitLiteral(JCTree.JCLiteral paramJCLiteral) {
/*     */     }
/*     */ 
/*     */     public void visitParens(JCTree.JCParens paramJCParens) {
/* 182 */       scan(paramJCParens.expr);
/*     */     }
/*     */ 
/*     */     public void visitSelect(JCTree.JCFieldAccess paramJCFieldAccess)
/*     */     {
/* 187 */       scan(paramJCFieldAccess.selected);
/*     */     }
/*     */ 
/*     */     public void visitTypeCast(JCTree.JCTypeCast paramJCTypeCast)
/*     */     {
/* 192 */       scan(paramJCTypeCast.clazz);
/* 193 */       scan(paramJCTypeCast.expr);
/*     */     }
/*     */ 
/*     */     public void visitTypeIdent(JCTree.JCPrimitiveTypeTree paramJCPrimitiveTypeTree)
/*     */     {
/*     */     }
/*     */ 
/*     */     public void visitUnary(JCTree.JCUnary paramJCUnary) {
/* 201 */       switch (JavadocMemberEnter.2.$SwitchMap$com$sun$tools$javac$tree$JCTree$Tag[paramJCUnary.getTag().ordinal()]) { case 20:
/*     */       case 21:
/*     */       case 22:
/*     */       case 23:
/* 203 */         break;
/*     */       default:
/* 205 */         this.maybeConstantExpr = false;
/*     */       }
/*     */     }
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.javadoc.JavadocMemberEnter
 * JD-Core Version:    0.6.2
 */