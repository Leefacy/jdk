package com.sun.xml.internal.xsom;

import org.relaxng.datatype.ValidationContext;
import org.xml.sax.Attributes;
import org.xml.sax.Locator;

public abstract interface ForeignAttributes extends Attributes
{
  public abstract ValidationContext getContext();

  public abstract Locator getLocator();
}

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.xml.internal.xsom.ForeignAttributes
 * JD-Core Version:    0.6.2
 */