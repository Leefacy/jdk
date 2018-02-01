/*    */ package com.sun.tools.internal.xjc.reader.xmlschema.bindinfo;
/*    */ 
/*    */ import com.sun.tools.internal.xjc.model.CPropertyInfo;
/*    */ import com.sun.tools.internal.xjc.reader.Ring;
/*    */ import com.sun.tools.internal.xjc.reader.xmlschema.BGMBuilder;
/*    */ import com.sun.xml.internal.xsom.XSComponent;
/*    */ import javax.xml.bind.annotation.XmlRootElement;
/*    */ import javax.xml.namespace.QName;
/*    */ 
/*    */ @XmlRootElement(name="inlineBinaryData")
/*    */ public class BIInlineBinaryData extends AbstractDeclarationImpl
/*    */ {
/* 62 */   public static final QName NAME = new QName("http://java.sun.com/xml/ns/jaxb", "inlineBinaryData");
/*    */ 
/*    */   public static void handle(XSComponent source, CPropertyInfo prop)
/*    */   {
/* 51 */     BIInlineBinaryData inline = (BIInlineBinaryData)((BGMBuilder)Ring.get(BGMBuilder.class)).getBindInfo(source).get(BIInlineBinaryData.class);
/* 52 */     if (inline != null) {
/* 53 */       prop.inlineBinaryData = true;
/* 54 */       inline.markAsAcknowledged();
/*    */     }
/*    */   }
/*    */ 
/*    */   public final QName getName() {
/* 59 */     return NAME;
/*    */   }
/*    */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.internal.xjc.reader.xmlschema.bindinfo.BIInlineBinaryData
 * JD-Core Version:    0.6.2
 */