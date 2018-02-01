/*      */ package com.sun.tools.javac.comp;
/*      */ 
/*      */ import com.sun.source.tree.IdentifierTree;
/*      */ import com.sun.source.tree.LambdaExpressionTree.BodyKind;
/*      */ import com.sun.source.tree.MemberReferenceTree.ReferenceMode;
/*      */ import com.sun.source.tree.MemberSelectTree;
/*      */ import com.sun.source.tree.Tree.Kind;
/*      */ import com.sun.source.tree.TreeVisitor;
/*      */ import com.sun.source.util.SimpleTreeVisitor;
/*      */ import com.sun.tools.javac.code.Attribute.Compound;
/*      */ import com.sun.tools.javac.code.Attribute.TypeCompound;
/*      */ import com.sun.tools.javac.code.BoundKind;
/*      */ import com.sun.tools.javac.code.DeferredLintHandler;
/*      */ import com.sun.tools.javac.code.Flags;
/*      */ import com.sun.tools.javac.code.Kinds;
/*      */ import com.sun.tools.javac.code.Lint;
/*      */ import com.sun.tools.javac.code.Lint.LintCategory;
/*      */ import com.sun.tools.javac.code.Scope;
/*      */ import com.sun.tools.javac.code.Scope.Entry;
/*      */ import com.sun.tools.javac.code.Source;
/*      */ import com.sun.tools.javac.code.Symbol;
/*      */ import com.sun.tools.javac.code.Symbol.ClassSymbol;
/*      */ import com.sun.tools.javac.code.Symbol.CompletionFailure;
/*      */ import com.sun.tools.javac.code.Symbol.MethodSymbol;
/*      */ import com.sun.tools.javac.code.Symbol.OperatorSymbol;
/*      */ import com.sun.tools.javac.code.Symbol.PackageSymbol;
/*      */ import com.sun.tools.javac.code.Symbol.TypeSymbol;
/*      */ import com.sun.tools.javac.code.Symbol.VarSymbol;
/*      */ import com.sun.tools.javac.code.Symtab;
/*      */ import com.sun.tools.javac.code.Type;
/*      */ import com.sun.tools.javac.code.Type.ArrayType;
/*      */ import com.sun.tools.javac.code.Type.ClassType;
/*      */ import com.sun.tools.javac.code.Type.ForAll;
/*      */ import com.sun.tools.javac.code.Type.IntersectionClassType;
/*      */ import com.sun.tools.javac.code.Type.Mapping;
/*      */ import com.sun.tools.javac.code.Type.MethodType;
/*      */ import com.sun.tools.javac.code.Type.TypeVar;
/*      */ import com.sun.tools.javac.code.Type.UnionClassType;
/*      */ import com.sun.tools.javac.code.Type.WildcardType;
/*      */ import com.sun.tools.javac.code.TypeAnnotations;
/*      */ import com.sun.tools.javac.code.TypeAnnotations.AnnotationType;
/*      */ import com.sun.tools.javac.code.TypeTag;
/*      */ import com.sun.tools.javac.code.Types;
/*      */ import com.sun.tools.javac.code.Types.FunctionDescriptorLookupError;
/*      */ import com.sun.tools.javac.code.Types.MapVisitor;
/*      */ import com.sun.tools.javac.jvm.Target;
/*      */ import com.sun.tools.javac.tree.EndPosTable;
/*      */ import com.sun.tools.javac.tree.JCTree;
/*      */ import com.sun.tools.javac.tree.JCTree.JCAnnotatedType;
/*      */ import com.sun.tools.javac.tree.JCTree.JCAnnotation;
/*      */ import com.sun.tools.javac.tree.JCTree.JCArrayAccess;
/*      */ import com.sun.tools.javac.tree.JCTree.JCArrayTypeTree;
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
/*      */ import com.sun.tools.javac.tree.JCTree.JCErroneous;
/*      */ import com.sun.tools.javac.tree.JCTree.JCExpression;
/*      */ import com.sun.tools.javac.tree.JCTree.JCExpressionStatement;
/*      */ import com.sun.tools.javac.tree.JCTree.JCFieldAccess;
/*      */ import com.sun.tools.javac.tree.JCTree.JCForLoop;
/*      */ import com.sun.tools.javac.tree.JCTree.JCFunctionalExpression;
/*      */ import com.sun.tools.javac.tree.JCTree.JCIdent;
/*      */ import com.sun.tools.javac.tree.JCTree.JCIf;
/*      */ import com.sun.tools.javac.tree.JCTree.JCImport;
/*      */ import com.sun.tools.javac.tree.JCTree.JCInstanceOf;
/*      */ import com.sun.tools.javac.tree.JCTree.JCLabeledStatement;
/*      */ import com.sun.tools.javac.tree.JCTree.JCLambda;
/*      */ import com.sun.tools.javac.tree.JCTree.JCLambda.ParameterKind;
/*      */ import com.sun.tools.javac.tree.JCTree.JCLiteral;
/*      */ import com.sun.tools.javac.tree.JCTree.JCMemberReference;
/*      */ import com.sun.tools.javac.tree.JCTree.JCMemberReference.ReferenceKind;
/*      */ import com.sun.tools.javac.tree.JCTree.JCMethodDecl;
/*      */ import com.sun.tools.javac.tree.JCTree.JCMethodInvocation;
/*      */ import com.sun.tools.javac.tree.JCTree.JCModifiers;
/*      */ import com.sun.tools.javac.tree.JCTree.JCNewArray;
/*      */ import com.sun.tools.javac.tree.JCTree.JCNewClass;
/*      */ import com.sun.tools.javac.tree.JCTree.JCParens;
/*      */ import com.sun.tools.javac.tree.JCTree.JCPolyExpression.PolyKind;
/*      */ import com.sun.tools.javac.tree.JCTree.JCPrimitiveTypeTree;
/*      */ import com.sun.tools.javac.tree.JCTree.JCReturn;
/*      */ import com.sun.tools.javac.tree.JCTree.JCSkip;
/*      */ import com.sun.tools.javac.tree.JCTree.JCStatement;
/*      */ import com.sun.tools.javac.tree.JCTree.JCSwitch;
/*      */ import com.sun.tools.javac.tree.JCTree.JCSynchronized;
/*      */ import com.sun.tools.javac.tree.JCTree.JCThrow;
/*      */ import com.sun.tools.javac.tree.JCTree.JCTry;
/*      */ import com.sun.tools.javac.tree.JCTree.JCTypeApply;
/*      */ import com.sun.tools.javac.tree.JCTree.JCTypeCast;
/*      */ import com.sun.tools.javac.tree.JCTree.JCTypeIntersection;
/*      */ import com.sun.tools.javac.tree.JCTree.JCTypeParameter;
/*      */ import com.sun.tools.javac.tree.JCTree.JCTypeUnion;
/*      */ import com.sun.tools.javac.tree.JCTree.JCUnary;
/*      */ import com.sun.tools.javac.tree.JCTree.JCVariableDecl;
/*      */ import com.sun.tools.javac.tree.JCTree.JCWhileLoop;
/*      */ import com.sun.tools.javac.tree.JCTree.JCWildcard;
/*      */ import com.sun.tools.javac.tree.JCTree.Tag;
/*      */ import com.sun.tools.javac.tree.JCTree.TypeBoundKind;
/*      */ import com.sun.tools.javac.tree.JCTree.Visitor;
/*      */ import com.sun.tools.javac.tree.TreeInfo;
/*      */ import com.sun.tools.javac.tree.TreeMaker;
/*      */ import com.sun.tools.javac.tree.TreeScanner;
/*      */ import com.sun.tools.javac.tree.TreeTranslator;
/*      */ import com.sun.tools.javac.util.Assert;
/*      */ import com.sun.tools.javac.util.Context;
/*      */ import com.sun.tools.javac.util.Context.Key;
/*      */ import com.sun.tools.javac.util.Filter;
/*      */ import com.sun.tools.javac.util.JCDiagnostic;
/*      */ import com.sun.tools.javac.util.JCDiagnostic.DiagnosticPosition;
/*      */ import com.sun.tools.javac.util.JCDiagnostic.DiagnosticType;
/*      */ import com.sun.tools.javac.util.JCDiagnostic.Factory;
/*      */ import com.sun.tools.javac.util.List;
/*      */ import com.sun.tools.javac.util.ListBuffer;
/*      */ import com.sun.tools.javac.util.Log;
/*      */ import com.sun.tools.javac.util.Log.DiscardDiagnosticHandler;
/*      */ import com.sun.tools.javac.util.Name;
/*      */ import com.sun.tools.javac.util.Names;
/*      */ import com.sun.tools.javac.util.Options;
/*      */ import com.sun.tools.javac.util.Pair;
/*      */ import com.sun.tools.javac.util.Warner;
/*      */ import java.util.HashMap;
/*      */ import java.util.HashSet;
/*      */ import java.util.Iterator;
/*      */ import java.util.Map;
/*      */ import java.util.Set;
/*      */ import javax.lang.model.element.ElementKind;
/*      */ import javax.tools.JavaFileObject;
/*      */ 
/*      */ public class Attr extends JCTree.Visitor
/*      */ {
/*   76 */   protected static final Context.Key<Attr> attrKey = new Context.Key();
/*      */   final Names names;
/*      */   final Log log;
/*      */   final Symtab syms;
/*      */   final Resolve rs;
/*      */   final Infer infer;
/*      */   final DeferredAttr deferredAttr;
/*      */   final Check chk;
/*      */   final Flow flow;
/*      */   final MemberEnter memberEnter;
/*      */   final TreeMaker make;
/*      */   final ConstFold cfolder;
/*      */   final Enter enter;
/*      */   final Target target;
/*      */   final Types types;
/*      */   final JCDiagnostic.Factory diags;
/*      */   final Annotate annotate;
/*      */   final TypeAnnotations typeAnnotations;
/*      */   final DeferredLintHandler deferredLintHandler;
/*      */   final TypeEnvs typeEnvs;
/*      */   boolean relax;
/*      */   boolean allowPoly;
/*      */   boolean allowTypeAnnos;
/*      */   boolean allowGenerics;
/*      */   boolean allowVarargs;
/*      */   boolean allowEnums;
/*      */   boolean allowBoxing;
/*      */   boolean allowCovariantReturns;
/*      */   boolean allowLambda;
/*      */   boolean allowDefaultMethods;
/*      */   boolean allowStaticInterfaceMethods;
/*      */   boolean allowAnonOuterThis;
/*      */   boolean findDiamonds;
/*      */   static final boolean allowDiamondFinder = true;
/*      */   boolean useBeforeDeclarationWarning;
/*      */   boolean identifyLambdaCandidate;
/*      */   boolean allowStringsInSwitch;
/*      */   String sourceName;
/*  368 */   private TreeVisitor<Symbol, Env<AttrContext>> identAttributer = new IdentAttributer(null);
/*      */ 
/*  450 */   private JCTree breakTree = null;
/*      */   final ResultInfo statInfo;
/*      */   final ResultInfo varInfo;
/*      */   final ResultInfo unknownAnyPolyInfo;
/*      */   final ResultInfo unknownExprInfo;
/*      */   final ResultInfo unknownTypeInfo;
/*      */   final ResultInfo unknownTypeExprInfo;
/*      */   final ResultInfo recoveryInfo;
/*      */   Env<AttrContext> env;
/*      */   ResultInfo resultInfo;
/*      */   Type result;
/* 1478 */   TreeTranslator removeClassParams = new TreeTranslator()
/*      */   {
/*      */     public void visitTypeApply(JCTree.JCTypeApply paramAnonymousJCTypeApply) {
/* 1481 */       this.result = translate(paramAnonymousJCTypeApply.clazz);
/*      */     }
/* 1478 */   };
/*      */ 
/* 1559 */   static final TypeTag[] primitiveTags = { TypeTag.BYTE, TypeTag.CHAR, TypeTag.SHORT, TypeTag.INT, TypeTag.LONG, TypeTag.FLOAT, TypeTag.DOUBLE, TypeTag.BOOLEAN };
/*      */ 
/* 2433 */   Types.MapVisitor<JCDiagnostic.DiagnosticPosition> targetChecker = new Types.MapVisitor()
/*      */   {
/*      */     public Type visitClassType(Type.ClassType paramAnonymousClassType, JCDiagnostic.DiagnosticPosition paramAnonymousDiagnosticPosition)
/*      */     {
/* 2438 */       return paramAnonymousClassType.isIntersection() ? 
/* 2438 */         visitIntersectionClassType((Type.IntersectionClassType)paramAnonymousClassType, paramAnonymousDiagnosticPosition) : 
/* 2438 */         paramAnonymousClassType;
/*      */     }
/*      */ 
/*      */     public Type visitIntersectionClassType(Type.IntersectionClassType paramAnonymousIntersectionClassType, JCDiagnostic.DiagnosticPosition paramAnonymousDiagnosticPosition) {
/* 2442 */       Symbol localSymbol = Attr.this.types.findDescriptorSymbol(makeNotionalInterface(paramAnonymousIntersectionClassType));
/* 2443 */       Object localObject = null;
/* 2444 */       for (Type localType : paramAnonymousIntersectionClassType.getExplicitComponents()) {
/* 2445 */         Symbol.TypeSymbol localTypeSymbol = localType.tsym;
/* 2446 */         if ((Attr.this.types.isFunctionalInterface(localTypeSymbol)) && 
/* 2447 */           (Attr.this.types
/* 2447 */           .findDescriptorSymbol(localTypeSymbol) == 
/* 2447 */           localSymbol))
/* 2448 */           localObject = localType;
/* 2449 */         else if ((!localTypeSymbol.isInterface()) || ((localTypeSymbol.flags() & 0x2000) != 0L))
/*      */         {
/* 2451 */           reportIntersectionError(paramAnonymousDiagnosticPosition, "not.an.intf.component", new Object[] { localTypeSymbol });
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/* 2456 */       return localObject != null ? localObject : 
/* 2456 */         (Type)paramAnonymousIntersectionClassType
/* 2456 */         .getExplicitComponents().head;
/*      */     }
/*      */ 
/*      */     private Symbol.TypeSymbol makeNotionalInterface(Type.IntersectionClassType paramAnonymousIntersectionClassType) {
/* 2460 */       ListBuffer localListBuffer1 = new ListBuffer();
/* 2461 */       ListBuffer localListBuffer2 = new ListBuffer();
/* 2462 */       for (Object localObject = paramAnonymousIntersectionClassType.interfaces_field.iterator(); ((Iterator)localObject).hasNext(); ) { Type localType = (Type)((Iterator)localObject).next();
/* 2463 */         if (localType.isParameterized()) {
/* 2464 */           localListBuffer1.appendList(localType.tsym.type.allparams());
/*      */         }
/* 2466 */         localListBuffer2.append(localType.tsym.type);
/*      */       }
/* 2468 */       localObject = Attr.this.types.makeIntersectionType(localListBuffer2.toList());
/* 2469 */       ((Type.IntersectionClassType)localObject).allparams_field = localListBuffer1.toList();
/* 2470 */       ((Type.IntersectionClassType)localObject).tsym.flags_field |= 512L;
/* 2471 */       return ((Type.IntersectionClassType)localObject).tsym;
/*      */     }
/*      */ 
/*      */     private void reportIntersectionError(JCDiagnostic.DiagnosticPosition paramAnonymousDiagnosticPosition, String paramAnonymousString, Object[] paramAnonymousArrayOfObject) {
/* 2475 */       Attr.this.resultInfo.checkContext.report(paramAnonymousDiagnosticPosition, Attr.this.diags.fragment("bad.intersection.target.for.functional.expr", new Object[] { Attr.this.diags
/* 2476 */         .fragment(paramAnonymousString, paramAnonymousArrayOfObject) }));
/*      */     }
/* 2433 */   };
/*      */ 
/* 2596 */   private Map<Symbol.ClassSymbol, Symbol.MethodSymbol> clinits = new HashMap();
/*      */ 
/* 3747 */   Warner noteWarner = new Warner();
/*      */ 
/* 4385 */   public static final Filter<Symbol> anyNonAbstractOrDefaultMethod = new Filter()
/*      */   {
/*      */     public boolean accepts(Symbol paramAnonymousSymbol)
/*      */     {
/* 4389 */       return (paramAnonymousSymbol.kind == 16) && 
/* 4389 */         ((paramAnonymousSymbol
/* 4389 */         .flags() & 0x400) != 1024L);
/*      */     }
/* 4385 */   };
/*      */ 
/*      */   public static Attr instance(Context paramContext)
/*      */   {
/*  100 */     Attr localAttr = (Attr)paramContext.get(attrKey);
/*  101 */     if (localAttr == null)
/*  102 */       localAttr = new Attr(paramContext);
/*  103 */     return localAttr;
/*      */   }
/*      */ 
/*      */   protected Attr(Context paramContext) {
/*  107 */     paramContext.put(attrKey, this);
/*      */ 
/*  109 */     this.names = Names.instance(paramContext);
/*  110 */     this.log = Log.instance(paramContext);
/*  111 */     this.syms = Symtab.instance(paramContext);
/*  112 */     this.rs = Resolve.instance(paramContext);
/*  113 */     this.chk = Check.instance(paramContext);
/*  114 */     this.flow = Flow.instance(paramContext);
/*  115 */     this.memberEnter = MemberEnter.instance(paramContext);
/*  116 */     this.make = TreeMaker.instance(paramContext);
/*  117 */     this.enter = Enter.instance(paramContext);
/*  118 */     this.infer = Infer.instance(paramContext);
/*  119 */     this.deferredAttr = DeferredAttr.instance(paramContext);
/*  120 */     this.cfolder = ConstFold.instance(paramContext);
/*  121 */     this.target = Target.instance(paramContext);
/*  122 */     this.types = Types.instance(paramContext);
/*  123 */     this.diags = JCDiagnostic.Factory.instance(paramContext);
/*  124 */     this.annotate = Annotate.instance(paramContext);
/*  125 */     this.typeAnnotations = TypeAnnotations.instance(paramContext);
/*  126 */     this.deferredLintHandler = DeferredLintHandler.instance(paramContext);
/*  127 */     this.typeEnvs = TypeEnvs.instance(paramContext);
/*      */ 
/*  129 */     Options localOptions = Options.instance(paramContext);
/*      */ 
/*  131 */     Source localSource = Source.instance(paramContext);
/*  132 */     this.allowGenerics = localSource.allowGenerics();
/*  133 */     this.allowVarargs = localSource.allowVarargs();
/*  134 */     this.allowEnums = localSource.allowEnums();
/*  135 */     this.allowBoxing = localSource.allowBoxing();
/*  136 */     this.allowCovariantReturns = localSource.allowCovariantReturns();
/*  137 */     this.allowAnonOuterThis = localSource.allowAnonOuterThis();
/*  138 */     this.allowStringsInSwitch = localSource.allowStringsInSwitch();
/*  139 */     this.allowPoly = localSource.allowPoly();
/*  140 */     this.allowTypeAnnos = localSource.allowTypeAnnotations();
/*  141 */     this.allowLambda = localSource.allowLambda();
/*  142 */     this.allowDefaultMethods = localSource.allowDefaultMethods();
/*  143 */     this.allowStaticInterfaceMethods = localSource.allowStaticInterfaceMethods();
/*  144 */     this.sourceName = localSource.name;
/*  145 */     this.relax = ((localOptions.isSet("-retrofit")) || 
/*  146 */       (localOptions
/*  146 */       .isSet("-relax")));
/*      */ 
/*  147 */     this.findDiamonds = ((localOptions.get("findDiamond") != null) && 
/*  148 */       (localSource
/*  148 */       .allowDiamond()));
/*  149 */     this.useBeforeDeclarationWarning = localOptions.isSet("useBeforeDeclarationWarning");
/*  150 */     this.identifyLambdaCandidate = localOptions.getBoolean("identifyLambdaCandidate", false);
/*      */ 
/*  152 */     this.statInfo = new ResultInfo(0, Type.noType);
/*  153 */     this.varInfo = new ResultInfo(4, Type.noType);
/*  154 */     this.unknownExprInfo = new ResultInfo(12, Type.noType);
/*  155 */     this.unknownAnyPolyInfo = new ResultInfo(12, Infer.anyPoly);
/*  156 */     this.unknownTypeInfo = new ResultInfo(2, Type.noType);
/*  157 */     this.unknownTypeExprInfo = new ResultInfo(14, Type.noType);
/*  158 */     this.recoveryInfo = new RecoveryInfo(this.deferredAttr.emptyDeferredAttrContext);
/*      */   }
/*      */ 
/*      */   Type check(final JCTree paramJCTree, final Type paramType, final int paramInt, final ResultInfo paramResultInfo)
/*      */   {
/*  254 */     Infer.InferenceContext localInferenceContext = paramResultInfo.checkContext.inferenceContext();
/*      */     Type localType;
/*  256 */     if ((!paramType.hasTag(TypeTag.ERROR)) && (!paramResultInfo.pt.hasTag(TypeTag.METHOD)) && (!paramResultInfo.pt.hasTag(TypeTag.FORALL))) {
/*  257 */       if ((paramInt & (paramResultInfo.pkind ^ 0xFFFFFFFF)) != 0) {
/*  258 */         this.log.error(paramJCTree.pos(), "unexpected.type", new Object[] { 
/*  259 */           Kinds.kindNames(paramResultInfo.pkind), 
/*  260 */           Kinds.kindName(paramInt) });
/*      */ 
/*  261 */         localType = this.types.createErrorType(paramType);
/*  262 */       } else if ((this.allowPoly) && (localInferenceContext.free(paramType)))
/*      */       {
/*  265 */         localType = paramResultInfo.pt;
/*  266 */         localInferenceContext.addFreeTypeListener(List.of(paramType, paramResultInfo.pt), new Infer.FreeTypeListener()
/*      */         {
/*      */           public void typesInferred(Infer.InferenceContext paramAnonymousInferenceContext)
/*      */           {
/*  270 */             Attr.ResultInfo localResultInfo = paramResultInfo
/*  270 */               .dup(paramAnonymousInferenceContext
/*  270 */               .asInstType(paramResultInfo.pt));
/*      */ 
/*  271 */             Attr.this.check(paramJCTree, paramAnonymousInferenceContext.asInstType(paramType), paramInt, localResultInfo);
/*      */           } } );
/*      */       }
/*      */       else {
/*  275 */         localType = paramResultInfo.check(paramJCTree, paramType);
/*      */       }
/*      */     }
/*  278 */     else localType = paramType;
/*      */ 
/*  280 */     paramJCTree.type = localType;
/*  281 */     return localType;
/*      */   }
/*      */ 
/*      */   boolean isAssignableAsBlankFinal(Symbol.VarSymbol paramVarSymbol, Env<AttrContext> paramEnv)
/*      */   {
/*  290 */     Symbol localSymbol = ((AttrContext)paramEnv.info).scope.owner;
/*      */ 
/*  293 */     if (paramVarSymbol.owner != localSymbol) if (((localSymbol.name != this.names.init) && (localSymbol.kind != 4) && 
/*  298 */         ((localSymbol
/*  298 */         .flags() & 0x100000) == 0L)) || (paramVarSymbol.owner != localSymbol.owner))
/*      */       {
/*      */         break label97;
/*      */       }
/*  302 */     label97: return ((paramVarSymbol
/*  302 */       .flags() & 0x8) != 0L) == Resolve.isStatic(paramEnv);
/*      */   }
/*      */ 
/*      */   void checkAssignable(JCDiagnostic.DiagnosticPosition paramDiagnosticPosition, Symbol.VarSymbol paramVarSymbol, JCTree paramJCTree, Env<AttrContext> paramEnv)
/*      */   {
/*  313 */     if (((paramVarSymbol.flags() & 0x10) != 0L) && (
/*  314 */       ((paramVarSymbol
/*  314 */       .flags() & 0x40000) != 0L) || ((paramJCTree != null) && (
/*  317 */       (!paramJCTree
/*  317 */       .hasTag(JCTree.Tag.IDENT)) || 
/*  317 */       (TreeInfo.name(paramJCTree) != this.names._this))) || 
/*  318 */       (!isAssignableAsBlankFinal(paramVarSymbol, paramEnv))))
/*      */     {
/*  319 */       if (paramVarSymbol.isResourceVariable())
/*  320 */         this.log.error(paramDiagnosticPosition, "try.resource.may.not.be.assigned", new Object[] { paramVarSymbol });
/*      */       else
/*  322 */         this.log.error(paramDiagnosticPosition, "cant.assign.val.to.final.var", new Object[] { paramVarSymbol });
/*      */     }
/*      */   }
/*      */ 
/*      */   boolean isStaticReference(JCTree paramJCTree)
/*      */   {
/*  333 */     if (paramJCTree.hasTag(JCTree.Tag.SELECT)) {
/*  334 */       Symbol localSymbol = TreeInfo.symbol(((JCTree.JCFieldAccess)paramJCTree).selected);
/*  335 */       if ((localSymbol == null) || (localSymbol.kind != 2)) {
/*  336 */         return false;
/*      */       }
/*      */     }
/*  339 */     return true;
/*      */   }
/*      */ 
/*      */   static boolean isType(Symbol paramSymbol)
/*      */   {
/*  345 */     return (paramSymbol != null) && (paramSymbol.kind == 2);
/*      */   }
/*      */ 
/*      */   Symbol thisSym(JCDiagnostic.DiagnosticPosition paramDiagnosticPosition, Env<AttrContext> paramEnv)
/*      */   {
/*  352 */     return this.rs.resolveSelf(paramDiagnosticPosition, paramEnv, paramEnv.enclClass.sym, this.names._this);
/*      */   }
/*      */ 
/*      */   public Symbol attribIdent(JCTree paramJCTree, JCTree.JCCompilationUnit paramJCCompilationUnit)
/*      */   {
/*  360 */     Env localEnv = this.enter.topLevelEnv(paramJCCompilationUnit);
/*  361 */     localEnv.enclClass = this.make.ClassDef(this.make.Modifiers(0L), this.syms.errSymbol.name, null, null, null, null);
/*      */ 
/*  364 */     localEnv.enclClass.sym = this.syms.errSymbol;
/*  365 */     return (Symbol)paramJCTree.accept(this.identAttributer, localEnv);
/*      */   }
/*      */ 
/*      */   public Type coerce(Type paramType1, Type paramType2)
/*      */   {
/*  392 */     return this.cfolder.coerce(paramType1, paramType2);
/*      */   }
/*      */ 
/*      */   public Type attribType(JCTree paramJCTree, Symbol.TypeSymbol paramTypeSymbol) {
/*  396 */     Env localEnv1 = this.typeEnvs.get(paramTypeSymbol);
/*  397 */     Env localEnv2 = localEnv1.dup(paramJCTree, ((AttrContext)localEnv1.info).dup());
/*  398 */     return attribTree(paramJCTree, localEnv2, this.unknownTypeInfo);
/*      */   }
/*      */ 
/*      */   public Type attribImportQualifier(JCTree.JCImport paramJCImport, Env<AttrContext> paramEnv)
/*      */   {
/*  403 */     JCTree.JCFieldAccess localJCFieldAccess = (JCTree.JCFieldAccess)paramJCImport.qualid;
/*  404 */     return attribTree(localJCFieldAccess.selected, paramEnv, new ResultInfo(paramJCImport.staticImport ? 2 : 3, Type.noType));
/*      */   }
/*      */ 
/*      */   public Env<AttrContext> attribExprToTree(JCTree paramJCTree1, Env<AttrContext> paramEnv, JCTree paramJCTree2)
/*      */   {
/*  411 */     this.breakTree = paramJCTree2;
/*  412 */     JavaFileObject localJavaFileObject = this.log.useSource(paramEnv.toplevel.sourcefile);
/*      */     try {
/*  414 */       attribExpr(paramJCTree1, paramEnv);
/*      */     } catch (BreakAttr localBreakAttr) {
/*  416 */       return localBreakAttr.env;
/*      */     }
/*      */     catch (AssertionError localAssertionError)
/*      */     {
/*      */       Env localEnv;
/*  418 */       if ((localAssertionError.getCause() instanceof BreakAttr)) {
/*  419 */         return ((BreakAttr)localAssertionError.getCause()).env;
/*      */       }
/*  421 */       throw localAssertionError;
/*      */     }
/*      */     finally {
/*  424 */       this.breakTree = null;
/*  425 */       this.log.useSource(localJavaFileObject);
/*      */     }
/*  427 */     return paramEnv;
/*      */   }
/*      */ 
/*      */   public Env<AttrContext> attribStatToTree(JCTree paramJCTree1, Env<AttrContext> paramEnv, JCTree paramJCTree2) {
/*  431 */     this.breakTree = paramJCTree2;
/*  432 */     JavaFileObject localJavaFileObject = this.log.useSource(paramEnv.toplevel.sourcefile);
/*      */     try {
/*  434 */       attribStat(paramJCTree1, paramEnv);
/*      */     } catch (BreakAttr localBreakAttr) {
/*  436 */       return localBreakAttr.env;
/*      */     }
/*      */     catch (AssertionError localAssertionError)
/*      */     {
/*      */       Env localEnv;
/*  438 */       if ((localAssertionError.getCause() instanceof BreakAttr)) {
/*  439 */         return ((BreakAttr)localAssertionError.getCause()).env;
/*      */       }
/*  441 */       throw localAssertionError;
/*      */     }
/*      */     finally {
/*  444 */       this.breakTree = null;
/*  445 */       this.log.useSource(localJavaFileObject);
/*      */     }
/*  447 */     return paramEnv;
/*      */   }
/*      */ 
/*      */   Type pt()
/*      */   {
/*  530 */     return this.resultInfo.pt;
/*      */   }
/*      */ 
/*      */   int pkind() {
/*  534 */     return this.resultInfo.pkind;
/*      */   }
/*      */ 
/*      */   Type attribTree(JCTree paramJCTree, Env<AttrContext> paramEnv, ResultInfo paramResultInfo)
/*      */   {
/*  561 */     Env localEnv = this.env;
/*  562 */     ResultInfo localResultInfo = this.resultInfo;
/*      */     try {
/*  564 */       this.env = paramEnv;
/*  565 */       this.resultInfo = paramResultInfo;
/*  566 */       paramJCTree.accept(this);
/*  567 */       if ((paramJCTree == this.breakTree) && 
/*  568 */         (paramResultInfo.checkContext
/*  568 */         .deferredAttrContext().mode == DeferredAttr.AttrMode.CHECK)) {
/*  569 */         throw new BreakAttr(copyEnv(paramEnv), null);
/*      */       }
/*  571 */       return this.result;
/*      */     } catch (Symbol.CompletionFailure localCompletionFailure) {
/*  573 */       paramJCTree.type = this.syms.errType;
/*  574 */       return this.chk.completionError(paramJCTree.pos(), localCompletionFailure);
/*      */     } finally {
/*  576 */       this.env = localEnv;
/*  577 */       this.resultInfo = localResultInfo;
/*      */     }
/*      */   }
/*      */ 
/*      */   Env<AttrContext> copyEnv(Env<AttrContext> paramEnv)
/*      */   {
/*  583 */     Env localEnv = paramEnv
/*  583 */       .dup(paramEnv.tree, ((AttrContext)paramEnv.info)
/*  583 */       .dup(copyScope(((AttrContext)paramEnv.info).scope)));
/*      */ 
/*  584 */     if (localEnv.outer != null) {
/*  585 */       localEnv.outer = copyEnv(localEnv.outer);
/*      */     }
/*  587 */     return localEnv;
/*      */   }
/*      */ 
/*      */   Scope copyScope(Scope paramScope) {
/*  591 */     Scope localScope = new Scope(paramScope.owner);
/*  592 */     List localList = List.nil();
/*  593 */     while (paramScope != null) {
/*  594 */       for (localObject = paramScope.elems; localObject != null; localObject = ((Scope.Entry)localObject).sibling) {
/*  595 */         localList = localList.prepend(((Scope.Entry)localObject).sym);
/*      */       }
/*  597 */       paramScope = paramScope.next;
/*      */     }
/*  599 */     for (Object localObject = localList.iterator(); ((Iterator)localObject).hasNext(); ) { Symbol localSymbol = (Symbol)((Iterator)localObject).next();
/*  600 */       localScope.enter(localSymbol);
/*      */     }
/*  602 */     return localScope;
/*      */   }
/*      */ 
/*      */   public Type attribExpr(JCTree paramJCTree, Env<AttrContext> paramEnv, Type paramType)
/*      */   {
/*  608 */     return attribTree(paramJCTree, paramEnv, new ResultInfo(12, !paramType.hasTag(TypeTag.ERROR) ? paramType : Type.noType));
/*      */   }
/*      */ 
/*      */   public Type attribExpr(JCTree paramJCTree, Env<AttrContext> paramEnv)
/*      */   {
/*  615 */     return attribTree(paramJCTree, paramEnv, this.unknownExprInfo);
/*      */   }
/*      */ 
/*      */   public Type attribType(JCTree paramJCTree, Env<AttrContext> paramEnv)
/*      */   {
/*  621 */     Type localType = attribType(paramJCTree, paramEnv, Type.noType);
/*  622 */     return localType;
/*      */   }
/*      */ 
/*      */   Type attribType(JCTree paramJCTree, Env<AttrContext> paramEnv, Type paramType)
/*      */   {
/*  628 */     Type localType = attribTree(paramJCTree, paramEnv, new ResultInfo(2, paramType));
/*  629 */     return localType;
/*      */   }
/*      */ 
/*      */   public Type attribStat(JCTree paramJCTree, Env<AttrContext> paramEnv)
/*      */   {
/*  635 */     return attribTree(paramJCTree, paramEnv, this.statInfo);
/*      */   }
/*      */ 
/*      */   List<Type> attribExprs(List<JCTree.JCExpression> paramList, Env<AttrContext> paramEnv, Type paramType)
/*      */   {
/*  641 */     ListBuffer localListBuffer = new ListBuffer();
/*  642 */     for (Object localObject = paramList; ((List)localObject).nonEmpty(); localObject = ((List)localObject).tail)
/*  643 */       localListBuffer.append(attribExpr((JCTree)((List)localObject).head, paramEnv, paramType));
/*  644 */     return localListBuffer.toList();
/*      */   }
/*      */ 
/*      */   <T extends JCTree> void attribStats(List<T> paramList, Env<AttrContext> paramEnv)
/*      */   {
/*  650 */     for (Object localObject = paramList; ((List)localObject).nonEmpty(); localObject = ((List)localObject).tail)
/*  651 */       attribStat((JCTree)((List)localObject).head, paramEnv);
/*      */   }
/*      */ 
/*      */   int attribArgs(List<JCTree.JCExpression> paramList, Env<AttrContext> paramEnv, ListBuffer<Type> paramListBuffer)
/*      */   {
/*  657 */     int i = 12;
/*  658 */     for (JCTree.JCExpression localJCExpression : paramList)
/*      */     {
/*      */       Object localObject;
/*  660 */       if ((this.allowPoly) && (this.deferredAttr.isDeferred(paramEnv, localJCExpression)))
/*      */       {
/*      */         DeferredAttr tmp60_57 = this.deferredAttr; tmp60_57.getClass(); localObject = new DeferredAttr.DeferredType(tmp60_57, localJCExpression, paramEnv);
/*  662 */         i |= 32;
/*      */       } else {
/*  664 */         localObject = this.chk.checkNonVoid(localJCExpression, attribTree(localJCExpression, paramEnv, this.unknownAnyPolyInfo));
/*      */       }
/*  666 */       paramListBuffer.append(localObject);
/*      */     }
/*  668 */     return i;
/*      */   }
/*      */ 
/*      */   List<Type> attribAnyTypes(List<JCTree.JCExpression> paramList, Env<AttrContext> paramEnv)
/*      */   {
/*  675 */     ListBuffer localListBuffer = new ListBuffer();
/*  676 */     for (Object localObject = paramList; ((List)localObject).nonEmpty(); localObject = ((List)localObject).tail)
/*  677 */       localListBuffer.append(attribType((JCTree)((List)localObject).head, paramEnv));
/*  678 */     return localListBuffer.toList();
/*      */   }
/*      */ 
/*      */   List<Type> attribTypes(List<JCTree.JCExpression> paramList, Env<AttrContext> paramEnv)
/*      */   {
/*  685 */     List localList = attribAnyTypes(paramList, paramEnv);
/*  686 */     return this.chk.checkRefTypes(paramList, localList);
/*      */   }
/*      */ 
/*      */   void attribTypeVariables(List<JCTree.JCTypeParameter> paramList, Env<AttrContext> paramEnv)
/*      */   {
/*  696 */     for (Iterator localIterator1 = paramList.iterator(); localIterator1.hasNext(); ) { localJCTypeParameter = (JCTree.JCTypeParameter)localIterator1.next();
/*  697 */       Type.TypeVar localTypeVar = (Type.TypeVar)localJCTypeParameter.type;
/*  698 */       localTypeVar.tsym.flags_field |= 268435456L;
/*  699 */       localTypeVar.bound = Type.noType;
/*  700 */       if (!localJCTypeParameter.bounds.isEmpty()) {
/*  701 */         List localList = List.of(attribType((JCTree)localJCTypeParameter.bounds.head, paramEnv));
/*  702 */         for (JCTree.JCExpression localJCExpression : localJCTypeParameter.bounds.tail)
/*  703 */           localList = localList.prepend(attribType(localJCExpression, paramEnv));
/*  704 */         this.types.setBounds(localTypeVar, localList.reverse());
/*      */       }
/*      */       else
/*      */       {
/*  708 */         this.types.setBounds(localTypeVar, List.of(this.syms.objectType));
/*      */       }
/*  710 */       localTypeVar.tsym.flags_field &= -268435457L;
/*      */     }
/*  712 */     JCTree.JCTypeParameter localJCTypeParameter;
/*  712 */     for (localIterator1 = paramList.iterator(); localIterator1.hasNext(); ) { localJCTypeParameter = (JCTree.JCTypeParameter)localIterator1.next();
/*  713 */       this.chk.checkNonCyclic(localJCTypeParameter.pos(), (Type.TypeVar)localJCTypeParameter.type);
/*      */     }
/*      */   }
/*      */ 
/*      */   void attribAnnotationTypes(List<JCTree.JCAnnotation> paramList, Env<AttrContext> paramEnv)
/*      */   {
/*  722 */     for (Object localObject = paramList; ((List)localObject).nonEmpty(); localObject = ((List)localObject).tail) {
/*  723 */       JCTree.JCAnnotation localJCAnnotation = (JCTree.JCAnnotation)((List)localObject).head;
/*  724 */       attribType(localJCAnnotation.annotationType, paramEnv);
/*      */     }
/*      */   }
/*      */ 
/*      */   public Object attribLazyConstantValue(Env<AttrContext> paramEnv, JCTree.JCVariableDecl paramJCVariableDecl, Type paramType)
/*      */   {
/*  740 */     JCDiagnostic.DiagnosticPosition localDiagnosticPosition = this.deferredLintHandler
/*  740 */       .setPos(paramJCVariableDecl
/*  740 */       .pos());
/*      */     try
/*      */     {
/*  748 */       this.memberEnter.typeAnnotate(paramJCVariableDecl.init, paramEnv, null, paramJCVariableDecl.pos());
/*  749 */       this.annotate.flush();
/*  750 */       Type localType = attribExpr(paramJCVariableDecl.init, paramEnv, paramType);
/*      */       Object localObject1;
/*  751 */       if (localType.constValue() != null) {
/*  752 */         return coerce(localType, paramType).constValue();
/*      */       }
/*  754 */       return null;
/*      */     }
/*      */     finally {
/*  757 */       this.deferredLintHandler.setPos(localDiagnosticPosition);
/*      */     }
/*      */   }
/*      */ 
/*      */   Type attribBase(JCTree paramJCTree, Env<AttrContext> paramEnv, boolean paramBoolean1, boolean paramBoolean2, boolean paramBoolean3)
/*      */   {
/*  776 */     Type localType = paramJCTree.type != null ? paramJCTree.type : 
/*  776 */       attribType(paramJCTree, paramEnv);
/*      */ 
/*  777 */     return checkBase(localType, paramJCTree, paramEnv, paramBoolean1, paramBoolean2, paramBoolean3);
/*      */   }
/*      */ 
/*      */   Type checkBase(Type paramType, JCTree paramJCTree, Env<AttrContext> paramEnv, boolean paramBoolean1, boolean paramBoolean2, boolean paramBoolean3)
/*      */   {
/*  785 */     if (paramType.tsym.isAnonymous()) {
/*  786 */       this.log.error(paramJCTree.pos(), "cant.inherit.from.anon", new Object[0]);
/*  787 */       return this.types.createErrorType(paramType);
/*      */     }
/*  789 */     if (paramType.isErroneous())
/*  790 */       return paramType;
/*  791 */     if ((paramType.hasTag(TypeTag.TYPEVAR)) && (!paramBoolean1) && (!paramBoolean2))
/*      */     {
/*  793 */       if (paramType.getUpperBound() == null) {
/*  794 */         this.log.error(paramJCTree.pos(), "illegal.forward.ref", new Object[0]);
/*  795 */         return this.types.createErrorType(paramType);
/*      */       }
/*      */     }
/*  798 */     else paramType = this.chk.checkClassType(paramJCTree.pos(), paramType, paramBoolean3 | !this.allowGenerics);
/*      */ 
/*  800 */     if ((paramBoolean2) && ((paramType.tsym.flags() & 0x200) == 0L)) {
/*  801 */       this.log.error(paramJCTree.pos(), "intf.expected.here", new Object[0]);
/*      */ 
/*  804 */       return this.types.createErrorType(paramType);
/*  805 */     }if ((paramBoolean3) && (paramBoolean1))
/*      */     {
/*  807 */       if ((paramType.tsym
/*  807 */         .flags() & 0x200) != 0L) {
/*  808 */         this.log.error(paramJCTree.pos(), "no.intf.expected.here", new Object[0]);
/*  809 */         return this.types.createErrorType(paramType);
/*      */       }
/*      */     }
/*  811 */     if ((paramBoolean3) && 
/*  812 */       ((paramType.tsym
/*  812 */       .flags() & 0x10) != 0L)) {
/*  813 */       this.log.error(paramJCTree.pos(), "cant.inherit.from.final", new Object[] { paramType.tsym });
/*      */     }
/*      */ 
/*  816 */     this.chk.checkNonCyclic(paramJCTree.pos(), paramType);
/*  817 */     return paramType;
/*      */   }
/*      */ 
/*      */   Type attribIdentAsEnumType(Env<AttrContext> paramEnv, JCTree.JCIdent paramJCIdent) {
/*  821 */     Assert.check((paramEnv.enclClass.sym.flags() & 0x4000) != 0L);
/*  822 */     paramJCIdent.type = ((AttrContext)paramEnv.info).scope.owner.type;
/*  823 */     paramJCIdent.sym = ((AttrContext)paramEnv.info).scope.owner;
/*  824 */     return paramJCIdent.type;
/*      */   }
/*      */ 
/*      */   public void visitClassDef(JCTree.JCClassDecl paramJCClassDecl)
/*      */   {
/*  830 */     if ((((AttrContext)this.env.info).scope.owner.kind & 0x14) != 0) {
/*  831 */       this.enter.classEnter(paramJCClassDecl, this.env);
/*      */     }
/*  837 */     else if ((this.env.tree.hasTag(JCTree.Tag.NEWCLASS)) && (TreeInfo.isInAnnotation(this.env, paramJCClassDecl))) {
/*  838 */       this.enter.classEnter(paramJCClassDecl, this.env);
/*      */     }
/*      */ 
/*  841 */     Symbol.ClassSymbol localClassSymbol = paramJCClassDecl.sym;
/*  842 */     if (localClassSymbol == null)
/*      */     {
/*  844 */       this.result = null;
/*      */     }
/*      */     else {
/*  847 */       localClassSymbol.complete();
/*      */ 
/*  854 */       if ((((AttrContext)this.env.info).isSelfCall) && 
/*  855 */         (this.env.tree
/*  855 */         .hasTag(JCTree.Tag.NEWCLASS)) && 
/*  855 */         (((JCTree.JCNewClass)this.env.tree).encl == null))
/*      */       {
/*  858 */         localClassSymbol.flags_field |= 4194304L;
/*      */       }
/*  860 */       attribClass(paramJCClassDecl.pos(), localClassSymbol);
/*  861 */       this.result = (paramJCClassDecl.type = localClassSymbol.type);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void visitMethodDef(JCTree.JCMethodDecl paramJCMethodDecl) {
/*  866 */     Symbol.MethodSymbol localMethodSymbol1 = paramJCMethodDecl.sym;
/*  867 */     int i = (localMethodSymbol1.flags() & 0x0) != 0L ? 1 : 0;
/*      */ 
/*  869 */     Lint localLint1 = ((AttrContext)this.env.info).lint.augment(localMethodSymbol1);
/*  870 */     Lint localLint2 = this.chk.setLint(localLint1);
/*  871 */     Symbol.MethodSymbol localMethodSymbol2 = this.chk.setMethod(localMethodSymbol1);
/*      */     try {
/*  873 */       this.deferredLintHandler.flush(paramJCMethodDecl.pos());
/*  874 */       this.chk.checkDeprecatedAnnotation(paramJCMethodDecl.pos(), localMethodSymbol1);
/*      */ 
/*  879 */       Env localEnv = this.memberEnter.methodEnv(paramJCMethodDecl, this.env);
/*  880 */       ((AttrContext)localEnv.info).lint = localLint1;
/*      */ 
/*  882 */       attribStats(paramJCMethodDecl.typarams, localEnv);
/*      */ 
/*  886 */       if (localMethodSymbol1.isStatic())
/*  887 */         this.chk.checkHideClashes(paramJCMethodDecl.pos(), this.env.enclClass.type, localMethodSymbol1);
/*      */       else {
/*  889 */         this.chk.checkOverrideClashes(paramJCMethodDecl.pos(), this.env.enclClass.type, localMethodSymbol1);
/*      */       }
/*  891 */       this.chk.checkOverride(paramJCMethodDecl, localMethodSymbol1);
/*      */ 
/*  893 */       if ((i != 0) && (this.types.overridesObjectMethod(localMethodSymbol1.enclClass(), localMethodSymbol1))) {
/*  894 */         this.log.error(paramJCMethodDecl, "default.overrides.object.member", new Object[] { localMethodSymbol1.name, Kinds.kindName(localMethodSymbol1.location()), localMethodSymbol1.location() });
/*      */       }
/*      */ 
/*  898 */       for (Object localObject1 = paramJCMethodDecl.typarams; ((List)localObject1).nonEmpty(); localObject1 = ((List)localObject1).tail) {
/*  899 */         ((AttrContext)localEnv.info).scope.enterIfAbsent(((JCTree.JCTypeParameter)((List)localObject1).head).type.tsym);
/*      */       }
/*  901 */       localObject1 = this.env.enclClass.sym;
/*  902 */       if (((((Symbol.ClassSymbol)localObject1).flags() & 0x2000) != 0L) && 
/*  903 */         (paramJCMethodDecl.params
/*  903 */         .nonEmpty())) {
/*  904 */         this.log.error(((JCTree.JCVariableDecl)paramJCMethodDecl.params.head).pos(), "intf.annotation.members.cant.have.params", new Object[0]);
/*      */       }
/*      */ 
/*  908 */       for (Object localObject2 = paramJCMethodDecl.params; ((List)localObject2).nonEmpty(); localObject2 = ((List)localObject2).tail) {
/*  909 */         attribStat((JCTree)((List)localObject2).head, localEnv);
/*      */       }
/*      */ 
/*  912 */       this.chk.checkVarargsMethodDecl(localEnv, paramJCMethodDecl);
/*      */ 
/*  915 */       this.chk.validate(paramJCMethodDecl.typarams, localEnv);
/*      */ 
/*  918 */       if ((paramJCMethodDecl.restype != null) && (!paramJCMethodDecl.restype.type.hasTag(TypeTag.VOID))) {
/*  919 */         this.chk.validate(paramJCMethodDecl.restype, localEnv);
/*      */       }
/*      */ 
/*  922 */       if (paramJCMethodDecl.recvparam != null)
/*      */       {
/*  926 */         localObject2 = this.memberEnter.methodEnv(paramJCMethodDecl, this.env);
/*  927 */         attribType(paramJCMethodDecl.recvparam, (Env)localObject2);
/*  928 */         this.chk.validate(paramJCMethodDecl.recvparam, (Env)localObject2);
/*      */       }
/*      */ 
/*  932 */       if ((((Symbol.ClassSymbol)localObject1).flags() & 0x2000) != 0L)
/*      */       {
/*  934 */         if (paramJCMethodDecl.thrown.nonEmpty()) {
/*  935 */           this.log.error(((JCTree.JCExpression)paramJCMethodDecl.thrown.head).pos(), "throws.not.allowed.in.intf.annotation", new Object[0]);
/*      */         }
/*      */ 
/*  939 */         if (paramJCMethodDecl.typarams.nonEmpty()) {
/*  940 */           this.log.error(((JCTree.JCTypeParameter)paramJCMethodDecl.typarams.head).pos(), "intf.annotation.members.cant.have.type.params", new Object[0]);
/*      */         }
/*      */ 
/*  944 */         this.chk.validateAnnotationType(paramJCMethodDecl.restype);
/*      */ 
/*  946 */         this.chk.validateAnnotationMethod(paramJCMethodDecl.pos(), localMethodSymbol1);
/*      */       }
/*      */ 
/*  949 */       for (localObject2 = paramJCMethodDecl.thrown; ((List)localObject2).nonEmpty(); localObject2 = ((List)localObject2).tail) {
/*  950 */         this.chk.checkType(((JCTree.JCExpression)((List)localObject2).head).pos(), ((JCTree.JCExpression)((List)localObject2).head).type, this.syms.throwableType);
/*      */       }
/*  952 */       if (paramJCMethodDecl.body == null)
/*      */       {
/*  956 */         if ((i != 0) || (((paramJCMethodDecl.sym.flags() & 0x500) == 0L) && (!this.relax)))
/*      */         {
/*  958 */           this.log.error(paramJCMethodDecl.pos(), "missing.meth.body.or.decl.abstract", new Object[0]);
/*  959 */         }if ((paramJCMethodDecl.defaultValue != null) && 
/*  960 */           ((((Symbol.ClassSymbol)localObject1).flags() & 0x2000) == 0L)) {
/*  961 */           this.log.error(paramJCMethodDecl.pos(), "default.allowed.in.intf.annotation.member", new Object[0]);
/*      */         }
/*      */       }
/*  964 */       else if (((paramJCMethodDecl.sym.flags() & 0x400) != 0L) && (i == 0)) {
/*  965 */         if ((((Symbol.ClassSymbol)localObject1).flags() & 0x200) != 0L)
/*  966 */           this.log.error(paramJCMethodDecl.body.pos(), "intf.meth.cant.have.body", new Object[0]);
/*      */         else
/*  968 */           this.log.error(paramJCMethodDecl.pos(), "abstract.meth.cant.have.body", new Object[0]);
/*      */       }
/*  970 */       else if ((paramJCMethodDecl.mods.flags & 0x100) != 0L) {
/*  971 */         this.log.error(paramJCMethodDecl.pos(), "native.meth.cant.have.body", new Object[0]);
/*      */       }
/*      */       else
/*      */       {
/*  976 */         if ((paramJCMethodDecl.name == this.names.init) && (((Symbol.ClassSymbol)localObject1).type != this.syms.objectType)) {
/*  977 */           localObject2 = paramJCMethodDecl.body;
/*  978 */           if ((((JCTree.JCBlock)localObject2).stats.isEmpty()) || 
/*  979 */             (!TreeInfo.isSelfCall((JCTree)((JCTree.JCBlock)localObject2).stats.head)))
/*      */           {
/*  980 */             ((JCTree.JCBlock)localObject2).stats = ((JCTree.JCBlock)localObject2).stats
/*  981 */               .prepend(this.memberEnter
/*  981 */               .SuperCall(this.make
/*  981 */               .at(((JCTree.JCBlock)localObject2).pos), 
/*  982 */               List.nil(), 
/*  983 */               List.nil(), false));
/*      */           }
/*  985 */           else if (((this.env.enclClass.sym.flags() & 0x4000) != 0L) && ((paramJCMethodDecl.mods.flags & 0x0) == 0L))
/*      */           {
/*  987 */             if (TreeInfo.isSuperCall((JCTree)((JCTree.JCBlock)localObject2).stats.head))
/*      */             {
/*  992 */               this.log.error(((JCTree.JCStatement)paramJCMethodDecl.body.stats.head).pos(), "call.to.super.not.allowed.in.enum.ctor", new Object[] { this.env.enclClass.sym });
/*      */             }
/*      */ 
/*      */           }
/*      */ 
/*      */         }
/*      */ 
/*  999 */         this.memberEnter.typeAnnotate(paramJCMethodDecl.body, localEnv, localMethodSymbol1, null);
/* 1000 */         this.annotate.flush();
/*      */ 
/* 1003 */         attribStat(paramJCMethodDecl.body, localEnv);
/*      */       }
/*      */ 
/* 1006 */       ((AttrContext)localEnv.info).scope.leave();
/* 1007 */       this.result = (paramJCMethodDecl.type = localMethodSymbol1.type);
/*      */     }
/*      */     finally {
/* 1010 */       this.chk.setLint(localLint2);
/* 1011 */       this.chk.setMethod(localMethodSymbol2);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void visitVarDef(JCTree.JCVariableDecl paramJCVariableDecl)
/*      */   {
/* 1017 */     if (((AttrContext)this.env.info).scope.owner.kind == 16) {
/* 1018 */       if (paramJCVariableDecl.sym != null)
/*      */       {
/* 1020 */         ((AttrContext)this.env.info).scope.enter(paramJCVariableDecl.sym); break label133;
/*      */       }
/*      */       try {
/* 1023 */         this.annotate.enterStart();
/* 1024 */         this.memberEnter.memberEnter(paramJCVariableDecl, this.env);
/*      */ 
/* 1026 */         this.annotate.enterDone(); } finally { this.annotate.enterDone(); }
/*      */ 
/*      */     }
/*      */ 
/* 1030 */     if (paramJCVariableDecl.init != null)
/*      */     {
/* 1032 */       this.memberEnter.typeAnnotate(paramJCVariableDecl.init, this.env, paramJCVariableDecl.sym, paramJCVariableDecl.pos());
/* 1033 */       this.annotate.flush();
/*      */     }
/*      */ 
/* 1037 */     label133: Symbol.VarSymbol localVarSymbol = paramJCVariableDecl.sym;
/* 1038 */     Lint localLint1 = ((AttrContext)this.env.info).lint.augment(localVarSymbol);
/* 1039 */     Lint localLint2 = this.chk.setLint(localLint1);
/*      */ 
/* 1042 */     if ((this.env.tree.hasTag(JCTree.Tag.LAMBDA)) && (((JCTree.JCLambda)this.env.tree).paramKind == JCTree.JCLambda.ParameterKind.IMPLICIT));
/* 1044 */     int i = (paramJCVariableDecl.sym
/* 1044 */       .flags() & 0x0) != 0L ? 1 : 0;
/* 1045 */     this.chk.validate(paramJCVariableDecl.vartype, this.env, i == 0);
/*      */     try
/*      */     {
/* 1048 */       localVarSymbol.getConstValue();
/* 1049 */       this.deferredLintHandler.flush(paramJCVariableDecl.pos());
/* 1050 */       this.chk.checkDeprecatedAnnotation(paramJCVariableDecl.pos(), localVarSymbol);
/*      */ 
/* 1052 */       if ((paramJCVariableDecl.init != null) && (
/* 1053 */         ((localVarSymbol.flags_field & 0x10) == 0L) || 
/* 1054 */         (!this.memberEnter
/* 1054 */         .needsLazyConstValue(paramJCVariableDecl.init))))
/*      */       {
/* 1059 */         Env localEnv = this.memberEnter.initEnv(paramJCVariableDecl, this.env);
/* 1060 */         ((AttrContext)localEnv.info).lint = localLint1;
/*      */ 
/* 1064 */         ((AttrContext)localEnv.info).enclVar = localVarSymbol;
/* 1065 */         attribExpr(paramJCVariableDecl.init, localEnv, localVarSymbol.type);
/*      */       }
/*      */ 
/* 1068 */       this.result = (paramJCVariableDecl.type = localVarSymbol.type);
/*      */     }
/*      */     finally {
/* 1071 */       this.chk.setLint(localLint2);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void visitSkip(JCTree.JCSkip paramJCSkip) {
/* 1076 */     this.result = null;
/*      */   }
/*      */ 
/*      */   public void visitBlock(JCTree.JCBlock paramJCBlock)
/*      */   {
/*      */     Env localEnv;
/* 1080 */     if (((AttrContext)this.env.info).scope.owner.kind == 2)
/*      */     {
/* 1085 */       localEnv = this.env
/* 1085 */         .dup(paramJCBlock, ((AttrContext)this.env.info)
/* 1085 */         .dup(((AttrContext)this.env.info).scope
/* 1085 */         .dupUnshared()));
/* 1086 */       ((AttrContext)localEnv.info).scope.owner = new Symbol.MethodSymbol(paramJCBlock.flags | 0x100000 | ((AttrContext)this.env.info).scope.owner
/* 1088 */         .flags() & 0x800, this.names.empty, null, ((AttrContext)this.env.info).scope.owner);
/*      */ 
/* 1090 */       if ((paramJCBlock.flags & 0x8) != 0L) ((AttrContext)localEnv.info).staticLevel += 1;
/*      */ 
/* 1093 */       this.memberEnter.typeAnnotate(paramJCBlock, localEnv, ((AttrContext)localEnv.info).scope.owner, null);
/* 1094 */       this.annotate.flush();
/*      */ 
/* 1099 */       Symbol.ClassSymbol localClassSymbol = (Symbol.ClassSymbol)((AttrContext)this.env.info).scope.owner;
/* 1100 */       List localList = ((AttrContext)localEnv.info).scope.owner.getRawTypeAttributes();
/* 1101 */       if ((paramJCBlock.flags & 0x8) != 0L)
/* 1102 */         localClassSymbol.appendClassInitTypeAttributes(localList);
/*      */       else {
/* 1104 */         localClassSymbol.appendInitTypeAttributes(localList);
/*      */       }
/*      */ 
/* 1108 */       attribStats(paramJCBlock.stats, localEnv);
/*      */     }
/*      */     else
/*      */     {
/* 1112 */       localEnv = this.env
/* 1112 */         .dup(paramJCBlock, ((AttrContext)this.env.info)
/* 1112 */         .dup(((AttrContext)this.env.info).scope
/* 1112 */         .dup()));
/*      */       try {
/* 1114 */         attribStats(paramJCBlock.stats, localEnv);
/*      */       } finally {
/* 1116 */         ((AttrContext)localEnv.info).scope.leave();
/*      */       }
/*      */     }
/* 1119 */     this.result = null;
/*      */   }
/*      */ 
/*      */   public void visitDoLoop(JCTree.JCDoWhileLoop paramJCDoWhileLoop) {
/* 1123 */     attribStat(paramJCDoWhileLoop.body, this.env.dup(paramJCDoWhileLoop));
/* 1124 */     attribExpr(paramJCDoWhileLoop.cond, this.env, this.syms.booleanType);
/* 1125 */     this.result = null;
/*      */   }
/*      */ 
/*      */   public void visitWhileLoop(JCTree.JCWhileLoop paramJCWhileLoop) {
/* 1129 */     attribExpr(paramJCWhileLoop.cond, this.env, this.syms.booleanType);
/* 1130 */     attribStat(paramJCWhileLoop.body, this.env.dup(paramJCWhileLoop));
/* 1131 */     this.result = null;
/*      */   }
/*      */ 
/*      */   public void visitForLoop(JCTree.JCForLoop paramJCForLoop)
/*      */   {
/* 1136 */     Env localEnv = this.env
/* 1136 */       .dup(this.env.tree, ((AttrContext)this.env.info)
/* 1136 */       .dup(((AttrContext)this.env.info).scope
/* 1136 */       .dup()));
/*      */     try {
/* 1138 */       attribStats(paramJCForLoop.init, localEnv);
/* 1139 */       if (paramJCForLoop.cond != null) attribExpr(paramJCForLoop.cond, localEnv, this.syms.booleanType);
/* 1140 */       localEnv.tree = paramJCForLoop;
/* 1141 */       attribStats(paramJCForLoop.step, localEnv);
/* 1142 */       attribStat(paramJCForLoop.body, localEnv);
/* 1143 */       this.result = null;
/*      */ 
/* 1146 */       ((AttrContext)localEnv.info).scope.leave(); } finally { ((AttrContext)localEnv.info).scope.leave(); }
/*      */ 
/*      */   }
/*      */ 
/*      */   public void visitForeachLoop(JCTree.JCEnhancedForLoop paramJCEnhancedForLoop)
/*      */   {
/* 1152 */     Env localEnv = this.env
/* 1152 */       .dup(this.env.tree, ((AttrContext)this.env.info)
/* 1152 */       .dup(((AttrContext)this.env.info).scope
/* 1152 */       .dup()));
/*      */     try
/*      */     {
/* 1157 */       Type localType1 = this.types.cvarUpperBound(attribExpr(paramJCEnhancedForLoop.expr, localEnv));
/* 1158 */       attribStat(paramJCEnhancedForLoop.var, localEnv);
/* 1159 */       this.chk.checkNonVoid(paramJCEnhancedForLoop.pos(), localType1);
/* 1160 */       Type localType2 = this.types.elemtype(localType1);
/* 1161 */       if (localType2 == null)
/*      */       {
/* 1163 */         Type localType3 = this.types.asSuper(localType1, this.syms.iterableType.tsym);
/* 1164 */         if (localType3 == null) {
/* 1165 */           this.log.error(paramJCEnhancedForLoop.expr.pos(), "foreach.not.applicable.to.type", new Object[] { localType1, this.diags
/* 1168 */             .fragment("type.req.array.or.iterable", new Object[0]) });
/*      */ 
/* 1169 */           localType2 = this.types.createErrorType(localType1);
/*      */         } else {
/* 1171 */           List localList = localType3.allparams();
/*      */ 
/* 1174 */           localType2 = localList.isEmpty() ? this.syms.objectType : this.types
/* 1174 */             .wildUpperBound((Type)localList.head);
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/* 1177 */       this.chk.checkType(paramJCEnhancedForLoop.expr.pos(), localType2, paramJCEnhancedForLoop.var.sym.type);
/* 1178 */       localEnv.tree = paramJCEnhancedForLoop;
/* 1179 */       attribStat(paramJCEnhancedForLoop.body, localEnv);
/* 1180 */       this.result = null;
/*      */     }
/*      */     finally {
/* 1183 */       ((AttrContext)localEnv.info).scope.leave();
/*      */     }
/*      */   }
/*      */ 
/*      */   public void visitLabelled(JCTree.JCLabeledStatement paramJCLabeledStatement)
/*      */   {
/* 1189 */     Env localEnv = this.env;
/* 1190 */     while ((localEnv != null) && (!localEnv.tree.hasTag(JCTree.Tag.CLASSDEF))) {
/* 1191 */       if ((localEnv.tree.hasTag(JCTree.Tag.LABELLED)) && (((JCTree.JCLabeledStatement)localEnv.tree).label == paramJCLabeledStatement.label))
/*      */       {
/* 1193 */         this.log.error(paramJCLabeledStatement.pos(), "label.already.in.use", new Object[] { paramJCLabeledStatement.label });
/*      */ 
/* 1195 */         break;
/*      */       }
/* 1197 */       localEnv = localEnv.next;
/*      */     }
/*      */ 
/* 1200 */     attribStat(paramJCLabeledStatement.body, this.env.dup(paramJCLabeledStatement));
/* 1201 */     this.result = null;
/*      */   }
/*      */ 
/*      */   public void visitSwitch(JCTree.JCSwitch paramJCSwitch) {
/* 1205 */     Type localType = attribExpr(paramJCSwitch.selector, this.env);
/*      */ 
/* 1208 */     Env localEnv1 = this.env
/* 1208 */       .dup(paramJCSwitch, ((AttrContext)this.env.info)
/* 1208 */       .dup(((AttrContext)this.env.info).scope
/* 1208 */       .dup()));
/*      */     try
/*      */     {
/* 1212 */       if (this.allowEnums);
/* 1214 */       int i = (localType.tsym
/* 1214 */         .flags() & 0x4000) != 0L ? 1 : 0;
/* 1215 */       int j = 0;
/* 1216 */       if (this.types.isSameType(localType, this.syms.stringType)) {
/* 1217 */         if (this.allowStringsInSwitch)
/* 1218 */           j = 1;
/*      */         else {
/* 1220 */           this.log.error(paramJCSwitch.selector.pos(), "string.switch.not.supported.in.source", new Object[] { this.sourceName });
/*      */         }
/*      */       }
/* 1223 */       if ((i == 0) && (j == 0)) {
/* 1224 */         localType = this.chk.checkType(paramJCSwitch.selector.pos(), localType, this.syms.intType);
/*      */       }
/*      */ 
/* 1228 */       HashSet localHashSet = new HashSet();
/* 1229 */       int k = 0;
/* 1230 */       for (List localList = paramJCSwitch.cases; localList.nonEmpty(); localList = localList.tail) {
/* 1231 */         JCTree.JCCase localJCCase = (JCTree.JCCase)localList.head;
/*      */ 
/* 1233 */         Env localEnv2 = localEnv1
/* 1233 */           .dup(localJCCase, ((AttrContext)this.env.info)
/* 1233 */           .dup(((AttrContext)localEnv1.info).scope
/* 1233 */           .dup()));
/*      */         try {
/* 1235 */           if (localJCCase.pat != null)
/*      */           {
/*      */             Object localObject1;
/* 1236 */             if (i != 0) {
/* 1237 */               localObject1 = enumConstant(localJCCase.pat, localType);
/* 1238 */               if (localObject1 == null)
/* 1239 */                 this.log.error(localJCCase.pat.pos(), "enum.label.must.be.unqualified.enum", new Object[0]);
/* 1240 */               else if (!localHashSet.add(localObject1))
/* 1241 */                 this.log.error(localJCCase.pos(), "duplicate.case.label", new Object[0]);
/*      */             }
/*      */             else {
/* 1244 */               localObject1 = attribExpr(localJCCase.pat, localEnv1, localType);
/* 1245 */               if (!((Type)localObject1).hasTag(TypeTag.ERROR)) {
/* 1246 */                 if (((Type)localObject1).constValue() == null) {
/* 1247 */                   this.log.error(localJCCase.pat.pos(), j != 0 ? "string.const.req" : "const.expr.req", new Object[0]);
/*      */                 }
/* 1249 */                 else if (localHashSet.contains(((Type)localObject1).constValue()))
/* 1250 */                   this.log.error(localJCCase.pos(), "duplicate.case.label", new Object[0]);
/*      */                 else
/* 1252 */                   localHashSet.add(((Type)localObject1).constValue());
/*      */               }
/*      */             }
/*      */           }
/* 1256 */           else if (k != 0) {
/* 1257 */             this.log.error(localJCCase.pos(), "duplicate.default.label", new Object[0]);
/*      */           } else {
/* 1259 */             k = 1;
/*      */           }
/* 1261 */           attribStats(localJCCase.stats, localEnv2);
/*      */         } finally {
/* 1263 */           ((AttrContext)localEnv2.info).scope.leave();
/* 1264 */           addVars(localJCCase.stats, ((AttrContext)localEnv1.info).scope);
/*      */         }
/*      */       }
/*      */ 
/* 1268 */       this.result = null;
/*      */     }
/*      */     finally {
/* 1271 */       ((AttrContext)localEnv1.info).scope.leave();
/*      */     }
/*      */   }
/*      */ 
/*      */   private static void addVars(List<JCTree.JCStatement> paramList, Scope paramScope)
/*      */   {
/* 1277 */     for (; paramList.nonEmpty(); paramList = paramList.tail) {
/* 1278 */       JCTree localJCTree = (JCTree)paramList.head;
/* 1279 */       if (localJCTree.hasTag(JCTree.Tag.VARDEF))
/* 1280 */         paramScope.enter(((JCTree.JCVariableDecl)localJCTree).sym);
/*      */     }
/*      */   }
/*      */ 
/*      */   private Symbol enumConstant(JCTree paramJCTree, Type paramType)
/*      */   {
/* 1286 */     if (!paramJCTree.hasTag(JCTree.Tag.IDENT)) {
/* 1287 */       this.log.error(paramJCTree.pos(), "enum.label.must.be.unqualified.enum", new Object[0]);
/* 1288 */       return this.syms.errSymbol;
/*      */     }
/* 1290 */     JCTree.JCIdent localJCIdent = (JCTree.JCIdent)paramJCTree;
/* 1291 */     Name localName = localJCIdent.name;
/* 1292 */     for (Scope.Entry localEntry = paramType.tsym.members().lookup(localName); 
/* 1293 */       localEntry.scope != null; localEntry = localEntry.next()) {
/* 1294 */       if (localEntry.sym.kind == 4) {
/* 1295 */         Symbol localSymbol = localJCIdent.sym = localEntry.sym;
/* 1296 */         ((Symbol.VarSymbol)localSymbol).getConstValue();
/* 1297 */         localJCIdent.type = localSymbol.type;
/* 1298 */         return (localSymbol.flags_field & 0x4000) == 0L ? null : localSymbol;
/*      */       }
/*      */     }
/*      */ 
/* 1302 */     return null;
/*      */   }
/*      */ 
/*      */   public void visitSynchronized(JCTree.JCSynchronized paramJCSynchronized) {
/* 1306 */     this.chk.checkRefType(paramJCSynchronized.pos(), attribExpr(paramJCSynchronized.lock, this.env));
/* 1307 */     attribStat(paramJCSynchronized.body, this.env);
/* 1308 */     this.result = null;
/*      */   }
/*      */ 
/*      */   public void visitTry(JCTree.JCTry paramJCTry)
/*      */   {
/* 1313 */     Env localEnv1 = this.env.dup(paramJCTry, ((AttrContext)this.env.info).dup(((AttrContext)this.env.info).scope.dup()));
/*      */     try { boolean bool = paramJCTry.resources.nonEmpty();
/*      */ 
/* 1318 */       Env localEnv2 = bool ? this.env
/* 1318 */         .dup(paramJCTry, ((AttrContext)localEnv1.info)
/* 1318 */         .dup(((AttrContext)localEnv1.info).scope
/* 1318 */         .dup())) : localEnv1;
/*      */       Object localObject2;
/*      */       Object localObject3;
/*      */       Object localObject4;
/*      */       try { for (localObject1 = paramJCTry.resources.iterator(); ((Iterator)localObject1).hasNext(); ) { localObject2 = (JCTree)((Iterator)localObject1).next();
/* 1323 */           localObject3 = new Check.NestedCheckContext(this.resultInfo.checkContext)
/*      */           {
/*      */             public void report(JCDiagnostic.DiagnosticPosition paramAnonymousDiagnosticPosition, JCDiagnostic paramAnonymousJCDiagnostic) {
/* 1326 */               Attr.this.chk.basicHandler.report(paramAnonymousDiagnosticPosition, Attr.this.diags.fragment("try.not.applicable.to.type", new Object[] { paramAnonymousJCDiagnostic }));
/*      */             }
/*      */           };
/* 1329 */           localObject4 = new ResultInfo(12, this.syms.autoCloseableType, (Check.CheckContext)localObject3);
/* 1330 */           if (((JCTree)localObject2).hasTag(JCTree.Tag.VARDEF)) {
/* 1331 */             attribStat((JCTree)localObject2, localEnv2);
/* 1332 */             ((ResultInfo)localObject4).check((JCDiagnostic.DiagnosticPosition)localObject2, ((JCTree)localObject2).type);
/*      */ 
/* 1335 */             checkAutoCloseable(((JCTree)localObject2).pos(), localEnv1, ((JCTree)localObject2).type);
/*      */ 
/* 1337 */             Symbol.VarSymbol localVarSymbol = ((JCTree.JCVariableDecl)localObject2).sym;
/* 1338 */             localVarSymbol.setData(ElementKind.RESOURCE_VARIABLE);
/*      */           } else {
/* 1340 */             attribTree((JCTree)localObject2, localEnv2, (ResultInfo)localObject4);
/*      */           }
/*      */ 
/*      */         }
/*      */ 
/*      */       }
/*      */       finally
/*      */       {
/* 1346 */         if (!bool);
/*      */       }
/*      */ 
/* 1351 */       for (Object localObject1 = paramJCTry.catchers; ((List)localObject1).nonEmpty(); localObject1 = ((List)localObject1).tail) {
/* 1352 */         localObject2 = (JCTree.JCCatch)((List)localObject1).head;
/*      */ 
/* 1354 */         localObject3 = localEnv1
/* 1354 */           .dup((JCTree)localObject2, ((AttrContext)localEnv1.info)
/* 1354 */           .dup(((AttrContext)localEnv1.info).scope
/* 1354 */           .dup()));
/*      */         try {
/* 1356 */           localObject4 = attribStat(((JCTree.JCCatch)localObject2).param, (Env)localObject3);
/* 1357 */           if (TreeInfo.isMultiCatch((JCTree.JCCatch)localObject2))
/*      */           {
/* 1359 */             ((JCTree.JCCatch)localObject2).param.sym.flags_field |= 549755813904L;
/*      */           }
/* 1361 */           if (((JCTree.JCCatch)localObject2).param.sym.kind == 4) {
/* 1362 */             ((JCTree.JCCatch)localObject2).param.sym.setData(ElementKind.EXCEPTION_PARAMETER);
/*      */           }
/* 1364 */           this.chk.checkType(((JCTree.JCCatch)localObject2).param.vartype.pos(), this.chk
/* 1365 */             .checkClassType(((JCTree.JCCatch)localObject2).param.vartype
/* 1365 */             .pos(), (Type)localObject4), this.syms.throwableType);
/*      */         }
/*      */         finally
/*      */         {
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/* 1374 */       if (paramJCTry.finalizer != null) attribStat(paramJCTry.finalizer, localEnv1);
/* 1375 */       this.result = null;
/*      */     } finally
/*      */     {
/* 1378 */       ((AttrContext)localEnv1.info).scope.leave();
/*      */     }
/*      */   }
/*      */ 
/*      */   void checkAutoCloseable(JCDiagnostic.DiagnosticPosition paramDiagnosticPosition, Env<AttrContext> paramEnv, Type paramType) {
/* 1383 */     if ((!paramType.isErroneous()) && 
/* 1384 */       (this.types
/* 1384 */       .asSuper(paramType, this.syms.autoCloseableType.tsym) != null) && 
/* 1385 */       (!this.types
/* 1385 */       .isSameType(paramType, this.syms.autoCloseableType)))
/*      */     {
/* 1386 */       Object localObject1 = this.syms.noSymbol;
/* 1387 */       Log.DiscardDiagnosticHandler localDiscardDiagnosticHandler = new Log.DiscardDiagnosticHandler(this.log);
/*      */       try {
/* 1389 */         localObject1 = this.rs.resolveQualifiedMethod(paramDiagnosticPosition, paramEnv, paramType, this.names.close, 
/* 1393 */           List.nil(), 
/* 1394 */           List.nil());
/*      */       }
/*      */       finally {
/* 1397 */         this.log.popDiagnosticHandler(localDiscardDiagnosticHandler);
/*      */       }
/* 1399 */       if ((((Symbol)localObject1).kind == 16) && 
/* 1400 */         (((Symbol)localObject1)
/* 1400 */         .overrides(this.syms.autoCloseableClose, paramType.tsym, this.types, true)) && 
/* 1401 */         (this.chk
/* 1401 */         .isHandled(this.syms.interruptedExceptionType, this.types
/* 1401 */         .memberType(paramType, (Symbol)localObject1)
/* 1401 */         .getThrownTypes())) && 
/* 1402 */         (((AttrContext)paramEnv.info).lint
/* 1402 */         .isEnabled(Lint.LintCategory.TRY)))
/*      */       {
/* 1403 */         this.log.warning(Lint.LintCategory.TRY, paramDiagnosticPosition, "try.resource.throws.interrupted.exc", new Object[] { paramType });
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   public void visitConditional(JCTree.JCConditional paramJCConditional) {
/* 1409 */     Type localType1 = attribExpr(paramJCConditional.cond, this.env, this.syms.booleanType);
/*      */ 
/* 1411 */     paramJCConditional.polyKind = ((!this.allowPoly) || 
/* 1412 */       ((pt().hasTag(TypeTag.NONE)) && (pt() != Type.recoveryType)) || 
/* 1413 */       (isBooleanOrNumeric(this.env, paramJCConditional)) ? 
/* 1413 */       JCTree.JCPolyExpression.PolyKind.STANDALONE : JCTree.JCPolyExpression.PolyKind.POLY);
/*      */ 
/* 1416 */     if ((paramJCConditional.polyKind == JCTree.JCPolyExpression.PolyKind.POLY) && (this.resultInfo.pt.hasTag(TypeTag.VOID)))
/*      */     {
/* 1418 */       this.resultInfo.checkContext.report(paramJCConditional, this.diags.fragment("conditional.target.cant.be.void", new Object[0]));
/* 1419 */       this.result = (paramJCConditional.type = this.types.createErrorType(this.resultInfo.pt));
/* 1420 */       return;
/*      */     }
/*      */ 
/* 1425 */     ResultInfo localResultInfo = paramJCConditional.polyKind == JCTree.JCPolyExpression.PolyKind.STANDALONE ? this.unknownExprInfo : this.resultInfo
/* 1425 */       .dup(new Check.NestedCheckContext(this.resultInfo.checkContext)
/*      */     {
/*      */       public void report(JCDiagnostic.DiagnosticPosition paramAnonymousDiagnosticPosition, JCDiagnostic paramAnonymousJCDiagnostic)
/*      */       {
/* 1431 */         this.enclosingContext.report(paramAnonymousDiagnosticPosition, Attr.this.diags.fragment("incompatible.type.in.conditional", new Object[] { paramAnonymousJCDiagnostic }));
/*      */       }
/*      */     });
/* 1435 */     Type localType2 = attribTree(paramJCConditional.truepart, this.env, localResultInfo);
/* 1436 */     Type localType3 = attribTree(paramJCConditional.falsepart, this.env, localResultInfo);
/*      */ 
/* 1438 */     Type localType4 = paramJCConditional.polyKind == JCTree.JCPolyExpression.PolyKind.STANDALONE ? condType(paramJCConditional, localType2, localType3) : pt();
/* 1439 */     if ((localType1.constValue() != null) && 
/* 1440 */       (localType2
/* 1440 */       .constValue() != null) && 
/* 1441 */       (localType3
/* 1441 */       .constValue() != null) && 
/* 1442 */       (!localType4
/* 1442 */       .hasTag(TypeTag.NONE)))
/*      */     {
/* 1444 */       localType4 = this.cfolder.coerce(localType1.isTrue() ? localType2 : localType3, localType4);
/*      */     }
/* 1446 */     this.result = check(paramJCConditional, localType4, 12, this.resultInfo);
/*      */   }
/*      */ 
/*      */   private boolean isBooleanOrNumeric(Env<AttrContext> paramEnv, JCTree.JCExpression paramJCExpression) {
/* 1450 */     switch (14.$SwitchMap$com$sun$tools$javac$tree$JCTree$Tag[paramJCExpression.getTag().ordinal()]) { case 1:
/* 1451 */       return (((JCTree.JCLiteral)paramJCExpression).typetag.isSubRangeOf(TypeTag.DOUBLE)) || (((JCTree.JCLiteral)paramJCExpression).typetag == TypeTag.BOOLEAN) || (((JCTree.JCLiteral)paramJCExpression).typetag == TypeTag.BOT);
/*      */     case 2:
/*      */     case 3:
/* 1454 */       return false;
/*      */     case 4:
/* 1455 */       return isBooleanOrNumeric(paramEnv, ((JCTree.JCParens)paramJCExpression).expr);
/*      */     case 5:
/* 1457 */       JCTree.JCConditional localJCConditional = (JCTree.JCConditional)paramJCExpression;
/*      */ 
/* 1459 */       return (isBooleanOrNumeric(paramEnv, localJCConditional.truepart)) && 
/* 1459 */         (isBooleanOrNumeric(paramEnv, localJCConditional.falsepart));
/*      */     case 6:
/* 1462 */       JCTree.JCMethodInvocation localJCMethodInvocation = (JCTree.JCMethodInvocation)this.deferredAttr
/* 1462 */         .attribSpeculative(paramJCExpression, paramEnv, this.unknownExprInfo);
/*      */ 
/* 1463 */       Type localType1 = TreeInfo.symbol(localJCMethodInvocation.meth).type.getReturnType();
/* 1464 */       return this.types.unboxedTypeOrType(localType1).isPrimitive();
/*      */     case 7:
/* 1467 */       JCTree.JCExpression localJCExpression1 = (JCTree.JCExpression)this.removeClassParams
/* 1467 */         .translate(((JCTree.JCNewClass)paramJCExpression).clazz);
/*      */ 
/* 1469 */       JCTree.JCExpression localJCExpression2 = (JCTree.JCExpression)this.deferredAttr
/* 1469 */         .attribSpeculative(localJCExpression1, paramEnv, this.unknownTypeInfo);
/*      */ 
/* 1470 */       return this.types.unboxedTypeOrType(localJCExpression2.type).isPrimitive();
/*      */     }
/* 1472 */     Type localType2 = this.deferredAttr.attribSpeculative(paramJCExpression, paramEnv, this.unknownExprInfo).type;
/* 1473 */     localType2 = this.types.unboxedTypeOrType(localType2);
/* 1474 */     return localType2.isPrimitive();
/*      */   }
/*      */ 
/*      */   private Type condType(JCDiagnostic.DiagnosticPosition paramDiagnosticPosition, Type paramType1, Type paramType2)
/*      */   {
/* 1498 */     if (this.types.isSameType(paramType1, paramType2)) {
/* 1499 */       return paramType1.baseType();
/*      */     }
/*      */ 
/* 1502 */     Type localType1 = (!this.allowBoxing) || (paramType1.isPrimitive()) ? paramType1 : this.types
/* 1502 */       .unboxedType(paramType1);
/*      */ 
/* 1504 */     Type localType2 = (!this.allowBoxing) || (paramType2.isPrimitive()) ? paramType2 : this.types
/* 1504 */       .unboxedType(paramType2);
/*      */ 
/* 1510 */     if ((localType1.isPrimitive()) && (localType2.isPrimitive()))
/*      */     {
/* 1514 */       if ((localType1.getTag().isStrictSubRangeOf(TypeTag.INT)) && 
/* 1515 */         (localType2
/* 1515 */         .hasTag(TypeTag.INT)) && 
/* 1516 */         (this.types
/* 1516 */         .isAssignable(localType2, localType1)))
/*      */       {
/* 1517 */         return localType1.baseType();
/*      */       }
/* 1519 */       if ((localType2.getTag().isStrictSubRangeOf(TypeTag.INT)) && 
/* 1520 */         (localType1
/* 1520 */         .hasTag(TypeTag.INT)) && 
/* 1521 */         (this.types
/* 1521 */         .isAssignable(localType1, localType2)))
/*      */       {
/* 1522 */         return localType2.baseType();
/*      */       }
/*      */ 
/* 1525 */       for (TypeTag localTypeTag : primitiveTags) {
/* 1526 */         Type localType3 = this.syms.typeOfTag[localTypeTag.ordinal()];
/* 1527 */         if ((this.types.isSubtype(localType1, localType3)) && 
/* 1528 */           (this.types
/* 1528 */           .isSubtype(localType2, localType3)))
/*      */         {
/* 1529 */           return localType3;
/*      */         }
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/* 1535 */     if (this.allowBoxing) {
/* 1536 */       if (paramType1.isPrimitive())
/* 1537 */         paramType1 = this.types.boxedClass(paramType1).type;
/* 1538 */       if (paramType2.isPrimitive()) {
/* 1539 */         paramType2 = this.types.boxedClass(paramType2).type;
/*      */       }
/*      */     }
/* 1542 */     if (this.types.isSubtype(paramType1, paramType2))
/* 1543 */       return paramType2.baseType();
/* 1544 */     if (this.types.isSubtype(paramType2, paramType1)) {
/* 1545 */       return paramType1.baseType();
/*      */     }
/* 1547 */     if ((!this.allowBoxing) || (paramType1.hasTag(TypeTag.VOID)) || (paramType2.hasTag(TypeTag.VOID))) {
/* 1548 */       this.log.error(paramDiagnosticPosition, "neither.conditional.subtype", new Object[] { paramType1, paramType2 });
/*      */ 
/* 1550 */       return paramType1.baseType();
/*      */     }
/*      */ 
/* 1556 */     return this.types.lub(new Type[] { paramType1.baseType(), paramType2.baseType() });
/*      */   }
/*      */ 
/*      */   public void visitIf(JCTree.JCIf paramJCIf)
/*      */   {
/* 1571 */     attribExpr(paramJCIf.cond, this.env, this.syms.booleanType);
/* 1572 */     attribStat(paramJCIf.thenpart, this.env);
/* 1573 */     if (paramJCIf.elsepart != null)
/* 1574 */       attribStat(paramJCIf.elsepart, this.env);
/* 1575 */     this.chk.checkEmptyIf(paramJCIf);
/* 1576 */     this.result = null;
/*      */   }
/*      */ 
/*      */   public void visitExec(JCTree.JCExpressionStatement paramJCExpressionStatement)
/*      */   {
/* 1582 */     Env localEnv = this.env.dup(paramJCExpressionStatement);
/* 1583 */     attribExpr(paramJCExpressionStatement.expr, localEnv);
/* 1584 */     this.result = null;
/*      */   }
/*      */ 
/*      */   public void visitBreak(JCTree.JCBreak paramJCBreak) {
/* 1588 */     paramJCBreak.target = findJumpTarget(paramJCBreak.pos(), paramJCBreak.getTag(), paramJCBreak.label, this.env);
/* 1589 */     this.result = null;
/*      */   }
/*      */ 
/*      */   public void visitContinue(JCTree.JCContinue paramJCContinue) {
/* 1593 */     paramJCContinue.target = findJumpTarget(paramJCContinue.pos(), paramJCContinue.getTag(), paramJCContinue.label, this.env);
/* 1594 */     this.result = null;
/*      */   }
/*      */ 
/*      */   private JCTree findJumpTarget(JCDiagnostic.DiagnosticPosition paramDiagnosticPosition, JCTree.Tag paramTag, Name paramName, Env<AttrContext> paramEnv)
/*      */   {
/* 1615 */     Object localObject = paramEnv;
/*      */ 
/* 1617 */     while (localObject != null) {
/* 1618 */       switch (14.$SwitchMap$com$sun$tools$javac$tree$JCTree$Tag[localObject.tree.getTag().ordinal()]) {
/*      */       case 8:
/* 1620 */         JCTree.JCLabeledStatement localJCLabeledStatement = (JCTree.JCLabeledStatement)((Env)localObject).tree;
/* 1621 */         if (paramName == localJCLabeledStatement.label)
/*      */         {
/* 1623 */           if (paramTag == JCTree.Tag.CONTINUE) {
/* 1624 */             if ((!localJCLabeledStatement.body.hasTag(JCTree.Tag.DOLOOP)) && 
/* 1625 */               (!localJCLabeledStatement.body
/* 1625 */               .hasTag(JCTree.Tag.WHILELOOP)) && 
/* 1626 */               (!localJCLabeledStatement.body
/* 1626 */               .hasTag(JCTree.Tag.FORLOOP)) && 
/* 1627 */               (!localJCLabeledStatement.body
/* 1627 */               .hasTag(JCTree.Tag.FOREACHLOOP)))
/*      */             {
/* 1628 */               this.log.error(paramDiagnosticPosition, "not.loop.label", new Object[] { paramName });
/*      */             }
/*      */ 
/* 1631 */             return TreeInfo.referencedStatement(localJCLabeledStatement);
/*      */           }
/* 1633 */           return localJCLabeledStatement;
/*      */         }
/*      */ 
/*      */         break;
/*      */       case 9:
/*      */       case 10:
/*      */       case 11:
/*      */       case 12:
/* 1641 */         if (paramName == null) return ((Env)localObject).tree;
/*      */         break;
/*      */       case 13:
/* 1644 */         if ((paramName == null) && (paramTag == JCTree.Tag.BREAK)) return ((Env)localObject).tree;
/*      */         break;
/*      */       case 2:
/*      */       case 14:
/*      */       case 15:
/* 1649 */         break;
/*      */       case 3:
/*      */       case 4:
/*      */       case 5:
/*      */       case 6:
/* 1652 */       case 7: } localObject = ((Env)localObject).next;
/*      */     }
/* 1654 */     if (paramName != null)
/* 1655 */       this.log.error(paramDiagnosticPosition, "undef.label", new Object[] { paramName });
/* 1656 */     else if (paramTag == JCTree.Tag.CONTINUE)
/* 1657 */       this.log.error(paramDiagnosticPosition, "cont.outside.loop", new Object[0]);
/*      */     else
/* 1659 */       this.log.error(paramDiagnosticPosition, "break.outside.switch.loop", new Object[0]);
/* 1660 */     return null;
/*      */   }
/*      */ 
/*      */   public void visitReturn(JCTree.JCReturn paramJCReturn)
/*      */   {
/* 1666 */     if (((AttrContext)this.env.info).returnResult == null) {
/* 1667 */       this.log.error(paramJCReturn.pos(), "ret.outside.meth", new Object[0]);
/*      */     }
/* 1671 */     else if (paramJCReturn.expr != null) {
/* 1672 */       if (((AttrContext)this.env.info).returnResult.pt.hasTag(TypeTag.VOID)) {
/* 1673 */         ((AttrContext)this.env.info).returnResult.checkContext.report(paramJCReturn.expr.pos(), this.diags
/* 1674 */           .fragment("unexpected.ret.val", new Object[0]));
/*      */       }
/*      */ 
/* 1676 */       attribTree(paramJCReturn.expr, this.env, ((AttrContext)this.env.info).returnResult);
/* 1677 */     } else if ((!((AttrContext)this.env.info).returnResult.pt.hasTag(TypeTag.VOID)) && 
/* 1678 */       (!((AttrContext)this.env.info).returnResult.pt
/* 1678 */       .hasTag(TypeTag.NONE)))
/*      */     {
/* 1679 */       ((AttrContext)this.env.info).returnResult.checkContext.report(paramJCReturn.pos(), this.diags
/* 1680 */         .fragment("missing.ret.val", new Object[0]));
/*      */     }
/*      */ 
/* 1683 */     this.result = null;
/*      */   }
/*      */ 
/*      */   public void visitThrow(JCTree.JCThrow paramJCThrow) {
/* 1687 */     Type localType = attribExpr(paramJCThrow.expr, this.env, this.allowPoly ? Type.noType : this.syms.throwableType);
/* 1688 */     if (this.allowPoly) {
/* 1689 */       this.chk.checkType(paramJCThrow, localType, this.syms.throwableType);
/*      */     }
/* 1691 */     this.result = null;
/*      */   }
/*      */ 
/*      */   public void visitAssert(JCTree.JCAssert paramJCAssert) {
/* 1695 */     attribExpr(paramJCAssert.cond, this.env, this.syms.booleanType);
/* 1696 */     if (paramJCAssert.detail != null) {
/* 1697 */       this.chk.checkNonVoid(paramJCAssert.detail.pos(), attribExpr(paramJCAssert.detail, this.env));
/*      */     }
/* 1699 */     this.result = null;
/*      */   }
/*      */ 
/*      */   public void visitApply(JCTree.JCMethodInvocation paramJCMethodInvocation)
/*      */   {
/* 1709 */     Env localEnv = this.env.dup(paramJCMethodInvocation, ((AttrContext)this.env.info).dup());
/*      */ 
/* 1715 */     List localList2 = null;
/*      */ 
/* 1717 */     Name localName = TreeInfo.name(paramJCMethodInvocation.meth);
/*      */ 
/* 1719 */     int i = (localName == this.names._this) || (localName == this.names._super) ? 1 : 0;
/*      */ 
/* 1722 */     ListBuffer localListBuffer = new ListBuffer();
/*      */     List localList1;
/*      */     Type localType2;
/*      */     Object localObject;
/*      */     Type localType4;
/* 1723 */     if (i != 0)
/*      */     {
/* 1726 */       if (checkFirstConstructorStat(paramJCMethodInvocation, this.env))
/*      */       {
/* 1730 */         ((AttrContext)localEnv.info).isSelfCall = true;
/*      */ 
/* 1733 */         attribArgs(paramJCMethodInvocation.args, localEnv, localListBuffer);
/* 1734 */         localList1 = localListBuffer.toList();
/* 1735 */         localList2 = attribTypes(paramJCMethodInvocation.typeargs, localEnv);
/*      */ 
/* 1739 */         Type localType1 = this.env.enclClass.sym.type;
/* 1740 */         if (localName == this.names._super) {
/* 1741 */           if (localType1 == this.syms.objectType) {
/* 1742 */             this.log.error(paramJCMethodInvocation.meth.pos(), "no.superclass", new Object[] { localType1 });
/* 1743 */             localType1 = this.types.createErrorType(this.syms.objectType);
/*      */           } else {
/* 1745 */             localType1 = this.types.supertype(localType1);
/*      */           }
/*      */         }
/*      */ 
/* 1749 */         if (localType1.hasTag(TypeTag.CLASS)) {
/* 1750 */           localType2 = localType1.getEnclosingType();
/* 1751 */           while ((localType2 != null) && (localType2.hasTag(TypeTag.TYPEVAR)))
/* 1752 */             localType2 = localType2.getUpperBound();
/* 1753 */           if (localType2.hasTag(TypeTag.CLASS))
/*      */           {
/* 1756 */             if (paramJCMethodInvocation.meth.hasTag(JCTree.Tag.SELECT)) {
/* 1757 */               JCTree.JCExpression localJCExpression = ((JCTree.JCFieldAccess)paramJCMethodInvocation.meth).selected;
/*      */ 
/* 1763 */               this.chk.checkRefType(localJCExpression.pos(), 
/* 1764 */                 attribExpr(localJCExpression, localEnv, localType2));
/*      */             }
/* 1766 */             else if (localName == this.names._super)
/*      */             {
/* 1769 */               this.rs.resolveImplicitThis(paramJCMethodInvocation.meth.pos(), localEnv, localType1, true);
/*      */             }
/*      */           }
/* 1772 */           else if (paramJCMethodInvocation.meth.hasTag(JCTree.Tag.SELECT)) {
/* 1773 */             this.log.error(paramJCMethodInvocation.meth.pos(), "illegal.qual.not.icls", new Object[] { localType1.tsym });
/*      */           }
/*      */ 
/* 1779 */           if ((localType1.tsym == this.syms.enumSym) && (this.allowEnums)) {
/* 1780 */             localList1 = localList1.prepend(this.syms.intType).prepend(this.syms.stringType);
/*      */           }
/*      */ 
/* 1785 */           boolean bool = ((AttrContext)localEnv.info).selectSuper;
/* 1786 */           ((AttrContext)localEnv.info).selectSuper = true;
/* 1787 */           ((AttrContext)localEnv.info).pendingResolutionPhase = null;
/* 1788 */           localObject = this.rs.resolveConstructor(paramJCMethodInvocation.meth
/* 1789 */             .pos(), localEnv, localType1, localList1, localList2);
/* 1790 */           ((AttrContext)localEnv.info).selectSuper = bool;
/*      */ 
/* 1793 */           TreeInfo.setSymbol(paramJCMethodInvocation.meth, (Symbol)localObject);
/*      */ 
/* 1797 */           localType4 = newMethodTemplate(this.resultInfo.pt, localList1, localList2);
/* 1798 */           checkId(paramJCMethodInvocation.meth, localType1, (Symbol)localObject, localEnv, new ResultInfo(16, localType4));
/*      */         }
/*      */       }
/*      */ 
/* 1802 */       this.result = (paramJCMethodInvocation.type = this.syms.voidType);
/*      */     }
/*      */     else
/*      */     {
/* 1806 */       int j = attribArgs(paramJCMethodInvocation.args, localEnv, localListBuffer);
/* 1807 */       localList1 = localListBuffer.toList();
/* 1808 */       localList2 = attribAnyTypes(paramJCMethodInvocation.typeargs, localEnv);
/*      */ 
/* 1813 */       localType2 = newMethodTemplate(this.resultInfo.pt, localList1, localList2);
/* 1814 */       ((AttrContext)localEnv.info).pendingResolutionPhase = null;
/* 1815 */       Type localType3 = attribTree(paramJCMethodInvocation.meth, localEnv, new ResultInfo(j, localType2, this.resultInfo.checkContext));
/*      */ 
/* 1818 */       localObject = localType3.getReturnType();
/* 1819 */       if (((Type)localObject).hasTag(TypeTag.WILDCARD)) {
/* 1820 */         throw new AssertionError(localType3);
/*      */       }
/* 1822 */       localType4 = paramJCMethodInvocation.meth.hasTag(JCTree.Tag.SELECT) ? ((JCTree.JCFieldAccess)paramJCMethodInvocation.meth).selected.type : this.env.enclClass.sym.type;
/*      */ 
/* 1825 */       localObject = adjustMethodReturnType(localType4, localName, localList1, (Type)localObject);
/*      */ 
/* 1827 */       this.chk.checkRefTypes(paramJCMethodInvocation.typeargs, localList2);
/*      */ 
/* 1831 */       this.result = check(paramJCMethodInvocation, capture((Type)localObject), 12, this.resultInfo);
/*      */     }
/* 1833 */     this.chk.validate(paramJCMethodInvocation.typeargs, localEnv);
/*      */   }
/*      */ 
/*      */   Type adjustMethodReturnType(Type paramType1, Name paramName, List<Type> paramList, Type paramType2) {
/* 1837 */     if ((this.allowCovariantReturns) && (paramName == this.names.clone))
/*      */     {
/* 1839 */       if (this.types
/* 1839 */         .isArray(paramType1))
/*      */       {
/* 1842 */         return paramType1; } 
/* 1843 */     }if ((this.allowGenerics) && (paramName == this.names.getClass))
/*      */     {
/* 1845 */       if (paramList
/* 1845 */         .isEmpty())
/*      */       {
/* 1848 */         return new Type.ClassType(paramType2.getEnclosingType(), 
/* 1848 */           List.of(new Type.WildcardType(this.types
/* 1848 */           .erasure(paramType1), 
/* 1848 */           BoundKind.EXTENDS, this.syms.boundClass)), paramType2.tsym);
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/* 1853 */     return paramType2;
/*      */   }
/*      */ 
/*      */   boolean checkFirstConstructorStat(JCTree.JCMethodInvocation paramJCMethodInvocation, Env<AttrContext> paramEnv)
/*      */   {
/* 1863 */     JCTree.JCMethodDecl localJCMethodDecl = paramEnv.enclMethod;
/* 1864 */     if ((localJCMethodDecl != null) && (localJCMethodDecl.name == this.names.init)) {
/* 1865 */       JCTree.JCBlock localJCBlock = localJCMethodDecl.body;
/* 1866 */       if ((((JCTree.JCStatement)localJCBlock.stats.head).hasTag(JCTree.Tag.EXEC)) && (((JCTree.JCExpressionStatement)localJCBlock.stats.head).expr == paramJCMethodInvocation))
/*      */       {
/* 1868 */         return true;
/*      */       }
/*      */     }
/* 1870 */     this.log.error(paramJCMethodInvocation.pos(), "call.must.be.first.stmt.in.ctor", new Object[] { 
/* 1871 */       TreeInfo.name(paramJCMethodInvocation.meth) });
/*      */ 
/* 1872 */     return false;
/*      */   }
/*      */ 
/*      */   Type newMethodTemplate(Type paramType, List<Type> paramList1, List<Type> paramList2)
/*      */   {
/* 1878 */     Type.MethodType localMethodType = new Type.MethodType(paramList1, paramType, List.nil(), this.syms.methodClass);
/* 1879 */     return paramList2 == null ? localMethodType : new Type.ForAll(paramList2, localMethodType);
/*      */   }
/*      */ 
/*      */   public void visitNewClass(final JCTree.JCNewClass paramJCNewClass) {
/* 1883 */     Object localObject1 = this.types.createErrorType(paramJCNewClass.type);
/*      */ 
/* 1887 */     Env localEnv1 = this.env.dup(paramJCNewClass, ((AttrContext)this.env.info).dup());
/*      */ 
/* 1891 */     JCTree.JCClassDecl localJCClassDecl = paramJCNewClass.def;
/*      */ 
/* 1895 */     Object localObject2 = paramJCNewClass.clazz;
/*      */ 
/* 1898 */     JCTree.JCAnnotatedType localJCAnnotatedType1 = null;
/*      */     Object localObject3;
/* 1900 */     if (((JCTree.JCExpression)localObject2).hasTag(JCTree.Tag.TYPEAPPLY)) {
/* 1901 */       localObject3 = ((JCTree.JCTypeApply)localObject2).clazz;
/* 1902 */       if (((JCTree.JCExpression)localObject3).hasTag(JCTree.Tag.ANNOTATED_TYPE)) {
/* 1903 */         localJCAnnotatedType1 = (JCTree.JCAnnotatedType)localObject3;
/* 1904 */         localObject3 = localJCAnnotatedType1.underlyingType;
/*      */       }
/*      */     }
/* 1907 */     else if (((JCTree.JCExpression)localObject2).hasTag(JCTree.Tag.ANNOTATED_TYPE)) {
/* 1908 */       localJCAnnotatedType1 = (JCTree.JCAnnotatedType)localObject2;
/* 1909 */       localObject3 = localJCAnnotatedType1.underlyingType;
/*      */     } else {
/* 1911 */       localObject3 = localObject2;
/*      */     }
/*      */ 
/* 1915 */     Object localObject4 = localObject3;
/*      */ 
/* 1917 */     if (paramJCNewClass.encl != null)
/*      */     {
/* 1926 */       localType1 = this.chk.checkRefType(paramJCNewClass.encl.pos(), 
/* 1927 */         attribExpr(paramJCNewClass.encl, this.env));
/*      */ 
/* 1930 */       localObject4 = this.make.at(((JCTree.JCExpression)localObject2).pos).Select(this.make.Type(localType1), ((JCTree.JCIdent)localObject3).name);
/*      */ 
/* 1933 */       localObject5 = this.env.toplevel.endPositions;
/* 1934 */       ((EndPosTable)localObject5).storeEnd((JCTree)localObject4, paramJCNewClass.getEndPosition((EndPosTable)localObject5));
/* 1935 */       if (((JCTree.JCExpression)localObject2).hasTag(JCTree.Tag.ANNOTATED_TYPE)) {
/* 1936 */         JCTree.JCAnnotatedType localJCAnnotatedType2 = (JCTree.JCAnnotatedType)localObject2;
/* 1937 */         localList1 = localJCAnnotatedType2.annotations;
/*      */ 
/* 1939 */         if (localJCAnnotatedType2.underlyingType.hasTag(JCTree.Tag.TYPEAPPLY))
/*      */         {
/* 1941 */           localObject4 = this.make.at(paramJCNewClass.pos)
/* 1941 */             .TypeApply((JCTree.JCExpression)localObject4, ((JCTree.JCTypeApply)localObject2).arguments);
/*      */         }
/*      */ 
/* 1946 */         localObject4 = this.make.at(paramJCNewClass.pos)
/* 1946 */           .AnnotatedType(localList1, (JCTree.JCExpression)localObject4);
/*      */       }
/* 1947 */       else if (((JCTree.JCExpression)localObject2).hasTag(JCTree.Tag.TYPEAPPLY))
/*      */       {
/* 1949 */         localObject4 = this.make.at(paramJCNewClass.pos)
/* 1949 */           .TypeApply((JCTree.JCExpression)localObject4, ((JCTree.JCTypeApply)localObject2).arguments);
/*      */       }
/*      */ 
/* 1953 */       localObject2 = localObject4;
/*      */     }
/*      */ 
/* 1960 */     Type localType1 = TreeInfo.isEnumInit(this.env.tree) ? 
/* 1959 */       attribIdentAsEnumType(this.env, (JCTree.JCIdent)localObject2) : 
/* 1960 */       attribType((JCTree)localObject2, this.env);
/*      */ 
/* 1962 */     localType1 = this.chk.checkDiamond(paramJCNewClass, localType1);
/* 1963 */     this.chk.validate((JCTree)localObject2, localEnv1);
/* 1964 */     if (paramJCNewClass.encl != null)
/*      */     {
/* 1967 */       paramJCNewClass.clazz.type = localType1;
/* 1968 */       TreeInfo.setSymbol((JCTree)localObject3, TreeInfo.symbol((JCTree)localObject4));
/* 1969 */       ((JCTree.JCExpression)localObject3).type = ((JCTree.JCIdent)localObject3).sym.type;
/* 1970 */       if (localJCAnnotatedType1 != null) {
/* 1971 */         localJCAnnotatedType1.type = ((JCTree.JCExpression)localObject3).type;
/*      */       }
/* 1973 */       if (!localType1.isErroneous()) {
/* 1974 */         if ((localJCClassDecl != null) && (localType1.tsym.isInterface()))
/* 1975 */           this.log.error(paramJCNewClass.encl.pos(), "anon.class.impl.intf.no.qual.for.new", new Object[0]);
/* 1976 */         else if (localType1.tsym.isStatic())
/* 1977 */           this.log.error(paramJCNewClass.encl.pos(), "qualified.new.of.static.class", new Object[] { localType1.tsym });
/*      */       }
/*      */     }
/* 1980 */     else if ((!localType1.tsym.isInterface()) && 
/* 1981 */       (localType1
/* 1981 */       .getEnclosingType().hasTag(TypeTag.CLASS)))
/*      */     {
/* 1983 */       this.rs.resolveImplicitThis(paramJCNewClass.pos(), this.env, localType1);
/*      */     }
/*      */ 
/* 1987 */     Object localObject5 = new ListBuffer();
/* 1988 */     int i = attribArgs(paramJCNewClass.args, localEnv1, (ListBuffer)localObject5);
/* 1989 */     List localList1 = ((ListBuffer)localObject5).toList();
/* 1990 */     List localList2 = attribTypes(paramJCNewClass.typeargs, localEnv1);
/*      */ 
/* 1993 */     if (localType1.hasTag(TypeTag.CLASS))
/*      */     {
/* 1995 */       if ((this.allowEnums) && ((localType1.tsym.flags_field & 0x4000) != 0L))
/*      */       {
/* 1997 */         if ((!this.env.tree
/* 1997 */           .hasTag(JCTree.Tag.VARDEF)) || 
/* 1997 */           ((((JCTree.JCVariableDecl)this.env.tree).mods.flags & 0x4000) == 0L) || (((JCTree.JCVariableDecl)this.env.tree).init != paramJCNewClass))
/*      */         {
/* 2000 */           this.log.error(paramJCNewClass.pos(), "enum.cant.be.instantiated", new Object[0]);
/*      */         }
/*      */       }
/*      */       Object localObject6;
/* 2002 */       if ((localJCClassDecl == null) && 
/* 2003 */         ((localType1.tsym
/* 2003 */         .flags() & 0x600) != 0L)) {
/* 2004 */         this.log.error(paramJCNewClass.pos(), "abstract.cant.be.instantiated", new Object[] { localType1.tsym });
/*      */       }
/* 2006 */       else if ((localJCClassDecl != null) && (localType1.tsym.isInterface()))
/*      */       {
/* 2009 */         if (!localList1.isEmpty()) {
/* 2010 */           this.log.error(((JCTree.JCExpression)paramJCNewClass.args.head).pos(), "anon.class.impl.intf.no.args", new Object[0]);
/*      */         }
/* 2012 */         if (!localList2.isEmpty()) {
/* 2013 */           this.log.error(((JCTree.JCExpression)paramJCNewClass.typeargs.head).pos(), "anon.class.impl.intf.no.typeargs", new Object[0]);
/*      */         }
/*      */ 
/* 2016 */         localList1 = List.nil();
/* 2017 */         localList2 = List.nil();
/* 2018 */       } else if (TreeInfo.isDiamond(paramJCNewClass))
/*      */       {
/* 2020 */         localObject6 = new Type.ClassType(localType1.getEnclosingType(), localType1.tsym.type
/* 2020 */           .getTypeArguments(), localType1.tsym);
/*      */ 
/* 2023 */         Env localEnv2 = localEnv1.dup(paramJCNewClass);
/* 2024 */         ((AttrContext)localEnv2.info).selectSuper = (localJCClassDecl != null);
/* 2025 */         ((AttrContext)localEnv2.info).pendingResolutionPhase = null;
/*      */ 
/* 2030 */         Symbol localSymbol = this.rs.resolveDiamond(paramJCNewClass.pos(), localEnv2, (Type)localObject6, localList1, localList2);
/*      */ 
/* 2035 */         paramJCNewClass.constructor = localSymbol.baseSymbol();
/*      */ 
/* 2037 */         final Symbol.TypeSymbol localTypeSymbol = localType1.tsym;
/* 2038 */         ResultInfo localResultInfo = new ResultInfo(i, newMethodTemplate(this.resultInfo.pt, localList1, localList2), new Check.NestedCheckContext(this.resultInfo.checkContext)
/*      */         {
/*      */           public void report(JCDiagnostic.DiagnosticPosition paramAnonymousDiagnosticPosition, JCDiagnostic paramAnonymousJCDiagnostic) {
/* 2041 */             this.enclosingContext.report(paramJCNewClass.clazz, Attr.this.diags
/* 2042 */               .fragment("cant.apply.diamond.1", new Object[] { Attr.this.diags
/* 2042 */               .fragment("diamond", new Object[] { localTypeSymbol }), 
/* 2042 */               paramAnonymousJCDiagnostic }));
/*      */           }
/*      */         });
/* 2045 */         Type localType2 = paramJCNewClass.constructorType = this.types.createErrorType(localType1);
/* 2046 */         localType2 = checkId(paramJCNewClass, (Type)localObject6, localSymbol, localEnv2, localResultInfo);
/*      */ 
/* 2051 */         paramJCNewClass.clazz.type = this.types.createErrorType(localType1);
/* 2052 */         if (!localType2.isErroneous()) {
/* 2053 */           paramJCNewClass.clazz.type = (localType1 = localType2.getReturnType());
/* 2054 */           paramJCNewClass.constructorType = this.types.createMethodTypeWithReturn(localType2, this.syms.voidType);
/*      */         }
/* 2056 */         localType1 = this.chk.checkClassType(paramJCNewClass.clazz, paramJCNewClass.clazz.type, true);
/*      */       }
/*      */       else
/*      */       {
/* 2066 */         localObject6 = localEnv1.dup(paramJCNewClass);
/* 2067 */         ((AttrContext)((Env)localObject6).info).selectSuper = (localJCClassDecl != null);
/* 2068 */         ((AttrContext)((Env)localObject6).info).pendingResolutionPhase = null;
/* 2069 */         paramJCNewClass.constructor = this.rs.resolveConstructor(paramJCNewClass
/* 2070 */           .pos(), (Env)localObject6, localType1, localList1, localList2);
/* 2071 */         if (localJCClassDecl == null) {
/* 2072 */           paramJCNewClass.constructorType = checkId(paramJCNewClass, localType1, paramJCNewClass.constructor, (Env)localObject6, new ResultInfo(i, 
/* 2076 */             newMethodTemplate(this.syms.voidType, localList1, localList2)));
/*      */ 
/* 2077 */           if (((AttrContext)((Env)localObject6).info).lastResolveVarargs())
/* 2078 */             Assert.check((paramJCNewClass.constructorType.isErroneous()) || (paramJCNewClass.varargsElement != null));
/*      */         }
/* 2080 */         if ((localJCClassDecl == null) && 
/* 2081 */           (!localType1
/* 2081 */           .isErroneous()) && 
/* 2082 */           (localType1
/* 2082 */           .getTypeArguments().nonEmpty()) && (this.findDiamonds))
/*      */         {
/* 2084 */           findDiamond(localEnv1, paramJCNewClass, localType1);
/*      */         }
/*      */       }
/*      */ 
/* 2088 */       if (localJCClassDecl != null)
/*      */       {
/* 2121 */         if (Resolve.isStatic(this.env)) localJCClassDecl.mods.flags |= 8L;
/*      */ 
/* 2123 */         if (localType1.tsym.isInterface())
/* 2124 */           localJCClassDecl.implementing = List.of(localObject2);
/*      */         else {
/* 2126 */           localJCClassDecl.extending = ((JCTree.JCExpression)localObject2);
/*      */         }
/*      */ 
/* 2129 */         if ((this.resultInfo.checkContext.deferredAttrContext().mode == DeferredAttr.AttrMode.CHECK) && 
/* 2130 */           (isSerializable(localType1)))
/*      */         {
/* 2131 */           ((AttrContext)localEnv1.info).isSerializable = true;
/*      */         }
/*      */ 
/* 2134 */         attribStat(localJCClassDecl, localEnv1);
/*      */ 
/* 2136 */         checkLambdaCandidate(paramJCNewClass, localJCClassDecl.sym, localType1);
/*      */ 
/* 2141 */         if ((paramJCNewClass.encl != null) && (!localType1.tsym.isInterface())) {
/* 2142 */           paramJCNewClass.args = paramJCNewClass.args.prepend(makeNullCheck(paramJCNewClass.encl));
/* 2143 */           localList1 = localList1.prepend(paramJCNewClass.encl.type);
/* 2144 */           paramJCNewClass.encl = null;
/*      */         }
/*      */ 
/* 2148 */         localType1 = localJCClassDecl.sym.type;
/* 2149 */         localObject6 = paramJCNewClass.constructor = this.rs.resolveConstructor(paramJCNewClass
/* 2150 */           .pos(), localEnv1, localType1, localList1, localList2);
/* 2151 */         Assert.check(((Symbol)localObject6).kind < 129);
/* 2152 */         paramJCNewClass.constructor = ((Symbol)localObject6);
/* 2153 */         paramJCNewClass.constructorType = checkId(paramJCNewClass, localType1, paramJCNewClass.constructor, localEnv1, new ResultInfo(i, 
/* 2157 */           newMethodTemplate(this.syms.voidType, localList1, localList2)));
/*      */       }
/*      */ 
/* 2160 */       if ((paramJCNewClass.constructor != null) && (paramJCNewClass.constructor.kind == 16))
/* 2161 */         localObject1 = localType1;
/*      */     }
/* 2163 */     this.result = check(paramJCNewClass, (Type)localObject1, 12, this.resultInfo);
/* 2164 */     this.chk.validate(paramJCNewClass.typeargs, localEnv1);
/*      */   }
/*      */ 
/*      */   void findDiamond(Env<AttrContext> paramEnv, JCTree.JCNewClass paramJCNewClass, Type paramType) {
/* 2168 */     JCTree.JCTypeApply localJCTypeApply = (JCTree.JCTypeApply)paramJCNewClass.clazz;
/* 2169 */     List localList = localJCTypeApply.arguments;
/*      */     try
/*      */     {
/* 2172 */       localJCTypeApply.arguments = List.nil();
/*      */ 
/* 2174 */       ResultInfo localResultInfo = new ResultInfo(12, this.resultInfo.checkContext
/* 2174 */         .inferenceContext().free(this.resultInfo.pt) ? Type.noType : pt());
/* 2175 */       Type localType1 = this.deferredAttr.attribSpeculative(paramJCNewClass, paramEnv, localResultInfo).type;
/* 2176 */       Type localType2 = this.allowPoly ? this.syms.objectType : paramType;
/*      */ 
/* 2179 */       if (!localType1.isErroneous()) { if ((this.allowPoly) && 
/* 2180 */           (pt() == Infer.anyPoly))
/* 2181 */           if (!this.types
/* 2181 */             .isSameType(localType1, paramType))
/*      */             break label242;
/* 2182 */         else if (!this.types
/* 2182 */             .isAssignable(localType1, 
/* 2182 */             pt().hasTag(TypeTag.NONE) ? localType2 : pt(), this.types.noWarnings))
/*      */             break label242; String str = this.types.isSameType(paramType, localType1) ? "diamond.redundant.args" : "diamond.redundant.args.1";
/*      */ 
/* 2186 */         this.log.warning(paramJCNewClass.clazz.pos(), str, new Object[] { paramType, localType1 }); }
/*      */     }
/*      */     finally {
/* 2189 */       label242: localJCTypeApply.arguments = localList;
/*      */     }
/*      */   }
/*      */ 
/*      */   private void checkLambdaCandidate(JCTree.JCNewClass paramJCNewClass, Symbol.ClassSymbol paramClassSymbol, Type paramType) {
/* 2194 */     if ((this.allowLambda) && (this.identifyLambdaCandidate))
/*      */     {
/* 2196 */       if ((paramType
/* 2196 */         .hasTag(TypeTag.CLASS)) && 
/* 2197 */         (!pt().hasTag(TypeTag.NONE)) && 
/* 2198 */         (this.types
/* 2198 */         .isFunctionalInterface(paramType.tsym)))
/*      */       {
/* 2199 */         Symbol localSymbol1 = this.types.findDescriptorSymbol(paramType.tsym);
/* 2200 */         int i = 0;
/* 2201 */         int j = 0;
/* 2202 */         for (Symbol localSymbol2 : paramClassSymbol.members().getElements())
/* 2203 */           if (((localSymbol2.flags() & 0x1000) == 0L) && 
/* 2204 */             (!localSymbol2
/* 2204 */             .isConstructor())) {
/* 2205 */             i++;
/* 2206 */             if ((localSymbol2.kind == 16) && 
/* 2207 */               (localSymbol2.name
/* 2207 */               .equals(localSymbol1.name)))
/*      */             {
/* 2208 */               Type localType = this.types.memberType(paramType, localSymbol2);
/* 2209 */               if (this.types.overrideEquivalent(localType, this.types.memberType(paramType, localSymbol1)))
/* 2210 */                 j = 1;
/*      */             }
/*      */           }
/* 2213 */         if ((j != 0) && (i == 1))
/* 2214 */           this.log.note(paramJCNewClass.def, "potential.lambda.found", new Object[0]);
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   public JCTree.JCExpression makeNullCheck(JCTree.JCExpression paramJCExpression)
/*      */   {
/* 2223 */     Name localName = TreeInfo.name(paramJCExpression);
/* 2224 */     if ((localName == this.names._this) || (localName == this.names._super)) return paramJCExpression;
/*      */ 
/* 2226 */     JCTree.Tag localTag = JCTree.Tag.NULLCHK;
/* 2227 */     JCTree.JCUnary localJCUnary = this.make.at(paramJCExpression.pos).Unary(localTag, paramJCExpression);
/* 2228 */     localJCUnary.operator = this.syms.nullcheck;
/* 2229 */     localJCUnary.type = paramJCExpression.type;
/* 2230 */     return localJCUnary;
/*      */   }
/*      */ 
/*      */   public void visitNewArray(JCTree.JCNewArray paramJCNewArray) {
/* 2234 */     Object localObject = this.types.createErrorType(paramJCNewArray.type);
/* 2235 */     Env localEnv = this.env.dup(paramJCNewArray);
/*      */     Type localType;
/* 2237 */     if (paramJCNewArray.elemtype != null) {
/* 2238 */       localType = attribType(paramJCNewArray.elemtype, localEnv);
/* 2239 */       this.chk.validate(paramJCNewArray.elemtype, localEnv);
/* 2240 */       localObject = localType;
/* 2241 */       for (List localList = paramJCNewArray.dims; localList.nonEmpty(); localList = localList.tail) {
/* 2242 */         attribExpr((JCTree)localList.head, localEnv, this.syms.intType);
/* 2243 */         localObject = new Type.ArrayType((Type)localObject, this.syms.arrayClass);
/*      */       }
/*      */ 
/*      */     }
/* 2248 */     else if (pt().hasTag(TypeTag.ARRAY)) {
/* 2249 */       localType = this.types.elemtype(pt());
/*      */     } else {
/* 2251 */       if (!pt().hasTag(TypeTag.ERROR)) {
/* 2252 */         this.log.error(paramJCNewArray.pos(), "illegal.initializer.for.type", new Object[] { 
/* 2253 */           pt() });
/*      */       }
/* 2255 */       localType = this.types.createErrorType(pt());
/*      */     }
/*      */ 
/* 2258 */     if (paramJCNewArray.elems != null) {
/* 2259 */       attribExprs(paramJCNewArray.elems, localEnv, localType);
/* 2260 */       localObject = new Type.ArrayType(localType, this.syms.arrayClass);
/*      */     }
/* 2262 */     if (!this.types.isReifiable(localType))
/* 2263 */       this.log.error(paramJCNewArray.pos(), "generic.array.creation", new Object[0]);
/* 2264 */     this.result = check(paramJCNewArray, (Type)localObject, 12, this.resultInfo);
/*      */   }
/*      */ 
/*      */   public void visitLambda(JCTree.JCLambda paramJCLambda)
/*      */   {
/* 2275 */     if ((pt().isErroneous()) || ((pt().hasTag(TypeTag.NONE)) && (pt() != Type.recoveryType))) {
/* 2276 */       if (pt().hasTag(TypeTag.NONE))
/*      */       {
/* 2278 */         this.log.error(paramJCLambda.pos(), "unexpected.lambda", new Object[0]);
/*      */       }
/* 2280 */       this.result = (paramJCLambda.type = this.types.createErrorType(pt()));
/* 2281 */       return;
/*      */     }
/*      */ 
/* 2284 */     Env localEnv = lambdaEnv(paramJCLambda, this.env);
/*      */ 
/* 2286 */     int i = this.resultInfo.checkContext
/* 2286 */       .deferredAttrContext().mode == DeferredAttr.AttrMode.CHECK ? 1 : 0;
/*      */     try {
/* 2288 */       Object localObject1 = pt();
/* 2289 */       if ((i != 0) && (isSerializable((Type)localObject1))) {
/* 2290 */         ((AttrContext)localEnv.info).isSerializable = true;
/*      */       }
/* 2292 */       localObject2 = null;
/* 2293 */       if (paramJCLambda.paramKind == JCTree.JCLambda.ParameterKind.EXPLICIT)
/*      */       {
/* 2295 */         attribStats(paramJCLambda.params, localEnv);
/* 2296 */         localObject2 = TreeInfo.types(paramJCLambda.params);
/*      */       }
/*      */       Type localType;
/* 2300 */       if (pt() != Type.recoveryType)
/*      */       {
/* 2305 */         localObject1 = (Type)this.targetChecker.visit((Type)localObject1, paramJCLambda);
/* 2306 */         if (localObject2 != null) {
/* 2307 */           localObject1 = this.infer.instantiateFunctionalInterface(paramJCLambda, (Type)localObject1, (List)localObject2, this.resultInfo.checkContext);
/*      */         }
/*      */ 
/* 2310 */         localObject1 = this.types.removeWildcards((Type)localObject1);
/* 2311 */         localType = this.types.findDescriptorType((Type)localObject1);
/*      */       } else {
/* 2313 */         localObject1 = Type.recoveryType;
/* 2314 */         localType = fallbackDescriptorType(paramJCLambda);
/*      */       }
/*      */ 
/* 2317 */       setFunctionalInfo(localEnv, paramJCLambda, pt(), localType, (Type)localObject1, this.resultInfo.checkContext);
/*      */ 
/* 2319 */       if (localType.hasTag(TypeTag.FORALL))
/*      */       {
/* 2321 */         this.resultInfo.checkContext.report(paramJCLambda, this.diags.fragment("invalid.generic.lambda.target", new Object[] { localType, 
/* 2322 */           Kinds.kindName(((Type)localObject1).tsym), 
/* 2322 */           ((Type)localObject1).tsym }));
/* 2323 */         this.result = (paramJCLambda.type = this.types.createErrorType(pt()));
/*      */         return;
/*      */       }
/*      */       Object localObject5;
/* 2327 */       if (paramJCLambda.paramKind == JCTree.JCLambda.ParameterKind.IMPLICIT)
/*      */       {
/* 2329 */         localObject3 = localType.getParameterTypes();
/* 2330 */         localObject4 = paramJCLambda.params;
/*      */ 
/* 2332 */         int j = 0;
/*      */ 
/* 2334 */         while (((List)localObject4).nonEmpty()) {
/* 2335 */           if (((List)localObject3).isEmpty())
/*      */           {
/* 2337 */             j = 1;
/*      */           }
/*      */ 
/* 2340 */           localObject5 = j != 0 ? this.syms.errType : (Type)((List)localObject3).head;
/*      */ 
/* 2343 */           ((JCTree.JCVariableDecl)((List)localObject4).head).vartype = this.make.at((JCDiagnostic.DiagnosticPosition)((List)localObject4).head).Type((Type)localObject5);
/* 2344 */           ((JCTree.JCVariableDecl)((List)localObject4).head).sym = null;
/* 2345 */           localObject3 = ((List)localObject3).isEmpty() ? localObject3 : ((List)localObject3).tail;
/*      */ 
/* 2348 */           localObject4 = ((List)localObject4).tail;
/*      */         }
/*      */ 
/* 2352 */         attribStats(paramJCLambda.params, localEnv);
/*      */ 
/* 2354 */         if (j != 0) {
/* 2355 */           this.resultInfo.checkContext.report(paramJCLambda, this.diags.fragment("incompatible.arg.types.in.lambda", new Object[0]));
/* 2356 */           this.result = (paramJCLambda.type = this.types.createErrorType((Type)localObject1));
/* 2357 */           return;
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/* 2365 */       i = 0;
/*      */ 
/* 2368 */       Object localObject3 = paramJCLambda.getBodyKind() == LambdaExpressionTree.BodyKind.EXPRESSION ? new ExpressionLambdaReturnContext(
/* 2368 */         (JCTree.JCExpression)paramJCLambda
/* 2368 */         .getBody(), this.resultInfo.checkContext) : new FunctionalReturnContext(this.resultInfo.checkContext);
/*      */ 
/* 2373 */       Object localObject4 = localType.getReturnType() == Type.recoveryType ? this.recoveryInfo : new ResultInfo(12, localType
/* 2373 */         .getReturnType(), (Check.CheckContext)localObject3);
/* 2374 */       ((AttrContext)localEnv.info).returnResult = ((ResultInfo)localObject4);
/*      */ 
/* 2376 */       if (paramJCLambda.getBodyKind() == LambdaExpressionTree.BodyKind.EXPRESSION) {
/* 2377 */         attribTree(paramJCLambda.getBody(), localEnv, (ResultInfo)localObject4);
/*      */       } else {
/* 2379 */         JCTree.JCBlock localJCBlock = (JCTree.JCBlock)paramJCLambda.body;
/* 2380 */         attribStats(localJCBlock.stats, localEnv);
/*      */       }
/*      */ 
/* 2383 */       this.result = check(paramJCLambda, (Type)localObject1, 12, this.resultInfo);
/*      */ 
/* 2386 */       boolean bool = this.resultInfo.checkContext
/* 2386 */         .deferredAttrContext().mode == DeferredAttr.AttrMode.SPECULATIVE;
/*      */ 
/* 2388 */       preFlow(paramJCLambda);
/* 2389 */       this.flow.analyzeLambda(this.env, paramJCLambda, this.make, bool);
/*      */ 
/* 2391 */       checkLambdaCompatible(paramJCLambda, localType, this.resultInfo.checkContext);
/*      */ 
/* 2393 */       if (!bool)
/*      */       {
/* 2395 */         if (this.resultInfo.checkContext.inferenceContext().free(localType.getThrownTypes())) {
/* 2396 */           localObject5 = this.flow.analyzeLambdaThrownTypes(this.env, paramJCLambda, this.make);
/* 2397 */           List localList = this.resultInfo.checkContext.inferenceContext().asUndetVars(localType.getThrownTypes());
/*      */ 
/* 2399 */           this.chk.unhandled((List)localObject5, localList);
/*      */         }
/*      */ 
/* 2402 */         checkAccessibleTypes(paramJCLambda, localEnv, this.resultInfo.checkContext.inferenceContext(), new Type[] { localType, localObject1 });
/*      */       }
/* 2404 */       this.result = check(paramJCLambda, (Type)localObject1, 12, this.resultInfo);
/*      */     } catch (Types.FunctionDescriptorLookupError localFunctionDescriptorLookupError) {
/* 2406 */       Object localObject2 = localFunctionDescriptorLookupError.getDiagnostic();
/* 2407 */       this.resultInfo.checkContext.report(paramJCLambda, (JCDiagnostic)localObject2);
/* 2408 */       this.result = (paramJCLambda.type = this.types.createErrorType(pt()));
/* 2409 */       return;
/*      */     } finally {
/* 2411 */       ((AttrContext)localEnv.info).scope.leave();
/* 2412 */       if (i != 0)
/* 2413 */         attribTree(paramJCLambda, this.env, this.recoveryInfo);
/*      */     }
/*      */   }
/*      */ 
/*      */   void preFlow(JCTree.JCLambda paramJCLambda)
/*      */   {
/*      */     // Byte code:
/*      */     //   0: new 1211	com/sun/tools/javac/comp/Attr$6
/*      */     //   3: dup
/*      */     //   4: aload_0
/*      */     //   5: invokespecial 3193	com/sun/tools/javac/comp/Attr$6:<init>	(Lcom/sun/tools/javac/comp/Attr;)V
/*      */     //   8: aload_1
/*      */     //   9: invokevirtual 3194	com/sun/tools/javac/comp/Attr$6:scan	(Lcom/sun/tools/javac/tree/JCTree;)V
/*      */     //   12: return
/*      */   }
/*      */ 
/*      */   private Type fallbackDescriptorType(JCTree.JCExpression paramJCExpression)
/*      */   {
/* 2481 */     switch (paramJCExpression.getTag()) {
/*      */     case LAMBDA:
/* 2483 */       JCTree.JCLambda localJCLambda = (JCTree.JCLambda)paramJCExpression;
/* 2484 */       List localList = List.nil();
/* 2485 */       for (JCTree.JCVariableDecl localJCVariableDecl : localJCLambda.params)
/*      */       {
/* 2488 */         localList = localJCVariableDecl.vartype != null ? localList
/* 2487 */           .append(localJCVariableDecl.vartype.type) : 
/* 2487 */           localList
/* 2488 */           .append(this.syms.errType);
/*      */       }
/*      */ 
/* 2491 */       return new Type.MethodType(localList, Type.recoveryType, 
/* 2491 */         List.of(this.syms.throwableType), 
/* 2491 */         this.syms.methodClass);
/*      */     case REFERENCE:
/* 2494 */       return new Type.MethodType(List.nil(), Type.recoveryType, 
/* 2494 */         List.of(this.syms.throwableType), 
/* 2494 */         this.syms.methodClass);
/*      */     }
/* 2496 */     Assert.error("Cannot get here!");
/*      */ 
/* 2498 */     return null;
/*      */   }
/*      */ 
/*      */   private void checkAccessibleTypes(JCDiagnostic.DiagnosticPosition paramDiagnosticPosition, Env<AttrContext> paramEnv, Infer.InferenceContext paramInferenceContext, Type[] paramArrayOfType)
/*      */   {
/* 2503 */     checkAccessibleTypes(paramDiagnosticPosition, paramEnv, paramInferenceContext, List.from(paramArrayOfType));
/*      */   }
/*      */ 
/*      */   private void checkAccessibleTypes(final JCDiagnostic.DiagnosticPosition paramDiagnosticPosition, final Env<AttrContext> paramEnv, Infer.InferenceContext paramInferenceContext, final List<Type> paramList)
/*      */   {
/* 2508 */     if (paramInferenceContext.free(paramList))
/* 2509 */       paramInferenceContext.addFreeTypeListener(paramList, new Infer.FreeTypeListener()
/*      */       {
/*      */         public void typesInferred(Infer.InferenceContext paramAnonymousInferenceContext) {
/* 2512 */           Attr.this.checkAccessibleTypes(paramDiagnosticPosition, paramEnv, paramAnonymousInferenceContext, paramAnonymousInferenceContext.asInstTypes(paramList));
/*      */         }
/*      */       });
/*      */     else
/* 2516 */       for (Type localType : paramList)
/* 2517 */         this.rs.checkAccessibleType(paramEnv, localType);
/*      */   }
/*      */ 
/*      */   private void checkLambdaCompatible(JCTree.JCLambda paramJCLambda, Type paramType, Check.CheckContext paramCheckContext)
/*      */   {
/* 2570 */     Type localType = paramCheckContext.inferenceContext().asUndetVar(paramType.getReturnType());
/*      */ 
/* 2576 */     if ((paramJCLambda.getBodyKind() == LambdaExpressionTree.BodyKind.STATEMENT) && (paramJCLambda.canCompleteNormally) && 
/* 2577 */       (!localType
/* 2577 */       .hasTag(TypeTag.VOID)) && 
/* 2577 */       (localType != Type.recoveryType)) {
/* 2578 */       paramCheckContext.report(paramJCLambda, this.diags.fragment("incompatible.ret.type.in.lambda", new Object[] { this.diags
/* 2579 */         .fragment("missing.ret.val", new Object[] { localType }) }));
/*      */     }
/*      */ 
/* 2582 */     List localList = paramCheckContext.inferenceContext().asUndetVars(paramType.getParameterTypes());
/* 2583 */     if (!this.types.isSameTypes(localList, TreeInfo.types(paramJCLambda.params)))
/* 2584 */       paramCheckContext.report(paramJCLambda, this.diags.fragment("incompatible.arg.types.in.lambda", new Object[0]));
/*      */   }
/*      */ 
/*      */   public Symbol.MethodSymbol removeClinit(Symbol.ClassSymbol paramClassSymbol)
/*      */   {
/* 2599 */     return (Symbol.MethodSymbol)this.clinits.remove(paramClassSymbol);
/*      */   }
/*      */ 
/*      */   public Env<AttrContext> lambdaEnv(JCTree.JCLambda paramJCLambda, Env<AttrContext> paramEnv)
/*      */   {
/* 2614 */     Symbol localSymbol = ((AttrContext)paramEnv.info).scope.owner;
/*      */     Env localEnv;
/* 2615 */     if ((localSymbol.kind == 4) && (localSymbol.owner.kind == 2))
/*      */     {
/* 2617 */       localEnv = paramEnv.dup(paramJCLambda, ((AttrContext)paramEnv.info).dup(((AttrContext)paramEnv.info).scope.dupUnshared()));
/* 2618 */       Symbol.ClassSymbol localClassSymbol = localSymbol.enclClass();
/*      */       Object localObject1;
/*      */       Object localObject2;
/* 2623 */       if ((localSymbol.flags() & 0x8) == 0L) {
/* 2624 */         localObject1 = localClassSymbol.members_field.getElementsByName(this.names.init).iterator(); if (((Iterator)localObject1).hasNext()) { localObject2 = (Symbol)((Iterator)localObject1).next();
/* 2625 */           ((AttrContext)localEnv.info).scope.owner = ((Symbol)localObject2);
/*      */         }
/*      */ 
/*      */       }
/*      */       else
/*      */       {
/* 2632 */         localObject1 = (Symbol.MethodSymbol)this.clinits.get(localClassSymbol);
/* 2633 */         if (localObject1 == null)
/*      */         {
/* 2635 */           localObject2 = new Type.MethodType(List.nil(), this.syms.voidType, 
/* 2635 */             List.nil(), this.syms.methodClass);
/* 2636 */           localObject1 = new Symbol.MethodSymbol(4106L, this.names.clinit, (Type)localObject2, localClassSymbol);
/*      */ 
/* 2638 */           ((Symbol.MethodSymbol)localObject1).params = List.nil();
/* 2639 */           this.clinits.put(localClassSymbol, localObject1);
/*      */         }
/* 2641 */         ((AttrContext)localEnv.info).scope.owner = ((Symbol)localObject1);
/*      */       }
/*      */     } else {
/* 2644 */       localEnv = paramEnv.dup(paramJCLambda, ((AttrContext)paramEnv.info).dup(((AttrContext)paramEnv.info).scope.dup()));
/*      */     }
/* 2646 */     return localEnv;
/*      */   }
/*      */ 
/*      */   public void visitReference(JCTree.JCMemberReference paramJCMemberReference)
/*      */   {
/* 2651 */     if ((pt().isErroneous()) || ((pt().hasTag(TypeTag.NONE)) && (pt() != Type.recoveryType))) {
/* 2652 */       if (pt().hasTag(TypeTag.NONE))
/*      */       {
/* 2654 */         this.log.error(paramJCMemberReference.pos(), "unexpected.mref", new Object[0]);
/*      */       }
/* 2656 */       this.result = (paramJCMemberReference.type = this.types.createErrorType(pt()));
/* 2657 */       return;
/*      */     }
/* 2659 */     Env localEnv = this.env.dup(paramJCMemberReference);
/*      */     try
/*      */     {
/* 2663 */       Type localType1 = attribTree(paramJCMemberReference.expr, this.env, memberReferenceQualifierResult(paramJCMemberReference));
/*      */ 
/* 2665 */       if (paramJCMemberReference.getMode() == MemberReferenceTree.ReferenceMode.NEW) {
/* 2666 */         localType1 = this.chk.checkConstructorRefType(paramJCMemberReference.expr, localType1);
/* 2667 */         if ((!localType1.isErroneous()) && 
/* 2668 */           (localType1
/* 2668 */           .isRaw()) && (paramJCMemberReference.typeargs != null))
/*      */         {
/* 2670 */           this.log.error(paramJCMemberReference.expr.pos(), "invalid.mref", new Object[] { Kinds.kindName(paramJCMemberReference.getMode()), this.diags
/* 2671 */             .fragment("mref.infer.and.explicit.params", new Object[0]) });
/*      */ 
/* 2672 */           localType1 = this.types.createErrorType(localType1);
/*      */         }
/*      */       }
/*      */ 
/* 2676 */       if (localType1.isErroneous())
/*      */       {
/* 2679 */         this.result = (paramJCMemberReference.type = localType1);
/* 2680 */         return;
/*      */       }
/*      */ 
/* 2683 */       if (TreeInfo.isStaticSelector(paramJCMemberReference.expr, this.names))
/*      */       {
/* 2687 */         this.chk.validate(paramJCMemberReference.expr, this.env, false);
/*      */       }
/*      */ 
/* 2691 */       localObject1 = List.nil();
/* 2692 */       if (paramJCMemberReference.typeargs != null) {
/* 2693 */         localObject1 = attribTypes(paramJCMemberReference.typeargs, localEnv);
/*      */       }
/*      */ 
/* 2697 */       Object localObject2 = pt();
/*      */ 
/* 2700 */       int i = (this.resultInfo.checkContext
/* 2699 */         .deferredAttrContext().mode == DeferredAttr.AttrMode.CHECK) && 
/* 2700 */         (isSerializable((Type)localObject2)) ? 
/* 2700 */         1 : 0;
/*      */       Type localType2;
/* 2701 */       if (localObject2 != Type.recoveryType) {
/* 2702 */         localObject2 = this.types.removeWildcards((Type)this.targetChecker.visit((Type)localObject2, paramJCMemberReference));
/* 2703 */         localType2 = this.types.findDescriptorType((Type)localObject2);
/*      */       } else {
/* 2705 */         localObject2 = Type.recoveryType;
/* 2706 */         localType2 = fallbackDescriptorType(paramJCMemberReference);
/*      */       }
/*      */ 
/* 2709 */       setFunctionalInfo(localEnv, paramJCMemberReference, pt(), localType2, (Type)localObject2, this.resultInfo.checkContext);
/* 2710 */       List localList1 = localType2.getParameterTypes();
/* 2711 */       Object localObject3 = this.rs.resolveMethodCheck;
/*      */ 
/* 2713 */       if (this.resultInfo.checkContext.inferenceContext().free(localList1))
/*      */       {
/*      */         Resolve tmp448_445 = this.rs; tmp448_445.getClass(); localObject3 = new Resolve.MethodReferenceCheck(tmp448_445, this.resultInfo.checkContext.inferenceContext());
/*      */       }
/*      */ 
/* 2717 */       Pair localPair = null;
/* 2718 */       List localList2 = this.resultInfo.checkContext.inferenceContext().save();
/*      */       try {
/* 2720 */         localPair = this.rs.resolveMemberReference(localEnv, paramJCMemberReference, paramJCMemberReference.expr.type, paramJCMemberReference.name, localList1, (List)localObject1, (Resolve.MethodCheck)localObject3, this.resultInfo.checkContext
/* 2722 */           .inferenceContext(), this.resultInfo.checkContext
/* 2723 */           .deferredAttrContext().mode);
/*      */       } finally {
/* 2725 */         this.resultInfo.checkContext.inferenceContext().rollback(localList2);
/*      */       }
/*      */ 
/* 2728 */       Symbol localSymbol = (Symbol)localPair.fst;
/* 2729 */       Resolve.ReferenceLookupHelper localReferenceLookupHelper = (Resolve.ReferenceLookupHelper)localPair.snd;
/*      */ 
/* 2731 */       if (localSymbol.kind != 16)
/*      */       {
/*      */         int j;
/* 2733 */         switch (localSymbol.kind) {
/*      */         case 136:
/* 2735 */           j = 0;
/* 2736 */           break;
/*      */         case 129:
/*      */         case 130:
/*      */         case 131:
/*      */         case 132:
/*      */         case 134:
/*      */         case 135:
/*      */         case 138:
/* 2744 */           j = 1;
/* 2745 */           break;
/*      */         case 133:
/*      */         case 137:
/*      */         default:
/* 2747 */           Assert.error("unexpected result kind " + localSymbol.kind);
/* 2748 */           j = 0;
/*      */         }
/*      */ 
/* 2751 */         localObject5 = ((Resolve.ResolveError)localSymbol.baseSymbol()).getDiagnostic(JCDiagnostic.DiagnosticType.FRAGMENT, paramJCMemberReference, localType1.tsym, localType1, paramJCMemberReference.name, localList1, (List)localObject1);
/*      */ 
/* 2754 */         JCDiagnostic.DiagnosticType localDiagnosticType = j != 0 ? JCDiagnostic.DiagnosticType.FRAGMENT : JCDiagnostic.DiagnosticType.ERROR;
/*      */ 
/* 2757 */         JCDiagnostic localJCDiagnostic = this.diags.create(localDiagnosticType, this.log.currentSource(), paramJCMemberReference, "invalid.mref", new Object[] { 
/* 2758 */           Kinds.kindName(paramJCMemberReference
/* 2758 */           .getMode()), localObject5 });
/*      */ 
/* 2760 */         if ((j != 0) && (localObject2 == Type.recoveryType))
/*      */         {
/* 2763 */           this.result = (paramJCMemberReference.type = localObject2);
/* 2764 */           return;
/*      */         }
/* 2766 */         if (j != 0)
/* 2767 */           this.resultInfo.checkContext.report(paramJCMemberReference, localJCDiagnostic);
/*      */         else {
/* 2769 */           this.log.report(localJCDiagnostic);
/*      */         }
/* 2771 */         this.result = (paramJCMemberReference.type = this.types.createErrorType((Type)localObject2));
/* 2772 */         return;
/*      */       }
/*      */ 
/* 2776 */       paramJCMemberReference.sym = localSymbol.baseSymbol();
/* 2777 */       paramJCMemberReference.kind = localReferenceLookupHelper.referenceKind(paramJCMemberReference.sym);
/* 2778 */       paramJCMemberReference.ownerAccessible = this.rs.isAccessible(localEnv, paramJCMemberReference.sym.enclClass());
/*      */ 
/* 2780 */       if (localType2.getReturnType() == Type.recoveryType)
/*      */       {
/* 2782 */         this.result = (paramJCMemberReference.type = localObject2);
/* 2783 */         return;
/*      */       }
/*      */ 
/* 2786 */       if (this.resultInfo.checkContext.deferredAttrContext().mode == DeferredAttr.AttrMode.CHECK)
/*      */       {
/* 2788 */         if ((paramJCMemberReference.getMode() == MemberReferenceTree.ReferenceMode.INVOKE) && 
/* 2789 */           (TreeInfo.isStaticSelector(paramJCMemberReference.expr, this.names)) && 
/* 2790 */           (paramJCMemberReference.kind
/* 2790 */           .isUnbound()) && 
/* 2791 */           (!((Type)localType2
/* 2791 */           .getParameterTypes().head).isParameterized())) {
/* 2792 */           this.chk.checkRaw(paramJCMemberReference.expr, localEnv);
/*      */         }
/*      */ 
/* 2795 */         if ((paramJCMemberReference.sym.isStatic()) && (TreeInfo.isStaticSelector(paramJCMemberReference.expr, this.names)) && 
/* 2796 */           (localType1
/* 2796 */           .getTypeArguments().nonEmpty()))
/*      */         {
/* 2798 */           this.log.error(paramJCMemberReference.expr.pos(), "invalid.mref", new Object[] { Kinds.kindName(paramJCMemberReference.getMode()), this.diags
/* 2799 */             .fragment("static.mref.with.targs", new Object[0]) });
/*      */ 
/* 2800 */           this.result = (paramJCMemberReference.type = this.types.createErrorType((Type)localObject2));
/* 2801 */           return;
/*      */         }
/*      */ 
/* 2804 */         if ((paramJCMemberReference.sym.isStatic()) && (!TreeInfo.isStaticSelector(paramJCMemberReference.expr, this.names)) && 
/* 2805 */           (!paramJCMemberReference.kind
/* 2805 */           .isUnbound()))
/*      */         {
/* 2807 */           this.log.error(paramJCMemberReference.expr.pos(), "invalid.mref", new Object[] { Kinds.kindName(paramJCMemberReference.getMode()), this.diags
/* 2808 */             .fragment("static.bound.mref", new Object[0]) });
/*      */ 
/* 2809 */           this.result = (paramJCMemberReference.type = this.types.createErrorType((Type)localObject2));
/* 2810 */           return;
/*      */         }
/*      */ 
/* 2813 */         if ((!localSymbol.isStatic()) && (paramJCMemberReference.kind == JCTree.JCMemberReference.ReferenceKind.SUPER))
/*      */         {
/* 2815 */           this.rs.checkNonAbstract(paramJCMemberReference.pos(), paramJCMemberReference.sym);
/*      */         }
/*      */ 
/* 2818 */         if (i != 0) {
/* 2819 */           this.chk.checkElemAccessFromSerializableLambda(paramJCMemberReference);
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/* 2824 */       ResultInfo localResultInfo = this.resultInfo
/* 2824 */         .dup(newMethodTemplate(localType2
/* 2825 */         .getReturnType().hasTag(TypeTag.VOID) ? Type.noType : localType2.getReturnType(), paramJCMemberReference.kind
/* 2826 */         .isUnbound() ? localList1.tail : localList1, (List)localObject1), new FunctionalReturnContext(this.resultInfo.checkContext));
/*      */ 
/* 2829 */       Object localObject5 = checkId(paramJCMemberReference, localReferenceLookupHelper.site, localSymbol, localEnv, localResultInfo);
/*      */ 
/* 2831 */       if ((paramJCMemberReference.kind.isUnbound()) && 
/* 2832 */         (this.resultInfo.checkContext
/* 2832 */         .inferenceContext().free((Type)localList1.head)))
/*      */       {
/* 2834 */         if (!this.types.isSubtype(this.resultInfo.checkContext.inferenceContext().asUndetVar((Type)localList1.head), localType1))
/*      */         {
/* 2838 */           Assert.error("Can't get here");
/*      */         }
/*      */       }
/*      */ 
/* 2842 */       if (!((Type)localObject5).isErroneous()) {
/* 2843 */         localObject5 = this.types.createMethodTypeWithReturn((Type)localObject5, 
/* 2844 */           adjustMethodReturnType(localReferenceLookupHelper.site, paramJCMemberReference.name, localResultInfo.pt
/* 2844 */           .getParameterTypes(), ((Type)localObject5).getReturnType()));
/*      */       }
/*      */ 
/* 2850 */       boolean bool = this.resultInfo.checkContext
/* 2850 */         .deferredAttrContext().mode == DeferredAttr.AttrMode.SPECULATIVE;
/* 2851 */       checkReferenceCompatible(paramJCMemberReference, localType2, (Type)localObject5, this.resultInfo.checkContext, bool);
/* 2852 */       if (!bool) {
/* 2853 */         checkAccessibleTypes(paramJCMemberReference, localEnv, this.resultInfo.checkContext.inferenceContext(), new Type[] { localType2, localObject2 });
/*      */       }
/* 2855 */       this.result = check(paramJCMemberReference, (Type)localObject2, 12, this.resultInfo);
/*      */     } catch (Types.FunctionDescriptorLookupError localFunctionDescriptorLookupError) {
/* 2857 */       Object localObject1 = localFunctionDescriptorLookupError.getDiagnostic();
/* 2858 */       this.resultInfo.checkContext.report(paramJCMemberReference, (JCDiagnostic)localObject1);
/* 2859 */       this.result = (paramJCMemberReference.type = this.types.createErrorType(pt()));
/* 2860 */       return;
/*      */     }
/*      */   }
/*      */ 
/*      */   ResultInfo memberReferenceQualifierResult(JCTree.JCMemberReference paramJCMemberReference)
/*      */   {
/* 2866 */     return new ResultInfo(paramJCMemberReference.getMode() == MemberReferenceTree.ReferenceMode.INVOKE ? 14 : 2, Type.noType);
/*      */   }
/*      */ 
/*      */   void checkReferenceCompatible(JCTree.JCMemberReference paramJCMemberReference, Type paramType1, Type paramType2, Check.CheckContext paramCheckContext, boolean paramBoolean)
/*      */   {
/* 2872 */     Type localType1 = paramCheckContext.inferenceContext().asUndetVar(paramType1.getReturnType());
/*      */ 
/* 2875 */     switch (paramJCMemberReference.getMode()) {
/*      */     case NEW:
/* 2877 */       if (!paramJCMemberReference.expr.type.isRaw())
/* 2878 */         localType2 = paramJCMemberReference.expr.type;
/* 2879 */       break;
/*      */     }
/*      */ 
/* 2882 */     Type localType2 = paramType2.getReturnType();
/*      */ 
/* 2885 */     Type localType3 = localType2;
/*      */ 
/* 2887 */     if (localType1.hasTag(TypeTag.VOID)) {
/* 2888 */       localType3 = null;
/*      */     }
/*      */ 
/* 2891 */     if ((!localType1.hasTag(TypeTag.VOID)) && (!localType2.hasTag(TypeTag.VOID)) && (
/* 2892 */       (localType2.isErroneous()) || 
/* 2893 */       (new FunctionalReturnContext(paramCheckContext)
/* 2893 */       .compatible(localType2, localType1, this.types.noWarnings))))
/*      */     {
/* 2894 */       localType3 = null;
/*      */     }
/*      */ 
/* 2898 */     if (localType3 != null) {
/* 2899 */       paramCheckContext.report(paramJCMemberReference, this.diags.fragment("incompatible.ret.type.in.mref", new Object[] { this.diags
/* 2900 */         .fragment("inconvertible.types", new Object[] { localType2, paramType1
/* 2900 */         .getReturnType() }) }));
/*      */     }
/*      */ 
/* 2903 */     if (!paramBoolean) {
/* 2904 */       List localList = paramCheckContext.inferenceContext().asUndetVars(paramType1.getThrownTypes());
/* 2905 */       if (this.chk.unhandled(paramType2.getThrownTypes(), localList).nonEmpty())
/* 2906 */         this.log.error(paramJCMemberReference, "incompatible.thrown.types.in.mref", new Object[] { paramType2.getThrownTypes() });
/*      */     }
/*      */   }
/*      */ 
/*      */   private void setFunctionalInfo(final Env<AttrContext> paramEnv, final JCTree.JCFunctionalExpression paramJCFunctionalExpression, final Type paramType1, final Type paramType2, final Type paramType3, final Check.CheckContext paramCheckContext)
/*      */   {
/* 2918 */     if (paramCheckContext.inferenceContext().free(paramType2)) {
/* 2919 */       paramCheckContext.inferenceContext().addFreeTypeListener(List.of(paramType1, paramType2), new Infer.FreeTypeListener() {
/*      */         public void typesInferred(Infer.InferenceContext paramAnonymousInferenceContext) {
/* 2921 */           Attr.this.setFunctionalInfo(paramEnv, paramJCFunctionalExpression, paramType1, paramAnonymousInferenceContext.asInstType(paramType2), paramAnonymousInferenceContext
/* 2922 */             .asInstType(paramType3), 
/* 2922 */             paramCheckContext);
/*      */         } } );
/*      */     }
/*      */     else {
/* 2926 */       ListBuffer localListBuffer = new ListBuffer();
/*      */       Object localObject1;
/*      */       Object localObject2;
/* 2927 */       if (paramType1.hasTag(TypeTag.CLASS)) {
/* 2928 */         if (paramType1.isCompound()) {
/* 2929 */           localListBuffer.append(this.types.removeWildcards(paramType3));
/* 2930 */           for (localObject1 = ((Type.IntersectionClassType)pt()).interfaces_field.iterator(); ((Iterator)localObject1).hasNext(); ) { localObject2 = (Type)((Iterator)localObject1).next();
/* 2931 */             if (localObject2 != paramType3)
/* 2932 */               localListBuffer.append(this.types.removeWildcards((Type)localObject2)); }
/*      */         }
/*      */         else
/*      */         {
/* 2936 */           localListBuffer.append(this.types.removeWildcards(paramType3));
/*      */         }
/*      */       }
/* 2939 */       paramJCFunctionalExpression.targets = localListBuffer.toList();
/* 2940 */       if ((paramCheckContext.deferredAttrContext().mode == DeferredAttr.AttrMode.CHECK) && (paramType1 != Type.recoveryType))
/*      */       {
/*      */         try
/*      */         {
/* 2948 */           localObject1 = this.types.makeFunctionalInterfaceClass(paramEnv, this.names.empty, 
/* 2949 */             List.of(paramJCFunctionalExpression.targets.head), 
/* 2949 */             1024L);
/* 2950 */           if (localObject1 != null)
/* 2951 */             this.chk.checkImplementations(paramEnv.tree, (Symbol.ClassSymbol)localObject1, (Symbol.ClassSymbol)localObject1);
/*      */         }
/*      */         catch (Types.FunctionDescriptorLookupError localFunctionDescriptorLookupError) {
/* 2954 */           localObject2 = localFunctionDescriptorLookupError.getDiagnostic();
/* 2955 */           this.resultInfo.checkContext.report(paramEnv.tree, (JCDiagnostic)localObject2);
/*      */         }
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   public void visitParens(JCTree.JCParens paramJCParens) {
/* 2962 */     Type localType = attribTree(paramJCParens.expr, this.env, this.resultInfo);
/* 2963 */     this.result = check(paramJCParens, localType, pkind(), this.resultInfo);
/* 2964 */     Symbol localSymbol = TreeInfo.symbol(paramJCParens);
/* 2965 */     if ((localSymbol != null) && ((localSymbol.kind & 0x3) != 0))
/* 2966 */       this.log.error(paramJCParens.pos(), "illegal.start.of.type", new Object[0]);
/*      */   }
/*      */ 
/*      */   public void visitAssign(JCTree.JCAssign paramJCAssign) {
/* 2970 */     Type localType1 = attribTree(paramJCAssign.lhs, this.env.dup(paramJCAssign), this.varInfo);
/* 2971 */     Type localType2 = capture(localType1);
/* 2972 */     attribExpr(paramJCAssign.rhs, this.env, localType1);
/* 2973 */     this.result = check(paramJCAssign, localType2, 12, this.resultInfo);
/*      */   }
/*      */ 
/*      */   public void visitAssignop(JCTree.JCAssignOp paramJCAssignOp)
/*      */   {
/* 2978 */     Type localType1 = attribTree(paramJCAssignOp.lhs, this.env, this.varInfo);
/* 2979 */     Type localType2 = attribExpr(paramJCAssignOp.rhs, this.env);
/*      */ 
/* 2981 */     Symbol localSymbol = paramJCAssignOp.operator = this.rs.resolveBinaryOperator(paramJCAssignOp
/* 2982 */       .pos(), paramJCAssignOp.getTag().noAssignOp(), this.env, localType1, localType2);
/*      */ 
/* 2985 */     if ((localSymbol.kind == 16) && 
/* 2986 */       (!localType1
/* 2986 */       .isErroneous()) && 
/* 2987 */       (!localType2
/* 2987 */       .isErroneous())) {
/* 2988 */       this.chk.checkOperator(paramJCAssignOp.pos(), (Symbol.OperatorSymbol)localSymbol, paramJCAssignOp
/* 2990 */         .getTag().noAssignOp(), localType1, localType2);
/*      */ 
/* 2993 */       this.chk.checkDivZero(paramJCAssignOp.rhs.pos(), localSymbol, localType2);
/* 2994 */       this.chk.checkCastable(paramJCAssignOp.rhs.pos(), localSymbol.type
/* 2995 */         .getReturnType(), localType1);
/*      */     }
/*      */ 
/* 2998 */     this.result = check(paramJCAssignOp, localType1, 12, this.resultInfo);
/*      */   }
/*      */ 
/*      */   public void visitUnary(JCTree.JCUnary paramJCUnary)
/*      */   {
/* 3005 */     Type localType1 = paramJCUnary.getTag().isIncOrDecUnaryOp() ? 
/* 3004 */       attribTree(paramJCUnary.arg, this.env, this.varInfo) : 
/* 3004 */       this.chk
/* 3005 */       .checkNonVoid(paramJCUnary.arg
/* 3005 */       .pos(), attribExpr(paramJCUnary.arg, this.env));
/*      */ 
/* 3009 */     Symbol localSymbol = paramJCUnary.operator = this.rs
/* 3009 */       .resolveUnaryOperator(paramJCUnary
/* 3009 */       .pos(), paramJCUnary.getTag(), this.env, localType1);
/*      */ 
/* 3011 */     Type localType2 = this.types.createErrorType(paramJCUnary.type);
/* 3012 */     if ((localSymbol.kind == 16) && 
/* 3013 */       (!localType1
/* 3013 */       .isErroneous()))
/*      */     {
/* 3016 */       localType2 = paramJCUnary.getTag().isIncOrDecUnaryOp() ? paramJCUnary.arg.type : localSymbol.type
/* 3016 */         .getReturnType();
/* 3017 */       int i = ((Symbol.OperatorSymbol)localSymbol).opcode;
/*      */ 
/* 3020 */       if (localType1.constValue() != null) {
/* 3021 */         Type localType3 = this.cfolder.fold1(i, localType1);
/* 3022 */         if (localType3 != null) {
/* 3023 */           localType2 = this.cfolder.coerce(localType3, localType2);
/*      */         }
/*      */       }
/*      */     }
/* 3027 */     this.result = check(paramJCUnary, localType2, 12, this.resultInfo);
/*      */   }
/*      */ 
/*      */   public void visitBinary(JCTree.JCBinary paramJCBinary)
/*      */   {
/* 3032 */     Type localType1 = this.chk.checkNonVoid(paramJCBinary.lhs.pos(), attribExpr(paramJCBinary.lhs, this.env));
/* 3033 */     Type localType2 = this.chk.checkNonVoid(paramJCBinary.lhs.pos(), attribExpr(paramJCBinary.rhs, this.env));
/*      */ 
/* 3037 */     Symbol localSymbol = paramJCBinary.operator = this.rs
/* 3037 */       .resolveBinaryOperator(paramJCBinary
/* 3037 */       .pos(), paramJCBinary.getTag(), this.env, localType1, localType2);
/*      */ 
/* 3039 */     Type localType3 = this.types.createErrorType(paramJCBinary.type);
/* 3040 */     if ((localSymbol.kind == 16) && 
/* 3041 */       (!localType1
/* 3041 */       .isErroneous()) && 
/* 3042 */       (!localType2
/* 3042 */       .isErroneous())) {
/* 3043 */       localType3 = localSymbol.type.getReturnType();
/*      */ 
/* 3046 */       int i = this.chk.checkOperator(paramJCBinary.lhs.pos(), (Symbol.OperatorSymbol)localSymbol, paramJCBinary
/* 3048 */         .getTag(), localType1, localType2);
/*      */ 
/* 3053 */       if ((localType1.constValue() != null) && (localType2.constValue() != null)) {
/* 3054 */         Type localType4 = this.cfolder.fold2(i, localType1, localType2);
/* 3055 */         if (localType4 != null) {
/* 3056 */           localType3 = this.cfolder.coerce(localType4, localType3);
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/* 3063 */       if (((i == 165) || (i == 166)) && 
/* 3064 */         (!this.types.isEqualityComparable(localType1, localType2, new Warner(paramJCBinary
/* 3065 */         .pos())))) {
/* 3066 */         this.log.error(paramJCBinary.pos(), "incomparable.types", new Object[] { localType1, localType2 });
/*      */       }
/*      */ 
/* 3070 */       this.chk.checkDivZero(paramJCBinary.rhs.pos(), localSymbol, localType2);
/*      */     }
/* 3072 */     this.result = check(paramJCBinary, localType3, 12, this.resultInfo);
/*      */   }
/*      */ 
/*      */   public void visitTypeCast(JCTree.JCTypeCast paramJCTypeCast) {
/* 3076 */     Type localType1 = attribType(paramJCTypeCast.clazz, this.env);
/* 3077 */     this.chk.validate(paramJCTypeCast.clazz, this.env, false);
/*      */ 
/* 3080 */     Env localEnv = this.env.dup(paramJCTypeCast);
/*      */ 
/* 3083 */     JCTree.JCExpression localJCExpression = TreeInfo.skipParens(paramJCTypeCast.expr);
/* 3084 */     int i = (this.allowPoly) && ((localJCExpression.hasTag(JCTree.Tag.LAMBDA)) || (localJCExpression.hasTag(JCTree.Tag.REFERENCE))) ? 1 : 0;
/*      */     ResultInfo localResultInfo;
/* 3085 */     if (i != 0)
/*      */     {
/* 3087 */       localResultInfo = new ResultInfo(12, localType1, new Check.NestedCheckContext(this.resultInfo.checkContext)
/*      */       {
/*      */         public boolean compatible(Type paramAnonymousType1, Type paramAnonymousType2, Warner paramAnonymousWarner) {
/* 3090 */           return Attr.this.types.isCastable(paramAnonymousType1, paramAnonymousType2, paramAnonymousWarner);
/*      */         }
/*      */       });
/*      */     }
/*      */     else {
/* 3095 */       localResultInfo = this.unknownExprInfo;
/*      */     }
/* 3097 */     Type localType2 = attribTree(paramJCTypeCast.expr, localEnv, localResultInfo);
/* 3098 */     Type localType3 = i != 0 ? localType1 : this.chk.checkCastable(paramJCTypeCast.expr.pos(), localType2, localType1);
/* 3099 */     if (localType2.constValue() != null)
/* 3100 */       localType3 = this.cfolder.coerce(localType2, localType3);
/* 3101 */     this.result = check(paramJCTypeCast, capture(localType3), 12, this.resultInfo);
/* 3102 */     if (i == 0)
/* 3103 */       this.chk.checkRedundantCast(localEnv, paramJCTypeCast);
/*      */   }
/*      */ 
/*      */   public void visitTypeTest(JCTree.JCInstanceOf paramJCInstanceOf) {
/* 3107 */     Type localType1 = this.chk.checkNullOrRefType(paramJCInstanceOf.expr
/* 3108 */       .pos(), attribExpr(paramJCInstanceOf.expr, this.env));
/* 3109 */     Type localType2 = attribType(paramJCInstanceOf.clazz, this.env);
/* 3110 */     if (!localType2.hasTag(TypeTag.TYPEVAR)) {
/* 3111 */       localType2 = this.chk.checkClassOrArrayType(paramJCInstanceOf.clazz.pos(), localType2);
/*      */     }
/* 3113 */     if ((!localType2.isErroneous()) && (!this.types.isReifiable(localType2))) {
/* 3114 */       this.log.error(paramJCInstanceOf.clazz.pos(), "illegal.generic.type.for.instof", new Object[0]);
/* 3115 */       localType2 = this.types.createErrorType(localType2);
/*      */     }
/* 3117 */     this.chk.validate(paramJCInstanceOf.clazz, this.env, false);
/* 3118 */     this.chk.checkCastable(paramJCInstanceOf.expr.pos(), localType1, localType2);
/* 3119 */     this.result = check(paramJCInstanceOf, this.syms.booleanType, 12, this.resultInfo);
/*      */   }
/*      */ 
/*      */   public void visitIndexed(JCTree.JCArrayAccess paramJCArrayAccess) {
/* 3123 */     Type localType1 = this.types.createErrorType(paramJCArrayAccess.type);
/* 3124 */     Type localType2 = attribExpr(paramJCArrayAccess.indexed, this.env);
/* 3125 */     attribExpr(paramJCArrayAccess.index, this.env, this.syms.intType);
/* 3126 */     if (this.types.isArray(localType2))
/* 3127 */       localType1 = this.types.elemtype(localType2);
/* 3128 */     else if (!localType2.hasTag(TypeTag.ERROR))
/* 3129 */       this.log.error(paramJCArrayAccess.pos(), "array.req.but.found", new Object[] { localType2 });
/* 3130 */     if ((pkind() & 0x4) == 0) localType1 = capture(localType1);
/* 3131 */     this.result = check(paramJCArrayAccess, localType1, 4, this.resultInfo);
/*      */   }
/*      */ 
/*      */   public void visitIdent(JCTree.JCIdent paramJCIdent)
/*      */   {
/*      */     Symbol localSymbol;
/* 3138 */     if ((pt().hasTag(TypeTag.METHOD)) || (pt().hasTag(TypeTag.FORALL)))
/*      */     {
/* 3141 */       ((AttrContext)this.env.info).pendingResolutionPhase = null;
/* 3142 */       localSymbol = this.rs.resolveMethod(paramJCIdent.pos(), this.env, paramJCIdent.name, pt().getParameterTypes(), pt().getTypeArguments());
/* 3143 */     } else if ((paramJCIdent.sym != null) && (paramJCIdent.sym.kind != 4)) {
/* 3144 */       localSymbol = paramJCIdent.sym;
/*      */     } else {
/* 3146 */       localSymbol = this.rs.resolveIdent(paramJCIdent.pos(), this.env, paramJCIdent.name, pkind());
/*      */     }
/* 3148 */     paramJCIdent.sym = localSymbol;
/*      */ 
/* 3157 */     Env localEnv = this.env;
/* 3158 */     int i = 0;
/* 3159 */     if ((this.env.enclClass.sym.owner.kind != 1) && ((localSymbol.kind & 0x16) != 0) && (localSymbol.owner.kind == 2) && (paramJCIdent.name != this.names._this) && (paramJCIdent.name != this.names._super))
/*      */     {
/* 3165 */       while ((localEnv.outer != null) && 
/* 3166 */         (!localSymbol
/* 3166 */         .isMemberOf(localEnv.enclClass.sym, this.types)))
/*      */       {
/* 3167 */         if ((localEnv.enclClass.sym.flags() & 0x400000) != 0L)
/* 3168 */           i = !this.allowAnonOuterThis ? 1 : 0;
/* 3169 */         localEnv = localEnv.outer;
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/* 3174 */     if (localSymbol.kind == 4) {
/* 3175 */       localObject = (Symbol.VarSymbol)localSymbol;
/*      */ 
/* 3179 */       checkInit(paramJCIdent, this.env, (Symbol.VarSymbol)localObject, false);
/*      */ 
/* 3183 */       if (pkind() == 4) {
/* 3184 */         checkAssignable(paramJCIdent.pos(), (Symbol.VarSymbol)localObject, null, this.env);
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/* 3190 */     if (((((AttrContext)localEnv.info).isSelfCall) || (i != 0)) && ((localSymbol.kind & 0x14) != 0) && (localSymbol.owner.kind == 2))
/*      */     {
/* 3193 */       if ((localSymbol
/* 3193 */         .flags() & 0x8) == 0L)
/* 3194 */         this.chk.earlyRefError(paramJCIdent.pos(), localSymbol.kind == 4 ? localSymbol : thisSym(paramJCIdent.pos(), this.env));
/*      */     }
/* 3196 */     Object localObject = this.env;
/* 3197 */     if ((localSymbol.kind != 63) && (localSymbol.kind != 2) && (localSymbol.owner != null) && (localSymbol.owner != ((Env)localObject).enclClass.sym))
/*      */     {
/* 3201 */       while ((((Env)localObject).outer != null) && (!this.rs.isAccessible(this.env, ((Env)localObject).enclClass.sym.type, localSymbol))) {
/* 3202 */         localObject = ((Env)localObject).outer;
/*      */       }
/*      */     }
/* 3205 */     if (((AttrContext)this.env.info).isSerializable) {
/* 3206 */       this.chk.checkElemAccessFromSerializableLambda(paramJCIdent);
/*      */     }
/*      */ 
/* 3209 */     this.result = checkId(paramJCIdent, ((Env)localObject).enclClass.sym.type, localSymbol, this.env, this.resultInfo);
/*      */   }
/*      */ 
/*      */   public void visitSelect(JCTree.JCFieldAccess paramJCFieldAccess)
/*      */   {
/* 3214 */     int i = 0;
/* 3215 */     if ((paramJCFieldAccess.name == this.names._this) || (paramJCFieldAccess.name == this.names._super) || (paramJCFieldAccess.name == this.names._class))
/*      */     {
/* 3218 */       i = 2;
/*      */     } else {
/* 3220 */       if ((pkind() & 0x1) != 0) i |= 1;
/* 3221 */       if ((pkind() & 0x2) != 0) i = i | 0x2 | 0x1;
/* 3222 */       if ((pkind() & 0x1C) != 0) i = i | 0xC | 0x2;
/*      */ 
/*      */     }
/*      */ 
/* 3226 */     Object localObject1 = attribTree(paramJCFieldAccess.selected, this.env, new ResultInfo(i, Infer.anyPoly));
/* 3227 */     if ((pkind() & 0x3) == 0) {
/* 3228 */       localObject1 = capture((Type)localObject1);
/*      */     }
/*      */ 
/* 3231 */     if (i == 2) {
/* 3232 */       localObject2 = localObject1;
/* 3233 */       while (((Type)localObject2).hasTag(TypeTag.ARRAY))
/* 3234 */         localObject2 = ((Type.ArrayType)((Type)localObject2).unannotatedType()).elemtype;
/* 3235 */       if (((Type)localObject2).hasTag(TypeTag.TYPEVAR)) {
/* 3236 */         this.log.error(paramJCFieldAccess.pos(), "type.var.cant.be.deref", new Object[0]);
/* 3237 */         this.result = (paramJCFieldAccess.type = this.types.createErrorType(paramJCFieldAccess.name, ((Type)localObject1).tsym, (Type)localObject1));
/* 3238 */         paramJCFieldAccess.sym = paramJCFieldAccess.type.tsym;
/* 3239 */         return;
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/* 3246 */     Object localObject2 = TreeInfo.symbol(paramJCFieldAccess.selected);
/* 3247 */     boolean bool1 = ((AttrContext)this.env.info).selectSuper;
/* 3248 */     ((AttrContext)this.env.info).selectSuper = ((localObject2 != null) && (((Symbol)localObject2).name == this.names._super));
/*      */ 
/* 3253 */     ((AttrContext)this.env.info).pendingResolutionPhase = null;
/* 3254 */     Object localObject3 = selectSym(paramJCFieldAccess, (Symbol)localObject2, (Type)localObject1, this.env, this.resultInfo);
/* 3255 */     if ((((Symbol)localObject3).kind == 4) && (((Symbol)localObject3).name != this.names._super) && (((AttrContext)this.env.info).defaultSuperCallSite != null)) {
/* 3256 */       this.log.error(paramJCFieldAccess.selected.pos(), "not.encl.class", new Object[] { ((Type)localObject1).tsym });
/* 3257 */       localObject3 = this.syms.errSymbol;
/*      */     }
/* 3259 */     if ((((Symbol)localObject3).exists()) && (!isType((Symbol)localObject3)) && ((pkind() & 0x3) != 0)) {
/* 3260 */       localObject1 = capture((Type)localObject1);
/* 3261 */       localObject3 = selectSym(paramJCFieldAccess, (Symbol)localObject2, (Type)localObject1, this.env, this.resultInfo);
/*      */     }
/* 3263 */     boolean bool2 = ((AttrContext)this.env.info).lastResolveVarargs();
/* 3264 */     paramJCFieldAccess.sym = ((Symbol)localObject3);
/*      */ 
/* 3266 */     if ((((Type)localObject1).hasTag(TypeTag.TYPEVAR)) && (!isType((Symbol)localObject3)) && (((Symbol)localObject3).kind != 63)) {
/* 3267 */       while (((Type)localObject1).hasTag(TypeTag.TYPEVAR)) localObject1 = ((Type)localObject1).getUpperBound();
/* 3268 */       localObject1 = capture((Type)localObject1);
/*      */     }
/*      */     Object localObject4;
/* 3272 */     if (((Symbol)localObject3).kind == 4) {
/* 3273 */       localObject4 = (Symbol.VarSymbol)localObject3;
/*      */ 
/* 3277 */       checkInit(paramJCFieldAccess, this.env, (Symbol.VarSymbol)localObject4, true);
/*      */ 
/* 3281 */       if (pkind() == 4) {
/* 3282 */         checkAssignable(paramJCFieldAccess.pos(), (Symbol.VarSymbol)localObject4, paramJCFieldAccess.selected, this.env);
/*      */       }
/*      */     }
/* 3285 */     if ((localObject2 != null) && (((Symbol)localObject2).kind == 4))
/*      */     {
/* 3287 */       if ((((Symbol.VarSymbol)localObject2)
/* 3287 */         .isResourceVariable()) && (((Symbol)localObject3).kind == 16))
/*      */       {
/* 3289 */         if ((((Symbol)localObject3).name
/* 3289 */           .equals(this.names.close)) && 
/* 3290 */           (((Symbol)localObject3)
/* 3290 */           .overrides(this.syms.autoCloseableClose, ((Symbol)localObject2).type.tsym, this.types, true)) && 
/* 3291 */           (((AttrContext)this.env.info).lint
/* 3291 */           .isEnabled(Lint.LintCategory.TRY)))
/*      */         {
/* 3292 */           this.log.warning(Lint.LintCategory.TRY, paramJCFieldAccess, "try.explicit.close.call", new Object[0]);
/*      */         }
/*      */       }
/*      */     }
/* 3296 */     if ((isType((Symbol)localObject3)) && ((localObject2 == null) || ((((Symbol)localObject2).kind & 0x3) == 0))) {
/* 3297 */       paramJCFieldAccess.type = check(paramJCFieldAccess.selected, pt(), localObject2 == null ? 12 : ((Symbol)localObject2).kind, new ResultInfo(3, 
/* 3298 */         pt()));
/*      */     }
/*      */ 
/* 3301 */     if (isType((Symbol)localObject2)) {
/* 3302 */       if (((Symbol)localObject3).name == this.names._this)
/*      */       {
/* 3305 */         if ((((AttrContext)this.env.info).isSelfCall) && (((Type)localObject1).tsym == this.env.enclClass.sym))
/*      */         {
/* 3307 */           this.chk.earlyRefError(paramJCFieldAccess.pos(), (Symbol)localObject3);
/*      */         }
/*      */ 
/*      */       }
/* 3311 */       else if (((((Symbol)localObject3).flags() & 0x8) == 0L) && 
/* 3312 */         (!this.env.next.tree
/* 3312 */         .hasTag(JCTree.Tag.REFERENCE)) && 
/* 3312 */         (((Symbol)localObject3).name != this.names._super) && (
/* 3312 */         (((Symbol)localObject3).kind == 4) || (((Symbol)localObject3).kind == 16)))
/*      */       {
/*      */         Resolve tmp921_918 = this.rs; tmp921_918.getClass(); this.rs.accessBase(new Resolve.StaticError(tmp921_918, (Symbol)localObject3), paramJCFieldAccess
/* 3316 */           .pos(), (Type)localObject1, ((Symbol)localObject3).name, true);
/*      */       }
/*      */ 
/* 3319 */       if ((!this.allowStaticInterfaceMethods) && (((Symbol)localObject2).isInterface()) && 
/* 3320 */         (((Symbol)localObject3)
/* 3320 */         .isStatic()) && (((Symbol)localObject3).kind == 16))
/* 3321 */         this.log.error(paramJCFieldAccess.pos(), "static.intf.method.invoke.not.supported.in.source", new Object[] { this.sourceName });
/*      */     }
/* 3323 */     else if ((((Symbol)localObject3).kind != 63) && ((((Symbol)localObject3).flags() & 0x8) != 0L) && (((Symbol)localObject3).name != this.names._class))
/*      */     {
/* 3326 */       this.chk.warnStatic(paramJCFieldAccess, "static.not.qualified.by.type", new Object[] { Kinds.kindName(((Symbol)localObject3).kind), ((Symbol)localObject3).owner });
/*      */     }
/*      */ 
/* 3330 */     if ((((AttrContext)this.env.info).selectSuper) && ((((Symbol)localObject3).flags() & 0x8) == 0L))
/*      */     {
/* 3333 */       this.rs.checkNonAbstract(paramJCFieldAccess.pos(), (Symbol)localObject3);
/*      */ 
/* 3335 */       if (((Type)localObject1).isRaw())
/*      */       {
/* 3337 */         localObject4 = this.types.asSuper(this.env.enclClass.sym.type, ((Type)localObject1).tsym);
/* 3338 */         if (localObject4 != null) localObject1 = localObject4;
/*      */       }
/*      */     }
/*      */ 
/* 3342 */     if (((AttrContext)this.env.info).isSerializable) {
/* 3343 */       this.chk.checkElemAccessFromSerializableLambda(paramJCFieldAccess);
/*      */     }
/*      */ 
/* 3346 */     ((AttrContext)this.env.info).selectSuper = bool1;
/* 3347 */     this.result = checkId(paramJCFieldAccess, (Type)localObject1, (Symbol)localObject3, this.env, this.resultInfo);
/*      */   }
/*      */ 
/*      */   private Symbol selectSym(JCTree.JCFieldAccess paramJCFieldAccess, Symbol paramSymbol, Type paramType, Env<AttrContext> paramEnv, ResultInfo paramResultInfo)
/*      */   {
/* 3362 */     JCDiagnostic.DiagnosticPosition localDiagnosticPosition = paramJCFieldAccess.pos();
/* 3363 */     Name localName = paramJCFieldAccess.name;
/*      */     Object localObject1;
/*      */     Object localObject2;
/* 3364 */     switch (14.$SwitchMap$com$sun$tools$javac$code$TypeTag[paramType.getTag().ordinal()]) {
/*      */     case 1:
/* 3366 */       return this.rs.accessBase(this.rs
/* 3367 */         .findIdentInPackage(paramEnv, paramType.tsym, localName, paramResultInfo.pkind), 
/* 3367 */         localDiagnosticPosition, paramSymbol, paramType, localName, true);
/*      */     case 2:
/*      */     case 3:
/* 3371 */       if ((paramResultInfo.pt.hasTag(TypeTag.METHOD)) || (paramResultInfo.pt.hasTag(TypeTag.FORALL)))
/* 3372 */         return this.rs.resolveQualifiedMethod(localDiagnosticPosition, paramEnv, paramSymbol, paramType, localName, paramResultInfo.pt
/* 3373 */           .getParameterTypes(), paramResultInfo.pt.getTypeArguments());
/* 3374 */       if ((localName == this.names._this) || (localName == this.names._super))
/* 3375 */         return this.rs.resolveSelf(localDiagnosticPosition, paramEnv, paramType.tsym, localName);
/* 3376 */       if (localName == this.names._class)
/*      */       {
/* 3379 */         localObject1 = this.syms.classType;
/*      */ 
/* 3382 */         localObject2 = this.allowGenerics ? 
/* 3381 */           List.of(this.types
/* 3381 */           .erasure(paramType)) : 
/* 3382 */           List.nil();
/* 3383 */         localObject1 = new Type.ClassType(((Type)localObject1).getEnclosingType(), (List)localObject2, ((Type)localObject1).tsym);
/* 3384 */         return new Symbol.VarSymbol(25L, this.names._class, (Type)localObject1, paramType.tsym);
/*      */       }
/*      */ 
/* 3388 */       localObject1 = this.rs.findIdentInType(paramEnv, paramType, localName, paramResultInfo.pkind);
/* 3389 */       if ((paramResultInfo.pkind & 0x80) == 0)
/* 3390 */         localObject1 = this.rs.accessBase((Symbol)localObject1, localDiagnosticPosition, paramSymbol, paramType, localName, true);
/* 3391 */       return localObject1;
/*      */     case 4:
/* 3394 */       throw new AssertionError(paramJCFieldAccess);
/*      */     case 5:
/* 3403 */       localObject1 = paramType.getUpperBound() != null ? 
/* 3403 */         selectSym(paramJCFieldAccess, paramSymbol, 
/* 3403 */         capture(paramType
/* 3403 */         .getUpperBound()), paramEnv, paramResultInfo) : null;
/*      */ 
/* 3405 */       if (localObject1 == null) {
/* 3406 */         this.log.error(localDiagnosticPosition, "type.var.cant.be.deref", new Object[0]);
/* 3407 */         return this.syms.errSymbol;
/*      */       }
/*      */       Resolve tmp432_429 = this.rs; tmp432_429.getClass(); localObject2 = (((Symbol)localObject1).flags() & 0x2) != 0L ? new Resolve.AccessError(tmp432_429, paramEnv, paramType, (Symbol)localObject1) : localObject1;
/*      */ 
/* 3412 */       this.rs.accessBase((Symbol)localObject2, localDiagnosticPosition, paramSymbol, paramType, localName, true);
/* 3413 */       return localObject1;
/*      */     case 6:
/* 3417 */       return this.types.createErrorType(localName, paramType.tsym, paramType).tsym;
/*      */     }
/*      */ 
/* 3421 */     if (localName == this.names._class)
/*      */     {
/* 3424 */       localObject2 = this.syms.classType;
/* 3425 */       Type localType = this.types.boxedClass(paramType).type;
/* 3426 */       localObject2 = new Type.ClassType(((Type)localObject2).getEnclosingType(), List.of(localType), ((Type)localObject2).tsym);
/* 3427 */       return new Symbol.VarSymbol(25L, this.names._class, (Type)localObject2, paramType.tsym);
/*      */     }
/*      */ 
/* 3430 */     this.log.error(localDiagnosticPosition, "cant.deref", new Object[] { paramType });
/* 3431 */     return this.syms.errSymbol;
/*      */   }
/*      */ 
/*      */   Type checkId(JCTree paramJCTree, Type paramType, Symbol paramSymbol, Env<AttrContext> paramEnv, ResultInfo paramResultInfo)
/*      */   {
/* 3466 */     return (paramResultInfo.pt.hasTag(TypeTag.FORALL)) || (paramResultInfo.pt.hasTag(TypeTag.METHOD)) ? 
/* 3465 */       checkMethodId(paramJCTree, paramType, paramSymbol, paramEnv, paramResultInfo) : 
/* 3466 */       checkIdInternal(paramJCTree, paramType, paramSymbol, paramResultInfo.pt, paramEnv, paramResultInfo);
/*      */   }
/*      */ 
/*      */   Type checkMethodId(JCTree paramJCTree, Type paramType, Symbol paramSymbol, Env<AttrContext> paramEnv, ResultInfo paramResultInfo)
/*      */   {
/* 3475 */     int i = (paramSymbol
/* 3475 */       .baseSymbol().flags() & 0x0) != 0L ? 1 : 0;
/*      */ 
/* 3478 */     return i != 0 ? 
/* 3477 */       checkSigPolyMethodId(paramJCTree, paramType, paramSymbol, paramEnv, paramResultInfo) : 
/* 3478 */       checkMethodIdInternal(paramJCTree, paramType, paramSymbol, paramEnv, paramResultInfo);
/*      */   }
/*      */ 
/*      */   Type checkSigPolyMethodId(JCTree paramJCTree, Type paramType, Symbol paramSymbol, Env<AttrContext> paramEnv, ResultInfo paramResultInfo)
/*      */   {
/* 3487 */     checkMethodIdInternal(paramJCTree, paramType, paramSymbol.baseSymbol(), paramEnv, paramResultInfo);
/* 3488 */     ((AttrContext)paramEnv.info).pendingResolutionPhase = Resolve.MethodResolutionPhase.BASIC;
/* 3489 */     return paramSymbol.type;
/*      */   }
/*      */ 
/*      */   Type checkMethodIdInternal(JCTree paramJCTree, Type paramType, Symbol paramSymbol, Env<AttrContext> paramEnv, ResultInfo paramResultInfo)
/*      */   {
/* 3497 */     if ((paramResultInfo.pkind & 0x20) != 0)
/*      */     {
/*      */       DeferredAttr tmp24_21 = this.deferredAttr; tmp24_21.getClass(); Type localType1 = paramResultInfo.pt.map(new DeferredAttr.RecoveryDeferredTypeMap(tmp24_21, DeferredAttr.AttrMode.SPECULATIVE, paramSymbol, ((AttrContext)paramEnv.info).pendingResolutionPhase));
/* 3499 */       Type localType2 = checkIdInternal(paramJCTree, paramType, paramSymbol, localType1, paramEnv, paramResultInfo);
/*      */       DeferredAttr tmp80_77 = this.deferredAttr; tmp80_77.getClass(); paramResultInfo.pt.map(new DeferredAttr.RecoveryDeferredTypeMap(tmp80_77, DeferredAttr.AttrMode.CHECK, paramSymbol, ((AttrContext)paramEnv.info).pendingResolutionPhase));
/* 3501 */       return localType2;
/*      */     }
/* 3503 */     return checkIdInternal(paramJCTree, paramType, paramSymbol, paramResultInfo.pt, paramEnv, paramResultInfo);
/*      */   }
/*      */ 
/*      */   Type checkIdInternal(JCTree paramJCTree, Type paramType1, Symbol paramSymbol, Type paramType2, Env<AttrContext> paramEnv, ResultInfo paramResultInfo)
/*      */   {
/* 3513 */     if (paramType2.isErroneous())
/* 3514 */       return this.types.createErrorType(paramType1);
/*      */     Object localObject1;
/*      */     Object localObject2;
/*      */     Type localType;
/* 3517 */     switch (paramSymbol.kind)
/*      */     {
/*      */     case 2:
/* 3521 */       localObject1 = paramSymbol.type;
/* 3522 */       if (((Type)localObject1).hasTag(TypeTag.CLASS)) {
/* 3523 */         this.chk.checkForBadAuxiliaryClassAccess(paramJCTree.pos(), paramEnv, (Symbol.ClassSymbol)paramSymbol);
/* 3524 */         localObject2 = ((Type)localObject1).getEnclosingType();
/*      */ 
/* 3529 */         if (((Type)localObject1).tsym.type.getTypeArguments().nonEmpty()) {
/* 3530 */           localObject1 = this.types.erasure((Type)localObject1);
/*      */         }
/* 3543 */         else if ((((Type)localObject2).hasTag(TypeTag.CLASS)) && (paramType1 != localObject2)) {
/* 3544 */           localType = paramType1;
/* 3545 */           if (localType.hasTag(TypeTag.CLASS)) {
/* 3546 */             localType = this.types.asEnclosingSuper(paramType1, ((Type)localObject2).tsym);
/*      */           }
/* 3548 */           if (localType == null)
/* 3549 */             localType = this.types.erasure((Type)localObject2);
/* 3550 */           if (localType != localObject2)
/*      */           {
/* 3552 */             localObject1 = new Type.ClassType(localType, 
/* 3552 */               List.nil(), ((Type)localObject1).tsym);
/*      */           }
/*      */         }
/*      */       }
/* 3554 */       break;
/*      */     case 4:
/* 3557 */       localObject2 = (Symbol.VarSymbol)paramSymbol;
/*      */ 
/* 3561 */       if ((this.allowGenerics) && (paramResultInfo.pkind == 4) && (((Symbol.VarSymbol)localObject2).owner.kind == 2))
/*      */       {
/* 3564 */         if (((((Symbol.VarSymbol)localObject2)
/* 3564 */           .flags() & 0x8) == 0L) && (
/* 3565 */           (paramType1
/* 3565 */           .hasTag(TypeTag.CLASS)) || 
/* 3565 */           (paramType1.hasTag(TypeTag.TYPEVAR)))) {
/* 3566 */           localType = this.types.asOuterSuper(paramType1, ((Symbol.VarSymbol)localObject2).owner);
/* 3567 */           if ((localType != null) && 
/* 3568 */             (localType
/* 3568 */             .isRaw()) && 
/* 3569 */             (!this.types
/* 3569 */             .isSameType(((Symbol.VarSymbol)localObject2).type, ((Symbol.VarSymbol)localObject2)
/* 3569 */             .erasure(this.types))))
/*      */           {
/* 3570 */             this.chk.warnUnchecked(paramJCTree.pos(), "unchecked.assign.to.var", new Object[] { localObject2, localType });
/*      */           }
/*      */ 
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/* 3579 */       localObject1 = (paramSymbol.owner.kind == 2) && (paramSymbol.name != this.names._this) && (paramSymbol.name != this.names._super) ? this.types
/* 3579 */         .memberType(paramType1, paramSymbol) : 
/* 3579 */         paramSymbol.type;
/*      */ 
/* 3584 */       if ((((Symbol.VarSymbol)localObject2).getConstValue() != null) && (isStaticReference(paramJCTree))) {
/* 3585 */         localObject1 = ((Type)localObject1).constType(((Symbol.VarSymbol)localObject2).getConstValue());
/*      */       }
/* 3587 */       if (paramResultInfo.pkind == 12)
/* 3588 */         localObject1 = capture((Type)localObject1); break;
/*      */     case 16:
/* 3592 */       localObject1 = checkMethod(paramType1, paramSymbol, new ResultInfo(paramResultInfo.pkind, paramResultInfo.pt
/* 3593 */         .getReturnType(), paramResultInfo.checkContext), paramEnv, 
/* 3594 */         TreeInfo.args(paramEnv.tree), 
/* 3594 */         paramResultInfo.pt.getParameterTypes(), paramResultInfo.pt
/* 3595 */         .getTypeArguments());
/* 3596 */       break;
/*      */     case 1:
/*      */     case 63:
/* 3599 */       localObject1 = paramSymbol.type;
/* 3600 */       break;
/*      */     default:
/* 3602 */       throw new AssertionError("unexpected kind: " + paramSymbol.kind + " in tree " + paramJCTree);
/*      */     }
/*      */ 
/* 3610 */     if (paramSymbol.name != this.names.init) {
/* 3611 */       this.chk.checkDeprecated(paramJCTree.pos(), ((AttrContext)paramEnv.info).scope.owner, paramSymbol);
/* 3612 */       this.chk.checkSunAPI(paramJCTree.pos(), paramSymbol);
/* 3613 */       this.chk.checkProfile(paramJCTree.pos(), paramSymbol);
/*      */     }
/*      */ 
/* 3618 */     return check(paramJCTree, (Type)localObject1, paramSymbol.kind, paramResultInfo);
/*      */   }
/*      */ 
/*      */   private void checkInit(JCTree paramJCTree, Env<AttrContext> paramEnv, Symbol.VarSymbol paramVarSymbol, boolean paramBoolean)
/*      */   {
/* 3643 */     if (((((AttrContext)paramEnv.info).enclVar == paramVarSymbol) || (paramVarSymbol.pos > paramJCTree.pos)) && (paramVarSymbol.owner.kind == 2))
/*      */     {
/* 3645 */       if ((enclosingInitEnv(paramEnv) != null) && 
/* 3646 */         (paramVarSymbol.owner == ((AttrContext)paramEnv.info).scope.owner
/* 3646 */         .enclClass())) {
/* 3647 */         if ((((paramVarSymbol
/* 3647 */           .flags() & 0x8) != 0L) == Resolve.isStatic(paramEnv)) && (
/* 3648 */           (!paramEnv.tree
/* 3648 */           .hasTag(JCTree.Tag.ASSIGN)) || 
/* 3649 */           (TreeInfo.skipParens(((JCTree.JCAssign)paramEnv.tree).lhs) != 
/* 3649 */           paramJCTree))) {
/* 3650 */           String str = ((AttrContext)paramEnv.info).enclVar == paramVarSymbol ? "self.ref" : "forward.ref";
/*      */ 
/* 3652 */           if ((!paramBoolean) || (isStaticEnumField(paramVarSymbol)))
/* 3653 */             this.log.error(paramJCTree.pos(), "illegal." + str, new Object[0]);
/* 3654 */           else if (this.useBeforeDeclarationWarning)
/* 3655 */             this.log.warning(paramJCTree.pos(), str, new Object[] { paramVarSymbol });
/*      */         }
/*      */       }
/*      */     }
/* 3659 */     paramVarSymbol.getConstValue();
/*      */ 
/* 3661 */     checkEnumInitializer(paramJCTree, paramEnv, paramVarSymbol);
/*      */   }
/*      */ 
/*      */   Env<AttrContext> enclosingInitEnv(Env<AttrContext> paramEnv)
/*      */   {
/*      */     while (true)
/*      */     {
/* 3670 */       switch (14.$SwitchMap$com$sun$tools$javac$tree$JCTree$Tag[paramEnv.tree.getTag().ordinal()]) {
/*      */       case 16:
/* 3672 */         JCTree.JCVariableDecl localJCVariableDecl = (JCTree.JCVariableDecl)paramEnv.tree;
/* 3673 */         if (localJCVariableDecl.sym.owner.kind == 2)
/*      */         {
/* 3675 */           return paramEnv;
/*      */         }
/*      */         break;
/*      */       case 17:
/* 3679 */         if (paramEnv.next.tree.hasTag(JCTree.Tag.CLASSDEF))
/*      */         {
/* 3681 */           return paramEnv;
/*      */         }
/*      */         break;
/*      */       case 14:
/*      */       case 15:
/*      */       case 18:
/* 3687 */         return null;
/*      */       }
/* 3689 */       Assert.checkNonNull(paramEnv.next);
/* 3690 */       paramEnv = paramEnv.next;
/*      */     }
/*      */   }
/*      */ 
/*      */   private void checkEnumInitializer(JCTree paramJCTree, Env<AttrContext> paramEnv, Symbol.VarSymbol paramVarSymbol)
/*      */   {
/* 3716 */     if (isStaticEnumField(paramVarSymbol)) {
/* 3717 */       Symbol.ClassSymbol localClassSymbol = ((AttrContext)paramEnv.info).scope.owner.enclClass();
/*      */ 
/* 3719 */       if ((localClassSymbol == null) || (localClassSymbol.owner == null)) {
/* 3720 */         return;
/*      */       }
/*      */ 
/* 3725 */       if ((paramVarSymbol.owner != localClassSymbol) && (!this.types.isSubtype(localClassSymbol.type, paramVarSymbol.owner.type))) {
/* 3726 */         return;
/*      */       }
/*      */ 
/* 3730 */       if (!Resolve.isInitializer(paramEnv)) {
/* 3731 */         return;
/*      */       }
/* 3733 */       this.log.error(paramJCTree.pos(), "illegal.enum.static.ref", new Object[0]);
/*      */     }
/*      */   }
/*      */ 
/*      */   private boolean isStaticEnumField(Symbol.VarSymbol paramVarSymbol)
/*      */   {
/* 3743 */     return (Flags.isEnum(paramVarSymbol.owner)) && 
/* 3742 */       (Flags.isStatic(paramVarSymbol)) && 
/* 3743 */       (!Flags.isConstant(paramVarSymbol)) && 
/* 3743 */       (paramVarSymbol.name != this.names._class);
/*      */   }
/*      */ 
/*      */   public Type checkMethod(Type paramType, final Symbol paramSymbol, ResultInfo paramResultInfo, Env<AttrContext> paramEnv, List<JCTree.JCExpression> paramList, List<Type> paramList1, List<Type> paramList2)
/*      */   {
/*      */     Object localObject1;
/* 3761 */     if ((this.allowGenerics) && 
/* 3762 */       ((paramSymbol
/* 3762 */       .flags() & 0x8) == 0L) && (
/* 3763 */       (paramType
/* 3763 */       .hasTag(TypeTag.CLASS)) || 
/* 3763 */       (paramType.hasTag(TypeTag.TYPEVAR)))) {
/* 3764 */       localObject1 = this.types.asOuterSuper(paramType, paramSymbol.owner);
/* 3765 */       if ((localObject1 != null) && (((Type)localObject1).isRaw()) && 
/* 3766 */         (!this.types
/* 3766 */         .isSameTypes(paramSymbol.type
/* 3766 */         .getParameterTypes(), paramSymbol
/* 3767 */         .erasure(this.types)
/* 3767 */         .getParameterTypes())))
/* 3768 */         this.chk.warnUnchecked(paramEnv.tree.pos(), "unchecked.call.mbr.of.raw.type", new Object[] { paramSymbol, localObject1 });
/*      */     }
/*      */     Object localObject2;
/*      */     Object localObject3;
/* 3774 */     if (((AttrContext)paramEnv.info).defaultSuperCallSite != null) {
/* 3775 */       for (localObject1 = this.types.interfaces(paramEnv.enclClass.type).prepend(this.types.supertype(paramEnv.enclClass.type)).iterator(); ((Iterator)localObject1).hasNext(); ) { localObject2 = (Type)((Iterator)localObject1).next();
/* 3776 */         if ((((Type)localObject2).tsym.isSubClass(paramSymbol.enclClass(), this.types)) && 
/* 3777 */           (!this.types
/* 3777 */           .isSameType((Type)localObject2, ((AttrContext)paramEnv.info).defaultSuperCallSite)))
/*      */         {
/* 3779 */           localObject3 = this.types
/* 3779 */             .interfaceCandidates((Type)localObject2, (Symbol.MethodSymbol)paramSymbol);
/*      */ 
/* 3780 */           if ((((List)localObject3).nonEmpty()) && (((List)localObject3).head != paramSymbol))
/*      */           {
/* 3782 */             if (((Symbol.MethodSymbol)((List)localObject3).head)
/* 3782 */               .overrides(paramSymbol, ((Symbol.MethodSymbol)((List)localObject3).head)
/* 3782 */               .enclClass(), this.types, true)) {
/* 3783 */               this.log.error(paramEnv.tree.pos(), "illegal.default.super.call", new Object[] { ((AttrContext)paramEnv.info).defaultSuperCallSite, this.diags
/* 3784 */                 .fragment("overridden.default", new Object[] { paramSymbol, localObject2 }) });
/*      */ 
/* 3785 */               break;
/*      */             }
/*      */           }
/*      */         } } ((AttrContext)paramEnv.info).defaultSuperCallSite = null;
/*      */     }
/*      */ 
/* 3791 */     if ((paramSymbol.isStatic()) && (paramType.isInterface()) && (paramEnv.tree.hasTag(JCTree.Tag.APPLY))) {
/* 3792 */       localObject1 = (JCTree.JCMethodInvocation)paramEnv.tree;
/* 3793 */       if ((((JCTree.JCMethodInvocation)localObject1).meth.hasTag(JCTree.Tag.SELECT)) && 
/* 3794 */         (!TreeInfo.isStaticSelector(((JCTree.JCFieldAccess)((JCTree.JCMethodInvocation)localObject1).meth).selected, this.names)))
/*      */       {
/* 3795 */         this.log.error(paramEnv.tree.pos(), "illegal.static.intf.meth.call", new Object[] { paramType });
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/* 3803 */     this.noteWarner.clear();
/*      */     try {
/* 3805 */       localObject1 = this.rs.checkMethod(paramEnv, paramType, paramSymbol, paramResultInfo, paramList1, paramList2, this.noteWarner);
/*      */       DeferredAttr tmp524_521 = this.deferredAttr; tmp524_521.getClass(); localObject2 = new DeferredAttr.DeferredTypeMap(tmp524_521, DeferredAttr.AttrMode.CHECK, paramSymbol, ((AttrContext)paramEnv.info).pendingResolutionPhase);
/*      */ 
/* 3817 */       paramList1 = Type.map(paramList1, (Type.Mapping)localObject2);
/*      */ 
/* 3819 */       if (this.noteWarner.hasNonSilentLint(Lint.LintCategory.UNCHECKED)) {
/* 3820 */         this.chk.warnUnchecked(paramEnv.tree.pos(), "unchecked.meth.invocation.applied", new Object[] { 
/* 3822 */           Kinds.kindName(paramSymbol), 
/* 3822 */           paramSymbol.name, this.rs
/* 3824 */           .methodArguments(paramSymbol.type
/* 3824 */           .getParameterTypes()), this.rs
/* 3825 */           .methodArguments(Type.map(paramList1, (Type.Mapping)localObject2)), 
/* 3826 */           Kinds.kindName(paramSymbol
/* 3826 */           .location()), paramSymbol
/* 3827 */           .location() });
/*      */ 
/* 3830 */         localObject1 = new Type.MethodType(((Type)localObject1).getParameterTypes(), this.types
/* 3829 */           .erasure(((Type)localObject1)
/* 3829 */           .getReturnType()), this.types
/* 3830 */           .erasure(((Type)localObject1)
/* 3830 */           .getThrownTypes()), this.syms.methodClass);
/*      */       }
/*      */ 
/* 3834 */       return this.chk.checkMethod((Type)localObject1, paramSymbol, paramEnv, paramList, paramList1, ((AttrContext)paramEnv.info).lastResolveVarargs(), paramResultInfo.checkContext
/* 3835 */         .inferenceContext());
/*      */     }
/*      */     catch (Infer.InferenceException localInferenceException)
/*      */     {
/* 3839 */       paramResultInfo.checkContext.report(paramEnv.tree.pos(), localInferenceException.getDiagnostic());
/* 3840 */       return this.types.createErrorType(paramType);
/*      */     } catch (Resolve.InapplicableMethodException localInapplicableMethodException) {
/* 3842 */       localObject2 = localInapplicableMethodException.getDiagnostic();
/*      */       Resolve tmp792_789 = this.rs; tmp792_789.getClass(); localObject3 = new Resolve.InapplicableSymbolError(tmp792_789, null)
/*      */       {
/*      */         protected Pair<Symbol, JCDiagnostic> errCandidate() {
/* 3846 */           return new Pair(paramSymbol, this.val$diag);
/*      */         }
/*      */       };
/*      */       Resolve tmp816_813 = this.rs; tmp816_813.getClass(); List localList = Type.map(paramList1, new Resolve.ResolveDeferredRecoveryMap(tmp816_813, DeferredAttr.AttrMode.CHECK, paramSymbol, ((AttrContext)paramEnv.info).pendingResolutionPhase));
/*      */ 
/* 3851 */       JCDiagnostic localJCDiagnostic = ((Resolve.InapplicableSymbolError)localObject3).getDiagnostic(JCDiagnostic.DiagnosticType.ERROR, paramEnv.tree, paramSymbol, paramType, paramSymbol.name, localList, paramList2);
/*      */ 
/* 3853 */       this.log.report(localJCDiagnostic);
/* 3854 */     }return this.types.createErrorType(paramType);
/*      */   }
/*      */ 
/*      */   public void visitLiteral(JCTree.JCLiteral paramJCLiteral)
/*      */   {
/* 3859 */     this.result = check(paramJCLiteral, 
/* 3860 */       litType(paramJCLiteral.typetag)
/* 3860 */       .constType(paramJCLiteral.value), 12, this.resultInfo);
/*      */   }
/*      */ 
/*      */   Type litType(TypeTag paramTypeTag)
/*      */   {
/* 3866 */     return paramTypeTag == TypeTag.CLASS ? this.syms.stringType : this.syms.typeOfTag[paramTypeTag.ordinal()];
/*      */   }
/*      */ 
/*      */   public void visitTypeIdent(JCTree.JCPrimitiveTypeTree paramJCPrimitiveTypeTree) {
/* 3870 */     this.result = check(paramJCPrimitiveTypeTree, this.syms.typeOfTag[paramJCPrimitiveTypeTree.typetag.ordinal()], 2, this.resultInfo);
/*      */   }
/*      */ 
/*      */   public void visitTypeArray(JCTree.JCArrayTypeTree paramJCArrayTypeTree) {
/* 3874 */     Type localType = attribType(paramJCArrayTypeTree.elemtype, this.env);
/* 3875 */     Type.ArrayType localArrayType = new Type.ArrayType(localType, this.syms.arrayClass);
/* 3876 */     this.result = check(paramJCArrayTypeTree, localArrayType, 2, this.resultInfo);
/*      */   }
/*      */ 
/*      */   public void visitTypeApply(JCTree.JCTypeApply paramJCTypeApply)
/*      */   {
/* 3884 */     Object localObject1 = this.types.createErrorType(paramJCTypeApply.type);
/*      */ 
/* 3887 */     Type localType1 = this.chk.checkClassType(paramJCTypeApply.clazz.pos(), attribType(paramJCTypeApply.clazz, this.env));
/*      */ 
/* 3890 */     Object localObject2 = attribTypes(paramJCTypeApply.arguments, this.env);
/*      */ 
/* 3892 */     if (localType1.hasTag(TypeTag.CLASS)) {
/* 3893 */       List localList1 = localType1.tsym.type.getTypeArguments();
/* 3894 */       if (((List)localObject2).isEmpty()) {
/* 3895 */         localObject2 = localList1;
/*      */       }
/* 3897 */       if (((List)localObject2).length() == localList1.length()) {
/* 3898 */         Object localObject3 = localObject2;
/* 3899 */         List localList2 = localList1;
/* 3900 */         while (((List)localObject3).nonEmpty()) {
/* 3901 */           ((List)localObject3).head = ((Type)((List)localObject3).head).withTypeVar((Type)localList2.head);
/* 3902 */           localObject3 = ((List)localObject3).tail;
/* 3903 */           localList2 = localList2.tail;
/*      */         }
/*      */ 
/* 3906 */         Object localObject4 = localType1.getEnclosingType();
/* 3907 */         if (((Type)localObject4).hasTag(TypeTag.CLASS))
/*      */         {
/* 3909 */           JCTree.JCExpression localJCExpression = TreeInfo.typeIn(paramJCTypeApply.clazz);
/*      */           Type localType2;
/* 3910 */           if (localJCExpression.hasTag(JCTree.Tag.IDENT))
/* 3911 */             localType2 = this.env.enclClass.sym.type;
/* 3912 */           else if (localJCExpression.hasTag(JCTree.Tag.SELECT))
/* 3913 */             localType2 = ((JCTree.JCFieldAccess)localJCExpression).selected.type;
/* 3914 */           else throw new AssertionError("" + paramJCTypeApply);
/* 3915 */           if ((((Type)localObject4).hasTag(TypeTag.CLASS)) && (localType2 != localObject4)) {
/* 3916 */             if (localType2.hasTag(TypeTag.CLASS))
/* 3917 */               localType2 = this.types.asOuterSuper(localType2, ((Type)localObject4).tsym);
/* 3918 */             if (localType2 == null)
/* 3919 */               localType2 = this.types.erasure((Type)localObject4);
/* 3920 */             localObject4 = localType2;
/*      */           }
/*      */         }
/* 3923 */         localObject1 = new Type.ClassType((Type)localObject4, (List)localObject2, localType1.tsym);
/*      */       } else {
/* 3925 */         if (localList1.length() != 0)
/* 3926 */           this.log.error(paramJCTypeApply.pos(), "wrong.number.type.args", new Object[] { 
/* 3927 */             Integer.toString(localList1
/* 3927 */             .length()) });
/*      */         else {
/* 3929 */           this.log.error(paramJCTypeApply.pos(), "type.doesnt.take.params", new Object[] { localType1.tsym });
/*      */         }
/* 3931 */         localObject1 = this.types.createErrorType(paramJCTypeApply.type);
/*      */       }
/*      */     }
/* 3934 */     this.result = check(paramJCTypeApply, (Type)localObject1, 2, this.resultInfo);
/*      */   }
/*      */ 
/*      */   public void visitTypeUnion(JCTree.JCTypeUnion paramJCTypeUnion) {
/* 3938 */     ListBuffer localListBuffer1 = new ListBuffer();
/* 3939 */     ListBuffer localListBuffer2 = null;
/* 3940 */     for (Object localObject1 = paramJCTypeUnion.alternatives.iterator(); ((Iterator)localObject1).hasNext(); ) { localObject2 = (JCTree.JCExpression)((Iterator)localObject1).next();
/* 3941 */       Type localType1 = attribType((JCTree)localObject2, this.env);
/* 3942 */       localType1 = this.chk.checkType(((JCTree.JCExpression)localObject2).pos(), this.chk
/* 3943 */         .checkClassType(((JCTree.JCExpression)localObject2)
/* 3943 */         .pos(), localType1), this.syms.throwableType);
/*      */ 
/* 3945 */       if (!localType1.isErroneous())
/*      */       {
/* 3948 */         if (this.chk.intersects(localType1, localListBuffer1.toList())) {
/* 3949 */           for (Type localType2 : localListBuffer1) {
/* 3950 */             boolean bool1 = this.types.isSubtype(localType1, localType2);
/* 3951 */             boolean bool2 = this.types.isSubtype(localType2, localType1);
/* 3952 */             if ((bool1) || (bool2))
/*      */             {
/* 3954 */               Type localType3 = bool1 ? localType1 : localType2;
/* 3955 */               Type localType4 = bool1 ? localType2 : localType1;
/* 3956 */               this.log.error(((JCTree.JCExpression)localObject2).pos(), "multicatch.types.must.be.disjoint", new Object[] { localType3, localType4 });
/*      */             }
/*      */           }
/*      */         }
/* 3960 */         localListBuffer1.append(localType1);
/* 3961 */         if (localListBuffer2 != null)
/* 3962 */           localListBuffer2.append(localType1);
/*      */       } else {
/* 3964 */         if (localListBuffer2 == null) {
/* 3965 */           localListBuffer2 = new ListBuffer();
/* 3966 */           localListBuffer2.appendList(localListBuffer1);
/*      */         }
/* 3968 */         localListBuffer2.append(localType1);
/*      */       }
/*      */     }
/*      */     Object localObject2;
/* 3971 */     localObject1 = check(paramJCTypeUnion, this.types.lub(localListBuffer1.toList()), 2, this.resultInfo);
/* 3972 */     if (((Type)localObject1).hasTag(TypeTag.CLASS))
/*      */     {
/* 3974 */       localObject2 = (localListBuffer2 == null ? localListBuffer1 : localListBuffer2)
/* 3974 */         .toList();
/* 3975 */       localObject1 = new Type.UnionClassType((Type.ClassType)localObject1, (List)localObject2);
/*      */     }
/* 3977 */     paramJCTypeUnion.type = (this.result = localObject1);
/*      */   }
/*      */ 
/*      */   public void visitTypeIntersection(JCTree.JCTypeIntersection paramJCTypeIntersection) {
/* 3981 */     attribTypes(paramJCTypeIntersection.bounds, this.env);
/* 3982 */     paramJCTypeIntersection.type = (this.result = checkIntersection(paramJCTypeIntersection, paramJCTypeIntersection.bounds));
/*      */   }
/*      */ 
/*      */   public void visitTypeParameter(JCTree.JCTypeParameter paramJCTypeParameter) {
/* 3986 */     Type.TypeVar localTypeVar = (Type.TypeVar)paramJCTypeParameter.type;
/*      */ 
/* 3988 */     if ((paramJCTypeParameter.annotations != null) && (paramJCTypeParameter.annotations.nonEmpty())) {
/* 3989 */       annotateType(paramJCTypeParameter, paramJCTypeParameter.annotations);
/*      */     }
/*      */ 
/* 3992 */     if (!localTypeVar.bound.isErroneous())
/*      */     {
/* 3994 */       localTypeVar.bound = checkIntersection(paramJCTypeParameter, paramJCTypeParameter.bounds);
/*      */     }
/*      */   }
/*      */ 
/*      */   Type checkIntersection(JCTree paramJCTree, List<JCTree.JCExpression> paramList) {
/* 3999 */     HashSet localHashSet = new HashSet();
/* 4000 */     if (paramList.nonEmpty())
/*      */     {
/* 4002 */       ((JCTree.JCExpression)paramList.head).type = checkBase(((JCTree.JCExpression)paramList.head).type, (JCTree)paramList.head, this.env, false, false, false);
/* 4003 */       localHashSet.add(this.types.erasure(((JCTree.JCExpression)paramList.head).type));
/* 4004 */       if (((JCTree.JCExpression)paramList.head).type.isErroneous()) {
/* 4005 */         return ((JCTree.JCExpression)paramList.head).type;
/*      */       }
/* 4007 */       if (((JCTree.JCExpression)paramList.head).type.hasTag(TypeTag.TYPEVAR))
/*      */       {
/* 4009 */         if (paramList.tail.nonEmpty()) {
/* 4010 */           this.log.error(((JCTree.JCExpression)paramList.tail.head).pos(), "type.var.may.not.be.followed.by.other.bounds", new Object[0]);
/*      */ 
/* 4012 */           return ((JCTree.JCExpression)paramList.head).type;
/*      */         }
/*      */       }
/*      */       else
/*      */       {
/* 4017 */         for (localObject1 = paramList.tail.iterator(); ((Iterator)localObject1).hasNext(); ) { localJCExpression = (JCTree.JCExpression)((Iterator)localObject1).next();
/* 4018 */           localJCExpression.type = checkBase(localJCExpression.type, localJCExpression, this.env, false, true, false);
/* 4019 */           if (localJCExpression.type.isErroneous()) {
/* 4020 */             paramList = List.of(localJCExpression);
/*      */           }
/* 4022 */           else if (localJCExpression.type.hasTag(TypeTag.CLASS))
/* 4023 */             this.chk.checkNotRepeated(localJCExpression.pos(), this.types.erasure(localJCExpression.type), localHashSet);
/*      */         }
/*      */       }
/*      */     }
/*      */     JCTree.JCExpression localJCExpression;
/* 4029 */     if (paramList.length() == 0)
/* 4030 */       return this.syms.objectType;
/* 4031 */     if (paramList.length() == 1) {
/* 4032 */       return ((JCTree.JCExpression)paramList.head).type;
/*      */     }
/* 4034 */     Object localObject1 = this.types.makeIntersectionType(TreeInfo.types(paramList));
/*      */     Object localObject2;
/* 4041 */     if (!((JCTree.JCExpression)paramList.head).type.isInterface()) {
/* 4042 */       localJCExpression = (JCTree.JCExpression)paramList.head;
/* 4043 */       localObject2 = paramList.tail;
/*      */     } else {
/* 4045 */       localJCExpression = null;
/* 4046 */       localObject2 = paramList;
/*      */     }
/* 4048 */     JCTree.JCClassDecl localJCClassDecl = this.make.at(paramJCTree).ClassDef(this.make
/* 4049 */       .Modifiers(1025L), 
/* 4049 */       this.names.empty, 
/* 4050 */       List.nil(), localJCExpression, (List)localObject2, 
/* 4051 */       List.nil());
/*      */ 
/* 4053 */     Symbol.ClassSymbol localClassSymbol = (Symbol.ClassSymbol)((Type)localObject1).tsym;
/* 4054 */     Assert.check((localClassSymbol.flags() & 0x1000000) != 0L);
/* 4055 */     localJCClassDecl.sym = localClassSymbol;
/* 4056 */     localClassSymbol.sourcefile = this.env.toplevel.sourcefile;
/*      */ 
/* 4059 */     localClassSymbol.flags_field |= 268435456L;
/* 4060 */     Env localEnv = this.enter.classEnv(localJCClassDecl, this.env);
/* 4061 */     this.typeEnvs.put(localClassSymbol, localEnv);
/* 4062 */     attribClass(localClassSymbol);
/* 4063 */     return localObject1;
/*      */   }
/*      */ 
/*      */   public void visitWildcard(JCTree.JCWildcard paramJCWildcard)
/*      */   {
/* 4071 */     Type localType = paramJCWildcard.kind.kind == BoundKind.UNBOUND ? this.syms.objectType : 
/* 4071 */       attribType(paramJCWildcard.inner, this.env);
/*      */ 
/* 4072 */     this.result = check(paramJCWildcard, new Type.WildcardType(this.chk.checkRefType(paramJCWildcard.pos(), localType), paramJCWildcard.kind.kind, this.syms.boundClass), 2, this.resultInfo);
/*      */   }
/*      */ 
/*      */   public void visitAnnotation(JCTree.JCAnnotation paramJCAnnotation)
/*      */   {
/* 4079 */     Assert.error("should be handled in Annotate");
/*      */   }
/*      */ 
/*      */   public void visitAnnotatedType(JCTree.JCAnnotatedType paramJCAnnotatedType) {
/* 4083 */     Type localType = attribType(paramJCAnnotatedType.getUnderlyingType(), this.env);
/* 4084 */     attribAnnotationTypes(paramJCAnnotatedType.annotations, this.env);
/* 4085 */     annotateType(paramJCAnnotatedType, paramJCAnnotatedType.annotations);
/* 4086 */     this.result = (paramJCAnnotatedType.type = localType);
/*      */   }
/*      */ 
/*      */   public void annotateType(final JCTree paramJCTree, final List<JCTree.JCAnnotation> paramList)
/*      */   {
/* 4093 */     this.annotate.typeAnnotation(new Annotate.Worker()
/*      */     {
/*      */       public String toString() {
/* 4096 */         return "annotate " + paramList + " onto " + paramJCTree;
/*      */       }
/*      */ 
/*      */       public void run() {
/* 4100 */         List localList = Attr.fromAnnotations(paramList);
/* 4101 */         if (paramList.size() == localList.size())
/*      */         {
/* 4103 */           paramJCTree.type = paramJCTree.type.unannotatedType().annotatedType(localList);
/*      */         }
/*      */       }
/*      */     });
/*      */   }
/*      */ 
/*      */   private static List<Attribute.TypeCompound> fromAnnotations(List<JCTree.JCAnnotation> paramList) {
/* 4110 */     if (paramList.isEmpty()) {
/* 4111 */       return List.nil();
/*      */     }
/*      */ 
/* 4114 */     ListBuffer localListBuffer = new ListBuffer();
/* 4115 */     for (JCTree.JCAnnotation localJCAnnotation : paramList) {
/* 4116 */       if (localJCAnnotation.attribute != null)
/*      */       {
/* 4122 */         localListBuffer.append((Attribute.TypeCompound)localJCAnnotation.attribute);
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/* 4129 */     return localListBuffer.toList();
/*      */   }
/*      */ 
/*      */   public void visitErroneous(JCTree.JCErroneous paramJCErroneous) {
/* 4133 */     if (paramJCErroneous.errs != null)
/* 4134 */       for (JCTree localJCTree : paramJCErroneous.errs)
/* 4135 */         attribTree(localJCTree, this.env, new ResultInfo(63, pt()));
/* 4136 */     this.result = (paramJCErroneous.type = this.syms.errType);
/*      */   }
/*      */ 
/*      */   public void visitTree(JCTree paramJCTree)
/*      */   {
/* 4142 */     throw new AssertionError();
/*      */   }
/*      */ 
/*      */   public void attrib(Env<AttrContext> paramEnv)
/*      */   {
/* 4149 */     if (paramEnv.tree.hasTag(JCTree.Tag.TOPLEVEL))
/* 4150 */       attribTopLevel(paramEnv);
/*      */     else
/* 4152 */       attribClass(paramEnv.tree.pos(), paramEnv.enclClass.sym);
/*      */   }
/*      */ 
/*      */   public void attribTopLevel(Env<AttrContext> paramEnv)
/*      */   {
/* 4160 */     JCTree.JCCompilationUnit localJCCompilationUnit = paramEnv.toplevel;
/*      */     try {
/* 4162 */       this.annotate.flush();
/*      */     } catch (Symbol.CompletionFailure localCompletionFailure) {
/* 4164 */       this.chk.completionError(localJCCompilationUnit.pos(), localCompletionFailure);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void attribClass(JCDiagnostic.DiagnosticPosition paramDiagnosticPosition, Symbol.ClassSymbol paramClassSymbol)
/*      */   {
/*      */     try
/*      */     {
/* 4176 */       this.annotate.flush();
/* 4177 */       attribClass(paramClassSymbol);
/*      */     } catch (Symbol.CompletionFailure localCompletionFailure) {
/* 4179 */       this.chk.completionError(paramDiagnosticPosition, localCompletionFailure);
/*      */     }
/*      */   }
/*      */ 
/*      */   void attribClass(Symbol.ClassSymbol paramClassSymbol)
/*      */     throws Symbol.CompletionFailure
/*      */   {
/* 4187 */     if (paramClassSymbol.type.hasTag(TypeTag.ERROR)) return;
/*      */ 
/* 4191 */     this.chk.checkNonCyclic(null, paramClassSymbol.type);
/*      */ 
/* 4193 */     Type localType = this.types.supertype(paramClassSymbol.type);
/* 4194 */     if ((paramClassSymbol.flags_field & 0x1000000) == 0L)
/*      */     {
/* 4196 */       if (localType.hasTag(TypeTag.CLASS)) {
/* 4197 */         attribClass((Symbol.ClassSymbol)localType.tsym);
/*      */       }
/*      */ 
/* 4200 */       if ((paramClassSymbol.owner.kind == 2) && (paramClassSymbol.owner.type.hasTag(TypeTag.CLASS))) {
/* 4201 */         attribClass((Symbol.ClassSymbol)paramClassSymbol.owner);
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/* 4207 */     if ((paramClassSymbol.flags_field & 0x10000000) != 0L) {
/* 4208 */       paramClassSymbol.flags_field &= -268435457L;
/*      */ 
/* 4211 */       Env localEnv1 = this.typeEnvs.get(paramClassSymbol);
/*      */ 
/* 4218 */       Env localEnv2 = localEnv1;
/* 4219 */       while (((AttrContext)localEnv2.info).lint == null) {
/* 4220 */         localEnv2 = localEnv2.next;
/*      */       }
/*      */ 
/* 4223 */       ((AttrContext)localEnv1.info).lint = ((AttrContext)localEnv2.info).lint.augment(paramClassSymbol);
/*      */ 
/* 4225 */       Lint localLint = this.chk.setLint(((AttrContext)localEnv1.info).lint);
/* 4226 */       JavaFileObject localJavaFileObject = this.log.useSource(paramClassSymbol.sourcefile);
/* 4227 */       ResultInfo localResultInfo = ((AttrContext)localEnv1.info).returnResult;
/*      */       try
/*      */       {
/* 4230 */         this.deferredLintHandler.flush(localEnv1.tree);
/* 4231 */         ((AttrContext)localEnv1.info).returnResult = null;
/*      */ 
/* 4233 */         if ((localType.tsym == this.syms.enumSym) && ((paramClassSymbol.flags_field & 0x1004000) == 0L))
/*      */         {
/* 4235 */           this.log.error(localEnv1.tree.pos(), "enum.no.subclassing", new Object[0]);
/*      */         }
/*      */ 
/* 4238 */         if ((localType.tsym != null) && ((localType.tsym.flags_field & 0x4000) != 0L) && ((paramClassSymbol.flags_field & 0x1004000) == 0L))
/*      */         {
/* 4241 */           this.log.error(localEnv1.tree.pos(), "enum.types.not.extensible", new Object[0]);
/*      */         }
/*      */ 
/* 4244 */         if (isSerializable(paramClassSymbol.type)) {
/* 4245 */           ((AttrContext)localEnv1.info).isSerializable = true;
/*      */         }
/*      */ 
/* 4248 */         attribClassBody(localEnv1, paramClassSymbol);
/*      */ 
/* 4250 */         this.chk.checkDeprecatedAnnotation(localEnv1.tree.pos(), paramClassSymbol);
/* 4251 */         this.chk.checkClassOverrideEqualsAndHashIfNeeded(localEnv1.tree.pos(), paramClassSymbol);
/* 4252 */         this.chk.checkFunctionalInterface((JCTree.JCClassDecl)localEnv1.tree, paramClassSymbol);
/*      */       } finally {
/* 4254 */         ((AttrContext)localEnv1.info).returnResult = localResultInfo;
/* 4255 */         this.log.useSource(localJavaFileObject);
/* 4256 */         this.chk.setLint(localLint);
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   public void visitImport(JCTree.JCImport paramJCImport)
/*      */   {
/*      */   }
/*      */ 
/*      */   private void attribClassBody(Env<AttrContext> paramEnv, Symbol.ClassSymbol paramClassSymbol)
/*      */   {
/* 4268 */     JCTree.JCClassDecl localJCClassDecl = (JCTree.JCClassDecl)paramEnv.tree;
/* 4269 */     Assert.check(paramClassSymbol == localJCClassDecl.sym);
/*      */ 
/* 4272 */     attribStats(localJCClassDecl.typarams, paramEnv);
/* 4273 */     if (!paramClassSymbol.isAnonymous())
/*      */     {
/* 4275 */       this.chk.validate(localJCClassDecl.typarams, paramEnv);
/* 4276 */       this.chk.validate(localJCClassDecl.extending, paramEnv);
/* 4277 */       this.chk.validate(localJCClassDecl.implementing, paramEnv);
/*      */     }
/*      */ 
/* 4280 */     paramClassSymbol.markAbstractIfNeeded(this.types);
/*      */ 
/* 4284 */     if (((paramClassSymbol.flags() & 0x600) == 0L) && 
/* 4285 */       (!this.relax))
/* 4286 */       this.chk.checkAllDefined(localJCClassDecl.pos(), paramClassSymbol);
/*      */     Object localObject2;
/* 4289 */     if ((paramClassSymbol.flags() & 0x2000) != 0L) {
/* 4290 */       if (localJCClassDecl.implementing.nonEmpty()) {
/* 4291 */         this.log.error(((JCTree.JCExpression)localJCClassDecl.implementing.head).pos(), "cant.extend.intf.annotation", new Object[0]);
/*      */       }
/* 4293 */       if (localJCClassDecl.typarams.nonEmpty()) {
/* 4294 */         this.log.error(((JCTree.JCTypeParameter)localJCClassDecl.typarams.head).pos(), "intf.annotation.cant.have.type.params", new Object[0]);
/*      */       }
/*      */ 
/* 4298 */       localObject1 = paramClassSymbol.attribute(this.syms.repeatableType.tsym);
/* 4299 */       if (localObject1 != null)
/*      */       {
/* 4301 */         localObject2 = getDiagnosticPosition(localJCClassDecl, ((Attribute.Compound)localObject1).type);
/* 4302 */         Assert.checkNonNull(localObject2);
/*      */ 
/* 4304 */         this.chk.validateRepeatable(paramClassSymbol, (Attribute.Compound)localObject1, (JCDiagnostic.DiagnosticPosition)localObject2);
/*      */       }
/*      */ 
/*      */     }
/*      */     else
/*      */     {
/* 4310 */       this.chk.checkCompatibleSupertypes(localJCClassDecl.pos(), paramClassSymbol.type);
/* 4311 */       if (this.allowDefaultMethods) {
/* 4312 */         this.chk.checkDefaultMethodClashes(localJCClassDecl.pos(), paramClassSymbol.type);
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/* 4318 */     this.chk.checkClassBounds(localJCClassDecl.pos(), paramClassSymbol.type);
/*      */ 
/* 4320 */     localJCClassDecl.type = paramClassSymbol.type;
/*      */ 
/* 4322 */     for (Object localObject1 = localJCClassDecl.typarams; 
/* 4323 */       ((List)localObject1).nonEmpty(); localObject1 = ((List)localObject1).tail) {
/* 4324 */       Assert.checkNonNull(((AttrContext)paramEnv.info).scope.lookup(((JCTree.JCTypeParameter)((List)localObject1).head).name).scope);
/*      */     }
/*      */ 
/* 4328 */     if ((!paramClassSymbol.type.allparams().isEmpty()) && (this.types.isSubtype(paramClassSymbol.type, this.syms.throwableType))) {
/* 4329 */       this.log.error(localJCClassDecl.extending.pos(), "generic.throwable", new Object[0]);
/*      */     }
/*      */ 
/* 4333 */     this.chk.checkImplementations(localJCClassDecl);
/*      */ 
/* 4336 */     checkAutoCloseable(localJCClassDecl.pos(), paramEnv, paramClassSymbol.type);
/*      */ 
/* 4338 */     for (localObject1 = localJCClassDecl.defs; ((List)localObject1).nonEmpty(); localObject1 = ((List)localObject1).tail)
/*      */     {
/* 4340 */       attribStat((JCTree)((List)localObject1).head, paramEnv);
/*      */ 
/* 4343 */       if ((paramClassSymbol.owner.kind != 1) && 
/* 4344 */         (((paramClassSymbol
/* 4344 */         .flags() & 0x8) == 0L) || (paramClassSymbol.name == this.names.empty)) && 
/* 4345 */         ((TreeInfo.flags((JCTree)((List)localObject1).head) & 
/* 4345 */         0x208) != 0L)) {
/* 4346 */         localObject2 = null;
/* 4347 */         if (((JCTree)((List)localObject1).head).hasTag(JCTree.Tag.VARDEF)) localObject2 = ((JCTree.JCVariableDecl)((List)localObject1).head).sym;
/* 4348 */         if ((localObject2 == null) || (((Symbol)localObject2).kind != 4) || 
/* 4350 */           (((Symbol.VarSymbol)localObject2)
/* 4350 */           .getConstValue() == null)) {
/* 4351 */           this.log.error(((JCTree)((List)localObject1).head).pos(), "icls.cant.have.static.decl", new Object[] { paramClassSymbol });
/*      */         }
/*      */       }
/*      */     }
/*      */ 
/* 4356 */     this.chk.checkCyclicConstructors(localJCClassDecl);
/*      */ 
/* 4359 */     this.chk.checkNonCyclicElements(localJCClassDecl);
/*      */ 
/* 4362 */     if ((((AttrContext)paramEnv.info).lint.isEnabled(Lint.LintCategory.SERIAL)) && 
/* 4363 */       (isSerializable(paramClassSymbol.type)) && 
/* 4364 */       ((paramClassSymbol
/* 4364 */       .flags() & 0x4000) == 0L) && 
/* 4365 */       (checkForSerial(paramClassSymbol)))
/*      */     {
/* 4366 */       checkSerialVersionUID(localJCClassDecl, paramClassSymbol);
/*      */     }
/* 4368 */     if (this.allowTypeAnnos)
/*      */     {
/* 4370 */       this.typeAnnotations.organizeTypeAnnotationsBodies(localJCClassDecl);
/*      */ 
/* 4373 */       validateTypeAnnotations(localJCClassDecl, false);
/*      */     }
/*      */   }
/*      */ 
/*      */   boolean checkForSerial(Symbol.ClassSymbol paramClassSymbol) {
/* 4378 */     if ((paramClassSymbol.flags() & 0x400) == 0L) {
/* 4379 */       return true;
/*      */     }
/* 4381 */     return paramClassSymbol.members().anyMatch(anyNonAbstractOrDefaultMethod);
/*      */   }
/*      */ 
/*      */   private JCDiagnostic.DiagnosticPosition getDiagnosticPosition(JCTree.JCClassDecl paramJCClassDecl, Type paramType)
/*      */   {
/* 4395 */     for (List localList = paramJCClassDecl.mods.annotations; !localList.isEmpty(); localList = localList.tail) {
/* 4396 */       if (this.types.isSameType(((JCTree.JCAnnotation)localList.head).annotationType.type, paramType)) {
/* 4397 */         return ((JCTree.JCAnnotation)localList.head).pos();
/*      */       }
/*      */     }
/* 4400 */     return null;
/*      */   }
/*      */ 
/*      */   boolean isSerializable(Type paramType)
/*      */   {
/*      */     try {
/* 4406 */       this.syms.serializableType.complete();
/*      */     }
/*      */     catch (Symbol.CompletionFailure localCompletionFailure) {
/* 4409 */       return false;
/*      */     }
/* 4411 */     return this.types.isSubtype(paramType, this.syms.serializableType);
/*      */   }
/*      */ 
/*      */   private void checkSerialVersionUID(JCTree.JCClassDecl paramJCClassDecl, Symbol.ClassSymbol paramClassSymbol)
/*      */   {
/* 4418 */     Scope.Entry localEntry = paramClassSymbol.members().lookup(this.names.serialVersionUID);
/* 4419 */     while ((localEntry.scope != null) && (localEntry.sym.kind != 4)) localEntry = localEntry.next();
/* 4420 */     if (localEntry.scope == null) {
/* 4421 */       this.log.warning(Lint.LintCategory.SERIAL, paramJCClassDecl
/* 4422 */         .pos(), "missing.SVUID", new Object[] { paramClassSymbol });
/* 4423 */       return;
/*      */     }
/*      */ 
/* 4427 */     Symbol.VarSymbol localVarSymbol = (Symbol.VarSymbol)localEntry.sym;
/* 4428 */     if ((localVarSymbol.flags() & 0x18) != 24L)
/*      */     {
/* 4430 */       this.log.warning(Lint.LintCategory.SERIAL, 
/* 4431 */         TreeInfo.diagnosticPositionFor(localVarSymbol, paramJCClassDecl), 
/* 4431 */         "improper.SVUID", new Object[] { paramClassSymbol });
/*      */     }
/* 4434 */     else if (!localVarSymbol.type.hasTag(TypeTag.LONG)) {
/* 4435 */       this.log.warning(Lint.LintCategory.SERIAL, 
/* 4436 */         TreeInfo.diagnosticPositionFor(localVarSymbol, paramJCClassDecl), 
/* 4436 */         "long.SVUID", new Object[] { paramClassSymbol });
/*      */     }
/* 4439 */     else if (localVarSymbol.getConstValue() == null)
/* 4440 */       this.log.warning(Lint.LintCategory.SERIAL, 
/* 4441 */         TreeInfo.diagnosticPositionFor(localVarSymbol, paramJCClassDecl), 
/* 4441 */         "constant.SVUID", new Object[] { paramClassSymbol });
/*      */   }
/*      */ 
/*      */   private Type capture(Type paramType) {
/* 4445 */     return this.types.capture(paramType);
/*      */   }
/*      */ 
/*      */   public void validateTypeAnnotations(JCTree paramJCTree, boolean paramBoolean) {
/* 4449 */     paramJCTree.accept(new TypeAnnotationsValidator(paramBoolean));
/*      */   }
/*      */ 
/*      */   public void postAttr(JCTree paramJCTree)
/*      */   {
/* 4685 */     new PostAttrAnalyzer().scan(paramJCTree);
/*      */   }
/*      */ 
/*      */   private static class BreakAttr extends RuntimeException
/*      */   {
/*      */     static final long serialVersionUID = -6924771130405446405L;
/*      */     private Env<AttrContext> env;
/*      */ 
/*      */     private BreakAttr(Env<AttrContext> paramEnv)
/*      */     {
/*  456 */       this.env = paramEnv;
/*      */     }
/*      */   }
/*      */ 
/*      */   class ExpressionLambdaReturnContext extends Attr.FunctionalReturnContext
/*      */   {
/*      */     JCTree.JCExpression expr;
/*      */ 
/*      */     ExpressionLambdaReturnContext(JCTree.JCExpression paramCheckContext, Check.CheckContext arg3)
/*      */     {
/* 2551 */       super(localCheckContext);
/* 2552 */       this.expr = paramCheckContext;
/*      */     }
/*      */ 
/*      */     public boolean compatible(Type paramType1, Type paramType2, Warner paramWarner)
/*      */     {
/* 2559 */       return ((TreeInfo.isExpressionStatement(this.expr)) && (paramType2.hasTag(TypeTag.VOID))) || 
/* 2559 */         (super
/* 2559 */         .compatible(paramType1, paramType2, paramWarner));
/*      */     }
/*      */   }
/*      */ 
/*      */   class FunctionalReturnContext extends Check.NestedCheckContext
/*      */   {
/*      */     FunctionalReturnContext(Check.CheckContext arg2)
/*      */     {
/* 2531 */       super();
/*      */     }
/*      */ 
/*      */     public boolean compatible(Type paramType1, Type paramType2, Warner paramWarner)
/*      */     {
/* 2537 */       return Attr.this.chk.basicHandler.compatible(paramType1, inferenceContext().asUndetVar(paramType2), paramWarner);
/*      */     }
/*      */ 
/*      */     public void report(JCDiagnostic.DiagnosticPosition paramDiagnosticPosition, JCDiagnostic paramJCDiagnostic)
/*      */     {
/* 2542 */       this.enclosingContext.report(paramDiagnosticPosition, Attr.this.diags.fragment("incompatible.ret.type.in.lambda", new Object[] { paramJCDiagnostic }));
/*      */     }
/*      */   }
/*      */ 
/*      */   private class IdentAttributer extends SimpleTreeVisitor<Symbol, Env<AttrContext>>
/*      */   {
/*      */     private IdentAttributer()
/*      */     {
/*      */     }
/*      */ 
/*      */     public Symbol visitMemberSelect(MemberSelectTree paramMemberSelectTree, Env<AttrContext> paramEnv)
/*      */     {
/*  372 */       Symbol localSymbol = (Symbol)visit(paramMemberSelectTree.getExpression(), paramEnv);
/*  373 */       if ((localSymbol.kind == 63) || (localSymbol.kind == 137))
/*  374 */         return localSymbol;
/*  375 */       Name localName = (Name)paramMemberSelectTree.getIdentifier();
/*  376 */       if (localSymbol.kind == 1) {
/*  377 */         paramEnv.toplevel.packge = ((Symbol.PackageSymbol)localSymbol);
/*  378 */         return Attr.this.rs.findIdentInPackage(paramEnv, (Symbol.TypeSymbol)localSymbol, localName, 3);
/*      */       }
/*  380 */       paramEnv.enclClass.sym = ((Symbol.ClassSymbol)localSymbol);
/*  381 */       return Attr.this.rs.findMemberType(paramEnv, localSymbol.asType(), localName, (Symbol.TypeSymbol)localSymbol);
/*      */     }
/*      */ 
/*      */     public Symbol visitIdentifier(IdentifierTree paramIdentifierTree, Env<AttrContext> paramEnv)
/*      */     {
/*  387 */       return Attr.this.rs.findIdent(paramEnv, (Name)paramIdentifierTree.getName(), 3);
/*      */     }
/*      */   }
/*      */ 
/*      */   class PostAttrAnalyzer extends TreeScanner
/*      */   {
/*      */     PostAttrAnalyzer()
/*      */     {
/*      */     }
/*      */ 
/*      */     private void initTypeIfNeeded(JCTree paramJCTree)
/*      */     {
/* 4691 */       if (paramJCTree.type == null)
/* 4692 */         if (paramJCTree.hasTag(JCTree.Tag.METHODDEF))
/* 4693 */           paramJCTree.type = dummyMethodType((JCTree.JCMethodDecl)paramJCTree);
/*      */         else
/* 4695 */           paramJCTree.type = Attr.this.syms.unknownType;
/*      */     }
/*      */ 
/*      */     private Type dummyMethodType(JCTree.JCMethodDecl paramJCMethodDecl)
/*      */     {
/* 4706 */       Object localObject = Attr.this.syms.unknownType;
/* 4707 */       if ((paramJCMethodDecl != null) && (paramJCMethodDecl.restype.hasTag(JCTree.Tag.TYPEIDENT))) {
/* 4708 */         JCTree.JCPrimitiveTypeTree localJCPrimitiveTypeTree = (JCTree.JCPrimitiveTypeTree)paramJCMethodDecl.restype;
/* 4709 */         if (localJCPrimitiveTypeTree.typetag == TypeTag.VOID) {
/* 4710 */           localObject = Attr.this.syms.voidType;
/*      */         }
/*      */       }
/* 4713 */       return new Type.MethodType(List.nil(), (Type)localObject, 
/* 4713 */         List.nil(), Attr.this.syms.methodClass);
/*      */     }
/*      */     private Type dummyMethodType() {
/* 4716 */       return dummyMethodType(null);
/*      */     }
/*      */ 
/*      */     public void scan(JCTree paramJCTree)
/*      */     {
/* 4721 */       if (paramJCTree == null) return;
/* 4722 */       if ((paramJCTree instanceof JCTree.JCExpression)) {
/* 4723 */         initTypeIfNeeded(paramJCTree);
/*      */       }
/* 4725 */       super.scan(paramJCTree);
/*      */     }
/*      */ 
/*      */     public void visitIdent(JCTree.JCIdent paramJCIdent)
/*      */     {
/* 4730 */       if (paramJCIdent.sym == null)
/* 4731 */         paramJCIdent.sym = Attr.this.syms.unknownSymbol;
/*      */     }
/*      */ 
/*      */     public void visitSelect(JCTree.JCFieldAccess paramJCFieldAccess)
/*      */     {
/* 4737 */       if (paramJCFieldAccess.sym == null) {
/* 4738 */         paramJCFieldAccess.sym = Attr.this.syms.unknownSymbol;
/*      */       }
/* 4740 */       super.visitSelect(paramJCFieldAccess);
/*      */     }
/*      */ 
/*      */     public void visitClassDef(JCTree.JCClassDecl paramJCClassDecl)
/*      */     {
/* 4745 */       initTypeIfNeeded(paramJCClassDecl);
/* 4746 */       if (paramJCClassDecl.sym == null) {
/* 4747 */         paramJCClassDecl.sym = new Symbol.ClassSymbol(0L, paramJCClassDecl.name, paramJCClassDecl.type, Attr.this.syms.noSymbol);
/*      */       }
/* 4749 */       super.visitClassDef(paramJCClassDecl);
/*      */     }
/*      */ 
/*      */     public void visitMethodDef(JCTree.JCMethodDecl paramJCMethodDecl)
/*      */     {
/* 4754 */       initTypeIfNeeded(paramJCMethodDecl);
/* 4755 */       if (paramJCMethodDecl.sym == null) {
/* 4756 */         paramJCMethodDecl.sym = new Symbol.MethodSymbol(0L, paramJCMethodDecl.name, paramJCMethodDecl.type, Attr.this.syms.noSymbol);
/*      */       }
/* 4758 */       super.visitMethodDef(paramJCMethodDecl);
/*      */     }
/*      */ 
/*      */     public void visitVarDef(JCTree.JCVariableDecl paramJCVariableDecl)
/*      */     {
/* 4763 */       initTypeIfNeeded(paramJCVariableDecl);
/* 4764 */       if (paramJCVariableDecl.sym == null) {
/* 4765 */         paramJCVariableDecl.sym = new Symbol.VarSymbol(0L, paramJCVariableDecl.name, paramJCVariableDecl.type, Attr.this.syms.noSymbol);
/* 4766 */         paramJCVariableDecl.sym.adr = 0;
/*      */       }
/* 4768 */       super.visitVarDef(paramJCVariableDecl);
/*      */     }
/*      */ 
/*      */     public void visitNewClass(JCTree.JCNewClass paramJCNewClass)
/*      */     {
/* 4773 */       if (paramJCNewClass.constructor == null) {
/* 4774 */         paramJCNewClass.constructor = new Symbol.MethodSymbol(0L, Attr.this.names.init, 
/* 4775 */           dummyMethodType(), Attr.this.syms.noSymbol);
/*      */       }
/* 4777 */       if (paramJCNewClass.constructorType == null) {
/* 4778 */         paramJCNewClass.constructorType = Attr.this.syms.unknownType;
/*      */       }
/* 4780 */       super.visitNewClass(paramJCNewClass);
/*      */     }
/*      */ 
/*      */     public void visitAssignop(JCTree.JCAssignOp paramJCAssignOp)
/*      */     {
/* 4785 */       if (paramJCAssignOp.operator == null) {
/* 4786 */         paramJCAssignOp.operator = new Symbol.OperatorSymbol(Attr.this.names.empty, dummyMethodType(), -1, Attr.this.syms.noSymbol);
/*      */       }
/*      */ 
/* 4789 */       super.visitAssignop(paramJCAssignOp);
/*      */     }
/*      */ 
/*      */     public void visitBinary(JCTree.JCBinary paramJCBinary)
/*      */     {
/* 4794 */       if (paramJCBinary.operator == null) {
/* 4795 */         paramJCBinary.operator = new Symbol.OperatorSymbol(Attr.this.names.empty, dummyMethodType(), -1, Attr.this.syms.noSymbol);
/*      */       }
/*      */ 
/* 4798 */       super.visitBinary(paramJCBinary);
/*      */     }
/*      */ 
/*      */     public void visitUnary(JCTree.JCUnary paramJCUnary)
/*      */     {
/* 4803 */       if (paramJCUnary.operator == null) {
/* 4804 */         paramJCUnary.operator = new Symbol.OperatorSymbol(Attr.this.names.empty, dummyMethodType(), -1, Attr.this.syms.noSymbol);
/*      */       }
/*      */ 
/* 4807 */       super.visitUnary(paramJCUnary);
/*      */     }
/*      */ 
/*      */     public void visitLambda(JCTree.JCLambda paramJCLambda)
/*      */     {
/* 4812 */       super.visitLambda(paramJCLambda);
/* 4813 */       if (paramJCLambda.targets == null)
/* 4814 */         paramJCLambda.targets = List.nil();
/*      */     }
/*      */ 
/*      */     public void visitReference(JCTree.JCMemberReference paramJCMemberReference)
/*      */     {
/* 4820 */       super.visitReference(paramJCMemberReference);
/* 4821 */       if (paramJCMemberReference.sym == null) {
/* 4822 */         paramJCMemberReference.sym = new Symbol.MethodSymbol(0L, Attr.this.names.empty, dummyMethodType(), Attr.this.syms.noSymbol);
/*      */       }
/*      */ 
/* 4825 */       if (paramJCMemberReference.targets == null)
/* 4826 */         paramJCMemberReference.targets = List.nil();
/*      */     }
/*      */   }
/*      */ 
/*      */   class RecoveryInfo extends Attr.ResultInfo
/*      */   {
/*      */     public RecoveryInfo(DeferredAttr.DeferredAttrContext arg2)
/*      */     {
/*  504 */       super(12, Type.recoveryType, new Check.NestedCheckContext(Attr.this)
/*      */       {
/*      */         public DeferredAttr.DeferredAttrContext deferredAttrContext() {
/*  507 */           return localDeferredAttrContext;
/*      */         }
/*      */ 
/*      */         public boolean compatible(Type paramAnonymousType1, Type paramAnonymousType2, Warner paramAnonymousWarner) {
/*  511 */           return true;
/*      */         }
/*      */ 
/*      */         public void report(JCDiagnostic.DiagnosticPosition paramAnonymousDiagnosticPosition, JCDiagnostic paramAnonymousJCDiagnostic) {
/*  515 */           this.val$this$0.chk.basicHandler.report(paramAnonymousDiagnosticPosition, paramAnonymousJCDiagnostic);
/*      */         }
/*      */       });
/*      */     }
/*      */   }
/*      */ 
/*      */   class ResultInfo
/*      */   {
/*      */     final int pkind;
/*      */     final Type pt;
/*      */     final Check.CheckContext checkContext;
/*      */ 
/*      */     ResultInfo(int paramType, Type arg3)
/*      */     {
/*  466 */       this(paramType, localType, Attr.this.chk.basicHandler);
/*      */     }
/*      */ 
/*      */     protected ResultInfo(int paramType, Type paramCheckContext, Check.CheckContext arg4) {
/*  470 */       this.pkind = paramType;
/*  471 */       this.pt = paramCheckContext;
/*      */       Object localObject;
/*  472 */       this.checkContext = localObject;
/*      */     }
/*      */ 
/*      */     protected Type check(JCDiagnostic.DiagnosticPosition paramDiagnosticPosition, Type paramType) {
/*  476 */       return Attr.this.chk.checkType(paramDiagnosticPosition, paramType, this.pt, this.checkContext);
/*      */     }
/*      */ 
/*      */     protected ResultInfo dup(Type paramType) {
/*  480 */       return new ResultInfo(Attr.this, this.pkind, paramType, this.checkContext);
/*      */     }
/*      */ 
/*      */     protected ResultInfo dup(Check.CheckContext paramCheckContext) {
/*  484 */       return new ResultInfo(Attr.this, this.pkind, this.pt, paramCheckContext);
/*      */     }
/*      */ 
/*      */     protected ResultInfo dup(Type paramType, Check.CheckContext paramCheckContext) {
/*  488 */       return new ResultInfo(Attr.this, this.pkind, paramType, paramCheckContext);
/*      */     }
/*      */ 
/*      */     public String toString()
/*      */     {
/*  493 */       if (this.pt != null) {
/*  494 */         return this.pt.toString();
/*      */       }
/*  496 */       return "";
/*      */     }
/*      */   }
/*      */ 
/*      */   private final class TypeAnnotationsValidator extends TreeScanner
/*      */   {
/*      */     private final boolean sigOnly;
/*      */ 
/*      */     public TypeAnnotationsValidator(boolean arg2)
/*      */     {
/*      */       boolean bool;
/* 4456 */       this.sigOnly = bool;
/*      */     }
/*      */ 
/*      */     public void visitAnnotation(JCTree.JCAnnotation paramJCAnnotation) {
/* 4460 */       Attr.this.chk.validateTypeAnnotation(paramJCAnnotation, false);
/* 4461 */       super.visitAnnotation(paramJCAnnotation);
/*      */     }
/*      */     public void visitAnnotatedType(JCTree.JCAnnotatedType paramJCAnnotatedType) {
/* 4464 */       if (!paramJCAnnotatedType.underlyingType.type.isErroneous())
/* 4465 */         super.visitAnnotatedType(paramJCAnnotatedType);
/*      */     }
/*      */ 
/*      */     public void visitTypeParameter(JCTree.JCTypeParameter paramJCTypeParameter) {
/* 4469 */       Attr.this.chk.validateTypeAnnotations(paramJCTypeParameter.annotations, true);
/* 4470 */       scan(paramJCTypeParameter.bounds);
/*      */     }
/*      */ 
/*      */     public void visitMethodDef(JCTree.JCMethodDecl paramJCMethodDecl)
/*      */     {
/* 4477 */       if ((paramJCMethodDecl.recvparam != null) && 
/* 4478 */         (!paramJCMethodDecl.recvparam.vartype.type
/* 4478 */         .isErroneous())) {
/* 4479 */         checkForDeclarationAnnotations(paramJCMethodDecl.recvparam.mods.annotations, paramJCMethodDecl.recvparam.vartype.type.tsym);
/*      */       }
/*      */ 
/* 4482 */       if ((paramJCMethodDecl.restype != null) && (paramJCMethodDecl.restype.type != null)) {
/* 4483 */         validateAnnotatedType(paramJCMethodDecl.restype, paramJCMethodDecl.restype.type);
/*      */       }
/* 4485 */       if (this.sigOnly) {
/* 4486 */         scan(paramJCMethodDecl.mods);
/* 4487 */         scan(paramJCMethodDecl.restype);
/* 4488 */         scan(paramJCMethodDecl.typarams);
/* 4489 */         scan(paramJCMethodDecl.recvparam);
/* 4490 */         scan(paramJCMethodDecl.params);
/* 4491 */         scan(paramJCMethodDecl.thrown);
/*      */       } else {
/* 4493 */         scan(paramJCMethodDecl.defaultValue);
/* 4494 */         scan(paramJCMethodDecl.body);
/*      */       }
/*      */     }
/*      */ 
/* 4498 */     public void visitVarDef(JCTree.JCVariableDecl paramJCVariableDecl) { if ((paramJCVariableDecl.sym != null) && (paramJCVariableDecl.sym.type != null))
/* 4499 */         validateAnnotatedType(paramJCVariableDecl.vartype, paramJCVariableDecl.sym.type);
/* 4500 */       scan(paramJCVariableDecl.mods);
/* 4501 */       scan(paramJCVariableDecl.vartype);
/* 4502 */       if (!this.sigOnly)
/* 4503 */         scan(paramJCVariableDecl.init); }
/*      */ 
/*      */     public void visitTypeCast(JCTree.JCTypeCast paramJCTypeCast)
/*      */     {
/* 4507 */       if ((paramJCTypeCast.clazz != null) && (paramJCTypeCast.clazz.type != null))
/* 4508 */         validateAnnotatedType(paramJCTypeCast.clazz, paramJCTypeCast.clazz.type);
/* 4509 */       super.visitTypeCast(paramJCTypeCast);
/*      */     }
/*      */     public void visitTypeTest(JCTree.JCInstanceOf paramJCInstanceOf) {
/* 4512 */       if ((paramJCInstanceOf.clazz != null) && (paramJCInstanceOf.clazz.type != null))
/* 4513 */         validateAnnotatedType(paramJCInstanceOf.clazz, paramJCInstanceOf.clazz.type);
/* 4514 */       super.visitTypeTest(paramJCInstanceOf);
/*      */     }
/*      */     public void visitNewClass(JCTree.JCNewClass paramJCNewClass) {
/* 4517 */       if ((paramJCNewClass.clazz != null) && (paramJCNewClass.clazz.type != null)) {
/* 4518 */         if (paramJCNewClass.clazz.hasTag(JCTree.Tag.ANNOTATED_TYPE)) {
/* 4519 */           checkForDeclarationAnnotations(((JCTree.JCAnnotatedType)paramJCNewClass.clazz).annotations, paramJCNewClass.clazz.type.tsym);
/*      */         }
/*      */ 
/* 4522 */         if (paramJCNewClass.def != null) {
/* 4523 */           checkForDeclarationAnnotations(paramJCNewClass.def.mods.annotations, paramJCNewClass.clazz.type.tsym);
/*      */         }
/*      */ 
/* 4526 */         validateAnnotatedType(paramJCNewClass.clazz, paramJCNewClass.clazz.type);
/*      */       }
/* 4528 */       super.visitNewClass(paramJCNewClass);
/*      */     }
/*      */     public void visitNewArray(JCTree.JCNewArray paramJCNewArray) {
/* 4531 */       if ((paramJCNewArray.elemtype != null) && (paramJCNewArray.elemtype.type != null)) {
/* 4532 */         if (paramJCNewArray.elemtype.hasTag(JCTree.Tag.ANNOTATED_TYPE)) {
/* 4533 */           checkForDeclarationAnnotations(((JCTree.JCAnnotatedType)paramJCNewArray.elemtype).annotations, paramJCNewArray.elemtype.type.tsym);
/*      */         }
/*      */ 
/* 4536 */         validateAnnotatedType(paramJCNewArray.elemtype, paramJCNewArray.elemtype.type);
/*      */       }
/* 4538 */       super.visitNewArray(paramJCNewArray);
/*      */     }
/*      */     public void visitClassDef(JCTree.JCClassDecl paramJCClassDecl) {
/* 4541 */       if (this.sigOnly) {
/* 4542 */         scan(paramJCClassDecl.mods);
/* 4543 */         scan(paramJCClassDecl.typarams);
/* 4544 */         scan(paramJCClassDecl.extending);
/* 4545 */         scan(paramJCClassDecl.implementing);
/*      */       }
/* 4547 */       for (JCTree localJCTree : paramJCClassDecl.defs)
/* 4548 */         if (!localJCTree.hasTag(JCTree.Tag.CLASSDEF))
/*      */         {
/* 4551 */           scan(localJCTree);
/*      */         }
/*      */     }
/*      */ 
/* 4555 */     public void visitBlock(JCTree.JCBlock paramJCBlock) { if (!this.sigOnly)
/* 4556 */         scan(paramJCBlock.stats);
/*      */     }
/*      */ 
/*      */     private void validateAnnotatedType(JCTree paramJCTree, Type paramType)
/*      */     {
/* 4571 */       if (paramType.isPrimitiveOrVoid()) {
/* 4572 */         return;
/*      */       }
/*      */ 
/* 4575 */       Object localObject1 = paramJCTree;
/* 4576 */       Type localType = paramType;
/*      */ 
/* 4578 */       int i = 1;
/* 4579 */       while (i != 0)
/*      */       {
/*      */         Object localObject2;
/*      */         Object localObject3;
/* 4580 */         if (((JCTree)localObject1).hasTag(JCTree.Tag.TYPEAPPLY)) {
/* 4581 */           localObject2 = localType.getTypeArguments();
/* 4582 */           localObject3 = ((JCTree.JCTypeApply)localObject1).getTypeArguments();
/* 4583 */           if (((List)localObject3).length() > 0)
/*      */           {
/* 4585 */             if (((List)localObject2).length() == ((List)localObject3).length()) {
/* 4586 */               for (int j = 0; j < ((List)localObject2).length(); j++) {
/* 4587 */                 validateAnnotatedType((JCTree)((List)localObject3).get(j), (Type)((List)localObject2).get(j));
/*      */               }
/*      */ 
/*      */             }
/*      */ 
/*      */           }
/*      */ 
/* 4596 */           localObject1 = ((JCTree.JCTypeApply)localObject1).clazz;
/*      */         }
/*      */ 
/* 4599 */         if (((JCTree)localObject1).hasTag(JCTree.Tag.SELECT)) {
/* 4600 */           localObject1 = ((JCTree.JCFieldAccess)localObject1).getExpression();
/* 4601 */           if ((localType != null) && 
/* 4602 */             (!localType
/* 4602 */             .hasTag(TypeTag.NONE)))
/*      */           {
/* 4603 */             localType = localType.getEnclosingType();
/*      */           }
/*      */         }
/*      */         else
/*      */         {
/*      */           Object localObject4;
/* 4605 */           if (((JCTree)localObject1).hasTag(JCTree.Tag.ANNOTATED_TYPE)) {
/* 4606 */             localObject2 = (JCTree.JCAnnotatedType)localObject1;
/* 4607 */             if ((localType == null) || 
/* 4608 */               (localType
/* 4608 */               .hasTag(TypeTag.NONE)))
/*      */             {
/* 4609 */               if (((JCTree.JCAnnotatedType)localObject2).getAnnotations().size() == 1) {
/* 4610 */                 Attr.this.log.error(((JCTree.JCAnnotatedType)localObject2).underlyingType.pos(), "cant.type.annotate.scoping.1", new Object[] { ((JCTree.JCAnnotation)((JCTree.JCAnnotatedType)localObject2).getAnnotations().head).attribute });
/*      */               } else {
/* 4612 */                 localObject3 = new ListBuffer();
/* 4613 */                 for (localObject4 = ((JCTree.JCAnnotatedType)localObject2).getAnnotations().iterator(); ((Iterator)localObject4).hasNext(); ) { JCTree.JCAnnotation localJCAnnotation = (JCTree.JCAnnotation)((Iterator)localObject4).next();
/* 4614 */                   ((ListBuffer)localObject3).add(localJCAnnotation.attribute);
/*      */                 }
/* 4616 */                 Attr.this.log.error(((JCTree.JCAnnotatedType)localObject2).underlyingType.pos(), "cant.type.annotate.scoping", new Object[] { ((ListBuffer)localObject3).toList() });
/*      */               }
/* 4618 */               i = 0;
/*      */             }
/* 4620 */             localObject1 = ((JCTree.JCAnnotatedType)localObject2).underlyingType;
/*      */           }
/* 4622 */           else if (((JCTree)localObject1).hasTag(JCTree.Tag.IDENT)) {
/* 4623 */             i = 0;
/* 4624 */           } else if (((JCTree)localObject1).hasTag(JCTree.Tag.WILDCARD)) {
/* 4625 */             localObject2 = (JCTree.JCWildcard)localObject1;
/* 4626 */             if (((JCTree.JCWildcard)localObject2).getKind() == Tree.Kind.EXTENDS_WILDCARD)
/* 4627 */               validateAnnotatedType(((JCTree.JCWildcard)localObject2).getBound(), ((Type.WildcardType)localType.unannotatedType()).getExtendsBound());
/* 4628 */             else if (((JCTree.JCWildcard)localObject2).getKind() == Tree.Kind.SUPER_WILDCARD) {
/* 4629 */               validateAnnotatedType(((JCTree.JCWildcard)localObject2).getBound(), ((Type.WildcardType)localType.unannotatedType()).getSuperBound());
/*      */             }
/*      */ 
/* 4633 */             i = 0;
/* 4634 */           } else if (((JCTree)localObject1).hasTag(JCTree.Tag.TYPEARRAY)) {
/* 4635 */             localObject2 = (JCTree.JCArrayTypeTree)localObject1;
/* 4636 */             validateAnnotatedType(((JCTree.JCArrayTypeTree)localObject2).getType(), ((Type.ArrayType)localType.unannotatedType()).getComponentType());
/* 4637 */             i = 0;
/* 4638 */           } else if (((JCTree)localObject1).hasTag(JCTree.Tag.TYPEUNION)) {
/* 4639 */             localObject2 = (JCTree.JCTypeUnion)localObject1;
/* 4640 */             for (localObject3 = ((JCTree.JCTypeUnion)localObject2).getTypeAlternatives().iterator(); ((Iterator)localObject3).hasNext(); ) { localObject4 = (JCTree)((Iterator)localObject3).next();
/* 4641 */               validateAnnotatedType((JCTree)localObject4, ((JCTree)localObject4).type);
/*      */             }
/* 4643 */             i = 0;
/* 4644 */           } else if (((JCTree)localObject1).hasTag(JCTree.Tag.TYPEINTERSECTION)) {
/* 4645 */             localObject2 = (JCTree.JCTypeIntersection)localObject1;
/* 4646 */             for (localObject3 = ((JCTree.JCTypeIntersection)localObject2).getBounds().iterator(); ((Iterator)localObject3).hasNext(); ) { localObject4 = (JCTree)((Iterator)localObject3).next();
/* 4647 */               validateAnnotatedType((JCTree)localObject4, ((JCTree)localObject4).type);
/*      */             }
/* 4649 */             i = 0;
/* 4650 */           } else if ((((JCTree)localObject1).getKind() == Tree.Kind.PRIMITIVE_TYPE) || 
/* 4651 */             (((JCTree)localObject1)
/* 4651 */             .getKind() == Tree.Kind.ERRONEOUS)) {
/* 4652 */             i = 0;
/*      */           } else {
/* 4654 */             Assert.error("Unexpected tree: " + localObject1 + " with kind: " + ((JCTree)localObject1).getKind() + " within: " + paramJCTree + " with kind: " + paramJCTree
/* 4655 */               .getKind());
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/*      */ 
/*      */     private void checkForDeclarationAnnotations(List<? extends JCTree.JCAnnotation> paramList, Symbol paramSymbol)
/*      */     {
/* 4666 */       for (JCTree.JCAnnotation localJCAnnotation : paramList)
/* 4667 */         if ((!localJCAnnotation.type.isErroneous()) && 
/* 4668 */           (Attr.this.typeAnnotations
/* 4668 */           .annotationType(localJCAnnotation.attribute, paramSymbol) == 
/* 4668 */           TypeAnnotations.AnnotationType.DECLARATION))
/* 4669 */           Attr.this.log.error(localJCAnnotation.pos(), "annotation.type.not.applicable", new Object[0]);
/*      */     }
/*      */   }
/*      */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.javac.comp.Attr
 * JD-Core Version:    0.6.2
 */