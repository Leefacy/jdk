/*      */ package sun.rmi.rmic.iiop;
/*      */ 
/*      */ import java.io.IOException;
/*      */ import java.text.DateFormat;
/*      */ import java.util.Date;
/*      */ import java.util.Enumeration;
/*      */ import java.util.HashSet;
/*      */ import java.util.Hashtable;
/*      */ import java.util.Locale;
/*      */ import java.util.StringTokenizer;
/*      */ import java.util.Vector;
/*      */ import sun.rmi.rmic.IndentingWriter;
/*      */ import sun.rmi.rmic.Main;
/*      */ import sun.tools.java.ClassDefinition;
/*      */ import sun.tools.java.CompilerError;
/*      */ import sun.tools.java.Identifier;
/*      */ 
/*      */ public class IDLGenerator extends Generator
/*      */ {
/*   57 */   private boolean valueMethods = true;
/*   58 */   private boolean factory = true;
/*   59 */   private Hashtable ifHash = new Hashtable();
/*   60 */   private Hashtable imHash = new Hashtable();
/*      */ 
/*   62 */   private boolean isThrown = true;
/*   63 */   private boolean isException = true;
/*   64 */   private boolean isForward = true;
/*   65 */   private boolean forValuetype = true;
/*      */ 
/*      */   protected boolean requireNewInstance()
/*      */   {
/*   81 */     return false;
/*      */   }
/*      */ 
/*      */   protected boolean parseNonConforming(ContextStack paramContextStack)
/*      */   {
/*   89 */     return this.valueMethods;
/*      */   }
/*      */ 
/*      */   protected CompoundType getTopType(ClassDefinition paramClassDefinition, ContextStack paramContextStack)
/*      */   {
/*  100 */     return CompoundType.forCompound(paramClassDefinition, paramContextStack);
/*      */   }
/*      */ 
/*      */   protected Identifier getOutputId(Generator.OutputType paramOutputType)
/*      */   {
/*  113 */     Identifier localIdentifier = super.getOutputId(paramOutputType);
/*      */ 
/*  115 */     Type localType = paramOutputType.getType();
/*  116 */     String str1 = paramOutputType.getName();
/*      */ 
/*  118 */     if (localIdentifier == idJavaLangClass) {
/*  119 */       if (localType.isArray()) {
/*  120 */         return Identifier.lookup("org.omg.boxedRMI.javax.rmi.CORBA." + str1);
/*      */       }
/*  122 */       return idClassDesc;
/*      */     }
/*  124 */     if ((localIdentifier == idJavaLangString) && 
/*  125 */       (localType
/*  125 */       .isArray())) {
/*  126 */       return Identifier.lookup("org.omg.boxedRMI.CORBA." + str1);
/*      */     }
/*  128 */     if (("org.omg.CORBA.Object".equals(localType.getQualifiedName())) && 
/*  129 */       (localType
/*  129 */       .isArray()))
/*  130 */       return Identifier.lookup("org.omg.boxedRMI." + str1);
/*      */     Object localObject1;
/*      */     Object localObject2;
/*  132 */     if (localType.isArray()) {
/*  133 */       localObject1 = (ArrayType)localType;
/*  134 */       localObject2 = ((ArrayType)localObject1).getElementType();
/*  135 */       if (((Type)localObject2).isCompound()) {
/*  136 */         CompoundType localCompoundType = (CompoundType)localObject2;
/*  137 */         String str2 = localCompoundType.getQualifiedName();
/*  138 */         if (localCompoundType.isIDLEntity())
/*  139 */           return Identifier.lookup(getQualifiedName((Type)localObject1));
/*      */       }
/*  141 */       return Identifier.lookup(idBoxedRMI, localIdentifier);
/*      */     }
/*      */ 
/*  144 */     if (localType.isCompound()) {
/*  145 */       localObject1 = (CompoundType)localType;
/*  146 */       localObject2 = ((CompoundType)localObject1).getQualifiedName();
/*  147 */       if (((CompoundType)localObject1).isBoxed()) {
/*  148 */         return Identifier.lookup(getQualifiedName((Type)localObject1));
/*      */       }
/*      */     }
/*  151 */     return localIdentifier;
/*      */   }
/*      */ 
/*      */   protected String getFileNameExtensionFor(Generator.OutputType paramOutputType)
/*      */   {
/*  164 */     return ".idl";
/*      */   }
/*      */ 
/*      */   public boolean parseArgs(String[] paramArrayOfString, Main paramMain)
/*      */   {
/*  176 */     boolean bool = super.parseArgs(paramArrayOfString, paramMain);
/*      */ 
/*  179 */     if (bool)
/*      */     {
/*  181 */       for (int i = 0; i < paramArrayOfString.length; i++) {
/*  182 */         if (paramArrayOfString[i] != null) {
/*  183 */           if (paramArrayOfString[i].equalsIgnoreCase("-idl")) {
/*  184 */             this.idl = true;
/*  185 */             paramArrayOfString[i] = null;
/*      */           }
/*  187 */           else if (paramArrayOfString[i].equalsIgnoreCase("-valueMethods")) {
/*  188 */             this.valueMethods = true;
/*  189 */             paramArrayOfString[i] = null;
/*      */           }
/*  191 */           else if (paramArrayOfString[i].equalsIgnoreCase("-noValueMethods")) {
/*  192 */             this.valueMethods = false;
/*  193 */             paramArrayOfString[i] = null;
/*      */           }
/*  195 */           else if (paramArrayOfString[i].equalsIgnoreCase("-init")) {
/*  196 */             this.factory = false;
/*  197 */             paramArrayOfString[i] = null;
/*      */           }
/*  199 */           else if (paramArrayOfString[i].equalsIgnoreCase("-factory")) {
/*  200 */             this.factory = true;
/*  201 */             paramArrayOfString[i] = null;
/*      */           }
/*      */           else
/*      */           {
/*      */             String str1;
/*      */             String str2;
/*  203 */             if (paramArrayOfString[i].equalsIgnoreCase("-idlfile")) {
/*  204 */               paramArrayOfString[i] = null;
/*  205 */               i++; if ((i < paramArrayOfString.length) && (paramArrayOfString[i] != null) && (!paramArrayOfString[i].startsWith("-"))) {
/*  206 */                 str1 = paramArrayOfString[i];
/*  207 */                 paramArrayOfString[i] = null;
/*  208 */                 i++; if ((i < paramArrayOfString.length) && (paramArrayOfString[i] != null) && (!paramArrayOfString[i].startsWith("-"))) {
/*  209 */                   str2 = paramArrayOfString[i];
/*  210 */                   paramArrayOfString[i] = null;
/*  211 */                   this.ifHash.put(str1, str2);
/*  212 */                   continue;
/*      */                 }
/*      */               }
/*  215 */               paramMain.error("rmic.option.requires.argument", "-idlfile");
/*  216 */               bool = false;
/*      */             }
/*  218 */             else if (paramArrayOfString[i].equalsIgnoreCase("-idlmodule")) {
/*  219 */               paramArrayOfString[i] = null;
/*  220 */               i++; if ((i < paramArrayOfString.length) && (paramArrayOfString[i] != null) && (!paramArrayOfString[i].startsWith("-"))) {
/*  221 */                 str1 = paramArrayOfString[i];
/*  222 */                 paramArrayOfString[i] = null;
/*  223 */                 i++; if ((i < paramArrayOfString.length) && (paramArrayOfString[i] != null) && (!paramArrayOfString[i].startsWith("-"))) {
/*  224 */                   str2 = paramArrayOfString[i];
/*  225 */                   paramArrayOfString[i] = null;
/*  226 */                   this.imHash.put(str1, str2);
/*  227 */                   continue;
/*      */                 }
/*      */               }
/*  230 */               paramMain.error("rmic.option.requires.argument", "-idlmodule");
/*  231 */               bool = false;
/*      */             }
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/*      */ 
/*  238 */     return bool;
/*      */   }
/*      */ 
/*      */   protected Generator.OutputType[] getOutputTypesFor(CompoundType paramCompoundType, HashSet paramHashSet)
/*      */   {
/*  254 */     Vector localVector1 = getAllReferencesFor(paramCompoundType);
/*  255 */     Vector localVector2 = new Vector();
/*  256 */     for (int i = 0; i < localVector1.size(); i++) {
/*  257 */       Type localType1 = (Type)localVector1.elementAt(i);
/*      */       Object localObject;
/*  258 */       if (localType1.isArray()) {
/*  259 */         localObject = (ArrayType)localType1;
/*  260 */         int j = ((ArrayType)localObject).getArrayDimension();
/*  261 */         Type localType2 = ((ArrayType)localObject).getElementType();
/*  262 */         String str1 = unEsc(localType2.getIDLName()).replace(' ', '_');
/*  263 */         for (int k = 0; k < j; k++) {
/*  264 */           String str2 = "seq" + (k + 1) + "_" + str1;
/*  265 */           localVector2.addElement(new Generator.OutputType(this, str2, (Type)localObject));
/*      */         }
/*      */       }
/*  268 */       else if (localType1.isCompound()) {
/*  269 */         localObject = unEsc(localType1.getIDLName());
/*  270 */         localVector2.addElement(new Generator.OutputType(this, ((String)localObject).replace(' ', '_'), localType1));
/*  271 */         if (localType1.isClass()) {
/*  272 */           ClassType localClassType = (ClassType)localType1;
/*  273 */           if (localClassType.isException()) {
/*  274 */             localObject = unEsc(localClassType.getIDLExceptionName());
/*  275 */             localVector2.addElement(new Generator.OutputType(this, ((String)localObject).replace(' ', '_'), localType1));
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/*  280 */     Generator.OutputType[] arrayOfOutputType = new Generator.OutputType[localVector2.size()];
/*  281 */     localVector2.copyInto(arrayOfOutputType);
/*  282 */     return arrayOfOutputType;
/*      */   }
/*      */ 
/*      */   protected Vector getAllReferencesFor(CompoundType paramCompoundType)
/*      */   {
/*  293 */     Hashtable localHashtable1 = new Hashtable();
/*  294 */     Hashtable localHashtable2 = new Hashtable();
/*  295 */     Hashtable localHashtable3 = new Hashtable();
/*      */ 
/*  297 */     localHashtable1.put(paramCompoundType.getQualifiedName(), paramCompoundType);
/*  298 */     accumulateReferences(localHashtable1, localHashtable2, localHashtable3);
/*      */     int i;
/*      */     do {
/*  300 */       i = localHashtable1.size();
/*  301 */       accumulateReferences(localHashtable1, localHashtable2, localHashtable3);
/*      */     }
/*  303 */     while (i < localHashtable1.size());
/*      */ 
/*  305 */     Vector localVector = new Vector();
/*  306 */     Enumeration localEnumeration1 = localHashtable1.elements();
/*      */     Object localObject;
/*  307 */     while (localEnumeration1.hasMoreElements()) {
/*  308 */       localObject = (CompoundType)localEnumeration1.nextElement();
/*  309 */       localVector.addElement(localObject);
/*      */     }
/*  311 */     localEnumeration1 = localHashtable2.elements();
/*  312 */     while (localEnumeration1.hasMoreElements()) {
/*  313 */       localObject = (CompoundType)localEnumeration1.nextElement();
/*  314 */       localVector.addElement(localObject);
/*      */     }
/*  316 */     localEnumeration1 = localHashtable3.elements();
/*      */ 
/*  318 */     while (localEnumeration1.hasMoreElements()) {
/*  319 */       localObject = (ArrayType)localEnumeration1.nextElement();
/*  320 */       int j = ((ArrayType)localObject).getArrayDimension();
/*  321 */       Type localType = ((ArrayType)localObject).getElementType();
/*  322 */       Enumeration localEnumeration2 = localHashtable3.elements();
/*      */       while (true) { if (!localEnumeration2.hasMoreElements()) break label249;
/*  324 */         ArrayType localArrayType = (ArrayType)localEnumeration2.nextElement();
/*  325 */         if ((localType == localArrayType.getElementType()) && 
/*  326 */           (j < localArrayType
/*  326 */           .getArrayDimension()))
/*      */           break;
/*      */       }
/*  329 */       label249: localVector.addElement(localObject);
/*      */     }
/*  331 */     return localVector;
/*      */   }
/*      */ 
/*      */   protected void accumulateReferences(Hashtable paramHashtable1, Hashtable paramHashtable2, Hashtable paramHashtable3)
/*      */   {
/*  347 */     Enumeration localEnumeration = paramHashtable1.elements();
/*      */     Object localObject1;
/*      */     Object localObject2;
/*  348 */     while (localEnumeration.hasMoreElements()) {
/*  349 */       localObject1 = (CompoundType)localEnumeration.nextElement();
/*  350 */       localObject2 = getData((CompoundType)localObject1);
/*  351 */       Vector localVector = getMethods((CompoundType)localObject1);
/*  352 */       getInterfaces((CompoundType)localObject1, paramHashtable1);
/*  353 */       getInheritance((CompoundType)localObject1, paramHashtable1);
/*  354 */       getMethodReferences(localVector, paramHashtable1, paramHashtable2, paramHashtable3, paramHashtable1);
/*  355 */       getMemberReferences((Vector)localObject2, paramHashtable1, paramHashtable2, paramHashtable3);
/*      */     }
/*  357 */     localEnumeration = paramHashtable3.elements();
/*  358 */     while (localEnumeration.hasMoreElements()) {
/*  359 */       localObject1 = (ArrayType)localEnumeration.nextElement();
/*  360 */       localObject2 = ((ArrayType)localObject1).getElementType();
/*  361 */       addReference((Type)localObject2, paramHashtable1, paramHashtable2, paramHashtable3);
/*      */     }
/*  363 */     localEnumeration = paramHashtable1.elements();
/*  364 */     while (localEnumeration.hasMoreElements()) {
/*  365 */       localObject1 = (CompoundType)localEnumeration.nextElement();
/*  366 */       if (!isIDLGeneratedFor((CompoundType)localObject1))
/*  367 */         paramHashtable1.remove(((CompoundType)localObject1).getQualifiedName());
/*      */     }
/*      */   }
/*      */ 
/*      */   protected boolean isIDLGeneratedFor(CompoundType paramCompoundType)
/*      */   {
/*  386 */     if (paramCompoundType.isCORBAObject()) return false;
/*  387 */     if (paramCompoundType.isIDLEntity()) {
/*  388 */       if (paramCompoundType.isBoxed()) return true;
/*      */ 
/*  390 */       if ("org.omg.CORBA.portable.IDLEntity"
/*  390 */         .equals(paramCompoundType
/*  390 */         .getQualifiedName())) return true;
/*  391 */       if (paramCompoundType.isCORBAUserException()) return true;
/*  392 */       return false;
/*  393 */     }Hashtable localHashtable = new Hashtable();
/*  394 */     getInterfaces(paramCompoundType, localHashtable);
/*  395 */     if (paramCompoundType.getTypeCode() == 65536) {
/*  396 */       if (localHashtable.size() < 2) return false;
/*  397 */       return true;
/*  398 */     }return true;
/*      */   }
/*      */ 
/*      */   protected void writeOutputFor(Generator.OutputType paramOutputType, HashSet paramHashSet, IndentingWriter paramIndentingWriter)
/*      */     throws IOException
/*      */   {
/*  416 */     Type localType = paramOutputType.getType();
/*  417 */     if (localType.isArray()) {
/*  418 */       writeSequence(paramOutputType, paramIndentingWriter);
/*  419 */       return;
/*      */     }
/*  421 */     if (isSpecialReference(localType)) {
/*  422 */       writeSpecial(localType, paramIndentingWriter);
/*      */       return;
/*      */     }
/*      */     Object localObject;
/*  425 */     if (localType.isCompound()) {
/*  426 */       localObject = (CompoundType)localType;
/*  427 */       if ((((CompoundType)localObject).isIDLEntity()) && (((CompoundType)localObject).isBoxed())) {
/*  428 */         writeBoxedIDL((CompoundType)localObject, paramIndentingWriter);
/*  429 */         return;
/*      */       }
/*      */     }
/*  432 */     if (localType.isClass()) {
/*  433 */       localObject = (ClassType)localType;
/*  434 */       if (((ClassType)localObject).isException()) {
/*  435 */         String str1 = unEsc(((ClassType)localObject).getIDLExceptionName());
/*  436 */         String str2 = paramOutputType.getName();
/*  437 */         if (str2.equals(str1.replace(' ', '_'))) {
/*  438 */           writeException((ClassType)localObject, paramIndentingWriter);
/*  439 */           return;
/*      */         }
/*      */       }
/*      */     }
/*  443 */     switch (localType.getTypeCode()) {
/*      */     case 65536:
/*  445 */       writeImplementation((ImplementationType)localType, paramIndentingWriter);
/*  446 */       break;
/*      */     case 16384:
/*      */     case 131072:
/*  449 */       writeNCType((CompoundType)localType, paramIndentingWriter);
/*  450 */       break;
/*      */     case 4096:
/*      */     case 8192:
/*  453 */       writeRemote((RemoteType)localType, paramIndentingWriter);
/*  454 */       break;
/*      */     case 32768:
/*  456 */       writeValue((ValueType)localType, paramIndentingWriter);
/*  457 */       break;
/*      */     default:
/*  461 */       throw new CompilerError("IDLGenerator got unexpected type code: " + localType
/*  461 */         .getTypeCode());
/*      */     }
/*      */   }
/*      */ 
/*      */   protected void writeImplementation(ImplementationType paramImplementationType, IndentingWriter paramIndentingWriter)
/*      */     throws IOException
/*      */   {
/*  475 */     Hashtable localHashtable1 = new Hashtable();
/*  476 */     Hashtable localHashtable2 = new Hashtable();
/*  477 */     getInterfaces(paramImplementationType, localHashtable1);
/*      */ 
/*  479 */     writeBanner(paramImplementationType, 0, !this.isException, paramIndentingWriter);
/*  480 */     writeInheritedIncludes(localHashtable1, paramIndentingWriter);
/*  481 */     writeIfndef(paramImplementationType, 0, !this.isException, !this.isForward, paramIndentingWriter);
/*  482 */     writeIncOrb(paramIndentingWriter);
/*  483 */     writeModule1(paramImplementationType, paramIndentingWriter);
/*  484 */     paramIndentingWriter.pln(); paramIndentingWriter.pI();
/*  485 */     paramIndentingWriter.p("interface " + paramImplementationType.getIDLName());
/*  486 */     writeInherits(localHashtable1, !this.forValuetype, paramIndentingWriter);
/*      */ 
/*  488 */     paramIndentingWriter.pln(" {");
/*  489 */     paramIndentingWriter.pln("};");
/*      */ 
/*  491 */     paramIndentingWriter.pO(); paramIndentingWriter.pln();
/*  492 */     writeModule2(paramImplementationType, paramIndentingWriter);
/*  493 */     writeEpilog(paramImplementationType, localHashtable2, paramIndentingWriter);
/*      */   }
/*      */ 
/*      */   protected void writeNCType(CompoundType paramCompoundType, IndentingWriter paramIndentingWriter)
/*      */     throws IOException
/*      */   {
/*  508 */     Vector localVector1 = getConstants(paramCompoundType);
/*  509 */     Vector localVector2 = getMethods(paramCompoundType);
/*  510 */     Hashtable localHashtable1 = new Hashtable();
/*  511 */     Hashtable localHashtable2 = new Hashtable();
/*  512 */     Hashtable localHashtable3 = new Hashtable();
/*  513 */     Hashtable localHashtable4 = new Hashtable();
/*  514 */     Hashtable localHashtable5 = new Hashtable();
/*  515 */     getInterfaces(paramCompoundType, localHashtable1);
/*  516 */     getInheritance(paramCompoundType, localHashtable1);
/*  517 */     getMethodReferences(localVector2, localHashtable2, localHashtable3, localHashtable4, localHashtable5);
/*      */ 
/*  519 */     writeProlog(paramCompoundType, localHashtable2, localHashtable3, localHashtable4, localHashtable5, localHashtable1, paramIndentingWriter);
/*  520 */     writeModule1(paramCompoundType, paramIndentingWriter);
/*  521 */     paramIndentingWriter.pln(); paramIndentingWriter.pI();
/*  522 */     paramIndentingWriter.p("abstract valuetype " + paramCompoundType.getIDLName());
/*  523 */     writeInherits(localHashtable1, !this.forValuetype, paramIndentingWriter);
/*      */ 
/*  525 */     paramIndentingWriter.pln(" {");
/*  526 */     if (localVector1.size() + localVector2.size() > 0) {
/*  527 */       paramIndentingWriter.pln(); paramIndentingWriter.pI();
/*  528 */       for (int i = 0; i < localVector1.size(); i++)
/*  529 */         writeConstant((CompoundType.Member)localVector1.elementAt(i), paramIndentingWriter);
/*  530 */       for (i = 0; i < localVector2.size(); i++)
/*  531 */         writeMethod((CompoundType.Method)localVector2.elementAt(i), paramIndentingWriter);
/*  532 */       paramIndentingWriter.pO(); paramIndentingWriter.pln();
/*      */     }
/*  534 */     paramIndentingWriter.pln("};");
/*      */ 
/*  536 */     paramIndentingWriter.pO(); paramIndentingWriter.pln();
/*  537 */     writeModule2(paramCompoundType, paramIndentingWriter);
/*  538 */     writeEpilog(paramCompoundType, localHashtable2, paramIndentingWriter);
/*      */   }
/*      */ 
/*      */   protected void writeRemote(RemoteType paramRemoteType, IndentingWriter paramIndentingWriter)
/*      */     throws IOException
/*      */   {
/*  554 */     Vector localVector1 = getConstants(paramRemoteType);
/*  555 */     Vector localVector2 = getMethods(paramRemoteType);
/*  556 */     Hashtable localHashtable1 = new Hashtable();
/*  557 */     Hashtable localHashtable2 = new Hashtable();
/*  558 */     Hashtable localHashtable3 = new Hashtable();
/*  559 */     Hashtable localHashtable4 = new Hashtable();
/*  560 */     Hashtable localHashtable5 = new Hashtable();
/*  561 */     getInterfaces(paramRemoteType, localHashtable1);
/*  562 */     getMethodReferences(localVector2, localHashtable2, localHashtable3, localHashtable4, localHashtable5);
/*      */ 
/*  564 */     writeProlog(paramRemoteType, localHashtable2, localHashtable3, localHashtable4, localHashtable5, localHashtable1, paramIndentingWriter);
/*  565 */     writeModule1(paramRemoteType, paramIndentingWriter);
/*  566 */     paramIndentingWriter.pln(); paramIndentingWriter.pI();
/*  567 */     if (paramRemoteType.getTypeCode() == 8192) paramIndentingWriter.p("abstract ");
/*  568 */     paramIndentingWriter.p("interface " + paramRemoteType.getIDLName());
/*  569 */     writeInherits(localHashtable1, !this.forValuetype, paramIndentingWriter);
/*      */ 
/*  571 */     paramIndentingWriter.pln(" {");
/*  572 */     if (localVector1.size() + localVector2.size() > 0) {
/*  573 */       paramIndentingWriter.pln(); paramIndentingWriter.pI();
/*  574 */       for (int i = 0; i < localVector1.size(); i++)
/*  575 */         writeConstant((CompoundType.Member)localVector1.elementAt(i), paramIndentingWriter);
/*  576 */       for (i = 0; i < localVector2.size(); i++)
/*  577 */         writeMethod((CompoundType.Method)localVector2.elementAt(i), paramIndentingWriter);
/*  578 */       paramIndentingWriter.pO(); paramIndentingWriter.pln();
/*      */     }
/*  580 */     paramIndentingWriter.pln("};");
/*      */ 
/*  582 */     paramIndentingWriter.pO(); paramIndentingWriter.pln();
/*  583 */     writeRepositoryID(paramRemoteType, paramIndentingWriter);
/*  584 */     paramIndentingWriter.pln();
/*  585 */     writeModule2(paramRemoteType, paramIndentingWriter);
/*  586 */     writeEpilog(paramRemoteType, localHashtable2, paramIndentingWriter);
/*      */   }
/*      */ 
/*      */   protected void writeValue(ValueType paramValueType, IndentingWriter paramIndentingWriter)
/*      */     throws IOException
/*      */   {
/*  600 */     Vector localVector1 = getData(paramValueType);
/*  601 */     Vector localVector2 = getConstants(paramValueType);
/*  602 */     Vector localVector3 = getMethods(paramValueType);
/*  603 */     Hashtable localHashtable1 = new Hashtable();
/*  604 */     Hashtable localHashtable2 = new Hashtable();
/*  605 */     Hashtable localHashtable3 = new Hashtable();
/*  606 */     Hashtable localHashtable4 = new Hashtable();
/*  607 */     Hashtable localHashtable5 = new Hashtable();
/*  608 */     getInterfaces(paramValueType, localHashtable1);
/*  609 */     getInheritance(paramValueType, localHashtable1);
/*  610 */     getMethodReferences(localVector3, localHashtable2, localHashtable3, localHashtable4, localHashtable5);
/*  611 */     getMemberReferences(localVector1, localHashtable2, localHashtable3, localHashtable4);
/*      */ 
/*  613 */     writeProlog(paramValueType, localHashtable2, localHashtable3, localHashtable4, localHashtable5, localHashtable1, paramIndentingWriter);
/*  614 */     writeModule1(paramValueType, paramIndentingWriter);
/*  615 */     paramIndentingWriter.pln(); paramIndentingWriter.pI();
/*  616 */     if (paramValueType.isCustom()) paramIndentingWriter.p("custom ");
/*  617 */     paramIndentingWriter.p("valuetype " + paramValueType.getIDLName());
/*  618 */     writeInherits(localHashtable1, this.forValuetype, paramIndentingWriter);
/*      */ 
/*  620 */     paramIndentingWriter.pln(" {");
/*  621 */     if (localVector2.size() + localVector1.size() + localVector3.size() > 0) {
/*  622 */       paramIndentingWriter.pln(); paramIndentingWriter.pI();
/*  623 */       for (int i = 0; i < localVector2.size(); i++)
/*  624 */         writeConstant((CompoundType.Member)localVector2.elementAt(i), paramIndentingWriter);
/*      */       CompoundType.Member localMember;
/*  625 */       for (i = 0; i < localVector1.size(); i++) {
/*  626 */         localMember = (CompoundType.Member)localVector1.elementAt(i);
/*  627 */         if (localMember.getType().isPrimitive())
/*  628 */           writeData(localMember, paramIndentingWriter);
/*      */       }
/*  630 */       for (i = 0; i < localVector1.size(); i++) {
/*  631 */         localMember = (CompoundType.Member)localVector1.elementAt(i);
/*  632 */         if (!localMember.getType().isPrimitive())
/*  633 */           writeData(localMember, paramIndentingWriter);
/*      */       }
/*  635 */       for (i = 0; i < localVector3.size(); i++)
/*  636 */         writeMethod((CompoundType.Method)localVector3.elementAt(i), paramIndentingWriter);
/*  637 */       paramIndentingWriter.pO(); paramIndentingWriter.pln();
/*      */     }
/*  639 */     paramIndentingWriter.pln("};");
/*      */ 
/*  641 */     paramIndentingWriter.pO(); paramIndentingWriter.pln();
/*  642 */     writeRepositoryID(paramValueType, paramIndentingWriter);
/*  643 */     paramIndentingWriter.pln();
/*  644 */     writeModule2(paramValueType, paramIndentingWriter);
/*  645 */     writeEpilog(paramValueType, localHashtable2, paramIndentingWriter);
/*      */   }
/*      */ 
/*      */   protected void writeProlog(CompoundType paramCompoundType, Hashtable paramHashtable1, Hashtable paramHashtable2, Hashtable paramHashtable3, Hashtable paramHashtable4, Hashtable paramHashtable5, IndentingWriter paramIndentingWriter)
/*      */     throws IOException
/*      */   {
/*  668 */     writeBanner(paramCompoundType, 0, !this.isException, paramIndentingWriter);
/*  669 */     writeForwardReferences(paramHashtable1, paramIndentingWriter);
/*  670 */     writeIncludes(paramHashtable4, this.isThrown, paramIndentingWriter);
/*  671 */     writeInheritedIncludes(paramHashtable5, paramIndentingWriter);
/*  672 */     writeIncludes(paramHashtable2, !this.isThrown, paramIndentingWriter);
/*  673 */     writeBoxedRMIIncludes(paramHashtable3, paramIndentingWriter);
/*  674 */     writeIDLEntityIncludes(paramHashtable1, paramIndentingWriter);
/*  675 */     writeIncOrb(paramIndentingWriter);
/*  676 */     writeIfndef(paramCompoundType, 0, !this.isException, !this.isForward, paramIndentingWriter);
/*      */   }
/*      */ 
/*      */   protected void writeEpilog(CompoundType paramCompoundType, Hashtable paramHashtable, IndentingWriter paramIndentingWriter)
/*      */     throws IOException
/*      */   {
/*  691 */     writeIncludes(paramHashtable, !this.isThrown, paramIndentingWriter);
/*  692 */     writeEndif(paramIndentingWriter);
/*      */   }
/*      */ 
/*      */   protected void writeSpecial(Type paramType, IndentingWriter paramIndentingWriter)
/*      */     throws IOException
/*      */   {
/*  706 */     String str = paramType.getQualifiedName();
/*  707 */     if ("java.io.Serializable".equals(str))
/*  708 */       writeJavaIoSerializable(paramType, paramIndentingWriter);
/*  709 */     else if ("java.io.Externalizable".equals(str))
/*  710 */       writeJavaIoExternalizable(paramType, paramIndentingWriter);
/*  711 */     else if ("java.lang.Object".equals(str))
/*  712 */       writeJavaLangObject(paramType, paramIndentingWriter);
/*  713 */     else if ("java.rmi.Remote".equals(str))
/*  714 */       writeJavaRmiRemote(paramType, paramIndentingWriter);
/*  715 */     else if ("org.omg.CORBA.portable.IDLEntity".equals(str))
/*  716 */       writeIDLEntity(paramType, paramIndentingWriter);
/*      */   }
/*      */ 
/*      */   protected void writeJavaIoSerializable(Type paramType, IndentingWriter paramIndentingWriter)
/*      */     throws IOException
/*      */   {
/*  731 */     writeBanner(paramType, 0, !this.isException, paramIndentingWriter);
/*  732 */     writeIfndef(paramType, 0, !this.isException, !this.isForward, paramIndentingWriter);
/*  733 */     writeModule1(paramType, paramIndentingWriter);
/*  734 */     paramIndentingWriter.pln(); paramIndentingWriter.pI();
/*  735 */     paramIndentingWriter.pln("typedef any Serializable;");
/*  736 */     paramIndentingWriter.pO(); paramIndentingWriter.pln();
/*  737 */     writeModule2(paramType, paramIndentingWriter);
/*  738 */     writeEndif(paramIndentingWriter);
/*      */   }
/*      */ 
/*      */   protected void writeJavaIoExternalizable(Type paramType, IndentingWriter paramIndentingWriter)
/*      */     throws IOException
/*      */   {
/*  752 */     writeBanner(paramType, 0, !this.isException, paramIndentingWriter);
/*  753 */     writeIfndef(paramType, 0, !this.isException, !this.isForward, paramIndentingWriter);
/*  754 */     writeModule1(paramType, paramIndentingWriter);
/*  755 */     paramIndentingWriter.pln(); paramIndentingWriter.pI();
/*  756 */     paramIndentingWriter.pln("typedef any Externalizable;");
/*  757 */     paramIndentingWriter.pO(); paramIndentingWriter.pln();
/*  758 */     writeModule2(paramType, paramIndentingWriter);
/*  759 */     writeEndif(paramIndentingWriter);
/*      */   }
/*      */ 
/*      */   protected void writeJavaLangObject(Type paramType, IndentingWriter paramIndentingWriter)
/*      */     throws IOException
/*      */   {
/*  773 */     writeBanner(paramType, 0, !this.isException, paramIndentingWriter);
/*  774 */     writeIfndef(paramType, 0, !this.isException, !this.isForward, paramIndentingWriter);
/*  775 */     writeModule1(paramType, paramIndentingWriter);
/*  776 */     paramIndentingWriter.pln(); paramIndentingWriter.pI();
/*  777 */     paramIndentingWriter.pln("typedef any _Object;");
/*  778 */     paramIndentingWriter.pO(); paramIndentingWriter.pln();
/*  779 */     writeModule2(paramType, paramIndentingWriter);
/*  780 */     writeEndif(paramIndentingWriter);
/*      */   }
/*      */ 
/*      */   protected void writeJavaRmiRemote(Type paramType, IndentingWriter paramIndentingWriter)
/*      */     throws IOException
/*      */   {
/*  794 */     writeBanner(paramType, 0, !this.isException, paramIndentingWriter);
/*  795 */     writeIfndef(paramType, 0, !this.isException, !this.isForward, paramIndentingWriter);
/*  796 */     writeModule1(paramType, paramIndentingWriter);
/*  797 */     paramIndentingWriter.pln(); paramIndentingWriter.pI();
/*  798 */     paramIndentingWriter.pln("typedef Object Remote;");
/*  799 */     paramIndentingWriter.pO(); paramIndentingWriter.pln();
/*  800 */     writeModule2(paramType, paramIndentingWriter);
/*  801 */     writeEndif(paramIndentingWriter);
/*      */   }
/*      */ 
/*      */   protected void writeIDLEntity(Type paramType, IndentingWriter paramIndentingWriter)
/*      */     throws IOException
/*      */   {
/*  816 */     writeBanner(paramType, 0, !this.isException, paramIndentingWriter);
/*  817 */     writeIfndef(paramType, 0, !this.isException, !this.isForward, paramIndentingWriter);
/*  818 */     writeModule1(paramType, paramIndentingWriter);
/*  819 */     paramIndentingWriter.pln(); paramIndentingWriter.pI();
/*  820 */     paramIndentingWriter.pln("typedef any IDLEntity;");
/*  821 */     paramIndentingWriter.pO(); paramIndentingWriter.pln();
/*  822 */     writeModule2(paramType, paramIndentingWriter);
/*  823 */     writeEndif(paramIndentingWriter);
/*      */   }
/*      */ 
/*      */   protected void getInterfaces(CompoundType paramCompoundType, Hashtable paramHashtable)
/*      */   {
/*  835 */     InterfaceType[] arrayOfInterfaceType = paramCompoundType.getInterfaces();
/*      */ 
/*  837 */     for (int i = 0; i < arrayOfInterfaceType.length; i++) {
/*  838 */       String str = arrayOfInterfaceType[i].getQualifiedName();
/*  839 */       switch (paramCompoundType.getTypeCode()) {
/*      */       case 32768:
/*      */       case 131072:
/*  842 */       }if ((!"java.io.Externalizable".equals(str)) && 
/*  843 */         (!"java.io.Serializable"
/*  843 */         .equals(str)) && (
/*  844 */         (!"org.omg.CORBA.portable.IDLEntity"
/*  844 */         .equals(str)) || 
/*  844 */         (
/*  845 */         (goto 113) && 
/*  848 */         (!"java.rmi.Remote".equals(str)))))
/*      */       {
/*  852 */         paramHashtable.put(str, arrayOfInterfaceType[i]);
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   protected void getInheritance(CompoundType paramCompoundType, Hashtable paramHashtable)
/*      */   {
/*  865 */     ClassType localClassType = paramCompoundType.getSuperclass();
/*  866 */     if (localClassType == null) return;
/*  867 */     String str = localClassType.getQualifiedName();
/*  868 */     switch (paramCompoundType.getTypeCode()) {
/*      */     case 32768:
/*      */     case 131072:
/*  871 */       if ("java.lang.Object".equals(str)) return;
/*      */       break;
/*      */     default:
/*  874 */       return;
/*      */     }
/*  876 */     paramHashtable.put(str, localClassType);
/*      */   }
/*      */ 
/*      */   protected void getMethodReferences(Vector paramVector, Hashtable paramHashtable1, Hashtable paramHashtable2, Hashtable paramHashtable3, Hashtable paramHashtable4)
/*      */   {
/*  894 */     for (int i = 0; i < paramVector.size(); i++) {
/*  895 */       CompoundType.Method localMethod = (CompoundType.Method)paramVector.elementAt(i);
/*  896 */       Type[] arrayOfType = localMethod.getArguments();
/*  897 */       Type localType = localMethod.getReturnType();
/*  898 */       getExceptions(localMethod, paramHashtable4);
/*  899 */       for (int j = 0; j < arrayOfType.length; j++)
/*  900 */         addReference(arrayOfType[j], paramHashtable1, paramHashtable2, paramHashtable3);
/*  901 */       addReference(localType, paramHashtable1, paramHashtable2, paramHashtable3);
/*      */     }
/*      */   }
/*      */ 
/*      */   protected void getMemberReferences(Vector paramVector, Hashtable paramHashtable1, Hashtable paramHashtable2, Hashtable paramHashtable3)
/*      */   {
/*  918 */     for (int i = 0; i < paramVector.size(); i++) {
/*  919 */       CompoundType.Member localMember = (CompoundType.Member)paramVector.elementAt(i);
/*  920 */       Type localType = localMember.getType();
/*  921 */       addReference(localType, paramHashtable1, paramHashtable2, paramHashtable3);
/*      */     }
/*      */   }
/*      */ 
/*      */   protected void addReference(Type paramType, Hashtable paramHashtable1, Hashtable paramHashtable2, Hashtable paramHashtable3)
/*      */   {
/*  940 */     String str = paramType.getQualifiedName();
/*  941 */     switch (paramType.getTypeCode()) {
/*      */     case 4096:
/*      */     case 8192:
/*      */     case 16384:
/*      */     case 32768:
/*      */     case 131072:
/*  947 */       paramHashtable1.put(str, paramType);
/*  948 */       return;
/*      */     case 2048:
/*  950 */       if ("org.omg.CORBA.Object".equals(str)) return;
/*  951 */       paramHashtable1.put(str, paramType);
/*  952 */       return;
/*      */     case 262144:
/*  954 */       paramHashtable3.put(str + paramType.getArrayDimension(), paramType);
/*  955 */       return;
/*      */     }
/*  957 */     if (isSpecialReference(paramType))
/*  958 */       paramHashtable2.put(str, paramType);
/*      */   }
/*      */ 
/*      */   protected boolean isSpecialReference(Type paramType)
/*      */   {
/*  975 */     String str = paramType.getQualifiedName();
/*  976 */     if ("java.io.Serializable".equals(str)) return true;
/*  977 */     if ("java.io.Externalizable".equals(str)) return true;
/*  978 */     if ("java.lang.Object".equals(str)) return true;
/*  979 */     if ("java.rmi.Remote".equals(str)) return true;
/*  980 */     if ("org.omg.CORBA.portable.IDLEntity".equals(str)) return true;
/*  981 */     return false;
/*      */   }
/*      */ 
/*      */   protected void getExceptions(CompoundType.Method paramMethod, Hashtable paramHashtable)
/*      */   {
/*  995 */     ValueType[] arrayOfValueType = paramMethod.getExceptions();
/*  996 */     for (int i = 0; i < arrayOfValueType.length; i++) {
/*  997 */       ValueType localValueType = arrayOfValueType[i];
/*  998 */       if ((localValueType.isCheckedException()) && 
/*  999 */         (!localValueType
/*  999 */         .isRemoteExceptionOrSubclass()))
/* 1000 */         paramHashtable.put(localValueType.getQualifiedName(), localValueType);
/*      */     }
/*      */   }
/*      */ 
/*      */   protected Vector getMethods(CompoundType paramCompoundType)
/*      */   {
/* 1014 */     Vector localVector = new Vector();
/* 1015 */     int i = paramCompoundType.getTypeCode();
/* 1016 */     switch (i) { case 4096:
/*      */     case 8192:
/* 1018 */       break;
/*      */     case 16384:
/*      */     case 32768:
/*      */     case 131072:
/* 1021 */       if (this.valueMethods) break; default:
/* 1022 */       return localVector;
/*      */     }
/* 1024 */     Identifier localIdentifier = paramCompoundType.getIdentifier();
/* 1025 */     CompoundType.Method[] arrayOfMethod = paramCompoundType.getMethods();
/*      */ 
/* 1027 */     for (int j = 0; j < arrayOfMethod.length; j++)
/* 1028 */       if ((!arrayOfMethod[j].isPrivate()) && 
/* 1029 */         (!arrayOfMethod[j]
/* 1029 */         .isInherited()))
/*      */       {
/* 1031 */         if (i == 32768) { String str = arrayOfMethod[j].getName();
/* 1033 */           if (("readObject".equals(str)) || 
/* 1034 */             ("writeObject"
/* 1034 */             .equals(str)) || 
/* 1035 */             ("readExternal"
/* 1035 */             .equals(str)) || 
/* 1036 */             ("writeExternal"
/* 1036 */             .equals(str))); } else if (((i != 131072) && (i != 16384)) || 
/* 1041 */           (!arrayOfMethod[j]
/* 1041 */           .isConstructor()))
/*      */         {
/* 1043 */           localVector.addElement(arrayOfMethod[j]);
/*      */         }
/*      */       }
/* 1045 */     return localVector;
/*      */   }
/*      */ 
/*      */   protected Vector getConstants(CompoundType paramCompoundType)
/*      */   {
/* 1058 */     Vector localVector = new Vector();
/* 1059 */     CompoundType.Member[] arrayOfMember = paramCompoundType.getMembers();
/* 1060 */     for (int i = 0; i < arrayOfMember.length; i++) {
/* 1061 */       Type localType = arrayOfMember[i].getType();
/* 1062 */       String str = arrayOfMember[i].getValue();
/* 1063 */       if ((arrayOfMember[i].isPublic()) && 
/* 1064 */         (arrayOfMember[i]
/* 1064 */         .isFinal()) && 
/* 1065 */         (arrayOfMember[i]
/* 1065 */         .isStatic()) && 
/* 1066 */         ((localType
/* 1066 */         .isPrimitive()) || ("String".equals(localType.getName()))) && (str != null))
/*      */       {
/* 1068 */         localVector.addElement(arrayOfMember[i]);
/*      */       }
/*      */     }
/* 1070 */     return localVector;
/*      */   }
/*      */ 
/*      */   protected Vector getData(CompoundType paramCompoundType)
/*      */   {
/* 1085 */     Vector localVector = new Vector();
/* 1086 */     if (paramCompoundType.getTypeCode() != 32768) return localVector;
/* 1087 */     ValueType localValueType = (ValueType)paramCompoundType;
/* 1088 */     CompoundType.Member[] arrayOfMember = localValueType.getMembers();
/* 1089 */     int i = !localValueType.isCustom() ? 1 : 0;
/* 1090 */     for (int j = 0; j < arrayOfMember.length; j++) {
/* 1091 */       if ((!arrayOfMember[j].isStatic()) && 
/* 1092 */         (!arrayOfMember[j]
/* 1092 */         .isTransient()) && (
/* 1093 */         (arrayOfMember[j]
/* 1093 */         .isPublic()) || (i != 0)))
/*      */       {
/* 1095 */         String str = arrayOfMember[j].getName();
/* 1096 */         for (int k = 0; k < localVector.size(); k++) {
/* 1097 */           CompoundType.Member localMember = (CompoundType.Member)localVector.elementAt(k);
/* 1098 */           if (str.compareTo(localMember.getName()) < 0) break;
/*      */         }
/* 1100 */         localVector.insertElementAt(arrayOfMember[j], k);
/*      */       }
/*      */     }
/* 1103 */     return localVector;
/*      */   }
/*      */ 
/*      */   protected void writeForwardReferences(Hashtable paramHashtable, IndentingWriter paramIndentingWriter)
/*      */     throws IOException
/*      */   {
/* 1117 */     Enumeration localEnumeration = paramHashtable.elements();
/*      */ 
/* 1119 */     while (localEnumeration.hasMoreElements()) {
/* 1120 */       Type localType = (Type)localEnumeration.nextElement();
/* 1121 */       if (localType.isCompound()) { CompoundType localCompoundType = (CompoundType)localType;
/* 1123 */         if (localCompoundType.isIDLEntity());
/*      */       }
/*      */       else {
/* 1126 */         writeForwardReference(localType, paramIndentingWriter);
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   protected void writeForwardReference(Type paramType, IndentingWriter paramIndentingWriter)
/*      */     throws IOException
/*      */   {
/* 1140 */     String str = paramType.getQualifiedName();
/* 1141 */     if ((!"java.lang.String".equals(str)) && 
/* 1142 */       ("org.omg.CORBA.Object".equals(str))) return;
/*      */ 
/* 1144 */     writeIfndef(paramType, 0, !this.isException, this.isForward, paramIndentingWriter);
/* 1145 */     writeModule1(paramType, paramIndentingWriter);
/* 1146 */     paramIndentingWriter.pln(); paramIndentingWriter.pI();
/* 1147 */     switch (paramType.getTypeCode()) { case 16384:
/*      */     case 131072:
/* 1149 */       paramIndentingWriter.p("abstract valuetype "); break;
/*      */     case 8192:
/* 1150 */       paramIndentingWriter.p("abstract interface "); break;
/*      */     case 32768:
/* 1151 */       paramIndentingWriter.p("valuetype "); break;
/*      */     case 2048:
/*      */     case 4096:
/* 1153 */       paramIndentingWriter.p("interface "); break;
/*      */     }
/*      */ 
/* 1156 */     paramIndentingWriter.pln(paramType.getIDLName() + ";");
/* 1157 */     paramIndentingWriter.pO(); paramIndentingWriter.pln();
/* 1158 */     writeModule2(paramType, paramIndentingWriter);
/* 1159 */     writeEndif(paramIndentingWriter);
/*      */   }
/*      */ 
/*      */   protected void writeForwardReference(ArrayType paramArrayType, int paramInt, IndentingWriter paramIndentingWriter)
/*      */     throws IOException
/*      */   {
/* 1177 */     Type localType = paramArrayType.getElementType();
/* 1178 */     if (paramInt < 1) {
/* 1179 */       if (localType.isCompound()) {
/* 1180 */         localObject = (CompoundType)localType;
/* 1181 */         writeForwardReference(localType, paramIndentingWriter);
/*      */       }
/* 1183 */       return;
/*      */     }
/* 1185 */     Object localObject = unEsc(localType.getIDLName()).replace(' ', '_');
/*      */ 
/* 1187 */     writeIfndef(paramArrayType, paramInt, !this.isException, this.isForward, paramIndentingWriter);
/* 1188 */     writeModule1(paramArrayType, paramIndentingWriter);
/* 1189 */     paramIndentingWriter.pln(); paramIndentingWriter.pI();
/* 1190 */     switch (localType.getTypeCode()) { case 16384:
/*      */     case 131072:
/* 1192 */       paramIndentingWriter.p("abstract valuetype "); break;
/*      */     case 8192:
/* 1193 */       paramIndentingWriter.p("abstract interface "); break;
/*      */     case 32768:
/* 1194 */       paramIndentingWriter.p("valuetype "); break;
/*      */     case 2048:
/*      */     case 4096:
/* 1196 */       paramIndentingWriter.p("interface "); break;
/*      */     }
/*      */ 
/* 1199 */     paramIndentingWriter.pln("seq" + paramInt + "_" + (String)localObject + ";");
/* 1200 */     paramIndentingWriter.pO(); paramIndentingWriter.pln();
/* 1201 */     writeModule2(paramArrayType, paramIndentingWriter);
/* 1202 */     writeEndif(paramIndentingWriter);
/*      */   }
/*      */ 
/*      */   protected void writeIDLEntityIncludes(Hashtable paramHashtable, IndentingWriter paramIndentingWriter)
/*      */     throws IOException
/*      */   {
/* 1215 */     Enumeration localEnumeration = paramHashtable.elements();
/* 1216 */     while (localEnumeration.hasMoreElements()) {
/* 1217 */       Type localType = (Type)localEnumeration.nextElement();
/* 1218 */       if (localType.isCompound()) {
/* 1219 */         CompoundType localCompoundType = (CompoundType)localType;
/* 1220 */         if (localCompoundType.isIDLEntity()) {
/* 1221 */           writeInclude(localCompoundType, 0, !this.isThrown, paramIndentingWriter);
/* 1222 */           paramHashtable.remove(localCompoundType.getQualifiedName());
/*      */         }
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   protected void writeIncludes(Hashtable paramHashtable, boolean paramBoolean, IndentingWriter paramIndentingWriter)
/*      */     throws IOException
/*      */   {
/* 1240 */     Enumeration localEnumeration = paramHashtable.elements();
/* 1241 */     while (localEnumeration.hasMoreElements()) {
/* 1242 */       CompoundType localCompoundType = (CompoundType)localEnumeration.nextElement();
/* 1243 */       writeInclude(localCompoundType, 0, paramBoolean, paramIndentingWriter);
/*      */     }
/*      */   }
/*      */ 
/*      */   protected void writeBoxedRMIIncludes(Hashtable paramHashtable, IndentingWriter paramIndentingWriter)
/*      */     throws IOException
/*      */   {
/* 1258 */     Enumeration localEnumeration1 = paramHashtable.elements();
/*      */ 
/* 1260 */     while (localEnumeration1.hasMoreElements()) {
/* 1261 */       ArrayType localArrayType1 = (ArrayType)localEnumeration1.nextElement();
/* 1262 */       int i = localArrayType1.getArrayDimension();
/* 1263 */       Type localType = localArrayType1.getElementType();
/*      */ 
/* 1265 */       Enumeration localEnumeration2 = paramHashtable.elements();
/*      */       while (true) { if (!localEnumeration2.hasMoreElements()) break label93;
/* 1267 */         ArrayType localArrayType2 = (ArrayType)localEnumeration2.nextElement();
/* 1268 */         if ((localType == localArrayType2.getElementType()) && 
/* 1269 */           (i < localArrayType2
/* 1269 */           .getArrayDimension()))
/*      */           break;
/*      */       }
/* 1272 */       label93: writeInclude(localArrayType1, i, !this.isThrown, paramIndentingWriter);
/*      */     }
/*      */   }
/*      */ 
/*      */   protected void writeInheritedIncludes(Hashtable paramHashtable, IndentingWriter paramIndentingWriter)
/*      */     throws IOException
/*      */   {
/* 1286 */     Enumeration localEnumeration = paramHashtable.elements();
/* 1287 */     while (localEnumeration.hasMoreElements()) {
/* 1288 */       CompoundType localCompoundType = (CompoundType)localEnumeration.nextElement();
/* 1289 */       writeInclude(localCompoundType, 0, !this.isThrown, paramIndentingWriter);
/*      */     }
/*      */   }
/*      */ 
/*      */   protected void writeInclude(Type paramType, int paramInt, boolean paramBoolean, IndentingWriter paramIndentingWriter)
/*      */     throws IOException
/*      */   {
/*      */     CompoundType localCompoundType;
/*      */     Object localObject;
/*      */     String[] arrayOfString;
/*      */     String str;
/* 1310 */     if (paramType.isCompound()) {
/* 1311 */       localCompoundType = (CompoundType)paramType;
/* 1312 */       localObject = localCompoundType.getQualifiedName();
/* 1313 */       if ("java.lang.String".equals(localObject)) {
/* 1314 */         writeIncOrb(paramIndentingWriter);
/* 1315 */         return;
/*      */       }
/* 1317 */       if ("org.omg.CORBA.Object".equals(localObject))
/* 1318 */         return;
/* 1319 */       arrayOfString = getIDLModuleNames(localCompoundType);
/* 1320 */       str = unEsc(localCompoundType.getIDLName());
/*      */ 
/* 1322 */       if (localCompoundType.isException())
/* 1323 */         if (localCompoundType.isIDLEntityException()) {
/* 1324 */           if (localCompoundType.isCORBAUserException()) {
/* 1325 */             if (paramBoolean) str = unEsc(localCompoundType.getIDLExceptionName()); 
/*      */           }
/*      */           else
/* 1327 */             str = localCompoundType.getName();
/* 1328 */         } else if (paramBoolean)
/* 1329 */           str = unEsc(localCompoundType.getIDLExceptionName());
/*      */     }
/* 1331 */     else if (paramType.isArray()) {
/* 1332 */       localObject = paramType.getElementType();
/* 1333 */       if (paramInt > 0) {
/* 1334 */         arrayOfString = getIDLModuleNames(paramType);
/* 1335 */         str = "seq" + paramInt + "_" + unEsc(((Type)localObject).getIDLName().replace(' ', '_'));
/*      */       }
/*      */       else {
/* 1338 */         if (!((Type)localObject).isCompound()) return;
/* 1339 */         localCompoundType = (CompoundType)localObject;
/* 1340 */         arrayOfString = getIDLModuleNames(localCompoundType);
/* 1341 */         str = unEsc(localCompoundType.getIDLName());
/* 1342 */         writeInclude(localCompoundType, arrayOfString, str, paramIndentingWriter);
/* 1343 */         return;
/*      */       }
/*      */     } else {
/* 1346 */       return;
/* 1347 */     }writeInclude(paramType, arrayOfString, str, paramIndentingWriter);
/*      */   }
/*      */ 
/*      */   protected void writeInclude(Type paramType, String[] paramArrayOfString, String paramString, IndentingWriter paramIndentingWriter)
/*      */     throws IOException
/*      */   {
/* 1365 */     if (paramType.isCompound()) {
/* 1366 */       CompoundType localCompoundType = (CompoundType)paramType;
/*      */ 
/* 1368 */       if ((this.ifHash.size() > 0) && 
/* 1369 */         (localCompoundType
/* 1369 */         .isIDLEntity())) {
/* 1370 */         String str1 = paramType.getQualifiedName();
/*      */ 
/* 1372 */         Enumeration localEnumeration = this.ifHash.keys();
/* 1373 */         while (localEnumeration.hasMoreElements()) {
/* 1374 */           String str2 = (String)localEnumeration.nextElement();
/* 1375 */           if (str1.startsWith(str2)) {
/* 1376 */             String str3 = (String)this.ifHash.get(str2);
/* 1377 */             paramIndentingWriter.pln("#include \"" + str3 + "\"");
/* 1378 */             return;
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/* 1383 */     else if (!paramType.isArray()) {
/* 1384 */       return;
/*      */     }
/* 1386 */     paramIndentingWriter.p("#include \"");
/* 1387 */     for (int i = 0; i < paramArrayOfString.length; i++) paramIndentingWriter.p(paramArrayOfString[i] + "/");
/* 1388 */     paramIndentingWriter.p(paramString + ".idl\"");
/* 1389 */     paramIndentingWriter.pln();
/*      */   }
/*      */ 
/*      */   protected String getQualifiedName(Type paramType)
/*      */   {
/* 1401 */     String[] arrayOfString = getIDLModuleNames(paramType);
/* 1402 */     int i = arrayOfString.length;
/* 1403 */     StringBuffer localStringBuffer = new StringBuffer();
/* 1404 */     for (int j = 0; j < i; j++)
/* 1405 */       localStringBuffer.append(arrayOfString[j] + ".");
/* 1406 */     localStringBuffer.append(paramType.getIDLName());
/* 1407 */     return localStringBuffer.toString();
/*      */   }
/*      */ 
/*      */   protected String getQualifiedIDLName(Type paramType)
/*      */   {
/* 1418 */     if (paramType.isPrimitive())
/* 1419 */       return paramType.getIDLName();
/* 1420 */     if ((!paramType.isArray()) && 
/* 1421 */       ("org.omg.CORBA.Object"
/* 1421 */       .equals(paramType
/* 1421 */       .getQualifiedName()))) {
/* 1422 */       return paramType.getIDLName();
/*      */     }
/* 1424 */     String[] arrayOfString = getIDLModuleNames(paramType);
/* 1425 */     int i = arrayOfString.length;
/* 1426 */     if (i > 0) {
/* 1427 */       StringBuffer localStringBuffer = new StringBuffer();
/* 1428 */       for (int j = 0; j < i; j++)
/* 1429 */         localStringBuffer.append("::" + arrayOfString[j]);
/* 1430 */       localStringBuffer.append("::" + paramType.getIDLName());
/* 1431 */       return localStringBuffer.toString();
/*      */     }
/* 1433 */     return paramType.getIDLName();
/*      */   }
/*      */ 
/*      */   protected String[] getIDLModuleNames(Type paramType)
/*      */   {
/* 1448 */     String[] arrayOfString1 = paramType.getIDLModuleNames();
/*      */     CompoundType localCompoundType;
/* 1450 */     if (paramType.isCompound()) {
/* 1451 */       localCompoundType = (CompoundType)paramType;
/* 1452 */       if (!localCompoundType.isIDLEntity) return arrayOfString1;
/*      */ 
/* 1454 */       if ("org.omg.CORBA.portable.IDLEntity"
/* 1454 */         .equals(paramType
/* 1454 */         .getQualifiedName()))
/* 1455 */         return arrayOfString1;
/*      */     }
/* 1457 */     else if (paramType.isArray()) {
/* 1458 */       localObject = paramType.getElementType();
/* 1459 */       if (((Type)localObject).isCompound()) {
/* 1460 */         localCompoundType = (CompoundType)localObject;
/* 1461 */         if (!localCompoundType.isIDLEntity) return arrayOfString1;
/*      */ 
/* 1463 */         if ("org.omg.CORBA.portable.IDLEntity"
/* 1463 */           .equals(paramType
/* 1463 */           .getQualifiedName()))
/* 1464 */           return arrayOfString1;
/*      */       } else {
/* 1466 */         return arrayOfString1;
/*      */       }
/*      */     } else { return arrayOfString1; }
/*      */ 
/*      */ 
/* 1471 */     Object localObject = new Vector();
/* 1472 */     if (!translateJavaPackage(localCompoundType, (Vector)localObject)) {
/* 1473 */       stripJavaPackage(localCompoundType, (Vector)localObject);
/*      */     }
/* 1475 */     if (localCompoundType.isBoxed()) {
/* 1476 */       ((Vector)localObject).insertElementAt("org", 0);
/* 1477 */       ((Vector)localObject).insertElementAt("omg", 1);
/* 1478 */       ((Vector)localObject).insertElementAt("boxedIDL", 2);
/*      */     }
/* 1480 */     if (paramType.isArray()) {
/* 1481 */       ((Vector)localObject).insertElementAt("org", 0);
/* 1482 */       ((Vector)localObject).insertElementAt("omg", 1);
/* 1483 */       ((Vector)localObject).insertElementAt("boxedRMI", 2);
/*      */     }
/* 1485 */     String[] arrayOfString2 = new String[((Vector)localObject).size()];
/* 1486 */     ((Vector)localObject).copyInto(arrayOfString2);
/* 1487 */     return arrayOfString2;
/*      */   }
/*      */ 
/*      */   protected boolean translateJavaPackage(CompoundType paramCompoundType, Vector paramVector)
/*      */   {
/* 1502 */     paramVector.removeAllElements();
/* 1503 */     boolean bool = false;
/* 1504 */     String str1 = null;
/* 1505 */     if (!paramCompoundType.isIDLEntity()) return bool;
/*      */ 
/* 1507 */     String str2 = paramCompoundType.getPackageName();
/* 1508 */     if (str2 == null) return bool;
/* 1509 */     StringTokenizer localStringTokenizer1 = new StringTokenizer(str2, ".");
/* 1510 */     while (localStringTokenizer1.hasMoreTokens()) paramVector.addElement(localStringTokenizer1.nextToken());
/*      */ 
/* 1512 */     if (this.imHash.size() > 0) {
/* 1513 */       Enumeration localEnumeration = this.imHash.keys();
/*      */ 
/* 1516 */       while (localEnumeration.hasMoreElements()) {
/* 1517 */         String str3 = (String)localEnumeration.nextElement();
/* 1518 */         StringTokenizer localStringTokenizer2 = new StringTokenizer(str3, ".");
/* 1519 */         int i = paramVector.size();
/*      */ 
/* 1521 */         for (int j = 0; ; j++) { if ((j >= i) || (!localStringTokenizer2.hasMoreTokens())) break label168;
/* 1522 */           if (!paramVector.elementAt(j).equals(localStringTokenizer2.nextToken()))
/*      */             break;
/*      */         }
/* 1525 */         label168: if (localStringTokenizer2.hasMoreTokens()) { str1 = localStringTokenizer2.nextToken();
/* 1527 */           if ((!paramCompoundType.getName().equals(str1)) || 
/* 1528 */             (localStringTokenizer2
/* 1528 */             .hasMoreTokens()));
/*      */         }
/*      */         else {
/* 1532 */           bool = true;
/* 1533 */           for (int k = 0; k < j; k++) {
/* 1534 */             paramVector.removeElementAt(0);
/*      */           }
/* 1536 */           String str4 = (String)this.imHash.get(str3);
/* 1537 */           StringTokenizer localStringTokenizer3 = new StringTokenizer(str4, "::");
/*      */ 
/* 1539 */           int m = localStringTokenizer3.countTokens();
/* 1540 */           int n = 0;
/* 1541 */           if (str1 != null) m--;
/* 1542 */           for (n = 0; n < m; n++)
/* 1543 */             paramVector.insertElementAt(localStringTokenizer3.nextToken(), n);
/* 1544 */           if (str1 != null) {
/* 1545 */             String str5 = localStringTokenizer3.nextToken();
/* 1546 */             if (!paramCompoundType.getName().equals(str5))
/* 1547 */               paramVector.insertElementAt(str5, n); 
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/* 1551 */     return bool;
/*      */   }
/*      */ 
/*      */   protected void stripJavaPackage(CompoundType paramCompoundType, Vector paramVector)
/*      */   {
/* 1567 */     paramVector.removeAllElements();
/* 1568 */     if (!paramCompoundType.isIDLEntity()) return;
/*      */ 
/* 1570 */     String str1 = paramCompoundType.getRepositoryID().substring(4);
/* 1571 */     StringTokenizer localStringTokenizer1 = new StringTokenizer(str1, "/");
/* 1572 */     if (localStringTokenizer1.countTokens() < 2) return;
/*      */ 
/* 1574 */     while (localStringTokenizer1.hasMoreTokens())
/* 1575 */       paramVector.addElement(localStringTokenizer1.nextToken());
/* 1576 */     paramVector.removeElementAt(paramVector.size() - 1);
/*      */ 
/* 1578 */     String str2 = paramCompoundType.getPackageName();
/* 1579 */     if (str2 == null) return;
/* 1580 */     Vector localVector = new Vector();
/* 1581 */     StringTokenizer localStringTokenizer2 = new StringTokenizer(str2, ".");
/* 1582 */     while (localStringTokenizer2.hasMoreTokens()) localVector.addElement(localStringTokenizer2.nextToken());
/*      */ 
/* 1584 */     int i = paramVector.size() - 1;
/* 1585 */     for (int j = localVector.size() - 1; 
/* 1586 */       (i >= 0) && (j >= 0); 
/* 1590 */       j--)
/*      */     {
/* 1587 */       String str3 = (String)paramVector.elementAt(i);
/* 1588 */       String str4 = (String)localVector.elementAt(j);
/* 1589 */       if (!str4.equals(str3)) break;
/* 1590 */       i--;
/*      */     }
/* 1592 */     for (int k = 0; k <= i; k++)
/* 1593 */       paramVector.removeElementAt(0);
/*      */   }
/*      */ 
/*      */   protected void writeSequence(Generator.OutputType paramOutputType, IndentingWriter paramIndentingWriter)
/*      */     throws IOException
/*      */   {
/* 1610 */     ArrayType localArrayType = (ArrayType)paramOutputType.getType();
/* 1611 */     Type localType = localArrayType.getElementType();
/* 1612 */     String str1 = paramOutputType.getName();
/* 1613 */     int i = Integer.parseInt(str1.substring(3, str1.indexOf("_")));
/* 1614 */     String str2 = unEsc(localType.getIDLName()).replace(' ', '_');
/* 1615 */     String str3 = getQualifiedIDLName(localType);
/* 1616 */     String str4 = localType.getQualifiedName();
/*      */ 
/* 1618 */     String str5 = localArrayType.getRepositoryID();
/* 1619 */     int j = str5.indexOf('[');
/* 1620 */     int k = str5.lastIndexOf('[') + 1;
/*      */ 
/* 1623 */     StringBuffer localStringBuffer = new StringBuffer(str5
/* 1622 */       .substring(0, j) + 
/* 1622 */       str5
/* 1623 */       .substring(k));
/*      */ 
/* 1624 */     for (int m = 0; m < i; m++) localStringBuffer.insert(j, '[');
/*      */ 
/* 1626 */     String str6 = "seq" + i + "_" + str2;
/* 1627 */     int n = 0;
/* 1628 */     if (localType.isCompound()) {
/* 1629 */       CompoundType localCompoundType = (CompoundType)localType;
/* 1630 */       n = (localCompoundType.isIDLEntity()) || (localCompoundType.isCORBAObject()) ? 1 : 0;
/*      */     }
/*      */ 
/* 1633 */     if ((localType
/* 1633 */       .isCompound()) && 
/* 1634 */       (!isSpecialReference(localType)) && 
/* 1634 */       (i == 1) && (n == 0));
/* 1638 */     int i1 = (!"org.omg.CORBA.Object"
/* 1637 */       .equals(str4)) && 
/* 1638 */       (!"java.lang.String"
/* 1638 */       .equals(str4)) ? 
/* 1638 */       1 : 0;
/*      */ 
/* 1640 */     writeBanner(localArrayType, i, !this.isException, paramIndentingWriter);
/* 1641 */     if ((i == 1) && ("java.lang.String".equals(str4)))
/* 1642 */       writeIncOrb(paramIndentingWriter);
/* 1643 */     if (((i != 1) || (!"org.omg.CORBA.Object".equals(str4))) && (
/* 1644 */       (isSpecialReference(localType)) || (i > 1) || (n != 0)))
/* 1645 */       writeInclude(localArrayType, i - 1, !this.isThrown, paramIndentingWriter);
/* 1646 */     writeIfndef(localArrayType, i, !this.isException, !this.isForward, paramIndentingWriter);
/* 1647 */     if (i1 != 0)
/* 1648 */       writeForwardReference(localArrayType, i - 1, paramIndentingWriter);
/* 1649 */     writeModule1(localArrayType, paramIndentingWriter);
/* 1650 */     paramIndentingWriter.pln(); paramIndentingWriter.pI();
/* 1651 */     paramIndentingWriter.p("valuetype " + str6);
/* 1652 */     paramIndentingWriter.p(" sequence<");
/* 1653 */     if (i == 1) { paramIndentingWriter.p(str3);
/*      */     } else {
/* 1655 */       paramIndentingWriter.p("seq" + (i - 1) + "_");
/* 1656 */       paramIndentingWriter.p(str2);
/*      */     }
/* 1658 */     paramIndentingWriter.pln(">;");
/* 1659 */     paramIndentingWriter.pO(); paramIndentingWriter.pln();
/* 1660 */     paramIndentingWriter.pln("#pragma ID " + str6 + " \"" + localStringBuffer + "\"");
/* 1661 */     paramIndentingWriter.pln();
/* 1662 */     writeModule2(localArrayType, paramIndentingWriter);
/* 1663 */     if (i1 != 0)
/* 1664 */       writeInclude(localArrayType, i - 1, !this.isThrown, paramIndentingWriter);
/* 1665 */     writeEndif(paramIndentingWriter);
/*      */   }
/*      */ 
/*      */   protected void writeBoxedIDL(CompoundType paramCompoundType, IndentingWriter paramIndentingWriter)
/*      */     throws IOException
/*      */   {
/* 1678 */     String[] arrayOfString1 = getIDLModuleNames(paramCompoundType);
/* 1679 */     int i = arrayOfString1.length;
/* 1680 */     String[] arrayOfString2 = new String[i - 3];
/* 1681 */     for (int j = 0; j < i - 3; j++) arrayOfString2[j] = arrayOfString1[(j + 3)];
/* 1682 */     String str = unEsc(paramCompoundType.getIDLName());
/*      */ 
/* 1684 */     writeBanner(paramCompoundType, 0, !this.isException, paramIndentingWriter);
/* 1685 */     writeInclude(paramCompoundType, arrayOfString2, str, paramIndentingWriter);
/* 1686 */     writeIfndef(paramCompoundType, 0, !this.isException, !this.isForward, paramIndentingWriter);
/* 1687 */     writeModule1(paramCompoundType, paramIndentingWriter);
/* 1688 */     paramIndentingWriter.pln(); paramIndentingWriter.pI();
/*      */ 
/* 1690 */     paramIndentingWriter.p("valuetype " + str + " ");
/* 1691 */     for (int k = 0; k < arrayOfString2.length; k++)
/* 1692 */       paramIndentingWriter.p("::" + arrayOfString2[k]);
/* 1693 */     paramIndentingWriter.pln("::" + str + ";");
/*      */ 
/* 1695 */     paramIndentingWriter.pO(); paramIndentingWriter.pln();
/* 1696 */     writeRepositoryID(paramCompoundType, paramIndentingWriter);
/* 1697 */     paramIndentingWriter.pln();
/* 1698 */     writeModule2(paramCompoundType, paramIndentingWriter);
/* 1699 */     writeEndif(paramIndentingWriter);
/*      */   }
/*      */ 
/*      */   protected void writeException(ClassType paramClassType, IndentingWriter paramIndentingWriter)
/*      */     throws IOException
/*      */   {
/* 1712 */     writeBanner(paramClassType, 0, this.isException, paramIndentingWriter);
/* 1713 */     writeIfndef(paramClassType, 0, this.isException, !this.isForward, paramIndentingWriter);
/* 1714 */     writeForwardReference(paramClassType, paramIndentingWriter);
/* 1715 */     writeModule1(paramClassType, paramIndentingWriter);
/* 1716 */     paramIndentingWriter.pln(); paramIndentingWriter.pI();
/*      */ 
/* 1718 */     paramIndentingWriter.pln("exception " + paramClassType.getIDLExceptionName() + " {");
/* 1719 */     paramIndentingWriter.pln(); paramIndentingWriter.pI();
/* 1720 */     paramIndentingWriter.pln(paramClassType.getIDLName() + " value;");
/* 1721 */     paramIndentingWriter.pO(); paramIndentingWriter.pln();
/* 1722 */     paramIndentingWriter.pln("};");
/*      */ 
/* 1724 */     paramIndentingWriter.pO(); paramIndentingWriter.pln();
/* 1725 */     writeModule2(paramClassType, paramIndentingWriter);
/* 1726 */     writeInclude(paramClassType, 0, !this.isThrown, paramIndentingWriter);
/* 1727 */     writeEndif(paramIndentingWriter);
/*      */   }
/*      */ 
/*      */   protected void writeRepositoryID(Type paramType, IndentingWriter paramIndentingWriter)
/*      */     throws IOException
/*      */   {
/* 1740 */     String str = paramType.getRepositoryID();
/* 1741 */     if (paramType.isCompound()) {
/* 1742 */       CompoundType localCompoundType = (CompoundType)paramType;
/* 1743 */       if (localCompoundType.isBoxed()) {
/* 1744 */         str = localCompoundType.getBoxedRepositoryID();
/*      */       }
/*      */     }
/* 1747 */     paramIndentingWriter.pln("#pragma ID " + paramType.getIDLName() + " \"" + str + "\"");
/*      */   }
/*      */ 
/*      */   protected void writeInherits(Hashtable paramHashtable, boolean paramBoolean, IndentingWriter paramIndentingWriter)
/*      */     throws IOException
/*      */   {
/* 1765 */     int i = paramHashtable.size();
/* 1766 */     int j = 0;
/* 1767 */     int k = 0;
/* 1768 */     if (i < 1) return;
/* 1769 */     Enumeration localEnumeration = paramHashtable.elements();
/*      */     CompoundType localCompoundType;
/* 1771 */     if (paramBoolean)
/* 1772 */       while (localEnumeration.hasMoreElements()) {
/* 1773 */         localCompoundType = (CompoundType)localEnumeration.nextElement();
/* 1774 */         if (localCompoundType.getTypeCode() == 8192) k++;
/*      */       }
/* 1776 */     j = i - k;
/*      */     int m;
/* 1778 */     if (j > 0) {
/* 1779 */       paramIndentingWriter.p(": ");
/* 1780 */       localEnumeration = paramHashtable.elements();
/* 1781 */       while (localEnumeration.hasMoreElements()) {
/* 1782 */         localCompoundType = (CompoundType)localEnumeration.nextElement();
/* 1783 */         if (localCompoundType.isClass()) {
/* 1784 */           paramIndentingWriter.p(getQualifiedIDLName(localCompoundType));
/* 1785 */           if (j > 1) paramIndentingWriter.p(", ");
/* 1786 */           else if (i > 1) paramIndentingWriter.p(" ");
/*      */         }
/*      */       }
/*      */ 
/* 1790 */       m = 0;
/* 1791 */       localEnumeration = paramHashtable.elements();
/* 1792 */       while (localEnumeration.hasMoreElements()) {
/* 1793 */         localCompoundType = (CompoundType)localEnumeration.nextElement();
/* 1794 */         if ((!localCompoundType.isClass()) && 
/* 1795 */           (localCompoundType
/* 1795 */           .getTypeCode() != 8192)) {
/* 1796 */           if (m++ > 0) paramIndentingWriter.p(", ");
/* 1797 */           paramIndentingWriter.p(getQualifiedIDLName(localCompoundType));
/*      */         }
/*      */       }
/*      */     }
/* 1801 */     if (k > 0) {
/* 1802 */       paramIndentingWriter.p(" supports ");
/* 1803 */       m = 0;
/* 1804 */       localEnumeration = paramHashtable.elements();
/* 1805 */       while (localEnumeration.hasMoreElements()) {
/* 1806 */         localCompoundType = (CompoundType)localEnumeration.nextElement();
/* 1807 */         if (localCompoundType.getTypeCode() == 8192) {
/* 1808 */           if (m++ > 0) paramIndentingWriter.p(", ");
/* 1809 */           paramIndentingWriter.p(getQualifiedIDLName(localCompoundType));
/*      */         }
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   protected void writeConstant(CompoundType.Member paramMember, IndentingWriter paramIndentingWriter)
/*      */     throws IOException
/*      */   {
/* 1825 */     Type localType = paramMember.getType();
/* 1826 */     paramIndentingWriter.p("const ");
/* 1827 */     paramIndentingWriter.p(getQualifiedIDLName(localType));
/* 1828 */     paramIndentingWriter.p(" " + paramMember.getIDLName() + " = " + paramMember.getValue());
/* 1829 */     paramIndentingWriter.pln(";");
/*      */   }
/*      */ 
/*      */   protected void writeData(CompoundType.Member paramMember, IndentingWriter paramIndentingWriter)
/*      */     throws IOException
/*      */   {
/* 1843 */     if (paramMember.isInnerClassDeclaration()) return;
/* 1844 */     Type localType = paramMember.getType();
/* 1845 */     if (paramMember.isPublic())
/* 1846 */       paramIndentingWriter.p("public ");
/* 1847 */     else paramIndentingWriter.p("private ");
/* 1848 */     paramIndentingWriter.pln(getQualifiedIDLName(localType) + " " + paramMember
/* 1849 */       .getIDLName() + ";");
/*      */   }
/*      */ 
/*      */   protected void writeAttribute(CompoundType.Method paramMethod, IndentingWriter paramIndentingWriter)
/*      */     throws IOException
/*      */   {
/* 1863 */     if (paramMethod.getAttributeKind() == 5) return;
/* 1864 */     Type localType = paramMethod.getReturnType();
/* 1865 */     if (!paramMethod.isReadWriteAttribute()) paramIndentingWriter.p("readonly ");
/* 1866 */     paramIndentingWriter.p("attribute " + getQualifiedIDLName(localType) + " ");
/* 1867 */     paramIndentingWriter.pln(paramMethod.getAttributeName() + ";");
/*      */   }
/*      */ 
/*      */   protected void writeMethod(CompoundType.Method paramMethod, IndentingWriter paramIndentingWriter)
/*      */     throws IOException
/*      */   {
/* 1881 */     if (paramMethod.isAttribute()) {
/* 1882 */       writeAttribute(paramMethod, paramIndentingWriter);
/* 1883 */       return;
/*      */     }
/* 1885 */     Type[] arrayOfType = paramMethod.getArguments();
/* 1886 */     String[] arrayOfString1 = paramMethod.getArgumentNames();
/* 1887 */     Type localType = paramMethod.getReturnType();
/* 1888 */     Hashtable localHashtable = new Hashtable();
/* 1889 */     getExceptions(paramMethod, localHashtable);
/*      */ 
/* 1891 */     if (paramMethod.isConstructor()) {
/* 1892 */       if (this.factory) paramIndentingWriter.p("factory " + paramMethod.getIDLName() + "("); else
/* 1893 */         paramIndentingWriter.p("init(");
/*      */     } else {
/* 1895 */       paramIndentingWriter.p(getQualifiedIDLName(localType));
/* 1896 */       paramIndentingWriter.p(" " + paramMethod.getIDLName() + "(");
/*      */     }
/* 1898 */     paramIndentingWriter.pI();
/*      */ 
/* 1900 */     for (int i = 0; i < arrayOfType.length; i++) {
/* 1901 */       if (i > 0) paramIndentingWriter.pln(","); else
/* 1902 */         paramIndentingWriter.pln();
/* 1903 */       paramIndentingWriter.p("in ");
/* 1904 */       paramIndentingWriter.p(getQualifiedIDLName(arrayOfType[i]));
/* 1905 */       paramIndentingWriter.p(" " + arrayOfString1[i]);
/*      */     }
/* 1907 */     paramIndentingWriter.pO();
/* 1908 */     paramIndentingWriter.p(" )");
/*      */ 
/* 1910 */     if (localHashtable.size() > 0) {
/* 1911 */       paramIndentingWriter.pln(" raises (");
/* 1912 */       paramIndentingWriter.pI();
/* 1913 */       i = 0;
/* 1914 */       Enumeration localEnumeration = localHashtable.elements();
/* 1915 */       while (localEnumeration.hasMoreElements()) {
/* 1916 */         ValueType localValueType = (ValueType)localEnumeration.nextElement();
/* 1917 */         if (i > 0) paramIndentingWriter.pln(",");
/* 1918 */         if (localValueType.isIDLEntityException()) {
/* 1919 */           if (localValueType.isCORBAUserException()) {
/* 1920 */             paramIndentingWriter.p("::org::omg::CORBA::UserEx");
/*      */           } else {
/* 1922 */             String[] arrayOfString2 = getIDLModuleNames(localValueType);
/* 1923 */             for (int j = 0; j < arrayOfString2.length; j++)
/* 1924 */               paramIndentingWriter.p("::" + arrayOfString2[j]);
/* 1925 */             paramIndentingWriter.p("::" + localValueType.getName());
/*      */           }
/*      */         } else paramIndentingWriter.p(localValueType.getQualifiedIDLExceptionName(true));
/* 1928 */         i++;
/*      */       }
/* 1930 */       paramIndentingWriter.pO();
/* 1931 */       paramIndentingWriter.p(" )");
/*      */     }
/*      */ 
/* 1934 */     paramIndentingWriter.pln(";");
/*      */   }
/*      */ 
/*      */   protected String unEsc(String paramString)
/*      */   {
/* 1945 */     if (paramString.startsWith("_")) return paramString.substring(1);
/* 1946 */     return paramString;
/*      */   }
/*      */ 
/*      */   protected void writeBanner(Type paramType, int paramInt, boolean paramBoolean, IndentingWriter paramIndentingWriter)
/*      */     throws IOException
/*      */   {
/* 1963 */     String[] arrayOfString = getIDLModuleNames(paramType);
/* 1964 */     String str1 = unEsc(paramType.getIDLName());
/*      */     Object localObject;
/* 1965 */     if ((paramBoolean) && (paramType.isClass())) {
/* 1966 */       localObject = (ClassType)paramType;
/* 1967 */       str1 = unEsc(((ClassType)localObject).getIDLExceptionName());
/*      */     }
/* 1969 */     if ((paramInt > 0) && (paramType.isArray())) {
/* 1970 */       localObject = paramType.getElementType();
/* 1971 */       str1 = "seq" + paramInt + "_" + unEsc(((Type)localObject).getIDLName().replace(' ', '_'));
/*      */     }
/*      */ 
/* 1974 */     paramIndentingWriter.pln("/**");
/* 1975 */     paramIndentingWriter.p(" * ");
/* 1976 */     for (int i = 0; i < arrayOfString.length; i++)
/* 1977 */       paramIndentingWriter.p(arrayOfString[i] + "/");
/* 1978 */     paramIndentingWriter.pln(str1 + ".idl");
/* 1979 */     paramIndentingWriter.pln(" * Generated by rmic -idl. Do not edit");
/*      */ 
/* 1982 */     String str2 = DateFormat.getDateTimeInstance(0, 0, 
/* 1981 */       Locale.getDefault())
/* 1982 */       .format(new Date());
/*      */ 
/* 1983 */     String str3 = "o'clock";
/* 1984 */     int j = str2.indexOf(str3);
/* 1985 */     paramIndentingWriter.p(" * ");
/* 1986 */     if (j > -1)
/* 1987 */       paramIndentingWriter.pln(str2.substring(0, j) + str2.substring(j + str3.length()));
/* 1988 */     else paramIndentingWriter.pln(str2);
/* 1989 */     paramIndentingWriter.pln(" */");
/* 1990 */     paramIndentingWriter.pln();
/*      */   }
/*      */ 
/*      */   protected void writeIncOrb(IndentingWriter paramIndentingWriter)
/*      */     throws IOException
/*      */   {
/* 2001 */     paramIndentingWriter.pln("#include \"orb.idl\"");
/*      */   }
/*      */ 
/*      */   protected void writeIfndef(Type paramType, int paramInt, boolean paramBoolean1, boolean paramBoolean2, IndentingWriter paramIndentingWriter)
/*      */     throws IOException
/*      */   {
/* 2020 */     String[] arrayOfString = getIDLModuleNames(paramType);
/* 2021 */     String str = unEsc(paramType.getIDLName());
/*      */     Object localObject;
/* 2022 */     if ((paramBoolean1) && (paramType.isClass())) {
/* 2023 */       localObject = (ClassType)paramType;
/* 2024 */       str = unEsc(((ClassType)localObject).getIDLExceptionName());
/*      */     }
/* 2026 */     if ((paramInt > 0) && (paramType.isArray())) {
/* 2027 */       localObject = paramType.getElementType();
/* 2028 */       str = "seq" + paramInt + "_" + unEsc(((Type)localObject).getIDLName().replace(' ', '_'));
/*      */     }
/* 2030 */     paramIndentingWriter.pln();
/* 2031 */     paramIndentingWriter.p("#ifndef __");
/* 2032 */     for (int i = 0; i < arrayOfString.length; i++) paramIndentingWriter.p(arrayOfString[i] + "_");
/* 2033 */     paramIndentingWriter.pln(str + "__");
/* 2034 */     if (!paramBoolean2) {
/* 2035 */       paramIndentingWriter.p("#define __");
/* 2036 */       for (i = 0; i < arrayOfString.length; i++) paramIndentingWriter.p(arrayOfString[i] + "_");
/* 2037 */       paramIndentingWriter.pln(str + "__");
/* 2038 */       paramIndentingWriter.pln();
/*      */     }
/*      */   }
/*      */ 
/*      */   protected void writeEndif(IndentingWriter paramIndentingWriter)
/*      */     throws IOException
/*      */   {
/* 2050 */     paramIndentingWriter.pln("#endif");
/* 2051 */     paramIndentingWriter.pln();
/*      */   }
/*      */ 
/*      */   protected void writeModule1(Type paramType, IndentingWriter paramIndentingWriter)
/*      */     throws IOException
/*      */   {
/* 2064 */     String[] arrayOfString = getIDLModuleNames(paramType);
/* 2065 */     paramIndentingWriter.pln();
/* 2066 */     for (int i = 0; i < arrayOfString.length; i++)
/* 2067 */       paramIndentingWriter.pln("module " + arrayOfString[i] + " {");
/*      */   }
/*      */ 
/*      */   protected void writeModule2(Type paramType, IndentingWriter paramIndentingWriter)
/*      */     throws IOException
/*      */   {
/* 2079 */     String[] arrayOfString = getIDLModuleNames(paramType);
/* 2080 */     for (int i = 0; i < arrayOfString.length; i++) paramIndentingWriter.pln("};");
/* 2081 */     paramIndentingWriter.pln();
/*      */   }
/*      */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     sun.rmi.rmic.iiop.IDLGenerator
 * JD-Core Version:    0.6.2
 */