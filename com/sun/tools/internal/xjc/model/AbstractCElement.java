/*    */ package com.sun.tools.internal.xjc.model;
/*    */ 
/*    */ import com.sun.xml.internal.xsom.XSComponent;
/*    */ import javax.xml.bind.annotation.XmlTransient;
/*    */ import org.xml.sax.Locator;
/*    */ 
/*    */ abstract class AbstractCElement extends AbstractCTypeInfoImpl
/*    */   implements CElement
/*    */ {
/*    */ 
/*    */   @XmlTransient
/*    */   private final Locator locator;
/*    */   private boolean isAbstract;
/*    */ 
/*    */   protected AbstractCElement(Model model, XSComponent source, Locator locator, CCustomizations customizations)
/*    */   {
/* 50 */     super(model, source, customizations);
/* 51 */     this.locator = locator;
/*    */   }
/*    */ 
/*    */   public Locator getLocator() {
/* 55 */     return this.locator;
/*    */   }
/*    */ 
/*    */   public boolean isAbstract() {
/* 59 */     return this.isAbstract;
/*    */   }
/*    */ 
/*    */   public void setAbstract() {
/* 63 */     this.isAbstract = true;
/*    */   }
/*    */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.internal.xjc.model.AbstractCElement
 * JD-Core Version:    0.6.2
 */