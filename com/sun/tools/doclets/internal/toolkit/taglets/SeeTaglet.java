/*    */ package com.sun.tools.doclets.internal.toolkit.taglets;
/*    */ 
/*    */ import com.sun.javadoc.Doc;
/*    */ import com.sun.javadoc.MethodDoc;
/*    */ import com.sun.javadoc.ProgramElementDoc;
/*    */ import com.sun.javadoc.SeeTag;
/*    */ import com.sun.javadoc.Tag;
/*    */ import com.sun.tools.doclets.internal.toolkit.Content;
/*    */ import com.sun.tools.doclets.internal.toolkit.util.DocFinder;
/*    */ import com.sun.tools.doclets.internal.toolkit.util.DocFinder.Input;
/*    */ import com.sun.tools.doclets.internal.toolkit.util.DocFinder.Output;
/*    */ 
/*    */ public class SeeTaglet extends BaseTaglet
/*    */   implements InheritableTaglet
/*    */ {
/*    */   public SeeTaglet()
/*    */   {
/* 46 */     this.name = "see";
/*    */   }
/*    */ 
/*    */   public void inherit(DocFinder.Input paramInput, DocFinder.Output paramOutput)
/*    */   {
/* 53 */     SeeTag[] arrayOfSeeTag = paramInput.element.seeTags();
/* 54 */     if (arrayOfSeeTag.length > 0) {
/* 55 */       paramOutput.holder = paramInput.element;
/* 56 */       paramOutput.holderTag = arrayOfSeeTag[0];
/* 57 */       paramOutput.inlineTags = (paramInput.isFirstSentence ? arrayOfSeeTag[0]
/* 58 */         .firstSentenceTags() : arrayOfSeeTag[0].inlineTags());
/*    */     }
/*    */   }
/*    */ 
/*    */   public Content getTagletOutput(Doc paramDoc, TagletWriter paramTagletWriter)
/*    */   {
/* 66 */     SeeTag[] arrayOfSeeTag = paramDoc.seeTags();
/* 67 */     if ((arrayOfSeeTag.length == 0) && ((paramDoc instanceof MethodDoc)))
/*    */     {
/* 69 */       DocFinder.Output localOutput = DocFinder.search(new DocFinder.Input((MethodDoc)paramDoc, this));
/*    */ 
/* 70 */       if (localOutput.holder != null) {
/* 71 */         arrayOfSeeTag = localOutput.holder.seeTags();
/*    */       }
/*    */     }
/* 74 */     return paramTagletWriter.seeTagOutput(paramDoc, arrayOfSeeTag);
/*    */   }
/*    */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.doclets.internal.toolkit.taglets.SeeTaglet
 * JD-Core Version:    0.6.2
 */