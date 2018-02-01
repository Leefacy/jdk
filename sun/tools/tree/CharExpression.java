/*    */ package sun.tools.tree;
/*    */ 
/*    */ import java.io.PrintStream;
/*    */ import sun.tools.java.Type;
/*    */ 
/*    */ public class CharExpression extends IntegerExpression
/*    */ {
/*    */   public CharExpression(long paramLong, char paramChar)
/*    */   {
/* 42 */     super(63, paramLong, Type.tChar, paramChar);
/*    */   }
/*    */ 
/*    */   public void print(PrintStream paramPrintStream)
/*    */   {
/* 49 */     paramPrintStream.print(this.value + "c");
/*    */   }
/*    */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     sun.tools.tree.CharExpression
 * JD-Core Version:    0.6.2
 */