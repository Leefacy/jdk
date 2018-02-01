package com.sun.tools.internal.xjc.model;

import com.sun.codemodel.internal.JType;
import com.sun.tools.internal.xjc.model.nav.NClass;
import com.sun.tools.internal.xjc.model.nav.NType;
import com.sun.tools.internal.xjc.outline.Aspect;
import com.sun.tools.internal.xjc.outline.Outline;
import com.sun.xml.internal.bind.v2.model.core.TypeInfo;

public abstract interface CTypeInfo extends TypeInfo<NType, NClass>, CCustomizable
{
  public abstract JType toType(Outline paramOutline, Aspect paramAspect);
}

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.internal.xjc.model.CTypeInfo
 * JD-Core Version:    0.6.2
 */