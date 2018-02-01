/*    */ package sun.tools.asm;
/*    */ 
/*    */ import java.io.DataOutputStream;
/*    */ import java.io.IOException;
/*    */ import sun.tools.java.Environment;
/*    */ 
/*    */ final class StringConstantData extends ConstantPoolData
/*    */ {
/*    */   String str;
/*    */ 
/*    */   StringConstantData(ConstantPool paramConstantPool, String paramString)
/*    */   {
/* 47 */     this.str = paramString;
/*    */   }
/*    */ 
/*    */   void write(Environment paramEnvironment, DataOutputStream paramDataOutputStream, ConstantPool paramConstantPool)
/*    */     throws IOException
/*    */   {
/* 54 */     paramDataOutputStream.writeByte(1);
/* 55 */     paramDataOutputStream.writeUTF(this.str);
/*    */   }
/*    */ 
/*    */   int order()
/*    */   {
/* 62 */     return 4;
/*    */   }
/*    */ 
/*    */   public String toString()
/*    */   {
/* 69 */     return "StringConstantData[" + this.str + "]=" + this.str.hashCode();
/*    */   }
/*    */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     sun.tools.asm.StringConstantData
 * JD-Core Version:    0.6.2
 */