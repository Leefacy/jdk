package com.sun.tools.doclets.internal.toolkit;

import com.sun.javadoc.ClassDoc;
import com.sun.javadoc.ProgramElementDoc;
import com.sun.javadoc.Tag;
import java.io.IOException;
import java.util.List;

public abstract interface MemberSummaryWriter
{
  public abstract Content getMemberSummaryHeader(ClassDoc paramClassDoc, Content paramContent);

  public abstract Content getSummaryTableTree(ClassDoc paramClassDoc, List<Content> paramList);

  public abstract void addMemberSummary(ClassDoc paramClassDoc, ProgramElementDoc paramProgramElementDoc, Tag[] paramArrayOfTag, List<Content> paramList, int paramInt);

  public abstract Content getInheritedSummaryHeader(ClassDoc paramClassDoc);

  public abstract void addInheritedMemberSummary(ClassDoc paramClassDoc, ProgramElementDoc paramProgramElementDoc, boolean paramBoolean1, boolean paramBoolean2, Content paramContent);

  public abstract Content getInheritedSummaryLinksTree();

  public abstract Content getMemberTree(Content paramContent);

  public abstract void close()
    throws IOException;
}

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.doclets.internal.toolkit.MemberSummaryWriter
 * JD-Core Version:    0.6.2
 */