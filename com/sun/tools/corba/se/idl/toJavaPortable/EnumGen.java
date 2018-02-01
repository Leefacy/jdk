/*     */ package com.sun.tools.corba.se.idl.toJavaPortable;
/*     */ 
/*     */ import com.sun.tools.corba.se.idl.Comment;
/*     */ import com.sun.tools.corba.se.idl.EnumEntry;
/*     */ import com.sun.tools.corba.se.idl.GenFileStream;
/*     */ import com.sun.tools.corba.se.idl.SymtabEntry;
/*     */ import java.io.PrintWriter;
/*     */ import java.util.Enumeration;
/*     */ import java.util.Hashtable;
/*     */ import java.util.Vector;
/*     */ 
/*     */ public class EnumGen
/*     */   implements com.sun.tools.corba.se.idl.EnumGen, JavaGenerator
/*     */ {
/* 263 */   protected Hashtable symbolTable = null;
/* 264 */   protected EnumEntry e = null;
/* 265 */   protected PrintWriter stream = null;
/*     */ 
/* 268 */   String className = null;
/* 269 */   String fullClassName = null;
/*     */ 
/*     */   public void generate(Hashtable paramHashtable, EnumEntry paramEnumEntry, PrintWriter paramPrintWriter)
/*     */   {
/*  68 */     this.symbolTable = paramHashtable;
/*  69 */     this.e = paramEnumEntry;
/*  70 */     init();
/*     */ 
/*  72 */     openStream();
/*  73 */     if (this.stream == null) return;
/*  74 */     generateHolder();
/*  75 */     generateHelper();
/*  76 */     writeHeading();
/*  77 */     writeBody();
/*  78 */     writeClosing();
/*  79 */     closeStream();
/*     */   }
/*     */ 
/*     */   protected void init()
/*     */   {
/*  87 */     this.className = this.e.name();
/*  88 */     this.fullClassName = Util.javaName(this.e);
/*     */   }
/*     */ 
/*     */   protected void openStream()
/*     */   {
/*  96 */     this.stream = Util.stream(this.e, ".java");
/*     */   }
/*     */ 
/*     */   protected void generateHolder()
/*     */   {
/* 104 */     ((Factories)Compile.compiler.factories()).holder().generate(this.symbolTable, this.e);
/*     */   }
/*     */ 
/*     */   protected void generateHelper()
/*     */   {
/* 112 */     ((Factories)Compile.compiler.factories()).helper().generate(this.symbolTable, this.e);
/*     */   }
/*     */ 
/*     */   protected void writeHeading()
/*     */   {
/* 121 */     Util.writePackage(this.stream, this.e);
/* 122 */     Util.writeProlog(this.stream, ((GenFileStream)this.stream).name());
/* 123 */     if (this.e.comment() != null)
/* 124 */       this.e.comment().generate("", this.stream);
/* 125 */     this.stream.println("public class " + this.className + " implements org.omg.CORBA.portable.IDLEntity");
/* 126 */     this.stream.println("{");
/*     */   }
/*     */ 
/*     */   protected void writeBody()
/*     */   {
/* 134 */     this.stream.println("  private        int __value;");
/* 135 */     this.stream.println("  private static int __size = " + this.e.elements().size() + ';');
/* 136 */     this.stream.println("  private static " + this.fullClassName + "[] __array = new " + this.fullClassName + " [__size];");
/* 137 */     this.stream.println();
/* 138 */     for (int i = 0; i < this.e.elements().size(); i++)
/*     */     {
/* 140 */       String str = (String)this.e.elements().elementAt(i);
/* 141 */       this.stream.println("  public static final int _" + str + " = " + i + ';');
/* 142 */       this.stream.println("  public static final " + this.fullClassName + ' ' + str + " = new " + this.fullClassName + "(_" + str + ");");
/*     */     }
/* 144 */     this.stream.println();
/* 145 */     writeValue();
/* 146 */     writeFromInt();
/* 147 */     writeCtors();
/*     */   }
/*     */ 
/*     */   protected void writeValue()
/*     */   {
/* 155 */     this.stream.println("  public int value ()");
/* 156 */     this.stream.println("  {");
/* 157 */     this.stream.println("    return __value;");
/* 158 */     this.stream.println("  }");
/* 159 */     this.stream.println();
/*     */   }
/*     */ 
/*     */   protected void writeFromInt()
/*     */   {
/* 167 */     this.stream.println("  public static " + this.fullClassName + " from_int (int value)");
/* 168 */     this.stream.println("  {");
/* 169 */     this.stream.println("    if (value >= 0 && value < __size)");
/* 170 */     this.stream.println("      return __array[value];");
/* 171 */     this.stream.println("    else");
/* 172 */     this.stream.println("      throw new org.omg.CORBA.BAD_PARAM ();");
/* 173 */     this.stream.println("  }");
/* 174 */     this.stream.println();
/*     */   }
/*     */ 
/*     */   protected void writeCtors()
/*     */   {
/* 182 */     this.stream.println("  protected " + this.className + " (int value)");
/* 183 */     this.stream.println("  {");
/* 184 */     this.stream.println("    __value = value;");
/* 185 */     this.stream.println("    __array[__value] = this;");
/* 186 */     this.stream.println("  }");
/*     */   }
/*     */ 
/*     */   protected void writeClosing()
/*     */   {
/* 194 */     this.stream.println("} // class " + this.className);
/*     */   }
/*     */ 
/*     */   protected void closeStream()
/*     */   {
/* 202 */     this.stream.close();
/*     */   }
/*     */ 
/*     */   public int helperType(int paramInt, String paramString1, TCOffsets paramTCOffsets, String paramString2, SymtabEntry paramSymtabEntry, PrintWriter paramPrintWriter)
/*     */   {
/* 210 */     paramTCOffsets.set(paramSymtabEntry);
/* 211 */     EnumEntry localEnumEntry = (EnumEntry)paramSymtabEntry;
/* 212 */     StringBuffer localStringBuffer = new StringBuffer("new String[] { ");
/* 213 */     Enumeration localEnumeration = localEnumEntry.elements().elements();
/* 214 */     int i = 1;
/* 215 */     while (localEnumeration.hasMoreElements())
/*     */     {
/* 217 */       if (i != 0)
/* 218 */         i = 0;
/*     */       else
/* 220 */         localStringBuffer.append(", ");
/* 221 */       localStringBuffer.append('"' + Util.stripLeadingUnderscores((String)localEnumeration.nextElement()) + '"');
/*     */     }
/* 223 */     localStringBuffer.append("} ");
/* 224 */     paramPrintWriter.println(paramString1 + paramString2 + " = org.omg.CORBA.ORB.init ().create_enum_tc (" + 
/* 225 */       Util.helperName(localEnumEntry, true) + 
/* 225 */       ".id (), \"" + 
/* 227 */       Util.stripLeadingUnderscores(paramSymtabEntry
/* 227 */       .name()) + "\", " + new String(localStringBuffer) + ");");
/*     */ 
/* 229 */     return paramInt + 1;
/*     */   }
/*     */ 
/*     */   public int type(int paramInt, String paramString1, TCOffsets paramTCOffsets, String paramString2, SymtabEntry paramSymtabEntry, PrintWriter paramPrintWriter)
/*     */   {
/* 234 */     paramPrintWriter.println(paramString1 + paramString2 + " = " + Util.helperName(paramSymtabEntry, true) + ".type ();");
/* 235 */     return paramInt;
/*     */   }
/*     */ 
/*     */   public void helperRead(String paramString, SymtabEntry paramSymtabEntry, PrintWriter paramPrintWriter)
/*     */   {
/* 240 */     paramPrintWriter.println("    return " + Util.javaQualifiedName(paramSymtabEntry) + ".from_int (istream.read_long ());");
/*     */   }
/*     */ 
/*     */   public void helperWrite(SymtabEntry paramSymtabEntry, PrintWriter paramPrintWriter)
/*     */   {
/* 245 */     paramPrintWriter.println("    ostream.write_long (value.value ());");
/*     */   }
/*     */ 
/*     */   public int read(int paramInt, String paramString1, String paramString2, SymtabEntry paramSymtabEntry, PrintWriter paramPrintWriter)
/*     */   {
/* 250 */     paramPrintWriter.println(paramString1 + paramString2 + " = " + Util.javaQualifiedName(paramSymtabEntry) + ".from_int (istream.read_long ());");
/* 251 */     return paramInt;
/*     */   }
/*     */ 
/*     */   public int write(int paramInt, String paramString1, String paramString2, SymtabEntry paramSymtabEntry, PrintWriter paramPrintWriter)
/*     */   {
/* 256 */     paramPrintWriter.println(paramString1 + "ostream.write_long (" + paramString2 + ".value ());");
/* 257 */     return paramInt;
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.corba.se.idl.toJavaPortable.EnumGen
 * JD-Core Version:    0.6.2
 */