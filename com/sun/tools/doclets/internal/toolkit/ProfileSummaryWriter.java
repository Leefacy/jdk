package com.sun.tools.doclets.internal.toolkit;

import com.sun.javadoc.ClassDoc;
import com.sun.javadoc.PackageDoc;
import java.io.IOException;

public abstract interface ProfileSummaryWriter
{
  public abstract Content getProfileHeader(String paramString);

  public abstract Content getContentHeader();

  public abstract Content getSummaryHeader();

  public abstract Content getSummaryTree(Content paramContent);

  public abstract Content getPackageSummaryHeader(PackageDoc paramPackageDoc);

  public abstract Content getPackageSummaryTree(Content paramContent);

  public abstract void addClassesSummary(ClassDoc[] paramArrayOfClassDoc, String paramString1, String paramString2, String[] paramArrayOfString, Content paramContent);

  public abstract void addProfileFooter(Content paramContent);

  public abstract void printDocument(Content paramContent)
    throws IOException;

  public abstract void close()
    throws IOException;
}

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.doclets.internal.toolkit.ProfileSummaryWriter
 * JD-Core Version:    0.6.2
 */