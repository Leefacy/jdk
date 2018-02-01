package com.sun.tools.internal.xjc.generator.annotation.spec;

import com.sun.codemodel.internal.JAnnotationWriter;
import com.sun.codemodel.internal.JType;
import javax.xml.bind.annotation.XmlType;

public abstract interface XmlTypeWriter extends JAnnotationWriter<XmlType>
{
  public abstract XmlTypeWriter name(String paramString);

  public abstract XmlTypeWriter namespace(String paramString);

  public abstract XmlTypeWriter propOrder(String paramString);

  public abstract XmlTypeWriter factoryClass(Class paramClass);

  public abstract XmlTypeWriter factoryClass(JType paramJType);

  public abstract XmlTypeWriter factoryMethod(String paramString);
}

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.internal.xjc.generator.annotation.spec.XmlTypeWriter
 * JD-Core Version:    0.6.2
 */