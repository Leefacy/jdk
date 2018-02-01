/*    */ package com.sun.tools.classfile;
/*    */ 
/*    */ import java.io.IOException;
/*    */ 
/*    */ public class RuntimeInvisibleParameterAnnotations_attribute extends RuntimeParameterAnnotations_attribute
/*    */ {
/*    */   RuntimeInvisibleParameterAnnotations_attribute(ClassReader paramClassReader, int paramInt1, int paramInt2)
/*    */     throws IOException, Annotation.InvalidAnnotation
/*    */   {
/* 41 */     super(paramClassReader, paramInt1, paramInt2);
/*    */   }
/*    */ 
/*    */   public RuntimeInvisibleParameterAnnotations_attribute(ConstantPool paramConstantPool, Annotation[][] paramArrayOfAnnotation) throws ConstantPoolException
/*    */   {
/* 46 */     this(paramConstantPool.getUTF8Index("RuntimeInvisibleParameterAnnotations"), paramArrayOfAnnotation);
/*    */   }
/*    */ 
/*    */   public RuntimeInvisibleParameterAnnotations_attribute(int paramInt, Annotation[][] paramArrayOfAnnotation) {
/* 50 */     super(paramInt, paramArrayOfAnnotation);
/*    */   }
/*    */ 
/*    */   public <R, P> R accept(Attribute.Visitor<R, P> paramVisitor, P paramP) {
/* 54 */     return paramVisitor.visitRuntimeInvisibleParameterAnnotations(this, paramP);
/*    */   }
/*    */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.classfile.RuntimeInvisibleParameterAnnotations_attribute
 * JD-Core Version:    0.6.2
 */