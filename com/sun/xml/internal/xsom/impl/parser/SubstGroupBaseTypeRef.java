/*    */ package com.sun.xml.internal.xsom.impl.parser;
/*    */ 
/*    */ import com.sun.xml.internal.xsom.XSElementDecl;
/*    */ import com.sun.xml.internal.xsom.XSType;
/*    */ import com.sun.xml.internal.xsom.impl.Ref.Element;
/*    */ import com.sun.xml.internal.xsom.impl.Ref.Type;
/*    */ 
/*    */ public class SubstGroupBaseTypeRef
/*    */   implements Ref.Type
/*    */ {
/*    */   private final Ref.Element e;
/*    */ 
/*    */   public SubstGroupBaseTypeRef(Ref.Element _e)
/*    */   {
/* 41 */     this.e = _e;
/*    */   }
/*    */ 
/*    */   public XSType getType() {
/* 45 */     return this.e.get().getType();
/*    */   }
/*    */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.xml.internal.xsom.impl.parser.SubstGroupBaseTypeRef
 * JD-Core Version:    0.6.2
 */