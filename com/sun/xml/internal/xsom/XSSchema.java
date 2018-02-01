package com.sun.xml.internal.xsom;

import com.sun.xml.internal.xsom.parser.SchemaDocument;
import java.util.Iterator;
import java.util.Map;

public abstract interface XSSchema extends XSComponent
{
  public abstract String getTargetNamespace();

  public abstract Map<String, XSAttributeDecl> getAttributeDecls();

  public abstract Iterator<XSAttributeDecl> iterateAttributeDecls();

  public abstract XSAttributeDecl getAttributeDecl(String paramString);

  public abstract Map<String, XSElementDecl> getElementDecls();

  public abstract Iterator<XSElementDecl> iterateElementDecls();

  public abstract XSElementDecl getElementDecl(String paramString);

  public abstract Map<String, XSAttGroupDecl> getAttGroupDecls();

  public abstract Iterator<XSAttGroupDecl> iterateAttGroupDecls();

  public abstract XSAttGroupDecl getAttGroupDecl(String paramString);

  public abstract Map<String, XSModelGroupDecl> getModelGroupDecls();

  public abstract Iterator<XSModelGroupDecl> iterateModelGroupDecls();

  public abstract XSModelGroupDecl getModelGroupDecl(String paramString);

  public abstract Map<String, XSType> getTypes();

  public abstract Iterator<XSType> iterateTypes();

  public abstract XSType getType(String paramString);

  public abstract Map<String, XSSimpleType> getSimpleTypes();

  public abstract Iterator<XSSimpleType> iterateSimpleTypes();

  public abstract XSSimpleType getSimpleType(String paramString);

  public abstract Map<String, XSComplexType> getComplexTypes();

  public abstract Iterator<XSComplexType> iterateComplexTypes();

  public abstract XSComplexType getComplexType(String paramString);

  public abstract Map<String, XSNotation> getNotations();

  public abstract Iterator<XSNotation> iterateNotations();

  public abstract XSNotation getNotation(String paramString);

  public abstract Map<String, XSIdentityConstraint> getIdentityConstraints();

  public abstract XSIdentityConstraint getIdentityConstraint(String paramString);

  /** @deprecated */
  public abstract SchemaDocument getSourceDocument();

  public abstract XSSchemaSet getRoot();
}

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.xml.internal.xsom.XSSchema
 * JD-Core Version:    0.6.2
 */