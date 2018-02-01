package com.sun.tools.example.debug.tty;

import com.sun.jdi.ReferenceType;
import com.sun.jdi.request.ClassPrepareRequest;

abstract interface ReferenceTypeSpec
{
  public abstract boolean matches(ReferenceType paramReferenceType);

  public abstract ClassPrepareRequest createPrepareRequest();

  public abstract int hashCode();

  public abstract boolean equals(Object paramObject);
}

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.example.debug.tty.ReferenceTypeSpec
 * JD-Core Version:    0.6.2
 */