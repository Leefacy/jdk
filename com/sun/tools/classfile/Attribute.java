/*     */ package com.sun.tools.classfile;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.lang.reflect.Constructor;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ 
/*     */ public abstract class Attribute
/*     */ {
/*     */   public static final String AnnotationDefault = "AnnotationDefault";
/*     */   public static final String BootstrapMethods = "BootstrapMethods";
/*     */   public static final String CharacterRangeTable = "CharacterRangeTable";
/*     */   public static final String Code = "Code";
/*     */   public static final String ConstantValue = "ConstantValue";
/*     */   public static final String CompilationID = "CompilationID";
/*     */   public static final String Deprecated = "Deprecated";
/*     */   public static final String EnclosingMethod = "EnclosingMethod";
/*     */   public static final String Exceptions = "Exceptions";
/*     */   public static final String InnerClasses = "InnerClasses";
/*     */   public static final String LineNumberTable = "LineNumberTable";
/*     */   public static final String LocalVariableTable = "LocalVariableTable";
/*     */   public static final String LocalVariableTypeTable = "LocalVariableTypeTable";
/*     */   public static final String MethodParameters = "MethodParameters";
/*     */   public static final String RuntimeVisibleAnnotations = "RuntimeVisibleAnnotations";
/*     */   public static final String RuntimeInvisibleAnnotations = "RuntimeInvisibleAnnotations";
/*     */   public static final String RuntimeVisibleParameterAnnotations = "RuntimeVisibleParameterAnnotations";
/*     */   public static final String RuntimeInvisibleParameterAnnotations = "RuntimeInvisibleParameterAnnotations";
/*     */   public static final String RuntimeVisibleTypeAnnotations = "RuntimeVisibleTypeAnnotations";
/*     */   public static final String RuntimeInvisibleTypeAnnotations = "RuntimeInvisibleTypeAnnotations";
/*     */   public static final String Signature = "Signature";
/*     */   public static final String SourceDebugExtension = "SourceDebugExtension";
/*     */   public static final String SourceFile = "SourceFile";
/*     */   public static final String SourceID = "SourceID";
/*     */   public static final String StackMap = "StackMap";
/*     */   public static final String StackMapTable = "StackMapTable";
/*     */   public static final String Synthetic = "Synthetic";
/*     */   public final int attribute_name_index;
/*     */   public final int attribute_length;
/*     */ 
/*     */   public static Attribute read(ClassReader paramClassReader)
/*     */     throws IOException
/*     */   {
/* 140 */     return paramClassReader.readAttribute();
/*     */   }
/*     */ 
/*     */   protected Attribute(int paramInt1, int paramInt2) {
/* 144 */     this.attribute_name_index = paramInt1;
/* 145 */     this.attribute_length = paramInt2;
/*     */   }
/*     */ 
/*     */   public String getName(ConstantPool paramConstantPool) throws ConstantPoolException {
/* 149 */     return paramConstantPool.getUTF8Value(this.attribute_name_index);
/*     */   }
/*     */ 
/*     */   public abstract <R, D> R accept(Visitor<R, D> paramVisitor, D paramD);
/*     */ 
/*     */   public int byteLength() {
/* 155 */     return 6 + this.attribute_length;
/*     */   }
/*     */ 
/*     */   public static class Factory
/*     */   {
/*     */     private Map<String, Class<? extends Attribute>> standardAttributes;
/*     */ 
/*     */     public Attribute createAttribute(ClassReader paramClassReader, int paramInt, byte[] paramArrayOfByte)
/*     */       throws IOException
/*     */     {
/*  76 */       if (this.standardAttributes == null) {
/*  77 */         init();
/*  80 */       }
/*     */ ConstantPool localConstantPool = paramClassReader.getConstantPool();
/*     */       String str1;
/*     */       try {
/*  83 */         String str2 = localConstantPool.getUTF8Value(paramInt);
/*  84 */         Class localClass = (Class)this.standardAttributes.get(str2);
/*  85 */         if (localClass != null) {
/*     */           try {
/*  87 */             Class[] arrayOfClass = { ClassReader.class, Integer.TYPE, Integer.TYPE };
/*  88 */             Constructor localConstructor = localClass.getDeclaredConstructor(arrayOfClass);
/*  89 */             return (Attribute)localConstructor.newInstance(new Object[] { paramClassReader, Integer.valueOf(paramInt), Integer.valueOf(paramArrayOfByte.length) });
/*     */           } catch (Throwable localThrowable) {
/*  91 */             str1 = localThrowable.toString();
/*     */           }
/*     */         }
/*     */         else
/*     */         {
/*  96 */           str1 = "unknown attribute";
/*     */         }
/*     */       } catch (ConstantPoolException localConstantPoolException) {
/*  99 */         str1 = localConstantPoolException.toString();
/*     */       }
/*     */ 
/* 102 */       return new DefaultAttribute(paramClassReader, paramInt, paramArrayOfByte, str1);
/*     */     }
/*     */ 
/*     */     protected void init() {
/* 106 */       this.standardAttributes = new HashMap();
/* 107 */       this.standardAttributes.put("AnnotationDefault", AnnotationDefault_attribute.class);
/* 108 */       this.standardAttributes.put("BootstrapMethods", BootstrapMethods_attribute.class);
/* 109 */       this.standardAttributes.put("CharacterRangeTable", CharacterRangeTable_attribute.class);
/* 110 */       this.standardAttributes.put("Code", Code_attribute.class);
/* 111 */       this.standardAttributes.put("CompilationID", CompilationID_attribute.class);
/* 112 */       this.standardAttributes.put("ConstantValue", ConstantValue_attribute.class);
/* 113 */       this.standardAttributes.put("Deprecated", Deprecated_attribute.class);
/* 114 */       this.standardAttributes.put("EnclosingMethod", EnclosingMethod_attribute.class);
/* 115 */       this.standardAttributes.put("Exceptions", Exceptions_attribute.class);
/* 116 */       this.standardAttributes.put("InnerClasses", InnerClasses_attribute.class);
/* 117 */       this.standardAttributes.put("LineNumberTable", LineNumberTable_attribute.class);
/* 118 */       this.standardAttributes.put("LocalVariableTable", LocalVariableTable_attribute.class);
/* 119 */       this.standardAttributes.put("LocalVariableTypeTable", LocalVariableTypeTable_attribute.class);
/* 120 */       this.standardAttributes.put("MethodParameters", MethodParameters_attribute.class);
/* 121 */       this.standardAttributes.put("RuntimeInvisibleAnnotations", RuntimeInvisibleAnnotations_attribute.class);
/* 122 */       this.standardAttributes.put("RuntimeInvisibleParameterAnnotations", RuntimeInvisibleParameterAnnotations_attribute.class);
/* 123 */       this.standardAttributes.put("RuntimeVisibleAnnotations", RuntimeVisibleAnnotations_attribute.class);
/* 124 */       this.standardAttributes.put("RuntimeVisibleParameterAnnotations", RuntimeVisibleParameterAnnotations_attribute.class);
/* 125 */       this.standardAttributes.put("RuntimeVisibleTypeAnnotations", RuntimeVisibleTypeAnnotations_attribute.class);
/* 126 */       this.standardAttributes.put("RuntimeInvisibleTypeAnnotations", RuntimeInvisibleTypeAnnotations_attribute.class);
/* 127 */       this.standardAttributes.put("Signature", Signature_attribute.class);
/* 128 */       this.standardAttributes.put("SourceDebugExtension", SourceDebugExtension_attribute.class);
/* 129 */       this.standardAttributes.put("SourceFile", SourceFile_attribute.class);
/* 130 */       this.standardAttributes.put("SourceID", SourceID_attribute.class);
/* 131 */       this.standardAttributes.put("StackMap", StackMap_attribute.class);
/* 132 */       this.standardAttributes.put("StackMapTable", StackMapTable_attribute.class);
/* 133 */       this.standardAttributes.put("Synthetic", Synthetic_attribute.class);
/*     */     }
/*     */   }
/*     */ 
/*     */   public static abstract interface Visitor<R, P>
/*     */   {
/*     */     public abstract R visitBootstrapMethods(BootstrapMethods_attribute paramBootstrapMethods_attribute, P paramP);
/*     */ 
/*     */     public abstract R visitDefault(DefaultAttribute paramDefaultAttribute, P paramP);
/*     */ 
/*     */     public abstract R visitAnnotationDefault(AnnotationDefault_attribute paramAnnotationDefault_attribute, P paramP);
/*     */ 
/*     */     public abstract R visitCharacterRangeTable(CharacterRangeTable_attribute paramCharacterRangeTable_attribute, P paramP);
/*     */ 
/*     */     public abstract R visitCode(Code_attribute paramCode_attribute, P paramP);
/*     */ 
/*     */     public abstract R visitCompilationID(CompilationID_attribute paramCompilationID_attribute, P paramP);
/*     */ 
/*     */     public abstract R visitConstantValue(ConstantValue_attribute paramConstantValue_attribute, P paramP);
/*     */ 
/*     */     public abstract R visitDeprecated(Deprecated_attribute paramDeprecated_attribute, P paramP);
/*     */ 
/*     */     public abstract R visitEnclosingMethod(EnclosingMethod_attribute paramEnclosingMethod_attribute, P paramP);
/*     */ 
/*     */     public abstract R visitExceptions(Exceptions_attribute paramExceptions_attribute, P paramP);
/*     */ 
/*     */     public abstract R visitInnerClasses(InnerClasses_attribute paramInnerClasses_attribute, P paramP);
/*     */ 
/*     */     public abstract R visitLineNumberTable(LineNumberTable_attribute paramLineNumberTable_attribute, P paramP);
/*     */ 
/*     */     public abstract R visitLocalVariableTable(LocalVariableTable_attribute paramLocalVariableTable_attribute, P paramP);
/*     */ 
/*     */     public abstract R visitLocalVariableTypeTable(LocalVariableTypeTable_attribute paramLocalVariableTypeTable_attribute, P paramP);
/*     */ 
/*     */     public abstract R visitMethodParameters(MethodParameters_attribute paramMethodParameters_attribute, P paramP);
/*     */ 
/*     */     public abstract R visitRuntimeVisibleAnnotations(RuntimeVisibleAnnotations_attribute paramRuntimeVisibleAnnotations_attribute, P paramP);
/*     */ 
/*     */     public abstract R visitRuntimeInvisibleAnnotations(RuntimeInvisibleAnnotations_attribute paramRuntimeInvisibleAnnotations_attribute, P paramP);
/*     */ 
/*     */     public abstract R visitRuntimeVisibleParameterAnnotations(RuntimeVisibleParameterAnnotations_attribute paramRuntimeVisibleParameterAnnotations_attribute, P paramP);
/*     */ 
/*     */     public abstract R visitRuntimeInvisibleParameterAnnotations(RuntimeInvisibleParameterAnnotations_attribute paramRuntimeInvisibleParameterAnnotations_attribute, P paramP);
/*     */ 
/*     */     public abstract R visitRuntimeVisibleTypeAnnotations(RuntimeVisibleTypeAnnotations_attribute paramRuntimeVisibleTypeAnnotations_attribute, P paramP);
/*     */ 
/*     */     public abstract R visitRuntimeInvisibleTypeAnnotations(RuntimeInvisibleTypeAnnotations_attribute paramRuntimeInvisibleTypeAnnotations_attribute, P paramP);
/*     */ 
/*     */     public abstract R visitSignature(Signature_attribute paramSignature_attribute, P paramP);
/*     */ 
/*     */     public abstract R visitSourceDebugExtension(SourceDebugExtension_attribute paramSourceDebugExtension_attribute, P paramP);
/*     */ 
/*     */     public abstract R visitSourceFile(SourceFile_attribute paramSourceFile_attribute, P paramP);
/*     */ 
/*     */     public abstract R visitSourceID(SourceID_attribute paramSourceID_attribute, P paramP);
/*     */ 
/*     */     public abstract R visitStackMap(StackMap_attribute paramStackMap_attribute, P paramP);
/*     */ 
/*     */     public abstract R visitStackMapTable(StackMapTable_attribute paramStackMapTable_attribute, P paramP);
/*     */ 
/*     */     public abstract R visitSynthetic(Synthetic_attribute paramSynthetic_attribute, P paramP);
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.classfile.Attribute
 * JD-Core Version:    0.6.2
 */