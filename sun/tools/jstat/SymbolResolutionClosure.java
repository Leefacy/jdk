/*    */ package sun.tools.jstat;
/*    */ 
/*    */ import java.io.PrintStream;
/*    */ import sun.jvmstat.monitor.MonitorException;
/*    */ 
/*    */ public class SymbolResolutionClosure
/*    */   implements Closure
/*    */ {
/* 40 */   private static final boolean debug = Boolean.getBoolean("SymbolResolutionClosure.debug")
/* 40 */     ;
/*    */   private ExpressionEvaluator ee;
/*    */ 
/*    */   public SymbolResolutionClosure(ExpressionEvaluator paramExpressionEvaluator)
/*    */   {
/* 45 */     this.ee = paramExpressionEvaluator;
/*    */   }
/*    */ 
/*    */   public void visit(Object paramObject, boolean paramBoolean) throws MonitorException {
/* 49 */     if (!(paramObject instanceof ColumnFormat)) {
/* 50 */       return;
/*    */     }
/*    */ 
/* 53 */     ColumnFormat localColumnFormat = (ColumnFormat)paramObject;
/* 54 */     Expression localExpression = localColumnFormat.getExpression();
/* 55 */     String str = localExpression.toString();
/* 56 */     localExpression = (Expression)this.ee.evaluate(localExpression);
/* 57 */     if (debug) {
/* 58 */       System.out.print("Expression: " + str + " resolved to " + localExpression
/* 59 */         .toString());
/*    */     }
/* 61 */     localColumnFormat.setExpression(localExpression);
/*    */   }
/*    */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     sun.tools.jstat.SymbolResolutionClosure
 * JD-Core Version:    0.6.2
 */