/*    */ package sun.tools.tree;
/*    */ 
/*    */ import sun.tools.java.Environment;
/*    */ import sun.tools.java.Type;
/*    */ 
/*    */ public abstract class DivRemExpression extends BinaryArithmeticExpression
/*    */ {
/*    */   public DivRemExpression(int paramInt, long paramLong, Expression paramExpression1, Expression paramExpression2)
/*    */   {
/* 43 */     super(paramInt, paramLong, paramExpression1, paramExpression2);
/*    */   }
/*    */ 
/*    */   public Expression inline(Environment paramEnvironment, Context paramContext)
/*    */   {
/* 52 */     if (this.type.inMask(62)) {
/* 53 */       this.right = this.right.inlineValue(paramEnvironment, paramContext);
/* 54 */       if ((this.right.isConstant()) && (!this.right.equals(0)))
/*    */       {
/* 56 */         this.left = this.left.inline(paramEnvironment, paramContext);
/* 57 */         return this.left;
/*    */       }
/* 59 */       this.left = this.left.inlineValue(paramEnvironment, paramContext);
/*    */       try {
/* 61 */         return eval().simplify();
/*    */       } catch (ArithmeticException localArithmeticException) {
/* 63 */         paramEnvironment.error(this.where, "arithmetic.exception");
/* 64 */         return this;
/*    */       }
/*    */ 
/*    */     }
/*    */ 
/* 69 */     return super.inline(paramEnvironment, paramContext);
/*    */   }
/*    */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     sun.tools.tree.DivRemExpression
 * JD-Core Version:    0.6.2
 */