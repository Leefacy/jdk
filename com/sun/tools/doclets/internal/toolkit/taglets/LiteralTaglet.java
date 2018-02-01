/*    */ package com.sun.tools.doclets.internal.toolkit.taglets;
/*    */ 
/*    */ import com.sun.javadoc.Tag;
/*    */ import com.sun.tools.doclets.internal.toolkit.Content;
/*    */ import java.util.Map;
/*    */ 
/*    */ public class LiteralTaglet extends BaseInlineTaglet
/*    */ {
/*    */   private static final String NAME = "literal";
/*    */ 
/*    */   public static void register(Map<String, Taglet> paramMap)
/*    */   {
/* 56 */     paramMap.remove("literal");
/* 57 */     paramMap.put("literal", new LiteralTaglet());
/*    */   }
/*    */ 
/*    */   public String getName() {
/* 61 */     return "literal";
/*    */   }
/*    */ 
/*    */   public Content getTagletOutput(Tag paramTag, TagletWriter paramTagletWriter)
/*    */   {
/* 68 */     return paramTagletWriter.literalTagOutput(paramTag);
/*    */   }
/*    */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.doclets.internal.toolkit.taglets.LiteralTaglet
 * JD-Core Version:    0.6.2
 */