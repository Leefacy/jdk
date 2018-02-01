package com.sun.tools.internal.xjc.generator.annotation.spec;

import com.sun.codemodel.internal.JAnnotationWriter;
import javax.xml.bind.annotation.XmlAccessOrder;
import javax.xml.bind.annotation.XmlAccessorOrder;

public abstract interface XmlAccessorOrderWriter extends JAnnotationWriter<XmlAccessorOrder>
{
  public abstract XmlAccessorOrderWriter value(XmlAccessOrder paramXmlAccessOrder);
}

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.internal.xjc.generator.annotation.spec.XmlAccessorOrderWriter
 * JD-Core Version:    0.6.2
 */