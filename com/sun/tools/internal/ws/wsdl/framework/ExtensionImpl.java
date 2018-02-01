/*    */ package com.sun.tools.internal.ws.wsdl.framework;
/*    */ 
/*    */ import com.sun.tools.internal.ws.api.wsdl.TWSDLExtensible;
/*    */ import com.sun.tools.internal.ws.api.wsdl.TWSDLExtension;
/*    */ import org.xml.sax.Locator;
/*    */ 
/*    */ public abstract class ExtensionImpl extends Entity
/*    */   implements TWSDLExtension
/*    */ {
/*    */   private TWSDLExtensible _parent;
/*    */ 
/*    */   public ExtensionImpl(Locator locator)
/*    */   {
/* 40 */     super(locator);
/*    */   }
/*    */ 
/*    */   public TWSDLExtensible getParent() {
/* 44 */     return this._parent;
/*    */   }
/*    */ 
/*    */   public void setParent(TWSDLExtensible parent) {
/* 48 */     this._parent = parent;
/*    */   }
/*    */ 
/*    */   public void accept(ExtensionVisitor visitor) throws Exception {
/* 52 */     visitor.preVisit(this);
/* 53 */     visitor.postVisit(this);
/*    */   }
/*    */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.internal.ws.wsdl.framework.ExtensionImpl
 * JD-Core Version:    0.6.2
 */