/*    */ package com.sun.xml.internal.xsom.impl;
/*    */ 
/*    */ import com.sun.xml.internal.xsom.XSFacet;
/*    */ import com.sun.xml.internal.xsom.XSSimpleType;
/*    */ import com.sun.xml.internal.xsom.XSUnionSimpleType;
/*    */ import com.sun.xml.internal.xsom.XSVariety;
/*    */ import com.sun.xml.internal.xsom.impl.parser.SchemaDocumentImpl;
/*    */ import com.sun.xml.internal.xsom.visitor.XSSimpleTypeFunction;
/*    */ import com.sun.xml.internal.xsom.visitor.XSSimpleTypeVisitor;
/*    */ import java.util.Collections;
/*    */ import java.util.Iterator;
/*    */ import java.util.List;
/*    */ import java.util.Set;
/*    */ import org.xml.sax.Locator;
/*    */ 
/*    */ public class UnionSimpleTypeImpl extends SimpleTypeImpl
/*    */   implements XSUnionSimpleType
/*    */ {
/*    */   private final Ref.SimpleType[] memberTypes;
/*    */ 
/*    */   public UnionSimpleTypeImpl(SchemaDocumentImpl _parent, AnnotationImpl _annon, Locator _loc, ForeignAttributesImpl _fa, String _name, boolean _anonymous, Set<XSVariety> finalSet, Ref.SimpleType[] _members)
/*    */   {
/* 49 */     super(_parent, _annon, _loc, _fa, _name, _anonymous, finalSet, _parent
/* 50 */       .getSchema().parent.anySimpleType);
/*    */ 
/* 52 */     this.memberTypes = _members;
/*    */   }
/*    */ 
/*    */   public XSSimpleType getMember(int idx) {
/* 56 */     return this.memberTypes[idx].getType(); } 
/* 57 */   public int getMemberSize() { return this.memberTypes.length; }
/*    */ 
/*    */   public Iterator<XSSimpleType> iterator() {
/* 60 */     return new Iterator() {
/* 61 */       int idx = 0;
/*    */ 
/* 63 */       public boolean hasNext() { return this.idx < UnionSimpleTypeImpl.this.memberTypes.length; }
/*    */ 
/*    */       public XSSimpleType next()
/*    */       {
/* 67 */         return UnionSimpleTypeImpl.this.memberTypes[(this.idx++)].getType();
/*    */       }
/*    */ 
/*    */       public void remove() {
/* 71 */         throw new UnsupportedOperationException();
/*    */       }
/*    */     };
/*    */   }
/*    */ 
/*    */   public void visit(XSSimpleTypeVisitor visitor) {
/* 77 */     visitor.unionSimpleType(this);
/*    */   }
/*    */   public Object apply(XSSimpleTypeFunction function) {
/* 80 */     return function.unionSimpleType(this);
/*    */   }
/*    */ 
/*    */   public XSUnionSimpleType getBaseUnionType() {
/* 84 */     return this;
/*    */   }
/*    */ 
/*    */   public XSFacet getFacet(String name) {
/* 88 */     return null; } 
/* 89 */   public List<XSFacet> getFacets(String name) { return Collections.EMPTY_LIST; } 
/*    */   public XSVariety getVariety() {
/* 91 */     return XSVariety.UNION;
/*    */   }
/* 93 */   public XSSimpleType getPrimitiveType() { return null; } 
/*    */   public boolean isUnion() {
/* 95 */     return true; } 
/* 96 */   public XSUnionSimpleType asUnion() { return this; }
/*    */ 
/*    */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.xml.internal.xsom.impl.UnionSimpleTypeImpl
 * JD-Core Version:    0.6.2
 */