/*    */ package com.sun.tools.classfile;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import java.nio.charset.Charset;
/*    */ 
/*    */ public class SourceDebugExtension_attribute extends Attribute
/*    */ {
/* 42 */   private static final Charset UTF8 = Charset.forName("UTF-8");
/*    */   public final byte[] debug_extension;
/*    */ 
/*    */   SourceDebugExtension_attribute(ClassReader paramClassReader, int paramInt1, int paramInt2)
/*    */     throws IOException
/*    */   {
/* 45 */     super(paramInt1, paramInt2);
/* 46 */     this.debug_extension = new byte[this.attribute_length];
/* 47 */     paramClassReader.readFully(this.debug_extension);
/*    */   }
/*    */ 
/*    */   public SourceDebugExtension_attribute(ConstantPool paramConstantPool, byte[] paramArrayOfByte) throws ConstantPoolException
/*    */   {
/* 52 */     this(paramConstantPool.getUTF8Index("SourceDebugExtension"), paramArrayOfByte);
/*    */   }
/*    */ 
/*    */   public SourceDebugExtension_attribute(int paramInt, byte[] paramArrayOfByte) {
/* 56 */     super(paramInt, paramArrayOfByte.length);
/* 57 */     this.debug_extension = paramArrayOfByte;
/*    */   }
/*    */ 
/*    */   public String getValue() {
/* 61 */     return new String(this.debug_extension, UTF8);
/*    */   }
/*    */ 
/*    */   public <R, D> R accept(Attribute.Visitor<R, D> paramVisitor, D paramD) {
/* 65 */     return paramVisitor.visitSourceDebugExtension(this, paramD);
/*    */   }
/*    */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.classfile.SourceDebugExtension_attribute
 * JD-Core Version:    0.6.2
 */