package com.sun.tools.internal.xjc.generator.annotation.spec;

import com.sun.codemodel.internal.JAnnotationWriter;
import javax.xml.bind.annotation.XmlNs;

public abstract interface XmlNsWriter extends JAnnotationWriter<XmlNs>
{
  public abstract XmlNsWriter prefix(String paramString);

  public abstract XmlNsWriter namespaceURI(String paramString);
}

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.internal.xjc.generator.annotation.spec.XmlNsWriter
 * JD-Core Version:    0.6.2
 */