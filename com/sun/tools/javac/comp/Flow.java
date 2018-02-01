/*      */ package com.sun.tools.javac.comp;
/*      */ 
/*      */ import com.sun.source.tree.LambdaExpressionTree.BodyKind;
/*      */ import com.sun.tools.javac.code.Lint;
/*      */ import com.sun.tools.javac.code.Lint.LintCategory;
/*      */ import com.sun.tools.javac.code.Scope;
/*      */ import com.sun.tools.javac.code.Source;
/*      */ import com.sun.tools.javac.code.Symbol;
/*      */ import com.sun.tools.javac.code.Symbol.ClassSymbol;
/*      */ import com.sun.tools.javac.code.Symbol.MethodSymbol;
/*      */ import com.sun.tools.javac.code.Symbol.VarSymbol;
/*      */ import com.sun.tools.javac.code.Symtab;
/*      */ import com.sun.tools.javac.code.Type;
/*      */ import com.sun.tools.javac.code.TypeTag;
/*      */ import com.sun.tools.javac.code.Types;
/*      */ import com.sun.tools.javac.tree.JCTree;
/*      */ import com.sun.tools.javac.tree.JCTree.JCAnnotatedType;
/*      */ import com.sun.tools.javac.tree.JCTree.JCAssert;
/*      */ import com.sun.tools.javac.tree.JCTree.JCAssign;
/*      */ import com.sun.tools.javac.tree.JCTree.JCAssignOp;
/*      */ import com.sun.tools.javac.tree.JCTree.JCBinary;
/*      */ import com.sun.tools.javac.tree.JCTree.JCBlock;
/*      */ import com.sun.tools.javac.tree.JCTree.JCBreak;
/*      */ import com.sun.tools.javac.tree.JCTree.JCCase;
/*      */ import com.sun.tools.javac.tree.JCTree.JCCatch;
/*      */ import com.sun.tools.javac.tree.JCTree.JCClassDecl;
/*      */ import com.sun.tools.javac.tree.JCTree.JCCompilationUnit;
/*      */ import com.sun.tools.javac.tree.JCTree.JCConditional;
/*      */ import com.sun.tools.javac.tree.JCTree.JCContinue;
/*      */ import com.sun.tools.javac.tree.JCTree.JCDoWhileLoop;
/*      */ import com.sun.tools.javac.tree.JCTree.JCEnhancedForLoop;
/*      */ import com.sun.tools.javac.tree.JCTree.JCExpression;
/*      */ import com.sun.tools.javac.tree.JCTree.JCFieldAccess;
/*      */ import com.sun.tools.javac.tree.JCTree.JCForLoop;
/*      */ import com.sun.tools.javac.tree.JCTree.JCIdent;
/*      */ import com.sun.tools.javac.tree.JCTree.JCIf;
/*      */ import com.sun.tools.javac.tree.JCTree.JCLabeledStatement;
/*      */ import com.sun.tools.javac.tree.JCTree.JCLambda;
/*      */ import com.sun.tools.javac.tree.JCTree.JCMethodDecl;
/*      */ import com.sun.tools.javac.tree.JCTree.JCMethodInvocation;
/*      */ import com.sun.tools.javac.tree.JCTree.JCModifiers;
/*      */ import com.sun.tools.javac.tree.JCTree.JCNewArray;
/*      */ import com.sun.tools.javac.tree.JCTree.JCNewClass;
/*      */ import com.sun.tools.javac.tree.JCTree.JCReturn;
/*      */ import com.sun.tools.javac.tree.JCTree.JCStatement;
/*      */ import com.sun.tools.javac.tree.JCTree.JCSwitch;
/*      */ import com.sun.tools.javac.tree.JCTree.JCThrow;
/*      */ import com.sun.tools.javac.tree.JCTree.JCTry;
/*      */ import com.sun.tools.javac.tree.JCTree.JCTypeUnion;
/*      */ import com.sun.tools.javac.tree.JCTree.JCUnary;
/*      */ import com.sun.tools.javac.tree.JCTree.JCVariableDecl;
/*      */ import com.sun.tools.javac.tree.JCTree.JCWhileLoop;
/*      */ import com.sun.tools.javac.tree.JCTree.Tag;
/*      */ import com.sun.tools.javac.tree.TreeInfo;
/*      */ import com.sun.tools.javac.tree.TreeMaker;
/*      */ import com.sun.tools.javac.tree.TreeScanner;
/*      */ import com.sun.tools.javac.util.ArrayUtils;
/*      */ import com.sun.tools.javac.util.Assert;
/*      */ import com.sun.tools.javac.util.Bits;
/*      */ import com.sun.tools.javac.util.Context;
/*      */ import com.sun.tools.javac.util.Context.Key;
/*      */ import com.sun.tools.javac.util.JCDiagnostic.DiagnosticPosition;
/*      */ import com.sun.tools.javac.util.JCDiagnostic.Factory;
/*      */ import com.sun.tools.javac.util.List;
/*      */ import com.sun.tools.javac.util.ListBuffer;
/*      */ import com.sun.tools.javac.util.Log;
/*      */ import com.sun.tools.javac.util.Log.DiscardDiagnosticHandler;
/*      */ import com.sun.tools.javac.util.Names;
/*      */ import java.util.HashMap;
/*      */ import java.util.Iterator;
/*      */ 
/*      */ public class Flow
/*      */ {
/*  184 */   protected static final Context.Key<Flow> flowKey = new Context.Key();
/*      */   private final Names names;
/*      */   private final Log log;
/*      */   private final Symtab syms;
/*      */   private final Types types;
/*      */   private final Check chk;
/*      */   private TreeMaker make;
/*      */   private final Resolve rs;
/*      */   private final JCDiagnostic.Factory diags;
/*      */   private Env<AttrContext> attrEnv;
/*      */   private Lint lint;
/*      */   private final boolean allowImprovedRethrowAnalysis;
/*      */   private final boolean allowImprovedCatchAnalysis;
/*      */   private final boolean allowEffectivelyFinalInInnerClasses;
/*      */   private final boolean enforceThisDotInit;
/*      */ 
/*      */   public static Flow instance(Context paramContext)
/*      */   {
/*  203 */     Flow localFlow = (Flow)paramContext.get(flowKey);
/*  204 */     if (localFlow == null)
/*  205 */       localFlow = new Flow(paramContext);
/*  206 */     return localFlow;
/*      */   }
/*      */ 
/*      */   public void analyzeTree(Env<AttrContext> paramEnv, TreeMaker paramTreeMaker) {
/*  210 */     new AliveAnalyzer().analyzeTree(paramEnv, paramTreeMaker);
/*  211 */     new AssignAnalyzer().analyzeTree(paramEnv);
/*  212 */     new FlowAnalyzer().analyzeTree(paramEnv, paramTreeMaker);
/*  213 */     new CaptureAnalyzer().analyzeTree(paramEnv, paramTreeMaker);
/*      */   }
/*      */ 
/*      */   public void analyzeLambda(Env<AttrContext> paramEnv, JCTree.JCLambda paramJCLambda, TreeMaker paramTreeMaker, boolean paramBoolean) {
/*  217 */     Log.DiscardDiagnosticHandler localDiscardDiagnosticHandler = null;
/*      */ 
/*  223 */     if (!paramBoolean)
/*  224 */       localDiscardDiagnosticHandler = new Log.DiscardDiagnosticHandler(this.log);
/*      */     try
/*      */     {
/*  227 */       new AliveAnalyzer().analyzeTree(paramEnv, paramJCLambda, paramTreeMaker);
/*      */     } finally {
/*  229 */       if (!paramBoolean)
/*  230 */         this.log.popDiagnosticHandler(localDiscardDiagnosticHandler);
/*      */     }
/*      */   }
/*      */ 
/*      */   public List<Type> analyzeLambdaThrownTypes(final Env<AttrContext> paramEnv, JCTree.JCLambda paramJCLambda, TreeMaker paramTreeMaker)
/*      */   {
/*  242 */     Log.DiscardDiagnosticHandler localDiscardDiagnosticHandler = new Log.DiscardDiagnosticHandler(this.log);
/*      */     try {
/*  244 */       new AssignAnalyzer(paramEnv) {
/*  245 */         Scope enclosedSymbols = new Scope(paramEnv.enclClass.sym);
/*      */ 
/*      */         public void visitVarDef(JCTree.JCVariableDecl paramAnonymousJCVariableDecl) {
/*  248 */           this.enclosedSymbols.enter(paramAnonymousJCVariableDecl.sym);
/*  249 */           super.visitVarDef(paramAnonymousJCVariableDecl);
/*      */         }
/*      */ 
/*      */         protected boolean trackable(Symbol.VarSymbol paramAnonymousVarSymbol) {
/*  253 */           return (this.enclosedSymbols.includes(paramAnonymousVarSymbol)) && (paramAnonymousVarSymbol.owner.kind == 16);
/*      */         }
/*      */       }
/*  256 */       .analyzeTree(paramEnv, paramJCLambda);
/*      */ 
/*  257 */       LambdaFlowAnalyzer localLambdaFlowAnalyzer = new LambdaFlowAnalyzer();
/*  258 */       localLambdaFlowAnalyzer.analyzeTree(paramEnv, paramJCLambda, paramTreeMaker);
/*  259 */       return localLambdaFlowAnalyzer.inferredThrownTypes;
/*      */     } finally {
/*  261 */       this.log.popDiagnosticHandler(localDiscardDiagnosticHandler);
/*      */     }
/*      */   }
/*      */ 
/*      */   protected Flow(Context paramContext)
/*      */   {
/*  293 */     paramContext.put(flowKey, this);
/*  294 */     this.names = Names.instance(paramContext);
/*  295 */     this.log = Log.instance(paramContext);
/*  296 */     this.syms = Symtab.instance(paramContext);
/*  297 */     this.types = Types.instance(paramContext);
/*  298 */     this.chk = Check.instance(paramContext);
/*  299 */     this.lint = Lint.instance(paramContext);
/*  300 */     this.rs = Resolve.instance(paramContext);
/*  301 */     this.diags = JCDiagnostic.Factory.instance(paramContext);
/*  302 */     Source localSource = Source.instance(paramContext);
/*  303 */     this.allowImprovedRethrowAnalysis = localSource.allowImprovedRethrowAnalysis();
/*  304 */     this.allowImprovedCatchAnalysis = localSource.allowImprovedCatchAnalysis();
/*  305 */     this.allowEffectivelyFinalInInnerClasses = localSource.allowEffectivelyFinalInInnerClasses();
/*  306 */     this.enforceThisDotInit = localSource.enforceThisDotInit();
/*      */   }
/*      */ 
/*      */   class AliveAnalyzer extends Flow.BaseAnalyzer<Flow.BaseAnalyzer.PendingExit>
/*      */   {
/*      */     private boolean alive;
/*      */ 
/*      */     AliveAnalyzer()
/*      */     {
/*      */     }
/*      */ 
/*      */     void markDead()
/*      */     {
/*  424 */       this.alive = false;
/*      */     }
/*      */ 
/*      */     void scanDef(JCTree paramJCTree)
/*      */     {
/*  434 */       scanStat(paramJCTree);
/*  435 */       if ((paramJCTree != null) && (paramJCTree.hasTag(JCTree.Tag.BLOCK)) && (!this.alive))
/*  436 */         Flow.this.log.error(paramJCTree.pos(), "initializer.must.be.able.to.complete.normally", new Object[0]);
/*      */     }
/*      */ 
/*      */     void scanStat(JCTree paramJCTree)
/*      */     {
/*  444 */       if ((!this.alive) && (paramJCTree != null)) {
/*  445 */         Flow.this.log.error(paramJCTree.pos(), "unreachable.stmt", new Object[0]);
/*  446 */         if (!paramJCTree.hasTag(JCTree.Tag.SKIP)) this.alive = true;
/*      */       }
/*  448 */       scan(paramJCTree);
/*      */     }
/*      */ 
/*      */     void scanStats(List<? extends JCTree.JCStatement> paramList)
/*      */     {
/*  454 */       if (paramList != null)
/*  455 */         for (Object localObject = paramList; ((List)localObject).nonEmpty(); localObject = ((List)localObject).tail)
/*  456 */           scanStat((JCTree)((List)localObject).head);
/*      */     }
/*      */ 
/*      */     public void visitClassDef(JCTree.JCClassDecl paramJCClassDecl)
/*      */     {
/*  462 */       if (paramJCClassDecl.sym == null) return;
/*  463 */       boolean bool = this.alive;
/*  464 */       ListBuffer localListBuffer = this.pendingExits;
/*  465 */       Lint localLint = Flow.this.lint;
/*      */ 
/*  467 */       this.pendingExits = new ListBuffer();
/*  468 */       Flow.this.lint = Flow.this.lint.augment(paramJCClassDecl.sym);
/*      */       try
/*      */       {
/*  472 */         for (List localList = paramJCClassDecl.defs; localList.nonEmpty(); localList = localList.tail) {
/*  473 */           if ((!((JCTree)localList.head).hasTag(JCTree.Tag.METHODDEF)) && 
/*  474 */             ((TreeInfo.flags((JCTree)localList.head) & 
/*  474 */             0x8) != 0L)) {
/*  475 */             scanDef((JCTree)localList.head);
/*      */           }
/*      */ 
/*      */         }
/*      */ 
/*  480 */         for (localList = paramJCClassDecl.defs; localList.nonEmpty(); localList = localList.tail) {
/*  481 */           if ((!((JCTree)localList.head).hasTag(JCTree.Tag.METHODDEF)) && 
/*  482 */             ((TreeInfo.flags((JCTree)localList.head) & 
/*  482 */             0x8) == 0L)) {
/*  483 */             scanDef((JCTree)localList.head);
/*      */           }
/*      */ 
/*      */         }
/*      */ 
/*  488 */         for (localList = paramJCClassDecl.defs; localList.nonEmpty(); localList = localList.tail)
/*  489 */           if (((JCTree)localList.head).hasTag(JCTree.Tag.METHODDEF))
/*  490 */             scan((JCTree)localList.head);
/*      */       }
/*      */       finally
/*      */       {
/*  494 */         this.pendingExits = localListBuffer;
/*  495 */         this.alive = bool;
/*  496 */         Flow.this.lint = localLint;
/*      */       }
/*      */     }
/*      */ 
/*      */     public void visitMethodDef(JCTree.JCMethodDecl paramJCMethodDecl) {
/*  501 */       if (paramJCMethodDecl.body == null) return;
/*  502 */       Lint localLint = Flow.this.lint;
/*      */ 
/*  504 */       Flow.this.lint = Flow.this.lint.augment(paramJCMethodDecl.sym);
/*      */ 
/*  506 */       Assert.check(this.pendingExits.isEmpty());
/*      */       try
/*      */       {
/*  509 */         this.alive = true;
/*  510 */         scanStat(paramJCMethodDecl.body);
/*      */ 
/*  512 */         if ((this.alive) && (!paramJCMethodDecl.sym.type.getReturnType().hasTag(TypeTag.VOID))) {
/*  513 */           Flow.this.log.error(TreeInfo.diagEndPos(paramJCMethodDecl.body), "missing.ret.stmt", new Object[0]);
/*      */         }
/*  515 */         List localList = this.pendingExits.toList();
/*  516 */         this.pendingExits = new ListBuffer();
/*  517 */         while (localList.nonEmpty()) {
/*  518 */           Flow.BaseAnalyzer.PendingExit localPendingExit = (Flow.BaseAnalyzer.PendingExit)localList.head;
/*  519 */           localList = localList.tail;
/*  520 */           Assert.check(localPendingExit.tree.hasTag(JCTree.Tag.RETURN));
/*      */         }
/*      */       } finally {
/*  523 */         Flow.this.lint = localLint;
/*      */       }
/*      */     }
/*      */ 
/*      */     public void visitVarDef(JCTree.JCVariableDecl paramJCVariableDecl) {
/*  528 */       if (paramJCVariableDecl.init != null) {
/*  529 */         Lint localLint = Flow.this.lint;
/*  530 */         Flow.this.lint = Flow.this.lint.augment(paramJCVariableDecl.sym);
/*      */         try {
/*  532 */           scan(paramJCVariableDecl.init);
/*      */ 
/*  534 */           Flow.this.lint = localLint; } finally { Flow.this.lint = localLint; }
/*      */       }
/*      */     }
/*      */ 
/*      */     public void visitBlock(JCTree.JCBlock paramJCBlock)
/*      */     {
/*  540 */       scanStats(paramJCBlock.stats);
/*      */     }
/*      */ 
/*      */     public void visitDoLoop(JCTree.JCDoWhileLoop paramJCDoWhileLoop) {
/*  544 */       ListBuffer localListBuffer = this.pendingExits;
/*  545 */       this.pendingExits = new ListBuffer();
/*  546 */       scanStat(paramJCDoWhileLoop.body);
/*  547 */       this.alive |= resolveContinues(paramJCDoWhileLoop);
/*  548 */       scan(paramJCDoWhileLoop.cond);
/*  549 */       this.alive = ((this.alive) && (!paramJCDoWhileLoop.cond.type.isTrue()));
/*  550 */       this.alive |= resolveBreaks(paramJCDoWhileLoop, localListBuffer);
/*      */     }
/*      */ 
/*      */     public void visitWhileLoop(JCTree.JCWhileLoop paramJCWhileLoop) {
/*  554 */       ListBuffer localListBuffer = this.pendingExits;
/*  555 */       this.pendingExits = new ListBuffer();
/*  556 */       scan(paramJCWhileLoop.cond);
/*  557 */       this.alive = (!paramJCWhileLoop.cond.type.isFalse());
/*  558 */       scanStat(paramJCWhileLoop.body);
/*  559 */       this.alive |= resolveContinues(paramJCWhileLoop);
/*  560 */       this.alive = ((resolveBreaks(paramJCWhileLoop, localListBuffer)) || 
/*  561 */         (!paramJCWhileLoop.cond.type
/*  561 */         .isTrue()));
/*      */     }
/*      */ 
/*      */     public void visitForLoop(JCTree.JCForLoop paramJCForLoop) {
/*  565 */       ListBuffer localListBuffer = this.pendingExits;
/*  566 */       scanStats(paramJCForLoop.init);
/*  567 */       this.pendingExits = new ListBuffer();
/*  568 */       if (paramJCForLoop.cond != null) {
/*  569 */         scan(paramJCForLoop.cond);
/*  570 */         this.alive = (!paramJCForLoop.cond.type.isFalse());
/*      */       } else {
/*  572 */         this.alive = true;
/*      */       }
/*  574 */       scanStat(paramJCForLoop.body);
/*  575 */       this.alive |= resolveContinues(paramJCForLoop);
/*  576 */       scan(paramJCForLoop.step);
/*  577 */       this.alive = ((resolveBreaks(paramJCForLoop, localListBuffer)) || ((paramJCForLoop.cond != null) && 
/*  578 */         (!paramJCForLoop.cond.type
/*  578 */         .isTrue())));
/*      */     }
/*      */ 
/*      */     public void visitForeachLoop(JCTree.JCEnhancedForLoop paramJCEnhancedForLoop) {
/*  582 */       visitVarDef(paramJCEnhancedForLoop.var);
/*  583 */       ListBuffer localListBuffer = this.pendingExits;
/*  584 */       scan(paramJCEnhancedForLoop.expr);
/*  585 */       this.pendingExits = new ListBuffer();
/*  586 */       scanStat(paramJCEnhancedForLoop.body);
/*  587 */       this.alive |= resolveContinues(paramJCEnhancedForLoop);
/*  588 */       resolveBreaks(paramJCEnhancedForLoop, localListBuffer);
/*  589 */       this.alive = true;
/*      */     }
/*      */ 
/*      */     public void visitLabelled(JCTree.JCLabeledStatement paramJCLabeledStatement) {
/*  593 */       ListBuffer localListBuffer = this.pendingExits;
/*  594 */       this.pendingExits = new ListBuffer();
/*  595 */       scanStat(paramJCLabeledStatement.body);
/*  596 */       this.alive |= resolveBreaks(paramJCLabeledStatement, localListBuffer);
/*      */     }
/*      */ 
/*      */     public void visitSwitch(JCTree.JCSwitch paramJCSwitch) {
/*  600 */       ListBuffer localListBuffer = this.pendingExits;
/*  601 */       this.pendingExits = new ListBuffer();
/*  602 */       scan(paramJCSwitch.selector);
/*  603 */       int i = 0;
/*  604 */       for (List localList = paramJCSwitch.cases; localList.nonEmpty(); localList = localList.tail) {
/*  605 */         this.alive = true;
/*  606 */         JCTree.JCCase localJCCase = (JCTree.JCCase)localList.head;
/*  607 */         if (localJCCase.pat == null)
/*  608 */           i = 1;
/*      */         else
/*  610 */           scan(localJCCase.pat);
/*  611 */         scanStats(localJCCase.stats);
/*      */ 
/*  613 */         if ((this.alive) && 
/*  614 */           (Flow.this.lint
/*  614 */           .isEnabled(Lint.LintCategory.FALLTHROUGH)) && 
/*  615 */           (localJCCase.stats
/*  615 */           .nonEmpty()) && (localList.tail.nonEmpty())) {
/*  616 */           Flow.this.log.warning(Lint.LintCategory.FALLTHROUGH, ((JCTree.JCCase)localList.tail.head)
/*  617 */             .pos(), "possible.fall-through.into.case", new Object[0]);
/*      */         }
/*      */       }
/*  620 */       if (i == 0) {
/*  621 */         this.alive = true;
/*      */       }
/*  623 */       this.alive |= resolveBreaks(paramJCSwitch, localListBuffer);
/*      */     }
/*      */ 
/*      */     public void visitTry(JCTree.JCTry paramJCTry) {
/*  627 */       ListBuffer localListBuffer = this.pendingExits;
/*  628 */       this.pendingExits = new ListBuffer();
/*  629 */       for (Iterator localIterator = paramJCTry.resources.iterator(); localIterator.hasNext(); ) { localObject = (JCTree)localIterator.next();
/*  630 */         if ((localObject instanceof JCTree.JCVariableDecl)) {
/*  631 */           localJCVariableDecl = (JCTree.JCVariableDecl)localObject;
/*  632 */           visitVarDef(localJCVariableDecl);
/*  633 */         } else if ((localObject instanceof JCTree.JCExpression)) {
/*  634 */           scan((JCTree.JCExpression)localObject);
/*      */         } else {
/*  636 */           throw new AssertionError(paramJCTry);
/*      */         }
/*      */       }
/*      */       JCTree.JCVariableDecl localJCVariableDecl;
/*  640 */       scanStat(paramJCTry.body);
/*  641 */       boolean bool = this.alive;
/*      */ 
/*  643 */       for (Object localObject = paramJCTry.catchers; ((List)localObject).nonEmpty(); localObject = ((List)localObject).tail) {
/*  644 */         this.alive = true;
/*  645 */         localJCVariableDecl = ((JCTree.JCCatch)((List)localObject).head).param;
/*  646 */         scan(localJCVariableDecl);
/*  647 */         scanStat(((JCTree.JCCatch)((List)localObject).head).body);
/*  648 */         bool |= this.alive;
/*      */       }
/*  650 */       if (paramJCTry.finalizer != null) {
/*  651 */         localObject = this.pendingExits;
/*  652 */         this.pendingExits = localListBuffer;
/*  653 */         this.alive = true;
/*  654 */         scanStat(paramJCTry.finalizer);
/*  655 */         paramJCTry.finallyCanCompleteNormally = this.alive;
/*  656 */         if (!this.alive) {
/*  657 */           if (Flow.this.lint.isEnabled(Lint.LintCategory.FINALLY))
/*  658 */             Flow.this.log.warning(Lint.LintCategory.FINALLY, 
/*  659 */               TreeInfo.diagEndPos(paramJCTry.finalizer), 
/*  659 */               "finally.cannot.complete", new Object[0]);
/*      */         }
/*      */         else
/*      */         {
/*  663 */           while (((ListBuffer)localObject).nonEmpty()) {
/*  664 */             this.pendingExits.append(((ListBuffer)localObject).next());
/*      */           }
/*  666 */           this.alive = bool;
/*      */         }
/*      */       } else {
/*  669 */         this.alive = bool;
/*  670 */         localObject = this.pendingExits;
/*  671 */         this.pendingExits = localListBuffer;
/*  672 */         while (((ListBuffer)localObject).nonEmpty()) this.pendingExits.append(((ListBuffer)localObject).next());
/*      */       }
/*      */     }
/*      */ 
/*      */     public void visitIf(JCTree.JCIf paramJCIf)
/*      */     {
/*  678 */       scan(paramJCIf.cond);
/*  679 */       scanStat(paramJCIf.thenpart);
/*  680 */       if (paramJCIf.elsepart != null) {
/*  681 */         boolean bool = this.alive;
/*  682 */         this.alive = true;
/*  683 */         scanStat(paramJCIf.elsepart);
/*  684 */         this.alive |= bool;
/*      */       } else {
/*  686 */         this.alive = true;
/*      */       }
/*      */     }
/*      */ 
/*      */     public void visitBreak(JCTree.JCBreak paramJCBreak) {
/*  691 */       recordExit(new Flow.BaseAnalyzer.PendingExit(paramJCBreak));
/*      */     }
/*      */ 
/*      */     public void visitContinue(JCTree.JCContinue paramJCContinue) {
/*  695 */       recordExit(new Flow.BaseAnalyzer.PendingExit(paramJCContinue));
/*      */     }
/*      */ 
/*      */     public void visitReturn(JCTree.JCReturn paramJCReturn) {
/*  699 */       scan(paramJCReturn.expr);
/*  700 */       recordExit(new Flow.BaseAnalyzer.PendingExit(paramJCReturn));
/*      */     }
/*      */ 
/*      */     public void visitThrow(JCTree.JCThrow paramJCThrow) {
/*  704 */       scan(paramJCThrow.expr);
/*  705 */       markDead();
/*      */     }
/*      */ 
/*      */     public void visitApply(JCTree.JCMethodInvocation paramJCMethodInvocation) {
/*  709 */       scan(paramJCMethodInvocation.meth);
/*  710 */       scan(paramJCMethodInvocation.args);
/*      */     }
/*      */ 
/*      */     public void visitNewClass(JCTree.JCNewClass paramJCNewClass) {
/*  714 */       scan(paramJCNewClass.encl);
/*  715 */       scan(paramJCNewClass.args);
/*  716 */       if (paramJCNewClass.def != null)
/*  717 */         scan(paramJCNewClass.def);
/*      */     }
/*      */ 
/*      */     public void visitLambda(JCTree.JCLambda paramJCLambda)
/*      */     {
/*  723 */       if ((paramJCLambda.type != null) && 
/*  724 */         (paramJCLambda.type
/*  724 */         .isErroneous())) {
/*  725 */         return;
/*      */       }
/*      */ 
/*  728 */       ListBuffer localListBuffer = this.pendingExits;
/*  729 */       boolean bool = this.alive;
/*      */       try {
/*  731 */         this.pendingExits = new ListBuffer();
/*  732 */         this.alive = true;
/*  733 */         scanStat(paramJCLambda.body);
/*  734 */         paramJCLambda.canCompleteNormally = this.alive;
/*      */       }
/*      */       finally {
/*  737 */         this.pendingExits = localListBuffer;
/*  738 */         this.alive = bool;
/*      */       }
/*      */     }
/*      */ 
/*      */     public void visitTopLevel(JCTree.JCCompilationUnit paramJCCompilationUnit)
/*      */     {
/*      */     }
/*      */ 
/*      */     public void analyzeTree(Env<AttrContext> paramEnv, TreeMaker paramTreeMaker)
/*      */     {
/*  753 */       analyzeTree(paramEnv, paramEnv.tree, paramTreeMaker);
/*      */     }
/*      */     public void analyzeTree(Env<AttrContext> paramEnv, JCTree paramJCTree, TreeMaker paramTreeMaker) {
/*      */       try {
/*  757 */         Flow.this.attrEnv = paramEnv;
/*  758 */         Flow.this.make = paramTreeMaker;
/*  759 */         this.pendingExits = new ListBuffer();
/*  760 */         this.alive = true;
/*  761 */         scan(paramJCTree);
/*      */       } finally {
/*  763 */         this.pendingExits = null;
/*  764 */         Flow.this.make = null;
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   public class AssignAnalyzer extends Flow.BaseAnalyzer<AssignPendingExit>
/*      */   {
/*      */     final Bits inits;
/*      */     final Bits uninits;
/*      */     final Bits uninitsTry;
/*      */     final Bits initsWhenTrue;
/*      */     final Bits initsWhenFalse;
/*      */     final Bits uninitsWhenTrue;
/*      */     final Bits uninitsWhenFalse;
/*      */     protected JCTree.JCVariableDecl[] vardecls;
/*      */     JCTree.JCClassDecl classDef;
/*      */     int firstadr;
/*      */     protected int nextadr;
/*      */     protected int returnadr;
/*      */     Scope unrefdResources;
/* 1434 */     Flow.FlowKind flowKind = Flow.FlowKind.NORMAL;
/*      */     int startPos;
/* 1471 */     private boolean isInitialConstructor = false;
/*      */ 
/*      */     public AssignAnalyzer()
/*      */     {
/* 1462 */       this.inits = new Bits();
/* 1463 */       this.uninits = new Bits();
/* 1464 */       this.uninitsTry = new Bits();
/* 1465 */       this.initsWhenTrue = new Bits(true);
/* 1466 */       this.initsWhenFalse = new Bits(true);
/* 1467 */       this.uninitsWhenTrue = new Bits(true);
/* 1468 */       this.uninitsWhenFalse = new Bits(true);
/*      */     }
/*      */ 
/*      */     void markDead()
/*      */     {
/* 1475 */       if (!this.isInitialConstructor)
/* 1476 */         this.inits.inclRange(this.returnadr, this.nextadr);
/*      */       else {
/* 1478 */         for (int i = this.returnadr; i < this.nextadr; i++) {
/* 1479 */           if (!isFinalUninitializedStaticField(this.vardecls[i].sym)) {
/* 1480 */             this.inits.incl(i);
/*      */           }
/*      */         }
/*      */       }
/* 1484 */       this.uninits.inclRange(this.returnadr, this.nextadr);
/*      */     }
/*      */ 
/*      */     protected boolean trackable(Symbol.VarSymbol paramVarSymbol)
/*      */     {
/* 1496 */       return (paramVarSymbol.pos >= this.startPos) && ((paramVarSymbol.owner.kind == 16) || 
/* 1496 */         (isFinalUninitializedField(paramVarSymbol)));
/*      */     }
/*      */ 
/*      */     boolean isFinalUninitializedField(Symbol.VarSymbol paramVarSymbol)
/*      */     {
/* 1502 */       return (paramVarSymbol.owner.kind == 2) && 
/* 1501 */         ((paramVarSymbol
/* 1501 */         .flags() & 0x40010) == 16L) && 
/* 1502 */         (this.classDef.sym
/* 1502 */         .isEnclosedBy((Symbol.ClassSymbol)paramVarSymbol.owner));
/*      */     }
/*      */ 
/*      */     boolean isFinalUninitializedStaticField(Symbol.VarSymbol paramVarSymbol)
/*      */     {
/* 1506 */       return (isFinalUninitializedField(paramVarSymbol)) && (paramVarSymbol.isStatic());
/*      */     }
/*      */ 
/*      */     void newVar(JCTree.JCVariableDecl paramJCVariableDecl)
/*      */     {
/* 1514 */       Symbol.VarSymbol localVarSymbol = paramJCVariableDecl.sym;
/* 1515 */       this.vardecls = ((JCTree.JCVariableDecl[])ArrayUtils.ensureCapacity(this.vardecls, this.nextadr));
/* 1516 */       if ((localVarSymbol.flags() & 0x10) == 0L) {
/* 1517 */         localVarSymbol.flags_field |= 2199023255552L;
/*      */       }
/* 1519 */       localVarSymbol.adr = this.nextadr;
/* 1520 */       this.vardecls[this.nextadr] = paramJCVariableDecl;
/* 1521 */       this.inits.excl(this.nextadr);
/* 1522 */       this.uninits.incl(this.nextadr);
/* 1523 */       this.nextadr += 1;
/*      */     }
/*      */ 
/*      */     void letInit(JCDiagnostic.DiagnosticPosition paramDiagnosticPosition, Symbol.VarSymbol paramVarSymbol)
/*      */     {
/* 1529 */       if ((paramVarSymbol.adr >= this.firstadr) && (trackable(paramVarSymbol))) {
/* 1530 */         if ((paramVarSymbol.flags() & 0x0) != 0L) {
/* 1531 */           if (!this.uninits.isMember(paramVarSymbol.adr))
/*      */           {
/* 1535 */             paramVarSymbol.flags_field &= -2199023255553L;
/*      */           }
/* 1537 */           else uninit(paramVarSymbol);
/*      */         }
/* 1539 */         else if ((paramVarSymbol.flags() & 0x10) != 0L) {
/* 1540 */           if ((paramVarSymbol.flags() & 0x0) != 0L) {
/* 1541 */             if ((paramVarSymbol.flags() & 0x0) != 0L)
/* 1542 */               Flow.this.log.error(paramDiagnosticPosition, "multicatch.parameter.may.not.be.assigned", new Object[] { paramVarSymbol });
/*      */             else {
/* 1544 */               Flow.this.log.error(paramDiagnosticPosition, "final.parameter.may.not.be.assigned", new Object[] { paramVarSymbol });
/*      */             }
/*      */           }
/* 1547 */           else if (!this.uninits.isMember(paramVarSymbol.adr))
/* 1548 */             Flow.this.log.error(paramDiagnosticPosition, this.flowKind.errKey, new Object[] { paramVarSymbol });
/*      */           else {
/* 1550 */             uninit(paramVarSymbol);
/*      */           }
/*      */         }
/* 1553 */         this.inits.incl(paramVarSymbol.adr);
/* 1554 */       } else if ((paramVarSymbol.flags() & 0x10) != 0L) {
/* 1555 */         Flow.this.log.error(paramDiagnosticPosition, "var.might.already.be.assigned", new Object[] { paramVarSymbol });
/*      */       }
/*      */     }
/*      */ 
/*      */     void uninit(Symbol.VarSymbol paramVarSymbol) {
/* 1560 */       if (!this.inits.isMember(paramVarSymbol.adr))
/*      */       {
/* 1562 */         this.uninits.excl(paramVarSymbol.adr);
/* 1563 */         this.uninitsTry.excl(paramVarSymbol.adr);
/*      */       }
/*      */       else {
/* 1566 */         this.uninits.excl(paramVarSymbol.adr);
/*      */       }
/*      */     }
/*      */ 
/*      */     void letInit(JCTree paramJCTree)
/*      */     {
/* 1575 */       paramJCTree = TreeInfo.skipParens(paramJCTree);
/* 1576 */       if ((paramJCTree.hasTag(JCTree.Tag.IDENT)) || (paramJCTree.hasTag(JCTree.Tag.SELECT))) {
/* 1577 */         Symbol localSymbol = TreeInfo.symbol(paramJCTree);
/* 1578 */         if (localSymbol.kind == 4)
/* 1579 */           letInit(paramJCTree.pos(), (Symbol.VarSymbol)localSymbol);
/*      */       }
/*      */     }
/*      */ 
/*      */     void checkInit(JCDiagnostic.DiagnosticPosition paramDiagnosticPosition, Symbol.VarSymbol paramVarSymbol)
/*      */     {
/* 1587 */       checkInit(paramDiagnosticPosition, paramVarSymbol, "var.might.not.have.been.initialized");
/*      */     }
/*      */ 
/*      */     void checkInit(JCDiagnostic.DiagnosticPosition paramDiagnosticPosition, Symbol.VarSymbol paramVarSymbol, String paramString) {
/* 1591 */       if (((paramVarSymbol.adr >= this.firstadr) || (paramVarSymbol.owner.kind != 2)) && 
/* 1592 */         (trackable(paramVarSymbol)) && 
/* 1593 */         (!this.inits
/* 1593 */         .isMember(paramVarSymbol.adr)))
/*      */       {
/* 1594 */         Flow.this.log.error(paramDiagnosticPosition, paramString, new Object[] { paramVarSymbol });
/* 1595 */         this.inits.incl(paramVarSymbol.adr);
/*      */       }
/*      */     }
/*      */ 
/*      */     private void resetBits(Bits[] paramArrayOfBits)
/*      */     {
/* 1602 */       for (Bits localBits : paramArrayOfBits)
/* 1603 */         localBits.reset();
/*      */     }
/*      */ 
/*      */     void split(boolean paramBoolean)
/*      */     {
/* 1610 */       this.initsWhenFalse.assign(this.inits);
/* 1611 */       this.uninitsWhenFalse.assign(this.uninits);
/* 1612 */       this.initsWhenTrue.assign(this.inits);
/* 1613 */       this.uninitsWhenTrue.assign(this.uninits);
/* 1614 */       if (paramBoolean)
/* 1615 */         resetBits(new Bits[] { this.inits, this.uninits });
/*      */     }
/*      */ 
/*      */     protected void merge()
/*      */     {
/* 1622 */       this.inits.assign(this.initsWhenFalse.andSet(this.initsWhenTrue));
/* 1623 */       this.uninits.assign(this.uninitsWhenFalse.andSet(this.uninitsWhenTrue));
/*      */     }
/*      */ 
/*      */     void scanExpr(JCTree paramJCTree)
/*      */     {
/* 1634 */       if (paramJCTree != null) {
/* 1635 */         scan(paramJCTree);
/* 1636 */         if (this.inits.isReset())
/* 1637 */           merge();
/*      */       }
/*      */     }
/*      */ 
/*      */     void scanExprs(List<? extends JCTree.JCExpression> paramList)
/*      */     {
/* 1645 */       if (paramList != null)
/* 1646 */         for (Object localObject = paramList; ((List)localObject).nonEmpty(); localObject = ((List)localObject).tail)
/* 1647 */           scanExpr((JCTree)((List)localObject).head);
/*      */     }
/*      */ 
/*      */     void scanCond(JCTree paramJCTree)
/*      */     {
/* 1654 */       if (paramJCTree.type.isFalse()) {
/* 1655 */         if (this.inits.isReset()) merge();
/* 1656 */         this.initsWhenTrue.assign(this.inits);
/* 1657 */         this.initsWhenTrue.inclRange(this.firstadr, this.nextadr);
/* 1658 */         this.uninitsWhenTrue.assign(this.uninits);
/* 1659 */         this.uninitsWhenTrue.inclRange(this.firstadr, this.nextadr);
/* 1660 */         this.initsWhenFalse.assign(this.inits);
/* 1661 */         this.uninitsWhenFalse.assign(this.uninits);
/* 1662 */       } else if (paramJCTree.type.isTrue()) {
/* 1663 */         if (this.inits.isReset()) merge();
/* 1664 */         this.initsWhenFalse.assign(this.inits);
/* 1665 */         this.initsWhenFalse.inclRange(this.firstadr, this.nextadr);
/* 1666 */         this.uninitsWhenFalse.assign(this.uninits);
/* 1667 */         this.uninitsWhenFalse.inclRange(this.firstadr, this.nextadr);
/* 1668 */         this.initsWhenTrue.assign(this.inits);
/* 1669 */         this.uninitsWhenTrue.assign(this.uninits);
/*      */       } else {
/* 1671 */         scan(paramJCTree);
/* 1672 */         if (!this.inits.isReset())
/* 1673 */           split(paramJCTree.type != Flow.this.syms.unknownType);
/*      */       }
/* 1675 */       if (paramJCTree.type != Flow.this.syms.unknownType)
/* 1676 */         resetBits(new Bits[] { this.inits, this.uninits });
/*      */     }
/*      */ 
/*      */     public void visitClassDef(JCTree.JCClassDecl paramJCClassDecl)
/*      */     {
/* 1683 */       if (paramJCClassDecl.sym == null) {
/* 1684 */         return;
/*      */       }
/*      */ 
/* 1687 */       Lint localLint = Flow.this.lint;
/* 1688 */       Flow.this.lint = Flow.this.lint.augment(paramJCClassDecl.sym);
/*      */       try {
/* 1690 */         if (paramJCClassDecl.sym == null) {
/* 1691 */           return;
/*      */         }
/*      */ 
/* 1694 */         JCTree.JCClassDecl localJCClassDecl = this.classDef;
/* 1695 */         int i = this.firstadr;
/* 1696 */         int j = this.nextadr;
/* 1697 */         ListBuffer localListBuffer = this.pendingExits;
/*      */ 
/* 1699 */         this.pendingExits = new ListBuffer();
/* 1700 */         if (paramJCClassDecl.name != Flow.this.names.empty) {
/* 1701 */           this.firstadr = this.nextadr;
/*      */         }
/* 1703 */         this.classDef = paramJCClassDecl;
/*      */         try
/*      */         {
/*      */           JCTree.JCVariableDecl localJCVariableDecl;
/*      */           Symbol.VarSymbol localVarSymbol;
/* 1706 */           for (List localList = paramJCClassDecl.defs; localList.nonEmpty(); localList = localList.tail) {
/* 1707 */             if (((JCTree)localList.head).hasTag(JCTree.Tag.VARDEF)) {
/* 1708 */               localJCVariableDecl = (JCTree.JCVariableDecl)localList.head;
/* 1709 */               if ((localJCVariableDecl.mods.flags & 0x8) != 0L) {
/* 1710 */                 localVarSymbol = localJCVariableDecl.sym;
/* 1711 */                 if (trackable(localVarSymbol)) {
/* 1712 */                   newVar(localJCVariableDecl);
/*      */                 }
/*      */               }
/*      */             }
/*      */ 
/*      */           }
/*      */ 
/* 1719 */           for (localList = paramJCClassDecl.defs; localList.nonEmpty(); localList = localList.tail) {
/* 1720 */             if ((!((JCTree)localList.head).hasTag(JCTree.Tag.METHODDEF)) && 
/* 1721 */               ((TreeInfo.flags((JCTree)localList.head) & 
/* 1721 */               0x8) != 0L)) {
/* 1722 */               scan((JCTree)localList.head);
/*      */             }
/*      */ 
/*      */           }
/*      */ 
/* 1727 */           for (localList = paramJCClassDecl.defs; localList.nonEmpty(); localList = localList.tail) {
/* 1728 */             if (((JCTree)localList.head).hasTag(JCTree.Tag.VARDEF)) {
/* 1729 */               localJCVariableDecl = (JCTree.JCVariableDecl)localList.head;
/* 1730 */               if ((localJCVariableDecl.mods.flags & 0x8) == 0L) {
/* 1731 */                 localVarSymbol = localJCVariableDecl.sym;
/* 1732 */                 if (trackable(localVarSymbol)) {
/* 1733 */                   newVar(localJCVariableDecl);
/*      */                 }
/*      */               }
/*      */             }
/*      */           }
/*      */ 
/* 1739 */           for (localList = paramJCClassDecl.defs; localList.nonEmpty(); localList = localList.tail) {
/* 1740 */             if ((!((JCTree)localList.head).hasTag(JCTree.Tag.METHODDEF)) && 
/* 1741 */               ((TreeInfo.flags((JCTree)localList.head) & 
/* 1741 */               0x8) == 0L)) {
/* 1742 */               scan((JCTree)localList.head);
/*      */             }
/*      */ 
/*      */           }
/*      */ 
/* 1747 */           for (localList = paramJCClassDecl.defs; localList.nonEmpty(); localList = localList.tail)
/* 1748 */             if (((JCTree)localList.head).hasTag(JCTree.Tag.METHODDEF))
/* 1749 */               scan((JCTree)localList.head);
/*      */         }
/*      */         finally
/*      */         {
/* 1753 */           this.pendingExits = localListBuffer;
/* 1754 */           this.nextadr = j;
/* 1755 */           this.firstadr = i;
/* 1756 */           this.classDef = localJCClassDecl;
/*      */         }
/*      */       } finally {
/* 1759 */         Flow.this.lint = localLint;
/*      */       }
/*      */     }
/*      */ 
/*      */     public void visitMethodDef(JCTree.JCMethodDecl paramJCMethodDecl) {
/* 1764 */       if (paramJCMethodDecl.body == null) {
/* 1765 */         return;
/*      */       }
/*      */ 
/* 1770 */       if ((paramJCMethodDecl.sym.flags() & 0x1000) != 0L) {
/* 1771 */         return;
/*      */       }
/*      */ 
/* 1774 */       Lint localLint = Flow.this.lint;
/* 1775 */       Flow.this.lint = Flow.this.lint.augment(paramJCMethodDecl.sym);
/*      */       try {
/* 1777 */         if (paramJCMethodDecl.body == null) {
/* 1778 */           return;
/*      */         }
/*      */ 
/* 1782 */         if ((paramJCMethodDecl.sym.flags() & 0x1000) == 4096L) {
/* 1783 */           return;
/*      */         }
/*      */ 
/* 1786 */         Bits localBits1 = new Bits(this.inits);
/* 1787 */         Bits localBits2 = new Bits(this.uninits);
/* 1788 */         int i = this.nextadr;
/* 1789 */         int j = this.firstadr;
/* 1790 */         int k = this.returnadr;
/*      */ 
/* 1792 */         Assert.check(this.pendingExits.isEmpty());
/* 1793 */         boolean bool = this.isInitialConstructor;
/*      */         try {
/* 1795 */           this.isInitialConstructor = TreeInfo.isInitialConstructor(paramJCMethodDecl);
/*      */ 
/* 1797 */           if (!this.isInitialConstructor) {
/* 1798 */             this.firstadr = this.nextadr;
/*      */           }
/* 1800 */           for (List localList1 = paramJCMethodDecl.params; localList1.nonEmpty(); localList1 = localList1.tail) {
/* 1801 */             JCTree.JCVariableDecl localJCVariableDecl1 = (JCTree.JCVariableDecl)localList1.head;
/* 1802 */             scan(localJCVariableDecl1);
/* 1803 */             Assert.check((localJCVariableDecl1.sym.flags() & 0x0) != 0L, "Method parameter without PARAMETER flag");
/*      */ 
/* 1807 */             initParam(localJCVariableDecl1);
/*      */           }
/*      */ 
/* 1811 */           scan(paramJCMethodDecl.body);
/*      */ 
/* 1813 */           if (this.isInitialConstructor) {
/* 1814 */             int m = (paramJCMethodDecl.sym.flags() & 0x0) != 0L ? 1 : 0;
/*      */ 
/* 1816 */             for (int n = this.firstadr; n < this.nextadr; n++) {
/* 1817 */               JCTree.JCVariableDecl localJCVariableDecl2 = this.vardecls[n];
/* 1818 */               Symbol.VarSymbol localVarSymbol = localJCVariableDecl2.sym;
/* 1819 */               if (localVarSymbol.owner == this.classDef.sym)
/*      */               {
/* 1822 */                 if (m != 0) {
/* 1823 */                   checkInit(TreeInfo.diagnosticPositionFor(localVarSymbol, localJCVariableDecl2), localVarSymbol, "var.not.initialized.in.default.constructor");
/*      */                 }
/*      */                 else {
/* 1826 */                   checkInit(TreeInfo.diagEndPos(paramJCMethodDecl.body), localVarSymbol);
/*      */                 }
/*      */               }
/*      */             }
/*      */           }
/* 1831 */           List localList2 = this.pendingExits.toList();
/* 1832 */           this.pendingExits = new ListBuffer();
/* 1833 */           while (localList2.nonEmpty()) {
/* 1834 */             AssignPendingExit localAssignPendingExit = (AssignPendingExit)localList2.head;
/* 1835 */             localList2 = localList2.tail;
/* 1836 */             Assert.check(localAssignPendingExit.tree.hasTag(JCTree.Tag.RETURN), localAssignPendingExit.tree);
/* 1837 */             if (this.isInitialConstructor) {
/* 1838 */               this.inits.assign(localAssignPendingExit.exit_inits);
/* 1839 */               for (int i1 = this.firstadr; i1 < this.nextadr; i1++)
/* 1840 */                 checkInit(localAssignPendingExit.tree.pos(), this.vardecls[i1].sym);
/*      */             }
/*      */           }
/*      */         }
/*      */         finally {
/* 1845 */           this.inits.assign(localBits1);
/* 1846 */           this.uninits.assign(localBits2);
/* 1847 */           this.nextadr = i;
/* 1848 */           this.firstadr = j;
/* 1849 */           this.returnadr = k;
/* 1850 */           this.isInitialConstructor = bool;
/*      */         }
/*      */       } finally {
/* 1853 */         Flow.this.lint = localLint;
/*      */       }
/*      */     }
/*      */ 
/*      */     protected void initParam(JCTree.JCVariableDecl paramJCVariableDecl) {
/* 1858 */       this.inits.incl(paramJCVariableDecl.sym.adr);
/* 1859 */       this.uninits.excl(paramJCVariableDecl.sym.adr);
/*      */     }
/*      */ 
/*      */     public void visitVarDef(JCTree.JCVariableDecl paramJCVariableDecl) {
/* 1863 */       Lint localLint = Flow.this.lint;
/* 1864 */       Flow.this.lint = Flow.this.lint.augment(paramJCVariableDecl.sym);
/*      */       try {
/* 1866 */         boolean bool = trackable(paramJCVariableDecl.sym);
/* 1867 */         if ((bool) && (paramJCVariableDecl.sym.owner.kind == 16)) {
/* 1868 */           newVar(paramJCVariableDecl);
/*      */         }
/* 1870 */         if (paramJCVariableDecl.init != null) {
/* 1871 */           scanExpr(paramJCVariableDecl.init);
/* 1872 */           if (bool)
/* 1873 */             letInit(paramJCVariableDecl.pos(), paramJCVariableDecl.sym);
/*      */         }
/*      */       }
/*      */       finally {
/* 1877 */         Flow.this.lint = localLint;
/*      */       }
/*      */     }
/*      */ 
/*      */     public void visitBlock(JCTree.JCBlock paramJCBlock) {
/* 1882 */       int i = this.nextadr;
/* 1883 */       scan(paramJCBlock.stats);
/* 1884 */       this.nextadr = i;
/*      */     }
/*      */ 
/*      */     public void visitDoLoop(JCTree.JCDoWhileLoop paramJCDoWhileLoop) {
/* 1888 */       ListBuffer localListBuffer = this.pendingExits;
/* 1889 */       Flow.FlowKind localFlowKind = this.flowKind;
/* 1890 */       this.flowKind = Flow.FlowKind.NORMAL;
/* 1891 */       Bits localBits1 = new Bits(true);
/* 1892 */       Bits localBits2 = new Bits(true);
/* 1893 */       this.pendingExits = new ListBuffer();
/* 1894 */       int i = Flow.this.log.nerrors;
/*      */       while (true) {
/* 1896 */         Bits localBits3 = new Bits(this.uninits);
/* 1897 */         localBits3.excludeFrom(this.nextadr);
/* 1898 */         scan(paramJCDoWhileLoop.body);
/* 1899 */         resolveContinues(paramJCDoWhileLoop);
/* 1900 */         scanCond(paramJCDoWhileLoop.cond);
/* 1901 */         if (!this.flowKind.isFinal()) {
/* 1902 */           localBits1.assign(this.initsWhenFalse);
/* 1903 */           localBits2.assign(this.uninitsWhenFalse);
/*      */         }
/* 1905 */         if ((Flow.this.log.nerrors != i) || 
/* 1906 */           (this.flowKind
/* 1906 */           .isFinal()) || 
/* 1907 */           (new Bits(localBits3)
/* 1907 */           .diffSet(this.uninitsWhenTrue)
/* 1907 */           .nextBit(this.firstadr) == -1))
/*      */           break;
/* 1909 */         this.inits.assign(this.initsWhenTrue);
/* 1910 */         this.uninits.assign(localBits3.andSet(this.uninitsWhenTrue));
/* 1911 */         this.flowKind = Flow.FlowKind.SPECULATIVE_LOOP;
/*      */       }
/* 1913 */       this.flowKind = localFlowKind;
/* 1914 */       this.inits.assign(localBits1);
/* 1915 */       this.uninits.assign(localBits2);
/* 1916 */       resolveBreaks(paramJCDoWhileLoop, localListBuffer);
/*      */     }
/*      */ 
/*      */     public void visitWhileLoop(JCTree.JCWhileLoop paramJCWhileLoop) {
/* 1920 */       ListBuffer localListBuffer = this.pendingExits;
/* 1921 */       Flow.FlowKind localFlowKind = this.flowKind;
/* 1922 */       this.flowKind = Flow.FlowKind.NORMAL;
/* 1923 */       Bits localBits1 = new Bits(true);
/* 1924 */       Bits localBits2 = new Bits(true);
/* 1925 */       this.pendingExits = new ListBuffer();
/* 1926 */       int i = Flow.this.log.nerrors;
/* 1927 */       Bits localBits3 = new Bits(this.uninits);
/* 1928 */       localBits3.excludeFrom(this.nextadr);
/*      */       while (true) {
/* 1930 */         scanCond(paramJCWhileLoop.cond);
/* 1931 */         if (!this.flowKind.isFinal()) {
/* 1932 */           localBits1.assign(this.initsWhenFalse);
/* 1933 */           localBits2.assign(this.uninitsWhenFalse);
/*      */         }
/* 1935 */         this.inits.assign(this.initsWhenTrue);
/* 1936 */         this.uninits.assign(this.uninitsWhenTrue);
/* 1937 */         scan(paramJCWhileLoop.body);
/* 1938 */         resolveContinues(paramJCWhileLoop);
/* 1939 */         if ((Flow.this.log.nerrors != i) || 
/* 1940 */           (this.flowKind
/* 1940 */           .isFinal()) || 
/* 1941 */           (new Bits(localBits3)
/* 1941 */           .diffSet(this.uninits)
/* 1941 */           .nextBit(this.firstadr) == -1)) {
/*      */           break;
/*      */         }
/* 1944 */         this.uninits.assign(localBits3.andSet(this.uninits));
/* 1945 */         this.flowKind = Flow.FlowKind.SPECULATIVE_LOOP;
/*      */       }
/* 1947 */       this.flowKind = localFlowKind;
/*      */ 
/* 1950 */       this.inits.assign(localBits1);
/* 1951 */       this.uninits.assign(localBits2);
/* 1952 */       resolveBreaks(paramJCWhileLoop, localListBuffer);
/*      */     }
/*      */ 
/*      */     public void visitForLoop(JCTree.JCForLoop paramJCForLoop) {
/* 1956 */       ListBuffer localListBuffer = this.pendingExits;
/* 1957 */       Flow.FlowKind localFlowKind = this.flowKind;
/* 1958 */       this.flowKind = Flow.FlowKind.NORMAL;
/* 1959 */       int i = this.nextadr;
/* 1960 */       scan(paramJCForLoop.init);
/* 1961 */       Bits localBits1 = new Bits(true);
/* 1962 */       Bits localBits2 = new Bits(true);
/* 1963 */       this.pendingExits = new ListBuffer();
/* 1964 */       int j = Flow.this.log.nerrors;
/*      */       while (true) {
/* 1966 */         Bits localBits3 = new Bits(this.uninits);
/* 1967 */         localBits3.excludeFrom(this.nextadr);
/* 1968 */         if (paramJCForLoop.cond != null) {
/* 1969 */           scanCond(paramJCForLoop.cond);
/* 1970 */           if (!this.flowKind.isFinal()) {
/* 1971 */             localBits1.assign(this.initsWhenFalse);
/* 1972 */             localBits2.assign(this.uninitsWhenFalse);
/*      */           }
/* 1974 */           this.inits.assign(this.initsWhenTrue);
/* 1975 */           this.uninits.assign(this.uninitsWhenTrue);
/* 1976 */         } else if (!this.flowKind.isFinal()) {
/* 1977 */           localBits1.assign(this.inits);
/* 1978 */           localBits1.inclRange(this.firstadr, this.nextadr);
/* 1979 */           localBits2.assign(this.uninits);
/* 1980 */           localBits2.inclRange(this.firstadr, this.nextadr);
/*      */         }
/* 1982 */         scan(paramJCForLoop.body);
/* 1983 */         resolveContinues(paramJCForLoop);
/* 1984 */         scan(paramJCForLoop.step);
/* 1985 */         if ((Flow.this.log.nerrors != j) || 
/* 1986 */           (this.flowKind
/* 1986 */           .isFinal()) || 
/* 1987 */           (new Bits(localBits3)
/* 1987 */           .diffSet(this.uninits)
/* 1987 */           .nextBit(this.firstadr) == -1))
/*      */           break;
/* 1989 */         this.uninits.assign(localBits3.andSet(this.uninits));
/* 1990 */         this.flowKind = Flow.FlowKind.SPECULATIVE_LOOP;
/*      */       }
/* 1992 */       this.flowKind = localFlowKind;
/*      */ 
/* 1995 */       this.inits.assign(localBits1);
/* 1996 */       this.uninits.assign(localBits2);
/* 1997 */       resolveBreaks(paramJCForLoop, localListBuffer);
/* 1998 */       this.nextadr = i;
/*      */     }
/*      */ 
/*      */     public void visitForeachLoop(JCTree.JCEnhancedForLoop paramJCEnhancedForLoop) {
/* 2002 */       visitVarDef(paramJCEnhancedForLoop.var);
/*      */ 
/* 2004 */       ListBuffer localListBuffer = this.pendingExits;
/* 2005 */       Flow.FlowKind localFlowKind = this.flowKind;
/* 2006 */       this.flowKind = Flow.FlowKind.NORMAL;
/* 2007 */       int i = this.nextadr;
/* 2008 */       scan(paramJCEnhancedForLoop.expr);
/* 2009 */       Bits localBits1 = new Bits(this.inits);
/* 2010 */       Bits localBits2 = new Bits(this.uninits);
/*      */ 
/* 2012 */       letInit(paramJCEnhancedForLoop.pos(), paramJCEnhancedForLoop.var.sym);
/* 2013 */       this.pendingExits = new ListBuffer();
/* 2014 */       int j = Flow.this.log.nerrors;
/*      */       while (true) {
/* 2016 */         Bits localBits3 = new Bits(this.uninits);
/* 2017 */         localBits3.excludeFrom(this.nextadr);
/* 2018 */         scan(paramJCEnhancedForLoop.body);
/* 2019 */         resolveContinues(paramJCEnhancedForLoop);
/* 2020 */         if ((Flow.this.log.nerrors != j) || 
/* 2021 */           (this.flowKind
/* 2021 */           .isFinal()) || 
/* 2022 */           (new Bits(localBits3)
/* 2022 */           .diffSet(this.uninits)
/* 2022 */           .nextBit(this.firstadr) == -1))
/*      */           break;
/* 2024 */         this.uninits.assign(localBits3.andSet(this.uninits));
/* 2025 */         this.flowKind = Flow.FlowKind.SPECULATIVE_LOOP;
/*      */       }
/* 2027 */       this.flowKind = localFlowKind;
/* 2028 */       this.inits.assign(localBits1);
/* 2029 */       this.uninits.assign(localBits2.andSet(this.uninits));
/* 2030 */       resolveBreaks(paramJCEnhancedForLoop, localListBuffer);
/* 2031 */       this.nextadr = i;
/*      */     }
/*      */ 
/*      */     public void visitLabelled(JCTree.JCLabeledStatement paramJCLabeledStatement) {
/* 2035 */       ListBuffer localListBuffer = this.pendingExits;
/* 2036 */       this.pendingExits = new ListBuffer();
/* 2037 */       scan(paramJCLabeledStatement.body);
/* 2038 */       resolveBreaks(paramJCLabeledStatement, localListBuffer);
/*      */     }
/*      */ 
/*      */     public void visitSwitch(JCTree.JCSwitch paramJCSwitch) {
/* 2042 */       ListBuffer localListBuffer = this.pendingExits;
/* 2043 */       this.pendingExits = new ListBuffer();
/* 2044 */       int i = this.nextadr;
/* 2045 */       scanExpr(paramJCSwitch.selector);
/* 2046 */       Bits localBits1 = new Bits(this.inits);
/* 2047 */       Bits localBits2 = new Bits(this.uninits);
/* 2048 */       int j = 0;
/* 2049 */       for (List localList = paramJCSwitch.cases; localList.nonEmpty(); localList = localList.tail) {
/* 2050 */         this.inits.assign(localBits1);
/* 2051 */         this.uninits.assign(this.uninits.andSet(localBits2));
/* 2052 */         JCTree.JCCase localJCCase = (JCTree.JCCase)localList.head;
/* 2053 */         if (localJCCase.pat == null)
/* 2054 */           j = 1;
/*      */         else {
/* 2056 */           scanExpr(localJCCase.pat);
/*      */         }
/* 2058 */         if (j != 0) {
/* 2059 */           this.inits.assign(localBits1);
/* 2060 */           this.uninits.assign(this.uninits.andSet(localBits2));
/*      */         }
/* 2062 */         scan(localJCCase.stats);
/* 2063 */         addVars(localJCCase.stats, localBits1, localBits2);
/* 2064 */         if (j == 0) {
/* 2065 */           this.inits.assign(localBits1);
/* 2066 */           this.uninits.assign(this.uninits.andSet(localBits2));
/*      */         }
/*      */       }
/*      */ 
/* 2070 */       if (j == 0) {
/* 2071 */         this.inits.andSet(localBits1);
/*      */       }
/* 2073 */       resolveBreaks(paramJCSwitch, localListBuffer);
/* 2074 */       this.nextadr = i;
/*      */     }
/*      */ 
/*      */     private void addVars(List<JCTree.JCStatement> paramList, Bits paramBits1, Bits paramBits2)
/*      */     {
/* 2080 */       for (; paramList.nonEmpty(); paramList = paramList.tail) {
/* 2081 */         JCTree localJCTree = (JCTree)paramList.head;
/* 2082 */         if (localJCTree.hasTag(JCTree.Tag.VARDEF)) {
/* 2083 */           int i = ((JCTree.JCVariableDecl)localJCTree).sym.adr;
/* 2084 */           paramBits1.excl(i);
/* 2085 */           paramBits2.incl(i);
/*      */         }
/*      */       }
/*      */     }
/*      */ 
/*      */     public void visitTry(JCTree.JCTry paramJCTry) {
/* 2091 */       ListBuffer localListBuffer1 = new ListBuffer();
/* 2092 */       Bits localBits1 = new Bits(this.uninitsTry);
/* 2093 */       ListBuffer localListBuffer2 = this.pendingExits;
/* 2094 */       this.pendingExits = new ListBuffer();
/* 2095 */       Bits localBits2 = new Bits(this.inits);
/* 2096 */       this.uninitsTry.assign(this.uninits);
/* 2097 */       for (Object localObject1 = paramJCTry.resources.iterator(); ((Iterator)localObject1).hasNext(); ) { localObject2 = (JCTree)((Iterator)localObject1).next();
/* 2098 */         if ((localObject2 instanceof JCTree.JCVariableDecl)) {
/* 2099 */           JCTree.JCVariableDecl localJCVariableDecl = (JCTree.JCVariableDecl)localObject2;
/* 2100 */           visitVarDef(localJCVariableDecl);
/* 2101 */           this.unrefdResources.enter(localJCVariableDecl.sym);
/* 2102 */           localListBuffer1.append(localJCVariableDecl);
/* 2103 */         } else if ((localObject2 instanceof JCTree.JCExpression)) {
/* 2104 */           scanExpr((JCTree.JCExpression)localObject2);
/*      */         } else {
/* 2106 */           throw new AssertionError(paramJCTry);
/*      */         }
/*      */       }
/* 2109 */       scan(paramJCTry.body);
/* 2110 */       this.uninitsTry.andSet(this.uninits);
/* 2111 */       localObject1 = new Bits(this.inits);
/* 2112 */       Object localObject2 = new Bits(this.uninits);
/* 2113 */       int i = this.nextadr;
/*      */ 
/* 2115 */       if ((!localListBuffer1.isEmpty()) && 
/* 2116 */         (Flow.this.lint
/* 2116 */         .isEnabled(Lint.LintCategory.TRY))) {
/* 2117 */         for (localObject3 = localListBuffer1.iterator(); ((Iterator)localObject3).hasNext(); ) { localObject4 = (JCTree.JCVariableDecl)((Iterator)localObject3).next();
/* 2118 */           if (this.unrefdResources.includes(((JCTree.JCVariableDecl)localObject4).sym)) {
/* 2119 */             Flow.this.log.warning(Lint.LintCategory.TRY, ((JCTree.JCVariableDecl)localObject4).pos(), "try.resource.not.referenced", new Object[] { ((JCTree.JCVariableDecl)localObject4).sym });
/*      */ 
/* 2121 */             this.unrefdResources.remove(((JCTree.JCVariableDecl)localObject4).sym);
/*      */           }
/*      */ 
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/* 2130 */       Object localObject3 = new Bits(localBits2);
/* 2131 */       Object localObject4 = new Bits(this.uninitsTry);
/*      */       Object localObject6;
/* 2133 */       for (Object localObject5 = paramJCTry.catchers; ((List)localObject5).nonEmpty(); localObject5 = ((List)localObject5).tail) {
/* 2134 */         localObject6 = ((JCTree.JCCatch)((List)localObject5).head).param;
/* 2135 */         this.inits.assign((Bits)localObject3);
/* 2136 */         this.uninits.assign((Bits)localObject4);
/* 2137 */         scan((JCTree)localObject6);
/*      */ 
/* 2141 */         initParam((JCTree.JCVariableDecl)localObject6);
/* 2142 */         scan(((JCTree.JCCatch)((List)localObject5).head).body);
/* 2143 */         ((Bits)localObject1).andSet(this.inits);
/* 2144 */         ((Bits)localObject2).andSet(this.uninits);
/* 2145 */         this.nextadr = i;
/*      */       }
/* 2147 */       if (paramJCTry.finalizer != null) {
/* 2148 */         this.inits.assign(localBits2);
/* 2149 */         this.uninits.assign(this.uninitsTry);
/* 2150 */         localObject5 = this.pendingExits;
/* 2151 */         this.pendingExits = localListBuffer2;
/* 2152 */         scan(paramJCTry.finalizer);
/* 2153 */         if (paramJCTry.finallyCanCompleteNormally)
/*      */         {
/* 2156 */           this.uninits.andSet((Bits)localObject2);
/*      */ 
/* 2159 */           while (((ListBuffer)localObject5).nonEmpty()) {
/* 2160 */             localObject6 = (AssignPendingExit)((ListBuffer)localObject5).next();
/* 2161 */             if (((AssignPendingExit)localObject6).exit_inits != null) {
/* 2162 */               ((AssignPendingExit)localObject6).exit_inits.orSet(this.inits);
/* 2163 */               ((AssignPendingExit)localObject6).exit_uninits.andSet(this.uninits);
/*      */             }
/* 2165 */             this.pendingExits.append(localObject6);
/*      */           }
/* 2167 */           this.inits.orSet((Bits)localObject1);
/*      */         }
/*      */       } else {
/* 2170 */         this.inits.assign((Bits)localObject1);
/* 2171 */         this.uninits.assign((Bits)localObject2);
/* 2172 */         localObject5 = this.pendingExits;
/* 2173 */         this.pendingExits = localListBuffer2;
/* 2174 */         while (((ListBuffer)localObject5).nonEmpty()) this.pendingExits.append(((ListBuffer)localObject5).next());
/*      */       }
/* 2176 */       this.uninitsTry.andSet(localBits1).andSet(this.uninits);
/*      */     }
/*      */ 
/*      */     public void visitConditional(JCTree.JCConditional paramJCConditional) {
/* 2180 */       scanCond(paramJCConditional.cond);
/* 2181 */       Bits localBits1 = new Bits(this.initsWhenFalse);
/* 2182 */       Bits localBits2 = new Bits(this.uninitsWhenFalse);
/* 2183 */       this.inits.assign(this.initsWhenTrue);
/* 2184 */       this.uninits.assign(this.uninitsWhenTrue);
/*      */       Bits localBits3;
/*      */       Bits localBits4;
/* 2185 */       if ((paramJCConditional.truepart.type.hasTag(TypeTag.BOOLEAN)) && 
/* 2186 */         (paramJCConditional.falsepart.type
/* 2186 */         .hasTag(TypeTag.BOOLEAN)))
/*      */       {
/* 2191 */         scanCond(paramJCConditional.truepart);
/* 2192 */         localBits3 = new Bits(this.initsWhenTrue);
/* 2193 */         localBits4 = new Bits(this.initsWhenFalse);
/* 2194 */         Bits localBits5 = new Bits(this.uninitsWhenTrue);
/* 2195 */         Bits localBits6 = new Bits(this.uninitsWhenFalse);
/* 2196 */         this.inits.assign(localBits1);
/* 2197 */         this.uninits.assign(localBits2);
/* 2198 */         scanCond(paramJCConditional.falsepart);
/* 2199 */         this.initsWhenTrue.andSet(localBits3);
/* 2200 */         this.initsWhenFalse.andSet(localBits4);
/* 2201 */         this.uninitsWhenTrue.andSet(localBits5);
/* 2202 */         this.uninitsWhenFalse.andSet(localBits6);
/*      */       } else {
/* 2204 */         scanExpr(paramJCConditional.truepart);
/* 2205 */         localBits3 = new Bits(this.inits);
/* 2206 */         localBits4 = new Bits(this.uninits);
/* 2207 */         this.inits.assign(localBits1);
/* 2208 */         this.uninits.assign(localBits2);
/* 2209 */         scanExpr(paramJCConditional.falsepart);
/* 2210 */         this.inits.andSet(localBits3);
/* 2211 */         this.uninits.andSet(localBits4);
/*      */       }
/*      */     }
/*      */ 
/*      */     public void visitIf(JCTree.JCIf paramJCIf) {
/* 2216 */       scanCond(paramJCIf.cond);
/* 2217 */       Bits localBits1 = new Bits(this.initsWhenFalse);
/* 2218 */       Bits localBits2 = new Bits(this.uninitsWhenFalse);
/* 2219 */       this.inits.assign(this.initsWhenTrue);
/* 2220 */       this.uninits.assign(this.uninitsWhenTrue);
/* 2221 */       scan(paramJCIf.thenpart);
/* 2222 */       if (paramJCIf.elsepart != null) {
/* 2223 */         Bits localBits3 = new Bits(this.inits);
/* 2224 */         Bits localBits4 = new Bits(this.uninits);
/* 2225 */         this.inits.assign(localBits1);
/* 2226 */         this.uninits.assign(localBits2);
/* 2227 */         scan(paramJCIf.elsepart);
/* 2228 */         this.inits.andSet(localBits3);
/* 2229 */         this.uninits.andSet(localBits4);
/*      */       } else {
/* 2231 */         this.inits.andSet(localBits1);
/* 2232 */         this.uninits.andSet(localBits2);
/*      */       }
/*      */     }
/*      */ 
/*      */     public void visitBreak(JCTree.JCBreak paramJCBreak)
/*      */     {
/* 2238 */       recordExit(new AssignPendingExit(paramJCBreak, this.inits, this.uninits));
/*      */     }
/*      */ 
/*      */     public void visitContinue(JCTree.JCContinue paramJCContinue)
/*      */     {
/* 2243 */       recordExit(new AssignPendingExit(paramJCContinue, this.inits, this.uninits));
/*      */     }
/*      */ 
/*      */     public void visitReturn(JCTree.JCReturn paramJCReturn)
/*      */     {
/* 2248 */       scanExpr(paramJCReturn.expr);
/* 2249 */       recordExit(new AssignPendingExit(paramJCReturn, this.inits, this.uninits));
/*      */     }
/*      */ 
/*      */     public void visitThrow(JCTree.JCThrow paramJCThrow) {
/* 2253 */       scanExpr(paramJCThrow.expr);
/* 2254 */       markDead();
/*      */     }
/*      */ 
/*      */     public void visitApply(JCTree.JCMethodInvocation paramJCMethodInvocation) {
/* 2258 */       scanExpr(paramJCMethodInvocation.meth);
/* 2259 */       scanExprs(paramJCMethodInvocation.args);
/*      */     }
/*      */ 
/*      */     public void visitNewClass(JCTree.JCNewClass paramJCNewClass) {
/* 2263 */       scanExpr(paramJCNewClass.encl);
/* 2264 */       scanExprs(paramJCNewClass.args);
/* 2265 */       scan(paramJCNewClass.def);
/*      */     }
/*      */ 
/*      */     public void visitLambda(JCTree.JCLambda paramJCLambda)
/*      */     {
/* 2270 */       Bits localBits1 = new Bits(this.uninits);
/* 2271 */       Bits localBits2 = new Bits(this.inits);
/* 2272 */       int i = this.returnadr;
/* 2273 */       ListBuffer localListBuffer = this.pendingExits;
/*      */       try {
/* 2275 */         this.returnadr = this.nextadr;
/* 2276 */         this.pendingExits = new ListBuffer();
/* 2277 */         for (List localList = paramJCLambda.params; localList.nonEmpty(); localList = localList.tail) {
/* 2278 */           JCTree.JCVariableDecl localJCVariableDecl = (JCTree.JCVariableDecl)localList.head;
/* 2279 */           scan(localJCVariableDecl);
/* 2280 */           this.inits.incl(localJCVariableDecl.sym.adr);
/* 2281 */           this.uninits.excl(localJCVariableDecl.sym.adr);
/*      */         }
/* 2283 */         if (paramJCLambda.getBodyKind() == LambdaExpressionTree.BodyKind.EXPRESSION)
/* 2284 */           scanExpr(paramJCLambda.body);
/*      */         else
/* 2286 */           scan(paramJCLambda.body);
/*      */       }
/*      */       finally
/*      */       {
/* 2290 */         this.returnadr = i;
/* 2291 */         this.uninits.assign(localBits1);
/* 2292 */         this.inits.assign(localBits2);
/* 2293 */         this.pendingExits = localListBuffer;
/*      */       }
/*      */     }
/*      */ 
/*      */     public void visitNewArray(JCTree.JCNewArray paramJCNewArray) {
/* 2298 */       scanExprs(paramJCNewArray.dims);
/* 2299 */       scanExprs(paramJCNewArray.elems);
/*      */     }
/*      */ 
/*      */     public void visitAssert(JCTree.JCAssert paramJCAssert) {
/* 2303 */       Bits localBits1 = new Bits(this.inits);
/* 2304 */       Bits localBits2 = new Bits(this.uninits);
/* 2305 */       scanCond(paramJCAssert.cond);
/* 2306 */       localBits2.andSet(this.uninitsWhenTrue);
/* 2307 */       if (paramJCAssert.detail != null) {
/* 2308 */         this.inits.assign(this.initsWhenFalse);
/* 2309 */         this.uninits.assign(this.uninitsWhenFalse);
/* 2310 */         scanExpr(paramJCAssert.detail);
/*      */       }
/* 2312 */       this.inits.assign(localBits1);
/* 2313 */       this.uninits.assign(localBits2);
/*      */     }
/*      */ 
/*      */     public void visitAssign(JCTree.JCAssign paramJCAssign) {
/* 2317 */       JCTree.JCExpression localJCExpression = TreeInfo.skipParens(paramJCAssign.lhs);
/* 2318 */       if (!isIdentOrThisDotIdent(localJCExpression))
/* 2319 */         scanExpr(localJCExpression);
/* 2320 */       scanExpr(paramJCAssign.rhs);
/* 2321 */       letInit(localJCExpression);
/*      */     }
/*      */     private boolean isIdentOrThisDotIdent(JCTree paramJCTree) {
/* 2324 */       if (paramJCTree.hasTag(JCTree.Tag.IDENT))
/* 2325 */         return true;
/* 2326 */       if (!paramJCTree.hasTag(JCTree.Tag.SELECT)) {
/* 2327 */         return false;
/*      */       }
/* 2329 */       JCTree.JCFieldAccess localJCFieldAccess = (JCTree.JCFieldAccess)paramJCTree;
/*      */ 
/* 2331 */       return (localJCFieldAccess.selected.hasTag(JCTree.Tag.IDENT)) && 
/* 2331 */         (((JCTree.JCIdent)localJCFieldAccess.selected).name == Flow.this.names._this);
/*      */     }
/*      */ 
/*      */     public void visitSelect(JCTree.JCFieldAccess paramJCFieldAccess)
/*      */     {
/* 2337 */       super.visitSelect(paramJCFieldAccess);
/* 2338 */       if ((Flow.this.enforceThisDotInit) && 
/* 2339 */         (paramJCFieldAccess.selected
/* 2339 */         .hasTag(JCTree.Tag.IDENT)) && 
/* 2340 */         (((JCTree.JCIdent)paramJCFieldAccess.selected).name == Flow.this.names._this) && 
/* 2340 */         (paramJCFieldAccess.sym.kind == 4))
/*      */       {
/* 2343 */         checkInit(paramJCFieldAccess.pos(), (Symbol.VarSymbol)paramJCFieldAccess.sym);
/*      */       }
/*      */     }
/*      */ 
/*      */     public void visitAssignop(JCTree.JCAssignOp paramJCAssignOp) {
/* 2348 */       scanExpr(paramJCAssignOp.lhs);
/* 2349 */       scanExpr(paramJCAssignOp.rhs);
/* 2350 */       letInit(paramJCAssignOp.lhs);
/*      */     }
/*      */ 
/*      */     public void visitUnary(JCTree.JCUnary paramJCUnary) {
/* 2354 */       switch (Flow.2.$SwitchMap$com$sun$tools$javac$tree$JCTree$Tag[paramJCUnary.getTag().ordinal()]) {
/*      */       case 1:
/* 2356 */         scanCond(paramJCUnary.arg);
/* 2357 */         Bits localBits = new Bits(this.initsWhenFalse);
/* 2358 */         this.initsWhenFalse.assign(this.initsWhenTrue);
/* 2359 */         this.initsWhenTrue.assign(localBits);
/* 2360 */         localBits.assign(this.uninitsWhenFalse);
/* 2361 */         this.uninitsWhenFalse.assign(this.uninitsWhenTrue);
/* 2362 */         this.uninitsWhenTrue.assign(localBits);
/* 2363 */         break;
/*      */       case 2:
/*      */       case 3:
/*      */       case 4:
/*      */       case 5:
/* 2366 */         scanExpr(paramJCUnary.arg);
/* 2367 */         letInit(paramJCUnary.arg);
/* 2368 */         break;
/*      */       default:
/* 2370 */         scanExpr(paramJCUnary.arg);
/*      */       }
/*      */     }
/*      */ 
/*      */     public void visitBinary(JCTree.JCBinary paramJCBinary) {
/* 2375 */       switch (Flow.2.$SwitchMap$com$sun$tools$javac$tree$JCTree$Tag[paramJCBinary.getTag().ordinal()]) {
/*      */       case 6:
/* 2377 */         scanCond(paramJCBinary.lhs);
/* 2378 */         Bits localBits1 = new Bits(this.initsWhenFalse);
/* 2379 */         Bits localBits2 = new Bits(this.uninitsWhenFalse);
/* 2380 */         this.inits.assign(this.initsWhenTrue);
/* 2381 */         this.uninits.assign(this.uninitsWhenTrue);
/* 2382 */         scanCond(paramJCBinary.rhs);
/* 2383 */         this.initsWhenFalse.andSet(localBits1);
/* 2384 */         this.uninitsWhenFalse.andSet(localBits2);
/* 2385 */         break;
/*      */       case 7:
/* 2387 */         scanCond(paramJCBinary.lhs);
/* 2388 */         Bits localBits3 = new Bits(this.initsWhenTrue);
/* 2389 */         Bits localBits4 = new Bits(this.uninitsWhenTrue);
/* 2390 */         this.inits.assign(this.initsWhenFalse);
/* 2391 */         this.uninits.assign(this.uninitsWhenFalse);
/* 2392 */         scanCond(paramJCBinary.rhs);
/* 2393 */         this.initsWhenTrue.andSet(localBits3);
/* 2394 */         this.uninitsWhenTrue.andSet(localBits4);
/* 2395 */         break;
/*      */       default:
/* 2397 */         scanExpr(paramJCBinary.lhs);
/* 2398 */         scanExpr(paramJCBinary.rhs);
/*      */       }
/*      */     }
/*      */ 
/*      */     public void visitIdent(JCTree.JCIdent paramJCIdent) {
/* 2403 */       if (paramJCIdent.sym.kind == 4) {
/* 2404 */         checkInit(paramJCIdent.pos(), (Symbol.VarSymbol)paramJCIdent.sym);
/* 2405 */         referenced(paramJCIdent.sym);
/*      */       }
/*      */     }
/*      */ 
/*      */     void referenced(Symbol paramSymbol) {
/* 2410 */       this.unrefdResources.remove(paramSymbol);
/*      */     }
/*      */ 
/*      */     public void visitAnnotatedType(JCTree.JCAnnotatedType paramJCAnnotatedType)
/*      */     {
/* 2415 */       paramJCAnnotatedType.underlyingType.accept(this);
/*      */     }
/*      */ 
/*      */     public void visitTopLevel(JCTree.JCCompilationUnit paramJCCompilationUnit)
/*      */     {
/*      */     }
/*      */ 
/*      */     public void analyzeTree(Env<?> paramEnv)
/*      */     {
/* 2429 */       analyzeTree(paramEnv, paramEnv.tree);
/*      */     }
/*      */ 
/*      */     public void analyzeTree(Env<?> paramEnv, JCTree paramJCTree) {
/*      */       try {
/* 2434 */         this.startPos = paramJCTree.pos().getStartPosition();
/*      */         int i;
/* 2436 */         if (this.vardecls == null)
/* 2437 */           this.vardecls = new JCTree.JCVariableDecl[32];
/*      */         else
/* 2439 */           for (i = 0; i < this.vardecls.length; i++)
/* 2440 */             this.vardecls[i] = null;
/* 2441 */         this.firstadr = 0;
/* 2442 */         this.nextadr = 0;
/* 2443 */         this.pendingExits = new ListBuffer();
/* 2444 */         this.classDef = null;
/* 2445 */         this.unrefdResources = new Scope(paramEnv.enclClass.sym);
/* 2446 */         scan(paramJCTree);
/*      */ 
/* 2449 */         this.startPos = -1;
/* 2450 */         resetBits(new Bits[] { this.inits, this.uninits, this.uninitsTry, this.initsWhenTrue, this.initsWhenFalse, this.uninitsWhenTrue, this.uninitsWhenFalse });
/*      */ 
/* 2452 */         if (this.vardecls != null) {
/* 2453 */           for (i = 0; i < this.vardecls.length; i++)
/* 2454 */             this.vardecls[i] = null;
/*      */         }
/* 2456 */         this.firstadr = 0;
/* 2457 */         this.nextadr = 0;
/* 2458 */         this.pendingExits = null;
/* 2459 */         this.classDef = null;
/* 2460 */         this.unrefdResources = null;
/*      */       }
/*      */       finally
/*      */       {
/* 2449 */         this.startPos = -1;
/* 2450 */         resetBits(new Bits[] { this.inits, this.uninits, this.uninitsTry, this.initsWhenTrue, this.initsWhenFalse, this.uninitsWhenTrue, this.uninitsWhenFalse });
/*      */ 
/* 2452 */         if (this.vardecls != null) {
/* 2453 */           for (int j = 0; j < this.vardecls.length; j++)
/* 2454 */             this.vardecls[j] = null;
/*      */         }
/* 2456 */         this.firstadr = 0;
/* 2457 */         this.nextadr = 0;
/* 2458 */         this.pendingExits = null;
/* 2459 */         this.classDef = null;
/* 2460 */         this.unrefdResources = null;
/*      */       }
/*      */     }
/*      */ 
/*      */     public class AssignPendingExit extends Flow.BaseAnalyzer.PendingExit
/*      */     {
/*      */       final Bits inits;
/*      */       final Bits uninits;
/* 1443 */       final Bits exit_inits = new Bits(true);
/* 1444 */       final Bits exit_uninits = new Bits(true);
/*      */ 
/*      */       public AssignPendingExit(JCTree paramBits1, Bits paramBits2, Bits arg4) {
/* 1447 */         super();
/* 1448 */         this.inits = paramBits2;
/*      */         Bits localBits;
/* 1449 */         this.uninits = localBits;
/* 1450 */         this.exit_inits.assign(paramBits2);
/* 1451 */         this.exit_uninits.assign(localBits);
/*      */       }
/*      */ 
/*      */       void resolveJump()
/*      */       {
/* 1456 */         this.inits.andSet(this.exit_inits);
/* 1457 */         this.uninits.andSet(this.exit_uninits);
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   static abstract class BaseAnalyzer<P extends PendingExit> extends TreeScanner
/*      */   {
/*      */     ListBuffer<P> pendingExits;
/*      */ 
/*      */     abstract void markDead();
/*      */ 
/*      */     void recordExit(P paramP)
/*      */     {
/*  365 */       this.pendingExits.append(paramP);
/*  366 */       markDead();
/*      */     }
/*      */ 
/*      */     private boolean resolveJump(JCTree paramJCTree, ListBuffer<P> paramListBuffer, JumpKind paramJumpKind)
/*      */     {
/*  373 */       boolean bool = false;
/*  374 */       List localList = this.pendingExits.toList();
/*  375 */       this.pendingExits = paramListBuffer;
/*  376 */       for (; localList.nonEmpty(); localList = localList.tail) {
/*  377 */         PendingExit localPendingExit = (PendingExit)localList.head;
/*  378 */         if ((localPendingExit.tree.hasTag(paramJumpKind.treeTag)) && 
/*  379 */           (paramJumpKind
/*  379 */           .getTarget(localPendingExit.tree) == 
/*  379 */           paramJCTree)) {
/*  380 */           localPendingExit.resolveJump();
/*  381 */           bool = true;
/*      */         } else {
/*  383 */           this.pendingExits.append(localPendingExit);
/*      */         }
/*      */       }
/*  386 */       return bool;
/*      */     }
/*      */ 
/*      */     boolean resolveContinues(JCTree paramJCTree)
/*      */     {
/*  391 */       return resolveJump(paramJCTree, new ListBuffer(), JumpKind.CONTINUE);
/*      */     }
/*      */ 
/*      */     boolean resolveBreaks(JCTree paramJCTree, ListBuffer<P> paramListBuffer)
/*      */     {
/*  396 */       return resolveJump(paramJCTree, paramListBuffer, JumpKind.BREAK);
/*      */     }
/*      */ 
/*      */     public void scan(JCTree paramJCTree)
/*      */     {
/*  401 */       if ((paramJCTree != null) && ((paramJCTree.type == null) || (paramJCTree.type != Type.stuckType)))
/*      */       {
/*  404 */         super.scan(paramJCTree);
/*      */       }
/*      */     }
/*      */ 
/*      */     static abstract enum JumpKind
/*      */     {
/*  316 */       BREAK(JCTree.Tag.BREAK), 
/*      */ 
/*  322 */       CONTINUE(JCTree.Tag.CONTINUE);
/*      */ 
/*      */       final JCTree.Tag treeTag;
/*      */ 
/*      */       private JumpKind(JCTree.Tag paramTag)
/*      */       {
/*  332 */         this.treeTag = paramTag;
/*      */       }
/*      */ 
/*      */       abstract JCTree getTarget(JCTree paramJCTree);
/*      */     }
/*      */ 
/*      */     static class PendingExit
/*      */     {
/*      */       JCTree tree;
/*      */ 
/*      */       PendingExit(JCTree paramJCTree)
/*      */       {
/*  353 */         this.tree = paramJCTree;
/*      */       }
/*      */ 
/*      */       void resolveJump()
/*      */       {
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   class CaptureAnalyzer extends Flow.BaseAnalyzer<Flow.BaseAnalyzer.PendingExit>
/*      */   {
/*      */     JCTree currentTree;
/*      */ 
/*      */     CaptureAnalyzer()
/*      */     {
/*      */     }
/*      */ 
/*      */     void markDead()
/*      */     {
/*      */     }
/*      */ 
/*      */     void checkEffectivelyFinal(JCDiagnostic.DiagnosticPosition paramDiagnosticPosition, Symbol.VarSymbol paramVarSymbol)
/*      */     {
/* 2483 */       if ((this.currentTree != null) && (paramVarSymbol.owner.kind == 16))
/*      */       {
/* 2485 */         if (paramVarSymbol.pos < this.currentTree
/* 2485 */           .getStartPosition())
/* 2486 */           switch (Flow.2.$SwitchMap$com$sun$tools$javac$tree$JCTree$Tag[this.currentTree.getTag().ordinal()]) {
/*      */           case 8:
/* 2488 */             if (!Flow.this.allowEffectivelyFinalInInnerClasses) {
/* 2489 */               if ((paramVarSymbol.flags() & 0x10) != 0L) return;
/* 2490 */               reportInnerClsNeedsFinalError(paramDiagnosticPosition, paramVarSymbol); } break;
/*      */           case 9:
/* 2495 */             if ((paramVarSymbol.flags() & 0x10) == 0L)
/* 2496 */               reportEffectivelyFinalError(paramDiagnosticPosition, paramVarSymbol);
/*      */             break;
/*      */           }
/*      */       }
/*      */     }
/*      */ 
/*      */     void letInit(JCTree paramJCTree)
/*      */     {
/* 2504 */       paramJCTree = TreeInfo.skipParens(paramJCTree);
/* 2505 */       if ((paramJCTree.hasTag(JCTree.Tag.IDENT)) || (paramJCTree.hasTag(JCTree.Tag.SELECT))) {
/* 2506 */         Symbol localSymbol = TreeInfo.symbol(paramJCTree);
/* 2507 */         if ((this.currentTree != null) && (localSymbol.kind == 4) && (localSymbol.owner.kind == 16))
/*      */         {
/* 2510 */           if (((Symbol.VarSymbol)localSymbol).pos < this.currentTree
/* 2510 */             .getStartPosition())
/* 2511 */             switch (Flow.2.$SwitchMap$com$sun$tools$javac$tree$JCTree$Tag[this.currentTree.getTag().ordinal()]) {
/*      */             case 8:
/* 2513 */               if (!Flow.this.allowEffectivelyFinalInInnerClasses)
/* 2514 */                 reportInnerClsNeedsFinalError(paramJCTree, localSymbol);
/* 2515 */               break;
/*      */             case 9:
/* 2518 */               reportEffectivelyFinalError(paramJCTree, localSymbol);
/*      */             }
/*      */         }
/*      */       }
/*      */     }
/*      */ 
/*      */     void reportEffectivelyFinalError(JCDiagnostic.DiagnosticPosition paramDiagnosticPosition, Symbol paramSymbol) {
/* 2525 */       String str = this.currentTree.hasTag(JCTree.Tag.LAMBDA) ? "lambda" : "inner.cls";
/*      */ 
/* 2527 */       Flow.this.log.error(paramDiagnosticPosition, "cant.ref.non.effectively.final.var", new Object[] { paramSymbol, Flow.this.diags.fragment(str, new Object[0]) });
/*      */     }
/*      */ 
/*      */     void reportInnerClsNeedsFinalError(JCDiagnostic.DiagnosticPosition paramDiagnosticPosition, Symbol paramSymbol) {
/* 2531 */       Flow.this.log.error(paramDiagnosticPosition, "local.var.accessed.from.icls.needs.final", new Object[] { paramSymbol });
/*      */     }
/*      */ 
/*      */     public void visitClassDef(JCTree.JCClassDecl paramJCClassDecl)
/*      */     {
/* 2543 */       JCTree localJCTree = this.currentTree;
/*      */       try {
/* 2545 */         this.currentTree = (paramJCClassDecl.sym.isLocal() ? paramJCClassDecl : null);
/* 2546 */         super.visitClassDef(paramJCClassDecl);
/*      */ 
/* 2548 */         this.currentTree = localJCTree; } finally { this.currentTree = localJCTree; }
/*      */ 
/*      */     }
/*      */ 
/*      */     public void visitLambda(JCTree.JCLambda paramJCLambda)
/*      */     {
/* 2554 */       JCTree localJCTree = this.currentTree;
/*      */       try {
/* 2556 */         this.currentTree = paramJCLambda;
/* 2557 */         super.visitLambda(paramJCLambda);
/*      */ 
/* 2559 */         this.currentTree = localJCTree; } finally { this.currentTree = localJCTree; }
/*      */ 
/*      */     }
/*      */ 
/*      */     public void visitIdent(JCTree.JCIdent paramJCIdent)
/*      */     {
/* 2565 */       if (paramJCIdent.sym.kind == 4)
/* 2566 */         checkEffectivelyFinal(paramJCIdent, (Symbol.VarSymbol)paramJCIdent.sym);
/*      */     }
/*      */ 
/*      */     public void visitAssign(JCTree.JCAssign paramJCAssign)
/*      */     {
/* 2571 */       JCTree.JCExpression localJCExpression = TreeInfo.skipParens(paramJCAssign.lhs);
/* 2572 */       if (!(localJCExpression instanceof JCTree.JCIdent)) {
/* 2573 */         scan(localJCExpression);
/*      */       }
/* 2575 */       scan(paramJCAssign.rhs);
/* 2576 */       letInit(localJCExpression);
/*      */     }
/*      */ 
/*      */     public void visitAssignop(JCTree.JCAssignOp paramJCAssignOp) {
/* 2580 */       scan(paramJCAssignOp.lhs);
/* 2581 */       scan(paramJCAssignOp.rhs);
/* 2582 */       letInit(paramJCAssignOp.lhs);
/*      */     }
/*      */ 
/*      */     public void visitUnary(JCTree.JCUnary paramJCUnary) {
/* 2586 */       switch (Flow.2.$SwitchMap$com$sun$tools$javac$tree$JCTree$Tag[paramJCUnary.getTag().ordinal()]) { case 2:
/*      */       case 3:
/*      */       case 4:
/*      */       case 5:
/* 2589 */         scan(paramJCUnary.arg);
/* 2590 */         letInit(paramJCUnary.arg);
/* 2591 */         break;
/*      */       default:
/* 2593 */         scan(paramJCUnary.arg);
/*      */       }
/*      */     }
/*      */ 
/*      */     public void visitTopLevel(JCTree.JCCompilationUnit paramJCCompilationUnit)
/*      */     {
/*      */     }
/*      */ 
/*      */     public void analyzeTree(Env<AttrContext> paramEnv, TreeMaker paramTreeMaker)
/*      */     {
/* 2608 */       analyzeTree(paramEnv, paramEnv.tree, paramTreeMaker);
/*      */     }
/*      */     public void analyzeTree(Env<AttrContext> paramEnv, JCTree paramJCTree, TreeMaker paramTreeMaker) {
/*      */       try {
/* 2612 */         Flow.this.attrEnv = paramEnv;
/* 2613 */         Flow.this.make = paramTreeMaker;
/* 2614 */         this.pendingExits = new ListBuffer();
/* 2615 */         scan(paramJCTree);
/*      */       } finally {
/* 2617 */         this.pendingExits = null;
/* 2618 */         Flow.this.make = null;
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   class FlowAnalyzer extends Flow.BaseAnalyzer<FlowPendingExit>
/*      */   {
/*      */     HashMap<Symbol, List<Type>> preciseRethrowTypes;
/*      */     JCTree.JCClassDecl classDef;
/*      */     List<Type> thrown;
/*      */     List<Type> caught;
/*      */ 
/*      */     FlowAnalyzer()
/*      */     {
/*      */     }
/*      */ 
/*      */     void markDead()
/*      */     {
/*      */     }
/*      */ 
/*      */     void errorUncaught()
/*      */     {
/*  815 */       for (FlowPendingExit localFlowPendingExit = (FlowPendingExit)this.pendingExits.next(); 
/*  816 */         localFlowPendingExit != null; 
/*  817 */         localFlowPendingExit = (FlowPendingExit)this.pendingExits.next())
/*  818 */         if ((this.classDef != null) && (this.classDef.pos == localFlowPendingExit.tree.pos))
/*      */         {
/*  820 */           Flow.this.log.error(localFlowPendingExit.tree.pos(), "unreported.exception.default.constructor", new Object[] { localFlowPendingExit.thrown });
/*      */         }
/*  823 */         else if ((localFlowPendingExit.tree.hasTag(JCTree.Tag.VARDEF)) && 
/*  824 */           (((JCTree.JCVariableDecl)localFlowPendingExit.tree).sym
/*  824 */           .isResourceVariable())) {
/*  825 */           Flow.this.log.error(localFlowPendingExit.tree.pos(), "unreported.exception.implicit.close", new Object[] { localFlowPendingExit.thrown, ((JCTree.JCVariableDecl)localFlowPendingExit.tree).sym.name });
/*      */         }
/*      */         else
/*      */         {
/*  830 */           Flow.this.log.error(localFlowPendingExit.tree.pos(), "unreported.exception.need.to.catch.or.throw", new Object[] { localFlowPendingExit.thrown });
/*      */         }
/*      */     }
/*      */ 
/*      */     void markThrown(JCTree paramJCTree, Type paramType)
/*      */     {
/*  841 */       if (!Flow.this.chk.isUnchecked(paramJCTree.pos(), paramType)) {
/*  842 */         if (!Flow.this.chk.isHandled(paramType, this.caught)) {
/*  843 */           this.pendingExits.append(new FlowPendingExit(paramJCTree, paramType));
/*      */         }
/*  845 */         this.thrown = Flow.this.chk.incl(paramType, this.thrown);
/*      */       }
/*      */     }
/*      */ 
/*      */     public void visitClassDef(JCTree.JCClassDecl paramJCClassDecl)
/*      */     {
/*  856 */       if (paramJCClassDecl.sym == null) return;
/*      */ 
/*  858 */       JCTree.JCClassDecl localJCClassDecl = this.classDef;
/*  859 */       List localList1 = this.thrown;
/*  860 */       List localList2 = this.caught;
/*  861 */       ListBuffer localListBuffer = this.pendingExits;
/*  862 */       Lint localLint = Flow.this.lint;
/*      */ 
/*  864 */       this.pendingExits = new ListBuffer();
/*  865 */       if (paramJCClassDecl.name != Flow.this.names.empty) {
/*  866 */         this.caught = List.nil();
/*      */       }
/*  868 */       this.classDef = paramJCClassDecl;
/*  869 */       this.thrown = List.nil();
/*  870 */       Flow.this.lint = Flow.this.lint.augment(paramJCClassDecl.sym);
/*      */       try
/*      */       {
/*  874 */         for (List localList3 = paramJCClassDecl.defs; localList3.nonEmpty(); localList3 = localList3.tail)
/*  875 */           if ((!((JCTree)localList3.head).hasTag(JCTree.Tag.METHODDEF)) && 
/*  876 */             ((TreeInfo.flags((JCTree)localList3.head) & 
/*  876 */             0x8) != 0L)) {
/*  877 */             scan((JCTree)localList3.head);
/*  878 */             errorUncaught();
/*      */           }
/*      */         Object localObject1;
/*  884 */         if (paramJCClassDecl.name != Flow.this.names.empty) {
/*  885 */           int i = 1;
/*  886 */           for (localObject1 = paramJCClassDecl.defs; ((List)localObject1).nonEmpty(); localObject1 = ((List)localObject1).tail) {
/*  887 */             if (TreeInfo.isInitialConstructor((JCTree)((List)localObject1).head))
/*      */             {
/*  889 */               List localList5 = ((JCTree.JCMethodDecl)((List)localObject1).head).sym.type
/*  889 */                 .getThrownTypes();
/*  890 */               if (i != 0) {
/*  891 */                 this.caught = localList5;
/*  892 */                 i = 0;
/*      */               } else {
/*  894 */                 this.caught = Flow.this.chk.intersect(localList5, this.caught);
/*      */               }
/*      */             }
/*      */           }
/*      */ 
/*      */         }
/*      */ 
/*  901 */         for (List localList4 = paramJCClassDecl.defs; localList4.nonEmpty(); localList4 = localList4.tail) {
/*  902 */           if ((!((JCTree)localList4.head).hasTag(JCTree.Tag.METHODDEF)) && 
/*  903 */             ((TreeInfo.flags((JCTree)localList4.head) & 
/*  903 */             0x8) == 0L)) {
/*  904 */             scan((JCTree)localList4.head);
/*  905 */             errorUncaught();
/*      */           }
/*      */ 
/*      */         }
/*      */ 
/*  915 */         if (paramJCClassDecl.name == Flow.this.names.empty) {
/*  916 */           for (localList4 = paramJCClassDecl.defs; localList4.nonEmpty(); localList4 = localList4.tail) {
/*  917 */             if (TreeInfo.isInitialConstructor((JCTree)localList4.head)) {
/*  918 */               localObject1 = (JCTree.JCMethodDecl)localList4.head;
/*  919 */               ((JCTree.JCMethodDecl)localObject1).thrown = Flow.this.make.Types(this.thrown);
/*  920 */               ((JCTree.JCMethodDecl)localObject1).sym.type = Flow.this.types.createMethodTypeWithThrown(((JCTree.JCMethodDecl)localObject1).sym.type, this.thrown);
/*      */             }
/*      */           }
/*  923 */           localList1 = Flow.this.chk.union(this.thrown, localList1);
/*      */         }
/*      */ 
/*  927 */         for (localList4 = paramJCClassDecl.defs; localList4.nonEmpty(); localList4 = localList4.tail) {
/*  928 */           if (((JCTree)localList4.head).hasTag(JCTree.Tag.METHODDEF)) {
/*  929 */             scan((JCTree)localList4.head);
/*  930 */             errorUncaught();
/*      */           }
/*      */         }
/*      */ 
/*  934 */         this.thrown = localList1;
/*      */       } finally {
/*  936 */         this.pendingExits = localListBuffer;
/*  937 */         this.caught = localList2;
/*  938 */         this.classDef = localJCClassDecl;
/*  939 */         Flow.this.lint = localLint;
/*      */       }
/*      */     }
/*      */ 
/*      */     public void visitMethodDef(JCTree.JCMethodDecl paramJCMethodDecl) {
/*  944 */       if (paramJCMethodDecl.body == null) return;
/*      */ 
/*  946 */       List localList1 = this.caught;
/*  947 */       List localList2 = paramJCMethodDecl.sym.type.getThrownTypes();
/*  948 */       Lint localLint = Flow.this.lint;
/*      */ 
/*  950 */       Flow.this.lint = Flow.this.lint.augment(paramJCMethodDecl.sym);
/*      */ 
/*  952 */       Assert.check(this.pendingExits.isEmpty());
/*      */       try
/*      */       {
/*      */         Object localObject1;
/*  955 */         for (List localList3 = paramJCMethodDecl.params; localList3.nonEmpty(); localList3 = localList3.tail) {
/*  956 */           localObject1 = (JCTree.JCVariableDecl)localList3.head;
/*  957 */           scan((JCTree)localObject1);
/*      */         }
/*  959 */         if (TreeInfo.isInitialConstructor(paramJCMethodDecl))
/*  960 */           this.caught = Flow.this.chk.union(this.caught, localList2);
/*  961 */         else if ((paramJCMethodDecl.sym.flags() & 0x100008) != 1048576L) {
/*  962 */           this.caught = localList2;
/*      */         }
/*      */ 
/*  966 */         scan(paramJCMethodDecl.body);
/*      */ 
/*  968 */         localList3 = this.pendingExits.toList();
/*  969 */         this.pendingExits = new ListBuffer();
/*  970 */         while (localList3.nonEmpty()) {
/*  971 */           localObject1 = (FlowPendingExit)localList3.head;
/*  972 */           localList3 = localList3.tail;
/*  973 */           if (((FlowPendingExit)localObject1).thrown == null) {
/*  974 */             Assert.check(((FlowPendingExit)localObject1).tree.hasTag(JCTree.Tag.RETURN));
/*      */           }
/*      */           else
/*  977 */             this.pendingExits.append(localObject1);
/*      */         }
/*      */       }
/*      */       finally {
/*  981 */         this.caught = localList1;
/*  982 */         Flow.this.lint = localLint;
/*      */       }
/*      */     }
/*      */ 
/*      */     public void visitVarDef(JCTree.JCVariableDecl paramJCVariableDecl) {
/*  987 */       if (paramJCVariableDecl.init != null) {
/*  988 */         Lint localLint = Flow.this.lint;
/*  989 */         Flow.this.lint = Flow.this.lint.augment(paramJCVariableDecl.sym);
/*      */         try {
/*  991 */           scan(paramJCVariableDecl.init);
/*      */ 
/*  993 */           Flow.this.lint = localLint; } finally { Flow.this.lint = localLint; }
/*      */       }
/*      */     }
/*      */ 
/*      */     public void visitBlock(JCTree.JCBlock paramJCBlock)
/*      */     {
/*  999 */       scan(paramJCBlock.stats);
/*      */     }
/*      */ 
/*      */     public void visitDoLoop(JCTree.JCDoWhileLoop paramJCDoWhileLoop) {
/* 1003 */       ListBuffer localListBuffer = this.pendingExits;
/* 1004 */       this.pendingExits = new ListBuffer();
/* 1005 */       scan(paramJCDoWhileLoop.body);
/* 1006 */       resolveContinues(paramJCDoWhileLoop);
/* 1007 */       scan(paramJCDoWhileLoop.cond);
/* 1008 */       resolveBreaks(paramJCDoWhileLoop, localListBuffer);
/*      */     }
/*      */ 
/*      */     public void visitWhileLoop(JCTree.JCWhileLoop paramJCWhileLoop) {
/* 1012 */       ListBuffer localListBuffer = this.pendingExits;
/* 1013 */       this.pendingExits = new ListBuffer();
/* 1014 */       scan(paramJCWhileLoop.cond);
/* 1015 */       scan(paramJCWhileLoop.body);
/* 1016 */       resolveContinues(paramJCWhileLoop);
/* 1017 */       resolveBreaks(paramJCWhileLoop, localListBuffer);
/*      */     }
/*      */ 
/*      */     public void visitForLoop(JCTree.JCForLoop paramJCForLoop) {
/* 1021 */       ListBuffer localListBuffer = this.pendingExits;
/* 1022 */       scan(paramJCForLoop.init);
/* 1023 */       this.pendingExits = new ListBuffer();
/* 1024 */       if (paramJCForLoop.cond != null) {
/* 1025 */         scan(paramJCForLoop.cond);
/*      */       }
/* 1027 */       scan(paramJCForLoop.body);
/* 1028 */       resolveContinues(paramJCForLoop);
/* 1029 */       scan(paramJCForLoop.step);
/* 1030 */       resolveBreaks(paramJCForLoop, localListBuffer);
/*      */     }
/*      */ 
/*      */     public void visitForeachLoop(JCTree.JCEnhancedForLoop paramJCEnhancedForLoop) {
/* 1034 */       visitVarDef(paramJCEnhancedForLoop.var);
/* 1035 */       ListBuffer localListBuffer = this.pendingExits;
/* 1036 */       scan(paramJCEnhancedForLoop.expr);
/* 1037 */       this.pendingExits = new ListBuffer();
/* 1038 */       scan(paramJCEnhancedForLoop.body);
/* 1039 */       resolveContinues(paramJCEnhancedForLoop);
/* 1040 */       resolveBreaks(paramJCEnhancedForLoop, localListBuffer);
/*      */     }
/*      */ 
/*      */     public void visitLabelled(JCTree.JCLabeledStatement paramJCLabeledStatement) {
/* 1044 */       ListBuffer localListBuffer = this.pendingExits;
/* 1045 */       this.pendingExits = new ListBuffer();
/* 1046 */       scan(paramJCLabeledStatement.body);
/* 1047 */       resolveBreaks(paramJCLabeledStatement, localListBuffer);
/*      */     }
/*      */ 
/*      */     public void visitSwitch(JCTree.JCSwitch paramJCSwitch) {
/* 1051 */       ListBuffer localListBuffer = this.pendingExits;
/* 1052 */       this.pendingExits = new ListBuffer();
/* 1053 */       scan(paramJCSwitch.selector);
/* 1054 */       for (List localList = paramJCSwitch.cases; localList.nonEmpty(); localList = localList.tail) {
/* 1055 */         JCTree.JCCase localJCCase = (JCTree.JCCase)localList.head;
/* 1056 */         if (localJCCase.pat != null) {
/* 1057 */           scan(localJCCase.pat);
/*      */         }
/* 1059 */         scan(localJCCase.stats);
/*      */       }
/* 1061 */       resolveBreaks(paramJCSwitch, localListBuffer);
/*      */     }
/*      */ 
/*      */     public void visitTry(JCTree.JCTry paramJCTry) {
/* 1065 */       List localList1 = this.caught;
/* 1066 */       List localList2 = this.thrown;
/* 1067 */       this.thrown = List.nil();
/* 1068 */       for (Object localObject1 = paramJCTry.catchers; ((List)localObject1).nonEmpty(); localObject1 = ((List)localObject1).tail)
/*      */       {
/* 1071 */         localObject2 = TreeInfo.isMultiCatch((JCTree.JCCatch)((List)localObject1).head) ? ((JCTree.JCTypeUnion)((JCTree.JCCatch)((List)localObject1).head).param.vartype).alternatives : 
/* 1071 */           List.of(((JCTree.JCCatch)((List)localObject1).head).param.vartype);
/*      */ 
/* 1072 */         for (localObject3 = ((List)localObject2).iterator(); ((Iterator)localObject3).hasNext(); ) { localObject4 = (JCTree.JCExpression)((Iterator)localObject3).next();
/* 1073 */           this.caught = Flow.this.chk.incl(((JCTree.JCExpression)localObject4).type, this.caught);
/*      */         }
/*      */       }
/*      */ 
/* 1077 */       localObject1 = this.pendingExits;
/* 1078 */       this.pendingExits = new ListBuffer();
/* 1079 */       for (Object localObject2 = paramJCTry.resources.iterator(); ((Iterator)localObject2).hasNext(); ) { localObject3 = (JCTree)((Iterator)localObject2).next();
/* 1080 */         if ((localObject3 instanceof JCTree.JCVariableDecl)) {
/* 1081 */           localObject4 = (JCTree.JCVariableDecl)localObject3;
/* 1082 */           visitVarDef((JCTree.JCVariableDecl)localObject4);
/* 1083 */         } else if ((localObject3 instanceof JCTree.JCExpression)) {
/* 1084 */           scan((JCTree.JCExpression)localObject3);
/*      */         } else {
/* 1086 */           throw new AssertionError(paramJCTry);
/*      */         }
/*      */       }
/* 1089 */       for (localObject2 = paramJCTry.resources.iterator(); ((Iterator)localObject2).hasNext(); ) { localObject3 = (JCTree)((Iterator)localObject2).next();
/*      */ 
/* 1092 */         localObject4 = ((JCTree)localObject3).type.isCompound() ? Flow.this.types
/* 1091 */           .interfaces(((JCTree)localObject3).type).prepend(Flow.this.types.supertype(((JCTree)localObject3).type)) : 
/* 1092 */           List.of(((JCTree)localObject3).type);
/*      */ 
/* 1093 */         for (localObject5 = ((List)localObject4).iterator(); ((Iterator)localObject5).hasNext(); ) { localObject6 = (Type)((Iterator)localObject5).next();
/* 1094 */           if (Flow.this.types.asSuper((Type)localObject6, Flow.this.syms.autoCloseableType.tsym) != null) {
/* 1095 */             localObject7 = Flow.this.rs.resolveQualifiedMethod(paramJCTry, Flow.this.attrEnv, 
/* 1096 */               (Type)localObject6, Flow.this.names.close, 
/* 1099 */               List.nil(), 
/* 1100 */               List.nil());
/* 1101 */             localObject8 = Flow.this.types.memberType(((JCTree)localObject3).type, (Symbol)localObject7);
/* 1102 */             if (((Symbol)localObject7).kind == 16)
/* 1103 */               for (localIterator = ((Type)localObject8).getThrownTypes().iterator(); localIterator.hasNext(); ) { localObject9 = (Type)localIterator.next();
/* 1104 */                 markThrown((JCTree)localObject3, (Type)localObject9);
/*      */               }
/*      */           }
/*      */         }
/*      */       }
/*      */       Object localObject5;
/*      */       Object localObject6;
/*      */       Object localObject7;
/*      */       Object localObject8;
/*      */       Iterator localIterator;
/*      */       Object localObject9;
/* 1110 */       scan(paramJCTry.body);
/*      */ 
/* 1112 */       localObject2 = Flow.this.allowImprovedCatchAnalysis ? Flow.this.chk
/* 1112 */         .union(this.thrown, List.of(Flow.this.syms.runtimeExceptionType, Flow.this.syms.errorType)) : this.thrown;
/*      */ 
/* 1114 */       this.thrown = localList2;
/* 1115 */       this.caught = localList1;
/*      */ 
/* 1117 */       Object localObject3 = List.nil();
/* 1118 */       for (Object localObject4 = paramJCTry.catchers; ((List)localObject4).nonEmpty(); localObject4 = ((List)localObject4).tail) {
/* 1119 */         localObject5 = ((JCTree.JCCatch)((List)localObject4).head).param;
/*      */ 
/* 1122 */         localObject6 = TreeInfo.isMultiCatch((JCTree.JCCatch)((List)localObject4).head) ? ((JCTree.JCTypeUnion)((JCTree.JCCatch)((List)localObject4).head).param.vartype).alternatives : 
/* 1122 */           List.of(((JCTree.JCCatch)((List)localObject4).head).param.vartype);
/*      */ 
/* 1123 */         localObject7 = List.nil();
/* 1124 */         localObject8 = Flow.this.chk.diff((List)localObject2, (List)localObject3);
/* 1125 */         for (localIterator = ((List)localObject6).iterator(); localIterator.hasNext(); ) { localObject9 = (JCTree.JCExpression)localIterator.next();
/* 1126 */           Type localType = ((JCTree.JCExpression)localObject9).type;
/* 1127 */           if (localType != Flow.this.syms.unknownType) {
/* 1128 */             localObject7 = ((List)localObject7).append(localType);
/* 1129 */             if (!Flow.this.types.isSameType(localType, Flow.this.syms.objectType))
/*      */             {
/* 1131 */               checkCaughtType(((JCTree.JCCatch)((List)localObject4).head).pos(), localType, (List)localObject2, (List)localObject3);
/* 1132 */               localObject3 = Flow.this.chk.incl(localType, (List)localObject3);
/*      */             }
/*      */           } }
/* 1135 */         scan((JCTree)localObject5);
/* 1136 */         this.preciseRethrowTypes.put(((JCTree.JCVariableDecl)localObject5).sym, Flow.this.chk.intersect((List)localObject7, (List)localObject8));
/* 1137 */         scan(((JCTree.JCCatch)((List)localObject4).head).body);
/* 1138 */         this.preciseRethrowTypes.remove(((JCTree.JCVariableDecl)localObject5).sym);
/*      */       }
/* 1140 */       if (paramJCTry.finalizer != null) {
/* 1141 */         localObject4 = this.thrown;
/* 1142 */         this.thrown = List.nil();
/* 1143 */         localObject5 = this.pendingExits;
/* 1144 */         this.pendingExits = ((ListBuffer)localObject1);
/* 1145 */         scan(paramJCTry.finalizer);
/* 1146 */         if (!paramJCTry.finallyCanCompleteNormally)
/*      */         {
/* 1148 */           this.thrown = Flow.this.chk.union(this.thrown, localList2);
/*      */         } else {
/* 1150 */           this.thrown = Flow.this.chk.union(this.thrown, Flow.this.chk.diff((List)localObject2, (List)localObject3));
/* 1151 */           this.thrown = Flow.this.chk.union(this.thrown, (List)localObject4);
/*      */ 
/* 1154 */           while (((ListBuffer)localObject5).nonEmpty())
/* 1155 */             this.pendingExits.append(((ListBuffer)localObject5).next());
/*      */         }
/*      */       }
/*      */       else {
/* 1159 */         this.thrown = Flow.this.chk.union(this.thrown, Flow.this.chk.diff((List)localObject2, (List)localObject3));
/* 1160 */         localObject4 = this.pendingExits;
/* 1161 */         this.pendingExits = ((ListBuffer)localObject1);
/* 1162 */         while (((ListBuffer)localObject4).nonEmpty()) this.pendingExits.append(((ListBuffer)localObject4).next());
/*      */       }
/*      */     }
/*      */ 
/*      */     public void visitIf(JCTree.JCIf paramJCIf)
/*      */     {
/* 1168 */       scan(paramJCIf.cond);
/* 1169 */       scan(paramJCIf.thenpart);
/* 1170 */       if (paramJCIf.elsepart != null)
/* 1171 */         scan(paramJCIf.elsepart);
/*      */     }
/*      */ 
/*      */     void checkCaughtType(JCDiagnostic.DiagnosticPosition paramDiagnosticPosition, Type paramType, List<Type> paramList1, List<Type> paramList2)
/*      */     {
/* 1176 */       if (Flow.this.chk.subset(paramType, paramList2)) {
/* 1177 */         Flow.this.log.error(paramDiagnosticPosition, "except.already.caught", new Object[] { paramType });
/* 1178 */       } else if ((!Flow.this.chk.isUnchecked(paramDiagnosticPosition, paramType)) && 
/* 1179 */         (!isExceptionOrThrowable(paramType)) && 
/* 1180 */         (!Flow.this.chk
/* 1180 */         .intersects(paramType, paramList1))) {
/* 1181 */         Flow.this.log.error(paramDiagnosticPosition, "except.never.thrown.in.try", new Object[] { paramType });
/* 1182 */       } else if (Flow.this.allowImprovedCatchAnalysis) {
/* 1183 */         List localList = Flow.this.chk.intersect(List.of(paramType), paramList1);
/*      */ 
/* 1188 */         if ((Flow.this.chk.diff(localList, paramList2).isEmpty()) && 
/* 1189 */           (!isExceptionOrThrowable(paramType)))
/*      */         {
/* 1190 */           String str = localList.length() == 1 ? "unreachable.catch" : "unreachable.catch.1";
/*      */ 
/* 1193 */           Flow.this.log.warning(paramDiagnosticPosition, str, new Object[] { localList });
/*      */         }
/*      */       }
/*      */     }
/*      */ 
/*      */     private boolean isExceptionOrThrowable(Type paramType)
/*      */     {
/* 1200 */       return (paramType.tsym == Flow.this.syms.throwableType.tsym) || 
/* 1200 */         (paramType.tsym == Flow.this.syms.exceptionType.tsym);
/*      */     }
/*      */ 
/*      */     public void visitBreak(JCTree.JCBreak paramJCBreak)
/*      */     {
/* 1204 */       recordExit(new FlowPendingExit(paramJCBreak, null));
/*      */     }
/*      */ 
/*      */     public void visitContinue(JCTree.JCContinue paramJCContinue) {
/* 1208 */       recordExit(new FlowPendingExit(paramJCContinue, null));
/*      */     }
/*      */ 
/*      */     public void visitReturn(JCTree.JCReturn paramJCReturn) {
/* 1212 */       scan(paramJCReturn.expr);
/* 1213 */       recordExit(new FlowPendingExit(paramJCReturn, null));
/*      */     }
/*      */ 
/*      */     public void visitThrow(JCTree.JCThrow paramJCThrow) {
/* 1217 */       scan(paramJCThrow.expr);
/* 1218 */       Symbol localSymbol = TreeInfo.symbol(paramJCThrow.expr);
/* 1219 */       if ((localSymbol != null) && (localSymbol.kind == 4))
/*      */       {
/* 1221 */         if (((localSymbol
/* 1221 */           .flags() & 0x10) != 0L) && 
/* 1222 */           (this.preciseRethrowTypes
/* 1222 */           .get(localSymbol) != null) && 
/* 1223 */           (Flow.this.allowImprovedRethrowAnalysis))
/*      */         {
/* 1224 */           for (Type localType : (List)this.preciseRethrowTypes.get(localSymbol))
/* 1225 */             markThrown(paramJCThrow, localType);
/* 1226 */           break label122;
/*      */         }
/*      */       }
/* 1229 */       markThrown(paramJCThrow, paramJCThrow.expr.type);
/*      */ 
/* 1231 */       label122: markDead();
/*      */     }
/*      */ 
/*      */     public void visitApply(JCTree.JCMethodInvocation paramJCMethodInvocation) {
/* 1235 */       scan(paramJCMethodInvocation.meth);
/* 1236 */       scan(paramJCMethodInvocation.args);
/* 1237 */       for (List localList = paramJCMethodInvocation.meth.type.getThrownTypes(); localList.nonEmpty(); localList = localList.tail)
/* 1238 */         markThrown(paramJCMethodInvocation, (Type)localList.head);
/*      */     }
/*      */ 
/*      */     public void visitNewClass(JCTree.JCNewClass paramJCNewClass) {
/* 1242 */       scan(paramJCNewClass.encl);
/* 1243 */       scan(paramJCNewClass.args);
/*      */ 
/* 1245 */       for (List localList1 = paramJCNewClass.constructorType.getThrownTypes(); 
/* 1246 */         localList1.nonEmpty(); 
/* 1247 */         localList1 = localList1.tail) {
/* 1248 */         markThrown(paramJCNewClass, (Type)localList1.head);
/*      */       }
/* 1250 */       localList1 = this.caught;
/*      */       try
/*      */       {
/* 1260 */         if (paramJCNewClass.def != null) {
/* 1261 */           for (List localList2 = paramJCNewClass.constructor.type.getThrownTypes(); 
/* 1262 */             localList2.nonEmpty(); 
/* 1263 */             localList2 = localList2.tail)
/* 1264 */             this.caught = Flow.this.chk.incl((Type)localList2.head, this.caught);
/*      */         }
/* 1266 */         scan(paramJCNewClass.def);
/*      */       }
/*      */       finally {
/* 1269 */         this.caught = localList1;
/*      */       }
/*      */     }
/*      */ 
/*      */     public void visitLambda(JCTree.JCLambda paramJCLambda)
/*      */     {
/* 1275 */       if ((paramJCLambda.type != null) && 
/* 1276 */         (paramJCLambda.type
/* 1276 */         .isErroneous())) {
/* 1277 */         return;
/*      */       }
/* 1279 */       List localList1 = this.caught;
/* 1280 */       List localList2 = this.thrown;
/* 1281 */       ListBuffer localListBuffer = this.pendingExits;
/*      */       try {
/* 1283 */         this.pendingExits = new ListBuffer();
/* 1284 */         this.caught = paramJCLambda.getDescriptorType(Flow.this.types).getThrownTypes();
/* 1285 */         this.thrown = List.nil();
/* 1286 */         scan(paramJCLambda.body);
/* 1287 */         List localList3 = this.pendingExits.toList();
/* 1288 */         this.pendingExits = new ListBuffer();
/* 1289 */         while (localList3.nonEmpty()) {
/* 1290 */           FlowPendingExit localFlowPendingExit = (FlowPendingExit)localList3.head;
/* 1291 */           localList3 = localList3.tail;
/* 1292 */           if (localFlowPendingExit.thrown == null) {
/* 1293 */             Assert.check(localFlowPendingExit.tree.hasTag(JCTree.Tag.RETURN));
/*      */           }
/*      */           else {
/* 1296 */             this.pendingExits.append(localFlowPendingExit);
/*      */           }
/*      */         }
/*      */ 
/* 1300 */         errorUncaught();
/*      */       } finally {
/* 1302 */         this.pendingExits = localListBuffer;
/* 1303 */         this.caught = localList1;
/* 1304 */         this.thrown = localList2;
/*      */       }
/*      */     }
/*      */ 
/*      */     public void visitTopLevel(JCTree.JCCompilationUnit paramJCCompilationUnit)
/*      */     {
/*      */     }
/*      */ 
/*      */     public void analyzeTree(Env<AttrContext> paramEnv, TreeMaker paramTreeMaker)
/*      */     {
/* 1319 */       analyzeTree(paramEnv, paramEnv.tree, paramTreeMaker);
/*      */     }
/*      */     public void analyzeTree(Env<AttrContext> paramEnv, JCTree paramJCTree, TreeMaker paramTreeMaker) {
/*      */       try {
/* 1323 */         Flow.this.attrEnv = paramEnv;
/* 1324 */         Flow.this.make = paramTreeMaker;
/* 1325 */         this.pendingExits = new ListBuffer();
/* 1326 */         this.preciseRethrowTypes = new HashMap();
/* 1327 */         this.thrown = (this.caught = null);
/* 1328 */         this.classDef = null;
/* 1329 */         scan(paramJCTree);
/*      */       } finally {
/* 1331 */         this.pendingExits = null;
/* 1332 */         Flow.this.make = null;
/* 1333 */         this.thrown = (this.caught = null);
/* 1334 */         this.classDef = null;
/*      */       }
/*      */     }
/*      */ 
/*      */     class FlowPendingExit extends Flow.BaseAnalyzer.PendingExit
/*      */     {
/*      */       Type thrown;
/*      */ 
/*      */       FlowPendingExit(JCTree paramType, Type arg3)
/*      */       {
/*  800 */         super();
/*      */         Object localObject;
/*  801 */         this.thrown = localObject;
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   static enum FlowKind
/*      */   {
/*  272 */     NORMAL("var.might.already.be.assigned", false), 
/*      */ 
/*  277 */     SPECULATIVE_LOOP("var.might.be.assigned.in.loop", true);
/*      */ 
/*      */     final String errKey;
/*      */     final boolean isFinal;
/*      */ 
/*  283 */     private FlowKind(String paramString, boolean paramBoolean) { this.errKey = paramString;
/*  284 */       this.isFinal = paramBoolean; }
/*      */ 
/*      */     boolean isFinal()
/*      */     {
/*  288 */       return this.isFinal;
/*      */     }
/*      */   }
/*      */ 
/*      */   class LambdaFlowAnalyzer extends Flow.FlowAnalyzer
/*      */   {
/*      */     List<Type> inferredThrownTypes;
/*      */     boolean inLambda;
/*      */ 
/*      */     LambdaFlowAnalyzer()
/*      */     {
/* 1342 */       super();
/*      */     }
/*      */ 
/*      */     public void visitLambda(JCTree.JCLambda paramJCLambda)
/*      */     {
/* 1347 */       if (((paramJCLambda.type != null) && 
/* 1348 */         (paramJCLambda.type
/* 1348 */         .isErroneous())) || (this.inLambda)) {
/* 1349 */         return;
/*      */       }
/* 1351 */       List localList1 = this.caught;
/* 1352 */       List localList2 = this.thrown;
/* 1353 */       ListBuffer localListBuffer = this.pendingExits;
/* 1354 */       this.inLambda = true;
/*      */       try {
/* 1356 */         this.pendingExits = new ListBuffer();
/* 1357 */         this.caught = List.of(Flow.this.syms.throwableType);
/* 1358 */         this.thrown = List.nil();
/* 1359 */         scan(paramJCLambda.body);
/* 1360 */         this.inferredThrownTypes = this.thrown;
/*      */       } finally {
/* 1362 */         this.pendingExits = localListBuffer;
/* 1363 */         this.caught = localList1;
/* 1364 */         this.thrown = localList2;
/* 1365 */         this.inLambda = false;
/*      */       }
/*      */     }
/*      */ 
/*      */     public void visitClassDef(JCTree.JCClassDecl paramJCClassDecl)
/*      */     {
/*      */     }
/*      */   }
/*      */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.javac.comp.Flow
 * JD-Core Version:    0.6.2
 */