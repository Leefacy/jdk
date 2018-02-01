/*    */ package sun.tools.tree;
/*    */ 
/*    */ import sun.tools.asm.Assembler;
/*    */ import sun.tools.java.Environment;
/*    */ 
/*    */ public class PreIncExpression extends IncDecExpression
/*    */ {
/*    */   public PreIncExpression(long paramLong, Expression paramExpression)
/*    */   {
/* 42 */     super(39, paramLong, paramExpression);
/*    */   }
/*    */ 
/*    */   public void codeValue(Environment paramEnvironment, Context paramContext, Assembler paramAssembler)
/*    */   {
/* 49 */     codeIncDec(paramEnvironment, paramContext, paramAssembler, true, true, true);
/*    */   }
/*    */   public void code(Environment paramEnvironment, Context paramContext, Assembler paramAssembler) {
/* 52 */     codeIncDec(paramEnvironment, paramContext, paramAssembler, true, true, false);
/*    */   }
/*    */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     sun.tools.tree.PreIncExpression
 * JD-Core Version:    0.6.2
 */