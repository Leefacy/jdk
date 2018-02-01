/*    */ package com.sun.xml.internal.xsom.impl.util;
/*    */ 
/*    */ import org.xml.sax.EntityResolver;
/*    */ import org.xml.sax.InputSource;
/*    */ 
/*    */ public class ResourceEntityResolver
/*    */   implements EntityResolver
/*    */ {
/*    */   private final Class base;
/*    */ 
/*    */   public ResourceEntityResolver(Class _base)
/*    */   {
/* 33 */     this.base = _base;
/*    */   }
/*    */ 
/*    */   public InputSource resolveEntity(String publicId, String systemId)
/*    */   {
/* 39 */     return new InputSource(this.base.getResourceAsStream(systemId));
/*    */   }
/*    */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.xml.internal.xsom.impl.util.ResourceEntityResolver
 * JD-Core Version:    0.6.2
 */