/*      */ package sun.tools.javac;
/*      */ 
/*      */ import java.io.ByteArrayOutputStream;
/*      */ import java.io.DataOutputStream;
/*      */ import java.io.IOException;
/*      */ import java.io.OutputStream;
/*      */ import java.io.PrintStream;
/*      */ import java.util.Arrays;
/*      */ import java.util.Enumeration;
/*      */ import java.util.Hashtable;
/*      */ import java.util.Iterator;
/*      */ import java.util.Vector;
/*      */ import sun.tools.asm.Assembler;
/*      */ import sun.tools.asm.ConstantPool;
/*      */ import sun.tools.java.AmbiguousClass;
/*      */ import sun.tools.java.ClassDeclaration;
/*      */ import sun.tools.java.ClassDefinition;
/*      */ import sun.tools.java.ClassFile;
/*      */ import sun.tools.java.ClassNotFound;
/*      */ import sun.tools.java.CompilerError;
/*      */ import sun.tools.java.Environment;
/*      */ import sun.tools.java.Identifier;
/*      */ import sun.tools.java.IdentifierToken;
/*      */ import sun.tools.java.Imports;
/*      */ import sun.tools.java.MemberDefinition;
/*      */ import sun.tools.java.Type;
/*      */ import sun.tools.tree.AssignExpression;
/*      */ import sun.tools.tree.CatchStatement;
/*      */ import sun.tools.tree.CompoundStatement;
/*      */ import sun.tools.tree.Context;
/*      */ import sun.tools.tree.Expression;
/*      */ import sun.tools.tree.ExpressionStatement;
/*      */ import sun.tools.tree.FieldExpression;
/*      */ import sun.tools.tree.IdentifierExpression;
/*      */ import sun.tools.tree.LocalMember;
/*      */ import sun.tools.tree.MethodExpression;
/*      */ import sun.tools.tree.NewInstanceExpression;
/*      */ import sun.tools.tree.ReturnStatement;
/*      */ import sun.tools.tree.Statement;
/*      */ import sun.tools.tree.StringExpression;
/*      */ import sun.tools.tree.SuperExpression;
/*      */ import sun.tools.tree.ThisExpression;
/*      */ import sun.tools.tree.ThrowStatement;
/*      */ import sun.tools.tree.TryStatement;
/*      */ import sun.tools.tree.TypeExpression;
/*      */ import sun.tools.tree.UplevelReference;
/*      */ import sun.tools.tree.Vset;
/*      */ 
/*      */ @Deprecated
/*      */ public class SourceClass extends ClassDefinition
/*      */ {
/*      */   Environment toplevelEnv;
/*      */   SourceMember defConstructor;
/*   68 */   ConstantPool tab = new ConstantPool();
/*      */ 
/*   73 */   Hashtable deps = new Hashtable(11);
/*      */   LocalMember thisArg;
/*      */   long endPosition;
/*   95 */   private Type dummyArgumentType = null;
/*      */ 
/*  631 */   private boolean sourceFileChecked = false;
/*      */ 
/*  656 */   private boolean supersChecked = false;
/*      */ 
/* 1073 */   private boolean basicChecking = false;
/* 1074 */   private boolean basicCheckDone = false;
/*      */ 
/* 1196 */   private boolean resolving = false;
/*      */ 
/* 1396 */   private boolean inlinedLocalClass = false;
/*      */ 
/* 1492 */   private static int[] classModifierBits = { 1, 2, 4, 8, 16, 512, 1024, 32, 65536, 131072, 2097152, 2048 };
/*      */ 
/* 1497 */   private static String[] classModifierNames = { "PUBLIC", "PRIVATE", "PROTECTED", "STATIC", "FINAL", "INTERFACE", "ABSTRACT", "SUPER", "ANONYMOUS", "LOCAL", "STRICTFP", "STRICT" };
/*      */ 
/* 1897 */   private MemberDefinition lookup = null;
/*      */ 
/* 2043 */   private static Vector active = new Vector();
/*      */ 
/*      */   public SourceClass(Environment paramEnvironment, long paramLong, ClassDeclaration paramClassDeclaration, String paramString, int paramInt, IdentifierToken paramIdentifierToken, IdentifierToken[] paramArrayOfIdentifierToken, SourceClass paramSourceClass, Identifier paramIdentifier)
/*      */   {
/*  105 */     super(paramEnvironment.getSource(), paramLong, paramClassDeclaration, paramInt, paramIdentifierToken, paramArrayOfIdentifierToken);
/*      */ 
/*  107 */     setOuterClass(paramSourceClass);
/*      */ 
/*  109 */     this.toplevelEnv = paramEnvironment;
/*  110 */     this.documentation = paramString;
/*      */ 
/*  112 */     if (ClassDefinition.containsDeprecated(paramString)) {
/*  113 */       this.modifiers |= 262144;
/*      */     }
/*      */ 
/*  117 */     if ((isStatic()) && (paramSourceClass == null)) {
/*  118 */       paramEnvironment.error(paramLong, "static.class", this);
/*  119 */       this.modifiers &= -9;
/*      */     }
/*      */ 
/*  129 */     if ((isLocal()) || ((paramSourceClass != null) && (!paramSourceClass.isTopLevel()))) {
/*  130 */       if (isInterface()) {
/*  131 */         paramEnvironment.error(paramLong, "inner.interface");
/*  132 */       } else if (isStatic()) {
/*  133 */         paramEnvironment.error(paramLong, "static.inner.class", this);
/*  134 */         this.modifiers &= -9;
/*  135 */         if (this.innerClassMember != null) {
/*  136 */           this.innerClassMember.subModifiers(8);
/*      */         }
/*      */       }
/*      */     }
/*      */ 
/*  141 */     if ((isPrivate()) && (paramSourceClass == null)) {
/*  142 */       paramEnvironment.error(paramLong, "private.class", this);
/*  143 */       this.modifiers &= -3;
/*      */     }
/*  145 */     if ((isProtected()) && (paramSourceClass == null)) {
/*  146 */       paramEnvironment.error(paramLong, "protected.class", this);
/*  147 */       this.modifiers &= -5;
/*      */     }
/*      */     Object localObject2;
/*  156 */     if ((!isTopLevel()) && (!isLocal())) {
/*  157 */       localObject1 = paramSourceClass.getThisArgument();
/*  158 */       localObject2 = getReference((LocalMember)localObject1);
/*  159 */       setOuterMember(((UplevelReference)localObject2).getLocalField(paramEnvironment));
/*      */     }
/*      */ 
/*  165 */     if (paramIdentifier != null) {
/*  166 */       setLocalName(paramIdentifier);
/*      */     }
/*      */ 
/*  173 */     Object localObject1 = getLocalName();
/*  174 */     if (localObject1 != idNull)
/*      */     {
/*  177 */       for (localObject2 = paramSourceClass; localObject2 != null; 
/*  178 */         localObject2 = ((ClassDefinition)localObject2).getOuterClass()) {
/*  179 */         Identifier localIdentifier = ((ClassDefinition)localObject2).getLocalName();
/*  180 */         if (localObject1.equals(localIdentifier))
/*  181 */           paramEnvironment.error(paramLong, "inner.redefined", localObject1);
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   public long getEndPosition()
/*      */   {
/*  191 */     return this.endPosition;
/*      */   }
/*      */ 
/*      */   public void setEndPosition(long paramLong) {
/*  195 */     this.endPosition = paramLong;
/*      */   }
/*      */ 
/*      */   public String getAbsoluteName()
/*      */   {
/*  204 */     String str = ((ClassFile)getSource()).getAbsoluteName();
/*      */ 
/*  206 */     return str;
/*      */   }
/*      */ 
/*      */   public Imports getImports()
/*      */   {
/*  214 */     return this.toplevelEnv.getImports();
/*      */   }
/*      */ 
/*      */   public LocalMember getThisArgument()
/*      */   {
/*  221 */     if (this.thisArg == null) {
/*  222 */       this.thisArg = new LocalMember(this.where, this, 0, getType(), idThis);
/*      */     }
/*  224 */     return this.thisArg;
/*      */   }
/*      */ 
/*      */   public void addDependency(ClassDeclaration paramClassDeclaration)
/*      */   {
/*  231 */     if (this.tab != null) {
/*  232 */       this.tab.put(paramClassDeclaration);
/*      */     }
/*      */ 
/*  237 */     if ((this.toplevelEnv.print_dependencies()) && (paramClassDeclaration != getClassDeclaration()))
/*  238 */       this.deps.put(paramClassDeclaration, paramClassDeclaration);
/*      */   }
/*      */ 
/*      */   public void addMember(Environment paramEnvironment, MemberDefinition paramMemberDefinition)
/*      */   {
/*  247 */     switch (paramMemberDefinition.getModifiers() & 0x7) {
/*      */     case 0:
/*      */     case 1:
/*      */     case 2:
/*      */     case 4:
/*  252 */       break;
/*      */     case 3:
/*      */     default:
/*  254 */       paramEnvironment.error(paramMemberDefinition.getWhere(), "inconsistent.modifier", paramMemberDefinition);
/*      */ 
/*  256 */       if (paramMemberDefinition.isPublic())
/*  257 */         paramMemberDefinition.subModifiers(6);
/*      */       else {
/*  259 */         paramMemberDefinition.subModifiers(2);
/*      */       }
/*      */ 
/*      */       break;
/*      */     }
/*      */ 
/*  265 */     if ((paramMemberDefinition.isStatic()) && (!isTopLevel()) && (!paramMemberDefinition.isSynthetic()))
/*  266 */       if (paramMemberDefinition.isMethod()) {
/*  267 */         paramEnvironment.error(paramMemberDefinition.getWhere(), "static.inner.method", paramMemberDefinition, this);
/*  268 */         paramMemberDefinition.subModifiers(8);
/*  269 */       } else if (paramMemberDefinition.isVariable()) {
/*  270 */         if ((!paramMemberDefinition.isFinal()) || (paramMemberDefinition.isBlankFinal())) {
/*  271 */           paramEnvironment.error(paramMemberDefinition.getWhere(), "static.inner.field", paramMemberDefinition.getName(), this);
/*  272 */           paramMemberDefinition.subModifiers(8);
/*      */         }
/*      */ 
/*      */       }
/*      */       else
/*      */       {
/*  280 */         paramMemberDefinition.subModifiers(8);
/*      */       }
/*      */     Object localObject1;
/*  284 */     if (paramMemberDefinition.isMethod()) {
/*  285 */       if (paramMemberDefinition.isConstructor()) {
/*  286 */         if (paramMemberDefinition.getClassDefinition().isInterface()) {
/*  287 */           paramEnvironment.error(paramMemberDefinition.getWhere(), "intf.constructor");
/*  288 */           return;
/*      */         }
/*  290 */         if ((paramMemberDefinition.isNative()) || (paramMemberDefinition.isAbstract()) || 
/*  291 */           (paramMemberDefinition
/*  291 */           .isStatic()) || (paramMemberDefinition.isSynchronized()) || (paramMemberDefinition.isFinal())) {
/*  292 */           paramEnvironment.error(paramMemberDefinition.getWhere(), "constr.modifier", paramMemberDefinition);
/*  293 */           paramMemberDefinition.subModifiers(1336);
/*      */         }
/*      */       }
/*  296 */       else if ((paramMemberDefinition.isInitializer()) && 
/*  297 */         (paramMemberDefinition.getClassDefinition().isInterface())) {
/*  298 */         paramEnvironment.error(paramMemberDefinition.getWhere(), "intf.initializer");
/*  299 */         return;
/*      */       }
/*      */ 
/*  304 */       if (paramMemberDefinition.getType().getReturnType().isVoidArray()) {
/*  305 */         paramEnvironment.error(paramMemberDefinition.getWhere(), "void.array");
/*      */       }
/*      */ 
/*  308 */       if ((paramMemberDefinition.getClassDefinition().isInterface()) && (
/*  309 */         (paramMemberDefinition
/*  309 */         .isStatic()) || (paramMemberDefinition.isSynchronized()) || (paramMemberDefinition.isNative()) || 
/*  310 */         (paramMemberDefinition
/*  310 */         .isFinal()) || (paramMemberDefinition.isPrivate()) || (paramMemberDefinition.isProtected()))) {
/*  311 */         paramEnvironment.error(paramMemberDefinition.getWhere(), "intf.modifier.method", paramMemberDefinition);
/*  312 */         paramMemberDefinition.subModifiers(314);
/*      */       }
/*      */ 
/*  315 */       if (paramMemberDefinition.isTransient()) {
/*  316 */         paramEnvironment.error(paramMemberDefinition.getWhere(), "transient.meth", paramMemberDefinition);
/*  317 */         paramMemberDefinition.subModifiers(128);
/*      */       }
/*  319 */       if (paramMemberDefinition.isVolatile()) {
/*  320 */         paramEnvironment.error(paramMemberDefinition.getWhere(), "volatile.meth", paramMemberDefinition);
/*  321 */         paramMemberDefinition.subModifiers(64);
/*      */       }
/*  323 */       if (paramMemberDefinition.isAbstract()) {
/*  324 */         if (paramMemberDefinition.isPrivate()) {
/*  325 */           paramEnvironment.error(paramMemberDefinition.getWhere(), "abstract.private.modifier", paramMemberDefinition);
/*  326 */           paramMemberDefinition.subModifiers(2);
/*      */         }
/*  328 */         if (paramMemberDefinition.isStatic()) {
/*  329 */           paramEnvironment.error(paramMemberDefinition.getWhere(), "abstract.static.modifier", paramMemberDefinition);
/*  330 */           paramMemberDefinition.subModifiers(8);
/*      */         }
/*  332 */         if (paramMemberDefinition.isFinal()) {
/*  333 */           paramEnvironment.error(paramMemberDefinition.getWhere(), "abstract.final.modifier", paramMemberDefinition);
/*  334 */           paramMemberDefinition.subModifiers(16);
/*      */         }
/*  336 */         if (paramMemberDefinition.isNative()) {
/*  337 */           paramEnvironment.error(paramMemberDefinition.getWhere(), "abstract.native.modifier", paramMemberDefinition);
/*  338 */           paramMemberDefinition.subModifiers(256);
/*      */         }
/*  340 */         if (paramMemberDefinition.isSynchronized()) {
/*  341 */           paramEnvironment.error(paramMemberDefinition.getWhere(), "abstract.synchronized.modifier", paramMemberDefinition);
/*  342 */           paramMemberDefinition.subModifiers(32);
/*      */         }
/*      */       }
/*  345 */       if ((paramMemberDefinition.isAbstract()) || (paramMemberDefinition.isNative())) {
/*  346 */         if (paramMemberDefinition.getValue() != null) {
/*  347 */           paramEnvironment.error(paramMemberDefinition.getWhere(), "invalid.meth.body", paramMemberDefinition);
/*  348 */           paramMemberDefinition.setValue(null);
/*      */         }
/*      */       }
/*  351 */       else if (paramMemberDefinition.getValue() == null) {
/*  352 */         if (paramMemberDefinition.isConstructor())
/*  353 */           paramEnvironment.error(paramMemberDefinition.getWhere(), "no.constructor.body", paramMemberDefinition);
/*      */         else {
/*  355 */           paramEnvironment.error(paramMemberDefinition.getWhere(), "no.meth.body", paramMemberDefinition);
/*      */         }
/*  357 */         paramMemberDefinition.addModifiers(1024);
/*      */       }
/*      */ 
/*  360 */       localObject1 = paramMemberDefinition.getArguments();
/*  361 */       if (localObject1 != null)
/*      */       {
/*  363 */         int i = ((Vector)localObject1).size();
/*  364 */         Type[] arrayOfType = paramMemberDefinition.getType().getArgumentTypes();
/*  365 */         for (int j = 0; j < arrayOfType.length; j++) {
/*  366 */           Object localObject2 = ((Vector)localObject1).elementAt(j);
/*  367 */           long l = paramMemberDefinition.getWhere();
/*  368 */           if ((localObject2 instanceof MemberDefinition)) {
/*  369 */             l = ((MemberDefinition)localObject2).getWhere();
/*  370 */             localObject2 = ((MemberDefinition)localObject2).getName();
/*      */           }
/*      */ 
/*  373 */           if ((arrayOfType[j].isType(11)) || 
/*  374 */             (arrayOfType[j]
/*  374 */             .isVoidArray()))
/*  375 */             paramEnvironment.error(l, "void.argument", localObject2);
/*      */         }
/*      */       }
/*      */     }
/*  379 */     else if (paramMemberDefinition.isInnerClass()) {
/*  380 */       if ((paramMemberDefinition.isVolatile()) || 
/*  381 */         (paramMemberDefinition
/*  381 */         .isTransient()) || (paramMemberDefinition.isNative()) || (paramMemberDefinition.isSynchronized())) {
/*  382 */         paramEnvironment.error(paramMemberDefinition.getWhere(), "inner.modifier", paramMemberDefinition);
/*  383 */         paramMemberDefinition.subModifiers(480);
/*      */       }
/*      */ 
/*  387 */       if ((paramMemberDefinition.getClassDefinition().isInterface()) && (
/*  388 */         (paramMemberDefinition
/*  388 */         .isPrivate()) || (paramMemberDefinition.isProtected()))) {
/*  389 */         paramEnvironment.error(paramMemberDefinition.getWhere(), "intf.modifier.field", paramMemberDefinition);
/*  390 */         paramMemberDefinition.subModifiers(6);
/*  391 */         paramMemberDefinition.addModifiers(1);
/*      */ 
/*  394 */         localObject1 = paramMemberDefinition.getInnerClass();
/*  395 */         ((ClassDefinition)localObject1).subModifiers(6);
/*  396 */         ((ClassDefinition)localObject1).addModifiers(1);
/*      */       }
/*      */     } else {
/*  399 */       if ((paramMemberDefinition.getType().isType(11)) || (paramMemberDefinition.getType().isVoidArray())) {
/*  400 */         paramEnvironment.error(paramMemberDefinition.getWhere(), "void.inst.var", paramMemberDefinition.getName());
/*      */ 
/*  402 */         return;
/*      */       }
/*      */ 
/*  405 */       if ((paramMemberDefinition.isSynchronized()) || (paramMemberDefinition.isAbstract()) || (paramMemberDefinition.isNative())) {
/*  406 */         paramEnvironment.error(paramMemberDefinition.getWhere(), "var.modifier", paramMemberDefinition);
/*  407 */         paramMemberDefinition.subModifiers(1312);
/*      */       }
/*  409 */       if (paramMemberDefinition.isStrict()) {
/*  410 */         paramEnvironment.error(paramMemberDefinition.getWhere(), "var.floatmodifier", paramMemberDefinition);
/*  411 */         paramMemberDefinition.subModifiers(2097152);
/*      */       }
/*  413 */       if ((paramMemberDefinition.isTransient()) && (isInterface())) {
/*  414 */         paramEnvironment.error(paramMemberDefinition.getWhere(), "transient.modifier", paramMemberDefinition);
/*  415 */         paramMemberDefinition.subModifiers(128);
/*      */       }
/*  417 */       if ((paramMemberDefinition.isVolatile()) && ((isInterface()) || (paramMemberDefinition.isFinal()))) {
/*  418 */         paramEnvironment.error(paramMemberDefinition.getWhere(), "volatile.modifier", paramMemberDefinition);
/*  419 */         paramMemberDefinition.subModifiers(64);
/*      */       }
/*  421 */       if ((paramMemberDefinition.isFinal()) && (paramMemberDefinition.getValue() == null) && (isInterface())) {
/*  422 */         paramEnvironment.error(paramMemberDefinition.getWhere(), "initializer.needed", paramMemberDefinition);
/*  423 */         paramMemberDefinition.subModifiers(16);
/*      */       }
/*      */ 
/*  426 */       if ((paramMemberDefinition.getClassDefinition().isInterface()) && (
/*  427 */         (paramMemberDefinition
/*  427 */         .isPrivate()) || (paramMemberDefinition.isProtected()))) {
/*  428 */         paramEnvironment.error(paramMemberDefinition.getWhere(), "intf.modifier.field", paramMemberDefinition);
/*  429 */         paramMemberDefinition.subModifiers(6);
/*  430 */         paramMemberDefinition.addModifiers(1);
/*      */       }
/*      */     }
/*      */ 
/*  434 */     if (!paramMemberDefinition.isInitializer()) {
/*  435 */       for (localObject1 = getFirstMatch(paramMemberDefinition.getName()); 
/*  436 */         localObject1 != null; localObject1 = ((MemberDefinition)localObject1).getNextMatch()) {
/*  437 */         if ((paramMemberDefinition.isVariable()) && (((MemberDefinition)localObject1).isVariable())) {
/*  438 */           paramEnvironment.error(paramMemberDefinition.getWhere(), "var.multidef", paramMemberDefinition, localObject1);
/*  439 */           return;
/*  440 */         }if ((paramMemberDefinition.isInnerClass()) && (((MemberDefinition)localObject1).isInnerClass()) && 
/*  441 */           (!paramMemberDefinition
/*  441 */           .getInnerClass().isLocal()) && 
/*  442 */           (!((MemberDefinition)localObject1)
/*  442 */           .getInnerClass().isLocal()))
/*      */         {
/*  446 */           paramEnvironment.error(paramMemberDefinition.getWhere(), "inner.class.multidef", paramMemberDefinition);
/*  447 */           return;
/*      */         }
/*      */       }
/*      */     }
/*      */ 
/*  452 */     super.addMember(paramEnvironment, paramMemberDefinition);
/*      */   }
/*      */ 
/*      */   public Environment setupEnv(Environment paramEnvironment)
/*      */   {
/*  466 */     return new Environment(this.toplevelEnv, this);
/*      */   }
/*      */ 
/*      */   public boolean reportDeprecated(Environment paramEnvironment)
/*      */   {
/*  475 */     return false;
/*      */   }
/*      */ 
/*      */   public void noteUsedBy(ClassDefinition paramClassDefinition, long paramLong, Environment paramEnvironment)
/*      */   {
/*  484 */     super.noteUsedBy(paramClassDefinition, paramLong, paramEnvironment);
/*  485 */     Object localObject = this;
/*  486 */     while (((ClassDefinition)localObject).isInnerClass()) {
/*  487 */       localObject = ((ClassDefinition)localObject).getOuterClass();
/*      */     }
/*  489 */     if (((ClassDefinition)localObject).isPublic()) {
/*  490 */       return;
/*      */     }
/*  492 */     while (paramClassDefinition.isInnerClass()) {
/*  493 */       paramClassDefinition = paramClassDefinition.getOuterClass();
/*      */     }
/*  495 */     if (((ClassDefinition)localObject).getSource().equals(paramClassDefinition.getSource())) {
/*  496 */       return;
/*      */     }
/*  498 */     ((SourceClass)localObject).checkSourceFile(paramEnvironment, paramLong);
/*      */   }
/*      */ 
/*      */   public void check(Environment paramEnvironment)
/*      */     throws ClassNotFound
/*      */   {
/*  505 */     paramEnvironment.dtEnter("SourceClass.check: " + getName());
/*  506 */     if (isInsideLocal())
/*      */     {
/*  512 */       paramEnvironment.dtEvent("SourceClass.check: INSIDE LOCAL " + 
/*  513 */         getOuterClass().getName());
/*  514 */       getOuterClass().check(paramEnvironment);
/*      */     } else {
/*  516 */       if (isInnerClass()) {
/*  517 */         paramEnvironment.dtEvent("SourceClass.check: INNER CLASS " + 
/*  518 */           getOuterClass().getName());
/*      */ 
/*  520 */         ((SourceClass)getOuterClass()).maybeCheck(paramEnvironment);
/*      */       }
/*  522 */       Vset localVset = new Vset();
/*  523 */       Context localContext = null;
/*      */ 
/*  525 */       paramEnvironment.dtEvent("SourceClass.check: CHECK INTERNAL " + getName());
/*  526 */       localVset = checkInternal(setupEnv(paramEnvironment), localContext, localVset);
/*      */     }
/*      */ 
/*  529 */     paramEnvironment.dtExit("SourceClass.check: " + getName());
/*      */   }
/*      */ 
/*      */   private void maybeCheck(Environment paramEnvironment) throws ClassNotFound {
/*  533 */     paramEnvironment.dtEvent("SourceClass.maybeCheck: " + getName());
/*      */ 
/*  536 */     ClassDeclaration localClassDeclaration = getClassDeclaration();
/*  537 */     if (localClassDeclaration.getStatus() == 4)
/*      */     {
/*  539 */       localClassDeclaration.setDefinition(this, 5);
/*  540 */       check(paramEnvironment);
/*      */     }
/*      */   }
/*      */ 
/*      */   private Vset checkInternal(Environment paramEnvironment, Context paramContext, Vset paramVset) throws ClassNotFound
/*      */   {
/*  546 */     Identifier localIdentifier1 = getClassDeclaration().getName();
/*  547 */     if (paramEnvironment.verbose()) {
/*  548 */       paramEnvironment.output("[checking class " + localIdentifier1 + "]");
/*      */     }
/*      */ 
/*  553 */     this.classContext = paramContext;
/*      */ 
/*  559 */     basicCheck(Context.newEnvironment(paramEnvironment, paramContext));
/*      */ 
/*  566 */     ClassDeclaration localClassDeclaration = getSuperClass();
/*  567 */     if (localClassDeclaration != null) {
/*  568 */       long l1 = getWhere();
/*  569 */       l1 = IdentifierToken.getWhere(this.superClassId, l1);
/*  570 */       paramEnvironment.resolveExtendsByName(l1, this, localClassDeclaration.getName());
/*      */     }
/*      */     Object localObject;
/*  572 */     for (int i = 0; i < this.interfaces.length; i++) {
/*  573 */       localObject = this.interfaces[i];
/*  574 */       long l2 = getWhere();
/*      */ 
/*  577 */       if ((this.interfaceIds != null) && (this.interfaceIds.length == this.interfaces.length))
/*      */       {
/*  579 */         l2 = IdentifierToken.getWhere(this.interfaceIds[i], l2);
/*      */       }
/*  581 */       paramEnvironment.resolveExtendsByName(l2, this, ((ClassDeclaration)localObject).getName());
/*      */     }
/*      */ 
/*  586 */     if ((!isInnerClass()) && (!isInsideLocal()))
/*      */     {
/*  588 */       Identifier localIdentifier2 = localIdentifier1.getName();
/*      */       try
/*      */       {
/*  591 */         localObject = this.toplevelEnv.getImports();
/*  592 */         localIdentifier3 = ((Imports)localObject).resolve(paramEnvironment, localIdentifier2);
/*  593 */         if (localIdentifier3 != getName())
/*  594 */           paramEnvironment.error(this.where, "class.multidef.import", localIdentifier2, localIdentifier3);
/*      */       }
/*      */       catch (AmbiguousClass localAmbiguousClass) {
/*  597 */         Identifier localIdentifier3 = localAmbiguousClass.name1 != getName() ? localAmbiguousClass.name1 : localAmbiguousClass.name2;
/*  598 */         paramEnvironment.error(this.where, "class.multidef.import", localIdentifier2, localIdentifier3);
/*      */       }
/*      */       catch (ClassNotFound localClassNotFound)
/*      */       {
/*      */       }
/*      */ 
/*  622 */       if (isPublic()) {
/*  623 */         checkSourceFile(paramEnvironment, getWhere());
/*      */       }
/*      */     }
/*      */ 
/*  627 */     paramVset = checkMembers(paramEnvironment, paramContext, paramVset);
/*  628 */     return paramVset;
/*      */   }
/*      */ 
/*      */   public void checkSourceFile(Environment paramEnvironment, long paramLong)
/*      */   {
/*  638 */     if (this.sourceFileChecked) return;
/*  639 */     this.sourceFileChecked = true;
/*      */ 
/*  641 */     String str1 = getName().getName() + ".java";
/*  642 */     String str2 = ((ClassFile)getSource()).getName();
/*  643 */     if (!str2.equals(str1))
/*  644 */       if (isPublic())
/*  645 */         paramEnvironment.error(paramLong, "public.class.file", this, str1);
/*      */       else
/*  647 */         paramEnvironment.error(paramLong, "warn.package.class.file", this, str2, str1);
/*      */   }
/*      */ 
/*      */   public ClassDeclaration getSuperClass(Environment paramEnvironment)
/*      */   {
/*  663 */     paramEnvironment.dtEnter("SourceClass.getSuperClass: " + this);
/*      */ 
/*  669 */     if ((this.superClass == null) && (this.superClassId != null) && (!this.supersChecked)) {
/*  670 */       resolveTypeStructure(paramEnvironment);
/*      */     }
/*      */ 
/*  677 */     paramEnvironment.dtExit("SourceClass.getSuperClass: " + this);
/*  678 */     return this.superClass;
/*      */   }
/*      */ 
/*      */   private void checkSupers(Environment paramEnvironment)
/*      */     throws ClassNotFound
/*      */   {
/*  690 */     this.supersCheckStarted = true;
/*      */ 
/*  692 */     paramEnvironment.dtEnter("SourceClass.checkSupers: " + this);
/*      */     Object localObject;
/*  694 */     if (isInterface()) {
/*  695 */       if (isFinal()) {
/*  696 */         Identifier localIdentifier = getClassDeclaration().getName();
/*  697 */         paramEnvironment.error(getWhere(), "final.intf", localIdentifier);
/*      */       }
/*      */ 
/*      */     }
/*  708 */     else if (getSuperClass(paramEnvironment) != null) {
/*  709 */       long l1 = getWhere();
/*  710 */       l1 = IdentifierToken.getWhere(this.superClassId, l1);
/*      */       try
/*      */       {
/*  713 */         ClassDefinition localClassDefinition1 = getSuperClass().getClassDefinition(paramEnvironment);
/*      */ 
/*  715 */         localClassDefinition1.resolveTypeStructure(paramEnvironment);
/*      */ 
/*  720 */         if (!extendsCanAccess(paramEnvironment, getSuperClass())) {
/*  721 */           paramEnvironment.error(l1, "cant.access.class", getSuperClass());
/*      */ 
/*  723 */           this.superClass = null;
/*  724 */         } else if (localClassDefinition1.isFinal()) {
/*  725 */           paramEnvironment.error(l1, "super.is.final", getSuperClass());
/*      */ 
/*  727 */           this.superClass = null;
/*  728 */         } else if (localClassDefinition1.isInterface()) {
/*  729 */           paramEnvironment.error(l1, "super.is.intf", getSuperClass());
/*  730 */           this.superClass = null;
/*  731 */         } else if (superClassOf(paramEnvironment, getSuperClass())) {
/*  732 */           paramEnvironment.error(l1, "cyclic.super");
/*  733 */           this.superClass = null;
/*      */         } else {
/*  735 */           localClassDefinition1.noteUsedBy(this, l1, paramEnvironment);
/*      */         }
/*  737 */         if (this.superClass == null) {
/*  738 */           localClassDefinition1 = null;
/*      */         }
/*      */         else
/*      */         {
/*  745 */           ClassDefinition localClassDefinition2 = localClassDefinition1;
/*      */           while (true) {
/*  747 */             if (enclosingClassOf(localClassDefinition2))
/*      */             {
/*  750 */               paramEnvironment.error(l1, "super.is.inner");
/*  751 */               this.superClass = null;
/*  752 */               break;
/*      */             }
/*      */ 
/*  759 */             localObject = localClassDefinition2.getSuperClass(paramEnvironment);
/*  760 */             if (localObject == null)
/*      */             {
/*      */               break;
/*      */             }
/*  764 */             localClassDefinition2 = ((ClassDeclaration)localObject).getClassDefinition(paramEnvironment);
/*      */           }
/*      */ 
/*      */         }
/*      */ 
/*      */       }
/*      */       catch (ClassNotFound localClassNotFound1)
/*      */       {
/*      */         try
/*      */         {
/*  777 */           paramEnvironment.resolve(localClassNotFound1.name);
/*      */         } catch (AmbiguousClass localAmbiguousClass1) {
/*  779 */           paramEnvironment.error(l1, "ambig.class", localAmbiguousClass1.name1, localAmbiguousClass1.name2);
/*      */ 
/*  781 */           this.superClass = null;
/*  782 */           break label355;
/*      */         }
/*      */         catch (ClassNotFound localClassNotFound2) {
/*      */         }
/*  786 */         paramEnvironment.error(l1, "super.not.found", localClassNotFound1.name, this);
/*  787 */         this.superClass = null;
/*      */       }
/*      */ 
/*      */     }
/*      */     else
/*      */     {
/*  797 */       label355: if (isAnonymous())
/*      */       {
/*  799 */         throw new CompilerError("anonymous super");
/*  800 */       }if (!getName().equals(idJavaLangObject)) {
/*  801 */         throw new CompilerError("unresolved super");
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/*  808 */     this.supersChecked = true;
/*      */ 
/*  811 */     for (int i = 0; i < this.interfaces.length; i++) {
/*  812 */       ClassDeclaration localClassDeclaration = this.interfaces[i];
/*  813 */       long l2 = getWhere();
/*  814 */       if ((this.interfaceIds != null) && (this.interfaceIds.length == this.interfaces.length))
/*      */       {
/*  816 */         l2 = IdentifierToken.getWhere(this.interfaceIds[i], l2);
/*      */       }
/*      */       try {
/*  819 */         localObject = localClassDeclaration.getClassDefinition(paramEnvironment);
/*      */ 
/*  821 */         ((ClassDefinition)localObject).resolveTypeStructure(paramEnvironment);
/*      */ 
/*  824 */         if (!extendsCanAccess(paramEnvironment, localClassDeclaration)) {
/*  825 */           paramEnvironment.error(l2, "cant.access.class", localClassDeclaration);
/*  826 */         } else if (!localClassDeclaration.getClassDefinition(paramEnvironment).isInterface()) {
/*  827 */           paramEnvironment.error(l2, "not.intf", localClassDeclaration);
/*  828 */         } else if ((isInterface()) && (implementedBy(paramEnvironment, localClassDeclaration))) {
/*  829 */           paramEnvironment.error(l2, "cyclic.intf", localClassDeclaration);
/*      */         } else {
/*  831 */           ((ClassDefinition)localObject).noteUsedBy(this, l2, paramEnvironment);
/*      */ 
/*  833 */           continue;
/*      */         }
/*      */ 
/*      */       }
/*      */       catch (ClassNotFound localClassNotFound3)
/*      */       {
/*      */         try
/*      */         {
/*  844 */           paramEnvironment.resolve(localClassNotFound3.name);
/*      */         } catch (AmbiguousClass localAmbiguousClass2) {
/*  846 */           paramEnvironment.error(l2, "ambig.class", localAmbiguousClass2.name1, localAmbiguousClass2.name2);
/*      */ 
/*  848 */           this.superClass = null;
/*  849 */           break label624;
/*      */         }
/*      */         catch (ClassNotFound localClassNotFound4) {
/*      */         }
/*  853 */         paramEnvironment.error(l2, "intf.not.found", localClassNotFound3.name, this);
/*  854 */         this.superClass = null;
/*      */       }
/*      */ 
/*  859 */       label624: ClassDeclaration[] arrayOfClassDeclaration = new ClassDeclaration[this.interfaces.length - 1];
/*      */ 
/*  861 */       System.arraycopy(this.interfaces, 0, arrayOfClassDeclaration, 0, i);
/*  862 */       System.arraycopy(this.interfaces, i + 1, arrayOfClassDeclaration, i, arrayOfClassDeclaration.length - i);
/*      */ 
/*  864 */       this.interfaces = arrayOfClassDeclaration;
/*  865 */       i--;
/*      */     }
/*  867 */     paramEnvironment.dtExit("SourceClass.checkSupers: " + this);
/*      */   }
/*      */ 
/*      */   private Vset checkMembers(Environment paramEnvironment, Context paramContext, Vset paramVset)
/*      */     throws ClassNotFound
/*      */   {
/*  885 */     if (getError()) {
/*  886 */       return paramVset;
/*      */     }
/*      */ 
/*  896 */     for (Object localObject1 = getFirstMember(); 
/*  897 */       localObject1 != null; localObject1 = ((MemberDefinition)localObject1).getNextMember()) {
/*  898 */       if (((MemberDefinition)localObject1).isInnerClass())
/*      */       {
/*  900 */         localObject2 = (SourceClass)((MemberDefinition)localObject1).getInnerClass();
/*  901 */         if (((SourceClass)localObject2).isMember()) {
/*  902 */           ((SourceClass)localObject2).basicCheck(paramEnvironment);
/*      */         }
/*      */       }
/*      */     }
/*      */ 
/*  907 */     if ((isFinal()) && (isAbstract())) {
/*  908 */       paramEnvironment.error(this.where, "final.abstract", getName().getName());
/*      */     }
/*      */ 
/*  917 */     if ((!isInterface()) && (!isAbstract()) && (mustBeAbstract(paramEnvironment)))
/*      */     {
/*  919 */       this.modifiers |= 1024;
/*      */ 
/*  924 */       localObject1 = getPermanentlyAbstractMethods();
/*  925 */       while (((Iterator)localObject1).hasNext()) {
/*  926 */         localObject2 = (MemberDefinition)((Iterator)localObject1).next();
/*      */ 
/*  930 */         paramEnvironment.error(this.where, "abstract.class.cannot.override", 
/*  931 */           getClassDeclaration(), localObject2, ((MemberDefinition)localObject2)
/*  932 */           .getDefiningClassDeclaration());
/*      */       }
/*      */ 
/*  936 */       localObject1 = getMethods(paramEnvironment);
/*  937 */       while (((Iterator)localObject1).hasNext())
/*      */       {
/*  940 */         localObject2 = (MemberDefinition)((Iterator)localObject1).next();
/*  941 */         if (((MemberDefinition)localObject2).isAbstract()) {
/*  942 */           paramEnvironment.error(this.where, "abstract.class", 
/*  943 */             getClassDeclaration(), localObject2, ((MemberDefinition)localObject2)
/*  944 */             .getDefiningClassDeclaration());
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/*  952 */     localObject1 = new Context(paramContext);
/*  953 */     Object localObject2 = paramVset.copy();
/*  954 */     Vset localVset1 = paramVset.copy();
/*      */ 
/*  962 */     for (Object localObject3 = getFirstMember(); 
/*  963 */       localObject3 != null; localObject3 = ((MemberDefinition)localObject3).getNextMember()) {
/*  964 */       if ((((MemberDefinition)localObject3).isVariable()) && (((MemberDefinition)localObject3).isBlankFinal()))
/*      */       {
/*  967 */         int i = ((Context)localObject1).declareFieldNumber((MemberDefinition)localObject3);
/*  968 */         if (((MemberDefinition)localObject3).isStatic()) {
/*  969 */           localVset1 = localVset1.addVarUnassigned(i);
/*  970 */           localObject2 = ((Vset)localObject2).addVar(i);
/*      */         } else {
/*  972 */           localObject2 = ((Vset)localObject2).addVarUnassigned(i);
/*  973 */           localVset1 = localVset1.addVar(i);
/*      */         }
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/*  979 */     localObject3 = new Context((Context)localObject1, this);
/*  980 */     LocalMember localLocalMember = getThisArgument();
/*  981 */     int j = ((Context)localObject3).declare(paramEnvironment, localLocalMember);
/*  982 */     localObject2 = ((Vset)localObject2).addVar(j);
/*      */ 
/*  986 */     for (MemberDefinition localMemberDefinition = getFirstMember(); 
/*  987 */       localMemberDefinition != null; localMemberDefinition = localMemberDefinition.getNextMember()) {
/*      */       try {
/*  989 */         if ((localMemberDefinition.isVariable()) || (localMemberDefinition.isInitializer()))
/*  990 */           if (localMemberDefinition.isStatic())
/*  991 */             localVset1 = localMemberDefinition.check(paramEnvironment, (Context)localObject1, localVset1);
/*      */           else
/*  993 */             localObject2 = localMemberDefinition.check(paramEnvironment, (Context)localObject3, (Vset)localObject2);
/*      */       }
/*      */       catch (ClassNotFound localClassNotFound1)
/*      */       {
/*  997 */         paramEnvironment.error(localMemberDefinition.getWhere(), "class.not.found", localClassNotFound1.name, this);
/*      */       }
/*      */     }
/*      */ 
/* 1001 */     checkBlankFinals(paramEnvironment, (Context)localObject1, localVset1, true);
/*      */ 
/* 1005 */     for (localMemberDefinition = getFirstMember(); 
/* 1006 */       localMemberDefinition != null; localMemberDefinition = localMemberDefinition.getNextMember()) {
/*      */       try
/*      */       {
/*      */         Vset localVset2;
/* 1008 */         if (localMemberDefinition.isConstructor())
/*      */         {
/* 1012 */           localVset2 = localMemberDefinition.check(paramEnvironment, (Context)localObject1, ((Vset)localObject2).copy());
/*      */ 
/* 1014 */           checkBlankFinals(paramEnvironment, (Context)localObject1, localVset2, false);
/*      */         }
/*      */         else {
/* 1017 */           localVset2 = localMemberDefinition.check(paramEnvironment, paramContext, paramVset.copy());
/*      */         }
/*      */       }
/*      */       catch (ClassNotFound localClassNotFound2) {
/* 1021 */         paramEnvironment.error(localMemberDefinition.getWhere(), "class.not.found", localClassNotFound2.name, this);
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/* 1028 */     getClassDeclaration().setDefinition(this, 5);
/*      */ 
/* 1035 */     for (localMemberDefinition = getFirstMember(); 
/* 1036 */       localMemberDefinition != null; localMemberDefinition = localMemberDefinition.getNextMember()) {
/* 1037 */       if (localMemberDefinition.isInnerClass()) {
/* 1038 */         SourceClass localSourceClass = (SourceClass)localMemberDefinition.getInnerClass();
/* 1039 */         if (!localSourceClass.isInsideLocal()) {
/* 1040 */           localSourceClass.maybeCheck(paramEnvironment);
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/* 1048 */     return paramVset;
/*      */   }
/*      */ 
/*      */   private void checkBlankFinals(Environment paramEnvironment, Context paramContext, Vset paramVset, boolean paramBoolean)
/*      */   {
/* 1055 */     for (int i = 0; i < paramContext.getVarNumber(); i++)
/* 1056 */       if (!paramVset.testVar(i)) {
/* 1057 */         MemberDefinition localMemberDefinition = paramContext.getElement(i);
/* 1058 */         if ((localMemberDefinition != null) && (localMemberDefinition.isBlankFinal()) && 
/* 1059 */           (localMemberDefinition
/* 1059 */           .isStatic() == paramBoolean) && 
/* 1060 */           (localMemberDefinition
/* 1060 */           .getClassDefinition() == this))
/* 1061 */           paramEnvironment.error(localMemberDefinition.getWhere(), "final.var.not.initialized", localMemberDefinition
/* 1062 */             .getName());
/*      */       }
/*      */   }
/*      */ 
/*      */   protected void basicCheck(Environment paramEnvironment)
/*      */     throws ClassNotFound
/*      */   {
/* 1077 */     paramEnvironment.dtEnter("SourceClass.basicCheck: " + getName());
/*      */ 
/* 1079 */     super.basicCheck(paramEnvironment);
/*      */ 
/* 1081 */     if ((this.basicChecking) || (this.basicCheckDone)) {
/* 1082 */       paramEnvironment.dtExit("SourceClass.basicCheck: OK " + getName());
/* 1083 */       return;
/*      */     }
/*      */ 
/* 1086 */     paramEnvironment.dtEvent("SourceClass.basicCheck: CHECKING " + getName());
/*      */ 
/* 1088 */     this.basicChecking = true;
/*      */ 
/* 1090 */     paramEnvironment = setupEnv(paramEnvironment);
/*      */ 
/* 1092 */     Imports localImports = paramEnvironment.getImports();
/* 1093 */     if (localImports != null) {
/* 1094 */       localImports.resolve(paramEnvironment);
/*      */     }
/*      */ 
/* 1097 */     resolveTypeStructure(paramEnvironment);
/*      */ 
/* 1106 */     if (!isInterface())
/*      */     {
/* 1114 */       if (!hasConstructor()) {
/* 1115 */         CompoundStatement localCompoundStatement = new CompoundStatement(getWhere(), new Statement[0]);
/* 1116 */         Type localType = Type.tMethod(Type.tVoid);
/*      */ 
/* 1136 */         int i = getModifiers() & (
/* 1136 */           isInnerClass() ? 5 : 1);
/* 1137 */         paramEnvironment.makeMemberDefinition(paramEnvironment, getWhere(), this, null, i, localType, idInit, null, null, localCompoundStatement);
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/* 1146 */     if (doInheritanceChecks)
/*      */     {
/* 1150 */       collectInheritedMethods(paramEnvironment);
/*      */     }
/*      */ 
/* 1153 */     this.basicChecking = false;
/* 1154 */     this.basicCheckDone = true;
/* 1155 */     paramEnvironment.dtExit("SourceClass.basicCheck: " + getName());
/*      */   }
/*      */ 
/*      */   protected void addMirandaMethods(Environment paramEnvironment, Iterator paramIterator)
/*      */   {
/* 1168 */     while (paramIterator.hasNext())
/*      */     {
/* 1170 */       MemberDefinition localMemberDefinition = (MemberDefinition)paramIterator
/* 1170 */         .next();
/*      */ 
/* 1172 */       addMember(localMemberDefinition);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void resolveTypeStructure(Environment paramEnvironment)
/*      */   {
/* 1201 */     paramEnvironment.dtEnter("SourceClass.resolveTypeStructure: " + getName());
/*      */ 
/* 1205 */     ClassDefinition localClassDefinition = getOuterClass();
/* 1206 */     if ((localClassDefinition != null) && ((localClassDefinition instanceof SourceClass)) && (!((SourceClass)localClassDefinition).resolved))
/*      */     {
/* 1209 */       ((SourceClass)localClassDefinition).resolveTypeStructure(paramEnvironment);
/*      */     }
/*      */ 
/* 1215 */     if ((this.resolved) || (this.resolving))
/*      */     {
/* 1217 */       paramEnvironment.dtExit("SourceClass.resolveTypeStructure: OK " + getName());
/* 1218 */       return;
/*      */     }
/*      */ 
/* 1225 */     this.resolving = true;
/*      */ 
/* 1228 */     paramEnvironment.dtEvent("SourceClass.resolveTypeStructure: RESOLVING " + getName());
/*      */ 
/* 1230 */     paramEnvironment = setupEnv(paramEnvironment);
/*      */ 
/* 1234 */     resolveSupers(paramEnvironment);
/*      */     try
/*      */     {
/* 1244 */       checkSupers(paramEnvironment);
/*      */     }
/*      */     catch (ClassNotFound localClassNotFound) {
/* 1247 */       paramEnvironment.error(this.where, "class.not.found", localClassNotFound.name, this);
/*      */     }
/*      */ 
/* 1251 */     for (MemberDefinition localMemberDefinition1 = getFirstMember(); localMemberDefinition1 != null; localMemberDefinition1 = localMemberDefinition1.getNextMember()) {
/* 1252 */       if ((localMemberDefinition1 instanceof SourceMember)) {
/* 1253 */         ((SourceMember)localMemberDefinition1).resolveTypeStructure(paramEnvironment);
/*      */       }
/*      */     }
/* 1256 */     this.resolving = false;
/*      */ 
/* 1269 */     this.resolved = true;
/*      */     MemberDefinition localMemberDefinition2;
/* 1273 */     for (localMemberDefinition1 = getFirstMember(); localMemberDefinition1 != null; localMemberDefinition1 = localMemberDefinition1.getNextMember()) {
/* 1274 */       if ((!localMemberDefinition1.isInitializer()) && 
/* 1275 */         (localMemberDefinition1.isMethod())) {
/* 1276 */         for (localMemberDefinition2 = localMemberDefinition1; (localMemberDefinition2 = localMemberDefinition2.getNextMatch()) != null; ) {
/* 1277 */           if (localMemberDefinition2.isMethod()) {
/* 1278 */             if (localMemberDefinition1.getType().equals(localMemberDefinition2.getType())) {
/* 1279 */               paramEnvironment.error(localMemberDefinition1.getWhere(), "meth.multidef", localMemberDefinition1);
/*      */             }
/* 1282 */             else if (localMemberDefinition1.getType().equalArguments(localMemberDefinition2.getType())) {
/* 1283 */               paramEnvironment.error(localMemberDefinition1.getWhere(), "meth.redef.rettype", localMemberDefinition1, localMemberDefinition2);
/*      */             }
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/* 1289 */     paramEnvironment.dtExit("SourceClass.resolveTypeStructure: " + getName());
/*      */   }
/*      */ 
/*      */   protected void resolveSupers(Environment paramEnvironment)
/*      */   {
/* 1294 */     paramEnvironment.dtEnter("SourceClass.resolveSupers: " + this);
/*      */ 
/* 1296 */     if ((this.superClassId != null) && (this.superClass == null)) {
/* 1297 */       this.superClass = resolveSuper(paramEnvironment, this.superClassId);
/*      */ 
/* 1301 */       if ((this.superClass == getClassDeclaration()) && 
/* 1302 */         (getName().equals(idJavaLangObject))) {
/* 1303 */         this.superClass = null;
/* 1304 */         this.superClassId = null;
/*      */       }
/*      */     }
/*      */ 
/* 1308 */     if ((this.interfaceIds != null) && (this.interfaces == null)) {
/* 1309 */       this.interfaces = new ClassDeclaration[this.interfaceIds.length];
/* 1310 */       for (int i = 0; i < this.interfaces.length; i++) {
/* 1311 */         this.interfaces[i] = resolveSuper(paramEnvironment, this.interfaceIds[i]);
/* 1312 */         for (int j = 0; j < i; j++) {
/* 1313 */           if (this.interfaces[i] == this.interfaces[j]) {
/* 1314 */             Identifier localIdentifier = this.interfaceIds[i].getName();
/* 1315 */             long l = this.interfaceIds[j].getWhere();
/* 1316 */             paramEnvironment.error(l, "intf.repeated", localIdentifier);
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/*      */ 
/* 1322 */     paramEnvironment.dtExit("SourceClass.resolveSupers: " + this);
/*      */   }
/*      */ 
/*      */   private ClassDeclaration resolveSuper(Environment paramEnvironment, IdentifierToken paramIdentifierToken) {
/* 1326 */     Identifier localIdentifier = paramIdentifierToken.getName();
/*      */ 
/* 1328 */     paramEnvironment.dtEnter("SourceClass.resolveSuper: " + localIdentifier);
/* 1329 */     if (isInnerClass())
/* 1330 */       localIdentifier = this.outerClass.resolveName(paramEnvironment, localIdentifier);
/*      */     else
/* 1332 */       localIdentifier = paramEnvironment.resolveName(localIdentifier);
/* 1333 */     ClassDeclaration localClassDeclaration = paramEnvironment.getClassDeclaration(localIdentifier);
/*      */ 
/* 1336 */     paramEnvironment.dtExit("SourceClass.resolveSuper: " + localIdentifier);
/* 1337 */     return localClassDeclaration;
/*      */   }
/*      */ 
/*      */   public Vset checkLocalClass(Environment paramEnvironment, Context paramContext, Vset paramVset, ClassDefinition paramClassDefinition, Expression[] paramArrayOfExpression, Type[] paramArrayOfType)
/*      */     throws ClassNotFound
/*      */   {
/* 1351 */     paramEnvironment = setupEnv(paramEnvironment);
/*      */ 
/* 1353 */     if ((paramClassDefinition != null) != isAnonymous()) {
/* 1354 */       throw new CompilerError("resolveAnonymousStructure");
/*      */     }
/* 1356 */     if (isAnonymous()) {
/* 1357 */       resolveAnonymousStructure(paramEnvironment, paramClassDefinition, paramArrayOfExpression, paramArrayOfType);
/*      */     }
/*      */ 
/* 1361 */     paramVset = checkInternal(paramEnvironment, paramContext, paramVset);
/*      */ 
/* 1366 */     return paramVset;
/*      */   }
/*      */ 
/*      */   public void inlineLocalClass(Environment paramEnvironment)
/*      */   {
/* 1374 */     for (MemberDefinition localMemberDefinition = getFirstMember(); localMemberDefinition != null; localMemberDefinition = localMemberDefinition.getNextMember())
/* 1375 */       if (((!localMemberDefinition.isVariable()) && (!localMemberDefinition.isInitializer())) || (localMemberDefinition.isStatic()))
/*      */       {
/*      */         try
/*      */         {
/* 1379 */           ((SourceMember)localMemberDefinition).inline(paramEnvironment);
/*      */         } catch (ClassNotFound localClassNotFound) {
/* 1381 */           paramEnvironment.error(localMemberDefinition.getWhere(), "class.not.found", localClassNotFound.name, this);
/*      */         }
/*      */       }
/* 1384 */     if ((getReferencesFrozen() != null) && (!this.inlinedLocalClass)) {
/* 1385 */       this.inlinedLocalClass = true;
/*      */ 
/* 1388 */       for (localMemberDefinition = getFirstMember(); localMemberDefinition != null; localMemberDefinition = localMemberDefinition.getNextMember())
/* 1389 */         if (localMemberDefinition.isConstructor())
/*      */         {
/* 1391 */           ((SourceMember)localMemberDefinition).addUplevelArguments();
/*      */         }
/*      */     }
/*      */   }
/*      */ 
/*      */   public Vset checkInsideClass(Environment paramEnvironment, Context paramContext, Vset paramVset)
/*      */     throws ClassNotFound
/*      */   {
/* 1403 */     if ((!isInsideLocal()) || (isLocal())) {
/* 1404 */       throw new CompilerError("checkInsideClass");
/*      */     }
/* 1406 */     return checkInternal(paramEnvironment, paramContext, paramVset);
/*      */   }
/*      */ 
/*      */   private void resolveAnonymousStructure(Environment paramEnvironment, ClassDefinition paramClassDefinition, Expression[] paramArrayOfExpression, Type[] paramArrayOfType)
/*      */     throws ClassNotFound
/*      */   {
/* 1418 */     paramEnvironment.dtEvent("SourceClass.resolveAnonymousStructure: " + this + ", super " + paramClassDefinition);
/*      */ 
/* 1432 */     if (paramClassDefinition.isInterface())
/*      */     {
/* 1434 */       int i = this.interfaces == null ? 0 : this.interfaces.length;
/* 1435 */       localObject = new ClassDeclaration[1 + i];
/* 1436 */       if (i > 0) {
/* 1437 */         System.arraycopy(this.interfaces, 0, localObject, 1, i);
/* 1438 */         if ((this.interfaceIds != null) && (this.interfaceIds.length == i)) {
/* 1439 */           IdentifierToken[] arrayOfIdentifierToken = new IdentifierToken[1 + i];
/* 1440 */           System.arraycopy(this.interfaceIds, 0, arrayOfIdentifierToken, 1, i);
/* 1441 */           arrayOfIdentifierToken[0] = new IdentifierToken(paramClassDefinition.getName());
/*      */         }
/*      */       }
/* 1444 */       localObject[0] = paramClassDefinition.getClassDeclaration();
/* 1445 */       this.interfaces = ((ClassDeclaration[])localObject);
/*      */ 
/* 1447 */       paramClassDefinition = this.toplevelEnv.getClassDefinition(idJavaLangObject);
/*      */     }
/* 1449 */     this.superClass = paramClassDefinition.getClassDeclaration();
/*      */ 
/* 1451 */     if (hasConstructor()) {
/* 1452 */       throw new CompilerError("anonymous constructor");
/*      */     }
/*      */ 
/* 1456 */     Type localType = Type.tMethod(Type.tVoid, paramArrayOfType);
/* 1457 */     Object localObject = new IdentifierToken[paramArrayOfType.length];
/* 1458 */     for (int j = 0; j < localObject.length; j++) {
/* 1459 */       localObject[j] = new IdentifierToken(paramArrayOfExpression[j].getWhere(), 
/* 1460 */         Identifier.lookup("$" + j));
/*      */     }
/*      */ 
/* 1462 */     j = (paramClassDefinition.isTopLevel()) || (paramClassDefinition.isLocal()) ? 0 : 1;
/* 1463 */     Expression[] arrayOfExpression = new Expression[-j + paramArrayOfExpression.length];
/* 1464 */     for (int k = j; k < paramArrayOfExpression.length; k++) {
/* 1465 */       arrayOfExpression[(-j + k)] = new IdentifierExpression(localObject[k]);
/*      */     }
/* 1467 */     long l = getWhere();
/*      */     SuperExpression localSuperExpression;
/* 1469 */     if (j == 0)
/* 1470 */       localSuperExpression = new SuperExpression(l);
/*      */     else {
/* 1472 */       localSuperExpression = new SuperExpression(l, new IdentifierExpression(localObject[0]));
/*      */     }
/*      */ 
/* 1475 */     MethodExpression localMethodExpression = new MethodExpression(l, localSuperExpression, idInit, arrayOfExpression);
/*      */ 
/* 1478 */     Statement[] arrayOfStatement = { new ExpressionStatement(l, localMethodExpression) };
/* 1479 */     CompoundStatement localCompoundStatement = new CompoundStatement(l, arrayOfStatement);
/* 1480 */     int m = 524288;
/* 1481 */     paramEnvironment.makeMemberDefinition(paramEnvironment, l, this, null, m, localType, idInit, (IdentifierToken[])localObject, null, localCompoundStatement);
/*      */   }
/*      */ 
/*      */   static String classModifierString(int paramInt)
/*      */   {
/* 1503 */     String str = "";
/* 1504 */     for (int i = 0; i < classModifierBits.length; i++) {
/* 1505 */       if ((paramInt & classModifierBits[i]) != 0) {
/* 1506 */         str = str + " " + classModifierNames[i];
/* 1507 */         paramInt &= (classModifierBits[i] ^ 0xFFFFFFFF);
/*      */       }
/*      */     }
/* 1510 */     if (paramInt != 0) {
/* 1511 */       str = str + " ILLEGAL:" + Integer.toHexString(paramInt);
/*      */     }
/* 1513 */     return str;
/*      */   }
/*      */ 
/*      */   public MemberDefinition getAccessMember(Environment paramEnvironment, Context paramContext, MemberDefinition paramMemberDefinition, boolean paramBoolean)
/*      */   {
/* 1522 */     return getAccessMember(paramEnvironment, paramContext, paramMemberDefinition, false, paramBoolean);
/*      */   }
/*      */ 
/*      */   public MemberDefinition getUpdateMember(Environment paramEnvironment, Context paramContext, MemberDefinition paramMemberDefinition, boolean paramBoolean)
/*      */   {
/* 1527 */     if (!paramMemberDefinition.isVariable()) {
/* 1528 */       throw new CompilerError("method");
/*      */     }
/* 1530 */     return getAccessMember(paramEnvironment, paramContext, paramMemberDefinition, true, paramBoolean);
/*      */   }
/*      */ 
/*      */   private MemberDefinition getAccessMember(Environment paramEnvironment, Context paramContext, MemberDefinition paramMemberDefinition, boolean paramBoolean1, boolean paramBoolean2)
/*      */   {
/* 1545 */     boolean bool1 = paramMemberDefinition.isStatic();
/* 1546 */     boolean bool2 = paramMemberDefinition.isMethod();
/*      */ 
/* 1554 */     for (MemberDefinition localMemberDefinition = getFirstMember(); localMemberDefinition != null; localMemberDefinition = localMemberDefinition.getNextMember())
/* 1555 */       if (localMemberDefinition.getAccessMethodTarget() == paramMemberDefinition) {
/* 1556 */         if ((bool2) && (localMemberDefinition.isSuperAccessMethod() == paramBoolean2))
/*      */         {
/*      */           break;
/*      */         }
/*      */ 
/* 1561 */         int i = localMemberDefinition.getType().getArgumentTypes().length;
/*      */ 
/* 1565 */         if (i == (bool1 ? 0 : 1))
/*      */           break;
/*      */       }
/*      */     Object localObject1;
/* 1571 */     if (localMemberDefinition != null) {
/* 1572 */       if (!paramBoolean1) {
/* 1573 */         return localMemberDefinition;
/*      */       }
/* 1575 */       localObject1 = localMemberDefinition.getAccessUpdateMember();
/* 1576 */       if (localObject1 != null) {
/* 1577 */         return localObject1;
/*      */       }
/*      */     }
/* 1580 */     else if (paramBoolean1)
/*      */     {
/* 1582 */       localMemberDefinition = getAccessMember(paramEnvironment, paramContext, paramMemberDefinition, false, paramBoolean2);
/*      */     }
/*      */ 
/* 1588 */     Type localType = null;
/*      */     Object localObject4;
/*      */     Object localObject5;
/*      */     int m;
/*      */     Object localObject6;
/* 1590 */     if (paramMemberDefinition.isConstructor())
/*      */     {
/* 1594 */       localObject1 = idInit;
/*      */ 
/* 1596 */       SourceClass localSourceClass = (SourceClass)getTopClass();
/* 1597 */       localType = localSourceClass.dummyArgumentType;
/* 1598 */       if (localType == null)
/*      */       {
/* 1600 */         localObject3 = new IdentifierToken(0L, idJavaLangObject);
/*      */ 
/* 1602 */         localObject4 = new IdentifierToken[0];
/* 1603 */         localObject5 = new IdentifierToken(0L, idNull);
/* 1604 */         m = 589832;
/*      */ 
/* 1607 */         if (localSourceClass.isInterface()) {
/* 1608 */           m |= 1;
/*      */         }
/*      */ 
/* 1611 */         localObject6 = this.toplevelEnv
/* 1611 */           .makeClassDefinition(this.toplevelEnv, 0L, (IdentifierToken)localObject5, null, m, (IdentifierToken)localObject3, (IdentifierToken[])localObject4, localSourceClass);
/*      */ 
/* 1618 */         ((ClassDefinition)localObject6).getClassDeclaration().setDefinition((ClassDefinition)localObject6, 4);
/* 1619 */         Expression[] arrayOfExpression1 = new Expression[0];
/* 1620 */         localObject8 = new Type[0];
/*      */         try
/*      */         {
/* 1623 */           ClassDefinition localClassDefinition = this.toplevelEnv
/* 1623 */             .getClassDefinition(idJavaLangObject);
/*      */ 
/* 1624 */           ((ClassDefinition)localObject6).checkLocalClass(this.toplevelEnv, null, new Vset(), localClassDefinition, arrayOfExpression1, (Type[])localObject8);
/*      */         }
/*      */         catch (ClassNotFound localClassNotFound1) {
/*      */         }
/* 1628 */         localType = ((ClassDefinition)localObject6).getType();
/* 1629 */         localSourceClass.dummyArgumentType = localType;
/*      */       }
/*      */     }
/*      */     else
/*      */     {
/* 1634 */       for (int j = 0; ; j++) {
/* 1635 */         localObject1 = Identifier.lookup("access$" + j);
/* 1636 */         if (getFirstMatch((Identifier)localObject1) == null)
/*      */         {
/*      */           break;
/*      */         }
/*      */       }
/*      */     }
/*      */ 
/* 1643 */     Object localObject3 = paramMemberDefinition.getType();
/*      */     Object localObject2;
/* 1645 */     if (bool1) {
/* 1646 */       if (!bool2) {
/* 1647 */         if (!paramBoolean1) {
/* 1648 */           localObject4 = new Type[0];
/* 1649 */           localObject2 = localObject4;
/* 1650 */           localObject3 = Type.tMethod((Type)localObject3);
/*      */         } else {
/* 1652 */           localObject4 = new Type[] { localObject3 };
/* 1653 */           localObject2 = localObject4;
/* 1654 */           localObject3 = Type.tMethod(Type.tVoid, (Type[])localObject2);
/*      */         }
/*      */       }
/*      */       else
/*      */       {
/* 1659 */         localObject2 = ((Type)localObject3).getArgumentTypes();
/*      */       }
/*      */ 
/*      */     }
/*      */     else
/*      */     {
/* 1666 */       localObject4 = getType();
/* 1667 */       if (!bool2) {
/* 1668 */         if (!paramBoolean1) {
/* 1669 */           localObject5 = new Type[] { localObject4 };
/* 1670 */           localObject2 = localObject5;
/* 1671 */           localObject3 = Type.tMethod((Type)localObject3, (Type[])localObject2);
/*      */         } else {
/* 1673 */           localObject5 = new Type[] { localObject4, localObject3 };
/* 1674 */           localObject2 = localObject5;
/* 1675 */           localObject3 = Type.tMethod(Type.tVoid, (Type[])localObject2);
/*      */         }
/*      */       }
/*      */       else {
/* 1679 */         localObject5 = ((Type)localObject3).getArgumentTypes();
/* 1680 */         m = localObject5.length;
/* 1681 */         if (paramMemberDefinition.isConstructor())
/*      */         {
/* 1685 */           localObject6 = ((SourceMember)paramMemberDefinition)
/* 1685 */             .getOuterThisArg();
/* 1686 */           if (localObject6 != null)
/*      */           {
/* 1690 */             if (localObject5[0] != ((MemberDefinition)localObject6).getType()) {
/* 1691 */               throw new CompilerError("misplaced outer this");
/*      */             }
/*      */ 
/* 1695 */             localObject2 = new Type[m];
/* 1696 */             localObject2[0] = localType;
/* 1697 */             for (i1 = 1; i1 < m; i1++)
/* 1698 */               localObject2[i1] = localObject5[i1];
/*      */           }
/*      */           else
/*      */           {
/* 1702 */             localObject2 = new Type[m + 1];
/* 1703 */             localObject2[0] = localType;
/* 1704 */             for (i1 = 0; i1 < m; i1++) {
/* 1705 */               localObject2[(i1 + 1)] = localObject5[i1];
/*      */             }
/*      */           }
/*      */         }
/*      */         else
/*      */         {
/* 1711 */           localObject2 = new Type[m + 1];
/* 1712 */           localObject2[0] = localObject4;
/* 1713 */           for (int n = 0; n < m; n++) {
/* 1714 */             localObject2[(n + 1)] = localObject5[n];
/*      */           }
/*      */         }
/* 1717 */         localObject3 = Type.tMethod(((Type)localObject3).getReturnType(), (Type[])localObject2);
/*      */       }
/*      */     }
/*      */ 
/* 1721 */     int k = localObject2.length;
/* 1722 */     long l = paramMemberDefinition.getWhere();
/* 1723 */     IdentifierToken[] arrayOfIdentifierToken = new IdentifierToken[k];
/* 1724 */     for (int i1 = 0; i1 < k; i1++) {
/* 1725 */       arrayOfIdentifierToken[i1] = new IdentifierToken(l, Identifier.lookup("$" + i1));
/*      */     }
/*      */ 
/* 1728 */     Object localObject7 = null;
/* 1729 */     Object localObject8 = null;
/* 1730 */     Expression[] arrayOfExpression2 = null;
/*      */     int i2;
/* 1732 */     if (bool1) {
/* 1733 */       arrayOfExpression2 = new Expression[k];
/* 1734 */       for (i2 = 0; i2 < k; i2++)
/* 1735 */         arrayOfExpression2[i2] = new IdentifierExpression(arrayOfIdentifierToken[i2]);
/*      */     }
/*      */     else {
/* 1738 */       if (paramMemberDefinition.isConstructor())
/*      */       {
/* 1741 */         localObject8 = new ThisExpression(l);
/*      */ 
/* 1744 */         arrayOfExpression2 = new Expression[k - 1];
/* 1745 */         for (i2 = 1; i2 < k; i2++) {
/* 1746 */           arrayOfExpression2[(i2 - 1)] = new IdentifierExpression(arrayOfIdentifierToken[i2]);
/*      */         }
/*      */       }
/*      */       else
/*      */       {
/* 1751 */         localObject8 = new IdentifierExpression(arrayOfIdentifierToken[0]);
/*      */ 
/* 1753 */         arrayOfExpression2 = new Expression[k - 1];
/* 1754 */         for (i2 = 1; i2 < k; i2++) {
/* 1755 */           arrayOfExpression2[(i2 - 1)] = new IdentifierExpression(arrayOfIdentifierToken[i2]);
/*      */         }
/*      */       }
/* 1758 */       localObject7 = localObject8;
/*      */     }
/*      */ 
/* 1761 */     if (!bool2) {
/* 1762 */       localObject7 = new FieldExpression(l, (Expression)localObject7, paramMemberDefinition);
/* 1763 */       if (paramBoolean1)
/* 1764 */         localObject7 = new AssignExpression(l, (Expression)localObject7, arrayOfExpression2[0]);
/*      */     }
/*      */     else
/*      */     {
/* 1768 */       localObject7 = new MethodExpression(l, (Expression)localObject7, paramMemberDefinition, arrayOfExpression2, paramBoolean2);
/*      */     }
/*      */ 
/* 1772 */     if (((Type)localObject3).getReturnType().isType(11))
/* 1773 */       localObject9 = new ExpressionStatement(l, (Expression)localObject7);
/*      */     else {
/* 1775 */       localObject9 = new ReturnStatement(l, (Expression)localObject7);
/*      */     }
/* 1777 */     Statement[] arrayOfStatement = { localObject9 };
/* 1778 */     Object localObject9 = new CompoundStatement(l, arrayOfStatement);
/*      */ 
/* 1784 */     int i3 = 524288;
/* 1785 */     if (!paramMemberDefinition.isConstructor()) {
/* 1786 */       i3 |= 8;
/*      */     }
/*      */ 
/* 1805 */     SourceMember localSourceMember = (SourceMember)paramEnvironment
/* 1805 */       .makeMemberDefinition(paramEnvironment, l, this, null, i3, (Type)localObject3, (Identifier)localObject1, arrayOfIdentifierToken, paramMemberDefinition
/* 1807 */       .getExceptionIds(), localObject9);
/*      */ 
/* 1811 */     localSourceMember.setExceptions(paramMemberDefinition.getExceptions(paramEnvironment));
/*      */ 
/* 1813 */     localSourceMember.setAccessMethodTarget(paramMemberDefinition);
/* 1814 */     if (paramBoolean1) {
/* 1815 */       localMemberDefinition.setAccessUpdateMember(localSourceMember);
/*      */     }
/* 1817 */     localSourceMember.setIsSuperAccessMethod(paramBoolean2);
/*      */ 
/* 1843 */     Context localContext = localSourceMember.getClassDefinition().getClassContext();
/* 1844 */     if (localContext != null) {
/*      */       try
/*      */       {
/* 1847 */         localSourceMember.check(paramEnvironment, localContext, new Vset());
/*      */       } catch (ClassNotFound localClassNotFound2) {
/* 1849 */         paramEnvironment.error(l, "class.not.found", localClassNotFound2.name, this);
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/* 1858 */     return localSourceMember;
/*      */   }
/*      */ 
/*      */   SourceClass findLookupContext()
/*      */   {
/*      */     SourceClass localSourceClass;
/* 1868 */     for (MemberDefinition localMemberDefinition = getFirstMember(); 
/* 1869 */       localMemberDefinition != null; 
/* 1870 */       localMemberDefinition = localMemberDefinition.getNextMember()) {
/* 1871 */       if (localMemberDefinition.isInnerClass()) {
/* 1872 */         localSourceClass = (SourceClass)localMemberDefinition.getInnerClass();
/* 1873 */         if (!localSourceClass.isInterface()) {
/* 1874 */           return localSourceClass;
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/* 1882 */     for (localMemberDefinition = getFirstMember(); 
/* 1883 */       localMemberDefinition != null; 
/* 1884 */       localMemberDefinition = localMemberDefinition.getNextMember()) {
/* 1885 */       if (localMemberDefinition.isInnerClass())
/*      */       {
/* 1887 */         localSourceClass = ((SourceClass)localMemberDefinition
/* 1887 */           .getInnerClass()).findLookupContext();
/* 1888 */         if (localSourceClass != null) {
/* 1889 */           return localSourceClass;
/*      */         }
/*      */       }
/*      */     }
/*      */ 
/* 1894 */     return null;
/*      */   }
/*      */ 
/*      */   public MemberDefinition getClassLiteralLookup(long paramLong)
/*      */   {
/* 1905 */     if (this.lookup != null) {
/* 1906 */       return this.lookup;
/*      */     }
/*      */ 
/* 1913 */     if (this.outerClass != null) {
/* 1914 */       this.lookup = this.outerClass.getClassLiteralLookup(paramLong);
/* 1915 */       return this.lookup;
/*      */     }
/*      */ 
/* 1920 */     SourceClass localSourceClass = this;
/* 1921 */     int i = 0;
/*      */ 
/* 1923 */     if (isInterface())
/*      */     {
/* 1926 */       localSourceClass = findLookupContext();
/* 1927 */       if (localSourceClass == null)
/*      */       {
/* 1934 */         i = 1;
/* 1935 */         localObject1 = new IdentifierToken(paramLong, idJavaLangObject);
/*      */ 
/* 1937 */         localObject2 = new IdentifierToken[0];
/* 1938 */         IdentifierToken localIdentifierToken1 = new IdentifierToken(paramLong, idNull);
/* 1939 */         int j = 589833;
/*      */ 
/* 1941 */         localSourceClass = (SourceClass)this.toplevelEnv
/* 1941 */           .makeClassDefinition(this.toplevelEnv, paramLong, localIdentifierToken1, null, j, (IdentifierToken)localObject1, (IdentifierToken[])localObject2, this);
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/* 1949 */     Object localObject1 = Identifier.lookup("class$");
/* 1950 */     Object localObject2 = { Type.tString };
/*      */ 
/* 1979 */     long l = localSourceClass.getWhere();
/* 1980 */     IdentifierToken localIdentifierToken2 = new IdentifierToken(l, (Identifier)localObject1);
/* 1981 */     Object localObject3 = new IdentifierExpression(localIdentifierToken2);
/* 1982 */     Expression[] arrayOfExpression1 = { localObject3 };
/* 1983 */     Identifier localIdentifier1 = Identifier.lookup("forName");
/* 1984 */     localObject3 = new MethodExpression(l, new TypeExpression(l, Type.tClassDesc), localIdentifier1, arrayOfExpression1);
/*      */ 
/* 1986 */     Object localObject4 = new ReturnStatement(l, (Expression)localObject3);
/*      */ 
/* 1989 */     Identifier localIdentifier2 = Identifier.lookup("java.lang.ClassNotFoundException");
/*      */ 
/* 1991 */     Identifier localIdentifier3 = Identifier.lookup("java.lang.NoClassDefFoundError");
/*      */ 
/* 1992 */     Type localType1 = Type.tClass(localIdentifier2);
/* 1993 */     Type localType2 = Type.tClass(localIdentifier3);
/* 1994 */     Identifier localIdentifier4 = Identifier.lookup("getMessage");
/* 1995 */     localObject3 = new IdentifierExpression(l, localIdentifier1);
/* 1996 */     localObject3 = new MethodExpression(l, (Expression)localObject3, localIdentifier4, new Expression[0]);
/* 1997 */     Expression[] arrayOfExpression2 = { localObject3 };
/* 1998 */     localObject3 = new NewInstanceExpression(l, new TypeExpression(l, localType2), arrayOfExpression2);
/* 1999 */     CatchStatement localCatchStatement = new CatchStatement(l, new TypeExpression(l, localType1), new IdentifierToken(localIdentifier1), new ThrowStatement(l, (Expression)localObject3));
/*      */ 
/* 2002 */     Statement[] arrayOfStatement = { localCatchStatement };
/* 2003 */     localObject4 = new TryStatement(l, (Statement)localObject4, arrayOfStatement);
/*      */ 
/* 2005 */     Type localType3 = Type.tMethod(Type.tClassDesc, (Type[])localObject2);
/* 2006 */     IdentifierToken[] arrayOfIdentifierToken = { localIdentifierToken2 };
/*      */ 
/* 2011 */     this.lookup = this.toplevelEnv.makeMemberDefinition(this.toplevelEnv, l, localSourceClass, null, 524296, localType3, (Identifier)localObject1, arrayOfIdentifierToken, null, localObject4);
/*      */ 
/* 2019 */     if (i != 0) {
/* 2020 */       if (localSourceClass.getClassDeclaration().getStatus() == 5) {
/* 2021 */         throw new CompilerError("duplicate check");
/*      */       }
/* 2023 */       localSourceClass.getClassDeclaration().setDefinition(localSourceClass, 4);
/* 2024 */       Expression[] arrayOfExpression3 = new Expression[0];
/* 2025 */       Type[] arrayOfType = new Type[0];
/*      */       try
/*      */       {
/* 2028 */         ClassDefinition localClassDefinition = this.toplevelEnv
/* 2028 */           .getClassDefinition(idJavaLangObject);
/*      */ 
/* 2029 */         localSourceClass.checkLocalClass(this.toplevelEnv, null, new Vset(), localClassDefinition, arrayOfExpression3, arrayOfType);
/*      */       }
/*      */       catch (ClassNotFound localClassNotFound) {
/*      */       }
/*      */     }
/* 2034 */     return this.lookup;
/*      */   }
/*      */ 
/*      */   public void compile(OutputStream paramOutputStream)
/*      */     throws InterruptedException, IOException
/*      */   {
/* 2050 */     Environment localEnvironment = this.toplevelEnv;
/* 2051 */     synchronized (active) {
/* 2052 */       while (active.contains(getName())) {
/* 2053 */         active.wait();
/*      */       }
/* 2055 */       active.addElement(getName());
/*      */     }
/*      */     try
/*      */     {
/* 2059 */       compileClass(localEnvironment, paramOutputStream);
/*      */     } catch (ClassNotFound localClassNotFound) {
/* 2061 */       throw new CompilerError(localClassNotFound);
/*      */     } finally {
/* 2063 */       synchronized (active) {
/* 2064 */         active.removeElement(getName());
/* 2065 */         active.notifyAll();
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   private static void assertModifiers(int paramInt1, int paramInt2)
/*      */   {
/* 2078 */     if ((paramInt1 & paramInt2) != paramInt2)
/* 2079 */       throw new CompilerError("illegal class modifiers");
/*      */   }
/*      */ 
/*      */   protected void compileClass(Environment paramEnvironment, OutputStream paramOutputStream)
/*      */     throws IOException, ClassNotFound
/*      */   {
/* 2085 */     Vector localVector1 = new Vector();
/* 2086 */     Vector localVector2 = new Vector();
/* 2087 */     Vector localVector3 = new Vector();
/* 2088 */     CompilerMember localCompilerMember1 = new CompilerMember(new MemberDefinition(getWhere(), this, 8, Type.tMethod(Type.tVoid), idClassInit, null, null), new Assembler());
/* 2089 */     Context localContext = new Context((Context)null, localCompilerMember1.field);
/*      */ 
/* 2091 */     for (Object localObject1 = this; ((ClassDefinition)localObject1).isInnerClass(); localObject1 = ((ClassDefinition)localObject1).getOuterClass()) {
/* 2092 */       localVector3.addElement(localObject1);
/*      */     }
/*      */ 
/* 2095 */     int i = localVector3.size();
/* 2096 */     int j = i;
/*      */     while (true) { j--; if (j < 0) break;
/* 2097 */       localVector3.addElement(localVector3.elementAt(j)); }
/* 2098 */     j = i;
/*      */     while (true) { j--; if (j < 0) break;
/* 2099 */       localVector3.removeElementAt(j);
/*      */     }
/*      */ 
/* 2103 */     boolean bool1 = isDeprecated();
/* 2104 */     boolean bool2 = isSynthetic();
/* 2105 */     int k = 0;
/* 2106 */     int m = 0;
/*      */ 
/* 2109 */     for (SourceMember localSourceMember = (SourceMember)getFirstMember(); 
/* 2110 */       localSourceMember != null; 
/* 2111 */       localSourceMember = (SourceMember)localSourceMember.getNextMember())
/*      */     {
/* 2115 */       bool1 |= localSourceMember.isDeprecated();
/* 2116 */       bool2 |= localSourceMember.isSynthetic();
/*      */       try
/*      */       {
/*      */         CompilerMember localCompilerMember2;
/* 2119 */         if (localSourceMember.isMethod())
/*      */         {
/* 2121 */           m = m | (localSourceMember
/* 2121 */             .getExceptions(paramEnvironment).length > 0 ? 
/* 2121 */             1 : 0);
/*      */ 
/* 2123 */           if (localSourceMember.isInitializer()) {
/* 2124 */             if (localSourceMember.isStatic())
/* 2125 */               localSourceMember.code(paramEnvironment, localCompilerMember1.asm);
/*      */           }
/*      */           else {
/* 2128 */             localCompilerMember2 = new CompilerMember(localSourceMember, new Assembler());
/*      */ 
/* 2130 */             localSourceMember.code(paramEnvironment, localCompilerMember2.asm);
/* 2131 */             localVector2.addElement(localCompilerMember2);
/*      */           }
/* 2133 */         } else if (localSourceMember.isInnerClass()) {
/* 2134 */           localVector3.addElement(localSourceMember.getInnerClass());
/* 2135 */         } else if (localSourceMember.isVariable()) {
/* 2136 */           localSourceMember.inline(paramEnvironment);
/* 2137 */           localCompilerMember2 = new CompilerMember(localSourceMember, null);
/* 2138 */           localVector1.addElement(localCompilerMember2);
/* 2139 */           if (localSourceMember.isStatic()) {
/* 2140 */             localSourceMember.codeInit(paramEnvironment, localContext, localCompilerMember1.asm);
/*      */           }
/*      */ 
/* 2144 */           k = k | (localSourceMember
/* 2144 */             .getInitialValue() != null ? 1 : 0);
/*      */         }
/*      */       } catch (CompilerError localCompilerError) {
/* 2147 */         localCompilerError.printStackTrace();
/* 2148 */         paramEnvironment.error(localSourceMember, 0L, "generic", localSourceMember
/* 2149 */           .getClassDeclaration() + ":" + localSourceMember + "@" + localCompilerError
/* 2150 */           .toString(), null, null);
/*      */       }
/*      */     }
/* 2153 */     if (!localCompilerMember1.asm.empty()) {
/* 2154 */       localCompilerMember1.asm.add(getWhere(), 177, true);
/* 2155 */       localVector2.addElement(localCompilerMember1);
/*      */     }
/*      */ 
/* 2159 */     if (getNestError()) {
/* 2160 */       return;
/*      */     }
/*      */ 
/* 2163 */     int n = 0;
/*      */ 
/* 2166 */     if (localVector2.size() > 0) {
/* 2167 */       this.tab.put("Code");
/*      */     }
/* 2169 */     if (k != 0) {
/* 2170 */       this.tab.put("ConstantValue");
/*      */     }
/*      */ 
/* 2173 */     String str1 = null;
/* 2174 */     if (paramEnvironment.debug_source()) {
/* 2175 */       str1 = ((ClassFile)getSource()).getName();
/* 2176 */       this.tab.put("SourceFile");
/* 2177 */       this.tab.put(str1);
/* 2178 */       n++;
/*      */     }
/*      */ 
/* 2181 */     if (m != 0) {
/* 2182 */       this.tab.put("Exceptions");
/*      */     }
/*      */ 
/* 2185 */     if (paramEnvironment.debug_lines()) {
/* 2186 */       this.tab.put("LineNumberTable");
/*      */     }
/* 2188 */     if (bool1) {
/* 2189 */       this.tab.put("Deprecated");
/* 2190 */       if (isDeprecated()) {
/* 2191 */         n++;
/*      */       }
/*      */     }
/* 2194 */     if (bool2) {
/* 2195 */       this.tab.put("Synthetic");
/* 2196 */       if (isSynthetic()) {
/* 2197 */         n++;
/*      */       }
/*      */     }
/*      */ 
/* 2201 */     if (paramEnvironment.coverage()) {
/* 2202 */       n += 2;
/* 2203 */       this.tab.put("AbsoluteSourcePath");
/* 2204 */       this.tab.put("TimeStamp");
/* 2205 */       this.tab.put("CoverageTable");
/*      */     }
/*      */ 
/* 2208 */     if (paramEnvironment.debug_vars()) {
/* 2209 */       this.tab.put("LocalVariableTable");
/*      */     }
/* 2211 */     if (localVector3.size() > 0) {
/* 2212 */       this.tab.put("InnerClasses");
/* 2213 */       n++;
/*      */     }
/*      */ 
/* 2217 */     String str2 = "";
/* 2218 */     long l = 0L;
/*      */ 
/* 2220 */     if (paramEnvironment.coverage()) {
/* 2221 */       str2 = getAbsoluteName();
/* 2222 */       l = System.currentTimeMillis();
/* 2223 */       this.tab.put(str2);
/*      */     }
/*      */ 
/* 2226 */     this.tab.put(getClassDeclaration());
/* 2227 */     if (getSuperClass() != null) {
/* 2228 */       this.tab.put(getSuperClass());
/*      */     }
/* 2230 */     for (int i1 = 0; i1 < this.interfaces.length; i1++) {
/* 2231 */       this.tab.put(this.interfaces[i1]);
/*      */     }
/*      */ 
/* 2239 */     CompilerMember[] arrayOfCompilerMember = new CompilerMember[localVector2
/* 2239 */       .size()];
/* 2240 */     localVector2.copyInto(arrayOfCompilerMember);
/* 2241 */     Arrays.sort(arrayOfCompilerMember);
/* 2242 */     for (int i2 = 0; i2 < localVector2.size(); i2++) {
/* 2243 */       localVector2.setElementAt(arrayOfCompilerMember[i2], i2);
/*      */     }
/*      */ 
/* 2246 */     for (Object localObject2 = localVector2.elements(); ((Enumeration)localObject2).hasMoreElements(); ) {
/* 2247 */       localObject3 = (CompilerMember)((Enumeration)localObject2).nextElement();
/*      */       try {
/* 2249 */         ((CompilerMember)localObject3).asm.optimize(paramEnvironment);
/* 2250 */         ((CompilerMember)localObject3).asm.collect(paramEnvironment, ((CompilerMember)localObject3).field, this.tab);
/* 2251 */         this.tab.put(((CompilerMember)localObject3).name);
/* 2252 */         this.tab.put(((CompilerMember)localObject3).sig);
/* 2253 */         ClassDeclaration[] arrayOfClassDeclaration1 = ((CompilerMember)localObject3).field.getExceptions(paramEnvironment);
/* 2254 */         for (int i5 = 0; i5 < arrayOfClassDeclaration1.length; i5++)
/* 2255 */           this.tab.put(arrayOfClassDeclaration1[i5]);
/*      */       }
/*      */       catch (Exception localException) {
/* 2258 */         localException.printStackTrace();
/* 2259 */         paramEnvironment.error(((CompilerMember)localObject3).field, -1L, "generic", ((CompilerMember)localObject3).field.getName() + "@" + localException.toString(), null, null);
/* 2260 */         ((CompilerMember)localObject3).asm.listing(System.out);
/*      */       }
/*      */     }
/*      */     Object localObject3;
/* 2265 */     for (localObject2 = localVector1.elements(); ((Enumeration)localObject2).hasMoreElements(); ) {
/* 2266 */       localObject3 = (CompilerMember)((Enumeration)localObject2).nextElement();
/* 2267 */       this.tab.put(((CompilerMember)localObject3).name);
/* 2268 */       this.tab.put(((CompilerMember)localObject3).sig);
/*      */ 
/* 2270 */       localObject4 = ((CompilerMember)localObject3).field.getInitialValue();
/* 2271 */       if (localObject4 != null)
/* 2272 */         this.tab.put((localObject4 instanceof String) ? new StringExpression(((CompilerMember)localObject3).field.getWhere(), (String)localObject4) : localObject4);
/*      */     }
/*      */     Object localObject4;
/* 2277 */     localObject2 = localVector3.elements();
/* 2278 */     while (((Enumeration)localObject2).hasMoreElements()) {
/* 2279 */       localObject3 = (ClassDefinition)((Enumeration)localObject2).nextElement();
/* 2280 */       this.tab.put(((ClassDefinition)localObject3).getClassDeclaration());
/*      */ 
/* 2284 */       if (!((ClassDefinition)localObject3).isLocal()) {
/* 2285 */         localObject4 = ((ClassDefinition)localObject3).getOuterClass();
/* 2286 */         this.tab.put(((ClassDefinition)localObject4).getClassDeclaration());
/*      */       }
/*      */ 
/* 2291 */       localObject4 = ((ClassDefinition)localObject3).getLocalName();
/* 2292 */       if (localObject4 != idNull) {
/* 2293 */         this.tab.put(((Identifier)localObject4).toString());
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/* 2298 */     localObject2 = new DataOutputStream(paramOutputStream);
/* 2299 */     ((DataOutputStream)localObject2).writeInt(-889275714);
/* 2300 */     ((DataOutputStream)localObject2).writeShort(this.toplevelEnv.getMinorVersion());
/* 2301 */     ((DataOutputStream)localObject2).writeShort(this.toplevelEnv.getMajorVersion());
/* 2302 */     this.tab.write(paramEnvironment, (DataOutputStream)localObject2);
/*      */ 
/* 2305 */     int i3 = getModifiers() & 0x200611;
/*      */ 
/* 2319 */     if (isInterface())
/*      */     {
/* 2326 */       assertModifiers(i3, 1024);
/*      */     }
/*      */     else
/*      */     {
/* 2331 */       i3 |= 32;
/*      */     }
/*      */ 
/* 2335 */     if (this.outerClass != null)
/*      */     {
/* 2340 */       if (isProtected()) i3 |= 1;
/*      */ 
/* 2342 */       if (this.outerClass.isInterface()) {
/* 2343 */         assertModifiers(i3, 1);
/*      */       }
/*      */     }
/*      */ 
/* 2347 */     ((DataOutputStream)localObject2).writeShort(i3);
/*      */ 
/* 2349 */     if (paramEnvironment.dumpModifiers()) {
/* 2350 */       localObject4 = getName();
/*      */ 
/* 2352 */       localObject5 = Identifier.lookup(((Identifier)localObject4)
/* 2352 */         .getQualifier(), ((Identifier)localObject4).getFlatName());
/* 2353 */       System.out.println();
/* 2354 */       System.out.println("CLASSFILE  " + localObject5);
/* 2355 */       System.out.println("---" + classModifierString(i3));
/*      */     }
/*      */ 
/* 2358 */     ((DataOutputStream)localObject2).writeShort(this.tab.index(getClassDeclaration()));
/* 2359 */     ((DataOutputStream)localObject2).writeShort(getSuperClass() != null ? this.tab.index(getSuperClass()) : 0);
/* 2360 */     ((DataOutputStream)localObject2).writeShort(this.interfaces.length);
/* 2361 */     for (int i4 = 0; i4 < this.interfaces.length; i4++) {
/* 2362 */       ((DataOutputStream)localObject2).writeShort(this.tab.index(this.interfaces[i4]));
/*      */     }
/*      */ 
/* 2366 */     ByteArrayOutputStream localByteArrayOutputStream = new ByteArrayOutputStream(256);
/* 2367 */     Object localObject5 = new ByteArrayOutputStream(256);
/* 2368 */     DataOutputStream localDataOutputStream = new DataOutputStream(localByteArrayOutputStream);
/*      */ 
/* 2370 */     ((DataOutputStream)localObject2).writeShort(localVector1.size());
/* 2371 */     for (Object localObject6 = localVector1.elements(); ((Enumeration)localObject6).hasMoreElements(); ) {
/* 2372 */       localObject7 = (CompilerMember)((Enumeration)localObject6).nextElement();
/* 2373 */       Object localObject8 = ((CompilerMember)localObject7).field.getInitialValue();
/*      */ 
/* 2375 */       ((DataOutputStream)localObject2).writeShort(((CompilerMember)localObject7).field.getModifiers() & 0xDF);
/* 2376 */       ((DataOutputStream)localObject2).writeShort(this.tab.index(((CompilerMember)localObject7).name));
/* 2377 */       ((DataOutputStream)localObject2).writeShort(this.tab.index(((CompilerMember)localObject7).sig));
/*      */ 
/* 2379 */       int i7 = localObject8 != null ? 1 : 0;
/* 2380 */       i9 = ((CompilerMember)localObject7).field.isDeprecated();
/* 2381 */       bool3 = ((CompilerMember)localObject7).field.isSynthetic();
/* 2382 */       i7 += (i9 != 0 ? 1 : 0) + (bool3 ? 1 : 0);
/*      */ 
/* 2384 */       ((DataOutputStream)localObject2).writeShort(i7);
/* 2385 */       if (localObject8 != null) {
/* 2386 */         ((DataOutputStream)localObject2).writeShort(this.tab.index("ConstantValue"));
/* 2387 */         ((DataOutputStream)localObject2).writeInt(2);
/* 2388 */         ((DataOutputStream)localObject2).writeShort(this.tab.index((localObject8 instanceof String) ? new StringExpression(((CompilerMember)localObject7).field.getWhere(), (String)localObject8) : localObject8));
/*      */       }
/* 2390 */       if (i9 != 0) {
/* 2391 */         ((DataOutputStream)localObject2).writeShort(this.tab.index("Deprecated"));
/* 2392 */         ((DataOutputStream)localObject2).writeInt(0);
/*      */       }
/* 2394 */       if (bool3) {
/* 2395 */         ((DataOutputStream)localObject2).writeShort(this.tab.index("Synthetic"));
/* 2396 */         ((DataOutputStream)localObject2).writeInt(0);
/*      */       }
/*      */     }
/*      */     Object localObject7;
/*      */     int i9;
/*      */     boolean bool3;
/* 2402 */     ((DataOutputStream)localObject2).writeShort(localVector2.size());
/* 2403 */     for (localObject6 = localVector2.elements(); ((Enumeration)localObject6).hasMoreElements(); ) {
/* 2404 */       localObject7 = (CompilerMember)((Enumeration)localObject6).nextElement();
/*      */ 
/* 2406 */       int i6 = ((CompilerMember)localObject7).field.getModifiers() & 0x20053F;
/*      */ 
/* 2410 */       if (((i6 & 0x200000) != 0) || ((i3 & 0x200000) != 0)) {
/* 2411 */         i6 |= 2048;
/*      */       }
/* 2414 */       else if (paramEnvironment.strictdefault()) {
/* 2415 */         i6 |= 2048;
/*      */       }
/*      */ 
/* 2418 */       ((DataOutputStream)localObject2).writeShort(i6);
/*      */ 
/* 2420 */       ((DataOutputStream)localObject2).writeShort(this.tab.index(((CompilerMember)localObject7).name));
/* 2421 */       ((DataOutputStream)localObject2).writeShort(this.tab.index(((CompilerMember)localObject7).sig));
/* 2422 */       ClassDeclaration[] arrayOfClassDeclaration2 = ((CompilerMember)localObject7).field.getExceptions(paramEnvironment);
/* 2423 */       i9 = arrayOfClassDeclaration2.length > 0 ? 1 : 0;
/* 2424 */       bool3 = ((CompilerMember)localObject7).field.isDeprecated();
/* 2425 */       boolean bool4 = ((CompilerMember)localObject7).field.isSynthetic();
/*      */       int i10;
/* 2426 */       i9 += (bool3 ? 1 : 0) + (bool4 ? 1 : 0);
/*      */       int i11;
/* 2428 */       if (!((CompilerMember)localObject7).asm.empty()) {
/* 2429 */         ((DataOutputStream)localObject2).writeShort(i10 + 1);
/* 2430 */         ((CompilerMember)localObject7).asm.write(paramEnvironment, localDataOutputStream, ((CompilerMember)localObject7).field, this.tab);
/* 2431 */         i11 = 0;
/* 2432 */         if (paramEnvironment.debug_lines()) {
/* 2433 */           i11++;
/*      */         }
/*      */ 
/* 2436 */         if (paramEnvironment.coverage()) {
/* 2437 */           i11++;
/*      */         }
/*      */ 
/* 2440 */         if (paramEnvironment.debug_vars()) {
/* 2441 */           i11++;
/*      */         }
/* 2443 */         localDataOutputStream.writeShort(i11);
/*      */ 
/* 2445 */         if (paramEnvironment.debug_lines()) {
/* 2446 */           ((CompilerMember)localObject7).asm.writeLineNumberTable(paramEnvironment, new DataOutputStream((OutputStream)localObject5), this.tab);
/* 2447 */           localDataOutputStream.writeShort(this.tab.index("LineNumberTable"));
/* 2448 */           localDataOutputStream.writeInt(((ByteArrayOutputStream)localObject5).size());
/* 2449 */           ((ByteArrayOutputStream)localObject5).writeTo(localByteArrayOutputStream);
/* 2450 */           ((ByteArrayOutputStream)localObject5).reset();
/*      */         }
/*      */ 
/* 2454 */         if (paramEnvironment.coverage()) {
/* 2455 */           ((CompilerMember)localObject7).asm.writeCoverageTable(paramEnvironment, this, new DataOutputStream((OutputStream)localObject5), this.tab, ((CompilerMember)localObject7).field.getWhere());
/* 2456 */           localDataOutputStream.writeShort(this.tab.index("CoverageTable"));
/* 2457 */           localDataOutputStream.writeInt(((ByteArrayOutputStream)localObject5).size());
/* 2458 */           ((ByteArrayOutputStream)localObject5).writeTo(localByteArrayOutputStream);
/* 2459 */           ((ByteArrayOutputStream)localObject5).reset();
/*      */         }
/*      */ 
/* 2462 */         if (paramEnvironment.debug_vars()) {
/* 2463 */           ((CompilerMember)localObject7).asm.writeLocalVariableTable(paramEnvironment, ((CompilerMember)localObject7).field, new DataOutputStream((OutputStream)localObject5), this.tab);
/* 2464 */           localDataOutputStream.writeShort(this.tab.index("LocalVariableTable"));
/* 2465 */           localDataOutputStream.writeInt(((ByteArrayOutputStream)localObject5).size());
/* 2466 */           ((ByteArrayOutputStream)localObject5).writeTo(localByteArrayOutputStream);
/* 2467 */           ((ByteArrayOutputStream)localObject5).reset();
/*      */         }
/*      */ 
/* 2470 */         ((DataOutputStream)localObject2).writeShort(this.tab.index("Code"));
/* 2471 */         ((DataOutputStream)localObject2).writeInt(localByteArrayOutputStream.size());
/* 2472 */         localByteArrayOutputStream.writeTo((OutputStream)localObject2);
/* 2473 */         localByteArrayOutputStream.reset();
/*      */       }
/*      */       else {
/* 2476 */         if ((paramEnvironment.coverage()) && ((((CompilerMember)localObject7).field.getModifiers() & 0x100) > 0)) {
/* 2477 */           ((CompilerMember)localObject7).asm.addNativeToJcovTab(paramEnvironment, this);
/*      */         }
/* 2479 */         ((DataOutputStream)localObject2).writeShort(i10);
/*      */       }
/*      */ 
/* 2482 */       if (arrayOfClassDeclaration2.length > 0) {
/* 2483 */         ((DataOutputStream)localObject2).writeShort(this.tab.index("Exceptions"));
/* 2484 */         ((DataOutputStream)localObject2).writeInt(2 + arrayOfClassDeclaration2.length * 2);
/* 2485 */         ((DataOutputStream)localObject2).writeShort(arrayOfClassDeclaration2.length);
/* 2486 */         for (i11 = 0; i11 < arrayOfClassDeclaration2.length; i11++) {
/* 2487 */           ((DataOutputStream)localObject2).writeShort(this.tab.index(arrayOfClassDeclaration2[i11]));
/*      */         }
/*      */       }
/* 2490 */       if (bool3) {
/* 2491 */         ((DataOutputStream)localObject2).writeShort(this.tab.index("Deprecated"));
/* 2492 */         ((DataOutputStream)localObject2).writeInt(0);
/*      */       }
/* 2494 */       if (bool4) {
/* 2495 */         ((DataOutputStream)localObject2).writeShort(this.tab.index("Synthetic"));
/* 2496 */         ((DataOutputStream)localObject2).writeInt(0);
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/* 2501 */     ((DataOutputStream)localObject2).writeShort(n);
/*      */ 
/* 2503 */     if (paramEnvironment.debug_source()) {
/* 2504 */       ((DataOutputStream)localObject2).writeShort(this.tab.index("SourceFile"));
/* 2505 */       ((DataOutputStream)localObject2).writeInt(2);
/* 2506 */       ((DataOutputStream)localObject2).writeShort(this.tab.index(str1));
/*      */     }
/*      */ 
/* 2509 */     if (isDeprecated()) {
/* 2510 */       ((DataOutputStream)localObject2).writeShort(this.tab.index("Deprecated"));
/* 2511 */       ((DataOutputStream)localObject2).writeInt(0);
/*      */     }
/* 2513 */     if (isSynthetic()) {
/* 2514 */       ((DataOutputStream)localObject2).writeShort(this.tab.index("Synthetic"));
/* 2515 */       ((DataOutputStream)localObject2).writeInt(0);
/*      */     }
/*      */ 
/* 2519 */     if (paramEnvironment.coverage()) {
/* 2520 */       ((DataOutputStream)localObject2).writeShort(this.tab.index("AbsoluteSourcePath"));
/* 2521 */       ((DataOutputStream)localObject2).writeInt(2);
/* 2522 */       ((DataOutputStream)localObject2).writeShort(this.tab.index(str2));
/* 2523 */       ((DataOutputStream)localObject2).writeShort(this.tab.index("TimeStamp"));
/* 2524 */       ((DataOutputStream)localObject2).writeInt(8);
/* 2525 */       ((DataOutputStream)localObject2).writeLong(l);
/*      */     }
/*      */ 
/* 2529 */     if (localVector3.size() > 0) {
/* 2530 */       ((DataOutputStream)localObject2).writeShort(this.tab.index("InnerClasses"));
/* 2531 */       ((DataOutputStream)localObject2).writeInt(2 + 8 * localVector3.size());
/* 2532 */       ((DataOutputStream)localObject2).writeShort(localVector3.size());
/* 2533 */       localObject6 = localVector3.elements();
/* 2534 */       while (((Enumeration)localObject6).hasMoreElements())
/*      */       {
/* 2552 */         localObject7 = (ClassDefinition)((Enumeration)localObject6).nextElement();
/* 2553 */         ((DataOutputStream)localObject2).writeShort(this.tab.index(((ClassDefinition)localObject7).getClassDeclaration()));
/*      */ 
/* 2560 */         if ((((ClassDefinition)localObject7).isLocal()) || (((ClassDefinition)localObject7).isAnonymous())) {
/* 2561 */           ((DataOutputStream)localObject2).writeShort(0);
/*      */         }
/*      */         else
/*      */         {
/* 2566 */           localObject9 = ((ClassDefinition)localObject7).getOuterClass();
/* 2567 */           ((DataOutputStream)localObject2).writeShort(this.tab.index(((ClassDefinition)localObject9).getClassDeclaration()));
/*      */         }
/*      */ 
/* 2571 */         Object localObject9 = ((ClassDefinition)localObject7).getLocalName();
/* 2572 */         if (localObject9 == idNull) {
/* 2573 */           if (!((ClassDefinition)localObject7).isAnonymous()) {
/* 2574 */             throw new CompilerError("compileClass(), anonymous");
/*      */           }
/* 2576 */           ((DataOutputStream)localObject2).writeShort(0);
/*      */         } else {
/* 2578 */           ((DataOutputStream)localObject2).writeShort(this.tab.index(((Identifier)localObject9).toString()));
/*      */         }
/*      */ 
/* 2582 */         int i8 = ((ClassDefinition)localObject7).getInnerClassMember().getModifiers() & 0xE1F;
/*      */ 
/* 2590 */         if (((ClassDefinition)localObject7).isInterface())
/*      */         {
/* 2592 */           assertModifiers(i8, 1032);
/*      */         }
/* 2594 */         if (((ClassDefinition)localObject7).getOuterClass().isInterface())
/*      */         {
/* 2596 */           i8 &= -7;
/* 2597 */           assertModifiers(i8, 9);
/*      */         }
/*      */ 
/* 2600 */         ((DataOutputStream)localObject2).writeShort(i8);
/*      */ 
/* 2602 */         if (paramEnvironment.dumpModifiers()) {
/* 2603 */           Identifier localIdentifier1 = ((ClassDefinition)localObject7).getInnerClassMember().getName();
/*      */ 
/* 2605 */           Identifier localIdentifier2 = Identifier.lookup(localIdentifier1
/* 2605 */             .getQualifier(), localIdentifier1.getFlatName());
/* 2606 */           System.out.println("INNERCLASS " + localIdentifier2);
/* 2607 */           System.out.println("---" + classModifierString(i8));
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/* 2614 */     ((DataOutputStream)localObject2).flush();
/* 2615 */     this.tab = null;
/*      */ 
/* 2619 */     if (paramEnvironment.covdata()) {
/* 2620 */       localObject6 = new Assembler();
/* 2621 */       ((Assembler)localObject6).GenVecJCov(paramEnvironment, this, l);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void printClassDependencies(Environment paramEnvironment)
/*      */   {
/*      */     String str2;
/*      */     Enumeration localEnumeration;
/* 2633 */     if (this.toplevelEnv.print_dependencies())
/*      */     {
/* 2637 */       String str1 = ((ClassFile)getSource()).getAbsoluteName();
/*      */ 
/* 2644 */       str2 = Type.mangleInnerType(getName()).toString();
/*      */ 
/* 2647 */       long l1 = getWhere() >> 32;
/*      */ 
/* 2650 */       long l2 = getEndPosition() >> 32;
/*      */ 
/* 2654 */       System.out.println("CLASS:" + str1 + "," + l1 + "," + l2 + "," + str2);
/*      */ 
/* 2665 */       for (localEnumeration = this.deps.elements(); localEnumeration.hasMoreElements(); ) {
/* 2666 */         ClassDeclaration localClassDeclaration = (ClassDeclaration)localEnumeration.nextElement();
/*      */ 
/* 2669 */         String str3 = Type.mangleInnerType(localClassDeclaration
/* 2669 */           .getName()).toString();
/* 2670 */         paramEnvironment.output("CLDEP:" + str2 + "," + str3);
/*      */       }
/*      */     }
/*      */   }
/*      */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     sun.tools.javac.SourceClass
 * JD-Core Version:    0.6.2
 */