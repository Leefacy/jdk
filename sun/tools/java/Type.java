/*     */ package sun.tools.java;
/*     */ 
/*     */ import java.util.Hashtable;
/*     */ 
/*     */ public class Type
/*     */   implements Constants
/*     */ {
/*  60 */   private static final Hashtable typeHash = new Hashtable(231);
/*     */   protected int typeCode;
/*     */   protected String typeSig;
/*  79 */   public static final Type[] noArgs = new Type[0];
/*  80 */   public static final Type tError = new Type(13, "?");
/*  81 */   public static final Type tPackage = new Type(13, ".");
/*  82 */   public static final Type tNull = new Type(8, "*");
/*  83 */   public static final Type tVoid = new Type(11, "V");
/*  84 */   public static final Type tBoolean = new Type(0, "Z");
/*  85 */   public static final Type tByte = new Type(1, "B");
/*  86 */   public static final Type tChar = new Type(2, "C");
/*  87 */   public static final Type tShort = new Type(3, "S");
/*  88 */   public static final Type tInt = new Type(4, "I");
/*  89 */   public static final Type tFloat = new Type(6, "F");
/*  90 */   public static final Type tLong = new Type(5, "J");
/*  91 */   public static final Type tDouble = new Type(7, "D");
/*  92 */   public static final Type tObject = tClass(idJavaLangObject);
/*  93 */   public static final Type tClassDesc = tClass(idJavaLangClass);
/*  94 */   public static final Type tString = tClass(idJavaLangString);
/*  95 */   public static final Type tCloneable = tClass(idJavaLangCloneable);
/*  96 */   public static final Type tSerializable = tClass(idJavaIoSerializable);
/*     */ 
/*     */   protected Type(int paramInt, String paramString)
/*     */   {
/* 102 */     this.typeCode = paramInt;
/* 103 */     this.typeSig = paramString;
/* 104 */     typeHash.put(paramString, this);
/*     */   }
/*     */ 
/*     */   public final String getTypeSignature()
/*     */   {
/* 111 */     return this.typeSig;
/*     */   }
/*     */ 
/*     */   public final int getTypeCode()
/*     */   {
/* 118 */     return this.typeCode;
/*     */   }
/*     */ 
/*     */   public final int getTypeMask()
/*     */   {
/* 128 */     return 1 << this.typeCode;
/*     */   }
/*     */ 
/*     */   public final boolean isType(int paramInt)
/*     */   {
/* 135 */     return this.typeCode == paramInt;
/*     */   }
/*     */ 
/*     */   public boolean isVoidArray()
/*     */   {
/* 148 */     if (!isType(9)) {
/* 149 */       return false;
/*     */     }
/*     */ 
/* 152 */     Type localType = this;
/* 153 */     while (localType.isType(9)) {
/* 154 */       localType = localType.getElementType();
/*     */     }
/* 156 */     return localType.isType(11);
/*     */   }
/*     */ 
/*     */   public final boolean inMask(int paramInt)
/*     */   {
/* 164 */     return (1 << this.typeCode & paramInt) != 0;
/*     */   }
/*     */ 
/*     */   public static synchronized Type tArray(Type paramType)
/*     */   {
/* 171 */     String str = new String("[" + paramType.getTypeSignature());
/* 172 */     Object localObject = (Type)typeHash.get(str);
/* 173 */     if (localObject == null) {
/* 174 */       localObject = new ArrayType(str, paramType);
/*     */     }
/* 176 */     return localObject;
/*     */   }
/*     */ 
/*     */   public Type getElementType()
/*     */   {
/* 184 */     throw new CompilerError("getElementType");
/*     */   }
/*     */ 
/*     */   public int getArrayDimension()
/*     */   {
/* 192 */     return 0;
/*     */   }
/*     */ 
/*     */   public static synchronized Type tClass(Identifier paramIdentifier)
/*     */   {
/* 200 */     if (paramIdentifier.isInner()) {
/* 201 */       localObject1 = tClass(mangleInnerType(paramIdentifier));
/* 202 */       if (((Type)localObject1).getClassName() != paramIdentifier)
/*     */       {
/* 205 */         changeClassName(((Type)localObject1).getClassName(), paramIdentifier);
/* 206 */       }return localObject1;
/*     */     }
/*     */ 
/* 209 */     if (paramIdentifier.typeObject != null) {
/* 210 */       return paramIdentifier.typeObject;
/*     */     }
/*     */ 
/* 214 */     Object localObject1 = new String("L" + paramIdentifier
/* 214 */       .toString().replace('.', '/') + ";");
/*     */ 
/* 216 */     Object localObject2 = (Type)typeHash.get(localObject1);
/* 217 */     if (localObject2 == null) {
/* 218 */       localObject2 = new ClassType((String)localObject1, paramIdentifier);
/*     */     }
/*     */ 
/* 221 */     paramIdentifier.typeObject = ((Type)localObject2);
/* 222 */     return localObject2;
/*     */   }
/*     */ 
/*     */   public Identifier getClassName()
/*     */   {
/* 229 */     throw new CompilerError("getClassName:" + this);
/*     */   }
/*     */ 
/*     */   public static Identifier mangleInnerType(Identifier paramIdentifier)
/*     */   {
/* 242 */     if (!paramIdentifier.isInner()) return paramIdentifier;
/* 243 */     Identifier localIdentifier = Identifier.lookup(paramIdentifier
/* 244 */       .getFlatName().toString()
/* 245 */       .replace('.', '$'));
/*     */ 
/* 246 */     if (localIdentifier.isInner()) throw new CompilerError("mangle " + localIdentifier);
/* 247 */     return Identifier.lookup(paramIdentifier.getQualifier(), localIdentifier);
/*     */   }
/*     */ 
/*     */   static void changeClassName(Identifier paramIdentifier1, Identifier paramIdentifier2)
/*     */   {
/* 263 */     ((ClassType)tClass(paramIdentifier1)).className = paramIdentifier2;
/*     */   }
/*     */ 
/*     */   public static synchronized Type tMethod(Type paramType)
/*     */   {
/* 270 */     return tMethod(paramType, noArgs);
/*     */   }
/*     */ 
/*     */   public static synchronized Type tMethod(Type paramType, Type[] paramArrayOfType)
/*     */   {
/* 277 */     StringBuffer localStringBuffer = new StringBuffer();
/* 278 */     localStringBuffer.append("(");
/* 279 */     for (int i = 0; i < paramArrayOfType.length; i++) {
/* 280 */       localStringBuffer.append(paramArrayOfType[i].getTypeSignature());
/*     */     }
/* 282 */     localStringBuffer.append(")");
/* 283 */     localStringBuffer.append(paramType.getTypeSignature());
/*     */ 
/* 285 */     String str = localStringBuffer.toString();
/* 286 */     Object localObject = (Type)typeHash.get(str);
/* 287 */     if (localObject == null) {
/* 288 */       localObject = new MethodType(str, paramType, paramArrayOfType);
/*     */     }
/* 290 */     return localObject;
/*     */   }
/*     */ 
/*     */   public Type getReturnType()
/*     */   {
/* 297 */     throw new CompilerError("getReturnType");
/*     */   }
/*     */ 
/*     */   public Type[] getArgumentTypes()
/*     */   {
/* 304 */     throw new CompilerError("getArgumentTypes");
/*     */   }
/*     */ 
/*     */   public static synchronized Type tType(String paramString)
/*     */   {
/* 312 */     Type localType = (Type)typeHash.get(paramString);
/* 313 */     if (localType != null) {
/* 314 */       return localType;
/*     */     }
/*     */ 
/* 317 */     switch (paramString.charAt(0)) {
/*     */     case '[':
/* 319 */       return tArray(tType(paramString.substring(1)));
/*     */     case 'L':
/* 322 */       return tClass(Identifier.lookup(paramString.substring(1, paramString.length() - 1).replace('/', '.')));
/*     */     case '(':
/* 325 */       Object localObject = new Type[8];
/* 326 */       int i = 0;
/*     */       int k;
/* 329 */       for (int j = 1; paramString.charAt(j) != ')'; j = k) {
/* 330 */         for (k = j; paramString.charAt(k) == '['; k++);
/* 331 */         while ((paramString.charAt(k++) == 'L') && 
/* 332 */           (paramString.charAt(k++) != ';'));
/* 334 */         if (i == localObject.length) {
/* 335 */           arrayOfType = new Type[i * 2];
/* 336 */           System.arraycopy(localObject, 0, arrayOfType, 0, i);
/* 337 */           localObject = arrayOfType;
/*     */         }
/* 339 */         localObject[(i++)] = tType(paramString.substring(j, k));
/*     */       }
/*     */ 
/* 342 */       Type[] arrayOfType = new Type[i];
/* 343 */       System.arraycopy(localObject, 0, arrayOfType, 0, i);
/* 344 */       return tMethod(tType(paramString.substring(j + 1)), arrayOfType);
/*     */     }
/*     */ 
/* 348 */     throw new CompilerError("invalid TypeSignature:" + paramString);
/*     */   }
/*     */ 
/*     */   public boolean equalArguments(Type paramType)
/*     */   {
/* 357 */     return false;
/*     */   }
/*     */ 
/*     */   public int stackSize()
/*     */   {
/* 366 */     switch (this.typeCode) {
/*     */     case 11:
/*     */     case 13:
/* 369 */       return 0;
/*     */     case 0:
/*     */     case 1:
/*     */     case 2:
/*     */     case 3:
/*     */     case 4:
/*     */     case 6:
/*     */     case 9:
/*     */     case 10:
/* 378 */       return 1;
/*     */     case 5:
/*     */     case 7:
/* 381 */       return 2;
/*     */     case 8:
/* 383 */     case 12: } throw new CompilerError("stackSize " + toString());
/*     */   }
/*     */ 
/*     */   public int getTypeCodeOffset()
/*     */   {
/* 394 */     switch (this.typeCode) {
/*     */     case 0:
/*     */     case 1:
/*     */     case 2:
/*     */     case 3:
/*     */     case 4:
/* 400 */       return 0;
/*     */     case 5:
/* 402 */       return 1;
/*     */     case 6:
/* 404 */       return 2;
/*     */     case 7:
/* 406 */       return 3;
/*     */     case 8:
/*     */     case 9:
/*     */     case 10:
/* 410 */       return 4;
/*     */     }
/* 412 */     throw new CompilerError("invalid typecode: " + this.typeCode);
/*     */   }
/*     */ 
/*     */   public String typeString(String paramString, boolean paramBoolean1, boolean paramBoolean2)
/*     */   {
/* 420 */     String str = null;
/*     */ 
/* 422 */     switch (this.typeCode) { case 8:
/* 423 */       str = "null"; break;
/*     */     case 11:
/* 424 */       str = "void"; break;
/*     */     case 0:
/* 425 */       str = "boolean"; break;
/*     */     case 1:
/* 426 */       str = "byte"; break;
/*     */     case 2:
/* 427 */       str = "char"; break;
/*     */     case 3:
/* 428 */       str = "short"; break;
/*     */     case 4:
/* 429 */       str = "int"; break;
/*     */     case 5:
/* 430 */       str = "long"; break;
/*     */     case 6:
/* 431 */       str = "float"; break;
/*     */     case 7:
/* 432 */       str = "double"; break;
/*     */     case 13:
/* 433 */       str = "<error>";
/* 434 */       if (this == tPackage) str = "<package>"; break;
/*     */     case 9:
/*     */     case 10:
/*     */     case 12:
/*     */     default:
/* 436 */       str = "unknown";
/*     */     }
/*     */ 
/* 439 */     return paramString.length() > 0 ? str + " " + paramString : str;
/*     */   }
/*     */ 
/*     */   public String typeString(String paramString)
/*     */   {
/* 446 */     return typeString(paramString, false, true);
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 453 */     return typeString("", false, true);
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     sun.tools.java.Type
 * JD-Core Version:    0.6.2
 */