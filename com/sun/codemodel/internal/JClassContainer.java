package com.sun.codemodel.internal;

import java.util.Iterator;

public abstract interface JClassContainer
{
  public abstract boolean isClass();

  public abstract boolean isPackage();

  public abstract JDefinedClass _class(int paramInt, String paramString)
    throws JClassAlreadyExistsException;

  public abstract JDefinedClass _class(String paramString)
    throws JClassAlreadyExistsException;

  public abstract JDefinedClass _interface(int paramInt, String paramString)
    throws JClassAlreadyExistsException;

  public abstract JDefinedClass _interface(String paramString)
    throws JClassAlreadyExistsException;

  /** @deprecated */
  public abstract JDefinedClass _class(int paramInt, String paramString, boolean paramBoolean)
    throws JClassAlreadyExistsException;

  public abstract JDefinedClass _class(int paramInt, String paramString, ClassType paramClassType)
    throws JClassAlreadyExistsException;

  public abstract Iterator<JDefinedClass> classes();

  public abstract JClassContainer parentContainer();

  public abstract JPackage getPackage();

  public abstract JCodeModel owner();

  public abstract JDefinedClass _annotationTypeDeclaration(String paramString)
    throws JClassAlreadyExistsException;

  public abstract JDefinedClass _enum(String paramString)
    throws JClassAlreadyExistsException;
}

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.codemodel.internal.JClassContainer
 * JD-Core Version:    0.6.2
 */