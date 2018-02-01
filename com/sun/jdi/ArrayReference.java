package com.sun.jdi;

import java.util.List;
import jdk.Exported;

@Exported
public abstract interface ArrayReference extends ObjectReference
{
  public abstract int length();

  public abstract Value getValue(int paramInt);

  public abstract List<Value> getValues();

  public abstract List<Value> getValues(int paramInt1, int paramInt2);

  public abstract void setValue(int paramInt, Value paramValue)
    throws InvalidTypeException, ClassNotLoadedException;

  public abstract void setValues(List<? extends Value> paramList)
    throws InvalidTypeException, ClassNotLoadedException;

  public abstract void setValues(int paramInt1, List<? extends Value> paramList, int paramInt2, int paramInt3)
    throws InvalidTypeException, ClassNotLoadedException;
}

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.jdi.ArrayReference
 * JD-Core Version:    0.6.2
 */