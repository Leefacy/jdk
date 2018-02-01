package com.sun.tools.doclets.internal.toolkit;

import com.sun.javadoc.ClassDoc;
import com.sun.javadoc.FieldDoc;
import java.io.IOException;

public abstract interface FieldWriter
{
  public abstract Content getFieldDetailsTreeHeader(ClassDoc paramClassDoc, Content paramContent);

  public abstract Content getFieldDocTreeHeader(FieldDoc paramFieldDoc, Content paramContent);

  public abstract Content getSignature(FieldDoc paramFieldDoc);

  public abstract void addDeprecated(FieldDoc paramFieldDoc, Content paramContent);

  public abstract void addComments(FieldDoc paramFieldDoc, Content paramContent);

  public abstract void addTags(FieldDoc paramFieldDoc, Content paramContent);

  public abstract Content getFieldDetails(Content paramContent);

  public abstract Content getFieldDoc(Content paramContent, boolean paramBoolean);

  public abstract void close()
    throws IOException;
}

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.doclets.internal.toolkit.FieldWriter
 * JD-Core Version:    0.6.2
 */