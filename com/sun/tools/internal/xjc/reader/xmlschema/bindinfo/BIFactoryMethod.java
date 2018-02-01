/*    */ package com.sun.tools.internal.xjc.reader.xmlschema.bindinfo;
/*    */ 
/*    */ import com.sun.tools.internal.xjc.model.CPropertyInfo;
/*    */ import com.sun.tools.internal.xjc.reader.Ring;
/*    */ import com.sun.tools.internal.xjc.reader.xmlschema.BGMBuilder;
/*    */ import com.sun.xml.internal.xsom.XSComponent;
/*    */ import javax.xml.bind.annotation.XmlAttribute;
/*    */ import javax.xml.bind.annotation.XmlRootElement;
/*    */ import javax.xml.namespace.QName;
/*    */ 
/*    */ @XmlRootElement(name="factoryMethod")
/*    */ public class BIFactoryMethod extends AbstractDeclarationImpl
/*    */ {
/*    */ 
/*    */   @XmlAttribute
/*    */   public String name;
/* 64 */   public static final QName NAME = new QName("http://java.sun.com/xml/ns/jaxb", "factoryMethod");
/*    */ 
/*    */   public static void handle(XSComponent source, CPropertyInfo prop)
/*    */   {
/* 53 */     BIInlineBinaryData inline = (BIInlineBinaryData)((BGMBuilder)Ring.get(BGMBuilder.class)).getBindInfo(source).get(BIInlineBinaryData.class);
/* 54 */     if (inline != null) {
/* 55 */       prop.inlineBinaryData = true;
/* 56 */       inline.markAsAcknowledged();
/*    */     }
/*    */   }
/*    */ 
/*    */   public final QName getName() {
/* 61 */     return NAME;
/*    */   }
/*    */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.internal.xjc.reader.xmlschema.bindinfo.BIFactoryMethod
 * JD-Core Version:    0.6.2
 */