/*    */ package com.sun.tools.internal.jxc.ap;
/*    */ 
/*    */ public enum Const
/*    */ {
/* 43 */   CONFIG_FILE_OPTION("jaxb.config"), 
/*    */ 
/* 45 */   DEBUG_OPTION("jaxb.debug");
/*    */ 
/*    */   private String value;
/*    */ 
/*    */   private Const(String value) {
/* 50 */     this.value = value;
/*    */   }
/*    */ 
/*    */   public String getValue() {
/* 54 */     return this.value;
/*    */   }
/*    */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.internal.jxc.ap.Const
 * JD-Core Version:    0.6.2
 */