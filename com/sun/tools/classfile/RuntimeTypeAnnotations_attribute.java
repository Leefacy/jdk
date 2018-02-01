/*    */ package com.sun.tools.classfile;
/*    */ 
/*    */ import java.io.IOException;
/*    */ 
/*    */ public abstract class RuntimeTypeAnnotations_attribute extends Attribute
/*    */ {
/*    */   public final TypeAnnotation[] annotations;
/*    */ 
/*    */   protected RuntimeTypeAnnotations_attribute(ClassReader paramClassReader, int paramInt1, int paramInt2)
/*    */     throws IOException, Annotation.InvalidAnnotation
/*    */   {
/* 41 */     super(paramInt1, paramInt2);
/* 42 */     int i = paramClassReader.readUnsignedShort();
/* 43 */     this.annotations = new TypeAnnotation[i];
/* 44 */     for (int j = 0; j < this.annotations.length; j++)
/* 45 */       this.annotations[j] = new TypeAnnotation(paramClassReader);
/*    */   }
/*    */ 
/*    */   protected RuntimeTypeAnnotations_attribute(int paramInt, TypeAnnotation[] paramArrayOfTypeAnnotation) {
/* 49 */     super(paramInt, length(paramArrayOfTypeAnnotation));
/* 50 */     this.annotations = paramArrayOfTypeAnnotation;
/*    */   }
/*    */ 
/*    */   private static int length(TypeAnnotation[] paramArrayOfTypeAnnotation) {
/* 54 */     int i = 2;
/* 55 */     for (TypeAnnotation localTypeAnnotation : paramArrayOfTypeAnnotation)
/* 56 */       i += localTypeAnnotation.length();
/* 57 */     return i;
/*    */   }
/*    */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.classfile.RuntimeTypeAnnotations_attribute
 * JD-Core Version:    0.6.2
 */