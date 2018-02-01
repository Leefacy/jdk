/*     */ package com.sun.tools.javadoc;
/*     */ 
/*     */ import com.sun.javadoc.AnnotationDesc;
/*     */ import com.sun.javadoc.AnnotationDesc.ElementValuePair;
/*     */ import com.sun.javadoc.AnnotationTypeDoc;
/*     */ import com.sun.javadoc.AnnotationTypeElementDoc;
/*     */ import com.sun.javadoc.AnnotationValue;
/*     */ import com.sun.tools.javac.code.Attribute;
/*     */ import com.sun.tools.javac.code.Attribute.Compound;
/*     */ import com.sun.tools.javac.code.Symbol.ClassSymbol;
/*     */ import com.sun.tools.javac.code.Symbol.MethodSymbol;
/*     */ import com.sun.tools.javac.code.Type;
/*     */ import com.sun.tools.javac.util.List;
/*     */ import com.sun.tools.javac.util.Pair;
/*     */ 
/*     */ public class AnnotationDescImpl
/*     */   implements AnnotationDesc
/*     */ {
/*     */   private final DocEnv env;
/*     */   private final Attribute.Compound annotation;
/*     */ 
/*     */   AnnotationDescImpl(DocEnv paramDocEnv, Attribute.Compound paramCompound)
/*     */   {
/*  58 */     this.env = paramDocEnv;
/*  59 */     this.annotation = paramCompound;
/*     */   }
/*     */ 
/*     */   public AnnotationTypeDoc annotationType()
/*     */   {
/*  66 */     Symbol.ClassSymbol localClassSymbol = (Symbol.ClassSymbol)this.annotation.type.tsym;
/*  67 */     if (this.annotation.type.isErroneous()) {
/*  68 */       this.env.warning(null, "javadoc.class_not_found", this.annotation.type.toString());
/*  69 */       return new AnnotationTypeDocImpl(this.env, localClassSymbol);
/*     */     }
/*  71 */     return (AnnotationTypeDoc)this.env.getClassDoc(localClassSymbol);
/*     */   }
/*     */ 
/*     */   public AnnotationDesc.ElementValuePair[] elementValues()
/*     */   {
/*  82 */     List localList = this.annotation.values;
/*  83 */     AnnotationDesc.ElementValuePair[] arrayOfElementValuePair = new AnnotationDesc.ElementValuePair[localList.length()];
/*  84 */     int i = 0;
/*  85 */     for (Pair localPair : localList) {
/*  86 */       arrayOfElementValuePair[(i++)] = new ElementValuePairImpl(this.env, (Symbol.MethodSymbol)localPair.fst, (Attribute)localPair.snd);
/*     */     }
/*  88 */     return arrayOfElementValuePair;
/*     */   }
/*     */ 
/*     */   public boolean isSynthesized()
/*     */   {
/*  97 */     return this.annotation.isSynthesized();
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 110 */     StringBuilder localStringBuilder = new StringBuilder("@");
/* 111 */     localStringBuilder.append(this.annotation.type.tsym);
/*     */ 
/* 113 */     AnnotationDesc.ElementValuePair[] arrayOfElementValuePair1 = elementValues();
/* 114 */     if (arrayOfElementValuePair1.length > 0) {
/* 115 */       localStringBuilder.append('(');
/* 116 */       int i = 1;
/* 117 */       for (AnnotationDesc.ElementValuePair localElementValuePair : arrayOfElementValuePair1) {
/* 118 */         if (i == 0) {
/* 119 */           localStringBuilder.append(", ");
/*     */         }
/* 121 */         i = 0;
/*     */ 
/* 123 */         String str = localElementValuePair.element().name();
/* 124 */         if ((arrayOfElementValuePair1.length == 1) && (str.equals("value")))
/* 125 */           localStringBuilder.append(localElementValuePair.value());
/*     */         else {
/* 127 */           localStringBuilder.append(localElementValuePair);
/*     */         }
/*     */       }
/* 130 */       localStringBuilder.append(')');
/*     */     }
/* 132 */     return localStringBuilder.toString();
/*     */   }
/*     */ 
/*     */   public static class ElementValuePairImpl
/*     */     implements AnnotationDesc.ElementValuePair
/*     */   {
/*     */     private final DocEnv env;
/*     */     private final Symbol.MethodSymbol meth;
/*     */     private final Attribute value;
/*     */ 
/*     */     ElementValuePairImpl(DocEnv paramDocEnv, Symbol.MethodSymbol paramMethodSymbol, Attribute paramAttribute)
/*     */     {
/* 147 */       this.env = paramDocEnv;
/* 148 */       this.meth = paramMethodSymbol;
/* 149 */       this.value = paramAttribute;
/*     */     }
/*     */ 
/*     */     public AnnotationTypeElementDoc element()
/*     */     {
/* 156 */       return this.env.getAnnotationTypeElementDoc(this.meth);
/*     */     }
/*     */ 
/*     */     public AnnotationValue value()
/*     */     {
/* 163 */       return new AnnotationValueImpl(this.env, this.value);
/*     */     }
/*     */ 
/*     */     public String toString()
/*     */     {
/* 172 */       return this.meth.name + "=" + value();
/*     */     }
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.javadoc.AnnotationDescImpl
 * JD-Core Version:    0.6.2
 */