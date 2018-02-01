/*    */ package com.sun.codemodel.internal;
/*    */ 
/*    */ import java.util.Collections;
/*    */ import java.util.Iterator;
/*    */ import java.util.List;
/*    */ 
/*    */ public final class JNullType extends JClass
/*    */ {
/*    */   JNullType(JCodeModel _owner)
/*    */   {
/* 44 */     super(_owner);
/*    */   }
/*    */   public String name() {
/* 47 */     return "null"; } 
/* 48 */   public String fullName() { return "null"; } 
/*    */   public JPackage _package() {
/* 50 */     return owner()._package("");
/*    */   }
/* 52 */   public JClass _extends() { return null; }
/*    */ 
/*    */   public Iterator<JClass> _implements() {
/* 55 */     return Collections.emptyList().iterator();
/*    */   }
/*    */   public boolean isInterface() {
/* 58 */     return false; } 
/* 59 */   public boolean isAbstract() { return false; }
/*    */ 
/*    */   protected JClass substituteParams(JTypeVar[] variables, List<JClass> bindings) {
/* 62 */     return this;
/*    */   }
/*    */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.codemodel.internal.JNullType
 * JD-Core Version:    0.6.2
 */