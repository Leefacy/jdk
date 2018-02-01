/*    */ package com.sun.tools.internal.ws.processor.model;
/*    */ 
/*    */ import com.sun.tools.internal.ws.wscompile.ErrorReceiver;
/*    */ 
/*    */ public class Request extends Message
/*    */ {
/*    */   public Request(com.sun.tools.internal.ws.wsdl.document.Message entity, ErrorReceiver receiver)
/*    */   {
/* 38 */     super(entity, receiver);
/*    */   }
/*    */ 
/*    */   public void accept(ModelVisitor visitor) throws Exception {
/* 42 */     visitor.visit(this);
/*    */   }
/*    */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.internal.ws.processor.model.Request
 * JD-Core Version:    0.6.2
 */