package com.sun.tools.doclets.internal.toolkit;

import com.sun.javadoc.ClassDoc;
import com.sun.javadoc.ConstructorDoc;
import java.io.IOException;

public abstract interface ConstructorWriter
{
  public abstract Content getConstructorDetailsTreeHeader(ClassDoc paramClassDoc, Content paramContent);

  public abstract Content getConstructorDocTreeHeader(ConstructorDoc paramConstructorDoc, Content paramContent);

  public abstract Content getSignature(ConstructorDoc paramConstructorDoc);

  public abstract void addDeprecated(ConstructorDoc paramConstructorDoc, Content paramContent);

  public abstract void addComments(ConstructorDoc paramConstructorDoc, Content paramContent);

  public abstract void addTags(ConstructorDoc paramConstructorDoc, Content paramContent);

  public abstract Content getConstructorDetails(Content paramContent);

  public abstract Content getConstructorDoc(Content paramContent, boolean paramBoolean);

  public abstract void setFoundNonPubConstructor(boolean paramBoolean);

  public abstract void close()
    throws IOException;
}

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.doclets.internal.toolkit.ConstructorWriter
 * JD-Core Version:    0.6.2
 */