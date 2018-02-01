/*      */ package sun.tools.javac;
/*      */ 
/*      */ import java.io.BufferedInputStream;
/*      */ import java.io.DataInputStream;
/*      */ import java.io.EOFException;
/*      */ import java.io.File;
/*      */ import java.io.FileInputStream;
/*      */ import java.io.FileNotFoundException;
/*      */ import java.io.IOException;
/*      */ import java.io.InputStream;
/*      */ import java.io.InputStreamReader;
/*      */ import java.io.OutputStream;
/*      */ import java.io.PrintStream;
/*      */ import java.util.Enumeration;
/*      */ import java.util.HashSet;
/*      */ import java.util.Hashtable;
/*      */ import java.util.Set;
/*      */ import java.util.StringTokenizer;
/*      */ import java.util.Vector;
/*      */ import sun.tools.java.BinaryClass;
/*      */ import sun.tools.java.ClassDeclaration;
/*      */ import sun.tools.java.ClassDefinition;
/*      */ import sun.tools.java.ClassFile;
/*      */ import sun.tools.java.ClassNotFound;
/*      */ import sun.tools.java.ClassPath;
/*      */ import sun.tools.java.CompilerError;
/*      */ import sun.tools.java.Environment;
/*      */ import sun.tools.java.Identifier;
/*      */ import sun.tools.java.IdentifierToken;
/*      */ import sun.tools.java.Imports;
/*      */ import sun.tools.java.MemberDefinition;
/*      */ import sun.tools.java.Package;
/*      */ import sun.tools.java.Type;
/*      */ import sun.tools.tree.Node;
/*      */ 
/*      */ @Deprecated
/*      */ public class BatchEnvironment extends Environment
/*      */   implements ErrorConsumer
/*      */ {
/*      */   OutputStream out;
/*      */   protected ClassPath sourcePath;
/*      */   protected ClassPath binaryPath;
/*   64 */   Hashtable packages = new Hashtable(31);
/*      */ 
/*   69 */   Vector classesOrdered = new Vector();
/*      */ 
/*   74 */   Hashtable classes = new Hashtable(351);
/*      */   public int flags;
/*   89 */   public short majorVersion = 45;
/*   90 */   public short minorVersion = 3;
/*      */   public File covFile;
/*      */   public int nerrors;
/*      */   public int nwarnings;
/*      */   public int ndeprecations;
/*  109 */   Vector deprecationFiles = new Vector();
/*      */   ErrorConsumer errorConsumer;
/*      */   private Set exemptPackages;
/*      */   String errorFileName;
/*      */   ErrorMessage errors;
/*      */   private int errorsPushed;
/* 1160 */   public int errorLimit = 100;
/*      */   private boolean hitErrorLimit;
/*      */ 
/*      */   public BatchEnvironment(ClassPath paramClassPath)
/*      */   {
/*  122 */     this(System.out, paramClassPath);
/*      */   }
/*      */ 
/*      */   public BatchEnvironment(OutputStream paramOutputStream, ClassPath paramClassPath) {
/*  126 */     this(paramOutputStream, paramClassPath, (ErrorConsumer)null);
/*      */   }
/*      */ 
/*      */   public BatchEnvironment(OutputStream paramOutputStream, ClassPath paramClassPath, ErrorConsumer paramErrorConsumer)
/*      */   {
/*  131 */     this(paramOutputStream, paramClassPath, paramClassPath, paramErrorConsumer);
/*      */   }
/*      */ 
/*      */   public BatchEnvironment(ClassPath paramClassPath1, ClassPath paramClassPath2)
/*      */   {
/*  140 */     this(System.out, paramClassPath1, paramClassPath2);
/*      */   }
/*      */ 
/*      */   public BatchEnvironment(OutputStream paramOutputStream, ClassPath paramClassPath1, ClassPath paramClassPath2)
/*      */   {
/*  145 */     this(paramOutputStream, paramClassPath1, paramClassPath2, (ErrorConsumer)null);
/*      */   }
/*      */ 
/*      */   public BatchEnvironment(OutputStream paramOutputStream, ClassPath paramClassPath1, ClassPath paramClassPath2, ErrorConsumer paramErrorConsumer)
/*      */   {
/*  151 */     this.out = paramOutputStream;
/*  152 */     this.sourcePath = paramClassPath1;
/*  153 */     this.binaryPath = paramClassPath2;
/*  154 */     this.errorConsumer = (paramErrorConsumer == null ? this : paramErrorConsumer);
/*      */   }
/*      */ 
/*      */   static BatchEnvironment create(OutputStream paramOutputStream, String paramString1, String paramString2, String paramString3, String paramString4)
/*      */   {
/*  165 */     ClassPath[] arrayOfClassPath = classPaths(paramString1, paramString2, paramString3, paramString4);
/*      */ 
/*  167 */     return new BatchEnvironment(paramOutputStream, arrayOfClassPath[0], arrayOfClassPath[1]);
/*      */   }
/*      */ 
/*      */   protected static ClassPath[] classPaths(String paramString1, String paramString2, String paramString3, String paramString4)
/*      */   {
/*  177 */     StringBuffer localStringBuffer = new StringBuffer();
/*      */ 
/*  179 */     if (paramString2 == null)
/*      */     {
/*  183 */       paramString2 = System.getProperty("env.class.path");
/*  184 */       if (paramString2 == null) {
/*  185 */         paramString2 = ".";
/*      */       }
/*      */     }
/*  188 */     if (paramString1 == null) {
/*  189 */       paramString1 = paramString2;
/*      */     }
/*  191 */     if (paramString3 == null) {
/*  192 */       paramString3 = System.getProperty("sun.boot.class.path");
/*  193 */       if (paramString3 == null) {
/*  194 */         paramString3 = paramString2;
/*      */       }
/*      */     }
/*  197 */     appendPath(localStringBuffer, paramString3);
/*      */ 
/*  199 */     if (paramString4 == null) {
/*  200 */       paramString4 = System.getProperty("java.ext.dirs");
/*      */     }
/*  202 */     if (paramString4 != null) {
/*  203 */       StringTokenizer localStringTokenizer = new StringTokenizer(paramString4, File.pathSeparator);
/*      */ 
/*  205 */       while (localStringTokenizer.hasMoreTokens()) {
/*  206 */         String str1 = localStringTokenizer.nextToken();
/*  207 */         File localFile = new File(str1);
/*  208 */         if (!str1.endsWith(File.separator)) {
/*  209 */           str1 = str1 + File.separator;
/*      */         }
/*  211 */         if (localFile.isDirectory()) {
/*  212 */           String[] arrayOfString = localFile.list();
/*  213 */           for (int i = 0; i < arrayOfString.length; i++) {
/*  214 */             String str2 = arrayOfString[i];
/*  215 */             if (str2.endsWith(".jar")) {
/*  216 */               appendPath(localStringBuffer, str1 + str2);
/*      */             }
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/*      */ 
/*  223 */     appendPath(localStringBuffer, paramString2);
/*      */ 
/*  225 */     ClassPath localClassPath1 = new ClassPath(paramString1);
/*  226 */     ClassPath localClassPath2 = new ClassPath(localStringBuffer.toString());
/*      */ 
/*  228 */     return new ClassPath[] { localClassPath1, localClassPath2 };
/*      */   }
/*      */ 
/*      */   private static void appendPath(StringBuffer paramStringBuffer, String paramString) {
/*  232 */     if (paramString.length() > 0) {
/*  233 */       if (paramStringBuffer.length() > 0) {
/*  234 */         paramStringBuffer.append(File.pathSeparator);
/*      */       }
/*  236 */       paramStringBuffer.append(paramString);
/*      */     }
/*      */   }
/*      */ 
/*      */   public int getFlags()
/*      */   {
/*  244 */     return this.flags;
/*      */   }
/*      */ 
/*      */   public short getMajorVersion()
/*      */   {
/*  251 */     return this.majorVersion;
/*      */   }
/*      */ 
/*      */   public short getMinorVersion()
/*      */   {
/*  258 */     return this.minorVersion;
/*      */   }
/*      */ 
/*      */   public File getcovFile()
/*      */   {
/*  266 */     return this.covFile;
/*      */   }
/*      */ 
/*      */   public Enumeration getClasses()
/*      */   {
/*  275 */     return this.classesOrdered.elements();
/*      */   }
/*      */ 
/*      */   public boolean isExemptPackage(Identifier paramIdentifier)
/*      */   {
/*  290 */     if (this.exemptPackages == null)
/*      */     {
/*  293 */       setExemptPackages();
/*      */     }
/*      */ 
/*  296 */     return this.exemptPackages.contains(paramIdentifier);
/*      */   }
/*      */ 
/*      */   private void setExemptPackages()
/*      */   {
/*  320 */     this.exemptPackages = new HashSet(101);
/*      */ 
/*  323 */     for (Enumeration localEnumeration = getClasses(); localEnumeration.hasMoreElements(); ) {
/*  324 */       ClassDeclaration localClassDeclaration = (ClassDeclaration)localEnumeration.nextElement();
/*  325 */       if (localClassDeclaration.getStatus() == 4) {
/*  326 */         SourceClass localSourceClass = (SourceClass)localClassDeclaration.getClassDefinition();
/*  327 */         if (!localSourceClass.isLocal())
/*      */         {
/*  330 */           Identifier localIdentifier = localSourceClass.getImports().getCurrentPackage();
/*      */ 
/*  334 */           while ((localIdentifier != idNull) && (this.exemptPackages.add(localIdentifier))) {
/*  335 */             localIdentifier = localIdentifier.getQualifier();
/*      */           }
/*      */ 
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/*  348 */     if (!this.exemptPackages.contains(idJavaLang))
/*      */     {
/*  350 */       this.exemptPackages.add(idJavaLang);
/*      */       try
/*      */       {
/*  353 */         if (!getPackage(idJavaLang).exists())
/*      */         {
/*  355 */           error(0L, "package.not.found.strong", idJavaLang);
/*  356 */           return;
/*      */         }
/*      */       }
/*      */       catch (IOException localIOException)
/*      */       {
/*  361 */         error(0L, "io.exception.package", idJavaLang);
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   public ClassDeclaration getClassDeclaration(Identifier paramIdentifier)
/*      */   {
/*  388 */     return getClassDeclaration(Type.tClass(paramIdentifier));
/*      */   }
/*      */ 
/*      */   public ClassDeclaration getClassDeclaration(Type paramType) {
/*  392 */     ClassDeclaration localClassDeclaration = (ClassDeclaration)this.classes.get(paramType);
/*  393 */     if (localClassDeclaration == null) {
/*  394 */       this.classes.put(paramType, localClassDeclaration = new ClassDeclaration(paramType.getClassName()));
/*  395 */       this.classesOrdered.addElement(localClassDeclaration);
/*      */     }
/*  397 */     return localClassDeclaration;
/*      */   }
/*      */ 
/*      */   public boolean classExists(Identifier paramIdentifier)
/*      */   {
/*  405 */     if (paramIdentifier.isInner()) {
/*  406 */       paramIdentifier = paramIdentifier.getTopName();
/*      */     }
/*  408 */     Type localType = Type.tClass(paramIdentifier);
/*      */     try {
/*  410 */       ClassDeclaration localClassDeclaration = (ClassDeclaration)this.classes.get(localType);
/*      */ 
/*  412 */       return localClassDeclaration != null ? localClassDeclaration.getName().equals(paramIdentifier) : 
/*  412 */         getPackage(paramIdentifier
/*  412 */         .getQualifier()).classExists(paramIdentifier.getName()); } catch (IOException localIOException) {
/*      */     }
/*  414 */     return true;
/*      */   }
/*      */ 
/*      */   public Package getPackage(Identifier paramIdentifier)
/*      */     throws IOException
/*      */   {
/*  451 */     Package localPackage = (Package)this.packages.get(paramIdentifier);
/*  452 */     if (localPackage == null) {
/*  453 */       this.packages.put(paramIdentifier, localPackage = new Package(this.sourcePath, this.binaryPath, paramIdentifier));
/*      */     }
/*  455 */     return localPackage;
/*      */   }
/*      */ 
/*      */   public void parseFile(ClassFile paramClassFile) throws FileNotFoundException {
/*  462 */     long l = System.currentTimeMillis();
/*      */ 
/*  466 */     dtEnter("parseFile: PARSING SOURCE " + paramClassFile);
/*      */ 
/*  468 */     Environment localEnvironment = new Environment(this, paramClassFile);
/*      */     InputStream localInputStream;
/*      */     BatchParser localBatchParser;
/*      */     try {
/*  471 */       localInputStream = paramClassFile.getInputStream();
/*  472 */       localEnvironment.setCharacterEncoding(getCharacterEncoding());
/*      */ 
/*  474 */       localBatchParser = new BatchParser(localEnvironment, localInputStream);
/*      */     } catch (IOException localIOException1) {
/*  476 */       dtEvent("parseFile: IO EXCEPTION " + paramClassFile);
/*  477 */       throw new FileNotFoundException();
/*      */     }
/*      */     try
/*      */     {
/*  481 */       localBatchParser.parseFile();
/*      */     } catch (Exception localException) {
/*  483 */       throw new CompilerError(localException);
/*      */     }
/*      */     try
/*      */     {
/*  487 */       localInputStream.close();
/*      */     }
/*      */     catch (IOException localIOException2)
/*      */     {
/*      */     }
/*  492 */     if (verbose()) {
/*  493 */       l = System.currentTimeMillis() - l;
/*  494 */       output(Main.getText("benv.parsed_in", paramClassFile.getPath(), 
/*  495 */         Long.toString(l)));
/*      */     }
/*      */ 
/*  498 */     if (localBatchParser.classes.size() == 0)
/*      */     {
/*  505 */       localBatchParser.imports.resolve(localEnvironment);
/*      */     }
/*      */     else
/*      */     {
/*  530 */       Enumeration localEnumeration = localBatchParser.classes.elements();
/*      */ 
/*  533 */       ClassDefinition localClassDefinition1 = (ClassDefinition)localEnumeration.nextElement();
/*  534 */       if (localClassDefinition1.isInnerClass()) {
/*  535 */         throw new CompilerError("BatchEnvironment, first is inner");
/*      */       }
/*      */ 
/*  538 */       Object localObject = localClassDefinition1;
/*      */ 
/*  540 */       while (localEnumeration.hasMoreElements()) {
/*  541 */         ClassDefinition localClassDefinition2 = (ClassDefinition)localEnumeration.nextElement();
/*      */ 
/*  543 */         if (!localClassDefinition2.isInnerClass())
/*      */         {
/*  546 */           ((ClassDefinition)localObject).addDependency(localClassDefinition2.getClassDeclaration());
/*  547 */           localClassDefinition2.addDependency(((ClassDefinition)localObject).getClassDeclaration());
/*  548 */           localObject = localClassDefinition2;
/*      */         }
/*      */       }
/*      */ 
/*  552 */       if (localObject != localClassDefinition1) {
/*  553 */         ((ClassDefinition)localObject).addDependency(localClassDefinition1.getClassDeclaration());
/*  554 */         localClassDefinition1.addDependency(((ClassDefinition)localObject).getClassDeclaration());
/*      */       }
/*      */     }
/*      */ 
/*  558 */     dtExit("parseFile: SOURCE PARSED " + paramClassFile);
/*      */   }
/*      */ 
/*      */   BinaryClass loadFile(ClassFile paramClassFile)
/*      */     throws IOException
/*      */   {
/*  565 */     long l = System.currentTimeMillis();
/*  566 */     InputStream localInputStream = paramClassFile.getInputStream();
/*  567 */     BinaryClass localBinaryClass = null;
/*      */ 
/*  569 */     dtEnter("loadFile: LOADING CLASSFILE " + paramClassFile);
/*      */     try
/*      */     {
/*  572 */       DataInputStream localDataInputStream = new DataInputStream(new BufferedInputStream(localInputStream));
/*      */ 
/*  574 */       localBinaryClass = BinaryClass.load(new Environment(this, paramClassFile), localDataInputStream, 
/*  575 */         loadFileFlags());
/*      */     } catch (ClassFormatError localClassFormatError) {
/*  577 */       error(0L, "class.format", paramClassFile.getPath(), localClassFormatError.getMessage());
/*  578 */       dtExit("loadFile: CLASS FORMAT ERROR " + paramClassFile);
/*  579 */       return null;
/*      */     }
/*      */     catch (EOFException localEOFException)
/*      */     {
/*  584 */       error(0L, "truncated.class", paramClassFile.getPath());
/*  585 */       return null;
/*      */     }
/*      */ 
/*  588 */     localInputStream.close();
/*  589 */     if (verbose()) {
/*  590 */       l = System.currentTimeMillis() - l;
/*  591 */       output(Main.getText("benv.loaded_in", paramClassFile.getPath(), 
/*  592 */         Long.toString(l)));
/*      */     }
/*      */ 
/*  595 */     dtExit("loadFile: CLASSFILE LOADED " + paramClassFile);
/*      */ 
/*  597 */     return localBinaryClass;
/*      */   }
/*      */ 
/*      */   int loadFileFlags()
/*      */   {
/*  604 */     return 0;
/*      */   }
/*      */ 
/*      */   boolean needsCompilation(Hashtable paramHashtable, ClassDeclaration paramClassDeclaration)
/*      */   {
/*  611 */     switch (paramClassDeclaration.getStatus())
/*      */     {
/*      */     case 0:
/*  614 */       dtEnter("needsCompilation: UNDEFINED " + paramClassDeclaration.getName());
/*  615 */       loadDefinition(paramClassDeclaration);
/*  616 */       return needsCompilation(paramHashtable, paramClassDeclaration);
/*      */     case 1:
/*  619 */       dtEnter("needsCompilation: UNDECIDED " + paramClassDeclaration.getName());
/*      */       BinaryClass localBinaryClass;
/*      */       Enumeration localEnumeration;
/*  620 */       if (paramHashtable.get(paramClassDeclaration) == null) {
/*  621 */         paramHashtable.put(paramClassDeclaration, paramClassDeclaration);
/*      */ 
/*  623 */         localBinaryClass = (BinaryClass)paramClassDeclaration.getClassDefinition();
/*  624 */         for (localEnumeration = localBinaryClass.getDependencies(); localEnumeration.hasMoreElements(); ) {
/*  625 */           ClassDeclaration localClassDeclaration = (ClassDeclaration)localEnumeration.nextElement();
/*  626 */           if (needsCompilation(paramHashtable, localClassDeclaration))
/*      */           {
/*  628 */             paramClassDeclaration.setDefinition(localBinaryClass, 3);
/*  629 */             dtExit("needsCompilation: YES (source) " + paramClassDeclaration.getName());
/*  630 */             return true;
/*      */           }
/*      */         }
/*      */       }
/*  634 */       dtExit("needsCompilation: NO (undecided) " + paramClassDeclaration.getName());
/*  635 */       return false;
/*      */     case 2:
/*  639 */       dtEnter("needsCompilation: BINARY " + paramClassDeclaration.getName());
/*  640 */       dtExit("needsCompilation: NO (binary) " + paramClassDeclaration.getName());
/*      */ 
/*  642 */       return false;
/*      */     }
/*      */ 
/*  646 */     dtExit("needsCompilation: YES " + paramClassDeclaration.getName());
/*  647 */     return true;
/*      */   }
/*      */ 
/*      */   public void loadDefinition(ClassDeclaration paramClassDeclaration)
/*      */   {
/*  658 */     dtEnter("loadDefinition: ENTER " + paramClassDeclaration
/*  659 */       .getName() + ", status " + paramClassDeclaration.getStatus());
/*      */     Object localObject1;
/*      */     Object localObject2;
/*      */     Object localObject3;
/*  660 */     switch (paramClassDeclaration.getStatus())
/*      */     {
/*      */     case 0:
/*  663 */       dtEvent("loadDefinition: STATUS IS UNDEFINED");
/*  664 */       localObject1 = paramClassDeclaration.getName();
/*      */       try
/*      */       {
/*  667 */         localObject2 = getPackage(((Identifier)localObject1).getQualifier());
/*      */       }
/*      */       catch (IOException localIOException1)
/*      */       {
/*  671 */         paramClassDeclaration.setDefinition(null, 7);
/*      */ 
/*  673 */         error(0L, "io.exception", paramClassDeclaration);
/*      */ 
/*  675 */         dtExit("loadDefinition: IO EXCEPTION (package)");
/*  676 */         return;
/*      */       }
/*  678 */       localObject3 = ((Package)localObject2).getBinaryFile(((Identifier)localObject1).getName());
/*  679 */       if (localObject3 == null)
/*      */       {
/*  681 */         paramClassDeclaration.setDefinition(null, 3);
/*      */ 
/*  683 */         dtExit("loadDefinition: MUST BE SOURCE (no binary) " + paramClassDeclaration
/*  684 */           .getName());
/*  685 */         return;
/*      */       }
/*      */ 
/*  688 */       ClassFile localClassFile = ((Package)localObject2).getSourceFile(((Identifier)localObject1).getName());
/*  689 */       if (localClassFile == null)
/*      */       {
/*  691 */         dtEvent("loadDefinition: NO SOURCE " + paramClassDeclaration.getName());
/*  692 */         localBinaryClass = null;
/*      */         try {
/*  694 */           localBinaryClass = loadFile((ClassFile)localObject3);
/*      */         }
/*      */         catch (IOException localIOException4)
/*      */         {
/*  698 */           paramClassDeclaration.setDefinition(null, 7);
/*      */ 
/*  700 */           error(0L, "io.exception", localObject3);
/*      */ 
/*  702 */           dtExit("loadDefinition: IO EXCEPTION (binary)");
/*  703 */           return;
/*      */         }
/*  705 */         if ((localBinaryClass != null) && (!localBinaryClass.getName().equals(localObject1))) {
/*  706 */           error(0L, "wrong.class", ((ClassFile)localObject3).getPath(), paramClassDeclaration, localBinaryClass);
/*  707 */           localBinaryClass = null;
/*      */ 
/*  709 */           dtEvent("loadDefinition: WRONG CLASS (binary)");
/*      */         }
/*  711 */         if (localBinaryClass == null)
/*      */         {
/*  713 */           paramClassDeclaration.setDefinition(null, 7);
/*      */ 
/*  715 */           dtExit("loadDefinition: NOT FOUND (source or binary)");
/*  716 */           return;
/*      */         }
/*      */ 
/*  720 */         if (localBinaryClass.getSource() != null) {
/*  721 */           localClassFile = new ClassFile(new File((String)localBinaryClass.getSource()));
/*      */ 
/*  723 */           localClassFile = ((Package)localObject2).getSourceFile(localClassFile.getName());
/*  724 */           if ((localClassFile != null) && (localClassFile.exists()))
/*      */           {
/*  726 */             dtEvent("loadDefinition: FILENAME IN BINARY " + localClassFile);
/*      */ 
/*  728 */             if (localClassFile.lastModified() > ((ClassFile)localObject3).lastModified())
/*      */             {
/*  730 */               paramClassDeclaration.setDefinition(localBinaryClass, 3);
/*      */ 
/*  732 */               dtEvent("loadDefinition: SOURCE IS NEWER " + localClassFile);
/*      */ 
/*  734 */               localBinaryClass.loadNested(this);
/*      */ 
/*  736 */               dtExit("loadDefinition: MUST BE SOURCE " + paramClassDeclaration
/*  737 */                 .getName());
/*  738 */               return;
/*      */             }
/*  740 */             if (dependencies()) {
/*  741 */               paramClassDeclaration.setDefinition(localBinaryClass, 1);
/*      */ 
/*  743 */               dtEvent("loadDefinition: UNDECIDED " + paramClassDeclaration
/*  744 */                 .getName());
/*      */             } else {
/*  746 */               paramClassDeclaration.setDefinition(localBinaryClass, 2);
/*      */ 
/*  748 */               dtEvent("loadDefinition: MUST BE BINARY " + paramClassDeclaration
/*  749 */                 .getName());
/*      */             }
/*  751 */             localBinaryClass.loadNested(this);
/*      */ 
/*  753 */             dtExit("loadDefinition: EXIT " + paramClassDeclaration
/*  754 */               .getName() + ", status " + paramClassDeclaration.getStatus());
/*  755 */             return;
/*      */           }
/*      */ 
/*      */         }
/*      */ 
/*  760 */         paramClassDeclaration.setDefinition(localBinaryClass, 2);
/*      */ 
/*  762 */         dtEvent("loadDefinition: MUST BE BINARY (no source) " + paramClassDeclaration
/*  763 */           .getName());
/*  764 */         localBinaryClass.loadNested(this);
/*      */ 
/*  766 */         dtExit("loadDefinition: EXIT " + paramClassDeclaration
/*  767 */           .getName() + ", status " + paramClassDeclaration.getStatus());
/*  768 */         return;
/*      */       }
/*  770 */       BinaryClass localBinaryClass = null;
/*      */       try {
/*  772 */         if (localClassFile.lastModified() > ((ClassFile)localObject3).lastModified())
/*      */         {
/*  774 */           paramClassDeclaration.setDefinition(null, 3);
/*      */ 
/*  776 */           dtEvent("loadDefinition: MUST BE SOURCE (younger than binary) " + paramClassDeclaration
/*  777 */             .getName());
/*  778 */           return;
/*      */         }
/*  780 */         localBinaryClass = loadFile((ClassFile)localObject3);
/*      */       } catch (IOException localIOException5) {
/*  782 */         error(0L, "io.exception", localObject3);
/*      */ 
/*  784 */         dtEvent("loadDefinition: IO EXCEPTION (binary)");
/*      */       }
/*  786 */       if ((localBinaryClass != null) && (!localBinaryClass.getName().equals(localObject1))) {
/*  787 */         error(0L, "wrong.class", ((ClassFile)localObject3).getPath(), paramClassDeclaration, localBinaryClass);
/*  788 */         localBinaryClass = null;
/*      */ 
/*  790 */         dtEvent("loadDefinition: WRONG CLASS (binary)");
/*      */       }
/*  792 */       if (localBinaryClass != null) {
/*  793 */         Identifier localIdentifier = localBinaryClass.getName();
/*  794 */         if (localIdentifier.equals(paramClassDeclaration.getName())) {
/*  795 */           if (dependencies()) {
/*  796 */             paramClassDeclaration.setDefinition(localBinaryClass, 1);
/*      */ 
/*  798 */             dtEvent("loadDefinition: UNDECIDED " + localIdentifier);
/*      */           } else {
/*  800 */             paramClassDeclaration.setDefinition(localBinaryClass, 2);
/*      */ 
/*  802 */             dtEvent("loadDefinition: MUST BE BINARY " + localIdentifier);
/*      */           }
/*      */         } else {
/*  805 */           paramClassDeclaration.setDefinition(null, 7);
/*      */ 
/*  807 */           dtEvent("loadDefinition: NOT FOUND (source or binary)");
/*  808 */           if (dependencies()) {
/*  809 */             getClassDeclaration(localIdentifier).setDefinition(localBinaryClass, 1);
/*      */ 
/*  811 */             dtEvent("loadDefinition: UNDECIDED " + localIdentifier);
/*      */           } else {
/*  813 */             getClassDeclaration(localIdentifier).setDefinition(localBinaryClass, 2);
/*      */ 
/*  815 */             dtEvent("loadDefinition: MUST BE BINARY " + localIdentifier);
/*      */           }
/*      */         }
/*      */       } else {
/*  819 */         paramClassDeclaration.setDefinition(null, 7);
/*      */ 
/*  821 */         dtEvent("loadDefinition: NOT FOUND (source or binary)");
/*      */       }
/*  823 */       if ((localBinaryClass != null) && (localBinaryClass == paramClassDeclaration.getClassDefinition()))
/*  824 */         localBinaryClass.loadNested(this);
/*  825 */       dtExit("loadDefinition: EXIT " + paramClassDeclaration
/*  826 */         .getName() + ", status " + paramClassDeclaration.getStatus());
/*  827 */       return;
/*      */     case 1:
/*  831 */       dtEvent("loadDefinition: STATUS IS UNDECIDED");
/*  832 */       localObject1 = new Hashtable();
/*  833 */       if (!needsCompilation((Hashtable)localObject1, paramClassDeclaration))
/*      */       {
/*  835 */         for (localObject2 = ((Hashtable)localObject1).keys(); ((Enumeration)localObject2).hasMoreElements(); ) {
/*  836 */           localObject3 = (ClassDeclaration)((Enumeration)localObject2).nextElement();
/*  837 */           if (((ClassDeclaration)localObject3).getStatus() == 1)
/*      */           {
/*  839 */             ((ClassDeclaration)localObject3).setDefinition(((ClassDeclaration)localObject3).getClassDefinition(), 2);
/*      */ 
/*  841 */             dtEvent("loadDefinition: MUST BE BINARY " + localObject3);
/*      */           }
/*      */         }
/*      */       }
/*  845 */       dtExit("loadDefinition: EXIT " + paramClassDeclaration
/*  846 */         .getName() + ", status " + paramClassDeclaration.getStatus());
/*  847 */       return;
/*      */     case 3:
/*  851 */       dtEvent("loadDefinition: STATUS IS SOURCE");
/*  852 */       localObject1 = null;
/*  853 */       localObject2 = null;
/*      */       Object localObject4;
/*  854 */       if (paramClassDeclaration.getClassDefinition() != null)
/*      */       {
/*      */         try {
/*  857 */           localObject2 = getPackage(paramClassDeclaration.getName().getQualifier());
/*  858 */           localObject1 = ((Package)localObject2).getSourceFile((String)paramClassDeclaration.getClassDefinition().getSource());
/*      */         } catch (IOException localIOException2) {
/*  860 */           error(0L, "io.exception", paramClassDeclaration);
/*      */ 
/*  862 */           dtEvent("loadDefinition: IO EXCEPTION (package)");
/*      */         }
/*  864 */         if (localObject1 == null) {
/*  865 */           localObject4 = (String)paramClassDeclaration.getClassDefinition().getSource();
/*  866 */           localObject1 = new ClassFile(new File((String)localObject4));
/*      */         }
/*      */       }
/*      */       else {
/*  870 */         localObject4 = paramClassDeclaration.getName();
/*      */         try {
/*  872 */           localObject2 = getPackage(((Identifier)localObject4).getQualifier());
/*  873 */           localObject1 = ((Package)localObject2).getSourceFile(((Identifier)localObject4).getName());
/*      */         } catch (IOException localIOException3) {
/*  875 */           error(0L, "io.exception", paramClassDeclaration);
/*      */ 
/*  877 */           dtEvent("loadDefinition: IO EXCEPTION (package)");
/*      */         }
/*  879 */         if (localObject1 == null)
/*      */         {
/*  881 */           paramClassDeclaration.setDefinition(null, 7);
/*      */ 
/*  883 */           dtExit("loadDefinition: SOURCE NOT FOUND " + paramClassDeclaration
/*  884 */             .getName() + ", status " + paramClassDeclaration.getStatus());
/*  885 */           return;
/*      */         }
/*      */       }
/*      */       try {
/*  889 */         parseFile((ClassFile)localObject1);
/*      */       } catch (FileNotFoundException localFileNotFoundException) {
/*  891 */         error(0L, "io.exception", localObject1);
/*  892 */         dtEvent("loadDefinition: IO EXCEPTION (source)");
/*      */       }
/*  894 */       if ((paramClassDeclaration.getClassDefinition() == null) || (paramClassDeclaration.getStatus() == 3))
/*      */       {
/*  896 */         error(0L, "wrong.source", ((ClassFile)localObject1).getPath(), paramClassDeclaration, localObject2);
/*  897 */         paramClassDeclaration.setDefinition(null, 7);
/*      */ 
/*  899 */         dtEvent("loadDefinition: WRONG CLASS (source) " + paramClassDeclaration
/*  900 */           .getName());
/*      */       }
/*  902 */       dtExit("loadDefinition: EXIT " + paramClassDeclaration
/*  903 */         .getName() + ", status " + paramClassDeclaration.getStatus());
/*  904 */       return;
/*      */     case 2:
/*      */     }
/*  907 */     dtExit("loadDefinition: EXIT " + paramClassDeclaration
/*  908 */       .getName() + ", status " + paramClassDeclaration.getStatus());
/*      */   }
/*      */ 
/*      */   public ClassDefinition makeClassDefinition(Environment paramEnvironment, long paramLong, IdentifierToken paramIdentifierToken1, String paramString, int paramInt, IdentifierToken paramIdentifierToken2, IdentifierToken[] paramArrayOfIdentifierToken, ClassDefinition paramClassDefinition)
/*      */   {
/*  922 */     Identifier localIdentifier1 = paramIdentifierToken1.getName();
/*  923 */     long l = paramIdentifierToken1.getWhere();
/*      */ 
/*  926 */     String str = null;
/*  927 */     ClassDefinition localClassDefinition = null;
/*      */ 
/*  939 */     Identifier localIdentifier3 = null;
/*      */     Identifier localIdentifier2;
/*  941 */     if ((localIdentifier1.isQualified()) || (localIdentifier1.isInner())) {
/*  942 */       localIdentifier2 = localIdentifier1;
/*  943 */     } else if ((paramInt & 0x30000) != 0)
/*      */     {
/*  948 */       localClassDefinition = paramClassDefinition.getTopClass();
/*      */ 
/*  955 */       for (int i = 1; ; i++) {
/*  956 */         str = i + (localIdentifier1.equals(idNull) ? "" : new StringBuilder().append("$").append(localIdentifier1).toString());
/*  957 */         if (localClassDefinition.getLocalClass(str) == null) {
/*      */           break;
/*      */         }
/*      */       }
/*  961 */       localObject = localClassDefinition.getName();
/*  962 */       localIdentifier2 = Identifier.lookupInner((Identifier)localObject, Identifier.lookup(str));
/*      */ 
/*  964 */       if ((paramInt & 0x10000) != 0) {
/*  965 */         localIdentifier3 = idNull;
/*      */       }
/*      */       else
/*  968 */         localIdentifier3 = localIdentifier1;
/*      */     }
/*  970 */     else if (paramClassDefinition != null)
/*      */     {
/*  972 */       localIdentifier2 = Identifier.lookupInner(paramClassDefinition.getName(), localIdentifier1);
/*      */     } else {
/*  974 */       localIdentifier2 = localIdentifier1;
/*      */     }
/*      */ 
/*  978 */     Object localObject = paramEnvironment.getClassDeclaration(localIdentifier2);
/*      */ 
/*  981 */     if (((ClassDeclaration)localObject).isDefined()) {
/*  982 */       paramEnvironment.error(l, "class.multidef", ((ClassDeclaration)localObject)
/*  983 */         .getName(), ((ClassDeclaration)localObject).getClassDefinition().getSource());
/*      */ 
/*  985 */       localObject = new ClassDeclaration(localIdentifier2);
/*      */     }
/*      */ 
/*  988 */     if ((paramIdentifierToken2 == null) && (!localIdentifier2.equals(idJavaLangObject))) {
/*  989 */       paramIdentifierToken2 = new IdentifierToken(idJavaLangObject);
/*      */     }
/*      */ 
/*  992 */     SourceClass localSourceClass = new SourceClass(paramEnvironment, paramLong, (ClassDeclaration)localObject, paramString, paramInt, paramIdentifierToken2, paramArrayOfIdentifierToken, (SourceClass)paramClassDefinition, localIdentifier3);
/*      */ 
/*  997 */     if (paramClassDefinition != null)
/*      */     {
/*  999 */       paramClassDefinition.addMember(paramEnvironment, new SourceMember(localSourceClass));
/*      */ 
/* 1006 */       if ((paramInt & 0x30000) != 0) {
/* 1007 */         localClassDefinition.addLocalClass(localSourceClass, str);
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/* 1015 */     return localSourceClass;
/*      */   }
/*      */ 
/*      */   public MemberDefinition makeMemberDefinition(Environment paramEnvironment, long paramLong, ClassDefinition paramClassDefinition, String paramString, int paramInt, Type paramType, Identifier paramIdentifier, IdentifierToken[] paramArrayOfIdentifierToken1, IdentifierToken[] paramArrayOfIdentifierToken2, Object paramObject)
/*      */   {
/* 1028 */     dtEvent("makeMemberDefinition: " + paramIdentifier + " IN " + paramClassDefinition);
/* 1029 */     Vector localVector = null;
/* 1030 */     if (paramArrayOfIdentifierToken1 != null) {
/* 1031 */       localVector = new Vector(paramArrayOfIdentifierToken1.length);
/* 1032 */       for (int i = 0; i < paramArrayOfIdentifierToken1.length; i++) {
/* 1033 */         localVector.addElement(paramArrayOfIdentifierToken1[i]);
/*      */       }
/*      */     }
/* 1036 */     SourceMember localSourceMember = new SourceMember(paramLong, paramClassDefinition, paramString, paramInt, paramType, paramIdentifier, localVector, paramArrayOfIdentifierToken2, (Node)paramObject);
/*      */ 
/* 1038 */     paramClassDefinition.addMember(paramEnvironment, localSourceMember);
/* 1039 */     return localSourceMember;
/*      */   }
/*      */ 
/*      */   public void shutdown()
/*      */   {
/*      */     try
/*      */     {
/* 1047 */       if (this.sourcePath != null) {
/* 1048 */         this.sourcePath.close();
/*      */       }
/* 1050 */       if ((this.binaryPath != null) && (this.binaryPath != this.sourcePath))
/* 1051 */         this.binaryPath.close();
/*      */     }
/*      */     catch (IOException localIOException) {
/* 1054 */       output(Main.getText("benv.failed_to_close_class_path", localIOException
/* 1055 */         .toString()));
/*      */     }
/* 1057 */     this.sourcePath = null;
/* 1058 */     this.binaryPath = null;
/*      */ 
/* 1060 */     super.shutdown();
/*      */   }
/*      */ 
/*      */   public String errorString(String paramString, Object paramObject1, Object paramObject2, Object paramObject3)
/*      */   {
/* 1068 */     String str = null;
/*      */ 
/* 1070 */     if (paramString.startsWith("warn."))
/* 1071 */       str = "javac.err." + paramString.substring(5);
/*      */     else {
/* 1073 */       str = "javac.err." + paramString;
/*      */     }
/* 1075 */     return Main.getText(str, paramObject1 != null ? paramObject1
/* 1076 */       .toString() : null, paramObject2 != null ? paramObject2
/* 1077 */       .toString() : null, paramObject3 != null ? paramObject3
/* 1078 */       .toString() : null);
/*      */   }
/*      */ 
/*      */   protected boolean insertError(long paramLong, String paramString)
/*      */   {
/*      */     Object localObject;
/* 1107 */     if ((this.errors == null) || (this.errors.where > paramLong))
/*      */     {
/* 1111 */       localObject = new ErrorMessage(paramLong, paramString);
/* 1112 */       ((ErrorMessage)localObject).next = this.errors;
/* 1113 */       this.errors = ((ErrorMessage)localObject);
/*      */     } else {
/* 1115 */       if ((this.errors.where == paramLong) && 
/* 1116 */         (this.errors.message
/* 1116 */         .equals(paramString)))
/*      */       {
/* 1119 */         return false;
/*      */       }
/*      */ 
/* 1124 */       localObject = this.errors;
/*      */       ErrorMessage localErrorMessage1;
/* 1127 */       while (((localErrorMessage1 = ((ErrorMessage)localObject).next) != null) && (localErrorMessage1.where < paramLong))
/*      */       {
/* 1129 */         localObject = localErrorMessage1;
/*      */       }
/*      */ 
/* 1135 */       while (((localErrorMessage1 = ((ErrorMessage)localObject).next) != null) && (localErrorMessage1.where == paramLong))
/*      */       {
/* 1137 */         if (localErrorMessage1.message.equals(paramString))
/*      */         {
/* 1140 */           return false;
/*      */         }
/* 1142 */         localObject = localErrorMessage1;
/*      */       }
/*      */ 
/* 1146 */       ErrorMessage localErrorMessage2 = new ErrorMessage(paramLong, paramString);
/* 1147 */       localErrorMessage2.next = ((ErrorMessage)localObject).next;
/* 1148 */       ((ErrorMessage)localObject).next = localErrorMessage2;
/*      */     }
/*      */ 
/* 1152 */     return true;
/*      */   }
/*      */ 
/*      */   public void pushError(String paramString1, int paramInt, String paramString2, String paramString3, String paramString4)
/*      */   {
/* 1170 */     int i = this.errorLimit + this.nwarnings;
/* 1171 */     if ((++this.errorsPushed >= i) && (this.errorLimit >= 0)) {
/* 1172 */       if (!this.hitErrorLimit) {
/* 1173 */         this.hitErrorLimit = true;
/* 1174 */         output(errorString("too.many.errors", new Integer(this.errorLimit), null, null));
/*      */       }
/*      */ 
/* 1177 */       return;
/*      */     }
/* 1179 */     if (paramString1.endsWith(".java")) {
/* 1180 */       output(paramString1 + ":" + paramInt + ": " + paramString2);
/* 1181 */       output(paramString3);
/* 1182 */       output(paramString4);
/*      */     }
/*      */     else
/*      */     {
/* 1187 */       output(paramString1 + ": " + paramString2);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void flushErrors() {
/* 1192 */     if (this.errors == null) {
/* 1193 */       return;
/*      */     }
/*      */ 
/* 1196 */     int i = 0;
/*      */ 
/* 1198 */     char[] arrayOfChar1 = null;
/* 1199 */     int j = 0;
/*      */     try
/*      */     {
/* 1204 */       FileInputStream localFileInputStream = new FileInputStream(this.errorFileName);
/* 1205 */       arrayOfChar1 = new char[localFileInputStream.available()];
/*      */ 
/* 1208 */       InputStreamReader localInputStreamReader = getCharacterEncoding() != null ? new InputStreamReader(localFileInputStream, 
/* 1208 */         getCharacterEncoding()) : new InputStreamReader(localFileInputStream);
/*      */ 
/* 1210 */       j = localInputStreamReader.read(arrayOfChar1);
/* 1211 */       localInputStreamReader.close();
/* 1212 */       i = 1;
/*      */     }
/*      */     catch (IOException localIOException)
/*      */     {
/*      */     }
/*      */ 
/* 1218 */     for (ErrorMessage localErrorMessage = this.errors; localErrorMessage != null; localErrorMessage = localErrorMessage.next)
/*      */     {
/* 1224 */       int k = (int)(localErrorMessage.where >>> 32);
/* 1225 */       int m = (int)(localErrorMessage.where & 0xFFFFFFFF);
/* 1226 */       if (m > j) m = j;
/*      */ 
/* 1228 */       String str1 = "";
/* 1229 */       String str2 = "";
/* 1230 */       if (i != 0)
/*      */       {
/* 1232 */         for (int n = m; (n > 0) && (arrayOfChar1[(n - 1)] != '\n') && (arrayOfChar1[(n - 1)] != '\r'); n--);
/* 1233 */         for (int i1 = m; (i1 < j) && (arrayOfChar1[i1] != '\n') && (arrayOfChar1[i1] != '\r'); i1++);
/* 1234 */         str1 = new String(arrayOfChar1, n, i1 - n);
/*      */ 
/* 1236 */         char[] arrayOfChar2 = new char[m - n + 1];
/* 1237 */         for (i1 = n; i1 < m; i1++) {
/* 1238 */           arrayOfChar2[(i1 - n)] = (arrayOfChar1[i1] == '\t' ? 9 : ' ');
/*      */         }
/* 1240 */         arrayOfChar2[(m - n)] = '^';
/* 1241 */         str2 = new String(arrayOfChar2);
/*      */       }
/*      */ 
/* 1244 */       this.errorConsumer.pushError(this.errorFileName, k, localErrorMessage.message, str1, str2);
/*      */     }
/*      */ 
/* 1247 */     this.errors = null;
/*      */   }
/*      */ 
/*      */   public void reportError(Object paramObject, long paramLong, String paramString1, String paramString2)
/*      */   {
/* 1255 */     if (paramObject == null) {
/* 1256 */       if (this.errorFileName != null) {
/* 1257 */         flushErrors();
/* 1258 */         this.errorFileName = null;
/*      */       }
/* 1260 */       if (paramString1.startsWith("warn.")) {
/* 1261 */         if (warnings()) {
/* 1262 */           this.nwarnings += 1;
/* 1263 */           output(paramString2);
/*      */         }
/* 1265 */         return;
/*      */       }
/* 1267 */       output("error: " + paramString2);
/* 1268 */       this.nerrors += 1;
/* 1269 */       this.flags |= 65536;
/*      */     }
/* 1271 */     else if ((paramObject instanceof String)) {
/* 1272 */       String str = (String)paramObject;
/*      */ 
/* 1275 */       if (!str.equals(this.errorFileName)) {
/* 1276 */         flushErrors();
/* 1277 */         this.errorFileName = str;
/*      */       }
/*      */ 
/* 1282 */       if (paramString1.startsWith("warn.")) {
/* 1283 */         if (paramString1.indexOf("is.deprecated") >= 0)
/*      */         {
/* 1286 */           if (!this.deprecationFiles.contains(paramObject)) {
/* 1287 */             this.deprecationFiles.addElement(paramObject);
/*      */           }
/*      */ 
/* 1293 */           if (deprecation()) {
/* 1294 */             if (insertError(paramLong, paramString2))
/* 1295 */               this.ndeprecations += 1;
/*      */           }
/*      */           else {
/* 1298 */             this.ndeprecations += 1;
/*      */           }
/*      */ 
/*      */         }
/* 1304 */         else if (warnings()) {
/* 1305 */           if (insertError(paramLong, paramString2))
/* 1306 */             this.nwarnings += 1;
/*      */         }
/*      */         else {
/* 1309 */           this.nwarnings += 1;
/*      */         }
/*      */ 
/*      */       }
/* 1315 */       else if (insertError(paramLong, paramString2)) {
/* 1316 */         this.nerrors += 1;
/* 1317 */         this.flags |= 65536;
/*      */       }
/*      */     }
/* 1320 */     else if ((paramObject instanceof ClassFile)) {
/* 1321 */       reportError(((ClassFile)paramObject).getPath(), paramLong, paramString1, paramString2);
/*      */     }
/* 1323 */     else if ((paramObject instanceof Identifier)) {
/* 1324 */       reportError(paramObject.toString(), paramLong, paramString1, paramString2);
/*      */     }
/* 1326 */     else if ((paramObject instanceof ClassDeclaration)) {
/*      */       try {
/* 1328 */         reportError(((ClassDeclaration)paramObject).getClassDefinition(this), paramLong, paramString1, paramString2);
/*      */       } catch (ClassNotFound localClassNotFound) {
/* 1330 */         reportError(((ClassDeclaration)paramObject).getName(), paramLong, paramString1, paramString2);
/*      */       }
/* 1332 */     } else if ((paramObject instanceof ClassDefinition)) {
/* 1333 */       ClassDefinition localClassDefinition = (ClassDefinition)paramObject;
/* 1334 */       if (!paramString1.startsWith("warn.")) {
/* 1335 */         localClassDefinition.setError();
/*      */       }
/* 1337 */       reportError(localClassDefinition.getSource(), paramLong, paramString1, paramString2);
/*      */     }
/* 1339 */     else if ((paramObject instanceof MemberDefinition)) {
/* 1340 */       reportError(((MemberDefinition)paramObject).getClassDeclaration(), paramLong, paramString1, paramString2);
/*      */     }
/*      */     else {
/* 1343 */       output(paramObject + ":error=" + paramString1 + ":" + paramString2);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void error(Object paramObject1, long paramLong, String paramString, Object paramObject2, Object paramObject3, Object paramObject4)
/*      */   {
/* 1351 */     if (this.errorsPushed >= this.errorLimit + this.nwarnings)
/*      */     {
/* 1353 */       return;
/*      */     }
/* 1355 */     if (System.getProperty("javac.dump.stack") != null) {
/* 1356 */       output("javac.err." + paramString + ": " + errorString(paramString, paramObject2, paramObject3, paramObject4));
/* 1357 */       new Exception("Stack trace").printStackTrace(new PrintStream(this.out));
/*      */     }
/* 1359 */     reportError(paramObject1, paramLong, paramString, errorString(paramString, paramObject2, paramObject3, paramObject4));
/*      */   }
/*      */ 
/*      */   public void output(String paramString)
/*      */   {
/* 1367 */     PrintStream localPrintStream = (this.out instanceof PrintStream) ? (PrintStream)this.out : new PrintStream(this.out, true);
/*      */ 
/* 1370 */     localPrintStream.println(paramString);
/*      */   }
/*      */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     sun.tools.javac.BatchEnvironment
 * JD-Core Version:    0.6.2
 */