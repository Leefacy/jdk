/*    */ package com.sun.xml.internal.rngom.binary;
/*    */ 
/*    */ import com.sun.xml.internal.rngom.binary.visitor.PatternFunction;
/*    */ import com.sun.xml.internal.rngom.binary.visitor.PatternVisitor;
/*    */ 
/*    */ public class AfterPattern extends BinaryPattern
/*    */ {
/*    */   AfterPattern(Pattern p1, Pattern p2)
/*    */   {
/* 54 */     super(false, 
/* 55 */       combineHashCode(41, p1
/* 55 */       .hashCode(), p2.hashCode()), p1, p2);
/*    */   }
/*    */ 
/*    */   boolean isNotAllowed()
/*    */   {
/* 61 */     return this.p1.isNotAllowed();
/*    */   }
/*    */ 
/*    */   public Object apply(PatternFunction f) {
/* 65 */     return f.caseAfter(this);
/*    */   }
/*    */   public void accept(PatternVisitor visitor) {
/* 68 */     visitor.visitAfter(this.p1, this.p2);
/*    */   }
/*    */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.xml.internal.rngom.binary.AfterPattern
 * JD-Core Version:    0.6.2
 */