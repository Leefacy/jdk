package com.sun.xml.internal.xsom.visitor;

import com.sun.xml.internal.xsom.XSElementDecl;
import com.sun.xml.internal.xsom.XSModelGroup;
import com.sun.xml.internal.xsom.XSModelGroupDecl;
import com.sun.xml.internal.xsom.XSWildcard;

public abstract interface XSTermFunction<T>
{
  public abstract T wildcard(XSWildcard paramXSWildcard);

  public abstract T modelGroupDecl(XSModelGroupDecl paramXSModelGroupDecl);

  public abstract T modelGroup(XSModelGroup paramXSModelGroup);

  public abstract T elementDecl(XSElementDecl paramXSElementDecl);
}

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.xml.internal.xsom.visitor.XSTermFunction
 * JD-Core Version:    0.6.2
 */