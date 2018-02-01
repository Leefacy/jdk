/*    */ package sun.tools.tree;
/*    */ 
/*    */ public class CheckContext extends Context
/*    */ {
/* 37 */   public Vset vsBreak = Vset.DEAD_END;
/* 38 */   public Vset vsContinue = Vset.DEAD_END;
/*    */ 
/* 44 */   public Vset vsTryExit = Vset.DEAD_END;
/*    */ 
/*    */   CheckContext(Context paramContext, Statement paramStatement)
/*    */   {
/* 50 */     super(paramContext, paramStatement);
/*    */   }
/*    */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     sun.tools.tree.CheckContext
 * JD-Core Version:    0.6.2
 */