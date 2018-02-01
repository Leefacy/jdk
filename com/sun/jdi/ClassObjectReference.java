package com.sun.jdi;

import jdk.Exported;

@Exported
public abstract interface ClassObjectReference extends ObjectReference
{
  public abstract ReferenceType reflectedType();
}

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.jdi.ClassObjectReference
 * JD-Core Version:    0.6.2
 */