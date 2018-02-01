/*     */ package com.sun.tools.classfile;
/*     */ 
/*     */ import java.io.ByteArrayOutputStream;
/*     */ import java.io.DataOutputStream;
/*     */ import java.io.File;
/*     */ import java.io.FileOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.OutputStream;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ 
/*     */ public class ClassWriter
/*     */ {
/*     */   protected ClassFile classFile;
/*     */   protected ClassOutputStream out;
/*     */   protected AttributeWriter attributeWriter;
/*     */   protected ConstantPoolWriter constantPoolWriter;
/*     */ 
/*     */   public ClassWriter()
/*     */   {
/*  51 */     this.attributeWriter = new AttributeWriter();
/*  52 */     this.constantPoolWriter = new ConstantPoolWriter();
/*  53 */     this.out = new ClassOutputStream();
/*     */   }
/*     */ 
/*     */   public void write(ClassFile paramClassFile, File paramFile)
/*     */     throws IOException
/*     */   {
/*  60 */     FileOutputStream localFileOutputStream = new FileOutputStream(paramFile);
/*     */     try {
/*  62 */       write(paramClassFile, localFileOutputStream);
/*     */     } finally {
/*  64 */       localFileOutputStream.close();
/*     */     }
/*     */   }
/*     */ 
/*     */   public void write(ClassFile paramClassFile, OutputStream paramOutputStream)
/*     */     throws IOException
/*     */   {
/*  72 */     this.classFile = paramClassFile;
/*  73 */     this.out.reset();
/*  74 */     write();
/*  75 */     this.out.writeTo(paramOutputStream);
/*     */   }
/*     */ 
/*     */   protected void write() throws IOException {
/*  79 */     writeHeader();
/*  80 */     writeConstantPool();
/*  81 */     writeAccessFlags(this.classFile.access_flags);
/*  82 */     writeClassInfo();
/*  83 */     writeFields();
/*  84 */     writeMethods();
/*  85 */     writeAttributes(this.classFile.attributes);
/*     */   }
/*     */ 
/*     */   protected void writeHeader() {
/*  89 */     this.out.writeInt(this.classFile.magic);
/*  90 */     this.out.writeShort(this.classFile.minor_version);
/*  91 */     this.out.writeShort(this.classFile.major_version);
/*     */   }
/*     */ 
/*     */   protected void writeAccessFlags(AccessFlags paramAccessFlags) {
/*  95 */     this.out.writeShort(paramAccessFlags.flags);
/*     */   }
/*     */ 
/*     */   protected void writeAttributes(Attributes paramAttributes) {
/*  99 */     int i = paramAttributes.size();
/* 100 */     this.out.writeShort(i);
/* 101 */     for (Attribute localAttribute : paramAttributes)
/* 102 */       this.attributeWriter.write(localAttribute, this.out);
/*     */   }
/*     */ 
/*     */   protected void writeClassInfo() {
/* 106 */     this.out.writeShort(this.classFile.this_class);
/* 107 */     this.out.writeShort(this.classFile.super_class);
/* 108 */     int[] arrayOfInt1 = this.classFile.interfaces;
/* 109 */     this.out.writeShort(arrayOfInt1.length);
/* 110 */     for (int k : arrayOfInt1)
/* 111 */       this.out.writeShort(k);
/*     */   }
/*     */ 
/*     */   protected void writeDescriptor(Descriptor paramDescriptor) {
/* 115 */     this.out.writeShort(paramDescriptor.index);
/*     */   }
/*     */ 
/*     */   protected void writeConstantPool() {
/* 119 */     ConstantPool localConstantPool = this.classFile.constant_pool;
/* 120 */     int i = localConstantPool.size();
/* 121 */     this.out.writeShort(i);
/* 122 */     for (ConstantPool.CPInfo localCPInfo : localConstantPool.entries())
/* 123 */       this.constantPoolWriter.write(localCPInfo, this.out);
/*     */   }
/*     */ 
/*     */   protected void writeFields() throws IOException {
/* 127 */     Field[] arrayOfField1 = this.classFile.fields;
/* 128 */     this.out.writeShort(arrayOfField1.length);
/* 129 */     for (Field localField : arrayOfField1)
/* 130 */       writeField(localField);
/*     */   }
/*     */ 
/*     */   protected void writeField(Field paramField) throws IOException {
/* 134 */     writeAccessFlags(paramField.access_flags);
/* 135 */     this.out.writeShort(paramField.name_index);
/* 136 */     writeDescriptor(paramField.descriptor);
/* 137 */     writeAttributes(paramField.attributes);
/*     */   }
/*     */ 
/*     */   protected void writeMethods() throws IOException {
/* 141 */     Method[] arrayOfMethod1 = this.classFile.methods;
/* 142 */     this.out.writeShort(arrayOfMethod1.length);
/* 143 */     for (Method localMethod : arrayOfMethod1)
/* 144 */       writeMethod(localMethod);
/*     */   }
/*     */ 
/*     */   protected void writeMethod(Method paramMethod) throws IOException
/*     */   {
/* 149 */     writeAccessFlags(paramMethod.access_flags);
/* 150 */     this.out.writeShort(paramMethod.name_index);
/* 151 */     writeDescriptor(paramMethod.descriptor);
/* 152 */     writeAttributes(paramMethod.attributes);
/*     */   }
/*     */ 
/*     */   protected static class AnnotationWriter
/*     */     implements Annotation.element_value.Visitor<Void, ClassWriter.ClassOutputStream>
/*     */   {
/*     */     public void write(Annotation[] paramArrayOfAnnotation, ClassWriter.ClassOutputStream paramClassOutputStream)
/*     */     {
/* 665 */       paramClassOutputStream.writeShort(paramArrayOfAnnotation.length);
/* 666 */       for (Annotation localAnnotation : paramArrayOfAnnotation)
/* 667 */         write(localAnnotation, paramClassOutputStream);
/*     */     }
/*     */ 
/*     */     public void write(TypeAnnotation[] paramArrayOfTypeAnnotation, ClassWriter.ClassOutputStream paramClassOutputStream) {
/* 671 */       paramClassOutputStream.writeShort(paramArrayOfTypeAnnotation.length);
/* 672 */       for (TypeAnnotation localTypeAnnotation : paramArrayOfTypeAnnotation)
/* 673 */         write(localTypeAnnotation, paramClassOutputStream);
/*     */     }
/*     */ 
/*     */     public void write(Annotation paramAnnotation, ClassWriter.ClassOutputStream paramClassOutputStream) {
/* 677 */       paramClassOutputStream.writeShort(paramAnnotation.type_index);
/* 678 */       paramClassOutputStream.writeShort(paramAnnotation.element_value_pairs.length);
/* 679 */       for (Annotation.element_value_pair localelement_value_pair : paramAnnotation.element_value_pairs)
/* 680 */         write(localelement_value_pair, paramClassOutputStream);
/*     */     }
/*     */ 
/*     */     public void write(TypeAnnotation paramTypeAnnotation, ClassWriter.ClassOutputStream paramClassOutputStream) {
/* 684 */       write(paramTypeAnnotation.position, paramClassOutputStream);
/* 685 */       write(paramTypeAnnotation.annotation, paramClassOutputStream);
/*     */     }
/*     */ 
/*     */     public void write(Annotation.element_value_pair paramelement_value_pair, ClassWriter.ClassOutputStream paramClassOutputStream) {
/* 689 */       paramClassOutputStream.writeShort(paramelement_value_pair.element_name_index);
/* 690 */       write(paramelement_value_pair.value, paramClassOutputStream);
/*     */     }
/*     */ 
/*     */     public void write(Annotation.element_value paramelement_value, ClassWriter.ClassOutputStream paramClassOutputStream) {
/* 694 */       paramClassOutputStream.writeByte(paramelement_value.tag);
/* 695 */       paramelement_value.accept(this, paramClassOutputStream);
/*     */     }
/*     */ 
/*     */     public Void visitPrimitive(Annotation.Primitive_element_value paramPrimitive_element_value, ClassWriter.ClassOutputStream paramClassOutputStream) {
/* 699 */       paramClassOutputStream.writeShort(paramPrimitive_element_value.const_value_index);
/* 700 */       return null;
/*     */     }
/*     */ 
/*     */     public Void visitEnum(Annotation.Enum_element_value paramEnum_element_value, ClassWriter.ClassOutputStream paramClassOutputStream) {
/* 704 */       paramClassOutputStream.writeShort(paramEnum_element_value.type_name_index);
/* 705 */       paramClassOutputStream.writeShort(paramEnum_element_value.const_name_index);
/* 706 */       return null;
/*     */     }
/*     */ 
/*     */     public Void visitClass(Annotation.Class_element_value paramClass_element_value, ClassWriter.ClassOutputStream paramClassOutputStream) {
/* 710 */       paramClassOutputStream.writeShort(paramClass_element_value.class_info_index);
/* 711 */       return null;
/*     */     }
/*     */ 
/*     */     public Void visitAnnotation(Annotation.Annotation_element_value paramAnnotation_element_value, ClassWriter.ClassOutputStream paramClassOutputStream) {
/* 715 */       write(paramAnnotation_element_value.annotation_value, paramClassOutputStream);
/* 716 */       return null;
/*     */     }
/*     */ 
/*     */     public Void visitArray(Annotation.Array_element_value paramArray_element_value, ClassWriter.ClassOutputStream paramClassOutputStream) {
/* 720 */       paramClassOutputStream.writeShort(paramArray_element_value.num_values);
/* 721 */       for (Annotation.element_value localelement_value : paramArray_element_value.values)
/* 722 */         write(localelement_value, paramClassOutputStream);
/* 723 */       return null;
/*     */     }
/*     */ 
/*     */     private void write(TypeAnnotation.Position paramPosition, ClassWriter.ClassOutputStream paramClassOutputStream)
/*     */     {
/* 728 */       paramClassOutputStream.writeByte(paramPosition.type.targetTypeValue());
/*     */       int j;
/* 729 */       switch (ClassWriter.1.$SwitchMap$com$sun$tools$classfile$TypeAnnotation$TargetType[paramPosition.type.ordinal()])
/*     */       {
/*     */       case 1:
/*     */       case 2:
/*     */       case 3:
/*     */       case 4:
/* 737 */         paramClassOutputStream.writeShort(paramPosition.offset);
/* 738 */         break;
/*     */       case 5:
/*     */       case 6:
/* 743 */         int i = paramPosition.lvarOffset.length;
/* 744 */         paramClassOutputStream.writeShort(i);
/* 745 */         for (j = 0; j < i; j++) {
/* 746 */           paramClassOutputStream.writeShort(1);
/* 747 */           paramClassOutputStream.writeShort(paramPosition.lvarOffset[j]);
/* 748 */           paramClassOutputStream.writeShort(paramPosition.lvarLength[j]);
/* 749 */           paramClassOutputStream.writeShort(paramPosition.lvarIndex[j]);
/*     */         }
/* 751 */         break;
/*     */       case 7:
/* 754 */         paramClassOutputStream.writeShort(paramPosition.exception_index);
/* 755 */         break;
/*     */       case 8:
/* 759 */         break;
/*     */       case 9:
/*     */       case 10:
/* 763 */         paramClassOutputStream.writeByte(paramPosition.parameter_index);
/* 764 */         break;
/*     */       case 11:
/*     */       case 12:
/* 768 */         paramClassOutputStream.writeByte(paramPosition.parameter_index);
/* 769 */         paramClassOutputStream.writeByte(paramPosition.bound_index);
/* 770 */         break;
/*     */       case 13:
/* 773 */         paramClassOutputStream.writeShort(paramPosition.type_index);
/* 774 */         break;
/*     */       case 14:
/* 777 */         paramClassOutputStream.writeShort(paramPosition.type_index);
/* 778 */         break;
/*     */       case 15:
/* 781 */         paramClassOutputStream.writeByte(paramPosition.parameter_index);
/* 782 */         break;
/*     */       case 16:
/*     */       case 17:
/*     */       case 18:
/*     */       case 19:
/*     */       case 20:
/* 790 */         paramClassOutputStream.writeShort(paramPosition.offset);
/* 791 */         paramClassOutputStream.writeByte(paramPosition.type_index);
/* 792 */         break;
/*     */       case 21:
/*     */       case 22:
/* 796 */         break;
/*     */       case 23:
/* 798 */         throw new AssertionError("ClassWriter: UNKNOWN target type should never occur!");
/*     */       default:
/* 800 */         throw new AssertionError("ClassWriter: Unknown target type for position: " + paramPosition);
/*     */       }
/*     */ 
/* 805 */       paramClassOutputStream.writeByte((byte)paramPosition.location.size());
/* 806 */       for (Iterator localIterator = TypeAnnotation.Position.getBinaryFromTypePath(paramPosition.location).iterator(); localIterator.hasNext(); ) { j = ((Integer)localIterator.next()).intValue();
/* 807 */         paramClassOutputStream.writeByte((byte)j);
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   protected static class AttributeWriter
/*     */     implements Attribute.Visitor<Void, ClassWriter.ClassOutputStream>
/*     */   {
/* 339 */     protected ClassWriter.ClassOutputStream sharedOut = new ClassWriter.ClassOutputStream();
/* 340 */     protected ClassWriter.AnnotationWriter annotationWriter = new ClassWriter.AnnotationWriter();
/*     */     protected ClassWriter.StackMapTableWriter stackMapWriter;
/*     */ 
/*     */     public void write(Attributes paramAttributes, ClassWriter.ClassOutputStream paramClassOutputStream)
/*     */     {
/* 324 */       int i = paramAttributes.size();
/* 325 */       paramClassOutputStream.writeShort(i);
/* 326 */       for (Attribute localAttribute : paramAttributes)
/* 327 */         write(localAttribute, paramClassOutputStream);
/*     */     }
/*     */ 
/*     */     public void write(Attribute paramAttribute, ClassWriter.ClassOutputStream paramClassOutputStream)
/*     */     {
/* 332 */       paramClassOutputStream.writeShort(paramAttribute.attribute_name_index);
/* 333 */       this.sharedOut.reset();
/* 334 */       paramAttribute.accept(this, this.sharedOut);
/* 335 */       paramClassOutputStream.writeInt(this.sharedOut.size());
/* 336 */       this.sharedOut.writeTo(paramClassOutputStream);
/*     */     }
/*     */ 
/*     */     public Void visitDefault(DefaultAttribute paramDefaultAttribute, ClassWriter.ClassOutputStream paramClassOutputStream)
/*     */     {
/* 343 */       paramClassOutputStream.write(paramDefaultAttribute.info, 0, paramDefaultAttribute.info.length);
/* 344 */       return null;
/*     */     }
/*     */ 
/*     */     public Void visitAnnotationDefault(AnnotationDefault_attribute paramAnnotationDefault_attribute, ClassWriter.ClassOutputStream paramClassOutputStream) {
/* 348 */       this.annotationWriter.write(paramAnnotationDefault_attribute.default_value, paramClassOutputStream);
/* 349 */       return null;
/*     */     }
/*     */ 
/*     */     public Void visitBootstrapMethods(BootstrapMethods_attribute paramBootstrapMethods_attribute, ClassWriter.ClassOutputStream paramClassOutputStream) {
/* 353 */       paramClassOutputStream.writeShort(paramBootstrapMethods_attribute.bootstrap_method_specifiers.length);
/* 354 */       for (BootstrapMethods_attribute.BootstrapMethodSpecifier localBootstrapMethodSpecifier : paramBootstrapMethods_attribute.bootstrap_method_specifiers) {
/* 355 */         paramClassOutputStream.writeShort(localBootstrapMethodSpecifier.bootstrap_method_ref);
/* 356 */         int k = localBootstrapMethodSpecifier.bootstrap_arguments.length;
/* 357 */         paramClassOutputStream.writeShort(k);
/* 358 */         for (int i1 : localBootstrapMethodSpecifier.bootstrap_arguments) {
/* 359 */           paramClassOutputStream.writeShort(i1);
/*     */         }
/*     */       }
/* 362 */       return null;
/*     */     }
/*     */ 
/*     */     public Void visitCharacterRangeTable(CharacterRangeTable_attribute paramCharacterRangeTable_attribute, ClassWriter.ClassOutputStream paramClassOutputStream) {
/* 366 */       paramClassOutputStream.writeShort(paramCharacterRangeTable_attribute.character_range_table.length);
/* 367 */       for (CharacterRangeTable_attribute.Entry localEntry : paramCharacterRangeTable_attribute.character_range_table)
/* 368 */         writeCharacterRangeTableEntry(localEntry, paramClassOutputStream);
/* 369 */       return null;
/*     */     }
/*     */ 
/*     */     protected void writeCharacterRangeTableEntry(CharacterRangeTable_attribute.Entry paramEntry, ClassWriter.ClassOutputStream paramClassOutputStream) {
/* 373 */       paramClassOutputStream.writeShort(paramEntry.start_pc);
/* 374 */       paramClassOutputStream.writeShort(paramEntry.end_pc);
/* 375 */       paramClassOutputStream.writeInt(paramEntry.character_range_start);
/* 376 */       paramClassOutputStream.writeInt(paramEntry.character_range_end);
/* 377 */       paramClassOutputStream.writeShort(paramEntry.flags);
/*     */     }
/*     */ 
/*     */     public Void visitCode(Code_attribute paramCode_attribute, ClassWriter.ClassOutputStream paramClassOutputStream) {
/* 381 */       paramClassOutputStream.writeShort(paramCode_attribute.max_stack);
/* 382 */       paramClassOutputStream.writeShort(paramCode_attribute.max_locals);
/* 383 */       paramClassOutputStream.writeInt(paramCode_attribute.code.length);
/* 384 */       paramClassOutputStream.write(paramCode_attribute.code, 0, paramCode_attribute.code.length);
/* 385 */       paramClassOutputStream.writeShort(paramCode_attribute.exception_table.length);
/* 386 */       for (Code_attribute.Exception_data localException_data : paramCode_attribute.exception_table)
/* 387 */         writeExceptionTableEntry(localException_data, paramClassOutputStream);
/* 388 */       new AttributeWriter().write(paramCode_attribute.attributes, paramClassOutputStream);
/* 389 */       return null;
/*     */     }
/*     */ 
/*     */     protected void writeExceptionTableEntry(Code_attribute.Exception_data paramException_data, ClassWriter.ClassOutputStream paramClassOutputStream) {
/* 393 */       paramClassOutputStream.writeShort(paramException_data.start_pc);
/* 394 */       paramClassOutputStream.writeShort(paramException_data.end_pc);
/* 395 */       paramClassOutputStream.writeShort(paramException_data.handler_pc);
/* 396 */       paramClassOutputStream.writeShort(paramException_data.catch_type);
/*     */     }
/*     */ 
/*     */     public Void visitCompilationID(CompilationID_attribute paramCompilationID_attribute, ClassWriter.ClassOutputStream paramClassOutputStream) {
/* 400 */       paramClassOutputStream.writeShort(paramCompilationID_attribute.compilationID_index);
/* 401 */       return null;
/*     */     }
/*     */ 
/*     */     public Void visitConstantValue(ConstantValue_attribute paramConstantValue_attribute, ClassWriter.ClassOutputStream paramClassOutputStream) {
/* 405 */       paramClassOutputStream.writeShort(paramConstantValue_attribute.constantvalue_index);
/* 406 */       return null;
/*     */     }
/*     */ 
/*     */     public Void visitDeprecated(Deprecated_attribute paramDeprecated_attribute, ClassWriter.ClassOutputStream paramClassOutputStream) {
/* 410 */       return null;
/*     */     }
/*     */ 
/*     */     public Void visitEnclosingMethod(EnclosingMethod_attribute paramEnclosingMethod_attribute, ClassWriter.ClassOutputStream paramClassOutputStream) {
/* 414 */       paramClassOutputStream.writeShort(paramEnclosingMethod_attribute.class_index);
/* 415 */       paramClassOutputStream.writeShort(paramEnclosingMethod_attribute.method_index);
/* 416 */       return null;
/*     */     }
/*     */ 
/*     */     public Void visitExceptions(Exceptions_attribute paramExceptions_attribute, ClassWriter.ClassOutputStream paramClassOutputStream) {
/* 420 */       paramClassOutputStream.writeShort(paramExceptions_attribute.exception_index_table.length);
/* 421 */       for (int k : paramExceptions_attribute.exception_index_table)
/* 422 */         paramClassOutputStream.writeShort(k);
/* 423 */       return null;
/*     */     }
/*     */ 
/*     */     public Void visitInnerClasses(InnerClasses_attribute paramInnerClasses_attribute, ClassWriter.ClassOutputStream paramClassOutputStream) {
/* 427 */       paramClassOutputStream.writeShort(paramInnerClasses_attribute.classes.length);
/* 428 */       for (InnerClasses_attribute.Info localInfo : paramInnerClasses_attribute.classes)
/* 429 */         writeInnerClassesInfo(localInfo, paramClassOutputStream);
/* 430 */       return null;
/*     */     }
/*     */ 
/*     */     protected void writeInnerClassesInfo(InnerClasses_attribute.Info paramInfo, ClassWriter.ClassOutputStream paramClassOutputStream) {
/* 434 */       paramClassOutputStream.writeShort(paramInfo.inner_class_info_index);
/* 435 */       paramClassOutputStream.writeShort(paramInfo.outer_class_info_index);
/* 436 */       paramClassOutputStream.writeShort(paramInfo.inner_name_index);
/* 437 */       writeAccessFlags(paramInfo.inner_class_access_flags, paramClassOutputStream);
/*     */     }
/*     */ 
/*     */     public Void visitLineNumberTable(LineNumberTable_attribute paramLineNumberTable_attribute, ClassWriter.ClassOutputStream paramClassOutputStream) {
/* 441 */       paramClassOutputStream.writeShort(paramLineNumberTable_attribute.line_number_table.length);
/* 442 */       for (LineNumberTable_attribute.Entry localEntry : paramLineNumberTable_attribute.line_number_table)
/* 443 */         writeLineNumberTableEntry(localEntry, paramClassOutputStream);
/* 444 */       return null;
/*     */     }
/*     */ 
/*     */     protected void writeLineNumberTableEntry(LineNumberTable_attribute.Entry paramEntry, ClassWriter.ClassOutputStream paramClassOutputStream) {
/* 448 */       paramClassOutputStream.writeShort(paramEntry.start_pc);
/* 449 */       paramClassOutputStream.writeShort(paramEntry.line_number);
/*     */     }
/*     */ 
/*     */     public Void visitLocalVariableTable(LocalVariableTable_attribute paramLocalVariableTable_attribute, ClassWriter.ClassOutputStream paramClassOutputStream) {
/* 453 */       paramClassOutputStream.writeShort(paramLocalVariableTable_attribute.local_variable_table.length);
/* 454 */       for (LocalVariableTable_attribute.Entry localEntry : paramLocalVariableTable_attribute.local_variable_table)
/* 455 */         writeLocalVariableTableEntry(localEntry, paramClassOutputStream);
/* 456 */       return null;
/*     */     }
/*     */ 
/*     */     protected void writeLocalVariableTableEntry(LocalVariableTable_attribute.Entry paramEntry, ClassWriter.ClassOutputStream paramClassOutputStream) {
/* 460 */       paramClassOutputStream.writeShort(paramEntry.start_pc);
/* 461 */       paramClassOutputStream.writeShort(paramEntry.length);
/* 462 */       paramClassOutputStream.writeShort(paramEntry.name_index);
/* 463 */       paramClassOutputStream.writeShort(paramEntry.descriptor_index);
/* 464 */       paramClassOutputStream.writeShort(paramEntry.index);
/*     */     }
/*     */ 
/*     */     public Void visitLocalVariableTypeTable(LocalVariableTypeTable_attribute paramLocalVariableTypeTable_attribute, ClassWriter.ClassOutputStream paramClassOutputStream) {
/* 468 */       paramClassOutputStream.writeShort(paramLocalVariableTypeTable_attribute.local_variable_table.length);
/* 469 */       for (LocalVariableTypeTable_attribute.Entry localEntry : paramLocalVariableTypeTable_attribute.local_variable_table)
/* 470 */         writeLocalVariableTypeTableEntry(localEntry, paramClassOutputStream);
/* 471 */       return null;
/*     */     }
/*     */ 
/*     */     protected void writeLocalVariableTypeTableEntry(LocalVariableTypeTable_attribute.Entry paramEntry, ClassWriter.ClassOutputStream paramClassOutputStream) {
/* 475 */       paramClassOutputStream.writeShort(paramEntry.start_pc);
/* 476 */       paramClassOutputStream.writeShort(paramEntry.length);
/* 477 */       paramClassOutputStream.writeShort(paramEntry.name_index);
/* 478 */       paramClassOutputStream.writeShort(paramEntry.signature_index);
/* 479 */       paramClassOutputStream.writeShort(paramEntry.index);
/*     */     }
/*     */ 
/*     */     public Void visitMethodParameters(MethodParameters_attribute paramMethodParameters_attribute, ClassWriter.ClassOutputStream paramClassOutputStream) {
/* 483 */       paramClassOutputStream.writeByte(paramMethodParameters_attribute.method_parameter_table.length);
/* 484 */       for (MethodParameters_attribute.Entry localEntry : paramMethodParameters_attribute.method_parameter_table) {
/* 485 */         paramClassOutputStream.writeShort(localEntry.name_index);
/* 486 */         paramClassOutputStream.writeShort(localEntry.flags);
/*     */       }
/* 488 */       return null;
/*     */     }
/*     */ 
/*     */     public Void visitRuntimeVisibleAnnotations(RuntimeVisibleAnnotations_attribute paramRuntimeVisibleAnnotations_attribute, ClassWriter.ClassOutputStream paramClassOutputStream) {
/* 492 */       this.annotationWriter.write(paramRuntimeVisibleAnnotations_attribute.annotations, paramClassOutputStream);
/* 493 */       return null;
/*     */     }
/*     */ 
/*     */     public Void visitRuntimeInvisibleAnnotations(RuntimeInvisibleAnnotations_attribute paramRuntimeInvisibleAnnotations_attribute, ClassWriter.ClassOutputStream paramClassOutputStream) {
/* 497 */       this.annotationWriter.write(paramRuntimeInvisibleAnnotations_attribute.annotations, paramClassOutputStream);
/* 498 */       return null;
/*     */     }
/*     */ 
/*     */     public Void visitRuntimeVisibleTypeAnnotations(RuntimeVisibleTypeAnnotations_attribute paramRuntimeVisibleTypeAnnotations_attribute, ClassWriter.ClassOutputStream paramClassOutputStream) {
/* 502 */       this.annotationWriter.write(paramRuntimeVisibleTypeAnnotations_attribute.annotations, paramClassOutputStream);
/* 503 */       return null;
/*     */     }
/*     */ 
/*     */     public Void visitRuntimeInvisibleTypeAnnotations(RuntimeInvisibleTypeAnnotations_attribute paramRuntimeInvisibleTypeAnnotations_attribute, ClassWriter.ClassOutputStream paramClassOutputStream) {
/* 507 */       this.annotationWriter.write(paramRuntimeInvisibleTypeAnnotations_attribute.annotations, paramClassOutputStream);
/* 508 */       return null;
/*     */     }
/*     */ 
/*     */     public Void visitRuntimeVisibleParameterAnnotations(RuntimeVisibleParameterAnnotations_attribute paramRuntimeVisibleParameterAnnotations_attribute, ClassWriter.ClassOutputStream paramClassOutputStream) {
/* 512 */       paramClassOutputStream.writeByte(paramRuntimeVisibleParameterAnnotations_attribute.parameter_annotations.length);
/* 513 */       for (Annotation[] arrayOfAnnotation1 : paramRuntimeVisibleParameterAnnotations_attribute.parameter_annotations)
/* 514 */         this.annotationWriter.write(arrayOfAnnotation1, paramClassOutputStream);
/* 515 */       return null;
/*     */     }
/*     */ 
/*     */     public Void visitRuntimeInvisibleParameterAnnotations(RuntimeInvisibleParameterAnnotations_attribute paramRuntimeInvisibleParameterAnnotations_attribute, ClassWriter.ClassOutputStream paramClassOutputStream) {
/* 519 */       paramClassOutputStream.writeByte(paramRuntimeInvisibleParameterAnnotations_attribute.parameter_annotations.length);
/* 520 */       for (Annotation[] arrayOfAnnotation1 : paramRuntimeInvisibleParameterAnnotations_attribute.parameter_annotations)
/* 521 */         this.annotationWriter.write(arrayOfAnnotation1, paramClassOutputStream);
/* 522 */       return null;
/*     */     }
/*     */ 
/*     */     public Void visitSignature(Signature_attribute paramSignature_attribute, ClassWriter.ClassOutputStream paramClassOutputStream) {
/* 526 */       paramClassOutputStream.writeShort(paramSignature_attribute.signature_index);
/* 527 */       return null;
/*     */     }
/*     */ 
/*     */     public Void visitSourceDebugExtension(SourceDebugExtension_attribute paramSourceDebugExtension_attribute, ClassWriter.ClassOutputStream paramClassOutputStream) {
/* 531 */       paramClassOutputStream.write(paramSourceDebugExtension_attribute.debug_extension, 0, paramSourceDebugExtension_attribute.debug_extension.length);
/* 532 */       return null;
/*     */     }
/*     */ 
/*     */     public Void visitSourceFile(SourceFile_attribute paramSourceFile_attribute, ClassWriter.ClassOutputStream paramClassOutputStream) {
/* 536 */       paramClassOutputStream.writeShort(paramSourceFile_attribute.sourcefile_index);
/* 537 */       return null;
/*     */     }
/*     */ 
/*     */     public Void visitSourceID(SourceID_attribute paramSourceID_attribute, ClassWriter.ClassOutputStream paramClassOutputStream) {
/* 541 */       paramClassOutputStream.writeShort(paramSourceID_attribute.sourceID_index);
/* 542 */       return null;
/*     */     }
/*     */ 
/*     */     public Void visitStackMap(StackMap_attribute paramStackMap_attribute, ClassWriter.ClassOutputStream paramClassOutputStream) {
/* 546 */       if (this.stackMapWriter == null) {
/* 547 */         this.stackMapWriter = new ClassWriter.StackMapTableWriter();
/*     */       }
/* 549 */       paramClassOutputStream.writeShort(paramStackMap_attribute.entries.length);
/* 550 */       for (StackMap_attribute.stack_map_frame localstack_map_frame : paramStackMap_attribute.entries)
/* 551 */         this.stackMapWriter.write(localstack_map_frame, paramClassOutputStream);
/* 552 */       return null;
/*     */     }
/*     */ 
/*     */     public Void visitStackMapTable(StackMapTable_attribute paramStackMapTable_attribute, ClassWriter.ClassOutputStream paramClassOutputStream) {
/* 556 */       if (this.stackMapWriter == null) {
/* 557 */         this.stackMapWriter = new ClassWriter.StackMapTableWriter();
/*     */       }
/* 559 */       paramClassOutputStream.writeShort(paramStackMapTable_attribute.entries.length);
/* 560 */       for (StackMapTable_attribute.stack_map_frame localstack_map_frame : paramStackMapTable_attribute.entries)
/* 561 */         this.stackMapWriter.write(localstack_map_frame, paramClassOutputStream);
/* 562 */       return null;
/*     */     }
/*     */ 
/*     */     public Void visitSynthetic(Synthetic_attribute paramSynthetic_attribute, ClassWriter.ClassOutputStream paramClassOutputStream) {
/* 566 */       return null;
/*     */     }
/*     */ 
/*     */     protected void writeAccessFlags(AccessFlags paramAccessFlags, ClassWriter.ClassOutputStream paramClassOutputStream) {
/* 570 */       this.sharedOut.writeShort(paramAccessFlags.flags);
/*     */     }
/*     */   }
/*     */ 
/*     */   protected static class ClassOutputStream extends ByteArrayOutputStream
/*     */   {
/*     */     private DataOutputStream d;
/*     */ 
/*     */     public ClassOutputStream()
/*     */     {
/* 168 */       this.d = new DataOutputStream(this);
/*     */     }
/*     */ 
/*     */     public void writeByte(int paramInt) {
/*     */       try {
/* 173 */         this.d.writeByte(paramInt);
/*     */       } catch (IOException localIOException) {
/*     */       }
/*     */     }
/*     */ 
/*     */     public void writeShort(int paramInt) {
/*     */       try {
/* 180 */         this.d.writeShort(paramInt);
/*     */       } catch (IOException localIOException) {
/*     */       }
/*     */     }
/*     */ 
/*     */     public void writeInt(int paramInt) {
/*     */       try {
/* 187 */         this.d.writeInt(paramInt);
/*     */       } catch (IOException localIOException) {
/*     */       }
/*     */     }
/*     */ 
/*     */     public void writeLong(long paramLong) {
/*     */       try {
/* 194 */         this.d.writeLong(paramLong);
/*     */       } catch (IOException localIOException) {
/*     */       }
/*     */     }
/*     */ 
/*     */     public void writeFloat(float paramFloat) {
/*     */       try {
/* 201 */         this.d.writeFloat(paramFloat);
/*     */       } catch (IOException localIOException) {
/*     */       }
/*     */     }
/*     */ 
/*     */     public void writeDouble(double paramDouble) {
/*     */       try {
/* 208 */         this.d.writeDouble(paramDouble);
/*     */       } catch (IOException localIOException) {
/*     */       }
/*     */     }
/*     */ 
/*     */     public void writeUTF(String paramString) {
/*     */       try {
/* 215 */         this.d.writeUTF(paramString);
/*     */       } catch (IOException localIOException) {
/*     */       }
/*     */     }
/*     */ 
/*     */     public void writeTo(ClassOutputStream paramClassOutputStream) {
/*     */       try {
/* 222 */         super.writeTo(paramClassOutputStream);
/*     */       }
/*     */       catch (IOException localIOException)
/*     */       {
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   protected static class ConstantPoolWriter
/*     */     implements ConstantPool.Visitor<Integer, ClassWriter.ClassOutputStream>
/*     */   {
/*     */     protected int write(ConstantPool.CPInfo paramCPInfo, ClassWriter.ClassOutputStream paramClassOutputStream)
/*     */     {
/* 236 */       paramClassOutputStream.writeByte(paramCPInfo.getTag());
/* 237 */       return ((Integer)paramCPInfo.accept(this, paramClassOutputStream)).intValue();
/*     */     }
/*     */ 
/*     */     public Integer visitClass(ConstantPool.CONSTANT_Class_info paramCONSTANT_Class_info, ClassWriter.ClassOutputStream paramClassOutputStream) {
/* 241 */       paramClassOutputStream.writeShort(paramCONSTANT_Class_info.name_index);
/* 242 */       return Integer.valueOf(1);
/*     */     }
/*     */ 
/*     */     public Integer visitDouble(ConstantPool.CONSTANT_Double_info paramCONSTANT_Double_info, ClassWriter.ClassOutputStream paramClassOutputStream) {
/* 246 */       paramClassOutputStream.writeDouble(paramCONSTANT_Double_info.value);
/* 247 */       return Integer.valueOf(2);
/*     */     }
/*     */ 
/*     */     public Integer visitFieldref(ConstantPool.CONSTANT_Fieldref_info paramCONSTANT_Fieldref_info, ClassWriter.ClassOutputStream paramClassOutputStream) {
/* 251 */       writeRef(paramCONSTANT_Fieldref_info, paramClassOutputStream);
/* 252 */       return Integer.valueOf(1);
/*     */     }
/*     */ 
/*     */     public Integer visitFloat(ConstantPool.CONSTANT_Float_info paramCONSTANT_Float_info, ClassWriter.ClassOutputStream paramClassOutputStream) {
/* 256 */       paramClassOutputStream.writeFloat(paramCONSTANT_Float_info.value);
/* 257 */       return Integer.valueOf(1);
/*     */     }
/*     */ 
/*     */     public Integer visitInteger(ConstantPool.CONSTANT_Integer_info paramCONSTANT_Integer_info, ClassWriter.ClassOutputStream paramClassOutputStream) {
/* 261 */       paramClassOutputStream.writeInt(paramCONSTANT_Integer_info.value);
/* 262 */       return Integer.valueOf(1);
/*     */     }
/*     */ 
/*     */     public Integer visitInterfaceMethodref(ConstantPool.CONSTANT_InterfaceMethodref_info paramCONSTANT_InterfaceMethodref_info, ClassWriter.ClassOutputStream paramClassOutputStream) {
/* 266 */       writeRef(paramCONSTANT_InterfaceMethodref_info, paramClassOutputStream);
/* 267 */       return Integer.valueOf(1);
/*     */     }
/*     */ 
/*     */     public Integer visitInvokeDynamic(ConstantPool.CONSTANT_InvokeDynamic_info paramCONSTANT_InvokeDynamic_info, ClassWriter.ClassOutputStream paramClassOutputStream) {
/* 271 */       paramClassOutputStream.writeShort(paramCONSTANT_InvokeDynamic_info.bootstrap_method_attr_index);
/* 272 */       paramClassOutputStream.writeShort(paramCONSTANT_InvokeDynamic_info.name_and_type_index);
/* 273 */       return Integer.valueOf(1);
/*     */     }
/*     */ 
/*     */     public Integer visitLong(ConstantPool.CONSTANT_Long_info paramCONSTANT_Long_info, ClassWriter.ClassOutputStream paramClassOutputStream) {
/* 277 */       paramClassOutputStream.writeLong(paramCONSTANT_Long_info.value);
/* 278 */       return Integer.valueOf(2);
/*     */     }
/*     */ 
/*     */     public Integer visitNameAndType(ConstantPool.CONSTANT_NameAndType_info paramCONSTANT_NameAndType_info, ClassWriter.ClassOutputStream paramClassOutputStream) {
/* 282 */       paramClassOutputStream.writeShort(paramCONSTANT_NameAndType_info.name_index);
/* 283 */       paramClassOutputStream.writeShort(paramCONSTANT_NameAndType_info.type_index);
/* 284 */       return Integer.valueOf(1);
/*     */     }
/*     */ 
/*     */     public Integer visitMethodHandle(ConstantPool.CONSTANT_MethodHandle_info paramCONSTANT_MethodHandle_info, ClassWriter.ClassOutputStream paramClassOutputStream) {
/* 288 */       paramClassOutputStream.writeByte(paramCONSTANT_MethodHandle_info.reference_kind.tag);
/* 289 */       paramClassOutputStream.writeShort(paramCONSTANT_MethodHandle_info.reference_index);
/* 290 */       return Integer.valueOf(1);
/*     */     }
/*     */ 
/*     */     public Integer visitMethodType(ConstantPool.CONSTANT_MethodType_info paramCONSTANT_MethodType_info, ClassWriter.ClassOutputStream paramClassOutputStream) {
/* 294 */       paramClassOutputStream.writeShort(paramCONSTANT_MethodType_info.descriptor_index);
/* 295 */       return Integer.valueOf(1);
/*     */     }
/*     */ 
/*     */     public Integer visitMethodref(ConstantPool.CONSTANT_Methodref_info paramCONSTANT_Methodref_info, ClassWriter.ClassOutputStream paramClassOutputStream) {
/* 299 */       return writeRef(paramCONSTANT_Methodref_info, paramClassOutputStream);
/*     */     }
/*     */ 
/*     */     public Integer visitString(ConstantPool.CONSTANT_String_info paramCONSTANT_String_info, ClassWriter.ClassOutputStream paramClassOutputStream) {
/* 303 */       paramClassOutputStream.writeShort(paramCONSTANT_String_info.string_index);
/* 304 */       return Integer.valueOf(1);
/*     */     }
/*     */ 
/*     */     public Integer visitUtf8(ConstantPool.CONSTANT_Utf8_info paramCONSTANT_Utf8_info, ClassWriter.ClassOutputStream paramClassOutputStream) {
/* 308 */       paramClassOutputStream.writeUTF(paramCONSTANT_Utf8_info.value);
/* 309 */       return Integer.valueOf(1);
/*     */     }
/*     */ 
/*     */     protected Integer writeRef(ConstantPool.CPRefInfo paramCPRefInfo, ClassWriter.ClassOutputStream paramClassOutputStream) {
/* 313 */       paramClassOutputStream.writeShort(paramCPRefInfo.class_index);
/* 314 */       paramClassOutputStream.writeShort(paramCPRefInfo.name_and_type_index);
/* 315 */       return Integer.valueOf(1);
/*     */     }
/*     */   }
/*     */ 
/*     */   protected static class StackMapTableWriter
/*     */     implements StackMapTable_attribute.stack_map_frame.Visitor<Void, ClassWriter.ClassOutputStream>
/*     */   {
/*     */     public void write(StackMapTable_attribute.stack_map_frame paramstack_map_frame, ClassWriter.ClassOutputStream paramClassOutputStream)
/*     */     {
/* 583 */       paramClassOutputStream.write(paramstack_map_frame.frame_type);
/* 584 */       paramstack_map_frame.accept(this, paramClassOutputStream);
/*     */     }
/*     */ 
/*     */     public Void visit_same_frame(StackMapTable_attribute.same_frame paramsame_frame, ClassWriter.ClassOutputStream paramClassOutputStream) {
/* 588 */       return null;
/*     */     }
/*     */ 
/*     */     public Void visit_same_locals_1_stack_item_frame(StackMapTable_attribute.same_locals_1_stack_item_frame paramsame_locals_1_stack_item_frame, ClassWriter.ClassOutputStream paramClassOutputStream) {
/* 592 */       writeVerificationTypeInfo(paramsame_locals_1_stack_item_frame.stack[0], paramClassOutputStream);
/* 593 */       return null;
/*     */     }
/*     */ 
/*     */     public Void visit_same_locals_1_stack_item_frame_extended(StackMapTable_attribute.same_locals_1_stack_item_frame_extended paramsame_locals_1_stack_item_frame_extended, ClassWriter.ClassOutputStream paramClassOutputStream) {
/* 597 */       paramClassOutputStream.writeShort(paramsame_locals_1_stack_item_frame_extended.offset_delta);
/* 598 */       writeVerificationTypeInfo(paramsame_locals_1_stack_item_frame_extended.stack[0], paramClassOutputStream);
/* 599 */       return null;
/*     */     }
/*     */ 
/*     */     public Void visit_chop_frame(StackMapTable_attribute.chop_frame paramchop_frame, ClassWriter.ClassOutputStream paramClassOutputStream) {
/* 603 */       paramClassOutputStream.writeShort(paramchop_frame.offset_delta);
/* 604 */       return null;
/*     */     }
/*     */ 
/*     */     public Void visit_same_frame_extended(StackMapTable_attribute.same_frame_extended paramsame_frame_extended, ClassWriter.ClassOutputStream paramClassOutputStream) {
/* 608 */       paramClassOutputStream.writeShort(paramsame_frame_extended.offset_delta);
/* 609 */       return null;
/*     */     }
/*     */ 
/*     */     public Void visit_append_frame(StackMapTable_attribute.append_frame paramappend_frame, ClassWriter.ClassOutputStream paramClassOutputStream) {
/* 613 */       paramClassOutputStream.writeShort(paramappend_frame.offset_delta);
/* 614 */       for (StackMapTable_attribute.verification_type_info localverification_type_info : paramappend_frame.locals)
/* 615 */         writeVerificationTypeInfo(localverification_type_info, paramClassOutputStream);
/* 616 */       return null;
/*     */     }
/*     */ 
/*     */     public Void visit_full_frame(StackMapTable_attribute.full_frame paramfull_frame, ClassWriter.ClassOutputStream paramClassOutputStream) {
/* 620 */       paramClassOutputStream.writeShort(paramfull_frame.offset_delta);
/* 621 */       paramClassOutputStream.writeShort(paramfull_frame.locals.length);
/*     */       StackMapTable_attribute.verification_type_info localverification_type_info;
/* 622 */       for (localverification_type_info : paramfull_frame.locals)
/* 623 */         writeVerificationTypeInfo(localverification_type_info, paramClassOutputStream);
/* 624 */       paramClassOutputStream.writeShort(paramfull_frame.stack.length);
/* 625 */       for (localverification_type_info : paramfull_frame.stack)
/* 626 */         writeVerificationTypeInfo(localverification_type_info, paramClassOutputStream);
/* 627 */       return null;
/*     */     }
/*     */ 
/*     */     protected void writeVerificationTypeInfo(StackMapTable_attribute.verification_type_info paramverification_type_info, ClassWriter.ClassOutputStream paramClassOutputStream)
/*     */     {
/* 632 */       paramClassOutputStream.write(paramverification_type_info.tag);
/* 633 */       switch (paramverification_type_info.tag) {
/*     */       case 0:
/*     */       case 1:
/*     */       case 2:
/*     */       case 3:
/*     */       case 4:
/*     */       case 5:
/*     */       case 6:
/* 641 */         break;
/*     */       case 7:
/* 644 */         StackMapTable_attribute.Object_variable_info localObject_variable_info = (StackMapTable_attribute.Object_variable_info)paramverification_type_info;
/* 645 */         paramClassOutputStream.writeShort(localObject_variable_info.cpool_index);
/* 646 */         break;
/*     */       case 8:
/* 649 */         StackMapTable_attribute.Uninitialized_variable_info localUninitialized_variable_info = (StackMapTable_attribute.Uninitialized_variable_info)paramverification_type_info;
/* 650 */         paramClassOutputStream.writeShort(localUninitialized_variable_info.offset);
/* 651 */         break;
/*     */       default:
/* 654 */         throw new Error();
/*     */       }
/*     */     }
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.classfile.ClassWriter
 * JD-Core Version:    0.6.2
 */