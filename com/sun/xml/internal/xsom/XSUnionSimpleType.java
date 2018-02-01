package com.sun.xml.internal.xsom;

public abstract interface XSUnionSimpleType extends XSSimpleType, Iterable<XSSimpleType>
{
  public abstract XSSimpleType getMember(int paramInt);

  public abstract int getMemberSize();
}

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.xml.internal.xsom.XSUnionSimpleType
 * JD-Core Version:    0.6.2
 */