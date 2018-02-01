/*     */ package com.sun.tools.javac.code;
/*     */ 
/*     */ import com.sun.tools.javac.util.Assert;
/*     */ import com.sun.tools.javac.util.Constants;
/*     */ import com.sun.tools.javac.util.List;
/*     */ import com.sun.tools.javac.util.Name;
/*     */ import com.sun.tools.javac.util.Name.Table;
/*     */ import com.sun.tools.javac.util.Names;
/*     */ import com.sun.tools.javac.util.Pair;
/*     */ import java.util.LinkedHashMap;
/*     */ import java.util.Map;
/*     */ import javax.lang.model.element.AnnotationMirror;
/*     */ import javax.lang.model.element.AnnotationValue;
/*     */ import javax.lang.model.element.AnnotationValueVisitor;
/*     */ import javax.lang.model.type.DeclaredType;
/*     */ 
/*     */ public abstract class Attribute
/*     */   implements AnnotationValue
/*     */ {
/*     */   public Type type;
/*     */ 
/*     */   public Attribute(Type paramType)
/*     */   {
/*  50 */     this.type = paramType;
/*     */   }
/*     */ 
/*     */   public abstract void accept(Visitor paramVisitor);
/*     */ 
/*     */   public Object getValue() {
/*  56 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */   public <R, P> R accept(AnnotationValueVisitor<R, P> paramAnnotationValueVisitor, P paramP) {
/*  60 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */   public boolean isSynthesized() {
/*  64 */     return false;
/*     */   }
/*     */   public TypeAnnotationPosition getPosition() {
/*  67 */     return null;
/*     */   }
/*     */ 
/*     */   public static class Array extends Attribute
/*     */   {
/*     */     public final Attribute[] values;
/*     */ 
/*     */     public Array(Type paramType, Attribute[] paramArrayOfAttribute)
/*     */     {
/* 302 */       super();
/* 303 */       this.values = paramArrayOfAttribute;
/*     */     }
/*     */ 
/*     */     public Array(Type paramType, List<Attribute> paramList) {
/* 307 */       super();
/* 308 */       this.values = ((Attribute[])paramList.toArray(new Attribute[paramList.size()]));
/*     */     }
/*     */     public void accept(Attribute.Visitor paramVisitor) {
/* 311 */       paramVisitor.visitArray(this);
/*     */     }
/* 313 */     public String toString() { StringBuilder localStringBuilder = new StringBuilder();
/* 314 */       localStringBuilder.append('{');
/* 315 */       int i = 1;
/* 316 */       for (Attribute localAttribute : this.values) {
/* 317 */         if (i == 0)
/* 318 */           localStringBuilder.append(", ");
/* 319 */         i = 0;
/* 320 */         localStringBuilder.append(localAttribute);
/*     */       }
/* 322 */       localStringBuilder.append('}');
/* 323 */       return localStringBuilder.toString(); }
/*     */ 
/*     */     public List<Attribute> getValue() {
/* 326 */       return List.from(this.values);
/*     */     }
/*     */     public <R, P> R accept(AnnotationValueVisitor<R, P> paramAnnotationValueVisitor, P paramP) {
/* 329 */       return paramAnnotationValueVisitor.visitArray(getValue(), paramP);
/*     */     }
/*     */ 
/*     */     public TypeAnnotationPosition getPosition()
/*     */     {
/* 334 */       if (this.values.length != 0) {
/* 335 */         return this.values[0].getPosition();
/*     */       }
/* 337 */       return null;
/*     */     }
/*     */   }
/*     */ 
/*     */   public static class Class extends Attribute
/*     */   {
/*     */     public final Type classType;
/*     */ 
/*     */     public void accept(Attribute.Visitor paramVisitor)
/*     */     {
/* 110 */       paramVisitor.visitClass(this);
/*     */     }
/* 112 */     public Class(Types paramTypes, Type paramType) { super();
/* 113 */       this.classType = paramType;
/*     */     }
/*     */ 
/*     */     static Type makeClassType(Types paramTypes, Type paramType)
/*     */     {
/* 118 */       Type localType = paramType.isPrimitive() ? paramTypes
/* 117 */         .boxedClass(paramType).type : 
/* 117 */         paramTypes
/* 118 */         .erasure(paramType);
/*     */ 
/* 120 */       return new Type.ClassType(paramTypes.syms.classType.getEnclosingType(), 
/* 120 */         List.of(localType), 
/* 120 */         paramTypes.syms.classType.tsym);
/*     */     }
/*     */ 
/*     */     public String toString() {
/* 124 */       return this.classType + ".class";
/*     */     }
/*     */     public Type getValue() {
/* 127 */       return this.classType;
/*     */     }
/*     */     public <R, P> R accept(AnnotationValueVisitor<R, P> paramAnnotationValueVisitor, P paramP) {
/* 130 */       return paramAnnotationValueVisitor.visitType(this.classType, paramP);
/*     */     }
/*     */   }
/*     */ 
/*     */   public static class Compound extends Attribute
/*     */     implements AnnotationMirror
/*     */   {
/*     */     public final List<Pair<Symbol.MethodSymbol, Attribute>> values;
/* 145 */     private boolean synthesized = false;
/*     */ 
/*     */     public boolean isSynthesized()
/*     */     {
/* 149 */       return this.synthesized;
/*     */     }
/*     */ 
/*     */     public void setSynthesized(boolean paramBoolean) {
/* 153 */       this.synthesized = paramBoolean;
/*     */     }
/*     */ 
/*     */     public Compound(Type paramType, List<Pair<Symbol.MethodSymbol, Attribute>> paramList)
/*     */     {
/* 158 */       super();
/* 159 */       this.values = paramList;
/*     */     }
/* 161 */     public void accept(Attribute.Visitor paramVisitor) { paramVisitor.visitCompound(this); }
/*     */ 
/*     */ 
/*     */     public String toString()
/*     */     {
/* 172 */       StringBuilder localStringBuilder = new StringBuilder();
/* 173 */       localStringBuilder.append("@");
/* 174 */       localStringBuilder.append(this.type);
/* 175 */       int i = this.values.length();
/* 176 */       if (i > 0) {
/* 177 */         localStringBuilder.append('(');
/* 178 */         int j = 1;
/* 179 */         for (Pair localPair : this.values) {
/* 180 */           if (j == 0) localStringBuilder.append(", ");
/* 181 */           j = 0;
/*     */ 
/* 183 */           Name localName = ((Symbol.MethodSymbol)localPair.fst).name;
/* 184 */           if ((i > 1) || (localName != localName.table.names.value)) {
/* 185 */             localStringBuilder.append(localName);
/* 186 */             localStringBuilder.append('=');
/*     */           }
/* 188 */           localStringBuilder.append(localPair.snd);
/*     */         }
/* 190 */         localStringBuilder.append(')');
/*     */       }
/* 192 */       return localStringBuilder.toString();
/*     */     }
/*     */ 
/*     */     public Attribute member(Name paramName) {
/* 196 */       Pair localPair = getElemPair(paramName);
/* 197 */       return localPair == null ? null : (Attribute)localPair.snd;
/*     */     }
/*     */ 
/*     */     private Pair<Symbol.MethodSymbol, Attribute> getElemPair(Name paramName) {
/* 201 */       for (Pair localPair : this.values)
/* 202 */         if (((Symbol.MethodSymbol)localPair.fst).name == paramName) return localPair;
/* 203 */       return null;
/*     */     }
/*     */ 
/*     */     public Compound getValue() {
/* 207 */       return this;
/*     */     }
/*     */ 
/*     */     public <R, P> R accept(AnnotationValueVisitor<R, P> paramAnnotationValueVisitor, P paramP) {
/* 211 */       return paramAnnotationValueVisitor.visitAnnotation(this, paramP);
/*     */     }
/*     */ 
/*     */     public DeclaredType getAnnotationType() {
/* 215 */       return (DeclaredType)this.type;
/*     */     }
/*     */ 
/*     */     public TypeAnnotationPosition getPosition()
/*     */     {
/* 220 */       if (this.values.size() != 0) {
/* 221 */         Name localName = ((Symbol.MethodSymbol)((Pair)this.values.head).fst).name.table.names.value;
/* 222 */         Pair localPair = getElemPair(localName);
/* 223 */         return localPair == null ? null : ((Attribute)localPair.snd).getPosition();
/*     */       }
/* 225 */       return null;
/*     */     }
/*     */ 
/*     */     public Map<Symbol.MethodSymbol, Attribute> getElementValues() {
/* 229 */       LinkedHashMap localLinkedHashMap = new LinkedHashMap();
/*     */ 
/* 231 */       for (Pair localPair : this.values)
/* 232 */         localLinkedHashMap.put(localPair.fst, localPair.snd);
/* 233 */       return localLinkedHashMap;
/*     */     }
/*     */   }
/*     */ 
/*     */   public static class Constant extends Attribute
/*     */   {
/*     */     public final Object value;
/*     */ 
/*     */     public void accept(Attribute.Visitor paramVisitor)
/*     */     {
/*  72 */       paramVisitor.visitConstant(this);
/*     */     }
/*  74 */     public Constant(Type paramType, Object paramObject) { super();
/*  75 */       this.value = paramObject; }
/*     */ 
/*     */     public String toString() {
/*  78 */       return Constants.format(this.value, this.type);
/*     */     }
/*     */     public Object getValue() {
/*  81 */       return Constants.decode(this.value, this.type);
/*     */     }
/*     */     public <R, P> R accept(AnnotationValueVisitor<R, P> paramAnnotationValueVisitor, P paramP) {
/*  84 */       if ((this.value instanceof String))
/*  85 */         return paramAnnotationValueVisitor.visitString((String)this.value, paramP);
/*  86 */       if ((this.value instanceof Integer)) {
/*  87 */         int i = ((Integer)this.value).intValue();
/*  88 */         switch (Attribute.1.$SwitchMap$com$sun$tools$javac$code$TypeTag[this.type.getTag().ordinal()]) { case 1:
/*  89 */           return paramAnnotationValueVisitor.visitBoolean(i != 0, paramP);
/*     */         case 2:
/*  90 */           return paramAnnotationValueVisitor.visitChar((char)i, paramP);
/*     */         case 3:
/*  91 */           return paramAnnotationValueVisitor.visitByte((byte)i, paramP);
/*     */         case 4:
/*  92 */           return paramAnnotationValueVisitor.visitShort((short)i, paramP);
/*     */         case 5:
/*  93 */           return paramAnnotationValueVisitor.visitInt(i, paramP);
/*     */         }
/*     */       }
/*  96 */       switch (Attribute.1.$SwitchMap$com$sun$tools$javac$code$TypeTag[this.type.getTag().ordinal()]) { case 6:
/*  97 */         return paramAnnotationValueVisitor.visitLong(((Long)this.value).longValue(), paramP);
/*     */       case 7:
/*  98 */         return paramAnnotationValueVisitor.visitFloat(((Float)this.value).floatValue(), paramP);
/*     */       case 8:
/*  99 */         return paramAnnotationValueVisitor.visitDouble(((Double)this.value).doubleValue(), paramP);
/*     */       }
/* 101 */       throw new AssertionError("Bad annotation element value: " + this.value);
/*     */     }
/*     */   }
/*     */ 
/*     */   public static class Enum extends Attribute
/*     */   {
/*     */     public Symbol.VarSymbol value;
/*     */ 
/*     */     public Enum(Type paramType, Symbol.VarSymbol paramVarSymbol)
/*     */     {
/* 346 */       super();
/* 347 */       this.value = ((Symbol.VarSymbol)Assert.checkNonNull(paramVarSymbol));
/*     */     }
/* 349 */     public void accept(Attribute.Visitor paramVisitor) { paramVisitor.visitEnum(this); } 
/*     */     public String toString() {
/* 351 */       return this.value.enclClass() + "." + this.value;
/*     */     }
/*     */     public Symbol.VarSymbol getValue() {
/* 354 */       return this.value;
/*     */     }
/*     */     public <R, P> R accept(AnnotationValueVisitor<R, P> paramAnnotationValueVisitor, P paramP) {
/* 357 */       return paramAnnotationValueVisitor.visitEnumConstant(this.value, paramP);
/*     */     }
/*     */   }
/*     */ 
/*     */   public static class Error extends Attribute {
/*     */     public Error(Type paramType) {
/* 363 */       super();
/*     */     }
/* 365 */     public void accept(Attribute.Visitor paramVisitor) { paramVisitor.visitError(this); } 
/*     */     public String toString() {
/* 367 */       return "<error>";
/*     */     }
/*     */     public String getValue() {
/* 370 */       return toString();
/*     */     }
/*     */     public <R, P> R accept(AnnotationValueVisitor<R, P> paramAnnotationValueVisitor, P paramP) {
/* 373 */       return paramAnnotationValueVisitor.visitString(toString(), paramP);
/*     */     }
/*     */   }
/*     */ 
/*     */   public static enum RetentionPolicy
/*     */   {
/* 397 */     SOURCE, 
/* 398 */     CLASS, 
/* 399 */     RUNTIME;
/*     */   }
/*     */ 
/*     */   public static class TypeCompound extends Attribute.Compound
/*     */   {
/*     */     public TypeAnnotationPosition position;
/*     */ 
/*     */     public TypeCompound(Attribute.Compound paramCompound, TypeAnnotationPosition paramTypeAnnotationPosition)
/*     */     {
/* 242 */       this(paramCompound.type, paramCompound.values, paramTypeAnnotationPosition);
/*     */     }
/*     */ 
/*     */     public TypeCompound(Type paramType, List<Pair<Symbol.MethodSymbol, Attribute>> paramList, TypeAnnotationPosition paramTypeAnnotationPosition)
/*     */     {
/* 247 */       super(paramList);
/* 248 */       this.position = paramTypeAnnotationPosition;
/*     */     }
/*     */ 
/*     */     public TypeAnnotationPosition getPosition()
/*     */     {
/* 253 */       if (hasUnknownPosition()) {
/* 254 */         this.position = super.getPosition();
/*     */       }
/* 256 */       return this.position;
/*     */     }
/*     */ 
/*     */     public boolean hasUnknownPosition() {
/* 260 */       return this.position.type == TargetType.UNKNOWN;
/*     */     }
/*     */ 
/*     */     public boolean isContainerTypeCompound() {
/* 264 */       if ((isSynthesized()) && (this.values.size() == 1))
/* 265 */         return getFirstEmbeddedTC() != null;
/* 266 */       return false;
/*     */     }
/*     */ 
/*     */     private TypeCompound getFirstEmbeddedTC() {
/* 270 */       if (this.values.size() == 1) {
/* 271 */         Pair localPair = (Pair)this.values.get(0);
/* 272 */         if ((((Symbol.MethodSymbol)localPair.fst).getSimpleName().contentEquals("value")) && ((localPair.snd instanceof Attribute.Array)))
/*     */         {
/* 274 */           Attribute.Array localArray = (Attribute.Array)localPair.snd;
/* 275 */           if ((localArray.values.length != 0) && ((localArray.values[0] instanceof TypeCompound)))
/*     */           {
/* 277 */             return (TypeCompound)localArray.values[0];
/*     */           }
/*     */         }
/*     */       }
/* 280 */       return null;
/*     */     }
/*     */ 
/*     */     public boolean tryFixPosition() {
/* 284 */       if (!isContainerTypeCompound()) {
/* 285 */         return false;
/*     */       }
/* 287 */       TypeCompound localTypeCompound = getFirstEmbeddedTC();
/* 288 */       if ((localTypeCompound != null) && (localTypeCompound.position != null) && (localTypeCompound.position.type != TargetType.UNKNOWN))
/*     */       {
/* 290 */         this.position = localTypeCompound.position;
/* 291 */         return true;
/*     */       }
/* 293 */       return false;
/*     */     }
/*     */   }
/*     */ 
/*     */   public static class UnresolvedClass extends Attribute.Error
/*     */   {
/*     */     public Type classType;
/*     */ 
/*     */     public UnresolvedClass(Type paramType1, Type paramType2)
/*     */     {
/* 380 */       super();
/* 381 */       this.classType = paramType2;
/*     */     }
/*     */   }
/*     */ 
/*     */   public static abstract interface Visitor
/*     */   {
/*     */     public abstract void visitConstant(Attribute.Constant paramConstant);
/*     */ 
/*     */     public abstract void visitClass(Attribute.Class paramClass);
/*     */ 
/*     */     public abstract void visitCompound(Attribute.Compound paramCompound);
/*     */ 
/*     */     public abstract void visitArray(Attribute.Array paramArray);
/*     */ 
/*     */     public abstract void visitEnum(Attribute.Enum paramEnum);
/*     */ 
/*     */     public abstract void visitError(Attribute.Error paramError);
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.javac.code.Attribute
 * JD-Core Version:    0.6.2
 */