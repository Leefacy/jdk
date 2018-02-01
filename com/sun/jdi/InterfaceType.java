/*     */ package com.sun.jdi;
/*     */ 
/*     */ import java.util.List;
/*     */ import jdk.Exported;
/*     */ 
/*     */ @Exported
/*     */ public abstract interface InterfaceType extends ReferenceType
/*     */ {
/*     */   public abstract List<InterfaceType> superinterfaces();
/*     */ 
/*     */   public abstract List<InterfaceType> subinterfaces();
/*     */ 
/*     */   public abstract List<ClassType> implementors();
/*     */ 
/*     */   public Value invokeMethod(ThreadReference paramThreadReference, Method paramMethod, List<? extends Value> paramList, int paramInt)
/*     */     throws InvalidTypeException, ClassNotLoadedException, IncompatibleThreadStateException, InvocationException
/*     */   {
/* 199 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ }

/* Location:           D:\dt\jdk\tools.jar
 * Qualified Name:     com.sun.jdi.InterfaceType
 * JD-Core Version:    0.6.2
 */