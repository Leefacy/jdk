package com.sun.xml.internal.xsom.visitor;

import com.sun.xml.internal.xsom.XSListSimpleType;
import com.sun.xml.internal.xsom.XSRestrictionSimpleType;
import com.sun.xml.internal.xsom.XSUnionSimpleType;

public abstract interface XSSimpleTypeVisitor
{
  public abstract void listSimpleType(XSListSimpleType paramXSListSimpleType);

  public abstract void unionSimpleType(XSUnionSimpleType paramXSUnionSimpleType);

  public abstract void restrictionSimpleType(XSRestrictionSimpleType paramXSRestrictionSimpleType);
}

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.xml.internal.xsom.visitor.XSSimpleTypeVisitor
 * JD-Core Version:    0.6.2
 */