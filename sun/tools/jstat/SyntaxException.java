/*    */ package sun.tools.jstat;
/*    */ 
/*    */ import java.util.Iterator;
/*    */ import java.util.Set;
/*    */ 
/*    */ public class SyntaxException extends ParserException
/*    */ {
/*    */   private String message;
/*    */ 
/*    */   public SyntaxException(String paramString)
/*    */   {
/* 43 */     this.message = paramString;
/*    */   }
/*    */ 
/*    */   public SyntaxException(int paramInt, String paramString1, String paramString2) {
/* 47 */     this.message = ("Syntax error at line " + paramInt + ": Expected " + paramString1 + ", Found " + paramString2);
/*    */   }
/*    */ 
/*    */   public SyntaxException(int paramInt, String paramString, Token paramToken)
/*    */   {
/* 53 */     this.message = 
/* 55 */       ("Syntax error at line " + paramInt + ": Expected " + paramString + ", Found " + paramToken
/* 55 */       .toMessage());
/*    */   }
/*    */ 
/*    */   public SyntaxException(int paramInt, Token paramToken1, Token paramToken2) {
/* 59 */     this.message = 
/* 61 */       ("Syntax error at line " + paramInt + ": Expected " + paramToken1
/* 60 */       .toMessage() + ", Found " + paramToken2
/* 61 */       .toMessage());
/*    */   }
/*    */ 
/*    */   public SyntaxException(int paramInt, Set paramSet, Token paramToken) {
/* 65 */     StringBuilder localStringBuilder = new StringBuilder();
/*    */ 
/* 67 */     localStringBuilder.append("Syntax error at line " + paramInt + ": Expected one of '");
/*    */ 
/* 69 */     int i = 1;
/* 70 */     for (Iterator localIterator = paramSet.iterator(); localIterator.hasNext(); ) {
/* 71 */       String str = (String)localIterator.next();
/* 72 */       if (i != 0) {
/* 73 */         localStringBuilder.append(str);
/* 74 */         i = 0;
/*    */       } else {
/* 76 */         localStringBuilder.append("|" + str);
/*    */       }
/*    */     }
/*    */ 
/* 80 */     localStringBuilder.append("', Found " + paramToken.toMessage());
/* 81 */     this.message = localStringBuilder.toString();
/*    */   }
/*    */ 
/*    */   public String getMessage() {
/* 85 */     return this.message;
/*    */   }
/*    */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     sun.tools.jstat.SyntaxException
 * JD-Core Version:    0.6.2
 */