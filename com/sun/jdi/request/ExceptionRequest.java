package com.sun.jdi.request;

import com.sun.jdi.ObjectReference;
import com.sun.jdi.ReferenceType;
import com.sun.jdi.ThreadReference;
import jdk.Exported;

@Exported
public abstract interface ExceptionRequest extends EventRequest
{
  public abstract ReferenceType exception();

  public abstract boolean notifyCaught();

  public abstract boolean notifyUncaught();

  public abstract void addThreadFilter(ThreadReference paramThreadReference);

  public abstract void addClassFilter(ReferenceType paramReferenceType);

  public abstract void addClassFilter(String paramString);

  public abstract void addClassExclusionFilter(String paramString);

  public abstract void addInstanceFilter(ObjectReference paramObjectReference);
}

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.jdi.request.ExceptionRequest
 * JD-Core Version:    0.6.2
 */