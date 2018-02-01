/*    */ package com.sun.xml.internal.xsom.util;
/*    */ 
/*    */ import com.sun.xml.internal.xsom.XSType;
/*    */ import java.util.Set;
/*    */ 
/*    */ public class SimpleTypeSet extends TypeSet
/*    */ {
/*    */   private final Set typeSet;
/*    */ 
/*    */   public SimpleTypeSet(Set s)
/*    */   {
/* 45 */     this.typeSet = s;
/*    */   }
/*    */ 
/*    */   public boolean contains(XSType type)
/*    */   {
/* 52 */     return this.typeSet.contains(type);
/*    */   }
/*    */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.xml.internal.xsom.util.SimpleTypeSet
 * JD-Core Version:    0.6.2
 */