package com.sun.tools.classfile;

public abstract interface Dependency
{
  public abstract Location getOrigin();

  public abstract Location getTarget();

  public static abstract interface Filter
  {
    public abstract boolean accepts(Dependency paramDependency);
  }

  public static abstract interface Finder
  {
    public abstract Iterable<? extends Dependency> findDependencies(ClassFile paramClassFile);
  }

  public static abstract interface Location
  {
    public abstract String getName();

    public abstract String getClassName();

    public abstract String getPackageName();
  }
}

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.classfile.Dependency
 * JD-Core Version:    0.6.2
 */