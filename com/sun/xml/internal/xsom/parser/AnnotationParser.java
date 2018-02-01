package com.sun.xml.internal.xsom.parser;

import org.xml.sax.ContentHandler;
import org.xml.sax.EntityResolver;
import org.xml.sax.ErrorHandler;

public abstract class AnnotationParser
{
  public abstract ContentHandler getContentHandler(AnnotationContext paramAnnotationContext, String paramString, ErrorHandler paramErrorHandler, EntityResolver paramEntityResolver);

  public abstract Object getResult(Object paramObject);
}

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.xml.internal.xsom.parser.AnnotationParser
 * JD-Core Version:    0.6.2
 */