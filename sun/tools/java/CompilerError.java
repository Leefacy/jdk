/*    */ package sun.tools.java;
/*    */ 
/*    */ public class CompilerError extends Error
/*    */ {
/*    */   Throwable e;
/*    */ 
/*    */   public CompilerError(String paramString)
/*    */   {
/* 44 */     super(paramString);
/* 45 */     this.e = this;
/*    */   }
/*    */ 
/*    */   public CompilerError(Exception paramException)
/*    */   {
/* 52 */     super(paramException.getMessage());
/* 53 */     this.e = paramException;
/*    */   }
/*    */ 
/*    */   public void printStackTrace() {
/* 57 */     if (this.e == this)
/* 58 */       super.printStackTrace();
/*    */     else
/* 60 */       this.e.printStackTrace();
/*    */   }
/*    */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     sun.tools.java.CompilerError
 * JD-Core Version:    0.6.2
 */