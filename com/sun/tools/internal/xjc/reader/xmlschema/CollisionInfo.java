/*    */ package com.sun.tools.internal.xjc.reader.xmlschema;
/*    */ 
/*    */ import org.xml.sax.Locator;
/*    */ 
/*    */ final class CollisionInfo
/*    */ {
/*    */   private final String name;
/*    */   private final Locator source1;
/*    */   private final Locator source2;
/*    */ 
/*    */   public CollisionInfo(String name, Locator source1, Locator source2)
/*    */   {
/* 42 */     this.name = name;
/* 43 */     this.source1 = source1;
/* 44 */     this.source2 = source2;
/*    */   }
/*    */ 
/*    */   public String toString()
/*    */   {
/* 51 */     return Messages.format("CollisionInfo.CollisionInfo", new Object[] { this.name, 
/* 52 */       printLocator(this.source1), 
/* 52 */       printLocator(this.source2) });
/*    */   }
/*    */ 
/*    */   private String printLocator(Locator loc) {
/* 56 */     if (loc == null) return "";
/*    */ 
/* 58 */     int line = loc.getLineNumber();
/* 59 */     String sysId = loc.getSystemId();
/* 60 */     if (sysId == null) sysId = Messages.format("CollisionInfo.UnknownFile", new Object[0]);
/*    */ 
/* 62 */     if (line != -1) {
/* 63 */       return Messages.format("CollisionInfo.LineXOfY", new Object[] { 
/* 64 */         Integer.toString(line), 
/* 64 */         sysId });
/*    */     }
/* 66 */     return sysId;
/*    */   }
/*    */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.internal.xjc.reader.xmlschema.CollisionInfo
 * JD-Core Version:    0.6.2
 */