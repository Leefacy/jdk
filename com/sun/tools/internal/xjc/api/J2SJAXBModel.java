package com.sun.tools.internal.xjc.api;

import java.io.IOException;
import javax.xml.bind.SchemaOutputResolver;
import javax.xml.namespace.QName;
import javax.xml.transform.Result;

public abstract interface J2SJAXBModel extends JAXBModel
{
  public abstract QName getXmlTypeName(Reference paramReference);

  public abstract void generateSchema(SchemaOutputResolver paramSchemaOutputResolver, ErrorListener paramErrorListener)
    throws IOException;

  public abstract void generateEpisodeFile(Result paramResult);
}

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.internal.xjc.api.J2SJAXBModel
 * JD-Core Version:    0.6.2
 */