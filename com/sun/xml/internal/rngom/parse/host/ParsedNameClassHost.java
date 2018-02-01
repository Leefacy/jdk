/*    */ package com.sun.xml.internal.rngom.parse.host;
/*    */ 
/*    */ import com.sun.xml.internal.rngom.ast.om.ParsedNameClass;
/*    */ 
/*    */ final class ParsedNameClassHost
/*    */   implements ParsedNameClass
/*    */ {
/*    */   final ParsedNameClass lhs;
/*    */   final ParsedNameClass rhs;
/*    */ 
/*    */   ParsedNameClassHost(ParsedNameClass lhs, ParsedNameClass rhs)
/*    */   {
/* 60 */     this.lhs = lhs;
/* 61 */     this.rhs = rhs;
/*    */   }
/*    */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.xml.internal.rngom.parse.host.ParsedNameClassHost
 * JD-Core Version:    0.6.2
 */