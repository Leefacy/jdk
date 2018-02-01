package com.sun.xml.internal.xsom;

public abstract interface XSAttributeUse extends XSComponent
{
  public abstract boolean isRequired();

  public abstract XSAttributeDecl getDecl();

  public abstract XmlString getDefaultValue();

  public abstract XmlString getFixedValue();
}

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.xml.internal.xsom.XSAttributeUse
 * JD-Core Version:    0.6.2
 */