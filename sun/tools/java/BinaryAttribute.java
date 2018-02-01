/*     */ package sun.tools.java;
/*     */ 
/*     */ import java.io.DataInputStream;
/*     */ import java.io.DataOutputStream;
/*     */ import java.io.IOException;
/*     */ 
/*     */ public final class BinaryAttribute
/*     */   implements Constants
/*     */ {
/*     */   Identifier name;
/*     */   byte[] data;
/*     */   BinaryAttribute next;
/*     */ 
/*     */   BinaryAttribute(Identifier paramIdentifier, byte[] paramArrayOfByte, BinaryAttribute paramBinaryAttribute)
/*     */   {
/*  50 */     this.name = paramIdentifier;
/*  51 */     this.data = paramArrayOfByte;
/*  52 */     this.next = paramBinaryAttribute;
/*     */   }
/*     */ 
/*     */   public static BinaryAttribute load(DataInputStream paramDataInputStream, BinaryConstantPool paramBinaryConstantPool, int paramInt)
/*     */     throws IOException
/*     */   {
/*  59 */     BinaryAttribute localBinaryAttribute = null;
/*  60 */     int i = paramDataInputStream.readUnsignedShort();
/*     */ 
/*  62 */     for (int j = 0; j < i; j++)
/*     */     {
/*  64 */       Identifier localIdentifier = paramBinaryConstantPool.getIdentifier(paramDataInputStream.readUnsignedShort());
/*     */ 
/*  66 */       int k = paramDataInputStream.readInt();
/*     */ 
/*  68 */       if ((localIdentifier.equals(idCode)) && ((paramInt & 0x2) == 0)) {
/*  69 */         paramDataInputStream.skipBytes(k);
/*     */       } else {
/*  71 */         byte[] arrayOfByte = new byte[k];
/*  72 */         paramDataInputStream.readFully(arrayOfByte);
/*  73 */         localBinaryAttribute = new BinaryAttribute(localIdentifier, arrayOfByte, localBinaryAttribute);
/*     */       }
/*     */     }
/*  76 */     return localBinaryAttribute;
/*     */   }
/*     */ 
/*     */   static void write(BinaryAttribute paramBinaryAttribute, DataOutputStream paramDataOutputStream, BinaryConstantPool paramBinaryConstantPool, Environment paramEnvironment)
/*     */     throws IOException
/*     */   {
/*  84 */     int i = 0;
/*  85 */     for (BinaryAttribute localBinaryAttribute = paramBinaryAttribute; localBinaryAttribute != null; localBinaryAttribute = localBinaryAttribute.next)
/*  86 */       i++;
/*  87 */     paramDataOutputStream.writeShort(i);
/*     */ 
/*  90 */     for (localBinaryAttribute = paramBinaryAttribute; localBinaryAttribute != null; localBinaryAttribute = localBinaryAttribute.next) {
/*  91 */       Identifier localIdentifier = localBinaryAttribute.name;
/*  92 */       byte[] arrayOfByte = localBinaryAttribute.data;
/*     */ 
/*  94 */       paramDataOutputStream.writeShort(paramBinaryConstantPool.indexString(localIdentifier.toString(), paramEnvironment));
/*     */ 
/*  96 */       paramDataOutputStream.writeInt(arrayOfByte.length);
/*     */ 
/*  98 */       paramDataOutputStream.write(arrayOfByte, 0, arrayOfByte.length);
/*     */     }
/*     */   }
/*     */ 
/*     */   public Identifier getName()
/*     */   {
/* 106 */     return this.name;
/*     */   }
/* 108 */   public byte[] getData() { return this.data; } 
/*     */   public BinaryAttribute getNextAttribute() {
/* 110 */     return this.next;
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     sun.tools.java.BinaryAttribute
 * JD-Core Version:    0.6.2
 */