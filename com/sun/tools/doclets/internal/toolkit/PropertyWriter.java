package com.sun.tools.doclets.internal.toolkit;

import com.sun.javadoc.ClassDoc;
import com.sun.javadoc.MethodDoc;
import java.io.IOException;

public abstract interface PropertyWriter
{
  public abstract Content getPropertyDetailsTreeHeader(ClassDoc paramClassDoc, Content paramContent);

  public abstract Content getPropertyDocTreeHeader(MethodDoc paramMethodDoc, Content paramContent);

  public abstract Content getSignature(MethodDoc paramMethodDoc);

  public abstract void addDeprecated(MethodDoc paramMethodDoc, Content paramContent);

  public abstract void addComments(MethodDoc paramMethodDoc, Content paramContent);

  public abstract void addTags(MethodDoc paramMethodDoc, Content paramContent);

  public abstract Content getPropertyDetails(Content paramContent);

  public abstract Content getPropertyDoc(Content paramContent, boolean paramBoolean);

  public abstract void close()
    throws IOException;
}

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.doclets.internal.toolkit.PropertyWriter
 * JD-Core Version:    0.6.2
 */