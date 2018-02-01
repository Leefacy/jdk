package com.sun.xml.internal.xsom.visitor;

import com.sun.xml.internal.xsom.XSAnnotation;
import com.sun.xml.internal.xsom.XSAttGroupDecl;
import com.sun.xml.internal.xsom.XSAttributeDecl;
import com.sun.xml.internal.xsom.XSAttributeUse;
import com.sun.xml.internal.xsom.XSComplexType;
import com.sun.xml.internal.xsom.XSFacet;
import com.sun.xml.internal.xsom.XSIdentityConstraint;
import com.sun.xml.internal.xsom.XSNotation;
import com.sun.xml.internal.xsom.XSSchema;
import com.sun.xml.internal.xsom.XSXPath;

public abstract interface XSFunction<T> extends XSContentTypeFunction<T>, XSTermFunction<T>
{
  public abstract T annotation(XSAnnotation paramXSAnnotation);

  public abstract T attGroupDecl(XSAttGroupDecl paramXSAttGroupDecl);

  public abstract T attributeDecl(XSAttributeDecl paramXSAttributeDecl);

  public abstract T attributeUse(XSAttributeUse paramXSAttributeUse);

  public abstract T complexType(XSComplexType paramXSComplexType);

  public abstract T schema(XSSchema paramXSSchema);

  public abstract T facet(XSFacet paramXSFacet);

  public abstract T notation(XSNotation paramXSNotation);

  public abstract T identityConstraint(XSIdentityConstraint paramXSIdentityConstraint);

  public abstract T xpath(XSXPath paramXSXPath);
}

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.xml.internal.xsom.visitor.XSFunction
 * JD-Core Version:    0.6.2
 */