/*    */ package com.sun.tools.example.debug.tty;
/*    */ 
/*    */ abstract class WatchpointSpec extends EventRequestSpec
/*    */ {
/*    */   final String fieldId;
/*    */ 
/*    */   WatchpointSpec(ReferenceTypeSpec paramReferenceTypeSpec, String paramString)
/*    */     throws MalformedMemberNameException
/*    */   {
/* 42 */     super(paramReferenceTypeSpec);
/* 43 */     this.fieldId = paramString;
/* 44 */     if (!isJavaIdentifier(paramString))
/* 45 */       throw new MalformedMemberNameException(paramString);
/*    */   }
/*    */ 
/*    */   public int hashCode()
/*    */   {
/* 52 */     return this.refSpec.hashCode() + this.fieldId.hashCode() + 
/* 52 */       getClass().hashCode();
/*    */   }
/*    */ 
/*    */   public boolean equals(Object paramObject)
/*    */   {
/* 57 */     if ((paramObject instanceof WatchpointSpec)) {
/* 58 */       WatchpointSpec localWatchpointSpec = (WatchpointSpec)paramObject;
/*    */ 
/* 62 */       return (this.fieldId.equals(localWatchpointSpec.fieldId)) && 
/* 61 */         (this.refSpec
/* 61 */         .equals(localWatchpointSpec.refSpec)) && 
/* 62 */         (getClass().equals(localWatchpointSpec.getClass()));
/*    */     }
/* 64 */     return false;
/*    */   }
/*    */ 
/*    */   String errorMessageFor(Exception paramException)
/*    */   {
/* 70 */     if ((paramException instanceof NoSuchFieldException)) {
/* 71 */       return MessageOutput.format("No field in", new Object[] { this.fieldId, this.refSpec
/* 72 */         .toString() });
/*    */     }
/* 74 */     return super.errorMessageFor(paramException);
/*    */   }
/*    */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.example.debug.tty.WatchpointSpec
 * JD-Core Version:    0.6.2
 */