/*    */ package com.sun.tools.hat.internal.model;
/*    */ 
/*    */ public class StackTrace
/*    */ {
/*    */   private StackFrame[] frames;
/*    */ 
/*    */   public StackTrace(StackFrame[] paramArrayOfStackFrame)
/*    */   {
/* 50 */     this.frames = paramArrayOfStackFrame;
/*    */   }
/*    */ 
/*    */   public StackTrace traceForDepth(int paramInt)
/*    */   {
/* 59 */     if (paramInt >= this.frames.length) {
/* 60 */       return this;
/*    */     }
/* 62 */     StackFrame[] arrayOfStackFrame = new StackFrame[paramInt];
/* 63 */     System.arraycopy(this.frames, 0, arrayOfStackFrame, 0, paramInt);
/* 64 */     return new StackTrace(arrayOfStackFrame);
/*    */   }
/*    */ 
/*    */   public void resolve(Snapshot paramSnapshot)
/*    */   {
/* 69 */     for (int i = 0; i < this.frames.length; i++)
/* 70 */       this.frames[i].resolve(paramSnapshot);
/*    */   }
/*    */ 
/*    */   public StackFrame[] getFrames()
/*    */   {
/* 75 */     return this.frames;
/*    */   }
/*    */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.tools.hat.internal.model.StackTrace
 * JD-Core Version:    0.6.2
 */