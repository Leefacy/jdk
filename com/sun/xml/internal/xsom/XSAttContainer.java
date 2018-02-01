package com.sun.xml.internal.xsom;

import java.util.Collection;
import java.util.Iterator;

public abstract interface XSAttContainer extends XSDeclaration
{
  public abstract XSWildcard getAttributeWildcard();

  public abstract XSAttributeUse getAttributeUse(String paramString1, String paramString2);

  public abstract Iterator<? extends XSAttributeUse> iterateAttributeUses();

  public abstract Collection<? extends XSAttributeUse> getAttributeUses();

  public abstract XSAttributeUse getDeclaredAttributeUse(String paramString1, String paramString2);

  public abstract Iterator<? extends XSAttributeUse> iterateDeclaredAttributeUses();

  public abstract Collection<? extends XSAttributeUse> getDeclaredAttributeUses();

  public abstract Iterator<? extends XSAttGroupDecl> iterateAttGroups();

  public abstract Collection<? extends XSAttGroupDecl> getAttGroups();
}

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.xml.internal.xsom.XSAttContainer
 * JD-Core Version:    0.6.2
 */