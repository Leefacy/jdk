/*    */ package com.sun.tools.internal.xjc.model;
/*    */ 
/*    */ import org.w3c.dom.Element;
/*    */ import org.xml.sax.Locator;
/*    */ 
/*    */ public class CPluginCustomization
/*    */ {
/*    */   public final Element element;
/*    */   public final Locator locator;
/*    */   private boolean acknowledged;
/*    */ 
/*    */   public void markAsAcknowledged()
/*    */   {
/* 69 */     this.acknowledged = true;
/*    */   }
/*    */ 
/*    */   public CPluginCustomization(Element element, Locator locator) {
/* 73 */     this.element = element;
/* 74 */     this.locator = locator;
/*    */   }
/*    */ 
/*    */   public boolean isAcknowledged() {
/* 78 */     return this.acknowledged;
/*    */   }
/*    */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.internal.xjc.model.CPluginCustomization
 * JD-Core Version:    0.6.2
 */