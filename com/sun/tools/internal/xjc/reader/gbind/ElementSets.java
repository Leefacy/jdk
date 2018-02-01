/*    */ package com.sun.tools.internal.xjc.reader.gbind;
/*    */ 
/*    */ import java.util.LinkedHashSet;
/*    */ 
/*    */ public final class ElementSets
/*    */ {
/*    */   public static ElementSet union(ElementSet lhs, ElementSet rhs)
/*    */   {
/* 42 */     if (lhs.contains(rhs))
/* 43 */       return lhs;
/* 44 */     if (lhs == ElementSet.EMPTY_SET)
/* 45 */       return rhs;
/* 46 */     if (rhs == ElementSet.EMPTY_SET)
/* 47 */       return lhs;
/* 48 */     return new MultiValueSet(lhs, rhs);
/*    */   }
/*    */ 
/*    */   private static final class MultiValueSet extends LinkedHashSet<Element>
/*    */     implements ElementSet
/*    */   {
/*    */     public MultiValueSet(ElementSet lhs, ElementSet rhs)
/*    */     {
/* 58 */       addAll(lhs);
/* 59 */       addAll(rhs);
/*    */ 
/* 62 */       assert (size() > 1);
/*    */     }
/*    */ 
/*    */     private void addAll(ElementSet lhs) {
/* 66 */       if ((lhs instanceof MultiValueSet))
/* 67 */         super.addAll((MultiValueSet)lhs);
/*    */       else
/* 69 */         for (Element e : lhs)
/* 70 */           add(e);
/*    */     }
/*    */ 
/*    */     public boolean contains(ElementSet rhs)
/*    */     {
/* 76 */       return (super.contains(rhs)) || (rhs == ElementSet.EMPTY_SET);
/*    */     }
/*    */ 
/*    */     public void addNext(Element element) {
/* 80 */       for (Element e : this)
/* 81 */         e.addNext(element);
/*    */     }
/*    */   }
/*    */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.internal.xjc.reader.gbind.ElementSets
 * JD-Core Version:    0.6.2
 */