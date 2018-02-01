/*    */ package sun.tools.asm;
/*    */ 
/*    */ import sun.tools.java.MemberDefinition;
/*    */ 
/*    */ public final class LocalVariable
/*    */ {
/*    */   MemberDefinition field;
/*    */   int slot;
/*    */   int from;
/*    */   int to;
/*    */ 
/*    */   public LocalVariable(MemberDefinition paramMemberDefinition, int paramInt)
/*    */   {
/* 48 */     if (paramMemberDefinition == null) {
/* 49 */       new Exception().printStackTrace();
/*    */     }
/* 51 */     this.field = paramMemberDefinition;
/* 52 */     this.slot = paramInt;
/* 53 */     this.to = -1;
/*    */   }
/*    */ 
/*    */   LocalVariable(MemberDefinition paramMemberDefinition, int paramInt1, int paramInt2, int paramInt3) {
/* 57 */     this.field = paramMemberDefinition;
/* 58 */     this.slot = paramInt1;
/* 59 */     this.from = paramInt2;
/* 60 */     this.to = paramInt3;
/*    */   }
/*    */ 
/*    */   public String toString() {
/* 64 */     return this.field + "/" + this.slot;
/*    */   }
/*    */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     sun.tools.asm.LocalVariable
 * JD-Core Version:    0.6.2
 */