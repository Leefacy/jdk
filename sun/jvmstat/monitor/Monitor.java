package sun.jvmstat.monitor;

public abstract interface Monitor
{
  public abstract String getName();

  public abstract String getBaseName();

  public abstract Units getUnits();

  public abstract Variability getVariability();

  public abstract boolean isVector();

  public abstract int getVectorLength();

  public abstract boolean isSupported();

  public abstract Object getValue();
}

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     sun.jvmstat.monitor.Monitor
 * JD-Core Version:    0.6.2
 */