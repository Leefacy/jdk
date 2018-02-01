/*    */ package com.sun.xml.internal.rngom.digested;
/*    */ 
/*    */ import com.sun.xml.internal.rngom.nc.NameClass;
/*    */ 
/*    */ public class DElementPattern extends DXmlTokenPattern
/*    */ {
/*    */   public DElementPattern(NameClass name)
/*    */   {
/* 55 */     super(name);
/*    */   }
/*    */ 
/*    */   public Object accept(DPatternVisitor visitor) {
/* 59 */     return visitor.onElement(this);
/*    */   }
/*    */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.xml.internal.rngom.digested.DElementPattern
 * JD-Core Version:    0.6.2
 */