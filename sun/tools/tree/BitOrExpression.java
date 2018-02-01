/*    */ package sun.tools.tree;
/*    */ 
/*    */ import sun.tools.asm.Assembler;
/*    */ import sun.tools.java.Environment;
/*    */ import sun.tools.java.Type;
/*    */ 
/*    */ public class BitOrExpression extends BinaryBitExpression
/*    */ {
/*    */   public BitOrExpression(long paramLong, Expression paramExpression1, Expression paramExpression2)
/*    */   {
/* 42 */     super(16, paramLong, paramExpression1, paramExpression2);
/*    */   }
/*    */ 
/*    */   Expression eval(boolean paramBoolean1, boolean paramBoolean2)
/*    */   {
/* 49 */     return new BooleanExpression(this.where, paramBoolean1 | paramBoolean2);
/*    */   }
/*    */   Expression eval(int paramInt1, int paramInt2) {
/* 52 */     return new IntExpression(this.where, paramInt1 | paramInt2);
/*    */   }
/*    */   Expression eval(long paramLong1, long paramLong2) {
/* 55 */     return new LongExpression(this.where, paramLong1 | paramLong2);
/*    */   }
/*    */ 
/*    */   Expression simplify()
/*    */   {
/* 62 */     if ((this.left.equals(false)) || (this.left.equals(0)))
/* 63 */       return this.right;
/* 64 */     if ((this.right.equals(false)) || (this.right.equals(0)))
/* 65 */       return this.left;
/* 66 */     if (this.left.equals(true))
/* 67 */       return new CommaExpression(this.where, this.right, this.left).simplify();
/* 68 */     if (this.right.equals(true))
/* 69 */       return new CommaExpression(this.where, this.left, this.right).simplify();
/* 70 */     return this;
/*    */   }
/*    */ 
/*    */   void codeOperation(Environment paramEnvironment, Context paramContext, Assembler paramAssembler)
/*    */   {
/* 77 */     paramAssembler.add(this.where, 128 + this.type.getTypeCodeOffset());
/*    */   }
/*    */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     sun.tools.tree.BitOrExpression
 * JD-Core Version:    0.6.2
 */