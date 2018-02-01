package com.sun.tools.internal.xjc.generator.annotation.spec;

import com.sun.codemodel.internal.JAnnotationWriter;
import javax.xml.bind.annotation.XmlAttribute;

public abstract interface XmlAttributeWriter extends JAnnotationWriter<XmlAttribute>
{
  public abstract XmlAttributeWriter name(String paramString);

  public abstract XmlAttributeWriter namespace(String paramString);

  public abstract XmlAttributeWriter required(boolean paramBoolean);
}

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.internal.xjc.generator.annotation.spec.XmlAttributeWriter
 * JD-Core Version:    0.6.2
 */