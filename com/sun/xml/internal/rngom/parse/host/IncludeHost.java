/*    */ package com.sun.xml.internal.rngom.parse.host;
/*    */ 
/*    */ import com.sun.xml.internal.rngom.ast.builder.Annotations;
/*    */ import com.sun.xml.internal.rngom.ast.builder.BuildException;
/*    */ import com.sun.xml.internal.rngom.ast.builder.Include;
/*    */ import com.sun.xml.internal.rngom.ast.om.Location;
/*    */ import com.sun.xml.internal.rngom.parse.IllegalSchemaException;
/*    */ import com.sun.xml.internal.rngom.parse.Parseable;
/*    */ 
/*    */ public class IncludeHost extends GrammarSectionHost
/*    */   implements Include
/*    */ {
/*    */   private final Include lhs;
/*    */   private final Include rhs;
/*    */ 
/*    */   IncludeHost(Include lhs, Include rhs)
/*    */   {
/* 66 */     super(lhs, rhs);
/* 67 */     this.lhs = lhs;
/* 68 */     this.rhs = rhs;
/*    */   }
/*    */ 
/*    */   public void endInclude(Parseable current, String uri, String ns, Location _loc, Annotations _anno) throws BuildException, IllegalSchemaException {
/* 72 */     LocationHost loc = cast(_loc);
/* 73 */     AnnotationsHost anno = cast(_anno);
/*    */ 
/* 75 */     this.lhs.endInclude(current, uri, ns, loc.lhs, anno.lhs);
/* 76 */     this.rhs.endInclude(current, uri, ns, loc.rhs, anno.rhs);
/*    */   }
/*    */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.xml.internal.rngom.parse.host.IncludeHost
 * JD-Core Version:    0.6.2
 */