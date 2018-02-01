/*     */ package com.sun.tools.classfile;
/*     */ 
/*     */ import java.io.IOException;
/*     */ 
/*     */ public class Annotation
/*     */ {
/*     */   public final int type_index;
/*     */   public final int num_element_value_pairs;
/*     */   public final element_value_pair[] element_value_pairs;
/*     */ 
/*     */   Annotation(ClassReader paramClassReader)
/*     */     throws IOException, Annotation.InvalidAnnotation
/*     */   {
/*  47 */     this.type_index = paramClassReader.readUnsignedShort();
/*  48 */     this.num_element_value_pairs = paramClassReader.readUnsignedShort();
/*  49 */     this.element_value_pairs = new element_value_pair[this.num_element_value_pairs];
/*  50 */     for (int i = 0; i < this.element_value_pairs.length; i++)
/*  51 */       this.element_value_pairs[i] = new element_value_pair(paramClassReader);
/*     */   }
/*     */ 
/*     */   public Annotation(ConstantPool paramConstantPool, int paramInt, element_value_pair[] paramArrayOfelement_value_pair)
/*     */   {
/*  57 */     this.type_index = paramInt;
/*  58 */     this.num_element_value_pairs = paramArrayOfelement_value_pair.length;
/*  59 */     this.element_value_pairs = paramArrayOfelement_value_pair;
/*     */   }
/*     */ 
/*     */   public int length() {
/*  63 */     int i = 4;
/*  64 */     for (element_value_pair localelement_value_pair : this.element_value_pairs)
/*  65 */       i += localelement_value_pair.length();
/*  66 */     return i;
/*     */   }
/*     */ 
/*     */   public static class Annotation_element_value extends Annotation.element_value
/*     */   {
/*     */     public final Annotation annotation_value;
/*     */ 
/*     */     Annotation_element_value(ClassReader paramClassReader, int paramInt)
/*     */       throws IOException, Annotation.InvalidAnnotation
/*     */     {
/* 188 */       super();
/* 189 */       this.annotation_value = new Annotation(paramClassReader);
/*     */     }
/*     */ 
/*     */     public int length()
/*     */     {
/* 194 */       return this.annotation_value.length();
/*     */     }
/*     */ 
/*     */     public <R, P> R accept(Annotation.element_value.Visitor<R, P> paramVisitor, P paramP) {
/* 198 */       return paramVisitor.visitAnnotation(this, paramP);
/*     */     }
/*     */   }
/*     */ 
/*     */   public static class Array_element_value extends Annotation.element_value {
/*     */     public final int num_values;
/*     */     public final Annotation.element_value[] values;
/*     */ 
/* 207 */     Array_element_value(ClassReader paramClassReader, int paramInt) throws IOException, Annotation.InvalidAnnotation { super();
/* 208 */       this.num_values = paramClassReader.readUnsignedShort();
/* 209 */       this.values = new Annotation.element_value[this.num_values];
/* 210 */       for (int i = 0; i < this.values.length; i++)
/* 211 */         this.values[i] = Annotation.element_value.read(paramClassReader);
/*     */     }
/*     */ 
/*     */     public int length()
/*     */     {
/* 216 */       int i = 2;
/* 217 */       for (int j = 0; j < this.values.length; j++)
/* 218 */         i += this.values[j].length();
/* 219 */       return i;
/*     */     }
/*     */ 
/*     */     public <R, P> R accept(Annotation.element_value.Visitor<R, P> paramVisitor, P paramP) {
/* 223 */       return paramVisitor.visitArray(this, paramP);
/*     */     }
/*     */   }
/*     */ 
/*     */   public static class Class_element_value extends Annotation.element_value
/*     */   {
/*     */     public final int class_info_index;
/*     */ 
/*     */     Class_element_value(ClassReader paramClassReader, int paramInt)
/*     */       throws IOException
/*     */     {
/* 169 */       super();
/* 170 */       this.class_info_index = paramClassReader.readUnsignedShort();
/*     */     }
/*     */ 
/*     */     public int length()
/*     */     {
/* 175 */       return 2;
/*     */     }
/*     */ 
/*     */     public <R, P> R accept(Annotation.element_value.Visitor<R, P> paramVisitor, P paramP) {
/* 179 */       return paramVisitor.visitClass(this, paramP);
/*     */     }
/*     */   }
/*     */ 
/*     */   public static class Enum_element_value extends Annotation.element_value
/*     */   {
/*     */     public final int type_name_index;
/*     */     public final int const_name_index;
/*     */ 
/*     */     Enum_element_value(ClassReader paramClassReader, int paramInt)
/*     */       throws IOException
/*     */     {
/* 149 */       super();
/* 150 */       this.type_name_index = paramClassReader.readUnsignedShort();
/* 151 */       this.const_name_index = paramClassReader.readUnsignedShort();
/*     */     }
/*     */ 
/*     */     public int length()
/*     */     {
/* 156 */       return 4;
/*     */     }
/*     */ 
/*     */     public <R, P> R accept(Annotation.element_value.Visitor<R, P> paramVisitor, P paramP) {
/* 160 */       return paramVisitor.visitEnum(this, paramP);
/*     */     }
/*     */   }
/*     */ 
/*     */   static class InvalidAnnotation extends AttributeException
/*     */   {
/*     */     private static final long serialVersionUID = -4620480740735772708L;
/*     */ 
/*     */     InvalidAnnotation(String paramString)
/*     */     {
/*  42 */       super();
/*     */     }
/*     */   }
/*     */ 
/*     */   public static class Primitive_element_value extends Annotation.element_value
/*     */   {
/*     */     public final int const_value_index;
/*     */ 
/*     */     Primitive_element_value(ClassReader paramClassReader, int paramInt)
/*     */       throws IOException
/*     */     {
/* 130 */       super();
/* 131 */       this.const_value_index = paramClassReader.readUnsignedShort();
/*     */     }
/*     */ 
/*     */     public int length()
/*     */     {
/* 136 */       return 2;
/*     */     }
/*     */ 
/*     */     public <R, P> R accept(Annotation.element_value.Visitor<R, P> paramVisitor, P paramP) {
/* 140 */       return paramVisitor.visitPrimitive(this, paramP);
/*     */     }
/*     */   }
/*     */ 
/*     */   public static abstract class element_value
/*     */   {
/*     */     public final int tag;
/*     */ 
/*     */     public static element_value read(ClassReader paramClassReader)
/*     */       throws IOException, Annotation.InvalidAnnotation
/*     */     {
/*  79 */       int i = paramClassReader.readUnsignedByte();
/*  80 */       switch (i) {
/*     */       case 66:
/*     */       case 67:
/*     */       case 68:
/*     */       case 70:
/*     */       case 73:
/*     */       case 74:
/*     */       case 83:
/*     */       case 90:
/*     */       case 115:
/*  90 */         return new Annotation.Primitive_element_value(paramClassReader, i);
/*     */       case 101:
/*  93 */         return new Annotation.Enum_element_value(paramClassReader, i);
/*     */       case 99:
/*  96 */         return new Annotation.Class_element_value(paramClassReader, i);
/*     */       case 64:
/*  99 */         return new Annotation.Annotation_element_value(paramClassReader, i);
/*     */       case 91:
/* 102 */         return new Annotation.Array_element_value(paramClassReader, i);
/*     */       case 65:
/*     */       case 69:
/*     */       case 71:
/*     */       case 72:
/*     */       case 75:
/*     */       case 76:
/*     */       case 77:
/*     */       case 78:
/*     */       case 79:
/*     */       case 80:
/*     */       case 81:
/*     */       case 82:
/*     */       case 84:
/*     */       case 85:
/*     */       case 86:
/*     */       case 87:
/*     */       case 88:
/*     */       case 89:
/*     */       case 92:
/*     */       case 93:
/*     */       case 94:
/*     */       case 95:
/*     */       case 96:
/*     */       case 97:
/*     */       case 98:
/*     */       case 100:
/*     */       case 102:
/*     */       case 103:
/*     */       case 104:
/*     */       case 105:
/*     */       case 106:
/*     */       case 107:
/*     */       case 108:
/*     */       case 109:
/*     */       case 110:
/*     */       case 111:
/*     */       case 112:
/*     */       case 113:
/* 105 */       case 114: } throw new Annotation.InvalidAnnotation("unrecognized tag: " + i);
/*     */     }
/*     */ 
/*     */     protected element_value(int paramInt)
/*     */     {
/* 110 */       this.tag = paramInt;
/*     */     }
/*     */ 
/*     */     public abstract int length();
/*     */ 
/*     */     public abstract <R, P> R accept(Visitor<R, P> paramVisitor, P paramP);
/*     */ 
/*     */     public static abstract interface Visitor<R, P>
/*     */     {
/*     */       public abstract R visitPrimitive(Annotation.Primitive_element_value paramPrimitive_element_value, P paramP);
/*     */ 
/*     */       public abstract R visitEnum(Annotation.Enum_element_value paramEnum_element_value, P paramP);
/*     */ 
/*     */       public abstract R visitClass(Annotation.Class_element_value paramClass_element_value, P paramP);
/*     */ 
/*     */       public abstract R visitAnnotation(Annotation.Annotation_element_value paramAnnotation_element_value, P paramP);
/*     */ 
/*     */       public abstract R visitArray(Annotation.Array_element_value paramArray_element_value, P paramP);
/*     */     }
/*     */   }
/*     */ 
/*     */   public static class element_value_pair
/*     */   {
/*     */     public final int element_name_index;
/*     */     public final Annotation.element_value value;
/*     */ 
/*     */     element_value_pair(ClassReader paramClassReader)
/*     */       throws IOException, Annotation.InvalidAnnotation
/*     */     {
/* 233 */       this.element_name_index = paramClassReader.readUnsignedShort();
/* 234 */       this.value = Annotation.element_value.read(paramClassReader);
/*     */     }
/*     */ 
/*     */     public int length() {
/* 238 */       return 2 + this.value.length();
/*     */     }
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.classfile.Annotation
 * JD-Core Version:    0.6.2
 */