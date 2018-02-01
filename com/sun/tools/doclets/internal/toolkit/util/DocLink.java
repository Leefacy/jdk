/*    */ package com.sun.tools.doclets.internal.toolkit.util;
/*    */ 
/*    */ public class DocLink
/*    */ {
/*    */   final String path;
/*    */   final String query;
/*    */   final String fragment;
/*    */ 
/*    */   public static DocLink fragment(String paramString)
/*    */   {
/* 48 */     return new DocLink((String)null, (String)null, paramString);
/*    */   }
/*    */ 
/*    */   public DocLink(DocPath paramDocPath)
/*    */   {
/* 53 */     this(paramDocPath.getPath(), null, null);
/*    */   }
/*    */ 
/*    */   public DocLink(DocPath paramDocPath, String paramString1, String paramString2)
/*    */   {
/* 61 */     this(paramDocPath.getPath(), paramString1, paramString2);
/*    */   }
/*    */ 
/*    */   public DocLink(String paramString1, String paramString2, String paramString3)
/*    */   {
/* 69 */     this.path = paramString1;
/* 70 */     this.query = paramString2;
/* 71 */     this.fragment = paramString3;
/*    */   }
/*    */ 
/*    */   public String toString()
/*    */   {
/* 81 */     if ((this.path != null) && (isEmpty(this.query)) && (isEmpty(this.fragment))) {
/* 82 */       return this.path;
/*    */     }
/* 84 */     StringBuilder localStringBuilder = new StringBuilder();
/* 85 */     if (this.path != null)
/* 86 */       localStringBuilder.append(this.path);
/* 87 */     if (!isEmpty(this.query))
/* 88 */       localStringBuilder.append("?").append(this.query);
/* 89 */     if (!isEmpty(this.fragment))
/* 90 */       localStringBuilder.append("#").append(this.fragment);
/* 91 */     return localStringBuilder.toString();
/*    */   }
/*    */ 
/*    */   private static boolean isEmpty(String paramString) {
/* 95 */     return (paramString == null) || (paramString.isEmpty());
/*    */   }
/*    */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.doclets.internal.toolkit.util.DocLink
 * JD-Core Version:    0.6.2
 */