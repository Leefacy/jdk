/*    */ package sun.tools.asm;
/*    */ 
/*    */ import java.util.Vector;
/*    */ 
/*    */ public final class TryData
/*    */ {
/* 38 */   Vector<CatchData> catches = new Vector();
/* 39 */   Label endLabel = new Label();
/*    */ 
/*    */   public CatchData add(Object paramObject)
/*    */   {
/* 45 */     CatchData localCatchData = new CatchData(paramObject);
/* 46 */     this.catches.addElement(localCatchData);
/* 47 */     return localCatchData;
/*    */   }
/*    */ 
/*    */   public CatchData getCatch(int paramInt)
/*    */   {
/* 54 */     return (CatchData)this.catches.elementAt(paramInt);
/*    */   }
/*    */ 
/*    */   public Label getEndLabel()
/*    */   {
/* 61 */     return this.endLabel;
/*    */   }
/*    */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     sun.tools.asm.TryData
 * JD-Core Version:    0.6.2
 */