/*    */ package com.sun.xml.internal.xsom.impl.scd;
/*    */ 
/*    */ import com.sun.xml.internal.xsom.SCD;
/*    */ import com.sun.xml.internal.xsom.XSComponent;
/*    */ import java.util.Iterator;
/*    */ 
/*    */ public final class SCDImpl extends SCD
/*    */ {
/*    */   private final Step[] steps;
/*    */   private final String text;
/*    */ 
/*    */   public SCDImpl(String text, Step[] steps)
/*    */   {
/* 50 */     this.text = text;
/* 51 */     this.steps = steps;
/*    */   }
/*    */ 
/*    */   public Iterator<XSComponent> select(Iterator<? extends XSComponent> contextNode) {
/* 55 */     Iterator nodeSet = contextNode;
/*    */ 
/* 57 */     int len = this.steps.length;
/* 58 */     for (int i = 0; i < len; i++) {
/* 59 */       if ((i != 0) && (i != len - 1) && (!this.steps[(i - 1)].axis.isModelGroup()) && (this.steps[i].axis.isModelGroup()))
/*    */       {
/* 65 */         nodeSet = new Iterators.Unique(new Iterators.Map(nodeSet)
/*    */         {
/*    */           protected Iterator<XSComponent> apply(XSComponent u)
/*    */           {
/* 70 */             return new Iterators.Union(
/* 69 */               Iterators.singleton(u), 
/* 69 */               Axis.INTERMEDIATE_SKIP
/* 70 */               .iterator(u));
/*    */           }
/*    */ 
/*    */         });
/*    */       }
/*    */ 
/* 75 */       nodeSet = this.steps[i].evaluate(nodeSet);
/*    */     }
/*    */ 
/* 78 */     return nodeSet;
/*    */   }
/*    */ 
/*    */   public String toString() {
/* 82 */     return this.text;
/*    */   }
/*    */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.xml.internal.xsom.impl.scd.SCDImpl
 * JD-Core Version:    0.6.2
 */