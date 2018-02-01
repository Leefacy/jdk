/*    */ package com.sun.xml.internal.rngom.parse.host;
/*    */ 
/*    */ import com.sun.xml.internal.rngom.ast.builder.BuildException;
/*    */ import com.sun.xml.internal.rngom.ast.builder.CommentList;
/*    */ import com.sun.xml.internal.rngom.ast.om.Location;
/*    */ 
/*    */ class CommentListHost extends Base
/*    */   implements CommentList
/*    */ {
/*    */   final CommentList lhs;
/*    */   final CommentList rhs;
/*    */ 
/*    */   CommentListHost(CommentList lhs, CommentList rhs)
/*    */   {
/* 63 */     this.lhs = lhs;
/* 64 */     this.rhs = rhs;
/*    */   }
/*    */ 
/*    */   public void addComment(String value, Location _loc) throws BuildException {
/* 68 */     LocationHost loc = cast(_loc);
/* 69 */     if (this.lhs != null)
/* 70 */       this.lhs.addComment(value, loc.lhs);
/* 71 */     if (this.rhs != null)
/* 72 */       this.rhs.addComment(value, loc.rhs);
/*    */   }
/*    */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.xml.internal.rngom.parse.host.CommentListHost
 * JD-Core Version:    0.6.2
 */