package com.sun.tools.doclets.internal.toolkit;

import com.sun.javadoc.ClassDoc;
import java.io.IOException;

public abstract interface PackageSummaryWriter
{
  public abstract Content getPackageHeader(String paramString);

  public abstract Content getContentHeader();

  public abstract Content getSummaryHeader();

  public abstract void addClassesSummary(ClassDoc[] paramArrayOfClassDoc, String paramString1, String paramString2, String[] paramArrayOfString, Content paramContent);

  public abstract void addPackageDescription(Content paramContent);

  public abstract void addPackageTags(Content paramContent);

  public abstract void addPackageFooter(Content paramContent);

  public abstract void printDocument(Content paramContent)
    throws IOException;

  public abstract void close()
    throws IOException;
}

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.doclets.internal.toolkit.PackageSummaryWriter
 * JD-Core Version:    0.6.2
 */