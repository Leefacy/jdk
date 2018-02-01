/*    */ package sun.tools.asm;
/*    */ 
/*    */ import sun.tools.java.MemberDefinition;
/*    */ 
/*    */ final class NameAndTypeData
/*    */ {
/*    */   MemberDefinition field;
/*    */ 
/*    */   NameAndTypeData(MemberDefinition paramMemberDefinition)
/*    */   {
/* 45 */     this.field = paramMemberDefinition;
/*    */   }
/*    */ 
/*    */   public int hashCode()
/*    */   {
/* 52 */     return this.field.getName().hashCode() * this.field.getType().hashCode();
/*    */   }
/*    */ 
/*    */   public boolean equals(Object paramObject)
/*    */   {
/* 59 */     if ((paramObject != null) && ((paramObject instanceof NameAndTypeData))) {
/* 60 */       NameAndTypeData localNameAndTypeData = (NameAndTypeData)paramObject;
/*    */ 
/* 62 */       return (this.field.getName().equals(localNameAndTypeData.field.getName())) && 
/* 62 */         (this.field
/* 62 */         .getType().equals(localNameAndTypeData.field.getType()));
/*    */     }
/* 64 */     return false;
/*    */   }
/*    */ 
/*    */   public String toString()
/*    */   {
/* 71 */     return "%%" + this.field.toString() + "%%";
/*    */   }
/*    */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     sun.tools.asm.NameAndTypeData
 * JD-Core Version:    0.6.2
 */