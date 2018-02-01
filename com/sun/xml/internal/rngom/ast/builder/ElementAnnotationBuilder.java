package com.sun.xml.internal.rngom.ast.builder;

import com.sun.xml.internal.rngom.ast.om.Location;
import com.sun.xml.internal.rngom.ast.om.ParsedElementAnnotation;
import com.sun.xml.internal.rngom.ast.om.ParsedPattern;

public abstract interface ElementAnnotationBuilder<P extends ParsedPattern, E extends ParsedElementAnnotation, L extends Location, A extends Annotations<E, L, CL>, CL extends CommentList<L>> extends Annotations<E, L, CL>
{
  public abstract void addText(String paramString, L paramL, CL paramCL)
    throws BuildException;

  public abstract E makeElementAnnotation()
    throws BuildException;
}

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.xml.internal.rngom.ast.builder.ElementAnnotationBuilder
 * JD-Core Version:    0.6.2
 */