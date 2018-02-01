package com.sun.codemodel.internal;

public abstract interface JGenerifiable
{
  public abstract JTypeVar generify(String paramString);

  public abstract JTypeVar generify(String paramString, Class<?> paramClass);

  public abstract JTypeVar generify(String paramString, JClass paramJClass);

  public abstract JTypeVar[] typeParams();
}

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.codemodel.internal.JGenerifiable
 * JD-Core Version:    0.6.2
 */