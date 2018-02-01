/*     */ package com.sun.tools.classfile;
/*     */ 
/*     */ import java.util.Locale;
/*     */ 
/*     */ public class Instruction
/*     */ {
/*     */   private byte[] bytes;
/*     */   private int pc;
/*     */ 
/*     */   public Instruction(byte[] paramArrayOfByte, int paramInt)
/*     */   {
/* 162 */     this.bytes = paramArrayOfByte;
/* 163 */     this.pc = paramInt;
/*     */   }
/*     */ 
/*     */   public int getPC()
/*     */   {
/* 168 */     return this.pc;
/*     */   }
/*     */ 
/*     */   public int getByte(int paramInt)
/*     */   {
/* 173 */     return this.bytes[(this.pc + paramInt)];
/*     */   }
/*     */ 
/*     */   public int getUnsignedByte(int paramInt)
/*     */   {
/* 178 */     return getByte(paramInt) & 0xFF;
/*     */   }
/*     */ 
/*     */   public int getShort(int paramInt)
/*     */   {
/* 183 */     return getByte(paramInt) << 8 | getUnsignedByte(paramInt + 1);
/*     */   }
/*     */ 
/*     */   public int getUnsignedShort(int paramInt)
/*     */   {
/* 188 */     return getShort(paramInt) & 0xFFFF;
/*     */   }
/*     */ 
/*     */   public int getInt(int paramInt)
/*     */   {
/* 193 */     return getShort(paramInt) << 16 | getUnsignedShort(paramInt + 2);
/*     */   }
/*     */ 
/*     */   public Opcode getOpcode()
/*     */   {
/* 199 */     int i = getUnsignedByte(0);
/* 200 */     switch (i) {
/*     */     case 196:
/*     */     case 254:
/*     */     case 255:
/* 204 */       return Opcode.get(i, getUnsignedByte(1));
/*     */     }
/* 206 */     return Opcode.get(i);
/*     */   }
/*     */ 
/*     */   public String getMnemonic()
/*     */   {
/* 212 */     Opcode localOpcode = getOpcode();
/* 213 */     if (localOpcode == null) {
/* 214 */       return "bytecode " + getUnsignedByte(0);
/*     */     }
/* 216 */     return localOpcode.toString().toLowerCase(Locale.US);
/*     */   }
/*     */ 
/*     */   public int length()
/*     */   {
/* 222 */     Opcode localOpcode = getOpcode();
/* 223 */     if (localOpcode == null)
/* 224 */       return 1;
/*     */     int i;
/*     */     int j;
/* 226 */     switch (localOpcode) {
/*     */     case TABLESWITCH:
/* 228 */       i = align(this.pc + 1) - this.pc;
/* 229 */       j = getInt(i + 4);
/* 230 */       int k = getInt(i + 8);
/* 231 */       return i + 12 + 4 * (k - j + 1);
/*     */     case LOOKUPSWITCH:
/* 234 */       i = align(this.pc + 1) - this.pc;
/* 235 */       j = getInt(i + 4);
/* 236 */       return i + 8 + 8 * j;
/*     */     }
/*     */ 
/* 240 */     return localOpcode.kind.length;
/*     */   }
/*     */ 
/*     */   public Kind getKind()
/*     */   {
/* 246 */     Opcode localOpcode = getOpcode();
/* 247 */     return localOpcode != null ? localOpcode.kind : Kind.UNKNOWN;
/*     */   }
/*     */ 
/*     */   public <R, P> R accept(KindVisitor<R, P> paramKindVisitor, P paramP)
/*     */   {
/* 253 */     switch (1.$SwitchMap$com$sun$tools$classfile$Instruction$Kind[getKind().ordinal()]) {
/*     */     case 1:
/* 255 */       return paramKindVisitor.visitNoOperands(this, paramP);
/*     */     case 2:
/* 258 */       return paramKindVisitor.visitArrayType(this, 
/* 259 */         TypeKind.get(getUnsignedByte(1)), 
/* 259 */         paramP);
/*     */     case 3:
/* 262 */       return paramKindVisitor.visitBranch(this, getShort(1), paramP);
/*     */     case 4:
/* 265 */       return paramKindVisitor.visitBranch(this, getInt(1), paramP);
/*     */     case 5:
/* 268 */       return paramKindVisitor.visitValue(this, getByte(1), paramP);
/*     */     case 6:
/* 271 */       return paramKindVisitor.visitConstantPoolRef(this, getUnsignedByte(1), paramP);
/*     */     case 7:
/* 274 */       return paramKindVisitor.visitConstantPoolRef(this, getUnsignedShort(1), paramP);
/*     */     case 8:
/*     */     case 9:
/* 278 */       return paramKindVisitor.visitConstantPoolRefAndValue(this, 
/* 279 */         getUnsignedShort(1), 
/* 279 */         getUnsignedByte(3), paramP);
/*     */     case 10:
/*     */       int i;
/*     */       int j;
/*     */       int k;
/*     */       int[] arrayOfInt2;
/*     */       int n;
/* 282 */       switch (getOpcode()) {
/*     */       case TABLESWITCH:
/* 284 */         i = align(this.pc + 1) - this.pc;
/* 285 */         j = getInt(i);
/* 286 */         k = getInt(i + 4);
/* 287 */         int m = getInt(i + 8);
/* 288 */         arrayOfInt2 = new int[m - k + 1];
/* 289 */         for (n = 0; n < arrayOfInt2.length; n++)
/* 290 */           arrayOfInt2[n] = getInt(i + 12 + 4 * n);
/* 291 */         return paramKindVisitor.visitTableSwitch(this, j, k, m, arrayOfInt2, paramP);
/*     */       case LOOKUPSWITCH:
/* 295 */         i = align(this.pc + 1) - this.pc;
/* 296 */         j = getInt(i);
/* 297 */         k = getInt(i + 4);
/* 298 */         int[] arrayOfInt1 = new int[k];
/* 299 */         arrayOfInt2 = new int[k];
/* 300 */         for (n = 0; n < k; n++) {
/* 301 */           arrayOfInt1[n] = getInt(i + 8 + n * 8);
/* 302 */           arrayOfInt2[n] = getInt(i + 12 + n * 8);
/*     */         }
/* 304 */         return paramKindVisitor.visitLookupSwitch(this, j, k, arrayOfInt1, arrayOfInt2, paramP);
/*     */       }
/*     */ 
/* 308 */       throw new IllegalStateException();
/*     */     case 11:
/* 313 */       return paramKindVisitor.visitLocal(this, getUnsignedByte(1), paramP);
/*     */     case 12:
/* 316 */       return paramKindVisitor.visitLocalAndValue(this, 
/* 317 */         getUnsignedByte(1), 
/* 317 */         getByte(2), paramP);
/*     */     case 13:
/* 320 */       return paramKindVisitor.visitValue(this, getShort(1), paramP);
/*     */     case 14:
/* 323 */       return paramKindVisitor.visitNoOperands(this, paramP);
/*     */     case 15:
/* 326 */       return paramKindVisitor.visitLocal(this, getUnsignedShort(2), paramP);
/*     */     case 16:
/* 329 */       return paramKindVisitor.visitConstantPoolRef(this, getUnsignedShort(2), paramP);
/*     */     case 17:
/* 332 */       return paramKindVisitor.visitConstantPoolRefAndValue(this, 
/* 333 */         getUnsignedShort(2), 
/* 333 */         getUnsignedByte(4), paramP);
/*     */     case 18:
/* 336 */       return paramKindVisitor.visitLocalAndValue(this, 
/* 337 */         getUnsignedShort(2), 
/* 337 */         getShort(4), paramP);
/*     */     case 19:
/* 340 */       return paramKindVisitor.visitUnknown(this, paramP);
/*     */     }
/*     */ 
/* 343 */     throw new IllegalStateException();
/*     */   }
/*     */ 
/*     */   private static int align(int paramInt)
/*     */   {
/* 348 */     return paramInt + 3 & 0xFFFFFFFC;
/*     */   }
/*     */ 
/*     */   public static enum Kind
/*     */   {
/*  45 */     NO_OPERANDS(1), 
/*     */ 
/*  47 */     ATYPE(2), 
/*     */ 
/*  49 */     BRANCH(3), 
/*     */ 
/*  51 */     BRANCH_W(5), 
/*     */ 
/*  53 */     BYTE(2), 
/*     */ 
/*  55 */     CPREF(2), 
/*     */ 
/*  57 */     CPREF_W(3), 
/*     */ 
/*  60 */     CPREF_W_UBYTE(4), 
/*     */ 
/*  63 */     CPREF_W_UBYTE_ZERO(5), 
/*     */ 
/*  66 */     DYNAMIC(-1), 
/*     */ 
/*  68 */     LOCAL(2), 
/*     */ 
/*  71 */     LOCAL_BYTE(3), 
/*     */ 
/*  73 */     SHORT(3), 
/*     */ 
/*  75 */     WIDE_NO_OPERANDS(2), 
/*     */ 
/*  77 */     WIDE_LOCAL(4), 
/*     */ 
/*  79 */     WIDE_CPREF_W(4), 
/*     */ 
/*  82 */     WIDE_CPREF_W_SHORT(6), 
/*     */ 
/*  85 */     WIDE_LOCAL_SHORT(6), 
/*     */ 
/*  87 */     UNKNOWN(1);
/*     */ 
/*     */     public final int length;
/*     */ 
/*  90 */     private Kind(int paramInt) { this.length = paramInt; }
/*     */ 
/*     */   }
/*     */ 
/*     */   public static abstract interface KindVisitor<R, P>
/*     */   {
/*     */     public abstract R visitNoOperands(Instruction paramInstruction, P paramP);
/*     */ 
/*     */     public abstract R visitArrayType(Instruction paramInstruction, Instruction.TypeKind paramTypeKind, P paramP);
/*     */ 
/*     */     public abstract R visitBranch(Instruction paramInstruction, int paramInt, P paramP);
/*     */ 
/*     */     public abstract R visitConstantPoolRef(Instruction paramInstruction, int paramInt, P paramP);
/*     */ 
/*     */     public abstract R visitConstantPoolRefAndValue(Instruction paramInstruction, int paramInt1, int paramInt2, P paramP);
/*     */ 
/*     */     public abstract R visitLocal(Instruction paramInstruction, int paramInt, P paramP);
/*     */ 
/*     */     public abstract R visitLocalAndValue(Instruction paramInstruction, int paramInt1, int paramInt2, P paramP);
/*     */ 
/*     */     public abstract R visitLookupSwitch(Instruction paramInstruction, int paramInt1, int paramInt2, int[] paramArrayOfInt1, int[] paramArrayOfInt2, P paramP);
/*     */ 
/*     */     public abstract R visitTableSwitch(Instruction paramInstruction, int paramInt1, int paramInt2, int paramInt3, int[] paramArrayOfInt, P paramP);
/*     */ 
/*     */     public abstract R visitValue(Instruction paramInstruction, int paramInt, P paramP);
/*     */ 
/*     */     public abstract R visitUnknown(Instruction paramInstruction, P paramP);
/*     */   }
/*     */ 
/*     */   public static enum TypeKind
/*     */   {
/* 129 */     T_BOOLEAN(4, "boolean"), 
/* 130 */     T_CHAR(5, "char"), 
/* 131 */     T_FLOAT(6, "float"), 
/* 132 */     T_DOUBLE(7, "double"), 
/* 133 */     T_BYTE(8, "byte"), 
/* 134 */     T_SHORT(9, "short"), 
/* 135 */     T_INT(10, "int"), 
/* 136 */     T_LONG(11, "long");
/*     */ 
/*     */     public final int value;
/*     */     public final String name;
/*     */ 
/* 138 */     private TypeKind(int paramInt, String paramString) { this.value = paramInt;
/* 139 */       this.name = paramString; }
/*     */ 
/*     */     public static TypeKind get(int paramInt)
/*     */     {
/* 143 */       switch (paramInt) { case 4:
/* 144 */         return T_BOOLEAN;
/*     */       case 5:
/* 145 */         return T_CHAR;
/*     */       case 6:
/* 146 */         return T_FLOAT;
/*     */       case 7:
/* 147 */         return T_DOUBLE;
/*     */       case 8:
/* 148 */         return T_BYTE;
/*     */       case 9:
/* 149 */         return T_SHORT;
/*     */       case 10:
/* 150 */         return T_INT;
/*     */       case 11:
/* 151 */         return T_LONG; }
/* 152 */       return null;
/*     */     }
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.classfile.Instruction
 * JD-Core Version:    0.6.2
 */