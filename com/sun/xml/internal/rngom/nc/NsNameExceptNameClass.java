/*    */ package com.sun.xml.internal.rngom.nc;
/*    */ 
/*    */ import javax.xml.namespace.QName;
/*    */ 
/*    */ public class NsNameExceptNameClass extends NameClass
/*    */ {
/*    */   private final NameClass nameClass;
/*    */   private final String namespaceURI;
/*    */ 
/*    */   public NsNameExceptNameClass(String namespaceURI, NameClass nameClass)
/*    */   {
/* 56 */     this.namespaceURI = namespaceURI;
/* 57 */     this.nameClass = nameClass;
/*    */   }
/*    */ 
/*    */   public boolean contains(QName name)
/*    */   {
/* 62 */     return (this.namespaceURI.equals(name.getNamespaceURI())) && 
/* 62 */       (!this.nameClass
/* 62 */       .contains(name));
/*    */   }
/*    */ 
/*    */   public int containsSpecificity(QName name)
/*    */   {
/* 66 */     return contains(name) ? 1 : -1;
/*    */   }
/*    */ 
/*    */   public boolean equals(Object obj)
/*    */   {
/* 71 */     if ((obj == null) || (!(obj instanceof NsNameExceptNameClass)))
/* 72 */       return false;
/* 73 */     NsNameExceptNameClass other = (NsNameExceptNameClass)obj;
/*    */ 
/* 75 */     return (this.namespaceURI.equals(other.namespaceURI)) && 
/* 75 */       (this.nameClass
/* 75 */       .equals(other.nameClass));
/*    */   }
/*    */ 
/*    */   public int hashCode()
/*    */   {
/* 80 */     return this.namespaceURI.hashCode() ^ this.nameClass.hashCode();
/*    */   }
/*    */ 
/*    */   public <V> V accept(NameClassVisitor<V> visitor) {
/* 84 */     return visitor.visitNsNameExcept(this.namespaceURI, this.nameClass);
/*    */   }
/*    */ 
/*    */   public boolean isOpen() {
/* 88 */     return true;
/*    */   }
/*    */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.xml.internal.rngom.nc.NsNameExceptNameClass
 * JD-Core Version:    0.6.2
 */