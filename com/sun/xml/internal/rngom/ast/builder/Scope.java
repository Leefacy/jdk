package com.sun.xml.internal.rngom.ast.builder;

import com.sun.xml.internal.rngom.ast.om.Location;
import com.sun.xml.internal.rngom.ast.om.ParsedElementAnnotation;
import com.sun.xml.internal.rngom.ast.om.ParsedPattern;

public abstract interface Scope<P extends ParsedPattern, E extends ParsedElementAnnotation, L extends Location, A extends Annotations<E, L, CL>, CL extends CommentList<L>> extends GrammarSection<P, E, L, A, CL>
{
  public abstract P makeParentRef(String paramString, L paramL, A paramA)
    throws BuildException;

  public abstract P makeRef(String paramString, L paramL, A paramA)
    throws BuildException;
}

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.xml.internal.rngom.ast.builder.Scope
 * JD-Core Version:    0.6.2
 */