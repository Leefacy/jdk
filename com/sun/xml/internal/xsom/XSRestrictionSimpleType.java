package com.sun.xml.internal.xsom;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;

public abstract interface XSRestrictionSimpleType extends XSSimpleType
{
  public abstract Iterator<XSFacet> iterateDeclaredFacets();

  public abstract Collection<? extends XSFacet> getDeclaredFacets();

  public abstract XSFacet getDeclaredFacet(String paramString);

  public abstract List<XSFacet> getDeclaredFacets(String paramString);
}

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.xml.internal.xsom.XSRestrictionSimpleType
 * JD-Core Version:    0.6.2
 */