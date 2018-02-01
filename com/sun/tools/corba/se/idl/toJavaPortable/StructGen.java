/*     */ package com.sun.tools.corba.se.idl.toJavaPortable;
/*     */ 
/*     */ import com.sun.tools.corba.se.idl.Comment;
/*     */ import com.sun.tools.corba.se.idl.GenFileStream;
/*     */ import com.sun.tools.corba.se.idl.InterfaceEntry;
/*     */ import com.sun.tools.corba.se.idl.InterfaceState;
/*     */ import com.sun.tools.corba.se.idl.PrimitiveEntry;
/*     */ import com.sun.tools.corba.se.idl.SequenceEntry;
/*     */ import com.sun.tools.corba.se.idl.StringEntry;
/*     */ import com.sun.tools.corba.se.idl.StructEntry;
/*     */ import com.sun.tools.corba.se.idl.SymtabEntry;
/*     */ import com.sun.tools.corba.se.idl.TypedefEntry;
/*     */ import com.sun.tools.corba.se.idl.ValueBoxEntry;
/*     */ import com.sun.tools.corba.se.idl.ValueEntry;
/*     */ import java.io.PrintWriter;
/*     */ import java.util.Enumeration;
/*     */ import java.util.Hashtable;
/*     */ import java.util.Vector;
/*     */ 
/*     */ public class StructGen
/*     */   implements com.sun.tools.corba.se.idl.StructGen, JavaGenerator
/*     */ {
/* 447 */   protected Hashtable symbolTable = null;
/* 448 */   protected StructEntry s = null;
/* 449 */   protected PrintWriter stream = null;
/*     */ 
/* 451 */   protected boolean thisIsReallyAnException = false;
/*     */   private boolean[] memberIsPrimitive;
/*     */   private boolean[] memberIsInterface;
/*     */   private boolean[] memberIsTypedef;
/*     */ 
/*     */   public StructGen()
/*     */   {
/*     */   }
/*     */ 
/*     */   protected StructGen(boolean paramBoolean)
/*     */   {
/*  86 */     this.thisIsReallyAnException = paramBoolean;
/*     */   }
/*     */ 
/*     */   public void generate(Hashtable paramHashtable, StructEntry paramStructEntry, PrintWriter paramPrintWriter)
/*     */   {
/*  94 */     this.symbolTable = paramHashtable;
/*  95 */     this.s = paramStructEntry;
/*     */ 
/*  98 */     openStream();
/*  99 */     if (this.stream == null)
/* 100 */       return;
/* 101 */     generateHelper();
/* 102 */     generateHolder();
/* 103 */     writeHeading();
/* 104 */     writeBody();
/* 105 */     writeClosing();
/* 106 */     closeStream();
/* 107 */     generateContainedTypes();
/*     */   }
/*     */ 
/*     */   protected void init()
/*     */   {
/*     */   }
/*     */ 
/*     */   protected void openStream()
/*     */   {
/* 122 */     this.stream = Util.stream(this.s, ".java");
/*     */   }
/*     */ 
/*     */   protected void generateHelper()
/*     */   {
/* 130 */     ((Factories)Compile.compiler.factories()).helper().generate(this.symbolTable, this.s);
/*     */   }
/*     */ 
/*     */   protected void generateHolder()
/*     */   {
/* 138 */     ((Factories)Compile.compiler.factories()).holder().generate(this.symbolTable, this.s);
/*     */   }
/*     */ 
/*     */   protected void writeHeading()
/*     */   {
/* 146 */     Util.writePackage(this.stream, this.s);
/* 147 */     Util.writeProlog(this.stream, ((GenFileStream)this.stream).name());
/*     */ 
/* 149 */     if (this.s.comment() != null) {
/* 150 */       this.s.comment().generate("", this.stream);
/*     */     }
/* 152 */     this.stream.print("public final class " + this.s.name());
/* 153 */     if (this.thisIsReallyAnException)
/* 154 */       this.stream.print(" extends org.omg.CORBA.UserException");
/*     */     else
/* 156 */       this.stream.print(" implements org.omg.CORBA.portable.IDLEntity");
/* 157 */     this.stream.println();
/* 158 */     this.stream.println("{");
/*     */   }
/*     */ 
/*     */   protected void writeBody()
/*     */   {
/* 166 */     writeMembers();
/* 167 */     writeCtors();
/*     */   }
/*     */ 
/*     */   protected void writeClosing()
/*     */   {
/* 175 */     this.stream.println("} // class " + this.s.name());
/*     */   }
/*     */ 
/*     */   protected void closeStream()
/*     */   {
/* 183 */     this.stream.close();
/*     */   }
/*     */ 
/*     */   protected void generateContainedTypes()
/*     */   {
/* 192 */     Enumeration localEnumeration = this.s.contained().elements();
/* 193 */     while (localEnumeration.hasMoreElements())
/*     */     {
/* 195 */       SymtabEntry localSymtabEntry = (SymtabEntry)localEnumeration.nextElement();
/*     */ 
/* 202 */       if (!(localSymtabEntry instanceof SequenceEntry))
/* 203 */         localSymtabEntry.generate(this.symbolTable, this.stream);
/*     */     }
/*     */   }
/*     */ 
/*     */   protected void writeMembers()
/*     */   {
/* 213 */     int i = this.s.members().size();
/* 214 */     this.memberIsPrimitive = new boolean[i];
/* 215 */     this.memberIsInterface = new boolean[i];
/* 216 */     this.memberIsTypedef = new boolean[i];
/* 217 */     for (int j = 0; j < this.s.members().size(); j++)
/*     */     {
/* 219 */       SymtabEntry localSymtabEntry = (SymtabEntry)this.s.members().elementAt(j);
/* 220 */       this.memberIsPrimitive[j] = (localSymtabEntry.type() instanceof PrimitiveEntry);
/* 221 */       this.memberIsInterface[j] = (localSymtabEntry.type() instanceof InterfaceEntry);
/* 222 */       this.memberIsTypedef[j] = (localSymtabEntry.type() instanceof TypedefEntry);
/* 223 */       Util.fillInfo(localSymtabEntry);
/*     */ 
/* 225 */       if (localSymtabEntry.comment() != null)
/* 226 */         localSymtabEntry.comment().generate("  ", this.stream);
/* 227 */       Util.writeInitializer("  public ", localSymtabEntry.name(), "", localSymtabEntry, this.stream);
/*     */     }
/*     */   }
/*     */ 
/*     */   protected void writeCtors()
/*     */   {
/* 237 */     this.stream.println();
/* 238 */     this.stream.println("  public " + this.s.name() + " ()");
/* 239 */     this.stream.println("  {");
/*     */ 
/* 241 */     if (this.thisIsReallyAnException)
/* 242 */       this.stream.println("    super(" + this.s.name() + "Helper.id());");
/* 243 */     this.stream.println("  } // ctor");
/* 244 */     writeInitializationCtor(true);
/* 245 */     if (this.thisIsReallyAnException)
/*     */     {
/* 248 */       writeInitializationCtor(false);
/*     */     }
/*     */   }
/*     */ 
/*     */   private void writeInitializationCtor(boolean paramBoolean)
/*     */   {
/* 255 */     if ((!paramBoolean) || (this.s.members().size() > 0))
/*     */     {
/* 257 */       this.stream.println();
/* 258 */       this.stream.print("  public " + this.s.name() + " (");
/* 259 */       int i = 1;
/* 260 */       if (!paramBoolean) {
/* 261 */         this.stream.print("String $reason");
/* 262 */         i = 0;
/*     */       }
/*     */       SymtabEntry localSymtabEntry;
/* 265 */       for (int j = 0; j < this.s.members().size(); j++)
/*     */       {
/* 267 */         localSymtabEntry = (SymtabEntry)this.s.members().elementAt(j);
/* 268 */         if (i != 0)
/* 269 */           i = 0;
/*     */         else
/* 271 */           this.stream.print(", ");
/* 272 */         this.stream.print(Util.javaName(localSymtabEntry) + " _" + localSymtabEntry.name());
/*     */       }
/* 274 */       this.stream.println(")");
/* 275 */       this.stream.println("  {");
/*     */ 
/* 277 */       if (this.thisIsReallyAnException) {
/* 278 */         if (paramBoolean)
/* 279 */           this.stream.println("    super(" + this.s.name() + "Helper.id());");
/*     */         else
/* 281 */           this.stream.println("    super(" + this.s.name() + "Helper.id() + \"  \" + $reason);");
/*     */       }
/* 283 */       for (j = 0; j < this.s.members().size(); j++)
/*     */       {
/* 285 */         localSymtabEntry = (SymtabEntry)this.s.members().elementAt(j);
/* 286 */         this.stream.println("    " + localSymtabEntry.name() + " = _" + localSymtabEntry.name() + ";");
/*     */       }
/* 288 */       this.stream.println("  } // ctor");
/*     */     }
/* 290 */     this.stream.println();
/*     */   }
/*     */ 
/*     */   public int helperType(int paramInt, String paramString1, TCOffsets paramTCOffsets, String paramString2, SymtabEntry paramSymtabEntry, PrintWriter paramPrintWriter)
/*     */   {
/* 298 */     TCOffsets localTCOffsets = new TCOffsets();
/* 299 */     localTCOffsets.set(paramSymtabEntry);
/* 300 */     int i = localTCOffsets.currentOffset();
/* 301 */     StructEntry localStructEntry = (StructEntry)paramSymtabEntry;
/* 302 */     String str1 = "_members" + paramInt++;
/* 303 */     paramPrintWriter.println(paramString1 + "org.omg.CORBA.StructMember[] " + str1 + " = new org.omg.CORBA.StructMember [" + localStructEntry.members().size() + "];");
/* 304 */     String str2 = "_tcOf" + str1;
/* 305 */     paramPrintWriter.println(paramString1 + "org.omg.CORBA.TypeCode " + str2 + " = null;");
/* 306 */     for (int j = 0; j < localStructEntry.members().size(); j++)
/*     */     {
/* 308 */       TypedefEntry localTypedefEntry = (TypedefEntry)localStructEntry.members().elementAt(j);
/* 309 */       String str3 = localTypedefEntry.name();
/*     */ 
/* 311 */       paramInt = ((JavaGenerator)localTypedefEntry.generator()).type(paramInt, paramString1, localTCOffsets, str2, localTypedefEntry, paramPrintWriter);
/* 312 */       paramPrintWriter.println(paramString1 + str1 + '[' + j + "] = new org.omg.CORBA.StructMember (");
/* 313 */       paramPrintWriter.println(paramString1 + "  \"" + Util.stripLeadingUnderscores(str3) + "\",");
/* 314 */       paramPrintWriter.println(paramString1 + "  " + str2 + ',');
/* 315 */       paramPrintWriter.println(paramString1 + "  null);");
/* 316 */       int k = localTCOffsets.currentOffset();
/* 317 */       localTCOffsets = new TCOffsets();
/* 318 */       localTCOffsets.set(paramSymtabEntry);
/* 319 */       localTCOffsets.bumpCurrentOffset(k - i);
/*     */     }
/*     */ 
/* 322 */     paramTCOffsets.bumpCurrentOffset(localTCOffsets.currentOffset());
/*     */ 
/* 325 */     paramPrintWriter.println(paramString1 + paramString2 + " = org.omg.CORBA.ORB.init ().create_" + (this.thisIsReallyAnException ? "exception" : "struct") + "_tc (" + Util.helperName(localStructEntry, true) + ".id (), \"" + Util.stripLeadingUnderscores(paramSymtabEntry.name()) + "\", " + str1 + ");");
/* 326 */     return paramInt;
/*     */   }
/*     */ 
/*     */   public int type(int paramInt, String paramString1, TCOffsets paramTCOffsets, String paramString2, SymtabEntry paramSymtabEntry, PrintWriter paramPrintWriter) {
/* 330 */     paramPrintWriter.println(paramString1 + paramString2 + " = " + Util.helperName(paramSymtabEntry, true) + ".type ();");
/* 331 */     return paramInt;
/*     */   }
/*     */ 
/*     */   public void helperRead(String paramString, SymtabEntry paramSymtabEntry, PrintWriter paramPrintWriter)
/*     */   {
/* 336 */     paramPrintWriter.println("    " + paramString + " value = new " + paramString + " ();");
/* 337 */     read(0, "    ", "value", paramSymtabEntry, paramPrintWriter);
/* 338 */     paramPrintWriter.println("    return value;");
/*     */   }
/*     */ 
/*     */   public int read(int paramInt, String paramString1, String paramString2, SymtabEntry paramSymtabEntry, PrintWriter paramPrintWriter)
/*     */   {
/* 343 */     if (this.thisIsReallyAnException)
/*     */     {
/* 345 */       paramPrintWriter.println(paramString1 + "// read and discard the repository ID");
/* 346 */       paramPrintWriter.println(paramString1 + "istream.read_string ();");
/*     */     }
/*     */ 
/* 349 */     Enumeration localEnumeration = ((StructEntry)paramSymtabEntry).members().elements();
/* 350 */     while (localEnumeration.hasMoreElements())
/*     */     {
/* 352 */       TypedefEntry localTypedefEntry1 = (TypedefEntry)localEnumeration.nextElement();
/* 353 */       SymtabEntry localSymtabEntry1 = localTypedefEntry1.type();
/*     */ 
/* 355 */       if ((!localTypedefEntry1.arrayInfo().isEmpty()) || ((localSymtabEntry1 instanceof SequenceEntry)) || ((localSymtabEntry1 instanceof PrimitiveEntry)) || ((localSymtabEntry1 instanceof StringEntry)) || ((localSymtabEntry1 instanceof TypedefEntry)))
/*     */       {
/* 358 */         paramInt = ((JavaGenerator)localTypedefEntry1.generator()).read(paramInt, paramString1, paramString2 + '.' + localTypedefEntry1.name(), localTypedefEntry1, paramPrintWriter);
/* 359 */       } else if ((localSymtabEntry1 instanceof ValueBoxEntry))
/*     */       {
/* 362 */         Vector localVector = ((ValueBoxEntry)localSymtabEntry1).state();
/* 363 */         TypedefEntry localTypedefEntry2 = ((InterfaceState)localVector.elementAt(0)).entry;
/* 364 */         SymtabEntry localSymtabEntry2 = localTypedefEntry2.type();
/*     */ 
/* 366 */         String str1 = null;
/* 367 */         String str2 = null;
/*     */ 
/* 369 */         if (((localSymtabEntry2 instanceof SequenceEntry)) || ((localSymtabEntry2 instanceof StringEntry)) || 
/* 370 */           (!localTypedefEntry2
/* 370 */           .arrayInfo().isEmpty()))
/*     */         {
/* 372 */           str1 = Util.javaName(localSymtabEntry2);
/*     */ 
/* 376 */           str2 = Util.helperName(localSymtabEntry1, true);
/*     */         }
/*     */         else
/*     */         {
/* 380 */           str1 = Util.javaName(localSymtabEntry1);
/*     */ 
/* 383 */           str2 = Util.helperName(localSymtabEntry1, true);
/*     */         }
/*     */ 
/* 386 */         if (Util.corbaLevel(2.4F, 99.0F))
/* 387 */           paramPrintWriter.println(paramString1 + paramString2 + '.' + localTypedefEntry1.name() + " = (" + str1 + ") " + str2 + ".read (istream);");
/*     */         else {
/* 389 */           paramPrintWriter.println(paramString1 + paramString2 + '.' + localTypedefEntry1.name() + " = (" + str1 + ") ((org.omg.CORBA_2_3.portable.InputStream)istream).read_value (" + str2 + ".get_instance ());");
/*     */         }
/*     */ 
/*     */       }
/* 393 */       else if (((localSymtabEntry1 instanceof ValueEntry)) && 
/* 394 */         (!Util.corbaLevel(2.4F, 99.0F)))
/*     */       {
/* 397 */         paramPrintWriter.println(paramString1 + paramString2 + '.' + localTypedefEntry1.name() + " = (" + Util.javaName(localSymtabEntry1) + ") ((org.omg.CORBA_2_3.portable.InputStream)istream).read_value (" + Util.helperName(localSymtabEntry1, false) + ".get_instance ());");
/*     */       }
/*     */       else {
/* 400 */         paramPrintWriter.println(paramString1 + paramString2 + '.' + localTypedefEntry1.name() + " = " + Util.helperName(localTypedefEntry1.type(), true) + ".read (istream);");
/*     */       }
/*     */     }
/* 402 */     return paramInt;
/*     */   }
/*     */ 
/*     */   public void helperWrite(SymtabEntry paramSymtabEntry, PrintWriter paramPrintWriter)
/*     */   {
/* 407 */     write(0, "    ", "value", paramSymtabEntry, paramPrintWriter);
/*     */   }
/*     */ 
/*     */   public int write(int paramInt, String paramString1, String paramString2, SymtabEntry paramSymtabEntry, PrintWriter paramPrintWriter)
/*     */   {
/* 412 */     if (this.thisIsReallyAnException)
/*     */     {
/* 414 */       paramPrintWriter.println(paramString1 + "// write the repository ID");
/* 415 */       paramPrintWriter.println(paramString1 + "ostream.write_string (id ());");
/*     */     }
/*     */ 
/* 418 */     Vector localVector = ((StructEntry)paramSymtabEntry).members();
/* 419 */     for (int i = 0; i < localVector.size(); i++)
/*     */     {
/* 421 */       TypedefEntry localTypedefEntry = (TypedefEntry)localVector.elementAt(i);
/* 422 */       SymtabEntry localSymtabEntry = localTypedefEntry.type();
/*     */ 
/* 424 */       if ((!localTypedefEntry.arrayInfo().isEmpty()) || ((localSymtabEntry instanceof SequenceEntry)) || ((localSymtabEntry instanceof TypedefEntry)) || ((localSymtabEntry instanceof PrimitiveEntry)) || ((localSymtabEntry instanceof StringEntry)))
/*     */       {
/* 427 */         paramInt = ((JavaGenerator)localTypedefEntry.generator()).write(paramInt, "    ", paramString2 + '.' + localTypedefEntry.name(), localTypedefEntry, paramPrintWriter);
/*     */       }
/* 431 */       else if ((((localSymtabEntry instanceof ValueEntry)) || ((localSymtabEntry instanceof ValueBoxEntry))) && 
/* 432 */         (!Util.corbaLevel(2.4F, 99.0F)))
/*     */       {
/* 433 */         paramPrintWriter.println(paramString1 + "((org.omg.CORBA_2_3.portable.OutputStream)ostream).write_value ((java.io.Serializable) " + paramString2 + '.' + localTypedefEntry
/* 434 */           .name() + ", " + 
/* 435 */           Util.helperName(localTypedefEntry
/* 435 */           .type(), true) + ".get_instance ());");
/*     */       }
/*     */       else
/*     */       {
/* 439 */         paramPrintWriter.println(paramString1 + Util.helperName(localTypedefEntry.type(), true) + ".write (ostream, " + paramString2 + '.' + localTypedefEntry.name() + ");");
/*     */       }
/*     */     }
/* 441 */     return paramInt;
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.corba.se.idl.toJavaPortable.StructGen
 * JD-Core Version:    0.6.2
 */