package com.sun.tools.corba.se.idl;

import java.io.PrintWriter;
import java.util.Hashtable;

public abstract interface AttributeGen extends Generator
{
  public abstract void generate(Hashtable paramHashtable, AttributeEntry paramAttributeEntry, PrintWriter paramPrintWriter);
}

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.corba.se.idl.AttributeGen
 * JD-Core Version:    0.6.2
 */