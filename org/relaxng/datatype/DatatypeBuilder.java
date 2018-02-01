package org.relaxng.datatype;

public abstract interface DatatypeBuilder
{
  public abstract void addParameter(String paramString1, String paramString2, ValidationContext paramValidationContext)
    throws DatatypeException;

  public abstract Datatype createDatatype()
    throws DatatypeException;
}

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     org.relaxng.datatype.DatatypeBuilder
 * JD-Core Version:    0.6.2
 */