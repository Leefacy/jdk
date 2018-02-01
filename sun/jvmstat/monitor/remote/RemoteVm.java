package sun.jvmstat.monitor.remote;

import java.rmi.Remote;
import java.rmi.RemoteException;

public abstract interface RemoteVm extends Remote
{
  public abstract byte[] getBytes()
    throws RemoteException;

  public abstract int getCapacity()
    throws RemoteException;

  public abstract int getLocalVmId()
    throws RemoteException;

  public abstract void detach()
    throws RemoteException;
}

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     sun.jvmstat.monitor.remote.RemoteVm
 * JD-Core Version:    0.6.2
 */