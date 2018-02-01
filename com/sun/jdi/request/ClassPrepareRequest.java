package com.sun.jdi.request;

import com.sun.jdi.ReferenceType;
import jdk.Exported;

@Exported
public abstract interface ClassPrepareRequest extends EventRequest
{
  public abstract void addClassFilter(ReferenceType paramReferenceType);

  public abstract void addClassFilter(String paramString);

  public abstract void addClassExclusionFilter(String paramString);

  public abstract void addSourceNameFilter(String paramString);
}

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.jdi.request.ClassPrepareRequest
 * JD-Core Version:    0.6.2
 */