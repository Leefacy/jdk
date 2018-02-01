/*    */ package com.sun.tools.internal.xjc.reader.xmlschema.bindinfo;
/*    */ 
/*    */ import javax.xml.bind.annotation.XmlAttribute;
/*    */ import javax.xml.bind.annotation.XmlElement;
/*    */ import javax.xml.bind.annotation.XmlRootElement;
/*    */ import javax.xml.namespace.QName;
/*    */ 
/*    */ @XmlRootElement(name="typesafeEnumMember")
/*    */ public class BIEnumMember extends AbstractDeclarationImpl
/*    */ {
/*    */ 
/*    */   @XmlAttribute
/*    */   public final String name;
/*    */ 
/*    */   @XmlElement
/*    */   public final String javadoc;
/* 64 */   public static final QName NAME = new QName("http://java.sun.com/xml/ns/jaxb", "typesafeEnumMember");
/*    */ 
/*    */   protected BIEnumMember()
/*    */   {
/* 44 */     this.name = null;
/* 45 */     this.javadoc = null;
/*    */   }
/*    */ 
/*    */   public QName getName()
/*    */   {
/* 61 */     return NAME;
/*    */   }
/*    */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.internal.xjc.reader.xmlschema.bindinfo.BIEnumMember
 * JD-Core Version:    0.6.2
 */