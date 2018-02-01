/*      */ package sun.rmi.rmic.iiop;
/*      */ 
/*      */ import com.sun.corba.se.impl.util.PackagePrefixChecker;
/*      */ import com.sun.corba.se.impl.util.Utility;
/*      */ import java.io.File;
/*      */ import java.io.IOException;
/*      */ import java.util.Arrays;
/*      */ import java.util.Enumeration;
/*      */ import java.util.HashSet;
/*      */ import java.util.Hashtable;
/*      */ import java.util.Vector;
/*      */ import sun.rmi.rmic.IndentingWriter;
/*      */ import sun.rmi.rmic.Main;
/*      */ import sun.tools.java.ClassDefinition;
/*      */ import sun.tools.java.CompilerError;
/*      */ import sun.tools.java.Identifier;
/*      */ 
/*      */ public class StubGenerator extends Generator
/*      */ {
/*      */   private static final String DEFAULT_STUB_CLASS = "javax.rmi.CORBA.Stub";
/*      */   private static final String DEFAULT_TIE_CLASS = "org.omg.CORBA_2_3.portable.ObjectImpl";
/*      */   private static final String DEFAULT_POA_TIE_CLASS = "org.omg.PortableServer.Servant";
/*   70 */   protected boolean reverseIDs = false;
/*   71 */   protected boolean localStubs = true;
/*   72 */   protected boolean standardPackage = false;
/*   73 */   protected boolean useHash = true;
/*   74 */   protected String stubBaseClass = "javax.rmi.CORBA.Stub";
/*   75 */   protected String tieBaseClass = "org.omg.CORBA_2_3.portable.ObjectImpl";
/*   76 */   protected HashSet namesInUse = new HashSet();
/*   77 */   protected Hashtable classesInUse = new Hashtable();
/*   78 */   protected Hashtable imports = new Hashtable();
/*   79 */   protected int importCount = 0;
/*   80 */   protected String currentPackage = null;
/*   81 */   protected String currentClass = null;
/*   82 */   protected boolean castArray = false;
/*   83 */   protected Hashtable transactionalObjects = new Hashtable();
/*   84 */   protected boolean POATie = false;
/*   85 */   protected boolean emitPermissionCheck = false;
/*      */ 
/*  623 */   private static final String NO_IMPORT = new String();
/*      */   static final String SINGLE_SLASH = "\\";
/*      */   static final String DOUBLE_SLASH = "\\\\";
/*      */ 
/*      */   public void generate(sun.rmi.rmic.BatchEnvironment paramBatchEnvironment, ClassDefinition paramClassDefinition, File paramFile)
/*      */   {
/*   99 */     ((BatchEnvironment)paramBatchEnvironment)
/*  100 */       .setStandardPackage(this.standardPackage);
/*      */ 
/*  101 */     super.generate(paramBatchEnvironment, paramClassDefinition, paramFile);
/*      */   }
/*      */ 
/*      */   protected boolean requireNewInstance()
/*      */   {
/*  111 */     return false;
/*      */   }
/*      */ 
/*      */   protected boolean parseNonConforming(ContextStack paramContextStack)
/*      */   {
/*  124 */     return paramContextStack.getEnv().getParseNonConforming();
/*      */   }
/*      */ 
/*      */   protected CompoundType getTopType(ClassDefinition paramClassDefinition, ContextStack paramContextStack)
/*      */   {
/*  135 */     Object localObject = null;
/*      */ 
/*  139 */     if (paramClassDefinition.isInterface())
/*      */     {
/*  143 */       localObject = AbstractType.forAbstract(paramClassDefinition, paramContextStack, true);
/*      */ 
/*  145 */       if (localObject == null)
/*      */       {
/*  149 */         localObject = RemoteType.forRemote(paramClassDefinition, paramContextStack, false);
/*      */       }
/*      */ 
/*      */     }
/*      */     else
/*      */     {
/*  155 */       localObject = ImplementationType.forImplementation(paramClassDefinition, paramContextStack, false);
/*      */     }
/*      */ 
/*  158 */     return localObject;
/*      */   }
/*      */ 
/*      */   public boolean parseArgs(String[] paramArrayOfString, Main paramMain)
/*      */   {
/*  169 */     Object localObject = new Object();
/*      */ 
/*  173 */     this.reverseIDs = false;
/*  174 */     this.localStubs = true;
/*  175 */     this.useHash = true;
/*  176 */     this.stubBaseClass = "javax.rmi.CORBA.Stub";
/*      */ 
/*  178 */     this.transactionalObjects = new Hashtable();
/*      */ 
/*  182 */     boolean bool = super.parseArgs(paramArrayOfString, paramMain);
/*  183 */     if (bool) {
/*  184 */       for (int i = 0; i < paramArrayOfString.length; i++) {
/*  185 */         if (paramArrayOfString[i] != null) {
/*  186 */           String str = paramArrayOfString[i].toLowerCase();
/*  187 */           if (str.equals("-iiop")) {
/*  188 */             paramArrayOfString[i] = null;
/*  189 */           } else if (str.equals("-xreverseids")) {
/*  190 */             this.reverseIDs = true;
/*  191 */             paramArrayOfString[i] = null;
/*  192 */           } else if (str.equals("-nolocalstubs")) {
/*  193 */             this.localStubs = false;
/*  194 */             paramArrayOfString[i] = null;
/*  195 */           } else if (str.equals("-xnohash")) {
/*  196 */             this.useHash = false;
/*  197 */             paramArrayOfString[i] = null;
/*  198 */           } else if (paramArrayOfString[i].equals("-standardPackage")) {
/*  199 */             this.standardPackage = true;
/*  200 */             paramArrayOfString[i] = null;
/*  201 */           } else if (paramArrayOfString[i].equals("-emitPermissionCheck")) {
/*  202 */             this.emitPermissionCheck = true;
/*  203 */             paramArrayOfString[i] = null;
/*  204 */           } else if (str.equals("-xstubbase")) {
/*  205 */             paramArrayOfString[i] = null;
/*  206 */             i++; if ((i < paramArrayOfString.length) && (paramArrayOfString[i] != null) && (!paramArrayOfString[i].startsWith("-"))) {
/*  207 */               this.stubBaseClass = paramArrayOfString[i];
/*  208 */               paramArrayOfString[i] = null;
/*      */             } else {
/*  210 */               paramMain.error("rmic.option.requires.argument", "-Xstubbase");
/*  211 */               bool = false;
/*      */             }
/*  213 */           } else if (str.equals("-xtiebase")) {
/*  214 */             paramArrayOfString[i] = null;
/*  215 */             i++; if ((i < paramArrayOfString.length) && (paramArrayOfString[i] != null) && (!paramArrayOfString[i].startsWith("-"))) {
/*  216 */               this.tieBaseClass = paramArrayOfString[i];
/*  217 */               paramArrayOfString[i] = null;
/*      */             } else {
/*  219 */               paramMain.error("rmic.option.requires.argument", "-Xtiebase");
/*  220 */               bool = false;
/*      */             }
/*  222 */           } else if (str.equals("-transactional"))
/*      */           {
/*  226 */             for (int j = i + 1; j < paramArrayOfString.length; j++) {
/*  227 */               if (paramArrayOfString[j].charAt(1) != '-') {
/*  228 */                 this.transactionalObjects.put(paramArrayOfString[j], localObject);
/*  229 */                 break;
/*      */               }
/*      */             }
/*  232 */             paramArrayOfString[i] = null;
/*  233 */           } else if (str.equals("-poa")) {
/*  234 */             this.POATie = true;
/*  235 */             paramArrayOfString[i] = null;
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/*  240 */     if (this.POATie)
/*  241 */       this.tieBaseClass = "org.omg.PortableServer.Servant";
/*      */     else {
/*  243 */       this.tieBaseClass = "org.omg.CORBA_2_3.portable.ObjectImpl";
/*      */     }
/*  245 */     return bool;
/*      */   }
/*      */ 
/*      */   protected Generator.OutputType[] getOutputTypesFor(CompoundType paramCompoundType, HashSet paramHashSet)
/*      */   {
/*  267 */     int i = 69632;
/*  268 */     Type[] arrayOfType = paramCompoundType.collectMatching(i, paramHashSet);
/*  269 */     int j = arrayOfType.length;
/*  270 */     Vector localVector = new Vector(j + 5);
/*  271 */     BatchEnvironment localBatchEnvironment = paramCompoundType.getEnv();
/*      */ 
/*  275 */     for (int k = 0; k < arrayOfType.length; k++)
/*      */     {
/*  277 */       Type localType = arrayOfType[k];
/*  278 */       String str = localType.getName();
/*  279 */       int m = 1;
/*      */ 
/*  283 */       if ((localType instanceof ImplementationType))
/*      */       {
/*  287 */         localVector.addElement(new Generator.OutputType(this, Utility.tieNameForCompiler(str), localType));
/*      */ 
/*  292 */         int n = 0;
/*  293 */         InterfaceType[] arrayOfInterfaceType = ((CompoundType)localType).getInterfaces();
/*  294 */         for (int i1 = 0; i1 < arrayOfInterfaceType.length; i1++) {
/*  295 */           if ((arrayOfInterfaceType[i1].isType(4096)) && 
/*  296 */             (!arrayOfInterfaceType[i1]
/*  296 */             .isType(8192)))
/*      */           {
/*  297 */             n++;
/*      */           }
/*      */         }
/*      */ 
/*  301 */         if (n <= 1)
/*      */         {
/*  305 */           m = 0;
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/*  311 */       if ((localType instanceof AbstractType))
/*      */       {
/*  315 */         m = 0;
/*      */       }
/*      */ 
/*  318 */       if (m != 0)
/*      */       {
/*  322 */         localVector.addElement(new Generator.OutputType(this, Utility.stubNameForCompiler(str), localType));
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/*  328 */     Generator.OutputType[] arrayOfOutputType = new Generator.OutputType[localVector.size()];
/*  329 */     localVector.copyInto(arrayOfOutputType);
/*  330 */     return arrayOfOutputType;
/*      */   }
/*      */ 
/*      */   protected String getFileNameExtensionFor(Generator.OutputType paramOutputType)
/*      */   {
/*  341 */     return ".java";
/*      */   }
/*      */ 
/*      */   protected void writeOutputFor(Generator.OutputType paramOutputType, HashSet paramHashSet, IndentingWriter paramIndentingWriter)
/*      */     throws IOException
/*      */   {
/*  355 */     String str = paramOutputType.getName();
/*  356 */     CompoundType localCompoundType = (CompoundType)paramOutputType.getType();
/*      */ 
/*  360 */     if (str.endsWith("_Stub"))
/*      */     {
/*  364 */       writeStub(paramOutputType, paramIndentingWriter);
/*      */     }
/*      */     else
/*      */     {
/*  370 */       writeTie(paramOutputType, paramIndentingWriter);
/*      */     }
/*      */   }
/*      */ 
/*      */   protected void writeStub(Generator.OutputType paramOutputType, IndentingWriter paramIndentingWriter)
/*      */     throws IOException
/*      */   {
/*  380 */     CompoundType localCompoundType = (CompoundType)paramOutputType.getType();
/*  381 */     RemoteType[] arrayOfRemoteType = getDirectRemoteInterfaces(localCompoundType);
/*      */ 
/*  385 */     paramIndentingWriter.pln("// Stub class generated by rmic, do not edit.");
/*  386 */     paramIndentingWriter.pln("// Contents subject to change without notice.");
/*  387 */     paramIndentingWriter.pln();
/*      */ 
/*  391 */     setStandardClassesInUse(localCompoundType, true);
/*      */ 
/*  395 */     addClassesInUse(localCompoundType, arrayOfRemoteType);
/*      */ 
/*  399 */     writePackageAndImports(paramIndentingWriter);
/*      */ 
/*  405 */     if (this.emitPermissionCheck) {
/*  406 */       paramIndentingWriter.pln("import java.security.AccessController;");
/*  407 */       paramIndentingWriter.pln("import java.security.PrivilegedAction;");
/*  408 */       paramIndentingWriter.pln("import java.io.SerializablePermission;");
/*  409 */       paramIndentingWriter.pln();
/*  410 */       paramIndentingWriter.pln();
/*      */     }
/*      */ 
/*  415 */     paramIndentingWriter.p("public class " + this.currentClass);
/*      */ 
/*  417 */     paramIndentingWriter.p(" extends " + getName(this.stubBaseClass));
/*  418 */     paramIndentingWriter.p(" implements ");
/*  419 */     if (arrayOfRemoteType.length > 0) {
/*  420 */       for (int i = 0; i < arrayOfRemoteType.length; i++) {
/*  421 */         if (i > 0) {
/*  422 */           paramIndentingWriter.pln(",");
/*      */         }
/*  424 */         String str = testUtil(getName(arrayOfRemoteType[i]), localCompoundType);
/*  425 */         paramIndentingWriter.p(str);
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/*  433 */     if (!implementsRemote(localCompoundType)) {
/*  434 */       paramIndentingWriter.pln(",");
/*  435 */       paramIndentingWriter.p(getName("java.rmi.Remote"));
/*      */     }
/*      */ 
/*  438 */     paramIndentingWriter.plnI(" {");
/*  439 */     paramIndentingWriter.pln();
/*      */ 
/*  443 */     writeIds(paramIndentingWriter, localCompoundType, false);
/*  444 */     paramIndentingWriter.pln();
/*      */ 
/*  446 */     if (this.emitPermissionCheck)
/*      */     {
/*  477 */       paramIndentingWriter.pln();
/*  478 */       paramIndentingWriter.plnI("private transient boolean _instantiated = false;");
/*  479 */       paramIndentingWriter.pln();
/*  480 */       paramIndentingWriter.pO();
/*  481 */       paramIndentingWriter.plnI("private static Void checkPermission() {");
/*  482 */       paramIndentingWriter.plnI("SecurityManager sm = System.getSecurityManager();");
/*  483 */       paramIndentingWriter.pln("if (sm != null) {");
/*  484 */       paramIndentingWriter.pI();
/*  485 */       paramIndentingWriter.plnI("sm.checkPermission(new SerializablePermission(");
/*  486 */       paramIndentingWriter.plnI("\"enableSubclassImplementation\"));");
/*  487 */       paramIndentingWriter.pO();
/*  488 */       paramIndentingWriter.pO();
/*  489 */       paramIndentingWriter.pOln("}");
/*  490 */       paramIndentingWriter.pln("return null;");
/*  491 */       paramIndentingWriter.pO();
/*  492 */       paramIndentingWriter.pOln("}");
/*  493 */       paramIndentingWriter.pln();
/*  494 */       paramIndentingWriter.pO();
/*      */ 
/*  496 */       paramIndentingWriter.pI();
/*  497 */       paramIndentingWriter.plnI("private " + this.currentClass + "(Void ignore) {  }");
/*  498 */       paramIndentingWriter.pln();
/*  499 */       paramIndentingWriter.pO();
/*      */ 
/*  501 */       paramIndentingWriter.plnI("public " + this.currentClass + "() {");
/*  502 */       paramIndentingWriter.pln("this(checkPermission());");
/*  503 */       paramIndentingWriter.pln("_instantiated = true;");
/*  504 */       paramIndentingWriter.pOln("}");
/*  505 */       paramIndentingWriter.pln();
/*  506 */       paramIndentingWriter.plnI("private void readObject(java.io.ObjectInputStream s) throws IOException, ClassNotFoundException {");
/*  507 */       paramIndentingWriter.plnI("checkPermission();");
/*  508 */       paramIndentingWriter.pO();
/*  509 */       paramIndentingWriter.pln("s.defaultReadObject();");
/*  510 */       paramIndentingWriter.pln("_instantiated = true;");
/*  511 */       paramIndentingWriter.pOln("}");
/*  512 */       paramIndentingWriter.pln();
/*      */     }
/*      */ 
/*  516 */     if (!this.emitPermissionCheck) {
/*  517 */       paramIndentingWriter.pI();
/*      */     }
/*      */ 
/*  522 */     paramIndentingWriter.plnI("public String[] _ids() { ");
/*  523 */     paramIndentingWriter.pln("return (String[]) _type_ids.clone();");
/*  524 */     paramIndentingWriter.pOln("}");
/*      */ 
/*  528 */     CompoundType.Method[] arrayOfMethod = localCompoundType.getMethods();
/*  529 */     int j = arrayOfMethod.length;
/*  530 */     if (j > 0) {
/*  531 */       int k = 1;
/*  532 */       for (int m = 0; m < j; m++) {
/*  533 */         if (!arrayOfMethod[m].isConstructor()) {
/*  534 */           if (k != 0) {
/*  535 */             k = 0;
/*      */           }
/*  537 */           paramIndentingWriter.pln();
/*  538 */           writeStubMethod(paramIndentingWriter, arrayOfMethod[m], localCompoundType);
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/*  545 */     writeCastArray(paramIndentingWriter);
/*      */ 
/*  547 */     paramIndentingWriter.pOln("}");
/*      */   }
/*      */ 
/*      */   void addClassInUse(String paramString) {
/*  551 */     String str1 = paramString;
/*  552 */     String str2 = null;
/*  553 */     int i = paramString.lastIndexOf('.');
/*  554 */     if (i > 0) {
/*  555 */       str1 = paramString.substring(i + 1);
/*  556 */       str2 = paramString.substring(0, i);
/*      */     }
/*  558 */     addClassInUse(str1, paramString, str2);
/*      */   }
/*      */ 
/*      */   void addClassInUse(Type paramType) {
/*  562 */     if (!paramType.isPrimitive()) {
/*  563 */       Identifier localIdentifier = paramType.getIdentifier();
/*  564 */       String str1 = IDLNames.replace(localIdentifier.getName().toString(), ". ", ".");
/*  565 */       String str2 = paramType.getPackageName();
/*      */       String str3;
/*  567 */       if (str2 != null)
/*  568 */         str3 = str2 + "." + str1;
/*      */       else {
/*  570 */         str3 = str1;
/*      */       }
/*  572 */       addClassInUse(str1, str3, str2);
/*      */     }
/*      */   }
/*      */ 
/*      */   void addClassInUse(Type[] paramArrayOfType) {
/*  577 */     for (int i = 0; i < paramArrayOfType.length; i++)
/*  578 */       addClassInUse(paramArrayOfType[i]);
/*      */   }
/*      */ 
/*      */   void addStubInUse(Type paramType)
/*      */   {
/*  583 */     if ((paramType.getIdentifier() != idCorbaObject) && 
/*  584 */       (paramType
/*  584 */       .isType(2048)))
/*      */     {
/*  585 */       String str1 = getStubNameFor(paramType, false);
/*  586 */       String str2 = paramType.getPackageName();
/*      */       String str3;
/*  588 */       if (str2 == null)
/*  589 */         str3 = str1;
/*      */       else {
/*  591 */         str3 = str2 + "." + str1;
/*      */       }
/*  593 */       addClassInUse(str1, str3, str2);
/*      */     }
/*  595 */     if ((paramType.isType(4096)) || 
/*  596 */       (paramType
/*  596 */       .isType(524288)))
/*      */     {
/*  597 */       addClassInUse("javax.rmi.PortableRemoteObject");
/*      */     }
/*      */   }
/*      */ 
/*      */   String getStubNameFor(Type paramType, boolean paramBoolean)
/*      */   {
/*      */     String str2;
/*  604 */     if (paramBoolean)
/*  605 */       str2 = paramType.getQualifiedName();
/*      */     else
/*  607 */       str2 = paramType.getName();
/*      */     String str1;
/*  609 */     if (((CompoundType)paramType).isCORBAObject())
/*  610 */       str1 = Utility.idlStubName(str2);
/*      */     else {
/*  612 */       str1 = Utility.stubNameForCompiler(str2);
/*      */     }
/*  614 */     return str1;
/*      */   }
/*      */ 
/*      */   void addStubInUse(Type[] paramArrayOfType) {
/*  618 */     for (int i = 0; i < paramArrayOfType.length; i++)
/*  619 */       addStubInUse(paramArrayOfType[i]);
/*      */   }
/*      */ 
/*      */   void addClassInUse(String paramString1, String paramString2, String paramString3)
/*      */   {
/*  629 */     String str1 = (String)this.classesInUse.get(paramString2);
/*      */ 
/*  631 */     if (str1 == null)
/*      */     {
/*  636 */       String str2 = (String)this.imports.get(paramString1);
/*  637 */       String str3 = null;
/*      */ 
/*  639 */       if (paramString3 == null)
/*      */       {
/*  643 */         str3 = paramString1;
/*      */       }
/*  645 */       else if (paramString3.equals("java.lang"))
/*      */       {
/*  649 */         str3 = paramString1;
/*      */ 
/*  653 */         if (str3.endsWith("_Stub")) str3 = Util.packagePrefix() + paramString2;
/*      */       }
/*  655 */       else if ((this.currentPackage != null) && (paramString3.equals(this.currentPackage)))
/*      */       {
/*  659 */         str3 = paramString1;
/*      */ 
/*  664 */         if (str2 != null)
/*      */         {
/*  668 */           str3 = paramString2;
/*      */         }
/*      */ 
/*      */       }
/*  672 */       else if (str2 != null)
/*      */       {
/*  679 */         str3 = paramString2;
/*      */       }
/*  699 */       else if (paramString2.equals("org.omg.CORBA.Object"))
/*      */       {
/*  703 */         str3 = paramString2;
/*      */       }
/*  712 */       else if (paramString1.indexOf('.') != -1) {
/*  713 */         str3 = paramString2;
/*      */       } else {
/*  715 */         str3 = paramString1;
/*  716 */         this.imports.put(paramString1, paramString2);
/*  717 */         this.importCount += 1;
/*      */       }
/*      */ 
/*  723 */       this.classesInUse.put(paramString2, str3);
/*      */     }
/*      */   }
/*      */ 
/*      */   String getName(Type paramType) {
/*  728 */     if (paramType.isPrimitive()) {
/*  729 */       return paramType.getName() + paramType.getArrayBrackets();
/*      */     }
/*  731 */     Identifier localIdentifier = paramType.getIdentifier();
/*  732 */     String str = IDLNames.replace(localIdentifier.toString(), ". ", ".");
/*  733 */     return getName(str) + paramType.getArrayBrackets();
/*      */   }
/*      */ 
/*      */   String getExceptionName(Type paramType)
/*      */   {
/*  738 */     Identifier localIdentifier = paramType.getIdentifier();
/*  739 */     return IDLNames.replace(localIdentifier.toString(), ". ", ".");
/*      */   }
/*      */ 
/*      */   String getName(String paramString) {
/*  743 */     return (String)this.classesInUse.get(paramString);
/*      */   }
/*      */ 
/*      */   String getName(Identifier paramIdentifier) {
/*  747 */     return getName(paramIdentifier.toString());
/*      */   }
/*      */ 
/*      */   String getStubName(Type paramType) {
/*  751 */     String str = getStubNameFor(paramType, true);
/*  752 */     return getName(str);
/*      */   }
/*      */ 
/*      */   void setStandardClassesInUse(CompoundType paramCompoundType, boolean paramBoolean)
/*      */     throws IOException
/*      */   {
/*  760 */     this.currentPackage = paramCompoundType.getPackageName();
/*  761 */     this.imports.clear();
/*  762 */     this.classesInUse.clear();
/*  763 */     this.namesInUse.clear();
/*  764 */     this.importCount = 0;
/*  765 */     this.castArray = false;
/*      */ 
/*  769 */     addClassInUse(paramCompoundType);
/*      */ 
/*  773 */     if (paramBoolean)
/*  774 */       this.currentClass = Utility.stubNameForCompiler(paramCompoundType.getName());
/*      */     else {
/*  776 */       this.currentClass = Utility.tieNameForCompiler(paramCompoundType.getName());
/*      */     }
/*      */ 
/*  781 */     if (this.currentPackage == null)
/*  782 */       addClassInUse(this.currentClass, this.currentClass, this.currentPackage);
/*      */     else {
/*  784 */       addClassInUse(this.currentClass, this.currentPackage + "." + this.currentClass, this.currentPackage);
/*      */     }
/*      */ 
/*  789 */     addClassInUse("javax.rmi.CORBA.Util");
/*  790 */     addClassInUse(idRemote.toString());
/*  791 */     addClassInUse(idRemoteException.toString());
/*  792 */     addClassInUse(idOutputStream.toString());
/*  793 */     addClassInUse(idInputStream.toString());
/*  794 */     addClassInUse(idSystemException.toString());
/*  795 */     addClassInUse(idJavaIoSerializable.toString());
/*  796 */     addClassInUse(idCorbaORB.toString());
/*  797 */     addClassInUse(idReplyHandler.toString());
/*      */ 
/*  801 */     if (paramBoolean) {
/*  802 */       addClassInUse(this.stubBaseClass);
/*  803 */       addClassInUse("java.rmi.UnexpectedException");
/*  804 */       addClassInUse(idRemarshalException.toString());
/*  805 */       addClassInUse(idApplicationException.toString());
/*  806 */       if (this.localStubs)
/*  807 */         addClassInUse("org.omg.CORBA.portable.ServantObject");
/*      */     }
/*      */     else {
/*  810 */       addClassInUse(paramCompoundType);
/*  811 */       addClassInUse(this.tieBaseClass);
/*  812 */       addClassInUse(idTieInterface.toString());
/*  813 */       addClassInUse(idBadMethodException.toString());
/*  814 */       addClassInUse(idPortableUnknownException.toString());
/*  815 */       addClassInUse(idJavaLangThrowable.toString());
/*      */     }
/*      */   }
/*      */ 
/*      */   void addClassesInUse(CompoundType paramCompoundType, RemoteType[] paramArrayOfRemoteType)
/*      */   {
/*  823 */     CompoundType.Method[] arrayOfMethod = paramCompoundType.getMethods();
/*  824 */     for (int i = 0; i < arrayOfMethod.length; i++) {
/*  825 */       addClassInUse(arrayOfMethod[i].getReturnType());
/*  826 */       addStubInUse(arrayOfMethod[i].getReturnType());
/*  827 */       addClassInUse(arrayOfMethod[i].getArguments());
/*  828 */       addStubInUse(arrayOfMethod[i].getArguments());
/*  829 */       addClassInUse(arrayOfMethod[i].getExceptions());
/*      */ 
/*  831 */       addClassInUse(arrayOfMethod[i].getImplExceptions());
/*      */     }
/*      */ 
/*  836 */     if (paramArrayOfRemoteType != null)
/*  837 */       addClassInUse(paramArrayOfRemoteType);
/*      */   }
/*      */ 
/*      */   void writePackageAndImports(IndentingWriter paramIndentingWriter)
/*      */     throws IOException
/*      */   {
/*  845 */     if (this.currentPackage != null) {
/*  846 */       paramIndentingWriter.pln("package " + 
/*  847 */         Util.correctPackageName(this.currentPackage, false, this.standardPackage) + 
/*  847 */         ";");
/*      */ 
/*  850 */       paramIndentingWriter.pln();
/*      */     }
/*      */ 
/*  855 */     String[] arrayOfString = new String[this.importCount];
/*  856 */     int i = 0;
/*  857 */     for (Enumeration localEnumeration = this.imports.elements(); localEnumeration.hasMoreElements(); ) {
/*  858 */       String str = (String)localEnumeration.nextElement();
/*  859 */       if (str != NO_IMPORT) {
/*  860 */         arrayOfString[(i++)] = str;
/*      */       }
/*      */     }
/*      */ 
/*  864 */     Arrays.sort(arrayOfString, new StringComparator());
/*      */ 
/*  868 */     for (int j = 0; j < this.importCount; j++)
/*      */     {
/*  870 */       if ((Util.isOffendingPackage(arrayOfString[j])) && 
/*  871 */         (arrayOfString[j]
/*  871 */         .endsWith("_Stub")) && 
/*  872 */         (String.valueOf(arrayOfString[j]
/*  872 */         .charAt(arrayOfString[j]
/*  872 */         .lastIndexOf(".") + 
/*  872 */         1)).equals("_")))
/*      */       {
/*  874 */         paramIndentingWriter.pln("import " + PackagePrefixChecker.packagePrefix() + arrayOfString[j] + ";");
/*      */       }
/*  876 */       else paramIndentingWriter.pln("import " + arrayOfString[j] + ";");
/*      */     }
/*      */ 
/*  879 */     paramIndentingWriter.pln();
/*      */ 
/*  882 */     if ((this.currentPackage != null) && (Util.isOffendingPackage(this.currentPackage))) {
/*  883 */       paramIndentingWriter.pln("import " + this.currentPackage + ".*  ;");
/*      */     }
/*  885 */     paramIndentingWriter.pln();
/*      */   }
/*      */ 
/*      */   boolean implementsRemote(CompoundType paramCompoundType)
/*      */   {
/*  890 */     boolean bool = (paramCompoundType.isType(4096)) && (!paramCompoundType.isType(8192));
/*      */ 
/*  895 */     if (!bool) {
/*  896 */       InterfaceType[] arrayOfInterfaceType = paramCompoundType.getInterfaces();
/*  897 */       for (int i = 0; i < arrayOfInterfaceType.length; i++) {
/*  898 */         bool = implementsRemote(arrayOfInterfaceType[i]);
/*  899 */         if (bool)
/*      */         {
/*      */           break;
/*      */         }
/*      */       }
/*      */     }
/*  905 */     return bool;
/*      */   }
/*      */ 
/*      */   void writeStubMethod(IndentingWriter paramIndentingWriter, CompoundType.Method paramMethod, CompoundType paramCompoundType)
/*      */     throws IOException
/*      */   {
/*  913 */     String str1 = paramMethod.getName();
/*  914 */     String str2 = paramMethod.getIDLName();
/*      */ 
/*  916 */     Type[] arrayOfType = paramMethod.getArguments();
/*  917 */     String[] arrayOfString = paramMethod.getArgumentNames();
/*  918 */     Type localType = paramMethod.getReturnType();
/*  919 */     ValueType[] arrayOfValueType = getStubExceptions(paramMethod, false);
/*  920 */     int i = 0;
/*      */ 
/*  922 */     addNamesInUse(paramMethod);
/*  923 */     addNameInUse("_type_ids");
/*      */ 
/*  925 */     String str3 = testUtil(getName(localType), localType);
/*  926 */     paramIndentingWriter.p("public " + str3 + " " + str1 + "(");
/*  927 */     for (int j = 0; j < arrayOfType.length; j++) {
/*  928 */       if (j > 0)
/*  929 */         paramIndentingWriter.p(", ");
/*  930 */       paramIndentingWriter.p(getName(arrayOfType[j]) + " " + arrayOfString[j]);
/*      */     }
/*      */ 
/*  933 */     paramIndentingWriter.p(")");
/*  934 */     if (arrayOfValueType.length > 0) {
/*  935 */       paramIndentingWriter.p(" throws ");
/*  936 */       for (j = 0; j < arrayOfValueType.length; j++) {
/*  937 */         if (j > 0) {
/*  938 */           paramIndentingWriter.p(", ");
/*      */         }
/*      */ 
/*  941 */         paramIndentingWriter.p(getExceptionName(arrayOfValueType[j]));
/*      */       }
/*      */     }
/*      */ 
/*  945 */     paramIndentingWriter.plnI(" {");
/*      */ 
/*  948 */     if (this.emitPermissionCheck) {
/*  949 */       paramIndentingWriter.pln("if ((System.getSecurityManager() != null) && (!_instantiated)) {");
/*  950 */       paramIndentingWriter.plnI("    throw new java.io.IOError(new java.io.IOException(\"InvalidObject \"));");
/*  951 */       paramIndentingWriter.pOln("}");
/*  952 */       paramIndentingWriter.pln();
/*      */     }
/*      */ 
/*  956 */     if (this.localStubs)
/*  957 */       writeLocalStubMethodBody(paramIndentingWriter, paramMethod, paramCompoundType);
/*      */     else {
/*  959 */       writeNonLocalStubMethodBody(paramIndentingWriter, paramMethod, paramCompoundType);
/*      */     }
/*      */ 
/*  964 */     paramIndentingWriter.pOln("}");
/*      */   }
/*      */ 
/*      */   void writeLocalStubMethodBody(IndentingWriter paramIndentingWriter, CompoundType.Method paramMethod, CompoundType paramCompoundType)
/*      */     throws IOException
/*      */   {
/*  973 */     String[] arrayOfString1 = paramMethod.getArgumentNames();
/*  974 */     Type localType = paramMethod.getReturnType();
/*  975 */     ValueType[] arrayOfValueType = getStubExceptions(paramMethod, false);
/*  976 */     String str2 = paramMethod.getName();
/*  977 */     String str3 = paramMethod.getIDLName();
/*      */ 
/*  979 */     paramIndentingWriter.plnI("if (!Util.isLocal(this)) {");
/*  980 */     writeNonLocalStubMethodBody(paramIndentingWriter, paramMethod, paramCompoundType);
/*  981 */     paramIndentingWriter.pOlnI("} else {");
/*  982 */     String str4 = getVariableName("so");
/*      */ 
/*  984 */     paramIndentingWriter.pln("ServantObject " + str4 + " = _servant_preinvoke(\"" + str3 + "\"," + getName(paramCompoundType) + ".class);");
/*  985 */     paramIndentingWriter.plnI("if (" + str4 + " == null) {");
/*  986 */     if (!localType.isType(1)) {
/*  987 */       paramIndentingWriter.p("return ");
/*      */     }
/*  989 */     paramIndentingWriter.p(str2 + "(");
/*  990 */     for (int i = 0; i < arrayOfString1.length; i++) {
/*  991 */       if (i > 0)
/*  992 */         paramIndentingWriter.p(", ");
/*  993 */       paramIndentingWriter.p(arrayOfString1[i]);
/*      */     }
/*  995 */     paramIndentingWriter.pln(");");
/*  996 */     if (localType.isType(1)) {
/*  997 */       paramIndentingWriter.pln("return ;");
/*      */     }
/*      */ 
/* 1000 */     paramIndentingWriter.pOln("}");
/* 1001 */     paramIndentingWriter.plnI("try {");
/*      */ 
/* 1006 */     String[] arrayOfString2 = writeCopyArguments(paramMethod, paramIndentingWriter);
/*      */ 
/* 1010 */     boolean bool = mustCopy(localType);
/* 1011 */     String str5 = null;
/* 1012 */     if (!localType.isType(1)) {
/* 1013 */       if (bool) {
/* 1014 */         str5 = getVariableName("result");
/* 1015 */         str1 = testUtil(getName(localType), localType);
/* 1016 */         paramIndentingWriter.p(str1 + " " + str5 + " = ");
/*      */       } else {
/* 1018 */         paramIndentingWriter.p("return ");
/*      */       }
/*      */     }
/* 1021 */     String str1 = testUtil(getName(paramCompoundType), paramCompoundType);
/* 1022 */     paramIndentingWriter.p("((" + str1 + ")" + str4 + ".servant)." + str2 + "(");
/*      */ 
/* 1024 */     for (int j = 0; j < arrayOfString2.length; j++) {
/* 1025 */       if (j > 0)
/* 1026 */         paramIndentingWriter.p(", ");
/* 1027 */       paramIndentingWriter.p(arrayOfString2[j]);
/*      */     }
/*      */ 
/* 1030 */     if (bool) {
/* 1031 */       paramIndentingWriter.pln(");");
/* 1032 */       str1 = testUtil(getName(localType), localType);
/* 1033 */       paramIndentingWriter.pln("return (" + str1 + ")Util.copyObject(" + str5 + ",_orb());");
/*      */     } else {
/* 1035 */       paramIndentingWriter.pln(");");
/*      */     }
/*      */ 
/* 1038 */     String str6 = getVariableName("ex");
/* 1039 */     String str7 = getVariableName("exCopy");
/* 1040 */     paramIndentingWriter.pOlnI("} catch (Throwable " + str6 + ") {");
/*      */ 
/* 1042 */     paramIndentingWriter.pln("Throwable " + str7 + " = (Throwable)Util.copyObject(" + str6 + ",_orb());");
/* 1043 */     for (int k = 0; k < arrayOfValueType.length; k++) {
/* 1044 */       if ((arrayOfValueType[k].getIdentifier() != idRemoteException) && 
/* 1045 */         (arrayOfValueType[k]
/* 1045 */         .isType(32768)))
/*      */       {
/* 1047 */         paramIndentingWriter.plnI("if (" + str7 + " instanceof " + getExceptionName(arrayOfValueType[k]) + ") {");
/* 1048 */         paramIndentingWriter.pln("throw (" + getExceptionName(arrayOfValueType[k]) + ")" + str7 + ";");
/* 1049 */         paramIndentingWriter.pOln("}");
/*      */       }
/*      */     }
/*      */ 
/* 1053 */     paramIndentingWriter.pln("throw Util.wrapException(" + str7 + ");");
/* 1054 */     paramIndentingWriter.pOlnI("} finally {");
/* 1055 */     paramIndentingWriter.pln("_servant_postinvoke(" + str4 + ");");
/* 1056 */     paramIndentingWriter.pOln("}");
/* 1057 */     paramIndentingWriter.pOln("}");
/*      */   }
/*      */ 
/*      */   void writeNonLocalStubMethodBody(IndentingWriter paramIndentingWriter, CompoundType.Method paramMethod, CompoundType paramCompoundType)
/*      */     throws IOException
/*      */   {
/* 1065 */     String str1 = paramMethod.getName();
/* 1066 */     String str2 = paramMethod.getIDLName();
/*      */ 
/* 1068 */     Type[] arrayOfType = paramMethod.getArguments();
/* 1069 */     String[] arrayOfString = paramMethod.getArgumentNames();
/* 1070 */     Type localType = paramMethod.getReturnType();
/* 1071 */     ValueType[] arrayOfValueType = getStubExceptions(paramMethod, true);
/*      */ 
/* 1073 */     String str3 = getVariableName("in");
/* 1074 */     String str4 = getVariableName("out");
/* 1075 */     String str5 = getVariableName("ex");
/*      */ 
/* 1080 */     boolean bool = false;
/* 1081 */     for (int i = 0; i < arrayOfValueType.length; i++) {
/* 1082 */       if ((arrayOfValueType[i].getIdentifier() != idRemoteException) && 
/* 1083 */         (arrayOfValueType[i]
/* 1083 */         .isType(32768)) && 
/* 1084 */         (needNewReadStreamClass(arrayOfValueType[i])))
/*      */       {
/* 1085 */         bool = true;
/* 1086 */         break;
/*      */       }
/*      */     }
/* 1089 */     if (!bool) {
/* 1090 */       for (i = 0; i < arrayOfType.length; i++) {
/* 1091 */         if (needNewReadStreamClass(arrayOfType[i])) {
/* 1092 */           bool = true;
/* 1093 */           break;
/*      */         }
/*      */       }
/*      */     }
/* 1097 */     if (!bool) {
/* 1098 */       bool = needNewReadStreamClass(localType);
/*      */     }
/*      */ 
/* 1104 */     i = 0;
/* 1105 */     for (int j = 0; j < arrayOfType.length; j++) {
/* 1106 */       if (needNewWriteStreamClass(arrayOfType[j])) {
/* 1107 */         i = 1;
/* 1108 */         break;
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/* 1114 */     paramIndentingWriter.plnI("try {");
/* 1115 */     if (bool)
/* 1116 */       paramIndentingWriter.pln(idExtInputStream + " " + str3 + " = null;");
/*      */     else {
/* 1118 */       paramIndentingWriter.pln(idInputStream + " " + str3 + " = null;");
/*      */     }
/* 1120 */     paramIndentingWriter.plnI("try {");
/*      */ 
/* 1122 */     String str6 = "null";
/*      */ 
/* 1124 */     if (i != 0) {
/* 1125 */       paramIndentingWriter.plnI(idExtOutputStream + " " + str4 + " = ");
/* 1126 */       paramIndentingWriter.pln("(" + idExtOutputStream + ")");
/* 1127 */       paramIndentingWriter.pln("_request(\"" + str2 + "\", true);");
/* 1128 */       paramIndentingWriter.pO();
/*      */     } else {
/* 1130 */       paramIndentingWriter.pln("OutputStream " + str4 + " = _request(\"" + str2 + "\", true);");
/*      */     }
/*      */ 
/* 1133 */     if (arrayOfType.length > 0) {
/* 1134 */       writeMarshalArguments(paramIndentingWriter, str4, arrayOfType, arrayOfString);
/* 1135 */       paramIndentingWriter.pln();
/*      */     }
/* 1137 */     str6 = str4;
/*      */ 
/* 1139 */     if (localType.isType(1)) {
/* 1140 */       paramIndentingWriter.pln("_invoke(" + str6 + ");");
/*      */     } else {
/* 1142 */       if (bool) {
/* 1143 */         paramIndentingWriter.plnI(str3 + " = (" + idExtInputStream + ")_invoke(" + str6 + ");");
/* 1144 */         paramIndentingWriter.pO();
/*      */       } else {
/* 1146 */         paramIndentingWriter.pln(str3 + " = _invoke(" + str6 + ");");
/*      */       }
/* 1148 */       paramIndentingWriter.p("return ");
/* 1149 */       writeUnmarshalArgument(paramIndentingWriter, str3, localType, null);
/* 1150 */       paramIndentingWriter.pln();
/*      */     }
/*      */ 
/* 1155 */     paramIndentingWriter.pOlnI("} catch (" + getName(idApplicationException) + " " + str5 + ") {");
/* 1156 */     if (bool)
/* 1157 */       paramIndentingWriter.pln(str3 + " = (" + idExtInputStream + ") " + str5 + ".getInputStream();");
/*      */     else {
/* 1159 */       paramIndentingWriter.pln(str3 + " = " + str5 + ".getInputStream();");
/*      */     }
/*      */ 
/* 1162 */     int k = 0;
/* 1163 */     int m = 0;
/* 1164 */     for (int n = 0; n < arrayOfValueType.length; n++) {
/* 1165 */       if (arrayOfValueType[n].getIdentifier() != idRemoteException)
/*      */       {
/* 1169 */         if ((arrayOfValueType[n].isIDLEntityException()) && (!arrayOfValueType[n].isCORBAUserException()))
/*      */         {
/* 1173 */           if ((m == 0) && (k == 0)) {
/* 1174 */             paramIndentingWriter.pln("String $_id = " + str5 + ".getId();");
/* 1175 */             m = 1;
/*      */           }
/*      */ 
/* 1178 */           String str7 = IDLNames.replace(arrayOfValueType[n].getQualifiedIDLName(false), "::", ".");
/* 1179 */           str7 = str7 + "Helper";
/* 1180 */           paramIndentingWriter.plnI("if ($_id.equals(" + str7 + ".id())) {");
/* 1181 */           paramIndentingWriter.pln("throw " + str7 + ".read(" + str3 + ");");
/*      */         }
/*      */         else
/*      */         {
/* 1187 */           if ((m == 0) && (k == 0)) {
/* 1188 */             paramIndentingWriter.pln("String $_id = " + str3 + ".read_string();");
/* 1189 */             m = 1;
/* 1190 */             k = 1;
/* 1191 */           } else if ((m != 0) && (k == 0)) {
/* 1192 */             paramIndentingWriter.pln("$_id = " + str3 + ".read_string();");
/* 1193 */             k = 1;
/*      */           }
/* 1195 */           paramIndentingWriter.plnI("if ($_id.equals(\"" + getExceptionRepositoryID(arrayOfValueType[n]) + "\")) {");
/*      */ 
/* 1197 */           paramIndentingWriter.pln("throw (" + getExceptionName(arrayOfValueType[n]) + ") " + str3 + ".read_value(" + getExceptionName(arrayOfValueType[n]) + ".class);");
/*      */         }
/* 1199 */         paramIndentingWriter.pOln("}");
/*      */       }
/*      */     }
/* 1202 */     if ((m == 0) && (k == 0)) {
/* 1203 */       paramIndentingWriter.pln("String $_id = " + str3 + ".read_string();");
/* 1204 */       m = 1;
/* 1205 */       k = 1;
/* 1206 */     } else if ((m != 0) && (k == 0)) {
/* 1207 */       paramIndentingWriter.pln("$_id = " + str3 + ".read_string();");
/* 1208 */       k = 1;
/*      */     }
/* 1210 */     paramIndentingWriter.pln("throw new UnexpectedException($_id);");
/*      */ 
/* 1214 */     paramIndentingWriter.pOlnI("} catch (" + getName(idRemarshalException) + " " + str5 + ") {");
/* 1215 */     if (!localType.isType(1)) {
/* 1216 */       paramIndentingWriter.p("return ");
/*      */     }
/* 1218 */     paramIndentingWriter.p(str1 + "(");
/* 1219 */     for (n = 0; n < arrayOfType.length; n++) {
/* 1220 */       if (n > 0) {
/* 1221 */         paramIndentingWriter.p(",");
/*      */       }
/* 1223 */       paramIndentingWriter.p(arrayOfString[n]);
/*      */     }
/* 1225 */     paramIndentingWriter.pln(");");
/*      */ 
/* 1229 */     paramIndentingWriter.pOlnI("} finally {");
/* 1230 */     paramIndentingWriter.pln("_releaseReply(" + str3 + ");");
/*      */ 
/* 1232 */     paramIndentingWriter.pOln("}");
/*      */ 
/* 1236 */     paramIndentingWriter.pOlnI("} catch (SystemException " + str5 + ") {");
/* 1237 */     paramIndentingWriter.pln("throw Util.mapSystemException(" + str5 + ");");
/* 1238 */     paramIndentingWriter.pOln("}");
/*      */   }
/*      */ 
/*      */   void allocateResult(IndentingWriter paramIndentingWriter, Type paramType)
/*      */     throws IOException
/*      */   {
/* 1245 */     if (!paramType.isType(1)) {
/* 1246 */       String str = testUtil(getName(paramType), paramType);
/* 1247 */       paramIndentingWriter.p(str + " result = ");
/*      */     }
/*      */   }
/*      */ 
/*      */   int getTypeCode(Type paramType)
/*      */   {
/* 1253 */     int i = paramType.getTypeCode();
/*      */ 
/* 1258 */     if (((paramType instanceof CompoundType)) && 
/* 1259 */       (((CompoundType)paramType)
/* 1259 */       .isAbstractBase())) {
/* 1260 */       i = 8192;
/*      */     }
/*      */ 
/* 1263 */     return i;
/*      */   }
/*      */ 
/*      */   void writeMarshalArgument(IndentingWriter paramIndentingWriter, String paramString1, Type paramType, String paramString2)
/*      */     throws IOException
/*      */   {
/* 1275 */     int i = getTypeCode(paramType);
/*      */ 
/* 1277 */     switch (i) {
/*      */     case 2:
/* 1279 */       paramIndentingWriter.p(paramString1 + ".write_boolean(" + paramString2 + ");");
/* 1280 */       break;
/*      */     case 4:
/* 1282 */       paramIndentingWriter.p(paramString1 + ".write_octet(" + paramString2 + ");");
/* 1283 */       break;
/*      */     case 8:
/* 1285 */       paramIndentingWriter.p(paramString1 + ".write_wchar(" + paramString2 + ");");
/* 1286 */       break;
/*      */     case 16:
/* 1288 */       paramIndentingWriter.p(paramString1 + ".write_short(" + paramString2 + ");");
/* 1289 */       break;
/*      */     case 32:
/* 1291 */       paramIndentingWriter.p(paramString1 + ".write_long(" + paramString2 + ");");
/* 1292 */       break;
/*      */     case 64:
/* 1294 */       paramIndentingWriter.p(paramString1 + ".write_longlong(" + paramString2 + ");");
/* 1295 */       break;
/*      */     case 128:
/* 1297 */       paramIndentingWriter.p(paramString1 + ".write_float(" + paramString2 + ");");
/* 1298 */       break;
/*      */     case 256:
/* 1300 */       paramIndentingWriter.p(paramString1 + ".write_double(" + paramString2 + ");");
/* 1301 */       break;
/*      */     case 512:
/* 1303 */       paramIndentingWriter.p(paramString1 + ".write_value(" + paramString2 + "," + getName(paramType) + ".class);");
/* 1304 */       break;
/*      */     case 1024:
/* 1306 */       paramIndentingWriter.p("Util.writeAny(" + paramString1 + "," + paramString2 + ");");
/* 1307 */       break;
/*      */     case 2048:
/* 1309 */       paramIndentingWriter.p(paramString1 + ".write_Object(" + paramString2 + ");");
/* 1310 */       break;
/*      */     case 4096:
/* 1312 */       paramIndentingWriter.p("Util.writeRemoteObject(" + paramString1 + "," + paramString2 + ");");
/* 1313 */       break;
/*      */     case 8192:
/* 1315 */       paramIndentingWriter.p("Util.writeAbstractObject(" + paramString1 + "," + paramString2 + ");");
/* 1316 */       break;
/*      */     case 16384:
/* 1318 */       paramIndentingWriter.p(paramString1 + ".write_value((Serializable)" + paramString2 + "," + getName(paramType) + ".class);");
/* 1319 */       break;
/*      */     case 32768:
/* 1321 */       paramIndentingWriter.p(paramString1 + ".write_value(" + paramString2 + "," + getName(paramType) + ".class);");
/* 1322 */       break;
/*      */     case 65536:
/* 1324 */       paramIndentingWriter.p(paramString1 + ".write_value((Serializable)" + paramString2 + "," + getName(paramType) + ".class);");
/* 1325 */       break;
/*      */     case 131072:
/* 1327 */       paramIndentingWriter.p(paramString1 + ".write_value((Serializable)" + paramString2 + "," + getName(paramType) + ".class);");
/* 1328 */       break;
/*      */     case 262144:
/* 1330 */       this.castArray = true;
/* 1331 */       paramIndentingWriter.p(paramString1 + ".write_value(cast_array(" + paramString2 + ")," + getName(paramType) + ".class);");
/* 1332 */       break;
/*      */     case 524288:
/* 1334 */       paramIndentingWriter.p("Util.writeRemoteObject(" + paramString1 + "," + paramString2 + ");");
/* 1335 */       break;
/*      */     default:
/* 1337 */       throw new Error("unexpected type code: " + i);
/*      */     }
/*      */   }
/*      */ 
/*      */   void writeUnmarshalArgument(IndentingWriter paramIndentingWriter, String paramString1, Type paramType, String paramString2)
/*      */     throws IOException
/*      */   {
/* 1352 */     int i = getTypeCode(paramType);
/*      */ 
/* 1354 */     if (paramString2 != null) {
/* 1355 */       paramIndentingWriter.p(paramString2 + " = ");
/*      */     }
/*      */ 
/* 1358 */     switch (i) {
/*      */     case 2:
/* 1360 */       paramIndentingWriter.p(paramString1 + ".read_boolean();");
/* 1361 */       break;
/*      */     case 4:
/* 1363 */       paramIndentingWriter.p(paramString1 + ".read_octet();");
/* 1364 */       break;
/*      */     case 8:
/* 1366 */       paramIndentingWriter.p(paramString1 + ".read_wchar();");
/* 1367 */       break;
/*      */     case 16:
/* 1369 */       paramIndentingWriter.p(paramString1 + ".read_short();");
/* 1370 */       break;
/*      */     case 32:
/* 1372 */       paramIndentingWriter.p(paramString1 + ".read_long();");
/* 1373 */       break;
/*      */     case 64:
/* 1375 */       paramIndentingWriter.p(paramString1 + ".read_longlong();");
/* 1376 */       break;
/*      */     case 128:
/* 1378 */       paramIndentingWriter.p(paramString1 + ".read_float();");
/* 1379 */       break;
/*      */     case 256:
/* 1381 */       paramIndentingWriter.p(paramString1 + ".read_double();");
/* 1382 */       break;
/*      */     case 512:
/* 1384 */       paramIndentingWriter.p("(String) " + paramString1 + ".read_value(" + getName(paramType) + ".class);");
/* 1385 */       break;
/*      */     case 1024:
/* 1387 */       if (paramType.getIdentifier() != idJavaLangObject) {
/* 1388 */         paramIndentingWriter.p("(" + getName(paramType) + ") ");
/*      */       }
/* 1390 */       paramIndentingWriter.p("Util.readAny(" + paramString1 + ");");
/* 1391 */       break;
/*      */     case 2048:
/* 1393 */       if (paramType.getIdentifier() == idCorbaObject)
/* 1394 */         paramIndentingWriter.p("(" + getName(paramType) + ") " + paramString1 + ".read_Object();");
/*      */       else {
/* 1396 */         paramIndentingWriter.p("(" + getName(paramType) + ") " + paramString1 + ".read_Object(" + getStubName(paramType) + ".class);");
/*      */       }
/* 1398 */       break;
/*      */     case 4096:
/* 1400 */       String str = testUtil(getName(paramType), paramType);
/* 1401 */       paramIndentingWriter.p("(" + str + ") " + "PortableRemoteObject.narrow(" + paramString1 + ".read_Object(), " + str + ".class);");
/*      */ 
/* 1403 */       break;
/*      */     case 8192:
/* 1405 */       paramIndentingWriter.p("(" + getName(paramType) + ") " + paramString1 + ".read_abstract_interface();");
/* 1406 */       break;
/*      */     case 16384:
/* 1408 */       paramIndentingWriter.p("(" + getName(paramType) + ") " + paramString1 + ".read_value(" + getName(paramType) + ".class);");
/* 1409 */       break;
/*      */     case 32768:
/* 1411 */       paramIndentingWriter.p("(" + getName(paramType) + ") " + paramString1 + ".read_value(" + getName(paramType) + ".class);");
/* 1412 */       break;
/*      */     case 65536:
/* 1414 */       paramIndentingWriter.p("(" + getName(paramType) + ") " + paramString1 + ".read_value(" + getName(paramType) + ".class);");
/* 1415 */       break;
/*      */     case 131072:
/* 1417 */       paramIndentingWriter.p("(" + getName(paramType) + ") " + paramString1 + ".read_value(" + getName(paramType) + ".class);");
/* 1418 */       break;
/*      */     case 262144:
/* 1420 */       paramIndentingWriter.p("(" + getName(paramType) + ") " + paramString1 + ".read_value(" + getName(paramType) + ".class);");
/* 1421 */       break;
/*      */     case 524288:
/* 1423 */       paramIndentingWriter.p("(" + getName(paramType) + ") " + "PortableRemoteObject.narrow(" + paramString1 + ".read_Object(), " + 
/* 1424 */         getName(paramType) + 
/* 1424 */         ".class);");
/*      */ 
/* 1426 */       break;
/*      */     default:
/* 1428 */       throw new Error("unexpected type code: " + i);
/*      */     }
/*      */   }
/*      */ 
/*      */   String[] getAllRemoteRepIDs(CompoundType paramCompoundType)
/*      */   {
/* 1450 */     Type[] arrayOfType = collectAllRemoteInterfaces(paramCompoundType);
/*      */ 
/* 1452 */     int i = arrayOfType.length;
/* 1453 */     boolean bool = paramCompoundType instanceof ImplementationType;
/* 1454 */     InterfaceType[] arrayOfInterfaceType = paramCompoundType.getInterfaces();
/* 1455 */     int j = countRemote(arrayOfInterfaceType, false);
/* 1456 */     int k = 0;
/*      */     String[] arrayOfString;
/*      */     int n;
/*      */     Object localObject;
/* 1461 */     if ((bool) && (j > 1))
/*      */     {
/* 1465 */       arrayOfString = new String[i + 1];
/* 1466 */       arrayOfString[0] = getRepositoryID(paramCompoundType);
/* 1467 */       k = 1;
/*      */     }
/*      */     else
/*      */     {
/* 1473 */       arrayOfString = new String[i];
/*      */ 
/* 1479 */       if (i > 1)
/*      */       {
/* 1483 */         String str = null;
/*      */ 
/* 1485 */         if (bool)
/*      */         {
/* 1490 */           for (n = 0; n < arrayOfInterfaceType.length; n++) {
/* 1491 */             if (arrayOfInterfaceType[n].isType(4096)) {
/* 1492 */               str = arrayOfInterfaceType[n].getRepositoryID();
/* 1493 */               break;
/*      */             }
/*      */ 
/*      */           }
/*      */ 
/*      */         }
/*      */         else
/*      */         {
/* 1501 */           str = paramCompoundType.getRepositoryID();
/*      */         }
/*      */ 
/* 1507 */         for (n = 0; n < i; n++) {
/* 1508 */           if (arrayOfType[n].getRepositoryID() == str)
/*      */           {
/* 1512 */             if (n <= 0) break;
/* 1513 */             localObject = arrayOfType[0];
/* 1514 */             arrayOfType[0] = arrayOfType[n];
/* 1515 */             arrayOfType[n] = localObject;
/* 1516 */             break;
/*      */           }
/*      */ 
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/* 1526 */     for (int m = 0; m < arrayOfType.length; m++) {
/* 1527 */       arrayOfString[(k++)] = getRepositoryID(arrayOfType[m]);
/*      */     }
/*      */ 
/* 1535 */     if (this.reverseIDs) {
/* 1536 */       m = 0;
/* 1537 */       n = arrayOfString.length - 1;
/* 1538 */       while (m < n) {
/* 1539 */         localObject = arrayOfString[m];
/* 1540 */         arrayOfString[(m++)] = arrayOfString[n];
/* 1541 */         arrayOfString[(n--)] = localObject;
/*      */       }
/*      */     }
/*      */ 
/* 1545 */     return arrayOfString;
/*      */   }
/*      */ 
/*      */   Type[] collectAllRemoteInterfaces(CompoundType paramCompoundType)
/*      */   {
/* 1552 */     Vector localVector = new Vector();
/*      */ 
/* 1557 */     addRemoteInterfaces(localVector, paramCompoundType);
/*      */ 
/* 1561 */     Type[] arrayOfType = new Type[localVector.size()];
/* 1562 */     localVector.copyInto(arrayOfType);
/*      */ 
/* 1564 */     return arrayOfType;
/*      */   }
/*      */ 
/*      */   void addRemoteInterfaces(Vector paramVector, CompoundType paramCompoundType)
/*      */   {
/* 1572 */     if (paramCompoundType != null) {
/* 1573 */       if ((paramCompoundType.isInterface()) && (!paramVector.contains(paramCompoundType))) {
/* 1574 */         paramVector.addElement(paramCompoundType);
/*      */       }
/*      */ 
/* 1577 */       InterfaceType[] arrayOfInterfaceType = paramCompoundType.getInterfaces();
/* 1578 */       for (int i = 0; i < arrayOfInterfaceType.length; i++)
/*      */       {
/* 1580 */         if (arrayOfInterfaceType[i].isType(4096)) {
/* 1581 */           addRemoteInterfaces(paramVector, arrayOfInterfaceType[i]);
/*      */         }
/*      */       }
/*      */ 
/* 1585 */       addRemoteInterfaces(paramVector, paramCompoundType.getSuperclass());
/*      */     }
/*      */   }
/*      */ 
/*      */   RemoteType[] getDirectRemoteInterfaces(CompoundType paramCompoundType)
/*      */   {
/* 1596 */     InterfaceType[] arrayOfInterfaceType1 = paramCompoundType.getInterfaces();
/*      */     InterfaceType[] arrayOfInterfaceType2;
/* 1609 */     if ((paramCompoundType instanceof ImplementationType))
/*      */     {
/* 1614 */       arrayOfInterfaceType2 = arrayOfInterfaceType1;
/*      */     }
/*      */     else
/*      */     {
/* 1620 */       arrayOfInterfaceType2 = new InterfaceType[1];
/* 1621 */       arrayOfInterfaceType2[0] = ((InterfaceType)paramCompoundType);
/*      */     }
/*      */ 
/* 1627 */     int i = countRemote(arrayOfInterfaceType2, false);
/*      */ 
/* 1629 */     if (i == 0) {
/* 1630 */       throw new CompilerError("iiop.StubGenerator: No remote interfaces!");
/*      */     }
/*      */ 
/* 1633 */     RemoteType[] arrayOfRemoteType = new RemoteType[i];
/* 1634 */     int j = 0;
/* 1635 */     for (int k = 0; k < arrayOfInterfaceType2.length; k++) {
/* 1636 */       if (arrayOfInterfaceType2[k].isType(4096)) {
/* 1637 */         arrayOfRemoteType[(j++)] = ((RemoteType)arrayOfInterfaceType2[k]);
/*      */       }
/*      */     }
/*      */ 
/* 1641 */     return arrayOfRemoteType;
/*      */   }
/*      */ 
/*      */   int countRemote(Type[] paramArrayOfType, boolean paramBoolean) {
/* 1645 */     int i = 0;
/* 1646 */     for (int j = 0; j < paramArrayOfType.length; j++) {
/* 1647 */       if ((paramArrayOfType[j].isType(4096)) && ((paramBoolean) || 
/* 1648 */         (!paramArrayOfType[j]
/* 1648 */         .isType(8192))))
/*      */       {
/* 1649 */         i++;
/*      */       }
/*      */     }
/*      */ 
/* 1653 */     return i;
/*      */   }
/*      */ 
/*      */   void writeCastArray(IndentingWriter paramIndentingWriter) throws IOException {
/* 1657 */     if (this.castArray) {
/* 1658 */       paramIndentingWriter.pln();
/* 1659 */       paramIndentingWriter.pln("// This method is required as a work-around for");
/* 1660 */       paramIndentingWriter.pln("// a bug in the JDK 1.1.6 verifier.");
/* 1661 */       paramIndentingWriter.pln();
/* 1662 */       paramIndentingWriter.plnI("private " + getName(idJavaIoSerializable) + " cast_array(Object obj) {");
/* 1663 */       paramIndentingWriter.pln("return (" + getName(idJavaIoSerializable) + ")obj;");
/* 1664 */       paramIndentingWriter.pOln("}");
/*      */     }
/*      */   }
/*      */ 
/*      */   void writeIds(IndentingWriter paramIndentingWriter, CompoundType paramCompoundType, boolean paramBoolean) throws IOException {
/* 1669 */     paramIndentingWriter.plnI("private static final String[] _type_ids = {");
/*      */ 
/* 1671 */     String[] arrayOfString = getAllRemoteRepIDs(paramCompoundType);
/*      */ 
/* 1673 */     if (arrayOfString.length > 0) {
/* 1674 */       for (int i = 0; i < arrayOfString.length; i++) {
/* 1675 */         if (i > 0)
/* 1676 */           paramIndentingWriter.pln(", ");
/* 1677 */         paramIndentingWriter.p("\"" + arrayOfString[i] + "\"");
/*      */       }
/*      */     }
/*      */     else {
/* 1681 */       paramIndentingWriter.pln("\"\"");
/*      */     }
/* 1683 */     String str = paramCompoundType.getQualifiedName();
/* 1684 */     int j = (paramBoolean) && (this.transactionalObjects.containsKey(str)) ? 1 : 0;
/*      */ 
/* 1686 */     if (j != 0)
/*      */     {
/* 1688 */       paramIndentingWriter.pln(", ");
/* 1689 */       paramIndentingWriter.pln("\"IDL:omg.org/CosTransactions/TransactionalObject:1.0\"");
/* 1690 */     } else if (arrayOfString.length > 0) {
/* 1691 */       paramIndentingWriter.pln();
/*      */     }
/* 1693 */     paramIndentingWriter.pOln("};");
/*      */   }
/*      */ 
/*      */   protected void writeTie(Generator.OutputType paramOutputType, IndentingWriter paramIndentingWriter)
/*      */     throws IOException
/*      */   {
/* 1703 */     CompoundType localCompoundType = (CompoundType)paramOutputType.getType();
/* 1704 */     RemoteType[] arrayOfRemoteType = null;
/*      */ 
/* 1707 */     paramIndentingWriter.pln("// Tie class generated by rmic, do not edit.");
/* 1708 */     paramIndentingWriter.pln("// Contents subject to change without notice.");
/* 1709 */     paramIndentingWriter.pln();
/*      */ 
/* 1712 */     setStandardClassesInUse(localCompoundType, false);
/*      */ 
/* 1715 */     addClassesInUse(localCompoundType, arrayOfRemoteType);
/*      */ 
/* 1718 */     writePackageAndImports(paramIndentingWriter);
/*      */ 
/* 1721 */     paramIndentingWriter.p("public class " + this.currentClass + " extends " + 
/* 1722 */       getName(this.tieBaseClass) + 
/* 1722 */       " implements Tie");
/*      */ 
/* 1727 */     if (!implementsRemote(localCompoundType)) {
/* 1728 */       paramIndentingWriter.pln(",");
/* 1729 */       paramIndentingWriter.p(getName("java.rmi.Remote"));
/*      */     }
/*      */ 
/* 1732 */     paramIndentingWriter.plnI(" {");
/*      */ 
/* 1735 */     paramIndentingWriter.pln();
/* 1736 */     paramIndentingWriter.pln("volatile private " + getName(localCompoundType) + " target = null;");
/* 1737 */     paramIndentingWriter.pln();
/*      */ 
/* 1740 */     writeIds(paramIndentingWriter, localCompoundType, true);
/*      */ 
/* 1743 */     paramIndentingWriter.pln();
/* 1744 */     paramIndentingWriter.plnI("public void setTarget(Remote target) {");
/* 1745 */     paramIndentingWriter.pln("this.target = (" + getName(localCompoundType) + ") target;");
/* 1746 */     paramIndentingWriter.pOln("}");
/*      */ 
/* 1749 */     paramIndentingWriter.pln();
/* 1750 */     paramIndentingWriter.plnI("public Remote getTarget() {");
/* 1751 */     paramIndentingWriter.pln("return target;");
/* 1752 */     paramIndentingWriter.pOln("}");
/*      */ 
/* 1755 */     paramIndentingWriter.pln();
/* 1756 */     write_tie_thisObject_method(paramIndentingWriter, idCorbaObject);
/*      */ 
/* 1759 */     paramIndentingWriter.pln();
/* 1760 */     write_tie_deactivate_method(paramIndentingWriter);
/*      */ 
/* 1763 */     paramIndentingWriter.pln();
/* 1764 */     paramIndentingWriter.plnI("public ORB orb() {");
/* 1765 */     paramIndentingWriter.pln("return _orb();");
/* 1766 */     paramIndentingWriter.pOln("}");
/*      */ 
/* 1769 */     paramIndentingWriter.pln();
/* 1770 */     write_tie_orb_method(paramIndentingWriter);
/*      */ 
/* 1773 */     paramIndentingWriter.pln();
/* 1774 */     write_tie__ids_method(paramIndentingWriter);
/*      */ 
/* 1777 */     CompoundType.Method[] arrayOfMethod = localCompoundType.getMethods();
/*      */ 
/* 1782 */     addNamesInUse(arrayOfMethod);
/* 1783 */     addNameInUse("target");
/* 1784 */     addNameInUse("_type_ids");
/*      */ 
/* 1787 */     paramIndentingWriter.pln();
/*      */ 
/* 1789 */     String str1 = getVariableName("in");
/* 1790 */     String str2 = getVariableName("_in");
/* 1791 */     String str3 = getVariableName("ex");
/* 1792 */     String str4 = getVariableName("method");
/* 1793 */     String str5 = getVariableName("reply");
/*      */ 
/* 1795 */     paramIndentingWriter.plnI("public OutputStream  _invoke(String " + str4 + ", InputStream " + str2 + ", " + "ResponseHandler " + str5 + ") throws SystemException {");
/*      */ 
/* 1798 */     if (arrayOfMethod.length > 0) {
/* 1799 */       paramIndentingWriter.plnI("try {");
/* 1800 */       paramIndentingWriter.pln(getName(localCompoundType) + " target = this.target;");
/* 1801 */       paramIndentingWriter.plnI("if (target == null) {");
/* 1802 */       paramIndentingWriter.pln("throw new java.io.IOException();");
/* 1803 */       paramIndentingWriter.pOln("}");
/* 1804 */       paramIndentingWriter.plnI(idExtInputStream + " " + str1 + " = ");
/* 1805 */       paramIndentingWriter.pln("(" + idExtInputStream + ") " + str2 + ";");
/* 1806 */       paramIndentingWriter.pO();
/*      */ 
/* 1811 */       StaticStringsHash localStaticStringsHash = getStringsHash(arrayOfMethod);
/*      */       int i;
/* 1813 */       if (localStaticStringsHash != null) {
/* 1814 */         paramIndentingWriter.plnI("switch (" + str4 + "." + localStaticStringsHash.method + ") {");
/* 1815 */         for (i = 0; i < localStaticStringsHash.buckets.length; i++) {
/* 1816 */           paramIndentingWriter.plnI("case " + localStaticStringsHash.keys[i] + ": ");
/* 1817 */           for (int j = 0; j < localStaticStringsHash.buckets[i].length; j++) {
/* 1818 */             CompoundType.Method localMethod2 = arrayOfMethod[localStaticStringsHash.buckets[i][j]];
/* 1819 */             if (j > 0) {
/* 1820 */               paramIndentingWriter.pO("} else ");
/*      */             }
/* 1822 */             paramIndentingWriter.plnI("if (" + str4 + ".equals(\"" + localMethod2.getIDLName() + "\")) {");
/* 1823 */             writeTieMethod(paramIndentingWriter, localCompoundType, localMethod2);
/*      */           }
/* 1825 */           paramIndentingWriter.pOln("}");
/* 1826 */           paramIndentingWriter.pO();
/*      */         }
/*      */       } else {
/* 1829 */         for (i = 0; i < arrayOfMethod.length; i++) {
/* 1830 */           CompoundType.Method localMethod1 = arrayOfMethod[i];
/* 1831 */           if (i > 0) {
/* 1832 */             paramIndentingWriter.pO("} else ");
/*      */           }
/*      */ 
/* 1835 */           paramIndentingWriter.plnI("if (" + str4 + ".equals(\"" + localMethod1.getIDLName() + "\")) {");
/* 1836 */           writeTieMethod(paramIndentingWriter, localCompoundType, localMethod1);
/*      */         }
/*      */       }
/*      */ 
/* 1840 */       if (localStaticStringsHash != null) {
/* 1841 */         paramIndentingWriter.pI();
/*      */       }
/*      */ 
/* 1848 */       if (localStaticStringsHash != null) {
/* 1849 */         paramIndentingWriter.pO();
/*      */       }
/* 1851 */       paramIndentingWriter.pOln("}");
/* 1852 */       paramIndentingWriter.pln("throw new " + getName(idBadMethodException) + "();");
/*      */ 
/* 1854 */       paramIndentingWriter.pOlnI("} catch (" + getName(idSystemException) + " " + str3 + ") {");
/* 1855 */       paramIndentingWriter.pln("throw " + str3 + ";");
/*      */ 
/* 1857 */       paramIndentingWriter.pOlnI("} catch (" + getName(idJavaLangThrowable) + " " + str3 + ") {");
/* 1858 */       paramIndentingWriter.pln("throw new " + getName(idPortableUnknownException) + "(" + str3 + ");");
/* 1859 */       paramIndentingWriter.pOln("}");
/*      */     }
/*      */     else
/*      */     {
/* 1863 */       paramIndentingWriter.pln("throw new " + getName(idBadMethodException) + "();");
/*      */     }
/*      */ 
/* 1866 */     paramIndentingWriter.pOln("}");
/*      */ 
/* 1870 */     writeCastArray(paramIndentingWriter);
/*      */ 
/* 1873 */     paramIndentingWriter.pOln("}");
/*      */   }
/*      */   public void catchWrongPolicy(IndentingWriter paramIndentingWriter) throws IOException {
/* 1876 */     paramIndentingWriter.pln("");
/*      */   }
/*      */   public void catchServantNotActive(IndentingWriter paramIndentingWriter) throws IOException {
/* 1879 */     paramIndentingWriter.pln("");
/*      */   }
/*      */   public void catchObjectNotActive(IndentingWriter paramIndentingWriter) throws IOException {
/* 1882 */     paramIndentingWriter.pln("");
/*      */   }
/*      */ 
/*      */   public void write_tie_thisObject_method(IndentingWriter paramIndentingWriter, Identifier paramIdentifier)
/*      */     throws IOException
/*      */   {
/* 1889 */     if (this.POATie) {
/* 1890 */       paramIndentingWriter.plnI("public " + paramIdentifier + " thisObject() {");
/*      */ 
/* 1902 */       paramIndentingWriter.pln("return _this_object();");
/* 1903 */       paramIndentingWriter.pOln("}");
/*      */     } else {
/* 1905 */       paramIndentingWriter.plnI("public " + paramIdentifier + " thisObject() {");
/* 1906 */       paramIndentingWriter.pln("return this;");
/* 1907 */       paramIndentingWriter.pOln("}");
/*      */     }
/*      */   }
/*      */ 
/*      */   public void write_tie_deactivate_method(IndentingWriter paramIndentingWriter)
/*      */     throws IOException
/*      */   {
/* 1914 */     if (this.POATie) {
/* 1915 */       paramIndentingWriter.plnI("public void deactivate() {");
/* 1916 */       paramIndentingWriter.pln("try{");
/* 1917 */       paramIndentingWriter.pln("_poa().deactivate_object(_poa().servant_to_id(this));");
/* 1918 */       paramIndentingWriter.pln("}catch (org.omg.PortableServer.POAPackage.WrongPolicy exception){");
/* 1919 */       catchWrongPolicy(paramIndentingWriter);
/* 1920 */       paramIndentingWriter.pln("}catch (org.omg.PortableServer.POAPackage.ObjectNotActive exception){");
/* 1921 */       catchObjectNotActive(paramIndentingWriter);
/* 1922 */       paramIndentingWriter.pln("}catch (org.omg.PortableServer.POAPackage.ServantNotActive exception){");
/* 1923 */       catchServantNotActive(paramIndentingWriter);
/* 1924 */       paramIndentingWriter.pln("}");
/* 1925 */       paramIndentingWriter.pOln("}");
/*      */     } else {
/* 1927 */       paramIndentingWriter.plnI("public void deactivate() {");
/* 1928 */       paramIndentingWriter.pln("_orb().disconnect(this);");
/* 1929 */       paramIndentingWriter.pln("_set_delegate(null);");
/* 1930 */       paramIndentingWriter.pln("target = null;");
/* 1931 */       paramIndentingWriter.pOln("}");
/*      */     }
/*      */   }
/*      */ 
/*      */   public void write_tie_orb_method(IndentingWriter paramIndentingWriter)
/*      */     throws IOException
/*      */   {
/* 1938 */     if (this.POATie) {
/* 1939 */       paramIndentingWriter.plnI("public void orb(ORB orb) {");
/*      */ 
/* 1949 */       paramIndentingWriter.pln("try {");
/* 1950 */       paramIndentingWriter.pln("    ((org.omg.CORBA_2_3.ORB)orb).set_delegate(this);");
/* 1951 */       paramIndentingWriter.pln("}");
/* 1952 */       paramIndentingWriter.pln("catch(ClassCastException e) {");
/* 1953 */       paramIndentingWriter.pln("    throw new org.omg.CORBA.BAD_PARAM");
/* 1954 */       paramIndentingWriter.pln("        (\"POA Servant requires an instance of org.omg.CORBA_2_3.ORB\");");
/* 1955 */       paramIndentingWriter.pln("}");
/* 1956 */       paramIndentingWriter.pOln("}");
/*      */     } else {
/* 1958 */       paramIndentingWriter.plnI("public void orb(ORB orb) {");
/* 1959 */       paramIndentingWriter.pln("orb.connect(this);");
/* 1960 */       paramIndentingWriter.pOln("}");
/*      */     }
/*      */   }
/*      */ 
/*      */   public void write_tie__ids_method(IndentingWriter paramIndentingWriter)
/*      */     throws IOException
/*      */   {
/* 1967 */     if (this.POATie) {
/* 1968 */       paramIndentingWriter.plnI("public String[] _all_interfaces(org.omg.PortableServer.POA poa, byte[] objectId){");
/* 1969 */       paramIndentingWriter.pln("return (String[]) _type_ids.clone();");
/* 1970 */       paramIndentingWriter.pOln("}");
/*      */     } else {
/* 1972 */       paramIndentingWriter.plnI("public String[] _ids() { ");
/* 1973 */       paramIndentingWriter.pln("return (String[]) _type_ids.clone();");
/* 1974 */       paramIndentingWriter.pOln("}");
/*      */     }
/*      */   }
/*      */ 
/*      */   StaticStringsHash getStringsHash(CompoundType.Method[] paramArrayOfMethod)
/*      */   {
/* 1980 */     if ((this.useHash) && (paramArrayOfMethod.length > 1)) {
/* 1981 */       String[] arrayOfString = new String[paramArrayOfMethod.length];
/* 1982 */       for (int i = 0; i < arrayOfString.length; i++) {
/* 1983 */         arrayOfString[i] = paramArrayOfMethod[i].getIDLName();
/*      */       }
/* 1985 */       return new StaticStringsHash(arrayOfString);
/*      */     }
/* 1987 */     return null;
/*      */   }
/*      */ 
/*      */   static boolean needNewReadStreamClass(Type paramType) {
/* 1991 */     if (paramType.isType(8192)) {
/* 1992 */       return true;
/*      */     }
/*      */ 
/* 1996 */     if (((paramType instanceof CompoundType)) && 
/* 1997 */       (((CompoundType)paramType)
/* 1997 */       .isAbstractBase())) {
/* 1998 */       return true;
/*      */     }
/* 2000 */     return needNewWriteStreamClass(paramType);
/*      */   }
/*      */ 
/*      */   static boolean needNewWriteStreamClass(Type paramType) {
/* 2004 */     switch (paramType.getTypeCode()) { case 1:
/*      */     case 2:
/*      */     case 4:
/*      */     case 8:
/*      */     case 16:
/*      */     case 32:
/*      */     case 64:
/*      */     case 128:
/*      */     case 256:
/* 2013 */       return false;
/*      */     case 512:
/* 2015 */       return true;
/*      */     case 1024:
/* 2016 */       return false;
/*      */     case 2048:
/* 2017 */       return false;
/*      */     case 4096:
/* 2018 */       return false;
/*      */     case 8192:
/* 2019 */       return false;
/*      */     case 16384:
/* 2020 */       return true;
/*      */     case 32768:
/* 2021 */       return true;
/*      */     case 65536:
/* 2022 */       return true;
/*      */     case 131072:
/* 2023 */       return true;
/*      */     case 262144:
/* 2024 */       return true;
/*      */     case 524288:
/* 2025 */       return false;
/*      */     }
/* 2027 */     throw new Error("unexpected type code: " + paramType.getTypeCode());
/*      */   }
/*      */ 
/*      */   String[] writeCopyArguments(CompoundType.Method paramMethod, IndentingWriter paramIndentingWriter)
/*      */     throws IOException
/*      */   {
/* 2039 */     Type[] arrayOfType = paramMethod.getArguments();
/* 2040 */     String[] arrayOfString1 = paramMethod.getArgumentNames();
/*      */ 
/* 2044 */     String[] arrayOfString2 = new String[arrayOfString1.length];
/* 2045 */     for (int i = 0; i < arrayOfString2.length; i++) {
/* 2046 */       arrayOfString2[i] = arrayOfString1[i];
/*      */     }
/*      */ 
/* 2056 */     i = 0;
/* 2057 */     boolean[] arrayOfBoolean = new boolean[arrayOfType.length];
/* 2058 */     int j = 0;
/* 2059 */     int k = 0;
/*      */ 
/* 2062 */     for (int m = 0; m < arrayOfType.length; m++) {
/* 2063 */       if (mustCopy(arrayOfType[m])) {
/* 2064 */         arrayOfBoolean[m] = true;
/* 2065 */         j++;
/* 2066 */         k = m;
/* 2067 */         if ((arrayOfType[m].getTypeCode() != 4096) && 
/* 2068 */           (arrayOfType[m]
/* 2068 */           .getTypeCode() != 65536))
/* 2069 */           i = 1;
/*      */       }
/*      */       else {
/* 2072 */         arrayOfBoolean[m] = false;
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/* 2077 */     if (j > 0)
/*      */     {
/* 2080 */       if (i != 0)
/*      */       {
/* 2084 */         for (m = 0; m < arrayOfType.length; m++) {
/* 2085 */           if (arrayOfType[m].getTypeCode() == 512) {
/* 2086 */             arrayOfBoolean[m] = true;
/* 2087 */             j++;
/*      */           }
/*      */ 
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/* 2094 */       if (j > 1)
/*      */       {
/* 2096 */         String str = getVariableName("copies");
/* 2097 */         paramIndentingWriter.p("Object[] " + str + " = Util.copyObjects(new Object[]{");
/* 2098 */         int n = 1;
/* 2099 */         for (int i1 = 0; i1 < arrayOfType.length; i1++) {
/* 2100 */           if (arrayOfBoolean[i1] != 0) {
/* 2101 */             if (n == 0) {
/* 2102 */               paramIndentingWriter.p(",");
/*      */             }
/* 2104 */             n = 0;
/* 2105 */             paramIndentingWriter.p(arrayOfString1[i1]);
/*      */           }
/*      */         }
/* 2108 */         paramIndentingWriter.pln("},_orb());");
/*      */ 
/* 2113 */         i1 = 0;
/* 2114 */         for (int i2 = 0; i2 < arrayOfType.length; i2++) {
/* 2115 */           if (arrayOfBoolean[i2] != 0) {
/* 2116 */             arrayOfString2[i2] = getVariableName(arrayOfString2[i2] + "Copy");
/* 2117 */             paramIndentingWriter.pln(getName(arrayOfType[i2]) + " " + arrayOfString2[i2] + " = (" + getName(arrayOfType[i2]) + ") " + str + "[" + i1++ + "];");
/*      */           }
/*      */         }
/*      */ 
/*      */       }
/*      */       else
/*      */       {
/* 2124 */         arrayOfString2[k] = getVariableName(arrayOfString2[k] + "Copy");
/* 2125 */         paramIndentingWriter.pln(getName(arrayOfType[k]) + " " + arrayOfString2[k] + " = (" + 
/* 2126 */           getName(arrayOfType[k]) + 
/* 2126 */           ") Util.copyObject(" + arrayOfString1[k] + ",_orb());");
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/* 2131 */     return arrayOfString2;
/*      */   }
/*      */ 
/*      */   String getRepositoryID(Type paramType)
/*      */   {
/* 2138 */     return IDLNames.replace(paramType.getRepositoryID(), "\\", "\\\\");
/*      */   }
/*      */ 
/*      */   String getExceptionRepositoryID(Type paramType) {
/* 2142 */     ClassType localClassType = (ClassType)paramType;
/* 2143 */     return IDLNames.getIDLRepositoryID(localClassType.getQualifiedIDLExceptionName(false));
/*      */   }
/*      */ 
/*      */   String getVariableName(String paramString) {
/* 2147 */     while (this.namesInUse.contains(paramString)) {
/* 2148 */       paramString = "$" + paramString;
/*      */     }
/*      */ 
/* 2151 */     return paramString;
/*      */   }
/*      */ 
/*      */   void addNamesInUse(CompoundType.Method[] paramArrayOfMethod) {
/* 2155 */     for (int i = 0; i < paramArrayOfMethod.length; i++)
/* 2156 */       addNamesInUse(paramArrayOfMethod[i]);
/*      */   }
/*      */ 
/*      */   void addNamesInUse(CompoundType.Method paramMethod)
/*      */   {
/* 2161 */     String[] arrayOfString = paramMethod.getArgumentNames();
/* 2162 */     for (int i = 0; i < arrayOfString.length; i++)
/* 2163 */       addNameInUse(arrayOfString[i]);
/*      */   }
/*      */ 
/*      */   void addNameInUse(String paramString)
/*      */   {
/* 2168 */     this.namesInUse.add(paramString);
/*      */   }
/*      */ 
/*      */   static boolean mustCopy(Type paramType) {
/* 2172 */     switch (paramType.getTypeCode()) { case 1:
/*      */     case 2:
/*      */     case 4:
/*      */     case 8:
/*      */     case 16:
/*      */     case 32:
/*      */     case 64:
/*      */     case 128:
/*      */     case 256:
/*      */     case 512:
/* 2182 */       return false;
/*      */     case 1024:
/* 2184 */       return true;
/*      */     case 2048:
/* 2186 */       return false;
/*      */     case 4096:
/*      */     case 8192:
/*      */     case 16384:
/*      */     case 32768:
/*      */     case 65536:
/*      */     case 131072:
/*      */     case 262144:
/*      */     case 524288:
/* 2195 */       return true;
/*      */     }
/* 2197 */     throw new Error("unexpected type code: " + paramType.getTypeCode());
/*      */   }
/*      */ 
/*      */   ValueType[] getStubExceptions(CompoundType.Method paramMethod, boolean paramBoolean)
/*      */   {
/* 2203 */     ValueType[] arrayOfValueType = paramMethod.getFilteredStubExceptions(paramMethod.getExceptions());
/*      */ 
/* 2210 */     if (paramBoolean) {
/* 2211 */       Arrays.sort(arrayOfValueType, new UserExceptionComparator());
/*      */     }
/*      */ 
/* 2214 */     return arrayOfValueType;
/*      */   }
/*      */ 
/*      */   ValueType[] getTieExceptions(CompoundType.Method paramMethod) {
/* 2218 */     return paramMethod.getUniqueCatchList(paramMethod.getImplExceptions());
/*      */   }
/*      */ 
/*      */   void writeTieMethod(IndentingWriter paramIndentingWriter, CompoundType paramCompoundType, CompoundType.Method paramMethod) throws IOException
/*      */   {
/* 2223 */     String str1 = paramMethod.getName();
/* 2224 */     Type[] arrayOfType = paramMethod.getArguments();
/* 2225 */     String[] arrayOfString = paramMethod.getArgumentNames();
/* 2226 */     Type localType = paramMethod.getReturnType();
/* 2227 */     ValueType[] arrayOfValueType = getTieExceptions(paramMethod);
/* 2228 */     String str2 = getVariableName("in");
/* 2229 */     String str3 = getVariableName("ex");
/* 2230 */     String str4 = getVariableName("out");
/* 2231 */     String str5 = getVariableName("reply");
/*      */ 
/* 2233 */     for (int i = 0; i < arrayOfType.length; i++) {
/* 2234 */       paramIndentingWriter.p(getName(arrayOfType[i]) + " " + arrayOfString[i] + " = ");
/* 2235 */       writeUnmarshalArgument(paramIndentingWriter, str2, arrayOfType[i], null);
/* 2236 */       paramIndentingWriter.pln();
/*      */     }
/*      */ 
/* 2239 */     i = arrayOfValueType != null ? 1 : 0;
/* 2240 */     int j = !localType.isType(1) ? 1 : 0;
/*      */ 
/* 2242 */     if ((i != 0) && (j != 0)) {
/* 2243 */       String str6 = testUtil(getName(localType), localType);
/* 2244 */       paramIndentingWriter.pln(str6 + " result;");
/*      */     }
/*      */ 
/* 2247 */     if (i != 0) {
/* 2248 */       paramIndentingWriter.plnI("try {");
/*      */     }
/* 2250 */     if (j != 0) {
/* 2251 */       if (i != 0)
/* 2252 */         paramIndentingWriter.p("result = ");
/*      */       else {
/* 2254 */         paramIndentingWriter.p(getName(localType) + " result = ");
/*      */       }
/*      */     }
/*      */ 
/* 2258 */     paramIndentingWriter.p("target." + str1 + "(");
/* 2259 */     for (int k = 0; k < arrayOfString.length; k++) {
/* 2260 */       if (k > 0)
/* 2261 */         paramIndentingWriter.p(", ");
/* 2262 */       paramIndentingWriter.p(arrayOfString[k]);
/*      */     }
/* 2264 */     paramIndentingWriter.pln(");");
/*      */ 
/* 2266 */     if (i != 0) {
/* 2267 */       for (k = 0; k < arrayOfValueType.length; k++) {
/* 2268 */         paramIndentingWriter.pOlnI("} catch (" + getName(arrayOfValueType[k]) + " " + str3 + ") {");
/*      */ 
/* 2272 */         if ((arrayOfValueType[k].isIDLEntityException()) && (!arrayOfValueType[k].isCORBAUserException()))
/*      */         {
/* 2276 */           String str7 = IDLNames.replace(arrayOfValueType[k].getQualifiedIDLName(false), "::", ".");
/* 2277 */           str7 = str7 + "Helper";
/* 2278 */           paramIndentingWriter.pln(idOutputStream + " " + str4 + " = " + str5 + ".createExceptionReply();");
/* 2279 */           paramIndentingWriter.pln(str7 + ".write(" + str4 + "," + str3 + ");");
/*      */         }
/*      */         else
/*      */         {
/* 2285 */           paramIndentingWriter.pln("String id = \"" + getExceptionRepositoryID(arrayOfValueType[k]) + "\";");
/* 2286 */           paramIndentingWriter.plnI(idExtOutputStream + " " + str4 + " = ");
/* 2287 */           paramIndentingWriter.pln("(" + idExtOutputStream + ") " + str5 + ".createExceptionReply();");
/* 2288 */           paramIndentingWriter.pOln(str4 + ".write_string(id);");
/* 2289 */           paramIndentingWriter.pln(str4 + ".write_value(" + str3 + "," + getName(arrayOfValueType[k]) + ".class);");
/*      */         }
/*      */ 
/* 2292 */         paramIndentingWriter.pln("return " + str4 + ";");
/*      */       }
/* 2294 */       paramIndentingWriter.pOln("}");
/*      */     }
/*      */ 
/* 2297 */     if (needNewWriteStreamClass(localType)) {
/* 2298 */       paramIndentingWriter.plnI(idExtOutputStream + " " + str4 + " = ");
/* 2299 */       paramIndentingWriter.pln("(" + idExtOutputStream + ") " + str5 + ".createReply();");
/* 2300 */       paramIndentingWriter.pO();
/*      */     } else {
/* 2302 */       paramIndentingWriter.pln("OutputStream " + str4 + " = " + str5 + ".createReply();");
/*      */     }
/*      */ 
/* 2305 */     if (j != 0) {
/* 2306 */       writeMarshalArgument(paramIndentingWriter, str4, localType, "result");
/* 2307 */       paramIndentingWriter.pln();
/*      */     }
/*      */ 
/* 2310 */     paramIndentingWriter.pln("return " + str4 + ";");
/*      */   }
/*      */ 
/*      */   void writeMarshalArguments(IndentingWriter paramIndentingWriter, String paramString, Type[] paramArrayOfType, String[] paramArrayOfString)
/*      */     throws IOException
/*      */   {
/* 2324 */     if (paramArrayOfType.length != paramArrayOfString.length) {
/* 2325 */       throw new Error("paramter type and name arrays different sizes");
/*      */     }
/*      */ 
/* 2328 */     for (int i = 0; i < paramArrayOfType.length; i++) {
/* 2329 */       writeMarshalArgument(paramIndentingWriter, paramString, paramArrayOfType[i], paramArrayOfString[i]);
/* 2330 */       if (i != paramArrayOfType.length - 1)
/* 2331 */         paramIndentingWriter.pln();
/*      */     }
/*      */   }
/*      */ 
/*      */   String testUtil(String paramString, Type paramType)
/*      */   {
/* 2342 */     if (paramString.equals("Util")) {
/* 2343 */       String str = paramType.getPackageName() + "." + paramString;
/* 2344 */       return str;
/*      */     }
/* 2346 */     return paramString;
/*      */   }
/*      */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     sun.rmi.rmic.iiop.StubGenerator
 * JD-Core Version:    0.6.2
 */