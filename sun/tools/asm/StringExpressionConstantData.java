/*    */ package sun.tools.asm;
/*    */ 
/*    */ import java.io.DataOutputStream;
/*    */ import java.io.IOException;
/*    */ import sun.tools.java.Environment;
/*    */ import sun.tools.tree.StringExpression;
/*    */ 
/*    */ final class StringExpressionConstantData extends ConstantPoolData
/*    */ {
/*    */   StringExpression str;
/*    */ 
/*    */   StringExpressionConstantData(ConstantPool paramConstantPool, StringExpression paramStringExpression)
/*    */   {
/* 49 */     this.str = paramStringExpression;
/* 50 */     paramConstantPool.put(paramStringExpression.getValue());
/*    */   }
/*    */ 
/*    */   void write(Environment paramEnvironment, DataOutputStream paramDataOutputStream, ConstantPool paramConstantPool)
/*    */     throws IOException
/*    */   {
/* 57 */     paramDataOutputStream.writeByte(8);
/* 58 */     paramDataOutputStream.writeShort(paramConstantPool.index(this.str.getValue()));
/*    */   }
/*    */ 
/*    */   int order()
/*    */   {
/* 65 */     return 0;
/*    */   }
/*    */ 
/*    */   public String toString()
/*    */   {
/* 72 */     return "StringExpressionConstantData[" + this.str.getValue() + "]=" + this.str.getValue().hashCode();
/*    */   }
/*    */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     sun.tools.asm.StringExpressionConstantData
 * JD-Core Version:    0.6.2
 */