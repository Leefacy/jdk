/*    */ package com.sun.tools.internal.ws.processor;
/*    */ 
/*    */ import com.sun.xml.internal.ws.util.exception.JAXWSExceptionBase;
/*    */ 
/*    */ public class ProcessorException extends JAXWSExceptionBase
/*    */ {
/*    */   public ProcessorException(String key, Object[] args)
/*    */   {
/* 41 */     super(key, args);
/*    */   }
/*    */ 
/*    */   public ProcessorException(String msg) {
/* 45 */     super(msg);
/*    */   }
/*    */ 
/*    */   public ProcessorException(Throwable throwable) {
/* 49 */     super(throwable);
/*    */   }
/*    */ 
/*    */   public String getDefaultResourceBundleName() {
/* 53 */     return "com.sun.tools.internal.ws.resources.processor";
/*    */   }
/*    */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.internal.ws.processor.ProcessorException
 * JD-Core Version:    0.6.2
 */