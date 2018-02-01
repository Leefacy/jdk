/*    */ package com.sun.tools.internal.xjc.reader.relaxng;
/*    */ 
/*    */ import com.sun.xml.internal.rngom.digested.DDefine;
/*    */ import com.sun.xml.internal.rngom.digested.DGrammarPattern;
/*    */ import com.sun.xml.internal.rngom.digested.DPattern;
/*    */ import com.sun.xml.internal.rngom.digested.DPatternWalker;
/*    */ import com.sun.xml.internal.rngom.digested.DRefPattern;
/*    */ import java.util.HashSet;
/*    */ import java.util.Set;
/*    */ 
/*    */ final class DefineFinder extends DPatternWalker
/*    */ {
/* 43 */   public final Set<DDefine> defs = new HashSet();
/*    */ 
/*    */   public Void onGrammar(DGrammarPattern p) {
/* 46 */     for (DDefine def : p) {
/* 47 */       this.defs.add(def);
/* 48 */       def.getPattern().accept(this);
/*    */     }
/*    */ 
/* 51 */     return (Void)p.getStart().accept(this);
/*    */   }
/*    */ 
/*    */   public Void onRef(DRefPattern p)
/*    */   {
/* 59 */     return null;
/*    */   }
/*    */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.internal.xjc.reader.relaxng.DefineFinder
 * JD-Core Version:    0.6.2
 */