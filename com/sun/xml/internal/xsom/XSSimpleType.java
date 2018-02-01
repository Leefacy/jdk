/*    */ package com.sun.xml.internal.xsom;
/*    */ 
/*    */ import com.sun.xml.internal.xsom.visitor.XSSimpleTypeFunction;
/*    */ import com.sun.xml.internal.xsom.visitor.XSSimpleTypeVisitor;
/*    */ import java.util.List;
/*    */ 
/*    */ public abstract interface XSSimpleType extends XSType, XSContentType
/*    */ {
/*    */   public abstract XSSimpleType getSimpleBaseType();
/*    */ 
/*    */   public abstract XSVariety getVariety();
/*    */ 
/*    */   public abstract XSSimpleType getPrimitiveType();
/*    */ 
/*    */   public abstract boolean isPrimitive();
/*    */ 
/*    */   public abstract XSListSimpleType getBaseListType();
/*    */ 
/*    */   public abstract XSUnionSimpleType getBaseUnionType();
/*    */ 
/*    */   public abstract boolean isFinal(XSVariety paramXSVariety);
/*    */ 
/*    */   public abstract XSSimpleType getRedefinedBy();
/*    */ 
/*    */   public abstract XSFacet getFacet(String paramString);
/*    */ 
/*    */   public abstract List<XSFacet> getFacets(String paramString);
/*    */ 
/*    */   public abstract void visit(XSSimpleTypeVisitor paramXSSimpleTypeVisitor);
/*    */ 
/*    */   public abstract <T> T apply(XSSimpleTypeFunction<T> paramXSSimpleTypeFunction);
/*    */ 
/*    */   public abstract boolean isRestriction();
/*    */ 
/*    */   public abstract boolean isList();
/*    */ 
/*    */   public abstract boolean isUnion();
/*    */ 
/*    */   public abstract XSRestrictionSimpleType asRestriction();
/*    */ 
/*    */   public abstract XSListSimpleType asList();
/*    */ 
/*    */   public abstract XSUnionSimpleType asUnion();
/*    */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.xml.internal.xsom.XSSimpleType
 * JD-Core Version:    0.6.2
 */