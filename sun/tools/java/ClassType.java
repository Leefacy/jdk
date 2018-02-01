/*    */ package sun.tools.java;
/*    */ 
/*    */ public final class ClassType extends Type
/*    */ {
/*    */   Identifier className;
/*    */ 
/*    */   ClassType(String paramString, Identifier paramIdentifier)
/*    */   {
/* 50 */     super(10, paramString);
/* 51 */     this.className = paramIdentifier;
/*    */   }
/*    */ 
/*    */   public Identifier getClassName() {
/* 55 */     return this.className;
/*    */   }
/*    */ 
/*    */   public String typeString(String paramString, boolean paramBoolean1, boolean paramBoolean2)
/*    */   {
/* 61 */     String str = (paramBoolean1 ? getClassName().getFlatName() : 
/* 60 */       Identifier.lookup(getClassName().getQualifier(), 
/* 61 */       getClassName().getFlatName())).toString();
/* 62 */     return paramString.length() > 0 ? str + " " + paramString : str;
/*    */   }
/*    */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     sun.tools.java.ClassType
 * JD-Core Version:    0.6.2
 */