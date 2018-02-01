/*    */ package com.sun.tools.internal.ws.wsdl.parser;
/*    */ 
/*    */ import com.sun.tools.internal.ws.api.wsdl.TWSDLExtensible;
/*    */ import com.sun.tools.internal.ws.api.wsdl.TWSDLParserContext;
/*    */ import com.sun.tools.internal.ws.util.xml.XmlUtil;
/*    */ import com.sun.tools.internal.ws.wscompile.ErrorReceiver;
/*    */ import com.sun.xml.internal.ws.api.addressing.AddressingVersion;
/*    */ import java.util.Map;
/*    */ import javax.xml.namespace.QName;
/*    */ import org.w3c.dom.Element;
/*    */ 
/*    */ public class W3CAddressingExtensionHandler extends AbstractExtensionHandler
/*    */ {
/*    */   public W3CAddressingExtensionHandler(Map<String, AbstractExtensionHandler> extensionHandlerMap)
/*    */   {
/* 48 */     this(extensionHandlerMap, null);
/*    */   }
/*    */ 
/*    */   public W3CAddressingExtensionHandler(Map<String, AbstractExtensionHandler> extensionHandlerMap, ErrorReceiver errReceiver) {
/* 52 */     super(extensionHandlerMap);
/*    */   }
/*    */ 
/*    */   public String getNamespaceURI()
/*    */   {
/* 57 */     return AddressingVersion.W3C.wsdlNsUri;
/*    */   }
/*    */ 
/*    */   protected QName getWSDLExtensionQName()
/*    */   {
/* 62 */     return AddressingVersion.W3C.wsdlExtensionTag;
/*    */   }
/*    */ 
/*    */   public boolean handleBindingExtension(TWSDLParserContext context, TWSDLExtensible parent, Element e)
/*    */   {
/* 67 */     if (XmlUtil.matchesTagNS(e, getWSDLExtensionQName()))
/*    */     {
/* 78 */       return true;
/*    */     }
/* 80 */     return false;
/*    */   }
/*    */ 
/*    */   public boolean handlePortExtension(TWSDLParserContext context, TWSDLExtensible parent, Element e)
/*    */   {
/* 85 */     return handleBindingExtension(context, parent, e);
/*    */   }
/*    */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.internal.ws.wsdl.parser.W3CAddressingExtensionHandler
 * JD-Core Version:    0.6.2
 */