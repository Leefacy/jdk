/*      */ package com.sun.tools.corba.se.idl.toJavaPortable;
/*      */ 
/*      */ import com.sun.tools.corba.se.idl.ConstEntry;
/*      */ import com.sun.tools.corba.se.idl.EnumEntry;
/*      */ import com.sun.tools.corba.se.idl.ExceptionEntry;
/*      */ import com.sun.tools.corba.se.idl.GenFileStream;
/*      */ import com.sun.tools.corba.se.idl.InterfaceEntry;
/*      */ import com.sun.tools.corba.se.idl.InterfaceState;
/*      */ import com.sun.tools.corba.se.idl.MethodEntry;
/*      */ import com.sun.tools.corba.se.idl.NativeEntry;
/*      */ import com.sun.tools.corba.se.idl.ParameterEntry;
/*      */ import com.sun.tools.corba.se.idl.PrimitiveEntry;
/*      */ import com.sun.tools.corba.se.idl.SequenceEntry;
/*      */ import com.sun.tools.corba.se.idl.StringEntry;
/*      */ import com.sun.tools.corba.se.idl.StructEntry;
/*      */ import com.sun.tools.corba.se.idl.SymtabEntry;
/*      */ import com.sun.tools.corba.se.idl.TypedefEntry;
/*      */ import com.sun.tools.corba.se.idl.UnionBranch;
/*      */ import com.sun.tools.corba.se.idl.UnionEntry;
/*      */ import com.sun.tools.corba.se.idl.ValueBoxEntry;
/*      */ import com.sun.tools.corba.se.idl.ValueEntry;
/*      */ import com.sun.tools.corba.se.idl.constExpr.BinaryExpr;
/*      */ import com.sun.tools.corba.se.idl.constExpr.Divide;
/*      */ import com.sun.tools.corba.se.idl.constExpr.Expression;
/*      */ import com.sun.tools.corba.se.idl.constExpr.Minus;
/*      */ import com.sun.tools.corba.se.idl.constExpr.Not;
/*      */ import com.sun.tools.corba.se.idl.constExpr.Plus;
/*      */ import com.sun.tools.corba.se.idl.constExpr.Terminal;
/*      */ import com.sun.tools.corba.se.idl.constExpr.Times;
/*      */ import com.sun.tools.corba.se.idl.constExpr.UnaryExpr;
/*      */ import java.io.File;
/*      */ import java.io.PrintStream;
/*      */ import java.io.PrintWriter;
/*      */ import java.math.BigInteger;
/*      */ import java.text.DateFormat;
/*      */ import java.util.Date;
/*      */ import java.util.Enumeration;
/*      */ import java.util.Hashtable;
/*      */ import java.util.Locale;
/*      */ import java.util.StringTokenizer;
/*      */ import java.util.TimeZone;
/*      */ import java.util.Vector;
/*      */ 
/*      */ public class Util extends com.sun.tools.corba.se.idl.Util
/*      */ {
/*      */   public static final short TypeFile = 0;
/*      */   public static final short StubFile = 1;
/*      */   public static final short HelperFile = 2;
/*      */   public static final short HolderFile = 3;
/*      */   public static final short StateFile = 4;
/* 1427 */   static Hashtable symbolTable = new Hashtable();
/* 1428 */   static Hashtable packageTranslation = new Hashtable();
/*      */ 
/*      */   public static String getVersion()
/*      */   {
/*  114 */     return com.sun.tools.corba.se.idl.Util.getVersion("com/sun/tools/corba/se/idl/toJavaPortable/toJavaPortable.prp");
/*      */   }
/*      */ 
/*      */   static void setSymbolTable(Hashtable paramHashtable)
/*      */   {
/*  123 */     symbolTable = paramHashtable;
/*      */   }
/*      */ 
/*      */   public static void setPackageTranslation(Hashtable paramHashtable)
/*      */   {
/*  128 */     packageTranslation = paramHashtable;
/*      */   }
/*      */ 
/*      */   public static boolean isInterface(String paramString)
/*      */   {
/*  133 */     return isInterface(paramString, symbolTable);
/*      */   }
/*      */ 
/*      */   static String arrayInfo(Vector paramVector)
/*      */   {
/*  138 */     int i = paramVector.size();
/*  139 */     String str = "";
/*  140 */     Enumeration localEnumeration = paramVector.elements();
/*  141 */     while (localEnumeration.hasMoreElements())
/*  142 */       str = str + '[' + parseExpression((Expression)localEnumeration.nextElement()) + ']';
/*  143 */     return str;
/*      */   }
/*      */ 
/*      */   public static String sansArrayInfo(Vector paramVector)
/*      */   {
/*  149 */     int i = paramVector.size();
/*  150 */     String str = "";
/*  151 */     for (int j = 0; j < i; j++)
/*  152 */       str = str + "[]";
/*  153 */     return str;
/*      */   }
/*      */ 
/*      */   public static String sansArrayInfo(String paramString)
/*      */   {
/*  159 */     int i = paramString.indexOf('[');
/*  160 */     if (i >= 0)
/*      */     {
/*  162 */       String str = paramString.substring(i);
/*  163 */       paramString = paramString.substring(0, i);
/*  164 */       while (!str.equals(""))
/*      */       {
/*  166 */         paramString = paramString + "[]";
/*  167 */         str = str.substring(str.indexOf(']') + 1);
/*      */       }
/*      */     }
/*  170 */     return paramString;
/*      */   }
/*      */ 
/*      */   public static String fileName(SymtabEntry paramSymtabEntry, String paramString)
/*      */   {
/*  179 */     NameModifierImpl localNameModifierImpl = new NameModifierImpl();
/*  180 */     return fileName(paramSymtabEntry, localNameModifierImpl, paramString);
/*      */   }
/*      */ 
/*      */   public static String fileName(SymtabEntry paramSymtabEntry, NameModifier paramNameModifier, String paramString)
/*      */   {
/*  187 */     String str1 = containerFullName(paramSymtabEntry.container());
/*  188 */     if ((str1 != null) && (!str1.equals(""))) {
/*  189 */       mkdir(str1);
/*      */     }
/*  191 */     String str2 = paramSymtabEntry.name();
/*  192 */     str2 = paramNameModifier.makeName(str2) + paramString;
/*  193 */     if ((str1 != null) && (!str1.equals(""))) {
/*  194 */       str2 = str1 + '/' + str2;
/*      */     }
/*  196 */     return str2.replace('/', File.separatorChar);
/*      */   }
/*      */ 
/*      */   public static GenFileStream stream(SymtabEntry paramSymtabEntry, String paramString)
/*      */   {
/*  201 */     NameModifierImpl localNameModifierImpl = new NameModifierImpl();
/*  202 */     return stream(paramSymtabEntry, localNameModifierImpl, paramString);
/*      */   }
/*      */ 
/*      */   public static GenFileStream stream(SymtabEntry paramSymtabEntry, NameModifier paramNameModifier, String paramString)
/*      */   {
/*  207 */     return getStream(fileName(paramSymtabEntry, paramNameModifier, paramString), paramSymtabEntry);
/*      */   }
/*      */ 
/*      */   public static GenFileStream getStream(String paramString, SymtabEntry paramSymtabEntry)
/*      */   {
/*  213 */     String str = ((Arguments)Compile.compiler.arguments).targetDir + paramString;
/*  214 */     if ((Compile.compiler.arguments.keepOldFiles) && (new File(str).exists())) {
/*  215 */       return null;
/*      */     }
/*      */ 
/*  218 */     return new GenFileStream(str);
/*      */   }
/*      */ 
/*      */   public static String containerFullName(SymtabEntry paramSymtabEntry)
/*      */   {
/*  223 */     String str = doContainerFullName(paramSymtabEntry);
/*  224 */     if (packageTranslation.size() > 0)
/*  225 */       str = translate(str);
/*  226 */     return str;
/*      */   }
/*      */ 
/*      */   public static String translate(String paramString)
/*      */   {
/*  231 */     String str1 = paramString;
/*  232 */     String str2 = "";
/*      */     int i;
/*      */     do
/*      */     {
/*  239 */       String str3 = (String)packageTranslation.get(str1);
/*  240 */       if (str3 != null) {
/*  241 */         return str3 + str2;
/*      */       }
/*  243 */       i = str1.lastIndexOf('/');
/*  244 */       if (i >= 0) {
/*  245 */         str2 = str1.substring(i) + str2;
/*  246 */         str1 = str1.substring(0, i);
/*      */       }
/*      */     }
/*  248 */     while (i >= 0);
/*      */ 
/*  250 */     return paramString;
/*      */   }
/*      */ 
/*      */   private static String doContainerFullName(SymtabEntry paramSymtabEntry)
/*      */   {
/*  255 */     String str = "";
/*      */ 
/*  257 */     if (paramSymtabEntry == null) {
/*  258 */       str = "";
/*      */     }
/*      */     else {
/*  261 */       if (((paramSymtabEntry instanceof InterfaceEntry)) || ((paramSymtabEntry instanceof StructEntry)) || ((paramSymtabEntry instanceof UnionEntry)))
/*      */       {
/*  264 */         str = paramSymtabEntry.name() + "Package";
/*      */       }
/*  266 */       else str = paramSymtabEntry.name();
/*      */ 
/*  268 */       if ((paramSymtabEntry.container() != null) && 
/*  269 */         (!paramSymtabEntry
/*  269 */         .container().name().equals(""))) {
/*  270 */         str = doContainerFullName(paramSymtabEntry.container()) + '/' + str;
/*      */       }
/*      */     }
/*  273 */     return str;
/*      */   }
/*      */ 
/*      */   public static String javaName(SymtabEntry paramSymtabEntry)
/*      */   {
/*  285 */     String str = "";
/*  286 */     if (((paramSymtabEntry instanceof TypedefEntry)) || ((paramSymtabEntry instanceof SequenceEntry)))
/*      */     {
/*      */       try {
/*  289 */         str = sansArrayInfo((String)paramSymtabEntry.dynamicVariable(Compile.typedefInfo));
/*      */       }
/*      */       catch (NoSuchFieldException localNoSuchFieldException)
/*      */       {
/*  293 */         str = paramSymtabEntry.name();
/*      */       }
/*  295 */     } else if ((paramSymtabEntry instanceof PrimitiveEntry)) {
/*  296 */       str = javaPrimName(paramSymtabEntry.name());
/*  297 */     } else if ((paramSymtabEntry instanceof StringEntry)) {
/*  298 */       str = "String";
/*  299 */     } else if ((paramSymtabEntry instanceof NativeEntry)) {
/*  300 */       str = javaNativeName(paramSymtabEntry.name());
/*  301 */     } else if (((paramSymtabEntry instanceof ValueEntry)) && (paramSymtabEntry.name().equals("ValueBase"))) {
/*  302 */       str = "java.io.Serializable";
/*  303 */     } else if ((paramSymtabEntry instanceof ValueBoxEntry))
/*      */     {
/*  305 */       ValueBoxEntry localValueBoxEntry = (ValueBoxEntry)paramSymtabEntry;
/*  306 */       TypedefEntry localTypedefEntry = ((InterfaceState)localValueBoxEntry.state().elementAt(0)).entry;
/*  307 */       SymtabEntry localSymtabEntry = localTypedefEntry.type();
/*  308 */       if ((localSymtabEntry instanceof PrimitiveEntry))
/*      */       {
/*  310 */         str = containerFullName(paramSymtabEntry.container());
/*  311 */         if (!str.equals(""))
/*  312 */           str = str + '.';
/*  313 */         str = str + paramSymtabEntry.name();
/*      */       }
/*      */       else {
/*  316 */         str = javaName(localSymtabEntry);
/*      */       }
/*      */     }
/*      */     else {
/*  320 */       str = containerFullName(paramSymtabEntry.container());
/*  321 */       if (str.equals(""))
/*  322 */         str = paramSymtabEntry.name();
/*      */       else {
/*  324 */         str = str + '.' + paramSymtabEntry.name();
/*      */       }
/*      */     }
/*      */ 
/*  328 */     return str.replace('/', '.');
/*      */   }
/*      */ 
/*      */   public static String javaPrimName(String paramString)
/*      */   {
/*  333 */     if ((paramString.equals("long")) || (paramString.equals("unsigned long")))
/*  334 */       paramString = "int";
/*  335 */     else if (paramString.equals("octet")) {
/*  336 */       paramString = "byte";
/*      */     }
/*  338 */     else if ((paramString.equals("long long")) || (paramString.equals("unsigned long long")))
/*  339 */       paramString = "long";
/*  340 */     else if (paramString.equals("wchar"))
/*  341 */       paramString = "char";
/*  342 */     else if (paramString.equals("unsigned short"))
/*  343 */       paramString = "short";
/*  344 */     else if (paramString.equals("any"))
/*  345 */       paramString = "org.omg.CORBA.Any";
/*  346 */     else if (paramString.equals("TypeCode"))
/*  347 */       paramString = "org.omg.CORBA.TypeCode";
/*  348 */     else if (paramString.equals("Principal"))
/*  349 */       paramString = "org.omg.CORBA.Principal";
/*  350 */     return paramString;
/*      */   }
/*      */ 
/*      */   public static String javaNativeName(String paramString)
/*      */   {
/*  358 */     if ((paramString.equals("AbstractBase")) || (paramString.equals("Cookie")))
/*  359 */       paramString = "java.lang.Object";
/*  360 */     else if (paramString.equals("Servant"))
/*  361 */       paramString = "org.omg.PortableServer.Servant";
/*  362 */     else if (paramString.equals("ValueFactory"))
/*  363 */       paramString = "org.omg.CORBA.portable.ValueFactory";
/*  364 */     return paramString;
/*      */   }
/*      */ 
/*      */   public static String javaQualifiedName(SymtabEntry paramSymtabEntry)
/*      */   {
/*  374 */     String str = "";
/*  375 */     if ((paramSymtabEntry instanceof PrimitiveEntry)) {
/*  376 */       str = javaPrimName(paramSymtabEntry.name());
/*  377 */     } else if ((paramSymtabEntry instanceof StringEntry)) {
/*  378 */       str = "String";
/*  379 */     } else if (((paramSymtabEntry instanceof ValueEntry)) && (paramSymtabEntry.name().equals("ValueBase"))) {
/*  380 */       str = "java.io.Serializable";
/*      */     }
/*      */     else {
/*  383 */       SymtabEntry localSymtabEntry = paramSymtabEntry.container();
/*  384 */       if (localSymtabEntry != null)
/*  385 */         str = localSymtabEntry.name();
/*  386 */       if (str.equals(""))
/*  387 */         str = paramSymtabEntry.name();
/*      */       else
/*  389 */         str = containerFullName(paramSymtabEntry.container()) + '.' + paramSymtabEntry.name();
/*      */     }
/*  391 */     return str.replace('/', '.');
/*      */   }
/*      */ 
/*      */   public static String collapseName(String paramString)
/*      */   {
/*  402 */     if (paramString.equals("unsigned short"))
/*  403 */       paramString = "ushort";
/*  404 */     else if (paramString.equals("unsigned long"))
/*  405 */       paramString = "ulong";
/*  406 */     else if (paramString.equals("unsigned long long"))
/*  407 */       paramString = "ulonglong";
/*  408 */     else if (paramString.equals("long long"))
/*  409 */       paramString = "longlong";
/*  410 */     return paramString;
/*      */   }
/*      */ 
/*      */   public static SymtabEntry typeOf(SymtabEntry paramSymtabEntry)
/*      */   {
/*  418 */     while (((paramSymtabEntry instanceof TypedefEntry)) && (((TypedefEntry)paramSymtabEntry).arrayInfo().isEmpty()) && (!(paramSymtabEntry.type() instanceof SequenceEntry)))
/*  419 */       paramSymtabEntry = paramSymtabEntry.type();
/*  420 */     return paramSymtabEntry;
/*      */   }
/*      */ 
/*      */   static void fillInfo(SymtabEntry paramSymtabEntry)
/*      */   {
/*  428 */     String str = "";
/*  429 */     SymtabEntry localSymtabEntry = paramSymtabEntry;
/*  430 */     int i = 0;
/*      */     do
/*      */     {
/*      */       try
/*      */       {
/*  436 */         i = localSymtabEntry.dynamicVariable(Compile.typedefInfo) != null ? 1 : 0;
/*      */       }
/*      */       catch (NoSuchFieldException localNoSuchFieldException1)
/*      */       {
/*      */       }
/*      */ 
/*  442 */       if (i == 0)
/*      */       {
/*  444 */         if ((localSymtabEntry instanceof TypedefEntry)) {
/*  445 */           str = str + arrayInfo(((TypedefEntry)localSymtabEntry).arrayInfo());
/*  446 */         } else if ((localSymtabEntry instanceof SequenceEntry))
/*      */         {
/*  448 */           Expression localExpression = ((SequenceEntry)localSymtabEntry).maxSize();
/*  449 */           if (localExpression == null)
/*  450 */             str = str + "[]";
/*      */           else
/*  452 */             str = str + '[' + parseExpression(localExpression) + ']';
/*      */         }
/*  454 */         if (localSymtabEntry.type() != null)
/*      */         {
/*  462 */           localSymtabEntry = localSymtabEntry.type();
/*      */         }
/*      */       }
/*      */     }
/*  464 */     while ((i == 0) && (localSymtabEntry != null) && (((localSymtabEntry instanceof TypedefEntry)) || ((localSymtabEntry instanceof SequenceEntry))));
/*      */ 
/*  469 */     if ((localSymtabEntry instanceof ValueBoxEntry))
/*  470 */       fillValueBoxInfo((ValueBoxEntry)localSymtabEntry);
/*      */     try
/*      */     {
/*  473 */       if (i != 0)
/*  474 */         paramSymtabEntry.dynamicVariable(Compile.typedefInfo, (String)localSymtabEntry.dynamicVariable(Compile.typedefInfo) + str);
/*      */       else
/*  476 */         paramSymtabEntry.dynamicVariable(Compile.typedefInfo, javaName(localSymtabEntry) + str);
/*      */     }
/*      */     catch (NoSuchFieldException localNoSuchFieldException2)
/*      */     {
/*      */     }
/*      */   }
/*      */ 
/*      */   static void fillValueBoxInfo(ValueBoxEntry paramValueBoxEntry)
/*      */   {
/*  488 */     TypedefEntry localTypedefEntry = ((InterfaceState)paramValueBoxEntry.state().elementAt(0)).entry;
/*  489 */     if (localTypedefEntry.type() != null)
/*  490 */       fillInfo(localTypedefEntry.type());
/*  491 */     fillInfo(localTypedefEntry);
/*      */   }
/*      */ 
/*      */   public static String holderName(SymtabEntry paramSymtabEntry)
/*      */   {
/*      */     String str;
/*  500 */     if ((paramSymtabEntry instanceof PrimitiveEntry)) {
/*  501 */       if (paramSymtabEntry.name().equals("any"))
/*  502 */         str = "org.omg.CORBA.AnyHolder";
/*  503 */       else if (paramSymtabEntry.name().equals("TypeCode"))
/*  504 */         str = "org.omg.CORBA.TypeCodeHolder";
/*  505 */       else if (paramSymtabEntry.name().equals("Principal"))
/*  506 */         str = "org.omg.CORBA.PrincipalHolder";
/*      */       else
/*  508 */         str = "org.omg.CORBA." + capitalize(javaQualifiedName(paramSymtabEntry)) + "Holder";
/*  509 */     } else if ((paramSymtabEntry instanceof TypedefEntry))
/*      */     {
/*  511 */       TypedefEntry localTypedefEntry = (TypedefEntry)paramSymtabEntry;
/*  512 */       if ((!localTypedefEntry.arrayInfo().isEmpty()) || ((localTypedefEntry.type() instanceof SequenceEntry)))
/*  513 */         str = javaQualifiedName(paramSymtabEntry) + "Holder";
/*      */       else
/*  515 */         str = holderName(paramSymtabEntry.type());
/*      */     }
/*  517 */     else if ((paramSymtabEntry instanceof StringEntry)) {
/*  518 */       str = "org.omg.CORBA.StringHolder";
/*  519 */     } else if ((paramSymtabEntry instanceof ValueEntry))
/*      */     {
/*  521 */       if (paramSymtabEntry.name().equals("ValueBase"))
/*  522 */         str = "org.omg.CORBA.ValueBaseHolder";
/*      */       else
/*  524 */         str = javaName(paramSymtabEntry) + "Holder";
/*  525 */     } else if ((paramSymtabEntry instanceof NativeEntry))
/*      */     {
/*  529 */       str = javaQualifiedName(paramSymtabEntry) + "Holder";
/*      */     }
/*      */     else {
/*  532 */       str = javaName(paramSymtabEntry) + "Holder";
/*  533 */     }return str;
/*      */   }
/*      */ 
/*      */   public static String helperName(SymtabEntry paramSymtabEntry, boolean paramBoolean)
/*      */   {
/*  541 */     if (((paramSymtabEntry instanceof ValueEntry)) && 
/*  542 */       (paramSymtabEntry.name().equals("ValueBase"))) {
/*  543 */       return "org.omg.CORBA.ValueBaseHelper";
/*      */     }
/*  545 */     if (paramBoolean) {
/*  546 */       return javaQualifiedName(paramSymtabEntry) + "Helper";
/*      */     }
/*  548 */     return javaName(paramSymtabEntry) + "Helper";
/*      */   }
/*      */ 
/*      */   public static void writePackage(PrintWriter paramPrintWriter, SymtabEntry paramSymtabEntry)
/*      */   {
/*  563 */     writePackage(paramPrintWriter, paramSymtabEntry, (short)0);
/*      */   }
/*      */ 
/*      */   public static void writePackage(PrintWriter paramPrintWriter, SymtabEntry paramSymtabEntry, String paramString, short paramShort)
/*      */   {
/*  571 */     if ((paramString != null) && (!paramString.equals("")))
/*      */     {
/*  573 */       paramPrintWriter.println("package " + paramString.replace('/', '.') + ';');
/*      */ 
/*  578 */       if (!Compile.compiler.importTypes.isEmpty())
/*      */       {
/*  580 */         paramPrintWriter.println();
/*  581 */         Vector localVector = addImportLines(paramSymtabEntry, Compile.compiler.importTypes, paramShort);
/*  582 */         printImports(localVector, paramPrintWriter);
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   public static void writePackage(PrintWriter paramPrintWriter, SymtabEntry paramSymtabEntry, short paramShort)
/*      */   {
/*  592 */     String str = containerFullName(paramSymtabEntry.container());
/*  593 */     if ((str != null) && (!str.equals("")))
/*      */     {
/*  595 */       paramPrintWriter.println("package " + str.replace('/', '.') + ';');
/*      */ 
/*  599 */       if (((paramShort != 3) || ((paramSymtabEntry instanceof TypedefEntry))) && (!Compile.compiler.importTypes.isEmpty()))
/*      */       {
/*  601 */         paramPrintWriter.println();
/*  602 */         Vector localVector = addImportLines(paramSymtabEntry, Compile.compiler.importTypes, paramShort);
/*  603 */         printImports(localVector, paramPrintWriter);
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   private static void printImports(Vector paramVector, PrintWriter paramPrintWriter)
/*      */   {
/*  632 */     Enumeration localEnumeration = paramVector.elements();
/*  633 */     while (localEnumeration.hasMoreElements())
/*  634 */       paramPrintWriter.println("import " + (String)localEnumeration.nextElement() + ';');
/*      */   }
/*      */ 
/*      */   private static void addTo(Vector paramVector, String paramString)
/*      */   {
/*  643 */     if ((paramString.startsWith("ValueBase")) && (
/*  644 */       (paramString.compareTo("ValueBase") == 0) || 
/*  645 */       (paramString
/*  645 */       .compareTo("ValueBaseHolder") == 0) || 
/*  646 */       (paramString
/*  646 */       .compareTo("ValueBaseHelper") == 0)))
/*      */     {
/*  647 */       return;
/*  648 */     }if (!paramVector.contains(paramString))
/*  649 */       paramVector.addElement(paramString);
/*      */   }
/*      */ 
/*      */   private static Vector addImportLines(SymtabEntry paramSymtabEntry, Vector paramVector, short paramShort)
/*      */   {
/*  657 */     Vector localVector = new Vector();
/*      */     Object localObject2;
/*      */     Object localObject4;
/*      */     Object localObject6;
/*  658 */     if ((paramSymtabEntry instanceof ConstEntry))
/*      */     {
/*  660 */       localObject1 = (ConstEntry)paramSymtabEntry;
/*  661 */       localObject2 = ((ConstEntry)localObject1).value().value();
/*  662 */       if (((localObject2 instanceof ConstEntry)) && (paramVector.contains(localObject2)))
/*  663 */         addTo(localVector, ((ConstEntry)localObject2).name());
/*      */     }
/*  665 */     else if (((paramSymtabEntry instanceof ValueEntry)) && (paramShort == 2))
/*      */     {
/*  669 */       if (((ValueEntry)paramSymtabEntry).derivedFrom().size() > 0)
/*      */       {
/*  671 */         localObject1 = (ValueEntry)((ValueEntry)paramSymtabEntry).derivedFrom().elementAt(0);
/*  672 */         localObject2 = ((ValueEntry)localObject1).name();
/*  673 */         if ((!"ValueBase".equals(localObject2)) && 
/*  674 */           (paramVector.contains(localObject1)))
/*  675 */           addTo(localVector, (String)localObject2 + "Helper");
/*      */       }
/*      */     }
/*      */     else
/*      */     {
/*      */       Object localObject3;
/*      */       Object localObject5;
/*      */       Object localObject7;
/*  678 */       if (((paramSymtabEntry instanceof InterfaceEntry)) && ((paramShort == 0) || (paramShort == 1)))
/*      */       {
/*  680 */         localObject1 = (InterfaceEntry)paramSymtabEntry;
/*      */ 
/*  682 */         if ((localObject1 instanceof ValueEntry))
/*      */         {
/*  685 */           localObject2 = ((ValueEntry)localObject1).supports().elements();
/*  686 */           while (((Enumeration)localObject2).hasMoreElements())
/*      */           {
/*  688 */             localObject3 = (SymtabEntry)((Enumeration)localObject2).nextElement();
/*  689 */             if (paramVector.contains(localObject3))
/*      */             {
/*  691 */               addTo(localVector, ((SymtabEntry)localObject3).name() + "Operations");
/*      */             }
/*      */ 
/*  694 */             if (paramShort == 1)
/*      */             {
/*  696 */               if (paramVector.contains(localObject3))
/*  697 */                 addTo(localVector, ((SymtabEntry)localObject3).name());
/*  698 */               localObject5 = addImportLines((SymtabEntry)localObject3, paramVector, (short)1);
/*  699 */               localObject7 = ((Vector)localObject5).elements();
/*  700 */               while (((Enumeration)localObject7).hasMoreElements())
/*      */               {
/*  702 */                 addTo(localVector, (String)((Enumeration)localObject7).nextElement());
/*      */               }
/*      */             }
/*      */           }
/*      */ 
/*      */         }
/*      */ 
/*  709 */         localObject2 = ((InterfaceEntry)localObject1).derivedFrom().elements();
/*  710 */         while (((Enumeration)localObject2).hasMoreElements())
/*      */         {
/*  712 */           localObject3 = (SymtabEntry)((Enumeration)localObject2).nextElement();
/*  713 */           if (paramVector.contains(localObject3))
/*      */           {
/*  715 */             addTo(localVector, ((SymtabEntry)localObject3).name());
/*      */ 
/*  718 */             if (!(localObject3 instanceof ValueEntry)) {
/*  719 */               addTo(localVector, ((SymtabEntry)localObject3).name() + "Operations");
/*      */             }
/*      */           }
/*  722 */           if (paramShort == 1)
/*      */           {
/*  724 */             localObject5 = addImportLines((SymtabEntry)localObject3, paramVector, (short)1);
/*  725 */             localObject7 = ((Vector)localObject5).elements();
/*  726 */             while (((Enumeration)localObject7).hasMoreElements())
/*      */             {
/*  728 */               addTo(localVector, (String)((Enumeration)localObject7).nextElement());
/*      */             }
/*      */           }
/*      */         }
/*      */ 
/*  733 */         localObject2 = ((InterfaceEntry)localObject1).methods().elements();
/*  734 */         while (((Enumeration)localObject2).hasMoreElements())
/*      */         {
/*  736 */           localObject3 = (MethodEntry)((Enumeration)localObject2).nextElement();
/*      */ 
/*  739 */           localObject5 = typeOf(((MethodEntry)localObject3).type());
/*  740 */           if ((localObject5 != null) && (paramVector.contains(localObject5)) && (
/*  741 */             (paramShort == 0) || (paramShort == 1)))
/*      */           {
/*  743 */             addTo(localVector, ((SymtabEntry)localObject5).name());
/*  744 */             addTo(localVector, ((SymtabEntry)localObject5).name() + "Holder");
/*  745 */             if (paramShort == 1)
/*  746 */               addTo(localVector, ((SymtabEntry)localObject5).name() + "Helper");
/*      */           }
/*  748 */           checkForArrays((SymtabEntry)localObject5, paramVector, localVector);
/*      */ 
/*  751 */           if (paramShort == 1) {
/*  752 */             checkForBounds((SymtabEntry)localObject5, paramVector, localVector);
/*      */           }
/*      */ 
/*  755 */           localObject7 = ((MethodEntry)localObject3).exceptions().elements();
/*  756 */           while (((Enumeration)localObject7).hasMoreElements())
/*      */           {
/*  758 */             localObject8 = (ExceptionEntry)((Enumeration)localObject7).nextElement();
/*  759 */             if (paramVector.contains(localObject8))
/*      */             {
/*  761 */               addTo(localVector, ((ExceptionEntry)localObject8).name());
/*  762 */               addTo(localVector, ((ExceptionEntry)localObject8).name() + "Helper");
/*      */             }
/*      */ 
/*      */           }
/*      */ 
/*  767 */           Object localObject8 = ((MethodEntry)localObject3).parameters().elements();
/*  768 */           while (((Enumeration)localObject8).hasMoreElements())
/*      */           {
/*  770 */             ParameterEntry localParameterEntry = (ParameterEntry)((Enumeration)localObject8).nextElement();
/*  771 */             SymtabEntry localSymtabEntry = typeOf(localParameterEntry.type());
/*  772 */             if (paramVector.contains(localSymtabEntry))
/*      */             {
/*  775 */               if (paramShort == 1)
/*  776 */                 addTo(localVector, localSymtabEntry.name() + "Helper");
/*  777 */               if (localParameterEntry.passType() == 0)
/*  778 */                 addTo(localVector, localSymtabEntry.name());
/*      */               else
/*  780 */                 addTo(localVector, localSymtabEntry.name() + "Holder");
/*      */             }
/*  782 */             checkForArrays(localSymtabEntry, paramVector, localVector);
/*      */ 
/*  784 */             if (paramShort == 1)
/*  785 */               checkForBounds(localSymtabEntry, paramVector, localVector);
/*      */           }
/*      */         }
/*      */       }
/*  789 */       else if ((paramSymtabEntry instanceof StructEntry))
/*      */       {
/*  791 */         localObject1 = (StructEntry)paramSymtabEntry;
/*      */ 
/*  794 */         localObject2 = ((StructEntry)localObject1).members().elements();
/*  795 */         while (((Enumeration)localObject2).hasMoreElements())
/*      */         {
/*  797 */           localObject3 = (TypedefEntry)((Enumeration)localObject2).nextElement();
/*      */ 
/*  800 */           localObject5 = ((SymtabEntry)localObject3).type();
/*  801 */           localObject3 = typeOf((SymtabEntry)localObject3);
/*  802 */           if (paramVector.contains(localObject3))
/*      */           {
/*  807 */             if ((!(localObject3 instanceof TypedefEntry)) && (!(localObject3 instanceof ValueBoxEntry))) {
/*  808 */               addTo(localVector, ((SymtabEntry)localObject3).name());
/*      */             }
/*      */ 
/*  812 */             if (paramShort == 2)
/*      */             {
/*  814 */               addTo(localVector, ((SymtabEntry)localObject3).name() + "Helper");
/*  815 */               if ((localObject5 instanceof TypedefEntry))
/*  816 */                 addTo(localVector, ((SymtabEntry)localObject5).name() + "Helper");
/*      */             }
/*      */           }
/*  819 */           checkForArrays((SymtabEntry)localObject3, paramVector, localVector);
/*  820 */           checkForBounds((SymtabEntry)localObject3, paramVector, localVector);
/*      */         }
/*      */       }
/*  823 */       else if ((paramSymtabEntry instanceof TypedefEntry))
/*      */       {
/*  825 */         localObject1 = (TypedefEntry)paramSymtabEntry;
/*  826 */         localObject2 = checkForArrayBase((TypedefEntry)localObject1, paramVector, localVector);
/*  827 */         if (paramShort == 2)
/*      */         {
/*  829 */           checkForArrayDimensions((String)localObject2, paramVector, localVector);
/*      */           try
/*      */           {
/*  832 */             localObject3 = (String)((TypedefEntry)localObject1).dynamicVariable(Compile.typedefInfo);
/*  833 */             int i = ((String)localObject3).indexOf('[');
/*  834 */             if (i >= 0) {
/*  835 */               localObject3 = ((String)localObject3).substring(0, i);
/*      */             }
/*  837 */             localObject7 = (SymtabEntry)symbolTable.get(localObject3);
/*  838 */             if ((localObject7 != null) && (paramVector.contains(localObject7))) {
/*  839 */               addTo(localVector, ((SymtabEntry)localObject7).name() + "Helper");
/*      */             }
/*      */           }
/*      */           catch (NoSuchFieldException localNoSuchFieldException)
/*      */           {
/*      */           }
/*      */ 
/*  846 */           checkForBounds(typeOf((SymtabEntry)localObject1), paramVector, localVector);
/*      */         }
/*  848 */         localObject4 = addImportLines(((TypedefEntry)localObject1).type(), paramVector, paramShort);
/*  849 */         localObject6 = ((Vector)localObject4).elements();
/*  850 */         while (((Enumeration)localObject6).hasMoreElements())
/*  851 */           addTo(localVector, (String)((Enumeration)localObject6).nextElement());
/*      */       }
/*  853 */       else if ((paramSymtabEntry instanceof UnionEntry))
/*      */       {
/*  855 */         localObject1 = (UnionEntry)paramSymtabEntry;
/*      */ 
/*  858 */         localObject2 = typeOf(((UnionEntry)localObject1).type());
/*  859 */         if (((localObject2 instanceof EnumEntry)) && (paramVector.contains(localObject2))) {
/*  860 */           addTo(localVector, ((SymtabEntry)localObject2).name());
/*      */         }
/*      */ 
/*  863 */         localObject4 = ((UnionEntry)localObject1).branches().elements();
/*  864 */         while (((Enumeration)localObject4).hasMoreElements())
/*      */         {
/*  866 */           localObject6 = (UnionBranch)((Enumeration)localObject4).nextElement();
/*  867 */           localObject7 = typeOf(((UnionBranch)localObject6).typedef);
/*  868 */           if (paramVector.contains(localObject7))
/*      */           {
/*  870 */             addTo(localVector, ((SymtabEntry)localObject7).name());
/*  871 */             if (paramShort == 2)
/*  872 */               addTo(localVector, ((SymtabEntry)localObject7).name() + "Helper");
/*      */           }
/*  874 */           checkForArrays((SymtabEntry)localObject7, paramVector, localVector);
/*      */ 
/*  876 */           checkForBounds((SymtabEntry)localObject7, paramVector, localVector);
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/*  883 */     Object localObject1 = localVector.elements();
/*  884 */     while (((Enumeration)localObject1).hasMoreElements())
/*      */     {
/*  886 */       localObject2 = (String)((Enumeration)localObject1).nextElement();
/*  887 */       localObject4 = (SymtabEntry)symbolTable.get(localObject2);
/*  888 */       if ((localObject4 != null) && ((localObject4 instanceof TypedefEntry)))
/*      */       {
/*  890 */         localObject6 = (TypedefEntry)localObject4;
/*  891 */         if ((((TypedefEntry)localObject6).arrayInfo().size() == 0) || (!(((TypedefEntry)localObject6).type() instanceof SequenceEntry)))
/*  892 */           localVector.removeElement(localObject2);
/*      */       }
/*      */     }
/*  895 */     return localVector;
/*      */   }
/*      */ 
/*      */   private static void checkForArrays(SymtabEntry paramSymtabEntry, Vector paramVector1, Vector paramVector2)
/*      */   {
/*  903 */     if ((paramSymtabEntry instanceof TypedefEntry))
/*      */     {
/*  905 */       TypedefEntry localTypedefEntry = (TypedefEntry)paramSymtabEntry;
/*  906 */       String str = checkForArrayBase(localTypedefEntry, paramVector1, paramVector2);
/*  907 */       checkForArrayDimensions(str, paramVector1, paramVector2);
/*      */     }
/*      */   }
/*      */ 
/*      */   private static String checkForArrayBase(TypedefEntry paramTypedefEntry, Vector paramVector1, Vector paramVector2)
/*      */   {
/*  916 */     String str1 = "";
/*      */     try
/*      */     {
/*  919 */       String str2 = (String)paramTypedefEntry.dynamicVariable(Compile.typedefInfo);
/*  920 */       int i = str2.indexOf('[');
/*  921 */       if (i >= 0)
/*      */       {
/*  923 */         str1 = str2.substring(i);
/*  924 */         str2 = str2.substring(0, i);
/*      */       }
/*      */ 
/*  928 */       SymtabEntry localSymtabEntry = (SymtabEntry)symbolTable.get(str2);
/*  929 */       if ((localSymtabEntry != null) && (paramVector1.contains(localSymtabEntry)))
/*  930 */         addTo(paramVector2, localSymtabEntry.name());
/*      */     }
/*      */     catch (NoSuchFieldException localNoSuchFieldException) {
/*      */     }
/*  934 */     return str1;
/*      */   }
/*      */ 
/*      */   private static void checkForArrayDimensions(String paramString, Vector paramVector1, Vector paramVector2)
/*      */   {
/*  944 */     while (!paramString.equals(""))
/*      */     {
/*  946 */       int i = paramString.indexOf(']');
/*  947 */       String str = paramString.substring(1, i);
/*  948 */       paramString = paramString.substring(i + 1);
/*  949 */       SymtabEntry localSymtabEntry = (SymtabEntry)symbolTable.get(str);
/*  950 */       if (localSymtabEntry == null)
/*      */       {
/*  954 */         int j = str.lastIndexOf('.');
/*  955 */         if (j >= 0)
/*  956 */           localSymtabEntry = (SymtabEntry)symbolTable.get(str.substring(0, j));
/*      */       }
/*  958 */       if ((localSymtabEntry != null) && (paramVector1.contains(localSymtabEntry)))
/*  959 */         addTo(paramVector2, localSymtabEntry.name());
/*      */     }
/*      */   }
/*      */ 
/*      */   private static void checkForBounds(SymtabEntry paramSymtabEntry, Vector paramVector1, Vector paramVector2)
/*      */   {
/*  973 */     SymtabEntry localSymtabEntry = paramSymtabEntry;
/*  974 */     while ((localSymtabEntry instanceof TypedefEntry)) {
/*  975 */       localSymtabEntry = localSymtabEntry.type();
/*      */     }
/*  977 */     if (((localSymtabEntry instanceof StringEntry)) && (((StringEntry)localSymtabEntry).maxSize() != null)) {
/*  978 */       checkForGlobalConstants(((StringEntry)localSymtabEntry).maxSize().rep(), paramVector1, paramVector2);
/*      */     }
/*  980 */     else if (((localSymtabEntry instanceof SequenceEntry)) && (((SequenceEntry)localSymtabEntry).maxSize() != null))
/*  981 */       checkForGlobalConstants(((SequenceEntry)localSymtabEntry).maxSize().rep(), paramVector1, paramVector2);
/*      */   }
/*      */ 
/*      */   private static void checkForGlobalConstants(String paramString, Vector paramVector1, Vector paramVector2)
/*      */   {
/*  993 */     StringTokenizer localStringTokenizer = new StringTokenizer(paramString, " +-*()~&|^%<>");
/*  994 */     while (localStringTokenizer.hasMoreTokens())
/*      */     {
/*  996 */       String str = localStringTokenizer.nextToken();
/*      */ 
/* 1000 */       if (!str.equals("/"))
/*      */       {
/* 1002 */         SymtabEntry localSymtabEntry1 = (SymtabEntry)symbolTable.get(str);
/* 1003 */         if ((localSymtabEntry1 instanceof ConstEntry))
/*      */         {
/* 1005 */           int i = str.indexOf('/');
/* 1006 */           if (i < 0)
/*      */           {
/* 1008 */             if (paramVector1.contains(localSymtabEntry1))
/* 1009 */               addTo(paramVector2, localSymtabEntry1.name());
/*      */           }
/*      */           else
/*      */           {
/* 1013 */             SymtabEntry localSymtabEntry2 = (SymtabEntry)symbolTable.get(str.substring(0, i));
/* 1014 */             if (((localSymtabEntry2 instanceof InterfaceEntry)) && (paramVector1.contains(localSymtabEntry2)))
/* 1015 */               addTo(paramVector2, localSymtabEntry2.name());
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   public static void writeInitializer(String paramString1, String paramString2, String paramString3, SymtabEntry paramSymtabEntry, PrintWriter paramPrintWriter)
/*      */   {
/* 1027 */     if ((paramSymtabEntry instanceof TypedefEntry))
/*      */     {
/* 1029 */       TypedefEntry localTypedefEntry = (TypedefEntry)paramSymtabEntry;
/* 1030 */       writeInitializer(paramString1, paramString2, paramString3 + sansArrayInfo(localTypedefEntry.arrayInfo()), localTypedefEntry.type(), paramPrintWriter);
/*      */     }
/* 1032 */     else if ((paramSymtabEntry instanceof SequenceEntry)) {
/* 1033 */       writeInitializer(paramString1, paramString2, paramString3 + "[]", paramSymtabEntry.type(), paramPrintWriter);
/* 1034 */     } else if ((paramSymtabEntry instanceof EnumEntry)) {
/* 1035 */       if (paramString3.length() > 0)
/* 1036 */         paramPrintWriter.println(paramString1 + javaName(paramSymtabEntry) + ' ' + paramString2 + paramString3 + " = null;");
/*      */       else
/* 1038 */         paramPrintWriter.println(paramString1 + javaName(paramSymtabEntry) + ' ' + paramString2 + " = null;");
/* 1039 */     } else if ((paramSymtabEntry instanceof PrimitiveEntry))
/*      */     {
/* 1041 */       int i = paramString3.length() > 0 ? 1 : 0;
/* 1042 */       String str = javaPrimName(paramSymtabEntry.name());
/* 1043 */       if (str.equals("boolean"))
/* 1044 */         paramPrintWriter.println(paramString1 + "boolean " + paramString2 + paramString3 + " = " + (i != 0 ? "null;" : "false;"));
/* 1045 */       else if (str.equals("org.omg.CORBA.TypeCode"))
/* 1046 */         paramPrintWriter.println(paramString1 + "org.omg.CORBA.TypeCode " + paramString2 + paramString3 + " = null;");
/* 1047 */       else if (str.equals("org.omg.CORBA.Any"))
/* 1048 */         paramPrintWriter.println(paramString1 + "org.omg.CORBA.Any " + paramString2 + paramString3 + " = null;");
/* 1049 */       else if (str.equals("org.omg.CORBA.Principal"))
/* 1050 */         paramPrintWriter.println(paramString1 + "org.omg.CORBA.Principal " + paramString2 + paramString3 + " = null;");
/*      */       else {
/* 1052 */         paramPrintWriter.println(paramString1 + str + ' ' + paramString2 + paramString3 + " = " + (i != 0 ? "null;" : new StringBuilder().append('(').append(str).append(")0;").toString()));
/*      */       }
/*      */ 
/*      */     }
/*      */     else
/*      */     {
/* 1059 */       paramPrintWriter.println(paramString1 + javaName(paramSymtabEntry) + ' ' + paramString2 + paramString3 + " = null;");
/*      */     }
/*      */   }
/*      */ 
/*      */   public static void writeInitializer(String paramString1, String paramString2, String paramString3, SymtabEntry paramSymtabEntry, String paramString4, PrintWriter paramPrintWriter)
/*      */   {
/* 1067 */     if ((paramSymtabEntry instanceof TypedefEntry))
/*      */     {
/* 1069 */       TypedefEntry localTypedefEntry = (TypedefEntry)paramSymtabEntry;
/* 1070 */       writeInitializer(paramString1, paramString2, paramString3 + sansArrayInfo(localTypedefEntry.arrayInfo()), localTypedefEntry.type(), paramString4, paramPrintWriter);
/*      */     }
/* 1072 */     else if ((paramSymtabEntry instanceof SequenceEntry)) {
/* 1073 */       writeInitializer(paramString1, paramString2, paramString3 + "[]", paramSymtabEntry.type(), paramString4, paramPrintWriter);
/* 1074 */     } else if ((paramSymtabEntry instanceof EnumEntry)) {
/* 1075 */       if (paramString3.length() > 0)
/* 1076 */         paramPrintWriter.println(paramString1 + javaName(paramSymtabEntry) + ' ' + paramString2 + paramString3 + " = " + paramString4 + ';');
/*      */       else
/* 1078 */         paramPrintWriter.println(paramString1 + javaName(paramSymtabEntry) + ' ' + paramString2 + " = " + paramString4 + ';');
/* 1079 */     } else if ((paramSymtabEntry instanceof PrimitiveEntry))
/*      */     {
/* 1081 */       int i = paramString3.length() > 0 ? 1 : 0;
/* 1082 */       String str = javaPrimName(paramSymtabEntry.name());
/* 1083 */       if (str.equals("boolean"))
/* 1084 */         paramPrintWriter.println(paramString1 + "boolean " + paramString2 + paramString3 + " = " + paramString4 + ';');
/* 1085 */       else if (str.equals("org.omg.CORBA.TypeCode"))
/* 1086 */         paramPrintWriter.println(paramString1 + "org.omg.CORBA.TypeCode " + paramString2 + paramString3 + " = " + paramString4 + ';');
/* 1087 */       else if (str.equals("org.omg.CORBA.Any"))
/* 1088 */         paramPrintWriter.println(paramString1 + "org.omg.CORBA.Any " + paramString2 + paramString3 + " = " + paramString4 + ';');
/* 1089 */       else if (str.equals("org.omg.CORBA.Principal"))
/* 1090 */         paramPrintWriter.println(paramString1 + "org.omg.CORBA.Principal " + paramString2 + paramString3 + " = " + paramString4 + ';');
/*      */       else {
/* 1092 */         paramPrintWriter.println(paramString1 + str + ' ' + paramString2 + paramString3 + " = " + paramString4 + ';');
/*      */       }
/*      */ 
/*      */     }
/*      */     else
/*      */     {
/* 1099 */       paramPrintWriter.println(paramString1 + javaName(paramSymtabEntry) + ' ' + paramString2 + paramString3 + " = " + paramString4 + ';');
/*      */     }
/*      */   }
/*      */ 
/*      */   public static void mkdir(String paramString)
/*      */   {
/* 1107 */     String str = ((Arguments)Compile.compiler.arguments).targetDir;
/* 1108 */     paramString = (str + paramString).replace('/', File.separatorChar);
/* 1109 */     File localFile = new File(paramString);
/* 1110 */     if ((!localFile.exists()) && 
/* 1111 */       (!localFile.mkdirs()))
/* 1112 */       System.err.println(getMessage("Util.cantCreatePkg", paramString));
/*      */   }
/*      */ 
/*      */   public static void writeProlog(PrintWriter paramPrintWriter, String paramString)
/*      */   {
/* 1121 */     String str = ((Arguments)Compile.compiler.arguments).targetDir;
/* 1122 */     if (str != null)
/* 1123 */       paramString = paramString.substring(str.length());
/* 1124 */     paramPrintWriter.println();
/* 1125 */     paramPrintWriter.println("/**");
/* 1126 */     paramPrintWriter.println("* " + paramString.replace(File.separatorChar, '/') + " .");
/*      */ 
/* 1128 */     paramPrintWriter.println("* " + getMessage("toJavaProlog1", 
/* 1129 */       getMessage("Version.product", 
/* 1129 */       getMessage("Version.number"))));
/*      */ 
/* 1132 */     paramPrintWriter.println("* " + getMessage("toJavaProlog2", Compile.compiler.arguments.file.replace(File.separatorChar, '/')));
/*      */ 
/* 1139 */     DateFormat localDateFormat = DateFormat.getDateTimeInstance(0, 0, Locale.getDefault());
/*      */ 
/* 1144 */     if (Locale.getDefault() == Locale.JAPAN)
/* 1145 */       localDateFormat.setTimeZone(TimeZone.getTimeZone("JST"));
/*      */     else {
/* 1147 */       localDateFormat.setTimeZone(TimeZone.getDefault());
/*      */     }
/* 1149 */     paramPrintWriter.println("* " + localDateFormat.format(new Date()));
/*      */ 
/* 1154 */     paramPrintWriter.println("*/");
/* 1155 */     paramPrintWriter.println();
/*      */   }
/*      */ 
/*      */   public static String stripLeadingUnderscores(String paramString)
/*      */   {
/* 1168 */     while (paramString.startsWith("_"))
/* 1169 */       paramString = paramString.substring(1);
/* 1170 */     return paramString;
/*      */   }
/*      */ 
/*      */   public static String stripLeadingUnderscoresFromID(String paramString)
/*      */   {
/* 1178 */     String str = "";
/* 1179 */     int i = paramString.indexOf(':');
/* 1180 */     if (i >= 0)
/*      */       do
/*      */       {
/* 1183 */         str = str + paramString.substring(0, i + 1);
/* 1184 */         paramString = paramString.substring(i + 1);
/* 1185 */         while (paramString.startsWith("_"))
/* 1186 */           paramString = paramString.substring(1);
/* 1187 */         i = paramString.indexOf('/');
/* 1188 */       }while (i >= 0);
/* 1189 */     return str + paramString;
/*      */   }
/*      */ 
/*      */   public static String parseExpression(Expression paramExpression)
/*      */   {
/* 1197 */     if ((paramExpression instanceof Terminal))
/* 1198 */       return parseTerminal((Terminal)paramExpression);
/* 1199 */     if ((paramExpression instanceof BinaryExpr))
/* 1200 */       return parseBinary((BinaryExpr)paramExpression);
/* 1201 */     if ((paramExpression instanceof UnaryExpr)) {
/* 1202 */       return parseUnary((UnaryExpr)paramExpression);
/*      */     }
/* 1204 */     return "(UNKNOWN_VALUE)";
/*      */   }
/*      */ 
/*      */   static String parseTerminal(Terminal paramTerminal)
/*      */   {
/*      */     Object localObject1;
/* 1213 */     if ((paramTerminal.value() instanceof ConstEntry))
/*      */     {
/* 1215 */       localObject1 = (ConstEntry)paramTerminal.value();
/* 1216 */       if ((((ConstEntry)localObject1).container() instanceof InterfaceEntry)) {
/* 1217 */         return javaQualifiedName(((ConstEntry)localObject1).container()) + '.' + ((ConstEntry)localObject1).name();
/*      */       }
/* 1219 */       return javaQualifiedName((SymtabEntry)localObject1) + ".value";
/*      */     }
/* 1221 */     if ((paramTerminal.value() instanceof Expression))
/* 1222 */       return '(' + parseExpression((Expression)paramTerminal.value()) + ')';
/* 1223 */     if ((paramTerminal.value() instanceof Character))
/*      */     {
/* 1225 */       if (((Character)paramTerminal.value()).charValue() == '\013')
/*      */       {
/* 1227 */         return "'\\013'";
/* 1228 */       }if (((Character)paramTerminal.value()).charValue() == '\007')
/*      */       {
/* 1230 */         return "'\\007'";
/* 1231 */       }if (paramTerminal.rep().startsWith("'\\x"))
/* 1232 */         return hexToOctal(paramTerminal.rep());
/* 1233 */       if (paramTerminal.rep().equals("'\\?'")) {
/* 1234 */         return "'?'";
/*      */       }
/* 1236 */       return paramTerminal.rep();
/*      */     }
/* 1238 */     if ((paramTerminal.value() instanceof Boolean)) {
/* 1239 */       return paramTerminal.value().toString();
/*      */     }
/*      */ 
/* 1258 */     if ((paramTerminal.value() instanceof BigInteger))
/*      */     {
/* 1266 */       localObject1 = (SymtabEntry)symbolTable.get(paramTerminal.type());
/*      */ 
/* 1269 */       while (((SymtabEntry)localObject1).type() != null) {
/* 1270 */         localObject1 = ((SymtabEntry)localObject1).type();
/*      */       }
/* 1272 */       String str = ((SymtabEntry)localObject1).name();
/*      */       Object localObject2;
/*      */       int i;
/* 1274 */       if ((str.equals("unsigned long long")) && 
/* 1275 */         (((BigInteger)paramTerminal
/* 1275 */         .value()).compareTo(Expression.llMax) > 0))
/*      */       {
/* 1278 */         localObject2 = (BigInteger)paramTerminal.value();
/* 1279 */         localObject2 = ((BigInteger)localObject2).subtract(Expression.twoPow64);
/* 1280 */         i = paramTerminal.rep().indexOf(')');
/* 1281 */         if (i < 0) {
/* 1282 */           return ((BigInteger)localObject2).toString() + 'L';
/*      */         }
/* 1284 */         return '(' + ((BigInteger)localObject2).toString() + 'L' + ')';
/*      */       }
/* 1286 */       if ((str.indexOf("long long") >= 0) || (str.equals("unsigned long")))
/*      */       {
/* 1288 */         localObject2 = paramTerminal.rep();
/* 1289 */         i = ((String)localObject2).indexOf(')');
/* 1290 */         if (i < 0) {
/* 1291 */           return (String)localObject2 + 'L';
/*      */         }
/* 1293 */         return ((String)localObject2).substring(0, i) + 'L' + ((String)localObject2).substring(i);
/*      */       }
/*      */ 
/* 1296 */       return paramTerminal.rep();
/*      */     }
/*      */ 
/* 1299 */     return paramTerminal.rep();
/*      */   }
/*      */ 
/*      */   static String hexToOctal(String paramString)
/*      */   {
/* 1309 */     paramString = paramString.substring(3, paramString.length() - 1);
/* 1310 */     return "'\\" + Integer.toString(Integer.parseInt(paramString, 16), 8) + "'";
/*      */   }
/*      */ 
/*      */   static String parseBinary(BinaryExpr paramBinaryExpr)
/*      */   {
/* 1318 */     String str = "";
/* 1319 */     if (((paramBinaryExpr.value() instanceof Float)) || ((paramBinaryExpr.value() instanceof Double)))
/*      */     {
/* 1321 */       str = "(double)";
/* 1322 */       if ((!(paramBinaryExpr instanceof Plus)) && (!(paramBinaryExpr instanceof Minus)) && (!(paramBinaryExpr instanceof Times)) && (!(paramBinaryExpr instanceof Divide)))
/*      */       {
/* 1324 */         System.err.println("Operator " + paramBinaryExpr.op() + " is invalid on floating point numbers");
/*      */       }
/* 1326 */     } else if ((paramBinaryExpr.value() instanceof Number))
/*      */     {
/* 1328 */       if (paramBinaryExpr.type().indexOf("long long") >= 0)
/* 1329 */         str = "(long)";
/*      */       else
/* 1331 */         str = "(int)";
/*      */     }
/*      */     else
/*      */     {
/* 1335 */       str = "";
/* 1336 */       System.err.println("Unknown type in constant expression");
/*      */     }
/*      */ 
/* 1345 */     if (paramBinaryExpr.type().equals("unsigned long long"))
/*      */     {
/* 1347 */       BigInteger localBigInteger = (BigInteger)paramBinaryExpr.value();
/* 1348 */       if (localBigInteger.compareTo(Expression.llMax) > 0)
/* 1349 */         localBigInteger = localBigInteger.subtract(Expression.twoPow64);
/* 1350 */       return str + '(' + localBigInteger.toString() + 'L' + ')';
/*      */     }
/*      */ 
/* 1353 */     return str + '(' + parseExpression(paramBinaryExpr.left()) + ' ' + paramBinaryExpr.op() + ' ' + parseExpression(paramBinaryExpr.right()) + ')';
/*      */   }
/*      */ 
/*      */   static String parseUnary(UnaryExpr paramUnaryExpr)
/*      */   {
/* 1362 */     if (!(paramUnaryExpr.value() instanceof Number))
/* 1363 */       return "(UNKNOWN_VALUE)";
/* 1364 */     if ((((paramUnaryExpr.value() instanceof Float)) || ((paramUnaryExpr.value() instanceof Double))) && ((paramUnaryExpr instanceof Not))) {
/* 1365 */       return "(UNKNOWN_VALUE)";
/*      */     }
/*      */ 
/* 1368 */     String str = "";
/* 1369 */     if (((paramUnaryExpr.operand().value() instanceof Float)) || 
/* 1370 */       ((paramUnaryExpr
/* 1370 */       .operand().value() instanceof Double))) {
/* 1371 */       str = "(double)";
/*      */     }
/* 1375 */     else if (paramUnaryExpr.type().indexOf("long long") >= 0)
/* 1376 */       str = "(long)";
/*      */     else {
/* 1378 */       str = "(int)";
/*      */     }
/*      */ 
/* 1386 */     if (paramUnaryExpr.type().equals("unsigned long long"))
/*      */     {
/* 1388 */       BigInteger localBigInteger = (BigInteger)paramUnaryExpr.value();
/* 1389 */       if (localBigInteger.compareTo(Expression.llMax) > 0)
/* 1390 */         localBigInteger = localBigInteger.subtract(Expression.twoPow64);
/* 1391 */       return str + '(' + localBigInteger.toString() + 'L' + ')';
/*      */     }
/*      */ 
/* 1394 */     return str + paramUnaryExpr.op() + parseExpression(paramUnaryExpr.operand());
/*      */   }
/*      */ 
/*      */   public static boolean IDLEntity(SymtabEntry paramSymtabEntry)
/*      */   {
/* 1404 */     boolean bool = true;
/* 1405 */     if (((paramSymtabEntry instanceof PrimitiveEntry)) || ((paramSymtabEntry instanceof StringEntry)))
/* 1406 */       bool = false;
/* 1407 */     else if ((paramSymtabEntry instanceof TypedefEntry))
/* 1408 */       bool = IDLEntity(paramSymtabEntry.type());
/* 1409 */     return bool;
/*      */   }
/*      */ 
/*      */   public static boolean corbaLevel(float paramFloat1, float paramFloat2)
/*      */   {
/* 1419 */     float f1 = Compile.compiler.arguments.corbaLevel;
/* 1420 */     float f2 = 0.001F;
/* 1421 */     if ((f1 - paramFloat1 + f2 >= 0.0F) && (paramFloat2 - f1 + f2 >= 0.0F)) {
/* 1422 */       return true;
/*      */     }
/* 1424 */     return false;
/*      */   }
/*      */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.corba.se.idl.toJavaPortable.Util
 * JD-Core Version:    0.6.2
 */