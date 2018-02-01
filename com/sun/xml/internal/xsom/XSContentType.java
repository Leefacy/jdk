package com.sun.xml.internal.xsom;

import com.sun.xml.internal.xsom.visitor.XSContentTypeFunction;
import com.sun.xml.internal.xsom.visitor.XSContentTypeVisitor;

public abstract interface XSContentType extends XSComponent
{
  public abstract XSSimpleType asSimpleType();

  public abstract XSParticle asParticle();

  public abstract XSContentType asEmpty();

  public abstract <T> T apply(XSContentTypeFunction<T> paramXSContentTypeFunction);

  public abstract void visit(XSContentTypeVisitor paramXSContentTypeVisitor);
}

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.xml.internal.xsom.XSContentType
 * JD-Core Version:    0.6.2
 */