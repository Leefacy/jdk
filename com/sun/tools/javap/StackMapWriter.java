/*     */ package com.sun.tools.javap;
/*     */ 
/*     */ import com.sun.tools.classfile.AccessFlags;
/*     */ import com.sun.tools.classfile.Attributes;
/*     */ import com.sun.tools.classfile.ClassFile;
/*     */ import com.sun.tools.classfile.Code_attribute;
/*     */ import com.sun.tools.classfile.ConstantPool;
/*     */ import com.sun.tools.classfile.ConstantPool.CONSTANT_Class_info;
/*     */ import com.sun.tools.classfile.ConstantPoolException;
/*     */ import com.sun.tools.classfile.Descriptor;
/*     */ import com.sun.tools.classfile.Descriptor.InvalidDescriptor;
/*     */ import com.sun.tools.classfile.Instruction;
/*     */ import com.sun.tools.classfile.Method;
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
/*     */ import java.util.Arrays;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ 
/*     */ public class StackMapWriter extends InstructionDetailWriter
/*     */ {
/*     */   private Map<Integer, StackMap> map;
/*     */   private ClassWriter classWriter;
/* 290 */   private final StackMapTable_attribute.verification_type_info[] empty = new StackMapTable_attribute.verification_type_info[0];
/*     */ 
/*     */   static StackMapWriter instance(Context paramContext)
/*     */   {
/*  56 */     StackMapWriter localStackMapWriter = (StackMapWriter)paramContext.get(StackMapWriter.class);
/*  57 */     if (localStackMapWriter == null)
/*  58 */       localStackMapWriter = new StackMapWriter(paramContext);
/*  59 */     return localStackMapWriter;
/*     */   }
/*     */ 
/*     */   protected StackMapWriter(Context paramContext) {
/*  63 */     super(paramContext);
/*  64 */     paramContext.put(StackMapWriter.class, this);
/*  65 */     this.classWriter = ClassWriter.instance(paramContext);
/*     */   }
/*     */ 
/*     */   public void reset(Code_attribute paramCode_attribute) {
/*  69 */     setStackMap((StackMapTable_attribute)paramCode_attribute.attributes.get("StackMapTable"));
/*     */   }
/*     */ 
/*     */   void setStackMap(StackMapTable_attribute paramStackMapTable_attribute) {
/*  73 */     if (paramStackMapTable_attribute == null) {
/*  74 */       this.map = null;
/*  75 */       return;
/*  78 */     }
/*     */ Method localMethod = this.classWriter.getMethod();
/*  79 */     Descriptor localDescriptor = localMethod.descriptor;
/*     */     String[] arrayOfString;
/*     */     try { ConstantPool localConstantPool = this.classWriter.getClassFile().constant_pool;
/*  83 */       localObject = localDescriptor.getParameterTypes(localConstantPool);
/*  84 */       arrayOfString = ((String)localObject).substring(1, ((String)localObject).length() - 1).split("[, ]+");
/*     */     } catch (ConstantPoolException localConstantPoolException) {
/*  86 */       return;
/*     */     } catch (Descriptor.InvalidDescriptor localInvalidDescriptor) {
/*  88 */       return;
/*     */     }
/*  90 */     boolean bool = localMethod.access_flags.is(8);
/*     */ 
/*  92 */     Object localObject = new StackMapTable_attribute.verification_type_info[(bool ? 0 : 1) + arrayOfString.length];
/*  93 */     if (!bool)
/*  94 */       localObject[0] = new CustomVerificationTypeInfo("this");
/*  95 */     for (int i = 0; i < arrayOfString.length; i++) {
/*  96 */       localObject[((bool ? 0 : 1) + i)] = new CustomVerificationTypeInfo(arrayOfString[i]
/*  97 */         .replace(".", "/"));
/*     */     }
/*     */ 
/* 100 */     this.map = new HashMap();
/* 101 */     StackMapBuilder localStackMapBuilder = new StackMapBuilder();
/*     */ 
/* 107 */     int j = -1;
/*     */ 
/* 109 */     this.map.put(Integer.valueOf(j), new StackMap((StackMapTable_attribute.verification_type_info[])localObject, this.empty));
/*     */ 
/* 111 */     for (int k = 0; k < paramStackMapTable_attribute.entries.length; k++)
/* 112 */       j = ((Integer)paramStackMapTable_attribute.entries[k].accept(localStackMapBuilder, Integer.valueOf(j))).intValue();
/*     */   }
/*     */ 
/*     */   public void writeInitialDetails() {
/* 116 */     writeDetails(-1);
/*     */   }
/*     */ 
/*     */   public void writeDetails(Instruction paramInstruction) {
/* 120 */     writeDetails(paramInstruction.getPC());
/*     */   }
/*     */ 
/*     */   private void writeDetails(int paramInt) {
/* 124 */     if (this.map == null) {
/* 125 */       return;
/*     */     }
/* 127 */     StackMap localStackMap = (StackMap)this.map.get(Integer.valueOf(paramInt));
/* 128 */     if (localStackMap != null) {
/* 129 */       print("StackMap locals: ", localStackMap.locals);
/* 130 */       print("StackMap stack: ", localStackMap.stack);
/*     */     }
/*     */   }
/*     */ 
/*     */   void print(String paramString, StackMapTable_attribute.verification_type_info[] paramArrayOfverification_type_info)
/*     */   {
/* 136 */     print(paramString);
/* 137 */     for (int i = 0; i < paramArrayOfverification_type_info.length; i++) {
/* 138 */       print(" ");
/* 139 */       print(paramArrayOfverification_type_info[i]);
/*     */     }
/* 141 */     println();
/*     */   }
/*     */ 
/*     */   void print(StackMapTable_attribute.verification_type_info paramverification_type_info) {
/* 145 */     if (paramverification_type_info == null) {
/* 146 */       print("ERROR");
/* 147 */       return;
/*     */     }
/*     */ 
/* 150 */     switch (paramverification_type_info.tag) {
/*     */     case -1:
/* 152 */       print(((CustomVerificationTypeInfo)paramverification_type_info).text);
/* 153 */       break;
/*     */     case 0:
/* 156 */       print("top");
/* 157 */       break;
/*     */     case 1:
/* 160 */       print("int");
/* 161 */       break;
/*     */     case 2:
/* 164 */       print("float");
/* 165 */       break;
/*     */     case 4:
/* 168 */       print("long");
/* 169 */       break;
/*     */     case 3:
/* 172 */       print("double");
/* 173 */       break;
/*     */     case 5:
/* 176 */       print("null");
/* 177 */       break;
/*     */     case 6:
/* 180 */       print("uninit_this");
/* 181 */       break;
/*     */     case 7:
/*     */       try
/*     */       {
/* 185 */         ConstantPool localConstantPool = this.classWriter.getClassFile().constant_pool;
/* 186 */         ConstantPool.CONSTANT_Class_info localCONSTANT_Class_info = localConstantPool.getClassInfo(((StackMapTable_attribute.Object_variable_info)paramverification_type_info).cpool_index);
/* 187 */         print(localConstantPool.getUTF8Value(localCONSTANT_Class_info.name_index));
/*     */       } catch (ConstantPoolException localConstantPoolException) {
/* 189 */         print("??");
/*     */       }
/*     */ 
/*     */     case 8:
/* 194 */       print(Integer.valueOf(((StackMapTable_attribute.Uninitialized_variable_info)paramverification_type_info).offset));
/*     */     }
/*     */   }
/*     */ 
/*     */   static class CustomVerificationTypeInfo extends StackMapTable_attribute.verification_type_info
/*     */   {
/*     */     private String text;
/*     */ 
/*     */     public CustomVerificationTypeInfo(String paramString)
/*     */     {
/* 284 */       super();
/* 285 */       this.text = paramString;
/*     */     }
/*     */   }
/*     */ 
/*     */   static class StackMap
/*     */   {
/*     */     private final StackMapTable_attribute.verification_type_info[] locals;
/*     */     private final StackMapTable_attribute.verification_type_info[] stack;
/*     */ 
/*     */     StackMap(StackMapTable_attribute.verification_type_info[] paramArrayOfverification_type_info1, StackMapTable_attribute.verification_type_info[] paramArrayOfverification_type_info2)
/*     */     {
/* 274 */       this.locals = paramArrayOfverification_type_info1;
/* 275 */       this.stack = paramArrayOfverification_type_info2;
/*     */     }
/*     */   }
/*     */ 
/*     */   class StackMapBuilder
/*     */     implements StackMapTable_attribute.stack_map_frame.Visitor<Integer, Integer>
/*     */   {
/*     */     StackMapBuilder()
/*     */     {
/*     */     }
/*     */ 
/*     */     public Integer visit_same_frame(StackMapTable_attribute.same_frame paramsame_frame, Integer paramInteger)
/*     */     {
/* 207 */       int i = paramInteger.intValue() + paramsame_frame.getOffsetDelta() + 1;
/* 208 */       StackMapWriter.StackMap localStackMap = (StackMapWriter.StackMap)StackMapWriter.this.map.get(paramInteger);
/* 209 */       assert (localStackMap != null);
/* 210 */       StackMapWriter.this.map.put(Integer.valueOf(i), localStackMap);
/* 211 */       return Integer.valueOf(i);
/*     */     }
/*     */ 
/*     */     public Integer visit_same_locals_1_stack_item_frame(StackMapTable_attribute.same_locals_1_stack_item_frame paramsame_locals_1_stack_item_frame, Integer paramInteger) {
/* 215 */       int i = paramInteger.intValue() + paramsame_locals_1_stack_item_frame.getOffsetDelta() + 1;
/* 216 */       StackMapWriter.StackMap localStackMap1 = (StackMapWriter.StackMap)StackMapWriter.this.map.get(paramInteger);
/* 217 */       assert (localStackMap1 != null);
/* 218 */       StackMapWriter.StackMap localStackMap2 = new StackMapWriter.StackMap(localStackMap1.locals, paramsame_locals_1_stack_item_frame.stack);
/* 219 */       StackMapWriter.this.map.put(Integer.valueOf(i), localStackMap2);
/* 220 */       return Integer.valueOf(i);
/*     */     }
/*     */ 
/*     */     public Integer visit_same_locals_1_stack_item_frame_extended(StackMapTable_attribute.same_locals_1_stack_item_frame_extended paramsame_locals_1_stack_item_frame_extended, Integer paramInteger) {
/* 224 */       int i = paramInteger.intValue() + paramsame_locals_1_stack_item_frame_extended.getOffsetDelta() + 1;
/* 225 */       StackMapWriter.StackMap localStackMap1 = (StackMapWriter.StackMap)StackMapWriter.this.map.get(paramInteger);
/* 226 */       assert (localStackMap1 != null);
/* 227 */       StackMapWriter.StackMap localStackMap2 = new StackMapWriter.StackMap(localStackMap1.locals, paramsame_locals_1_stack_item_frame_extended.stack);
/* 228 */       StackMapWriter.this.map.put(Integer.valueOf(i), localStackMap2);
/* 229 */       return Integer.valueOf(i);
/*     */     }
/*     */ 
/*     */     public Integer visit_chop_frame(StackMapTable_attribute.chop_frame paramchop_frame, Integer paramInteger) {
/* 233 */       int i = paramInteger.intValue() + paramchop_frame.getOffsetDelta() + 1;
/* 234 */       StackMapWriter.StackMap localStackMap1 = (StackMapWriter.StackMap)StackMapWriter.this.map.get(paramInteger);
/* 235 */       assert (localStackMap1 != null);
/* 236 */       int j = 251 - paramchop_frame.frame_type;
/* 237 */       StackMapTable_attribute.verification_type_info[] arrayOfverification_type_info = (StackMapTable_attribute.verification_type_info[])Arrays.copyOf(localStackMap1.locals, localStackMap1.locals.length - j);
/* 238 */       StackMapWriter.StackMap localStackMap2 = new StackMapWriter.StackMap(arrayOfverification_type_info, StackMapWriter.this.empty);
/* 239 */       StackMapWriter.this.map.put(Integer.valueOf(i), localStackMap2);
/* 240 */       return Integer.valueOf(i);
/*     */     }
/*     */ 
/*     */     public Integer visit_same_frame_extended(StackMapTable_attribute.same_frame_extended paramsame_frame_extended, Integer paramInteger) {
/* 244 */       int i = paramInteger.intValue() + paramsame_frame_extended.getOffsetDelta();
/* 245 */       StackMapWriter.StackMap localStackMap = (StackMapWriter.StackMap)StackMapWriter.this.map.get(paramInteger);
/* 246 */       assert (localStackMap != null);
/* 247 */       StackMapWriter.this.map.put(Integer.valueOf(i), localStackMap);
/* 248 */       return Integer.valueOf(i);
/*     */     }
/*     */ 
/*     */     public Integer visit_append_frame(StackMapTable_attribute.append_frame paramappend_frame, Integer paramInteger) {
/* 252 */       int i = paramInteger.intValue() + paramappend_frame.getOffsetDelta() + 1;
/* 253 */       StackMapWriter.StackMap localStackMap1 = (StackMapWriter.StackMap)StackMapWriter.this.map.get(paramInteger);
/* 254 */       assert (localStackMap1 != null);
/* 255 */       StackMapTable_attribute.verification_type_info[] arrayOfverification_type_info = new StackMapTable_attribute.verification_type_info[localStackMap1.locals.length + paramappend_frame.locals.length];
/* 256 */       System.arraycopy(localStackMap1.locals, 0, arrayOfverification_type_info, 0, localStackMap1.locals.length);
/* 257 */       System.arraycopy(paramappend_frame.locals, 0, arrayOfverification_type_info, localStackMap1.locals.length, paramappend_frame.locals.length);
/* 258 */       StackMapWriter.StackMap localStackMap2 = new StackMapWriter.StackMap(arrayOfverification_type_info, StackMapWriter.this.empty);
/* 259 */       StackMapWriter.this.map.put(Integer.valueOf(i), localStackMap2);
/* 260 */       return Integer.valueOf(i);
/*     */     }
/*     */ 
/*     */     public Integer visit_full_frame(StackMapTable_attribute.full_frame paramfull_frame, Integer paramInteger) {
/* 264 */       int i = paramInteger.intValue() + paramfull_frame.getOffsetDelta() + 1;
/* 265 */       StackMapWriter.StackMap localStackMap = new StackMapWriter.StackMap(paramfull_frame.locals, paramfull_frame.stack);
/* 266 */       StackMapWriter.this.map.put(Integer.valueOf(i), localStackMap);
/* 267 */       return Integer.valueOf(i);
/*     */     }
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.javap.StackMapWriter
 * JD-Core Version:    0.6.2
 */