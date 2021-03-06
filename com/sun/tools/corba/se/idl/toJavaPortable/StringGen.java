/*     */ package com.sun.tools.corba.se.idl.toJavaPortable;
/*     */ 
/*     */ import com.sun.tools.corba.se.idl.StringEntry;
/*     */ import com.sun.tools.corba.se.idl.SymtabEntry;
/*     */ import java.io.PrintWriter;
/*     */ import java.util.Hashtable;
/*     */ 
/*     */ public class StringGen
/*     */   implements com.sun.tools.corba.se.idl.StringGen, JavaGenerator
/*     */ {
/*     */   public void generate(Hashtable paramHashtable, StringEntry paramStringEntry, PrintWriter paramPrintWriter)
/*     */   {
/*     */   }
/*     */ 
/*     */   public int helperType(int paramInt, String paramString1, TCOffsets paramTCOffsets, String paramString2, SymtabEntry paramSymtabEntry, PrintWriter paramPrintWriter)
/*     */   {
/*  73 */     return type(paramInt, paramString1, paramTCOffsets, paramString2, paramSymtabEntry, paramPrintWriter);
/*     */   }
/*     */ 
/*     */   public int type(int paramInt, String paramString1, TCOffsets paramTCOffsets, String paramString2, SymtabEntry paramSymtabEntry, PrintWriter paramPrintWriter) {
/*  77 */     paramTCOffsets.set(paramSymtabEntry);
/*  78 */     StringEntry localStringEntry = (StringEntry)paramSymtabEntry;
/*     */     String str;
/*  80 */     if (localStringEntry.maxSize() == null)
/*  81 */       str = "0";
/*     */     else {
/*  83 */       str = Util.parseExpression(localStringEntry.maxSize());
/*     */     }
/*     */ 
/*  88 */     paramPrintWriter.println(paramString1 + paramString2 + " = org.omg.CORBA.ORB.init ().create_" + paramSymtabEntry
/*  91 */       .name() + "_tc (" + str + ");");
/*     */ 
/*  94 */     return paramInt;
/*     */   }
/*     */ 
/*     */   public void helperRead(String paramString, SymtabEntry paramSymtabEntry, PrintWriter paramPrintWriter)
/*     */   {
/*     */   }
/*     */ 
/*     */   public void helperWrite(SymtabEntry paramSymtabEntry, PrintWriter paramPrintWriter)
/*     */   {
/*     */   }
/*     */ 
/*     */   public int read(int paramInt, String paramString1, String paramString2, SymtabEntry paramSymtabEntry, PrintWriter paramPrintWriter)
/*     */   {
/* 107 */     StringEntry localStringEntry = (StringEntry)paramSymtabEntry;
/* 108 */     String str = paramSymtabEntry.name();
/* 109 */     if (str.equals("string"))
/* 110 */       paramPrintWriter.println(paramString1 + paramString2 + " = istream.read_string ();");
/* 111 */     else if (str.equals("wstring"))
/* 112 */       paramPrintWriter.println(paramString1 + paramString2 + " = istream.read_wstring ();");
/* 113 */     if (localStringEntry.maxSize() != null)
/*     */     {
/* 115 */       paramPrintWriter.println(paramString1 + "if (" + paramString2 + ".length () > (" + Util.parseExpression(localStringEntry.maxSize()) + "))");
/* 116 */       paramPrintWriter.println(paramString1 + "  throw new org.omg.CORBA.MARSHAL (0, org.omg.CORBA.CompletionStatus.COMPLETED_MAYBE);");
/*     */     }
/* 118 */     return paramInt;
/*     */   }
/*     */ 
/*     */   public int write(int paramInt, String paramString1, String paramString2, SymtabEntry paramSymtabEntry, PrintWriter paramPrintWriter)
/*     */   {
/* 123 */     StringEntry localStringEntry = (StringEntry)paramSymtabEntry;
/* 124 */     if (localStringEntry.maxSize() != null)
/*     */     {
/* 126 */       paramPrintWriter.print(paramString1 + "if (" + paramString2 + ".length () > (" + Util.parseExpression(localStringEntry.maxSize()) + "))");
/* 127 */       paramPrintWriter.println(paramString1 + "  throw new org.omg.CORBA.MARSHAL (0, org.omg.CORBA.CompletionStatus.COMPLETED_MAYBE);");
/*     */     }
/* 129 */     String str = paramSymtabEntry.name();
/* 130 */     if (str.equals("string"))
/* 131 */       paramPrintWriter.println(paramString1 + "ostream.write_string (" + paramString2 + ");");
/* 132 */     else if (str.equals("wstring"))
/* 133 */       paramPrintWriter.println(paramString1 + "ostream.write_wstring (" + paramString2 + ");");
/* 134 */     return paramInt;
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.corba.se.idl.toJavaPortable.StringGen
 * JD-Core Version:    0.6.2
 */