/*      */ package sun.rmi.rmic.iiop;
/*      */ 
/*      */ import java.io.File;
/*      */ import java.io.IOException;
/*      */ import java.util.Arrays;
/*      */ import java.util.Enumeration;
/*      */ import java.util.HashSet;
/*      */ import java.util.Vector;
/*      */ import sun.rmi.rmic.IndentingWriter;
/*      */ import sun.rmi.rmic.Main;
/*      */ import sun.tools.java.ClassDeclaration;
/*      */ import sun.tools.java.ClassDefinition;
/*      */ import sun.tools.java.ClassNotFound;
/*      */ import sun.tools.java.CompilerError;
/*      */ import sun.tools.java.Identifier;
/*      */ import sun.tools.java.MemberDefinition;
/*      */ import sun.tools.tree.IntegerExpression;
/*      */ import sun.tools.tree.LocalMember;
/*      */ import sun.tools.tree.Node;
/*      */ 
/*      */ public abstract class CompoundType extends Type
/*      */ {
/*      */   protected Method[] methods;
/*      */   protected InterfaceType[] interfaces;
/*      */   protected Member[] members;
/*      */   protected ClassDefinition classDef;
/*      */   protected ClassDeclaration classDecl;
/*   67 */   protected boolean isCORBAObject = false;
/*   68 */   protected boolean isIDLEntity = false;
/*   69 */   protected boolean isAbstractBase = false;
/*   70 */   protected boolean isValueBase = false;
/*   71 */   protected boolean isCORBAUserException = false;
/*   72 */   protected boolean isException = false;
/*   73 */   protected boolean isCheckedException = false;
/*   74 */   protected boolean isRemoteExceptionOrSubclass = false;
/*      */   protected String idlExceptionName;
/*      */   protected String qualifiedIDLExceptionName;
/*      */ 
/*      */   public boolean isCORBAObject()
/*      */   {
/*   87 */     return this.isCORBAObject;
/*      */   }
/*      */ 
/*      */   public boolean isIDLEntity()
/*      */   {
/*   95 */     return this.isIDLEntity;
/*      */   }
/*      */ 
/*      */   public boolean isValueBase()
/*      */   {
/*  103 */     return this.isValueBase;
/*      */   }
/*      */ 
/*      */   public boolean isAbstractBase()
/*      */   {
/*  111 */     return this.isAbstractBase;
/*      */   }
/*      */ 
/*      */   public boolean isException()
/*      */   {
/*  118 */     return this.isException;
/*      */   }
/*      */ 
/*      */   public boolean isCheckedException()
/*      */   {
/*  126 */     return this.isCheckedException;
/*      */   }
/*      */ 
/*      */   public boolean isRemoteExceptionOrSubclass()
/*      */   {
/*  135 */     return this.isRemoteExceptionOrSubclass;
/*      */   }
/*      */ 
/*      */   public boolean isCORBAUserException()
/*      */   {
/*  143 */     return this.isCORBAUserException;
/*      */   }
/*      */ 
/*      */   public boolean isIDLEntityException()
/*      */   {
/*  151 */     return (isIDLEntity()) && (isException());
/*      */   }
/*      */ 
/*      */   public boolean isBoxed()
/*      */   {
/*  161 */     return (isIDLEntity()) && (!isValueBase()) && 
/*  160 */       (!isAbstractBase()) && (!isCORBAObject()) && 
/*  161 */       (!isIDLEntityException());
/*      */   }
/*      */ 
/*      */   public String getIDLExceptionName()
/*      */   {
/*  170 */     return this.idlExceptionName;
/*      */   }
/*      */ 
/*      */   public String getQualifiedIDLExceptionName(boolean paramBoolean)
/*      */   {
/*  180 */     if ((this.qualifiedIDLExceptionName != null) && (paramBoolean))
/*      */     {
/*  182 */       if (getIDLModuleNames().length > 0)
/*  183 */         return "::" + this.qualifiedIDLExceptionName;
/*      */     }
/*  185 */     return this.qualifiedIDLExceptionName;
/*      */   }
/*      */ 
/*      */   public String getSignature()
/*      */   {
/*  194 */     String str = this.classDecl.getType().getTypeSignature();
/*  195 */     if (str.endsWith(";")) {
/*  196 */       str = str.substring(0, str.length() - 1);
/*      */     }
/*  198 */     return str;
/*      */   }
/*      */ 
/*      */   public ClassDeclaration getClassDeclaration()
/*      */   {
/*  205 */     return this.classDecl;
/*      */   }
/*      */ 
/*      */   public ClassDefinition getClassDefinition()
/*      */   {
/*  212 */     return this.classDef;
/*      */   }
/*      */ 
/*      */   public ClassType getSuperclass()
/*      */   {
/*  220 */     return null;
/*      */   }
/*      */ 
/*      */   public InterfaceType[] getInterfaces()
/*      */   {
/*  229 */     if (this.interfaces != null) {
/*  230 */       return (InterfaceType[])this.interfaces.clone();
/*      */     }
/*  232 */     return null;
/*      */   }
/*      */ 
/*      */   public Method[] getMethods()
/*      */   {
/*  240 */     if (this.methods != null) {
/*  241 */       return (Method[])this.methods.clone();
/*      */     }
/*  243 */     return null;
/*      */   }
/*      */ 
/*      */   public Member[] getMembers()
/*      */   {
/*  251 */     if (this.members != null) {
/*  252 */       return (Member[])this.members.clone();
/*      */     }
/*  254 */     return null;
/*      */   }
/*      */ 
/*      */   public static CompoundType forCompound(ClassDefinition paramClassDefinition, ContextStack paramContextStack)
/*      */   {
/*  266 */     CompoundType localCompoundType = null;
/*      */     try
/*      */     {
/*  269 */       localCompoundType = (CompoundType)makeType(paramClassDefinition.getType(), paramClassDefinition, paramContextStack);
/*      */     } catch (ClassCastException localClassCastException) {
/*      */     }
/*  272 */     return localCompoundType;
/*      */   }
/*      */ 
/*      */   protected void destroy()
/*      */   {
/*  284 */     if (!this.destroyed) {
/*  285 */       super.destroy();
/*      */       int i;
/*  287 */       if (this.methods != null) {
/*  288 */         for (i = 0; i < this.methods.length; i++) {
/*  289 */           if (this.methods[i] != null) this.methods[i].destroy();
/*      */         }
/*  291 */         this.methods = null;
/*      */       }
/*      */ 
/*  294 */       if (this.interfaces != null) {
/*  295 */         for (i = 0; i < this.interfaces.length; i++) {
/*  296 */           if (this.interfaces[i] != null) this.interfaces[i].destroy();
/*      */         }
/*  298 */         this.interfaces = null;
/*      */       }
/*      */ 
/*  301 */       if (this.members != null) {
/*  302 */         for (i = 0; i < this.members.length; i++) {
/*  303 */           if (this.members[i] != null) this.members[i].destroy();
/*      */         }
/*  305 */         this.members = null;
/*      */       }
/*      */ 
/*  308 */       this.classDef = null;
/*  309 */       this.classDecl = null;
/*      */     }
/*      */   }
/*      */ 
/*      */   protected Class loadClass()
/*      */   {
/*  318 */     Class localClass = null;
/*      */     try
/*      */     {
/*  328 */       this.env.getMain().compileAllClasses(this.env);
/*      */     } catch (Exception localException1) {
/*  330 */       for (Enumeration localEnumeration = this.env.getClasses(); localEnumeration.hasMoreElements(); ) {
/*  331 */         ClassDeclaration localClassDeclaration = (ClassDeclaration)localEnumeration.nextElement();
/*      */       }
/*  333 */       failedConstraint(26, false, this.stack, "required classes");
/*  334 */       this.env.flushErrors();
/*      */     }
/*      */ 
/*      */     try
/*      */     {
/*  343 */       ClassLoader localClassLoader = Thread.currentThread().getContextClassLoader();
/*  344 */       localClass = localClassLoader.loadClass(getQualifiedName());
/*      */     }
/*      */     catch (ClassNotFoundException localClassNotFoundException1) {
/*      */       try {
/*  348 */         localClass = this.env.classPathLoader.loadClass(getQualifiedName());
/*      */       }
/*      */       catch (NullPointerException localNullPointerException)
/*      */       {
/*      */       }
/*      */       catch (ClassNotFoundException localClassNotFoundException2)
/*      */       {
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/*  366 */     if (localClass == null)
/*      */     {
/*  373 */       if (this.env.loader == null) {
/*  374 */         File localFile = this.env.getMain().getDestinationDir();
/*  375 */         if (localFile == null) {
/*  376 */           localFile = new File(".");
/*      */         }
/*  378 */         this.env.loader = new DirectoryLoader(localFile);
/*      */       }
/*      */       try
/*      */       {
/*  382 */         localClass = this.env.loader.loadClass(getQualifiedName());
/*      */       } catch (Exception localException2) {
/*      */       }
/*      */     }
/*  386 */     return localClass;
/*      */   }
/*      */ 
/*      */   protected boolean printExtends(IndentingWriter paramIndentingWriter, boolean paramBoolean1, boolean paramBoolean2, boolean paramBoolean3)
/*      */     throws IOException
/*      */   {
/*  396 */     ClassType localClassType = getSuperclass();
/*      */ 
/*  398 */     if ((localClassType != null) && ((!paramBoolean2) || (
/*  399 */       (!localClassType
/*  399 */       .isType(1024)) && 
/*  399 */       (!localClassType.isType(2048))))) {
/*  400 */       paramIndentingWriter.p(" extends ");
/*  401 */       localClassType.printTypeName(paramIndentingWriter, paramBoolean1, paramBoolean2, paramBoolean3);
/*  402 */       return true;
/*      */     }
/*  404 */     return false;
/*      */   }
/*      */ 
/*      */   protected void printImplements(IndentingWriter paramIndentingWriter, String paramString, boolean paramBoolean1, boolean paramBoolean2, boolean paramBoolean3)
/*      */     throws IOException
/*      */   {
/*  415 */     InterfaceType[] arrayOfInterfaceType = getInterfaces();
/*      */ 
/*  417 */     String str = " implements";
/*      */ 
/*  419 */     if (isInterface()) {
/*  420 */       str = " extends";
/*      */     }
/*      */ 
/*  423 */     if (paramBoolean2) {
/*  424 */       str = ":";
/*      */     }
/*      */ 
/*  427 */     for (int i = 0; i < arrayOfInterfaceType.length; i++)
/*  428 */       if ((!paramBoolean2) || ((!arrayOfInterfaceType[i].isType(1024)) && (!arrayOfInterfaceType[i].isType(2048)))) {
/*  429 */         if (i == 0)
/*  430 */           paramIndentingWriter.p(paramString + str + " ");
/*      */         else {
/*  432 */           paramIndentingWriter.p(", ");
/*      */         }
/*  434 */         arrayOfInterfaceType[i].printTypeName(paramIndentingWriter, paramBoolean1, paramBoolean2, paramBoolean3);
/*      */       }
/*      */   }
/*      */ 
/*      */   protected void printMembers(IndentingWriter paramIndentingWriter, boolean paramBoolean1, boolean paramBoolean2, boolean paramBoolean3)
/*      */     throws IOException
/*      */   {
/*  446 */     Member[] arrayOfMember = getMembers();
/*      */ 
/*  448 */     for (int i = 0; i < arrayOfMember.length; i++)
/*  449 */       if (!arrayOfMember[i].isInnerClassDeclaration()) {
/*  450 */         Type localType = arrayOfMember[i].getType();
/*  451 */         String str1 = arrayOfMember[i].getVisibility();
/*      */         String str2;
/*  454 */         if (paramBoolean2)
/*  455 */           str2 = arrayOfMember[i].getIDLName();
/*      */         else {
/*  457 */           str2 = arrayOfMember[i].getName();
/*      */         }
/*      */ 
/*  460 */         String str3 = arrayOfMember[i].getValue();
/*      */ 
/*  462 */         paramIndentingWriter.p(str1);
/*  463 */         if (str1.length() > 0) {
/*  464 */           paramIndentingWriter.p(" ");
/*      */         }
/*  466 */         localType.printTypeName(paramIndentingWriter, paramBoolean1, paramBoolean2, paramBoolean3);
/*  467 */         paramIndentingWriter.p(" " + str2);
/*      */ 
/*  469 */         if (str3 != null)
/*  470 */           paramIndentingWriter.pln(" = " + str3 + ";");
/*      */         else
/*  472 */           paramIndentingWriter.pln(";");
/*      */       }
/*      */   }
/*      */ 
/*      */   protected void printMethods(IndentingWriter paramIndentingWriter, boolean paramBoolean1, boolean paramBoolean2, boolean paramBoolean3)
/*      */     throws IOException
/*      */   {
/*  485 */     Method[] arrayOfMethod = getMethods();
/*      */ 
/*  487 */     for (int i = 0; i < arrayOfMethod.length; i++) {
/*  488 */       Method localMethod = arrayOfMethod[i];
/*  489 */       printMethod(localMethod, paramIndentingWriter, paramBoolean1, paramBoolean2, paramBoolean3);
/*      */     }
/*      */   }
/*      */ 
/*      */   protected void printMethod(Method paramMethod, IndentingWriter paramIndentingWriter, boolean paramBoolean1, boolean paramBoolean2, boolean paramBoolean3)
/*      */     throws IOException
/*      */   {
/*  504 */     String str = paramMethod.getVisibility();
/*      */ 
/*  506 */     paramIndentingWriter.p(str);
/*  507 */     if (str.length() > 0) {
/*  508 */       paramIndentingWriter.p(" ");
/*      */     }
/*      */ 
/*  513 */     paramMethod.getReturnType().printTypeName(paramIndentingWriter, paramBoolean1, paramBoolean2, paramBoolean3);
/*      */ 
/*  517 */     if (paramBoolean2)
/*  518 */       paramIndentingWriter.p(" " + paramMethod.getIDLName());
/*      */     else {
/*  520 */       paramIndentingWriter.p(" " + paramMethod.getName());
/*      */     }
/*      */ 
/*  525 */     paramIndentingWriter.p(" (");
/*  526 */     Type[] arrayOfType = paramMethod.getArguments();
/*  527 */     String[] arrayOfString = paramMethod.getArgumentNames();
/*      */ 
/*  529 */     for (int i = 0; i < arrayOfType.length; i++) {
/*  530 */       if (i > 0) {
/*  531 */         paramIndentingWriter.p(", ");
/*      */       }
/*      */ 
/*  534 */       if (paramBoolean2) {
/*  535 */         paramIndentingWriter.p("in ");
/*      */       }
/*      */ 
/*  538 */       arrayOfType[i].printTypeName(paramIndentingWriter, paramBoolean1, paramBoolean2, paramBoolean3);
/*  539 */       paramIndentingWriter.p(" " + arrayOfString[i]);
/*      */     }
/*  541 */     paramIndentingWriter.p(")");
/*      */     ValueType[] arrayOfValueType;
/*  547 */     if (isType(65536))
/*  548 */       arrayOfValueType = paramMethod.getImplExceptions();
/*      */     else {
/*  550 */       arrayOfValueType = paramMethod.getExceptions();
/*      */     }
/*      */ 
/*  553 */     for (int j = 0; j < arrayOfValueType.length; j++) {
/*  554 */       if (j == 0) {
/*  555 */         if (paramBoolean2)
/*  556 */           paramIndentingWriter.p(" raises (");
/*      */         else
/*  558 */           paramIndentingWriter.p(" throws ");
/*      */       }
/*      */       else {
/*  561 */         paramIndentingWriter.p(", ");
/*      */       }
/*      */ 
/*  564 */       if (paramBoolean2) {
/*  565 */         if (paramBoolean1)
/*  566 */           paramIndentingWriter.p(arrayOfValueType[j].getQualifiedIDLExceptionName(paramBoolean3));
/*      */         else {
/*  568 */           paramIndentingWriter.p(arrayOfValueType[j].getIDLExceptionName());
/*      */         }
/*  570 */         paramIndentingWriter.p(" [a.k.a. ");
/*  571 */         arrayOfValueType[j].printTypeName(paramIndentingWriter, paramBoolean1, paramBoolean2, paramBoolean3);
/*  572 */         paramIndentingWriter.p("]");
/*      */       } else {
/*  574 */         arrayOfValueType[j].printTypeName(paramIndentingWriter, paramBoolean1, paramBoolean2, paramBoolean3);
/*      */       }
/*      */     }
/*      */ 
/*  578 */     if ((paramBoolean2) && (arrayOfValueType.length > 0)) {
/*  579 */       paramIndentingWriter.p(")");
/*      */     }
/*      */ 
/*  582 */     if (paramMethod.isInherited()) {
/*  583 */       paramIndentingWriter.p(" // Inherited from ");
/*  584 */       paramIndentingWriter.p(paramMethod.getDeclaredBy());
/*      */     }
/*      */ 
/*  587 */     paramIndentingWriter.pln(";");
/*      */   }
/*      */ 
/*      */   protected CompoundType(ContextStack paramContextStack, int paramInt, ClassDefinition paramClassDefinition)
/*      */   {
/*  595 */     super(paramContextStack, paramInt);
/*  596 */     this.classDef = paramClassDefinition;
/*  597 */     this.classDecl = paramClassDefinition.getClassDeclaration();
/*  598 */     this.interfaces = new InterfaceType[0];
/*  599 */     this.methods = new Method[0];
/*  600 */     this.members = new Member[0];
/*      */ 
/*  604 */     if (paramClassDefinition.isInnerClass()) {
/*  605 */       setTypeCode(paramInt | 0x80000000);
/*      */     }
/*      */ 
/*  610 */     setFlags();
/*      */   }
/*      */ 
/*      */   private void setFlags()
/*      */   {
/*      */     try
/*      */     {
/*  619 */       this.isCORBAObject = this.env.defCorbaObject.implementedBy(this.env, this.classDecl);
/*  620 */       this.isIDLEntity = this.env.defIDLEntity.implementedBy(this.env, this.classDecl);
/*  621 */       this.isValueBase = this.env.defValueBase.implementedBy(this.env, this.classDecl);
/*  622 */       this.isAbstractBase = ((isInterface()) && (this.isIDLEntity) && (!this.isValueBase) && (!this.isCORBAObject));
/*      */ 
/*  626 */       this.isCORBAUserException = (this.classDecl.getName() == idCorbaUserException);
/*      */ 
/*  630 */       if (this.env.defThrowable.implementedBy(this.env, this.classDecl))
/*      */       {
/*  634 */         this.isException = true;
/*      */ 
/*  638 */         if ((this.env.defRuntimeException.implementedBy(this.env, this.classDecl)) || 
/*  639 */           (this.env.defError
/*  639 */           .implementedBy(this.env, this.classDecl)))
/*      */         {
/*  640 */           this.isCheckedException = false;
/*      */         }
/*  642 */         else this.isCheckedException = true;
/*      */ 
/*  647 */         if (this.env.defRemoteException.implementedBy(this.env, this.classDecl))
/*  648 */           this.isRemoteExceptionOrSubclass = true;
/*      */         else
/*  650 */           this.isRemoteExceptionOrSubclass = false;
/*      */       }
/*      */       else {
/*  653 */         this.isException = false;
/*      */       }
/*      */     } catch (ClassNotFound localClassNotFound) {
/*  656 */       classNotFound(this.stack, localClassNotFound);
/*      */     }
/*      */   }
/*      */ 
/*      */   protected CompoundType(ContextStack paramContextStack, ClassDefinition paramClassDefinition, int paramInt)
/*      */   {
/*  666 */     super(paramContextStack, paramInt);
/*  667 */     this.classDef = paramClassDefinition;
/*  668 */     this.classDecl = paramClassDefinition.getClassDeclaration();
/*      */ 
/*  672 */     if (paramClassDefinition.isInnerClass()) {
/*  673 */       setTypeCode(paramInt | 0x80000000);
/*      */     }
/*      */ 
/*  678 */     setFlags();
/*      */ 
/*  682 */     Identifier localIdentifier = paramClassDefinition.getName();
/*      */     try
/*      */     {
/*  690 */       String str = IDLNames.getClassOrInterfaceName(localIdentifier, this.env);
/*  691 */       String[] arrayOfString = IDLNames.getModuleNames(localIdentifier, isBoxed(), this.env);
/*      */ 
/*  693 */       setNames(localIdentifier, arrayOfString, str);
/*      */ 
/*  697 */       if (isException())
/*      */       {
/*  701 */         this.isException = true;
/*  702 */         this.idlExceptionName = IDLNames.getExceptionName(getIDLName());
/*  703 */         this.qualifiedIDLExceptionName = 
/*  704 */           IDLNames.getQualifiedName(getIDLModuleNames(), this.idlExceptionName);
/*      */       }
/*      */ 
/*  709 */       this.interfaces = null;
/*  710 */       this.methods = null;
/*  711 */       this.members = null;
/*      */     }
/*      */     catch (Exception localException) {
/*  714 */       failedConstraint(7, false, paramContextStack, localIdentifier.toString(), localException.getMessage());
/*  715 */       throw new CompilerError("");
/*      */     }
/*      */   }
/*      */ 
/*      */   protected boolean initialize(Vector paramVector1, Vector paramVector2, Vector paramVector3, ContextStack paramContextStack, boolean paramBoolean)
/*      */   {
/*  728 */     boolean bool = true;
/*      */ 
/*  732 */     if ((paramVector1 != null) && (paramVector1.size() > 0)) {
/*  733 */       this.interfaces = new InterfaceType[paramVector1.size()];
/*  734 */       paramVector1.copyInto(this.interfaces);
/*      */     } else {
/*  736 */       this.interfaces = new InterfaceType[0];
/*      */     }
/*      */ 
/*  739 */     if ((paramVector2 != null) && (paramVector2.size() > 0)) {
/*  740 */       this.methods = new Method[paramVector2.size()];
/*  741 */       paramVector2.copyInto(this.methods);
/*      */       try
/*      */       {
/*  746 */         IDLNames.setMethodNames(this, this.methods, this.env);
/*      */       } catch (Exception localException1) {
/*  748 */         failedConstraint(13, paramBoolean, paramContextStack, getQualifiedName(), localException1.getMessage());
/*  749 */         bool = false;
/*      */       }
/*      */     }
/*      */     else {
/*  753 */       this.methods = new Method[0];
/*      */     }
/*      */ 
/*  756 */     if ((paramVector3 != null) && (paramVector3.size() > 0)) {
/*  757 */       this.members = new Member[paramVector3.size()];
/*  758 */       paramVector3.copyInto(this.members);
/*      */ 
/*  763 */       for (int i = 0; i < this.members.length; i++) {
/*  764 */         if (this.members[i].isInnerClassDeclaration()) {
/*      */           try {
/*  766 */             this.members[i].init(paramContextStack, this);
/*      */           } catch (CompilerError localCompilerError) {
/*  768 */             return false;
/*      */           }
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/*      */       try
/*      */       {
/*  776 */         IDLNames.setMemberNames(this, this.members, this.methods, this.env);
/*      */       } catch (Exception localException2) {
/*  778 */         int j = this.classDef.isInterface() ? 19 : 20;
/*  779 */         failedConstraint(j, paramBoolean, paramContextStack, getQualifiedName(), localException2.getMessage());
/*  780 */         bool = false;
/*      */       }
/*      */     }
/*      */     else {
/*  784 */       this.members = new Member[0];
/*      */     }
/*      */ 
/*  789 */     if (bool) {
/*  790 */       bool = setRepositoryID();
/*      */     }
/*      */ 
/*  793 */     return bool;
/*      */   }
/*      */ 
/*      */   protected static Type makeType(sun.tools.java.Type paramType, ClassDefinition paramClassDefinition, ContextStack paramContextStack)
/*      */   {
/*  803 */     if (paramContextStack.anyErrors()) return null;
/*      */ 
/*  807 */     String str = paramType.toString();
/*      */ 
/*  809 */     Object localObject = getType(str, paramContextStack);
/*      */ 
/*  811 */     if (localObject != null) {
/*  812 */       return localObject;
/*      */     }
/*      */ 
/*  817 */     localObject = getType(str + paramContextStack.getContextCodeString(), paramContextStack);
/*      */ 
/*  819 */     if (localObject != null) {
/*  820 */       return localObject;
/*      */     }
/*      */ 
/*  825 */     BatchEnvironment localBatchEnvironment = paramContextStack.getEnv();
/*  826 */     int i = paramType.getTypeCode();
/*  827 */     switch (i)
/*      */     {
/*      */     case 0:
/*      */     case 1:
/*      */     case 2:
/*      */     case 3:
/*      */     case 4:
/*      */     case 5:
/*      */     case 6:
/*      */     case 7:
/*  839 */       localObject = PrimitiveType.forPrimitive(paramType, paramContextStack);
/*  840 */       break;
/*      */     case 9:
/*  847 */       localObject = ArrayType.forArray(paramType, paramContextStack);
/*  848 */       break;
/*      */     case 10:
/*      */       try
/*      */       {
/*  856 */         ClassDefinition localClassDefinition = paramClassDefinition;
/*      */ 
/*  858 */         if (localClassDefinition == null) {
/*  859 */           localClassDefinition = localBatchEnvironment.getClassDeclaration(paramType).getClassDefinition(localBatchEnvironment);
/*      */         }
/*      */ 
/*  864 */         if (localClassDefinition.isInterface())
/*      */         {
/*  868 */           localObject = SpecialInterfaceType.forSpecial(localClassDefinition, paramContextStack);
/*      */ 
/*  870 */           if (localObject == null)
/*      */           {
/*  874 */             if (localBatchEnvironment.defRemote.implementedBy(localBatchEnvironment, localClassDefinition.getClassDeclaration()))
/*      */             {
/*  879 */               boolean bool1 = paramContextStack.isParentAValue();
/*  880 */               localObject = RemoteType.forRemote(localClassDefinition, paramContextStack, bool1);
/*      */ 
/*  885 */               if ((localObject == null) && (bool1)) {
/*  886 */                 localObject = NCInterfaceType.forNCInterface(localClassDefinition, paramContextStack);
/*      */               }
/*      */ 
/*      */             }
/*      */             else
/*      */             {
/*  892 */               localObject = AbstractType.forAbstract(localClassDefinition, paramContextStack, true);
/*      */ 
/*  894 */               if (localObject == null)
/*      */               {
/*  898 */                 localObject = NCInterfaceType.forNCInterface(localClassDefinition, paramContextStack);
/*      */               }
/*      */             }
/*      */           }
/*      */ 
/*      */         }
/*      */         else
/*      */         {
/*  906 */           localObject = SpecialClassType.forSpecial(localClassDefinition, paramContextStack);
/*      */ 
/*  908 */           if (localObject == null)
/*      */           {
/*  910 */             ClassDeclaration localClassDeclaration = localClassDefinition.getClassDeclaration();
/*      */ 
/*  914 */             if (localBatchEnvironment.defRemote.implementedBy(localBatchEnvironment, localClassDeclaration))
/*      */             {
/*  919 */               boolean bool2 = paramContextStack.isParentAValue();
/*  920 */               localObject = ImplementationType.forImplementation(localClassDefinition, paramContextStack, bool2);
/*      */ 
/*  925 */               if ((localObject == null) && (bool2)) {
/*  926 */                 localObject = NCClassType.forNCClass(localClassDefinition, paramContextStack);
/*      */               }
/*      */ 
/*      */             }
/*      */             else
/*      */             {
/*  932 */               if (localBatchEnvironment.defSerializable.implementedBy(localBatchEnvironment, localClassDeclaration))
/*      */               {
/*  937 */                 localObject = ValueType.forValue(localClassDefinition, paramContextStack, true);
/*      */               }
/*      */ 
/*  940 */               if (localObject == null)
/*      */               {
/*  944 */                 localObject = NCClassType.forNCClass(localClassDefinition, paramContextStack);
/*      */               }
/*      */             }
/*      */           }
/*      */         }
/*      */       } catch (ClassNotFound localClassNotFound) {
/*  950 */         classNotFound(paramContextStack, localClassNotFound);
/*      */       }
/*      */ 
/*      */     case 8:
/*      */     default:
/*  955 */       throw new CompilerError("Unknown typecode (" + i + ") for " + paramType.getTypeSignature());
/*      */     }
/*      */ 
/*  958 */     return localObject;
/*      */   }
/*      */ 
/*      */   public static boolean isRemoteException(ClassType paramClassType, BatchEnvironment paramBatchEnvironment)
/*      */   {
/*  966 */     sun.tools.java.Type localType = paramClassType.getClassDeclaration().getType();
/*      */ 
/*  968 */     if ((localType.equals(paramBatchEnvironment.typeRemoteException)) || 
/*  969 */       (localType
/*  969 */       .equals(paramBatchEnvironment.typeIOException)) || 
/*  970 */       (localType
/*  970 */       .equals(paramBatchEnvironment.typeException)) || 
/*  971 */       (localType
/*  971 */       .equals(paramBatchEnvironment.typeThrowable)))
/*      */     {
/*  973 */       return true;
/*      */     }
/*  975 */     return false;
/*      */   }
/*      */ 
/*      */   protected boolean isConformingRemoteMethod(Method paramMethod, boolean paramBoolean)
/*      */     throws ClassNotFound
/*      */   {
/*  987 */     int i = 0;
/*  988 */     ValueType[] arrayOfValueType = paramMethod.getExceptions();
/*      */ 
/*  990 */     for (int j = 0; j < arrayOfValueType.length; j++)
/*      */     {
/*  994 */       if (isRemoteException(arrayOfValueType[j], this.env))
/*      */       {
/*  998 */         i = 1;
/*  999 */         break;
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/* 1005 */     if (i == 0)
/*      */     {
/* 1009 */       failedConstraint(5, paramBoolean, this.stack, paramMethod.getEnclosing(), paramMethod.toString());
/*      */     }
/*      */ 
/* 1015 */     j = !isIDLEntityException(paramMethod.getReturnType(), paramMethod, paramBoolean) ? 1 : 0;
/* 1016 */     if (j != 0) {
/* 1017 */       Type[] arrayOfType = paramMethod.getArguments();
/* 1018 */       for (int k = 0; k < arrayOfType.length; k++) {
/* 1019 */         if (isIDLEntityException(arrayOfType[k], paramMethod, paramBoolean)) {
/* 1020 */           j = 0;
/* 1021 */           break;
/*      */         }
/*      */       }
/*      */     }
/*      */ 
/* 1026 */     return (i != 0) && (j != 0);
/*      */   }
/*      */ 
/*      */   protected boolean isIDLEntityException(Type paramType, Method paramMethod, boolean paramBoolean) throws ClassNotFound
/*      */   {
/* 1031 */     if (paramType.isArray()) {
/* 1032 */       paramType = paramType.getElementType();
/*      */     }
/* 1034 */     if ((paramType.isCompound()) && 
/* 1035 */       (((CompoundType)paramType).isIDLEntityException())) {
/* 1036 */       failedConstraint(18, paramBoolean, this.stack, paramMethod.getEnclosing(), paramMethod.toString());
/* 1037 */       return true;
/*      */     }
/*      */ 
/* 1040 */     return false;
/*      */   }
/*      */ 
/*      */   protected void swapInvalidTypes()
/*      */   {
/* 1050 */     for (int i = 0; i < this.interfaces.length; i++) {
/* 1051 */       if (this.interfaces[i].getStatus() != 1) {
/* 1052 */         this.interfaces[i] = ((InterfaceType)getValidType(this.interfaces[i]));
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/* 1058 */     for (i = 0; i < this.methods.length; i++) {
/* 1059 */       this.methods[i].swapInvalidTypes();
/*      */     }
/*      */ 
/* 1064 */     for (i = 0; i < this.members.length; i++)
/* 1065 */       this.members[i].swapInvalidTypes();
/*      */   }
/*      */ 
/*      */   protected boolean addTypes(int paramInt, HashSet paramHashSet, Vector paramVector)
/*      */   {
/* 1079 */     boolean bool = super.addTypes(paramInt, paramHashSet, paramVector);
/*      */ 
/* 1083 */     if (bool)
/*      */     {
/* 1087 */       ClassType localClassType = getSuperclass();
/*      */ 
/* 1089 */       if (localClassType != null) {
/* 1090 */         localClassType.addTypes(paramInt, paramHashSet, paramVector);
/*      */       }
/*      */ 
/* 1096 */       for (int i = 0; i < this.interfaces.length; i++)
/*      */       {
/* 1101 */         this.interfaces[i].addTypes(paramInt, paramHashSet, paramVector);
/*      */       }
/*      */       Object localObject1;
/* 1107 */       for (i = 0; i < this.methods.length; i++)
/*      */       {
/* 1112 */         this.methods[i].getReturnType().addTypes(paramInt, paramHashSet, paramVector);
/*      */ 
/* 1116 */         localObject1 = this.methods[i].getArguments();
/*      */ 
/* 1119 */         for (int j = 0; j < localObject1.length; j++)
/*      */         {
/* 1121 */           Object localObject2 = localObject1[j];
/*      */ 
/* 1126 */           localObject2.addTypes(paramInt, paramHashSet, paramVector);
/*      */         }
/*      */ 
/* 1131 */         ValueType[] arrayOfValueType = this.methods[i].getExceptions();
/*      */ 
/* 1134 */         for (int k = 0; k < arrayOfValueType.length; k++)
/*      */         {
/* 1136 */           ValueType localValueType = arrayOfValueType[k];
/*      */ 
/* 1140 */           localValueType.addTypes(paramInt, paramHashSet, paramVector);
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/* 1147 */       for (i = 0; i < this.members.length; i++)
/*      */       {
/* 1149 */         localObject1 = this.members[i].getType();
/*      */ 
/* 1154 */         ((Type)localObject1).addTypes(paramInt, paramHashSet, paramVector);
/*      */       }
/*      */     }
/*      */ 
/* 1158 */     return bool;
/*      */   }
/*      */ 
/*      */   private boolean isConformingConstantType(MemberDefinition paramMemberDefinition)
/*      */   {
/* 1165 */     return isConformingConstantType(paramMemberDefinition.getType(), paramMemberDefinition);
/*      */   }
/*      */ 
/*      */   private boolean isConformingConstantType(sun.tools.java.Type paramType, MemberDefinition paramMemberDefinition)
/*      */   {
/* 1175 */     boolean bool = true;
/* 1176 */     int i = paramType.getTypeCode();
/* 1177 */     switch (i)
/*      */     {
/*      */     case 0:
/*      */     case 1:
/*      */     case 2:
/*      */     case 3:
/*      */     case 4:
/*      */     case 5:
/*      */     case 6:
/*      */     case 7:
/* 1187 */       break;
/*      */     case 10:
/* 1192 */       if (paramType.getClassName() != idJavaLangString) {
/* 1193 */         failedConstraint(3, false, this.stack, paramMemberDefinition.getClassDefinition(), paramMemberDefinition.getName());
/* 1194 */         bool = false; } break;
/*      */     case 9:
/* 1201 */       failedConstraint(3, false, this.stack, paramMemberDefinition.getClassDefinition(), paramMemberDefinition.getName());
/* 1202 */       bool = false;
/* 1203 */       break;
/*      */     case 8:
/*      */     default:
/* 1207 */       throw new Error("unexpected type code: " + i);
/*      */     }
/*      */ 
/* 1210 */     return bool;
/*      */   }
/*      */ 
/*      */   protected Vector updateParentClassMethods(ClassDefinition paramClassDefinition, Vector paramVector, boolean paramBoolean, ContextStack paramContextStack)
/*      */     throws ClassNotFound
/*      */   {
/* 1231 */     ClassDeclaration localClassDeclaration = paramClassDefinition.getSuperClass(this.env);
/*      */ 
/* 1233 */     while (localClassDeclaration != null)
/*      */     {
/* 1235 */       ClassDefinition localClassDefinition = localClassDeclaration.getClassDefinition(this.env);
/* 1236 */       Identifier localIdentifier = localClassDeclaration.getName();
/*      */ 
/* 1238 */       if (localIdentifier == idJavaLangObject)
/*      */       {
/*      */         break;
/*      */       }
/*      */ 
/* 1243 */       for (MemberDefinition localMemberDefinition = localClassDefinition.getFirstMember(); 
/* 1244 */         localMemberDefinition != null; 
/* 1245 */         localMemberDefinition = localMemberDefinition.getNextMember())
/*      */       {
/* 1247 */         if ((localMemberDefinition.isMethod()) && 
/* 1248 */           (!localMemberDefinition
/* 1248 */           .isInitializer()) && 
/* 1249 */           (!localMemberDefinition
/* 1249 */           .isConstructor()) && 
/* 1250 */           (!localMemberDefinition
/* 1250 */           .isPrivate()))
/*      */         {
/*      */           Method localMethod1;
/*      */           try
/*      */           {
/* 1256 */             localMethod1 = new Method(this, localMemberDefinition, paramBoolean, paramContextStack);
/*      */           }
/*      */           catch (Exception localException) {
/* 1259 */             return null;
/*      */           }
/*      */ 
/* 1264 */           int i = paramVector.indexOf(localMethod1);
/* 1265 */           if (i >= 0)
/*      */           {
/* 1269 */             Method localMethod2 = (Method)paramVector.elementAt(i);
/* 1270 */             localMethod2.setDeclaredBy(localIdentifier);
/*      */           } else {
/* 1272 */             paramVector.addElement(localMethod1);
/*      */           }
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/* 1278 */       localClassDeclaration = localClassDefinition.getSuperClass(this.env);
/*      */     }
/*      */ 
/* 1281 */     return paramVector;
/*      */   }
/*      */ 
/*      */   protected Vector addAllMethods(ClassDefinition paramClassDefinition, Vector paramVector, boolean paramBoolean1, boolean paramBoolean2, ContextStack paramContextStack)
/*      */     throws ClassNotFound
/*      */   {
/* 1305 */     ClassDeclaration[] arrayOfClassDeclaration = paramClassDefinition.getInterfaces();
/*      */     Object localObject1;
/* 1311 */     for (int i = 0; i < arrayOfClassDeclaration.length; i++)
/*      */     {
/* 1313 */       localObject1 = addAllMethods(arrayOfClassDeclaration[i].getClassDefinition(this.env), paramVector, paramBoolean1, paramBoolean2, paramContextStack);
/*      */ 
/* 1316 */       if (localObject1 == null) {
/* 1317 */         return null;
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/* 1324 */     for (MemberDefinition localMemberDefinition = paramClassDefinition.getFirstMember(); 
/* 1325 */       localMemberDefinition != null; 
/* 1326 */       localMemberDefinition = localMemberDefinition.getNextMember())
/*      */     {
/* 1328 */       if ((localMemberDefinition.isMethod()) && 
/* 1329 */         (!localMemberDefinition
/* 1329 */         .isInitializer()) && 
/* 1330 */         (!localMemberDefinition
/* 1330 */         .isPrivate()))
/*      */       {
/*      */         try
/*      */         {
/* 1336 */           localObject1 = new Method(this, localMemberDefinition, paramBoolean2, paramContextStack);
/*      */         }
/*      */         catch (Exception localException) {
/* 1339 */           return null;
/*      */         }
/*      */ 
/* 1344 */         if (!paramVector.contains(localObject1))
/*      */         {
/* 1348 */           paramVector.addElement(localObject1);
/*      */         }
/*      */         else
/*      */         {
/* 1355 */           if ((paramBoolean1) && (paramClassDefinition != this.classDef) && 
/* 1356 */             (!paramContextStack
/* 1356 */             .isParentAValue()) && (!paramContextStack.getContext().isValue()))
/*      */           {
/* 1360 */             Method localMethod = (Method)paramVector.elementAt(paramVector.indexOf(localObject1));
/* 1361 */             localObject2 = localMethod.getMemberDefinition().getClassDefinition();
/*      */ 
/* 1366 */             if ((paramClassDefinition != localObject2) && 
/* 1367 */               (!inheritsFrom(paramClassDefinition, (ClassDefinition)localObject2)) && 
/* 1368 */               (!inheritsFrom((ClassDefinition)localObject2, paramClassDefinition)))
/*      */             {
/* 1373 */               localObject3 = ((ClassDefinition)localObject2).getName() + " and " + paramClassDefinition.getName();
/* 1374 */               failedConstraint(6, paramBoolean2, paramContextStack, this.classDef, localObject3, localObject1);
/* 1375 */               return null;
/*      */             }
/*      */ 
/*      */           }
/*      */ 
/* 1382 */           int j = paramVector.indexOf(localObject1);
/* 1383 */           Object localObject2 = (Method)paramVector.get(j);
/*      */ 
/* 1388 */           Object localObject3 = ((Method)localObject1).mergeWith((Method)localObject2);
/*      */ 
/* 1391 */           paramVector.set(j, localObject3);
/*      */         }
/*      */       }
/*      */     }
/*      */ 
/* 1396 */     return paramVector;
/*      */   }
/*      */ 
/*      */   protected boolean inheritsFrom(ClassDefinition paramClassDefinition1, ClassDefinition paramClassDefinition2)
/*      */   {
/* 1402 */     if (paramClassDefinition1 == paramClassDefinition2)
/* 1403 */       return true;
/*      */     ClassDefinition localClassDefinition;
/* 1406 */     if (paramClassDefinition1.getSuperClass() != null) {
/* 1407 */       localClassDefinition = paramClassDefinition1.getSuperClass().getClassDefinition();
/* 1408 */       if (inheritsFrom(localClassDefinition, paramClassDefinition2)) {
/* 1409 */         return true;
/*      */       }
/*      */     }
/* 1412 */     ClassDeclaration[] arrayOfClassDeclaration = paramClassDefinition1.getInterfaces();
/* 1413 */     for (int i = 0; i < arrayOfClassDeclaration.length; i++) {
/* 1414 */       localClassDefinition = arrayOfClassDeclaration[i].getClassDefinition();
/* 1415 */       if (inheritsFrom(localClassDefinition, paramClassDefinition2))
/* 1416 */         return true;
/*      */     }
/* 1418 */     return false;
/*      */   }
/*      */ 
/*      */   protected Vector addRemoteInterfaces(Vector paramVector, boolean paramBoolean, ContextStack paramContextStack)
/*      */     throws ClassNotFound
/*      */   {
/* 1431 */     ClassDefinition localClassDefinition1 = getClassDefinition();
/* 1432 */     ClassDeclaration[] arrayOfClassDeclaration = localClassDefinition1.getInterfaces();
/*      */ 
/* 1434 */     paramContextStack.setNewContextCode(10);
/*      */ 
/* 1436 */     for (int i = 0; i < arrayOfClassDeclaration.length; i++)
/*      */     {
/* 1438 */       ClassDefinition localClassDefinition2 = arrayOfClassDeclaration[i].getClassDefinition(this.env);
/*      */ 
/* 1442 */       Object localObject = SpecialInterfaceType.forSpecial(localClassDefinition2, paramContextStack);
/*      */ 
/* 1444 */       if (localObject == null)
/*      */       {
/* 1448 */         if (this.env.defRemote.implementedBy(this.env, arrayOfClassDeclaration[i]))
/*      */         {
/* 1452 */           localObject = RemoteType.forRemote(localClassDefinition2, paramContextStack, false);
/*      */         }
/*      */         else
/*      */         {
/* 1458 */           localObject = AbstractType.forAbstract(localClassDefinition2, paramContextStack, true);
/*      */ 
/* 1460 */           if ((localObject == null) && (paramBoolean))
/*      */           {
/* 1464 */             localObject = NCInterfaceType.forNCInterface(localClassDefinition2, paramContextStack);
/*      */           }
/*      */         }
/*      */       }
/*      */ 
/* 1469 */       if (localObject != null)
/* 1470 */         paramVector.addElement(localObject);
/*      */       else {
/* 1472 */         return null;
/*      */       }
/*      */     }
/*      */ 
/* 1476 */     return paramVector;
/*      */   }
/*      */ 
/*      */   protected Vector addNonRemoteInterfaces(Vector paramVector, ContextStack paramContextStack)
/*      */     throws ClassNotFound
/*      */   {
/* 1488 */     ClassDefinition localClassDefinition1 = getClassDefinition();
/* 1489 */     ClassDeclaration[] arrayOfClassDeclaration = localClassDefinition1.getInterfaces();
/*      */ 
/* 1491 */     paramContextStack.setNewContextCode(10);
/*      */ 
/* 1493 */     for (int i = 0; i < arrayOfClassDeclaration.length; i++)
/*      */     {
/* 1495 */       ClassDefinition localClassDefinition2 = arrayOfClassDeclaration[i].getClassDefinition(this.env);
/*      */ 
/* 1499 */       Object localObject = SpecialInterfaceType.forSpecial(localClassDefinition2, paramContextStack);
/*      */ 
/* 1501 */       if (localObject == null)
/*      */       {
/* 1505 */         localObject = AbstractType.forAbstract(localClassDefinition2, paramContextStack, true);
/*      */ 
/* 1507 */         if (localObject == null)
/*      */         {
/* 1511 */           localObject = NCInterfaceType.forNCInterface(localClassDefinition2, paramContextStack);
/*      */         }
/*      */       }
/*      */ 
/* 1515 */       if (localObject != null)
/* 1516 */         paramVector.addElement(localObject);
/*      */       else {
/* 1518 */         return null;
/*      */       }
/*      */     }
/*      */ 
/* 1522 */     return paramVector;
/*      */   }
/*      */ 
/*      */   protected boolean addAllMembers(Vector paramVector, boolean paramBoolean1, boolean paramBoolean2, ContextStack paramContextStack)
/*      */   {
/* 1535 */     boolean bool = true;
/*      */ 
/* 1539 */     for (MemberDefinition localMemberDefinition = getClassDefinition().getFirstMember(); 
/* 1540 */       (localMemberDefinition != null) && (bool); 
/* 1541 */       localMemberDefinition = localMemberDefinition.getNextMember())
/*      */     {
/* 1543 */       if (!localMemberDefinition.isMethod()) {
/*      */         try
/*      */         {
/* 1546 */           String str = null;
/*      */ 
/* 1550 */           localMemberDefinition.getValue(this.env);
/*      */ 
/* 1554 */           Node localNode = localMemberDefinition.getValue();
/*      */           Object localObject;
/* 1556 */           if (localNode != null)
/*      */           {
/* 1560 */             if (localMemberDefinition.getType().getTypeCode() == 2) {
/* 1561 */               localObject = (Integer)((IntegerExpression)localNode).getValue();
/* 1562 */               str = "L'" + String.valueOf((char)((Integer)localObject).intValue()) + "'";
/*      */             } else {
/* 1564 */               str = localNode.toString();
/*      */             }
/*      */ 
/*      */           }
/*      */ 
/* 1570 */           if ((paramBoolean1) && (localMemberDefinition.getInnerClass() == null))
/*      */           {
/* 1574 */             if ((str == null) || (!isConformingConstantType(localMemberDefinition))) {
/* 1575 */               failedConstraint(3, paramBoolean2, paramContextStack, localMemberDefinition.getClassDefinition(), localMemberDefinition.getName());
/* 1576 */               bool = false;
/* 1577 */               break;
/*      */             }
/*      */ 
/*      */           }
/*      */ 
/*      */           try
/*      */           {
/* 1584 */             localObject = new Member(localMemberDefinition, str, paramContextStack, this);
/* 1585 */             paramVector.addElement(localObject);
/*      */           } catch (CompilerError localCompilerError) {
/* 1587 */             bool = false;
/*      */           }
/*      */         }
/*      */         catch (ClassNotFound localClassNotFound) {
/* 1591 */           classNotFound(paramContextStack, localClassNotFound);
/* 1592 */           bool = false;
/*      */         }
/*      */       }
/*      */     }
/*      */ 
/* 1597 */     return bool;
/*      */   }
/*      */ 
/*      */   protected boolean addConformingConstants(Vector paramVector, boolean paramBoolean, ContextStack paramContextStack)
/*      */   {
/* 1607 */     boolean bool = true;
/*      */ 
/* 1611 */     for (MemberDefinition localMemberDefinition = getClassDefinition().getFirstMember(); 
/* 1612 */       (localMemberDefinition != null) && (bool); 
/* 1613 */       localMemberDefinition = localMemberDefinition.getNextMember())
/*      */     {
/* 1615 */       if (!localMemberDefinition.isMethod()) {
/*      */         try
/*      */         {
/* 1618 */           String str = null;
/*      */ 
/* 1622 */           localMemberDefinition.getValue(this.env);
/*      */ 
/* 1626 */           Node localNode = localMemberDefinition.getValue();
/*      */ 
/* 1628 */           if (localNode != null) {
/* 1629 */             str = localNode.toString();
/*      */           }
/*      */ 
/* 1635 */           if (str != null)
/*      */           {
/* 1639 */             if (!isConformingConstantType(localMemberDefinition)) {
/* 1640 */               failedConstraint(3, paramBoolean, paramContextStack, localMemberDefinition.getClassDefinition(), localMemberDefinition.getName());
/* 1641 */               bool = false;
/* 1642 */               break;
/*      */             }
/*      */ 
/*      */             try
/*      */             {
/* 1648 */               Member localMember = new Member(localMemberDefinition, str, paramContextStack, this);
/* 1649 */               paramVector.addElement(localMember);
/*      */             } catch (CompilerError localCompilerError) {
/* 1651 */               bool = false;
/*      */             }
/*      */           }
/*      */         } catch (ClassNotFound localClassNotFound) {
/* 1655 */           classNotFound(paramContextStack, localClassNotFound);
/* 1656 */           bool = false;
/*      */         }
/*      */       }
/*      */     }
/*      */ 
/* 1661 */     return bool;
/*      */   }
/*      */ 
/*      */   protected ValueType[] getMethodExceptions(MemberDefinition paramMemberDefinition, boolean paramBoolean, ContextStack paramContextStack)
/*      */     throws Exception
/*      */   {
/* 1668 */     int i = 1;
/* 1669 */     paramContextStack.setNewContextCode(5);
/* 1670 */     ClassDeclaration[] arrayOfClassDeclaration = paramMemberDefinition.getExceptions(this.env);
/* 1671 */     Object localObject = new ValueType[arrayOfClassDeclaration.length];
/*      */     try
/*      */     {
/* 1674 */       for (int j = 0; j < arrayOfClassDeclaration.length; j++) {
/* 1675 */         ClassDefinition localClassDefinition = arrayOfClassDeclaration[j].getClassDefinition(this.env);
/*      */         try {
/* 1677 */           ValueType localValueType = ValueType.forValue(localClassDefinition, paramContextStack, false);
/* 1678 */           if (localValueType != null)
/* 1679 */             localObject[j] = localValueType;
/*      */           else
/* 1681 */             i = 0;
/*      */         }
/*      */         catch (ClassCastException localClassCastException) {
/* 1684 */           failedConstraint(22, paramBoolean, paramContextStack, getQualifiedName());
/* 1685 */           throw new CompilerError("Method: exception " + localClassDefinition.getName() + " not a class type!");
/*      */         } catch (NullPointerException localNullPointerException) {
/* 1687 */           failedConstraint(23, paramBoolean, paramContextStack, getQualifiedName());
/* 1688 */           throw new CompilerError("Method: caught null pointer exception");
/*      */         }
/*      */       }
/*      */     } catch (ClassNotFound localClassNotFound) {
/* 1692 */       classNotFound(paramBoolean, paramContextStack, localClassNotFound);
/* 1693 */       i = 0;
/*      */     }
/*      */ 
/* 1696 */     if (i == 0) {
/* 1697 */       throw new Exception();
/*      */     }
/*      */ 
/* 1703 */     int k = 0;
/* 1704 */     for (int m = 0; m < localObject.length; m++) {
/* 1705 */       for (int n = 0; n < localObject.length; n++) {
/* 1706 */         if ((m != n) && (localObject[m] != null) && (localObject[m] == localObject[n])) {
/* 1707 */           localObject[n] = null;
/* 1708 */           k++;
/*      */         }
/*      */       }
/*      */     }
/* 1712 */     if (k > 0) {
/* 1713 */       m = 0;
/* 1714 */       ValueType[] arrayOfValueType = new ValueType[localObject.length - k];
/* 1715 */       for (int i1 = 0; i1 < localObject.length; i1++) {
/* 1716 */         if (localObject[i1] != null) {
/* 1717 */           arrayOfValueType[(m++)] = localObject[i1];
/*      */         }
/*      */       }
/* 1720 */       localObject = arrayOfValueType;
/*      */     }
/*      */ 
/* 1723 */     return localObject;
/*      */   }
/*      */ 
/*      */   protected static String getVisibilityString(MemberDefinition paramMemberDefinition)
/*      */   {
/* 1728 */     String str1 = "";
/* 1729 */     String str2 = "";
/*      */ 
/* 1731 */     if (paramMemberDefinition.isPublic()) {
/* 1732 */       str1 = str1 + "public";
/* 1733 */       str2 = " ";
/* 1734 */     } else if (paramMemberDefinition.isProtected()) {
/* 1735 */       str1 = str1 + "protected";
/* 1736 */       str2 = " ";
/* 1737 */     } else if (paramMemberDefinition.isPrivate()) {
/* 1738 */       str1 = str1 + "private";
/* 1739 */       str2 = " ";
/*      */     }
/*      */ 
/* 1742 */     if (paramMemberDefinition.isStatic()) {
/* 1743 */       str1 = str1 + str2;
/* 1744 */       str1 = str1 + "static";
/* 1745 */       str2 = " ";
/*      */     }
/*      */ 
/* 1748 */     if (paramMemberDefinition.isFinal()) {
/* 1749 */       str1 = str1 + str2;
/* 1750 */       str1 = str1 + "final";
/* 1751 */       str2 = " ";
/*      */     }
/*      */ 
/* 1754 */     return str1;
/*      */   }
/*      */ 
/*      */   protected boolean assertNotImpl(Type paramType, boolean paramBoolean1, ContextStack paramContextStack, CompoundType paramCompoundType, boolean paramBoolean2)
/*      */   {
/* 1763 */     if (paramType.isType(65536)) {
/* 1764 */       int i = paramBoolean2 ? 28 : 21;
/* 1765 */       failedConstraint(i, paramBoolean1, paramContextStack, paramType, paramCompoundType.getName());
/* 1766 */       return false;
/*      */     }
/* 1768 */     return true;
/*      */   }
/*      */ 
/*      */   public class Member
/*      */     implements ContextElement, Cloneable
/*      */   {
/*      */     private Type type;
/*      */     private String vis;
/*      */     private String value;
/*      */     private String name;
/*      */     private String idlName;
/*      */     private boolean innerClassDecl;
/*      */     private boolean constant;
/*      */     private MemberDefinition member;
/*      */     private boolean forceTransient;
/*      */ 
/*      */     public String getElementName()
/*      */     {
/* 2468 */       return "\"" + getName() + "\"";
/*      */     }
/*      */ 
/*      */     public Type getType()
/*      */     {
/* 2475 */       return this.type;
/*      */     }
/*      */ 
/*      */     public String getName()
/*      */     {
/* 2482 */       return this.name;
/*      */     }
/*      */ 
/*      */     public String getIDLName()
/*      */     {
/* 2490 */       return this.idlName;
/*      */     }
/*      */ 
/*      */     public String getVisibility()
/*      */     {
/* 2497 */       return this.vis;
/*      */     }
/*      */ 
/*      */     public boolean isPublic()
/*      */     {
/* 2504 */       return this.member.isPublic();
/*      */     }
/*      */ 
/*      */     public boolean isPrivate() {
/* 2508 */       return this.member.isPrivate();
/*      */     }
/*      */ 
/*      */     public boolean isStatic() {
/* 2512 */       return this.member.isStatic();
/*      */     }
/*      */ 
/*      */     public boolean isFinal() {
/* 2516 */       return this.member.isFinal();
/*      */     }
/*      */ 
/*      */     public boolean isTransient() {
/* 2520 */       if (this.forceTransient) return true;
/* 2521 */       return this.member.isTransient();
/*      */     }
/*      */ 
/*      */     public String getValue()
/*      */     {
/* 2528 */       return this.value;
/*      */     }
/*      */ 
/*      */     public boolean isInnerClassDeclaration()
/*      */     {
/* 2536 */       return this.innerClassDecl;
/*      */     }
/*      */ 
/*      */     public boolean isConstant()
/*      */     {
/* 2543 */       return this.constant;
/*      */     }
/*      */ 
/*      */     public String toString()
/*      */     {
/* 2551 */       String str = this.type.toString();
/*      */ 
/* 2553 */       if (this.value != null) {
/* 2554 */         str = str + " = " + this.value;
/*      */       }
/*      */ 
/* 2557 */       return str;
/*      */     }
/*      */ 
/*      */     protected void swapInvalidTypes()
/*      */     {
/* 2564 */       if (this.type.getStatus() != 1)
/* 2565 */         this.type = CompoundType.this.getValidType(this.type);
/*      */     }
/*      */ 
/*      */     protected void setTransient()
/*      */     {
/* 2570 */       if (!isTransient()) {
/* 2571 */         this.forceTransient = true;
/* 2572 */         if (this.vis.length() > 0)
/* 2573 */           this.vis += " transient";
/*      */         else
/* 2575 */           this.vis = "transient";
/*      */       }
/*      */     }
/*      */ 
/*      */     protected MemberDefinition getMemberDefinition()
/*      */     {
/* 2581 */       return this.member;
/*      */     }
/*      */ 
/*      */     public void destroy()
/*      */     {
/* 2588 */       if (this.type != null) {
/* 2589 */         this.type.destroy();
/* 2590 */         this.type = null;
/* 2591 */         this.vis = null;
/* 2592 */         this.value = null;
/* 2593 */         this.name = null;
/* 2594 */         this.idlName = null;
/* 2595 */         this.member = null;
/*      */       }
/*      */     }
/*      */ 
/*      */     public Member(MemberDefinition paramString, String paramContextStack, ContextStack paramCompoundType, CompoundType arg5)
/*      */     {
/* 2616 */       this.member = paramString;
/* 2617 */       this.value = paramContextStack;
/* 2618 */       this.forceTransient = false;
/* 2619 */       this.innerClassDecl = (paramString.getInnerClass() != null);
/*      */ 
/* 2625 */       if (!this.innerClassDecl)
/*      */       {
/*      */         CompoundType localCompoundType;
/* 2626 */         init(paramCompoundType, localCompoundType);
/*      */       }
/*      */     }
/*      */ 
/*      */     public void init(ContextStack paramContextStack, CompoundType paramCompoundType)
/*      */     {
/* 2632 */       this.constant = false;
/* 2633 */       this.name = this.member.getName().toString();
/* 2634 */       this.vis = CompoundType.getVisibilityString(this.member);
/* 2635 */       this.idlName = null;
/*      */ 
/* 2639 */       int i = 6;
/* 2640 */       paramContextStack.setNewContextCode(i);
/*      */ 
/* 2644 */       if (this.member.isVariable()) {
/* 2645 */         if ((this.value != null) && (this.member.isConstant())) {
/* 2646 */           i = 7;
/* 2647 */           this.constant = true;
/* 2648 */         } else if (this.member.isStatic()) {
/* 2649 */           i = 8;
/* 2650 */         } else if (this.member.isTransient()) {
/* 2651 */           i = 9;
/*      */         }
/*      */       }
/*      */ 
/* 2655 */       paramContextStack.setNewContextCode(i);
/* 2656 */       paramContextStack.push(this);
/*      */ 
/* 2658 */       this.type = CompoundType.makeType(this.member.getType(), null, paramContextStack);
/*      */ 
/* 2660 */       if (this.type != null) { if (!this.innerClassDecl) { if ((this.member
/* 2662 */             .isStatic()) || 
/* 2663 */             (this.member
/* 2663 */             .isTransient()) || 
/* 2664 */             (CompoundType.this
/* 2664 */             .assertNotImpl(this.type, false, paramContextStack, paramCompoundType, true))); } } else { paramContextStack.pop(false);
/* 2666 */         throw new CompilerError("");
/*      */       }
/*      */ 
/* 2671 */       if ((this.constant) && (this.type.isPrimitive())) {
/* 2672 */         if ((this.type.isType(64)) || (this.type.isType(128)) || (this.type.isType(256))) {
/* 2673 */           int j = this.value.length();
/* 2674 */           char c = this.value.charAt(j - 1);
/* 2675 */           if (!Character.isDigit(c))
/* 2676 */             this.value = this.value.substring(0, j - 1);
/*      */         }
/* 2678 */         else if (this.type.isType(2)) {
/* 2679 */           this.value = this.value.toUpperCase();
/*      */         }
/*      */       }
/* 2682 */       if ((this.constant) && (this.type.isType(512))) {
/* 2683 */         this.value = ("L" + this.value);
/*      */       }
/* 2685 */       paramContextStack.pop(true);
/*      */     }
/*      */ 
/*      */     public void setIDLName(String paramString) {
/* 2689 */       this.idlName = paramString;
/*      */     }
/*      */ 
/*      */     protected Object clone()
/*      */     {
/*      */       try
/*      */       {
/* 2697 */         return super.clone(); } catch (CloneNotSupportedException localCloneNotSupportedException) {
/*      */       }
/* 2699 */       throw new Error("clone failed");
/*      */     }
/*      */   }
/*      */ 
/*      */   public class Method
/*      */     implements ContextElement, Cloneable
/*      */   {
/*      */     private MemberDefinition memberDef;
/*      */     private CompoundType enclosing;
/*      */     private ValueType[] exceptions;
/*      */     private ValueType[] implExceptions;
/*      */     private Type returnType;
/*      */     private Type[] arguments;
/*      */     private String[] argumentNames;
/*      */     private String vis;
/*      */     private String name;
/*      */     private String idlName;
/* 2337 */     private String stringRep = null;
/* 2338 */     private int attributeKind = 0;
/* 2339 */     private String attributeName = null;
/* 2340 */     private int attributePairIndex = -1;
/* 2341 */     private Identifier declaredBy = null;
/*      */ 
/*      */     public boolean isInherited()
/*      */     {
/* 1786 */       return this.declaredBy != this.enclosing.getIdentifier();
/*      */     }
/*      */ 
/*      */     public boolean isAttribute()
/*      */     {
/* 1794 */       return this.attributeKind != 0;
/*      */     }
/*      */ 
/*      */     public boolean isReadWriteAttribute()
/*      */     {
/* 1801 */       return (this.attributeKind == 3) || (this.attributeKind == 4);
/*      */     }
/*      */ 
/*      */     public int getAttributeKind()
/*      */     {
/* 1809 */       return this.attributeKind;
/*      */     }
/*      */ 
/*      */     public String getAttributeName()
/*      */     {
/* 1817 */       return this.attributeName;
/*      */     }
/*      */ 
/*      */     public int getAttributePairIndex()
/*      */     {
/* 1826 */       return this.attributePairIndex;
/*      */     }
/*      */ 
/*      */     public String getElementName()
/*      */     {
/* 1833 */       return this.memberDef.toString();
/*      */     }
/*      */ 
/*      */     public boolean equals(Object paramObject)
/*      */     {
/* 1840 */       Method localMethod = (Method)paramObject;
/*      */ 
/* 1842 */       if ((getName().equals(localMethod.getName())) && (this.arguments.length == localMethod.arguments.length))
/*      */       {
/* 1845 */         for (int i = 0; i < this.arguments.length; i++) {
/* 1846 */           if (!this.arguments[i].equals(localMethod.arguments[i])) {
/* 1847 */             return false;
/*      */           }
/*      */         }
/* 1850 */         return true;
/*      */       }
/* 1852 */       return false;
/*      */     }
/*      */ 
/*      */     public int hashCode() {
/* 1856 */       return getName().hashCode() ^ Arrays.hashCode(this.arguments);
/*      */     }
/*      */ 
/*      */     public Method mergeWith(Method paramMethod)
/*      */     {
/* 1868 */       if (!equals(paramMethod)) {
/* 1869 */         CompoundType.this.env.error(0L, "attempt to merge method failed:", getName(), this.enclosing
/* 1870 */           .getClassDefinition().getName());
/*      */       }
/*      */ 
/* 1873 */       Vector localVector = new Vector();
/*      */       try {
/* 1875 */         collectCompatibleExceptions(paramMethod.exceptions, this.exceptions, localVector);
/*      */ 
/* 1877 */         collectCompatibleExceptions(this.exceptions, paramMethod.exceptions, localVector);
/*      */       }
/*      */       catch (ClassNotFound localClassNotFound) {
/* 1880 */         CompoundType.this.env.error(0L, "class.not.found", localClassNotFound.name, this.enclosing
/* 1881 */           .getClassDefinition().getName());
/* 1882 */         return null;
/*      */       }
/*      */ 
/* 1885 */       Method localMethod = (Method)clone();
/* 1886 */       localMethod.exceptions = new ValueType[localVector.size()];
/* 1887 */       localVector.copyInto(localMethod.exceptions);
/* 1888 */       localMethod.implExceptions = localMethod.exceptions;
/*      */ 
/* 1890 */       return localMethod;
/*      */     }
/*      */ 
/*      */     private void collectCompatibleExceptions(ValueType[] paramArrayOfValueType1, ValueType[] paramArrayOfValueType2, Vector paramVector)
/*      */       throws ClassNotFound
/*      */     {
/* 1901 */       for (int i = 0; i < paramArrayOfValueType1.length; i++) {
/* 1902 */         ClassDefinition localClassDefinition = paramArrayOfValueType1[i].getClassDefinition();
/* 1903 */         if (!paramVector.contains(paramArrayOfValueType1[i]))
/* 1904 */           for (int j = 0; j < paramArrayOfValueType2.length; j++)
/* 1905 */             if (localClassDefinition.subClassOf(this.enclosing
/* 1906 */               .getEnv(), paramArrayOfValueType2[j]
/* 1907 */               .getClassDeclaration())) {
/* 1908 */               paramVector.addElement(paramArrayOfValueType1[i]);
/* 1909 */               break;
/*      */             }
/*      */       }
/*      */     }
/*      */ 
/*      */     public CompoundType getEnclosing()
/*      */     {
/* 1920 */       return this.enclosing;
/*      */     }
/*      */ 
/*      */     public Identifier getDeclaredBy()
/*      */     {
/* 1928 */       return this.declaredBy;
/*      */     }
/*      */ 
/*      */     public String getVisibility()
/*      */     {
/* 1935 */       return this.vis;
/*      */     }
/*      */ 
/*      */     public boolean isPublic()
/*      */     {
/* 1942 */       return this.memberDef.isPublic();
/*      */     }
/*      */ 
/*      */     public boolean isProtected() {
/* 1946 */       return this.memberDef.isPrivate();
/*      */     }
/*      */ 
/*      */     public boolean isPrivate() {
/* 1950 */       return this.memberDef.isPrivate();
/*      */     }
/*      */ 
/*      */     public boolean isStatic() {
/* 1954 */       return this.memberDef.isStatic();
/*      */     }
/*      */ 
/*      */     public String getName()
/*      */     {
/* 1961 */       return this.name;
/*      */     }
/*      */ 
/*      */     public String getIDLName()
/*      */     {
/* 1969 */       return this.idlName;
/*      */     }
/*      */ 
/*      */     public sun.tools.java.Type getType()
/*      */     {
/* 1976 */       return this.memberDef.getType();
/*      */     }
/*      */ 
/*      */     public boolean isConstructor()
/*      */     {
/* 1983 */       return this.memberDef.isConstructor();
/*      */     }
/*      */ 
/*      */     public boolean isNormalMethod()
/*      */     {
/* 1991 */       return (!this.memberDef.isConstructor()) && (this.attributeKind == 0);
/*      */     }
/*      */ 
/*      */     public Type getReturnType()
/*      */     {
/* 1998 */       return this.returnType;
/*      */     }
/*      */ 
/*      */     public Type[] getArguments()
/*      */     {
/* 2005 */       return (Type[])this.arguments.clone();
/*      */     }
/*      */ 
/*      */     public String[] getArgumentNames()
/*      */     {
/* 2012 */       return this.argumentNames;
/*      */     }
/*      */ 
/*      */     public MemberDefinition getMemberDefinition()
/*      */     {
/* 2019 */       return this.memberDef;
/*      */     }
/*      */ 
/*      */     public ValueType[] getExceptions()
/*      */     {
/* 2032 */       return (ValueType[])this.exceptions.clone();
/*      */     }
/*      */ 
/*      */     public ValueType[] getImplExceptions()
/*      */     {
/* 2040 */       return (ValueType[])this.implExceptions.clone();
/*      */     }
/*      */ 
/*      */     public ValueType[] getUniqueCatchList(ValueType[] paramArrayOfValueType)
/*      */     {
/* 2052 */       ValueType[] arrayOfValueType1 = paramArrayOfValueType;
/* 2053 */       int i = paramArrayOfValueType.length;
/*      */       int m;
/*      */       try
/*      */       {
/*      */         Object localObject;
/* 2058 */         for (int j = 0; j < paramArrayOfValueType.length; j++) {
/* 2059 */           localObject = paramArrayOfValueType[j].getClassDeclaration();
/* 2060 */           if ((CompoundType.this.env.defRemoteException.superClassOf(CompoundType.this.env, (ClassDeclaration)localObject)) || 
/* 2061 */             (CompoundType.this.env.defRuntimeException
/* 2061 */             .superClassOf(CompoundType.this.env, (ClassDeclaration)localObject)) || 
/* 2062 */             (CompoundType.this.env.defError
/* 2062 */             .superClassOf(CompoundType.this.env, (ClassDeclaration)localObject)))
/*      */           {
/* 2063 */             paramArrayOfValueType[j] = null;
/* 2064 */             i--;
/*      */           }
/*      */ 
/*      */         }
/*      */ 
/* 2069 */         for (j = 0; j < paramArrayOfValueType.length; j++) {
/* 2070 */           if (paramArrayOfValueType[j] != null) {
/* 2071 */             localObject = paramArrayOfValueType[j].getClassDefinition();
/* 2072 */             for (m = 0; m < paramArrayOfValueType.length; m++)
/* 2073 */               if ((m != j) && (paramArrayOfValueType[j] != null) && (paramArrayOfValueType[m] != null) && 
/* 2074 */                 (((ClassDefinition)localObject)
/* 2074 */                 .superClassOf(CompoundType.this.env, paramArrayOfValueType[m]
/* 2074 */                 .getClassDeclaration()))) {
/* 2075 */                 paramArrayOfValueType[m] = null;
/* 2076 */                 i--;
/*      */               }
/*      */           }
/*      */         }
/*      */       }
/*      */       catch (ClassNotFound localClassNotFound)
/*      */       {
/* 2083 */         Type.classNotFound(CompoundType.this.stack, localClassNotFound);
/*      */       }
/*      */ 
/* 2088 */       if (i < paramArrayOfValueType.length) {
/* 2089 */         ValueType[] arrayOfValueType2 = new ValueType[i];
/* 2090 */         int k = 0;
/* 2091 */         for (m = 0; m < paramArrayOfValueType.length; m++) {
/* 2092 */           if (paramArrayOfValueType[m] != null) {
/* 2093 */             arrayOfValueType2[(k++)] = paramArrayOfValueType[m];
/*      */           }
/*      */         }
/* 2096 */         paramArrayOfValueType = arrayOfValueType2;
/*      */       }
/*      */ 
/* 2099 */       if (paramArrayOfValueType.length == 0) {
/* 2100 */         return null;
/*      */       }
/* 2102 */       return paramArrayOfValueType;
/*      */     }
/*      */ 
/*      */     public ValueType[] getFilteredStubExceptions(ValueType[] paramArrayOfValueType)
/*      */     {
/* 2115 */       ValueType[] arrayOfValueType1 = paramArrayOfValueType;
/* 2116 */       int i = paramArrayOfValueType.length;
/*      */       try
/*      */       {
/* 2120 */         for (int j = 0; j < paramArrayOfValueType.length; j++) {
/* 2121 */           ClassDeclaration localClassDeclaration = paramArrayOfValueType[j].getClassDeclaration();
/* 2122 */           if (((CompoundType.this.env.defRemoteException.superClassOf(CompoundType.this.env, localClassDeclaration)) && 
/* 2123 */             (!CompoundType.this.env.defRemoteException
/* 2123 */             .getClassDeclaration().equals(localClassDeclaration))) || 
/* 2124 */             (CompoundType.this.env.defRuntimeException
/* 2124 */             .superClassOf(CompoundType.this.env, localClassDeclaration)) || 
/* 2125 */             (CompoundType.this.env.defError
/* 2125 */             .superClassOf(CompoundType.this.env, localClassDeclaration)))
/*      */           {
/* 2126 */             paramArrayOfValueType[j] = null;
/* 2127 */             i--;
/*      */           }
/*      */         }
/*      */       }
/*      */       catch (ClassNotFound localClassNotFound) {
/* 2132 */         Type.classNotFound(CompoundType.this.stack, localClassNotFound);
/*      */       }
/*      */ 
/* 2137 */       if (i < paramArrayOfValueType.length) {
/* 2138 */         ValueType[] arrayOfValueType2 = new ValueType[i];
/* 2139 */         int k = 0;
/* 2140 */         for (int m = 0; m < paramArrayOfValueType.length; m++) {
/* 2141 */           if (paramArrayOfValueType[m] != null) {
/* 2142 */             arrayOfValueType2[(k++)] = paramArrayOfValueType[m];
/*      */           }
/*      */         }
/* 2145 */         paramArrayOfValueType = arrayOfValueType2;
/*      */       }
/*      */ 
/* 2148 */       return paramArrayOfValueType;
/*      */     }
/*      */ 
/*      */     public String toString()
/*      */     {
/* 2156 */       if (this.stringRep == null)
/*      */       {
/* 2158 */         StringBuffer localStringBuffer = new StringBuffer(this.returnType.toString());
/*      */ 
/* 2162 */         localStringBuffer.append(" ");
/* 2163 */         localStringBuffer.append(getName());
/* 2164 */         localStringBuffer.append(" (");
/*      */ 
/* 2168 */         for (int i = 0; i < this.arguments.length; i++) {
/* 2169 */           if (i > 0) {
/* 2170 */             localStringBuffer.append(", ");
/*      */           }
/* 2172 */           localStringBuffer.append(this.arguments[i]);
/* 2173 */           localStringBuffer.append(" ");
/* 2174 */           localStringBuffer.append(this.argumentNames[i]);
/*      */         }
/*      */ 
/* 2177 */         localStringBuffer.append(")");
/*      */ 
/* 2181 */         for (i = 0; i < this.exceptions.length; i++) {
/* 2182 */           if (i == 0)
/* 2183 */             localStringBuffer.append(" throws ");
/*      */           else {
/* 2185 */             localStringBuffer.append(", ");
/*      */           }
/* 2187 */           localStringBuffer.append(this.exceptions[i]);
/*      */         }
/*      */ 
/* 2190 */         localStringBuffer.append(";");
/*      */ 
/* 2192 */         this.stringRep = localStringBuffer.toString();
/*      */       }
/*      */ 
/* 2195 */       return this.stringRep;
/*      */     }
/*      */ 
/*      */     public void setAttributeKind(int paramInt)
/*      */     {
/* 2203 */       this.attributeKind = paramInt;
/*      */     }
/*      */ 
/*      */     public void setAttributePairIndex(int paramInt)
/*      */     {
/* 2210 */       this.attributePairIndex = paramInt;
/*      */     }
/*      */ 
/*      */     public void setAttributeName(String paramString)
/*      */     {
/* 2217 */       this.attributeName = paramString;
/*      */     }
/*      */ 
/*      */     public void setIDLName(String paramString)
/*      */     {
/* 2224 */       this.idlName = paramString;
/*      */     }
/*      */ 
/*      */     public void setImplExceptions(ValueType[] paramArrayOfValueType)
/*      */     {
/* 2231 */       this.implExceptions = paramArrayOfValueType;
/*      */     }
/*      */ 
/*      */     public void setDeclaredBy(Identifier paramIdentifier)
/*      */     {
/* 2238 */       this.declaredBy = paramIdentifier;
/*      */     }
/*      */ 
/*      */     protected void swapInvalidTypes()
/*      */     {
/* 2248 */       if (this.returnType.getStatus() != 1) {
/* 2249 */         this.returnType = CompoundType.this.getValidType(this.returnType);
/*      */       }
/*      */ 
/* 2254 */       for (int i = 0; i < this.arguments.length; i++) {
/* 2255 */         if (this.arguments[i].getStatus() != 1) {
/* 2256 */           this.arguments[i] = CompoundType.this.getValidType(this.arguments[i]);
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/* 2262 */       for (i = 0; i < this.exceptions.length; i++) {
/* 2263 */         if (this.exceptions[i].getStatus() != 1) {
/* 2264 */           this.exceptions[i] = ((ValueType)CompoundType.this.getValidType(this.exceptions[i]));
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/* 2270 */       for (i = 0; i < this.implExceptions.length; i++)
/* 2271 */         if (this.implExceptions[i].getStatus() != 1)
/* 2272 */           this.implExceptions[i] = ((ValueType)CompoundType.this.getValidType(this.implExceptions[i]));
/*      */     }
/*      */ 
/*      */     public void destroy()
/*      */     {
/* 2281 */       if (this.memberDef != null) {
/* 2282 */         this.memberDef = null;
/* 2283 */         this.enclosing = null;
/*      */         int i;
/* 2284 */         if (this.exceptions != null) {
/* 2285 */           for (i = 0; i < this.exceptions.length; i++) {
/* 2286 */             if (this.exceptions[i] != null) this.exceptions[i].destroy();
/* 2287 */             this.exceptions[i] = null;
/*      */           }
/* 2289 */           this.exceptions = null;
/*      */         }
/*      */ 
/* 2292 */         if (this.implExceptions != null) {
/* 2293 */           for (i = 0; i < this.implExceptions.length; i++) {
/* 2294 */             if (this.implExceptions[i] != null) this.implExceptions[i].destroy();
/* 2295 */             this.implExceptions[i] = null;
/*      */           }
/* 2297 */           this.implExceptions = null;
/*      */         }
/*      */ 
/* 2300 */         if (this.returnType != null) this.returnType.destroy();
/* 2301 */         this.returnType = null;
/*      */ 
/* 2303 */         if (this.arguments != null) {
/* 2304 */           for (i = 0; i < this.arguments.length; i++) {
/* 2305 */             if (this.arguments[i] != null) this.arguments[i].destroy();
/* 2306 */             this.arguments[i] = null;
/*      */           }
/* 2308 */           this.arguments = null;
/*      */         }
/*      */ 
/* 2311 */         if (this.argumentNames != null) {
/* 2312 */           for (i = 0; i < this.argumentNames.length; i++) {
/* 2313 */             this.argumentNames[i] = null;
/*      */           }
/* 2315 */           this.argumentNames = null;
/*      */         }
/*      */ 
/* 2318 */         this.vis = null;
/* 2319 */         this.name = null;
/* 2320 */         this.idlName = null;
/* 2321 */         this.stringRep = null;
/* 2322 */         this.attributeName = null;
/* 2323 */         this.declaredBy = null;
/*      */       }
/*      */     }
/*      */ 
/*      */     private String makeArgName(int paramInt, Type paramType)
/*      */     {
/* 2347 */       return "arg" + paramInt;
/*      */     }
/*      */ 
/*      */     public Method(CompoundType paramMemberDefinition, MemberDefinition paramBoolean, boolean paramContextStack, ContextStack arg5)
/*      */       throws Exception
/*      */     {
/* 2359 */       this.enclosing = paramMemberDefinition;
/* 2360 */       this.memberDef = paramBoolean;
/* 2361 */       this.vis = CompoundType.getVisibilityString(paramBoolean);
/* 2362 */       this.idlName = null;
/* 2363 */       int i = 1;
/* 2364 */       this.declaredBy = paramBoolean.getClassDeclaration().getName();
/*      */ 
/* 2368 */       this.name = paramBoolean.getName().toString();
/*      */       ContextStack localContextStack;
/* 2372 */       localContextStack.setNewContextCode(2);
/* 2373 */       localContextStack.push(this);
/*      */ 
/* 2377 */       localContextStack.setNewContextCode(3);
/* 2378 */       sun.tools.java.Type localType1 = paramBoolean.getType();
/* 2379 */       sun.tools.java.Type localType2 = localType1.getReturnType();
/*      */ 
/* 2381 */       if (localType2 == sun.tools.java.Type.tVoid) {
/* 2382 */         this.returnType = PrimitiveType.forPrimitive(localType2, localContextStack);
/*      */       } else {
/* 2384 */         this.returnType = CompoundType.makeType(localType2, null, localContextStack);
/* 2385 */         if ((this.returnType == null) || 
/* 2386 */           (!CompoundType.this
/* 2386 */           .assertNotImpl(this.returnType, paramContextStack, localContextStack, paramMemberDefinition, false)))
/*      */         {
/* 2387 */           i = 0;
/* 2388 */           Type.failedConstraint(24, paramContextStack, localContextStack, paramMemberDefinition.getName());
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/* 2394 */       localContextStack.setNewContextCode(4);
/* 2395 */       sun.tools.java.Type[] arrayOfType = paramBoolean.getType().getArgumentTypes();
/* 2396 */       this.arguments = new Type[arrayOfType.length];
/* 2397 */       this.argumentNames = new String[arrayOfType.length];
/* 2398 */       Vector localVector = paramBoolean.getArguments();
/*      */ 
/* 2400 */       for (int j = 0; j < arrayOfType.length; j++) {
/* 2401 */         Type localType = null;
/*      */         try {
/* 2403 */           localType = CompoundType.makeType(arrayOfType[j], null, localContextStack);
/*      */         }
/*      */         catch (Exception localException2) {
/*      */         }
/* 2407 */         if (localType != null) {
/* 2408 */           if (!CompoundType.this.assertNotImpl(localType, paramContextStack, localContextStack, paramMemberDefinition, false)) {
/* 2409 */             i = 0;
/*      */           } else {
/* 2411 */             this.arguments[j] = localType;
/* 2412 */             if (localVector != null) {
/* 2413 */               LocalMember localLocalMember = (LocalMember)localVector.elementAt(j + 1);
/* 2414 */               this.argumentNames[j] = localLocalMember.getName().toString();
/*      */             } else {
/* 2416 */               this.argumentNames[j] = makeArgName(j, localType);
/*      */             }
/*      */           }
/*      */         } else {
/* 2420 */           i = 0;
/* 2421 */           Type.failedConstraint(25, false, localContextStack, paramMemberDefinition.getQualifiedName(), this.name);
/*      */         }
/*      */       }
/*      */ 
/* 2425 */       if (i == 0) {
/* 2426 */         localContextStack.pop(false);
/* 2427 */         throw new Exception();
/*      */       }
/*      */ 
/*      */       try
/*      */       {
/* 2433 */         this.exceptions = paramMemberDefinition.getMethodExceptions(paramBoolean, paramContextStack, localContextStack);
/* 2434 */         this.implExceptions = this.exceptions;
/* 2435 */         localContextStack.pop(true);
/*      */       } catch (Exception localException1) {
/* 2437 */         localContextStack.pop(false);
/* 2438 */         throw new Exception();
/*      */       }
/*      */     }
/*      */ 
/*      */     protected Object clone()
/*      */     {
/*      */       try
/*      */       {
/* 2447 */         return super.clone(); } catch (CloneNotSupportedException localCloneNotSupportedException) {
/*      */       }
/* 2449 */       throw new Error("clone failed");
/*      */     }
/*      */   }
/*      */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     sun.rmi.rmic.iiop.CompoundType
 * JD-Core Version:    0.6.2
 */