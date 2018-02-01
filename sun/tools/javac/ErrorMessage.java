/*    */ package sun.tools.javac;
/*    */ 
/*    */ @Deprecated
/*    */ final class ErrorMessage
/*    */ {
/*    */   long where;
/*    */   String message;
/*    */   ErrorMessage next;
/*    */ 
/*    */   ErrorMessage(long paramLong, String paramString)
/*    */   {
/* 46 */     this.where = paramLong;
/* 47 */     this.message = paramString;
/*    */   }
/*    */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     sun.tools.javac.ErrorMessage
 * JD-Core Version:    0.6.2
 */