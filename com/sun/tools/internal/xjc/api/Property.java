package com.sun.tools.internal.xjc.api;

import com.sun.codemodel.internal.JType;
import javax.xml.namespace.QName;

public abstract interface Property
{
  public abstract String name();

  public abstract JType type();

  public abstract QName elementName();

  public abstract QName rawName();
}

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.internal.xjc.api.Property
 * JD-Core Version:    0.6.2
 */