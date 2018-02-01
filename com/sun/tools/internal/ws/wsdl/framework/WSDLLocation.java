/*    */ package com.sun.tools.internal.ws.wsdl.framework;
/*    */ 
/*    */ public class WSDLLocation
/*    */ {
/*    */   private LocationContext[] contexts;
/*    */   private int idPos;
/*    */   private LocationContext currentContext;
/*    */ 
/*    */   WSDLLocation()
/*    */   {
/* 37 */     reset();
/*    */   }
/*    */ 
/*    */   public void push() {
/* 41 */     int max = this.contexts.length;
/* 42 */     this.idPos += 1;
/* 43 */     if (this.idPos >= max) {
/* 44 */       LocationContext[] newContexts = new LocationContext[max * 2];
/* 45 */       System.arraycopy(this.contexts, 0, newContexts, 0, max);
/* 46 */       this.contexts = newContexts;
/*    */     }
/* 48 */     this.currentContext = this.contexts[this.idPos];
/* 49 */     if (this.currentContext == null)
/*    */     {
/*    */       void tmp84_81 = new LocationContext(null); this.currentContext = tmp84_81; this.contexts[this.idPos] = tmp84_81;
/*    */     }
/*    */   }
/*    */ 
/*    */   public void pop() {
/* 55 */     this.idPos -= 1;
/* 56 */     if (this.idPos >= 0)
/* 57 */       this.currentContext = this.contexts[this.idPos];
/*    */   }
/*    */ 
/*    */   public final void reset()
/*    */   {
/* 62 */     this.contexts = new LocationContext[32];
/* 63 */     this.idPos = 0;
/*    */     void tmp31_28 = new LocationContext(null); this.currentContext = tmp31_28; this.contexts[this.idPos] = tmp31_28;
/*    */   }
/*    */ 
/*    */   public String getLocation() {
/* 68 */     return this.currentContext.getLocation();
/*    */   }
/*    */ 
/*    */   public void setLocation(String loc) {
/* 72 */     this.currentContext.setLocation(loc);
/*    */   }
/*    */ 
/*    */   private static class LocationContext
/*    */   {
/*    */     private String location;
/*    */ 
/*    */     void setLocation(String loc)
/*    */     {
/* 82 */       this.location = loc;
/*    */     }
/*    */ 
/*    */     String getLocation() {
/* 86 */       return this.location;
/*    */     }
/*    */   }
/*    */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.internal.ws.wsdl.framework.WSDLLocation
 * JD-Core Version:    0.6.2
 */