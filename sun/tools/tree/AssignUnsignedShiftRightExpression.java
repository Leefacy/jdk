/*    */ package sun.tools.tree;
/*    */ 
/*    */ import sun.tools.asm.Assembler;
/*    */ import sun.tools.java.Environment;
/*    */ import sun.tools.java.Type;
/*    */ 
/*    */ public class AssignUnsignedShiftRightExpression extends AssignOpExpression
/*    */ {
/*    */   public AssignUnsignedShiftRightExpression(long paramLong, Expression paramExpression1, Expression paramExpression2)
/*    */   {
/* 42 */     super(9, paramLong, paramExpression1, paramExpression2);
/*    */   }
/*    */ 
/*    */   void codeOperation(Environment paramEnvironment, Context paramContext, Assembler paramAssembler)
/*    */   {
/* 49 */     paramAssembler.add(this.where, 124 + this.itype.getTypeCodeOffset());
/*    */   }
/*    */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     sun.tools.tree.AssignUnsignedShiftRightExpression
 * JD-Core Version:    0.6.2
 */