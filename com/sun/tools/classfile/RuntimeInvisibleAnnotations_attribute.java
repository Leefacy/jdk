/*    */ package com.sun.tools.classfile;
/*    */ 
/*    */ import java.io.IOException;
/*    */ 
/*    */ public class RuntimeInvisibleAnnotations_attribute extends RuntimeAnnotations_attribute
/*    */ {
/*    */   RuntimeInvisibleAnnotations_attribute(ClassReader paramClassReader, int paramInt1, int paramInt2)
/*    */     throws IOException, AttributeException
/*    */   {
/* 41 */     super(paramClassReader, paramInt1, paramInt2);
/*    */   }
/*    */ 
/*    */   public RuntimeInvisibleAnnotations_attribute(ConstantPool paramConstantPool, Annotation[] paramArrayOfAnnotation) throws ConstantPoolException
/*    */   {
/* 46 */     this(paramConstantPool.getUTF8Index("RuntimeInvisibleAnnotations"), paramArrayOfAnnotation);
/*    */   }
/*    */ 
/*    */   public RuntimeInvisibleAnnotations_attribute(int paramInt, Annotation[] paramArrayOfAnnotation) {
/* 50 */     super(paramInt, paramArrayOfAnnotation);
/*    */   }
/*    */ 
/*    */   public <R, P> R accept(Attribute.Visitor<R, P> paramVisitor, P paramP) {
/* 54 */     return paramVisitor.visitRuntimeInvisibleAnnotations(this, paramP);
/*    */   }
/*    */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.classfile.RuntimeInvisibleAnnotations_attribute
 * JD-Core Version:    0.6.2
 */