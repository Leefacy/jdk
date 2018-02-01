/*     */ package com.sun.tools.classfile;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ 
/*     */ public class Signature extends Descriptor
/*     */ {
/*     */   private String sig;
/*     */   private int sigp;
/*     */   private Type type;
/*     */ 
/*     */   public Signature(int paramInt)
/*     */   {
/*  43 */     super(paramInt);
/*     */   }
/*     */ 
/*     */   public Type getType(ConstantPool paramConstantPool) throws ConstantPoolException {
/*  47 */     if (this.type == null)
/*  48 */       this.type = parse(getValue(paramConstantPool));
/*  49 */     return this.type;
/*     */   }
/*     */ 
/*     */   public int getParameterCount(ConstantPool paramConstantPool) throws ConstantPoolException
/*     */   {
/*  54 */     Type.MethodType localMethodType = (Type.MethodType)getType(paramConstantPool);
/*  55 */     return localMethodType.paramTypes.size();
/*     */   }
/*     */ 
/*     */   public String getParameterTypes(ConstantPool paramConstantPool) throws ConstantPoolException
/*     */   {
/*  60 */     Type.MethodType localMethodType = (Type.MethodType)getType(paramConstantPool);
/*  61 */     StringBuilder localStringBuilder = new StringBuilder();
/*  62 */     localStringBuilder.append("(");
/*  63 */     String str = "";
/*  64 */     for (Type localType : localMethodType.paramTypes) {
/*  65 */       localStringBuilder.append(str);
/*  66 */       localStringBuilder.append(localType);
/*  67 */       str = ", ";
/*     */     }
/*  69 */     localStringBuilder.append(")");
/*  70 */     return localStringBuilder.toString();
/*     */   }
/*     */ 
/*     */   public String getReturnType(ConstantPool paramConstantPool) throws ConstantPoolException
/*     */   {
/*  75 */     Type.MethodType localMethodType = (Type.MethodType)getType(paramConstantPool);
/*  76 */     return localMethodType.returnType.toString();
/*     */   }
/*     */ 
/*     */   public String getFieldType(ConstantPool paramConstantPool) throws ConstantPoolException
/*     */   {
/*  81 */     return getType(paramConstantPool).toString();
/*     */   }
/*     */ 
/*     */   private Type parse(String paramString) {
/*  85 */     this.sig = paramString;
/*  86 */     this.sigp = 0;
/*     */ 
/*  88 */     List localList = null;
/*  89 */     if (paramString.charAt(this.sigp) == '<') {
/*  90 */       localList = parseTypeParamTypes();
/*     */     }
/*  92 */     if (paramString.charAt(this.sigp) == '(') {
/*  93 */       localObject1 = parseTypeSignatures(')');
/*  94 */       localObject2 = parseTypeSignature();
/*  95 */       localArrayList = null;
/*  96 */       while ((this.sigp < paramString.length()) && (paramString.charAt(this.sigp) == '^')) {
/*  97 */         this.sigp += 1;
/*  98 */         if (localArrayList == null)
/*  99 */           localArrayList = new ArrayList();
/* 100 */         localArrayList.add(parseTypeSignature());
/*     */       }
/* 102 */       return new Type.MethodType(localList, (List)localObject1, (Type)localObject2, localArrayList);
/*     */     }
/* 104 */     Object localObject1 = parseTypeSignature();
/* 105 */     if ((localList == null) && (this.sigp == paramString.length()))
/* 106 */       return localObject1;
/* 107 */     Object localObject2 = localObject1;
/* 108 */     ArrayList localArrayList = null;
/* 109 */     while (this.sigp < paramString.length()) {
/* 110 */       if (localArrayList == null)
/* 111 */         localArrayList = new ArrayList();
/* 112 */       localArrayList.add(parseTypeSignature());
/*     */     }
/* 114 */     return new Type.ClassSigType(localList, (Type)localObject2, localArrayList);
/*     */   }
/*     */ 
/*     */   private Type parseTypeSignature()
/*     */   {
/* 120 */     switch (this.sig.charAt(this.sigp)) {
/*     */     case 'B':
/* 122 */       this.sigp += 1;
/* 123 */       return new Type.SimpleType("byte");
/*     */     case 'C':
/* 126 */       this.sigp += 1;
/* 127 */       return new Type.SimpleType("char");
/*     */     case 'D':
/* 130 */       this.sigp += 1;
/* 131 */       return new Type.SimpleType("double");
/*     */     case 'F':
/* 134 */       this.sigp += 1;
/* 135 */       return new Type.SimpleType("float");
/*     */     case 'I':
/* 138 */       this.sigp += 1;
/* 139 */       return new Type.SimpleType("int");
/*     */     case 'J':
/* 142 */       this.sigp += 1;
/* 143 */       return new Type.SimpleType("long");
/*     */     case 'L':
/* 146 */       return parseClassTypeSignature();
/*     */     case 'S':
/* 149 */       this.sigp += 1;
/* 150 */       return new Type.SimpleType("short");
/*     */     case 'T':
/* 153 */       return parseTypeVariableSignature();
/*     */     case 'V':
/* 156 */       this.sigp += 1;
/* 157 */       return new Type.SimpleType("void");
/*     */     case 'Z':
/* 160 */       this.sigp += 1;
/* 161 */       return new Type.SimpleType("boolean");
/*     */     case '[':
/* 164 */       this.sigp += 1;
/* 165 */       return new Type.ArrayType(parseTypeSignature());
/*     */     case '*':
/* 168 */       this.sigp += 1;
/* 169 */       return new Type.WildcardType();
/*     */     case '+':
/* 172 */       this.sigp += 1;
/* 173 */       return new Type.WildcardType(Type.WildcardType.Kind.EXTENDS, parseTypeSignature());
/*     */     case '-':
/* 176 */       this.sigp += 1;
/* 177 */       return new Type.WildcardType(Type.WildcardType.Kind.SUPER, parseTypeSignature());
/*     */     case ',':
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
/*     */     case 'U':
/*     */     case 'W':
/*     */     case 'X':
/* 180 */     case 'Y': } throw new IllegalStateException(debugInfo());
/*     */   }
/*     */ 
/*     */   private List<Type> parseTypeSignatures(char paramChar)
/*     */   {
/* 185 */     this.sigp += 1;
/* 186 */     ArrayList localArrayList = new ArrayList();
/* 187 */     while (this.sig.charAt(this.sigp) != paramChar)
/* 188 */       localArrayList.add(parseTypeSignature());
/* 189 */     this.sigp += 1;
/* 190 */     return localArrayList;
/*     */   }
/*     */ 
/*     */   private Type parseClassTypeSignature() {
/* 194 */     assert (this.sig.charAt(this.sigp) == 'L');
/* 195 */     this.sigp += 1;
/* 196 */     return parseClassTypeSignatureRest();
/*     */   }
/*     */ 
/* 200 */   private Type parseClassTypeSignatureRest() { StringBuilder localStringBuilder = new StringBuilder();
/* 201 */     List localList = null;
/* 202 */     Type.ClassType localClassType = null;
/*     */     char c;
/*     */     do switch (c = this.sig.charAt(this.sigp)) {
/*     */       case '<':
/* 208 */         localList = parseTypeSignatures('>');
/* 209 */         break;
/*     */       case '.':
/*     */       case ';':
/* 213 */         this.sigp += 1;
/* 214 */         localClassType = new Type.ClassType(localClassType, localStringBuilder.toString(), localList);
/* 215 */         localStringBuilder.setLength(0);
/* 216 */         localList = null;
/* 217 */         break;
/*     */       default:
/* 220 */         this.sigp += 1;
/* 221 */         localStringBuilder.append(c);
/*     */       }
/*     */ 
/* 224 */     while (c != ';');
/*     */ 
/* 226 */     return localClassType; }
/*     */ 
/*     */   private List<Type.TypeParamType> parseTypeParamTypes()
/*     */   {
/* 230 */     assert (this.sig.charAt(this.sigp) == '<');
/* 231 */     this.sigp += 1;
/* 232 */     ArrayList localArrayList = new ArrayList();
/* 233 */     while (this.sig.charAt(this.sigp) != '>')
/* 234 */       localArrayList.add(parseTypeParamType());
/* 235 */     this.sigp += 1;
/* 236 */     return localArrayList;
/*     */   }
/*     */ 
/*     */   private Type.TypeParamType parseTypeParamType() {
/* 240 */     int i = this.sig.indexOf(":", this.sigp);
/* 241 */     String str = this.sig.substring(this.sigp, i);
/* 242 */     Type localType = null;
/* 243 */     ArrayList localArrayList = null;
/* 244 */     this.sigp = (i + 1);
/* 245 */     if (this.sig.charAt(this.sigp) != ':')
/* 246 */       localType = parseTypeSignature();
/* 247 */     while (this.sig.charAt(this.sigp) == ':') {
/* 248 */       this.sigp += 1;
/* 249 */       if (localArrayList == null)
/* 250 */         localArrayList = new ArrayList();
/* 251 */       localArrayList.add(parseTypeSignature());
/*     */     }
/* 253 */     return new Type.TypeParamType(str, localType, localArrayList);
/*     */   }
/*     */ 
/*     */   private Type parseTypeVariableSignature() {
/* 257 */     this.sigp += 1;
/* 258 */     int i = this.sig.indexOf(';', this.sigp);
/* 259 */     Type.SimpleType localSimpleType = new Type.SimpleType(this.sig.substring(this.sigp, i));
/* 260 */     this.sigp = (i + 1);
/* 261 */     return localSimpleType;
/*     */   }
/*     */ 
/*     */   private String debugInfo() {
/* 265 */     return this.sig.substring(0, this.sigp) + "!" + this.sig.charAt(this.sigp) + "!" + this.sig.substring(this.sigp + 1);
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.classfile.Signature
 * JD-Core Version:    0.6.2
 */