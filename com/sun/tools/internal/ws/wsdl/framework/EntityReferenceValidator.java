package com.sun.tools.internal.ws.wsdl.framework;

import javax.xml.namespace.QName;

public abstract interface EntityReferenceValidator
{
  public abstract boolean isValid(Kind paramKind, QName paramQName);
}

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.internal.ws.wsdl.framework.EntityReferenceValidator
 * JD-Core Version:    0.6.2
 */