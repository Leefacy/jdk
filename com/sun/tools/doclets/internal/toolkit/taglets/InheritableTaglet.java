package com.sun.tools.doclets.internal.toolkit.taglets;

import com.sun.tools.doclets.internal.toolkit.util.DocFinder.Input;
import com.sun.tools.doclets.internal.toolkit.util.DocFinder.Output;

public abstract interface InheritableTaglet extends Taglet
{
  public abstract void inherit(DocFinder.Input paramInput, DocFinder.Output paramOutput);
}

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.doclets.internal.toolkit.taglets.InheritableTaglet
 * JD-Core Version:    0.6.2
 */