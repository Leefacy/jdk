package sun.jvmstat.monitor;

import java.util.List;
import sun.jvmstat.monitor.event.VmListener;

public abstract interface MonitoredVm
{
  public abstract VmIdentifier getVmIdentifier();

  public abstract Monitor findByName(String paramString)
    throws MonitorException;

  public abstract List<Monitor> findByPattern(String paramString)
    throws MonitorException;

  public abstract void detach();

  public abstract void setInterval(int paramInt);

  public abstract int getInterval();

  public abstract void setLastException(Exception paramException);

  public abstract Exception getLastException();

  public abstract void clearLastException();

  public abstract boolean isErrored();

  public abstract void addVmListener(VmListener paramVmListener)
    throws MonitorException;

  public abstract void removeVmListener(VmListener paramVmListener)
    throws MonitorException;
}

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     sun.jvmstat.monitor.MonitoredVm
 * JD-Core Version:    0.6.2
 */