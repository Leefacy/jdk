/*    */ package sun.tools.java;
/*    */ 
/*    */ public class ClassNotFound extends Exception
/*    */ {
/*    */   public Identifier name;
/*    */ 
/*    */   public ClassNotFound(Identifier paramIdentifier)
/*    */   {
/* 47 */     super(paramIdentifier.toString());
/* 48 */     this.name = paramIdentifier;
/*    */   }
/*    */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     sun.tools.java.ClassNotFound
 * JD-Core Version:    0.6.2
 */