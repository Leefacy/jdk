package com.sun.tools.corba.se.idl;

public abstract interface InterfaceType
{
  public static final int NORMAL = 0;
  public static final int ABSTRACT = 1;
  public static final int LOCAL = 2;
  public static final int LOCALSERVANT = 3;
  public static final int LOCAL_SIGNATURE_ONLY = 4;

  public abstract int getInterfaceType();

  public abstract void setInterfaceType(int paramInt);
}

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.corba.se.idl.InterfaceType
 * JD-Core Version:    0.6.2
 */