/*    */ package com.sun.xml.internal.xsom.impl;
/*    */ 
/*    */ import com.sun.xml.internal.xsom.XSAnnotation;
/*    */ import org.xml.sax.Locator;
/*    */ import org.xml.sax.helpers.LocatorImpl;
/*    */ 
/*    */ public class AnnotationImpl
/*    */   implements XSAnnotation
/*    */ {
/*    */   private Object annotation;
/*    */   private final Locator locator;
/* 78 */   private static final LocatorImplUnmodifiable NULL_LOCATION = new LocatorImplUnmodifiable(null);
/*    */ 
/*    */   public Object getAnnotation()
/*    */   {
/* 35 */     return this.annotation;
/*    */   }
/*    */   public Object setAnnotation(Object o) {
/* 38 */     Object r = this.annotation;
/* 39 */     this.annotation = o;
/* 40 */     return r;
/*    */   }
/*    */ 
/*    */   public Locator getLocator() {
/* 44 */     return this.locator;
/*    */   }
/*    */   public AnnotationImpl(Object o, Locator _loc) {
/* 47 */     this.annotation = o;
/* 48 */     this.locator = _loc;
/*    */   }
/*    */ 
/*    */   public AnnotationImpl() {
/* 52 */     this.locator = NULL_LOCATION;
/*    */   }
/*    */ 
/*    */   private static class LocatorImplUnmodifiable extends LocatorImpl
/*    */   {
/*    */     public void setColumnNumber(int columnNumber)
/*    */     {
/*    */     }
/*    */ 
/*    */     public void setPublicId(String publicId)
/*    */     {
/*    */     }
/*    */ 
/*    */     public void setSystemId(String systemId)
/*    */     {
/*    */     }
/*    */ 
/*    */     public void setLineNumber(int lineNumber)
/*    */     {
/*    */     }
/*    */   }
/*    */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.xml.internal.xsom.impl.AnnotationImpl
 * JD-Core Version:    0.6.2
 */