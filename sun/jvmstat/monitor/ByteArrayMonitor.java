package sun.jvmstat.monitor;

public abstract interface ByteArrayMonitor extends Monitor
{
  public abstract byte[] byteArrayValue();

  public abstract byte byteAt(int paramInt);
}

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     sun.jvmstat.monitor.ByteArrayMonitor
 * JD-Core Version:    0.6.2
 */