/*    */ package com.sun.xml.internal.rngom.binary;
/*    */ 
/*    */ import com.sun.xml.internal.rngom.nc.NameClass;
/*    */ import java.util.ArrayList;
/*    */ import java.util.List;
/*    */ 
/*    */ class DuplicateAttributeDetector
/*    */ {
/* 54 */   private List nameClasses = new ArrayList();
/* 55 */   private Alternative alternatives = null;
/*    */ 
/*    */   boolean addAttribute(NameClass nc)
/*    */   {
/* 70 */     int lim = this.nameClasses.size();
/* 71 */     for (Alternative a = this.alternatives; a != null; a = a.parent) {
/* 72 */       for (int i = a.endIndex; i < lim; i++)
/* 73 */         if (nc.hasOverlapWith((NameClass)this.nameClasses.get(i)))
/* 74 */           return false;
/* 75 */       lim = a.startIndex;
/*    */     }
/* 77 */     for (int i = 0; i < lim; i++)
/* 78 */       if (nc.hasOverlapWith((NameClass)this.nameClasses.get(i)))
/* 79 */         return false;
/* 80 */     this.nameClasses.add(nc);
/* 81 */     return true;
/*    */   }
/*    */ 
/*    */   void startChoice() {
/* 85 */     this.alternatives = new Alternative(this.nameClasses.size(), this.alternatives, null);
/*    */   }
/*    */ 
/*    */   void alternative() {
/* 89 */     this.alternatives.endIndex = this.nameClasses.size();
/*    */   }
/*    */ 
/*    */   void endChoice() {
/* 93 */     this.alternatives = this.alternatives.parent;
/*    */   }
/*    */ 
/*    */   private static class Alternative
/*    */   {
/*    */     private int startIndex;
/*    */     private int endIndex;
/*    */     private Alternative parent;
/*    */ 
/*    */     private Alternative(int startIndex, Alternative parent)
/*    */     {
/* 63 */       this.startIndex = startIndex;
/* 64 */       this.endIndex = startIndex;
/* 65 */       this.parent = parent;
/*    */     }
/*    */   }
/*    */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.xml.internal.rngom.binary.DuplicateAttributeDetector
 * JD-Core Version:    0.6.2
 */