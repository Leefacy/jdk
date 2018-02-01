/*     */ package sun.tools.tree;
/*     */ 
/*     */ import java.io.PrintStream;
/*     */ import java.util.Hashtable;
/*     */ import sun.tools.asm.Assembler;
/*     */ import sun.tools.java.AmbiguousClass;
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
/*     */ public class NewInstanceExpression extends NaryExpression
/*     */ {
/*     */   MemberDefinition field;
/*     */   Expression outerArg;
/*     */   ClassDefinition body;
/*  44 */   MemberDefinition implMethod = null;
/*     */ 
/* 376 */   final int MAXINLINECOST = Statement.MAXINLINECOST;
/*     */ 
/*     */   public NewInstanceExpression(long paramLong, Expression paramExpression, Expression[] paramArrayOfExpression)
/*     */   {
/*  50 */     super(42, paramLong, Type.tError, paramExpression, paramArrayOfExpression);
/*     */   }
/*     */ 
/*     */   public NewInstanceExpression(long paramLong, Expression paramExpression1, Expression[] paramArrayOfExpression, Expression paramExpression2, ClassDefinition paramClassDefinition)
/*     */   {
/*  55 */     this(paramLong, paramExpression1, paramArrayOfExpression);
/*  56 */     this.outerArg = paramExpression2;
/*  57 */     this.body = paramClassDefinition;
/*     */   }
/*     */ 
/*     */   public Expression getOuterArg()
/*     */   {
/*  65 */     return this.outerArg;
/*     */   }
/*     */ 
/*     */   int precedence() {
/*  69 */     return 100;
/*     */   }
/*     */ 
/*     */   public Expression order()
/*     */   {
/*  74 */     if ((this.outerArg != null) && (opPrecedence[46] > this.outerArg.precedence())) {
/*  75 */       UnaryExpression localUnaryExpression = (UnaryExpression)this.outerArg;
/*  76 */       this.outerArg = localUnaryExpression.right;
/*  77 */       localUnaryExpression.right = order();
/*  78 */       return localUnaryExpression;
/*     */     }
/*  80 */     return this;
/*     */   }
/*     */ 
/*     */   public Vset checkValue(Environment paramEnvironment, Context paramContext, Vset paramVset, Hashtable paramHashtable)
/*     */   {
/*  88 */     ClassDefinition localClassDefinition1 = null;
/*     */ 
/*  90 */     Expression localExpression = null;
/*     */     try
/*     */     {
/*  93 */       if (this.outerArg != null) {
/*  94 */         paramVset = this.outerArg.checkValue(paramEnvironment, paramContext, paramVset, paramHashtable);
/*     */ 
/* 100 */         localExpression = this.outerArg;
/*     */ 
/* 103 */         Identifier localIdentifier = FieldExpression.toIdentifier(this.right);
/*     */ 
/* 107 */         if ((localIdentifier != null) && (localIdentifier.isQualified())) {
/* 108 */           paramEnvironment.error(this.where, "unqualified.name.required", localIdentifier);
/*     */         }
/*     */ 
/* 111 */         if ((localIdentifier == null) || (!this.outerArg.type.isType(10))) {
/* 112 */           if (!this.outerArg.type.isType(13)) {
/* 113 */             paramEnvironment.error(this.where, "invalid.field.reference", idNew, this.outerArg.type);
/*     */           }
/*     */ 
/* 116 */           this.outerArg = null;
/*     */         }
/*     */         else
/*     */         {
/* 122 */           ClassDefinition localClassDefinition2 = paramEnvironment.getClassDefinition(this.outerArg.type);
/* 123 */           localObject1 = localClassDefinition2.resolveInnerClass(paramEnvironment, localIdentifier);
/* 124 */           this.right = new TypeExpression(this.right.where, Type.tClass((Identifier)localObject1));
/*     */ 
/* 126 */           paramEnvironment.resolve(this.right.where, paramContext.field.getClassDefinition(), this.right.type);
/*     */         }
/*     */ 
/*     */       }
/*     */ 
/* 132 */       if (!(this.right instanceof TypeExpression))
/*     */       {
/* 134 */         this.right = new TypeExpression(this.right.where, this.right.toType(paramEnvironment, paramContext));
/*     */       }
/*     */ 
/* 137 */       if (this.right.type.isType(10))
/* 138 */         localClassDefinition1 = paramEnvironment.getClassDefinition(this.right.type);
/*     */     } catch (AmbiguousClass localAmbiguousClass) {
/* 140 */       paramEnvironment.error(this.where, "ambig.class", localAmbiguousClass.name1, localAmbiguousClass.name2);
/*     */     } catch (ClassNotFound localClassNotFound1) {
/* 142 */       paramEnvironment.error(this.where, "class.not.found", localClassNotFound1.name, paramContext.field);
/*     */     }
/*     */ 
/* 145 */     Type localType = this.right.type;
/* 146 */     boolean bool = localType.isType(13);
/*     */ 
/* 148 */     if ((!localType.isType(10)) && 
/* 149 */       (!bool)) {
/* 150 */       paramEnvironment.error(this.where, "invalid.arg.type", localType, opNames[this.op]);
/* 151 */       bool = true;
/*     */     }
/*     */ 
/* 159 */     if (localClassDefinition1 == null) {
/* 160 */       this.type = Type.tError;
/* 161 */       return paramVset;
/*     */     }
/*     */ 
/* 165 */     Object localObject1 = this.args;
/*     */ 
/* 167 */     localObject1 = insertOuterLink(paramEnvironment, paramContext, this.where, localClassDefinition1, this.outerArg, (Expression[])localObject1);
/*     */ 
/* 168 */     if (localObject1.length > this.args.length)
/* 169 */       this.outerArg = localObject1[0];
/* 170 */     else if (this.outerArg != null)
/*     */     {
/* 172 */       this.outerArg = new CommaExpression(this.outerArg.where, this.outerArg, null);
/*     */     }
/*     */ 
/* 175 */     Type[] arrayOfType = new Type[localObject1.length];
/*     */ 
/* 177 */     for (int i = 0; i < localObject1.length; i++)
/*     */     {
/* 179 */       if (localObject1[i] != localExpression) {
/* 180 */         paramVset = localObject1[i].checkValue(paramEnvironment, paramContext, paramVset, paramHashtable);
/*     */       }
/* 182 */       arrayOfType[i] = localObject1[i].type;
/* 183 */       bool = (bool) || (arrayOfType[i].isType(13));
/*     */     }
/*     */ 
/*     */     try
/*     */     {
/* 188 */       if (bool) {
/* 189 */         this.type = Type.tError;
/* 190 */         return paramVset;
/*     */       }
/*     */ 
/* 195 */       ClassDefinition localClassDefinition3 = paramContext.field.getClassDefinition();
/*     */ 
/* 197 */       ClassDeclaration localClassDeclaration = paramEnvironment.getClassDeclaration(localType);
/*     */       Object localObject2;
/*     */       Object localObject3;
/* 200 */       if (this.body != null)
/*     */       {
/* 202 */         localObject2 = localClassDefinition3.getName().getQualifier();
/*     */ 
/* 205 */         localObject3 = null;
/* 206 */         if (localClassDefinition1.isInterface())
/*     */         {
/* 213 */           localObject3 = paramEnvironment.getClassDefinition(idJavaLangObject);
/*     */         }
/*     */         else {
/* 216 */           localObject3 = localClassDefinition1;
/*     */         }
/*     */ 
/* 220 */         MemberDefinition localMemberDefinition = ((ClassDefinition)localObject3)
/* 220 */           .matchAnonConstructor(paramEnvironment, (Identifier)localObject2, arrayOfType);
/*     */ 
/* 221 */         if (localMemberDefinition != null)
/*     */         {
/* 231 */           paramEnvironment.dtEvent("NewInstanceExpression.checkValue: ANON CLASS " + this.body + " SUPER " + localClassDefinition1);
/*     */ 
/* 234 */           paramVset = this.body.checkLocalClass(paramEnvironment, paramContext, paramVset, localClassDefinition1, (Expression[])localObject1, localMemberDefinition
/* 236 */             .getType()
/* 237 */             .getArgumentTypes());
/*     */ 
/* 241 */           localType = this.body.getClassDeclaration().getType();
/*     */ 
/* 243 */           localClassDefinition1 = this.body;
/*     */         }
/*     */       }
/*     */       else {
/* 247 */         if (localClassDefinition1.isInterface()) {
/* 248 */           paramEnvironment.error(this.where, "new.intf", localClassDeclaration);
/* 249 */           return paramVset;
/*     */         }
/*     */ 
/* 253 */         if (localClassDefinition1.mustBeAbstract(paramEnvironment)) {
/* 254 */           paramEnvironment.error(this.where, "new.abstract", localClassDeclaration);
/* 255 */           return paramVset;
/*     */         }
/*     */ 
/*     */       }
/*     */ 
/* 260 */       this.field = localClassDefinition1.matchMethod(paramEnvironment, localClassDefinition3, idInit, arrayOfType);
/*     */ 
/* 263 */       if (this.field == null) {
/* 264 */         localObject2 = localClassDefinition1.findAnyMethod(paramEnvironment, idInit);
/* 265 */         if (localObject2 != null)
/*     */         {
/* 267 */           if (new MethodExpression(this.where, this.right, (MemberDefinition)localObject2, (Expression[])localObject1)
/* 267 */             .diagnoseMismatch(paramEnvironment, (Expression[])localObject1, arrayOfType))
/*     */           {
/* 268 */             return paramVset; } 
/* 269 */         }localObject3 = localClassDeclaration.getName().getName().toString();
/* 270 */         localObject3 = Type.tMethod(Type.tError, arrayOfType).typeString((String)localObject3, false, false);
/* 271 */         paramEnvironment.error(this.where, "unmatched.constr", localObject3, localClassDeclaration);
/* 272 */         return paramVset;
/*     */       }
/*     */ 
/* 275 */       if (this.field.isPrivate()) {
/* 276 */         localObject2 = this.field.getClassDefinition();
/* 277 */         if (localObject2 != localClassDefinition3)
/*     */         {
/* 279 */           this.implMethod = ((ClassDefinition)localObject2).getAccessMember(paramEnvironment, paramContext, this.field, false);
/*     */         }
/*     */ 
/*     */       }
/*     */ 
/* 284 */       if (localClassDefinition1.mustBeAbstract(paramEnvironment)) {
/* 285 */         paramEnvironment.error(this.where, "new.abstract", localClassDeclaration);
/* 286 */         return paramVset;
/*     */       }
/*     */ 
/* 289 */       if (this.field.reportDeprecated(paramEnvironment)) {
/* 290 */         paramEnvironment.error(this.where, "warn.constr.is.deprecated", this.field, this.field
/* 291 */           .getClassDefinition());
/*     */       }
/*     */ 
/* 297 */       if ((this.field.isProtected()) && 
/* 298 */         (!localClassDefinition3
/* 298 */         .getName().getQualifier().equals(this.field
/* 299 */         .getClassDeclaration().getName().getQualifier()))) {
/* 300 */         paramEnvironment.error(this.where, "invalid.protected.constructor.use", localClassDefinition3);
/*     */       }
/*     */     }
/*     */     catch (ClassNotFound localClassNotFound2)
/*     */     {
/* 305 */       paramEnvironment.error(this.where, "class.not.found", localClassNotFound2.name, opNames[this.op]);
/* 306 */       return paramVset;
/*     */     }
/*     */     catch (AmbiguousMember localAmbiguousMember) {
/* 309 */       paramEnvironment.error(this.where, "ambig.constr", localAmbiguousMember.field1, localAmbiguousMember.field2);
/* 310 */       return paramVset;
/*     */     }
/*     */ 
/* 314 */     arrayOfType = this.field.getType().getArgumentTypes();
/* 315 */     for (int j = 0; j < localObject1.length; j++) {
/* 316 */       localObject1[j] = convert(paramEnvironment, paramContext, arrayOfType[j], localObject1[j]);
/*     */     }
/* 318 */     if (localObject1.length > this.args.length) {
/* 319 */       this.outerArg = localObject1[0];
/*     */ 
/* 321 */       for (j = 1; j < localObject1.length; j++) {
/* 322 */         this.args[(j - 1)] = localObject1[j];
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 327 */     ClassDeclaration[] arrayOfClassDeclaration = this.field.getExceptions(paramEnvironment);
/* 328 */     for (int k = 0; k < arrayOfClassDeclaration.length; k++) {
/* 329 */       if (paramHashtable.get(arrayOfClassDeclaration[k]) == null) {
/* 330 */         paramHashtable.put(arrayOfClassDeclaration[k], this);
/*     */       }
/*     */     }
/*     */ 
/* 334 */     this.type = localType;
/*     */ 
/* 336 */     return paramVset;
/*     */   }
/*     */ 
/*     */   public static Expression[] insertOuterLink(Environment paramEnvironment, Context paramContext, long paramLong, ClassDefinition paramClassDefinition, Expression paramExpression, Expression[] paramArrayOfExpression)
/*     */   {
/* 350 */     if ((!paramClassDefinition.isTopLevel()) && (!paramClassDefinition.isLocal())) {
/* 351 */       Expression[] arrayOfExpression = new Expression[1 + paramArrayOfExpression.length];
/* 352 */       System.arraycopy(paramArrayOfExpression, 0, arrayOfExpression, 1, paramArrayOfExpression.length);
/*     */       try {
/* 354 */         if (paramExpression == null)
/* 355 */           paramExpression = paramContext.findOuterLink(paramEnvironment, paramLong, paramClassDefinition
/* 356 */             .findAnyMethod(paramEnvironment, idInit));
/*     */       }
/*     */       catch (ClassNotFound localClassNotFound)
/*     */       {
/*     */       }
/* 360 */       arrayOfExpression[0] = paramExpression;
/* 361 */       paramArrayOfExpression = arrayOfExpression;
/*     */     }
/* 363 */     return paramArrayOfExpression;
/*     */   }
/*     */ 
/*     */   public Vset check(Environment paramEnvironment, Context paramContext, Vset paramVset, Hashtable paramHashtable)
/*     */   {
/* 370 */     return checkValue(paramEnvironment, paramContext, paramVset, paramHashtable);
/*     */   }
/*     */ 
/*     */   public Expression copyInline(Context paramContext)
/*     */   {
/* 379 */     NewInstanceExpression localNewInstanceExpression = (NewInstanceExpression)super.copyInline(paramContext);
/* 380 */     if (this.outerArg != null) {
/* 381 */       localNewInstanceExpression.outerArg = this.outerArg.copyInline(paramContext);
/*     */     }
/* 383 */     return localNewInstanceExpression;
/*     */   }
/*     */ 
/*     */   Expression inlineNewInstance(Environment paramEnvironment, Context paramContext, Statement paramStatement) {
/* 387 */     if (paramEnvironment.dump()) {
/* 388 */       System.out.println("INLINE NEW INSTANCE " + this.field + " in " + paramContext.field);
/*     */     }
/* 390 */     LocalMember[] arrayOfLocalMember = LocalMember.copyArguments(paramContext, this.field);
/* 391 */     Statement[] arrayOfStatement = new Statement[arrayOfLocalMember.length + 2];
/*     */ 
/* 393 */     int i = 1;
/* 394 */     if ((this.outerArg != null) && (!this.outerArg.type.isType(11))) {
/* 395 */       i = 2;
/* 396 */       arrayOfStatement[1] = new VarDeclarationStatement(this.where, arrayOfLocalMember[1], this.outerArg);
/* 397 */     } else if (this.outerArg != null) {
/* 398 */       arrayOfStatement[0] = new ExpressionStatement(this.where, this.outerArg);
/*     */     }
/* 400 */     for (int j = 0; j < this.args.length; j++) {
/* 401 */       arrayOfStatement[(j + i)] = new VarDeclarationStatement(this.where, arrayOfLocalMember[(j + i)], this.args[j]);
/*     */     }
/*     */ 
/* 404 */     arrayOfStatement[(arrayOfStatement.length - 1)] = (paramStatement != null ? paramStatement.copyInline(paramContext, false) : null);
/*     */ 
/* 407 */     LocalMember.doneWithArguments(paramContext, arrayOfLocalMember);
/*     */ 
/* 409 */     return new InlineNewInstanceExpression(this.where, this.type, this.field, new CompoundStatement(this.where, arrayOfStatement)).inline(paramEnvironment, paramContext);
/*     */   }
/*     */ 
/*     */   public Expression inline(Environment paramEnvironment, Context paramContext) {
/* 413 */     return inlineValue(paramEnvironment, paramContext);
/*     */   }
/*     */   public Expression inlineValue(Environment paramEnvironment, Context paramContext) {
/* 416 */     if (this.body != null) {
/* 417 */       this.body.inlineLocalClass(paramEnvironment);
/*     */     }
/* 419 */     ClassDefinition localClassDefinition = this.field.getClassDefinition();
/* 420 */     UplevelReference localUplevelReference = localClassDefinition.getReferencesFrozen();
/* 421 */     if (localUplevelReference != null) {
/* 422 */       localUplevelReference.willCodeArguments(paramEnvironment, paramContext);
/*     */     }
/*     */ 
/*     */     try
/*     */     {
/* 427 */       if (this.outerArg != null) {
/* 428 */         if (this.outerArg.type.isType(11))
/* 429 */           this.outerArg = this.outerArg.inline(paramEnvironment, paramContext);
/*     */         else
/* 431 */           this.outerArg = this.outerArg.inlineValue(paramEnvironment, paramContext);
/*     */       }
/* 433 */       for (int i = 0; i < this.args.length; i++) {
/* 434 */         this.args[i] = this.args[i].inlineValue(paramEnvironment, paramContext);
/*     */       }
/*     */ 
/*     */     }
/*     */     catch (ClassNotFound localClassNotFound)
/*     */     {
/* 449 */       throw new CompilerError(localClassNotFound);
/*     */     }
/* 451 */     if ((this.outerArg != null) && (this.outerArg.type.isType(11))) {
/* 452 */       Expression localExpression = this.outerArg;
/* 453 */       this.outerArg = null;
/* 454 */       return new CommaExpression(this.where, localExpression, this);
/*     */     }
/* 456 */     return this;
/*     */   }
/*     */ 
/*     */   public int costInline(int paramInt, Environment paramEnvironment, Context paramContext) {
/* 460 */     if (this.body != null) {
/* 461 */       return paramInt;
/*     */     }
/* 463 */     if (paramContext == null) {
/* 464 */       return 2 + super.costInline(paramInt, paramEnvironment, paramContext);
/*     */     }
/*     */ 
/* 467 */     ClassDefinition localClassDefinition = paramContext.field.getClassDefinition();
/*     */     try
/*     */     {
/* 471 */       if ((localClassDefinition.permitInlinedAccess(paramEnvironment, this.field.getClassDeclaration())) && 
/* 472 */         (localClassDefinition
/* 472 */         .permitInlinedAccess(paramEnvironment, this.field)))
/*     */       {
/* 473 */         return 2 + super.costInline(paramInt, paramEnvironment, paramContext);
/*     */       }
/*     */     } catch (ClassNotFound localClassNotFound) {
/*     */     }
/* 477 */     return paramInt;
/*     */   }
/*     */ 
/*     */   public void code(Environment paramEnvironment, Context paramContext, Assembler paramAssembler)
/*     */   {
/* 485 */     codeCommon(paramEnvironment, paramContext, paramAssembler, false);
/*     */   }
/*     */   public void codeValue(Environment paramEnvironment, Context paramContext, Assembler paramAssembler) {
/* 488 */     codeCommon(paramEnvironment, paramContext, paramAssembler, true);
/*     */   }
/*     */ 
/*     */   private void codeCommon(Environment paramEnvironment, Context paramContext, Assembler paramAssembler, boolean paramBoolean) {
/* 492 */     paramAssembler.add(this.where, 187, this.field.getClassDeclaration());
/* 493 */     if (paramBoolean) {
/* 494 */       paramAssembler.add(this.where, 89);
/*     */     }
/*     */ 
/* 497 */     ClassDefinition localClassDefinition = this.field.getClassDefinition();
/* 498 */     UplevelReference localUplevelReference = localClassDefinition.getReferencesFrozen();
/*     */ 
/* 500 */     if (localUplevelReference != null) {
/* 501 */       localUplevelReference.codeArguments(paramEnvironment, paramContext, paramAssembler, this.where, this.field);
/*     */     }
/*     */ 
/* 504 */     if (this.outerArg != null) {
/* 505 */       this.outerArg.codeValue(paramEnvironment, paramContext, paramAssembler);
/*     */       Object localObject;
/* 506 */       switch (this.outerArg.op)
/*     */       {
/*     */       case 49:
/*     */       case 82:
/*     */       case 83:
/* 511 */         break;
/*     */       case 46:
/* 513 */         localObject = ((FieldExpression)this.outerArg).field;
/* 514 */         if ((localObject != null) && (((MemberDefinition)localObject).isNeverNull()))
/*     */         {
/*     */           break;
/*     */         }
/*     */ 
/*     */       default:
/*     */         try
/*     */         {
/* 523 */           localObject = paramEnvironment.getClassDefinition(idJavaLangObject);
/* 524 */           MemberDefinition localMemberDefinition = ((ClassDefinition)localObject).getFirstMatch(idGetClass);
/* 525 */           paramAssembler.add(this.where, 89);
/* 526 */           paramAssembler.add(this.where, 182, localMemberDefinition);
/* 527 */           paramAssembler.add(this.where, 87);
/*     */         }
/*     */         catch (ClassNotFound localClassNotFound) {
/*     */         }
/*     */       }
/*     */     }
/* 533 */     if (this.implMethod != null)
/*     */     {
/* 536 */       paramAssembler.add(this.where, 1);
/*     */     }
/*     */ 
/* 539 */     for (int i = 0; i < this.args.length; i++) {
/* 540 */       this.args[i].codeValue(paramEnvironment, paramContext, paramAssembler);
/*     */     }
/* 542 */     paramAssembler.add(this.where, 183, this.implMethod != null ? this.implMethod : this.field);
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     sun.tools.tree.NewInstanceExpression
 * JD-Core Version:    0.6.2
 */