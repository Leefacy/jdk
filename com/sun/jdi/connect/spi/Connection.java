package com.sun.jdi.connect.spi;

import java.io.IOException;
import jdk.Exported;

@Exported
public abstract class Connection
{
  public abstract byte[] readPacket()
    throws IOException;

  public abstract void writePacket(byte[] paramArrayOfByte)
    throws IOException;

  public abstract void close()
    throws IOException;

  public abstract boolean isOpen();
}

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.jdi.connect.spi.Connection
 * JD-Core Version:    0.6.2
 */