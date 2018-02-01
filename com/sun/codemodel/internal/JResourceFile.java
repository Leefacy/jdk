/*    */ package com.sun.codemodel.internal;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import java.io.OutputStream;
/*    */ 
/*    */ public abstract class JResourceFile
/*    */ {
/*    */   private final String name;
/*    */ 
/*    */   protected JResourceFile(String name)
/*    */   {
/* 39 */     this.name = name;
/*    */   }
/*    */ 
/*    */   public String name()
/*    */   {
/* 46 */     return this.name;
/*    */   }
/*    */ 
/*    */   protected boolean isResource()
/*    */   {
/* 58 */     return true;
/*    */   }
/*    */ 
/*    */   protected abstract void build(OutputStream paramOutputStream)
/*    */     throws IOException;
/*    */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.codemodel.internal.JResourceFile
 * JD-Core Version:    0.6.2
 */