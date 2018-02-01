/*    */ package sun.tools.tree;
/*    */ 
/*    */ import java.io.PrintStream;
/*    */ import sun.tools.java.Type;
/*    */ 
/*    */ public class ShortExpression extends IntegerExpression
/*    */ {
/*    */   public ShortExpression(long paramLong, short paramShort)
/*    */   {
/* 42 */     super(64, paramLong, Type.tShort, paramShort);
/*    */   }
/*    */ 
/*    */   public void print(PrintStream paramPrintStream)
/*    */   {
/* 49 */     paramPrintStream.print(this.value + "s");
/*    */   }
/*    */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     sun.tools.tree.ShortExpression
 * JD-Core Version:    0.6.2
 */