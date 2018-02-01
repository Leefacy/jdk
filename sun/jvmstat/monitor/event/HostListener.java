package sun.jvmstat.monitor.event;

import java.util.EventListener;

public abstract interface HostListener extends EventListener
{
  public abstract void vmStatusChanged(VmStatusChangeEvent paramVmStatusChangeEvent);

  public abstract void disconnected(HostEvent paramHostEvent);
}

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     sun.jvmstat.monitor.event.HostListener
 * JD-Core Version:    0.6.2
 */