/*     */ package com.sun.tools.classfile;
/*     */ 
/*     */ import java.util.Map;
/*     */ 
/*     */ public class ClassTranslator
/*     */   implements ConstantPool.Visitor<ConstantPool.CPInfo, Map<Object, Object>>
/*     */ {
/*     */   public ClassFile translate(ClassFile paramClassFile, Map<Object, Object> paramMap)
/*     */   {
/*  66 */     ClassFile localClassFile = (ClassFile)paramMap.get(paramClassFile);
/*  67 */     if (localClassFile == null) {
/*  68 */       ConstantPool localConstantPool = translate(paramClassFile.constant_pool, paramMap);
/*  69 */       Field[] arrayOfField = translate(paramClassFile.fields, paramClassFile.constant_pool, paramMap);
/*  70 */       Method[] arrayOfMethod = translateMethods(paramClassFile.methods, paramClassFile.constant_pool, paramMap);
/*  71 */       Attributes localAttributes = translateAttributes(paramClassFile.attributes, paramClassFile.constant_pool, paramMap);
/*     */ 
/*  74 */       if ((localConstantPool == paramClassFile.constant_pool) && (arrayOfField == paramClassFile.fields) && (arrayOfMethod == paramClassFile.methods) && (localAttributes == paramClassFile.attributes))
/*     */       {
/*  78 */         localClassFile = paramClassFile;
/*     */       }
/*  80 */       else localClassFile = new ClassFile(paramClassFile.magic, paramClassFile.minor_version, paramClassFile.major_version, localConstantPool, paramClassFile.access_flags, paramClassFile.this_class, paramClassFile.super_class, paramClassFile.interfaces, arrayOfField, arrayOfMethod, localAttributes);
/*     */ 
/*  92 */       paramMap.put(paramClassFile, localClassFile);
/*     */     }
/*  94 */     return localClassFile;
/*     */   }
/*     */ 
/*     */   ConstantPool translate(ConstantPool paramConstantPool, Map<Object, Object> paramMap) {
/*  98 */     ConstantPool localConstantPool = (ConstantPool)paramMap.get(paramConstantPool);
/*  99 */     if (localConstantPool == null) {
/* 100 */       ConstantPool.CPInfo[] arrayOfCPInfo = new ConstantPool.CPInfo[paramConstantPool.size()];
/* 101 */       int i = 1;
/* 102 */       for (int j = 0; j < paramConstantPool.size(); ) {
/*     */         ConstantPool.CPInfo localCPInfo1;
/*     */         try {
/* 105 */           localCPInfo1 = paramConstantPool.get(j);
/*     */         } catch (ConstantPool.InvalidIndex localInvalidIndex) {
/* 107 */           throw new IllegalStateException(localInvalidIndex);
/*     */         }
/* 109 */         ConstantPool.CPInfo localCPInfo2 = translate(localCPInfo1, paramMap);
/* 110 */         i &= (localCPInfo1 == localCPInfo2 ? 1 : 0);
/* 111 */         arrayOfCPInfo[j] = localCPInfo2;
/* 112 */         if (localCPInfo1.getTag() != localCPInfo2.getTag())
/* 113 */           throw new IllegalStateException();
/* 114 */         j += localCPInfo1.size();
/*     */       }
/*     */ 
/* 117 */       if (i != 0)
/* 118 */         localConstantPool = paramConstantPool;
/*     */       else {
/* 120 */         localConstantPool = new ConstantPool(arrayOfCPInfo);
/*     */       }
/* 122 */       paramMap.put(paramConstantPool, localConstantPool);
/*     */     }
/* 124 */     return localConstantPool;
/*     */   }
/*     */ 
/*     */   ConstantPool.CPInfo translate(ConstantPool.CPInfo paramCPInfo, Map<Object, Object> paramMap) {
/* 128 */     ConstantPool.CPInfo localCPInfo = (ConstantPool.CPInfo)paramMap.get(paramCPInfo);
/* 129 */     if (localCPInfo == null) {
/* 130 */       localCPInfo = (ConstantPool.CPInfo)paramCPInfo.accept(this, paramMap);
/* 131 */       paramMap.put(paramCPInfo, localCPInfo);
/*     */     }
/* 133 */     return localCPInfo;
/*     */   }
/*     */ 
/*     */   Field[] translate(Field[] paramArrayOfField, ConstantPool paramConstantPool, Map<Object, Object> paramMap) {
/* 137 */     Field[] arrayOfField = (Field[])paramMap.get(paramArrayOfField);
/* 138 */     if (arrayOfField == null) {
/* 139 */       arrayOfField = new Field[paramArrayOfField.length];
/* 140 */       for (int i = 0; i < paramArrayOfField.length; i++)
/* 141 */         arrayOfField[i] = translate(paramArrayOfField[i], paramConstantPool, paramMap);
/* 142 */       if (equal(paramArrayOfField, arrayOfField))
/* 143 */         arrayOfField = paramArrayOfField;
/* 144 */       paramMap.put(paramArrayOfField, arrayOfField);
/*     */     }
/* 146 */     return arrayOfField;
/*     */   }
/*     */ 
/*     */   Field translate(Field paramField, ConstantPool paramConstantPool, Map<Object, Object> paramMap) {
/* 150 */     Field localField = (Field)paramMap.get(paramField);
/* 151 */     if (localField == null) {
/* 152 */       Attributes localAttributes = translateAttributes(paramField.attributes, paramConstantPool, paramMap);
/*     */ 
/* 155 */       if (localAttributes == paramField.attributes)
/* 156 */         localField = paramField;
/*     */       else {
/* 158 */         localField = new Field(paramField.access_flags, paramField.name_index, paramField.descriptor, localAttributes);
/*     */       }
/*     */ 
/* 163 */       paramMap.put(paramField, localField);
/*     */     }
/* 165 */     return localField;
/*     */   }
/*     */ 
/*     */   Method[] translateMethods(Method[] paramArrayOfMethod, ConstantPool paramConstantPool, Map<Object, Object> paramMap) {
/* 169 */     Method[] arrayOfMethod = (Method[])paramMap.get(paramArrayOfMethod);
/* 170 */     if (arrayOfMethod == null) {
/* 171 */       arrayOfMethod = new Method[paramArrayOfMethod.length];
/* 172 */       for (int i = 0; i < paramArrayOfMethod.length; i++)
/* 173 */         arrayOfMethod[i] = translate(paramArrayOfMethod[i], paramConstantPool, paramMap);
/* 174 */       if (equal(paramArrayOfMethod, arrayOfMethod))
/* 175 */         arrayOfMethod = paramArrayOfMethod;
/* 176 */       paramMap.put(paramArrayOfMethod, arrayOfMethod);
/*     */     }
/* 178 */     return arrayOfMethod;
/*     */   }
/*     */ 
/*     */   Method translate(Method paramMethod, ConstantPool paramConstantPool, Map<Object, Object> paramMap) {
/* 182 */     Method localMethod = (Method)paramMap.get(paramMethod);
/* 183 */     if (localMethod == null) {
/* 184 */       Attributes localAttributes = translateAttributes(paramMethod.attributes, paramConstantPool, paramMap);
/*     */ 
/* 187 */       if (localAttributes == paramMethod.attributes)
/* 188 */         localMethod = paramMethod;
/*     */       else {
/* 190 */         localMethod = new Method(paramMethod.access_flags, paramMethod.name_index, paramMethod.descriptor, localAttributes);
/*     */       }
/*     */ 
/* 195 */       paramMap.put(paramMethod, localMethod);
/*     */     }
/* 197 */     return localMethod;
/*     */   }
/*     */ 
/*     */   Attributes translateAttributes(Attributes paramAttributes, ConstantPool paramConstantPool, Map<Object, Object> paramMap)
/*     */   {
/* 202 */     Attributes localAttributes = (Attributes)paramMap.get(paramAttributes);
/* 203 */     if (localAttributes == null) {
/* 204 */       Attribute[] arrayOfAttribute = new Attribute[paramAttributes.size()];
/* 205 */       ConstantPool localConstantPool = translate(paramConstantPool, paramMap);
/* 206 */       int i = 1;
/* 207 */       for (int j = 0; j < paramAttributes.size(); j++) {
/* 208 */         Attribute localAttribute1 = paramAttributes.get(j);
/* 209 */         Attribute localAttribute2 = translate(localAttribute1, paramMap);
/* 210 */         if (localAttribute2 != localAttribute1)
/* 211 */           i = 0;
/* 212 */         arrayOfAttribute[j] = localAttribute2;
/*     */       }
/* 214 */       if ((localConstantPool == paramConstantPool) && (i != 0))
/* 215 */         localAttributes = paramAttributes;
/*     */       else
/* 217 */         localAttributes = new Attributes(localConstantPool, arrayOfAttribute);
/* 218 */       paramMap.put(paramAttributes, localAttributes);
/*     */     }
/* 220 */     return localAttributes;
/*     */   }
/*     */ 
/*     */   Attribute translate(Attribute paramAttribute, Map<Object, Object> paramMap) {
/* 224 */     Attribute localAttribute = (Attribute)paramMap.get(paramAttribute);
/* 225 */     if (localAttribute == null) {
/* 226 */       localAttribute = paramAttribute;
/*     */ 
/* 228 */       paramMap.put(paramAttribute, localAttribute);
/*     */     }
/* 230 */     return localAttribute;
/*     */   }
/*     */ 
/*     */   private static <T> boolean equal(T[] paramArrayOfT1, T[] paramArrayOfT2) {
/* 234 */     if ((paramArrayOfT1 == null) || (paramArrayOfT2 == null))
/* 235 */       return paramArrayOfT1 == paramArrayOfT2;
/* 236 */     if (paramArrayOfT1.length != paramArrayOfT2.length)
/* 237 */       return false;
/* 238 */     for (int i = 0; i < paramArrayOfT1.length; i++) {
/* 239 */       if (paramArrayOfT1[i] != paramArrayOfT2[i])
/* 240 */         return false;
/*     */     }
/* 242 */     return true;
/*     */   }
/*     */ 
/*     */   public ConstantPool.CPInfo visitClass(ConstantPool.CONSTANT_Class_info paramCONSTANT_Class_info, Map<Object, Object> paramMap) {
/* 246 */     ConstantPool.CONSTANT_Class_info localCONSTANT_Class_info = (ConstantPool.CONSTANT_Class_info)paramMap.get(paramCONSTANT_Class_info);
/* 247 */     if (localCONSTANT_Class_info == null) {
/* 248 */       ConstantPool localConstantPool = translate(paramCONSTANT_Class_info.cp, paramMap);
/* 249 */       if (localConstantPool == paramCONSTANT_Class_info.cp)
/* 250 */         localCONSTANT_Class_info = paramCONSTANT_Class_info;
/*     */       else
/* 252 */         localCONSTANT_Class_info = new ConstantPool.CONSTANT_Class_info(localConstantPool, paramCONSTANT_Class_info.name_index);
/* 253 */       paramMap.put(paramCONSTANT_Class_info, localCONSTANT_Class_info);
/*     */     }
/* 255 */     return paramCONSTANT_Class_info;
/*     */   }
/*     */ 
/*     */   public ConstantPool.CPInfo visitDouble(ConstantPool.CONSTANT_Double_info paramCONSTANT_Double_info, Map<Object, Object> paramMap) {
/* 259 */     ConstantPool.CONSTANT_Double_info localCONSTANT_Double_info = (ConstantPool.CONSTANT_Double_info)paramMap.get(paramCONSTANT_Double_info);
/* 260 */     if (localCONSTANT_Double_info == null) {
/* 261 */       localCONSTANT_Double_info = paramCONSTANT_Double_info;
/* 262 */       paramMap.put(paramCONSTANT_Double_info, localCONSTANT_Double_info);
/*     */     }
/* 264 */     return paramCONSTANT_Double_info;
/*     */   }
/*     */ 
/*     */   public ConstantPool.CPInfo visitFieldref(ConstantPool.CONSTANT_Fieldref_info paramCONSTANT_Fieldref_info, Map<Object, Object> paramMap) {
/* 268 */     ConstantPool.CONSTANT_Fieldref_info localCONSTANT_Fieldref_info = (ConstantPool.CONSTANT_Fieldref_info)paramMap.get(paramCONSTANT_Fieldref_info);
/* 269 */     if (localCONSTANT_Fieldref_info == null) {
/* 270 */       ConstantPool localConstantPool = translate(paramCONSTANT_Fieldref_info.cp, paramMap);
/* 271 */       if (localConstantPool == paramCONSTANT_Fieldref_info.cp)
/* 272 */         localCONSTANT_Fieldref_info = paramCONSTANT_Fieldref_info;
/*     */       else
/* 274 */         localCONSTANT_Fieldref_info = new ConstantPool.CONSTANT_Fieldref_info(localConstantPool, paramCONSTANT_Fieldref_info.class_index, paramCONSTANT_Fieldref_info.name_and_type_index);
/* 275 */       paramMap.put(paramCONSTANT_Fieldref_info, localCONSTANT_Fieldref_info);
/*     */     }
/* 277 */     return paramCONSTANT_Fieldref_info;
/*     */   }
/*     */ 
/*     */   public ConstantPool.CPInfo visitFloat(ConstantPool.CONSTANT_Float_info paramCONSTANT_Float_info, Map<Object, Object> paramMap) {
/* 281 */     ConstantPool.CONSTANT_Float_info localCONSTANT_Float_info = (ConstantPool.CONSTANT_Float_info)paramMap.get(paramCONSTANT_Float_info);
/* 282 */     if (localCONSTANT_Float_info == null) {
/* 283 */       localCONSTANT_Float_info = paramCONSTANT_Float_info;
/* 284 */       paramMap.put(paramCONSTANT_Float_info, localCONSTANT_Float_info);
/*     */     }
/* 286 */     return paramCONSTANT_Float_info;
/*     */   }
/*     */ 
/*     */   public ConstantPool.CPInfo visitInteger(ConstantPool.CONSTANT_Integer_info paramCONSTANT_Integer_info, Map<Object, Object> paramMap) {
/* 290 */     ConstantPool.CONSTANT_Integer_info localCONSTANT_Integer_info = (ConstantPool.CONSTANT_Integer_info)paramMap.get(paramCONSTANT_Integer_info);
/* 291 */     if (localCONSTANT_Integer_info == null) {
/* 292 */       localCONSTANT_Integer_info = paramCONSTANT_Integer_info;
/* 293 */       paramMap.put(paramCONSTANT_Integer_info, localCONSTANT_Integer_info);
/*     */     }
/* 295 */     return paramCONSTANT_Integer_info;
/*     */   }
/*     */ 
/*     */   public ConstantPool.CPInfo visitInterfaceMethodref(ConstantPool.CONSTANT_InterfaceMethodref_info paramCONSTANT_InterfaceMethodref_info, Map<Object, Object> paramMap) {
/* 299 */     ConstantPool.CONSTANT_InterfaceMethodref_info localCONSTANT_InterfaceMethodref_info = (ConstantPool.CONSTANT_InterfaceMethodref_info)paramMap.get(paramCONSTANT_InterfaceMethodref_info);
/* 300 */     if (localCONSTANT_InterfaceMethodref_info == null) {
/* 301 */       ConstantPool localConstantPool = translate(paramCONSTANT_InterfaceMethodref_info.cp, paramMap);
/* 302 */       if (localConstantPool == paramCONSTANT_InterfaceMethodref_info.cp)
/* 303 */         localCONSTANT_InterfaceMethodref_info = paramCONSTANT_InterfaceMethodref_info;
/*     */       else
/* 305 */         localCONSTANT_InterfaceMethodref_info = new ConstantPool.CONSTANT_InterfaceMethodref_info(localConstantPool, paramCONSTANT_InterfaceMethodref_info.class_index, paramCONSTANT_InterfaceMethodref_info.name_and_type_index);
/* 306 */       paramMap.put(paramCONSTANT_InterfaceMethodref_info, localCONSTANT_InterfaceMethodref_info);
/*     */     }
/* 308 */     return paramCONSTANT_InterfaceMethodref_info;
/*     */   }
/*     */ 
/*     */   public ConstantPool.CPInfo visitInvokeDynamic(ConstantPool.CONSTANT_InvokeDynamic_info paramCONSTANT_InvokeDynamic_info, Map<Object, Object> paramMap) {
/* 312 */     ConstantPool.CONSTANT_InvokeDynamic_info localCONSTANT_InvokeDynamic_info = (ConstantPool.CONSTANT_InvokeDynamic_info)paramMap.get(paramCONSTANT_InvokeDynamic_info);
/* 313 */     if (localCONSTANT_InvokeDynamic_info == null) {
/* 314 */       ConstantPool localConstantPool = translate(paramCONSTANT_InvokeDynamic_info.cp, paramMap);
/* 315 */       if (localConstantPool == paramCONSTANT_InvokeDynamic_info.cp)
/* 316 */         localCONSTANT_InvokeDynamic_info = paramCONSTANT_InvokeDynamic_info;
/*     */       else {
/* 318 */         localCONSTANT_InvokeDynamic_info = new ConstantPool.CONSTANT_InvokeDynamic_info(localConstantPool, paramCONSTANT_InvokeDynamic_info.bootstrap_method_attr_index, paramCONSTANT_InvokeDynamic_info.name_and_type_index);
/*     */       }
/* 320 */       paramMap.put(paramCONSTANT_InvokeDynamic_info, localCONSTANT_InvokeDynamic_info);
/*     */     }
/* 322 */     return paramCONSTANT_InvokeDynamic_info;
/*     */   }
/*     */ 
/*     */   public ConstantPool.CPInfo visitLong(ConstantPool.CONSTANT_Long_info paramCONSTANT_Long_info, Map<Object, Object> paramMap) {
/* 326 */     ConstantPool.CONSTANT_Long_info localCONSTANT_Long_info = (ConstantPool.CONSTANT_Long_info)paramMap.get(paramCONSTANT_Long_info);
/* 327 */     if (localCONSTANT_Long_info == null) {
/* 328 */       localCONSTANT_Long_info = paramCONSTANT_Long_info;
/* 329 */       paramMap.put(paramCONSTANT_Long_info, localCONSTANT_Long_info);
/*     */     }
/* 331 */     return paramCONSTANT_Long_info;
/*     */   }
/*     */ 
/*     */   public ConstantPool.CPInfo visitNameAndType(ConstantPool.CONSTANT_NameAndType_info paramCONSTANT_NameAndType_info, Map<Object, Object> paramMap) {
/* 335 */     ConstantPool.CONSTANT_NameAndType_info localCONSTANT_NameAndType_info = (ConstantPool.CONSTANT_NameAndType_info)paramMap.get(paramCONSTANT_NameAndType_info);
/* 336 */     if (localCONSTANT_NameAndType_info == null) {
/* 337 */       ConstantPool localConstantPool = translate(paramCONSTANT_NameAndType_info.cp, paramMap);
/* 338 */       if (localConstantPool == paramCONSTANT_NameAndType_info.cp)
/* 339 */         localCONSTANT_NameAndType_info = paramCONSTANT_NameAndType_info;
/*     */       else
/* 341 */         localCONSTANT_NameAndType_info = new ConstantPool.CONSTANT_NameAndType_info(localConstantPool, paramCONSTANT_NameAndType_info.name_index, paramCONSTANT_NameAndType_info.type_index);
/* 342 */       paramMap.put(paramCONSTANT_NameAndType_info, localCONSTANT_NameAndType_info);
/*     */     }
/* 344 */     return paramCONSTANT_NameAndType_info;
/*     */   }
/*     */ 
/*     */   public ConstantPool.CPInfo visitMethodref(ConstantPool.CONSTANT_Methodref_info paramCONSTANT_Methodref_info, Map<Object, Object> paramMap) {
/* 348 */     ConstantPool.CONSTANT_Methodref_info localCONSTANT_Methodref_info = (ConstantPool.CONSTANT_Methodref_info)paramMap.get(paramCONSTANT_Methodref_info);
/* 349 */     if (localCONSTANT_Methodref_info == null) {
/* 350 */       ConstantPool localConstantPool = translate(paramCONSTANT_Methodref_info.cp, paramMap);
/* 351 */       if (localConstantPool == paramCONSTANT_Methodref_info.cp)
/* 352 */         localCONSTANT_Methodref_info = paramCONSTANT_Methodref_info;
/*     */       else
/* 354 */         localCONSTANT_Methodref_info = new ConstantPool.CONSTANT_Methodref_info(localConstantPool, paramCONSTANT_Methodref_info.class_index, paramCONSTANT_Methodref_info.name_and_type_index);
/* 355 */       paramMap.put(paramCONSTANT_Methodref_info, localCONSTANT_Methodref_info);
/*     */     }
/* 357 */     return paramCONSTANT_Methodref_info;
/*     */   }
/*     */ 
/*     */   public ConstantPool.CPInfo visitMethodHandle(ConstantPool.CONSTANT_MethodHandle_info paramCONSTANT_MethodHandle_info, Map<Object, Object> paramMap) {
/* 361 */     ConstantPool.CONSTANT_MethodHandle_info localCONSTANT_MethodHandle_info = (ConstantPool.CONSTANT_MethodHandle_info)paramMap.get(paramCONSTANT_MethodHandle_info);
/* 362 */     if (localCONSTANT_MethodHandle_info == null) {
/* 363 */       ConstantPool localConstantPool = translate(paramCONSTANT_MethodHandle_info.cp, paramMap);
/* 364 */       if (localConstantPool == paramCONSTANT_MethodHandle_info.cp)
/* 365 */         localCONSTANT_MethodHandle_info = paramCONSTANT_MethodHandle_info;
/*     */       else {
/* 367 */         localCONSTANT_MethodHandle_info = new ConstantPool.CONSTANT_MethodHandle_info(localConstantPool, paramCONSTANT_MethodHandle_info.reference_kind, paramCONSTANT_MethodHandle_info.reference_index);
/*     */       }
/* 369 */       paramMap.put(paramCONSTANT_MethodHandle_info, localCONSTANT_MethodHandle_info);
/*     */     }
/* 371 */     return paramCONSTANT_MethodHandle_info;
/*     */   }
/*     */ 
/*     */   public ConstantPool.CPInfo visitMethodType(ConstantPool.CONSTANT_MethodType_info paramCONSTANT_MethodType_info, Map<Object, Object> paramMap) {
/* 375 */     ConstantPool.CONSTANT_MethodType_info localCONSTANT_MethodType_info = (ConstantPool.CONSTANT_MethodType_info)paramMap.get(paramCONSTANT_MethodType_info);
/* 376 */     if (localCONSTANT_MethodType_info == null) {
/* 377 */       ConstantPool localConstantPool = translate(paramCONSTANT_MethodType_info.cp, paramMap);
/* 378 */       if (localConstantPool == paramCONSTANT_MethodType_info.cp)
/* 379 */         localCONSTANT_MethodType_info = paramCONSTANT_MethodType_info;
/*     */       else {
/* 381 */         localCONSTANT_MethodType_info = new ConstantPool.CONSTANT_MethodType_info(localConstantPool, paramCONSTANT_MethodType_info.descriptor_index);
/*     */       }
/* 383 */       paramMap.put(paramCONSTANT_MethodType_info, localCONSTANT_MethodType_info);
/*     */     }
/* 385 */     return paramCONSTANT_MethodType_info;
/*     */   }
/*     */ 
/*     */   public ConstantPool.CPInfo visitString(ConstantPool.CONSTANT_String_info paramCONSTANT_String_info, Map<Object, Object> paramMap) {
/* 389 */     ConstantPool.CONSTANT_String_info localCONSTANT_String_info = (ConstantPool.CONSTANT_String_info)paramMap.get(paramCONSTANT_String_info);
/* 390 */     if (localCONSTANT_String_info == null) {
/* 391 */       ConstantPool localConstantPool = translate(paramCONSTANT_String_info.cp, paramMap);
/* 392 */       if (localConstantPool == paramCONSTANT_String_info.cp)
/* 393 */         localCONSTANT_String_info = paramCONSTANT_String_info;
/*     */       else
/* 395 */         localCONSTANT_String_info = new ConstantPool.CONSTANT_String_info(localConstantPool, paramCONSTANT_String_info.string_index);
/* 396 */       paramMap.put(paramCONSTANT_String_info, localCONSTANT_String_info);
/*     */     }
/* 398 */     return paramCONSTANT_String_info;
/*     */   }
/*     */ 
/*     */   public ConstantPool.CPInfo visitUtf8(ConstantPool.CONSTANT_Utf8_info paramCONSTANT_Utf8_info, Map<Object, Object> paramMap) {
/* 402 */     ConstantPool.CONSTANT_Utf8_info localCONSTANT_Utf8_info = (ConstantPool.CONSTANT_Utf8_info)paramMap.get(paramCONSTANT_Utf8_info);
/* 403 */     if (localCONSTANT_Utf8_info == null) {
/* 404 */       localCONSTANT_Utf8_info = paramCONSTANT_Utf8_info;
/* 405 */       paramMap.put(paramCONSTANT_Utf8_info, localCONSTANT_Utf8_info);
/*     */     }
/* 407 */     return paramCONSTANT_Utf8_info;
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.classfile.ClassTranslator
 * JD-Core Version:    0.6.2
 */