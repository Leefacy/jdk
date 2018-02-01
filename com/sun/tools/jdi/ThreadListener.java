package com.sun.tools.jdi;

import java.util.EventListener;

abstract interface ThreadListener extends EventListener
{
  public abstract boolean threadResumable(ThreadAction paramThreadAction);
}

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.jdi.ThreadListener
 * JD-Core Version:    0.6.2
 */