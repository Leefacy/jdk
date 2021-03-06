/*    */ package com.sun.tools.internal.xjc.addon.code_injector;
/*    */ 
/*    */ import com.sun.codemodel.internal.JDefinedClass;
/*    */ import com.sun.tools.internal.xjc.Options;
/*    */ import com.sun.tools.internal.xjc.Plugin;
/*    */ import com.sun.tools.internal.xjc.model.CClassInfo;
/*    */ import com.sun.tools.internal.xjc.model.CCustomizations;
/*    */ import com.sun.tools.internal.xjc.model.CPluginCustomization;
/*    */ import com.sun.tools.internal.xjc.outline.ClassOutline;
/*    */ import com.sun.tools.internal.xjc.outline.Outline;
/*    */ import com.sun.tools.internal.xjc.util.DOMUtils;
/*    */ import java.util.Collections;
/*    */ import java.util.List;
/*    */ import org.xml.sax.ErrorHandler;
/*    */ 
/*    */ public class PluginImpl extends Plugin
/*    */ {
/*    */   public String getOptionName()
/*    */   {
/* 49 */     return "Xinject-code";
/*    */   }
/*    */ 
/*    */   public List<String> getCustomizationURIs() {
/* 53 */     return Collections.singletonList("http://jaxb.dev.java.net/plugin/code-injector");
/*    */   }
/*    */ 
/*    */   public boolean isCustomizationTagName(String nsUri, String localName) {
/* 57 */     return (nsUri.equals("http://jaxb.dev.java.net/plugin/code-injector")) && (localName.equals("code"));
/*    */   }
/*    */ 
/*    */   public String getUsage() {
/* 61 */     return "  -Xinject-code      :  inject specified Java code fragments into the generated code";
/*    */   }
/*    */ 
/*    */   public boolean run(Outline model, Options opt, ErrorHandler errorHandler)
/*    */   {
/* 66 */     for (ClassOutline co : model.getClasses()) {
/* 67 */       CPluginCustomization c = co.target.getCustomizations().find("http://jaxb.dev.java.net/plugin/code-injector", "code");
/* 68 */       if (c != null)
/*    */       {
/* 71 */         c.markAsAcknowledged();
/*    */ 
/* 74 */         String codeFragment = DOMUtils.getElementText(c.element);
/*    */ 
/* 77 */         co.implClass.direct(codeFragment);
/*    */       }
/*    */     }
/* 80 */     return true;
/*    */   }
/*    */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.internal.xjc.addon.code_injector.PluginImpl
 * JD-Core Version:    0.6.2
 */