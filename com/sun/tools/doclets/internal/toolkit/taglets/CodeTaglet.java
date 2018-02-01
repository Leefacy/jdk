/*    */ package com.sun.tools.doclets.internal.toolkit.taglets;
/*    */ 
/*    */ import com.sun.javadoc.Tag;
/*    */ import com.sun.tools.doclets.internal.toolkit.Content;
/*    */ import java.util.Map;
/*    */ 
/*    */ public class CodeTaglet extends BaseInlineTaglet
/*    */ {
/*    */   private static final String NAME = "code";
/*    */ 
/*    */   public static void register(Map<String, Taglet> paramMap)
/*    */   {
/* 57 */     paramMap.remove("code");
/* 58 */     paramMap.put("code", new CodeTaglet());
/*    */   }
/*    */ 
/*    */   public String getName() {
/* 62 */     return "code";
/*    */   }
/*    */ 
/*    */   public Content getTagletOutput(Tag paramTag, TagletWriter paramTagletWriter)
/*    */   {
/* 69 */     return paramTagletWriter.codeTagOutput(paramTag);
/*    */   }
/*    */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.doclets.internal.toolkit.taglets.CodeTaglet
 * JD-Core Version:    0.6.2
 */