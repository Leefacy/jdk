package com.sun.jdi;

import java.util.List;
import jdk.Exported;

@Exported
public abstract interface ClassLoaderReference extends ObjectReference
{
  public abstract List<ReferenceType> definedClasses();

  public abstract List<ReferenceType> visibleClasses();
}

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.jdi.ClassLoaderReference
 * JD-Core Version:    0.6.2
 */