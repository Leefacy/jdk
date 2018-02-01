/*    */ package com.sun.xml.internal.xsom.impl;
/*    */ 
/*    */ import com.sun.xml.internal.xsom.XSElementDecl;
/*    */ import com.sun.xml.internal.xsom.XSModelGroup;
/*    */ import com.sun.xml.internal.xsom.XSModelGroupDecl;
/*    */ import com.sun.xml.internal.xsom.XSTerm;
/*    */ import com.sun.xml.internal.xsom.XSWildcard;
/*    */ import com.sun.xml.internal.xsom.impl.parser.SchemaDocumentImpl;
/*    */ import com.sun.xml.internal.xsom.visitor.XSFunction;
/*    */ import com.sun.xml.internal.xsom.visitor.XSTermFunction;
/*    */ import com.sun.xml.internal.xsom.visitor.XSTermFunctionWithParam;
/*    */ import com.sun.xml.internal.xsom.visitor.XSTermVisitor;
/*    */ import com.sun.xml.internal.xsom.visitor.XSVisitor;
/*    */ import org.xml.sax.Locator;
/*    */ 
/*    */ public class ModelGroupDeclImpl extends DeclarationImpl
/*    */   implements XSModelGroupDecl, Ref.Term
/*    */ {
/*    */   private final ModelGroupImpl modelGroup;
/*    */ 
/*    */   public ModelGroupDeclImpl(SchemaDocumentImpl owner, AnnotationImpl _annon, Locator _loc, ForeignAttributesImpl _fa, String _targetNamespace, String _name, ModelGroupImpl _modelGroup)
/*    */   {
/* 48 */     super(owner, _annon, _loc, _fa, _targetNamespace, _name, false);
/* 49 */     this.modelGroup = _modelGroup;
/*    */ 
/* 51 */     if (this.modelGroup == null)
/* 52 */       throw new IllegalArgumentException();
/*    */   }
/*    */ 
/*    */   public XSModelGroup getModelGroup() {
/* 56 */     return this.modelGroup;
/*    */   }
/*    */ 
/*    */   public void redefine(ModelGroupDeclImpl oldMG)
/*    */   {
/* 63 */     this.modelGroup.redefine(oldMG);
/*    */   }
/*    */ 
/*    */   public void visit(XSVisitor visitor)
/*    */   {
/* 68 */     visitor.modelGroupDecl(this);
/*    */   }
/*    */   public void visit(XSTermVisitor visitor) {
/* 71 */     visitor.modelGroupDecl(this);
/*    */   }
/*    */   public Object apply(XSTermFunction function) {
/* 74 */     return function.modelGroupDecl(this);
/*    */   }
/*    */ 
/*    */   public <T, P> T apply(XSTermFunctionWithParam<T, P> function, P param) {
/* 78 */     return function.modelGroupDecl(this, param);
/*    */   }
/*    */ 
/*    */   public Object apply(XSFunction function) {
/* 82 */     return function.modelGroupDecl(this);
/*    */   }
/*    */ 
/*    */   public boolean isWildcard() {
/* 86 */     return false; } 
/* 87 */   public boolean isModelGroupDecl() { return true; } 
/* 88 */   public boolean isModelGroup() { return false; } 
/* 89 */   public boolean isElementDecl() { return false; } 
/*    */   public XSWildcard asWildcard() {
/* 91 */     return null; } 
/* 92 */   public XSModelGroupDecl asModelGroupDecl() { return this; } 
/* 93 */   public XSModelGroup asModelGroup() { return null; } 
/* 94 */   public XSElementDecl asElementDecl() { return null; }
/*    */ 
/*    */   public XSTerm getTerm()
/*    */   {
/* 98 */     return this;
/*    */   }
/*    */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.xml.internal.xsom.impl.ModelGroupDeclImpl
 * JD-Core Version:    0.6.2
 */