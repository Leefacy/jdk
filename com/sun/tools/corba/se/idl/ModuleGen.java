package com.sun.tools.corba.se.idl;

import java.io.PrintWriter;
import java.util.Hashtable;

public abstract interface ModuleGen extends Generator
{
  public abstract void generate(Hashtable paramHashtable, ModuleEntry paramModuleEntry, PrintWriter paramPrintWriter);
}

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.corba.se.idl.ModuleGen
 * JD-Core Version:    0.6.2
 */