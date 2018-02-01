/*    */ package com.sun.xml.internal.xsom.impl;
/*    */ 
/*    */ import com.sun.xml.internal.xsom.XSAttGroupDecl;
/*    */ import com.sun.xml.internal.xsom.XSAttributeDecl;
/*    */ import com.sun.xml.internal.xsom.XSComplexType;
/*    */ import com.sun.xml.internal.xsom.XSContentType;
/*    */ import com.sun.xml.internal.xsom.XSElementDecl;
/*    */ import com.sun.xml.internal.xsom.XSIdentityConstraint;
/*    */ import com.sun.xml.internal.xsom.XSSimpleType;
/*    */ import com.sun.xml.internal.xsom.XSTerm;
/*    */ import com.sun.xml.internal.xsom.XSType;
/*    */ 
/*    */ public abstract class Ref
/*    */ {
/*    */   public static abstract interface AttGroup
/*    */   {
/*    */     public abstract XSAttGroupDecl get();
/*    */   }
/*    */ 
/*    */   public static abstract interface Attribute
/*    */   {
/*    */     public abstract XSAttributeDecl getAttribute();
/*    */   }
/*    */ 
/*    */   public static abstract interface ComplexType extends Ref.Type
/*    */   {
/*    */     public abstract XSComplexType getType();
/*    */   }
/*    */ 
/*    */   public static abstract interface ContentType
/*    */   {
/*    */     public abstract XSContentType getContentType();
/*    */   }
/*    */ 
/*    */   public static abstract interface Element extends Ref.Term
/*    */   {
/*    */     public abstract XSElementDecl get();
/*    */   }
/*    */ 
/*    */   public static abstract interface IdentityConstraint
/*    */   {
/*    */     public abstract XSIdentityConstraint get();
/*    */   }
/*    */ 
/*    */   public static abstract interface SimpleType extends Ref.Type
/*    */   {
/*    */     public abstract XSSimpleType getType();
/*    */   }
/*    */ 
/*    */   public static abstract interface Term
/*    */   {
/*    */     public abstract XSTerm getTerm();
/*    */   }
/*    */ 
/*    */   public static abstract interface Type
/*    */   {
/*    */     public abstract XSType getType();
/*    */   }
/*    */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.xml.internal.xsom.impl.Ref
 * JD-Core Version:    0.6.2
 */