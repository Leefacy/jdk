/*    */ package com.sun.xml.internal.xsom.util;
/*    */ 
/*    */ import com.sun.xml.internal.xsom.XSType;
/*    */ 
/*    */ public class TypeClosure extends TypeSet
/*    */ {
/*    */   private final TypeSet typeSet;
/*    */ 
/*    */   public TypeClosure(TypeSet typeSet)
/*    */   {
/* 44 */     this.typeSet = typeSet;
/*    */   }
/*    */ 
/*    */   public boolean contains(XSType type)
/*    */   {
/* 53 */     if (this.typeSet.contains(type)) {
/* 54 */       return true;
/*    */     }
/* 56 */     XSType baseType = type.getBaseType();
/* 57 */     if (baseType == null) {
/* 58 */       return false;
/*    */     }
/*    */ 
/* 61 */     return contains(baseType);
/*    */   }
/*    */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.xml.internal.xsom.util.TypeClosure
 * JD-Core Version:    0.6.2
 */