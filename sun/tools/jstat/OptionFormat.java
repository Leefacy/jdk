/*     */ package sun.tools.jstat;
/*     */ 
/*     */ import java.io.PrintStream;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import sun.jvmstat.monitor.MonitorException;
/*     */ 
/*     */ public class OptionFormat
/*     */ {
/*     */   protected String name;
/*     */   protected List<OptionFormat> children;
/*     */ 
/*     */   public OptionFormat(String paramString)
/*     */   {
/*  43 */     this.name = paramString;
/*  44 */     this.children = new ArrayList();
/*     */   }
/*     */ 
/*     */   public boolean equals(Object paramObject) {
/*  48 */     if (paramObject == this) {
/*  49 */       return true;
/*     */     }
/*  51 */     if (!(paramObject instanceof OptionFormat)) {
/*  52 */       return false;
/*     */     }
/*  54 */     OptionFormat localOptionFormat = (OptionFormat)paramObject;
/*  55 */     return this.name.compareTo(localOptionFormat.name) == 0;
/*     */   }
/*     */ 
/*     */   public int hashCode() {
/*  59 */     return this.name.hashCode();
/*     */   }
/*     */ 
/*     */   public void addSubFormat(OptionFormat paramOptionFormat) {
/*  63 */     this.children.add(paramOptionFormat);
/*     */   }
/*     */ 
/*     */   public OptionFormat getSubFormat(int paramInt) {
/*  67 */     return (OptionFormat)this.children.get(paramInt);
/*     */   }
/*     */ 
/*     */   public void insertSubFormat(int paramInt, OptionFormat paramOptionFormat) {
/*  71 */     this.children.add(paramInt, paramOptionFormat);
/*     */   }
/*     */ 
/*     */   public String getName() {
/*  75 */     return this.name;
/*     */   }
/*     */ 
/*     */   public void apply(Closure paramClosure) throws MonitorException
/*     */   {
/*  80 */     for (Iterator localIterator = this.children.iterator(); localIterator.hasNext(); ) {
/*  81 */       localOptionFormat = (OptionFormat)localIterator.next();
/*  82 */       paramClosure.visit(localOptionFormat, localIterator.hasNext());
/*     */     }
/*     */     OptionFormat localOptionFormat;
/*  85 */     for (localIterator = this.children.iterator(); localIterator.hasNext(); ) {
/*  86 */       localOptionFormat = (OptionFormat)localIterator.next();
/*  87 */       localOptionFormat.apply(paramClosure);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void printFormat() {
/*  92 */     printFormat(0);
/*     */   }
/*     */ 
/*     */   public void printFormat(int paramInt) {
/*  96 */     String str = "  ";
/*  97 */     StringBuilder localStringBuilder = new StringBuilder("");
/*     */ 
/*  99 */     for (int i = 0; i < paramInt; i++) {
/* 100 */       localStringBuilder.append(str);
/*     */     }
/* 102 */     System.out.println(localStringBuilder + this.name + " {");
/*     */ 
/* 105 */     for (OptionFormat localOptionFormat : this.children) {
/* 106 */       localOptionFormat.printFormat(paramInt + 1);
/*     */     }
/* 108 */     System.out.println(localStringBuilder + "}");
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     sun.tools.jstat.OptionFormat
 * JD-Core Version:    0.6.2
 */