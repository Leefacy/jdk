/*    */ package com.sun.tools.classfile;
/*    */ 
/*    */ import java.io.IOException;
/*    */ 
/*    */ public class Signature_attribute extends Attribute
/*    */ {
/*    */   public final int signature_index;
/*    */ 
/*    */   Signature_attribute(ClassReader paramClassReader, int paramInt1, int paramInt2)
/*    */     throws IOException
/*    */   {
/* 40 */     super(paramInt1, paramInt2);
/* 41 */     this.signature_index = paramClassReader.readUnsignedShort();
/*    */   }
/*    */ 
/*    */   public Signature_attribute(ConstantPool paramConstantPool, int paramInt) throws ConstantPoolException
/*    */   {
/* 46 */     this(paramConstantPool.getUTF8Index("Signature"), paramInt);
/*    */   }
/*    */ 
/*    */   public Signature_attribute(int paramInt1, int paramInt2) {
/* 50 */     super(paramInt1, 2);
/* 51 */     this.signature_index = paramInt2;
/*    */   }
/*    */ 
/*    */   public String getSignature(ConstantPool paramConstantPool) throws ConstantPoolException {
/* 55 */     return paramConstantPool.getUTF8Value(this.signature_index);
/*    */   }
/*    */ 
/*    */   public Signature getParsedSignature() {
/* 59 */     return new Signature(this.signature_index);
/*    */   }
/*    */ 
/*    */   public <R, D> R accept(Attribute.Visitor<R, D> paramVisitor, D paramD) {
/* 63 */     return paramVisitor.visitSignature(this, paramD);
/*    */   }
/*    */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.classfile.Signature_attribute
 * JD-Core Version:    0.6.2
 */