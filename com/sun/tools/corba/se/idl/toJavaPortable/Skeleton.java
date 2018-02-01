/*     */ package com.sun.tools.corba.se.idl.toJavaPortable;
/*     */ 
/*     */ import com.sun.tools.corba.se.idl.AttributeEntry;
/*     */ import com.sun.tools.corba.se.idl.Comment;
/*     */ import com.sun.tools.corba.se.idl.GenFileStream;
/*     */ import com.sun.tools.corba.se.idl.InterfaceEntry;
/*     */ import com.sun.tools.corba.se.idl.MethodEntry;
/*     */ import com.sun.tools.corba.se.idl.RepositoryID;
/*     */ import com.sun.tools.corba.se.idl.SymtabEntry;
/*     */ import com.sun.tools.corba.se.idl.ValueEntry;
/*     */ import java.io.PrintWriter;
/*     */ import java.util.Enumeration;
/*     */ import java.util.Hashtable;
/*     */ import java.util.Vector;
/*     */ 
/*     */ public class Skeleton
/*     */   implements AuxGen
/*     */ {
/*     */   private NameModifier skeletonNameModifier;
/*     */   private NameModifier tieNameModifier;
/* 557 */   protected Hashtable symbolTable = null;
/* 558 */   protected InterfaceEntry i = null;
/* 559 */   protected PrintWriter stream = null;
/*     */ 
/* 562 */   protected String tieClassName = null;
/* 563 */   protected String skeletonClassName = null;
/* 564 */   protected boolean tie = false;
/* 565 */   protected boolean poa = false;
/* 566 */   protected Vector methodList = null;
/* 567 */   protected String intfName = "";
/*     */ 
/*     */   public void generate(Hashtable paramHashtable, SymtabEntry paramSymtabEntry)
/*     */   {
/*  82 */     if ((paramSymtabEntry instanceof ValueEntry))
/*     */     {
/*  84 */       ValueEntry localValueEntry = (ValueEntry)paramSymtabEntry;
/*  85 */       if ((localValueEntry.supports().size() == 0) || 
/*  86 */         (((InterfaceEntry)localValueEntry
/*  86 */         .supports().elementAt(0)).isAbstract())) {
/*  87 */         return;
/*     */       }
/*     */     }
/*  90 */     if (((InterfaceEntry)paramSymtabEntry).isAbstract()) {
/*  91 */       return;
/*     */     }
/*     */ 
/*  95 */     this.symbolTable = paramHashtable;
/*     */ 
/*  97 */     this.i = ((InterfaceEntry)paramSymtabEntry);
/*  98 */     init();
/*  99 */     openStream();
/* 100 */     if (this.stream == null)
/* 101 */       return;
/* 102 */     writeHeading();
/* 103 */     writeBody();
/* 104 */     writeClosing();
/* 105 */     closeStream();
/*     */   }
/*     */ 
/*     */   protected void init()
/*     */   {
/* 113 */     this.tie = ((Arguments)Compile.compiler.arguments).TIEServer;
/* 114 */     this.poa = ((Arguments)Compile.compiler.arguments).POAServer;
/*     */ 
/* 116 */     this.skeletonNameModifier = ((Arguments)Compile.compiler.arguments).skeletonNameModifier;
/*     */ 
/* 118 */     this.tieNameModifier = ((Arguments)Compile.compiler.arguments).tieNameModifier;
/*     */ 
/* 121 */     this.tieClassName = this.tieNameModifier.makeName(this.i.name());
/* 122 */     this.skeletonClassName = this.skeletonNameModifier.makeName(this.i.name());
/*     */ 
/* 124 */     this.intfName = Util.javaName(this.i);
/*     */ 
/* 126 */     if ((this.i instanceof ValueEntry))
/*     */     {
/* 128 */       ValueEntry localValueEntry = (ValueEntry)this.i;
/* 129 */       InterfaceEntry localInterfaceEntry = (InterfaceEntry)localValueEntry.supports().elementAt(0);
/* 130 */       this.intfName = Util.javaName(localInterfaceEntry);
/*     */     }
/*     */   }
/*     */ 
/*     */   protected void openStream()
/*     */   {
/* 136 */     if (this.tie)
/* 137 */       this.stream = Util.stream(this.i, this.tieNameModifier, ".java");
/*     */     else
/* 139 */       this.stream = Util.stream(this.i, this.skeletonNameModifier, ".java");
/*     */   }
/*     */ 
/*     */   protected void writeHeading()
/*     */   {
/* 144 */     Util.writePackage(this.stream, this.i, (short)1);
/* 145 */     Util.writeProlog(this.stream, ((GenFileStream)this.stream).name());
/* 146 */     if (this.i.comment() != null)
/* 147 */       this.i.comment().generate("", this.stream);
/* 148 */     writeClassDeclaration();
/* 149 */     this.stream.println('{');
/* 150 */     this.stream.println();
/*     */   }
/*     */ 
/*     */   protected void writeClassDeclaration()
/*     */   {
/* 155 */     if (this.tie) {
/* 156 */       this.stream.println("public class " + this.tieClassName + " extends " + this.skeletonClassName);
/*     */     }
/* 159 */     else if (this.poa) {
/* 160 */       this.stream.println("public abstract class " + this.skeletonClassName + " extends org.omg.PortableServer.Servant");
/*     */ 
/* 162 */       this.stream.print(" implements " + this.intfName + "Operations, ");
/* 163 */       this.stream.println("org.omg.CORBA.portable.InvokeHandler");
/*     */     } else {
/* 165 */       this.stream.println("public abstract class " + this.skeletonClassName + " extends org.omg.CORBA.portable.ObjectImpl");
/*     */ 
/* 167 */       this.stream.print("                implements " + this.intfName + ", ");
/* 168 */       this.stream.println("org.omg.CORBA.portable.InvokeHandler");
/*     */     }
/*     */   }
/*     */ 
/*     */   protected void writeBody()
/*     */   {
/* 181 */     writeCtors();
/* 182 */     if ((this.i instanceof ValueEntry))
/*     */     {
/* 186 */       ValueEntry localValueEntry = (ValueEntry)this.i;
/* 187 */       this.i = ((InterfaceEntry)localValueEntry.supports().elementAt(0));
/*     */     }
/* 189 */     buildMethodList();
/*     */ 
/* 191 */     if (this.tie)
/*     */     {
/* 193 */       if (this.poa) {
/* 194 */         writeMethods();
/* 195 */         this.stream.println("  private " + this.intfName + "Operations _impl;");
/* 196 */         this.stream.println("  private org.omg.PortableServer.POA _poa;");
/*     */       } else {
/* 198 */         writeMethods();
/* 199 */         this.stream.println("  private " + this.intfName + "Operations _impl;");
/*     */       }
/*     */ 
/*     */     }
/* 203 */     else if (this.poa) {
/* 204 */       writeMethodTable();
/* 205 */       writeDispatchMethod();
/* 206 */       writeCORBAOperations();
/*     */     } else {
/* 208 */       writeMethodTable();
/* 209 */       writeDispatchMethod();
/* 210 */       writeCORBAOperations();
/*     */     }
/*     */ 
/* 214 */     writeOperations();
/*     */   }
/*     */ 
/*     */   protected void writeClosing()
/*     */   {
/* 223 */     this.stream.println();
/* 224 */     if (this.tie)
/* 225 */       this.stream.println("} // class " + this.tieClassName);
/*     */     else
/* 227 */       this.stream.println("} // class " + this.skeletonClassName);
/*     */   }
/*     */ 
/*     */   protected void closeStream()
/*     */   {
/* 236 */     this.stream.close();
/*     */   }
/*     */ 
/*     */   protected void writeCtors()
/*     */   {
/* 241 */     this.stream.println("  // Constructors");
/*     */ 
/* 243 */     if (!this.poa) {
/* 244 */       if (this.tie) {
/* 245 */         this.stream.println("  public " + this.tieClassName + " ()");
/* 246 */         this.stream.println("  {");
/* 247 */         this.stream.println("  }");
/*     */       } else {
/* 249 */         this.stream.println("  public " + this.skeletonClassName + " ()");
/* 250 */         this.stream.println("  {");
/* 251 */         this.stream.println("  }");
/*     */       }
/*     */     }
/* 254 */     this.stream.println();
/*     */ 
/* 256 */     if (this.tie) {
/* 257 */       if (this.poa)
/*     */       {
/* 259 */         writePOATieCtors();
/*     */ 
/* 261 */         writePOATieFieldAccessMethods();
/*     */       } else {
/* 263 */         this.stream.println("  public " + this.tieClassName + " (" + this.intfName + "Operations impl)");
/*     */ 
/* 265 */         this.stream.println("  {");
/*     */ 
/* 267 */         if (((InterfaceEntry)this.i.derivedFrom().firstElement()).state() != null)
/* 268 */           this.stream.println("    super (impl);");
/*     */         else
/* 270 */           this.stream.println("    super ();");
/* 271 */         this.stream.println("    _impl = impl;");
/* 272 */         this.stream.println("  }");
/* 273 */         this.stream.println();
/*     */       }
/*     */     }
/* 276 */     else if (!this.poa);
/*     */   }
/*     */ 
/*     */   private void writePOATieCtors()
/*     */   {
/* 286 */     this.stream.println("  public " + this.tieClassName + " ( " + this.intfName + "Operations delegate ) {");
/* 287 */     this.stream.println("      this._impl = delegate;");
/* 288 */     this.stream.println("  }");
/*     */ 
/* 290 */     this.stream.println("  public " + this.tieClassName + " ( " + this.intfName + "Operations delegate , org.omg.PortableServer.POA poa ) {");
/*     */ 
/* 292 */     this.stream.println("      this._impl = delegate;");
/* 293 */     this.stream.println("      this._poa      = poa;");
/* 294 */     this.stream.println("  }");
/*     */   }
/*     */ 
/*     */   private void writePOATieFieldAccessMethods()
/*     */   {
/* 299 */     this.stream.println("  public " + this.intfName + "Operations _delegate() {");
/* 300 */     this.stream.println("      return this._impl;");
/* 301 */     this.stream.println("  }");
/*     */ 
/* 303 */     this.stream.println("  public void _delegate (" + this.intfName + "Operations delegate ) {");
/* 304 */     this.stream.println("      this._impl = delegate;");
/* 305 */     this.stream.println("  }");
/*     */ 
/* 307 */     this.stream.println("  public org.omg.PortableServer.POA _default_POA() {");
/* 308 */     this.stream.println("      if(_poa != null) {");
/* 309 */     this.stream.println("          return _poa;");
/* 310 */     this.stream.println("      }");
/* 311 */     this.stream.println("      else {");
/* 312 */     this.stream.println("          return super._default_POA();");
/* 313 */     this.stream.println("      }");
/* 314 */     this.stream.println("  }");
/*     */   }
/*     */ 
/*     */   protected void buildMethodList()
/*     */   {
/* 323 */     this.methodList = new Vector();
/*     */ 
/* 325 */     buildMethodList(this.i);
/*     */   }
/*     */ 
/*     */   private void buildMethodList(InterfaceEntry paramInterfaceEntry)
/*     */   {
/* 334 */     Enumeration localEnumeration1 = paramInterfaceEntry.methods().elements();
/* 335 */     while (localEnumeration1.hasMoreElements()) {
/* 336 */       addMethod((MethodEntry)localEnumeration1.nextElement());
/*     */     }
/*     */ 
/* 339 */     Enumeration localEnumeration2 = paramInterfaceEntry.derivedFrom().elements();
/* 340 */     while (localEnumeration2.hasMoreElements())
/*     */     {
/* 342 */       InterfaceEntry localInterfaceEntry = (InterfaceEntry)localEnumeration2.nextElement();
/* 343 */       if (!localInterfaceEntry.name().equals("Object"))
/* 344 */         buildMethodList(localInterfaceEntry);
/*     */     }
/*     */   }
/*     */ 
/*     */   private void addMethod(MethodEntry paramMethodEntry)
/*     */   {
/* 353 */     if (!this.methodList.contains(paramMethodEntry))
/* 354 */       this.methodList.addElement(paramMethodEntry);
/*     */   }
/*     */ 
/*     */   protected void writeDispatchMethod()
/*     */   {
/* 362 */     String str = "                                ";
/* 363 */     this.stream.println("  public org.omg.CORBA.portable.OutputStream _invoke (String $method,");
/* 364 */     this.stream.println(str + "org.omg.CORBA.portable.InputStream in,");
/* 365 */     this.stream.println(str + "org.omg.CORBA.portable.ResponseHandler $rh)");
/* 366 */     this.stream.println("  {");
/*     */ 
/* 372 */     boolean bool = false;
/* 373 */     if ((this.i instanceof InterfaceEntry)) {
/* 374 */       bool = this.i.isLocalServant();
/*     */     }
/*     */ 
/* 377 */     if (!bool)
/*     */     {
/* 379 */       this.stream.println("    org.omg.CORBA.portable.OutputStream out = null;");
/* 380 */       this.stream.println("    java.lang.Integer __method = (java.lang.Integer)_methods.get ($method);");
/* 381 */       this.stream.println("    if (__method == null)");
/* 382 */       this.stream.println("      throw new org.omg.CORBA.BAD_OPERATION (0, org.omg.CORBA.CompletionStatus.COMPLETED_MAYBE);");
/* 383 */       this.stream.println();
/* 384 */       if (this.methodList.size() > 0)
/*     */       {
/* 386 */         this.stream.println("    switch (__method.intValue ())");
/* 387 */         this.stream.println("    {");
/*     */ 
/* 390 */         int j = 0;
/* 391 */         for (int k = 0; k < this.methodList.size(); k++)
/*     */         {
/* 393 */           MethodEntry localMethodEntry = (MethodEntry)this.methodList.elementAt(k);
/* 394 */           ((MethodGen)localMethodEntry.generator()).dispatchSkeleton(this.symbolTable, localMethodEntry, this.stream, j);
/* 395 */           if (((localMethodEntry instanceof AttributeEntry)) && (!((AttributeEntry)localMethodEntry).readOnly()))
/* 396 */             j += 2;
/*     */           else {
/* 398 */             j++;
/*     */           }
/*     */         }
/* 401 */         str = "       ";
/* 402 */         this.stream.println(str + "default:");
/* 403 */         this.stream.println(str + "  throw new org.omg.CORBA.BAD_OPERATION (0, org.omg.CORBA.CompletionStatus.COMPLETED_MAYBE);");
/* 404 */         this.stream.println("    }");
/* 405 */         this.stream.println();
/*     */       }
/* 407 */       this.stream.println("    return out;");
/*     */     } else {
/* 409 */       this.stream.println("    throw new org.omg.CORBA.BAD_OPERATION();");
/*     */     }
/* 411 */     this.stream.println("  } // _invoke");
/* 412 */     this.stream.println();
/*     */   }
/*     */ 
/*     */   protected void writeMethodTable()
/*     */   {
/* 421 */     this.stream.println("  private static java.util.Hashtable _methods = new java.util.Hashtable ();");
/* 422 */     this.stream.println("  static");
/* 423 */     this.stream.println("  {");
/*     */ 
/* 425 */     int j = -1;
/* 426 */     Enumeration localEnumeration = this.methodList.elements();
/* 427 */     while (localEnumeration.hasMoreElements())
/*     */     {
/* 429 */       MethodEntry localMethodEntry = (MethodEntry)localEnumeration.nextElement();
/* 430 */       if ((localMethodEntry instanceof AttributeEntry))
/*     */       {
/* 432 */         this.stream.println("    _methods.put (\"_get_" + Util.stripLeadingUnderscores(localMethodEntry.name()) + "\", new java.lang.Integer (" + ++j + "));");
/* 433 */         if (!((AttributeEntry)localMethodEntry).readOnly())
/* 434 */           this.stream.println("    _methods.put (\"_set_" + Util.stripLeadingUnderscores(localMethodEntry.name()) + "\", new java.lang.Integer (" + ++j + "));");
/*     */       }
/*     */       else {
/* 437 */         this.stream.println("    _methods.put (\"" + Util.stripLeadingUnderscores(localMethodEntry.name()) + "\", new java.lang.Integer (" + ++j + "));");
/*     */       }
/*     */     }
/* 439 */     this.stream.println("  }");
/* 440 */     this.stream.println();
/*     */   }
/*     */ 
/*     */   protected void writeMethods()
/*     */   {
/* 448 */     int j = 0;
/* 449 */     for (int k = 0; k < this.methodList.size(); k++)
/*     */     {
/* 451 */       MethodEntry localMethodEntry = (MethodEntry)this.methodList.elementAt(k);
/* 452 */       ((MethodGen)localMethodEntry.generator())
/* 453 */         .skeleton(this.symbolTable, localMethodEntry, this.stream, j);
/*     */ 
/* 454 */       if (((localMethodEntry instanceof AttributeEntry)) && 
/* 455 */         (!((AttributeEntry)localMethodEntry)
/* 455 */         .readOnly()))
/* 456 */         j += 2;
/*     */       else
/* 458 */         j++;
/* 459 */       this.stream.println();
/*     */     }
/*     */   }
/*     */ 
/*     */   private void writeIDs()
/*     */   {
/* 468 */     Vector localVector = new Vector();
/* 469 */     buildIDList(this.i, localVector);
/* 470 */     Enumeration localEnumeration = localVector.elements();
/* 471 */     int j = 1;
/* 472 */     while (localEnumeration.hasMoreElements())
/*     */     {
/* 474 */       if (j != 0)
/* 475 */         j = 0;
/*     */       else
/* 477 */         this.stream.println(", ");
/* 478 */       this.stream.print("    \"" + (String)localEnumeration.nextElement() + '"');
/*     */     }
/*     */   }
/*     */ 
/*     */   private void buildIDList(InterfaceEntry paramInterfaceEntry, Vector paramVector)
/*     */   {
/* 487 */     if (!paramInterfaceEntry.fullName().equals("org/omg/CORBA/Object"))
/*     */     {
/* 489 */       String str = Util.stripLeadingUnderscoresFromID(paramInterfaceEntry.repositoryID().ID());
/* 490 */       if (!paramVector.contains(str))
/* 491 */         paramVector.addElement(str);
/* 492 */       Enumeration localEnumeration = paramInterfaceEntry.derivedFrom().elements();
/* 493 */       while (localEnumeration.hasMoreElements())
/* 494 */         buildIDList((InterfaceEntry)localEnumeration.nextElement(), paramVector);
/*     */     }
/*     */   }
/*     */ 
/*     */   protected void writeCORBAOperations()
/*     */   {
/* 503 */     this.stream.println("  // Type-specific CORBA::Object operations");
/*     */ 
/* 505 */     this.stream.println("  private static String[] __ids = {");
/* 506 */     writeIDs();
/* 507 */     this.stream.println("};");
/* 508 */     this.stream.println();
/* 509 */     if (this.poa)
/* 510 */       writePOACORBAOperations();
/*     */     else
/* 512 */       writeNonPOACORBAOperations();
/*     */   }
/*     */ 
/*     */   protected void writePOACORBAOperations()
/*     */   {
/* 517 */     this.stream.println("  public String[] _all_interfaces (org.omg.PortableServer.POA poa, byte[] objectId)");
/*     */ 
/* 523 */     this.stream.println("  {");
/* 524 */     this.stream.println("    return (String[])__ids.clone ();");
/* 525 */     this.stream.println("  }");
/* 526 */     this.stream.println();
/*     */ 
/* 528 */     this.stream.println("  public " + this.i.name() + " _this() ");
/* 529 */     this.stream.println("  {");
/* 530 */     this.stream.println("    return " + this.i.name() + "Helper.narrow(");
/* 531 */     this.stream.println("    super._this_object());");
/* 532 */     this.stream.println("  }");
/* 533 */     this.stream.println();
/*     */ 
/* 535 */     this.stream.println("  public " + this.i.name() + " _this(org.omg.CORBA.ORB orb) ");
/* 536 */     this.stream.println("  {");
/* 537 */     this.stream.println("    return " + this.i.name() + "Helper.narrow(");
/* 538 */     this.stream.println("    super._this_object(orb));");
/* 539 */     this.stream.println("  }");
/* 540 */     this.stream.println();
/*     */   }
/*     */   protected void writeNonPOACORBAOperations() {
/* 543 */     this.stream.println("  public String[] _ids ()");
/* 544 */     this.stream.println("  {");
/* 545 */     this.stream.println("    return (String[])__ids.clone ();");
/* 546 */     this.stream.println("  }");
/* 547 */     this.stream.println();
/*     */   }
/*     */ 
/*     */   protected void writeOperations()
/*     */   {
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.corba.se.idl.toJavaPortable.Skeleton
 * JD-Core Version:    0.6.2
 */