package com.sun.tools.internal.jxc.gen.config;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

public abstract interface NGCCEventReceiver
{
  public abstract void enterElement(String paramString1, String paramString2, String paramString3, Attributes paramAttributes)
    throws SAXException;

  public abstract void leaveElement(String paramString1, String paramString2, String paramString3)
    throws SAXException;

  public abstract void text(String paramString)
    throws SAXException;

  public abstract void enterAttribute(String paramString1, String paramString2, String paramString3)
    throws SAXException;

  public abstract void leaveAttribute(String paramString1, String paramString2, String paramString3)
    throws SAXException;
}

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.internal.jxc.gen.config.NGCCEventReceiver
 * JD-Core Version:    0.6.2
 */