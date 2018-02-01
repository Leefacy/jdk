package com.sun.xml.internal.xsom.visitor;

import com.sun.xml.internal.xsom.XSWildcard.Any;
import com.sun.xml.internal.xsom.XSWildcard.Other;
import com.sun.xml.internal.xsom.XSWildcard.Union;

public abstract interface XSWildcardFunction<T>
{
  public abstract T any(XSWildcard.Any paramAny);

  public abstract T other(XSWildcard.Other paramOther);

  public abstract T union(XSWildcard.Union paramUnion);
}

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.xml.internal.xsom.visitor.XSWildcardFunction
 * JD-Core Version:    0.6.2
 */