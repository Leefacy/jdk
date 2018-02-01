package com.sun.tools.internal.xjc.model;

public abstract interface CPropertyVisitor<V>
{
  public abstract V onElement(CElementPropertyInfo paramCElementPropertyInfo);

  public abstract V onAttribute(CAttributePropertyInfo paramCAttributePropertyInfo);

  public abstract V onValue(CValuePropertyInfo paramCValuePropertyInfo);

  public abstract V onReference(CReferencePropertyInfo paramCReferencePropertyInfo);
}

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.internal.xjc.model.CPropertyVisitor
 * JD-Core Version:    0.6.2
 */