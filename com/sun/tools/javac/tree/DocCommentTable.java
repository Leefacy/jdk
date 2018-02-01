package com.sun.tools.javac.tree;

import com.sun.tools.javac.parser.Tokens.Comment;

public abstract interface DocCommentTable
{
  public abstract boolean hasComment(JCTree paramJCTree);

  public abstract Tokens.Comment getComment(JCTree paramJCTree);

  public abstract String getCommentText(JCTree paramJCTree);

  public abstract DCTree.DCDocComment getCommentTree(JCTree paramJCTree);

  public abstract void putComment(JCTree paramJCTree, Tokens.Comment paramComment);
}

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.javac.tree.DocCommentTable
 * JD-Core Version:    0.6.2
 */