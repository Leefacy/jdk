package com.sun.jdi;

import java.util.List;
import java.util.Map;
import jdk.Exported;

@Exported
public abstract interface StackFrame extends Mirror, Locatable
{
  public abstract Location location();

  public abstract ThreadReference thread();

  public abstract ObjectReference thisObject();

  public abstract List<LocalVariable> visibleVariables()
    throws AbsentInformationException;

  public abstract LocalVariable visibleVariableByName(String paramString)
    throws AbsentInformationException;

  public abstract Value getValue(LocalVariable paramLocalVariable);

  public abstract Map<LocalVariable, Value> getValues(List<? extends LocalVariable> paramList);

  public abstract void setValue(LocalVariable paramLocalVariable, Value paramValue)
    throws InvalidTypeException, ClassNotLoadedException;

  public abstract List<Value> getArgumentValues();
}

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.jdi.StackFrame
 * JD-Core Version:    0.6.2
 */