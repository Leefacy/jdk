/*    */ package com.sun.tools.jdi;
/*    */ 
/*    */ import com.sun.jdi.AbsentInformationException;
/*    */ 
/*    */ class BaseLineInfo
/*    */   implements LineInfo
/*    */ {
/*    */   private final int lineNumber;
/*    */   private final ReferenceTypeImpl declaringType;
/*    */ 
/*    */   BaseLineInfo(int paramInt, ReferenceTypeImpl paramReferenceTypeImpl)
/*    */   {
/* 36 */     this.lineNumber = paramInt;
/* 37 */     this.declaringType = paramReferenceTypeImpl;
/*    */   }
/*    */ 
/*    */   public String liStratum() {
/* 41 */     return "Java";
/*    */   }
/*    */ 
/*    */   public int liLineNumber() {
/* 45 */     return this.lineNumber;
/*    */   }
/*    */ 
/*    */   public String liSourceName() throws AbsentInformationException
/*    */   {
/* 50 */     return this.declaringType.baseSourceName();
/*    */   }
/*    */ 
/*    */   public String liSourcePath() throws AbsentInformationException
/*    */   {
/* 55 */     return this.declaringType.baseSourcePath();
/*    */   }
/*    */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.jdi.BaseLineInfo
 * JD-Core Version:    0.6.2
 */