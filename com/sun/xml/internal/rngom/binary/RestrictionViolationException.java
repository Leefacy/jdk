/*    */ package com.sun.xml.internal.rngom.binary;
/*    */ 
/*    */ import javax.xml.namespace.QName;
/*    */ import org.xml.sax.Locator;
/*    */ 
/*    */ final class RestrictionViolationException extends Exception
/*    */ {
/*    */   private String messageId;
/*    */   private Locator loc;
/*    */   private QName name;
/*    */ 
/*    */   RestrictionViolationException(String messageId)
/*    */   {
/* 58 */     this.messageId = messageId;
/*    */   }
/*    */ 
/*    */   RestrictionViolationException(String messageId, QName name) {
/* 62 */     this.messageId = messageId;
/* 63 */     this.name = name;
/*    */   }
/*    */ 
/*    */   String getMessageId() {
/* 67 */     return this.messageId;
/*    */   }
/*    */ 
/*    */   Locator getLocator() {
/* 71 */     return this.loc;
/*    */   }
/*    */ 
/*    */   void maybeSetLocator(Locator loc) {
/* 75 */     if (this.loc == null)
/* 76 */       this.loc = loc;
/*    */   }
/*    */ 
/*    */   QName getName() {
/* 80 */     return this.name;
/*    */   }
/*    */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.xml.internal.rngom.binary.RestrictionViolationException
 * JD-Core Version:    0.6.2
 */