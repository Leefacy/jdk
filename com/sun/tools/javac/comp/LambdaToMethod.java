/*      */ package com.sun.tools.javac.comp;
/*      */ 
/*      */ import com.sun.source.tree.LambdaExpressionTree.BodyKind;
/*      */ import com.sun.source.tree.MemberReferenceTree.ReferenceMode;
/*      */ import com.sun.tools.javac.code.Attribute.TypeCompound;
/*      */ import com.sun.tools.javac.code.Scope;
/*      */ import com.sun.tools.javac.code.Symbol;
/*      */ import com.sun.tools.javac.code.Symbol.ClassSymbol;
/*      */ import com.sun.tools.javac.code.Symbol.DynamicMethodSymbol;
/*      */ import com.sun.tools.javac.code.Symbol.MethodSymbol;
/*      */ import com.sun.tools.javac.code.Symbol.TypeSymbol;
/*      */ import com.sun.tools.javac.code.Symbol.VarSymbol;
/*      */ import com.sun.tools.javac.code.Symtab;
/*      */ import com.sun.tools.javac.code.Type;
/*      */ import com.sun.tools.javac.code.Type.MethodType;
/*      */ import com.sun.tools.javac.code.Type.TypeVar;
/*      */ import com.sun.tools.javac.code.TypeAnnotationPosition;
/*      */ import com.sun.tools.javac.code.TypeTag;
/*      */ import com.sun.tools.javac.code.Types;
/*      */ import com.sun.tools.javac.code.Types.SignatureGenerator;
/*      */ import com.sun.tools.javac.jvm.Pool.MethodHandle;
/*      */ import com.sun.tools.javac.tree.JCTree;
/*      */ import com.sun.tools.javac.tree.JCTree.JCAssign;
/*      */ import com.sun.tools.javac.tree.JCTree.JCBinary;
/*      */ import com.sun.tools.javac.tree.JCTree.JCBlock;
/*      */ import com.sun.tools.javac.tree.JCTree.JCBreak;
/*      */ import com.sun.tools.javac.tree.JCTree.JCClassDecl;
/*      */ import com.sun.tools.javac.tree.JCTree.JCExpression;
/*      */ import com.sun.tools.javac.tree.JCTree.JCFieldAccess;
/*      */ import com.sun.tools.javac.tree.JCTree.JCFunctionalExpression;
/*      */ import com.sun.tools.javac.tree.JCTree.JCIdent;
/*      */ import com.sun.tools.javac.tree.JCTree.JCLambda;
/*      */ import com.sun.tools.javac.tree.JCTree.JCLiteral;
/*      */ import com.sun.tools.javac.tree.JCTree.JCMemberReference;
/*      */ import com.sun.tools.javac.tree.JCTree.JCMemberReference.ReferenceKind;
/*      */ import com.sun.tools.javac.tree.JCTree.JCMethodDecl;
/*      */ import com.sun.tools.javac.tree.JCTree.JCMethodInvocation;
/*      */ import com.sun.tools.javac.tree.JCTree.JCNewArray;
/*      */ import com.sun.tools.javac.tree.JCTree.JCNewClass;
/*      */ import com.sun.tools.javac.tree.JCTree.JCReturn;
/*      */ import com.sun.tools.javac.tree.JCTree.JCStatement;
/*      */ import com.sun.tools.javac.tree.JCTree.JCTypeCast;
/*      */ import com.sun.tools.javac.tree.JCTree.JCVariableDecl;
/*      */ import com.sun.tools.javac.tree.JCTree.Tag;
/*      */ import com.sun.tools.javac.tree.TreeInfo;
/*      */ import com.sun.tools.javac.tree.TreeMaker;
/*      */ import com.sun.tools.javac.tree.TreeTranslator;
/*      */ import com.sun.tools.javac.util.Assert;
/*      */ import com.sun.tools.javac.util.Context;
/*      */ import com.sun.tools.javac.util.Context.Key;
/*      */ import com.sun.tools.javac.util.DiagnosticSource;
/*      */ import com.sun.tools.javac.util.JCDiagnostic.DiagnosticPosition;
/*      */ import com.sun.tools.javac.util.JCDiagnostic.Factory;
/*      */ import com.sun.tools.javac.util.List;
/*      */ import com.sun.tools.javac.util.ListBuffer;
/*      */ import com.sun.tools.javac.util.Log;
/*      */ import com.sun.tools.javac.util.Name;
/*      */ import com.sun.tools.javac.util.Names;
/*      */ import com.sun.tools.javac.util.Options;
/*      */ import java.util.Collection;
/*      */ import java.util.EnumMap;
/*      */ import java.util.HashMap;
/*      */ import java.util.HashSet;
/*      */ import java.util.Iterator;
/*      */ import java.util.LinkedHashMap;
/*      */ import java.util.Map;
/*      */ import java.util.Map.Entry;
/*      */ import java.util.Set;
/*      */ import javax.lang.model.type.TypeKind;
/*      */ 
/*      */ public class LambdaToMethod extends TreeTranslator
/*      */ {
/*      */   private Attr attr;
/*      */   private JCDiagnostic.Factory diags;
/*      */   private Log log;
/*      */   private Lower lower;
/*      */   private Names names;
/*      */   private Symtab syms;
/*      */   private Resolve rs;
/*      */   private TreeMaker make;
/*      */   private Types types;
/*      */   private TransTypes transTypes;
/*      */   private Env<AttrContext> attrEnv;
/*      */   private LambdaAnalyzerPreprocessor analyzer;
/*      */   private Map<JCTree, LambdaToMethod.LambdaAnalyzerPreprocessor.TranslationContext<?>> contextMap;
/*      */   private LambdaToMethod.LambdaAnalyzerPreprocessor.TranslationContext<?> context;
/*      */   private KlassInfo kInfo;
/*      */   private boolean dumpLambdaToMethodStats;
/*      */   private final boolean forceSerializable;
/*      */   public static final int FLAG_SERIALIZABLE = 1;
/*      */   public static final int FLAG_MARKERS = 2;
/*      */   public static final int FLAG_BRIDGES = 4;
/*  117 */   protected static final Context.Key<LambdaToMethod> unlambdaKey = new Context.Key();
/*      */ 
/*      */   public static LambdaToMethod instance(Context paramContext)
/*      */   {
/*  121 */     LambdaToMethod localLambdaToMethod = (LambdaToMethod)paramContext.get(unlambdaKey);
/*  122 */     if (localLambdaToMethod == null) {
/*  123 */       localLambdaToMethod = new LambdaToMethod(paramContext);
/*      */     }
/*  125 */     return localLambdaToMethod;
/*      */   }
/*      */   private LambdaToMethod(Context paramContext) {
/*  128 */     paramContext.put(unlambdaKey, this);
/*  129 */     this.diags = JCDiagnostic.Factory.instance(paramContext);
/*  130 */     this.log = Log.instance(paramContext);
/*  131 */     this.lower = Lower.instance(paramContext);
/*  132 */     this.names = Names.instance(paramContext);
/*  133 */     this.syms = Symtab.instance(paramContext);
/*  134 */     this.rs = Resolve.instance(paramContext);
/*  135 */     this.make = TreeMaker.instance(paramContext);
/*  136 */     this.types = Types.instance(paramContext);
/*  137 */     this.transTypes = TransTypes.instance(paramContext);
/*  138 */     this.analyzer = new LambdaAnalyzerPreprocessor();
/*  139 */     Options localOptions = Options.instance(paramContext);
/*  140 */     this.dumpLambdaToMethodStats = localOptions.isSet("dumpLambdaToMethodStats");
/*  141 */     this.attr = Attr.instance(paramContext);
/*  142 */     this.forceSerializable = localOptions.isSet("forceSerializable");
/*      */   }
/*      */ 
/*      */   public <T extends JCTree> T translate(T paramT)
/*      */   {
/*  189 */     LambdaToMethod.LambdaAnalyzerPreprocessor.TranslationContext localTranslationContext = (LambdaToMethod.LambdaAnalyzerPreprocessor.TranslationContext)this.contextMap.get(paramT);
/*  190 */     return translate(paramT, localTranslationContext != null ? localTranslationContext : this.context);
/*      */   }
/*      */ 
/*      */   <T extends JCTree> T translate(T paramT, LambdaToMethod.LambdaAnalyzerPreprocessor.TranslationContext<?> paramTranslationContext) {
/*  194 */     LambdaToMethod.LambdaAnalyzerPreprocessor.TranslationContext localTranslationContext = this.context;
/*      */     try {
/*  196 */       this.context = paramTranslationContext;
/*  197 */       return super.translate(paramT);
/*      */     }
/*      */     finally {
/*  200 */       this.context = localTranslationContext;
/*      */     }
/*      */   }
/*      */ 
/*      */   <T extends JCTree> List<T> translate(List<T> paramList, LambdaToMethod.LambdaAnalyzerPreprocessor.TranslationContext<?> paramTranslationContext) {
/*  205 */     ListBuffer localListBuffer = new ListBuffer();
/*  206 */     for (JCTree localJCTree : paramList) {
/*  207 */       localListBuffer.append(translate(localJCTree, paramTranslationContext));
/*      */     }
/*  209 */     return localListBuffer.toList();
/*      */   }
/*      */ 
/*      */   public JCTree translateTopLevelClass(Env<AttrContext> paramEnv, JCTree paramJCTree, TreeMaker paramTreeMaker) {
/*  213 */     this.make = paramTreeMaker;
/*  214 */     this.attrEnv = paramEnv;
/*  215 */     this.context = null;
/*  216 */     this.contextMap = new HashMap();
/*  217 */     return translate(paramJCTree);
/*      */   }
/*      */ 
/*      */   public void visitClassDef(JCTree.JCClassDecl paramJCClassDecl)
/*      */   {
/*  230 */     if (paramJCClassDecl.sym.owner.kind == 1)
/*      */     {
/*  232 */       paramJCClassDecl = this.analyzer.analyzeAndPreprocessClass(paramJCClassDecl);
/*      */     }
/*  234 */     KlassInfo localKlassInfo = this.kInfo;
/*      */     try {
/*  236 */       this.kInfo = new KlassInfo(paramJCClassDecl, null);
/*  237 */       super.visitClassDef(paramJCClassDecl);
/*  238 */       if (!this.kInfo.deserializeCases.isEmpty()) {
/*  239 */         int i = this.make.pos;
/*      */         try {
/*  241 */           this.make.at(paramJCClassDecl);
/*  242 */           this.kInfo.addMethod(makeDeserializeMethod(paramJCClassDecl.sym));
/*      */         } finally {
/*  244 */           this.make.at(i);
/*      */         }
/*      */       }
/*      */ 
/*  248 */       List localList = this.kInfo.appendedMethodList.toList();
/*  249 */       paramJCClassDecl.defs = paramJCClassDecl.defs.appendList(localList);
/*  250 */       for (JCTree localJCTree : localList) {
/*  251 */         paramJCClassDecl.sym.members().enter(((JCTree.JCMethodDecl)localJCTree).sym);
/*      */       }
/*  253 */       this.result = paramJCClassDecl;
/*      */     } finally {
/*  255 */       this.kInfo = localKlassInfo;
/*      */     }
/*      */   }
/*      */ 
/*      */   public void visitLambda(JCTree.JCLambda paramJCLambda)
/*      */   {
/*  267 */     LambdaToMethod.LambdaAnalyzerPreprocessor.LambdaTranslationContext localLambdaTranslationContext = (LambdaToMethod.LambdaAnalyzerPreprocessor.LambdaTranslationContext)this.context;
/*  268 */     Symbol.MethodSymbol localMethodSymbol = localLambdaTranslationContext.translatedSym;
/*  269 */     Type.MethodType localMethodType = (Type.MethodType)localMethodSymbol.type;
/*      */ 
/*  272 */     Object localObject1 = localLambdaTranslationContext.owner;
/*  273 */     ListBuffer localListBuffer = new ListBuffer();
/*  274 */     Object localObject2 = new ListBuffer();
/*      */ 
/*  276 */     for (Object localObject3 = ((Symbol)localObject1).getRawTypeAttributes().iterator(); ((Iterator)localObject3).hasNext(); ) { localObject4 = (Attribute.TypeCompound)((Iterator)localObject3).next();
/*  277 */       if (((Attribute.TypeCompound)localObject4).position.onLambda == paramJCLambda)
/*  278 */         ((ListBuffer)localObject2).append(localObject4);
/*      */       else
/*  280 */         localListBuffer.append(localObject4);
/*      */     }
/*      */     Object localObject4;
/*  283 */     if (((ListBuffer)localObject2).nonEmpty()) {
/*  284 */       ((Symbol)localObject1).setTypeAttributes(localListBuffer.toList());
/*  285 */       localMethodSymbol.setTypeAttributes(((ListBuffer)localObject2).toList());
/*      */     }
/*      */ 
/*  290 */     localObject1 = this.make.MethodDef(this.make.Modifiers(localMethodSymbol.flags_field), localMethodSymbol.name, this.make
/*  292 */       .QualIdent(localMethodType
/*  292 */       .getReturnType().tsym), 
/*  293 */       List.nil(), localLambdaTranslationContext.syntheticParams, localMethodType
/*  295 */       .getThrownTypes() == null ? 
/*  296 */       List.nil() : this.make
/*  297 */       .Types(localMethodType
/*  297 */       .getThrownTypes()), null, null);
/*      */ 
/*  300 */     ((JCTree.JCMethodDecl)localObject1).sym = localMethodSymbol;
/*  301 */     ((JCTree.JCMethodDecl)localObject1).type = localMethodType;
/*      */ 
/*  308 */     ((JCTree.JCMethodDecl)localObject1).body = ((JCTree.JCBlock)translate(makeLambdaBody(paramJCLambda, (JCTree.JCMethodDecl)localObject1)));
/*      */ 
/*  311 */     this.kInfo.addMethod((JCTree)localObject1);
/*      */ 
/*  324 */     localListBuffer = new ListBuffer();
/*      */ 
/*  326 */     if (localLambdaTranslationContext.methodReferenceReceiver != null)
/*  327 */       localListBuffer.append(localLambdaTranslationContext.methodReferenceReceiver);
/*  328 */     else if (!localMethodSymbol.isStatic()) {
/*  329 */       localListBuffer.append(makeThis(localMethodSymbol.owner
/*  330 */         .enclClass().asType(), localLambdaTranslationContext.owner
/*  331 */         .enclClass()));
/*      */     }
/*      */ 
/*  335 */     for (localObject2 = localLambdaTranslationContext.getSymbolMap(LambdaSymbolKind.CAPTURED_VAR).keySet().iterator(); ((Iterator)localObject2).hasNext(); ) { localObject3 = (Symbol)((Iterator)localObject2).next();
/*  336 */       if (localObject3 != localLambdaTranslationContext.self) {
/*  337 */         localObject4 = this.make.Ident((Symbol)localObject3).setType(((Symbol)localObject3).type);
/*  338 */         localListBuffer.append((JCTree.JCExpression)localObject4);
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/*  343 */     localObject2 = translate(localListBuffer.toList(), localLambdaTranslationContext.prev);
/*      */ 
/*  346 */     int i = referenceKind(localMethodSymbol);
/*      */ 
/*  349 */     this.result = makeMetafactoryIndyCall(this.context, i, localMethodSymbol, (List)localObject2);
/*      */   }
/*      */ 
/*      */   private JCTree.JCIdent makeThis(Type paramType, Symbol paramSymbol) {
/*  353 */     Symbol.VarSymbol localVarSymbol = new Symbol.VarSymbol(8589938704L, this.names._this, paramType, paramSymbol);
/*      */ 
/*  357 */     return this.make.Ident(localVarSymbol);
/*      */   }
/*      */ 
/*      */   public void visitReference(JCTree.JCMemberReference paramJCMemberReference)
/*      */   {
/*  367 */     LambdaToMethod.LambdaAnalyzerPreprocessor.ReferenceTranslationContext localReferenceTranslationContext = (LambdaToMethod.LambdaAnalyzerPreprocessor.ReferenceTranslationContext)this.context;
/*      */ 
/*  371 */     Symbol localSymbol = localReferenceTranslationContext.isSignaturePolymorphic() ? localReferenceTranslationContext.sigPolySym : paramJCMemberReference.sym;
/*      */     Object localObject;
/*  377 */     switch (1.$SwitchMap$com$sun$tools$javac$tree$JCTree$JCMemberReference$ReferenceKind[paramJCMemberReference.kind.ordinal()])
/*      */     {
/*      */     case 1:
/*      */     case 2:
/*  381 */       localObject = makeThis(localReferenceTranslationContext.owner
/*  382 */         .enclClass().asType(), localReferenceTranslationContext.owner
/*  383 */         .enclClass());
/*  384 */       break;
/*      */     case 3:
/*  387 */       localObject = paramJCMemberReference.getQualifierExpression();
/*  388 */       localObject = this.attr.makeNullCheck((JCTree.JCExpression)localObject);
/*  389 */       break;
/*      */     case 4:
/*      */     case 5:
/*      */     case 6:
/*      */     case 7:
/*  395 */       localObject = null;
/*  396 */       break;
/*      */     default:
/*  399 */       throw new InternalError("Should not have an invalid kind");
/*      */     }
/*      */ 
/*  402 */     List localList = localObject == null ? List.nil() : translate(List.of(localObject), localReferenceTranslationContext.prev);
/*      */ 
/*  406 */     this.result = makeMetafactoryIndyCall(localReferenceTranslationContext, localReferenceTranslationContext.referenceKind(), localSymbol, localList);
/*      */   }
/*      */ 
/*      */   public void visitIdent(JCTree.JCIdent paramJCIdent)
/*      */   {
/*  415 */     if ((this.context == null) || (!this.analyzer.lambdaIdentSymbolFilter(paramJCIdent.sym))) {
/*  416 */       super.visitIdent(paramJCIdent);
/*      */     } else {
/*  418 */       int i = this.make.pos;
/*      */       try {
/*  420 */         this.make.at(paramJCIdent);
/*      */ 
/*  422 */         LambdaToMethod.LambdaAnalyzerPreprocessor.LambdaTranslationContext localLambdaTranslationContext = (LambdaToMethod.LambdaAnalyzerPreprocessor.LambdaTranslationContext)this.context;
/*  423 */         JCTree localJCTree = localLambdaTranslationContext.translate(paramJCIdent);
/*  424 */         if (localJCTree != null) {
/*  425 */           this.result = localJCTree;
/*      */         }
/*      */         else
/*      */         {
/*  429 */           super.visitIdent(paramJCIdent);
/*      */         }
/*      */       } finally {
/*  432 */         this.make.at(i);
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   public void visitVarDef(JCTree.JCVariableDecl paramJCVariableDecl)
/*      */   {
/*  439 */     LambdaToMethod.LambdaAnalyzerPreprocessor.LambdaTranslationContext localLambdaTranslationContext = (LambdaToMethod.LambdaAnalyzerPreprocessor.LambdaTranslationContext)this.context;
/*  440 */     if ((this.context != null) && (localLambdaTranslationContext.getSymbolMap(LambdaSymbolKind.LOCAL_VAR).containsKey(paramJCVariableDecl.sym))) {
/*  441 */       paramJCVariableDecl.init = ((JCTree.JCExpression)translate(paramJCVariableDecl.init));
/*  442 */       paramJCVariableDecl.sym = ((Symbol.VarSymbol)localLambdaTranslationContext.getSymbolMap(LambdaSymbolKind.LOCAL_VAR).get(paramJCVariableDecl.sym));
/*  443 */       this.result = paramJCVariableDecl;
/*  444 */     } else if ((this.context != null) && (localLambdaTranslationContext.getSymbolMap(LambdaSymbolKind.TYPE_VAR).containsKey(paramJCVariableDecl.sym))) {
/*  445 */       JCTree.JCExpression localJCExpression = (JCTree.JCExpression)translate(paramJCVariableDecl.init);
/*  446 */       Symbol.VarSymbol localVarSymbol = (Symbol.VarSymbol)localLambdaTranslationContext.getSymbolMap(LambdaSymbolKind.TYPE_VAR).get(paramJCVariableDecl.sym);
/*  447 */       int i = this.make.pos;
/*      */       try {
/*  449 */         this.result = this.make.at(paramJCVariableDecl).VarDef(localVarSymbol, localJCExpression);
/*      */       } finally {
/*  451 */         this.make.at(i);
/*      */       }
/*      */ 
/*  454 */       Scope localScope = paramJCVariableDecl.sym.owner.members();
/*  455 */       if (localScope != null) {
/*  456 */         localScope.remove(paramJCVariableDecl.sym);
/*  457 */         localScope.enter(localVarSymbol);
/*      */       }
/*      */     } else {
/*  460 */       super.visitVarDef(paramJCVariableDecl);
/*      */     }
/*      */   }
/*      */ 
/*      */   private JCTree.JCBlock makeLambdaBody(JCTree.JCLambda paramJCLambda, JCTree.JCMethodDecl paramJCMethodDecl)
/*      */   {
/*  471 */     return paramJCLambda.getBodyKind() == LambdaExpressionTree.BodyKind.EXPRESSION ? 
/*  470 */       makeLambdaExpressionBody((JCTree.JCExpression)paramJCLambda.body, paramJCMethodDecl) : 
/*  471 */       makeLambdaStatementBody((JCTree.JCBlock)paramJCLambda.body, paramJCMethodDecl, paramJCLambda.canCompleteNormally);
/*      */   }
/*      */ 
/*      */   private JCTree.JCBlock makeLambdaExpressionBody(JCTree.JCExpression paramJCExpression, JCTree.JCMethodDecl paramJCMethodDecl)
/*      */   {
/*  475 */     Type localType = paramJCMethodDecl.type.getReturnType();
/*  476 */     boolean bool1 = paramJCExpression.type.hasTag(TypeTag.VOID);
/*  477 */     boolean bool2 = localType.hasTag(TypeTag.VOID);
/*  478 */     boolean bool3 = this.types.isSameType(localType, this.types.boxedClass(this.syms.voidType).type);
/*  479 */     int i = this.make.pos;
/*      */     try
/*      */     {
/*      */       JCTree.JCBlock localJCBlock;
/*  481 */       if (bool2)
/*      */       {
/*  484 */         localObject1 = this.make.at(paramJCExpression).Exec(paramJCExpression);
/*  485 */         return this.make.Block(0L, List.of(localObject1));
/*  486 */       }if ((bool1) && (bool3))
/*      */       {
/*  489 */         localObject1 = new ListBuffer();
/*  490 */         ((ListBuffer)localObject1).append(this.make.at(paramJCExpression).Exec(paramJCExpression));
/*  491 */         ((ListBuffer)localObject1).append(this.make.Return(this.make.Literal(TypeTag.BOT, null).setType(this.syms.botType)));
/*  492 */         return this.make.Block(0L, ((ListBuffer)localObject1).toList());
/*      */       }
/*      */ 
/*  496 */       Object localObject1 = this.transTypes.coerce(this.attrEnv, paramJCExpression, localType);
/*  497 */       return this.make.at((JCDiagnostic.DiagnosticPosition)localObject1).Block(0L, List.of(this.make.Return((JCTree.JCExpression)localObject1)));
/*      */     }
/*      */     finally {
/*  500 */       this.make.at(i);
/*      */     }
/*      */   }
/*      */ 
/*      */   private JCTree.JCBlock makeLambdaStatementBody(JCTree.JCBlock paramJCBlock, final JCTree.JCMethodDecl paramJCMethodDecl, boolean paramBoolean) {
/*  505 */     final Type localType = paramJCMethodDecl.type.getReturnType();
/*  506 */     final boolean bool1 = localType.hasTag(TypeTag.VOID);
/*  507 */     boolean bool2 = this.types.isSameType(localType, this.types.boxedClass(this.syms.voidType).type);
/*      */ 
/*  544 */     JCTree.JCBlock localJCBlock = (JCTree.JCBlock)new TreeTranslator()
/*      */     {
/*      */       public void visitClassDef(JCTree.JCClassDecl paramAnonymousJCClassDecl)
/*      */       {
/*  514 */         this.result = paramAnonymousJCClassDecl;
/*      */       }
/*      */ 
/*      */       public void visitLambda(JCTree.JCLambda paramAnonymousJCLambda)
/*      */       {
/*  520 */         this.result = paramAnonymousJCLambda;
/*      */       }
/*      */ 
/*      */       public void visitReturn(JCTree.JCReturn paramAnonymousJCReturn)
/*      */       {
/*  525 */         int i = paramAnonymousJCReturn.expr == null ? 1 : 0;
/*  526 */         if ((bool1) && (i == 0))
/*      */         {
/*  529 */           Symbol.VarSymbol localVarSymbol = this.this$0.makeSyntheticVar(0L, this.this$0.names.fromString("$loc"), paramAnonymousJCReturn.expr.type, paramJCMethodDecl.sym);
/*  530 */           JCTree.JCVariableDecl localJCVariableDecl = this.this$0.make.VarDef(localVarSymbol, paramAnonymousJCReturn.expr);
/*  531 */           this.result = this.this$0.make.Block(0L, List.of(localJCVariableDecl, this.this$0.make.Return(null)));
/*  532 */         } else if ((!bool1) || (i == 0))
/*      */         {
/*  535 */           paramAnonymousJCReturn.expr = this.this$0.transTypes.coerce(this.this$0.attrEnv, paramAnonymousJCReturn.expr, localType);
/*  536 */           this.result = paramAnonymousJCReturn;
/*      */         } else {
/*  538 */           this.result = paramAnonymousJCReturn;
/*      */         }
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/*  544 */     .translate(paramJCBlock);
/*  545 */     if ((paramBoolean) && (bool2))
/*      */     {
/*  548 */       localJCBlock.stats = localJCBlock.stats.append(this.make.Return(this.make.Literal(TypeTag.BOT, null).setType(this.syms.botType)));
/*      */     }
/*  550 */     return localJCBlock;
/*      */   }
/*      */ 
/*      */   private JCTree.JCMethodDecl makeDeserializeMethod(Symbol paramSymbol) {
/*  554 */     ListBuffer localListBuffer1 = new ListBuffer();
/*  555 */     ListBuffer localListBuffer2 = new ListBuffer();
/*  556 */     for (Object localObject1 = this.kInfo.deserializeCases.entrySet().iterator(); ((Iterator)localObject1).hasNext(); ) { localObject2 = (Map.Entry)((Iterator)localObject1).next();
/*  557 */       localObject3 = this.make.Break(null);
/*  558 */       localListBuffer2.add(localObject3);
/*  559 */       List localList = ((ListBuffer)((Map.Entry)localObject2).getValue()).append(localObject3).toList();
/*  560 */       localListBuffer1.add(this.make.Case(this.make.Literal(((Map.Entry)localObject2).getKey()), localList));
/*      */     }
/*  562 */     localObject1 = this.make.Switch(deserGetter("getImplMethodName", this.syms.stringType), localListBuffer1.toList());
/*  563 */     for (Object localObject2 = localListBuffer2.iterator(); ((Iterator)localObject2).hasNext(); ) { localObject3 = (JCTree.JCBreak)((Iterator)localObject2).next();
/*  564 */       ((JCTree.JCBreak)localObject3).target = ((JCTree)localObject1);
/*      */     }
/*  566 */     localObject2 = this.make.Block(0L, List.of(localObject1, this.make
/*  568 */       .Throw(makeNewClass(this.syms.illegalArgumentExceptionType, 
/*  570 */       List.of(this.make
/*  570 */       .Literal("Invalid lambda deserialization"))))));
/*      */ 
/*  571 */     Object localObject3 = this.make.MethodDef(this.make.Modifiers(this.kInfo.deserMethodSym.flags()), this.names.deserializeLambda, this.make
/*  573 */       .QualIdent(this.kInfo.deserMethodSym
/*  573 */       .getReturnType().tsym), 
/*  574 */       List.nil(), 
/*  575 */       List.of(this.make
/*  575 */       .VarDef(this.kInfo.deserParamSym, 
/*  575 */       null)), 
/*  576 */       List.nil(), (JCTree.JCBlock)localObject2, null);
/*      */ 
/*  579 */     ((JCTree.JCMethodDecl)localObject3).sym = this.kInfo.deserMethodSym;
/*  580 */     ((JCTree.JCMethodDecl)localObject3).type = this.kInfo.deserMethodSym.type;
/*      */ 
/*  582 */     return localObject3;
/*      */   }
/*      */ 
/*      */   JCTree.JCNewClass makeNewClass(Type paramType, List<JCTree.JCExpression> paramList, Symbol paramSymbol)
/*      */   {
/*  591 */     JCTree.JCNewClass localJCNewClass = this.make.NewClass(null, null, this.make
/*  592 */       .QualIdent(paramType.tsym), 
/*  592 */       paramList, null);
/*  593 */     localJCNewClass.constructor = paramSymbol;
/*  594 */     localJCNewClass.type = paramType;
/*  595 */     return localJCNewClass;
/*      */   }
/*      */ 
/*      */   JCTree.JCNewClass makeNewClass(Type paramType, List<JCTree.JCExpression> paramList)
/*      */   {
/*  603 */     return makeNewClass(paramType, paramList, this.rs
/*  604 */       .resolveConstructor(null, this.attrEnv, paramType, 
/*  604 */       TreeInfo.types(paramList), 
/*  604 */       List.nil()));
/*      */   }
/*      */ 
/*      */   private void addDeserializationCase(int paramInt, Symbol paramSymbol, Type paramType, Symbol.MethodSymbol paramMethodSymbol, JCDiagnostic.DiagnosticPosition paramDiagnosticPosition, List<Object> paramList, Type.MethodType paramMethodType)
/*      */   {
/*  609 */     String str1 = classSig(paramType);
/*  610 */     String str2 = paramMethodSymbol.getSimpleName().toString();
/*  611 */     String str3 = typeSig(this.types.erasure(paramMethodSymbol.type));
/*  612 */     String str4 = classSig(this.types.erasure(paramSymbol.owner.type));
/*  613 */     String str5 = paramSymbol.getQualifiedName().toString();
/*  614 */     String str6 = typeSig(this.types.erasure(paramSymbol.type));
/*      */ 
/*  616 */     JCTree.JCExpression localJCExpression = eqTest(this.syms.intType, deserGetter("getImplMethodKind", this.syms.intType), this.make.Literal(Integer.valueOf(paramInt)));
/*  617 */     ListBuffer localListBuffer = new ListBuffer();
/*  618 */     int i = 0;
/*  619 */     for (Object localObject1 = paramMethodType.getParameterTypes().iterator(); ((Iterator)localObject1).hasNext(); ) { localObject2 = (Type)((Iterator)localObject1).next();
/*  620 */       List localList1 = new ListBuffer().append(this.make.Literal(Integer.valueOf(i))).toList();
/*  621 */       List localList2 = new ListBuffer().append(this.syms.intType).toList();
/*  622 */       localListBuffer.add(this.make.TypeCast(this.types.erasure((Type)localObject2), deserGetter("getCapturedArg", this.syms.objectType, localList2, localList1)));
/*  623 */       i++;
/*      */     }
/*  625 */     localObject1 = this.make.If(
/*  626 */       deserTest(deserTest(deserTest(deserTest(deserTest(localJCExpression, "getFunctionalInterfaceClass", str1), 
/*  626 */       "getFunctionalInterfaceMethodName", str2), "getFunctionalInterfaceMethodSignature", str3), "getImplClass", str4), "getImplMethodSignature", str6), this.make
/*  633 */       .Return(makeIndyCall(paramDiagnosticPosition, this.syms.lambdaMetafactory, this.names.altMetafactory, paramList, paramMethodType, localListBuffer
/*  637 */       .toList(), paramMethodSymbol.name)), null);
/*      */ 
/*  639 */     Object localObject2 = (ListBuffer)this.kInfo.deserializeCases.get(str5);
/*  640 */     if (localObject2 == null) {
/*  641 */       localObject2 = new ListBuffer();
/*  642 */       this.kInfo.deserializeCases.put(str5, localObject2);
/*      */     }
/*      */ 
/*  654 */     ((ListBuffer)localObject2).append(localObject1);
/*      */   }
/*      */ 
/*      */   private JCTree.JCExpression eqTest(Type paramType, JCTree.JCExpression paramJCExpression1, JCTree.JCExpression paramJCExpression2) {
/*  658 */     JCTree.JCBinary localJCBinary = this.make.Binary(JCTree.Tag.EQ, paramJCExpression1, paramJCExpression2);
/*  659 */     localJCBinary.operator = this.rs.resolveBinaryOperator(null, JCTree.Tag.EQ, this.attrEnv, paramType, paramType);
/*  660 */     localJCBinary.setType(this.syms.booleanType);
/*  661 */     return localJCBinary;
/*      */   }
/*      */ 
/*      */   private JCTree.JCExpression deserTest(JCTree.JCExpression paramJCExpression, String paramString1, String paramString2) {
/*  665 */     Type.MethodType localMethodType = new Type.MethodType(List.of(this.syms.objectType), this.syms.booleanType, List.nil(), this.syms.methodClass);
/*  666 */     Symbol localSymbol = this.rs.resolveQualifiedMethod(null, this.attrEnv, this.syms.objectType, this.names.equals, List.of(this.syms.objectType), List.nil());
/*  667 */     JCTree.JCMethodInvocation localJCMethodInvocation = this.make.Apply(
/*  668 */       List.nil(), this.make
/*  669 */       .Select(deserGetter(paramString1, this.syms.stringType), 
/*  669 */       localSymbol).setType(localMethodType), 
/*  670 */       List.of(this.make
/*  670 */       .Literal(paramString2)));
/*      */ 
/*  671 */     localJCMethodInvocation.setType(this.syms.booleanType);
/*  672 */     JCTree.JCBinary localJCBinary = this.make.Binary(JCTree.Tag.AND, paramJCExpression, localJCMethodInvocation);
/*  673 */     localJCBinary.operator = this.rs.resolveBinaryOperator(null, JCTree.Tag.AND, this.attrEnv, this.syms.booleanType, this.syms.booleanType);
/*  674 */     localJCBinary.setType(this.syms.booleanType);
/*  675 */     return localJCBinary;
/*      */   }
/*      */ 
/*      */   private JCTree.JCExpression deserGetter(String paramString, Type paramType) {
/*  679 */     return deserGetter(paramString, paramType, List.nil(), List.nil());
/*      */   }
/*      */ 
/*      */   private JCTree.JCExpression deserGetter(String paramString, Type paramType, List<Type> paramList, List<JCTree.JCExpression> paramList1) {
/*  683 */     Type.MethodType localMethodType = new Type.MethodType(paramList, paramType, List.nil(), this.syms.methodClass);
/*  684 */     Symbol localSymbol = this.rs.resolveQualifiedMethod(null, this.attrEnv, this.syms.serializedLambdaType, this.names.fromString(paramString), paramList, List.nil());
/*      */ 
/*  688 */     return this.make.Apply(
/*  686 */       List.nil(), this.make
/*  687 */       .Select(this.make
/*  687 */       .Ident(this.kInfo.deserParamSym)
/*  687 */       .setType(this.syms.serializedLambdaType), localSymbol).setType(localMethodType), paramList1)
/*  688 */       .setType(paramType);
/*      */   }
/*      */ 
/*      */   private Symbol.MethodSymbol makePrivateSyntheticMethod(long paramLong, Name paramName, Type paramType, Symbol paramSymbol)
/*      */   {
/*  695 */     return new Symbol.MethodSymbol(paramLong | 0x1000 | 0x2, paramName, paramType, paramSymbol);
/*      */   }
/*      */ 
/*      */   private Symbol.VarSymbol makeSyntheticVar(long paramLong, String paramString, Type paramType, Symbol paramSymbol)
/*      */   {
/*  702 */     return makeSyntheticVar(paramLong, this.names.fromString(paramString), paramType, paramSymbol);
/*      */   }
/*      */ 
/*      */   private Symbol.VarSymbol makeSyntheticVar(long paramLong, Name paramName, Type paramType, Symbol paramSymbol)
/*      */   {
/*  709 */     return new Symbol.VarSymbol(paramLong | 0x1000, paramName, paramType, paramSymbol);
/*      */   }
/*      */ 
/*      */   private void setVarargsIfNeeded(JCTree paramJCTree, Type paramType)
/*      */   {
/*  717 */     if (paramType != null)
/*  718 */       switch (paramJCTree.getTag()) { case APPLY:
/*  719 */         ((JCTree.JCMethodInvocation)paramJCTree).varargsElement = paramType; break;
/*      */       case NEWCLASS:
/*  720 */         ((JCTree.JCNewClass)paramJCTree).varargsElement = paramType; break;
/*      */       default:
/*  721 */         throw new AssertionError();
/*      */       }
/*      */   }
/*      */ 
/*      */   private List<JCTree.JCExpression> convertArgs(Symbol paramSymbol, List<JCTree.JCExpression> paramList, Type paramType)
/*      */   {
/*  733 */     Assert.check(paramSymbol.kind == 16);
/*  734 */     List localList = this.types.erasure(paramSymbol.type).getParameterTypes();
/*  735 */     if (paramType != null) {
/*  736 */       Assert.check((paramSymbol.flags() & 0x0) != 0L);
/*      */     }
/*  738 */     return this.transTypes.translateArgs(paramList, localList, paramType, this.attrEnv);
/*      */   }
/*      */ 
/*      */   private Type.MethodType typeToMethodType(Type paramType)
/*      */   {
/*  942 */     Type localType = this.types.erasure(paramType);
/*      */ 
/*  945 */     return new Type.MethodType(localType.getParameterTypes(), localType
/*  944 */       .getReturnType(), localType
/*  945 */       .getThrownTypes(), this.syms.methodClass);
/*      */   }
/*      */ 
/*      */   private JCTree.JCExpression makeMetafactoryIndyCall(LambdaToMethod.LambdaAnalyzerPreprocessor.TranslationContext<?> paramTranslationContext, int paramInt, Symbol paramSymbol, List<JCTree.JCExpression> paramList)
/*      */   {
/*  954 */     JCTree.JCFunctionalExpression localJCFunctionalExpression = paramTranslationContext.tree;
/*      */ 
/*  956 */     Symbol.MethodSymbol localMethodSymbol = (Symbol.MethodSymbol)this.types.findDescriptorSymbol(localJCFunctionalExpression.type.tsym);
/*  957 */     List localList = List.of(
/*  958 */       typeToMethodType(localMethodSymbol.type), 
/*  958 */       new Pool.MethodHandle(paramInt, paramSymbol, this.types), 
/*  960 */       typeToMethodType(localJCFunctionalExpression
/*  960 */       .getDescriptorType(this.types)));
/*      */ 
/*  963 */     ListBuffer localListBuffer1 = new ListBuffer();
/*  964 */     for (Object localObject1 = paramList.iterator(); ((Iterator)localObject1).hasNext(); ) { localObject2 = (JCTree.JCExpression)((Iterator)localObject1).next();
/*  965 */       localListBuffer1.append(((JCTree.JCExpression)localObject2).type);
/*      */     }
/*      */ 
/*  971 */     localObject1 = new Type.MethodType(localListBuffer1.toList(), localJCFunctionalExpression.type, 
/*  971 */       List.nil(), this.syms.methodClass);
/*      */ 
/*  974 */     Object localObject2 = paramTranslationContext.needsAltMetafactory() ? this.names.altMetafactory : this.names.metafactory;
/*      */ 
/*  977 */     if (paramTranslationContext.needsAltMetafactory()) {
/*  978 */       ListBuffer localListBuffer2 = new ListBuffer();
/*  979 */       for (Type localType1 : localJCFunctionalExpression.targets.tail) {
/*  980 */         if (localType1.tsym != this.syms.serializableType.tsym) {
/*  981 */           localListBuffer2.append(localType1.tsym);
/*      */         }
/*      */       }
/*  984 */       int i = paramTranslationContext.isSerializable() ? 1 : 0;
/*  985 */       boolean bool1 = localListBuffer2.nonEmpty();
/*  986 */       boolean bool2 = paramTranslationContext.bridges.nonEmpty();
/*  987 */       if (bool1) {
/*  988 */         i |= 2;
/*      */       }
/*  990 */       if (bool2) {
/*  991 */         i |= 4;
/*      */       }
/*  993 */       localList = localList.append(Integer.valueOf(i));
/*  994 */       if (bool1) {
/*  995 */         localList = localList.append(Integer.valueOf(localListBuffer2.length()));
/*  996 */         localList = localList.appendList(localListBuffer2.toList());
/*      */       }
/*  998 */       if (bool2) {
/*  999 */         localList = localList.append(Integer.valueOf(paramTranslationContext.bridges.length() - 1));
/* 1000 */         for (Symbol localSymbol : paramTranslationContext.bridges) {
/* 1001 */           Type localType2 = localSymbol.erasure(this.types);
/* 1002 */           if (!this.types.isSameType(localType2, localMethodSymbol.erasure(this.types))) {
/* 1003 */             localList = localList.append(localSymbol.erasure(this.types));
/*      */           }
/*      */         }
/*      */       }
/* 1007 */       if (paramTranslationContext.isSerializable()) {
/* 1008 */         int j = this.make.pos;
/*      */         try {
/* 1010 */           this.make.at(this.kInfo.clazz);
/* 1011 */           addDeserializationCase(paramInt, paramSymbol, localJCFunctionalExpression.type, localMethodSymbol, localJCFunctionalExpression, localList, (Type.MethodType)localObject1);
/*      */         }
/*      */         finally {
/* 1014 */           this.make.at(j);
/*      */         }
/*      */       }
/*      */     }
/*      */ 
/* 1019 */     return makeIndyCall(localJCFunctionalExpression, this.syms.lambdaMetafactory, (Name)localObject2, localList, (Type.MethodType)localObject1, paramList, localMethodSymbol.name);
/*      */   }
/*      */ 
/*      */   private JCTree.JCExpression makeIndyCall(JCDiagnostic.DiagnosticPosition paramDiagnosticPosition, Type paramType, Name paramName1, List<Object> paramList, Type.MethodType paramMethodType, List<JCTree.JCExpression> paramList1, Name paramName2)
/*      */   {
/* 1029 */     int i = this.make.pos;
/*      */     try {
/* 1031 */       this.make.at(paramDiagnosticPosition);
/*      */ 
/* 1034 */       List localList = List.of(this.syms.methodHandleLookupType, this.syms.stringType, this.syms.methodTypeType)
/* 1034 */         .appendList(bsmStaticArgToTypes(paramList));
/*      */ 
/* 1036 */       Symbol.MethodSymbol localMethodSymbol = this.rs.resolveInternalMethod(paramDiagnosticPosition, this.attrEnv, paramType, paramName1, localList, 
/* 1037 */         List.nil());
/*      */ 
/* 1047 */       Symbol.DynamicMethodSymbol localDynamicMethodSymbol = new Symbol.DynamicMethodSymbol(paramName2, this.syms.noSymbol, localMethodSymbol
/* 1042 */         .isStatic() ? 6 : 5, (Symbol.MethodSymbol)localMethodSymbol, paramMethodType, paramList
/* 1047 */         .toArray());
/*      */ 
/* 1049 */       JCTree.JCFieldAccess localJCFieldAccess = this.make.Select(this.make.QualIdent(paramType.tsym), paramName1);
/* 1050 */       localJCFieldAccess.sym = localDynamicMethodSymbol;
/* 1051 */       localJCFieldAccess.type = paramMethodType.getReturnType();
/*      */ 
/* 1053 */       JCTree.JCMethodInvocation localJCMethodInvocation1 = this.make.Apply(List.nil(), localJCFieldAccess, paramList1);
/* 1054 */       localJCMethodInvocation1.type = paramMethodType.getReturnType();
/* 1055 */       return localJCMethodInvocation1;
/*      */     } finally {
/* 1057 */       this.make.at(i);
/*      */     }
/*      */   }
/*      */ 
/*      */   private List<Type> bsmStaticArgToTypes(List<Object> paramList) {
/* 1062 */     ListBuffer localListBuffer = new ListBuffer();
/* 1063 */     for (Iterator localIterator = paramList.iterator(); localIterator.hasNext(); ) { Object localObject = localIterator.next();
/* 1064 */       localListBuffer.append(bsmStaticArgToType(localObject));
/*      */     }
/* 1066 */     return localListBuffer.toList();
/*      */   }
/*      */ 
/*      */   private Type bsmStaticArgToType(Object paramObject) {
/* 1070 */     Assert.checkNonNull(paramObject);
/* 1071 */     if ((paramObject instanceof Symbol.ClassSymbol))
/* 1072 */       return this.syms.classType;
/* 1073 */     if ((paramObject instanceof Integer))
/* 1074 */       return this.syms.intType;
/* 1075 */     if ((paramObject instanceof Long))
/* 1076 */       return this.syms.longType;
/* 1077 */     if ((paramObject instanceof Float))
/* 1078 */       return this.syms.floatType;
/* 1079 */     if ((paramObject instanceof Double))
/* 1080 */       return this.syms.doubleType;
/* 1081 */     if ((paramObject instanceof String))
/* 1082 */       return this.syms.stringType;
/* 1083 */     if ((paramObject instanceof Pool.MethodHandle))
/* 1084 */       return this.syms.methodHandleType;
/* 1085 */     if ((paramObject instanceof Type.MethodType)) {
/* 1086 */       return this.syms.methodTypeType;
/*      */     }
/* 1088 */     Assert.error("bad static arg " + paramObject.getClass());
/* 1089 */     return null;
/*      */   }
/*      */ 
/*      */   private int referenceKind(Symbol paramSymbol)
/*      */   {
/* 1097 */     if (paramSymbol.isConstructor()) {
/* 1098 */       return 8;
/*      */     }
/* 1100 */     if (paramSymbol.isStatic())
/* 1101 */       return 6;
/* 1102 */     if ((paramSymbol.flags() & 0x2) != 0L)
/* 1103 */       return 7;
/* 1104 */     if (paramSymbol.enclClass().isInterface()) {
/* 1105 */       return 9;
/*      */     }
/* 1107 */     return 5;
/*      */   }
/*      */ 
/*      */   private String typeSig(Type paramType)
/*      */   {
/* 2153 */     L2MSignatureGenerator localL2MSignatureGenerator = new L2MSignatureGenerator();
/* 2154 */     localL2MSignatureGenerator.assembleSig(paramType);
/* 2155 */     return localL2MSignatureGenerator.toString();
/*      */   }
/*      */ 
/*      */   private String classSig(Type paramType) {
/* 2159 */     L2MSignatureGenerator localL2MSignatureGenerator = new L2MSignatureGenerator();
/* 2160 */     localL2MSignatureGenerator.assembleClassSig(paramType);
/* 2161 */     return localL2MSignatureGenerator.toString();
/*      */   }
/*      */ 
/*      */   private class KlassInfo
/*      */   {
/*      */     private ListBuffer<JCTree> appendedMethodList;
/*      */     private final Map<String, ListBuffer<JCTree.JCStatement>> deserializeCases;
/*      */     private final Symbol.MethodSymbol deserMethodSym;
/*      */     private final Symbol.VarSymbol deserParamSym;
/*      */     private final JCTree.JCClassDecl clazz;
/*      */ 
/*      */     private KlassInfo(JCTree.JCClassDecl arg2)
/*      */     {
/*      */       Object localObject;
/*  171 */       this.clazz = localObject;
/*  172 */       this.appendedMethodList = new ListBuffer();
/*  173 */       this.deserializeCases = new HashMap();
/*      */ 
/*  175 */       Type.MethodType localMethodType = new Type.MethodType(List.of(LambdaToMethod.this.syms.serializedLambdaType), LambdaToMethod.this.syms.objectType, 
/*  175 */         List.nil(), LambdaToMethod.this.syms.methodClass);
/*  176 */       this.deserMethodSym = LambdaToMethod.this.makePrivateSyntheticMethod(8L, LambdaToMethod.this.names.deserializeLambda, localMethodType, localObject.sym);
/*  177 */       this.deserParamSym = new Symbol.VarSymbol(16L, LambdaToMethod.this.names.fromString("lambda"), LambdaToMethod.this.syms.serializedLambdaType, 
/*  178 */         this.deserMethodSym);
/*      */     }
/*      */ 
/*      */     private void addMethod(JCTree paramJCTree) {
/*  182 */       this.appendedMethodList = this.appendedMethodList.prepend(paramJCTree);
/*      */     }
/*      */   }
/*      */ 
/*      */   private class L2MSignatureGenerator extends Types.SignatureGenerator
/*      */   {
/* 2172 */     StringBuilder sb = new StringBuilder();
/*      */ 
/*      */     L2MSignatureGenerator() {
/* 2175 */       super();
/*      */     }
/*      */ 
/*      */     protected void append(char paramChar)
/*      */     {
/* 2180 */       this.sb.append(paramChar);
/*      */     }
/*      */ 
/*      */     protected void append(byte[] paramArrayOfByte)
/*      */     {
/* 2185 */       this.sb.append(new String(paramArrayOfByte));
/*      */     }
/*      */ 
/*      */     protected void append(Name paramName)
/*      */     {
/* 2190 */       this.sb.append(paramName.toString());
/*      */     }
/*      */ 
/*      */     public String toString()
/*      */     {
/* 2195 */       return this.sb.toString();
/*      */     }
/*      */   }
/*      */ 
/*      */   class LambdaAnalyzerPreprocessor extends TreeTranslator
/*      */   {
/*      */     private List<Frame> frameStack;
/* 1128 */     private int lambdaCount = 0;
/*      */ 
/* 1147 */     private SyntheticMethodNameCounter syntheticMethodNameCounts = new SyntheticMethodNameCounter(null);
/*      */     private Map<Symbol, JCTree.JCClassDecl> localClassDefs;
/* 1156 */     private Map<Symbol.ClassSymbol, Symbol> clinits = new HashMap();
/*      */ 
/*      */     LambdaAnalyzerPreprocessor() {
/*      */     }
/* 1160 */     private JCTree.JCClassDecl analyzeAndPreprocessClass(JCTree.JCClassDecl paramJCClassDecl) { this.frameStack = List.nil();
/* 1161 */       this.localClassDefs = new HashMap();
/* 1162 */       return (JCTree.JCClassDecl)translate(paramJCClassDecl);
/*      */     }
/*      */ 
/*      */     public void visitBlock(JCTree.JCBlock paramJCBlock)
/*      */     {
/* 1167 */       List localList = this.frameStack;
/*      */       try {
/* 1169 */         if ((this.frameStack.nonEmpty()) && (((Frame)this.frameStack.head).tree.hasTag(JCTree.Tag.CLASSDEF))) {
/* 1170 */           this.frameStack = this.frameStack.prepend(new Frame(paramJCBlock));
/*      */         }
/* 1172 */         super.visitBlock(paramJCBlock);
/*      */ 
/* 1175 */         this.frameStack = localList; } finally { this.frameStack = localList; }
/*      */ 
/*      */     }
/*      */ 
/*      */     public void visitClassDef(JCTree.JCClassDecl paramJCClassDecl)
/*      */     {
/* 1181 */       List localList = this.frameStack;
/* 1182 */       int i = this.lambdaCount;
/* 1183 */       SyntheticMethodNameCounter localSyntheticMethodNameCounter = this.syntheticMethodNameCounts;
/*      */ 
/* 1185 */       Object localObject1 = this.clinits;
/* 1186 */       DiagnosticSource localDiagnosticSource = LambdaToMethod.this.log.currentSource();
/*      */       try {
/* 1188 */         LambdaToMethod.this.log.useSource(paramJCClassDecl.sym.sourcefile);
/* 1189 */         this.lambdaCount = 0;
/* 1190 */         this.syntheticMethodNameCounts = new SyntheticMethodNameCounter(null);
/* 1191 */         localObject1 = new HashMap();
/* 1192 */         if (paramJCClassDecl.sym.owner.kind == 16) {
/* 1193 */           this.localClassDefs.put(paramJCClassDecl.sym, paramJCClassDecl);
/*      */         }
/* 1195 */         if (directlyEnclosingLambda() != null) {
/* 1196 */           paramJCClassDecl.sym.owner = owner();
/* 1197 */           if (paramJCClassDecl.sym.hasOuterInstance())
/*      */           {
/* 1200 */             TranslationContext localTranslationContext = context();
/* 1201 */             while (localTranslationContext != null) {
/* 1202 */               if (localTranslationContext.tree.getTag() == JCTree.Tag.LAMBDA) {
/* 1203 */                 ((LambdaTranslationContext)localTranslationContext)
/* 1204 */                   .addSymbol(paramJCClassDecl.sym.type
/* 1204 */                   .getEnclosingType().tsym, LambdaToMethod.LambdaSymbolKind.CAPTURED_THIS);
/*      */               }
/* 1206 */               localTranslationContext = localTranslationContext.prev;
/*      */             }
/*      */           }
/*      */         }
/* 1210 */         this.frameStack = this.frameStack.prepend(new Frame(paramJCClassDecl));
/* 1211 */         super.visitClassDef(paramJCClassDecl);
/*      */       }
/*      */       finally {
/* 1214 */         LambdaToMethod.this.log.useSource(localDiagnosticSource.getFile());
/* 1215 */         this.frameStack = localList;
/* 1216 */         this.lambdaCount = i;
/* 1217 */         this.syntheticMethodNameCounts = localSyntheticMethodNameCounter;
/* 1218 */         this.clinits = ((Map)localObject1);
/*      */       }
/*      */     }
/*      */ 
/*      */     public void visitIdent(JCTree.JCIdent paramJCIdent)
/*      */     {
/* 1224 */       if ((context() != null) && (lambdaIdentSymbolFilter(paramJCIdent.sym)))
/*      */       {
/*      */         TranslationContext localTranslationContext;
/*      */         JCTree localJCTree;
/* 1225 */         if ((paramJCIdent.sym.kind == 4) && (paramJCIdent.sym.owner.kind == 16))
/*      */         {
/* 1227 */           if (paramJCIdent.type
/* 1227 */             .constValue() == null) {
/* 1228 */             localTranslationContext = context();
/* 1229 */             while (localTranslationContext != null) {
/* 1230 */               if (localTranslationContext.tree.getTag() == JCTree.Tag.LAMBDA) {
/* 1231 */                 localJCTree = capturedDecl(localTranslationContext.depth, paramJCIdent.sym);
/* 1232 */                 if (localJCTree == null) break;
/* 1233 */                 ((LambdaTranslationContext)localTranslationContext)
/* 1234 */                   .addSymbol(paramJCIdent.sym, LambdaToMethod.LambdaSymbolKind.CAPTURED_VAR);
/*      */               }
/*      */ 
/* 1236 */               localTranslationContext = localTranslationContext.prev;
/*      */             }
/* 1238 */             break label245; }  } if (paramJCIdent.sym.owner.kind == 2) {
/* 1239 */           localTranslationContext = context();
/* 1240 */           while (localTranslationContext != null) {
/* 1241 */             if (localTranslationContext.tree.hasTag(JCTree.Tag.LAMBDA)) {
/* 1242 */               localJCTree = capturedDecl(localTranslationContext.depth, paramJCIdent.sym);
/* 1243 */               if (localJCTree == null) break;
/* 1244 */               switch (LambdaToMethod.1.$SwitchMap$com$sun$tools$javac$tree$JCTree$Tag[localJCTree.getTag().ordinal()]) {
/*      */               case 3:
/* 1246 */                 JCTree.JCClassDecl localJCClassDecl = (JCTree.JCClassDecl)localJCTree;
/* 1247 */                 ((LambdaTranslationContext)localTranslationContext)
/* 1248 */                   .addSymbol(localJCClassDecl.sym, LambdaToMethod.LambdaSymbolKind.CAPTURED_THIS);
/*      */ 
/* 1249 */                 break;
/*      */               default:
/* 1251 */                 Assert.error("bad block kind");
/*      */               }
/*      */             }
/* 1254 */             localTranslationContext = localTranslationContext.prev;
/*      */           }
/*      */         }
/*      */       }
/* 1258 */       label245: super.visitIdent(paramJCIdent);
/*      */     }
/*      */ 
/*      */     public void visitLambda(JCTree.JCLambda paramJCLambda)
/*      */     {
/* 1263 */       analyzeLambda(paramJCLambda, "lambda.stat");
/*      */     }
/*      */ 
/*      */     private void analyzeLambda(JCTree.JCLambda paramJCLambda, JCTree.JCExpression paramJCExpression)
/*      */     {
/* 1268 */       JCTree.JCExpression localJCExpression = (JCTree.JCExpression)translate(paramJCExpression);
/* 1269 */       LambdaTranslationContext localLambdaTranslationContext = analyzeLambda(paramJCLambda, "mref.stat.1");
/* 1270 */       if (localJCExpression != null)
/* 1271 */         localLambdaTranslationContext.methodReferenceReceiver = localJCExpression;
/*      */     }
/*      */ 
/*      */     private LambdaTranslationContext analyzeLambda(JCTree.JCLambda paramJCLambda, String paramString)
/*      */     {
/* 1276 */       List localList = this.frameStack;
/*      */       try {
/* 1278 */         LambdaTranslationContext localLambdaTranslationContext = new LambdaTranslationContext(paramJCLambda);
/* 1279 */         if (LambdaToMethod.this.dumpLambdaToMethodStats) {
/* 1280 */           LambdaToMethod.this.log.note(paramJCLambda, paramString, new Object[] { Boolean.valueOf(localLambdaTranslationContext.needsAltMetafactory()), localLambdaTranslationContext.translatedSym });
/*      */         }
/* 1282 */         this.frameStack = this.frameStack.prepend(new Frame(paramJCLambda));
/* 1283 */         for (Object localObject1 = paramJCLambda.params.iterator(); ((Iterator)localObject1).hasNext(); ) { JCTree.JCVariableDecl localJCVariableDecl = (JCTree.JCVariableDecl)((Iterator)localObject1).next();
/* 1284 */           localLambdaTranslationContext.addSymbol(localJCVariableDecl.sym, LambdaToMethod.LambdaSymbolKind.PARAM);
/* 1285 */           ((Frame)this.frameStack.head).addLocal(localJCVariableDecl.sym);
/*      */         }
/* 1287 */         LambdaToMethod.this.contextMap.put(paramJCLambda, localLambdaTranslationContext);
/* 1288 */         super.visitLambda(paramJCLambda);
/* 1289 */         localLambdaTranslationContext.complete();
/* 1290 */         return localLambdaTranslationContext;
/*      */       }
/*      */       finally {
/* 1293 */         this.frameStack = localList;
/*      */       }
/*      */     }
/*      */ 
/*      */     public void visitMethodDef(JCTree.JCMethodDecl paramJCMethodDecl)
/*      */     {
/* 1299 */       List localList = this.frameStack;
/*      */       try {
/* 1301 */         this.frameStack = this.frameStack.prepend(new Frame(paramJCMethodDecl));
/* 1302 */         super.visitMethodDef(paramJCMethodDecl);
/*      */ 
/* 1305 */         this.frameStack = localList; } finally { this.frameStack = localList; }
/*      */ 
/*      */     }
/*      */ 
/*      */     public void visitNewClass(JCTree.JCNewClass paramJCNewClass)
/*      */     {
/* 1311 */       Symbol.TypeSymbol localTypeSymbol = paramJCNewClass.type.tsym;
/* 1312 */       boolean bool1 = currentlyInClass(localTypeSymbol);
/* 1313 */       boolean bool2 = localTypeSymbol.isLocal();
/*      */       Object localObject;
/* 1314 */       if (((bool1) && (bool2)) || (lambdaNewClassFilter(context(), paramJCNewClass))) {
/* 1315 */         localObject = context();
/* 1316 */         while (localObject != null) {
/* 1317 */           if (((TranslationContext)localObject).tree.getTag() == JCTree.Tag.LAMBDA) {
/* 1318 */             ((LambdaTranslationContext)localObject)
/* 1319 */               .addSymbol(paramJCNewClass.type
/* 1319 */               .getEnclosingType().tsym, LambdaToMethod.LambdaSymbolKind.CAPTURED_THIS);
/*      */           }
/* 1321 */           localObject = ((TranslationContext)localObject).prev;
/*      */         }
/*      */       }
/* 1324 */       if ((context() != null) && (!bool1) && (bool2)) {
/* 1325 */         localObject = (LambdaTranslationContext)context();
/* 1326 */         captureLocalClassDefs(localTypeSymbol, (LambdaTranslationContext)localObject);
/*      */       }
/* 1328 */       super.visitNewClass(paramJCNewClass);
/*      */     }
/*      */ 
/*      */     void captureLocalClassDefs(Symbol paramSymbol, final LambdaTranslationContext paramLambdaTranslationContext) {
/* 1332 */       JCTree.JCClassDecl localJCClassDecl = (JCTree.JCClassDecl)this.localClassDefs.get(paramSymbol);
/* 1333 */       if ((localJCClassDecl != null) && (paramLambdaTranslationContext.freeVarProcessedLocalClasses.add(paramSymbol)))
/*      */       {
/*      */         Lower tmp43_40 = LambdaToMethod.this.lower; tmp43_40.getClass(); Lower.BasicFreeVarCollector local1 = new Lower.BasicFreeVarCollector(tmp43_40)
/*      */         {
/*      */           void addFreeVars(Symbol.ClassSymbol paramAnonymousClassSymbol) {
/* 1337 */             LambdaToMethod.LambdaAnalyzerPreprocessor.this.captureLocalClassDefs(paramAnonymousClassSymbol, paramLambdaTranslationContext);
/*      */           }
/*      */ 
/*      */           void visitSymbol(Symbol paramAnonymousSymbol) {
/* 1341 */             if ((paramAnonymousSymbol.kind == 4) && (paramAnonymousSymbol.owner.kind == 16))
/*      */             {
/* 1343 */               if (((Symbol.VarSymbol)paramAnonymousSymbol)
/* 1343 */                 .getConstValue() == null) {
/* 1344 */                 LambdaToMethod.LambdaAnalyzerPreprocessor.TranslationContext localTranslationContext = LambdaToMethod.LambdaAnalyzerPreprocessor.this.context();
/* 1345 */                 while (localTranslationContext != null) {
/* 1346 */                   if (localTranslationContext.tree.getTag() == JCTree.Tag.LAMBDA) {
/* 1347 */                     JCTree localJCTree = LambdaToMethod.LambdaAnalyzerPreprocessor.this.capturedDecl(localTranslationContext.depth, paramAnonymousSymbol);
/* 1348 */                     if (localJCTree == null) break;
/* 1349 */                     ((LambdaToMethod.LambdaAnalyzerPreprocessor.LambdaTranslationContext)localTranslationContext).addSymbol(paramAnonymousSymbol, LambdaToMethod.LambdaSymbolKind.CAPTURED_VAR);
/*      */                   }
/* 1351 */                   localTranslationContext = localTranslationContext.prev;
/*      */                 }
/*      */               }
/*      */             }
/*      */           }
/*      */         };
/* 1356 */         local1.scan(localJCClassDecl);
/*      */       }
/*      */     }
/*      */ 
/*      */     boolean currentlyInClass(Symbol paramSymbol) {
/* 1361 */       for (Frame localFrame : this.frameStack) {
/* 1362 */         if (localFrame.tree.hasTag(JCTree.Tag.CLASSDEF)) {
/* 1363 */           JCTree.JCClassDecl localJCClassDecl = (JCTree.JCClassDecl)localFrame.tree;
/* 1364 */           if (localJCClassDecl.sym == paramSymbol) {
/* 1365 */             return true;
/*      */           }
/*      */         }
/*      */       }
/* 1369 */       return false;
/*      */     }
/*      */ 
/*      */     public void visitReference(JCTree.JCMemberReference paramJCMemberReference)
/*      */     {
/* 1386 */       ReferenceTranslationContext localReferenceTranslationContext = new ReferenceTranslationContext(paramJCMemberReference);
/* 1387 */       LambdaToMethod.this.contextMap.put(paramJCMemberReference, localReferenceTranslationContext);
/* 1388 */       if (localReferenceTranslationContext.needsConversionToLambda())
/*      */       {
/* 1390 */         LambdaToMethod.MemberReferenceToLambda localMemberReferenceToLambda = new LambdaToMethod.MemberReferenceToLambda(LambdaToMethod.this, paramJCMemberReference, localReferenceTranslationContext, owner());
/* 1391 */         analyzeLambda(localMemberReferenceToLambda.lambda(), localMemberReferenceToLambda.getReceiverExpression());
/*      */       } else {
/* 1393 */         super.visitReference(paramJCMemberReference);
/* 1394 */         if (LambdaToMethod.this.dumpLambdaToMethodStats)
/* 1395 */           LambdaToMethod.this.log.note(paramJCMemberReference, "mref.stat", new Object[] { Boolean.valueOf(localReferenceTranslationContext.needsAltMetafactory()), null });
/*      */       }
/*      */     }
/*      */ 
/*      */     public void visitSelect(JCTree.JCFieldAccess paramJCFieldAccess)
/*      */     {
/* 1402 */       if ((context() != null) && (paramJCFieldAccess.sym.kind == 4) && (
/* 1403 */         (paramJCFieldAccess.sym.name == LambdaToMethod.this.names._this) || 
/* 1404 */         (paramJCFieldAccess.sym.name == LambdaToMethod.this.names._super)))
/*      */       {
/* 1407 */         TranslationContext localTranslationContext = context();
/* 1408 */         while (localTranslationContext != null) {
/* 1409 */           if (localTranslationContext.tree.hasTag(JCTree.Tag.LAMBDA)) {
/* 1410 */             JCTree.JCClassDecl localJCClassDecl = (JCTree.JCClassDecl)capturedDecl(localTranslationContext.depth, paramJCFieldAccess.sym);
/* 1411 */             if (localJCClassDecl == null) break;
/* 1412 */             ((LambdaTranslationContext)localTranslationContext).addSymbol(localJCClassDecl.sym, LambdaToMethod.LambdaSymbolKind.CAPTURED_THIS);
/*      */           }
/* 1414 */           localTranslationContext = localTranslationContext.prev;
/*      */         }
/*      */       }
/* 1417 */       super.visitSelect(paramJCFieldAccess);
/*      */     }
/*      */ 
/*      */     public void visitVarDef(JCTree.JCVariableDecl paramJCVariableDecl)
/*      */     {
/* 1422 */       TranslationContext localTranslationContext = context();
/* 1423 */       Object localObject1 = (localTranslationContext != null) && ((localTranslationContext instanceof LambdaTranslationContext)) ? (LambdaTranslationContext)localTranslationContext : null;
/*      */ 
/* 1426 */       if (localObject1 != null) {
/* 1427 */         if (((Frame)this.frameStack.head).tree.hasTag(JCTree.Tag.LAMBDA)) {
/* 1428 */           localObject1.addSymbol(paramJCVariableDecl.sym, LambdaToMethod.LambdaSymbolKind.LOCAL_VAR);
/*      */         }
/*      */ 
/* 1432 */         localObject2 = paramJCVariableDecl.sym.asType();
/* 1433 */         if ((inClassWithinLambda()) && (!LambdaToMethod.this.types.isSameType(LambdaToMethod.this.types.erasure((Type)localObject2), (Type)localObject2))) {
/* 1434 */           localObject1.addSymbol(paramJCVariableDecl.sym, LambdaToMethod.LambdaSymbolKind.TYPE_VAR);
/*      */         }
/*      */       }
/*      */ 
/* 1438 */       Object localObject2 = this.frameStack;
/*      */       try {
/* 1440 */         if (paramJCVariableDecl.sym.owner.kind == 16) {
/* 1441 */           ((Frame)this.frameStack.head).addLocal(paramJCVariableDecl.sym);
/*      */         }
/* 1443 */         this.frameStack = this.frameStack.prepend(new Frame(paramJCVariableDecl));
/* 1444 */         super.visitVarDef(paramJCVariableDecl);
/*      */       }
/*      */       finally {
/* 1447 */         this.frameStack = ((List)localObject2);
/*      */       }
/*      */     }
/*      */ 
/*      */     private Symbol owner()
/*      */     {
/* 1456 */       return owner(false);
/*      */     }
/*      */ 
/*      */     private Symbol owner(boolean paramBoolean)
/*      */     {
/* 1461 */       List localList = this.frameStack;
/* 1462 */       while (localList.nonEmpty()) {
/* 1463 */         switch (LambdaToMethod.1.$SwitchMap$com$sun$tools$javac$tree$JCTree$Tag[((Frame)localList.head).tree.getTag().ordinal()]) {
/*      */         case 4:
/* 1465 */           if (((JCTree.JCVariableDecl)((Frame)localList.head).tree).sym.isLocal()) {
/* 1466 */             localList = localList.tail;
/*      */           }
/*      */           else {
/* 1469 */             JCTree.JCClassDecl localJCClassDecl1 = (JCTree.JCClassDecl)((Frame)localList.tail.head).tree;
/* 1470 */             return initSym(localJCClassDecl1.sym, ((JCTree.JCVariableDecl)((Frame)localList.head).tree).sym
/* 1471 */               .flags() & 0x8); } break;
/*      */         case 5:
/* 1473 */           JCTree.JCClassDecl localJCClassDecl2 = (JCTree.JCClassDecl)((Frame)localList.tail.head).tree;
/* 1474 */           return initSym(localJCClassDecl2.sym, ((JCTree.JCBlock)((Frame)localList.head).tree).flags & 0x8);
/*      */         case 3:
/* 1477 */           return ((JCTree.JCClassDecl)((Frame)localList.head).tree).sym;
/*      */         case 6:
/* 1479 */           return ((JCTree.JCMethodDecl)((Frame)localList.head).tree).sym;
/*      */         case 7:
/* 1481 */           if (!paramBoolean)
/*      */           {
/* 1483 */             return ((LambdaTranslationContext)LambdaToMethod.this.contextMap
/* 1483 */               .get(((Frame)localList.head).tree)).translatedSym;
/*      */           }
/*      */         default:
/* 1485 */           localList = localList.tail;
/*      */         }
/*      */       }
/* 1488 */       Assert.error();
/* 1489 */       return null;
/*      */     }
/*      */ 
/*      */     private Symbol initSym(Symbol.ClassSymbol paramClassSymbol, long paramLong) {
/* 1493 */       int i = (paramLong & 0x8) != 0L ? 1 : 0;
/* 1494 */       if (i != 0)
/*      */       {
/* 1500 */         localObject = LambdaToMethod.this.attr.removeClinit(paramClassSymbol);
/* 1501 */         if (localObject != null) {
/* 1502 */           this.clinits.put(paramClassSymbol, localObject);
/* 1503 */           return localObject;
/*      */         }
/*      */ 
/* 1508 */         localObject = (Symbol.MethodSymbol)this.clinits.get(paramClassSymbol);
/* 1509 */         if (localObject == null)
/*      */         {
/* 1512 */           localObject = LambdaToMethod.this.makePrivateSyntheticMethod(8L, LambdaToMethod.this.names.clinit, 
/* 1513 */             new Type.MethodType(
/* 1514 */             List.nil(), LambdaToMethod.this.syms.voidType, 
/* 1515 */             List.nil(), LambdaToMethod.this.syms.methodClass), paramClassSymbol);
/*      */ 
/* 1517 */           this.clinits.put(paramClassSymbol, localObject);
/*      */         }
/* 1519 */         return localObject;
/*      */       }
/*      */ 
/* 1522 */       Object localObject = paramClassSymbol.members_field.getElementsByName(LambdaToMethod.this.names.init).iterator(); if (((Iterator)localObject).hasNext()) { Symbol localSymbol = (Symbol)((Iterator)localObject).next();
/* 1523 */         return localSymbol;
/*      */       }
/*      */ 
/* 1526 */       Assert.error("init not found");
/* 1527 */       return null;
/*      */     }
/*      */ 
/*      */     private JCTree directlyEnclosingLambda() {
/* 1531 */       if (this.frameStack.isEmpty()) {
/* 1532 */         return null;
/*      */       }
/* 1534 */       List localList = this.frameStack;
/* 1535 */       while (localList.nonEmpty()) {
/* 1536 */         switch (LambdaToMethod.1.$SwitchMap$com$sun$tools$javac$tree$JCTree$Tag[((Frame)localList.head).tree.getTag().ordinal()]) {
/*      */         case 3:
/*      */         case 6:
/* 1539 */           return null;
/*      */         case 7:
/* 1541 */           return ((Frame)localList.head).tree;
/*      */         case 4:
/* 1543 */         case 5: } localList = localList.tail;
/*      */       }
/*      */ 
/* 1546 */       Assert.error();
/* 1547 */       return null;
/*      */     }
/*      */ 
/*      */     private boolean inClassWithinLambda() {
/* 1551 */       if (this.frameStack.isEmpty()) {
/* 1552 */         return false;
/*      */       }
/* 1554 */       List localList = this.frameStack;
/* 1555 */       boolean bool = false;
/* 1556 */       while (localList.nonEmpty()) {
/* 1557 */         switch (LambdaToMethod.1.$SwitchMap$com$sun$tools$javac$tree$JCTree$Tag[((Frame)localList.head).tree.getTag().ordinal()]) {
/*      */         case 7:
/* 1559 */           return bool;
/*      */         case 3:
/* 1561 */           bool = true;
/* 1562 */           localList = localList.tail;
/* 1563 */           break;
/*      */         default:
/* 1565 */           localList = localList.tail;
/*      */         }
/*      */       }
/*      */ 
/* 1569 */       return false;
/*      */     }
/*      */ 
/*      */     private JCTree capturedDecl(int paramInt, Symbol paramSymbol)
/*      */     {
/* 1578 */       int i = this.frameStack.size() - 1;
/* 1579 */       for (Frame localFrame : this.frameStack) {
/* 1580 */         switch (LambdaToMethod.1.$SwitchMap$com$sun$tools$javac$tree$JCTree$Tag[localFrame.tree.getTag().ordinal()]) {
/*      */         case 3:
/* 1582 */           Symbol.ClassSymbol localClassSymbol = ((JCTree.JCClassDecl)localFrame.tree).sym;
/* 1583 */           if (paramSymbol.isMemberOf(localClassSymbol, LambdaToMethod.this.types)) {
/* 1584 */             return i > paramInt ? null : localFrame.tree;
/*      */           }
/*      */           break;
/*      */         case 4:
/* 1588 */           if ((((JCTree.JCVariableDecl)localFrame.tree).sym == paramSymbol) && (paramSymbol.owner.kind == 16))
/*      */           {
/* 1590 */             return i > paramInt ? null : localFrame.tree;
/*      */           }
/*      */           break;
/*      */         case 5:
/*      */         case 6:
/*      */         case 7:
/* 1596 */           if ((localFrame.locals != null) && (localFrame.locals.contains(paramSymbol))) {
/* 1597 */             return i > paramInt ? null : localFrame.tree;
/*      */           }
/*      */           break;
/*      */         default:
/* 1601 */           Assert.error("bad decl kind " + localFrame.tree.getTag());
/*      */         }
/* 1603 */         i--;
/*      */       }
/* 1605 */       return null;
/*      */     }
/*      */ 
/*      */     private TranslationContext<?> context() {
/* 1609 */       for (Frame localFrame : this.frameStack) {
/* 1610 */         TranslationContext localTranslationContext = (TranslationContext)LambdaToMethod.this.contextMap.get(localFrame.tree);
/* 1611 */         if (localTranslationContext != null) {
/* 1612 */           return localTranslationContext;
/*      */         }
/*      */       }
/* 1615 */       return null;
/*      */     }
/*      */ 
/*      */     private boolean lambdaIdentSymbolFilter(Symbol paramSymbol)
/*      */     {
/* 1625 */       return ((paramSymbol.kind == 4) || (paramSymbol.kind == 16)) && 
/* 1624 */         (!paramSymbol
/* 1624 */         .isStatic()) && 
/* 1625 */         (paramSymbol.name != LambdaToMethod.this.names.init);
/*      */     }
/*      */ 
/*      */     private boolean lambdaNewClassFilter(TranslationContext<?> paramTranslationContext, JCTree.JCNewClass paramJCNewClass)
/*      */     {
/* 1633 */       if ((paramTranslationContext != null) && (paramJCNewClass.encl == null) && (paramJCNewClass.def == null))
/*      */       {
/* 1636 */         if (!paramJCNewClass.type
/* 1636 */           .getEnclosingType().hasTag(TypeTag.NONE)) {
/* 1637 */           Type localType1 = paramJCNewClass.type.getEnclosingType();
/* 1638 */           Type localType2 = paramTranslationContext.owner.enclClass().type;
/* 1639 */           while (!localType2.hasTag(TypeTag.NONE)) {
/* 1640 */             if (localType2.tsym.isSubClass(localType1.tsym, LambdaToMethod.this.types)) {
/* 1641 */               return true;
/*      */             }
/* 1643 */             localType2 = localType2.getEnclosingType();
/*      */           }
/* 1645 */           return false;
/*      */         }
/*      */       }
/* 1647 */       return false;
/*      */     }
/*      */ 
/*      */     private class Frame
/*      */     {
/*      */       final JCTree tree;
/*      */       List<Symbol> locals;
/*      */ 
/*      */       public Frame(JCTree arg2)
/*      */       {
/*      */         Object localObject;
/* 1656 */         this.tree = localObject;
/*      */       }
/*      */ 
/*      */       void addLocal(Symbol paramSymbol) {
/* 1660 */         if (this.locals == null) {
/* 1661 */           this.locals = List.nil();
/*      */         }
/* 1663 */         this.locals = this.locals.prepend(paramSymbol);
/*      */       }
/*      */     }
/*      */ 
/*      */     private class LambdaTranslationContext extends LambdaToMethod.LambdaAnalyzerPreprocessor.TranslationContext<JCTree.JCLambda>
/*      */     {
/*      */       final Symbol self;
/*      */       final Symbol assignedTo;
/*      */       Map<LambdaToMethod.LambdaSymbolKind, Map<Symbol, Symbol>> translatedSymbols;
/*      */       Symbol.MethodSymbol translatedSym;
/*      */       List<JCTree.JCVariableDecl> syntheticParams;
/*      */       final Set<Symbol> freeVarProcessedLocalClasses;
/*      */       JCTree.JCExpression methodReferenceReceiver;
/*      */ 
/*      */       LambdaTranslationContext(JCTree.JCLambda arg2)
/*      */       {
/* 1778 */         super(localJCFunctionalExpression);
/* 1779 */         LambdaToMethod.LambdaAnalyzerPreprocessor.Frame localFrame = (LambdaToMethod.LambdaAnalyzerPreprocessor.Frame)LambdaToMethod.LambdaAnalyzerPreprocessor.this.frameStack.head;
/* 1780 */         switch (LambdaToMethod.1.$SwitchMap$com$sun$tools$javac$tree$JCTree$Tag[localFrame.tree.getTag().ordinal()]) {
/*      */         case 4:
/* 1782 */           this.assignedTo = (this.self = ((JCTree.JCVariableDecl)localFrame.tree).sym);
/* 1783 */           break;
/*      */         case 8:
/* 1785 */           this.self = null;
/* 1786 */           this.assignedTo = TreeInfo.symbol(((JCTree.JCAssign)localFrame.tree).getVariable());
/* 1787 */           break;
/*      */         default:
/* 1789 */           this.assignedTo = (this.self = null);
/*      */         }
/*      */ 
/* 1794 */         this.translatedSym = LambdaToMethod.this.makePrivateSyntheticMethod(0L, null, null, this.owner.enclClass());
/*      */ 
/* 1796 */         this.translatedSymbols = new EnumMap(LambdaToMethod.LambdaSymbolKind.class);
/*      */ 
/* 1798 */         this.translatedSymbols.put(LambdaToMethod.LambdaSymbolKind.PARAM, new LinkedHashMap());
/* 1799 */         this.translatedSymbols.put(LambdaToMethod.LambdaSymbolKind.LOCAL_VAR, new LinkedHashMap());
/* 1800 */         this.translatedSymbols.put(LambdaToMethod.LambdaSymbolKind.CAPTURED_VAR, new LinkedHashMap());
/* 1801 */         this.translatedSymbols.put(LambdaToMethod.LambdaSymbolKind.CAPTURED_THIS, new LinkedHashMap());
/* 1802 */         this.translatedSymbols.put(LambdaToMethod.LambdaSymbolKind.TYPE_VAR, new LinkedHashMap());
/*      */ 
/* 1804 */         this.freeVarProcessedLocalClasses = new HashSet();
/*      */       }
/*      */ 
/*      */       private String serializedLambdaDisambiguation()
/*      */       {
/* 1814 */         StringBuilder localStringBuilder = new StringBuilder();
/*      */ 
/* 1821 */         Assert.check((this.owner.type != null) || 
/* 1823 */           (LambdaToMethod.LambdaAnalyzerPreprocessor.this
/* 1823 */           .directlyEnclosingLambda() != null));
/* 1824 */         if (this.owner.type != null) {
/* 1825 */           localStringBuilder.append(LambdaToMethod.this.typeSig(this.owner.type));
/* 1826 */           localStringBuilder.append(":");
/*      */         }
/*      */ 
/* 1830 */         localStringBuilder.append(LambdaToMethod.this.types.findDescriptorSymbol(((JCTree.JCLambda)this.tree).type.tsym).owner.flatName());
/* 1831 */         localStringBuilder.append(" ");
/*      */ 
/* 1834 */         if (this.assignedTo != null) {
/* 1835 */           localStringBuilder.append(this.assignedTo.flatName());
/* 1836 */           localStringBuilder.append("=");
/*      */         }
/*      */ 
/* 1839 */         for (Symbol localSymbol : getSymbolMap(LambdaToMethod.LambdaSymbolKind.CAPTURED_VAR).keySet()) {
/* 1840 */           if (localSymbol != this.self) {
/* 1841 */             localStringBuilder.append(LambdaToMethod.this.typeSig(localSymbol.type));
/* 1842 */             localStringBuilder.append(" ");
/* 1843 */             localStringBuilder.append(localSymbol.flatName());
/* 1844 */             localStringBuilder.append(",");
/*      */           }
/*      */         }
/*      */ 
/* 1848 */         return localStringBuilder.toString();
/*      */       }
/*      */ 
/*      */       private Name lambdaName()
/*      */       {
/* 1857 */         return LambdaToMethod.this.names.lambda.append(LambdaToMethod.this.names.fromString(enclosingMethodName() + "$" + LambdaToMethod.LambdaAnalyzerPreprocessor.access$3208(LambdaToMethod.LambdaAnalyzerPreprocessor.this)));
/*      */       }
/*      */ 
/*      */       private Name serializedLambdaName()
/*      */       {
/* 1867 */         StringBuilder localStringBuilder = new StringBuilder();
/* 1868 */         localStringBuilder.append(LambdaToMethod.this.names.lambda);
/*      */ 
/* 1870 */         localStringBuilder.append(enclosingMethodName());
/* 1871 */         localStringBuilder.append('$');
/*      */ 
/* 1874 */         String str1 = serializedLambdaDisambiguation();
/* 1875 */         localStringBuilder.append(Integer.toHexString(str1.hashCode()));
/* 1876 */         localStringBuilder.append('$');
/*      */ 
/* 1879 */         localStringBuilder.append(LambdaToMethod.LambdaAnalyzerPreprocessor.this.syntheticMethodNameCounts.getIndex(localStringBuilder));
/* 1880 */         String str2 = localStringBuilder.toString();
/*      */ 
/* 1882 */         return LambdaToMethod.this.names.fromString(str2);
/*      */       }
/*      */ 
/*      */       Symbol translate(Symbol paramSymbol, LambdaToMethod.LambdaSymbolKind paramLambdaSymbolKind)
/*      */       {
/*      */         Object localObject;
/* 1891 */         switch (LambdaToMethod.1.$SwitchMap$com$sun$tools$javac$comp$LambdaToMethod$LambdaSymbolKind[paramLambdaSymbolKind.ordinal()]) {
/*      */         case 1:
/* 1893 */           localObject = paramSymbol;
/* 1894 */           break;
/*      */         case 2:
/* 1898 */           localObject = new Symbol.VarSymbol(paramSymbol.flags(), paramSymbol.name, LambdaToMethod.this.types
/* 1898 */             .erasure(paramSymbol.type), paramSymbol.owner);
/*      */ 
/* 1903 */           ((Symbol.VarSymbol)localObject).pos = ((Symbol.VarSymbol)paramSymbol).pos;
/* 1904 */           break;
/*      */         case 3:
/* 1906 */           localObject = new Symbol.VarSymbol(8589938704L, paramSymbol.name, LambdaToMethod.this.types.erasure(paramSymbol.type), this.translatedSym)
/*      */           {
/*      */             public Symbol baseSymbol()
/*      */             {
/* 1910 */               return this.val$sym;
/*      */             }
/*      */           };
/* 1913 */           break;
/*      */         case 4:
/* 1915 */           localObject = new Symbol.VarSymbol(paramSymbol.flags() & 0x10, paramSymbol.name, paramSymbol.type, this.translatedSym);
/* 1916 */           ((Symbol.VarSymbol)localObject).pos = ((Symbol.VarSymbol)paramSymbol).pos;
/* 1917 */           break;
/*      */         case 5:
/* 1919 */           localObject = new Symbol.VarSymbol(paramSymbol.flags() & 0x10 | 0x0, paramSymbol.name, LambdaToMethod.this.types.erasure(paramSymbol.type), this.translatedSym);
/* 1920 */           ((Symbol.VarSymbol)localObject).pos = ((Symbol.VarSymbol)paramSymbol).pos;
/* 1921 */           break;
/*      */         default:
/* 1923 */           Assert.error(paramLambdaSymbolKind.name());
/* 1924 */           throw new AssertionError();
/*      */         }
/* 1926 */         if (localObject != paramSymbol) {
/* 1927 */           ((Symbol)localObject).setDeclarationAttributes(paramSymbol.getRawAttributes());
/* 1928 */           ((Symbol)localObject).setTypeAttributes(paramSymbol.getRawTypeAttributes());
/*      */         }
/* 1930 */         return localObject;
/*      */       }
/*      */ 
/*      */       void addSymbol(Symbol paramSymbol, LambdaToMethod.LambdaSymbolKind paramLambdaSymbolKind) {
/* 1934 */         Map localMap = getSymbolMap(paramLambdaSymbolKind);
/* 1935 */         if (!localMap.containsKey(paramSymbol))
/* 1936 */           localMap.put(paramSymbol, translate(paramSymbol, paramLambdaSymbolKind));
/*      */       }
/*      */ 
/*      */       Map<Symbol, Symbol> getSymbolMap(LambdaToMethod.LambdaSymbolKind paramLambdaSymbolKind)
/*      */       {
/* 1941 */         Map localMap = (Map)this.translatedSymbols.get(paramLambdaSymbolKind);
/* 1942 */         Assert.checkNonNull(localMap);
/* 1943 */         return localMap;
/*      */       }
/*      */ 
/*      */       JCTree translate(JCTree.JCIdent paramJCIdent) {
/* 1947 */         for (Map localMap : this.translatedSymbols.values()) {
/* 1948 */           if (localMap.containsKey(paramJCIdent.sym)) {
/* 1949 */             Symbol localSymbol = (Symbol)localMap.get(paramJCIdent.sym);
/* 1950 */             JCTree.JCExpression localJCExpression = LambdaToMethod.this.make.Ident(localSymbol).setType(paramJCIdent.type);
/* 1951 */             localSymbol.setTypeAttributes(paramJCIdent.sym.getRawTypeAttributes());
/* 1952 */             return localJCExpression;
/*      */           }
/*      */         }
/* 1955 */         return null;
/*      */       }
/*      */ 
/*      */       void complete()
/*      */       {
/* 1965 */         if (this.syntheticParams != null) {
/* 1966 */           return;
/*      */         }
/* 1968 */         boolean bool = this.translatedSym.owner.isInterface();
/* 1969 */         int i = !getSymbolMap(LambdaToMethod.LambdaSymbolKind.CAPTURED_THIS).isEmpty() ? 1 : 0;
/*      */ 
/* 1976 */         this.translatedSym.flags_field = (0x1000 | this.owner.flags_field & 0x800 | this.owner.owner.flags_field & 0x800 | 0x2 | (i != 0 ? 0L : bool ? 8796093022208L : 8L));
/*      */ 
/* 1983 */         ListBuffer localListBuffer1 = new ListBuffer();
/* 1984 */         ListBuffer localListBuffer2 = new ListBuffer();
/*      */ 
/* 1991 */         for (Iterator localIterator = getSymbolMap(LambdaToMethod.LambdaSymbolKind.CAPTURED_VAR).values().iterator(); localIterator.hasNext(); ) { localSymbol = (Symbol)localIterator.next();
/* 1992 */           localListBuffer1.append(LambdaToMethod.this.make.VarDef((Symbol.VarSymbol)localSymbol, null));
/* 1993 */           localListBuffer2.append((Symbol.VarSymbol)localSymbol);
/*      */         }
/* 1995 */         Symbol localSymbol;
/* 1995 */         for (localIterator = getSymbolMap(LambdaToMethod.LambdaSymbolKind.PARAM).values().iterator(); localIterator.hasNext(); ) { localSymbol = (Symbol)localIterator.next();
/* 1996 */           localListBuffer1.append(LambdaToMethod.this.make.VarDef((Symbol.VarSymbol)localSymbol, null));
/* 1997 */           localListBuffer2.append((Symbol.VarSymbol)localSymbol);
/*      */         }
/* 1999 */         this.syntheticParams = localListBuffer1.toList();
/*      */ 
/* 2001 */         this.translatedSym.params = localListBuffer2.toList();
/*      */ 
/* 2004 */         this.translatedSym.name = (isSerializable() ? 
/* 2005 */           serializedLambdaName() : 
/* 2006 */           lambdaName());
/*      */ 
/* 2009 */         this.translatedSym.type = LambdaToMethod.this.types.createMethodTypeWithParameters(
/* 2010 */           generatedLambdaSig(), 
/* 2011 */           TreeInfo.types(this.syntheticParams));
/*      */       }
/*      */ 
/*      */       Type generatedLambdaSig()
/*      */       {
/* 2015 */         return LambdaToMethod.this.types.erasure(((JCTree.JCLambda)this.tree).getDescriptorType(LambdaToMethod.this.types));
/*      */       }
/*      */     }
/*      */ 
/*      */     private final class ReferenceTranslationContext extends LambdaToMethod.LambdaAnalyzerPreprocessor.TranslationContext<JCTree.JCMemberReference>
/*      */     {
/*      */       final boolean isSuper;
/*      */       final Symbol sigPolySym;
/*      */ 
/*      */       ReferenceTranslationContext(JCTree.JCMemberReference arg2)
/*      */       {
/* 2031 */         super(localJCFunctionalExpression);
/* 2032 */         this.isSuper = localJCFunctionalExpression.hasKind(JCTree.JCMemberReference.ReferenceKind.SUPER);
/* 2033 */         this.sigPolySym = (isSignaturePolymorphic() ? LambdaToMethod.this
/* 2034 */           .makePrivateSyntheticMethod(localJCFunctionalExpression.sym
/* 2034 */           .flags(), localJCFunctionalExpression.sym.name, 
/* 2036 */           bridgedRefSig(), localJCFunctionalExpression.sym
/* 2037 */           .enclClass()) : null);
/*      */       }
/*      */ 
/*      */       int referenceKind()
/*      */       {
/* 2045 */         return LambdaToMethod.this.referenceKind(((JCTree.JCMemberReference)this.tree).sym);
/*      */       }
/*      */ 
/*      */       boolean needsVarArgsConversion() {
/* 2049 */         return ((JCTree.JCMemberReference)this.tree).varargsElement != null;
/*      */       }
/*      */ 
/*      */       boolean isArrayOp()
/*      */       {
/* 2056 */         return ((JCTree.JCMemberReference)this.tree).sym.owner == LambdaToMethod.this.syms.arrayClass;
/*      */       }
/*      */ 
/*      */       boolean receiverAccessible()
/*      */       {
/* 2063 */         return ((JCTree.JCMemberReference)this.tree).ownerAccessible;
/*      */       }
/*      */ 
/*      */       boolean isPrivateInOtherClass()
/*      */       {
/* 2072 */         return ((((JCTree.JCMemberReference)this.tree).sym.flags() & 0x2) != 0L) && 
/* 2072 */           (!LambdaToMethod.this.types
/* 2072 */           .isSameType(LambdaToMethod.this.types
/* 2073 */           .erasure(((JCTree.JCMemberReference)this.tree).sym.enclClass().asType()), LambdaToMethod.this.types
/* 2074 */           .erasure(this.owner.enclClass().asType())));
/*      */       }
/*      */ 
/*      */       final boolean isSignaturePolymorphic()
/*      */       {
/* 2083 */         return (((JCTree.JCMemberReference)this.tree).sym.kind == 16) && 
/* 2083 */           (LambdaToMethod.this.types
/* 2083 */           .isSignaturePolymorphic((Symbol.MethodSymbol)((JCTree.JCMemberReference)this.tree).sym));
/*      */       }
/*      */ 
/*      */       boolean interfaceParameterIsIntersectionType()
/*      */       {
/* 2091 */         List localList = ((JCTree.JCMemberReference)this.tree).getDescriptorType(LambdaToMethod.this.types).getParameterTypes();
/* 2092 */         if (((JCTree.JCMemberReference)this.tree).kind == JCTree.JCMemberReference.ReferenceKind.UNBOUND);
/* 2093 */         for (localList = localList.tail; 
/* 2095 */           localList.nonEmpty(); localList = localList.tail) {
/* 2096 */           Type localType = (Type)localList.head;
/* 2097 */           if (localType.getKind() == TypeKind.TYPEVAR) {
/* 2098 */             Type.TypeVar localTypeVar = (Type.TypeVar)localType;
/* 2099 */             if (localTypeVar.bound.getKind() == TypeKind.INTERSECTION) {
/* 2100 */               return true;
/*      */             }
/*      */           }
/*      */         }
/* 2104 */         return false;
/*      */       }
/*      */ 
/*      */       final boolean needsConversionToLambda()
/*      */       {
/* 2112 */         if ((!interfaceParameterIsIntersectionType()) && (!this.isSuper))
/*      */         {
/* 2114 */           if ((!needsVarArgsConversion()) && 
/* 2115 */             (!isArrayOp()) && 
/* 2116 */             (!isPrivateInOtherClass()) && 
/* 2117 */             (receiverAccessible()))
/* 2118 */             if ((((JCTree.JCMemberReference)this.tree)
/* 2118 */               .getMode() != MemberReferenceTree.ReferenceMode.NEW) || (((JCTree.JCMemberReference)this.tree).kind == JCTree.JCMemberReference.ReferenceKind.ARRAY_CTOR)) break label116;
/*      */         }
/* 2120 */         label116: return (((JCTree.JCMemberReference)this.tree).sym.owner
/* 2120 */           .isLocal()) || (((JCTree.JCMemberReference)this.tree).sym.owner.isInner());
/*      */       }
/*      */ 
/*      */       Type generatedRefSig() {
/* 2124 */         return LambdaToMethod.this.types.erasure(((JCTree.JCMemberReference)this.tree).sym.type);
/*      */       }
/*      */ 
/*      */       Type bridgedRefSig() {
/* 2128 */         return LambdaToMethod.this.types.erasure(LambdaToMethod.this.types.findDescriptorSymbol(((Type)((JCTree.JCMemberReference)this.tree).targets.head).tsym).type);
/*      */       }
/*      */     }
/*      */ 
/*      */     private class SyntheticMethodNameCounter
/*      */     {
/* 1135 */       private Map<String, Integer> map = new HashMap();
/*      */ 
/*      */       private SyntheticMethodNameCounter() {  } 
/* 1137 */       int getIndex(StringBuilder paramStringBuilder) { String str = paramStringBuilder.toString();
/* 1138 */         Integer localInteger = (Integer)this.map.get(str);
/* 1139 */         if (localInteger == null) {
/* 1140 */           localInteger = Integer.valueOf(0);
/*      */         }
/* 1142 */         localInteger = Integer.valueOf(localInteger.intValue() + 1);
/* 1143 */         this.map.put(str, localInteger);
/* 1144 */         return localInteger.intValue();
/*      */       }
/*      */     }
/*      */ 
/*      */     private abstract class TranslationContext<T extends JCTree.JCFunctionalExpression>
/*      */     {
/*      */       final T tree;
/*      */       final Symbol owner;
/*      */       final int depth;
/*      */       final TranslationContext<?> prev;
/*      */       final List<Symbol> bridges;
/*      */ 
/*      */       TranslationContext()
/*      */       {
/*      */         Object localObject;
/* 1689 */         this.tree = localObject;
/* 1690 */         this.owner = LambdaToMethod.LambdaAnalyzerPreprocessor.this.owner();
/* 1691 */         this.depth = (LambdaToMethod.LambdaAnalyzerPreprocessor.this.frameStack.size() - 1);
/* 1692 */         this.prev = LambdaToMethod.LambdaAnalyzerPreprocessor.this.context();
/*      */ 
/* 1694 */         Symbol.ClassSymbol localClassSymbol = LambdaToMethod.this.types
/* 1694 */           .makeFunctionalInterfaceClass(LambdaToMethod.this.attrEnv, LambdaToMethod.this.names.empty, localObject.targets, 1536L);
/* 1695 */         this.bridges = LambdaToMethod.this.types.functionalInterfaceBridges(localClassSymbol);
/*      */       }
/*      */ 
/*      */       boolean needsAltMetafactory()
/*      */       {
/* 1702 */         return (this.tree.targets.length() > 1) || 
/* 1701 */           (isSerializable()) || 
/* 1702 */           (this.bridges
/* 1702 */           .length() > 1);
/*      */       }
/*      */ 
/*      */       boolean isSerializable()
/*      */       {
/* 1707 */         if (LambdaToMethod.this.forceSerializable) {
/* 1708 */           return true;
/*      */         }
/* 1710 */         for (Type localType : this.tree.targets) {
/* 1711 */           if (LambdaToMethod.this.types.asSuper(localType, LambdaToMethod.this.syms.serializableType.tsym) != null) {
/* 1712 */             return true;
/*      */           }
/*      */         }
/* 1715 */         return false;
/*      */       }
/*      */ 
/*      */       String enclosingMethodName()
/*      */       {
/* 1723 */         return syntheticMethodNameComponent(this.owner.name);
/*      */       }
/*      */ 
/*      */       String syntheticMethodNameComponent(Name paramName)
/*      */       {
/* 1731 */         if (paramName == null) {
/* 1732 */           return "null";
/*      */         }
/* 1734 */         String str = paramName.toString();
/* 1735 */         if (str.equals("<clinit>"))
/* 1736 */           str = "static";
/* 1737 */         else if (str.equals("<init>")) {
/* 1738 */           str = "new";
/*      */         }
/* 1740 */         return str;
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   static enum LambdaSymbolKind
/*      */   {
/* 2139 */     PARAM, 
/* 2140 */     LOCAL_VAR, 
/* 2141 */     CAPTURED_VAR, 
/* 2142 */     CAPTURED_THIS, 
/* 2143 */     TYPE_VAR;
/*      */   }
/*      */ 
/*      */   private class MemberReferenceToLambda
/*      */   {
/*      */     private final JCTree.JCMemberReference tree;
/*      */     private final LambdaToMethod.LambdaAnalyzerPreprocessor.ReferenceTranslationContext localContext;
/*      */     private final Symbol owner;
/*  751 */     private final ListBuffer<JCTree.JCExpression> args = new ListBuffer();
/*  752 */     private final ListBuffer<JCTree.JCVariableDecl> params = new ListBuffer();
/*      */ 
/*  754 */     private JCTree.JCExpression receiverExpression = null;
/*      */ 
/*      */     MemberReferenceToLambda(JCTree.JCMemberReference paramReferenceTranslationContext, LambdaToMethod.LambdaAnalyzerPreprocessor.ReferenceTranslationContext paramSymbol, Symbol arg4) {
/*  757 */       this.tree = paramReferenceTranslationContext;
/*  758 */       this.localContext = paramSymbol;
/*      */       Object localObject;
/*  759 */       this.owner = localObject;
/*      */     }
/*      */ 
/*      */     JCTree.JCLambda lambda() {
/*  763 */       int i = LambdaToMethod.this.make.pos;
/*      */       try {
/*  765 */         LambdaToMethod.this.make.at(this.tree);
/*      */ 
/*  769 */         Symbol.VarSymbol localVarSymbol = addParametersReturnReceiver();
/*      */ 
/*  772 */         JCTree.JCExpression localJCExpression = this.tree.getMode() == MemberReferenceTree.ReferenceMode.INVOKE ? 
/*  771 */           expressionInvoke(localVarSymbol) : 
/*  772 */           expressionNew();
/*      */ 
/*  774 */         JCTree.JCLambda localJCLambda1 = LambdaToMethod.this.make.Lambda(this.params.toList(), localJCExpression);
/*  775 */         localJCLambda1.targets = this.tree.targets;
/*  776 */         localJCLambda1.type = this.tree.type;
/*  777 */         localJCLambda1.pos = this.tree.pos;
/*  778 */         return localJCLambda1;
/*      */       } finally {
/*  780 */         LambdaToMethod.this.make.at(i);
/*      */       }
/*      */     }
/*      */ 
/*      */     Symbol.VarSymbol addParametersReturnReceiver()
/*      */     {
/*  790 */       Type localType1 = this.localContext.bridgedRefSig();
/*  791 */       List localList1 = localType1.getParameterTypes();
/*  792 */       List localList2 = this.tree.getDescriptorType(LambdaToMethod.this.types).getParameterTypes();
/*      */       Symbol.VarSymbol localVarSymbol;
/*  796 */       switch (LambdaToMethod.1.$SwitchMap$com$sun$tools$javac$tree$JCTree$JCMemberReference$ReferenceKind[this.tree.kind.ordinal()])
/*      */       {
/*      */       case 3:
/*  799 */         localVarSymbol = addParameter("rec$", this.tree.getQualifierExpression().type, false);
/*  800 */         this.receiverExpression = LambdaToMethod.this.attr.makeNullCheck(this.tree.getQualifierExpression());
/*  801 */         break;
/*      */       case 4:
/*  805 */         localVarSymbol = addParameter("rec$", (Type)localType1.getParameterTypes().head, false);
/*  806 */         localList1 = localList1.tail;
/*  807 */         localList2 = localList2.tail;
/*  808 */         break;
/*      */       default:
/*  810 */         localVarSymbol = null;
/*      */       }
/*      */ 
/*  813 */       List localList3 = this.tree.sym.type.getParameterTypes();
/*  814 */       int i = localList3.size();
/*  815 */       int j = localList1.size();
/*      */ 
/*  817 */       int k = this.localContext.needsVarArgsConversion() ? i - 1 : i;
/*      */ 
/*  820 */       int m = (this.tree.varargsElement != null) || (i == localList2.size()) ? 1 : 0;
/*      */ 
/*  829 */       for (int n = 0; (localList3.nonEmpty()) && (n < k); n++)
/*      */       {
/*  831 */         Type localType2 = (Type)localList3.head;
/*      */ 
/*  835 */         if ((m != 0) && (((Type)localList2.head).getKind() == TypeKind.TYPEVAR)) {
/*  836 */           Type.TypeVar localTypeVar = (Type.TypeVar)localList2.head;
/*  837 */           if (localTypeVar.bound.getKind() == TypeKind.INTERSECTION) {
/*  838 */             localType2 = (Type)localList1.head;
/*      */           }
/*      */         }
/*  841 */         addParameter("x$" + n, localType2, true);
/*      */ 
/*  844 */         localList3 = localList3.tail;
/*  845 */         localList1 = localList1.tail;
/*  846 */         localList2 = localList2.tail;
/*      */       }
/*      */ 
/*  849 */       for (n = k; n < j; n++) {
/*  850 */         addParameter("xva$" + n, this.tree.varargsElement, true);
/*      */       }
/*      */ 
/*  853 */       return localVarSymbol;
/*      */     }
/*      */ 
/*      */     JCTree.JCExpression getReceiverExpression() {
/*  857 */       return this.receiverExpression;
/*      */     }
/*      */ 
/*      */     private JCTree.JCExpression makeReceiver(Symbol.VarSymbol paramVarSymbol) {
/*  861 */       if (paramVarSymbol == null) return null;
/*  862 */       Object localObject = LambdaToMethod.this.make.Ident(paramVarSymbol);
/*  863 */       Type localType = this.tree.sym.enclClass().type;
/*  864 */       if (localType == LambdaToMethod.this.syms.arrayClass.type)
/*      */       {
/*  866 */         localType = this.tree.getQualifierExpression().type;
/*      */       }
/*  868 */       if (!paramVarSymbol.type.tsym.isSubClass(localType.tsym, LambdaToMethod.this.types)) {
/*  869 */         localObject = LambdaToMethod.this.make.TypeCast(LambdaToMethod.this.make.Type(localType), (JCTree.JCExpression)localObject).setType(localType);
/*      */       }
/*  871 */       return localObject;
/*      */     }
/*      */ 
/*      */     private JCTree.JCExpression expressionInvoke(Symbol.VarSymbol paramVarSymbol)
/*      */     {
/*  884 */       JCTree.JCExpression localJCExpression = paramVarSymbol != null ? 
/*  883 */         makeReceiver(paramVarSymbol) : this.tree.sym
/*  880 */         .isStatic() ? LambdaToMethod.this.make
/*  881 */         .Type(this.tree.sym.owner.type) : 
/*  883 */         this.tree
/*  884 */         .getQualifierExpression();
/*      */ 
/*  887 */       JCTree.JCFieldAccess localJCFieldAccess = LambdaToMethod.this.make.Select(localJCExpression, this.tree.sym.name);
/*  888 */       localJCFieldAccess.sym = this.tree.sym;
/*  889 */       localJCFieldAccess.type = this.tree.sym.erasure(LambdaToMethod.this.types);
/*      */ 
/*  894 */       Object localObject = LambdaToMethod.this.make.Apply(List.nil(), localJCFieldAccess, LambdaToMethod.this
/*  893 */         .convertArgs(this.tree.sym, this.args
/*  893 */         .toList(), this.tree.varargsElement))
/*  894 */         .setType(this.tree.sym
/*  894 */         .erasure(LambdaToMethod.this.types)
/*  894 */         .getReturnType());
/*      */ 
/*  896 */       localObject = LambdaToMethod.this.transTypes.coerce((JCTree.JCExpression)localObject, this.localContext.generatedRefSig().getReturnType());
/*  897 */       LambdaToMethod.this.setVarargsIfNeeded((JCTree)localObject, this.tree.varargsElement);
/*  898 */       return localObject;
/*      */     }
/*      */ 
/*      */     private JCTree.JCExpression expressionNew()
/*      */     {
/*  905 */       if (this.tree.kind == JCTree.JCMemberReference.ReferenceKind.ARRAY_CTOR)
/*      */       {
/*  907 */         localObject = LambdaToMethod.this.make.NewArray(LambdaToMethod.this.make
/*  908 */           .Type(LambdaToMethod.this.types.elemtype(this.tree.getQualifierExpression().type)), 
/*  909 */           List.of(LambdaToMethod.this.make
/*  909 */           .Ident((JCTree.JCVariableDecl)this.params.first())), null);
/*      */ 
/*  911 */         ((JCTree.JCNewArray)localObject).type = this.tree.getQualifierExpression().type;
/*  912 */         return localObject;
/*      */       }
/*      */ 
/*  917 */       Object localObject = LambdaToMethod.this.make.NewClass(null, 
/*  918 */         List.nil(), LambdaToMethod.this.make
/*  919 */         .Type(this.tree.getQualifierExpression().type), LambdaToMethod.this
/*  920 */         .convertArgs(this.tree.sym, this.args
/*  920 */         .toList(), this.tree.varargsElement), null);
/*      */ 
/*  922 */       ((JCTree.JCNewClass)localObject).constructor = this.tree.sym;
/*  923 */       ((JCTree.JCNewClass)localObject).constructorType = this.tree.sym.erasure(LambdaToMethod.this.types);
/*  924 */       ((JCTree.JCNewClass)localObject).type = this.tree.getQualifierExpression().type;
/*  925 */       LambdaToMethod.this.setVarargsIfNeeded((JCTree)localObject, this.tree.varargsElement);
/*  926 */       return localObject;
/*      */     }
/*      */ 
/*      */     private Symbol.VarSymbol addParameter(String paramString, Type paramType, boolean paramBoolean)
/*      */     {
/*  931 */       Symbol.VarSymbol localVarSymbol = new Symbol.VarSymbol(8589938688L, LambdaToMethod.this.names.fromString(paramString), paramType, this.owner);
/*  932 */       localVarSymbol.pos = this.tree.pos;
/*  933 */       this.params.append(LambdaToMethod.this.make.VarDef(localVarSymbol, null));
/*  934 */       if (paramBoolean) {
/*  935 */         this.args.append(LambdaToMethod.this.make.Ident(localVarSymbol));
/*      */       }
/*  937 */       return localVarSymbol;
/*      */     }
/*      */   }
/*      */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.javac.comp.LambdaToMethod
 * JD-Core Version:    0.6.2
 */