/*    */ package com.sun.tools.internal.ws.util;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import org.xml.sax.EntityResolver;
/*    */ import org.xml.sax.InputSource;
/*    */ import org.xml.sax.SAXException;
/*    */ 
/*    */ public class ForkEntityResolver
/*    */   implements EntityResolver
/*    */ {
/*    */   private final EntityResolver lhs;
/*    */   private final EntityResolver rhs;
/*    */ 
/*    */   public ForkEntityResolver(EntityResolver lhs, EntityResolver rhs)
/*    */   {
/* 44 */     this.lhs = lhs;
/* 45 */     this.rhs = rhs;
/*    */   }
/*    */ 
/*    */   public InputSource resolveEntity(String publicId, String systemId) throws SAXException, IOException {
/* 49 */     InputSource is = this.lhs.resolveEntity(publicId, systemId);
/* 50 */     if (is != null) {
/* 51 */       return is;
/*    */     }
/* 53 */     if (publicId == null)
/*    */     {
/* 55 */       publicId = systemId;
/*    */     }
/*    */ 
/* 58 */     return this.rhs.resolveEntity(publicId, systemId);
/*    */   }
/*    */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.internal.ws.util.ForkEntityResolver
 * JD-Core Version:    0.6.2
 */