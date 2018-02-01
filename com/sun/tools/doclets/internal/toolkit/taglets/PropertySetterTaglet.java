/*    */ package com.sun.tools.doclets.internal.toolkit.taglets;
/*    */ 
/*    */ import com.sun.tools.doclets.internal.toolkit.Configuration;
/*    */ 
/*    */ public class PropertySetterTaglet extends BasePropertyTaglet
/*    */ {
/*    */   public PropertySetterTaglet()
/*    */   {
/* 43 */     this.name = "propertySetter";
/*    */   }
/*    */ 
/*    */   String getText(TagletWriter paramTagletWriter)
/*    */   {
/* 48 */     return paramTagletWriter.configuration().getText("doclet.PropertySetter");
/*    */   }
/*    */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.doclets.internal.toolkit.taglets.PropertySetterTaglet
 * JD-Core Version:    0.6.2
 */