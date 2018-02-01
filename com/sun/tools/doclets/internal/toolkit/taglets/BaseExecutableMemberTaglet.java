/*    */ package com.sun.tools.doclets.internal.toolkit.taglets;
/*    */ 
/*    */ public abstract class BaseExecutableMemberTaglet extends BaseTaglet
/*    */ {
/*    */   public boolean inField()
/*    */   {
/* 50 */     return false;
/*    */   }
/*    */ 
/*    */   public boolean inOverview()
/*    */   {
/* 61 */     return false;
/*    */   }
/*    */ 
/*    */   public boolean inPackage()
/*    */   {
/* 72 */     return false;
/*    */   }
/*    */ 
/*    */   public boolean inType()
/*    */   {
/* 83 */     return false;
/*    */   }
/*    */ 
/*    */   public boolean isInlineTag()
/*    */   {
/* 93 */     return false;
/*    */   }
/*    */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.doclets.internal.toolkit.taglets.BaseExecutableMemberTaglet
 * JD-Core Version:    0.6.2
 */