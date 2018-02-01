package com.sun.jdi;

import com.sun.jdi.connect.AttachingConnector;
import com.sun.jdi.connect.Connector;
import com.sun.jdi.connect.LaunchingConnector;
import com.sun.jdi.connect.ListeningConnector;
import com.sun.jdi.connect.spi.Connection;
import java.io.IOException;
import java.util.List;
import jdk.Exported;

@Exported
public abstract interface VirtualMachineManager
{
  public abstract LaunchingConnector defaultConnector();

  public abstract List<LaunchingConnector> launchingConnectors();

  public abstract List<AttachingConnector> attachingConnectors();

  public abstract List<ListeningConnector> listeningConnectors();

  public abstract List<Connector> allConnectors();

  public abstract List<VirtualMachine> connectedVirtualMachines();

  public abstract int majorInterfaceVersion();

  public abstract int minorInterfaceVersion();

  public abstract VirtualMachine createVirtualMachine(Connection paramConnection, Process paramProcess)
    throws IOException;

  public abstract VirtualMachine createVirtualMachine(Connection paramConnection)
    throws IOException;
}

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.jdi.VirtualMachineManager
 * JD-Core Version:    0.6.2
 */