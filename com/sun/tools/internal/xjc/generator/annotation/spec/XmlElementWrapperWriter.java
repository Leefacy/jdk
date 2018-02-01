package com.sun.tools.internal.xjc.generator.annotation.spec;

import com.sun.codemodel.internal.JAnnotationWriter;
import javax.xml.bind.annotation.XmlElementWrapper;

public abstract interface XmlElementWrapperWriter extends JAnnotationWriter<XmlElementWrapper>
{
  public abstract XmlElementWrapperWriter name(String paramString);

  public abstract XmlElementWrapperWriter namespace(String paramString);

  public abstract XmlElementWrapperWriter required(boolean paramBoolean);

  public abstract XmlElementWrapperWriter nillable(boolean paramBoolean);
}

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.internal.xjc.generator.annotation.spec.XmlElementWrapperWriter
 * JD-Core Version:    0.6.2
 */