package com.sun.tools.internal.xjc.outline;

import com.sun.codemodel.internal.JClass;
import com.sun.codemodel.internal.JClassContainer;
import com.sun.codemodel.internal.JCodeModel;
import com.sun.codemodel.internal.JPackage;
import com.sun.codemodel.internal.JType;
import com.sun.tools.internal.xjc.ErrorReceiver;
import com.sun.tools.internal.xjc.model.CClassInfo;
import com.sun.tools.internal.xjc.model.CClassInfoParent;
import com.sun.tools.internal.xjc.model.CElementInfo;
import com.sun.tools.internal.xjc.model.CEnumLeafInfo;
import com.sun.tools.internal.xjc.model.CPropertyInfo;
import com.sun.tools.internal.xjc.model.CTypeRef;
import com.sun.tools.internal.xjc.model.Model;
import com.sun.tools.internal.xjc.util.CodeModelClassFactory;
import java.util.Collection;

public abstract interface Outline
{
  public abstract Model getModel();

  public abstract JCodeModel getCodeModel();

  public abstract FieldOutline getField(CPropertyInfo paramCPropertyInfo);

  public abstract PackageOutline getPackageContext(JPackage paramJPackage);

  public abstract Collection<? extends ClassOutline> getClasses();

  public abstract ClassOutline getClazz(CClassInfo paramCClassInfo);

  public abstract ElementOutline getElement(CElementInfo paramCElementInfo);

  public abstract EnumOutline getEnum(CEnumLeafInfo paramCEnumLeafInfo);

  public abstract Collection<EnumOutline> getEnums();

  public abstract Iterable<? extends PackageOutline> getAllPackageContexts();

  public abstract CodeModelClassFactory getClassFactory();

  public abstract ErrorReceiver getErrorReceiver();

  public abstract JClassContainer getContainer(CClassInfoParent paramCClassInfoParent, Aspect paramAspect);

  public abstract JType resolve(CTypeRef paramCTypeRef, Aspect paramAspect);

  public abstract JClass addRuntime(Class paramClass);
}

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.internal.xjc.outline.Outline
 * JD-Core Version:    0.6.2
 */