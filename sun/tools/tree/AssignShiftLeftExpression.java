/*    */ package sun.tools.tree;
/*    */ 
/*    */ import sun.tools.asm.Assembler;
/*    */ import sun.tools.java.Environment;
/*    */ import sun.tools.java.Type;
/*    */ 
/*    */ public class AssignShiftLeftExpression extends AssignOpExpression
/*    */ {
/*    */   public AssignShiftLeftExpression(long paramLong, Expression paramExpression1, Expression paramExpression2)
/*    */   {
/* 42 */     super(7, paramLong, paramExpression1, paramExpression2);
/*    */   }
/*    */ 
/*    */   void codeOperation(Environment paramEnvironment, Context paramContext, Assembler paramAssembler)
/*    */   {
/* 50 */     paramAssembler.add(this.where, 120 + this.itype.getTypeCodeOffset());
/*    */   }
/*    */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     sun.tools.tree.AssignShiftLeftExpression
 * JD-Core Version:    0.6.2
 */