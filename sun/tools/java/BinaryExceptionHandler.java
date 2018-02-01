/*    */ package sun.tools.java;
/*    */ 
/*    */ public class BinaryExceptionHandler
/*    */ {
/*    */   public int startPC;
/*    */   public int endPC;
/*    */   public int handlerPC;
/*    */   public ClassDeclaration exceptionClass;
/*    */ 
/*    */   BinaryExceptionHandler(int paramInt1, int paramInt2, int paramInt3, ClassDeclaration paramClassDeclaration)
/*    */   {
/* 44 */     this.startPC = paramInt1;
/* 45 */     this.endPC = paramInt2;
/* 46 */     this.handlerPC = paramInt3;
/* 47 */     this.exceptionClass = paramClassDeclaration;
/*    */   }
/*    */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     sun.tools.java.BinaryExceptionHandler
 * JD-Core Version:    0.6.2
 */