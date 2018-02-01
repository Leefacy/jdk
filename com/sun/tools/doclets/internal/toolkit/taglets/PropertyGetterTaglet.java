/*    */ package com.sun.tools.doclets.internal.toolkit.taglets;
/*    */ 
/*    */ import com.sun.tools.doclets.internal.toolkit.Configuration;
/*    */ 
/*    */ public class PropertyGetterTaglet extends BasePropertyTaglet
/*    */ {
/*    */   public PropertyGetterTaglet()
/*    */   {
/* 43 */     this.name = "propertyGetter";
/*    */   }
/*    */ 
/*    */   String getText(TagletWriter paramTagletWriter)
/*    */   {
/* 48 */     return paramTagletWriter.configuration().getText("doclet.PropertyGetter");
/*    */   }
/*    */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.doclets.internal.toolkit.taglets.PropertyGetterTaglet
 * JD-Core Version:    0.6.2
 */