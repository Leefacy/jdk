/*    */ package com.sun.xml.internal.rngom.nc;
/*    */ 
/*    */ import javax.xml.namespace.QName;
/*    */ 
/*    */ public class ChoiceNameClass extends NameClass
/*    */ {
/*    */   private final NameClass nameClass1;
/*    */   private final NameClass nameClass2;
/*    */ 
/*    */   public ChoiceNameClass(NameClass nameClass1, NameClass nameClass2)
/*    */   {
/* 56 */     this.nameClass1 = nameClass1;
/* 57 */     this.nameClass2 = nameClass2;
/*    */   }
/*    */ 
/*    */   public boolean contains(QName name) {
/* 61 */     return (this.nameClass1.contains(name)) || (this.nameClass2.contains(name));
/*    */   }
/*    */ 
/*    */   public int containsSpecificity(QName name) {
/* 65 */     return Math.max(this.nameClass1
/* 66 */       .containsSpecificity(name), 
/* 66 */       this.nameClass2
/* 67 */       .containsSpecificity(name));
/*    */   }
/*    */ 
/*    */   public int hashCode()
/*    */   {
/* 72 */     return this.nameClass1.hashCode() ^ this.nameClass2.hashCode();
/*    */   }
/*    */ 
/*    */   public boolean equals(Object obj)
/*    */   {
/* 77 */     if ((obj == null) || (!(obj instanceof ChoiceNameClass)))
/* 78 */       return false;
/* 79 */     ChoiceNameClass other = (ChoiceNameClass)obj;
/*    */ 
/* 82 */     return (this.nameClass1
/* 81 */       .equals(other.nameClass1)) && 
/* 82 */       (this.nameClass2
/* 82 */       .equals(other.nameClass2));
/*    */   }
/*    */ 
/*    */   public <V> V accept(NameClassVisitor<V> visitor)
/*    */   {
/* 86 */     return visitor.visitChoice(this.nameClass1, this.nameClass2);
/*    */   }
/*    */ 
/*    */   public boolean isOpen() {
/* 90 */     return (this.nameClass1.isOpen()) || (this.nameClass2.isOpen());
/*    */   }
/*    */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.xml.internal.rngom.nc.ChoiceNameClass
 * JD-Core Version:    0.6.2
 */