package com.sun.jdi;

import jdk.Exported;

@Exported
public abstract interface Location extends Mirror, Comparable<Location>
{
  public abstract ReferenceType declaringType();

  public abstract Method method();

  public abstract long codeIndex();

  public abstract String sourceName()
    throws AbsentInformationException;

  public abstract String sourceName(String paramString)
    throws AbsentInformationException;

  public abstract String sourcePath()
    throws AbsentInformationException;

  public abstract String sourcePath(String paramString)
    throws AbsentInformationException;

  public abstract int lineNumber();

  public abstract int lineNumber(String paramString);

  public abstract boolean equals(Object paramObject);

  public abstract int hashCode();
}

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.jdi.Location
 * JD-Core Version:    0.6.2
 */