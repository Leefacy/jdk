/*    */ package com.sun.tools.internal.xjc.reader.dtd.bindinfo;
/*    */ 
/*    */ import org.w3c.dom.Element;
/*    */ import org.xml.sax.Locator;
/*    */ 
/*    */ class DOMLocator
/*    */ {
/*    */   private static final String locationNamespace = "http://www.sun.com/xmlns/jaxb/dom-location";
/*    */   private static final String systemId = "systemid";
/*    */   private static final String column = "column";
/*    */   private static final String line = "line";
/*    */ 
/*    */   public static void setLocationInfo(Element e, Locator loc)
/*    */   {
/* 40 */     e.setAttributeNS("http://www.sun.com/xmlns/jaxb/dom-location", "loc:systemid", loc.getSystemId());
/* 41 */     e.setAttributeNS("http://www.sun.com/xmlns/jaxb/dom-location", "loc:column", Integer.toString(loc.getLineNumber()));
/* 42 */     e.setAttributeNS("http://www.sun.com/xmlns/jaxb/dom-location", "loc:line", Integer.toString(loc.getColumnNumber()));
/*    */   }
/*    */ 
/*    */   public static Locator getLocationInfo(Element e)
/*    */   {
/* 53 */     if (DOMUtil.getAttribute(e, "http://www.sun.com/xmlns/jaxb/dom-location", "systemid") == null) {
/* 54 */       return null;
/*    */     }
/* 56 */     return new Locator() {
/*    */       public int getLineNumber() {
/* 58 */         return Integer.parseInt(DOMUtil.getAttribute(this.val$e, "http://www.sun.com/xmlns/jaxb/dom-location", "line"));
/*    */       }
/*    */       public int getColumnNumber() {
/* 61 */         return Integer.parseInt(DOMUtil.getAttribute(this.val$e, "http://www.sun.com/xmlns/jaxb/dom-location", "column"));
/*    */       }
/*    */       public String getSystemId() {
/* 64 */         return DOMUtil.getAttribute(this.val$e, "http://www.sun.com/xmlns/jaxb/dom-location", "systemid");
/*    */       }
/*    */       public String getPublicId() {
/* 67 */         return null;
/*    */       }
/*    */     };
/*    */   }
/*    */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.internal.xjc.reader.dtd.bindinfo.DOMLocator
 * JD-Core Version:    0.6.2
 */