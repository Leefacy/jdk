/*    */ package sun.tools.java;
/*    */ 
/*    */ public class AmbiguousClass extends ClassNotFound
/*    */ {
/*    */   public Identifier name1;
/*    */   public Identifier name2;
/*    */ 
/*    */   public AmbiguousClass(Identifier paramIdentifier1, Identifier paramIdentifier2)
/*    */   {
/* 49 */     super(paramIdentifier1.getName());
/* 50 */     this.name1 = paramIdentifier1;
/* 51 */     this.name2 = paramIdentifier2;
/*    */   }
/*    */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     sun.tools.java.AmbiguousClass
 * JD-Core Version:    0.6.2
 */