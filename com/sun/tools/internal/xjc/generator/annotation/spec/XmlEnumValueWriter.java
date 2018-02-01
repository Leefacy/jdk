package com.sun.tools.internal.xjc.generator.annotation.spec;

import com.sun.codemodel.internal.JAnnotationWriter;
import javax.xml.bind.annotation.XmlEnumValue;

public abstract interface XmlEnumValueWriter extends JAnnotationWriter<XmlEnumValue>
{
  public abstract XmlEnumValueWriter value(String paramString);
}

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.internal.xjc.generator.annotation.spec.XmlEnumValueWriter
 * JD-Core Version:    0.6.2
 */