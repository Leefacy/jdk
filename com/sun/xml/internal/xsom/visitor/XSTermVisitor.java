package com.sun.xml.internal.xsom.visitor;

import com.sun.xml.internal.xsom.XSElementDecl;
import com.sun.xml.internal.xsom.XSModelGroup;
import com.sun.xml.internal.xsom.XSModelGroupDecl;
import com.sun.xml.internal.xsom.XSWildcard;

public abstract interface XSTermVisitor
{
  public abstract void wildcard(XSWildcard paramXSWildcard);

  public abstract void modelGroupDecl(XSModelGroupDecl paramXSModelGroupDecl);

  public abstract void modelGroup(XSModelGroup paramXSModelGroup);

  public abstract void elementDecl(XSElementDecl paramXSElementDecl);
}

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.xml.internal.xsom.visitor.XSTermVisitor
 * JD-Core Version:    0.6.2
 */