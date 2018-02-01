/*    */ package com.sun.xml.internal.rngom.parse.host;
/*    */ 
/*    */ import com.sun.xml.internal.rngom.ast.builder.Annotations;
/*    */ import com.sun.xml.internal.rngom.ast.builder.BuildException;
/*    */ import com.sun.xml.internal.rngom.ast.builder.Div;
/*    */ import com.sun.xml.internal.rngom.ast.om.Location;
/*    */ 
/*    */ public class DivHost extends GrammarSectionHost
/*    */   implements Div
/*    */ {
/*    */   private final Div lhs;
/*    */   private final Div rhs;
/*    */ 
/*    */   DivHost(Div lhs, Div rhs)
/*    */   {
/* 63 */     super(lhs, rhs);
/* 64 */     this.lhs = lhs;
/* 65 */     this.rhs = rhs;
/*    */   }
/*    */ 
/*    */   public void endDiv(Location _loc, Annotations _anno) throws BuildException {
/* 69 */     LocationHost loc = cast(_loc);
/* 70 */     AnnotationsHost anno = cast(_anno);
/*    */ 
/* 72 */     this.lhs.endDiv(loc.lhs, anno.lhs);
/* 73 */     this.rhs.endDiv(loc.rhs, anno.rhs);
/*    */   }
/*    */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.xml.internal.rngom.parse.host.DivHost
 * JD-Core Version:    0.6.2
 */