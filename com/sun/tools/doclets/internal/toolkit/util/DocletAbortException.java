/*    */ package com.sun.tools.doclets.internal.toolkit.util;
/*    */ 
/*    */ public class DocletAbortException extends RuntimeException
/*    */ {
/*    */   private static final long serialVersionUID = -9131058909576418984L;
/*    */ 
/*    */   public DocletAbortException(String paramString)
/*    */   {
/* 38 */     super(paramString);
/*    */   }
/*    */ 
/*    */   public DocletAbortException(Throwable paramThrowable) {
/* 42 */     super(paramThrowable);
/*    */   }
/*    */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.doclets.internal.toolkit.util.DocletAbortException
 * JD-Core Version:    0.6.2
 */