package com.sun.tools.internal.xjc.generator.annotation.spec;

import com.sun.codemodel.internal.JAnnotationWriter;
import com.sun.codemodel.internal.JType;
import javax.xml.bind.annotation.XmlElementDecl;

public abstract interface XmlElementDeclWriter extends JAnnotationWriter<XmlElementDecl>
{
  public abstract XmlElementDeclWriter name(String paramString);

  public abstract XmlElementDeclWriter scope(Class paramClass);

  public abstract XmlElementDeclWriter scope(JType paramJType);

  public abstract XmlElementDeclWriter namespace(String paramString);

  public abstract XmlElementDeclWriter defaultValue(String paramString);

  public abstract XmlElementDeclWriter substitutionHeadNamespace(String paramString);

  public abstract XmlElementDeclWriter substitutionHeadName(String paramString);
}

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.internal.xjc.generator.annotation.spec.XmlElementDeclWriter
 * JD-Core Version:    0.6.2
 */