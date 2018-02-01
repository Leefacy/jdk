package com.sun.tools.internal.xjc.generator.annotation.spec;

import com.sun.codemodel.internal.JAnnotationWriter;
import com.sun.codemodel.internal.JType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

public abstract interface XmlJavaTypeAdapterWriter extends JAnnotationWriter<XmlJavaTypeAdapter>
{
  public abstract XmlJavaTypeAdapterWriter type(Class paramClass);

  public abstract XmlJavaTypeAdapterWriter type(JType paramJType);

  public abstract XmlJavaTypeAdapterWriter value(Class paramClass);

  public abstract XmlJavaTypeAdapterWriter value(JType paramJType);
}

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.internal.xjc.generator.annotation.spec.XmlJavaTypeAdapterWriter
 * JD-Core Version:    0.6.2
 */