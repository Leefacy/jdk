package com.sun.jdi;

import java.util.List;
import java.util.Map;
import jdk.Exported;

@Exported
public abstract interface ObjectReference extends Value
{
  public static final int INVOKE_SINGLE_THREADED = 1;
  public static final int INVOKE_NONVIRTUAL = 2;

  public abstract ReferenceType referenceType();

  public abstract Value getValue(Field paramField);

  public abstract Map<Field, Value> getValues(List<? extends Field> paramList);

  public abstract void setValue(Field paramField, Value paramValue)
    throws InvalidTypeException, ClassNotLoadedException;

  public abstract Value invokeMethod(ThreadReference paramThreadReference, Method paramMethod, List<? extends Value> paramList, int paramInt)
    throws InvalidTypeException, ClassNotLoadedException, IncompatibleThreadStateException, InvocationException;

  public abstract void disableCollection();

  public abstract void enableCollection();

  public abstract boolean isCollected();

  public abstract long uniqueID();

  public abstract List<ThreadReference> waitingThreads()
    throws IncompatibleThreadStateException;

  public abstract ThreadReference owningThread()
    throws IncompatibleThreadStateException;

  public abstract int entryCount()
    throws IncompatibleThreadStateException;

  public abstract List<ObjectReference> referringObjects(long paramLong);

  public abstract boolean equals(Object paramObject);

  public abstract int hashCode();
}

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.jdi.ObjectReference
 * JD-Core Version:    0.6.2
 */