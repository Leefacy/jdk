/*    */ package com.sun.tools.internal.xjc.api;
/*    */ 
/*    */ public enum SpecVersion
/*    */ {
/* 34 */   V2_0, V2_1, V2_2;
/*    */ 
/* 58 */   public static final SpecVersion LATEST = V2_2;
/*    */ 
/*    */   public boolean isLaterThan(SpecVersion t)
/*    */   {
/* 40 */     return ordinal() >= t.ordinal();
/*    */   }
/*    */ 
/*    */   public static SpecVersion parse(String token)
/*    */   {
/* 49 */     if (token.equals("2.0"))
/* 50 */       return V2_0;
/* 51 */     if (token.equals("2.1"))
/* 52 */       return V2_1;
/* 53 */     if (token.equals("2.2"))
/* 54 */       return V2_2;
/* 55 */     return null;
/*    */   }
/*    */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.internal.xjc.api.SpecVersion
 * JD-Core Version:    0.6.2
 */