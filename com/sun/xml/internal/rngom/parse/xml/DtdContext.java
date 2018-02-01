/*    */ package com.sun.xml.internal.rngom.parse.xml;
/*    */ 
/*    */ import java.util.Hashtable;
/*    */ import org.relaxng.datatype.ValidationContext;
/*    */ import org.xml.sax.DTDHandler;
/*    */ import org.xml.sax.SAXException;
/*    */ 
/*    */ public abstract class DtdContext
/*    */   implements DTDHandler, ValidationContext
/*    */ {
/*    */   private final Hashtable notationTable;
/*    */   private final Hashtable unparsedEntityTable;
/*    */ 
/*    */   public DtdContext()
/*    */   {
/* 59 */     this.notationTable = new Hashtable();
/* 60 */     this.unparsedEntityTable = new Hashtable();
/*    */   }
/*    */ 
/*    */   public DtdContext(DtdContext dc) {
/* 64 */     this.notationTable = dc.notationTable;
/* 65 */     this.unparsedEntityTable = dc.unparsedEntityTable;
/*    */   }
/*    */ 
/*    */   public void notationDecl(String name, String publicId, String systemId) throws SAXException
/*    */   {
/* 70 */     this.notationTable.put(name, name);
/*    */   }
/*    */ 
/*    */   public void unparsedEntityDecl(String name, String publicId, String systemId, String notationName)
/*    */     throws SAXException
/*    */   {
/* 79 */     this.unparsedEntityTable.put(name, name);
/*    */   }
/*    */ 
/*    */   public boolean isNotation(String notationName) {
/* 83 */     return this.notationTable.get(notationName) != null;
/*    */   }
/*    */ 
/*    */   public boolean isUnparsedEntity(String entityName) {
/* 87 */     return this.unparsedEntityTable.get(entityName) != null;
/*    */   }
/*    */ 
/*    */   public void clearDtdContext() {
/* 91 */     this.notationTable.clear();
/* 92 */     this.unparsedEntityTable.clear();
/*    */   }
/*    */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.xml.internal.rngom.parse.xml.DtdContext
 * JD-Core Version:    0.6.2
 */