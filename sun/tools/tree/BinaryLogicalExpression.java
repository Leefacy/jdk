/*    */ package sun.tools.tree;
/*    */ 
/*    */ import java.util.Hashtable;
/*    */ import sun.tools.java.Environment;
/*    */ import sun.tools.java.Type;
/*    */ 
/*    */ public abstract class BinaryLogicalExpression extends BinaryExpression
/*    */ {
/*    */   public BinaryLogicalExpression(int paramInt, long paramLong, Expression paramExpression1, Expression paramExpression2)
/*    */   {
/* 42 */     super(paramInt, paramLong, Type.tBoolean, paramExpression1, paramExpression2);
/*    */   }
/*    */ 
/*    */   public Vset checkValue(Environment paramEnvironment, Context paramContext, Vset paramVset, Hashtable paramHashtable)
/*    */   {
/* 50 */     ConditionVars localConditionVars = new ConditionVars();
/*    */ 
/* 53 */     checkCondition(paramEnvironment, paramContext, paramVset, paramHashtable, localConditionVars);
/*    */ 
/* 55 */     return localConditionVars.vsTrue.join(localConditionVars.vsFalse);
/*    */   }
/*    */ 
/*    */   public abstract void checkCondition(Environment paramEnvironment, Context paramContext, Vset paramVset, Hashtable paramHashtable, ConditionVars paramConditionVars);
/*    */ 
/*    */   public Expression inline(Environment paramEnvironment, Context paramContext)
/*    */   {
/* 71 */     this.left = this.left.inlineValue(paramEnvironment, paramContext);
/* 72 */     this.right = this.right.inlineValue(paramEnvironment, paramContext);
/* 73 */     return this;
/*    */   }
/*    */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     sun.tools.tree.BinaryLogicalExpression
 * JD-Core Version:    0.6.2
 */