package com.sun.jdi;

import jdk.Exported;

@Exported
public abstract interface Mirror
{
  public abstract VirtualMachine virtualMachine();

  public abstract String toString();
}

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.jdi.Mirror
 * JD-Core Version:    0.6.2
 */