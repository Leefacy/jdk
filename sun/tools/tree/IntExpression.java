/*    */ package sun.tools.tree;
/*    */ 
/*    */ import java.io.PrintStream;
/*    */ import sun.tools.java.Type;
/*    */ 
/*    */ public class IntExpression extends IntegerExpression
/*    */ {
/*    */   public IntExpression(long paramLong, int paramInt)
/*    */   {
/* 42 */     super(65, paramLong, Type.tInt, paramInt);
/*    */   }
/*    */ 
/*    */   public boolean equals(Object paramObject)
/*    */   {
/* 50 */     if ((paramObject != null) && ((paramObject instanceof IntExpression))) {
/* 51 */       return this.value == ((IntExpression)paramObject).value;
/*    */     }
/* 53 */     return false;
/*    */   }
/*    */ 
/*    */   public int hashCode()
/*    */   {
/* 61 */     return this.value;
/*    */   }
/*    */ 
/*    */   public void print(PrintStream paramPrintStream)
/*    */   {
/* 68 */     paramPrintStream.print(this.value);
/*    */   }
/*    */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     sun.tools.tree.IntExpression
 * JD-Core Version:    0.6.2
 */