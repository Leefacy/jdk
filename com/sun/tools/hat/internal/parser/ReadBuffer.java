package com.sun.tools.hat.internal.parser;

import java.io.IOException;

public abstract interface ReadBuffer
{
  public abstract void get(long paramLong, byte[] paramArrayOfByte)
    throws IOException;

  public abstract char getChar(long paramLong)
    throws IOException;

  public abstract byte getByte(long paramLong)
    throws IOException;

  public abstract short getShort(long paramLong)
    throws IOException;

  public abstract int getInt(long paramLong)
    throws IOException;

  public abstract long getLong(long paramLong)
    throws IOException;
}

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.hat.internal.parser.ReadBuffer
 * JD-Core Version:    0.6.2
 */