package com.sun.codemodel.internal;

import java.lang.annotation.Annotation;
import java.util.Collection;

public abstract interface JAnnotatable
{
  public abstract JAnnotationUse annotate(JClass paramJClass);

  public abstract JAnnotationUse annotate(Class<? extends Annotation> paramClass);

  public abstract <W extends JAnnotationWriter> W annotate2(Class<W> paramClass);

  public abstract Collection<JAnnotationUse> annotations();
}

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.codemodel.internal.JAnnotatable
 * JD-Core Version:    0.6.2
 */