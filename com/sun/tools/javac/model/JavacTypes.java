/*     */ package com.sun.tools.javac.model;
/*     */ 
/*     */ import com.sun.tools.javac.code.BoundKind;
/*     */ import com.sun.tools.javac.code.Scope;
/*     */ import com.sun.tools.javac.code.Scope.Entry;
/*     */ import com.sun.tools.javac.code.Symbol;
/*     */ import com.sun.tools.javac.code.Symbol.ClassSymbol;
/*     */ import com.sun.tools.javac.code.Symbol.MethodSymbol;
/*     */ import com.sun.tools.javac.code.Symtab;
/*     */ import com.sun.tools.javac.code.Type;
/*     */ import com.sun.tools.javac.code.Type.ArrayType;
/*     */ import com.sun.tools.javac.code.Type.ClassType;
/*     */ import com.sun.tools.javac.code.Type.WildcardType;
/*     */ import com.sun.tools.javac.util.Context;
/*     */ import com.sun.tools.javac.util.ListBuffer;
/*     */ import java.util.Collections;
/*     */ import java.util.EnumSet;
/*     */ import java.util.LinkedHashSet;
/*     */ import java.util.Set;
/*     */ import javax.lang.model.element.Element;
/*     */ import javax.lang.model.element.ElementKind;
/*     */ import javax.lang.model.element.Modifier;
/*     */ import javax.lang.model.element.TypeElement;
/*     */ import javax.lang.model.type.ArrayType;
/*     */ import javax.lang.model.type.DeclaredType;
/*     */ import javax.lang.model.type.ExecutableType;
/*     */ import javax.lang.model.type.NoType;
/*     */ import javax.lang.model.type.NullType;
/*     */ import javax.lang.model.type.PrimitiveType;
/*     */ import javax.lang.model.type.ReferenceType;
/*     */ import javax.lang.model.type.TypeKind;
/*     */ import javax.lang.model.type.TypeMirror;
/*     */ import javax.lang.model.type.WildcardType;
/*     */ 
/*     */ public class JavacTypes
/*     */   implements javax.lang.model.util.Types
/*     */ {
/*     */   private Symtab syms;
/*     */   private com.sun.tools.javac.code.Types types;
/* 284 */   private static final Set<TypeKind> EXEC_OR_PKG = EnumSet.of(TypeKind.EXECUTABLE, TypeKind.PACKAGE)
/* 284 */     ;
/*     */ 
/*     */   public static JavacTypes instance(Context paramContext)
/*     */   {
/*  55 */     JavacTypes localJavacTypes = (JavacTypes)paramContext.get(JavacTypes.class);
/*  56 */     if (localJavacTypes == null)
/*  57 */       localJavacTypes = new JavacTypes(paramContext);
/*  58 */     return localJavacTypes;
/*     */   }
/*     */ 
/*     */   protected JavacTypes(Context paramContext)
/*     */   {
/*  65 */     setContext(paramContext);
/*     */   }
/*     */ 
/*     */   public void setContext(Context paramContext)
/*     */   {
/*  73 */     paramContext.put(JavacTypes.class, this);
/*  74 */     this.syms = Symtab.instance(paramContext);
/*  75 */     this.types = com.sun.tools.javac.code.Types.instance(paramContext);
/*     */   }
/*     */ 
/*     */   public Element asElement(TypeMirror paramTypeMirror) {
/*  79 */     switch (1.$SwitchMap$javax$lang$model$type$TypeKind[paramTypeMirror.getKind().ordinal()]) {
/*     */     case 1:
/*     */     case 2:
/*     */     case 3:
/*     */     case 4:
/*  84 */       Type localType = (Type)cast(Type.class, paramTypeMirror);
/*  85 */       return localType.asElement();
/*     */     }
/*  87 */     return null;
/*     */   }
/*     */ 
/*     */   public boolean isSameType(TypeMirror paramTypeMirror1, TypeMirror paramTypeMirror2)
/*     */   {
/*  92 */     return this.types.isSameType((Type)paramTypeMirror1, (Type)paramTypeMirror2);
/*     */   }
/*     */ 
/*     */   public boolean isSubtype(TypeMirror paramTypeMirror1, TypeMirror paramTypeMirror2) {
/*  96 */     validateTypeNotIn(paramTypeMirror1, EXEC_OR_PKG);
/*  97 */     validateTypeNotIn(paramTypeMirror2, EXEC_OR_PKG);
/*  98 */     return this.types.isSubtype((Type)paramTypeMirror1, (Type)paramTypeMirror2);
/*     */   }
/*     */ 
/*     */   public boolean isAssignable(TypeMirror paramTypeMirror1, TypeMirror paramTypeMirror2) {
/* 102 */     validateTypeNotIn(paramTypeMirror1, EXEC_OR_PKG);
/* 103 */     validateTypeNotIn(paramTypeMirror2, EXEC_OR_PKG);
/* 104 */     return this.types.isAssignable((Type)paramTypeMirror1, (Type)paramTypeMirror2);
/*     */   }
/*     */ 
/*     */   public boolean contains(TypeMirror paramTypeMirror1, TypeMirror paramTypeMirror2) {
/* 108 */     validateTypeNotIn(paramTypeMirror1, EXEC_OR_PKG);
/* 109 */     validateTypeNotIn(paramTypeMirror2, EXEC_OR_PKG);
/* 110 */     return this.types.containsType((Type)paramTypeMirror1, (Type)paramTypeMirror2);
/*     */   }
/*     */ 
/*     */   public boolean isSubsignature(ExecutableType paramExecutableType1, ExecutableType paramExecutableType2) {
/* 114 */     return this.types.isSubSignature((Type)paramExecutableType1, (Type)paramExecutableType2);
/*     */   }
/*     */ 
/*     */   public java.util.List<Type> directSupertypes(TypeMirror paramTypeMirror) {
/* 118 */     validateTypeNotIn(paramTypeMirror, EXEC_OR_PKG);
/* 119 */     return this.types.directSupertypes((Type)paramTypeMirror);
/*     */   }
/*     */ 
/*     */   public TypeMirror erasure(TypeMirror paramTypeMirror) {
/* 123 */     if (paramTypeMirror.getKind() == TypeKind.PACKAGE)
/* 124 */       throw new IllegalArgumentException(paramTypeMirror.toString());
/* 125 */     return this.types.erasure((Type)paramTypeMirror);
/*     */   }
/*     */ 
/*     */   public TypeElement boxedClass(PrimitiveType paramPrimitiveType) {
/* 129 */     return this.types.boxedClass((Type)paramPrimitiveType);
/*     */   }
/*     */ 
/*     */   public PrimitiveType unboxedType(TypeMirror paramTypeMirror) {
/* 133 */     if (paramTypeMirror.getKind() != TypeKind.DECLARED)
/* 134 */       throw new IllegalArgumentException(paramTypeMirror.toString());
/* 135 */     Type localType = this.types.unboxedType((Type)paramTypeMirror);
/* 136 */     if (!localType.isPrimitive())
/* 137 */       throw new IllegalArgumentException(paramTypeMirror.toString());
/* 138 */     return (PrimitiveType)localType;
/*     */   }
/*     */ 
/*     */   public TypeMirror capture(TypeMirror paramTypeMirror) {
/* 142 */     validateTypeNotIn(paramTypeMirror, EXEC_OR_PKG);
/* 143 */     return this.types.capture((Type)paramTypeMirror);
/*     */   }
/*     */ 
/*     */   public PrimitiveType getPrimitiveType(TypeKind paramTypeKind) {
/* 147 */     switch (1.$SwitchMap$javax$lang$model$type$TypeKind[paramTypeKind.ordinal()]) { case 5:
/* 148 */       return this.syms.booleanType;
/*     */     case 6:
/* 149 */       return this.syms.byteType;
/*     */     case 7:
/* 150 */       return this.syms.shortType;
/*     */     case 8:
/* 151 */       return this.syms.intType;
/*     */     case 9:
/* 152 */       return this.syms.longType;
/*     */     case 10:
/* 153 */       return this.syms.charType;
/*     */     case 11:
/* 154 */       return this.syms.floatType;
/*     */     case 12:
/* 155 */       return this.syms.doubleType;
/*     */     }
/* 157 */     throw new IllegalArgumentException("Not a primitive type: " + paramTypeKind);
/*     */   }
/*     */ 
/*     */   public NullType getNullType()
/*     */   {
/* 162 */     return (NullType)this.syms.botType;
/*     */   }
/*     */ 
/*     */   public NoType getNoType(TypeKind paramTypeKind) {
/* 166 */     switch (1.$SwitchMap$javax$lang$model$type$TypeKind[paramTypeKind.ordinal()]) { case 13:
/* 167 */       return this.syms.voidType;
/*     */     case 14:
/* 168 */       return Type.noType;
/*     */     }
/* 170 */     throw new IllegalArgumentException(paramTypeKind.toString());
/*     */   }
/*     */ 
/*     */   public ArrayType getArrayType(TypeMirror paramTypeMirror)
/*     */   {
/* 175 */     switch (1.$SwitchMap$javax$lang$model$type$TypeKind[paramTypeMirror.getKind().ordinal()]) {
/*     */     case 13:
/*     */     case 15:
/*     */     case 16:
/*     */     case 17:
/* 180 */       throw new IllegalArgumentException(paramTypeMirror.toString());
/*     */     case 14:
/* 182 */     }return new Type.ArrayType((Type)paramTypeMirror, this.syms.arrayClass);
/*     */   }
/*     */ 
/*     */   public WildcardType getWildcardType(TypeMirror paramTypeMirror1, TypeMirror paramTypeMirror2)
/*     */   {
/*     */     BoundKind localBoundKind;
/*     */     Type localType;
/* 189 */     if ((paramTypeMirror1 == null) && (paramTypeMirror2 == null)) {
/* 190 */       localBoundKind = BoundKind.UNBOUND;
/* 191 */       localType = this.syms.objectType;
/* 192 */     } else if (paramTypeMirror2 == null) {
/* 193 */       localBoundKind = BoundKind.EXTENDS;
/* 194 */       localType = (Type)paramTypeMirror1;
/* 195 */     } else if (paramTypeMirror1 == null) {
/* 196 */       localBoundKind = BoundKind.SUPER;
/* 197 */       localType = (Type)paramTypeMirror2;
/*     */     } else {
/* 199 */       throw new IllegalArgumentException("Extends and super bounds cannot both be provided");
/*     */     }
/*     */ 
/* 202 */     switch (1.$SwitchMap$javax$lang$model$type$TypeKind[localType.getKind().ordinal()]) {
/*     */     case 1:
/*     */     case 3:
/*     */     case 4:
/*     */     case 18:
/* 207 */       return new Type.WildcardType(localType, localBoundKind, this.syms.boundClass);
/*     */     }
/* 209 */     throw new IllegalArgumentException(localType.toString());
/*     */   }
/*     */ 
/*     */   public DeclaredType getDeclaredType(TypeElement paramTypeElement, TypeMirror[] paramArrayOfTypeMirror)
/*     */   {
/* 215 */     Symbol.ClassSymbol localClassSymbol = (Symbol.ClassSymbol)paramTypeElement;
/*     */ 
/* 217 */     if (paramArrayOfTypeMirror.length == 0)
/* 218 */       return (DeclaredType)localClassSymbol.erasure(this.types);
/* 219 */     if (localClassSymbol.type.getEnclosingType().isParameterized()) {
/* 220 */       throw new IllegalArgumentException(localClassSymbol.toString());
/*     */     }
/* 222 */     return getDeclaredType0(localClassSymbol.type.getEnclosingType(), localClassSymbol, paramArrayOfTypeMirror);
/*     */   }
/*     */ 
/*     */   public DeclaredType getDeclaredType(DeclaredType paramDeclaredType, TypeElement paramTypeElement, TypeMirror[] paramArrayOfTypeMirror)
/*     */   {
/* 228 */     if (paramDeclaredType == null) {
/* 229 */       return getDeclaredType(paramTypeElement, paramArrayOfTypeMirror);
/*     */     }
/* 231 */     Symbol.ClassSymbol localClassSymbol = (Symbol.ClassSymbol)paramTypeElement;
/* 232 */     Type localType = (Type)paramDeclaredType;
/*     */ 
/* 234 */     if (localType.tsym != localClassSymbol.owner.enclClass())
/* 235 */       throw new IllegalArgumentException(paramDeclaredType.toString());
/* 236 */     if (!localType.isParameterized()) {
/* 237 */       return getDeclaredType(paramTypeElement, paramArrayOfTypeMirror);
/*     */     }
/* 239 */     return getDeclaredType0(localType, localClassSymbol, paramArrayOfTypeMirror);
/*     */   }
/*     */ 
/*     */   private DeclaredType getDeclaredType0(Type paramType, Symbol.ClassSymbol paramClassSymbol, TypeMirror[] paramArrayOfTypeMirror)
/*     */   {
/* 245 */     if (paramArrayOfTypeMirror.length != paramClassSymbol.type.getTypeArguments().length()) {
/* 246 */       throw new IllegalArgumentException("Incorrect number of type arguments");
/*     */     }
/*     */ 
/* 249 */     ListBuffer localListBuffer = new ListBuffer();
/* 250 */     for (TypeMirror localTypeMirror : paramArrayOfTypeMirror) {
/* 251 */       if ((!(localTypeMirror instanceof ReferenceType)) && (!(localTypeMirror instanceof WildcardType)))
/* 252 */         throw new IllegalArgumentException(localTypeMirror.toString());
/* 253 */       localListBuffer.append((Type)localTypeMirror);
/*     */     }
/*     */ 
/* 257 */     return new Type.ClassType(paramType, localListBuffer.toList(), paramClassSymbol);
/*     */   }
/*     */ 
/*     */   public TypeMirror asMemberOf(DeclaredType paramDeclaredType, Element paramElement)
/*     */   {
/* 275 */     Type localType = (Type)paramDeclaredType;
/* 276 */     Symbol localSymbol = (Symbol)paramElement;
/* 277 */     if (this.types.asSuper(localType, localSymbol.getEnclosingElement()) == null)
/* 278 */       throw new IllegalArgumentException(localSymbol + "@" + localType);
/* 279 */     return this.types.memberType(localType, localSymbol);
/*     */   }
/*     */ 
/*     */   private void validateTypeNotIn(TypeMirror paramTypeMirror, Set<TypeKind> paramSet)
/*     */   {
/* 290 */     if (paramSet.contains(paramTypeMirror.getKind()))
/* 291 */       throw new IllegalArgumentException(paramTypeMirror.toString());
/*     */   }
/*     */ 
/*     */   private static <T> T cast(Class<T> paramClass, Object paramObject)
/*     */   {
/* 300 */     if (!paramClass.isInstance(paramObject))
/* 301 */       throw new IllegalArgumentException(paramObject.toString());
/* 302 */     return paramClass.cast(paramObject);
/*     */   }
/*     */ 
/*     */   public Set<Symbol.MethodSymbol> getOverriddenMethods(Element paramElement) {
/* 306 */     if ((paramElement.getKind() != ElementKind.METHOD) || 
/* 307 */       (paramElement
/* 307 */       .getModifiers().contains(Modifier.STATIC)) || 
/* 308 */       (paramElement
/* 308 */       .getModifiers().contains(Modifier.PRIVATE))) {
/* 309 */       return Collections.emptySet();
/*     */     }
/* 311 */     if (!(paramElement instanceof Symbol.MethodSymbol)) {
/* 312 */       throw new IllegalArgumentException();
/*     */     }
/* 314 */     Symbol.MethodSymbol localMethodSymbol = (Symbol.MethodSymbol)paramElement;
/* 315 */     Symbol.ClassSymbol localClassSymbol1 = (Symbol.ClassSymbol)localMethodSymbol.owner;
/*     */ 
/* 317 */     LinkedHashSet localLinkedHashSet = new LinkedHashSet();
/* 318 */     for (Type localType : this.types.closure(localClassSymbol1.type)) {
/* 319 */       if (localType != localClassSymbol1.type) {
/* 320 */         Symbol.ClassSymbol localClassSymbol2 = (Symbol.ClassSymbol)localType.tsym;
/* 321 */         for (Scope.Entry localEntry = localClassSymbol2.members().lookup(localMethodSymbol.name); localEntry.scope != null; localEntry = localEntry.next()) {
/* 322 */           if ((localEntry.sym.kind == 16) && (localMethodSymbol.overrides(localEntry.sym, localClassSymbol1, this.types, true))) {
/* 323 */             localLinkedHashSet.add((Symbol.MethodSymbol)localEntry.sym);
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */ 
/* 329 */     return localLinkedHashSet;
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.javac.model.JavacTypes
 * JD-Core Version:    0.6.2
 */