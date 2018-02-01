package com.sun.tools.internal.xjc.model;

import com.sun.tools.internal.xjc.model.nav.NClass;
import com.sun.tools.internal.xjc.model.nav.NType;
import com.sun.xml.internal.bind.v2.model.core.Element;

public abstract interface CElement extends CTypeInfo, Element<NType, NClass>
{
  public abstract void setAbstract();

  public abstract boolean isAbstract();
}

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.internal.xjc.model.CElement
 * JD-Core Version:    0.6.2
 */