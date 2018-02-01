/*    */ package sun.rmi.rmic;
/*    */ 
/*    */ import sun.tools.java.Identifier;
/*    */ 
/*    */ public class Names
/*    */ {
/*    */   public static final Identifier stubFor(Identifier paramIdentifier)
/*    */   {
/* 44 */     return Identifier.lookup(paramIdentifier + "_Stub");
/*    */   }
/*    */ 
/*    */   public static final Identifier skeletonFor(Identifier paramIdentifier)
/*    */   {
/* 51 */     return Identifier.lookup(paramIdentifier + "_Skel");
/*    */   }
/*    */ 
/*    */   public static final Identifier mangleClass(Identifier paramIdentifier)
/*    */   {
/* 71 */     if (!paramIdentifier.isInner()) {
/* 72 */       return paramIdentifier;
/*    */     }
/*    */ 
/* 79 */     Identifier localIdentifier = Identifier.lookup(paramIdentifier
/* 80 */       .getFlatName().toString()
/* 81 */       .replace('.', '$'));
/*    */ 
/* 82 */     if (localIdentifier.isInner()) {
/* 83 */       throw new Error("failed to mangle inner class name");
/*    */     }
/*    */ 
/* 86 */     return Identifier.lookup(paramIdentifier.getQualifier(), localIdentifier);
/*    */   }
/*    */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     sun.rmi.rmic.Names
 * JD-Core Version:    0.6.2
 */