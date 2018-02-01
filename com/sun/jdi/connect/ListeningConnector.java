package com.sun.jdi.connect;

import com.sun.jdi.VirtualMachine;
import java.io.IOException;
import java.util.Map;
import jdk.Exported;

@Exported
public abstract interface ListeningConnector extends Connector
{
  public abstract boolean supportsMultipleConnections();

  public abstract String startListening(Map<String, ? extends Connector.Argument> paramMap)
    throws IOException, IllegalConnectorArgumentsException;

  public abstract void stopListening(Map<String, ? extends Connector.Argument> paramMap)
    throws IOException, IllegalConnectorArgumentsException;

  public abstract VirtualMachine accept(Map<String, ? extends Connector.Argument> paramMap)
    throws IOException, IllegalConnectorArgumentsException;
}

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.jdi.connect.ListeningConnector
 * JD-Core Version:    0.6.2
 */