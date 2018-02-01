package sun.jvmstat.monitor.event;

import java.util.EventListener;

public abstract interface VmListener extends EventListener
{
  public abstract void monitorStatusChanged(MonitorStatusChangeEvent paramMonitorStatusChangeEvent);

  public abstract void monitorsUpdated(VmEvent paramVmEvent);

  public abstract void disconnected(VmEvent paramVmEvent);
}

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     sun.jvmstat.monitor.event.VmListener
 * JD-Core Version:    0.6.2
 */