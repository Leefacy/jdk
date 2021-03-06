/*    */ package sun.tools.tree;
/*    */ 
/*    */ import java.io.PrintStream;
/*    */ import sun.tools.asm.Assembler;
/*    */ import sun.tools.java.Environment;
/*    */ import sun.tools.java.Type;
/*    */ 
/*    */ public class FloatExpression extends ConstantExpression
/*    */ {
/*    */   float value;
/*    */ 
/*    */   public FloatExpression(long paramLong, float paramFloat)
/*    */   {
/* 45 */     super(67, paramLong, Type.tFloat);
/* 46 */     this.value = paramFloat;
/*    */   }
/*    */ 
/*    */   public Object getValue()
/*    */   {
/* 53 */     return new Float(this.value);
/*    */   }
/*    */ 
/*    */   public boolean equals(int paramInt)
/*    */   {
/* 60 */     return this.value == paramInt;
/*    */   }
/*    */ 
/*    */   public boolean equalsDefault()
/*    */   {
/* 68 */     return Float.floatToIntBits(this.value) == 0;
/*    */   }
/*    */ 
/*    */   public void codeValue(Environment paramEnvironment, Context paramContext, Assembler paramAssembler)
/*    */   {
/* 75 */     paramAssembler.add(this.where, 18, new Float(this.value));
/*    */   }
/*    */ 
/*    */   public void print(PrintStream paramPrintStream)
/*    */   {
/* 82 */     paramPrintStream.print(this.value + "F");
/*    */   }
/*    */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     sun.tools.tree.FloatExpression
 * JD-Core Version:    0.6.2
 */