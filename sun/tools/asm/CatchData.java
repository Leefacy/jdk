/*    */ package sun.tools.asm;
/*    */ 
/*    */ public final class CatchData
/*    */ {
/*    */   Object type;
/*    */   Label label;
/*    */ 
/*    */   CatchData(Object paramObject)
/*    */   {
/* 45 */     this.type = paramObject;
/* 46 */     this.label = new Label();
/*    */   }
/*    */ 
/*    */   public Label getLabel()
/*    */   {
/* 53 */     return this.label;
/*    */   }
/*    */ 
/*    */   public Object getType()
/*    */   {
/* 60 */     return this.type;
/*    */   }
/*    */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     sun.tools.asm.CatchData
 * JD-Core Version:    0.6.2
 */