package com.sun.tools.doclets.internal.toolkit;

import com.sun.javadoc.AnnotationTypeDoc;
import java.io.IOException;

public abstract interface AnnotationTypeWriter
{
  public abstract Content getHeader(String paramString);

  public abstract Content getAnnotationContentHeader();

  public abstract Content getAnnotationInfoTreeHeader();

  public abstract Content getAnnotationInfo(Content paramContent);

  public abstract void addAnnotationTypeSignature(String paramString, Content paramContent);

  public abstract void addAnnotationTypeDescription(Content paramContent);

  public abstract void addAnnotationTypeTagInfo(Content paramContent);

  public abstract void addAnnotationTypeDeprecationInfo(Content paramContent);

  public abstract Content getMemberTreeHeader();

  public abstract Content getMemberTree(Content paramContent);

  public abstract Content getMemberSummaryTree(Content paramContent);

  public abstract Content getMemberDetailsTree(Content paramContent);

  public abstract void addFooter(Content paramContent);

  public abstract void printDocument(Content paramContent)
    throws IOException;

  public abstract void close()
    throws IOException;

  public abstract AnnotationTypeDoc getAnnotationTypeDoc();
}

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.doclets.internal.toolkit.AnnotationTypeWriter
 * JD-Core Version:    0.6.2
 */