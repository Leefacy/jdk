package com.sun.tools.internal.ws.processor.modeler.annotation;

import com.sun.tools.internal.ws.processor.modeler.ModelerException;
import com.sun.tools.internal.ws.wscompile.WsgenOptions;
import java.io.File;
import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.Name;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;

public abstract interface ModelBuilder
{
  public abstract ProcessingEnvironment getProcessingEnvironment();

  public abstract String getOperationName(Name paramName);

  public abstract TypeMirror getHolderValueType(TypeMirror paramTypeMirror);

  public abstract boolean checkAndSetProcessed(TypeElement paramTypeElement);

  public abstract boolean isServiceException(TypeMirror paramTypeMirror);

  public abstract boolean isRemote(TypeElement paramTypeElement);

  public abstract boolean canOverWriteClass(String paramString);

  public abstract WsgenOptions getOptions();

  public abstract File getSourceDir();

  public abstract void log(String paramString);

  public abstract void processWarning(String paramString);

  public abstract void processError(String paramString);

  public abstract void processError(String paramString, Element paramElement)
    throws ModelerException;
}

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.internal.ws.processor.modeler.annotation.ModelBuilder
 * JD-Core Version:    0.6.2
 */