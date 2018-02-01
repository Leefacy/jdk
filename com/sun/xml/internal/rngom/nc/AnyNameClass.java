/*    */ package com.sun.xml.internal.rngom.nc;
/*    */ 
/*    */ import javax.xml.namespace.QName;
/*    */ 
/*    */ final class AnyNameClass extends NameClass
/*    */ {
/*    */   public boolean contains(QName name)
/*    */   {
/* 55 */     return true;
/*    */   }
/*    */ 
/*    */   public int containsSpecificity(QName name) {
/* 59 */     return 0;
/*    */   }
/*    */ 
/*    */   public boolean equals(Object obj)
/*    */   {
/* 64 */     return obj == this;
/*    */   }
/*    */ 
/*    */   public int hashCode()
/*    */   {
/* 69 */     return AnyNameClass.class.hashCode();
/*    */   }
/*    */ 
/*    */   public <V> V accept(NameClassVisitor<V> visitor) {
/* 73 */     return visitor.visitAnyName();
/*    */   }
/*    */ 
/*    */   public boolean isOpen() {
/* 77 */     return true;
/*    */   }
/*    */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.xml.internal.rngom.nc.AnyNameClass
 * JD-Core Version:    0.6.2
 */