package com.sun.xml.internal.xsom.parser;

import java.io.IOException;
import org.xml.sax.ContentHandler;
import org.xml.sax.EntityResolver;
import org.xml.sax.ErrorHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public abstract interface XMLParser
{
  public abstract void parse(InputSource paramInputSource, ContentHandler paramContentHandler, ErrorHandler paramErrorHandler, EntityResolver paramEntityResolver)
    throws SAXException, IOException;
}

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.xml.internal.xsom.parser.XMLParser
 * JD-Core Version:    0.6.2
 */