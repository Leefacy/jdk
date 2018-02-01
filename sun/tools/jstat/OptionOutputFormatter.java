/*    */ package sun.tools.jstat;
/*    */ 
/*    */ import sun.jvmstat.monitor.MonitorException;
/*    */ import sun.jvmstat.monitor.MonitoredVm;
/*    */ 
/*    */ public class OptionOutputFormatter
/*    */   implements OutputFormatter
/*    */ {
/*    */   private OptionFormat format;
/*    */   private String header;
/*    */   private MonitoredVm vm;
/*    */ 
/*    */   public OptionOutputFormatter(MonitoredVm paramMonitoredVm, OptionFormat paramOptionFormat)
/*    */     throws MonitorException
/*    */   {
/* 44 */     this.vm = paramMonitoredVm;
/* 45 */     this.format = paramOptionFormat;
/* 46 */     resolve();
/*    */   }
/*    */ 
/*    */   private void resolve() throws MonitorException {
/* 50 */     ExpressionResolver localExpressionResolver = new ExpressionResolver(this.vm);
/* 51 */     SymbolResolutionClosure localSymbolResolutionClosure = new SymbolResolutionClosure(localExpressionResolver);
/* 52 */     this.format.apply(localSymbolResolutionClosure);
/*    */   }
/*    */ 
/*    */   public String getHeader() throws MonitorException {
/* 56 */     if (this.header == null) {
/* 57 */       HeaderClosure localHeaderClosure = new HeaderClosure();
/* 58 */       this.format.apply(localHeaderClosure);
/* 59 */       this.header = localHeaderClosure.getHeader();
/*    */     }
/* 61 */     return this.header;
/*    */   }
/*    */ 
/*    */   public String getRow() throws MonitorException {
/* 65 */     RowClosure localRowClosure = new RowClosure(this.vm);
/* 66 */     this.format.apply(localRowClosure);
/* 67 */     return localRowClosure.getRow();
/*    */   }
/*    */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     sun.tools.jstat.OptionOutputFormatter
 * JD-Core Version:    0.6.2
 */