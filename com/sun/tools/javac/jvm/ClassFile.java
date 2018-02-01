/*     */ package com.sun.tools.javac.jvm;
/*     */ 
/*     */ import com.sun.tools.javac.code.Type;
/*     */ import com.sun.tools.javac.code.Types;
/*     */ import com.sun.tools.javac.code.Types.UniqueType;
/*     */ import com.sun.tools.javac.util.Name;
/*     */ 
/*     */ public class ClassFile
/*     */ {
/*     */   public static final int JAVA_MAGIC = -889275714;
/*     */   public static final int CONSTANT_Utf8 = 1;
/*     */   public static final int CONSTANT_Unicode = 2;
/*     */   public static final int CONSTANT_Integer = 3;
/*     */   public static final int CONSTANT_Float = 4;
/*     */   public static final int CONSTANT_Long = 5;
/*     */   public static final int CONSTANT_Double = 6;
/*     */   public static final int CONSTANT_Class = 7;
/*     */   public static final int CONSTANT_String = 8;
/*     */   public static final int CONSTANT_Fieldref = 9;
/*     */   public static final int CONSTANT_Methodref = 10;
/*     */   public static final int CONSTANT_InterfaceMethodref = 11;
/*     */   public static final int CONSTANT_NameandType = 12;
/*     */   public static final int CONSTANT_MethodHandle = 15;
/*     */   public static final int CONSTANT_MethodType = 16;
/*     */   public static final int CONSTANT_InvokeDynamic = 18;
/*     */   public static final int REF_getField = 1;
/*     */   public static final int REF_getStatic = 2;
/*     */   public static final int REF_putField = 3;
/*     */   public static final int REF_putStatic = 4;
/*     */   public static final int REF_invokeVirtual = 5;
/*     */   public static final int REF_invokeStatic = 6;
/*     */   public static final int REF_invokeSpecial = 7;
/*     */   public static final int REF_newInvokeSpecial = 8;
/*     */   public static final int REF_invokeInterface = 9;
/*     */   public static final int MAX_PARAMETERS = 255;
/*     */   public static final int MAX_DIMENSIONS = 255;
/*     */   public static final int MAX_CODE = 65535;
/*     */   public static final int MAX_LOCALS = 65535;
/*     */   public static final int MAX_STACK = 65535;
/*     */ 
/*     */   public static byte[] internalize(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
/*     */   {
/* 127 */     byte[] arrayOfByte = new byte[paramInt2];
/* 128 */     for (int i = 0; i < paramInt2; i++) {
/* 129 */       int j = paramArrayOfByte[(paramInt1 + i)];
/* 130 */       if (j == 47) arrayOfByte[i] = 46; else
/* 131 */         arrayOfByte[i] = j;
/*     */     }
/* 133 */     return arrayOfByte;
/*     */   }
/*     */ 
/*     */   public static byte[] internalize(Name paramName)
/*     */   {
/* 140 */     return internalize(paramName.getByteArray(), paramName.getByteOffset(), paramName.getByteLength());
/*     */   }
/*     */ 
/*     */   public static byte[] externalize(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
/*     */   {
/* 147 */     byte[] arrayOfByte = new byte[paramInt2];
/* 148 */     for (int i = 0; i < paramInt2; i++) {
/* 149 */       int j = paramArrayOfByte[(paramInt1 + i)];
/* 150 */       if (j == 46) arrayOfByte[i] = 47; else
/* 151 */         arrayOfByte[i] = j;
/*     */     }
/* 153 */     return arrayOfByte;
/*     */   }
/*     */ 
/*     */   public static byte[] externalize(Name paramName)
/*     */   {
/* 160 */     return externalize(paramName.getByteArray(), paramName.getByteOffset(), paramName.getByteLength());
/*     */   }
/*     */ 
/*     */   public static class NameAndType
/*     */   {
/*     */     Name name;
/*     */     Types.UniqueType uniqueType;
/*     */     Types types;
/*     */ 
/*     */     NameAndType(Name paramName, Type paramType, Types paramTypes)
/*     */     {
/* 175 */       this.name = paramName;
/* 176 */       this.uniqueType = new Types.UniqueType(paramType, paramTypes);
/* 177 */       this.types = paramTypes;
/*     */     }
/*     */ 
/*     */     void setType(Type paramType) {
/* 181 */       this.uniqueType = new Types.UniqueType(paramType, this.types);
/*     */     }
/*     */ 
/*     */     public boolean equals(Object paramObject)
/*     */     {
/* 186 */       if (((paramObject instanceof NameAndType)) && (this.name == ((NameAndType)paramObject).name));
/* 188 */       return this.uniqueType
/* 188 */         .equals(((NameAndType)paramObject).uniqueType);
/*     */     }
/*     */ 
/*     */     public int hashCode()
/*     */     {
/* 193 */       return this.name.hashCode() * this.uniqueType.hashCode();
/*     */     }
/*     */   }
/*     */ 
/*     */   public static enum Version
/*     */   {
/* 106 */     V45_3(45, 3), 
/* 107 */     V49(49, 0), 
/* 108 */     V50(50, 0), 
/* 109 */     V51(51, 0), 
/* 110 */     V52(52, 0);
/*     */ 
/*     */     public final int major;
/*     */     public final int minor;
/*     */ 
/* 112 */     private Version(int paramInt1, int paramInt2) { this.major = paramInt1;
/* 113 */       this.minor = paramInt2;
/*     */     }
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.javac.jvm.ClassFile
 * JD-Core Version:    0.6.2
 */