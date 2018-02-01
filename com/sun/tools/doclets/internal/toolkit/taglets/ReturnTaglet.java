/*    */ package com.sun.tools.doclets.internal.toolkit.taglets;
/*    */ 
/*    */ import com.sun.javadoc.Doc;
/*    */ import com.sun.javadoc.MethodDoc;
/*    */ import com.sun.javadoc.ProgramElementDoc;
/*    */ import com.sun.javadoc.Tag;
/*    */ import com.sun.javadoc.Type;
/*    */ import com.sun.tools.doclets.internal.toolkit.Content;
/*    */ import com.sun.tools.doclets.internal.toolkit.util.DocFinder;
/*    */ import com.sun.tools.doclets.internal.toolkit.util.DocFinder.Input;
/*    */ import com.sun.tools.doclets.internal.toolkit.util.DocFinder.Output;
/*    */ import com.sun.tools.doclets.internal.toolkit.util.MessageRetriever;
/*    */ 
/*    */ public class ReturnTaglet extends BaseExecutableMemberTaglet
/*    */   implements InheritableTaglet
/*    */ {
/*    */   public ReturnTaglet()
/*    */   {
/* 47 */     this.name = "return";
/*    */   }
/*    */ 
/*    */   public void inherit(DocFinder.Input paramInput, DocFinder.Output paramOutput)
/*    */   {
/* 54 */     Tag[] arrayOfTag = paramInput.element.tags("return");
/* 55 */     if (arrayOfTag.length > 0) {
/* 56 */       paramOutput.holder = paramInput.element;
/* 57 */       paramOutput.holderTag = arrayOfTag[0];
/* 58 */       paramOutput.inlineTags = (paramInput.isFirstSentence ? arrayOfTag[0]
/* 59 */         .firstSentenceTags() : arrayOfTag[0].inlineTags());
/*    */     }
/*    */   }
/*    */ 
/*    */   public boolean inConstructor()
/*    */   {
/* 71 */     return false;
/*    */   }
/*    */ 
/*    */   public Content getTagletOutput(Doc paramDoc, TagletWriter paramTagletWriter)
/*    */   {
/* 78 */     Type localType = ((MethodDoc)paramDoc).returnType();
/* 79 */     Tag[] arrayOfTag = paramDoc.tags(this.name);
/*    */ 
/* 82 */     if ((localType.isPrimitive()) && (localType.typeName().equals("void"))) {
/* 83 */       if (arrayOfTag.length > 0) {
/* 84 */         paramTagletWriter.getMsgRetriever().warning(paramDoc.position(), "doclet.Return_tag_on_void_method", new Object[0]);
/*    */       }
/*    */ 
/* 87 */       return null;
/*    */     }
/*    */ 
/* 90 */     if (arrayOfTag.length == 0)
/*    */     {
/* 92 */       DocFinder.Output localOutput = DocFinder.search(new DocFinder.Input((MethodDoc)paramDoc, this));
/*    */ 
/* 93 */       arrayOfTag = new Tag[] { localOutput.holderTag == null ? arrayOfTag : localOutput.holderTag };
/*    */     }
/* 95 */     return arrayOfTag.length > 0 ? paramTagletWriter.returnTagOutput(arrayOfTag[0]) : null;
/*    */   }
/*    */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.doclets.internal.toolkit.taglets.ReturnTaglet
 * JD-Core Version:    0.6.2
 */