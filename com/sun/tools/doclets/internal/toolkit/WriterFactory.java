package com.sun.tools.doclets.internal.toolkit;

import com.sun.javadoc.AnnotationTypeDoc;
import com.sun.javadoc.ClassDoc;
import com.sun.javadoc.PackageDoc;
import com.sun.javadoc.Type;
import com.sun.tools.doclets.internal.toolkit.util.ClassTree;
import com.sun.tools.javac.jvm.Profile;

public abstract interface WriterFactory
{
  public abstract ConstantsSummaryWriter getConstantsSummaryWriter()
    throws Exception;

  public abstract PackageSummaryWriter getPackageSummaryWriter(PackageDoc paramPackageDoc1, PackageDoc paramPackageDoc2, PackageDoc paramPackageDoc3)
    throws Exception;

  public abstract ProfileSummaryWriter getProfileSummaryWriter(Profile paramProfile1, Profile paramProfile2, Profile paramProfile3)
    throws Exception;

  public abstract ProfilePackageSummaryWriter getProfilePackageSummaryWriter(PackageDoc paramPackageDoc1, PackageDoc paramPackageDoc2, PackageDoc paramPackageDoc3, Profile paramProfile)
    throws Exception;

  public abstract ClassWriter getClassWriter(ClassDoc paramClassDoc1, ClassDoc paramClassDoc2, ClassDoc paramClassDoc3, ClassTree paramClassTree)
    throws Exception;

  public abstract AnnotationTypeWriter getAnnotationTypeWriter(AnnotationTypeDoc paramAnnotationTypeDoc, Type paramType1, Type paramType2)
    throws Exception;

  public abstract MethodWriter getMethodWriter(ClassWriter paramClassWriter)
    throws Exception;

  public abstract AnnotationTypeFieldWriter getAnnotationTypeFieldWriter(AnnotationTypeWriter paramAnnotationTypeWriter)
    throws Exception;

  public abstract AnnotationTypeOptionalMemberWriter getAnnotationTypeOptionalMemberWriter(AnnotationTypeWriter paramAnnotationTypeWriter)
    throws Exception;

  public abstract AnnotationTypeRequiredMemberWriter getAnnotationTypeRequiredMemberWriter(AnnotationTypeWriter paramAnnotationTypeWriter)
    throws Exception;

  public abstract EnumConstantWriter getEnumConstantWriter(ClassWriter paramClassWriter)
    throws Exception;

  public abstract FieldWriter getFieldWriter(ClassWriter paramClassWriter)
    throws Exception;

  public abstract PropertyWriter getPropertyWriter(ClassWriter paramClassWriter)
    throws Exception;

  public abstract ConstructorWriter getConstructorWriter(ClassWriter paramClassWriter)
    throws Exception;

  public abstract MemberSummaryWriter getMemberSummaryWriter(ClassWriter paramClassWriter, int paramInt)
    throws Exception;

  public abstract MemberSummaryWriter getMemberSummaryWriter(AnnotationTypeWriter paramAnnotationTypeWriter, int paramInt)
    throws Exception;

  public abstract SerializedFormWriter getSerializedFormWriter()
    throws Exception;
}

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.doclets.internal.toolkit.WriterFactory
 * JD-Core Version:    0.6.2
 */