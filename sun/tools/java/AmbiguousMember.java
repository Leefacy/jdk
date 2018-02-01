/*    */ package sun.tools.java;
/*    */ 
/*    */ public class AmbiguousMember extends Exception
/*    */ {
/*    */   public MemberDefinition field1;
/*    */   public MemberDefinition field2;
/*    */ 
/*    */   public AmbiguousMember(MemberDefinition paramMemberDefinition1, MemberDefinition paramMemberDefinition2)
/*    */   {
/* 50 */     super(paramMemberDefinition1.getName() + " + " + paramMemberDefinition2.getName());
/* 51 */     this.field1 = paramMemberDefinition1;
/* 52 */     this.field2 = paramMemberDefinition2;
/*    */   }
/*    */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     sun.tools.java.AmbiguousMember
 * JD-Core Version:    0.6.2
 */