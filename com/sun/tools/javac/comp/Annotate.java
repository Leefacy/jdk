/*     */ package com.sun.tools.javac.comp;
/*     */ 
/*     */ import com.sun.tools.javac.code.Attribute;
/*     */ import com.sun.tools.javac.code.Attribute.Array;
/*     */ import com.sun.tools.javac.code.Attribute.Class;
/*     */ import com.sun.tools.javac.code.Attribute.Compound;
/*     */ import com.sun.tools.javac.code.Attribute.Constant;
/*     */ import com.sun.tools.javac.code.Attribute.Enum;
/*     */ import com.sun.tools.javac.code.Attribute.Error;
/*     */ import com.sun.tools.javac.code.Attribute.TypeCompound;
/*     */ import com.sun.tools.javac.code.Attribute.UnresolvedClass;
/*     */ import com.sun.tools.javac.code.Kinds;
/*     */ import com.sun.tools.javac.code.Scope;
/*     */ import com.sun.tools.javac.code.Symbol;
/*     */ import com.sun.tools.javac.code.Symbol.CompletionFailure;
/*     */ import com.sun.tools.javac.code.Symbol.MethodSymbol;
/*     */ import com.sun.tools.javac.code.Symbol.TypeSymbol;
/*     */ import com.sun.tools.javac.code.Symbol.VarSymbol;
/*     */ import com.sun.tools.javac.code.Symtab;
/*     */ import com.sun.tools.javac.code.Type;
/*     */ import com.sun.tools.javac.code.Type.ArrayType;
/*     */ import com.sun.tools.javac.code.TypeAnnotationPosition;
/*     */ import com.sun.tools.javac.code.TypeTag;
/*     */ import com.sun.tools.javac.code.Types;
/*     */ import com.sun.tools.javac.tree.JCTree;
/*     */ import com.sun.tools.javac.tree.JCTree.JCAnnotation;
/*     */ import com.sun.tools.javac.tree.JCTree.JCAssign;
/*     */ import com.sun.tools.javac.tree.JCTree.JCExpression;
/*     */ import com.sun.tools.javac.tree.JCTree.JCFieldAccess;
/*     */ import com.sun.tools.javac.tree.JCTree.JCIdent;
/*     */ import com.sun.tools.javac.tree.JCTree.JCNewArray;
/*     */ import com.sun.tools.javac.tree.JCTree.Tag;
/*     */ import com.sun.tools.javac.tree.TreeInfo;
/*     */ import com.sun.tools.javac.tree.TreeMaker;
/*     */ import com.sun.tools.javac.util.Assert;
/*     */ import com.sun.tools.javac.util.Context;
/*     */ import com.sun.tools.javac.util.Context.Key;
/*     */ import com.sun.tools.javac.util.JCDiagnostic.DiagnosticPosition;
/*     */ import com.sun.tools.javac.util.List;
/*     */ import com.sun.tools.javac.util.ListBuffer;
/*     */ import com.sun.tools.javac.util.Log;
/*     */ import com.sun.tools.javac.util.Name;
/*     */ import com.sun.tools.javac.util.Names;
/*     */ import com.sun.tools.javac.util.Pair;
/*     */ import java.util.Iterator;
/*     */ import java.util.Map;
/*     */ 
/*     */ public class Annotate
/*     */ {
/*  52 */   protected static final Context.Key<Annotate> annotateKey = new Context.Key();
/*     */   final Attr attr;
/*     */   final TreeMaker make;
/*     */   final Log log;
/*     */   final Symtab syms;
/*     */   final Names names;
/*     */   final Resolve rs;
/*     */   final Types types;
/*     */   final ConstFold cfolder;
/*     */   final Check chk;
/*  89 */   private int enterCount = 0;
/*     */ 
/*  91 */   ListBuffer<Worker> q = new ListBuffer();
/*  92 */   ListBuffer<Worker> typesQ = new ListBuffer();
/*  93 */   ListBuffer<Worker> repeatedQ = new ListBuffer();
/*  94 */   ListBuffer<Worker> afterRepeatedQ = new ListBuffer();
/*  95 */   ListBuffer<Worker> validateQ = new ListBuffer();
/*     */ 
/*     */   public static Annotate instance(Context paramContext)
/*     */   {
/*  56 */     Annotate localAnnotate = (Annotate)paramContext.get(annotateKey);
/*  57 */     if (localAnnotate == null)
/*  58 */       localAnnotate = new Annotate(paramContext);
/*  59 */     return localAnnotate;
/*     */   }
/*     */ 
/*     */   protected Annotate(Context paramContext)
/*     */   {
/*  73 */     paramContext.put(annotateKey, this);
/*  74 */     this.attr = Attr.instance(paramContext);
/*  75 */     this.make = TreeMaker.instance(paramContext);
/*  76 */     this.log = Log.instance(paramContext);
/*  77 */     this.syms = Symtab.instance(paramContext);
/*  78 */     this.names = Names.instance(paramContext);
/*  79 */     this.rs = Resolve.instance(paramContext);
/*  80 */     this.types = Types.instance(paramContext);
/*  81 */     this.cfolder = ConstFold.instance(paramContext);
/*  82 */     this.chk = Check.instance(paramContext);
/*     */   }
/*     */ 
/*     */   public void earlier(Worker paramWorker)
/*     */   {
/*  98 */     this.q.prepend(paramWorker);
/*     */   }
/*     */ 
/*     */   public void normal(Worker paramWorker) {
/* 102 */     this.q.append(paramWorker);
/*     */   }
/*     */ 
/*     */   public void typeAnnotation(Worker paramWorker) {
/* 106 */     this.typesQ.append(paramWorker);
/*     */   }
/*     */ 
/*     */   public void repeated(Worker paramWorker) {
/* 110 */     this.repeatedQ.append(paramWorker);
/*     */   }
/*     */ 
/*     */   public void afterRepeated(Worker paramWorker) {
/* 114 */     this.afterRepeatedQ.append(paramWorker);
/*     */   }
/*     */ 
/*     */   public void validate(Worker paramWorker) {
/* 118 */     this.validateQ.append(paramWorker);
/*     */   }
/*     */ 
/*     */   public void enterStart()
/*     */   {
/* 123 */     this.enterCount += 1;
/*     */   }
/*     */ 
/*     */   public void enterDone()
/*     */   {
/* 128 */     this.enterCount -= 1;
/* 129 */     flush();
/*     */   }
/*     */ 
/*     */   public void enterDoneWithoutFlush()
/*     */   {
/* 135 */     this.enterCount -= 1;
/*     */   }
/*     */ 
/*     */   public void flush() {
/* 139 */     if (this.enterCount != 0) return;
/* 140 */     this.enterCount += 1;
/*     */     try {
/* 142 */       while (this.q.nonEmpty()) {
/* 143 */         ((Worker)this.q.next()).run();
/*     */       }
/* 145 */       while (this.typesQ.nonEmpty()) {
/* 146 */         ((Worker)this.typesQ.next()).run();
/*     */       }
/* 148 */       while (this.repeatedQ.nonEmpty()) {
/* 149 */         ((Worker)this.repeatedQ.next()).run();
/*     */       }
/* 151 */       while (this.afterRepeatedQ.nonEmpty()) {
/* 152 */         ((Worker)this.afterRepeatedQ.next()).run();
/*     */       }
/* 154 */       while (this.validateQ.nonEmpty()) {
/* 155 */         ((Worker)this.validateQ.next()).run();
/*     */       }
/*     */ 
/* 158 */       this.enterCount -= 1; } finally { this.enterCount -= 1; }
/*     */ 
/*     */   }
/*     */ 
/*     */   Attribute.Compound enterAnnotation(JCTree.JCAnnotation paramJCAnnotation, Type paramType, Env<AttrContext> paramEnv)
/*     */   {
/* 236 */     return enterAnnotation(paramJCAnnotation, paramType, paramEnv, false);
/*     */   }
/*     */ 
/*     */   Attribute.TypeCompound enterTypeAnnotation(JCTree.JCAnnotation paramJCAnnotation, Type paramType, Env<AttrContext> paramEnv)
/*     */   {
/* 242 */     return (Attribute.TypeCompound)enterAnnotation(paramJCAnnotation, paramType, paramEnv, true);
/*     */   }
/*     */ 
/*     */   Attribute.Compound enterAnnotation(JCTree.JCAnnotation paramJCAnnotation, Type paramType, Env<AttrContext> paramEnv, boolean paramBoolean)
/*     */   {
/* 255 */     Type localType1 = paramJCAnnotation.annotationType.type != null ? paramJCAnnotation.annotationType.type : this.attr
/* 255 */       .attribType(paramJCAnnotation.annotationType, paramEnv);
/*     */ 
/* 256 */     paramJCAnnotation.type = this.chk.checkType(paramJCAnnotation.annotationType.pos(), localType1, paramType);
/* 257 */     if (paramJCAnnotation.type.isErroneous())
/*     */     {
/* 259 */       this.attr.postAttr(paramJCAnnotation);
/*     */ 
/* 261 */       if (paramBoolean) {
/* 262 */         return new Attribute.TypeCompound(paramJCAnnotation.type, List.nil(), new TypeAnnotationPosition());
/*     */       }
/*     */ 
/* 265 */       return new Attribute.Compound(paramJCAnnotation.type, List.nil());
/*     */     }
/*     */ 
/* 268 */     if ((paramJCAnnotation.type.tsym.flags() & 0x2000) == 0L) {
/* 269 */       this.log.error(paramJCAnnotation.annotationType.pos(), "not.annotation.type", new Object[] { paramJCAnnotation.type
/* 270 */         .toString() });
/*     */ 
/* 273 */       this.attr.postAttr(paramJCAnnotation);
/*     */ 
/* 275 */       if (paramBoolean) {
/* 276 */         return new Attribute.TypeCompound(paramJCAnnotation.type, List.nil(), null);
/*     */       }
/* 278 */       return new Attribute.Compound(paramJCAnnotation.type, List.nil());
/*     */     }
/*     */ 
/* 281 */     List localList = paramJCAnnotation.args;
/* 282 */     if ((localList.length() == 1) && (!((JCTree.JCExpression)localList.head).hasTag(JCTree.Tag.ASSIGN)))
/*     */     {
/* 284 */       localList.head = this.make.at(((JCTree.JCExpression)localList.head).pos)
/* 285 */         .Assign(this.make
/* 285 */         .Ident(this.names.value), 
/* 285 */         (JCTree.JCExpression)localList.head);
/*     */     }
/* 287 */     ListBuffer localListBuffer = new ListBuffer();
/*     */ 
/* 289 */     for (Object localObject = localList; ((List)localObject).nonEmpty(); localObject = ((List)localObject).tail) {
/* 290 */       JCTree.JCExpression localJCExpression = (JCTree.JCExpression)((List)localObject).head;
/* 291 */       if (!localJCExpression.hasTag(JCTree.Tag.ASSIGN)) {
/* 292 */         this.log.error(localJCExpression.pos(), "annotation.value.must.be.name.value", new Object[0]);
/*     */       }
/*     */       else {
/* 295 */         JCTree.JCAssign localJCAssign = (JCTree.JCAssign)localJCExpression;
/* 296 */         if (!localJCAssign.lhs.hasTag(JCTree.Tag.IDENT)) {
/* 297 */           this.log.error(localJCExpression.pos(), "annotation.value.must.be.name.value", new Object[0]);
/*     */         }
/*     */         else {
/* 300 */           JCTree.JCIdent localJCIdent = (JCTree.JCIdent)localJCAssign.lhs;
/* 301 */           Symbol localSymbol = this.rs.resolveQualifiedMethod(localJCAssign.rhs.pos(), paramEnv, paramJCAnnotation.type, localJCIdent.name, 
/* 305 */             List.nil(), null);
/*     */ 
/* 307 */           localJCIdent.sym = localSymbol;
/* 308 */           localJCIdent.type = localSymbol.type;
/* 309 */           if (localSymbol.owner != paramJCAnnotation.type.tsym)
/* 310 */             this.log.error(localJCIdent.pos(), "no.annotation.member", new Object[] { localJCIdent.name, paramJCAnnotation.type });
/* 311 */           Type localType2 = localSymbol.type.getReturnType();
/* 312 */           Attribute localAttribute = enterAttributeValue(localType2, localJCAssign.rhs, paramEnv);
/* 313 */           if (!localSymbol.type.isErroneous())
/* 314 */             localListBuffer.append(new Pair((Symbol.MethodSymbol)localSymbol, localAttribute));
/* 315 */           localJCExpression.type = localType2;
/*     */         }
/*     */       }
/*     */     }
/* 317 */     if (paramBoolean) {
/* 318 */       if ((paramJCAnnotation.attribute == null) || (!(paramJCAnnotation.attribute instanceof Attribute.TypeCompound)))
/*     */       {
/* 320 */         localObject = new Attribute.TypeCompound(paramJCAnnotation.type, localListBuffer.toList(), new TypeAnnotationPosition());
/* 321 */         paramJCAnnotation.attribute = ((Attribute.Compound)localObject);
/* 322 */         return localObject;
/*     */       }
/*     */ 
/* 325 */       return paramJCAnnotation.attribute;
/*     */     }
/*     */ 
/* 328 */     localObject = new Attribute.Compound(paramJCAnnotation.type, localListBuffer.toList());
/* 329 */     paramJCAnnotation.attribute = ((Attribute.Compound)localObject);
/* 330 */     return localObject;
/*     */   }
/*     */ 
/*     */   Attribute enterAttributeValue(Type paramType, JCTree.JCExpression paramJCExpression, Env<AttrContext> paramEnv)
/*     */   {
/*     */     try
/*     */     {
/* 341 */       paramType.tsym.complete();
/*     */     } catch (Symbol.CompletionFailure localCompletionFailure) {
/* 343 */       this.log.error(paramJCExpression.pos(), "cant.resolve", new Object[] { Kinds.kindName(localCompletionFailure.sym), localCompletionFailure.sym });
/* 344 */       paramType = this.syms.errType;
/*     */     }
/*     */     Object localObject1;
/*     */     Object localObject2;
/*     */     Object localObject3;
/* 346 */     if (paramType.hasTag(TypeTag.ARRAY)) {
/* 347 */       if (!paramJCExpression.hasTag(JCTree.Tag.NEWARRAY))
/*     */       {
/* 349 */         paramJCExpression = this.make.at(paramJCExpression.pos)
/* 349 */           .NewArray(null, 
/* 349 */           List.nil(), List.of(paramJCExpression));
/*     */       }
/* 351 */       localObject1 = (JCTree.JCNewArray)paramJCExpression;
/* 352 */       if (((JCTree.JCNewArray)localObject1).elemtype != null) {
/* 353 */         this.log.error(((JCTree.JCNewArray)localObject1).elemtype.pos(), "new.not.allowed.in.annotation", new Object[0]);
/*     */       }
/* 355 */       localObject2 = new ListBuffer();
/* 356 */       for (localObject3 = ((JCTree.JCNewArray)localObject1).elems; ((List)localObject3).nonEmpty(); localObject3 = ((List)localObject3).tail) {
/* 357 */         ((ListBuffer)localObject2).append(enterAttributeValue(this.types.elemtype(paramType), (JCTree.JCExpression)((List)localObject3).head, paramEnv));
/*     */       }
/*     */ 
/* 361 */       ((JCTree.JCNewArray)localObject1).type = paramType;
/*     */ 
/* 363 */       return new Attribute.Array(paramType, 
/* 363 */         (Attribute[])((ListBuffer)localObject2)
/* 363 */         .toArray(new Attribute[((ListBuffer)localObject2)
/* 363 */         .length()]));
/*     */     }
/* 365 */     if (paramJCExpression.hasTag(JCTree.Tag.NEWARRAY)) {
/* 366 */       if (!paramType.isErroneous())
/* 367 */         this.log.error(paramJCExpression.pos(), "annotation.value.not.allowable.type", new Object[0]);
/* 368 */       localObject1 = (JCTree.JCNewArray)paramJCExpression;
/* 369 */       if (((JCTree.JCNewArray)localObject1).elemtype != null) {
/* 370 */         this.log.error(((JCTree.JCNewArray)localObject1).elemtype.pos(), "new.not.allowed.in.annotation", new Object[0]);
/*     */       }
/* 372 */       for (localObject2 = ((JCTree.JCNewArray)localObject1).elems; ((List)localObject2).nonEmpty(); localObject2 = ((List)localObject2).tail) {
/* 373 */         enterAttributeValue(this.syms.errType, (JCTree.JCExpression)((List)localObject2).head, paramEnv);
/*     */       }
/*     */ 
/* 377 */       return new Attribute.Error(this.syms.errType);
/*     */     }
/* 379 */     if ((paramType.tsym.flags() & 0x2000) != 0L) {
/* 380 */       if (paramJCExpression.hasTag(JCTree.Tag.ANNOTATION)) {
/* 381 */         return enterAnnotation((JCTree.JCAnnotation)paramJCExpression, paramType, paramEnv);
/*     */       }
/* 383 */       this.log.error(paramJCExpression.pos(), "annotation.value.must.be.annotation", new Object[0]);
/* 384 */       paramType = this.syms.errType;
/*     */     }
/*     */ 
/* 387 */     if (paramJCExpression.hasTag(JCTree.Tag.ANNOTATION)) {
/* 388 */       if (!paramType.isErroneous())
/* 389 */         this.log.error(paramJCExpression.pos(), "annotation.not.valid.for.type", new Object[] { paramType });
/* 390 */       enterAnnotation((JCTree.JCAnnotation)paramJCExpression, this.syms.errType, paramEnv);
/* 391 */       return new Attribute.Error(((JCTree.JCAnnotation)paramJCExpression).annotationType.type);
/*     */     }
/* 393 */     if ((paramType.isPrimitive()) || (this.types.isSameType(paramType, this.syms.stringType))) {
/* 394 */       localObject1 = this.attr.attribExpr(paramJCExpression, paramEnv, paramType);
/* 395 */       if (((Type)localObject1).isErroneous())
/* 396 */         return new Attribute.Error(((Type)localObject1).getOriginalType());
/* 397 */       if (((Type)localObject1).constValue() == null) {
/* 398 */         this.log.error(paramJCExpression.pos(), "attribute.value.must.be.constant", new Object[0]);
/* 399 */         return new Attribute.Error(paramType);
/*     */       }
/* 401 */       localObject1 = this.cfolder.coerce((Type)localObject1, paramType);
/* 402 */       return new Attribute.Constant(paramType, ((Type)localObject1).constValue());
/*     */     }
/* 404 */     if (paramType.tsym == this.syms.classType.tsym) {
/* 405 */       localObject1 = this.attr.attribExpr(paramJCExpression, paramEnv, paramType);
/* 406 */       if (((Type)localObject1).isErroneous())
/*     */       {
/* 408 */         if ((TreeInfo.name(paramJCExpression) == this.names._class) && 
/* 409 */           (((JCTree.JCFieldAccess)paramJCExpression).selected.type
/* 409 */           .isErroneous())) {
/* 410 */           localObject2 = ((JCTree.JCFieldAccess)paramJCExpression).selected.type.tsym.flatName();
/*     */ 
/* 412 */           return new Attribute.UnresolvedClass(paramType, this.types
/* 412 */             .createErrorType((Name)localObject2, this.syms.unknownSymbol, this.syms.classType));
/*     */         }
/*     */ 
/* 415 */         return new Attribute.Error(((Type)localObject1).getOriginalType());
/*     */       }
/*     */ 
/* 421 */       if (TreeInfo.name(paramJCExpression) != this.names._class) {
/* 422 */         this.log.error(paramJCExpression.pos(), "annotation.value.must.be.class.literal", new Object[0]);
/* 423 */         return new Attribute.Error(this.syms.errType);
/*     */       }
/* 425 */       return new Attribute.Class(this.types, ((JCTree.JCFieldAccess)paramJCExpression).selected.type);
/*     */     }
/*     */ 
/* 428 */     if ((paramType.hasTag(TypeTag.CLASS)) && 
/* 429 */       ((paramType.tsym
/* 429 */       .flags() & 0x4000) != 0L)) {
/* 430 */       localObject1 = this.attr.attribExpr(paramJCExpression, paramEnv, paramType);
/* 431 */       localObject2 = TreeInfo.symbol(paramJCExpression);
/* 432 */       if ((localObject2 == null) || 
/* 433 */         (TreeInfo.nonstaticSelect(paramJCExpression)) || 
/* 433 */         (((Symbol)localObject2).kind != 4) || 
/* 435 */         ((((Symbol)localObject2)
/* 435 */         .flags() & 0x4000) == 0L)) {
/* 436 */         this.log.error(paramJCExpression.pos(), "enum.annotation.must.be.enum.constant", new Object[0]);
/* 437 */         return new Attribute.Error(((Type)localObject1).getOriginalType());
/*     */       }
/* 439 */       localObject3 = (Symbol.VarSymbol)localObject2;
/* 440 */       return new Attribute.Enum(paramType, (Symbol.VarSymbol)localObject3);
/*     */     }
/*     */ 
/* 443 */     if (!paramType.isErroneous())
/* 444 */       this.log.error(paramJCExpression.pos(), "annotation.value.not.allowable.type", new Object[0]);
/* 445 */     return new Attribute.Error(this.attr.attribExpr(paramJCExpression, paramEnv, paramType));
/*     */   }
/*     */ 
/*     */   private <T extends Attribute.Compound> T processRepeatedAnnotations(List<T> paramList, AnnotateRepeatedContext<T> paramAnnotateRepeatedContext, Symbol paramSymbol)
/*     */   {
/* 459 */     Attribute.Compound localCompound = (Attribute.Compound)paramList.head;
/* 460 */     List localList = List.nil();
/* 461 */     Type localType = null;
/* 462 */     Type.ArrayType localArrayType = null;
/* 463 */     Object localObject1 = null;
/* 464 */     Symbol.MethodSymbol localMethodSymbol = null;
/*     */ 
/* 466 */     Assert.check((!paramList.isEmpty()) && 
/* 467 */       (!paramList.tail
/* 467 */       .isEmpty()));
/*     */ 
/* 469 */     int i = 0;
/*     */     Object localObject3;
/*     */     Object localObject5;
/* 470 */     for (Object localObject2 = paramList; 
/* 471 */       !((List)localObject2).isEmpty(); 
/* 472 */       localObject2 = ((List)localObject2).tail)
/*     */     {
/* 474 */       i++;
/*     */ 
/* 477 */       Assert.check((i > 1) || (!((List)localObject2).tail.isEmpty()));
/*     */ 
/* 479 */       localObject3 = (Attribute.Compound)((List)localObject2).head;
/*     */ 
/* 481 */       localType = ((Attribute.Compound)localObject3).type;
/* 482 */       if (localArrayType == null) {
/* 483 */         localArrayType = this.types.makeArrayType(localType);
/*     */       }
/*     */ 
/* 487 */       boolean bool = i > 1;
/* 488 */       localObject5 = getContainingType((Attribute.Compound)localObject3, (JCDiagnostic.DiagnosticPosition)paramAnnotateRepeatedContext.pos.get(localObject3), bool);
/* 489 */       if (localObject5 != null)
/*     */       {
/* 495 */         Assert.check((localObject1 == null) || (localObject5 == localObject1));
/* 496 */         localObject1 = localObject5;
/*     */ 
/* 498 */         localMethodSymbol = validateContainer(localObject1, localType, (JCDiagnostic.DiagnosticPosition)paramAnnotateRepeatedContext.pos.get(localObject3));
/*     */ 
/* 500 */         if (localMethodSymbol != null)
/*     */         {
/* 505 */           localList = localList.prepend(localObject3);
/*     */         }
/*     */       }
/*     */     }
/* 508 */     if (!localList.isEmpty()) {
/* 509 */       localList = localList.reverse();
/* 510 */       localObject2 = this.make.at((JCDiagnostic.DiagnosticPosition)paramAnnotateRepeatedContext.pos.get(localCompound));
/* 511 */       localObject3 = new Pair(localMethodSymbol, new Attribute.Array(localArrayType, localList));
/*     */ 
/* 514 */       if (paramAnnotateRepeatedContext.isTypeCompound)
/*     */       {
/* 523 */         localObject4 = new Attribute.TypeCompound(localObject1, List.of(localObject3), ((Attribute.TypeCompound)paramList.head).position);
/*     */ 
/* 528 */         ((Attribute.TypeCompound)localObject4).setSynthesized(true);
/*     */ 
/* 531 */         localObject5 = localObject4;
/* 532 */         return localObject5;
/*     */       }
/* 534 */       Object localObject4 = new Attribute.Compound(localObject1, List.of(localObject3));
/* 535 */       localObject5 = ((TreeMaker)localObject2).Annotation((Attribute)localObject4);
/*     */ 
/* 537 */       if (!this.chk.annotationApplicable((JCTree.JCAnnotation)localObject5, paramSymbol)) {
/* 538 */         this.log.error(((JCTree.JCAnnotation)localObject5).pos(), "invalid.repeatable.annotation.incompatible.target", new Object[] { localObject1, localType });
/*     */       }
/* 540 */       if (!this.chk.validateAnnotationDeferErrors((JCTree.JCAnnotation)localObject5)) {
/* 541 */         this.log.error(((JCTree.JCAnnotation)localObject5).pos(), "duplicate.annotation.invalid.repeated", new Object[] { localType });
/*     */       }
/* 543 */       localObject4 = enterAnnotation((JCTree.JCAnnotation)localObject5, localObject1, paramAnnotateRepeatedContext.env);
/* 544 */       ((Attribute.Compound)localObject4).setSynthesized(true);
/*     */ 
/* 547 */       Object localObject6 = localObject4;
/* 548 */       return localObject6;
/*     */     }
/*     */ 
/* 551 */     return null;
/*     */   }
/*     */ 
/*     */   private Type getContainingType(Attribute.Compound paramCompound, JCDiagnostic.DiagnosticPosition paramDiagnosticPosition, boolean paramBoolean)
/*     */   {
/* 560 */     Type localType = paramCompound.type;
/* 561 */     Symbol.TypeSymbol localTypeSymbol = localType.tsym;
/*     */ 
/* 565 */     Attribute.Compound localCompound = localTypeSymbol.attribute(this.syms.repeatableType.tsym);
/* 566 */     if (localCompound == null) {
/* 567 */       if (paramBoolean)
/* 568 */         this.log.error(paramDiagnosticPosition, "duplicate.annotation.missing.container", new Object[] { localType, this.syms.repeatableType });
/* 569 */       return null;
/*     */     }
/*     */ 
/* 572 */     return filterSame(extractContainingType(localCompound, paramDiagnosticPosition, localTypeSymbol), localType);
/*     */   }
/*     */ 
/*     */   private Type filterSame(Type paramType1, Type paramType2)
/*     */   {
/* 578 */     if ((paramType1 == null) || (paramType2 == null)) {
/* 579 */       return paramType1;
/*     */     }
/*     */ 
/* 582 */     return this.types.isSameType(paramType1, paramType2) ? null : paramType1;
/*     */   }
/*     */ 
/*     */   private Type extractContainingType(Attribute.Compound paramCompound, JCDiagnostic.DiagnosticPosition paramDiagnosticPosition, Symbol.TypeSymbol paramTypeSymbol)
/*     */   {
/* 595 */     if (paramCompound.values.isEmpty()) {
/* 596 */       this.log.error(paramDiagnosticPosition, "invalid.repeatable.annotation", new Object[] { paramTypeSymbol });
/* 597 */       return null;
/*     */     }
/* 599 */     Pair localPair = (Pair)paramCompound.values.head;
/* 600 */     Name localName = ((Symbol.MethodSymbol)localPair.fst).name;
/* 601 */     if (localName != this.names.value) {
/* 602 */       this.log.error(paramDiagnosticPosition, "invalid.repeatable.annotation", new Object[] { paramTypeSymbol });
/* 603 */       return null;
/*     */     }
/* 605 */     if (!(localPair.snd instanceof Attribute.Class)) {
/* 606 */       this.log.error(paramDiagnosticPosition, "invalid.repeatable.annotation", new Object[] { paramTypeSymbol });
/* 607 */       return null;
/*     */     }
/*     */ 
/* 610 */     return ((Attribute.Class)localPair.snd).getValue();
/*     */   }
/*     */ 
/*     */   private Symbol.MethodSymbol validateContainer(Type paramType1, Type paramType2, JCDiagnostic.DiagnosticPosition paramDiagnosticPosition)
/*     */   {
/* 622 */     Symbol.MethodSymbol localMethodSymbol = null;
/* 623 */     int i = 0;
/*     */ 
/* 626 */     Scope localScope = paramType1.tsym.members();
/* 627 */     int j = 0;
/* 628 */     int k = 0;
/* 629 */     for (Object localObject1 = localScope.getElementsByName(this.names.value).iterator(); ((Iterator)localObject1).hasNext(); ) { localObject2 = (Symbol)((Iterator)localObject1).next();
/* 630 */       j++;
/*     */ 
/* 632 */       if ((j == 1) && (((Symbol)localObject2).kind == 16))
/*     */       {
/* 634 */         localMethodSymbol = (Symbol.MethodSymbol)localObject2;
/*     */       }
/* 636 */       else k = 1;
/*     */     }
/*     */ 
/* 639 */     if (k != 0) {
/* 640 */       this.log.error(paramDiagnosticPosition, "invalid.repeatable.annotation.multiple.values", new Object[] { paramType1, 
/* 643 */         Integer.valueOf(j) });
/*     */ 
/* 644 */       return null;
/* 645 */     }if (j == 0) {
/* 646 */       this.log.error(paramDiagnosticPosition, "invalid.repeatable.annotation.no.value", new Object[] { paramType1 });
/*     */ 
/* 649 */       return null;
/*     */     }
/*     */ 
/* 654 */     if (localMethodSymbol.kind != 16) {
/* 655 */       this.log.error(paramDiagnosticPosition, "invalid.repeatable.annotation.invalid.value", new Object[] { paramType1 });
/*     */ 
/* 658 */       i = 1;
/*     */     }
/*     */ 
/* 663 */     localObject1 = localMethodSymbol.type.getReturnType();
/* 664 */     Object localObject2 = this.types.makeArrayType(paramType2);
/* 665 */     if ((!this.types.isArray((Type)localObject1)) || 
/* 666 */       (!this.types
/* 666 */       .isSameType((Type)localObject2, (Type)localObject1)))
/*     */     {
/* 667 */       this.log.error(paramDiagnosticPosition, "invalid.repeatable.annotation.value.return", new Object[] { paramType1, localObject1, localObject2 });
/*     */ 
/* 672 */       i = 1;
/*     */     }
/* 674 */     if (k != 0) {
/* 675 */       i = 1;
/*     */     }
/*     */ 
/* 681 */     return i != 0 ? null : localMethodSymbol;
/*     */   }
/*     */ 
/*     */   public class AnnotateRepeatedContext<T extends Attribute.Compound>
/*     */   {
/*     */     public final Env<AttrContext> env;
/*     */     public final Map<Symbol.TypeSymbol, ListBuffer<T>> annotated;
/*     */     public final Map<T, JCDiagnostic.DiagnosticPosition> pos;
/*     */     public final Log log;
/*     */     public final boolean isTypeCompound;
/*     */ 
/*     */     public AnnotateRepeatedContext(Map<Symbol.TypeSymbol, ListBuffer<T>> paramMap, Map<T, JCDiagnostic.DiagnosticPosition> paramLog, Log paramBoolean, boolean arg5)
/*     */     {
/* 190 */       Assert.checkNonNull(paramMap);
/* 191 */       Assert.checkNonNull(paramLog);
/* 192 */       Assert.checkNonNull(paramBoolean);
/*     */       Object localObject;
/* 193 */       Assert.checkNonNull(localObject);
/*     */ 
/* 195 */       this.env = paramMap;
/* 196 */       this.annotated = paramLog;
/* 197 */       this.pos = paramBoolean;
/* 198 */       this.log = localObject;
/*     */       boolean bool;
/* 199 */       this.isTypeCompound = bool;
/*     */     }
/*     */ 
/*     */     public T processRepeatedAnnotations(List<T> paramList, Symbol paramSymbol)
/*     */     {
/* 211 */       return Annotate.this.processRepeatedAnnotations(paramList, this, paramSymbol);
/*     */     }
/*     */ 
/*     */     public void annotateRepeated(Annotate.Worker paramWorker)
/*     */     {
/* 221 */       Annotate.this.repeated(paramWorker);
/*     */     }
/*     */   }
/*     */ 
/*     */   public static abstract interface Worker
/*     */   {
/*     */     public abstract void run();
/*     */ 
/*     */     public abstract String toString();
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.javac.comp.Annotate
 * JD-Core Version:    0.6.2
 */