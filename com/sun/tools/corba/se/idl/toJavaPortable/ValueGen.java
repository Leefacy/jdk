/*     */ package com.sun.tools.corba.se.idl.toJavaPortable;
/*     */ 
/*     */ import com.sun.tools.corba.se.idl.Comment;
/*     */ import com.sun.tools.corba.se.idl.GenFileStream;
/*     */ import com.sun.tools.corba.se.idl.InterfaceEntry;
/*     */ import com.sun.tools.corba.se.idl.InterfaceState;
/*     */ import com.sun.tools.corba.se.idl.MethodEntry;
/*     */ import com.sun.tools.corba.se.idl.PrimitiveEntry;
/*     */ import com.sun.tools.corba.se.idl.RepositoryID;
/*     */ import com.sun.tools.corba.se.idl.SequenceEntry;
/*     */ import com.sun.tools.corba.se.idl.StringEntry;
/*     */ import com.sun.tools.corba.se.idl.SymtabEntry;
/*     */ import com.sun.tools.corba.se.idl.TypedefEntry;
/*     */ import com.sun.tools.corba.se.idl.ValueBoxEntry;
/*     */ import com.sun.tools.corba.se.idl.ValueEntry;
/*     */ import java.io.PrintWriter;
/*     */ import java.util.Enumeration;
/*     */ import java.util.Hashtable;
/*     */ import java.util.Vector;
/*     */ 
/*     */ public class ValueGen
/*     */   implements com.sun.tools.corba.se.idl.ValueGen, JavaGenerator
/*     */ {
/* 761 */   protected int emit = 0;
/* 762 */   protected Factories factories = null;
/* 763 */   protected Hashtable symbolTable = null;
/* 764 */   protected ValueEntry v = null;
/* 765 */   protected PrintWriter stream = null;
/* 766 */   protected boolean explicitDefaultInit = false;
/*     */ 
/*     */   public void generate(Hashtable paramHashtable, ValueEntry paramValueEntry, PrintWriter paramPrintWriter)
/*     */   {
/*  87 */     this.symbolTable = paramHashtable;
/*  88 */     this.v = paramValueEntry;
/*  89 */     init();
/*     */ 
/*  91 */     openStream();
/*  92 */     if (this.stream == null)
/*  93 */       return;
/*  94 */     generateTie();
/*  95 */     generateHelper();
/*  96 */     generateHolder();
/*  97 */     writeHeading();
/*  98 */     writeBody();
/*  99 */     writeClosing();
/* 100 */     closeStream();
/*     */   }
/*     */ 
/*     */   protected void init()
/*     */   {
/* 108 */     this.emit = ((Arguments)Compile.compiler.arguments).emit;
/* 109 */     this.factories = ((Factories)Compile.compiler.factories());
/*     */   }
/*     */ 
/*     */   protected void openStream()
/*     */   {
/* 117 */     this.stream = Util.stream(this.v, ".java");
/*     */   }
/*     */ 
/*     */   protected void generateTie()
/*     */   {
/* 126 */     boolean bool = ((Arguments)Compile.compiler.arguments).TIEServer;
/* 127 */     if ((this.v.supports().size() > 0) && (bool))
/*     */     {
/* 129 */       Factories localFactories = (Factories)Compile.compiler.factories();
/* 130 */       localFactories.skeleton().generate(this.symbolTable, this.v);
/*     */     }
/*     */   }
/*     */ 
/*     */   protected void generateHelper()
/*     */   {
/* 139 */     ((Factories)Compile.compiler.factories()).helper().generate(this.symbolTable, this.v);
/*     */   }
/*     */ 
/*     */   protected void generateHolder()
/*     */   {
/* 147 */     ((Factories)Compile.compiler.factories()).holder().generate(this.symbolTable, this.v);
/*     */   }
/*     */ 
/*     */   protected void writeHeading()
/*     */   {
/* 155 */     Util.writePackage(this.stream, this.v);
/* 156 */     Util.writeProlog(this.stream, ((GenFileStream)this.stream).name());
/*     */ 
/* 158 */     if (this.v.comment() != null) {
/* 159 */       this.v.comment().generate("", this.stream);
/*     */     }
/* 161 */     if (this.v.isAbstract())
/*     */     {
/* 163 */       writeAbstract();
/* 164 */       return;
/*     */     }
/*     */ 
/* 167 */     this.stream.print("public class " + this.v.name());
/*     */ 
/* 170 */     SymtabEntry localSymtabEntry = (SymtabEntry)this.v.derivedFrom().elementAt(0);
/*     */ 
/* 173 */     String str = Util.javaName(localSymtabEntry);
/* 174 */     int i = 0;
/*     */ 
/* 176 */     if (str.equals("java.io.Serializable"))
/*     */     {
/* 179 */       this.stream.print(" implements org.omg.CORBA.portable.ValueBase");
/* 180 */       i = 1;
/*     */     }
/* 182 */     else if (!((ValueEntry)localSymtabEntry).isAbstract()) {
/* 183 */       this.stream.print(" extends " + str);
/*     */     }
/*     */ 
/* 186 */     for (int j = 0; j < this.v.derivedFrom().size(); j++) {
/* 187 */       localSymtabEntry = (SymtabEntry)this.v.derivedFrom().elementAt(j);
/* 188 */       if (((ValueEntry)localSymtabEntry).isAbstract())
/*     */       {
/* 190 */         if (i == 0)
/*     */         {
/* 192 */           this.stream.print(" implements ");
/* 193 */           i = 1;
/*     */         }
/*     */         else {
/* 196 */           this.stream.print(", ");
/* 197 */         }this.stream.print(Util.javaName(localSymtabEntry));
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 202 */     if (this.v.supports().size() > 0) {
/* 203 */       if (i == 0)
/*     */       {
/* 205 */         this.stream.print(" implements ");
/* 206 */         i = 1;
/*     */       }
/*     */       else {
/* 209 */         this.stream.print(", ");
/*     */       }
/* 211 */       InterfaceEntry localInterfaceEntry = (InterfaceEntry)this.v.supports().elementAt(0);
/*     */ 
/* 213 */       if (localInterfaceEntry.isAbstract())
/* 214 */         this.stream.print(Util.javaName(localInterfaceEntry));
/*     */       else {
/* 216 */         this.stream.print(Util.javaName(localInterfaceEntry) + "Operations");
/*     */       }
/*     */     }
/*     */ 
/* 220 */     if (this.v.isCustom()) {
/* 221 */       if (i == 0)
/*     */       {
/* 223 */         this.stream.print(" implements ");
/* 224 */         i = 1;
/*     */       }
/*     */       else {
/* 227 */         this.stream.print(", ");
/*     */       }
/* 229 */       this.stream.print("org.omg.CORBA.CustomMarshal ");
/*     */     }
/*     */ 
/* 232 */     this.stream.println();
/* 233 */     this.stream.println("{");
/*     */   }
/*     */ 
/*     */   protected void writeBody()
/*     */   {
/* 241 */     writeMembers();
/* 242 */     writeInitializers();
/* 243 */     writeConstructor();
/* 244 */     writeTruncatable();
/* 245 */     writeMethods();
/*     */   }
/*     */ 
/*     */   protected void writeClosing()
/*     */   {
/* 253 */     if (this.v.isAbstract())
/* 254 */       this.stream.println("} // interface " + this.v.name());
/*     */     else
/* 256 */       this.stream.println("} // class " + this.v.name());
/*     */   }
/*     */ 
/*     */   protected void closeStream()
/*     */   {
/* 264 */     this.stream.close();
/*     */   }
/*     */ 
/*     */   protected void writeConstructor()
/*     */   {
/* 273 */     if ((!this.v.isAbstract()) && (!this.explicitDefaultInit)) {
/* 274 */       this.stream.println("  protected " + this.v.name() + " () {}");
/* 275 */       this.stream.println();
/*     */     }
/*     */   }
/*     */ 
/*     */   protected void writeTruncatable()
/*     */   {
/* 285 */     if (!this.v.isAbstract()) {
/* 286 */       this.stream.println("  public String[] _truncatable_ids() {");
/* 287 */       this.stream.println("      return " + Util.helperName(this.v, true) + ".get_instance().get_truncatable_base_ids();");
/* 288 */       this.stream.println("  }");
/* 289 */       this.stream.println();
/*     */     }
/*     */   }
/*     */ 
/*     */   protected void writeMembers()
/*     */   {
/* 299 */     if (this.v.state() == null) {
/* 300 */       return;
/*     */     }
/* 302 */     for (int i = 0; i < this.v.state().size(); i++)
/*     */     {
/* 304 */       InterfaceState localInterfaceState = (InterfaceState)this.v.state().elementAt(i);
/* 305 */       TypedefEntry localTypedefEntry = localInterfaceState.entry;
/* 306 */       Util.fillInfo(localTypedefEntry);
/*     */ 
/* 308 */       if (localTypedefEntry.comment() != null) {
/* 309 */         localTypedefEntry.comment().generate(" ", this.stream);
/*     */       }
/* 311 */       String str = "  ";
/* 312 */       if (localInterfaceState.modifier == 2)
/* 313 */         str = "  public ";
/* 314 */       Util.writeInitializer(str, localTypedefEntry.name(), "", localTypedefEntry, this.stream);
/*     */     }
/*     */   }
/*     */ 
/*     */   protected void writeInitializers()
/*     */   {
/* 323 */     Vector localVector = this.v.initializers();
/* 324 */     if (localVector != null)
/*     */     {
/* 326 */       this.stream.println();
/* 327 */       for (int i = 0; i < localVector.size(); i++)
/*     */       {
/* 329 */         MethodEntry localMethodEntry = (MethodEntry)localVector.elementAt(i);
/* 330 */         localMethodEntry.valueMethod(true);
/* 331 */         ((MethodGen)localMethodEntry.generator()).interfaceMethod(this.symbolTable, localMethodEntry, this.stream);
/* 332 */         if (localMethodEntry.parameters().isEmpty())
/* 333 */           this.explicitDefaultInit = true;
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   protected void writeMethods()
/*     */   {
/* 349 */     Enumeration localEnumeration = this.v.contained().elements();
/*     */     Object localObject1;
/*     */     Object localObject2;
/* 350 */     while (localEnumeration.hasMoreElements())
/*     */     {
/* 352 */       localObject1 = (SymtabEntry)localEnumeration.nextElement();
/* 353 */       if ((localObject1 instanceof MethodEntry))
/*     */       {
/* 355 */         localObject2 = (MethodEntry)localObject1;
/* 356 */         ((MethodGen)((MethodEntry)localObject2).generator()).interfaceMethod(this.symbolTable, (MethodEntry)localObject2, this.stream);
/*     */       }
/*     */       else
/*     */       {
/* 361 */         if ((localObject1 instanceof TypedefEntry)) {
/* 362 */           ((SymtabEntry)localObject1).type().generate(this.symbolTable, this.stream);
/*     */         }
/*     */ 
/* 366 */         ((SymtabEntry)localObject1).generate(this.symbolTable, this.stream);
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 373 */     if (this.v.isAbstract())
/*     */       return;
/*     */     Object localObject3;
/*     */     MethodEntry localMethodEntry1;
/* 380 */     if (this.v.supports().size() > 0)
/*     */     {
/* 382 */       localObject1 = (InterfaceEntry)this.v.supports().elementAt(0);
/* 383 */       localObject2 = ((InterfaceEntry)localObject1).allMethods().elements();
/* 384 */       while (((Enumeration)localObject2).hasMoreElements())
/*     */       {
/* 386 */         localObject3 = (MethodEntry)((Enumeration)localObject2).nextElement();
/*     */ 
/* 390 */         localMethodEntry1 = (MethodEntry)((MethodEntry)localObject3).clone();
/* 391 */         localMethodEntry1.container(this.v);
/* 392 */         ((MethodGen)localMethodEntry1.generator()).interfaceMethod(this.symbolTable, localMethodEntry1, this.stream);
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 398 */     for (int i = 0; i < this.v.derivedFrom().size(); i++) {
/* 399 */       localObject2 = (ValueEntry)this.v.derivedFrom().elementAt(i);
/* 400 */       if (((ValueEntry)localObject2).isAbstract())
/*     */       {
/* 402 */         localObject3 = ((ValueEntry)localObject2).allMethods().elements();
/* 403 */         while (((Enumeration)localObject3).hasMoreElements())
/*     */         {
/* 405 */           localMethodEntry1 = (MethodEntry)((Enumeration)localObject3).nextElement();
/*     */ 
/* 409 */           MethodEntry localMethodEntry2 = (MethodEntry)localMethodEntry1.clone();
/* 410 */           localMethodEntry2.container(this.v);
/* 411 */           ((MethodGen)localMethodEntry2.generator()).interfaceMethod(this.symbolTable, localMethodEntry2, this.stream);
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   protected void writeStreamableMethods()
/*     */   {
/* 424 */     this.stream.println("  public void _read (org.omg.CORBA.portable.InputStream istream)");
/* 425 */     this.stream.println("  {");
/* 426 */     read(0, "    ", "this", this.v, this.stream);
/* 427 */     this.stream.println("  }");
/* 428 */     this.stream.println();
/* 429 */     this.stream.println("  public void _write (org.omg.CORBA.portable.OutputStream ostream)");
/* 430 */     this.stream.println("  {");
/* 431 */     write(0, "    ", "this", this.v, this.stream);
/* 432 */     this.stream.println("  }");
/* 433 */     this.stream.println();
/* 434 */     this.stream.println("  public org.omg.CORBA.TypeCode _type ()");
/* 435 */     this.stream.println("  {");
/* 436 */     this.stream.println("    return " + Util.helperName(this.v, false) + ".type ();");
/* 437 */     this.stream.println("  }");
/*     */   }
/*     */ 
/*     */   public int helperType(int paramInt, String paramString1, TCOffsets paramTCOffsets, String paramString2, SymtabEntry paramSymtabEntry, PrintWriter paramPrintWriter)
/*     */   {
/* 445 */     ValueEntry localValueEntry = (ValueEntry)paramSymtabEntry;
/* 446 */     Vector localVector = localValueEntry.state();
/* 447 */     int i = localVector == null ? 0 : localVector.size();
/* 448 */     String str1 = "_members" + paramInt++;
/* 449 */     String str2 = "_tcOf" + str1;
/*     */ 
/* 451 */     paramPrintWriter.println(paramString1 + "org.omg.CORBA.ValueMember[] " + str1 + " = new org.omg.CORBA.ValueMember[" + i + "];");
/*     */ 
/* 455 */     paramPrintWriter.println(paramString1 + "org.omg.CORBA.TypeCode " + str2 + " = null;");
/*     */ 
/* 458 */     String str3 = "_id";
/*     */ 
/* 461 */     for (int j = 0; j < i; j++)
/*     */     {
/* 463 */       InterfaceState localInterfaceState = (InterfaceState)localVector.elementAt(j);
/* 464 */       TypedefEntry localTypedefEntry = localInterfaceState.entry;
/* 465 */       SymtabEntry localSymtabEntry = Util.typeOf(localTypedefEntry);
/*     */       String str4;
/*     */       String str5;
/* 466 */       if (hasRepId(localTypedefEntry))
/*     */       {
/* 468 */         str4 = Util.helperName(localSymtabEntry, true) + ".id ()";
/* 469 */         if (((localSymtabEntry instanceof ValueEntry)) || ((localSymtabEntry instanceof ValueBoxEntry)))
/*     */         {
/* 471 */           str5 = "\"\"";
/*     */         }
/*     */         else {
/* 474 */           String str6 = localSymtabEntry.repositoryID().ID();
/* 475 */           str5 = '"' + str6.substring(str6.lastIndexOf(':') + 1) + '"';
/*     */         }
/*     */       }
/*     */       else
/*     */       {
/* 480 */         str4 = "\"\"";
/* 481 */         str5 = "\"\"";
/*     */       }
/*     */ 
/* 485 */       paramPrintWriter.println(paramString1 + "// ValueMember instance for " + localTypedefEntry.name());
/* 486 */       paramInt = ((JavaGenerator)localTypedefEntry.generator()).type(paramInt, paramString1, paramTCOffsets, str2, localTypedefEntry, paramPrintWriter);
/* 487 */       paramPrintWriter.println(paramString1 + str1 + "[" + j + "] = new org.omg.CORBA.ValueMember (" + '"' + localTypedefEntry
/* 488 */         .name() + "\", ");
/* 489 */       paramPrintWriter.println(paramString1 + "    " + str4 + ", ");
/* 490 */       paramPrintWriter.println(paramString1 + "    " + str3 + ", ");
/* 491 */       paramPrintWriter.println(paramString1 + "    " + str5 + ", ");
/* 492 */       paramPrintWriter.println(paramString1 + "    " + str2 + ", ");
/* 493 */       paramPrintWriter.println(paramString1 + "    " + "null, ");
/* 494 */       paramPrintWriter.println(paramString1 + "    " + "org.omg.CORBA." + (localInterfaceState.modifier == 2 ? "PUBLIC_MEMBER" : "PRIVATE_MEMBER") + ".value" + ");");
/*     */     }
/*     */ 
/* 499 */     paramPrintWriter.println(paramString1 + paramString2 + " = org.omg.CORBA.ORB.init ().create_value_tc (" + "_id, " + '"' + paramSymtabEntry
/* 501 */       .name() + "\", " + 
/* 502 */       getValueModifier(localValueEntry) + 
/* 502 */       ", " + 
/* 503 */       getConcreteBaseTypeCode(localValueEntry) + 
/* 503 */       ", " + str1 + ");");
/*     */ 
/* 507 */     return paramInt;
/*     */   }
/*     */ 
/*     */   public int type(int paramInt, String paramString1, TCOffsets paramTCOffsets, String paramString2, SymtabEntry paramSymtabEntry, PrintWriter paramPrintWriter) {
/* 511 */     paramPrintWriter.println(paramString1 + paramString2 + " = " + Util.helperName(paramSymtabEntry, true) + ".type ();");
/* 512 */     return paramInt;
/*     */   }
/*     */ 
/*     */   private static boolean hasRepId(SymtabEntry paramSymtabEntry)
/*     */   {
/* 520 */     SymtabEntry localSymtabEntry = Util.typeOf(paramSymtabEntry);
/*     */ 
/* 525 */     return (!(localSymtabEntry instanceof PrimitiveEntry)) && (!(localSymtabEntry instanceof StringEntry)) && ((!(localSymtabEntry instanceof TypedefEntry)) || 
/* 524 */       (((TypedefEntry)localSymtabEntry)
/* 524 */       .arrayInfo().isEmpty())) && (
/* 524 */       (!(localSymtabEntry instanceof TypedefEntry)) || 
/* 525 */       (!(paramSymtabEntry
/* 525 */       .type() instanceof SequenceEntry)));
/*     */   }
/*     */ 
/*     */   private static String getValueModifier(ValueEntry paramValueEntry)
/*     */   {
/* 530 */     String str = "NONE";
/* 531 */     if (paramValueEntry.isCustom())
/* 532 */       str = "CUSTOM";
/* 533 */     else if (paramValueEntry.isAbstract())
/* 534 */       str = "ABSTRACT";
/* 535 */     else if (paramValueEntry.isSafe())
/* 536 */       str = "TRUNCATABLE";
/* 537 */     return "org.omg.CORBA.VM_" + str + ".value";
/*     */   }
/*     */ 
/*     */   private static String getConcreteBaseTypeCode(ValueEntry paramValueEntry)
/*     */   {
/* 542 */     Vector localVector = paramValueEntry.derivedFrom();
/* 543 */     if (!paramValueEntry.isAbstract())
/*     */     {
/* 545 */       SymtabEntry localSymtabEntry = (SymtabEntry)paramValueEntry.derivedFrom().elementAt(0);
/* 546 */       if (!"ValueBase".equals(localSymtabEntry.name()))
/* 547 */         return Util.helperName(localSymtabEntry, true) + ".type ()";
/*     */     }
/* 549 */     return "null";
/*     */   }
/*     */ 
/*     */   public void helperRead(String paramString, SymtabEntry paramSymtabEntry, PrintWriter paramPrintWriter)
/*     */   {
/* 557 */     if (((ValueEntry)paramSymtabEntry).isAbstract())
/*     */     {
/* 559 */       paramPrintWriter.println("    throw new org.omg.CORBA.BAD_OPERATION (\"abstract value cannot be instantiated\");");
/*     */     }
/*     */     else
/*     */     {
/* 563 */       paramPrintWriter.println("    return (" + paramString + ") ((org.omg.CORBA_2_3.portable.InputStream) istream).read_value (get_instance());");
/*     */     }
/* 565 */     paramPrintWriter.println("  }");
/* 566 */     paramPrintWriter.println();
/*     */ 
/* 570 */     paramPrintWriter.println("  public java.io.Serializable read_value (org.omg.CORBA.portable.InputStream istream)");
/* 571 */     paramPrintWriter.println("  {");
/*     */ 
/* 574 */     if (((ValueEntry)paramSymtabEntry).isAbstract())
/*     */     {
/* 576 */       paramPrintWriter.println("    throw new org.omg.CORBA.BAD_OPERATION (\"abstract value cannot be instantiated\");");
/*     */     }
/* 579 */     else if (((ValueEntry)paramSymtabEntry).isCustom())
/*     */     {
/* 581 */       paramPrintWriter.println("    throw new org.omg.CORBA.BAD_OPERATION (\"custom values should use unmarshal()\");");
/*     */     }
/*     */     else
/*     */     {
/* 585 */       paramPrintWriter.println("    " + paramString + " value = new " + paramString + " ();");
/* 586 */       read(0, "    ", "value", paramSymtabEntry, paramPrintWriter);
/* 587 */       paramPrintWriter.println("    return value;");
/*     */     }
/* 589 */     paramPrintWriter.println("  }");
/* 590 */     paramPrintWriter.println();
/*     */ 
/* 598 */     paramPrintWriter.println("  public static void read (org.omg.CORBA.portable.InputStream istream, " + paramString + " value)");
/* 599 */     paramPrintWriter.println("  {");
/* 600 */     read(0, "    ", "value", paramSymtabEntry, paramPrintWriter);
/*     */   }
/*     */ 
/*     */   public int read(int paramInt, String paramString1, String paramString2, SymtabEntry paramSymtabEntry, PrintWriter paramPrintWriter)
/*     */   {
/* 606 */     Vector localVector = ((ValueEntry)paramSymtabEntry).derivedFrom();
/* 607 */     if ((localVector != null) && (localVector.size() != 0))
/*     */     {
/* 609 */       localObject = (ValueEntry)localVector.elementAt(0);
/* 610 */       if (localObject == null) {
/* 611 */         return paramInt;
/*     */       }
/* 613 */       if (!Util.javaQualifiedName((SymtabEntry)localObject).equals("java.io.Serializable")) {
/* 614 */         paramPrintWriter.println(paramString1 + Util.helperName((SymtabEntry)localObject, true) + ".read (istream, value);");
/*     */       }
/*     */     }
/* 617 */     Object localObject = ((ValueEntry)paramSymtabEntry).state();
/* 618 */     int i = localObject == null ? 0 : ((Vector)localObject).size();
/*     */ 
/* 620 */     for (int j = 0; j < i; j++)
/*     */     {
/* 622 */       TypedefEntry localTypedefEntry = ((InterfaceState)((Vector)localObject).elementAt(j)).entry;
/* 623 */       String str1 = localTypedefEntry.name();
/* 624 */       SymtabEntry localSymtabEntry = localTypedefEntry.type();
/*     */ 
/* 626 */       if (((localSymtabEntry instanceof PrimitiveEntry)) || ((localSymtabEntry instanceof TypedefEntry)) || ((localSymtabEntry instanceof SequenceEntry)) || ((localSymtabEntry instanceof StringEntry)) || 
/* 630 */         (!localTypedefEntry
/* 630 */         .arrayInfo().isEmpty())) {
/* 631 */         paramInt = ((JavaGenerator)localTypedefEntry.generator()).read(paramInt, paramString1, paramString2 + '.' + str1, localTypedefEntry, paramPrintWriter);
/* 632 */       } else if ((localSymtabEntry instanceof ValueEntry))
/*     */       {
/* 634 */         String str2 = Util.javaQualifiedName(localSymtabEntry);
/* 635 */         if ((localSymtabEntry instanceof ValueBoxEntry))
/*     */         {
/* 638 */           str2 = Util.javaName(localSymtabEntry);
/* 639 */         }paramPrintWriter.println("    " + paramString2 + '.' + str1 + " = (" + str2 + ") ((org.omg.CORBA_2_3.portable.InputStream)istream).read_value (" + 
/* 640 */           Util.helperName(localSymtabEntry, true) + 
/* 640 */           ".get_instance ());");
/*     */       }
/*     */       else
/*     */       {
/* 644 */         paramPrintWriter.println(paramString1 + paramString2 + '.' + str1 + " = " + 
/* 645 */           Util.helperName(localSymtabEntry, true) + 
/* 645 */           ".read (istream);");
/*     */       }
/*     */     }
/* 648 */     return paramInt;
/*     */   }
/*     */ 
/*     */   public void helperWrite(SymtabEntry paramSymtabEntry, PrintWriter paramPrintWriter)
/*     */   {
/* 655 */     paramPrintWriter.println("    ((org.omg.CORBA_2_3.portable.OutputStream) ostream).write_value (value, get_instance());");
/* 656 */     paramPrintWriter.println("  }");
/* 657 */     paramPrintWriter.println();
/*     */ 
/* 662 */     if (!((ValueEntry)paramSymtabEntry).isCustom())
/*     */     {
/* 664 */       paramPrintWriter.println("  public static void _write (org.omg.CORBA.portable.OutputStream ostream, " + Util.javaName(paramSymtabEntry) + " value)");
/* 665 */       paramPrintWriter.println("  {");
/* 666 */       write(0, "    ", "value", paramSymtabEntry, paramPrintWriter);
/* 667 */       paramPrintWriter.println("  }");
/* 668 */       paramPrintWriter.println();
/*     */     }
/*     */ 
/* 672 */     paramPrintWriter.println("  public void write_value (org.omg.CORBA.portable.OutputStream ostream, java.io.Serializable obj)");
/* 673 */     paramPrintWriter.println("  {");
/*     */ 
/* 676 */     if (((ValueEntry)paramSymtabEntry).isCustom())
/*     */     {
/* 678 */       paramPrintWriter.println("    throw new org.omg.CORBA.BAD_OPERATION (\"custom values should use marshal()\");");
/*     */     }
/*     */     else {
/* 681 */       String str = Util.javaName(paramSymtabEntry);
/* 682 */       paramPrintWriter.println("    _write (ostream, (" + str + ") obj);");
/*     */     }
/*     */   }
/*     */ 
/*     */   public int write(int paramInt, String paramString1, String paramString2, SymtabEntry paramSymtabEntry, PrintWriter paramPrintWriter)
/*     */   {
/* 690 */     Vector localVector = ((ValueEntry)paramSymtabEntry).derivedFrom();
/* 691 */     if ((localVector != null) && (localVector.size() != 0))
/*     */     {
/* 693 */       localObject = (ValueEntry)localVector.elementAt(0);
/* 694 */       if (localObject == null) {
/* 695 */         return paramInt;
/*     */       }
/*     */ 
/* 698 */       if (!Util.javaQualifiedName((SymtabEntry)localObject).equals("java.io.Serializable")) {
/* 699 */         paramPrintWriter.println(paramString1 + Util.helperName((SymtabEntry)localObject, true) + "._write (ostream, value);");
/*     */       }
/*     */     }
/* 702 */     Object localObject = ((ValueEntry)paramSymtabEntry).state();
/* 703 */     int i = localObject == null ? 0 : ((Vector)localObject).size();
/* 704 */     for (int j = 0; j < i; j++)
/*     */     {
/* 706 */       TypedefEntry localTypedefEntry = ((InterfaceState)((Vector)localObject).elementAt(j)).entry;
/* 707 */       String str = localTypedefEntry.name();
/* 708 */       SymtabEntry localSymtabEntry = localTypedefEntry.type();
/*     */ 
/* 710 */       if (((localSymtabEntry instanceof PrimitiveEntry)) || ((localSymtabEntry instanceof TypedefEntry)) || ((localSymtabEntry instanceof SequenceEntry)) || ((localSymtabEntry instanceof StringEntry)) || 
/* 714 */         (!localTypedefEntry
/* 714 */         .arrayInfo().isEmpty()))
/* 715 */         paramInt = ((JavaGenerator)localTypedefEntry.generator()).write(paramInt, paramString1, paramString2 + '.' + str, localTypedefEntry, paramPrintWriter);
/*     */       else {
/* 717 */         paramPrintWriter.println(paramString1 + Util.helperName(localSymtabEntry, true) + ".write (ostream, " + paramString2 + '.' + str + ");");
/*     */       }
/*     */     }
/*     */ 
/* 721 */     return paramInt;
/*     */   }
/*     */ 
/*     */   protected void writeAbstract()
/*     */   {
/* 729 */     this.stream.print("public interface " + this.v.name());
/*     */     SymtabEntry localSymtabEntry;
/* 733 */     if (this.v.derivedFrom().size() == 0) {
/* 734 */       this.stream.print(" extends org.omg.CORBA.portable.ValueBase");
/*     */     }
/*     */     else
/*     */     {
/* 739 */       for (int i = 0; i < this.v.derivedFrom().size(); i++)
/*     */       {
/* 741 */         if (i == 0)
/* 742 */           this.stream.print(" extends ");
/*     */         else
/* 744 */           this.stream.print(", ");
/* 745 */         localSymtabEntry = (SymtabEntry)this.v.derivedFrom().elementAt(i);
/* 746 */         this.stream.print(Util.javaName(localSymtabEntry));
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 751 */     if (this.v.supports().size() > 0)
/*     */     {
/* 753 */       this.stream.print(", ");
/* 754 */       localSymtabEntry = (SymtabEntry)this.v.supports().elementAt(0);
/* 755 */       this.stream.print(Util.javaName(localSymtabEntry));
/*     */     }
/* 757 */     this.stream.println();
/* 758 */     this.stream.println("{");
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.corba.se.idl.toJavaPortable.ValueGen
 * JD-Core Version:    0.6.2
 */