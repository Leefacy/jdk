/*     */ package com.sun.tools.corba.se.idl;
/*     */ 
/*     */ import java.security.MessageDigest;
/*     */ import java.util.Hashtable;
/*     */ 
/*     */ public class ValueRepositoryId
/*     */ {
/*     */   private MessageDigest sha;
/*     */   private int index;
/*     */   private Hashtable types;
/*     */   private String hashcode;
/*     */ 
/*     */   public ValueRepositoryId()
/*     */   {
/*     */     try
/*     */     {
/*  57 */       this.sha = MessageDigest.getInstance("SHA-1");
/*     */     }
/*     */     catch (Exception localException) {
/*     */     }
/*  61 */     this.index = 0;
/*  62 */     this.types = new Hashtable();
/*  63 */     this.hashcode = null;
/*     */   }
/*     */ 
/*     */   public void addValue(int paramInt)
/*     */   {
/*  70 */     this.sha.update((byte)(paramInt >> 24 & 0xF));
/*  71 */     this.sha.update((byte)(paramInt >> 16 & 0xF));
/*  72 */     this.sha.update((byte)(paramInt >> 8 & 0xF));
/*  73 */     this.sha.update((byte)(paramInt & 0xF));
/*  74 */     this.index += 1;
/*     */   }
/*     */ 
/*     */   public void addType(SymtabEntry paramSymtabEntry)
/*     */   {
/*  82 */     this.types.put(paramSymtabEntry, new Integer(this.index));
/*     */   }
/*     */ 
/*     */   public boolean isNewType(SymtabEntry paramSymtabEntry)
/*     */   {
/*  94 */     Object localObject = this.types.get(paramSymtabEntry);
/*  95 */     if (localObject == null)
/*     */     {
/*  97 */       addType(paramSymtabEntry);
/*  98 */       return true;
/*     */     }
/* 100 */     addValue(-1);
/* 101 */     addValue(((Integer)localObject).intValue());
/* 102 */     return false;
/*     */   }
/*     */ 
/*     */   public String getHashcode()
/*     */   {
/* 112 */     if (this.hashcode == null)
/*     */     {
/* 114 */       byte[] arrayOfByte = this.sha.digest();
/* 115 */       this.hashcode = 
/* 118 */         (hexOf(arrayOfByte[0]) + hexOf(arrayOfByte[1]) + 
/* 116 */         hexOf(arrayOfByte[2]) + 
/* 116 */         hexOf(arrayOfByte[3]) + 
/* 117 */         hexOf(arrayOfByte[4]) + 
/* 117 */         hexOf(arrayOfByte[5]) + 
/* 118 */         hexOf(arrayOfByte[6]) + 
/* 118 */         hexOf(arrayOfByte[7]));
/*     */     }
/* 120 */     return this.hashcode;
/*     */   }
/*     */ 
/*     */   private static String hexOf(byte paramByte)
/*     */   {
/* 126 */     int i = paramByte >> 4 & 0xF;
/* 127 */     int j = paramByte & 0xF;
/*     */ 
/* 129 */     return "0123456789ABCDEF".substring(i, i + 1) + "0123456789ABCDEF"
/* 129 */       .substring(j, j + 1);
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.corba.se.idl.ValueRepositoryId
 * JD-Core Version:    0.6.2
 */