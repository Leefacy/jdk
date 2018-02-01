package com.sun.tools.internal.ws.processor.modeler.annotation;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.type.TypeMirror;

public abstract interface TypeMoniker
{
  public abstract TypeMirror create(ProcessingEnvironment paramProcessingEnvironment);
}

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.internal.ws.processor.modeler.annotation.TypeMoniker
 * JD-Core Version:    0.6.2
 */