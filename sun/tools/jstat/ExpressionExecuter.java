/*    */ package sun.tools.jstat;
/*    */ 
/*    */ import java.io.PrintStream;
/*    */ import java.util.HashMap;
/*    */ import sun.jvmstat.monitor.Monitor;
/*    */ import sun.jvmstat.monitor.MonitoredVm;
/*    */ 
/*    */ public class ExpressionExecuter
/*    */   implements ExpressionEvaluator
/*    */ {
/* 40 */   private static final boolean debug = Boolean.getBoolean("ExpressionEvaluator.debug")
/* 40 */     ;
/*    */   private MonitoredVm vm;
/* 42 */   private HashMap<String, Object> map = new HashMap();
/*    */ 
/*    */   ExpressionExecuter(MonitoredVm paramMonitoredVm) {
/* 45 */     this.vm = paramMonitoredVm;
/*    */   }
/*    */ 
/*    */   public Object evaluate(Expression paramExpression)
/*    */   {
/* 52 */     if (paramExpression == null) {
/* 53 */       return null;
/*    */     }
/*    */ 
/* 56 */     if (debug) {
/* 57 */       System.out.println("Evaluating expression: " + paramExpression);
/*    */     }
/*    */ 
/* 60 */     if ((paramExpression instanceof Literal)) {
/* 61 */       return ((Literal)paramExpression).getValue();
/*    */     }
/*    */ 
/* 64 */     if ((paramExpression instanceof Identifier)) {
/* 65 */       localObject1 = (Identifier)paramExpression;
/* 66 */       if (this.map.containsKey(((Identifier)localObject1).getName())) {
/* 67 */         return this.map.get(((Identifier)localObject1).getName());
/*    */       }
/*    */ 
/* 71 */       localObject2 = (Monitor)((Identifier)localObject1).getValue();
/* 72 */       localObject3 = ((Monitor)localObject2).getValue();
/* 73 */       this.map.put(((Identifier)localObject1).getName(), localObject3);
/* 74 */       return localObject3;
/*    */     }
/*    */ 
/* 78 */     Object localObject1 = paramExpression.getLeft();
/* 79 */     Object localObject2 = paramExpression.getRight();
/*    */ 
/* 81 */     Object localObject3 = paramExpression.getOperator();
/*    */ 
/* 83 */     if (localObject3 == null) {
/* 84 */       return evaluate((Expression)localObject1);
/*    */     }
/* 86 */     Double localDouble1 = new Double(((Number)evaluate((Expression)localObject1)).doubleValue());
/* 87 */     Double localDouble2 = new Double(((Number)evaluate((Expression)localObject2)).doubleValue());
/* 88 */     double d = ((Operator)localObject3).eval(localDouble1.doubleValue(), localDouble2.doubleValue());
/* 89 */     if (debug) {
/* 90 */       System.out.println("Performed Operation: " + localDouble1 + localObject3 + localDouble2 + " = " + d);
/*    */     }
/*    */ 
/* 93 */     return new Double(d);
/*    */   }
/*    */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     sun.tools.jstat.ExpressionExecuter
 * JD-Core Version:    0.6.2
 */