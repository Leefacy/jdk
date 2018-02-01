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

public abstract interface XSVisitor extends XSTermVisitor, XSContentTypeVisitor
{
  public abstract void annotation(XSAnnotation paramXSAnnotation);

  public abstract void attGroupDecl(XSAttGroupDecl paramXSAttGroupDecl);

  public abstract void attributeDecl(XSAttributeDecl paramXSAttributeDecl);

  public abstract void attributeUse(XSAttributeUse paramXSAttributeUse);

  public abstract void complexType(XSComplexType paramXSComplexType);

  public abstract void schema(XSSchema paramXSSchema);

  public abstract void facet(XSFacet paramXSFacet);

  public abstract void notation(XSNotation paramXSNotation);

  public abstract void identityConstraint(XSIdentityConstraint paramXSIdentityConstraint);

  public abstract void xpath(XSXPath paramXSXPath);
}

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.xml.internal.xsom.visitor.XSVisitor
 * JD-Core Version:    0.6.2
 */