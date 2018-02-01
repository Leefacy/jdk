package com.sun.xml.internal.xsom;

import java.util.Collection;
import java.util.Iterator;
import javax.xml.namespace.NamespaceContext;

public abstract interface XSSchemaSet
{
  public abstract XSSchema getSchema(String paramString);

  public abstract XSSchema getSchema(int paramInt);

  public abstract int getSchemaSize();

  public abstract Iterator<XSSchema> iterateSchema();

  public abstract Collection<XSSchema> getSchemas();

  public abstract XSType getType(String paramString1, String paramString2);

  public abstract XSSimpleType getSimpleType(String paramString1, String paramString2);

  public abstract XSAttributeDecl getAttributeDecl(String paramString1, String paramString2);

  public abstract XSElementDecl getElementDecl(String paramString1, String paramString2);

  public abstract XSModelGroupDecl getModelGroupDecl(String paramString1, String paramString2);

  public abstract XSAttGroupDecl getAttGroupDecl(String paramString1, String paramString2);

  public abstract XSComplexType getComplexType(String paramString1, String paramString2);

  public abstract XSIdentityConstraint getIdentityConstraint(String paramString1, String paramString2);

  public abstract Iterator<XSElementDecl> iterateElementDecls();

  public abstract Iterator<XSType> iterateTypes();

  public abstract Iterator<XSAttributeDecl> iterateAttributeDecls();

  public abstract Iterator<XSAttGroupDecl> iterateAttGroupDecls();

  public abstract Iterator<XSModelGroupDecl> iterateModelGroupDecls();

  public abstract Iterator<XSSimpleType> iterateSimpleTypes();

  public abstract Iterator<XSComplexType> iterateComplexTypes();

  public abstract Iterator<XSNotation> iterateNotations();

  public abstract Iterator<XSIdentityConstraint> iterateIdentityConstraints();

  public abstract XSComplexType getAnyType();

  public abstract XSSimpleType getAnySimpleType();

  public abstract XSContentType getEmpty();

  public abstract Collection<XSComponent> select(String paramString, NamespaceContext paramNamespaceContext);

  public abstract XSComponent selectSingle(String paramString, NamespaceContext paramNamespaceContext);
}

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.xml.internal.xsom.XSSchemaSet
 * JD-Core Version:    0.6.2
 */