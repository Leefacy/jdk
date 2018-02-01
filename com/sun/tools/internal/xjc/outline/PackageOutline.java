package com.sun.tools.internal.xjc.outline;

import com.sun.codemodel.internal.JDefinedClass;
import com.sun.codemodel.internal.JPackage;
import com.sun.tools.internal.xjc.generator.bean.ObjectFactoryGenerator;
import java.util.Set;
import javax.xml.bind.annotation.XmlNsForm;

public abstract interface PackageOutline
{
  public abstract JPackage _package();

  public abstract JDefinedClass objectFactory();

  public abstract ObjectFactoryGenerator objectFactoryGenerator();

  public abstract Set<? extends ClassOutline> getClasses();

  public abstract String getMostUsedNamespaceURI();

  public abstract XmlNsForm getElementFormDefault();

  public abstract XmlNsForm getAttributeFormDefault();
}

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.internal.xjc.outline.PackageOutline
 * JD-Core Version:    0.6.2
 */