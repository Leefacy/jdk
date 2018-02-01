/*     */ package com.sun.tools.corba.se.idl.toJavaPortable;
/*     */ 
/*     */ import com.sun.tools.corba.se.idl.InterfaceEntry;
/*     */ import com.sun.tools.corba.se.idl.PrimitiveEntry;
/*     */ import com.sun.tools.corba.se.idl.SequenceEntry;
/*     */ import com.sun.tools.corba.se.idl.StringEntry;
/*     */ import com.sun.tools.corba.se.idl.SymtabEntry;
/*     */ import com.sun.tools.corba.se.idl.constExpr.Expression;
/*     */ import java.io.PrintWriter;
/*     */ import java.util.Hashtable;
/*     */ 
/*     */ public class SequenceGen
/*     */   implements com.sun.tools.corba.se.idl.SequenceGen, JavaGenerator
/*     */ {
/*     */   public void generate(Hashtable paramHashtable, SequenceEntry paramSequenceEntry, PrintWriter paramPrintWriter)
/*     */   {
/*     */   }
/*     */ 
/*     */   public int helperType(int paramInt, String paramString1, TCOffsets paramTCOffsets, String paramString2, SymtabEntry paramSymtabEntry, PrintWriter paramPrintWriter)
/*     */   {
/*  77 */     int i = paramTCOffsets.offset(paramSymtabEntry.type().fullName());
/*     */     Expression localExpression;
/*  78 */     if (i >= 0)
/*     */     {
/*  86 */       paramTCOffsets.set(null);
/*  87 */       localExpression = ((SequenceEntry)paramSymtabEntry).maxSize();
/*  88 */       if (localExpression == null)
/*  89 */         paramPrintWriter.println(paramString1 + paramString2 + " = org.omg.CORBA.ORB.init ().create_recursive_sequence_tc (0, " + (i - paramTCOffsets.currentOffset()) + ");");
/*     */       else
/*  91 */         paramPrintWriter.println(paramString1 + paramString2 + " = org.omg.CORBA.ORB.init ().create_recursive_sequence_tc (" + Util.parseExpression(localExpression) + ", " + (i - paramTCOffsets.currentOffset()) + ");");
/*  92 */       paramTCOffsets.bumpCurrentOffset(4);
/*     */     }
/*     */     else
/*     */     {
/*  97 */       paramTCOffsets.set(paramSymtabEntry);
/*  98 */       paramInt = ((JavaGenerator)paramSymtabEntry.type().generator()).helperType(paramInt + 1, paramString1, paramTCOffsets, paramString2, paramSymtabEntry.type(), paramPrintWriter);
/*  99 */       localExpression = ((SequenceEntry)paramSymtabEntry).maxSize();
/* 100 */       if (localExpression == null)
/* 101 */         paramPrintWriter.println(paramString1 + paramString2 + " = org.omg.CORBA.ORB.init ().create_sequence_tc (0, " + paramString2 + ");");
/*     */       else
/* 103 */         paramPrintWriter.println(paramString1 + paramString2 + " = org.omg.CORBA.ORB.init ().create_sequence_tc (" + Util.parseExpression(localExpression) + ", " + paramString2 + ");");
/*     */     }
/* 105 */     paramTCOffsets.bumpCurrentOffset(4);
/* 106 */     return paramInt;
/*     */   }
/*     */ 
/*     */   public int type(int paramInt, String paramString1, TCOffsets paramTCOffsets, String paramString2, SymtabEntry paramSymtabEntry, PrintWriter paramPrintWriter) {
/* 110 */     int i = paramTCOffsets.offset(paramSymtabEntry.type().fullName());
/* 111 */     if (i >= 0)
/*     */     {
/* 114 */       paramTCOffsets.set(null);
/*     */ 
/* 120 */       paramPrintWriter.println(paramString1 + paramString2 + " = org.omg.CORBA.ORB.init ().create_recursive_tc (" + "\"\"" + ");");
/* 121 */       paramTCOffsets.bumpCurrentOffset(4);
/*     */     }
/*     */     else
/*     */     {
/* 126 */       paramTCOffsets.set(paramSymtabEntry);
/* 127 */       paramInt = ((JavaGenerator)paramSymtabEntry.type().generator()).type(paramInt + 1, paramString1, paramTCOffsets, paramString2, paramSymtabEntry.type(), paramPrintWriter);
/* 128 */       Expression localExpression = ((SequenceEntry)paramSymtabEntry).maxSize();
/* 129 */       if (localExpression == null)
/* 130 */         paramPrintWriter.println(paramString1 + paramString2 + " = org.omg.CORBA.ORB.init ().create_sequence_tc (0, " + paramString2 + ");");
/*     */       else {
/* 132 */         paramPrintWriter.println(paramString1 + paramString2 + " = org.omg.CORBA.ORB.init ().create_sequence_tc (" + Util.parseExpression(localExpression) + ", " + paramString2 + ");");
/*     */       }
/*     */     }
/* 135 */     return paramInt;
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
/* 148 */     SequenceEntry localSequenceEntry = (SequenceEntry)paramSymtabEntry;
/* 149 */     String str1 = "_len" + paramInt++;
/* 150 */     paramPrintWriter.println(paramString1 + "int " + str1 + " = istream.read_long ();");
/* 151 */     if (localSequenceEntry.maxSize() != null)
/*     */     {
/* 153 */       paramPrintWriter.println(paramString1 + "if (" + str1 + " > (" + Util.parseExpression(localSequenceEntry.maxSize()) + "))");
/* 154 */       paramPrintWriter.println(paramString1 + "  throw new org.omg.CORBA.MARSHAL (0, org.omg.CORBA.CompletionStatus.COMPLETED_MAYBE);");
/*     */     }
/*     */ 
/*     */     try
/*     */     {
/* 159 */       str2 = Util.sansArrayInfo((String)localSequenceEntry.dynamicVariable(Compile.typedefInfo));
/*     */     }
/*     */     catch (NoSuchFieldException localNoSuchFieldException)
/*     */     {
/* 163 */       str2 = localSequenceEntry.name();
/*     */     }
/* 165 */     int i = str2.indexOf('[');
/* 166 */     String str3 = str2.substring(i);
/* 167 */     String str2 = str2.substring(0, i);
/*     */ 
/* 170 */     SymtabEntry localSymtabEntry = (SymtabEntry)Util.symbolTable.get(str2.replace('.', '/'));
/* 171 */     if ((localSymtabEntry != null) && ((localSymtabEntry instanceof InterfaceEntry)) && (((InterfaceEntry)localSymtabEntry).state() != null))
/*     */     {
/* 174 */       str2 = Util.javaName((InterfaceEntry)localSymtabEntry);
/*     */     }
/* 176 */     str3 = str3.substring(2);
/* 177 */     paramPrintWriter.println(paramString1 + paramString2 + " = new " + str2 + '[' + str1 + ']' + str3 + ';');
/*     */     String str4;
/*     */     int j;
/* 178 */     if ((localSequenceEntry.type() instanceof PrimitiveEntry))
/*     */     {
/* 181 */       if ((localSequenceEntry.type().name().equals("any")) || 
/* 182 */         (localSequenceEntry
/* 182 */         .type().name().equals("TypeCode")) || 
/* 183 */         (localSequenceEntry
/* 183 */         .type().name().equals("Principal")))
/*     */       {
/* 185 */         str4 = "_o" + paramInt;
/* 186 */         paramPrintWriter.println(paramString1 + "for (int " + str4 + " = 0;" + str4 + " < " + paramString2 + ".length; ++" + str4 + ')');
/* 187 */         paramPrintWriter.println(paramString1 + "  " + paramString2 + '[' + str4 + "] = istream.read_" + localSequenceEntry.type().name() + " ();");
/*     */       }
/*     */       else
/*     */       {
/* 191 */         str4 = paramString2;
/* 192 */         j = str4.indexOf(' ');
/* 193 */         if (j != -1)
/* 194 */           str4 = str4.substring(j + 1);
/* 195 */         paramPrintWriter.println(paramString1 + "istream.read_" + Util.collapseName(paramSymtabEntry.type().name()) + "_array (" + str4 + ", 0, " + str1 + ");");
/*     */       }
/* 197 */     } else if ((paramSymtabEntry.type() instanceof StringEntry))
/*     */     {
/* 199 */       str4 = "_o" + paramInt;
/* 200 */       paramPrintWriter.println(paramString1 + "for (int " + str4 + " = 0;" + str4 + " < " + paramString2 + ".length; ++" + str4 + ')');
/* 201 */       paramPrintWriter.println(paramString1 + "  " + paramString2 + '[' + str4 + "] = istream.read_" + localSequenceEntry.type().name() + " ();");
/*     */     }
/* 203 */     else if ((paramSymtabEntry.type() instanceof SequenceEntry))
/*     */     {
/* 205 */       str4 = "_o" + paramInt;
/* 206 */       paramPrintWriter.println(paramString1 + "for (int " + str4 + " = 0;" + str4 + " < " + paramString2 + ".length; ++" + str4 + ')');
/* 207 */       paramPrintWriter.println(paramString1 + '{');
/* 208 */       paramInt = ((JavaGenerator)localSequenceEntry.type().generator()).read(paramInt, paramString1 + "  ", paramString2 + '[' + str4 + ']', localSequenceEntry.type(), paramPrintWriter);
/* 209 */       paramPrintWriter.println(paramString1 + '}');
/*     */     }
/*     */     else
/*     */     {
/* 213 */       str4 = paramString2;
/* 214 */       j = str4.indexOf(' ');
/* 215 */       if (j != -1)
/* 216 */         str4 = str4.substring(j + 1);
/* 217 */       String str5 = "_o" + paramInt;
/* 218 */       paramPrintWriter.println(paramString1 + "for (int " + str5 + " = 0;" + str5 + " < " + str4 + ".length; ++" + str5 + ')');
/* 219 */       paramPrintWriter.println(paramString1 + "  " + str4 + '[' + str5 + "] = " + Util.helperName(localSequenceEntry.type(), true) + ".read (istream);");
/*     */     }
/* 221 */     return paramInt;
/*     */   }
/*     */ 
/*     */   public int write(int paramInt, String paramString1, String paramString2, SymtabEntry paramSymtabEntry, PrintWriter paramPrintWriter)
/*     */   {
/* 226 */     SequenceEntry localSequenceEntry = (SequenceEntry)paramSymtabEntry;
/* 227 */     if (localSequenceEntry.maxSize() != null)
/*     */     {
/* 229 */       paramPrintWriter.println(paramString1 + "if (" + paramString2 + ".length > (" + Util.parseExpression(localSequenceEntry.maxSize()) + "))");
/* 230 */       paramPrintWriter.println(paramString1 + "  throw new org.omg.CORBA.MARSHAL (0, org.omg.CORBA.CompletionStatus.COMPLETED_MAYBE);");
/*     */     }
/* 232 */     paramPrintWriter.println(paramString1 + "ostream.write_long (" + paramString2 + ".length);");
/*     */     String str;
/* 233 */     if ((paramSymtabEntry.type() instanceof PrimitiveEntry))
/*     */     {
/* 236 */       if ((paramSymtabEntry.type().name().equals("any")) || 
/* 237 */         (paramSymtabEntry
/* 237 */         .type().name().equals("TypeCode")) || 
/* 238 */         (paramSymtabEntry
/* 238 */         .type().name().equals("Principal")))
/*     */       {
/* 240 */         str = "_i" + paramInt++;
/* 241 */         paramPrintWriter.println(paramString1 + "for (int " + str + " = 0;" + str + " < " + paramString2 + ".length; ++" + str + ')');
/* 242 */         paramPrintWriter.println(paramString1 + "  ostream.write_" + localSequenceEntry.type().name() + " (" + paramString2 + '[' + str + "]);");
/*     */       }
/*     */       else {
/* 245 */         paramPrintWriter.println(paramString1 + "ostream.write_" + Util.collapseName(paramSymtabEntry.type().name()) + "_array (" + paramString2 + ", 0, " + paramString2 + ".length);");
/*     */       } } else if ((paramSymtabEntry.type() instanceof StringEntry))
/*     */     {
/* 248 */       str = "_i" + paramInt++;
/* 249 */       paramPrintWriter.println(paramString1 + "for (int " + str + " = 0;" + str + " < " + paramString2 + ".length; ++" + str + ')');
/* 250 */       paramPrintWriter.println(paramString1 + "  ostream.write_" + localSequenceEntry.type().name() + " (" + paramString2 + '[' + str + "]);");
/*     */     }
/* 252 */     else if ((paramSymtabEntry.type() instanceof SequenceEntry))
/*     */     {
/* 254 */       str = "_i" + paramInt++;
/* 255 */       paramPrintWriter.println(paramString1 + "for (int " + str + " = 0;" + str + " < " + paramString2 + ".length; ++" + str + ')');
/* 256 */       paramPrintWriter.println(paramString1 + '{');
/* 257 */       paramInt = ((JavaGenerator)localSequenceEntry.type().generator()).write(paramInt, paramString1 + "  ", paramString2 + '[' + str + ']', localSequenceEntry.type(), paramPrintWriter);
/* 258 */       paramPrintWriter.println(paramString1 + '}');
/*     */     }
/*     */     else
/*     */     {
/* 262 */       str = "_i" + paramInt++;
/* 263 */       paramPrintWriter.println(paramString1 + "for (int " + str + " = 0;" + str + " < " + paramString2 + ".length; ++" + str + ')');
/* 264 */       paramPrintWriter.println(paramString1 + "  " + Util.helperName(localSequenceEntry.type(), true) + ".write (ostream, " + paramString2 + '[' + str + "]);");
/*     */     }
/* 266 */     return paramInt;
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.corba.se.idl.toJavaPortable.SequenceGen
 * JD-Core Version:    0.6.2
 */