/*    */ package com.sun.tools.internal.ws.wsdl.framework;
/*    */ 
/*    */ import javax.xml.namespace.QName;
/*    */ 
/*    */ public class ExternalEntityReference
/*    */ {
/*    */   private AbstractDocument _document;
/*    */   private Kind _kind;
/*    */   private QName _name;
/*    */ 
/*    */   public ExternalEntityReference(AbstractDocument document, Kind kind, QName name)
/*    */   {
/* 41 */     this._document = document;
/* 42 */     this._kind = kind;
/* 43 */     this._name = name;
/*    */   }
/*    */ 
/*    */   public AbstractDocument getDocument() {
/* 47 */     return this._document;
/*    */   }
/*    */ 
/*    */   public Kind getKind() {
/* 51 */     return this._kind;
/*    */   }
/*    */ 
/*    */   public QName getName() {
/* 55 */     return this._name;
/*    */   }
/*    */ 
/*    */   public GloballyKnown resolve() {
/* 59 */     return this._document.find(this._kind, this._name);
/*    */   }
/*    */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.internal.ws.wsdl.framework.ExternalEntityReference
 * JD-Core Version:    0.6.2
 */