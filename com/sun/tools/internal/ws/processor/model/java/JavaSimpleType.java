/*    */ package com.sun.tools.internal.ws.processor.model.java;
/*    */ 
/*    */ import com.sun.tools.internal.ws.processor.model.jaxb.JAXBTypeAndAnnotation;
/*    */ 
/*    */ public class JavaSimpleType extends JavaType
/*    */ {
/*    */   public JavaSimpleType()
/*    */   {
/*    */   }
/*    */ 
/*    */   public JavaSimpleType(String name, String initString)
/*    */   {
/* 39 */     super(name, true, initString);
/*    */   }
/*    */ 
/*    */   public JavaSimpleType(JAXBTypeAndAnnotation jtype) {
/* 43 */     super(jtype);
/*    */   }
/*    */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.internal.ws.processor.model.java.JavaSimpleType
 * JD-Core Version:    0.6.2
 */