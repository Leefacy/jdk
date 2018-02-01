/*    */ package com.sun.tools.hat.internal.oql;
/*    */ 
/*    */ class OQLQuery
/*    */ {
/*    */   String selectExpr;
/*    */   boolean isInstanceOf;
/*    */   String className;
/*    */   String identifier;
/*    */   String whereExpr;
/*    */ 
/*    */   OQLQuery(String paramString1, boolean paramBoolean, String paramString2, String paramString3, String paramString4)
/*    */   {
/* 42 */     this.selectExpr = paramString1;
/* 43 */     this.isInstanceOf = paramBoolean;
/* 44 */     this.className = paramString2;
/* 45 */     this.identifier = paramString3;
/* 46 */     this.whereExpr = paramString4;
/*    */   }
/*    */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.hat.internal.oql.OQLQuery
 * JD-Core Version:    0.6.2
 */