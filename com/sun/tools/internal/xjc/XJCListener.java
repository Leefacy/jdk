/*     */ package com.sun.tools.internal.xjc;
/*     */ 
/*     */ import com.sun.tools.internal.xjc.api.ErrorListener;
/*     */ import com.sun.tools.internal.xjc.outline.Outline;
/*     */ 
/*     */ public abstract class XJCListener
/*     */   implements ErrorListener
/*     */ {
/*     */   /** @deprecated */
/*     */   public void generatedFile(String fileName)
/*     */   {
/*     */   }
/*     */ 
/*     */   public void generatedFile(String fileName, int current, int total)
/*     */   {
/*  76 */     generatedFile(fileName);
/*     */   }
/*     */ 
/*     */   public void message(String msg)
/*     */   {
/*     */   }
/*     */ 
/*     */   public void compiled(Outline outline)
/*     */   {
/*     */   }
/*     */ 
/*     */   public boolean isCanceled()
/*     */   {
/* 117 */     return false;
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.internal.xjc.XJCListener
 * JD-Core Version:    0.6.2
 */