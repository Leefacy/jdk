/*     */ package com.sun.tools.javac.code;
/*     */ 
/*     */ import com.sun.tools.javac.model.AnnotationProxyMaker;
/*     */ import com.sun.tools.javac.util.List;
/*     */ import com.sun.tools.javac.util.ListBuffer;
/*     */ import com.sun.tools.javac.util.Name;
/*     */ import com.sun.tools.javac.util.Name.Table;
/*     */ import com.sun.tools.javac.util.Names;
/*     */ import java.lang.annotation.Annotation;
/*     */ import java.lang.annotation.Inherited;
/*     */ import java.lang.reflect.Array;
/*     */ import java.lang.reflect.InvocationTargetException;
/*     */ import java.lang.reflect.Method;
/*     */ import java.util.Iterator;
/*     */ import javax.lang.model.AnnotatedConstruct;
/*     */ 
/*     */ public abstract class AnnoConstruct
/*     */   implements AnnotatedConstruct
/*     */ {
/* 183 */   private static final Class<? extends Annotation> REPEATABLE_CLASS = initRepeatable();
/* 184 */   private static final Method VALUE_ELEMENT_METHOD = initValueElementMethod();
/*     */ 
/*     */   public abstract List<? extends Attribute.Compound> getAnnotationMirrors();
/*     */ 
/*     */   protected <A extends Annotation> Attribute.Compound getAttribute(Class<A> paramClass)
/*     */   {
/*  59 */     String str = paramClass.getName();
/*     */ 
/*  61 */     for (Attribute.Compound localCompound : getAnnotationMirrors()) {
/*  62 */       if (str.equals(localCompound.type.tsym.flatName().toString())) {
/*  63 */         return localCompound;
/*     */       }
/*     */     }
/*  66 */     return null;
/*     */   }
/*     */ 
/*     */   protected <A extends Annotation> A[] getInheritedAnnotations(Class<A> paramClass)
/*     */   {
/*  72 */     return (Annotation[])Array.newInstance(paramClass, 0);
/*     */   }
/*     */ 
/*     */   public <A extends Annotation> A[] getAnnotationsByType(Class<A> paramClass)
/*     */   {
/*  79 */     if (!paramClass.isAnnotation()) {
/*  80 */       throw new IllegalArgumentException("Not an annotation type: " + paramClass);
/*     */     }
/*     */ 
/*  84 */     Class localClass = getContainer(paramClass);
/*  85 */     if (localClass == null) {
/*  86 */       localObject1 = getAnnotation(paramClass);
/*  87 */       int i = localObject1 == null ? 0 : 1;
/*     */ 
/*  90 */       Annotation[] arrayOfAnnotation1 = (Annotation[])Array.newInstance(paramClass, i);
/*  91 */       if (localObject1 != null)
/*  92 */         arrayOfAnnotation1[0] = localObject1;
/*  93 */       return arrayOfAnnotation1;
/*     */     }
/*     */ 
/*  97 */     Object localObject1 = paramClass.getName();
/*  98 */     String str = localClass.getName();
/*  99 */     int j = -1; int k = -1;
/* 100 */     Object localObject2 = null; Object localObject3 = null;
/*     */ 
/* 102 */     int m = -1;
/* 103 */     for (Object localObject4 = getAnnotationMirrors().iterator(); ((Iterator)localObject4).hasNext(); ) { Attribute.Compound localCompound = (Attribute.Compound)((Iterator)localObject4).next();
/* 104 */       m++;
/* 105 */       if (localCompound.type.tsym.flatName().contentEquals((CharSequence)localObject1)) {
/* 106 */         j = m;
/* 107 */         localObject2 = localCompound;
/* 108 */       } else if ((str != null) && 
/* 109 */         (localCompound.type.tsym
/* 109 */         .flatName().contentEquals(str))) {
/* 110 */         k = m;
/* 111 */         localObject3 = localCompound;
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 116 */     if ((localObject2 == null) && (localObject3 == null) && 
/* 117 */       (paramClass
/* 117 */       .isAnnotationPresent(Inherited.class)))
/*     */     {
/* 118 */       return getInheritedAnnotations(paramClass);
/*     */     }
/* 120 */     localObject4 = unpackContained(localObject3);
/*     */ 
/* 124 */     if ((localObject2 == null) && (localObject4.length == 0) && 
/* 125 */       (paramClass
/* 125 */       .isAnnotationPresent(Inherited.class)))
/*     */     {
/* 126 */       return getInheritedAnnotations(paramClass);
/*     */     }
/* 128 */     int n = (localObject2 == null ? 0 : 1) + localObject4.length;
/*     */ 
/* 130 */     Annotation[] arrayOfAnnotation2 = (Annotation[])Array.newInstance(paramClass, n);
/*     */ 
/* 133 */     int i1 = -1;
/* 134 */     int i2 = arrayOfAnnotation2.length;
/* 135 */     if ((j >= 0) && (k >= 0)) {
/* 136 */       if (j < k) {
/* 137 */         arrayOfAnnotation2[0] = AnnotationProxyMaker.generateAnnotation(localObject2, paramClass);
/* 138 */         i1 = 1;
/*     */       } else {
/* 140 */         arrayOfAnnotation2[(arrayOfAnnotation2.length - 1)] = AnnotationProxyMaker.generateAnnotation(localObject2, paramClass);
/* 141 */         i1 = 0;
/* 142 */         i2--;
/*     */       }
/*     */     } else { if (j >= 0) {
/* 145 */         arrayOfAnnotation2[0] = AnnotationProxyMaker.generateAnnotation(localObject2, paramClass);
/* 146 */         return arrayOfAnnotation2;
/*     */       }
/*     */ 
/* 149 */       i1 = 0;
/*     */     }
/*     */ 
/* 152 */     for (int i3 = 0; i3 + i1 < i2; i3++) {
/* 153 */       arrayOfAnnotation2[(i1 + i3)] = AnnotationProxyMaker.generateAnnotation(localObject4[i3], paramClass);
/*     */     }
/* 155 */     return arrayOfAnnotation2;
/*     */   }
/*     */ 
/*     */   private Attribute.Compound[] unpackContained(Attribute.Compound paramCompound)
/*     */   {
/* 160 */     Attribute[] arrayOfAttribute1 = null;
/* 161 */     if (paramCompound != null)
/* 162 */       arrayOfAttribute1 = unpackAttributes(paramCompound);
/* 163 */     ListBuffer localListBuffer = new ListBuffer();
/* 164 */     if (arrayOfAttribute1 != null) {
/* 165 */       for (Attribute localAttribute : arrayOfAttribute1)
/* 166 */         if ((localAttribute instanceof Attribute.Compound))
/* 167 */           localListBuffer = localListBuffer.append((Attribute.Compound)localAttribute);
/*     */     }
/* 169 */     return (Attribute.Compound[])localListBuffer.toArray(new Attribute.Compound[localListBuffer.size()]);
/*     */   }
/*     */ 
/*     */   public <A extends Annotation> A getAnnotation(Class<A> paramClass)
/*     */   {
/* 175 */     if (!paramClass.isAnnotation()) {
/* 176 */       throw new IllegalArgumentException("Not an annotation type: " + paramClass);
/*     */     }
/* 178 */     Attribute.Compound localCompound = getAttribute(paramClass);
/* 179 */     return localCompound == null ? null : AnnotationProxyMaker.generateAnnotation(localCompound, paramClass);
/*     */   }
/*     */ 
/*     */   private static Class<? extends Annotation> initRepeatable()
/*     */   {
/*     */     try
/*     */     {
/* 191 */       return Class.forName("java.lang.annotation.Repeatable").asSubclass(Annotation.class); } catch (ClassNotFoundException|SecurityException localClassNotFoundException) {
/*     */     }
/* 193 */     return null;
/*     */   }
/*     */ 
/*     */   private static Method initValueElementMethod()
/*     */   {
/* 198 */     if (REPEATABLE_CLASS == null) {
/* 199 */       return null;
/*     */     }
/* 201 */     Method localMethod = null;
/*     */     try {
/* 203 */       localMethod = REPEATABLE_CLASS.getMethod("value", new Class[0]);
/* 204 */       if (localMethod != null)
/* 205 */         localMethod.setAccessible(true);
/* 206 */       return localMethod; } catch (NoSuchMethodException localNoSuchMethodException) {
/*     */     }
/* 208 */     return null;
/*     */   }
/*     */ 
/*     */   private static Class<? extends Annotation> getContainer(Class<? extends Annotation> paramClass)
/*     */   {
/* 218 */     if ((REPEATABLE_CLASS != null) && (VALUE_ELEMENT_METHOD != null))
/*     */     {
/* 221 */       Annotation localAnnotation = paramClass.getAnnotation(REPEATABLE_CLASS);
/* 222 */       if (localAnnotation != null)
/*     */       {
/*     */         try
/*     */         {
/* 227 */           Class localClass = (Class)VALUE_ELEMENT_METHOD.invoke(localAnnotation, new Object[0]);
/* 228 */           if (localClass == null) {
/* 229 */             return null;
/*     */           }
/* 231 */           return localClass;
/*     */         } catch (ClassCastException|IllegalAccessException|InvocationTargetException localClassCastException) {
/* 233 */           return null;
/*     */         }
/*     */       }
/*     */     }
/* 237 */     return null;
/*     */   }
/*     */ 
/*     */   private static Attribute[] unpackAttributes(Attribute.Compound paramCompound)
/*     */   {
/* 246 */     return ((Attribute.Array)paramCompound.member(paramCompound.type.tsym.name.table.names.value)).values;
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.javac.code.AnnoConstruct
 * JD-Core Version:    0.6.2
 */