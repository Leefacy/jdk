/*     */ package com.sun.tools.corba.se.idl.toJavaPortable;
/*     */ 
/*     */ import com.sun.tools.corba.se.idl.InterfaceState;
/*     */ import com.sun.tools.corba.se.idl.PrimitiveEntry;
/*     */ import com.sun.tools.corba.se.idl.SequenceEntry;
/*     */ import com.sun.tools.corba.se.idl.StringEntry;
/*     */ import com.sun.tools.corba.se.idl.SymtabEntry;
/*     */ import com.sun.tools.corba.se.idl.TypedefEntry;
/*     */ import com.sun.tools.corba.se.idl.ValueBoxEntry;
/*     */ import com.sun.tools.corba.se.idl.ValueEntry;
/*     */ import java.io.PrintWriter;
/*     */ import java.util.Vector;
/*     */ 
/*     */ public class ValueBoxGen24 extends ValueBoxGen
/*     */ {
/*     */   protected void writeTruncatable()
/*     */   {
/*  76 */     this.stream.print("  private static String[] _truncatable_ids = {");
/*  77 */     this.stream.println(Util.helperName(this.v, true) + ".id ()};");
/*  78 */     this.stream.println();
/*  79 */     this.stream.println("  public String[] _truncatable_ids() {");
/*  80 */     this.stream.println("    return _truncatable_ids;");
/*  81 */     this.stream.println("  }");
/*  82 */     this.stream.println();
/*     */   }
/*     */ 
/*     */   public void helperRead(String paramString, SymtabEntry paramSymtabEntry, PrintWriter paramPrintWriter)
/*     */   {
/*  91 */     paramPrintWriter.println("    if (!(istream instanceof org.omg.CORBA_2_3.portable.InputStream)) {");
/*  92 */     paramPrintWriter.println("      throw new org.omg.CORBA.BAD_PARAM(); }");
/*  93 */     paramPrintWriter.println("    return (" + paramString + ") ((org.omg.CORBA_2_3.portable.InputStream) istream).read_value (_instance);");
/*  94 */     paramPrintWriter.println("  }");
/*  95 */     paramPrintWriter.println();
/*     */ 
/*  99 */     paramPrintWriter.println("  public java.io.Serializable read_value (org.omg.CORBA.portable.InputStream istream)");
/* 100 */     paramPrintWriter.println("  {");
/*     */ 
/* 102 */     String str = "    ";
/* 103 */     Vector localVector = ((ValueBoxEntry)paramSymtabEntry).state();
/* 104 */     TypedefEntry localTypedefEntry = ((InterfaceState)localVector.elementAt(0)).entry;
/* 105 */     SymtabEntry localSymtabEntry = localTypedefEntry.type();
/* 106 */     if (((localSymtabEntry instanceof PrimitiveEntry)) || ((localSymtabEntry instanceof SequenceEntry)) || ((localSymtabEntry instanceof TypedefEntry)) || ((localSymtabEntry instanceof StringEntry)) || 
/* 110 */       (!localTypedefEntry
/* 110 */       .arrayInfo().isEmpty())) {
/* 111 */       paramPrintWriter.println(str + Util.javaName(localSymtabEntry) + " tmp;");
/* 112 */       ((JavaGenerator)localTypedefEntry.generator()).read(0, str, "tmp", localTypedefEntry, paramPrintWriter);
/*     */     }
/*     */     else {
/* 115 */       paramPrintWriter.println(str + Util.javaName(localSymtabEntry) + " tmp = " + 
/* 116 */         Util.helperName(localSymtabEntry, true) + 
/* 116 */         ".read (istream);");
/* 117 */     }if ((localSymtabEntry instanceof PrimitiveEntry))
/* 118 */       paramPrintWriter.println(str + "return new " + paramString + " (tmp);");
/*     */     else
/* 120 */       paramPrintWriter.println(str + "return (java.io.Serializable) tmp;");
/*     */   }
/*     */ 
/*     */   public void helperWrite(SymtabEntry paramSymtabEntry, PrintWriter paramPrintWriter)
/*     */   {
/* 128 */     paramPrintWriter.println("    if (!(ostream instanceof org.omg.CORBA_2_3.portable.OutputStream)) {");
/* 129 */     paramPrintWriter.println("      throw new org.omg.CORBA.BAD_PARAM(); }");
/* 130 */     paramPrintWriter.println("    ((org.omg.CORBA_2_3.portable.OutputStream) ostream).write_value (value, _instance);");
/* 131 */     paramPrintWriter.println("  }");
/* 132 */     paramPrintWriter.println();
/*     */ 
/* 136 */     paramPrintWriter.println("  public void write_value (org.omg.CORBA.portable.OutputStream ostream, java.io.Serializable value)");
/* 137 */     paramPrintWriter.println("  {");
/*     */ 
/* 139 */     String str = Util.javaName(paramSymtabEntry);
/* 140 */     paramPrintWriter.println("    if (!(value instanceof " + str + ")) {");
/* 141 */     paramPrintWriter.println("      throw new org.omg.CORBA.MARSHAL(); }");
/* 142 */     paramPrintWriter.println("    " + str + " valueType = (" + str + ") value;");
/* 143 */     write(0, "    ", "valueType", paramSymtabEntry, paramPrintWriter);
/*     */   }
/*     */ 
/*     */   public int write(int paramInt, String paramString1, String paramString2, SymtabEntry paramSymtabEntry, PrintWriter paramPrintWriter)
/*     */   {
/* 151 */     Vector localVector = ((ValueEntry)paramSymtabEntry).state();
/* 152 */     TypedefEntry localTypedefEntry = ((InterfaceState)localVector.elementAt(0)).entry;
/* 153 */     SymtabEntry localSymtabEntry = localTypedefEntry.type();
/*     */ 
/* 155 */     if (((localSymtabEntry instanceof PrimitiveEntry)) || (!localTypedefEntry.arrayInfo().isEmpty()))
/* 156 */       paramInt = ((JavaGenerator)localTypedefEntry.generator()).write(paramInt, paramString1, paramString2 + ".value", localTypedefEntry, paramPrintWriter);
/* 157 */     else if (((localSymtabEntry instanceof SequenceEntry)) || ((localSymtabEntry instanceof StringEntry)) || ((localSymtabEntry instanceof TypedefEntry)) || (!localTypedefEntry.arrayInfo().isEmpty()))
/* 158 */       paramInt = ((JavaGenerator)localTypedefEntry.generator()).write(paramInt, paramString1, paramString2, localTypedefEntry, paramPrintWriter);
/*     */     else
/* 160 */       paramPrintWriter.println(paramString1 + Util.helperName(localSymtabEntry, true) + ".write (ostream, " + paramString2 + ");");
/* 161 */     return paramInt;
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.corba.se.idl.toJavaPortable.ValueBoxGen24
 * JD-Core Version:    0.6.2
 */