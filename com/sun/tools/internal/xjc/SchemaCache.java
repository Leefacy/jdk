/*    */ package com.sun.tools.internal.xjc;
/*    */ 
/*    */ import com.sun.xml.internal.bind.v2.util.XmlFactory;
/*    */ import java.net.URL;
/*    */ import javax.xml.validation.Schema;
/*    */ import javax.xml.validation.SchemaFactory;
/*    */ import javax.xml.validation.ValidatorHandler;
/*    */ import org.xml.sax.SAXException;
/*    */ 
/*    */ public final class SchemaCache
/*    */ {
/*    */   private Schema schema;
/*    */   private final URL source;
/*    */ 
/*    */   public SchemaCache(URL source)
/*    */   {
/* 55 */     this.source = source;
/*    */   }
/*    */ 
/*    */   public ValidatorHandler newValidator() {
/* 59 */     synchronized (this) {
/* 60 */       if (this.schema == null) {
/*    */         try
/*    */         {
/* 63 */           SchemaFactory sf = XmlFactory.createSchemaFactory("http://www.w3.org/2001/XMLSchema", false);
/* 64 */           this.schema = XmlFactory.allowExternalAccess(sf, "file", false).newSchema(this.source);
/*    */         }
/*    */         catch (SAXException e) {
/* 67 */           throw new AssertionError(e);
/*    */         }
/*    */       }
/*    */     }
/*    */ 
/* 72 */     ValidatorHandler handler = this.schema.newValidatorHandler();
/* 73 */     return handler;
/*    */   }
/*    */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.internal.xjc.SchemaCache
 * JD-Core Version:    0.6.2
 */