package sun.jvmstat.monitor.remote;

import sun.jvmstat.monitor.MonitoredVm;

public abstract interface BufferedMonitoredVm extends MonitoredVm
{
  public abstract byte[] getBytes();

  public abstract int getCapacity();
}

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     sun.jvmstat.monitor.remote.BufferedMonitoredVm
 * JD-Core Version:    0.6.2
 */