/*     */ package com.sun.tools.corba.se.idl.toJavaPortable;
/*     */ 
/*     */ import com.sun.tools.corba.se.idl.InterfaceEntry;
/*     */ import com.sun.tools.corba.se.idl.InterfaceState;
/*     */ import com.sun.tools.corba.se.idl.PrimitiveEntry;
/*     */ import com.sun.tools.corba.se.idl.SequenceEntry;
/*     */ import com.sun.tools.corba.se.idl.StringEntry;
/*     */ import com.sun.tools.corba.se.idl.StructEntry;
/*     */ import com.sun.tools.corba.se.idl.SymtabEntry;
/*     */ import com.sun.tools.corba.se.idl.TypedefEntry;
/*     */ import com.sun.tools.corba.se.idl.UnionEntry;
/*     */ import com.sun.tools.corba.se.idl.constExpr.Expression;
/*     */ import java.io.PrintWriter;
/*     */ import java.util.Enumeration;
/*     */ import java.util.Hashtable;
/*     */ import java.util.Vector;
/*     */ 
/*     */ public class TypedefGen
/*     */   implements com.sun.tools.corba.se.idl.TypedefGen, JavaGenerator
/*     */ {
/* 324 */   protected Hashtable symbolTable = null;
/* 325 */   protected TypedefEntry t = null;
/*     */ 
/*     */   public void generate(Hashtable paramHashtable, TypedefEntry paramTypedefEntry, PrintWriter paramPrintWriter)
/*     */   {
/*  81 */     this.symbolTable = paramHashtable;
/*  82 */     this.t = paramTypedefEntry;
/*     */ 
/*  84 */     if ((paramTypedefEntry.arrayInfo().size() > 0) || ((paramTypedefEntry.type() instanceof SequenceEntry)))
/*  85 */       generateHolder();
/*  86 */     generateHelper();
/*     */   }
/*     */ 
/*     */   protected void generateHolder()
/*     */   {
/*  94 */     ((Factories)Compile.compiler.factories()).holder().generate(this.symbolTable, this.t);
/*     */   }
/*     */ 
/*     */   protected void generateHelper()
/*     */   {
/* 102 */     ((Factories)Compile.compiler.factories()).helper().generate(this.symbolTable, this.t);
/*     */   }
/*     */ 
/*     */   private boolean inStruct(TypedefEntry paramTypedefEntry)
/*     */   {
/* 110 */     boolean bool = false;
/* 111 */     if (((paramTypedefEntry.container() instanceof StructEntry)) || ((paramTypedefEntry.container() instanceof UnionEntry))) {
/* 112 */       bool = true;
/* 113 */     } else if ((paramTypedefEntry.container() instanceof InterfaceEntry))
/*     */     {
/* 115 */       InterfaceEntry localInterfaceEntry = (InterfaceEntry)paramTypedefEntry.container();
/* 116 */       if (localInterfaceEntry.state() != null)
/*     */       {
/* 118 */         Enumeration localEnumeration = localInterfaceEntry.state().elements();
/* 119 */         while (localEnumeration.hasMoreElements()) {
/* 120 */           if (((InterfaceState)localEnumeration.nextElement()).entry == paramTypedefEntry)
/*     */           {
/* 122 */             bool = true;
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/* 127 */     return bool;
/*     */   }
/*     */ 
/*     */   public int helperType(int paramInt, String paramString1, TCOffsets paramTCOffsets, String paramString2, SymtabEntry paramSymtabEntry, PrintWriter paramPrintWriter)
/*     */   {
/* 132 */     TypedefEntry localTypedefEntry = (TypedefEntry)paramSymtabEntry;
/* 133 */     boolean bool = inStruct(localTypedefEntry);
/* 134 */     if (bool)
/* 135 */       paramTCOffsets.setMember(paramSymtabEntry);
/*     */     else {
/* 137 */       paramTCOffsets.set(paramSymtabEntry);
/*     */     }
/*     */ 
/* 140 */     paramInt = ((JavaGenerator)localTypedefEntry.type().generator()).type(paramInt, paramString1, paramTCOffsets, paramString2, localTypedefEntry.type(), paramPrintWriter);
/*     */ 
/* 142 */     if ((bool) && (localTypedefEntry.arrayInfo().size() != 0)) {
/* 143 */       paramTCOffsets.bumpCurrentOffset(4);
/*     */     }
/*     */ 
/* 146 */     int i = localTypedefEntry.arrayInfo().size();
/* 147 */     for (int j = 0; j < i; j++)
/*     */     {
/* 149 */       String str = Util.parseExpression((Expression)localTypedefEntry.arrayInfo().elementAt(j));
/* 150 */       paramPrintWriter.println(paramString1 + paramString2 + " = org.omg.CORBA.ORB.init ().create_array_tc (" + str + ", " + paramString2 + " );");
/*     */     }
/*     */ 
/* 155 */     if (!bool)
/*     */     {
/* 158 */       paramPrintWriter.println(paramString1 + paramString2 + " = org.omg.CORBA.ORB.init ().create_alias_tc (" + Util.helperName(localTypedefEntry, true) + ".id (), \"" + Util.stripLeadingUnderscores(localTypedefEntry.name()) + "\", " + paramString2 + ");");
/*     */     }
/* 160 */     return paramInt;
/*     */   }
/*     */ 
/*     */   public int type(int paramInt, String paramString1, TCOffsets paramTCOffsets, String paramString2, SymtabEntry paramSymtabEntry, PrintWriter paramPrintWriter)
/*     */   {
/* 170 */     return helperType(paramInt, paramString1, paramTCOffsets, paramString2, paramSymtabEntry, paramPrintWriter);
/*     */   }
/*     */ 
/*     */   public void helperRead(String paramString, SymtabEntry paramSymtabEntry, PrintWriter paramPrintWriter)
/*     */   {
/* 175 */     Util.writeInitializer("    ", "value", "", paramSymtabEntry, paramPrintWriter);
/* 176 */     read(0, "    ", "value", paramSymtabEntry, paramPrintWriter);
/* 177 */     paramPrintWriter.println("    return value;");
/*     */   }
/*     */ 
/*     */   public void helperWrite(SymtabEntry paramSymtabEntry, PrintWriter paramPrintWriter)
/*     */   {
/* 182 */     write(0, "    ", "value", paramSymtabEntry, paramPrintWriter);
/*     */   }
/*     */ 
/*     */   public int read(int paramInt, String paramString1, String paramString2, SymtabEntry paramSymtabEntry, PrintWriter paramPrintWriter)
/*     */   {
/* 187 */     TypedefEntry localTypedefEntry = (TypedefEntry)paramSymtabEntry;
/* 188 */     String str1 = Util.arrayInfo(localTypedefEntry.arrayInfo());
/* 189 */     if (!str1.equals(""))
/*     */     {
/* 210 */       int i = 0;
/* 211 */       String str2 = "";
/*     */       try
/*     */       {
/* 215 */         str3 = (String)localTypedefEntry.dynamicVariable(Compile.typedefInfo);
/*     */       }
/*     */       catch (NoSuchFieldException localNoSuchFieldException)
/*     */       {
/* 219 */         str3 = localTypedefEntry.name();
/*     */       }
/* 221 */       int j = str3.indexOf('[');
/* 222 */       String str4 = Util.sansArrayInfo(str3.substring(j)) + "[]";
/* 223 */       String str3 = str3.substring(0, j);
/*     */ 
/* 226 */       SymtabEntry localSymtabEntry2 = (SymtabEntry)Util.symbolTable.get(str3.replace('.', '/'));
/* 227 */       if (((localSymtabEntry2 instanceof InterfaceEntry)) && (((InterfaceEntry)localSymtabEntry2).state() != null))
/*     */       {
/* 230 */         str3 = Util.javaName((InterfaceEntry)localSymtabEntry2);
/*     */       }
/*     */ 
/* 233 */       while (!str1.equals(""))
/*     */       {
/* 235 */         m = str1.indexOf(']');
/* 236 */         String str5 = str1.substring(1, m);
/* 237 */         k = str4.indexOf(']');
/* 238 */         str4 = '[' + str5 + str4.substring(k + 2);
/* 239 */         paramPrintWriter.println(paramString1 + paramString2 + " = new " + str3 + str4 + ';');
/* 240 */         str2 = "_o" + paramInt++;
/* 241 */         paramPrintWriter.println(paramString1 + "for (int " + str2 + " = 0;" + str2 + " < (" + str5 + "); ++" + str2 + ')');
/* 242 */         paramPrintWriter.println(paramString1 + '{');
/* 243 */         i++;
/* 244 */         str1 = str1.substring(m + 1);
/* 245 */         paramString1 = paramString1 + "  ";
/* 246 */         paramString2 = paramString2 + '[' + str2 + ']';
/*     */       }
/* 248 */       int k = str4.indexOf(']');
/* 249 */       if (((localTypedefEntry.type() instanceof SequenceEntry)) || ((localTypedefEntry.type() instanceof PrimitiveEntry)) || ((localTypedefEntry.type() instanceof StringEntry)))
/* 250 */         paramInt = ((JavaGenerator)localTypedefEntry.type().generator()).read(paramInt, paramString1, paramString2, localTypedefEntry.type(), paramPrintWriter);
/* 251 */       else if (((localTypedefEntry.type() instanceof InterfaceEntry)) && (localTypedefEntry.type().fullName().equals("org/omg/CORBA/Object")))
/* 252 */         paramPrintWriter.println(paramString1 + paramString2 + " = istream.read_Object ();");
/*     */       else
/* 254 */         paramPrintWriter.println(paramString1 + paramString2 + " = " + Util.helperName(localTypedefEntry.type(), true) + ".read (istream);");
/* 255 */       for (int m = 0; m < i; m++)
/*     */       {
/* 257 */         paramString1 = paramString1.substring(2);
/* 258 */         paramPrintWriter.println(paramString1 + '}');
/*     */       }
/*     */     }
/*     */     else
/*     */     {
/* 263 */       SymtabEntry localSymtabEntry1 = Util.typeOf(localTypedefEntry.type());
/* 264 */       if (((localSymtabEntry1 instanceof SequenceEntry)) || ((localSymtabEntry1 instanceof PrimitiveEntry)) || ((localSymtabEntry1 instanceof StringEntry)))
/* 265 */         paramInt = ((JavaGenerator)localSymtabEntry1.generator()).read(paramInt, paramString1, paramString2, localSymtabEntry1, paramPrintWriter);
/* 266 */       else if (((localSymtabEntry1 instanceof InterfaceEntry)) && (localSymtabEntry1.fullName().equals("org/omg/CORBA/Object")))
/* 267 */         paramPrintWriter.println(paramString1 + paramString2 + " = istream.read_Object ();");
/*     */       else
/* 269 */         paramPrintWriter.println(paramString1 + paramString2 + " = " + Util.helperName(localSymtabEntry1, true) + ".read (istream);");
/*     */     }
/* 271 */     return paramInt;
/*     */   }
/*     */ 
/*     */   public int write(int paramInt, String paramString1, String paramString2, SymtabEntry paramSymtabEntry, PrintWriter paramPrintWriter)
/*     */   {
/* 276 */     TypedefEntry localTypedefEntry = (TypedefEntry)paramSymtabEntry;
/* 277 */     String str1 = Util.arrayInfo(localTypedefEntry.arrayInfo());
/* 278 */     if (!str1.equals(""))
/*     */     {
/* 280 */       int i = 0;
/* 281 */       String str2 = "";
/* 282 */       while (!str1.equals(""))
/*     */       {
/* 284 */         j = str1.indexOf(']');
/* 285 */         String str3 = str1.substring(1, j);
/* 286 */         paramPrintWriter.println(paramString1 + "if (" + paramString2 + ".length != (" + str3 + "))");
/* 287 */         paramPrintWriter.println(paramString1 + "  throw new org.omg.CORBA.MARSHAL (0, org.omg.CORBA.CompletionStatus.COMPLETED_MAYBE);");
/* 288 */         str2 = "_i" + paramInt++;
/* 289 */         paramPrintWriter.println(paramString1 + "for (int " + str2 + " = 0;" + str2 + " < (" + str3 + "); ++" + str2 + ')');
/* 290 */         paramPrintWriter.println(paramString1 + '{');
/* 291 */         i++;
/* 292 */         str1 = str1.substring(j + 1);
/* 293 */         paramString1 = paramString1 + "  ";
/* 294 */         paramString2 = paramString2 + '[' + str2 + ']';
/*     */       }
/* 296 */       if (((localTypedefEntry.type() instanceof SequenceEntry)) || ((localTypedefEntry.type() instanceof PrimitiveEntry)) || ((localTypedefEntry.type() instanceof StringEntry)))
/* 297 */         paramInt = ((JavaGenerator)localTypedefEntry.type().generator()).write(paramInt, paramString1, paramString2, localTypedefEntry.type(), paramPrintWriter);
/* 298 */       else if (((localTypedefEntry.type() instanceof InterfaceEntry)) && (localTypedefEntry.type().fullName().equals("org/omg/CORBA/Object")))
/* 299 */         paramPrintWriter.println(paramString1 + "ostream.write_Object (" + paramString2 + ");");
/*     */       else
/* 301 */         paramPrintWriter.println(paramString1 + Util.helperName(localTypedefEntry.type(), true) + ".write (ostream, " + paramString2 + ");");
/* 302 */       for (int j = 0; j < i; j++)
/*     */       {
/* 304 */         paramString1 = paramString1.substring(2);
/* 305 */         paramPrintWriter.println(paramString1 + '}');
/*     */       }
/*     */     }
/*     */     else
/*     */     {
/* 310 */       SymtabEntry localSymtabEntry = Util.typeOf(localTypedefEntry.type());
/* 311 */       if (((localSymtabEntry instanceof SequenceEntry)) || ((localSymtabEntry instanceof PrimitiveEntry)) || ((localSymtabEntry instanceof StringEntry)))
/* 312 */         paramInt = ((JavaGenerator)localSymtabEntry.generator()).write(paramInt, paramString1, paramString2, localSymtabEntry, paramPrintWriter);
/* 313 */       else if (((localSymtabEntry instanceof InterfaceEntry)) && (localSymtabEntry.fullName().equals("org/omg/CORBA/Object")))
/* 314 */         paramPrintWriter.println(paramString1 + "ostream.write_Object (" + paramString2 + ");");
/*     */       else
/* 316 */         paramPrintWriter.println(paramString1 + Util.helperName(localSymtabEntry, true) + ".write (ostream, " + paramString2 + ");");
/*     */     }
/* 318 */     return paramInt;
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.corba.se.idl.toJavaPortable.TypedefGen
 * JD-Core Version:    0.6.2
 */