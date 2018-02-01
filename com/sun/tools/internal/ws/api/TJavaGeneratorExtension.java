package com.sun.tools.internal.ws.api;

import com.sun.codemodel.internal.JMethod;
import com.sun.tools.internal.ws.api.wsdl.TWSDLOperation;

/** @deprecated */
public abstract class TJavaGeneratorExtension
{
  public abstract void writeMethodAnnotations(TWSDLOperation paramTWSDLOperation, JMethod paramJMethod);
}

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.internal.ws.api.TJavaGeneratorExtension
 * JD-Core Version:    0.6.2
 */