/*    */ package com.sun.tools.doclets.formats.html.markup;
/*    */ 
/*    */ import com.sun.tools.javac.util.StringUtils;
/*    */ 
/*    */ public enum HtmlAttr
/*    */ {
/* 41 */   ALT, 
/* 42 */   BORDER, 
/* 43 */   CELLPADDING, 
/* 44 */   CELLSPACING, 
/* 45 */   CLASS, 
/* 46 */   CLEAR, 
/* 47 */   COLS, 
/* 48 */   CONTENT, 
/* 49 */   HREF, 
/* 50 */   HTTP_EQUIV("http-equiv"), 
/* 51 */   ID, 
/* 52 */   LANG, 
/* 53 */   NAME, 
/* 54 */   ONLOAD, 
/* 55 */   REL, 
/* 56 */   ROWS, 
/* 57 */   SCOPE, 
/* 58 */   SCROLLING, 
/* 59 */   SRC, 
/* 60 */   SUMMARY, 
/* 61 */   TARGET, 
/* 62 */   TITLE, 
/* 63 */   TYPE, 
/* 64 */   WIDTH;
/*    */ 
/*    */   private final String value;
/*    */ 
/*    */   private HtmlAttr() {
/* 69 */     this.value = StringUtils.toLowerCase(name());
/*    */   }
/*    */ 
/*    */   private HtmlAttr(String paramString) {
/* 73 */     this.value = paramString;
/*    */   }
/*    */ 
/*    */   public String toString() {
/* 77 */     return this.value;
/*    */   }
/*    */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.doclets.formats.html.markup.HtmlAttr
 * JD-Core Version:    0.6.2
 */