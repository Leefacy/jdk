/*     */ package com.sun.tools.jdi;
/*     */ 
/*     */ import java.io.IOException;
/*     */ 
/*     */ public class Packet
/*     */ {
/*     */   public static final short NoFlags = 0;
/*     */   public static final short Reply = 128;
/*     */   public static final short ReplyNoError = 0;
/*  36 */   static int uID = 1;
/*  37 */   static final byte[] nullData = new byte[0];
/*     */   int id;
/*     */   short flags;
/*     */   short cmdSet;
/*     */   short cmd;
/*     */   short errorCode;
/*     */   byte[] data;
/*  48 */   volatile boolean replied = false;
/*     */ 
/*     */   public byte[] toByteArray()
/*     */   {
/*  54 */     int i = this.data.length + 11;
/*  55 */     byte[] arrayOfByte = new byte[i];
/*  56 */     arrayOfByte[0] = ((byte)(i >>> 24 & 0xFF));
/*  57 */     arrayOfByte[1] = ((byte)(i >>> 16 & 0xFF));
/*  58 */     arrayOfByte[2] = ((byte)(i >>> 8 & 0xFF));
/*  59 */     arrayOfByte[3] = ((byte)(i >>> 0 & 0xFF));
/*  60 */     arrayOfByte[4] = ((byte)(this.id >>> 24 & 0xFF));
/*  61 */     arrayOfByte[5] = ((byte)(this.id >>> 16 & 0xFF));
/*  62 */     arrayOfByte[6] = ((byte)(this.id >>> 8 & 0xFF));
/*  63 */     arrayOfByte[7] = ((byte)(this.id >>> 0 & 0xFF));
/*  64 */     arrayOfByte[8] = ((byte)this.flags);
/*  65 */     if ((this.flags & 0x80) == 0) {
/*  66 */       arrayOfByte[9] = ((byte)this.cmdSet);
/*  67 */       arrayOfByte[10] = ((byte)this.cmd);
/*     */     } else {
/*  69 */       arrayOfByte[9] = ((byte)(this.errorCode >>> 8 & 0xFF));
/*  70 */       arrayOfByte[10] = ((byte)(this.errorCode >>> 0 & 0xFF));
/*     */     }
/*  72 */     if (this.data.length > 0) {
/*  73 */       System.arraycopy(this.data, 0, arrayOfByte, 11, this.data.length);
/*     */     }
/*  75 */     return arrayOfByte;
/*     */   }
/*     */ 
/*     */   public static Packet fromByteArray(byte[] paramArrayOfByte)
/*     */     throws IOException
/*     */   {
/*  82 */     if (paramArrayOfByte.length < 11) {
/*  83 */       throw new IOException("packet is insufficient size");
/*     */     }
/*     */ 
/*  86 */     int i = paramArrayOfByte[0] & 0xFF;
/*  87 */     int j = paramArrayOfByte[1] & 0xFF;
/*  88 */     int k = paramArrayOfByte[2] & 0xFF;
/*  89 */     int m = paramArrayOfByte[3] & 0xFF;
/*  90 */     int n = i << 24 | j << 16 | k << 8 | m << 0;
/*  91 */     if (n != paramArrayOfByte.length) {
/*  92 */       throw new IOException("length size mis-match");
/*     */     }
/*     */ 
/*  95 */     int i1 = paramArrayOfByte[4] & 0xFF;
/*  96 */     int i2 = paramArrayOfByte[5] & 0xFF;
/*  97 */     int i3 = paramArrayOfByte[6] & 0xFF;
/*  98 */     int i4 = paramArrayOfByte[7] & 0xFF;
/*     */ 
/* 100 */     Packet localPacket = new Packet();
/* 101 */     localPacket.id = (i1 << 24 | i2 << 16 | i3 << 8 | i4 << 0);
/*     */ 
/* 103 */     localPacket.flags = ((short)(paramArrayOfByte[8] & 0xFF));
/*     */ 
/* 105 */     if ((localPacket.flags & 0x80) == 0) {
/* 106 */       localPacket.cmdSet = ((short)(paramArrayOfByte[9] & 0xFF));
/* 107 */       localPacket.cmd = ((short)(paramArrayOfByte[10] & 0xFF));
/*     */     } else {
/* 109 */       int i5 = (short)(paramArrayOfByte[9] & 0xFF);
/* 110 */       int i6 = (short)(paramArrayOfByte[10] & 0xFF);
/* 111 */       localPacket.errorCode = ((short)((i5 << 8) + (i6 << 0)));
/*     */     }
/*     */ 
/* 114 */     localPacket.data = new byte[paramArrayOfByte.length - 11];
/* 115 */     System.arraycopy(paramArrayOfByte, 11, localPacket.data, 0, localPacket.data.length);
/* 116 */     return localPacket;
/*     */   }
/*     */ 
/*     */   Packet()
/*     */   {
/* 121 */     this.id = uniqID();
/* 122 */     this.flags = 0;
/* 123 */     this.data = nullData;
/*     */   }
/*     */ 
/*     */   private static synchronized int uniqID()
/*     */   {
/* 133 */     return uID++;
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.jdi.Packet
 * JD-Core Version:    0.6.2
 */