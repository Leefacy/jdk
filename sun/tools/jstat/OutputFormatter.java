package sun.tools.jstat;

import sun.jvmstat.monitor.MonitorException;

public abstract interface OutputFormatter
{
  public abstract String getHeader()
    throws MonitorException;

  public abstract String getRow()
    throws MonitorException;
}

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     sun.tools.jstat.OutputFormatter
 * JD-Core Version:    0.6.2
 */