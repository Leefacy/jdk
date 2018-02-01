/*    */ package sun.tools.javac;
/*    */ 
/*    */ import sun.tools.asm.Assembler;
/*    */ import sun.tools.java.Identifier;
/*    */ import sun.tools.java.MemberDefinition;
/*    */ import sun.tools.java.Type;
/*    */ 
/*    */ @Deprecated
/*    */ final class CompilerMember
/*    */   implements Comparable
/*    */ {
/*    */   MemberDefinition field;
/*    */   Assembler asm;
/*    */   Object value;
/*    */   String name;
/*    */   String sig;
/*    */   String key;
/*    */ 
/*    */   CompilerMember(MemberDefinition paramMemberDefinition, Assembler paramAssembler)
/*    */   {
/* 50 */     this.field = paramMemberDefinition;
/* 51 */     this.asm = paramAssembler;
/* 52 */     this.name = paramMemberDefinition.getName().toString();
/* 53 */     this.sig = paramMemberDefinition.getType().getTypeSignature();
/*    */   }
/*    */ 
/*    */   public int compareTo(Object paramObject) {
/* 57 */     CompilerMember localCompilerMember = (CompilerMember)paramObject;
/* 58 */     return getKey().compareTo(localCompilerMember.getKey());
/*    */   }
/*    */ 
/*    */   String getKey() {
/* 62 */     if (this.key == null)
/* 63 */       this.key = (this.name + this.sig);
/* 64 */     return this.key;
/*    */   }
/*    */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     sun.tools.javac.CompilerMember
 * JD-Core Version:    0.6.2
 */