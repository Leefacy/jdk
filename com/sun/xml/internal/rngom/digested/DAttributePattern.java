/*    */ package com.sun.xml.internal.rngom.digested;
/*    */ 
/*    */ import com.sun.xml.internal.rngom.nc.NameClass;
/*    */ 
/*    */ public class DAttributePattern extends DXmlTokenPattern
/*    */ {
/*    */   public DAttributePattern(NameClass name)
/*    */   {
/* 55 */     super(name);
/*    */   }
/*    */   public Object accept(DPatternVisitor visitor) {
/* 58 */     return visitor.onAttribute(this);
/*    */   }
/*    */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.xml.internal.rngom.digested.DAttributePattern
 * JD-Core Version:    0.6.2
 */