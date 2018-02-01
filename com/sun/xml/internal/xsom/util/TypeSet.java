/*    */ package com.sun.xml.internal.xsom.util;
/*    */ 
/*    */ import com.sun.xml.internal.xsom.XSType;
/*    */ 
/*    */ public abstract class TypeSet
/*    */ {
/*    */   public abstract boolean contains(XSType paramXSType);
/*    */ 
/*    */   public static TypeSet intersection(TypeSet a, final TypeSet b)
/*    */   {
/* 57 */     return new TypeSet() {
/*    */       public boolean contains(XSType type) {
/* 59 */         return (this.val$a.contains(type)) && (b.contains(type));
/*    */       }
/*    */     };
/*    */   }
/*    */ 
/*    */   public static TypeSet union(TypeSet a, final TypeSet b)
/*    */   {
/* 73 */     return new TypeSet() {
/*    */       public boolean contains(XSType type) {
/* 75 */         return (this.val$a.contains(type)) || (b.contains(type));
/*    */       }
/*    */     };
/*    */   }
/*    */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.xml.internal.xsom.util.TypeSet
 * JD-Core Version:    0.6.2
 */