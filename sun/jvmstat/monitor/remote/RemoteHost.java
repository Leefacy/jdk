package sun.jvmstat.monitor.remote;

import java.rmi.Remote;
import java.rmi.RemoteException;
import sun.jvmstat.monitor.MonitorException;

public abstract interface RemoteHost extends Remote
{
  public abstract RemoteVm attachVm(int paramInt, String paramString)
    throws RemoteException, MonitorException;

  public abstract void detachVm(RemoteVm paramRemoteVm)
    throws RemoteException, MonitorException;

  public abstract int[] activeVms()
    throws RemoteException, MonitorException;
}

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     sun.jvmstat.monitor.remote.RemoteHost
 * JD-Core Version:    0.6.2
 */