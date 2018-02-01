/*    */ package com.sun.xml.internal.xsom.impl;
/*    */ 
/*    */ import com.sun.xml.internal.xsom.XSAttributeDecl;
/*    */ import com.sun.xml.internal.xsom.XSSimpleType;
/*    */ import com.sun.xml.internal.xsom.XmlString;
/*    */ import com.sun.xml.internal.xsom.impl.parser.SchemaDocumentImpl;
/*    */ import com.sun.xml.internal.xsom.visitor.XSFunction;
/*    */ import com.sun.xml.internal.xsom.visitor.XSVisitor;
/*    */ import org.xml.sax.Locator;
/*    */ 
/*    */ public class AttributeDeclImpl extends DeclarationImpl
/*    */   implements XSAttributeDecl, Ref.Attribute
/*    */ {
/*    */   private final Ref.SimpleType type;
/*    */   private final XmlString defaultValue;
/*    */   private final XmlString fixedValue;
/*    */ 
/*    */   public AttributeDeclImpl(SchemaDocumentImpl owner, String _targetNamespace, String _name, AnnotationImpl _annon, Locator _loc, ForeignAttributesImpl _fa, boolean _anonymous, XmlString _defValue, XmlString _fixedValue, Ref.SimpleType _type)
/*    */   {
/* 44 */     super(owner, _annon, _loc, _fa, _targetNamespace, _name, _anonymous);
/*    */ 
/* 46 */     if (_name == null) {
/* 47 */       throw new IllegalArgumentException();
/*    */     }
/* 49 */     this.defaultValue = _defValue;
/* 50 */     this.fixedValue = _fixedValue;
/* 51 */     this.type = _type;
/*    */   }
/*    */ 
/*    */   public XSSimpleType getType() {
/* 55 */     return this.type.getType();
/*    */   }
/*    */   public XmlString getDefaultValue() {
/* 58 */     return this.defaultValue;
/*    */   }
/*    */   public XmlString getFixedValue() {
/* 61 */     return this.fixedValue;
/*    */   }
/*    */   public void visit(XSVisitor visitor) {
/* 64 */     visitor.attributeDecl(this);
/*    */   }
/*    */   public Object apply(XSFunction function) {
/* 67 */     return function.attributeDecl(this);
/*    */   }
/*    */ 
/*    */   public XSAttributeDecl getAttribute()
/*    */   {
/* 72 */     return this;
/*    */   }
/*    */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.xml.internal.xsom.impl.AttributeDeclImpl
 * JD-Core Version:    0.6.2
 */