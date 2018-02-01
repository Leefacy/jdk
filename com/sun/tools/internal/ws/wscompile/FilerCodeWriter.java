/*    */ package com.sun.tools.internal.ws.wscompile;
/*    */ 
/*    */ import com.sun.codemodel.internal.JPackage;
/*    */ import java.io.File;
/*    */ import java.io.IOException;
/*    */ import java.io.Writer;
/*    */ import javax.annotation.processing.Filer;
/*    */ import javax.lang.model.element.Element;
/*    */ import javax.tools.JavaFileObject;
/*    */ 
/*    */ public class FilerCodeWriter extends WSCodeWriter
/*    */ {
/*    */   private final Filer filer;
/*    */   private Writer w;
/*    */ 
/*    */   public FilerCodeWriter(File outDir, Options options)
/*    */     throws IOException
/*    */   {
/* 48 */     super(outDir, options);
/* 49 */     this.filer = options.filer;
/*    */   }
/*    */ 
/*    */   public Writer openSource(JPackage pkg, String fileName) throws IOException {
/* 53 */     String tmp = fileName.substring(0, fileName.length() - 5);
/* 54 */     if ((pkg.name() != null) && (!"".equals(pkg.name())))
/* 55 */       this.w = this.filer.createSourceFile(pkg.name() + "." + tmp, new Element[0]).openWriter();
/*    */     else {
/* 57 */       this.w = this.filer.createSourceFile(tmp, new Element[0]).openWriter();
/*    */     }
/* 59 */     return this.w;
/*    */   }
/*    */ 
/*    */   public void close() throws IOException
/*    */   {
/* 64 */     super.close();
/* 65 */     if (this.w != null)
/* 66 */       this.w.close();
/* 67 */     this.w = null;
/*    */   }
/*    */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.internal.ws.wscompile.FilerCodeWriter
 * JD-Core Version:    0.6.2
 */