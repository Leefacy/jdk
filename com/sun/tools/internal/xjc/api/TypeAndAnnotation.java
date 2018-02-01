package com.sun.tools.internal.xjc.api;

import com.sun.codemodel.internal.JAnnotatable;
import com.sun.codemodel.internal.JType;

public abstract interface TypeAndAnnotation
{
  public abstract JType getTypeClass();

  public abstract void annotate(JAnnotatable paramJAnnotatable);

  public abstract boolean equals(Object paramObject);
}

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.internal.xjc.api.TypeAndAnnotation
 * JD-Core Version:    0.6.2
 */