package com.sun.jdi;

import jdk.Exported;

@Exported
public abstract interface TypeComponent extends Mirror, Accessible
{
  public abstract String name();

  public abstract String signature();

  public abstract String genericSignature();

  public abstract ReferenceType declaringType();

  public abstract boolean isStatic();

  public abstract boolean isFinal();

  public abstract boolean isSynthetic();
}

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.jdi.TypeComponent
 * JD-Core Version:    0.6.2
 */