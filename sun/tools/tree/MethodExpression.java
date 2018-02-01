/*     */ package sun.tools.tree;
/*     */ 
/*     */ import java.io.PrintStream;
/*     */ import java.util.Hashtable;
/*     */ import sun.tools.asm.Assembler;
/*     */ import sun.tools.java.AmbiguousMember;
/*     */ import sun.tools.java.ClassDeclaration;
/*     */ import sun.tools.java.ClassDefinition;
/*     */ import sun.tools.java.ClassNotFound;
/*     */ import sun.tools.java.CompilerError;
/*     */ import sun.tools.java.Environment;
/*     */ import sun.tools.java.Identifier;
/*     */ import sun.tools.java.MemberDefinition;
/*     */ import sun.tools.java.Type;
/*     */ 
/*     */ public class MethodExpression extends NaryExpression
/*     */ {
/*     */   Identifier id;
/*     */   ClassDefinition clazz;
/*     */   MemberDefinition field;
/*     */   Expression implementation;
/*     */   private boolean isSuper;
/* 643 */   static final int MAXINLINECOST = Statement.MAXINLINECOST;
/*     */ 
/*     */   public MethodExpression(long paramLong, Expression paramExpression, Identifier paramIdentifier, Expression[] paramArrayOfExpression)
/*     */   {
/*  51 */     super(47, paramLong, Type.tError, paramExpression, paramArrayOfExpression);
/*  52 */     this.id = paramIdentifier;
/*     */   }
/*     */   public MethodExpression(long paramLong, Expression paramExpression, MemberDefinition paramMemberDefinition, Expression[] paramArrayOfExpression) {
/*  55 */     super(47, paramLong, paramMemberDefinition.getType().getReturnType(), paramExpression, paramArrayOfExpression);
/*  56 */     this.id = paramMemberDefinition.getName();
/*  57 */     this.field = paramMemberDefinition;
/*  58 */     this.clazz = paramMemberDefinition.getClassDefinition();
/*     */   }
/*     */ 
/*     */   public MethodExpression(long paramLong, Expression paramExpression, MemberDefinition paramMemberDefinition, Expression[] paramArrayOfExpression, boolean paramBoolean)
/*     */   {
/*  68 */     this(paramLong, paramExpression, paramMemberDefinition, paramArrayOfExpression);
/*  69 */     this.isSuper = paramBoolean;
/*     */   }
/*     */ 
/*     */   public Expression getImplementation() {
/*  73 */     if (this.implementation != null)
/*  74 */       return this.implementation;
/*  75 */     return this;
/*     */   }
/*     */ 
/*     */   public Vset checkValue(Environment paramEnvironment, Context paramContext, Vset paramVset, Hashtable paramHashtable)
/*     */   {
/*  82 */     ClassDeclaration localClassDeclaration = null;
/*  83 */     int i = 0;
/*  84 */     boolean bool = false;
/*     */ 
/*  87 */     MemberDefinition localMemberDefinition1 = null;
/*     */ 
/*  89 */     ClassDefinition localClassDefinition1 = paramContext.field.getClassDefinition();
/*     */ 
/*  93 */     Expression[] arrayOfExpression = this.args;
/*  94 */     if (this.id.equals(idInit)) {
/*  95 */       localObject1 = localClassDefinition1;
/*     */       try {
/*  97 */         Expression localExpression = null;
/*  98 */         if ((this.right instanceof SuperExpression))
/*     */         {
/* 100 */           localObject1 = ((ClassDefinition)localObject1).getSuperClass().getClassDefinition(paramEnvironment);
/* 101 */           localExpression = ((SuperExpression)this.right).outerArg;
/* 102 */         } else if ((this.right instanceof ThisExpression))
/*     */         {
/* 104 */           localExpression = ((ThisExpression)this.right).outerArg;
/*     */         }
/*     */ 
/* 107 */         arrayOfExpression = NewInstanceExpression.insertOuterLink(paramEnvironment, paramContext, this.where, (ClassDefinition)localObject1, localExpression, arrayOfExpression);
/*     */       } catch (ClassNotFound localClassNotFound1) {
/*     */       }
/*     */     }
/*     */ 
/* 113 */     Object localObject1 = new Type[arrayOfExpression.length];
/*     */ 
/* 117 */     ClassDefinition localClassDefinition2 = localClassDefinition1;
/*     */     Object localObject5;
/*     */     Object localObject6;
/*     */     try {
/* 120 */       if (this.right == null) {
/* 121 */         bool = paramContext.field.isStatic();
/*     */ 
/* 123 */         ClassDefinition localClassDefinition3 = localClassDefinition1;
/* 124 */         MemberDefinition localMemberDefinition2 = null;
/* 125 */         for (; localClassDefinition3 != null; localClassDefinition3 = localClassDefinition3.getOuterClass()) {
/* 126 */           localMemberDefinition2 = localClassDefinition3.findAnyMethod(paramEnvironment, this.id);
/* 127 */           if (localMemberDefinition2 != null) {
/*     */             break;
/*     */           }
/*     */         }
/* 131 */         if (localMemberDefinition2 == null)
/*     */         {
/* 133 */           localClassDeclaration = paramContext.field.getClassDeclaration();
/*     */         }
/*     */         else {
/* 136 */           localClassDeclaration = localClassDefinition3.getClassDeclaration();
/*     */ 
/* 140 */           if (localMemberDefinition2.getClassDefinition() != localClassDefinition3) {
/* 141 */             localObject5 = localClassDefinition3;
/* 142 */             while ((localObject5 = ((ClassDefinition)localObject5).getOuterClass()) != null) {
/* 143 */               localObject6 = ((ClassDefinition)localObject5).findAnyMethod(paramEnvironment, this.id);
/* 144 */               if ((localObject6 != null) && (((MemberDefinition)localObject6).getClassDefinition() == localObject5)) {
/* 145 */                 paramEnvironment.error(this.where, "inherited.hides.method", this.id, localClassDefinition3
/* 146 */                   .getClassDeclaration(), ((ClassDefinition)localObject5)
/* 147 */                   .getClassDeclaration());
/* 148 */                 break;
/*     */               }
/*     */             }
/*     */           }
/*     */         }
/*     */       } else {
/* 154 */         if (this.id.equals(idInit)) {
/* 155 */           int j = paramContext.getThisNumber();
/* 156 */           if (!paramContext.field.isConstructor()) {
/* 157 */             paramEnvironment.error(this.where, "invalid.constr.invoke");
/* 158 */             return paramVset.addVar(j);
/*     */           }
/*     */ 
/* 192 */           if ((!paramVset.isReallyDeadEnd()) && (paramVset.testVar(j))) {
/* 193 */             paramEnvironment.error(this.where, "constr.invoke.not.first");
/* 194 */             return paramVset;
/*     */           }
/* 196 */           paramVset = paramVset.addVar(j);
/* 197 */           if ((this.right instanceof SuperExpression))
/*     */           {
/* 199 */             paramVset = this.right.checkAmbigName(paramEnvironment, paramContext, paramVset, paramHashtable, this);
/*     */           }
/* 201 */           else paramVset = this.right.checkValue(paramEnvironment, paramContext, paramVset, paramHashtable); 
/*     */         }
/*     */         else
/*     */         {
/* 204 */           paramVset = this.right.checkAmbigName(paramEnvironment, paramContext, paramVset, paramHashtable, this);
/* 205 */           if (this.right.type == Type.tPackage) {
/* 206 */             FieldExpression.reportFailedPackagePrefix(paramEnvironment, this.right);
/* 207 */             return paramVset;
/*     */           }
/* 209 */           if ((this.right instanceof TypeExpression)) {
/* 210 */             bool = true;
/*     */           }
/*     */         }
/* 213 */         if (this.right.type.isType(10)) {
/* 214 */           localClassDeclaration = paramEnvironment.getClassDeclaration(this.right.type);
/* 215 */         } else if (this.right.type.isType(9)) {
/* 216 */           i = 1;
/* 217 */           localClassDeclaration = paramEnvironment.getClassDeclaration(Type.tObject);
/*     */         } else {
/* 219 */           if (!this.right.type.isType(13)) {
/* 220 */             paramEnvironment.error(this.where, "invalid.method.invoke", this.right.type);
/*     */           }
/* 222 */           return paramVset;
/*     */         }
/*     */         Object localObject2;
/* 235 */         if ((this.right instanceof FieldExpression)) {
/* 236 */           localObject2 = ((FieldExpression)this.right).id;
/* 237 */           if (localObject2 == idThis) {
/* 238 */             localClassDefinition2 = ((FieldExpression)this.right).clazz;
/* 239 */           } else if (localObject2 == idSuper) {
/* 240 */             this.isSuper = true;
/* 241 */             localClassDefinition2 = ((FieldExpression)this.right).clazz;
/*     */           }
/* 243 */         } else if ((this.right instanceof SuperExpression)) {
/* 244 */           this.isSuper = true;
/*     */         }
/*     */ 
/* 254 */         if (this.id != idInit)
/*     */         {
/* 257 */           if (!FieldExpression.isTypeAccessible(this.where, paramEnvironment, this.right.type, localClassDefinition2))
/*     */           {
/* 261 */             localObject2 = localClassDefinition2
/* 261 */               .getClassDeclaration();
/* 262 */             if (bool)
/* 263 */               paramEnvironment.error(this.where, "no.type.access", this.id, this.right.type
/* 264 */                 .toString(), localObject2);
/*     */             else {
/* 266 */               paramEnvironment.error(this.where, "cant.access.member.type", this.id, this.right.type
/* 267 */                 .toString(), localObject2);
/*     */             }
/*     */           }
/*     */         }
/*     */ 
/*     */       }
/*     */ 
/* 274 */       int k = 0;
/*     */ 
/* 277 */       if (this.id.equals(idInit)) {
/* 278 */         paramVset = paramVset.clearVar(paramContext.getThisNumber());
/*     */       }
/*     */ 
/* 281 */       for (int n = 0; n < arrayOfExpression.length; n++) {
/* 282 */         paramVset = arrayOfExpression[n].checkValue(paramEnvironment, paramContext, paramVset, paramHashtable);
/* 283 */         localObject1[n] = arrayOfExpression[n].type;
/* 284 */         k = (k != 0) || (localObject1[n].isType(13)) ? 1 : 0;
/*     */       }
/*     */ 
/* 288 */       if (this.id.equals(idInit)) {
/* 289 */         paramVset = paramVset.addVar(paramContext.getThisNumber());
/*     */       }
/*     */ 
/* 293 */       if (k != 0) {
/* 294 */         return paramVset;
/*     */       }
/*     */ 
/* 298 */       this.clazz = localClassDeclaration.getClassDefinition(paramEnvironment);
/*     */       Object localObject4;
/* 300 */       if (this.field == null)
/*     */       {
/* 302 */         this.field = this.clazz.matchMethod(paramEnvironment, localClassDefinition2, this.id, (Type[])localObject1);
/*     */ 
/* 304 */         if (this.field == null) {
/* 305 */           if (this.id.equals(idInit)) {
/* 306 */             if (diagnoseMismatch(paramEnvironment, arrayOfExpression, (Type[])localObject1))
/* 307 */               return paramVset;
/* 308 */             localObject4 = this.clazz.getName().getName().toString();
/* 309 */             localObject4 = Type.tMethod(Type.tError, (Type[])localObject1).typeString((String)localObject4, false, false);
/* 310 */             paramEnvironment.error(this.where, "unmatched.constr", localObject4, localClassDeclaration);
/* 311 */             return paramVset;
/*     */           }
/* 313 */           localObject4 = this.id.toString();
/* 314 */           localObject4 = Type.tMethod(Type.tError, (Type[])localObject1).typeString((String)localObject4, false, false);
/* 315 */           if (this.clazz.findAnyMethod(paramEnvironment, this.id) == null) {
/* 316 */             if (paramContext.getField(paramEnvironment, this.id) != null)
/* 317 */               paramEnvironment.error(this.where, "invalid.method", this.id, localClassDeclaration);
/*     */             else
/* 319 */               paramEnvironment.error(this.where, "undef.meth", localObject4, localClassDeclaration);
/*     */           }
/* 321 */           else if (!diagnoseMismatch(paramEnvironment, arrayOfExpression, (Type[])localObject1))
/*     */           {
/* 323 */             paramEnvironment.error(this.where, "unmatched.meth", localObject4, localClassDeclaration);
/*     */           }
/* 325 */           return paramVset;
/*     */         }
/*     */ 
/*     */       }
/*     */ 
/* 330 */       this.type = this.field.getType().getReturnType();
/*     */ 
/* 333 */       if ((bool) && (!this.field.isStatic())) {
/* 334 */         paramEnvironment.error(this.where, "no.static.meth.access", this.field, this.field
/* 335 */           .getClassDeclaration());
/* 336 */         return paramVset;
/*     */       }
/*     */ 
/* 339 */       if ((this.field.isProtected()) && (this.right != null) && (!(this.right instanceof SuperExpression)) && ((!(this.right instanceof FieldExpression)) || (((FieldExpression)this.right).id != idSuper)))
/*     */       {
/* 345 */         if (!localClassDefinition2
/* 345 */           .protectedAccess(paramEnvironment, this.field, this.right.type))
/*     */         {
/* 346 */           paramEnvironment.error(this.where, "invalid.protected.method.use", this.field
/* 347 */             .getName(), this.field.getClassDeclaration(), this.right.type);
/*     */ 
/* 349 */           return paramVset;
/*     */         }
/*     */ 
/*     */       }
/*     */ 
/* 357 */       if (((this.right instanceof FieldExpression)) && (((FieldExpression)this.right).id == idSuper))
/*     */       {
/* 359 */         if (!this.field.isPrivate())
/*     */         {
/* 365 */           if (localClassDefinition2 != localClassDefinition1) {
/* 366 */             localMemberDefinition1 = localClassDefinition2.getAccessMember(paramEnvironment, paramContext, this.field, true);
/*     */           }
/*     */         }
/*     */ 
/*     */       }
/*     */ 
/* 372 */       if ((localMemberDefinition1 == null) && (this.field.isPrivate())) {
/* 373 */         localObject4 = this.field.getClassDefinition();
/* 374 */         if (localObject4 != localClassDefinition1) {
/* 375 */           localMemberDefinition1 = ((ClassDefinition)localObject4).getAccessMember(paramEnvironment, paramContext, this.field, false);
/*     */         }
/*     */ 
/*     */       }
/*     */ 
/* 380 */       if ((this.field.isAbstract()) && (this.right != null) && (this.right.op == 83)) {
/* 381 */         paramEnvironment.error(this.where, "invoke.abstract", this.field, this.field.getClassDeclaration());
/* 382 */         return paramVset;
/*     */       }
/*     */ 
/* 385 */       if (this.field.reportDeprecated(paramEnvironment)) {
/* 386 */         if (this.field.isConstructor())
/* 387 */           paramEnvironment.error(this.where, "warn.constr.is.deprecated", this.field);
/*     */         else {
/* 389 */           paramEnvironment.error(this.where, "warn.meth.is.deprecated", this.field, this.field
/* 390 */             .getClassDefinition());
/*     */         }
/*     */ 
/*     */       }
/*     */ 
/* 395 */       if ((this.field.isConstructor()) && (paramContext.field.equals(this.field))) {
/* 396 */         paramEnvironment.error(this.where, "recursive.constr", this.field);
/*     */       }
/*     */ 
/* 410 */       if (localClassDefinition2 == localClassDefinition1) {
/* 411 */         localObject4 = this.field.getClassDefinition();
/* 412 */         if ((!this.field.isConstructor()) && 
/* 413 */           (((ClassDefinition)localObject4)
/* 413 */           .isPackagePrivate()))
/*     */         {
/* 415 */           if (!((ClassDefinition)localObject4)
/* 414 */             .getName().getQualifier()
/* 415 */             .equals(localClassDefinition2
/* 415 */             .getName().getQualifier()))
/*     */           {
/* 428 */             this.field = 
/* 429 */               MemberDefinition.makeProxyMember(this.field, this.clazz, paramEnvironment);
/*     */           }
/*     */         }
/*     */       }
/*     */ 
/* 433 */       localClassDefinition2.addDependency(this.field.getClassDeclaration());
/* 434 */       if (localClassDefinition2 != localClassDefinition1)
/* 435 */         localClassDefinition1.addDependency(this.field.getClassDeclaration());
/*     */     }
/*     */     catch (ClassNotFound localClassNotFound2)
/*     */     {
/* 439 */       paramEnvironment.error(this.where, "class.not.found", localClassNotFound2.name, paramContext.field);
/* 440 */       return paramVset;
/*     */     }
/*     */     catch (AmbiguousMember localAmbiguousMember) {
/* 443 */       paramEnvironment.error(this.where, "ambig.field", this.id, localAmbiguousMember.field1, localAmbiguousMember.field2);
/* 444 */       return paramVset;
/*     */     }
/*     */ 
/* 448 */     if ((this.right == null) && (!this.field.isStatic())) {
/* 449 */       this.right = paramContext.findOuterLink(paramEnvironment, this.where, this.field);
/* 450 */       paramVset = this.right.checkValue(paramEnvironment, paramContext, paramVset, paramHashtable);
/*     */     }
/*     */ 
/* 454 */     localObject1 = this.field.getType().getArgumentTypes();
/* 455 */     for (int m = 0; m < arrayOfExpression.length; m++)
/* 456 */       arrayOfExpression[m] = convert(paramEnvironment, paramContext, localObject1[m], arrayOfExpression[m]);
/*     */     int i4;
/* 459 */     if (this.field.isConstructor()) {
/* 460 */       localObject3 = this.field;
/* 461 */       if (localMemberDefinition1 != null) {
/* 462 */         localObject3 = localMemberDefinition1;
/*     */       }
/* 464 */       int i1 = arrayOfExpression.length;
/* 465 */       localObject5 = arrayOfExpression;
/* 466 */       if (i1 > this.args.length)
/*     */       {
/* 470 */         if ((this.right instanceof SuperExpression)) {
/* 471 */           localObject6 = new SuperExpression(this.right.where, paramContext);
/* 472 */           ((SuperExpression)this.right).outerArg = arrayOfExpression[0];
/* 473 */         } else if ((this.right instanceof ThisExpression)) {
/* 474 */           localObject6 = new ThisExpression(this.right.where, paramContext);
/*     */         } else {
/* 476 */           throw new CompilerError("this.init");
/*     */         }
/*     */         int i5;
/* 478 */         if (localMemberDefinition1 != null)
/*     */         {
/* 483 */           localObject5 = new Expression[i1 + 1];
/* 484 */           this.args = new Expression[i1];
/* 485 */           localObject5[0] = arrayOfExpression[0];
/*     */           void tmp1963_1960 = new NullExpression(this.where); localObject5[1] = tmp1963_1960; this.args[0] = tmp1963_1960;
/* 487 */           for (i5 = 1; i5 < i1; i5++)
/*     */           {
/*     */             Expression tmp1993_1992 = arrayOfExpression[i5]; localObject5[(i5 + 1)] = tmp1993_1992; this.args[i5] = tmp1993_1992;
/*     */           }
/*     */         }
/*     */         else
/*     */         {
/* 493 */           for (i5 = 1; i5 < i1; i5++) {
/* 494 */             this.args[(i5 - 1)] = arrayOfExpression[i5];
/*     */           }
/*     */         }
/* 497 */         this.implementation = new MethodExpression(this.where, (Expression)localObject6, (MemberDefinition)localObject3, (Expression[])localObject5);
/* 498 */         this.implementation.type = this.type;
/*     */       }
/*     */       else {
/* 501 */         if (localMemberDefinition1 != null)
/*     */         {
/* 504 */           localObject5 = new Expression[i1 + 1];
/* 505 */           localObject5[0] = new NullExpression(this.where);
/* 506 */           for (i4 = 0; i4 < i1; i4++) {
/* 507 */             localObject5[(i4 + 1)] = arrayOfExpression[i4];
/*     */           }
/*     */         }
/* 510 */         this.implementation = new MethodExpression(this.where, this.right, (MemberDefinition)localObject3, (Expression[])localObject5);
/*     */       }
/*     */     }
/*     */     else
/*     */     {
/* 515 */       if (arrayOfExpression.length > this.args.length) {
/* 516 */         throw new CompilerError("method arg");
/*     */       }
/* 518 */       if (localMemberDefinition1 != null)
/*     */       {
/* 520 */         localObject3 = this.args;
/* 521 */         if (this.field.isStatic()) {
/* 522 */           MethodExpression localMethodExpression = new MethodExpression(this.where, null, localMemberDefinition1, (Expression[])localObject3);
/* 523 */           this.implementation = new CommaExpression(this.where, this.right, localMethodExpression);
/*     */         }
/*     */         else {
/* 526 */           int i2 = localObject3.length;
/* 527 */           localObject5 = new Expression[i2 + 1];
/* 528 */           localObject5[0] = this.right;
/* 529 */           for (i4 = 0; i4 < i2; i4++) {
/* 530 */             localObject5[(i4 + 1)] = localObject3[i4];
/*     */           }
/* 532 */           this.implementation = new MethodExpression(this.where, null, localMemberDefinition1, (Expression[])localObject5);
/*     */         }
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 538 */     if ((paramContext.field.isConstructor()) && 
/* 539 */       (this.field
/* 539 */       .isConstructor()) && (this.right != null) && (this.right.op == 83)) {
/* 540 */       localObject3 = makeVarInits(paramEnvironment, paramContext);
/* 541 */       if (localObject3 != null) {
/* 542 */         if (this.implementation == null)
/* 543 */           this.implementation = ((Expression)clone());
/* 544 */         this.implementation = new CommaExpression(this.where, this.implementation, (Expression)localObject3);
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 549 */     Object localObject3 = this.field.getExceptions(paramEnvironment);
/* 550 */     if ((i != 0) && (this.field.getName() == idClone) && 
/* 551 */       (this.field
/* 551 */       .getType().getArgumentTypes().length == 0))
/*     */     {
/* 555 */       localObject3 = new ClassDeclaration[0];
/*     */ 
/* 557 */       for (Context localContext = paramContext; localContext != null; localContext = localContext.prev) {
/* 558 */         if ((localContext.node != null) && (localContext.node.op == 101)) {
/* 559 */           ((TryStatement)localContext.node).arrayCloneWhere = this.where;
/*     */         }
/*     */       }
/*     */     }
/* 563 */     for (int i3 = 0; i3 < localObject3.length; i3++) {
/* 564 */       if (paramHashtable.get(localObject3[i3]) == null) {
/* 565 */         paramHashtable.put(localObject3[i3], this);
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 572 */     if ((paramContext.field.isConstructor()) && 
/* 573 */       (this.field
/* 573 */       .isConstructor()) && (this.right != null) && (this.right.op == 82)) {
/* 574 */       ClassDefinition localClassDefinition4 = this.field.getClassDefinition();
/* 575 */       for (localObject5 = localClassDefinition4.getFirstMember(); localObject5 != null; localObject5 = ((MemberDefinition)localObject5).getNextMember()) {
/* 576 */         if ((((MemberDefinition)localObject5).isVariable()) && (((MemberDefinition)localObject5).isBlankFinal()) && (!((MemberDefinition)localObject5).isStatic()))
/*     */         {
/* 579 */           paramVset = paramVset.addVar(paramContext.getFieldNumber((MemberDefinition)localObject5));
/*     */         }
/*     */       }
/*     */     }
/*     */ 
/* 584 */     return paramVset;
/*     */   }
/*     */ 
/*     */   public Vset check(Environment paramEnvironment, Context paramContext, Vset paramVset, Hashtable paramHashtable)
/*     */   {
/* 591 */     return checkValue(paramEnvironment, paramContext, paramVset, paramHashtable);
/*     */   }
/*     */ 
/*     */   boolean diagnoseMismatch(Environment paramEnvironment, Expression[] paramArrayOfExpression, Type[] paramArrayOfType)
/*     */     throws ClassNotFound
/*     */   {
/* 606 */     Type[] arrayOfType = new Type[1];
/* 607 */     boolean bool = false;
/* 608 */     int i = 0;
/* 609 */     while (i < paramArrayOfType.length) {
/* 610 */       int j = this.clazz.diagnoseMismatch(paramEnvironment, this.id, paramArrayOfType, i, arrayOfType);
/* 611 */       String str1 = this.id.equals(idInit) ? "constructor" : opNames[this.op];
/* 612 */       if (j == -2) {
/* 613 */         paramEnvironment.error(this.where, "wrong.number.args", str1);
/* 614 */         bool = true;
/*     */       }
/* 616 */       if (j < 0) break;
/* 617 */       int k = j >> 2;
/* 618 */       int m = (j & 0x2) != 0 ? 1 : 0;
/* 619 */       int n = (j & 0x1) != 0 ? 1 : 0;
/* 620 */       Type localType = arrayOfType[0];
/*     */ 
/* 624 */       String str2 = "" + localType;
/*     */ 
/* 630 */       if (m != 0)
/* 631 */         paramEnvironment.error(paramArrayOfExpression[k].where, "explicit.cast.needed", str1, paramArrayOfType[k], str2);
/*     */       else
/* 633 */         paramEnvironment.error(paramArrayOfExpression[k].where, "incompatible.type", str1, paramArrayOfType[k], str2);
/* 634 */       bool = true;
/* 635 */       i = k + 1;
/*     */     }
/* 637 */     return bool;
/*     */   }
/*     */ 
/*     */   private Expression inlineMethod(Environment paramEnvironment, Context paramContext, Statement paramStatement, boolean paramBoolean)
/*     */   {
/* 647 */     if (paramEnvironment.dump()) {
/* 648 */       System.out.println("INLINE METHOD " + this.field + " in " + paramContext.field);
/*     */     }
/* 650 */     LocalMember[] arrayOfLocalMember = LocalMember.copyArguments(paramContext, this.field);
/* 651 */     Statement[] arrayOfStatement = new Statement[arrayOfLocalMember.length + 2];
/*     */ 
/* 653 */     int i = 0;
/* 654 */     if (this.field.isStatic()) {
/* 655 */       arrayOfStatement[0] = new ExpressionStatement(this.where, this.right);
/*     */     } else {
/* 657 */       if ((this.right != null) && (this.right.op == 83)) {
/* 658 */         this.right = new ThisExpression(this.right.where, paramContext);
/*     */       }
/* 660 */       arrayOfStatement[0] = new VarDeclarationStatement(this.where, arrayOfLocalMember[(i++)], this.right);
/*     */     }
/* 662 */     for (int j = 0; j < this.args.length; j++) {
/* 663 */       arrayOfStatement[(j + 1)] = new VarDeclarationStatement(this.where, arrayOfLocalMember[(i++)], this.args[j]);
/*     */     }
/*     */ 
/* 668 */     arrayOfStatement[(arrayOfStatement.length - 1)] = (paramStatement != null ? paramStatement.copyInline(paramContext, paramBoolean) : null);
/*     */ 
/* 670 */     LocalMember.doneWithArguments(paramContext, arrayOfLocalMember);
/*     */ 
/* 673 */     Type localType = paramBoolean ? this.type : Type.tVoid;
/* 674 */     InlineMethodExpression localInlineMethodExpression = new InlineMethodExpression(this.where, localType, this.field, new CompoundStatement(this.where, arrayOfStatement));
/* 675 */     return paramBoolean ? localInlineMethodExpression.inlineValue(paramEnvironment, paramContext) : localInlineMethodExpression.inline(paramEnvironment, paramContext);
/*     */   }
/*     */ 
/*     */   public Expression inline(Environment paramEnvironment, Context paramContext) {
/* 679 */     if (this.implementation != null)
/* 680 */       return this.implementation.inline(paramEnvironment, paramContext);
/*     */     try {
/* 682 */       if (this.right != null) {
/* 683 */         this.right = (this.field.isStatic() ? this.right.inline(paramEnvironment, paramContext) : this.right.inlineValue(paramEnvironment, paramContext));
/*     */       }
/* 685 */       for (int i = 0; i < this.args.length; i++) {
/* 686 */         this.args[i] = this.args[i].inlineValue(paramEnvironment, paramContext);
/*     */       }
/*     */ 
/* 690 */       ClassDefinition localClassDefinition = paramContext.field.getClassDefinition();
/*     */ 
/* 692 */       Object localObject = this;
/*     */       Statement localStatement;
/* 693 */       if ((paramEnvironment.opt()) && (this.field.isInlineable(paramEnvironment, this.clazz.isFinal())) && ((this.right == null) || (this.right.op == 82) || 
/* 697 */         (this.field
/* 697 */         .isStatic())))
/*     */       {
/* 701 */         if (localClassDefinition
/* 701 */           .permitInlinedAccess(paramEnvironment, this.field
/* 702 */           .getClassDeclaration()))
/* 703 */           if ((localClassDefinition
/* 703 */             .permitInlinedAccess(paramEnvironment, this.field)) && 
/* 703 */             ((this.right == null) || 
/* 704 */             (localClassDefinition
/* 704 */             .permitInlinedAccess(paramEnvironment, paramEnvironment
/* 705 */             .getClassDeclaration(this.right.type)))) && 
/* 704 */             ((this.id == null) || 
/* 707 */             (!this.id
/* 707 */             .equals(idInit))) && 
/* 708 */             (!paramContext.field
/* 708 */             .isInitializer()) && (paramContext.field.isMethod()) && 
/* 709 */             (paramContext
/* 709 */             .getInlineMemberContext(this.field) == null)) { localStatement = (Statement)this.field.getValue(paramEnvironment);
/* 711 */             if ((localStatement != null) && 
/* 712 */               (localStatement
/* 712 */               .costInline(MAXINLINECOST, paramEnvironment, paramContext) >= 
/* 712 */               MAXINLINECOST)); }  
/* 713 */       }return inlineMethod(paramEnvironment, paramContext, localStatement, false);
/*     */     }
/*     */     catch (ClassNotFound localClassNotFound)
/*     */     {
/* 719 */       throw new CompilerError(localClassNotFound);
/*     */     }
/*     */   }
/*     */ 
/*     */   public Expression inlineValue(Environment paramEnvironment, Context paramContext) {
/* 724 */     if (this.implementation != null)
/* 725 */       return this.implementation.inlineValue(paramEnvironment, paramContext);
/*     */     try {
/* 727 */       if (this.right != null)
/* 728 */         this.right = (this.field.isStatic() ? this.right.inline(paramEnvironment, paramContext) : this.right.inlineValue(paramEnvironment, paramContext));
/*     */       Object localObject;
/* 730 */       if (this.field.getName().equals(idInit)) {
/* 731 */         ClassDefinition localClassDefinition1 = this.field.getClassDefinition();
/* 732 */         localObject = localClassDefinition1.getReferencesFrozen();
/* 733 */         if (localObject != null) {
/* 734 */           ((UplevelReference)localObject).willCodeArguments(paramEnvironment, paramContext);
/*     */         }
/*     */       }
/* 737 */       for (int i = 0; i < this.args.length; i++) {
/* 738 */         this.args[i] = this.args[i].inlineValue(paramEnvironment, paramContext);
/*     */       }
/*     */ 
/* 742 */       ClassDefinition localClassDefinition2 = paramContext.field.getClassDefinition();
/*     */ 
/* 744 */       if ((paramEnvironment.opt()) && (this.field.isInlineable(paramEnvironment, this.clazz.isFinal())) && ((this.right == null) || (this.right.op == 82) || 
/* 748 */         (this.field
/* 748 */         .isStatic())))
/*     */       {
/* 752 */         if (localClassDefinition2
/* 752 */           .permitInlinedAccess(paramEnvironment, this.field
/* 753 */           .getClassDeclaration()))
/* 754 */           if ((localClassDefinition2
/* 754 */             .permitInlinedAccess(paramEnvironment, this.field)) && 
/* 754 */             ((this.right == null) || 
/* 755 */             (localClassDefinition2
/* 755 */             .permitInlinedAccess(paramEnvironment, paramEnvironment
/* 756 */             .getClassDeclaration(this.right.type)))))
/*     */           {
/* 758 */             if ((!paramContext.field
/* 758 */               .isInitializer()) && (paramContext.field.isMethod()) && 
/* 759 */               (paramContext
/* 759 */               .getInlineMemberContext(this.field) == null))
/*     */             {
/* 760 */               localObject = (Statement)this.field.getValue(paramEnvironment);
/* 761 */               if ((localObject == null) || 
/* 762 */                 (((Statement)localObject)
/* 762 */                 .costInline(MAXINLINECOST, paramEnvironment, paramContext) < 
/* 762 */                 MAXINLINECOST))
/* 763 */                 return inlineMethod(paramEnvironment, paramContext, (Statement)localObject, true); 
/*     */             }
/*     */           }
/*     */       }
/* 766 */       return this;
/*     */     } catch (ClassNotFound localClassNotFound) {
/* 768 */       throw new CompilerError(localClassNotFound);
/*     */     }
/*     */   }
/*     */ 
/*     */   public Expression copyInline(Context paramContext) {
/* 773 */     if (this.implementation != null)
/* 774 */       return this.implementation.copyInline(paramContext);
/* 775 */     return super.copyInline(paramContext);
/*     */   }
/*     */ 
/*     */   public int costInline(int paramInt, Environment paramEnvironment, Context paramContext) {
/* 779 */     if (this.implementation != null) {
/* 780 */       return this.implementation.costInline(paramInt, paramEnvironment, paramContext);
/*     */     }
/*     */ 
/* 784 */     if ((this.right != null) && (this.right.op == 83)) {
/* 785 */       return paramInt;
/*     */     }
/* 787 */     return super.costInline(paramInt, paramEnvironment, paramContext);
/*     */   }
/*     */ 
/*     */   private Expression makeVarInits(Environment paramEnvironment, Context paramContext)
/*     */   {
/* 798 */     ClassDefinition localClassDefinition = paramContext.field.getClassDefinition();
/* 799 */     Object localObject1 = null;
/* 800 */     for (MemberDefinition localMemberDefinition = localClassDefinition.getFirstMember(); localMemberDefinition != null; localMemberDefinition = localMemberDefinition.getNextMember()) {
/* 801 */       if (((localMemberDefinition.isVariable()) || (localMemberDefinition.isInitializer())) && (!localMemberDefinition.isStatic())) {
/*     */         try {
/* 803 */           localMemberDefinition.check(paramEnvironment);
/*     */         } catch (ClassNotFound localClassNotFound) {
/* 805 */           paramEnvironment.error(localMemberDefinition.getWhere(), "class.not.found", localClassNotFound.name, localMemberDefinition
/* 806 */             .getClassDefinition());
/*     */         }
/* 808 */         Object localObject2 = null;
/*     */         Object localObject3;
/* 809 */         if (localMemberDefinition.isUplevelValue()) {
/* 810 */           if (localMemberDefinition != localClassDefinition.findOuterMember())
/*     */           {
/*     */             continue;
/*     */           }
/*     */ 
/* 815 */           localObject3 = new IdentifierExpression(this.where, localMemberDefinition
/* 815 */             .getName());
/* 816 */           if (!((IdentifierExpression)localObject3).bind(paramEnvironment, paramContext)) {
/* 817 */             throw new CompilerError("bind " + ((IdentifierExpression)localObject3).id);
/*     */           }
/* 819 */           localObject2 = localObject3;
/* 820 */         } else if (localMemberDefinition.isInitializer()) {
/* 821 */           localObject3 = (Statement)localMemberDefinition.getValue();
/* 822 */           localObject2 = new InlineMethodExpression(this.where, Type.tVoid, localMemberDefinition, (Statement)localObject3);
/*     */         } else {
/* 824 */           localObject2 = (Expression)localMemberDefinition.getValue();
/*     */         }
/*     */ 
/* 832 */         if (localObject2 != null) {
/* 833 */           long l = localMemberDefinition.getWhere();
/* 834 */           localObject2 = ((Expression)localObject2).copyInline(paramContext);
/* 835 */           Object localObject4 = localObject2;
/* 836 */           if (localMemberDefinition.isVariable()) {
/* 837 */             Object localObject5 = new ThisExpression(l, paramContext);
/* 838 */             localObject5 = new FieldExpression(l, (Expression)localObject5, localMemberDefinition);
/* 839 */             localObject4 = new AssignExpression(l, (Expression)localObject5, (Expression)localObject2);
/*     */           }
/* 841 */           localObject1 = localObject1 == null ? localObject4 : new CommaExpression(l, (Expression)localObject1, (Expression)localObject4);
/*     */         }
/*     */       }
/*     */     }
/* 845 */     return localObject1;
/*     */   }
/*     */ 
/*     */   public void codeValue(Environment paramEnvironment, Context paramContext, Assembler paramAssembler)
/*     */   {
/* 852 */     if (this.implementation != null)
/* 853 */       throw new CompilerError("codeValue");
/* 854 */     int i = 0;
/*     */     ClassDefinition localClassDefinition;
/*     */     UplevelReference localUplevelReference;
/* 855 */     if (this.field.isStatic()) {
/* 856 */       if (this.right != null)
/* 857 */         this.right.code(paramEnvironment, paramContext, paramAssembler);
/*     */     }
/* 859 */     else if (this.right == null) {
/* 860 */       paramAssembler.add(this.where, 25, new Integer(0));
/* 861 */     } else if (this.right.op == 83)
/*     */     {
/* 866 */       this.right.codeValue(paramEnvironment, paramContext, paramAssembler);
/* 867 */       if (idInit.equals(this.id))
/*     */       {
/* 869 */         localClassDefinition = this.field.getClassDefinition();
/* 870 */         localUplevelReference = localClassDefinition.getReferencesFrozen();
/* 871 */         if (localUplevelReference != null)
/*     */         {
/* 874 */           if (localUplevelReference.isClientOuterField())
/*     */           {
/* 876 */             this.args[(i++)].codeValue(paramEnvironment, paramContext, paramAssembler);
/*     */           }
/* 878 */           localUplevelReference.codeArguments(paramEnvironment, paramContext, paramAssembler, this.where, this.field);
/*     */         }
/*     */       }
/*     */     } else {
/* 882 */       this.right.codeValue(paramEnvironment, paramContext, paramAssembler);
/*     */     }
/*     */ 
/* 892 */     for (; i < this.args.length; i++) {
/* 893 */       this.args[i].codeValue(paramEnvironment, paramContext, paramAssembler);
/*     */     }
/*     */ 
/* 896 */     if (this.field.isStatic())
/* 897 */       paramAssembler.add(this.where, 184, this.field);
/* 898 */     else if ((this.field.isConstructor()) || (this.field.isPrivate()) || (this.isSuper))
/* 899 */       paramAssembler.add(this.where, 183, this.field);
/* 900 */     else if (this.field.getClassDefinition().isInterface())
/* 901 */       paramAssembler.add(this.where, 185, this.field);
/*     */     else {
/* 903 */       paramAssembler.add(this.where, 182, this.field);
/*     */     }
/*     */ 
/* 906 */     if ((this.right != null) && (this.right.op == 83) && (idInit.equals(this.id)))
/*     */     {
/* 908 */       localClassDefinition = paramContext.field.getClassDefinition();
/* 909 */       localUplevelReference = localClassDefinition.getReferencesFrozen();
/* 910 */       if (localUplevelReference != null)
/*     */       {
/* 913 */         localUplevelReference.codeInitialization(paramEnvironment, paramContext, paramAssembler, this.where, this.field);
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   public Expression firstConstructor()
/*     */   {
/* 922 */     return this.id.equals(idInit) ? this : null;
/*     */   }
/*     */ 
/*     */   public void print(PrintStream paramPrintStream)
/*     */   {
/* 929 */     paramPrintStream.print("(" + opNames[this.op]);
/* 930 */     if (this.right != null) {
/* 931 */       paramPrintStream.print(" ");
/* 932 */       this.right.print(paramPrintStream);
/*     */     }
/* 934 */     paramPrintStream.print(" " + (this.id == null ? idInit : this.id));
/* 935 */     for (int i = 0; i < this.args.length; i++) {
/* 936 */       paramPrintStream.print(" ");
/* 937 */       if (this.args[i] != null)
/* 938 */         this.args[i].print(paramPrintStream);
/*     */       else {
/* 940 */         paramPrintStream.print("<null>");
/*     */       }
/*     */     }
/* 943 */     paramPrintStream.print(")");
/* 944 */     if (this.implementation != null) {
/* 945 */       paramPrintStream.print("/IMPL=");
/* 946 */       this.implementation.print(paramPrintStream);
/*     */     }
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     sun.tools.tree.MethodExpression
 * JD-Core Version:    0.6.2
 */