/*    */ package com.sun.tools.jdi;
/*    */ 
/*    */ import com.sun.jdi.AbsentInformationException;
/*    */ 
/*    */ class StratumLineInfo
/*    */   implements LineInfo
/*    */ {
/*    */   private final String stratumID;
/*    */   private final int lineNumber;
/*    */   private final String sourceName;
/*    */   private final String sourcePath;
/*    */ 
/*    */   StratumLineInfo(String paramString1, int paramInt, String paramString2, String paramString3)
/*    */   {
/* 38 */     this.stratumID = paramString1;
/* 39 */     this.lineNumber = paramInt;
/* 40 */     this.sourceName = paramString2;
/* 41 */     this.sourcePath = paramString3;
/*    */   }
/*    */ 
/*    */   public String liStratum() {
/* 45 */     return this.stratumID;
/*    */   }
/*    */ 
/*    */   public int liLineNumber() {
/* 49 */     return this.lineNumber;
/*    */   }
/*    */ 
/*    */   public String liSourceName() throws AbsentInformationException
/*    */   {
/* 54 */     if (this.sourceName == null) {
/* 55 */       throw new AbsentInformationException();
/*    */     }
/* 57 */     return this.sourceName;
/*    */   }
/*    */ 
/*    */   public String liSourcePath() throws AbsentInformationException
/*    */   {
/* 62 */     if (this.sourcePath == null) {
/* 63 */       throw new AbsentInformationException();
/*    */     }
/* 65 */     return this.sourcePath;
/*    */   }
/*    */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.jdi.StratumLineInfo
 * JD-Core Version:    0.6.2
 */