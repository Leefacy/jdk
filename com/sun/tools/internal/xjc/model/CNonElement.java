package com.sun.tools.internal.xjc.model;

import com.sun.tools.internal.xjc.model.nav.NClass;
import com.sun.tools.internal.xjc.model.nav.NType;
import com.sun.xml.internal.bind.v2.model.core.NonElement;

public abstract interface CNonElement extends NonElement<NType, NClass>, TypeUse, CTypeInfo
{
  @Deprecated
  public abstract CNonElement getInfo();

  @Deprecated
  public abstract boolean isCollection();

  @Deprecated
  public abstract CAdapter getAdapterUse();
}

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.internal.xjc.model.CNonElement
 * JD-Core Version:    0.6.2
 */