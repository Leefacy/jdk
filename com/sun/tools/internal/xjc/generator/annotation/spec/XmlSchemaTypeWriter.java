package com.sun.tools.internal.xjc.generator.annotation.spec;

import com.sun.codemodel.internal.JAnnotationWriter;
import com.sun.codemodel.internal.JType;
import javax.xml.bind.annotation.XmlSchemaType;

public abstract interface XmlSchemaTypeWriter extends JAnnotationWriter<XmlSchemaType>
{
  public abstract XmlSchemaTypeWriter name(String paramString);

  public abstract XmlSchemaTypeWriter type(Class paramClass);

  public abstract XmlSchemaTypeWriter type(JType paramJType);

  public abstract XmlSchemaTypeWriter namespace(String paramString);
}

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.internal.xjc.generator.annotation.spec.XmlSchemaTypeWriter
 * JD-Core Version:    0.6.2
 */