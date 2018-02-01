/*    */ package com.sun.tools.classfile;
/*    */ 
/*    */ import java.io.IOException;
/*    */ 
/*    */ public abstract class RuntimeAnnotations_attribute extends Attribute
/*    */ {
/*    */   public final Annotation[] annotations;
/*    */ 
/*    */   protected RuntimeAnnotations_attribute(ClassReader paramClassReader, int paramInt1, int paramInt2)
/*    */     throws IOException, Annotation.InvalidAnnotation
/*    */   {
/* 41 */     super(paramInt1, paramInt2);
/* 42 */     int i = paramClassReader.readUnsignedShort();
/* 43 */     this.annotations = new Annotation[i];
/* 44 */     for (int j = 0; j < this.annotations.length; j++)
/* 45 */       this.annotations[j] = new Annotation(paramClassReader);
/*    */   }
/*    */ 
/*    */   protected RuntimeAnnotations_attribute(int paramInt, Annotation[] paramArrayOfAnnotation) {
/* 49 */     super(paramInt, length(paramArrayOfAnnotation));
/* 50 */     this.annotations = paramArrayOfAnnotation;
/*    */   }
/*    */ 
/*    */   private static int length(Annotation[] paramArrayOfAnnotation) {
/* 54 */     int i = 2;
/* 55 */     for (Annotation localAnnotation : paramArrayOfAnnotation)
/* 56 */       i += localAnnotation.length();
/* 57 */     return i;
/*    */   }
/*    */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.classfile.RuntimeAnnotations_attribute
 * JD-Core Version:    0.6.2
 */