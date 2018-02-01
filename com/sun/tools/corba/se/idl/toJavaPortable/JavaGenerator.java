package com.sun.tools.corba.se.idl.toJavaPortable;

import com.sun.tools.corba.se.idl.SymtabEntry;
import java.io.PrintWriter;

public abstract interface JavaGenerator
{
  public abstract int helperType(int paramInt, String paramString1, TCOffsets paramTCOffsets, String paramString2, SymtabEntry paramSymtabEntry, PrintWriter paramPrintWriter);

  public abstract void helperRead(String paramString, SymtabEntry paramSymtabEntry, PrintWriter paramPrintWriter);

  public abstract void helperWrite(SymtabEntry paramSymtabEntry, PrintWriter paramPrintWriter);

  public abstract int read(int paramInt, String paramString1, String paramString2, SymtabEntry paramSymtabEntry, PrintWriter paramPrintWriter);

  public abstract int write(int paramInt, String paramString1, String paramString2, SymtabEntry paramSymtabEntry, PrintWriter paramPrintWriter);

  public abstract int type(int paramInt, String paramString1, TCOffsets paramTCOffsets, String paramString2, SymtabEntry paramSymtabEntry, PrintWriter paramPrintWriter);
}

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.corba.se.idl.toJavaPortable.JavaGenerator
 * JD-Core Version:    0.6.2
 */