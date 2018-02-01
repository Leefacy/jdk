/*    */ package com.sun.xml.internal.rngom.nc;
/*    */ 
/*    */ import javax.xml.namespace.QName;
/*    */ 
/*    */ public class AnyNameExceptNameClass extends NameClass
/*    */ {
/*    */   private final NameClass nameClass;
/*    */ 
/*    */   public AnyNameExceptNameClass(NameClass nameClass)
/*    */   {
/* 55 */     this.nameClass = nameClass;
/*    */   }
/*    */ 
/*    */   public boolean contains(QName name) {
/* 59 */     return !this.nameClass.contains(name);
/*    */   }
/*    */ 
/*    */   public int containsSpecificity(QName name) {
/* 63 */     return contains(name) ? 0 : -1;
/*    */   }
/*    */ 
/*    */   public boolean equals(Object obj) {
/* 67 */     if ((obj == null) || (!(obj instanceof AnyNameExceptNameClass)))
/* 68 */       return false;
/* 69 */     return this.nameClass.equals(((AnyNameExceptNameClass)obj).nameClass);
/*    */   }
/*    */ 
/*    */   public int hashCode() {
/* 73 */     return this.nameClass.hashCode() ^ 0xFFFFFFFF;
/*    */   }
/*    */ 
/*    */   public <V> V accept(NameClassVisitor<V> visitor) {
/* 77 */     return visitor.visitAnyNameExcept(this.nameClass);
/*    */   }
/*    */ 
/*    */   public boolean isOpen() {
/* 81 */     return true;
/*    */   }
/*    */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.xml.internal.rngom.nc.AnyNameExceptNameClass
 * JD-Core Version:    0.6.2
 */