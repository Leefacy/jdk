/*     */ package sun.tools.java;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.io.PrintStream;
/*     */ 
/*     */ public class Environment
/*     */   implements Constants
/*     */ {
/*     */   Environment env;
/*     */   String encoding;
/*     */   Object source;
/* 898 */   private static boolean debugging = System.getProperty("javac.debug") != null;
/*     */ 
/* 973 */   private static boolean dependtrace = System.getProperty("javac.trace.depend") != null
/* 973 */     ;
/*     */ 
/* 994 */   private static boolean dumpmodifiers = System.getProperty("javac.dump.modifiers") != null
/* 994 */     ;
/*     */ 
/*     */   public Environment(Environment paramEnvironment, Object paramObject)
/*     */   {
/*  73 */     if ((paramEnvironment != null) && (paramEnvironment.env != null) && (paramEnvironment.getClass() == getClass()))
/*  74 */       paramEnvironment = paramEnvironment.env;
/*  75 */     this.env = paramEnvironment;
/*  76 */     this.source = paramObject;
/*     */   }
/*     */   public Environment() {
/*  79 */     this(null, null);
/*     */   }
/*     */ 
/*     */   public boolean isExemptPackage(Identifier paramIdentifier)
/*     */   {
/*  87 */     return this.env.isExemptPackage(paramIdentifier);
/*     */   }
/*     */ 
/*     */   public ClassDeclaration getClassDeclaration(Identifier paramIdentifier)
/*     */   {
/*  94 */     return this.env.getClassDeclaration(paramIdentifier);
/*     */   }
/*     */ 
/*     */   public final ClassDefinition getClassDefinition(Identifier paramIdentifier)
/*     */     throws ClassNotFound
/*     */   {
/* 104 */     if (paramIdentifier.isInner()) {
/* 105 */       Object localObject1 = getClassDefinition(paramIdentifier.getTopName());
/* 106 */       Identifier localIdentifier1 = paramIdentifier.getFlatName();
/*     */ 
/* 108 */       while (localIdentifier1.isQualified()) {
/* 109 */         localIdentifier1 = localIdentifier1.getTail();
/* 110 */         Identifier localIdentifier2 = localIdentifier1.getHead();
/*     */ 
/* 112 */         String str = localIdentifier2.toString();
/*     */         Object localObject2;
/* 123 */         if ((str.length() > 0) && 
/* 124 */           (Character.isDigit(str
/* 124 */           .charAt(0))))
/*     */         {
/* 125 */           localObject2 = ((ClassDefinition)localObject1).getLocalClass(str);
/* 126 */           if (localObject2 != null)
/* 127 */             localObject1 = localObject2;
/*     */           else;
/*     */         }
/*     */         else {
/* 131 */           localObject2 = ((ClassDefinition)localObject1).getFirstMatch(localIdentifier2);
/* 132 */           for (; ; localObject2 = ((MemberDefinition)localObject2).getNextMatch()) { if (localObject2 == null) break label128;
/* 133 */             if (((MemberDefinition)localObject2).isInnerClass()) {
/* 134 */               localObject1 = ((MemberDefinition)localObject2).getInnerClass();
/* 135 */               break;
/*     */             }
/*     */           }
/*     */ 
/* 139 */           label128: throw new ClassNotFound(Identifier.lookupInner(((ClassDefinition)localObject1).getName(), localIdentifier2));
/*     */         }
/*     */       }
/* 142 */       return localObject1;
/*     */     }
/* 144 */     return getClassDeclaration(paramIdentifier).getClassDefinition(this);
/*     */   }
/*     */ 
/*     */   public ClassDeclaration getClassDeclaration(Type paramType)
/*     */   {
/* 153 */     return getClassDeclaration(paramType.getClassName());
/*     */   }
/*     */ 
/*     */   public final ClassDefinition getClassDefinition(Type paramType)
/*     */     throws ClassNotFound
/*     */   {
/* 161 */     return getClassDefinition(paramType.getClassName());
/*     */   }
/*     */ 
/*     */   public boolean classExists(Identifier paramIdentifier)
/*     */   {
/* 170 */     return this.env.classExists(paramIdentifier);
/*     */   }
/*     */ 
/*     */   public final boolean classExists(Type paramType) {
/* 174 */     return (!paramType.isType(10)) || (classExists(paramType.getClassName()));
/*     */   }
/*     */ 
/*     */   public Package getPackage(Identifier paramIdentifier)
/*     */     throws IOException
/*     */   {
/* 181 */     return this.env.getPackage(paramIdentifier);
/*     */   }
/*     */ 
/*     */   public void loadDefinition(ClassDeclaration paramClassDeclaration)
/*     */   {
/* 188 */     this.env.loadDefinition(paramClassDeclaration);
/*     */   }
/*     */ 
/*     */   public final Object getSource()
/*     */   {
/* 195 */     return this.source;
/*     */   }
/*     */ 
/*     */   public boolean resolve(long paramLong, ClassDefinition paramClassDefinition, Type paramType)
/*     */   {
/* 232 */     switch (paramType.getTypeCode())
/*     */     {
/*     */     case 10:
/*     */       try {
/* 236 */         Identifier localIdentifier = paramType.getClassName();
/* 237 */         if ((!localIdentifier.isQualified()) && (!localIdentifier.isInner()) && (!classExists(localIdentifier))) {
/* 238 */           resolve(localIdentifier);
/*     */         }
/* 240 */         ClassDefinition localClassDefinition = getQualifiedClassDefinition(paramLong, localIdentifier, paramClassDefinition, false);
/* 241 */         if (!paramClassDefinition.canAccess(this, localClassDefinition.getClassDeclaration()))
/*     */         {
/* 244 */           error(paramLong, "cant.access.class", localClassDefinition);
/* 245 */           return true;
/*     */         }
/* 247 */         localClassDefinition.noteUsedBy(paramClassDefinition, paramLong, this.env);
/*     */       } catch (AmbiguousClass localAmbiguousClass) {
/* 249 */         error(paramLong, "ambig.class", localAmbiguousClass.name1, localAmbiguousClass.name2);
/* 250 */         return false;
/*     */       }
/*     */       catch (ClassNotFound localClassNotFound)
/*     */       {
/*     */         try {
/* 255 */           if ((localClassNotFound.name.isInner()) && 
/* 256 */             (getPackage(localClassNotFound.name
/* 256 */             .getTopName()).exists()))
/* 257 */             this.env.error(paramLong, "class.and.package", localClassNotFound.name
/* 258 */               .getTopName());
/*     */         }
/*     */         catch (IOException localIOException) {
/* 261 */           this.env.error(paramLong, "io.exception", "package check");
/*     */         }
/*     */ 
/* 265 */         error(paramLong, "class.not.found.no.context", localClassNotFound.name);
/* 266 */         return false;
/*     */       }
/* 268 */       return true;
/*     */     case 9:
/* 272 */       return resolve(paramLong, paramClassDefinition, paramType.getElementType());
/*     */     case 12:
/* 275 */       boolean bool = resolve(paramLong, paramClassDefinition, paramType.getReturnType());
/* 276 */       Type[] arrayOfType = paramType.getArgumentTypes();
/* 277 */       for (int i = arrayOfType.length; i-- > 0; ) {
/* 278 */         bool &= resolve(paramLong, paramClassDefinition, arrayOfType[i]);
/*     */       }
/* 280 */       return bool;
/*     */     case 11:
/* 282 */     }return true;
/*     */   }
/*     */ 
/*     */   public boolean resolveByName(long paramLong, ClassDefinition paramClassDefinition, Identifier paramIdentifier)
/*     */   {
/* 291 */     return resolveByName(paramLong, paramClassDefinition, paramIdentifier, false);
/*     */   }
/*     */ 
/*     */   public boolean resolveExtendsByName(long paramLong, ClassDefinition paramClassDefinition, Identifier paramIdentifier) {
/* 295 */     return resolveByName(paramLong, paramClassDefinition, paramIdentifier, true);
/*     */   }
/*     */ 
/*     */   private boolean resolveByName(long paramLong, ClassDefinition paramClassDefinition, Identifier paramIdentifier, boolean paramBoolean)
/*     */   {
/*     */     try
/*     */     {
/* 302 */       if ((!paramIdentifier.isQualified()) && (!paramIdentifier.isInner()) && (!classExists(paramIdentifier))) {
/* 303 */         resolve(paramIdentifier);
/*     */       }
/* 305 */       ClassDefinition localClassDefinition = getQualifiedClassDefinition(paramLong, paramIdentifier, paramClassDefinition, paramBoolean);
/* 306 */       ClassDeclaration localClassDeclaration = localClassDefinition.getClassDeclaration();
/* 307 */       if (((paramBoolean) || (!paramClassDefinition.canAccess(this, localClassDeclaration))) && ((!paramBoolean) || 
/* 309 */         (!paramClassDefinition
/* 309 */         .extendsCanAccess(this, localClassDeclaration))))
/*     */       {
/* 310 */         error(paramLong, "cant.access.class", localClassDefinition);
/* 311 */         return true;
/*     */       }
/*     */     } catch (AmbiguousClass localAmbiguousClass) {
/* 314 */       error(paramLong, "ambig.class", localAmbiguousClass.name1, localAmbiguousClass.name2);
/* 315 */       return false;
/*     */     }
/*     */     catch (ClassNotFound localClassNotFound)
/*     */     {
/*     */       try {
/* 320 */         if ((localClassNotFound.name.isInner()) && 
/* 321 */           (getPackage(localClassNotFound.name
/* 321 */           .getTopName()).exists()))
/* 322 */           this.env.error(paramLong, "class.and.package", localClassNotFound.name
/* 323 */             .getTopName());
/*     */       }
/*     */       catch (IOException localIOException) {
/* 326 */         this.env.error(paramLong, "io.exception", "package check");
/*     */       }
/* 328 */       error(paramLong, "class.not.found", localClassNotFound.name, "type name");
/* 329 */       return false;
/*     */     }
/* 331 */     return true;
/*     */   }
/*     */ 
/*     */   public final ClassDefinition getQualifiedClassDefinition(long paramLong, Identifier paramIdentifier, ClassDefinition paramClassDefinition, boolean paramBoolean)
/*     */     throws ClassNotFound
/*     */   {
/* 344 */     if (paramIdentifier.isInner()) {
/* 345 */       Object localObject1 = getClassDefinition(paramIdentifier.getTopName());
/* 346 */       Identifier localIdentifier1 = paramIdentifier.getFlatName();
/*     */ 
/* 348 */       while (localIdentifier1.isQualified()) {
/* 349 */         localIdentifier1 = localIdentifier1.getTail();
/* 350 */         Identifier localIdentifier2 = localIdentifier1.getHead();
/*     */ 
/* 352 */         String str = localIdentifier2.toString();
/*     */         Object localObject2;
/* 355 */         if ((str.length() > 0) && 
/* 356 */           (Character.isDigit(str
/* 356 */           .charAt(0))))
/*     */         {
/* 357 */           localObject2 = ((ClassDefinition)localObject1).getLocalClass(str);
/* 358 */           if (localObject2 != null)
/* 359 */             localObject1 = localObject2;
/*     */           else;
/*     */         }
/*     */         else {
/* 363 */           localObject2 = ((ClassDefinition)localObject1).getFirstMatch(localIdentifier2);
/* 364 */           for (; ; localObject2 = ((MemberDefinition)localObject2).getNextMatch()) { if (localObject2 == null) break label206;
/* 365 */             if (((MemberDefinition)localObject2).isInnerClass()) {
/* 366 */               ClassDeclaration localClassDeclaration1 = ((ClassDefinition)localObject1).getClassDeclaration();
/* 367 */               localObject1 = ((MemberDefinition)localObject2).getInnerClass();
/* 368 */               ClassDeclaration localClassDeclaration2 = ((ClassDefinition)localObject1).getClassDeclaration();
/*     */ 
/* 373 */               if (((paramBoolean) || 
/* 374 */                 (paramClassDefinition
/* 374 */                 .canAccess(this.env, localClassDeclaration2))) && (
/* 374 */                 (!paramBoolean) || 
/* 377 */                 (paramClassDefinition
/* 377 */                 .extendsCanAccess(this.env, localClassDeclaration2))))
/*     */               {
/*     */                 break;
/*     */               }
/*     */ 
/* 379 */               this.env.error(paramLong, "no.type.access", localIdentifier2, localClassDeclaration1, paramClassDefinition); break;
/*     */             }
/*     */ 
/*     */           }
/*     */ 
/* 408 */           label206: throw new ClassNotFound(Identifier.lookupInner(((ClassDefinition)localObject1).getName(), localIdentifier2));
/*     */         }
/*     */       }
/* 411 */       return localObject1;
/*     */     }
/* 413 */     return getClassDeclaration(paramIdentifier).getClassDefinition(this);
/*     */   }
/*     */ 
/*     */   public Type resolveNames(ClassDefinition paramClassDefinition, Type paramType, boolean paramBoolean)
/*     */   {
/* 430 */     dtEvent("Environment.resolveNames: " + paramClassDefinition + ", " + paramType);
/*     */     Object localObject1;
/*     */     Object localObject2;
/* 431 */     switch (paramType.getTypeCode()) {
/*     */     case 10:
/* 433 */       localObject1 = paramType.getClassName();
/*     */ 
/* 435 */       if (paramBoolean)
/* 436 */         localObject2 = resolvePackageQualifiedName((Identifier)localObject1);
/*     */       else {
/* 438 */         localObject2 = paramClassDefinition.resolveName(this, (Identifier)localObject1);
/*     */       }
/* 440 */       if (localObject1 != localObject2)
/* 441 */         paramType = Type.tClass((Identifier)localObject2); break;
/*     */     case 9:
/* 447 */       paramType = Type.tArray(resolveNames(paramClassDefinition, paramType.getElementType(), paramBoolean));
/* 448 */       break;
/*     */     case 12:
/* 451 */       localObject1 = paramType.getReturnType();
/* 452 */       localObject2 = resolveNames(paramClassDefinition, (Type)localObject1, paramBoolean);
/* 453 */       Type[] arrayOfType1 = paramType.getArgumentTypes();
/* 454 */       Type[] arrayOfType2 = new Type[arrayOfType1.length];
/* 455 */       int i = localObject1 != localObject2 ? 1 : 0;
/* 456 */       for (int j = arrayOfType1.length; j-- > 0; ) {
/* 457 */         Type localType1 = arrayOfType1[j];
/* 458 */         Type localType2 = resolveNames(paramClassDefinition, localType1, paramBoolean);
/* 459 */         arrayOfType2[j] = localType2;
/* 460 */         if (localType1 != localType2) {
/* 461 */           i = 1;
/*     */         }
/*     */       }
/* 464 */       if (i != 0)
/* 465 */         paramType = Type.tMethod((Type)localObject2, arrayOfType2); break;
/*     */     case 11:
/*     */     }
/*     */ 
/* 470 */     return paramType;
/*     */   }
/*     */ 
/*     */   public Identifier resolveName(Identifier paramIdentifier)
/*     */   {
/* 481 */     if (paramIdentifier.isQualified())
/*     */     {
/* 485 */       Identifier localIdentifier = resolveName(paramIdentifier.getHead());
/*     */ 
/* 487 */       if (localIdentifier.hasAmbigPrefix())
/*     */       {
/* 492 */         return localIdentifier;
/*     */       }
/*     */ 
/* 495 */       if (!classExists(localIdentifier)) {
/* 496 */         return resolvePackageQualifiedName(paramIdentifier);
/*     */       }
/*     */       try
/*     */       {
/* 500 */         return getClassDefinition(localIdentifier)
/* 500 */           .resolveInnerClass(this, paramIdentifier
/* 500 */           .getTail());
/*     */       }
/*     */       catch (ClassNotFound localClassNotFound2) {
/* 503 */         return Identifier.lookupInner(localIdentifier, paramIdentifier.getTail());
/*     */       }
/*     */     }
/*     */     try {
/* 507 */       return resolve(paramIdentifier);
/*     */     }
/*     */     catch (AmbiguousClass localAmbiguousClass)
/*     */     {
/* 516 */       if (paramIdentifier.hasAmbigPrefix()) {
/* 517 */         return paramIdentifier;
/*     */       }
/* 519 */       return paramIdentifier.addAmbigPrefix();
/*     */     }
/*     */     catch (ClassNotFound localClassNotFound1)
/*     */     {
/* 523 */       Imports localImports = getImports();
/* 524 */       if (localImports != null)
/* 525 */         return localImports.forceResolve(this, paramIdentifier);
/*     */     }
/* 527 */     return paramIdentifier;
/*     */   }
/*     */ 
/*     */   public final Identifier resolvePackageQualifiedName(Identifier paramIdentifier)
/*     */   {
/* 542 */     Identifier localIdentifier1 = null;
/*     */ 
/* 544 */     while (!classExists(paramIdentifier))
/*     */     {
/* 547 */       if (!paramIdentifier.isQualified()) {
/* 548 */         paramIdentifier = localIdentifier1 == null ? paramIdentifier : Identifier.lookup(paramIdentifier, localIdentifier1);
/* 549 */         localIdentifier1 = null;
/* 550 */         break;
/*     */       }
/* 552 */       Identifier localIdentifier2 = paramIdentifier.getName();
/* 553 */       localIdentifier1 = localIdentifier1 == null ? localIdentifier2 : Identifier.lookup(localIdentifier2, localIdentifier1);
/* 554 */       paramIdentifier = paramIdentifier.getQualifier();
/*     */     }
/* 556 */     if (localIdentifier1 != null)
/* 557 */       paramIdentifier = Identifier.lookupInner(paramIdentifier, localIdentifier1);
/* 558 */     return paramIdentifier;
/*     */   }
/*     */ 
/*     */   public Identifier resolve(Identifier paramIdentifier)
/*     */     throws ClassNotFound
/*     */   {
/* 565 */     if (this.env == null) return paramIdentifier;
/* 566 */     return this.env.resolve(paramIdentifier);
/*     */   }
/*     */ 
/*     */   public Imports getImports()
/*     */   {
/* 573 */     if (this.env == null) return null;
/* 574 */     return this.env.getImports();
/*     */   }
/*     */ 
/*     */   public ClassDefinition makeClassDefinition(Environment paramEnvironment, long paramLong, IdentifierToken paramIdentifierToken1, String paramString, int paramInt, IdentifierToken paramIdentifierToken2, IdentifierToken[] paramArrayOfIdentifierToken, ClassDefinition paramClassDefinition)
/*     */   {
/* 586 */     if (this.env == null) return null;
/* 587 */     return this.env.makeClassDefinition(paramEnvironment, paramLong, paramIdentifierToken1, paramString, paramInt, paramIdentifierToken2, paramArrayOfIdentifierToken, paramClassDefinition);
/*     */   }
/*     */ 
/*     */   public MemberDefinition makeMemberDefinition(Environment paramEnvironment, long paramLong, ClassDefinition paramClassDefinition, String paramString, int paramInt, Type paramType, Identifier paramIdentifier, IdentifierToken[] paramArrayOfIdentifierToken1, IdentifierToken[] paramArrayOfIdentifierToken2, Object paramObject)
/*     */   {
/* 602 */     if (this.env == null) return null;
/* 603 */     return this.env.makeMemberDefinition(paramEnvironment, paramLong, paramClassDefinition, paramString, paramInt, paramType, paramIdentifier, paramArrayOfIdentifierToken1, paramArrayOfIdentifierToken2, paramObject);
/*     */   }
/*     */ 
/*     */   public boolean isApplicable(MemberDefinition paramMemberDefinition, Type[] paramArrayOfType)
/*     */     throws ClassNotFound
/*     */   {
/* 612 */     Type localType = paramMemberDefinition.getType();
/* 613 */     if (!localType.isType(12))
/* 614 */       return false;
/* 615 */     Type[] arrayOfType = localType.getArgumentTypes();
/* 616 */     if (paramArrayOfType.length != arrayOfType.length)
/* 617 */       return false;
/* 618 */     int i = paramArrayOfType.length;
/*     */     do { i--; if (i < 0) break; }
/* 619 */     while (isMoreSpecific(paramArrayOfType[i], arrayOfType[i]));
/* 620 */     return false;
/* 621 */     return true;
/*     */   }
/*     */ 
/*     */   public boolean isMoreSpecific(MemberDefinition paramMemberDefinition1, MemberDefinition paramMemberDefinition2)
/*     */     throws ClassNotFound
/*     */   {
/* 630 */     Type localType1 = paramMemberDefinition1.getClassDeclaration().getType();
/* 631 */     Type localType2 = paramMemberDefinition2.getClassDeclaration().getType();
/*     */ 
/* 633 */     boolean bool = (isMoreSpecific(localType1, localType2)) && 
/* 633 */       (isApplicable(paramMemberDefinition2, paramMemberDefinition1
/* 633 */       .getType().getArgumentTypes()));
/*     */ 
/* 636 */     return bool;
/*     */   }
/*     */ 
/*     */   public boolean isMoreSpecific(Type paramType1, Type paramType2)
/*     */     throws ClassNotFound
/*     */   {
/* 644 */     return implicitCast(paramType1, paramType2);
/*     */   }
/*     */ 
/*     */   public boolean implicitCast(Type paramType1, Type paramType2)
/*     */     throws ClassNotFound
/*     */   {
/* 652 */     if (paramType1 == paramType2) {
/* 653 */       return true;
/*     */     }
/* 655 */     int i = paramType2.getTypeCode();
/*     */ 
/* 657 */     switch (paramType1.getTypeCode()) {
/*     */     case 1:
/* 659 */       if (i == 3)
/* 660 */         return true;
/*     */     case 2:
/*     */     case 3:
/* 663 */       if (i == 4) return true;
/*     */     case 4:
/* 665 */       if (i == 5) return true;
/*     */     case 5:
/* 667 */       if (i == 6) return true;
/*     */     case 6:
/* 669 */       if (i == 7) return true;
/*     */     case 7:
/*     */     default:
/* 672 */       return false;
/*     */     case 8:
/* 675 */       return paramType2.inMask(1792);
/*     */     case 9:
/* 678 */       if (!paramType2.isType(9)) {
/* 679 */         return (paramType2 == Type.tObject) || (paramType2 == Type.tCloneable) || (paramType2 == Type.tSerializable);
/*     */       }
/*     */ 
/*     */       do
/*     */       {
/* 684 */         paramType1 = paramType1.getElementType();
/* 685 */         paramType2 = paramType2.getElementType();
/* 686 */       }while ((paramType1.isType(9)) && (paramType2.isType(9)));
/* 687 */       if ((paramType1.inMask(1536)) && 
/* 688 */         (paramType2
/* 688 */         .inMask(1536)))
/*     */       {
/* 689 */         return isMoreSpecific(paramType1, paramType2);
/*     */       }
/* 691 */       return paramType1.getTypeCode() == paramType2.getTypeCode();
/*     */     case 10:
/*     */     }
/*     */ 
/* 696 */     if (i == 10) {
/* 697 */       ClassDefinition localClassDefinition1 = getClassDefinition(paramType1);
/* 698 */       ClassDefinition localClassDefinition2 = getClassDefinition(paramType2);
/* 699 */       return localClassDefinition2.implementedBy(this, localClassDefinition1
/* 700 */         .getClassDeclaration());
/*     */     }
/* 702 */     return false;
/*     */   }
/*     */ 
/*     */   public boolean explicitCast(Type paramType1, Type paramType2)
/*     */     throws ClassNotFound
/*     */   {
/* 713 */     if (implicitCast(paramType1, paramType2)) {
/* 714 */       return true;
/*     */     }
/* 716 */     if (paramType1.inMask(254))
/* 717 */       return paramType2.inMask(254);
/*     */     Object localObject1;
/*     */     Object localObject2;
/* 719 */     if ((paramType1.isType(10)) && (paramType2.isType(10))) {
/* 720 */       localObject1 = getClassDefinition(paramType1);
/* 721 */       localObject2 = getClassDefinition(paramType2);
/* 722 */       if (((ClassDefinition)localObject2).isFinal()) {
/* 723 */         return ((ClassDefinition)localObject1).implementedBy(this, ((ClassDefinition)localObject2)
/* 724 */           .getClassDeclaration());
/*     */       }
/* 726 */       if (((ClassDefinition)localObject1).isFinal()) {
/* 727 */         return ((ClassDefinition)localObject2).implementedBy(this, ((ClassDefinition)localObject1)
/* 728 */           .getClassDeclaration());
/*     */       }
/*     */ 
/* 736 */       if ((((ClassDefinition)localObject2).isInterface()) && (((ClassDefinition)localObject1).isInterface())) {
/* 737 */         return ((ClassDefinition)localObject2).couldImplement((ClassDefinition)localObject1);
/*     */       }
/*     */ 
/* 742 */       return (((ClassDefinition)localObject2).isInterface()) || 
/* 741 */         (((ClassDefinition)localObject1)
/* 741 */         .isInterface()) || 
/* 742 */         (((ClassDefinition)localObject1)
/* 742 */         .superClassOf(this, ((ClassDefinition)localObject2)
/* 742 */         .getClassDeclaration()));
/*     */     }
/* 744 */     if (paramType2.isType(9))
/* 745 */       if (paramType1.isType(9)) {
/* 746 */         localObject1 = paramType1.getElementType();
/* 747 */         localObject2 = paramType2.getElementType();
/* 748 */         while ((((Type)localObject1).getTypeCode() == 9) && 
/* 749 */           (((Type)localObject2)
/* 749 */           .getTypeCode() == 9)) {
/* 750 */           localObject1 = ((Type)localObject1).getElementType();
/* 751 */           localObject2 = ((Type)localObject2).getElementType();
/*     */         }
/* 753 */         if ((((Type)localObject1).inMask(1536)) && 
/* 754 */           (((Type)localObject2)
/* 754 */           .inMask(1536)))
/*     */         {
/* 755 */           return explicitCast((Type)localObject1, (Type)localObject2);
/*     */         }
/* 757 */       } else if ((paramType1 == Type.tObject) || (paramType1 == Type.tCloneable) || (paramType1 == Type.tSerializable))
/*     */       {
/* 759 */         return true;
/*     */       }
/* 761 */     return false;
/*     */   }
/*     */ 
/*     */   public int getFlags()
/*     */   {
/* 768 */     return this.env.getFlags();
/*     */   }
/*     */ 
/*     */   public final boolean debug_lines()
/*     */   {
/* 778 */     return (getFlags() & 0x1000) != 0;
/*     */   }
/*     */   public final boolean debug_vars() {
/* 781 */     return (getFlags() & 0x2000) != 0;
/*     */   }
/*     */   public final boolean debug_source() {
/* 784 */     return (getFlags() & 0x40000) != 0;
/*     */   }
/*     */ 
/*     */   public final boolean opt()
/*     */   {
/* 793 */     return (getFlags() & 0x4000) != 0;
/*     */   }
/*     */   public final boolean opt_interclass() {
/* 796 */     return (getFlags() & 0x8000) != 0;
/*     */   }
/*     */ 
/*     */   public final boolean verbose()
/*     */   {
/* 803 */     return (getFlags() & 0x1) != 0;
/*     */   }
/*     */ 
/*     */   public final boolean dump()
/*     */   {
/* 810 */     return (getFlags() & 0x2) != 0;
/*     */   }
/*     */ 
/*     */   public final boolean warnings()
/*     */   {
/* 817 */     return (getFlags() & 0x4) != 0;
/*     */   }
/*     */ 
/*     */   public final boolean dependencies()
/*     */   {
/* 824 */     return (getFlags() & 0x20) != 0;
/*     */   }
/*     */ 
/*     */   public final boolean print_dependencies()
/*     */   {
/* 831 */     return (getFlags() & 0x400) != 0;
/*     */   }
/*     */ 
/*     */   public final boolean deprecation()
/*     */   {
/* 838 */     return (getFlags() & 0x200) != 0;
/*     */   }
/*     */ 
/*     */   public final boolean version12()
/*     */   {
/* 846 */     return (getFlags() & 0x800) != 0;
/*     */   }
/*     */ 
/*     */   public final boolean strictdefault()
/*     */   {
/* 853 */     return (getFlags() & 0x20000) != 0;
/*     */   }
/*     */ 
/*     */   public void shutdown()
/*     */   {
/* 860 */     if (this.env != null)
/* 861 */       this.env.shutdown();
/*     */   }
/*     */ 
/*     */   public void error(Object paramObject1, long paramLong, String paramString, Object paramObject2, Object paramObject3, Object paramObject4)
/*     */   {
/* 875 */     this.env.error(paramObject1, paramLong, paramString, paramObject2, paramObject3, paramObject4);
/*     */   }
/*     */   public final void error(long paramLong, String paramString, Object paramObject1, Object paramObject2, Object paramObject3) {
/* 878 */     error(this.source, paramLong, paramString, paramObject1, paramObject2, paramObject3);
/*     */   }
/*     */   public final void error(long paramLong, String paramString, Object paramObject1, Object paramObject2) {
/* 881 */     error(this.source, paramLong, paramString, paramObject1, paramObject2, null);
/*     */   }
/*     */   public final void error(long paramLong, String paramString, Object paramObject) {
/* 884 */     error(this.source, paramLong, paramString, paramObject, null, null);
/*     */   }
/*     */   public final void error(long paramLong, String paramString) {
/* 887 */     error(this.source, paramLong, paramString, null, null, null);
/*     */   }
/*     */ 
/*     */   public void output(String paramString)
/*     */   {
/* 895 */     this.env.output(paramString);
/*     */   }
/*     */ 
/*     */   public static void debugOutput(Object paramObject)
/*     */   {
/* 901 */     if (debugging)
/* 902 */       System.out.println(paramObject.toString());
/*     */   }
/*     */ 
/*     */   public void setCharacterEncoding(String paramString)
/*     */   {
/* 909 */     this.encoding = paramString;
/*     */   }
/*     */ 
/*     */   public String getCharacterEncoding()
/*     */   {
/* 916 */     return this.encoding;
/*     */   }
/*     */ 
/*     */   public short getMajorVersion()
/*     */   {
/* 923 */     if (this.env == null) return 45;
/* 924 */     return this.env.getMajorVersion();
/*     */   }
/*     */ 
/*     */   public short getMinorVersion()
/*     */   {
/* 931 */     if (this.env == null) return 3;
/* 932 */     return this.env.getMinorVersion();
/*     */   }
/*     */ 
/*     */   public final boolean coverage()
/*     */   {
/* 940 */     return (getFlags() & 0x40) != 0;
/*     */   }
/*     */ 
/*     */   public final boolean covdata()
/*     */   {
/* 947 */     return (getFlags() & 0x80) != 0;
/*     */   }
/*     */ 
/*     */   public File getcovFile()
/*     */   {
/* 954 */     return this.env.getcovFile();
/*     */   }
/*     */ 
/*     */   public void dtEnter(String paramString)
/*     */   {
/* 976 */     if (dependtrace) System.out.println(">>> " + paramString); 
/*     */   }
/*     */ 
/*     */   public void dtExit(String paramString)
/*     */   {
/* 980 */     if (dependtrace) System.out.println("<<< " + paramString); 
/*     */   }
/*     */ 
/*     */   public void dtEvent(String paramString)
/*     */   {
/* 984 */     if (dependtrace) System.out.println(paramString);
/*     */   }
/*     */ 
/*     */   public boolean dumpModifiers()
/*     */   {
/* 996 */     return dumpmodifiers;
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     sun.tools.java.Environment
 * JD-Core Version:    0.6.2
 */