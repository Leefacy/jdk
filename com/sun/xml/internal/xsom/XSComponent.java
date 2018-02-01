package com.sun.xml.internal.xsom;

import com.sun.xml.internal.xsom.parser.SchemaDocument;
import com.sun.xml.internal.xsom.visitor.XSFunction;
import com.sun.xml.internal.xsom.visitor.XSVisitor;
import java.util.Collection;
import java.util.List;
import javax.xml.namespace.NamespaceContext;
import org.xml.sax.Locator;

public abstract interface XSComponent
{
  public abstract XSAnnotation getAnnotation();

  public abstract XSAnnotation getAnnotation(boolean paramBoolean);

  public abstract List<? extends ForeignAttributes> getForeignAttributes();

  public abstract String getForeignAttribute(String paramString1, String paramString2);

  public abstract Locator getLocator();

  public abstract XSSchema getOwnerSchema();

  public abstract XSSchemaSet getRoot();

  public abstract SchemaDocument getSourceDocument();

  public abstract Collection<XSComponent> select(String paramString, NamespaceContext paramNamespaceContext);

  public abstract XSComponent selectSingle(String paramString, NamespaceContext paramNamespaceContext);

  public abstract void visit(XSVisitor paramXSVisitor);

  public abstract <T> T apply(XSFunction<T> paramXSFunction);
}

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.xml.internal.xsom.XSComponent
 * JD-Core Version:    0.6.2
 */