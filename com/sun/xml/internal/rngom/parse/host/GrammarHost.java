/*    */ package com.sun.xml.internal.rngom.parse.host;
/*    */ 
/*    */ import com.sun.xml.internal.rngom.ast.builder.Annotations;
/*    */ import com.sun.xml.internal.rngom.ast.builder.BuildException;
/*    */ import com.sun.xml.internal.rngom.ast.builder.Grammar;
/*    */ import com.sun.xml.internal.rngom.ast.om.Location;
/*    */ import com.sun.xml.internal.rngom.ast.om.ParsedPattern;
/*    */ 
/*    */ public class GrammarHost extends ScopeHost
/*    */   implements Grammar
/*    */ {
/*    */   final Grammar lhs;
/*    */   final Grammar rhs;
/*    */ 
/*    */   public GrammarHost(Grammar lhs, Grammar rhs)
/*    */   {
/* 72 */     super(lhs, rhs);
/* 73 */     this.lhs = lhs;
/* 74 */     this.rhs = rhs;
/*    */   }
/*    */ 
/*    */   public ParsedPattern endGrammar(Location _loc, Annotations _anno) throws BuildException {
/* 78 */     LocationHost loc = cast(_loc);
/* 79 */     AnnotationsHost anno = cast(_anno);
/*    */ 
/* 83 */     return new ParsedPatternHost(this.lhs
/* 82 */       .endGrammar(loc.lhs, anno.lhs), 
/* 82 */       this.rhs
/* 83 */       .endGrammar(loc.rhs, anno.rhs));
/*    */   }
/*    */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.xml.internal.rngom.parse.host.GrammarHost
 * JD-Core Version:    0.6.2
 */