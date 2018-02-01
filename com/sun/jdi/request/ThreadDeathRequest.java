package com.sun.jdi.request;

import com.sun.jdi.ThreadReference;
import jdk.Exported;

@Exported
public abstract interface ThreadDeathRequest extends EventRequest
{
  public abstract void addThreadFilter(ThreadReference paramThreadReference);
}

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.jdi.request.ThreadDeathRequest
 * JD-Core Version:    0.6.2
 */