/*     */ package com.sun.tools.classfile;
/*     */ 
/*     */ import java.io.IOException;
/*     */ 
/*     */ public class StackMapTable_attribute extends Attribute
/*     */ {
/*     */   public final int number_of_entries;
/*     */   public final stack_map_frame[] entries;
/*     */ 
/*     */   StackMapTable_attribute(ClassReader paramClassReader, int paramInt1, int paramInt2)
/*     */     throws IOException, StackMapTable_attribute.InvalidStackMap
/*     */   {
/*  48 */     super(paramInt1, paramInt2);
/*  49 */     this.number_of_entries = paramClassReader.readUnsignedShort();
/*  50 */     this.entries = new stack_map_frame[this.number_of_entries];
/*  51 */     for (int i = 0; i < this.number_of_entries; i++)
/*  52 */       this.entries[i] = stack_map_frame.read(paramClassReader);
/*     */   }
/*     */ 
/*     */   public StackMapTable_attribute(ConstantPool paramConstantPool, stack_map_frame[] paramArrayOfstack_map_frame) throws ConstantPoolException
/*     */   {
/*  57 */     this(paramConstantPool.getUTF8Index("StackMapTable"), paramArrayOfstack_map_frame);
/*     */   }
/*     */ 
/*     */   public StackMapTable_attribute(int paramInt, stack_map_frame[] paramArrayOfstack_map_frame) {
/*  61 */     super(paramInt, length(paramArrayOfstack_map_frame));
/*  62 */     this.number_of_entries = paramArrayOfstack_map_frame.length;
/*  63 */     this.entries = paramArrayOfstack_map_frame;
/*     */   }
/*     */ 
/*     */   public <R, D> R accept(Attribute.Visitor<R, D> paramVisitor, D paramD) {
/*  67 */     return paramVisitor.visitStackMapTable(this, paramD);
/*     */   }
/*     */ 
/*     */   static int length(stack_map_frame[] paramArrayOfstack_map_frame) {
/*  71 */     int i = 2;
/*  72 */     for (stack_map_frame localstack_map_frame : paramArrayOfstack_map_frame)
/*  73 */       i += localstack_map_frame.length();
/*  74 */     return i;
/*     */   }
/*     */ 
/*     */   static class InvalidStackMap extends AttributeException
/*     */   {
/*     */     private static final long serialVersionUID = -5659038410855089780L;
/*     */ 
/*     */     InvalidStackMap(String paramString)
/*     */     {
/*  42 */       super();
/*     */     }
/*     */   }
/*     */ 
/*     */   public static class Object_variable_info extends StackMapTable_attribute.verification_type_info
/*     */   {
/*     */     public final int cpool_index;
/*     */ 
/*     */     Object_variable_info(ClassReader paramClassReader)
/*     */       throws IOException
/*     */     {
/* 354 */       super();
/* 355 */       this.cpool_index = paramClassReader.readUnsignedShort();
/*     */     }
/*     */ 
/*     */     public int length()
/*     */     {
/* 360 */       return super.length() + 2;
/*     */     }
/*     */   }
/*     */ 
/*     */   public static class Uninitialized_variable_info extends StackMapTable_attribute.verification_type_info {
/*     */     public final int offset;
/*     */ 
/*     */     Uninitialized_variable_info(ClassReader paramClassReader) throws IOException {
/* 368 */       super();
/* 369 */       this.offset = paramClassReader.readUnsignedShort();
/*     */     }
/*     */ 
/*     */     public int length()
/*     */     {
/* 374 */       return super.length() + 2;
/*     */     }
/*     */   }
/*     */ 
/*     */   public static class append_frame extends StackMapTable_attribute.stack_map_frame
/*     */   {
/*     */     public final int offset_delta;
/*     */     public final StackMapTable_attribute.verification_type_info[] locals;
/*     */ 
/*     */     append_frame(int paramInt, ClassReader paramClassReader)
/*     */       throws IOException, StackMapTable_attribute.InvalidStackMap
/*     */     {
/* 238 */       super();
/* 239 */       this.offset_delta = paramClassReader.readUnsignedShort();
/* 240 */       this.locals = new StackMapTable_attribute.verification_type_info[paramInt - 251];
/* 241 */       for (int i = 0; i < this.locals.length; i++)
/* 242 */         this.locals[i] = StackMapTable_attribute.verification_type_info.read(paramClassReader);
/*     */     }
/*     */ 
/*     */     public int length()
/*     */     {
/* 247 */       int i = super.length() + 2;
/* 248 */       for (StackMapTable_attribute.verification_type_info localverification_type_info : this.locals)
/* 249 */         i += localverification_type_info.length();
/* 250 */       return i;
/*     */     }
/*     */ 
/*     */     public <R, D> R accept(StackMapTable_attribute.stack_map_frame.Visitor<R, D> paramVisitor, D paramD) {
/* 254 */       return paramVisitor.visit_append_frame(this, paramD);
/*     */     }
/*     */ 
/*     */     public int getOffsetDelta() {
/* 258 */       return this.offset_delta;
/*     */     }
/*     */   }
/*     */ 
/*     */   public static class chop_frame extends StackMapTable_attribute.stack_map_frame
/*     */   {
/*     */     public final int offset_delta;
/*     */ 
/*     */     chop_frame(int paramInt, ClassReader paramClassReader)
/*     */       throws IOException
/*     */     {
/* 193 */       super();
/* 194 */       this.offset_delta = paramClassReader.readUnsignedShort();
/*     */     }
/*     */ 
/*     */     public int length()
/*     */     {
/* 199 */       return super.length() + 2;
/*     */     }
/*     */ 
/*     */     public <R, D> R accept(StackMapTable_attribute.stack_map_frame.Visitor<R, D> paramVisitor, D paramD) {
/* 203 */       return paramVisitor.visit_chop_frame(this, paramD);
/*     */     }
/*     */ 
/*     */     public int getOffsetDelta() {
/* 207 */       return this.offset_delta;
/*     */     }
/*     */   }
/*     */ 
/*     */   public static class full_frame extends StackMapTable_attribute.stack_map_frame
/*     */   {
/*     */     public final int offset_delta;
/*     */     public final int number_of_locals;
/*     */     public final StackMapTable_attribute.verification_type_info[] locals;
/*     */     public final int number_of_stack_items;
/*     */     public final StackMapTable_attribute.verification_type_info[] stack;
/*     */ 
/*     */     full_frame(int paramInt, ClassReader paramClassReader)
/*     */       throws IOException, StackMapTable_attribute.InvalidStackMap
/*     */     {
/* 268 */       super();
/* 269 */       this.offset_delta = paramClassReader.readUnsignedShort();
/* 270 */       this.number_of_locals = paramClassReader.readUnsignedShort();
/* 271 */       this.locals = new StackMapTable_attribute.verification_type_info[this.number_of_locals];
/* 272 */       for (int i = 0; i < this.locals.length; i++)
/* 273 */         this.locals[i] = StackMapTable_attribute.verification_type_info.read(paramClassReader);
/* 274 */       this.number_of_stack_items = paramClassReader.readUnsignedShort();
/* 275 */       this.stack = new StackMapTable_attribute.verification_type_info[this.number_of_stack_items];
/* 276 */       for (i = 0; i < this.stack.length; i++)
/* 277 */         this.stack[i] = StackMapTable_attribute.verification_type_info.read(paramClassReader);
/*     */     }
/*     */ 
/*     */     public int length()
/*     */     {
/* 282 */       int i = super.length() + 2;
/*     */       StackMapTable_attribute.verification_type_info localverification_type_info;
/* 283 */       for (localverification_type_info : this.locals)
/* 284 */         i += localverification_type_info.length();
/* 285 */       i += 2;
/* 286 */       for (localverification_type_info : this.stack)
/* 287 */         i += localverification_type_info.length();
/* 288 */       return i;
/*     */     }
/*     */ 
/*     */     public <R, D> R accept(StackMapTable_attribute.stack_map_frame.Visitor<R, D> paramVisitor, D paramD) {
/* 292 */       return paramVisitor.visit_full_frame(this, paramD);
/*     */     }
/*     */ 
/*     */     public int getOffsetDelta() {
/* 296 */       return this.offset_delta;
/*     */     }
/*     */   }
/*     */ 
/*     */   public static class same_frame extends StackMapTable_attribute.stack_map_frame
/*     */   {
/*     */     same_frame(int paramInt)
/*     */     {
/* 129 */       super();
/*     */     }
/*     */ 
/*     */     public <R, D> R accept(StackMapTable_attribute.stack_map_frame.Visitor<R, D> paramVisitor, D paramD) {
/* 133 */       return paramVisitor.visit_same_frame(this, paramD);
/*     */     }
/*     */ 
/*     */     public int getOffsetDelta() {
/* 137 */       return this.frame_type;
/*     */     }
/*     */   }
/*     */ 
/*     */   public static class same_frame_extended extends StackMapTable_attribute.stack_map_frame
/*     */   {
/*     */     public final int offset_delta;
/*     */ 
/*     */     same_frame_extended(int paramInt, ClassReader paramClassReader)
/*     */       throws IOException
/*     */     {
/* 215 */       super();
/* 216 */       this.offset_delta = paramClassReader.readUnsignedShort();
/*     */     }
/*     */ 
/*     */     public int length()
/*     */     {
/* 221 */       return super.length() + 2;
/*     */     }
/*     */ 
/*     */     public <R, D> R accept(StackMapTable_attribute.stack_map_frame.Visitor<R, D> paramVisitor, D paramD) {
/* 225 */       return paramVisitor.visit_same_frame_extended(this, paramD);
/*     */     }
/*     */ 
/*     */     public int getOffsetDelta() {
/* 229 */       return this.offset_delta;
/*     */     }
/*     */   }
/*     */ 
/*     */   public static class same_locals_1_stack_item_frame extends StackMapTable_attribute.stack_map_frame
/*     */   {
/*     */     public final StackMapTable_attribute.verification_type_info[] stack;
/*     */ 
/*     */     same_locals_1_stack_item_frame(int paramInt, ClassReader paramClassReader)
/*     */       throws IOException, StackMapTable_attribute.InvalidStackMap
/*     */     {
/* 144 */       super();
/* 145 */       this.stack = new StackMapTable_attribute.verification_type_info[1];
/* 146 */       this.stack[0] = StackMapTable_attribute.verification_type_info.read(paramClassReader);
/*     */     }
/*     */ 
/*     */     public int length()
/*     */     {
/* 151 */       return super.length() + this.stack[0].length();
/*     */     }
/*     */ 
/*     */     public <R, D> R accept(StackMapTable_attribute.stack_map_frame.Visitor<R, D> paramVisitor, D paramD) {
/* 155 */       return paramVisitor.visit_same_locals_1_stack_item_frame(this, paramD);
/*     */     }
/*     */ 
/*     */     public int getOffsetDelta() {
/* 159 */       return this.frame_type - 64;
/*     */     }
/*     */   }
/*     */ 
/*     */   public static class same_locals_1_stack_item_frame_extended extends StackMapTable_attribute.stack_map_frame {
/*     */     public final int offset_delta;
/*     */     public final StackMapTable_attribute.verification_type_info[] stack;
/*     */ 
/* 168 */     same_locals_1_stack_item_frame_extended(int paramInt, ClassReader paramClassReader) throws IOException, StackMapTable_attribute.InvalidStackMap { super();
/* 169 */       this.offset_delta = paramClassReader.readUnsignedShort();
/* 170 */       this.stack = new StackMapTable_attribute.verification_type_info[1];
/* 171 */       this.stack[0] = StackMapTable_attribute.verification_type_info.read(paramClassReader);
/*     */     }
/*     */ 
/*     */     public int length()
/*     */     {
/* 176 */       return super.length() + 2 + this.stack[0].length();
/*     */     }
/*     */ 
/*     */     public <R, D> R accept(StackMapTable_attribute.stack_map_frame.Visitor<R, D> paramVisitor, D paramD) {
/* 180 */       return paramVisitor.visit_same_locals_1_stack_item_frame_extended(this, paramD);
/*     */     }
/*     */ 
/*     */     public int getOffsetDelta() {
/* 184 */       return this.offset_delta;
/*     */     }
/*     */   }
/*     */ 
/*     */   public static abstract class stack_map_frame
/*     */   {
/*     */     public final int frame_type;
/*     */ 
/*     */     static stack_map_frame read(ClassReader paramClassReader)
/*     */       throws IOException, StackMapTable_attribute.InvalidStackMap
/*     */     {
/*  83 */       int i = paramClassReader.readUnsignedByte();
/*  84 */       if (i <= 63)
/*  85 */         return new StackMapTable_attribute.same_frame(i);
/*  86 */       if (i <= 127)
/*  87 */         return new StackMapTable_attribute.same_locals_1_stack_item_frame(i, paramClassReader);
/*  88 */       if (i <= 246)
/*  89 */         throw new Error("unknown frame_type " + i);
/*  90 */       if (i == 247)
/*  91 */         return new StackMapTable_attribute.same_locals_1_stack_item_frame_extended(i, paramClassReader);
/*  92 */       if (i <= 250)
/*  93 */         return new StackMapTable_attribute.chop_frame(i, paramClassReader);
/*  94 */       if (i == 251)
/*  95 */         return new StackMapTable_attribute.same_frame_extended(i, paramClassReader);
/*  96 */       if (i <= 254) {
/*  97 */         return new StackMapTable_attribute.append_frame(i, paramClassReader);
/*     */       }
/*  99 */       return new StackMapTable_attribute.full_frame(i, paramClassReader);
/*     */     }
/*     */ 
/*     */     protected stack_map_frame(int paramInt) {
/* 103 */       this.frame_type = paramInt;
/*     */     }
/*     */ 
/*     */     public int length() {
/* 107 */       return 1;
/*     */     }
/*     */ 
/*     */     public abstract int getOffsetDelta();
/*     */ 
/*     */     public abstract <R, D> R accept(Visitor<R, D> paramVisitor, D paramD);
/*     */ 
/*     */     public static abstract interface Visitor<R, P>
/*     */     {
/*     */       public abstract R visit_same_frame(StackMapTable_attribute.same_frame paramsame_frame, P paramP);
/*     */ 
/*     */       public abstract R visit_same_locals_1_stack_item_frame(StackMapTable_attribute.same_locals_1_stack_item_frame paramsame_locals_1_stack_item_frame, P paramP);
/*     */ 
/*     */       public abstract R visit_same_locals_1_stack_item_frame_extended(StackMapTable_attribute.same_locals_1_stack_item_frame_extended paramsame_locals_1_stack_item_frame_extended, P paramP);
/*     */ 
/*     */       public abstract R visit_chop_frame(StackMapTable_attribute.chop_frame paramchop_frame, P paramP);
/*     */ 
/*     */       public abstract R visit_same_frame_extended(StackMapTable_attribute.same_frame_extended paramsame_frame_extended, P paramP);
/*     */ 
/*     */       public abstract R visit_append_frame(StackMapTable_attribute.append_frame paramappend_frame, P paramP);
/*     */ 
/*     */       public abstract R visit_full_frame(StackMapTable_attribute.full_frame paramfull_frame, P paramP);
/*     */     }
/*     */   }
/*     */ 
/*     */   public static class verification_type_info
/*     */   {
/*     */     public static final int ITEM_Top = 0;
/*     */     public static final int ITEM_Integer = 1;
/*     */     public static final int ITEM_Float = 2;
/*     */     public static final int ITEM_Long = 4;
/*     */     public static final int ITEM_Double = 3;
/*     */     public static final int ITEM_Null = 5;
/*     */     public static final int ITEM_UninitializedThis = 6;
/*     */     public static final int ITEM_Object = 7;
/*     */     public static final int ITEM_Uninitialized = 8;
/*     */     public final int tag;
/*     */ 
/*     */     static verification_type_info read(ClassReader paramClassReader)
/*     */       throws IOException, StackMapTable_attribute.InvalidStackMap
/*     */     {
/* 319 */       int i = paramClassReader.readUnsignedByte();
/* 320 */       switch (i) {
/*     */       case 0:
/*     */       case 1:
/*     */       case 2:
/*     */       case 3:
/*     */       case 4:
/*     */       case 5:
/*     */       case 6:
/* 328 */         return new verification_type_info(i);
/*     */       case 7:
/* 331 */         return new StackMapTable_attribute.Object_variable_info(paramClassReader);
/*     */       case 8:
/* 334 */         return new StackMapTable_attribute.Uninitialized_variable_info(paramClassReader);
/*     */       }
/*     */ 
/* 337 */       throw new StackMapTable_attribute.InvalidStackMap("unrecognized verification_type_info tag");
/*     */     }
/*     */ 
/*     */     protected verification_type_info(int paramInt)
/*     */     {
/* 342 */       this.tag = paramInt;
/*     */     }
/*     */ 
/*     */     public int length() {
/* 346 */       return 1;
/*     */     }
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.classfile.StackMapTable_attribute
 * JD-Core Version:    0.6.2
 */