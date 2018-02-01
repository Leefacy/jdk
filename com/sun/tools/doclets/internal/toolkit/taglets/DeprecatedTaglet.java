/*    */ package com.sun.tools.doclets.internal.toolkit.taglets;
/*    */ 
/*    */ import com.sun.javadoc.Doc;
/*    */ import com.sun.tools.doclets.internal.toolkit.Content;
/*    */ 
/*    */ public class DeprecatedTaglet extends BaseTaglet
/*    */ {
/*    */   public DeprecatedTaglet()
/*    */   {
/* 46 */     this.name = "deprecated";
/*    */   }
/*    */ 
/*    */   public Content getTagletOutput(Doc paramDoc, TagletWriter paramTagletWriter)
/*    */   {
/* 53 */     return paramTagletWriter.deprecatedTagOutput(paramDoc);
/*    */   }
/*    */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.doclets.internal.toolkit.taglets.DeprecatedTaglet
 * JD-Core Version:    0.6.2
 */