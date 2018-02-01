/*    */ package sun.tools.tree;
/*    */ 
/*    */ import sun.tools.java.Environment;
/*    */ import sun.tools.java.Type;
/*    */ 
/*    */ public class BinaryCompareExpression extends BinaryExpression
/*    */ {
/*    */   public BinaryCompareExpression(int paramInt, long paramLong, Expression paramExpression1, Expression paramExpression2)
/*    */   {
/* 41 */     super(paramInt, paramLong, Type.tBoolean, paramExpression1, paramExpression2);
/*    */   }
/*    */ 
/*    */   void selectType(Environment paramEnvironment, Context paramContext, int paramInt)
/*    */   {
/* 48 */     Type localType = Type.tInt;
/* 49 */     if ((paramInt & 0x80) != 0)
/* 50 */       localType = Type.tDouble;
/* 51 */     else if ((paramInt & 0x40) != 0)
/* 52 */       localType = Type.tFloat;
/* 53 */     else if ((paramInt & 0x20) != 0) {
/* 54 */       localType = Type.tLong;
/*    */     }
/* 56 */     this.left = convert(paramEnvironment, paramContext, localType, this.left);
/* 57 */     this.right = convert(paramEnvironment, paramContext, localType, this.right);
/*    */   }
/*    */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     sun.tools.tree.BinaryCompareExpression
 * JD-Core Version:    0.6.2
 */