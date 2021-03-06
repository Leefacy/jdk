/*     */ package com.sun.tools.corba.se.idl.toJavaPortable;
/*     */ 
/*     */ import com.sun.tools.corba.se.idl.AttributeEntry;
/*     */ import com.sun.tools.corba.se.idl.Comment;
/*     */ import com.sun.tools.corba.se.idl.GenFileStream;
/*     */ import com.sun.tools.corba.se.idl.InterfaceEntry;
/*     */ import com.sun.tools.corba.se.idl.MethodEntry;
/*     */ import com.sun.tools.corba.se.idl.RepositoryID;
/*     */ import com.sun.tools.corba.se.idl.SymtabEntry;
/*     */ import java.io.File;
/*     */ import java.io.PrintWriter;
/*     */ import java.util.Enumeration;
/*     */ import java.util.Hashtable;
/*     */ import java.util.Vector;
/*     */ 
/*     */ public class Stub
/*     */   implements AuxGen
/*     */ {
/* 371 */   protected Hashtable symbolTable = null;
/* 372 */   protected InterfaceEntry i = null;
/* 373 */   protected PrintWriter stream = null;
/*     */ 
/* 376 */   protected Vector methodList = null;
/* 377 */   protected String classSuffix = "";
/* 378 */   protected boolean localStub = false;
/* 379 */   private boolean isAbstract = false;
/*     */ 
/*     */   public void generate(Hashtable paramHashtable, SymtabEntry paramSymtabEntry)
/*     */   {
/*  69 */     this.symbolTable = paramHashtable;
/*  70 */     this.i = ((InterfaceEntry)paramSymtabEntry);
/*  71 */     this.localStub = this.i.isLocalServant();
/*  72 */     this.isAbstract = this.i.isAbstract();
/*  73 */     init();
/*     */ 
/*  75 */     openStream();
/*  76 */     if (this.stream == null)
/*  77 */       return;
/*  78 */     writeHeading();
/*  79 */     writeBody();
/*  80 */     writeClosing();
/*  81 */     closeStream();
/*     */   }
/*     */ 
/*     */   protected void init()
/*     */   {
/*  89 */     this.classSuffix = "Stub";
/*     */   }
/*     */ 
/*     */   protected void openStream()
/*     */   {
/*  97 */     String str1 = '_' + this.i.name() + this.classSuffix;
/*  98 */     String str2 = Util.containerFullName(this.i.container());
/*  99 */     if ((str2 != null) && (!str2.equals("")))
/*     */     {
/* 101 */       Util.mkdir(str2);
/* 102 */       str1 = str2 + '/' + str1;
/*     */     }
/* 104 */     this.stream = Util.getStream(str1.replace('/', File.separatorChar) + ".java", this.i);
/*     */   }
/*     */ 
/*     */   protected void writeHeading()
/*     */   {
/* 112 */     Util.writePackage(this.stream, this.i, (short)1);
/* 113 */     Util.writeProlog(this.stream, ((GenFileStream)this.stream).name());
/*     */ 
/* 116 */     if (this.i.comment() != null) {
/* 117 */       this.i.comment().generate("", this.stream);
/*     */     }
/* 119 */     writeClassDeclaration();
/* 120 */     this.stream.println('{');
/*     */   }
/*     */ 
/*     */   protected void writeClassDeclaration()
/*     */   {
/* 128 */     this.stream.print("public class _" + this.i.name() + this.classSuffix + " extends org.omg.CORBA.portable.ObjectImpl");
/* 129 */     this.stream.println(" implements " + Util.javaName(this.i));
/*     */   }
/*     */ 
/*     */   protected void writeBody()
/*     */   {
/* 141 */     writeCtors();
/* 142 */     buildMethodList();
/* 143 */     writeMethods();
/* 144 */     writeCORBAObjectMethods();
/* 145 */     writeSerializationMethods();
/*     */   }
/*     */ 
/*     */   protected void writeClosing()
/*     */   {
/* 153 */     this.stream.println("} // class _" + this.i.name() + this.classSuffix);
/*     */   }
/*     */ 
/*     */   protected void closeStream()
/*     */   {
/* 161 */     this.stream.close();
/*     */   }
/*     */ 
/*     */   protected void writeCtors()
/*     */   {
/* 169 */     String str = this.i.name();
/*     */ 
/* 197 */     if (this.localStub) {
/* 198 */       this.stream.println("  final public static java.lang.Class _opsClass = " + str + "Operations.class;");
/*     */ 
/* 200 */       this.stream.println();
/*     */     }
/* 202 */     this.stream.println();
/*     */   }
/*     */ 
/*     */   protected void buildMethodList()
/*     */   {
/* 211 */     this.methodList = new Vector();
/*     */ 
/* 213 */     buildMethodList(this.i);
/*     */   }
/*     */ 
/*     */   private void buildMethodList(InterfaceEntry paramInterfaceEntry)
/*     */   {
/* 222 */     Enumeration localEnumeration1 = paramInterfaceEntry.methods().elements();
/* 223 */     while (localEnumeration1.hasMoreElements()) {
/* 224 */       addMethod((MethodEntry)localEnumeration1.nextElement());
/*     */     }
/*     */ 
/* 227 */     Enumeration localEnumeration2 = paramInterfaceEntry.derivedFrom().elements();
/* 228 */     while (localEnumeration2.hasMoreElements())
/*     */     {
/* 230 */       InterfaceEntry localInterfaceEntry = (InterfaceEntry)localEnumeration2.nextElement();
/* 231 */       if (!localInterfaceEntry.name().equals("Object"))
/* 232 */         buildMethodList(localInterfaceEntry);
/*     */     }
/*     */   }
/*     */ 
/*     */   private void addMethod(MethodEntry paramMethodEntry)
/*     */   {
/* 241 */     if (!this.methodList.contains(paramMethodEntry))
/* 242 */       this.methodList.addElement(paramMethodEntry);
/*     */   }
/*     */ 
/*     */   protected void writeMethods()
/*     */   {
/* 252 */     int j = this.methodList.size();
/* 253 */     Enumeration localEnumeration = this.methodList.elements();
/* 254 */     while (localEnumeration.hasMoreElements())
/*     */     {
/* 256 */       Object localObject = localEnumeration.nextElement();
/* 257 */       if (((localObject instanceof AttributeEntry)) && (!((AttributeEntry)localObject).readOnly())) {
/* 258 */         j++;
/*     */       }
/*     */     }
/* 261 */     if ((((Arguments)Compile.compiler.arguments).LocalOptimization) && (!this.isAbstract))
/*     */     {
/* 264 */       this.stream.println("    final public static java.lang.Class _opsClass =");
/* 265 */       this.stream.println("        " + this.i.name() + "Operations.class;");
/*     */     }
/*     */ 
/* 269 */     int k = 0;
/* 270 */     for (int m = 0; m < this.methodList.size(); m++)
/*     */     {
/* 272 */       MethodEntry localMethodEntry = (MethodEntry)this.methodList.elementAt(m);
/* 273 */       if (!this.localStub)
/* 274 */         ((MethodGen)localMethodEntry.generator()).stub(this.i.name(), this.isAbstract, this.symbolTable, localMethodEntry, this.stream, k);
/*     */       else {
/* 276 */         ((MethodGen)localMethodEntry.generator()).localstub(this.symbolTable, localMethodEntry, this.stream, k, this.i);
/*     */       }
/* 278 */       if (((localMethodEntry instanceof AttributeEntry)) && (!((AttributeEntry)localMethodEntry).readOnly()))
/* 279 */         k += 2;
/*     */       else
/* 281 */         k++;
/*     */     }
/*     */   }
/*     */ 
/*     */   private void buildIDList(InterfaceEntry paramInterfaceEntry, Vector paramVector)
/*     */   {
/* 290 */     if (!paramInterfaceEntry.fullName().equals("org/omg/CORBA/Object"))
/*     */     {
/* 292 */       String str = Util.stripLeadingUnderscoresFromID(paramInterfaceEntry.repositoryID().ID());
/* 293 */       if (!paramVector.contains(str))
/* 294 */         paramVector.addElement(str);
/* 295 */       Enumeration localEnumeration = paramInterfaceEntry.derivedFrom().elements();
/* 296 */       while (localEnumeration.hasMoreElements())
/* 297 */         buildIDList((InterfaceEntry)localEnumeration.nextElement(), paramVector);
/*     */     }
/*     */   }
/*     */ 
/*     */   private void writeIDs()
/*     */   {
/* 306 */     Vector localVector = new Vector();
/* 307 */     buildIDList(this.i, localVector);
/* 308 */     Enumeration localEnumeration = localVector.elements();
/* 309 */     int j = 1;
/* 310 */     while (localEnumeration.hasMoreElements())
/*     */     {
/* 312 */       if (j != 0)
/* 313 */         j = 0;
/*     */       else
/* 315 */         this.stream.println(", ");
/* 316 */       this.stream.print("    \"" + (String)localEnumeration.nextElement() + '"');
/*     */     }
/*     */   }
/*     */ 
/*     */   protected void writeCORBAObjectMethods()
/*     */   {
/* 325 */     this.stream.println("  // Type-specific CORBA::Object operations");
/* 326 */     this.stream.println("  private static String[] __ids = {");
/* 327 */     writeIDs();
/* 328 */     this.stream.println("};");
/* 329 */     this.stream.println();
/* 330 */     this.stream.println("  public String[] _ids ()");
/* 331 */     this.stream.println("  {");
/* 332 */     this.stream.println("    return (String[])__ids.clone ();");
/* 333 */     this.stream.println("  }");
/* 334 */     this.stream.println();
/*     */   }
/*     */ 
/*     */   protected void writeSerializationMethods()
/*     */   {
/* 342 */     this.stream.println("  private void readObject (java.io.ObjectInputStream s) throws java.io.IOException");
/* 343 */     this.stream.println("  {");
/* 344 */     this.stream.println("     String str = s.readUTF ();");
/* 345 */     this.stream.println("     String[] args = null;");
/* 346 */     this.stream.println("     java.util.Properties props = null;");
/* 347 */     this.stream.println("     org.omg.CORBA.ORB orb = org.omg.CORBA.ORB.init (args, props);");
/* 348 */     this.stream.println("   try {");
/* 349 */     this.stream.println("     org.omg.CORBA.Object obj = orb.string_to_object (str);");
/* 350 */     this.stream.println("     org.omg.CORBA.portable.Delegate delegate = ((org.omg.CORBA.portable.ObjectImpl) obj)._get_delegate ();");
/* 351 */     this.stream.println("     _set_delegate (delegate);");
/* 352 */     this.stream.println("   } finally {");
/* 353 */     this.stream.println("     orb.destroy() ;");
/* 354 */     this.stream.println("   }");
/* 355 */     this.stream.println("  }");
/* 356 */     this.stream.println();
/* 357 */     this.stream.println("  private void writeObject (java.io.ObjectOutputStream s) throws java.io.IOException");
/* 358 */     this.stream.println("  {");
/* 359 */     this.stream.println("     String[] args = null;");
/* 360 */     this.stream.println("     java.util.Properties props = null;");
/* 361 */     this.stream.println("     org.omg.CORBA.ORB orb = org.omg.CORBA.ORB.init (args, props);");
/* 362 */     this.stream.println("   try {");
/* 363 */     this.stream.println("     String str = orb.object_to_string (this);");
/* 364 */     this.stream.println("     s.writeUTF (str);");
/* 365 */     this.stream.println("   } finally {");
/* 366 */     this.stream.println("     orb.destroy() ;");
/* 367 */     this.stream.println("   }");
/* 368 */     this.stream.println("  }");
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.corba.se.idl.toJavaPortable.Stub
 * JD-Core Version:    0.6.2
 */