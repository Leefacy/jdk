package com.sun.tools.internal.xjc.api;

import java.util.List;
import javax.xml.namespace.QName;

public abstract interface Mapping
{
  public abstract QName getElement();

  public abstract TypeAndAnnotation getType();

  public abstract List<? extends Property> getWrapperStyleDrilldown();
}

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.internal.xjc.api.Mapping
 * JD-Core Version:    0.6.2
 */