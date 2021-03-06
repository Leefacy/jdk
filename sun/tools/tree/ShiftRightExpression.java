/*    */ package sun.tools.tree;
/*    */ 
/*    */ import sun.tools.asm.Assembler;
/*    */ import sun.tools.java.Environment;
/*    */ import sun.tools.java.Type;
/*    */ 
/*    */ public class ShiftRightExpression extends BinaryShiftExpression
/*    */ {
/*    */   public ShiftRightExpression(long paramLong, Expression paramExpression1, Expression paramExpression2)
/*    */   {
/* 41 */     super(27, paramLong, paramExpression1, paramExpression2);
/*    */   }
/*    */ 
/*    */   Expression eval(int paramInt1, int paramInt2)
/*    */   {
/* 48 */     return new IntExpression(this.where, paramInt1 >> paramInt2);
/*    */   }
/*    */   Expression eval(long paramLong1, long paramLong2) {
/* 51 */     return new LongExpression(this.where, paramLong1 >> (int)paramLong2);
/*    */   }
/*    */ 
/*    */   Expression simplify()
/*    */   {
/* 58 */     if (this.right.equals(0)) {
/* 59 */       return this.left;
/*    */     }
/* 61 */     if (this.left.equals(0)) {
/* 62 */       return new CommaExpression(this.where, this.right, this.left).simplify();
/*    */     }
/* 64 */     return this;
/*    */   }
/*    */ 
/*    */   void codeOperation(Environment paramEnvironment, Context paramContext, Assembler paramAssembler)
/*    */   {
/* 71 */     paramAssembler.add(this.where, 122 + this.type.getTypeCodeOffset());
/*    */   }
/*    */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     sun.tools.tree.ShiftRightExpression
 * JD-Core Version:    0.6.2
 */