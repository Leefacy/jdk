package com.sun.tools.internal.xjc.outline;

import com.sun.codemodel.internal.JExpression;
import com.sun.codemodel.internal.JType;
import com.sun.tools.internal.xjc.model.CPropertyInfo;

public abstract interface FieldOutline
{
  public abstract ClassOutline parent();

  public abstract CPropertyInfo getPropertyInfo();

  public abstract JType getRawType();

  public abstract FieldAccessor create(JExpression paramJExpression);
}

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.internal.xjc.outline.FieldOutline
 * JD-Core Version:    0.6.2
 */