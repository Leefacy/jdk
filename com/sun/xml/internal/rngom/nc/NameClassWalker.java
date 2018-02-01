/*    */ package com.sun.xml.internal.rngom.nc;
/*    */ 
/*    */ import javax.xml.namespace.QName;
/*    */ 
/*    */ public class NameClassWalker
/*    */   implements NameClassVisitor<Void>
/*    */ {
/*    */   public Void visitChoice(NameClass nc1, NameClass nc2)
/*    */   {
/* 56 */     nc1.accept(this);
/* 57 */     return (Void)nc2.accept(this);
/*    */   }
/*    */ 
/*    */   public Void visitNsName(String ns) {
/* 61 */     return null;
/*    */   }
/*    */ 
/*    */   public Void visitNsNameExcept(String ns, NameClass nc) {
/* 65 */     return (Void)nc.accept(this);
/*    */   }
/*    */ 
/*    */   public Void visitAnyName() {
/* 69 */     return null;
/*    */   }
/*    */ 
/*    */   public Void visitAnyNameExcept(NameClass nc) {
/* 73 */     return (Void)nc.accept(this);
/*    */   }
/*    */ 
/*    */   public Void visitName(QName name) {
/* 77 */     return null;
/*    */   }
/*    */ 
/*    */   public Void visitNull() {
/* 81 */     return null;
/*    */   }
/*    */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.xml.internal.rngom.nc.NameClassWalker
 * JD-Core Version:    0.6.2
 */