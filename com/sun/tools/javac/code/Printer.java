/*     */ package com.sun.tools.javac.code;
/*     */ 
/*     */ import com.sun.tools.javac.api.Messages;
/*     */ import com.sun.tools.javac.util.List;
/*     */ import com.sun.tools.javac.util.ListBuffer;
/*     */ import com.sun.tools.javac.util.Name;
/*     */ import com.sun.tools.javac.util.Name.Table;
/*     */ import com.sun.tools.javac.util.Names;
/*     */ import java.util.Locale;
/*     */ 
/*     */ public abstract class Printer
/*     */   implements Type.Visitor<String, Locale>, Symbol.Visitor<String, Locale>
/*     */ {
/*  54 */   List<Type> seenCaptured = List.nil();
/*     */   static final int PRIME = 997;
/*     */ 
/*     */   protected abstract String localize(Locale paramLocale, String paramString, Object[] paramArrayOfObject);
/*     */ 
/*     */   protected abstract String capturedVarId(Type.CapturedType paramCapturedType, Locale paramLocale);
/*     */ 
/*     */   public static Printer createStandardPrinter(Messages paramMessages)
/*     */   {
/*  86 */     return new Printer()
/*     */     {
/*     */       protected String localize(Locale paramAnonymousLocale, String paramAnonymousString, Object[] paramAnonymousArrayOfObject) {
/*  89 */         return this.val$messages.getLocalizedString(paramAnonymousLocale, paramAnonymousString, paramAnonymousArrayOfObject);
/*     */       }
/*     */ 
/*     */       protected String capturedVarId(Type.CapturedType paramAnonymousCapturedType, Locale paramAnonymousLocale)
/*     */       {
/*  94 */         return (paramAnonymousCapturedType.hashCode() & 0xFFFFFFFF) % 997L + "";
/*     */       }
/*     */     };
/*     */   }
/*     */ 
/*     */   public String visitTypes(List<Type> paramList, Locale paramLocale)
/*     */   {
/* 106 */     ListBuffer localListBuffer = new ListBuffer();
/* 107 */     for (Type localType : paramList) {
/* 108 */       localListBuffer.append(visit(localType, paramLocale));
/*     */     }
/* 110 */     return localListBuffer.toList().toString();
/*     */   }
/*     */ 
/*     */   public String visitSymbols(List<Symbol> paramList, Locale paramLocale)
/*     */   {
/* 121 */     ListBuffer localListBuffer = new ListBuffer();
/* 122 */     for (Symbol localSymbol : paramList) {
/* 123 */       localListBuffer.append(visit(localSymbol, paramLocale));
/*     */     }
/* 125 */     return localListBuffer.toList().toString();
/*     */   }
/*     */ 
/*     */   public String visit(Type paramType, Locale paramLocale)
/*     */   {
/* 136 */     return (String)paramType.accept(this, paramLocale);
/*     */   }
/*     */ 
/*     */   public String visit(Symbol paramSymbol, Locale paramLocale)
/*     */   {
/* 147 */     return (String)paramSymbol.accept(this, paramLocale);
/*     */   }
/*     */ 
/*     */   public String visitCapturedType(Type.CapturedType paramCapturedType, Locale paramLocale)
/*     */   {
/* 152 */     if (this.seenCaptured.contains(paramCapturedType)) {
/* 153 */       return localize(paramLocale, "compiler.misc.type.captureof.1", new Object[] { 
/* 154 */         capturedVarId(paramCapturedType, paramLocale) });
/*     */     }
/*     */     try
/*     */     {
/* 157 */       this.seenCaptured = this.seenCaptured.prepend(paramCapturedType);
/* 158 */       return localize(paramLocale, "compiler.misc.type.captureof", new Object[] { 
/* 159 */         capturedVarId(paramCapturedType, paramLocale), 
/* 160 */         visit(paramCapturedType.wildcard, paramLocale) });
/*     */     }
/*     */     finally
/*     */     {
/* 163 */       this.seenCaptured = this.seenCaptured.tail;
/*     */     }
/*     */   }
/*     */ 
/*     */   public String visitForAll(Type.ForAll paramForAll, Locale paramLocale)
/*     */   {
/* 170 */     return "<" + visitTypes(paramForAll.tvars, paramLocale) + ">" + visit(paramForAll.qtype, paramLocale);
/*     */   }
/*     */ 
/*     */   public String visitUndetVar(Type.UndetVar paramUndetVar, Locale paramLocale)
/*     */   {
/* 175 */     if (paramUndetVar.inst != null) {
/* 176 */       return visit(paramUndetVar.inst, paramLocale);
/*     */     }
/* 178 */     return visit(paramUndetVar.qtype, paramLocale) + "?";
/*     */   }
/*     */ 
/*     */   public String visitArrayType(Type.ArrayType paramArrayType, Locale paramLocale)
/*     */   {
/* 184 */     StringBuilder localStringBuilder = new StringBuilder();
/* 185 */     printBaseElementType(paramArrayType, localStringBuilder, paramLocale);
/* 186 */     printBrackets(paramArrayType, localStringBuilder, paramLocale);
/* 187 */     return localStringBuilder.toString();
/*     */   }
/*     */ 
/*     */   void printBaseElementType(Type paramType, StringBuilder paramStringBuilder, Locale paramLocale) {
/* 191 */     Type localType = paramType;
/* 192 */     while (localType.hasTag(TypeTag.ARRAY)) {
/* 193 */       localType = localType.unannotatedType();
/* 194 */       localType = ((Type.ArrayType)localType).elemtype;
/*     */     }
/* 196 */     paramStringBuilder.append(visit(localType, paramLocale));
/*     */   }
/*     */ 
/*     */   void printBrackets(Type paramType, StringBuilder paramStringBuilder, Locale paramLocale) {
/* 200 */     Type localType = paramType;
/* 201 */     while (localType.hasTag(TypeTag.ARRAY)) {
/* 202 */       if (localType.isAnnotated()) {
/* 203 */         paramStringBuilder.append(' ');
/* 204 */         paramStringBuilder.append(localType.getAnnotationMirrors());
/* 205 */         paramStringBuilder.append(' ');
/*     */       }
/* 207 */       paramStringBuilder.append("[]");
/* 208 */       localType = localType.unannotatedType();
/* 209 */       localType = ((Type.ArrayType)localType).elemtype;
/*     */     }
/*     */   }
/*     */ 
/*     */   public String visitClassType(Type.ClassType paramClassType, Locale paramLocale)
/*     */   {
/* 215 */     StringBuilder localStringBuilder = new StringBuilder();
/* 216 */     if ((paramClassType.getEnclosingType().hasTag(TypeTag.CLASS)) && (paramClassType.tsym.owner.kind == 2)) {
/* 217 */       localStringBuilder.append(visit(paramClassType.getEnclosingType(), paramLocale));
/* 218 */       localStringBuilder.append('.');
/* 219 */       localStringBuilder.append(className(paramClassType, false, paramLocale));
/*     */     } else {
/* 221 */       localStringBuilder.append(className(paramClassType, true, paramLocale));
/*     */     }
/* 223 */     if (paramClassType.getTypeArguments().nonEmpty()) {
/* 224 */       localStringBuilder.append('<');
/* 225 */       localStringBuilder.append(visitTypes(paramClassType.getTypeArguments(), paramLocale));
/* 226 */       localStringBuilder.append('>');
/*     */     }
/* 228 */     return localStringBuilder.toString();
/*     */   }
/*     */ 
/*     */   public String visitMethodType(Type.MethodType paramMethodType, Locale paramLocale)
/*     */   {
/* 233 */     return "(" + printMethodArgs(paramMethodType.argtypes, false, paramLocale) + ")" + visit(paramMethodType.restype, paramLocale);
/*     */   }
/*     */ 
/*     */   public String visitPackageType(Type.PackageType paramPackageType, Locale paramLocale)
/*     */   {
/* 238 */     return paramPackageType.tsym.getQualifiedName().toString();
/*     */   }
/*     */ 
/*     */   public String visitWildcardType(Type.WildcardType paramWildcardType, Locale paramLocale)
/*     */   {
/* 243 */     StringBuilder localStringBuilder = new StringBuilder();
/* 244 */     localStringBuilder.append(paramWildcardType.kind);
/* 245 */     if (paramWildcardType.kind != BoundKind.UNBOUND) {
/* 246 */       localStringBuilder.append(visit(paramWildcardType.type, paramLocale));
/*     */     }
/* 248 */     return localStringBuilder.toString();
/*     */   }
/*     */ 
/*     */   public String visitErrorType(Type.ErrorType paramErrorType, Locale paramLocale)
/*     */   {
/* 253 */     return visitType(paramErrorType, paramLocale);
/*     */   }
/*     */ 
/*     */   public String visitTypeVar(Type.TypeVar paramTypeVar, Locale paramLocale)
/*     */   {
/* 258 */     return visitType(paramTypeVar, paramLocale);
/*     */   }
/*     */ 
/*     */   public String visitAnnotatedType(Type.AnnotatedType paramAnnotatedType, Locale paramLocale)
/*     */   {
/* 263 */     if (paramAnnotatedType.getAnnotationMirrors().nonEmpty()) {
/* 264 */       if (paramAnnotatedType.unannotatedType().hasTag(TypeTag.ARRAY)) {
/* 265 */         StringBuilder localStringBuilder = new StringBuilder();
/* 266 */         printBaseElementType(paramAnnotatedType, localStringBuilder, paramLocale);
/* 267 */         printBrackets(paramAnnotatedType, localStringBuilder, paramLocale);
/* 268 */         return localStringBuilder.toString();
/* 269 */       }if ((paramAnnotatedType.unannotatedType().hasTag(TypeTag.CLASS)) && 
/* 270 */         (paramAnnotatedType
/* 270 */         .unannotatedType().getEnclosingType() != Type.noType))
/*     */       {
/* 274 */         return visit(paramAnnotatedType.unannotatedType().getEnclosingType(), paramLocale) + ". " + paramAnnotatedType
/* 273 */           .getAnnotationMirrors() + " " + 
/* 274 */           className((Type.ClassType)paramAnnotatedType
/* 274 */           .unannotatedType(), false, paramLocale);
/*     */       }
/* 276 */       return paramAnnotatedType.getAnnotationMirrors() + " " + visit(paramAnnotatedType.unannotatedType(), paramLocale);
/*     */     }
/*     */ 
/* 279 */     return visit(paramAnnotatedType.unannotatedType(), paramLocale);
/*     */   }
/*     */ 
/*     */   public String visitType(Type paramType, Locale paramLocale)
/*     */   {
/* 286 */     String str = (paramType.tsym == null) || (paramType.tsym.name == null) ? 
/* 285 */       localize(paramLocale, "compiler.misc.type.none", new Object[0]) : 
/* 285 */       paramType.tsym.name
/* 286 */       .toString();
/* 287 */     return str;
/*     */   }
/*     */ 
/*     */   protected String className(Type.ClassType paramClassType, boolean paramBoolean, Locale paramLocale)
/*     */   {
/* 301 */     Symbol.TypeSymbol localTypeSymbol = paramClassType.tsym;
/*     */     Object localObject1;
/*     */     Object localObject2;
/* 302 */     if ((localTypeSymbol.name.length() == 0) && ((localTypeSymbol.flags() & 0x1000000) != 0L)) {
/* 303 */       localObject1 = new StringBuilder(visit(paramClassType.supertype_field, paramLocale));
/* 304 */       for (localObject2 = paramClassType.interfaces_field; ((List)localObject2).nonEmpty(); localObject2 = ((List)localObject2).tail) {
/* 305 */         ((StringBuilder)localObject1).append('&');
/* 306 */         ((StringBuilder)localObject1).append(visit((Type)((List)localObject2).head, paramLocale));
/*     */       }
/* 308 */       return ((StringBuilder)localObject1).toString();
/* 309 */     }if (localTypeSymbol.name.length() == 0)
/*     */     {
/* 311 */       localObject2 = (Type.ClassType)paramClassType.tsym.type;
/* 312 */       if (localObject2 == null)
/* 313 */         localObject1 = localize(paramLocale, "compiler.misc.anonymous.class", new Object[] { null });
/* 314 */       else if ((((Type.ClassType)localObject2).interfaces_field != null) && (((Type.ClassType)localObject2).interfaces_field.nonEmpty())) {
/* 315 */         localObject1 = localize(paramLocale, "compiler.misc.anonymous.class", new Object[] { 
/* 316 */           visit((Type)((Type.ClassType)localObject2).interfaces_field.head, paramLocale) });
/*     */       }
/*     */       else {
/* 318 */         localObject1 = localize(paramLocale, "compiler.misc.anonymous.class", new Object[] { 
/* 319 */           visit(((Type.ClassType)localObject2).supertype_field, paramLocale) });
/*     */       }
/*     */ 
/* 321 */       return localObject1;
/* 322 */     }if (paramBoolean) {
/* 323 */       return localTypeSymbol.getQualifiedName().toString();
/*     */     }
/* 325 */     return localTypeSymbol.name.toString();
/*     */   }
/*     */ 
/*     */   protected String printMethodArgs(List<Type> paramList, boolean paramBoolean, Locale paramLocale)
/*     */   {
/* 339 */     if (!paramBoolean) {
/* 340 */       return visitTypes(paramList, paramLocale);
/*     */     }
/* 342 */     StringBuilder localStringBuilder = new StringBuilder();
/* 343 */     while (paramList.tail.nonEmpty()) {
/* 344 */       localStringBuilder.append(visit((Type)paramList.head, paramLocale));
/* 345 */       paramList = paramList.tail;
/* 346 */       localStringBuilder.append(',');
/*     */     }
/* 348 */     if (((Type)paramList.head).unannotatedType().hasTag(TypeTag.ARRAY)) {
/* 349 */       localStringBuilder.append(visit(((Type.ArrayType)((Type)paramList.head).unannotatedType()).elemtype, paramLocale));
/* 350 */       if (((Type)paramList.head).getAnnotationMirrors().nonEmpty()) {
/* 351 */         localStringBuilder.append(' ');
/* 352 */         localStringBuilder.append(((Type)paramList.head).getAnnotationMirrors());
/* 353 */         localStringBuilder.append(' ');
/*     */       }
/* 355 */       localStringBuilder.append("...");
/*     */     } else {
/* 357 */       localStringBuilder.append(visit((Type)paramList.head, paramLocale));
/*     */     }
/* 359 */     return localStringBuilder.toString();
/*     */   }
/*     */ 
/*     */   public String visitClassSymbol(Symbol.ClassSymbol paramClassSymbol, Locale paramLocale)
/*     */   {
/* 367 */     return paramClassSymbol.name.isEmpty() ? 
/* 366 */       localize(paramLocale, "compiler.misc.anonymous.class", new Object[] { paramClassSymbol.flatname }) : 
/* 366 */       paramClassSymbol.fullname
/* 367 */       .toString();
/*     */   }
/*     */ 
/*     */   public String visitMethodSymbol(Symbol.MethodSymbol paramMethodSymbol, Locale paramLocale)
/*     */   {
/* 372 */     if (paramMethodSymbol.isStaticOrInstanceInit()) {
/* 373 */       return paramMethodSymbol.owner.name.toString();
/*     */     }
/*     */ 
/* 377 */     String str = paramMethodSymbol.name == paramMethodSymbol.name.table.names.init ? paramMethodSymbol.owner.name
/* 376 */       .toString() : paramMethodSymbol.name
/* 377 */       .toString();
/* 378 */     if (paramMethodSymbol.type != null) {
/* 379 */       if (paramMethodSymbol.type.hasTag(TypeTag.FORALL)) {
/* 380 */         str = "<" + visitTypes(paramMethodSymbol.type.getTypeArguments(), paramLocale) + ">" + str;
/*     */       }
/* 382 */       str = str + "(" + printMethodArgs(paramMethodSymbol.type
/* 383 */         .getParameterTypes(), 
/* 384 */         (paramMethodSymbol
/* 384 */         .flags() & 0x0) != 0L, paramLocale) + ")";
/*     */     }
/*     */ 
/* 387 */     return str;
/*     */   }
/*     */ 
/*     */   public String visitOperatorSymbol(Symbol.OperatorSymbol paramOperatorSymbol, Locale paramLocale)
/*     */   {
/* 393 */     return visitMethodSymbol(paramOperatorSymbol, paramLocale);
/*     */   }
/*     */ 
/*     */   public String visitPackageSymbol(Symbol.PackageSymbol paramPackageSymbol, Locale paramLocale)
/*     */   {
/* 400 */     return paramPackageSymbol.isUnnamed() ? 
/* 399 */       localize(paramLocale, "compiler.misc.unnamed.package", new Object[0]) : 
/* 399 */       paramPackageSymbol.fullname
/* 400 */       .toString();
/*     */   }
/*     */ 
/*     */   public String visitTypeSymbol(Symbol.TypeSymbol paramTypeSymbol, Locale paramLocale)
/*     */   {
/* 405 */     return visitSymbol(paramTypeSymbol, paramLocale);
/*     */   }
/*     */ 
/*     */   public String visitVarSymbol(Symbol.VarSymbol paramVarSymbol, Locale paramLocale)
/*     */   {
/* 410 */     return visitSymbol(paramVarSymbol, paramLocale);
/*     */   }
/*     */ 
/*     */   public String visitSymbol(Symbol paramSymbol, Locale paramLocale)
/*     */   {
/* 415 */     return paramSymbol.name.toString();
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.javac.code.Printer
 * JD-Core Version:    0.6.2
 */