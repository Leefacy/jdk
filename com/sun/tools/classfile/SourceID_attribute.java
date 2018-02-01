/*    */ package com.sun.tools.classfile;
/*    */ 
/*    */ import java.io.IOException;
/*    */ 
/*    */ public class SourceID_attribute extends Attribute
/*    */ {
/*    */   public final int sourceID_index;
/*    */ 
/*    */   SourceID_attribute(ClassReader paramClassReader, int paramInt1, int paramInt2)
/*    */     throws IOException
/*    */   {
/* 38 */     super(paramInt1, paramInt2);
/* 39 */     this.sourceID_index = paramClassReader.readUnsignedShort();
/*    */   }
/*    */ 
/*    */   public SourceID_attribute(ConstantPool paramConstantPool, int paramInt) throws ConstantPoolException
/*    */   {
/* 44 */     this(paramConstantPool.getUTF8Index("SourceID"), paramInt);
/*    */   }
/*    */ 
/*    */   public SourceID_attribute(int paramInt1, int paramInt2) {
/* 48 */     super(paramInt1, 2);
/* 49 */     this.sourceID_index = paramInt2;
/*    */   }
/*    */ 
/*    */   String getSourceID(ConstantPool paramConstantPool) throws ConstantPoolException {
/* 53 */     return paramConstantPool.getUTF8Value(this.sourceID_index);
/*    */   }
/*    */ 
/*    */   public <R, D> R accept(Attribute.Visitor<R, D> paramVisitor, D paramD) {
/* 57 */     return paramVisitor.visitSourceID(this, paramD);
/*    */   }
/*    */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.classfile.SourceID_attribute
 * JD-Core Version:    0.6.2
 */