/*     */ package com.sun.tools.javac.code;
/*     */ 
/*     */ import com.sun.source.tree.Tree.Kind;
/*     */ import javax.lang.model.type.TypeKind;
/*     */ 
/*     */ public enum TypeTag
/*     */ {
/*  45 */   BYTE(1, 125, true), 
/*     */ 
/*  49 */   CHAR(2, 122, true), 
/*     */ 
/*  53 */   SHORT(4, 124, true), 
/*     */ 
/*  57 */   LONG(16, 112, true), 
/*     */ 
/*  61 */   FLOAT(32, 96, true), 
/*     */ 
/*  64 */   INT(8, 120, true), 
/*     */ 
/*  67 */   DOUBLE(64, 64, true), 
/*     */ 
/*  70 */   BOOLEAN(0, 0, true), 
/*     */ 
/*  74 */   VOID, 
/*     */ 
/*  78 */   CLASS, 
/*     */ 
/*  82 */   ARRAY, 
/*     */ 
/*  86 */   METHOD, 
/*     */ 
/*  90 */   PACKAGE, 
/*     */ 
/*  94 */   TYPEVAR, 
/*     */ 
/*  98 */   WILDCARD, 
/*     */ 
/* 102 */   FORALL, 
/*     */ 
/* 106 */   DEFERRED, 
/*     */ 
/* 110 */   BOT, 
/*     */ 
/* 114 */   NONE, 
/*     */ 
/* 118 */   ERROR, 
/*     */ 
/* 122 */   UNKNOWN, 
/*     */ 
/* 126 */   UNDETVAR, 
/*     */ 
/* 130 */   UNINITIALIZED_THIS, 
/*     */ 
/* 132 */   UNINITIALIZED_OBJECT;
/*     */ 
/*     */   final int superClasses;
/*     */   final int numericClass;
/*     */   final boolean isPrimitive;
/*     */ 
/* 139 */   private TypeTag() { this(0, 0, false); }
/*     */ 
/*     */   private TypeTag(int paramInt1, int paramInt2, boolean paramBoolean)
/*     */   {
/* 143 */     this.superClasses = paramInt2;
/* 144 */     this.numericClass = paramInt1;
/* 145 */     this.isPrimitive = paramBoolean;
/*     */   }
/*     */ 
/*     */   public boolean isStrictSubRangeOf(TypeTag paramTypeTag)
/*     */   {
/* 178 */     return ((this.superClasses & paramTypeTag.numericClass) != 0) && (this != paramTypeTag);
/*     */   }
/*     */ 
/*     */   public boolean isSubRangeOf(TypeTag paramTypeTag) {
/* 182 */     return (this.superClasses & paramTypeTag.numericClass) != 0;
/*     */   }
/*     */ 
/*     */   public static int getTypeTagCount()
/*     */   {
/* 189 */     return UNDETVAR.ordinal() + 1;
/*     */   }
/*     */ 
/*     */   public Tree.Kind getKindLiteral() {
/* 193 */     switch (1.$SwitchMap$com$sun$tools$javac$code$TypeTag[ordinal()]) {
/*     */     case 1:
/* 195 */       return Tree.Kind.INT_LITERAL;
/*     */     case 2:
/* 197 */       return Tree.Kind.LONG_LITERAL;
/*     */     case 3:
/* 199 */       return Tree.Kind.FLOAT_LITERAL;
/*     */     case 4:
/* 201 */       return Tree.Kind.DOUBLE_LITERAL;
/*     */     case 5:
/* 203 */       return Tree.Kind.BOOLEAN_LITERAL;
/*     */     case 6:
/* 205 */       return Tree.Kind.CHAR_LITERAL;
/*     */     case 7:
/* 207 */       return Tree.Kind.STRING_LITERAL;
/*     */     case 8:
/* 209 */       return Tree.Kind.NULL_LITERAL;
/*     */     }
/* 211 */     throw new AssertionError("unknown literal kind " + this);
/*     */   }
/*     */ 
/*     */   public TypeKind getPrimitiveTypeKind()
/*     */   {
/* 216 */     switch (1.$SwitchMap$com$sun$tools$javac$code$TypeTag[ordinal()]) {
/*     */     case 5:
/* 218 */       return TypeKind.BOOLEAN;
/*     */     case 9:
/* 220 */       return TypeKind.BYTE;
/*     */     case 10:
/* 222 */       return TypeKind.SHORT;
/*     */     case 1:
/* 224 */       return TypeKind.INT;
/*     */     case 2:
/* 226 */       return TypeKind.LONG;
/*     */     case 6:
/* 228 */       return TypeKind.CHAR;
/*     */     case 3:
/* 230 */       return TypeKind.FLOAT;
/*     */     case 4:
/* 232 */       return TypeKind.DOUBLE;
/*     */     case 11:
/* 234 */       return TypeKind.VOID;
/*     */     case 7:
/* 236 */     case 8: } throw new AssertionError("unknown primitive type " + this);
/*     */   }
/*     */ 
/*     */   public static class NumericClasses
/*     */   {
/*     */     public static final int BYTE_CLASS = 1;
/*     */     public static final int CHAR_CLASS = 2;
/*     */     public static final int SHORT_CLASS = 4;
/*     */     public static final int INT_CLASS = 8;
/*     */     public static final int LONG_CLASS = 16;
/*     */     public static final int FLOAT_CLASS = 32;
/*     */     public static final int DOUBLE_CLASS = 64;
/*     */     static final int BYTE_SUPERCLASSES = 125;
/*     */     static final int CHAR_SUPERCLASSES = 122;
/*     */     static final int SHORT_SUPERCLASSES = 124;
/*     */     static final int INT_SUPERCLASSES = 120;
/*     */     static final int LONG_SUPERCLASSES = 112;
/*     */     static final int FLOAT_SUPERCLASSES = 96;
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.javac.code.TypeTag
 * JD-Core Version:    0.6.2
 */