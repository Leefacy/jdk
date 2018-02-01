package com.sun.jdi.request;

import com.sun.jdi.ObjectReference;
import com.sun.jdi.ReferenceType;
import com.sun.jdi.ThreadReference;
import jdk.Exported;

@Exported
public abstract interface StepRequest extends EventRequest
{
  public static final int STEP_INTO = 1;
  public static final int STEP_OVER = 2;
  public static final int STEP_OUT = 3;
  public static final int STEP_MIN = -1;
  public static final int STEP_LINE = -2;

  public abstract ThreadReference thread();

  public abstract int size();

  public abstract int depth();

  public abstract void addClassFilter(ReferenceType paramReferenceType);

  public abstract void addClassFilter(String paramString);

  public abstract void addClassExclusionFilter(String paramString);

  public abstract void addInstanceFilter(ObjectReference paramObjectReference);
}

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.jdi.request.StepRequest
 * JD-Core Version:    0.6.2
 */