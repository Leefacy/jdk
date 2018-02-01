package com.sun.xml.internal.rngom.ast.builder;

import com.sun.xml.internal.rngom.ast.om.Location;

public abstract interface CommentList<L extends Location>
{
  public abstract void addComment(String paramString, L paramL)
    throws BuildException;
}

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.xml.internal.rngom.ast.builder.CommentList
 * JD-Core Version:    0.6.2
 */