package com.sun.tools.internal.xjc.generator.annotation.spec;

import com.sun.codemodel.internal.JAnnotationWriter;
import javax.xml.bind.annotation.XmlElementRefs;

public abstract interface XmlElementRefsWriter extends JAnnotationWriter<XmlElementRefs>
{
  public abstract XmlElementRefWriter value();
}

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.internal.xjc.generator.annotation.spec.XmlElementRefsWriter
 * JD-Core Version:    0.6.2
 */