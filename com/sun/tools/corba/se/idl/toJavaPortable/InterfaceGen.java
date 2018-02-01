/*     */ package com.sun.tools.corba.se.idl.toJavaPortable;
/*     */ 
/*     */ import com.sun.tools.corba.se.idl.Comment;
/*     */ import com.sun.tools.corba.se.idl.ConstEntry;
/*     */ import com.sun.tools.corba.se.idl.GenFileStream;
/*     */ import com.sun.tools.corba.se.idl.InterfaceEntry;
/*     */ import com.sun.tools.corba.se.idl.MethodEntry;
/*     */ import com.sun.tools.corba.se.idl.SymtabEntry;
/*     */ import java.io.PrintWriter;
/*     */ import java.util.Enumeration;
/*     */ import java.util.Hashtable;
/*     */ import java.util.Vector;
/*     */ 
/*     */ public class InterfaceGen
/*     */   implements com.sun.tools.corba.se.idl.InterfaceGen, JavaGenerator
/*     */ {
/* 851 */   protected int emit = 0;
/* 852 */   protected Factories factories = null;
/*     */ 
/* 854 */   protected Hashtable symbolTable = null;
/* 855 */   protected InterfaceEntry i = null;
/* 856 */   protected PrintWriter stream = null;
/*     */   protected static final int SIGNATURE = 1;
/*     */   protected static final int OPERATIONS = 2;
/* 861 */   protected int intfType = 0;
/*     */ 
/*     */   public void generate(Hashtable paramHashtable, InterfaceEntry paramInterfaceEntry, PrintWriter paramPrintWriter)
/*     */   {
/*  90 */     if (!isPseudo(paramInterfaceEntry))
/*     */     {
/*  92 */       this.symbolTable = paramHashtable;
/*  93 */       this.i = paramInterfaceEntry;
/*  94 */       init();
/*     */ 
/* 103 */       if (!paramInterfaceEntry.isLocalSignature())
/*     */       {
/* 105 */         if (!paramInterfaceEntry.isLocal())
/*     */         {
/* 108 */           generateSkeleton();
/*     */ 
/* 116 */           Arguments localArguments = (Arguments)Compile.compiler.arguments;
/* 117 */           if ((localArguments.TIEServer == true) && (localArguments.emit == 3))
/*     */           {
/* 120 */             localArguments.TIEServer = false;
/*     */ 
/* 122 */             generateSkeleton();
/*     */ 
/* 124 */             localArguments.TIEServer = true;
/*     */           }
/* 126 */           generateStub();
/*     */         }
/* 128 */         generateHolder();
/* 129 */         generateHelper();
/*     */       }
/* 131 */       this.intfType = 1;
/* 132 */       generateInterface();
/* 133 */       this.intfType = 2;
/* 134 */       generateInterface();
/* 135 */       this.intfType = 0;
/*     */     }
/*     */   }
/*     */ 
/*     */   protected void init()
/*     */   {
/* 144 */     this.emit = ((Arguments)Compile.compiler.arguments).emit;
/* 145 */     this.factories = ((Factories)Compile.compiler.factories());
/*     */   }
/*     */ 
/*     */   protected void generateSkeleton()
/*     */   {
/* 158 */     if (this.emit != 1)
/* 159 */       this.factories.skeleton().generate(this.symbolTable, this.i);
/*     */   }
/*     */ 
/*     */   protected void generateStub()
/*     */   {
/* 169 */     if (this.emit != 2)
/* 170 */       this.factories.stub().generate(this.symbolTable, this.i);
/*     */   }
/*     */ 
/*     */   protected void generateHelper()
/*     */   {
/* 178 */     if (this.emit != 2)
/* 179 */       this.factories.helper().generate(this.symbolTable, this.i);
/*     */   }
/*     */ 
/*     */   protected void generateHolder()
/*     */   {
/* 187 */     if (this.emit != 2)
/* 188 */       this.factories.holder().generate(this.symbolTable, this.i);
/*     */   }
/*     */ 
/*     */   protected void generateInterface()
/*     */   {
/* 207 */     init();
/* 208 */     openStream();
/* 209 */     if (this.stream == null)
/* 210 */       return;
/* 211 */     writeHeading();
/* 212 */     if (this.intfType == 2)
/* 213 */       writeOperationsBody();
/* 214 */     if (this.intfType == 1)
/* 215 */       writeSignatureBody();
/* 216 */     writeClosing();
/* 217 */     closeStream();
/*     */   }
/*     */ 
/*     */   protected void openStream()
/*     */   {
/* 225 */     if ((this.i.isAbstract()) || (this.intfType == 1))
/* 226 */       this.stream = Util.stream(this.i, ".java");
/* 227 */     else if (this.intfType == 2)
/* 228 */       this.stream = Util.stream(this.i, "Operations.java");
/*     */   }
/*     */ 
/*     */   protected void writeHeading()
/*     */   {
/* 236 */     Util.writePackage(this.stream, this.i, (short)0);
/* 237 */     Util.writeProlog(this.stream, ((GenFileStream)this.stream).name());
/*     */ 
/* 240 */     if (this.i.comment() != null) {
/* 241 */       this.i.comment().generate("", this.stream);
/*     */     }
/* 243 */     String str = this.i.name();
/*     */ 
/* 271 */     if (this.intfType == 1)
/* 272 */       writeSignatureHeading();
/* 273 */     else if (this.intfType == 2) {
/* 274 */       writeOperationsHeading();
/*     */     }
/*     */ 
/* 277 */     this.stream.println();
/* 278 */     this.stream.println('{');
/*     */   }
/*     */ 
/*     */   protected void writeSignatureHeading()
/*     */   {
/* 286 */     String str = this.i.name();
/* 287 */     this.stream.print("public interface " + str + " extends " + str + "Operations, ");
/* 288 */     int j = 1;
/* 289 */     int k = 0;
/* 290 */     for (int m = 0; m < this.i.derivedFrom().size(); m++)
/*     */     {
/* 292 */       if (j != 0)
/* 293 */         j = 0;
/*     */       else
/* 295 */         this.stream.print(", ");
/* 296 */       InterfaceEntry localInterfaceEntry = (InterfaceEntry)this.i.derivedFrom().elementAt(m);
/* 297 */       this.stream.print(Util.javaName(localInterfaceEntry));
/* 298 */       if (!localInterfaceEntry.isAbstract()) {
/* 299 */         k = 1;
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 304 */     if (k == 0) {
/* 305 */       this.stream.print(", org.omg.CORBA.Object, org.omg.CORBA.portable.IDLEntity ");
/*     */     }
/* 310 */     else if (this.i.derivedFrom().size() == 1)
/* 311 */       this.stream.print(", org.omg.CORBA.portable.IDLEntity ");
/*     */   }
/*     */ 
/*     */   protected void writeOperationsHeading()
/*     */   {
/* 320 */     this.stream.print("public interface " + this.i.name());
/* 321 */     if (!this.i.isAbstract()) {
/* 322 */       this.stream.print("Operations ");
/*     */     }
/* 326 */     else if (this.i.derivedFrom().size() == 0) {
/* 327 */       this.stream.print(" extends org.omg.CORBA.portable.IDLEntity");
/*     */     }
/*     */ 
/* 330 */     int j = 1;
/* 331 */     for (int k = 0; k < this.i.derivedFrom().size(); k++)
/*     */     {
/* 333 */       InterfaceEntry localInterfaceEntry = (InterfaceEntry)this.i.derivedFrom().elementAt(k);
/* 334 */       String str = Util.javaName(localInterfaceEntry);
/*     */ 
/* 337 */       if (!str.equals("org.omg.CORBA.Object"))
/*     */       {
/* 340 */         if (j != 0)
/*     */         {
/* 342 */           j = 0;
/* 343 */           this.stream.print(" extends ");
/*     */         }
/*     */         else {
/* 346 */           this.stream.print(", ");
/*     */         }
/*     */ 
/* 350 */         if ((localInterfaceEntry.isAbstract()) || (this.i.isAbstract()))
/* 351 */           this.stream.print(str);
/*     */         else
/* 353 */           this.stream.print(str + "Operations");
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   protected void writeOperationsBody()
/*     */   {
/* 364 */     Enumeration localEnumeration = this.i.contained().elements();
/* 365 */     while (localEnumeration.hasMoreElements())
/*     */     {
/* 367 */       SymtabEntry localSymtabEntry = (SymtabEntry)localEnumeration.nextElement();
/* 368 */       if ((localSymtabEntry instanceof MethodEntry))
/*     */       {
/* 370 */         MethodEntry localMethodEntry = (MethodEntry)localSymtabEntry;
/* 371 */         ((MethodGen)localMethodEntry.generator()).interfaceMethod(this.symbolTable, localMethodEntry, this.stream);
/*     */       }
/* 374 */       else if (!(localSymtabEntry instanceof ConstEntry)) {
/* 375 */         localSymtabEntry.generate(this.symbolTable, this.stream);
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   protected void writeSignatureBody()
/*     */   {
/* 385 */     Enumeration localEnumeration = this.i.contained().elements();
/* 386 */     while (localEnumeration.hasMoreElements())
/*     */     {
/* 388 */       SymtabEntry localSymtabEntry = (SymtabEntry)localEnumeration.nextElement();
/* 389 */       if ((localSymtabEntry instanceof ConstEntry))
/* 390 */         localSymtabEntry.generate(this.symbolTable, this.stream);
/*     */     }
/*     */   }
/*     */ 
/*     */   protected void writeClosing()
/*     */   {
/* 399 */     String str = this.i.name();
/* 400 */     if ((!this.i.isAbstract()) && (this.intfType == 2))
/* 401 */       str = str + "Operations";
/* 402 */     this.stream.println("} // interface " + str);
/*     */   }
/*     */ 
/*     */   protected void closeStream()
/*     */   {
/* 410 */     this.stream.close();
/*     */   }
/*     */ 
/*     */   public int helperType(int paramInt, String paramString1, TCOffsets paramTCOffsets, String paramString2, SymtabEntry paramSymtabEntry, PrintWriter paramPrintWriter)
/*     */   {
/* 441 */     InterfaceEntry localInterfaceEntry = (InterfaceEntry)paramSymtabEntry;
/* 442 */     paramTCOffsets.set(paramSymtabEntry);
/* 443 */     if (paramSymtabEntry.fullName().equals("org/omg/CORBA/Object")) {
/* 444 */       paramPrintWriter.println(paramString1 + paramString2 + " = org.omg.CORBA.ORB.init ().get_primitive_tc (org.omg.CORBA.TCKind.tk_objref);");
/*     */     }
/*     */     else
/* 447 */       paramPrintWriter.println(paramString1 + paramString2 + " = org.omg.CORBA.ORB.init ().create_interface_tc (" + 
/* 450 */         Util.helperName(localInterfaceEntry, true) + 
/* 450 */         ".id (), " + '"' + 
/* 451 */         Util.stripLeadingUnderscores(paramSymtabEntry
/* 451 */         .name()) + "\");");
/* 452 */     return paramInt;
/*     */   }
/*     */ 
/*     */   public int type(int paramInt, String paramString1, TCOffsets paramTCOffsets, String paramString2, SymtabEntry paramSymtabEntry, PrintWriter paramPrintWriter) {
/* 456 */     paramPrintWriter.println(paramString1 + paramString2 + " = " + Util.helperName(paramSymtabEntry, true) + ".type ();");
/* 457 */     return paramInt;
/*     */   }
/*     */ 
/*     */   public void helperRead(String paramString, SymtabEntry paramSymtabEntry, PrintWriter paramPrintWriter)
/*     */   {
/* 479 */     InterfaceEntry localInterfaceEntry = (InterfaceEntry)paramSymtabEntry;
/* 480 */     if (localInterfaceEntry.isAbstract())
/* 481 */       paramPrintWriter.println("    return narrow (((org.omg.CORBA_2_3.portable.InputStream)istream).read_abstract_interface (_" + localInterfaceEntry.name() + "Stub.class));");
/*     */     else
/* 483 */       paramPrintWriter.println("    return narrow (istream.read_Object (_" + localInterfaceEntry.name() + "Stub.class));");
/*     */   }
/*     */ 
/*     */   public void helperWrite(SymtabEntry paramSymtabEntry, PrintWriter paramPrintWriter)
/*     */   {
/* 499 */     write(0, "    ", "value", paramSymtabEntry, paramPrintWriter);
/*     */   }
/*     */ 
/*     */   public int read(int paramInt, String paramString1, String paramString2, SymtabEntry paramSymtabEntry, PrintWriter paramPrintWriter)
/*     */   {
/* 521 */     InterfaceEntry localInterfaceEntry = (InterfaceEntry)paramSymtabEntry;
/* 522 */     if (paramSymtabEntry.fullName().equals("org/omg/CORBA/Object"))
/* 523 */       paramPrintWriter.println(paramString1 + paramString2 + " = istream.read_Object (_" + localInterfaceEntry.name() + "Stub.class);");
/*     */     else
/* 525 */       paramPrintWriter.println(paramString1 + paramString2 + " = " + Util.helperName(paramSymtabEntry, false) + ".narrow (istream.read_Object (_" + localInterfaceEntry.name() + "Stub.class));");
/* 526 */     return paramInt;
/*     */   }
/*     */ 
/*     */   public int write(int paramInt, String paramString1, String paramString2, SymtabEntry paramSymtabEntry, PrintWriter paramPrintWriter)
/*     */   {
/* 548 */     InterfaceEntry localInterfaceEntry = (InterfaceEntry)paramSymtabEntry;
/* 549 */     if (localInterfaceEntry.isAbstract())
/* 550 */       paramPrintWriter.println(paramString1 + "((org.omg.CORBA_2_3.portable.OutputStream)ostream).write_abstract_interface ((java.lang.Object) " + paramString2 + ");");
/*     */     else
/* 552 */       paramPrintWriter.println(paramString1 + "ostream.write_Object ((org.omg.CORBA.Object) " + paramString2 + ");");
/* 553 */     return paramInt;
/*     */   }
/*     */ 
/*     */   private boolean isPseudo(InterfaceEntry paramInterfaceEntry)
/*     */   {
/* 816 */     String str = paramInterfaceEntry.fullName();
/* 817 */     if (str.equalsIgnoreCase("CORBA/TypeCode"))
/* 818 */       return true;
/* 819 */     if (str.equalsIgnoreCase("CORBA/Principal"))
/* 820 */       return true;
/* 821 */     if (str.equalsIgnoreCase("CORBA/ORB"))
/* 822 */       return true;
/* 823 */     if (str.equalsIgnoreCase("CORBA/Any"))
/* 824 */       return true;
/* 825 */     if (str.equalsIgnoreCase("CORBA/Context"))
/* 826 */       return true;
/* 827 */     if (str.equalsIgnoreCase("CORBA/ContextList"))
/* 828 */       return true;
/* 829 */     if (str.equalsIgnoreCase("CORBA/DynamicImplementation"))
/* 830 */       return true;
/* 831 */     if (str.equalsIgnoreCase("CORBA/Environment"))
/* 832 */       return true;
/* 833 */     if (str.equalsIgnoreCase("CORBA/ExceptionList"))
/* 834 */       return true;
/* 835 */     if (str.equalsIgnoreCase("CORBA/NVList"))
/* 836 */       return true;
/* 837 */     if (str.equalsIgnoreCase("CORBA/NamedValue"))
/* 838 */       return true;
/* 839 */     if (str.equalsIgnoreCase("CORBA/Request"))
/* 840 */       return true;
/* 841 */     if (str.equalsIgnoreCase("CORBA/ServerRequest"))
/* 842 */       return true;
/* 843 */     if (str.equalsIgnoreCase("CORBA/UserException"))
/* 844 */       return true;
/* 845 */     return false;
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.corba.se.idl.toJavaPortable.InterfaceGen
 * JD-Core Version:    0.6.2
 */