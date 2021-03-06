/*    */ package com.sun.tools.internal.xjc.generator.bean;
/*    */ 
/*    */ import com.sun.codemodel.internal.JClass;
/*    */ import com.sun.codemodel.internal.JDefinedClass;
/*    */ import com.sun.tools.internal.xjc.model.CClassInfo;
/*    */ import com.sun.tools.internal.xjc.model.Model;
/*    */ import com.sun.tools.internal.xjc.outline.ClassOutline;
/*    */ import java.util.Set;
/*    */ 
/*    */ public final class ClassOutlineImpl extends ClassOutline
/*    */ {
/*    */   private final BeanGenerator _parent;
/*    */ 
/*    */   public MethodWriter createMethodWriter()
/*    */   {
/* 45 */     return this._parent.getModel().strategy.createMethodWriter(this);
/*    */   }
/*    */ 
/*    */   public PackageOutlineImpl _package()
/*    */   {
/* 53 */     return (PackageOutlineImpl)super._package();
/*    */   }
/*    */ 
/*    */   ClassOutlineImpl(BeanGenerator _parent, CClassInfo _target, JDefinedClass exposedClass, JDefinedClass _implClass, JClass _implRef)
/*    */   {
/* 58 */     super(_target, exposedClass, _implRef, _implClass);
/* 59 */     this._parent = _parent;
/* 60 */     _package().classes.add(this);
/*    */   }
/*    */ 
/*    */   public BeanGenerator parent() {
/* 64 */     return this._parent;
/*    */   }
/*    */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.internal.xjc.generator.bean.ClassOutlineImpl
 * JD-Core Version:    0.6.2
 */