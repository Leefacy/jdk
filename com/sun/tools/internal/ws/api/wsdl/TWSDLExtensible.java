package com.sun.tools.internal.ws.api.wsdl;

import javax.xml.namespace.QName;

/** @deprecated */
public abstract interface TWSDLExtensible
{
  public abstract String getNameValue();

  public abstract String getNamespaceURI();

  public abstract QName getWSDLElementName();

  public abstract void addExtension(TWSDLExtension paramTWSDLExtension);

  public abstract Iterable<? extends TWSDLExtension> extensions();

  public abstract TWSDLExtensible getParent();
}

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.internal.ws.api.wsdl.TWSDLExtensible
 * JD-Core Version:    0.6.2
 */