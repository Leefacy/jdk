package com.sun.tools.internal.ws.api.wsdl;

import com.sun.codemodel.internal.JClass;
import java.util.Map;

/** @deprecated */
public abstract interface TWSDLOperation extends TWSDLExtensible
{
  public abstract Map<String, JClass> getFaults();
}

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.internal.ws.api.wsdl.TWSDLOperation
 * JD-Core Version:    0.6.2
 */