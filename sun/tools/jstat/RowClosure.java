/*    */ package sun.tools.jstat;
/*    */ 
/*    */ import java.text.DecimalFormat;
/*    */ import java.text.DecimalFormatSymbols;
/*    */ import sun.jvmstat.monitor.MonitorException;
/*    */ import sun.jvmstat.monitor.MonitoredVm;
/*    */ 
/*    */ public class RowClosure
/*    */   implements Closure
/*    */ {
/*    */   private MonitoredVm vm;
/* 41 */   private StringBuilder row = new StringBuilder();
/*    */ 
/*    */   public RowClosure(MonitoredVm paramMonitoredVm) {
/* 44 */     this.vm = paramMonitoredVm;
/*    */   }
/*    */ 
/*    */   public void visit(Object paramObject, boolean paramBoolean) throws MonitorException {
/* 48 */     if (!(paramObject instanceof ColumnFormat)) {
/* 49 */       return;
/*    */     }
/*    */ 
/* 52 */     ColumnFormat localColumnFormat = (ColumnFormat)paramObject;
/* 53 */     String str = null;
/*    */ 
/* 55 */     Expression localExpression = localColumnFormat.getExpression();
/* 56 */     ExpressionExecuter localExpressionExecuter = new ExpressionExecuter(this.vm);
/* 57 */     Object localObject = localExpressionExecuter.evaluate(localExpression);
/*    */ 
/* 59 */     if ((localObject instanceof String)) {
/* 60 */       str = (String)localObject;
/* 61 */     } else if ((localObject instanceof Number)) {
/* 62 */       double d1 = ((Number)localObject).doubleValue();
/* 63 */       double d2 = localColumnFormat.getScale().scale(d1);
/* 64 */       DecimalFormat localDecimalFormat = new DecimalFormat(localColumnFormat.getFormat());
/* 65 */       DecimalFormatSymbols localDecimalFormatSymbols = localDecimalFormat.getDecimalFormatSymbols();
/* 66 */       localDecimalFormatSymbols.setNaN("-");
/* 67 */       localDecimalFormat.setDecimalFormatSymbols(localDecimalFormatSymbols);
/* 68 */       str = localDecimalFormat.format(d2);
/*    */     }
/*    */ 
/* 71 */     localColumnFormat.setPreviousValue(localObject);
/* 72 */     str = localColumnFormat.getAlignment().align(str, localColumnFormat.getWidth());
/* 73 */     this.row.append(str);
/* 74 */     if (paramBoolean)
/* 75 */       this.row.append(" ");
/*    */   }
/*    */ 
/*    */   public String getRow()
/*    */   {
/* 80 */     return this.row.toString();
/*    */   }
/*    */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     sun.tools.jstat.RowClosure
 * JD-Core Version:    0.6.2
 */