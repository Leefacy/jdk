/*    */ package sun.tools.tree;
/*    */ 
/*    */ import java.io.PrintStream;
/*    */ import sun.tools.asm.Assembler;
/*    */ import sun.tools.java.Environment;
/*    */ import sun.tools.java.Type;
/*    */ 
/*    */ public class NullExpression extends ConstantExpression
/*    */ {
/*    */   public NullExpression(long paramLong)
/*    */   {
/* 43 */     super(84, paramLong, Type.tNull);
/*    */   }
/*    */ 
/*    */   public boolean equals(int paramInt)
/*    */   {
/* 50 */     return paramInt == 0;
/*    */   }
/*    */ 
/*    */   public boolean isNull() {
/* 54 */     return true;
/*    */   }
/*    */ 
/*    */   public void codeValue(Environment paramEnvironment, Context paramContext, Assembler paramAssembler)
/*    */   {
/* 61 */     paramAssembler.add(this.where, 1);
/*    */   }
/*    */ 
/*    */   public void print(PrintStream paramPrintStream)
/*    */   {
/* 68 */     paramPrintStream.print("null");
/*    */   }
/*    */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     sun.tools.tree.NullExpression
 * JD-Core Version:    0.6.2
 */