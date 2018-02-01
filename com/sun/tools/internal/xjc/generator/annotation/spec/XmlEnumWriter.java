package com.sun.tools.internal.xjc.generator.annotation.spec;

import com.sun.codemodel.internal.JAnnotationWriter;
import com.sun.codemodel.internal.JType;
import javax.xml.bind.annotation.XmlEnum;

public abstract interface XmlEnumWriter extends JAnnotationWriter<XmlEnum>
{
  public abstract XmlEnumWriter value(Class paramClass);

  public abstract XmlEnumWriter value(JType paramJType);
}

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.internal.xjc.generator.annotation.spec.XmlEnumWriter
 * JD-Core Version:    0.6.2
 */