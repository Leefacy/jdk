/*    */ package com.sun.tools.internal.ws.processor.modeler;
/*    */ 
/*    */ import com.sun.istack.internal.localization.Localizable;
/*    */ import com.sun.tools.internal.ws.processor.ProcessorException;
/*    */ 
/*    */ public class ModelerException extends ProcessorException
/*    */ {
/*    */   public ModelerException(String key)
/*    */   {
/* 42 */     super(key);
/*    */   }
/*    */ 
/*    */   public ModelerException(String key, Object[] args) {
/* 46 */     super(key, args);
/*    */   }
/*    */ 
/*    */   public ModelerException(Throwable throwable) {
/* 50 */     super(throwable);
/*    */   }
/*    */ 
/*    */   public ModelerException(Localizable arg) {
/* 54 */     super("modeler.nestedModelError", new Object[] { arg });
/*    */   }
/*    */ 
/*    */   public String getDefaultResourceBundleName() {
/* 58 */     return "com.sun.tools.internal.ws.resources.modeler";
/*    */   }
/*    */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.internal.ws.processor.modeler.ModelerException
 * JD-Core Version:    0.6.2
 */