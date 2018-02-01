/*    */ package com.sun.tools.internal.ws.wsdl.document.jaxws;
/*    */ 
/*    */ public class CustomName
/*    */ {
/*    */   private String javaDoc;
/*    */   private String name;
/*    */ 
/*    */   public CustomName()
/*    */   {
/*    */   }
/*    */ 
/*    */   public CustomName(String name, String javaDoc)
/*    */   {
/* 48 */     this.name = name;
/* 49 */     this.javaDoc = javaDoc;
/*    */   }
/*    */ 
/*    */   public String getJavaDoc()
/*    */   {
/* 56 */     return this.javaDoc;
/*    */   }
/*    */ 
/*    */   public void setJavaDoc(String javaDoc)
/*    */   {
/* 62 */     this.javaDoc = javaDoc;
/*    */   }
/*    */ 
/*    */   public String getName()
/*    */   {
/* 68 */     return this.name;
/*    */   }
/*    */ 
/*    */   public void setName(String name)
/*    */   {
/* 74 */     this.name = name;
/*    */   }
/*    */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.internal.ws.wsdl.document.jaxws.CustomName
 * JD-Core Version:    0.6.2
 */