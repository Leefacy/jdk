/*     */ package com.sun.tools.corba.se.idl.toJavaPortable;
/*     */ 
/*     */ import com.sun.tools.corba.se.idl.AttributeEntry;
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
/*     */ import com.sun.tools.corba.se.idl.ValueEntry;
/*     */ import java.io.PrintWriter;
/*     */ import java.util.Enumeration;
/*     */ import java.util.Hashtable;
/*     */ import java.util.Vector;
/*     */ 
/*     */ public class ValueGen24 extends ValueGen
/*     */ {
/*     */   protected void writeConstructor()
/*     */   {
/*     */   }
/*     */ 
/*     */   public void helperWrite(SymtabEntry paramSymtabEntry, PrintWriter paramPrintWriter)
/*     */   {
/*  90 */     paramPrintWriter.println("    ((org.omg.CORBA_2_3.portable.OutputStream) ostream).write_value (value, id ());");
/*     */   }
/*     */ 
/*     */   public void helperRead(String paramString, SymtabEntry paramSymtabEntry, PrintWriter paramPrintWriter)
/*     */   {
/* 100 */     paramPrintWriter.println("    return (" + paramString + ")((org.omg.CORBA_2_3.portable.InputStream) istream).read_value (id ());");
/*     */   }
/*     */ 
/*     */   protected void writeInitializers()
/*     */   {
/*     */   }
/*     */ 
/*     */   protected void writeTruncatable()
/*     */   {
/* 117 */     if (!this.v.isAbstract()) {
/* 118 */       this.stream.println("  private static String[] _truncatable_ids = {");
/* 119 */       this.stream.print("    " + Util.helperName(this.v, true) + ".id ()");
/*     */ 
/* 124 */       Object localObject = this.v;
/* 125 */       while (((ValueEntry)localObject).isSafe())
/*     */       {
/* 127 */         this.stream.println(",");
/* 128 */         ValueEntry localValueEntry = (ValueEntry)((ValueEntry)localObject).derivedFrom().elementAt(0);
/* 129 */         this.stream.print("    \"" + Util.stripLeadingUnderscoresFromID(localValueEntry.repositoryID().ID()) + "\"");
/* 130 */         localObject = localValueEntry;
/*     */       }
/* 132 */       this.stream.println();
/* 133 */       this.stream.println("  };");
/* 134 */       this.stream.println();
/* 135 */       this.stream.println("  public String[] _truncatable_ids() {");
/* 136 */       this.stream.println("    return _truncatable_ids;");
/* 137 */       this.stream.println("  }");
/* 138 */       this.stream.println();
/*     */     }
/*     */   }
/*     */ 
/*     */   protected void writeHeading()
/*     */   {
/* 163 */     ImplStreamWriter localImplStreamWriter = new ImplStreamWriter();
/*     */ 
/* 165 */     Util.writePackage(this.stream, this.v);
/* 166 */     Util.writeProlog(this.stream, ((GenFileStream)this.stream).name());
/*     */ 
/* 168 */     if (this.v.comment() != null) {
/* 169 */       this.v.comment().generate("", this.stream);
/*     */     }
/* 171 */     if (this.v.isAbstract()) {
/* 172 */       writeAbstract();
/* 173 */       return;
/*     */     }
/* 175 */     this.stream.print("public abstract class " + this.v.name());
/*     */ 
/* 178 */     SymtabEntry localSymtabEntry = (SymtabEntry)this.v.derivedFrom().elementAt(0);
/*     */ 
/* 181 */     String str1 = Util.javaName(localSymtabEntry);
/* 182 */     int i = 0;
/*     */ 
/* 184 */     if (str1.equals("java.io.Serializable")) {
/* 185 */       if (this.v.isCustom()) {
/* 186 */         localImplStreamWriter.writeClassName("org.omg.CORBA.portable.CustomValue");
/* 187 */         i = 1;
/*     */       } else {
/* 189 */         localImplStreamWriter.writeClassName("org.omg.CORBA.portable.StreamableValue");
/*     */       } } else if (!((ValueEntry)localSymtabEntry).isAbstract()) {
/* 191 */       this.stream.print(" extends " + str1);
/*     */     }
/*     */ 
/* 194 */     for (int j = 0; j < this.v.derivedFrom().size(); j++) {
/* 195 */       localSymtabEntry = (SymtabEntry)this.v.derivedFrom().elementAt(j);
/* 196 */       if (((ValueEntry)localSymtabEntry).isAbstract()) {
/* 197 */         localImplStreamWriter.writeClassName(Util.javaName(localSymtabEntry));
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 202 */     Enumeration localEnumeration = this.v.supports().elements();
/* 203 */     while (localEnumeration.hasMoreElements()) {
/* 204 */       InterfaceEntry localInterfaceEntry = (InterfaceEntry)localEnumeration.nextElement();
/* 205 */       String str2 = Util.javaName(localInterfaceEntry);
/* 206 */       if (!localInterfaceEntry.isAbstract())
/* 207 */         str2 = str2 + "Operations";
/* 208 */       localImplStreamWriter.writeClassName(str2);
/*     */     }
/*     */ 
/* 212 */     if ((this.v.isCustom()) && (i == 0)) {
/* 213 */       localImplStreamWriter.writeClassName("org.omg.CORBA.portable.CustomValue");
/*     */     }
/* 215 */     this.stream.println();
/* 216 */     this.stream.println("{");
/*     */   }
/*     */ 
/*     */   protected void writeMembers()
/*     */   {
/* 225 */     if (this.v.state() == null) {
/* 226 */       return;
/*     */     }
/* 228 */     for (int i = 0; i < this.v.state().size(); i++)
/*     */     {
/* 230 */       InterfaceState localInterfaceState = (InterfaceState)this.v.state().elementAt(i);
/* 231 */       TypedefEntry localTypedefEntry = localInterfaceState.entry;
/* 232 */       Util.fillInfo(localTypedefEntry);
/*     */ 
/* 234 */       if (localTypedefEntry.comment() != null) {
/* 235 */         localTypedefEntry.comment().generate(" ", this.stream);
/*     */       }
/* 237 */       String str = "  ";
/* 238 */       if (localInterfaceState.modifier == 2)
/* 239 */         str = "  public ";
/*     */       else
/* 241 */         str = "  protected ";
/* 242 */       Util.writeInitializer(str, localTypedefEntry.name(), "", localTypedefEntry, this.stream);
/*     */     }
/* 244 */     this.stream.println();
/*     */   }
/*     */ 
/*     */   protected void writeMethods()
/*     */   {
/* 259 */     Enumeration localEnumeration = this.v.contained().elements();
/* 260 */     while (localEnumeration.hasMoreElements())
/*     */     {
/* 262 */       SymtabEntry localSymtabEntry = (SymtabEntry)localEnumeration.nextElement();
/*     */       Object localObject;
/* 263 */       if ((localSymtabEntry instanceof AttributeEntry))
/*     */       {
/* 265 */         localObject = (AttributeEntry)localSymtabEntry;
/* 266 */         ((AttributeGen24)((AttributeEntry)localObject).generator()).abstractMethod(this.symbolTable, (MethodEntry)localObject, this.stream);
/*     */       }
/* 268 */       else if ((localSymtabEntry instanceof MethodEntry))
/*     */       {
/* 270 */         localObject = (MethodEntry)localSymtabEntry;
/* 271 */         ((MethodGen24)((MethodEntry)localObject).generator()).abstractMethod(this.symbolTable, (MethodEntry)localObject, this.stream);
/*     */       }
/*     */       else
/*     */       {
/* 276 */         if ((localSymtabEntry instanceof TypedefEntry)) {
/* 277 */           localSymtabEntry.type().generate(this.symbolTable, this.stream);
/*     */         }
/*     */ 
/* 281 */         localSymtabEntry.generate(this.symbolTable, this.stream);
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 288 */     if (this.v.isAbstract()) {
/* 289 */       return;
/*     */     }
/*     */ 
/* 292 */     if ((!this.v.isCustom()) && (!this.v.isAbstract()))
/* 293 */       writeStreamableMethods();
/*     */   }
/*     */ 
/*     */   public int read(int paramInt, String paramString1, String paramString2, SymtabEntry paramSymtabEntry, PrintWriter paramPrintWriter)
/*     */   {
/* 302 */     Vector localVector = ((ValueEntry)paramSymtabEntry).derivedFrom();
/* 303 */     if ((localVector != null) && (localVector.size() != 0))
/*     */     {
/* 305 */       localObject = (ValueEntry)localVector.elementAt(0);
/* 306 */       if (localObject == null) {
/* 307 */         return paramInt;
/*     */       }
/*     */ 
/* 310 */       if ((!((ValueEntry)localObject).isAbstract()) && (!Util.javaQualifiedName((SymtabEntry)localObject).equals("java.io.Serializable"))) {
/* 311 */         paramPrintWriter.println(paramString1 + "super._read (istream);");
/*     */       }
/*     */     }
/* 314 */     Object localObject = ((ValueEntry)paramSymtabEntry).state();
/* 315 */     int i = localObject == null ? 0 : ((Vector)localObject).size();
/*     */ 
/* 317 */     for (int j = 0; j < i; j++)
/*     */     {
/* 319 */       TypedefEntry localTypedefEntry = ((InterfaceState)((Vector)localObject).elementAt(j)).entry;
/* 320 */       String str = localTypedefEntry.name();
/* 321 */       SymtabEntry localSymtabEntry = localTypedefEntry.type();
/*     */ 
/* 323 */       if (((localSymtabEntry instanceof PrimitiveEntry)) || ((localSymtabEntry instanceof TypedefEntry)) || ((localSymtabEntry instanceof SequenceEntry)) || ((localSymtabEntry instanceof StringEntry)) || 
/* 327 */         (!localTypedefEntry
/* 327 */         .arrayInfo().isEmpty()))
/* 328 */         paramInt = ((JavaGenerator)localTypedefEntry.generator()).read(paramInt, paramString1, paramString2 + '.' + str, localTypedefEntry, paramPrintWriter);
/*     */       else {
/* 330 */         paramPrintWriter.println(paramString1 + paramString2 + '.' + str + " = " + 
/* 331 */           Util.helperName(localSymtabEntry, true) + 
/* 331 */           ".read (istream);");
/*     */       }
/*     */     }
/* 334 */     return paramInt;
/*     */   }
/*     */ 
/*     */   public int write(int paramInt, String paramString1, String paramString2, SymtabEntry paramSymtabEntry, PrintWriter paramPrintWriter)
/*     */   {
/* 343 */     Vector localVector = ((ValueEntry)paramSymtabEntry).derivedFrom();
/* 344 */     if ((localVector != null) && (localVector.size() != 0))
/*     */     {
/* 346 */       localObject = (ValueEntry)localVector.elementAt(0);
/* 347 */       if (localObject == null) {
/* 348 */         return paramInt;
/*     */       }
/* 350 */       if ((!((ValueEntry)localObject).isAbstract()) && (!Util.javaQualifiedName((SymtabEntry)localObject).equals("java.io.Serializable"))) {
/* 351 */         paramPrintWriter.println(paramString1 + "super._write (ostream);");
/*     */       }
/*     */     }
/* 354 */     Object localObject = ((ValueEntry)paramSymtabEntry).state();
/* 355 */     int i = localObject == null ? 0 : ((Vector)localObject).size();
/* 356 */     for (int j = 0; j < i; j++)
/*     */     {
/* 358 */       TypedefEntry localTypedefEntry = ((InterfaceState)((Vector)localObject).elementAt(j)).entry;
/* 359 */       String str = localTypedefEntry.name();
/* 360 */       SymtabEntry localSymtabEntry = localTypedefEntry.type();
/*     */ 
/* 362 */       if (((localSymtabEntry instanceof PrimitiveEntry)) || ((localSymtabEntry instanceof TypedefEntry)) || ((localSymtabEntry instanceof SequenceEntry)) || ((localSymtabEntry instanceof StringEntry)) || 
/* 366 */         (!localTypedefEntry
/* 366 */         .arrayInfo().isEmpty()))
/* 367 */         paramInt = ((JavaGenerator)localTypedefEntry.generator()).write(paramInt, paramString1, paramString2 + '.' + str, localTypedefEntry, paramPrintWriter);
/*     */       else {
/* 369 */         paramPrintWriter.println(paramString1 + Util.helperName(localSymtabEntry, true) + ".write (ostream, " + paramString2 + '.' + str + ");");
/*     */       }
/*     */     }
/*     */ 
/* 373 */     return paramInt;
/*     */   }
/*     */ 
/*     */   public void generate(Hashtable paramHashtable, ValueEntry paramValueEntry, PrintWriter paramPrintWriter)
/*     */   {
/* 381 */     this.symbolTable = paramHashtable;
/* 382 */     this.v = paramValueEntry;
/* 383 */     init();
/*     */ 
/* 385 */     openStream();
/* 386 */     if (this.stream == null)
/* 387 */       return;
/* 388 */     generateTie();
/* 389 */     generateHelper();
/* 390 */     generateHolder();
/* 391 */     if (!paramValueEntry.isAbstract()) {
/* 392 */       generateValueFactory();
/* 393 */       generateDefaultFactory();
/*     */     }
/* 395 */     writeHeading();
/* 396 */     writeBody();
/* 397 */     writeClosing();
/* 398 */     closeStream();
/*     */   }
/*     */ 
/*     */   protected void generateValueFactory()
/*     */   {
/* 406 */     ((Factories)Compile.compiler.factories()).valueFactory().generate(this.symbolTable, this.v);
/*     */   }
/*     */ 
/*     */   protected void generateDefaultFactory()
/*     */   {
/* 414 */     ((Factories)Compile.compiler.factories()).defaultFactory().generate(this.symbolTable, this.v);
/*     */   }
/*     */ 
/*     */   class ImplStreamWriter
/*     */   {
/* 143 */     private boolean isImplementsWritten = false;
/*     */ 
/*     */     ImplStreamWriter() {
/*     */     }
/* 147 */     public void writeClassName(String paramString) { if (!this.isImplementsWritten) {
/* 148 */         ValueGen24.this.stream.print(" implements ");
/* 149 */         this.isImplementsWritten = true;
/*     */       } else {
/* 151 */         ValueGen24.this.stream.print(", ");
/*     */       }
/* 153 */       ValueGen24.this.stream.print(paramString);
/*     */     }
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.corba.se.idl.toJavaPortable.ValueGen24
 * JD-Core Version:    0.6.2
 */