/*    */ package com.sun.tools.javadoc;
/*    */ 
/*    */ import com.sun.javadoc.MemberDoc;
/*    */ import com.sun.source.util.TreePath;
/*    */ import com.sun.tools.javac.code.Symbol;
/*    */ 
/*    */ public abstract class MemberDocImpl extends ProgramElementDocImpl
/*    */   implements MemberDoc
/*    */ {
/*    */   public MemberDocImpl(DocEnv paramDocEnv, Symbol paramSymbol, TreePath paramTreePath)
/*    */   {
/* 60 */     super(paramDocEnv, paramSymbol, paramTreePath);
/*    */   }
/*    */ 
/*    */   public abstract boolean isSynthetic();
/*    */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.javadoc.MemberDocImpl
 * JD-Core Version:    0.6.2
 */