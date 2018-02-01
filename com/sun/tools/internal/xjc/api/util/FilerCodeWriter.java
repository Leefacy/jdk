/*    */ package com.sun.tools.internal.xjc.api.util;
/*    */ 
/*    */ import com.sun.codemodel.internal.CodeWriter;
/*    */ import com.sun.codemodel.internal.JPackage;
/*    */ import java.io.IOException;
/*    */ import java.io.OutputStream;
/*    */ import java.io.Writer;
/*    */ import javax.annotation.processing.Filer;
/*    */ import javax.lang.model.element.Element;
/*    */ import javax.tools.FileObject;
/*    */ import javax.tools.JavaFileObject;
/*    */ import javax.tools.StandardLocation;
/*    */ 
/*    */ public final class FilerCodeWriter extends CodeWriter
/*    */ {
/*    */   private final Filer filer;
/*    */ 
/*    */   public FilerCodeWriter(Filer filer)
/*    */   {
/* 50 */     this.filer = filer;
/*    */   }
/*    */ 
/*    */   public OutputStream openBinary(JPackage pkg, String fileName)
/*    */     throws IOException
/*    */   {
/*    */     StandardLocation loc;
/*    */     StandardLocation loc;
/* 55 */     if (fileName.endsWith(".java"))
/*    */     {
/* 58 */       loc = StandardLocation.SOURCE_PATH;
/*    */     }
/*    */     else {
/* 61 */       loc = StandardLocation.CLASS_PATH;
/*    */     }
/* 63 */     return this.filer.createResource(loc, pkg.name(), fileName, new Element[0]).openOutputStream();
/*    */   }
/*    */ 
/*    */   public Writer openSource(JPackage pkg, String fileName)
/*    */     throws IOException
/*    */   {
/*    */     String name;
/* 68 */     if (pkg.isUnnamed())
/* 69 */       name = fileName;
/*    */     else {
/* 71 */       name = pkg.name() + '.' + fileName;
/*    */     }
/* 73 */     String name = name.substring(0, name.length() - 5);
/*    */ 
/* 75 */     return this.filer.createSourceFile(name, new Element[0]).openWriter();
/*    */   }
/*    */ 
/*    */   public void close()
/*    */   {
/*    */   }
/*    */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.internal.xjc.api.util.FilerCodeWriter
 * JD-Core Version:    0.6.2
 */