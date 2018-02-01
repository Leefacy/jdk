package com.sun.jdi.connect;

import com.sun.jdi.VirtualMachine;
import java.io.IOException;
import java.util.Map;
import jdk.Exported;

@Exported
public abstract interface LaunchingConnector extends Connector
{
  public abstract VirtualMachine launch(Map<String, ? extends Connector.Argument> paramMap)
    throws IOException, IllegalConnectorArgumentsException, VMStartException;
}

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.jdi.connect.LaunchingConnector
 * JD-Core Version:    0.6.2
 */