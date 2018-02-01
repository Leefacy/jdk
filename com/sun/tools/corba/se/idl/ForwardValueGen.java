package com.sun.tools.corba.se.idl;

import java.io.PrintWriter;
import java.util.Hashtable;

public abstract interface ForwardValueGen extends Generator
{
  public abstract void generate(Hashtable paramHashtable, ForwardValueEntry paramForwardValueEntry, PrintWriter paramPrintWriter);
}

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.corba.se.idl.ForwardValueGen
 * JD-Core Version:    0.6.2
 */