package com.sun.tools.internal.xjc.generator.annotation.spec;

import com.sun.codemodel.internal.JAnnotationWriter;
import com.sun.codemodel.internal.JType;
import javax.xml.bind.annotation.XmlSeeAlso;

public abstract interface XmlSeeAlsoWriter extends JAnnotationWriter<XmlSeeAlso>
{
  public abstract XmlSeeAlsoWriter value(Class paramClass);

  public abstract XmlSeeAlsoWriter value(JType paramJType);
}

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.internal.xjc.generator.annotation.spec.XmlSeeAlsoWriter
 * JD-Core Version:    0.6.2
 */