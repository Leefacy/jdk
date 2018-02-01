/*    */ package com.sun.tools.doclets.formats.html.markup;
/*    */ 
/*    */ import com.sun.tools.doclets.internal.toolkit.Content;
/*    */ import java.io.IOException;
/*    */ import java.io.Writer;
/*    */ import java.util.ArrayList;
/*    */ import java.util.Collections;
/*    */ import java.util.List;
/*    */ 
/*    */ public class ContentBuilder extends Content
/*    */ {
/* 39 */   protected List<Content> contents = Collections.emptyList();
/*    */ 
/*    */   public void addContent(Content paramContent)
/*    */   {
/* 43 */     nullCheck(paramContent);
/* 44 */     ensureMutableContents();
/* 45 */     if ((paramContent instanceof ContentBuilder))
/* 46 */       this.contents.addAll(((ContentBuilder)paramContent).contents);
/*    */     else
/* 48 */       this.contents.add(paramContent);
/*    */   }
/*    */ 
/*    */   public void addContent(String paramString)
/*    */   {
/* 53 */     if (paramString.isEmpty())
/* 54 */       return;
/* 55 */     ensureMutableContents();
/* 56 */     Content localContent = this.contents.isEmpty() ? null : (Content)this.contents.get(this.contents.size() - 1);
/*    */     StringContent localStringContent;
/* 58 */     if ((localContent != null) && ((localContent instanceof StringContent)))
/* 59 */       localStringContent = (StringContent)localContent;
/*    */     else {
/* 61 */       this.contents.add(localStringContent = new StringContent());
/*    */     }
/* 63 */     localStringContent.addContent(paramString);
/*    */   }
/*    */ 
/*    */   public boolean write(Writer paramWriter, boolean paramBoolean) throws IOException
/*    */   {
/* 68 */     for (Content localContent : this.contents) {
/* 69 */       paramBoolean = localContent.write(paramWriter, paramBoolean);
/*    */     }
/* 71 */     return paramBoolean;
/*    */   }
/*    */ 
/*    */   public boolean isEmpty()
/*    */   {
/* 76 */     for (Content localContent : this.contents) {
/* 77 */       if (!localContent.isEmpty())
/* 78 */         return false;
/*    */     }
/* 80 */     return true;
/*    */   }
/*    */ 
/*    */   public int charCount()
/*    */   {
/* 85 */     int i = 0;
/* 86 */     for (Content localContent : this.contents)
/* 87 */       i += localContent.charCount();
/* 88 */     return i;
/*    */   }
/*    */ 
/*    */   private void ensureMutableContents() {
/* 92 */     if (this.contents.isEmpty())
/* 93 */       this.contents = new ArrayList();
/*    */   }
/*    */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.doclets.formats.html.markup.ContentBuilder
 * JD-Core Version:    0.6.2
 */