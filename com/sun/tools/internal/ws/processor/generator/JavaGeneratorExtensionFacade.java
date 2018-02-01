/*    */ package com.sun.tools.internal.ws.processor.generator;
/*    */ 
/*    */ import com.sun.codemodel.internal.JMethod;
/*    */ import com.sun.tools.internal.ws.api.TJavaGeneratorExtension;
/*    */ import com.sun.tools.internal.ws.api.wsdl.TWSDLOperation;
/*    */ 
/*    */ public final class JavaGeneratorExtensionFacade extends TJavaGeneratorExtension
/*    */ {
/*    */   private final TJavaGeneratorExtension[] extensions;
/*    */ 
/*    */   JavaGeneratorExtensionFacade(TJavaGeneratorExtension[] extensions)
/*    */   {
/* 39 */     assert (extensions != null);
/* 40 */     this.extensions = extensions;
/*    */   }
/*    */ 
/*    */   public void writeMethodAnnotations(TWSDLOperation wsdlOperation, JMethod jMethod) {
/* 44 */     for (TJavaGeneratorExtension e : this.extensions)
/* 45 */       e.writeMethodAnnotations(wsdlOperation, jMethod);
/*    */   }
/*    */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.internal.ws.processor.generator.JavaGeneratorExtensionFacade
 * JD-Core Version:    0.6.2
 */