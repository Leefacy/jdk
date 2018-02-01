/*    */ package com.sun.tools.doclets.internal.toolkit.util;
/*    */ 
/*    */ import com.sun.javadoc.MethodDoc;
/*    */ 
/*    */ public class TaggedMethodFinder extends MethodFinder
/*    */ {
/*    */   public boolean isCorrectMethod(MethodDoc paramMethodDoc)
/*    */   {
/* 43 */     return paramMethodDoc.paramTags().length + paramMethodDoc.tags("return").length + paramMethodDoc
/* 43 */       .throwsTags().length + paramMethodDoc.seeTags().length > 0;
/*    */   }
/*    */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.doclets.internal.toolkit.util.TaggedMethodFinder
 * JD-Core Version:    0.6.2
 */