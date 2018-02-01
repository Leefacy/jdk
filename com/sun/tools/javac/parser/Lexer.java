package com.sun.tools.javac.parser;

import com.sun.tools.javac.util.Position.LineMap;

public abstract interface Lexer
{
  public abstract void nextToken();

  public abstract Tokens.Token token();

  public abstract Tokens.Token token(int paramInt);

  public abstract Tokens.Token prevToken();

  public abstract Tokens.Token split();

  public abstract int errPos();

  public abstract void errPos(int paramInt);

  public abstract Position.LineMap getLineMap();
}

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.javac.parser.Lexer
 * JD-Core Version:    0.6.2
 */