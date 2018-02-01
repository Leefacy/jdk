/*    */ package com.sun.xml.internal.rngom.binary;
/*    */ 
/*    */ import com.sun.xml.internal.rngom.nc.ChoiceNameClass;
/*    */ import com.sun.xml.internal.rngom.nc.NameClass;
/*    */ 
/*    */ class Alphabet
/*    */ {
/*    */   private NameClass nameClass;
/*    */ 
/*    */   boolean isEmpty()
/*    */   {
/* 55 */     return this.nameClass == null;
/*    */   }
/*    */ 
/*    */   void addElement(NameClass nc) {
/* 59 */     if (this.nameClass == null)
/* 60 */       this.nameClass = nc;
/* 61 */     else if (nc != null)
/* 62 */       this.nameClass = new ChoiceNameClass(this.nameClass, nc);
/*    */   }
/*    */ 
/*    */   void addAlphabet(Alphabet a) {
/* 66 */     addElement(a.nameClass);
/*    */   }
/*    */ 
/*    */   void checkOverlap(Alphabet a) throws RestrictionViolationException {
/* 70 */     if ((this.nameClass != null) && (a.nameClass != null))
/*    */     {
/* 72 */       if (this.nameClass
/* 72 */         .hasOverlapWith(a.nameClass))
/*    */       {
/* 73 */         throw new RestrictionViolationException("interleave_element_overlap");
/*    */       }
/*    */     }
/*    */   }
/*    */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.xml.internal.rngom.binary.Alphabet
 * JD-Core Version:    0.6.2
 */