package com.sun.tools.internal.xjc.generator.annotation.spec;

import com.sun.codemodel.internal.JAnnotationWriter;
import com.sun.codemodel.internal.JType;
import javax.xml.bind.annotation.XmlAnyElement;

public abstract interface XmlAnyElementWriter extends JAnnotationWriter<XmlAnyElement>
{
  public abstract XmlAnyElementWriter value(Class paramClass);

  public abstract XmlAnyElementWriter value(JType paramJType);

  public abstract XmlAnyElementWriter lax(boolean paramBoolean);
}

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.internal.xjc.generator.annotation.spec.XmlAnyElementWriter
 * JD-Core Version:    0.6.2
 */