/*    */ package sun.tools.tree;
/*    */ 
/*    */ import sun.tools.asm.Assembler;
/*    */ import sun.tools.java.Environment;
/*    */ import sun.tools.java.Type;
/*    */ 
/*    */ public class SubtractExpression extends BinaryArithmeticExpression
/*    */ {
/*    */   public SubtractExpression(long paramLong, Expression paramExpression1, Expression paramExpression2)
/*    */   {
/* 42 */     super(30, paramLong, paramExpression1, paramExpression2);
/*    */   }
/*    */ 
/*    */   Expression eval(int paramInt1, int paramInt2)
/*    */   {
/* 49 */     return new IntExpression(this.where, paramInt1 - paramInt2);
/*    */   }
/*    */   Expression eval(long paramLong1, long paramLong2) {
/* 52 */     return new LongExpression(this.where, paramLong1 - paramLong2);
/*    */   }
/*    */   Expression eval(float paramFloat1, float paramFloat2) {
/* 55 */     return new FloatExpression(this.where, paramFloat1 - paramFloat2);
/*    */   }
/*    */   Expression eval(double paramDouble1, double paramDouble2) {
/* 58 */     return new DoubleExpression(this.where, paramDouble1 - paramDouble2);
/*    */   }
/*    */ 
/*    */   Expression simplify()
/*    */   {
/* 66 */     if (this.type.inMask(62)) {
/* 67 */       if (this.left.equals(0)) {
/* 68 */         return new NegativeExpression(this.where, this.right);
/*    */       }
/* 70 */       if (this.right.equals(0)) {
/* 71 */         return this.left;
/*    */       }
/*    */     }
/* 74 */     return this;
/*    */   }
/*    */ 
/*    */   void codeOperation(Environment paramEnvironment, Context paramContext, Assembler paramAssembler)
/*    */   {
/* 81 */     paramAssembler.add(this.where, 100 + this.type.getTypeCodeOffset());
/*    */   }
/*    */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     sun.tools.tree.SubtractExpression
 * JD-Core Version:    0.6.2
 */