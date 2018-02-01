package com.sun.tools.jdi;

import com.sun.jdi.VirtualMachineManager;
import com.sun.jdi.connect.Connector;
import com.sun.jdi.connect.LaunchingConnector;

public abstract interface VirtualMachineManagerService extends VirtualMachineManager
{
  public abstract void setDefaultConnector(LaunchingConnector paramLaunchingConnector);

  public abstract void addConnector(Connector paramConnector);

  public abstract void removeConnector(Connector paramConnector);
}

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.jdi.VirtualMachineManagerService
 * JD-Core Version:    0.6.2
 */