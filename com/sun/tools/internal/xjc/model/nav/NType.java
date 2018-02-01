package com.sun.tools.internal.xjc.model.nav;

import com.sun.codemodel.internal.JType;
import com.sun.tools.internal.xjc.outline.Aspect;
import com.sun.tools.internal.xjc.outline.Outline;

public abstract interface NType
{
  public abstract JType toType(Outline paramOutline, Aspect paramAspect);

  public abstract boolean isBoxedType();

  public abstract String fullName();
}

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.internal.xjc.model.nav.NType
 * JD-Core Version:    0.6.2
 */