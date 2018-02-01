/*      */ package com.sun.tools.corba.se.idl.toJavaPortable;
/*      */ 
/*      */ import com.sun.tools.corba.se.idl.EnumEntry;
/*      */ import com.sun.tools.corba.se.idl.GenFileStream;
/*      */ import com.sun.tools.corba.se.idl.PrimitiveEntry;
/*      */ import com.sun.tools.corba.se.idl.SequenceEntry;
/*      */ import com.sun.tools.corba.se.idl.StringEntry;
/*      */ import com.sun.tools.corba.se.idl.SymtabEntry;
/*      */ import com.sun.tools.corba.se.idl.TypedefEntry;
/*      */ import com.sun.tools.corba.se.idl.UnionBranch;
/*      */ import com.sun.tools.corba.se.idl.UnionEntry;
/*      */ import com.sun.tools.corba.se.idl.constExpr.EvaluationException;
/*      */ import com.sun.tools.corba.se.idl.constExpr.Expression;
/*      */ import java.io.PrintWriter;
/*      */ import java.util.Enumeration;
/*      */ import java.util.Hashtable;
/*      */ import java.util.Vector;
/*      */ 
/*      */ public class UnionGen
/*      */   implements com.sun.tools.corba.se.idl.UnionGen, JavaGenerator
/*      */ {
/* 1068 */   protected Hashtable symbolTable = null;
/* 1069 */   protected UnionEntry u = null;
/* 1070 */   protected PrintWriter stream = null;
/* 1071 */   protected SymtabEntry utype = null;
/*      */   protected boolean unionIsEnum;
/* 1073 */   protected String typePackage = "";
/*      */ 
/*      */   public void generate(Hashtable paramHashtable, UnionEntry paramUnionEntry, PrintWriter paramPrintWriter)
/*      */   {
/*   84 */     this.symbolTable = paramHashtable;
/*   85 */     this.u = paramUnionEntry;
/*   86 */     init();
/*      */ 
/*   88 */     openStream();
/*   89 */     if (this.stream == null)
/*   90 */       return;
/*   91 */     generateHelper();
/*   92 */     generateHolder();
/*   93 */     writeHeading();
/*   94 */     writeBody();
/*   95 */     writeClosing();
/*   96 */     closeStream();
/*   97 */     generateContainedTypes();
/*      */   }
/*      */ 
/*      */   protected void init()
/*      */   {
/*  105 */     this.utype = Util.typeOf(this.u.type());
/*  106 */     this.unionIsEnum = (this.utype instanceof EnumEntry);
/*      */   }
/*      */ 
/*      */   protected void openStream()
/*      */   {
/*  114 */     this.stream = Util.stream(this.u, ".java");
/*      */   }
/*      */ 
/*      */   protected void generateHelper()
/*      */   {
/*  122 */     ((Factories)Compile.compiler.factories()).helper().generate(this.symbolTable, this.u);
/*      */   }
/*      */ 
/*      */   protected void generateHolder()
/*      */   {
/*  130 */     ((Factories)Compile.compiler.factories()).holder().generate(this.symbolTable, this.u);
/*      */   }
/*      */ 
/*      */   protected void writeHeading()
/*      */   {
/*  139 */     if (this.unionIsEnum)
/*  140 */       this.typePackage = (Util.javaQualifiedName(this.utype) + '.');
/*      */     else {
/*  142 */       this.typePackage = "";
/*      */     }
/*  144 */     Util.writePackage(this.stream, this.u);
/*  145 */     Util.writeProlog(this.stream, ((GenFileStream)this.stream).name());
/*      */ 
/*  147 */     String str = this.u.name();
/*  148 */     this.stream.println("public final class " + this.u.name() + " implements org.omg.CORBA.portable.IDLEntity");
/*  149 */     this.stream.println("{");
/*      */   }
/*      */ 
/*      */   protected void writeBody()
/*      */   {
/*  158 */     int i = this.u.branches().size() + 1;
/*  159 */     Enumeration localEnumeration = this.u.branches().elements();
/*  160 */     int j = 0;
/*      */     UnionBranch localUnionBranch;
/*  161 */     while (localEnumeration.hasMoreElements())
/*      */     {
/*  163 */       localUnionBranch = (UnionBranch)localEnumeration.nextElement();
/*  164 */       Util.fillInfo(localUnionBranch.typedef);
/*      */ 
/*  167 */       this.stream.println("  private " + Util.javaName(localUnionBranch.typedef) + " ___" + localUnionBranch.typedef.name() + ";");
/*  168 */       j++;
/*      */     }
/*  170 */     this.stream.println("  private " + Util.javaName(this.utype) + " __discriminator;");
/*  171 */     this.stream.println("  private boolean __uninitialized = true;");
/*      */ 
/*  174 */     this.stream.println();
/*  175 */     this.stream.println("  public " + this.u.name() + " ()");
/*  176 */     this.stream.println("  {");
/*  177 */     this.stream.println("  }");
/*      */ 
/*  180 */     this.stream.println();
/*  181 */     this.stream.println("  public " + Util.javaName(this.utype) + " " + safeName(this.u, "discriminator") + " ()");
/*  182 */     this.stream.println("  {");
/*  183 */     this.stream.println("    if (__uninitialized)");
/*  184 */     this.stream.println("      throw new org.omg.CORBA.BAD_OPERATION ();");
/*  185 */     this.stream.println("    return __discriminator;");
/*  186 */     this.stream.println("  }");
/*      */ 
/*  192 */     localEnumeration = this.u.branches().elements();
/*  193 */     j = 0;
/*  194 */     while (localEnumeration.hasMoreElements())
/*      */     {
/*  196 */       localUnionBranch = (UnionBranch)localEnumeration.nextElement();
/*  197 */       writeBranchMethods(this.stream, this.u, localUnionBranch, j++);
/*      */     }
/*  199 */     if ((this.u.defaultBranch() == null) && (!coversAll(this.u)))
/*      */     {
/*  201 */       this.stream.println();
/*  202 */       this.stream.println("  public void _default ()");
/*  203 */       this.stream.println("  {");
/*  204 */       this.stream.println("    __discriminator = " + defaultDiscriminator(this.u) + ';');
/*  205 */       this.stream.println("    __uninitialized = false;");
/*  206 */       this.stream.println("  }");
/*      */ 
/*  208 */       this.stream.println();
/*  209 */       this.stream.println("  public void _default (" + Util.javaName(this.utype) + " discriminator)");
/*      */ 
/*  211 */       this.stream.println("  {");
/*  212 */       this.stream.println("    verifyDefault( discriminator ) ;");
/*  213 */       this.stream.println("    __discriminator = discriminator ;");
/*  214 */       this.stream.println("    __uninitialized = false;");
/*  215 */       this.stream.println("  }");
/*      */ 
/*  217 */       writeVerifyDefault();
/*      */     }
/*  219 */     this.stream.println();
/*      */   }
/*      */ 
/*      */   protected void writeClosing()
/*      */   {
/*  227 */     this.stream.println("} // class " + this.u.name());
/*      */   }
/*      */ 
/*      */   protected void closeStream()
/*      */   {
/*  235 */     this.stream.close();
/*      */   }
/*      */ 
/*      */   protected void generateContainedTypes()
/*      */   {
/*  243 */     Enumeration localEnumeration = this.u.contained().elements();
/*  244 */     while (localEnumeration.hasMoreElements())
/*      */     {
/*  246 */       SymtabEntry localSymtabEntry = (SymtabEntry)localEnumeration.nextElement();
/*      */ 
/*  252 */       if (!(localSymtabEntry instanceof SequenceEntry))
/*  253 */         localSymtabEntry.generate(this.symbolTable, this.stream);
/*      */     }
/*      */   }
/*      */ 
/*      */   private void writeVerifyDefault()
/*      */   {
/*  259 */     Vector localVector = vectorizeLabels(this.u.branches(), true);
/*      */ 
/*  261 */     if (Util.javaName(this.utype).equals("boolean")) {
/*  262 */       this.stream.println("");
/*  263 */       this.stream.println("  private void verifyDefault (boolean discriminator)");
/*  264 */       this.stream.println("  {");
/*  265 */       if (localVector.contains("true"))
/*  266 */         this.stream.println("    if ( discriminator )");
/*      */       else
/*  268 */         this.stream.println("    if ( !discriminator )");
/*  269 */       this.stream.println("        throw new org.omg.CORBA.BAD_OPERATION();");
/*  270 */       this.stream.println("  }");
/*  271 */       return;
/*      */     }
/*      */ 
/*  274 */     this.stream.println("");
/*  275 */     this.stream.println("  private void verifyDefault( " + Util.javaName(this.utype) + " value )");
/*      */ 
/*  277 */     this.stream.println("  {");
/*      */ 
/*  279 */     if (this.unionIsEnum)
/*  280 */       this.stream.println("    switch (value.value()) {");
/*      */     else {
/*  282 */       this.stream.println("    switch (value) {");
/*      */     }
/*  284 */     Enumeration localEnumeration = localVector.elements();
/*  285 */     while (localEnumeration.hasMoreElements()) {
/*  286 */       String str = (String)localEnumeration.nextElement();
/*  287 */       this.stream.println("      case " + str + ":");
/*      */     }
/*      */ 
/*  290 */     this.stream.println("        throw new org.omg.CORBA.BAD_OPERATION() ;");
/*  291 */     this.stream.println("");
/*  292 */     this.stream.println("      default:");
/*  293 */     this.stream.println("        return;");
/*  294 */     this.stream.println("    }");
/*  295 */     this.stream.println("  }");
/*      */   }
/*      */ 
/*      */   private String defaultDiscriminator(UnionEntry paramUnionEntry)
/*      */   {
/*  300 */     Vector localVector1 = vectorizeLabels(paramUnionEntry.branches(), false);
/*  301 */     Object localObject1 = null;
/*  302 */     SymtabEntry localSymtabEntry = Util.typeOf(paramUnionEntry.type());
/*  303 */     if (((localSymtabEntry instanceof PrimitiveEntry)) && (localSymtabEntry.name().equals("boolean")))
/*      */     {
/*  307 */       if (localVector1.contains("true"))
/*  308 */         localObject1 = "false";
/*      */       else
/*  310 */         localObject1 = "true";
/*      */     }
/*      */     else
/*      */     {
/*      */       Object localObject2;
/*  311 */       if (localSymtabEntry.name().equals("char"))
/*      */       {
/*  314 */         int i = 0;
/*  315 */         localObject2 = "'\\u0000'";
/*  316 */         while ((i != 65535) && (localVector1.contains(localObject2))) {
/*  317 */           i++; if (i / 16 == 0)
/*  318 */             localObject2 = "'\\u000" + i + "'";
/*  319 */           else if (i / 256 == 0)
/*  320 */             localObject2 = "\\u00" + i + "'";
/*  321 */           else if (i / 4096 == 0)
/*  322 */             localObject2 = "\\u0" + i + "'";
/*      */           else
/*  324 */             localObject2 = "\\u" + i + "'"; 
/*      */         }
/*  325 */         localObject1 = localObject2;
/*  326 */       } else if ((localSymtabEntry instanceof EnumEntry)) {
/*  327 */         Enumeration localEnumeration = localVector1.elements();
/*  328 */         localObject2 = (EnumEntry)localSymtabEntry;
/*  329 */         Vector localVector2 = (Vector)((EnumEntry)localObject2).elements().clone();
/*      */ 
/*  332 */         while (localEnumeration.hasMoreElements()) {
/*  333 */           localVector2.removeElement(localEnumeration.nextElement());
/*      */         }
/*      */ 
/*  339 */         if (localVector2.size() == 0)
/*  340 */           localObject1 = this.typePackage + (String)((EnumEntry)localObject2).elements().lastElement();
/*      */         else
/*  342 */           localObject1 = this.typePackage + (String)localVector2.firstElement();
/*      */       }
/*      */       else
/*      */       {
/*      */         int j;
/*  343 */         if (localSymtabEntry.name().equals("octet")) {
/*  344 */           j = -128;
/*  345 */           while ((j != 127) && (localVector1.contains(Integer.toString(j))))
/*  346 */             j = (short)(j + 1);
/*  347 */           localObject1 = Integer.toString(j);
/*  348 */         } else if (localSymtabEntry.name().equals("short")) {
/*  349 */           j = -32768;
/*  350 */           while ((j != 32767) && (localVector1.contains(Integer.toString(j))))
/*  351 */             j = (short)(j + 1);
/*  352 */           localObject1 = Integer.toString(j);
/*  353 */         } else if (localSymtabEntry.name().equals("long")) {
/*  354 */           j = -2147483648;
/*  355 */           while ((j != 2147483647) && (localVector1.contains(Integer.toString(j))))
/*  356 */             j++;
/*  357 */           localObject1 = Integer.toString(j);
/*  358 */         } else if (localSymtabEntry.name().equals("long long")) {
/*  359 */           long l1 = -9223372036854775808L;
/*  360 */           while ((l1 != 9223372036854775807L) && (localVector1.contains(Long.toString(l1))))
/*  361 */             l1 += 1L;
/*  362 */           localObject1 = Long.toString(l1);
/*      */         }
/*      */         else
/*      */         {
/*      */           int k;
/*  363 */           if (localSymtabEntry.name().equals("unsigned short")) {
/*  364 */             k = 0;
/*  365 */             while ((k != 32767) && (localVector1.contains(Integer.toString(k))))
/*  366 */               k = (short)(k + 1);
/*  367 */             localObject1 = Integer.toString(k);
/*  368 */           } else if (localSymtabEntry.name().equals("unsigned long")) {
/*  369 */             k = 0;
/*  370 */             while ((k != 2147483647) && (localVector1.contains(Integer.toString(k))))
/*  371 */               k++;
/*  372 */             localObject1 = Integer.toString(k);
/*  373 */           } else if (localSymtabEntry.name().equals("unsigned long long")) {
/*  374 */             long l2 = 0L;
/*  375 */             while ((l2 != 9223372036854775807L) && (localVector1.contains(Long.toString(l2))))
/*  376 */               l2 += 1L;
/*  377 */             localObject1 = Long.toString(l2);
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/*  380 */     return localObject1;
/*      */   }
/*      */ 
/*      */   private Vector vectorizeLabels(Vector paramVector, boolean paramBoolean)
/*      */   {
/*  388 */     Vector localVector = new Vector();
/*  389 */     Enumeration localEnumeration1 = paramVector.elements();
/*  390 */     while (localEnumeration1.hasMoreElements())
/*      */     {
/*  392 */       UnionBranch localUnionBranch = (UnionBranch)localEnumeration1.nextElement();
/*  393 */       Enumeration localEnumeration2 = localUnionBranch.labels.elements();
/*  394 */       while (localEnumeration2.hasMoreElements())
/*      */       {
/*  396 */         Expression localExpression = (Expression)localEnumeration2.nextElement();
/*      */         String str;
/*  399 */         if (this.unionIsEnum) {
/*  400 */           if (paramBoolean)
/*  401 */             str = this.typePackage + "_" + Util.parseExpression(localExpression);
/*      */           else
/*  403 */             str = this.typePackage + Util.parseExpression(localExpression);
/*      */         }
/*  405 */         else str = Util.parseExpression(localExpression);
/*      */ 
/*  407 */         localVector.addElement(str);
/*      */       }
/*      */     }
/*  410 */     return localVector;
/*      */   }
/*      */ 
/*      */   private String safeName(UnionEntry paramUnionEntry, String paramString)
/*      */   {
/*  418 */     Enumeration localEnumeration = paramUnionEntry.branches().elements();
/*  419 */     while (localEnumeration.hasMoreElements()) {
/*  420 */       if (((UnionBranch)localEnumeration.nextElement()).typedef.name().equals(paramString))
/*      */       {
/*  422 */         paramString = '_' + paramString;
/*      */       }
/*      */     }
/*  425 */     return paramString;
/*      */   }
/*      */ 
/*      */   private boolean coversAll(UnionEntry paramUnionEntry)
/*      */   {
/*  438 */     SymtabEntry localSymtabEntry = Util.typeOf(paramUnionEntry.type());
/*      */ 
/*  440 */     boolean bool = false;
/*  441 */     if (localSymtabEntry.name().equals("boolean")) {
/*  442 */       if (paramUnionEntry.branches().size() == 2)
/*  443 */         bool = true;
/*  444 */     } else if ((localSymtabEntry instanceof EnumEntry)) {
/*  445 */       Vector localVector = vectorizeLabels(paramUnionEntry.branches(), true);
/*  446 */       if (localVector.size() == ((EnumEntry)localSymtabEntry).elements().size()) {
/*  447 */         bool = true;
/*      */       }
/*      */     }
/*  450 */     return bool;
/*      */   }
/*      */ 
/*      */   private void writeBranchMethods(PrintWriter paramPrintWriter, UnionEntry paramUnionEntry, UnionBranch paramUnionBranch, int paramInt)
/*      */   {
/*  459 */     paramPrintWriter.println();
/*      */ 
/*  462 */     paramPrintWriter.println("  public " + Util.javaName(paramUnionBranch.typedef) + " " + paramUnionBranch.typedef.name() + " ()");
/*  463 */     paramPrintWriter.println("  {");
/*  464 */     paramPrintWriter.println("    if (__uninitialized)");
/*  465 */     paramPrintWriter.println("      throw new org.omg.CORBA.BAD_OPERATION ();");
/*  466 */     paramPrintWriter.println("    verify" + paramUnionBranch.typedef.name() + " (__discriminator);");
/*  467 */     paramPrintWriter.println("    return ___" + paramUnionBranch.typedef.name() + ";");
/*  468 */     paramPrintWriter.println("  }");
/*      */ 
/*  471 */     paramPrintWriter.println();
/*      */ 
/*  474 */     paramPrintWriter.println("  public void " + paramUnionBranch.typedef.name() + " (" + Util.javaName(paramUnionBranch.typedef) + " value)");
/*  475 */     paramPrintWriter.println("  {");
/*  476 */     if (paramUnionBranch.labels.size() == 0)
/*      */     {
/*  479 */       paramPrintWriter.println("    __discriminator = " + defaultDiscriminator(paramUnionEntry) + ";");
/*      */     }
/*  484 */     else if (this.unionIsEnum)
/*  485 */       paramPrintWriter.println("    __discriminator = " + this.typePackage + Util.parseExpression((Expression)paramUnionBranch.labels.firstElement()) + ";");
/*      */     else {
/*  487 */       paramPrintWriter.println("    __discriminator = " + cast((Expression)paramUnionBranch.labels.firstElement(), paramUnionEntry.type()) + ";");
/*      */     }
/*  489 */     paramPrintWriter.println("    ___" + paramUnionBranch.typedef.name() + " = value;");
/*  490 */     paramPrintWriter.println("    __uninitialized = false;");
/*  491 */     paramPrintWriter.println("  }");
/*      */ 
/*  493 */     SymtabEntry localSymtabEntry = Util.typeOf(paramUnionEntry.type());
/*      */ 
/*  497 */     if ((paramUnionBranch.labels.size() > 0) || (paramUnionBranch.isDefault))
/*      */     {
/*  499 */       paramPrintWriter.println();
/*      */ 
/*  502 */       paramPrintWriter.println("  public void " + paramUnionBranch.typedef.name() + " (" + Util.javaName(localSymtabEntry) + " discriminator, " + Util.javaName(paramUnionBranch.typedef) + " value)");
/*  503 */       paramPrintWriter.println("  {");
/*  504 */       paramPrintWriter.println("    verify" + paramUnionBranch.typedef.name() + " (discriminator);");
/*  505 */       paramPrintWriter.println("    __discriminator = discriminator;");
/*  506 */       paramPrintWriter.println("    ___" + paramUnionBranch.typedef.name() + " = value;");
/*  507 */       paramPrintWriter.println("    __uninitialized = false;");
/*  508 */       paramPrintWriter.println("  }");
/*      */     }
/*      */ 
/*  512 */     paramPrintWriter.println();
/*  513 */     paramPrintWriter.println("  private void verify" + paramUnionBranch.typedef.name() + " (" + Util.javaName(localSymtabEntry) + " discriminator)");
/*  514 */     paramPrintWriter.println("  {");
/*      */ 
/*  516 */     int i = 1;
/*      */ 
/*  518 */     if ((!paramUnionBranch.isDefault) || (paramUnionEntry.branches().size() != 1))
/*      */     {
/*  525 */       paramPrintWriter.print("    if (");
/*      */       Enumeration localEnumeration1;
/*      */       Object localObject;
/*  526 */       if (paramUnionBranch.isDefault)
/*      */       {
/*  528 */         localEnumeration1 = paramUnionEntry.branches().elements();
/*  529 */         while (localEnumeration1.hasMoreElements())
/*      */         {
/*  531 */           localObject = (UnionBranch)localEnumeration1.nextElement();
/*  532 */           if (localObject != paramUnionBranch)
/*      */           {
/*  534 */             Enumeration localEnumeration2 = ((UnionBranch)localObject).labels.elements();
/*  535 */             while (localEnumeration2.hasMoreElements())
/*      */             {
/*  537 */               Expression localExpression = (Expression)localEnumeration2.nextElement();
/*  538 */               if (i == 0)
/*  539 */                 paramPrintWriter.print(" || ");
/*  540 */               if (this.unionIsEnum)
/*  541 */                 paramPrintWriter.print("discriminator == " + this.typePackage + Util.parseExpression(localExpression));
/*      */               else
/*  543 */                 paramPrintWriter.print("discriminator == " + Util.parseExpression(localExpression));
/*  544 */               i = 0;
/*      */             }
/*      */           }
/*      */         }
/*      */       }
/*      */       else
/*      */       {
/*  551 */         localEnumeration1 = paramUnionBranch.labels.elements();
/*  552 */         while (localEnumeration1.hasMoreElements())
/*      */         {
/*  554 */           localObject = (Expression)localEnumeration1.nextElement();
/*  555 */           if (i == 0)
/*  556 */             paramPrintWriter.print(" && ");
/*  557 */           if (this.unionIsEnum)
/*  558 */             paramPrintWriter.print("discriminator != " + this.typePackage + Util.parseExpression((Expression)localObject));
/*      */           else
/*  560 */             paramPrintWriter.print("discriminator != " + Util.parseExpression((Expression)localObject));
/*  561 */           i = 0;
/*      */         }
/*      */       }
/*  564 */       paramPrintWriter.println(")");
/*  565 */       paramPrintWriter.println("      throw new org.omg.CORBA.BAD_OPERATION ();");
/*      */     }
/*  567 */     paramPrintWriter.println("  }");
/*      */   }
/*      */ 
/*      */   private int unionLabelSize(UnionEntry paramUnionEntry)
/*      */   {
/*  583 */     int i = 0;
/*  584 */     Vector localVector = paramUnionEntry.branches();
/*  585 */     for (int j = 0; j < localVector.size(); j++) {
/*  586 */       UnionBranch localUnionBranch = (UnionBranch)localVector.get(j);
/*  587 */       int k = localUnionBranch.labels.size();
/*  588 */       i += (k == 0 ? 1 : k);
/*      */     }
/*  590 */     return i;
/*      */   }
/*      */ 
/*      */   public int helperType(int paramInt, String paramString1, TCOffsets paramTCOffsets, String paramString2, SymtabEntry paramSymtabEntry, PrintWriter paramPrintWriter)
/*      */   {
/*  596 */     TCOffsets localTCOffsets = new TCOffsets();
/*  597 */     UnionEntry localUnionEntry = (UnionEntry)paramSymtabEntry;
/*  598 */     String str1 = "_disTypeCode" + paramInt;
/*  599 */     String str2 = "_members" + paramInt;
/*      */ 
/*  602 */     paramPrintWriter.println(paramString1 + "org.omg.CORBA.TypeCode " + str1 + ';');
/*  603 */     paramInt = ((JavaGenerator)localUnionEntry.type().generator()).type(paramInt + 1, paramString1, localTCOffsets, str1, localUnionEntry
/*  604 */       .type(), paramPrintWriter);
/*  605 */     paramTCOffsets.bumpCurrentOffset(localTCOffsets.currentOffset());
/*      */ 
/*  607 */     paramPrintWriter.println(paramString1 + "org.omg.CORBA.UnionMember[] " + str2 + " = new org.omg.CORBA.UnionMember [" + 
/*  608 */       unionLabelSize(localUnionEntry) + 
/*  608 */       "];");
/*  609 */     String str3 = "_tcOf" + str2;
/*  610 */     String str4 = "_anyOf" + str2;
/*  611 */     paramPrintWriter.println(paramString1 + "org.omg.CORBA.TypeCode " + str3 + ';');
/*  612 */     paramPrintWriter.println(paramString1 + "org.omg.CORBA.Any " + str4 + ';');
/*      */ 
/*  614 */     localTCOffsets = new TCOffsets();
/*  615 */     localTCOffsets.set(paramSymtabEntry);
/*  616 */     int i = localTCOffsets.currentOffset();
/*  617 */     for (int j = 0; j < localUnionEntry.branches().size(); j++) {
/*  618 */       UnionBranch localUnionBranch = (UnionBranch)localUnionEntry.branches().elementAt(j);
/*  619 */       TypedefEntry localTypedefEntry = localUnionBranch.typedef;
/*  620 */       Vector localVector = localUnionBranch.labels;
/*  621 */       String str5 = Util.stripLeadingUnderscores(localTypedefEntry.name());
/*      */       Object localObject;
/*  623 */       if (localVector.size() == 0) {
/*  624 */         paramPrintWriter.println();
/*  625 */         paramPrintWriter.println(paramString1 + "// Branch for " + str5 + " (Default case)");
/*      */ 
/*  627 */         localObject = Util.typeOf(localUnionEntry.type());
/*  628 */         paramPrintWriter.println(paramString1 + str4 + " = org.omg.CORBA.ORB.init ().create_any ();");
/*      */ 
/*  630 */         paramPrintWriter.println(paramString1 + str4 + ".insert_octet ((byte)0); // default member label");
/*      */ 
/*  633 */         localTCOffsets.bumpCurrentOffset(4);
/*  634 */         paramInt = ((JavaGenerator)localTypedefEntry.generator()).type(paramInt, paramString1, localTCOffsets, str3, localTypedefEntry, paramPrintWriter);
/*  635 */         int k = localTCOffsets.currentOffset();
/*  636 */         localTCOffsets = new TCOffsets();
/*  637 */         localTCOffsets.set(paramSymtabEntry);
/*  638 */         localTCOffsets.bumpCurrentOffset(k - i);
/*      */ 
/*  641 */         paramPrintWriter.println(paramString1 + str2 + '[' + j + "] = new org.omg.CORBA.UnionMember (");
/*  642 */         paramPrintWriter.println(paramString1 + "  \"" + str5 + "\",");
/*  643 */         paramPrintWriter.println(paramString1 + "  " + str4 + ',');
/*  644 */         paramPrintWriter.println(paramString1 + "  " + str3 + ',');
/*  645 */         paramPrintWriter.println(paramString1 + "  null);");
/*      */       } else {
/*  647 */         localObject = localVector.elements();
/*  648 */         while (((Enumeration)localObject).hasMoreElements()) {
/*  649 */           Expression localExpression = (Expression)((Enumeration)localObject).nextElement();
/*  650 */           String str6 = Util.parseExpression(localExpression);
/*      */ 
/*  652 */           paramPrintWriter.println();
/*  653 */           paramPrintWriter.println(paramString1 + "// Branch for " + str5 + " (case label " + str6 + ")");
/*      */ 
/*  656 */           SymtabEntry localSymtabEntry = Util.typeOf(localUnionEntry.type());
/*      */ 
/*  659 */           paramPrintWriter.println(paramString1 + str4 + " = org.omg.CORBA.ORB.init ().create_any ();");
/*      */ 
/*  661 */           if ((localSymtabEntry instanceof PrimitiveEntry)) {
/*  662 */             paramPrintWriter.println(paramString1 + str4 + ".insert_" + 
/*  663 */               Util.collapseName(localSymtabEntry
/*  663 */               .name()) + " ((" + Util.javaName(localSymtabEntry) + ')' + str6 + ");");
/*      */           }
/*      */           else {
/*  666 */             String str7 = Util.javaName(localSymtabEntry);
/*  667 */             paramPrintWriter.println(paramString1 + Util.helperName(localSymtabEntry, false) + ".insert (" + str4 + ", " + str7 + '.' + str6 + ");");
/*      */           }
/*      */ 
/*  672 */           localTCOffsets.bumpCurrentOffset(4);
/*  673 */           paramInt = ((JavaGenerator)localTypedefEntry.generator()).type(paramInt, paramString1, localTCOffsets, str3, localTypedefEntry, paramPrintWriter);
/*  674 */           int m = localTCOffsets.currentOffset();
/*  675 */           localTCOffsets = new TCOffsets();
/*  676 */           localTCOffsets.set(paramSymtabEntry);
/*  677 */           localTCOffsets.bumpCurrentOffset(m - i);
/*      */ 
/*  680 */           paramPrintWriter.println(paramString1 + str2 + '[' + j + "] = new org.omg.CORBA.UnionMember (");
/*  681 */           paramPrintWriter.println(paramString1 + "  \"" + str5 + "\",");
/*  682 */           paramPrintWriter.println(paramString1 + "  " + str4 + ',');
/*  683 */           paramPrintWriter.println(paramString1 + "  " + str3 + ',');
/*  684 */           paramPrintWriter.println(paramString1 + "  null);");
/*      */         }
/*      */       }
/*      */     }
/*      */ 
/*  689 */     paramTCOffsets.bumpCurrentOffset(localTCOffsets.currentOffset());
/*      */ 
/*  692 */     paramPrintWriter.println(paramString1 + paramString2 + " = org.omg.CORBA.ORB.init ().create_union_tc (" + 
/*  693 */       Util.helperName(localUnionEntry, true) + 
/*  693 */       ".id (), \"" + paramSymtabEntry.name() + "\", " + str1 + ", " + str2 + ");");
/*      */ 
/*  695 */     return paramInt;
/*      */   }
/*      */ 
/*      */   public int type(int paramInt, String paramString1, TCOffsets paramTCOffsets, String paramString2, SymtabEntry paramSymtabEntry, PrintWriter paramPrintWriter)
/*      */   {
/*  701 */     paramPrintWriter.println(paramString1 + paramString2 + " = " + Util.helperName(paramSymtabEntry, true) + ".type ();");
/*  702 */     return paramInt;
/*      */   }
/*      */ 
/*      */   public void helperRead(String paramString, SymtabEntry paramSymtabEntry, PrintWriter paramPrintWriter)
/*      */   {
/*  707 */     paramPrintWriter.println("    " + paramString + " value = new " + paramString + " ();");
/*  708 */     read(0, "    ", "value", paramSymtabEntry, paramPrintWriter);
/*  709 */     paramPrintWriter.println("    return value;");
/*      */   }
/*      */ 
/*      */   public void helperWrite(SymtabEntry paramSymtabEntry, PrintWriter paramPrintWriter)
/*      */   {
/*  714 */     write(0, "    ", "value", paramSymtabEntry, paramPrintWriter);
/*      */   }
/*      */ 
/*      */   public int read(int paramInt, String paramString1, String paramString2, SymtabEntry paramSymtabEntry, PrintWriter paramPrintWriter)
/*      */   {
/*  720 */     UnionEntry localUnionEntry = (UnionEntry)paramSymtabEntry;
/*  721 */     String str = "_dis" + paramInt++;
/*  722 */     SymtabEntry localSymtabEntry = Util.typeOf(localUnionEntry.type());
/*  723 */     Util.writeInitializer(paramString1, str, "", localSymtabEntry, paramPrintWriter);
/*      */ 
/*  725 */     if ((localSymtabEntry instanceof PrimitiveEntry))
/*  726 */       paramInt = ((JavaGenerator)localSymtabEntry.generator()).read(paramInt, paramString1, str, localSymtabEntry, paramPrintWriter);
/*      */     else {
/*  728 */       paramPrintWriter.println(paramString1 + str + " = " + Util.helperName(localSymtabEntry, true) + ".read (istream);");
/*      */     }
/*  730 */     if (localSymtabEntry.name().equals("boolean"))
/*  731 */       paramInt = readBoolean(str, paramInt, paramString1, paramString2, localUnionEntry, paramPrintWriter);
/*      */     else {
/*  733 */       paramInt = readNonBoolean(str, paramInt, paramString1, paramString2, localUnionEntry, paramPrintWriter);
/*      */     }
/*  735 */     return paramInt;
/*      */   }
/*      */ 
/*      */   private int readBoolean(String paramString1, int paramInt, String paramString2, String paramString3, UnionEntry paramUnionEntry, PrintWriter paramPrintWriter)
/*      */   {
/*  741 */     Object localObject1 = (UnionBranch)paramUnionEntry.branches().firstElement();
/*      */     Object localObject2;
/*  744 */     if (paramUnionEntry.branches().size() == 2)
/*  745 */       localObject2 = (UnionBranch)paramUnionEntry.branches().lastElement();
/*      */     else {
/*  747 */       localObject2 = null;
/*      */     }
/*  749 */     boolean bool = false;
/*  750 */     int i = 0;
/*      */     try {
/*  752 */       if ((paramUnionEntry.branches().size() == 1) && (
/*  753 */         (paramUnionEntry
/*  753 */         .defaultBranch() != null) || (((UnionBranch)localObject1).labels.size() == 2))) {
/*  754 */         i = 1;
/*      */       } else {
/*  756 */         Expression localExpression = (Expression)((UnionBranch)localObject1).labels.firstElement();
/*  757 */         Boolean localBoolean = (Boolean)localExpression.evaluate();
/*  758 */         bool = localBoolean.booleanValue();
/*      */       }
/*      */     }
/*      */     catch (EvaluationException localEvaluationException)
/*      */     {
/*      */     }
/*  764 */     if (i != 0)
/*      */     {
/*  767 */       paramInt = readBranch(paramInt, paramString2, ((UnionBranch)localObject1).typedef.name(), "", ((UnionBranch)localObject1).typedef, paramPrintWriter);
/*      */     }
/*      */     else {
/*  770 */       if (!bool) {
/*  771 */         Object localObject3 = localObject1;
/*  772 */         localObject1 = localObject2;
/*  773 */         localObject2 = localObject3;
/*      */       }
/*      */ 
/*  776 */       paramPrintWriter.println(paramString2 + "if (" + paramString1 + ')');
/*      */ 
/*  778 */       if (localObject1 == null) {
/*  779 */         paramPrintWriter.println(paramString2 + "  value._default(" + paramString1 + ");");
/*      */       } else {
/*  781 */         paramPrintWriter.println(paramString2 + '{');
/*  782 */         paramInt = readBranch(paramInt, paramString2 + "  ", ((UnionBranch)localObject1).typedef.name(), paramString1, ((UnionBranch)localObject1).typedef, paramPrintWriter);
/*      */ 
/*  784 */         paramPrintWriter.println(paramString2 + '}');
/*      */       }
/*      */ 
/*  787 */       paramPrintWriter.println(paramString2 + "else");
/*      */ 
/*  789 */       if (localObject2 == null) {
/*  790 */         paramPrintWriter.println(paramString2 + "  value._default(" + paramString1 + ");");
/*      */       } else {
/*  792 */         paramPrintWriter.println(paramString2 + '{');
/*  793 */         paramInt = readBranch(paramInt, paramString2 + "  ", ((UnionBranch)localObject2).typedef.name(), paramString1, ((UnionBranch)localObject2).typedef, paramPrintWriter);
/*      */ 
/*  795 */         paramPrintWriter.println(paramString2 + '}');
/*      */       }
/*      */     }
/*      */ 
/*  799 */     return paramInt;
/*      */   }
/*      */ 
/*      */   private int readNonBoolean(String paramString1, int paramInt, String paramString2, String paramString3, UnionEntry paramUnionEntry, PrintWriter paramPrintWriter)
/*      */   {
/*  805 */     SymtabEntry localSymtabEntry = Util.typeOf(paramUnionEntry.type());
/*      */ 
/*  807 */     if ((localSymtabEntry instanceof EnumEntry))
/*  808 */       paramPrintWriter.println(paramString2 + "switch (" + paramString1 + ".value ())");
/*      */     else {
/*  810 */       paramPrintWriter.println(paramString2 + "switch (" + paramString1 + ')');
/*      */     }
/*  812 */     paramPrintWriter.println(paramString2 + '{');
/*  813 */     String str1 = Util.javaQualifiedName(localSymtabEntry) + '.';
/*      */ 
/*  815 */     Enumeration localEnumeration1 = paramUnionEntry.branches().elements();
/*  816 */     while (localEnumeration1.hasMoreElements()) {
/*  817 */       UnionBranch localUnionBranch = (UnionBranch)localEnumeration1.nextElement();
/*  818 */       Enumeration localEnumeration2 = localUnionBranch.labels.elements();
/*      */ 
/*  820 */       while (localEnumeration2.hasMoreElements()) {
/*  821 */         Expression localExpression = (Expression)localEnumeration2.nextElement();
/*      */ 
/*  823 */         if ((localSymtabEntry instanceof EnumEntry)) {
/*  824 */           String str2 = Util.parseExpression(localExpression);
/*  825 */           paramPrintWriter.println(paramString2 + "  case " + str1 + '_' + str2 + ':');
/*      */         } else {
/*  827 */           paramPrintWriter.println(paramString2 + "  case " + cast(localExpression, localSymtabEntry) + ':');
/*      */         }
/*      */       }
/*  830 */       if (!localUnionBranch.typedef.equals(paramUnionEntry.defaultBranch())) {
/*  831 */         paramInt = readBranch(paramInt, paramString2 + "    ", localUnionBranch.typedef.name(), localUnionBranch.labels
/*  832 */           .size() > 1 ? paramString1 : "", localUnionBranch.typedef, paramPrintWriter);
/*      */ 
/*  834 */         paramPrintWriter.println(paramString2 + "    break;");
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/*  840 */     if (!coversAll(paramUnionEntry)) {
/*  841 */       paramPrintWriter.println(paramString2 + "  default:");
/*      */ 
/*  843 */       if (paramUnionEntry.defaultBranch() == null)
/*      */       {
/*  846 */         paramPrintWriter.println(paramString2 + "    value._default( " + paramString1 + " ) ;");
/*      */       }
/*  848 */       else paramInt = readBranch(paramInt, paramString2 + "    ", paramUnionEntry.defaultBranch().name(), paramString1, paramUnionEntry
/*  849 */           .defaultBranch(), paramPrintWriter);
/*      */ 
/*  852 */       paramPrintWriter.println(paramString2 + "    break;");
/*      */     }
/*      */ 
/*  855 */     paramPrintWriter.println(paramString2 + '}');
/*      */ 
/*  857 */     return paramInt;
/*      */   }
/*      */ 
/*      */   private int readBranch(int paramInt, String paramString1, String paramString2, String paramString3, TypedefEntry paramTypedefEntry, PrintWriter paramPrintWriter)
/*      */   {
/*  862 */     SymtabEntry localSymtabEntry = paramTypedefEntry.type();
/*  863 */     Util.writeInitializer(paramString1, '_' + paramString2, "", paramTypedefEntry, paramPrintWriter);
/*      */ 
/*  865 */     if ((!paramTypedefEntry.arrayInfo().isEmpty()) || ((localSymtabEntry instanceof SequenceEntry)) || ((localSymtabEntry instanceof PrimitiveEntry)) || ((localSymtabEntry instanceof StringEntry)))
/*      */     {
/*  869 */       paramInt = ((JavaGenerator)paramTypedefEntry.generator()).read(paramInt, paramString1, '_' + paramString2, paramTypedefEntry, paramPrintWriter);
/*      */     }
/*  871 */     else paramPrintWriter.println(paramString1 + '_' + paramString2 + " = " + Util.helperName(localSymtabEntry, true) + ".read (istream);");
/*      */ 
/*  874 */     paramPrintWriter.print(paramString1 + "value." + paramString2 + " (");
/*  875 */     if (paramString3 == "")
/*  876 */       paramPrintWriter.println("_" + paramString2 + ");");
/*      */     else {
/*  878 */       paramPrintWriter.println(paramString3 + ", " + "_" + paramString2 + ");");
/*      */     }
/*  880 */     return paramInt;
/*      */   }
/*      */ 
/*      */   public int write(int paramInt, String paramString1, String paramString2, SymtabEntry paramSymtabEntry, PrintWriter paramPrintWriter)
/*      */   {
/*  888 */     UnionEntry localUnionEntry = (UnionEntry)paramSymtabEntry;
/*  889 */     SymtabEntry localSymtabEntry = Util.typeOf(localUnionEntry.type());
/*  890 */     if ((localSymtabEntry instanceof PrimitiveEntry))
/*  891 */       paramInt = ((JavaGenerator)localSymtabEntry.generator()).write(paramInt, paramString1, paramString2 + ".discriminator ()", localSymtabEntry, paramPrintWriter);
/*      */     else
/*  893 */       paramPrintWriter.println(paramString1 + Util.helperName(localSymtabEntry, true) + ".write (ostream, " + paramString2 + ".discriminator ());");
/*  894 */     if (localSymtabEntry.name().equals("boolean"))
/*  895 */       paramInt = writeBoolean(paramString2 + ".discriminator ()", paramInt, paramString1, paramString2, localUnionEntry, paramPrintWriter);
/*      */     else
/*  897 */       paramInt = writeNonBoolean(paramString2 + ".discriminator ()", paramInt, paramString1, paramString2, localUnionEntry, paramPrintWriter);
/*  898 */     return paramInt;
/*      */   }
/*      */ 
/*      */   private int writeBoolean(String paramString1, int paramInt, String paramString2, String paramString3, UnionEntry paramUnionEntry, PrintWriter paramPrintWriter)
/*      */   {
/*  906 */     SymtabEntry localSymtabEntry = Util.typeOf(paramUnionEntry.type());
/*  907 */     Object localObject1 = (UnionBranch)paramUnionEntry.branches().firstElement();
/*      */     Object localObject2;
/*  909 */     if (paramUnionEntry.branches().size() == 2)
/*  910 */       localObject2 = (UnionBranch)paramUnionEntry.branches().lastElement();
/*      */     else
/*  912 */       localObject2 = null;
/*  913 */     boolean bool = false;
/*  914 */     int i = 0;
/*      */     try
/*      */     {
/*  917 */       if ((paramUnionEntry.branches().size() == 1) && ((paramUnionEntry.defaultBranch() != null) || (((UnionBranch)localObject1).labels.size() == 2)))
/*  918 */         i = 1;
/*      */       else
/*  920 */         bool = ((Boolean)((Expression)((UnionBranch)localObject1).labels.firstElement()).evaluate()).booleanValue();
/*      */     }
/*      */     catch (EvaluationException localEvaluationException)
/*      */     {
/*      */     }
/*  925 */     if (i != 0)
/*      */     {
/*  929 */       paramInt = writeBranch(paramInt, paramString2, paramString3, ((UnionBranch)localObject1).typedef, paramPrintWriter);
/*      */     }
/*      */     else
/*      */     {
/*  934 */       if (!bool)
/*      */       {
/*  936 */         Object localObject3 = localObject1;
/*  937 */         localObject1 = localObject2;
/*  938 */         localObject2 = localObject3;
/*      */       }
/*  940 */       if ((localObject1 != null) && (localObject2 != null)) {
/*  941 */         paramPrintWriter.println(paramString2 + "if (" + paramString1 + ')');
/*  942 */         paramPrintWriter.println(paramString2 + '{');
/*  943 */         paramInt = writeBranch(paramInt, paramString2 + "  ", paramString3, ((UnionBranch)localObject1).typedef, paramPrintWriter);
/*  944 */         paramPrintWriter.println(paramString2 + '}');
/*  945 */         paramPrintWriter.println(paramString2 + "else");
/*  946 */         paramPrintWriter.println(paramString2 + '{');
/*  947 */         paramInt = writeBranch(paramInt, paramString2 + "  ", paramString3, ((UnionBranch)localObject2).typedef, paramPrintWriter);
/*  948 */         paramPrintWriter.println(paramString2 + '}');
/*  949 */       } else if (localObject1 != null) {
/*  950 */         paramPrintWriter.println(paramString2 + "if (" + paramString1 + ')');
/*  951 */         paramPrintWriter.println(paramString2 + '{');
/*  952 */         paramInt = writeBranch(paramInt, paramString2 + "  ", paramString3, ((UnionBranch)localObject1).typedef, paramPrintWriter);
/*  953 */         paramPrintWriter.println(paramString2 + '}');
/*      */       } else {
/*  955 */         paramPrintWriter.println(paramString2 + "if (!" + paramString1 + ')');
/*  956 */         paramPrintWriter.println(paramString2 + '{');
/*  957 */         paramInt = writeBranch(paramInt, paramString2 + "  ", paramString3, ((UnionBranch)localObject2).typedef, paramPrintWriter);
/*  958 */         paramPrintWriter.println(paramString2 + '}');
/*      */       }
/*      */     }
/*  961 */     return paramInt;
/*      */   }
/*      */ 
/*      */   private int writeNonBoolean(String paramString1, int paramInt, String paramString2, String paramString3, UnionEntry paramUnionEntry, PrintWriter paramPrintWriter)
/*      */   {
/*  969 */     SymtabEntry localSymtabEntry = Util.typeOf(paramUnionEntry.type());
/*  970 */     if ((localSymtabEntry instanceof EnumEntry))
/*  971 */       paramPrintWriter.println(paramString2 + "switch (" + paramString3 + ".discriminator ().value ())");
/*      */     else
/*  973 */       paramPrintWriter.println(paramString2 + "switch (" + paramString3 + ".discriminator ())");
/*  974 */     paramPrintWriter.println(paramString2 + "{");
/*  975 */     String str1 = Util.javaQualifiedName(localSymtabEntry) + '.';
/*  976 */     Enumeration localEnumeration1 = paramUnionEntry.branches().elements();
/*  977 */     while (localEnumeration1.hasMoreElements())
/*      */     {
/*  979 */       UnionBranch localUnionBranch = (UnionBranch)localEnumeration1.nextElement();
/*  980 */       Enumeration localEnumeration2 = localUnionBranch.labels.elements();
/*  981 */       while (localEnumeration2.hasMoreElements())
/*      */       {
/*  983 */         Expression localExpression = (Expression)localEnumeration2.nextElement();
/*  984 */         if ((localSymtabEntry instanceof EnumEntry))
/*      */         {
/*  986 */           String str2 = Util.parseExpression(localExpression);
/*  987 */           paramPrintWriter.println(paramString2 + "  case " + str1 + '_' + str2 + ":");
/*      */         }
/*      */         else {
/*  990 */           paramPrintWriter.println(paramString2 + "  case " + cast(localExpression, localSymtabEntry) + ':');
/*      */         }
/*      */       }
/*  992 */       if (!localUnionBranch.typedef.equals(paramUnionEntry.defaultBranch()))
/*      */       {
/*  994 */         paramInt = writeBranch(paramInt, paramString2 + "    ", paramString3, localUnionBranch.typedef, paramPrintWriter);
/*  995 */         paramPrintWriter.println(paramString2 + "    break;");
/*      */       }
/*      */     }
/*  998 */     if (paramUnionEntry.defaultBranch() != null) {
/*  999 */       paramPrintWriter.println(paramString2 + "  default:");
/* 1000 */       paramInt = writeBranch(paramInt, paramString2 + "    ", paramString3, paramUnionEntry.defaultBranch(), paramPrintWriter);
/* 1001 */       paramPrintWriter.println(paramString2 + "    break;");
/*      */     }
/* 1003 */     paramPrintWriter.println(paramString2 + "}");
/* 1004 */     return paramInt;
/*      */   }
/*      */ 
/*      */   private int writeBranch(int paramInt, String paramString1, String paramString2, TypedefEntry paramTypedefEntry, PrintWriter paramPrintWriter)
/*      */   {
/* 1012 */     SymtabEntry localSymtabEntry = paramTypedefEntry.type();
/* 1013 */     if ((!paramTypedefEntry.arrayInfo().isEmpty()) || ((localSymtabEntry instanceof SequenceEntry)) || ((localSymtabEntry instanceof PrimitiveEntry)) || ((localSymtabEntry instanceof StringEntry)))
/* 1014 */       paramInt = ((JavaGenerator)paramTypedefEntry.generator()).write(paramInt, paramString1, paramString2 + '.' + paramTypedefEntry.name() + " ()", paramTypedefEntry, paramPrintWriter);
/*      */     else
/* 1016 */       paramPrintWriter.println(paramString1 + Util.helperName(localSymtabEntry, true) + ".write (ostream, " + paramString2 + '.' + paramTypedefEntry.name() + " ());");
/* 1017 */     return paramInt;
/*      */   }
/*      */ 
/*      */   private String cast(Expression paramExpression, SymtabEntry paramSymtabEntry)
/*      */   {
/* 1028 */     String str = Util.parseExpression(paramExpression);
/* 1029 */     if (paramSymtabEntry.name().indexOf("short") >= 0)
/*      */     {
/* 1031 */       if ((paramExpression.value() instanceof Long))
/*      */       {
/* 1033 */         long l1 = ((Long)paramExpression.value()).longValue();
/* 1034 */         if (l1 > 32767L)
/* 1035 */           str = "(short)(" + str + ')';
/*      */       }
/* 1037 */       else if ((paramExpression.value() instanceof Integer))
/*      */       {
/* 1039 */         int i = ((Integer)paramExpression.value()).intValue();
/* 1040 */         if (i > 32767)
/* 1041 */           str = "(short)(" + str + ')';
/*      */       }
/*      */     }
/* 1044 */     else if (paramSymtabEntry.name().indexOf("long") >= 0)
/*      */     {
/* 1046 */       if ((paramExpression.value() instanceof Long))
/*      */       {
/* 1048 */         long l2 = ((Long)paramExpression.value()).longValue();
/*      */ 
/* 1052 */         if ((l2 > 2147483647L) || (l2 == -2147483648L))
/* 1053 */           str = "(int)(" + str + ')';
/*      */       }
/* 1055 */       else if ((paramExpression.value() instanceof Integer))
/*      */       {
/* 1057 */         int j = ((Integer)paramExpression.value()).intValue();
/*      */ 
/* 1061 */         if ((j > 2147483647) || (j == -2147483648))
/* 1062 */           str = "(int)(" + str + ')';
/*      */       }
/*      */     }
/* 1065 */     return str;
/*      */   }
/*      */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.corba.se.idl.toJavaPortable.UnionGen
 * JD-Core Version:    0.6.2
 */