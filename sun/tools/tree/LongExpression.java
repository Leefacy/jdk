/*    */ package sun.tools.tree;
/*    */ 
/*    */ import java.io.PrintStream;
/*    */ import sun.tools.asm.Assembler;
/*    */ import sun.tools.java.Environment;
/*    */ import sun.tools.java.Type;
/*    */ 
/*    */ public class LongExpression extends ConstantExpression
/*    */ {
/*    */   long value;
/*    */ 
/*    */   public LongExpression(long paramLong1, long paramLong2)
/*    */   {
/* 45 */     super(66, paramLong1, Type.tLong);
/* 46 */     this.value = paramLong2;
/*    */   }
/*    */ 
/*    */   public Object getValue()
/*    */   {
/* 53 */     return new Long(this.value);
/*    */   }
/*    */ 
/*    */   public boolean equals(int paramInt)
/*    */   {
/* 60 */     return this.value == paramInt;
/*    */   }
/*    */ 
/*    */   public boolean equalsDefault()
/*    */   {
/* 67 */     return this.value == 0L;
/*    */   }
/*    */ 
/*    */   public void codeValue(Environment paramEnvironment, Context paramContext, Assembler paramAssembler)
/*    */   {
/* 74 */     paramAssembler.add(this.where, 20, new Long(this.value));
/*    */   }
/*    */ 
/*    */   public void print(PrintStream paramPrintStream)
/*    */   {
/* 81 */     paramPrintStream.print(this.value + "L");
/*    */   }
/*    */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     sun.tools.tree.LongExpression
 * JD-Core Version:    0.6.2
 */