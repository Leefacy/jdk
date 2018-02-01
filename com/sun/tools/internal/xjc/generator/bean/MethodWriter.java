/*    */ package com.sun.tools.internal.xjc.generator.bean;
/*    */ 
/*    */ import com.sun.codemodel.internal.JCodeModel;
/*    */ import com.sun.codemodel.internal.JDocComment;
/*    */ import com.sun.codemodel.internal.JMethod;
/*    */ import com.sun.codemodel.internal.JType;
/*    */ import com.sun.codemodel.internal.JVar;
/*    */ import com.sun.tools.internal.xjc.outline.ClassOutline;
/*    */ import com.sun.tools.internal.xjc.outline.Outline;
/*    */ 
/*    */ public abstract class MethodWriter
/*    */ {
/*    */   protected final JCodeModel codeModel;
/*    */ 
/*    */   protected MethodWriter(ClassOutline context)
/*    */   {
/* 52 */     this.codeModel = context.parent().getCodeModel();
/*    */   }
/*    */ 
/*    */   public abstract JMethod declareMethod(JType paramJType, String paramString);
/*    */ 
/*    */   public final JMethod declareMethod(Class returnType, String methodName)
/*    */   {
/* 65 */     return declareMethod(this.codeModel.ref(returnType), methodName);
/*    */   }
/*    */ 
/*    */   public abstract JDocComment javadoc();
/*    */ 
/*    */   public abstract JVar addParameter(JType paramJType, String paramString);
/*    */ 
/*    */   public final JVar addParameter(Class type, String name)
/*    */   {
/* 86 */     return addParameter(this.codeModel.ref(type), name);
/*    */   }
/*    */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.internal.xjc.generator.bean.MethodWriter
 * JD-Core Version:    0.6.2
 */