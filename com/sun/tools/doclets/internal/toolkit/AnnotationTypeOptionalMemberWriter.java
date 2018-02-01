package com.sun.tools.doclets.internal.toolkit;

import com.sun.javadoc.MemberDoc;

public abstract interface AnnotationTypeOptionalMemberWriter extends AnnotationTypeRequiredMemberWriter
{
  public abstract void addDefaultValueInfo(MemberDoc paramMemberDoc, Content paramContent);
}

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.doclets.internal.toolkit.AnnotationTypeOptionalMemberWriter
 * JD-Core Version:    0.6.2
 */