/*     */ package com.sun.tools.corba.se.idl.toJavaPortable;
/*     */ 
/*     */ import com.sun.tools.corba.se.idl.EnumEntry;
/*     */ import com.sun.tools.corba.se.idl.InterfaceEntry;
/*     */ import com.sun.tools.corba.se.idl.RepositoryID;
/*     */ import com.sun.tools.corba.se.idl.StringEntry;
/*     */ import com.sun.tools.corba.se.idl.StructEntry;
/*     */ import com.sun.tools.corba.se.idl.SymtabEntry;
/*     */ import com.sun.tools.corba.se.idl.TypedefEntry;
/*     */ import com.sun.tools.corba.se.idl.UnionEntry;
/*     */ import java.util.Enumeration;
/*     */ import java.util.Hashtable;
/*     */ import java.util.Vector;
/*     */ 
/*     */ public class TCOffsets
/*     */ {
/* 143 */   private Hashtable tcs = new Hashtable();
/* 144 */   private int offset = 0;
/*     */ 
/*     */   public int offset(String paramString)
/*     */   {
/*  63 */     Integer localInteger = (Integer)this.tcs.get(paramString);
/*  64 */     return localInteger == null ? -1 : localInteger.intValue();
/*     */   }
/*     */ 
/*     */   public void set(SymtabEntry paramSymtabEntry)
/*     */   {
/*  72 */     if (paramSymtabEntry == null) {
/*  73 */       this.offset += 8;
/*     */     }
/*     */     else {
/*  76 */       this.tcs.put(paramSymtabEntry.fullName(), new Integer(this.offset));
/*  77 */       this.offset += 4;
/*  78 */       String str = Util.stripLeadingUnderscoresFromID(paramSymtabEntry.repositoryID().ID());
/*  79 */       if ((paramSymtabEntry instanceof InterfaceEntry)) {
/*  80 */         this.offset += alignStrLen(str) + alignStrLen(paramSymtabEntry.name());
/*  81 */       } else if ((paramSymtabEntry instanceof StructEntry)) {
/*  82 */         this.offset += alignStrLen(str) + alignStrLen(paramSymtabEntry.name()) + 4;
/*  83 */       } else if ((paramSymtabEntry instanceof UnionEntry)) {
/*  84 */         this.offset += alignStrLen(str) + alignStrLen(paramSymtabEntry.name()) + 12;
/*  85 */       } else if ((paramSymtabEntry instanceof EnumEntry))
/*     */       {
/*  87 */         this.offset += alignStrLen(str) + alignStrLen(paramSymtabEntry.name()) + 4;
/*  88 */         Enumeration localEnumeration = ((EnumEntry)paramSymtabEntry).elements().elements();
/*  89 */         while (localEnumeration.hasMoreElements())
/*  90 */           this.offset += alignStrLen((String)localEnumeration.nextElement());
/*     */       }
/*  92 */       else if ((paramSymtabEntry instanceof StringEntry)) {
/*  93 */         this.offset += 4;
/*  94 */       } else if ((paramSymtabEntry instanceof TypedefEntry))
/*     */       {
/*  96 */         this.offset += alignStrLen(str) + alignStrLen(paramSymtabEntry.name());
/*  97 */         if (((TypedefEntry)paramSymtabEntry).arrayInfo().size() != 0)
/*  98 */           this.offset += 8;
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   public int alignStrLen(String paramString)
/*     */   {
/* 111 */     int i = paramString.length() + 1;
/* 112 */     int j = 4 - i % 4;
/* 113 */     if (j == 4) j = 0;
/* 114 */     return i + j + 4;
/*     */   }
/*     */ 
/*     */   public void setMember(SymtabEntry paramSymtabEntry)
/*     */   {
/* 122 */     this.offset += alignStrLen(paramSymtabEntry.name());
/* 123 */     if (((TypedefEntry)paramSymtabEntry).arrayInfo().size() != 0)
/* 124 */       this.offset += 4;
/*     */   }
/*     */ 
/*     */   public int currentOffset()
/*     */   {
/* 132 */     return this.offset;
/*     */   }
/*     */ 
/*     */   public void bumpCurrentOffset(int paramInt)
/*     */   {
/* 140 */     this.offset += paramInt;
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.corba.se.idl.toJavaPortable.TCOffsets
 * JD-Core Version:    0.6.2
 */