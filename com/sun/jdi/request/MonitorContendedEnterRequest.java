package com.sun.jdi.request;

import com.sun.jdi.ObjectReference;
import com.sun.jdi.ReferenceType;
import com.sun.jdi.ThreadReference;
import jdk.Exported;

@Exported
public abstract interface MonitorContendedEnterRequest extends EventRequest
{
  public abstract void addThreadFilter(ThreadReference paramThreadReference);

  public abstract void addClassFilter(ReferenceType paramReferenceType);

  public abstract void addClassFilter(String paramString);

  public abstract void addClassExclusionFilter(String paramString);

  public abstract void addInstanceFilter(ObjectReference paramObjectReference);
}

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.jdi.request.MonitorContendedEnterRequest
 * JD-Core Version:    0.6.2
 */