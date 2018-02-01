package sun.tools.jstat;

import sun.jvmstat.monitor.MonitorException;

abstract interface Closure
{
  public abstract void visit(Object paramObject, boolean paramBoolean)
    throws MonitorException;
}

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     sun.tools.jstat.Closure
 * JD-Core Version:    0.6.2
 */