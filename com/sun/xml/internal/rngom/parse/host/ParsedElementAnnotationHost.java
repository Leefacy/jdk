/*    */ package com.sun.xml.internal.rngom.parse.host;
/*    */ 
/*    */ import com.sun.xml.internal.rngom.ast.om.ParsedElementAnnotation;
/*    */ 
/*    */ final class ParsedElementAnnotationHost
/*    */   implements ParsedElementAnnotation
/*    */ {
/*    */   final ParsedElementAnnotation lhs;
/*    */   final ParsedElementAnnotation rhs;
/*    */ 
/*    */   ParsedElementAnnotationHost(ParsedElementAnnotation lhs, ParsedElementAnnotation rhs)
/*    */   {
/* 60 */     this.lhs = lhs;
/* 61 */     this.rhs = rhs;
/*    */   }
/*    */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.xml.internal.rngom.parse.host.ParsedElementAnnotationHost
 * JD-Core Version:    0.6.2
 */