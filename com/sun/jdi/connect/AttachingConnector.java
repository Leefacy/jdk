package com.sun.jdi.connect;

import com.sun.jdi.VirtualMachine;
import java.io.IOException;
import java.util.Map;
import jdk.Exported;

@Exported
public abstract interface AttachingConnector extends Connector
{
  public abstract VirtualMachine attach(Map<String, ? extends Connector.Argument> paramMap)
    throws IOException, IllegalConnectorArgumentsException;
}

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.jdi.connect.AttachingConnector
 * JD-Core Version:    0.6.2
 */