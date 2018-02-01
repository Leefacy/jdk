/*    */ package com.sun.tools.classfile;
/*    */ 
/*    */ import java.io.IOException;
/*    */ 
/*    */ public class InnerClasses_attribute extends Attribute
/*    */ {
/*    */   public final int number_of_classes;
/*    */   public final Info[] classes;
/*    */ 
/*    */   InnerClasses_attribute(ClassReader paramClassReader, int paramInt1, int paramInt2)
/*    */     throws IOException
/*    */   {
/* 42 */     super(paramInt1, paramInt2);
/* 43 */     this.number_of_classes = paramClassReader.readUnsignedShort();
/* 44 */     this.classes = new Info[this.number_of_classes];
/* 45 */     for (int i = 0; i < this.number_of_classes; i++)
/* 46 */       this.classes[i] = new Info(paramClassReader);
/*    */   }
/*    */ 
/*    */   public InnerClasses_attribute(ConstantPool paramConstantPool, Info[] paramArrayOfInfo) throws ConstantPoolException
/*    */   {
/* 51 */     this(paramConstantPool.getUTF8Index("InnerClasses"), paramArrayOfInfo);
/*    */   }
/*    */ 
/*    */   public InnerClasses_attribute(int paramInt, Info[] paramArrayOfInfo) {
/* 55 */     super(paramInt, 2 + Info.length() * paramArrayOfInfo.length);
/* 56 */     this.number_of_classes = paramArrayOfInfo.length;
/* 57 */     this.classes = paramArrayOfInfo;
/*    */   }
/*    */ 
/*    */   public <R, D> R accept(Attribute.Visitor<R, D> paramVisitor, D paramD) {
/* 61 */     return paramVisitor.visitInnerClasses(this, paramD); } 
/*    */   public static class Info { public final int inner_class_info_index;
/*    */     public final int outer_class_info_index;
/*    */     public final int inner_name_index;
/*    */     public final AccessFlags inner_class_access_flags;
/*    */ 
/* 69 */     Info(ClassReader paramClassReader) throws IOException { this.inner_class_info_index = paramClassReader.readUnsignedShort();
/* 70 */       this.outer_class_info_index = paramClassReader.readUnsignedShort();
/* 71 */       this.inner_name_index = paramClassReader.readUnsignedShort();
/* 72 */       this.inner_class_access_flags = new AccessFlags(paramClassReader.readUnsignedShort()); }
/*    */ 
/*    */     public ConstantPool.CONSTANT_Class_info getInnerClassInfo(ConstantPool paramConstantPool) throws ConstantPoolException
/*    */     {
/* 76 */       if (this.inner_class_info_index == 0)
/* 77 */         return null;
/* 78 */       return paramConstantPool.getClassInfo(this.inner_class_info_index);
/*    */     }
/*    */ 
/*    */     public ConstantPool.CONSTANT_Class_info getOuterClassInfo(ConstantPool paramConstantPool) throws ConstantPoolException {
/* 82 */       if (this.outer_class_info_index == 0)
/* 83 */         return null;
/* 84 */       return paramConstantPool.getClassInfo(this.outer_class_info_index);
/*    */     }
/*    */ 
/*    */     public String getInnerName(ConstantPool paramConstantPool) throws ConstantPoolException {
/* 88 */       if (this.inner_name_index == 0)
/* 89 */         return null;
/* 90 */       return paramConstantPool.getUTF8Value(this.inner_name_index);
/*    */     }
/*    */ 
/*    */     public static int length() {
/* 94 */       return 8;
/*    */     }
/*    */   }
/*    */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.classfile.InnerClasses_attribute
 * JD-Core Version:    0.6.2
 */