/*     */ package com.sun.tools.classfile;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ 
/*     */ public class TypeAnnotation
/*     */ {
/*     */   public final ConstantPool constant_pool;
/*     */   public final Position position;
/*     */   public final Annotation annotation;
/*     */ 
/*     */   TypeAnnotation(ClassReader paramClassReader)
/*     */     throws IOException, Annotation.InvalidAnnotation
/*     */   {
/*  44 */     this.constant_pool = paramClassReader.getConstantPool();
/*  45 */     this.position = read_position(paramClassReader);
/*  46 */     this.annotation = new Annotation(paramClassReader);
/*     */   }
/*     */ 
/*     */   public TypeAnnotation(ConstantPool paramConstantPool, Annotation paramAnnotation, Position paramPosition)
/*     */   {
/*  51 */     this.constant_pool = paramConstantPool;
/*  52 */     this.position = paramPosition;
/*  53 */     this.annotation = paramAnnotation;
/*     */   }
/*     */ 
/*     */   public int length() {
/*  57 */     int i = this.annotation.length();
/*  58 */     i += position_length(this.position);
/*  59 */     return i;
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/*     */     try
/*     */     {
/*  66 */       return "@" + this.constant_pool.getUTF8Value(this.annotation.type_index).toString().substring(1) + " pos: " + this.position
/*  66 */         .toString();
/*     */     } catch (Exception localException) {
/*  68 */       localException.printStackTrace();
/*  69 */       return localException.toString();
/*     */     }
/*     */   }
/*     */ 
/*     */   private static Position read_position(ClassReader paramClassReader)
/*     */     throws IOException, Annotation.InvalidAnnotation
/*     */   {
/*  79 */     int i = paramClassReader.readUnsignedByte();
/*  80 */     if (!TargetType.isValidTargetTypeValue(i)) {
/*  81 */       throw new Annotation.InvalidAnnotation("TypeAnnotation: Invalid type annotation target type value: " + String.format("0x%02X", new Object[] { Integer.valueOf(i) }));
/*     */     }
/*  83 */     TargetType localTargetType = TargetType.fromTargetTypeValue(i);
/*     */ 
/*  85 */     Position localPosition = new Position();
/*  86 */     localPosition.type = localTargetType;
/*     */     int k;
/*  88 */     switch (1.$SwitchMap$com$sun$tools$classfile$TypeAnnotation$TargetType[localTargetType.ordinal()])
/*     */     {
/*     */     case 1:
/*     */     case 2:
/*     */     case 3:
/*     */     case 4:
/*  96 */       localPosition.offset = paramClassReader.readUnsignedShort();
/*  97 */       break;
/*     */     case 5:
/*     */     case 6:
/* 102 */       j = paramClassReader.readUnsignedShort();
/* 103 */       localPosition.lvarOffset = new int[j];
/* 104 */       localPosition.lvarLength = new int[j];
/* 105 */       localPosition.lvarIndex = new int[j];
/* 106 */       for (k = 0; k < j; k++) {
/* 107 */         localPosition.lvarOffset[k] = paramClassReader.readUnsignedShort();
/* 108 */         localPosition.lvarLength[k] = paramClassReader.readUnsignedShort();
/* 109 */         localPosition.lvarIndex[k] = paramClassReader.readUnsignedShort();
/*     */       }
/* 111 */       break;
/*     */     case 7:
/* 114 */       localPosition.exception_index = paramClassReader.readUnsignedShort();
/* 115 */       break;
/*     */     case 8:
/* 119 */       break;
/*     */     case 9:
/*     */     case 10:
/* 123 */       localPosition.parameter_index = paramClassReader.readUnsignedByte();
/* 124 */       break;
/*     */     case 11:
/*     */     case 12:
/* 128 */       localPosition.parameter_index = paramClassReader.readUnsignedByte();
/* 129 */       localPosition.bound_index = paramClassReader.readUnsignedByte();
/* 130 */       break;
/*     */     case 13:
/* 133 */       k = paramClassReader.readUnsignedShort();
/* 134 */       if (k == 65535)
/* 135 */         k = -1;
/* 136 */       localPosition.type_index = k;
/* 137 */       break;
/*     */     case 14:
/* 140 */       localPosition.type_index = paramClassReader.readUnsignedShort();
/* 141 */       break;
/*     */     case 15:
/* 144 */       localPosition.parameter_index = paramClassReader.readUnsignedByte();
/* 145 */       break;
/*     */     case 16:
/*     */     case 17:
/*     */     case 18:
/*     */     case 19:
/*     */     case 20:
/* 153 */       localPosition.offset = paramClassReader.readUnsignedShort();
/* 154 */       localPosition.type_index = paramClassReader.readUnsignedByte();
/* 155 */       break;
/*     */     case 21:
/*     */     case 22:
/* 159 */       break;
/*     */     case 23:
/* 161 */       throw new AssertionError("TypeAnnotation: UNKNOWN target type should never occur!");
/*     */     default:
/* 163 */       throw new AssertionError("TypeAnnotation: Unknown target type: " + localTargetType);
/*     */     }
/*     */ 
/* 167 */     int j = paramClassReader.readUnsignedByte();
/* 168 */     ArrayList localArrayList = new ArrayList(j);
/* 169 */     for (int m = 0; m < j * 2; m++)
/* 170 */       localArrayList.add(Integer.valueOf(paramClassReader.readUnsignedByte()));
/* 171 */     localPosition.location = Position.getTypePathFromBinary(localArrayList);
/*     */ 
/* 173 */     return localPosition;
/*     */   }
/*     */ 
/*     */   private static int position_length(Position paramPosition) {
/* 177 */     int i = 0;
/* 178 */     i++;
/* 179 */     switch (1.$SwitchMap$com$sun$tools$classfile$TypeAnnotation$TargetType[paramPosition.type.ordinal()])
/*     */     {
/*     */     case 1:
/*     */     case 2:
/*     */     case 3:
/*     */     case 4:
/* 187 */       i += 2;
/* 188 */       break;
/*     */     case 5:
/*     */     case 6:
/* 193 */       i += 2;
/* 194 */       int j = paramPosition.lvarOffset.length;
/* 195 */       i += 2 * j;
/* 196 */       i += 2 * j;
/* 197 */       i += 2 * j;
/* 198 */       break;
/*     */     case 7:
/* 201 */       i += 2;
/* 202 */       break;
/*     */     case 8:
/* 206 */       break;
/*     */     case 9:
/*     */     case 10:
/* 210 */       i++;
/* 211 */       break;
/*     */     case 11:
/*     */     case 12:
/* 215 */       i++;
/* 216 */       i++;
/* 217 */       break;
/*     */     case 13:
/* 220 */       i += 2;
/* 221 */       break;
/*     */     case 14:
/* 224 */       i += 2;
/* 225 */       break;
/*     */     case 15:
/* 228 */       i++;
/* 229 */       break;
/*     */     case 16:
/*     */     case 17:
/*     */     case 18:
/*     */     case 19:
/*     */     case 20:
/* 237 */       i += 2;
/* 238 */       i++;
/* 239 */       break;
/*     */     case 21:
/*     */     case 22:
/* 243 */       break;
/*     */     case 23:
/* 245 */       throw new AssertionError("TypeAnnotation: UNKNOWN target type should never occur!");
/*     */     default:
/* 247 */       throw new AssertionError("TypeAnnotation: Unknown target type: " + paramPosition.type);
/*     */     }
/*     */ 
/* 251 */     i++;
/* 252 */     i += 2 * paramPosition.location.size();
/*     */ 
/* 255 */     return i;
/*     */   }
/*     */ 
/*     */   public static class Position
/*     */   {
/* 341 */     public TypeAnnotation.TargetType type = TypeAnnotation.TargetType.UNKNOWN;
/*     */ 
/* 345 */     public List<TypePathEntry> location = new ArrayList(0);
/*     */ 
/* 348 */     public int pos = -1;
/*     */ 
/* 351 */     public boolean isValidOffset = false;
/* 352 */     public int offset = -1;
/*     */ 
/* 355 */     public int[] lvarOffset = null;
/* 356 */     public int[] lvarLength = null;
/* 357 */     public int[] lvarIndex = null;
/*     */ 
/* 360 */     public int bound_index = -2147483648;
/*     */ 
/* 363 */     public int parameter_index = -2147483648;
/*     */ 
/* 366 */     public int type_index = -2147483648;
/*     */ 
/* 369 */     public int exception_index = -2147483648;
/*     */ 
/*     */     public String toString()
/*     */     {
/* 375 */       StringBuilder localStringBuilder = new StringBuilder();
/* 376 */       localStringBuilder.append('[');
/* 377 */       localStringBuilder.append(this.type);
/*     */ 
/* 379 */       switch (TypeAnnotation.1.$SwitchMap$com$sun$tools$classfile$TypeAnnotation$TargetType[this.type.ordinal()])
/*     */       {
/*     */       case 1:
/*     */       case 2:
/*     */       case 3:
/*     */       case 4:
/* 387 */         localStringBuilder.append(", offset = ");
/* 388 */         localStringBuilder.append(this.offset);
/* 389 */         break;
/*     */       case 5:
/*     */       case 6:
/* 394 */         if (this.lvarOffset == null) {
/* 395 */           localStringBuilder.append(", lvarOffset is null!");
/*     */         }
/*     */         else {
/* 398 */           localStringBuilder.append(", {");
/* 399 */           for (int i = 0; i < this.lvarOffset.length; i++) {
/* 400 */             if (i != 0) localStringBuilder.append("; ");
/* 401 */             localStringBuilder.append("start_pc = ");
/* 402 */             localStringBuilder.append(this.lvarOffset[i]);
/* 403 */             localStringBuilder.append(", length = ");
/* 404 */             localStringBuilder.append(this.lvarLength[i]);
/* 405 */             localStringBuilder.append(", index = ");
/* 406 */             localStringBuilder.append(this.lvarIndex[i]);
/*     */           }
/* 408 */           localStringBuilder.append("}");
/* 409 */         }break;
/*     */       case 8:
/* 413 */         break;
/*     */       case 9:
/*     */       case 10:
/* 417 */         localStringBuilder.append(", param_index = ");
/* 418 */         localStringBuilder.append(this.parameter_index);
/* 419 */         break;
/*     */       case 11:
/*     */       case 12:
/* 423 */         localStringBuilder.append(", param_index = ");
/* 424 */         localStringBuilder.append(this.parameter_index);
/* 425 */         localStringBuilder.append(", bound_index = ");
/* 426 */         localStringBuilder.append(this.bound_index);
/* 427 */         break;
/*     */       case 13:
/* 430 */         localStringBuilder.append(", type_index = ");
/* 431 */         localStringBuilder.append(this.type_index);
/* 432 */         break;
/*     */       case 14:
/* 435 */         localStringBuilder.append(", type_index = ");
/* 436 */         localStringBuilder.append(this.type_index);
/* 437 */         break;
/*     */       case 7:
/* 440 */         localStringBuilder.append(", exception_index = ");
/* 441 */         localStringBuilder.append(this.exception_index);
/* 442 */         break;
/*     */       case 15:
/* 445 */         localStringBuilder.append(", param_index = ");
/* 446 */         localStringBuilder.append(this.parameter_index);
/* 447 */         break;
/*     */       case 16:
/*     */       case 17:
/*     */       case 18:
/*     */       case 19:
/*     */       case 20:
/* 455 */         localStringBuilder.append(", offset = ");
/* 456 */         localStringBuilder.append(this.offset);
/* 457 */         localStringBuilder.append(", type_index = ");
/* 458 */         localStringBuilder.append(this.type_index);
/* 459 */         break;
/*     */       case 21:
/*     */       case 22:
/* 463 */         break;
/*     */       case 23:
/* 465 */         localStringBuilder.append(", position UNKNOWN!");
/* 466 */         break;
/*     */       default:
/* 468 */         throw new AssertionError("Unknown target type: " + this.type);
/*     */       }
/*     */ 
/* 472 */       if (!this.location.isEmpty()) {
/* 473 */         localStringBuilder.append(", location = (");
/* 474 */         localStringBuilder.append(this.location);
/* 475 */         localStringBuilder.append(")");
/*     */       }
/*     */ 
/* 478 */       localStringBuilder.append(", pos = ");
/* 479 */       localStringBuilder.append(this.pos);
/*     */ 
/* 481 */       localStringBuilder.append(']');
/* 482 */       return localStringBuilder.toString();
/*     */     }
/*     */ 
/*     */     public boolean emitToClassfile()
/*     */     {
/* 491 */       return (!this.type.isLocal()) || (this.isValidOffset);
/*     */     }
/*     */ 
/*     */     public static List<TypePathEntry> getTypePathFromBinary(List<Integer> paramList)
/*     */     {
/* 501 */       ArrayList localArrayList = new ArrayList(paramList.size() / 2);
/* 502 */       int i = 0;
/* 503 */       while (i < paramList.size()) {
/* 504 */         if (i + 1 == paramList.size()) {
/* 505 */           throw new AssertionError("Could not decode type path: " + paramList);
/*     */         }
/* 507 */         localArrayList.add(TypePathEntry.fromBinary(((Integer)paramList.get(i)).intValue(), ((Integer)paramList.get(i + 1)).intValue()));
/* 508 */         i += 2;
/*     */       }
/* 510 */       return localArrayList;
/*     */     }
/*     */ 
/*     */     public static List<Integer> getBinaryFromTypePath(List<TypePathEntry> paramList) {
/* 514 */       ArrayList localArrayList = new ArrayList(paramList.size() * 2);
/* 515 */       for (TypePathEntry localTypePathEntry : paramList) {
/* 516 */         localArrayList.add(Integer.valueOf(localTypePathEntry.tag.tag));
/* 517 */         localArrayList.add(Integer.valueOf(localTypePathEntry.arg));
/*     */       }
/* 519 */       return localArrayList;
/*     */     }
/*     */ 
/*     */     public static class TypePathEntry
/*     */     {
/*     */       public static final int bytesPerEntry = 2;
/*     */       public final TypeAnnotation.Position.TypePathEntryKind tag;
/*     */       public final int arg;
/* 280 */       public static final TypePathEntry ARRAY = new TypePathEntry(TypeAnnotation.Position.TypePathEntryKind.ARRAY);
/* 281 */       public static final TypePathEntry INNER_TYPE = new TypePathEntry(TypeAnnotation.Position.TypePathEntryKind.INNER_TYPE);
/* 282 */       public static final TypePathEntry WILDCARD = new TypePathEntry(TypeAnnotation.Position.TypePathEntryKind.WILDCARD);
/*     */ 
/*     */       private TypePathEntry(TypeAnnotation.Position.TypePathEntryKind paramTypePathEntryKind) {
/* 285 */         if ((paramTypePathEntryKind != TypeAnnotation.Position.TypePathEntryKind.ARRAY) && (paramTypePathEntryKind != TypeAnnotation.Position.TypePathEntryKind.INNER_TYPE) && (paramTypePathEntryKind != TypeAnnotation.Position.TypePathEntryKind.WILDCARD))
/*     */         {
/* 288 */           throw new AssertionError("Invalid TypePathEntryKind: " + paramTypePathEntryKind);
/*     */         }
/* 290 */         this.tag = paramTypePathEntryKind;
/* 291 */         this.arg = 0;
/*     */       }
/*     */ 
/*     */       public TypePathEntry(TypeAnnotation.Position.TypePathEntryKind paramTypePathEntryKind, int paramInt) {
/* 295 */         if (paramTypePathEntryKind != TypeAnnotation.Position.TypePathEntryKind.TYPE_ARGUMENT) {
/* 296 */           throw new AssertionError("Invalid TypePathEntryKind: " + paramTypePathEntryKind);
/*     */         }
/* 298 */         this.tag = paramTypePathEntryKind;
/* 299 */         this.arg = paramInt;
/*     */       }
/*     */ 
/*     */       public static TypePathEntry fromBinary(int paramInt1, int paramInt2) {
/* 303 */         if ((paramInt2 != 0) && (paramInt1 != TypeAnnotation.Position.TypePathEntryKind.TYPE_ARGUMENT.tag)) {
/* 304 */           throw new AssertionError("Invalid TypePathEntry tag/arg: " + paramInt1 + "/" + paramInt2);
/*     */         }
/* 306 */         switch (paramInt1) {
/*     */         case 0:
/* 308 */           return ARRAY;
/*     */         case 1:
/* 310 */           return INNER_TYPE;
/*     */         case 2:
/* 312 */           return WILDCARD;
/*     */         case 3:
/* 314 */           return new TypePathEntry(TypeAnnotation.Position.TypePathEntryKind.TYPE_ARGUMENT, paramInt2);
/*     */         }
/* 316 */         throw new AssertionError("Invalid TypePathEntryKind tag: " + paramInt1);
/*     */       }
/*     */ 
/*     */       public String toString()
/*     */       {
/* 322 */         return this.tag.toString() + (this.tag == TypeAnnotation.Position.TypePathEntryKind.TYPE_ARGUMENT ? "(" + this.arg + ")" : "");
/*     */       }
/*     */ 
/*     */       public boolean equals(Object paramObject)
/*     */       {
/* 328 */         if (!(paramObject instanceof TypePathEntry)) {
/* 329 */           return false;
/*     */         }
/* 331 */         TypePathEntry localTypePathEntry = (TypePathEntry)paramObject;
/* 332 */         return (this.tag == localTypePathEntry.tag) && (this.arg == localTypePathEntry.arg);
/*     */       }
/*     */ 
/*     */       public int hashCode()
/*     */       {
/* 337 */         return this.tag.hashCode() * 17 + this.arg;
/*     */       }
/*     */     }
/*     */ 
/*     */     public static enum TypePathEntryKind
/*     */     {
/* 261 */       ARRAY(0), 
/* 262 */       INNER_TYPE(1), 
/* 263 */       WILDCARD(2), 
/* 264 */       TYPE_ARGUMENT(3);
/*     */ 
/*     */       public final int tag;
/*     */ 
/*     */       private TypePathEntryKind(int paramInt) {
/* 269 */         this.tag = paramInt;
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   public static enum TargetType
/*     */   {
/* 527 */     CLASS_TYPE_PARAMETER(0), 
/*     */ 
/* 530 */     METHOD_TYPE_PARAMETER(1), 
/*     */ 
/* 533 */     CLASS_EXTENDS(16), 
/*     */ 
/* 536 */     CLASS_TYPE_PARAMETER_BOUND(17), 
/*     */ 
/* 539 */     METHOD_TYPE_PARAMETER_BOUND(18), 
/*     */ 
/* 542 */     FIELD(19), 
/*     */ 
/* 545 */     METHOD_RETURN(20), 
/*     */ 
/* 548 */     METHOD_RECEIVER(21), 
/*     */ 
/* 551 */     METHOD_FORMAL_PARAMETER(22), 
/*     */ 
/* 554 */     THROWS(23), 
/*     */ 
/* 557 */     LOCAL_VARIABLE(64, true), 
/*     */ 
/* 560 */     RESOURCE_VARIABLE(65, true), 
/*     */ 
/* 563 */     EXCEPTION_PARAMETER(66, true), 
/*     */ 
/* 566 */     INSTANCEOF(67, true), 
/*     */ 
/* 569 */     NEW(68, true), 
/*     */ 
/* 572 */     CONSTRUCTOR_REFERENCE(69, true), 
/*     */ 
/* 575 */     METHOD_REFERENCE(70, true), 
/*     */ 
/* 578 */     CAST(71, true), 
/*     */ 
/* 581 */     CONSTRUCTOR_INVOCATION_TYPE_ARGUMENT(72, true), 
/*     */ 
/* 584 */     METHOD_INVOCATION_TYPE_ARGUMENT(73, true), 
/*     */ 
/* 587 */     CONSTRUCTOR_REFERENCE_TYPE_ARGUMENT(74, true), 
/*     */ 
/* 590 */     METHOD_REFERENCE_TYPE_ARGUMENT(75, true), 
/*     */ 
/* 593 */     UNKNOWN(255);
/*     */ 
/*     */     private static final int MAXIMUM_TARGET_TYPE_VALUE = 75;
/*     */     private final int targetTypeValue;
/*     */     private final boolean isLocal;
/*     */     private static final TargetType[] targets;
/*     */ 
/* 601 */     private TargetType(int paramInt) { this(paramInt, false); }
/*     */ 
/*     */     private TargetType(int paramInt, boolean paramBoolean)
/*     */     {
/* 605 */       if ((paramInt < 0) || (paramInt > 255))
/*     */       {
/* 607 */         throw new AssertionError("Attribute type value needs to be an unsigned byte: " + String.format("0x%02X", new Object[] { Integer.valueOf(paramInt) }));
/* 608 */       }this.targetTypeValue = paramInt;
/* 609 */       this.isLocal = paramBoolean;
/*     */     }
/*     */ 
/*     */     public boolean isLocal()
/*     */     {
/* 620 */       return this.isLocal;
/*     */     }
/*     */ 
/*     */     public int targetTypeValue() {
/* 624 */       return this.targetTypeValue;
/*     */     }
/*     */ 
/*     */     public static boolean isValidTargetTypeValue(int paramInt)
/*     */     {
/* 643 */       if (paramInt == UNKNOWN.targetTypeValue)
/* 644 */         return true;
/* 645 */       return (paramInt >= 0) && (paramInt < targets.length);
/*     */     }
/*     */ 
/*     */     public static TargetType fromTargetTypeValue(int paramInt) {
/* 649 */       if (paramInt == UNKNOWN.targetTypeValue) {
/* 650 */         return UNKNOWN;
/*     */       }
/* 652 */       if ((paramInt < 0) || (paramInt >= targets.length))
/* 653 */         throw new AssertionError("Unknown TargetType: " + paramInt);
/* 654 */       return targets[paramInt];
/*     */     }
/*     */ 
/*     */     static
/*     */     {
/* 630 */       targets = new TargetType[76];
/* 631 */       TargetType[] arrayOfTargetType1 = values();
/* 632 */       for (TargetType localTargetType : arrayOfTargetType1) {
/* 633 */         if (localTargetType.targetTypeValue != UNKNOWN.targetTypeValue)
/* 634 */           targets[localTargetType.targetTypeValue] = localTargetType;
/*     */       }
/* 636 */       for (int i = 0; i <= 75; i++)
/* 637 */         if (targets[i] == null)
/* 638 */           targets[i] = UNKNOWN;
/*     */     }
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.classfile.TypeAnnotation
 * JD-Core Version:    0.6.2
 */