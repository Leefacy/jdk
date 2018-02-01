package com.sun.jdi;

import jdk.Exported;

@Exported
public abstract interface Accessible
{
  public abstract int modifiers();

  public abstract boolean isPrivate();

  public abstract boolean isPackagePrivate();

  public abstract boolean isProtected();

  public abstract boolean isPublic();
}

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.jdi.Accessible
 * JD-Core Version:    0.6.2
 */