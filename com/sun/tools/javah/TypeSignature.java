/*     */ package com.sun.tools.javah;
/*     */ 
/*     */ import java.io.PrintStream;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import java.util.StringTokenizer;
/*     */ import javax.lang.model.element.Name;
/*     */ import javax.lang.model.element.TypeElement;
/*     */ import javax.lang.model.type.ArrayType;
/*     */ import javax.lang.model.type.DeclaredType;
/*     */ import javax.lang.model.type.NoType;
/*     */ import javax.lang.model.type.PrimitiveType;
/*     */ import javax.lang.model.type.TypeKind;
/*     */ import javax.lang.model.type.TypeMirror;
/*     */ import javax.lang.model.type.TypeVariable;
/*     */ import javax.lang.model.type.TypeVisitor;
/*     */ import javax.lang.model.util.Elements;
/*     */ import javax.lang.model.util.SimpleTypeVisitor8;
/*     */ 
/*     */ public class TypeSignature
/*     */ {
/*     */   Elements elems;
/*     */   private static final String SIG_VOID = "V";
/*     */   private static final String SIG_BOOLEAN = "Z";
/*     */   private static final String SIG_BYTE = "B";
/*     */   private static final String SIG_CHAR = "C";
/*     */   private static final String SIG_SHORT = "S";
/*     */   private static final String SIG_INT = "I";
/*     */   private static final String SIG_LONG = "J";
/*     */   private static final String SIG_FLOAT = "F";
/*     */   private static final String SIG_DOUBLE = "D";
/*     */   private static final String SIG_ARRAY = "[";
/*     */   private static final String SIG_CLASS = "L";
/*     */ 
/*     */   public TypeSignature(Elements paramElements)
/*     */   {
/*  81 */     this.elems = paramElements;
/*     */   }
/*     */ 
/*     */   public String getTypeSignature(String paramString)
/*     */     throws TypeSignature.SignatureException
/*     */   {
/*  88 */     return getParamJVMSignature(paramString);
/*     */   }
/*     */ 
/*     */   public String getTypeSignature(String paramString, TypeMirror paramTypeMirror)
/*     */     throws TypeSignature.SignatureException
/*     */   {
/*  96 */     String str1 = null;
/*  97 */     String str2 = null;
/*  98 */     ArrayList localArrayList = new ArrayList();
/*  99 */     String str3 = null;
/* 100 */     String str4 = null;
/* 101 */     String str5 = null;
/* 102 */     String str6 = null;
/* 103 */     int i = 0;
/*     */ 
/* 105 */     int j = -1;
/* 106 */     int k = -1;
/* 107 */     StringTokenizer localStringTokenizer = null;
/* 108 */     int m = 0;
/*     */ 
/* 111 */     if (paramString != null) {
/* 112 */       j = paramString.indexOf("(");
/* 113 */       k = paramString.indexOf(")");
/*     */     }
/*     */ 
/* 116 */     if ((j != -1) && (k != -1) && 
/* 117 */       (j + 1 < paramString
/* 117 */       .length()) && 
/* 118 */       (k < paramString
/* 118 */       .length())) {
/* 119 */       str1 = paramString.substring(j + 1, k);
/*     */     }
/*     */ 
/* 123 */     if (str1 != null) {
/* 124 */       if (str1.indexOf(",") != -1) {
/* 125 */         localStringTokenizer = new StringTokenizer(str1, ",");
/* 126 */         if (localStringTokenizer != null)
/* 127 */           while (localStringTokenizer.hasMoreTokens())
/* 128 */             localArrayList.add(localStringTokenizer.nextToken());
/*     */       }
/*     */       else
/*     */       {
/* 132 */         localArrayList.add(str1);
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 137 */     str2 = "(";
/*     */ 
/* 140 */     while (localArrayList.isEmpty() != true) {
/* 141 */       str3 = ((String)localArrayList.remove(m)).trim();
/* 142 */       str4 = getParamJVMSignature(str3);
/* 143 */       if (str4 != null) {
/* 144 */         str2 = str2 + str4;
/*     */       }
/*     */     }
/*     */ 
/* 148 */     str2 = str2 + ")";
/*     */ 
/* 152 */     str6 = "";
/* 153 */     if (paramTypeMirror != null) {
/* 154 */       i = dimensions(paramTypeMirror);
/*     */     }
/*     */ 
/* 158 */     while (i-- > 0) {
/* 159 */       str6 = str6 + "[";
/*     */     }
/* 161 */     if (paramTypeMirror != null) {
/* 162 */       str5 = qualifiedTypeName(paramTypeMirror);
/* 163 */       str6 = str6 + getComponentType(str5);
/*     */     } else {
/* 165 */       System.out.println("Invalid return type.");
/*     */     }
/*     */ 
/* 168 */     str2 = str2 + str6;
/*     */ 
/* 170 */     return str2;
/*     */   }
/*     */ 
/*     */   private String getParamJVMSignature(String paramString)
/*     */     throws TypeSignature.SignatureException
/*     */   {
/* 177 */     String str1 = "";
/* 178 */     String str2 = "";
/*     */ 
/* 180 */     if (paramString != null)
/*     */     {
/* 182 */       if (paramString.indexOf("[]") != -1)
/*     */       {
/* 184 */         int i = paramString.indexOf("[]");
/* 185 */         str2 = paramString.substring(0, i);
/* 186 */         String str3 = paramString.substring(i);
/* 187 */         if (str3 != null)
/* 188 */           while (str3.indexOf("[]") != -1) {
/* 189 */             str1 = str1 + "[";
/* 190 */             int j = str3.indexOf("]") + 1;
/* 191 */             if (j < str3.length())
/* 192 */               str3 = str3.substring(j);
/*     */             else
/* 194 */               str3 = "";
/*     */           }
/*     */       } else {
/* 197 */         str2 = paramString;
/*     */       }
/* 199 */       str1 = str1 + getComponentType(str2);
/*     */     }
/* 201 */     return str1;
/*     */   }
/*     */ 
/*     */   private String getComponentType(String paramString)
/*     */     throws TypeSignature.SignatureException
/*     */   {
/* 209 */     String str1 = "";
/*     */ 
/* 211 */     if (paramString != null) {
/* 212 */       if (paramString.equals("void")) { str1 = str1 + "V";
/* 213 */       } else if (paramString.equals("boolean")) { str1 = str1 + "Z";
/* 214 */       } else if (paramString.equals("byte")) { str1 = str1 + "B";
/* 215 */       } else if (paramString.equals("char")) { str1 = str1 + "C";
/* 216 */       } else if (paramString.equals("short")) { str1 = str1 + "S";
/* 217 */       } else if (paramString.equals("int")) { str1 = str1 + "I";
/* 218 */       } else if (paramString.equals("long")) { str1 = str1 + "J";
/* 219 */       } else if (paramString.equals("float")) { str1 = str1 + "F";
/* 220 */       } else if (paramString.equals("double")) { str1 = str1 + "D"; }
/* 222 */       else if (!paramString.equals("")) {
/* 223 */         TypeElement localTypeElement = this.elems.getTypeElement(paramString);
/*     */ 
/* 225 */         if (localTypeElement == null) {
/* 226 */           throw new SignatureException(paramString);
/*     */         }
/* 228 */         String str2 = localTypeElement.getQualifiedName().toString();
/* 229 */         String str3 = str2.replace('.', '/');
/* 230 */         str1 = str1 + "L";
/* 231 */         str1 = str1 + str3;
/* 232 */         str1 = str1 + ";";
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 237 */     return str1;
/*     */   }
/*     */ 
/*     */   int dimensions(TypeMirror paramTypeMirror) {
/* 241 */     if (paramTypeMirror.getKind() != TypeKind.ARRAY)
/* 242 */       return 0;
/* 243 */     return 1 + dimensions(((ArrayType)paramTypeMirror).getComponentType());
/*     */   }
/*     */ 
/*     */   String qualifiedTypeName(TypeMirror paramTypeMirror)
/*     */   {
/* 248 */     SimpleTypeVisitor8 local1 = new SimpleTypeVisitor8()
/*     */     {
/*     */       public Name visitArray(ArrayType paramAnonymousArrayType, Void paramAnonymousVoid) {
/* 251 */         return (Name)paramAnonymousArrayType.getComponentType().accept(this, paramAnonymousVoid);
/*     */       }
/*     */ 
/*     */       public Name visitDeclared(DeclaredType paramAnonymousDeclaredType, Void paramAnonymousVoid)
/*     */       {
/* 256 */         return ((TypeElement)paramAnonymousDeclaredType.asElement()).getQualifiedName();
/*     */       }
/*     */ 
/*     */       public Name visitPrimitive(PrimitiveType paramAnonymousPrimitiveType, Void paramAnonymousVoid)
/*     */       {
/* 261 */         return TypeSignature.this.elems.getName(paramAnonymousPrimitiveType.toString());
/*     */       }
/*     */ 
/*     */       public Name visitNoType(NoType paramAnonymousNoType, Void paramAnonymousVoid)
/*     */       {
/* 266 */         if (paramAnonymousNoType.getKind() == TypeKind.VOID)
/* 267 */           return TypeSignature.this.elems.getName("void");
/* 268 */         return (Name)defaultAction(paramAnonymousNoType, paramAnonymousVoid);
/*     */       }
/*     */ 
/*     */       public Name visitTypeVariable(TypeVariable paramAnonymousTypeVariable, Void paramAnonymousVoid)
/*     */       {
/* 273 */         return (Name)paramAnonymousTypeVariable.getUpperBound().accept(this, paramAnonymousVoid);
/*     */       }
/*     */     };
/* 276 */     return ((Name)local1.visit(paramTypeMirror)).toString();
/*     */   }
/*     */ 
/*     */   static class SignatureException extends Exception
/*     */   {
/*     */     private static final long serialVersionUID = 1L;
/*     */ 
/*     */     SignatureException(String paramString)
/*     */     {
/*  58 */       super();
/*     */     }
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.javah.TypeSignature
 * JD-Core Version:    0.6.2
 */