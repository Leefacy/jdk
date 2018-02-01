/*     */ package sun.tools.asm;
/*     */ 
/*     */ import java.util.Enumeration;
/*     */ import java.util.Hashtable;
/*     */ 
/*     */ public final class SwitchData
/*     */ {
/*     */   int minValue;
/*     */   int maxValue;
/*  41 */   Label defaultLabel = new Label();
/*  42 */   Hashtable<Integer, Label> tab = new Hashtable();
/*     */ 
/*  44 */   Hashtable<Integer, Long> whereCaseTab = null;
/*     */ 
/*     */   public Label get(int paramInt)
/*     */   {
/*  51 */     return (Label)this.tab.get(Integer.valueOf(paramInt));
/*     */   }
/*     */ 
/*     */   public Label get(Integer paramInteger)
/*     */   {
/*  58 */     return (Label)this.tab.get(paramInteger);
/*     */   }
/*     */ 
/*     */   public void add(int paramInt, Label paramLabel)
/*     */   {
/*  65 */     if (this.tab.size() == 0) {
/*  66 */       this.minValue = paramInt;
/*  67 */       this.maxValue = paramInt;
/*     */     } else {
/*  69 */       if (paramInt < this.minValue) {
/*  70 */         this.minValue = paramInt;
/*     */       }
/*  72 */       if (paramInt > this.maxValue) {
/*  73 */         this.maxValue = paramInt;
/*     */       }
/*     */     }
/*  76 */     this.tab.put(Integer.valueOf(paramInt), paramLabel);
/*     */   }
/*     */ 
/*     */   public Label getDefaultLabel()
/*     */   {
/*  83 */     return this.defaultLabel;
/*     */   }
/*     */ 
/*     */   public synchronized Enumeration<Integer> sortedKeys()
/*     */   {
/*  90 */     return new SwitchDataEnumeration(this.tab);
/*     */   }
/*     */ 
/*     */   public void initTableCase()
/*     */   {
/*  95 */     this.whereCaseTab = new Hashtable();
/*     */   }
/*     */   public void addTableCase(int paramInt, long paramLong) {
/*  98 */     if (this.whereCaseTab != null)
/*  99 */       this.whereCaseTab.put(Integer.valueOf(paramInt), Long.valueOf(paramLong));
/*     */   }
/*     */ 
/*     */   public void addTableDefault(long paramLong)
/*     */   {
/* 104 */     if (this.whereCaseTab != null)
/* 105 */       this.whereCaseTab.put("default", Long.valueOf(paramLong)); 
/*     */   }
/*     */ 
/* 108 */   public long whereCase(Object paramObject) { Long localLong = (Long)this.whereCaseTab.get(paramObject);
/* 109 */     return localLong == null ? 0L : localLong.longValue(); }
/*     */ 
/*     */   public boolean getDefault() {
/* 112 */     return whereCase("default") != 0L;
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     sun.tools.asm.SwitchData
 * JD-Core Version:    0.6.2
 */