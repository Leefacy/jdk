package com.sun.jdi.request;

import com.sun.jdi.Mirror;
import jdk.Exported;

@Exported
public abstract interface EventRequest extends Mirror
{
  public static final int SUSPEND_NONE = 0;
  public static final int SUSPEND_EVENT_THREAD = 1;
  public static final int SUSPEND_ALL = 2;

  public abstract boolean isEnabled();

  public abstract void setEnabled(boolean paramBoolean);

  public abstract void enable();

  public abstract void disable();

  public abstract void addCountFilter(int paramInt);

  public abstract void setSuspendPolicy(int paramInt);

  public abstract int suspendPolicy();

  public abstract void putProperty(Object paramObject1, Object paramObject2);

  public abstract Object getProperty(Object paramObject);
}

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.jdi.request.EventRequest
 * JD-Core Version:    0.6.2
 */