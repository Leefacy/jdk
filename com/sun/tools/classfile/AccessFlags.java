/*     */ package com.sun.tools.classfile;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.util.LinkedHashSet;
/*     */ import java.util.Set;
/*     */ 
/*     */ public class AccessFlags
/*     */ {
/*     */   public static final int ACC_PUBLIC = 1;
/*     */   public static final int ACC_PRIVATE = 2;
/*     */   public static final int ACC_PROTECTED = 4;
/*     */   public static final int ACC_STATIC = 8;
/*     */   public static final int ACC_FINAL = 16;
/*     */   public static final int ACC_SUPER = 32;
/*     */   public static final int ACC_SYNCHRONIZED = 32;
/*     */   public static final int ACC_VOLATILE = 64;
/*     */   public static final int ACC_BRIDGE = 64;
/*     */   public static final int ACC_TRANSIENT = 128;
/*     */   public static final int ACC_VARARGS = 128;
/*     */   public static final int ACC_NATIVE = 256;
/*     */   public static final int ACC_INTERFACE = 512;
/*     */   public static final int ACC_ABSTRACT = 1024;
/*     */   public static final int ACC_STRICT = 2048;
/*     */   public static final int ACC_SYNTHETIC = 4096;
/*     */   public static final int ACC_ANNOTATION = 8192;
/*     */   public static final int ACC_ENUM = 16384;
/*     */   public static final int ACC_MANDATED = 32768;
/*  83 */   private static final int[] classModifiers = { 1, 16, 1024 };
/*     */ 
/*  87 */   private static final int[] classFlags = { 1, 16, 32, 512, 1024, 4096, 8192, 16384 };
/*     */ 
/* 101 */   private static final int[] innerClassModifiers = { 1, 2, 4, 8, 16, 1024 };
/*     */ 
/* 106 */   private static final int[] innerClassFlags = { 1, 2, 4, 8, 16, 32, 512, 1024, 4096, 8192, 16384 };
/*     */ 
/* 120 */   private static final int[] fieldModifiers = { 1, 2, 4, 8, 16, 64, 128 };
/*     */ 
/* 125 */   private static final int[] fieldFlags = { 1, 2, 4, 8, 16, 64, 128, 4096, 16384 };
/*     */ 
/* 138 */   private static final int[] methodModifiers = { 1, 2, 4, 8, 16, 32, 256, 1024, 2048 };
/*     */ 
/* 143 */   private static final int[] methodFlags = { 1, 2, 4, 8, 16, 32, 64, 128, 256, 1024, 2048, 4096 };
/*     */   public final int flags;
/*     */ 
/*     */   AccessFlags(ClassReader paramClassReader)
/*     */     throws IOException
/*     */   {
/*  64 */     this(paramClassReader.readUnsignedShort());
/*     */   }
/*     */ 
/*     */   public AccessFlags(int paramInt) {
/*  68 */     this.flags = paramInt;
/*     */   }
/*     */ 
/*     */   public AccessFlags ignore(int paramInt) {
/*  72 */     return new AccessFlags(this.flags & (paramInt ^ 0xFFFFFFFF));
/*     */   }
/*     */ 
/*     */   public boolean is(int paramInt) {
/*  76 */     return (this.flags & paramInt) != 0;
/*     */   }
/*     */ 
/*     */   public int byteLength() {
/*  80 */     return 2;
/*     */   }
/*     */ 
/*     */   public Set<String> getClassModifiers()
/*     */   {
/*  93 */     int i = (this.flags & 0x200) != 0 ? this.flags & 0xFFFFFBFF : this.flags;
/*  94 */     return getModifiers(i, classModifiers, Kind.Class);
/*     */   }
/*     */ 
/*     */   public Set<String> getClassFlags() {
/*  98 */     return getFlags(classFlags, Kind.Class);
/*     */   }
/*     */ 
/*     */   public Set<String> getInnerClassModifiers()
/*     */   {
/* 112 */     int i = (this.flags & 0x200) != 0 ? this.flags & 0xFFFFFBFF : this.flags;
/* 113 */     return getModifiers(i, innerClassModifiers, Kind.InnerClass);
/*     */   }
/*     */ 
/*     */   public Set<String> getInnerClassFlags() {
/* 117 */     return getFlags(innerClassFlags, Kind.InnerClass);
/*     */   }
/*     */ 
/*     */   public Set<String> getFieldModifiers()
/*     */   {
/* 131 */     return getModifiers(fieldModifiers, Kind.Field);
/*     */   }
/*     */ 
/*     */   public Set<String> getFieldFlags() {
/* 135 */     return getFlags(fieldFlags, Kind.Field);
/*     */   }
/*     */ 
/*     */   public Set<String> getMethodModifiers()
/*     */   {
/* 150 */     return getModifiers(methodModifiers, Kind.Method);
/*     */   }
/*     */ 
/*     */   public Set<String> getMethodFlags() {
/* 154 */     return getFlags(methodFlags, Kind.Method);
/*     */   }
/*     */ 
/*     */   private Set<String> getModifiers(int[] paramArrayOfInt, Kind paramKind) {
/* 158 */     return getModifiers(this.flags, paramArrayOfInt, paramKind);
/*     */   }
/*     */ 
/*     */   private static Set<String> getModifiers(int paramInt, int[] paramArrayOfInt, Kind paramKind) {
/* 162 */     LinkedHashSet localLinkedHashSet = new LinkedHashSet();
/* 163 */     for (int k : paramArrayOfInt) {
/* 164 */       if ((paramInt & k) != 0)
/* 165 */         localLinkedHashSet.add(flagToModifier(k, paramKind));
/*     */     }
/* 167 */     return localLinkedHashSet;
/*     */   }
/*     */ 
/*     */   private Set<String> getFlags(int[] paramArrayOfInt, Kind paramKind) {
/* 171 */     LinkedHashSet localLinkedHashSet = new LinkedHashSet();
/* 172 */     int i = this.flags;
/* 173 */     for (int n : paramArrayOfInt) {
/* 174 */       if ((i & n) != 0) {
/* 175 */         localLinkedHashSet.add(flagToName(n, paramKind));
/* 176 */         i &= (n ^ 0xFFFFFFFF);
/*     */       }
/*     */     }
/* 179 */     while (i != 0) {
/* 180 */       int j = Integer.highestOneBit(i);
/* 181 */       localLinkedHashSet.add("0x" + Integer.toHexString(j));
/* 182 */       i &= (j ^ 0xFFFFFFFF);
/*     */     }
/* 184 */     return localLinkedHashSet;
/*     */   }
/*     */ 
/*     */   private static String flagToModifier(int paramInt, Kind paramKind) {
/* 188 */     switch (paramInt) {
/*     */     case 1:
/* 190 */       return "public";
/*     */     case 2:
/* 192 */       return "private";
/*     */     case 4:
/* 194 */       return "protected";
/*     */     case 8:
/* 196 */       return "static";
/*     */     case 16:
/* 198 */       return "final";
/*     */     case 32:
/* 200 */       return "synchronized";
/*     */     case 128:
/* 202 */       return paramKind == Kind.Field ? "transient" : null;
/*     */     case 64:
/* 204 */       return "volatile";
/*     */     case 256:
/* 206 */       return "native";
/*     */     case 1024:
/* 208 */       return "abstract";
/*     */     case 2048:
/* 210 */       return "strictfp";
/*     */     case 32768:
/* 212 */       return "mandated";
/*     */     }
/* 214 */     return null;
/*     */   }
/*     */ 
/*     */   private static String flagToName(int paramInt, Kind paramKind)
/*     */   {
/* 219 */     switch (paramInt) {
/*     */     case 1:
/* 221 */       return "ACC_PUBLIC";
/*     */     case 2:
/* 223 */       return "ACC_PRIVATE";
/*     */     case 4:
/* 225 */       return "ACC_PROTECTED";
/*     */     case 8:
/* 227 */       return "ACC_STATIC";
/*     */     case 16:
/* 229 */       return "ACC_FINAL";
/*     */     case 32:
/* 231 */       return paramKind == Kind.Class ? "ACC_SUPER" : "ACC_SYNCHRONIZED";
/*     */     case 64:
/* 233 */       return paramKind == Kind.Field ? "ACC_VOLATILE" : "ACC_BRIDGE";
/*     */     case 128:
/* 235 */       return paramKind == Kind.Field ? "ACC_TRANSIENT" : "ACC_VARARGS";
/*     */     case 256:
/* 237 */       return "ACC_NATIVE";
/*     */     case 512:
/* 239 */       return "ACC_INTERFACE";
/*     */     case 1024:
/* 241 */       return "ACC_ABSTRACT";
/*     */     case 2048:
/* 243 */       return "ACC_STRICT";
/*     */     case 4096:
/* 245 */       return "ACC_SYNTHETIC";
/*     */     case 8192:
/* 247 */       return "ACC_ANNOTATION";
/*     */     case 16384:
/* 249 */       return "ACC_ENUM";
/*     */     case 32768:
/* 251 */       return "ACC_MANDATED";
/*     */     }
/* 253 */     return null;
/*     */   }
/*     */ 
/*     */   public static enum Kind
/*     */   {
/*  61 */     Class, InnerClass, Field, Method;
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.classfile.AccessFlags
 * JD-Core Version:    0.6.2
 */