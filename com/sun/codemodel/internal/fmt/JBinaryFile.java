/*    */ package com.sun.codemodel.internal.fmt;
/*    */ 
/*    */ import com.sun.codemodel.internal.JResourceFile;
/*    */ import java.io.ByteArrayOutputStream;
/*    */ import java.io.IOException;
/*    */ import java.io.OutputStream;
/*    */ 
/*    */ public final class JBinaryFile extends JResourceFile
/*    */ {
/* 43 */   private final ByteArrayOutputStream baos = new ByteArrayOutputStream();
/*    */ 
/*    */   public JBinaryFile(String name) {
/* 46 */     super(name);
/*    */   }
/*    */ 
/*    */   public OutputStream getDataStore()
/*    */   {
/* 56 */     return this.baos;
/*    */   }
/*    */ 
/*    */   public void build(OutputStream os) throws IOException {
/* 60 */     os.write(this.baos.toByteArray());
/*    */   }
/*    */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.codemodel.internal.fmt.JBinaryFile
 * JD-Core Version:    0.6.2
 */