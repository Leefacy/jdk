package com.sun.xml.internal.xsom;

import java.util.List;

public abstract interface XSIdentityConstraint extends XSComponent
{
  public static final short KEY = 0;
  public static final short KEYREF = 1;
  public static final short UNIQUE = 2;

  public abstract XSElementDecl getParent();

  public abstract String getName();

  public abstract String getTargetNamespace();

  public abstract short getCategory();

  public abstract XSXPath getSelector();

  public abstract List<XSXPath> getFields();

  public abstract XSIdentityConstraint getReferencedKey();
}

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.xml.internal.xsom.XSIdentityConstraint
 * JD-Core Version:    0.6.2
 */