/*    */ package com.sun.tools.internal.jxc.api;
/*    */ 
/*    */ import com.sun.tools.internal.jxc.api.impl.j2s.JavaCompilerImpl;
/*    */ import com.sun.tools.internal.xjc.api.JavaCompiler;
/*    */ 
/*    */ public class JXC
/*    */ {
/*    */   public static JavaCompiler createJavaCompiler()
/*    */   {
/* 43 */     return new JavaCompilerImpl();
/*    */   }
/*    */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.internal.jxc.api.JXC
 * JD-Core Version:    0.6.2
 */