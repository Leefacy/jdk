package com.sun.tools.internal.xjc.reader.xmlschema.bindinfo;

import java.util.Collection;
import javax.xml.namespace.QName;
import org.xml.sax.Locator;

public abstract interface BIDeclaration
{
  public abstract void setParent(BindInfo paramBindInfo);

  public abstract QName getName();

  public abstract Locator getLocation();

  public abstract void markAsAcknowledged();

  public abstract boolean isAcknowledged();

  public abstract void onSetOwner();

  public abstract Collection<BIDeclaration> getChildren();
}

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.internal.xjc.reader.xmlschema.bindinfo.BIDeclaration
 * JD-Core Version:    0.6.2
 */