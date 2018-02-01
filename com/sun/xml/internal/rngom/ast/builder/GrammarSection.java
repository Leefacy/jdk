/*    */ package com.sun.xml.internal.rngom.ast.builder;
/*    */ 
/*    */ import com.sun.xml.internal.rngom.ast.om.Location;
/*    */ import com.sun.xml.internal.rngom.ast.om.ParsedElementAnnotation;
/*    */ import com.sun.xml.internal.rngom.ast.om.ParsedPattern;
/*    */ 
/*    */ public abstract interface GrammarSection<P extends ParsedPattern, E extends ParsedElementAnnotation, L extends Location, A extends Annotations<E, L, CL>, CL extends CommentList<L>>
/*    */ {
/* 74 */   public static final Combine COMBINE_CHOICE = new Combine("choice", null);
/* 75 */   public static final Combine COMBINE_INTERLEAVE = new Combine("interleave", null);
/*    */   public static final String START = "";
/*    */ 
/*    */   public abstract void define(String paramString, Combine paramCombine, P paramP, L paramL, A paramA)
/*    */     throws BuildException;
/*    */ 
/*    */   public abstract void topLevelAnnotation(E paramE)
/*    */     throws BuildException;
/*    */ 
/*    */   public abstract void topLevelComment(CL paramCL)
/*    */     throws BuildException;
/*    */ 
/*    */   public abstract Div<P, E, L, A, CL> makeDiv();
/*    */ 
/*    */   public abstract Include<P, E, L, A, CL> makeInclude();
/*    */ 
/*    */   public static final class Combine
/*    */   {
/*    */     private final String name;
/*    */ 
/*    */     private Combine(String name)
/*    */     {
/* 67 */       this.name = name;
/*    */     }
/*    */     public final String toString() {
/* 70 */       return this.name;
/*    */     }
/*    */   }
/*    */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.xml.internal.rngom.ast.builder.GrammarSection
 * JD-Core Version:    0.6.2
 */