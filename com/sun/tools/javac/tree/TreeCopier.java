/*     */ package com.sun.tools.javac.tree;
/*     */ 
/*     */ import com.sun.source.tree.AnnotatedTypeTree;
/*     */ import com.sun.source.tree.AnnotationTree;
/*     */ import com.sun.source.tree.ArrayAccessTree;
/*     */ import com.sun.source.tree.ArrayTypeTree;
/*     */ import com.sun.source.tree.AssertTree;
/*     */ import com.sun.source.tree.AssignmentTree;
/*     */ import com.sun.source.tree.BinaryTree;
/*     */ import com.sun.source.tree.BlockTree;
/*     */ import com.sun.source.tree.BreakTree;
/*     */ import com.sun.source.tree.CaseTree;
/*     */ import com.sun.source.tree.CatchTree;
/*     */ import com.sun.source.tree.ClassTree;
/*     */ import com.sun.source.tree.CompilationUnitTree;
/*     */ import com.sun.source.tree.CompoundAssignmentTree;
/*     */ import com.sun.source.tree.ConditionalExpressionTree;
/*     */ import com.sun.source.tree.ContinueTree;
/*     */ import com.sun.source.tree.DoWhileLoopTree;
/*     */ import com.sun.source.tree.EmptyStatementTree;
/*     */ import com.sun.source.tree.EnhancedForLoopTree;
/*     */ import com.sun.source.tree.ErroneousTree;
/*     */ import com.sun.source.tree.ExpressionStatementTree;
/*     */ import com.sun.source.tree.ForLoopTree;
/*     */ import com.sun.source.tree.IdentifierTree;
/*     */ import com.sun.source.tree.IfTree;
/*     */ import com.sun.source.tree.ImportTree;
/*     */ import com.sun.source.tree.InstanceOfTree;
/*     */ import com.sun.source.tree.IntersectionTypeTree;
/*     */ import com.sun.source.tree.LabeledStatementTree;
/*     */ import com.sun.source.tree.LambdaExpressionTree;
/*     */ import com.sun.source.tree.LiteralTree;
/*     */ import com.sun.source.tree.MemberReferenceTree;
/*     */ import com.sun.source.tree.MemberSelectTree;
/*     */ import com.sun.source.tree.MethodInvocationTree;
/*     */ import com.sun.source.tree.MethodTree;
/*     */ import com.sun.source.tree.ModifiersTree;
/*     */ import com.sun.source.tree.NewArrayTree;
/*     */ import com.sun.source.tree.NewClassTree;
/*     */ import com.sun.source.tree.ParameterizedTypeTree;
/*     */ import com.sun.source.tree.ParenthesizedTree;
/*     */ import com.sun.source.tree.PrimitiveTypeTree;
/*     */ import com.sun.source.tree.ReturnTree;
/*     */ import com.sun.source.tree.SwitchTree;
/*     */ import com.sun.source.tree.SynchronizedTree;
/*     */ import com.sun.source.tree.ThrowTree;
/*     */ import com.sun.source.tree.Tree;
/*     */ import com.sun.source.tree.Tree.Kind;
/*     */ import com.sun.source.tree.TreeVisitor;
/*     */ import com.sun.source.tree.TryTree;
/*     */ import com.sun.source.tree.TypeCastTree;
/*     */ import com.sun.source.tree.TypeParameterTree;
/*     */ import com.sun.source.tree.UnaryTree;
/*     */ import com.sun.source.tree.UnionTypeTree;
/*     */ import com.sun.source.tree.VariableTree;
/*     */ import com.sun.source.tree.WhileLoopTree;
/*     */ import com.sun.source.tree.WildcardTree;
/*     */ import com.sun.tools.javac.util.List;
/*     */ import com.sun.tools.javac.util.ListBuffer;
/*     */ 
/*     */ public class TreeCopier<P>
/*     */   implements TreeVisitor<JCTree, P>
/*     */ {
/*     */   private TreeMaker M;
/*     */ 
/*     */   public TreeCopier(TreeMaker paramTreeMaker)
/*     */   {
/*  47 */     this.M = paramTreeMaker;
/*     */   }
/*     */ 
/*     */   public <T extends JCTree> T copy(T paramT) {
/*  51 */     return copy(paramT, null);
/*     */   }
/*     */ 
/*     */   public <T extends JCTree> T copy(T paramT, P paramP)
/*     */   {
/*  56 */     if (paramT == null)
/*  57 */       return null;
/*  58 */     return (JCTree)paramT.accept(this, paramP);
/*     */   }
/*     */ 
/*     */   public <T extends JCTree> List<T> copy(List<T> paramList) {
/*  62 */     return copy(paramList, null);
/*     */   }
/*     */ 
/*     */   public <T extends JCTree> List<T> copy(List<T> paramList, P paramP) {
/*  66 */     if (paramList == null)
/*  67 */       return null;
/*  68 */     ListBuffer localListBuffer = new ListBuffer();
/*  69 */     for (JCTree localJCTree : paramList)
/*  70 */       localListBuffer.append(copy(localJCTree, paramP));
/*  71 */     return localListBuffer.toList();
/*     */   }
/*     */ 
/*     */   public JCTree visitAnnotatedType(AnnotatedTypeTree paramAnnotatedTypeTree, P paramP) {
/*  75 */     JCTree.JCAnnotatedType localJCAnnotatedType = (JCTree.JCAnnotatedType)paramAnnotatedTypeTree;
/*  76 */     List localList = copy(localJCAnnotatedType.annotations, paramP);
/*  77 */     JCTree.JCExpression localJCExpression = (JCTree.JCExpression)copy(localJCAnnotatedType.underlyingType, paramP);
/*  78 */     return this.M.at(localJCAnnotatedType.pos).AnnotatedType(localList, localJCExpression);
/*     */   }
/*     */ 
/*     */   public JCTree visitAnnotation(AnnotationTree paramAnnotationTree, P paramP) {
/*  82 */     JCTree.JCAnnotation localJCAnnotation1 = (JCTree.JCAnnotation)paramAnnotationTree;
/*  83 */     JCTree localJCTree = copy(localJCAnnotation1.annotationType, paramP);
/*  84 */     List localList = copy(localJCAnnotation1.args, paramP);
/*  85 */     if (localJCAnnotation1.getKind() == Tree.Kind.TYPE_ANNOTATION) {
/*  86 */       localJCAnnotation2 = this.M.at(localJCAnnotation1.pos).TypeAnnotation(localJCTree, localList);
/*  87 */       localJCAnnotation2.attribute = localJCAnnotation1.attribute;
/*  88 */       return localJCAnnotation2;
/*     */     }
/*  90 */     JCTree.JCAnnotation localJCAnnotation2 = this.M.at(localJCAnnotation1.pos).Annotation(localJCTree, localList);
/*  91 */     localJCAnnotation2.attribute = localJCAnnotation1.attribute;
/*  92 */     return localJCAnnotation2;
/*     */   }
/*     */ 
/*     */   public JCTree visitAssert(AssertTree paramAssertTree, P paramP)
/*     */   {
/*  97 */     JCTree.JCAssert localJCAssert = (JCTree.JCAssert)paramAssertTree;
/*  98 */     JCTree.JCExpression localJCExpression1 = (JCTree.JCExpression)copy(localJCAssert.cond, paramP);
/*  99 */     JCTree.JCExpression localJCExpression2 = (JCTree.JCExpression)copy(localJCAssert.detail, paramP);
/* 100 */     return this.M.at(localJCAssert.pos).Assert(localJCExpression1, localJCExpression2);
/*     */   }
/*     */ 
/*     */   public JCTree visitAssignment(AssignmentTree paramAssignmentTree, P paramP) {
/* 104 */     JCTree.JCAssign localJCAssign = (JCTree.JCAssign)paramAssignmentTree;
/* 105 */     JCTree.JCExpression localJCExpression1 = (JCTree.JCExpression)copy(localJCAssign.lhs, paramP);
/* 106 */     JCTree.JCExpression localJCExpression2 = (JCTree.JCExpression)copy(localJCAssign.rhs, paramP);
/* 107 */     return this.M.at(localJCAssign.pos).Assign(localJCExpression1, localJCExpression2);
/*     */   }
/*     */ 
/*     */   public JCTree visitCompoundAssignment(CompoundAssignmentTree paramCompoundAssignmentTree, P paramP) {
/* 111 */     JCTree.JCAssignOp localJCAssignOp = (JCTree.JCAssignOp)paramCompoundAssignmentTree;
/* 112 */     JCTree localJCTree1 = copy(localJCAssignOp.lhs, paramP);
/* 113 */     JCTree localJCTree2 = copy(localJCAssignOp.rhs, paramP);
/* 114 */     return this.M.at(localJCAssignOp.pos).Assignop(localJCAssignOp.getTag(), localJCTree1, localJCTree2);
/*     */   }
/*     */ 
/*     */   public JCTree visitBinary(BinaryTree paramBinaryTree, P paramP) {
/* 118 */     JCTree.JCBinary localJCBinary = (JCTree.JCBinary)paramBinaryTree;
/* 119 */     JCTree.JCExpression localJCExpression1 = (JCTree.JCExpression)copy(localJCBinary.lhs, paramP);
/* 120 */     JCTree.JCExpression localJCExpression2 = (JCTree.JCExpression)copy(localJCBinary.rhs, paramP);
/* 121 */     return this.M.at(localJCBinary.pos).Binary(localJCBinary.getTag(), localJCExpression1, localJCExpression2);
/*     */   }
/*     */ 
/*     */   public JCTree visitBlock(BlockTree paramBlockTree, P paramP) {
/* 125 */     JCTree.JCBlock localJCBlock = (JCTree.JCBlock)paramBlockTree;
/* 126 */     List localList = copy(localJCBlock.stats, paramP);
/* 127 */     return this.M.at(localJCBlock.pos).Block(localJCBlock.flags, localList);
/*     */   }
/*     */ 
/*     */   public JCTree visitBreak(BreakTree paramBreakTree, P paramP) {
/* 131 */     JCTree.JCBreak localJCBreak = (JCTree.JCBreak)paramBreakTree;
/* 132 */     return this.M.at(localJCBreak.pos).Break(localJCBreak.label);
/*     */   }
/*     */ 
/*     */   public JCTree visitCase(CaseTree paramCaseTree, P paramP) {
/* 136 */     JCTree.JCCase localJCCase = (JCTree.JCCase)paramCaseTree;
/* 137 */     JCTree.JCExpression localJCExpression = (JCTree.JCExpression)copy(localJCCase.pat, paramP);
/* 138 */     List localList = copy(localJCCase.stats, paramP);
/* 139 */     return this.M.at(localJCCase.pos).Case(localJCExpression, localList);
/*     */   }
/*     */ 
/*     */   public JCTree visitCatch(CatchTree paramCatchTree, P paramP) {
/* 143 */     JCTree.JCCatch localJCCatch = (JCTree.JCCatch)paramCatchTree;
/* 144 */     JCTree.JCVariableDecl localJCVariableDecl = (JCTree.JCVariableDecl)copy(localJCCatch.param, paramP);
/* 145 */     JCTree.JCBlock localJCBlock = (JCTree.JCBlock)copy(localJCCatch.body, paramP);
/* 146 */     return this.M.at(localJCCatch.pos).Catch(localJCVariableDecl, localJCBlock);
/*     */   }
/*     */ 
/*     */   public JCTree visitClass(ClassTree paramClassTree, P paramP) {
/* 150 */     JCTree.JCClassDecl localJCClassDecl = (JCTree.JCClassDecl)paramClassTree;
/* 151 */     JCTree.JCModifiers localJCModifiers = (JCTree.JCModifiers)copy(localJCClassDecl.mods, paramP);
/* 152 */     List localList1 = copy(localJCClassDecl.typarams, paramP);
/* 153 */     JCTree.JCExpression localJCExpression = (JCTree.JCExpression)copy(localJCClassDecl.extending, paramP);
/* 154 */     List localList2 = copy(localJCClassDecl.implementing, paramP);
/* 155 */     List localList3 = copy(localJCClassDecl.defs, paramP);
/* 156 */     return this.M.at(localJCClassDecl.pos).ClassDef(localJCModifiers, localJCClassDecl.name, localList1, localJCExpression, localList2, localList3);
/*     */   }
/*     */ 
/*     */   public JCTree visitConditionalExpression(ConditionalExpressionTree paramConditionalExpressionTree, P paramP) {
/* 160 */     JCTree.JCConditional localJCConditional = (JCTree.JCConditional)paramConditionalExpressionTree;
/* 161 */     JCTree.JCExpression localJCExpression1 = (JCTree.JCExpression)copy(localJCConditional.cond, paramP);
/* 162 */     JCTree.JCExpression localJCExpression2 = (JCTree.JCExpression)copy(localJCConditional.truepart, paramP);
/* 163 */     JCTree.JCExpression localJCExpression3 = (JCTree.JCExpression)copy(localJCConditional.falsepart, paramP);
/* 164 */     return this.M.at(localJCConditional.pos).Conditional(localJCExpression1, localJCExpression2, localJCExpression3);
/*     */   }
/*     */ 
/*     */   public JCTree visitContinue(ContinueTree paramContinueTree, P paramP) {
/* 168 */     JCTree.JCContinue localJCContinue = (JCTree.JCContinue)paramContinueTree;
/* 169 */     return this.M.at(localJCContinue.pos).Continue(localJCContinue.label);
/*     */   }
/*     */ 
/*     */   public JCTree visitDoWhileLoop(DoWhileLoopTree paramDoWhileLoopTree, P paramP) {
/* 173 */     JCTree.JCDoWhileLoop localJCDoWhileLoop = (JCTree.JCDoWhileLoop)paramDoWhileLoopTree;
/* 174 */     JCTree.JCStatement localJCStatement = (JCTree.JCStatement)copy(localJCDoWhileLoop.body, paramP);
/* 175 */     JCTree.JCExpression localJCExpression = (JCTree.JCExpression)copy(localJCDoWhileLoop.cond, paramP);
/* 176 */     return this.M.at(localJCDoWhileLoop.pos).DoLoop(localJCStatement, localJCExpression);
/*     */   }
/*     */ 
/*     */   public JCTree visitErroneous(ErroneousTree paramErroneousTree, P paramP) {
/* 180 */     JCTree.JCErroneous localJCErroneous = (JCTree.JCErroneous)paramErroneousTree;
/* 181 */     List localList = copy(localJCErroneous.errs, paramP);
/* 182 */     return this.M.at(localJCErroneous.pos).Erroneous(localList);
/*     */   }
/*     */ 
/*     */   public JCTree visitExpressionStatement(ExpressionStatementTree paramExpressionStatementTree, P paramP) {
/* 186 */     JCTree.JCExpressionStatement localJCExpressionStatement = (JCTree.JCExpressionStatement)paramExpressionStatementTree;
/* 187 */     JCTree.JCExpression localJCExpression = (JCTree.JCExpression)copy(localJCExpressionStatement.expr, paramP);
/* 188 */     return this.M.at(localJCExpressionStatement.pos).Exec(localJCExpression);
/*     */   }
/*     */ 
/*     */   public JCTree visitEnhancedForLoop(EnhancedForLoopTree paramEnhancedForLoopTree, P paramP) {
/* 192 */     JCTree.JCEnhancedForLoop localJCEnhancedForLoop = (JCTree.JCEnhancedForLoop)paramEnhancedForLoopTree;
/* 193 */     JCTree.JCVariableDecl localJCVariableDecl = (JCTree.JCVariableDecl)copy(localJCEnhancedForLoop.var, paramP);
/* 194 */     JCTree.JCExpression localJCExpression = (JCTree.JCExpression)copy(localJCEnhancedForLoop.expr, paramP);
/* 195 */     JCTree.JCStatement localJCStatement = (JCTree.JCStatement)copy(localJCEnhancedForLoop.body, paramP);
/* 196 */     return this.M.at(localJCEnhancedForLoop.pos).ForeachLoop(localJCVariableDecl, localJCExpression, localJCStatement);
/*     */   }
/*     */ 
/*     */   public JCTree visitForLoop(ForLoopTree paramForLoopTree, P paramP) {
/* 200 */     JCTree.JCForLoop localJCForLoop = (JCTree.JCForLoop)paramForLoopTree;
/* 201 */     List localList1 = copy(localJCForLoop.init, paramP);
/* 202 */     JCTree.JCExpression localJCExpression = (JCTree.JCExpression)copy(localJCForLoop.cond, paramP);
/* 203 */     List localList2 = copy(localJCForLoop.step, paramP);
/* 204 */     JCTree.JCStatement localJCStatement = (JCTree.JCStatement)copy(localJCForLoop.body, paramP);
/* 205 */     return this.M.at(localJCForLoop.pos).ForLoop(localList1, localJCExpression, localList2, localJCStatement);
/*     */   }
/*     */ 
/*     */   public JCTree visitIdentifier(IdentifierTree paramIdentifierTree, P paramP) {
/* 209 */     JCTree.JCIdent localJCIdent = (JCTree.JCIdent)paramIdentifierTree;
/* 210 */     return this.M.at(localJCIdent.pos).Ident(localJCIdent.name);
/*     */   }
/*     */ 
/*     */   public JCTree visitIf(IfTree paramIfTree, P paramP) {
/* 214 */     JCTree.JCIf localJCIf = (JCTree.JCIf)paramIfTree;
/* 215 */     JCTree.JCExpression localJCExpression = (JCTree.JCExpression)copy(localJCIf.cond, paramP);
/* 216 */     JCTree.JCStatement localJCStatement1 = (JCTree.JCStatement)copy(localJCIf.thenpart, paramP);
/* 217 */     JCTree.JCStatement localJCStatement2 = (JCTree.JCStatement)copy(localJCIf.elsepart, paramP);
/* 218 */     return this.M.at(localJCIf.pos).If(localJCExpression, localJCStatement1, localJCStatement2);
/*     */   }
/*     */ 
/*     */   public JCTree visitImport(ImportTree paramImportTree, P paramP) {
/* 222 */     JCTree.JCImport localJCImport = (JCTree.JCImport)paramImportTree;
/* 223 */     JCTree localJCTree = copy(localJCImport.qualid, paramP);
/* 224 */     return this.M.at(localJCImport.pos).Import(localJCTree, localJCImport.staticImport);
/*     */   }
/*     */ 
/*     */   public JCTree visitArrayAccess(ArrayAccessTree paramArrayAccessTree, P paramP) {
/* 228 */     JCTree.JCArrayAccess localJCArrayAccess = (JCTree.JCArrayAccess)paramArrayAccessTree;
/* 229 */     JCTree.JCExpression localJCExpression1 = (JCTree.JCExpression)copy(localJCArrayAccess.indexed, paramP);
/* 230 */     JCTree.JCExpression localJCExpression2 = (JCTree.JCExpression)copy(localJCArrayAccess.index, paramP);
/* 231 */     return this.M.at(localJCArrayAccess.pos).Indexed(localJCExpression1, localJCExpression2);
/*     */   }
/*     */ 
/*     */   public JCTree visitLabeledStatement(LabeledStatementTree paramLabeledStatementTree, P paramP) {
/* 235 */     JCTree.JCLabeledStatement localJCLabeledStatement = (JCTree.JCLabeledStatement)paramLabeledStatementTree;
/* 236 */     JCTree.JCStatement localJCStatement = (JCTree.JCStatement)copy(localJCLabeledStatement.body, paramP);
/* 237 */     return this.M.at(localJCLabeledStatement.pos).Labelled(localJCLabeledStatement.label, localJCStatement);
/*     */   }
/*     */ 
/*     */   public JCTree visitLiteral(LiteralTree paramLiteralTree, P paramP) {
/* 241 */     JCTree.JCLiteral localJCLiteral = (JCTree.JCLiteral)paramLiteralTree;
/* 242 */     return this.M.at(localJCLiteral.pos).Literal(localJCLiteral.typetag, localJCLiteral.value);
/*     */   }
/*     */ 
/*     */   public JCTree visitMethod(MethodTree paramMethodTree, P paramP) {
/* 246 */     JCTree.JCMethodDecl localJCMethodDecl = (JCTree.JCMethodDecl)paramMethodTree;
/* 247 */     JCTree.JCModifiers localJCModifiers = (JCTree.JCModifiers)copy(localJCMethodDecl.mods, paramP);
/* 248 */     JCTree.JCExpression localJCExpression1 = (JCTree.JCExpression)copy(localJCMethodDecl.restype, paramP);
/* 249 */     List localList1 = copy(localJCMethodDecl.typarams, paramP);
/* 250 */     List localList2 = copy(localJCMethodDecl.params, paramP);
/* 251 */     JCTree.JCVariableDecl localJCVariableDecl = (JCTree.JCVariableDecl)copy(localJCMethodDecl.recvparam, paramP);
/* 252 */     List localList3 = copy(localJCMethodDecl.thrown, paramP);
/* 253 */     JCTree.JCBlock localJCBlock = (JCTree.JCBlock)copy(localJCMethodDecl.body, paramP);
/* 254 */     JCTree.JCExpression localJCExpression2 = (JCTree.JCExpression)copy(localJCMethodDecl.defaultValue, paramP);
/* 255 */     return this.M.at(localJCMethodDecl.pos).MethodDef(localJCModifiers, localJCMethodDecl.name, localJCExpression1, localList1, localJCVariableDecl, localList2, localList3, localJCBlock, localJCExpression2);
/*     */   }
/*     */ 
/*     */   public JCTree visitMethodInvocation(MethodInvocationTree paramMethodInvocationTree, P paramP) {
/* 259 */     JCTree.JCMethodInvocation localJCMethodInvocation = (JCTree.JCMethodInvocation)paramMethodInvocationTree;
/* 260 */     List localList1 = copy(localJCMethodInvocation.typeargs, paramP);
/* 261 */     JCTree.JCExpression localJCExpression = (JCTree.JCExpression)copy(localJCMethodInvocation.meth, paramP);
/* 262 */     List localList2 = copy(localJCMethodInvocation.args, paramP);
/* 263 */     return this.M.at(localJCMethodInvocation.pos).Apply(localList1, localJCExpression, localList2);
/*     */   }
/*     */ 
/*     */   public JCTree visitModifiers(ModifiersTree paramModifiersTree, P paramP) {
/* 267 */     JCTree.JCModifiers localJCModifiers = (JCTree.JCModifiers)paramModifiersTree;
/* 268 */     List localList = copy(localJCModifiers.annotations, paramP);
/* 269 */     return this.M.at(localJCModifiers.pos).Modifiers(localJCModifiers.flags, localList);
/*     */   }
/*     */ 
/*     */   public JCTree visitNewArray(NewArrayTree paramNewArrayTree, P paramP) {
/* 273 */     JCTree.JCNewArray localJCNewArray = (JCTree.JCNewArray)paramNewArrayTree;
/* 274 */     JCTree.JCExpression localJCExpression = (JCTree.JCExpression)copy(localJCNewArray.elemtype, paramP);
/* 275 */     List localList1 = copy(localJCNewArray.dims, paramP);
/* 276 */     List localList2 = copy(localJCNewArray.elems, paramP);
/* 277 */     return this.M.at(localJCNewArray.pos).NewArray(localJCExpression, localList1, localList2);
/*     */   }
/*     */ 
/*     */   public JCTree visitNewClass(NewClassTree paramNewClassTree, P paramP) {
/* 281 */     JCTree.JCNewClass localJCNewClass = (JCTree.JCNewClass)paramNewClassTree;
/* 282 */     JCTree.JCExpression localJCExpression1 = (JCTree.JCExpression)copy(localJCNewClass.encl, paramP);
/* 283 */     List localList1 = copy(localJCNewClass.typeargs, paramP);
/* 284 */     JCTree.JCExpression localJCExpression2 = (JCTree.JCExpression)copy(localJCNewClass.clazz, paramP);
/* 285 */     List localList2 = copy(localJCNewClass.args, paramP);
/* 286 */     JCTree.JCClassDecl localJCClassDecl = (JCTree.JCClassDecl)copy(localJCNewClass.def, paramP);
/* 287 */     return this.M.at(localJCNewClass.pos).NewClass(localJCExpression1, localList1, localJCExpression2, localList2, localJCClassDecl);
/*     */   }
/*     */ 
/*     */   public JCTree visitLambdaExpression(LambdaExpressionTree paramLambdaExpressionTree, P paramP) {
/* 291 */     JCTree.JCLambda localJCLambda = (JCTree.JCLambda)paramLambdaExpressionTree;
/* 292 */     List localList = copy(localJCLambda.params, paramP);
/* 293 */     JCTree localJCTree = copy(localJCLambda.body, paramP);
/* 294 */     return this.M.at(localJCLambda.pos).Lambda(localList, localJCTree);
/*     */   }
/*     */ 
/*     */   public JCTree visitParenthesized(ParenthesizedTree paramParenthesizedTree, P paramP) {
/* 298 */     JCTree.JCParens localJCParens = (JCTree.JCParens)paramParenthesizedTree;
/* 299 */     JCTree.JCExpression localJCExpression = (JCTree.JCExpression)copy(localJCParens.expr, paramP);
/* 300 */     return this.M.at(localJCParens.pos).Parens(localJCExpression);
/*     */   }
/*     */ 
/*     */   public JCTree visitReturn(ReturnTree paramReturnTree, P paramP) {
/* 304 */     JCTree.JCReturn localJCReturn = (JCTree.JCReturn)paramReturnTree;
/* 305 */     JCTree.JCExpression localJCExpression = (JCTree.JCExpression)copy(localJCReturn.expr, paramP);
/* 306 */     return this.M.at(localJCReturn.pos).Return(localJCExpression);
/*     */   }
/*     */ 
/*     */   public JCTree visitMemberSelect(MemberSelectTree paramMemberSelectTree, P paramP) {
/* 310 */     JCTree.JCFieldAccess localJCFieldAccess = (JCTree.JCFieldAccess)paramMemberSelectTree;
/* 311 */     JCTree.JCExpression localJCExpression = (JCTree.JCExpression)copy(localJCFieldAccess.selected, paramP);
/* 312 */     return this.M.at(localJCFieldAccess.pos).Select(localJCExpression, localJCFieldAccess.name);
/*     */   }
/*     */ 
/*     */   public JCTree visitMemberReference(MemberReferenceTree paramMemberReferenceTree, P paramP) {
/* 316 */     JCTree.JCMemberReference localJCMemberReference = (JCTree.JCMemberReference)paramMemberReferenceTree;
/* 317 */     JCTree.JCExpression localJCExpression = (JCTree.JCExpression)copy(localJCMemberReference.expr, paramP);
/* 318 */     List localList = copy(localJCMemberReference.typeargs, paramP);
/* 319 */     return this.M.at(localJCMemberReference.pos).Reference(localJCMemberReference.mode, localJCMemberReference.name, localJCExpression, localList);
/*     */   }
/*     */ 
/*     */   public JCTree visitEmptyStatement(EmptyStatementTree paramEmptyStatementTree, P paramP) {
/* 323 */     JCTree.JCSkip localJCSkip = (JCTree.JCSkip)paramEmptyStatementTree;
/* 324 */     return this.M.at(localJCSkip.pos).Skip();
/*     */   }
/*     */ 
/*     */   public JCTree visitSwitch(SwitchTree paramSwitchTree, P paramP) {
/* 328 */     JCTree.JCSwitch localJCSwitch = (JCTree.JCSwitch)paramSwitchTree;
/* 329 */     JCTree.JCExpression localJCExpression = (JCTree.JCExpression)copy(localJCSwitch.selector, paramP);
/* 330 */     List localList = copy(localJCSwitch.cases, paramP);
/* 331 */     return this.M.at(localJCSwitch.pos).Switch(localJCExpression, localList);
/*     */   }
/*     */ 
/*     */   public JCTree visitSynchronized(SynchronizedTree paramSynchronizedTree, P paramP) {
/* 335 */     JCTree.JCSynchronized localJCSynchronized = (JCTree.JCSynchronized)paramSynchronizedTree;
/* 336 */     JCTree.JCExpression localJCExpression = (JCTree.JCExpression)copy(localJCSynchronized.lock, paramP);
/* 337 */     JCTree.JCBlock localJCBlock = (JCTree.JCBlock)copy(localJCSynchronized.body, paramP);
/* 338 */     return this.M.at(localJCSynchronized.pos).Synchronized(localJCExpression, localJCBlock);
/*     */   }
/*     */ 
/*     */   public JCTree visitThrow(ThrowTree paramThrowTree, P paramP) {
/* 342 */     JCTree.JCThrow localJCThrow = (JCTree.JCThrow)paramThrowTree;
/* 343 */     JCTree.JCExpression localJCExpression = (JCTree.JCExpression)copy(localJCThrow.expr, paramP);
/* 344 */     return this.M.at(localJCThrow.pos).Throw(localJCExpression);
/*     */   }
/*     */ 
/*     */   public JCTree visitCompilationUnit(CompilationUnitTree paramCompilationUnitTree, P paramP) {
/* 348 */     JCTree.JCCompilationUnit localJCCompilationUnit = (JCTree.JCCompilationUnit)paramCompilationUnitTree;
/* 349 */     List localList1 = copy(localJCCompilationUnit.packageAnnotations, paramP);
/* 350 */     JCTree.JCExpression localJCExpression = (JCTree.JCExpression)copy(localJCCompilationUnit.pid, paramP);
/* 351 */     List localList2 = copy(localJCCompilationUnit.defs, paramP);
/* 352 */     return this.M.at(localJCCompilationUnit.pos).TopLevel(localList1, localJCExpression, localList2);
/*     */   }
/*     */ 
/*     */   public JCTree visitTry(TryTree paramTryTree, P paramP) {
/* 356 */     JCTree.JCTry localJCTry = (JCTree.JCTry)paramTryTree;
/* 357 */     List localList1 = copy(localJCTry.resources, paramP);
/* 358 */     JCTree.JCBlock localJCBlock1 = (JCTree.JCBlock)copy(localJCTry.body, paramP);
/* 359 */     List localList2 = copy(localJCTry.catchers, paramP);
/* 360 */     JCTree.JCBlock localJCBlock2 = (JCTree.JCBlock)copy(localJCTry.finalizer, paramP);
/* 361 */     return this.M.at(localJCTry.pos).Try(localList1, localJCBlock1, localList2, localJCBlock2);
/*     */   }
/*     */ 
/*     */   public JCTree visitParameterizedType(ParameterizedTypeTree paramParameterizedTypeTree, P paramP) {
/* 365 */     JCTree.JCTypeApply localJCTypeApply = (JCTree.JCTypeApply)paramParameterizedTypeTree;
/* 366 */     JCTree.JCExpression localJCExpression = (JCTree.JCExpression)copy(localJCTypeApply.clazz, paramP);
/* 367 */     List localList = copy(localJCTypeApply.arguments, paramP);
/* 368 */     return this.M.at(localJCTypeApply.pos).TypeApply(localJCExpression, localList);
/*     */   }
/*     */ 
/*     */   public JCTree visitUnionType(UnionTypeTree paramUnionTypeTree, P paramP) {
/* 372 */     JCTree.JCTypeUnion localJCTypeUnion = (JCTree.JCTypeUnion)paramUnionTypeTree;
/* 373 */     List localList = copy(localJCTypeUnion.alternatives, paramP);
/* 374 */     return this.M.at(localJCTypeUnion.pos).TypeUnion(localList);
/*     */   }
/*     */ 
/*     */   public JCTree visitIntersectionType(IntersectionTypeTree paramIntersectionTypeTree, P paramP) {
/* 378 */     JCTree.JCTypeIntersection localJCTypeIntersection = (JCTree.JCTypeIntersection)paramIntersectionTypeTree;
/* 379 */     List localList = copy(localJCTypeIntersection.bounds, paramP);
/* 380 */     return this.M.at(localJCTypeIntersection.pos).TypeIntersection(localList);
/*     */   }
/*     */ 
/*     */   public JCTree visitArrayType(ArrayTypeTree paramArrayTypeTree, P paramP) {
/* 384 */     JCTree.JCArrayTypeTree localJCArrayTypeTree = (JCTree.JCArrayTypeTree)paramArrayTypeTree;
/* 385 */     JCTree.JCExpression localJCExpression = (JCTree.JCExpression)copy(localJCArrayTypeTree.elemtype, paramP);
/* 386 */     return this.M.at(localJCArrayTypeTree.pos).TypeArray(localJCExpression);
/*     */   }
/*     */ 
/*     */   public JCTree visitTypeCast(TypeCastTree paramTypeCastTree, P paramP) {
/* 390 */     JCTree.JCTypeCast localJCTypeCast = (JCTree.JCTypeCast)paramTypeCastTree;
/* 391 */     JCTree localJCTree = copy(localJCTypeCast.clazz, paramP);
/* 392 */     JCTree.JCExpression localJCExpression = (JCTree.JCExpression)copy(localJCTypeCast.expr, paramP);
/* 393 */     return this.M.at(localJCTypeCast.pos).TypeCast(localJCTree, localJCExpression);
/*     */   }
/*     */ 
/*     */   public JCTree visitPrimitiveType(PrimitiveTypeTree paramPrimitiveTypeTree, P paramP) {
/* 397 */     JCTree.JCPrimitiveTypeTree localJCPrimitiveTypeTree = (JCTree.JCPrimitiveTypeTree)paramPrimitiveTypeTree;
/* 398 */     return this.M.at(localJCPrimitiveTypeTree.pos).TypeIdent(localJCPrimitiveTypeTree.typetag);
/*     */   }
/*     */ 
/*     */   public JCTree visitTypeParameter(TypeParameterTree paramTypeParameterTree, P paramP) {
/* 402 */     JCTree.JCTypeParameter localJCTypeParameter = (JCTree.JCTypeParameter)paramTypeParameterTree;
/* 403 */     List localList1 = copy(localJCTypeParameter.annotations, paramP);
/* 404 */     List localList2 = copy(localJCTypeParameter.bounds, paramP);
/* 405 */     return this.M.at(localJCTypeParameter.pos).TypeParameter(localJCTypeParameter.name, localList2, localList1);
/*     */   }
/*     */ 
/*     */   public JCTree visitInstanceOf(InstanceOfTree paramInstanceOfTree, P paramP) {
/* 409 */     JCTree.JCInstanceOf localJCInstanceOf = (JCTree.JCInstanceOf)paramInstanceOfTree;
/* 410 */     JCTree.JCExpression localJCExpression = (JCTree.JCExpression)copy(localJCInstanceOf.expr, paramP);
/* 411 */     JCTree localJCTree = copy(localJCInstanceOf.clazz, paramP);
/* 412 */     return this.M.at(localJCInstanceOf.pos).TypeTest(localJCExpression, localJCTree);
/*     */   }
/*     */ 
/*     */   public JCTree visitUnary(UnaryTree paramUnaryTree, P paramP) {
/* 416 */     JCTree.JCUnary localJCUnary = (JCTree.JCUnary)paramUnaryTree;
/* 417 */     JCTree.JCExpression localJCExpression = (JCTree.JCExpression)copy(localJCUnary.arg, paramP);
/* 418 */     return this.M.at(localJCUnary.pos).Unary(localJCUnary.getTag(), localJCExpression);
/*     */   }
/*     */ 
/*     */   public JCTree visitVariable(VariableTree paramVariableTree, P paramP) {
/* 422 */     JCTree.JCVariableDecl localJCVariableDecl = (JCTree.JCVariableDecl)paramVariableTree;
/* 423 */     JCTree.JCModifiers localJCModifiers = (JCTree.JCModifiers)copy(localJCVariableDecl.mods, paramP);
/* 424 */     JCTree.JCExpression localJCExpression1 = (JCTree.JCExpression)copy(localJCVariableDecl.vartype, paramP);
/* 425 */     if (localJCVariableDecl.nameexpr == null) {
/* 426 */       localJCExpression2 = (JCTree.JCExpression)copy(localJCVariableDecl.init, paramP);
/* 427 */       return this.M.at(localJCVariableDecl.pos).VarDef(localJCModifiers, localJCVariableDecl.name, localJCExpression1, localJCExpression2);
/*     */     }
/* 429 */     JCTree.JCExpression localJCExpression2 = (JCTree.JCExpression)copy(localJCVariableDecl.nameexpr, paramP);
/* 430 */     return this.M.at(localJCVariableDecl.pos).ReceiverVarDef(localJCModifiers, localJCExpression2, localJCExpression1);
/*     */   }
/*     */ 
/*     */   public JCTree visitWhileLoop(WhileLoopTree paramWhileLoopTree, P paramP)
/*     */   {
/* 435 */     JCTree.JCWhileLoop localJCWhileLoop = (JCTree.JCWhileLoop)paramWhileLoopTree;
/* 436 */     JCTree.JCStatement localJCStatement = (JCTree.JCStatement)copy(localJCWhileLoop.body, paramP);
/* 437 */     JCTree.JCExpression localJCExpression = (JCTree.JCExpression)copy(localJCWhileLoop.cond, paramP);
/* 438 */     return this.M.at(localJCWhileLoop.pos).WhileLoop(localJCExpression, localJCStatement);
/*     */   }
/*     */ 
/*     */   public JCTree visitWildcard(WildcardTree paramWildcardTree, P paramP) {
/* 442 */     JCTree.JCWildcard localJCWildcard = (JCTree.JCWildcard)paramWildcardTree;
/* 443 */     JCTree.TypeBoundKind localTypeBoundKind = this.M.at(localJCWildcard.kind.pos).TypeBoundKind(localJCWildcard.kind.kind);
/* 444 */     JCTree localJCTree = copy(localJCWildcard.inner, paramP);
/* 445 */     return this.M.at(localJCWildcard.pos).Wildcard(localTypeBoundKind, localJCTree);
/*     */   }
/*     */ 
/*     */   public JCTree visitOther(Tree paramTree, P paramP) {
/* 449 */     JCTree localJCTree1 = (JCTree)paramTree;
/* 450 */     switch (1.$SwitchMap$com$sun$tools$javac$tree$JCTree$Tag[localJCTree1.getTag().ordinal()]) {
/*     */     case 1:
/* 452 */       JCTree.LetExpr localLetExpr = (JCTree.LetExpr)paramTree;
/* 453 */       List localList = copy(localLetExpr.defs, paramP);
/* 454 */       JCTree localJCTree2 = copy(localLetExpr.expr, paramP);
/* 455 */       return this.M.at(localLetExpr.pos).LetExpr(localList, localJCTree2);
/*     */     }
/*     */ 
/* 458 */     throw new AssertionError("unknown tree tag: " + localJCTree1.getTag());
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.javac.tree.TreeCopier
 * JD-Core Version:    0.6.2
 */