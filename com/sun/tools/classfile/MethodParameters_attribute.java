/*    */ package com.sun.tools.classfile;
/*    */ 
/*    */ import java.io.IOException;
/*    */ 
/*    */ public class MethodParameters_attribute extends Attribute
/*    */ {
/*    */   public final int method_parameter_table_length;
/*    */   public final Entry[] method_parameter_table;
/*    */ 
/*    */   MethodParameters_attribute(ClassReader paramClassReader, int paramInt1, int paramInt2)
/*    */     throws IOException
/*    */   {
/* 47 */     super(paramInt1, paramInt2);
/*    */ 
/* 49 */     this.method_parameter_table_length = paramClassReader.readUnsignedByte();
/* 50 */     this.method_parameter_table = new Entry[this.method_parameter_table_length];
/* 51 */     for (int i = 0; i < this.method_parameter_table_length; i++)
/* 52 */       this.method_parameter_table[i] = new Entry(paramClassReader);
/*    */   }
/*    */ 
/*    */   public MethodParameters_attribute(ConstantPool paramConstantPool, Entry[] paramArrayOfEntry)
/*    */     throws ConstantPoolException
/*    */   {
/* 58 */     this(paramConstantPool.getUTF8Index("MethodParameters"), paramArrayOfEntry);
/*    */   }
/*    */ 
/*    */   public MethodParameters_attribute(int paramInt, Entry[] paramArrayOfEntry)
/*    */   {
/* 64 */     super(paramInt, 1 + paramArrayOfEntry.length * Entry.length());
/* 65 */     this.method_parameter_table_length = paramArrayOfEntry.length;
/* 66 */     this.method_parameter_table = paramArrayOfEntry;
/*    */   }
/*    */ 
/*    */   public <R, D> R accept(Attribute.Visitor<R, D> paramVisitor, D paramD) {
/* 70 */     return paramVisitor.visitMethodParameters(this, paramD); } 
/*    */   public static class Entry { public final int name_index;
/*    */     public final int flags;
/*    */ 
/* 75 */     Entry(ClassReader paramClassReader) throws IOException { this.name_index = paramClassReader.readUnsignedShort();
/* 76 */       this.flags = paramClassReader.readUnsignedShort(); }
/*    */ 
/*    */     public static int length()
/*    */     {
/* 80 */       return 6;
/*    */     }
/*    */   }
/*    */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.classfile.MethodParameters_attribute
 * JD-Core Version:    0.6.2
 */