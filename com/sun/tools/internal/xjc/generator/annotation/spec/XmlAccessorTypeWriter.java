package com.sun.tools.internal.xjc.generator.annotation.spec;

import com.sun.codemodel.internal.JAnnotationWriter;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;

public abstract interface XmlAccessorTypeWriter extends JAnnotationWriter<XmlAccessorType>
{
  public abstract XmlAccessorTypeWriter value(XmlAccessType paramXmlAccessType);
}

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.internal.xjc.generator.annotation.spec.XmlAccessorTypeWriter
 * JD-Core Version:    0.6.2
 */