/*    */ package com.sun.tools.classfile;
/*    */ 
/*    */ public class DefaultAttribute extends Attribute
/*    */ {
/*    */   public final byte[] info;
/*    */   public final String reason;
/*    */ 
/*    */   DefaultAttribute(ClassReader paramClassReader, int paramInt, byte[] paramArrayOfByte)
/*    */   {
/* 36 */     this(paramClassReader, paramInt, paramArrayOfByte, null);
/*    */   }
/*    */ 
/*    */   DefaultAttribute(ClassReader paramClassReader, int paramInt, byte[] paramArrayOfByte, String paramString) {
/* 40 */     super(paramInt, paramArrayOfByte.length);
/* 41 */     this.info = paramArrayOfByte;
/* 42 */     this.reason = paramString;
/*    */   }
/*    */ 
/*    */   public DefaultAttribute(ConstantPool paramConstantPool, int paramInt, byte[] paramArrayOfByte) {
/* 46 */     this(paramConstantPool, paramInt, paramArrayOfByte, null);
/*    */   }
/*    */ 
/*    */   public DefaultAttribute(ConstantPool paramConstantPool, int paramInt, byte[] paramArrayOfByte, String paramString)
/*    */   {
/* 51 */     super(paramInt, paramArrayOfByte.length);
/* 52 */     this.info = paramArrayOfByte;
/* 53 */     this.reason = paramString;
/*    */   }
/*    */ 
/*    */   public <R, P> R accept(Attribute.Visitor<R, P> paramVisitor, P paramP) {
/* 57 */     return paramVisitor.visitDefault(this, paramP);
/*    */   }
/*    */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.classfile.DefaultAttribute
 * JD-Core Version:    0.6.2
 */