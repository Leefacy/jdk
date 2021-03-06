/*    */ package com.sun.tools.internal.ws.processor.modeler.annotation;
/*    */ 
/*    */ public enum WebServiceConstants
/*    */ {
/* 33 */   SERVICE("Service"), 
/*    */ 
/* 35 */   JAXWS_PACKAGE_PD("jaxws."), 
/*    */ 
/* 37 */   PD_JAXWS_PACKAGE_PD(".jaxws."), 
/*    */ 
/* 39 */   BEAN("Bean"), 
/*    */ 
/* 41 */   FAULT_INFO("faultInfo"), 
/*    */ 
/* 43 */   RESPONSE("Response");
/*    */ 
/*    */   private String value;
/*    */ 
/*    */   private WebServiceConstants(String value) {
/* 48 */     this.value = value;
/*    */   }
/*    */ 
/*    */   public String getValue() {
/* 52 */     return this.value;
/*    */   }
/*    */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.internal.ws.processor.modeler.annotation.WebServiceConstants
 * JD-Core Version:    0.6.2
 */