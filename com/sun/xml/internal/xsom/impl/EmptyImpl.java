/*    */ package com.sun.xml.internal.xsom.impl;
/*    */ 
/*    */ import com.sun.xml.internal.xsom.XSContentType;
/*    */ import com.sun.xml.internal.xsom.XSParticle;
/*    */ import com.sun.xml.internal.xsom.XSSimpleType;
/*    */ import com.sun.xml.internal.xsom.visitor.XSContentTypeFunction;
/*    */ import com.sun.xml.internal.xsom.visitor.XSContentTypeVisitor;
/*    */ import com.sun.xml.internal.xsom.visitor.XSFunction;
/*    */ import com.sun.xml.internal.xsom.visitor.XSVisitor;
/*    */ 
/*    */ public class EmptyImpl extends ComponentImpl
/*    */   implements ContentTypeImpl
/*    */ {
/*    */   public EmptyImpl()
/*    */   {
/* 42 */     super(null, null, null, null);
/*    */   }
/* 44 */   public XSSimpleType asSimpleType() { return null; } 
/* 45 */   public XSParticle asParticle() { return null; } 
/* 46 */   public XSContentType asEmpty() { return this; }
/*    */ 
/*    */   public Object apply(XSContentTypeFunction function) {
/* 49 */     return function.empty(this);
/*    */   }
/*    */   public Object apply(XSFunction function) {
/* 52 */     return function.empty(this);
/*    */   }
/*    */   public void visit(XSVisitor visitor) {
/* 55 */     visitor.empty(this);
/*    */   }
/*    */   public void visit(XSContentTypeVisitor visitor) {
/* 58 */     visitor.empty(this);
/*    */   }
/*    */   public XSContentType getContentType() {
/* 61 */     return this;
/*    */   }
/*    */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.xml.internal.xsom.impl.EmptyImpl
 * JD-Core Version:    0.6.2
 */