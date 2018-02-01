/*      */ package com.sun.tools.corba.se.idl.toJavaPortable;
/*      */ 
/*      */ import com.sun.tools.corba.se.idl.AttributeEntry;
/*      */ import com.sun.tools.corba.se.idl.Comment;
/*      */ import com.sun.tools.corba.se.idl.ExceptionEntry;
/*      */ import com.sun.tools.corba.se.idl.InterfaceEntry;
/*      */ import com.sun.tools.corba.se.idl.InterfaceState;
/*      */ import com.sun.tools.corba.se.idl.MethodEntry;
/*      */ import com.sun.tools.corba.se.idl.ParameterEntry;
/*      */ import com.sun.tools.corba.se.idl.PrimitiveEntry;
/*      */ import com.sun.tools.corba.se.idl.RepositoryID;
/*      */ import com.sun.tools.corba.se.idl.SequenceEntry;
/*      */ import com.sun.tools.corba.se.idl.StringEntry;
/*      */ import com.sun.tools.corba.se.idl.SymtabEntry;
/*      */ import com.sun.tools.corba.se.idl.TypedefEntry;
/*      */ import com.sun.tools.corba.se.idl.ValueBoxEntry;
/*      */ import com.sun.tools.corba.se.idl.ValueEntry;
/*      */ import com.sun.tools.corba.se.idl.constExpr.Expression;
/*      */ import java.io.PrintWriter;
/*      */ import java.util.Enumeration;
/*      */ import java.util.Hashtable;
/*      */ import java.util.Vector;
/*      */ 
/*      */ public class MethodGen
/*      */   implements com.sun.tools.corba.se.idl.MethodGen
/*      */ {
/*      */   private static final String ONE_INDENT = "    ";
/*      */   private static final String TWO_INDENT = "        ";
/*      */   private static final String THREE_INDENT = "            ";
/*      */   private static final String FOUR_INDENT = "                ";
/*      */   private static final String FIVE_INDENT = "                    ";
/*      */   private static final int ATTRIBUTE_METHOD_PREFIX_LENGTH = 5;
/* 1084 */   protected int methodIndex = 0;
/* 1085 */   protected String realName = "";
/* 1086 */   protected Hashtable symbolTable = null;
/* 1087 */   protected MethodEntry m = null;
/* 1088 */   protected PrintWriter stream = null;
/* 1089 */   protected boolean localOptimization = false;
/* 1090 */   protected boolean isAbstract = false;
/*      */ 
/*      */   public void generate(Hashtable paramHashtable, MethodEntry paramMethodEntry, PrintWriter paramPrintWriter)
/*      */   {
/*      */   }
/*      */ 
/*      */   protected void interfaceMethod(Hashtable paramHashtable, MethodEntry paramMethodEntry, PrintWriter paramPrintWriter)
/*      */   {
/*  108 */     this.symbolTable = paramHashtable;
/*  109 */     this.m = paramMethodEntry;
/*  110 */     this.stream = paramPrintWriter;
/*  111 */     if (paramMethodEntry.comment() != null)
/*  112 */       paramMethodEntry.comment().generate("", paramPrintWriter);
/*  113 */     paramPrintWriter.print("  ");
/*  114 */     SymtabEntry localSymtabEntry = paramMethodEntry.container();
/*  115 */     boolean bool = false;
/*  116 */     int i = 0;
/*  117 */     if ((localSymtabEntry instanceof ValueEntry))
/*      */     {
/*  119 */       bool = ((ValueEntry)localSymtabEntry).isAbstract();
/*  120 */       i = 1;
/*      */     }
/*  122 */     if ((i != 0) && (!bool))
/*  123 */       paramPrintWriter.print("public ");
/*  124 */     writeMethodSignature();
/*  125 */     if ((i != 0) && (!bool))
/*      */     {
/*  127 */       paramPrintWriter.println();
/*  128 */       paramPrintWriter.println("  {");
/*  129 */       paramPrintWriter.println("  }");
/*  130 */       paramPrintWriter.println();
/*      */     }
/*      */     else {
/*  133 */       paramPrintWriter.println(";");
/*      */     }
/*      */   }
/*      */ 
/*      */   protected void stub(String paramString, boolean paramBoolean, Hashtable paramHashtable, MethodEntry paramMethodEntry, PrintWriter paramPrintWriter, int paramInt)
/*      */   {
/*  142 */     this.localOptimization = ((Arguments)Compile.compiler.arguments).LocalOptimization;
/*      */ 
/*  144 */     this.isAbstract = paramBoolean;
/*  145 */     this.symbolTable = paramHashtable;
/*  146 */     this.m = paramMethodEntry;
/*  147 */     this.stream = paramPrintWriter;
/*  148 */     this.methodIndex = paramInt;
/*  149 */     if (paramMethodEntry.comment() != null)
/*  150 */       paramMethodEntry.comment().generate("  ", paramPrintWriter);
/*  151 */     paramPrintWriter.print("  public ");
/*  152 */     writeMethodSignature();
/*  153 */     paramPrintWriter.println();
/*  154 */     paramPrintWriter.println("  {");
/*  155 */     writeStubBody(paramString);
/*  156 */     paramPrintWriter.println("  } // " + paramMethodEntry.name());
/*  157 */     paramPrintWriter.println();
/*      */   }
/*      */ 
/*      */   protected void localstub(Hashtable paramHashtable, MethodEntry paramMethodEntry, PrintWriter paramPrintWriter, int paramInt, InterfaceEntry paramInterfaceEntry)
/*      */   {
/*  165 */     this.symbolTable = paramHashtable;
/*  166 */     this.m = paramMethodEntry;
/*  167 */     this.stream = paramPrintWriter;
/*  168 */     this.methodIndex = paramInt;
/*  169 */     if (paramMethodEntry.comment() != null)
/*  170 */       paramMethodEntry.comment().generate("  ", paramPrintWriter);
/*  171 */     paramPrintWriter.print("  public ");
/*  172 */     writeMethodSignature();
/*  173 */     paramPrintWriter.println();
/*  174 */     paramPrintWriter.println("  {");
/*  175 */     writeLocalStubBody(paramInterfaceEntry);
/*  176 */     paramPrintWriter.println("  } // " + paramMethodEntry.name());
/*  177 */     paramPrintWriter.println();
/*      */   }
/*      */ 
/*      */   protected void skeleton(Hashtable paramHashtable, MethodEntry paramMethodEntry, PrintWriter paramPrintWriter, int paramInt)
/*      */   {
/*  184 */     this.symbolTable = paramHashtable;
/*  185 */     this.m = paramMethodEntry;
/*  186 */     this.stream = paramPrintWriter;
/*  187 */     this.methodIndex = paramInt;
/*  188 */     if (paramMethodEntry.comment() != null)
/*  189 */       paramMethodEntry.comment().generate("  ", paramPrintWriter);
/*  190 */     paramPrintWriter.print("  public ");
/*  191 */     writeMethodSignature();
/*  192 */     paramPrintWriter.println();
/*  193 */     paramPrintWriter.println("  {");
/*  194 */     writeSkeletonBody();
/*  195 */     paramPrintWriter.println("  } // " + paramMethodEntry.name());
/*      */   }
/*      */ 
/*      */   protected void dispatchSkeleton(Hashtable paramHashtable, MethodEntry paramMethodEntry, PrintWriter paramPrintWriter, int paramInt)
/*      */   {
/*  203 */     this.symbolTable = paramHashtable;
/*  204 */     this.m = paramMethodEntry;
/*  205 */     this.stream = paramPrintWriter;
/*  206 */     this.methodIndex = paramInt;
/*  207 */     if (paramMethodEntry.comment() != null)
/*  208 */       paramMethodEntry.comment().generate("  ", paramPrintWriter);
/*  209 */     writeDispatchCall();
/*      */   }
/*      */ 
/*      */   protected boolean isValueInitializer()
/*      */   {
/*  219 */     MethodEntry localMethodEntry = null;
/*  220 */     if ((this.m.container() instanceof ValueEntry))
/*      */     {
/*  222 */       Enumeration localEnumeration = ((ValueEntry)this.m.container()).initializers().elements();
/*  223 */       while ((localMethodEntry != this.m) && (localEnumeration.hasMoreElements()))
/*  224 */         localMethodEntry = (MethodEntry)localEnumeration.nextElement();
/*      */     }
/*  226 */     return (localMethodEntry == this.m) && (null != this.m);
/*      */   }
/*      */ 
/*      */   protected void writeMethodSignature()
/*      */   {
/*  234 */     boolean bool = isValueInitializer();
/*      */ 
/*  246 */     if (this.m.type() == null)
/*      */     {
/*  248 */       if (!bool) {
/*  249 */         this.stream.print("void");
/*      */       }
/*      */ 
/*      */     }
/*      */     else
/*      */     {
/*  255 */       this.stream.print(Util.javaName(this.m.type()));
/*      */     }
/*      */ 
/*  262 */     if (bool)
/*  263 */       this.stream.print(' ' + this.m.container().name() + " (");
/*      */     else {
/*  265 */       this.stream.print(' ' + this.m.name() + " (");
/*      */     }
/*      */ 
/*  268 */     int i = 1;
/*  269 */     Enumeration localEnumeration = this.m.parameters().elements();
/*  270 */     while (localEnumeration.hasMoreElements())
/*      */     {
/*  272 */       if (i != 0)
/*  273 */         i = 0;
/*      */       else
/*  275 */         this.stream.print(", ");
/*  276 */       ParameterEntry localParameterEntry = (ParameterEntry)localEnumeration.nextElement();
/*      */ 
/*  278 */       writeParmType(localParameterEntry.type(), localParameterEntry.passType());
/*      */ 
/*  281 */       this.stream.print(' ' + localParameterEntry.name());
/*      */     }
/*      */ 
/*  285 */     if (this.m.contexts().size() > 0)
/*      */     {
/*  287 */       if (i == 0)
/*  288 */         this.stream.print(", ");
/*  289 */       this.stream.print("org.omg.CORBA.Context $context");
/*      */     }
/*      */ 
/*  293 */     if (this.m.exceptions().size() > 0)
/*      */     {
/*  295 */       this.stream.print(") throws ");
/*  296 */       localEnumeration = this.m.exceptions().elements();
/*  297 */       i = 1;
/*  298 */       while (localEnumeration.hasMoreElements())
/*      */       {
/*  300 */         if (i != 0)
/*  301 */           i = 0;
/*      */         else
/*  303 */           this.stream.print(", ");
/*  304 */         this.stream.print(Util.javaName((SymtabEntry)localEnumeration.nextElement()));
/*      */       }
/*      */     }
/*      */ 
/*  308 */     this.stream.print(')');
/*      */   }
/*      */ 
/*      */   protected void writeParmType(SymtabEntry paramSymtabEntry, int paramInt)
/*      */   {
/*  316 */     if (paramInt != 0)
/*      */     {
/*  318 */       paramSymtabEntry = Util.typeOf(paramSymtabEntry);
/*  319 */       this.stream.print(Util.holderName(paramSymtabEntry));
/*      */     }
/*      */     else
/*      */     {
/*  324 */       this.stream.print(Util.javaName(paramSymtabEntry));
/*      */     }
/*      */   }
/*      */ 
/*      */   protected void writeDispatchCall()
/*      */   {
/*  332 */     String str1 = "       ";
/*  333 */     String str2 = this.m.fullName();
/*  334 */     if ((this.m instanceof AttributeEntry))
/*      */     {
/*  337 */       int i = str2.lastIndexOf('/') + 1;
/*  338 */       if (this.m.type() == null)
/*  339 */         str2 = str2.substring(0, i) + "_set_" + this.m.name();
/*      */       else
/*  341 */         str2 = str2.substring(0, i) + "_get_" + this.m.name();
/*      */     }
/*  343 */     this.stream.println(str1 + "case " + this.methodIndex + ":  // " + str2);
/*  344 */     this.stream.println(str1 + "{");
/*  345 */     str1 = str1 + "  ";
/*  346 */     if (this.m.exceptions().size() > 0)
/*      */     {
/*  348 */       this.stream.println(str1 + "try {");
/*  349 */       str1 = str1 + "  ";
/*      */     }
/*      */ 
/*  353 */     SymtabEntry localSymtabEntry1 = Util.typeOf(this.m.type());
/*  354 */     Enumeration localEnumeration = this.m.parameters().elements();
/*  355 */     localEnumeration = this.m.parameters().elements();
/*      */     Object localObject1;
/*      */     Object localObject2;
/*  356 */     while (localEnumeration.hasMoreElements())
/*      */     {
/*  358 */       ParameterEntry localParameterEntry = (ParameterEntry)localEnumeration.nextElement();
/*  359 */       localObject1 = localParameterEntry.name();
/*  360 */       String str3 = '_' + (String)localObject1;
/*  361 */       localObject2 = localParameterEntry.type();
/*  362 */       int n = localParameterEntry.passType();
/*      */ 
/*  364 */       if (n == 0) {
/*  365 */         Util.writeInitializer(str1, (String)localObject1, "", (SymtabEntry)localObject2, writeInputStreamRead("in", (SymtabEntry)localObject2), this.stream);
/*      */       }
/*      */       else
/*      */       {
/*  369 */         String str4 = Util.holderName((SymtabEntry)localObject2);
/*  370 */         this.stream.println(str1 + str4 + ' ' + (String)localObject1 + " = new " + str4 + " ();");
/*  371 */         if (n == 1)
/*      */         {
/*  373 */           if ((localObject2 instanceof ValueBoxEntry))
/*      */           {
/*  375 */             ValueBoxEntry localValueBoxEntry = (ValueBoxEntry)localObject2;
/*  376 */             TypedefEntry localTypedefEntry = ((InterfaceState)localValueBoxEntry.state().elementAt(0)).entry;
/*  377 */             SymtabEntry localSymtabEntry2 = localTypedefEntry.type();
/*  378 */             if ((localSymtabEntry2 instanceof PrimitiveEntry))
/*  379 */               this.stream.println(str1 + (String)localObject1 + ".value = (" + writeInputStreamRead("in", localParameterEntry.type()) + ").value;");
/*      */             else
/*  381 */               this.stream.println(str1 + (String)localObject1 + ".value = " + writeInputStreamRead("in", localParameterEntry.type()) + ";");
/*      */           }
/*      */           else {
/*  384 */             this.stream.println(str1 + (String)localObject1 + ".value = " + writeInputStreamRead("in", localParameterEntry.type()) + ";");
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/*      */ 
/*  390 */     if (this.m.contexts().size() > 0)
/*      */     {
/*  392 */       this.stream.println(str1 + "org.omg.CORBA.Context $context = in.read_Context ();");
/*      */     }
/*      */ 
/*  396 */     if (localSymtabEntry1 != null) {
/*  397 */       Util.writeInitializer(str1, "$result", "", localSymtabEntry1, this.stream);
/*      */     }
/*      */ 
/*  400 */     writeMethodCall(str1);
/*      */ 
/*  402 */     localEnumeration = this.m.parameters().elements();
/*  403 */     int j = 1;
/*  404 */     while (localEnumeration.hasMoreElements())
/*      */     {
/*  406 */       localObject1 = (ParameterEntry)localEnumeration.nextElement();
/*  407 */       if (j != 0)
/*  408 */         j = 0;
/*      */       else
/*  410 */         this.stream.print(", ");
/*  411 */       this.stream.print(((ParameterEntry)localObject1).name());
/*      */     }
/*      */ 
/*  415 */     if (this.m.contexts().size() > 0)
/*      */     {
/*  417 */       if (j == 0)
/*  418 */         this.stream.print(", ");
/*  419 */       this.stream.print("$context");
/*      */     }
/*      */ 
/*  422 */     this.stream.println(");");
/*      */ 
/*  425 */     writeCreateReply(str1);
/*      */ 
/*  428 */     if (localSymtabEntry1 != null)
/*      */     {
/*  430 */       writeOutputStreamWrite(str1, "out", "$result", localSymtabEntry1, this.stream);
/*      */     }
/*      */ 
/*  434 */     localEnumeration = this.m.parameters().elements();
/*  435 */     while (localEnumeration.hasMoreElements())
/*      */     {
/*  437 */       localObject1 = (ParameterEntry)localEnumeration.nextElement();
/*  438 */       int k = ((ParameterEntry)localObject1).passType();
/*  439 */       if (k != 0)
/*      */       {
/*  441 */         writeOutputStreamWrite(str1, "out", ((ParameterEntry)localObject1).name() + ".value", ((ParameterEntry)localObject1).type(), this.stream);
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/*  446 */     if (this.m.exceptions().size() > 0)
/*      */     {
/*  448 */       localObject1 = this.m.exceptions().elements();
/*  449 */       while (((Enumeration)localObject1).hasMoreElements())
/*      */       {
/*  451 */         str1 = "         ";
/*  452 */         ExceptionEntry localExceptionEntry = (ExceptionEntry)((Enumeration)localObject1).nextElement();
/*  453 */         localObject2 = Util.javaQualifiedName(localExceptionEntry);
/*  454 */         this.stream.println(str1 + "} catch (" + (String)localObject2 + " $ex) {");
/*  455 */         str1 = str1 + "  ";
/*  456 */         this.stream.println(str1 + "out = $rh.createExceptionReply ();");
/*  457 */         this.stream.println(str1 + Util.helperName(localExceptionEntry, true) + ".write (out, $ex);");
/*      */       }
/*      */ 
/*  460 */       str1 = "         ";
/*  461 */       this.stream.println(str1 + "}");
/*      */     }
/*      */ 
/*  464 */     this.stream.println("         break;");
/*  465 */     this.stream.println("       }");
/*  466 */     this.stream.println();
/*      */   }
/*      */ 
/*      */   protected void writeStubBody(String paramString)
/*      */   {
/*  475 */     String str = Util.stripLeadingUnderscores(this.m.name());
/*  476 */     if ((this.m instanceof AttributeEntry))
/*      */     {
/*  478 */       if (this.m.type() == null)
/*  479 */         str = "_set_" + str;
/*      */       else
/*  481 */         str = "_get_" + str;
/*      */     }
/*  483 */     if ((this.localOptimization) && (!this.isAbstract)) {
/*  484 */       this.stream.println("    while(true) {");
/*  485 */       this.stream.println("        if(!this._is_local()) {");
/*      */     }
/*  487 */     this.stream.println("            org.omg.CORBA.portable.InputStream $in = null;");
/*      */ 
/*  489 */     this.stream.println("            try {");
/*  490 */     this.stream.println("                org.omg.CORBA.portable.OutputStream $out = _request (\"" + str + "\", " + (
/*  491 */       !this.m
/*  491 */       .oneway()) + ");");
/*      */ 
/*  495 */     Enumeration localEnumeration1 = this.m.parameters().elements();
/*      */     ParameterEntry localParameterEntry;
/*      */     Object localObject1;
/*      */     Object localObject2;
/*  496 */     while (localEnumeration1.hasMoreElements())
/*      */     {
/*  498 */       localParameterEntry = (ParameterEntry)localEnumeration1.nextElement();
/*  499 */       localObject1 = Util.typeOf(localParameterEntry.type());
/*  500 */       if (((localObject1 instanceof StringEntry)) && (
/*  501 */         (localParameterEntry.passType() == 0) || 
/*  502 */         (localParameterEntry
/*  502 */         .passType() == 1)))
/*      */       {
/*  504 */         localObject2 = (StringEntry)localObject1;
/*  505 */         if (((StringEntry)localObject2).maxSize() != null)
/*      */         {
/*  507 */           this.stream.print("            if (" + localParameterEntry.name());
/*  508 */           if (localParameterEntry.passType() == 1)
/*  509 */             this.stream.print(".value");
/*  510 */           this.stream.print(" == null || " + localParameterEntry.name());
/*  511 */           if (localParameterEntry.passType() == 1)
/*  512 */             this.stream.print(".value");
/*  513 */           this.stream.println(".length () > (" + 
/*  514 */             Util.parseExpression(((StringEntry)localObject2)
/*  514 */             .maxSize()) + "))");
/*  515 */           this.stream.println("            throw new org.omg.CORBA.BAD_PARAM (0, org.omg.CORBA.CompletionStatus.COMPLETED_NO);");
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/*  524 */     localEnumeration1 = this.m.parameters().elements();
/*  525 */     while (localEnumeration1.hasMoreElements())
/*      */     {
/*  527 */       localParameterEntry = (ParameterEntry)localEnumeration1.nextElement();
/*  528 */       if (localParameterEntry.passType() == 0) {
/*  529 */         writeOutputStreamWrite("                ", "$out", localParameterEntry.name(), localParameterEntry.type(), this.stream);
/*      */       }
/*  531 */       else if (localParameterEntry.passType() == 1) {
/*  532 */         writeOutputStreamWrite("                ", "$out", localParameterEntry.name() + ".value", localParameterEntry
/*  533 */           .type(), this.stream);
/*      */       }
/*      */     }
/*      */ 
/*  537 */     if (this.m.contexts().size() > 0)
/*      */     {
/*  539 */       this.stream.println("                org.omg.CORBA.ContextList $contextList =_orb ().create_context_list ();");
/*      */ 
/*  542 */       for (int i = 0; i < this.m.contexts().size(); i++)
/*      */       {
/*  544 */         this.stream.println("                $contextList.add (\"" + this.m
/*  545 */           .contexts().elementAt(i) + "\");");
/*      */       }
/*  547 */       this.stream.println("                $out.write_Context ($context, $contextList);");
/*      */     }
/*      */ 
/*  552 */     this.stream.println("                $in = _invoke ($out);");
/*      */ 
/*  554 */     SymtabEntry localSymtabEntry1 = this.m.type();
/*  555 */     if (localSymtabEntry1 != null) {
/*  556 */       Util.writeInitializer("                ", "$result", "", localSymtabEntry1, 
/*  557 */         writeInputStreamRead("$in", localSymtabEntry1), 
/*  557 */         this.stream);
/*      */     }
/*      */ 
/*  560 */     localEnumeration1 = this.m.parameters().elements();
/*      */     Object localObject3;
/*  561 */     while (localEnumeration1.hasMoreElements())
/*      */     {
/*  563 */       localObject1 = (ParameterEntry)localEnumeration1.nextElement();
/*  564 */       if (((ParameterEntry)localObject1).passType() != 0)
/*      */       {
/*  566 */         if ((((ParameterEntry)localObject1).type() instanceof ValueBoxEntry))
/*      */         {
/*  568 */           localObject2 = (ValueBoxEntry)((ParameterEntry)localObject1).type();
/*      */ 
/*  570 */           localObject3 = ((InterfaceState)((ValueBoxEntry)localObject2)
/*  570 */             .state().elementAt(0)).entry;
/*  571 */           SymtabEntry localSymtabEntry2 = ((TypedefEntry)localObject3).type();
/*  572 */           if ((localSymtabEntry2 instanceof PrimitiveEntry)) {
/*  573 */             this.stream.println("                " + ((ParameterEntry)localObject1).name() + ".value = (" + 
/*  574 */               writeInputStreamRead("$in", ((ParameterEntry)localObject1)
/*  574 */               .type()) + ").value;");
/*      */           }
/*      */           else
/*  577 */             this.stream.println("                " + ((ParameterEntry)localObject1).name() + ".value = " + 
/*  578 */               writeInputStreamRead("$in", ((ParameterEntry)localObject1)
/*  578 */               .type()) + ";");
/*      */         }
/*      */         else {
/*  581 */           this.stream.println("                " + ((ParameterEntry)localObject1).name() + ".value = " + 
/*  582 */             writeInputStreamRead("$in", ((ParameterEntry)localObject1)
/*  582 */             .type()) + ";");
/*      */         }
/*      */       }
/*      */     }
/*      */ 
/*  587 */     localEnumeration1 = this.m.parameters().elements();
/*  588 */     while (localEnumeration1.hasMoreElements())
/*      */     {
/*  590 */       localObject1 = (ParameterEntry)localEnumeration1.nextElement();
/*  591 */       localObject2 = Util.typeOf(((ParameterEntry)localObject1).type());
/*  592 */       if (((localObject2 instanceof StringEntry)) && (
/*  593 */         (((ParameterEntry)localObject1).passType() == 2) || 
/*  594 */         (((ParameterEntry)localObject1)
/*  594 */         .passType() == 1)))
/*      */       {
/*  596 */         localObject3 = (StringEntry)localObject2;
/*  597 */         if (((StringEntry)localObject3).maxSize() != null)
/*      */         {
/*  599 */           this.stream.print("                if (" + ((ParameterEntry)localObject1).name() + ".value.length ()");
/*      */ 
/*  601 */           this.stream.println("         > (" + 
/*  602 */             Util.parseExpression(((StringEntry)localObject3)
/*  602 */             .maxSize()) + "))");
/*  603 */           this.stream.println("                    throw new org.omg.CORBA.MARSHAL(0,org.omg.CORBA.CompletionStatus.COMPLETED_NO);");
/*      */         }
/*      */       }
/*      */     }
/*      */ 
/*  608 */     if ((localSymtabEntry1 instanceof StringEntry))
/*      */     {
/*  610 */       localObject1 = (StringEntry)localSymtabEntry1;
/*  611 */       if (((StringEntry)localObject1).maxSize() != null)
/*      */       {
/*  613 */         this.stream.println("                if ($result.length () > (" + 
/*  614 */           Util.parseExpression(((StringEntry)localObject1)
/*  614 */           .maxSize()) + "))");
/*  615 */         this.stream.println("                    throw new org.omg.CORBA.MARSHAL (0, org.omg.CORBA.CompletionStatus.COMPLETED_NO);");
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/*  622 */     if (localSymtabEntry1 != null)
/*  623 */       this.stream.println("                return $result;");
/*      */     else {
/*  625 */       this.stream.println("                return;");
/*      */     }
/*      */ 
/*  629 */     this.stream.println("            } catch (org.omg.CORBA.portable.ApplicationException $ex) {");
/*      */ 
/*  631 */     this.stream.println("                $in = $ex.getInputStream ();");
/*  632 */     this.stream.println("                String _id = $ex.getId ();");
/*      */ 
/*  634 */     if (this.m.exceptions().size() > 0)
/*      */     {
/*  636 */       localObject1 = this.m.exceptions().elements();
/*  637 */       int k = 1;
/*  638 */       while (((Enumeration)localObject1).hasMoreElements())
/*      */       {
/*  640 */         localObject3 = (ExceptionEntry)((Enumeration)localObject1).nextElement();
/*  641 */         if (k != 0)
/*      */         {
/*  643 */           this.stream.print("                if ");
/*  644 */           k = 0;
/*      */         }
/*      */         else {
/*  647 */           this.stream.print("                else if ");
/*      */         }
/*  649 */         this.stream.println("(_id.equals (\"" + ((ExceptionEntry)localObject3).repositoryID().ID() + "\"))");
/*  650 */         this.stream.println("                    throw " + 
/*  651 */           Util.helperName((SymtabEntry)localObject3, false) + 
/*  651 */           ".read ($in);");
/*      */       }
/*  653 */       this.stream.println("                else");
/*  654 */       this.stream.println("                    throw new org.omg.CORBA.MARSHAL (_id);");
/*      */     }
/*      */     else {
/*  657 */       this.stream.println("                throw new org.omg.CORBA.MARSHAL (_id);");
/*      */     }
/*  659 */     this.stream.println("            } catch (org.omg.CORBA.portable.RemarshalException $rm) {");
/*      */ 
/*  661 */     this.stream.print("                ");
/*  662 */     if (this.m.type() != null)
/*  663 */       this.stream.print("return ");
/*  664 */     this.stream.print(this.m.name() + " (");
/*      */ 
/*  667 */     int j = 1;
/*  668 */     Enumeration localEnumeration2 = this.m.parameters().elements();
/*  669 */     while (localEnumeration2.hasMoreElements())
/*      */     {
/*  671 */       if (j != 0)
/*  672 */         j = 0;
/*      */       else
/*  674 */         this.stream.print(", ");
/*  675 */       localObject3 = (ParameterEntry)localEnumeration2.nextElement();
/*  676 */       this.stream.print(((ParameterEntry)localObject3).name());
/*      */     }
/*      */ 
/*  679 */     if (this.m.contexts().size() > 0)
/*      */     {
/*  681 */       if (j == 0)
/*  682 */         this.stream.print(", ");
/*  683 */       this.stream.print("$context");
/*      */     }
/*      */ 
/*  686 */     this.stream.println("        );");
/*  687 */     this.stream.println("            } finally {");
/*  688 */     this.stream.println("                _releaseReply ($in);");
/*  689 */     this.stream.println("            }");
/*  690 */     if ((this.localOptimization) && (!this.isAbstract)) {
/*  691 */       this.stream.println("        }");
/*  692 */       writeStubBodyForLocalInvocation(paramString, str);
/*      */     }
/*      */   }
/*      */ 
/*      */   private void writeStubBodyForLocalInvocation(String paramString1, String paramString2)
/*      */   {
/*  706 */     this.stream.println("        else {");
/*  707 */     this.stream.println("            org.omg.CORBA.portable.ServantObject _so =");
/*      */ 
/*  709 */     this.stream.println("                _servant_preinvoke(\"" + paramString2 + "\", _opsClass);");
/*      */ 
/*  711 */     this.stream.println("            if (_so == null ) {");
/*  712 */     this.stream.println("                continue;");
/*  713 */     this.stream.println("            }");
/*  714 */     this.stream.println("            " + paramString1 + "Operations _self =");
/*  715 */     this.stream.println("                (" + paramString1 + "Operations) _so.servant;");
/*  716 */     this.stream.println("            try {");
/*  717 */     Enumeration localEnumeration = this.m.parameters().elements();
/*  718 */     if ((this.m instanceof AttributeEntry))
/*      */     {
/*  722 */       paramString2 = paramString2.substring(5);
/*      */     }
/*  724 */     int i = this.m.type() == null ? 1 : 0;
/*  725 */     if (i == 0) {
/*  726 */       this.stream.println("                " + Util.javaName(this.m.type()) + " $result;");
/*      */     }
/*      */ 
/*  729 */     if (!isValueInitializer()) {
/*  730 */       if (i != 0)
/*  731 */         this.stream.print("                _self." + paramString2 + "( ");
/*      */       else {
/*  733 */         this.stream.print("                $result = _self." + paramString2 + "( ");
/*      */       }
/*      */ 
/*  736 */       while (localEnumeration.hasMoreElements()) {
/*  737 */         ParameterEntry localParameterEntry = (ParameterEntry)localEnumeration.nextElement();
/*  738 */         if (localEnumeration.hasMoreElements())
/*  739 */           this.stream.print(" " + localParameterEntry.name() + ",");
/*      */         else {
/*  741 */           this.stream.print(" " + localParameterEntry.name());
/*      */         }
/*      */       }
/*  744 */       this.stream.print(");");
/*  745 */       this.stream.println(" ");
/*  746 */       if (i != 0)
/*  747 */         this.stream.println("                return;");
/*      */       else {
/*  749 */         this.stream.println("                return $result;");
/*      */       }
/*      */     }
/*  752 */     this.stream.println(" ");
/*  753 */     this.stream.println("            }");
/*  754 */     this.stream.println("            finally {");
/*  755 */     this.stream.println("                _servant_postinvoke(_so);");
/*  756 */     this.stream.println("            }");
/*  757 */     this.stream.println("        }");
/*  758 */     this.stream.println("    }");
/*      */   }
/*      */ 
/*      */   protected void writeLocalStubBody(InterfaceEntry paramInterfaceEntry)
/*      */   {
/*  765 */     String str1 = Util.stripLeadingUnderscores(this.m.name());
/*  766 */     if ((this.m instanceof AttributeEntry))
/*      */     {
/*  768 */       if (this.m.type() == null)
/*  769 */         str1 = "_set_" + str1;
/*      */       else {
/*  771 */         str1 = "_get_" + str1;
/*      */       }
/*      */     }
/*  774 */     this.stream.println("      org.omg.CORBA.portable.ServantObject $so = _servant_preinvoke (\"" + str1 + "\", " + "_opsClass);");
/*      */ 
/*  779 */     String str2 = paramInterfaceEntry.name() + "Operations";
/*  780 */     this.stream.println("      " + str2 + "  $self = " + "(" + str2 + ") " + "$so.servant;");
/*  781 */     this.stream.println();
/*  782 */     this.stream.println("      try {");
/*  783 */     this.stream.print("         ");
/*  784 */     if (this.m.type() != null)
/*  785 */       this.stream.print("return ");
/*  786 */     this.stream.print("$self." + this.m.name() + " (");
/*      */ 
/*  789 */     int i = 1;
/*  790 */     Enumeration localEnumeration = this.m.parameters().elements();
/*  791 */     while (localEnumeration.hasMoreElements())
/*      */     {
/*  793 */       if (i != 0)
/*  794 */         i = 0;
/*      */       else
/*  796 */         this.stream.print(", ");
/*  797 */       ParameterEntry localParameterEntry = (ParameterEntry)localEnumeration.nextElement();
/*  798 */       this.stream.print(localParameterEntry.name());
/*      */     }
/*      */ 
/*  801 */     if (this.m.contexts().size() > 0)
/*      */     {
/*  803 */       if (i == 0)
/*  804 */         this.stream.print(", ");
/*  805 */       this.stream.print("$context");
/*      */     }
/*      */ 
/*  808 */     this.stream.println(");");
/*      */ 
/*  811 */     this.stream.println("      } finally {");
/*  812 */     this.stream.println("          _servant_postinvoke ($so);");
/*  813 */     this.stream.println("      }");
/*      */   }
/*      */ 
/*      */   private void writeInsert(String paramString1, String paramString2, String paramString3, SymtabEntry paramSymtabEntry, PrintWriter paramPrintWriter)
/*      */   {
/*  825 */     String str = paramSymtabEntry.name();
/*  826 */     if ((paramSymtabEntry instanceof PrimitiveEntry))
/*      */     {
/*  829 */       if (str.equals("long long"))
/*  830 */         paramPrintWriter.println(paramString1 + paramString3 + ".insert_longlong (" + paramString2 + ");");
/*  831 */       else if (str.equals("unsigned short"))
/*  832 */         paramPrintWriter.println(paramString1 + paramString3 + ".insert_ushort (" + paramString2 + ");");
/*  833 */       else if (str.equals("unsigned long"))
/*  834 */         paramPrintWriter.println(paramString1 + paramString3 + ".insert_ulong (" + paramString2 + ");");
/*  835 */       else if (str.equals("unsigned long long"))
/*  836 */         paramPrintWriter.println(paramString1 + paramString3 + ".insert_ulonglong (" + paramString2 + ");");
/*      */       else
/*  838 */         paramPrintWriter.println(paramString1 + paramString3 + ".insert_" + str + " (" + paramString2 + ");");
/*      */     }
/*  840 */     else if ((paramSymtabEntry instanceof StringEntry))
/*  841 */       paramPrintWriter.println(paramString1 + paramString3 + ".insert_" + str + " (" + paramString2 + ");");
/*      */     else
/*  843 */       paramPrintWriter.println(paramString1 + Util.helperName(paramSymtabEntry, true) + ".insert (" + paramString3 + ", " + paramString2 + ");");
/*      */   }
/*      */ 
/*      */   private void writeType(String paramString1, String paramString2, SymtabEntry paramSymtabEntry, PrintWriter paramPrintWriter)
/*      */   {
/*  851 */     if ((paramSymtabEntry instanceof PrimitiveEntry))
/*      */     {
/*  854 */       if (paramSymtabEntry.name().equals("long long"))
/*  855 */         paramPrintWriter.println(paramString1 + paramString2 + " (org.omg.CORBA.ORB.init ().get_primitive_tc (org.omg.CORBA.TCKind.tk_longlong));");
/*  856 */       else if (paramSymtabEntry.name().equals("unsigned short"))
/*  857 */         paramPrintWriter.println(paramString1 + paramString2 + " (org.omg.CORBA.ORB.init ().get_primitive_tc (org.omg.CORBA.TCKind.tk_ushort));");
/*  858 */       else if (paramSymtabEntry.name().equals("unsigned long"))
/*  859 */         paramPrintWriter.println(paramString1 + paramString2 + " (org.omg.CORBA.ORB.init ().get_primitive_tc (org.omg.CORBA.TCKind.tk_ulong));");
/*  860 */       else if (paramSymtabEntry.name().equals("unsigned long long"))
/*  861 */         paramPrintWriter.println(paramString1 + paramString2 + " (org.omg.CORBA.ORB.init ().get_primitive_tc (org.omg.CORBA.TCKind.tk_ulonglong));");
/*      */       else
/*  863 */         paramPrintWriter.println(paramString1 + paramString2 + " (org.omg.CORBA.ORB.init ().get_primitive_tc (org.omg.CORBA.TCKind.tk_" + paramSymtabEntry.name() + "));");
/*      */     }
/*  865 */     else if ((paramSymtabEntry instanceof StringEntry))
/*      */     {
/*  867 */       StringEntry localStringEntry = (StringEntry)paramSymtabEntry;
/*  868 */       Expression localExpression = localStringEntry.maxSize();
/*  869 */       if (localExpression == null)
/*  870 */         paramPrintWriter.println(paramString1 + paramString2 + " (org.omg.CORBA.ORB.init ().create_" + paramSymtabEntry.name() + "_tc (" + Util.parseExpression(localExpression) + "));");
/*      */       else
/*  872 */         paramPrintWriter.println(paramString1 + paramString2 + " (org.omg.CORBA.ORB.init ().create_" + paramSymtabEntry.name() + "_tc (0));");
/*      */     }
/*      */     else {
/*  875 */       paramPrintWriter.println(paramString1 + paramString2 + '(' + Util.helperName(paramSymtabEntry, true) + ".type ());");
/*      */     }
/*      */   }
/*      */ 
/*      */   private void writeExtract(String paramString1, String paramString2, String paramString3, SymtabEntry paramSymtabEntry, PrintWriter paramPrintWriter)
/*      */   {
/*  883 */     if ((paramSymtabEntry instanceof PrimitiveEntry))
/*      */     {
/*  885 */       if (paramSymtabEntry.name().equals("long long"))
/*  886 */         paramPrintWriter.println(paramString1 + paramString2 + " = " + paramString3 + ".extract_longlong ();");
/*  887 */       else if (paramSymtabEntry.name().equals("unsigned short"))
/*  888 */         paramPrintWriter.println(paramString1 + paramString2 + " = " + paramString3 + ".extract_ushort ();");
/*  889 */       else if (paramSymtabEntry.name().equals("unsigned long"))
/*  890 */         paramPrintWriter.println(paramString1 + paramString2 + " = " + paramString3 + ".extract_ulong ();");
/*  891 */       else if (paramSymtabEntry.name().equals("unsigned long long"))
/*  892 */         paramPrintWriter.println(paramString1 + paramString2 + " = " + paramString3 + ".extract_ulonglong ();");
/*      */       else
/*  894 */         paramPrintWriter.println(paramString1 + paramString2 + " = " + paramString3 + ".extract_" + paramSymtabEntry.name() + " ();");
/*      */     }
/*  896 */     else if ((paramSymtabEntry instanceof StringEntry))
/*  897 */       paramPrintWriter.println(paramString1 + paramString2 + " = " + paramString3 + ".extract_" + paramSymtabEntry.name() + " ();");
/*      */     else
/*  899 */       paramPrintWriter.println(paramString1 + paramString2 + " = " + Util.helperName(paramSymtabEntry, true) + ".extract (" + paramString3 + ");");
/*      */   }
/*      */ 
/*      */   private String writeExtract(String paramString, SymtabEntry paramSymtabEntry)
/*      */   {
/*      */     String str;
/*  908 */     if ((paramSymtabEntry instanceof PrimitiveEntry))
/*      */     {
/*  910 */       if (paramSymtabEntry.name().equals("long long"))
/*  911 */         str = paramString + ".extract_longlong ()";
/*  912 */       else if (paramSymtabEntry.name().equals("unsigned short"))
/*  913 */         str = paramString + ".extract_ushort ()";
/*  914 */       else if (paramSymtabEntry.name().equals("unsigned long"))
/*  915 */         str = paramString + ".extract_ulong ()";
/*  916 */       else if (paramSymtabEntry.name().equals("unsigned long long"))
/*  917 */         str = paramString + ".extract_ulonglong ()";
/*      */       else
/*  919 */         str = paramString + ".extract_" + paramSymtabEntry.name() + " ()";
/*      */     }
/*  921 */     else if ((paramSymtabEntry instanceof StringEntry))
/*  922 */       str = paramString + ".extract_" + paramSymtabEntry.name() + " ()";
/*      */     else
/*  924 */       str = Util.helperName(paramSymtabEntry, true) + ".extract (" + paramString + ')';
/*  925 */     return str;
/*      */   }
/*      */ 
/*      */   private void writeSkeletonBody()
/*      */   {
/*  933 */     SymtabEntry localSymtabEntry = Util.typeOf(this.m.type());
/*      */ 
/*  936 */     this.stream.print("    ");
/*  937 */     if (localSymtabEntry != null)
/*  938 */       this.stream.print("return ");
/*  939 */     this.stream.print("_impl." + this.m.name() + '(');
/*      */ 
/*  942 */     Enumeration localEnumeration = this.m.parameters().elements();
/*  943 */     int i = 1;
/*  944 */     while (localEnumeration.hasMoreElements())
/*      */     {
/*  946 */       ParameterEntry localParameterEntry = (ParameterEntry)localEnumeration.nextElement();
/*  947 */       if (i != 0)
/*  948 */         i = 0;
/*      */       else
/*  950 */         this.stream.print(", ");
/*  951 */       this.stream.print(localParameterEntry.name());
/*      */     }
/*  953 */     if (this.m.contexts().size() != 0)
/*      */     {
/*  955 */       if (i == 0)
/*  956 */         this.stream.print(", ");
/*  957 */       this.stream.print("$context");
/*      */     }
/*      */ 
/*  960 */     this.stream.println(");");
/*      */   }
/*      */ 
/*      */   protected String passType(int paramInt)
/*      */   {
/*      */     String str;
/*  969 */     switch (paramInt)
/*      */     {
/*      */     case 1:
/*  972 */       str = "org.omg.CORBA.ARG_INOUT.value";
/*  973 */       break;
/*      */     case 2:
/*  975 */       str = "org.omg.CORBA.ARG_OUT.value";
/*  976 */       break;
/*      */     case 0:
/*      */     default:
/*  979 */       str = "org.omg.CORBA.ARG_IN.value";
/*      */     }
/*      */ 
/*  982 */     return str;
/*      */   }
/*      */ 
/*      */   protected void serverMethodName(String paramString)
/*      */   {
/*  993 */     this.realName = (paramString == null ? "" : paramString);
/*      */   }
/*      */ 
/*      */   private void writeOutputStreamWrite(String paramString1, String paramString2, String paramString3, SymtabEntry paramSymtabEntry, PrintWriter paramPrintWriter)
/*      */   {
/* 1001 */     String str = paramSymtabEntry.name();
/* 1002 */     paramPrintWriter.print(paramString1);
/* 1003 */     if ((paramSymtabEntry instanceof PrimitiveEntry))
/*      */     {
/* 1005 */       if (str.equals("long long"))
/* 1006 */         paramPrintWriter.println(paramString2 + ".write_longlong (" + paramString3 + ");");
/* 1007 */       else if (str.equals("unsigned short"))
/* 1008 */         paramPrintWriter.println(paramString2 + ".write_ushort (" + paramString3 + ");");
/* 1009 */       else if (str.equals("unsigned long"))
/* 1010 */         paramPrintWriter.println(paramString2 + ".write_ulong (" + paramString3 + ");");
/* 1011 */       else if (str.equals("unsigned long long"))
/* 1012 */         paramPrintWriter.println(paramString2 + ".write_ulonglong (" + paramString3 + ");");
/*      */       else
/* 1014 */         paramPrintWriter.println(paramString2 + ".write_" + str + " (" + paramString3 + ");");
/*      */     }
/* 1016 */     else if ((paramSymtabEntry instanceof StringEntry)) {
/* 1017 */       paramPrintWriter.println(paramString2 + ".write_" + str + " (" + paramString3 + ");");
/* 1018 */     } else if ((paramSymtabEntry instanceof SequenceEntry)) {
/* 1019 */       paramPrintWriter.println(paramString2 + ".write_" + paramSymtabEntry.type().name() + " (" + paramString3 + ");");
/* 1020 */     } else if ((paramSymtabEntry instanceof ValueBoxEntry))
/*      */     {
/* 1022 */       ValueBoxEntry localValueBoxEntry = (ValueBoxEntry)paramSymtabEntry;
/* 1023 */       TypedefEntry localTypedefEntry = ((InterfaceState)localValueBoxEntry.state().elementAt(0)).entry;
/* 1024 */       SymtabEntry localSymtabEntry = localTypedefEntry.type();
/*      */ 
/* 1027 */       if (((localSymtabEntry instanceof PrimitiveEntry)) && (paramString3.endsWith(".value")))
/* 1028 */         paramPrintWriter.println(Util.helperName(paramSymtabEntry, true) + ".write (" + paramString2 + ", " + " new " + 
/* 1029 */           Util.javaQualifiedName(paramSymtabEntry) + 
/* 1029 */           " (" + paramString3 + "));");
/*      */       else
/* 1031 */         paramPrintWriter.println(Util.helperName(paramSymtabEntry, true) + ".write (" + paramString2 + ", " + paramString3 + ");");
/*      */     }
/* 1033 */     else if ((paramSymtabEntry instanceof ValueEntry)) {
/* 1034 */       paramPrintWriter.println(Util.helperName(paramSymtabEntry, true) + ".write (" + paramString2 + ", " + paramString3 + ");");
/*      */     } else {
/* 1036 */       paramPrintWriter.println(Util.helperName(paramSymtabEntry, true) + ".write (" + paramString2 + ", " + paramString3 + ");");
/*      */     }
/*      */   }
/*      */ 
/*      */   private String writeInputStreamRead(String paramString, SymtabEntry paramSymtabEntry)
/*      */   {
/* 1044 */     String str = "";
/* 1045 */     if ((paramSymtabEntry instanceof PrimitiveEntry))
/*      */     {
/* 1047 */       if (paramSymtabEntry.name().equals("long long"))
/* 1048 */         str = paramString + ".read_longlong ()";
/* 1049 */       else if (paramSymtabEntry.name().equals("unsigned short"))
/* 1050 */         str = paramString + ".read_ushort ()";
/* 1051 */       else if (paramSymtabEntry.name().equals("unsigned long"))
/* 1052 */         str = paramString + ".read_ulong ()";
/* 1053 */       else if (paramSymtabEntry.name().equals("unsigned long long"))
/* 1054 */         str = paramString + ".read_ulonglong ()";
/*      */       else
/* 1056 */         str = paramString + ".read_" + paramSymtabEntry.name() + " ()";
/*      */     }
/* 1058 */     else if ((paramSymtabEntry instanceof StringEntry))
/* 1059 */       str = paramString + ".read_" + paramSymtabEntry.name() + " ()";
/*      */     else
/* 1061 */       str = Util.helperName(paramSymtabEntry, true) + ".read (" + paramString + ')';
/* 1062 */     return str;
/*      */   }
/*      */ 
/*      */   protected void writeMethodCall(String paramString)
/*      */   {
/* 1070 */     SymtabEntry localSymtabEntry = Util.typeOf(this.m.type());
/* 1071 */     if (localSymtabEntry == null)
/* 1072 */       this.stream.print(paramString + "this." + this.m.name() + " (");
/*      */     else
/* 1074 */       this.stream.print(paramString + "$result = this." + this.m.name() + " (");
/*      */   }
/*      */ 
/*      */   protected void writeCreateReply(String paramString)
/*      */   {
/* 1081 */     this.stream.println(paramString + "out = $rh.createReply();");
/*      */   }
/*      */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.corba.se.idl.toJavaPortable.MethodGen
 * JD-Core Version:    0.6.2
 */