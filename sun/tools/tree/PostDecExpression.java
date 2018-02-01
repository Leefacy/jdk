/*    */ package sun.tools.tree;
/*    */ 
/*    */ import sun.tools.asm.Assembler;
/*    */ import sun.tools.java.Environment;
/*    */ 
/*    */ public class PostDecExpression extends IncDecExpression
/*    */ {
/*    */   public PostDecExpression(long paramLong, Expression paramExpression)
/*    */   {
/* 42 */     super(45, paramLong, paramExpression);
/*    */   }
/*    */ 
/*    */   public void codeValue(Environment paramEnvironment, Context paramContext, Assembler paramAssembler)
/*    */   {
/* 49 */     codeIncDec(paramEnvironment, paramContext, paramAssembler, false, false, true);
/*    */   }
/*    */   public void code(Environment paramEnvironment, Context paramContext, Assembler paramAssembler) {
/* 52 */     codeIncDec(paramEnvironment, paramContext, paramAssembler, false, false, false);
/*    */   }
/*    */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     sun.tools.tree.PostDecExpression
 * JD-Core Version:    0.6.2
 */