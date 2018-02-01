package com.sun.xml.internal.rngom.ast.builder;

import com.sun.xml.internal.rngom.ast.om.Location;
import com.sun.xml.internal.rngom.ast.om.ParsedElementAnnotation;

public abstract interface Annotations<E extends ParsedElementAnnotation, L extends Location, CL extends CommentList<L>>
{
  public abstract void addAttribute(String paramString1, String paramString2, String paramString3, String paramString4, L paramL)
    throws BuildException;

  public abstract void addElement(E paramE)
    throws BuildException;

  public abstract void addComment(CL paramCL)
    throws BuildException;

  public abstract void addLeadingComment(CL paramCL)
    throws BuildException;
}

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.xml.internal.rngom.ast.builder.Annotations
 * JD-Core Version:    0.6.2
 */