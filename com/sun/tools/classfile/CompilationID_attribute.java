/*    */ package com.sun.tools.classfile;
/*    */ 
/*    */ import java.io.IOException;
/*    */ 
/*    */ public class CompilationID_attribute extends Attribute
/*    */ {
/*    */   public final int compilationID_index;
/*    */ 
/*    */   CompilationID_attribute(ClassReader paramClassReader, int paramInt1, int paramInt2)
/*    */     throws IOException
/*    */   {
/* 38 */     super(paramInt1, paramInt2);
/* 39 */     this.compilationID_index = paramClassReader.readUnsignedShort();
/*    */   }
/*    */ 
/*    */   public CompilationID_attribute(ConstantPool paramConstantPool, int paramInt) throws ConstantPoolException
/*    */   {
/* 44 */     this(paramConstantPool.getUTF8Index("CompilationID"), paramInt);
/*    */   }
/*    */ 
/*    */   public CompilationID_attribute(int paramInt1, int paramInt2) {
/* 48 */     super(paramInt1, 2);
/* 49 */     this.compilationID_index = paramInt2;
/*    */   }
/*    */ 
/*    */   String getCompilationID(ConstantPool paramConstantPool) throws ConstantPoolException
/*    */   {
/* 54 */     return paramConstantPool.getUTF8Value(this.compilationID_index);
/*    */   }
/*    */ 
/*    */   public <R, D> R accept(Attribute.Visitor<R, D> paramVisitor, D paramD) {
/* 58 */     return paramVisitor.visitCompilationID(this, paramD);
/*    */   }
/*    */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.classfile.CompilationID_attribute
 * JD-Core Version:    0.6.2
 */