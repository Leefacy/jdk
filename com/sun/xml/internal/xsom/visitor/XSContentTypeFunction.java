package com.sun.xml.internal.xsom.visitor;

import com.sun.xml.internal.xsom.XSContentType;
import com.sun.xml.internal.xsom.XSParticle;
import com.sun.xml.internal.xsom.XSSimpleType;

public abstract interface XSContentTypeFunction<T>
{
  public abstract T simpleType(XSSimpleType paramXSSimpleType);

  public abstract T particle(XSParticle paramXSParticle);

  public abstract T empty(XSContentType paramXSContentType);
}

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.xml.internal.xsom.visitor.XSContentTypeFunction
 * JD-Core Version:    0.6.2
 */