/*    */ package com.sun.xml.internal.rngom.parse.host;
/*    */ 
/*    */ import com.sun.xml.internal.rngom.ast.builder.BuildException;
/*    */ import com.sun.xml.internal.rngom.ast.builder.CommentList;
/*    */ import com.sun.xml.internal.rngom.ast.builder.ElementAnnotationBuilder;
/*    */ import com.sun.xml.internal.rngom.ast.om.Location;
/*    */ import com.sun.xml.internal.rngom.ast.om.ParsedElementAnnotation;
/*    */ 
/*    */ final class ElementAnnotationBuilderHost extends AnnotationsHost
/*    */   implements ElementAnnotationBuilder
/*    */ {
/*    */   final ElementAnnotationBuilder lhs;
/*    */   final ElementAnnotationBuilder rhs;
/*    */ 
/*    */   ElementAnnotationBuilderHost(ElementAnnotationBuilder lhs, ElementAnnotationBuilder rhs)
/*    */   {
/* 64 */     super(lhs, rhs);
/* 65 */     this.lhs = lhs;
/* 66 */     this.rhs = rhs;
/*    */   }
/*    */ 
/*    */   public void addText(String value, Location _loc, CommentList _comments) throws BuildException {
/* 70 */     LocationHost loc = cast(_loc);
/* 71 */     CommentListHost comments = (CommentListHost)_comments;
/*    */ 
/* 73 */     this.lhs.addText(value, loc.lhs, comments == null ? null : comments.lhs);
/* 74 */     this.rhs.addText(value, loc.rhs, comments == null ? null : comments.rhs);
/*    */   }
/*    */ 
/*    */   public ParsedElementAnnotation makeElementAnnotation()
/*    */     throws BuildException
/*    */   {
/* 80 */     return new ParsedElementAnnotationHost(this.lhs
/* 79 */       .makeElementAnnotation(), this.rhs
/* 80 */       .makeElementAnnotation());
/*    */   }
/*    */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.xml.internal.rngom.parse.host.ElementAnnotationBuilderHost
 * JD-Core Version:    0.6.2
 */