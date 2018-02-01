package org.relaxng.datatype;

public abstract interface DatatypeLibrary
{
  public abstract DatatypeBuilder createDatatypeBuilder(String paramString)
    throws DatatypeException;

  public abstract Datatype createDatatype(String paramString)
    throws DatatypeException;
}

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     org.relaxng.datatype.DatatypeLibrary
 * JD-Core Version:    0.6.2
 */