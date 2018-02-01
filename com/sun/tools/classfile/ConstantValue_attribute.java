/*    */ package com.sun.tools.classfile;
/*    */ 
/*    */ import java.io.IOException;
/*    */ 
/*    */ public class ConstantValue_attribute extends Attribute
/*    */ {
/*    */   public final int constantvalue_index;
/*    */ 
/*    */   ConstantValue_attribute(ClassReader paramClassReader, int paramInt1, int paramInt2)
/*    */     throws IOException
/*    */   {
/* 40 */     super(paramInt1, paramInt2);
/* 41 */     this.constantvalue_index = paramClassReader.readUnsignedShort();
/*    */   }
/*    */ 
/*    */   public ConstantValue_attribute(ConstantPool paramConstantPool, int paramInt) throws ConstantPoolException
/*    */   {
/* 46 */     this(paramConstantPool.getUTF8Index("ConstantValue"), paramInt);
/*    */   }
/*    */ 
/*    */   public ConstantValue_attribute(int paramInt1, int paramInt2) {
/* 50 */     super(paramInt1, 2);
/* 51 */     this.constantvalue_index = paramInt2;
/*    */   }
/*    */ 
/*    */   public <R, D> R accept(Attribute.Visitor<R, D> paramVisitor, D paramD) {
/* 55 */     return paramVisitor.visitConstantValue(this, paramD);
/*    */   }
/*    */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.classfile.ConstantValue_attribute
 * JD-Core Version:    0.6.2
 */