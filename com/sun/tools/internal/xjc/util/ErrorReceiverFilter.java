/*    */ package com.sun.tools.internal.xjc.util;
/*    */ 
/*    */ import com.sun.tools.internal.xjc.ErrorReceiver;
/*    */ import com.sun.tools.internal.xjc.api.ErrorListener;
/*    */ import org.xml.sax.SAXParseException;
/*    */ 
/*    */ public class ErrorReceiverFilter extends ErrorReceiver
/*    */ {
/*    */   private ErrorListener core;
/* 54 */   private boolean hadError = false;
/*    */ 
/*    */   public ErrorReceiverFilter()
/*    */   {
/*    */   }
/*    */ 
/*    */   public ErrorReceiverFilter(ErrorListener h)
/*    */   {
/* 46 */     setErrorReceiver(h);
/*    */   }
/*    */ 
/*    */   public void setErrorReceiver(ErrorListener handler)
/*    */   {
/* 51 */     this.core = handler;
/*    */   }
/*    */ 
/*    */   public final boolean hadError() {
/* 55 */     return this.hadError;
/*    */   }
/*    */   public void info(SAXParseException exception) {
/* 58 */     if (this.core != null) this.core.info(exception); 
/*    */   }
/*    */ 
/*    */   public void warning(SAXParseException exception)
/*    */   {
/* 62 */     if (this.core != null) this.core.warning(exception); 
/*    */   }
/*    */ 
/*    */   public void error(SAXParseException exception)
/*    */   {
/* 66 */     this.hadError = true;
/* 67 */     if (this.core != null) this.core.error(exception); 
/*    */   }
/*    */ 
/*    */   public void fatalError(SAXParseException exception)
/*    */   {
/* 71 */     this.hadError = true;
/* 72 */     if (this.core != null) this.core.fatalError(exception);
/*    */   }
/*    */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.internal.xjc.util.ErrorReceiverFilter
 * JD-Core Version:    0.6.2
 */