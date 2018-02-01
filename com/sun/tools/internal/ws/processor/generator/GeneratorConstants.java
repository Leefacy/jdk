/*    */ package com.sun.tools.internal.ws.processor.generator;
/*    */ 
/*    */ public enum GeneratorConstants
/*    */ {
/* 33 */   DOTC("."), 
/*    */ 
/* 35 */   SIG_INNERCLASS("$"), 
/*    */ 
/* 37 */   JAVA_SRC_SUFFIX(".java"), 
/*    */ 
/* 39 */   QNAME_SUFFIX("_QNAME"), 
/*    */ 
/* 41 */   GET("get"), 
/*    */ 
/* 43 */   IS("is"), 
/*    */ 
/* 45 */   RESPONSE("Response"), 
/*    */ 
/* 47 */   FAULT_CLASS_MEMBER_NAME("faultInfo");
/*    */ 
/*    */   private String value;
/*    */ 
/*    */   private GeneratorConstants(String value) {
/* 52 */     this.value = value;
/*    */   }
/*    */ 
/*    */   public String getValue() {
/* 56 */     return this.value;
/*    */   }
/*    */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.internal.ws.processor.generator.GeneratorConstants
 * JD-Core Version:    0.6.2
 */