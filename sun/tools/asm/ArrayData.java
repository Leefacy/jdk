/*    */ package sun.tools.asm;
/*    */ 
/*    */ import sun.tools.java.Type;
/*    */ 
/*    */ public final class ArrayData
/*    */ {
/*    */   Type type;
/*    */   int nargs;
/*    */ 
/*    */   public ArrayData(Type paramType, int paramInt)
/*    */   {
/* 41 */     this.type = paramType;
/* 42 */     this.nargs = paramInt;
/*    */   }
/*    */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     sun.tools.asm.ArrayData
 * JD-Core Version:    0.6.2
 */