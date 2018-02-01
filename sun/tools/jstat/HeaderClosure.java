/*    */ package sun.tools.jstat;
/*    */ 
/*    */ import sun.jvmstat.monitor.MonitorException;
/*    */ 
/*    */ public class HeaderClosure
/*    */   implements Closure
/*    */ {
/*    */   private static final char ALIGN_CHAR = '^';
/* 41 */   private StringBuilder header = new StringBuilder();
/*    */ 
/*    */   public void visit(Object paramObject, boolean paramBoolean)
/*    */     throws MonitorException
/*    */   {
/* 49 */     if (!(paramObject instanceof ColumnFormat)) {
/* 50 */       return;
/*    */     }
/*    */ 
/* 53 */     ColumnFormat localColumnFormat = (ColumnFormat)paramObject;
/*    */ 
/* 55 */     String str = localColumnFormat.getHeader();
/*    */ 
/* 58 */     if (str.indexOf('^') >= 0) {
/* 59 */       int i = str.length();
/* 60 */       if ((str.charAt(0) == '^') && 
/* 61 */         (str
/* 61 */         .charAt(i - 1) == 
/* 61 */         '^'))
/*    */       {
/* 63 */         localColumnFormat.setWidth(Math.max(localColumnFormat.getWidth(), 
/* 64 */           Math.max(localColumnFormat
/* 64 */           .getFormat().length(), i - 2)));
/* 65 */         str = str.substring(1, i - 1);
/* 66 */         str = Alignment.CENTER.align(str, localColumnFormat.getWidth());
/* 67 */       } else if (str.charAt(0) == '^')
/*    */       {
/* 69 */         localColumnFormat.setWidth(Math.max(localColumnFormat.getWidth(), 
/* 70 */           Math.max(localColumnFormat
/* 70 */           .getFormat().length(), i - 1)));
/* 71 */         str = str.substring(1, i);
/* 72 */         str = Alignment.LEFT.align(str, localColumnFormat.getWidth());
/* 73 */       } else if (str.charAt(i - 1) == '^')
/*    */       {
/* 75 */         localColumnFormat.setWidth(Math.max(localColumnFormat.getWidth(), 
/* 76 */           Math.max(localColumnFormat
/* 76 */           .getFormat().length(), i - 1)));
/* 77 */         str = str.substring(0, i - 1);
/* 78 */         str = Alignment.RIGHT.align(str, localColumnFormat.getWidth());
/*    */       }
/*    */ 
/*    */     }
/*    */ 
/* 86 */     this.header.append(str);
/* 87 */     if (paramBoolean)
/* 88 */       this.header.append(" ");
/*    */   }
/*    */ 
/*    */   public String getHeader()
/*    */   {
/* 96 */     return this.header.toString();
/*    */   }
/*    */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     sun.tools.jstat.HeaderClosure
 * JD-Core Version:    0.6.2
 */