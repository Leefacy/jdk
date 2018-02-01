/*    */ package com.sun.xml.internal.xsom.impl;
/*    */ 
/*    */ import com.sun.xml.internal.xsom.XSFacet;
/*    */ import com.sun.xml.internal.xsom.XmlString;
/*    */ import com.sun.xml.internal.xsom.impl.parser.SchemaDocumentImpl;
/*    */ import com.sun.xml.internal.xsom.visitor.XSFunction;
/*    */ import com.sun.xml.internal.xsom.visitor.XSVisitor;
/*    */ import org.xml.sax.Locator;
/*    */ 
/*    */ public class FacetImpl extends ComponentImpl
/*    */   implements XSFacet
/*    */ {
/*    */   private final String name;
/*    */   private final XmlString value;
/*    */   private boolean fixed;
/*    */ 
/*    */   public FacetImpl(SchemaDocumentImpl owner, AnnotationImpl _annon, Locator _loc, ForeignAttributesImpl _fa, String _name, XmlString _value, boolean _fixed)
/*    */   {
/* 39 */     super(owner, _annon, _loc, _fa);
/*    */ 
/* 41 */     this.name = _name;
/* 42 */     this.value = _value;
/* 43 */     this.fixed = _fixed;
/*    */   }
/*    */ 
/*    */   public String getName() {
/* 47 */     return this.name;
/*    */   }
/*    */   public XmlString getValue() {
/* 50 */     return this.value;
/*    */   }
/*    */   public boolean isFixed() {
/* 53 */     return this.fixed;
/*    */   }
/*    */ 
/*    */   public void visit(XSVisitor visitor) {
/* 57 */     visitor.facet(this);
/*    */   }
/*    */   public Object apply(XSFunction function) {
/* 60 */     return function.facet(this);
/*    */   }
/*    */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.xml.internal.xsom.impl.FacetImpl
 * JD-Core Version:    0.6.2
 */