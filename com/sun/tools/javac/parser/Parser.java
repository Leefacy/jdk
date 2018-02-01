package com.sun.tools.javac.parser;

import com.sun.tools.javac.tree.JCTree.JCCompilationUnit;
import com.sun.tools.javac.tree.JCTree.JCExpression;
import com.sun.tools.javac.tree.JCTree.JCStatement;

public abstract interface Parser
{
  public abstract JCTree.JCCompilationUnit parseCompilationUnit();

  public abstract JCTree.JCExpression parseExpression();

  public abstract JCTree.JCStatement parseStatement();

  public abstract JCTree.JCExpression parseType();
}

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.javac.parser.Parser
 * JD-Core Version:    0.6.2
 */