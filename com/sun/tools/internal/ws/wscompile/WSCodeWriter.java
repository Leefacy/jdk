/*    */ package com.sun.tools.internal.ws.wscompile;
/*    */ 
/*    */ import com.sun.codemodel.internal.JPackage;
/*    */ import com.sun.codemodel.internal.writer.FileCodeWriter;
/*    */ import java.io.File;
/*    */ import java.io.IOException;
/*    */ 
/*    */ public class WSCodeWriter extends FileCodeWriter
/*    */ {
/*    */   private final Options options;
/*    */ 
/*    */   public WSCodeWriter(File outDir, Options options)
/*    */     throws IOException
/*    */   {
/* 45 */     super(outDir, options.encoding);
/* 46 */     this.options = options;
/*    */   }
/*    */ 
/*    */   protected File getFile(JPackage pkg, String fileName) throws IOException {
/* 50 */     File f = super.getFile(pkg, fileName);
/*    */ 
/* 52 */     this.options.addGeneratedFile(f);
/*    */ 
/* 58 */     return f;
/*    */   }
/*    */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.internal.ws.wscompile.WSCodeWriter
 * JD-Core Version:    0.6.2
 */