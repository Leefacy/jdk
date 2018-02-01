package com.sun.tools.corba.se.idl;

import java.io.PrintWriter;
import java.util.Hashtable;

public abstract interface MethodGen extends Generator
{
  public abstract void generate(Hashtable paramHashtable, MethodEntry paramMethodEntry, PrintWriter paramPrintWriter);
}

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.corba.se.idl.MethodGen
 * JD-Core Version:    0.6.2
 */