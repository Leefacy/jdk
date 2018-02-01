package com.sun.tools.internal.xjc.generator.bean;

import com.sun.codemodel.internal.JDefinedClass;
import com.sun.tools.internal.xjc.model.CElementInfo;

public abstract class ObjectFactoryGenerator
{
  abstract void populate(CElementInfo paramCElementInfo);

  abstract void populate(ClassOutlineImpl paramClassOutlineImpl);

  public abstract JDefinedClass getObjectFactory();
}

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.internal.xjc.generator.bean.ObjectFactoryGenerator
 * JD-Core Version:    0.6.2
 */