/*    */ package com.sun.xml.internal.xsom.impl;
/*    */ 
/*    */ import com.sun.xml.internal.xsom.XSFacet;
/*    */ import com.sun.xml.internal.xsom.XSListSimpleType;
/*    */ import com.sun.xml.internal.xsom.XSSimpleType;
/*    */ import com.sun.xml.internal.xsom.XSVariety;
/*    */ import com.sun.xml.internal.xsom.impl.parser.SchemaDocumentImpl;
/*    */ import com.sun.xml.internal.xsom.visitor.XSSimpleTypeFunction;
/*    */ import com.sun.xml.internal.xsom.visitor.XSSimpleTypeVisitor;
/*    */ import java.util.Collections;
/*    */ import java.util.List;
/*    */ import java.util.Set;
/*    */ import org.xml.sax.Locator;
/*    */ 
/*    */ public class ListSimpleTypeImpl extends SimpleTypeImpl
/*    */   implements XSListSimpleType
/*    */ {
/*    */   private final Ref.SimpleType itemType;
/*    */ 
/*    */   public ListSimpleTypeImpl(SchemaDocumentImpl _parent, AnnotationImpl _annon, Locator _loc, ForeignAttributesImpl _fa, String _name, boolean _anonymous, Set<XSVariety> finalSet, Ref.SimpleType _itemType)
/*    */   {
/* 48 */     super(_parent, _annon, _loc, _fa, _name, _anonymous, finalSet, _parent
/* 49 */       .getSchema().parent.anySimpleType);
/*    */ 
/* 51 */     this.itemType = _itemType;
/*    */   }
/*    */ 
/*    */   public XSSimpleType getItemType() {
/* 55 */     return this.itemType.getType();
/*    */   }
/*    */   public void visit(XSSimpleTypeVisitor visitor) {
/* 58 */     visitor.listSimpleType(this);
/*    */   }
/*    */   public Object apply(XSSimpleTypeFunction function) {
/* 61 */     return function.listSimpleType(this);
/*    */   }
/*    */ 
/*    */   public XSFacet getFacet(String name) {
/* 65 */     return null; } 
/* 66 */   public List<XSFacet> getFacets(String name) { return Collections.EMPTY_LIST; } 
/*    */   public XSVariety getVariety() {
/* 68 */     return XSVariety.LIST;
/*    */   }
/* 70 */   public XSSimpleType getPrimitiveType() { return null; } 
/*    */   public XSListSimpleType getBaseListType() {
/* 72 */     return this;
/*    */   }
/* 74 */   public boolean isList() { return true; } 
/* 75 */   public XSListSimpleType asList() { return this; }
/*    */ 
/*    */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.xml.internal.xsom.impl.ListSimpleTypeImpl
 * JD-Core Version:    0.6.2
 */