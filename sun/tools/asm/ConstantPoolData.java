/*    */ package sun.tools.asm;
/*    */ 
/*    */ import java.io.DataOutputStream;
/*    */ import java.io.IOException;
/*    */ import sun.tools.java.Environment;
/*    */ import sun.tools.java.RuntimeConstants;
/*    */ 
/*    */ abstract class ConstantPoolData
/*    */   implements RuntimeConstants
/*    */ {
/*    */   int index;
/*    */ 
/*    */   abstract void write(Environment paramEnvironment, DataOutputStream paramDataOutputStream, ConstantPool paramConstantPool)
/*    */     throws IOException;
/*    */ 
/*    */   int order()
/*    */   {
/* 53 */     return 0;
/*    */   }
/*    */ 
/*    */   int width()
/*    */   {
/* 60 */     return 1;
/*    */   }
/*    */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     sun.tools.asm.ConstantPoolData
 * JD-Core Version:    0.6.2
 */