/*    */ package com.sun.xml.internal.rngom.parse.host;
/*    */ 
/*    */ import com.sun.xml.internal.rngom.ast.om.ParsedPattern;
/*    */ 
/*    */ public class ParsedPatternHost
/*    */   implements ParsedPattern
/*    */ {
/*    */   public final ParsedPattern lhs;
/*    */   public final ParsedPattern rhs;
/*    */ 
/*    */   ParsedPatternHost(ParsedPattern lhs, ParsedPattern rhs)
/*    */   {
/* 60 */     this.lhs = lhs;
/* 61 */     this.rhs = rhs;
/*    */   }
/*    */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.xml.internal.rngom.parse.host.ParsedPatternHost
 * JD-Core Version:    0.6.2
 */