package com.sun.tools.internal.xjc.generator.annotation.spec;

import com.sun.codemodel.internal.JAnnotationWriter;
import javax.xml.bind.annotation.XmlElements;

public abstract interface XmlElementsWriter extends JAnnotationWriter<XmlElements>
{
  public abstract XmlElementWriter value();
}

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.internal.xjc.generator.annotation.spec.XmlElementsWriter
 * JD-Core Version:    0.6.2
 */