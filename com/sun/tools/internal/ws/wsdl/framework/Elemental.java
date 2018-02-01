package com.sun.tools.internal.ws.wsdl.framework;

import javax.xml.namespace.QName;
import org.xml.sax.Locator;

public abstract interface Elemental
{
  public abstract QName getElementName();

  public abstract Locator getLocator();
}

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.internal.ws.wsdl.framework.Elemental
 * JD-Core Version:    0.6.2
 */