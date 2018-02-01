package com.sun.xml.internal.rngom.parse;

import java.util.Enumeration;
import org.relaxng.datatype.ValidationContext;

public abstract interface Context extends ValidationContext
{
  public abstract Enumeration prefixes();

  public abstract Context copy();
}

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.xml.internal.rngom.parse.Context
 * JD-Core Version:    0.6.2
 */