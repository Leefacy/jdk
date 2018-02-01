/*     */ package com.sun.tools.doclets.internal.toolkit.builders;
/*     */ 
/*     */ import com.sun.javadoc.ClassDoc;
/*     */ import com.sun.javadoc.Doc;
/*     */ import com.sun.javadoc.FieldDoc;
/*     */ import com.sun.javadoc.MemberDoc;
/*     */ import com.sun.javadoc.MethodDoc;
/*     */ import com.sun.javadoc.PackageDoc;
/*     */ import com.sun.javadoc.RootDoc;
/*     */ import com.sun.javadoc.SerialFieldTag;
/*     */ import com.sun.javadoc.Tag;
/*     */ import com.sun.javadoc.Type;
/*     */ import com.sun.tools.doclets.internal.toolkit.Configuration;
/*     */ import com.sun.tools.doclets.internal.toolkit.Content;
/*     */ import com.sun.tools.doclets.internal.toolkit.SerializedFormWriter;
/*     */ import com.sun.tools.doclets.internal.toolkit.SerializedFormWriter.SerialFieldWriter;
/*     */ import com.sun.tools.doclets.internal.toolkit.SerializedFormWriter.SerialMethodWriter;
/*     */ import com.sun.tools.doclets.internal.toolkit.WriterFactory;
/*     */ import com.sun.tools.doclets.internal.toolkit.util.DocletAbortException;
/*     */ import com.sun.tools.doclets.internal.toolkit.util.MessageRetriever;
/*     */ import com.sun.tools.doclets.internal.toolkit.util.Util;
/*     */ import com.sun.tools.javac.util.StringUtils;
/*     */ import java.io.IOException;
/*     */ import java.util.Arrays;
/*     */ 
/*     */ public class SerializedFormBuilder extends AbstractBuilder
/*     */ {
/*     */   public static final String NAME = "SerializedForm";
/*     */   private SerializedFormWriter writer;
/*     */   private SerializedFormWriter.SerialFieldWriter fieldWriter;
/*     */   private SerializedFormWriter.SerialMethodWriter methodWriter;
/*     */   private static final String SERIAL_VERSION_UID_HEADER = "serialVersionUID:";
/*     */   private PackageDoc currentPackage;
/*     */   private ClassDoc currentClass;
/*     */   protected MemberDoc currentMember;
/*     */   private Content contentTree;
/*     */ 
/*     */   private SerializedFormBuilder(AbstractBuilder.Context paramContext)
/*     */   {
/* 103 */     super(paramContext);
/*     */   }
/*     */ 
/*     */   public static SerializedFormBuilder getInstance(AbstractBuilder.Context paramContext)
/*     */   {
/* 111 */     return new SerializedFormBuilder(paramContext);
/*     */   }
/*     */ 
/*     */   public void build()
/*     */     throws IOException
/*     */   {
/* 118 */     if (!serialClassFoundToDocument(this.configuration.root.classes()))
/*     */     {
/* 120 */       return;
/*     */     }
/*     */     try {
/* 123 */       this.writer = this.configuration.getWriterFactory().getSerializedFormWriter();
/* 124 */       if (this.writer == null)
/*     */       {
/* 126 */         return;
/*     */       }
/*     */     } catch (Exception localException) {
/* 129 */       throw new DocletAbortException(localException);
/*     */     }
/* 131 */     build(this.layoutParser.parseXML("SerializedForm"), this.contentTree);
/* 132 */     this.writer.close();
/*     */   }
/*     */ 
/*     */   public String getName()
/*     */   {
/* 139 */     return "SerializedForm";
/*     */   }
/*     */ 
/*     */   public void buildSerializedForm(XMLNode paramXMLNode, Content paramContent)
/*     */     throws Exception
/*     */   {
/* 149 */     paramContent = this.writer.getHeader(this.configuration.getText("doclet.Serialized_Form"));
/*     */ 
/* 151 */     buildChildren(paramXMLNode, paramContent);
/* 152 */     this.writer.addFooter(paramContent);
/* 153 */     this.writer.printDocument(paramContent);
/* 154 */     this.writer.close();
/*     */   }
/*     */ 
/*     */   public void buildSerializedFormSummaries(XMLNode paramXMLNode, Content paramContent)
/*     */   {
/* 164 */     Content localContent = this.writer.getSerializedSummariesHeader();
/* 165 */     PackageDoc[] arrayOfPackageDoc = this.configuration.packages;
/* 166 */     for (int i = 0; i < arrayOfPackageDoc.length; i++) {
/* 167 */       this.currentPackage = arrayOfPackageDoc[i];
/* 168 */       buildChildren(paramXMLNode, localContent);
/*     */     }
/* 170 */     paramContent.addContent(this.writer.getSerializedContent(localContent));
/*     */   }
/*     */ 
/*     */   public void buildPackageSerializedForm(XMLNode paramXMLNode, Content paramContent)
/*     */   {
/* 181 */     Content localContent = this.writer.getPackageSerializedHeader();
/* 182 */     String str = this.currentPackage.name();
/* 183 */     ClassDoc[] arrayOfClassDoc = this.currentPackage.allClasses(false);
/* 184 */     if ((arrayOfClassDoc == null) || (arrayOfClassDoc.length == 0)) {
/* 185 */       return;
/*     */     }
/* 187 */     if (!serialInclude(this.currentPackage)) {
/* 188 */       return;
/*     */     }
/* 190 */     if (!serialClassFoundToDocument(arrayOfClassDoc)) {
/* 191 */       return;
/*     */     }
/* 193 */     buildChildren(paramXMLNode, localContent);
/* 194 */     paramContent.addContent(localContent);
/*     */   }
/*     */ 
/*     */   public void buildPackageHeader(XMLNode paramXMLNode, Content paramContent)
/*     */   {
/* 204 */     paramContent.addContent(this.writer.getPackageHeader(
/* 205 */       Util.getPackageName(this.currentPackage)));
/*     */   }
/*     */ 
/*     */   public void buildClassSerializedForm(XMLNode paramXMLNode, Content paramContent)
/*     */   {
/* 215 */     Content localContent1 = this.writer.getClassSerializedHeader();
/* 216 */     ClassDoc[] arrayOfClassDoc = this.currentPackage.allClasses(false);
/* 217 */     Arrays.sort(arrayOfClassDoc);
/* 218 */     for (int i = 0; i < arrayOfClassDoc.length; i++) {
/* 219 */       this.currentClass = arrayOfClassDoc[i];
/* 220 */       this.fieldWriter = this.writer.getSerialFieldWriter(this.currentClass);
/* 221 */       this.methodWriter = this.writer.getSerialMethodWriter(this.currentClass);
/* 222 */       if ((this.currentClass.isClass()) && (this.currentClass.isSerializable()) && 
/* 223 */         (serialClassInclude(this.currentClass)))
/*     */       {
/* 226 */         Content localContent2 = this.writer.getClassHeader(this.currentClass);
/* 227 */         buildChildren(paramXMLNode, localContent2);
/* 228 */         localContent1.addContent(localContent2);
/*     */       }
/*     */     }
/* 231 */     paramContent.addContent(localContent1);
/*     */   }
/*     */ 
/*     */   public void buildSerialUIDInfo(XMLNode paramXMLNode, Content paramContent)
/*     */   {
/* 241 */     Content localContent = this.writer.getSerialUIDInfoHeader();
/* 242 */     FieldDoc[] arrayOfFieldDoc = this.currentClass.fields(false);
/* 243 */     for (int i = 0; i < arrayOfFieldDoc.length; i++) {
/* 244 */       if ((arrayOfFieldDoc[i].name().equals("serialVersionUID")) && 
/* 245 */         (arrayOfFieldDoc[i]
/* 245 */         .constantValueExpression() != null)) {
/* 246 */         this.writer.addSerialUIDInfo("serialVersionUID:", arrayOfFieldDoc[i]
/* 247 */           .constantValueExpression(), localContent);
/* 248 */         break;
/*     */       }
/*     */     }
/* 251 */     paramContent.addContent(localContent);
/*     */   }
/*     */ 
/*     */   public void buildClassContent(XMLNode paramXMLNode, Content paramContent)
/*     */   {
/* 261 */     Content localContent = this.writer.getClassContentHeader();
/* 262 */     buildChildren(paramXMLNode, localContent);
/* 263 */     paramContent.addContent(localContent);
/*     */   }
/*     */ 
/*     */   public void buildSerializableMethods(XMLNode paramXMLNode, Content paramContent)
/*     */   {
/* 274 */     Content localContent1 = this.methodWriter.getSerializableMethodsHeader();
/* 275 */     MethodDoc[] arrayOfMethodDoc = this.currentClass.serializationMethods();
/* 276 */     int i = arrayOfMethodDoc.length;
/* 277 */     if (i > 0) {
/* 278 */       for (int j = 0; j < i; j++) {
/* 279 */         this.currentMember = arrayOfMethodDoc[j];
/* 280 */         Content localContent3 = this.methodWriter.getMethodsContentHeader(j == i - 1);
/*     */ 
/* 282 */         buildChildren(paramXMLNode, localContent3);
/* 283 */         localContent1.addContent(localContent3);
/*     */       }
/*     */     }
/* 286 */     if (this.currentClass.serializationMethods().length > 0) {
/* 287 */       paramContent.addContent(this.methodWriter.getSerializableMethods(this.configuration
/* 288 */         .getText("doclet.Serialized_Form_methods"), 
/* 288 */         localContent1));
/*     */ 
/* 290 */       if ((this.currentClass.isSerializable()) && (!this.currentClass.isExternalizable()) && 
/* 291 */         (this.currentClass.serializationMethods().length == 0)) {
/* 292 */         Content localContent2 = this.methodWriter.getNoCustomizationMsg(this.configuration
/* 293 */           .getText("doclet.Serializable_no_customization"));
/*     */ 
/* 295 */         paramContent.addContent(this.methodWriter.getSerializableMethods(this.configuration
/* 296 */           .getText("doclet.Serialized_Form_methods"), 
/* 296 */           localContent2));
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   public void buildMethodSubHeader(XMLNode paramXMLNode, Content paramContent)
/*     */   {
/* 310 */     this.methodWriter.addMemberHeader((MethodDoc)this.currentMember, paramContent);
/*     */   }
/*     */ 
/*     */   public void buildDeprecatedMethodInfo(XMLNode paramXMLNode, Content paramContent)
/*     */   {
/* 320 */     this.methodWriter.addDeprecatedMemberInfo((MethodDoc)this.currentMember, paramContent);
/*     */   }
/*     */ 
/*     */   public void buildMethodInfo(XMLNode paramXMLNode, Content paramContent)
/*     */   {
/* 330 */     if (this.configuration.nocomment) {
/* 331 */       return;
/*     */     }
/* 333 */     buildChildren(paramXMLNode, paramContent);
/*     */   }
/*     */ 
/*     */   public void buildMethodDescription(XMLNode paramXMLNode, Content paramContent)
/*     */   {
/* 343 */     this.methodWriter.addMemberDescription((MethodDoc)this.currentMember, paramContent);
/*     */   }
/*     */ 
/*     */   public void buildMethodTags(XMLNode paramXMLNode, Content paramContent)
/*     */   {
/* 353 */     this.methodWriter.addMemberTags((MethodDoc)this.currentMember, paramContent);
/* 354 */     MethodDoc localMethodDoc = (MethodDoc)this.currentMember;
/* 355 */     if ((localMethodDoc.name().compareTo("writeExternal") == 0) && 
/* 356 */       (localMethodDoc
/* 356 */       .tags("serialData").length == 0) && 
/* 357 */       (this.configuration.serialwarn))
/* 358 */       this.configuration.getDocletSpecificMsg().warning(this.currentMember
/* 359 */         .position(), "doclet.MissingSerialDataTag", new Object[] { localMethodDoc
/* 360 */         .containingClass().qualifiedName(), localMethodDoc.name() });
/*     */   }
/*     */ 
/*     */   public void buildFieldHeader(XMLNode paramXMLNode, Content paramContent)
/*     */   {
/* 372 */     if (this.currentClass.serializableFields().length > 0)
/* 373 */       buildFieldSerializationOverview(this.currentClass, paramContent);
/*     */   }
/*     */ 
/*     */   public void buildFieldSerializationOverview(ClassDoc paramClassDoc, Content paramContent)
/*     */   {
/* 384 */     if (paramClassDoc.definesSerializableFields()) {
/* 385 */       FieldDoc localFieldDoc = paramClassDoc.serializableFields()[0];
/*     */ 
/* 388 */       if (this.fieldWriter.shouldPrintOverview(localFieldDoc)) {
/* 389 */         Content localContent1 = this.fieldWriter.getSerializableFieldsHeader();
/* 390 */         Content localContent2 = this.fieldWriter.getFieldsContentHeader(true);
/* 391 */         this.fieldWriter.addMemberDeprecatedInfo(localFieldDoc, localContent2);
/*     */ 
/* 393 */         if (!this.configuration.nocomment) {
/* 394 */           this.fieldWriter.addMemberDescription(localFieldDoc, localContent2);
/*     */ 
/* 396 */           this.fieldWriter.addMemberTags(localFieldDoc, localContent2);
/*     */         }
/*     */ 
/* 399 */         localContent1.addContent(localContent2);
/* 400 */         paramContent.addContent(this.fieldWriter.getSerializableFields(this.configuration
/* 401 */           .getText("doclet.Serialized_Form_class"), 
/* 401 */           localContent1));
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   public void buildSerializableFields(XMLNode paramXMLNode, Content paramContent)
/*     */   {
/* 414 */     FieldDoc[] arrayOfFieldDoc = this.currentClass.serializableFields();
/* 415 */     int i = arrayOfFieldDoc.length;
/* 416 */     if (i > 0) {
/* 417 */       Content localContent1 = this.fieldWriter.getSerializableFieldsHeader();
/* 418 */       for (int j = 0; j < i; j++) {
/* 419 */         this.currentMember = arrayOfFieldDoc[j];
/* 420 */         if (!this.currentClass.definesSerializableFields()) {
/* 421 */           Content localContent2 = this.fieldWriter.getFieldsContentHeader(j == i - 1);
/*     */ 
/* 423 */           buildChildren(paramXMLNode, localContent2);
/* 424 */           localContent1.addContent(localContent2);
/*     */         }
/*     */         else {
/* 427 */           buildSerialFieldTagsInfo(localContent1);
/*     */         }
/*     */       }
/* 430 */       paramContent.addContent(this.fieldWriter.getSerializableFields(this.configuration
/* 431 */         .getText("doclet.Serialized_Form_fields"), 
/* 431 */         localContent1));
/*     */     }
/*     */   }
/*     */ 
/*     */   public void buildFieldSubHeader(XMLNode paramXMLNode, Content paramContent)
/*     */   {
/* 443 */     if (!this.currentClass.definesSerializableFields()) {
/* 444 */       FieldDoc localFieldDoc = (FieldDoc)this.currentMember;
/* 445 */       this.fieldWriter.addMemberHeader(localFieldDoc.type().asClassDoc(), localFieldDoc
/* 446 */         .type().typeName(), localFieldDoc.type().dimension(), localFieldDoc.name(), paramContent);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void buildFieldDeprecationInfo(XMLNode paramXMLNode, Content paramContent)
/*     */   {
/* 458 */     if (!this.currentClass.definesSerializableFields()) {
/* 459 */       FieldDoc localFieldDoc = (FieldDoc)this.currentMember;
/* 460 */       this.fieldWriter.addMemberDeprecatedInfo(localFieldDoc, paramContent);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void buildSerialFieldTagsInfo(Content paramContent)
/*     */   {
/* 470 */     if (this.configuration.nocomment) {
/* 471 */       return;
/*     */     }
/* 473 */     FieldDoc localFieldDoc = (FieldDoc)this.currentMember;
/*     */ 
/* 478 */     SerialFieldTag[] arrayOfSerialFieldTag = localFieldDoc.serialFieldTags();
/* 479 */     Arrays.sort(arrayOfSerialFieldTag);
/* 480 */     int i = arrayOfSerialFieldTag.length;
/* 481 */     for (int j = 0; j < i; j++)
/* 482 */       if ((arrayOfSerialFieldTag[j].fieldName() != null) && (arrayOfSerialFieldTag[j].fieldType() != null))
/*     */       {
/* 484 */         Content localContent = this.fieldWriter.getFieldsContentHeader(j == i - 1);
/*     */ 
/* 486 */         this.fieldWriter.addMemberHeader(arrayOfSerialFieldTag[j].fieldTypeDoc(), arrayOfSerialFieldTag[j]
/* 487 */           .fieldType(), "", arrayOfSerialFieldTag[j].fieldName(), localContent);
/* 488 */         this.fieldWriter.addMemberDescription(arrayOfSerialFieldTag[j], localContent);
/* 489 */         paramContent.addContent(localContent);
/*     */       }
/*     */   }
/*     */ 
/*     */   public void buildFieldInfo(XMLNode paramXMLNode, Content paramContent)
/*     */   {
/* 500 */     if (this.configuration.nocomment) {
/* 501 */       return;
/*     */     }
/* 503 */     FieldDoc localFieldDoc = (FieldDoc)this.currentMember;
/* 504 */     ClassDoc localClassDoc = localFieldDoc.containingClass();
/*     */ 
/* 506 */     if ((localFieldDoc.tags("serial").length == 0) && (!localFieldDoc.isSynthetic()) && (this.configuration.serialwarn))
/*     */     {
/* 508 */       this.configuration.message.warning(localFieldDoc.position(), "doclet.MissingSerialTag", new Object[] { localClassDoc
/* 509 */         .qualifiedName(), localFieldDoc
/* 510 */         .name() });
/*     */     }
/* 512 */     this.fieldWriter.addMemberDescription(localFieldDoc, paramContent);
/* 513 */     this.fieldWriter.addMemberTags(localFieldDoc, paramContent);
/*     */   }
/*     */ 
/*     */   public static boolean serialInclude(Doc paramDoc)
/*     */   {
/* 523 */     if (paramDoc == null) {
/* 524 */       return false;
/*     */     }
/*     */ 
/* 528 */     return paramDoc.isClass() ? 
/* 527 */       serialClassInclude((ClassDoc)paramDoc) : 
/* 528 */       serialDocInclude(paramDoc);
/*     */   }
/*     */ 
/*     */   private static boolean serialClassInclude(ClassDoc paramClassDoc)
/*     */   {
/* 538 */     if (paramClassDoc.isEnum())
/* 539 */       return false;
/*     */     try
/*     */     {
/* 542 */       paramClassDoc.superclassType();
/*     */     }
/*     */     catch (NullPointerException localNullPointerException) {
/* 545 */       return false;
/*     */     }
/* 547 */     if (paramClassDoc.isSerializable()) {
/* 548 */       if (paramClassDoc.tags("serial").length > 0)
/* 549 */         return serialDocInclude(paramClassDoc);
/* 550 */       if ((paramClassDoc.isPublic()) || (paramClassDoc.isProtected())) {
/* 551 */         return true;
/*     */       }
/* 553 */       return false;
/*     */     }
/*     */ 
/* 556 */     return false;
/*     */   }
/*     */ 
/*     */   private static boolean serialDocInclude(Doc paramDoc)
/*     */   {
/* 566 */     if (paramDoc.isEnum()) {
/* 567 */       return false;
/*     */     }
/* 569 */     Tag[] arrayOfTag = paramDoc.tags("serial");
/* 570 */     if (arrayOfTag.length > 0) {
/* 571 */       String str = StringUtils.toLowerCase(arrayOfTag[0].text());
/* 572 */       if (str.indexOf("exclude") >= 0)
/* 573 */         return false;
/* 574 */       if (str.indexOf("include") >= 0) {
/* 575 */         return true;
/*     */       }
/*     */     }
/* 578 */     return true;
/*     */   }
/*     */ 
/*     */   private boolean serialClassFoundToDocument(ClassDoc[] paramArrayOfClassDoc)
/*     */   {
/* 588 */     for (int i = 0; i < paramArrayOfClassDoc.length; i++) {
/* 589 */       if (serialClassInclude(paramArrayOfClassDoc[i])) {
/* 590 */         return true;
/*     */       }
/*     */     }
/* 593 */     return false;
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.doclets.internal.toolkit.builders.SerializedFormBuilder
 * JD-Core Version:    0.6.2
 */