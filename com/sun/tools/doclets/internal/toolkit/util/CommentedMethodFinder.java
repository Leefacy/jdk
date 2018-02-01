/*    */ package com.sun.tools.doclets.internal.toolkit.util;
/*    */ 
/*    */ import com.sun.javadoc.MethodDoc;
/*    */ 
/*    */ public class CommentedMethodFinder extends MethodFinder
/*    */ {
/*    */   public boolean isCorrectMethod(MethodDoc paramMethodDoc)
/*    */   {
/* 41 */     return paramMethodDoc.inlineTags().length > 0;
/*    */   }
/*    */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.doclets.internal.toolkit.util.CommentedMethodFinder
 * JD-Core Version:    0.6.2
 */