package com.sun.tools.internal.xjc.outline;

import com.sun.codemodel.internal.JBlock;
import com.sun.codemodel.internal.JExpression;
import com.sun.codemodel.internal.JVar;
import com.sun.tools.internal.xjc.model.CPropertyInfo;

public abstract interface FieldAccessor
{
  public abstract void toRawValue(JBlock paramJBlock, JVar paramJVar);

  public abstract void fromRawValue(JBlock paramJBlock, String paramString, JExpression paramJExpression);

  public abstract void unsetValues(JBlock paramJBlock);

  public abstract JExpression hasSetValue();

  public abstract FieldOutline owner();

  public abstract CPropertyInfo getPropertyInfo();
}

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.internal.xjc.outline.FieldAccessor
 * JD-Core Version:    0.6.2
 */