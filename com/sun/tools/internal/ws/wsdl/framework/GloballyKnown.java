package com.sun.tools.internal.ws.wsdl.framework;

public abstract interface GloballyKnown extends Elemental
{
  public abstract String getName();

  public abstract Kind getKind();

  public abstract Defining getDefining();
}

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.internal.ws.wsdl.framework.GloballyKnown
 * JD-Core Version:    0.6.2
 */