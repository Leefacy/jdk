/*    */ package com.sun.xml.internal.xsom;
/*    */ 
/*    */ public abstract interface XSModelGroup extends XSComponent, XSTerm, Iterable<XSParticle>
/*    */ {
/* 61 */   public static final Compositor ALL = Compositor.ALL;
/*    */ 
/* 65 */   public static final Compositor SEQUENCE = Compositor.SEQUENCE;
/*    */ 
/* 69 */   public static final Compositor CHOICE = Compositor.CHOICE;
/*    */ 
/*    */   public abstract Compositor getCompositor();
/*    */ 
/*    */   public abstract XSParticle getChild(int paramInt);
/*    */ 
/*    */   public abstract int getSize();
/*    */ 
/*    */   public abstract XSParticle[] getChildren();
/*    */ 
/*    */   public static enum Compositor
/*    */   {
/* 41 */     ALL("all"), CHOICE("choice"), SEQUENCE("sequence");
/*    */ 
/*    */     private final String value;
/*    */ 
/* 44 */     private Compositor(String _value) { this.value = _value; }
/*    */ 
/*    */ 
/*    */     public String toString()
/*    */     {
/* 55 */       return this.value;
/*    */     }
/*    */   }
/*    */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.xml.internal.xsom.XSModelGroup
 * JD-Core Version:    0.6.2
 */