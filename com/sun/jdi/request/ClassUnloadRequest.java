package com.sun.jdi.request;

import jdk.Exported;

@Exported
public abstract interface ClassUnloadRequest extends EventRequest
{
  public abstract void addClassFilter(String paramString);

  public abstract void addClassExclusionFilter(String paramString);
}

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.jdi.request.ClassUnloadRequest
 * JD-Core Version:    0.6.2
 */