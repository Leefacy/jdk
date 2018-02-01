/*     */ package com.sun.tools.corba.se.idl.toJavaPortable;
/*     */ 
/*     */ import com.sun.tools.corba.se.idl.Comment;
/*     */ import com.sun.tools.corba.se.idl.GenFileStream;
/*     */ import com.sun.tools.corba.se.idl.InterfaceState;
/*     */ import com.sun.tools.corba.se.idl.PrimitiveEntry;
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
/*     */ public class ValueBoxGen
/*     */   implements com.sun.tools.corba.se.idl.ValueBoxGen, JavaGenerator
/*     */ {
/* 346 */   protected Hashtable symbolTable = null;
/* 347 */   protected ValueBoxEntry v = null;
/* 348 */   protected PrintWriter stream = null;
/*     */ 
/*     */   public void generate(Hashtable paramHashtable, ValueBoxEntry paramValueBoxEntry, PrintWriter paramPrintWriter)
/*     */   {
/*  78 */     this.symbolTable = paramHashtable;
/*  79 */     this.v = paramValueBoxEntry;
/*     */ 
/*  81 */     TypedefEntry localTypedefEntry = ((InterfaceState)paramValueBoxEntry.state().elementAt(0)).entry;
/*  82 */     SymtabEntry localSymtabEntry1 = localTypedefEntry.type();
/*     */ 
/*  84 */     if ((localSymtabEntry1 instanceof PrimitiveEntry))
/*     */     {
/*  86 */       openStream();
/*  87 */       if (this.stream == null)
/*  88 */         return;
/*  89 */       writeHeading();
/*  90 */       writeBody();
/*  91 */       writeClosing();
/*  92 */       closeStream();
/*     */     }
/*     */     else
/*     */     {
/*  99 */       Enumeration localEnumeration = paramValueBoxEntry.contained().elements();
/* 100 */       while (localEnumeration.hasMoreElements())
/*     */       {
/* 102 */         SymtabEntry localSymtabEntry2 = (SymtabEntry)localEnumeration.nextElement();
/*     */ 
/* 107 */         if (localSymtabEntry2.type() != null)
/* 108 */           localSymtabEntry2.type().generate(paramHashtable, this.stream);
/*     */       }
/*     */     }
/* 111 */     generateHelper();
/* 112 */     generateHolder();
/*     */   }
/*     */ 
/*     */   protected void openStream()
/*     */   {
/* 120 */     this.stream = Util.stream(this.v, ".java");
/*     */   }
/*     */ 
/*     */   protected void generateHelper()
/*     */   {
/* 128 */     ((Factories)Compile.compiler.factories()).helper().generate(this.symbolTable, this.v);
/*     */   }
/*     */ 
/*     */   protected void generateHolder()
/*     */   {
/* 136 */     ((Factories)Compile.compiler.factories()).holder().generate(this.symbolTable, this.v);
/*     */   }
/*     */ 
/*     */   protected void writeHeading()
/*     */   {
/* 144 */     Util.writePackage(this.stream, this.v);
/* 145 */     Util.writeProlog(this.stream, ((GenFileStream)this.stream).name());
/* 146 */     if (this.v.comment() != null) {
/* 147 */       this.v.comment().generate("", this.stream);
/*     */     }
/* 149 */     this.stream.println("public class " + this.v.name() + " implements org.omg.CORBA.portable.ValueBase");
/* 150 */     this.stream.println("{");
/*     */   }
/*     */ 
/*     */   protected void writeBody()
/*     */   {
/* 158 */     InterfaceState localInterfaceState = (InterfaceState)this.v.state().elementAt(0);
/* 159 */     TypedefEntry localTypedefEntry = localInterfaceState.entry;
/* 160 */     Util.fillInfo(localTypedefEntry);
/* 161 */     if (localTypedefEntry.comment() != null)
/* 162 */       localTypedefEntry.comment().generate(" ", this.stream);
/* 163 */     this.stream.println("  public " + Util.javaName(localTypedefEntry) + " value;");
/* 164 */     this.stream.println("  public " + this.v.name() + " (" + Util.javaName(localTypedefEntry) + " initial)");
/* 165 */     this.stream.println("  {");
/* 166 */     this.stream.println("    value = initial;");
/* 167 */     this.stream.println("  }");
/* 168 */     this.stream.println();
/* 169 */     writeTruncatable();
/*     */   }
/*     */ 
/*     */   protected void writeTruncatable()
/*     */   {
/* 179 */     this.stream.println("  public String[] _truncatable_ids() {");
/* 180 */     this.stream.println("      return " + Util.helperName(this.v, true) + ".get_instance().get_truncatable_base_ids();");
/* 181 */     this.stream.println("  }");
/* 182 */     this.stream.println();
/*     */   }
/*     */ 
/*     */   protected void writeClosing()
/*     */   {
/* 190 */     this.stream.println("} // class " + this.v.name());
/*     */   }
/*     */ 
/*     */   protected void closeStream()
/*     */   {
/* 198 */     this.stream.close();
/*     */   }
/*     */ 
/*     */   protected void writeStreamableMethods()
/*     */   {
/* 206 */     this.stream.println("  public void _read (org.omg.CORBA.portable.InputStream istream)");
/* 207 */     this.stream.println("  {");
/* 208 */     streamableRead("this", this.v, this.stream);
/* 209 */     this.stream.println("  }");
/* 210 */     this.stream.println();
/* 211 */     this.stream.println("  public void _write (org.omg.CORBA.portable.OutputStream ostream)");
/* 212 */     this.stream.println("  {");
/* 213 */     write(0, "    ", "this", this.v, this.stream);
/* 214 */     this.stream.println("  }");
/* 215 */     this.stream.println();
/* 216 */     this.stream.println("  public org.omg.CORBA.TypeCode _type ()");
/* 217 */     this.stream.println("  {");
/* 218 */     this.stream.println("    return " + Util.helperName(this.v, false) + ".type ();");
/* 219 */     this.stream.println("  }");
/*     */   }
/*     */ 
/*     */   public int helperType(int paramInt, String paramString1, TCOffsets paramTCOffsets, String paramString2, SymtabEntry paramSymtabEntry, PrintWriter paramPrintWriter)
/*     */   {
/* 227 */     ValueEntry localValueEntry = (ValueEntry)paramSymtabEntry;
/* 228 */     TypedefEntry localTypedefEntry = ((InterfaceState)localValueEntry.state().elementAt(0)).entry;
/* 229 */     SymtabEntry localSymtabEntry = Util.typeOf(localTypedefEntry);
/* 230 */     paramInt = ((JavaGenerator)localSymtabEntry.generator()).type(paramInt, paramString1, paramTCOffsets, paramString2, localSymtabEntry, paramPrintWriter);
/* 231 */     paramPrintWriter.println(paramString1 + paramString2 + " = org.omg.CORBA.ORB.init ().create_value_box_tc (" + "_id, " + '"' + paramSymtabEntry
/* 233 */       .name() + "\", " + paramString2 + ");");
/*     */ 
/* 236 */     return paramInt;
/*     */   }
/*     */ 
/*     */   public int type(int paramInt, String paramString1, TCOffsets paramTCOffsets, String paramString2, SymtabEntry paramSymtabEntry, PrintWriter paramPrintWriter) {
/* 240 */     paramPrintWriter.println(paramString1 + paramString2 + " = " + Util.helperName(paramSymtabEntry, true) + ".type ();");
/* 241 */     return paramInt;
/*     */   }
/*     */ 
/*     */   public int read(int paramInt, String paramString1, String paramString2, SymtabEntry paramSymtabEntry, PrintWriter paramPrintWriter)
/*     */   {
/* 246 */     return paramInt;
/*     */   }
/*     */ 
/*     */   public void helperRead(String paramString, SymtabEntry paramSymtabEntry, PrintWriter paramPrintWriter)
/*     */   {
/* 253 */     paramPrintWriter.println("    return (" + paramString + ") ((org.omg.CORBA_2_3.portable.InputStream) istream).read_value (get_instance());");
/* 254 */     paramPrintWriter.println("  }");
/* 255 */     paramPrintWriter.println();
/*     */ 
/* 259 */     paramPrintWriter.println("  public java.io.Serializable read_value (org.omg.CORBA.portable.InputStream istream)");
/* 260 */     paramPrintWriter.println("  {");
/*     */ 
/* 263 */     String str = "    ";
/* 264 */     Vector localVector = ((ValueBoxEntry)paramSymtabEntry).state();
/* 265 */     TypedefEntry localTypedefEntry = ((InterfaceState)localVector.elementAt(0)).entry;
/* 266 */     SymtabEntry localSymtabEntry = localTypedefEntry.type();
/* 267 */     if (((localSymtabEntry instanceof PrimitiveEntry)) || ((localSymtabEntry instanceof SequenceEntry)) || ((localSymtabEntry instanceof TypedefEntry)) || ((localSymtabEntry instanceof StringEntry)) || 
/* 271 */       (!localTypedefEntry
/* 271 */       .arrayInfo().isEmpty())) {
/* 272 */       paramPrintWriter.println(str + Util.javaName(localSymtabEntry) + " tmp;");
/* 273 */       ((JavaGenerator)localTypedefEntry.generator()).read(0, str, "tmp", localTypedefEntry, paramPrintWriter);
/*     */     }
/* 275 */     else if (((localSymtabEntry instanceof ValueEntry)) || ((localSymtabEntry instanceof ValueBoxEntry))) {
/* 276 */       paramPrintWriter.println(str + Util.javaQualifiedName(localSymtabEntry) + " tmp = (" + 
/* 277 */         Util.javaQualifiedName(localSymtabEntry) + 
/* 277 */         ") ((org.omg.CORBA_2_3.portable.InputStream)istream).read_value (" + Util.helperName(localSymtabEntry, true) + ".get_instance ());");
/*     */     } else {
/* 279 */       paramPrintWriter.println(str + Util.javaName(localSymtabEntry) + " tmp = " + 
/* 280 */         Util.helperName(localSymtabEntry, true) + 
/* 280 */         ".read (istream);");
/* 281 */     }if ((localSymtabEntry instanceof PrimitiveEntry))
/* 282 */       paramPrintWriter.println(str + "return new " + paramString + " (tmp);");
/*     */     else
/* 284 */       paramPrintWriter.println(str + "return tmp;");
/*     */   }
/*     */ 
/*     */   public void helperWrite(SymtabEntry paramSymtabEntry, PrintWriter paramPrintWriter)
/*     */   {
/* 291 */     paramPrintWriter.println("    ((org.omg.CORBA_2_3.portable.OutputStream) ostream).write_value (value, get_instance());");
/* 292 */     paramPrintWriter.println("  }");
/* 293 */     paramPrintWriter.println();
/*     */ 
/* 297 */     paramPrintWriter.println("  public void write_value (org.omg.CORBA.portable.OutputStream ostream, java.io.Serializable obj)");
/* 298 */     paramPrintWriter.println("  {");
/*     */ 
/* 300 */     String str = Util.javaName(paramSymtabEntry);
/* 301 */     paramPrintWriter.println("    " + str + " value  = (" + str + ") obj;");
/* 302 */     write(0, "    ", "value", paramSymtabEntry, paramPrintWriter);
/*     */   }
/*     */ 
/*     */   public int write(int paramInt, String paramString1, String paramString2, SymtabEntry paramSymtabEntry, PrintWriter paramPrintWriter)
/*     */   {
/* 307 */     Vector localVector = ((ValueEntry)paramSymtabEntry).state();
/* 308 */     TypedefEntry localTypedefEntry = ((InterfaceState)localVector.elementAt(0)).entry;
/* 309 */     SymtabEntry localSymtabEntry = localTypedefEntry.type();
/*     */ 
/* 311 */     if (((localSymtabEntry instanceof PrimitiveEntry)) || (!localTypedefEntry.arrayInfo().isEmpty()))
/* 312 */       paramInt = ((JavaGenerator)localTypedefEntry.generator()).write(paramInt, paramString1, paramString2 + ".value", localTypedefEntry, paramPrintWriter);
/* 313 */     else if (((localSymtabEntry instanceof SequenceEntry)) || ((localSymtabEntry instanceof StringEntry)) || ((localSymtabEntry instanceof TypedefEntry)) || (!localTypedefEntry.arrayInfo().isEmpty()))
/* 314 */       paramInt = ((JavaGenerator)localTypedefEntry.generator()).write(paramInt, paramString1, paramString2, localTypedefEntry, paramPrintWriter);
/* 315 */     else if (((localSymtabEntry instanceof ValueEntry)) || ((localSymtabEntry instanceof ValueBoxEntry))) {
/* 316 */       paramPrintWriter.println(paramString1 + "((org.omg.CORBA_2_3.portable.OutputStream)ostream).write_value ((java.io.Serializable) value, " + 
/* 318 */         Util.helperName(localSymtabEntry, true) + 
/* 318 */         ".get_instance ());");
/*     */     }
/*     */     else
/* 321 */       paramPrintWriter.println(paramString1 + Util.helperName(localSymtabEntry, true) + ".write (ostream, " + paramString2 + ");");
/* 322 */     return paramInt;
/*     */   }
/*     */ 
/*     */   protected void writeAbstract()
/*     */   {
/*     */   }
/*     */ 
/*     */   protected void streamableRead(String paramString, SymtabEntry paramSymtabEntry, PrintWriter paramPrintWriter)
/*     */   {
/* 331 */     Vector localVector = ((ValueBoxEntry)paramSymtabEntry).state();
/* 332 */     TypedefEntry localTypedefEntry1 = ((InterfaceState)localVector.elementAt(0)).entry;
/* 333 */     SymtabEntry localSymtabEntry = localTypedefEntry1.type();
/* 334 */     if (((localSymtabEntry instanceof PrimitiveEntry)) || ((localSymtabEntry instanceof SequenceEntry)) || ((localSymtabEntry instanceof TypedefEntry)) || ((localSymtabEntry instanceof StringEntry)) || 
/* 335 */       (!localTypedefEntry1
/* 335 */       .arrayInfo().isEmpty()))
/*     */     {
/* 337 */       TypedefEntry localTypedefEntry2 = ((InterfaceState)localVector.elementAt(0)).entry;
/* 338 */       ((JavaGenerator)localTypedefEntry1.generator()).read(0, "    ", paramString + ".value", localTypedefEntry1, paramPrintWriter);
/*     */     }
/* 340 */     else if (((localSymtabEntry instanceof ValueEntry)) || ((localSymtabEntry instanceof ValueBoxEntry))) {
/* 341 */       paramPrintWriter.println("    " + paramString + ".value = (" + Util.javaQualifiedName(localSymtabEntry) + ") ((org.omg.CORBA_2_3.portable.InputStream)istream).read_value (" + Util.helperName(localSymtabEntry, true) + ".get_instance ());");
/*     */     } else {
/* 343 */       paramPrintWriter.println("    " + paramString + ".value = " + Util.helperName(localSymtabEntry, true) + ".read (istream);");
/*     */     }
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.corba.se.idl.toJavaPortable.ValueBoxGen
 * JD-Core Version:    0.6.2
 */