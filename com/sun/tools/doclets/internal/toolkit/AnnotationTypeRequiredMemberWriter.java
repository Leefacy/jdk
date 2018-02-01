package com.sun.tools.doclets.internal.toolkit;

import com.sun.javadoc.ClassDoc;
import com.sun.javadoc.MemberDoc;
import java.io.IOException;

public abstract interface AnnotationTypeRequiredMemberWriter
{
  public abstract Content getMemberTreeHeader();

  public abstract void addAnnotationDetailsMarker(Content paramContent);

  public abstract void addAnnotationDetailsTreeHeader(ClassDoc paramClassDoc, Content paramContent);

  public abstract Content getAnnotationDocTreeHeader(MemberDoc paramMemberDoc, Content paramContent);

  public abstract Content getAnnotationDetails(Content paramContent);

  public abstract Content getAnnotationDoc(Content paramContent, boolean paramBoolean);

  public abstract Content getSignature(MemberDoc paramMemberDoc);

  public abstract void addDeprecated(MemberDoc paramMemberDoc, Content paramContent);

  public abstract void addComments(MemberDoc paramMemberDoc, Content paramContent);

  public abstract void addTags(MemberDoc paramMemberDoc, Content paramContent);

  public abstract void close()
    throws IOException;
}

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.doclets.internal.toolkit.AnnotationTypeRequiredMemberWriter
 * JD-Core Version:    0.6.2
 */