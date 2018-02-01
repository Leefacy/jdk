/*    */ package com.sun.xml.internal.rngom.parse.host;
/*    */ 
/*    */ import com.sun.xml.internal.rngom.ast.builder.Annotations;
/*    */ import com.sun.xml.internal.rngom.ast.om.Location;
/*    */ 
/*    */ public class Base
/*    */ {
/* 71 */   private static final AnnotationsHost nullAnnotations = new AnnotationsHost(null, null);
/* 72 */   private static final LocationHost nullLocation = new LocationHost(null, null);
/*    */ 
/*    */   protected AnnotationsHost cast(Annotations ann)
/*    */   {
/* 58 */     if (ann == null) {
/* 59 */       return nullAnnotations;
/*    */     }
/* 61 */     return (AnnotationsHost)ann;
/*    */   }
/*    */ 
/*    */   protected LocationHost cast(Location loc) {
/* 65 */     if (loc == null) {
/* 66 */       return nullLocation;
/*    */     }
/* 68 */     return (LocationHost)loc;
/*    */   }
/*    */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.xml.internal.rngom.parse.host.Base
 * JD-Core Version:    0.6.2
 */