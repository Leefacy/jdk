/*    */ package com.sun.tools.internal.xjc.api.impl.s2j;
/*    */ 
/*    */ import org.xml.sax.ErrorHandler;
/*    */ import org.xml.sax.SAXException;
/*    */ import org.xml.sax.SAXParseException;
/*    */ 
/*    */ final class DowngradingErrorHandler
/*    */   implements ErrorHandler
/*    */ {
/*    */   private final ErrorHandler core;
/*    */ 
/*    */   public DowngradingErrorHandler(ErrorHandler core)
/*    */   {
/* 41 */     this.core = core;
/*    */   }
/*    */ 
/*    */   public void warning(SAXParseException exception) throws SAXException {
/* 45 */     this.core.warning(exception);
/*    */   }
/*    */ 
/*    */   public void error(SAXParseException exception) throws SAXException {
/* 49 */     this.core.warning(exception);
/*    */   }
/*    */ 
/*    */   public void fatalError(SAXParseException exception) throws SAXException {
/* 53 */     this.core.warning(exception);
/*    */   }
/*    */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.internal.xjc.api.impl.s2j.DowngradingErrorHandler
 * JD-Core Version:    0.6.2
 */