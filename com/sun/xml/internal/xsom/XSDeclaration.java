package com.sun.xml.internal.xsom;

public abstract interface XSDeclaration extends XSComponent
{
  public abstract String getTargetNamespace();

  public abstract String getName();

  /** @deprecated */
  public abstract boolean isAnonymous();

  public abstract boolean isGlobal();

  public abstract boolean isLocal();
}

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.xml.internal.xsom.XSDeclaration
 * JD-Core Version:    0.6.2
 */