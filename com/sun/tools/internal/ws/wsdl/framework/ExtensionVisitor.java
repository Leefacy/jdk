package com.sun.tools.internal.ws.wsdl.framework;

import com.sun.tools.internal.ws.api.wsdl.TWSDLExtension;

public abstract interface ExtensionVisitor
{
  public abstract void preVisit(TWSDLExtension paramTWSDLExtension)
    throws Exception;

  public abstract void postVisit(TWSDLExtension paramTWSDLExtension)
    throws Exception;
}

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.internal.ws.wsdl.framework.ExtensionVisitor
 * JD-Core Version:    0.6.2
 */