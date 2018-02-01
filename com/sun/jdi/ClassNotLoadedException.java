/*    */ package com.sun.jdi;
/*    */ 
/*    */ import jdk.Exported;
/*    */ 
/*    */ @Exported
/*    */ public class ClassNotLoadedException extends Exception
/*    */ {
/*    */   private static final long serialVersionUID = -6242978768444298722L;
/*    */   private String className;
/*    */ 
/*    */   public ClassNotLoadedException(String paramString)
/*    */   {
/* 78 */     this.className = paramString;
/*    */   }
/*    */ 
/*    */   public ClassNotLoadedException(String paramString1, String paramString2) {
/* 82 */     super(paramString2);
/* 83 */     this.className = paramString1;
/*    */   }
/*    */ 
/*    */   public String className() {
/* 87 */     return this.className;
/*    */   }
/*    */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.jdi.ClassNotLoadedException
 * JD-Core Version:    0.6.2
 */