/*     */ package sun.tools.java;
/*     */ 
/*     */ import java.io.ByteArrayInputStream;
/*     */ import java.io.DataInputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.PrintStream;
/*     */ 
/*     */ public class BinaryCode
/*     */   implements Constants
/*     */ {
/*     */   int maxStack;
/*     */   int maxLocals;
/*     */   BinaryExceptionHandler[] exceptionHandlers;
/*     */   BinaryAttribute atts;
/*     */   BinaryConstantPool cpool;
/*     */   byte[] code;
/*     */ 
/*     */   public BinaryCode(byte[] paramArrayOfByte, BinaryConstantPool paramBinaryConstantPool, Environment paramEnvironment)
/*     */   {
/*  50 */     DataInputStream localDataInputStream = new DataInputStream(new ByteArrayInputStream(paramArrayOfByte));
/*     */     try {
/*  52 */       this.cpool = paramBinaryConstantPool;
/*     */ 
/*  54 */       this.maxStack = localDataInputStream.readUnsignedShort();
/*     */ 
/*  56 */       this.maxLocals = localDataInputStream.readUnsignedShort();
/*     */ 
/*  58 */       int i = localDataInputStream.readInt();
/*  59 */       this.code = new byte[i];
/*     */ 
/*  61 */       localDataInputStream.read(this.code);
/*     */ 
/*  63 */       int j = localDataInputStream.readUnsignedShort();
/*  64 */       this.exceptionHandlers = new BinaryExceptionHandler[j];
/*  65 */       for (int k = 0; k < j; k++)
/*     */       {
/*  67 */         int m = localDataInputStream.readUnsignedShort();
/*     */ 
/*  69 */         int n = localDataInputStream.readUnsignedShort();
/*     */ 
/*  71 */         int i1 = localDataInputStream.readUnsignedShort();
/*     */ 
/*  73 */         ClassDeclaration localClassDeclaration = paramBinaryConstantPool.getDeclaration(paramEnvironment, localDataInputStream.readUnsignedShort());
/*  74 */         this.exceptionHandlers[k] = new BinaryExceptionHandler(m, n, i1, localClassDeclaration);
/*     */       }
/*     */ 
/*  77 */       this.atts = BinaryAttribute.load(localDataInputStream, paramBinaryConstantPool, -1);
/*  78 */       if (localDataInputStream.available() != 0)
/*  79 */         System.err.println("Should have exhausted input stream!");
/*     */     }
/*     */     catch (IOException localIOException) {
/*  82 */       throw new CompilerError(localIOException);
/*     */     }
/*     */   }
/*     */ 
/*     */   public BinaryExceptionHandler[] getExceptionHandlers()
/*     */   {
/*  92 */     return this.exceptionHandlers;
/*     */   }
/*     */   public byte[] getCode() {
/*  95 */     return this.code;
/*     */   }
/*  97 */   public int getMaxStack() { return this.maxStack; } 
/*     */   public int getMaxLocals() {
/*  99 */     return this.maxLocals;
/*     */   }
/* 101 */   public BinaryAttribute getAttributes() { return this.atts; }
/*     */ 
/*     */ 
/*     */   public static BinaryCode load(BinaryMember paramBinaryMember, BinaryConstantPool paramBinaryConstantPool, Environment paramEnvironment)
/*     */   {
/* 108 */     byte[] arrayOfByte = paramBinaryMember.getAttribute(idCode);
/* 109 */     return arrayOfByte != null ? new BinaryCode(arrayOfByte, paramBinaryConstantPool, paramEnvironment) : null;
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     sun.tools.java.BinaryCode
 * JD-Core Version:    0.6.2
 */