/*     */ package com.sun.tools.javac.jvm;
/*     */ 
/*     */ import com.sun.tools.javac.code.Symbol;
/*     */ import com.sun.tools.javac.code.Symbol.DelegatedSymbol;
/*     */ import com.sun.tools.javac.code.Symbol.DynamicMethodSymbol;
/*     */ import com.sun.tools.javac.code.Symbol.MethodSymbol;
/*     */ import com.sun.tools.javac.code.Symbol.VarSymbol;
/*     */ import com.sun.tools.javac.code.Type;
/*     */ import com.sun.tools.javac.code.Types;
/*     */ import com.sun.tools.javac.code.Types.UniqueType;
/*     */ import com.sun.tools.javac.util.ArrayUtils;
/*     */ import com.sun.tools.javac.util.Assert;
/*     */ import com.sun.tools.javac.util.Filter;
/*     */ import com.sun.tools.javac.util.Name;
/*     */ import com.sun.tools.javac.util.Name.Table;
/*     */ import com.sun.tools.javac.util.Names;
/*     */ import java.util.Arrays;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ 
/*     */ public class Pool
/*     */ {
/*     */   public static final int MAX_ENTRIES = 65535;
/*     */   public static final int MAX_STRING_LENGTH = 65535;
/*     */   int pp;
/*     */   Object[] pool;
/*     */   Map<Object, Integer> indices;
/*     */   Types types;
/*     */ 
/*     */   public Pool(int paramInt, Object[] paramArrayOfObject, Types paramTypes)
/*     */   {
/*  71 */     this.pp = paramInt;
/*  72 */     this.pool = paramArrayOfObject;
/*  73 */     this.types = paramTypes;
/*  74 */     this.indices = new HashMap(paramArrayOfObject.length);
/*  75 */     for (int i = 1; i < paramInt; i++)
/*  76 */       if (paramArrayOfObject[i] != null) this.indices.put(paramArrayOfObject[i], Integer.valueOf(i));
/*     */   }
/*     */ 
/*     */   public Pool(Types paramTypes)
/*     */   {
/*  83 */     this(1, new Object[64], paramTypes);
/*     */   }
/*     */ 
/*     */   public int numEntries()
/*     */   {
/*  89 */     return this.pp;
/*     */   }
/*     */ 
/*     */   public void reset()
/*     */   {
/*  95 */     this.pp = 1;
/*  96 */     this.indices.clear();
/*     */   }
/*     */ 
/*     */   public int put(Object paramObject)
/*     */   {
/* 104 */     paramObject = makePoolValue(paramObject);
/*     */ 
/* 106 */     Integer localInteger = (Integer)this.indices.get(paramObject);
/* 107 */     if (localInteger == null)
/*     */     {
/* 109 */       localInteger = Integer.valueOf(this.pp);
/* 110 */       this.indices.put(paramObject, localInteger);
/* 111 */       this.pool = ArrayUtils.ensureCapacity(this.pool, this.pp);
/* 112 */       this.pool[(this.pp++)] = paramObject;
/* 113 */       if (((paramObject instanceof Long)) || ((paramObject instanceof Double))) {
/* 114 */         this.pool = ArrayUtils.ensureCapacity(this.pool, this.pp);
/* 115 */         this.pool[(this.pp++)] = null;
/*     */       }
/*     */     }
/* 118 */     return localInteger.intValue();
/*     */   }
/*     */ 
/*     */   Object makePoolValue(Object paramObject) {
/* 122 */     if ((paramObject instanceof Symbol.DynamicMethodSymbol))
/* 123 */       return new DynamicMethod((Symbol.DynamicMethodSymbol)paramObject, this.types);
/* 124 */     if ((paramObject instanceof Symbol.MethodSymbol))
/* 125 */       return new Method((Symbol.MethodSymbol)paramObject, this.types);
/* 126 */     if ((paramObject instanceof Symbol.VarSymbol))
/* 127 */       return new Variable((Symbol.VarSymbol)paramObject, this.types);
/* 128 */     if ((paramObject instanceof Type)) {
/* 129 */       return new Types.UniqueType((Type)paramObject, this.types);
/*     */     }
/* 131 */     return paramObject;
/*     */   }
/*     */ 
/*     */   public int get(Object paramObject)
/*     */   {
/* 139 */     Integer localInteger = (Integer)this.indices.get(paramObject);
/* 140 */     return localInteger == null ? -1 : localInteger.intValue();
/*     */   }
/*     */ 
/*     */   static class DynamicMethod extends Pool.Method
/*     */   {
/*     */     public Object[] uniqueStaticArgs;
/*     */ 
/*     */     DynamicMethod(Symbol.DynamicMethodSymbol paramDynamicMethodSymbol, Types paramTypes)
/*     */     {
/* 171 */       super(paramTypes);
/* 172 */       this.uniqueStaticArgs = getUniqueTypeArray(paramDynamicMethodSymbol.staticArgs, paramTypes);
/*     */     }
/*     */ 
/*     */     public boolean equals(Object paramObject)
/*     */     {
/* 177 */       if (!super.equals(paramObject)) return false;
/* 178 */       if (!(paramObject instanceof DynamicMethod)) return false;
/* 179 */       Symbol.DynamicMethodSymbol localDynamicMethodSymbol1 = (Symbol.DynamicMethodSymbol)this.other;
/* 180 */       Symbol.DynamicMethodSymbol localDynamicMethodSymbol2 = (Symbol.DynamicMethodSymbol)((DynamicMethod)paramObject).other;
/* 181 */       if ((localDynamicMethodSymbol1.bsm == localDynamicMethodSymbol2.bsm) && (localDynamicMethodSymbol1.bsmKind == localDynamicMethodSymbol2.bsmKind));
/* 183 */       return Arrays.equals(this.uniqueStaticArgs, ((DynamicMethod)paramObject).uniqueStaticArgs);
/*     */     }
/*     */ 
/*     */     public int hashCode()
/*     */     {
/* 189 */       int i = super.hashCode();
/* 190 */       Symbol.DynamicMethodSymbol localDynamicMethodSymbol = (Symbol.DynamicMethodSymbol)this.other;
/*     */ 
/* 192 */       i = i + (localDynamicMethodSymbol.bsmKind * 7 + localDynamicMethodSymbol.bsm
/* 192 */         .hashCode() * 11);
/* 193 */       for (int j = 0; j < localDynamicMethodSymbol.staticArgs.length; j++) {
/* 194 */         i += this.uniqueStaticArgs[j].hashCode() * 23;
/*     */       }
/* 196 */       return i;
/*     */     }
/*     */ 
/*     */     private Object[] getUniqueTypeArray(Object[] paramArrayOfObject, Types paramTypes) {
/* 200 */       Object[] arrayOfObject = new Object[paramArrayOfObject.length];
/* 201 */       for (int i = 0; i < paramArrayOfObject.length; i++) {
/* 202 */         if ((paramArrayOfObject[i] instanceof Type))
/* 203 */           arrayOfObject[i] = new Types.UniqueType((Type)paramArrayOfObject[i], paramTypes);
/*     */         else {
/* 205 */           arrayOfObject[i] = paramArrayOfObject[i];
/*     */         }
/*     */       }
/* 208 */       return arrayOfObject;
/*     */     }
/*     */   }
/*     */ 
/*     */   static class Method extends Symbol.DelegatedSymbol<Symbol.MethodSymbol>
/*     */   {
/*     */     Types.UniqueType uniqueType;
/*     */ 
/*     */     Method(Symbol.MethodSymbol paramMethodSymbol, Types paramTypes)
/*     */     {
/* 146 */       super();
/* 147 */       this.uniqueType = new Types.UniqueType(paramMethodSymbol.type, paramTypes);
/*     */     }
/*     */     public boolean equals(Object paramObject) {
/* 150 */       if (!(paramObject instanceof Method)) return false;
/* 151 */       Symbol.MethodSymbol localMethodSymbol1 = (Symbol.MethodSymbol)((Method)paramObject).other;
/* 152 */       Symbol.MethodSymbol localMethodSymbol2 = (Symbol.MethodSymbol)this.other;
/* 153 */       if ((localMethodSymbol1.name == localMethodSymbol2.name) && (localMethodSymbol1.owner == localMethodSymbol2.owner));
/* 156 */       return ((Method)paramObject).uniqueType
/* 156 */         .equals(this.uniqueType);
/*     */     }
/*     */ 
/*     */     public int hashCode()
/*     */     {
/* 159 */       Symbol.MethodSymbol localMethodSymbol = (Symbol.MethodSymbol)this.other;
/*     */ 
/* 163 */       return localMethodSymbol.name
/* 161 */         .hashCode() * 33 + localMethodSymbol.owner
/* 162 */         .hashCode() * 9 + this.uniqueType
/* 163 */         .hashCode();
/*     */     }
/*     */   }
/*     */ 
/*     */   public static class MethodHandle
/*     */   {
/*     */     int refKind;
/*     */     Symbol refSym;
/*     */     Types.UniqueType uniqueType;
/* 312 */     Filter<Name> nonInitFilter = new Filter() {
/*     */       public boolean accepts(Name paramAnonymousName) {
/* 314 */         return (paramAnonymousName != paramAnonymousName.table.names.init) && (paramAnonymousName != paramAnonymousName.table.names.clinit);
/*     */       }
/* 312 */     };
/*     */ 
/* 318 */     Filter<Name> initFilter = new Filter() {
/*     */       public boolean accepts(Name paramAnonymousName) {
/* 320 */         return paramAnonymousName == paramAnonymousName.table.names.init;
/*     */       }
/* 318 */     };
/*     */ 
/*     */     public MethodHandle(int paramInt, Symbol paramSymbol, Types paramTypes)
/*     */     {
/* 247 */       this.refKind = paramInt;
/* 248 */       this.refSym = paramSymbol;
/* 249 */       this.uniqueType = new Types.UniqueType(this.refSym.type, paramTypes);
/* 250 */       checkConsistent();
/*     */     }
/*     */     public boolean equals(Object paramObject) {
/* 253 */       if (!(paramObject instanceof MethodHandle)) return false;
/* 254 */       MethodHandle localMethodHandle = (MethodHandle)paramObject;
/* 255 */       if (localMethodHandle.refKind != this.refKind) return false;
/* 256 */       Symbol localSymbol = localMethodHandle.refSym;
/* 257 */       if ((localSymbol.name == this.refSym.name) && (localSymbol.owner == this.refSym.owner));
/* 260 */       return ((MethodHandle)paramObject).uniqueType
/* 260 */         .equals(this.uniqueType);
/*     */     }
/*     */ 
/*     */     public int hashCode()
/*     */     {
/* 267 */       return this.refKind * 65 + this.refSym.name
/* 265 */         .hashCode() * 33 + this.refSym.owner
/* 266 */         .hashCode() * 9 + this.uniqueType
/* 267 */         .hashCode();
/*     */     }
/*     */ 
/*     */     private void checkConsistent()
/*     */     {
/* 275 */       int i = 0;
/* 276 */       int j = -1;
/* 277 */       Filter localFilter = this.nonInitFilter;
/* 278 */       int k = 0;
/* 279 */       switch (this.refKind) {
/*     */       case 2:
/*     */       case 4:
/* 282 */         i = 1;
/*     */       case 1:
/*     */       case 3:
/* 285 */         j = 4;
/* 286 */         break;
/*     */       case 8:
/* 288 */         localFilter = this.initFilter;
/* 289 */         j = 16;
/* 290 */         break;
/*     */       case 9:
/* 292 */         k = 1;
/* 293 */         j = 16;
/* 294 */         break;
/*     */       case 6:
/* 296 */         k = 1;
/* 297 */         i = 1;
/*     */       case 5:
/* 299 */         j = 16;
/* 300 */         break;
/*     */       case 7:
/* 302 */         k = 1;
/* 303 */         j = 16;
/*     */       }
/*     */ 
/* 306 */       Assert.check((!this.refSym.isStatic()) || (i != 0));
/* 307 */       Assert.check(this.refSym.kind == j);
/* 308 */       Assert.check(localFilter.accepts(this.refSym.name));
/* 309 */       Assert.check((!this.refSym.owner.isInterface()) || (k != 0));
/*     */     }
/*     */   }
/*     */ 
/*     */   static class Variable extends Symbol.DelegatedSymbol<Symbol.VarSymbol>
/*     */   {
/*     */     Types.UniqueType uniqueType;
/*     */ 
/*     */     Variable(Symbol.VarSymbol paramVarSymbol, Types paramTypes)
/*     */     {
/* 215 */       super();
/* 216 */       this.uniqueType = new Types.UniqueType(paramVarSymbol.type, paramTypes);
/*     */     }
/*     */     public boolean equals(Object paramObject) {
/* 219 */       if (!(paramObject instanceof Variable)) return false;
/* 220 */       Symbol.VarSymbol localVarSymbol1 = (Symbol.VarSymbol)((Variable)paramObject).other;
/* 221 */       Symbol.VarSymbol localVarSymbol2 = (Symbol.VarSymbol)this.other;
/* 222 */       if ((localVarSymbol1.name == localVarSymbol2.name) && (localVarSymbol1.owner == localVarSymbol2.owner));
/* 225 */       return ((Variable)paramObject).uniqueType
/* 225 */         .equals(this.uniqueType);
/*     */     }
/*     */ 
/*     */     public int hashCode()
/*     */     {
/* 228 */       Symbol.VarSymbol localVarSymbol = (Symbol.VarSymbol)this.other;
/*     */ 
/* 232 */       return localVarSymbol.name
/* 230 */         .hashCode() * 33 + localVarSymbol.owner
/* 231 */         .hashCode() * 9 + this.uniqueType
/* 232 */         .hashCode();
/*     */     }
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.javac.jvm.Pool
 * JD-Core Version:    0.6.2
 */