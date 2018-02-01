/*     */ package com.sun.tools.javac.tree;
/*     */ 
/*     */ import com.sun.tools.javac.util.List;
/*     */ 
/*     */ public class TreeTranslator extends JCTree.Visitor
/*     */ {
/*     */   protected JCTree result;
/*     */ 
/*     */   public <T extends JCTree> T translate(T paramT)
/*     */   {
/*  55 */     if (paramT == null) {
/*  56 */       return null;
/*     */     }
/*  58 */     paramT.accept(this);
/*  59 */     JCTree localJCTree = this.result;
/*  60 */     this.result = null;
/*  61 */     return localJCTree;
/*     */   }
/*     */ 
/*     */   public <T extends JCTree> List<T> translate(List<T> paramList)
/*     */   {
/*  68 */     if (paramList == null) return null;
/*  69 */     for (Object localObject = paramList; ((List)localObject).nonEmpty(); localObject = ((List)localObject).tail)
/*  70 */       ((List)localObject).head = translate((JCTree)((List)localObject).head);
/*  71 */     return paramList;
/*     */   }
/*     */ 
/*     */   public List<JCTree.JCVariableDecl> translateVarDefs(List<JCTree.JCVariableDecl> paramList)
/*     */   {
/*  77 */     for (Object localObject = paramList; ((List)localObject).nonEmpty(); localObject = ((List)localObject).tail)
/*  78 */       ((List)localObject).head = translate((JCTree)((List)localObject).head);
/*  79 */     return paramList;
/*     */   }
/*     */ 
/*     */   public List<JCTree.JCTypeParameter> translateTypeParams(List<JCTree.JCTypeParameter> paramList)
/*     */   {
/*  85 */     for (Object localObject = paramList; ((List)localObject).nonEmpty(); localObject = ((List)localObject).tail)
/*  86 */       ((List)localObject).head = translate((JCTree)((List)localObject).head);
/*  87 */     return paramList;
/*     */   }
/*     */ 
/*     */   public List<JCTree.JCCase> translateCases(List<JCTree.JCCase> paramList)
/*     */   {
/*  93 */     for (Object localObject = paramList; ((List)localObject).nonEmpty(); localObject = ((List)localObject).tail)
/*  94 */       ((List)localObject).head = translate((JCTree)((List)localObject).head);
/*  95 */     return paramList;
/*     */   }
/*     */ 
/*     */   public List<JCTree.JCCatch> translateCatchers(List<JCTree.JCCatch> paramList)
/*     */   {
/* 101 */     for (Object localObject = paramList; ((List)localObject).nonEmpty(); localObject = ((List)localObject).tail)
/* 102 */       ((List)localObject).head = translate((JCTree)((List)localObject).head);
/* 103 */     return paramList;
/*     */   }
/*     */ 
/*     */   public List<JCTree.JCAnnotation> translateAnnotations(List<JCTree.JCAnnotation> paramList)
/*     */   {
/* 109 */     for (Object localObject = paramList; ((List)localObject).nonEmpty(); localObject = ((List)localObject).tail)
/* 110 */       ((List)localObject).head = translate((JCTree)((List)localObject).head);
/* 111 */     return paramList;
/*     */   }
/*     */ 
/*     */   public void visitTopLevel(JCTree.JCCompilationUnit paramJCCompilationUnit)
/*     */   {
/* 119 */     paramJCCompilationUnit.pid = ((JCTree.JCExpression)translate(paramJCCompilationUnit.pid));
/* 120 */     paramJCCompilationUnit.defs = translate(paramJCCompilationUnit.defs);
/* 121 */     this.result = paramJCCompilationUnit;
/*     */   }
/*     */ 
/*     */   public void visitImport(JCTree.JCImport paramJCImport) {
/* 125 */     paramJCImport.qualid = translate(paramJCImport.qualid);
/* 126 */     this.result = paramJCImport;
/*     */   }
/*     */ 
/*     */   public void visitClassDef(JCTree.JCClassDecl paramJCClassDecl) {
/* 130 */     paramJCClassDecl.mods = ((JCTree.JCModifiers)translate(paramJCClassDecl.mods));
/* 131 */     paramJCClassDecl.typarams = translateTypeParams(paramJCClassDecl.typarams);
/* 132 */     paramJCClassDecl.extending = ((JCTree.JCExpression)translate(paramJCClassDecl.extending));
/* 133 */     paramJCClassDecl.implementing = translate(paramJCClassDecl.implementing);
/* 134 */     paramJCClassDecl.defs = translate(paramJCClassDecl.defs);
/* 135 */     this.result = paramJCClassDecl;
/*     */   }
/*     */ 
/*     */   public void visitMethodDef(JCTree.JCMethodDecl paramJCMethodDecl) {
/* 139 */     paramJCMethodDecl.mods = ((JCTree.JCModifiers)translate(paramJCMethodDecl.mods));
/* 140 */     paramJCMethodDecl.restype = ((JCTree.JCExpression)translate(paramJCMethodDecl.restype));
/* 141 */     paramJCMethodDecl.typarams = translateTypeParams(paramJCMethodDecl.typarams);
/* 142 */     paramJCMethodDecl.recvparam = ((JCTree.JCVariableDecl)translate(paramJCMethodDecl.recvparam));
/* 143 */     paramJCMethodDecl.params = translateVarDefs(paramJCMethodDecl.params);
/* 144 */     paramJCMethodDecl.thrown = translate(paramJCMethodDecl.thrown);
/* 145 */     paramJCMethodDecl.body = ((JCTree.JCBlock)translate(paramJCMethodDecl.body));
/* 146 */     this.result = paramJCMethodDecl;
/*     */   }
/*     */ 
/*     */   public void visitVarDef(JCTree.JCVariableDecl paramJCVariableDecl) {
/* 150 */     paramJCVariableDecl.mods = ((JCTree.JCModifiers)translate(paramJCVariableDecl.mods));
/* 151 */     paramJCVariableDecl.nameexpr = ((JCTree.JCExpression)translate(paramJCVariableDecl.nameexpr));
/* 152 */     paramJCVariableDecl.vartype = ((JCTree.JCExpression)translate(paramJCVariableDecl.vartype));
/* 153 */     paramJCVariableDecl.init = ((JCTree.JCExpression)translate(paramJCVariableDecl.init));
/* 154 */     this.result = paramJCVariableDecl;
/*     */   }
/*     */ 
/*     */   public void visitSkip(JCTree.JCSkip paramJCSkip) {
/* 158 */     this.result = paramJCSkip;
/*     */   }
/*     */ 
/*     */   public void visitBlock(JCTree.JCBlock paramJCBlock) {
/* 162 */     paramJCBlock.stats = translate(paramJCBlock.stats);
/* 163 */     this.result = paramJCBlock;
/*     */   }
/*     */ 
/*     */   public void visitDoLoop(JCTree.JCDoWhileLoop paramJCDoWhileLoop) {
/* 167 */     paramJCDoWhileLoop.body = ((JCTree.JCStatement)translate(paramJCDoWhileLoop.body));
/* 168 */     paramJCDoWhileLoop.cond = ((JCTree.JCExpression)translate(paramJCDoWhileLoop.cond));
/* 169 */     this.result = paramJCDoWhileLoop;
/*     */   }
/*     */ 
/*     */   public void visitWhileLoop(JCTree.JCWhileLoop paramJCWhileLoop) {
/* 173 */     paramJCWhileLoop.cond = ((JCTree.JCExpression)translate(paramJCWhileLoop.cond));
/* 174 */     paramJCWhileLoop.body = ((JCTree.JCStatement)translate(paramJCWhileLoop.body));
/* 175 */     this.result = paramJCWhileLoop;
/*     */   }
/*     */ 
/*     */   public void visitForLoop(JCTree.JCForLoop paramJCForLoop) {
/* 179 */     paramJCForLoop.init = translate(paramJCForLoop.init);
/* 180 */     paramJCForLoop.cond = ((JCTree.JCExpression)translate(paramJCForLoop.cond));
/* 181 */     paramJCForLoop.step = translate(paramJCForLoop.step);
/* 182 */     paramJCForLoop.body = ((JCTree.JCStatement)translate(paramJCForLoop.body));
/* 183 */     this.result = paramJCForLoop;
/*     */   }
/*     */ 
/*     */   public void visitForeachLoop(JCTree.JCEnhancedForLoop paramJCEnhancedForLoop) {
/* 187 */     paramJCEnhancedForLoop.var = ((JCTree.JCVariableDecl)translate(paramJCEnhancedForLoop.var));
/* 188 */     paramJCEnhancedForLoop.expr = ((JCTree.JCExpression)translate(paramJCEnhancedForLoop.expr));
/* 189 */     paramJCEnhancedForLoop.body = ((JCTree.JCStatement)translate(paramJCEnhancedForLoop.body));
/* 190 */     this.result = paramJCEnhancedForLoop;
/*     */   }
/*     */ 
/*     */   public void visitLabelled(JCTree.JCLabeledStatement paramJCLabeledStatement) {
/* 194 */     paramJCLabeledStatement.body = ((JCTree.JCStatement)translate(paramJCLabeledStatement.body));
/* 195 */     this.result = paramJCLabeledStatement;
/*     */   }
/*     */ 
/*     */   public void visitSwitch(JCTree.JCSwitch paramJCSwitch) {
/* 199 */     paramJCSwitch.selector = ((JCTree.JCExpression)translate(paramJCSwitch.selector));
/* 200 */     paramJCSwitch.cases = translateCases(paramJCSwitch.cases);
/* 201 */     this.result = paramJCSwitch;
/*     */   }
/*     */ 
/*     */   public void visitCase(JCTree.JCCase paramJCCase) {
/* 205 */     paramJCCase.pat = ((JCTree.JCExpression)translate(paramJCCase.pat));
/* 206 */     paramJCCase.stats = translate(paramJCCase.stats);
/* 207 */     this.result = paramJCCase;
/*     */   }
/*     */ 
/*     */   public void visitSynchronized(JCTree.JCSynchronized paramJCSynchronized) {
/* 211 */     paramJCSynchronized.lock = ((JCTree.JCExpression)translate(paramJCSynchronized.lock));
/* 212 */     paramJCSynchronized.body = ((JCTree.JCBlock)translate(paramJCSynchronized.body));
/* 213 */     this.result = paramJCSynchronized;
/*     */   }
/*     */ 
/*     */   public void visitTry(JCTree.JCTry paramJCTry) {
/* 217 */     paramJCTry.resources = translate(paramJCTry.resources);
/* 218 */     paramJCTry.body = ((JCTree.JCBlock)translate(paramJCTry.body));
/* 219 */     paramJCTry.catchers = translateCatchers(paramJCTry.catchers);
/* 220 */     paramJCTry.finalizer = ((JCTree.JCBlock)translate(paramJCTry.finalizer));
/* 221 */     this.result = paramJCTry;
/*     */   }
/*     */ 
/*     */   public void visitCatch(JCTree.JCCatch paramJCCatch) {
/* 225 */     paramJCCatch.param = ((JCTree.JCVariableDecl)translate(paramJCCatch.param));
/* 226 */     paramJCCatch.body = ((JCTree.JCBlock)translate(paramJCCatch.body));
/* 227 */     this.result = paramJCCatch;
/*     */   }
/*     */ 
/*     */   public void visitConditional(JCTree.JCConditional paramJCConditional) {
/* 231 */     paramJCConditional.cond = ((JCTree.JCExpression)translate(paramJCConditional.cond));
/* 232 */     paramJCConditional.truepart = ((JCTree.JCExpression)translate(paramJCConditional.truepart));
/* 233 */     paramJCConditional.falsepart = ((JCTree.JCExpression)translate(paramJCConditional.falsepart));
/* 234 */     this.result = paramJCConditional;
/*     */   }
/*     */ 
/*     */   public void visitIf(JCTree.JCIf paramJCIf) {
/* 238 */     paramJCIf.cond = ((JCTree.JCExpression)translate(paramJCIf.cond));
/* 239 */     paramJCIf.thenpart = ((JCTree.JCStatement)translate(paramJCIf.thenpart));
/* 240 */     paramJCIf.elsepart = ((JCTree.JCStatement)translate(paramJCIf.elsepart));
/* 241 */     this.result = paramJCIf;
/*     */   }
/*     */ 
/*     */   public void visitExec(JCTree.JCExpressionStatement paramJCExpressionStatement) {
/* 245 */     paramJCExpressionStatement.expr = ((JCTree.JCExpression)translate(paramJCExpressionStatement.expr));
/* 246 */     this.result = paramJCExpressionStatement;
/*     */   }
/*     */ 
/*     */   public void visitBreak(JCTree.JCBreak paramJCBreak) {
/* 250 */     this.result = paramJCBreak;
/*     */   }
/*     */ 
/*     */   public void visitContinue(JCTree.JCContinue paramJCContinue) {
/* 254 */     this.result = paramJCContinue;
/*     */   }
/*     */ 
/*     */   public void visitReturn(JCTree.JCReturn paramJCReturn) {
/* 258 */     paramJCReturn.expr = ((JCTree.JCExpression)translate(paramJCReturn.expr));
/* 259 */     this.result = paramJCReturn;
/*     */   }
/*     */ 
/*     */   public void visitThrow(JCTree.JCThrow paramJCThrow) {
/* 263 */     paramJCThrow.expr = ((JCTree.JCExpression)translate(paramJCThrow.expr));
/* 264 */     this.result = paramJCThrow;
/*     */   }
/*     */ 
/*     */   public void visitAssert(JCTree.JCAssert paramJCAssert) {
/* 268 */     paramJCAssert.cond = ((JCTree.JCExpression)translate(paramJCAssert.cond));
/* 269 */     paramJCAssert.detail = ((JCTree.JCExpression)translate(paramJCAssert.detail));
/* 270 */     this.result = paramJCAssert;
/*     */   }
/*     */ 
/*     */   public void visitApply(JCTree.JCMethodInvocation paramJCMethodInvocation) {
/* 274 */     paramJCMethodInvocation.meth = ((JCTree.JCExpression)translate(paramJCMethodInvocation.meth));
/* 275 */     paramJCMethodInvocation.args = translate(paramJCMethodInvocation.args);
/* 276 */     this.result = paramJCMethodInvocation;
/*     */   }
/*     */ 
/*     */   public void visitNewClass(JCTree.JCNewClass paramJCNewClass) {
/* 280 */     paramJCNewClass.encl = ((JCTree.JCExpression)translate(paramJCNewClass.encl));
/* 281 */     paramJCNewClass.clazz = ((JCTree.JCExpression)translate(paramJCNewClass.clazz));
/* 282 */     paramJCNewClass.args = translate(paramJCNewClass.args);
/* 283 */     paramJCNewClass.def = ((JCTree.JCClassDecl)translate(paramJCNewClass.def));
/* 284 */     this.result = paramJCNewClass;
/*     */   }
/*     */ 
/*     */   public void visitLambda(JCTree.JCLambda paramJCLambda) {
/* 288 */     paramJCLambda.params = translate(paramJCLambda.params);
/* 289 */     paramJCLambda.body = translate(paramJCLambda.body);
/* 290 */     this.result = paramJCLambda;
/*     */   }
/*     */ 
/*     */   public void visitNewArray(JCTree.JCNewArray paramJCNewArray) {
/* 294 */     paramJCNewArray.annotations = translate(paramJCNewArray.annotations);
/* 295 */     List localList1 = List.nil();
/* 296 */     for (List localList2 : paramJCNewArray.dimAnnotations)
/* 297 */       localList1 = localList1.append(translate(localList2));
/* 298 */     paramJCNewArray.dimAnnotations = localList1;
/* 299 */     paramJCNewArray.elemtype = ((JCTree.JCExpression)translate(paramJCNewArray.elemtype));
/* 300 */     paramJCNewArray.dims = translate(paramJCNewArray.dims);
/* 301 */     paramJCNewArray.elems = translate(paramJCNewArray.elems);
/* 302 */     this.result = paramJCNewArray;
/*     */   }
/*     */ 
/*     */   public void visitParens(JCTree.JCParens paramJCParens) {
/* 306 */     paramJCParens.expr = ((JCTree.JCExpression)translate(paramJCParens.expr));
/* 307 */     this.result = paramJCParens;
/*     */   }
/*     */ 
/*     */   public void visitAssign(JCTree.JCAssign paramJCAssign) {
/* 311 */     paramJCAssign.lhs = ((JCTree.JCExpression)translate(paramJCAssign.lhs));
/* 312 */     paramJCAssign.rhs = ((JCTree.JCExpression)translate(paramJCAssign.rhs));
/* 313 */     this.result = paramJCAssign;
/*     */   }
/*     */ 
/*     */   public void visitAssignop(JCTree.JCAssignOp paramJCAssignOp) {
/* 317 */     paramJCAssignOp.lhs = ((JCTree.JCExpression)translate(paramJCAssignOp.lhs));
/* 318 */     paramJCAssignOp.rhs = ((JCTree.JCExpression)translate(paramJCAssignOp.rhs));
/* 319 */     this.result = paramJCAssignOp;
/*     */   }
/*     */ 
/*     */   public void visitUnary(JCTree.JCUnary paramJCUnary) {
/* 323 */     paramJCUnary.arg = ((JCTree.JCExpression)translate(paramJCUnary.arg));
/* 324 */     this.result = paramJCUnary;
/*     */   }
/*     */ 
/*     */   public void visitBinary(JCTree.JCBinary paramJCBinary) {
/* 328 */     paramJCBinary.lhs = ((JCTree.JCExpression)translate(paramJCBinary.lhs));
/* 329 */     paramJCBinary.rhs = ((JCTree.JCExpression)translate(paramJCBinary.rhs));
/* 330 */     this.result = paramJCBinary;
/*     */   }
/*     */ 
/*     */   public void visitTypeCast(JCTree.JCTypeCast paramJCTypeCast) {
/* 334 */     paramJCTypeCast.clazz = translate(paramJCTypeCast.clazz);
/* 335 */     paramJCTypeCast.expr = ((JCTree.JCExpression)translate(paramJCTypeCast.expr));
/* 336 */     this.result = paramJCTypeCast;
/*     */   }
/*     */ 
/*     */   public void visitTypeTest(JCTree.JCInstanceOf paramJCInstanceOf) {
/* 340 */     paramJCInstanceOf.expr = ((JCTree.JCExpression)translate(paramJCInstanceOf.expr));
/* 341 */     paramJCInstanceOf.clazz = translate(paramJCInstanceOf.clazz);
/* 342 */     this.result = paramJCInstanceOf;
/*     */   }
/*     */ 
/*     */   public void visitIndexed(JCTree.JCArrayAccess paramJCArrayAccess) {
/* 346 */     paramJCArrayAccess.indexed = ((JCTree.JCExpression)translate(paramJCArrayAccess.indexed));
/* 347 */     paramJCArrayAccess.index = ((JCTree.JCExpression)translate(paramJCArrayAccess.index));
/* 348 */     this.result = paramJCArrayAccess;
/*     */   }
/*     */ 
/*     */   public void visitSelect(JCTree.JCFieldAccess paramJCFieldAccess) {
/* 352 */     paramJCFieldAccess.selected = ((JCTree.JCExpression)translate(paramJCFieldAccess.selected));
/* 353 */     this.result = paramJCFieldAccess;
/*     */   }
/*     */ 
/*     */   public void visitReference(JCTree.JCMemberReference paramJCMemberReference) {
/* 357 */     paramJCMemberReference.expr = ((JCTree.JCExpression)translate(paramJCMemberReference.expr));
/* 358 */     this.result = paramJCMemberReference;
/*     */   }
/*     */ 
/*     */   public void visitIdent(JCTree.JCIdent paramJCIdent) {
/* 362 */     this.result = paramJCIdent;
/*     */   }
/*     */ 
/*     */   public void visitLiteral(JCTree.JCLiteral paramJCLiteral) {
/* 366 */     this.result = paramJCLiteral;
/*     */   }
/*     */ 
/*     */   public void visitTypeIdent(JCTree.JCPrimitiveTypeTree paramJCPrimitiveTypeTree) {
/* 370 */     this.result = paramJCPrimitiveTypeTree;
/*     */   }
/*     */ 
/*     */   public void visitTypeArray(JCTree.JCArrayTypeTree paramJCArrayTypeTree) {
/* 374 */     paramJCArrayTypeTree.elemtype = ((JCTree.JCExpression)translate(paramJCArrayTypeTree.elemtype));
/* 375 */     this.result = paramJCArrayTypeTree;
/*     */   }
/*     */ 
/*     */   public void visitTypeApply(JCTree.JCTypeApply paramJCTypeApply) {
/* 379 */     paramJCTypeApply.clazz = ((JCTree.JCExpression)translate(paramJCTypeApply.clazz));
/* 380 */     paramJCTypeApply.arguments = translate(paramJCTypeApply.arguments);
/* 381 */     this.result = paramJCTypeApply;
/*     */   }
/*     */ 
/*     */   public void visitTypeUnion(JCTree.JCTypeUnion paramJCTypeUnion) {
/* 385 */     paramJCTypeUnion.alternatives = translate(paramJCTypeUnion.alternatives);
/* 386 */     this.result = paramJCTypeUnion;
/*     */   }
/*     */ 
/*     */   public void visitTypeIntersection(JCTree.JCTypeIntersection paramJCTypeIntersection) {
/* 390 */     paramJCTypeIntersection.bounds = translate(paramJCTypeIntersection.bounds);
/* 391 */     this.result = paramJCTypeIntersection;
/*     */   }
/*     */ 
/*     */   public void visitTypeParameter(JCTree.JCTypeParameter paramJCTypeParameter) {
/* 395 */     paramJCTypeParameter.annotations = translate(paramJCTypeParameter.annotations);
/* 396 */     paramJCTypeParameter.bounds = translate(paramJCTypeParameter.bounds);
/* 397 */     this.result = paramJCTypeParameter;
/*     */   }
/*     */ 
/*     */   public void visitWildcard(JCTree.JCWildcard paramJCWildcard)
/*     */   {
/* 402 */     paramJCWildcard.kind = ((JCTree.TypeBoundKind)translate(paramJCWildcard.kind));
/* 403 */     paramJCWildcard.inner = translate(paramJCWildcard.inner);
/* 404 */     this.result = paramJCWildcard;
/*     */   }
/*     */ 
/*     */   public void visitTypeBoundKind(JCTree.TypeBoundKind paramTypeBoundKind)
/*     */   {
/* 409 */     this.result = paramTypeBoundKind;
/*     */   }
/*     */ 
/*     */   public void visitErroneous(JCTree.JCErroneous paramJCErroneous) {
/* 413 */     this.result = paramJCErroneous;
/*     */   }
/*     */ 
/*     */   public void visitLetExpr(JCTree.LetExpr paramLetExpr) {
/* 417 */     paramLetExpr.defs = translateVarDefs(paramLetExpr.defs);
/* 418 */     paramLetExpr.expr = translate(paramLetExpr.expr);
/* 419 */     this.result = paramLetExpr;
/*     */   }
/*     */ 
/*     */   public void visitModifiers(JCTree.JCModifiers paramJCModifiers) {
/* 423 */     paramJCModifiers.annotations = translateAnnotations(paramJCModifiers.annotations);
/* 424 */     this.result = paramJCModifiers;
/*     */   }
/*     */ 
/*     */   public void visitAnnotation(JCTree.JCAnnotation paramJCAnnotation) {
/* 428 */     paramJCAnnotation.annotationType = translate(paramJCAnnotation.annotationType);
/* 429 */     paramJCAnnotation.args = translate(paramJCAnnotation.args);
/* 430 */     this.result = paramJCAnnotation;
/*     */   }
/*     */ 
/*     */   public void visitAnnotatedType(JCTree.JCAnnotatedType paramJCAnnotatedType) {
/* 434 */     paramJCAnnotatedType.annotations = translate(paramJCAnnotatedType.annotations);
/* 435 */     paramJCAnnotatedType.underlyingType = ((JCTree.JCExpression)translate(paramJCAnnotatedType.underlyingType));
/* 436 */     this.result = paramJCAnnotatedType;
/*     */   }
/*     */ 
/*     */   public void visitTree(JCTree paramJCTree) {
/* 440 */     throw new AssertionError(paramJCTree);
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.javac.tree.TreeTranslator
 * JD-Core Version:    0.6.2
 */