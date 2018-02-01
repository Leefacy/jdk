/*    */ package sun.tools.jstat;
/*    */ 
/*    */ import java.util.Iterator;
/*    */ import java.util.List;
/*    */ import sun.jvmstat.monitor.Monitor;
/*    */ import sun.jvmstat.monitor.MonitorException;
/*    */ import sun.jvmstat.monitor.StringMonitor;
/*    */ 
/*    */ public class RawOutputFormatter
/*    */   implements OutputFormatter
/*    */ {
/*    */   private List logged;
/*    */   private String header;
/*    */   private boolean printStrings;
/*    */ 
/*    */   public RawOutputFormatter(List paramList, boolean paramBoolean)
/*    */   {
/* 43 */     this.logged = paramList;
/* 44 */     this.printStrings = paramBoolean;
/*    */   }
/*    */ 
/*    */   public String getHeader() throws MonitorException {
/* 48 */     if (this.header == null)
/*    */     {
/* 50 */       StringBuilder localStringBuilder = new StringBuilder();
/* 51 */       for (Iterator localIterator = this.logged.iterator(); localIterator.hasNext(); ) {
/* 52 */         Monitor localMonitor = (Monitor)localIterator.next();
/* 53 */         localStringBuilder.append(localMonitor.getName() + " ");
/*    */       }
/* 55 */       this.header = localStringBuilder.toString();
/*    */     }
/* 57 */     return this.header;
/*    */   }
/*    */ 
/*    */   public String getRow() throws MonitorException {
/* 61 */     StringBuilder localStringBuilder = new StringBuilder();
/* 62 */     int i = 0;
/* 63 */     for (Iterator localIterator = this.logged.iterator(); localIterator.hasNext(); ) {
/* 64 */       Monitor localMonitor = (Monitor)localIterator.next();
/* 65 */       if (i++ > 0) {
/* 66 */         localStringBuilder.append(" ");
/*    */       }
/* 68 */       if ((this.printStrings) && ((localMonitor instanceof StringMonitor)))
/* 69 */         localStringBuilder.append("\"").append(localMonitor.getValue()).append("\"");
/*    */       else {
/* 71 */         localStringBuilder.append(localMonitor.getValue());
/*    */       }
/*    */     }
/* 74 */     return localStringBuilder.toString();
/*    */   }
/*    */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     sun.tools.jstat.RawOutputFormatter
 * JD-Core Version:    0.6.2
 */