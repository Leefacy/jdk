package com.sun.tools.internal.ws.processor.model.exporter;

import org.xml.sax.ContentHandler;

public abstract interface ExternalObject
{
  public abstract String getType();

  public abstract void saveTo(ContentHandler paramContentHandler);
}

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.internal.ws.processor.model.exporter.ExternalObject
 * JD-Core Version:    0.6.2
 */