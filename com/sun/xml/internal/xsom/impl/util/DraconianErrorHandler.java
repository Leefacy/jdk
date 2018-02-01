/*    */ package com.sun.xml.internal.xsom.impl.util;
/*    */ 
/*    */ import org.xml.sax.ErrorHandler;
/*    */ import org.xml.sax.SAXException;
/*    */ import org.xml.sax.SAXParseException;
/*    */ 
/*    */ public class DraconianErrorHandler
/*    */   implements ErrorHandler
/*    */ {
/*    */   public void error(SAXParseException e)
/*    */     throws SAXException
/*    */   {
/* 37 */     throw e;
/*    */   }
/*    */   public void fatalError(SAXParseException e) throws SAXException {
/* 40 */     throw e;
/*    */   }
/*    */ 
/*    */   public void warning(SAXParseException e)
/*    */   {
/*    */   }
/*    */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.xml.internal.xsom.impl.util.DraconianErrorHandler
 * JD-Core Version:    0.6.2
 */