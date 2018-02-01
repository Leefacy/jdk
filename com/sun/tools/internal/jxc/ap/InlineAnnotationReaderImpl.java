/*     */ package com.sun.tools.internal.jxc.ap;
/*     */ 
/*     */ import com.sun.xml.internal.bind.v2.model.annotation.AbstractInlineAnnotationReaderImpl;
/*     */ import com.sun.xml.internal.bind.v2.model.annotation.Locatable;
/*     */ import com.sun.xml.internal.bind.v2.model.annotation.LocatableAnnotation;
/*     */ import java.lang.annotation.Annotation;
/*     */ import java.lang.reflect.InvocationTargetException;
/*     */ import java.lang.reflect.Method;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.List;
/*     */ import javax.lang.model.element.AnnotationMirror;
/*     */ import javax.lang.model.element.Element;
/*     */ import javax.lang.model.element.ExecutableElement;
/*     */ import javax.lang.model.element.TypeElement;
/*     */ import javax.lang.model.element.VariableElement;
/*     */ import javax.lang.model.type.DeclaredType;
/*     */ import javax.lang.model.type.MirroredTypeException;
/*     */ import javax.lang.model.type.MirroredTypesException;
/*     */ import javax.lang.model.type.TypeMirror;
/*     */ 
/*     */ public final class InlineAnnotationReaderImpl extends AbstractInlineAnnotationReaderImpl<TypeMirror, TypeElement, VariableElement, ExecutableElement>
/*     */ {
/*  55 */   public static final InlineAnnotationReaderImpl theInstance = new InlineAnnotationReaderImpl();
/*     */ 
/*     */   public <A extends Annotation> A getClassAnnotation(Class<A> a, TypeElement clazz, Locatable srcPos)
/*     */   {
/*  60 */     return LocatableAnnotation.create(clazz.getAnnotation(a), srcPos);
/*     */   }
/*     */ 
/*     */   public <A extends Annotation> A getFieldAnnotation(Class<A> a, VariableElement f, Locatable srcPos) {
/*  64 */     return LocatableAnnotation.create(f.getAnnotation(a), srcPos);
/*     */   }
/*     */ 
/*     */   public boolean hasFieldAnnotation(Class<? extends Annotation> annotationType, VariableElement f) {
/*  68 */     return f.getAnnotation(annotationType) != null;
/*     */   }
/*     */ 
/*     */   public boolean hasClassAnnotation(TypeElement clazz, Class<? extends Annotation> annotationType) {
/*  72 */     return clazz.getAnnotation(annotationType) != null;
/*     */   }
/*     */ 
/*     */   public Annotation[] getAllFieldAnnotations(VariableElement field, Locatable srcPos) {
/*  76 */     return getAllAnnotations(field, srcPos);
/*     */   }
/*     */ 
/*     */   public <A extends Annotation> A getMethodAnnotation(Class<A> a, ExecutableElement method, Locatable srcPos) {
/*  80 */     return LocatableAnnotation.create(method.getAnnotation(a), srcPos);
/*     */   }
/*     */ 
/*     */   public boolean hasMethodAnnotation(Class<? extends Annotation> a, ExecutableElement method) {
/*  84 */     return method.getAnnotation(a) != null;
/*     */   }
/*     */ 
/*     */   public Annotation[] getAllMethodAnnotations(ExecutableElement method, Locatable srcPos) {
/*  88 */     return getAllAnnotations(method, srcPos);
/*     */   }
/*     */ 
/*     */   private Annotation[] getAllAnnotations(Element decl, Locatable srcPos)
/*     */   {
/*  95 */     List r = new ArrayList();
/*     */ 
/*  97 */     for (AnnotationMirror m : decl.getAnnotationMirrors()) {
/*     */       try {
/*  99 */         String fullName = ((TypeElement)m.getAnnotationType().asElement()).getQualifiedName().toString();
/*     */ 
/* 101 */         Class type = SecureLoader.getClassClassLoader(getClass()).loadClass(fullName).asSubclass(Annotation.class);
/* 102 */         Annotation annotation = decl.getAnnotation(type);
/* 103 */         if (annotation != null)
/* 104 */           r.add(LocatableAnnotation.create(annotation, srcPos));
/*     */       }
/*     */       catch (ClassNotFoundException localClassNotFoundException)
/*     */       {
/*     */       }
/*     */     }
/* 110 */     return (Annotation[])r.toArray(new Annotation[r.size()]);
/*     */   }
/*     */ 
/*     */   public <A extends Annotation> A getMethodParameterAnnotation(Class<A> a, ExecutableElement m, int paramIndex, Locatable srcPos) {
/* 114 */     VariableElement[] params = (VariableElement[])m.getParameters().toArray(new VariableElement[m.getParameters().size()]);
/* 115 */     return LocatableAnnotation.create(params[paramIndex]
/* 116 */       .getAnnotation(a), 
/* 116 */       srcPos);
/*     */   }
/*     */ 
/*     */   public <A extends Annotation> A getPackageAnnotation(Class<A> a, TypeElement clazz, Locatable srcPos) {
/* 120 */     return LocatableAnnotation.create(clazz.getEnclosingElement().getAnnotation(a), srcPos);
/*     */   }
/*     */ 
/*     */   public TypeMirror getClassValue(Annotation a, String name) {
/*     */     try {
/* 125 */       a.annotationType().getMethod(name, new Class[0]).invoke(a, new Object[0]);
/* 126 */       if (!$assertionsDisabled) throw new AssertionError();
/* 127 */       throw new IllegalStateException("should throw a MirroredTypeException");
/*     */     } catch (IllegalAccessException e) {
/* 129 */       throw new IllegalAccessError(e.getMessage());
/*     */     } catch (InvocationTargetException e) {
/* 131 */       if ((e.getCause() instanceof MirroredTypeException)) {
/* 132 */         MirroredTypeException me = (MirroredTypeException)e.getCause();
/* 133 */         return me.getTypeMirror();
/*     */       }
/*     */ 
/* 136 */       throw new RuntimeException(e);
/*     */     } catch (NoSuchMethodException e) {
/* 138 */       throw new NoSuchMethodError(e.getMessage());
/*     */     }
/*     */   }
/*     */ 
/*     */   public TypeMirror[] getClassArrayValue(Annotation a, String name) {
/*     */     try {
/* 144 */       a.annotationType().getMethod(name, new Class[0]).invoke(a, new Object[0]);
/* 145 */       if (!$assertionsDisabled) throw new AssertionError();
/* 146 */       throw new IllegalStateException("should throw a MirroredTypesException");
/*     */     } catch (IllegalAccessException e) {
/* 148 */       throw new IllegalAccessError(e.getMessage());
/*     */     } catch (InvocationTargetException e) {
/* 150 */       if ((e.getCause() instanceof MirroredTypesException)) {
/* 151 */         MirroredTypesException me = (MirroredTypesException)e.getCause();
/* 152 */         Collection r = me.getTypeMirrors();
/* 153 */         return (TypeMirror[])r.toArray(new TypeMirror[r.size()]);
/*     */       }
/*     */ 
/* 157 */       if ((e.getCause() instanceof MirroredTypeException)) {
/* 158 */         MirroredTypeException me = (MirroredTypeException)e.getCause();
/* 159 */         TypeMirror tr = me.getTypeMirror();
/* 160 */         TypeMirror[] trArr = new TypeMirror[1];
/* 161 */         trArr[0] = tr;
/* 162 */         return trArr;
/*     */       }
/*     */ 
/* 166 */       throw new RuntimeException(e);
/*     */     } catch (NoSuchMethodException e) {
/* 168 */       throw new NoSuchMethodError(e.getMessage());
/*     */     }
/*     */   }
/*     */ 
/*     */   protected String fullName(ExecutableElement m) {
/* 173 */     return ((TypeElement)m.getEnclosingElement()).getQualifiedName().toString() + '#' + m.getSimpleName();
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.internal.jxc.ap.InlineAnnotationReaderImpl
 * JD-Core Version:    0.6.2
 */