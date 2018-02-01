package com.sun.tools.internal.xjc.api;

import com.sun.istack.internal.NotNull;
import com.sun.tools.internal.xjc.Options;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import org.w3c.dom.Element;
import org.xml.sax.ContentHandler;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;

public abstract interface SchemaCompiler
{
  public abstract ContentHandler getParserHandler(String paramString);

  public abstract void parseSchema(InputSource paramInputSource);

  public abstract void setTargetVersion(SpecVersion paramSpecVersion);

  public abstract void parseSchema(String paramString, Element paramElement);

  public abstract void parseSchema(String paramString, XMLStreamReader paramXMLStreamReader)
    throws XMLStreamException;

  public abstract void setErrorListener(ErrorListener paramErrorListener);

  public abstract void setEntityResolver(EntityResolver paramEntityResolver);

  public abstract void setDefaultPackageName(String paramString);

  public abstract void forcePackageName(String paramString);

  public abstract void setClassNameAllocator(ClassNameAllocator paramClassNameAllocator);

  public abstract void resetSchema();

  public abstract S2JJAXBModel bind();

  /** @deprecated */
  @NotNull
  public abstract Options getOptions();
}

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.internal.xjc.api.SchemaCompiler
 * JD-Core Version:    0.6.2
 */