/*    */ package com.sun.xml.internal.rngom.parse.host;
/*    */ 
/*    */ import com.sun.xml.internal.rngom.ast.om.Location;
/*    */ 
/*    */ final class LocationHost
/*    */   implements Location
/*    */ {
/*    */   final Location lhs;
/*    */   final Location rhs;
/*    */ 
/*    */   LocationHost(Location lhs, Location rhs)
/*    */   {
/* 60 */     this.lhs = lhs;
/* 61 */     this.rhs = rhs;
/*    */   }
/*    */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.xml.internal.rngom.parse.host.LocationHost
 * JD-Core Version:    0.6.2
 */