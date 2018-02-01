package com.sun.xml.internal.rngom.xml.sax;

import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

public abstract interface XMLReaderCreator
{
  public abstract XMLReader createXMLReader()
    throws SAXException;
}

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.xml.internal.rngom.xml.sax.XMLReaderCreator
 * JD-Core Version:    0.6.2
 */