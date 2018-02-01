/*    */ package com.sun.tools.example.debug.tty;
/*    */ 
/*    */ import com.sun.jdi.Field;
/*    */ import com.sun.jdi.ReferenceType;
/*    */ import com.sun.jdi.VirtualMachine;
/*    */ import com.sun.jdi.request.AccessWatchpointRequest;
/*    */ import com.sun.jdi.request.EventRequest;
/*    */ import com.sun.jdi.request.EventRequestManager;
/*    */ 
/*    */ class AccessWatchpointSpec extends WatchpointSpec
/*    */ {
/*    */   AccessWatchpointSpec(ReferenceTypeSpec paramReferenceTypeSpec, String paramString)
/*    */     throws MalformedMemberNameException
/*    */   {
/* 44 */     super(paramReferenceTypeSpec, paramString);
/*    */   }
/*    */ 
/*    */   EventRequest resolveEventRequest(ReferenceType paramReferenceType)
/*    */     throws NoSuchFieldException
/*    */   {
/* 53 */     Field localField = paramReferenceType.fieldByName(this.fieldId);
/* 54 */     EventRequestManager localEventRequestManager = paramReferenceType.virtualMachine().eventRequestManager();
/* 55 */     AccessWatchpointRequest localAccessWatchpointRequest = localEventRequestManager.createAccessWatchpointRequest(localField);
/* 56 */     localAccessWatchpointRequest.setSuspendPolicy(this.suspendPolicy);
/* 57 */     localAccessWatchpointRequest.enable();
/* 58 */     return localAccessWatchpointRequest;
/*    */   }
/*    */ 
/*    */   public String toString()
/*    */   {
/* 63 */     return MessageOutput.format("watch accesses of", new Object[] { this.refSpec
/* 64 */       .toString(), this.fieldId });
/*    */   }
/*    */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.example.debug.tty.AccessWatchpointSpec
 * JD-Core Version:    0.6.2
 */