package com.sun.tools.internal.ws.api.wsdl;

import org.w3c.dom.Element;
import org.xml.sax.Locator;

/** @deprecated */
public abstract interface TWSDLParserContext
{
  public abstract void push();

  public abstract void pop();

  public abstract String getNamespaceURI(String paramString);

  public abstract Iterable<String> getPrefixes();

  public abstract String getDefaultNamespaceURI();

  public abstract void registerNamespaces(Element paramElement);

  public abstract Locator getLocation(Element paramElement);
}

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.internal.ws.api.wsdl.TWSDLParserContext
 * JD-Core Version:    0.6.2
 */