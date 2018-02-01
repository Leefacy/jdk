package com.sun.tools.jdi;

import java.util.EventListener;

abstract interface VMListener extends EventListener
{
  public abstract boolean vmSuspended(VMAction paramVMAction);

  public abstract boolean vmNotSuspended(VMAction paramVMAction);
}

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.jdi.VMListener
 * JD-Core Version:    0.6.2
 */