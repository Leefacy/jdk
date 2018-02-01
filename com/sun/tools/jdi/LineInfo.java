package com.sun.tools.jdi;

import com.sun.jdi.AbsentInformationException;

abstract interface LineInfo
{
  public abstract String liStratum();

  public abstract int liLineNumber();

  public abstract String liSourceName()
    throws AbsentInformationException;

  public abstract String liSourcePath()
    throws AbsentInformationException;
}

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.jdi.LineInfo
 * JD-Core Version:    0.6.2
 */