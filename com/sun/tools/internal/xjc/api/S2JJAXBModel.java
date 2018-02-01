package com.sun.tools.internal.xjc.api;

import com.sun.codemodel.internal.JClass;
import com.sun.codemodel.internal.JCodeModel;
import com.sun.tools.internal.xjc.Plugin;
import java.util.Collection;
import java.util.List;
import javax.xml.namespace.QName;

public abstract interface S2JJAXBModel extends JAXBModel
{
  public abstract Mapping get(QName paramQName);

  public abstract List<JClass> getAllObjectFactories();

  public abstract Collection<? extends Mapping> getMappings();

  public abstract TypeAndAnnotation getJavaType(QName paramQName);

  public abstract JCodeModel generateCode(Plugin[] paramArrayOfPlugin, ErrorListener paramErrorListener);
}

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.internal.xjc.api.S2JJAXBModel
 * JD-Core Version:    0.6.2
 */