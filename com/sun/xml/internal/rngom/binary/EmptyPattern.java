/*    */ package com.sun.xml.internal.rngom.binary;
/*    */ 
/*    */ import com.sun.xml.internal.rngom.binary.visitor.PatternFunction;
/*    */ import com.sun.xml.internal.rngom.binary.visitor.PatternVisitor;
/*    */ 
/*    */ public class EmptyPattern extends Pattern
/*    */ {
/*    */   EmptyPattern()
/*    */   {
/* 53 */     super(true, 0, 5);
/*    */   }
/*    */   boolean samePattern(Pattern other) {
/* 56 */     return other instanceof EmptyPattern;
/*    */   }
/*    */   public void accept(PatternVisitor visitor) {
/* 59 */     visitor.visitEmpty();
/*    */   }
/*    */   public Object apply(PatternFunction f) {
/* 62 */     return f.caseEmpty(this);
/*    */   }
/*    */ 
/*    */   void checkRestrictions(int context, DuplicateAttributeDetector dad, Alphabet alpha) throws RestrictionViolationException
/*    */   {
/* 67 */     switch (context) {
/*    */     case 7:
/* 69 */       throw new RestrictionViolationException("data_except_contains_empty");
/*    */     case 0:
/* 71 */       throw new RestrictionViolationException("start_contains_empty");
/*    */     }
/*    */   }
/*    */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.xml.internal.rngom.binary.EmptyPattern
 * JD-Core Version:    0.6.2
 */