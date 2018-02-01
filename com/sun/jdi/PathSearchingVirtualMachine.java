package com.sun.jdi;

import java.util.List;
import jdk.Exported;

@Exported
public abstract interface PathSearchingVirtualMachine extends VirtualMachine
{
  public abstract List<String> classPath();

  public abstract List<String> bootClassPath();

  public abstract String baseDirectory();
}

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.jdi.PathSearchingVirtualMachine
 * JD-Core Version:    0.6.2
 */