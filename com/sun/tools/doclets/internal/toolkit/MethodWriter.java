package com.sun.tools.doclets.internal.toolkit;

import com.sun.javadoc.ClassDoc;
import com.sun.javadoc.MethodDoc;
import com.sun.javadoc.Type;
import java.io.IOException;

public abstract interface MethodWriter
{
  public abstract Content getMethodDetailsTreeHeader(ClassDoc paramClassDoc, Content paramContent);

  public abstract Content getMethodDocTreeHeader(MethodDoc paramMethodDoc, Content paramContent);

  public abstract Content getSignature(MethodDoc paramMethodDoc);

  public abstract void addDeprecated(MethodDoc paramMethodDoc, Content paramContent);

  public abstract void addComments(Type paramType, MethodDoc paramMethodDoc, Content paramContent);

  public abstract void addTags(MethodDoc paramMethodDoc, Content paramContent);

  public abstract Content getMethodDetails(Content paramContent);

  public abstract Content getMethodDoc(Content paramContent, boolean paramBoolean);

  public abstract void close()
    throws IOException;
}

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.doclets.internal.toolkit.MethodWriter
 * JD-Core Version:    0.6.2
 */