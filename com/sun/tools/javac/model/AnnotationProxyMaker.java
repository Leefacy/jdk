/*     */ package com.sun.tools.javac.model;
/*     */ 
/*     */ import com.sun.tools.javac.code.Attribute;
/*     */ import com.sun.tools.javac.code.Attribute.Array;
/*     */ import com.sun.tools.javac.code.Attribute.Class;
/*     */ import com.sun.tools.javac.code.Attribute.Compound;
/*     */ import com.sun.tools.javac.code.Attribute.Constant;
/*     */ import com.sun.tools.javac.code.Attribute.Enum;
/*     */ import com.sun.tools.javac.code.Attribute.Error;
/*     */ import com.sun.tools.javac.code.Attribute.UnresolvedClass;
/*     */ import com.sun.tools.javac.code.Attribute.Visitor;
/*     */ import com.sun.tools.javac.code.Scope;
/*     */ import com.sun.tools.javac.code.Scope.Entry;
/*     */ import com.sun.tools.javac.code.Symbol;
/*     */ import com.sun.tools.javac.code.Symbol.ClassSymbol;
/*     */ import com.sun.tools.javac.code.Symbol.MethodSymbol;
/*     */ import com.sun.tools.javac.code.Symbol.TypeSymbol;
/*     */ import com.sun.tools.javac.code.Symbol.VarSymbol;
/*     */ import com.sun.tools.javac.code.Type;
/*     */ import com.sun.tools.javac.code.Type.ArrayType;
/*     */ import com.sun.tools.javac.util.List;
/*     */ import com.sun.tools.javac.util.ListBuffer;
/*     */ import com.sun.tools.javac.util.Name;
/*     */ import com.sun.tools.javac.util.Name.Table;
/*     */ import com.sun.tools.javac.util.Names;
/*     */ import com.sun.tools.javac.util.Pair;
/*     */ import java.io.IOException;
/*     */ import java.io.ObjectInputStream;
/*     */ import java.lang.annotation.Annotation;
/*     */ import java.lang.annotation.AnnotationTypeMismatchException;
/*     */ import java.lang.reflect.Array;
/*     */ import java.lang.reflect.Method;
/*     */ import java.util.Iterator;
/*     */ import java.util.LinkedHashMap;
/*     */ import java.util.Map;
/*     */ import java.util.Map.Entry;
/*     */ import javax.lang.model.type.MirroredTypeException;
/*     */ import javax.lang.model.type.MirroredTypesException;
/*     */ import javax.lang.model.type.TypeMirror;
/*     */ import sun.reflect.annotation.AnnotationParser;
/*     */ import sun.reflect.annotation.AnnotationType;
/*     */ import sun.reflect.annotation.EnumConstantNotPresentExceptionProxy;
/*     */ import sun.reflect.annotation.ExceptionProxy;
/*     */ 
/*     */ public class AnnotationProxyMaker
/*     */ {
/*     */   private final Attribute.Compound anno;
/*     */   private final Class<? extends Annotation> annoType;
/*     */ 
/*     */   private AnnotationProxyMaker(Attribute.Compound paramCompound, Class<? extends Annotation> paramClass)
/*     */   {
/*  68 */     this.anno = paramCompound;
/*  69 */     this.annoType = paramClass;
/*     */   }
/*     */ 
/*     */   public static <A extends Annotation> A generateAnnotation(Attribute.Compound paramCompound, Class<A> paramClass)
/*     */   {
/*  78 */     AnnotationProxyMaker localAnnotationProxyMaker = new AnnotationProxyMaker(paramCompound, paramClass);
/*  79 */     return (Annotation)paramClass.cast(localAnnotationProxyMaker.generateAnnotation());
/*     */   }
/*     */ 
/*     */   private Annotation generateAnnotation()
/*     */   {
/*  87 */     return AnnotationParser.annotationForMap(this.annoType, 
/*  88 */       getAllReflectedValues());
/*     */   }
/*     */ 
/*     */   private Map<String, Object> getAllReflectedValues()
/*     */   {
/*  97 */     LinkedHashMap localLinkedHashMap = new LinkedHashMap();
/*     */ 
/* 100 */     for (Map.Entry localEntry : getAllValues().entrySet()) {
/* 101 */       Symbol.MethodSymbol localMethodSymbol = (Symbol.MethodSymbol)localEntry.getKey();
/* 102 */       Object localObject = generateValue(localMethodSymbol, (Attribute)localEntry.getValue());
/* 103 */       if (localObject != null) {
/* 104 */         localLinkedHashMap.put(localMethodSymbol.name.toString(), localObject);
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 110 */     return localLinkedHashMap;
/*     */   }
/*     */ 
/*     */   private Map<Symbol.MethodSymbol, Attribute> getAllValues()
/*     */   {
/* 118 */     LinkedHashMap localLinkedHashMap = new LinkedHashMap();
/*     */ 
/* 122 */     Symbol.ClassSymbol localClassSymbol = (Symbol.ClassSymbol)this.anno.type.tsym;
/*     */     Object localObject2;
/* 123 */     for (Object localObject1 = localClassSymbol.members().elems; localObject1 != null; localObject1 = ((Scope.Entry)localObject1).sibling) {
/* 124 */       if (((Scope.Entry)localObject1).sym.kind == 16) {
/* 125 */         localObject2 = (Symbol.MethodSymbol)((Scope.Entry)localObject1).sym;
/* 126 */         Attribute localAttribute = ((Symbol.MethodSymbol)localObject2).getDefaultValue();
/* 127 */         if (localAttribute != null) {
/* 128 */           localLinkedHashMap.put(localObject2, localAttribute);
/*     */         }
/*     */       }
/*     */     }
/* 132 */     for (localObject1 = this.anno.values.iterator(); ((Iterator)localObject1).hasNext(); ) { localObject2 = (Pair)((Iterator)localObject1).next();
/* 133 */       localLinkedHashMap.put(((Pair)localObject2).fst, ((Pair)localObject2).snd); }
/* 134 */     return localLinkedHashMap;
/*     */   }
/*     */ 
/*     */   private Object generateValue(Symbol.MethodSymbol paramMethodSymbol, Attribute paramAttribute)
/*     */   {
/* 143 */     ValueVisitor localValueVisitor = new ValueVisitor(paramMethodSymbol);
/* 144 */     return localValueVisitor.getValue(paramAttribute);
/*     */   }
/*     */ 
/*     */   private static final class MirroredTypeExceptionProxy extends ExceptionProxy
/*     */   {
/*     */     static final long serialVersionUID = 269L;
/*     */     private transient TypeMirror type;
/*     */     private final String typeString;
/*     */ 
/*     */     MirroredTypeExceptionProxy(TypeMirror paramTypeMirror)
/*     */     {
/* 289 */       this.type = paramTypeMirror;
/* 290 */       this.typeString = paramTypeMirror.toString();
/*     */     }
/*     */ 
/*     */     public String toString() {
/* 294 */       return this.typeString;
/*     */     }
/*     */ 
/*     */     public int hashCode() {
/* 298 */       return (this.type != null ? this.type : this.typeString).hashCode();
/*     */     }
/*     */ 
/*     */     public boolean equals(Object paramObject) {
/* 302 */       if ((this.type != null) && ((paramObject instanceof MirroredTypeExceptionProxy)));
/* 304 */       return this.type
/* 304 */         .equals(((MirroredTypeExceptionProxy)paramObject).type);
/*     */     }
/*     */ 
/*     */     protected RuntimeException generateException()
/*     */     {
/* 308 */       return new MirroredTypeException(this.type);
/*     */     }
/*     */ 
/*     */     private void readObject(ObjectInputStream paramObjectInputStream)
/*     */       throws IOException, ClassNotFoundException
/*     */     {
/* 314 */       paramObjectInputStream.defaultReadObject();
/* 315 */       this.type = null;
/*     */     }
/*     */   }
/*     */ 
/*     */   private static final class MirroredTypesExceptionProxy extends ExceptionProxy
/*     */   {
/*     */     static final long serialVersionUID = 269L;
/*     */     private transient List<TypeMirror> types;
/*     */     private final String typeStrings;
/*     */ 
/*     */     MirroredTypesExceptionProxy(List<TypeMirror> paramList)
/*     */     {
/* 332 */       this.types = paramList;
/* 333 */       this.typeStrings = paramList.toString();
/*     */     }
/*     */ 
/*     */     public String toString() {
/* 337 */       return this.typeStrings;
/*     */     }
/*     */ 
/*     */     public int hashCode() {
/* 341 */       return (this.types != null ? this.types : this.typeStrings).hashCode();
/*     */     }
/*     */ 
/*     */     public boolean equals(Object paramObject) {
/* 345 */       if ((this.types != null) && ((paramObject instanceof MirroredTypesExceptionProxy)));
/* 347 */       return this.types
/* 347 */         .equals(((MirroredTypesExceptionProxy)paramObject).types);
/*     */     }
/*     */ 
/*     */     protected RuntimeException generateException()
/*     */     {
/* 352 */       return new MirroredTypesException(this.types);
/*     */     }
/*     */ 
/*     */     private void readObject(ObjectInputStream paramObjectInputStream)
/*     */       throws IOException, ClassNotFoundException
/*     */     {
/* 358 */       paramObjectInputStream.defaultReadObject();
/* 359 */       this.types = null;
/*     */     }
/*     */   }
/*     */ 
/*     */   private class ValueVisitor
/*     */     implements Attribute.Visitor
/*     */   {
/*     */     private Symbol.MethodSymbol meth;
/*     */     private Class<?> returnClass;
/*     */     private Object value;
/*     */ 
/*     */     ValueVisitor(Symbol.MethodSymbol arg2)
/*     */     {
/*     */       Object localObject;
/* 155 */       this.meth = localObject;
/*     */     }
/*     */ 
/*     */     Object getValue(Attribute paramAttribute) {
/*     */       Method localMethod;
/*     */       try {
/* 161 */         localMethod = AnnotationProxyMaker.this.annoType.getMethod(this.meth.name.toString(), new Class[0]);
/*     */       } catch (NoSuchMethodException localNoSuchMethodException) {
/* 163 */         return null;
/*     */       }
/* 165 */       this.returnClass = localMethod.getReturnType();
/* 166 */       paramAttribute.accept(this);
/* 167 */       if (!(this.value instanceof ExceptionProxy))
/*     */       {
/* 169 */         if (!AnnotationType.invocationHandlerReturnType(this.returnClass)
/* 169 */           .isInstance(this.value))
/*     */         {
/* 170 */           typeMismatch(localMethod, paramAttribute);
/*     */         }
/*     */       }
/* 172 */       return this.value;
/*     */     }
/*     */ 
/*     */     public void visitConstant(Attribute.Constant paramConstant)
/*     */     {
/* 177 */       this.value = paramConstant.getValue();
/*     */     }
/*     */ 
/*     */     public void visitClass(Attribute.Class paramClass) {
/* 181 */       this.value = new AnnotationProxyMaker.MirroredTypeExceptionProxy(paramClass.classType);
/*     */     }
/*     */ 
/*     */     public void visitArray(Attribute.Array paramArray) {
/* 185 */       Name localName = ((Type.ArrayType)paramArray.type).elemtype.tsym.getQualifiedName();
/*     */ 
/* 187 */       if (localName.equals(localName.table.names.java_lang_Class))
/*     */       {
/* 189 */         ListBuffer localListBuffer = new ListBuffer();
/* 190 */         for (Object localObject3 : paramArray.values) {
/* 191 */           Type localType = ((Attribute.Class)localObject3).classType;
/* 192 */           localListBuffer.append(localType);
/*     */         }
/* 194 */         this.value = new AnnotationProxyMaker.MirroredTypesExceptionProxy(localListBuffer.toList());
/*     */       }
/*     */       else {
/* 197 */         int i = paramArray.values.length;
/* 198 */         ??? = this.returnClass;
/* 199 */         this.returnClass = this.returnClass.getComponentType();
/*     */         try {
/* 201 */           Object localObject2 = Array.newInstance(this.returnClass, i);
/* 202 */           for (??? = 0; ??? < i; ???++) {
/* 203 */             paramArray.values[???].accept(this);
/* 204 */             if ((this.value == null) || ((this.value instanceof ExceptionProxy)))
/* 205 */               return;
/*     */             try
/*     */             {
/* 208 */               Array.set(localObject2, ???, this.value);
/*     */             } catch (IllegalArgumentException localIllegalArgumentException) {
/* 210 */               this.value = null;
/* 211 */               return;
/*     */             }
/*     */           }
/* 214 */           this.value = localObject2;
/*     */         } finally {
/* 216 */           this.returnClass = ((Class)???);
/*     */         }
/*     */       }
/*     */     }
/*     */ 
/*     */     public void visitEnum(Attribute.Enum paramEnum)
/*     */     {
/* 223 */       if (this.returnClass.isEnum()) {
/* 224 */         String str = paramEnum.value.toString();
/*     */         try {
/* 226 */           this.value = Enum.valueOf(this.returnClass, str);
/*     */         } catch (IllegalArgumentException localIllegalArgumentException) {
/* 228 */           this.value = new EnumConstantNotPresentExceptionProxy(this.returnClass, str);
/*     */         }
/*     */       }
/*     */       else {
/* 232 */         this.value = null;
/*     */       }
/*     */     }
/*     */ 
/*     */     public void visitCompound(Attribute.Compound paramCompound)
/*     */     {
/*     */       try {
/* 239 */         Class localClass = this.returnClass
/* 239 */           .asSubclass(Annotation.class);
/*     */ 
/* 240 */         this.value = AnnotationProxyMaker.generateAnnotation(paramCompound, localClass);
/*     */       } catch (ClassCastException localClassCastException) {
/* 242 */         this.value = null;
/*     */       }
/*     */     }
/*     */ 
/*     */     public void visitError(Attribute.Error paramError) {
/* 247 */       if ((paramError instanceof Attribute.UnresolvedClass))
/* 248 */         this.value = new AnnotationProxyMaker.MirroredTypeExceptionProxy(((Attribute.UnresolvedClass)paramError).classType);
/*     */       else
/* 250 */         this.value = null;
/*     */     }
/*     */ 
/*     */     private void typeMismatch(Method paramMethod, final Attribute paramAttribute)
/*     */     {
/* 272 */       this.value = new ExceptionProxy()
/*     */       {
/*     */         static final long serialVersionUID = 269L;
/*     */         final transient Method method;
/*     */ 
/*     */         public String toString()
/*     */         {
/* 265 */           return "<error>";
/*     */         }
/*     */ 
/*     */         protected RuntimeException generateException() {
/* 269 */           return new AnnotationTypeMismatchException(this.method, paramAttribute.type
/* 269 */             .toString());
/*     */         }
/*     */       };
/*     */     }
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.javac.model.AnnotationProxyMaker
 * JD-Core Version:    0.6.2
 */