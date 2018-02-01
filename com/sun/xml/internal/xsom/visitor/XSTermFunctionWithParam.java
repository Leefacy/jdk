package com.sun.xml.internal.xsom.visitor;

import com.sun.xml.internal.xsom.XSElementDecl;
import com.sun.xml.internal.xsom.XSModelGroup;
import com.sun.xml.internal.xsom.XSModelGroupDecl;
import com.sun.xml.internal.xsom.XSWildcard;

public abstract interface XSTermFunctionWithParam<T, P>
{
  public abstract T wildcard(XSWildcard paramXSWildcard, P paramP);

  public abstract T modelGroupDecl(XSModelGroupDecl paramXSModelGroupDecl, P paramP);

  public abstract T modelGroup(XSModelGroup paramXSModelGroup, P paramP);

  public abstract T elementDecl(XSElementDecl paramXSElementDecl, P paramP);
}

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.xml.internal.xsom.visitor.XSTermFunctionWithParam
 * JD-Core Version:    0.6.2
 */