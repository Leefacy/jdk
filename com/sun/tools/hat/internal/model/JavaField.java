/*    */ package com.sun.tools.hat.internal.model;
/*    */ 
/*    */ public class JavaField
/*    */ {
/*    */   private String name;
/*    */   private String signature;
/*    */ 
/*    */   public JavaField(String paramString1, String paramString2)
/*    */   {
/* 47 */     this.name = paramString1;
/* 48 */     this.signature = paramString2;
/*    */   }
/*    */ 
/*    */   public boolean hasId()
/*    */   {
/* 57 */     int i = this.signature.charAt(0);
/* 58 */     return (i == 91) || (i == 76);
/*    */   }
/*    */ 
/*    */   public String getName() {
/* 62 */     return this.name;
/*    */   }
/*    */ 
/*    */   public String getSignature() {
/* 66 */     return this.signature;
/*    */   }
/*    */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.hat.internal.model.JavaField
 * JD-Core Version:    0.6.2
 */