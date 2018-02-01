/*    */ package com.sun.codemodel.internal.fmt;
/*    */ 
/*    */ import com.sun.codemodel.internal.JResourceFile;
/*    */ import java.io.DataInputStream;
/*    */ import java.io.IOException;
/*    */ import java.io.OutputStream;
/*    */ 
/*    */ public final class JStaticFile extends JResourceFile
/*    */ {
/*    */   private final ClassLoader classLoader;
/*    */   private final String resourceName;
/*    */   private final boolean isResource;
/*    */ 
/*    */   public JStaticFile(String _resourceName)
/*    */   {
/* 47 */     this(_resourceName, !_resourceName.endsWith(".java"));
/*    */   }
/*    */ 
/*    */   public JStaticFile(String _resourceName, boolean isResource) {
/* 51 */     this(SecureLoader.getClassClassLoader(JStaticFile.class), _resourceName, isResource);
/*    */   }
/*    */ 
/*    */   public JStaticFile(ClassLoader _classLoader, String _resourceName, boolean isResource)
/*    */   {
/* 59 */     super(_resourceName.substring(_resourceName.lastIndexOf('/') + 1));
/* 60 */     this.classLoader = _classLoader;
/* 61 */     this.resourceName = _resourceName;
/* 62 */     this.isResource = isResource;
/*    */   }
/*    */ 
/*    */   protected boolean isResource() {
/* 66 */     return this.isResource;
/*    */   }
/*    */ 
/*    */   protected void build(OutputStream os) throws IOException {
/* 70 */     DataInputStream dis = new DataInputStream(this.classLoader.getResourceAsStream(this.resourceName));
/*    */ 
/* 72 */     byte[] buf = new byte[256];
/*    */     int sz;
/* 74 */     while ((sz = dis.read(buf)) > 0) {
/* 75 */       os.write(buf, 0, sz);
/*    */     }
/* 77 */     dis.close();
/*    */   }
/*    */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.codemodel.internal.fmt.JStaticFile
 * JD-Core Version:    0.6.2
 */