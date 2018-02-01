package com.sun.tools.doclets.internal.toolkit;

import com.sun.javadoc.ClassDoc;
import java.io.IOException;

public abstract interface ClassWriter
{
  public abstract Content getHeader(String paramString);

  public abstract Content getClassContentHeader();

  public abstract void addClassTree(Content paramContent);

  public abstract Content getClassInfoTreeHeader();

  public abstract void addTypeParamInfo(Content paramContent);

  public abstract void addSuperInterfacesInfo(Content paramContent);

  public abstract void addImplementedInterfacesInfo(Content paramContent);

  public abstract void addSubClassInfo(Content paramContent);

  public abstract void addSubInterfacesInfo(Content paramContent);

  public abstract void addInterfaceUsageInfo(Content paramContent);

  public abstract void addFunctionalInterfaceInfo(Content paramContent);

  public abstract void addNestedClassInfo(Content paramContent);

  public abstract Content getClassInfo(Content paramContent);

  public abstract void addClassDeprecationInfo(Content paramContent);

  public abstract void addClassSignature(String paramString, Content paramContent);

  public abstract void addClassDescription(Content paramContent);

  public abstract void addClassTagInfo(Content paramContent);

  public abstract Content getMemberTreeHeader();

  public abstract void addFooter(Content paramContent);

  public abstract void printDocument(Content paramContent)
    throws IOException;

  public abstract void close()
    throws IOException;

  public abstract ClassDoc getClassDoc();

  public abstract Content getMemberSummaryTree(Content paramContent);

  public abstract Content getMemberDetailsTree(Content paramContent);
}

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.doclets.internal.toolkit.ClassWriter
 * JD-Core Version:    0.6.2
 */