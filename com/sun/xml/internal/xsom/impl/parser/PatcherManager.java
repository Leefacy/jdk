package com.sun.xml.internal.xsom.impl.parser;

import org.xml.sax.Locator;
import org.xml.sax.SAXException;

public abstract interface PatcherManager
{
  public abstract void addPatcher(Patch paramPatch);

  public abstract void addErrorChecker(Patch paramPatch);

  public abstract void reportError(String paramString, Locator paramLocator)
    throws SAXException;

  public static abstract interface Patcher
  {
    public abstract void run()
      throws SAXException;
  }
}

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.xml.internal.xsom.impl.parser.PatcherManager
 * JD-Core Version:    0.6.2
 */