/*    */ package com.sun.codemodel.internal.fmt;
/*    */ 
/*    */ import com.sun.codemodel.internal.JResourceFile;
/*    */ import java.io.IOException;
/*    */ import java.io.OutputStream;
/*    */ import java.io.OutputStreamWriter;
/*    */ import java.io.Writer;
/*    */ 
/*    */ public class JTextFile extends JResourceFile
/*    */ {
/* 48 */   private String contents = null;
/*    */ 
/*    */   public JTextFile(String name)
/*    */   {
/* 45 */     super(name);
/*    */   }
/*    */ 
/*    */   public void setContents(String _contents)
/*    */   {
/* 51 */     this.contents = _contents;
/*    */   }
/*    */ 
/*    */   public void build(OutputStream out) throws IOException {
/* 55 */     Writer w = new OutputStreamWriter(out);
/* 56 */     w.write(this.contents);
/* 57 */     w.close();
/*    */   }
/*    */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.codemodel.internal.fmt.JTextFile
 * JD-Core Version:    0.6.2
 */