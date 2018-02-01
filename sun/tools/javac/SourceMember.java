/*     */ package sun.tools.javac;
/*     */ 
/*     */ import java.io.PrintStream;
/*     */ import java.util.Enumeration;
/*     */ import java.util.Hashtable;
/*     */ import java.util.Vector;
/*     */ import sun.tools.asm.Assembler;
/*     */ import sun.tools.java.ClassDeclaration;
/*     */ import sun.tools.java.ClassDefinition;
/*     */ import sun.tools.java.ClassNotFound;
/*     */ import sun.tools.java.CompilerError;
/*     */ import sun.tools.java.Constants;
/*     */ import sun.tools.java.Environment;
/*     */ import sun.tools.java.Identifier;
/*     */ import sun.tools.java.IdentifierToken;
/*     */ import sun.tools.java.MemberDefinition;
/*     */ import sun.tools.java.Type;
/*     */ import sun.tools.tree.Context;
/*     */ import sun.tools.tree.Expression;
/*     */ import sun.tools.tree.ExpressionStatement;
/*     */ import sun.tools.tree.LocalMember;
/*     */ import sun.tools.tree.MethodExpression;
/*     */ import sun.tools.tree.Node;
/*     */ import sun.tools.tree.NullExpression;
/*     */ import sun.tools.tree.Statement;
/*     */ import sun.tools.tree.SuperExpression;
/*     */ import sun.tools.tree.UplevelReference;
/*     */ import sun.tools.tree.Vset;
/*     */ 
/*     */ @Deprecated
/*     */ public class SourceMember extends MemberDefinition
/*     */   implements Constants
/*     */ {
/*     */   Vector args;
/*     */   MemberDefinition abstractSource;
/*     */   int status;
/*     */   static final int PARSED = 0;
/*     */   static final int CHECKING = 1;
/*     */   static final int CHECKED = 2;
/*     */   static final int INLINING = 3;
/*     */   static final int INLINED = 4;
/*     */   static final int ERROR = 5;
/* 160 */   LocalMember outerThisArg = null;
/*     */ 
/* 322 */   public boolean resolved = false;
/*     */ 
/*     */   public Vector getArguments()
/*     */   {
/*  68 */     return this.args;
/*     */   }
/*     */ 
/*     */   public SourceMember(long paramLong, ClassDefinition paramClassDefinition, String paramString, int paramInt, Type paramType, Identifier paramIdentifier, Vector paramVector, IdentifierToken[] paramArrayOfIdentifierToken, Node paramNode)
/*     */   {
/*  79 */     super(paramLong, paramClassDefinition, paramInt, paramType, paramIdentifier, paramArrayOfIdentifierToken, paramNode);
/*  80 */     this.documentation = paramString;
/*  81 */     this.args = paramVector;
/*     */ 
/*  84 */     if (ClassDefinition.containsDeprecated(this.documentation))
/*  85 */       this.modifiers |= 262144;
/*     */   }
/*     */ 
/*     */   void createArgumentFields(Vector paramVector)
/*     */   {
/*  91 */     if (isMethod()) {
/*  92 */       this.args = new Vector();
/*     */ 
/*  94 */       if ((isConstructor()) || ((!isStatic()) && (!isInitializer()))) {
/*  95 */         this.args.addElement(((SourceClass)this.clazz).getThisArgument());
/*     */       }
/*     */ 
/*  98 */       if (paramVector != null) {
/*  99 */         Enumeration localEnumeration = paramVector.elements();
/* 100 */         Type[] arrayOfType = getType().getArgumentTypes();
/* 101 */         for (int i = 0; i < arrayOfType.length; i++) {
/* 102 */           Object localObject = localEnumeration.nextElement();
/* 103 */           if ((localObject instanceof LocalMember))
/*     */           {
/* 106 */             this.args = paramVector;
/*     */             return;
/*     */           }
/*     */           Identifier localIdentifier;
/*     */           int j;
/*     */           long l;
/* 112 */           if ((localObject instanceof Identifier))
/*     */           {
/* 114 */             localIdentifier = (Identifier)localObject;
/* 115 */             j = 0;
/* 116 */             l = getWhere();
/*     */           } else {
/* 118 */             IdentifierToken localIdentifierToken = (IdentifierToken)localObject;
/* 119 */             localIdentifier = localIdentifierToken.getName();
/* 120 */             j = localIdentifierToken.getModifiers();
/* 121 */             l = localIdentifierToken.getWhere();
/*     */           }
/* 123 */           this.args.addElement(new LocalMember(l, this.clazz, j, arrayOfType[i], localIdentifier));
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   public LocalMember getOuterThisArg()
/*     */   {
/* 167 */     return this.outerThisArg;
/*     */   }
/*     */ 
/*     */   void addOuterThis()
/*     */   {
/* 177 */     UplevelReference localUplevelReference = this.clazz.getReferences();
/*     */ 
/* 180 */     while ((localUplevelReference != null) && 
/* 181 */       (!localUplevelReference
/* 181 */       .isClientOuterField())) {
/* 182 */       localUplevelReference = localUplevelReference.getNext();
/*     */     }
/*     */ 
/* 186 */     if (localUplevelReference == null) {
/* 187 */       return;
/*     */     }
/*     */ 
/* 191 */     Type[] arrayOfType1 = this.type.getArgumentTypes();
/*     */ 
/* 194 */     Type[] arrayOfType2 = new Type[arrayOfType1.length + 1];
/*     */ 
/* 196 */     LocalMember localLocalMember = localUplevelReference.getLocalArgument();
/* 197 */     this.outerThisArg = localLocalMember;
/*     */ 
/* 202 */     this.args.insertElementAt(localLocalMember, 1);
/* 203 */     arrayOfType2[0] = localLocalMember.getType();
/*     */ 
/* 206 */     for (int i = 0; i < arrayOfType1.length; i++) {
/* 207 */       arrayOfType2[(i + 1)] = arrayOfType1[i];
/*     */     }
/*     */ 
/* 210 */     this.type = Type.tMethod(this.type.getReturnType(), arrayOfType2);
/*     */   }
/*     */ 
/*     */   void addUplevelArguments()
/*     */   {
/* 222 */     UplevelReference localUplevelReference1 = this.clazz.getReferences();
/* 223 */     this.clazz.getReferencesFrozen();
/*     */ 
/* 226 */     int i = 0;
/* 227 */     for (Object localObject = localUplevelReference1; localObject != null; localObject = ((UplevelReference)localObject).getNext()) {
/* 228 */       if (!((UplevelReference)localObject).isClientOuterField()) {
/* 229 */         i++;
/*     */       }
/*     */     }
/*     */ 
/* 233 */     if (i == 0)
/*     */     {
/* 235 */       return;
/*     */     }
/*     */ 
/* 239 */     localObject = this.type.getArgumentTypes();
/*     */ 
/* 242 */     Type[] arrayOfType = new Type[localObject.length + i];
/*     */ 
/* 246 */     int j = 0;
/* 247 */     for (UplevelReference localUplevelReference2 = localUplevelReference1; localUplevelReference2 != null; localUplevelReference2 = localUplevelReference2.getNext()) {
/* 248 */       if (!localUplevelReference2.isClientOuterField()) {
/* 249 */         LocalMember localLocalMember = localUplevelReference2.getLocalArgument();
/*     */ 
/* 251 */         this.args.insertElementAt(localLocalMember, 1 + j);
/* 252 */         arrayOfType[j] = localLocalMember.getType();
/*     */ 
/* 254 */         j++;
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 259 */     for (int k = 0; k < localObject.length; k++) {
/* 260 */       arrayOfType[(j + k)] = localObject[k];
/*     */     }
/*     */ 
/* 263 */     this.type = Type.tMethod(this.type.getReturnType(), arrayOfType);
/*     */   }
/*     */ 
/*     */   public SourceMember(ClassDefinition paramClassDefinition)
/*     */   {
/* 270 */     super(paramClassDefinition);
/*     */   }
/*     */ 
/*     */   public SourceMember(MemberDefinition paramMemberDefinition, ClassDefinition paramClassDefinition, Environment paramEnvironment)
/*     */   {
/* 279 */     this(paramMemberDefinition.getWhere(), paramClassDefinition, paramMemberDefinition.getDocumentation(), paramMemberDefinition
/* 280 */       .getModifiers() | 0x400, paramMemberDefinition.getType(), paramMemberDefinition.getName(), null, paramMemberDefinition
/* 281 */       .getExceptionIds(), null);
/* 282 */     this.args = paramMemberDefinition.getArguments();
/* 283 */     this.abstractSource = paramMemberDefinition;
/* 284 */     this.exp = paramMemberDefinition.getExceptions(paramEnvironment);
/*     */   }
/*     */ 
/*     */   public ClassDeclaration[] getExceptions(Environment paramEnvironment)
/*     */   {
/* 291 */     if ((!isMethod()) || (this.exp != null)) {
/* 292 */       return this.exp;
/*     */     }
/* 294 */     if (this.expIds == null)
/*     */     {
/* 296 */       this.exp = new ClassDeclaration[0];
/* 297 */       return this.exp;
/*     */     }
/*     */ 
/* 300 */     paramEnvironment = ((SourceClass)getClassDefinition()).setupEnv(paramEnvironment);
/* 301 */     this.exp = new ClassDeclaration[this.expIds.length];
/* 302 */     for (int i = 0; i < this.exp.length; i++) {
/* 303 */       Identifier localIdentifier1 = this.expIds[i].getName();
/* 304 */       Identifier localIdentifier2 = getClassDefinition().resolveName(paramEnvironment, localIdentifier1);
/* 305 */       this.exp[i] = paramEnvironment.getClassDeclaration(localIdentifier2);
/*     */     }
/* 307 */     return this.exp;
/*     */   }
/*     */ 
/*     */   public void setExceptions(ClassDeclaration[] paramArrayOfClassDeclaration)
/*     */   {
/* 314 */     this.exp = paramArrayOfClassDeclaration;
/*     */   }
/*     */ 
/*     */   public void resolveTypeStructure(Environment paramEnvironment)
/*     */   {
/* 325 */     paramEnvironment.dtEnter("SourceMember.resolveTypeStructure: " + this);
/*     */ 
/* 331 */     if (this.resolved) {
/* 332 */       paramEnvironment.dtEvent("SourceMember.resolveTypeStructure: OK " + this);
/*     */ 
/* 336 */       throw new CompilerError("multiple member type resolution");
/*     */     }
/*     */ 
/* 339 */     paramEnvironment.dtEvent("SourceMember.resolveTypeStructure: RESOLVING " + this);
/* 340 */     this.resolved = true;
/*     */ 
/* 343 */     super.resolveTypeStructure(paramEnvironment);
/*     */     Object localObject;
/* 344 */     if (isInnerClass()) {
/* 345 */       localObject = getInnerClass();
/* 346 */       if (((localObject instanceof SourceClass)) && (!((ClassDefinition)localObject).isLocal())) {
/* 347 */         ((SourceClass)localObject).resolveTypeStructure(paramEnvironment);
/*     */       }
/* 349 */       this.type = this.innerClass.getType();
/*     */     }
/*     */     else
/*     */     {
/* 356 */       this.type = paramEnvironment.resolveNames(getClassDefinition(), this.type, isSynthetic());
/*     */ 
/* 359 */       getExceptions(paramEnvironment);
/*     */ 
/* 361 */       if (isMethod()) {
/* 362 */         localObject = this.args; this.args = null;
/* 363 */         createArgumentFields((Vector)localObject);
/*     */ 
/* 365 */         if (isConstructor()) {
/* 366 */           addOuterThis();
/*     */         }
/*     */       }
/*     */     }
/* 370 */     paramEnvironment.dtExit("SourceMember.resolveTypeStructure: " + this);
/*     */   }
/*     */ 
/*     */   public ClassDeclaration getDefiningClassDeclaration()
/*     */   {
/* 377 */     if (this.abstractSource == null) {
/* 378 */       return super.getDefiningClassDeclaration();
/*     */     }
/* 380 */     return this.abstractSource.getDefiningClassDeclaration();
/*     */   }
/*     */ 
/*     */   public boolean reportDeprecated(Environment paramEnvironment)
/*     */   {
/* 389 */     return false;
/*     */   }
/*     */ 
/*     */   public void check(Environment paramEnvironment)
/*     */     throws ClassNotFound
/*     */   {
/* 400 */     paramEnvironment.dtEnter("SourceMember.check: " + 
/* 401 */       getName() + ", status = " + this.status);
/*     */ 
/* 403 */     if (this.status == 0) {
/* 404 */       if ((isSynthetic()) && (getValue() == null))
/*     */       {
/* 406 */         this.status = 2;
/*     */ 
/* 408 */         paramEnvironment.dtExit("SourceMember.check: BREAKING CYCLE");
/* 409 */         return;
/*     */       }
/* 411 */       paramEnvironment.dtEvent("SourceMember.check: CHECKING CLASS");
/* 412 */       this.clazz.check(paramEnvironment);
/* 413 */       if (this.status == 0) {
/* 414 */         if (getClassDefinition().getError()) {
/* 415 */           this.status = 5;
/*     */         }
/*     */         else {
/* 418 */           paramEnvironment.dtExit("SourceMember.check: CHECK FAILED");
/* 419 */           throw new CompilerError("check failed");
/*     */         }
/*     */       }
/*     */     }
/* 423 */     paramEnvironment.dtExit("SourceMember.check: DONE " + 
/* 424 */       getName() + ", status = " + this.status);
/*     */   }
/*     */ 
/*     */   public Vset check(Environment paramEnvironment, Context paramContext, Vset paramVset)
/*     */     throws ClassNotFound
/*     */   {
/* 434 */     paramEnvironment.dtEvent("SourceMember.check: MEMBER " + 
/* 435 */       getName() + ", status = " + this.status);
/* 436 */     if (this.status == 0)
/*     */     {
/*     */       Object localObject1;
/* 437 */       if (isInnerClass())
/*     */       {
/* 439 */         localObject1 = getInnerClass();
/* 440 */         if (((localObject1 instanceof SourceClass)) && (!((ClassDefinition)localObject1).isLocal()) && 
/* 441 */           (((ClassDefinition)localObject1)
/* 441 */           .isInsideLocal())) {
/* 442 */           this.status = 1;
/* 443 */           paramVset = ((SourceClass)localObject1).checkInsideClass(paramEnvironment, paramContext, paramVset);
/*     */         }
/* 445 */         this.status = 2;
/* 446 */         return paramVset;
/*     */       }
/* 448 */       if (paramEnvironment.dump()) {
/* 449 */         System.out.println("[check field " + getClassDeclaration().getName() + "." + getName() + "]");
/* 450 */         if (getValue() != null) {
/* 451 */           getValue().print(System.out);
/* 452 */           System.out.println();
/*     */         }
/*     */       }
/* 455 */       paramEnvironment = new Environment(paramEnvironment, this);
/*     */ 
/* 461 */       paramEnvironment.resolve(this.where, getClassDefinition(), getType());
/*     */       Object localObject5;
/* 465 */       if (isMethod())
/*     */       {
/* 467 */         localObject1 = paramEnvironment
/* 467 */           .getClassDeclaration(idJavaLangThrowable);
/*     */ 
/* 468 */         ClassDeclaration[] arrayOfClassDeclaration = getExceptions(paramEnvironment);
/* 469 */         for (int k = 0; k < arrayOfClassDeclaration.length; k++)
/*     */         {
/* 471 */           long l1 = getWhere();
/* 472 */           if ((this.expIds != null) && (k < this.expIds.length))
/* 473 */             l1 = IdentifierToken.getWhere(this.expIds[k], l1);
/*     */           try
/*     */           {
/* 476 */             localObject5 = arrayOfClassDeclaration[k].getClassDefinition(paramEnvironment);
/*     */ 
/* 482 */             paramEnvironment.resolveByName(l1, getClassDefinition(), ((ClassDefinition)localObject5).getName());
/*     */           }
/*     */           catch (ClassNotFound localClassNotFound) {
/* 485 */             paramEnvironment.error(l1, "class.not.found", localClassNotFound.name, "throws");
/* 486 */             break;
/*     */           }
/* 488 */           ((ClassDefinition)localObject5).noteUsedBy(getClassDefinition(), l1, paramEnvironment);
/*     */ 
/* 490 */           if (!getClassDefinition()
/* 490 */             .canAccess(paramEnvironment, ((ClassDefinition)localObject5)
/* 490 */             .getClassDeclaration()))
/* 491 */             paramEnvironment.error(l1, "cant.access.class", localObject5);
/* 492 */           else if (!((ClassDefinition)localObject5).subClassOf(paramEnvironment, (ClassDeclaration)localObject1)) {
/* 493 */             paramEnvironment.error(l1, "throws.not.throwable", localObject5);
/*     */           }
/*     */         }
/*     */       }
/*     */ 
/* 498 */       this.status = 1;
/*     */       Object localObject4;
/*     */       Object localObject6;
/*     */       Object localObject7;
/* 500 */       if ((isMethod()) && (this.args != null)) {
/* 501 */         int i = this.args.size();
/*     */ 
/* 503 */         for (int j = 0; j < i; j++) {
/* 504 */           localObject4 = (LocalMember)this.args.elementAt(j);
/* 505 */           localObject5 = ((LocalMember)localObject4).getName();
/* 506 */           for (int n = j + 1; n < i; n++) {
/* 507 */             localObject6 = (LocalMember)this.args.elementAt(n);
/* 508 */             localObject7 = ((LocalMember)localObject6).getName();
/* 509 */             if (localObject5.equals(localObject7)) {
/* 510 */               paramEnvironment.error(((LocalMember)localObject6).getWhere(), "duplicate.argument", localObject5);
/*     */ 
/* 512 */               break label537;
/*     */             }
/*     */           }
/*     */         }
/*     */       }
/*     */ 
/* 518 */       label537: if (getValue() != null) {
/* 519 */         paramContext = new Context(paramContext, this);
/*     */         Object localObject2;
/*     */         Object localObject3;
/*     */         ClassDeclaration localClassDeclaration2;
/*     */         Object localObject8;
/*     */         Object localObject9;
/*     */         ClassDeclaration localClassDeclaration1;
/* 521 */         if (isMethod()) {
/* 522 */           localObject2 = (Statement)getValue();
/*     */ 
/* 526 */           for (localObject3 = this.args.elements(); ((Enumeration)localObject3).hasMoreElements(); ) {
/* 527 */             localObject4 = (LocalMember)((Enumeration)localObject3).nextElement();
/* 528 */             paramVset.addVar(paramContext.declare(paramEnvironment, (LocalMember)localObject4));
/*     */           }
/*     */ 
/* 531 */           if (isConstructor())
/*     */           {
/* 534 */             paramVset.clearVar(paramContext.getThisNumber());
/*     */ 
/* 538 */             localObject3 = ((Statement)localObject2).firstConstructor();
/* 539 */             if ((localObject3 == null) && 
/* 540 */               (getClassDefinition().getSuperClass() != null)) {
/* 541 */               localObject3 = getDefaultSuperCall(paramEnvironment);
/* 542 */               localObject4 = new ExpressionStatement(this.where, (Expression)localObject3);
/*     */ 
/* 544 */               localObject2 = Statement.insertStatement((Statement)localObject4, (Statement)localObject2);
/* 545 */               setValue((Node)localObject2);
/*     */             }
/*     */ 
/*     */           }
/*     */ 
/* 550 */           localObject3 = getExceptions(paramEnvironment);
/* 551 */           int m = localObject3.length > 3 ? 17 : 7;
/* 552 */           localObject5 = new Hashtable(m);
/*     */ 
/* 554 */           paramVset = ((Statement)localObject2).checkMethod(paramEnvironment, paramContext, paramVset, (Hashtable)localObject5);
/*     */ 
/* 557 */           localClassDeclaration2 = paramEnvironment
/* 557 */             .getClassDeclaration(idJavaLangError);
/*     */ 
/* 559 */           localObject6 = paramEnvironment
/* 559 */             .getClassDeclaration(idJavaLangRuntimeException);
/*     */ 
/* 561 */           for (localObject7 = ((Hashtable)localObject5).keys(); ((Enumeration)localObject7).hasMoreElements(); ) {
/* 562 */             localObject8 = (ClassDeclaration)((Enumeration)localObject7).nextElement();
/* 563 */             localObject9 = ((ClassDeclaration)localObject8).getClassDefinition(paramEnvironment);
/* 564 */             if ((!((ClassDefinition)localObject9).subClassOf(paramEnvironment, localClassDeclaration2)) && 
/* 565 */               (!((ClassDefinition)localObject9)
/* 565 */               .subClassOf(paramEnvironment, (ClassDeclaration)localObject6)))
/*     */             {
/* 569 */               int i1 = 0;
/* 570 */               if (!isInitializer()) {
/* 571 */                 for (int i2 = 0; i2 < localObject3.length; i2++) {
/* 572 */                   if (((ClassDefinition)localObject9).subClassOf(paramEnvironment, localObject3[i2])) {
/* 573 */                     i1 = 1;
/*     */                   }
/*     */                 }
/*     */               }
/* 577 */               if (i1 == 0) {
/* 578 */                 Node localNode = (Node)((Hashtable)localObject5).get(localObject8);
/* 579 */                 long l2 = localNode.getWhere();
/*     */                 String str;
/* 582 */                 if (isConstructor())
/*     */                 {
/* 584 */                   if (l2 == 
/* 584 */                     getClassDefinition().getWhere())
/*     */                   {
/* 593 */                     str = "def.constructor.exception";
/*     */                   }
/*     */                   else
/* 596 */                     str = "constructor.exception";
/*     */                 }
/* 598 */                 else if (isInitializer())
/*     */                 {
/* 600 */                   str = "initializer.exception";
/*     */                 }
/*     */                 else {
/* 603 */                   str = "uncaught.exception";
/*     */                 }
/* 605 */                 paramEnvironment.error(l2, str, ((ClassDeclaration)localObject8).getName());
/*     */               }
/*     */             }
/*     */           }
/*     */         } else { localObject2 = new Hashtable(3);
/* 610 */           localObject3 = (Expression)getValue();
/*     */ 
/* 612 */           paramVset = ((Expression)localObject3).checkInitializer(paramEnvironment, paramContext, paramVset, 
/* 613 */             getType(), (Hashtable)localObject2);
/* 614 */           setValue(((Expression)localObject3).convert(paramEnvironment, paramContext, getType(), (Expression)localObject3));
/*     */ 
/* 622 */           if ((isStatic()) && (isFinal()) && (!this.clazz.isTopLevel()) && 
/* 623 */             (!((Expression)getValue()).isConstant())) {
/* 624 */             paramEnvironment.error(this.where, "static.inner.field", getName(), this);
/* 625 */             setValue(null);
/*     */           }
/*     */ 
/* 633 */           localClassDeclaration1 = paramEnvironment
/* 633 */             .getClassDeclaration(idJavaLangThrowable);
/*     */ 
/* 635 */           localObject5 = paramEnvironment
/* 635 */             .getClassDeclaration(idJavaLangError);
/*     */ 
/* 637 */           localClassDeclaration2 = paramEnvironment
/* 637 */             .getClassDeclaration(idJavaLangRuntimeException);
/*     */ 
/* 639 */           for (localObject6 = ((Hashtable)localObject2).keys(); ((Enumeration)localObject6).hasMoreElements(); ) {
/* 640 */             localObject7 = (ClassDeclaration)((Enumeration)localObject6).nextElement();
/* 641 */             localObject8 = ((ClassDeclaration)localObject7).getClassDefinition(paramEnvironment);
/*     */ 
/* 643 */             if ((!((ClassDefinition)localObject8).subClassOf(paramEnvironment, (ClassDeclaration)localObject5)) && 
/* 644 */               (!((ClassDefinition)localObject8)
/* 644 */               .subClassOf(paramEnvironment, localClassDeclaration2)) && 
/* 645 */               (((ClassDefinition)localObject8)
/* 645 */               .subClassOf(paramEnvironment, localClassDeclaration1)))
/*     */             {
/* 646 */               localObject9 = (Node)((Hashtable)localObject2).get(localObject7);
/* 647 */               paramEnvironment.error(((Node)localObject9).getWhere(), "initializer.exception", ((ClassDeclaration)localObject7)
/* 648 */                 .getName());
/*     */             }
/*     */           }
/*     */         }
/* 652 */         if (paramEnvironment.dump()) {
/* 653 */           getValue().print(System.out);
/* 654 */           System.out.println();
/*     */         }
/*     */       }
/* 657 */       this.status = (getClassDefinition().getError() ? 5 : 2);
/*     */     }
/*     */ 
/* 662 */     if ((isInitializer()) && (paramVset.isDeadEnd())) {
/* 663 */       paramEnvironment.error(this.where, "init.no.normal.completion");
/* 664 */       paramVset = paramVset.clearDeadEnd();
/*     */     }
/*     */ 
/* 667 */     return paramVset;
/*     */   }
/*     */ 
/*     */   private Expression getDefaultSuperCall(Environment paramEnvironment)
/*     */   {
/* 672 */     SuperExpression localSuperExpression = null;
/* 673 */     ClassDefinition localClassDefinition1 = getClassDefinition().getSuperClass().getClassDefinition();
/*     */ 
/* 677 */     ClassDefinition localClassDefinition2 = localClassDefinition1
/* 676 */       .isTopLevel() ? null : localClassDefinition1 == null ? null : 
/* 676 */       localClassDefinition1
/* 677 */       .getOuterClass();
/* 678 */     ClassDefinition localClassDefinition3 = getClassDefinition();
/* 679 */     if ((localClassDefinition2 != null) && (!Context.outerLinkExists(paramEnvironment, localClassDefinition2, localClassDefinition3))) {
/* 680 */       localSuperExpression = new SuperExpression(this.where, new NullExpression(this.where));
/* 681 */       paramEnvironment.error(this.where, "no.default.outer.arg", localClassDefinition2, getClassDefinition());
/*     */     }
/* 683 */     if (localSuperExpression == null) {
/* 684 */       localSuperExpression = new SuperExpression(this.where);
/*     */     }
/* 686 */     return new MethodExpression(this.where, localSuperExpression, idInit, new Expression[0]);
/*     */   }
/*     */ 
/*     */   void inline(Environment paramEnvironment)
/*     */     throws ClassNotFound
/*     */   {
/* 693 */     switch (this.status) {
/*     */     case 0:
/* 695 */       check(paramEnvironment);
/* 696 */       inline(paramEnvironment);
/* 697 */       break;
/*     */     case 2:
/* 700 */       if (paramEnvironment.dump()) {
/* 701 */         System.out.println("[inline field " + getClassDeclaration().getName() + "." + getName() + "]");
/*     */       }
/* 703 */       this.status = 3;
/* 704 */       paramEnvironment = new Environment(paramEnvironment, this);
/*     */       Object localObject1;
/*     */       Context localContext;
/*     */       Object localObject2;
/* 706 */       if (isMethod()) {
/* 707 */         if ((!isNative()) && (!isAbstract())) {
/* 708 */           localObject1 = (Statement)getValue();
/* 709 */           localContext = new Context((Context)null, this);
/* 710 */           for (localObject2 = this.args.elements(); ((Enumeration)localObject2).hasMoreElements(); ) {
/* 711 */             LocalMember localLocalMember = (LocalMember)((Enumeration)localObject2).nextElement();
/* 712 */             localContext.declare(paramEnvironment, localLocalMember);
/*     */           }
/* 714 */           setValue(((Statement)localObject1).inline(paramEnvironment, localContext));
/*     */         }
/*     */       } else { if (isInnerClass())
/*     */         {
/* 718 */           localObject1 = getInnerClass();
/* 719 */           if (((localObject1 instanceof SourceClass)) && (!((ClassDefinition)localObject1).isLocal()) && 
/* 720 */             (((ClassDefinition)localObject1)
/* 720 */             .isInsideLocal())) {
/* 721 */             this.status = 3;
/* 722 */             ((SourceClass)localObject1).inlineLocalClass(paramEnvironment);
/*     */           }
/* 724 */           this.status = 4;
/* 725 */           break;
/*     */         }
/* 727 */         if (getValue() != null) {
/* 728 */           localObject1 = new Context((Context)null, this);
/* 729 */           if (!isStatic())
/*     */           {
/* 731 */             localContext = new Context((Context)localObject1, this);
/*     */ 
/* 733 */             localObject2 = ((SourceClass)this.clazz)
/* 733 */               .getThisArgument();
/* 734 */             localContext.declare(paramEnvironment, (LocalMember)localObject2);
/* 735 */             setValue(((Expression)getValue())
/* 736 */               .inlineValue(paramEnvironment, localContext));
/*     */           }
/*     */           else {
/* 738 */             setValue(((Expression)getValue())
/* 739 */               .inlineValue(paramEnvironment, (Context)localObject1));
/*     */           }
/*     */         }
/*     */       }
/*     */ 
/* 743 */       if (paramEnvironment.dump()) {
/* 744 */         System.out.println("[inlined field " + getClassDeclaration().getName() + "." + getName() + "]");
/* 745 */         if (getValue() != null) {
/* 746 */           getValue().print(System.out);
/* 747 */           System.out.println();
/*     */         } else {
/* 749 */           System.out.println("<empty>");
/*     */         }
/*     */       }
/* 752 */       this.status = 4;
/*     */     }
/*     */   }
/*     */ 
/*     */   public Node getValue(Environment paramEnvironment)
/*     */     throws ClassNotFound
/*     */   {
/* 761 */     Node localNode = getValue();
/* 762 */     if ((localNode != null) && (this.status != 4))
/*     */     {
/* 764 */       paramEnvironment = ((SourceClass)this.clazz).setupEnv(paramEnvironment);
/* 765 */       inline(paramEnvironment);
/* 766 */       localNode = this.status == 4 ? getValue() : null;
/*     */     }
/* 768 */     return localNode;
/*     */   }
/*     */ 
/*     */   public boolean isInlineable(Environment paramEnvironment, boolean paramBoolean) throws ClassNotFound {
/* 772 */     if (super.isInlineable(paramEnvironment, paramBoolean)) {
/* 773 */       getValue(paramEnvironment);
/* 774 */       return (this.status == 4) && (!getClassDefinition().getError());
/*     */     }
/* 776 */     return false;
/*     */   }
/*     */ 
/*     */   public Object getInitialValue()
/*     */   {
/* 784 */     if ((isMethod()) || (getValue() == null) || (!isFinal()) || (this.status != 4)) {
/* 785 */       return null;
/*     */     }
/* 787 */     return ((Expression)getValue()).getValue();
/*     */   }
/*     */ 
/*     */   public void code(Environment paramEnvironment, Assembler paramAssembler)
/*     */     throws ClassNotFound
/*     */   {
/* 794 */     switch (this.status) {
/*     */     case 0:
/* 796 */       check(paramEnvironment);
/* 797 */       code(paramEnvironment, paramAssembler);
/* 798 */       return;
/*     */     case 2:
/* 801 */       inline(paramEnvironment);
/* 802 */       code(paramEnvironment, paramAssembler);
/* 803 */       return;
/*     */     case 4:
/* 807 */       if (paramEnvironment.dump()) {
/* 808 */         System.out.println("[code field " + getClassDeclaration().getName() + "." + getName() + "]");
/*     */       }
/* 810 */       if ((isMethod()) && (!isNative()) && (!isAbstract())) {
/* 811 */         paramEnvironment = new Environment(paramEnvironment, this);
/* 812 */         Context localContext = new Context((Context)null, this);
/* 813 */         Statement localStatement = (Statement)getValue();
/*     */ 
/* 815 */         for (Enumeration localEnumeration = this.args.elements(); localEnumeration.hasMoreElements(); ) {
/* 816 */           LocalMember localLocalMember = (LocalMember)localEnumeration.nextElement();
/* 817 */           localContext.declare(paramEnvironment, localLocalMember);
/*     */         }
/*     */ 
/* 839 */         if (localStatement != null) {
/* 840 */           localStatement.code(paramEnvironment, localContext, paramAssembler);
/*     */         }
/* 842 */         if ((getType().getReturnType().isType(11)) && (!isInitializer())) {
/* 843 */           paramAssembler.add(getWhere(), 177, true);
/*     */         }
/*     */       }
/* 846 */       return;
/*     */     case 1:
/*     */     case 3:
/*     */     }
/*     */   }
/* 851 */   public void codeInit(Environment paramEnvironment, Context paramContext, Assembler paramAssembler) throws ClassNotFound { if (isMethod()) {
/* 852 */       return;
/*     */     }
/* 854 */     switch (this.status) {
/*     */     case 0:
/* 856 */       check(paramEnvironment);
/* 857 */       codeInit(paramEnvironment, paramContext, paramAssembler);
/* 858 */       return;
/*     */     case 2:
/* 861 */       inline(paramEnvironment);
/* 862 */       codeInit(paramEnvironment, paramContext, paramAssembler);
/* 863 */       return;
/*     */     case 4:
/* 867 */       if (paramEnvironment.dump()) {
/* 868 */         System.out.println("[code initializer  " + getClassDeclaration().getName() + "." + getName() + "]");
/*     */       }
/* 870 */       if (getValue() != null) {
/* 871 */         Expression localExpression = (Expression)getValue();
/*     */ 
/* 877 */         if (isStatic()) {
/* 878 */           if (getInitialValue() == null)
/*     */           {
/* 880 */             localExpression.codeValue(paramEnvironment, paramContext, paramAssembler);
/* 881 */             paramAssembler.add(getWhere(), 179, this);
/*     */           }
/*     */ 
/*     */         }
/*     */         else
/*     */         {
/* 888 */           paramAssembler.add(getWhere(), 25, new Integer(0));
/* 889 */           localExpression.codeValue(paramEnvironment, paramContext, paramAssembler);
/* 890 */           paramAssembler.add(getWhere(), 181, this);
/*     */         }
/*     */       }
/* 893 */       return;
/*     */     case 1:
/*     */     case 3:
/*     */     }
/*     */   }
/*     */ 
/*     */   public void print(PrintStream paramPrintStream)
/*     */   {
/* 901 */     super.print(paramPrintStream);
/* 902 */     if (getValue() != null) {
/* 903 */       getValue().print(paramPrintStream);
/* 904 */       paramPrintStream.println();
/*     */     }
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     sun.tools.javac.SourceMember
 * JD-Core Version:    0.6.2
 */