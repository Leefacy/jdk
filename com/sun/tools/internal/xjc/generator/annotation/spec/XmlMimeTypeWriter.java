package com.sun.tools.internal.xjc.generator.annotation.spec;

import com.sun.codemodel.internal.JAnnotationWriter;
import javax.xml.bind.annotation.XmlMimeType;

public abstract interface XmlMimeTypeWriter extends JAnnotationWriter<XmlMimeType>
{
  public abstract XmlMimeTypeWriter value(String paramString);
}

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.internal.xjc.generator.annotation.spec.XmlMimeTypeWriter
 * JD-Core Version:    0.6.2
 */