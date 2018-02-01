/*    */ package com.sun.tools.javadoc;
/*    */ 
/*    */ import com.sun.javadoc.AnnotationTypeElementDoc;
/*    */ import com.sun.javadoc.AnnotationValue;
/*    */ import com.sun.source.util.TreePath;
/*    */ import com.sun.tools.javac.code.Symbol.MethodSymbol;
/*    */ 
/*    */ public class AnnotationTypeElementDocImpl extends MethodDocImpl
/*    */   implements AnnotationTypeElementDoc
/*    */ {
/*    */   public AnnotationTypeElementDocImpl(DocEnv paramDocEnv, Symbol.MethodSymbol paramMethodSymbol)
/*    */   {
/* 49 */     super(paramDocEnv, paramMethodSymbol);
/*    */   }
/*    */ 
/*    */   public AnnotationTypeElementDocImpl(DocEnv paramDocEnv, Symbol.MethodSymbol paramMethodSymbol, TreePath paramTreePath) {
/* 53 */     super(paramDocEnv, paramMethodSymbol, paramTreePath);
/*    */   }
/*    */ 
/*    */   public boolean isAnnotationTypeElement()
/*    */   {
/* 61 */     return !isMethod();
/*    */   }
/*    */ 
/*    */   public boolean isMethod()
/*    */   {
/* 70 */     return this.env.legacyDoclet;
/*    */   }
/*    */ 
/*    */   public boolean isAbstract()
/*    */   {
/* 78 */     return false;
/*    */   }
/*    */ 
/*    */   public AnnotationValue defaultValue()
/*    */   {
/* 86 */     return this.sym.defaultValue == null ? null : new AnnotationValueImpl(this.env, this.sym.defaultValue);
/*    */   }
/*    */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.javadoc.AnnotationTypeElementDocImpl
 * JD-Core Version:    0.6.2
 */