package com.sun.tools.internal.xjc.generator.annotation.spec;

import com.sun.codemodel.internal.JAnnotationWriter;
import javax.xml.bind.annotation.XmlNsForm;
import javax.xml.bind.annotation.XmlSchema;

public abstract interface XmlSchemaWriter extends JAnnotationWriter<XmlSchema>
{
  public abstract XmlSchemaWriter location(String paramString);

  public abstract XmlSchemaWriter namespace(String paramString);

  public abstract XmlNsWriter xmlns();

  public abstract XmlSchemaWriter elementFormDefault(XmlNsForm paramXmlNsForm);

  public abstract XmlSchemaWriter attributeFormDefault(XmlNsForm paramXmlNsForm);
}

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.internal.xjc.generator.annotation.spec.XmlSchemaWriter
 * JD-Core Version:    0.6.2
 */