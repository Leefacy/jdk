package com.sun.xml.internal.xsom;

import java.math.BigInteger;

public abstract interface XSParticle extends XSContentType
{
  public static final int UNBOUNDED = -1;

  public abstract BigInteger getMinOccurs();

  public abstract BigInteger getMaxOccurs();

  public abstract boolean isRepeated();

  public abstract XSTerm getTerm();
}

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.xml.internal.xsom.XSParticle
 * JD-Core Version:    0.6.2
 */