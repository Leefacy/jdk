package com.sun.tools.doclets.internal.toolkit;

import com.sun.javadoc.ClassDoc;
import com.sun.javadoc.FieldDoc;
import com.sun.javadoc.PackageDoc;
import java.io.IOException;
import java.util.List;
import java.util.Set;

public abstract interface ConstantsSummaryWriter
{
  public abstract void close()
    throws IOException;

  public abstract Content getHeader();

  public abstract Content getContentsHeader();

  public abstract void addLinkToPackageContent(PackageDoc paramPackageDoc, String paramString, Set<String> paramSet, Content paramContent);

  public abstract Content getContentsList(Content paramContent);

  public abstract Content getConstantSummaries();

  public abstract void addPackageName(PackageDoc paramPackageDoc, String paramString, Content paramContent);

  public abstract Content getClassConstantHeader();

  public abstract void addConstantMembers(ClassDoc paramClassDoc, List<FieldDoc> paramList, Content paramContent);

  public abstract void addFooter(Content paramContent);

  public abstract void printDocument(Content paramContent)
    throws IOException;
}

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.doclets.internal.toolkit.ConstantsSummaryWriter
 * JD-Core Version:    0.6.2
 */