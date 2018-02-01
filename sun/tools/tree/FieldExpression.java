/*      */ package sun.tools.tree;
/*      */ 
/*      */ import java.io.PrintStream;
/*      */ import java.util.Hashtable;
/*      */ import sun.tools.asm.Assembler;
/*      */ import sun.tools.java.AmbiguousClass;
/*      */ import sun.tools.java.AmbiguousMember;
/*      */ import sun.tools.java.ClassDeclaration;
/*      */ import sun.tools.java.ClassDefinition;
/*      */ import sun.tools.java.ClassNotFound;
/*      */ import sun.tools.java.CompilerError;
/*      */ import sun.tools.java.Environment;
/*      */ import sun.tools.java.Identifier;
/*      */ import sun.tools.java.MemberDefinition;
/*      */ import sun.tools.java.Type;
/*      */ 
/*      */ public class FieldExpression extends UnaryExpression
/*      */ {
/*      */   Identifier id;
/*      */   MemberDefinition field;
/*      */   Expression implementation;
/*      */   ClassDefinition clazz;
/*      */   private ClassDefinition superBase;
/*      */ 
/*      */   public FieldExpression(long paramLong, Expression paramExpression, Identifier paramIdentifier)
/*      */   {
/*   55 */     super(46, paramLong, Type.tError, paramExpression);
/*   56 */     this.id = paramIdentifier;
/*      */   }
/*      */   public FieldExpression(long paramLong, Expression paramExpression, MemberDefinition paramMemberDefinition) {
/*   59 */     super(46, paramLong, paramMemberDefinition.getType(), paramExpression);
/*   60 */     this.id = paramMemberDefinition.getName();
/*   61 */     this.field = paramMemberDefinition;
/*      */   }
/*      */ 
/*      */   public Expression getImplementation() {
/*   65 */     if (this.implementation != null)
/*   66 */       return this.implementation;
/*   67 */     return this;
/*      */   }
/*      */ 
/*      */   private boolean isQualSuper()
/*      */   {
/*   75 */     return this.superBase != null;
/*      */   }
/*      */ 
/*      */   public static Identifier toIdentifier(Expression paramExpression)
/*      */   {
/*   82 */     StringBuffer localStringBuffer = new StringBuffer();
/*   83 */     while (paramExpression.op == 46) {
/*   84 */       FieldExpression localFieldExpression = (FieldExpression)paramExpression;
/*   85 */       if ((localFieldExpression.id == idThis) || (localFieldExpression.id == idClass)) {
/*   86 */         return null;
/*      */       }
/*   88 */       localStringBuffer.insert(0, localFieldExpression.id);
/*   89 */       localStringBuffer.insert(0, '.');
/*   90 */       paramExpression = localFieldExpression.right;
/*      */     }
/*   92 */     if (paramExpression.op != 60) {
/*   93 */       return null;
/*      */     }
/*   95 */     localStringBuffer.insert(0, ((IdentifierExpression)paramExpression).id);
/*   96 */     return Identifier.lookup(localStringBuffer.toString());
/*      */   }
/*      */ 
/*      */   Type toType(Environment paramEnvironment, Context paramContext)
/*      */   {
/*  205 */     Identifier localIdentifier = toIdentifier(this);
/*  206 */     if (localIdentifier == null) {
/*  207 */       paramEnvironment.error(this.where, "invalid.type.expr");
/*  208 */       return Type.tError;
/*      */     }
/*  210 */     Type localType = Type.tClass(paramContext.resolveName(paramEnvironment, localIdentifier));
/*  211 */     if (paramEnvironment.resolve(this.where, paramContext.field.getClassDefinition(), localType)) {
/*  212 */       return localType;
/*      */     }
/*  214 */     return Type.tError;
/*      */   }
/*      */ 
/*      */   public Vset checkAmbigName(Environment paramEnvironment, Context paramContext, Vset paramVset, Hashtable paramHashtable, UnaryExpression paramUnaryExpression)
/*      */   {
/*  224 */     if ((this.id == idThis) || (this.id == idClass)) {
/*  225 */       paramUnaryExpression = null;
/*      */     }
/*  227 */     return checkCommon(paramEnvironment, paramContext, paramVset, paramHashtable, paramUnaryExpression, false);
/*      */   }
/*      */ 
/*      */   public Vset checkValue(Environment paramEnvironment, Context paramContext, Vset paramVset, Hashtable paramHashtable)
/*      */   {
/*  236 */     paramVset = checkCommon(paramEnvironment, paramContext, paramVset, paramHashtable, null, false);
/*  237 */     if ((this.id == idSuper) && (this.type != Type.tError))
/*      */     {
/*  240 */       paramEnvironment.error(this.where, "undef.var.super", idSuper);
/*      */     }
/*  242 */     return paramVset;
/*      */   }
/*      */ 
/*      */   static void reportFailedPackagePrefix(Environment paramEnvironment, Expression paramExpression)
/*      */   {
/*  252 */     reportFailedPackagePrefix(paramEnvironment, paramExpression, false);
/*      */   }
/*      */ 
/*      */   static void reportFailedPackagePrefix(Environment paramEnvironment, Expression paramExpression, boolean paramBoolean)
/*      */   {
/*  259 */     Expression localExpression = paramExpression;
/*  260 */     while ((localExpression instanceof UnaryExpression))
/*  261 */       localExpression = ((UnaryExpression)localExpression).right;
/*  262 */     IdentifierExpression localIdentifierExpression = (IdentifierExpression)localExpression;
/*      */     try
/*      */     {
/*  267 */       paramEnvironment.resolve(localIdentifierExpression.id);
/*      */     } catch (AmbiguousClass localAmbiguousClass) {
/*  269 */       paramEnvironment.error(paramExpression.where, "ambig.class", localAmbiguousClass.name1, localAmbiguousClass.name2);
/*  270 */       return;
/*      */     }
/*      */     catch (ClassNotFound localClassNotFound) {
/*      */     }
/*  274 */     if (localExpression == paramExpression) {
/*  275 */       if (paramBoolean)
/*  276 */         paramEnvironment.error(localIdentifierExpression.where, "undef.class", localIdentifierExpression.id);
/*      */       else {
/*  278 */         paramEnvironment.error(localIdentifierExpression.where, "undef.var.or.class", localIdentifierExpression.id);
/*      */       }
/*      */     }
/*  281 */     else if (paramBoolean)
/*  282 */       paramEnvironment.error(localIdentifierExpression.where, "undef.class.or.package", localIdentifierExpression.id);
/*      */     else
/*  284 */       paramEnvironment.error(localIdentifierExpression.where, "undef.var.class.or.package", localIdentifierExpression.id);
/*      */   }
/*      */ 
/*      */   private Expression implementFieldAccess(Environment paramEnvironment, Context paramContext, Expression paramExpression, boolean paramBoolean)
/*      */   {
/*  295 */     ClassDefinition localClassDefinition = accessBase(paramEnvironment, paramContext);
/*  296 */     if (localClassDefinition != null)
/*      */     {
/*  306 */       if (this.field.isFinal()) {
/*  307 */         localObject = (Expression)this.field.getValue();
/*      */ 
/*  311 */         if ((localObject != null) && (((Expression)localObject).isConstant()) && (!paramBoolean)) {
/*  312 */           return ((Expression)localObject).copyInline(paramContext);
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/*  317 */       Object localObject = localClassDefinition.getAccessMember(paramEnvironment, paramContext, this.field, isQualSuper());
/*      */ 
/*  320 */       if (!paramBoolean)
/*      */       {
/*  330 */         if (this.field.isStatic()) {
/*  331 */           arrayOfExpression = new Expression[0];
/*  332 */           MethodExpression localMethodExpression = new MethodExpression(this.where, null, (MemberDefinition)localObject, arrayOfExpression);
/*      */ 
/*  334 */           return new CommaExpression(this.where, paramExpression, localMethodExpression);
/*      */         }
/*  336 */         Expression[] arrayOfExpression = { paramExpression };
/*  337 */         return new MethodExpression(this.where, null, (MemberDefinition)localObject, arrayOfExpression);
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/*  342 */     return null;
/*      */   }
/*      */ 
/*      */   private ClassDefinition accessBase(Environment paramEnvironment, Context paramContext)
/*      */   {
/*      */     ClassDefinition localClassDefinition1;
/*      */     ClassDefinition localClassDefinition2;
/*  350 */     if (this.field.isPrivate()) {
/*  351 */       localClassDefinition1 = this.field.getClassDefinition();
/*  352 */       localClassDefinition2 = paramContext.field.getClassDefinition();
/*  353 */       if (localClassDefinition1 == localClassDefinition2)
/*      */       {
/*  356 */         return null;
/*      */       }
/*      */ 
/*  359 */       return localClassDefinition1;
/*  360 */     }if (this.field.isProtected()) {
/*  361 */       if (this.superBase == null)
/*      */       {
/*  366 */         return null;
/*      */       }
/*  368 */       localClassDefinition1 = this.field.getClassDefinition();
/*  369 */       localClassDefinition2 = paramContext.field.getClassDefinition();
/*  370 */       if (localClassDefinition1.inSamePackage(localClassDefinition2))
/*      */       {
/*  372 */         return null;
/*      */       }
/*      */ 
/*  381 */       return this.superBase;
/*      */     }
/*      */ 
/*  384 */     return null;
/*      */   }
/*      */ 
/*      */   static boolean isTypeAccessible(long paramLong, Environment paramEnvironment, Type paramType, ClassDefinition paramClassDefinition)
/*      */   {
/*  395 */     switch (paramType.getTypeCode()) {
/*      */     case 10:
/*      */       try {
/*  398 */         Identifier localIdentifier = paramType.getClassName();
/*      */ 
/*  403 */         ClassDefinition localClassDefinition = paramEnvironment.getClassDefinition(paramType);
/*  404 */         return paramClassDefinition.canAccess(paramEnvironment, localClassDefinition.getClassDeclaration());
/*      */       } catch (ClassNotFound localClassNotFound) {
/*  406 */         return true;
/*      */       }case 9:
/*  408 */       return isTypeAccessible(paramLong, paramEnvironment, paramType.getElementType(), paramClassDefinition);
/*      */     }
/*  410 */     return true;
/*      */   }
/*      */ 
/*      */   private Vset checkCommon(Environment paramEnvironment, Context paramContext, Vset paramVset, Hashtable paramHashtable, UnaryExpression paramUnaryExpression, boolean paramBoolean)
/*      */   {
/*      */     Object localObject2;
/*      */     Object localObject3;
/*  423 */     if (this.id == idClass)
/*      */     {
/*  426 */       localObject1 = this.right.toType(paramEnvironment, paramContext);
/*      */ 
/*  428 */       if ((!((Type)localObject1).isType(10)) && (!((Type)localObject1).isType(9))) {
/*  429 */         if (((Type)localObject1).isType(13)) {
/*  430 */           this.type = Type.tClassDesc;
/*  431 */           return paramVset;
/*      */         }
/*  433 */         String str1 = null;
/*  434 */         switch (((Type)localObject1).getTypeCode()) { case 11:
/*  435 */           str1 = "Void"; break;
/*      */         case 0:
/*  436 */           str1 = "Boolean"; break;
/*      */         case 1:
/*  437 */           str1 = "Byte"; break;
/*      */         case 2:
/*  438 */           str1 = "Character"; break;
/*      */         case 3:
/*  439 */           str1 = "Short"; break;
/*      */         case 4:
/*  440 */           str1 = "Integer"; break;
/*      */         case 6:
/*  441 */           str1 = "Float"; break;
/*      */         case 5:
/*  442 */           str1 = "Long"; break;
/*      */         case 7:
/*  443 */           str1 = "Double"; break;
/*      */         case 8:
/*      */         case 9:
/*      */         case 10:
/*      */         default:
/*  445 */           paramEnvironment.error(this.right.where, "invalid.type.expr");
/*  446 */           return paramVset;
/*      */         }
/*  448 */         localObject2 = Identifier.lookup(idJavaLang + "." + str1);
/*  449 */         localObject3 = new TypeExpression(this.where, Type.tClass((Identifier)localObject2));
/*  450 */         this.implementation = new FieldExpression(this.where, (Expression)localObject3, idTYPE);
/*  451 */         paramVset = this.implementation.checkValue(paramEnvironment, paramContext, paramVset, paramHashtable);
/*  452 */         this.type = this.implementation.type;
/*  453 */         return paramVset;
/*      */       }
/*      */ 
/*  457 */       if (((Type)localObject1).isVoidArray()) {
/*  458 */         this.type = Type.tClassDesc;
/*  459 */         paramEnvironment.error(this.right.where, "void.array");
/*  460 */         return paramVset;
/*      */       }
/*      */ 
/*  464 */       long l = paramContext.field.getWhere();
/*  465 */       localObject3 = paramContext.field.getClassDefinition();
/*  466 */       MemberDefinition localMemberDefinition1 = ((ClassDefinition)localObject3).getClassLiteralLookup(l);
/*      */ 
/*  468 */       String str2 = ((Type)localObject1).getTypeSignature();
/*      */       String str3;
/*  470 */       if (((Type)localObject1).isType(10))
/*      */       {
/*  474 */         str3 = str2.substring(1, str2.length() - 1)
/*  474 */           .replace('/', '.');
/*      */       }
/*      */       else
/*      */       {
/*  478 */         str3 = str2.replace('/', '.');
/*      */       }
/*      */ 
/*  481 */       if (((ClassDefinition)localObject3).isInterface())
/*      */       {
/*  487 */         this.implementation = 
/*  488 */           makeClassLiteralInlineRef(paramEnvironment, paramContext, localMemberDefinition1, str3);
/*      */       }
/*      */       else
/*      */       {
/*  492 */         ClassDefinition localClassDefinition2 = localMemberDefinition1.getClassDefinition();
/*      */ 
/*  494 */         MemberDefinition localMemberDefinition2 = getClassLiteralCache(paramEnvironment, paramContext, str3, localClassDefinition2);
/*      */ 
/*  495 */         this.implementation = 
/*  496 */           makeClassLiteralCacheRef(paramEnvironment, paramContext, localMemberDefinition1, localMemberDefinition2, str3);
/*      */       }
/*      */ 
/*  499 */       paramVset = this.implementation.checkValue(paramEnvironment, paramContext, paramVset, paramHashtable);
/*  500 */       this.type = this.implementation.type;
/*  501 */       return paramVset;
/*      */     }
/*      */ 
/*  506 */     if (this.field != null)
/*      */     {
/*  515 */       this.implementation = implementFieldAccess(paramEnvironment, paramContext, this.right, paramBoolean);
/*      */ 
/*  517 */       return this.right == null ? paramVset : this.right
/*  517 */         .checkAmbigName(paramEnvironment, paramContext, paramVset, paramHashtable, this);
/*      */     }
/*      */ 
/*  521 */     paramVset = this.right.checkAmbigName(paramEnvironment, paramContext, paramVset, paramHashtable, this);
/*  522 */     if (this.right.type == Type.tPackage)
/*      */     {
/*  524 */       if (paramUnaryExpression == null) {
/*  525 */         reportFailedPackagePrefix(paramEnvironment, this.right);
/*  526 */         return paramVset;
/*      */       }
/*      */ 
/*  532 */       localObject1 = toIdentifier(this);
/*  533 */       if ((localObject1 != null) && (paramEnvironment.classExists((Identifier)localObject1))) {
/*  534 */         paramUnaryExpression.right = new TypeExpression(this.where, Type.tClass((Identifier)localObject1));
/*      */ 
/*  536 */         ClassDefinition localClassDefinition1 = paramContext.field.getClassDefinition();
/*  537 */         paramEnvironment.resolve(this.where, localClassDefinition1, paramUnaryExpression.right.type);
/*  538 */         return paramVset;
/*      */       }
/*      */ 
/*  542 */       this.type = Type.tPackage;
/*  543 */       return paramVset;
/*      */     }
/*      */ 
/*  548 */     Object localObject1 = paramContext.field.getClassDefinition();
/*  549 */     boolean bool = this.right instanceof TypeExpression;
/*      */     try
/*      */     {
/*  555 */       if (!this.right.type.isType(10)) {
/*  556 */         if ((this.right.type.isType(9)) && (this.id.equals(idLength)))
/*      */         {
/*  559 */           if (!isTypeAccessible(this.where, paramEnvironment, this.right.type, (ClassDefinition)localObject1)) {
/*  560 */             localObject2 = ((ClassDefinition)localObject1).getClassDeclaration();
/*  561 */             if (bool)
/*  562 */               paramEnvironment.error(this.where, "no.type.access", this.id, this.right.type
/*  563 */                 .toString(), localObject2);
/*      */             else {
/*  565 */               paramEnvironment.error(this.where, "cant.access.member.type", this.id, this.right.type
/*  566 */                 .toString(), localObject2);
/*      */             }
/*      */           }
/*  569 */           this.type = Type.tInt;
/*  570 */           this.implementation = new LengthExpression(this.where, this.right);
/*  571 */           return paramVset;
/*      */         }
/*  573 */         if (!this.right.type.isType(13)) {
/*  574 */           paramEnvironment.error(this.where, "invalid.field.reference", this.id, this.right.type);
/*      */         }
/*  576 */         return paramVset;
/*      */       }
/*      */ 
/*  594 */       localObject2 = localObject1;
/*  595 */       if ((this.right instanceof FieldExpression)) {
/*  596 */         localObject3 = ((FieldExpression)this.right).id;
/*  597 */         if (localObject3 == idThis) {
/*  598 */           localObject2 = ((FieldExpression)this.right).clazz;
/*  599 */         } else if (localObject3 == idSuper) {
/*  600 */           localObject2 = ((FieldExpression)this.right).clazz;
/*  601 */           this.superBase = ((ClassDefinition)localObject2);
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/*  618 */       this.clazz = paramEnvironment.getClassDefinition(this.right.type);
/*  619 */       if ((this.id == idThis) || (this.id == idSuper)) {
/*  620 */         if (!bool) {
/*  621 */           paramEnvironment.error(this.right.where, "invalid.type.expr");
/*      */         }
/*      */ 
/*  630 */         if (paramContext.field.isSynthetic()) {
/*  631 */           throw new CompilerError("synthetic qualified this");
/*      */         }
/*      */ 
/*  640 */         this.implementation = paramContext.findOuterLink(paramEnvironment, this.where, this.clazz, null, true);
/*  641 */         paramVset = this.implementation.checkValue(paramEnvironment, paramContext, paramVset, paramHashtable);
/*  642 */         if (this.id == idSuper)
/*  643 */           this.type = this.clazz.getSuperClass().getType();
/*      */         else {
/*  645 */           this.type = this.clazz.getType();
/*      */         }
/*  647 */         return paramVset;
/*      */       }
/*      */ 
/*  651 */       this.field = this.clazz.getVariable(paramEnvironment, this.id, (ClassDefinition)localObject2);
/*      */ 
/*  653 */       if ((this.field == null) && (bool) && (paramUnaryExpression != null))
/*      */       {
/*  663 */         this.field = this.clazz.getInnerClass(paramEnvironment, this.id);
/*  664 */         if (this.field != null) {
/*  665 */           return checkInnerClass(paramEnvironment, paramContext, paramVset, paramHashtable, paramUnaryExpression);
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/*  672 */       if (this.field == null) {
/*  673 */         if ((this.field = this.clazz.findAnyMethod(paramEnvironment, this.id)) != null)
/*  674 */           paramEnvironment.error(this.where, "invalid.field", this.id, this.field
/*  675 */             .getClassDeclaration());
/*      */         else {
/*  677 */           paramEnvironment.error(this.where, "no.such.field", this.id, this.clazz);
/*      */         }
/*  679 */         return paramVset;
/*      */       }
/*      */ 
/*  685 */       if (!isTypeAccessible(this.where, paramEnvironment, this.right.type, (ClassDefinition)localObject2)) {
/*  686 */         localObject3 = ((ClassDefinition)localObject2).getClassDeclaration();
/*  687 */         if (bool)
/*  688 */           paramEnvironment.error(this.where, "no.type.access", this.id, this.right.type
/*  689 */             .toString(), localObject3);
/*      */         else {
/*  691 */           paramEnvironment.error(this.where, "cant.access.member.type", this.id, this.right.type
/*  692 */             .toString(), localObject3);
/*      */         }
/*      */       }
/*      */ 
/*  696 */       this.type = this.field.getType();
/*      */ 
/*  698 */       if (!((ClassDefinition)localObject2).canAccess(paramEnvironment, this.field)) {
/*  699 */         paramEnvironment.error(this.where, "no.field.access", this.id, this.clazz, ((ClassDefinition)localObject2)
/*  700 */           .getClassDeclaration());
/*  701 */         return paramVset;
/*      */       }
/*      */ 
/*  704 */       if ((bool) && (!this.field.isStatic()))
/*      */       {
/*  709 */         paramEnvironment.error(this.where, "no.static.field.access", this.id, this.clazz);
/*  710 */         return paramVset;
/*      */       }
/*      */ 
/*  713 */       this.implementation = implementFieldAccess(paramEnvironment, paramContext, this.right, paramBoolean);
/*      */ 
/*  717 */       if ((this.field.isProtected()) && (!(this.right instanceof SuperExpression)) && ((!(this.right instanceof FieldExpression)) || (((FieldExpression)this.right).id != idSuper)))
/*      */       {
/*  722 */         if (!((ClassDefinition)localObject2)
/*  722 */           .protectedAccess(paramEnvironment, this.field, this.right.type))
/*      */         {
/*  723 */           paramEnvironment.error(this.where, "invalid.protected.field.use", this.field
/*  724 */             .getName(), this.field.getClassDeclaration(), this.right.type);
/*      */ 
/*  726 */           return paramVset;
/*      */         }
/*      */       }
/*  729 */       if ((!this.field.isStatic()) && (this.right.op == 82) && 
/*  730 */         (!paramVset
/*  730 */         .testVar(paramContext
/*  730 */         .getThisNumber()))) {
/*  731 */         paramEnvironment.error(this.where, "access.inst.before.super", this.id);
/*      */       }
/*      */ 
/*  734 */       if (this.field.reportDeprecated(paramEnvironment)) {
/*  735 */         paramEnvironment.error(this.where, "warn.field.is.deprecated", this.id, this.field
/*  736 */           .getClassDefinition());
/*      */       }
/*      */ 
/*  749 */       if (localObject2 == localObject1) {
/*  750 */         localObject3 = this.field.getClassDefinition();
/*  751 */         if (((ClassDefinition)localObject3).isPackagePrivate())
/*      */         {
/*  753 */           if (!((ClassDefinition)localObject3)
/*  752 */             .getName().getQualifier()
/*  753 */             .equals(((ClassDefinition)localObject2)
/*  753 */             .getName().getQualifier()))
/*      */           {
/*  766 */             this.field = 
/*  767 */               MemberDefinition.makeProxyMember(this.field, this.clazz, paramEnvironment);
/*      */           }
/*      */         }
/*      */       }
/*      */ 
/*  771 */       ((ClassDefinition)localObject2).addDependency(this.field.getClassDeclaration());
/*      */     }
/*      */     catch (ClassNotFound localClassNotFound) {
/*  774 */       paramEnvironment.error(this.where, "class.not.found", localClassNotFound.name, paramContext.field);
/*      */     }
/*      */     catch (AmbiguousMember localAmbiguousMember) {
/*  777 */       paramEnvironment.error(this.where, "ambig.field", this.id, localAmbiguousMember.field1
/*  778 */         .getClassDeclaration(), localAmbiguousMember.field2.getClassDeclaration());
/*      */     }
/*  780 */     return paramVset;
/*      */   }
/*      */ 
/*      */   public FieldUpdater getAssigner(Environment paramEnvironment, Context paramContext)
/*      */   {
/*  799 */     if (this.field == null)
/*      */     {
/*  804 */       return null;
/*      */     }
/*  806 */     ClassDefinition localClassDefinition = accessBase(paramEnvironment, paramContext);
/*  807 */     if (localClassDefinition != null) {
/*  808 */       MemberDefinition localMemberDefinition = localClassDefinition.getUpdateMember(paramEnvironment, paramContext, this.field, isQualSuper());
/*      */ 
/*  810 */       Expression localExpression = this.right == null ? null : this.right.copyInline(paramContext);
/*      */ 
/*  812 */       return new FieldUpdater(this.where, this.field, localExpression, null, localMemberDefinition);
/*      */     }
/*  814 */     return null;
/*      */   }
/*      */ 
/*      */   public FieldUpdater getUpdater(Environment paramEnvironment, Context paramContext)
/*      */   {
/*  830 */     if (this.field == null)
/*      */     {
/*  835 */       return null;
/*      */     }
/*  837 */     ClassDefinition localClassDefinition = accessBase(paramEnvironment, paramContext);
/*  838 */     if (localClassDefinition != null) {
/*  839 */       MemberDefinition localMemberDefinition1 = localClassDefinition.getAccessMember(paramEnvironment, paramContext, this.field, isQualSuper());
/*  840 */       MemberDefinition localMemberDefinition2 = localClassDefinition.getUpdateMember(paramEnvironment, paramContext, this.field, isQualSuper());
/*      */ 
/*  842 */       Expression localExpression = this.right == null ? null : this.right.copyInline(paramContext);
/*  843 */       return new FieldUpdater(this.where, this.field, localExpression, localMemberDefinition1, localMemberDefinition2);
/*      */     }
/*  845 */     return null;
/*      */   }
/*      */ 
/*      */   private Vset checkInnerClass(Environment paramEnvironment, Context paramContext, Vset paramVset, Hashtable paramHashtable, UnaryExpression paramUnaryExpression)
/*      */   {
/*  855 */     ClassDefinition localClassDefinition1 = this.field.getInnerClass();
/*  856 */     this.type = localClassDefinition1.getType();
/*      */ 
/*  858 */     if (!localClassDefinition1.isTopLevel()) {
/*  859 */       paramEnvironment.error(this.where, "inner.static.ref", localClassDefinition1.getName());
/*      */     }
/*      */ 
/*  862 */     TypeExpression localTypeExpression = new TypeExpression(this.where, this.type);
/*      */ 
/*  865 */     ClassDefinition localClassDefinition2 = paramContext.field.getClassDefinition();
/*      */     try {
/*  867 */       if (!localClassDefinition2.canAccess(paramEnvironment, this.field)) {
/*  868 */         ClassDefinition localClassDefinition3 = paramEnvironment.getClassDefinition(this.right.type);
/*      */ 
/*  871 */         paramEnvironment.error(this.where, "no.type.access", this.id, localClassDefinition3, localClassDefinition2
/*  872 */           .getClassDeclaration());
/*  873 */         return paramVset;
/*      */       }
/*      */ 
/*  876 */       if ((this.field.isProtected()) && (!(this.right instanceof SuperExpression)) && ((!(this.right instanceof FieldExpression)) || (((FieldExpression)this.right).id != idSuper)))
/*      */       {
/*  881 */         if (!localClassDefinition2
/*  881 */           .protectedAccess(paramEnvironment, this.field, this.right.type))
/*      */         {
/*  882 */           paramEnvironment.error(this.where, "invalid.protected.field.use", this.field
/*  883 */             .getName(), this.field.getClassDeclaration(), this.right.type);
/*      */ 
/*  885 */           return paramVset;
/*      */         }
/*      */       }
/*  888 */       localClassDefinition1.noteUsedBy(localClassDefinition2, this.where, paramEnvironment);
/*      */     }
/*      */     catch (ClassNotFound localClassNotFound) {
/*  891 */       paramEnvironment.error(this.where, "class.not.found", localClassNotFound.name, paramContext.field);
/*      */     }
/*      */ 
/*  894 */     localClassDefinition2.addDependency(this.field.getClassDeclaration());
/*  895 */     if (paramUnaryExpression == null)
/*      */     {
/*  897 */       return localTypeExpression.checkValue(paramEnvironment, paramContext, paramVset, paramHashtable);
/*  898 */     }paramUnaryExpression.right = localTypeExpression;
/*  899 */     return paramVset;
/*      */   }
/*      */ 
/*      */   public Vset checkLHS(Environment paramEnvironment, Context paramContext, Vset paramVset, Hashtable paramHashtable)
/*      */   {
/*  907 */     int i = this.field != null ? 1 : 0;
/*      */ 
/*  910 */     checkCommon(paramEnvironment, paramContext, paramVset, paramHashtable, null, true);
/*      */ 
/*  915 */     if (this.implementation != null)
/*      */     {
/*  917 */       return super.checkLHS(paramEnvironment, paramContext, paramVset, paramHashtable);
/*      */     }
/*      */ 
/*  920 */     if ((this.field != null) && (this.field.isFinal()) && (i == 0)) {
/*  921 */       if (this.field.isBlankFinal()) {
/*  922 */         if (this.field.isStatic()) {
/*  923 */           if (this.right != null) {
/*  924 */             paramEnvironment.error(this.where, "qualified.static.final.assign");
/*      */           }
/*      */ 
/*      */         }
/*  929 */         else if ((this.right != null) && (this.right.op != 82)) {
/*  930 */           paramEnvironment.error(this.where, "bad.qualified.final.assign", this.field.getName());
/*      */ 
/*  933 */           return paramVset;
/*      */         }
/*      */ 
/*  936 */         paramVset = checkFinalAssign(paramEnvironment, paramContext, paramVset, this.where, this.field);
/*      */       } else {
/*  938 */         paramEnvironment.error(this.where, "assign.to.final", this.id);
/*      */       }
/*      */     }
/*  941 */     return paramVset;
/*      */   }
/*      */ 
/*      */   public Vset checkAssignOp(Environment paramEnvironment, Context paramContext, Vset paramVset, Hashtable paramHashtable, Expression paramExpression)
/*      */   {
/*  951 */     checkCommon(paramEnvironment, paramContext, paramVset, paramHashtable, null, true);
/*      */ 
/*  956 */     if (this.implementation != null) {
/*  957 */       return super.checkLHS(paramEnvironment, paramContext, paramVset, paramHashtable);
/*      */     }
/*  959 */     if ((this.field != null) && (this.field.isFinal())) {
/*  960 */       paramEnvironment.error(this.where, "assign.to.final", this.id);
/*      */     }
/*  962 */     return paramVset;
/*      */   }
/*      */ 
/*      */   public static Vset checkFinalAssign(Environment paramEnvironment, Context paramContext, Vset paramVset, long paramLong, MemberDefinition paramMemberDefinition)
/*      */   {
/*  982 */     if ((paramMemberDefinition.isBlankFinal()) && 
/*  983 */       (paramMemberDefinition
/*  983 */       .getClassDefinition() == paramContext.field.getClassDefinition())) {
/*  984 */       int i = paramContext.getFieldNumber(paramMemberDefinition);
/*  985 */       if ((i >= 0) && (paramVset.testVarUnassigned(i)))
/*      */       {
/*  987 */         paramVset = paramVset.addVar(i);
/*      */       }
/*      */       else {
/*  990 */         Identifier localIdentifier2 = paramMemberDefinition.getName();
/*  991 */         paramEnvironment.error(paramLong, "assign.to.blank.final", localIdentifier2);
/*      */       }
/*      */     }
/*      */     else {
/*  995 */       Identifier localIdentifier1 = paramMemberDefinition.getName();
/*  996 */       paramEnvironment.error(paramLong, "assign.to.final", localIdentifier1);
/*      */     }
/*  998 */     return paramVset;
/*      */   }
/*      */ 
/*      */   private static MemberDefinition getClassLiteralCache(Environment paramEnvironment, Context paramContext, String paramString, ClassDefinition paramClassDefinition)
/*      */   {
/*      */     String str;
/* 1013 */     if (!paramString.startsWith("[")) {
/* 1014 */       str = "class$" + paramString.replace('.', '$');
/*      */     } else {
/* 1016 */       str = "array$" + paramString.substring(1);
/* 1017 */       str = str.replace('[', '$');
/* 1018 */       if (paramString.endsWith(";"))
/*      */       {
/* 1020 */         str = str.substring(0, str.length() - 1);
/* 1021 */         str = str.replace('.', '$');
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/* 1025 */     Identifier localIdentifier = Identifier.lookup(str);
/*      */     MemberDefinition localMemberDefinition;
/*      */     try
/*      */     {
/* 1035 */       localMemberDefinition = paramClassDefinition.getVariable(paramEnvironment, localIdentifier, paramClassDefinition);
/*      */     } catch (ClassNotFound localClassNotFound) {
/* 1037 */       return null;
/*      */     } catch (AmbiguousMember localAmbiguousMember) {
/* 1039 */       return null;
/*      */     }
/*      */ 
/* 1047 */     if ((localMemberDefinition != null) && (localMemberDefinition.getClassDefinition() == paramClassDefinition)) {
/* 1048 */       return localMemberDefinition;
/*      */     }
/*      */ 
/* 1055 */     return paramEnvironment.makeMemberDefinition(paramEnvironment, paramClassDefinition.getWhere(), paramClassDefinition, null, 524296, Type.tClassDesc, localIdentifier, null, null, null);
/*      */   }
/*      */ 
/*      */   private Expression makeClassLiteralCacheRef(Environment paramEnvironment, Context paramContext, MemberDefinition paramMemberDefinition1, MemberDefinition paramMemberDefinition2, String paramString)
/*      */   {
/* 1068 */     TypeExpression localTypeExpression1 = new TypeExpression(this.where, paramMemberDefinition2
/* 1067 */       .getClassDefinition()
/* 1068 */       .getType());
/* 1069 */     FieldExpression localFieldExpression = new FieldExpression(this.where, localTypeExpression1, paramMemberDefinition2);
/*      */ 
/* 1071 */     NotEqualExpression localNotEqualExpression = new NotEqualExpression(this.where, localFieldExpression
/* 1071 */       .copyInline(paramContext), 
/* 1071 */       new NullExpression(this.where));
/*      */ 
/* 1074 */     TypeExpression localTypeExpression2 = new TypeExpression(this.where, paramMemberDefinition1
/* 1074 */       .getClassDefinition().getType());
/* 1075 */     StringExpression localStringExpression = new StringExpression(this.where, paramString);
/* 1076 */     Expression[] arrayOfExpression = { localStringExpression };
/* 1077 */     Object localObject = new MethodExpression(this.where, localTypeExpression2, paramMemberDefinition1, arrayOfExpression);
/*      */ 
/* 1079 */     localObject = new AssignExpression(this.where, localFieldExpression.copyInline(paramContext), (Expression)localObject);
/*      */ 
/* 1081 */     return new ConditionalExpression(this.where, localNotEqualExpression, localFieldExpression, (Expression)localObject);
/*      */   }
/*      */ 
/*      */   private Expression makeClassLiteralInlineRef(Environment paramEnvironment, Context paramContext, MemberDefinition paramMemberDefinition, String paramString)
/*      */   {
/* 1088 */     TypeExpression localTypeExpression = new TypeExpression(this.where, paramMemberDefinition
/* 1088 */       .getClassDefinition().getType());
/* 1089 */     StringExpression localStringExpression = new StringExpression(this.where, paramString);
/* 1090 */     Expression[] arrayOfExpression = { localStringExpression };
/* 1091 */     MethodExpression localMethodExpression = new MethodExpression(this.where, localTypeExpression, paramMemberDefinition, arrayOfExpression);
/*      */ 
/* 1093 */     return localMethodExpression;
/*      */   }
/*      */ 
/*      */   public boolean isConstant()
/*      */   {
/* 1101 */     if (this.implementation != null)
/* 1102 */       return this.implementation.isConstant();
/* 1103 */     if ((this.field != null) && ((this.right == null) || ((this.right instanceof TypeExpression)) || ((this.right.op == 82) && (this.right.where == this.where))))
/*      */     {
/* 1106 */       return this.field.isConstant();
/*      */     }
/* 1108 */     return false;
/*      */   }
/*      */ 
/*      */   public Expression inline(Environment paramEnvironment, Context paramContext)
/*      */   {
/* 1115 */     if (this.implementation != null) {
/* 1116 */       return this.implementation.inline(paramEnvironment, paramContext);
/*      */     }
/*      */ 
/* 1126 */     Expression localExpression = inlineValue(paramEnvironment, paramContext);
/* 1127 */     if ((localExpression instanceof FieldExpression)) {
/* 1128 */       FieldExpression localFieldExpression = (FieldExpression)localExpression;
/* 1129 */       if ((localFieldExpression.right != null) && (localFieldExpression.right.op == 82)) {
/* 1130 */         return null;
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/* 1136 */     return localExpression;
/*      */   }
/*      */   public Expression inlineValue(Environment paramEnvironment, Context paramContext) {
/* 1139 */     if (this.implementation != null)
/* 1140 */       return this.implementation.inlineValue(paramEnvironment, paramContext);
/*      */     try {
/* 1142 */       if (this.field == null)
/* 1143 */         return this;
/*      */       Expression localExpression;
/* 1146 */       if (this.field.isFinal()) {
/* 1147 */         localExpression = (Expression)this.field.getValue(paramEnvironment);
/* 1148 */         if ((localExpression != null) && (localExpression.isConstant()))
/*      */         {
/* 1150 */           localExpression = localExpression.copyInline(paramContext);
/* 1151 */           localExpression.where = this.where;
/* 1152 */           return new CommaExpression(this.where, this.right, localExpression).inlineValue(paramEnvironment, paramContext);
/*      */         }
/*      */       }
/*      */ 
/* 1156 */       if (this.right != null) {
/* 1157 */         if (this.field.isStatic()) {
/* 1158 */           localExpression = this.right.inline(paramEnvironment, paramContext);
/* 1159 */           this.right = null;
/* 1160 */           if (localExpression != null)
/* 1161 */             return new CommaExpression(this.where, localExpression, this);
/*      */         }
/*      */         else {
/* 1164 */           this.right = this.right.inlineValue(paramEnvironment, paramContext);
/*      */         }
/*      */       }
/* 1167 */       return this;
/*      */     }
/*      */     catch (ClassNotFound localClassNotFound) {
/* 1170 */       throw new CompilerError(localClassNotFound);
/*      */     }
/*      */   }
/*      */ 
/* 1174 */   public Expression inlineLHS(Environment paramEnvironment, Context paramContext) { if (this.implementation != null)
/* 1175 */       return this.implementation.inlineLHS(paramEnvironment, paramContext);
/* 1176 */     if (this.right != null) {
/* 1177 */       if (this.field.isStatic()) {
/* 1178 */         Expression localExpression = this.right.inline(paramEnvironment, paramContext);
/* 1179 */         this.right = null;
/* 1180 */         if (localExpression != null)
/* 1181 */           return new CommaExpression(this.where, localExpression, this);
/*      */       }
/*      */       else {
/* 1184 */         this.right = this.right.inlineValue(paramEnvironment, paramContext);
/*      */       }
/*      */     }
/* 1187 */     return this; }
/*      */ 
/*      */   public Expression copyInline(Context paramContext)
/*      */   {
/* 1191 */     if (this.implementation != null)
/* 1192 */       return this.implementation.copyInline(paramContext);
/* 1193 */     return super.copyInline(paramContext);
/*      */   }
/*      */ 
/*      */   public int costInline(int paramInt, Environment paramEnvironment, Context paramContext)
/*      */   {
/* 1200 */     if (this.implementation != null)
/* 1201 */       return this.implementation.costInline(paramInt, paramEnvironment, paramContext);
/* 1202 */     if (paramContext == null)
/*      */     {
/* 1204 */       return 3 + (this.right == null ? 0 : this.right
/* 1204 */         .costInline(paramInt, paramEnvironment, paramContext));
/*      */     }
/*      */ 
/* 1207 */     ClassDefinition localClassDefinition = paramContext.field.getClassDefinition();
/*      */     try
/*      */     {
/* 1211 */       if ((localClassDefinition.permitInlinedAccess(paramEnvironment, this.field.getClassDeclaration())) && 
/* 1212 */         (localClassDefinition
/* 1212 */         .permitInlinedAccess(paramEnvironment, this.field)))
/*      */       {
/* 1213 */         if (this.right == null) {
/* 1214 */           return 3;
/*      */         }
/* 1216 */         ClassDeclaration localClassDeclaration = paramEnvironment.getClassDeclaration(this.right.type);
/* 1217 */         if (localClassDefinition.permitInlinedAccess(paramEnvironment, localClassDeclaration))
/* 1218 */           return 3 + this.right.costInline(paramInt, paramEnvironment, paramContext);
/*      */       }
/*      */     }
/*      */     catch (ClassNotFound localClassNotFound)
/*      */     {
/*      */     }
/* 1224 */     return paramInt;
/*      */   }
/*      */ 
/*      */   int codeLValue(Environment paramEnvironment, Context paramContext, Assembler paramAssembler)
/*      */   {
/* 1231 */     if (this.implementation != null)
/* 1232 */       throw new CompilerError("codeLValue");
/* 1233 */     if (this.field.isStatic()) {
/* 1234 */       if (this.right != null) {
/* 1235 */         this.right.code(paramEnvironment, paramContext, paramAssembler);
/* 1236 */         return 1;
/*      */       }
/* 1238 */       return 0;
/*      */     }
/* 1240 */     this.right.codeValue(paramEnvironment, paramContext, paramAssembler);
/* 1241 */     return 1;
/*      */   }
/*      */   void codeLoad(Environment paramEnvironment, Context paramContext, Assembler paramAssembler) {
/* 1244 */     if (this.field == null) {
/* 1245 */       throw new CompilerError("should not be null");
/*      */     }
/* 1247 */     if (this.field.isStatic())
/* 1248 */       paramAssembler.add(this.where, 178, this.field);
/*      */     else
/* 1250 */       paramAssembler.add(this.where, 180, this.field);
/*      */   }
/*      */ 
/*      */   void codeStore(Environment paramEnvironment, Context paramContext, Assembler paramAssembler) {
/* 1254 */     if (this.field.isStatic())
/* 1255 */       paramAssembler.add(this.where, 179, this.field);
/*      */     else
/* 1257 */       paramAssembler.add(this.where, 181, this.field);
/*      */   }
/*      */ 
/*      */   public void codeValue(Environment paramEnvironment, Context paramContext, Assembler paramAssembler)
/*      */   {
/* 1262 */     codeLValue(paramEnvironment, paramContext, paramAssembler);
/* 1263 */     codeLoad(paramEnvironment, paramContext, paramAssembler);
/*      */   }
/*      */ 
/*      */   public void print(PrintStream paramPrintStream)
/*      */   {
/* 1270 */     paramPrintStream.print("(");
/* 1271 */     if (this.right != null)
/* 1272 */       this.right.print(paramPrintStream);
/*      */     else {
/* 1274 */       paramPrintStream.print("<empty>");
/*      */     }
/* 1276 */     paramPrintStream.print("." + this.id + ")");
/* 1277 */     if (this.implementation != null) {
/* 1278 */       paramPrintStream.print("/IMPL=");
/* 1279 */       this.implementation.print(paramPrintStream);
/*      */     }
/*      */   }
/*      */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     sun.tools.tree.FieldExpression
 * JD-Core Version:    0.6.2
 */