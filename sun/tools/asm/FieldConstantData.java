/*    */ package sun.tools.asm;
/*    */ 
/*    */ import java.io.DataOutputStream;
/*    */ import java.io.IOException;
/*    */ import sun.tools.java.ClassDefinition;
/*    */ import sun.tools.java.Environment;
/*    */ import sun.tools.java.MemberDefinition;
/*    */ 
/*    */ final class FieldConstantData extends ConstantPoolData
/*    */ {
/*    */   MemberDefinition field;
/*    */   NameAndTypeData nt;
/*    */ 
/*    */   FieldConstantData(ConstantPool paramConstantPool, MemberDefinition paramMemberDefinition)
/*    */   {
/* 48 */     this.field = paramMemberDefinition;
/* 49 */     this.nt = new NameAndTypeData(paramMemberDefinition);
/* 50 */     paramConstantPool.put(paramMemberDefinition.getClassDeclaration());
/* 51 */     paramConstantPool.put(this.nt);
/*    */   }
/*    */ 
/*    */   void write(Environment paramEnvironment, DataOutputStream paramDataOutputStream, ConstantPool paramConstantPool)
/*    */     throws IOException
/*    */   {
/* 58 */     if (this.field.isMethod()) {
/* 59 */       if (this.field.getClassDefinition().isInterface())
/* 60 */         paramDataOutputStream.writeByte(11);
/*    */       else
/* 62 */         paramDataOutputStream.writeByte(10);
/*    */     }
/*    */     else {
/* 65 */       paramDataOutputStream.writeByte(9);
/*    */     }
/* 67 */     paramDataOutputStream.writeShort(paramConstantPool.index(this.field.getClassDeclaration()));
/* 68 */     paramDataOutputStream.writeShort(paramConstantPool.index(this.nt));
/*    */   }
/*    */ 
/*    */   int order()
/*    */   {
/* 75 */     return 2;
/*    */   }
/*    */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     sun.tools.asm.FieldConstantData
 * JD-Core Version:    0.6.2
 */