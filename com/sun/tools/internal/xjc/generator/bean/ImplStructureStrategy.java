/*     */ package com.sun.tools.internal.xjc.generator.bean;
/*     */ 
/*     */ import com.sun.codemodel.internal.JClassContainer;
/*     */ import com.sun.codemodel.internal.JDefinedClass;
/*     */ import com.sun.codemodel.internal.JDocComment;
/*     */ import com.sun.codemodel.internal.JMethod;
/*     */ import com.sun.codemodel.internal.JPackage;
/*     */ import com.sun.codemodel.internal.JType;
/*     */ import com.sun.codemodel.internal.JVar;
/*     */ import com.sun.tools.internal.xjc.generator.annotation.spec.XmlAccessorTypeWriter;
/*     */ import com.sun.tools.internal.xjc.model.CClassInfo;
/*     */ import com.sun.tools.internal.xjc.outline.Aspect;
/*     */ import com.sun.tools.internal.xjc.outline.ClassOutline;
/*     */ import com.sun.tools.internal.xjc.outline.Outline;
/*     */ import com.sun.tools.internal.xjc.util.CodeModelClassFactory;
/*     */ import javax.xml.bind.annotation.XmlAccessType;
/*     */ import javax.xml.bind.annotation.XmlEnum;
/*     */ import javax.xml.bind.annotation.XmlEnumValue;
/*     */ 
/*     */ @XmlEnum(Boolean.class)
/*     */ public enum ImplStructureStrategy
/*     */ {
/*  62 */   BEAN_ONLY, 
/*     */ 
/* 114 */   INTF_AND_IMPL;
/*     */ 
/*     */   protected abstract Result createClasses(Outline paramOutline, CClassInfo paramCClassInfo);
/*     */ 
/*     */   protected abstract JPackage getPackage(JPackage paramJPackage, Aspect paramAspect);
/*     */ 
/*     */   protected abstract MethodWriter createMethodWriter(ClassOutlineImpl paramClassOutlineImpl);
/*     */ 
/*     */   protected abstract void _extends(ClassOutlineImpl paramClassOutlineImpl1, ClassOutlineImpl paramClassOutlineImpl2);
/*     */ 
/*     */   public static final class Result
/*     */   {
/*     */     public final JDefinedClass exposed;
/*     */     public final JDefinedClass implementation;
/*     */ 
/*     */     public Result(JDefinedClass exposed, JDefinedClass implementation)
/*     */     {
/* 212 */       this.exposed = exposed;
/* 213 */       this.implementation = implementation;
/*     */     }
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.internal.xjc.generator.bean.ImplStructureStrategy
 * JD-Core Version:    0.6.2
 */