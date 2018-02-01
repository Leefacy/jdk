/*    */ package com.sun.xml.internal.rngom.binary;
/*    */ 
/*    */ import com.sun.xml.internal.rngom.binary.visitor.PatternFunction;
/*    */ import com.sun.xml.internal.rngom.binary.visitor.PatternVisitor;
/*    */ import org.relaxng.datatype.Datatype;
/*    */ 
/*    */ public class DataPattern extends StringPattern
/*    */ {
/*    */   private Datatype dt;
/*    */ 
/*    */   DataPattern(Datatype dt)
/*    */   {
/* 56 */     super(combineHashCode(31, dt.hashCode()));
/* 57 */     this.dt = dt;
/*    */   }
/*    */ 
/*    */   boolean samePattern(Pattern other) {
/* 61 */     if (other.getClass() != getClass())
/* 62 */       return false;
/* 63 */     return this.dt.equals(((DataPattern)other).dt);
/*    */   }
/*    */ 
/*    */   public void accept(PatternVisitor visitor) {
/* 67 */     visitor.visitData(this.dt);
/*    */   }
/*    */ 
/*    */   public Object apply(PatternFunction f) {
/* 71 */     return f.caseData(this);
/*    */   }
/*    */ 
/*    */   Datatype getDatatype() {
/* 75 */     return this.dt;
/*    */   }
/*    */ 
/*    */   boolean allowsAnyString() {
/* 79 */     return false;
/*    */   }
/*    */ 
/*    */   void checkRestrictions(int context, DuplicateAttributeDetector dad, Alphabet alpha)
/*    */     throws RestrictionViolationException
/*    */   {
/* 86 */     switch (context) {
/*    */     case 0:
/* 88 */       throw new RestrictionViolationException("start_contains_data");
/*    */     }
/*    */   }
/*    */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.xml.internal.rngom.binary.DataPattern
 * JD-Core Version:    0.6.2
 */