/*     */ package sun.tools.tree;
/*     */ 
/*     */ import sun.tools.java.AmbiguousMember;
/*     */ import sun.tools.java.ClassDefinition;
/*     */ import sun.tools.java.ClassNotFound;
/*     */ import sun.tools.java.Constants;
/*     */ import sun.tools.java.Environment;
/*     */ import sun.tools.java.Identifier;
/*     */ import sun.tools.java.MemberDefinition;
/*     */ import sun.tools.java.Type;
/*     */ 
/*     */ public class Context
/*     */   implements Constants
/*     */ {
/*     */   Context prev;
/*     */   Node node;
/*     */   int varNumber;
/*     */   LocalMember locals;
/*     */   LocalMember classes;
/*     */   MemberDefinition field;
/*     */   int scopeNumber;
/*     */   int frameNumber;
/*     */ 
/*     */   public Context(Context paramContext, MemberDefinition paramMemberDefinition)
/*     */   {
/*  52 */     this.field = paramMemberDefinition;
/*  53 */     if (paramContext == null) {
/*  54 */       this.frameNumber = 1;
/*  55 */       this.scopeNumber = 2;
/*  56 */       this.varNumber = 0;
/*     */     } else {
/*  58 */       this.prev = paramContext;
/*  59 */       this.locals = paramContext.locals;
/*  60 */       this.classes = paramContext.classes;
/*  61 */       if ((paramMemberDefinition != null) && (
/*  62 */         (paramMemberDefinition
/*  62 */         .isVariable()) || (paramMemberDefinition.isInitializer())))
/*     */       {
/*  66 */         this.frameNumber = paramContext.frameNumber;
/*  67 */         paramContext.scopeNumber += 1;
/*     */       } else {
/*  69 */         this.frameNumber = (paramContext.scopeNumber + 1);
/*  70 */         this.scopeNumber = (this.frameNumber + 1);
/*     */       }
/*  72 */       this.varNumber = paramContext.varNumber;
/*     */     }
/*     */   }
/*     */ 
/*     */   public Context(Context paramContext, ClassDefinition paramClassDefinition)
/*     */   {
/*  80 */     this(paramContext, (MemberDefinition)null);
/*     */   }
/*     */ 
/*     */   Context(Context paramContext, Node paramNode)
/*     */   {
/*  87 */     if (paramContext == null) {
/*  88 */       this.frameNumber = 1;
/*  89 */       this.scopeNumber = 2;
/*  90 */       this.varNumber = 0;
/*     */     } else {
/*  92 */       this.prev = paramContext;
/*  93 */       this.locals = paramContext.locals;
/*     */ 
/*  96 */       this.classes = paramContext.classes;
/*  97 */       this.varNumber = paramContext.varNumber;
/*  98 */       this.field = paramContext.field;
/*  99 */       this.frameNumber = paramContext.frameNumber;
/* 100 */       paramContext.scopeNumber += 1;
/* 101 */       this.node = paramNode;
/*     */     }
/*     */   }
/*     */ 
/*     */   public Context(Context paramContext) {
/* 106 */     this(paramContext, (Node)null);
/*     */   }
/*     */ 
/*     */   public int declare(Environment paramEnvironment, LocalMember paramLocalMember)
/*     */   {
/* 114 */     paramLocalMember.scopeNumber = this.scopeNumber;
/* 115 */     if ((this.field == null) && (idThis.equals(paramLocalMember.getName()))) {
/* 116 */       paramLocalMember.scopeNumber += 1;
/*     */     }
/* 118 */     if (paramLocalMember.isInnerClass()) {
/* 119 */       paramLocalMember.prev = this.classes;
/* 120 */       this.classes = paramLocalMember;
/* 121 */       return 0;
/*     */     }
/*     */ 
/* 148 */     paramLocalMember.prev = this.locals;
/* 149 */     this.locals = paramLocalMember;
/* 150 */     paramLocalMember.number = this.varNumber;
/* 151 */     this.varNumber += paramLocalMember.getType().stackSize();
/* 152 */     return paramLocalMember.number;
/*     */   }
/*     */ 
/*     */   public LocalMember getLocalField(Identifier paramIdentifier)
/*     */   {
/* 160 */     for (LocalMember localLocalMember = this.locals; localLocalMember != null; localLocalMember = localLocalMember.prev) {
/* 161 */       if (paramIdentifier.equals(localLocalMember.getName())) {
/* 162 */         return localLocalMember;
/*     */       }
/*     */     }
/* 165 */     return null;
/*     */   }
/*     */ 
/*     */   public int getScopeNumber(ClassDefinition paramClassDefinition)
/*     */   {
/* 175 */     for (Context localContext = this; localContext != null; localContext = localContext.prev) {
/* 176 */       if ((localContext.field != null) && 
/* 177 */         (localContext.field.getClassDefinition() == paramClassDefinition)) {
/* 178 */         return localContext.frameNumber;
/*     */       }
/*     */     }
/* 181 */     return -1;
/*     */   }
/*     */ 
/*     */   private MemberDefinition getFieldCommon(Environment paramEnvironment, Identifier paramIdentifier, boolean paramBoolean)
/*     */     throws AmbiguousMember, ClassNotFound
/*     */   {
/* 191 */     LocalMember localLocalMember = getLocalField(paramIdentifier);
/* 192 */     int i = localLocalMember == null ? -2 : localLocalMember.scopeNumber;
/*     */ 
/* 194 */     ClassDefinition localClassDefinition1 = this.field.getClassDefinition();
/*     */ 
/* 197 */     for (ClassDefinition localClassDefinition2 = localClassDefinition1; 
/* 198 */       localClassDefinition2 != null; 
/* 199 */       localClassDefinition2 = localClassDefinition2.getOuterClass()) {
/* 200 */       MemberDefinition localMemberDefinition = localClassDefinition2.getVariable(paramEnvironment, paramIdentifier, localClassDefinition1);
/* 201 */       if ((localMemberDefinition != null) && (getScopeNumber(localClassDefinition2) > i) && (
/* 202 */         (!paramBoolean) || (localMemberDefinition.getClassDefinition() == localClassDefinition2)))
/*     */       {
/* 205 */         return localMemberDefinition;
/*     */       }
/*     */     }
/*     */ 
/* 209 */     return localLocalMember;
/*     */   }
/*     */ 
/*     */   public int declareFieldNumber(MemberDefinition paramMemberDefinition)
/*     */   {
/* 217 */     return declare(null, new LocalMember(paramMemberDefinition));
/*     */   }
/*     */ 
/*     */   public int getFieldNumber(MemberDefinition paramMemberDefinition)
/*     */   {
/* 225 */     for (LocalMember localLocalMember = this.locals; localLocalMember != null; localLocalMember = localLocalMember.prev) {
/* 226 */       if (localLocalMember.getMember() == paramMemberDefinition) {
/* 227 */         return localLocalMember.number;
/*     */       }
/*     */     }
/* 230 */     return -1;
/*     */   }
/*     */ 
/*     */   public MemberDefinition getElement(int paramInt)
/*     */   {
/* 238 */     for (LocalMember localLocalMember = this.locals; localLocalMember != null; localLocalMember = localLocalMember.prev) {
/* 239 */       if (localLocalMember.number == paramInt) {
/* 240 */         MemberDefinition localMemberDefinition = localLocalMember.getMember();
/* 241 */         return localMemberDefinition != null ? localMemberDefinition : localLocalMember;
/*     */       }
/*     */     }
/* 244 */     return null;
/*     */   }
/*     */ 
/*     */   public LocalMember getLocalClass(Identifier paramIdentifier)
/*     */   {
/* 252 */     for (LocalMember localLocalMember = this.classes; localLocalMember != null; localLocalMember = localLocalMember.prev) {
/* 253 */       if (paramIdentifier.equals(localLocalMember.getName())) {
/* 254 */         return localLocalMember;
/*     */       }
/*     */     }
/* 257 */     return null;
/*     */   }
/*     */ 
/*     */   private MemberDefinition getClassCommon(Environment paramEnvironment, Identifier paramIdentifier, boolean paramBoolean)
/*     */     throws ClassNotFound
/*     */   {
/* 263 */     LocalMember localLocalMember = getLocalClass(paramIdentifier);
/* 264 */     int i = localLocalMember == null ? -2 : localLocalMember.scopeNumber;
/*     */ 
/* 267 */     for (ClassDefinition localClassDefinition = this.field.getClassDefinition(); 
/* 268 */       localClassDefinition != null; 
/* 269 */       localClassDefinition = localClassDefinition.getOuterClass())
/*     */     {
/* 277 */       MemberDefinition localMemberDefinition = localClassDefinition.getInnerClass(paramEnvironment, paramIdentifier);
/* 278 */       if ((localMemberDefinition != null) && (getScopeNumber(localClassDefinition) > i) && (
/* 279 */         (!paramBoolean) || (localMemberDefinition.getClassDefinition() == localClassDefinition)))
/*     */       {
/* 282 */         return localMemberDefinition;
/*     */       }
/*     */     }
/*     */ 
/* 286 */     return localLocalMember;
/*     */   }
/*     */ 
/*     */   public final MemberDefinition getField(Environment paramEnvironment, Identifier paramIdentifier)
/*     */     throws AmbiguousMember, ClassNotFound
/*     */   {
/* 294 */     return getFieldCommon(paramEnvironment, paramIdentifier, false);
/*     */   }
/*     */ 
/*     */   public final MemberDefinition getApparentField(Environment paramEnvironment, Identifier paramIdentifier)
/*     */     throws AmbiguousMember, ClassNotFound
/*     */   {
/* 303 */     return getFieldCommon(paramEnvironment, paramIdentifier, true);
/*     */   }
/*     */ 
/*     */   public boolean isInScope(LocalMember paramLocalMember)
/*     */   {
/* 310 */     for (LocalMember localLocalMember = this.locals; localLocalMember != null; localLocalMember = localLocalMember.prev) {
/* 311 */       if (paramLocalMember == localLocalMember) {
/* 312 */         return true;
/*     */       }
/*     */     }
/* 315 */     return false;
/*     */   }
/*     */ 
/*     */   public UplevelReference noteReference(Environment paramEnvironment, LocalMember paramLocalMember)
/*     */   {
/* 333 */     int i = !isInScope(paramLocalMember) ? -1 : paramLocalMember.scopeNumber;
/*     */ 
/* 339 */     Object localObject = null;
/* 340 */     int j = -1;
/* 341 */     for (Context localContext = this; localContext != null; localContext = localContext.prev) {
/* 342 */       if (j != localContext.frameNumber)
/*     */       {
/* 345 */         j = localContext.frameNumber;
/* 346 */         if (i >= j)
/*     */         {
/*     */           break;
/*     */         }
/*     */ 
/* 351 */         ClassDefinition localClassDefinition = localContext.field.getClassDefinition();
/* 352 */         UplevelReference localUplevelReference = localClassDefinition.getReference(paramLocalMember);
/* 353 */         localUplevelReference.noteReference(paramEnvironment, localContext);
/*     */ 
/* 356 */         if (localObject == null)
/* 357 */           localObject = localUplevelReference;
/*     */       }
/*     */     }
/* 360 */     return localObject;
/*     */   }
/*     */ 
/*     */   public Expression makeReference(Environment paramEnvironment, LocalMember paramLocalMember)
/*     */   {
/* 373 */     UplevelReference localUplevelReference = noteReference(paramEnvironment, paramLocalMember);
/*     */ 
/* 376 */     if (localUplevelReference != null)
/* 377 */       return localUplevelReference.makeLocalReference(paramEnvironment, this);
/* 378 */     if (idThis.equals(paramLocalMember.getName())) {
/* 379 */       return new ThisExpression(0L, paramLocalMember);
/*     */     }
/* 381 */     return new IdentifierExpression(0L, paramLocalMember);
/*     */   }
/*     */ 
/*     */   public Expression findOuterLink(Environment paramEnvironment, long paramLong, MemberDefinition paramMemberDefinition)
/*     */   {
/* 396 */     ClassDefinition localClassDefinition1 = paramMemberDefinition.getClassDefinition();
/*     */ 
/* 400 */     ClassDefinition localClassDefinition2 = localClassDefinition1
/* 399 */       .isTopLevel() ? null : !paramMemberDefinition
/* 398 */       .isConstructor() ? localClassDefinition1 : paramMemberDefinition.isStatic() ? null : 
/* 399 */       localClassDefinition1
/* 400 */       .getOuterClass();
/* 401 */     if (localClassDefinition2 == null) {
/* 402 */       return null;
/*     */     }
/* 404 */     return findOuterLink(paramEnvironment, paramLong, localClassDefinition2, paramMemberDefinition, false);
/*     */   }
/*     */ 
/*     */   private static boolean match(Environment paramEnvironment, ClassDefinition paramClassDefinition1, ClassDefinition paramClassDefinition2)
/*     */   {
/*     */     try
/*     */     {
/* 411 */       return (paramClassDefinition1 == paramClassDefinition2) || 
/* 411 */         (paramClassDefinition2
/* 411 */         .implementedBy(paramEnvironment, paramClassDefinition1
/* 411 */         .getClassDeclaration())); } catch (ClassNotFound localClassNotFound) {
/*     */     }
/* 413 */     return false;
/*     */   }
/*     */ 
/*     */   public Expression findOuterLink(Environment paramEnvironment, long paramLong, ClassDefinition paramClassDefinition, MemberDefinition paramMemberDefinition, boolean paramBoolean)
/*     */   {
/* 421 */     if (this.field.isStatic()) {
/* 422 */       if (paramMemberDefinition == null)
/*     */       {
/* 424 */         localObject1 = paramClassDefinition.getName().getFlatName().getName();
/* 425 */         paramEnvironment.error(paramLong, "undef.var", Identifier.lookup((Identifier)localObject1, idThis));
/* 426 */       } else if (paramMemberDefinition.isConstructor()) {
/* 427 */         paramEnvironment.error(paramLong, "no.outer.arg", paramClassDefinition, paramMemberDefinition.getClassDeclaration());
/* 428 */       } else if (paramMemberDefinition.isMethod()) {
/* 429 */         paramEnvironment.error(paramLong, "no.static.meth.access", paramMemberDefinition, paramMemberDefinition
/* 430 */           .getClassDeclaration());
/*     */       } else {
/* 432 */         paramEnvironment.error(paramLong, "no.static.field.access", paramMemberDefinition.getName(), paramMemberDefinition
/* 433 */           .getClassDeclaration());
/*     */       }
/*     */ 
/* 442 */       localObject1 = new ThisExpression(paramLong, this);
/* 443 */       ((Expression)localObject1).type = paramClassDefinition.getType();
/* 444 */       return localObject1;
/*     */     }
/*     */ 
/* 448 */     Object localObject1 = this.locals;
/*     */ 
/* 451 */     Object localObject2 = null;
/*     */ 
/* 454 */     Object localObject3 = null;
/*     */ 
/* 457 */     ClassDefinition localClassDefinition1 = null;
/*     */ 
/* 460 */     ClassDefinition localClassDefinition2 = null;
/* 461 */     if (this.field.isConstructor()) {
/* 462 */       localClassDefinition2 = this.field.getClassDefinition();
/*     */     }
/*     */ 
/* 465 */     if (!this.field.isMethod()) {
/* 466 */       localClassDefinition1 = this.field.getClassDefinition();
/* 467 */       localObject2 = new ThisExpression(paramLong, this);
/*     */     }
/*     */     while (true)
/*     */     {
/* 471 */       if (localObject2 == null)
/*     */       {
/* 473 */         while ((localObject1 != null) && (!idThis.equals(((LocalMember)localObject1).getName()))) {
/* 474 */           localObject1 = ((LocalMember)localObject1).prev;
/*     */         }
/* 476 */         if (localObject1 == null) {
/*     */           break;
/*     */         }
/* 479 */         localObject2 = new ThisExpression(paramLong, (LocalMember)localObject1);
/* 480 */         localClassDefinition1 = ((LocalMember)localObject1).getClassDefinition();
/* 481 */         localObject3 = localObject1;
/* 482 */         localObject1 = ((LocalMember)localObject1).prev;
/*     */       }
/*     */ 
/* 488 */       if ((localClassDefinition1 == paramClassDefinition) || ((!paramBoolean) && 
/* 489 */         (match(paramEnvironment, localClassDefinition1, paramClassDefinition))))
/*     */       {
/*     */         break;
/*     */       }
/*     */ 
/* 495 */       localObject4 = localClassDefinition1.findOuterMember();
/* 496 */       if (localObject4 == null) {
/* 497 */         localObject2 = null;
/*     */       }
/*     */       else {
/* 500 */         ClassDefinition localClassDefinition3 = localClassDefinition1;
/* 501 */         localClassDefinition1 = localClassDefinition3.getOuterClass();
/*     */ 
/* 503 */         if (localClassDefinition3 == localClassDefinition2)
/*     */         {
/* 507 */           Identifier localIdentifier = ((MemberDefinition)localObject4).getName();
/* 508 */           IdentifierExpression localIdentifierExpression = new IdentifierExpression(paramLong, localIdentifier);
/* 509 */           localIdentifierExpression.bind(paramEnvironment, this);
/* 510 */           localObject2 = localIdentifierExpression;
/*     */         } else {
/* 512 */           localObject2 = new FieldExpression(paramLong, (Expression)localObject2, (MemberDefinition)localObject4);
/*     */         }
/*     */       }
/*     */     }
/* 515 */     if (localObject2 != null)
/*     */     {
/* 519 */       return localObject2;
/*     */     }
/*     */ 
/* 522 */     if (paramMemberDefinition == null)
/*     */     {
/* 524 */       localObject4 = paramClassDefinition.getName().getFlatName().getName();
/* 525 */       paramEnvironment.error(paramLong, "undef.var", Identifier.lookup((Identifier)localObject4, idThis));
/* 526 */     } else if (paramMemberDefinition.isConstructor()) {
/* 527 */       paramEnvironment.error(paramLong, "no.outer.arg", paramClassDefinition, paramMemberDefinition.getClassDefinition());
/*     */     } else {
/* 529 */       paramEnvironment.error(paramLong, "no.static.field.access", paramMemberDefinition, this.field);
/*     */     }
/*     */ 
/* 533 */     Object localObject4 = new ThisExpression(paramLong, this);
/* 534 */     ((Expression)localObject4).type = paramClassDefinition.getType();
/* 535 */     return localObject4;
/*     */   }
/*     */ 
/*     */   public static boolean outerLinkExists(Environment paramEnvironment, ClassDefinition paramClassDefinition1, ClassDefinition paramClassDefinition2)
/*     */   {
/* 544 */     while (!match(paramEnvironment, paramClassDefinition2, paramClassDefinition1)) {
/* 545 */       if (paramClassDefinition2.isTopLevel()) {
/* 546 */         return false;
/*     */       }
/* 548 */       paramClassDefinition2 = paramClassDefinition2.getOuterClass();
/*     */     }
/* 550 */     return true;
/*     */   }
/*     */ 
/*     */   public ClassDefinition findScope(Environment paramEnvironment, ClassDefinition paramClassDefinition)
/*     */   {
/* 557 */     ClassDefinition localClassDefinition = this.field.getClassDefinition();
/* 558 */     while ((localClassDefinition != null) && (!match(paramEnvironment, localClassDefinition, paramClassDefinition))) {
/* 559 */       localClassDefinition = localClassDefinition.getOuterClass();
/*     */     }
/* 561 */     return localClassDefinition;
/*     */   }
/*     */ 
/*     */   Identifier resolveName(Environment paramEnvironment, Identifier paramIdentifier)
/*     */   {
/*     */     Object localObject;
/* 571 */     if (paramIdentifier.isQualified())
/*     */     {
/* 575 */       localObject = resolveName(paramEnvironment, paramIdentifier.getHead());
/*     */ 
/* 577 */       if (((Identifier)localObject).hasAmbigPrefix())
/*     */       {
/* 582 */         return localObject;
/*     */       }
/*     */ 
/* 585 */       if (!paramEnvironment.classExists((Identifier)localObject)) {
/* 586 */         return paramEnvironment.resolvePackageQualifiedName(paramIdentifier);
/*     */       }
/*     */       try
/*     */       {
/* 590 */         return paramEnvironment.getClassDefinition((Identifier)localObject)
/* 590 */           .resolveInnerClass(paramEnvironment, paramIdentifier
/* 590 */           .getTail());
/*     */       }
/*     */       catch (ClassNotFound localClassNotFound2) {
/* 593 */         return Identifier.lookupInner((Identifier)localObject, paramIdentifier.getTail());
/*     */       }
/*     */     }
/*     */ 
/*     */     try
/*     */     {
/* 599 */       localObject = getClassCommon(paramEnvironment, paramIdentifier, false);
/* 600 */       if (localObject != null) {
/* 601 */         return ((MemberDefinition)localObject).getInnerClass().getName();
/*     */       }
/*     */     }
/*     */     catch (ClassNotFound localClassNotFound1)
/*     */     {
/*     */     }
/*     */ 
/* 608 */     return paramEnvironment.resolveName(paramIdentifier);
/*     */   }
/*     */ 
/*     */   public Identifier getApparentClassName(Environment paramEnvironment, Identifier paramIdentifier)
/*     */   {
/*     */     Object localObject;
/* 619 */     if (paramIdentifier.isQualified())
/*     */     {
/* 623 */       localObject = getApparentClassName(paramEnvironment, paramIdentifier.getHead());
/*     */ 
/* 625 */       return localObject == null ? idNull : 
/* 625 */         Identifier.lookup((Identifier)localObject, paramIdentifier
/* 626 */         .getTail());
/*     */     }
/*     */ 
/*     */     try
/*     */     {
/* 631 */       localObject = getClassCommon(paramEnvironment, paramIdentifier, true);
/* 632 */       if (localObject != null) {
/* 633 */         return ((MemberDefinition)localObject).getInnerClass().getName();
/*     */       }
/*     */     }
/*     */     catch (ClassNotFound localClassNotFound)
/*     */     {
/*     */     }
/*     */ 
/* 640 */     Identifier localIdentifier = this.field.getClassDefinition().getTopClass().getName();
/* 641 */     if (localIdentifier.getName().equals(paramIdentifier)) {
/* 642 */       return localIdentifier;
/*     */     }
/* 644 */     return idNull;
/*     */   }
/*     */ 
/*     */   public void checkBackBranch(Environment paramEnvironment, Statement paramStatement, Vset paramVset1, Vset paramVset2)
/*     */   {
/* 655 */     for (LocalMember localLocalMember = this.locals; localLocalMember != null; localLocalMember = localLocalMember.prev)
/* 656 */       if ((localLocalMember.isBlankFinal()) && 
/* 657 */         (paramVset1
/* 657 */         .testVarUnassigned(localLocalMember.number)) && 
/* 658 */         (!paramVset2
/* 658 */         .testVarUnassigned(localLocalMember.number)))
/*     */       {
/* 659 */         paramEnvironment.error(paramStatement.where, "assign.to.blank.final.in.loop", localLocalMember
/* 660 */           .getName());
/*     */       }
/*     */   }
/*     */ 
/*     */   public boolean canReach(Environment paramEnvironment, MemberDefinition paramMemberDefinition)
/*     */   {
/* 670 */     return this.field.canReach(paramEnvironment, paramMemberDefinition);
/*     */   }
/*     */ 
/*     */   public Context getLabelContext(Identifier paramIdentifier)
/*     */   {
/* 679 */     for (Context localContext = this; localContext != null; localContext = localContext.prev) {
/* 680 */       if ((localContext.node != null) && ((localContext.node instanceof Statement)) && 
/* 681 */         (((Statement)localContext.node).hasLabel(paramIdentifier))) {
/* 682 */         return localContext;
/*     */       }
/*     */     }
/* 685 */     return null;
/*     */   }
/*     */ 
/*     */   public Context getBreakContext(Identifier paramIdentifier)
/*     */   {
/* 693 */     if (paramIdentifier != null) {
/* 694 */       return getLabelContext(paramIdentifier);
/*     */     }
/* 696 */     for (Context localContext = this; localContext != null; localContext = localContext.prev) {
/* 697 */       if (localContext.node != null) {
/* 698 */         switch (localContext.node.op) {
/*     */         case 92:
/*     */         case 93:
/*     */         case 94:
/*     */         case 95:
/* 703 */           return localContext;
/*     */         }
/*     */       }
/*     */     }
/* 707 */     return null;
/*     */   }
/*     */ 
/*     */   public Context getContinueContext(Identifier paramIdentifier)
/*     */   {
/* 715 */     if (paramIdentifier != null) {
/* 716 */       return getLabelContext(paramIdentifier);
/*     */     }
/* 718 */     for (Context localContext = this; localContext != null; localContext = localContext.prev) {
/* 719 */       if (localContext.node != null) {
/* 720 */         switch (localContext.node.op) {
/*     */         case 92:
/*     */         case 93:
/*     */         case 94:
/* 724 */           return localContext;
/*     */         }
/*     */       }
/*     */     }
/* 728 */     return null;
/*     */   }
/*     */ 
/*     */   public CheckContext getReturnContext()
/*     */   {
/* 736 */     for (Context localContext = this; localContext != null; localContext = localContext.prev)
/*     */     {
/* 738 */       if ((localContext.node != null) && (localContext.node.op == 47)) {
/* 739 */         return (CheckContext)localContext;
/*     */       }
/*     */     }
/* 742 */     return null;
/*     */   }
/*     */ 
/*     */   public CheckContext getTryExitContext()
/*     */   {
/* 756 */     for (Context localContext = this; 
/* 757 */       (localContext != null) && (localContext.node != null) && (localContext.node.op != 47); 
/* 758 */       localContext = localContext.prev) {
/* 759 */       if (localContext.node.op == 101) {
/* 760 */         return (CheckContext)localContext;
/*     */       }
/*     */     }
/* 763 */     return null;
/*     */   }
/*     */ 
/*     */   Context getInlineContext()
/*     */   {
/* 770 */     for (Context localContext = this; localContext != null; localContext = localContext.prev) {
/* 771 */       if (localContext.node != null) {
/* 772 */         switch (localContext.node.op) {
/*     */         case 150:
/*     */         case 151:
/* 775 */           return localContext;
/*     */         }
/*     */       }
/*     */     }
/* 779 */     return null;
/*     */   }
/*     */ 
/*     */   Context getInlineMemberContext(MemberDefinition paramMemberDefinition)
/*     */   {
/* 786 */     for (Context localContext = this; localContext != null; localContext = localContext.prev) {
/* 787 */       if (localContext.node != null) {
/* 788 */         switch (localContext.node.op) {
/*     */         case 150:
/* 790 */           if (((InlineMethodExpression)localContext.node).field.equals(paramMemberDefinition)) {
/* 791 */             return localContext;
/*     */           }
/*     */           break;
/*     */         case 151:
/* 795 */           if (((InlineNewInstanceExpression)localContext.node).field.equals(paramMemberDefinition))
/* 796 */             return localContext;
/*     */           break;
/*     */         }
/*     */       }
/*     */     }
/* 801 */     return null;
/*     */   }
/*     */ 
/*     */   public final Vset removeAdditionalVars(Vset paramVset)
/*     */   {
/* 809 */     return paramVset.removeAdditionalVars(this.varNumber);
/*     */   }
/*     */ 
/*     */   public final int getVarNumber() {
/* 813 */     return this.varNumber;
/*     */   }
/*     */ 
/*     */   public int getThisNumber()
/*     */   {
/* 820 */     LocalMember localLocalMember = getLocalField(idThis);
/* 821 */     if ((localLocalMember != null) && 
/* 822 */       (localLocalMember
/* 822 */       .getClassDefinition() == this.field.getClassDefinition())) {
/* 823 */       return localLocalMember.number;
/*     */     }
/*     */ 
/* 826 */     return this.varNumber;
/*     */   }
/*     */ 
/*     */   public final MemberDefinition getField()
/*     */   {
/* 833 */     return this.field;
/*     */   }
/*     */ 
/*     */   public static Environment newEnvironment(Environment paramEnvironment, Context paramContext)
/*     */   {
/* 843 */     return new ContextEnvironment(paramEnvironment, paramContext);
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     sun.tools.tree.Context
 * JD-Core Version:    0.6.2
 */