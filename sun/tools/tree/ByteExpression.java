/*    */ package sun.tools.tree;
/*    */ 
/*    */ import java.io.PrintStream;
/*    */ import sun.tools.java.Type;
/*    */ 
/*    */ public class ByteExpression extends IntegerExpression
/*    */ {
/*    */   public ByteExpression(long paramLong, byte paramByte)
/*    */   {
/* 42 */     super(62, paramLong, Type.tByte, paramByte);
/*    */   }
/*    */ 
/*    */   public void print(PrintStream paramPrintStream)
/*    */   {
/* 49 */     paramPrintStream.print(this.value + "b");
/*    */   }
/*    */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     sun.tools.tree.ByteExpression
 * JD-Core Version:    0.6.2
 */