package com.sun.tools.internal.xjc.model;

import com.sun.codemodel.internal.JExpression;
import com.sun.tools.internal.xjc.outline.Outline;
import com.sun.xml.internal.bind.v2.model.core.ID;
import com.sun.xml.internal.xsom.XmlString;
import javax.activation.MimeType;

public abstract interface TypeUse
{
  public abstract boolean isCollection();

  public abstract CAdapter getAdapterUse();

  public abstract CNonElement getInfo();

  public abstract ID idUse();

  public abstract MimeType getExpectedMimeType();

  public abstract JExpression createConstant(Outline paramOutline, XmlString paramXmlString);
}

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.internal.xjc.model.TypeUse
 * JD-Core Version:    0.6.2
 */