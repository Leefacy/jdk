package com.sun.tools.jdi;

public abstract interface VMModifiers
{
  public static final int PUBLIC = 1;
  public static final int PRIVATE = 2;
  public static final int PROTECTED = 4;
  public static final int STATIC = 8;
  public static final int FINAL = 16;
  public static final int SYNCHRONIZED = 32;
  public static final int VOLATILE = 64;
  public static final int BRIDGE = 64;
  public static final int TRANSIENT = 128;
  public static final int VARARGS = 128;
  public static final int NATIVE = 256;
  public static final int INTERFACE = 512;
  public static final int ABSTRACT = 1024;
  public static final int ENUM_CONSTANT = 16384;
  public static final int SYNTHETIC = -268435456;
}

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.jdi.VMModifiers
 * JD-Core Version:    0.6.2
 */