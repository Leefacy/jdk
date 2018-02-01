/*     */ package com.sun.tools.javac.processing;
/*     */ 
/*     */ import java.lang.annotation.Annotation;
/*     */ import java.util.Collections;
/*     */ import java.util.LinkedHashSet;
/*     */ import java.util.List;
/*     */ import java.util.Set;
/*     */ import javax.annotation.processing.ProcessingEnvironment;
/*     */ import javax.annotation.processing.RoundEnvironment;
/*     */ import javax.lang.model.element.AnnotationMirror;
/*     */ import javax.lang.model.element.Element;
/*     */ import javax.lang.model.element.ElementKind;
/*     */ import javax.lang.model.element.ExecutableElement;
/*     */ import javax.lang.model.element.TypeElement;
/*     */ import javax.lang.model.type.DeclaredType;
/*     */ import javax.lang.model.util.ElementScanner8;
/*     */ import javax.lang.model.util.Elements;
/*     */ 
/*     */ public class JavacRoundEnvironment
/*     */   implements RoundEnvironment
/*     */ {
/*     */   private final boolean processingOver;
/*     */   private final boolean errorRaised;
/*     */   private final ProcessingEnvironment processingEnv;
/*     */   private final Set<? extends Element> rootElements;
/*     */   private static final String NOT_AN_ANNOTATION_TYPE = "The argument does not represent an annotation type: ";
/*     */ 
/*     */   JavacRoundEnvironment(boolean paramBoolean1, boolean paramBoolean2, Set<? extends Element> paramSet, ProcessingEnvironment paramProcessingEnvironment)
/*     */   {
/*  59 */     this.processingOver = paramBoolean1;
/*  60 */     this.errorRaised = paramBoolean2;
/*  61 */     this.rootElements = paramSet;
/*  62 */     this.processingEnv = paramProcessingEnvironment;
/*     */   }
/*     */ 
/*     */   public String toString() {
/*  66 */     return String.format("[errorRaised=%b, rootElements=%s, processingOver=%b]", new Object[] { 
/*  67 */       Boolean.valueOf(this.errorRaised), 
/*  67 */       this.rootElements, 
/*  69 */       Boolean.valueOf(this.processingOver) });
/*     */   }
/*     */ 
/*     */   public boolean processingOver()
/*     */   {
/*  73 */     return this.processingOver;
/*     */   }
/*     */ 
/*     */   public boolean errorRaised()
/*     */   {
/*  84 */     return this.errorRaised;
/*     */   }
/*     */ 
/*     */   public Set<? extends Element> getRootElements()
/*     */   {
/*  94 */     return this.rootElements;
/*     */   }
/*     */ 
/*     */   public Set<? extends Element> getElementsAnnotatedWith(TypeElement paramTypeElement)
/*     */   {
/* 113 */     Set localSet = Collections.emptySet();
/* 114 */     if (paramTypeElement.getKind() != ElementKind.ANNOTATION_TYPE) {
/* 115 */       throw new IllegalArgumentException("The argument does not represent an annotation type: " + paramTypeElement);
/*     */     }
/* 117 */     AnnotationSetScanner localAnnotationSetScanner = new AnnotationSetScanner(localSet);
/*     */ 
/* 120 */     for (Element localElement : this.rootElements) {
/* 121 */       localSet = (Set)localAnnotationSetScanner.scan(localElement, paramTypeElement);
/*     */     }
/* 123 */     return localSet;
/*     */   }
/*     */ 
/*     */   public Set<? extends Element> getElementsAnnotatedWith(Class<? extends Annotation> paramClass)
/*     */   {
/* 167 */     if (!paramClass.isAnnotation())
/* 168 */       throw new IllegalArgumentException("The argument does not represent an annotation type: " + paramClass);
/* 169 */     String str = paramClass.getCanonicalName();
/* 170 */     if (str == null) {
/* 171 */       return Collections.emptySet();
/*     */     }
/* 173 */     TypeElement localTypeElement = this.processingEnv.getElementUtils().getTypeElement(str);
/* 174 */     if (localTypeElement == null) {
/* 175 */       return Collections.emptySet();
/*     */     }
/* 177 */     return getElementsAnnotatedWith(localTypeElement);
/*     */   }
/*     */ 
/*     */   private class AnnotationSetScanner extends ElementScanner8<Set<Element>, TypeElement>
/*     */   {
/* 130 */     Set<Element> annotatedElements = new LinkedHashSet();
/*     */ 
/*     */     AnnotationSetScanner() {
/* 133 */       super();
/*     */     }
/*     */ 
/*     */     public Set<Element> visitType(TypeElement paramTypeElement1, TypeElement paramTypeElement2)
/*     */     {
/* 139 */       scan(paramTypeElement1.getTypeParameters(), paramTypeElement2);
/* 140 */       return (Set)super.visitType(paramTypeElement1, paramTypeElement2);
/*     */     }
/*     */ 
/*     */     public Set<Element> visitExecutable(ExecutableElement paramExecutableElement, TypeElement paramTypeElement)
/*     */     {
/* 146 */       scan(paramExecutableElement.getTypeParameters(), paramTypeElement);
/* 147 */       return (Set)super.visitExecutable(paramExecutableElement, paramTypeElement);
/*     */     }
/*     */ 
/*     */     public Set<Element> scan(Element paramElement, TypeElement paramTypeElement)
/*     */     {
/* 153 */       List localList = JavacRoundEnvironment.this.processingEnv
/* 153 */         .getElementUtils().getAllAnnotationMirrors(paramElement);
/* 154 */       for (AnnotationMirror localAnnotationMirror : localList) {
/* 155 */         if (paramTypeElement.equals(localAnnotationMirror.getAnnotationType().asElement()))
/* 156 */           this.annotatedElements.add(paramElement);
/*     */       }
/* 158 */       paramElement.accept(this, paramTypeElement);
/* 159 */       return this.annotatedElements;
/*     */     }
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.javac.processing.JavacRoundEnvironment
 * JD-Core Version:    0.6.2
 */