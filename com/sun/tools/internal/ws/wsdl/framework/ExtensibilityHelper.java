/*    */ package com.sun.tools.internal.ws.wsdl.framework;
/*    */ 
/*    */ import com.sun.tools.internal.ws.api.wsdl.TWSDLExtension;
/*    */ import java.util.ArrayList;
/*    */ import java.util.Iterator;
/*    */ import java.util.List;
/*    */ 
/*    */ public class ExtensibilityHelper
/*    */ {
/*    */   private List<TWSDLExtension> _extensions;
/*    */ 
/*    */   public void addExtension(TWSDLExtension e)
/*    */   {
/* 45 */     if (this._extensions == null) {
/* 46 */       this._extensions = new ArrayList();
/*    */     }
/* 48 */     this._extensions.add(e);
/*    */   }
/*    */ 
/*    */   public Iterable<TWSDLExtension> extensions() {
/* 52 */     if (this._extensions == null) {
/* 53 */       return new ArrayList();
/*    */     }
/* 55 */     return this._extensions;
/*    */   }
/*    */ 
/*    */   public void withAllSubEntitiesDo(EntityAction action)
/*    */   {
/*    */     Iterator iter;
/* 60 */     if (this._extensions != null)
/* 61 */       for (iter = this._extensions.iterator(); iter.hasNext(); )
/* 62 */         action.perform((Entity)iter.next());
/*    */   }
/*    */ 
/*    */   public void accept(ExtensionVisitor visitor)
/*    */     throws Exception
/*    */   {
/*    */     Iterator iter;
/* 68 */     if (this._extensions != null)
/* 69 */       for (iter = this._extensions.iterator(); iter.hasNext(); )
/* 70 */         ((ExtensionImpl)iter.next()).accept(visitor);
/*    */   }
/*    */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.internal.ws.wsdl.framework.ExtensibilityHelper
 * JD-Core Version:    0.6.2
 */