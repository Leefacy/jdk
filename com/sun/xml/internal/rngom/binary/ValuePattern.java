/*    */ package com.sun.xml.internal.rngom.binary;
/*    */ 
/*    */ import com.sun.xml.internal.rngom.binary.visitor.PatternFunction;
/*    */ import com.sun.xml.internal.rngom.binary.visitor.PatternVisitor;
/*    */ import org.relaxng.datatype.Datatype;
/*    */ 
/*    */ public class ValuePattern extends StringPattern
/*    */ {
/*    */   Object obj;
/*    */   Datatype dt;
/*    */ 
/*    */   ValuePattern(Datatype dt, Object obj)
/*    */   {
/* 57 */     super(combineHashCode(27, obj.hashCode()));
/* 58 */     this.dt = dt;
/* 59 */     this.obj = obj;
/*    */   }
/*    */ 
/*    */   boolean samePattern(Pattern other) {
/* 63 */     if (getClass() != other.getClass())
/* 64 */       return false;
/* 65 */     if (!(other instanceof ValuePattern)) {
/* 66 */       return false;
/*    */     }
/* 68 */     return (this.dt.equals(((ValuePattern)other).dt)) && 
/* 68 */       (this.dt
/* 68 */       .sameValue(this.obj, ((ValuePattern)other).obj));
/*    */   }
/*    */ 
/*    */   public void accept(PatternVisitor visitor)
/*    */   {
/* 72 */     visitor.visitValue(this.dt, this.obj);
/*    */   }
/*    */ 
/*    */   public Object apply(PatternFunction f) {
/* 76 */     return f.caseValue(this);
/*    */   }
/*    */ 
/*    */   void checkRestrictions(int context, DuplicateAttributeDetector dad, Alphabet alpha)
/*    */     throws RestrictionViolationException
/*    */   {
/* 82 */     switch (context) {
/*    */     case 0:
/* 84 */       throw new RestrictionViolationException("start_contains_value");
/*    */     }
/*    */   }
/*    */ 
/*    */   Datatype getDatatype() {
/* 89 */     return this.dt;
/*    */   }
/*    */ 
/*    */   Object getValue() {
/* 93 */     return this.obj;
/*    */   }
/*    */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.xml.internal.rngom.binary.ValuePattern
 * JD-Core Version:    0.6.2
 */