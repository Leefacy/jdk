/*     */ package com.sun.tools.javap;
/*     */ 
/*     */ import com.sun.tools.classfile.AccessFlags;
/*     */ import com.sun.tools.classfile.AnnotationDefault_attribute;
/*     */ import com.sun.tools.classfile.Attribute;
/*     */ import com.sun.tools.classfile.Attribute.Visitor;
/*     */ import com.sun.tools.classfile.Attributes;
/*     */ import com.sun.tools.classfile.BootstrapMethods_attribute;
/*     */ import com.sun.tools.classfile.BootstrapMethods_attribute.BootstrapMethodSpecifier;
/*     */ import com.sun.tools.classfile.CharacterRangeTable_attribute;
/*     */ import com.sun.tools.classfile.CharacterRangeTable_attribute.Entry;
/*     */ import com.sun.tools.classfile.Code_attribute;
/*     */ import com.sun.tools.classfile.CompilationID_attribute;
/*     */ import com.sun.tools.classfile.ConstantPool;
/*     */ import com.sun.tools.classfile.ConstantPoolException;
/*     */ import com.sun.tools.classfile.ConstantValue_attribute;
/*     */ import com.sun.tools.classfile.DefaultAttribute;
/*     */ import com.sun.tools.classfile.Deprecated_attribute;
/*     */ import com.sun.tools.classfile.EnclosingMethod_attribute;
/*     */ import com.sun.tools.classfile.Exceptions_attribute;
/*     */ import com.sun.tools.classfile.InnerClasses_attribute;
/*     */ import com.sun.tools.classfile.InnerClasses_attribute.Info;
/*     */ import com.sun.tools.classfile.LineNumberTable_attribute;
/*     */ import com.sun.tools.classfile.LineNumberTable_attribute.Entry;
/*     */ import com.sun.tools.classfile.LocalVariableTable_attribute;
/*     */ import com.sun.tools.classfile.LocalVariableTable_attribute.Entry;
/*     */ import com.sun.tools.classfile.LocalVariableTypeTable_attribute;
/*     */ import com.sun.tools.classfile.LocalVariableTypeTable_attribute.Entry;
/*     */ import com.sun.tools.classfile.MethodParameters_attribute;
/*     */ import com.sun.tools.classfile.MethodParameters_attribute.Entry;
/*     */ import com.sun.tools.classfile.RuntimeInvisibleAnnotations_attribute;
/*     */ import com.sun.tools.classfile.RuntimeInvisibleParameterAnnotations_attribute;
/*     */ import com.sun.tools.classfile.RuntimeInvisibleTypeAnnotations_attribute;
/*     */ import com.sun.tools.classfile.RuntimeVisibleAnnotations_attribute;
/*     */ import com.sun.tools.classfile.RuntimeVisibleParameterAnnotations_attribute;
/*     */ import com.sun.tools.classfile.RuntimeVisibleTypeAnnotations_attribute;
/*     */ import com.sun.tools.classfile.Signature_attribute;
/*     */ import com.sun.tools.classfile.SourceDebugExtension_attribute;
/*     */ import com.sun.tools.classfile.SourceFile_attribute;
/*     */ import com.sun.tools.classfile.SourceID_attribute;
/*     */ import com.sun.tools.classfile.StackMapTable_attribute;
/*     */ import com.sun.tools.classfile.StackMapTable_attribute.Object_variable_info;
/*     */ import com.sun.tools.classfile.StackMapTable_attribute.Uninitialized_variable_info;
/*     */ import com.sun.tools.classfile.StackMapTable_attribute.append_frame;
/*     */ import com.sun.tools.classfile.StackMapTable_attribute.chop_frame;
/*     */ import com.sun.tools.classfile.StackMapTable_attribute.full_frame;
/*     */ import com.sun.tools.classfile.StackMapTable_attribute.same_frame;
/*     */ import com.sun.tools.classfile.StackMapTable_attribute.same_frame_extended;
/*     */ import com.sun.tools.classfile.StackMapTable_attribute.same_locals_1_stack_item_frame;
/*     */ import com.sun.tools.classfile.StackMapTable_attribute.same_locals_1_stack_item_frame_extended;
/*     */ import com.sun.tools.classfile.StackMapTable_attribute.stack_map_frame;
/*     */ import com.sun.tools.classfile.StackMapTable_attribute.stack_map_frame.Visitor;
/*     */ import com.sun.tools.classfile.StackMapTable_attribute.verification_type_info;
/*     */ import com.sun.tools.classfile.StackMap_attribute;
/*     */ import com.sun.tools.classfile.StackMap_attribute.stack_map_frame;
/*     */ import com.sun.tools.classfile.Synthetic_attribute;
/*     */ import com.sun.tools.javac.util.StringUtils;
/*     */ 
/*     */ public class AttributeWriter extends BasicWriter
/*     */   implements Attribute.Visitor<Void, Void>
/*     */ {
/*     */   private static final String format = "%-31s%s";
/*     */   private AnnotationWriter annotationWriter;
/*     */   private CodeWriter codeWriter;
/*     */   private ConstantWriter constantWriter;
/*     */   private Options options;
/*     */   private ConstantPool constant_pool;
/*     */   private Object owner;
/*     */ 
/*     */   public static AttributeWriter instance(Context paramContext)
/*     */   {
/*  79 */     AttributeWriter localAttributeWriter = (AttributeWriter)paramContext.get(AttributeWriter.class);
/*  80 */     if (localAttributeWriter == null)
/*  81 */       localAttributeWriter = new AttributeWriter(paramContext);
/*  82 */     return localAttributeWriter;
/*     */   }
/*     */ 
/*     */   protected AttributeWriter(Context paramContext) {
/*  86 */     super(paramContext);
/*  87 */     paramContext.put(AttributeWriter.class, this);
/*  88 */     this.annotationWriter = AnnotationWriter.instance(paramContext);
/*  89 */     this.codeWriter = CodeWriter.instance(paramContext);
/*  90 */     this.constantWriter = ConstantWriter.instance(paramContext);
/*  91 */     this.options = Options.instance(paramContext);
/*     */   }
/*     */ 
/*     */   public void write(Object paramObject, Attribute paramAttribute, ConstantPool paramConstantPool) {
/*  95 */     if (paramAttribute != null)
/*     */     {
/*  97 */       paramObject.getClass();
/*  98 */       paramConstantPool.getClass();
/*  99 */       this.constant_pool = paramConstantPool;
/* 100 */       this.owner = paramObject;
/* 101 */       paramAttribute.accept(this, null);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void write(Object paramObject, Attributes paramAttributes, ConstantPool paramConstantPool) {
/* 106 */     if (paramAttributes != null)
/*     */     {
/* 108 */       paramObject.getClass();
/* 109 */       paramConstantPool.getClass();
/* 110 */       this.constant_pool = paramConstantPool;
/* 111 */       this.owner = paramObject;
/* 112 */       for (Attribute localAttribute : paramAttributes)
/* 113 */         localAttribute.accept(this, null);
/*     */     }
/*     */   }
/*     */ 
/*     */   public Void visitDefault(DefaultAttribute paramDefaultAttribute, Void paramVoid) {
/* 118 */     if (paramDefaultAttribute.reason != null) {
/* 119 */       report(paramDefaultAttribute.reason);
/*     */     }
/* 121 */     byte[] arrayOfByte = paramDefaultAttribute.info;
/* 122 */     int i = 0;
/* 123 */     int j = 0;
/* 124 */     print("  ");
/*     */     try {
/* 126 */       print(paramDefaultAttribute.getName(this.constant_pool));
/*     */     } catch (ConstantPoolException localConstantPoolException) {
/* 128 */       report(localConstantPoolException);
/* 129 */       print("attribute name = #" + paramDefaultAttribute.attribute_name_index);
/*     */     }
/* 131 */     print(": ");
/* 132 */     println("length = 0x" + toHex(paramDefaultAttribute.info.length));
/*     */ 
/* 134 */     print("   ");
/*     */ 
/* 136 */     while (i < arrayOfByte.length) {
/* 137 */       print(toHex(arrayOfByte[i], 2));
/*     */ 
/* 139 */       j++;
/* 140 */       if (j == 16) {
/* 141 */         println();
/* 142 */         print("   ");
/* 143 */         j = 0;
/*     */       } else {
/* 145 */         print(" ");
/*     */       }
/* 147 */       i++;
/*     */     }
/* 149 */     println();
/* 150 */     return null;
/*     */   }
/*     */ 
/*     */   public Void visitAnnotationDefault(AnnotationDefault_attribute paramAnnotationDefault_attribute, Void paramVoid) {
/* 154 */     println("AnnotationDefault:");
/* 155 */     indent(1);
/* 156 */     print("default_value: ");
/* 157 */     this.annotationWriter.write(paramAnnotationDefault_attribute.default_value);
/* 158 */     indent(-1);
/* 159 */     return null;
/*     */   }
/*     */ 
/*     */   public Void visitBootstrapMethods(BootstrapMethods_attribute paramBootstrapMethods_attribute, Void paramVoid) {
/* 163 */     println("BootstrapMethods:");
/* 164 */     for (int i = 0; i < paramBootstrapMethods_attribute.bootstrap_method_specifiers.length; i++) {
/* 165 */       BootstrapMethods_attribute.BootstrapMethodSpecifier localBootstrapMethodSpecifier = paramBootstrapMethods_attribute.bootstrap_method_specifiers[i];
/* 166 */       indent(1);
/* 167 */       print(i + ": #" + localBootstrapMethodSpecifier.bootstrap_method_ref + " ");
/* 168 */       println(this.constantWriter.stringValue(localBootstrapMethodSpecifier.bootstrap_method_ref));
/* 169 */       indent(1);
/* 170 */       println("Method arguments:");
/* 171 */       indent(1);
/* 172 */       for (int j = 0; j < localBootstrapMethodSpecifier.bootstrap_arguments.length; j++) {
/* 173 */         print("#" + localBootstrapMethodSpecifier.bootstrap_arguments[j] + " ");
/* 174 */         println(this.constantWriter.stringValue(localBootstrapMethodSpecifier.bootstrap_arguments[j]));
/*     */       }
/* 176 */       indent(-3);
/*     */     }
/* 178 */     return null;
/*     */   }
/*     */ 
/*     */   public Void visitCharacterRangeTable(CharacterRangeTable_attribute paramCharacterRangeTable_attribute, Void paramVoid) {
/* 182 */     println("CharacterRangeTable:");
/* 183 */     indent(1);
/* 184 */     for (int i = 0; i < paramCharacterRangeTable_attribute.character_range_table.length; i++) {
/* 185 */       CharacterRangeTable_attribute.Entry localEntry = paramCharacterRangeTable_attribute.character_range_table[i];
/* 186 */       print(String.format("    %2d, %2d, %6x, %6x, %4x", new Object[] { 
/* 187 */         Integer.valueOf(localEntry.start_pc), 
/* 187 */         Integer.valueOf(localEntry.end_pc), 
/* 188 */         Integer.valueOf(localEntry.character_range_start), 
/* 188 */         Integer.valueOf(localEntry.character_range_end), 
/* 189 */         Integer.valueOf(localEntry.flags) }));
/*     */ 
/* 190 */       tab();
/* 191 */       print(String.format("// %2d, %2d, %4d:%02d, %4d:%02d", new Object[] { 
/* 192 */         Integer.valueOf(localEntry.start_pc), 
/* 192 */         Integer.valueOf(localEntry.end_pc), 
/* 193 */         Integer.valueOf(localEntry.character_range_start >> 10), 
/* 193 */         Integer.valueOf(localEntry.character_range_start & 0x3FF), 
/* 194 */         Integer.valueOf(localEntry.character_range_end >> 10), 
/* 194 */         Integer.valueOf(localEntry.character_range_end & 0x3FF) }));
/* 195 */       if ((localEntry.flags & 0x1) != 0)
/* 196 */         print(", statement");
/* 197 */       if ((localEntry.flags & 0x2) != 0)
/* 198 */         print(", block");
/* 199 */       if ((localEntry.flags & 0x4) != 0)
/* 200 */         print(", assignment");
/* 201 */       if ((localEntry.flags & 0x8) != 0)
/* 202 */         print(", flow-controller");
/* 203 */       if ((localEntry.flags & 0x10) != 0)
/* 204 */         print(", flow-target");
/* 205 */       if ((localEntry.flags & 0x20) != 0)
/* 206 */         print(", invoke");
/* 207 */       if ((localEntry.flags & 0x40) != 0)
/* 208 */         print(", create");
/* 209 */       if ((localEntry.flags & 0x80) != 0)
/* 210 */         print(", branch-true");
/* 211 */       if ((localEntry.flags & 0x100) != 0)
/* 212 */         print(", branch-false");
/* 213 */       println();
/*     */     }
/* 215 */     indent(-1);
/* 216 */     return null;
/*     */   }
/*     */ 
/*     */   public Void visitCode(Code_attribute paramCode_attribute, Void paramVoid) {
/* 220 */     this.codeWriter.write(paramCode_attribute, this.constant_pool);
/* 221 */     return null;
/*     */   }
/*     */ 
/*     */   public Void visitCompilationID(CompilationID_attribute paramCompilationID_attribute, Void paramVoid) {
/* 225 */     this.constantWriter.write(paramCompilationID_attribute.compilationID_index);
/* 226 */     return null;
/*     */   }
/*     */ 
/*     */   public Void visitConstantValue(ConstantValue_attribute paramConstantValue_attribute, Void paramVoid) {
/* 230 */     print("ConstantValue: ");
/* 231 */     this.constantWriter.write(paramConstantValue_attribute.constantvalue_index);
/* 232 */     println();
/* 233 */     return null;
/*     */   }
/*     */ 
/*     */   public Void visitDeprecated(Deprecated_attribute paramDeprecated_attribute, Void paramVoid) {
/* 237 */     println("Deprecated: true");
/* 238 */     return null;
/*     */   }
/*     */ 
/*     */   public Void visitEnclosingMethod(EnclosingMethod_attribute paramEnclosingMethod_attribute, Void paramVoid) {
/* 242 */     print("EnclosingMethod: #" + paramEnclosingMethod_attribute.class_index + ".#" + paramEnclosingMethod_attribute.method_index);
/* 243 */     tab();
/* 244 */     print("// " + getJavaClassName(paramEnclosingMethod_attribute));
/* 245 */     if (paramEnclosingMethod_attribute.method_index != 0)
/* 246 */       print("." + getMethodName(paramEnclosingMethod_attribute));
/* 247 */     println();
/* 248 */     return null;
/*     */   }
/*     */ 
/*     */   private String getJavaClassName(EnclosingMethod_attribute paramEnclosingMethod_attribute) {
/*     */     try {
/* 253 */       return getJavaName(paramEnclosingMethod_attribute.getClassName(this.constant_pool));
/*     */     } catch (ConstantPoolException localConstantPoolException) {
/* 255 */       return report(localConstantPoolException);
/*     */     }
/*     */   }
/*     */ 
/*     */   private String getMethodName(EnclosingMethod_attribute paramEnclosingMethod_attribute) {
/*     */     try {
/* 261 */       return paramEnclosingMethod_attribute.getMethodName(this.constant_pool);
/*     */     } catch (ConstantPoolException localConstantPoolException) {
/* 263 */       return report(localConstantPoolException);
/*     */     }
/*     */   }
/*     */ 
/*     */   public Void visitExceptions(Exceptions_attribute paramExceptions_attribute, Void paramVoid) {
/* 268 */     println("Exceptions:");
/* 269 */     indent(1);
/* 270 */     print("throws ");
/* 271 */     for (int i = 0; i < paramExceptions_attribute.number_of_exceptions; i++) {
/* 272 */       if (i > 0)
/* 273 */         print(", ");
/* 274 */       print(getJavaException(paramExceptions_attribute, i));
/*     */     }
/* 276 */     println();
/* 277 */     indent(-1);
/* 278 */     return null;
/*     */   }
/*     */ 
/*     */   private String getJavaException(Exceptions_attribute paramExceptions_attribute, int paramInt) {
/*     */     try {
/* 283 */       return getJavaName(paramExceptions_attribute.getException(paramInt, this.constant_pool));
/*     */     } catch (ConstantPoolException localConstantPoolException) {
/* 285 */       return report(localConstantPoolException);
/*     */     }
/*     */   }
/*     */ 
/*     */   public Void visitInnerClasses(InnerClasses_attribute paramInnerClasses_attribute, Void paramVoid) {
/* 290 */     int i = 1;
/* 291 */     for (int j = 0; j < paramInnerClasses_attribute.classes.length; j++) {
/* 292 */       InnerClasses_attribute.Info localInfo = paramInnerClasses_attribute.classes[j];
/*     */ 
/* 294 */       AccessFlags localAccessFlags = localInfo.inner_class_access_flags;
/* 295 */       if (this.options.checkAccess(localAccessFlags)) {
/* 296 */         if (i != 0) {
/* 297 */           writeInnerClassHeader();
/* 298 */           i = 0;
/*     */         }
/* 300 */         print("   ");
/* 301 */         for (String str : localAccessFlags.getInnerClassModifiers())
/* 302 */           print(str + " ");
/* 303 */         if (localInfo.inner_name_index != 0) {
/* 304 */           print("#" + localInfo.inner_name_index + "= ");
/*     */         }
/* 306 */         print("#" + localInfo.inner_class_info_index);
/* 307 */         if (localInfo.outer_class_info_index != 0) {
/* 308 */           print(" of #" + localInfo.outer_class_info_index);
/*     */         }
/* 310 */         print("; //");
/* 311 */         if (localInfo.inner_name_index != 0) {
/* 312 */           print(getInnerName(this.constant_pool, localInfo) + "=");
/*     */         }
/* 314 */         this.constantWriter.write(localInfo.inner_class_info_index);
/* 315 */         if (localInfo.outer_class_info_index != 0) {
/* 316 */           print(" of ");
/* 317 */           this.constantWriter.write(localInfo.outer_class_info_index);
/*     */         }
/* 319 */         println();
/*     */       }
/*     */     }
/* 322 */     if (i == 0)
/* 323 */       indent(-1);
/* 324 */     return null;
/*     */   }
/*     */ 
/*     */   String getInnerName(ConstantPool paramConstantPool, InnerClasses_attribute.Info paramInfo) {
/*     */     try {
/* 329 */       return paramInfo.getInnerName(paramConstantPool);
/*     */     } catch (ConstantPoolException localConstantPoolException) {
/* 331 */       return report(localConstantPoolException);
/*     */     }
/*     */   }
/*     */ 
/*     */   private void writeInnerClassHeader() {
/* 336 */     println("InnerClasses:");
/* 337 */     indent(1);
/*     */   }
/*     */ 
/*     */   public Void visitLineNumberTable(LineNumberTable_attribute paramLineNumberTable_attribute, Void paramVoid) {
/* 341 */     println("LineNumberTable:");
/* 342 */     indent(1);
/* 343 */     for (LineNumberTable_attribute.Entry localEntry : paramLineNumberTable_attribute.line_number_table) {
/* 344 */       println("line " + localEntry.line_number + ": " + localEntry.start_pc);
/*     */     }
/* 346 */     indent(-1);
/* 347 */     return null;
/*     */   }
/*     */ 
/*     */   public Void visitLocalVariableTable(LocalVariableTable_attribute paramLocalVariableTable_attribute, Void paramVoid) {
/* 351 */     println("LocalVariableTable:");
/* 352 */     indent(1);
/* 353 */     println("Start  Length  Slot  Name   Signature");
/* 354 */     for (LocalVariableTable_attribute.Entry localEntry : paramLocalVariableTable_attribute.local_variable_table) {
/* 355 */       println(String.format("%5d %7d %5d %5s   %s", new Object[] { 
/* 356 */         Integer.valueOf(localEntry.start_pc), 
/* 356 */         Integer.valueOf(localEntry.length), Integer.valueOf(localEntry.index), this.constantWriter
/* 357 */         .stringValue(localEntry.name_index), 
/* 357 */         this.constantWriter
/* 358 */         .stringValue(localEntry.descriptor_index) }));
/*     */     }
/*     */ 
/* 360 */     indent(-1);
/* 361 */     return null;
/*     */   }
/*     */ 
/*     */   public Void visitLocalVariableTypeTable(LocalVariableTypeTable_attribute paramLocalVariableTypeTable_attribute, Void paramVoid) {
/* 365 */     println("LocalVariableTypeTable:");
/* 366 */     indent(1);
/* 367 */     println("Start  Length  Slot  Name   Signature");
/* 368 */     for (LocalVariableTypeTable_attribute.Entry localEntry : paramLocalVariableTypeTable_attribute.local_variable_table) {
/* 369 */       println(String.format("%5d %7d %5d %5s   %s", new Object[] { 
/* 370 */         Integer.valueOf(localEntry.start_pc), 
/* 370 */         Integer.valueOf(localEntry.length), Integer.valueOf(localEntry.index), this.constantWriter
/* 371 */         .stringValue(localEntry.name_index), 
/* 371 */         this.constantWriter
/* 372 */         .stringValue(localEntry.signature_index) }));
/*     */     }
/*     */ 
/* 374 */     indent(-1);
/* 375 */     return null;
/*     */   }
/*     */ 
/*     */   public Void visitMethodParameters(MethodParameters_attribute paramMethodParameters_attribute, Void paramVoid)
/*     */   {
/* 383 */     String str1 = String.format("%-31s%s", new Object[] { "Name", "Flags" });
/* 384 */     println("MethodParameters:");
/* 385 */     indent(1);
/* 386 */     println(str1);
/*     */ 
/* 388 */     for (MethodParameters_attribute.Entry localEntry : paramMethodParameters_attribute.method_parameter_table)
/*     */     {
/* 391 */       String str2 = localEntry.name_index != 0 ? this.constantWriter
/* 391 */         .stringValue(localEntry.name_index) : 
/* 391 */         "<no name>";
/* 392 */       String str3 = (0 != (localEntry.flags & 0x10) ? "final " : "") + (0 != (localEntry.flags & 0x8000) ? "mandated " : "") + (0 != (localEntry.flags & 0x1000) ? "synthetic" : "");
/*     */ 
/* 396 */       println(String.format("%-31s%s", new Object[] { str2, str3 }));
/*     */     }
/* 398 */     indent(-1);
/* 399 */     return null;
/*     */   }
/*     */ 
/*     */   public Void visitRuntimeVisibleAnnotations(RuntimeVisibleAnnotations_attribute paramRuntimeVisibleAnnotations_attribute, Void paramVoid) {
/* 403 */     println("RuntimeVisibleAnnotations:");
/* 404 */     indent(1);
/* 405 */     for (int i = 0; i < paramRuntimeVisibleAnnotations_attribute.annotations.length; i++) {
/* 406 */       print(i + ": ");
/* 407 */       this.annotationWriter.write(paramRuntimeVisibleAnnotations_attribute.annotations[i]);
/* 408 */       println();
/*     */     }
/* 410 */     indent(-1);
/* 411 */     return null;
/*     */   }
/*     */ 
/*     */   public Void visitRuntimeInvisibleAnnotations(RuntimeInvisibleAnnotations_attribute paramRuntimeInvisibleAnnotations_attribute, Void paramVoid) {
/* 415 */     println("RuntimeInvisibleAnnotations:");
/* 416 */     indent(1);
/* 417 */     for (int i = 0; i < paramRuntimeInvisibleAnnotations_attribute.annotations.length; i++) {
/* 418 */       print(i + ": ");
/* 419 */       this.annotationWriter.write(paramRuntimeInvisibleAnnotations_attribute.annotations[i]);
/* 420 */       println();
/*     */     }
/* 422 */     indent(-1);
/* 423 */     return null;
/*     */   }
/*     */ 
/*     */   public Void visitRuntimeVisibleTypeAnnotations(RuntimeVisibleTypeAnnotations_attribute paramRuntimeVisibleTypeAnnotations_attribute, Void paramVoid) {
/* 427 */     println("RuntimeVisibleTypeAnnotations:");
/* 428 */     indent(1);
/* 429 */     for (int i = 0; i < paramRuntimeVisibleTypeAnnotations_attribute.annotations.length; i++) {
/* 430 */       print(i + ": ");
/* 431 */       this.annotationWriter.write(paramRuntimeVisibleTypeAnnotations_attribute.annotations[i]);
/* 432 */       println();
/*     */     }
/* 434 */     indent(-1);
/* 435 */     return null;
/*     */   }
/*     */ 
/*     */   public Void visitRuntimeInvisibleTypeAnnotations(RuntimeInvisibleTypeAnnotations_attribute paramRuntimeInvisibleTypeAnnotations_attribute, Void paramVoid) {
/* 439 */     println("RuntimeInvisibleTypeAnnotations:");
/* 440 */     indent(1);
/* 441 */     for (int i = 0; i < paramRuntimeInvisibleTypeAnnotations_attribute.annotations.length; i++) {
/* 442 */       print(i + ": ");
/* 443 */       this.annotationWriter.write(paramRuntimeInvisibleTypeAnnotations_attribute.annotations[i]);
/* 444 */       println();
/*     */     }
/* 446 */     indent(-1);
/* 447 */     return null;
/*     */   }
/*     */ 
/*     */   public Void visitRuntimeVisibleParameterAnnotations(RuntimeVisibleParameterAnnotations_attribute paramRuntimeVisibleParameterAnnotations_attribute, Void paramVoid) {
/* 451 */     println("RuntimeVisibleParameterAnnotations:");
/* 452 */     indent(1);
/* 453 */     for (int i = 0; i < paramRuntimeVisibleParameterAnnotations_attribute.parameter_annotations.length; i++) {
/* 454 */       println("parameter " + i + ": ");
/* 455 */       indent(1);
/* 456 */       for (int j = 0; j < paramRuntimeVisibleParameterAnnotations_attribute.parameter_annotations[i].length; j++) {
/* 457 */         print(j + ": ");
/* 458 */         this.annotationWriter.write(paramRuntimeVisibleParameterAnnotations_attribute.parameter_annotations[i][j]);
/* 459 */         println();
/*     */       }
/* 461 */       indent(-1);
/*     */     }
/* 463 */     indent(-1);
/* 464 */     return null;
/*     */   }
/*     */ 
/*     */   public Void visitRuntimeInvisibleParameterAnnotations(RuntimeInvisibleParameterAnnotations_attribute paramRuntimeInvisibleParameterAnnotations_attribute, Void paramVoid) {
/* 468 */     println("RuntimeInvisibleParameterAnnotations:");
/* 469 */     indent(1);
/* 470 */     for (int i = 0; i < paramRuntimeInvisibleParameterAnnotations_attribute.parameter_annotations.length; i++) {
/* 471 */       println(i + ": ");
/* 472 */       indent(1);
/* 473 */       for (int j = 0; j < paramRuntimeInvisibleParameterAnnotations_attribute.parameter_annotations[i].length; j++) {
/* 474 */         print(j + ": ");
/* 475 */         this.annotationWriter.write(paramRuntimeInvisibleParameterAnnotations_attribute.parameter_annotations[i][j]);
/* 476 */         println();
/*     */       }
/* 478 */       indent(-1);
/*     */     }
/* 480 */     indent(-1);
/* 481 */     return null;
/*     */   }
/*     */ 
/*     */   public Void visitSignature(Signature_attribute paramSignature_attribute, Void paramVoid) {
/* 485 */     print("Signature: #" + paramSignature_attribute.signature_index);
/* 486 */     tab();
/* 487 */     println("// " + getSignature(paramSignature_attribute));
/* 488 */     return null;
/*     */   }
/*     */ 
/*     */   String getSignature(Signature_attribute paramSignature_attribute) {
/*     */     try {
/* 493 */       return paramSignature_attribute.getSignature(this.constant_pool);
/*     */     } catch (ConstantPoolException localConstantPoolException) {
/* 495 */       return report(localConstantPoolException);
/*     */     }
/*     */   }
/*     */ 
/*     */   public Void visitSourceDebugExtension(SourceDebugExtension_attribute paramSourceDebugExtension_attribute, Void paramVoid) {
/* 500 */     println("SourceDebugExtension:");
/* 501 */     indent(1);
/* 502 */     for (String str : paramSourceDebugExtension_attribute.getValue().split("[\r\n]+")) {
/* 503 */       println(str);
/*     */     }
/* 505 */     indent(-1);
/* 506 */     return null;
/*     */   }
/*     */ 
/*     */   public Void visitSourceFile(SourceFile_attribute paramSourceFile_attribute, Void paramVoid) {
/* 510 */     println("SourceFile: \"" + getSourceFile(paramSourceFile_attribute) + "\"");
/* 511 */     return null;
/*     */   }
/*     */ 
/*     */   private String getSourceFile(SourceFile_attribute paramSourceFile_attribute) {
/*     */     try {
/* 516 */       return paramSourceFile_attribute.getSourceFile(this.constant_pool);
/*     */     } catch (ConstantPoolException localConstantPoolException) {
/* 518 */       return report(localConstantPoolException);
/*     */     }
/*     */   }
/*     */ 
/*     */   public Void visitSourceID(SourceID_attribute paramSourceID_attribute, Void paramVoid) {
/* 523 */     this.constantWriter.write(paramSourceID_attribute.sourceID_index);
/* 524 */     return null;
/*     */   }
/*     */ 
/*     */   public Void visitStackMap(StackMap_attribute paramStackMap_attribute, Void paramVoid) {
/* 528 */     println("StackMap: number_of_entries = " + paramStackMap_attribute.number_of_entries);
/* 529 */     indent(1);
/* 530 */     StackMapTableWriter localStackMapTableWriter = new StackMapTableWriter();
/* 531 */     for (StackMap_attribute.stack_map_frame localstack_map_frame : paramStackMap_attribute.entries) {
/* 532 */       localStackMapTableWriter.write(localstack_map_frame);
/*     */     }
/* 534 */     indent(-1);
/* 535 */     return null;
/*     */   }
/*     */ 
/*     */   public Void visitStackMapTable(StackMapTable_attribute paramStackMapTable_attribute, Void paramVoid) {
/* 539 */     println("StackMapTable: number_of_entries = " + paramStackMapTable_attribute.number_of_entries);
/* 540 */     indent(1);
/* 541 */     StackMapTableWriter localStackMapTableWriter = new StackMapTableWriter();
/* 542 */     for (StackMapTable_attribute.stack_map_frame localstack_map_frame : paramStackMapTable_attribute.entries) {
/* 543 */       localStackMapTableWriter.write(localstack_map_frame);
/*     */     }
/* 545 */     indent(-1);
/* 546 */     return null;
/*     */   }
/*     */ 
/*     */   public Void visitSynthetic(Synthetic_attribute paramSynthetic_attribute, Void paramVoid)
/*     */   {
/* 681 */     println("Synthetic: true");
/* 682 */     return null;
/*     */   }
/*     */ 
/*     */   static String getJavaName(String paramString) {
/* 686 */     return paramString.replace('/', '.');
/*     */   }
/*     */ 
/*     */   String toHex(byte paramByte, int paramInt) {
/* 690 */     return toHex(paramByte & 0xFF, paramInt);
/*     */   }
/*     */ 
/*     */   static String toHex(int paramInt) {
/* 694 */     return StringUtils.toUpperCase(Integer.toString(paramInt, 16));
/*     */   }
/*     */ 
/*     */   static String toHex(int paramInt1, int paramInt2) {
/* 698 */     String str = StringUtils.toUpperCase(Integer.toHexString(paramInt1));
/* 699 */     while (str.length() < paramInt2)
/* 700 */       str = "0" + str;
/* 701 */     return StringUtils.toUpperCase(str);
/*     */   }
/*     */ 
/*     */   class StackMapTableWriter
/*     */     implements StackMapTable_attribute.stack_map_frame.Visitor<Void, Void>
/*     */   {
/*     */     StackMapTableWriter()
/*     */     {
/*     */     }
/*     */ 
/*     */     public void write(StackMapTable_attribute.stack_map_frame paramstack_map_frame)
/*     */     {
/* 552 */       paramstack_map_frame.accept(this, null);
/*     */     }
/*     */ 
/*     */     public Void visit_same_frame(StackMapTable_attribute.same_frame paramsame_frame, Void paramVoid) {
/* 556 */       printHeader(paramsame_frame, "/* same */");
/* 557 */       return null;
/*     */     }
/*     */ 
/*     */     public Void visit_same_locals_1_stack_item_frame(StackMapTable_attribute.same_locals_1_stack_item_frame paramsame_locals_1_stack_item_frame, Void paramVoid) {
/* 561 */       printHeader(paramsame_locals_1_stack_item_frame, "/* same_locals_1_stack_item */");
/* 562 */       AttributeWriter.this.indent(1);
/* 563 */       printMap("stack", paramsame_locals_1_stack_item_frame.stack);
/* 564 */       AttributeWriter.this.indent(-1);
/* 565 */       return null;
/*     */     }
/*     */ 
/*     */     public Void visit_same_locals_1_stack_item_frame_extended(StackMapTable_attribute.same_locals_1_stack_item_frame_extended paramsame_locals_1_stack_item_frame_extended, Void paramVoid) {
/* 569 */       printHeader(paramsame_locals_1_stack_item_frame_extended, "/* same_locals_1_stack_item_frame_extended */");
/* 570 */       AttributeWriter.this.indent(1);
/* 571 */       AttributeWriter.this.println("offset_delta = " + paramsame_locals_1_stack_item_frame_extended.offset_delta);
/* 572 */       printMap("stack", paramsame_locals_1_stack_item_frame_extended.stack);
/* 573 */       AttributeWriter.this.indent(-1);
/* 574 */       return null;
/*     */     }
/*     */ 
/*     */     public Void visit_chop_frame(StackMapTable_attribute.chop_frame paramchop_frame, Void paramVoid) {
/* 578 */       printHeader(paramchop_frame, "/* chop */");
/* 579 */       AttributeWriter.this.indent(1);
/* 580 */       AttributeWriter.this.println("offset_delta = " + paramchop_frame.offset_delta);
/* 581 */       AttributeWriter.this.indent(-1);
/* 582 */       return null;
/*     */     }
/*     */ 
/*     */     public Void visit_same_frame_extended(StackMapTable_attribute.same_frame_extended paramsame_frame_extended, Void paramVoid) {
/* 586 */       printHeader(paramsame_frame_extended, "/* same_frame_extended */");
/* 587 */       AttributeWriter.this.indent(1);
/* 588 */       AttributeWriter.this.println("offset_delta = " + paramsame_frame_extended.offset_delta);
/* 589 */       AttributeWriter.this.indent(-1);
/* 590 */       return null;
/*     */     }
/*     */ 
/*     */     public Void visit_append_frame(StackMapTable_attribute.append_frame paramappend_frame, Void paramVoid) {
/* 594 */       printHeader(paramappend_frame, "/* append */");
/* 595 */       AttributeWriter.this.indent(1);
/* 596 */       AttributeWriter.this.println("offset_delta = " + paramappend_frame.offset_delta);
/* 597 */       printMap("locals", paramappend_frame.locals);
/* 598 */       AttributeWriter.this.indent(-1);
/* 599 */       return null;
/*     */     }
/*     */ 
/*     */     public Void visit_full_frame(StackMapTable_attribute.full_frame paramfull_frame, Void paramVoid) {
/* 603 */       if ((paramfull_frame instanceof StackMap_attribute.stack_map_frame)) {
/* 604 */         printHeader(paramfull_frame, "offset = " + paramfull_frame.offset_delta);
/* 605 */         AttributeWriter.this.indent(1);
/*     */       } else {
/* 607 */         printHeader(paramfull_frame, "/* full_frame */");
/* 608 */         AttributeWriter.this.indent(1);
/* 609 */         AttributeWriter.this.println("offset_delta = " + paramfull_frame.offset_delta);
/*     */       }
/* 611 */       printMap("locals", paramfull_frame.locals);
/* 612 */       printMap("stack", paramfull_frame.stack);
/* 613 */       AttributeWriter.this.indent(-1);
/* 614 */       return null;
/*     */     }
/*     */ 
/*     */     void printHeader(StackMapTable_attribute.stack_map_frame paramstack_map_frame, String paramString) {
/* 618 */       AttributeWriter.this.print("frame_type = " + paramstack_map_frame.frame_type + " ");
/* 619 */       AttributeWriter.this.println(paramString);
/*     */     }
/*     */ 
/*     */     void printMap(String paramString, StackMapTable_attribute.verification_type_info[] paramArrayOfverification_type_info) {
/* 623 */       AttributeWriter.this.print(paramString + " = [");
/* 624 */       for (int i = 0; i < paramArrayOfverification_type_info.length; i++) {
/* 625 */         StackMapTable_attribute.verification_type_info localverification_type_info = paramArrayOfverification_type_info[i];
/* 626 */         int j = localverification_type_info.tag;
/* 627 */         switch (j) {
/*     */         case 7:
/* 629 */           AttributeWriter.this.print(" ");
/* 630 */           AttributeWriter.this.constantWriter.write(((StackMapTable_attribute.Object_variable_info)localverification_type_info).cpool_index);
/* 631 */           break;
/*     */         case 8:
/* 633 */           AttributeWriter.this.print(" " + mapTypeName(j));
/* 634 */           AttributeWriter.this.print(" " + ((StackMapTable_attribute.Uninitialized_variable_info)localverification_type_info).offset);
/* 635 */           break;
/*     */         default:
/* 637 */           AttributeWriter.this.print(" " + mapTypeName(j));
/*     */         }
/* 639 */         AttributeWriter.this.print(i == paramArrayOfverification_type_info.length - 1 ? " " : ",");
/*     */       }
/* 641 */       AttributeWriter.this.println("]");
/*     */     }
/*     */ 
/*     */     String mapTypeName(int paramInt) {
/* 645 */       switch (paramInt) {
/*     */       case 0:
/* 647 */         return "top";
/*     */       case 1:
/* 650 */         return "int";
/*     */       case 2:
/* 653 */         return "float";
/*     */       case 4:
/* 656 */         return "long";
/*     */       case 3:
/* 659 */         return "double";
/*     */       case 5:
/* 662 */         return "null";
/*     */       case 6:
/* 665 */         return "this";
/*     */       case 7:
/* 668 */         return "CP";
/*     */       case 8:
/* 671 */         return "uninitialized";
/*     */       }
/*     */ 
/* 674 */       AttributeWriter.this.report("unrecognized verification_type_info tag: " + paramInt);
/* 675 */       return "[tag:" + paramInt + "]";
/*     */     }
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.javap.AttributeWriter
 * JD-Core Version:    0.6.2
 */