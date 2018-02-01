/*    */ package com.sun.xml.internal.rngom.nc;
/*    */ 
/*    */ import javax.xml.namespace.QName;
/*    */ 
/*    */ final class NullNameClass extends NameClass
/*    */ {
/*    */   public boolean contains(QName name)
/*    */   {
/* 55 */     return false;
/*    */   }
/*    */ 
/*    */   public int containsSpecificity(QName name) {
/* 59 */     return -1;
/*    */   }
/*    */ 
/*    */   public int hashCode()
/*    */   {
/* 64 */     return NullNameClass.class.hashCode();
/*    */   }
/*    */ 
/*    */   public boolean equals(Object obj)
/*    */   {
/* 69 */     return this == obj;
/*    */   }
/*    */ 
/*    */   public <V> V accept(NameClassVisitor<V> visitor) {
/* 73 */     return visitor.visitNull();
/*    */   }
/*    */ 
/*    */   public boolean isOpen() {
/* 77 */     return false;
/*    */   }
/*    */ 
/*    */   private Object readResolve() {
/* 81 */     return NameClass.NULL;
/*    */   }
/*    */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.xml.internal.rngom.nc.NullNameClass
 * JD-Core Version:    0.6.2
 */