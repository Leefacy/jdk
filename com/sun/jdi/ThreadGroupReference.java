package com.sun.jdi;

import java.util.List;
import jdk.Exported;

@Exported
public abstract interface ThreadGroupReference extends ObjectReference
{
  public abstract String name();

  public abstract ThreadGroupReference parent();

  public abstract void suspend();

  public abstract void resume();

  public abstract List<ThreadReference> threads();

  public abstract List<ThreadGroupReference> threadGroups();
}

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.jdi.ThreadGroupReference
 * JD-Core Version:    0.6.2
 */