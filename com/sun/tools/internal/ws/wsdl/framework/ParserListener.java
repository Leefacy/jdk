package com.sun.tools.internal.ws.wsdl.framework;

import javax.xml.namespace.QName;

public abstract interface ParserListener
{
  public abstract void ignoringExtension(Entity paramEntity, QName paramQName1, QName paramQName2);

  public abstract void doneParsingEntity(QName paramQName, Entity paramEntity);
}

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.internal.ws.wsdl.framework.ParserListener
 * JD-Core Version:    0.6.2
 */