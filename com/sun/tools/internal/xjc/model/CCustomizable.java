package com.sun.tools.internal.xjc.model;

import com.sun.xml.internal.xsom.XSComponent;
import org.xml.sax.Locator;

public abstract interface CCustomizable
{
  public abstract CCustomizations getCustomizations();

  public abstract Locator getLocator();

  public abstract XSComponent getSchemaComponent();
}

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.internal.xjc.model.CCustomizable
 * JD-Core Version:    0.6.2
 */