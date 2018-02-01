package com.sun.xml.internal.rngom.ast.builder;

import com.sun.xml.internal.rngom.ast.om.Location;
import com.sun.xml.internal.rngom.ast.om.ParsedElementAnnotation;
import com.sun.xml.internal.rngom.ast.om.ParsedNameClass;
import java.util.List;

public abstract interface NameClassBuilder<N extends ParsedNameClass, E extends ParsedElementAnnotation, L extends Location, A extends Annotations<E, L, CL>, CL extends CommentList<L>>
{
  public abstract N annotate(N paramN, A paramA)
    throws BuildException;

  public abstract N annotateAfter(N paramN, E paramE)
    throws BuildException;

  public abstract N commentAfter(N paramN, CL paramCL)
    throws BuildException;

  public abstract N makeChoice(List<N> paramList, L paramL, A paramA);

  public abstract N makeName(String paramString1, String paramString2, String paramString3, L paramL, A paramA);

  public abstract N makeNsName(String paramString, L paramL, A paramA);

  public abstract N makeNsName(String paramString, N paramN, L paramL, A paramA);

  public abstract N makeAnyName(L paramL, A paramA);

  public abstract N makeAnyName(N paramN, L paramL, A paramA);

  public abstract N makeErrorNameClass();
}

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.xml.internal.rngom.ast.builder.NameClassBuilder
 * JD-Core Version:    0.6.2
 */