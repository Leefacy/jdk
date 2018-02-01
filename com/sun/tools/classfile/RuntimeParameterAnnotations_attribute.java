/*    */ package com.sun.tools.classfile;
/*    */ 
/*    */ import java.io.IOException;
/*    */ 
/*    */ public abstract class RuntimeParameterAnnotations_attribute extends Attribute
/*    */ {
/*    */   public final Annotation[][] parameter_annotations;
/*    */ 
/*    */   RuntimeParameterAnnotations_attribute(ClassReader paramClassReader, int paramInt1, int paramInt2)
/*    */     throws IOException, Annotation.InvalidAnnotation
/*    */   {
/* 41 */     super(paramInt1, paramInt2);
/* 42 */     int i = paramClassReader.readUnsignedByte();
/* 43 */     this.parameter_annotations = new Annotation[i][];
/* 44 */     for (int j = 0; j < this.parameter_annotations.length; j++) {
/* 45 */       int k = paramClassReader.readUnsignedShort();
/* 46 */       Annotation[] arrayOfAnnotation = new Annotation[k];
/* 47 */       for (int m = 0; m < k; m++)
/* 48 */         arrayOfAnnotation[m] = new Annotation(paramClassReader);
/* 49 */       this.parameter_annotations[j] = arrayOfAnnotation;
/*    */     }
/*    */   }
/*    */ 
/*    */   protected RuntimeParameterAnnotations_attribute(int paramInt, Annotation[][] paramArrayOfAnnotation) {
/* 54 */     super(paramInt, length(paramArrayOfAnnotation));
/* 55 */     this.parameter_annotations = paramArrayOfAnnotation;
/*    */   }
/*    */ 
/*    */   private static int length(Annotation[][] paramArrayOfAnnotation) {
/* 59 */     int i = 1;
/* 60 */     for (Annotation[] arrayOfAnnotation1 : paramArrayOfAnnotation) {
/* 61 */       i += 2;
/* 62 */       for (Annotation localAnnotation : arrayOfAnnotation1)
/* 63 */         i += localAnnotation.length();
/*    */     }
/* 65 */     return i;
/*    */   }
/*    */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.classfile.RuntimeParameterAnnotations_attribute
 * JD-Core Version:    0.6.2
 */