/*    */ package com.sun.tools.doclets.internal.toolkit.taglets;
/*    */ 
/*    */ import com.sun.javadoc.Tag;
/*    */ import com.sun.tools.doclets.internal.toolkit.Content;
/*    */ 
/*    */ public class DocRootTaglet extends BaseInlineTaglet
/*    */ {
/*    */   public DocRootTaglet()
/*    */   {
/* 53 */     this.name = "docRoot";
/*    */   }
/*    */ 
/*    */   public Content getTagletOutput(Tag paramTag, TagletWriter paramTagletWriter)
/*    */   {
/* 65 */     return paramTagletWriter.getDocRootOutput();
/*    */   }
/*    */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.doclets.internal.toolkit.taglets.DocRootTaglet
 * JD-Core Version:    0.6.2
 */