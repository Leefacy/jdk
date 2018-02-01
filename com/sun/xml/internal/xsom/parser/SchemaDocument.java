package com.sun.xml.internal.xsom.parser;

import com.sun.xml.internal.xsom.XSSchema;
import java.util.Set;

public abstract interface SchemaDocument
{
  public abstract String getSystemId();

  public abstract String getTargetNamespace();

  public abstract XSSchema getSchema();

  public abstract Set<SchemaDocument> getReferencedDocuments();

  public abstract Set<SchemaDocument> getIncludedDocuments();

  public abstract Set<SchemaDocument> getImportedDocuments(String paramString);

  public abstract boolean includes(SchemaDocument paramSchemaDocument);

  public abstract boolean imports(SchemaDocument paramSchemaDocument);

  public abstract Set<SchemaDocument> getReferers();
}

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.xml.internal.xsom.parser.SchemaDocument
 * JD-Core Version:    0.6.2
 */