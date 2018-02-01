/*      */ package com.sun.tools.javac.tree;
/*      */ 
/*      */ import com.sun.source.tree.AnnotatedTypeTree;
/*      */ import com.sun.source.tree.AnnotationTree;
/*      */ import com.sun.source.tree.ArrayAccessTree;
/*      */ import com.sun.source.tree.ArrayTypeTree;
/*      */ import com.sun.source.tree.AssertTree;
/*      */ import com.sun.source.tree.AssignmentTree;
/*      */ import com.sun.source.tree.BinaryTree;
/*      */ import com.sun.source.tree.BlockTree;
/*      */ import com.sun.source.tree.BreakTree;
/*      */ import com.sun.source.tree.CaseTree;
/*      */ import com.sun.source.tree.CatchTree;
/*      */ import com.sun.source.tree.ClassTree;
/*      */ import com.sun.source.tree.CompilationUnitTree;
/*      */ import com.sun.source.tree.CompoundAssignmentTree;
/*      */ import com.sun.source.tree.ConditionalExpressionTree;
/*      */ import com.sun.source.tree.ContinueTree;
/*      */ import com.sun.source.tree.DoWhileLoopTree;
/*      */ import com.sun.source.tree.EmptyStatementTree;
/*      */ import com.sun.source.tree.EnhancedForLoopTree;
/*      */ import com.sun.source.tree.ErroneousTree;
/*      */ import com.sun.source.tree.ExpressionStatementTree;
/*      */ import com.sun.source.tree.ExpressionTree;
/*      */ import com.sun.source.tree.ForLoopTree;
/*      */ import com.sun.source.tree.IdentifierTree;
/*      */ import com.sun.source.tree.IfTree;
/*      */ import com.sun.source.tree.ImportTree;
/*      */ import com.sun.source.tree.InstanceOfTree;
/*      */ import com.sun.source.tree.IntersectionTypeTree;
/*      */ import com.sun.source.tree.LabeledStatementTree;
/*      */ import com.sun.source.tree.LambdaExpressionTree;
/*      */ import com.sun.source.tree.LambdaExpressionTree.BodyKind;
/*      */ import com.sun.source.tree.LiteralTree;
/*      */ import com.sun.source.tree.MemberReferenceTree;
/*      */ import com.sun.source.tree.MemberReferenceTree.ReferenceMode;
/*      */ import com.sun.source.tree.MemberSelectTree;
/*      */ import com.sun.source.tree.MethodInvocationTree;
/*      */ import com.sun.source.tree.MethodTree;
/*      */ import com.sun.source.tree.ModifiersTree;
/*      */ import com.sun.source.tree.NewArrayTree;
/*      */ import com.sun.source.tree.NewClassTree;
/*      */ import com.sun.source.tree.ParameterizedTypeTree;
/*      */ import com.sun.source.tree.ParenthesizedTree;
/*      */ import com.sun.source.tree.PrimitiveTypeTree;
/*      */ import com.sun.source.tree.ReturnTree;
/*      */ import com.sun.source.tree.StatementTree;
/*      */ import com.sun.source.tree.SwitchTree;
/*      */ import com.sun.source.tree.SynchronizedTree;
/*      */ import com.sun.source.tree.ThrowTree;
/*      */ import com.sun.source.tree.Tree;
/*      */ import com.sun.source.tree.Tree.Kind;
/*      */ import com.sun.source.tree.TreeVisitor;
/*      */ import com.sun.source.tree.TryTree;
/*      */ import com.sun.source.tree.TypeCastTree;
/*      */ import com.sun.source.tree.TypeParameterTree;
/*      */ import com.sun.source.tree.UnaryTree;
/*      */ import com.sun.source.tree.UnionTypeTree;
/*      */ import com.sun.source.tree.VariableTree;
/*      */ import com.sun.source.tree.WhileLoopTree;
/*      */ import com.sun.source.tree.WildcardTree;
/*      */ import com.sun.tools.javac.code.Attribute.Compound;
/*      */ import com.sun.tools.javac.code.BoundKind;
/*      */ import com.sun.tools.javac.code.Flags;
/*      */ import com.sun.tools.javac.code.Scope.ImportScope;
/*      */ import com.sun.tools.javac.code.Scope.StarImportScope;
/*      */ import com.sun.tools.javac.code.Symbol;
/*      */ import com.sun.tools.javac.code.Symbol.ClassSymbol;
/*      */ import com.sun.tools.javac.code.Symbol.MethodSymbol;
/*      */ import com.sun.tools.javac.code.Symbol.PackageSymbol;
/*      */ import com.sun.tools.javac.code.Symbol.VarSymbol;
/*      */ import com.sun.tools.javac.code.Type;
/*      */ import com.sun.tools.javac.code.TypeTag;
/*      */ import com.sun.tools.javac.code.Types;
/*      */ import com.sun.tools.javac.util.Assert;
/*      */ import com.sun.tools.javac.util.JCDiagnostic.DiagnosticPosition;
/*      */ import com.sun.tools.javac.util.ListBuffer;
/*      */ import com.sun.tools.javac.util.Name;
/*      */ import com.sun.tools.javac.util.Position.LineMap;
/*      */ import java.io.IOException;
/*      */ import java.io.StringWriter;
/*      */ import java.util.Set;
/*      */ import javax.lang.model.element.Modifier;
/*      */ import javax.lang.model.type.TypeKind;
/*      */ import javax.tools.JavaFileObject;
/*      */ 
/*      */ public abstract class JCTree
/*      */   implements Tree, Cloneable, JCDiagnostic.DiagnosticPosition
/*      */ {
/*      */   public int pos;
/*      */   public Type type;
/*      */ 
/*      */   public abstract Tag getTag();
/*      */ 
/*      */   public boolean hasTag(Tag paramTag)
/*      */   {
/*  402 */     return paramTag == getTag();
/*      */   }
/*      */ 
/*      */   public String toString()
/*      */   {
/*  408 */     StringWriter localStringWriter = new StringWriter();
/*      */     try {
/*  410 */       new Pretty(localStringWriter, false).printExpr(this);
/*      */     }
/*      */     catch (IOException localIOException)
/*      */     {
/*  415 */       throw new AssertionError(localIOException);
/*      */     }
/*  417 */     return localStringWriter.toString();
/*      */   }
/*      */ 
/*      */   public JCTree setPos(int paramInt)
/*      */   {
/*  423 */     this.pos = paramInt;
/*  424 */     return this;
/*      */   }
/*      */ 
/*      */   public JCTree setType(Type paramType)
/*      */   {
/*  430 */     this.type = paramType;
/*  431 */     return this;
/*      */   }
/*      */ 
/*      */   public abstract void accept(Visitor paramVisitor);
/*      */ 
/*      */   public abstract <R, D> R accept(TreeVisitor<R, D> paramTreeVisitor, D paramD);
/*      */ 
/*      */   public Object clone()
/*      */   {
/*      */     try
/*      */     {
/*  445 */       return super.clone();
/*      */     } catch (CloneNotSupportedException localCloneNotSupportedException) {
/*  447 */       throw new RuntimeException(localCloneNotSupportedException);
/*      */     }
/*      */   }
/*      */ 
/*      */   public JCDiagnostic.DiagnosticPosition pos()
/*      */   {
/*  454 */     return this;
/*      */   }
/*      */ 
/*      */   public JCTree getTree()
/*      */   {
/*  459 */     return this;
/*      */   }
/*      */ 
/*      */   public int getStartPosition()
/*      */   {
/*  464 */     return TreeInfo.getStartPos(this);
/*      */   }
/*      */ 
/*      */   public int getPreferredPosition()
/*      */   {
/*  469 */     return this.pos;
/*      */   }
/*      */ 
/*      */   public int getEndPosition(EndPosTable paramEndPosTable)
/*      */   {
/*  474 */     return TreeInfo.getEndPos(this, paramEndPosTable);
/*      */   }
/*      */ 
/*      */   public static abstract interface Factory
/*      */   {
/*      */     public abstract JCTree.JCCompilationUnit TopLevel(com.sun.tools.javac.util.List<JCTree.JCAnnotation> paramList, JCTree.JCExpression paramJCExpression, com.sun.tools.javac.util.List<JCTree> paramList1);
/*      */ 
/*      */     public abstract JCTree.JCImport Import(JCTree paramJCTree, boolean paramBoolean);
/*      */ 
/*      */     public abstract JCTree.JCClassDecl ClassDef(JCTree.JCModifiers paramJCModifiers, Name paramName, com.sun.tools.javac.util.List<JCTree.JCTypeParameter> paramList, JCTree.JCExpression paramJCExpression, com.sun.tools.javac.util.List<JCTree.JCExpression> paramList1, com.sun.tools.javac.util.List<JCTree> paramList2);
/*      */ 
/*      */     public abstract JCTree.JCMethodDecl MethodDef(JCTree.JCModifiers paramJCModifiers, Name paramName, JCTree.JCExpression paramJCExpression1, com.sun.tools.javac.util.List<JCTree.JCTypeParameter> paramList, JCTree.JCVariableDecl paramJCVariableDecl, com.sun.tools.javac.util.List<JCTree.JCVariableDecl> paramList1, com.sun.tools.javac.util.List<JCTree.JCExpression> paramList2, JCTree.JCBlock paramJCBlock, JCTree.JCExpression paramJCExpression2);
/*      */ 
/*      */     public abstract JCTree.JCVariableDecl VarDef(JCTree.JCModifiers paramJCModifiers, Name paramName, JCTree.JCExpression paramJCExpression1, JCTree.JCExpression paramJCExpression2);
/*      */ 
/*      */     public abstract JCTree.JCSkip Skip();
/*      */ 
/*      */     public abstract JCTree.JCBlock Block(long paramLong, com.sun.tools.javac.util.List<JCTree.JCStatement> paramList);
/*      */ 
/*      */     public abstract JCTree.JCDoWhileLoop DoLoop(JCTree.JCStatement paramJCStatement, JCTree.JCExpression paramJCExpression);
/*      */ 
/*      */     public abstract JCTree.JCWhileLoop WhileLoop(JCTree.JCExpression paramJCExpression, JCTree.JCStatement paramJCStatement);
/*      */ 
/*      */     public abstract JCTree.JCForLoop ForLoop(com.sun.tools.javac.util.List<JCTree.JCStatement> paramList, JCTree.JCExpression paramJCExpression, com.sun.tools.javac.util.List<JCTree.JCExpressionStatement> paramList1, JCTree.JCStatement paramJCStatement);
/*      */ 
/*      */     public abstract JCTree.JCEnhancedForLoop ForeachLoop(JCTree.JCVariableDecl paramJCVariableDecl, JCTree.JCExpression paramJCExpression, JCTree.JCStatement paramJCStatement);
/*      */ 
/*      */     public abstract JCTree.JCLabeledStatement Labelled(Name paramName, JCTree.JCStatement paramJCStatement);
/*      */ 
/*      */     public abstract JCTree.JCSwitch Switch(JCTree.JCExpression paramJCExpression, com.sun.tools.javac.util.List<JCTree.JCCase> paramList);
/*      */ 
/*      */     public abstract JCTree.JCCase Case(JCTree.JCExpression paramJCExpression, com.sun.tools.javac.util.List<JCTree.JCStatement> paramList);
/*      */ 
/*      */     public abstract JCTree.JCSynchronized Synchronized(JCTree.JCExpression paramJCExpression, JCTree.JCBlock paramJCBlock);
/*      */ 
/*      */     public abstract JCTree.JCTry Try(JCTree.JCBlock paramJCBlock1, com.sun.tools.javac.util.List<JCTree.JCCatch> paramList, JCTree.JCBlock paramJCBlock2);
/*      */ 
/*      */     public abstract JCTree.JCTry Try(com.sun.tools.javac.util.List<JCTree> paramList, JCTree.JCBlock paramJCBlock1, com.sun.tools.javac.util.List<JCTree.JCCatch> paramList1, JCTree.JCBlock paramJCBlock2);
/*      */ 
/*      */     public abstract JCTree.JCCatch Catch(JCTree.JCVariableDecl paramJCVariableDecl, JCTree.JCBlock paramJCBlock);
/*      */ 
/*      */     public abstract JCTree.JCConditional Conditional(JCTree.JCExpression paramJCExpression1, JCTree.JCExpression paramJCExpression2, JCTree.JCExpression paramJCExpression3);
/*      */ 
/*      */     public abstract JCTree.JCIf If(JCTree.JCExpression paramJCExpression, JCTree.JCStatement paramJCStatement1, JCTree.JCStatement paramJCStatement2);
/*      */ 
/*      */     public abstract JCTree.JCExpressionStatement Exec(JCTree.JCExpression paramJCExpression);
/*      */ 
/*      */     public abstract JCTree.JCBreak Break(Name paramName);
/*      */ 
/*      */     public abstract JCTree.JCContinue Continue(Name paramName);
/*      */ 
/*      */     public abstract JCTree.JCReturn Return(JCTree.JCExpression paramJCExpression);
/*      */ 
/*      */     public abstract JCTree.JCThrow Throw(JCTree.JCExpression paramJCExpression);
/*      */ 
/*      */     public abstract JCTree.JCAssert Assert(JCTree.JCExpression paramJCExpression1, JCTree.JCExpression paramJCExpression2);
/*      */ 
/*      */     public abstract JCTree.JCMethodInvocation Apply(com.sun.tools.javac.util.List<JCTree.JCExpression> paramList1, JCTree.JCExpression paramJCExpression, com.sun.tools.javac.util.List<JCTree.JCExpression> paramList2);
/*      */ 
/*      */     public abstract JCTree.JCNewClass NewClass(JCTree.JCExpression paramJCExpression1, com.sun.tools.javac.util.List<JCTree.JCExpression> paramList1, JCTree.JCExpression paramJCExpression2, com.sun.tools.javac.util.List<JCTree.JCExpression> paramList2, JCTree.JCClassDecl paramJCClassDecl);
/*      */ 
/*      */     public abstract JCTree.JCNewArray NewArray(JCTree.JCExpression paramJCExpression, com.sun.tools.javac.util.List<JCTree.JCExpression> paramList1, com.sun.tools.javac.util.List<JCTree.JCExpression> paramList2);
/*      */ 
/*      */     public abstract JCTree.JCParens Parens(JCTree.JCExpression paramJCExpression);
/*      */ 
/*      */     public abstract JCTree.JCAssign Assign(JCTree.JCExpression paramJCExpression1, JCTree.JCExpression paramJCExpression2);
/*      */ 
/*      */     public abstract JCTree.JCAssignOp Assignop(JCTree.Tag paramTag, JCTree paramJCTree1, JCTree paramJCTree2);
/*      */ 
/*      */     public abstract JCTree.JCUnary Unary(JCTree.Tag paramTag, JCTree.JCExpression paramJCExpression);
/*      */ 
/*      */     public abstract JCTree.JCBinary Binary(JCTree.Tag paramTag, JCTree.JCExpression paramJCExpression1, JCTree.JCExpression paramJCExpression2);
/*      */ 
/*      */     public abstract JCTree.JCTypeCast TypeCast(JCTree paramJCTree, JCTree.JCExpression paramJCExpression);
/*      */ 
/*      */     public abstract JCTree.JCInstanceOf TypeTest(JCTree.JCExpression paramJCExpression, JCTree paramJCTree);
/*      */ 
/*      */     public abstract JCTree.JCArrayAccess Indexed(JCTree.JCExpression paramJCExpression1, JCTree.JCExpression paramJCExpression2);
/*      */ 
/*      */     public abstract JCTree.JCFieldAccess Select(JCTree.JCExpression paramJCExpression, Name paramName);
/*      */ 
/*      */     public abstract JCTree.JCIdent Ident(Name paramName);
/*      */ 
/*      */     public abstract JCTree.JCLiteral Literal(TypeTag paramTypeTag, Object paramObject);
/*      */ 
/*      */     public abstract JCTree.JCPrimitiveTypeTree TypeIdent(TypeTag paramTypeTag);
/*      */ 
/*      */     public abstract JCTree.JCArrayTypeTree TypeArray(JCTree.JCExpression paramJCExpression);
/*      */ 
/*      */     public abstract JCTree.JCTypeApply TypeApply(JCTree.JCExpression paramJCExpression, com.sun.tools.javac.util.List<JCTree.JCExpression> paramList);
/*      */ 
/*      */     public abstract JCTree.JCTypeParameter TypeParameter(Name paramName, com.sun.tools.javac.util.List<JCTree.JCExpression> paramList);
/*      */ 
/*      */     public abstract JCTree.JCWildcard Wildcard(JCTree.TypeBoundKind paramTypeBoundKind, JCTree paramJCTree);
/*      */ 
/*      */     public abstract JCTree.TypeBoundKind TypeBoundKind(BoundKind paramBoundKind);
/*      */ 
/*      */     public abstract JCTree.JCAnnotation Annotation(JCTree paramJCTree, com.sun.tools.javac.util.List<JCTree.JCExpression> paramList);
/*      */ 
/*      */     public abstract JCTree.JCModifiers Modifiers(long paramLong, com.sun.tools.javac.util.List<JCTree.JCAnnotation> paramList);
/*      */ 
/*      */     public abstract JCTree.JCErroneous Erroneous(com.sun.tools.javac.util.List<? extends JCTree> paramList);
/*      */ 
/*      */     public abstract JCTree.LetExpr LetExpr(com.sun.tools.javac.util.List<JCTree.JCVariableDecl> paramList, JCTree paramJCTree);
/*      */   }
/*      */ 
/*      */   public static class JCAnnotatedType extends JCTree.JCExpression
/*      */     implements AnnotatedTypeTree
/*      */   {
/*      */     public com.sun.tools.javac.util.List<JCTree.JCAnnotation> annotations;
/*      */     public JCTree.JCExpression underlyingType;
/*      */ 
/*      */     protected JCAnnotatedType(com.sun.tools.javac.util.List<JCTree.JCAnnotation> paramList, JCTree.JCExpression paramJCExpression)
/*      */     {
/* 2368 */       Assert.check((paramList != null) && (paramList.nonEmpty()));
/* 2369 */       this.annotations = paramList;
/* 2370 */       this.underlyingType = paramJCExpression;
/*      */     }
/*      */     public void accept(JCTree.Visitor paramVisitor) {
/* 2373 */       paramVisitor.visitAnnotatedType(this);
/*      */     }
/* 2375 */     public Tree.Kind getKind() { return Tree.Kind.ANNOTATED_TYPE; } 
/*      */     public com.sun.tools.javac.util.List<JCTree.JCAnnotation> getAnnotations() {
/* 2377 */       return this.annotations;
/*      */     }
/*      */     public JCTree.JCExpression getUnderlyingType() {
/* 2380 */       return this.underlyingType;
/*      */     }
/*      */ 
/*      */     public <R, D> R accept(TreeVisitor<R, D> paramTreeVisitor, D paramD) {
/* 2384 */       return paramTreeVisitor.visitAnnotatedType(this, paramD);
/*      */     }
/*      */ 
/*      */     public JCTree.Tag getTag() {
/* 2388 */       return JCTree.Tag.ANNOTATED_TYPE;
/*      */     }
/*      */   }
/*      */ 
/*      */   public static class JCAnnotation extends JCTree.JCExpression
/*      */     implements AnnotationTree
/*      */   {
/*      */     private JCTree.Tag tag;
/*      */     public JCTree annotationType;
/*      */     public com.sun.tools.javac.util.List<JCTree.JCExpression> args;
/*      */     public Attribute.Compound attribute;
/*      */ 
/*      */     protected JCAnnotation(JCTree.Tag paramTag, JCTree paramJCTree, com.sun.tools.javac.util.List<JCTree.JCExpression> paramList)
/*      */     {
/* 2311 */       this.tag = paramTag;
/* 2312 */       this.annotationType = paramJCTree;
/* 2313 */       this.args = paramList;
/*      */     }
/*      */ 
/*      */     public void accept(JCTree.Visitor paramVisitor) {
/* 2317 */       paramVisitor.visitAnnotation(this);
/*      */     }
/* 2319 */     public Tree.Kind getKind() { return TreeInfo.tagToKind(getTag()); } 
/*      */     public JCTree getAnnotationType() {
/* 2321 */       return this.annotationType;
/*      */     }
/* 2323 */     public com.sun.tools.javac.util.List<JCTree.JCExpression> getArguments() { return this.args; }
/*      */ 
/*      */     public <R, D> R accept(TreeVisitor<R, D> paramTreeVisitor, D paramD)
/*      */     {
/* 2327 */       return paramTreeVisitor.visitAnnotation(this, paramD);
/*      */     }
/*      */ 
/*      */     public JCTree.Tag getTag() {
/* 2331 */       return this.tag;
/*      */     }
/*      */   }
/*      */ 
/*      */   public static class JCArrayAccess extends JCTree.JCExpression
/*      */     implements ArrayAccessTree
/*      */   {
/*      */     public JCTree.JCExpression indexed;
/*      */     public JCTree.JCExpression index;
/*      */ 
/*      */     protected JCArrayAccess(JCTree.JCExpression paramJCExpression1, JCTree.JCExpression paramJCExpression2)
/*      */     {
/* 1862 */       this.indexed = paramJCExpression1;
/* 1863 */       this.index = paramJCExpression2;
/*      */     }
/*      */     public void accept(JCTree.Visitor paramVisitor) {
/* 1866 */       paramVisitor.visitIndexed(this);
/*      */     }
/* 1868 */     public Tree.Kind getKind() { return Tree.Kind.ARRAY_ACCESS; } 
/* 1869 */     public JCTree.JCExpression getExpression() { return this.indexed; } 
/* 1870 */     public JCTree.JCExpression getIndex() { return this.index; }
/*      */ 
/*      */     public <R, D> R accept(TreeVisitor<R, D> paramTreeVisitor, D paramD) {
/* 1873 */       return paramTreeVisitor.visitArrayAccess(this, paramD);
/*      */     }
/*      */ 
/*      */     public JCTree.Tag getTag() {
/* 1877 */       return JCTree.Tag.INDEXED;
/*      */     }
/*      */   }
/*      */ 
/*      */   public static class JCArrayTypeTree extends JCTree.JCExpression
/*      */     implements ArrayTypeTree
/*      */   {
/*      */     public JCTree.JCExpression elemtype;
/*      */ 
/*      */     protected JCArrayTypeTree(JCTree.JCExpression paramJCExpression)
/*      */     {
/* 2107 */       this.elemtype = paramJCExpression;
/*      */     }
/*      */     public void accept(JCTree.Visitor paramVisitor) {
/* 2110 */       paramVisitor.visitTypeArray(this);
/*      */     }
/* 2112 */     public Tree.Kind getKind() { return Tree.Kind.ARRAY_TYPE; } 
/* 2113 */     public JCTree getType() { return this.elemtype; }
/*      */ 
/*      */     public <R, D> R accept(TreeVisitor<R, D> paramTreeVisitor, D paramD) {
/* 2116 */       return paramTreeVisitor.visitArrayType(this, paramD);
/*      */     }
/*      */ 
/*      */     public JCTree.Tag getTag() {
/* 2120 */       return JCTree.Tag.TYPEARRAY;
/*      */     }
/*      */   }
/*      */ 
/*      */   public static class JCAssert extends JCTree.JCStatement
/*      */     implements AssertTree
/*      */   {
/*      */     public JCTree.JCExpression cond;
/*      */     public JCTree.JCExpression detail;
/*      */ 
/*      */     protected JCAssert(JCTree.JCExpression paramJCExpression1, JCTree.JCExpression paramJCExpression2)
/*      */     {
/* 1428 */       this.cond = paramJCExpression1;
/* 1429 */       this.detail = paramJCExpression2;
/*      */     }
/*      */     public void accept(JCTree.Visitor paramVisitor) {
/* 1432 */       paramVisitor.visitAssert(this);
/*      */     }
/* 1434 */     public Tree.Kind getKind() { return Tree.Kind.ASSERT; } 
/* 1435 */     public JCTree.JCExpression getCondition() { return this.cond; } 
/* 1436 */     public JCTree.JCExpression getDetail() { return this.detail; }
/*      */ 
/*      */     public <R, D> R accept(TreeVisitor<R, D> paramTreeVisitor, D paramD) {
/* 1439 */       return paramTreeVisitor.visitAssert(this, paramD);
/*      */     }
/*      */ 
/*      */     public JCTree.Tag getTag() {
/* 1443 */       return JCTree.Tag.ASSERT;
/*      */     }
/*      */   }
/*      */ 
/*      */   public static class JCAssign extends JCTree.JCExpression
/*      */     implements AssignmentTree
/*      */   {
/*      */     public JCTree.JCExpression lhs;
/*      */     public JCTree.JCExpression rhs;
/*      */ 
/*      */     protected JCAssign(JCTree.JCExpression paramJCExpression1, JCTree.JCExpression paramJCExpression2)
/*      */     {
/* 1682 */       this.lhs = paramJCExpression1;
/* 1683 */       this.rhs = paramJCExpression2;
/*      */     }
/*      */     public void accept(JCTree.Visitor paramVisitor) {
/* 1686 */       paramVisitor.visitAssign(this);
/*      */     }
/* 1688 */     public Tree.Kind getKind() { return Tree.Kind.ASSIGNMENT; } 
/* 1689 */     public JCTree.JCExpression getVariable() { return this.lhs; } 
/* 1690 */     public JCTree.JCExpression getExpression() { return this.rhs; }
/*      */ 
/*      */     public <R, D> R accept(TreeVisitor<R, D> paramTreeVisitor, D paramD) {
/* 1693 */       return paramTreeVisitor.visitAssignment(this, paramD);
/*      */     }
/*      */ 
/*      */     public JCTree.Tag getTag() {
/* 1697 */       return JCTree.Tag.ASSIGN;
/*      */     }
/*      */   }
/*      */ 
/*      */   public static class JCAssignOp extends JCTree.JCExpression implements CompoundAssignmentTree {
/*      */     private JCTree.Tag opcode;
/*      */     public JCTree.JCExpression lhs;
/*      */     public JCTree.JCExpression rhs;
/*      */     public Symbol operator;
/*      */ 
/* 1710 */     protected JCAssignOp(JCTree.Tag paramTag, JCTree paramJCTree1, JCTree paramJCTree2, Symbol paramSymbol) { this.opcode = paramTag;
/* 1711 */       this.lhs = ((JCTree.JCExpression)paramJCTree1);
/* 1712 */       this.rhs = ((JCTree.JCExpression)paramJCTree2);
/* 1713 */       this.operator = paramSymbol; }
/*      */ 
/*      */     public void accept(JCTree.Visitor paramVisitor) {
/* 1716 */       paramVisitor.visitAssignop(this);
/*      */     }
/* 1718 */     public Tree.Kind getKind() { return TreeInfo.tagToKind(getTag()); } 
/* 1719 */     public JCTree.JCExpression getVariable() { return this.lhs; } 
/* 1720 */     public JCTree.JCExpression getExpression() { return this.rhs; } 
/*      */     public Symbol getOperator() {
/* 1722 */       return this.operator;
/*      */     }
/*      */ 
/*      */     public <R, D> R accept(TreeVisitor<R, D> paramTreeVisitor, D paramD) {
/* 1726 */       return paramTreeVisitor.visitCompoundAssignment(this, paramD);
/*      */     }
/*      */ 
/*      */     public JCTree.Tag getTag() {
/* 1730 */       return this.opcode;
/*      */     }
/*      */   }
/*      */ 
/*      */   public static class JCBinary extends JCTree.JCExpression
/*      */     implements BinaryTree
/*      */   {
/*      */     private JCTree.Tag opcode;
/*      */     public JCTree.JCExpression lhs;
/*      */     public JCTree.JCExpression rhs;
/*      */     public Symbol operator;
/*      */ 
/*      */     protected JCBinary(JCTree.Tag paramTag, JCTree.JCExpression paramJCExpression1, JCTree.JCExpression paramJCExpression2, Symbol paramSymbol)
/*      */     {
/* 1779 */       this.opcode = paramTag;
/* 1780 */       this.lhs = paramJCExpression1;
/* 1781 */       this.rhs = paramJCExpression2;
/* 1782 */       this.operator = paramSymbol;
/*      */     }
/*      */     public void accept(JCTree.Visitor paramVisitor) {
/* 1785 */       paramVisitor.visitBinary(this);
/*      */     }
/* 1787 */     public Tree.Kind getKind() { return TreeInfo.tagToKind(getTag()); } 
/* 1788 */     public JCTree.JCExpression getLeftOperand() { return this.lhs; } 
/* 1789 */     public JCTree.JCExpression getRightOperand() { return this.rhs; } 
/*      */     public Symbol getOperator() {
/* 1791 */       return this.operator;
/*      */     }
/*      */ 
/*      */     public <R, D> R accept(TreeVisitor<R, D> paramTreeVisitor, D paramD) {
/* 1795 */       return paramTreeVisitor.visitBinary(this, paramD);
/*      */     }
/*      */ 
/*      */     public JCTree.Tag getTag() {
/* 1799 */       return this.opcode;
/*      */     }
/*      */   }
/*      */ 
/*      */   public static class JCBlock extends JCTree.JCStatement
/*      */     implements BlockTree
/*      */   {
/*      */     public long flags;
/*      */     public com.sun.tools.javac.util.List<JCTree.JCStatement> stats;
/*  903 */     public int endpos = -1;
/*      */ 
/*  905 */     protected JCBlock(long paramLong, com.sun.tools.javac.util.List<JCTree.JCStatement> paramList) { this.stats = paramList;
/*  906 */       this.flags = paramLong; }
/*      */ 
/*      */     public void accept(JCTree.Visitor paramVisitor) {
/*  909 */       paramVisitor.visitBlock(this);
/*      */     }
/*  911 */     public Tree.Kind getKind() { return Tree.Kind.BLOCK; } 
/*      */     public com.sun.tools.javac.util.List<JCTree.JCStatement> getStatements() {
/*  913 */       return this.stats;
/*      */     }
/*  915 */     public boolean isStatic() { return (this.flags & 0x8) != 0L; }
/*      */ 
/*      */     public <R, D> R accept(TreeVisitor<R, D> paramTreeVisitor, D paramD) {
/*  918 */       return paramTreeVisitor.visitBlock(this, paramD);
/*      */     }
/*      */ 
/*      */     public JCTree.Tag getTag()
/*      */     {
/*  923 */       return JCTree.Tag.BLOCK;
/*      */     }
/*      */   }
/*      */ 
/*      */   public static class JCBreak extends JCTree.JCStatement
/*      */     implements BreakTree
/*      */   {
/*      */     public Name label;
/*      */     public JCTree target;
/*      */ 
/*      */     protected JCBreak(Name paramName, JCTree paramJCTree)
/*      */     {
/* 1332 */       this.label = paramName;
/* 1333 */       this.target = paramJCTree;
/*      */     }
/*      */     public void accept(JCTree.Visitor paramVisitor) {
/* 1336 */       paramVisitor.visitBreak(this);
/*      */     }
/* 1338 */     public Tree.Kind getKind() { return Tree.Kind.BREAK; } 
/* 1339 */     public Name getLabel() { return this.label; }
/*      */ 
/*      */     public <R, D> R accept(TreeVisitor<R, D> paramTreeVisitor, D paramD) {
/* 1342 */       return paramTreeVisitor.visitBreak(this, paramD);
/*      */     }
/*      */ 
/*      */     public JCTree.Tag getTag() {
/* 1346 */       return JCTree.Tag.BREAK;
/*      */     }
/*      */   }
/*      */ 
/*      */   public static class JCCase extends JCTree.JCStatement
/*      */     implements CaseTree
/*      */   {
/*      */     public JCTree.JCExpression pat;
/*      */     public com.sun.tools.javac.util.List<JCTree.JCStatement> stats;
/*      */ 
/*      */     protected JCCase(JCTree.JCExpression paramJCExpression, com.sun.tools.javac.util.List<JCTree.JCStatement> paramList)
/*      */     {
/* 1109 */       this.pat = paramJCExpression;
/* 1110 */       this.stats = paramList;
/*      */     }
/*      */     public void accept(JCTree.Visitor paramVisitor) {
/* 1113 */       paramVisitor.visitCase(this);
/*      */     }
/* 1115 */     public Tree.Kind getKind() { return Tree.Kind.CASE; } 
/* 1116 */     public JCTree.JCExpression getExpression() { return this.pat; } 
/* 1117 */     public com.sun.tools.javac.util.List<JCTree.JCStatement> getStatements() { return this.stats; }
/*      */ 
/*      */     public <R, D> R accept(TreeVisitor<R, D> paramTreeVisitor, D paramD) {
/* 1120 */       return paramTreeVisitor.visitCase(this, paramD);
/*      */     }
/*      */ 
/*      */     public JCTree.Tag getTag() {
/* 1124 */       return JCTree.Tag.CASE;
/*      */     }
/*      */   }
/*      */ 
/*      */   public static class JCCatch extends JCTree
/*      */     implements CatchTree
/*      */   {
/*      */     public JCTree.JCVariableDecl param;
/*      */     public JCTree.JCBlock body;
/*      */ 
/*      */     protected JCCatch(JCTree.JCVariableDecl paramJCVariableDecl, JCTree.JCBlock paramJCBlock)
/*      */     {
/* 1202 */       this.param = paramJCVariableDecl;
/* 1203 */       this.body = paramJCBlock;
/*      */     }
/*      */     public void accept(JCTree.Visitor paramVisitor) {
/* 1206 */       paramVisitor.visitCatch(this);
/*      */     }
/* 1208 */     public Tree.Kind getKind() { return Tree.Kind.CATCH; } 
/* 1209 */     public JCTree.JCVariableDecl getParameter() { return this.param; } 
/* 1210 */     public JCTree.JCBlock getBlock() { return this.body; }
/*      */ 
/*      */     public <R, D> R accept(TreeVisitor<R, D> paramTreeVisitor, D paramD) {
/* 1213 */       return paramTreeVisitor.visitCatch(this, paramD);
/*      */     }
/*      */ 
/*      */     public JCTree.Tag getTag() {
/* 1217 */       return JCTree.Tag.CATCH;
/*      */     }
/*      */   }
/*      */ 
/*      */   public static class JCClassDecl extends JCTree.JCStatement
/*      */     implements ClassTree
/*      */   {
/*      */     public JCTree.JCModifiers mods;
/*      */     public Name name;
/*      */     public com.sun.tools.javac.util.List<JCTree.JCTypeParameter> typarams;
/*      */     public JCTree.JCExpression extending;
/*      */     public com.sun.tools.javac.util.List<JCTree.JCExpression> implementing;
/*      */     public com.sun.tools.javac.util.List<JCTree> defs;
/*      */     public Symbol.ClassSymbol sym;
/*      */ 
/*      */     protected JCClassDecl(JCTree.JCModifiers paramJCModifiers, Name paramName, com.sun.tools.javac.util.List<JCTree.JCTypeParameter> paramList, JCTree.JCExpression paramJCExpression, com.sun.tools.javac.util.List<JCTree.JCExpression> paramList1, com.sun.tools.javac.util.List<JCTree> paramList2, Symbol.ClassSymbol paramClassSymbol)
/*      */     {
/*  684 */       this.mods = paramJCModifiers;
/*  685 */       this.name = paramName;
/*  686 */       this.typarams = paramList;
/*  687 */       this.extending = paramJCExpression;
/*  688 */       this.implementing = paramList1;
/*  689 */       this.defs = paramList2;
/*  690 */       this.sym = paramClassSymbol;
/*      */     }
/*      */     public void accept(JCTree.Visitor paramVisitor) {
/*  693 */       paramVisitor.visitClassDef(this);
/*      */     }
/*      */     public Tree.Kind getKind() {
/*  696 */       if ((this.mods.flags & 0x2000) != 0L)
/*  697 */         return Tree.Kind.ANNOTATION_TYPE;
/*  698 */       if ((this.mods.flags & 0x200) != 0L)
/*  699 */         return Tree.Kind.INTERFACE;
/*  700 */       if ((this.mods.flags & 0x4000) != 0L) {
/*  701 */         return Tree.Kind.ENUM;
/*      */       }
/*  703 */       return Tree.Kind.CLASS;
/*      */     }
/*      */     public JCTree.JCModifiers getModifiers() {
/*  706 */       return this.mods; } 
/*  707 */     public Name getSimpleName() { return this.name; } 
/*      */     public com.sun.tools.javac.util.List<JCTree.JCTypeParameter> getTypeParameters() {
/*  709 */       return this.typarams;
/*      */     }
/*  711 */     public JCTree.JCExpression getExtendsClause() { return this.extending; } 
/*      */     public com.sun.tools.javac.util.List<JCTree.JCExpression> getImplementsClause() {
/*  713 */       return this.implementing;
/*      */     }
/*      */     public com.sun.tools.javac.util.List<JCTree> getMembers() {
/*  716 */       return this.defs;
/*      */     }
/*      */ 
/*      */     public <R, D> R accept(TreeVisitor<R, D> paramTreeVisitor, D paramD) {
/*  720 */       return paramTreeVisitor.visitClass(this, paramD);
/*      */     }
/*      */ 
/*      */     public JCTree.Tag getTag()
/*      */     {
/*  725 */       return JCTree.Tag.CLASSDEF;
/*      */     }
/*      */   }
/*      */ 
/*      */   public static class JCCompilationUnit extends JCTree
/*      */     implements CompilationUnitTree
/*      */   {
/*      */     public com.sun.tools.javac.util.List<JCTree.JCAnnotation> packageAnnotations;
/*      */     public JCTree.JCExpression pid;
/*      */     public com.sun.tools.javac.util.List<JCTree> defs;
/*      */     public JavaFileObject sourcefile;
/*      */     public Symbol.PackageSymbol packge;
/*      */     public Scope.ImportScope namedImportScope;
/*      */     public Scope.StarImportScope starImportScope;
/*  495 */     public Position.LineMap lineMap = null;
/*      */ 
/*  498 */     public DocCommentTable docComments = null;
/*      */ 
/*  501 */     public EndPosTable endPositions = null;
/*      */ 
/*      */     protected JCCompilationUnit(com.sun.tools.javac.util.List<JCTree.JCAnnotation> paramList, JCTree.JCExpression paramJCExpression, com.sun.tools.javac.util.List<JCTree> paramList1, JavaFileObject paramJavaFileObject, Symbol.PackageSymbol paramPackageSymbol, Scope.ImportScope paramImportScope, Scope.StarImportScope paramStarImportScope)
/*      */     {
/*  509 */       this.packageAnnotations = paramList;
/*  510 */       this.pid = paramJCExpression;
/*  511 */       this.defs = paramList1;
/*  512 */       this.sourcefile = paramJavaFileObject;
/*  513 */       this.packge = paramPackageSymbol;
/*  514 */       this.namedImportScope = paramImportScope;
/*  515 */       this.starImportScope = paramStarImportScope;
/*      */     }
/*      */     public void accept(JCTree.Visitor paramVisitor) {
/*  518 */       paramVisitor.visitTopLevel(this);
/*      */     }
/*  520 */     public Tree.Kind getKind() { return Tree.Kind.COMPILATION_UNIT; } 
/*      */     public com.sun.tools.javac.util.List<JCTree.JCAnnotation> getPackageAnnotations() {
/*  522 */       return this.packageAnnotations;
/*      */     }
/*      */     public com.sun.tools.javac.util.List<JCTree.JCImport> getImports() {
/*  525 */       ListBuffer localListBuffer = new ListBuffer();
/*  526 */       for (JCTree localJCTree : this.defs) {
/*  527 */         if (localJCTree.hasTag(JCTree.Tag.IMPORT))
/*  528 */           localListBuffer.append((JCTree.JCImport)localJCTree);
/*  529 */         else if (!localJCTree.hasTag(JCTree.Tag.SKIP))
/*      */             break;
/*      */       }
/*  532 */       return localListBuffer.toList();
/*      */     }
/*  534 */     public JCTree.JCExpression getPackageName() { return this.pid; } 
/*      */     public JavaFileObject getSourceFile() {
/*  536 */       return this.sourcefile;
/*      */     }
/*      */     public Position.LineMap getLineMap() {
/*  539 */       return this.lineMap;
/*      */     }
/*      */ 
/*      */     public com.sun.tools.javac.util.List<JCTree> getTypeDecls() {
/*  543 */       for (com.sun.tools.javac.util.List localList = this.defs; (!localList.isEmpty()) && 
/*  544 */         (((JCTree)localList.head).hasTag(JCTree.Tag.IMPORT)); localList = localList.tail);
/*  546 */       return localList;
/*      */     }
/*      */ 
/*      */     public <R, D> R accept(TreeVisitor<R, D> paramTreeVisitor, D paramD) {
/*  550 */       return paramTreeVisitor.visitCompilationUnit(this, paramD);
/*      */     }
/*      */ 
/*      */     public JCTree.Tag getTag()
/*      */     {
/*  555 */       return JCTree.Tag.TOPLEVEL;
/*      */     }
/*      */   }
/*      */ 
/*      */   public static class JCConditional extends JCTree.JCPolyExpression
/*      */     implements ConditionalExpressionTree
/*      */   {
/*      */     public JCTree.JCExpression cond;
/*      */     public JCTree.JCExpression truepart;
/*      */     public JCTree.JCExpression falsepart;
/*      */ 
/*      */     protected JCConditional(JCTree.JCExpression paramJCExpression1, JCTree.JCExpression paramJCExpression2, JCTree.JCExpression paramJCExpression3)
/*      */     {
/* 1232 */       this.cond = paramJCExpression1;
/* 1233 */       this.truepart = paramJCExpression2;
/* 1234 */       this.falsepart = paramJCExpression3;
/*      */     }
/*      */     public void accept(JCTree.Visitor paramVisitor) {
/* 1237 */       paramVisitor.visitConditional(this);
/*      */     }
/* 1239 */     public Tree.Kind getKind() { return Tree.Kind.CONDITIONAL_EXPRESSION; } 
/* 1240 */     public JCTree.JCExpression getCondition() { return this.cond; } 
/* 1241 */     public JCTree.JCExpression getTrueExpression() { return this.truepart; } 
/* 1242 */     public JCTree.JCExpression getFalseExpression() { return this.falsepart; }
/*      */ 
/*      */     public <R, D> R accept(TreeVisitor<R, D> paramTreeVisitor, D paramD) {
/* 1245 */       return paramTreeVisitor.visitConditionalExpression(this, paramD);
/*      */     }
/*      */ 
/*      */     public JCTree.Tag getTag() {
/* 1249 */       return JCTree.Tag.CONDEXPR;
/*      */     }
/*      */   }
/*      */ 
/*      */   public static class JCContinue extends JCTree.JCStatement
/*      */     implements ContinueTree
/*      */   {
/*      */     public Name label;
/*      */     public JCTree target;
/*      */ 
/*      */     protected JCContinue(Name paramName, JCTree paramJCTree)
/*      */     {
/* 1357 */       this.label = paramName;
/* 1358 */       this.target = paramJCTree;
/*      */     }
/*      */     public void accept(JCTree.Visitor paramVisitor) {
/* 1361 */       paramVisitor.visitContinue(this);
/*      */     }
/* 1363 */     public Tree.Kind getKind() { return Tree.Kind.CONTINUE; } 
/* 1364 */     public Name getLabel() { return this.label; }
/*      */ 
/*      */     public <R, D> R accept(TreeVisitor<R, D> paramTreeVisitor, D paramD) {
/* 1367 */       return paramTreeVisitor.visitContinue(this, paramD);
/*      */     }
/*      */ 
/*      */     public JCTree.Tag getTag() {
/* 1371 */       return JCTree.Tag.CONTINUE;
/*      */     }
/*      */   }
/*      */ 
/*      */   public static class JCDoWhileLoop extends JCTree.JCStatement
/*      */     implements DoWhileLoopTree
/*      */   {
/*      */     public JCTree.JCStatement body;
/*      */     public JCTree.JCExpression cond;
/*      */ 
/*      */     protected JCDoWhileLoop(JCTree.JCStatement paramJCStatement, JCTree.JCExpression paramJCExpression)
/*      */     {
/*  934 */       this.body = paramJCStatement;
/*  935 */       this.cond = paramJCExpression;
/*      */     }
/*      */     public void accept(JCTree.Visitor paramVisitor) {
/*  938 */       paramVisitor.visitDoLoop(this);
/*      */     }
/*  940 */     public Tree.Kind getKind() { return Tree.Kind.DO_WHILE_LOOP; } 
/*  941 */     public JCTree.JCExpression getCondition() { return this.cond; } 
/*  942 */     public JCTree.JCStatement getStatement() { return this.body; }
/*      */ 
/*      */     public <R, D> R accept(TreeVisitor<R, D> paramTreeVisitor, D paramD) {
/*  945 */       return paramTreeVisitor.visitDoWhileLoop(this, paramD);
/*      */     }
/*      */ 
/*      */     public JCTree.Tag getTag()
/*      */     {
/*  950 */       return JCTree.Tag.DOLOOP;
/*      */     }
/*      */   }
/*      */ 
/*      */   public static class JCEnhancedForLoop extends JCTree.JCStatement
/*      */     implements EnhancedForLoopTree
/*      */   {
/*      */     public JCTree.JCVariableDecl var;
/*      */     public JCTree.JCExpression expr;
/*      */     public JCTree.JCStatement body;
/*      */ 
/*      */     protected JCEnhancedForLoop(JCTree.JCVariableDecl paramJCVariableDecl, JCTree.JCExpression paramJCExpression, JCTree.JCStatement paramJCStatement)
/*      */     {
/* 1030 */       this.var = paramJCVariableDecl;
/* 1031 */       this.expr = paramJCExpression;
/* 1032 */       this.body = paramJCStatement;
/*      */     }
/*      */     public void accept(JCTree.Visitor paramVisitor) {
/* 1035 */       paramVisitor.visitForeachLoop(this);
/*      */     }
/* 1037 */     public Tree.Kind getKind() { return Tree.Kind.ENHANCED_FOR_LOOP; } 
/* 1038 */     public JCTree.JCVariableDecl getVariable() { return this.var; } 
/* 1039 */     public JCTree.JCExpression getExpression() { return this.expr; } 
/* 1040 */     public JCTree.JCStatement getStatement() { return this.body; }
/*      */ 
/*      */     public <R, D> R accept(TreeVisitor<R, D> paramTreeVisitor, D paramD) {
/* 1043 */       return paramTreeVisitor.visitEnhancedForLoop(this, paramD);
/*      */     }
/*      */ 
/*      */     public JCTree.Tag getTag() {
/* 1047 */       return JCTree.Tag.FOREACHLOOP;
/*      */     }
/*      */   }
/*      */ 
/*      */   public static class JCErroneous extends JCTree.JCExpression
/*      */     implements ErroneousTree
/*      */   {
/*      */     public com.sun.tools.javac.util.List<? extends JCTree> errs;
/*      */ 
/*      */     protected JCErroneous(com.sun.tools.javac.util.List<? extends JCTree> paramList)
/*      */     {
/* 2396 */       this.errs = paramList;
/*      */     }
/*      */     public void accept(JCTree.Visitor paramVisitor) {
/* 2399 */       paramVisitor.visitErroneous(this);
/*      */     }
/* 2401 */     public Tree.Kind getKind() { return Tree.Kind.ERRONEOUS; }
/*      */ 
/*      */     public com.sun.tools.javac.util.List<? extends JCTree> getErrorTrees() {
/* 2404 */       return this.errs;
/*      */     }
/*      */ 
/*      */     public <R, D> R accept(TreeVisitor<R, D> paramTreeVisitor, D paramD)
/*      */     {
/* 2409 */       return paramTreeVisitor.visitErroneous(this, paramD);
/*      */     }
/*      */ 
/*      */     public JCTree.Tag getTag() {
/* 2413 */       return JCTree.Tag.ERRONEOUS;
/*      */     }
/*      */   }
/*      */ 
/*      */   public static abstract class JCExpression extends JCTree
/*      */     implements ExpressionTree
/*      */   {
/*      */     public JCExpression setType(Type paramType)
/*      */     {
/*  604 */       super.setType(paramType);
/*  605 */       return this;
/*      */     }
/*      */ 
/*      */     public JCExpression setPos(int paramInt) {
/*  609 */       super.setPos(paramInt);
/*  610 */       return this;
/*      */     }
/*      */     public boolean isPoly() {
/*  613 */       return false; } 
/*  614 */     public boolean isStandalone() { return true; }
/*      */ 
/*      */   }
/*      */ 
/*      */   public static class JCExpressionStatement extends JCTree.JCStatement
/*      */     implements ExpressionStatementTree
/*      */   {
/*      */     public JCTree.JCExpression expr;
/*      */ 
/*      */     protected JCExpressionStatement(JCTree.JCExpression paramJCExpression)
/*      */     {
/* 1293 */       this.expr = paramJCExpression;
/*      */     }
/*      */     public void accept(JCTree.Visitor paramVisitor) {
/* 1296 */       paramVisitor.visitExec(this);
/*      */     }
/* 1298 */     public Tree.Kind getKind() { return Tree.Kind.EXPRESSION_STATEMENT; } 
/* 1299 */     public JCTree.JCExpression getExpression() { return this.expr; }
/*      */ 
/*      */     public <R, D> R accept(TreeVisitor<R, D> paramTreeVisitor, D paramD) {
/* 1302 */       return paramTreeVisitor.visitExpressionStatement(this, paramD);
/*      */     }
/*      */ 
/*      */     public JCTree.Tag getTag() {
/* 1306 */       return JCTree.Tag.EXEC;
/*      */     }
/*      */ 
/*      */     public String toString()
/*      */     {
/* 1312 */       StringWriter localStringWriter = new StringWriter();
/*      */       try {
/* 1314 */         new Pretty(localStringWriter, false).printStat(this);
/*      */       }
/*      */       catch (IOException localIOException)
/*      */       {
/* 1319 */         throw new AssertionError(localIOException);
/*      */       }
/* 1321 */       return localStringWriter.toString();
/*      */     }
/*      */   }
/*      */ 
/*      */   public static class JCFieldAccess extends JCTree.JCExpression
/*      */     implements MemberSelectTree
/*      */   {
/*      */     public JCTree.JCExpression selected;
/*      */     public Name name;
/*      */     public Symbol sym;
/*      */ 
/*      */     protected JCFieldAccess(JCTree.JCExpression paramJCExpression, Name paramName, Symbol paramSymbol)
/*      */     {
/* 1892 */       this.selected = paramJCExpression;
/* 1893 */       this.name = paramName;
/* 1894 */       this.sym = paramSymbol;
/*      */     }
/*      */     public void accept(JCTree.Visitor paramVisitor) {
/* 1897 */       paramVisitor.visitSelect(this);
/*      */     }
/* 1899 */     public Tree.Kind getKind() { return Tree.Kind.MEMBER_SELECT; } 
/* 1900 */     public JCTree.JCExpression getExpression() { return this.selected; }
/*      */ 
/*      */     public <R, D> R accept(TreeVisitor<R, D> paramTreeVisitor, D paramD) {
/* 1903 */       return paramTreeVisitor.visitMemberSelect(this, paramD);
/*      */     }
/* 1905 */     public Name getIdentifier() { return this.name; }
/*      */ 
/*      */     public JCTree.Tag getTag() {
/* 1908 */       return JCTree.Tag.SELECT;
/*      */     }
/*      */   }
/*      */ 
/*      */   public static class JCForLoop extends JCTree.JCStatement
/*      */     implements ForLoopTree
/*      */   {
/*      */     public com.sun.tools.javac.util.List<JCTree.JCStatement> init;
/*      */     public JCTree.JCExpression cond;
/*      */     public com.sun.tools.javac.util.List<JCTree.JCExpressionStatement> step;
/*      */     public JCTree.JCStatement body;
/*      */ 
/*      */     protected JCForLoop(com.sun.tools.javac.util.List<JCTree.JCStatement> paramList, JCTree.JCExpression paramJCExpression, com.sun.tools.javac.util.List<JCTree.JCExpressionStatement> paramList1, JCTree.JCStatement paramJCStatement)
/*      */     {
/*  994 */       this.init = paramList;
/*  995 */       this.cond = paramJCExpression;
/*  996 */       this.step = paramList1;
/*  997 */       this.body = paramJCStatement;
/*      */     }
/*      */     public void accept(JCTree.Visitor paramVisitor) {
/* 1000 */       paramVisitor.visitForLoop(this);
/*      */     }
/* 1002 */     public Tree.Kind getKind() { return Tree.Kind.FOR_LOOP; } 
/* 1003 */     public JCTree.JCExpression getCondition() { return this.cond; } 
/* 1004 */     public JCTree.JCStatement getStatement() { return this.body; } 
/*      */     public com.sun.tools.javac.util.List<JCTree.JCStatement> getInitializer() {
/* 1006 */       return this.init;
/*      */     }
/*      */     public com.sun.tools.javac.util.List<JCTree.JCExpressionStatement> getUpdate() {
/* 1009 */       return this.step;
/*      */     }
/*      */ 
/*      */     public <R, D> R accept(TreeVisitor<R, D> paramTreeVisitor, D paramD) {
/* 1013 */       return paramTreeVisitor.visitForLoop(this, paramD);
/*      */     }
/*      */ 
/*      */     public JCTree.Tag getTag()
/*      */     {
/* 1018 */       return JCTree.Tag.FORLOOP;
/*      */     }
/*      */   }
/*      */ 
/*      */   public static abstract class JCFunctionalExpression extends JCTree.JCPolyExpression
/*      */   {
/*      */     public com.sun.tools.javac.util.List<Type> targets;
/*      */ 
/*      */     public JCFunctionalExpression()
/*      */     {
/*  647 */       this.polyKind = JCTree.JCPolyExpression.PolyKind.POLY;
/*      */     }
/*      */ 
/*      */     public Type getDescriptorType(Types paramTypes)
/*      */     {
/*  654 */       return this.targets.nonEmpty() ? paramTypes.findDescriptorType((Type)this.targets.head) : paramTypes.createErrorType(null);
/*      */     }
/*      */   }
/*      */ 
/*      */   public static class JCIdent extends JCTree.JCExpression
/*      */     implements IdentifierTree
/*      */   {
/*      */     public Name name;
/*      */     public Symbol sym;
/*      */ 
/*      */     protected JCIdent(Name paramName, Symbol paramSymbol)
/*      */     {
/* 2007 */       this.name = paramName;
/* 2008 */       this.sym = paramSymbol;
/*      */     }
/*      */     public void accept(JCTree.Visitor paramVisitor) {
/* 2011 */       paramVisitor.visitIdent(this);
/*      */     }
/* 2013 */     public Tree.Kind getKind() { return Tree.Kind.IDENTIFIER; } 
/* 2014 */     public Name getName() { return this.name; }
/*      */ 
/*      */     public <R, D> R accept(TreeVisitor<R, D> paramTreeVisitor, D paramD) {
/* 2017 */       return paramTreeVisitor.visitIdentifier(this, paramD);
/*      */     }
/*      */ 
/*      */     public JCTree.Tag getTag() {
/* 2021 */       return JCTree.Tag.IDENT;
/*      */     }
/*      */   }
/*      */ 
/*      */   public static class JCIf extends JCTree.JCStatement
/*      */     implements IfTree
/*      */   {
/*      */     public JCTree.JCExpression cond;
/*      */     public JCTree.JCStatement thenpart;
/*      */     public JCTree.JCStatement elsepart;
/*      */ 
/*      */     protected JCIf(JCTree.JCExpression paramJCExpression, JCTree.JCStatement paramJCStatement1, JCTree.JCStatement paramJCStatement2)
/*      */     {
/* 1264 */       this.cond = paramJCExpression;
/* 1265 */       this.thenpart = paramJCStatement1;
/* 1266 */       this.elsepart = paramJCStatement2;
/*      */     }
/*      */     public void accept(JCTree.Visitor paramVisitor) {
/* 1269 */       paramVisitor.visitIf(this);
/*      */     }
/* 1271 */     public Tree.Kind getKind() { return Tree.Kind.IF; } 
/* 1272 */     public JCTree.JCExpression getCondition() { return this.cond; } 
/* 1273 */     public JCTree.JCStatement getThenStatement() { return this.thenpart; } 
/* 1274 */     public JCTree.JCStatement getElseStatement() { return this.elsepart; }
/*      */ 
/*      */     public <R, D> R accept(TreeVisitor<R, D> paramTreeVisitor, D paramD) {
/* 1277 */       return paramTreeVisitor.visitIf(this, paramD);
/*      */     }
/*      */ 
/*      */     public JCTree.Tag getTag() {
/* 1281 */       return JCTree.Tag.IF;
/*      */     }
/*      */   }
/*      */ 
/*      */   public static class JCImport extends JCTree
/*      */     implements ImportTree
/*      */   {
/*      */     public boolean staticImport;
/*      */     public JCTree qualid;
/*      */ 
/*      */     protected JCImport(JCTree paramJCTree, boolean paramBoolean)
/*      */     {
/*  567 */       this.qualid = paramJCTree;
/*  568 */       this.staticImport = paramBoolean;
/*      */     }
/*      */     public void accept(JCTree.Visitor paramVisitor) {
/*  571 */       paramVisitor.visitImport(this);
/*      */     }
/*  573 */     public boolean isStatic() { return this.staticImport; } 
/*  574 */     public JCTree getQualifiedIdentifier() { return this.qualid; } 
/*      */     public Tree.Kind getKind() {
/*  576 */       return Tree.Kind.IMPORT;
/*      */     }
/*      */     public <R, D> R accept(TreeVisitor<R, D> paramTreeVisitor, D paramD) {
/*  579 */       return paramTreeVisitor.visitImport(this, paramD);
/*      */     }
/*      */ 
/*      */     public JCTree.Tag getTag()
/*      */     {
/*  584 */       return JCTree.Tag.IMPORT;
/*      */     }
/*      */   }
/*      */ 
/*      */   public static class JCInstanceOf extends JCTree.JCExpression
/*      */     implements InstanceOfTree
/*      */   {
/*      */     public JCTree.JCExpression expr;
/*      */     public JCTree clazz;
/*      */ 
/*      */     protected JCInstanceOf(JCTree.JCExpression paramJCExpression, JCTree paramJCTree)
/*      */     {
/* 1836 */       this.expr = paramJCExpression;
/* 1837 */       this.clazz = paramJCTree;
/*      */     }
/*      */     public void accept(JCTree.Visitor paramVisitor) {
/* 1840 */       paramVisitor.visitTypeTest(this);
/*      */     }
/* 1842 */     public Tree.Kind getKind() { return Tree.Kind.INSTANCE_OF; } 
/* 1843 */     public JCTree getType() { return this.clazz; } 
/* 1844 */     public JCTree.JCExpression getExpression() { return this.expr; }
/*      */ 
/*      */     public <R, D> R accept(TreeVisitor<R, D> paramTreeVisitor, D paramD) {
/* 1847 */       return paramTreeVisitor.visitInstanceOf(this, paramD);
/*      */     }
/*      */ 
/*      */     public JCTree.Tag getTag() {
/* 1851 */       return JCTree.Tag.TYPETEST;
/*      */     }
/*      */   }
/*      */ 
/*      */   public static class JCLabeledStatement extends JCTree.JCStatement
/*      */     implements LabeledStatementTree
/*      */   {
/*      */     public Name label;
/*      */     public JCTree.JCStatement body;
/*      */ 
/*      */     protected JCLabeledStatement(Name paramName, JCTree.JCStatement paramJCStatement)
/*      */     {
/* 1058 */       this.label = paramName;
/* 1059 */       this.body = paramJCStatement;
/*      */     }
/*      */     public void accept(JCTree.Visitor paramVisitor) {
/* 1062 */       paramVisitor.visitLabelled(this); } 
/* 1063 */     public Tree.Kind getKind() { return Tree.Kind.LABELED_STATEMENT; } 
/* 1064 */     public Name getLabel() { return this.label; } 
/* 1065 */     public JCTree.JCStatement getStatement() { return this.body; }
/*      */ 
/*      */     public <R, D> R accept(TreeVisitor<R, D> paramTreeVisitor, D paramD) {
/* 1068 */       return paramTreeVisitor.visitLabeledStatement(this, paramD);
/*      */     }
/*      */ 
/*      */     public JCTree.Tag getTag() {
/* 1072 */       return JCTree.Tag.LABELLED;
/*      */     }
/*      */   }
/*      */ 
/*      */   public static class JCLambda extends JCTree.JCFunctionalExpression
/*      */     implements LambdaExpressionTree
/*      */   {
/*      */     public com.sun.tools.javac.util.List<JCTree.JCVariableDecl> params;
/*      */     public JCTree body;
/* 1604 */     public boolean canCompleteNormally = true;
/*      */     public ParameterKind paramKind;
/*      */ 
/*      */     public JCLambda(com.sun.tools.javac.util.List<JCTree.JCVariableDecl> paramList, JCTree paramJCTree)
/*      */     {
/* 1609 */       this.params = paramList;
/* 1610 */       this.body = paramJCTree;
/* 1611 */       if ((paramList.isEmpty()) || (((JCTree.JCVariableDecl)paramList.head).vartype != null))
/*      */       {
/* 1613 */         this.paramKind = ParameterKind.EXPLICIT;
/*      */       }
/* 1615 */       else this.paramKind = ParameterKind.IMPLICIT;
/*      */     }
/*      */ 
/*      */     public JCTree.Tag getTag()
/*      */     {
/* 1620 */       return JCTree.Tag.LAMBDA;
/*      */     }
/*      */ 
/*      */     public void accept(JCTree.Visitor paramVisitor) {
/* 1624 */       paramVisitor.visitLambda(this);
/*      */     }
/*      */ 
/*      */     public <R, D> R accept(TreeVisitor<R, D> paramTreeVisitor, D paramD) {
/* 1628 */       return paramTreeVisitor.visitLambdaExpression(this, paramD);
/*      */     }
/*      */     public Tree.Kind getKind() {
/* 1631 */       return Tree.Kind.LAMBDA_EXPRESSION;
/*      */     }
/*      */     public JCTree getBody() {
/* 1634 */       return this.body;
/*      */     }
/*      */     public java.util.List<? extends VariableTree> getParameters() {
/* 1637 */       return this.params;
/*      */     }
/*      */ 
/*      */     public JCLambda setType(Type paramType) {
/* 1641 */       super.setType(paramType);
/* 1642 */       return this;
/*      */     }
/*      */ 
/*      */     public LambdaExpressionTree.BodyKind getBodyKind() {
/* 1646 */       return this.body.hasTag(JCTree.Tag.BLOCK) ? LambdaExpressionTree.BodyKind.STATEMENT : LambdaExpressionTree.BodyKind.EXPRESSION;
/*      */     }
/*      */ 
/*      */     public static enum ParameterKind
/*      */     {
/* 1598 */       IMPLICIT, 
/* 1599 */       EXPLICIT;
/*      */     }
/*      */   }
/*      */ 
/*      */   public static class JCLiteral extends JCTree.JCExpression
/*      */     implements LiteralTree
/*      */   {
/*      */     public TypeTag typetag;
/*      */     public Object value;
/*      */ 
/*      */     protected JCLiteral(TypeTag paramTypeTag, Object paramObject)
/*      */     {
/* 2033 */       this.typetag = paramTypeTag;
/* 2034 */       this.value = paramObject;
/*      */     }
/*      */     public void accept(JCTree.Visitor paramVisitor) {
/* 2037 */       paramVisitor.visitLiteral(this);
/*      */     }
/*      */     public Tree.Kind getKind() {
/* 2040 */       return this.typetag.getKindLiteral();
/*      */     }
/*      */ 
/*      */     public Object getValue() {
/* 2044 */       switch (JCTree.1.$SwitchMap$com$sun$tools$javac$code$TypeTag[this.typetag.ordinal()]) {
/*      */       case 1:
/* 2046 */         int i = ((Integer)this.value).intValue();
/* 2047 */         return Boolean.valueOf(i != 0);
/*      */       case 2:
/* 2049 */         int j = ((Integer)this.value).intValue();
/* 2050 */         int k = (char)j;
/* 2051 */         if (k != j)
/* 2052 */           throw new AssertionError("bad value for char literal");
/* 2053 */         return Character.valueOf(k);
/*      */       }
/* 2055 */       return this.value;
/*      */     }
/*      */ 
/*      */     public <R, D> R accept(TreeVisitor<R, D> paramTreeVisitor, D paramD)
/*      */     {
/* 2060 */       return paramTreeVisitor.visitLiteral(this, paramD);
/*      */     }
/*      */ 
/*      */     public JCLiteral setType(Type paramType) {
/* 2064 */       super.setType(paramType);
/* 2065 */       return this;
/*      */     }
/*      */ 
/*      */     public JCTree.Tag getTag() {
/* 2069 */       return JCTree.Tag.LITERAL;
/*      */     }
/*      */   }
/*      */ 
/*      */   public static class JCMemberReference extends JCTree.JCFunctionalExpression
/*      */     implements MemberReferenceTree
/*      */   {
/*      */     public MemberReferenceTree.ReferenceMode mode;
/*      */     public ReferenceKind kind;
/*      */     public Name name;
/*      */     public JCTree.JCExpression expr;
/*      */     public com.sun.tools.javac.util.List<JCTree.JCExpression> typeargs;
/*      */     public Symbol sym;
/*      */     public Type varargsElement;
/*      */     public JCTree.JCPolyExpression.PolyKind refPolyKind;
/*      */     public boolean ownerAccessible;
/*      */     public OverloadKind overloadKind;
/*      */ 
/*      */     protected JCMemberReference(MemberReferenceTree.ReferenceMode paramReferenceMode, Name paramName, JCTree.JCExpression paramJCExpression, com.sun.tools.javac.util.List<JCTree.JCExpression> paramList)
/*      */     {
/* 1967 */       this.mode = paramReferenceMode;
/* 1968 */       this.name = paramName;
/* 1969 */       this.expr = paramJCExpression;
/* 1970 */       this.typeargs = paramList;
/*      */     }
/*      */     public void accept(JCTree.Visitor paramVisitor) {
/* 1973 */       paramVisitor.visitReference(this);
/*      */     }
/* 1975 */     public Tree.Kind getKind() { return Tree.Kind.MEMBER_REFERENCE; } 
/*      */     public MemberReferenceTree.ReferenceMode getMode() {
/* 1977 */       return this.mode;
/*      */     }
/* 1979 */     public JCTree.JCExpression getQualifierExpression() { return this.expr; } 
/*      */     public Name getName() {
/* 1981 */       return this.name;
/*      */     }
/* 1983 */     public com.sun.tools.javac.util.List<JCTree.JCExpression> getTypeArguments() { return this.typeargs; }
/*      */ 
/*      */     public <R, D> R accept(TreeVisitor<R, D> paramTreeVisitor, D paramD)
/*      */     {
/* 1987 */       return paramTreeVisitor.visitMemberReference(this, paramD);
/*      */     }
/*      */ 
/*      */     public JCTree.Tag getTag() {
/* 1991 */       return JCTree.Tag.REFERENCE;
/*      */     }
/*      */     public boolean hasKind(ReferenceKind paramReferenceKind) {
/* 1994 */       return this.kind == paramReferenceKind;
/*      */     }
/*      */ 
/*      */     public static enum OverloadKind
/*      */     {
/* 1929 */       OVERLOADED, 
/* 1930 */       UNOVERLOADED;
/*      */     }
/*      */ 
/*      */     public static enum ReferenceKind
/*      */     {
/* 1939 */       SUPER(MemberReferenceTree.ReferenceMode.INVOKE, false), 
/*      */ 
/* 1941 */       UNBOUND(MemberReferenceTree.ReferenceMode.INVOKE, true), 
/*      */ 
/* 1943 */       STATIC(MemberReferenceTree.ReferenceMode.INVOKE, false), 
/*      */ 
/* 1945 */       BOUND(MemberReferenceTree.ReferenceMode.INVOKE, false), 
/*      */ 
/* 1947 */       IMPLICIT_INNER(MemberReferenceTree.ReferenceMode.NEW, false), 
/*      */ 
/* 1949 */       TOPLEVEL(MemberReferenceTree.ReferenceMode.NEW, false), 
/*      */ 
/* 1951 */       ARRAY_CTOR(MemberReferenceTree.ReferenceMode.NEW, false);
/*      */ 
/*      */       final MemberReferenceTree.ReferenceMode mode;
/*      */       final boolean unbound;
/*      */ 
/* 1957 */       private ReferenceKind(MemberReferenceTree.ReferenceMode paramReferenceMode, boolean paramBoolean) { this.mode = paramReferenceMode;
/* 1958 */         this.unbound = paramBoolean; }
/*      */ 
/*      */       public boolean isUnbound()
/*      */       {
/* 1962 */         return this.unbound;
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   public static class JCMethodDecl extends JCTree
/*      */     implements MethodTree
/*      */   {
/*      */     public JCTree.JCModifiers mods;
/*      */     public Name name;
/*      */     public JCTree.JCExpression restype;
/*      */     public com.sun.tools.javac.util.List<JCTree.JCTypeParameter> typarams;
/*      */     public JCTree.JCVariableDecl recvparam;
/*      */     public com.sun.tools.javac.util.List<JCTree.JCVariableDecl> params;
/*      */     public com.sun.tools.javac.util.List<JCTree.JCExpression> thrown;
/*      */     public JCTree.JCBlock body;
/*      */     public JCTree.JCExpression defaultValue;
/*      */     public Symbol.MethodSymbol sym;
/*      */ 
/*      */     protected JCMethodDecl(JCTree.JCModifiers paramJCModifiers, Name paramName, JCTree.JCExpression paramJCExpression1, com.sun.tools.javac.util.List<JCTree.JCTypeParameter> paramList, JCTree.JCVariableDecl paramJCVariableDecl, com.sun.tools.javac.util.List<JCTree.JCVariableDecl> paramList1, com.sun.tools.javac.util.List<JCTree.JCExpression> paramList2, JCTree.JCBlock paramJCBlock, JCTree.JCExpression paramJCExpression2, Symbol.MethodSymbol paramMethodSymbol)
/*      */     {
/*  764 */       this.mods = paramJCModifiers;
/*  765 */       this.name = paramName;
/*  766 */       this.restype = paramJCExpression1;
/*  767 */       this.typarams = paramList;
/*  768 */       this.params = paramList1;
/*  769 */       this.recvparam = paramJCVariableDecl;
/*      */ 
/*  772 */       this.thrown = paramList2;
/*  773 */       this.body = paramJCBlock;
/*  774 */       this.defaultValue = paramJCExpression2;
/*  775 */       this.sym = paramMethodSymbol;
/*      */     }
/*      */     public void accept(JCTree.Visitor paramVisitor) {
/*  778 */       paramVisitor.visitMethodDef(this);
/*      */     }
/*  780 */     public Tree.Kind getKind() { return Tree.Kind.METHOD; } 
/*  781 */     public JCTree.JCModifiers getModifiers() { return this.mods; } 
/*  782 */     public Name getName() { return this.name; } 
/*  783 */     public JCTree getReturnType() { return this.restype; } 
/*      */     public com.sun.tools.javac.util.List<JCTree.JCTypeParameter> getTypeParameters() {
/*  785 */       return this.typarams;
/*      */     }
/*      */     public com.sun.tools.javac.util.List<JCTree.JCVariableDecl> getParameters() {
/*  788 */       return this.params;
/*      */     }
/*  790 */     public JCTree.JCVariableDecl getReceiverParameter() { return this.recvparam; } 
/*      */     public com.sun.tools.javac.util.List<JCTree.JCExpression> getThrows() {
/*  792 */       return this.thrown;
/*      */     }
/*  794 */     public JCTree.JCBlock getBody() { return this.body; } 
/*      */     public JCTree getDefaultValue() {
/*  796 */       return this.defaultValue;
/*      */     }
/*      */ 
/*      */     public <R, D> R accept(TreeVisitor<R, D> paramTreeVisitor, D paramD) {
/*  800 */       return paramTreeVisitor.visitMethod(this, paramD);
/*      */     }
/*      */ 
/*      */     public JCTree.Tag getTag()
/*      */     {
/*  805 */       return JCTree.Tag.METHODDEF;
/*      */     }
/*      */   }
/*      */ 
/*      */   public static class JCMethodInvocation extends JCTree.JCPolyExpression
/*      */     implements MethodInvocationTree
/*      */   {
/*      */     public com.sun.tools.javac.util.List<JCTree.JCExpression> typeargs;
/*      */     public JCTree.JCExpression meth;
/*      */     public com.sun.tools.javac.util.List<JCTree.JCExpression> args;
/*      */     public Type varargsElement;
/*      */ 
/*      */     protected JCMethodInvocation(com.sun.tools.javac.util.List<JCTree.JCExpression> paramList1, JCTree.JCExpression paramJCExpression, com.sun.tools.javac.util.List<JCTree.JCExpression> paramList2)
/*      */     {
/* 1459 */       this.typeargs = (paramList1 == null ? com.sun.tools.javac.util.List.nil() : paramList1);
/*      */ 
/* 1461 */       this.meth = paramJCExpression;
/* 1462 */       this.args = paramList2;
/*      */     }
/*      */     public void accept(JCTree.Visitor paramVisitor) {
/* 1465 */       paramVisitor.visitApply(this);
/*      */     }
/* 1467 */     public Tree.Kind getKind() { return Tree.Kind.METHOD_INVOCATION; } 
/*      */     public com.sun.tools.javac.util.List<JCTree.JCExpression> getTypeArguments() {
/* 1469 */       return this.typeargs;
/*      */     }
/* 1471 */     public JCTree.JCExpression getMethodSelect() { return this.meth; } 
/*      */     public com.sun.tools.javac.util.List<JCTree.JCExpression> getArguments() {
/* 1473 */       return this.args;
/*      */     }
/*      */ 
/*      */     public <R, D> R accept(TreeVisitor<R, D> paramTreeVisitor, D paramD) {
/* 1477 */       return paramTreeVisitor.visitMethodInvocation(this, paramD);
/*      */     }
/*      */ 
/*      */     public JCMethodInvocation setType(Type paramType) {
/* 1481 */       super.setType(paramType);
/* 1482 */       return this;
/*      */     }
/*      */ 
/*      */     public JCTree.Tag getTag() {
/* 1486 */       return JCTree.Tag.APPLY;
/*      */     }
/*      */   }
/*      */ 
/*      */   public static class JCModifiers extends JCTree
/*      */     implements ModifiersTree
/*      */   {
/*      */     public long flags;
/*      */     public com.sun.tools.javac.util.List<JCTree.JCAnnotation> annotations;
/*      */ 
/*      */     protected JCModifiers(long paramLong, com.sun.tools.javac.util.List<JCTree.JCAnnotation> paramList)
/*      */     {
/* 2339 */       this.flags = paramLong;
/* 2340 */       this.annotations = paramList;
/*      */     }
/*      */     public void accept(JCTree.Visitor paramVisitor) {
/* 2343 */       paramVisitor.visitModifiers(this);
/*      */     }
/* 2345 */     public Tree.Kind getKind() { return Tree.Kind.MODIFIERS; } 
/*      */     public Set<Modifier> getFlags() {
/* 2347 */       return Flags.asModifierSet(this.flags);
/*      */     }
/*      */     public com.sun.tools.javac.util.List<JCTree.JCAnnotation> getAnnotations() {
/* 2350 */       return this.annotations;
/*      */     }
/*      */ 
/*      */     public <R, D> R accept(TreeVisitor<R, D> paramTreeVisitor, D paramD) {
/* 2354 */       return paramTreeVisitor.visitModifiers(this, paramD);
/*      */     }
/*      */ 
/*      */     public JCTree.Tag getTag() {
/* 2358 */       return JCTree.Tag.MODIFIERS;
/*      */     }
/*      */   }
/*      */ 
/*      */   public static class JCNewArray extends JCTree.JCExpression
/*      */     implements NewArrayTree
/*      */   {
/*      */     public JCTree.JCExpression elemtype;
/*      */     public com.sun.tools.javac.util.List<JCTree.JCExpression> dims;
/*      */     public com.sun.tools.javac.util.List<JCTree.JCAnnotation> annotations;
/*      */     public com.sun.tools.javac.util.List<com.sun.tools.javac.util.List<JCTree.JCAnnotation>> dimAnnotations;
/*      */     public com.sun.tools.javac.util.List<JCTree.JCExpression> elems;
/*      */ 
/*      */     protected JCNewArray(JCTree.JCExpression paramJCExpression, com.sun.tools.javac.util.List<JCTree.JCExpression> paramList1, com.sun.tools.javac.util.List<JCTree.JCExpression> paramList2)
/*      */     {
/* 1555 */       this.elemtype = paramJCExpression;
/* 1556 */       this.dims = paramList1;
/* 1557 */       this.annotations = com.sun.tools.javac.util.List.nil();
/* 1558 */       this.dimAnnotations = com.sun.tools.javac.util.List.nil();
/* 1559 */       this.elems = paramList2;
/*      */     }
/*      */     public void accept(JCTree.Visitor paramVisitor) {
/* 1562 */       paramVisitor.visitNewArray(this);
/*      */     }
/* 1564 */     public Tree.Kind getKind() { return Tree.Kind.NEW_ARRAY; } 
/* 1565 */     public JCTree.JCExpression getType() { return this.elemtype; } 
/*      */     public com.sun.tools.javac.util.List<JCTree.JCExpression> getDimensions() {
/* 1567 */       return this.dims;
/*      */     }
/*      */     public com.sun.tools.javac.util.List<JCTree.JCExpression> getInitializers() {
/* 1570 */       return this.elems;
/*      */     }
/*      */ 
/*      */     public <R, D> R accept(TreeVisitor<R, D> paramTreeVisitor, D paramD) {
/* 1574 */       return paramTreeVisitor.visitNewArray(this, paramD);
/*      */     }
/*      */ 
/*      */     public JCTree.Tag getTag() {
/* 1578 */       return JCTree.Tag.NEWARRAY;
/*      */     }
/*      */ 
/*      */     public com.sun.tools.javac.util.List<JCTree.JCAnnotation> getAnnotations()
/*      */     {
/* 1583 */       return this.annotations;
/*      */     }
/*      */ 
/*      */     public com.sun.tools.javac.util.List<com.sun.tools.javac.util.List<JCTree.JCAnnotation>> getDimAnnotations()
/*      */     {
/* 1588 */       return this.dimAnnotations;
/*      */     }
/*      */   }
/*      */ 
/*      */   public static class JCNewClass extends JCTree.JCPolyExpression
/*      */     implements NewClassTree
/*      */   {
/*      */     public JCTree.JCExpression encl;
/*      */     public com.sun.tools.javac.util.List<JCTree.JCExpression> typeargs;
/*      */     public JCTree.JCExpression clazz;
/*      */     public com.sun.tools.javac.util.List<JCTree.JCExpression> args;
/*      */     public JCTree.JCClassDecl def;
/*      */     public Symbol constructor;
/*      */     public Type varargsElement;
/*      */     public Type constructorType;
/*      */ 
/*      */     protected JCNewClass(JCTree.JCExpression paramJCExpression1, com.sun.tools.javac.util.List<JCTree.JCExpression> paramList1, JCTree.JCExpression paramJCExpression2, com.sun.tools.javac.util.List<JCTree.JCExpression> paramList2, JCTree.JCClassDecl paramJCClassDecl)
/*      */     {
/* 1508 */       this.encl = paramJCExpression1;
/* 1509 */       this.typeargs = (paramList1 == null ? com.sun.tools.javac.util.List.nil() : paramList1);
/*      */ 
/* 1511 */       this.clazz = paramJCExpression2;
/* 1512 */       this.args = paramList2;
/* 1513 */       this.def = paramJCClassDecl;
/*      */     }
/*      */     public void accept(JCTree.Visitor paramVisitor) {
/* 1516 */       paramVisitor.visitNewClass(this);
/*      */     }
/* 1518 */     public Tree.Kind getKind() { return Tree.Kind.NEW_CLASS; } 
/*      */     public JCTree.JCExpression getEnclosingExpression() {
/* 1520 */       return this.encl;
/*      */     }
/*      */     public com.sun.tools.javac.util.List<JCTree.JCExpression> getTypeArguments() {
/* 1523 */       return this.typeargs;
/*      */     }
/* 1525 */     public JCTree.JCExpression getIdentifier() { return this.clazz; } 
/*      */     public com.sun.tools.javac.util.List<JCTree.JCExpression> getArguments() {
/* 1527 */       return this.args;
/*      */     }
/* 1529 */     public JCTree.JCClassDecl getClassBody() { return this.def; }
/*      */ 
/*      */     public <R, D> R accept(TreeVisitor<R, D> paramTreeVisitor, D paramD) {
/* 1532 */       return paramTreeVisitor.visitNewClass(this, paramD);
/*      */     }
/*      */ 
/*      */     public JCTree.Tag getTag() {
/* 1536 */       return JCTree.Tag.NEWCLASS;
/*      */     }
/*      */   }
/*      */ 
/*      */   public static class JCParens extends JCTree.JCExpression
/*      */     implements ParenthesizedTree
/*      */   {
/*      */     public JCTree.JCExpression expr;
/*      */ 
/*      */     protected JCParens(JCTree.JCExpression paramJCExpression)
/*      */     {
/* 1658 */       this.expr = paramJCExpression;
/*      */     }
/*      */     public void accept(JCTree.Visitor paramVisitor) {
/* 1661 */       paramVisitor.visitParens(this);
/*      */     }
/* 1663 */     public Tree.Kind getKind() { return Tree.Kind.PARENTHESIZED; } 
/* 1664 */     public JCTree.JCExpression getExpression() { return this.expr; }
/*      */ 
/*      */     public <R, D> R accept(TreeVisitor<R, D> paramTreeVisitor, D paramD) {
/* 1667 */       return paramTreeVisitor.visitParenthesized(this, paramD);
/*      */     }
/*      */ 
/*      */     public JCTree.Tag getTag() {
/* 1671 */       return JCTree.Tag.PARENS;
/*      */     }
/*      */   }
/*      */ 
/*      */   public static abstract class JCPolyExpression extends JCTree.JCExpression
/*      */   {
/*      */     public PolyKind polyKind;
/*      */ 
/*      */     public boolean isPoly()
/*      */     {
/*  636 */       return this.polyKind == PolyKind.POLY; } 
/*  637 */     public boolean isStandalone() { return this.polyKind == PolyKind.STANDALONE; }
/*      */ 
/*      */ 
/*      */     public static enum PolyKind
/*      */     {
/*  628 */       STANDALONE, 
/*      */ 
/*  630 */       POLY;
/*      */     }
/*      */   }
/*      */ 
/*      */   public static class JCPrimitiveTypeTree extends JCTree.JCExpression
/*      */     implements PrimitiveTypeTree
/*      */   {
/*      */     public TypeTag typetag;
/*      */ 
/*      */     protected JCPrimitiveTypeTree(TypeTag paramTypeTag)
/*      */     {
/* 2081 */       this.typetag = paramTypeTag;
/*      */     }
/*      */     public void accept(JCTree.Visitor paramVisitor) {
/* 2084 */       paramVisitor.visitTypeIdent(this);
/*      */     }
/* 2086 */     public Tree.Kind getKind() { return Tree.Kind.PRIMITIVE_TYPE; } 
/*      */     public TypeKind getPrimitiveTypeKind() {
/* 2088 */       return this.typetag.getPrimitiveTypeKind();
/*      */     }
/*      */ 
/*      */     public <R, D> R accept(TreeVisitor<R, D> paramTreeVisitor, D paramD)
/*      */     {
/* 2093 */       return paramTreeVisitor.visitPrimitiveType(this, paramD);
/*      */     }
/*      */ 
/*      */     public JCTree.Tag getTag() {
/* 2097 */       return JCTree.Tag.TYPEIDENT;
/*      */     }
/*      */   }
/*      */ 
/*      */   public static class JCReturn extends JCTree.JCStatement
/*      */     implements ReturnTree
/*      */   {
/*      */     public JCTree.JCExpression expr;
/*      */ 
/*      */     protected JCReturn(JCTree.JCExpression paramJCExpression)
/*      */     {
/* 1381 */       this.expr = paramJCExpression;
/*      */     }
/*      */     public void accept(JCTree.Visitor paramVisitor) {
/* 1384 */       paramVisitor.visitReturn(this);
/*      */     }
/* 1386 */     public Tree.Kind getKind() { return Tree.Kind.RETURN; } 
/* 1387 */     public JCTree.JCExpression getExpression() { return this.expr; }
/*      */ 
/*      */     public <R, D> R accept(TreeVisitor<R, D> paramTreeVisitor, D paramD) {
/* 1390 */       return paramTreeVisitor.visitReturn(this, paramD);
/*      */     }
/*      */ 
/*      */     public JCTree.Tag getTag() {
/* 1394 */       return JCTree.Tag.RETURN;
/*      */     }
/*      */   }
/*      */ 
/*      */   public static class JCSkip extends JCTree.JCStatement
/*      */     implements EmptyStatementTree
/*      */   {
/*      */     public void accept(JCTree.Visitor paramVisitor)
/*      */     {
/*  880 */       paramVisitor.visitSkip(this);
/*      */     }
/*  882 */     public Tree.Kind getKind() { return Tree.Kind.EMPTY_STATEMENT; }
/*      */ 
/*      */     public <R, D> R accept(TreeVisitor<R, D> paramTreeVisitor, D paramD) {
/*  885 */       return paramTreeVisitor.visitEmptyStatement(this, paramD);
/*      */     }
/*      */ 
/*      */     public JCTree.Tag getTag()
/*      */     {
/*  890 */       return JCTree.Tag.SKIP;
/*      */     }
/*      */   }
/*      */ 
/*      */   public static abstract class JCStatement extends JCTree
/*      */     implements StatementTree
/*      */   {
/*      */     public JCStatement setType(Type paramType)
/*      */     {
/*  591 */       super.setType(paramType);
/*  592 */       return this;
/*      */     }
/*      */ 
/*      */     public JCStatement setPos(int paramInt) {
/*  596 */       super.setPos(paramInt);
/*  597 */       return this;
/*      */     }
/*      */   }
/*      */ 
/*      */   public static class JCSwitch extends JCTree.JCStatement
/*      */     implements SwitchTree
/*      */   {
/*      */     public JCTree.JCExpression selector;
/*      */     public com.sun.tools.javac.util.List<JCTree.JCCase> cases;
/*      */ 
/*      */     protected JCSwitch(JCTree.JCExpression paramJCExpression, com.sun.tools.javac.util.List<JCTree.JCCase> paramList)
/*      */     {
/* 1083 */       this.selector = paramJCExpression;
/* 1084 */       this.cases = paramList;
/*      */     }
/*      */     public void accept(JCTree.Visitor paramVisitor) {
/* 1087 */       paramVisitor.visitSwitch(this);
/*      */     }
/* 1089 */     public Tree.Kind getKind() { return Tree.Kind.SWITCH; } 
/* 1090 */     public JCTree.JCExpression getExpression() { return this.selector; } 
/* 1091 */     public com.sun.tools.javac.util.List<JCTree.JCCase> getCases() { return this.cases; }
/*      */ 
/*      */     public <R, D> R accept(TreeVisitor<R, D> paramTreeVisitor, D paramD) {
/* 1094 */       return paramTreeVisitor.visitSwitch(this, paramD);
/*      */     }
/*      */ 
/*      */     public JCTree.Tag getTag() {
/* 1098 */       return JCTree.Tag.SWITCH;
/*      */     }
/*      */   }
/*      */ 
/*      */   public static class JCSynchronized extends JCTree.JCStatement
/*      */     implements SynchronizedTree
/*      */   {
/*      */     public JCTree.JCExpression lock;
/*      */     public JCTree.JCBlock body;
/*      */ 
/*      */     protected JCSynchronized(JCTree.JCExpression paramJCExpression, JCTree.JCBlock paramJCBlock)
/*      */     {
/* 1135 */       this.lock = paramJCExpression;
/* 1136 */       this.body = paramJCBlock;
/*      */     }
/*      */     public void accept(JCTree.Visitor paramVisitor) {
/* 1139 */       paramVisitor.visitSynchronized(this);
/*      */     }
/* 1141 */     public Tree.Kind getKind() { return Tree.Kind.SYNCHRONIZED; } 
/* 1142 */     public JCTree.JCExpression getExpression() { return this.lock; } 
/* 1143 */     public JCTree.JCBlock getBlock() { return this.body; }
/*      */ 
/*      */     public <R, D> R accept(TreeVisitor<R, D> paramTreeVisitor, D paramD) {
/* 1146 */       return paramTreeVisitor.visitSynchronized(this, paramD);
/*      */     }
/*      */ 
/*      */     public JCTree.Tag getTag() {
/* 1150 */       return JCTree.Tag.SYNCHRONIZED;
/*      */     }
/*      */   }
/*      */ 
/*      */   public static class JCThrow extends JCTree.JCStatement
/*      */     implements ThrowTree
/*      */   {
/*      */     public JCTree.JCExpression expr;
/*      */ 
/*      */     protected JCThrow(JCTree.JCExpression paramJCExpression)
/*      */     {
/* 1404 */       this.expr = paramJCExpression;
/*      */     }
/*      */     public void accept(JCTree.Visitor paramVisitor) {
/* 1407 */       paramVisitor.visitThrow(this);
/*      */     }
/* 1409 */     public Tree.Kind getKind() { return Tree.Kind.THROW; } 
/* 1410 */     public JCTree.JCExpression getExpression() { return this.expr; }
/*      */ 
/*      */     public <R, D> R accept(TreeVisitor<R, D> paramTreeVisitor, D paramD) {
/* 1413 */       return paramTreeVisitor.visitThrow(this, paramD);
/*      */     }
/*      */ 
/*      */     public JCTree.Tag getTag() {
/* 1417 */       return JCTree.Tag.THROW;
/*      */     }
/*      */   }
/*      */ 
/*      */   public static class JCTry extends JCTree.JCStatement
/*      */     implements TryTree
/*      */   {
/*      */     public JCTree.JCBlock body;
/*      */     public com.sun.tools.javac.util.List<JCTree.JCCatch> catchers;
/*      */     public JCTree.JCBlock finalizer;
/*      */     public com.sun.tools.javac.util.List<JCTree> resources;
/*      */     public boolean finallyCanCompleteNormally;
/*      */ 
/*      */     protected JCTry(com.sun.tools.javac.util.List<JCTree> paramList, JCTree.JCBlock paramJCBlock1, com.sun.tools.javac.util.List<JCTree.JCCatch> paramList1, JCTree.JCBlock paramJCBlock2)
/*      */     {
/* 1167 */       this.body = paramJCBlock1;
/* 1168 */       this.catchers = paramList1;
/* 1169 */       this.finalizer = paramJCBlock2;
/* 1170 */       this.resources = paramList;
/*      */     }
/*      */     public void accept(JCTree.Visitor paramVisitor) {
/* 1173 */       paramVisitor.visitTry(this);
/*      */     }
/* 1175 */     public Tree.Kind getKind() { return Tree.Kind.TRY; } 
/* 1176 */     public JCTree.JCBlock getBlock() { return this.body; } 
/*      */     public com.sun.tools.javac.util.List<JCTree.JCCatch> getCatches() {
/* 1178 */       return this.catchers;
/*      */     }
/* 1180 */     public JCTree.JCBlock getFinallyBlock() { return this.finalizer; }
/*      */ 
/*      */     public <R, D> R accept(TreeVisitor<R, D> paramTreeVisitor, D paramD) {
/* 1183 */       return paramTreeVisitor.visitTry(this, paramD);
/*      */     }
/*      */ 
/*      */     public com.sun.tools.javac.util.List<JCTree> getResources() {
/* 1187 */       return this.resources;
/*      */     }
/*      */ 
/*      */     public JCTree.Tag getTag() {
/* 1191 */       return JCTree.Tag.TRY;
/*      */     }
/*      */   }
/*      */ 
/*      */   public static class JCTypeApply extends JCTree.JCExpression
/*      */     implements ParameterizedTypeTree
/*      */   {
/*      */     public JCTree.JCExpression clazz;
/*      */     public com.sun.tools.javac.util.List<JCTree.JCExpression> arguments;
/*      */ 
/*      */     protected JCTypeApply(JCTree.JCExpression paramJCExpression, com.sun.tools.javac.util.List<JCTree.JCExpression> paramList)
/*      */     {
/* 2131 */       this.clazz = paramJCExpression;
/* 2132 */       this.arguments = paramList;
/*      */     }
/*      */     public void accept(JCTree.Visitor paramVisitor) {
/* 2135 */       paramVisitor.visitTypeApply(this);
/*      */     }
/* 2137 */     public Tree.Kind getKind() { return Tree.Kind.PARAMETERIZED_TYPE; } 
/* 2138 */     public JCTree getType() { return this.clazz; } 
/*      */     public com.sun.tools.javac.util.List<JCTree.JCExpression> getTypeArguments() {
/* 2140 */       return this.arguments;
/*      */     }
/*      */ 
/*      */     public <R, D> R accept(TreeVisitor<R, D> paramTreeVisitor, D paramD) {
/* 2144 */       return paramTreeVisitor.visitParameterizedType(this, paramD);
/*      */     }
/*      */ 
/*      */     public JCTree.Tag getTag() {
/* 2148 */       return JCTree.Tag.TYPEAPPLY;
/*      */     }
/*      */   }
/*      */ 
/*      */   public static class JCTypeCast extends JCTree.JCExpression
/*      */     implements TypeCastTree
/*      */   {
/*      */     public JCTree clazz;
/*      */     public JCTree.JCExpression expr;
/*      */ 
/*      */     protected JCTypeCast(JCTree paramJCTree, JCTree.JCExpression paramJCExpression)
/*      */     {
/* 1810 */       this.clazz = paramJCTree;
/* 1811 */       this.expr = paramJCExpression;
/*      */     }
/*      */     public void accept(JCTree.Visitor paramVisitor) {
/* 1814 */       paramVisitor.visitTypeCast(this);
/*      */     }
/* 1816 */     public Tree.Kind getKind() { return Tree.Kind.TYPE_CAST; } 
/* 1817 */     public JCTree getType() { return this.clazz; } 
/* 1818 */     public JCTree.JCExpression getExpression() { return this.expr; }
/*      */ 
/*      */     public <R, D> R accept(TreeVisitor<R, D> paramTreeVisitor, D paramD) {
/* 1821 */       return paramTreeVisitor.visitTypeCast(this, paramD);
/*      */     }
/*      */ 
/*      */     public JCTree.Tag getTag() {
/* 1825 */       return JCTree.Tag.TYPECAST;
/*      */     }
/*      */   }
/*      */ 
/*      */   public static class JCTypeIntersection extends JCTree.JCExpression
/*      */     implements IntersectionTypeTree
/*      */   {
/*      */     public com.sun.tools.javac.util.List<JCTree.JCExpression> bounds;
/*      */ 
/*      */     protected JCTypeIntersection(com.sun.tools.javac.util.List<JCTree.JCExpression> paramList)
/*      */     {
/* 2188 */       this.bounds = paramList;
/*      */     }
/*      */     public void accept(JCTree.Visitor paramVisitor) {
/* 2191 */       paramVisitor.visitTypeIntersection(this);
/*      */     }
/* 2193 */     public Tree.Kind getKind() { return Tree.Kind.INTERSECTION_TYPE; }
/*      */ 
/*      */     public com.sun.tools.javac.util.List<JCTree.JCExpression> getBounds() {
/* 2196 */       return this.bounds;
/*      */     }
/*      */ 
/*      */     public <R, D> R accept(TreeVisitor<R, D> paramTreeVisitor, D paramD) {
/* 2200 */       return paramTreeVisitor.visitIntersectionType(this, paramD);
/*      */     }
/*      */ 
/*      */     public JCTree.Tag getTag() {
/* 2204 */       return JCTree.Tag.TYPEINTERSECTION;
/*      */     }
/*      */   }
/*      */ 
/*      */   public static class JCTypeParameter extends JCTree
/*      */     implements TypeParameterTree
/*      */   {
/*      */     public Name name;
/*      */     public com.sun.tools.javac.util.List<JCTree.JCExpression> bounds;
/*      */     public com.sun.tools.javac.util.List<JCTree.JCAnnotation> annotations;
/*      */ 
/*      */     protected JCTypeParameter(Name paramName, com.sun.tools.javac.util.List<JCTree.JCExpression> paramList, com.sun.tools.javac.util.List<JCTree.JCAnnotation> paramList1)
/*      */     {
/* 2219 */       this.name = paramName;
/* 2220 */       this.bounds = paramList;
/* 2221 */       this.annotations = paramList1;
/*      */     }
/*      */     public void accept(JCTree.Visitor paramVisitor) {
/* 2224 */       paramVisitor.visitTypeParameter(this);
/*      */     }
/* 2226 */     public Tree.Kind getKind() { return Tree.Kind.TYPE_PARAMETER; } 
/* 2227 */     public Name getName() { return this.name; } 
/*      */     public com.sun.tools.javac.util.List<JCTree.JCExpression> getBounds() {
/* 2229 */       return this.bounds;
/*      */     }
/*      */     public com.sun.tools.javac.util.List<JCTree.JCAnnotation> getAnnotations() {
/* 2232 */       return this.annotations;
/*      */     }
/*      */ 
/*      */     public <R, D> R accept(TreeVisitor<R, D> paramTreeVisitor, D paramD) {
/* 2236 */       return paramTreeVisitor.visitTypeParameter(this, paramD);
/*      */     }
/*      */ 
/*      */     public JCTree.Tag getTag() {
/* 2240 */       return JCTree.Tag.TYPEPARAMETER;
/*      */     }
/*      */   }
/*      */ 
/*      */   public static class JCTypeUnion extends JCTree.JCExpression
/*      */     implements UnionTypeTree
/*      */   {
/*      */     public com.sun.tools.javac.util.List<JCTree.JCExpression> alternatives;
/*      */ 
/*      */     protected JCTypeUnion(com.sun.tools.javac.util.List<JCTree.JCExpression> paramList)
/*      */     {
/* 2160 */       this.alternatives = paramList;
/*      */     }
/*      */     public void accept(JCTree.Visitor paramVisitor) {
/* 2163 */       paramVisitor.visitTypeUnion(this);
/*      */     }
/* 2165 */     public Tree.Kind getKind() { return Tree.Kind.UNION_TYPE; }
/*      */ 
/*      */     public com.sun.tools.javac.util.List<JCTree.JCExpression> getTypeAlternatives() {
/* 2168 */       return this.alternatives;
/*      */     }
/*      */ 
/*      */     public <R, D> R accept(TreeVisitor<R, D> paramTreeVisitor, D paramD) {
/* 2172 */       return paramTreeVisitor.visitUnionType(this, paramD);
/*      */     }
/*      */ 
/*      */     public JCTree.Tag getTag() {
/* 2176 */       return JCTree.Tag.TYPEUNION;
/*      */     }
/*      */   }
/*      */ 
/*      */   public static class JCUnary extends JCTree.JCExpression
/*      */     implements UnaryTree
/*      */   {
/*      */     private JCTree.Tag opcode;
/*      */     public JCTree.JCExpression arg;
/*      */     public Symbol operator;
/*      */ 
/*      */     protected JCUnary(JCTree.Tag paramTag, JCTree.JCExpression paramJCExpression)
/*      */     {
/* 1742 */       this.opcode = paramTag;
/* 1743 */       this.arg = paramJCExpression;
/*      */     }
/*      */     public void accept(JCTree.Visitor paramVisitor) {
/* 1746 */       paramVisitor.visitUnary(this);
/*      */     }
/* 1748 */     public Tree.Kind getKind() { return TreeInfo.tagToKind(getTag()); } 
/* 1749 */     public JCTree.JCExpression getExpression() { return this.arg; } 
/*      */     public Symbol getOperator() {
/* 1751 */       return this.operator;
/*      */     }
/*      */ 
/*      */     public <R, D> R accept(TreeVisitor<R, D> paramTreeVisitor, D paramD) {
/* 1755 */       return paramTreeVisitor.visitUnary(this, paramD);
/*      */     }
/*      */ 
/*      */     public JCTree.Tag getTag() {
/* 1759 */       return this.opcode;
/*      */     }
/*      */ 
/*      */     public void setTag(JCTree.Tag paramTag) {
/* 1763 */       this.opcode = paramTag;
/*      */     }
/*      */   }
/*      */ 
/*      */   public static class JCVariableDecl extends JCTree.JCStatement
/*      */     implements VariableTree
/*      */   {
/*      */     public JCTree.JCModifiers mods;
/*      */     public Name name;
/*      */     public JCTree.JCExpression nameexpr;
/*      */     public JCTree.JCExpression vartype;
/*      */     public JCTree.JCExpression init;
/*      */     public Symbol.VarSymbol sym;
/*      */ 
/*      */     protected JCVariableDecl(JCTree.JCModifiers paramJCModifiers, Name paramName, JCTree.JCExpression paramJCExpression1, JCTree.JCExpression paramJCExpression2, Symbol.VarSymbol paramVarSymbol)
/*      */     {
/*  831 */       this.mods = paramJCModifiers;
/*  832 */       this.name = paramName;
/*  833 */       this.vartype = paramJCExpression1;
/*  834 */       this.init = paramJCExpression2;
/*  835 */       this.sym = paramVarSymbol;
/*      */     }
/*      */ 
/*      */     protected JCVariableDecl(JCTree.JCModifiers paramJCModifiers, JCTree.JCExpression paramJCExpression1, JCTree.JCExpression paramJCExpression2)
/*      */     {
/*  841 */       this(paramJCModifiers, null, paramJCExpression2, null, null);
/*  842 */       this.nameexpr = paramJCExpression1;
/*  843 */       if (paramJCExpression1.hasTag(JCTree.Tag.IDENT)) {
/*  844 */         this.name = ((JCTree.JCIdent)paramJCExpression1).name;
/*      */       }
/*      */       else
/*  847 */         this.name = ((JCTree.JCFieldAccess)paramJCExpression1).name;
/*      */     }
/*      */ 
/*      */     public void accept(JCTree.Visitor paramVisitor)
/*      */     {
/*  852 */       paramVisitor.visitVarDef(this);
/*      */     }
/*  854 */     public Tree.Kind getKind() { return Tree.Kind.VARIABLE; } 
/*  855 */     public JCTree.JCModifiers getModifiers() { return this.mods; } 
/*  856 */     public Name getName() { return this.name; } 
/*  857 */     public JCTree.JCExpression getNameExpression() { return this.nameexpr; } 
/*  858 */     public JCTree getType() { return this.vartype; } 
/*      */     public JCTree.JCExpression getInitializer() {
/*  860 */       return this.init;
/*      */     }
/*      */ 
/*      */     public <R, D> R accept(TreeVisitor<R, D> paramTreeVisitor, D paramD) {
/*  864 */       return paramTreeVisitor.visitVariable(this, paramD);
/*      */     }
/*      */ 
/*      */     public JCTree.Tag getTag()
/*      */     {
/*  869 */       return JCTree.Tag.VARDEF;
/*      */     }
/*      */   }
/*      */ 
/*      */   public static class JCWhileLoop extends JCTree.JCStatement
/*      */     implements WhileLoopTree
/*      */   {
/*      */     public JCTree.JCExpression cond;
/*      */     public JCTree.JCStatement body;
/*      */ 
/*      */     protected JCWhileLoop(JCTree.JCExpression paramJCExpression, JCTree.JCStatement paramJCStatement)
/*      */     {
/*  961 */       this.cond = paramJCExpression;
/*  962 */       this.body = paramJCStatement;
/*      */     }
/*      */     public void accept(JCTree.Visitor paramVisitor) {
/*  965 */       paramVisitor.visitWhileLoop(this);
/*      */     }
/*  967 */     public Tree.Kind getKind() { return Tree.Kind.WHILE_LOOP; } 
/*  968 */     public JCTree.JCExpression getCondition() { return this.cond; } 
/*  969 */     public JCTree.JCStatement getStatement() { return this.body; }
/*      */ 
/*      */     public <R, D> R accept(TreeVisitor<R, D> paramTreeVisitor, D paramD) {
/*  972 */       return paramTreeVisitor.visitWhileLoop(this, paramD);
/*      */     }
/*      */ 
/*      */     public JCTree.Tag getTag()
/*      */     {
/*  977 */       return JCTree.Tag.WHILELOOP;
/*      */     }
/*      */   }
/*      */ 
/*      */   public static class JCWildcard extends JCTree.JCExpression
/*      */     implements WildcardTree
/*      */   {
/*      */     public JCTree.TypeBoundKind kind;
/*      */     public JCTree inner;
/*      */ 
/*      */     protected JCWildcard(JCTree.TypeBoundKind paramTypeBoundKind, JCTree paramJCTree)
/*      */     {
/* 2248 */       paramTypeBoundKind.getClass();
/* 2249 */       this.kind = paramTypeBoundKind;
/* 2250 */       this.inner = paramJCTree;
/*      */     }
/*      */     public void accept(JCTree.Visitor paramVisitor) {
/* 2253 */       paramVisitor.visitWildcard(this);
/*      */     }
/*      */     public Tree.Kind getKind() {
/* 2256 */       switch (JCTree.1.$SwitchMap$com$sun$tools$javac$code$BoundKind[this.kind.kind.ordinal()]) {
/*      */       case 1:
/* 2258 */         return Tree.Kind.UNBOUNDED_WILDCARD;
/*      */       case 2:
/* 2260 */         return Tree.Kind.EXTENDS_WILDCARD;
/*      */       case 3:
/* 2262 */         return Tree.Kind.SUPER_WILDCARD;
/*      */       }
/* 2264 */       throw new AssertionError("Unknown wildcard bound " + this.kind);
/*      */     }
/*      */     public JCTree getBound() {
/* 2267 */       return this.inner;
/*      */     }
/*      */     public <R, D> R accept(TreeVisitor<R, D> paramTreeVisitor, D paramD) {
/* 2270 */       return paramTreeVisitor.visitWildcard(this, paramD);
/*      */     }
/*      */ 
/*      */     public JCTree.Tag getTag() {
/* 2274 */       return JCTree.Tag.WILDCARD;
/*      */     }
/*      */   }
/*      */ 
/*      */   public static class LetExpr extends JCTree.JCExpression
/*      */   {
/*      */     public com.sun.tools.javac.util.List<JCTree.JCVariableDecl> defs;
/*      */     public JCTree expr;
/*      */ 
/*      */     protected LetExpr(com.sun.tools.javac.util.List<JCTree.JCVariableDecl> paramList, JCTree paramJCTree)
/*      */     {
/* 2422 */       this.defs = paramList;
/* 2423 */       this.expr = paramJCTree;
/*      */     }
/*      */     public void accept(JCTree.Visitor paramVisitor) {
/* 2426 */       paramVisitor.visitLetExpr(this);
/*      */     }
/*      */     public Tree.Kind getKind() {
/* 2429 */       throw new AssertionError("LetExpr is not part of a public API");
/*      */     }
/*      */ 
/*      */     public <R, D> R accept(TreeVisitor<R, D> paramTreeVisitor, D paramD) {
/* 2433 */       throw new AssertionError("LetExpr is not part of a public API");
/*      */     }
/*      */ 
/*      */     public JCTree.Tag getTag() {
/* 2437 */       return JCTree.Tag.LETEXPR;
/*      */     }
/*      */   }
/*      */ 
/*      */   public static enum Tag
/*      */   {
/*   86 */     NO_TAG, 
/*      */ 
/*   90 */     TOPLEVEL, 
/*      */ 
/*   94 */     IMPORT, 
/*      */ 
/*   98 */     CLASSDEF, 
/*      */ 
/*  102 */     METHODDEF, 
/*      */ 
/*  106 */     VARDEF, 
/*      */ 
/*  110 */     SKIP, 
/*      */ 
/*  114 */     BLOCK, 
/*      */ 
/*  118 */     DOLOOP, 
/*      */ 
/*  122 */     WHILELOOP, 
/*      */ 
/*  126 */     FORLOOP, 
/*      */ 
/*  130 */     FOREACHLOOP, 
/*      */ 
/*  134 */     LABELLED, 
/*      */ 
/*  138 */     SWITCH, 
/*      */ 
/*  142 */     CASE, 
/*      */ 
/*  146 */     SYNCHRONIZED, 
/*      */ 
/*  150 */     TRY, 
/*      */ 
/*  154 */     CATCH, 
/*      */ 
/*  158 */     CONDEXPR, 
/*      */ 
/*  162 */     IF, 
/*      */ 
/*  166 */     EXEC, 
/*      */ 
/*  170 */     BREAK, 
/*      */ 
/*  174 */     CONTINUE, 
/*      */ 
/*  178 */     RETURN, 
/*      */ 
/*  182 */     THROW, 
/*      */ 
/*  186 */     ASSERT, 
/*      */ 
/*  190 */     APPLY, 
/*      */ 
/*  194 */     NEWCLASS, 
/*      */ 
/*  198 */     NEWARRAY, 
/*      */ 
/*  202 */     LAMBDA, 
/*      */ 
/*  206 */     PARENS, 
/*      */ 
/*  210 */     ASSIGN, 
/*      */ 
/*  214 */     TYPECAST, 
/*      */ 
/*  218 */     TYPETEST, 
/*      */ 
/*  222 */     INDEXED, 
/*      */ 
/*  226 */     SELECT, 
/*      */ 
/*  230 */     REFERENCE, 
/*      */ 
/*  234 */     IDENT, 
/*      */ 
/*  238 */     LITERAL, 
/*      */ 
/*  242 */     TYPEIDENT, 
/*      */ 
/*  246 */     TYPEARRAY, 
/*      */ 
/*  250 */     TYPEAPPLY, 
/*      */ 
/*  254 */     TYPEUNION, 
/*      */ 
/*  258 */     TYPEINTERSECTION, 
/*      */ 
/*  262 */     TYPEPARAMETER, 
/*      */ 
/*  266 */     WILDCARD, 
/*      */ 
/*  270 */     TYPEBOUNDKIND, 
/*      */ 
/*  274 */     ANNOTATION, 
/*      */ 
/*  278 */     TYPE_ANNOTATION, 
/*      */ 
/*  282 */     MODIFIERS, 
/*      */ 
/*  286 */     ANNOTATED_TYPE, 
/*      */ 
/*  290 */     ERRONEOUS, 
/*      */ 
/*  294 */     POS, 
/*  295 */     NEG, 
/*  296 */     NOT, 
/*  297 */     COMPL, 
/*  298 */     PREINC, 
/*  299 */     PREDEC, 
/*  300 */     POSTINC, 
/*  301 */     POSTDEC, 
/*      */ 
/*  305 */     NULLCHK, 
/*      */ 
/*  309 */     OR, 
/*  310 */     AND, 
/*  311 */     BITOR, 
/*  312 */     BITXOR, 
/*  313 */     BITAND, 
/*  314 */     EQ, 
/*  315 */     NE, 
/*  316 */     LT, 
/*  317 */     GT, 
/*  318 */     LE, 
/*  319 */     GE, 
/*  320 */     SL, 
/*  321 */     SR, 
/*  322 */     USR, 
/*  323 */     PLUS, 
/*  324 */     MINUS, 
/*  325 */     MUL, 
/*  326 */     DIV, 
/*  327 */     MOD, 
/*      */ 
/*  331 */     BITOR_ASG(BITOR), 
/*  332 */     BITXOR_ASG(BITXOR), 
/*  333 */     BITAND_ASG(BITAND), 
/*      */ 
/*  335 */     SL_ASG(SL), 
/*  336 */     SR_ASG(SR), 
/*  337 */     USR_ASG(USR), 
/*  338 */     PLUS_ASG(PLUS), 
/*  339 */     MINUS_ASG(MINUS), 
/*  340 */     MUL_ASG(MUL), 
/*  341 */     DIV_ASG(DIV), 
/*  342 */     MOD_ASG(MOD), 
/*      */ 
/*  346 */     LETEXPR;
/*      */ 
/*      */     private final Tag noAssignTag;
/*  350 */     private static final int numberOfOperators = MOD.ordinal() - POS.ordinal() + 1;
/*      */ 
/*      */     private Tag(Tag paramTag) {
/*  353 */       this.noAssignTag = paramTag;
/*      */     }
/*      */ 
/*      */     private Tag() {
/*  357 */       this(null);
/*      */     }
/*      */ 
/*      */     public static int getNumberOfOperators() {
/*  361 */       return numberOfOperators;
/*      */     }
/*      */ 
/*      */     public Tag noAssignOp() {
/*  365 */       if (this.noAssignTag != null)
/*  366 */         return this.noAssignTag;
/*  367 */       throw new AssertionError("noAssignOp() method is not available for non assignment tags");
/*      */     }
/*      */ 
/*      */     public boolean isPostUnaryOp() {
/*  371 */       return (this == POSTINC) || (this == POSTDEC);
/*      */     }
/*      */ 
/*      */     public boolean isIncOrDecUnaryOp() {
/*  375 */       return (this == PREINC) || (this == PREDEC) || (this == POSTINC) || (this == POSTDEC);
/*      */     }
/*      */ 
/*      */     public boolean isAssignop() {
/*  379 */       return this.noAssignTag != null;
/*      */     }
/*      */ 
/*      */     public int operatorIndex() {
/*  383 */       return ordinal() - POS.ordinal();
/*      */     }
/*      */   }
/*      */ 
/*      */   public static class TypeBoundKind extends JCTree
/*      */   {
/*      */     public BoundKind kind;
/*      */ 
/*      */     protected TypeBoundKind(BoundKind paramBoundKind)
/*      */     {
/* 2281 */       this.kind = paramBoundKind;
/*      */     }
/*      */     public void accept(JCTree.Visitor paramVisitor) {
/* 2284 */       paramVisitor.visitTypeBoundKind(this);
/*      */     }
/*      */     public Tree.Kind getKind() {
/* 2287 */       throw new AssertionError("TypeBoundKind is not part of a public API");
/*      */     }
/*      */ 
/*      */     public <R, D> R accept(TreeVisitor<R, D> paramTreeVisitor, D paramD) {
/* 2291 */       throw new AssertionError("TypeBoundKind is not part of a public API");
/*      */     }
/*      */ 
/*      */     public JCTree.Tag getTag() {
/* 2295 */       return JCTree.Tag.TYPEBOUNDKIND;
/*      */     }
/*      */   }
/*      */ 
/*      */   public static abstract class Visitor
/*      */   {
/*      */     public void visitTopLevel(JCTree.JCCompilationUnit paramJCCompilationUnit)
/*      */     {
/* 2533 */       visitTree(paramJCCompilationUnit); } 
/* 2534 */     public void visitImport(JCTree.JCImport paramJCImport) { visitTree(paramJCImport); } 
/* 2535 */     public void visitClassDef(JCTree.JCClassDecl paramJCClassDecl) { visitTree(paramJCClassDecl); } 
/* 2536 */     public void visitMethodDef(JCTree.JCMethodDecl paramJCMethodDecl) { visitTree(paramJCMethodDecl); } 
/* 2537 */     public void visitVarDef(JCTree.JCVariableDecl paramJCVariableDecl) { visitTree(paramJCVariableDecl); } 
/* 2538 */     public void visitSkip(JCTree.JCSkip paramJCSkip) { visitTree(paramJCSkip); } 
/* 2539 */     public void visitBlock(JCTree.JCBlock paramJCBlock) { visitTree(paramJCBlock); } 
/* 2540 */     public void visitDoLoop(JCTree.JCDoWhileLoop paramJCDoWhileLoop) { visitTree(paramJCDoWhileLoop); } 
/* 2541 */     public void visitWhileLoop(JCTree.JCWhileLoop paramJCWhileLoop) { visitTree(paramJCWhileLoop); } 
/* 2542 */     public void visitForLoop(JCTree.JCForLoop paramJCForLoop) { visitTree(paramJCForLoop); } 
/* 2543 */     public void visitForeachLoop(JCTree.JCEnhancedForLoop paramJCEnhancedForLoop) { visitTree(paramJCEnhancedForLoop); } 
/* 2544 */     public void visitLabelled(JCTree.JCLabeledStatement paramJCLabeledStatement) { visitTree(paramJCLabeledStatement); } 
/* 2545 */     public void visitSwitch(JCTree.JCSwitch paramJCSwitch) { visitTree(paramJCSwitch); } 
/* 2546 */     public void visitCase(JCTree.JCCase paramJCCase) { visitTree(paramJCCase); } 
/* 2547 */     public void visitSynchronized(JCTree.JCSynchronized paramJCSynchronized) { visitTree(paramJCSynchronized); } 
/* 2548 */     public void visitTry(JCTree.JCTry paramJCTry) { visitTree(paramJCTry); } 
/* 2549 */     public void visitCatch(JCTree.JCCatch paramJCCatch) { visitTree(paramJCCatch); } 
/* 2550 */     public void visitConditional(JCTree.JCConditional paramJCConditional) { visitTree(paramJCConditional); } 
/* 2551 */     public void visitIf(JCTree.JCIf paramJCIf) { visitTree(paramJCIf); } 
/* 2552 */     public void visitExec(JCTree.JCExpressionStatement paramJCExpressionStatement) { visitTree(paramJCExpressionStatement); } 
/* 2553 */     public void visitBreak(JCTree.JCBreak paramJCBreak) { visitTree(paramJCBreak); } 
/* 2554 */     public void visitContinue(JCTree.JCContinue paramJCContinue) { visitTree(paramJCContinue); } 
/* 2555 */     public void visitReturn(JCTree.JCReturn paramJCReturn) { visitTree(paramJCReturn); } 
/* 2556 */     public void visitThrow(JCTree.JCThrow paramJCThrow) { visitTree(paramJCThrow); } 
/* 2557 */     public void visitAssert(JCTree.JCAssert paramJCAssert) { visitTree(paramJCAssert); } 
/* 2558 */     public void visitApply(JCTree.JCMethodInvocation paramJCMethodInvocation) { visitTree(paramJCMethodInvocation); } 
/* 2559 */     public void visitNewClass(JCTree.JCNewClass paramJCNewClass) { visitTree(paramJCNewClass); } 
/* 2560 */     public void visitNewArray(JCTree.JCNewArray paramJCNewArray) { visitTree(paramJCNewArray); } 
/* 2561 */     public void visitLambda(JCTree.JCLambda paramJCLambda) { visitTree(paramJCLambda); } 
/* 2562 */     public void visitParens(JCTree.JCParens paramJCParens) { visitTree(paramJCParens); } 
/* 2563 */     public void visitAssign(JCTree.JCAssign paramJCAssign) { visitTree(paramJCAssign); } 
/* 2564 */     public void visitAssignop(JCTree.JCAssignOp paramJCAssignOp) { visitTree(paramJCAssignOp); } 
/* 2565 */     public void visitUnary(JCTree.JCUnary paramJCUnary) { visitTree(paramJCUnary); } 
/* 2566 */     public void visitBinary(JCTree.JCBinary paramJCBinary) { visitTree(paramJCBinary); } 
/* 2567 */     public void visitTypeCast(JCTree.JCTypeCast paramJCTypeCast) { visitTree(paramJCTypeCast); } 
/* 2568 */     public void visitTypeTest(JCTree.JCInstanceOf paramJCInstanceOf) { visitTree(paramJCInstanceOf); } 
/* 2569 */     public void visitIndexed(JCTree.JCArrayAccess paramJCArrayAccess) { visitTree(paramJCArrayAccess); } 
/* 2570 */     public void visitSelect(JCTree.JCFieldAccess paramJCFieldAccess) { visitTree(paramJCFieldAccess); } 
/* 2571 */     public void visitReference(JCTree.JCMemberReference paramJCMemberReference) { visitTree(paramJCMemberReference); } 
/* 2572 */     public void visitIdent(JCTree.JCIdent paramJCIdent) { visitTree(paramJCIdent); } 
/* 2573 */     public void visitLiteral(JCTree.JCLiteral paramJCLiteral) { visitTree(paramJCLiteral); } 
/* 2574 */     public void visitTypeIdent(JCTree.JCPrimitiveTypeTree paramJCPrimitiveTypeTree) { visitTree(paramJCPrimitiveTypeTree); } 
/* 2575 */     public void visitTypeArray(JCTree.JCArrayTypeTree paramJCArrayTypeTree) { visitTree(paramJCArrayTypeTree); } 
/* 2576 */     public void visitTypeApply(JCTree.JCTypeApply paramJCTypeApply) { visitTree(paramJCTypeApply); } 
/* 2577 */     public void visitTypeUnion(JCTree.JCTypeUnion paramJCTypeUnion) { visitTree(paramJCTypeUnion); } 
/* 2578 */     public void visitTypeIntersection(JCTree.JCTypeIntersection paramJCTypeIntersection) { visitTree(paramJCTypeIntersection); } 
/* 2579 */     public void visitTypeParameter(JCTree.JCTypeParameter paramJCTypeParameter) { visitTree(paramJCTypeParameter); } 
/* 2580 */     public void visitWildcard(JCTree.JCWildcard paramJCWildcard) { visitTree(paramJCWildcard); } 
/* 2581 */     public void visitTypeBoundKind(JCTree.TypeBoundKind paramTypeBoundKind) { visitTree(paramTypeBoundKind); } 
/* 2582 */     public void visitAnnotation(JCTree.JCAnnotation paramJCAnnotation) { visitTree(paramJCAnnotation); } 
/* 2583 */     public void visitModifiers(JCTree.JCModifiers paramJCModifiers) { visitTree(paramJCModifiers); } 
/* 2584 */     public void visitAnnotatedType(JCTree.JCAnnotatedType paramJCAnnotatedType) { visitTree(paramJCAnnotatedType); } 
/* 2585 */     public void visitErroneous(JCTree.JCErroneous paramJCErroneous) { visitTree(paramJCErroneous); } 
/* 2586 */     public void visitLetExpr(JCTree.LetExpr paramLetExpr) { visitTree(paramLetExpr); } 
/*      */     public void visitTree(JCTree paramJCTree) {
/* 2588 */       Assert.error();
/*      */     }
/*      */   }
/*      */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.javac.tree.JCTree
 * JD-Core Version:    0.6.2
 */