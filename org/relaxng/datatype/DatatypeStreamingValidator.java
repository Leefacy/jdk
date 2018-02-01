package org.relaxng.datatype;

public abstract interface DatatypeStreamingValidator
{
  public abstract void addCharacters(char[] paramArrayOfChar, int paramInt1, int paramInt2);

  public abstract boolean isValid();

  public abstract void checkValid()
    throws DatatypeException;
}

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     org.relaxng.datatype.DatatypeStreamingValidator
 * JD-Core Version:    0.6.2
 */