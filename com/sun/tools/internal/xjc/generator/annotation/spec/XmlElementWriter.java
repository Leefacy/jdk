package com.sun.tools.internal.xjc.generator.annotation.spec;

import com.sun.codemodel.internal.JAnnotationWriter;
import com.sun.codemodel.internal.JType;
import javax.xml.bind.annotation.XmlElement;

public abstract interface XmlElementWriter extends JAnnotationWriter<XmlElement>
{
  public abstract XmlElementWriter name(String paramString);

  public abstract XmlElementWriter type(Class paramClass);

  public abstract XmlElementWriter type(JType paramJType);

  public abstract XmlElementWriter namespace(String paramString);

  public abstract XmlElementWriter defaultValue(String paramString);

  public abstract XmlElementWriter required(boolean paramBoolean);

  public abstract XmlElementWriter nillable(boolean paramBoolean);
}

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.internal.xjc.generator.annotation.spec.XmlElementWriter
 * JD-Core Version:    0.6.2
 */