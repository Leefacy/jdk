package com.sun.xml.internal.xsom.visitor;

import com.sun.xml.internal.xsom.XSContentType;
import com.sun.xml.internal.xsom.XSParticle;
import com.sun.xml.internal.xsom.XSSimpleType;

public abstract interface XSContentTypeVisitor
{
  public abstract void simpleType(XSSimpleType paramXSSimpleType);

  public abstract void particle(XSParticle paramXSParticle);

  public abstract void empty(XSContentType paramXSContentType);
}

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.xml.internal.xsom.visitor.XSContentTypeVisitor
 * JD-Core Version:    0.6.2
 */