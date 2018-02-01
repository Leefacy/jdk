package com.sun.xml.internal.xsom.impl.parser;

import org.xml.sax.SAXException;

public abstract interface Patch
{
  public abstract void run()
    throws SAXException;
}

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.xml.internal.xsom.impl.parser.Patch
 * JD-Core Version:    0.6.2
 */