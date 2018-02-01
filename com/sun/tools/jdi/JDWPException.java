/*    */ package com.sun.tools.jdi;
/*    */ 
/*    */ import com.sun.jdi.ClassNotPreparedException;
/*    */ import com.sun.jdi.InconsistentDebugInfoException;
/*    */ import com.sun.jdi.InternalException;
/*    */ import com.sun.jdi.InvalidStackFrameException;
/*    */ import com.sun.jdi.ObjectCollectedException;
/*    */ import com.sun.jdi.VMDisconnectedException;
/*    */ import com.sun.jdi.VMOutOfMemoryException;
/*    */ 
/*    */ class JDWPException extends Exception
/*    */ {
/*    */   private static final long serialVersionUID = -6321344442751299874L;
/*    */   short errorCode;
/*    */ 
/*    */   JDWPException(short paramShort)
/*    */   {
/* 35 */     this.errorCode = paramShort;
/*    */   }
/*    */ 
/*    */   short errorCode() {
/* 39 */     return this.errorCode;
/*    */   }
/*    */ 
/*    */   RuntimeException toJDIException() {
/* 43 */     switch (this.errorCode) {
/*    */     case 20:
/* 45 */       return new ObjectCollectedException();
/*    */     case 112:
/* 47 */       return new VMDisconnectedException();
/*    */     case 110:
/* 49 */       return new VMOutOfMemoryException();
/*    */     case 22:
/* 51 */       return new ClassNotPreparedException();
/*    */     case 30:
/*    */     case 33:
/* 54 */       return new InvalidStackFrameException();
/*    */     case 99:
/* 56 */       return new UnsupportedOperationException();
/*    */     case 503:
/*    */     case 504:
/* 59 */       return new IndexOutOfBoundsException();
/*    */     case 34:
/* 61 */       return new InconsistentDebugInfoException();
/*    */     case 10:
/* 63 */       return new IllegalThreadStateException();
/*    */     }
/* 65 */     return new InternalException("Unexpected JDWP Error: " + this.errorCode, this.errorCode);
/*    */   }
/*    */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.jdi.JDWPException
 * JD-Core Version:    0.6.2
 */