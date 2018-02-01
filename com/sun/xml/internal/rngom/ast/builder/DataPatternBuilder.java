package com.sun.xml.internal.rngom.ast.builder;

import com.sun.xml.internal.rngom.ast.om.Location;
import com.sun.xml.internal.rngom.ast.om.ParsedElementAnnotation;
import com.sun.xml.internal.rngom.ast.om.ParsedPattern;
import com.sun.xml.internal.rngom.parse.Context;

public abstract interface DataPatternBuilder<P extends ParsedPattern, E extends ParsedElementAnnotation, L extends Location, A extends Annotations<E, L, CL>, CL extends CommentList<L>>
{
  public abstract void addParam(String paramString1, String paramString2, Context paramContext, String paramString3, L paramL, A paramA)
    throws BuildException;

  public abstract void annotation(E paramE);

  public abstract P makePattern(L paramL, A paramA)
    throws BuildException;

  public abstract P makePattern(P paramP, L paramL, A paramA)
    throws BuildException;
}

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.xml.internal.rngom.ast.builder.DataPatternBuilder
 * JD-Core Version:    0.6.2
 */