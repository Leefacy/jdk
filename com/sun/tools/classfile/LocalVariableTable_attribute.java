/*    */ package com.sun.tools.classfile;
/*    */ 
/*    */ import java.io.IOException;
/*    */ 
/*    */ public class LocalVariableTable_attribute extends Attribute
/*    */ {
/*    */   public final int local_variable_table_length;
/*    */   public final Entry[] local_variable_table;
/*    */ 
/*    */   LocalVariableTable_attribute(ClassReader paramClassReader, int paramInt1, int paramInt2)
/*    */     throws IOException
/*    */   {
/* 40 */     super(paramInt1, paramInt2);
/* 41 */     this.local_variable_table_length = paramClassReader.readUnsignedShort();
/* 42 */     this.local_variable_table = new Entry[this.local_variable_table_length];
/* 43 */     for (int i = 0; i < this.local_variable_table_length; i++)
/* 44 */       this.local_variable_table[i] = new Entry(paramClassReader);
/*    */   }
/*    */ 
/*    */   public LocalVariableTable_attribute(ConstantPool paramConstantPool, Entry[] paramArrayOfEntry) throws ConstantPoolException
/*    */   {
/* 49 */     this(paramConstantPool.getUTF8Index("LocalVariableTable"), paramArrayOfEntry);
/*    */   }
/*    */ 
/*    */   public LocalVariableTable_attribute(int paramInt, Entry[] paramArrayOfEntry) {
/* 53 */     super(paramInt, 2 + paramArrayOfEntry.length * Entry.length());
/* 54 */     this.local_variable_table_length = paramArrayOfEntry.length;
/* 55 */     this.local_variable_table = paramArrayOfEntry;
/*    */   }
/*    */ 
/*    */   public <R, D> R accept(Attribute.Visitor<R, D> paramVisitor, D paramD) {
/* 59 */     return paramVisitor.visitLocalVariableTable(this, paramD); } 
/*    */   public static class Entry { public final int start_pc;
/*    */     public final int length;
/*    */     public final int name_index;
/*    */     public final int descriptor_index;
/*    */     public final int index;
/*    */ 
/* 67 */     Entry(ClassReader paramClassReader) throws IOException { this.start_pc = paramClassReader.readUnsignedShort();
/* 68 */       this.length = paramClassReader.readUnsignedShort();
/* 69 */       this.name_index = paramClassReader.readUnsignedShort();
/* 70 */       this.descriptor_index = paramClassReader.readUnsignedShort();
/* 71 */       this.index = paramClassReader.readUnsignedShort(); }
/*    */ 
/*    */     public static int length()
/*    */     {
/* 75 */       return 10;
/*    */     }
/*    */   }
/*    */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.classfile.LocalVariableTable_attribute
 * JD-Core Version:    0.6.2
 */