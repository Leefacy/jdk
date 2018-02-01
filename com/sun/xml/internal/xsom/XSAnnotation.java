package com.sun.xml.internal.xsom;

import org.xml.sax.Locator;

public abstract interface XSAnnotation
{
  public abstract Object getAnnotation();

  public abstract Object setAnnotation(Object paramObject);

  public abstract Locator getLocator();
}

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.xml.internal.xsom.XSAnnotation
 * JD-Core Version:    0.6.2
 */