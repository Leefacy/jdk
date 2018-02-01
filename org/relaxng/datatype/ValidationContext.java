package org.relaxng.datatype;

public abstract interface ValidationContext
{
  public abstract String resolveNamespacePrefix(String paramString);

  public abstract String getBaseUri();

  public abstract boolean isUnparsedEntity(String paramString);

  public abstract boolean isNotation(String paramString);
}

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     org.relaxng.datatype.ValidationContext
 * JD-Core Version:    0.6.2
 */