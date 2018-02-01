package com.sun.tools.jdi;

import com.sun.jdi.ClassNotLoadedException;
import com.sun.jdi.Type;

abstract interface ValueContainer
{
  public abstract Type type()
    throws ClassNotLoadedException;

  public abstract Type findType(String paramString)
    throws ClassNotLoadedException;

  public abstract String typeName();

  public abstract String signature();
}

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.jdi.ValueContainer
 * JD-Core Version:    0.6.2
 */