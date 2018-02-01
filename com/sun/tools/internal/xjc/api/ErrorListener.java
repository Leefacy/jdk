package com.sun.tools.internal.xjc.api;

import org.xml.sax.SAXParseException;

public abstract interface ErrorListener extends com.sun.xml.internal.bind.api.ErrorListener
{
  public abstract void error(SAXParseException paramSAXParseException);

  public abstract void fatalError(SAXParseException paramSAXParseException);

  public abstract void warning(SAXParseException paramSAXParseException);

  public abstract void info(SAXParseException paramSAXParseException);
}

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.internal.xjc.api.ErrorListener
 * JD-Core Version:    0.6.2
 */