/*     */ package com.sun.tools.jdi;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ 
/*     */ public class JNITypeParser
/*     */ {
/*     */   static final char SIGNATURE_ENDCLASS = ';';
/*     */   static final char SIGNATURE_FUNC = '(';
/*     */   static final char SIGNATURE_ENDFUNC = ')';
/*     */   private String signature;
/*     */   private List<String> typeNameList;
/*     */   private List<String> signatureList;
/*     */   private int currentIndex;
/*     */ 
/*     */   JNITypeParser(String paramString)
/*     */   {
/*  43 */     this.signature = paramString;
/*     */   }
/*     */ 
/*     */   static String typeNameToSignature(String paramString) {
/*  47 */     StringBuffer localStringBuffer = new StringBuffer();
/*  48 */     int i = paramString.indexOf('[');
/*  49 */     int j = i;
/*  50 */     while (j != -1) {
/*  51 */       localStringBuffer.append('[');
/*  52 */       j = paramString.indexOf('[', j + 1);
/*     */     }
/*     */ 
/*  55 */     if (i != -1) {
/*  56 */       paramString = paramString.substring(0, i);
/*     */     }
/*     */ 
/*  59 */     if (paramString.equals("boolean")) {
/*  60 */       localStringBuffer.append('Z');
/*  61 */     } else if (paramString.equals("byte")) {
/*  62 */       localStringBuffer.append('B');
/*  63 */     } else if (paramString.equals("char")) {
/*  64 */       localStringBuffer.append('C');
/*  65 */     } else if (paramString.equals("short")) {
/*  66 */       localStringBuffer.append('S');
/*  67 */     } else if (paramString.equals("int")) {
/*  68 */       localStringBuffer.append('I');
/*  69 */     } else if (paramString.equals("long")) {
/*  70 */       localStringBuffer.append('J');
/*  71 */     } else if (paramString.equals("float")) {
/*  72 */       localStringBuffer.append('F');
/*  73 */     } else if (paramString.equals("double")) {
/*  74 */       localStringBuffer.append('D');
/*     */     } else {
/*  76 */       localStringBuffer.append('L');
/*  77 */       localStringBuffer.append(paramString.replace('.', '/'));
/*  78 */       localStringBuffer.append(';');
/*     */     }
/*     */ 
/*  81 */     return localStringBuffer.toString();
/*     */   }
/*     */ 
/*     */   String typeName() {
/*  85 */     return (String)typeNameList().get(typeNameList().size() - 1);
/*     */   }
/*     */ 
/*     */   List<String> argumentTypeNames() {
/*  89 */     return typeNameList().subList(0, typeNameList().size() - 1);
/*     */   }
/*     */ 
/*     */   String signature() {
/*  93 */     return (String)signatureList().get(signatureList().size() - 1);
/*     */   }
/*     */ 
/*     */   List<String> argumentSignatures() {
/*  97 */     return signatureList().subList(0, signatureList().size() - 1);
/*     */   }
/*     */ 
/*     */   int dimensionCount() {
/* 101 */     int i = 0;
/* 102 */     String str = signature();
/* 103 */     while (str.charAt(i) == '[') {
/* 104 */       i++;
/*     */     }
/* 106 */     return i;
/*     */   }
/*     */ 
/*     */   String componentSignature(int paramInt) {
/* 110 */     return signature().substring(paramInt);
/*     */   }
/*     */ 
/*     */   private synchronized List<String> signatureList() {
/* 114 */     if (this.signatureList == null) {
/* 115 */       this.signatureList = new ArrayList(10);
/*     */ 
/* 118 */       this.currentIndex = 0;
/*     */ 
/* 120 */       while (this.currentIndex < this.signature.length()) {
/* 121 */         String str = nextSignature();
/* 122 */         this.signatureList.add(str);
/*     */       }
/* 124 */       if (this.signatureList.size() == 0) {
/* 125 */         throw new IllegalArgumentException("Invalid JNI signature '" + this.signature + "'");
/*     */       }
/*     */     }
/*     */ 
/* 129 */     return this.signatureList;
/*     */   }
/*     */ 
/*     */   private synchronized List<String> typeNameList() {
/* 133 */     if (this.typeNameList == null) {
/* 134 */       this.typeNameList = new ArrayList(10);
/*     */ 
/* 137 */       this.currentIndex = 0;
/*     */ 
/* 139 */       while (this.currentIndex < this.signature.length()) {
/* 140 */         String str = nextTypeName();
/* 141 */         this.typeNameList.add(str);
/*     */       }
/* 143 */       if (this.typeNameList.size() == 0) {
/* 144 */         throw new IllegalArgumentException("Invalid JNI signature '" + this.signature + "'");
/*     */       }
/*     */     }
/*     */ 
/* 148 */     return this.typeNameList;
/*     */   }
/*     */ 
/*     */   private String nextSignature() {
/* 152 */     char c = this.signature.charAt(this.currentIndex++);
/*     */ 
/* 154 */     switch (c) {
/*     */     case '[':
/* 156 */       return c + nextSignature();
/*     */     case 'L':
/* 159 */       int i = this.signature.indexOf(';', this.currentIndex);
/*     */ 
/* 161 */       String str = this.signature.substring(this.currentIndex - 1, i + 1);
/*     */ 
/* 163 */       this.currentIndex = (i + 1);
/* 164 */       return str;
/*     */     case 'B':
/*     */     case 'C':
/*     */     case 'D':
/*     */     case 'F':
/*     */     case 'I':
/*     */     case 'J':
/*     */     case 'S':
/*     */     case 'V':
/*     */     case 'Z':
/* 175 */       return String.valueOf(c);
/*     */     case '(':
/*     */     case ')':
/* 179 */       return nextSignature();
/*     */     case '*':
/*     */     case '+':
/*     */     case ',':
/*     */     case '-':
/*     */     case '.':
/*     */     case '/':
/*     */     case '0':
/*     */     case '1':
/*     */     case '2':
/*     */     case '3':
/*     */     case '4':
/*     */     case '5':
/*     */     case '6':
/*     */     case '7':
/*     */     case '8':
/*     */     case '9':
/*     */     case ':':
/*     */     case ';':
/*     */     case '<':
/*     */     case '=':
/*     */     case '>':
/*     */     case '?':
/*     */     case '@':
/*     */     case 'A':
/*     */     case 'E':
/*     */     case 'G':
/*     */     case 'H':
/*     */     case 'K':
/*     */     case 'M':
/*     */     case 'N':
/*     */     case 'O':
/*     */     case 'P':
/*     */     case 'Q':
/*     */     case 'R':
/*     */     case 'T':
/*     */     case 'U':
/*     */     case 'W':
/*     */     case 'X':
/* 182 */     case 'Y': } throw new IllegalArgumentException("Invalid JNI signature character '" + c + "'");
/*     */   }
/*     */ 
/*     */   private String nextTypeName()
/*     */   {
/* 189 */     char c = this.signature.charAt(this.currentIndex++);
/*     */ 
/* 191 */     switch (c) {
/*     */     case '[':
/* 193 */       return nextTypeName() + "[]";
/*     */     case 'B':
/* 196 */       return "byte";
/*     */     case 'C':
/* 199 */       return "char";
/*     */     case 'L':
/* 202 */       int i = this.signature.indexOf(';', this.currentIndex);
/*     */ 
/* 204 */       String str = this.signature.substring(this.currentIndex, i);
/*     */ 
/* 206 */       str = str.replace('/', '.');
/* 207 */       this.currentIndex = (i + 1);
/* 208 */       return str;
/*     */     case 'F':
/* 211 */       return "float";
/*     */     case 'D':
/* 214 */       return "double";
/*     */     case 'I':
/* 217 */       return "int";
/*     */     case 'J':
/* 220 */       return "long";
/*     */     case 'S':
/* 223 */       return "short";
/*     */     case 'V':
/* 226 */       return "void";
/*     */     case 'Z':
/* 229 */       return "boolean";
/*     */     case '(':
/*     */     case ')':
/* 233 */       return nextTypeName();
/*     */     case '*':
/*     */     case '+':
/*     */     case ',':
/*     */     case '-':
/*     */     case '.':
/*     */     case '/':
/*     */     case '0':
/*     */     case '1':
/*     */     case '2':
/*     */     case '3':
/*     */     case '4':
/*     */     case '5':
/*     */     case '6':
/*     */     case '7':
/*     */     case '8':
/*     */     case '9':
/*     */     case ':':
/*     */     case ';':
/*     */     case '<':
/*     */     case '=':
/*     */     case '>':
/*     */     case '?':
/*     */     case '@':
/*     */     case 'A':
/*     */     case 'E':
/*     */     case 'G':
/*     */     case 'H':
/*     */     case 'K':
/*     */     case 'M':
/*     */     case 'N':
/*     */     case 'O':
/*     */     case 'P':
/*     */     case 'Q':
/*     */     case 'R':
/*     */     case 'T':
/*     */     case 'U':
/*     */     case 'W':
/*     */     case 'X':
/* 236 */     case 'Y': } throw new IllegalArgumentException("Invalid JNI signature character '" + c + "'");
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.jdi.JNITypeParser
 * JD-Core Version:    0.6.2
 */