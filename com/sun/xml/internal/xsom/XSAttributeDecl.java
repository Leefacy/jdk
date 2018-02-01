package com.sun.xml.internal.xsom;

public abstract interface XSAttributeDecl extends XSDeclaration
{
  public abstract XSSimpleType getType();

  public abstract XmlString getDefaultValue();

  public abstract XmlString getFixedValue();
}

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.xml.internal.xsom.XSAttributeDecl
 * JD-Core Version:    0.6.2
 */