/*     */ package com.sun.tools.corba.se.idl.som.idlemit;
/*     */ 
/*     */ import com.sun.tools.corba.se.idl.ForwardEntry;
/*     */ import com.sun.tools.corba.se.idl.PragmaHandler;
/*     */ import com.sun.tools.corba.se.idl.SymtabEntry;
/*     */ import com.sun.tools.corba.se.idl.som.cff.Messages;
/*     */ import java.util.Vector;
/*     */ 
/*     */ public class MetaPragma extends PragmaHandler
/*     */ {
/*  65 */   public static int metaKey = SymtabEntry.getVariableKey();
/*     */ 
/* 176 */   private static int initialState = 0;
/* 177 */   private static int commentState = 1;
/* 178 */   private static int textState = 2;
/* 179 */   private static int finalState = 3;
/*     */ 
/*     */   public boolean process(String paramString1, String paramString2)
/*     */   {
/*  75 */     if (!paramString1.equals("meta")) {
/*  76 */       return false;
/*     */     }
/*     */ 
/*     */     try
/*     */     {
/*  81 */       SymtabEntry localSymtabEntry = scopedName();
/*  82 */       if (localSymtabEntry == null)
/*     */       {
/*  84 */         parseException(Messages.msg("idlemit.MetaPragma.scopedNameNotFound"));
/*  85 */         skipToEOL();
/*     */       }
/*     */       else {
/*  88 */         String str = currentToken() + getStringToEOL();
/*     */ 
/*  91 */         Vector localVector = (Vector)localSymtabEntry.dynamicVariable(metaKey);
/*  92 */         if (localVector == null) {
/*  93 */           localVector = new Vector();
/*  94 */           localSymtabEntry.dynamicVariable(metaKey, localVector);
/*     */         }
/*  96 */         parseMsg(localVector, str);
/*     */       }
/*     */     }
/*     */     catch (Exception localException) {
/*     */     }
/* 101 */     return true;
/*     */   }
/*     */ 
/*     */   public static void processForward(ForwardEntry paramForwardEntry)
/*     */   {
/*     */     Vector localVector1;
/*     */     try
/*     */     {
/* 114 */       localVector1 = (Vector)paramForwardEntry.dynamicVariable(metaKey);
/*     */     } catch (Exception localException1) {
/* 116 */       localVector1 = null;
/*     */     }
/* 118 */     SymtabEntry localSymtabEntry = paramForwardEntry.type();
/* 119 */     if ((localVector1 != null) && (localSymtabEntry != null)) {
/*     */       Vector localVector2;
/*     */       try {
/* 122 */         localVector2 = (Vector)localSymtabEntry.dynamicVariable(metaKey);
/*     */       } catch (Exception localException2) {
/* 124 */         localVector2 = null;
/*     */       }
/*     */ 
/* 127 */       if (localVector2 == null)
/*     */         try
/*     */         {
/* 130 */           localSymtabEntry.dynamicVariable(metaKey, localVector1);
/*     */         } catch (Exception localException3) {
/*     */         }
/* 133 */       else if (localVector2 != localVector1)
/*     */       {
/* 138 */         for (int i = 0; i < localVector1.size(); i++)
/*     */           try {
/* 140 */             Object localObject = localVector1.elementAt(i);
/* 141 */             localVector2.addElement(localObject);
/*     */           }
/*     */           catch (Exception localException4)
/*     */           {
/*     */           }
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   private void parseMsg(Vector paramVector, String paramString)
/*     */   {
/* 182 */     int i = initialState;
/* 183 */     String str = "";
/* 184 */     int j = 0;
/* 185 */     while (i != finalState) {
/* 186 */       int k = j >= paramString.length() ? 1 : 0;
/* 187 */       char c = ' ';
/* 188 */       int m = 0;
/* 189 */       int n = 0;
/* 190 */       int i1 = 0;
/* 191 */       int i2 = 0;
/* 192 */       int i3 = 0;
/* 193 */       if (k == 0) {
/* 194 */         c = paramString.charAt(j);
/* 195 */         if ((c == '/') && (j + 1 < paramString.length())) {
/* 196 */           if (paramString.charAt(j + 1) == '/') {
/* 197 */             n = 1;
/* 198 */             j++;
/*     */           }
/* 200 */           else if (paramString.charAt(j + 1) == '*') {
/* 201 */             m = 1;
/* 202 */             j++; } else {
/* 203 */             i3 = 1;
/*     */           }
/* 205 */         } else if ((c == '*') && (j + 1 < paramString.length())) {
/* 206 */           if (paramString.charAt(j + 1) == '/') {
/* 207 */             i2 = 1;
/* 208 */             j++; } else {
/* 209 */             i3 = 1;
/*     */           }
/* 211 */         } else if ((Character.isSpace(c)) || (c == ',') || (c == ';'))
/*     */         {
/* 213 */           i1 = 1;
/*     */         } else i3 = 1;
/*     */       }
/*     */ 
/* 217 */       if (i == initialState) {
/* 218 */         if (m != 0) {
/* 219 */           i = commentState;
/*     */         }
/* 221 */         else if ((n != 0) || (k != 0)) {
/* 222 */           i = finalState;
/*     */         }
/* 224 */         else if (i3 != 0) {
/* 225 */           i = textState;
/* 226 */           str = str + c;
/*     */         }
/*     */       }
/* 229 */       else if (i == commentState) {
/* 230 */         if (k != 0) {
/* 231 */           i = finalState;
/*     */         }
/* 233 */         else if (i2 != 0) {
/* 234 */           i = initialState;
/*     */         }
/*     */       }
/* 237 */       else if (i == textState) {
/* 238 */         if ((k != 0) || (i2 != 0) || (n != 0) || (m != 0) || (i1 != 0))
/*     */         {
/* 240 */           if (!str.equals("")) {
/* 241 */             paramVector.addElement(str);
/*     */ 
/* 243 */             str = "";
/*     */           }
/* 245 */           if (k != 0)
/* 246 */             i = finalState;
/* 247 */           else if (m != 0)
/* 248 */             i = commentState;
/* 249 */           else i = initialState;
/*     */         }
/* 251 */         else if (i3 != 0) {
/* 252 */           str = str + c;
/*     */         }
/*     */       }
/* 255 */       j++;
/*     */     }
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.corba.se.idl.som.idlemit.MetaPragma
 * JD-Core Version:    0.6.2
 */