/*      */ package com.sun.tools.javac.comp;
/*      */ 
/*      */ import com.sun.tools.javac.code.Attribute;
/*      */ import com.sun.tools.javac.code.Attribute.Array;
/*      */ import com.sun.tools.javac.code.Attribute.Class;
/*      */ import com.sun.tools.javac.code.Attribute.Compound;
/*      */ import com.sun.tools.javac.code.Attribute.Enum;
/*      */ import com.sun.tools.javac.code.Attribute.RetentionPolicy;
/*      */ import com.sun.tools.javac.code.DeferredLintHandler;
/*      */ import com.sun.tools.javac.code.DeferredLintHandler.LintLogger;
/*      */ import com.sun.tools.javac.code.Flags;
/*      */ import com.sun.tools.javac.code.Kinds;
/*      */ import com.sun.tools.javac.code.Lint;
/*      */ import com.sun.tools.javac.code.Lint.LintCategory;
/*      */ import com.sun.tools.javac.code.Scope;
/*      */ import com.sun.tools.javac.code.Scope.CompoundScope;
/*      */ import com.sun.tools.javac.code.Scope.Entry;
/*      */ import com.sun.tools.javac.code.Source;
/*      */ import com.sun.tools.javac.code.Symbol;
/*      */ import com.sun.tools.javac.code.Symbol.ClassSymbol;
/*      */ import com.sun.tools.javac.code.Symbol.CompletionFailure;
/*      */ import com.sun.tools.javac.code.Symbol.MethodSymbol;
/*      */ import com.sun.tools.javac.code.Symbol.OperatorSymbol;
/*      */ import com.sun.tools.javac.code.Symbol.TypeSymbol;
/*      */ import com.sun.tools.javac.code.Symbol.VarSymbol;
/*      */ import com.sun.tools.javac.code.Symtab;
/*      */ import com.sun.tools.javac.code.Type;
/*      */ import com.sun.tools.javac.code.Type.ArrayType;
/*      */ import com.sun.tools.javac.code.Type.CapturedType;
/*      */ import com.sun.tools.javac.code.Type.ClassType;
/*      */ import com.sun.tools.javac.code.Type.ForAll;
/*      */ import com.sun.tools.javac.code.Type.TypeVar;
/*      */ import com.sun.tools.javac.code.Type.WildcardType;
/*      */ import com.sun.tools.javac.code.TypeTag;
/*      */ import com.sun.tools.javac.code.Types;
/*      */ import com.sun.tools.javac.code.Types.FunctionDescriptorLookupError;
/*      */ import com.sun.tools.javac.code.Types.UnaryVisitor;
/*      */ import com.sun.tools.javac.jvm.ClassReader.BadClassFile;
/*      */ import com.sun.tools.javac.jvm.Profile;
/*      */ import com.sun.tools.javac.jvm.Target;
/*      */ import com.sun.tools.javac.tree.JCTree;
/*      */ import com.sun.tools.javac.tree.JCTree.JCAnnotatedType;
/*      */ import com.sun.tools.javac.tree.JCTree.JCAnnotation;
/*      */ import com.sun.tools.javac.tree.JCTree.JCArrayTypeTree;
/*      */ import com.sun.tools.javac.tree.JCTree.JCAssign;
/*      */ import com.sun.tools.javac.tree.JCTree.JCClassDecl;
/*      */ import com.sun.tools.javac.tree.JCTree.JCCompilationUnit;
/*      */ import com.sun.tools.javac.tree.JCTree.JCExpression;
/*      */ import com.sun.tools.javac.tree.JCTree.JCFieldAccess;
/*      */ import com.sun.tools.javac.tree.JCTree.JCIdent;
/*      */ import com.sun.tools.javac.tree.JCTree.JCIf;
/*      */ import com.sun.tools.javac.tree.JCTree.JCMethodDecl;
/*      */ import com.sun.tools.javac.tree.JCTree.JCMethodInvocation;
/*      */ import com.sun.tools.javac.tree.JCTree.JCModifiers;
/*      */ import com.sun.tools.javac.tree.JCTree.JCNewArray;
/*      */ import com.sun.tools.javac.tree.JCTree.JCNewClass;
/*      */ import com.sun.tools.javac.tree.JCTree.JCPolyExpression.PolyKind;
/*      */ import com.sun.tools.javac.tree.JCTree.JCPrimitiveTypeTree;
/*      */ import com.sun.tools.javac.tree.JCTree.JCStatement;
/*      */ import com.sun.tools.javac.tree.JCTree.JCTypeApply;
/*      */ import com.sun.tools.javac.tree.JCTree.JCTypeCast;
/*      */ import com.sun.tools.javac.tree.JCTree.JCTypeParameter;
/*      */ import com.sun.tools.javac.tree.JCTree.JCVariableDecl;
/*      */ import com.sun.tools.javac.tree.JCTree.JCWildcard;
/*      */ import com.sun.tools.javac.tree.JCTree.Tag;
/*      */ import com.sun.tools.javac.tree.JCTree.Visitor;
/*      */ import com.sun.tools.javac.tree.TreeInfo;
/*      */ import com.sun.tools.javac.tree.TreeScanner;
/*      */ import com.sun.tools.javac.util.Abort;
/*      */ import com.sun.tools.javac.util.Assert;
/*      */ import com.sun.tools.javac.util.Context;
/*      */ import com.sun.tools.javac.util.Context.Key;
/*      */ import com.sun.tools.javac.util.DiagnosticSource;
/*      */ import com.sun.tools.javac.util.Filter;
/*      */ import com.sun.tools.javac.util.JCDiagnostic;
/*      */ import com.sun.tools.javac.util.JCDiagnostic.DiagnosticFlag;
/*      */ import com.sun.tools.javac.util.JCDiagnostic.DiagnosticPosition;
/*      */ import com.sun.tools.javac.util.JCDiagnostic.Factory;
/*      */ import com.sun.tools.javac.util.List;
/*      */ import com.sun.tools.javac.util.ListBuffer;
/*      */ import com.sun.tools.javac.util.Log;
/*      */ import com.sun.tools.javac.util.Log.DiscardDiagnosticHandler;
/*      */ import com.sun.tools.javac.util.MandatoryWarningHandler;
/*      */ import com.sun.tools.javac.util.Name;
/*      */ import com.sun.tools.javac.util.Names;
/*      */ import com.sun.tools.javac.util.Options;
/*      */ import com.sun.tools.javac.util.Pair;
/*      */ import com.sun.tools.javac.util.Warner;
/*      */ import java.util.Collection;
/*      */ import java.util.Collections;
/*      */ import java.util.HashMap;
/*      */ import java.util.HashSet;
/*      */ import java.util.Iterator;
/*      */ import java.util.LinkedHashSet;
/*      */ import java.util.Map;
/*      */ import java.util.Set;
/*      */ import javax.tools.JavaFileManager;
/*      */ 
/*      */ public class Check
/*      */ {
/*   67 */   protected static final Context.Key<Check> checkKey = new Context.Key();
/*      */   private final Names names;
/*      */   private final Log log;
/*      */   private final Resolve rs;
/*      */   private final Symtab syms;
/*      */   private final Enter enter;
/*      */   private final DeferredAttr deferredAttr;
/*      */   private final Infer infer;
/*      */   private final Types types;
/*      */   private final JCDiagnostic.Factory diags;
/*      */   private boolean warnOnSyntheticConflicts;
/*      */   private boolean suppressAbortOnBadClassFile;
/*      */   private boolean enableSunApiLintControl;
/*      */   private final TreeInfo treeinfo;
/*      */   private final JavaFileManager fileManager;
/*      */   private final Profile profile;
/*      */   private final boolean warnOnAccessToSensitiveMembers;
/*      */   private Lint lint;
/*      */   private Symbol.MethodSymbol method;
/*      */   boolean allowGenerics;
/*      */   boolean allowVarargs;
/*      */   boolean allowAnnotations;
/*      */   boolean allowCovariantReturns;
/*      */   boolean allowSimplifiedVarargs;
/*      */   boolean allowDefaultMethods;
/*      */   boolean allowStrictMethodClashCheck;
/*      */   boolean complexInference;
/*      */   char syntheticNameChar;
/*  196 */   public Map<Name, Symbol.ClassSymbol> compiled = new HashMap();
/*      */   private MandatoryWarningHandler deprecationHandler;
/*      */   private MandatoryWarningHandler uncheckedHandler;
/*      */   private MandatoryWarningHandler sunApiHandler;
/*      */   private DeferredLintHandler deferredLintHandler;
/*  496 */   CheckContext basicHandler = new CheckContext() {
/*      */     public void report(JCDiagnostic.DiagnosticPosition paramAnonymousDiagnosticPosition, JCDiagnostic paramAnonymousJCDiagnostic) {
/*  498 */       Check.this.log.error(paramAnonymousDiagnosticPosition, "prob.found.req", new Object[] { paramAnonymousJCDiagnostic });
/*      */     }
/*      */     public boolean compatible(Type paramAnonymousType1, Type paramAnonymousType2, Warner paramAnonymousWarner) {
/*  501 */       return Check.this.types.isAssignable(paramAnonymousType1, paramAnonymousType2, paramAnonymousWarner);
/*      */     }
/*      */ 
/*      */     public Warner checkWarner(JCDiagnostic.DiagnosticPosition paramAnonymousDiagnosticPosition, Type paramAnonymousType1, Type paramAnonymousType2) {
/*  505 */       return Check.this.convertWarner(paramAnonymousDiagnosticPosition, paramAnonymousType1, paramAnonymousType2);
/*      */     }
/*      */ 
/*      */     public Infer.InferenceContext inferenceContext() {
/*  509 */       return Check.this.infer.emptyContext;
/*      */     }
/*      */ 
/*      */     public DeferredAttr.DeferredAttrContext deferredAttrContext() {
/*  513 */       return Check.this.deferredAttr.emptyDeferredAttrContext;
/*      */     }
/*      */ 
/*      */     public String toString()
/*      */     {
/*  518 */       return "CheckContext: basicHandler";
/*      */     }
/*  496 */   };
/*      */   private static final boolean ignoreAnnotatedCasts = true;
/* 1007 */   Types.UnaryVisitor<Boolean> isTypeArgErroneous = new Types.UnaryVisitor() {
/*      */     public Boolean visitType(Type paramAnonymousType, Void paramAnonymousVoid) {
/* 1009 */       return Boolean.valueOf(paramAnonymousType.isErroneous());
/*      */     }
/*      */ 
/*      */     public Boolean visitTypeVar(Type.TypeVar paramAnonymousTypeVar, Void paramAnonymousVoid) {
/* 1013 */       return (Boolean)visit(paramAnonymousTypeVar.getUpperBound());
/*      */     }
/*      */ 
/*      */     public Boolean visitCapturedType(Type.CapturedType paramAnonymousCapturedType, Void paramAnonymousVoid) {
/* 1017 */       return Boolean.valueOf((((Boolean)visit(paramAnonymousCapturedType.getUpperBound())).booleanValue()) || 
/* 1018 */         (((Boolean)visit(paramAnonymousCapturedType
/* 1018 */         .getLowerBound())).booleanValue()));
/*      */     }
/*      */ 
/*      */     public Boolean visitWildcardType(Type.WildcardType paramAnonymousWildcardType, Void paramAnonymousVoid) {
/* 1022 */       return (Boolean)visit(paramAnonymousWildcardType.type);
/*      */     }
/* 1007 */   };
/*      */ 
/* 1751 */   Warner overrideWarner = new Warner();
/*      */ 
/* 1993 */   private Filter<Symbol> equalsHasCodeFilter = new Filter()
/*      */   {
/*      */     public boolean accepts(Symbol paramAnonymousSymbol) {
/* 1996 */       return (Symbol.MethodSymbol.implementation_filter.accepts(paramAnonymousSymbol)) && 
/* 1996 */         ((paramAnonymousSymbol
/* 1996 */         .flags() & 0x0) == 0L);
/*      */     }
/* 1993 */   };
/*      */   private Set<Name> defaultTargets;
/*      */   private final Name[] dfltTargetMeta;
/*      */ 
/*      */   public static Check instance(Context paramContext)
/*      */   {
/*   97 */     Check localCheck = (Check)paramContext.get(checkKey);
/*   98 */     if (localCheck == null)
/*   99 */       localCheck = new Check(paramContext);
/*  100 */     return localCheck;
/*      */   }
/*      */ 
/*      */   protected Check(Context paramContext) {
/*  104 */     paramContext.put(checkKey, this);
/*      */ 
/*  106 */     this.names = Names.instance(paramContext);
/*  107 */     this.dfltTargetMeta = new Name[] { this.names.PACKAGE, this.names.TYPE, this.names.FIELD, this.names.METHOD, this.names.CONSTRUCTOR, this.names.ANNOTATION_TYPE, this.names.LOCAL_VARIABLE, this.names.PARAMETER };
/*      */ 
/*  110 */     this.log = Log.instance(paramContext);
/*  111 */     this.rs = Resolve.instance(paramContext);
/*  112 */     this.syms = Symtab.instance(paramContext);
/*  113 */     this.enter = Enter.instance(paramContext);
/*  114 */     this.deferredAttr = DeferredAttr.instance(paramContext);
/*  115 */     this.infer = Infer.instance(paramContext);
/*  116 */     this.types = Types.instance(paramContext);
/*  117 */     this.diags = JCDiagnostic.Factory.instance(paramContext);
/*  118 */     Options localOptions = Options.instance(paramContext);
/*  119 */     this.lint = Lint.instance(paramContext);
/*  120 */     this.treeinfo = TreeInfo.instance(paramContext);
/*  121 */     this.fileManager = ((JavaFileManager)paramContext.get(JavaFileManager.class));
/*      */ 
/*  123 */     Source localSource = Source.instance(paramContext);
/*  124 */     this.allowGenerics = localSource.allowGenerics();
/*  125 */     this.allowVarargs = localSource.allowVarargs();
/*  126 */     this.allowAnnotations = localSource.allowAnnotations();
/*  127 */     this.allowCovariantReturns = localSource.allowCovariantReturns();
/*  128 */     this.allowSimplifiedVarargs = localSource.allowSimplifiedVarargs();
/*  129 */     this.allowDefaultMethods = localSource.allowDefaultMethods();
/*  130 */     this.allowStrictMethodClashCheck = localSource.allowStrictMethodClashCheck();
/*  131 */     this.complexInference = localOptions.isSet("complexinference");
/*  132 */     this.warnOnSyntheticConflicts = localOptions.isSet("warnOnSyntheticConflicts");
/*  133 */     this.suppressAbortOnBadClassFile = localOptions.isSet("suppressAbortOnBadClassFile");
/*  134 */     this.enableSunApiLintControl = localOptions.isSet("enableSunApiLintControl");
/*  135 */     this.warnOnAccessToSensitiveMembers = localOptions.isSet("warnOnAccessToSensitiveMembers");
/*      */ 
/*  137 */     Target localTarget = Target.instance(paramContext);
/*  138 */     this.syntheticNameChar = localTarget.syntheticNameChar();
/*      */ 
/*  140 */     this.profile = Profile.instance(paramContext);
/*      */ 
/*  142 */     boolean bool1 = this.lint.isEnabled(Lint.LintCategory.DEPRECATION);
/*  143 */     boolean bool2 = this.lint.isEnabled(Lint.LintCategory.UNCHECKED);
/*  144 */     boolean bool3 = this.lint.isEnabled(Lint.LintCategory.SUNAPI);
/*  145 */     boolean bool4 = localSource.enforceMandatoryWarnings();
/*      */ 
/*  147 */     this.deprecationHandler = new MandatoryWarningHandler(this.log, bool1, bool4, "deprecated", Lint.LintCategory.DEPRECATION);
/*      */ 
/*  149 */     this.uncheckedHandler = new MandatoryWarningHandler(this.log, bool2, bool4, "unchecked", Lint.LintCategory.UNCHECKED);
/*      */ 
/*  151 */     this.sunApiHandler = new MandatoryWarningHandler(this.log, bool3, bool4, "sunapi", null);
/*      */ 
/*  154 */     this.deferredLintHandler = DeferredLintHandler.instance(paramContext);
/*      */   }
/*      */ 
/*      */   Lint setLint(Lint paramLint)
/*      */   {
/*  219 */     Lint localLint = this.lint;
/*  220 */     this.lint = paramLint;
/*  221 */     return localLint;
/*      */   }
/*      */ 
/*      */   Symbol.MethodSymbol setMethod(Symbol.MethodSymbol paramMethodSymbol) {
/*  225 */     Symbol.MethodSymbol localMethodSymbol = this.method;
/*  226 */     this.method = paramMethodSymbol;
/*  227 */     return localMethodSymbol;
/*      */   }
/*      */ 
/*      */   void warnDeprecated(JCDiagnostic.DiagnosticPosition paramDiagnosticPosition, Symbol paramSymbol)
/*      */   {
/*  235 */     if (!this.lint.isSuppressed(Lint.LintCategory.DEPRECATION))
/*  236 */       this.deprecationHandler.report(paramDiagnosticPosition, "has.been.deprecated", new Object[] { paramSymbol, paramSymbol.location() });
/*      */   }
/*      */ 
/*      */   public void warnUnchecked(JCDiagnostic.DiagnosticPosition paramDiagnosticPosition, String paramString, Object[] paramArrayOfObject)
/*      */   {
/*  244 */     if (!this.lint.isSuppressed(Lint.LintCategory.UNCHECKED))
/*  245 */       this.uncheckedHandler.report(paramDiagnosticPosition, paramString, paramArrayOfObject);
/*      */   }
/*      */ 
/*      */   void warnUnsafeVararg(JCDiagnostic.DiagnosticPosition paramDiagnosticPosition, String paramString, Object[] paramArrayOfObject)
/*      */   {
/*  252 */     if ((this.lint.isEnabled(Lint.LintCategory.VARARGS)) && (this.allowSimplifiedVarargs))
/*  253 */       this.log.warning(Lint.LintCategory.VARARGS, paramDiagnosticPosition, paramString, paramArrayOfObject);
/*      */   }
/*      */ 
/*      */   public void warnSunApi(JCDiagnostic.DiagnosticPosition paramDiagnosticPosition, String paramString, Object[] paramArrayOfObject)
/*      */   {
/*  261 */     if (!this.lint.isSuppressed(Lint.LintCategory.SUNAPI))
/*  262 */       this.sunApiHandler.report(paramDiagnosticPosition, paramString, paramArrayOfObject);
/*      */   }
/*      */ 
/*      */   public void warnStatic(JCDiagnostic.DiagnosticPosition paramDiagnosticPosition, String paramString, Object[] paramArrayOfObject) {
/*  266 */     if (this.lint.isEnabled(Lint.LintCategory.STATIC))
/*  267 */       this.log.warning(Lint.LintCategory.STATIC, paramDiagnosticPosition, paramString, paramArrayOfObject);
/*      */   }
/*      */ 
/*      */   public void reportDeferredDiagnostics()
/*      */   {
/*  274 */     this.deprecationHandler.reportDeferredDiagnostic();
/*  275 */     this.uncheckedHandler.reportDeferredDiagnostic();
/*  276 */     this.sunApiHandler.reportDeferredDiagnostic();
/*      */   }
/*      */ 
/*      */   public Type completionError(JCDiagnostic.DiagnosticPosition paramDiagnosticPosition, Symbol.CompletionFailure paramCompletionFailure)
/*      */   {
/*  285 */     this.log.error(JCDiagnostic.DiagnosticFlag.NON_DEFERRABLE, paramDiagnosticPosition, "cant.access", new Object[] { paramCompletionFailure.sym, paramCompletionFailure.getDetailValue() });
/*  286 */     if (((paramCompletionFailure instanceof ClassReader.BadClassFile)) && (!this.suppressAbortOnBadClassFile))
/*  287 */       throw new Abort();
/*  288 */     return this.syms.errType;
/*      */   }
/*      */ 
/*      */   Type typeTagError(JCDiagnostic.DiagnosticPosition paramDiagnosticPosition, Object paramObject1, Object paramObject2)
/*      */   {
/*  300 */     if (((paramObject2 instanceof Type)) && (((Type)paramObject2).hasTag(TypeTag.VOID))) {
/*  301 */       this.log.error(paramDiagnosticPosition, "illegal.start.of.type", new Object[0]);
/*  302 */       return this.syms.errType;
/*      */     }
/*  304 */     this.log.error(paramDiagnosticPosition, "type.found.req", new Object[] { paramObject2, paramObject1 });
/*  305 */     return this.types.createErrorType((paramObject2 instanceof Type) ? (Type)paramObject2 : this.syms.errType);
/*      */   }
/*      */ 
/*      */   void earlyRefError(JCDiagnostic.DiagnosticPosition paramDiagnosticPosition, Symbol paramSymbol)
/*      */   {
/*  314 */     this.log.error(paramDiagnosticPosition, "cant.ref.before.ctor.called", new Object[] { paramSymbol });
/*      */   }
/*      */ 
/*      */   void duplicateError(JCDiagnostic.DiagnosticPosition paramDiagnosticPosition, Symbol paramSymbol)
/*      */   {
/*  320 */     if (!paramSymbol.type.isErroneous()) {
/*  321 */       Symbol localSymbol = paramSymbol.location();
/*  322 */       if ((localSymbol.kind == 16) && 
/*  323 */         (((Symbol.MethodSymbol)localSymbol)
/*  323 */         .isStaticOrInstanceInit()))
/*  324 */         this.log.error(paramDiagnosticPosition, "already.defined.in.clinit", new Object[] { Kinds.kindName(paramSymbol), paramSymbol, 
/*  325 */           Kinds.kindName(paramSymbol
/*  325 */           .location()), Kinds.kindName(paramSymbol.location().enclClass()), paramSymbol
/*  326 */           .location().enclClass() });
/*      */       else
/*  328 */         this.log.error(paramDiagnosticPosition, "already.defined", new Object[] { Kinds.kindName(paramSymbol), paramSymbol, 
/*  329 */           Kinds.kindName(paramSymbol
/*  329 */           .location()), paramSymbol.location() });
/*      */     }
/*      */   }
/*      */ 
/*      */   void varargsDuplicateError(JCDiagnostic.DiagnosticPosition paramDiagnosticPosition, Symbol paramSymbol1, Symbol paramSymbol2)
/*      */   {
/*  337 */     if ((!paramSymbol1.type.isErroneous()) && (!paramSymbol2.type.isErroneous()))
/*  338 */       this.log.error(paramDiagnosticPosition, "array.and.varargs", new Object[] { paramSymbol1, paramSymbol2, paramSymbol2.location() });
/*      */   }
/*      */ 
/*      */   void checkTransparentVar(JCDiagnostic.DiagnosticPosition paramDiagnosticPosition, Symbol.VarSymbol paramVarSymbol, Scope paramScope)
/*      */   {
/*  353 */     if (paramScope.next != null)
/*  354 */       for (Scope.Entry localEntry = paramScope.next.lookup(paramVarSymbol.name); 
/*  355 */         (localEntry.scope != null) && (localEntry.sym.owner == paramVarSymbol.owner); 
/*  356 */         localEntry = localEntry.next())
/*  357 */         if ((localEntry.sym.kind == 4) && ((localEntry.sym.owner.kind & 0x14) != 0) && (paramVarSymbol.name != this.names.error))
/*      */         {
/*  360 */           duplicateError(paramDiagnosticPosition, localEntry.sym);
/*  361 */           return;
/*      */         }
/*      */   }
/*      */ 
/*      */   void checkTransparentClass(JCDiagnostic.DiagnosticPosition paramDiagnosticPosition, Symbol.ClassSymbol paramClassSymbol, Scope paramScope)
/*      */   {
/*  374 */     if (paramScope.next != null)
/*  375 */       for (Scope.Entry localEntry = paramScope.next.lookup(paramClassSymbol.name); 
/*  376 */         (localEntry.scope != null) && (localEntry.sym.owner == paramClassSymbol.owner); 
/*  377 */         localEntry = localEntry.next())
/*  378 */         if ((localEntry.sym.kind == 2) && (!localEntry.sym.type.hasTag(TypeTag.TYPEVAR)) && ((localEntry.sym.owner.kind & 0x14) != 0) && (paramClassSymbol.name != this.names.error))
/*      */         {
/*  381 */           duplicateError(paramDiagnosticPosition, localEntry.sym);
/*  382 */           return;
/*      */         }
/*      */   }
/*      */ 
/*      */   boolean checkUniqueClassName(JCDiagnostic.DiagnosticPosition paramDiagnosticPosition, Name paramName, Scope paramScope)
/*      */   {
/*  396 */     for (Object localObject = paramScope.lookup(paramName); ((Scope.Entry)localObject).scope == paramScope; localObject = ((Scope.Entry)localObject).next()) {
/*  397 */       if ((((Scope.Entry)localObject).sym.kind == 2) && (((Scope.Entry)localObject).sym.name != this.names.error)) {
/*  398 */         duplicateError(paramDiagnosticPosition, ((Scope.Entry)localObject).sym);
/*  399 */         return false;
/*      */       }
/*      */     }
/*  402 */     for (localObject = paramScope.owner; localObject != null; localObject = ((Symbol)localObject).owner) {
/*  403 */       if ((((Symbol)localObject).kind == 2) && (((Symbol)localObject).name == paramName) && (((Symbol)localObject).name != this.names.error)) {
/*  404 */         duplicateError(paramDiagnosticPosition, (Symbol)localObject);
/*  405 */         return true;
/*      */       }
/*      */     }
/*  408 */     return true;
/*      */   }
/*      */ 
/*      */   Name localClassName(Symbol.ClassSymbol paramClassSymbol)
/*      */   {
/*  422 */     for (int i = 1; ; i++)
/*      */     {
/*  424 */       Name localName = this.names
/*  424 */         .fromString("" + paramClassSymbol.owner
/*  424 */         .enclClass().flatname + this.syntheticNameChar + i + paramClassSymbol.name);
/*      */ 
/*  427 */       if (this.compiled.get(localName) == null) return localName;
/*      */     }
/*      */   }
/*      */ 
/*      */   Type checkType(JCDiagnostic.DiagnosticPosition paramDiagnosticPosition, Type paramType1, Type paramType2)
/*      */   {
/*  529 */     return checkType(paramDiagnosticPosition, paramType1, paramType2, this.basicHandler);
/*      */   }
/*      */ 
/*      */   Type checkType(final JCDiagnostic.DiagnosticPosition paramDiagnosticPosition, final Type paramType1, final Type paramType2, final CheckContext paramCheckContext) {
/*  533 */     Infer.InferenceContext localInferenceContext = paramCheckContext.inferenceContext();
/*  534 */     if ((localInferenceContext.free(paramType2)) || (localInferenceContext.free(paramType1))) {
/*  535 */       localInferenceContext.addFreeTypeListener(List.of(paramType2, paramType1), new Infer.FreeTypeListener()
/*      */       {
/*      */         public void typesInferred(Infer.InferenceContext paramAnonymousInferenceContext) {
/*  538 */           Check.this.checkType(paramDiagnosticPosition, paramAnonymousInferenceContext.asInstType(paramType1), paramAnonymousInferenceContext.asInstType(paramType2), paramCheckContext);
/*      */         }
/*      */       });
/*      */     }
/*  542 */     if (paramType2.hasTag(TypeTag.ERROR))
/*  543 */       return paramType2;
/*  544 */     if (paramType2.hasTag(TypeTag.NONE))
/*  545 */       return paramType1;
/*  546 */     if (paramCheckContext.compatible(paramType1, paramType2, paramCheckContext.checkWarner(paramDiagnosticPosition, paramType1, paramType2))) {
/*  547 */       return paramType1;
/*      */     }
/*  549 */     if ((paramType1.isNumeric()) && (paramType2.isNumeric())) {
/*  550 */       paramCheckContext.report(paramDiagnosticPosition, this.diags.fragment("possible.loss.of.precision", new Object[] { paramType1, paramType2 }));
/*  551 */       return this.types.createErrorType(paramType1);
/*      */     }
/*  553 */     paramCheckContext.report(paramDiagnosticPosition, this.diags.fragment("inconvertible.types", new Object[] { paramType1, paramType2 }));
/*  554 */     return this.types.createErrorType(paramType1);
/*      */   }
/*      */ 
/*      */   Type checkCastable(JCDiagnostic.DiagnosticPosition paramDiagnosticPosition, Type paramType1, Type paramType2)
/*      */   {
/*  565 */     return checkCastable(paramDiagnosticPosition, paramType1, paramType2, this.basicHandler);
/*      */   }
/*      */   Type checkCastable(JCDiagnostic.DiagnosticPosition paramDiagnosticPosition, Type paramType1, Type paramType2, CheckContext paramCheckContext) {
/*  568 */     if (this.types.isCastable(paramType1, paramType2, castWarner(paramDiagnosticPosition, paramType1, paramType2))) {
/*  569 */       return paramType2;
/*      */     }
/*  571 */     paramCheckContext.report(paramDiagnosticPosition, this.diags.fragment("inconvertible.types", new Object[] { paramType1, paramType2 }));
/*  572 */     return this.types.createErrorType(paramType1);
/*      */   }
/*      */ 
/*      */   public void checkRedundantCast(Env<AttrContext> paramEnv, final JCTree.JCTypeCast paramJCTypeCast)
/*      */   {
/*  580 */     if ((!paramJCTypeCast.type.isErroneous()) && 
/*  581 */       (this.types
/*  581 */       .isSameType(paramJCTypeCast.expr.type, paramJCTypeCast.clazz.type)) && 
/*  582 */       (!TreeInfo.containsTypeAnnotation(paramJCTypeCast.clazz)) && 
/*  583 */       (!is292targetTypeCast(paramJCTypeCast)))
/*      */     {
/*  584 */       this.deferredLintHandler.report(new DeferredLintHandler.LintLogger()
/*      */       {
/*      */         public void report() {
/*  587 */           if (Check.this.lint.isEnabled(Lint.LintCategory.CAST))
/*  588 */             Check.this.log.warning(Lint.LintCategory.CAST, paramJCTypeCast
/*  589 */               .pos(), "redundant.cast", new Object[] { paramJCTypeCast.expr.type });
/*      */         }
/*      */       });
/*      */     }
/*      */   }
/*      */ 
/*      */   private boolean is292targetTypeCast(JCTree.JCTypeCast paramJCTypeCast) {
/*  596 */     boolean bool = false;
/*  597 */     JCTree.JCExpression localJCExpression = TreeInfo.skipParens(paramJCTypeCast.expr);
/*  598 */     if (localJCExpression.hasTag(JCTree.Tag.APPLY)) {
/*  599 */       JCTree.JCMethodInvocation localJCMethodInvocation = (JCTree.JCMethodInvocation)localJCExpression;
/*  600 */       Symbol localSymbol = TreeInfo.symbol(localJCMethodInvocation.meth);
/*  601 */       if ((localSymbol != null) && (localSymbol.kind == 16));
/*  603 */       bool = (localSymbol
/*  603 */         .flags() & 0x0) != 0L;
/*      */     }
/*  605 */     return bool;
/*      */   }
/*      */ 
/*      */   private boolean checkExtends(Type paramType1, Type paramType2)
/*      */   {
/*  618 */     if (paramType1.isUnbound())
/*  619 */       return true;
/*  620 */     if (!paramType1.hasTag(TypeTag.WILDCARD)) {
/*  621 */       paramType1 = this.types.cvarUpperBound(paramType1);
/*  622 */       return this.types.isSubtype(paramType1, paramType2);
/*  623 */     }if (paramType1.isExtendsBound())
/*  624 */       return this.types.isCastable(paramType2, this.types.wildUpperBound(paramType1), this.types.noWarnings);
/*  625 */     if (paramType1.isSuperBound()) {
/*  626 */       return !this.types.notSoftSubtype(this.types.wildLowerBound(paramType1), paramType2);
/*      */     }
/*  628 */     return true;
/*      */   }
/*      */ 
/*      */   Type checkNonVoid(JCDiagnostic.DiagnosticPosition paramDiagnosticPosition, Type paramType)
/*      */   {
/*  636 */     if (paramType.hasTag(TypeTag.VOID)) {
/*  637 */       this.log.error(paramDiagnosticPosition, "void.not.allowed.here", new Object[0]);
/*  638 */       return this.types.createErrorType(paramType);
/*      */     }
/*  640 */     return paramType;
/*      */   }
/*      */ 
/*      */   Type checkClassOrArrayType(JCDiagnostic.DiagnosticPosition paramDiagnosticPosition, Type paramType)
/*      */   {
/*  645 */     if ((!paramType.hasTag(TypeTag.CLASS)) && (!paramType.hasTag(TypeTag.ARRAY)) && (!paramType.hasTag(TypeTag.ERROR))) {
/*  646 */       return typeTagError(paramDiagnosticPosition, this.diags
/*  647 */         .fragment("type.req.class.array", new Object[0]), 
/*  648 */         asTypeParam(paramType));
/*      */     }
/*      */ 
/*  650 */     return paramType;
/*      */   }
/*      */ 
/*      */   Type checkClassType(JCDiagnostic.DiagnosticPosition paramDiagnosticPosition, Type paramType)
/*      */   {
/*  659 */     if ((!paramType.hasTag(TypeTag.CLASS)) && (!paramType.hasTag(TypeTag.ERROR))) {
/*  660 */       return typeTagError(paramDiagnosticPosition, this.diags
/*  661 */         .fragment("type.req.class", new Object[0]), 
/*  662 */         asTypeParam(paramType));
/*      */     }
/*      */ 
/*  664 */     return paramType;
/*      */   }
/*      */ 
/*      */   private Object asTypeParam(Type paramType)
/*      */   {
/*  670 */     return paramType.hasTag(TypeTag.TYPEVAR) ? this.diags
/*  670 */       .fragment("type.parameter", new Object[] { paramType }) : 
/*  670 */       paramType;
/*      */   }
/*      */ 
/*      */   Type checkConstructorRefType(JCDiagnostic.DiagnosticPosition paramDiagnosticPosition, Type paramType)
/*      */   {
/*  677 */     paramType = checkClassOrArrayType(paramDiagnosticPosition, paramType);
/*  678 */     if (paramType.hasTag(TypeTag.CLASS)) {
/*  679 */       if ((paramType.tsym.flags() & 0x600) != 0L) {
/*  680 */         this.log.error(paramDiagnosticPosition, "abstract.cant.be.instantiated", new Object[] { paramType.tsym });
/*  681 */         paramType = this.types.createErrorType(paramType);
/*  682 */       } else if ((paramType.tsym.flags() & 0x4000) != 0L) {
/*  683 */         this.log.error(paramDiagnosticPosition, "enum.cant.be.instantiated", new Object[0]);
/*  684 */         paramType = this.types.createErrorType(paramType);
/*      */       } else {
/*  686 */         paramType = checkClassType(paramDiagnosticPosition, paramType, true);
/*      */       }
/*  688 */     } else if ((paramType.hasTag(TypeTag.ARRAY)) && 
/*  689 */       (!this.types.isReifiable(((Type.ArrayType)paramType).elemtype))) {
/*  690 */       this.log.error(paramDiagnosticPosition, "generic.array.creation", new Object[0]);
/*  691 */       paramType = this.types.createErrorType(paramType);
/*      */     }
/*      */ 
/*  694 */     return paramType;
/*      */   }
/*      */ 
/*      */   Type checkClassType(JCDiagnostic.DiagnosticPosition paramDiagnosticPosition, Type paramType, boolean paramBoolean)
/*      */   {
/*  703 */     paramType = checkClassType(paramDiagnosticPosition, paramType);
/*  704 */     if ((paramBoolean) && (paramType.isParameterized())) {
/*  705 */       List localList = paramType.getTypeArguments();
/*  706 */       while (localList.nonEmpty()) {
/*  707 */         if (((Type)localList.head).hasTag(TypeTag.WILDCARD)) {
/*  708 */           return typeTagError(paramDiagnosticPosition, this.diags
/*  709 */             .fragment("type.req.exact", new Object[0]), 
/*  709 */             localList.head);
/*      */         }
/*  711 */         localList = localList.tail;
/*      */       }
/*      */     }
/*  714 */     return paramType;
/*      */   }
/*      */ 
/*      */   Type checkRefType(JCDiagnostic.DiagnosticPosition paramDiagnosticPosition, Type paramType)
/*      */   {
/*  723 */     if (paramType.isReference()) {
/*  724 */       return paramType;
/*      */     }
/*  726 */     return typeTagError(paramDiagnosticPosition, this.diags
/*  727 */       .fragment("type.req.ref", new Object[0]), 
/*  727 */       paramType);
/*      */   }
/*      */ 
/*      */   List<Type> checkRefTypes(List<JCTree.JCExpression> paramList, List<Type> paramList1)
/*      */   {
/*  737 */     Object localObject1 = paramList;
/*  738 */     for (Object localObject2 = paramList1; ((List)localObject2).nonEmpty(); localObject2 = ((List)localObject2).tail) {
/*  739 */       ((List)localObject2).head = checkRefType(((JCTree.JCExpression)((List)localObject1).head).pos(), (Type)((List)localObject2).head);
/*  740 */       localObject1 = ((List)localObject1).tail;
/*      */     }
/*  742 */     return paramList1;
/*      */   }
/*      */ 
/*      */   Type checkNullOrRefType(JCDiagnostic.DiagnosticPosition paramDiagnosticPosition, Type paramType)
/*      */   {
/*  750 */     if ((paramType.isReference()) || (paramType.hasTag(TypeTag.BOT))) {
/*  751 */       return paramType;
/*      */     }
/*  753 */     return typeTagError(paramDiagnosticPosition, this.diags
/*  754 */       .fragment("type.req.ref", new Object[0]), 
/*  754 */       paramType);
/*      */   }
/*      */ 
/*      */   boolean checkDisjoint(JCDiagnostic.DiagnosticPosition paramDiagnosticPosition, long paramLong1, long paramLong2, long paramLong3)
/*      */   {
/*  766 */     if (((paramLong1 & paramLong2) != 0L) && ((paramLong1 & paramLong3) != 0L)) {
/*  767 */       this.log.error(paramDiagnosticPosition, "illegal.combination.of.modifiers", new Object[] { 
/*  769 */         Flags.asFlagSet(TreeInfo.firstFlag(paramLong1 & paramLong2)), 
/*  770 */         Flags.asFlagSet(TreeInfo.firstFlag(paramLong1 & paramLong3)) });
/*      */ 
/*  771 */       return false;
/*      */     }
/*  773 */     return true;
/*      */   }
/*      */ 
/*      */   Type checkDiamond(JCTree.JCNewClass paramJCNewClass, Type paramType)
/*      */   {
/*  780 */     if ((!TreeInfo.isDiamond(paramJCNewClass)) || 
/*  781 */       (paramType
/*  781 */       .isErroneous()))
/*  782 */       return checkClassType(paramJCNewClass.clazz.pos(), paramType, true);
/*  783 */     if (paramJCNewClass.def != null) {
/*  784 */       this.log.error(paramJCNewClass.clazz.pos(), "cant.apply.diamond.1", new Object[] { paramType, this.diags
/*  786 */         .fragment("diamond.and.anon.class", new Object[] { paramType }) });
/*      */ 
/*  787 */       return this.types.createErrorType(paramType);
/*  788 */     }if (paramType.tsym.type.getTypeArguments().isEmpty()) {
/*  789 */       this.log.error(paramJCNewClass.clazz.pos(), "cant.apply.diamond.1", new Object[] { paramType, this.diags
/*  791 */         .fragment("diamond.non.generic", new Object[] { paramType }) });
/*      */ 
/*  792 */       return this.types.createErrorType(paramType);
/*  793 */     }if ((paramJCNewClass.typeargs != null) && 
/*  794 */       (paramJCNewClass.typeargs
/*  794 */       .nonEmpty())) {
/*  795 */       this.log.error(paramJCNewClass.clazz.pos(), "cant.apply.diamond.1", new Object[] { paramType, this.diags
/*  797 */         .fragment("diamond.and.explicit.params", new Object[] { paramType }) });
/*      */ 
/*  798 */       return this.types.createErrorType(paramType);
/*      */     }
/*  800 */     return paramType;
/*      */   }
/*      */ 
/*      */   void checkVarargsMethodDecl(Env<AttrContext> paramEnv, JCTree.JCMethodDecl paramJCMethodDecl)
/*      */   {
/*  805 */     Symbol.MethodSymbol localMethodSymbol = paramJCMethodDecl.sym;
/*  806 */     if (!this.allowSimplifiedVarargs) return;
/*  807 */     int i = localMethodSymbol.attribute(this.syms.trustMeType.tsym) != null ? 1 : 0;
/*  808 */     Type localType = null;
/*  809 */     if (localMethodSymbol.isVarArgs()) {
/*  810 */       localType = this.types.elemtype(((JCTree.JCVariableDecl)paramJCMethodDecl.params.last()).type);
/*      */     }
/*  812 */     if ((i != 0) && (!isTrustMeAllowedOnMethod(localMethodSymbol))) {
/*  813 */       if (localType != null) {
/*  814 */         this.log.error(paramJCMethodDecl, "varargs.invalid.trustme.anno", new Object[] { this.syms.trustMeType.tsym, this.diags
/*  817 */           .fragment("varargs.trustme.on.virtual.varargs", new Object[] { localMethodSymbol }) });
/*      */       }
/*      */       else
/*      */       {
/*  819 */         this.log.error(paramJCMethodDecl, "varargs.invalid.trustme.anno", new Object[] { this.syms.trustMeType.tsym, this.diags
/*  822 */           .fragment("varargs.trustme.on.non.varargs.meth", new Object[] { localMethodSymbol }) });
/*      */       }
/*      */ 
/*      */     }
/*  824 */     else if ((i != 0) && (localType != null) && 
/*  825 */       (this.types
/*  825 */       .isReifiable(localType)))
/*      */     {
/*  826 */       warnUnsafeVararg(paramJCMethodDecl, "varargs.redundant.trustme.anno", new Object[] { this.syms.trustMeType.tsym, this.diags
/*  829 */         .fragment("varargs.trustme.on.reifiable.varargs", new Object[] { localType }) });
/*      */     }
/*  831 */     else if ((i == 0) && (localType != null) && 
/*  832 */       (!this.types
/*  832 */       .isReifiable(localType)))
/*      */     {
/*  833 */       warnUnchecked(((JCTree.JCVariableDecl)paramJCMethodDecl.params.head).pos(), "unchecked.varargs.non.reifiable.type", new Object[] { localType });
/*      */     }
/*      */   }
/*      */ 
/*      */   private boolean isTrustMeAllowedOnMethod(Symbol paramSymbol)
/*      */   {
/*  840 */     return ((paramSymbol.flags() & 0x0) != 0L) && (
/*  839 */       (paramSymbol
/*  839 */       .isConstructor()) || 
/*  840 */       ((paramSymbol
/*  840 */       .flags() & 0x18) != 0L));
/*      */   }
/*      */ 
/*      */   Type checkMethod(final Type paramType, final Symbol paramSymbol, final Env<AttrContext> paramEnv, final List<JCTree.JCExpression> paramList, final List<Type> paramList1, final boolean paramBoolean, Infer.InferenceContext paramInferenceContext)
/*      */   {
/*  853 */     if (paramInferenceContext.free(paramType)) {
/*  854 */       paramInferenceContext.addFreeTypeListener(List.of(paramType), new Infer.FreeTypeListener() {
/*      */         public void typesInferred(Infer.InferenceContext paramAnonymousInferenceContext) {
/*  856 */           Check.this.checkMethod(paramAnonymousInferenceContext.asInstType(paramType), paramSymbol, paramEnv, paramList, paramList1, paramBoolean, paramAnonymousInferenceContext);
/*      */         }
/*      */       });
/*  859 */       return paramType;
/*      */     }
/*  861 */     Type localType1 = paramType;
/*  862 */     List localList1 = localType1.getParameterTypes();
/*  863 */     List localList2 = paramSymbol.type.getParameterTypes();
/*  864 */     if (localList2.length() != localList1.length()) localList2 = localList1;
/*  865 */     Type localType2 = paramBoolean ? (Type)localList1.last() : null;
/*  866 */     if ((paramSymbol.name == this.names.init) && (paramSymbol.owner == this.syms.enumSym)) {
/*  867 */       localList1 = localList1.tail.tail;
/*  868 */       localList2 = localList2.tail.tail;
/*      */     }
/*  870 */     Object localObject1 = paramList;
/*  871 */     if (localObject1 != null)
/*      */     {
/*      */       Object localObject3;
/*  873 */       while (localList1.head != localType2) {
/*  874 */         localObject2 = (JCTree)((List)localObject1).head;
/*  875 */         localObject3 = convertWarner(((JCTree)localObject2).pos(), ((JCTree)localObject2).type, (Type)localList2.head);
/*  876 */         assertConvertible((JCTree)localObject2, ((JCTree)localObject2).type, (Type)localList1.head, (Warner)localObject3);
/*  877 */         localObject1 = ((List)localObject1).tail;
/*  878 */         localList1 = localList1.tail;
/*  879 */         localList2 = localList2.tail;
/*      */       }
/*  881 */       if (paramBoolean) {
/*  882 */         localObject2 = this.types.elemtype(localType2);
/*  883 */         while (((List)localObject1).tail != null) {
/*  884 */           localObject3 = (JCTree)((List)localObject1).head;
/*  885 */           Warner localWarner = convertWarner(((JCTree)localObject3).pos(), ((JCTree)localObject3).type, (Type)localObject2);
/*  886 */           assertConvertible((JCTree)localObject3, ((JCTree)localObject3).type, (Type)localObject2, localWarner);
/*  887 */           localObject1 = ((List)localObject1).tail;
/*      */         }
/*  889 */       } else if (((paramSymbol.flags() & 0x0) == 17179869184L) && (this.allowVarargs))
/*      */       {
/*  892 */         localObject2 = (Type)localType1.getParameterTypes().last();
/*  893 */         localObject3 = (Type)paramList1.last();
/*  894 */         if ((this.types.isSubtypeUnchecked((Type)localObject3, this.types.elemtype((Type)localObject2))) && 
/*  895 */           (!this.types
/*  895 */           .isSameType(this.types
/*  895 */           .erasure((Type)localObject2), 
/*  895 */           this.types.erasure((Type)localObject3))))
/*  896 */           this.log.warning(((JCTree.JCExpression)paramList.last()).pos(), "inexact.non-varargs.call", new Object[] { this.types
/*  897 */             .elemtype((Type)localObject2), 
/*  897 */             localObject2 });
/*      */       }
/*      */     }
/*  900 */     if (paramBoolean) {
/*  901 */       localObject2 = (Type)localType1.getParameterTypes().last();
/*  902 */       if ((!this.types.isReifiable((Type)localObject2)) && ((!this.allowSimplifiedVarargs) || 
/*  904 */         (paramSymbol
/*  904 */         .attribute(this.syms.trustMeType.tsym) == null) || 
/*  905 */         (!isTrustMeAllowedOnMethod(paramSymbol))))
/*      */       {
/*  906 */         warnUnchecked(paramEnv.tree.pos(), "unchecked.generic.array.creation", new Object[] { localObject2 });
/*      */       }
/*      */ 
/*  910 */       if ((paramSymbol.baseSymbol().flags() & 0x0) == 0L) {
/*  911 */         TreeInfo.setVarargsElement(paramEnv.tree, this.types.elemtype((Type)localObject2));
/*      */       }
/*      */     }
/*      */ 
/*  915 */     Object localObject2 = (paramSymbol.type.hasTag(TypeTag.FORALL)) && 
/*  915 */       (paramSymbol.type
/*  915 */       .getReturnType().containsAny(((Type.ForAll)paramSymbol.type).tvars)) ? JCTree.JCPolyExpression.PolyKind.POLY : JCTree.JCPolyExpression.PolyKind.STANDALONE;
/*      */ 
/*  917 */     TreeInfo.setPolyKind(paramEnv.tree, (JCTree.JCPolyExpression.PolyKind)localObject2);
/*  918 */     return localType1;
/*      */   }
/*      */ 
/*      */   private void assertConvertible(JCTree paramJCTree, Type paramType1, Type paramType2, Warner paramWarner) {
/*  922 */     if (this.types.isConvertible(paramType1, paramType2, paramWarner)) {
/*  923 */       return;
/*      */     }
/*  925 */     if ((paramType2.isCompound()) && 
/*  926 */       (this.types
/*  926 */       .isSubtype(paramType1, this.types
/*  926 */       .supertype(paramType2))) && 
/*  927 */       (this.types
/*  927 */       .isSubtypeUnchecked(paramType1, this.types
/*  927 */       .interfaces(paramType2), 
/*  927 */       paramWarner)));
/*      */   }
/*      */ 
/*      */   public boolean checkValidGenericType(Type paramType)
/*      */   {
/*  939 */     return firstIncompatibleTypeArg(paramType) == null;
/*      */   }
/*      */ 
/*      */   private Type firstIncompatibleTypeArg(Type paramType) {
/*  943 */     List localList1 = paramType.tsym.type.allparams();
/*  944 */     List localList2 = paramType.allparams();
/*  945 */     List localList3 = paramType.getTypeArguments();
/*  946 */     List localList4 = paramType.tsym.type.getTypeArguments();
/*  947 */     ListBuffer localListBuffer = new ListBuffer();
/*      */ 
/*  951 */     while ((localList3.nonEmpty()) && (localList4.nonEmpty()))
/*      */     {
/*  956 */       localListBuffer.append(this.types.subst(((Type)localList4.head).getUpperBound(), localList1, localList2));
/*  957 */       localList3 = localList3.tail;
/*  958 */       localList4 = localList4.tail;
/*      */     }
/*      */ 
/*  961 */     localList3 = paramType.getTypeArguments();
/*  962 */     List localList5 = this.types.substBounds(localList1, localList1, this.types
/*  964 */       .capture(paramType)
/*  964 */       .allparams());
/*  965 */     while ((localList3.nonEmpty()) && (localList5.nonEmpty()))
/*      */     {
/*  967 */       ((Type)localList3.head).withTypeVar((Type.TypeVar)localList5.head);
/*  968 */       localList3 = localList3.tail;
/*  969 */       localList5 = localList5.tail;
/*      */     }
/*      */ 
/*  972 */     localList3 = paramType.getTypeArguments();
/*  973 */     List localList6 = localListBuffer.toList();
/*      */ 
/*  975 */     while ((localList3.nonEmpty()) && (localList6.nonEmpty())) {
/*  976 */       localObject = (Type)localList3.head;
/*  977 */       if ((!isTypeArgErroneous((Type)localObject)) && 
/*  978 */         (!((Type)localList6.head)
/*  978 */         .isErroneous()) && 
/*  979 */         (!checkExtends((Type)localObject, (Type)localList6.head)))
/*      */       {
/*  980 */         return (Type)localList3.head;
/*      */       }
/*  982 */       localList3 = localList3.tail;
/*  983 */       localList6 = localList6.tail;
/*      */     }
/*      */ 
/*  986 */     localList3 = paramType.getTypeArguments();
/*  987 */     localList6 = localListBuffer.toList();
/*      */ 
/*  989 */     for (Object localObject = this.types.capture(paramType).getTypeArguments().iterator(); ((Iterator)localObject).hasNext(); ) { Type localType = (Type)((Iterator)localObject).next();
/*  990 */       if ((localType.hasTag(TypeTag.TYPEVAR)) && 
/*  991 */         (localType
/*  991 */         .getUpperBound().isErroneous()) && 
/*  992 */         (!((Type)localList6.head)
/*  992 */         .isErroneous()) && 
/*  993 */         (!isTypeArgErroneous((Type)localList3.head)))
/*      */       {
/*  994 */         return (Type)localList3.head;
/*      */       }
/*  996 */       localList6 = localList6.tail;
/*  997 */       localList3 = localList3.tail;
/*      */     }
/*      */ 
/* 1000 */     return null;
/*      */   }
/*      */ 
/*      */   boolean isTypeArgErroneous(Type paramType) {
/* 1004 */     return ((Boolean)this.isTypeArgErroneous.visit(paramType)).booleanValue();
/*      */   }
/*      */ 
/*      */   long checkFlags(JCDiagnostic.DiagnosticPosition paramDiagnosticPosition, long paramLong, Symbol paramSymbol, JCTree paramJCTree)
/*      */   {
/* 1037 */     long l2 = 0L;
/*      */     long l1;
/* 1039 */     switch (paramSymbol.kind) {
/*      */     case 4:
/* 1041 */       if (TreeInfo.isReceiverParam(paramJCTree))
/* 1042 */         l1 = 8589934592L;
/* 1043 */       else if (paramSymbol.owner.kind != 2)
/* 1044 */         l1 = 8589934608L;
/* 1045 */       else if ((paramSymbol.owner.flags_field & 0x200) != 0L)
/* 1046 */         l1 = l2 = 25L;
/*      */       else
/* 1048 */         l1 = 16607L;
/* 1049 */       break;
/*      */     case 16:
/* 1051 */       if (paramSymbol.name == this.names.init) {
/* 1052 */         if ((paramSymbol.owner.flags_field & 0x4000) != 0L)
/*      */         {
/* 1056 */           l2 = 2L;
/* 1057 */           l1 = 2L;
/*      */         } else {
/* 1059 */           l1 = 7L;
/*      */         } } else if ((paramSymbol.owner.flags_field & 0x200) != 0L) {
/* 1061 */         if ((paramSymbol.owner.flags_field & 0x2000) != 0L) {
/* 1062 */           l1 = 1025L;
/* 1063 */           l2 = 1025L;
/* 1064 */         } else if ((paramLong & 0x8) != 0L) {
/* 1065 */           l1 = 8796093025289L;
/* 1066 */           l2 = 1L;
/* 1067 */           if ((paramLong & 0x0) != 0L)
/* 1068 */             l2 |= 1024L;
/*      */         }
/*      */         else {
/* 1071 */           l1 = l2 = 1025L;
/*      */         }
/*      */       }
/* 1074 */       else l1 = 3391L;
/*      */ 
/* 1077 */       if ((((paramLong | l2) & 0x400) == 0L) || ((paramLong & 0x0) != 0L))
/*      */       {
/* 1079 */         l2 |= paramSymbol.owner.flags_field & 0x800; } break;
/*      */     case 2:
/* 1082 */       if (paramSymbol.isLocal()) {
/* 1083 */         l1 = 23568L;
/* 1084 */         if (paramSymbol.name.isEmpty())
/*      */         {
/* 1087 */           l1 |= 8L;
/*      */ 
/* 1089 */           l2 |= 16L;
/*      */         }
/* 1091 */         if (((paramSymbol.owner.flags_field & 0x8) == 0L) && ((paramLong & 0x4000) != 0L))
/*      */         {
/* 1093 */           this.log.error(paramDiagnosticPosition, "enums.must.be.static", new Object[0]);
/*      */         } } else if (paramSymbol.owner.kind == 2) {
/* 1095 */         l1 = 24087L;
/* 1096 */         if ((paramSymbol.owner.owner.kind == 1) || ((paramSymbol.owner.flags_field & 0x8) != 0L))
/*      */         {
/* 1098 */           l1 |= 8L;
/* 1099 */         } else if ((paramLong & 0x4000) != 0L) {
/* 1100 */           this.log.error(paramDiagnosticPosition, "enums.must.be.static", new Object[0]);
/*      */         }
/* 1102 */         if ((paramLong & 0x4200) != 0L) l2 = 8L; 
/*      */       }
/* 1104 */       else { l1 = 32273L; }
/*      */ 
/*      */ 
/* 1107 */       if ((paramLong & 0x200) != 0L) l2 |= 1024L;
/*      */ 
/* 1109 */       if ((paramLong & 0x4000) != 0L)
/*      */       {
/* 1111 */         l1 &= -1041L;
/* 1112 */         l2 |= implicitEnumFinalFlag(paramJCTree);
/*      */       }
/*      */ 
/* 1115 */       l2 |= paramSymbol.owner.flags_field & 0x800;
/* 1116 */       break;
/*      */     default:
/* 1118 */       throw new AssertionError();
/*      */     }
/* 1120 */     long l3 = paramLong & 0xFFF & (l1 ^ 0xFFFFFFFF);
/* 1121 */     if (l3 != 0L) {
/* 1122 */       if ((l3 & 0x200) != 0L) {
/* 1123 */         this.log.error(paramDiagnosticPosition, "intf.not.allowed.here", new Object[0]);
/* 1124 */         l1 |= 512L;
/*      */       }
/*      */       else {
/* 1127 */         this.log.error(paramDiagnosticPosition, "mod.not.allowed.here", new Object[] { 
/* 1128 */           Flags.asFlagSet(l3) });
/*      */       }
/*      */ 
/*      */     }
/* 1131 */     else if ((paramSymbol.kind == 2) || 
/* 1134 */       (checkDisjoint(paramDiagnosticPosition, paramLong, 1024L, 8796093022218L)))
/*      */     {
/* 1138 */       if (checkDisjoint(paramDiagnosticPosition, paramLong, 8L, 8796093022208L))
/*      */       {
/* 1142 */         if (checkDisjoint(paramDiagnosticPosition, paramLong, 1536L, 304L))
/*      */         {
/* 1146 */           if (checkDisjoint(paramDiagnosticPosition, paramLong, 1L, 6L))
/*      */           {
/* 1150 */             if (checkDisjoint(paramDiagnosticPosition, paramLong, 2L, 5L))
/*      */             {
/* 1154 */               if ((checkDisjoint(paramDiagnosticPosition, paramLong, 16L, 64L)) && 
/* 1154 */                 (paramSymbol.kind != 2))
/*      */               {
/* 1159 */                 if (!checkDisjoint(paramDiagnosticPosition, paramLong, 1280L, 2048L));
/*      */               }
/*      */             }
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/*      */ 
/* 1164 */     return paramLong & (l1 | 0xFFFFF000) | l2;
/*      */   }
/*      */ 
/*      */   private long implicitEnumFinalFlag(JCTree paramJCTree)
/*      */   {
/* 1176 */     if (!paramJCTree.hasTag(JCTree.Tag.CLASSDEF)) return 0L;
/*      */ 
/* 1197 */     JCTree.Visitor local1SpecialTreeVisitor = new JCTree.Visitor()
/*      */     {
/*      */       boolean specialized;
/*      */ 
/*      */       public void visitTree(JCTree paramAnonymousJCTree)
/*      */       {
/*      */       }
/*      */ 
/*      */       public void visitVarDef(JCTree.JCVariableDecl paramAnonymousJCVariableDecl)
/*      */       {
/* 1188 */         if (((paramAnonymousJCVariableDecl.mods.flags & 0x4000) != 0L) && 
/* 1189 */           ((paramAnonymousJCVariableDecl.init instanceof JCTree.JCNewClass)) && (((JCTree.JCNewClass)paramAnonymousJCVariableDecl.init).def != null))
/*      */         {
/* 1191 */           this.specialized = true;
/*      */         }
/*      */       }
/*      */     };
/* 1198 */     JCTree.JCClassDecl localJCClassDecl = (JCTree.JCClassDecl)paramJCTree;
/* 1199 */     for (JCTree localJCTree : localJCClassDecl.defs) {
/* 1200 */       localJCTree.accept(local1SpecialTreeVisitor);
/* 1201 */       if (local1SpecialTreeVisitor.specialized) return 0L;
/*      */     }
/* 1203 */     return 16L;
/*      */   }
/*      */ 
/*      */   void validate(JCTree paramJCTree, Env<AttrContext> paramEnv)
/*      */   {
/* 1227 */     validate(paramJCTree, paramEnv, true);
/*      */   }
/*      */   void validate(JCTree paramJCTree, Env<AttrContext> paramEnv, boolean paramBoolean) {
/* 1230 */     new Validator(paramEnv).validateTree(paramJCTree, paramBoolean, true);
/*      */   }
/*      */ 
/*      */   void validate(List<? extends JCTree> paramList, Env<AttrContext> paramEnv)
/*      */   {
/* 1236 */     for (Object localObject = paramList; ((List)localObject).nonEmpty(); localObject = ((List)localObject).tail)
/* 1237 */       validate((JCTree)((List)localObject).head, paramEnv);
/*      */   }
/*      */ 
/*      */   void checkRaw(JCTree paramJCTree, Env<AttrContext> paramEnv)
/*      */   {
/* 1377 */     if ((this.lint.isEnabled(Lint.LintCategory.RAW)) && 
/* 1378 */       (paramJCTree.type
/* 1378 */       .hasTag(TypeTag.CLASS)) && 
/* 1379 */       (!TreeInfo.isDiamond(paramJCTree)) && 
/* 1380 */       (!withinAnonConstr(paramEnv)) && 
/* 1381 */       (paramJCTree.type
/* 1381 */       .isRaw()))
/* 1382 */       this.log.warning(Lint.LintCategory.RAW, paramJCTree
/* 1383 */         .pos(), "raw.class.use", new Object[] { paramJCTree.type, paramJCTree.type.tsym.type });
/*      */   }
/*      */ 
/*      */   private boolean withinAnonConstr(Env<AttrContext> paramEnv)
/*      */   {
/* 1388 */     return (paramEnv.enclClass.name.isEmpty()) && (paramEnv.enclMethod != null) && (paramEnv.enclMethod.name == this.names.init);
/*      */   }
/*      */ 
/*      */   boolean subset(Type paramType, List<Type> paramList)
/*      */   {
/* 1403 */     for (Object localObject = paramList; ((List)localObject).nonEmpty(); localObject = ((List)localObject).tail)
/* 1404 */       if (this.types.isSubtype(paramType, (Type)((List)localObject).head)) return true;
/* 1405 */     return false;
/*      */   }
/*      */ 
/*      */   boolean intersects(Type paramType, List<Type> paramList)
/*      */   {
/* 1412 */     for (Object localObject = paramList; ((List)localObject).nonEmpty(); localObject = ((List)localObject).tail)
/* 1413 */       if ((this.types.isSubtype(paramType, (Type)((List)localObject).head)) || (this.types.isSubtype((Type)((List)localObject).head, paramType))) return true;
/* 1414 */     return false;
/*      */   }
/*      */ 
/*      */   List<Type> incl(Type paramType, List<Type> paramList)
/*      */   {
/* 1421 */     return subset(paramType, paramList) ? paramList : excl(paramType, paramList).prepend(paramType);
/*      */   }
/*      */ 
/*      */   List<Type> excl(Type paramType, List<Type> paramList)
/*      */   {
/* 1427 */     if (paramList.isEmpty()) {
/* 1428 */       return paramList;
/*      */     }
/* 1430 */     List localList = excl(paramType, paramList.tail);
/* 1431 */     if (this.types.isSubtype((Type)paramList.head, paramType)) return localList;
/* 1432 */     if (localList == paramList.tail) return paramList;
/* 1433 */     return localList.prepend(paramList.head);
/*      */   }
/*      */ 
/*      */   List<Type> union(List<Type> paramList1, List<Type> paramList2)
/*      */   {
/* 1440 */     Object localObject1 = paramList1;
/* 1441 */     for (Object localObject2 = paramList2; ((List)localObject2).nonEmpty(); localObject2 = ((List)localObject2).tail)
/* 1442 */       localObject1 = incl((Type)((List)localObject2).head, (List)localObject1);
/* 1443 */     return localObject1;
/*      */   }
/*      */ 
/*      */   List<Type> diff(List<Type> paramList1, List<Type> paramList2)
/*      */   {
/* 1449 */     Object localObject1 = paramList1;
/* 1450 */     for (Object localObject2 = paramList2; ((List)localObject2).nonEmpty(); localObject2 = ((List)localObject2).tail)
/* 1451 */       localObject1 = excl((Type)((List)localObject2).head, (List)localObject1);
/* 1452 */     return localObject1;
/*      */   }
/*      */ 
/*      */   public List<Type> intersect(List<Type> paramList1, List<Type> paramList2)
/*      */   {
/* 1458 */     List localList = List.nil();
/* 1459 */     for (Object localObject = paramList1; ((List)localObject).nonEmpty(); localObject = ((List)localObject).tail)
/* 1460 */       if (subset((Type)((List)localObject).head, paramList2)) localList = incl((Type)((List)localObject).head, localList);
/* 1461 */     for (localObject = paramList2; ((List)localObject).nonEmpty(); localObject = ((List)localObject).tail)
/* 1462 */       if (subset((Type)((List)localObject).head, paramList1)) localList = incl((Type)((List)localObject).head, localList);
/* 1463 */     return localList;
/*      */   }
/*      */ 
/*      */   boolean isUnchecked(Symbol.ClassSymbol paramClassSymbol)
/*      */   {
/* 1472 */     return (paramClassSymbol.kind == 63) || 
/* 1471 */       (paramClassSymbol
/* 1471 */       .isSubClass(this.syms.errorType.tsym, this.types)) || 
/* 1472 */       (paramClassSymbol
/* 1472 */       .isSubClass(this.syms.runtimeExceptionType.tsym, this.types));
/*      */   }
/*      */ 
/*      */   boolean isUnchecked(Type paramType)
/*      */   {
/* 1481 */     return paramType
/* 1480 */       .hasTag(TypeTag.CLASS) ? 
/* 1480 */       isUnchecked((Symbol.ClassSymbol)paramType.tsym) : paramType
/* 1479 */       .hasTag(TypeTag.TYPEVAR) ? 
/* 1479 */       isUnchecked(this.types.supertype(paramType)) : 
/* 1480 */       paramType
/* 1481 */       .hasTag(TypeTag.BOT);
/*      */   }
/*      */ 
/*      */   boolean isUnchecked(JCDiagnostic.DiagnosticPosition paramDiagnosticPosition, Type paramType)
/*      */   {
/*      */     try
/*      */     {
/* 1488 */       return isUnchecked(paramType);
/*      */     } catch (Symbol.CompletionFailure localCompletionFailure) {
/* 1490 */       completionError(paramDiagnosticPosition, localCompletionFailure);
/* 1491 */     }return true;
/*      */   }
/*      */ 
/*      */   boolean isHandled(Type paramType, List<Type> paramList)
/*      */   {
/* 1498 */     return (isUnchecked(paramType)) || (subset(paramType, paramList));
/*      */   }
/*      */ 
/*      */   List<Type> unhandled(List<Type> paramList1, List<Type> paramList2)
/*      */   {
/* 1506 */     List localList = List.nil();
/* 1507 */     for (Object localObject = paramList1; ((List)localObject).nonEmpty(); localObject = ((List)localObject).tail)
/* 1508 */       if (!isHandled((Type)((List)localObject).head, paramList2)) localList = localList.prepend(((List)localObject).head);
/* 1509 */     return localList;
/*      */   }
/*      */ 
/*      */   static int protection(long paramLong)
/*      */   {
/* 1520 */     switch ((short)(int)(paramLong & 0x7)) { case 2:
/* 1521 */       return 3;
/*      */     case 4:
/* 1522 */       return 1;
/*      */     case 1:
/*      */     case 3:
/*      */     default:
/* 1524 */       return 0;
/* 1525 */     case 0: } return 2;
/*      */   }
/*      */ 
/*      */   Object cannotOverride(Symbol.MethodSymbol paramMethodSymbol1, Symbol.MethodSymbol paramMethodSymbol2)
/*      */   {
/*      */     String str;
/* 1536 */     if ((paramMethodSymbol2.owner.flags() & 0x200) == 0L)
/* 1537 */       str = "cant.override";
/* 1538 */     else if ((paramMethodSymbol1.owner.flags() & 0x200) == 0L)
/* 1539 */       str = "cant.implement";
/*      */     else
/* 1541 */       str = "clashes.with";
/* 1542 */     return this.diags.fragment(str, new Object[] { paramMethodSymbol1, paramMethodSymbol1.location(), paramMethodSymbol2, paramMethodSymbol2.location() });
/*      */   }
/*      */ 
/*      */   Object uncheckedOverrides(Symbol.MethodSymbol paramMethodSymbol1, Symbol.MethodSymbol paramMethodSymbol2)
/*      */   {
/*      */     String str;
/* 1552 */     if ((paramMethodSymbol2.owner.flags() & 0x200) == 0L)
/* 1553 */       str = "unchecked.override";
/* 1554 */     else if ((paramMethodSymbol1.owner.flags() & 0x200) == 0L)
/* 1555 */       str = "unchecked.implement";
/*      */     else
/* 1557 */       str = "unchecked.clash.with";
/* 1558 */     return this.diags.fragment(str, new Object[] { paramMethodSymbol1, paramMethodSymbol1.location(), paramMethodSymbol2, paramMethodSymbol2.location() });
/*      */   }
/*      */ 
/*      */   Object varargsOverrides(Symbol.MethodSymbol paramMethodSymbol1, Symbol.MethodSymbol paramMethodSymbol2)
/*      */   {
/*      */     String str;
/* 1568 */     if ((paramMethodSymbol2.owner.flags() & 0x200) == 0L)
/* 1569 */       str = "varargs.override";
/* 1570 */     else if ((paramMethodSymbol1.owner.flags() & 0x200) == 0L)
/* 1571 */       str = "varargs.implement";
/*      */     else
/* 1573 */       str = "varargs.clash.with";
/* 1574 */     return this.diags.fragment(str, new Object[] { paramMethodSymbol1, paramMethodSymbol1.location(), paramMethodSymbol2, paramMethodSymbol2.location() });
/*      */   }
/*      */ 
/*      */   void checkOverride(JCTree paramJCTree, Symbol.MethodSymbol paramMethodSymbol1, Symbol.MethodSymbol paramMethodSymbol2, Symbol.ClassSymbol paramClassSymbol)
/*      */   {
/* 1603 */     if (((paramMethodSymbol1.flags() & 0x80001000) != 0L) || ((paramMethodSymbol2.flags() & 0x1000) != 0L)) {
/* 1604 */       return;
/*      */     }
/*      */ 
/* 1608 */     if (((paramMethodSymbol1.flags() & 0x8) != 0L) && 
/* 1609 */       ((paramMethodSymbol2
/* 1609 */       .flags() & 0x8) == 0L)) {
/* 1610 */       this.log.error(TreeInfo.diagnosticPositionFor(paramMethodSymbol1, paramJCTree), "override.static", new Object[] { 
/* 1611 */         cannotOverride(paramMethodSymbol1, paramMethodSymbol2) });
/*      */ 
/* 1612 */       paramMethodSymbol1.flags_field |= 35184372088832L;
/* 1613 */       return;
/*      */     }
/*      */ 
/* 1618 */     if (((paramMethodSymbol2.flags() & 0x10) != 0L) || (
/* 1619 */       ((paramMethodSymbol1
/* 1619 */       .flags() & 0x8) == 0L) && 
/* 1620 */       ((paramMethodSymbol2
/* 1620 */       .flags() & 0x8) != 0L))) {
/* 1621 */       this.log.error(TreeInfo.diagnosticPositionFor(paramMethodSymbol1, paramJCTree), "override.meth", new Object[] { 
/* 1622 */         cannotOverride(paramMethodSymbol1, paramMethodSymbol2), 
/* 1623 */         Flags.asFlagSet(paramMethodSymbol2
/* 1623 */         .flags() & 0x18) });
/* 1624 */       paramMethodSymbol1.flags_field |= 35184372088832L;
/* 1625 */       return;
/*      */     }
/*      */ 
/* 1628 */     if ((paramMethodSymbol1.owner.flags() & 0x2000) != 0L)
/*      */     {
/* 1630 */       return;
/*      */     }
/*      */ 
/* 1634 */     if (((paramClassSymbol.flags() & 0x200) == 0L) && 
/* 1635 */       (protection(paramMethodSymbol1
/* 1635 */       .flags()) > protection(paramMethodSymbol2.flags()))) {
/* 1636 */       this.log.error(TreeInfo.diagnosticPositionFor(paramMethodSymbol1, paramJCTree), "override.weaker.access", new Object[] { 
/* 1637 */         cannotOverride(paramMethodSymbol1, paramMethodSymbol2), 
/* 1637 */         paramMethodSymbol2
/* 1638 */         .flags() == 0L ? "package" : 
/* 1640 */         Flags.asFlagSet(paramMethodSymbol2
/* 1640 */         .flags() & 0x7) });
/* 1641 */       paramMethodSymbol1.flags_field |= 35184372088832L;
/* 1642 */       return;
/*      */     }
/*      */ 
/* 1645 */     Type localType1 = this.types.memberType(paramClassSymbol.type, paramMethodSymbol1);
/* 1646 */     Type localType2 = this.types.memberType(paramClassSymbol.type, paramMethodSymbol2);
/*      */ 
/* 1651 */     List localList1 = localType1.getTypeArguments();
/* 1652 */     List localList2 = localType2.getTypeArguments();
/* 1653 */     Type localType3 = localType1.getReturnType();
/* 1654 */     Type localType4 = this.types.subst(localType2.getReturnType(), localList2, localList1);
/*      */ 
/* 1656 */     this.overrideWarner.clear();
/*      */ 
/* 1658 */     boolean bool = this.types
/* 1658 */       .returnTypeSubstitutable(localType1, localType2, localType4, this.overrideWarner);
/*      */ 
/* 1659 */     if (!bool) {
/* 1660 */       if ((this.allowCovariantReturns) || (paramMethodSymbol1.owner == paramClassSymbol) || 
/* 1662 */         (!paramMethodSymbol1.owner
/* 1662 */         .isSubClass(paramMethodSymbol2.owner, this.types)))
/*      */       {
/* 1665 */         this.log.error(TreeInfo.diagnosticPositionFor(paramMethodSymbol1, paramJCTree), "override.incompatible.ret", new Object[] { 
/* 1667 */           cannotOverride(paramMethodSymbol1, paramMethodSymbol2), 
/* 1667 */           localType3, localType4 });
/*      */ 
/* 1669 */         paramMethodSymbol1.flags_field |= 35184372088832L;
/*      */       }
/*      */     }
/* 1672 */     else if (this.overrideWarner.hasNonSilentLint(Lint.LintCategory.UNCHECKED)) {
/* 1673 */       warnUnchecked(TreeInfo.diagnosticPositionFor(paramMethodSymbol1, paramJCTree), "override.unchecked.ret", new Object[] { 
/* 1675 */         uncheckedOverrides(paramMethodSymbol1, paramMethodSymbol2), 
/* 1675 */         localType3, localType4 });
/*      */     }
/*      */ 
/* 1681 */     List localList3 = this.types.subst(localType2.getThrownTypes(), localList2, localList1);
/* 1682 */     List localList4 = unhandled(localType1.getThrownTypes(), this.types.erasure(localList3));
/* 1683 */     List localList5 = unhandled(localType1.getThrownTypes(), localList3);
/* 1684 */     if (localList4.nonEmpty()) {
/* 1685 */       this.log.error(TreeInfo.diagnosticPositionFor(paramMethodSymbol1, paramJCTree), "override.meth.doesnt.throw", new Object[] { 
/* 1687 */         cannotOverride(paramMethodSymbol1, paramMethodSymbol2), 
/* 1687 */         localList5.head });
/*      */ 
/* 1689 */       paramMethodSymbol1.flags_field |= 35184372088832L;
/* 1690 */       return;
/*      */     }
/* 1692 */     if (localList5.nonEmpty()) {
/* 1693 */       warnUnchecked(TreeInfo.diagnosticPositionFor(paramMethodSymbol1, paramJCTree), "override.unchecked.thrown", new Object[] { 
/* 1695 */         cannotOverride(paramMethodSymbol1, paramMethodSymbol2), 
/* 1695 */         localList5.head });
/*      */ 
/* 1697 */       return;
/*      */     }
/*      */ 
/* 1701 */     if ((((paramMethodSymbol1.flags() ^ paramMethodSymbol2.flags()) & 0x0) != 0L) && 
/* 1702 */       (this.lint
/* 1702 */       .isEnabled(Lint.LintCategory.OVERRIDES)))
/*      */     {
/* 1703 */       this.log.warning(TreeInfo.diagnosticPositionFor(paramMethodSymbol1, paramJCTree), 
/* 1704 */         (paramMethodSymbol1
/* 1704 */         .flags() & 0x0) != 0L ? "override.varargs.missing" : "override.varargs.extra", new Object[] { 
/* 1707 */         varargsOverrides(paramMethodSymbol1, paramMethodSymbol2) });
/*      */     }
/*      */ 
/* 1711 */     if ((paramMethodSymbol2.flags() & 0x80000000) != 0L) {
/* 1712 */       this.log.warning(TreeInfo.diagnosticPositionFor(paramMethodSymbol1, paramJCTree), "override.bridge", new Object[] { 
/* 1713 */         uncheckedOverrides(paramMethodSymbol1, paramMethodSymbol2) });
/*      */     }
/*      */ 
/* 1717 */     if (!isDeprecatedOverrideIgnorable(paramMethodSymbol2, paramClassSymbol)) {
/* 1718 */       Lint localLint = setLint(this.lint.augment(paramMethodSymbol1));
/*      */       try {
/* 1720 */         checkDeprecated(TreeInfo.diagnosticPositionFor(paramMethodSymbol1, paramJCTree), paramMethodSymbol1, paramMethodSymbol2);
/*      */       } finally {
/* 1722 */         setLint(localLint);
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   private boolean isDeprecatedOverrideIgnorable(Symbol.MethodSymbol paramMethodSymbol, Symbol.ClassSymbol paramClassSymbol)
/*      */   {
/* 1735 */     Symbol.ClassSymbol localClassSymbol = paramMethodSymbol.enclClass();
/* 1736 */     Type localType = this.types.supertype(paramClassSymbol.type);
/* 1737 */     if (!localType.hasTag(TypeTag.CLASS))
/* 1738 */       return true;
/* 1739 */     Symbol.MethodSymbol localMethodSymbol = paramMethodSymbol.implementation((Symbol.ClassSymbol)localType.tsym, this.types, false);
/*      */ 
/* 1741 */     if ((localClassSymbol != null) && ((localClassSymbol.flags() & 0x200) != 0L)) {
/* 1742 */       List localList = this.types.interfaces(paramClassSymbol.type);
/* 1743 */       return !localList.contains(localClassSymbol.type);
/*      */     }
/*      */ 
/* 1746 */     return localMethodSymbol != paramMethodSymbol;
/*      */   }
/*      */ 
/*      */   public void checkCompatibleConcretes(JCDiagnostic.DiagnosticPosition paramDiagnosticPosition, Type paramType)
/*      */   {
/* 1759 */     Type localType1 = this.types.supertype(paramType);
/* 1760 */     if (!localType1.hasTag(TypeTag.CLASS)) return;
/*      */ 
/* 1762 */     for (Type localType2 = localType1; 
/* 1763 */       (localType2.hasTag(TypeTag.CLASS)) && (localType2.tsym.type.isParameterized()); 
/* 1764 */       localType2 = this.types.supertype(localType2))
/* 1765 */       for (Scope.Entry localEntry1 = localType2.tsym.members().elems; 
/* 1766 */         localEntry1 != null; 
/* 1767 */         localEntry1 = localEntry1.sibling) {
/* 1768 */         Symbol localSymbol1 = localEntry1.sym;
/* 1769 */         if ((localSymbol1.kind == 16) && 
/* 1770 */           ((localSymbol1
/* 1770 */           .flags() & 0x80001008) == 0L) && 
/* 1771 */           (localSymbol1
/* 1771 */           .isInheritedIn(paramType.tsym, this.types)) && 
/* 1772 */           (((Symbol.MethodSymbol)localSymbol1)
/* 1772 */           .implementation(paramType.tsym, this.types, true) == 
/* 1772 */           localSymbol1))
/*      */         {
/* 1776 */           Type localType3 = this.types.memberType(localType2, localSymbol1);
/* 1777 */           int i = localType3.getParameterTypes().length();
/* 1778 */           if (localType3 != localSymbol1.type)
/*      */           {
/* 1780 */             for (Type localType4 = localType1; 
/* 1781 */               localType4.hasTag(TypeTag.CLASS); 
/* 1782 */               localType4 = this.types.supertype(localType4))
/* 1783 */               for (Scope.Entry localEntry2 = localType4.tsym.members().lookup(localSymbol1.name); 
/* 1784 */                 localEntry2.scope != null; 
/* 1785 */                 localEntry2 = localEntry2.next()) {
/* 1786 */                 Symbol localSymbol2 = localEntry2.sym;
/* 1787 */                 if ((localSymbol2 != localSymbol1) && (localSymbol2.kind == 16))
/*      */                 {
/* 1789 */                   if (((localSymbol2
/* 1789 */                     .flags() & 0x80001008) == 0L) && 
/* 1790 */                     (localSymbol2.type
/* 1790 */                     .getParameterTypes().length() == i) && 
/* 1791 */                     (localSymbol2
/* 1791 */                     .isInheritedIn(paramType.tsym, this.types)) && 
/* 1792 */                     (((Symbol.MethodSymbol)localSymbol2)
/* 1792 */                     .implementation(paramType.tsym, this.types, true) == 
/* 1792 */                     localSymbol2))
/*      */                   {
/* 1796 */                     Type localType5 = this.types.memberType(localType4, localSymbol2);
/* 1797 */                     if (this.types.overrideEquivalent(localType3, localType5))
/* 1798 */                       this.log.error(paramDiagnosticPosition, "concrete.inheritance.conflict", new Object[] { localSymbol1, localType2, localSymbol2, localType4, localType1 });
/*      */                   }
/*      */                 }
/*      */               }
/*      */           }
/*      */         }
/*      */       }
/*      */   }
/*      */ 
/*      */   public boolean checkCompatibleAbstracts(JCDiagnostic.DiagnosticPosition paramDiagnosticPosition, Type paramType1, Type paramType2)
/*      */   {
/* 1815 */     return checkCompatibleAbstracts(paramDiagnosticPosition, paramType1, paramType2, this.types
/* 1816 */       .makeIntersectionType(paramType1, paramType2));
/*      */   }
/*      */ 
/*      */   public boolean checkCompatibleAbstracts(JCDiagnostic.DiagnosticPosition paramDiagnosticPosition, Type paramType1, Type paramType2, Type paramType3)
/*      */   {
/* 1823 */     if ((paramType3.tsym.flags() & 0x1000000) != 0L)
/*      */     {
/* 1825 */       paramType1 = this.types.capture(paramType1);
/* 1826 */       paramType2 = this.types.capture(paramType2);
/*      */     }
/* 1828 */     return firstIncompatibility(paramDiagnosticPosition, paramType1, paramType2, paramType3) == null;
/*      */   }
/*      */ 
/*      */   private Symbol firstIncompatibility(JCDiagnostic.DiagnosticPosition paramDiagnosticPosition, Type paramType1, Type paramType2, Type paramType3)
/*      */   {
/* 1840 */     HashMap localHashMap1 = new HashMap();
/* 1841 */     closure(paramType1, localHashMap1);
/*      */     HashMap localHashMap2;
/* 1843 */     if (paramType1 == paramType2)
/* 1844 */       localHashMap2 = localHashMap1;
/*      */     else {
/* 1846 */       closure(paramType2, localHashMap1, localHashMap2 = new HashMap());
/*      */     }
/* 1848 */     for (Iterator localIterator1 = localHashMap1.values().iterator(); localIterator1.hasNext(); ) { localType1 = (Type)localIterator1.next();
/* 1849 */       for (Type localType2 : localHashMap2.values()) {
/* 1850 */         Symbol localSymbol = firstDirectIncompatibility(paramDiagnosticPosition, localType1, localType2, paramType3);
/* 1851 */         if (localSymbol != null) return localSymbol;
/*      */       }
/*      */     }
/*      */     Type localType1;
/* 1854 */     return null;
/*      */   }
/*      */ 
/*      */   private void closure(Type paramType, Map<Symbol.TypeSymbol, Type> paramMap)
/*      */   {
/* 1859 */     if (!paramType.hasTag(TypeTag.CLASS)) return;
/* 1860 */     if (paramMap.put(paramType.tsym, paramType) == null) {
/* 1861 */       closure(this.types.supertype(paramType), paramMap);
/* 1862 */       for (Type localType : this.types.interfaces(paramType))
/* 1863 */         closure(localType, paramMap);
/*      */     }
/*      */   }
/*      */ 
/*      */   private void closure(Type paramType, Map<Symbol.TypeSymbol, Type> paramMap1, Map<Symbol.TypeSymbol, Type> paramMap2)
/*      */   {
/* 1869 */     if (!paramType.hasTag(TypeTag.CLASS)) return;
/* 1870 */     if (paramMap1.get(paramType.tsym) != null) return;
/* 1871 */     if (paramMap2.put(paramType.tsym, paramType) == null) {
/* 1872 */       closure(this.types.supertype(paramType), paramMap1, paramMap2);
/* 1873 */       for (Type localType : this.types.interfaces(paramType))
/* 1874 */         closure(localType, paramMap1, paramMap2);
/*      */     }
/*      */   }
/*      */ 
/*      */   private Symbol firstDirectIncompatibility(JCDiagnostic.DiagnosticPosition paramDiagnosticPosition, Type paramType1, Type paramType2, Type paramType3)
/*      */   {
/* 1880 */     for (Scope.Entry localEntry1 = paramType1.tsym.members().elems; localEntry1 != null; localEntry1 = localEntry1.sibling) {
/* 1881 */       Symbol localSymbol1 = localEntry1.sym;
/* 1882 */       Type localType1 = null;
/* 1883 */       if ((localSymbol1.kind == 16) && (localSymbol1.isInheritedIn(paramType3.tsym, this.types)) && 
/* 1884 */         ((localSymbol1
/* 1884 */         .flags() & 0x1000) == 0L)) {
/* 1885 */         Symbol.MethodSymbol localMethodSymbol = ((Symbol.MethodSymbol)localSymbol1).implementation(paramType3.tsym, this.types, false);
/* 1886 */         if ((localMethodSymbol == null) || ((localMethodSymbol.flags() & 0x400) != 0L))
/* 1887 */           for (Scope.Entry localEntry2 = paramType2.tsym.members().lookup(localSymbol1.name); localEntry2.scope != null; localEntry2 = localEntry2.next()) {
/* 1888 */             Symbol localSymbol2 = localEntry2.sym;
/* 1889 */             if ((localSymbol1 != localSymbol2) && 
/* 1890 */               (localSymbol2.kind == 16) && (localSymbol2.isInheritedIn(paramType3.tsym, this.types)) && 
/* 1891 */               ((localSymbol2
/* 1891 */               .flags() & 0x1000) == 0L)) {
/* 1892 */               if (localType1 == null) localType1 = this.types.memberType(paramType1, localSymbol1);
/* 1893 */               Type localType2 = this.types.memberType(paramType2, localSymbol2);
/* 1894 */               if (this.types.overrideEquivalent(localType1, localType2)) {
/* 1895 */                 List localList1 = localType1.getTypeArguments();
/* 1896 */                 List localList2 = localType2.getTypeArguments();
/* 1897 */                 Type localType3 = localType1.getReturnType();
/* 1898 */                 Type localType4 = this.types.subst(localType2.getReturnType(), localList2, localList1);
/*      */ 
/* 1905 */                 int i = (this.types
/* 1900 */                   .isSameType(localType3, localType4)) || 
/* 1901 */                   ((!localType3
/* 1901 */                   .isPrimitiveOrVoid()) && 
/* 1902 */                   (!localType4
/* 1902 */                   .isPrimitiveOrVoid()) && (
/* 1903 */                   (this.types
/* 1903 */                   .covariantReturnType(localType3, localType4, this.types.noWarnings)) || 
/* 1904 */                   (this.types
/* 1904 */                   .covariantReturnType(localType4, localType3, this.types.noWarnings)))) || 
/* 1905 */                   (checkCommonOverriderIn(localSymbol1, localSymbol2, paramType3)) ? 
/* 1905 */                   1 : 0;
/* 1906 */                 if (i == 0) {
/* 1907 */                   this.log.error(paramDiagnosticPosition, "types.incompatible.diff.ret", new Object[] { paramType1, paramType2, localSymbol2.name + "(" + this.types
/* 1909 */                     .memberType(paramType2, localSymbol2)
/* 1909 */                     .getParameterTypes() + ")" });
/* 1910 */                   return localSymbol2;
/*      */                 }
/* 1912 */               } else if ((checkNameClash((Symbol.ClassSymbol)paramType3.tsym, localSymbol1, localSymbol2)) && 
/* 1913 */                 (!checkCommonOverriderIn(localSymbol1, localSymbol2, paramType3)))
/*      */               {
/* 1914 */                 this.log.error(paramDiagnosticPosition, "name.clash.same.erasure.no.override", new Object[] { localSymbol1, localSymbol1
/* 1916 */                   .location(), localSymbol2, localSymbol2
/* 1917 */                   .location() });
/* 1918 */                 return localSymbol2;
/*      */               }
/*      */             }
/*      */           }
/*      */       }
/*      */     }
/* 1922 */     return null;
/*      */   }
/*      */ 
/*      */   boolean checkCommonOverriderIn(Symbol paramSymbol1, Symbol paramSymbol2, Type paramType) {
/* 1926 */     HashMap localHashMap = new HashMap();
/* 1927 */     Type localType1 = this.types.memberType(paramType, paramSymbol1);
/* 1928 */     Type localType2 = this.types.memberType(paramType, paramSymbol2);
/* 1929 */     closure(paramType, localHashMap);
/* 1930 */     for (Type localType3 : localHashMap.values())
/* 1931 */       for (Scope.Entry localEntry = localType3.tsym.members().lookup(paramSymbol1.name); localEntry.scope != null; localEntry = localEntry.next()) {
/* 1932 */         Symbol localSymbol = localEntry.sym;
/* 1933 */         if ((localSymbol != paramSymbol1) && (localSymbol != paramSymbol2) && (localSymbol.kind == 16) && ((localSymbol.flags() & 0x80001000) == 0L)) {
/* 1934 */           Type localType4 = this.types.memberType(paramType, localSymbol);
/* 1935 */           if ((this.types.overrideEquivalent(localType4, localType1)) && 
/* 1936 */             (this.types
/* 1936 */             .overrideEquivalent(localType4, localType2)) && 
/* 1937 */             (this.types
/* 1937 */             .returnTypeSubstitutable(localType4, localType1)) && 
/* 1938 */             (this.types
/* 1938 */             .returnTypeSubstitutable(localType4, localType2)))
/*      */           {
/* 1939 */             return true;
/*      */           }
/*      */         }
/*      */       }
/* 1943 */     return false;
/*      */   }
/*      */ 
/*      */   void checkOverride(JCTree.JCMethodDecl paramJCMethodDecl, Symbol.MethodSymbol paramMethodSymbol)
/*      */   {
/* 1952 */     Symbol.ClassSymbol localClassSymbol = (Symbol.ClassSymbol)paramMethodSymbol.owner;
/* 1953 */     if (((localClassSymbol.flags() & 0x4000) != 0L) && (this.names.finalize.equals(paramMethodSymbol.name)) && 
/* 1954 */       (paramMethodSymbol.overrides(this.syms.enumFinalFinalize, localClassSymbol, this.types, false))) {
/* 1955 */       this.log.error(paramJCMethodDecl.pos(), "enum.no.finalize", new Object[0]);
/*      */       return;
/*      */     }
/*      */     Iterator localIterator;
/*      */     Object localObject2;
/* 1958 */     for (Object localObject1 = localClassSymbol.type; ((Type)localObject1).hasTag(TypeTag.CLASS); 
/* 1959 */       localObject1 = this.types.supertype((Type)localObject1)) {
/* 1960 */       if (localObject1 != localClassSymbol.type) {
/* 1961 */         checkOverride(paramJCMethodDecl, (Type)localObject1, localClassSymbol, paramMethodSymbol);
/*      */       }
/* 1963 */       for (localIterator = this.types.interfaces((Type)localObject1).iterator(); localIterator.hasNext(); ) { localObject2 = (Type)localIterator.next();
/* 1964 */         checkOverride(paramJCMethodDecl, (Type)localObject2, localClassSymbol, paramMethodSymbol);
/*      */       }
/*      */     }
/*      */ 
/* 1968 */     if ((paramMethodSymbol.attribute(this.syms.overrideType.tsym) != null) && (!isOverrider(paramMethodSymbol))) {
/* 1969 */       localObject1 = paramJCMethodDecl.pos();
/* 1970 */       for (localIterator = paramJCMethodDecl.getModifiers().annotations.iterator(); localIterator.hasNext(); ) { localObject2 = (JCTree.JCAnnotation)localIterator.next();
/* 1971 */         if (((JCTree.JCAnnotation)localObject2).annotationType.type.tsym == this.syms.overrideType.tsym) {
/* 1972 */           localObject1 = ((JCTree.JCAnnotation)localObject2).pos();
/* 1973 */           break;
/*      */         }
/*      */       }
/* 1976 */       this.log.error((JCDiagnostic.DiagnosticPosition)localObject1, "method.does.not.override.superclass", new Object[0]);
/*      */     }
/*      */   }
/*      */ 
/*      */   void checkOverride(JCTree paramJCTree, Type paramType, Symbol.ClassSymbol paramClassSymbol, Symbol.MethodSymbol paramMethodSymbol) {
/* 1981 */     Symbol.TypeSymbol localTypeSymbol = paramType.tsym;
/* 1982 */     Scope.Entry localEntry = localTypeSymbol.members().lookup(paramMethodSymbol.name);
/* 1983 */     while (localEntry.scope != null) {
/* 1984 */       if ((paramMethodSymbol.overrides(localEntry.sym, paramClassSymbol, this.types, false)) && 
/* 1985 */         ((localEntry.sym.flags() & 0x400) == 0L)) {
/* 1986 */         checkOverride(paramJCTree, paramMethodSymbol, (Symbol.MethodSymbol)localEntry.sym, paramClassSymbol);
/*      */       }
/*      */ 
/* 1989 */       localEntry = localEntry.next();
/*      */     }
/*      */   }
/*      */ 
/*      */   public void checkClassOverrideEqualsAndHashIfNeeded(JCDiagnostic.DiagnosticPosition paramDiagnosticPosition, Symbol.ClassSymbol paramClassSymbol)
/*      */   {
/* 2007 */     if ((paramClassSymbol == (Symbol.ClassSymbol)this.syms.objectType.tsym) || 
/* 2008 */       (paramClassSymbol
/* 2008 */       .isInterface()) || (paramClassSymbol.isEnum()) || 
/* 2009 */       ((paramClassSymbol
/* 2009 */       .flags() & 0x2000) != 0L) || 
/* 2010 */       ((paramClassSymbol
/* 2010 */       .flags() & 0x400) != 0L)) return;
/*      */ 
/* 2012 */     if (paramClassSymbol.isAnonymous()) {
/* 2013 */       List localList = this.types.interfaces(paramClassSymbol.type);
/* 2014 */       if ((localList != null) && (!localList.isEmpty()) && (((Type)localList.head).tsym == this.syms.comparatorType.tsym))
/* 2015 */         return;
/*      */     }
/* 2017 */     checkClassOverrideEqualsAndHash(paramDiagnosticPosition, paramClassSymbol);
/*      */   }
/*      */ 
/*      */   private void checkClassOverrideEqualsAndHash(JCDiagnostic.DiagnosticPosition paramDiagnosticPosition, Symbol.ClassSymbol paramClassSymbol)
/*      */   {
/* 2022 */     if (this.lint.isEnabled(Lint.LintCategory.OVERRIDES))
/*      */     {
/* 2024 */       Symbol.MethodSymbol localMethodSymbol1 = (Symbol.MethodSymbol)this.syms.objectType.tsym
/* 2024 */         .members().lookup(this.names.equals).sym;
/*      */ 
/* 2026 */       Symbol.MethodSymbol localMethodSymbol2 = (Symbol.MethodSymbol)this.syms.objectType.tsym
/* 2026 */         .members().lookup(this.names.hashCode).sym;
/* 2027 */       int i = this.types.implementation(localMethodSymbol1, paramClassSymbol, false, this.equalsHasCodeFilter).owner == paramClassSymbol ? 1 : 0;
/*      */ 
/* 2029 */       int j = this.types.implementation(localMethodSymbol2, paramClassSymbol, false, this.equalsHasCodeFilter) != localMethodSymbol2 ? 1 : 0;
/*      */ 
/* 2032 */       if ((i != 0) && (j == 0))
/* 2033 */         this.log.warning(Lint.LintCategory.OVERRIDES, paramDiagnosticPosition, "override.equals.but.not.hashcode", new Object[] { paramClassSymbol });
/*      */     }
/*      */   }
/*      */ 
/*      */   private boolean checkNameClash(Symbol.ClassSymbol paramClassSymbol, Symbol paramSymbol1, Symbol paramSymbol2)
/*      */   {
/* 2040 */     ClashFilter localClashFilter = new ClashFilter(paramClassSymbol.type);
/*      */ 
/* 2043 */     return (localClashFilter.accepts(paramSymbol1)) && 
/* 2042 */       (localClashFilter
/* 2042 */       .accepts(paramSymbol2)) && 
/* 2043 */       (this.types
/* 2043 */       .hasSameArgs(paramSymbol1
/* 2043 */       .erasure(this.types), 
/* 2043 */       paramSymbol2.erasure(this.types)));
/*      */   }
/*      */ 
/*      */   void checkAllDefined(JCDiagnostic.DiagnosticPosition paramDiagnosticPosition, Symbol.ClassSymbol paramClassSymbol)
/*      */   {
/* 2052 */     Symbol.MethodSymbol localMethodSymbol1 = this.types.firstUnimplementedAbstract(paramClassSymbol);
/* 2053 */     if (localMethodSymbol1 != null)
/*      */     {
/* 2056 */       Symbol.MethodSymbol localMethodSymbol2 = new Symbol.MethodSymbol(localMethodSymbol1
/* 2055 */         .flags(), localMethodSymbol1.name, this.types
/* 2056 */         .memberType(paramClassSymbol.type, localMethodSymbol1), 
/* 2056 */         localMethodSymbol1.owner);
/* 2057 */       this.log.error(paramDiagnosticPosition, "does.not.override.abstract", new Object[] { paramClassSymbol, localMethodSymbol2, localMethodSymbol2
/* 2058 */         .location() });
/*      */     }
/*      */   }
/*      */ 
/*      */   void checkNonCyclicDecl(JCTree.JCClassDecl paramJCClassDecl) {
/* 2063 */     CycleChecker localCycleChecker = new CycleChecker();
/* 2064 */     localCycleChecker.scan(paramJCClassDecl);
/* 2065 */     if ((!localCycleChecker.errorFound) && (!localCycleChecker.partialCheck))
/* 2066 */       paramJCClassDecl.sym.flags_field |= 1073741824L;
/*      */   }
/*      */ 
/*      */   void checkNonCyclic(JCDiagnostic.DiagnosticPosition paramDiagnosticPosition, Type paramType)
/*      */   {
/* 2176 */     checkNonCyclicInternal(paramDiagnosticPosition, paramType);
/*      */   }
/*      */ 
/*      */   void checkNonCyclic(JCDiagnostic.DiagnosticPosition paramDiagnosticPosition, Type.TypeVar paramTypeVar)
/*      */   {
/* 2181 */     checkNonCyclic1(paramDiagnosticPosition, paramTypeVar, List.nil());
/*      */   }
/*      */ 
/*      */   private void checkNonCyclic1(JCDiagnostic.DiagnosticPosition paramDiagnosticPosition, Type paramType, List<Type.TypeVar> paramList)
/*      */   {
/* 2186 */     if ((paramType.hasTag(TypeTag.TYPEVAR)) && ((paramType.tsym.flags() & 0x10000000) != 0L))
/*      */       return;
/*      */     Type.TypeVar localTypeVar;
/* 2188 */     if (paramList.contains(paramType)) {
/* 2189 */       localTypeVar = (Type.TypeVar)paramType.unannotatedType();
/* 2190 */       localTypeVar.bound = this.types.createErrorType(paramType);
/* 2191 */       this.log.error(paramDiagnosticPosition, "cyclic.inheritance", new Object[] { paramType });
/* 2192 */     } else if (paramType.hasTag(TypeTag.TYPEVAR)) {
/* 2193 */       localTypeVar = (Type.TypeVar)paramType.unannotatedType();
/* 2194 */       paramList = paramList.prepend(localTypeVar);
/* 2195 */       for (Type localType : this.types.getBounds(localTypeVar))
/* 2196 */         checkNonCyclic1(paramDiagnosticPosition, localType, paramList);
/*      */     }
/*      */   }
/*      */ 
/*      */   private boolean checkNonCyclicInternal(JCDiagnostic.DiagnosticPosition paramDiagnosticPosition, Type paramType)
/*      */   {
/* 2208 */     boolean bool = true;
/*      */ 
/* 2210 */     Symbol.TypeSymbol localTypeSymbol = paramType.tsym;
/* 2211 */     if ((localTypeSymbol.flags_field & 0x40000000) != 0L) return true;
/*      */ 
/* 2213 */     if ((localTypeSymbol.flags_field & 0x8000000) != 0L)
/* 2214 */       noteCyclic(paramDiagnosticPosition, (Symbol.ClassSymbol)localTypeSymbol);
/* 2215 */     else if (!localTypeSymbol.type.isErroneous()) {
/*      */       try {
/* 2217 */         localTypeSymbol.flags_field |= 134217728L;
/* 2218 */         if (localTypeSymbol.type.hasTag(TypeTag.CLASS)) {
/* 2219 */           Type.ClassType localClassType = (Type.ClassType)localTypeSymbol.type;
/*      */           Object localObject1;
/* 2220 */           if (localClassType.interfaces_field != null)
/* 2221 */             for (localObject1 = localClassType.interfaces_field; ((List)localObject1).nonEmpty(); localObject1 = ((List)localObject1).tail)
/* 2222 */               bool &= checkNonCyclicInternal(paramDiagnosticPosition, (Type)((List)localObject1).head);
/* 2223 */           if (localClassType.supertype_field != null) {
/* 2224 */             localObject1 = localClassType.supertype_field;
/* 2225 */             if ((localObject1 != null) && (((Type)localObject1).hasTag(TypeTag.CLASS)))
/* 2226 */               bool &= checkNonCyclicInternal(paramDiagnosticPosition, (Type)localObject1);
/*      */           }
/* 2228 */           if (localTypeSymbol.owner.kind == 2)
/* 2229 */             bool &= checkNonCyclicInternal(paramDiagnosticPosition, localTypeSymbol.owner.type);
/*      */         }
/*      */       } finally {
/* 2232 */         localTypeSymbol.flags_field &= -134217729L;
/*      */       }
/*      */     }
/* 2235 */     if (bool)
/* 2236 */       bool = ((localTypeSymbol.flags_field & 0x10000000) == 0L) && (localTypeSymbol.completer == null);
/* 2237 */     if (bool) localTypeSymbol.flags_field |= 1073741824L;
/* 2238 */     return bool;
/*      */   }
/*      */ 
/*      */   private void noteCyclic(JCDiagnostic.DiagnosticPosition paramDiagnosticPosition, Symbol.ClassSymbol paramClassSymbol)
/*      */   {
/* 2243 */     this.log.error(paramDiagnosticPosition, "cyclic.inheritance", new Object[] { paramClassSymbol });
/* 2244 */     for (Object localObject = this.types.interfaces(paramClassSymbol.type); ((List)localObject).nonEmpty(); localObject = ((List)localObject).tail)
/* 2245 */       ((List)localObject).head = this.types.createErrorType((Symbol.ClassSymbol)((Type)((List)localObject).head).tsym, Type.noType);
/* 2246 */     localObject = this.types.supertype(paramClassSymbol.type);
/* 2247 */     if (((Type)localObject).hasTag(TypeTag.CLASS))
/* 2248 */       ((Type.ClassType)paramClassSymbol.type).supertype_field = this.types.createErrorType((Symbol.ClassSymbol)((Type)localObject).tsym, Type.noType);
/* 2249 */     paramClassSymbol.type = this.types.createErrorType(paramClassSymbol, paramClassSymbol.type);
/* 2250 */     paramClassSymbol.flags_field |= 1073741824L;
/*      */   }
/*      */ 
/*      */   void checkImplementations(JCTree.JCClassDecl paramJCClassDecl)
/*      */   {
/* 2258 */     checkImplementations(paramJCClassDecl, paramJCClassDecl.sym, paramJCClassDecl.sym);
/*      */   }
/*      */ 
/*      */   void checkImplementations(JCTree paramJCTree, Symbol.ClassSymbol paramClassSymbol1, Symbol.ClassSymbol paramClassSymbol2)
/*      */   {
/* 2265 */     for (List localList = this.types.closure(paramClassSymbol2.type); localList.nonEmpty(); localList = localList.tail) {
/* 2266 */       Symbol.ClassSymbol localClassSymbol = (Symbol.ClassSymbol)((Type)localList.head).tsym;
/* 2267 */       if (((this.allowGenerics) || (paramClassSymbol1 != localClassSymbol)) && ((localClassSymbol.flags() & 0x400) != 0L))
/* 2268 */         for (Scope.Entry localEntry = localClassSymbol.members().elems; localEntry != null; localEntry = localEntry.sibling)
/* 2269 */           if ((localEntry.sym.kind == 16) && 
/* 2270 */             ((localEntry.sym
/* 2270 */             .flags() & 0x408) == 1024L)) {
/* 2271 */             Symbol.MethodSymbol localMethodSymbol1 = (Symbol.MethodSymbol)localEntry.sym;
/* 2272 */             Symbol.MethodSymbol localMethodSymbol2 = localMethodSymbol1.implementation(paramClassSymbol1, this.types, false);
/* 2273 */             if ((localMethodSymbol2 != null) && (localMethodSymbol2 != localMethodSymbol1))
/*      */             {
/* 2275 */               if ((localMethodSymbol2.owner
/* 2274 */                 .flags() & 0x200) == 
/* 2275 */                 (paramClassSymbol1
/* 2275 */                 .flags() & 0x200))
/*      */               {
/* 2282 */                 checkOverride(paramJCTree, localMethodSymbol2, localMethodSymbol1, paramClassSymbol1);
/*      */               }
/*      */             }
/*      */           }
/*      */     }
/*      */   }
/*      */ 
/*      */   void checkCompatibleSupertypes(JCDiagnostic.DiagnosticPosition paramDiagnosticPosition, Type paramType)
/*      */   {
/* 2296 */     List localList1 = this.types.interfaces(paramType);
/* 2297 */     Type localType = this.types.supertype(paramType);
/* 2298 */     if ((localType.hasTag(TypeTag.CLASS)) && 
/* 2299 */       ((localType.tsym
/* 2299 */       .flags() & 0x400) != 0L))
/* 2300 */       localList1 = localList1.prepend(localType);
/* 2301 */     for (List localList2 = localList1; localList2.nonEmpty(); localList2 = localList2.tail) {
/* 2302 */       if ((this.allowGenerics) && (!((Type)localList2.head).getTypeArguments().isEmpty()) && 
/* 2303 */         (!checkCompatibleAbstracts(paramDiagnosticPosition, (Type)localList2.head, (Type)localList2.head, paramType)))
/*      */       {
/* 2304 */         return;
/* 2305 */       }for (List localList3 = localList1; localList3 != localList2; localList3 = localList3.tail)
/* 2306 */         if (!checkCompatibleAbstracts(paramDiagnosticPosition, (Type)localList2.head, (Type)localList3.head, paramType))
/* 2307 */           return;
/*      */     }
/* 2309 */     checkCompatibleConcretes(paramDiagnosticPosition, paramType);
/*      */   }
/*      */ 
/*      */   void checkConflicts(JCDiagnostic.DiagnosticPosition paramDiagnosticPosition, Symbol paramSymbol, Symbol.TypeSymbol paramTypeSymbol) {
/* 2313 */     for (Type localType = paramTypeSymbol.type; localType != Type.noType; localType = this.types.supertype(localType))
/* 2314 */       for (Scope.Entry localEntry = localType.tsym.members().lookup(paramSymbol.name); localEntry.scope == localType.tsym.members(); localEntry = localEntry.next())
/*      */       {
/* 2316 */         if ((paramSymbol.kind == localEntry.sym.kind) && 
/* 2317 */           (this.types
/* 2317 */           .isSameType(this.types
/* 2317 */           .erasure(paramSymbol.type), 
/* 2317 */           this.types.erasure(localEntry.sym.type))) && (paramSymbol != localEntry.sym))
/*      */         {
/* 2319 */           if (((paramSymbol
/* 2319 */             .flags() & 0x1000) != (localEntry.sym.flags() & 0x1000)) && 
/* 2320 */             ((paramSymbol
/* 2320 */             .flags() & 0x200000) == 0L) && ((localEntry.sym.flags() & 0x200000) == 0L) && 
/* 2321 */             ((paramSymbol
/* 2321 */             .flags() & 0x80000000) == 0L) && ((localEntry.sym.flags() & 0x80000000) == 0L)) {
/* 2322 */             syntheticError(paramDiagnosticPosition, (localEntry.sym.flags() & 0x1000) == 0L ? localEntry.sym : paramSymbol);
/* 2323 */             return;
/*      */           }
/*      */         }
/*      */       }
/*      */   }
/*      */ 
/*      */   void checkOverrideClashes(JCDiagnostic.DiagnosticPosition paramDiagnosticPosition, Type paramType, Symbol.MethodSymbol paramMethodSymbol)
/*      */   {
/* 2337 */     ClashFilter localClashFilter = new ClashFilter(paramType);
/*      */ 
/* 2341 */     List localList = List.nil();
/* 2342 */     int i = 0;
/* 2343 */     for (Iterator localIterator1 = this.types.membersClosure(paramType, false).getElementsByName(paramMethodSymbol.name, localClashFilter).iterator(); localIterator1.hasNext(); ) { localObject = (Symbol)localIterator1.next();
/* 2344 */       if (!paramMethodSymbol.overrides((Symbol)localObject, paramType.tsym, this.types, false)) {
/* 2345 */         if ((localObject != paramMethodSymbol) && 
/* 2349 */           (i == 0)) {
/* 2350 */           localList = localList.prepend((Symbol.MethodSymbol)localObject);
/*      */         }
/*      */       }
/*      */       else
/*      */       {
/* 2355 */         if (localObject != paramMethodSymbol) {
/* 2356 */           i = 1;
/* 2357 */           localList = List.nil();
/*      */         }
/*      */ 
/* 2361 */         for (Symbol localSymbol : this.types.membersClosure(paramType, false).getElementsByName(paramMethodSymbol.name, localClashFilter))
/* 2362 */           if (localSymbol != localObject)
/*      */           {
/* 2365 */             if ((!this.types.isSubSignature(paramMethodSymbol.type, this.types.memberType(paramType, localSymbol), this.allowStrictMethodClashCheck)) && 
/* 2366 */               (this.types
/* 2366 */               .hasSameArgs(localSymbol
/* 2366 */               .erasure(this.types), 
/* 2366 */               ((Symbol)localObject).erasure(this.types)))) {
/* 2367 */               paramMethodSymbol.flags_field |= 4398046511104L;
/* 2368 */               String str = localObject == paramMethodSymbol ? "name.clash.same.erasure.no.override" : "name.clash.same.erasure.no.override.1";
/*      */ 
/* 2371 */               this.log.error(paramDiagnosticPosition, str, new Object[] { paramMethodSymbol, paramMethodSymbol
/* 2373 */                 .location(), localSymbol, localSymbol
/* 2374 */                 .location(), localObject, ((Symbol)localObject)
/* 2375 */                 .location() });
/*      */               return;
/*      */             }
/*      */           }
/*      */       }
/*      */     }
/*      */     Object localObject;
/* 2381 */     if (i == 0)
/* 2382 */       for (localIterator1 = localList.iterator(); localIterator1.hasNext(); ) { localObject = (Symbol.MethodSymbol)localIterator1.next();
/* 2383 */         checkPotentiallyAmbiguousOverloads(paramDiagnosticPosition, paramType, paramMethodSymbol, (Symbol.MethodSymbol)localObject);
/*      */       }
/*      */   }
/*      */ 
/*      */   void checkHideClashes(JCDiagnostic.DiagnosticPosition paramDiagnosticPosition, Type paramType, Symbol.MethodSymbol paramMethodSymbol)
/*      */   {
/* 2396 */     ClashFilter localClashFilter = new ClashFilter(paramType);
/*      */ 
/* 2398 */     for (Symbol localSymbol : this.types.membersClosure(paramType, true).getElementsByName(paramMethodSymbol.name, localClashFilter))
/*      */     {
/* 2401 */       if (!this.types.isSubSignature(paramMethodSymbol.type, this.types.memberType(paramType, localSymbol), this.allowStrictMethodClashCheck)) {
/* 2402 */         if (this.types.hasSameArgs(localSymbol.erasure(this.types), paramMethodSymbol.erasure(this.types))) {
/* 2403 */           this.log.error(paramDiagnosticPosition, "name.clash.same.erasure.no.hide", new Object[] { paramMethodSymbol, paramMethodSymbol
/* 2405 */             .location(), localSymbol, localSymbol
/* 2406 */             .location() });
/* 2407 */           return;
/*      */         }
/* 2409 */         checkPotentiallyAmbiguousOverloads(paramDiagnosticPosition, paramType, paramMethodSymbol, (Symbol.MethodSymbol)localSymbol);
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   void checkDefaultMethodClashes(JCDiagnostic.DiagnosticPosition paramDiagnosticPosition, Type paramType)
/*      */   {
/* 2439 */     DefaultMethodClashFilter localDefaultMethodClashFilter = new DefaultMethodClashFilter(paramType);
/* 2440 */     for (Iterator localIterator1 = this.types.membersClosure(paramType, false).getElements(localDefaultMethodClashFilter).iterator(); localIterator1.hasNext(); ) { localSymbol1 = (Symbol)localIterator1.next();
/* 2441 */       Assert.check(localSymbol1.kind == 16);
/* 2442 */       List localList = this.types.interfaceCandidates(paramType, (Symbol.MethodSymbol)localSymbol1);
/* 2443 */       if (localList.size() > 1) {
/* 2444 */         localListBuffer1 = new ListBuffer();
/* 2445 */         localListBuffer2 = new ListBuffer();
/* 2446 */         for (Symbol.MethodSymbol localMethodSymbol : localList) {
/* 2447 */           if ((localMethodSymbol.flags() & 0x0) != 0L)
/* 2448 */             localListBuffer2 = localListBuffer2.append(localMethodSymbol);
/* 2449 */           else if ((localMethodSymbol.flags() & 0x400) != 0L) {
/* 2450 */             localListBuffer1 = localListBuffer1.append(localMethodSymbol);
/*      */           }
/* 2452 */           if ((localListBuffer2.nonEmpty()) && (localListBuffer2.size() + localListBuffer1.size() >= 2))
/*      */           {
/* 2457 */             Symbol localSymbol2 = (Symbol)localListBuffer2.first();
/*      */             String str;
/*      */             Symbol localSymbol3;
/* 2459 */             if (localListBuffer2.size() > 1) {
/* 2460 */               str = "types.incompatible.unrelated.defaults";
/* 2461 */               localSymbol3 = (Symbol)localListBuffer2.toList().tail.head;
/*      */             } else {
/* 2463 */               str = "types.incompatible.abstract.default";
/* 2464 */               localSymbol3 = (Symbol)localListBuffer1.first();
/*      */             }
/* 2466 */             this.log.error(paramDiagnosticPosition, str, new Object[] { 
/* 2467 */               Kinds.kindName(paramType.tsym), 
/* 2467 */               paramType, localSymbol1.name, this.types
/* 2468 */               .memberType(paramType, localSymbol1)
/* 2468 */               .getParameterTypes(), localSymbol2
/* 2469 */               .location(), localSymbol3.location() });
/* 2470 */             break;
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/*      */     Symbol localSymbol1;
/*      */     ListBuffer localListBuffer1;
/*      */     ListBuffer localListBuffer2;
/*      */   }
/*      */ 
/*      */   void checkPotentiallyAmbiguousOverloads(JCDiagnostic.DiagnosticPosition paramDiagnosticPosition, Type paramType, Symbol.MethodSymbol paramMethodSymbol1, Symbol.MethodSymbol paramMethodSymbol2)
/*      */   {
/* 2502 */     if ((paramMethodSymbol1 != paramMethodSymbol2) && (this.allowDefaultMethods))
/*      */     {
/* 2504 */       if ((this.lint
/* 2504 */         .isEnabled(Lint.LintCategory.OVERLOADS)) && 
/* 2505 */         ((paramMethodSymbol1
/* 2505 */         .flags() & 0x0) == 0L) && 
/* 2506 */         ((paramMethodSymbol2
/* 2506 */         .flags() & 0x0) == 0L)) {
/* 2507 */         Type localType1 = this.types.memberType(paramType, paramMethodSymbol1);
/* 2508 */         Type localType2 = this.types.memberType(paramType, paramMethodSymbol2);
/*      */ 
/* 2510 */         if ((localType1.hasTag(TypeTag.FORALL)) && (localType2.hasTag(TypeTag.FORALL)) && 
/* 2511 */           (this.types
/* 2511 */           .hasSameBounds((Type.ForAll)localType1, (Type.ForAll)localType2)))
/*      */         {
/* 2512 */           localType2 = this.types.subst(localType2, ((Type.ForAll)localType2).tvars, ((Type.ForAll)localType1).tvars);
/*      */         }
/*      */ 
/* 2515 */         int i = Math.max(localType1.getParameterTypes().length(), localType2.getParameterTypes().length());
/* 2516 */         List localList1 = this.rs.adjustArgs(localType1.getParameterTypes(), paramMethodSymbol1, i, true);
/* 2517 */         List localList2 = this.rs.adjustArgs(localType2.getParameterTypes(), paramMethodSymbol2, i, true);
/*      */ 
/* 2519 */         if (localList1.length() != localList2.length()) return;
/* 2520 */         int j = 0;
/* 2521 */         while ((localList1.nonEmpty()) && (localList2.nonEmpty())) {
/* 2522 */           Type localType3 = (Type)localList1.head;
/* 2523 */           Type localType4 = (Type)localList2.head;
/* 2524 */           if ((!this.types.isSubtype(localType4, localType3)) && (!this.types.isSubtype(localType3, localType4))) {
/* 2525 */             if ((!this.types.isFunctionalInterface(localType3)) || (!this.types.isFunctionalInterface(localType4)) || 
/* 2526 */               (this.types
/* 2526 */               .findDescriptorType(localType3)
/* 2526 */               .getParameterTypes().length() <= 0))
/*      */               break;
/* 2528 */             if (this.types
/* 2527 */               .findDescriptorType(localType3)
/* 2527 */               .getParameterTypes().length() != this.types
/* 2528 */               .findDescriptorType(localType4)
/* 2528 */               .getParameterTypes().length()) break;
/* 2529 */             j = 1;
/*      */           }
/*      */ 
/* 2534 */           localList1 = localList1.tail;
/* 2535 */           localList2 = localList2.tail;
/*      */         }
/* 2537 */         if (j != 0)
/*      */         {
/* 2540 */           paramMethodSymbol1.flags_field |= 281474976710656L;
/* 2541 */           paramMethodSymbol2.flags_field |= 281474976710656L;
/* 2542 */           this.log.warning(Lint.LintCategory.OVERLOADS, paramDiagnosticPosition, "potentially.ambiguous.overload", new Object[] { paramMethodSymbol1, paramMethodSymbol1
/* 2543 */             .location(), paramMethodSymbol2, paramMethodSymbol2
/* 2544 */             .location() });
/* 2545 */           return;
/*      */         }
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/* 2551 */   void checkElemAccessFromSerializableLambda(JCTree paramJCTree) { if (this.warnOnAccessToSensitiveMembers) {
/* 2552 */       Symbol localSymbol = TreeInfo.symbol(paramJCTree);
/* 2553 */       if ((localSymbol.kind & 0x14) == 0) {
/* 2554 */         return;
/*      */       }
/*      */ 
/* 2557 */       if ((localSymbol.kind == 4) && (
/* 2558 */         ((localSymbol.flags() & 0x0) != 0L) || 
/* 2559 */         (localSymbol
/* 2559 */         .isLocal()) || (localSymbol.name == this.names._this) || (localSymbol.name == this.names._super)))
/*      */       {
/* 2562 */         return;
/*      */       }
/*      */ 
/* 2566 */       if ((!this.types.isSubtype(localSymbol.owner.type, this.syms.serializableType)) && 
/* 2567 */         (isEffectivelyNonPublic(localSymbol)))
/*      */       {
/* 2568 */         this.log.warning(paramJCTree.pos(), "access.to.sensitive.member.from.serializable.element", new Object[] { localSymbol });
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   private boolean isEffectivelyNonPublic(Symbol paramSymbol)
/*      */   {
/* 2575 */     if (paramSymbol.packge() == this.syms.rootPackage) {
/* 2576 */       return false;
/*      */     }
/*      */ 
/* 2579 */     while (paramSymbol.kind != 1) {
/* 2580 */       if ((paramSymbol.flags() & 1L) == 0L) {
/* 2581 */         return true;
/*      */       }
/* 2583 */       paramSymbol = paramSymbol.owner;
/*      */     }
/* 2585 */     return false;
/*      */   }
/*      */ 
/*      */   private void syntheticError(JCDiagnostic.DiagnosticPosition paramDiagnosticPosition, Symbol paramSymbol)
/*      */   {
/* 2591 */     if (!paramSymbol.type.isErroneous())
/* 2592 */       if (this.warnOnSyntheticConflicts) {
/* 2593 */         this.log.warning(paramDiagnosticPosition, "synthetic.name.conflict", new Object[] { paramSymbol, paramSymbol.location() });
/*      */       }
/*      */       else
/* 2596 */         this.log.error(paramDiagnosticPosition, "synthetic.name.conflict", new Object[] { paramSymbol, paramSymbol.location() });
/*      */   }
/*      */ 
/*      */   void checkClassBounds(JCDiagnostic.DiagnosticPosition paramDiagnosticPosition, Type paramType)
/*      */   {
/* 2607 */     checkClassBounds(paramDiagnosticPosition, new HashMap(), paramType);
/*      */   }
/*      */ 
/*      */   void checkClassBounds(JCDiagnostic.DiagnosticPosition paramDiagnosticPosition, Map<Symbol.TypeSymbol, Type> paramMap, Type paramType)
/*      */   {
/* 2617 */     if (paramType.isErroneous()) return;
/* 2618 */     for (Object localObject = this.types.interfaces(paramType); ((List)localObject).nonEmpty(); localObject = ((List)localObject).tail) {
/* 2619 */       Type localType1 = (Type)((List)localObject).head;
/* 2620 */       Type localType2 = (Type)paramMap.put(localType1.tsym, localType1);
/* 2621 */       if (localType2 != null) {
/* 2622 */         List localList1 = localType2.allparams();
/* 2623 */         List localList2 = localType1.allparams();
/* 2624 */         if (!this.types.containsTypeEquivalent(localList1, localList2)) {
/* 2625 */           this.log.error(paramDiagnosticPosition, "cant.inherit.diff.arg", new Object[] { localType1.tsym, 
/* 2626 */             Type.toString(localList1), 
/* 2627 */             Type.toString(localList2) });
/*      */         }
/*      */       }
/* 2629 */       checkClassBounds(paramDiagnosticPosition, paramMap, localType1);
/*      */     }
/* 2631 */     localObject = this.types.supertype(paramType);
/* 2632 */     if (localObject != Type.noType) checkClassBounds(paramDiagnosticPosition, paramMap, (Type)localObject);
/*      */   }
/*      */ 
/*      */   void checkNotRepeated(JCDiagnostic.DiagnosticPosition paramDiagnosticPosition, Type paramType, Set<Type> paramSet)
/*      */   {
/* 2639 */     if (paramSet.contains(paramType))
/* 2640 */       this.log.error(paramDiagnosticPosition, "repeated.interface", new Object[0]);
/*      */     else
/* 2642 */       paramSet.add(paramType);
/*      */   }
/*      */ 
/*      */   void validateAnnotationTree(JCTree paramJCTree)
/*      */   {
/* 2663 */     paramJCTree.accept(new TreeScanner()
/*      */     {
/*      */       public void visitAnnotation(JCTree.JCAnnotation paramAnonymousJCAnnotation)
/*      */       {
/* 2657 */         if (!paramAnonymousJCAnnotation.type.isErroneous()) {
/* 2658 */           super.visitAnnotation(paramAnonymousJCAnnotation);
/* 2659 */           Check.this.validateAnnotation(paramAnonymousJCAnnotation);
/*      */         }
/*      */       }
/*      */     });
/*      */   }
/*      */ 
/*      */   void validateAnnotationType(JCTree paramJCTree)
/*      */   {
/* 2675 */     if (paramJCTree != null)
/* 2676 */       validateAnnotationType(paramJCTree.pos(), paramJCTree.type);
/*      */   }
/*      */ 
/*      */   void validateAnnotationType(JCDiagnostic.DiagnosticPosition paramDiagnosticPosition, Type paramType)
/*      */   {
/* 2681 */     if (paramType.isPrimitive()) return;
/* 2682 */     if (this.types.isSameType(paramType, this.syms.stringType)) return;
/* 2683 */     if ((paramType.tsym.flags() & 0x4000) != 0L) return;
/* 2684 */     if ((paramType.tsym.flags() & 0x2000) != 0L) return;
/* 2685 */     if (this.types.cvarLowerBound(paramType).tsym == this.syms.classType.tsym) return;
/* 2686 */     if ((this.types.isArray(paramType)) && (!this.types.isArray(this.types.elemtype(paramType)))) {
/* 2687 */       validateAnnotationType(paramDiagnosticPosition, this.types.elemtype(paramType));
/* 2688 */       return;
/*      */     }
/* 2690 */     this.log.error(paramDiagnosticPosition, "invalid.annotation.member.type", new Object[0]);
/*      */   }
/*      */ 
/*      */   void validateAnnotationMethod(JCDiagnostic.DiagnosticPosition paramDiagnosticPosition, Symbol.MethodSymbol paramMethodSymbol)
/*      */   {
/* 2702 */     for (Type localType = this.syms.annotationType; localType.hasTag(TypeTag.CLASS); localType = this.types.supertype(localType)) {
/* 2703 */       Scope localScope = localType.tsym.members();
/* 2704 */       for (Scope.Entry localEntry = localScope.lookup(paramMethodSymbol.name); localEntry.scope != null; localEntry = localEntry.next())
/* 2705 */         if ((localEntry.sym.kind == 16) && 
/* 2706 */           ((localEntry.sym
/* 2706 */           .flags() & 0x5) != 0L) && 
/* 2707 */           (this.types
/* 2707 */           .overrideEquivalent(paramMethodSymbol.type, localEntry.sym.type)))
/*      */         {
/* 2708 */           this.log.error(paramDiagnosticPosition, "intf.annotation.member.clash", new Object[] { localEntry.sym, localType });
/*      */         }
/*      */     }
/*      */   }
/*      */ 
/*      */   public void validateAnnotations(List<JCTree.JCAnnotation> paramList, Symbol paramSymbol)
/*      */   {
/* 2716 */     for (JCTree.JCAnnotation localJCAnnotation : paramList)
/* 2717 */       validateAnnotation(localJCAnnotation, paramSymbol);
/*      */   }
/*      */ 
/*      */   public void validateTypeAnnotations(List<JCTree.JCAnnotation> paramList, boolean paramBoolean)
/*      */   {
/* 2723 */     for (JCTree.JCAnnotation localJCAnnotation : paramList)
/* 2724 */       validateTypeAnnotation(localJCAnnotation, paramBoolean);
/*      */   }
/*      */ 
/*      */   private void validateAnnotation(JCTree.JCAnnotation paramJCAnnotation, Symbol paramSymbol)
/*      */   {
/* 2730 */     validateAnnotationTree(paramJCAnnotation);
/*      */ 
/* 2732 */     if (!annotationApplicable(paramJCAnnotation, paramSymbol)) {
/* 2733 */       this.log.error(paramJCAnnotation.pos(), "annotation.type.not.applicable", new Object[0]);
/*      */     }
/* 2735 */     if (paramJCAnnotation.annotationType.type.tsym == this.syms.functionalInterfaceType.tsym)
/* 2736 */       if (paramSymbol.kind != 2)
/* 2737 */         this.log.error(paramJCAnnotation.pos(), "bad.functional.intf.anno", new Object[0]);
/* 2738 */       else if ((!paramSymbol.isInterface()) || ((paramSymbol.flags() & 0x2000) != 0L))
/* 2739 */         this.log.error(paramJCAnnotation.pos(), "bad.functional.intf.anno.1", new Object[] { this.diags.fragment("not.a.functional.intf", new Object[] { paramSymbol }) });
/*      */   }
/*      */ 
/*      */   public void validateTypeAnnotation(JCTree.JCAnnotation paramJCAnnotation, boolean paramBoolean)
/*      */   {
/* 2745 */     Assert.checkNonNull(paramJCAnnotation.type, "annotation tree hasn't been attributed yet: " + paramJCAnnotation);
/* 2746 */     validateAnnotationTree(paramJCAnnotation);
/*      */ 
/* 2748 */     if ((paramJCAnnotation.hasTag(JCTree.Tag.TYPE_ANNOTATION)) && 
/* 2749 */       (!paramJCAnnotation.annotationType.type
/* 2749 */       .isErroneous()) && 
/* 2750 */       (!isTypeAnnotation(paramJCAnnotation, paramBoolean)))
/*      */     {
/* 2751 */       this.log.error(paramJCAnnotation.pos(), "annotation.type.not.applicable", new Object[0]);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void validateRepeatable(Symbol.TypeSymbol paramTypeSymbol, Attribute.Compound paramCompound, JCDiagnostic.DiagnosticPosition paramDiagnosticPosition)
/*      */   {
/* 2765 */     Assert.check(this.types.isSameType(paramCompound.type, this.syms.repeatableType));
/*      */ 
/* 2767 */     Type localType = null;
/* 2768 */     List localList = paramCompound.values;
/* 2769 */     if (!localList.isEmpty()) {
/* 2770 */       Assert.check(((Symbol.MethodSymbol)((Pair)localList.head).fst).name == this.names.value);
/* 2771 */       localType = ((Attribute.Class)((Pair)localList.head).snd).getValue();
/*      */     }
/*      */ 
/* 2774 */     if (localType == null)
/*      */     {
/* 2776 */       return;
/*      */     }
/*      */ 
/* 2779 */     validateValue(localType.tsym, paramTypeSymbol, paramDiagnosticPosition);
/* 2780 */     validateRetention(localType.tsym, paramTypeSymbol, paramDiagnosticPosition);
/* 2781 */     validateDocumented(localType.tsym, paramTypeSymbol, paramDiagnosticPosition);
/* 2782 */     validateInherited(localType.tsym, paramTypeSymbol, paramDiagnosticPosition);
/* 2783 */     validateTarget(localType.tsym, paramTypeSymbol, paramDiagnosticPosition);
/* 2784 */     validateDefault(localType.tsym, paramDiagnosticPosition);
/*      */   }
/*      */ 
/*      */   private void validateValue(Symbol.TypeSymbol paramTypeSymbol1, Symbol.TypeSymbol paramTypeSymbol2, JCDiagnostic.DiagnosticPosition paramDiagnosticPosition) {
/* 2788 */     Scope.Entry localEntry = paramTypeSymbol1.members().lookup(this.names.value);
/* 2789 */     if ((localEntry.scope != null) && (localEntry.sym.kind == 16)) {
/* 2790 */       Symbol.MethodSymbol localMethodSymbol = (Symbol.MethodSymbol)localEntry.sym;
/* 2791 */       Type localType = localMethodSymbol.getReturnType();
/* 2792 */       if ((!localType.hasTag(TypeTag.ARRAY)) || (!this.types.isSameType(((Type.ArrayType)localType).elemtype, paramTypeSymbol2.type)))
/* 2793 */         this.log.error(paramDiagnosticPosition, "invalid.repeatable.annotation.value.return", new Object[] { paramTypeSymbol1, localType, this.types
/* 2794 */           .makeArrayType(paramTypeSymbol2.type) });
/*      */     }
/*      */     else
/*      */     {
/* 2797 */       this.log.error(paramDiagnosticPosition, "invalid.repeatable.annotation.no.value", new Object[] { paramTypeSymbol1 });
/*      */     }
/*      */   }
/*      */ 
/*      */   private void validateRetention(Symbol paramSymbol1, Symbol paramSymbol2, JCDiagnostic.DiagnosticPosition paramDiagnosticPosition) {
/* 2802 */     Attribute.RetentionPolicy localRetentionPolicy1 = this.types.getRetention(paramSymbol1);
/* 2803 */     Attribute.RetentionPolicy localRetentionPolicy2 = this.types.getRetention(paramSymbol2);
/*      */ 
/* 2805 */     int i = 0;
/* 2806 */     switch (localRetentionPolicy2) {
/*      */     case RUNTIME:
/* 2808 */       if (localRetentionPolicy1 != Attribute.RetentionPolicy.RUNTIME)
/* 2809 */         i = 1; break;
/*      */     case CLASS:
/* 2813 */       if (localRetentionPolicy1 == Attribute.RetentionPolicy.SOURCE)
/* 2814 */         i = 1;
/*      */       break;
/*      */     }
/* 2817 */     if (i != 0)
/* 2818 */       this.log.error(paramDiagnosticPosition, "invalid.repeatable.annotation.retention", new Object[] { paramSymbol1, localRetentionPolicy1, paramSymbol2, localRetentionPolicy2 });
/*      */   }
/*      */ 
/*      */   private void validateDocumented(Symbol paramSymbol1, Symbol paramSymbol2, JCDiagnostic.DiagnosticPosition paramDiagnosticPosition)
/*      */   {
/* 2825 */     if ((paramSymbol2.attribute(this.syms.documentedType.tsym) != null) && 
/* 2826 */       (paramSymbol1.attribute(this.syms.documentedType.tsym) == null))
/* 2827 */       this.log.error(paramDiagnosticPosition, "invalid.repeatable.annotation.not.documented", new Object[] { paramSymbol1, paramSymbol2 });
/*      */   }
/*      */ 
/*      */   private void validateInherited(Symbol paramSymbol1, Symbol paramSymbol2, JCDiagnostic.DiagnosticPosition paramDiagnosticPosition)
/*      */   {
/* 2833 */     if ((paramSymbol2.attribute(this.syms.inheritedType.tsym) != null) && 
/* 2834 */       (paramSymbol1.attribute(this.syms.inheritedType.tsym) == null))
/* 2835 */       this.log.error(paramDiagnosticPosition, "invalid.repeatable.annotation.not.inherited", new Object[] { paramSymbol1, paramSymbol2 });
/*      */   }
/*      */ 
/*      */   private void validateTarget(Symbol paramSymbol1, Symbol paramSymbol2, JCDiagnostic.DiagnosticPosition paramDiagnosticPosition)
/*      */   {
/* 2847 */     Attribute.Array localArray1 = getAttributeTargetAttribute(paramSymbol1);
/*      */     Object localObject1;
/* 2848 */     if (localArray1 == null) {
/* 2849 */       localObject1 = getDefaultTargetSet();
/*      */     } else {
/* 2851 */       localObject1 = new HashSet();
/* 2852 */       for (Object localObject3 : localArray1.values) {
/* 2853 */         if ((localObject3 instanceof Attribute.Enum))
/*      */         {
/* 2856 */           Attribute.Enum localEnum1 = (Attribute.Enum)localObject3;
/* 2857 */           ((Set)localObject1).add(localEnum1.value.name);
/*      */         }
/*      */       }
/*      */     }
/*      */ 
/* 2862 */     Attribute.Array localArray2 = getAttributeTargetAttribute(paramSymbol2);
/* 2863 */     if (localArray2 == null) {
/* 2864 */       ??? = getDefaultTargetSet();
/*      */     } else {
/* 2866 */       ??? = new HashSet();
/* 2867 */       for (Attribute localAttribute : localArray2.values) {
/* 2868 */         if ((localAttribute instanceof Attribute.Enum))
/*      */         {
/* 2871 */           Attribute.Enum localEnum2 = (Attribute.Enum)localAttribute;
/* 2872 */           ((Set)???).add(localEnum2.value.name);
/*      */         }
/*      */       }
/*      */     }
/* 2876 */     if (!isTargetSubsetOf((Set)localObject1, (Set)???))
/* 2877 */       this.log.error(paramDiagnosticPosition, "invalid.repeatable.annotation.incompatible.target", new Object[] { paramSymbol1, paramSymbol2 });
/*      */   }
/*      */ 
/*      */   private Set<Name> getDefaultTargetSet()
/*      */   {
/* 2883 */     if (this.defaultTargets == null) {
/* 2884 */       HashSet localHashSet = new HashSet();
/* 2885 */       localHashSet.add(this.names.ANNOTATION_TYPE);
/* 2886 */       localHashSet.add(this.names.CONSTRUCTOR);
/* 2887 */       localHashSet.add(this.names.FIELD);
/* 2888 */       localHashSet.add(this.names.LOCAL_VARIABLE);
/* 2889 */       localHashSet.add(this.names.METHOD);
/* 2890 */       localHashSet.add(this.names.PACKAGE);
/* 2891 */       localHashSet.add(this.names.PARAMETER);
/* 2892 */       localHashSet.add(this.names.TYPE);
/*      */ 
/* 2894 */       this.defaultTargets = Collections.unmodifiableSet(localHashSet);
/*      */     }
/*      */ 
/* 2897 */     return this.defaultTargets;
/*      */   }
/*      */ 
/*      */   private boolean isTargetSubsetOf(Set<Name> paramSet1, Set<Name> paramSet2)
/*      */   {
/* 2909 */     for (Name localName1 : paramSet1) {
/* 2910 */       int i = 0;
/* 2911 */       for (Name localName2 : paramSet2) {
/* 2912 */         if (localName2 == localName1) {
/* 2913 */           i = 1;
/* 2914 */           break;
/* 2915 */         }if ((localName2 == this.names.TYPE) && (localName1 == this.names.ANNOTATION_TYPE)) {
/* 2916 */           i = 1;
/* 2917 */           break;
/* 2918 */         }if ((localName2 == this.names.TYPE_USE) && ((localName1 == this.names.TYPE) || (localName1 == this.names.ANNOTATION_TYPE) || (localName1 == this.names.TYPE_PARAMETER)))
/*      */         {
/* 2922 */           i = 1;
/* 2923 */           break;
/*      */         }
/*      */       }
/* 2926 */       if (i == 0)
/* 2927 */         return false;
/*      */     }
/* 2929 */     return true;
/*      */   }
/*      */ 
/*      */   private void validateDefault(Symbol paramSymbol, JCDiagnostic.DiagnosticPosition paramDiagnosticPosition)
/*      */   {
/* 2934 */     Scope localScope = paramSymbol.members();
/* 2935 */     for (Symbol localSymbol : localScope.getElements())
/* 2936 */       if ((localSymbol.name != this.names.value) && (localSymbol.kind == 16) && (((Symbol.MethodSymbol)localSymbol).defaultValue == null))
/*      */       {
/* 2939 */         this.log.error(paramDiagnosticPosition, "invalid.repeatable.annotation.elem.nondefault", new Object[] { paramSymbol, localSymbol });
/*      */       }
/*      */   }
/*      */ 
/*      */   boolean isOverrider(Symbol paramSymbol)
/*      */   {
/* 2949 */     if ((paramSymbol.kind != 16) || (paramSymbol.isStatic()))
/* 2950 */       return false;
/* 2951 */     Symbol.MethodSymbol localMethodSymbol = (Symbol.MethodSymbol)paramSymbol;
/* 2952 */     Symbol.TypeSymbol localTypeSymbol = (Symbol.TypeSymbol)localMethodSymbol.owner;
/* 2953 */     for (Type localType : this.types.closure(localTypeSymbol.type)) {
/* 2954 */       if (localType != localTypeSymbol.type)
/*      */       {
/* 2956 */         Scope localScope = localType.tsym.members();
/* 2957 */         for (Scope.Entry localEntry = localScope.lookup(localMethodSymbol.name); localEntry.scope != null; localEntry = localEntry.next())
/* 2958 */           if ((!localEntry.sym.isStatic()) && (localMethodSymbol.overrides(localEntry.sym, localTypeSymbol, this.types, true)))
/* 2959 */             return true;
/*      */       }
/*      */     }
/* 2962 */     return false;
/*      */   }
/*      */ 
/*      */   protected boolean isTypeAnnotation(JCTree.JCAnnotation paramJCAnnotation, boolean paramBoolean)
/*      */   {
/* 2968 */     Attribute.Compound localCompound = paramJCAnnotation.annotationType.type.tsym
/* 2968 */       .attribute(this.syms.annotationTargetType.tsym);
/*      */ 
/* 2969 */     if (localCompound == null)
/*      */     {
/* 2971 */       return false;
/*      */     }
/*      */ 
/* 2974 */     Attribute localAttribute1 = localCompound.member(this.names.value);
/* 2975 */     if (!(localAttribute1 instanceof Attribute.Array)) {
/* 2976 */       return false;
/*      */     }
/*      */ 
/* 2979 */     Attribute.Array localArray = (Attribute.Array)localAttribute1;
/* 2980 */     for (Attribute localAttribute2 : localArray.values) {
/* 2981 */       if (!(localAttribute2 instanceof Attribute.Enum)) {
/* 2982 */         return false;
/*      */       }
/* 2984 */       Attribute.Enum localEnum = (Attribute.Enum)localAttribute2;
/*      */ 
/* 2986 */       if (localEnum.value.name == this.names.TYPE_USE)
/* 2987 */         return true;
/* 2988 */       if ((paramBoolean) && (localEnum.value.name == this.names.TYPE_PARAMETER))
/* 2989 */         return true;
/*      */     }
/* 2991 */     return false;
/*      */   }
/*      */ 
/*      */   boolean annotationApplicable(JCTree.JCAnnotation paramJCAnnotation, Symbol paramSymbol)
/*      */   {
/* 2996 */     Attribute.Array localArray = getAttributeTargetAttribute(paramJCAnnotation.annotationType.type.tsym);
/*      */     Name[] arrayOfName1;
/* 2999 */     if (localArray == null) {
/* 3000 */       arrayOfName1 = defaultTargetMetaInfo(paramJCAnnotation, paramSymbol);
/*      */     }
/*      */     else {
/* 3003 */       arrayOfName1 = new Name[localArray.values.length];
/* 3004 */       for (int i = 0; i < localArray.values.length; i++) {
/* 3005 */         Attribute localAttribute = localArray.values[i];
/* 3006 */         if (!(localAttribute instanceof Attribute.Enum)) {
/* 3007 */           return true;
/*      */         }
/* 3009 */         Attribute.Enum localEnum = (Attribute.Enum)localAttribute;
/* 3010 */         arrayOfName1[i] = localEnum.value.name;
/*      */       }
/*      */     }
/* 3013 */     for (Name localName : arrayOfName1) {
/* 3014 */       if (localName == this.names.TYPE) {
/* 3015 */         if (paramSymbol.kind == 2) return true; 
/*      */       }
/* 3016 */       else if (localName == this.names.FIELD) {
/* 3017 */         if ((paramSymbol.kind == 4) && (paramSymbol.owner.kind != 16)) return true; 
/*      */       }
/* 3018 */       else if (localName == this.names.METHOD) {
/* 3019 */         if ((paramSymbol.kind == 16) && (!paramSymbol.isConstructor())) return true; 
/*      */       }
/* 3020 */       else if (localName == this.names.PARAMETER) {
/* 3021 */         if ((paramSymbol.kind == 4) && (paramSymbol.owner.kind == 16))
/*      */         {
/* 3023 */           if ((paramSymbol
/* 3023 */             .flags() & 0x0) != 0L)
/* 3024 */             return true;
/*      */         }
/* 3026 */       } else if (localName == this.names.CONSTRUCTOR) {
/* 3027 */         if ((paramSymbol.kind == 16) && (paramSymbol.isConstructor())) return true; 
/*      */       }
/* 3028 */       else if (localName == this.names.LOCAL_VARIABLE) {
/* 3029 */         if ((paramSymbol.kind == 4) && (paramSymbol.owner.kind == 16) && 
/* 3030 */           ((paramSymbol
/* 3030 */           .flags() & 0x0) == 0L))
/* 3031 */           return true;
/*      */       }
/* 3033 */       else if (localName == this.names.ANNOTATION_TYPE) {
/* 3034 */         if ((paramSymbol.kind == 2) && ((paramSymbol.flags() & 0x2000) != 0L))
/* 3035 */           return true;
/*      */       }
/* 3037 */       else if (localName == this.names.PACKAGE) {
/* 3038 */         if (paramSymbol.kind == 1) return true; 
/*      */       }
/* 3039 */       else if (localName == this.names.TYPE_USE) {
/* 3040 */         if ((paramSymbol.kind == 2) || (paramSymbol.kind == 4) || ((paramSymbol.kind == 16) && 
/* 3042 */           (!paramSymbol
/* 3042 */           .isConstructor()) && 
/* 3043 */           (!paramSymbol.type
/* 3043 */           .getReturnType().hasTag(TypeTag.VOID))) || (
/* 3043 */           (paramSymbol.kind == 16) && 
/* 3044 */           (paramSymbol
/* 3044 */           .isConstructor())))
/* 3045 */           return true;
/*      */       }
/* 3047 */       else if (localName == this.names.TYPE_PARAMETER) {
/* 3048 */         if ((paramSymbol.kind == 2) && (paramSymbol.type.hasTag(TypeTag.TYPEVAR)))
/* 3049 */           return true;
/*      */       }
/*      */       else
/* 3052 */         return true;
/*      */     }
/* 3054 */     return false;
/*      */   }
/*      */ 
/*      */   Attribute.Array getAttributeTargetAttribute(Symbol paramSymbol)
/*      */   {
/* 3060 */     Attribute.Compound localCompound = paramSymbol
/* 3060 */       .attribute(this.syms.annotationTargetType.tsym);
/*      */ 
/* 3061 */     if (localCompound == null) return null;
/* 3062 */     Attribute localAttribute = localCompound.member(this.names.value);
/* 3063 */     if (!(localAttribute instanceof Attribute.Array)) return null;
/* 3064 */     return (Attribute.Array)localAttribute;
/*      */   }
/*      */ 
/*      */   private Name[] defaultTargetMetaInfo(JCTree.JCAnnotation paramJCAnnotation, Symbol paramSymbol)
/*      */   {
/* 3069 */     return this.dfltTargetMeta;
/*      */   }
/*      */ 
/*      */   public boolean validateAnnotationDeferErrors(JCTree.JCAnnotation paramJCAnnotation)
/*      */   {
/* 3078 */     boolean bool = false;
/* 3079 */     Log.DiscardDiagnosticHandler localDiscardDiagnosticHandler = new Log.DiscardDiagnosticHandler(this.log);
/*      */     try {
/* 3081 */       bool = validateAnnotation(paramJCAnnotation);
/*      */     } finally {
/* 3083 */       this.log.popDiagnosticHandler(localDiscardDiagnosticHandler);
/*      */     }
/* 3085 */     return bool;
/*      */   }
/*      */ 
/*      */   private boolean validateAnnotation(JCTree.JCAnnotation paramJCAnnotation) {
/* 3089 */     boolean bool = true;
/*      */ 
/* 3091 */     LinkedHashSet localLinkedHashSet = new LinkedHashSet();
/* 3092 */     for (Object localObject1 = paramJCAnnotation.annotationType.type.tsym.members().elems; 
/* 3093 */       localObject1 != null; 
/* 3094 */       localObject1 = ((Scope.Entry)localObject1).sibling) {
/* 3095 */       if ((((Scope.Entry)localObject1).sym.kind == 16) && (((Scope.Entry)localObject1).sym.name != this.names.clinit) && 
/* 3096 */         ((((Scope.Entry)localObject1).sym
/* 3096 */         .flags() & 0x1000) == 0L)) {
/* 3097 */         localLinkedHashSet.add((Symbol.MethodSymbol)((Scope.Entry)localObject1).sym);
/*      */       }
/*      */     }
/* 3100 */     for (localObject1 = paramJCAnnotation.args.iterator(); ((Iterator)localObject1).hasNext(); ) { localObject2 = (JCTree)((Iterator)localObject1).next();
/* 3101 */       if (((JCTree)localObject2).hasTag(JCTree.Tag.ASSIGN)) {
/* 3102 */         localObject3 = (JCTree.JCAssign)localObject2;
/* 3103 */         localObject4 = TreeInfo.symbol(((JCTree.JCAssign)localObject3).lhs);
/* 3104 */         if ((localObject4 != null) && (!((Symbol)localObject4).type.isErroneous())) {
/* 3105 */           if (!localLinkedHashSet.remove(localObject4)) {
/* 3106 */             bool = false;
/* 3107 */             this.log.error(((JCTree.JCAssign)localObject3).lhs.pos(), "duplicate.annotation.member.value", new Object[] { ((Symbol)localObject4).name, paramJCAnnotation.type });
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/*      */ 
/* 3113 */     localObject1 = List.nil();
/* 3114 */     for (Object localObject2 = localLinkedHashSet.iterator(); ((Iterator)localObject2).hasNext(); ) { localObject3 = (Symbol.MethodSymbol)((Iterator)localObject2).next();
/* 3115 */       if ((((Symbol.MethodSymbol)localObject3).defaultValue == null) && (!((Symbol.MethodSymbol)localObject3).type.isErroneous())) {
/* 3116 */         localObject1 = ((List)localObject1).append(((Symbol.MethodSymbol)localObject3).name);
/*      */       }
/*      */     }
/* 3119 */     localObject1 = ((List)localObject1).reverse();
/* 3120 */     if (((List)localObject1).nonEmpty()) {
/* 3121 */       bool = false;
/* 3122 */       localObject2 = ((List)localObject1).size() > 1 ? "annotation.missing.default.value.1" : "annotation.missing.default.value";
/*      */ 
/* 3125 */       this.log.error(paramJCAnnotation.pos(), (String)localObject2, new Object[] { paramJCAnnotation.type, localObject1 });
/*      */     }
/*      */ 
/* 3130 */     if ((paramJCAnnotation.annotationType.type.tsym != this.syms.annotationTargetType.tsym) || (paramJCAnnotation.args.tail == null))
/*      */     {
/* 3132 */       return bool;
/*      */     }
/* 3134 */     if (!((JCTree.JCExpression)paramJCAnnotation.args.head).hasTag(JCTree.Tag.ASSIGN)) return false;
/* 3135 */     localObject2 = (JCTree.JCAssign)paramJCAnnotation.args.head;
/* 3136 */     Object localObject3 = TreeInfo.symbol(((JCTree.JCAssign)localObject2).lhs);
/* 3137 */     if (((Symbol)localObject3).name != this.names.value) return false;
/* 3138 */     Object localObject4 = ((JCTree.JCAssign)localObject2).rhs;
/* 3139 */     if (!((JCTree)localObject4).hasTag(JCTree.Tag.NEWARRAY)) return false;
/* 3140 */     JCTree.JCNewArray localJCNewArray = (JCTree.JCNewArray)localObject4;
/* 3141 */     HashSet localHashSet = new HashSet();
/* 3142 */     for (JCTree localJCTree : localJCNewArray.elems) {
/* 3143 */       if (!localHashSet.add(TreeInfo.symbol(localJCTree))) {
/* 3144 */         bool = false;
/* 3145 */         this.log.error(localJCTree.pos(), "repeated.annotation.target", new Object[0]);
/*      */       }
/*      */     }
/* 3148 */     return bool;
/*      */   }
/*      */ 
/*      */   void checkDeprecatedAnnotation(JCDiagnostic.DiagnosticPosition paramDiagnosticPosition, Symbol paramSymbol) {
/* 3152 */     if ((this.allowAnnotations) && 
/* 3153 */       (this.lint
/* 3153 */       .isEnabled(Lint.LintCategory.DEP_ANN)) && 
/* 3154 */       ((paramSymbol
/* 3154 */       .flags() & 0x20000) != 0L) && 
/* 3155 */       (!this.syms.deprecatedType
/* 3155 */       .isErroneous()) && 
/* 3156 */       (paramSymbol
/* 3156 */       .attribute(this.syms.deprecatedType.tsym) == null))
/*      */     {
/* 3157 */       this.log.warning(Lint.LintCategory.DEP_ANN, paramDiagnosticPosition, "missing.deprecated.annotation", new Object[0]);
/*      */     }
/*      */   }
/*      */ 
/*      */   void checkDeprecated(final JCDiagnostic.DiagnosticPosition paramDiagnosticPosition, Symbol paramSymbol1, final Symbol paramSymbol2)
/*      */   {
/* 3163 */     if (((paramSymbol2.flags() & 0x20000) != 0L) && 
/* 3164 */       ((paramSymbol1
/* 3164 */       .flags() & 0x20000) == 0L) && 
/* 3165 */       (paramSymbol2
/* 3165 */       .outermostClass() != paramSymbol1.outermostClass()))
/* 3166 */       this.deferredLintHandler.report(new DeferredLintHandler.LintLogger()
/*      */       {
/*      */         public void report() {
/* 3169 */           Check.this.warnDeprecated(paramDiagnosticPosition, paramSymbol2);
/*      */         }
/*      */       });
/*      */   }
/*      */ 
/*      */   void checkSunAPI(final JCDiagnostic.DiagnosticPosition paramDiagnosticPosition, final Symbol paramSymbol)
/*      */   {
/* 3176 */     if ((paramSymbol.flags() & 0x0) != 0L)
/* 3177 */       this.deferredLintHandler.report(new DeferredLintHandler.LintLogger() {
/*      */         public void report() {
/* 3179 */           if (Check.this.enableSunApiLintControl)
/* 3180 */             Check.this.warnSunApi(paramDiagnosticPosition, "sun.proprietary", new Object[] { paramSymbol });
/*      */           else
/* 3182 */             Check.this.log.mandatoryWarning(paramDiagnosticPosition, "sun.proprietary", new Object[] { paramSymbol });
/*      */         }
/*      */       });
/*      */   }
/*      */ 
/*      */   void checkProfile(JCDiagnostic.DiagnosticPosition paramDiagnosticPosition, Symbol paramSymbol)
/*      */   {
/* 3189 */     if ((this.profile != Profile.DEFAULT) && ((paramSymbol.flags() & 0x0) != 0L))
/* 3190 */       this.log.error(paramDiagnosticPosition, "not.in.profile", new Object[] { paramSymbol, this.profile });
/*      */   }
/*      */ 
/*      */   void checkNonCyclicElements(JCTree.JCClassDecl paramJCClassDecl)
/*      */   {
/* 3201 */     if ((paramJCClassDecl.sym.flags_field & 0x2000) == 0L) return;
/* 3202 */     Assert.check((paramJCClassDecl.sym.flags_field & 0x8000000) == 0L);
/*      */     try {
/* 3204 */       paramJCClassDecl.sym.flags_field |= 134217728L;
/* 3205 */       for (JCTree localJCTree : paramJCClassDecl.defs)
/* 3206 */         if (localJCTree.hasTag(JCTree.Tag.METHODDEF)) {
/* 3207 */           JCTree.JCMethodDecl localJCMethodDecl = (JCTree.JCMethodDecl)localJCTree;
/* 3208 */           checkAnnotationResType(localJCMethodDecl.pos(), localJCMethodDecl.restype.type);
/*      */         }
/*      */     } finally {
/* 3211 */       paramJCClassDecl.sym.flags_field &= -134217729L;
/* 3212 */       paramJCClassDecl.sym.flags_field |= 34359738368L;
/*      */     }
/*      */   }
/*      */ 
/*      */   void checkNonCyclicElementsInternal(JCDiagnostic.DiagnosticPosition paramDiagnosticPosition, Symbol.TypeSymbol paramTypeSymbol) {
/* 3217 */     if ((paramTypeSymbol.flags_field & 0x0) != 0L)
/* 3218 */       return;
/* 3219 */     if ((paramTypeSymbol.flags_field & 0x8000000) != 0L) {
/* 3220 */       this.log.error(paramDiagnosticPosition, "cyclic.annotation.element", new Object[0]);
/* 3221 */       return;
/*      */     }
/*      */     try {
/* 3224 */       paramTypeSymbol.flags_field |= 134217728L;
/* 3225 */       for (Scope.Entry localEntry = paramTypeSymbol.members().elems; localEntry != null; localEntry = localEntry.sibling) {
/* 3226 */         Symbol localSymbol = localEntry.sym;
/* 3227 */         if (localSymbol.kind == 16)
/*      */         {
/* 3229 */           checkAnnotationResType(paramDiagnosticPosition, ((Symbol.MethodSymbol)localSymbol).type.getReturnType());
/*      */         }
/*      */       }
/*      */     } finally { paramTypeSymbol.flags_field &= -134217729L;
/* 3233 */       paramTypeSymbol.flags_field |= 34359738368L; }
/*      */   }
/*      */ 
/*      */   void checkAnnotationResType(JCDiagnostic.DiagnosticPosition paramDiagnosticPosition, Type paramType)
/*      */   {
/* 3238 */     switch (paramType.getTag()) {
/*      */     case CLASS:
/* 3240 */       if ((paramType.tsym.flags() & 0x2000) != 0L)
/* 3241 */         checkNonCyclicElementsInternal(paramDiagnosticPosition, paramType.tsym); break;
/*      */     case ARRAY:
/* 3244 */       checkAnnotationResType(paramDiagnosticPosition, this.types.elemtype(paramType));
/* 3245 */       break;
/*      */     }
/*      */   }
/*      */ 
/*      */   void checkCyclicConstructors(JCTree.JCClassDecl paramJCClassDecl)
/*      */   {
/* 3259 */     HashMap localHashMap = new HashMap();
/*      */     Object localObject2;
/* 3262 */     for (Object localObject1 = paramJCClassDecl.defs; ((List)localObject1).nonEmpty(); localObject1 = ((List)localObject1).tail) {
/* 3263 */       localObject2 = TreeInfo.firstConstructorCall((JCTree)((List)localObject1).head);
/* 3264 */       if (localObject2 != null) {
/* 3265 */         JCTree.JCMethodDecl localJCMethodDecl = (JCTree.JCMethodDecl)((List)localObject1).head;
/* 3266 */         if (TreeInfo.name(((JCTree.JCMethodInvocation)localObject2).meth) == this.names._this)
/* 3267 */           localHashMap.put(localJCMethodDecl.sym, TreeInfo.symbol(((JCTree.JCMethodInvocation)localObject2).meth));
/*      */         else {
/* 3269 */           localJCMethodDecl.sym.flags_field |= 1073741824L;
/*      */         }
/*      */       }
/*      */     }
/*      */ 
/* 3274 */     localObject1 = new Symbol[0];
/* 3275 */     localObject1 = (Symbol[])localHashMap.keySet().toArray((Object[])localObject1);
/* 3276 */     for (Symbol localSymbol : localObject1)
/* 3277 */       checkCyclicConstructor(paramJCClassDecl, localSymbol, localHashMap);
/*      */   }
/*      */ 
/*      */   private void checkCyclicConstructor(JCTree.JCClassDecl paramJCClassDecl, Symbol paramSymbol, Map<Symbol, Symbol> paramMap)
/*      */   {
/* 3286 */     if ((paramSymbol != null) && ((paramSymbol.flags_field & 0x40000000) == 0L)) {
/* 3287 */       if ((paramSymbol.flags_field & 0x8000000) != 0L) {
/* 3288 */         this.log.error(TreeInfo.diagnosticPositionFor(paramSymbol, paramJCClassDecl), "recursive.ctor.invocation", new Object[0]);
/*      */       }
/*      */       else {
/* 3291 */         paramSymbol.flags_field |= 134217728L;
/* 3292 */         checkCyclicConstructor(paramJCClassDecl, (Symbol)paramMap.remove(paramSymbol), paramMap);
/* 3293 */         paramSymbol.flags_field &= -134217729L;
/*      */       }
/* 3295 */       paramSymbol.flags_field |= 1073741824L;
/*      */     }
/*      */   }
/*      */ 
/*      */   int checkOperator(JCDiagnostic.DiagnosticPosition paramDiagnosticPosition, Symbol.OperatorSymbol paramOperatorSymbol, JCTree.Tag paramTag, Type paramType1, Type paramType2)
/*      */   {
/* 3317 */     if (paramOperatorSymbol.opcode == 277) {
/* 3318 */       this.log.error(paramDiagnosticPosition, "operator.cant.be.applied.1", new Object[] { this.treeinfo
/* 3320 */         .operatorName(paramTag), 
/* 3320 */         paramType1, paramType2 });
/*      */     }
/*      */ 
/* 3323 */     return paramOperatorSymbol.opcode;
/*      */   }
/*      */ 
/*      */   void checkDivZero(JCDiagnostic.DiagnosticPosition paramDiagnosticPosition, Symbol paramSymbol, Type paramType)
/*      */   {
/* 3334 */     if ((paramType.constValue() != null) && 
/* 3335 */       (this.lint
/* 3335 */       .isEnabled(Lint.LintCategory.DIVZERO)) && 
/* 3336 */       (paramType
/* 3336 */       .getTag().isSubRangeOf(TypeTag.LONG)) && 
/* 3337 */       (((Number)paramType
/* 3337 */       .constValue()).longValue() == 0L)) {
/* 3338 */       int i = ((Symbol.OperatorSymbol)paramSymbol).opcode;
/* 3339 */       if ((i == 108) || (i == 112) || (i == 109) || (i == 113))
/*      */       {
/* 3341 */         this.log.warning(Lint.LintCategory.DIVZERO, paramDiagnosticPosition, "div.zero", new Object[0]);
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   void checkEmptyIf(JCTree.JCIf paramJCIf)
/*      */   {
/* 3350 */     if ((paramJCIf.thenpart.hasTag(JCTree.Tag.SKIP)) && (paramJCIf.elsepart == null) && 
/* 3351 */       (this.lint
/* 3351 */       .isEnabled(Lint.LintCategory.EMPTY)))
/*      */     {
/* 3352 */       this.log.warning(Lint.LintCategory.EMPTY, paramJCIf.thenpart.pos(), "empty.if", new Object[0]);
/*      */     }
/*      */   }
/*      */ 
/*      */   boolean checkUnique(JCDiagnostic.DiagnosticPosition paramDiagnosticPosition, Symbol paramSymbol, Scope paramScope)
/*      */   {
/* 3361 */     if (paramSymbol.type.isErroneous())
/* 3362 */       return true;
/* 3363 */     if (paramSymbol.owner.name == this.names.any) return false;
/* 3364 */     for (Scope.Entry localEntry = paramScope.lookup(paramSymbol.name); localEntry.scope == paramScope; localEntry = localEntry.next()) {
/* 3365 */       if ((paramSymbol != localEntry.sym) && 
/* 3366 */         ((localEntry.sym
/* 3366 */         .flags() & 0x0) == 0L) && (paramSymbol.kind == localEntry.sym.kind) && (paramSymbol.name != this.names.error) && (
/* 3366 */         (paramSymbol.kind != 16) || 
/* 3370 */         (this.types
/* 3370 */         .hasSameArgs(paramSymbol.type, localEntry.sym.type)) || 
/* 3371 */         (this.types
/* 3371 */         .hasSameArgs(this.types
/* 3371 */         .erasure(paramSymbol.type), 
/* 3371 */         this.types.erasure(localEntry.sym.type))))) {
/* 3372 */         if ((paramSymbol.flags() & 0x0) != (localEntry.sym.flags() & 0x0)) {
/* 3373 */           varargsDuplicateError(paramDiagnosticPosition, paramSymbol, localEntry.sym);
/* 3374 */           return true;
/* 3375 */         }if ((paramSymbol.kind == 16) && (!this.types.hasSameArgs(paramSymbol.type, localEntry.sym.type, false))) {
/* 3376 */           duplicateErasureError(paramDiagnosticPosition, paramSymbol, localEntry.sym);
/* 3377 */           paramSymbol.flags_field |= 4398046511104L;
/* 3378 */           return true;
/*      */         }
/* 3380 */         duplicateError(paramDiagnosticPosition, localEntry.sym);
/* 3381 */         return false;
/*      */       }
/*      */     }
/*      */ 
/* 3385 */     return true;
/*      */   }
/*      */ 
/*      */   void duplicateErasureError(JCDiagnostic.DiagnosticPosition paramDiagnosticPosition, Symbol paramSymbol1, Symbol paramSymbol2)
/*      */   {
/* 3391 */     if ((!paramSymbol1.type.isErroneous()) && (!paramSymbol2.type.isErroneous()))
/* 3392 */       this.log.error(paramDiagnosticPosition, "name.clash.same.erasure", new Object[] { paramSymbol1, paramSymbol2 });
/*      */   }
/*      */ 
/*      */   boolean checkUniqueImport(JCDiagnostic.DiagnosticPosition paramDiagnosticPosition, Symbol paramSymbol, Scope paramScope)
/*      */   {
/* 3403 */     return checkUniqueImport(paramDiagnosticPosition, paramSymbol, paramScope, false);
/*      */   }
/*      */ 
/*      */   boolean checkUniqueStaticImport(JCDiagnostic.DiagnosticPosition paramDiagnosticPosition, Symbol paramSymbol, Scope paramScope)
/*      */   {
/* 3413 */     return checkUniqueImport(paramDiagnosticPosition, paramSymbol, paramScope, true);
/*      */   }
/*      */ 
/*      */   private boolean checkUniqueImport(JCDiagnostic.DiagnosticPosition paramDiagnosticPosition, Symbol paramSymbol, Scope paramScope, boolean paramBoolean)
/*      */   {
/* 3424 */     for (Scope.Entry localEntry = paramScope.lookup(paramSymbol.name); localEntry.scope != null; localEntry = localEntry.next())
/*      */     {
/* 3426 */       int i = localEntry.scope == paramScope ? 1 : 0;
/* 3427 */       if (((i != 0) || (paramSymbol != localEntry.sym)) && (paramSymbol.kind == localEntry.sym.kind) && (paramSymbol.name != this.names.error) && ((!paramBoolean) || 
/* 3430 */         (!localEntry
/* 3430 */         .isStaticallyImported()))) {
/* 3431 */         if (!localEntry.sym.type.isErroneous()) {
/* 3432 */           if (i == 0) {
/* 3433 */             if (paramBoolean)
/* 3434 */               this.log.error(paramDiagnosticPosition, "already.defined.static.single.import", new Object[] { localEntry.sym });
/*      */             else
/* 3436 */               this.log.error(paramDiagnosticPosition, "already.defined.single.import", new Object[] { localEntry.sym });
/*      */           }
/* 3438 */           else if (paramSymbol != localEntry.sym)
/* 3439 */             this.log.error(paramDiagnosticPosition, "already.defined.this.unit", new Object[] { localEntry.sym });
/*      */         }
/* 3441 */         return false;
/*      */       }
/*      */     }
/* 3444 */     return true;
/*      */   }
/*      */ 
/*      */   public void checkCanonical(JCTree paramJCTree)
/*      */   {
/* 3450 */     if (!isCanonical(paramJCTree))
/* 3451 */       this.log.error(paramJCTree.pos(), "import.requires.canonical", new Object[] { 
/* 3452 */         TreeInfo.symbol(paramJCTree) });
/*      */   }
/*      */ 
/*      */   private boolean isCanonical(JCTree paramJCTree)
/*      */   {
/* 3456 */     while (paramJCTree.hasTag(JCTree.Tag.SELECT)) {
/* 3457 */       JCTree.JCFieldAccess localJCFieldAccess = (JCTree.JCFieldAccess)paramJCTree;
/* 3458 */       if (localJCFieldAccess.sym.owner != TreeInfo.symbol(localJCFieldAccess.selected))
/* 3459 */         return false;
/* 3460 */       paramJCTree = localJCFieldAccess.selected;
/*      */     }
/* 3462 */     return true;
/*      */   }
/*      */ 
/*      */   void checkForBadAuxiliaryClassAccess(JCDiagnostic.DiagnosticPosition paramDiagnosticPosition, Env<AttrContext> paramEnv, Symbol.ClassSymbol paramClassSymbol)
/*      */   {
/* 3468 */     if ((this.lint.isEnabled(Lint.LintCategory.AUXILIARYCLASS)) && 
/* 3469 */       ((paramClassSymbol
/* 3469 */       .flags() & 0x0) != 0L) && 
/* 3470 */       (this.rs
/* 3470 */       .isAccessible(paramEnv, paramClassSymbol)) && 
/* 3471 */       (!this.fileManager
/* 3471 */       .isSameFile(paramClassSymbol.sourcefile, paramEnv.toplevel.sourcefile)))
/*      */     {
/* 3473 */       this.log.warning(paramDiagnosticPosition, "auxiliary.class.accessed.from.outside.of.its.source.file", new Object[] { paramClassSymbol, paramClassSymbol.sourcefile });
/*      */     }
/*      */   }
/*      */ 
/*      */   public Warner castWarner(JCDiagnostic.DiagnosticPosition paramDiagnosticPosition, Type paramType1, Type paramType2)
/*      */   {
/* 3513 */     return new ConversionWarner(paramDiagnosticPosition, "unchecked.cast.to.type", paramType1, paramType2);
/*      */   }
/*      */ 
/*      */   public Warner convertWarner(JCDiagnostic.DiagnosticPosition paramDiagnosticPosition, Type paramType1, Type paramType2) {
/* 3517 */     return new ConversionWarner(paramDiagnosticPosition, "unchecked.assign", paramType1, paramType2);
/*      */   }
/*      */ 
/*      */   public void checkFunctionalInterface(JCTree.JCClassDecl paramJCClassDecl, Symbol.ClassSymbol paramClassSymbol) {
/* 3521 */     Attribute.Compound localCompound = paramClassSymbol.attribute(this.syms.functionalInterfaceType.tsym);
/*      */ 
/* 3523 */     if (localCompound != null)
/*      */       try {
/* 3525 */         this.types.findDescriptorSymbol(paramClassSymbol);
/*      */       } catch (Types.FunctionDescriptorLookupError localFunctionDescriptorLookupError) {
/* 3527 */         JCDiagnostic.DiagnosticPosition localDiagnosticPosition = paramJCClassDecl.pos();
/* 3528 */         for (JCTree.JCAnnotation localJCAnnotation : paramJCClassDecl.getModifiers().annotations) {
/* 3529 */           if (localJCAnnotation.annotationType.type.tsym == this.syms.functionalInterfaceType.tsym) {
/* 3530 */             localDiagnosticPosition = localJCAnnotation.pos();
/* 3531 */             break;
/*      */           }
/*      */         }
/* 3534 */         this.log.error(localDiagnosticPosition, "bad.functional.intf.anno.1", new Object[] { localFunctionDescriptorLookupError.getDiagnostic() });
/*      */       }
/*      */   }
/*      */ 
/*      */   public static abstract interface CheckContext
/*      */   {
/*      */     public abstract boolean compatible(Type paramType1, Type paramType2, Warner paramWarner);
/*      */ 
/*      */     public abstract void report(JCDiagnostic.DiagnosticPosition paramDiagnosticPosition, JCDiagnostic paramJCDiagnostic);
/*      */ 
/*      */     public abstract Warner checkWarner(JCDiagnostic.DiagnosticPosition paramDiagnosticPosition, Type paramType1, Type paramType2);
/*      */ 
/*      */     public abstract Infer.InferenceContext inferenceContext();
/*      */ 
/*      */     public abstract DeferredAttr.DeferredAttrContext deferredAttrContext();
/*      */   }
/*      */ 
/*      */   private class ClashFilter
/*      */     implements Filter<Symbol>
/*      */   {
/*      */     Type site;
/*      */ 
/*      */     ClashFilter(Type arg2)
/*      */     {
/*      */       Object localObject;
/* 2421 */       this.site = localObject;
/*      */     }
/*      */ 
/*      */     boolean shouldSkip(Symbol paramSymbol) {
/* 2425 */       return ((paramSymbol.flags() & 0x0) != 0L) && (paramSymbol.owner == this.site.tsym);
/*      */     }
/*      */ 
/*      */     public boolean accepts(Symbol paramSymbol)
/*      */     {
/* 2434 */       return (paramSymbol.kind == 16) && 
/* 2431 */         ((paramSymbol
/* 2431 */         .flags() & 0x1000) == 0L) && 
/* 2432 */         (!shouldSkip(paramSymbol)) && 
/* 2433 */         (paramSymbol
/* 2433 */         .isInheritedIn(this.site.tsym, Check.this.types)) && 
/* 2434 */         (!paramSymbol
/* 2434 */         .isConstructor());
/*      */     }
/*      */   }
/*      */ 
/*      */   private class ConversionWarner extends Warner
/*      */   {
/*      */     final String uncheckedKey;
/*      */     final Type found;
/*      */     final Type expected;
/*      */ 
/*      */     public ConversionWarner(JCDiagnostic.DiagnosticPosition paramString, String paramType1, Type paramType2, Type arg5)
/*      */     {
/* 3483 */       super();
/* 3484 */       this.uncheckedKey = paramType1;
/* 3485 */       this.found = paramType2;
/*      */       Object localObject;
/* 3486 */       this.expected = localObject;
/*      */     }
/*      */ 
/*      */     public void warn(Lint.LintCategory paramLintCategory)
/*      */     {
/* 3491 */       boolean bool = this.warned;
/* 3492 */       super.warn(paramLintCategory);
/* 3493 */       if (bool) return;
/* 3494 */       switch (Check.9.$SwitchMap$com$sun$tools$javac$code$Lint$LintCategory[paramLintCategory.ordinal()]) {
/*      */       case 1:
/* 3496 */         Check.this.warnUnchecked(pos(), "prob.found.req", new Object[] { Check.this.diags.fragment(this.uncheckedKey, new Object[0]), this.found, this.expected });
/* 3497 */         break;
/*      */       case 2:
/* 3499 */         if ((Check.this.method != null) && 
/* 3500 */           (Check.this.method
/* 3500 */           .attribute(Check.this.syms.trustMeType.tsym) != null) && 
/* 3501 */           (Check.this
/* 3501 */           .isTrustMeAllowedOnMethod(Check.this.method)) && 
/* 3502 */           (!Check.this.types
/* 3502 */           .isReifiable((Type)Check.this.method.type.getParameterTypes().last())))
/* 3503 */           Check.this.warnUnsafeVararg(pos(), "varargs.unsafe.use.varargs.param", new Object[] { Check.this.method.params.last() }); break;
/*      */       default:
/* 3507 */         throw new AssertionError("Unexpected lint: " + paramLintCategory);
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   class CycleChecker extends TreeScanner
/*      */   {
/* 2072 */     List<Symbol> seenClasses = List.nil();
/* 2073 */     boolean errorFound = false;
/* 2074 */     boolean partialCheck = false;
/*      */ 
/*      */     CycleChecker() {  } 
/* 2077 */     private void checkSymbol(JCDiagnostic.DiagnosticPosition paramDiagnosticPosition, Symbol paramSymbol) { if ((paramSymbol != null) && (paramSymbol.kind == 2)) {
/* 2078 */         Env localEnv = Check.this.enter.getEnv((Symbol.TypeSymbol)paramSymbol);
/* 2079 */         if (localEnv != null) {
/* 2080 */           DiagnosticSource localDiagnosticSource = Check.this.log.currentSource();
/*      */           try {
/* 2082 */             Check.this.log.useSource(localEnv.toplevel.sourcefile);
/* 2083 */             scan(localEnv.tree);
/*      */           }
/*      */           finally {
/* 2086 */             Check.this.log.useSource(localDiagnosticSource.getFile());
/*      */           }
/* 2088 */         } else if (paramSymbol.kind == 2) {
/* 2089 */           checkClass(paramDiagnosticPosition, paramSymbol, List.nil());
/*      */         }
/*      */       }
/*      */       else {
/* 2093 */         this.partialCheck = true;
/*      */       }
/*      */     }
/*      */ 
/*      */     public void visitSelect(JCTree.JCFieldAccess paramJCFieldAccess)
/*      */     {
/* 2099 */       super.visitSelect(paramJCFieldAccess);
/* 2100 */       checkSymbol(paramJCFieldAccess.pos(), paramJCFieldAccess.sym);
/*      */     }
/*      */ 
/*      */     public void visitIdent(JCTree.JCIdent paramJCIdent)
/*      */     {
/* 2105 */       checkSymbol(paramJCIdent.pos(), paramJCIdent.sym);
/*      */     }
/*      */ 
/*      */     public void visitTypeApply(JCTree.JCTypeApply paramJCTypeApply)
/*      */     {
/* 2110 */       scan(paramJCTypeApply.clazz);
/*      */     }
/*      */ 
/*      */     public void visitTypeArray(JCTree.JCArrayTypeTree paramJCArrayTypeTree)
/*      */     {
/* 2115 */       scan(paramJCArrayTypeTree.elemtype);
/*      */     }
/*      */ 
/*      */     public void visitClassDef(JCTree.JCClassDecl paramJCClassDecl)
/*      */     {
/* 2120 */       List localList = List.nil();
/* 2121 */       if (paramJCClassDecl.getExtendsClause() != null) {
/* 2122 */         localList = localList.prepend(paramJCClassDecl.getExtendsClause());
/*      */       }
/* 2124 */       if (paramJCClassDecl.getImplementsClause() != null) {
/* 2125 */         for (JCTree localJCTree : paramJCClassDecl.getImplementsClause()) {
/* 2126 */           localList = localList.prepend(localJCTree);
/*      */         }
/*      */       }
/* 2129 */       checkClass(paramJCClassDecl.pos(), paramJCClassDecl.sym, localList);
/*      */     }
/*      */ 
/*      */     void checkClass(JCDiagnostic.DiagnosticPosition paramDiagnosticPosition, Symbol paramSymbol, List<JCTree> paramList) {
/* 2133 */       if ((paramSymbol.flags_field & 0x40000000) != 0L)
/* 2134 */         return;
/* 2135 */       if (this.seenClasses.contains(paramSymbol)) {
/* 2136 */         this.errorFound = true;
/* 2137 */         Check.this.noteCyclic(paramDiagnosticPosition, (Symbol.ClassSymbol)paramSymbol);
/* 2138 */       } else if (!paramSymbol.type.isErroneous()) {
/*      */         try {
/* 2140 */           this.seenClasses = this.seenClasses.prepend(paramSymbol);
/* 2141 */           if (paramSymbol.type.hasTag(TypeTag.CLASS)) {
/* 2142 */             if (paramList.nonEmpty()) {
/* 2143 */               scan(paramList);
/*      */             }
/*      */             else {
/* 2146 */               Type.ClassType localClassType = (Type.ClassType)paramSymbol.type;
/* 2147 */               if ((localClassType.supertype_field == null) || (localClassType.interfaces_field == null))
/*      */               {
/* 2150 */                 this.partialCheck = true;
/* 2151 */                 return;
/*      */               }
/* 2153 */               checkSymbol(paramDiagnosticPosition, localClassType.supertype_field.tsym);
/* 2154 */               for (Type localType : localClassType.interfaces_field) {
/* 2155 */                 checkSymbol(paramDiagnosticPosition, localType.tsym);
/*      */               }
/*      */             }
/* 2158 */             if (paramSymbol.owner.kind == 2)
/* 2159 */               checkSymbol(paramDiagnosticPosition, paramSymbol.owner);
/*      */           }
/*      */         }
/*      */         finally {
/* 2163 */           this.seenClasses = this.seenClasses.tail;
/*      */         }
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   private class DefaultMethodClashFilter
/*      */     implements Filter<Symbol>
/*      */   {
/*      */     Type site;
/*      */ 
/*      */     DefaultMethodClashFilter(Type arg2)
/*      */     {
/*      */       Object localObject;
/* 2483 */       this.site = localObject;
/*      */     }
/*      */ 
/*      */     public boolean accepts(Symbol paramSymbol)
/*      */     {
/* 2490 */       return (paramSymbol.kind == 16) && 
/* 2488 */         ((paramSymbol
/* 2488 */         .flags() & 0x0) != 0L) && 
/* 2489 */         (paramSymbol
/* 2489 */         .isInheritedIn(this.site.tsym, Check.this.types)) && 
/* 2490 */         (!paramSymbol
/* 2490 */         .isConstructor());
/*      */     }
/*      */   }
/*      */ 
/*      */   static class NestedCheckContext
/*      */     implements Check.CheckContext
/*      */   {
/*      */     Check.CheckContext enclosingContext;
/*      */ 
/*      */     NestedCheckContext(Check.CheckContext paramCheckContext)
/*      */     {
/*  469 */       this.enclosingContext = paramCheckContext;
/*      */     }
/*      */ 
/*      */     public boolean compatible(Type paramType1, Type paramType2, Warner paramWarner) {
/*  473 */       return this.enclosingContext.compatible(paramType1, paramType2, paramWarner);
/*      */     }
/*      */ 
/*      */     public void report(JCDiagnostic.DiagnosticPosition paramDiagnosticPosition, JCDiagnostic paramJCDiagnostic) {
/*  477 */       this.enclosingContext.report(paramDiagnosticPosition, paramJCDiagnostic);
/*      */     }
/*      */ 
/*      */     public Warner checkWarner(JCDiagnostic.DiagnosticPosition paramDiagnosticPosition, Type paramType1, Type paramType2) {
/*  481 */       return this.enclosingContext.checkWarner(paramDiagnosticPosition, paramType1, paramType2);
/*      */     }
/*      */ 
/*      */     public Infer.InferenceContext inferenceContext() {
/*  485 */       return this.enclosingContext.inferenceContext();
/*      */     }
/*      */ 
/*      */     public DeferredAttr.DeferredAttrContext deferredAttrContext() {
/*  489 */       return this.enclosingContext.deferredAttrContext();
/*      */     }
/*      */   }
/*      */ 
/*      */   class Validator extends JCTree.Visitor
/*      */   {
/*      */     boolean checkRaw;
/*      */     boolean isOuter;
/*      */     Env<AttrContext> env;
/*      */ 
/*      */     Validator()
/*      */     {
/*      */       Object localObject;
/* 1249 */       this.env = localObject;
/*      */     }
/*      */ 
/*      */     public void visitTypeArray(JCTree.JCArrayTypeTree paramJCArrayTypeTree)
/*      */     {
/* 1254 */       validateTree(paramJCArrayTypeTree.elemtype, this.checkRaw, this.isOuter);
/*      */     }
/*      */ 
/*      */     public void visitTypeApply(JCTree.JCTypeApply paramJCTypeApply)
/*      */     {
/* 1259 */       if (paramJCTypeApply.type.hasTag(TypeTag.CLASS)) {
/* 1260 */         List localList1 = paramJCTypeApply.arguments;
/* 1261 */         List localList2 = paramJCTypeApply.type.tsym.type.getTypeArguments();
/*      */ 
/* 1263 */         Type localType = Check.this.firstIncompatibleTypeArg(paramJCTypeApply.type);
/* 1264 */         if (localType != null) {
/* 1265 */           for (JCTree localJCTree : paramJCTypeApply.arguments) {
/* 1266 */             if (localJCTree.type == localType) {
/* 1267 */               Check.this.log.error(localJCTree, "not.within.bounds", new Object[] { localType, localList2.head });
/*      */             }
/* 1269 */             localList2 = localList2.tail;
/*      */           }
/*      */         }
/*      */ 
/* 1273 */         localList2 = paramJCTypeApply.type.tsym.type.getTypeArguments();
/*      */ 
/* 1275 */         int i = paramJCTypeApply.type.tsym.flatName() == Check.this.names.java_lang_Class ? 1 : 0;
/*      */ 
/* 1279 */         while ((localList1.nonEmpty()) && (localList2.nonEmpty())) {
/* 1280 */           validateTree((JCTree)localList1.head, (!this.isOuter) || (i == 0), false);
/*      */ 
/* 1283 */           localList1 = localList1.tail;
/* 1284 */           localList2 = localList2.tail;
/*      */         }
/*      */ 
/* 1289 */         if (paramJCTypeApply.type.getEnclosingType().isRaw())
/* 1290 */           Check.this.log.error(paramJCTypeApply.pos(), "improperly.formed.type.inner.raw.param", new Object[0]);
/* 1291 */         if (paramJCTypeApply.clazz.hasTag(JCTree.Tag.SELECT))
/* 1292 */           visitSelectInternal((JCTree.JCFieldAccess)paramJCTypeApply.clazz);
/*      */       }
/*      */     }
/*      */ 
/*      */     public void visitTypeParameter(JCTree.JCTypeParameter paramJCTypeParameter)
/*      */     {
/* 1298 */       validateTrees(paramJCTypeParameter.bounds, true, this.isOuter);
/* 1299 */       Check.this.checkClassBounds(paramJCTypeParameter.pos(), paramJCTypeParameter.type);
/*      */     }
/*      */ 
/*      */     public void visitWildcard(JCTree.JCWildcard paramJCWildcard)
/*      */     {
/* 1304 */       if (paramJCWildcard.inner != null)
/* 1305 */         validateTree(paramJCWildcard.inner, true, this.isOuter);
/*      */     }
/*      */ 
/*      */     public void visitSelect(JCTree.JCFieldAccess paramJCFieldAccess)
/*      */     {
/* 1310 */       if (paramJCFieldAccess.type.hasTag(TypeTag.CLASS)) {
/* 1311 */         visitSelectInternal(paramJCFieldAccess);
/*      */ 
/* 1315 */         if ((paramJCFieldAccess.selected.type.isParameterized()) && (paramJCFieldAccess.type.tsym.type.getTypeArguments().nonEmpty()))
/* 1316 */           Check.this.log.error(paramJCFieldAccess.pos(), "improperly.formed.type.param.missing", new Object[0]);
/*      */       }
/*      */     }
/*      */ 
/*      */     public void visitSelectInternal(JCTree.JCFieldAccess paramJCFieldAccess) {
/* 1321 */       if ((paramJCFieldAccess.type.tsym.isStatic()) && 
/* 1322 */         (paramJCFieldAccess.selected.type
/* 1322 */         .isParameterized()))
/*      */       {
/* 1326 */         Check.this.log.error(paramJCFieldAccess.pos(), "cant.select.static.class.from.param.type", new Object[0]);
/*      */       }
/*      */       else
/* 1329 */         paramJCFieldAccess.selected.accept(this);
/*      */     }
/*      */ 
/*      */     public void visitAnnotatedType(JCTree.JCAnnotatedType paramJCAnnotatedType)
/*      */     {
/* 1335 */       paramJCAnnotatedType.underlyingType.accept(this);
/*      */     }
/*      */ 
/*      */     public void visitTypeIdent(JCTree.JCPrimitiveTypeTree paramJCPrimitiveTypeTree)
/*      */     {
/* 1340 */       if (paramJCPrimitiveTypeTree.type.hasTag(TypeTag.VOID)) {
/* 1341 */         Check.this.log.error(paramJCPrimitiveTypeTree.pos(), "void.not.allowed.here", new Object[0]);
/*      */       }
/* 1343 */       super.visitTypeIdent(paramJCPrimitiveTypeTree);
/*      */     }
/*      */ 
/*      */     public void visitTree(JCTree paramJCTree)
/*      */     {
/*      */     }
/*      */ 
/*      */     public void validateTree(JCTree paramJCTree, boolean paramBoolean1, boolean paramBoolean2)
/*      */     {
/* 1353 */       if (paramJCTree != null) {
/* 1354 */         boolean bool = this.checkRaw;
/* 1355 */         this.checkRaw = paramBoolean1;
/* 1356 */         this.isOuter = paramBoolean2;
/*      */         try
/*      */         {
/* 1359 */           paramJCTree.accept(this);
/* 1360 */           if (paramBoolean1)
/* 1361 */             Check.this.checkRaw(paramJCTree, this.env);
/*      */         } catch (Symbol.CompletionFailure localCompletionFailure) {
/* 1363 */           Check.this.completionError(paramJCTree.pos(), localCompletionFailure);
/*      */         } finally {
/* 1365 */           this.checkRaw = bool;
/*      */         }
/*      */       }
/*      */     }
/*      */ 
/*      */     public void validateTrees(List<? extends JCTree> paramList, boolean paramBoolean1, boolean paramBoolean2) {
/* 1371 */       for (Object localObject = paramList; ((List)localObject).nonEmpty(); localObject = ((List)localObject).tail)
/* 1372 */         validateTree((JCTree)((List)localObject).head, paramBoolean1, paramBoolean2);
/*      */     }
/*      */   }
/*      */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.javac.comp.Check
 * JD-Core Version:    0.6.2
 */