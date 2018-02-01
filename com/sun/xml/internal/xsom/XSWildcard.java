package com.sun.xml.internal.xsom;

import com.sun.xml.internal.xsom.visitor.XSWildcardFunction;
import com.sun.xml.internal.xsom.visitor.XSWildcardVisitor;
import java.util.Collection;
import java.util.Iterator;

public abstract interface XSWildcard extends XSComponent, XSTerm
{
  public static final int LAX = 1;
  public static final int STRTICT = 2;
  public static final int SKIP = 3;

  public abstract int getMode();

  public abstract boolean acceptsNamespace(String paramString);

  public abstract void visit(XSWildcardVisitor paramXSWildcardVisitor);

  public abstract <T> T apply(XSWildcardFunction<T> paramXSWildcardFunction);

  public static abstract interface Any extends XSWildcard
  {
  }

  public static abstract interface Other extends XSWildcard
  {
    public abstract String getOtherNamespace();
  }

  public static abstract interface Union extends XSWildcard
  {
    public abstract Iterator<String> iterateNamespaces();

    public abstract Collection<String> getNamespaces();
  }
}

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.xml.internal.xsom.XSWildcard
 * JD-Core Version:    0.6.2
 */