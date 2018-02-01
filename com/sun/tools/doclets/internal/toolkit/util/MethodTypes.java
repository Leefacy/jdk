/*    */ package com.sun.tools.doclets.internal.toolkit.util;
/*    */ 
/*    */ public enum MethodTypes
/*    */ {
/* 34 */   ALL(65535, "doclet.All_Methods", "t0", true), 
/* 35 */   STATIC(1, "doclet.Static_Methods", "t1", false), 
/* 36 */   INSTANCE(2, "doclet.Instance_Methods", "t2", false), 
/* 37 */   ABSTRACT(4, "doclet.Abstract_Methods", "t3", false), 
/* 38 */   CONCRETE(8, "doclet.Concrete_Methods", "t4", false), 
/* 39 */   DEFAULT(16, "doclet.Default_Methods", "t5", false), 
/* 40 */   DEPRECATED(32, "doclet.Deprecated_Methods", "t6", false);
/*    */ 
/*    */   private final int value;
/*    */   private final String resourceKey;
/*    */   private final String tabId;
/*    */   private final boolean isDefaultTab;
/*    */ 
/* 48 */   private MethodTypes(int paramInt, String paramString1, String paramString2, boolean paramBoolean) { this.value = paramInt;
/* 49 */     this.resourceKey = paramString1;
/* 50 */     this.tabId = paramString2;
/* 51 */     this.isDefaultTab = paramBoolean; }
/*    */ 
/*    */   public int value()
/*    */   {
/* 55 */     return this.value;
/*    */   }
/*    */ 
/*    */   public String resourceKey() {
/* 59 */     return this.resourceKey;
/*    */   }
/*    */ 
/*    */   public String tabId() {
/* 63 */     return this.tabId;
/*    */   }
/*    */ 
/*    */   public boolean isDefaultTab() {
/* 67 */     return this.isDefaultTab;
/*    */   }
/*    */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.doclets.internal.toolkit.util.MethodTypes
 * JD-Core Version:    0.6.2
 */