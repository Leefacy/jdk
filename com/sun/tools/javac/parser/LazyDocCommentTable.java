/*    */ package com.sun.tools.javac.parser;
/*    */ 
/*    */ import com.sun.tools.javac.tree.DCTree.DCDocComment;
/*    */ import com.sun.tools.javac.tree.DocCommentTable;
/*    */ import com.sun.tools.javac.tree.JCTree;
/*    */ import com.sun.tools.javac.util.DiagnosticSource;
/*    */ import com.sun.tools.javac.util.Log;
/*    */ import java.util.HashMap;
/*    */ import java.util.Map;
/*    */ 
/*    */ public class LazyDocCommentTable
/*    */   implements DocCommentTable
/*    */ {
/*    */   ParserFactory fac;
/*    */   DiagnosticSource diagSource;
/*    */   Map<JCTree, Entry> table;
/*    */ 
/*    */   LazyDocCommentTable(ParserFactory paramParserFactory)
/*    */   {
/* 60 */     this.fac = paramParserFactory;
/* 61 */     this.diagSource = paramParserFactory.log.currentSource();
/* 62 */     this.table = new HashMap();
/*    */   }
/*    */ 
/*    */   public boolean hasComment(JCTree paramJCTree) {
/* 66 */     return this.table.containsKey(paramJCTree);
/*    */   }
/*    */ 
/*    */   public Tokens.Comment getComment(JCTree paramJCTree) {
/* 70 */     Entry localEntry = (Entry)this.table.get(paramJCTree);
/* 71 */     return localEntry == null ? null : localEntry.comment;
/*    */   }
/*    */ 
/*    */   public String getCommentText(JCTree paramJCTree) {
/* 75 */     Tokens.Comment localComment = getComment(paramJCTree);
/* 76 */     return localComment == null ? null : localComment.getText();
/*    */   }
/*    */ 
/*    */   public DCTree.DCDocComment getCommentTree(JCTree paramJCTree) {
/* 80 */     Entry localEntry = (Entry)this.table.get(paramJCTree);
/* 81 */     if (localEntry == null)
/* 82 */       return null;
/* 83 */     if (localEntry.tree == null)
/* 84 */       localEntry.tree = new DocCommentParser(this.fac, this.diagSource, localEntry.comment).parse();
/* 85 */     return localEntry.tree;
/*    */   }
/*    */ 
/*    */   public void putComment(JCTree paramJCTree, Tokens.Comment paramComment) {
/* 89 */     this.table.put(paramJCTree, new Entry(paramComment));
/*    */   }
/*    */ 
/*    */   private static class Entry
/*    */   {
/*    */     final Tokens.Comment comment;
/*    */     DCTree.DCDocComment tree;
/*    */ 
/*    */     Entry(Tokens.Comment paramComment)
/*    */     {
/* 51 */       this.comment = paramComment;
/*    */     }
/*    */   }
/*    */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.javac.parser.LazyDocCommentTable
 * JD-Core Version:    0.6.2
 */