/*    */ package com.sun.tools.doclets.formats.html;
/*    */ 
/*    */ import com.sun.tools.doclets.internal.toolkit.util.links.LinkOutput;
/*    */ 
/*    */ public class LinkOutputImpl
/*    */   implements LinkOutput
/*    */ {
/*    */   public StringBuilder output;
/*    */ 
/*    */   public LinkOutputImpl()
/*    */   {
/* 52 */     this.output = new StringBuilder();
/*    */   }
/*    */ 
/*    */   public void append(Object paramObject)
/*    */   {
/* 59 */     this.output.append((paramObject instanceof String) ? (String)paramObject : ((LinkOutputImpl)paramObject)
/* 60 */       .toString());
/*    */   }
/*    */ 
/*    */   public void insert(int paramInt, Object paramObject)
/*    */   {
/* 67 */     this.output.insert(paramInt, paramObject.toString());
/*    */   }
/*    */ 
/*    */   public String toString()
/*    */   {
/* 74 */     return this.output.toString();
/*    */   }
/*    */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.doclets.formats.html.LinkOutputImpl
 * JD-Core Version:    0.6.2
 */