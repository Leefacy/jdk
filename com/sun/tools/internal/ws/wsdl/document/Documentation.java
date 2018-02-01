/*    */ package com.sun.tools.internal.ws.wsdl.document;
/*    */ 
/*    */ public class Documentation
/*    */ {
/*    */   private String content;
/*    */ 
/*    */   public Documentation(String s)
/*    */   {
/* 36 */     this.content = s;
/*    */   }
/*    */ 
/*    */   public String getContent() {
/* 40 */     return this.content;
/*    */   }
/*    */ 
/*    */   public void setContent(String s) {
/* 44 */     this.content = s;
/*    */   }
/*    */ 
/*    */   public void accept(WSDLDocumentVisitor visitor) throws Exception {
/* 48 */     visitor.visit(this);
/*    */   }
/*    */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.internal.ws.wsdl.document.Documentation
 * JD-Core Version:    0.6.2
 */