package com.sun.tools.internal.xjc.generator.annotation.spec;

import com.sun.codemodel.internal.JAnnotationWriter;
import com.sun.codemodel.internal.JType;
import javax.xml.bind.annotation.XmlElementRef;

public abstract interface XmlElementRefWriter extends JAnnotationWriter<XmlElementRef>
{
  public abstract XmlElementRefWriter name(String paramString);

  public abstract XmlElementRefWriter type(Class paramClass);

  public abstract XmlElementRefWriter type(JType paramJType);

  public abstract XmlElementRefWriter namespace(String paramString);

  public abstract XmlElementRefWriter required(boolean paramBoolean);
}

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.internal.xjc.generator.annotation.spec.XmlElementRefWriter
 * JD-Core Version:    0.6.2
 */