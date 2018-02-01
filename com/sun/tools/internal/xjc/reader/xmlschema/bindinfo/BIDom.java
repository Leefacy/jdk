/*    */ package com.sun.tools.internal.xjc.reader.xmlschema.bindinfo;
/*    */ 
/*    */ import javax.xml.bind.annotation.XmlAttribute;
/*    */ import javax.xml.bind.annotation.XmlRootElement;
/*    */ import javax.xml.namespace.QName;
/*    */ 
/*    */ @XmlRootElement(name="dom")
/*    */ public class BIDom extends AbstractDeclarationImpl
/*    */ {
/*    */ 
/*    */   @XmlAttribute
/*    */   String type;
/* 50 */   public static final QName NAME = new QName("http://java.sun.com/xml/ns/jaxb", "dom");
/*    */ 
/*    */   public final QName getName()
/*    */   {
/* 47 */     return NAME;
/*    */   }
/*    */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.internal.xjc.reader.xmlschema.bindinfo.BIDom
 * JD-Core Version:    0.6.2
 */