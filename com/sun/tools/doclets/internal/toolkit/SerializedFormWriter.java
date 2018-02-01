package com.sun.tools.doclets.internal.toolkit;

import com.sun.javadoc.ClassDoc;
import com.sun.javadoc.FieldDoc;
import com.sun.javadoc.MethodDoc;
import com.sun.javadoc.SerialFieldTag;
import java.io.IOException;

public abstract interface SerializedFormWriter
{
  public abstract Content getHeader(String paramString);

  public abstract Content getSerializedSummariesHeader();

  public abstract Content getPackageSerializedHeader();

  public abstract Content getPackageHeader(String paramString);

  public abstract Content getClassSerializedHeader();

  public abstract Content getClassHeader(ClassDoc paramClassDoc);

  public abstract Content getSerialUIDInfoHeader();

  public abstract void addSerialUIDInfo(String paramString1, String paramString2, Content paramContent);

  public abstract Content getClassContentHeader();

  public abstract SerialFieldWriter getSerialFieldWriter(ClassDoc paramClassDoc);

  public abstract SerialMethodWriter getSerialMethodWriter(ClassDoc paramClassDoc);

  public abstract void close()
    throws IOException;

  public abstract Content getSerializedContent(Content paramContent);

  public abstract void addFooter(Content paramContent);

  public abstract void printDocument(Content paramContent)
    throws IOException;

  public static abstract interface SerialFieldWriter
  {
    public abstract Content getSerializableFieldsHeader();

    public abstract Content getFieldsContentHeader(boolean paramBoolean);

    public abstract Content getSerializableFields(String paramString, Content paramContent);

    public abstract void addMemberDeprecatedInfo(FieldDoc paramFieldDoc, Content paramContent);

    public abstract void addMemberDescription(FieldDoc paramFieldDoc, Content paramContent);

    public abstract void addMemberDescription(SerialFieldTag paramSerialFieldTag, Content paramContent);

    public abstract void addMemberTags(FieldDoc paramFieldDoc, Content paramContent);

    public abstract void addMemberHeader(ClassDoc paramClassDoc, String paramString1, String paramString2, String paramString3, Content paramContent);

    public abstract boolean shouldPrintOverview(FieldDoc paramFieldDoc);
  }

  public static abstract interface SerialMethodWriter
  {
    public abstract Content getSerializableMethodsHeader();

    public abstract Content getMethodsContentHeader(boolean paramBoolean);

    public abstract Content getSerializableMethods(String paramString, Content paramContent);

    public abstract Content getNoCustomizationMsg(String paramString);

    public abstract void addMemberHeader(MethodDoc paramMethodDoc, Content paramContent);

    public abstract void addDeprecatedMemberInfo(MethodDoc paramMethodDoc, Content paramContent);

    public abstract void addMemberDescription(MethodDoc paramMethodDoc, Content paramContent);

    public abstract void addMemberTags(MethodDoc paramMethodDoc, Content paramContent);
  }
}

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.doclets.internal.toolkit.SerializedFormWriter
 * JD-Core Version:    0.6.2
 */