/*     */ package com.sun.tools.javac.tree;
/*     */ 
/*     */ import com.sun.source.tree.MemberReferenceTree.ReferenceMode;
/*     */ import com.sun.tools.javac.code.Attribute;
/*     */ import com.sun.tools.javac.code.Attribute.Array;
/*     */ import com.sun.tools.javac.code.Attribute.Class;
/*     */ import com.sun.tools.javac.code.Attribute.Compound;
/*     */ import com.sun.tools.javac.code.Attribute.Constant;
/*     */ import com.sun.tools.javac.code.Attribute.Enum;
/*     */ import com.sun.tools.javac.code.Attribute.Error;
/*     */ import com.sun.tools.javac.code.Attribute.TypeCompound;
/*     */ import com.sun.tools.javac.code.Attribute.Visitor;
/*     */ import com.sun.tools.javac.code.BoundKind;
/*     */ import com.sun.tools.javac.code.Scope;
/*     */ import com.sun.tools.javac.code.Scope.Entry;
/*     */ import com.sun.tools.javac.code.Scope.ImportScope;
/*     */ import com.sun.tools.javac.code.Scope.StarImportScope;
/*     */ import com.sun.tools.javac.code.Symbol;
/*     */ import com.sun.tools.javac.code.Symbol.ClassSymbol;
/*     */ import com.sun.tools.javac.code.Symbol.MethodSymbol;
/*     */ import com.sun.tools.javac.code.Symbol.PackageSymbol;
/*     */ import com.sun.tools.javac.code.Symbol.TypeSymbol;
/*     */ import com.sun.tools.javac.code.Symbol.VarSymbol;
/*     */ import com.sun.tools.javac.code.Symtab;
/*     */ import com.sun.tools.javac.code.Type;
/*     */ import com.sun.tools.javac.code.Type.ArrayType;
/*     */ import com.sun.tools.javac.code.Type.JCPrimitiveType;
/*     */ import com.sun.tools.javac.code.Type.TypeVar;
/*     */ import com.sun.tools.javac.code.Type.WildcardType;
/*     */ import com.sun.tools.javac.code.TypeTag;
/*     */ import com.sun.tools.javac.code.Types;
/*     */ import com.sun.tools.javac.util.Assert;
/*     */ import com.sun.tools.javac.util.Context;
/*     */ import com.sun.tools.javac.util.Context.Key;
/*     */ import com.sun.tools.javac.util.JCDiagnostic.DiagnosticPosition;
/*     */ import com.sun.tools.javac.util.List;
/*     */ import com.sun.tools.javac.util.ListBuffer;
/*     */ import com.sun.tools.javac.util.Name;
/*     */ import com.sun.tools.javac.util.Names;
/*     */ import com.sun.tools.javac.util.Pair;
/*     */ import java.util.Iterator;
/*     */ 
/*     */ public class TreeMaker
/*     */   implements JCTree.Factory
/*     */ {
/*  50 */   protected static final Context.Key<TreeMaker> treeMakerKey = new Context.Key();
/*     */ 
/*  63 */   public int pos = -1;
/*     */   public JCTree.JCCompilationUnit toplevel;
/*     */   Names names;
/*     */   Types types;
/*     */   Symtab syms;
/* 838 */   AnnotationBuilder annotationBuilder = new AnnotationBuilder();
/*     */ 
/*     */   public static TreeMaker instance(Context paramContext)
/*     */   {
/*  55 */     TreeMaker localTreeMaker = (TreeMaker)paramContext.get(treeMakerKey);
/*  56 */     if (localTreeMaker == null)
/*  57 */       localTreeMaker = new TreeMaker(paramContext);
/*  58 */     return localTreeMaker;
/*     */   }
/*     */ 
/*     */   protected TreeMaker(Context paramContext)
/*     */   {
/*  80 */     paramContext.put(treeMakerKey, this);
/*  81 */     this.pos = -1;
/*  82 */     this.toplevel = null;
/*  83 */     this.names = Names.instance(paramContext);
/*  84 */     this.syms = Symtab.instance(paramContext);
/*  85 */     this.types = Types.instance(paramContext);
/*     */   }
/*     */ 
/*     */   protected TreeMaker(JCTree.JCCompilationUnit paramJCCompilationUnit, Names paramNames, Types paramTypes, Symtab paramSymtab)
/*     */   {
/*  91 */     this.pos = 0;
/*  92 */     this.toplevel = paramJCCompilationUnit;
/*  93 */     this.names = paramNames;
/*  94 */     this.types = paramTypes;
/*  95 */     this.syms = paramSymtab;
/*     */   }
/*     */ 
/*     */   public TreeMaker forToplevel(JCTree.JCCompilationUnit paramJCCompilationUnit)
/*     */   {
/* 101 */     return new TreeMaker(paramJCCompilationUnit, this.names, this.types, this.syms);
/*     */   }
/*     */ 
/*     */   public TreeMaker at(int paramInt)
/*     */   {
/* 107 */     this.pos = paramInt;
/* 108 */     return this;
/*     */   }
/*     */ 
/*     */   public TreeMaker at(JCDiagnostic.DiagnosticPosition paramDiagnosticPosition)
/*     */   {
/* 114 */     this.pos = (paramDiagnosticPosition == null ? -1 : paramDiagnosticPosition.getStartPosition());
/* 115 */     return this;
/*     */   }
/*     */ 
/*     */   public JCTree.JCCompilationUnit TopLevel(List<JCTree.JCAnnotation> paramList, JCTree.JCExpression paramJCExpression, List<JCTree> paramList1)
/*     */   {
/* 125 */     Assert.checkNonNull(paramList);
/* 126 */     for (Object localObject = paramList1.iterator(); ((Iterator)localObject).hasNext(); ) { JCTree localJCTree = (JCTree)((Iterator)localObject).next();
/* 127 */       Assert.check(((localJCTree instanceof JCTree.JCClassDecl)) || ((localJCTree instanceof JCTree.JCImport)) || ((localJCTree instanceof JCTree.JCSkip)) || ((localJCTree instanceof JCTree.JCErroneous)) || (((localJCTree instanceof JCTree.JCExpressionStatement)) && ((((JCTree.JCExpressionStatement)localJCTree).expr instanceof JCTree.JCErroneous))), localJCTree
/* 133 */         .getClass().getSimpleName()); }
/* 134 */     localObject = new JCTree.JCCompilationUnit(paramList, paramJCExpression, paramList1, null, null, null, null);
/*     */ 
/* 136 */     ((JCTree.JCCompilationUnit)localObject).pos = this.pos;
/* 137 */     return localObject;
/*     */   }
/*     */ 
/*     */   public JCTree.JCImport Import(JCTree paramJCTree, boolean paramBoolean) {
/* 141 */     JCTree.JCImport localJCImport = new JCTree.JCImport(paramJCTree, paramBoolean);
/* 142 */     localJCImport.pos = this.pos;
/* 143 */     return localJCImport;
/*     */   }
/*     */ 
/*     */   public JCTree.JCClassDecl ClassDef(JCTree.JCModifiers paramJCModifiers, Name paramName, List<JCTree.JCTypeParameter> paramList, JCTree.JCExpression paramJCExpression, List<JCTree.JCExpression> paramList1, List<JCTree> paramList2)
/*     */   {
/* 153 */     JCTree.JCClassDecl localJCClassDecl = new JCTree.JCClassDecl(paramJCModifiers, paramName, paramList, paramJCExpression, paramList1, paramList2, null);
/*     */ 
/* 160 */     localJCClassDecl.pos = this.pos;
/* 161 */     return localJCClassDecl;
/*     */   }
/*     */ 
/*     */   public JCTree.JCMethodDecl MethodDef(JCTree.JCModifiers paramJCModifiers, Name paramName, JCTree.JCExpression paramJCExpression1, List<JCTree.JCTypeParameter> paramList, List<JCTree.JCVariableDecl> paramList1, List<JCTree.JCExpression> paramList2, JCTree.JCBlock paramJCBlock, JCTree.JCExpression paramJCExpression2)
/*     */   {
/* 172 */     return MethodDef(paramJCModifiers, paramName, paramJCExpression1, paramList, null, paramList1, paramList2, paramJCBlock, paramJCExpression2);
/*     */   }
/*     */ 
/*     */   public JCTree.JCMethodDecl MethodDef(JCTree.JCModifiers paramJCModifiers, Name paramName, JCTree.JCExpression paramJCExpression1, List<JCTree.JCTypeParameter> paramList, JCTree.JCVariableDecl paramJCVariableDecl, List<JCTree.JCVariableDecl> paramList1, List<JCTree.JCExpression> paramList2, JCTree.JCBlock paramJCBlock, JCTree.JCExpression paramJCExpression2)
/*     */   {
/* 187 */     JCTree.JCMethodDecl localJCMethodDecl = new JCTree.JCMethodDecl(paramJCModifiers, paramName, paramJCExpression1, paramList, paramJCVariableDecl, paramList1, paramList2, paramJCBlock, paramJCExpression2, null);
/*     */ 
/* 197 */     localJCMethodDecl.pos = this.pos;
/* 198 */     return localJCMethodDecl;
/*     */   }
/*     */ 
/*     */   public JCTree.JCVariableDecl VarDef(JCTree.JCModifiers paramJCModifiers, Name paramName, JCTree.JCExpression paramJCExpression1, JCTree.JCExpression paramJCExpression2) {
/* 202 */     JCTree.JCVariableDecl localJCVariableDecl = new JCTree.JCVariableDecl(paramJCModifiers, paramName, paramJCExpression1, paramJCExpression2, null);
/* 203 */     localJCVariableDecl.pos = this.pos;
/* 204 */     return localJCVariableDecl;
/*     */   }
/*     */ 
/*     */   public JCTree.JCVariableDecl ReceiverVarDef(JCTree.JCModifiers paramJCModifiers, JCTree.JCExpression paramJCExpression1, JCTree.JCExpression paramJCExpression2) {
/* 208 */     JCTree.JCVariableDecl localJCVariableDecl = new JCTree.JCVariableDecl(paramJCModifiers, paramJCExpression1, paramJCExpression2);
/* 209 */     localJCVariableDecl.pos = this.pos;
/* 210 */     return localJCVariableDecl;
/*     */   }
/*     */ 
/*     */   public JCTree.JCSkip Skip() {
/* 214 */     JCTree.JCSkip localJCSkip = new JCTree.JCSkip();
/* 215 */     localJCSkip.pos = this.pos;
/* 216 */     return localJCSkip;
/*     */   }
/*     */ 
/*     */   public JCTree.JCBlock Block(long paramLong, List<JCTree.JCStatement> paramList) {
/* 220 */     JCTree.JCBlock localJCBlock = new JCTree.JCBlock(paramLong, paramList);
/* 221 */     localJCBlock.pos = this.pos;
/* 222 */     return localJCBlock;
/*     */   }
/*     */ 
/*     */   public JCTree.JCDoWhileLoop DoLoop(JCTree.JCStatement paramJCStatement, JCTree.JCExpression paramJCExpression) {
/* 226 */     JCTree.JCDoWhileLoop localJCDoWhileLoop = new JCTree.JCDoWhileLoop(paramJCStatement, paramJCExpression);
/* 227 */     localJCDoWhileLoop.pos = this.pos;
/* 228 */     return localJCDoWhileLoop;
/*     */   }
/*     */ 
/*     */   public JCTree.JCWhileLoop WhileLoop(JCTree.JCExpression paramJCExpression, JCTree.JCStatement paramJCStatement) {
/* 232 */     JCTree.JCWhileLoop localJCWhileLoop = new JCTree.JCWhileLoop(paramJCExpression, paramJCStatement);
/* 233 */     localJCWhileLoop.pos = this.pos;
/* 234 */     return localJCWhileLoop;
/*     */   }
/*     */ 
/*     */   public JCTree.JCForLoop ForLoop(List<JCTree.JCStatement> paramList, JCTree.JCExpression paramJCExpression, List<JCTree.JCExpressionStatement> paramList1, JCTree.JCStatement paramJCStatement)
/*     */   {
/* 242 */     JCTree.JCForLoop localJCForLoop = new JCTree.JCForLoop(paramList, paramJCExpression, paramList1, paramJCStatement);
/* 243 */     localJCForLoop.pos = this.pos;
/* 244 */     return localJCForLoop;
/*     */   }
/*     */ 
/*     */   public JCTree.JCEnhancedForLoop ForeachLoop(JCTree.JCVariableDecl paramJCVariableDecl, JCTree.JCExpression paramJCExpression, JCTree.JCStatement paramJCStatement) {
/* 248 */     JCTree.JCEnhancedForLoop localJCEnhancedForLoop = new JCTree.JCEnhancedForLoop(paramJCVariableDecl, paramJCExpression, paramJCStatement);
/* 249 */     localJCEnhancedForLoop.pos = this.pos;
/* 250 */     return localJCEnhancedForLoop;
/*     */   }
/*     */ 
/*     */   public JCTree.JCLabeledStatement Labelled(Name paramName, JCTree.JCStatement paramJCStatement) {
/* 254 */     JCTree.JCLabeledStatement localJCLabeledStatement = new JCTree.JCLabeledStatement(paramName, paramJCStatement);
/* 255 */     localJCLabeledStatement.pos = this.pos;
/* 256 */     return localJCLabeledStatement;
/*     */   }
/*     */ 
/*     */   public JCTree.JCSwitch Switch(JCTree.JCExpression paramJCExpression, List<JCTree.JCCase> paramList) {
/* 260 */     JCTree.JCSwitch localJCSwitch = new JCTree.JCSwitch(paramJCExpression, paramList);
/* 261 */     localJCSwitch.pos = this.pos;
/* 262 */     return localJCSwitch;
/*     */   }
/*     */ 
/*     */   public JCTree.JCCase Case(JCTree.JCExpression paramJCExpression, List<JCTree.JCStatement> paramList) {
/* 266 */     JCTree.JCCase localJCCase = new JCTree.JCCase(paramJCExpression, paramList);
/* 267 */     localJCCase.pos = this.pos;
/* 268 */     return localJCCase;
/*     */   }
/*     */ 
/*     */   public JCTree.JCSynchronized Synchronized(JCTree.JCExpression paramJCExpression, JCTree.JCBlock paramJCBlock) {
/* 272 */     JCTree.JCSynchronized localJCSynchronized = new JCTree.JCSynchronized(paramJCExpression, paramJCBlock);
/* 273 */     localJCSynchronized.pos = this.pos;
/* 274 */     return localJCSynchronized;
/*     */   }
/*     */ 
/*     */   public JCTree.JCTry Try(JCTree.JCBlock paramJCBlock1, List<JCTree.JCCatch> paramList, JCTree.JCBlock paramJCBlock2) {
/* 278 */     return Try(List.nil(), paramJCBlock1, paramList, paramJCBlock2);
/*     */   }
/*     */ 
/*     */   public JCTree.JCTry Try(List<JCTree> paramList, JCTree.JCBlock paramJCBlock1, List<JCTree.JCCatch> paramList1, JCTree.JCBlock paramJCBlock2)
/*     */   {
/* 285 */     JCTree.JCTry localJCTry = new JCTree.JCTry(paramList, paramJCBlock1, paramList1, paramJCBlock2);
/* 286 */     localJCTry.pos = this.pos;
/* 287 */     return localJCTry;
/*     */   }
/*     */ 
/*     */   public JCTree.JCCatch Catch(JCTree.JCVariableDecl paramJCVariableDecl, JCTree.JCBlock paramJCBlock) {
/* 291 */     JCTree.JCCatch localJCCatch = new JCTree.JCCatch(paramJCVariableDecl, paramJCBlock);
/* 292 */     localJCCatch.pos = this.pos;
/* 293 */     return localJCCatch;
/*     */   }
/*     */ 
/*     */   public JCTree.JCConditional Conditional(JCTree.JCExpression paramJCExpression1, JCTree.JCExpression paramJCExpression2, JCTree.JCExpression paramJCExpression3)
/*     */   {
/* 300 */     JCTree.JCConditional localJCConditional = new JCTree.JCConditional(paramJCExpression1, paramJCExpression2, paramJCExpression3);
/* 301 */     localJCConditional.pos = this.pos;
/* 302 */     return localJCConditional;
/*     */   }
/*     */ 
/*     */   public JCTree.JCIf If(JCTree.JCExpression paramJCExpression, JCTree.JCStatement paramJCStatement1, JCTree.JCStatement paramJCStatement2) {
/* 306 */     JCTree.JCIf localJCIf = new JCTree.JCIf(paramJCExpression, paramJCStatement1, paramJCStatement2);
/* 307 */     localJCIf.pos = this.pos;
/* 308 */     return localJCIf;
/*     */   }
/*     */ 
/*     */   public JCTree.JCExpressionStatement Exec(JCTree.JCExpression paramJCExpression) {
/* 312 */     JCTree.JCExpressionStatement localJCExpressionStatement = new JCTree.JCExpressionStatement(paramJCExpression);
/* 313 */     localJCExpressionStatement.pos = this.pos;
/* 314 */     return localJCExpressionStatement;
/*     */   }
/*     */ 
/*     */   public JCTree.JCBreak Break(Name paramName) {
/* 318 */     JCTree.JCBreak localJCBreak = new JCTree.JCBreak(paramName, null);
/* 319 */     localJCBreak.pos = this.pos;
/* 320 */     return localJCBreak;
/*     */   }
/*     */ 
/*     */   public JCTree.JCContinue Continue(Name paramName) {
/* 324 */     JCTree.JCContinue localJCContinue = new JCTree.JCContinue(paramName, null);
/* 325 */     localJCContinue.pos = this.pos;
/* 326 */     return localJCContinue;
/*     */   }
/*     */ 
/*     */   public JCTree.JCReturn Return(JCTree.JCExpression paramJCExpression) {
/* 330 */     JCTree.JCReturn localJCReturn = new JCTree.JCReturn(paramJCExpression);
/* 331 */     localJCReturn.pos = this.pos;
/* 332 */     return localJCReturn;
/*     */   }
/*     */ 
/*     */   public JCTree.JCThrow Throw(JCTree.JCExpression paramJCExpression) {
/* 336 */     JCTree.JCThrow localJCThrow = new JCTree.JCThrow(paramJCExpression);
/* 337 */     localJCThrow.pos = this.pos;
/* 338 */     return localJCThrow;
/*     */   }
/*     */ 
/*     */   public JCTree.JCAssert Assert(JCTree.JCExpression paramJCExpression1, JCTree.JCExpression paramJCExpression2) {
/* 342 */     JCTree.JCAssert localJCAssert = new JCTree.JCAssert(paramJCExpression1, paramJCExpression2);
/* 343 */     localJCAssert.pos = this.pos;
/* 344 */     return localJCAssert;
/*     */   }
/*     */ 
/*     */   public JCTree.JCMethodInvocation Apply(List<JCTree.JCExpression> paramList1, JCTree.JCExpression paramJCExpression, List<JCTree.JCExpression> paramList2)
/*     */   {
/* 351 */     JCTree.JCMethodInvocation localJCMethodInvocation = new JCTree.JCMethodInvocation(paramList1, paramJCExpression, paramList2);
/* 352 */     localJCMethodInvocation.pos = this.pos;
/* 353 */     return localJCMethodInvocation;
/*     */   }
/*     */ 
/*     */   public JCTree.JCNewClass NewClass(JCTree.JCExpression paramJCExpression1, List<JCTree.JCExpression> paramList1, JCTree.JCExpression paramJCExpression2, List<JCTree.JCExpression> paramList2, JCTree.JCClassDecl paramJCClassDecl)
/*     */   {
/* 362 */     JCTree.JCNewClass localJCNewClass = new JCTree.JCNewClass(paramJCExpression1, paramList1, paramJCExpression2, paramList2, paramJCClassDecl);
/* 363 */     localJCNewClass.pos = this.pos;
/* 364 */     return localJCNewClass;
/*     */   }
/*     */ 
/*     */   public JCTree.JCNewArray NewArray(JCTree.JCExpression paramJCExpression, List<JCTree.JCExpression> paramList1, List<JCTree.JCExpression> paramList2)
/*     */   {
/* 371 */     JCTree.JCNewArray localJCNewArray = new JCTree.JCNewArray(paramJCExpression, paramList1, paramList2);
/* 372 */     localJCNewArray.pos = this.pos;
/* 373 */     return localJCNewArray;
/*     */   }
/*     */ 
/*     */   public JCTree.JCLambda Lambda(List<JCTree.JCVariableDecl> paramList, JCTree paramJCTree)
/*     */   {
/* 379 */     JCTree.JCLambda localJCLambda = new JCTree.JCLambda(paramList, paramJCTree);
/* 380 */     localJCLambda.pos = this.pos;
/* 381 */     return localJCLambda;
/*     */   }
/*     */ 
/*     */   public JCTree.JCParens Parens(JCTree.JCExpression paramJCExpression) {
/* 385 */     JCTree.JCParens localJCParens = new JCTree.JCParens(paramJCExpression);
/* 386 */     localJCParens.pos = this.pos;
/* 387 */     return localJCParens;
/*     */   }
/*     */ 
/*     */   public JCTree.JCAssign Assign(JCTree.JCExpression paramJCExpression1, JCTree.JCExpression paramJCExpression2) {
/* 391 */     JCTree.JCAssign localJCAssign = new JCTree.JCAssign(paramJCExpression1, paramJCExpression2);
/* 392 */     localJCAssign.pos = this.pos;
/* 393 */     return localJCAssign;
/*     */   }
/*     */ 
/*     */   public JCTree.JCAssignOp Assignop(JCTree.Tag paramTag, JCTree paramJCTree1, JCTree paramJCTree2) {
/* 397 */     JCTree.JCAssignOp localJCAssignOp = new JCTree.JCAssignOp(paramTag, paramJCTree1, paramJCTree2, null);
/* 398 */     localJCAssignOp.pos = this.pos;
/* 399 */     return localJCAssignOp;
/*     */   }
/*     */ 
/*     */   public JCTree.JCUnary Unary(JCTree.Tag paramTag, JCTree.JCExpression paramJCExpression) {
/* 403 */     JCTree.JCUnary localJCUnary = new JCTree.JCUnary(paramTag, paramJCExpression);
/* 404 */     localJCUnary.pos = this.pos;
/* 405 */     return localJCUnary;
/*     */   }
/*     */ 
/*     */   public JCTree.JCBinary Binary(JCTree.Tag paramTag, JCTree.JCExpression paramJCExpression1, JCTree.JCExpression paramJCExpression2) {
/* 409 */     JCTree.JCBinary localJCBinary = new JCTree.JCBinary(paramTag, paramJCExpression1, paramJCExpression2, null);
/* 410 */     localJCBinary.pos = this.pos;
/* 411 */     return localJCBinary;
/*     */   }
/*     */ 
/*     */   public JCTree.JCTypeCast TypeCast(JCTree paramJCTree, JCTree.JCExpression paramJCExpression) {
/* 415 */     JCTree.JCTypeCast localJCTypeCast = new JCTree.JCTypeCast(paramJCTree, paramJCExpression);
/* 416 */     localJCTypeCast.pos = this.pos;
/* 417 */     return localJCTypeCast;
/*     */   }
/*     */ 
/*     */   public JCTree.JCInstanceOf TypeTest(JCTree.JCExpression paramJCExpression, JCTree paramJCTree) {
/* 421 */     JCTree.JCInstanceOf localJCInstanceOf = new JCTree.JCInstanceOf(paramJCExpression, paramJCTree);
/* 422 */     localJCInstanceOf.pos = this.pos;
/* 423 */     return localJCInstanceOf;
/*     */   }
/*     */ 
/*     */   public JCTree.JCArrayAccess Indexed(JCTree.JCExpression paramJCExpression1, JCTree.JCExpression paramJCExpression2) {
/* 427 */     JCTree.JCArrayAccess localJCArrayAccess = new JCTree.JCArrayAccess(paramJCExpression1, paramJCExpression2);
/* 428 */     localJCArrayAccess.pos = this.pos;
/* 429 */     return localJCArrayAccess;
/*     */   }
/*     */ 
/*     */   public JCTree.JCFieldAccess Select(JCTree.JCExpression paramJCExpression, Name paramName) {
/* 433 */     JCTree.JCFieldAccess localJCFieldAccess = new JCTree.JCFieldAccess(paramJCExpression, paramName, null);
/* 434 */     localJCFieldAccess.pos = this.pos;
/* 435 */     return localJCFieldAccess;
/*     */   }
/*     */ 
/*     */   public JCTree.JCMemberReference Reference(MemberReferenceTree.ReferenceMode paramReferenceMode, Name paramName, JCTree.JCExpression paramJCExpression, List<JCTree.JCExpression> paramList)
/*     */   {
/* 440 */     JCTree.JCMemberReference localJCMemberReference = new JCTree.JCMemberReference(paramReferenceMode, paramName, paramJCExpression, paramList);
/* 441 */     localJCMemberReference.pos = this.pos;
/* 442 */     return localJCMemberReference;
/*     */   }
/*     */ 
/*     */   public JCTree.JCIdent Ident(Name paramName) {
/* 446 */     JCTree.JCIdent localJCIdent = new JCTree.JCIdent(paramName, null);
/* 447 */     localJCIdent.pos = this.pos;
/* 448 */     return localJCIdent;
/*     */   }
/*     */ 
/*     */   public JCTree.JCLiteral Literal(TypeTag paramTypeTag, Object paramObject) {
/* 452 */     JCTree.JCLiteral localJCLiteral = new JCTree.JCLiteral(paramTypeTag, paramObject);
/* 453 */     localJCLiteral.pos = this.pos;
/* 454 */     return localJCLiteral;
/*     */   }
/*     */ 
/*     */   public JCTree.JCPrimitiveTypeTree TypeIdent(TypeTag paramTypeTag) {
/* 458 */     JCTree.JCPrimitiveTypeTree localJCPrimitiveTypeTree = new JCTree.JCPrimitiveTypeTree(paramTypeTag);
/* 459 */     localJCPrimitiveTypeTree.pos = this.pos;
/* 460 */     return localJCPrimitiveTypeTree;
/*     */   }
/*     */ 
/*     */   public JCTree.JCArrayTypeTree TypeArray(JCTree.JCExpression paramJCExpression) {
/* 464 */     JCTree.JCArrayTypeTree localJCArrayTypeTree = new JCTree.JCArrayTypeTree(paramJCExpression);
/* 465 */     localJCArrayTypeTree.pos = this.pos;
/* 466 */     return localJCArrayTypeTree;
/*     */   }
/*     */ 
/*     */   public JCTree.JCTypeApply TypeApply(JCTree.JCExpression paramJCExpression, List<JCTree.JCExpression> paramList) {
/* 470 */     JCTree.JCTypeApply localJCTypeApply = new JCTree.JCTypeApply(paramJCExpression, paramList);
/* 471 */     localJCTypeApply.pos = this.pos;
/* 472 */     return localJCTypeApply;
/*     */   }
/*     */ 
/*     */   public JCTree.JCTypeUnion TypeUnion(List<JCTree.JCExpression> paramList) {
/* 476 */     JCTree.JCTypeUnion localJCTypeUnion = new JCTree.JCTypeUnion(paramList);
/* 477 */     localJCTypeUnion.pos = this.pos;
/* 478 */     return localJCTypeUnion;
/*     */   }
/*     */ 
/*     */   public JCTree.JCTypeIntersection TypeIntersection(List<JCTree.JCExpression> paramList) {
/* 482 */     JCTree.JCTypeIntersection localJCTypeIntersection = new JCTree.JCTypeIntersection(paramList);
/* 483 */     localJCTypeIntersection.pos = this.pos;
/* 484 */     return localJCTypeIntersection;
/*     */   }
/*     */ 
/*     */   public JCTree.JCTypeParameter TypeParameter(Name paramName, List<JCTree.JCExpression> paramList) {
/* 488 */     return TypeParameter(paramName, paramList, List.nil());
/*     */   }
/*     */ 
/*     */   public JCTree.JCTypeParameter TypeParameter(Name paramName, List<JCTree.JCExpression> paramList, List<JCTree.JCAnnotation> paramList1) {
/* 492 */     JCTree.JCTypeParameter localJCTypeParameter = new JCTree.JCTypeParameter(paramName, paramList, paramList1);
/* 493 */     localJCTypeParameter.pos = this.pos;
/* 494 */     return localJCTypeParameter;
/*     */   }
/*     */ 
/*     */   public JCTree.JCWildcard Wildcard(JCTree.TypeBoundKind paramTypeBoundKind, JCTree paramJCTree) {
/* 498 */     JCTree.JCWildcard localJCWildcard = new JCTree.JCWildcard(paramTypeBoundKind, paramJCTree);
/* 499 */     localJCWildcard.pos = this.pos;
/* 500 */     return localJCWildcard;
/*     */   }
/*     */ 
/*     */   public JCTree.TypeBoundKind TypeBoundKind(BoundKind paramBoundKind) {
/* 504 */     JCTree.TypeBoundKind localTypeBoundKind = new JCTree.TypeBoundKind(paramBoundKind);
/* 505 */     localTypeBoundKind.pos = this.pos;
/* 506 */     return localTypeBoundKind;
/*     */   }
/*     */ 
/*     */   public JCTree.JCAnnotation Annotation(JCTree paramJCTree, List<JCTree.JCExpression> paramList) {
/* 510 */     JCTree.JCAnnotation localJCAnnotation = new JCTree.JCAnnotation(JCTree.Tag.ANNOTATION, paramJCTree, paramList);
/* 511 */     localJCAnnotation.pos = this.pos;
/* 512 */     return localJCAnnotation;
/*     */   }
/*     */ 
/*     */   public JCTree.JCAnnotation TypeAnnotation(JCTree paramJCTree, List<JCTree.JCExpression> paramList) {
/* 516 */     JCTree.JCAnnotation localJCAnnotation = new JCTree.JCAnnotation(JCTree.Tag.TYPE_ANNOTATION, paramJCTree, paramList);
/* 517 */     localJCAnnotation.pos = this.pos;
/* 518 */     return localJCAnnotation;
/*     */   }
/*     */ 
/*     */   public JCTree.JCModifiers Modifiers(long paramLong, List<JCTree.JCAnnotation> paramList) {
/* 522 */     JCTree.JCModifiers localJCModifiers = new JCTree.JCModifiers(paramLong, paramList);
/* 523 */     int i = (paramLong & 0x2DFF) == 0L ? 1 : 0;
/* 524 */     localJCModifiers.pos = ((i != 0) && (paramList.isEmpty()) ? -1 : this.pos);
/* 525 */     return localJCModifiers;
/*     */   }
/*     */ 
/*     */   public JCTree.JCModifiers Modifiers(long paramLong) {
/* 529 */     return Modifiers(paramLong, List.nil());
/*     */   }
/*     */ 
/*     */   public JCTree.JCAnnotatedType AnnotatedType(List<JCTree.JCAnnotation> paramList, JCTree.JCExpression paramJCExpression) {
/* 533 */     JCTree.JCAnnotatedType localJCAnnotatedType = new JCTree.JCAnnotatedType(paramList, paramJCExpression);
/* 534 */     localJCAnnotatedType.pos = this.pos;
/* 535 */     return localJCAnnotatedType;
/*     */   }
/*     */ 
/*     */   public JCTree.JCErroneous Erroneous() {
/* 539 */     return Erroneous(List.nil());
/*     */   }
/*     */ 
/*     */   public JCTree.JCErroneous Erroneous(List<? extends JCTree> paramList) {
/* 543 */     JCTree.JCErroneous localJCErroneous = new JCTree.JCErroneous(paramList);
/* 544 */     localJCErroneous.pos = this.pos;
/* 545 */     return localJCErroneous;
/*     */   }
/*     */ 
/*     */   public JCTree.LetExpr LetExpr(List<JCTree.JCVariableDecl> paramList, JCTree paramJCTree) {
/* 549 */     JCTree.LetExpr localLetExpr = new JCTree.LetExpr(paramList, paramJCTree);
/* 550 */     localLetExpr.pos = this.pos;
/* 551 */     return localLetExpr;
/*     */   }
/*     */ 
/*     */   public JCTree.JCClassDecl AnonymousClassDef(JCTree.JCModifiers paramJCModifiers, List<JCTree> paramList)
/*     */   {
/* 561 */     return ClassDef(paramJCModifiers, this.names.empty, 
/* 563 */       List.nil(), null, 
/* 565 */       List.nil(), paramList);
/*     */   }
/*     */ 
/*     */   public JCTree.LetExpr LetExpr(JCTree.JCVariableDecl paramJCVariableDecl, JCTree paramJCTree)
/*     */   {
/* 570 */     JCTree.LetExpr localLetExpr = new JCTree.LetExpr(List.of(paramJCVariableDecl), paramJCTree);
/* 571 */     localLetExpr.pos = this.pos;
/* 572 */     return localLetExpr;
/*     */   }
/*     */ 
/*     */   public JCTree.JCIdent Ident(Symbol paramSymbol)
/*     */   {
/* 582 */     return (JCTree.JCIdent)new JCTree.JCIdent(paramSymbol.name != this.names.empty ? paramSymbol.name : paramSymbol
/* 580 */       .flatName(), paramSymbol)
/* 581 */       .setPos(this.pos)
/* 582 */       .setType(paramSymbol.type);
/*     */   }
/*     */ 
/*     */   public JCTree.JCExpression Select(JCTree.JCExpression paramJCExpression, Symbol paramSymbol)
/*     */   {
/* 589 */     return new JCTree.JCFieldAccess(paramJCExpression, paramSymbol.name, paramSymbol).setPos(this.pos).setType(paramSymbol.type);
/*     */   }
/*     */ 
/*     */   public JCTree.JCExpression QualIdent(Symbol paramSymbol)
/*     */   {
/* 598 */     return isUnqualifiable(paramSymbol) ? 
/* 597 */       Ident(paramSymbol) : 
/* 598 */       Select(QualIdent(paramSymbol.owner), 
/* 598 */       paramSymbol);
/*     */   }
/*     */ 
/*     */   public JCTree.JCExpression Ident(JCTree.JCVariableDecl paramJCVariableDecl)
/*     */   {
/* 605 */     return Ident(paramJCVariableDecl.sym);
/*     */   }
/*     */ 
/*     */   public List<JCTree.JCExpression> Idents(List<JCTree.JCVariableDecl> paramList)
/*     */   {
/* 612 */     ListBuffer localListBuffer = new ListBuffer();
/* 613 */     for (Object localObject = paramList; ((List)localObject).nonEmpty(); localObject = ((List)localObject).tail)
/* 614 */       localListBuffer.append(Ident((JCTree.JCVariableDecl)((List)localObject).head));
/* 615 */     return localListBuffer.toList();
/*     */   }
/*     */ 
/*     */   public JCTree.JCExpression This(Type paramType)
/*     */   {
/* 621 */     return Ident(new Symbol.VarSymbol(16L, this.names._this, paramType, paramType.tsym));
/*     */   }
/*     */ 
/*     */   public JCTree.JCExpression ClassLiteral(Symbol.ClassSymbol paramClassSymbol)
/*     */   {
/* 627 */     return ClassLiteral(paramClassSymbol.type);
/*     */   }
/*     */ 
/*     */   public JCTree.JCExpression ClassLiteral(Type paramType)
/*     */   {
/* 633 */     Symbol.VarSymbol localVarSymbol = new Symbol.VarSymbol(25L, this.names._class, paramType, paramType.tsym);
/*     */ 
/* 637 */     return Select(Type(paramType), localVarSymbol);
/*     */   }
/*     */ 
/*     */   public JCTree.JCIdent Super(Type paramType, Symbol.TypeSymbol paramTypeSymbol)
/*     */   {
/* 643 */     return Ident(new Symbol.VarSymbol(16L, this.names._super, paramType, paramTypeSymbol));
/*     */   }
/*     */ 
/*     */   public JCTree.JCMethodInvocation App(JCTree.JCExpression paramJCExpression, List<JCTree.JCExpression> paramList)
/*     */   {
/* 651 */     return Apply(null, paramJCExpression, paramList).setType(paramJCExpression.type.getReturnType());
/*     */   }
/*     */ 
/*     */   public JCTree.JCMethodInvocation App(JCTree.JCExpression paramJCExpression)
/*     */   {
/* 658 */     return Apply(null, paramJCExpression, List.nil()).setType(paramJCExpression.type.getReturnType());
/*     */   }
/*     */ 
/*     */   public JCTree.JCExpression Create(Symbol paramSymbol, List<JCTree.JCExpression> paramList)
/*     */   {
/* 664 */     Type localType = paramSymbol.owner.erasure(this.types);
/* 665 */     JCTree.JCNewClass localJCNewClass = NewClass(null, null, Type(localType), paramList, null);
/* 666 */     localJCNewClass.constructor = paramSymbol;
/* 667 */     localJCNewClass.setType(localType);
/* 668 */     return localJCNewClass;
/*     */   }
/*     */ 
/*     */   public JCTree.JCExpression Type(Type paramType)
/*     */   {
/* 674 */     if (paramType == null) return null;
/*     */     Object localObject1;
/*     */     Object localObject2;
/* 676 */     switch (1.$SwitchMap$com$sun$tools$javac$code$TypeTag[paramType.getTag().ordinal()]) { case 1:
/*     */     case 2:
/*     */     case 3:
/*     */     case 4:
/*     */     case 5:
/*     */     case 6:
/*     */     case 7:
/*     */     case 8:
/*     */     case 9:
/* 679 */       localObject1 = TypeIdent(paramType.getTag());
/* 680 */       break;
/*     */     case 10:
/* 682 */       localObject1 = Ident(paramType.tsym);
/* 683 */       break;
/*     */     case 11:
/* 685 */       localObject2 = (Type.WildcardType)paramType;
/* 686 */       localObject1 = Wildcard(TypeBoundKind(((Type.WildcardType)localObject2).kind), Type(((Type.WildcardType)localObject2).type));
/* 687 */       break;
/*     */     case 12:
/* 690 */       localObject2 = paramType.getEnclosingType();
/*     */ 
/* 693 */       JCTree.JCExpression localJCExpression = (((Type)localObject2).hasTag(TypeTag.CLASS)) && (paramType.tsym.owner.kind == 2) ? 
/* 692 */         Select(Type((Type)localObject2), 
/* 692 */         paramType.tsym) : 
/* 693 */         QualIdent(paramType.tsym);
/*     */ 
/* 696 */       localObject1 = paramType.getTypeArguments().isEmpty() ? localJCExpression : 
/* 696 */         TypeApply(localJCExpression, 
/* 696 */         Types(paramType
/* 696 */         .getTypeArguments()));
/* 697 */       break;
/*     */     case 13:
/* 699 */       localObject1 = TypeArray(Type(this.types.elemtype(paramType)));
/* 700 */       break;
/*     */     case 14:
/* 702 */       localObject1 = TypeIdent(TypeTag.ERROR);
/* 703 */       break;
/*     */     default:
/* 705 */       throw new AssertionError("unexpected type: " + paramType);
/*     */     }
/* 707 */     return ((JCTree.JCExpression)localObject1).setType(paramType);
/*     */   }
/*     */ 
/*     */   public List<JCTree.JCExpression> Types(List<Type> paramList)
/*     */   {
/* 713 */     ListBuffer localListBuffer = new ListBuffer();
/* 714 */     for (Object localObject = paramList; ((List)localObject).nonEmpty(); localObject = ((List)localObject).tail)
/* 715 */       localListBuffer.append(Type((Type)((List)localObject).head));
/* 716 */     return localListBuffer.toList();
/*     */   }
/*     */ 
/*     */   public JCTree.JCVariableDecl VarDef(Symbol.VarSymbol paramVarSymbol, JCTree.JCExpression paramJCExpression)
/*     */   {
/* 729 */     return (JCTree.JCVariableDecl)new JCTree.JCVariableDecl(
/* 725 */       Modifiers(paramVarSymbol
/* 725 */       .flags(), Annotations(paramVarSymbol.getRawAttributes())), paramVarSymbol.name, 
/* 727 */       Type(paramVarSymbol.type), 
/* 727 */       paramJCExpression, paramVarSymbol)
/* 729 */       .setPos(this.pos)
/* 729 */       .setType(paramVarSymbol.type);
/*     */   }
/*     */ 
/*     */   public List<JCTree.JCAnnotation> Annotations(List<Attribute.Compound> paramList)
/*     */   {
/* 735 */     if (paramList == null) return List.nil();
/* 736 */     ListBuffer localListBuffer = new ListBuffer();
/* 737 */     for (Object localObject = paramList; ((List)localObject).nonEmpty(); localObject = ((List)localObject).tail) {
/* 738 */       Attribute localAttribute = (Attribute)((List)localObject).head;
/* 739 */       localListBuffer.append(Annotation(localAttribute));
/*     */     }
/* 741 */     return localListBuffer.toList();
/*     */   }
/*     */ 
/*     */   public JCTree.JCLiteral Literal(Object paramObject) {
/* 745 */     JCTree.JCLiteral localJCLiteral = null;
/* 746 */     if ((paramObject instanceof String))
/*     */     {
/* 748 */       localJCLiteral = Literal(TypeTag.CLASS, paramObject)
/* 748 */         .setType(this.syms.stringType
/* 748 */         .constType(paramObject));
/*     */     }
/* 749 */     else if ((paramObject instanceof Integer))
/*     */     {
/* 751 */       localJCLiteral = Literal(TypeTag.INT, paramObject)
/* 751 */         .setType(this.syms.intType
/* 751 */         .constType(paramObject));
/*     */     }
/* 752 */     else if ((paramObject instanceof Long))
/*     */     {
/* 754 */       localJCLiteral = Literal(TypeTag.LONG, paramObject)
/* 754 */         .setType(this.syms.longType
/* 754 */         .constType(paramObject));
/*     */     }
/* 755 */     else if ((paramObject instanceof Byte))
/*     */     {
/* 757 */       localJCLiteral = Literal(TypeTag.BYTE, paramObject)
/* 757 */         .setType(this.syms.byteType
/* 757 */         .constType(paramObject));
/*     */     }
/*     */     else
/*     */     {
/*     */       int i;
/* 758 */       if ((paramObject instanceof Character)) {
/* 759 */         i = ((Character)paramObject).toString().charAt(0);
/*     */ 
/* 761 */         localJCLiteral = Literal(TypeTag.CHAR, paramObject)
/* 761 */           .setType(this.syms.charType
/* 761 */           .constType(Integer.valueOf(i)));
/*     */       }
/* 762 */       else if ((paramObject instanceof Double))
/*     */       {
/* 764 */         localJCLiteral = Literal(TypeTag.DOUBLE, paramObject)
/* 764 */           .setType(this.syms.doubleType
/* 764 */           .constType(paramObject));
/*     */       }
/* 765 */       else if ((paramObject instanceof Float))
/*     */       {
/* 767 */         localJCLiteral = Literal(TypeTag.FLOAT, paramObject)
/* 767 */           .setType(this.syms.floatType
/* 767 */           .constType(paramObject));
/*     */       }
/* 768 */       else if ((paramObject instanceof Short))
/*     */       {
/* 770 */         localJCLiteral = Literal(TypeTag.SHORT, paramObject)
/* 770 */           .setType(this.syms.shortType
/* 770 */           .constType(paramObject));
/*     */       }
/* 771 */       else if ((paramObject instanceof Boolean)) {
/* 772 */         i = ((Boolean)paramObject).booleanValue() ? 1 : 0;
/*     */ 
/* 774 */         localJCLiteral = Literal(TypeTag.BOOLEAN, Integer.valueOf(i))
/* 774 */           .setType(this.syms.booleanType
/* 774 */           .constType(Integer.valueOf(i)));
/*     */       }
/*     */       else {
/* 776 */         throw new AssertionError(paramObject);
/*     */       }
/*     */     }
/* 778 */     return localJCLiteral;
/*     */   }
/*     */ 
/*     */   public JCTree.JCAnnotation Annotation(Attribute paramAttribute)
/*     */   {
/* 843 */     return this.annotationBuilder.translate((Attribute.Compound)paramAttribute);
/*     */   }
/*     */ 
/*     */   public JCTree.JCAnnotation TypeAnnotation(Attribute paramAttribute) {
/* 847 */     return this.annotationBuilder.translate((Attribute.TypeCompound)paramAttribute);
/*     */   }
/*     */ 
/*     */   public JCTree.JCMethodDecl MethodDef(Symbol.MethodSymbol paramMethodSymbol, JCTree.JCBlock paramJCBlock)
/*     */   {
/* 853 */     return MethodDef(paramMethodSymbol, paramMethodSymbol.type, paramJCBlock);
/*     */   }
/*     */ 
/*     */   public JCTree.JCMethodDecl MethodDef(Symbol.MethodSymbol paramMethodSymbol, Type paramType, JCTree.JCBlock paramJCBlock)
/*     */   {
/* 871 */     return (JCTree.JCMethodDecl)new JCTree.JCMethodDecl(
/* 862 */       Modifiers(paramMethodSymbol
/* 862 */       .flags(), Annotations(paramMethodSymbol.getRawAttributes())), paramMethodSymbol.name, 
/* 864 */       Type(paramType
/* 864 */       .getReturnType()), 
/* 865 */       TypeParams(paramType
/* 865 */       .getTypeArguments()), null, 
/* 867 */       Params(paramType
/* 867 */       .getParameterTypes(), paramMethodSymbol), 
/* 868 */       Types(paramType
/* 868 */       .getThrownTypes()), paramJCBlock, null, paramMethodSymbol)
/* 871 */       .setPos(this.pos)
/* 871 */       .setType(paramType);
/*     */   }
/*     */ 
/*     */   public JCTree.JCTypeParameter TypeParam(Name paramName, Type.TypeVar paramTypeVar)
/*     */   {
/* 878 */     return (JCTree.JCTypeParameter)TypeParameter(paramName, 
/* 878 */       Types(this.types
/* 878 */       .getBounds(paramTypeVar)))
/* 878 */       .setPos(this.pos).setType(paramTypeVar);
/*     */   }
/*     */ 
/*     */   public List<JCTree.JCTypeParameter> TypeParams(List<Type> paramList)
/*     */   {
/* 884 */     ListBuffer localListBuffer = new ListBuffer();
/* 885 */     for (Object localObject = paramList; ((List)localObject).nonEmpty(); localObject = ((List)localObject).tail)
/* 886 */       localListBuffer.append(TypeParam(((Type)((List)localObject).head).tsym.name, (Type.TypeVar)((List)localObject).head));
/* 887 */     return localListBuffer.toList();
/*     */   }
/*     */ 
/*     */   public JCTree.JCVariableDecl Param(Name paramName, Type paramType, Symbol paramSymbol)
/*     */   {
/* 893 */     return VarDef(new Symbol.VarSymbol(8589934592L, paramName, paramType, paramSymbol), null);
/*     */   }
/*     */ 
/*     */   public List<JCTree.JCVariableDecl> Params(List<Type> paramList, Symbol paramSymbol)
/*     */   {
/* 900 */     ListBuffer localListBuffer = new ListBuffer();
/* 901 */     Object localObject1 = paramSymbol.kind == 16 ? (Symbol.MethodSymbol)paramSymbol : null;
/*     */     Iterator localIterator;
/*     */     Object localObject2;
/* 902 */     if ((localObject1 != null) && (localObject1.params != null) && (paramList.length() == localObject1.params.length())) {
/* 903 */       for (localIterator = ((Symbol.MethodSymbol)paramSymbol).params.iterator(); localIterator.hasNext(); ) { localObject2 = (Symbol.VarSymbol)localIterator.next();
/* 904 */         localListBuffer.append(VarDef((Symbol.VarSymbol)localObject2, null)); }
/*     */     } else {
/* 906 */       int i = 0;
/* 907 */       for (localObject2 = paramList; ((List)localObject2).nonEmpty(); localObject2 = ((List)localObject2).tail)
/* 908 */         localListBuffer.append(Param(paramName(i++), (Type)((List)localObject2).head, paramSymbol));
/*     */     }
/* 910 */     return localListBuffer.toList();
/*     */   }
/*     */ 
/*     */   public JCTree.JCStatement Call(JCTree.JCExpression paramJCExpression)
/*     */   {
/* 917 */     return paramJCExpression.type.hasTag(TypeTag.VOID) ? Exec(paramJCExpression) : Return(paramJCExpression);
/*     */   }
/*     */ 
/*     */   public JCTree.JCStatement Assignment(Symbol paramSymbol, JCTree.JCExpression paramJCExpression)
/*     */   {
/* 923 */     return Exec(Assign(Ident(paramSymbol), paramJCExpression).setType(paramSymbol.type));
/*     */   }
/*     */ 
/*     */   public JCTree.JCArrayAccess Indexed(Symbol paramSymbol, JCTree.JCExpression paramJCExpression)
/*     */   {
/* 929 */     JCTree.JCArrayAccess localJCArrayAccess = new JCTree.JCArrayAccess(QualIdent(paramSymbol), paramJCExpression);
/* 930 */     localJCArrayAccess.type = ((Type.ArrayType)paramSymbol.type).elemtype;
/* 931 */     return localJCArrayAccess;
/*     */   }
/*     */ 
/*     */   public JCTree.JCTypeCast TypeCast(Type paramType, JCTree.JCExpression paramJCExpression)
/*     */   {
/* 937 */     return (JCTree.JCTypeCast)TypeCast(Type(paramType), paramJCExpression).setType(paramType);
/*     */   }
/*     */ 
/*     */   boolean isUnqualifiable(Symbol paramSymbol)
/*     */   {
/* 947 */     if ((paramSymbol.name == this.names.empty) || (paramSymbol.owner == null) || (paramSymbol.owner == this.syms.rootPackage) || (paramSymbol.owner.kind == 16) || (paramSymbol.owner.kind == 4))
/*     */     {
/* 951 */       return true;
/* 952 */     }if ((paramSymbol.kind == 2) && (this.toplevel != null))
/*     */     {
/* 954 */       Scope.Entry localEntry = this.toplevel.namedImportScope.lookup(paramSymbol.name);
/* 955 */       if (localEntry.scope != null) {
/* 956 */         if (localEntry.sym == paramSymbol);
/* 958 */         return localEntry
/* 958 */           .next().scope == null;
/*     */       }
/* 960 */       localEntry = this.toplevel.packge.members().lookup(paramSymbol.name);
/* 961 */       if (localEntry.scope != null) {
/* 962 */         if (localEntry.sym == paramSymbol);
/* 964 */         return localEntry
/* 964 */           .next().scope == null;
/*     */       }
/* 966 */       localEntry = this.toplevel.starImportScope.lookup(paramSymbol.name);
/* 967 */       if (localEntry.scope != null) {
/* 968 */         if (localEntry.sym == paramSymbol);
/* 970 */         return localEntry
/* 970 */           .next().scope == null;
/*     */       }
/*     */     }
/* 973 */     return false;
/*     */   }
/*     */ 
/*     */   public Name paramName(int paramInt)
/*     */   {
/* 978 */     return this.names.fromString("x" + paramInt);
/*     */   }
/*     */ 
/*     */   public Name typaramName(int paramInt) {
/* 982 */     return this.names.fromString("A" + paramInt);
/*     */   }
/*     */ 
/*     */   class AnnotationBuilder
/*     */     implements Attribute.Visitor
/*     */   {
/* 782 */     JCTree.JCExpression result = null;
/*     */ 
/*     */     AnnotationBuilder() {  } 
/* 784 */     public void visitConstant(Attribute.Constant paramConstant) { this.result = TreeMaker.this.Literal(paramConstant.type.getTag(), paramConstant.value); }
/*     */ 
/*     */     public void visitClass(Attribute.Class paramClass) {
/* 787 */       this.result = TreeMaker.this.ClassLiteral(paramClass.classType).setType(TreeMaker.this.syms.classType);
/*     */     }
/*     */     public void visitEnum(Attribute.Enum paramEnum) {
/* 790 */       this.result = TreeMaker.this.QualIdent(paramEnum.value);
/*     */     }
/*     */     public void visitError(Attribute.Error paramError) {
/* 793 */       this.result = TreeMaker.this.Erroneous();
/*     */     }
/*     */     public void visitCompound(Attribute.Compound paramCompound) {
/* 796 */       if ((paramCompound instanceof Attribute.TypeCompound))
/* 797 */         this.result = visitTypeCompoundInternal((Attribute.TypeCompound)paramCompound);
/*     */       else
/* 799 */         this.result = visitCompoundInternal(paramCompound);
/*     */     }
/*     */ 
/*     */     public JCTree.JCAnnotation visitCompoundInternal(Attribute.Compound paramCompound) {
/* 803 */       ListBuffer localListBuffer = new ListBuffer();
/* 804 */       for (List localList = paramCompound.values; localList.nonEmpty(); localList = localList.tail) {
/* 805 */         Pair localPair = (Pair)localList.head;
/* 806 */         JCTree.JCExpression localJCExpression = translate((Attribute)localPair.snd);
/* 807 */         localListBuffer.append(TreeMaker.this.Assign(TreeMaker.this.Ident((Symbol)localPair.fst), localJCExpression).setType(localJCExpression.type));
/*     */       }
/* 809 */       return TreeMaker.this.Annotation(TreeMaker.this.Type(paramCompound.type), localListBuffer.toList());
/*     */     }
/*     */     public JCTree.JCAnnotation visitTypeCompoundInternal(Attribute.TypeCompound paramTypeCompound) {
/* 812 */       ListBuffer localListBuffer = new ListBuffer();
/* 813 */       for (List localList = paramTypeCompound.values; localList.nonEmpty(); localList = localList.tail) {
/* 814 */         Pair localPair = (Pair)localList.head;
/* 815 */         JCTree.JCExpression localJCExpression = translate((Attribute)localPair.snd);
/* 816 */         localListBuffer.append(TreeMaker.this.Assign(TreeMaker.this.Ident((Symbol)localPair.fst), localJCExpression).setType(localJCExpression.type));
/*     */       }
/* 818 */       return TreeMaker.this.TypeAnnotation(TreeMaker.this.Type(paramTypeCompound.type), localListBuffer.toList());
/*     */     }
/*     */     public void visitArray(Attribute.Array paramArray) {
/* 821 */       ListBuffer localListBuffer = new ListBuffer();
/* 822 */       for (int i = 0; i < paramArray.values.length; i++)
/* 823 */         localListBuffer.append(translate(paramArray.values[i]));
/* 824 */       this.result = TreeMaker.this.NewArray(null, List.nil(), localListBuffer.toList()).setType(paramArray.type);
/*     */     }
/*     */     JCTree.JCExpression translate(Attribute paramAttribute) {
/* 827 */       paramAttribute.accept(this);
/* 828 */       return this.result;
/*     */     }
/*     */     JCTree.JCAnnotation translate(Attribute.Compound paramCompound) {
/* 831 */       return visitCompoundInternal(paramCompound);
/*     */     }
/*     */     JCTree.JCAnnotation translate(Attribute.TypeCompound paramTypeCompound) {
/* 834 */       return visitTypeCompoundInternal(paramTypeCompound);
/*     */     }
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.javac.tree.TreeMaker
 * JD-Core Version:    0.6.2
 */