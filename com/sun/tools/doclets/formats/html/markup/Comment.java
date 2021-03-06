/*    */ package com.sun.tools.doclets.formats.html.markup;
/*    */ 
/*    */ import com.sun.tools.doclets.internal.toolkit.Content;
/*    */ import com.sun.tools.doclets.internal.toolkit.util.DocletAbortException;
/*    */ import com.sun.tools.doclets.internal.toolkit.util.DocletConstants;
/*    */ import java.io.IOException;
/*    */ import java.io.Writer;
/*    */ 
/*    */ public class Comment extends Content
/*    */ {
/*    */   private String commentText;
/*    */ 
/*    */   public Comment(String paramString)
/*    */   {
/* 54 */     this.commentText = ((String)nullCheck(paramString));
/*    */   }
/*    */ 
/*    */   public void addContent(Content paramContent)
/*    */   {
/* 66 */     throw new DocletAbortException("not supported");
/*    */   }
/*    */ 
/*    */   public void addContent(String paramString)
/*    */   {
/* 78 */     throw new DocletAbortException("not supported");
/*    */   }
/*    */ 
/*    */   public boolean isEmpty()
/*    */   {
/* 85 */     return this.commentText.isEmpty();
/*    */   }
/*    */ 
/*    */   public boolean write(Writer paramWriter, boolean paramBoolean)
/*    */     throws IOException
/*    */   {
/* 93 */     if (!paramBoolean)
/* 94 */       paramWriter.write(DocletConstants.NL);
/* 95 */     paramWriter.write("<!-- ");
/* 96 */     paramWriter.write(this.commentText);
/* 97 */     paramWriter.write(" -->" + DocletConstants.NL);
/* 98 */     return true;
/*    */   }
/*    */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.doclets.formats.html.markup.Comment
 * JD-Core Version:    0.6.2
 */