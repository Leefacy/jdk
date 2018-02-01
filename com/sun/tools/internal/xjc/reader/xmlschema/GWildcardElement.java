/*    */ package com.sun.tools.internal.xjc.reader.xmlschema;
/*    */ 
/*    */ import com.sun.xml.internal.xsom.XSWildcard;
/*    */ 
/*    */ final class GWildcardElement extends GElement
/*    */ {
/* 43 */   private boolean strict = true;
/*    */ 
/*    */   public String toString() {
/* 46 */     return "#any";
/*    */   }
/*    */ 
/*    */   String getPropertyNameSeed() {
/* 50 */     return "any";
/*    */   }
/*    */ 
/*    */   public void merge(XSWildcard wc) {
/* 54 */     switch (wc.getMode()) {
/*    */     case 1:
/*    */     case 3:
/* 57 */       this.strict = false;
/*    */     }
/*    */   }
/*    */ 
/*    */   public boolean isStrict() {
/* 62 */     return this.strict;
/*    */   }
/*    */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.internal.xjc.reader.xmlschema.GWildcardElement
 * JD-Core Version:    0.6.2
 */