package com.sun.tools.doclets.internal.toolkit;

import com.sun.javadoc.ClassDoc;
import com.sun.javadoc.FieldDoc;
import java.io.IOException;

public abstract interface EnumConstantWriter
{
  public abstract Content getEnumConstantsDetailsTreeHeader(ClassDoc paramClassDoc, Content paramContent);

  public abstract Content getEnumConstantsTreeHeader(FieldDoc paramFieldDoc, Content paramContent);

  public abstract Content getSignature(FieldDoc paramFieldDoc);

  public abstract void addDeprecated(FieldDoc paramFieldDoc, Content paramContent);

  public abstract void addComments(FieldDoc paramFieldDoc, Content paramContent);

  public abstract void addTags(FieldDoc paramFieldDoc, Content paramContent);

  public abstract Content getEnumConstantsDetails(Content paramContent);

  public abstract Content getEnumConstants(Content paramContent, boolean paramBoolean);

  public abstract void close()
    throws IOException;
}

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.doclets.internal.toolkit.EnumConstantWriter
 * JD-Core Version:    0.6.2
 */