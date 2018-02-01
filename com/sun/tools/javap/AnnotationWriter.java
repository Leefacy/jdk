/*     */ package com.sun.tools.javap;
/*     */ 
/*     */ import com.sun.tools.classfile.Annotation;
/*     */ import com.sun.tools.classfile.Annotation.Annotation_element_value;
/*     */ import com.sun.tools.classfile.Annotation.Array_element_value;
/*     */ import com.sun.tools.classfile.Annotation.Class_element_value;
/*     */ import com.sun.tools.classfile.Annotation.Enum_element_value;
/*     */ import com.sun.tools.classfile.Annotation.Primitive_element_value;
/*     */ import com.sun.tools.classfile.Annotation.element_value;
/*     */ import com.sun.tools.classfile.Annotation.element_value.Visitor;
/*     */ import com.sun.tools.classfile.Annotation.element_value_pair;
/*     */ import com.sun.tools.classfile.ClassFile;
/*     */ import com.sun.tools.classfile.ConstantPool;
/*     */ import com.sun.tools.classfile.ConstantPoolException;
/*     */ import com.sun.tools.classfile.Descriptor;
/*     */ import com.sun.tools.classfile.Descriptor.InvalidDescriptor;
/*     */ import com.sun.tools.classfile.TypeAnnotation;
/*     */ import com.sun.tools.classfile.TypeAnnotation.Position;
/*     */ import java.util.List;
/*     */ 
/*     */ public class AnnotationWriter extends BasicWriter
/*     */ {
/* 237 */   element_value_Writer ev_writer = new element_value_Writer();
/*     */   private ClassWriter classWriter;
/*     */   private ConstantWriter constantWriter;
/*     */ 
/*     */   static AnnotationWriter instance(Context paramContext)
/*     */   {
/*  50 */     AnnotationWriter localAnnotationWriter = (AnnotationWriter)paramContext.get(AnnotationWriter.class);
/*  51 */     if (localAnnotationWriter == null)
/*  52 */       localAnnotationWriter = new AnnotationWriter(paramContext);
/*  53 */     return localAnnotationWriter;
/*     */   }
/*     */ 
/*     */   protected AnnotationWriter(Context paramContext) {
/*  57 */     super(paramContext);
/*  58 */     this.classWriter = ClassWriter.instance(paramContext);
/*  59 */     this.constantWriter = ConstantWriter.instance(paramContext);
/*     */   }
/*     */ 
/*     */   public void write(Annotation paramAnnotation) {
/*  63 */     write(paramAnnotation, false);
/*     */   }
/*     */ 
/*     */   public void write(Annotation paramAnnotation, boolean paramBoolean) {
/*  67 */     writeDescriptor(paramAnnotation.type_index, paramBoolean);
/*  68 */     int i = (paramAnnotation.num_element_value_pairs > 0) || (!paramBoolean) ? 1 : 0;
/*  69 */     if (i != 0)
/*  70 */       print("(");
/*  71 */     for (int j = 0; j < paramAnnotation.num_element_value_pairs; j++) {
/*  72 */       if (j > 0)
/*  73 */         print(",");
/*  74 */       write(paramAnnotation.element_value_pairs[j], paramBoolean);
/*     */     }
/*  76 */     if (i != 0)
/*  77 */       print(")");
/*     */   }
/*     */ 
/*     */   public void write(TypeAnnotation paramTypeAnnotation) {
/*  81 */     write(paramTypeAnnotation, true, false);
/*     */   }
/*     */ 
/*     */   public void write(TypeAnnotation paramTypeAnnotation, boolean paramBoolean1, boolean paramBoolean2) {
/*  85 */     write(paramTypeAnnotation.annotation, paramBoolean2);
/*  86 */     print(": ");
/*  87 */     write(paramTypeAnnotation.position, paramBoolean1);
/*     */   }
/*     */ 
/*     */   public void write(TypeAnnotation.Position paramPosition, boolean paramBoolean) {
/*  91 */     print(paramPosition.type);
/*     */ 
/*  93 */     switch (1.$SwitchMap$com$sun$tools$classfile$TypeAnnotation$TargetType[paramPosition.type.ordinal()])
/*     */     {
/*     */     case 1:
/*     */     case 2:
/*     */     case 3:
/*     */     case 4:
/* 101 */       if (paramBoolean) {
/* 102 */         print(", offset=");
/* 103 */         print(Integer.valueOf(paramPosition.offset)); } break;
/*     */     case 5:
/*     */     case 6:
/* 110 */       if (paramPosition.lvarOffset == null) {
/* 111 */         print(", lvarOffset is Null!");
/*     */       }
/*     */       else {
/* 114 */         print(", {");
/* 115 */         for (int i = 0; i < paramPosition.lvarOffset.length; i++) {
/* 116 */           if (i != 0) print("; ");
/* 117 */           if (paramBoolean) {
/* 118 */             print("start_pc=");
/* 119 */             print(Integer.valueOf(paramPosition.lvarOffset[i]));
/*     */           }
/* 121 */           print(", length=");
/* 122 */           print(Integer.valueOf(paramPosition.lvarLength[i]));
/* 123 */           print(", index=");
/* 124 */           print(Integer.valueOf(paramPosition.lvarIndex[i]));
/*     */         }
/* 126 */         print("}");
/* 127 */       }break;
/*     */     case 7:
/* 130 */       print(", exception_index=");
/* 131 */       print(Integer.valueOf(paramPosition.exception_index));
/* 132 */       break;
/*     */     case 8:
/* 136 */       break;
/*     */     case 9:
/*     */     case 10:
/* 140 */       print(", param_index=");
/* 141 */       print(Integer.valueOf(paramPosition.parameter_index));
/* 142 */       break;
/*     */     case 11:
/*     */     case 12:
/* 146 */       print(", param_index=");
/* 147 */       print(Integer.valueOf(paramPosition.parameter_index));
/* 148 */       print(", bound_index=");
/* 149 */       print(Integer.valueOf(paramPosition.bound_index));
/* 150 */       break;
/*     */     case 13:
/* 153 */       print(", type_index=");
/* 154 */       print(Integer.valueOf(paramPosition.type_index));
/* 155 */       break;
/*     */     case 14:
/* 158 */       print(", type_index=");
/* 159 */       print(Integer.valueOf(paramPosition.type_index));
/* 160 */       break;
/*     */     case 15:
/* 163 */       print(", param_index=");
/* 164 */       print(Integer.valueOf(paramPosition.parameter_index));
/* 165 */       break;
/*     */     case 16:
/*     */     case 17:
/*     */     case 18:
/*     */     case 19:
/*     */     case 20:
/* 173 */       if (paramBoolean) {
/* 174 */         print(", offset=");
/* 175 */         print(Integer.valueOf(paramPosition.offset));
/*     */       }
/* 177 */       print(", type_index=");
/* 178 */       print(Integer.valueOf(paramPosition.type_index));
/* 179 */       break;
/*     */     case 21:
/*     */     case 22:
/* 183 */       break;
/*     */     case 23:
/* 185 */       throw new AssertionError("AnnotationWriter: UNKNOWN target type should never occur!");
/*     */     default:
/* 187 */       throw new AssertionError("AnnotationWriter: Unknown target type for position: " + paramPosition);
/*     */     }
/*     */ 
/* 191 */     if (!paramPosition.location.isEmpty()) {
/* 192 */       print(", location=");
/* 193 */       print(paramPosition.location);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void write(Annotation.element_value_pair paramelement_value_pair) {
/* 198 */     write(paramelement_value_pair, false);
/*     */   }
/*     */ 
/*     */   public void write(Annotation.element_value_pair paramelement_value_pair, boolean paramBoolean) {
/* 202 */     writeIndex(paramelement_value_pair.element_name_index, paramBoolean);
/* 203 */     print("=");
/* 204 */     write(paramelement_value_pair.value, paramBoolean);
/*     */   }
/*     */ 
/*     */   public void write(Annotation.element_value paramelement_value) {
/* 208 */     write(paramelement_value, false);
/*     */   }
/*     */ 
/*     */   public void write(Annotation.element_value paramelement_value, boolean paramBoolean) {
/* 212 */     this.ev_writer.write(paramelement_value, paramBoolean);
/*     */   }
/*     */ 
/*     */   private void writeDescriptor(int paramInt, boolean paramBoolean) {
/* 216 */     if (paramBoolean)
/*     */       try {
/* 218 */         ConstantPool localConstantPool = this.classWriter.getClassFile().constant_pool;
/* 219 */         Descriptor localDescriptor = new Descriptor(paramInt);
/* 220 */         print(localDescriptor.getFieldType(localConstantPool));
/* 221 */         return;
/*     */       }
/*     */       catch (ConstantPoolException localConstantPoolException) {
/*     */       }
/*     */       catch (Descriptor.InvalidDescriptor localInvalidDescriptor) {
/*     */       }
/* 227 */     print("#" + paramInt);
/*     */   }
/*     */ 
/*     */   private void writeIndex(int paramInt, boolean paramBoolean) {
/* 231 */     if (paramBoolean)
/* 232 */       print(this.constantWriter.stringValue(paramInt));
/*     */     else
/* 234 */       print("#" + paramInt); 
/*     */   }
/*     */ 
/*     */   class element_value_Writer implements Annotation.element_value.Visitor<Void, Boolean> {
/*     */     element_value_Writer() {
/*     */     }
/*     */ 
/* 241 */     public void write(Annotation.element_value paramelement_value, boolean paramBoolean) { paramelement_value.accept(this, Boolean.valueOf(paramBoolean)); }
/*     */ 
/*     */     public Void visitPrimitive(Annotation.Primitive_element_value paramPrimitive_element_value, Boolean paramBoolean)
/*     */     {
/* 245 */       if (paramBoolean.booleanValue())
/* 246 */         AnnotationWriter.this.writeIndex(paramPrimitive_element_value.const_value_index, paramBoolean.booleanValue());
/*     */       else
/* 248 */         AnnotationWriter.this.print((char)paramPrimitive_element_value.tag + "#" + paramPrimitive_element_value.const_value_index);
/* 249 */       return null;
/*     */     }
/*     */ 
/*     */     public Void visitEnum(Annotation.Enum_element_value paramEnum_element_value, Boolean paramBoolean) {
/* 253 */       if (paramBoolean.booleanValue()) {
/* 254 */         AnnotationWriter.this.writeIndex(paramEnum_element_value.type_name_index, paramBoolean.booleanValue());
/* 255 */         AnnotationWriter.this.print(".");
/* 256 */         AnnotationWriter.this.writeIndex(paramEnum_element_value.const_name_index, paramBoolean.booleanValue());
/*     */       } else {
/* 258 */         AnnotationWriter.this.print((char)paramEnum_element_value.tag + "#" + paramEnum_element_value.type_name_index + ".#" + paramEnum_element_value.const_name_index);
/* 259 */       }return null;
/*     */     }
/*     */ 
/*     */     public Void visitClass(Annotation.Class_element_value paramClass_element_value, Boolean paramBoolean) {
/* 263 */       if (paramBoolean.booleanValue()) {
/* 264 */         AnnotationWriter.this.writeIndex(paramClass_element_value.class_info_index, paramBoolean.booleanValue());
/* 265 */         AnnotationWriter.this.print(".class");
/*     */       } else {
/* 267 */         AnnotationWriter.this.print((char)paramClass_element_value.tag + "#" + paramClass_element_value.class_info_index);
/* 268 */       }return null;
/*     */     }
/*     */ 
/*     */     public Void visitAnnotation(Annotation.Annotation_element_value paramAnnotation_element_value, Boolean paramBoolean) {
/* 272 */       AnnotationWriter.this.print(Character.valueOf((char)paramAnnotation_element_value.tag));
/* 273 */       AnnotationWriter.this.write(paramAnnotation_element_value.annotation_value, paramBoolean.booleanValue());
/* 274 */       return null;
/*     */     }
/*     */ 
/*     */     public Void visitArray(Annotation.Array_element_value paramArray_element_value, Boolean paramBoolean) {
/* 278 */       AnnotationWriter.this.print("[");
/* 279 */       for (int i = 0; i < paramArray_element_value.num_values; i++) {
/* 280 */         if (i > 0)
/* 281 */           AnnotationWriter.this.print(",");
/* 282 */         write(paramArray_element_value.values[i], paramBoolean.booleanValue());
/*     */       }
/* 284 */       AnnotationWriter.this.print("]");
/* 285 */       return null;
/*     */     }
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.javap.AnnotationWriter
 * JD-Core Version:    0.6.2
 */