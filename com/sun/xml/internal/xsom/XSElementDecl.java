package com.sun.xml.internal.xsom;

import java.util.List;
import java.util.Set;

public abstract interface XSElementDecl extends XSDeclaration, XSTerm
{
  public abstract XSType getType();

  public abstract boolean isNillable();

  public abstract XSElementDecl getSubstAffiliation();

  public abstract List<XSIdentityConstraint> getIdentityConstraints();

  public abstract boolean isSubstitutionExcluded(int paramInt);

  public abstract boolean isSubstitutionDisallowed(int paramInt);

  public abstract boolean isAbstract();

  /** @deprecated */
  public abstract XSElementDecl[] listSubstitutables();

  public abstract Set<? extends XSElementDecl> getSubstitutables();

  public abstract boolean canBeSubstitutedBy(XSElementDecl paramXSElementDecl);

  public abstract XmlString getDefaultValue();

  public abstract XmlString getFixedValue();

  public abstract Boolean getForm();
}

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.xml.internal.xsom.XSElementDecl
 * JD-Core Version:    0.6.2
 */