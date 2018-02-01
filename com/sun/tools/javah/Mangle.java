/*     */ package com.sun.tools.javah;
/*     */ 
/*     */ import javax.lang.model.element.ExecutableElement;
/*     */ import javax.lang.model.element.TypeElement;
/*     */ import javax.lang.model.element.VariableElement;
/*     */ import javax.lang.model.type.TypeMirror;
/*     */ import javax.lang.model.util.Elements;
/*     */ import javax.lang.model.util.Types;
/*     */ 
/*     */ public class Mangle
/*     */ {
/*     */   private Elements elems;
/*     */   private Types types;
/*     */ 
/*     */   Mangle(Elements paramElements, Types paramTypes)
/*     */   {
/*  64 */     this.elems = paramElements;
/*  65 */     this.types = paramTypes;
/*     */   }
/*     */ 
/*     */   public final String mangle(CharSequence paramCharSequence, int paramInt) {
/*  69 */     StringBuilder localStringBuilder = new StringBuilder(100);
/*  70 */     int i = paramCharSequence.length();
/*     */ 
/*  72 */     for (int j = 0; j < i; j++) {
/*  73 */       char c = paramCharSequence.charAt(j);
/*  74 */       if (isalnum(c)) {
/*  75 */         localStringBuilder.append(c);
/*  76 */       } else if ((c == '.') && (paramInt == 1))
/*     */       {
/*  78 */         localStringBuilder.append('_');
/*  79 */       } else if ((c == '$') && (paramInt == 1))
/*     */       {
/*  81 */         localStringBuilder.append('_');
/*  82 */         localStringBuilder.append('_');
/*  83 */       } else if ((c == '_') && (paramInt == 2)) {
/*  84 */         localStringBuilder.append('_');
/*  85 */       } else if ((c == '_') && (paramInt == 1)) {
/*  86 */         localStringBuilder.append('_');
/*  87 */       } else if (paramInt == 4) {
/*  88 */         String str = null;
/*  89 */         if (c == '_')
/*  90 */           str = "_1";
/*  91 */         else if (c == '.')
/*  92 */           str = "_";
/*  93 */         else if (c == ';')
/*  94 */           str = "_2";
/*  95 */         else if (c == '[')
/*  96 */           str = "_3";
/*  97 */         if (str != null)
/*  98 */           localStringBuilder.append(str);
/*     */         else
/* 100 */           localStringBuilder.append(mangleChar(c));
/*     */       }
/* 102 */       else if (paramInt == 5) {
/* 103 */         if (isprint(c))
/* 104 */           localStringBuilder.append(c);
/*     */         else
/* 106 */           localStringBuilder.append(mangleChar(c));
/*     */       }
/*     */       else {
/* 109 */         localStringBuilder.append(mangleChar(c));
/*     */       }
/*     */     }
/*     */ 
/* 113 */     return localStringBuilder.toString();
/*     */   }
/*     */ 
/*     */   public String mangleMethod(ExecutableElement paramExecutableElement, TypeElement paramTypeElement, int paramInt) throws TypeSignature.SignatureException
/*     */   {
/* 118 */     StringBuilder localStringBuilder = new StringBuilder(100);
/* 119 */     localStringBuilder.append("Java_");
/*     */ 
/* 121 */     if (paramInt == 6) {
/* 122 */       localStringBuilder.append(mangle(paramTypeElement.getQualifiedName(), 1));
/* 123 */       localStringBuilder.append('_');
/* 124 */       localStringBuilder.append(mangle(paramExecutableElement.getSimpleName(), 3));
/*     */ 
/* 126 */       localStringBuilder.append("_stub");
/* 127 */       return localStringBuilder.toString();
/*     */     }
/*     */ 
/* 131 */     localStringBuilder.append(mangle(getInnerQualifiedName(paramTypeElement), 4));
/* 132 */     localStringBuilder.append('_');
/* 133 */     localStringBuilder.append(mangle(paramExecutableElement.getSimpleName(), 4));
/*     */ 
/* 135 */     if (paramInt == 8) {
/* 136 */       localStringBuilder.append("__");
/* 137 */       String str1 = signature(paramExecutableElement);
/* 138 */       TypeSignature localTypeSignature = new TypeSignature(this.elems);
/* 139 */       String str2 = localTypeSignature.getTypeSignature(str1, paramExecutableElement.getReturnType());
/* 140 */       str2 = str2.substring(1);
/* 141 */       str2 = str2.substring(0, str2.lastIndexOf(')'));
/* 142 */       str2 = str2.replace('/', '.');
/* 143 */       localStringBuilder.append(mangle(str2, 4));
/*     */     }
/*     */ 
/* 146 */     return localStringBuilder.toString();
/*     */   }
/*     */ 
/*     */   private String getInnerQualifiedName(TypeElement paramTypeElement) {
/* 150 */     return this.elems.getBinaryName(paramTypeElement).toString();
/*     */   }
/*     */ 
/*     */   public final String mangleChar(char paramChar) {
/* 154 */     String str = Integer.toHexString(paramChar);
/* 155 */     int i = 5 - str.length();
/* 156 */     char[] arrayOfChar = new char[6];
/* 157 */     arrayOfChar[0] = '_';
/* 158 */     for (int j = 1; j <= i; j++)
/* 159 */       arrayOfChar[j] = '0';
/* 160 */     j = i + 1; for (int k = 0; j < 6; k++) {
/* 161 */       arrayOfChar[j] = str.charAt(k);
/*     */ 
/* 160 */       j++;
/*     */     }
/* 162 */     return new String(arrayOfChar);
/*     */   }
/*     */ 
/*     */   private String signature(ExecutableElement paramExecutableElement)
/*     */   {
/* 167 */     StringBuilder localStringBuilder = new StringBuilder();
/* 168 */     String str = "(";
/* 169 */     for (VariableElement localVariableElement : paramExecutableElement.getParameters()) {
/* 170 */       localStringBuilder.append(str);
/* 171 */       localStringBuilder.append(this.types.erasure(localVariableElement.asType()).toString());
/* 172 */       str = ",";
/*     */     }
/* 174 */     localStringBuilder.append(")");
/* 175 */     return localStringBuilder.toString();
/*     */   }
/*     */ 
/*     */   private static final boolean isalnum(char paramChar)
/*     */   {
/* 180 */     return (paramChar <= '') && (((paramChar >= 'A') && (paramChar <= 'Z')) || ((paramChar >= 'a') && (paramChar <= 'z')) || ((paramChar >= '0') && (paramChar <= '9')));
/*     */   }
/*     */ 
/*     */   private static final boolean isprint(char paramChar)
/*     */   {
/* 188 */     return (paramChar >= ' ') && (paramChar <= '~');
/*     */   }
/*     */ 
/*     */   public static class Type
/*     */   {
/*     */     public static final int CLASS = 1;
/*     */     public static final int FIELDSTUB = 2;
/*     */     public static final int FIELD = 3;
/*     */     public static final int JNI = 4;
/*     */     public static final int SIGNATURE = 5;
/*     */     public static final int METHOD_JDK_1 = 6;
/*     */     public static final int METHOD_JNI_SHORT = 7;
/*     */     public static final int METHOD_JNI_LONG = 8;
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.javah.Mangle
 * JD-Core Version:    0.6.2
 */