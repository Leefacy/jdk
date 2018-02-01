package com.sun.codemodel.internal;

import java.lang.annotation.Annotation;

public abstract interface JAnnotationWriter<A extends Annotation>
{
  public abstract JAnnotationUse getAnnotationUse();

  public abstract Class<A> getAnnotationType();
}

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.codemodel.internal.JAnnotationWriter
 * JD-Core Version:    0.6.2
 */