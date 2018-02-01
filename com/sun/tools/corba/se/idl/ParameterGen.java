package com.sun.tools.corba.se.idl;

import java.io.PrintWriter;
import java.util.Hashtable;

public abstract interface ParameterGen extends Generator
{
  public abstract void generate(Hashtable paramHashtable, ParameterEntry paramParameterEntry, PrintWriter paramPrintWriter);
}

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.corba.se.idl.ParameterGen
 * JD-Core Version:    0.6.2
 */