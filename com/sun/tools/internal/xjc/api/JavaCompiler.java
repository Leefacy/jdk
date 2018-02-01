package com.sun.tools.internal.xjc.api;

import java.util.Collection;
import java.util.Map;
import javax.annotation.processing.ProcessingEnvironment;
import javax.xml.namespace.QName;

public abstract interface JavaCompiler
{
  public abstract J2SJAXBModel bind(Collection<Reference> paramCollection, Map<QName, Reference> paramMap, String paramString, ProcessingEnvironment paramProcessingEnvironment);
}

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.internal.xjc.api.JavaCompiler
 * JD-Core Version:    0.6.2
 */