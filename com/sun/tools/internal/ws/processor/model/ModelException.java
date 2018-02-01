/*    */ package com.sun.tools.internal.ws.processor.model;
/*    */ 
/*    */ import com.sun.istack.internal.localization.Localizable;
/*    */ import com.sun.tools.internal.ws.processor.ProcessorException;
/*    */ 
/*    */ public class ModelException extends ProcessorException
/*    */ {
/*    */   public ModelException(String key, Object[] args)
/*    */   {
/* 42 */     super(key, args);
/*    */   }
/*    */ 
/*    */   public ModelException(Throwable throwable) {
/* 46 */     super(throwable);
/*    */   }
/*    */ 
/*    */   public ModelException(Localizable arg) {
/* 50 */     super("model.nestedModelError", new Object[] { arg });
/*    */   }
/*    */ 
/*    */   public String getDefaultResourceBundleName() {
/* 54 */     return "com.sun.tools.internal.ws.resources.model";
/*    */   }
/*    */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.internal.ws.processor.model.ModelException
 * JD-Core Version:    0.6.2
 */