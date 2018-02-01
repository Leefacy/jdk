/*    */ package com.sun.xml.internal.xsom;
/*    */ 
/*    */ public final class XSVariety
/*    */ {
/* 35 */   public static final XSVariety ATOMIC = new XSVariety("atomic");
/* 36 */   public static final XSVariety UNION = new XSVariety("union");
/* 37 */   public static final XSVariety LIST = new XSVariety("list");
/*    */   private final String name;
/*    */ 
/*    */   private XSVariety(String _name)
/*    */   {
/* 39 */     this.name = _name;
/*    */   }
/* 41 */   public String toString() { return this.name; }
/*    */ 
/*    */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.xml.internal.xsom.XSVariety
 * JD-Core Version:    0.6.2
 */