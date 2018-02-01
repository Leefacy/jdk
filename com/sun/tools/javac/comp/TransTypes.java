/*      */ package com.sun.tools.javac.comp;
/*      */ 
/*      */ import com.sun.tools.javac.code.Scope;
/*      */ import com.sun.tools.javac.code.Scope.Entry;
/*      */ import com.sun.tools.javac.code.Source;
/*      */ import com.sun.tools.javac.code.Symbol;
/*      */ import com.sun.tools.javac.code.Symbol.ClassSymbol;
/*      */ import com.sun.tools.javac.code.Symbol.MethodSymbol;
/*      */ import com.sun.tools.javac.code.Symbol.TypeSymbol;
/*      */ import com.sun.tools.javac.code.Symbol.VarSymbol;
/*      */ import com.sun.tools.javac.code.Symtab;
/*      */ import com.sun.tools.javac.code.Type;
/*      */ import com.sun.tools.javac.code.Type.IntersectionClassType;
/*      */ import com.sun.tools.javac.code.Type.JCPrimitiveType;
/*      */ import com.sun.tools.javac.code.Type.MethodType;
/*      */ import com.sun.tools.javac.code.TypeTag;
/*      */ import com.sun.tools.javac.code.Types;
/*      */ import com.sun.tools.javac.tree.JCTree;
/*      */ import com.sun.tools.javac.tree.JCTree.JCAnnotation;
/*      */ import com.sun.tools.javac.tree.JCTree.JCArrayAccess;
/*      */ import com.sun.tools.javac.tree.JCTree.JCArrayTypeTree;
/*      */ import com.sun.tools.javac.tree.JCTree.JCAssert;
/*      */ import com.sun.tools.javac.tree.JCTree.JCAssign;
/*      */ import com.sun.tools.javac.tree.JCTree.JCAssignOp;
/*      */ import com.sun.tools.javac.tree.JCTree.JCBinary;
/*      */ import com.sun.tools.javac.tree.JCTree.JCBlock;
/*      */ import com.sun.tools.javac.tree.JCTree.JCCase;
/*      */ import com.sun.tools.javac.tree.JCTree.JCClassDecl;
/*      */ import com.sun.tools.javac.tree.JCTree.JCConditional;
/*      */ import com.sun.tools.javac.tree.JCTree.JCDoWhileLoop;
/*      */ import com.sun.tools.javac.tree.JCTree.JCEnhancedForLoop;
/*      */ import com.sun.tools.javac.tree.JCTree.JCExpression;
/*      */ import com.sun.tools.javac.tree.JCTree.JCExpressionStatement;
/*      */ import com.sun.tools.javac.tree.JCTree.JCFieldAccess;
/*      */ import com.sun.tools.javac.tree.JCTree.JCForLoop;
/*      */ import com.sun.tools.javac.tree.JCTree.JCIdent;
/*      */ import com.sun.tools.javac.tree.JCTree.JCIf;
/*      */ import com.sun.tools.javac.tree.JCTree.JCInstanceOf;
/*      */ import com.sun.tools.javac.tree.JCTree.JCLambda;
/*      */ import com.sun.tools.javac.tree.JCTree.JCMemberReference;
/*      */ import com.sun.tools.javac.tree.JCTree.JCMethodDecl;
/*      */ import com.sun.tools.javac.tree.JCTree.JCMethodInvocation;
/*      */ import com.sun.tools.javac.tree.JCTree.JCNewArray;
/*      */ import com.sun.tools.javac.tree.JCTree.JCNewClass;
/*      */ import com.sun.tools.javac.tree.JCTree.JCParens;
/*      */ import com.sun.tools.javac.tree.JCTree.JCReturn;
/*      */ import com.sun.tools.javac.tree.JCTree.JCStatement;
/*      */ import com.sun.tools.javac.tree.JCTree.JCSwitch;
/*      */ import com.sun.tools.javac.tree.JCTree.JCSynchronized;
/*      */ import com.sun.tools.javac.tree.JCTree.JCThrow;
/*      */ import com.sun.tools.javac.tree.JCTree.JCTry;
/*      */ import com.sun.tools.javac.tree.JCTree.JCTypeApply;
/*      */ import com.sun.tools.javac.tree.JCTree.JCTypeCast;
/*      */ import com.sun.tools.javac.tree.JCTree.JCTypeIntersection;
/*      */ import com.sun.tools.javac.tree.JCTree.JCUnary;
/*      */ import com.sun.tools.javac.tree.JCTree.JCVariableDecl;
/*      */ import com.sun.tools.javac.tree.JCTree.JCWhileLoop;
/*      */ import com.sun.tools.javac.tree.TreeInfo;
/*      */ import com.sun.tools.javac.tree.TreeMaker;
/*      */ import com.sun.tools.javac.tree.TreeTranslator;
/*      */ import com.sun.tools.javac.util.Assert;
/*      */ import com.sun.tools.javac.util.Context;
/*      */ import com.sun.tools.javac.util.Context.Key;
/*      */ import com.sun.tools.javac.util.Filter;
/*      */ import com.sun.tools.javac.util.JCDiagnostic.DiagnosticPosition;
/*      */ import com.sun.tools.javac.util.List;
/*      */ import com.sun.tools.javac.util.ListBuffer;
/*      */ import com.sun.tools.javac.util.Log;
/*      */ import com.sun.tools.javac.util.Names;
/*      */ import java.util.HashMap;
/*      */ import java.util.Map;
/*      */ 
/*      */ public class TransTypes extends TreeTranslator
/*      */ {
/*   54 */   protected static final Context.Key<TransTypes> transTypesKey = new Context.Key();
/*      */   private Names names;
/*      */   private Log log;
/*      */   private Symtab syms;
/*      */   private TreeMaker make;
/*      */   private Enter enter;
/*      */   private boolean allowEnums;
/*      */   private boolean allowInterfaceBridges;
/*      */   private Types types;
/*      */   private final Resolve resolve;
/*      */   private final boolean addBridges;
/*      */   private final CompileStates compileStates;
/*      */   Map<Symbol.MethodSymbol, Symbol.MethodSymbol> overridden;
/*  393 */   private Filter<Symbol> overrideBridgeFilter = new Filter() {
/*      */     public boolean accepts(Symbol paramAnonymousSymbol) {
/*  395 */       return (paramAnonymousSymbol.flags() & 0x1000) != 4096L;
/*      */     }
/*  393 */   };
/*      */   private Type pt;
/*  515 */   JCTree currentMethod = null;
/*      */   private Env<AttrContext> env;
/*      */   private static final String statePreviousToFlowAssertMsg = "The current compile state [%s] of class %s is previous to FLOW";
/*      */ 
/*      */   public static TransTypes instance(Context paramContext)
/*      */   {
/*   59 */     TransTypes localTransTypes = (TransTypes)paramContext.get(transTypesKey);
/*   60 */     if (localTransTypes == null)
/*   61 */       localTransTypes = new TransTypes(paramContext);
/*   62 */     return localTransTypes;
/*      */   }
/*      */ 
/*      */   protected TransTypes(Context paramContext)
/*      */   {
/*   85 */     paramContext.put(transTypesKey, this);
/*   86 */     this.compileStates = CompileStates.instance(paramContext);
/*   87 */     this.names = Names.instance(paramContext);
/*   88 */     this.log = Log.instance(paramContext);
/*   89 */     this.syms = Symtab.instance(paramContext);
/*   90 */     this.enter = Enter.instance(paramContext);
/*   91 */     this.overridden = new HashMap();
/*   92 */     Source localSource = Source.instance(paramContext);
/*   93 */     this.allowEnums = localSource.allowEnums();
/*   94 */     this.addBridges = localSource.addBridges();
/*   95 */     this.allowInterfaceBridges = localSource.allowDefaultMethods();
/*   96 */     this.types = Types.instance(paramContext);
/*   97 */     this.make = TreeMaker.instance(paramContext);
/*   98 */     this.resolve = Resolve.instance(paramContext);
/*      */   }
/*      */ 
/*      */   JCTree.JCExpression cast(JCTree.JCExpression paramJCExpression, Type paramType)
/*      */   {
/*  112 */     int i = this.make.pos;
/*  113 */     this.make.at(paramJCExpression.pos);
/*  114 */     if (!this.types.isSameType(paramJCExpression.type, paramType)) {
/*  115 */       if (!this.resolve.isAccessible(this.env, paramType.tsym))
/*  116 */         this.resolve.logAccessErrorInternal(this.env, paramJCExpression, paramType);
/*  117 */       paramJCExpression = this.make.TypeCast(this.make.Type(paramType), paramJCExpression).setType(paramType);
/*      */     }
/*  119 */     this.make.pos = i;
/*  120 */     return paramJCExpression;
/*      */   }
/*      */ 
/*      */   public JCTree.JCExpression coerce(Env<AttrContext> paramEnv, JCTree.JCExpression paramJCExpression, Type paramType)
/*      */   {
/*  130 */     Env localEnv = this.env;
/*      */     try {
/*  132 */       this.env = paramEnv;
/*  133 */       return coerce(paramJCExpression, paramType);
/*      */     }
/*      */     finally {
/*  136 */       this.env = localEnv;
/*      */     }
/*      */   }
/*      */ 
/*  140 */   JCTree.JCExpression coerce(JCTree.JCExpression paramJCExpression, Type paramType) { Type localType = paramType.baseType();
/*  141 */     if (paramJCExpression.type.isPrimitive() == paramType.isPrimitive())
/*      */     {
/*  144 */       return this.types.isAssignable(paramJCExpression.type, localType, this.types.noWarnings) ? paramJCExpression : 
/*  144 */         cast(paramJCExpression, localType);
/*      */     }
/*      */ 
/*  146 */     return paramJCExpression;
/*      */   }
/*      */ 
/*      */   JCTree.JCExpression retype(JCTree.JCExpression paramJCExpression, Type paramType1, Type paramType2)
/*      */   {
/*  176 */     if (!paramType1.isPrimitive()) {
/*  177 */       if ((paramType2 != null) && (paramType2.isPrimitive())) {
/*  178 */         paramType2 = erasure(paramJCExpression.type);
/*      */       }
/*  180 */       paramJCExpression.type = paramType1;
/*  181 */       if (paramType2 != null) {
/*  182 */         return coerce(paramJCExpression, paramType2);
/*      */       }
/*      */     }
/*  185 */     return paramJCExpression;
/*      */   }
/*      */ 
/*      */   <T extends JCTree> List<T> translateArgs(List<T> paramList, List<Type> paramList1, Type paramType)
/*      */   {
/*  198 */     if (paramList1.isEmpty()) return paramList;
/*  199 */     Object localObject = paramList;
/*  200 */     while (paramList1.tail.nonEmpty()) {
/*  201 */       ((List)localObject).head = translate((JCTree)((List)localObject).head, (Type)paramList1.head);
/*  202 */       localObject = ((List)localObject).tail;
/*  203 */       paramList1 = paramList1.tail;
/*      */     }
/*  205 */     Type localType = (Type)paramList1.head;
/*  206 */     Assert.check((paramType != null) || (((List)localObject).length() == 1));
/*  207 */     if (paramType != null) {
/*  208 */       while (((List)localObject).nonEmpty()) {
/*  209 */         ((List)localObject).head = translate((JCTree)((List)localObject).head, paramType);
/*  210 */         localObject = ((List)localObject).tail;
/*      */       }
/*      */     }
/*  213 */     ((List)localObject).head = translate((JCTree)((List)localObject).head, localType);
/*      */ 
/*  215 */     return paramList;
/*      */   }
/*      */ 
/*      */   public <T extends JCTree> List<T> translateArgs(List<T> paramList, List<Type> paramList1, Type paramType, Env<AttrContext> paramEnv)
/*      */   {
/*  222 */     Env localEnv = this.env;
/*      */     try {
/*  224 */       this.env = paramEnv;
/*  225 */       return translateArgs(paramList, paramList1, paramType);
/*      */     }
/*      */     finally {
/*  228 */       this.env = localEnv;
/*      */     }
/*      */   }
/*      */ 
/*      */   void addBridge(JCDiagnostic.DiagnosticPosition paramDiagnosticPosition, Symbol.MethodSymbol paramMethodSymbol1, Symbol.MethodSymbol paramMethodSymbol2, Symbol.ClassSymbol paramClassSymbol, boolean paramBoolean, ListBuffer<JCTree> paramListBuffer)
/*      */   {
/*  251 */     this.make.at(paramDiagnosticPosition);
/*  252 */     Type localType1 = this.types.memberType(paramClassSymbol.type, paramMethodSymbol1);
/*  253 */     Type localType2 = erasure(localType1);
/*      */ 
/*  256 */     Type localType3 = paramMethodSymbol1.erasure(this.types);
/*      */ 
/*  258 */     long l = paramMethodSymbol2.flags() & 0x7 | 0x1000 | 0x80000000 | (paramClassSymbol
/*  258 */       .isInterface() ? 8796093022208L : 0L);
/*  259 */     if (paramBoolean) l |= 137438953472L;
/*  260 */     Symbol.MethodSymbol localMethodSymbol = new Symbol.MethodSymbol(l, paramMethodSymbol1.name, localType3, paramClassSymbol);
/*      */ 
/*  267 */     localMethodSymbol.params = createBridgeParams(paramMethodSymbol2, localMethodSymbol, localType3);
/*  268 */     localMethodSymbol.setAttributes(paramMethodSymbol2);
/*      */ 
/*  270 */     if (!paramBoolean) {
/*  271 */       JCTree.JCMethodDecl localJCMethodDecl = this.make.MethodDef(localMethodSymbol, null);
/*      */ 
/*  277 */       JCTree.JCIdent localJCIdent = paramMethodSymbol2.owner == paramClassSymbol ? this.make
/*  276 */         .This(paramClassSymbol
/*  276 */         .erasure(this.types)) : 
/*  276 */         this.make
/*  277 */         .Super(this.types
/*  277 */         .supertype(paramClassSymbol.type).tsym
/*  277 */         .erasure(this.types), paramClassSymbol);
/*      */ 
/*  280 */       Type localType4 = erasure(paramMethodSymbol2.type.getReturnType());
/*      */ 
/*  289 */       JCTree.JCMethodInvocation localJCMethodInvocation = this.make
/*  285 */         .Apply(null, this.make
/*  287 */         .Select(localJCIdent, paramMethodSymbol2)
/*  287 */         .setType(localType4), 
/*  288 */         translateArgs(this.make
/*  288 */         .Idents(localJCMethodDecl.params), 
/*  288 */         localType2.getParameterTypes(), null))
/*  289 */         .setType(localType4);
/*      */ 
/*  292 */       JCTree.JCReturn localJCReturn = localType2.getReturnType().hasTag(TypeTag.VOID) ? this.make
/*  291 */         .Exec(localJCMethodInvocation) : 
/*  291 */         this.make
/*  292 */         .Return(coerce(localJCMethodInvocation, localType3
/*  292 */         .getReturnType()));
/*  293 */       localJCMethodDecl.body = this.make.Block(0L, List.of(localJCReturn));
/*      */ 
/*  296 */       paramListBuffer.append(localJCMethodDecl);
/*      */     }
/*      */ 
/*  300 */     paramClassSymbol.members().enter(localMethodSymbol);
/*  301 */     this.overridden.put(localMethodSymbol, paramMethodSymbol1);
/*      */   }
/*      */ 
/*      */   private List<Symbol.VarSymbol> createBridgeParams(Symbol.MethodSymbol paramMethodSymbol1, Symbol.MethodSymbol paramMethodSymbol2, Type paramType)
/*      */   {
/*  306 */     List localList1 = null;
/*  307 */     if (paramMethodSymbol1.params != null) {
/*  308 */       localList1 = List.nil();
/*  309 */       List localList2 = paramMethodSymbol1.params;
/*  310 */       Type.MethodType localMethodType = (Type.MethodType)paramType;
/*  311 */       List localList3 = localMethodType.argtypes;
/*  312 */       while ((localList2.nonEmpty()) && (localList3.nonEmpty())) {
/*  313 */         Symbol.VarSymbol localVarSymbol = new Symbol.VarSymbol(((Symbol.VarSymbol)localList2.head).flags() | 0x1000 | 0x0, ((Symbol.VarSymbol)localList2.head).name, (Type)localList3.head, paramMethodSymbol2);
/*      */ 
/*  315 */         localVarSymbol.setAttributes((Symbol)localList2.head);
/*  316 */         localList1 = localList1.append(localVarSymbol);
/*  317 */         localList2 = localList2.tail;
/*  318 */         localList3 = localList3.tail;
/*      */       }
/*      */     }
/*  321 */     return localList1;
/*      */   }
/*      */ 
/*      */   void addBridgeIfNeeded(JCDiagnostic.DiagnosticPosition paramDiagnosticPosition, Symbol paramSymbol, Symbol.ClassSymbol paramClassSymbol, ListBuffer<JCTree> paramListBuffer)
/*      */   {
/*  346 */     if ((paramSymbol.kind == 16) && (paramSymbol.name != this.names.init))
/*      */     {
/*  348 */       if (((paramSymbol
/*  348 */         .flags() & 0xA) == 0L) && 
/*  349 */         ((paramSymbol
/*  349 */         .flags() & 0x1000) != 4096L) && 
/*  350 */         (paramSymbol
/*  350 */         .isMemberOf(paramClassSymbol, this.types)))
/*      */       {
/*  352 */         Symbol.MethodSymbol localMethodSymbol1 = (Symbol.MethodSymbol)paramSymbol;
/*  353 */         Symbol.MethodSymbol localMethodSymbol2 = localMethodSymbol1.binaryImplementation(paramClassSymbol, this.types);
/*  354 */         Symbol.MethodSymbol localMethodSymbol3 = localMethodSymbol1.implementation(paramClassSymbol, this.types, true, this.overrideBridgeFilter);
/*  355 */         if ((localMethodSymbol2 != null) && (localMethodSymbol2 != localMethodSymbol1)) { if (localMethodSymbol3 != null) {
/*  357 */             if (localMethodSymbol2.owner
/*  357 */               .isSubClass(localMethodSymbol3.owner, this.types));
/*      */           } } else {
/*  359 */           if ((localMethodSymbol3 != null) && (isBridgeNeeded(localMethodSymbol1, localMethodSymbol3, paramClassSymbol.type))) {
/*  360 */             addBridge(paramDiagnosticPosition, localMethodSymbol1, localMethodSymbol3, paramClassSymbol, localMethodSymbol2 == localMethodSymbol3, paramListBuffer); return;
/*  361 */           }if ((localMethodSymbol3 != localMethodSymbol1) || (localMethodSymbol3.owner == paramClassSymbol))
/*      */             return;
/*  363 */           if (((localMethodSymbol3
/*  363 */             .flags() & 0x10) != 0L) || 
/*  364 */             ((localMethodSymbol1
/*  364 */             .flags() & 0x401) != 1L) || 
/*  365 */             ((paramClassSymbol
/*  365 */             .flags() & 1L) <= (localMethodSymbol3.owner.flags() & 1L))) {
/*      */             return;
/*      */           }
/*  368 */           addBridge(paramDiagnosticPosition, localMethodSymbol1, localMethodSymbol3, paramClassSymbol, false, paramListBuffer); return;
/*      */         }
/*  370 */         if ((localMethodSymbol2.flags() & 0x1000) == 4096L) {
/*  371 */           Symbol.MethodSymbol localMethodSymbol4 = (Symbol.MethodSymbol)this.overridden.get(localMethodSymbol2);
/*  372 */           if ((localMethodSymbol4 != null) && (localMethodSymbol4 != localMethodSymbol1) && (
/*  373 */             (localMethodSymbol3 == null) || (!localMethodSymbol3.overrides(localMethodSymbol4, paramClassSymbol, this.types, true))))
/*      */           {
/*  375 */             this.log.error(paramDiagnosticPosition, "name.clash.same.erasure.no.override", new Object[] { localMethodSymbol4, localMethodSymbol4
/*  376 */               .location(paramClassSymbol.type, this.types), 
/*  376 */               localMethodSymbol1, localMethodSymbol1
/*  377 */               .location(paramClassSymbol.type, this.types) });
/*      */           }
/*      */ 
/*      */         }
/*  380 */         else if (!localMethodSymbol2.overrides(localMethodSymbol1, paramClassSymbol, this.types, true))
/*      */         {
/*  382 */           if ((localMethodSymbol2.owner == paramClassSymbol) || 
/*  383 */             (this.types
/*  383 */             .asSuper(localMethodSymbol2.owner.type, localMethodSymbol1.owner) == null))
/*      */           {
/*  386 */             this.log.error(paramDiagnosticPosition, "name.clash.same.erasure.no.override", new Object[] { localMethodSymbol2, localMethodSymbol2
/*  387 */               .location(paramClassSymbol.type, this.types), 
/*  387 */               localMethodSymbol1, localMethodSymbol1
/*  388 */               .location(paramClassSymbol.type, this.types) });
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   private boolean isBridgeNeeded(Symbol.MethodSymbol paramMethodSymbol1, Symbol.MethodSymbol paramMethodSymbol2, Type paramType)
/*      */   {
/*  407 */     if (paramMethodSymbol2 != paramMethodSymbol1)
/*      */     {
/*  410 */       Type localType1 = paramMethodSymbol1.erasure(this.types);
/*  411 */       if (!isSameMemberWhenErased(paramType, paramMethodSymbol1, localType1))
/*  412 */         return true;
/*  413 */       Type localType2 = paramMethodSymbol2.erasure(this.types);
/*  414 */       if (!isSameMemberWhenErased(paramType, paramMethodSymbol2, localType2)) {
/*  415 */         return true;
/*      */       }
/*      */ 
/*  419 */       return !this.types.isSameType(localType2.getReturnType(), localType1
/*  420 */         .getReturnType());
/*      */     }
/*      */ 
/*  423 */     if ((paramMethodSymbol1.flags() & 0x400) != 0L)
/*      */     {
/*  426 */       return false;
/*      */     }
/*      */ 
/*  432 */     return !isSameMemberWhenErased(paramType, paramMethodSymbol1, paramMethodSymbol1.erasure(this.types));
/*      */   }
/*      */ 
/*      */   private boolean isSameMemberWhenErased(Type paramType1, Symbol.MethodSymbol paramMethodSymbol, Type paramType2)
/*      */   {
/*  445 */     return this.types.isSameType(erasure(this.types.memberType(paramType1, paramMethodSymbol)), paramType2);
/*      */   }
/*      */ 
/*      */   void addBridges(JCDiagnostic.DiagnosticPosition paramDiagnosticPosition, Symbol.TypeSymbol paramTypeSymbol, Symbol.ClassSymbol paramClassSymbol, ListBuffer<JCTree> paramListBuffer)
/*      */   {
/*  453 */     for (Object localObject = paramTypeSymbol.members().elems; localObject != null; localObject = ((Scope.Entry)localObject).sibling)
/*  454 */       addBridgeIfNeeded(paramDiagnosticPosition, ((Scope.Entry)localObject).sym, paramClassSymbol, paramListBuffer);
/*  455 */     for (localObject = this.types.interfaces(paramTypeSymbol.type); ((List)localObject).nonEmpty(); localObject = ((List)localObject).tail)
/*  456 */       addBridges(paramDiagnosticPosition, ((Type)((List)localObject).head).tsym, paramClassSymbol, paramListBuffer);
/*      */   }
/*      */ 
/*      */   void addBridges(JCDiagnostic.DiagnosticPosition paramDiagnosticPosition, Symbol.ClassSymbol paramClassSymbol, ListBuffer<JCTree> paramListBuffer)
/*      */   {
/*  465 */     Type localType = this.types.supertype(paramClassSymbol.type);
/*  466 */     while (localType.hasTag(TypeTag.CLASS))
/*      */     {
/*  468 */       addBridges(paramDiagnosticPosition, localType.tsym, paramClassSymbol, paramListBuffer);
/*  469 */       localType = this.types.supertype(localType);
/*      */     }
/*  471 */     for (List localList = this.types.interfaces(paramClassSymbol.type); localList.nonEmpty(); localList = localList.tail)
/*      */     {
/*  473 */       addBridges(paramDiagnosticPosition, ((Type)localList.head).tsym, paramClassSymbol, paramListBuffer);
/*      */     }
/*      */   }
/*      */ 
/*      */   public <T extends JCTree> T translate(T paramT, Type paramType)
/*      */   {
/*  487 */     Type localType = this.pt;
/*      */     try {
/*  489 */       this.pt = paramType;
/*  490 */       return translate(paramT);
/*      */     } finally {
/*  492 */       this.pt = localType;
/*      */     }
/*      */   }
/*      */ 
/*      */   public <T extends JCTree> List<T> translate(List<T> paramList, Type paramType)
/*      */   {
/*  499 */     Type localType = this.pt;
/*      */     List localList;
/*      */     try {
/*  502 */       this.pt = paramType;
/*  503 */       localList = translate(paramList);
/*      */     } finally {
/*  505 */       this.pt = localType;
/*      */     }
/*  507 */     return localList;
/*      */   }
/*      */ 
/*      */   public void visitClassDef(JCTree.JCClassDecl paramJCClassDecl) {
/*  511 */     translateClass(paramJCClassDecl.sym);
/*  512 */     this.result = paramJCClassDecl;
/*      */   }
/*      */   public void visitMethodDef(JCTree.JCMethodDecl paramJCMethodDecl) {
/*  517 */     JCTree localJCTree = this.currentMethod;
/*      */     Scope.Entry localEntry;
/*      */     try {
/*  519 */       this.currentMethod = paramJCMethodDecl;
/*  520 */       paramJCMethodDecl.restype = ((JCTree.JCExpression)translate(paramJCMethodDecl.restype, null));
/*  521 */       paramJCMethodDecl.typarams = List.nil();
/*  522 */       paramJCMethodDecl.params = translateVarDefs(paramJCMethodDecl.params);
/*  523 */       paramJCMethodDecl.recvparam = ((JCTree.JCVariableDecl)translate(paramJCMethodDecl.recvparam, null));
/*  524 */       paramJCMethodDecl.thrown = translate(paramJCMethodDecl.thrown, null);
/*  525 */       paramJCMethodDecl.body = ((JCTree.JCBlock)translate(paramJCMethodDecl.body, paramJCMethodDecl.sym.erasure(this.types).getReturnType()));
/*  526 */       paramJCMethodDecl.type = erasure(paramJCMethodDecl.type);
/*  527 */       this.result = paramJCMethodDecl;
/*      */ 
/*  529 */       this.currentMethod = localJCTree; } finally { this.currentMethod = localJCTree; }
/*      */ 
/*      */ 
/*  535 */     for (; localEntry.sym != null; 
/*  535 */       localEntry = localEntry.next())
/*  536 */       if ((localEntry.sym != paramJCMethodDecl.sym) && 
/*  537 */         (this.types
/*  537 */         .isSameType(erasure(localEntry.sym.type), 
/*  537 */         paramJCMethodDecl.type))) {
/*  538 */         this.log.error(paramJCMethodDecl.pos(), "name.clash.same.erasure", new Object[] { paramJCMethodDecl.sym, localEntry.sym });
/*      */ 
/*  541 */         return;
/*      */       }
/*      */   }
/*      */ 
/*      */   public void visitVarDef(JCTree.JCVariableDecl paramJCVariableDecl)
/*      */   {
/*  547 */     paramJCVariableDecl.vartype = ((JCTree.JCExpression)translate(paramJCVariableDecl.vartype, null));
/*  548 */     paramJCVariableDecl.init = ((JCTree.JCExpression)translate(paramJCVariableDecl.init, paramJCVariableDecl.sym.erasure(this.types)));
/*  549 */     paramJCVariableDecl.type = erasure(paramJCVariableDecl.type);
/*  550 */     this.result = paramJCVariableDecl;
/*      */   }
/*      */ 
/*      */   public void visitDoLoop(JCTree.JCDoWhileLoop paramJCDoWhileLoop) {
/*  554 */     paramJCDoWhileLoop.body = ((JCTree.JCStatement)translate(paramJCDoWhileLoop.body));
/*  555 */     paramJCDoWhileLoop.cond = ((JCTree.JCExpression)translate(paramJCDoWhileLoop.cond, this.syms.booleanType));
/*  556 */     this.result = paramJCDoWhileLoop;
/*      */   }
/*      */ 
/*      */   public void visitWhileLoop(JCTree.JCWhileLoop paramJCWhileLoop) {
/*  560 */     paramJCWhileLoop.cond = ((JCTree.JCExpression)translate(paramJCWhileLoop.cond, this.syms.booleanType));
/*  561 */     paramJCWhileLoop.body = ((JCTree.JCStatement)translate(paramJCWhileLoop.body));
/*  562 */     this.result = paramJCWhileLoop;
/*      */   }
/*      */ 
/*      */   public void visitForLoop(JCTree.JCForLoop paramJCForLoop) {
/*  566 */     paramJCForLoop.init = translate(paramJCForLoop.init, null);
/*  567 */     if (paramJCForLoop.cond != null)
/*  568 */       paramJCForLoop.cond = ((JCTree.JCExpression)translate(paramJCForLoop.cond, this.syms.booleanType));
/*  569 */     paramJCForLoop.step = translate(paramJCForLoop.step, null);
/*  570 */     paramJCForLoop.body = ((JCTree.JCStatement)translate(paramJCForLoop.body));
/*  571 */     this.result = paramJCForLoop;
/*      */   }
/*      */ 
/*      */   public void visitForeachLoop(JCTree.JCEnhancedForLoop paramJCEnhancedForLoop) {
/*  575 */     paramJCEnhancedForLoop.var = ((JCTree.JCVariableDecl)translate(paramJCEnhancedForLoop.var, null));
/*  576 */     Type localType = paramJCEnhancedForLoop.expr.type;
/*  577 */     paramJCEnhancedForLoop.expr = ((JCTree.JCExpression)translate(paramJCEnhancedForLoop.expr, erasure(paramJCEnhancedForLoop.expr.type)));
/*  578 */     if (this.types.elemtype(paramJCEnhancedForLoop.expr.type) == null)
/*  579 */       paramJCEnhancedForLoop.expr.type = localType;
/*  580 */     paramJCEnhancedForLoop.body = ((JCTree.JCStatement)translate(paramJCEnhancedForLoop.body));
/*  581 */     this.result = paramJCEnhancedForLoop;
/*      */   }
/*      */ 
/*      */   public void visitLambda(JCTree.JCLambda paramJCLambda) {
/*  585 */     JCTree localJCTree = this.currentMethod;
/*      */     try {
/*  587 */       this.currentMethod = null;
/*  588 */       paramJCLambda.params = translate(paramJCLambda.params);
/*  589 */       paramJCLambda.body = translate(paramJCLambda.body, paramJCLambda.body.type == null ? null : erasure(paramJCLambda.body.type));
/*  590 */       paramJCLambda.type = erasure(paramJCLambda.type);
/*  591 */       this.result = paramJCLambda;
/*      */ 
/*  594 */       this.currentMethod = localJCTree; } finally { this.currentMethod = localJCTree; }
/*      */   }
/*      */ 
/*      */   public void visitSwitch(JCTree.JCSwitch paramJCSwitch)
/*      */   {
/*  599 */     Type localType = this.types.supertype(paramJCSwitch.selector.type);
/*  600 */     int i = (localType != null) && (localType.tsym == this.syms.enumSym) ? 1 : 0;
/*      */ 
/*  602 */     Type.JCPrimitiveType localJCPrimitiveType = i != 0 ? erasure(paramJCSwitch.selector.type) : this.syms.intType;
/*  603 */     paramJCSwitch.selector = ((JCTree.JCExpression)translate(paramJCSwitch.selector, localJCPrimitiveType));
/*  604 */     paramJCSwitch.cases = translateCases(paramJCSwitch.cases);
/*  605 */     this.result = paramJCSwitch;
/*      */   }
/*      */ 
/*      */   public void visitCase(JCTree.JCCase paramJCCase) {
/*  609 */     paramJCCase.pat = ((JCTree.JCExpression)translate(paramJCCase.pat, null));
/*  610 */     paramJCCase.stats = translate(paramJCCase.stats);
/*  611 */     this.result = paramJCCase;
/*      */   }
/*      */ 
/*      */   public void visitSynchronized(JCTree.JCSynchronized paramJCSynchronized) {
/*  615 */     paramJCSynchronized.lock = ((JCTree.JCExpression)translate(paramJCSynchronized.lock, erasure(paramJCSynchronized.lock.type)));
/*  616 */     paramJCSynchronized.body = ((JCTree.JCBlock)translate(paramJCSynchronized.body));
/*  617 */     this.result = paramJCSynchronized;
/*      */   }
/*      */ 
/*      */   public void visitTry(JCTree.JCTry paramJCTry) {
/*  621 */     paramJCTry.resources = translate(paramJCTry.resources, this.syms.autoCloseableType);
/*  622 */     paramJCTry.body = ((JCTree.JCBlock)translate(paramJCTry.body));
/*  623 */     paramJCTry.catchers = translateCatchers(paramJCTry.catchers);
/*  624 */     paramJCTry.finalizer = ((JCTree.JCBlock)translate(paramJCTry.finalizer));
/*  625 */     this.result = paramJCTry;
/*      */   }
/*      */ 
/*      */   public void visitConditional(JCTree.JCConditional paramJCConditional) {
/*  629 */     paramJCConditional.cond = ((JCTree.JCExpression)translate(paramJCConditional.cond, this.syms.booleanType));
/*  630 */     paramJCConditional.truepart = ((JCTree.JCExpression)translate(paramJCConditional.truepart, erasure(paramJCConditional.type)));
/*  631 */     paramJCConditional.falsepart = ((JCTree.JCExpression)translate(paramJCConditional.falsepart, erasure(paramJCConditional.type)));
/*  632 */     paramJCConditional.type = erasure(paramJCConditional.type);
/*  633 */     this.result = retype(paramJCConditional, paramJCConditional.type, this.pt);
/*      */   }
/*      */ 
/*      */   public void visitIf(JCTree.JCIf paramJCIf) {
/*  637 */     paramJCIf.cond = ((JCTree.JCExpression)translate(paramJCIf.cond, this.syms.booleanType));
/*  638 */     paramJCIf.thenpart = ((JCTree.JCStatement)translate(paramJCIf.thenpart));
/*  639 */     paramJCIf.elsepart = ((JCTree.JCStatement)translate(paramJCIf.elsepart));
/*  640 */     this.result = paramJCIf;
/*      */   }
/*      */ 
/*      */   public void visitExec(JCTree.JCExpressionStatement paramJCExpressionStatement) {
/*  644 */     paramJCExpressionStatement.expr = ((JCTree.JCExpression)translate(paramJCExpressionStatement.expr, null));
/*  645 */     this.result = paramJCExpressionStatement;
/*      */   }
/*      */ 
/*      */   public void visitReturn(JCTree.JCReturn paramJCReturn) {
/*  649 */     paramJCReturn.expr = ((JCTree.JCExpression)translate(paramJCReturn.expr, this.currentMethod != null ? this.types.erasure(this.currentMethod.type).getReturnType() : null));
/*  650 */     this.result = paramJCReturn;
/*      */   }
/*      */ 
/*      */   public void visitThrow(JCTree.JCThrow paramJCThrow) {
/*  654 */     paramJCThrow.expr = ((JCTree.JCExpression)translate(paramJCThrow.expr, erasure(paramJCThrow.expr.type)));
/*  655 */     this.result = paramJCThrow;
/*      */   }
/*      */ 
/*      */   public void visitAssert(JCTree.JCAssert paramJCAssert) {
/*  659 */     paramJCAssert.cond = ((JCTree.JCExpression)translate(paramJCAssert.cond, this.syms.booleanType));
/*  660 */     if (paramJCAssert.detail != null)
/*  661 */       paramJCAssert.detail = ((JCTree.JCExpression)translate(paramJCAssert.detail, erasure(paramJCAssert.detail.type)));
/*  662 */     this.result = paramJCAssert;
/*      */   }
/*      */ 
/*      */   public void visitApply(JCTree.JCMethodInvocation paramJCMethodInvocation) {
/*  666 */     paramJCMethodInvocation.meth = ((JCTree.JCExpression)translate(paramJCMethodInvocation.meth, null));
/*  667 */     Symbol localSymbol = TreeInfo.symbol(paramJCMethodInvocation.meth);
/*  668 */     Type localType = localSymbol.erasure(this.types);
/*  669 */     List localList = localType.getParameterTypes();
/*  670 */     if ((this.allowEnums) && (localSymbol.name == this.names.init) && (localSymbol.owner == this.syms.enumSym))
/*      */     {
/*  673 */       localList = localList.tail.tail;
/*  674 */     }if (paramJCMethodInvocation.varargsElement != null) {
/*  675 */       paramJCMethodInvocation.varargsElement = this.types.erasure(paramJCMethodInvocation.varargsElement);
/*      */     }
/*  677 */     else if (paramJCMethodInvocation.args.length() != localList.length()) {
/*  678 */       this.log.error(paramJCMethodInvocation.pos(), "method.invoked.with.incorrect.number.arguments", new Object[] { 
/*  680 */         Integer.valueOf(paramJCMethodInvocation.args
/*  680 */         .length()), Integer.valueOf(localList.length()) });
/*      */     }
/*  682 */     paramJCMethodInvocation.args = translateArgs(paramJCMethodInvocation.args, localList, paramJCMethodInvocation.varargsElement);
/*      */ 
/*  684 */     paramJCMethodInvocation.type = this.types.erasure(paramJCMethodInvocation.type);
/*      */ 
/*  686 */     this.result = retype(paramJCMethodInvocation, localType.getReturnType(), this.pt);
/*      */   }
/*      */ 
/*      */   public void visitNewClass(JCTree.JCNewClass paramJCNewClass) {
/*  690 */     if (paramJCNewClass.encl != null)
/*  691 */       paramJCNewClass.encl = ((JCTree.JCExpression)translate(paramJCNewClass.encl, erasure(paramJCNewClass.encl.type)));
/*  692 */     paramJCNewClass.clazz = ((JCTree.JCExpression)translate(paramJCNewClass.clazz, null));
/*  693 */     if (paramJCNewClass.varargsElement != null)
/*  694 */       paramJCNewClass.varargsElement = this.types.erasure(paramJCNewClass.varargsElement);
/*  695 */     paramJCNewClass.args = translateArgs(paramJCNewClass.args, paramJCNewClass.constructor
/*  696 */       .erasure(this.types)
/*  696 */       .getParameterTypes(), paramJCNewClass.varargsElement);
/*  697 */     paramJCNewClass.def = ((JCTree.JCClassDecl)translate(paramJCNewClass.def, null));
/*  698 */     if (paramJCNewClass.constructorType != null)
/*  699 */       paramJCNewClass.constructorType = erasure(paramJCNewClass.constructorType);
/*  700 */     paramJCNewClass.type = erasure(paramJCNewClass.type);
/*  701 */     this.result = paramJCNewClass;
/*      */   }
/*      */ 
/*      */   public void visitNewArray(JCTree.JCNewArray paramJCNewArray) {
/*  705 */     paramJCNewArray.elemtype = ((JCTree.JCExpression)translate(paramJCNewArray.elemtype, null));
/*  706 */     translate(paramJCNewArray.dims, this.syms.intType);
/*  707 */     if (paramJCNewArray.type != null) {
/*  708 */       paramJCNewArray.elems = translate(paramJCNewArray.elems, erasure(this.types.elemtype(paramJCNewArray.type)));
/*  709 */       paramJCNewArray.type = erasure(paramJCNewArray.type);
/*      */     } else {
/*  711 */       paramJCNewArray.elems = translate(paramJCNewArray.elems, null);
/*      */     }
/*      */ 
/*  714 */     this.result = paramJCNewArray;
/*      */   }
/*      */ 
/*      */   public void visitParens(JCTree.JCParens paramJCParens) {
/*  718 */     paramJCParens.expr = ((JCTree.JCExpression)translate(paramJCParens.expr, this.pt));
/*  719 */     paramJCParens.type = erasure(paramJCParens.type);
/*  720 */     this.result = paramJCParens;
/*      */   }
/*      */ 
/*      */   public void visitAssign(JCTree.JCAssign paramJCAssign) {
/*  724 */     paramJCAssign.lhs = ((JCTree.JCExpression)translate(paramJCAssign.lhs, null));
/*  725 */     paramJCAssign.rhs = ((JCTree.JCExpression)translate(paramJCAssign.rhs, erasure(paramJCAssign.lhs.type)));
/*  726 */     paramJCAssign.type = erasure(paramJCAssign.lhs.type);
/*  727 */     this.result = retype(paramJCAssign, paramJCAssign.type, this.pt);
/*      */   }
/*      */ 
/*      */   public void visitAssignop(JCTree.JCAssignOp paramJCAssignOp) {
/*  731 */     paramJCAssignOp.lhs = ((JCTree.JCExpression)translate(paramJCAssignOp.lhs, null));
/*  732 */     paramJCAssignOp.rhs = ((JCTree.JCExpression)translate(paramJCAssignOp.rhs, (Type)paramJCAssignOp.operator.type.getParameterTypes().tail.head));
/*  733 */     paramJCAssignOp.type = erasure(paramJCAssignOp.type);
/*  734 */     this.result = paramJCAssignOp;
/*      */   }
/*      */ 
/*      */   public void visitUnary(JCTree.JCUnary paramJCUnary) {
/*  738 */     paramJCUnary.arg = ((JCTree.JCExpression)translate(paramJCUnary.arg, (Type)paramJCUnary.operator.type.getParameterTypes().head));
/*  739 */     this.result = paramJCUnary;
/*      */   }
/*      */ 
/*      */   public void visitBinary(JCTree.JCBinary paramJCBinary) {
/*  743 */     paramJCBinary.lhs = ((JCTree.JCExpression)translate(paramJCBinary.lhs, (Type)paramJCBinary.operator.type.getParameterTypes().head));
/*  744 */     paramJCBinary.rhs = ((JCTree.JCExpression)translate(paramJCBinary.rhs, (Type)paramJCBinary.operator.type.getParameterTypes().tail.head));
/*  745 */     this.result = paramJCBinary;
/*      */   }
/*      */ 
/*      */   public void visitTypeCast(JCTree.JCTypeCast paramJCTypeCast) {
/*  749 */     paramJCTypeCast.clazz = translate(paramJCTypeCast.clazz, null);
/*  750 */     Type localType1 = paramJCTypeCast.type;
/*  751 */     paramJCTypeCast.type = erasure(paramJCTypeCast.type);
/*  752 */     paramJCTypeCast.expr = ((JCTree.JCExpression)translate(paramJCTypeCast.expr, paramJCTypeCast.type));
/*  753 */     if (localType1.isIntersection()) {
/*  754 */       Type.IntersectionClassType localIntersectionClassType = (Type.IntersectionClassType)localType1;
/*  755 */       for (Type localType2 : localIntersectionClassType.getExplicitComponents()) {
/*  756 */         Type localType3 = erasure(localType2);
/*  757 */         if (!this.types.isSameType(localType3, paramJCTypeCast.type)) {
/*  758 */           paramJCTypeCast.expr = coerce(paramJCTypeCast.expr, localType3);
/*      */         }
/*      */       }
/*      */     }
/*  762 */     this.result = paramJCTypeCast;
/*      */   }
/*      */ 
/*      */   public void visitTypeTest(JCTree.JCInstanceOf paramJCInstanceOf) {
/*  766 */     paramJCInstanceOf.expr = ((JCTree.JCExpression)translate(paramJCInstanceOf.expr, null));
/*  767 */     paramJCInstanceOf.clazz = translate(paramJCInstanceOf.clazz, null);
/*  768 */     this.result = paramJCInstanceOf;
/*      */   }
/*      */ 
/*      */   public void visitIndexed(JCTree.JCArrayAccess paramJCArrayAccess) {
/*  772 */     paramJCArrayAccess.indexed = ((JCTree.JCExpression)translate(paramJCArrayAccess.indexed, erasure(paramJCArrayAccess.indexed.type)));
/*  773 */     paramJCArrayAccess.index = ((JCTree.JCExpression)translate(paramJCArrayAccess.index, this.syms.intType));
/*      */ 
/*  776 */     this.result = retype(paramJCArrayAccess, this.types.elemtype(paramJCArrayAccess.indexed.type), this.pt);
/*      */   }
/*      */ 
/*      */   public void visitAnnotation(JCTree.JCAnnotation paramJCAnnotation)
/*      */   {
/*  782 */     this.result = paramJCAnnotation;
/*      */   }
/*      */ 
/*      */   public void visitIdent(JCTree.JCIdent paramJCIdent) {
/*  786 */     Type localType = paramJCIdent.sym.erasure(this.types);
/*      */ 
/*  789 */     if ((paramJCIdent.sym.kind == 2) && (paramJCIdent.sym.type.hasTag(TypeTag.TYPEVAR))) {
/*  790 */       this.result = this.make.at(paramJCIdent.pos).Type(localType);
/*      */     }
/*  793 */     else if (paramJCIdent.type.constValue() != null) {
/*  794 */       this.result = paramJCIdent;
/*      */     }
/*  797 */     else if (paramJCIdent.sym.kind == 4) {
/*  798 */       this.result = retype(paramJCIdent, localType, this.pt);
/*      */     }
/*      */     else {
/*  801 */       paramJCIdent.type = erasure(paramJCIdent.type);
/*  802 */       this.result = paramJCIdent;
/*      */     }
/*      */   }
/*      */ 
/*      */   public void visitSelect(JCTree.JCFieldAccess paramJCFieldAccess) {
/*  807 */     Type localType = paramJCFieldAccess.selected.type;
/*  808 */     while (localType.hasTag(TypeTag.TYPEVAR))
/*  809 */       localType = localType.getUpperBound();
/*  810 */     if (localType.isCompound()) {
/*  811 */       if ((paramJCFieldAccess.sym.flags() & 0x200000) != 0L) {
/*  812 */         paramJCFieldAccess.sym = ((Symbol.MethodSymbol)paramJCFieldAccess.sym)
/*  813 */           .implemented((Symbol.TypeSymbol)paramJCFieldAccess.sym.owner, this.types);
/*      */       }
/*      */ 
/*  815 */       paramJCFieldAccess.selected = coerce(
/*  816 */         (JCTree.JCExpression)translate(paramJCFieldAccess.selected, 
/*  816 */         erasure(paramJCFieldAccess.selected.type)), 
/*  817 */         erasure(paramJCFieldAccess.sym.owner.type));
/*      */     }
/*      */     else {
/*  819 */       paramJCFieldAccess.selected = ((JCTree.JCExpression)translate(paramJCFieldAccess.selected, erasure(localType)));
/*      */     }
/*      */ 
/*  822 */     if (paramJCFieldAccess.type.constValue() != null) {
/*  823 */       this.result = paramJCFieldAccess;
/*      */     }
/*  826 */     else if (paramJCFieldAccess.sym.kind == 4) {
/*  827 */       this.result = retype(paramJCFieldAccess, paramJCFieldAccess.sym.erasure(this.types), this.pt);
/*      */     }
/*      */     else {
/*  830 */       paramJCFieldAccess.type = erasure(paramJCFieldAccess.type);
/*  831 */       this.result = paramJCFieldAccess;
/*      */     }
/*      */   }
/*      */ 
/*      */   public void visitReference(JCTree.JCMemberReference paramJCMemberReference) {
/*  836 */     paramJCMemberReference.expr = ((JCTree.JCExpression)translate(paramJCMemberReference.expr, erasure(paramJCMemberReference.expr.type)));
/*  837 */     paramJCMemberReference.type = erasure(paramJCMemberReference.type);
/*  838 */     if (paramJCMemberReference.varargsElement != null)
/*  839 */       paramJCMemberReference.varargsElement = erasure(paramJCMemberReference.varargsElement);
/*  840 */     this.result = paramJCMemberReference;
/*      */   }
/*      */ 
/*      */   public void visitTypeArray(JCTree.JCArrayTypeTree paramJCArrayTypeTree) {
/*  844 */     paramJCArrayTypeTree.elemtype = ((JCTree.JCExpression)translate(paramJCArrayTypeTree.elemtype, null));
/*  845 */     paramJCArrayTypeTree.type = erasure(paramJCArrayTypeTree.type);
/*  846 */     this.result = paramJCArrayTypeTree;
/*      */   }
/*      */ 
/*      */   public void visitTypeApply(JCTree.JCTypeApply paramJCTypeApply)
/*      */   {
/*  852 */     JCTree localJCTree = translate(paramJCTypeApply.clazz, null);
/*  853 */     this.result = localJCTree;
/*      */   }
/*      */ 
/*      */   public void visitTypeIntersection(JCTree.JCTypeIntersection paramJCTypeIntersection) {
/*  857 */     paramJCTypeIntersection.bounds = translate(paramJCTypeIntersection.bounds, null);
/*  858 */     paramJCTypeIntersection.type = erasure(paramJCTypeIntersection.type);
/*  859 */     this.result = paramJCTypeIntersection;
/*      */   }
/*      */ 
/*      */   private Type erasure(Type paramType)
/*      */   {
/*  867 */     return this.types.erasure(paramType);
/*      */   }
/*      */ 
/*      */   private boolean boundsRestricted(Symbol.ClassSymbol paramClassSymbol) {
/*  871 */     Type localType1 = this.types.supertype(paramClassSymbol.type);
/*  872 */     if (localType1.isParameterized()) {
/*  873 */       List localList1 = localType1.allparams();
/*  874 */       List localList2 = localType1.tsym.type.allparams();
/*  875 */       while ((!localList1.isEmpty()) && (!localList2.isEmpty())) {
/*  876 */         Type localType2 = (Type)localList1.head;
/*  877 */         Type localType3 = (Type)localList2.head;
/*      */ 
/*  879 */         if (!this.types.isSameType(this.types.erasure(localType2), this.types
/*  880 */           .erasure(localType3)))
/*      */         {
/*  881 */           return true;
/*      */         }
/*  883 */         localList1 = localList1.tail;
/*  884 */         localList2 = localList2.tail;
/*      */       }
/*      */     }
/*  887 */     return false;
/*      */   }
/*      */ 
/*      */   private List<JCTree> addOverrideBridgesIfNeeded(JCDiagnostic.DiagnosticPosition paramDiagnosticPosition, Symbol.ClassSymbol paramClassSymbol)
/*      */   {
/*  892 */     ListBuffer localListBuffer = new ListBuffer();
/*  893 */     if ((paramClassSymbol.isInterface()) || (!boundsRestricted(paramClassSymbol)))
/*  894 */       return localListBuffer.toList();
/*  895 */     Type localType = this.types.supertype(paramClassSymbol.type);
/*  896 */     Scope localScope = localType.tsym.members();
/*  897 */     if (localScope.elems != null) {
/*  898 */       for (Symbol localSymbol : localScope.getElements(new NeedsOverridBridgeFilter(paramClassSymbol)))
/*      */       {
/*  900 */         Symbol.MethodSymbol localMethodSymbol1 = (Symbol.MethodSymbol)localSymbol;
/*  901 */         Symbol.MethodSymbol localMethodSymbol2 = (Symbol.MethodSymbol)localMethodSymbol1.asMemberOf(paramClassSymbol.type, this.types);
/*  902 */         Symbol.MethodSymbol localMethodSymbol3 = localMethodSymbol1.implementation(paramClassSymbol, this.types, false);
/*      */ 
/*  904 */         if (((localMethodSymbol3 == null) || (localMethodSymbol3.owner != paramClassSymbol)) && 
/*  905 */           (!this.types
/*  905 */           .isSameType(localMethodSymbol2
/*  905 */           .erasure(this.types), 
/*  905 */           localMethodSymbol1.erasure(this.types)))) {
/*  906 */           addOverrideBridges(paramDiagnosticPosition, localMethodSymbol1, localMethodSymbol2, paramClassSymbol, localListBuffer);
/*      */         }
/*      */       }
/*      */     }
/*  910 */     return localListBuffer.toList();
/*      */   }
/*      */ 
/*      */   private void addOverrideBridges(JCDiagnostic.DiagnosticPosition paramDiagnosticPosition, Symbol.MethodSymbol paramMethodSymbol1, Symbol.MethodSymbol paramMethodSymbol2, Symbol.ClassSymbol paramClassSymbol, ListBuffer<JCTree> paramListBuffer)
/*      */   {
/*  934 */     Type localType1 = paramMethodSymbol1.erasure(this.types);
/*  935 */     long l = paramMethodSymbol1.flags() & 0x7 | 0x1000 | 0x80000000 | 0x0;
/*  936 */     paramMethodSymbol2 = new Symbol.MethodSymbol(l, paramMethodSymbol2.name, paramMethodSymbol2.type, paramClassSymbol);
/*  937 */     JCTree.JCMethodDecl localJCMethodDecl = this.make.MethodDef(paramMethodSymbol2, null);
/*  938 */     JCTree.JCIdent localJCIdent = this.make.Super(this.types.supertype(paramClassSymbol.type).tsym.erasure(this.types), paramClassSymbol);
/*  939 */     Type localType2 = erasure(paramMethodSymbol1.type.getReturnType());
/*      */ 
/*  945 */     JCTree.JCMethodInvocation localJCMethodInvocation = this.make
/*  941 */       .Apply(null, this.make
/*  942 */       .Select(localJCIdent, paramMethodSymbol1)
/*  942 */       .setType(localType2), 
/*  943 */       translateArgs(this.make
/*  943 */       .Idents(localJCMethodDecl.params), 
/*  943 */       localType1
/*  944 */       .getParameterTypes(), null))
/*  945 */       .setType(localType2);
/*      */ 
/*  948 */     JCTree.JCReturn localJCReturn = paramMethodSymbol2.getReturnType().hasTag(TypeTag.VOID) ? this.make
/*  947 */       .Exec(localJCMethodInvocation) : 
/*  947 */       this.make
/*  948 */       .Return(coerce(localJCMethodInvocation, paramMethodSymbol2
/*  948 */       .erasure(this.types)
/*  948 */       .getReturnType()));
/*  949 */     localJCMethodDecl.body = this.make.Block(0L, List.of(localJCReturn));
/*  950 */     paramClassSymbol.members().enter(paramMethodSymbol2);
/*  951 */     paramListBuffer.append(localJCMethodDecl);
/*      */   }
/*      */ 
/*      */   void translateClass(Symbol.ClassSymbol paramClassSymbol)
/*      */   {
/*  964 */     Type localType1 = this.types.supertype(paramClassSymbol.type);
/*      */ 
/*  966 */     if (localType1.hasTag(TypeTag.CLASS)) {
/*  967 */       translateClass((Symbol.ClassSymbol)localType1.tsym);
/*      */     }
/*      */ 
/*  970 */     Env localEnv1 = this.enter.getEnv(paramClassSymbol);
/*  971 */     if ((localEnv1 == null) || ((paramClassSymbol.flags_field & 0x0) != 0L)) {
/*  972 */       return;
/*      */     }
/*  974 */     paramClassSymbol.flags_field |= 1125899906842624L;
/*      */ 
/*  984 */     int i = this.compileStates.get(localEnv1) != null ? 1 : 0;
/*  985 */     if ((i == 0) && (paramClassSymbol.outermostClass() == paramClassSymbol)) {
/*  986 */       Assert.error("No info for outermost class: " + localEnv1.enclClass.sym);
/*      */     }
/*      */ 
/*  989 */     if ((i != 0) && 
/*  990 */       (CompileStates.CompileState.FLOW
/*  990 */       .isAfter((CompileStates.CompileState)this.compileStates
/*  990 */       .get(localEnv1))))
/*      */     {
/*  991 */       Assert.error(String.format("The current compile state [%s] of class %s is previous to FLOW", new Object[] { this.compileStates
/*  992 */         .get(localEnv1), 
/*  992 */         localEnv1.enclClass.sym }));
/*      */     }
/*      */ 
/*  995 */     Env localEnv2 = this.env;
/*      */     try {
/*  997 */       this.env = localEnv1;
/*      */ 
/* 1000 */       TreeMaker localTreeMaker = this.make;
/* 1001 */       Type localType2 = this.pt;
/* 1002 */       this.make = this.make.forToplevel(this.env.toplevel);
/* 1003 */       this.pt = null;
/*      */       try {
/* 1005 */         JCTree.JCClassDecl localJCClassDecl = (JCTree.JCClassDecl)this.env.tree;
/* 1006 */         localJCClassDecl.typarams = List.nil();
/* 1007 */         super.visitClassDef(localJCClassDecl);
/* 1008 */         this.make.at(localJCClassDecl.pos);
/* 1009 */         if (this.addBridges) {
/* 1010 */           ListBuffer localListBuffer = new ListBuffer();
/*      */ 
/* 1013 */           if ((this.allowInterfaceBridges) || ((localJCClassDecl.sym.flags() & 0x200) == 0L)) {
/* 1014 */             addBridges(localJCClassDecl.pos(), paramClassSymbol, localListBuffer);
/*      */           }
/* 1016 */           localJCClassDecl.defs = localListBuffer.toList().prependList(localJCClassDecl.defs);
/*      */         }
/* 1018 */         localJCClassDecl.type = erasure(localJCClassDecl.type);
/*      */       } finally {
/* 1020 */         this.make = localTreeMaker;
/*      */       }
/*      */     }
/*      */     finally {
/* 1024 */       this.env = localEnv2;
/*      */     }
/*      */   }
/*      */ 
/*      */   public JCTree translateTopLevelClass(JCTree paramJCTree, TreeMaker paramTreeMaker)
/*      */   {
/* 1033 */     this.make = paramTreeMaker;
/* 1034 */     this.pt = null;
/* 1035 */     return translate(paramJCTree, null);
/*      */   }
/*      */ 
/*      */   class NeedsOverridBridgeFilter
/*      */     implements Filter<Symbol>
/*      */   {
/*      */     Symbol.ClassSymbol c;
/*      */ 
/*      */     NeedsOverridBridgeFilter(Symbol.ClassSymbol arg2)
/*      */     {
/*      */       Object localObject;
/*  918 */       this.c = localObject;
/*      */     }
/*      */ 
/*      */     public boolean accepts(Symbol paramSymbol)
/*      */     {
/*  925 */       return (paramSymbol.kind == 16) && 
/*  922 */         (!paramSymbol
/*  922 */         .isConstructor()) && 
/*  923 */         (paramSymbol
/*  923 */         .isInheritedIn(this.c, TransTypes.this.types)) && 
/*  924 */         ((paramSymbol
/*  924 */         .flags() & 0x10) == 0L) && 
/*  925 */         ((paramSymbol
/*  925 */         .flags() & 0x1000) != 4096L);
/*      */     }
/*      */   }
/*      */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.javac.comp.TransTypes
 * JD-Core Version:    0.6.2
 */