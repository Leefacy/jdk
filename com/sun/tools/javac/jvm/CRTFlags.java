package com.sun.tools.javac.jvm;

public abstract interface CRTFlags
{
  public static final int CRT_STATEMENT = 1;
  public static final int CRT_BLOCK = 2;
  public static final int CRT_ASSIGNMENT = 4;
  public static final int CRT_FLOW_CONTROLLER = 8;
  public static final int CRT_FLOW_TARGET = 16;
  public static final int CRT_INVOKE = 32;
  public static final int CRT_CREATE = 64;
  public static final int CRT_BRANCH_TRUE = 128;
  public static final int CRT_BRANCH_FALSE = 256;
  public static final int CRT_VALID_FLAGS = 511;
}

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.javac.jvm.CRTFlags
 * JD-Core Version:    0.6.2
 */