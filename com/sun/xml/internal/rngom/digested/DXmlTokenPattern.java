/*    */ package com.sun.xml.internal.rngom.digested;
/*    */ 
/*    */ import com.sun.xml.internal.rngom.nc.NameClass;
/*    */ 
/*    */ public abstract class DXmlTokenPattern extends DUnaryPattern
/*    */ {
/*    */   private final NameClass name;
/*    */ 
/*    */   public DXmlTokenPattern(NameClass name)
/*    */   {
/* 57 */     this.name = name;
/*    */   }
/*    */ 
/*    */   public NameClass getName()
/*    */   {
/* 64 */     return this.name;
/*    */   }
/*    */ 
/*    */   public final boolean isNullable() {
/* 68 */     return false;
/*    */   }
/*    */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.xml.internal.rngom.digested.DXmlTokenPattern
 * JD-Core Version:    0.6.2
 */