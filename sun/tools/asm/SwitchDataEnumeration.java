/*     */ package sun.tools.asm;
/*     */ 
/*     */ import java.util.Arrays;
/*     */ import java.util.Enumeration;
/*     */ import java.util.Hashtable;
/*     */ 
/*     */ class SwitchDataEnumeration
/*     */   implements Enumeration<Integer>
/*     */ {
/*     */   private Integer[] table;
/* 119 */   private int current_index = 0;
/*     */ 
/*     */   SwitchDataEnumeration(Hashtable<Integer, Label> paramHashtable)
/*     */   {
/* 127 */     this.table = new Integer[paramHashtable.size()];
/* 128 */     int i = 0;
/* 129 */     for (Enumeration localEnumeration = paramHashtable.keys(); localEnumeration.hasMoreElements(); ) {
/* 130 */       this.table[(i++)] = ((Integer)localEnumeration.nextElement());
/*     */     }
/* 132 */     Arrays.sort(this.table);
/* 133 */     this.current_index = 0;
/*     */   }
/*     */ 
/*     */   public boolean hasMoreElements()
/*     */   {
/* 140 */     return this.current_index < this.table.length;
/*     */   }
/*     */ 
/*     */   public Integer nextElement()
/*     */   {
/* 147 */     return this.table[(this.current_index++)];
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     sun.tools.asm.SwitchDataEnumeration
 * JD-Core Version:    0.6.2
 */