/*     */ package com.sun.tools.javac.code;
/*     */ 
/*     */ import com.sun.tools.javac.tree.JCTree.JCLambda;
/*     */ import com.sun.tools.javac.util.Assert;
/*     */ import com.sun.tools.javac.util.ListBuffer;
/*     */ import java.util.Iterator;
/*     */ 
/*     */ public class TypeAnnotationPosition
/*     */ {
/* 122 */   public TargetType type = TargetType.UNKNOWN;
/*     */ 
/* 125 */   public com.sun.tools.javac.util.List<TypePathEntry> location = com.sun.tools.javac.util.List.nil();
/*     */ 
/* 128 */   public int pos = -1;
/*     */ 
/* 132 */   public boolean isValidOffset = false;
/* 133 */   public int offset = -1;
/*     */ 
/* 136 */   public int[] lvarOffset = null;
/* 137 */   public int[] lvarLength = null;
/* 138 */   public int[] lvarIndex = null;
/*     */ 
/* 141 */   public int bound_index = -2147483648;
/*     */ 
/* 144 */   public int parameter_index = -2147483648;
/*     */ 
/* 147 */   public int type_index = -2147483648;
/*     */ 
/* 154 */   public int exception_index = -2147483648;
/*     */ 
/* 159 */   public JCTree.JCLambda onLambda = null;
/*     */ 
/*     */   public String toString()
/*     */   {
/* 165 */     StringBuilder localStringBuilder = new StringBuilder();
/* 166 */     localStringBuilder.append('[');
/* 167 */     localStringBuilder.append(this.type);
/*     */ 
/* 169 */     switch (1.$SwitchMap$com$sun$tools$javac$code$TargetType[this.type.ordinal()])
/*     */     {
/*     */     case 1:
/*     */     case 2:
/*     */     case 3:
/*     */     case 4:
/* 177 */       localStringBuilder.append(", offset = ");
/* 178 */       localStringBuilder.append(this.offset);
/* 179 */       break;
/*     */     case 5:
/*     */     case 6:
/* 184 */       if (this.lvarOffset == null) {
/* 185 */         localStringBuilder.append(", lvarOffset is null!");
/*     */       }
/*     */       else {
/* 188 */         localStringBuilder.append(", {");
/* 189 */         for (int i = 0; i < this.lvarOffset.length; i++) {
/* 190 */           if (i != 0) localStringBuilder.append("; ");
/* 191 */           localStringBuilder.append("start_pc = ");
/* 192 */           localStringBuilder.append(this.lvarOffset[i]);
/* 193 */           localStringBuilder.append(", length = ");
/* 194 */           localStringBuilder.append(this.lvarLength[i]);
/* 195 */           localStringBuilder.append(", index = ");
/* 196 */           localStringBuilder.append(this.lvarIndex[i]);
/*     */         }
/* 198 */         localStringBuilder.append("}");
/* 199 */       }break;
/*     */     case 7:
/* 203 */       break;
/*     */     case 8:
/*     */     case 9:
/* 207 */       localStringBuilder.append(", param_index = ");
/* 208 */       localStringBuilder.append(this.parameter_index);
/* 209 */       break;
/*     */     case 10:
/*     */     case 11:
/* 213 */       localStringBuilder.append(", param_index = ");
/* 214 */       localStringBuilder.append(this.parameter_index);
/* 215 */       localStringBuilder.append(", bound_index = ");
/* 216 */       localStringBuilder.append(this.bound_index);
/* 217 */       break;
/*     */     case 12:
/* 220 */       localStringBuilder.append(", type_index = ");
/* 221 */       localStringBuilder.append(this.type_index);
/* 222 */       break;
/*     */     case 13:
/* 225 */       localStringBuilder.append(", type_index = ");
/* 226 */       localStringBuilder.append(this.type_index);
/* 227 */       break;
/*     */     case 14:
/* 230 */       localStringBuilder.append(", exception_index = ");
/* 231 */       localStringBuilder.append(this.exception_index);
/* 232 */       break;
/*     */     case 15:
/* 235 */       localStringBuilder.append(", param_index = ");
/* 236 */       localStringBuilder.append(this.parameter_index);
/* 237 */       break;
/*     */     case 16:
/*     */     case 17:
/*     */     case 18:
/*     */     case 19:
/*     */     case 20:
/* 245 */       localStringBuilder.append(", offset = ");
/* 246 */       localStringBuilder.append(this.offset);
/* 247 */       localStringBuilder.append(", type_index = ");
/* 248 */       localStringBuilder.append(this.type_index);
/* 249 */       break;
/*     */     case 21:
/*     */     case 22:
/* 253 */       break;
/*     */     case 23:
/* 255 */       localStringBuilder.append(", position UNKNOWN!");
/* 256 */       break;
/*     */     default:
/* 258 */       Assert.error("Unknown target type: " + this.type);
/*     */     }
/*     */ 
/* 262 */     if (!this.location.isEmpty()) {
/* 263 */       localStringBuilder.append(", location = (");
/* 264 */       localStringBuilder.append(this.location);
/* 265 */       localStringBuilder.append(")");
/*     */     }
/*     */ 
/* 268 */     localStringBuilder.append(", pos = ");
/* 269 */     localStringBuilder.append(this.pos);
/*     */ 
/* 271 */     if (this.onLambda != null) {
/* 272 */       localStringBuilder.append(", onLambda hash = ");
/* 273 */       localStringBuilder.append(this.onLambda.hashCode());
/*     */     }
/*     */ 
/* 276 */     localStringBuilder.append(']');
/* 277 */     return localStringBuilder.toString();
/*     */   }
/*     */ 
/*     */   public boolean emitToClassfile()
/*     */   {
/* 286 */     return (!this.type.isLocal()) || (this.isValidOffset);
/*     */   }
/*     */ 
/*     */   public boolean matchesPos(int paramInt)
/*     */   {
/* 291 */     return this.pos == paramInt;
/*     */   }
/*     */ 
/*     */   public void updatePosOffset(int paramInt) {
/* 295 */     this.offset = paramInt;
/* 296 */     this.lvarOffset = new int[] { paramInt };
/* 297 */     this.isValidOffset = true;
/*     */   }
/*     */ 
/*     */   public static com.sun.tools.javac.util.List<TypePathEntry> getTypePathFromBinary(java.util.List<Integer> paramList)
/*     */   {
/* 307 */     ListBuffer localListBuffer = new ListBuffer();
/* 308 */     Iterator localIterator = paramList.iterator();
/* 309 */     while (localIterator.hasNext()) {
/* 310 */       Integer localInteger1 = (Integer)localIterator.next();
/* 311 */       Assert.check(localIterator.hasNext(), "Could not decode type path: " + paramList);
/* 312 */       Integer localInteger2 = (Integer)localIterator.next();
/* 313 */       localListBuffer = localListBuffer.append(TypePathEntry.fromBinary(localInteger1.intValue(), localInteger2.intValue()));
/*     */     }
/* 315 */     return localListBuffer.toList();
/*     */   }
/*     */ 
/*     */   public static com.sun.tools.javac.util.List<Integer> getBinaryFromTypePath(java.util.List<TypePathEntry> paramList) {
/* 319 */     ListBuffer localListBuffer = new ListBuffer();
/* 320 */     for (TypePathEntry localTypePathEntry : paramList) {
/* 321 */       localListBuffer = localListBuffer.append(Integer.valueOf(localTypePathEntry.tag.tag));
/* 322 */       localListBuffer = localListBuffer.append(Integer.valueOf(localTypePathEntry.arg));
/*     */     }
/* 324 */     return localListBuffer.toList();
/*     */   }
/*     */ 
/*     */   public static class TypePathEntry
/*     */   {
/*     */     public static final int bytesPerEntry = 2;
/*     */     public final TypeAnnotationPosition.TypePathEntryKind tag;
/*     */     public final int arg;
/*  63 */     public static final TypePathEntry ARRAY = new TypePathEntry(TypeAnnotationPosition.TypePathEntryKind.ARRAY);
/*  64 */     public static final TypePathEntry INNER_TYPE = new TypePathEntry(TypeAnnotationPosition.TypePathEntryKind.INNER_TYPE);
/*  65 */     public static final TypePathEntry WILDCARD = new TypePathEntry(TypeAnnotationPosition.TypePathEntryKind.WILDCARD);
/*     */ 
/*     */     private TypePathEntry(TypeAnnotationPosition.TypePathEntryKind paramTypePathEntryKind) {
/*  68 */       Assert.check((paramTypePathEntryKind == TypeAnnotationPosition.TypePathEntryKind.ARRAY) || (paramTypePathEntryKind == TypeAnnotationPosition.TypePathEntryKind.INNER_TYPE) || (paramTypePathEntryKind == TypeAnnotationPosition.TypePathEntryKind.WILDCARD), "Invalid TypePathEntryKind: " + paramTypePathEntryKind);
/*     */ 
/*  72 */       this.tag = paramTypePathEntryKind;
/*  73 */       this.arg = 0;
/*     */     }
/*     */ 
/*     */     public TypePathEntry(TypeAnnotationPosition.TypePathEntryKind paramTypePathEntryKind, int paramInt) {
/*  77 */       Assert.check(paramTypePathEntryKind == TypeAnnotationPosition.TypePathEntryKind.TYPE_ARGUMENT, "Invalid TypePathEntryKind: " + paramTypePathEntryKind);
/*     */ 
/*  79 */       this.tag = paramTypePathEntryKind;
/*  80 */       this.arg = paramInt;
/*     */     }
/*     */ 
/*     */     public static TypePathEntry fromBinary(int paramInt1, int paramInt2) {
/*  84 */       Assert.check((paramInt2 == 0) || (paramInt1 == TypeAnnotationPosition.TypePathEntryKind.TYPE_ARGUMENT.tag), "Invalid TypePathEntry tag/arg: " + paramInt1 + "/" + paramInt2);
/*     */ 
/*  86 */       switch (paramInt1) {
/*     */       case 0:
/*  88 */         return ARRAY;
/*     */       case 1:
/*  90 */         return INNER_TYPE;
/*     */       case 2:
/*  92 */         return WILDCARD;
/*     */       case 3:
/*  94 */         return new TypePathEntry(TypeAnnotationPosition.TypePathEntryKind.TYPE_ARGUMENT, paramInt2);
/*     */       }
/*  96 */       Assert.error("Invalid TypePathEntryKind tag: " + paramInt1);
/*  97 */       return null;
/*     */     }
/*     */ 
/*     */     public String toString()
/*     */     {
/* 103 */       return this.tag.toString() + (this.tag == TypeAnnotationPosition.TypePathEntryKind.TYPE_ARGUMENT ? "(" + this.arg + ")" : "");
/*     */     }
/*     */ 
/*     */     public boolean equals(Object paramObject)
/*     */     {
/* 109 */       if (!(paramObject instanceof TypePathEntry)) {
/* 110 */         return false;
/*     */       }
/* 112 */       TypePathEntry localTypePathEntry = (TypePathEntry)paramObject;
/* 113 */       return (this.tag == localTypePathEntry.tag) && (this.arg == localTypePathEntry.arg);
/*     */     }
/*     */ 
/*     */     public int hashCode()
/*     */     {
/* 118 */       return this.tag.hashCode() * 17 + this.arg;
/*     */     }
/*     */   }
/*     */ 
/*     */   public static enum TypePathEntryKind
/*     */   {
/*  44 */     ARRAY(0), 
/*  45 */     INNER_TYPE(1), 
/*  46 */     WILDCARD(2), 
/*  47 */     TYPE_ARGUMENT(3);
/*     */ 
/*     */     public final int tag;
/*     */ 
/*     */     private TypePathEntryKind(int paramInt) {
/*  52 */       this.tag = paramInt;
/*     */     }
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.javac.code.TypeAnnotationPosition
 * JD-Core Version:    0.6.2
 */