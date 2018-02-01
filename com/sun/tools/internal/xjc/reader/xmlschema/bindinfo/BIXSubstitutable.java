/*    */ package com.sun.tools.internal.xjc.reader.xmlschema.bindinfo;
/*    */ 
/*    */ import javax.xml.bind.annotation.XmlRootElement;
/*    */ import javax.xml.namespace.QName;
/*    */ 
/*    */ @XmlRootElement(name="substitutable", namespace="http://java.sun.com/xml/ns/jaxb/xjc")
/*    */ public final class BIXSubstitutable extends AbstractDeclarationImpl
/*    */ {
/* 47 */   public static final QName NAME = new QName("http://java.sun.com/xml/ns/jaxb/xjc", "substitutable");
/*    */ 
/*    */   public final QName getName()
/*    */   {
/* 44 */     return NAME;
/*    */   }
/*    */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.internal.xjc.reader.xmlschema.bindinfo.BIXSubstitutable
 * JD-Core Version:    0.6.2
 */