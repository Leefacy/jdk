/*    */ package com.sun.tools.classfile;
/*    */ 
/*    */ import java.io.IOException;
/*    */ 
/*    */ public class Exceptions_attribute extends Attribute
/*    */ {
/*    */   public final int number_of_exceptions;
/*    */   public final int[] exception_index_table;
/*    */ 
/*    */   Exceptions_attribute(ClassReader paramClassReader, int paramInt1, int paramInt2)
/*    */     throws IOException
/*    */   {
/* 40 */     super(paramInt1, paramInt2);
/* 41 */     this.number_of_exceptions = paramClassReader.readUnsignedShort();
/* 42 */     this.exception_index_table = new int[this.number_of_exceptions];
/* 43 */     for (int i = 0; i < this.number_of_exceptions; i++)
/* 44 */       this.exception_index_table[i] = paramClassReader.readUnsignedShort();
/*    */   }
/*    */ 
/*    */   public Exceptions_attribute(ConstantPool paramConstantPool, int[] paramArrayOfInt) throws ConstantPoolException
/*    */   {
/* 49 */     this(paramConstantPool.getUTF8Index("Exceptions"), paramArrayOfInt);
/*    */   }
/*    */ 
/*    */   public Exceptions_attribute(int paramInt, int[] paramArrayOfInt) {
/* 53 */     super(paramInt, 2 + 2 * paramArrayOfInt.length);
/* 54 */     this.number_of_exceptions = paramArrayOfInt.length;
/* 55 */     this.exception_index_table = paramArrayOfInt;
/*    */   }
/*    */ 
/*    */   public String getException(int paramInt, ConstantPool paramConstantPool) throws ConstantPoolException {
/* 59 */     int i = this.exception_index_table[paramInt];
/* 60 */     return paramConstantPool.getClassInfo(i).getName();
/*    */   }
/*    */ 
/*    */   public <R, D> R accept(Attribute.Visitor<R, D> paramVisitor, D paramD) {
/* 64 */     return paramVisitor.visitExceptions(this, paramD);
/*    */   }
/*    */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.classfile.Exceptions_attribute
 * JD-Core Version:    0.6.2
 */