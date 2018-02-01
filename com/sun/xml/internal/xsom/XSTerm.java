package com.sun.xml.internal.xsom;

import com.sun.xml.internal.xsom.visitor.XSTermFunction;
import com.sun.xml.internal.xsom.visitor.XSTermFunctionWithParam;
import com.sun.xml.internal.xsom.visitor.XSTermVisitor;

public abstract interface XSTerm extends XSComponent
{
  public abstract void visit(XSTermVisitor paramXSTermVisitor);

  public abstract <T> T apply(XSTermFunction<T> paramXSTermFunction);

  public abstract <T, P> T apply(XSTermFunctionWithParam<T, P> paramXSTermFunctionWithParam, P paramP);

  public abstract boolean isWildcard();

  public abstract boolean isModelGroupDecl();

  public abstract boolean isModelGroup();

  public abstract boolean isElementDecl();

  public abstract XSWildcard asWildcard();

  public abstract XSModelGroupDecl asModelGroupDecl();

  public abstract XSModelGroup asModelGroup();

  public abstract XSElementDecl asElementDecl();
}

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.xml.internal.xsom.XSTerm
 * JD-Core Version:    0.6.2
 */