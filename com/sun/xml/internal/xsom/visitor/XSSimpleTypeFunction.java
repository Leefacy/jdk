package com.sun.xml.internal.xsom.visitor;

import com.sun.xml.internal.xsom.XSListSimpleType;
import com.sun.xml.internal.xsom.XSRestrictionSimpleType;
import com.sun.xml.internal.xsom.XSUnionSimpleType;

public abstract interface XSSimpleTypeFunction<T>
{
  public abstract T listSimpleType(XSListSimpleType paramXSListSimpleType);

  public abstract T unionSimpleType(XSUnionSimpleType paramXSUnionSimpleType);

  public abstract T restrictionSimpleType(XSRestrictionSimpleType paramXSRestrictionSimpleType);
}

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.xml.internal.xsom.visitor.XSSimpleTypeFunction
 * JD-Core Version:    0.6.2
 */