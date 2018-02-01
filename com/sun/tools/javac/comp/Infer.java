/*      */ package com.sun.tools.javac.comp;
/*      */ 
/*      */ import com.sun.tools.javac.code.Lint.LintCategory;
/*      */ import com.sun.tools.javac.code.Source;
/*      */ import com.sun.tools.javac.code.Symbol;
/*      */ import com.sun.tools.javac.code.Symbol.ClassSymbol;
/*      */ import com.sun.tools.javac.code.Symbol.MethodSymbol;
/*      */ import com.sun.tools.javac.code.Symbol.TypeSymbol;
/*      */ import com.sun.tools.javac.code.Symbol.TypeVariableSymbol;
/*      */ import com.sun.tools.javac.code.Symtab;
/*      */ import com.sun.tools.javac.code.Type;
/*      */ import com.sun.tools.javac.code.Type.CapturedType;
/*      */ import com.sun.tools.javac.code.Type.CapturedUndetVar;
/*      */ import com.sun.tools.javac.code.Type.JCNoType;
/*      */ import com.sun.tools.javac.code.Type.Mapping;
/*      */ import com.sun.tools.javac.code.Type.MethodType;
/*      */ import com.sun.tools.javac.code.Type.TypeVar;
/*      */ import com.sun.tools.javac.code.Type.UndetVar;
/*      */ import com.sun.tools.javac.code.Type.UndetVar.InferenceBound;
/*      */ import com.sun.tools.javac.code.Type.UndetVar.UndetVarListener;
/*      */ import com.sun.tools.javac.code.TypeTag;
/*      */ import com.sun.tools.javac.code.Types;
/*      */ import com.sun.tools.javac.tree.JCTree;
/*      */ import com.sun.tools.javac.tree.JCTree.JCExpressionStatement;
/*      */ import com.sun.tools.javac.tree.JCTree.JCTypeCast;
/*      */ import com.sun.tools.javac.tree.JCTree.Tag;
/*      */ import com.sun.tools.javac.tree.TreeInfo;
/*      */ import com.sun.tools.javac.util.Assert;
/*      */ import com.sun.tools.javac.util.Context;
/*      */ import com.sun.tools.javac.util.Context.Key;
/*      */ import com.sun.tools.javac.util.Filter;
/*      */ import com.sun.tools.javac.util.GraphUtils;
/*      */ import com.sun.tools.javac.util.GraphUtils.DependencyKind;
/*      */ import com.sun.tools.javac.util.GraphUtils.Node;
/*      */ import com.sun.tools.javac.util.GraphUtils.TarjanNode;
/*      */ import com.sun.tools.javac.util.JCDiagnostic;
/*      */ import com.sun.tools.javac.util.JCDiagnostic.DiagnosticPosition;
/*      */ import com.sun.tools.javac.util.JCDiagnostic.Factory;
/*      */ import com.sun.tools.javac.util.List;
/*      */ import com.sun.tools.javac.util.ListBuffer;
/*      */ import com.sun.tools.javac.util.Log;
/*      */ import com.sun.tools.javac.util.Options;
/*      */ import com.sun.tools.javac.util.Pair;
/*      */ import com.sun.tools.javac.util.Warner;
/*      */ import java.util.ArrayList;
/*      */ import java.util.Collection;
/*      */ import java.util.Collections;
/*      */ import java.util.EnumMap;
/*      */ import java.util.EnumSet;
/*      */ import java.util.HashMap;
/*      */ import java.util.HashSet;
/*      */ import java.util.Iterator;
/*      */ import java.util.LinkedHashSet;
/*      */ import java.util.Map;
/*      */ import java.util.Map.Entry;
/*      */ import java.util.Set;
/*      */ 
/*      */ public class Infer
/*      */ {
/*   65 */   protected static final Context.Key<Infer> inferKey = new Context.Key();
/*      */   Resolve rs;
/*      */   Check chk;
/*      */   Symtab syms;
/*      */   Types types;
/*      */   JCDiagnostic.Factory diags;
/*      */   Log log;
/*      */   boolean allowGraphInference;
/*  101 */   public static final Type anyPoly = new Type.JCNoType();
/*      */   protected final InferenceException inferenceException;
/*      */   static final int MAX_INCORPORATION_STEPS = 100;
/*  965 */   EnumSet<IncorporationStep> incorporationStepsLegacy = EnumSet.of(IncorporationStep.EQ_CHECK_LEGACY);
/*      */ 
/*  969 */   EnumSet<IncorporationStep> incorporationStepsGraph = EnumSet.complementOf(EnumSet.of(IncorporationStep.EQ_CHECK_LEGACY))
/*  969 */     ;
/*      */ 
/* 1064 */   Map<IncorporationBinaryOp, Boolean> incorporationCache = new HashMap();
/*      */ 
/* 2326 */   final InferenceContext emptyContext = new InferenceContext(List.nil());
/*      */ 
/*      */   public static Infer instance(Context paramContext)
/*      */   {
/*   79 */     Infer localInfer = (Infer)paramContext.get(inferKey);
/*   80 */     if (localInfer == null)
/*   81 */       localInfer = new Infer(paramContext);
/*   82 */     return localInfer;
/*      */   }
/*      */ 
/*      */   protected Infer(Context paramContext) {
/*   86 */     paramContext.put(inferKey, this);
/*      */ 
/*   88 */     this.rs = Resolve.instance(paramContext);
/*   89 */     this.chk = Check.instance(paramContext);
/*   90 */     this.syms = Symtab.instance(paramContext);
/*   91 */     this.types = Types.instance(paramContext);
/*   92 */     this.diags = JCDiagnostic.Factory.instance(paramContext);
/*   93 */     this.log = Log.instance(paramContext);
/*   94 */     this.inferenceException = new InferenceException(this.diags);
/*   95 */     Options localOptions = Options.instance(paramContext);
/*   96 */     this.allowGraphInference = ((Source.instance(paramContext).allowGraphInference()) && 
/*   97 */       (localOptions
/*   97 */       .isUnset("useLegacyInference")));
/*      */   }
/*      */ 
/*      */   Type instantiateMethod(Env<AttrContext> paramEnv, List<Type> paramList1, Type.MethodType paramMethodType, Attr.ResultInfo paramResultInfo, Symbol.MethodSymbol paramMethodSymbol, List<Type> paramList2, boolean paramBoolean1, boolean paramBoolean2, Resolve.MethodResolutionContext paramMethodResolutionContext, Warner paramWarner)
/*      */     throws Infer.InferenceException
/*      */   {
/*  156 */     InferenceContext localInferenceContext = new InferenceContext(paramList1);
/*  157 */     this.inferenceException.clear();
/*      */     try
/*      */     {
/*  160 */       DeferredAttr.DeferredAttrContext localDeferredAttrContext = paramMethodResolutionContext
/*  160 */         .deferredAttrContext(paramMethodSymbol, localInferenceContext, paramResultInfo, paramWarner);
/*      */ 
/*  162 */       paramMethodResolutionContext.methodCheck.argumentsAcceptable(paramEnv, localDeferredAttrContext, paramList2, paramMethodType
/*  163 */         .getParameterTypes(), paramWarner);
/*      */       Object localObject1;
/*  165 */       if ((this.allowGraphInference) && (paramResultInfo != null))
/*      */       {
/*  167 */         if (!paramWarner
/*  167 */           .hasNonSilentLint(Lint.LintCategory.UNCHECKED))
/*      */         {
/*  169 */           checkWithinBounds(localInferenceContext, paramWarner);
/*  170 */           localObject1 = generateReturnConstraints(paramEnv.tree, paramResultInfo, paramMethodType, localInferenceContext);
/*      */ 
/*  172 */           paramMethodType = (Type.MethodType)this.types.createMethodTypeWithReturn(paramMethodType, (Type)localObject1);
/*      */ 
/*  174 */           if (paramResultInfo.checkContext.inferenceContext().free(paramResultInfo.pt))
/*      */           {
/*  176 */             localInferenceContext.dupTo(paramResultInfo.checkContext.inferenceContext());
/*  177 */             localDeferredAttrContext.complete();
/*  178 */             return paramMethodType;
/*      */           }
/*      */         }
/*      */       }
/*  182 */       localDeferredAttrContext.complete();
/*      */ 
/*  185 */       if (this.allowGraphInference)
/*  186 */         localInferenceContext.solve(paramWarner);
/*      */       else {
/*  188 */         localInferenceContext.solveLegacy(true, paramWarner, LegacyInferenceSteps.EQ_LOWER.steps);
/*      */       }
/*      */ 
/*  191 */       paramMethodType = (Type.MethodType)localInferenceContext.asInstType(paramMethodType);
/*      */ 
/*  193 */       if ((!this.allowGraphInference) && 
/*  194 */         (localInferenceContext
/*  194 */         .restvars().nonEmpty()) && (paramResultInfo != null))
/*      */       {
/*  196 */         if (!paramWarner
/*  196 */           .hasNonSilentLint(Lint.LintCategory.UNCHECKED))
/*      */         {
/*  197 */           generateReturnConstraints(paramEnv.tree, paramResultInfo, paramMethodType, localInferenceContext);
/*  198 */           localInferenceContext.solveLegacy(false, paramWarner, LegacyInferenceSteps.EQ_UPPER.steps);
/*  199 */           paramMethodType = (Type.MethodType)localInferenceContext.asInstType(paramMethodType);
/*      */         }
/*      */       }
/*  202 */       if ((paramResultInfo != null) && (this.rs.verboseResolutionMode.contains(Resolve.VerboseResolutionMode.DEFERRED_INST))) {
/*  203 */         this.log.note(paramEnv.tree.pos, "deferred.method.inst", new Object[] { paramMethodSymbol, paramMethodType, paramResultInfo.pt });
/*      */       }
/*      */ 
/*  207 */       return paramMethodType;
/*      */     } finally {
/*  209 */       if ((paramResultInfo != null) || (!this.allowGraphInference))
/*  210 */         localInferenceContext.notifyChange();
/*      */       else {
/*  212 */         localInferenceContext.notifyChange(localInferenceContext.boundedVars());
/*      */       }
/*  214 */       if (paramResultInfo == null)
/*      */       {
/*  218 */         localInferenceContext.captureTypeCache.clear();
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   Type generateReturnConstraints(JCTree paramJCTree, Attr.ResultInfo paramResultInfo, Type.MethodType paramMethodType, InferenceContext paramInferenceContext)
/*      */   {
/*  230 */     InferenceContext localInferenceContext = paramResultInfo.checkContext.inferenceContext();
/*  231 */     Type localType = paramMethodType.getReturnType();
/*  232 */     if ((paramMethodType.getReturnType().containsAny(paramInferenceContext.inferencevars)) && (localInferenceContext != this.emptyContext))
/*      */     {
/*  234 */       localType = this.types.capture(localType);
/*      */ 
/*  236 */       for (localObject1 = localType.getTypeArguments().iterator(); ((Iterator)localObject1).hasNext(); ) { localObject2 = (Type)((Iterator)localObject1).next();
/*  237 */         if ((((Type)localObject2).hasTag(TypeTag.TYPEVAR)) && (((Type.TypeVar)localObject2).isCaptured())) {
/*  238 */           paramInferenceContext.addVar((Type.TypeVar)localObject2);
/*      */         }
/*      */       }
/*      */     }
/*  242 */     Object localObject1 = paramInferenceContext.asUndetVar(localType);
/*  243 */     Object localObject2 = paramResultInfo.pt;
/*      */ 
/*  245 */     if (((Type)localObject1).hasTag(TypeTag.VOID))
/*  246 */       localObject2 = this.syms.voidType;
/*  247 */     else if (((Type)localObject2).hasTag(TypeTag.NONE))
/*  248 */       localObject2 = localType.isPrimitive() ? localType : this.syms.objectType;
/*  249 */     else if (((Type)localObject1).hasTag(TypeTag.UNDETVAR)) {
/*  250 */       if (paramResultInfo.pt.isReference()) {
/*  251 */         localObject2 = generateReturnConstraintsUndetVarToReference(paramJCTree, (Type.UndetVar)localObject1, (Type)localObject2, paramResultInfo, paramInferenceContext);
/*      */       }
/*  254 */       else if (((Type)localObject2).isPrimitive()) {
/*  255 */         localObject2 = generateReturnConstraintsPrimitive(paramJCTree, (Type.UndetVar)localObject1, (Type)localObject2, paramResultInfo, paramInferenceContext);
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/*  260 */     Assert.check((this.allowGraphInference) || (!localInferenceContext.free((Type)localObject2)), "legacy inference engine cannot handle constraints on both sides of a subtyping assertion");
/*      */ 
/*  263 */     Warner localWarner = new Warner();
/*  264 */     if (paramResultInfo.checkContext.compatible((Type)localObject1, localInferenceContext.asUndetVar((Type)localObject2), localWarner)) { if (!this.allowGraphInference) {
/*  266 */         if (!localWarner
/*  266 */           .hasLint(Lint.LintCategory.UNCHECKED));
/*      */       }
/*      */     } else throw this.inferenceException
/*  268 */         .setMessage("infer.no.conforming.instance.exists", new Object[] { paramInferenceContext
/*  269 */         .restvars(), paramMethodType.getReturnType(), localObject2 });
/*      */ 
/*  271 */     return localType;
/*      */   }
/*      */ 
/*      */   private Type generateReturnConstraintsPrimitive(JCTree paramJCTree, Type.UndetVar paramUndetVar, Type paramType, Attr.ResultInfo paramResultInfo, InferenceContext paramInferenceContext)
/*      */   {
/*  276 */     if (!this.allowGraphInference)
/*      */     {
/*  278 */       return this.types.boxedClass(paramType).type;
/*      */     }
/*      */ 
/*  281 */     for (Type localType1 : paramUndetVar.getBounds(new Type.UndetVar.InferenceBound[] { Type.UndetVar.InferenceBound.EQ, Type.UndetVar.InferenceBound.UPPER, Type.UndetVar.InferenceBound.LOWER }))
/*      */     {
/*  283 */       Type localType2 = this.types.unboxedType(localType1);
/*  284 */       if ((localType2 != null) && (!localType2.hasTag(TypeTag.NONE)))
/*      */       {
/*  287 */         return generateReferenceToTargetConstraint(paramJCTree, paramUndetVar, paramType, paramResultInfo, paramInferenceContext);
/*      */       }
/*      */     }
/*  290 */     return this.types.boxedClass(paramType).type;
/*      */   }
/*      */ 
/*      */   private Type generateReturnConstraintsUndetVarToReference(JCTree paramJCTree, Type.UndetVar paramUndetVar, Type paramType, Attr.ResultInfo paramResultInfo, InferenceContext paramInferenceContext)
/*      */   {
/*  296 */     Type localType1 = this.types.capture(paramType);
/*      */     Iterator localIterator;
/*      */     Type localType2;
/*      */     Object localObject;
/*  299 */     if (localType1 == paramType)
/*      */     {
/*  303 */       for (localIterator = paramUndetVar.getBounds(new Type.UndetVar.InferenceBound[] { Type.UndetVar.InferenceBound.EQ, Type.UndetVar.InferenceBound.LOWER }).iterator(); localIterator.hasNext(); ) { localType2 = (Type)localIterator.next();
/*  304 */         localObject = this.types.capture(localType2);
/*  305 */         if (localObject != localType2) {
/*  306 */           return generateReferenceToTargetConstraint(paramJCTree, paramUndetVar, paramType, paramResultInfo, paramInferenceContext);
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/*  315 */       for (localIterator = paramUndetVar.getBounds(new Type.UndetVar.InferenceBound[] { Type.UndetVar.InferenceBound.LOWER }).iterator(); localIterator.hasNext(); ) { localType2 = (Type)localIterator.next();
/*  316 */         for (localObject = paramUndetVar.getBounds(new Type.UndetVar.InferenceBound[] { Type.UndetVar.InferenceBound.LOWER }).iterator(); ((Iterator)localObject).hasNext(); ) { Type localType3 = (Type)((Iterator)localObject).next();
/*  317 */           if ((localType2 != localType3) && 
/*  318 */             (!paramInferenceContext
/*  318 */             .free(localType2)) && 
/*  319 */             (!paramInferenceContext
/*  319 */             .free(localType3)) && 
/*  320 */             (commonSuperWithDiffParameterization(localType2, localType3)))
/*      */           {
/*  321 */             return generateReferenceToTargetConstraint(paramJCTree, paramUndetVar, paramType, paramResultInfo, paramInferenceContext);
/*      */           }
/*      */ 
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/*  333 */     if (paramType.isParameterized()) {
/*  334 */       for (localIterator = paramUndetVar.getBounds(new Type.UndetVar.InferenceBound[] { Type.UndetVar.InferenceBound.EQ, Type.UndetVar.InferenceBound.LOWER }).iterator(); localIterator.hasNext(); ) { localType2 = (Type)localIterator.next();
/*  335 */         localObject = this.types.asSuper(localType2, paramType.tsym);
/*  336 */         if ((localObject != null) && (((Type)localObject).isRaw())) {
/*  337 */           return generateReferenceToTargetConstraint(paramJCTree, paramUndetVar, paramType, paramResultInfo, paramInferenceContext);
/*      */         }
/*      */       }
/*      */     }
/*      */ 
/*  342 */     return paramType;
/*      */   }
/*      */ 
/*      */   private boolean commonSuperWithDiffParameterization(Type paramType1, Type paramType2) {
/*  346 */     Pair localPair = getParameterizedSupers(paramType1, paramType2);
/*  347 */     return (localPair != null) && (!this.types.isSameType((Type)localPair.fst, (Type)localPair.snd));
/*      */   }
/*      */ 
/*      */   private Type generateReferenceToTargetConstraint(JCTree paramJCTree, Type.UndetVar paramUndetVar, Type paramType, Attr.ResultInfo paramResultInfo, InferenceContext paramInferenceContext)
/*      */   {
/*  353 */     paramInferenceContext.solve(List.of(paramUndetVar.qtype), new Warner());
/*  354 */     paramInferenceContext.notifyChange();
/*      */ 
/*  356 */     Type localType = paramResultInfo.checkContext.inferenceContext()
/*  356 */       .cachedCapture(paramJCTree, paramUndetVar.inst, false);
/*      */ 
/*  357 */     if (this.types.isConvertible(localType, paramResultInfo.checkContext
/*  358 */       .inferenceContext().asUndetVar(paramType)))
/*      */     {
/*  360 */       return this.syms.objectType;
/*      */     }
/*  362 */     return paramType;
/*      */   }
/*      */ 
/*      */   private void instantiateAsUninferredVars(List<Type> paramList, InferenceContext paramInferenceContext)
/*      */   {
/*  369 */     ListBuffer localListBuffer = new ListBuffer();
/*      */ 
/*  371 */     for (Object localObject1 = paramList.iterator(); ((Iterator)localObject1).hasNext(); ) { localObject2 = (Type)((Iterator)localObject1).next();
/*  372 */       localObject3 = (Type.UndetVar)paramInferenceContext.asUndetVar((Type)localObject2);
/*  373 */       localObject4 = ((Type.UndetVar)localObject3).getBounds(new Type.UndetVar.InferenceBound[] { Type.UndetVar.InferenceBound.UPPER });
/*  374 */       if (Type.containsAny((List)localObject4, paramList)) {
/*  375 */         localObject5 = new Symbol.TypeVariableSymbol(4096L, ((Type.UndetVar)localObject3).qtype.tsym.name, null, ((Type.UndetVar)localObject3).qtype.tsym.owner);
/*  376 */         ((Symbol.TypeSymbol)localObject5).type = new Type.TypeVar((Symbol.TypeSymbol)localObject5, this.types.makeIntersectionType(((Type.UndetVar)localObject3).getBounds(new Type.UndetVar.InferenceBound[] { Type.UndetVar.InferenceBound.UPPER })), null);
/*  377 */         localListBuffer.append(localObject3);
/*  378 */         ((Type.UndetVar)localObject3).inst = ((Symbol.TypeSymbol)localObject5).type;
/*  379 */       } else if (((List)localObject4).nonEmpty()) {
/*  380 */         ((Type.UndetVar)localObject3).inst = this.types.glb((List)localObject4);
/*      */       } else {
/*  382 */         ((Type.UndetVar)localObject3).inst = this.syms.objectType;
/*      */       }
/*      */     }
/*  387 */     Object localObject3;
/*      */     Object localObject4;
/*      */     Object localObject5;
/*  386 */     localObject1 = paramList;
/*  387 */     for (Object localObject2 = localListBuffer.iterator(); ((Iterator)localObject2).hasNext(); ) { localObject3 = (Type)((Iterator)localObject2).next();
/*  388 */       localObject4 = (Type.UndetVar)localObject3;
/*  389 */       localObject5 = (Type.TypeVar)((Type.UndetVar)localObject4).inst;
/*  390 */       ((Type.TypeVar)localObject5).bound = this.types.glb(paramInferenceContext.asInstTypes(this.types.getBounds((Type.TypeVar)localObject5)));
/*  391 */       if (((Type.TypeVar)localObject5).bound.isErroneous())
/*      */       {
/*  393 */         reportBoundError((Type.UndetVar)localObject4, BoundErrorKind.BAD_UPPER);
/*      */       }
/*  395 */       localObject1 = ((List)localObject1).tail;
/*      */     }
/*      */   }
/*      */ 
/*      */   Type instantiatePolymorphicSignatureInstance(Env<AttrContext> paramEnv, Symbol.MethodSymbol paramMethodSymbol, Resolve.MethodResolutionContext paramMethodResolutionContext, List<Type> paramList)
/*      */   {
/*      */     Type localType;
/*  418 */     switch (paramEnv.next.tree.getTag()) {
/*      */     case TYPECAST:
/*  420 */       localObject1 = (JCTree.JCTypeCast)paramEnv.next.tree;
/*  421 */       localType = TreeInfo.skipParens(((JCTree.JCTypeCast)localObject1).expr) == paramEnv.tree ? ((JCTree.JCTypeCast)localObject1).clazz.type : this.syms.objectType;
/*      */ 
/*  424 */       break;
/*      */     case EXEC:
/*  426 */       localObject2 = (JCTree.JCExpressionStatement)paramEnv.next.tree;
/*      */ 
/*  428 */       localType = TreeInfo.skipParens(((JCTree.JCExpressionStatement)localObject2).expr) == paramEnv.tree ? this.syms.voidType : this.syms.objectType;
/*      */ 
/*  431 */       break;
/*      */     default:
/*  433 */       localType = this.syms.objectType;
/*      */     }
/*      */ 
/*  436 */     Object localObject1 = Type.map(paramList, new ImplicitArgType(paramMethodSymbol, paramMethodResolutionContext.step));
/*      */ 
/*  439 */     Object localObject2 = paramMethodSymbol != null ? paramMethodSymbol
/*  438 */       .getThrownTypes() : 
/*  439 */       List.of(this.syms.throwableType);
/*      */ 
/*  441 */     Type.MethodType localMethodType = new Type.MethodType((List)localObject1, localType, (List)localObject2, this.syms.methodClass);
/*      */ 
/*  445 */     return localMethodType;
/*      */   }
/*      */ 
/*      */   public Type instantiateFunctionalInterface(JCDiagnostic.DiagnosticPosition paramDiagnosticPosition, Type paramType, List<Type> paramList, Check.CheckContext paramCheckContext)
/*      */   {
/*  472 */     if (this.types.capture(paramType) == paramType)
/*      */     {
/*  475 */       return paramType;
/*      */     }
/*  477 */     Type localType1 = paramType.tsym.type;
/*      */ 
/*  479 */     InferenceContext localInferenceContext = new InferenceContext(paramType.tsym.type
/*  479 */       .getTypeArguments());
/*      */ 
/*  481 */     Assert.check(paramList != null);
/*      */ 
/*  485 */     List localList1 = this.types.findDescriptorType(localType1).getParameterTypes();
/*  486 */     if (localList1.size() != paramList.size()) {
/*  487 */       paramCheckContext.report(paramDiagnosticPosition, this.diags.fragment("incompatible.arg.types.in.lambda", new Object[0]));
/*  488 */       return this.types.createErrorType(paramType);
/*      */     }
/*  490 */     for (Iterator localIterator = localList1.iterator(); localIterator.hasNext(); ) { localObject = (Type)localIterator.next();
/*  491 */       if (!this.types.isSameType(localInferenceContext.asUndetVar((Type)localObject), (Type)paramList.head)) {
/*  492 */         paramCheckContext.report(paramDiagnosticPosition, this.diags.fragment("no.suitable.functional.intf.inst", new Object[] { paramType }));
/*  493 */         return this.types.createErrorType(paramType);
/*      */       }
/*  495 */       paramList = paramList.tail;
/*      */     }
/*      */     try
/*      */     {
/*  499 */       localInferenceContext.solve(localInferenceContext.boundedVars(), this.types.noWarnings);
/*      */     } catch (InferenceException localInferenceException) {
/*  501 */       paramCheckContext.report(paramDiagnosticPosition, this.diags.fragment("no.suitable.functional.intf.inst", new Object[] { paramType }));
/*      */     }
/*      */ 
/*  504 */     List localList2 = paramType.getTypeArguments();
/*  505 */     for (Object localObject = localInferenceContext.undetvars.iterator(); ((Iterator)localObject).hasNext(); ) { Type localType2 = (Type)((Iterator)localObject).next();
/*  506 */       Type.UndetVar localUndetVar = (Type.UndetVar)localType2;
/*  507 */       if (localUndetVar.inst == null) {
/*  508 */         localUndetVar.inst = ((Type)localList2.head);
/*      */       }
/*  510 */       localList2 = localList2.tail;
/*      */     }
/*      */ 
/*  513 */     localObject = localInferenceContext.asInstType(localType1);
/*  514 */     if (!this.chk.checkValidGenericType((Type)localObject))
/*      */     {
/*  517 */       paramCheckContext.report(paramDiagnosticPosition, this.diags.fragment("no.suitable.functional.intf.inst", new Object[] { paramType }));
/*      */     }
/*      */ 
/*  520 */     paramCheckContext.compatible((Type)localObject, paramType, this.types.noWarnings);
/*  521 */     return localObject;
/*      */   }
/*      */ 
/*      */   void checkWithinBounds(InferenceContext paramInferenceContext, Warner paramWarner)
/*      */     throws Infer.InferenceException
/*      */   {
/*  532 */     MultiUndetVarListener localMultiUndetVarListener = new MultiUndetVarListener(paramInferenceContext.undetvars);
/*  533 */     List localList = paramInferenceContext.save();
/*      */     try {
/*      */       while (true) {
/*  536 */         localMultiUndetVarListener.reset();
/*  537 */         if (!this.allowGraphInference)
/*      */         {
/*  540 */           for (localIterator1 = paramInferenceContext.undetvars.iterator(); localIterator1.hasNext(); ) { localType = (Type)localIterator1.next();
/*  541 */             localUndetVar = (Type.UndetVar)localType;
/*  542 */             IncorporationStep.CHECK_BOUNDS.apply(localUndetVar, paramInferenceContext, paramWarner);
/*      */           }
/*      */         }
/*  545 */         Type localType;
/*      */         Type.UndetVar localUndetVar;
/*  545 */         for (Iterator localIterator1 = paramInferenceContext.undetvars.iterator(); localIterator1.hasNext(); ) { localType = (Type)localIterator1.next();
/*  546 */           localUndetVar = (Type.UndetVar)localType;
/*      */ 
/*  548 */           EnumSet localEnumSet = this.allowGraphInference ? this.incorporationStepsGraph : this.incorporationStepsLegacy;
/*      */ 
/*  550 */           for (IncorporationStep localIncorporationStep : localEnumSet) {
/*  551 */             if (localIncorporationStep.accepts(localUndetVar, paramInferenceContext)) {
/*  552 */               localIncorporationStep.apply(localUndetVar, paramInferenceContext, paramWarner);
/*      */             }
/*      */           }
/*      */         }
/*  556 */         if (localMultiUndetVarListener.changed) if (!this.allowGraphInference) break; 
/*      */       }
/*      */     }
/*      */     finally
/*      */     {
/*  560 */       localMultiUndetVarListener.detach();
/*  561 */       if (this.incorporationCache.size() == 100) {
/*  562 */         paramInferenceContext.rollback(localList);
/*      */       }
/*  564 */       this.incorporationCache.clear();
/*      */     }
/*      */   }
/*      */ 
/*      */   private Pair<Type, Type> getParameterizedSupers(Type paramType1, Type paramType2)
/*      */   {
/*  625 */     Type localType1 = this.types.lub(new Type[] { paramType1, paramType2 });
/*  626 */     if ((localType1 == this.syms.errType) || (localType1 == this.syms.botType) || 
/*  627 */       (!localType1
/*  627 */       .isParameterized())) {
/*  628 */       return null;
/*      */     }
/*  630 */     Type localType2 = this.types.asSuper(paramType1, localType1.tsym);
/*  631 */     Type localType3 = this.types.asSuper(paramType2, localType1.tsym);
/*  632 */     return new Pair(localType2, localType3);
/*      */   }
/*      */ 
/*      */   void checkCompatibleUpperBounds(Type.UndetVar paramUndetVar, InferenceContext paramInferenceContext)
/*      */   {
/* 1073 */     List localList = Type.filter(paramUndetVar
/* 1073 */       .getBounds(new Type.UndetVar.InferenceBound[] { Type.UndetVar.InferenceBound.UPPER }), 
/* 1073 */       new BoundFilter(paramInferenceContext));
/* 1074 */     Type localType = null;
/* 1075 */     if (localList.isEmpty())
/* 1076 */       localType = this.syms.objectType;
/* 1077 */     else if (localList.tail.isEmpty())
/* 1078 */       localType = (Type)localList.head;
/*      */     else
/* 1080 */       localType = this.types.glb(localList);
/* 1081 */     if ((localType == null) || (localType.isErroneous()))
/* 1082 */       reportBoundError(paramUndetVar, BoundErrorKind.BAD_UPPER);
/*      */   }
/*      */ 
/*      */   void reportBoundError(Type.UndetVar paramUndetVar, BoundErrorKind paramBoundErrorKind)
/*      */   {
/* 1172 */     throw paramBoundErrorKind.setMessage(this.inferenceException, paramUndetVar);
/*      */   }
/*      */ 
/*      */   abstract class BestLeafSolver extends Infer.LeafSolver
/*      */   {
/*      */     List<Type> varsToSolve;
/* 1302 */     final Map<Infer.GraphSolver.InferenceGraph.Node, Pair<List<Infer.GraphSolver.InferenceGraph.Node>, Integer>> treeCache = new HashMap();
/*      */ 
/* 1306 */     final Pair<List<Infer.GraphSolver.InferenceGraph.Node>, Integer> noPath = new Pair(null, 
/* 1307 */       Integer.valueOf(2147483647));
/*      */ 
/*      */     BestLeafSolver()
/*      */     {
/* 1266 */       super();
/*      */       Object localObject;
/* 1267 */       this.varsToSolve = localObject;
/*      */     }
/*      */ 
/*      */     Pair<List<Infer.GraphSolver.InferenceGraph.Node>, Integer> computeTreeToLeafs(Infer.GraphSolver.InferenceGraph.Node paramNode)
/*      */     {
/* 1277 */       Object localObject = (Pair)this.treeCache.get(paramNode);
/* 1278 */       if (localObject == null)
/*      */       {
/* 1280 */         if (paramNode.isLeaf())
/*      */         {
/* 1282 */           localObject = new Pair(List.of(paramNode), Integer.valueOf(((ListBuffer)paramNode.data).length()));
/*      */         }
/*      */         else {
/* 1285 */           Pair localPair1 = new Pair(List.of(paramNode), Integer.valueOf(((ListBuffer)paramNode.data).length()));
/* 1286 */           for (Infer.GraphSolver.InferenceGraph.Node localNode : paramNode.getAllDependencies())
/* 1287 */             if (localNode != paramNode) {
/* 1288 */               Pair localPair2 = computeTreeToLeafs(localNode);
/*      */ 
/* 1291 */               localPair1 = new Pair(((List)localPair1.fst)
/* 1290 */                 .prependList((List)localPair2.fst), 
/* 1291 */                 Integer.valueOf(((Integer)localPair1.snd)
/* 1291 */                 .intValue() + ((Integer)localPair2.snd).intValue()));
/*      */             }
/* 1293 */           localObject = localPair1;
/*      */         }
/*      */ 
/* 1296 */         this.treeCache.put(paramNode, localObject);
/*      */       }
/* 1298 */       return localObject;
/*      */     }
/*      */ 
/*      */     public Infer.GraphSolver.InferenceGraph.Node pickNode(Infer.GraphSolver.InferenceGraph paramInferenceGraph)
/*      */     {
/* 1314 */       this.treeCache.clear();
/* 1315 */       Object localObject = this.noPath;
/* 1316 */       for (Infer.GraphSolver.InferenceGraph.Node localNode : paramInferenceGraph.nodes) {
/* 1317 */         if (!Collections.disjoint((Collection)localNode.data, this.varsToSolve)) {
/* 1318 */           Pair localPair = computeTreeToLeafs(localNode);
/*      */ 
/* 1321 */           if (((Integer)localPair.snd).intValue() < ((Integer)((Pair)localObject).snd).intValue()) {
/* 1322 */             localObject = localPair;
/*      */           }
/*      */         }
/*      */       }
/* 1326 */       if (localObject == this.noPath)
/*      */       {
/* 1328 */         throw new Infer.GraphStrategy.NodeNotFoundException(paramInferenceGraph);
/*      */       }
/* 1330 */       return (Infer.GraphSolver.InferenceGraph.Node)((List)((Pair)localObject).fst).head;
/*      */     }
/*      */   }
/*      */ 
/*      */   static abstract enum BoundErrorKind
/*      */   {
/* 1107 */     BAD_UPPER, 
/*      */ 
/* 1117 */     BAD_EQ_UPPER, 
/*      */ 
/* 1127 */     BAD_EQ_LOWER, 
/*      */ 
/* 1137 */     UPPER, 
/*      */ 
/* 1147 */     LOWER, 
/*      */ 
/* 1157 */     EQ;
/*      */ 
/*      */     abstract Resolve.InapplicableMethodException setMessage(Infer.InferenceException paramInferenceException, Type.UndetVar paramUndetVar);
/*      */   }
/*      */ 
/*      */   protected static class BoundFilter
/*      */     implements Filter<Type>
/*      */   {
/*      */     Infer.InferenceContext inferenceContext;
/*      */ 
/*      */     public BoundFilter(Infer.InferenceContext paramInferenceContext)
/*      */     {
/* 1090 */       this.inferenceContext = paramInferenceContext;
/*      */     }
/*      */ 
/*      */     public boolean accepts(Type paramType)
/*      */     {
/* 1096 */       return (!paramType.isErroneous()) && (!this.inferenceContext.free(paramType)) && 
/* 1096 */         (!paramType
/* 1096 */         .hasTag(TypeTag.BOT));
/*      */     }
/*      */   }
/*      */ 
/*      */   static enum DependencyKind
/*      */     implements GraphUtils.DependencyKind
/*      */   {
/* 1538 */     BOUND("dotted"), 
/*      */ 
/* 1540 */     STUCK("dashed");
/*      */ 
/*      */     final String dotSyle;
/*      */ 
/*      */     private DependencyKind(String paramString) {
/* 1545 */       this.dotSyle = paramString;
/*      */     }
/*      */ 
/*      */     public String getDotStyle()
/*      */     {
/* 1550 */       return this.dotSyle;
/*      */     }
/*      */   }
/*      */ 
/*      */   static abstract interface FreeTypeListener
/*      */   {
/*      */     public abstract void typesInferred(Infer.InferenceContext paramInferenceContext);
/*      */   }
/*      */ 
/*      */   static enum GraphInferenceSteps
/*      */   {
/* 1516 */     EQ(EnumSet.of(Infer.InferenceStep.EQ)), 
/* 1517 */     EQ_LOWER(EnumSet.of(Infer.InferenceStep.EQ, Infer.InferenceStep.LOWER)), 
/* 1518 */     EQ_LOWER_THROWS_UPPER_CAPTURED(EnumSet.of(Infer.InferenceStep.EQ, Infer.InferenceStep.LOWER, Infer.InferenceStep.UPPER, Infer.InferenceStep.THROWS, Infer.InferenceStep.CAPTURED));
/*      */ 
/*      */     final EnumSet<Infer.InferenceStep> steps;
/*      */ 
/*      */     private GraphInferenceSteps(EnumSet<Infer.InferenceStep> paramEnumSet) {
/* 1523 */       this.steps = paramEnumSet;
/*      */     }
/*      */   }
/*      */ 
/*      */   class GraphSolver
/*      */   {
/*      */     Infer.InferenceContext inferenceContext;
/*      */     Map<Type, Set<Type>> stuckDeps;
/*      */     Warner warn;
/*      */ 
/*      */     GraphSolver(Map<Type, Set<Type>> paramWarner, Warner arg3)
/*      */     {
/* 1569 */       this.inferenceContext = paramWarner;
/*      */       Object localObject1;
/* 1570 */       this.stuckDeps = localObject1;
/*      */       Object localObject2;
/* 1571 */       this.warn = localObject2;
/*      */     }
/*      */ 
/*      */     void solve(Infer.GraphStrategy paramGraphStrategy)
/*      */     {
/* 1580 */       Infer.this.checkWithinBounds(this.inferenceContext, this.warn);
/* 1581 */       InferenceGraph localInferenceGraph = new InferenceGraph(this.stuckDeps);
/* 1582 */       while (!paramGraphStrategy.done()) {
/* 1583 */         Infer.GraphSolver.InferenceGraph.Node localNode = paramGraphStrategy.pickNode(localInferenceGraph);
/* 1584 */         List localList1 = List.from((Iterable)localNode.data);
/* 1585 */         List localList2 = this.inferenceContext.save();
/*      */         try
/*      */         {
/* 1588 */           if (Type.containsAny(this.inferenceContext.restvars(), localList1))
/*      */           {
/* 1590 */             Infer.GraphInferenceSteps[] arrayOfGraphInferenceSteps = Infer.GraphInferenceSteps.values(); int i = arrayOfGraphInferenceSteps.length; for (int j = 0; ; j++) { if (j >= i) break label149; Infer.GraphInferenceSteps localGraphInferenceSteps = arrayOfGraphInferenceSteps[j];
/* 1591 */               if (Infer.InferenceContext.access$600(this.inferenceContext, localList1, localGraphInferenceSteps.steps)) {
/* 1592 */                 Infer.this.checkWithinBounds(this.inferenceContext, this.warn);
/* 1593 */                 break;
/*      */               }
/*      */             }
/*      */ 
/* 1597 */             label149: throw Infer.this.inferenceException.setMessage();
/*      */           }
/*      */         }
/*      */         catch (Infer.InferenceException localInferenceException)
/*      */         {
/* 1602 */           this.inferenceContext.rollback(localList2);
/* 1603 */           Infer.this.instantiateAsUninferredVars(localList1, this.inferenceContext);
/* 1604 */           Infer.this.checkWithinBounds(this.inferenceContext, this.warn);
/*      */         }
/* 1606 */         localInferenceGraph.deleteNode(localNode);
/*      */       }
/*      */     }
/*      */ 
/*      */     class InferenceGraph
/*      */     {
/*      */       ArrayList<Node> nodes;
/*      */ 
/*      */       InferenceGraph()
/*      */       {
/*      */         Map localMap;
/* 1801 */         initNodes(localMap);
/*      */       }
/*      */ 
/*      */       public Node findNode(Type paramType)
/*      */       {
/* 1809 */         for (Node localNode : this.nodes) {
/* 1810 */           if (((ListBuffer)localNode.data).contains(paramType)) {
/* 1811 */             return localNode;
/*      */           }
/*      */         }
/* 1814 */         return null;
/*      */       }
/*      */ 
/*      */       public void deleteNode(Node paramNode)
/*      */       {
/* 1822 */         Assert.check(this.nodes.contains(paramNode));
/* 1823 */         this.nodes.remove(paramNode);
/* 1824 */         notifyUpdate(paramNode, null);
/*      */       }
/*      */ 
/*      */       void notifyUpdate(Node paramNode1, Node paramNode2)
/*      */       {
/* 1832 */         for (Node localNode : this.nodes)
/* 1833 */           localNode.graphChanged(paramNode1, paramNode2);
/*      */       }
/*      */ 
/*      */       void initNodes(Map<Type, Set<Type>> paramMap)
/*      */       {
/* 1845 */         this.nodes = new ArrayList();
/* 1846 */         for (Object localObject1 = Infer.GraphSolver.this.inferenceContext.restvars().iterator(); ((Iterator)localObject1).hasNext(); ) { localObject2 = (Type)((Iterator)localObject1).next();
/* 1847 */           this.nodes.add(new Node((Type)localObject2));
/*      */         }
/*      */ 
/* 1850 */         for (localObject1 = this.nodes.iterator(); ((Iterator)localObject1).hasNext(); ) { localObject2 = (Node)((Iterator)localObject1).next();
/* 1851 */           localObject3 = (Type)((ListBuffer)((Node)localObject2).data).first();
/* 1852 */           localObject4 = (Set)paramMap.get(localObject3);
/* 1853 */           for (localIterator = this.nodes.iterator(); localIterator.hasNext(); ) { localNode = (Node)localIterator.next();
/* 1854 */             Type localType = (Type)((ListBuffer)localNode.data).first();
/* 1855 */             Type.UndetVar localUndetVar = (Type.UndetVar)Infer.GraphSolver.this.inferenceContext.asUndetVar((Type)localObject3);
/* 1856 */             if (Type.containsAny(localUndetVar.getBounds(Type.UndetVar.InferenceBound.values()), List.of(localType)))
/*      */             {
/* 1858 */               ((Node)localObject2).addDependency(Infer.DependencyKind.BOUND, localNode);
/*      */             }
/* 1860 */             if ((localObject4 != null) && (((Set)localObject4).contains(localType)))
/*      */             {
/* 1862 */               ((Node)localObject2).addDependency(Infer.DependencyKind.STUCK, localNode);
/*      */             }
/*      */           }
/*      */         }
/* 1868 */         Object localObject3;
/*      */         Object localObject4;
/*      */         Iterator localIterator;
/*      */         Node localNode;
/* 1867 */         localObject1 = new ArrayList();
/* 1868 */         for (Object localObject2 = GraphUtils.tarjan(this.nodes).iterator(); ((Iterator)localObject2).hasNext(); ) { localObject3 = (List)((Iterator)localObject2).next();
/* 1869 */           if (((List)localObject3).length() > 1) {
/* 1870 */             localObject4 = (Node)((List)localObject3).head;
/* 1871 */             ((Node)localObject4).mergeWith(((List)localObject3).tail);
/* 1872 */             for (localIterator = ((List)localObject3).iterator(); localIterator.hasNext(); ) { localNode = (Node)localIterator.next();
/* 1873 */               notifyUpdate(localNode, (Node)localObject4);
/*      */             }
/*      */           }
/* 1876 */           ((ArrayList)localObject1).add(((List)localObject3).head);
/*      */         }
/* 1878 */         this.nodes = ((ArrayList)localObject1);
/*      */       }
/*      */ 
/*      */       String toDot()
/*      */       {
/* 1885 */         StringBuilder localStringBuilder = new StringBuilder();
/* 1886 */         for (Type localType : Infer.GraphSolver.this.inferenceContext.undetvars) {
/* 1887 */           Type.UndetVar localUndetVar = (Type.UndetVar)localType;
/* 1888 */           localStringBuilder.append(String.format("var %s - upper bounds = %s, lower bounds = %s, eq bounds = %s\\n", new Object[] { localUndetVar.qtype, localUndetVar
/* 1889 */             .getBounds(new Type.UndetVar.InferenceBound[] { Type.UndetVar.InferenceBound.UPPER }), 
/* 1889 */             localUndetVar.getBounds(new Type.UndetVar.InferenceBound[] { Type.UndetVar.InferenceBound.LOWER }), localUndetVar
/* 1890 */             .getBounds(new Type.UndetVar.InferenceBound[] { Type.UndetVar.InferenceBound.EQ }) }));
/*      */         }
/*      */ 
/* 1892 */         return GraphUtils.toDot(this.nodes, "inferenceGraph" + hashCode(), localStringBuilder.toString());
/*      */       }
/*      */ 
/*      */       class Node extends GraphUtils.TarjanNode<ListBuffer<Type>>
/*      */       {
/*      */         EnumMap<Infer.DependencyKind, Set<Node>> deps;
/*      */ 
/*      */         Node(Type arg2)
/*      */         {
/* 1630 */           super();
/* 1631 */           this.deps = new EnumMap(Infer.DependencyKind.class);
/*      */         }
/*      */ 
/*      */         public GraphUtils.DependencyKind[] getSupportedDependencyKinds()
/*      */         {
/* 1636 */           return Infer.DependencyKind.values();
/*      */         }
/*      */ 
/*      */         public String getDependencyName(GraphUtils.Node<ListBuffer<Type>> paramNode, GraphUtils.DependencyKind paramDependencyKind)
/*      */         {
/* 1641 */           if (paramDependencyKind == Infer.DependencyKind.STUCK) return "";
/*      */ 
/* 1643 */           StringBuilder localStringBuilder = new StringBuilder();
/* 1644 */           String str = "";
/* 1645 */           for (Type localType1 : (ListBuffer)this.data) {
/* 1646 */             Type.UndetVar localUndetVar = (Type.UndetVar)Infer.GraphSolver.this.inferenceContext.asUndetVar(localType1);
/* 1647 */             for (Type localType2 : localUndetVar.getBounds(Type.UndetVar.InferenceBound.values())) {
/* 1648 */               if (localType2.containsAny(List.from((Iterable)paramNode.data))) {
/* 1649 */                 localStringBuilder.append(str);
/* 1650 */                 localStringBuilder.append(localType2);
/* 1651 */                 str = ",";
/*      */               }
/*      */             }
/*      */           }
/* 1655 */           return localStringBuilder.toString();
/*      */         }
/*      */ 
/*      */         public Iterable<? extends Node> getAllDependencies()
/*      */         {
/* 1661 */           return getDependencies(Infer.DependencyKind.values());
/*      */         }
/*      */ 
/*      */         public Iterable<? extends GraphUtils.TarjanNode<ListBuffer<Type>>> getDependenciesByKind(GraphUtils.DependencyKind paramDependencyKind)
/*      */         {
/* 1666 */           return getDependencies(new Infer.DependencyKind[] { (Infer.DependencyKind)paramDependencyKind });
/*      */         }
/*      */ 
/*      */         protected Set<Node> getDependencies(Infer.DependencyKind[] paramArrayOfDependencyKind)
/*      */         {
/* 1673 */           LinkedHashSet localLinkedHashSet = new LinkedHashSet();
/* 1674 */           for (Infer.DependencyKind localDependencyKind : paramArrayOfDependencyKind) {
/* 1675 */             Set localSet = (Set)this.deps.get(localDependencyKind);
/* 1676 */             if (localSet != null) {
/* 1677 */               localLinkedHashSet.addAll(localSet);
/*      */             }
/*      */           }
/* 1680 */           return localLinkedHashSet;
/*      */         }
/*      */ 
/*      */         protected void addDependency(Infer.DependencyKind paramDependencyKind, Node paramNode)
/*      */         {
/* 1687 */           Object localObject = (Set)this.deps.get(paramDependencyKind);
/* 1688 */           if (localObject == null) {
/* 1689 */             localObject = new LinkedHashSet();
/* 1690 */             this.deps.put(paramDependencyKind, localObject);
/*      */           }
/* 1692 */           ((Set)localObject).add(paramNode);
/*      */         }
/*      */ 
/*      */         protected void addDependencies(Infer.DependencyKind paramDependencyKind, Set<Node> paramSet)
/*      */         {
/* 1699 */           for (Node localNode : paramSet)
/* 1700 */             addDependency(paramDependencyKind, localNode);
/*      */         }
/*      */ 
/*      */         protected Set<Infer.DependencyKind> removeDependency(Node paramNode)
/*      */         {
/* 1708 */           HashSet localHashSet = new HashSet();
/* 1709 */           for (Infer.DependencyKind localDependencyKind : Infer.DependencyKind.values()) {
/* 1710 */             Set localSet = (Set)this.deps.get(localDependencyKind);
/* 1711 */             if ((localSet != null) && 
/* 1712 */               (localSet.remove(paramNode))) {
/* 1713 */               localHashSet.add(localDependencyKind);
/*      */             }
/*      */           }
/* 1716 */           return localHashSet;
/*      */         }
/*      */ 
/*      */         protected Set<Node> closure(Infer.DependencyKind[] paramArrayOfDependencyKind)
/*      */         {
/* 1724 */           boolean bool = true;
/* 1725 */           HashSet localHashSet = new HashSet();
/* 1726 */           localHashSet.add(this);
/* 1727 */           while (bool) {
/* 1728 */             bool = false;
/* 1729 */             for (Node localNode : new HashSet(localHashSet)) {
/* 1730 */               bool = localHashSet.addAll(localNode.getDependencies(paramArrayOfDependencyKind));
/*      */             }
/*      */           }
/* 1733 */           return localHashSet;
/*      */         }
/*      */ 
/*      */         protected boolean isLeaf()
/*      */         {
/* 1742 */           Set localSet = getDependencies(new Infer.DependencyKind[] { Infer.DependencyKind.BOUND, Infer.DependencyKind.STUCK });
/* 1743 */           if (localSet.isEmpty()) return true;
/* 1744 */           for (Node localNode : localSet) {
/* 1745 */             if (localNode != this) {
/* 1746 */               return false;
/*      */             }
/*      */           }
/* 1749 */           return true;
/*      */         }
/*      */ 
/*      */         protected void mergeWith(List<? extends Node> paramList)
/*      */         {
/* 1758 */           for (Object localObject1 = paramList.iterator(); ((Iterator)localObject1).hasNext(); ) { localObject2 = (Node)((Iterator)localObject1).next();
/* 1759 */             Assert.check(((ListBuffer)((Node)localObject2).data).length() == 1, "Attempt to merge a compound node!");
/* 1760 */             ((ListBuffer)this.data).appendList((ListBuffer)((Node)localObject2).data);
/* 1761 */             for (localObject4 : Infer.DependencyKind.values())
/* 1762 */               addDependencies((Infer.DependencyKind)localObject4, ((Node)localObject2).getDependencies(new Infer.DependencyKind[] { localObject4 }));
/*      */           }
/*      */           Object localObject2;
/*      */           Object localObject4;
/* 1766 */           localObject1 = new EnumMap(Infer.DependencyKind.class);
/*      */           Object localObject3;
/* 1767 */           for (localObject3 : Infer.DependencyKind.values()) {
/* 1768 */             for (localObject4 = getDependencies(new Infer.DependencyKind[] { localObject3 }).iterator(); ((Iterator)localObject4).hasNext(); ) { Node localNode = (Node)((Iterator)localObject4).next();
/* 1769 */               Object localObject5 = (Set)((EnumMap)localObject1).get(localObject3);
/* 1770 */               if (localObject5 == null) {
/* 1771 */                 localObject5 = new LinkedHashSet();
/* 1772 */                 ((EnumMap)localObject1).put(localObject3, localObject5);
/*      */               }
/* 1774 */               if (((ListBuffer)this.data).contains(((ListBuffer)localNode.data).first()))
/* 1775 */                 ((Set)localObject5).add(this);
/*      */               else {
/* 1777 */                 ((Set)localObject5).add(localNode);
/*      */               }
/*      */             }
/*      */           }
/* 1781 */           this.deps = ((EnumMap)localObject1);
/*      */         }
/*      */ 
/*      */         private void graphChanged(Node paramNode1, Node paramNode2)
/*      */         {
/* 1789 */           for (Infer.DependencyKind localDependencyKind : removeDependency(paramNode1))
/* 1790 */             if (paramNode2 != null)
/* 1791 */               addDependency(localDependencyKind, paramNode2);
/*      */         }
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   static abstract interface GraphStrategy
/*      */   {
/*      */     public abstract Infer.GraphSolver.InferenceGraph.Node pickNode(Infer.GraphSolver.InferenceGraph paramInferenceGraph)
/*      */       throws Infer.GraphStrategy.NodeNotFoundException;
/*      */ 
/*      */     public abstract boolean done();
/*      */ 
/*      */     public static class NodeNotFoundException extends RuntimeException
/*      */     {
/*      */       private static final long serialVersionUID = 0L;
/*      */       Infer.GraphSolver.InferenceGraph graph;
/*      */ 
/*      */       public NodeNotFoundException(Infer.GraphSolver.InferenceGraph paramInferenceGraph)
/*      */       {
/* 1194 */         this.graph = paramInferenceGraph;
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   class ImplicitArgType extends DeferredAttr.DeferredTypeMap
/*      */   {
/*      */     public ImplicitArgType(Symbol paramMethodResolutionPhase, Resolve.MethodResolutionPhase arg3)
/*      */     {
/*  451 */       super(DeferredAttr.AttrMode.SPECULATIVE, paramMethodResolutionPhase, localMethodResolutionPhase);
/*      */     }
/*      */ 
/*      */     public Type apply(Type paramType) {
/*  455 */       paramType = Infer.this.types.erasure(super.apply(paramType));
/*  456 */       if (paramType.hasTag(TypeTag.BOT))
/*      */       {
/*  459 */         paramType = Infer.this.types.boxedClass(Infer.this.syms.voidType).type;
/*  460 */       }return paramType;
/*      */     }
/*      */   }
/*      */ 
/*      */   class IncorporationBinaryOp
/*      */   {
/*      */     Infer.IncorporationBinaryOpKind opKind;
/*      */     Type op1;
/*      */     Type op2;
/*      */ 
/*      */     IncorporationBinaryOp(Infer.IncorporationBinaryOpKind paramType1, Type paramType2, Type arg4)
/*      */     {
/* 1031 */       this.opKind = paramType1;
/* 1032 */       this.op1 = paramType2;
/*      */       Object localObject;
/* 1033 */       this.op2 = localObject;
/*      */     }
/*      */ 
/*      */     public boolean equals(Object paramObject)
/*      */     {
/* 1038 */       if (!(paramObject instanceof IncorporationBinaryOp)) {
/* 1039 */         return false;
/*      */       }
/* 1041 */       IncorporationBinaryOp localIncorporationBinaryOp = (IncorporationBinaryOp)paramObject;
/*      */ 
/* 1044 */       return (this.opKind == localIncorporationBinaryOp.opKind) && 
/* 1043 */         (Infer.this.types
/* 1043 */         .isSameType(this.op1, localIncorporationBinaryOp.op1, true)) && 
/* 1044 */         (Infer.this.types
/* 1044 */         .isSameType(this.op2, localIncorporationBinaryOp.op2, true));
/*      */     }
/*      */ 
/*      */     public int hashCode()
/*      */     {
/* 1050 */       int i = this.opKind.hashCode();
/* 1051 */       i *= 127;
/* 1052 */       i += Infer.this.types.hashCode(this.op1);
/* 1053 */       i *= 127;
/* 1054 */       i += Infer.this.types.hashCode(this.op2);
/* 1055 */       return i;
/*      */     }
/*      */ 
/*      */     boolean apply(Warner paramWarner) {
/* 1059 */       return this.opKind.apply(this.op1, this.op2, paramWarner, Infer.this.types);
/*      */     }
/*      */   }
/*      */ 
/*      */   static abstract enum IncorporationBinaryOpKind
/*      */   {
/*  977 */     IS_SUBTYPE, 
/*      */ 
/*  983 */     IS_SAME_TYPE, 
/*      */ 
/*  989 */     ADD_UPPER_BOUND, 
/*      */ 
/*  997 */     ADD_LOWER_BOUND, 
/*      */ 
/* 1005 */     ADD_EQ_BOUND;
/*      */ 
/*      */     abstract boolean apply(Type paramType1, Type paramType2, Warner paramWarner, Types paramTypes);
/*      */   }
/*      */ 
/*      */   static abstract enum IncorporationStep
/*      */   {
/*  645 */     CHECK_BOUNDS, 
/*      */ 
/*  681 */     EQ_CHECK_LEGACY, 
/*      */ 
/*  709 */     EQ_CHECK, 
/*      */ 
/*  731 */     CROSS_UPPER_LOWER, 
/*      */ 
/*  745 */     CROSS_UPPER_EQ, 
/*      */ 
/*  759 */     CROSS_EQ_LOWER, 
/*      */ 
/*  774 */     CROSS_UPPER_UPPER, 
/*      */ 
/*  823 */     CROSS_EQ_EQ, 
/*      */ 
/*  839 */     PROP_UPPER, 
/*      */ 
/*  865 */     PROP_LOWER, 
/*      */ 
/*  891 */     PROP_EQ;
/*      */ 
/*      */     abstract void apply(Type.UndetVar paramUndetVar, Infer.InferenceContext paramInferenceContext, Warner paramWarner);
/*      */ 
/*      */     boolean accepts(Type.UndetVar paramUndetVar, Infer.InferenceContext paramInferenceContext)
/*      */     {
/*  925 */       return !paramUndetVar.isCaptured();
/*      */     }
/*      */ 
/*      */     boolean isSubtype(Type paramType1, Type paramType2, Warner paramWarner, Infer paramInfer) {
/*  929 */       return doIncorporationOp(Infer.IncorporationBinaryOpKind.IS_SUBTYPE, paramType1, paramType2, paramWarner, paramInfer);
/*      */     }
/*      */ 
/*      */     boolean isSameType(Type paramType1, Type paramType2, Infer paramInfer) {
/*  933 */       return doIncorporationOp(Infer.IncorporationBinaryOpKind.IS_SAME_TYPE, paramType1, paramType2, null, paramInfer);
/*      */     }
/*      */ 
/*      */     void addBound(Type.UndetVar.InferenceBound paramInferenceBound, Type.UndetVar paramUndetVar, Type paramType, Infer paramInfer) {
/*  937 */       doIncorporationOp(opFor(paramInferenceBound), paramUndetVar, paramType, null, paramInfer);
/*      */     }
/*      */ 
/*      */     Infer.IncorporationBinaryOpKind opFor(Type.UndetVar.InferenceBound paramInferenceBound) {
/*  941 */       switch (Infer.1.$SwitchMap$com$sun$tools$javac$code$Type$UndetVar$InferenceBound[paramInferenceBound.ordinal()]) {
/*      */       case 1:
/*  943 */         return Infer.IncorporationBinaryOpKind.ADD_EQ_BOUND;
/*      */       case 2:
/*  945 */         return Infer.IncorporationBinaryOpKind.ADD_LOWER_BOUND;
/*      */       case 3:
/*  947 */         return Infer.IncorporationBinaryOpKind.ADD_UPPER_BOUND;
/*      */       }
/*  949 */       Assert.error("Can't get here!");
/*  950 */       return null;
/*      */     }
/*      */ 
/*      */     boolean doIncorporationOp(Infer.IncorporationBinaryOpKind paramIncorporationBinaryOpKind, Type paramType1, Type paramType2, Warner paramWarner, Infer paramInfer)
/*      */     {
/*      */       Infer tmp6_4 = paramInfer; tmp6_4.getClass(); Infer.IncorporationBinaryOp localIncorporationBinaryOp = new Infer.IncorporationBinaryOp(tmp6_4, paramIncorporationBinaryOpKind, paramType1, paramType2);
/*  956 */       Boolean localBoolean = (Boolean)paramInfer.incorporationCache.get(localIncorporationBinaryOp);
/*  957 */       if (localBoolean == null) {
/*  958 */         paramInfer.incorporationCache.put(localIncorporationBinaryOp, localBoolean = Boolean.valueOf(localIncorporationBinaryOp.apply(paramWarner)));
/*      */       }
/*  960 */       return localBoolean.booleanValue();
/*      */     }
/*      */   }
/*      */ 
/*      */   class InferenceContext
/*      */   {
/*      */     List<Type> undetvars;
/*      */     List<Type> inferencevars;
/* 1924 */     Map<Infer.FreeTypeListener, List<Type>> freeTypeListeners = new HashMap();
/*      */ 
/* 1927 */     List<Infer.FreeTypeListener> freetypeListeners = List.nil();
/*      */ 
/* 1934 */     Type.Mapping fromTypeVarFun = new Type.Mapping("fromTypeVarFunWithBounds")
/*      */     {
/*      */       public Type apply(Type paramAnonymousType) {
/* 1937 */         if (paramAnonymousType.hasTag(TypeTag.TYPEVAR)) {
/* 1938 */           Type.TypeVar localTypeVar = (Type.TypeVar)paramAnonymousType;
/* 1939 */           if (localTypeVar.isCaptured()) {
/* 1940 */             return new Type.CapturedUndetVar((Type.CapturedType)localTypeVar, Infer.this.types);
/*      */           }
/* 1942 */           return new Type.UndetVar(localTypeVar, Infer.this.types);
/*      */         }
/*      */ 
/* 1945 */         return paramAnonymousType.map(this);
/*      */       }
/* 1934 */     };
/*      */ 
/* 2310 */     Map<JCTree, Type> captureTypeCache = new HashMap();
/*      */ 
/*      */     public InferenceContext()
/*      */     {
/*      */       List localList;
/* 1930 */       this.undetvars = Type.map(localList, this.fromTypeVarFun);
/* 1931 */       this.inferencevars = localList;
/*      */     }
/*      */ 
/*      */     void addVar(Type.TypeVar paramTypeVar)
/*      */     {
/* 1954 */       this.undetvars = this.undetvars.prepend(this.fromTypeVarFun.apply(paramTypeVar));
/* 1955 */       this.inferencevars = this.inferencevars.prepend(paramTypeVar);
/*      */     }
/*      */ 
/*      */     List<Type> inferenceVars()
/*      */     {
/* 1963 */       return this.inferencevars;
/*      */     }
/*      */ 
/*      */     List<Type> restvars()
/*      */     {
/* 1971 */       return filterVars(new Filter() {
/*      */         public boolean accepts(Type.UndetVar paramAnonymousUndetVar) {
/* 1973 */           return paramAnonymousUndetVar.inst == null;
/*      */         }
/*      */       });
/*      */     }
/*      */ 
/*      */     List<Type> instvars()
/*      */     {
/* 1983 */       return filterVars(new Filter() {
/*      */         public boolean accepts(Type.UndetVar paramAnonymousUndetVar) {
/* 1985 */           return paramAnonymousUndetVar.inst != null;
/*      */         }
/*      */       });
/*      */     }
/*      */ 
/*      */     final List<Type> boundedVars()
/*      */     {
/* 1995 */       return filterVars(new Filter()
/*      */       {
/*      */         public boolean accepts(Type.UndetVar paramAnonymousUndetVar)
/*      */         {
/* 1999 */           return paramAnonymousUndetVar.getBounds(new Type.UndetVar.InferenceBound[] { Type.UndetVar.InferenceBound.UPPER })
/* 1998 */             .diff(paramAnonymousUndetVar
/* 1998 */             .getDeclaredBounds())
/* 1999 */             .appendList(paramAnonymousUndetVar
/* 1999 */             .getBounds(new Type.UndetVar.InferenceBound[] { Type.UndetVar.InferenceBound.EQ, Type.UndetVar.InferenceBound.LOWER }))
/* 1999 */             .nonEmpty();
/*      */         }
/*      */       });
/*      */     }
/*      */ 
/*      */     private List<Type> filterVars(Filter<Type.UndetVar> paramFilter)
/*      */     {
/* 2007 */       ListBuffer localListBuffer = new ListBuffer();
/* 2008 */       for (Type localType : this.undetvars) {
/* 2009 */         Type.UndetVar localUndetVar = (Type.UndetVar)localType;
/* 2010 */         if (paramFilter.accepts(localUndetVar)) {
/* 2011 */           localListBuffer.append(localUndetVar.qtype);
/*      */         }
/*      */       }
/* 2014 */       return localListBuffer.toList();
/*      */     }
/*      */ 
/*      */     final boolean free(Type paramType)
/*      */     {
/* 2021 */       return paramType.containsAny(this.inferencevars);
/*      */     }
/*      */ 
/*      */     final boolean free(List<Type> paramList) {
/* 2025 */       for (Type localType : paramList) {
/* 2026 */         if (free(localType)) return true;
/*      */       }
/* 2028 */       return false;
/*      */     }
/*      */ 
/*      */     final List<Type> freeVarsIn(Type paramType)
/*      */     {
/* 2035 */       ListBuffer localListBuffer = new ListBuffer();
/* 2036 */       for (Type localType : inferenceVars()) {
/* 2037 */         if (paramType.contains(localType)) {
/* 2038 */           localListBuffer.add(localType);
/*      */         }
/*      */       }
/* 2041 */       return localListBuffer.toList();
/*      */     }
/*      */ 
/*      */     final List<Type> freeVarsIn(List<Type> paramList) {
/* 2045 */       ListBuffer localListBuffer = new ListBuffer();
/* 2046 */       for (Object localObject1 = paramList.iterator(); ((Iterator)localObject1).hasNext(); ) { localObject2 = (Type)((Iterator)localObject1).next();
/* 2047 */         localListBuffer.appendList(freeVarsIn((Type)localObject2));
/*      */       }
/* 2049 */       localObject1 = new ListBuffer();
/* 2050 */       for (Object localObject2 = localListBuffer.iterator(); ((Iterator)localObject2).hasNext(); ) { Type localType = (Type)((Iterator)localObject2).next();
/* 2051 */         if (!((ListBuffer)localObject1).contains(localType)) {
/* 2052 */           ((ListBuffer)localObject1).add(localType);
/*      */         }
/*      */       }
/* 2055 */       return ((ListBuffer)localObject1).toList();
/*      */     }
/*      */ 
/*      */     final Type asUndetVar(Type paramType)
/*      */     {
/* 2064 */       return Infer.this.types.subst(paramType, this.inferencevars, this.undetvars);
/*      */     }
/*      */ 
/*      */     final List<Type> asUndetVars(List<Type> paramList) {
/* 2068 */       ListBuffer localListBuffer = new ListBuffer();
/* 2069 */       for (Type localType : paramList) {
/* 2070 */         localListBuffer.append(asUndetVar(localType));
/*      */       }
/* 2072 */       return localListBuffer.toList();
/*      */     }
/*      */ 
/*      */     List<Type> instTypes() {
/* 2076 */       ListBuffer localListBuffer = new ListBuffer();
/* 2077 */       for (Type localType : this.undetvars) {
/* 2078 */         Type.UndetVar localUndetVar = (Type.UndetVar)localType;
/* 2079 */         localListBuffer.append(localUndetVar.inst != null ? localUndetVar.inst : localUndetVar.qtype);
/*      */       }
/* 2081 */       return localListBuffer.toList();
/*      */     }
/*      */ 
/*      */     Type asInstType(Type paramType)
/*      */     {
/* 2090 */       return Infer.this.types.subst(paramType, this.inferencevars, instTypes());
/*      */     }
/*      */ 
/*      */     List<Type> asInstTypes(List<Type> paramList) {
/* 2094 */       ListBuffer localListBuffer = new ListBuffer();
/* 2095 */       for (Type localType : paramList) {
/* 2096 */         localListBuffer.append(asInstType(localType));
/*      */       }
/* 2098 */       return localListBuffer.toList();
/*      */     }
/*      */ 
/*      */     void addFreeTypeListener(List<Type> paramList, Infer.FreeTypeListener paramFreeTypeListener)
/*      */     {
/* 2105 */       this.freeTypeListeners.put(paramFreeTypeListener, freeVarsIn(paramList));
/*      */     }
/*      */ 
/*      */     void notifyChange()
/*      */     {
/* 2113 */       notifyChange(this.inferencevars.diff(restvars()));
/*      */     }
/*      */ 
/*      */     void notifyChange(List<Type> paramList) {
/* 2117 */       Object localObject = null;
/*      */ 
/* 2119 */       for (Map.Entry localEntry : new HashMap(this.freeTypeListeners).entrySet()) {
/* 2120 */         if (!Type.containsAny((List)localEntry.getValue(), this.inferencevars.diff(paramList))) {
/*      */           try {
/* 2122 */             ((Infer.FreeTypeListener)localEntry.getKey()).typesInferred(this);
/* 2123 */             this.freeTypeListeners.remove(localEntry.getKey());
/*      */           } catch (Infer.InferenceException localInferenceException) {
/* 2125 */             if (localObject == null) {
/* 2126 */               localObject = localInferenceException;
/*      */             }
/*      */           }
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/* 2133 */       if (localObject != null)
/* 2134 */         throw localObject;
/*      */     }
/*      */ 
/*      */     List<Type> save()
/*      */     {
/* 2142 */       ListBuffer localListBuffer = new ListBuffer();
/* 2143 */       for (Type localType1 : this.undetvars) {
/* 2144 */         Type.UndetVar localUndetVar1 = (Type.UndetVar)localType1;
/* 2145 */         Type.UndetVar localUndetVar2 = new Type.UndetVar((Type.TypeVar)localUndetVar1.qtype, Infer.this.types);
/*      */         Type.UndetVar.InferenceBound localInferenceBound;
/* 2146 */         for (localInferenceBound : Type.UndetVar.InferenceBound.values()) {
/* 2147 */           for (Type localType2 : localUndetVar1.getBounds(new Type.UndetVar.InferenceBound[] { localInferenceBound })) {
/* 2148 */             localUndetVar2.addBound(localInferenceBound, localType2, Infer.this.types);
/*      */           }
/*      */         }
/* 2151 */         localUndetVar2.inst = localUndetVar1.inst;
/* 2152 */         localListBuffer.add(localUndetVar2);
/*      */       }
/* 2154 */       return localListBuffer.toList();
/*      */     }
/*      */ 
/*      */     void rollback(List<Type> paramList)
/*      */     {
/* 2161 */       Assert.check((paramList != null) && (paramList.length() == this.undetvars.length()));
/*      */ 
/* 2163 */       for (Type localType : this.undetvars) {
/* 2164 */         Type.UndetVar localUndetVar1 = (Type.UndetVar)localType;
/* 2165 */         Type.UndetVar localUndetVar2 = (Type.UndetVar)paramList.head;
/* 2166 */         for (Type.UndetVar.InferenceBound localInferenceBound : Type.UndetVar.InferenceBound.values()) {
/* 2167 */           localUndetVar1.setBounds(localInferenceBound, localUndetVar2.getBounds(new Type.UndetVar.InferenceBound[] { localInferenceBound }));
/*      */         }
/* 2169 */         localUndetVar1.inst = localUndetVar2.inst;
/* 2170 */         paramList = paramList.tail;
/*      */       }
/*      */     }
/*      */ 
/*      */     void dupTo(InferenceContext paramInferenceContext)
/*      */     {
/* 2178 */       paramInferenceContext.inferencevars = paramInferenceContext.inferencevars.appendList(this.inferencevars
/* 2179 */         .diff(paramInferenceContext.inferencevars));
/*      */ 
/* 2180 */       paramInferenceContext.undetvars = paramInferenceContext.undetvars.appendList(this.undetvars
/* 2181 */         .diff(paramInferenceContext.undetvars));
/*      */ 
/* 2184 */       for (Type localType : this.inferencevars)
/* 2185 */         paramInferenceContext.freeTypeListeners.put(new Infer.FreeTypeListener() {
/*      */           public void typesInferred(Infer.InferenceContext paramAnonymousInferenceContext) {
/* 2187 */             Infer.InferenceContext.this.notifyChange();
/*      */           }
/*      */         }
/*      */         , List.of(localType));
/*      */     }
/*      */ 
/*      */     private void solve(Infer.GraphStrategy paramGraphStrategy, Warner paramWarner)
/*      */     {
/* 2194 */       solve(paramGraphStrategy, new HashMap(), paramWarner);
/*      */     }
/*      */ 
/*      */     private void solve(Infer.GraphStrategy paramGraphStrategy, Map<Type, Set<Type>> paramMap, Warner paramWarner)
/*      */     {
/* 2201 */       Infer.GraphSolver localGraphSolver = new Infer.GraphSolver(Infer.this, this, paramMap, paramWarner);
/* 2202 */       localGraphSolver.solve(paramGraphStrategy); } 
/*      */     public void solve(Warner paramWarner) { // Byte code:
/*      */       //   0: aload_0
/*      */       //   1: new 144	com/sun/tools/javac/comp/Infer$InferenceContext$6
/*      */       //   4: dup
/*      */       //   5: aload_0
/*      */       //   6: invokespecial 371	com/sun/tools/javac/comp/Infer$InferenceContext$6:<init>	(Lcom/sun/tools/javac/comp/Infer$InferenceContext;)V
/*      */       //   9: aload_1
/*      */       //   10: invokespecial 363	com/sun/tools/javac/comp/Infer$InferenceContext:solve	(Lcom/sun/tools/javac/comp/Infer$GraphStrategy;Lcom/sun/tools/javac/util/Warner;)V
/*      */       //   13: return } 
/* 2220 */     public void solve(final List<Type> paramList, Warner paramWarner) { solve(new Infer.BestLeafSolver(paramList, paramList) {
/*      */         public boolean done() {
/* 2222 */           return !Infer.InferenceContext.this.free(Infer.InferenceContext.this.asInstTypes(paramList));
/*      */         }
/*      */       }
/*      */       , paramWarner); } 
/*      */     public void solveAny(List<Type> paramList, Map<Type, Set<Type>> paramMap, Warner paramWarner) { // Byte code:
/*      */       //   0: aload_0
/*      */       //   1: new 146	com/sun/tools/javac/comp/Infer$InferenceContext$8
/*      */       //   4: dup
/*      */       //   5: aload_0
/*      */       //   6: aload_1
/*      */       //   7: aload_0
/*      */       //   8: invokevirtual 355	com/sun/tools/javac/comp/Infer$InferenceContext:restvars	()Lcom/sun/tools/javac/util/List;
/*      */       //   11: invokevirtual 383	com/sun/tools/javac/util/List:intersect	(Lcom/sun/tools/javac/util/List;)Lcom/sun/tools/javac/util/List;
/*      */       //   14: invokespecial 373	com/sun/tools/javac/comp/Infer$InferenceContext$8:<init>	(Lcom/sun/tools/javac/comp/Infer$InferenceContext;Lcom/sun/tools/javac/util/List;)V
/*      */       //   17: aload_2
/*      */       //   18: aload_3
/*      */       //   19: invokespecial 365	com/sun/tools/javac/comp/Infer$InferenceContext:solve	(Lcom/sun/tools/javac/comp/Infer$GraphStrategy;Ljava/util/Map;Lcom/sun/tools/javac/util/Warner;)V
/*      */       //   22: return } 
/* 2242 */     private boolean solveBasic(EnumSet<Infer.InferenceStep> paramEnumSet) { return solveBasic(this.inferencevars, paramEnumSet); }
/*      */ 
/*      */     private boolean solveBasic(List<Type> paramList, EnumSet<Infer.InferenceStep> paramEnumSet)
/*      */     {
/* 2246 */       boolean bool = false;
/* 2247 */       for (Type localType : paramList.intersect(restvars())) {
/* 2248 */         localUndetVar = (Type.UndetVar)asUndetVar(localType);
/* 2249 */         for (Infer.InferenceStep localInferenceStep : paramEnumSet)
/* 2250 */           if (localInferenceStep.accepts(localUndetVar, this)) {
/* 2251 */             localUndetVar.inst = localInferenceStep.solve(localUndetVar, this);
/* 2252 */             bool = true;
/* 2253 */             break;
/*      */           }
/*      */       }
/*      */       Type.UndetVar localUndetVar;
/* 2257 */       return bool;
/*      */     }
/*      */ 
/*      */     public void solveLegacy(boolean paramBoolean, Warner paramWarner, EnumSet<Infer.InferenceStep> paramEnumSet)
/*      */     {
/*      */       while (true)
/*      */       {
/* 2269 */         int i = !solveBasic(paramEnumSet) ? 1 : 0;
/* 2270 */         if ((restvars().isEmpty()) || (paramBoolean)) {
/*      */           break;
/*      */         }
/* 2273 */         if (i != 0)
/*      */         {
/* 2276 */           Infer.this.instantiateAsUninferredVars(restvars(), this);
/* 2277 */           break;
/*      */         }
/*      */ 
/* 2281 */         for (Type localType : this.undetvars) {
/* 2282 */           Type.UndetVar localUndetVar = (Type.UndetVar)localType;
/* 2283 */           localUndetVar.substBounds(inferenceVars(), instTypes(), Infer.this.types);
/*      */         }
/*      */       }
/*      */ 
/* 2287 */       Infer.this.checkWithinBounds(this, paramWarner);
/*      */     }
/*      */ 
/*      */     private Infer infer()
/*      */     {
/* 2292 */       return Infer.this;
/*      */     }
/*      */ 
/*      */     public String toString()
/*      */     {
/* 2297 */       return "Inference vars: " + this.inferencevars + '\n' + "Undet vars: " + this.undetvars;
/*      */     }
/*      */ 
/*      */     Type cachedCapture(JCTree paramJCTree, Type paramType, boolean paramBoolean)
/*      */     {
/* 2313 */       Type localType1 = (Type)this.captureTypeCache.get(paramJCTree);
/* 2314 */       if (localType1 != null) {
/* 2315 */         return localType1;
/*      */       }
/*      */ 
/* 2318 */       Type localType2 = Infer.this.types.capture(paramType);
/* 2319 */       if ((localType2 != paramType) && (!paramBoolean)) {
/* 2320 */         this.captureTypeCache.put(paramJCTree, localType2);
/*      */       }
/* 2322 */       return localType2;
/*      */     }
/*      */   }
/*      */ 
/*      */   public static class InferenceException extends Resolve.InapplicableMethodException
/*      */   {
/*      */     private static final long serialVersionUID = 0L;
/*  110 */     List<JCDiagnostic> messages = List.nil();
/*      */ 
/*      */     InferenceException(JCDiagnostic.Factory paramFactory) {
/*  113 */       super();
/*      */     }
/*      */ 
/*      */     Resolve.InapplicableMethodException setMessage()
/*      */     {
/*  119 */       return this;
/*      */     }
/*      */ 
/*      */     Resolve.InapplicableMethodException setMessage(JCDiagnostic paramJCDiagnostic)
/*      */     {
/*  124 */       this.messages = this.messages.append(paramJCDiagnostic);
/*  125 */       return this;
/*      */     }
/*      */ 
/*      */     public JCDiagnostic getDiagnostic()
/*      */     {
/*  130 */       return (JCDiagnostic)this.messages.head;
/*      */     }
/*      */ 
/*      */     void clear() {
/*  134 */       this.messages = List.nil();
/*      */     }
/*      */   }
/*      */ 
/*      */   static abstract enum InferenceStep
/*      */   {
/* 1346 */     EQ(Type.UndetVar.InferenceBound.EQ), 
/*      */ 
/* 1356 */     LOWER(Type.UndetVar.InferenceBound.LOWER), 
/*      */ 
/* 1376 */     THROWS(Type.UndetVar.InferenceBound.UPPER), 
/*      */ 
/* 1409 */     UPPER(Type.UndetVar.InferenceBound.UPPER), 
/*      */ 
/* 1429 */     UPPER_LEGACY(Type.UndetVar.InferenceBound.UPPER), 
/*      */ 
/* 1444 */     CAPTURED(Type.UndetVar.InferenceBound.UPPER);
/*      */ 
/*      */     final Type.UndetVar.InferenceBound ib;
/*      */ 
/*      */     private InferenceStep(Type.UndetVar.InferenceBound paramInferenceBound)
/*      */     {
/* 1468 */       this.ib = paramInferenceBound;
/*      */     }
/*      */ 
/*      */     abstract Type solve(Type.UndetVar paramUndetVar, Infer.InferenceContext paramInferenceContext);
/*      */ 
/*      */     public boolean accepts(Type.UndetVar paramUndetVar, Infer.InferenceContext paramInferenceContext)
/*      */     {
/* 1481 */       return (filterBounds(paramUndetVar, paramInferenceContext).nonEmpty()) && (!paramUndetVar.isCaptured());
/*      */     }
/*      */ 
/*      */     List<Type> filterBounds(Type.UndetVar paramUndetVar, Infer.InferenceContext paramInferenceContext)
/*      */     {
/* 1488 */       return Type.filter(paramUndetVar.getBounds(new Type.UndetVar.InferenceBound[] { this.ib }), new Infer.BoundFilter(paramInferenceContext));
/*      */     }
/*      */   }
/*      */ 
/*      */   abstract class LeafSolver
/*      */     implements Infer.GraphStrategy
/*      */   {
/*      */     LeafSolver()
/*      */     {
/*      */     }
/*      */ 
/*      */     public Infer.GraphSolver.InferenceGraph.Node pickNode(Infer.GraphSolver.InferenceGraph paramInferenceGraph)
/*      */     {
/* 1213 */       if (paramInferenceGraph.nodes.isEmpty())
/*      */       {
/* 1215 */         throw new Infer.GraphStrategy.NodeNotFoundException(paramInferenceGraph);
/*      */       }
/* 1217 */       return (Infer.GraphSolver.InferenceGraph.Node)paramInferenceGraph.nodes.get(0);
/*      */     }
/*      */ 
/*      */     boolean isSubtype(Type paramType1, Type paramType2, Warner paramWarner, Infer paramInfer) {
/* 1221 */       return doIncorporationOp(Infer.IncorporationBinaryOpKind.IS_SUBTYPE, paramType1, paramType2, paramWarner, paramInfer);
/*      */     }
/*      */ 
/*      */     boolean isSameType(Type paramType1, Type paramType2, Infer paramInfer) {
/* 1225 */       return doIncorporationOp(Infer.IncorporationBinaryOpKind.IS_SAME_TYPE, paramType1, paramType2, null, paramInfer);
/*      */     }
/*      */ 
/*      */     void addBound(Type.UndetVar.InferenceBound paramInferenceBound, Type.UndetVar paramUndetVar, Type paramType, Infer paramInfer) {
/* 1229 */       doIncorporationOp(opFor(paramInferenceBound), paramUndetVar, paramType, null, paramInfer);
/*      */     }
/*      */ 
/*      */     Infer.IncorporationBinaryOpKind opFor(Type.UndetVar.InferenceBound paramInferenceBound) {
/* 1233 */       switch (Infer.1.$SwitchMap$com$sun$tools$javac$code$Type$UndetVar$InferenceBound[paramInferenceBound.ordinal()]) {
/*      */       case 1:
/* 1235 */         return Infer.IncorporationBinaryOpKind.ADD_EQ_BOUND;
/*      */       case 2:
/* 1237 */         return Infer.IncorporationBinaryOpKind.ADD_LOWER_BOUND;
/*      */       case 3:
/* 1239 */         return Infer.IncorporationBinaryOpKind.ADD_UPPER_BOUND;
/*      */       }
/* 1241 */       Assert.error("Can't get here!");
/* 1242 */       return null;
/*      */     }
/*      */ 
/*      */     boolean doIncorporationOp(Infer.IncorporationBinaryOpKind paramIncorporationBinaryOpKind, Type paramType1, Type paramType2, Warner paramWarner, Infer paramInfer)
/*      */     {
/*      */       Infer tmp6_4 = paramInfer; tmp6_4.getClass(); Infer.IncorporationBinaryOp localIncorporationBinaryOp = new Infer.IncorporationBinaryOp(tmp6_4, paramIncorporationBinaryOpKind, paramType1, paramType2);
/* 1248 */       Boolean localBoolean = (Boolean)paramInfer.incorporationCache.get(localIncorporationBinaryOp);
/* 1249 */       if (localBoolean == null) {
/* 1250 */         paramInfer.incorporationCache.put(localIncorporationBinaryOp, localBoolean = Boolean.valueOf(localIncorporationBinaryOp.apply(paramWarner)));
/*      */       }
/* 1252 */       return localBoolean.booleanValue();
/*      */     }
/*      */   }
/*      */ 
/*      */   static enum LegacyInferenceSteps
/*      */   {
/* 1499 */     EQ_LOWER(EnumSet.of(Infer.InferenceStep.EQ, Infer.InferenceStep.LOWER)), 
/* 1500 */     EQ_UPPER(EnumSet.of(Infer.InferenceStep.EQ, Infer.InferenceStep.UPPER_LEGACY));
/*      */ 
/*      */     final EnumSet<Infer.InferenceStep> steps;
/*      */ 
/*      */     private LegacyInferenceSteps(EnumSet<Infer.InferenceStep> paramEnumSet) {
/* 1505 */       this.steps = paramEnumSet;
/*      */     }
/*      */   }
/*      */ 
/*      */   class MultiUndetVarListener
/*      */     implements Type.UndetVar.UndetVarListener
/*      */   {
/*      */     boolean changed;
/*      */     List<Type> undetvars;
/*      */ 
/*      */     public MultiUndetVarListener()
/*      */     {
/*      */       Object localObject;
/*  580 */       this.undetvars = localObject;
/*  581 */       for (Type localType : localObject) {
/*  582 */         Type.UndetVar localUndetVar = (Type.UndetVar)localType;
/*  583 */         localUndetVar.listener = this;
/*      */       }
/*      */     }
/*      */ 
/*      */     public void varChanged(Type.UndetVar paramUndetVar, Set<Type.UndetVar.InferenceBound> paramSet)
/*      */     {
/*  589 */       if (Infer.this.incorporationCache.size() < 100)
/*  590 */         this.changed = true;
/*      */     }
/*      */ 
/*      */     void reset()
/*      */     {
/*  595 */       this.changed = false;
/*      */     }
/*      */ 
/*      */     void detach() {
/*  599 */       for (Type localType : this.undetvars) {
/*  600 */         Type.UndetVar localUndetVar = (Type.UndetVar)localType;
/*  601 */         localUndetVar.listener = null;
/*      */       }
/*      */     }
/*      */   }
/*      */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.javac.comp.Infer
 * JD-Core Version:    0.6.2
 */