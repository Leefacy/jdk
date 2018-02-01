package com.sun.tools.internal.xjc.generator.annotation.spec;

import com.sun.codemodel.internal.JAnnotationWriter;
import javax.xml.bind.annotation.XmlSchemaTypes;

public abstract interface XmlSchemaTypesWriter extends JAnnotationWriter<XmlSchemaTypes>
{
  public abstract XmlSchemaTypeWriter value();
}

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.internal.xjc.generator.annotation.spec.XmlSchemaTypesWriter
 * JD-Core Version:    0.6.2
 */