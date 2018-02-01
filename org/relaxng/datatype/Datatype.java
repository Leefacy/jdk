package org.relaxng.datatype;

public abstract interface Datatype
{
  public static final int ID_TYPE_NULL = 0;
  public static final int ID_TYPE_ID = 1;
  public static final int ID_TYPE_IDREF = 2;
  public static final int ID_TYPE_IDREFS = 3;

  public abstract boolean isValid(String paramString, ValidationContext paramValidationContext);

  public abstract void checkValid(String paramString, ValidationContext paramValidationContext)
    throws DatatypeException;

  public abstract DatatypeStreamingValidator createStreamingValidator(ValidationContext paramValidationContext);

  public abstract Object createValue(String paramString, ValidationContext paramValidationContext);

  public abstract boolean sameValue(Object paramObject1, Object paramObject2);

  public abstract int valueHashCode(Object paramObject);

  public abstract int getIdType();

  public abstract boolean isContextDependent();
}

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     org.relaxng.datatype.Datatype
 * JD-Core Version:    0.6.2
 */