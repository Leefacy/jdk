/*    */ package com.sun.tools.classfile;
/*    */ 
/*    */ import java.io.IOException;
/*    */ 
/*    */ public class Deprecated_attribute extends Attribute
/*    */ {
/*    */   Deprecated_attribute(ClassReader paramClassReader, int paramInt1, int paramInt2)
/*    */     throws IOException
/*    */   {
/* 40 */     super(paramInt1, paramInt2);
/*    */   }
/*    */ 
/*    */   public Deprecated_attribute(ConstantPool paramConstantPool) throws ConstantPoolException
/*    */   {
/* 45 */     this(paramConstantPool.getUTF8Index("Deprecated"));
/*    */   }
/*    */ 
/*    */   public Deprecated_attribute(int paramInt) {
/* 49 */     super(paramInt, 0);
/*    */   }
/*    */ 
/*    */   public <R, D> R accept(Attribute.Visitor<R, D> paramVisitor, D paramD) {
/* 53 */     return paramVisitor.visitDeprecated(this, paramD);
/*    */   }
/*    */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.classfile.Deprecated_attribute
 * JD-Core Version:    0.6.2
 */