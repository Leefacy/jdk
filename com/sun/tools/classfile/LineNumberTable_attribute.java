/*    */ package com.sun.tools.classfile;
/*    */ 
/*    */ import java.io.IOException;
/*    */ 
/*    */ public class LineNumberTable_attribute extends Attribute
/*    */ {
/*    */   public final int line_number_table_length;
/*    */   public final Entry[] line_number_table;
/*    */ 
/*    */   LineNumberTable_attribute(ClassReader paramClassReader, int paramInt1, int paramInt2)
/*    */     throws IOException
/*    */   {
/* 40 */     super(paramInt1, paramInt2);
/* 41 */     this.line_number_table_length = paramClassReader.readUnsignedShort();
/* 42 */     this.line_number_table = new Entry[this.line_number_table_length];
/* 43 */     for (int i = 0; i < this.line_number_table_length; i++)
/* 44 */       this.line_number_table[i] = new Entry(paramClassReader);
/*    */   }
/*    */ 
/*    */   public LineNumberTable_attribute(ConstantPool paramConstantPool, Entry[] paramArrayOfEntry) throws ConstantPoolException
/*    */   {
/* 49 */     this(paramConstantPool.getUTF8Index("LineNumberTable"), paramArrayOfEntry);
/*    */   }
/*    */ 
/*    */   public LineNumberTable_attribute(int paramInt, Entry[] paramArrayOfEntry) {
/* 53 */     super(paramInt, 2 + paramArrayOfEntry.length * Entry.length());
/* 54 */     this.line_number_table_length = paramArrayOfEntry.length;
/* 55 */     this.line_number_table = paramArrayOfEntry;
/*    */   }
/*    */ 
/*    */   public <R, D> R accept(Attribute.Visitor<R, D> paramVisitor, D paramD) {
/* 59 */     return paramVisitor.visitLineNumberTable(this, paramD);
/*    */   }
/*    */   public static class Entry {
/*    */     public final int start_pc;
/*    */     public final int line_number;
/*    */ 
/*    */     Entry(ClassReader paramClassReader) throws IOException {
/* 67 */       this.start_pc = paramClassReader.readUnsignedShort();
/* 68 */       this.line_number = paramClassReader.readUnsignedShort();
/*    */     }
/*    */ 
/*    */     public static int length() {
/* 72 */       return 4;
/*    */     }
/*    */   }
/*    */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.classfile.LineNumberTable_attribute
 * JD-Core Version:    0.6.2
 */