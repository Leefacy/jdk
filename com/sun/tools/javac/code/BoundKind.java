/*    */ package com.sun.tools.javac.code;
/*    */ 
/*    */ public enum BoundKind
/*    */ {
/* 36 */   EXTENDS("? extends "), 
/* 37 */   SUPER("? super "), 
/* 38 */   UNBOUND("?");
/*    */ 
/*    */   private final String name;
/*    */ 
/*    */   private BoundKind(String paramString) {
/* 43 */     this.name = paramString;
/*    */   }
/*    */   public String toString() {
/* 46 */     return this.name;
/*    */   }
/*    */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.javac.code.BoundKind
 * JD-Core Version:    0.6.2
 */