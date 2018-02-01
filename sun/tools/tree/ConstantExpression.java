/*    */ package sun.tools.tree;
/*    */ 
/*    */ import sun.tools.java.Type;
/*    */ 
/*    */ class ConstantExpression extends Expression
/*    */ {
/*    */   public ConstantExpression(int paramInt, long paramLong, Type paramType)
/*    */   {
/* 40 */     super(paramInt, paramLong, paramType);
/*    */   }
/*    */ 
/*    */   public boolean isConstant()
/*    */   {
/* 47 */     return true;
/*    */   }
/*    */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     sun.tools.tree.ConstantExpression
 * JD-Core Version:    0.6.2
 */