/*    */ package com.sun.tools.internal.ws.processor.generator;
/*    */ 
/*    */ import com.sun.tools.internal.ws.processor.ProcessorException;
/*    */ 
/*    */ public class GeneratorException extends ProcessorException
/*    */ {
/*    */   public GeneratorException(String key, Object[] args)
/*    */   {
/* 37 */     super(key, args);
/*    */   }
/*    */ 
/*    */   public GeneratorException(Throwable throwable) {
/* 41 */     super(throwable);
/*    */   }
/*    */ 
/*    */   public String getDefaultResourceBundleName() {
/* 45 */     return "com.sun.tools.internal.ws.resources.generator";
/*    */   }
/*    */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.internal.ws.processor.generator.GeneratorException
 * JD-Core Version:    0.6.2
 */