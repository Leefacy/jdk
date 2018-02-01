/*    */ package sun.jvmstat.perfdata.monitor;
/*    */ 
/*    */ public class SyntaxException extends Exception
/*    */ {
/*    */   int lineno;
/*    */ 
/*    */   public SyntaxException(int paramInt)
/*    */   {
/* 43 */     this.lineno = paramInt;
/*    */   }
/*    */ 
/*    */   public String getMessage() {
/* 47 */     return "syntax error at line " + this.lineno;
/*    */   }
/*    */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     sun.jvmstat.perfdata.monitor.SyntaxException
 * JD-Core Version:    0.6.2
 */