/*    */ package com.sun.xml.internal.xsom;
/*    */ 
/*    */ import java.util.List;
/*    */ 
/*    */ public abstract interface XSComplexType extends XSType, XSAttContainer
/*    */ {
/*    */   public abstract boolean isAbstract();
/*    */ 
/*    */   public abstract boolean isFinal(int paramInt);
/*    */ 
/*    */   public abstract boolean isSubstitutionProhibited(int paramInt);
/*    */ 
/*    */   public abstract XSElementDecl getScope();
/*    */ 
/*    */   public abstract XSContentType getContentType();
/*    */ 
/*    */   public abstract XSContentType getExplicitContent();
/*    */ 
/*    */   public abstract boolean isMixed();
/*    */ 
/*    */   public abstract XSComplexType getRedefinedBy();
/*    */ 
/*    */   public abstract List<XSComplexType> getSubtypes();
/*    */ 
/*    */   public abstract List<XSElementDecl> getElementDecls();
/*    */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.xml.internal.xsom.XSComplexType
 * JD-Core Version:    0.6.2
 */