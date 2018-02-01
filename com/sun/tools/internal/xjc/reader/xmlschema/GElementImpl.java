/*    */ package com.sun.tools.internal.xjc.reader.xmlschema;
/*    */ 
/*    */ import com.sun.xml.internal.xsom.XSElementDecl;
/*    */ import javax.xml.namespace.QName;
/*    */ 
/*    */ final class GElementImpl extends GElement
/*    */ {
/*    */   public final QName tagName;
/*    */   public final XSElementDecl decl;
/*    */ 
/*    */   public GElementImpl(QName tagName, XSElementDecl decl)
/*    */   {
/* 56 */     this.tagName = tagName;
/* 57 */     this.decl = decl;
/*    */   }
/*    */ 
/*    */   public String toString() {
/* 61 */     return this.tagName.toString();
/*    */   }
/*    */ 
/*    */   String getPropertyNameSeed() {
/* 65 */     return this.tagName.getLocalPart();
/*    */   }
/*    */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.internal.xjc.reader.xmlschema.GElementImpl
 * JD-Core Version:    0.6.2
 */