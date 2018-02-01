/*    */ package com.sun.tools.internal.xjc;
/*    */ 
/*    */ import com.sun.istack.internal.Nullable;
/*    */ 
/*    */ public class BadCommandLineException extends Exception
/*    */ {
/*    */   private Options options;
/*    */ 
/*    */   public BadCommandLineException(String msg)
/*    */   {
/* 37 */     super(msg);
/*    */   }
/*    */ 
/*    */   public BadCommandLineException(String message, Throwable cause) {
/* 41 */     super(message, cause);
/*    */   }
/*    */ 
/*    */   public BadCommandLineException() {
/* 45 */     this(null);
/*    */   }
/*    */ 
/*    */   public void initOptions(Options opt) {
/* 49 */     assert (this.options == null);
/* 50 */     this.options = opt;
/*    */   }
/*    */ 
/*    */   @Nullable
/*    */   public Options getOptions()
/*    */   {
/* 57 */     return this.options;
/*    */   }
/*    */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.internal.xjc.BadCommandLineException
 * JD-Core Version:    0.6.2
 */