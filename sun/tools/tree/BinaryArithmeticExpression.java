/*    */ package sun.tools.tree;
/*    */ 
/*    */ import sun.tools.java.Environment;
/*    */ import sun.tools.java.Type;
/*    */ 
/*    */ public class BinaryArithmeticExpression extends BinaryExpression
/*    */ {
/*    */   public BinaryArithmeticExpression(int paramInt, long paramLong, Expression paramExpression1, Expression paramExpression2)
/*    */   {
/* 41 */     super(paramInt, paramLong, paramExpression1.type, paramExpression1, paramExpression2);
/*    */   }
/*    */ 
/*    */   void selectType(Environment paramEnvironment, Context paramContext, int paramInt)
/*    */   {
/* 48 */     if ((paramInt & 0x80) != 0)
/* 49 */       this.type = Type.tDouble;
/* 50 */     else if ((paramInt & 0x40) != 0)
/* 51 */       this.type = Type.tFloat;
/* 52 */     else if ((paramInt & 0x20) != 0)
/* 53 */       this.type = Type.tLong;
/*    */     else {
/* 55 */       this.type = Type.tInt;
/*    */     }
/* 57 */     this.left = convert(paramEnvironment, paramContext, this.type, this.left);
/* 58 */     this.right = convert(paramEnvironment, paramContext, this.type, this.right);
/*    */   }
/*    */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     sun.tools.tree.BinaryArithmeticExpression
 * JD-Core Version:    0.6.2
 */