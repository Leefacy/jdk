package com.sun.xml.internal.xsom.visitor;

import com.sun.xml.internal.xsom.XSWildcard.Any;
import com.sun.xml.internal.xsom.XSWildcard.Other;
import com.sun.xml.internal.xsom.XSWildcard.Union;

public abstract interface XSWildcardVisitor
{
  public abstract void any(XSWildcard.Any paramAny);

  public abstract void other(XSWildcard.Other paramOther);

  public abstract void union(XSWildcard.Union paramUnion);
}

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.xml.internal.xsom.visitor.XSWildcardVisitor
 * JD-Core Version:    0.6.2
 */