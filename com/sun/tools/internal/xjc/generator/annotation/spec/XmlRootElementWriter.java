package com.sun.tools.internal.xjc.generator.annotation.spec;

import com.sun.codemodel.internal.JAnnotationWriter;
import javax.xml.bind.annotation.XmlRootElement;

public abstract interface XmlRootElementWriter extends JAnnotationWriter<XmlRootElement>
{
  public abstract XmlRootElementWriter name(String paramString);

  public abstract XmlRootElementWriter namespace(String paramString);
}

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.internal.xjc.generator.annotation.spec.XmlRootElementWriter
 * JD-Core Version:    0.6.2
 */