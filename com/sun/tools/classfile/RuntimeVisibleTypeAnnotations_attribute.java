/*    */ package com.sun.tools.classfile;
/*    */ 
/*    */ import java.io.IOException;
/*    */ 
/*    */ public class RuntimeVisibleTypeAnnotations_attribute extends RuntimeTypeAnnotations_attribute
/*    */ {
/*    */   RuntimeVisibleTypeAnnotations_attribute(ClassReader paramClassReader, int paramInt1, int paramInt2)
/*    */     throws IOException, Annotation.InvalidAnnotation
/*    */   {
/* 41 */     super(paramClassReader, paramInt1, paramInt2);
/*    */   }
/*    */ 
/*    */   public RuntimeVisibleTypeAnnotations_attribute(ConstantPool paramConstantPool, TypeAnnotation[] paramArrayOfTypeAnnotation) throws ConstantPoolException
/*    */   {
/* 46 */     this(paramConstantPool.getUTF8Index("RuntimeVisibleTypeAnnotations"), paramArrayOfTypeAnnotation);
/*    */   }
/*    */ 
/*    */   public RuntimeVisibleTypeAnnotations_attribute(int paramInt, TypeAnnotation[] paramArrayOfTypeAnnotation) {
/* 50 */     super(paramInt, paramArrayOfTypeAnnotation);
/*    */   }
/*    */ 
/*    */   public <R, P> R accept(Attribute.Visitor<R, P> paramVisitor, P paramP) {
/* 54 */     return paramVisitor.visitRuntimeVisibleTypeAnnotations(this, paramP);
/*    */   }
/*    */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.classfile.RuntimeVisibleTypeAnnotations_attribute
 * JD-Core Version:    0.6.2
 */