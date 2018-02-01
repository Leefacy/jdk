/*      */ package com.sun.tools.javac.comp;
/*      */ 
/*      */ import com.sun.source.tree.LambdaExpressionTree.BodyKind;
/*      */ import com.sun.tools.javac.code.Scope;
/*      */ import com.sun.tools.javac.code.Symbol;
/*      */ import com.sun.tools.javac.code.Symbol.ClassSymbol;
/*      */ import com.sun.tools.javac.code.Symbol.MethodSymbol;
/*      */ import com.sun.tools.javac.code.Symtab;
/*      */ import com.sun.tools.javac.code.Type;
/*      */ import com.sun.tools.javac.code.Type.ForAll;
/*      */ import com.sun.tools.javac.code.Type.Mapping;
/*      */ import com.sun.tools.javac.code.TypeTag;
/*      */ import com.sun.tools.javac.code.Types;
/*      */ import com.sun.tools.javac.code.Types.FunctionDescriptorLookupError;
/*      */ import com.sun.tools.javac.tree.JCTree;
/*      */ import com.sun.tools.javac.tree.JCTree.JCAnnotatedType;
/*      */ import com.sun.tools.javac.tree.JCTree.JCBlock;
/*      */ import com.sun.tools.javac.tree.JCTree.JCClassDecl;
/*      */ import com.sun.tools.javac.tree.JCTree.JCConditional;
/*      */ import com.sun.tools.javac.tree.JCTree.JCExpression;
/*      */ import com.sun.tools.javac.tree.JCTree.JCFieldAccess;
/*      */ import com.sun.tools.javac.tree.JCTree.JCIdent;
/*      */ import com.sun.tools.javac.tree.JCTree.JCLambda;
/*      */ import com.sun.tools.javac.tree.JCTree.JCLambda.ParameterKind;
/*      */ import com.sun.tools.javac.tree.JCTree.JCLiteral;
/*      */ import com.sun.tools.javac.tree.JCTree.JCMemberReference;
/*      */ import com.sun.tools.javac.tree.JCTree.JCMemberReference.OverloadKind;
/*      */ import com.sun.tools.javac.tree.JCTree.JCMethodInvocation;
/*      */ import com.sun.tools.javac.tree.JCTree.JCNewClass;
/*      */ import com.sun.tools.javac.tree.JCTree.JCReturn;
/*      */ import com.sun.tools.javac.tree.JCTree.JCTypeCast;
/*      */ import com.sun.tools.javac.tree.JCTree.JCVariableDecl;
/*      */ import com.sun.tools.javac.tree.JCTree.Tag;
/*      */ import com.sun.tools.javac.tree.JCTree.Visitor;
/*      */ import com.sun.tools.javac.tree.TreeCopier;
/*      */ import com.sun.tools.javac.tree.TreeInfo;
/*      */ import com.sun.tools.javac.tree.TreeMaker;
/*      */ import com.sun.tools.javac.tree.TreeScanner;
/*      */ import com.sun.tools.javac.util.Assert;
/*      */ import com.sun.tools.javac.util.Context;
/*      */ import com.sun.tools.javac.util.Context.Key;
/*      */ import com.sun.tools.javac.util.Filter;
/*      */ import com.sun.tools.javac.util.JCDiagnostic;
/*      */ import com.sun.tools.javac.util.JCDiagnostic.DiagnosticPosition;
/*      */ import com.sun.tools.javac.util.JCDiagnostic.Factory;
/*      */ import com.sun.tools.javac.util.List;
/*      */ import com.sun.tools.javac.util.ListBuffer;
/*      */ import com.sun.tools.javac.util.Log;
/*      */ import com.sun.tools.javac.util.Log.DeferredDiagnosticHandler;
/*      */ import com.sun.tools.javac.util.Log.DiscardDiagnosticHandler;
/*      */ import com.sun.tools.javac.util.Name;
/*      */ import com.sun.tools.javac.util.Name.Table;
/*      */ import com.sun.tools.javac.util.Names;
/*      */ import com.sun.tools.javac.util.Warner;
/*      */ import java.util.ArrayList;
/*      */ import java.util.Collections;
/*      */ import java.util.EnumSet;
/*      */ import java.util.Iterator;
/*      */ import java.util.LinkedHashMap;
/*      */ import java.util.LinkedHashSet;
/*      */ import java.util.Map;
/*      */ import java.util.Set;
/*      */ import java.util.WeakHashMap;
/*      */ 
/*      */ public class DeferredAttr extends JCTree.Visitor
/*      */ {
/*   65 */   protected static final Context.Key<DeferredAttr> deferredAttrKey = new Context.Key();
/*      */   final Attr attr;
/*      */   final Check chk;
/*      */   final JCDiagnostic.Factory diags;
/*      */   final Enter enter;
/*      */   final Infer infer;
/*      */   final Resolve rs;
/*      */   final Log log;
/*      */   final Symtab syms;
/*      */   final TreeMaker make;
/*      */   final Types types;
/*      */   final Flow flow;
/*      */   final Names names;
/*      */   final TypeEnvs typeEnvs;
/*      */   final JCTree stuckTree;
/*  273 */   DeferredTypeCompleter basicCompleter = new DeferredTypeCompleter() {
/*      */     public Type complete(DeferredAttr.DeferredType paramAnonymousDeferredType, Attr.ResultInfo paramAnonymousResultInfo, DeferredAttr.DeferredAttrContext paramAnonymousDeferredAttrContext) {
/*  275 */       switch (DeferredAttr.6.$SwitchMap$com$sun$tools$javac$comp$DeferredAttr$AttrMode[paramAnonymousDeferredAttrContext.mode.ordinal()])
/*      */       {
/*      */       case 1:
/*  279 */         Assert.check((paramAnonymousDeferredType.mode == null) || (paramAnonymousDeferredType.mode == DeferredAttr.AttrMode.SPECULATIVE));
/*  280 */         JCTree localJCTree = DeferredAttr.this.attribSpeculative(paramAnonymousDeferredType.tree, paramAnonymousDeferredType.env, paramAnonymousResultInfo);
/*  281 */         paramAnonymousDeferredType.speculativeCache.put(localJCTree, paramAnonymousResultInfo);
/*  282 */         return localJCTree.type;
/*      */       case 2:
/*  284 */         Assert.check(paramAnonymousDeferredType.mode != null);
/*  285 */         return DeferredAttr.this.attr.attribTree(paramAnonymousDeferredType.tree, paramAnonymousDeferredType.env, paramAnonymousResultInfo);
/*      */       }
/*  287 */       Assert.error();
/*  288 */       return null;
/*      */     }
/*  273 */   };
/*      */ 
/*  292 */   DeferredTypeCompleter dummyCompleter = new DeferredTypeCompleter() {
/*      */     public Type complete(DeferredAttr.DeferredType paramAnonymousDeferredType, Attr.ResultInfo paramAnonymousResultInfo, DeferredAttr.DeferredAttrContext paramAnonymousDeferredAttrContext) {
/*  294 */       Assert.check(paramAnonymousDeferredAttrContext.mode == DeferredAttr.AttrMode.CHECK);
/*  295 */       return paramAnonymousDeferredType.tree.type = Type.stuckType;
/*      */     }
/*  292 */   };
/*      */ 
/*  323 */   DeferredStuckPolicy dummyStuckPolicy = new DeferredStuckPolicy()
/*      */   {
/*      */     public boolean isStuck() {
/*  326 */       return false;
/*      */     }
/*      */ 
/*      */     public Set<Type> stuckVars() {
/*  330 */       return Collections.emptySet();
/*      */     }
/*      */ 
/*      */     public Set<Type> depVars() {
/*  334 */       return Collections.emptySet();
/*      */     }
/*  323 */   };
/*      */ 
/*  396 */   protected UnenterScanner unenterScanner = new UnenterScanner();
/*      */   final DeferredAttrContext emptyDeferredAttrContext;
/* 1419 */   private EnumSet<JCTree.Tag> deferredCheckerTags = EnumSet.of(JCTree.Tag.LAMBDA, new JCTree.Tag[] { JCTree.Tag.REFERENCE, JCTree.Tag.PARENS, JCTree.Tag.TYPECAST, JCTree.Tag.CONDEXPR, JCTree.Tag.NEWCLASS, JCTree.Tag.APPLY, JCTree.Tag.LITERAL })
/* 1419 */     ;
/*      */ 
/*      */   public static DeferredAttr instance(Context paramContext)
/*      */   {
/*   83 */     DeferredAttr localDeferredAttr = (DeferredAttr)paramContext.get(deferredAttrKey);
/*   84 */     if (localDeferredAttr == null)
/*   85 */       localDeferredAttr = new DeferredAttr(paramContext);
/*   86 */     return localDeferredAttr; } 
/*      */   protected DeferredAttr(Context paramContext) { // Byte code:
/*      */     //   0: aload_0
/*      */     //   1: invokespecial 389	com/sun/tools/javac/tree/JCTree$Visitor:<init>	()V
/*      */     //   4: aload_0
/*      */     //   5: new 137	com/sun/tools/javac/comp/DeferredAttr$2
/*      */     //   8: dup
/*      */     //   9: aload_0
/*      */     //   10: invokespecial 373	com/sun/tools/javac/comp/DeferredAttr$2:<init>	(Lcom/sun/tools/javac/comp/DeferredAttr;)V
/*      */     //   13: putfield 335	com/sun/tools/javac/comp/DeferredAttr:basicCompleter	Lcom/sun/tools/javac/comp/DeferredAttr$DeferredTypeCompleter;
/*      */     //   16: aload_0
/*      */     //   17: new 138	com/sun/tools/javac/comp/DeferredAttr$3
/*      */     //   20: dup
/*      */     //   21: aload_0
/*      */     //   22: invokespecial 374	com/sun/tools/javac/comp/DeferredAttr$3:<init>	(Lcom/sun/tools/javac/comp/DeferredAttr;)V
/*      */     //   25: putfield 336	com/sun/tools/javac/comp/DeferredAttr:dummyCompleter	Lcom/sun/tools/javac/comp/DeferredAttr$DeferredTypeCompleter;
/*      */     //   28: aload_0
/*      */     //   29: new 139	com/sun/tools/javac/comp/DeferredAttr$4
/*      */     //   32: dup
/*      */     //   33: aload_0
/*      */     //   34: invokespecial 375	com/sun/tools/javac/comp/DeferredAttr$4:<init>	(Lcom/sun/tools/javac/comp/DeferredAttr;)V
/*      */     //   37: putfield 334	com/sun/tools/javac/comp/DeferredAttr:dummyStuckPolicy	Lcom/sun/tools/javac/comp/DeferredAttr$DeferredStuckPolicy;
/*      */     //   40: aload_0
/*      */     //   41: new 158	com/sun/tools/javac/comp/DeferredAttr$UnenterScanner
/*      */     //   44: dup
/*      */     //   45: aload_0
/*      */     //   46: invokespecial 380	com/sun/tools/javac/comp/DeferredAttr$UnenterScanner:<init>	(Lcom/sun/tools/javac/comp/DeferredAttr;)V
/*      */     //   49: putfield 337	com/sun/tools/javac/comp/DeferredAttr:unenterScanner	Lcom/sun/tools/javac/comp/DeferredAttr$UnenterScanner;
/*      */     //   52: aload_0
/*      */     //   53: getstatic 357	com/sun/tools/javac/tree/JCTree$Tag:LAMBDA	Lcom/sun/tools/javac/tree/JCTree$Tag;
/*      */     //   56: bipush 7
/*      */     //   58: anewarray 170	com/sun/tools/javac/tree/JCTree$Tag
/*      */     //   61: dup
/*      */     //   62: iconst_0
/*      */     //   63: getstatic 361	com/sun/tools/javac/tree/JCTree$Tag:REFERENCE	Lcom/sun/tools/javac/tree/JCTree$Tag;
/*      */     //   66: aastore
/*      */     //   67: dup
/*      */     //   68: iconst_1
/*      */     //   69: getstatic 360	com/sun/tools/javac/tree/JCTree$Tag:PARENS	Lcom/sun/tools/javac/tree/JCTree$Tag;
/*      */     //   72: aastore
/*      */     //   73: dup
/*      */     //   74: iconst_2
/*      */     //   75: getstatic 362	com/sun/tools/javac/tree/JCTree$Tag:TYPECAST	Lcom/sun/tools/javac/tree/JCTree$Tag;
/*      */     //   78: aastore
/*      */     //   79: dup
/*      */     //   80: iconst_3
/*      */     //   81: getstatic 356	com/sun/tools/javac/tree/JCTree$Tag:CONDEXPR	Lcom/sun/tools/javac/tree/JCTree$Tag;
/*      */     //   84: aastore
/*      */     //   85: dup
/*      */     //   86: iconst_4
/*      */     //   87: getstatic 359	com/sun/tools/javac/tree/JCTree$Tag:NEWCLASS	Lcom/sun/tools/javac/tree/JCTree$Tag;
/*      */     //   90: aastore
/*      */     //   91: dup
/*      */     //   92: iconst_5
/*      */     //   93: getstatic 355	com/sun/tools/javac/tree/JCTree$Tag:APPLY	Lcom/sun/tools/javac/tree/JCTree$Tag;
/*      */     //   96: aastore
/*      */     //   97: dup
/*      */     //   98: bipush 6
/*      */     //   100: getstatic 358	com/sun/tools/javac/tree/JCTree$Tag:LITERAL	Lcom/sun/tools/javac/tree/JCTree$Tag;
/*      */     //   103: aastore
/*      */     //   104: invokestatic 402	java/util/EnumSet:of	(Ljava/lang/Enum;[Ljava/lang/Enum;)Ljava/util/EnumSet;
/*      */     //   107: putfield 349	com/sun/tools/javac/comp/DeferredAttr:deferredCheckerTags	Ljava/util/EnumSet;
/*      */     //   110: aload_1
/*      */     //   111: getstatic 345	com/sun/tools/javac/comp/DeferredAttr:deferredAttrKey	Lcom/sun/tools/javac/util/Context$Key;
/*      */     //   114: aload_0
/*      */     //   115: invokevirtual 395	com/sun/tools/javac/util/Context:put	(Lcom/sun/tools/javac/util/Context$Key;Ljava/lang/Object;)V
/*      */     //   118: aload_0
/*      */     //   119: aload_1
/*      */     //   120: invokestatic 367	com/sun/tools/javac/comp/Attr:instance	(Lcom/sun/tools/javac/util/Context;)Lcom/sun/tools/javac/comp/Attr;
/*      */     //   123: putfield 331	com/sun/tools/javac/comp/DeferredAttr:attr	Lcom/sun/tools/javac/comp/Attr;
/*      */     //   126: aload_0
/*      */     //   127: aload_1
/*      */     //   128: invokestatic 370	com/sun/tools/javac/comp/Check:instance	(Lcom/sun/tools/javac/util/Context;)Lcom/sun/tools/javac/comp/Check;
/*      */     //   131: putfield 332	com/sun/tools/javac/comp/DeferredAttr:chk	Lcom/sun/tools/javac/comp/Check;
/*      */     //   134: aload_0
/*      */     //   135: aload_1
/*      */     //   136: invokestatic 397	com/sun/tools/javac/util/JCDiagnostic$Factory:instance	(Lcom/sun/tools/javac/util/Context;)Lcom/sun/tools/javac/util/JCDiagnostic$Factory;
/*      */     //   139: putfield 346	com/sun/tools/javac/comp/DeferredAttr:diags	Lcom/sun/tools/javac/util/JCDiagnostic$Factory;
/*      */     //   142: aload_0
/*      */     //   143: aload_1
/*      */     //   144: invokestatic 382	com/sun/tools/javac/comp/Enter:instance	(Lcom/sun/tools/javac/util/Context;)Lcom/sun/tools/javac/comp/Enter;
/*      */     //   147: putfield 338	com/sun/tools/javac/comp/DeferredAttr:enter	Lcom/sun/tools/javac/comp/Enter;
/*      */     //   150: aload_0
/*      */     //   151: aload_1
/*      */     //   152: invokestatic 385	com/sun/tools/javac/comp/Infer:instance	(Lcom/sun/tools/javac/util/Context;)Lcom/sun/tools/javac/comp/Infer;
/*      */     //   155: putfield 340	com/sun/tools/javac/comp/DeferredAttr:infer	Lcom/sun/tools/javac/comp/Infer;
/*      */     //   158: aload_0
/*      */     //   159: aload_1
/*      */     //   160: invokestatic 386	com/sun/tools/javac/comp/Resolve:instance	(Lcom/sun/tools/javac/util/Context;)Lcom/sun/tools/javac/comp/Resolve;
/*      */     //   163: putfield 341	com/sun/tools/javac/comp/DeferredAttr:rs	Lcom/sun/tools/javac/comp/Resolve;
/*      */     //   166: aload_0
/*      */     //   167: aload_1
/*      */     //   168: invokestatic 399	com/sun/tools/javac/util/Log:instance	(Lcom/sun/tools/javac/util/Context;)Lcom/sun/tools/javac/util/Log;
/*      */     //   171: putfield 347	com/sun/tools/javac/comp/DeferredAttr:log	Lcom/sun/tools/javac/util/Log;
/*      */     //   174: aload_0
/*      */     //   175: aload_1
/*      */     //   176: invokestatic 365	com/sun/tools/javac/code/Symtab:instance	(Lcom/sun/tools/javac/util/Context;)Lcom/sun/tools/javac/code/Symtab;
/*      */     //   179: putfield 329	com/sun/tools/javac/comp/DeferredAttr:syms	Lcom/sun/tools/javac/code/Symtab;
/*      */     //   182: aload_0
/*      */     //   183: aload_1
/*      */     //   184: invokestatic 393	com/sun/tools/javac/tree/TreeMaker:instance	(Lcom/sun/tools/javac/util/Context;)Lcom/sun/tools/javac/tree/TreeMaker;
/*      */     //   187: putfield 344	com/sun/tools/javac/comp/DeferredAttr:make	Lcom/sun/tools/javac/tree/TreeMaker;
/*      */     //   190: aload_0
/*      */     //   191: aload_1
/*      */     //   192: invokestatic 366	com/sun/tools/javac/code/Types:instance	(Lcom/sun/tools/javac/util/Context;)Lcom/sun/tools/javac/code/Types;
/*      */     //   195: putfield 330	com/sun/tools/javac/comp/DeferredAttr:types	Lcom/sun/tools/javac/code/Types;
/*      */     //   198: aload_0
/*      */     //   199: aload_1
/*      */     //   200: invokestatic 384	com/sun/tools/javac/comp/Flow:instance	(Lcom/sun/tools/javac/util/Context;)Lcom/sun/tools/javac/comp/Flow;
/*      */     //   203: putfield 339	com/sun/tools/javac/comp/DeferredAttr:flow	Lcom/sun/tools/javac/comp/Flow;
/*      */     //   206: aload_0
/*      */     //   207: aload_1
/*      */     //   208: invokestatic 401	com/sun/tools/javac/util/Names:instance	(Lcom/sun/tools/javac/util/Context;)Lcom/sun/tools/javac/util/Names;
/*      */     //   211: putfield 348	com/sun/tools/javac/comp/DeferredAttr:names	Lcom/sun/tools/javac/util/Names;
/*      */     //   214: aload_0
/*      */     //   215: aload_0
/*      */     //   216: getfield 344	com/sun/tools/javac/comp/DeferredAttr:make	Lcom/sun/tools/javac/tree/TreeMaker;
/*      */     //   219: aload_0
/*      */     //   220: getfield 348	com/sun/tools/javac/comp/DeferredAttr:names	Lcom/sun/tools/javac/util/Names;
/*      */     //   223: getfield 363	com/sun/tools/javac/util/Names:empty	Lcom/sun/tools/javac/util/Name;
/*      */     //   226: invokevirtual 392	com/sun/tools/javac/tree/TreeMaker:Ident	(Lcom/sun/tools/javac/util/Name;)Lcom/sun/tools/javac/tree/JCTree$JCIdent;
/*      */     //   229: getstatic 327	com/sun/tools/javac/code/Type:stuckType	Lcom/sun/tools/javac/code/Type$JCNoType;
/*      */     //   232: invokevirtual 388	com/sun/tools/javac/tree/JCTree$JCIdent:setType	(Lcom/sun/tools/javac/code/Type;)Lcom/sun/tools/javac/tree/JCTree$JCExpression;
/*      */     //   235: putfield 343	com/sun/tools/javac/comp/DeferredAttr:stuckTree	Lcom/sun/tools/javac/tree/JCTree;
/*      */     //   238: aload_0
/*      */     //   239: aload_1
/*      */     //   240: invokestatic 387	com/sun/tools/javac/comp/TypeEnvs:instance	(Lcom/sun/tools/javac/util/Context;)Lcom/sun/tools/javac/comp/TypeEnvs;
/*      */     //   243: putfield 342	com/sun/tools/javac/comp/DeferredAttr:typeEnvs	Lcom/sun/tools/javac/comp/TypeEnvs;
/*      */     //   246: aload_0
/*      */     //   247: new 136	com/sun/tools/javac/comp/DeferredAttr$1
/*      */     //   250: dup
/*      */     //   251: aload_0
/*      */     //   252: getstatic 350	com/sun/tools/javac/comp/DeferredAttr$AttrMode:CHECK	Lcom/sun/tools/javac/comp/DeferredAttr$AttrMode;
/*      */     //   255: aconst_null
/*      */     //   256: getstatic 354	com/sun/tools/javac/comp/Resolve$MethodResolutionPhase:BOX	Lcom/sun/tools/javac/comp/Resolve$MethodResolutionPhase;
/*      */     //   259: aload_0
/*      */     //   260: getfield 340	com/sun/tools/javac/comp/DeferredAttr:infer	Lcom/sun/tools/javac/comp/Infer;
/*      */     //   263: getfield 353	com/sun/tools/javac/comp/Infer:emptyContext	Lcom/sun/tools/javac/comp/Infer$InferenceContext;
/*      */     //   266: aconst_null
/*      */     //   267: aconst_null
/*      */     //   268: invokespecial 372	com/sun/tools/javac/comp/DeferredAttr$1:<init>	(Lcom/sun/tools/javac/comp/DeferredAttr;Lcom/sun/tools/javac/comp/DeferredAttr$AttrMode;Lcom/sun/tools/javac/code/Symbol;Lcom/sun/tools/javac/comp/Resolve$MethodResolutionPhase;Lcom/sun/tools/javac/comp/Infer$InferenceContext;Lcom/sun/tools/javac/comp/DeferredAttr$DeferredAttrContext;Lcom/sun/tools/javac/util/Warner;)V
/*      */     //   271: putfield 333	com/sun/tools/javac/comp/DeferredAttr:emptyDeferredAttrContext	Lcom/sun/tools/javac/comp/DeferredAttr$DeferredAttrContext;
/*      */     //   274: return } 
/*  363 */   JCTree attribSpeculative(JCTree paramJCTree, Env<AttrContext> paramEnv, Attr.ResultInfo paramResultInfo) { final JCTree localJCTree1 = new TreeCopier(this.make).copy(paramJCTree);
/*  364 */     Env localEnv = paramEnv.dup(localJCTree1, ((AttrContext)paramEnv.info).dup(((AttrContext)paramEnv.info).scope.dupUnshared()));
/*  365 */     ((AttrContext)localEnv.info).scope.owner = ((AttrContext)paramEnv.info).scope.owner;
/*  366 */     Log.DeferredDiagnosticHandler localDeferredDiagnosticHandler = new Log.DeferredDiagnosticHandler(this.log, new Filter()
/*      */     {
/*      */       public boolean accepts(final JCDiagnostic paramAnonymousJCDiagnostic)
/*      */       {
/*  381 */         TreeScanner local1PosScanner = new TreeScanner()
/*      */         {
/*  370 */           boolean found = false;
/*      */ 
/*      */           public void scan(JCTree paramAnonymous2JCTree)
/*      */           {
/*  374 */             if ((paramAnonymous2JCTree != null) && 
/*  375 */               (paramAnonymous2JCTree
/*  375 */               .pos() == paramAnonymousJCDiagnostic.getDiagnosticPosition())) {
/*  376 */               this.found = true;
/*      */             }
/*  378 */             super.scan(paramAnonymous2JCTree);
/*      */           }
/*      */         };
/*  382 */         local1PosScanner.scan(localJCTree1);
/*  383 */         return local1PosScanner.found;
/*      */       }
/*      */     });
/*      */     try {
/*  387 */       this.attr.attribTree(localJCTree1, localEnv, paramResultInfo);
/*  388 */       this.unenterScanner.scan(localJCTree1);
/*  389 */       return localJCTree1;
/*      */     } finally {
/*  391 */       this.unenterScanner.scan(localJCTree1);
/*  392 */       this.log.popDiagnosticHandler(localDeferredDiagnosticHandler);
/*      */     }
/*      */   }
/*      */ 
/*      */   boolean isDeferred(Env<AttrContext> paramEnv, JCTree.JCExpression paramJCExpression)
/*      */   {
/* 1099 */     DeferredChecker localDeferredChecker = new DeferredChecker(paramEnv);
/* 1100 */     localDeferredChecker.scan(paramJCExpression);
/* 1101 */     return localDeferredChecker.result.isPoly();
/*      */   }
/*      */ 
/*      */   static enum ArgumentExpressionKind
/*      */   {
/* 1111 */     POLY, 
/*      */ 
/* 1113 */     NO_POLY, 
/*      */ 
/* 1115 */     PRIMITIVE;
/*      */ 
/*      */     public final boolean isPoly()
/*      */     {
/* 1121 */       return this == POLY;
/*      */     }
/*      */ 
/*      */     public final boolean isPrimitive()
/*      */     {
/* 1128 */       return this == PRIMITIVE;
/*      */     }
/*      */ 
/*      */     static ArgumentExpressionKind standaloneKind(Type paramType, Types paramTypes)
/*      */     {
/* 1135 */       return paramTypes.unboxedTypeOrType(paramType).isPrimitive() ? PRIMITIVE : NO_POLY;
/*      */     }
/*      */ 
/*      */     static ArgumentExpressionKind methodKind(Symbol paramSymbol, Types paramTypes)
/*      */     {
/* 1144 */       Type localType = paramSymbol.type.getReturnType();
/* 1145 */       if ((paramSymbol.type.hasTag(TypeTag.FORALL)) && 
/* 1146 */         (localType
/* 1146 */         .containsAny(((Type.ForAll)paramSymbol.type).tvars)))
/*      */       {
/* 1147 */         return POLY;
/*      */       }
/* 1149 */       return standaloneKind(localType, paramTypes);
/*      */     }
/*      */   }
/*      */ 
/*      */   public static enum AttrMode
/*      */   {
/*  349 */     SPECULATIVE, 
/*      */ 
/*  353 */     CHECK;
/*      */   }
/*      */ 
/*      */   class CheckStuckPolicy extends DeferredAttr.PolyScanner
/*      */     implements DeferredAttr.DeferredStuckPolicy, Infer.FreeTypeListener
/*      */   {
/*      */     Type pt;
/*      */     Infer.InferenceContext inferenceContext;
/*  959 */     Set<Type> stuckVars = new LinkedHashSet();
/*  960 */     Set<Type> depVars = new LinkedHashSet();
/*      */ 
/*      */     public boolean isStuck()
/*      */     {
/*  964 */       return !this.stuckVars.isEmpty();
/*      */     }
/*      */ 
/*      */     public Set<Type> stuckVars()
/*      */     {
/*  969 */       return this.stuckVars;
/*      */     }
/*      */ 
/*      */     public Set<Type> depVars()
/*      */     {
/*  974 */       return this.depVars;
/*      */     }
/*      */ 
/*      */     public CheckStuckPolicy(Attr.ResultInfo paramDeferredType, DeferredAttr.DeferredType arg3) {
/*  978 */       this.pt = paramDeferredType.pt;
/*  979 */       this.inferenceContext = paramDeferredType.checkContext.inferenceContext();
/*      */       Object localObject;
/*  980 */       scan(localObject.tree);
/*  981 */       if (!this.stuckVars.isEmpty())
/*  982 */         paramDeferredType.checkContext.inferenceContext()
/*  983 */           .addFreeTypeListener(List.from(this.stuckVars), 
/*  983 */           this);
/*      */     }
/*      */ 
/*      */     public void typesInferred(Infer.InferenceContext paramInferenceContext)
/*      */     {
/*  989 */       this.stuckVars.clear();
/*      */     }
/*      */ 
/*      */     public void visitLambda(JCTree.JCLambda paramJCLambda)
/*      */     {
/*  994 */       if (this.inferenceContext.inferenceVars().contains(this.pt)) {
/*  995 */         this.stuckVars.add(this.pt);
/*      */       }
/*  997 */       if (!DeferredAttr.this.types.isFunctionalInterface(this.pt)) {
/*  998 */         return;
/*      */       }
/* 1000 */       Type localType = DeferredAttr.this.types.findDescriptorType(this.pt);
/* 1001 */       List localList = this.inferenceContext.freeVarsIn(localType.getParameterTypes());
/* 1002 */       if ((paramJCLambda.paramKind == JCTree.JCLambda.ParameterKind.IMPLICIT) && 
/* 1003 */         (localList
/* 1003 */         .nonEmpty())) {
/* 1004 */         this.stuckVars.addAll(localList);
/* 1005 */         this.depVars.addAll(this.inferenceContext.freeVarsIn(localType.getReturnType()));
/*      */       }
/* 1007 */       scanLambdaBody(paramJCLambda, localType.getReturnType());
/*      */     }
/*      */ 
/*      */     public void visitReference(JCTree.JCMemberReference paramJCMemberReference)
/*      */     {
/* 1012 */       scan(paramJCMemberReference.expr);
/* 1013 */       if (this.inferenceContext.inferenceVars().contains(this.pt)) {
/* 1014 */         this.stuckVars.add(this.pt);
/* 1015 */         return;
/*      */       }
/* 1017 */       if (!DeferredAttr.this.types.isFunctionalInterface(this.pt)) {
/* 1018 */         return;
/*      */       }
/*      */ 
/* 1021 */       Type localType = DeferredAttr.this.types.findDescriptorType(this.pt);
/* 1022 */       List localList = this.inferenceContext.freeVarsIn(localType.getParameterTypes());
/* 1023 */       if ((localList.nonEmpty()) && (paramJCMemberReference.overloadKind == JCTree.JCMemberReference.OverloadKind.OVERLOADED))
/*      */       {
/* 1025 */         this.stuckVars.addAll(localList);
/* 1026 */         this.depVars.addAll(this.inferenceContext.freeVarsIn(localType.getReturnType()));
/*      */       }
/*      */     }
/*      */ 
/*      */     void scanLambdaBody(JCTree.JCLambda paramJCLambda, final Type paramType)
/*      */     {
/*      */       Object localObject1;
/* 1031 */       if (paramJCLambda.getBodyKind() == LambdaExpressionTree.BodyKind.EXPRESSION) {
/* 1032 */         localObject1 = this.pt;
/*      */         try {
/* 1034 */           this.pt = paramType;
/* 1035 */           scan(paramJCLambda.body);
/*      */         } finally {
/* 1037 */           this.pt = ((Type)localObject1);
/*      */         }
/*      */       } else {
/* 1040 */         localObject1 = new DeferredAttr.LambdaReturnScanner()
/*      */         {
/*      */           public void visitReturn(JCTree.JCReturn paramAnonymousJCReturn) {
/* 1043 */             if (paramAnonymousJCReturn.expr != null) {
/* 1044 */               Type localType = DeferredAttr.CheckStuckPolicy.this.pt;
/*      */               try {
/* 1046 */                 DeferredAttr.CheckStuckPolicy.this.pt = paramType;
/* 1047 */                 DeferredAttr.CheckStuckPolicy.this.scan(paramAnonymousJCReturn.expr);
/*      */ 
/* 1049 */                 DeferredAttr.CheckStuckPolicy.this.pt = localType; } finally { DeferredAttr.CheckStuckPolicy.this.pt = localType; }
/*      */ 
/*      */             }
/*      */           }
/*      */         };
/* 1054 */         ((DeferredAttr.LambdaReturnScanner)localObject1).scan(paramJCLambda.body);
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   class DeferredAttrContext
/*      */   {
/*      */     final DeferredAttr.AttrMode mode;
/*      */     final Symbol msym;
/*      */     final Resolve.MethodResolutionPhase phase;
/*      */     final Infer.InferenceContext inferenceContext;
/*      */     final DeferredAttrContext parent;
/*      */     final Warner warn;
/*  445 */     ArrayList<DeferredAttr.DeferredAttrNode> deferredAttrNodes = new ArrayList();
/*      */ 
/*      */     DeferredAttrContext(DeferredAttr.AttrMode paramSymbol, Symbol paramMethodResolutionPhase, Resolve.MethodResolutionPhase paramInferenceContext, Infer.InferenceContext paramDeferredAttrContext, DeferredAttrContext paramWarner, Warner arg7)
/*      */     {
/*  449 */       this.mode = paramSymbol;
/*  450 */       this.msym = paramMethodResolutionPhase;
/*  451 */       this.phase = paramInferenceContext;
/*  452 */       this.parent = paramWarner;
/*      */       Object localObject;
/*  453 */       this.warn = localObject;
/*  454 */       this.inferenceContext = paramDeferredAttrContext;
/*      */     }
/*      */ 
/*      */     void addDeferredAttrNode(DeferredAttr.DeferredType paramDeferredType, Attr.ResultInfo paramResultInfo, DeferredAttr.DeferredStuckPolicy paramDeferredStuckPolicy)
/*      */     {
/*  463 */       this.deferredAttrNodes.add(new DeferredAttr.DeferredAttrNode(DeferredAttr.this, paramDeferredType, paramResultInfo, paramDeferredStuckPolicy));
/*      */     }
/*      */ 
/*      */     void complete()
/*      */     {
/*  473 */       while (!this.deferredAttrNodes.isEmpty()) {
/*  474 */         LinkedHashMap localLinkedHashMap = new LinkedHashMap();
/*  475 */         List localList1 = List.nil();
/*  476 */         int i = 0;
/*      */ 
/*  479 */         for (Iterator localIterator1 = List.from(this.deferredAttrNodes).iterator(); localIterator1.hasNext(); ) { localDeferredAttrNode = (DeferredAttr.DeferredAttrNode)localIterator1.next();
/*      */           List localList2;
/*  480 */           if (!localDeferredAttrNode.process(this))
/*      */           {
/*  483 */             localList2 = List.from(localDeferredAttrNode.deferredStuckPolicy
/*  482 */               .stuckVars())
/*  483 */               .intersect(this.inferenceContext
/*  483 */               .restvars());
/*  484 */             localList1 = localList1.prependList(localList2);
/*      */ 
/*  486 */             for (Type localType : List.from(localDeferredAttrNode.deferredStuckPolicy.depVars())
/*  487 */               .intersect(this.inferenceContext
/*  487 */               .restvars())) {
/*  488 */               Object localObject = (Set)localLinkedHashMap.get(localType);
/*  489 */               if (localObject == null) {
/*  490 */                 localObject = new LinkedHashSet();
/*  491 */                 localLinkedHashMap.put(localType, localObject);
/*      */               }
/*  493 */               ((Set)localObject).addAll(localList2);
/*      */             }
/*      */           } else {
/*  496 */             this.deferredAttrNodes.remove(localDeferredAttrNode);
/*  497 */             i = 1;
/*      */           }
/*      */         }
/*      */         DeferredAttr.DeferredAttrNode localDeferredAttrNode;
/*  500 */         if (i == 0) {
/*  501 */           if (insideOverloadPhase()) {
/*  502 */             for (localIterator1 = this.deferredAttrNodes.iterator(); localIterator1.hasNext(); ) { localDeferredAttrNode = (DeferredAttr.DeferredAttrNode)localIterator1.next();
/*  503 */               localDeferredAttrNode.dt.tree.type = Type.noType;
/*      */             }
/*  505 */             return;
/*      */           }
/*      */ 
/*      */           try
/*      */           {
/*  510 */             this.inferenceContext.solveAny(localList1, localLinkedHashMap, this.warn);
/*  511 */             this.inferenceContext.notifyChange();
/*      */           }
/*      */           catch (Infer.GraphStrategy.NodeNotFoundException localNodeNotFoundException)
/*      */           {
/*  516 */             break;
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/*      */ 
/*      */     private boolean insideOverloadPhase() {
/*  523 */       DeferredAttrContext localDeferredAttrContext = this;
/*  524 */       if (localDeferredAttrContext == DeferredAttr.this.emptyDeferredAttrContext) {
/*  525 */         return false;
/*      */       }
/*  527 */       if (localDeferredAttrContext.mode == DeferredAttr.AttrMode.SPECULATIVE) {
/*  528 */         return true;
/*      */       }
/*  530 */       return localDeferredAttrContext.parent.insideOverloadPhase();
/*      */     }
/*      */   }
/*      */ 
/*      */   class DeferredAttrNode
/*      */   {
/*      */     DeferredAttr.DeferredType dt;
/*      */     Attr.ResultInfo resultInfo;
/*      */     DeferredAttr.DeferredStuckPolicy deferredStuckPolicy;
/*      */ 
/*      */     DeferredAttrNode(DeferredAttr.DeferredType paramResultInfo, Attr.ResultInfo paramDeferredStuckPolicy, DeferredAttr.DeferredStuckPolicy arg4)
/*      */     {
/*  550 */       this.dt = paramResultInfo;
/*  551 */       this.resultInfo = paramDeferredStuckPolicy;
/*      */       Object localObject;
/*  552 */       this.deferredStuckPolicy = localObject;
/*      */     }
/*      */ 
/*      */     boolean process(final DeferredAttr.DeferredAttrContext paramDeferredAttrContext)
/*      */     {
/*  561 */       switch (DeferredAttr.6.$SwitchMap$com$sun$tools$javac$comp$DeferredAttr$AttrMode[paramDeferredAttrContext.mode.ordinal()]) {
/*      */       case 1:
/*  563 */         if (this.deferredStuckPolicy.isStuck()) {
/*  564 */           DeferredAttr.DeferredType.access$100(this.dt, this.resultInfo, DeferredAttr.this.dummyStuckPolicy, new StructuralStuckChecker());
/*  565 */           return true;
/*      */         }
/*  567 */         Assert.error("Cannot get here");
/*      */       case 2:
/*  570 */         if (this.deferredStuckPolicy.isStuck())
/*      */         {
/*  572 */           if ((paramDeferredAttrContext.parent != DeferredAttr.this.emptyDeferredAttrContext) && 
/*  573 */             (Type.containsAny(paramDeferredAttrContext.parent.inferenceContext.inferencevars, 
/*  574 */             List.from(this.deferredStuckPolicy
/*  574 */             .stuckVars())))) {
/*  575 */             paramDeferredAttrContext.parent.addDeferredAttrNode(this.dt, this.resultInfo
/*  576 */               .dup(new Check.NestedCheckContext(this.resultInfo.checkContext)
/*      */             {
/*      */               public Infer.InferenceContext inferenceContext()
/*      */               {
/*  579 */                 return paramDeferredAttrContext.parent.inferenceContext;
/*      */               }
/*      */ 
/*      */               public DeferredAttr.DeferredAttrContext deferredAttrContext() {
/*  583 */                 return paramDeferredAttrContext.parent;
/*      */               }
/*      */             }), this.deferredStuckPolicy);
/*      */ 
/*  586 */             this.dt.tree.type = Type.stuckType;
/*  587 */             return true;
/*      */           }
/*  589 */           return false;
/*      */         }
/*      */ 
/*  592 */         Assert.check(!paramDeferredAttrContext.insideOverloadPhase(), "attribution shouldn't be happening here");
/*      */ 
/*  595 */         Attr.ResultInfo localResultInfo = this.resultInfo
/*  595 */           .dup(paramDeferredAttrContext.inferenceContext
/*  595 */           .asInstType(this.resultInfo.pt));
/*      */ 
/*  596 */         DeferredAttr.DeferredType.access$100(this.dt, localResultInfo, DeferredAttr.this.dummyStuckPolicy, DeferredAttr.this.basicCompleter);
/*  597 */         return true;
/*      */       }
/*      */ 
/*  600 */       throw new AssertionError("Bad mode");
/*      */     }
/*      */ 
/*      */     class LambdaBodyStructChecker extends TreeScanner
/*      */     {
/*  785 */       boolean isVoidCompatible = true;
/*  786 */       boolean isPotentiallyValueCompatible = true;
/*      */ 
/*      */       LambdaBodyStructChecker()
/*      */       {
/*      */       }
/*      */ 
/*      */       public void visitClassDef(JCTree.JCClassDecl paramJCClassDecl)
/*      */       {
/*      */       }
/*      */ 
/*      */       public void visitLambda(JCTree.JCLambda paramJCLambda)
/*      */       {
/*      */       }
/*      */ 
/*      */       public void visitNewClass(JCTree.JCNewClass paramJCNewClass)
/*      */       {
/*      */       }
/*      */ 
/*      */       public void visitReturn(JCTree.JCReturn paramJCReturn) {
/*  805 */         if (paramJCReturn.expr != null)
/*  806 */           this.isVoidCompatible = false;
/*      */         else
/*  808 */           this.isPotentiallyValueCompatible = false;
/*      */       }
/*      */     }
/*      */ 
/*      */     class StructuralStuckChecker extends TreeScanner
/*      */       implements DeferredAttr.DeferredTypeCompleter
/*      */     {
/*      */       Attr.ResultInfo resultInfo;
/*      */       Infer.InferenceContext inferenceContext;
/*      */       Env<AttrContext> env;
/*      */ 
/*      */       StructuralStuckChecker()
/*      */       {
/*      */       }
/*      */ 
/*      */       public Type complete(DeferredAttr.DeferredType paramDeferredType, Attr.ResultInfo paramResultInfo, DeferredAttr.DeferredAttrContext paramDeferredAttrContext)
/*      */       {
/*  614 */         this.resultInfo = paramResultInfo;
/*  615 */         this.inferenceContext = paramDeferredAttrContext.inferenceContext;
/*  616 */         this.env = paramDeferredType.env;
/*  617 */         paramDeferredType.tree.accept(this);
/*  618 */         paramDeferredType.speculativeCache.put(DeferredAttr.this.stuckTree, paramResultInfo);
/*  619 */         return Type.noType;
/*      */       }
/*      */ 
/*      */       public void visitLambda(JCTree.JCLambda paramJCLambda)
/*      */       {
/*  624 */         Check.CheckContext localCheckContext = this.resultInfo.checkContext;
/*  625 */         Type localType1 = this.resultInfo.pt;
/*  626 */         if (!this.inferenceContext.inferencevars.contains(localType1))
/*      */         {
/*  628 */           Type localType2 = null;
/*      */           try {
/*  630 */             localType2 = DeferredAttr.this.types.findDescriptorType(localType1);
/*      */           } catch (Types.FunctionDescriptorLookupError localFunctionDescriptorLookupError) {
/*  632 */             localCheckContext.report(null, localFunctionDescriptorLookupError.getDiagnostic());
/*      */           }
/*      */ 
/*  635 */           if (localType2.getParameterTypes().length() != paramJCLambda.params.length()) {
/*  636 */             localCheckContext.report(paramJCLambda, DeferredAttr.this.diags
/*  637 */               .fragment("incompatible.arg.types.in.lambda", new Object[0]));
/*      */           }
/*      */ 
/*  640 */           Type localType3 = localType2.getReturnType();
/*  641 */           boolean bool1 = localType3.hasTag(TypeTag.VOID);
/*  642 */           if (paramJCLambda.getBodyKind() == LambdaExpressionTree.BodyKind.EXPRESSION)
/*      */           {
/*  644 */             int i = (!bool1) || 
/*  644 */               (TreeInfo.isExpressionStatement((JCTree.JCExpression)paramJCLambda
/*  644 */               .getBody())) ? 1 : 0;
/*  645 */             if (i == 0) {
/*  646 */               this.resultInfo.checkContext.report(paramJCLambda.pos(), DeferredAttr.this.diags
/*  647 */                 .fragment("incompatible.ret.type.in.lambda", new Object[] { DeferredAttr.this.diags
/*  648 */                 .fragment("missing.ret.val", new Object[] { localType3 }) }));
/*      */             }
/*      */           }
/*      */           else
/*      */           {
/*  651 */             DeferredAttr.DeferredAttrNode.LambdaBodyStructChecker localLambdaBodyStructChecker = new DeferredAttr.DeferredAttrNode.LambdaBodyStructChecker(DeferredAttr.DeferredAttrNode.this);
/*      */ 
/*  654 */             paramJCLambda.body.accept(localLambdaBodyStructChecker);
/*  655 */             boolean bool2 = localLambdaBodyStructChecker.isVoidCompatible;
/*      */ 
/*  657 */             if (bool1) {
/*  658 */               if (!bool2) {
/*  659 */                 this.resultInfo.checkContext.report(paramJCLambda.pos(), DeferredAttr.this.diags
/*  660 */                   .fragment("unexpected.ret.val", new Object[0]));
/*      */               }
/*      */             }
/*      */             else
/*      */             {
/*  664 */               int j = (localLambdaBodyStructChecker.isPotentiallyValueCompatible) && 
/*  664 */                 (!canLambdaBodyCompleteNormally(paramJCLambda)) ? 
/*  664 */                 1 : 0;
/*  665 */               if ((j == 0) && (!bool2)) {
/*  666 */                 DeferredAttr.this.log.error(paramJCLambda.body.pos(), "lambda.body.neither.value.nor.void.compatible", new Object[0]);
/*      */               }
/*      */ 
/*  670 */               if (j == 0)
/*  671 */                 this.resultInfo.checkContext.report(paramJCLambda.pos(), DeferredAttr.this.diags
/*  672 */                   .fragment("incompatible.ret.type.in.lambda", new Object[] { DeferredAttr.this.diags
/*  673 */                   .fragment("missing.ret.val", new Object[] { localType3 }) }));
/*      */             }
/*      */           }
/*      */         }
/*      */       }
/*      */ 
/*      */       boolean canLambdaBodyCompleteNormally(JCTree.JCLambda paramJCLambda)
/*      */       {
/*  681 */         JCTree.JCLambda localJCLambda = (JCTree.JCLambda)new TreeCopier(DeferredAttr.this.make).copy(paramJCLambda);
/*      */ 
/*  687 */         Env localEnv = DeferredAttr.this.attr.lambdaEnv(localJCLambda, this.env);
/*      */         try {
/*  689 */           List localList = localJCLambda.params;
/*  690 */           while (localList.nonEmpty()) {
/*  691 */             ((JCTree.JCVariableDecl)localList.head).vartype = DeferredAttr.this.make.at((JCDiagnostic.DiagnosticPosition)localList.head).Type(DeferredAttr.this.syms.errType);
/*  692 */             localList = localList.tail;
/*      */           }
/*      */ 
/*  695 */           DeferredAttr.this.attr.attribStats(localJCLambda.params, localEnv);
/*      */           Attr tmp148_145 = DeferredAttr.this.attr; tmp148_145.getClass(); Attr.ResultInfo localResultInfo = new Attr.ResultInfo(tmp148_145, 12, Type.noType);
/*  702 */           ((AttrContext)localEnv.info).returnResult = localResultInfo;
/*      */ 
/*  705 */           Log.DiscardDiagnosticHandler localDiscardDiagnosticHandler = new Log.DiscardDiagnosticHandler(DeferredAttr.this.log);
/*      */           try {
/*  707 */             JCTree.JCBlock localJCBlock1 = (JCTree.JCBlock)localJCLambda.body;
/*      */ 
/*  716 */             DeferredAttr.this.attr.attribStats(localJCBlock1.stats, localEnv);
/*      */ 
/*  718 */             DeferredAttr.this.attr.preFlow(localJCLambda);
/*      */ 
/*  722 */             DeferredAttr.this.flow.analyzeLambda(localEnv, localJCLambda, DeferredAttr.this.make, true);
/*      */           } finally {
/*  724 */             DeferredAttr.this.log.popDiagnosticHandler(localDiscardDiagnosticHandler);
/*      */           }
/*      */           JCTree.JCBlock localJCBlock2;
/*  726 */           return localJCLambda.canCompleteNormally;
/*      */         } finally {
/*  728 */           JCTree.JCBlock localJCBlock3 = (JCTree.JCBlock)localJCLambda.body;
/*  729 */           DeferredAttr.this.unenterScanner.scan(localJCBlock3.stats);
/*  730 */           ((AttrContext)localEnv.info).scope.leave();
/*      */         }
/*      */       }
/*      */ 
/*      */       public void visitNewClass(JCTree.JCNewClass paramJCNewClass)
/*      */       {
/*      */       }
/*      */ 
/*      */       public void visitApply(JCTree.JCMethodInvocation paramJCMethodInvocation)
/*      */       {
/*      */       }
/*      */ 
/*      */       public void visitReference(JCTree.JCMemberReference paramJCMemberReference)
/*      */       {
/*  746 */         Check.CheckContext localCheckContext = this.resultInfo.checkContext;
/*  747 */         Type localType = this.resultInfo.pt;
/*  748 */         if (!this.inferenceContext.inferencevars.contains(localType)) {
/*      */           try {
/*  750 */             DeferredAttr.this.types.findDescriptorType(localType);
/*      */           } catch (Types.FunctionDescriptorLookupError localFunctionDescriptorLookupError) {
/*  752 */             localCheckContext.report(null, localFunctionDescriptorLookupError.getDiagnostic());
/*      */           }
/*  754 */           Env localEnv = this.env.dup(paramJCMemberReference);
/*  755 */           JCTree.JCExpression localJCExpression = (JCTree.JCExpression)DeferredAttr.this.attribSpeculative(paramJCMemberReference.getQualifierExpression(), localEnv, DeferredAttr.this.attr
/*  756 */             .memberReferenceQualifierResult(paramJCMemberReference));
/*      */ 
/*  757 */           ListBuffer localListBuffer = new ListBuffer();
/*  758 */           for (Object localObject1 = DeferredAttr.this.types.findDescriptorType(localType).getParameterTypes().iterator(); ((Iterator)localObject1).hasNext(); ) { localObject2 = (Type)((Iterator)localObject1).next();
/*  759 */             localListBuffer.append(Type.noType);
/*      */           }
/*  761 */           localObject1 = (JCTree.JCMemberReference)new TreeCopier(DeferredAttr.this.make).copy(paramJCMemberReference);
/*  762 */           ((JCTree.JCMemberReference)localObject1).expr = localJCExpression;
/*      */ 
/*  764 */           Object localObject2 = DeferredAttr.this.rs
/*  764 */             .resolveMemberReferenceByArity(localEnv, (JCTree.JCMemberReference)localObject1, localJCExpression.type, paramJCMemberReference.name, localListBuffer
/*  765 */             .toList(), this.inferenceContext);
/*  766 */           switch (((Symbol)localObject2).kind)
/*      */           {
/*      */           case 134:
/*      */           case 135:
/*      */           case 136:
/*      */           case 138:
/*  773 */             localCheckContext.report(paramJCMemberReference, DeferredAttr.this.diags.fragment("incompatible.arg.types.in.mref", new Object[0]));
/*      */           case 137:
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   final class DeferredChecker extends DeferredAttr.FilterScanner
/*      */   {
/*      */     Env<AttrContext> env;
/*      */     DeferredAttr.ArgumentExpressionKind result;
/* 1269 */     DeferredAttr.MethodAnalyzer<DeferredAttr.ArgumentExpressionKind> argumentKindAnalyzer = new DeferredAttr.MethodAnalyzer()
/*      */     {
/*      */       public DeferredAttr.ArgumentExpressionKind process(Symbol.MethodSymbol paramAnonymousMethodSymbol)
/*      */       {
/* 1273 */         return DeferredAttr.ArgumentExpressionKind.methodKind(paramAnonymousMethodSymbol, DeferredAttr.this.types);
/*      */       }
/*      */ 
/*      */       public DeferredAttr.ArgumentExpressionKind reduce(DeferredAttr.ArgumentExpressionKind paramAnonymousArgumentExpressionKind1, DeferredAttr.ArgumentExpressionKind paramAnonymousArgumentExpressionKind2)
/*      */       {
/* 1278 */         switch (DeferredAttr.6.$SwitchMap$com$sun$tools$javac$comp$DeferredAttr$ArgumentExpressionKind[paramAnonymousArgumentExpressionKind1.ordinal()]) { case 1:
/* 1279 */           return paramAnonymousArgumentExpressionKind2;
/*      */         case 2:
/* 1280 */           return paramAnonymousArgumentExpressionKind2.isPoly() ? paramAnonymousArgumentExpressionKind2 : paramAnonymousArgumentExpressionKind1;
/*      */         case 3:
/* 1281 */           return paramAnonymousArgumentExpressionKind1;
/*      */         }
/* 1283 */         Assert.error();
/* 1284 */         return null;
/*      */       }
/*      */ 
/*      */       public boolean shouldStop(DeferredAttr.ArgumentExpressionKind paramAnonymousArgumentExpressionKind)
/*      */       {
/* 1289 */         return paramAnonymousArgumentExpressionKind.isPoly();
/*      */       }
/* 1269 */     };
/*      */ 
/* 1364 */     DeferredAttr.MethodAnalyzer<Symbol> returnSymbolAnalyzer = new DeferredAttr.MethodAnalyzer()
/*      */     {
/*      */       public Symbol process(Symbol.MethodSymbol paramAnonymousMethodSymbol) {
/* 1367 */         DeferredAttr.ArgumentExpressionKind localArgumentExpressionKind = DeferredAttr.ArgumentExpressionKind.methodKind(paramAnonymousMethodSymbol, DeferredAttr.this.types);
/* 1368 */         if ((localArgumentExpressionKind == DeferredAttr.ArgumentExpressionKind.POLY) || (paramAnonymousMethodSymbol.getReturnType().hasTag(TypeTag.TYPEVAR)))
/* 1369 */           return null;
/* 1370 */         return paramAnonymousMethodSymbol.getReturnType().tsym;
/*      */       }
/*      */ 
/*      */       public Symbol reduce(Symbol paramAnonymousSymbol1, Symbol paramAnonymousSymbol2) {
/* 1374 */         return paramAnonymousSymbol1 == paramAnonymousSymbol2 ? paramAnonymousSymbol1 : paramAnonymousSymbol1 == DeferredAttr.this.syms.errSymbol ? paramAnonymousSymbol2 : null;
/*      */       }
/*      */ 
/*      */       public boolean shouldStop(Symbol paramAnonymousSymbol) {
/* 1378 */         return paramAnonymousSymbol == null;
/*      */       }
/* 1364 */     };
/*      */ 
/*      */     public DeferredChecker()
/*      */     {
/* 1164 */       super();
/*      */       Object localObject;
/* 1165 */       this.env = localObject;
/*      */     }
/*      */ 
/*      */     public void visitLambda(JCTree.JCLambda paramJCLambda)
/*      */     {
/* 1171 */       this.result = DeferredAttr.ArgumentExpressionKind.POLY;
/*      */     }
/*      */ 
/*      */     public void visitReference(JCTree.JCMemberReference paramJCMemberReference)
/*      */     {
/* 1177 */       Env localEnv = this.env.dup(paramJCMemberReference);
/* 1178 */       JCTree.JCExpression localJCExpression = (JCTree.JCExpression)DeferredAttr.this.attribSpeculative(paramJCMemberReference.getQualifierExpression(), localEnv, DeferredAttr.this.attr
/* 1179 */         .memberReferenceQualifierResult(paramJCMemberReference));
/*      */ 
/* 1180 */       JCTree.JCMemberReference localJCMemberReference = (JCTree.JCMemberReference)new TreeCopier(DeferredAttr.this.make).copy(paramJCMemberReference);
/* 1181 */       localJCMemberReference.expr = localJCExpression;
/*      */ 
/* 1183 */       Symbol localSymbol = DeferredAttr.this.rs
/* 1183 */         .getMemberReference(paramJCMemberReference, localEnv, localJCMemberReference, localJCExpression.type, paramJCMemberReference.name);
/*      */ 
/* 1185 */       paramJCMemberReference.sym = localSymbol;
/* 1186 */       if ((localSymbol.kind >= 128) || 
/* 1187 */         (localSymbol.type
/* 1187 */         .hasTag(TypeTag.FORALL)) || 
/* 1188 */         ((localSymbol
/* 1188 */         .flags() & 0x0) != 0L) || (
/* 1189 */         (TreeInfo.isStaticSelector(localJCExpression, paramJCMemberReference.name.table.names)) && 
/* 1190 */         (localJCExpression.type
/* 1190 */         .isRaw())))
/* 1191 */         paramJCMemberReference.overloadKind = JCTree.JCMemberReference.OverloadKind.OVERLOADED;
/*      */       else {
/* 1193 */         paramJCMemberReference.overloadKind = JCTree.JCMemberReference.OverloadKind.UNOVERLOADED;
/*      */       }
/*      */ 
/* 1196 */       this.result = DeferredAttr.ArgumentExpressionKind.POLY;
/*      */     }
/*      */ 
/*      */     public void visitTypeCast(JCTree.JCTypeCast paramJCTypeCast)
/*      */     {
/* 1202 */       this.result = DeferredAttr.ArgumentExpressionKind.NO_POLY;
/*      */     }
/*      */ 
/*      */     public void visitConditional(JCTree.JCConditional paramJCConditional)
/*      */     {
/* 1207 */       scan(paramJCConditional.truepart);
/* 1208 */       if (!this.result.isPrimitive()) {
/* 1209 */         this.result = DeferredAttr.ArgumentExpressionKind.POLY;
/* 1210 */         return;
/*      */       }
/* 1212 */       scan(paramJCConditional.falsepart);
/* 1213 */       this.result = reduce(DeferredAttr.ArgumentExpressionKind.PRIMITIVE);
/*      */     }
/*      */ 
/*      */     public void visitNewClass(JCTree.JCNewClass paramJCNewClass)
/*      */     {
/* 1218 */       this.result = ((TreeInfo.isDiamond(paramJCNewClass)) || (DeferredAttr.this.attr.findDiamonds) ? DeferredAttr.ArgumentExpressionKind.POLY : DeferredAttr.ArgumentExpressionKind.NO_POLY);
/*      */     }
/*      */ 
/*      */     public void visitApply(JCTree.JCMethodInvocation paramJCMethodInvocation)
/*      */     {
/* 1224 */       Name localName = TreeInfo.name(paramJCMethodInvocation.meth);
/*      */ 
/* 1227 */       if ((paramJCMethodInvocation.typeargs.nonEmpty()) || (localName == localName.table.names._this) || (localName == localName.table.names._super))
/*      */       {
/* 1230 */         this.result = DeferredAttr.ArgumentExpressionKind.NO_POLY;
/* 1231 */         return;
/*      */       }
/*      */ 
/* 1235 */       Symbol localSymbol = quicklyResolveMethod(this.env, paramJCMethodInvocation);
/*      */ 
/* 1237 */       if (localSymbol == null) {
/* 1238 */         this.result = DeferredAttr.ArgumentExpressionKind.POLY;
/* 1239 */         return;
/*      */       }
/*      */ 
/* 1242 */       this.result = ((DeferredAttr.ArgumentExpressionKind)analyzeCandidateMethods(localSymbol, DeferredAttr.ArgumentExpressionKind.PRIMITIVE, this.argumentKindAnalyzer));
/*      */     }
/*      */ 
/*      */     private boolean isSimpleReceiver(JCTree paramJCTree)
/*      */     {
/* 1247 */       switch (DeferredAttr.6.$SwitchMap$com$sun$tools$javac$tree$JCTree$Tag[paramJCTree.getTag().ordinal()]) {
/*      */       case 1:
/* 1249 */         return true;
/*      */       case 2:
/* 1251 */         return isSimpleReceiver(((JCTree.JCFieldAccess)paramJCTree).selected);
/*      */       case 3:
/*      */       case 4:
/* 1254 */         return true;
/*      */       case 5:
/* 1256 */         return isSimpleReceiver(((JCTree.JCAnnotatedType)paramJCTree).underlyingType);
/*      */       case 6:
/* 1258 */         return true;
/*      */       case 7:
/* 1260 */         JCTree.JCNewClass localJCNewClass = (JCTree.JCNewClass)paramJCTree;
/* 1261 */         return (localJCNewClass.encl == null) && (localJCNewClass.def == null) && (!TreeInfo.isDiamond(localJCNewClass));
/*      */       }
/* 1263 */       return false;
/*      */     }
/*      */ 
/*      */     private DeferredAttr.ArgumentExpressionKind reduce(DeferredAttr.ArgumentExpressionKind paramArgumentExpressionKind) {
/* 1267 */       return (DeferredAttr.ArgumentExpressionKind)this.argumentKindAnalyzer.reduce(this.result, paramArgumentExpressionKind);
/*      */     }
/*      */ 
/*      */     public void visitLiteral(JCTree.JCLiteral paramJCLiteral)
/*      */     {
/* 1295 */       Type localType = DeferredAttr.this.attr.litType(paramJCLiteral.typetag);
/* 1296 */       this.result = DeferredAttr.ArgumentExpressionKind.standaloneKind(localType, DeferredAttr.this.types);
/*      */     }
/*      */ 
/*      */     void skip(JCTree paramJCTree)
/*      */     {
/* 1301 */       this.result = DeferredAttr.ArgumentExpressionKind.NO_POLY;
/*      */     }
/*      */ 
/*      */     private Symbol quicklyResolveMethod(Env<AttrContext> paramEnv, JCTree.JCMethodInvocation paramJCMethodInvocation) {
/* 1305 */       final JCTree localJCTree = paramJCMethodInvocation.meth.hasTag(JCTree.Tag.SELECT) ? ((JCTree.JCFieldAccess)paramJCMethodInvocation.meth).selected : null;
/*      */ 
/* 1309 */       if ((localJCTree != null) && (!isSimpleReceiver(localJCTree))) {
/* 1310 */         return null;
/*      */       }
/*      */ 
/* 1315 */       if (localJCTree != null)
/* 1316 */         switch (DeferredAttr.6.$SwitchMap$com$sun$tools$javac$tree$JCTree$Tag[localJCTree.getTag().ordinal()]) {
/*      */         case 6:
/* 1318 */           localObject1 = quicklyResolveMethod(paramEnv, (JCTree.JCMethodInvocation)localJCTree);
/* 1319 */           if (localObject1 == null) {
/* 1320 */             return null;
/*      */           }
/* 1322 */           localObject2 = (Symbol)analyzeCandidateMethods((Symbol)localObject1, DeferredAttr.this.syms.errSymbol, this.returnSymbolAnalyzer);
/*      */ 
/* 1323 */           if (localObject2 == null)
/* 1324 */             return null;
/* 1325 */           localType = ((Symbol)localObject2).type;
/* 1326 */           break;
/*      */         case 7:
/* 1328 */           localObject3 = (JCTree.JCNewClass)localJCTree;
/* 1329 */           localType = DeferredAttr.this.attribSpeculative(((JCTree.JCNewClass)localObject3).clazz, paramEnv, DeferredAttr.this.attr.unknownTypeExprInfo).type;
/* 1330 */           break;
/*      */         default:
/* 1332 */           localType = DeferredAttr.this.attribSpeculative(localJCTree, paramEnv, DeferredAttr.this.attr.unknownTypeExprInfo).type;
/* 1333 */           break;
/*      */         }
/*      */       else {
/* 1336 */         localType = paramEnv.enclClass.sym.type;
/*      */       }
/*      */ 
/* 1339 */       while (localType.hasTag(TypeTag.TYPEVAR)) {
/* 1340 */         localType = localType.getUpperBound();
/*      */       }
/*      */ 
/* 1343 */       Type localType = DeferredAttr.this.types.capture(localType);
/*      */ 
/* 1345 */       Object localObject1 = DeferredAttr.this.rs.dummyArgs(paramJCMethodInvocation.args.length());
/* 1346 */       Object localObject2 = TreeInfo.name(paramJCMethodInvocation.meth);
/*      */       Resolve tmp295_292 = DeferredAttr.this.rs; tmp295_292.getClass(); Object localObject3 = new Resolve.LookupHelper(tmp295_292, (Name)localObject2, localType, (List)localObject1, List.nil(), Resolve.MethodResolutionPhase.VARARITY)
/*      */       {
/*      */         Symbol lookup(Env<AttrContext> paramAnonymousEnv, Resolve.MethodResolutionPhase paramAnonymousMethodResolutionPhase)
/*      */         {
/* 1353 */           return localJCTree == null ? DeferredAttr.this.rs
/* 1352 */             .findFun(paramAnonymousEnv, this.name, this.argtypes, this.typeargtypes, paramAnonymousMethodResolutionPhase
/* 1352 */             .isBoxingRequired(), paramAnonymousMethodResolutionPhase.isVarargsRequired()) : DeferredAttr.this.rs
/* 1353 */             .findMethod(paramAnonymousEnv, this.site, this.name, this.argtypes, this.typeargtypes, paramAnonymousMethodResolutionPhase
/* 1353 */             .isBoxingRequired(), paramAnonymousMethodResolutionPhase.isVarargsRequired(), false);
/*      */         }
/*      */ 
/*      */         Symbol access(Env<AttrContext> paramAnonymousEnv, JCDiagnostic.DiagnosticPosition paramAnonymousDiagnosticPosition, Symbol paramAnonymousSymbol1, Symbol paramAnonymousSymbol2) {
/* 1357 */           return paramAnonymousSymbol2;
/*      */         }
/*      */       };
/* 1361 */       return DeferredAttr.this.rs.lookupMethod(paramEnv, paramJCMethodInvocation, localType.tsym, DeferredAttr.this.rs.arityMethodCheck, (Resolve.LookupHelper)localObject3);
/*      */     }
/*      */ 
/*      */     <E> E analyzeCandidateMethods(Symbol paramSymbol, E paramE, DeferredAttr.MethodAnalyzer<E> paramMethodAnalyzer)
/*      */     {
/* 1390 */       switch (paramSymbol.kind) {
/*      */       case 16:
/* 1392 */         return paramMethodAnalyzer.process((Symbol.MethodSymbol)paramSymbol);
/*      */       case 129:
/* 1394 */         Resolve.AmbiguityError localAmbiguityError = (Resolve.AmbiguityError)paramSymbol.baseSymbol();
/* 1395 */         Object localObject = paramE;
/* 1396 */         for (Symbol localSymbol : localAmbiguityError.ambiguousSyms) {
/* 1397 */           if (localSymbol.kind == 16) {
/* 1398 */             localObject = paramMethodAnalyzer.reduce(localObject, paramMethodAnalyzer.process((Symbol.MethodSymbol)localSymbol));
/* 1399 */             if (paramMethodAnalyzer.shouldStop(localObject))
/* 1400 */               return localObject;
/*      */           }
/*      */         }
/* 1403 */         return localObject;
/*      */       }
/* 1405 */       return paramE;
/*      */     }
/*      */   }
/*      */ 
/*      */   static abstract interface DeferredStuckPolicy
/*      */   {
/*      */     public abstract boolean isStuck();
/*      */ 
/*      */     public abstract Set<Type> stuckVars();
/*      */ 
/*      */     public abstract Set<Type> depVars();
/*      */   }
/*      */ 
/*      */   public class DeferredType extends Type
/*      */   {
/*      */     public JCTree.JCExpression tree;
/*      */     Env<AttrContext> env;
/*      */     DeferredAttr.AttrMode mode;
/*      */     SpeculativeCache speculativeCache;
/*      */ 
/*      */     DeferredType(Env<AttrContext> arg2)
/*      */     {
/*  140 */       super();
/*      */       Object localObject;
/*  141 */       this.tree = localObject;
/*      */       Env localEnv;
/*  142 */       this.env = DeferredAttr.this.attr.copyEnv(localEnv);
/*  143 */       this.speculativeCache = new SpeculativeCache();
/*      */     }
/*      */ 
/*      */     public TypeTag getTag()
/*      */     {
/*  148 */       return TypeTag.DEFERRED;
/*      */     }
/*      */ 
/*      */     public String toString()
/*      */     {
/*  153 */       return "DeferredType";
/*      */     }
/*      */ 
/*      */     Type speculativeType(Symbol paramSymbol, Resolve.MethodResolutionPhase paramMethodResolutionPhase)
/*      */     {
/*  212 */       DeferredAttr.DeferredType.SpeculativeCache.Entry localEntry = this.speculativeCache.get(paramSymbol, paramMethodResolutionPhase);
/*  213 */       return localEntry != null ? localEntry.speculativeTree.type : Type.noType;
/*      */     }
/*      */ 
/*      */     Type check(Attr.ResultInfo paramResultInfo)
/*      */     {
/*      */       Object localObject;
/*  225 */       if ((paramResultInfo.pt.hasTag(TypeTag.NONE)) || (paramResultInfo.pt.isErroneous()))
/*  226 */         localObject = DeferredAttr.this.dummyStuckPolicy;
/*  227 */       else if ((paramResultInfo.checkContext.deferredAttrContext().mode == DeferredAttr.AttrMode.SPECULATIVE) || 
/*  228 */         (paramResultInfo.checkContext
/*  228 */         .deferredAttrContext().insideOverloadPhase()))
/*  229 */         localObject = new DeferredAttr.OverloadStuckPolicy(DeferredAttr.this, paramResultInfo, this);
/*      */       else {
/*  231 */         localObject = new DeferredAttr.CheckStuckPolicy(DeferredAttr.this, paramResultInfo, this);
/*      */       }
/*  233 */       return check(paramResultInfo, (DeferredAttr.DeferredStuckPolicy)localObject, DeferredAttr.this.basicCompleter);
/*      */     }
/*      */ 
/*      */     private Type check(Attr.ResultInfo paramResultInfo, DeferredAttr.DeferredStuckPolicy paramDeferredStuckPolicy, DeferredAttr.DeferredTypeCompleter paramDeferredTypeCompleter)
/*      */     {
/*  239 */       DeferredAttr.DeferredAttrContext localDeferredAttrContext = paramResultInfo.checkContext
/*  239 */         .deferredAttrContext();
/*  240 */       Assert.check(localDeferredAttrContext != DeferredAttr.this.emptyDeferredAttrContext);
/*  241 */       if (paramDeferredStuckPolicy.isStuck()) {
/*  242 */         localDeferredAttrContext.addDeferredAttrNode(this, paramResultInfo, paramDeferredStuckPolicy);
/*  243 */         return Type.noType;
/*      */       }
/*      */       try {
/*  246 */         return paramDeferredTypeCompleter.complete(this, paramResultInfo, localDeferredAttrContext);
/*      */       } finally {
/*  248 */         this.mode = localDeferredAttrContext.mode;
/*      */       }
/*      */     }
/*      */ 
/*      */     class SpeculativeCache
/*      */     {
/*  164 */       private Map<Symbol, List<Entry>> cache = new WeakHashMap();
/*      */ 
/*      */       SpeculativeCache()
/*      */       {
/*      */       }
/*      */ 
/*      */       Entry get(Symbol paramSymbol, Resolve.MethodResolutionPhase paramMethodResolutionPhase)
/*      */       {
/*  186 */         List localList = (List)this.cache.get(paramSymbol);
/*  187 */         if (localList == null) return null;
/*  188 */         for (Entry localEntry : localList) {
/*  189 */           if (localEntry.matches(paramMethodResolutionPhase)) return localEntry;
/*      */         }
/*  191 */         return null;
/*      */       }
/*      */ 
/*      */       void put(JCTree paramJCTree, Attr.ResultInfo paramResultInfo)
/*      */       {
/*  199 */         Symbol localSymbol = paramResultInfo.checkContext.deferredAttrContext().msym;
/*  200 */         List localList = (List)this.cache.get(localSymbol);
/*  201 */         if (localList == null) {
/*  202 */           localList = List.nil();
/*      */         }
/*  204 */         this.cache.put(localSymbol, localList.prepend(new Entry(paramJCTree, paramResultInfo)));
/*      */       }
/*      */ 
/*      */       class Entry
/*      */       {
/*      */         JCTree speculativeTree;
/*      */         Attr.ResultInfo resultInfo;
/*      */ 
/*      */         public Entry(JCTree paramResultInfo, Attr.ResultInfo arg3)
/*      */         {
/*  172 */           this.speculativeTree = paramResultInfo;
/*      */           Object localObject;
/*  173 */           this.resultInfo = localObject;
/*      */         }
/*      */ 
/*      */         boolean matches(Resolve.MethodResolutionPhase paramMethodResolutionPhase) {
/*  177 */           return this.resultInfo.checkContext.deferredAttrContext().phase == paramMethodResolutionPhase;
/*      */         }
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   static abstract interface DeferredTypeCompleter
/*      */   {
/*      */     public abstract Type complete(DeferredAttr.DeferredType paramDeferredType, Attr.ResultInfo paramResultInfo, DeferredAttr.DeferredAttrContext paramDeferredAttrContext);
/*      */   }
/*      */ 
/*      */   class DeferredTypeMap extends Type.Mapping
/*      */   {
/*      */     DeferredAttr.DeferredAttrContext deferredAttrContext;
/*      */ 
/*      */     protected DeferredTypeMap(DeferredAttr.AttrMode paramSymbol, Symbol paramMethodResolutionPhase, Resolve.MethodResolutionPhase arg4)
/*      */     {
/*  828 */       super();
/*      */       Resolve.MethodResolutionPhase localMethodResolutionPhase;
/*  829 */       this.deferredAttrContext = new DeferredAttr.DeferredAttrContext(DeferredAttr.this, paramSymbol, paramMethodResolutionPhase, localMethodResolutionPhase, DeferredAttr.this.infer.emptyContext, DeferredAttr.this.emptyDeferredAttrContext, DeferredAttr.this.types.noWarnings);
/*      */     }
/*      */ 
/*      */     public Type apply(Type paramType)
/*      */     {
/*  835 */       if (!paramType.hasTag(TypeTag.DEFERRED)) {
/*  836 */         return paramType.map(this);
/*      */       }
/*  838 */       DeferredAttr.DeferredType localDeferredType = (DeferredAttr.DeferredType)paramType;
/*  839 */       return typeOf(localDeferredType);
/*      */     }
/*      */ 
/*      */     protected Type typeOf(DeferredAttr.DeferredType paramDeferredType)
/*      */     {
/*  844 */       switch (DeferredAttr.6.$SwitchMap$com$sun$tools$javac$comp$DeferredAttr$AttrMode[this.deferredAttrContext.mode.ordinal()]) {
/*      */       case 2:
/*  846 */         return paramDeferredType.tree.type == null ? Type.noType : paramDeferredType.tree.type;
/*      */       case 1:
/*  848 */         return paramDeferredType.speculativeType(this.deferredAttrContext.msym, this.deferredAttrContext.phase);
/*      */       }
/*  850 */       Assert.error();
/*  851 */       return null;
/*      */     }
/*      */   }
/*      */ 
/*      */   static abstract class FilterScanner extends TreeScanner
/*      */   {
/*      */     final Filter<JCTree> treeFilter;
/*      */ 
/*      */     FilterScanner(final Set<JCTree.Tag> paramSet)
/*      */     {
/*  902 */       this.treeFilter = new Filter() {
/*      */         public boolean accepts(JCTree paramAnonymousJCTree) {
/*  904 */           return paramSet.contains(paramAnonymousJCTree.getTag());
/*      */         }
/*      */       };
/*      */     }
/*      */ 
/*      */     public void scan(JCTree paramJCTree)
/*      */     {
/*  911 */       if (paramJCTree != null)
/*  912 */         if (this.treeFilter.accepts(paramJCTree))
/*  913 */           super.scan(paramJCTree);
/*      */         else
/*  915 */           skip(paramJCTree);
/*      */     }
/*      */ 
/*      */     void skip(JCTree paramJCTree)
/*      */     {
/*      */     }
/*      */   }
/*      */ 
/*      */   static class LambdaReturnScanner extends DeferredAttr.FilterScanner
/*      */   {
/*      */     LambdaReturnScanner()
/*      */     {
/*  944 */       super();
/*      */     }
/*      */   }
/*      */ 
/*      */   static abstract interface MethodAnalyzer<E>
/*      */   {
/*      */     public abstract E process(Symbol.MethodSymbol paramMethodSymbol);
/*      */ 
/*      */     public abstract E reduce(E paramE1, E paramE2);
/*      */ 
/*      */     public abstract boolean shouldStop(E paramE);
/*      */   }
/*      */ 
/*      */   class OverloadStuckPolicy extends DeferredAttr.CheckStuckPolicy
/*      */     implements DeferredAttr.DeferredStuckPolicy
/*      */   {
/*      */     boolean stuck;
/*      */ 
/*      */     public boolean isStuck()
/*      */     {
/* 1071 */       return (super.isStuck()) || (this.stuck);
/*      */     }
/*      */ 
/*      */     public OverloadStuckPolicy(Attr.ResultInfo paramDeferredType, DeferredAttr.DeferredType arg3) {
/* 1075 */       super(paramDeferredType, localDeferredType);
/*      */     }
/*      */ 
/*      */     public void visitLambda(JCTree.JCLambda paramJCLambda)
/*      */     {
/* 1080 */       super.visitLambda(paramJCLambda);
/* 1081 */       if (paramJCLambda.paramKind == JCTree.JCLambda.ParameterKind.IMPLICIT)
/* 1082 */         this.stuck = true;
/*      */     }
/*      */ 
/*      */     public void visitReference(JCTree.JCMemberReference paramJCMemberReference)
/*      */     {
/* 1088 */       super.visitReference(paramJCMemberReference);
/* 1089 */       if (paramJCMemberReference.overloadKind == JCTree.JCMemberReference.OverloadKind.OVERLOADED)
/* 1090 */         this.stuck = true;
/*      */     }
/*      */   }
/*      */ 
/*      */   static class PolyScanner extends DeferredAttr.FilterScanner
/*      */   {
/*      */     PolyScanner()
/*      */     {
/*  933 */       super();
/*      */     }
/*      */   }
/*      */ 
/*      */   public class RecoveryDeferredTypeMap extends DeferredAttr.DeferredTypeMap
/*      */   {
/*      */     public RecoveryDeferredTypeMap(DeferredAttr.AttrMode paramSymbol, Symbol paramMethodResolutionPhase, Resolve.MethodResolutionPhase arg4)
/*      */     {
/*  865 */       super(paramSymbol, paramMethodResolutionPhase, localObject != null ? localObject : Resolve.MethodResolutionPhase.BOX);
/*      */     }
/*      */ 
/*      */     protected Type typeOf(DeferredAttr.DeferredType paramDeferredType)
/*      */     {
/*  870 */       Type localType = super.typeOf(paramDeferredType);
/*      */ 
/*  872 */       return localType == Type.noType ? 
/*  872 */         recover(paramDeferredType) : 
/*  872 */         localType;
/*      */     }
/*      */ 
/*      */     private Type recover(DeferredAttr.DeferredType paramDeferredType)
/*      */     {
/*      */       Attr tmp13_10 = DeferredAttr.this.attr; tmp13_10.getClass(); paramDeferredType.check(new Attr.RecoveryInfo(tmp13_10, this.deferredAttrContext)
/*      */       {
/*      */         protected Type check(JCDiagnostic.DiagnosticPosition paramAnonymousDiagnosticPosition, Type paramAnonymousType) {
/*  886 */           return DeferredAttr.this.chk.checkNonVoid(paramAnonymousDiagnosticPosition, super.check(paramAnonymousDiagnosticPosition, paramAnonymousType));
/*      */         }
/*      */       });
/*  889 */       return super.apply(paramDeferredType);
/*      */     }
/*      */   }
/*      */ 
/*      */   class UnenterScanner extends TreeScanner
/*      */   {
/*      */     UnenterScanner()
/*      */     {
/*      */     }
/*      */ 
/*      */     public void visitClassDef(JCTree.JCClassDecl paramJCClassDecl)
/*      */     {
/*  401 */       Symbol.ClassSymbol localClassSymbol = paramJCClassDecl.sym;
/*      */ 
/*  405 */       if (localClassSymbol == null) return;
/*  406 */       DeferredAttr.this.typeEnvs.remove(localClassSymbol);
/*  407 */       DeferredAttr.this.chk.compiled.remove(localClassSymbol.flatname);
/*  408 */       DeferredAttr.this.syms.classes.remove(localClassSymbol.flatname);
/*  409 */       super.visitClassDef(paramJCClassDecl);
/*      */     }
/*      */   }
/*      */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.javac.comp.DeferredAttr
 * JD-Core Version:    0.6.2
 */