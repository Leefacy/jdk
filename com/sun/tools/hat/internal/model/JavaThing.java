/*    */ package com.sun.tools.hat.internal.model;
/*    */ 
/*    */ public abstract class JavaThing
/*    */ {
/*    */   public JavaThing dereference(Snapshot paramSnapshot, JavaField paramJavaField)
/*    */   {
/* 64 */     return this;
/*    */   }
/*    */ 
/*    */   public boolean isSameTypeAs(JavaThing paramJavaThing)
/*    */   {
/* 74 */     return getClass() == paramJavaThing.getClass();
/*    */   }
/*    */ 
/*    */   public abstract boolean isHeapAllocated();
/*    */ 
/*    */   public abstract int getSize();
/*    */ 
/*    */   public abstract String toString();
/*    */ 
/*    */   public int compareTo(JavaThing paramJavaThing)
/*    */   {
/* 96 */     return toString().compareTo(paramJavaThing.toString());
/*    */   }
/*    */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.hat.internal.model.JavaThing
 * JD-Core Version:    0.6.2
 */