package com.sun.jdi;

import java.util.List;
import jdk.Exported;

@Exported
public abstract interface ClassType extends ReferenceType
{
  public static final int INVOKE_SINGLE_THREADED = 1;

  public abstract ClassType superclass();

  public abstract List<InterfaceType> interfaces();

  public abstract List<InterfaceType> allInterfaces();

  public abstract List<ClassType> subclasses();

  public abstract boolean isEnum();

  public abstract void setValue(Field paramField, Value paramValue)
    throws InvalidTypeException, ClassNotLoadedException;

  public abstract Value invokeMethod(ThreadReference paramThreadReference, Method paramMethod, List<? extends Value> paramList, int paramInt)
    throws InvalidTypeException, ClassNotLoadedException, IncompatibleThreadStateException, InvocationException;

  public abstract ObjectReference newInstance(ThreadReference paramThreadReference, Method paramMethod, List<? extends Value> paramList, int paramInt)
    throws InvalidTypeException, ClassNotLoadedException, IncompatibleThreadStateException, InvocationException;

  public abstract Method concreteMethodByName(String paramString1, String paramString2);
}

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.jdi.ClassType
 * JD-Core Version:    0.6.2
 */