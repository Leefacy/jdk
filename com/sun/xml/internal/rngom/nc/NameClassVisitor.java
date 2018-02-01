package com.sun.xml.internal.rngom.nc;

import javax.xml.namespace.QName;

public abstract interface NameClassVisitor<V>
{
  public abstract V visitChoice(NameClass paramNameClass1, NameClass paramNameClass2);

  public abstract V visitNsName(String paramString);

  public abstract V visitNsNameExcept(String paramString, NameClass paramNameClass);

  public abstract V visitAnyName();

  public abstract V visitAnyNameExcept(NameClass paramNameClass);

  public abstract V visitName(QName paramQName);

  public abstract V visitNull();
}

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.xml.internal.rngom.nc.NameClassVisitor
 * JD-Core Version:    0.6.2
 */